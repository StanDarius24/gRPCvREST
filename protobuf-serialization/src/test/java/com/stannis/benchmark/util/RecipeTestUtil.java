package com.stannis.benchmark.util;

import com.stannis.benchmark.model.IngredientUsed;
import com.stannis.benchmark.model.Recipe;

import java.util.List;

public class RecipeTestUtil {

    public static Recipe createRecipe(String name, String description, List<IngredientUsed> ingredients) {
        Recipe recipe = new Recipe(name, description);

        if (!ingredients.isEmpty()) {
            recipe.addItems(ingredients);
        }

        return recipe;
    }


    public static String getMockRecipeJSONString(String description) {
        return "{\"name\":\"My Recipe\",\"description\":\"" + description + "\",\"ingredientsWithQuantity\":[{\"type\":\"ITEM\",\"ingredient\":{\"name\":\"Jalepeno\",\"description\":\"Spicy Pepper\"},\"quantity\":1},{\"type\":\"OUNCE\",\"ingredient\":{\"name\":\"Cheese\",\"description\":\"Creamy Cheese\"},\"quantity\":4}]}";
    }
}
