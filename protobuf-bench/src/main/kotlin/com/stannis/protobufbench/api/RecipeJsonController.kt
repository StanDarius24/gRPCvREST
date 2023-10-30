package com.dev9.ensor.api

import com.stannis.protobufbench.model.Recipe
import com.stannis.protobufbench.repository.RecipeRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/json")
class RecipeJsonController @Autowired constructor(repository: RecipeRepository) {

    private val repository: RecipeRepository

    init {
        this.repository = repository
    }

    @GetMapping("/recipe")
    fun getRecipeById(@RequestParam("id") id: Int): Recipe? {
        val found: Recipe? = repository.getRecipeById(id)
        if (found != null) {
            LOG.debug("Found JSON Recipe [{}] with ID[{}]", found.name, id)
        } else {
            LOG.info("JSON Recipe [{}] was not found", id)
        }
        return found
    }

    @PostMapping(value = ["/add"], consumes = ["application/json; charset=UTF-8"])
    fun saveRecipe(@RequestBody recipe: Recipe): Int {
        val id: Int? = repository.save(recipe)
        LOG.debug("Storing new recipe [{}] with ID [{}]", recipe.name, id)
        return id!!
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(RecipeJsonController::class.java)
    }
}
