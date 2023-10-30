package com.stannis.protobufbench.benchmark

import com.google.common.collect.ImmutableList
import com.google.common.collect.Lists
import com.stannis.protobufbench.model.Ingredient
import com.stannis.protobufbench.model.IngredientUsed
import com.stannis.protobufbench.model.MeasurementType
import com.stannis.protobufbench.model.Recipe
import generated.stannis.proto.Messages
import org.openjdk.jmh.annotations.*
import org.springframework.http.*
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.util.concurrent.TimeUnit


@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
class RESTBenchmark {
    private val template: RestTemplate
    private val jsonHeaders: HttpHeaders
    private val protoHeaders: HttpHeaders
    private var recipe: Recipe? = null
    private var protoRecipe: Messages.Recipe? = null

    init {
        val messageConverters: MutableList<HttpMessageConverter<*>> = Lists.newArrayList()
        messageConverters.add(ProtobufHttpMessageConverter())
        messageConverters.add(MappingJackson2HttpMessageConverter())
        template = RestTemplate(messageConverters)
        jsonHeaders = HttpHeaders()
        jsonHeaders.contentType = MediaType.APPLICATION_JSON
        protoHeaders = HttpHeaders()
        protoHeaders.contentType = ProtobufHttpMessageConverter.PROTOBUF
    }

    @Setup
    fun setup() {
        val jalepenoUsed = IngredientUsed(Ingredient("Jalepeno", "Spicy Pepper"), MeasurementType.ITEM, 1)
        val cheeseUsed = IngredientUsed(Ingredient("Cheese", "Creamy Cheese"), MeasurementType.OUNCE, 4)
        recipe = RecipeTestUtil.createRecipe(
            "My Recipe",
            "Some spicy recipe using a few items",
            ImmutableList.of(jalepenoUsed, cheeseUsed)
        )
        protoRecipe = RecipeProtoMapper.parseAsProto(recipe)
    }

    @Benchmark
    fun store_receipt_via_json(): Int? {
        val response = template.exchange(
            JSON_URL, HttpMethod.POST, HttpEntity<Any>(recipe, jsonHeaders),
            Int::class.java
        )
        return response.body
    }

    @Benchmark
    fun store_receipt_via_proto(): Int? {
        val response = template.exchange(
            PROTO_URL, HttpMethod.POST, HttpEntity<Any>(protoRecipe, protoHeaders),
            Int::class.java
        )
        return response.body
    }

    companion object {
        private const val JSON_URL = "http://localhost:8080/json/add"
        private const val PROTO_URL = "http://localhost:8080/proto/add"
    }
}

