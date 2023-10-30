package com.stannis.protobufbench.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.collect.Lists


class Recipe @JsonCreator constructor(
    @param:JsonProperty("name") var name: String,
    @param:JsonProperty("description") var description: String,
    @JsonProperty("ingredientsWithQuantity") ingredientsWithQuantity: List<IngredientUsed>
) {
    private var ingredientsWithQuantity: MutableList<IngredientUsed>

    constructor(name: String, description: String) : this(name, description, Lists.newArrayList<IngredientUsed>())

    init {
        this.ingredientsWithQuantity = Lists.newArrayList(ingredientsWithQuantity)
    }

    fun getIngredientsWithQuantity(): List<IngredientUsed> {
        return ingredientsWithQuantity
    }

    fun setIngredientsWithQuantity(ingredientsWithQuantity: MutableList<IngredientUsed>) {
        this.ingredientsWithQuantity = ingredientsWithQuantity
    }

    fun addItems(itemQuantity: List<IngredientUsed>?) {
        if (itemQuantity != null && itemQuantity.size > 0) {
            ingredientsWithQuantity.addAll(itemQuantity)
        }
    }

    override fun toString(): String {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ingredientsWithQuantity=" + ingredientsWithQuantity +
                '}'
    }
}

