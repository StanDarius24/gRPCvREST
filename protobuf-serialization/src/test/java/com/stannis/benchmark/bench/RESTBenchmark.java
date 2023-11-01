package com.stannis.benchmark.bench;

import com.stannis.benchmark.mapper.RecipeProtoMapper;
import com.stannis.benchmark.model.Ingredient;
import com.stannis.benchmark.model.IngredientUsed;
import com.stannis.benchmark.model.MeasurementType;
import com.stannis.benchmark.model.Recipe;
import com.stannis.benchmark.util.RecipeTestUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import generated.stannis.benchmark.Messages;

import org.openjdk.jmh.annotations.*;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class RESTBenchmark {

    private static final String JSON_URL = "http://localhost:8080/json/add";
    private static final String PROTO_URL = "http://localhost:8080/proto/add";

    private RestTemplate template;
    private HttpHeaders jsonHeaders;
    private HttpHeaders protoHeaders;
    private Recipe recipe;
    private Messages.Recipe protoRecipe;

    public RESTBenchmark() {
        List<HttpMessageConverter<?>> messageConverters = Lists.newArrayList();
        messageConverters.add(new ProtobufHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());

        template = new RestTemplate(messageConverters);

        jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

        protoHeaders = new HttpHeaders();
        protoHeaders.setContentType(ProtobufHttpMessageConverter.PROTOBUF);
    }

    @Setup
    public void setup() {
        IngredientUsed jalepenoUsed = new IngredientUsed(new Ingredient("Jalepeno", "Spicy Pepper"), MeasurementType.ITEM, 1);
        IngredientUsed cheeseUsed = new IngredientUsed(new Ingredient("Cheese", "Creamy Cheese"), MeasurementType.OUNCE, 4);

        recipe = RecipeTestUtil.createRecipe("My Recipe", "Some spicy recipe using a few items", ImmutableList.of(jalepenoUsed, cheeseUsed));
        protoRecipe = RecipeProtoMapper.parseAsProto(recipe);
    }

    @Benchmark
    public Integer store_receipt_via_json() {
        ResponseEntity<Integer> response = template.exchange(JSON_URL, HttpMethod.POST, new HttpEntity<>(recipe, jsonHeaders), Integer.class);

        return response.getBody();
    }

    @Benchmark
    public Integer store_receipt_via_proto() {
        ResponseEntity<Integer> response = template.exchange(PROTO_URL, HttpMethod.POST, new HttpEntity<>(protoRecipe, protoHeaders), Integer.class);

        return response.getBody();
    }

}
