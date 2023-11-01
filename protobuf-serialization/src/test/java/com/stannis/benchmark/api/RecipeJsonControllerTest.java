package com.stannis.benchmark.api;

import com.stannis.benchmark.ProtobufSerializationApplication;
import com.stannis.benchmark.model.Recipe;
import com.google.common.collect.ImmutableMap;
import com.stannis.benchmark.util.RecipeTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@SpringBootTest(classes = ProtobufSerializationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class RecipeJsonControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void getRecipeById() throws Exception {
        String description = "Recipe Description Here";
        ResponseEntity<Integer> response = createRecipeByURL(RecipeTestUtil.getMockRecipeJSONString(description));

        Integer createdID = response.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Recipe> foundRecipe = template.exchange("/json/recipe?id={id}", HttpMethod.GET, new HttpEntity<String>(headers), Recipe.class, ImmutableMap.of("id", createdID));

        assertThat(foundRecipe, is(notNullValue()));

        Recipe recipe = foundRecipe.getBody();

        assertThat(recipe.getDescription(), is(description));
    }

    @Test
    public void saveRecipe() throws Exception {
        ResponseEntity<Integer> response = createRecipeByURL(RecipeTestUtil.getMockRecipeJSONString("Recipe Description Here"));

        assertThat(response, is(notNullValue()));
        assertThat(response.getBody(), is(greaterThanOrEqualTo(0)));
    }

    private ResponseEntity<Integer> createRecipeByURL(String jsonString) {
        String url = "/json/add";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Integer> response = template.exchange(url, HttpMethod.POST, new HttpEntity<>(jsonString, headers), Integer.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        return response;
    }


}
