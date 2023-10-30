package com.stannis.protobufbench.mapper

import com.google.common.collect.ImmutableList
import com.stannis.protobufbench.model.Ingredient
import com.stannis.protobufbench.model.IngredientUsed
import com.stannis.protobufbench.model.MeasurementType
import com.stannis.protobufbench.model.Recipe
import generated.stannis.proto.Messages
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory


class RecipeProtoMapperTest : MemoryTest {
    private var recipe: Recipe? = null
    private val ingredientName = "Cheese"
    private val recipeName = "My Recipe"
    private val quantity = 4
    @Before
    fun setup() {
        val cheeseUsed = IngredientUsed(Ingredient(ingredientName, "Creamy Cheese"), MeasurementType.OUNCE, quantity)
        recipe =
            RecipeTestUtil.createRecipe(recipeName, "Some spicy recipe using a few items", ImmutableList.of(cheeseUsed))
    }

    @Test
    @Throws(Exception::class)
    fun parseAsProto() {
        val recipe: Messages.Recipe = RecipeProtoMapper.parseAsProto(recipe)
        assertThat(recipe.getIngredientsCount(), CoreMatchers.`is`(1))
        val ingredient: Messages.IngredientUsed = recipe.getIngredients(0)
        assertThat(ingredient.getQuantity(), CoreMatchers.`is`(quantity))
        assertThat(ingredient.getIngredient().getName(), CoreMatchers.`is`(ingredientName))
        assertThat(ingredient.getType(), CoreMatchers.`is`(Messages.MeasurementType.OUNCE))
    }

    @Test
    @Throws(Exception::class)
    fun getRecipe() {
        val protoRecipe: Messages.Recipe = RecipeProtoMapper.parseAsProto(recipe)
        val recipeAsBytes: ByteArray = protoRecipe.toByteArray()
        val recipe: Recipe = RecipeProtoMapper.getRecipe(recipeAsBytes)
        assertThat(recipe.name, CoreMatchers.`is`(recipeName))
        val ingredientsWithQuantity: List<IngredientUsed> = recipe.getIngredientsWithQuantity()
        assertThat(ingredientsWithQuantity.size, CoreMatchers.`is`(1))
        val item: IngredientUsed = ingredientsWithQuantity[0]
        assertThat(item.quantity, CoreMatchers.`is`(quantity))
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(RecipeProtoMapperTest::class.java)
    }
}
