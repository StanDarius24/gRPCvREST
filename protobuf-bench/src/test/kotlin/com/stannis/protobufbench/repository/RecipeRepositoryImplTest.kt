package com.stannis.protobufbench.repository

import com.google.common.collect.ImmutableList
import org.hamcrest.CoreMatchers


class RecipeRepositoryImplTest {
    private var recipe: Recipe? = null
    private var protoRecipe: Messages.Recipe? = null
    private val ingredientName = "Cheese"
    private val recipeName = "My Recipe"
    private val quantity = 4
    private var repository: RecipeRepository? = null
    @Before
    @Throws(Exception::class)
    fun setUp() {
        val cheeseUsed = IngredientUsed(Ingredient(ingredientName, "Creamy Cheese"), MeasurementType.OUNCE, quantity)
        recipe =
            RecipeTestUtil.createRecipe(recipeName, "Some spicy recipe using a few items", ImmutableList.of(cheeseUsed))
        protoRecipe = RecipeProtoMapper.parseAsProto(recipe)
        repository = RecipeRepositoryImpl()
    }

    @Test
    fun storeProtoBufRecipe() {
        val id: Int = repository.save(protoRecipe)
        val second: Int = repository.save(protoRecipe)
        assertThat(id, CoreMatchers.`is`(0))
        assertThat(second, CoreMatchers.`is`(1))
    }

    @get:Test
    val protoBufRecipeById: Unit
        get() {
            val id: Int = repository.save(protoRecipe)
            val foundById: Messages.Recipe? = repository!!.getProtoRecipeById(id)
            assertThat(foundById.getName(), `is`(protoRecipe.getName()))
            assertThat(foundById.getIngredientsCount(), CoreMatchers.`is`(1))
            val firstIngredient: Messages.IngredientUsed = foundById.getIngredients(0)
            assertThat(firstIngredient.getQuantity(), CoreMatchers.`is`(quantity))
            assertThat(firstIngredient.getIngredient().getName(), CoreMatchers.`is`(ingredientName))
        }

    @Test
    fun storeBothRecipes() {
        val rFirst: Int = repository.save(recipe)
        val rSecond: Int = repository.save(recipe)
        val bFirst: Int = repository.save(protoRecipe)
        val bSecond: Int = repository.save(protoRecipe)
        assertThat(rFirst, CoreMatchers.`is`(0))
        assertThat(rSecond, CoreMatchers.`is`(1))
        assertThat(bFirst, CoreMatchers.`is`(0))
        assertThat(bSecond, CoreMatchers.`is`(1))
    }

    @Test
    fun storeJavaRecipe() {
        val id: Int = repository.save(recipe)
        val second: Int = repository.save(recipe)
        assertThat(id, CoreMatchers.`is`(0))
        assertThat(second, CoreMatchers.`is`(1))
    }
}
