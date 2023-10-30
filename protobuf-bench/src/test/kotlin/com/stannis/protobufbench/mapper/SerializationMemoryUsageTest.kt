package com.stannis.protobufbench.mapper

import com.google.common.collect.ImmutableList
import org.hamcrest.CoreMatchers
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@SpringBootTest
@RunWith(SpringRunner::class)
class SerializationMemoryUsageTest : AbstractMemoryOprationCaptureTemplate() {
    @Autowired
    private val service: RecipeService? = null
    private var recipe: Recipe? = null
    private var recipeAsJSON: String? = null
    @Before
    fun setup() {
        val cheeseUsed = IngredientUsed(Ingredient("Cheese", "Creamy Cheese"), MeasurementType.OUNCE, 4)
        recipe = RecipeTestUtil.createRecipe(
            "My Cheese Recipe",
            "Some spicy recipe using a few items",
            ImmutableList.of(cheeseUsed)
        )
        recipeAsJSON = service.recipeAsJSON(recipe)
    }

    @Test
    fun memoryUsedDuringSerialization() {
        val memory: Long = getInitialMemory()
        val jsonResult: String = service.recipeAsJSON(recipe)
        val after: Long = getMemoryAfterOperation()
        logMemoryUsage(memory, after, "JSON: Serialization Memory Total")
        assertThat(jsonResult, CoreMatchers.`is`(CoreMatchers.notNullValue()))
    }

    @Test
    fun memoryUsedDuringDesrialization() {
        val memory: Long = getInitialMemory()
        val jsonResult: Recipe = service.getRecipe(recipeAsJSON)
        val after: Long = getMemoryAfterOperation()
        logMemoryUsage(memory, after, "JSON: Deserialization Memory Total")
        assertThat(jsonResult, CoreMatchers.`is`(CoreMatchers.notNullValue()))
    }

    @Test
    @Throws(Exception::class)
    fun memoryUsageForDeserialization() {
        val protoRecipe: Messages.Recipe = RecipeProtoMapper.parseAsProto(recipe)
        val recipeAsBytes: ByteArray = protoRecipe.toByteArray()
        val memory: Long = getInitialMemory()
        // activity
        val normalRecipe: Recipe = RecipeProtoMapper.getRecipe(recipeAsBytes)
        val after: Long = getMemoryAfterOperation()
        logMemoryUsage(memory, after, "Protobuf: Deerialization Memory Total")
        assertThat(normalRecipe, CoreMatchers.`is`(CoreMatchers.notNullValue()))
    }

    @Test
    @Throws(Exception::class)
    fun memoryUsageSerialization() {
        val memory: Long = getInitialMemory()
        val protoRecipe: Messages.Recipe = RecipeProtoMapper.parseAsProto(recipe)
        val after: Long = getMemoryAfterOperation()
        logMemoryUsage(memory, after, "Protobuf: Serialization Memory Total")
        assertThat(protoRecipe, CoreMatchers.`is`(CoreMatchers.notNullValue()))
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SerializationMemoryUsageTest::class.java)
    }
}

