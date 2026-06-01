package com.pitstop.stepdefs;

import com.pitstop.model.User;
import com.pitstop.service.UserService;
import io.cucumber.java.After;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserStepDefs {

    private final UserService userService = new UserService();
    private User registeredUser;
    private String sessionToken;
    private String lastErrorMessage;
    private boolean operationSucceeded;

    @After
    public void tearDown() {
        userService.reset();
    }

    @Given("the system does not contain a user with email {string}")
    public void system_no_user(String email) {
        assertNull(userService.findByEmail(email));
    }

    @Given("user {string} is already registered with email {string}")
    public void user_registered_no_pass(String username, String email) {
        userService.register(username, email, "Default123");
    }

    @Given("user {string} is already registered with email {string} and password {string}")
    public void user_registered(String username, String email, String password) {
        userService.register(username, email, password);
    }

    @When("the user registers with username {string}, email {string} and password {string}")
    public void user_registers(String username, String email, String password) {
        try {
            registeredUser = userService.register(username, email, password);
            operationSucceeded = true;
        } catch (IllegalArgumentException e) {
            lastErrorMessage = e.getMessage();
            operationSucceeded = false;
        }
    }

    @When("a new user tries to register with email {string} and password {string}")
    public void new_user_tries_register(String email, String password) {
        try {
            registeredUser = userService.register("newUser", email, password);
            operationSucceeded = true;
        } catch (IllegalArgumentException e) {
            lastErrorMessage = e.getMessage();
            operationSucceeded = false;
        }
    }

    @When("the user logs in with email {string} and password {string}")
    public void user_logs_in(String email, String password) {
        try {
            sessionToken = userService.login(email, password);
            operationSucceeded = true;
        } catch (IllegalArgumentException e) {
            lastErrorMessage = e.getMessage();
            operationSucceeded = false;
        }
    }

    @Then("the registration is successful")
    public void registration_successful() {
        assertTrue(operationSucceeded, "Expected success but failed: " + lastErrorMessage);
        assertNotNull(registeredUser);
    }

    @Then("the registration fails")
    public void registration_fails() {
        assertFalse(operationSucceeded);
    }

    @Then("the system contains a user with email {string}")
    public void system_contains_user(String email) {
        assertNotNull(userService.findByEmail(email));
    }

    @Then("the user has an empty digital passport created")
    public void user_has_empty_passport() {
        assertNotNull(registeredUser.getPassport());
        assertEquals(0, registeredUser.getPassport().countTotalStamps());
    }

    @Then("the login is successful")
    public void login_successful() {
        assertTrue(operationSucceeded, "Expected login success but failed: " + lastErrorMessage);
    }

    @Then("the login fails")
    public void login_fails() {
        assertFalse(operationSucceeded);
    }

    @Then("the system returns a valid session token")
    public void system_returns_token() {
        assertNotNull(sessionToken);
        assertTrue(sessionToken.startsWith("TOKEN_"));
    }

    @Then("the error message is {string}")
    public void error_message_is(String expectedMessage) {
        assertEquals(expectedMessage, lastErrorMessage);
    }
}
