package com.pitstop.stepdefs;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.*;

public class CommonStepDefs {

    @Before
    public void resetErrors() {
        CheckInStepDefs.lastError = null;
        RecipeStepDefs.lastError = null;
    }

    @Then("the error contains {string}")
    public void error_contains(String partialMessage) {
        // Pick whichever error was set last
        String error = RecipeStepDefs.lastError != null
                ? RecipeStepDefs.lastError
                : CheckInStepDefs.lastError;
        assertNotNull(error, "Expected an error but none was thrown.");
        assertTrue(error.contains(partialMessage),
                "Error '" + error + "' does not contain '" + partialMessage + "'");
    }
}
