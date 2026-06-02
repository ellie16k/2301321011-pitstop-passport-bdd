package com.pitstop;

import com.pitstop.model.User;
import com.pitstop.service.UserService;
import io.cucumber.java.After;
import io.cucumber.java.bg.*;

import static org.junit.Assert.*;

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

    @Дадено("системата не съдържа потребител с email {string}")
    public void system_no_user(String email) {
        assertNull("Очаква се системата да е празна за този email", userService.findByEmail(email));
    }

    @Дадено("потребителят {string} вече е регистриран с email {string}")
    public void user_registered_no_pass(String username, String email) {
        userService.register(username, email, "Default123");
    }

    @Дадено("потребителят {string} вече е регистриран с email {string} и парола {string}")
    public void user_registered(String username, String email, String password) {
        userService.register(username, email, password);
    }

    @Когато("потребителят се регистрира с username {string}, email {string} и парола {string}")
    public void user_registers(String username, String email, String password) {
        try {
            registeredUser = userService.register(username, email, password);
            operationSucceeded = true;
            lastErrorMessage = null;
        } catch (IllegalArgumentException e) {
            lastErrorMessage = e.getMessage();
            operationSucceeded = false;
        }
    }

    @Когато("нов потребител се опитва да се регистрира с email {string} и парола {string}")
    public void new_user_tries_register(String email, String password) {
        try {
            registeredUser = userService.register("newUser", email, password);
            operationSucceeded = true;
            lastErrorMessage = null;
        } catch (IllegalArgumentException e) {
            lastErrorMessage = e.getMessage();
            operationSucceeded = false;
        }
    }

    @Когато("потребителят влиза с email {string} и парола {string}")
    public void user_logs_in(String email, String password) {
        try {
            sessionToken = userService.login(email, password);
            operationSucceeded = true;
            lastErrorMessage = null;
        } catch (IllegalArgumentException e) {
            lastErrorMessage = e.getMessage();
            operationSucceeded = false;
        }
    }

    @То("регистрацията е успешна")
    public void registration_successful() {
        assertTrue("Очаквана успешна регистрация, но е неуспешна: " + lastErrorMessage, operationSucceeded);
        assertNotNull("Регистрираният потребител е null", registeredUser);
    }

    @То("регистрацията е неуспешна")
    public void registration_fails() {
        assertFalse("Очаквана неуспешна регистрация, но е успешна.", operationSucceeded);
    }

    @То("в системата съществува потребител с email {string}")
    public void system_contains_user(String email) {
        assertNotNull("Потребителят не е намерен в системата", userService.findByEmail(email));
    }

    @То("на потребителя е създаден празен дигитален паспорт")
    public void user_has_empty_passport() {
        assertNotNull("Паспортът на потребителя е null", registeredUser.getPassport());
        assertEquals(0, registeredUser.getPassport().countTotalStamps());
    }

    @То("входът е успешен")
    public void login_successful() {
        assertTrue("Очакван успешен вход, но е неуспешен: " + lastErrorMessage, operationSucceeded);
    }

    @То("входът е неуспешен")
    public void login_fails() {
        assertFalse("Очакван неуспешен вход, но е успешен.", operationSucceeded);
    }

    @То("системата връща валиден сесиен токен")
    public void system_returns_token() {
        assertNotNull("Токенът не трябва да е null", sessionToken);
        assertTrue("Токенът не започва с TOKEN_: " + sessionToken, sessionToken.startsWith("TOKEN_"));
    }

    private String getLastErrorAndClear() {
        if (lastErrorMessage != null) { String err = lastErrorMessage; lastErrorMessage = null; return err; }
        if (RecipeStepDefs.lastError != null) { String err = RecipeStepDefs.lastError; RecipeStepDefs.lastError = null; return err; }
        if (CheckInStepDefs.lastError != null) { String err = CheckInStepDefs.lastError; CheckInStepDefs.lastError = null; return err; }
        return null;
    }

    @То("се показва грешка {string}")
    public void error_message_is(String expectedMessage) {
        String actualError = getLastErrorAndClear();
        assertEquals(expectedMessage, actualError);
    }

    @То("се показва грешка съдържаща {string}")
    public void partial_error_is(String partialMessage) {
        String actualError = getLastErrorAndClear();
        assertNotNull("Очаквана грешка, но не е хвърлена.", actualError);
        assertTrue("Грешката '" + actualError + "' не съдържа '" + partialMessage + "'",
                actualError.contains(partialMessage));
    }
}