package com.stannis.protobufbench.api

import com.google.common.collect.ImmutableMap
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.junit4.SpringRunner


@SpringBootTest(
    classes = [ProtobufSerializationApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@RunWith(
    SpringRunner::class
)
class RecipeJsonControllerTest {
    @Autowired
    private val template: TestRestTemplate? = null

    @get:Throws(Exception::class)
    @get:Test
    val recipeById: Unit
        get() {
            val description = "Recipe Description Here"
            val response = createRecipeByURL(getMockRecipeJSONString(description))
            val createdID = response.body
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON
            val foundRecipe: ResponseEntity<Recipe> = template.exchange(
                "/json/recipe?id={id}", HttpMethod.GET, HttpEntity<String>(headers),
                Recipe::class.java, ImmutableMap.of("id", createdID)
            )
            MatcherAssert.assertThat<ResponseEntity<Recipe>>(
                foundRecipe,
                CoreMatchers.`is`<Any>(CoreMatchers.notNullValue())
            )
            val recipe: Recipe? = foundRecipe.getBody()
            assertThat(recipe.getDescription(), CoreMatchers.`is`(description))
        }

    @Test
    @Throws(Exception::class)
    fun saveRecipe() {
        val response = createRecipeByURL(getMockRecipeJSONString("Recipe Description Here"))
        MatcherAssert.assertThat(response, CoreMatchers.`is`(CoreMatchers.notNullValue()))
        MatcherAssert.assertThat(response.body, CoreMatchers.`is`(Matchers.greaterThanOrEqualTo(0)))
    }

    private fun createRecipeByURL(jsonString: String): ResponseEntity<Int> {
        val url = "/json/add"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val response = template!!.exchange(
            url, HttpMethod.POST, HttpEntity(jsonString, headers),
            Int::class.java
        )
        MatcherAssert.assertThat(response.statusCode, CoreMatchers.`is`(HttpStatus.OK))
        return response
    }
}
