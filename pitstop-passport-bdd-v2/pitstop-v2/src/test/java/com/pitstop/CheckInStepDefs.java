package com.pitstop;

import com.pitstop.model.DigitalPassport;
import com.pitstop.model.Pitstop;
import com.pitstop.model.Stamp;
import com.pitstop.model.User;
import com.pitstop.service.LocationService;
import com.pitstop.service.UserService;
import io.cucumber.java.bg.*;

import static org.junit.Assert.*;

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

    @Дадено("потребителят {string} е регистриран и логнат")
    public void user_registered_logged_in(String username) {
        UserService userService = new UserService();
        // Добавяме уникален email, за да не се дублира при пускане на всички тестове
        currentUser = userService.register(username, username + System.currentTimeMillis() + "@pitstop.bg", "Pass1234");
        passport = currentUser.getPassport();
    }

    @Дадено("в системата съществува спирка {string} с координати {string}, {string}")
    public void system_has_pitstop(String name, String latStr, String lonStr) {
        currentPitstop = new Pitstop(1, name, Double.parseDouble(latStr), Double.parseDouble(lonStr), "Nature");
    }

    @Дадено("потребителят няма предишни печати")
    public void user_has_no_stamps() {
        assertEquals(0, passport.countTotalStamps());
    }

    @Дадено("потребителят се намира на координати {string}, {string}")
    public void user_at_coordinates(String latStr, String lonStr) {
        userLat = Double.parseDouble(latStr);
        userLon = Double.parseDouble(lonStr);
    }

    @Дадено("потребителят вече е чекирал на {string}")
    public void user_already_checked_in(String pitstopName) {
        Stamp existingStamp = new Stamp(99, passport.getId(), currentPitstop, "old_photo.jpg");
        passport.addStamp(existingStamp);
    }

    @Когато("потребителят се опитва да чекира на {string}")
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

    @Когато("системата изчислява разстоянието до спирката на координати {string}, {string}")
    public void system_calculates_distance(String pitstopLatStr, String pitstopLonStr) {
        calculatedDistance = locationService.calculateDistance(userLat, userLon, Double.parseDouble(pitstopLatStr), Double.parseDouble(pitstopLonStr));
    }

    @То("чекирането е успешно")
    public void checkin_successful() {
        assertTrue("Очаквано успешно чекиране, но е неуспешно: " + lastError, checkInSucceeded);
        assertNotNull("Печатът не трябва да е null", lastStamp);
    }

    @То("чекирането е неуспешно")
    public void checkin_fails() {
        assertFalse("Очаквано неуспешно чекиране, но е успешно.", checkInSucceeded);
    }

    @То("в паспорта се добавя нов печат за {string}")
    public void new_stamp_added(String pitstopName) {
        assertNotNull("Печатът е null", lastStamp);
        assertEquals(pitstopName, lastStamp.getPitstop().getName());
    }

    @То("броят на печатите в паспорта е {int}")
    public void stamp_count_is(int expectedCount) {
        assertEquals(expectedCount, passport.countTotalStamps());
    }

    @То("изчисленото разстояние е по-голямо от {int} метра")
    public void distance_greater_than(int metres) {
        assertTrue("Разстоянието " + calculatedDistance + " не е > " + metres,
                calculatedDistance > metres);
    }
}