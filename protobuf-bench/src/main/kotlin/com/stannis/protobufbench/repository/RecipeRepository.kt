package com.stannis.protobufbench.repository

import com.stannis.protobufbench.model.Recipe
import generated.stannis.proto.Messages


interface RecipeRepository {

    fun getProtoRecipeById(id: Int): Messages.Recipe?

    fun save(recipe: Messages.Recipe?): Int?

    fun getRecipeById(id: Int): Recipe?

    fun save(recipe: Recipe?): Int?

}
