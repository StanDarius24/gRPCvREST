package com.stannis.protobufbench.mapper

import com.google.common.base.Throwables
import com.google.common.collect.ImmutableList
import com.google.protobuf.InvalidProtocolBufferException
import com.stannis.protobufbench.model.Ingredient
import com.stannis.protobufbench.model.IngredientUsed
import com.stannis.protobufbench.model.MeasurementType
import com.stannis.protobufbench.model.Recipe
import generated.stannis.proto.Messages


object RecipeProtoMapper {
    /**
     * Takes in a byte array (Protobuf message) and converts to a Recipe object
     *
     * @param byteRecipe byte[]
     * @return [Recipe]
     */
    fun getRecipe(byteRecipe: ByteArray?): Recipe {
        val recipe: Recipe
        try {
            val protoRecipe: Messages.Recipe = Messages.Recipe.parseFrom(byteRecipe)
            recipe = Recipe(protoRecipe.getName(), protoRecipe.getDescription())
            val ingredientsList: List<Messages.IngredientUsed> = protoRecipe.getIngredientsList()
            val builder: ImmutableList.Builder<IngredientUsed> = ImmutableList.builder<IngredientUsed>()
            for (ingredientUsed in ingredientsList) {
                val ingredient: Ingredient = getIngredientFromProtoIngredient(ingredientUsed.getIngredient())
                val type: MeasurementType = getMeasurementTypeFromProto(ingredientUsed.getType())
                builder.add(IngredientUsed(ingredient, type, ingredientUsed.getQuantity()))
            }
            recipe.addItems(builder.build())
        } catch (e: InvalidProtocolBufferException) {
            throw Throwables.propagate(e)
        }
        return recipe
    }

    fun parseAsProto(recipe: Recipe): Messages.Recipe {
        val recipeBuilder: Messages.Recipe.Builder = Messages.Recipe.newBuilder()
        recipeBuilder.setName(recipe.name)
        recipeBuilder.setDescription(recipe.description)
        val ingredientsWithQuantity: List<IngredientUsed> = recipe.getIngredientsWithQuantity()
        for (ingredientUsed in ingredientsWithQuantity) {
            val usedIngredientBuilder: Messages.IngredientUsed.Builder = Messages.IngredientUsed.newBuilder()
            usedIngredientBuilder.setIngredient(getIngredientBuilder(ingredientUsed))
            usedIngredientBuilder.setQuantity(ingredientUsed.quantity)
            usedIngredientBuilder.setType(Messages.MeasurementType.valueOf(ingredientUsed.getType().name))
            recipeBuilder.addIngredients(usedIngredientBuilder)
        }
        return recipeBuilder.build()
    }

    private fun getMeasurementTypeFromProto(type: Messages.MeasurementType): MeasurementType {
        return when (type) {
            Messages.MeasurementType.OUNCE -> MeasurementType.OUNCE
            Messages.MeasurementType.CUP -> MeasurementType.CUP
            Messages.MeasurementType.GALLON -> MeasurementType.GALLON
            Messages.MeasurementType.GRAMS -> MeasurementType.GRAMS
            Messages.MeasurementType.ITEM -> MeasurementType.ITEM
            Messages.MeasurementType.POUND -> MeasurementType.POUND
            Messages.MeasurementType.TABLESPOON -> MeasurementType.TABLESPOON
            Messages.MeasurementType.TEASPOON -> MeasurementType.TEASPOON
            else -> throw RuntimeException("Cannot figure out the type: $type")
        }
    }

    private fun getIngredientFromProtoIngredient(`in`: Messages.Ingredient): Ingredient {
        return Ingredient(`in`.getName(), `in`.getDescription())
    }

    private fun getIngredientBuilder(ingredientUsed: IngredientUsed): Messages.Ingredient.Builder {
        val ingredientBuilder: Messages.Ingredient.Builder = Messages.Ingredient.newBuilder()
        ingredientBuilder.setDescription(ingredientUsed.ingredient.description)
        ingredientBuilder.setName(ingredientUsed.ingredient.name)
        return ingredientBuilder
    }
}

