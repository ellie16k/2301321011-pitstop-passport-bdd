package com.pitstop.stepdefs;

import com.pitstop.model.DigitalPassport;
import com.pitstop.model.Pitstop;
import com.pitstop.model.Stamp;
import com.pitstop.model.User;
import com.pitstop.service.LocationService;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

public class CheckInStepDefs {

    private final LocationService locationService = new LocationService();
    private User currentUser;
    private DigitalPassport passport;
    private Pitstop currentPitstop;
    private double userLat;
    private double userLon;
    private Stamp lastStamp;
    public static String lastError;
    private boolean checkInSucceeded;
    private double calculatedDistance;

    @Given("the user {string} is registered and logged in")
    public void user_registered_logged_in(String username) {
        currentUser = new User(1, username, username + "@pitstop.bg", "hashed_pass");
        passport = currentUser.getPassport();
    }

    @Given("the system has a pitstop {string} with coordinates {double}, {double}")
    public void system_has_pitstop(String name, double lat, double lon) {
        currentPitstop = new Pitstop(1, name, lat, lon, "Nature");
    }

    @Given("the user has no previous stamps")
    public void user_has_no_stamps() {
        assertEquals(0, passport.countTotalStamps());
    }

    @Given("the user is at coordinates {double}, {double}")
    public void user_at_coordinates(double lat, double lon) {
        userLat = lat;
        userLon = lon;
    }

    @Given("the user has already checked in at {string}")
    public void user_already_checked_in(String pitstopName) {
        Stamp existingStamp = new Stamp(99, passport.getId(), currentPitstop, "old_photo.jpg");
        passport.addStamp(existingStamp);
    }

    @When("the user attempts to check in at {string}")
    public void user_attempts_checkin(String pitstopName) {
        try {
            lastStamp = locationService.checkIn(passport, currentPitstop, userLat, userLon, "photo.jpg");
            checkInSucceeded = true;
            lastError = null;
        } catch (IllegalStateException e) {
            lastError = e.getMessage();
            checkInSucceeded = false;
        }
    }

    @When("the system calculates the distance to the pitstop at coordinates {double}, {double}")
    public void system_calculates_distance(double pitstopLat, double pitstopLon) {
        calculatedDistance = locationService.calculateDistance(userLat, userLon, pitstopLat, pitstopLon);
    }

    @Then("the check-in is successful")
    public void checkin_successful() {
        assertTrue(checkInSucceeded, "Expected success but failed: " + lastError);
        assertNotNull(lastStamp);
    }

    @Then("the check-in fails")
    public void checkin_fails() {
        assertFalse(checkInSucceeded);
    }

    @Then("a new stamp for {string} is added to the passport")
    public void new_stamp_added(String pitstopName) {
        assertNotNull(lastStamp);
        assertEquals(pitstopName, lastStamp.getPitstop().getName());
    }

    @Then("the stamp count in the passport is {int}")
    public void stamp_count_is(int expectedCount) {
        assertEquals(expectedCount, passport.countTotalStamps());
    }

    @Then("the calculated distance is greater than {int} metres")
    public void distance_greater_than(int metres) {
        assertTrue(calculatedDistance > metres,
                "Distance " + calculatedDistance + " is not > " + metres);
    }
}
