package com.stannis.protobufbench.api

import com.stannis.protobufbench.repository.RecipeRepository
import generated.stannis.proto.Messages
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/proto")
class RecipeProtoController @Autowired constructor(repository: RecipeRepository) {
    private val repository: RecipeRepository

    init {
        this.repository = repository
    }

    @GetMapping("/recipe")
    fun getRecipeById(@RequestParam("id") id: Int?): Messages.Recipe? {
        val found: Messages.Recipe? = repository.getProtoRecipeById(id!!)
        if (found != null) {
            LOG.debug("Found Protobuf Recipe [{}] with ID[{}]", found.getName(), id)
        } else {
            LOG.info("Protobuf Recipe [{}] was not found", id)
        }
        return found
    }

    @PostMapping(value = ["/add"], consumes = ["application/x-protobuf"])
    fun saveRecipe(@RequestBody recipe: Messages.Recipe): String {
        val id: Int? = repository.save(recipe)
        LOG.info("Storing new recipe [{}] with ID [{}]", recipe.getName(), id)
        return String.format("%d", id)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(RecipeProtoController::class.java)
    }
}

