package com.pitstop.stepdefs;

import com.pitstop.model.DigitalPassport;
import com.pitstop.model.Pitstop;
import com.pitstop.model.Recipe;
import com.pitstop.model.Stamp;
import com.pitstop.model.User;
import com.pitstop.service.RecipeService;
import io.cucumber.java.After;
import io.cucumber.java.en.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeStepDefs {

    private final RecipeService recipeService = new RecipeService();
    private User currentUser;
    private DigitalPassport passport;
    private Recipe unlockedRecipe;
    private List<Recipe> unlockedList;
    public static String lastError;
    private boolean operationSucceeded;

    private int getRecipeIdByName(String name) {
        switch (name) {
            case "Rodopska Kava":       return 1;
            case "Banica s Kashkaval":  return 2;
            case "Dobrudjanski Hlyab":  return 3;
            case "Chernomorska Chorba": return 4;
            case "Bansko Kapama":       return 5;
            default: throw new IllegalArgumentException("Unknown recipe: " + name);
        }
    }

    @After
    public void tearDown() {
        recipeService.reset();
    }

    @Given("the user {string} is registered and logged in for recipes")
    public void user_registered_for_recipes(String username) {
        currentUser = new User(2, username, username + "@pitstop.bg", "hashed_pass");
        passport = currentUser.getPassport();
    }

    @Given("the system has a recipe {string} from region {string} requiring {int} stamps")
    public void system_has_recipe(String name, String region, int required) {
        int id = getRecipeIdByName(name);
        Recipe r = recipeService.findById(id);
        assertNotNull(r, "Recipe '" + name + "' not found.");
        assertEquals(required, r.getRequiredStamps());
    }

    @Given("the user has {int} stamps in their passport")
    public void user_has_stamps(int count) {
        for (int i = 0; i < count; i++) {
            Pitstop mockPitstop = new Pitstop(100 + i, "Mock Pitstop " + i,
                    42.0 + i * 0.01, 24.0 + i * 0.01, "Food");
            Stamp stamp = new Stamp(i, passport.getId(), mockPitstop, "photo_" + i + ".jpg");
            passport.addStamp(stamp);
        }
        assertEquals(count, passport.countTotalStamps());
    }

    @Given("the user has already unlocked recipe {string}")
    public void user_already_unlocked(String recipeName) {
        int id = getRecipeIdByName(recipeName);
        recipeService.unlock(passport, id);
    }

    @When("the user attempts to unlock recipe {string}")
    public void user_attempts_unlock(String recipeName) {
        int id = getRecipeIdByName(recipeName);
        try {
            unlockedRecipe = recipeService.unlock(passport, id);
            operationSucceeded = true;
            lastError = null;
        } catch (IllegalStateException | IllegalArgumentException e) {
            lastError = e.getMessage();
            operationSucceeded = false;
        }
    }

    @When("the user views their unlocked recipes")
    public void user_views_recipes() {
        unlockedList = recipeService.getUnlockedRecipes(passport);
    }

    @Then("the recipe is unlocked successfully")
    public void recipe_unlocked() {
        assertTrue(operationSucceeded, "Expected success but failed: " + lastError);
        assertNotNull(unlockedRecipe);
        assertTrue(unlockedRecipe.isUnlocked());
    }

    @Then("the recipe is added to the user's unlocked recipes list")
    public void recipe_in_list() {
        List<Recipe> list = recipeService.getUnlockedRecipes(passport);
        assertTrue(list.stream().anyMatch(r -> r.getId() == unlockedRecipe.getId()));
    }

    @Then("the unlock fails")
    public void unlock_fails() {
        assertFalse(operationSucceeded);
    }

    @Then("the list contains the recipe {string}")
    public void list_contains_recipe(String recipeName) {
        assertNotNull(unlockedList);
        assertTrue(unlockedList.stream().anyMatch(r -> r.getName().equals(recipeName)),
                "Recipe '" + recipeName + "' not in list.");
    }

    @Then("the exact error is {string}")
    public void exact_error_is(String exactMessage) {
        assertEquals(exactMessage, lastError);
    }
}
