package com.stannis.protobufbench.repository

import com.stannis.protobufbench.model.Recipe
import generated.stannis.proto.Messages
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap


@Component
class RecipeRepositoryImpl : RecipeRepository {

    private lateinit var buffRecipes: MutableMap<Int, Messages.Recipe>

    private lateinit var recipes: MutableMap<Int, Recipe>

    init {
        buffRecipes = ConcurrentHashMap<Int, Messages.Recipe>()
        recipes = ConcurrentHashMap<Int, Recipe>()
    }

    override fun getProtoRecipeById(id: Int): Messages.Recipe? {
        return buffRecipes[id]
    }

    override fun save(recipe: Messages.Recipe?): Int {
        val size = buffRecipes.size
        buffRecipes[size] = recipe!!
        return size
    }

    override fun getRecipeById(id: Int): Recipe? {
        return recipes[id]
    }

    override fun save(recipe: Recipe?): Int? {
        val size = recipes.size
        recipes[size] = recipe!!
        return size
    }
}

