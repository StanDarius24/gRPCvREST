package com.stannis.benchmark.bench;

import com.stannis.benchmark.model.Ingredient;
import com.stannis.benchmark.model.IngredientUsed;
import com.stannis.benchmark.model.MeasurementType;
import com.stannis.benchmark.model.Recipe;
import com.stannis.benchmark.service.RecipeService;
import com.stannis.benchmark.util.RecipeTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import generated.stannis.benchmark.Messages;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class SerializationBenchmark {

    private RecipeService service;
    private Recipe recipe;
    private byte[] protoRecipe;
    private String recipeAsJSON;


    @Setup(Level.Trial)
    public void setup() {
        IngredientUsed jalepenoUsed = new IngredientUsed(new Ingredient("Jalepeno", "Spicy Pepper"), MeasurementType.ITEM, 1);
        IngredientUsed cheeseUsed = new IngredientUsed(new Ingredient("Cheese", "Creamy Cheese"), MeasurementType.OUNCE, 4);

        recipe = RecipeTestUtil.createRecipe("My Recipe", "Some spicy recipe using a few items", ImmutableList.of(jalepenoUsed, cheeseUsed));
        service = new RecipeService(new ObjectMapper());

        protoRecipe = service.recipeAsProto(recipe).toByteArray();
        recipeAsJSON = service.recipeAsJSON(recipe);

    }

    @Benchmark
    public Messages.Recipe serialize_recipe_object_to_protobuf() {
        return service.recipeAsProto(recipe);
    }

    @Benchmark
    public String serialize_recipe_object_to_JSON() {
        return service.recipeAsJSON(recipe);
    }

    @Benchmark
    public Recipe deserialize_protobuf_to_recipe_object() {
        return service.getRecipe(protoRecipe);
    }

    @Benchmark
    public Recipe deserialize_json_to_recipe_object() {
        return service.getRecipe(recipeAsJSON);
    }

}
