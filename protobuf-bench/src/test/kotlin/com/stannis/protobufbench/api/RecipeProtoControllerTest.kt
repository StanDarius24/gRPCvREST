package com.stannis.protobufbench.api

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter
import org.springframework.test.context.junit4.SpringRunner


@SpringBootTest(
    classes = [ProtobufSerializationApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@RunWith(
    SpringRunner::class
)
class RecipeProtoControllerTest {
    @Autowired
    private val template: TestRestTemplate? = null

    @get:Throws(Exception::class)
    @get:Test
    val recipeById: Unit
        get() {
            val recipeName = "My Recipe Here"
            val response = createRecipeMessage(recipeName)
            val createdID = response.body
            val headers = HttpHeaders()
            headers.contentType = ProtobufHttpMessageConverter.PROTOBUF
            val foundRecipe: ResponseEntity<Messages.Recipe> = template.exchange(
                "/proto/recipe?id={id}", HttpMethod.GET, HttpEntity<String>(headers),
                Messages.Recipe::class.java, ImmutableMap.of("id", createdID)
            )
            MatcherAssert.assertThat<ResponseEntity<Messages.Recipe>>(
                foundRecipe,
                CoreMatchers.`is`<Any>(CoreMatchers.notNullValue())
            )
            val recipe: Messages.Recipe? = foundRecipe.getBody()
            assertThat(recipe.getName(), CoreMatchers.`is`(recipeName))
        }

    @Test
    @Throws(Exception::class)
    fun saveRecipe() {
        val response = createRecipeMessage("My Proto Recipe")
        MatcherAssert.assertThat(response, CoreMatchers.`is`(CoreMatchers.notNullValue()))
        MatcherAssert.assertThat(response.body, CoreMatchers.`is`(Matchers.greaterThanOrEqualTo(0)))
    }

    private fun createRecipeMessage(recipeName: String): ResponseEntity<Int> {
        val url = "/proto/add"
        val recipe: Messages.Recipe = RecipeProtoMapper.parseAsProto(getNewRecipe(recipeName))
        val headers = HttpHeaders()
        headers.contentType = ProtobufHttpMessageConverter.PROTOBUF
        val response = template!!.exchange(
            url, HttpMethod.POST, HttpEntity<Any>(recipe, headers),
            Int::class.java
        )
        MatcherAssert.assertThat(response.statusCode, CoreMatchers.`is`(HttpStatus.OK))
        return response
    }

    private fun getNewRecipe(recipeName: String): Recipe {
        val cheeseUsed = IngredientUsed(Ingredient("Cheese", "Creamy Cheese"), MeasurementType.OUNCE, 4)
        return RecipeTestUtil.createRecipe(
            recipeName,
            "Some spicy recipe using a few items",
            ImmutableList.of(cheeseUsed)
        )
    }
}
