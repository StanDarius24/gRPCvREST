package com.stannis.protobufbench.util

import com.stannis.protobufbench.model.IngredientUsed
import com.stannis.protobufbench.model.Recipe


object RecipeTestUtil {
    fun createRecipe(name: String, description: String, ingredients: List<IngredientUsed>): Recipe {
        val recipe = Recipe(name, description)
        if (ingredients.isNotEmpty()) {
            recipe.addItems(ingredients)
        }
        return recipe
    }

    fun getMockRecipeJSONString(description: String): String {
        return "{\"name\":\"My Recipe\"," +
                "\"description\":\"$description\"," +
                "\"ingredientsWithQuantity\":[" +
                    "{\"type\":\"ITEM\"," +
                    "\"ingredient\":{" +
                    "\"name\":\"Jalepeno\"," +
                    "\"description\":\"Spicy Pepper\"}," +
                    "\"quantity\":1}," +
                    "{\"type\":\"OUNCE\"," +
                    "\"ingredient\":{" +
                    "\"name\":\"Cheese\"," +
                    "\"description\":\"Creamy Cheese\"}," +
                    "\"quantity\":4}" +
                "]}"
    }
}

