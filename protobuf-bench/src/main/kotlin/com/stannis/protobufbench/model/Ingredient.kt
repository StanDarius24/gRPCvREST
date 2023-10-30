package com.stannis.protobufbench.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


class Ingredient @JsonCreator constructor(
    @param:JsonProperty("name") val name: String,
    @param:JsonProperty("description") val description: String
) {

    override fun toString(): String {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}'
    }
}

