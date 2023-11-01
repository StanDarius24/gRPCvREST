package com.stannis.benchmark.repository;

import com.stannis.benchmark.mapper.RecipeProtoMapper;
import com.stannis.benchmark.model.Ingredient;
import com.stannis.benchmark.model.IngredientUsed;
import com.stannis.benchmark.model.MeasurementType;
import com.stannis.benchmark.model.Recipe;
import com.stannis.benchmark.util.RecipeTestUtil;
import com.google.common.collect.ImmutableList;
import generated.stannis.benchmark.Messages;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RecipeRepositoryImplTest {

    private Recipe recipe;
    private Messages.Recipe protoRecipe;
    private String ingredientName = "Cheese";
    private String recipeName = "My Recipe";
    private int quantity = 4;

    private RecipeRepository repository;

    @Before
    public void setUp() throws Exception {
        IngredientUsed cheeseUsed = new IngredientUsed(new Ingredient(ingredientName, "Creamy Cheese"), MeasurementType.OUNCE, quantity);

        recipe = RecipeTestUtil.createRecipe(recipeName, "Some spicy recipe using a few items", ImmutableList.of(cheeseUsed));
        protoRecipe = RecipeProtoMapper.parseAsProto(recipe);

        repository = new RecipeRepositoryImpl();
    }

    @Test
    public void storeProtoBufRecipe() {
        Integer id = repository.save(protoRecipe);
        Integer second = repository.save(protoRecipe);

        assertThat(id, is(0));
        assertThat(second, is(1));
    }

    @Test
    public void getProtoBufRecipeById() {
        Integer id = repository.save(protoRecipe);

        Messages.Recipe foundById = repository.getProtoRecipeById(id);

        assertThat(foundById.getName(), is(protoRecipe.getName()));
        assertThat(foundById.getIngredientsCount(), is(1));

        Messages.IngredientUsed firstIngredient = foundById.getIngredients(0);
        assertThat(firstIngredient.getQuantity(), is(quantity));
        assertThat(firstIngredient.getIngredient().getName(), is(ingredientName));
    }


    @Test
    public void storeBothRecipes() {
        Integer rFirst = repository.save(recipe);
        Integer rSecond = repository.save(recipe);

        Integer bFirst = repository.save(protoRecipe);
        Integer bSecond = repository.save(protoRecipe);

        assertThat(rFirst, is(0));
        assertThat(rSecond, is(1));

        assertThat(bFirst, is(0));
        assertThat(bSecond, is(1));
    }

    @Test
    public void storeJavaRecipe() {
        Integer id = repository.save(recipe);
        Integer second = repository.save(recipe);

        assertThat(id, is(0));
        assertThat(second, is(1));
    }
}
