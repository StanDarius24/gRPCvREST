package com.stannis.protobufbench.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.ImmutableList
import com.google.protobuf.InvalidProtocolBufferException
import com.stannis.protobufbench.model.Recipe
import generated.stannis.proto.Messages
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class RecipeServiceTest {
    private var service: RecipeService? = null
    @Before
    @Throws(Exception::class)
    fun setUp() {
        service = RecipeService(ObjectMapper())
    }

    @Test
    @Throws(Exception::class)
    fun addRecipe() {
        val recipe: Recipe = createRecipe()
        assertThat(recipe.getIngredientsWithQuantity().size(), CoreMatchers.`is`(2))
    }

    @Test
    @Throws(InvalidProtocolBufferException::class)
    fun tryProtobufSerialization() {
        val recipe: Recipe = createRecipe()
        val protoRecipe: Messages.Recipe = service!!.recipeAsProto(recipe)
        assertThat(protoRecipe.getDescription(), `is`(recipe.description)))
        assertThat(protoRecipe.getIngredientsCount(), CoreMatchers.`is`(2))
    }

    @Test
    fun fromJSON() {
        val description = "Some spicy recipe using a few items"
        val json: String = getMockRecipeJSONString(description)
        val recipe: Recipe = service!!.getRecipe(json)
        assertThat(recipe.getDescription(), CoreMatchers.`is`(description))
        assertThat(recipe.getIngredientsWithQuantity().size(), CoreMatchers.`is`(2))
    }

    @Test
    @Throws(InvalidProtocolBufferException::class)
    fun protoSerializationToFile() {
        val dateTime = LocalDateTime.now()
        val dateFormatted = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm-ss"))
        val recipe: Recipe = createRecipe()
        val protoRecipe: Messages.Recipe = service!!.recipeAsProto(recipe)
        service!!.writeRecipeToFile(String.format("target/myrecipe.bin.%s.proto", dateFormatted), protoRecipe)
    }

    private fun createRecipe(): Recipe {
        val name = "My Recipe"
        val description = "Some spicy recipe using a few items"
        val jalepeno = Ingredient("Jalepeno", "Spicy Pepper")
        val cheese = Ingredient("Cheese", "Creamy Cheese")
        val jalepenoUsed = IngredientUsed(jalepeno, MeasurementType.ITEM, 1)
        val cheeseUsed = IngredientUsed(cheese, MeasurementType.OUNCE, 4)

        // one part Jalepeno, one part Cheese
        return RecipeTestUtil.createRecipe(name, description, ImmutableList.of(jalepenoUsed, cheeseUsed))
    }
}
