package com.stannis.benchmark.repository;

import com.stannis.benchmark.model.Recipe;
import generated.stannis.benchmark.Messages;


public interface RecipeRepository {

    Messages.Recipe getProtoRecipeById(int id);

    Integer save(Messages.Recipe recipe);

    Recipe getRecipeById(int id);

    Integer save(Recipe recipe);
}
