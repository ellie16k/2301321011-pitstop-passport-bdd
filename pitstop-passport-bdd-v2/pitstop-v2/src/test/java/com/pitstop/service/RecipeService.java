package com.pitstop.service;

import com.pitstop.model.DigitalPassport;
import com.pitstop.model.Recipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeService {

    private final Map<Integer, Recipe> recipeDb = new HashMap<>();
    private final Map<Integer, List<Integer>> unlockedRecipes = new HashMap<>();

    public RecipeService() {
        recipeDb.put(1, new Recipe(1, "Rodopi",    "Rodopska Kava",         3,  "Recipe content..."));
        recipeDb.put(2, new Recipe(2, "Trakiya",   "Banica s Kashkaval",    5,  "Recipe content..."));
        recipeDb.put(3, new Recipe(3, "Dobrudzha", "Dobrudjanski Hlyab",    6,  "Recipe content..."));
        recipeDb.put(4, new Recipe(4, "Chernomorie","Chernomorska Chorba",  10, "Recipe content..."));
        recipeDb.put(5, new Recipe(5, "Sofia",     "Bansko Kapama",         15, "Recipe content..."));
    }

    public Recipe unlock(DigitalPassport passport, int recipeId) {
        Recipe recipe = recipeDb.get(recipeId);
        if (recipe == null) {
            throw new IllegalArgumentException("Retseptata ne e namerena.");
        }
        int userStamps = passport.countTotalStamps();
        if (userStamps < recipe.getRequiredStamps()) {
            throw new IllegalStateException(
                String.format("Nedostatachen broy pechati. Imash %d, nuzhdni sa %d.",
                        userStamps, recipe.getRequiredStamps()));
        }
        List<Integer> unlocked = unlockedRecipes.getOrDefault(passport.getId(), new ArrayList<>());
        if (unlocked.contains(recipeId)) {
            throw new IllegalStateException("Retseptata veche e otkluchena.");
        }
        recipe.setUnlocked(true);
        unlocked.add(recipeId);
        unlockedRecipes.put(passport.getId(), unlocked);
        return recipe;
    }

    public List<Recipe> getUnlockedRecipes(DigitalPassport passport) {
        List<Integer> ids = unlockedRecipes.getOrDefault(passport.getId(), new ArrayList<>());
        List<Recipe> result = new ArrayList<>();
        for (int id : ids) {
            result.add(recipeDb.get(id));
        }
        return result;
    }

    public Recipe findById(int id) {
        return recipeDb.get(id);
    }

    public void reset() {
        unlockedRecipes.clear();
        recipeDb.values().forEach(r -> r.setUnlocked(false));
    }
}
