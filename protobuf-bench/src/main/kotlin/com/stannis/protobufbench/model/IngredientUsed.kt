package com.stannis.protobufbench.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


class IngredientUsed @JsonCreator constructor(
    @param:JsonProperty("ingredient") val ingredient: Ingredient,
    @JsonProperty("type") type: MeasurementType,
    @JsonProperty("quantity") quantity: Int
) {
    private val type: MeasurementType
    val quantity: Int

    init {
        this.type = type
        this.quantity = quantity
    }

    fun getType(): MeasurementType {
        return type
    }

    override fun toString(): String {
        return "IngredientUsed{" +
                "type=" + type +
                ", ingredient=" + ingredient +
                ", quantity=" + quantity +
                '}'
    }
}

