package com.stannis.protobufbench.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Throwables
import com.stannis.protobufbench.mapper.RecipeProtoMapper
import com.stannis.protobufbench.model.Recipe
import generated.stannis.proto.Messages
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.FileOutputStream
import java.io.IOException


@Component
class RecipeService @Autowired constructor(private val mapper: ObjectMapper) {

    fun recipeAsProto(recipe: Recipe): Messages.Recipe {
        return RecipeProtoMapper.parseAsProto(recipe)
    }

    fun recipeAsJSON(recipeJSONForm: Recipe?): String {
        return try {
            mapper.writeValueAsString(recipeJSONForm)
        } catch (e: JsonProcessingException) {
            throw Throwables.propagate(e)
        }
    }

    fun getRecipe(jsonRecipe: String): Recipe {
        return try {
            mapper.readValue(jsonRecipe.toByteArray(), Recipe::class.java)
        } catch (e: IOException) {
            throw Throwables.propagate(e)
        }
    }

    fun getRecipe(protoRecipe: ByteArray?): Recipe {
        return RecipeProtoMapper.getRecipe(protoRecipe)
    }

    fun writeRecipeToFile(filename: String?, recipe: Messages.Recipe?) {
        try {
            FileOutputStream(filename).use { output ->
                val builder: Messages.Recipe.Builder = Messages.Recipe.newBuilder(recipe)
                builder.build().writeTo(output)
                output.close()
            }
        } catch (e: IOException) {
            throw Throwables.propagate(e)
        }
    }
}

