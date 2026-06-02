package com.pitstop;

import com.pitstop.model.DigitalPassport;
import com.pitstop.model.Pitstop;
import com.pitstop.model.Recipe;
import com.pitstop.model.Stamp;
import com.pitstop.model.User;
import com.pitstop.service.RecipeService;
import com.pitstop.service.UserService;

import io.cucumber.java.After;
import io.cucumber.java.bg.*;

import java.util.List;

import static org.junit.Assert.*;

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
            case "Родопска Кава":            return 1;
            case "Баница с Кашкавал":        return 2;
            case "Добруджански Хляб":        return 3;
            case "Черноморска Рибена Чорба": return 4;
            case "Банско Капама":            return 5;
            default: throw new IllegalArgumentException("Непозната рецепта: " + name);
        }
    }

    @After
    public void tearDown() {
        recipeService.reset();
    }

    @Дадено("потребителят {string} е регистриран и логнат за рецепти")
    public void user_registered_for_recipes(String username) {
        UserService userService = new UserService();
        currentUser = userService.register(username, username + System.currentTimeMillis() + "@pitstop.bg", "Pass1234");
        passport = currentUser.getPassport();
    }

    @Дадено("в системата съществува рецепта {string} от регион {string} с изискване {int} печата")
    public void system_has_recipe(String name, String region, int required) {
        int id = getRecipeIdByName(name);
        Recipe r = recipeService.findById(id);
        assertNotNull("Рецептата '" + name + "' не е намерена в системата.", r);
        assertEquals(required, r.getRequiredStamps());
    }

    @Дадено("потребителят има {int} печата в паспорта си")
    public void user_has_stamps(int count) {
        for (int i = 0; i < count; i++) {
            Pitstop mockPitstop = new Pitstop(100 + i, "Mock Pitstop " + i,
                    42.0 + i * 0.01, 24.0 + i * 0.01, "Food");
            Stamp stamp = new Stamp(i, passport.getId(), mockPitstop, "photo_" + i + ".jpg");
            passport.addStamp(stamp);
        }
        assertEquals(count, passport.countTotalStamps());
    }

    @Дадено("потребителят вече е отключил рецепта {string}")
    public void user_already_unlocked(String recipeName) {
        int id = getRecipeIdByName(recipeName);
        recipeService.unlock(passport, id);
    }

    @Когато("потребителят се опитва да отключи рецепта {string}")
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

    @Когато("потребителят преглежда своите отключени рецепти")
    public void user_views_recipes() {
        unlockedList = recipeService.getUnlockedRecipes(passport);
    }

    @То("рецептата е отключена успешно")
    public void recipe_unlocked() {
        assertTrue("Очаквано успешно отключване, но е неуспешно: " + lastError, operationSucceeded);
        assertNotNull("Отключената рецепта е null", unlockedRecipe);
        assertTrue("Рецептата трябва да е със статус 'отключена'", unlockedRecipe.isUnlocked());
    }

    @То("рецептата се добавя в списъка с отключени рецепти на потребителя")
    public void recipe_in_list() {
        List<Recipe> list = recipeService.getUnlockedRecipes(passport);
        assertTrue("Рецептата не е намерена в отключените рецепти.",
                list.stream().anyMatch(r -> r.getId() == unlockedRecipe.getId()));
    }

    @То("отключването е неуспешно")
    public void unlock_fails() {
        assertFalse("Очаквано неуспешно отключване, но е успешно.", operationSucceeded);
    }

    @То("списъкът съдържа рецептата {string}")
    public void list_contains_recipe(String recipeName) {
        assertNotNull("Списъкът с рецепти е null", unlockedList);

        // Взимаме правилното ID на рецептата спрямо българското ѝ име
        int expectedId = getRecipeIdByName(recipeName);

        // Проверяваме дали в списъка има рецепта с това ID, вместо да сравняваме стрингове
        assertTrue("Рецептата '" + recipeName + "' не е в списъка.",
                unlockedList.stream().anyMatch(r -> r.getId() == expectedId));
    }

//    @Then("се показва грешка {string}")
//    public void exact_error_is(String exactMessage) {
//        assertEquals(exactMessage, lastError);
//    }
//
//    @Then("се показва грешка съдържаща {string}")
//    public void partial_error_is(String partialMessage) {
//        assertNotNull("Очаквана грешка, но не е хвърлена.", lastError);
//        assertTrue("Грешката '" + lastError + "' не съдържа '" + partialMessage + "'",
//                lastError.contains(partialMessage));
//    }
}