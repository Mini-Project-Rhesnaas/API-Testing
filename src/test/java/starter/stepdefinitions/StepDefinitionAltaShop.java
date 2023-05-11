package starter.stepdefinitions;

import com.github.javafaker.Faker;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import net.serenitybdd.screenplay.Actor;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.interactions.Put;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import starter.data.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class StepDefinitionAltaShop {

    String baseURL = "https://altashop-api.fly.dev";

    User user = new User();

    @Given("{actor} call api {string} with method {string} with payload")
    public void userCallApiWithMethodWithPayload(Actor actor, String path, String method, DataTable table) {
        actor.whoCan(CallAnApi.at(baseURL));

        JSONObject bodyRequest = new JSONObject();

        List<List<String>> rowlist = table.asLists(String.class);
        List<String> headerlist = rowlist.get(0);

        List<Map<String, String>> rowMap = table.asMaps(String.class, String.class);
        Map<String, String> valueList = rowMap.get(0);

        for (int i = 0; i < valueList.size(); i++) {
            Faker faker = new Faker();

            String key = headerlist.get(i);

            switch (valueList.get(key)){
                case "randomEmail" -> {
                    String randomEmail = faker.internet().emailAddress();
                    bodyRequest.put(key, randomEmail);
                    user.setEmail(randomEmail);
                }
                case "randomPassword" -> {
                    String randomPassword = faker.internet().password();
                    bodyRequest.put(key, randomPassword);
                    user.setPassword(randomPassword);
                }
                case "randomFullName" -> {
                 String randomFullname = faker.name().fullName();
                    bodyRequest.put(key, faker.name().fullName());
                    user.setFullName(randomFullname);
                }

                case "randomProductName" -> bodyRequest.put(key, faker.commerce().productName());
                case "randomProductDescription" -> bodyRequest.put(key, faker.lorem().sentence());
                case "userEmail" -> bodyRequest.put(key, user.getEmail());
                case "userPassword" -> bodyRequest.put(key, user.getPassword());
                case "randomCategories" -> bodyRequest.put(key, faker.harryPotter().character());
                case "randomCategoriesDescription" -> bodyRequest.put(key, faker.harryPotter().house());
                default -> bodyRequest.put(key, valueList.get(key));
            }
        }

        switch (method) {
            case "GET" -> actor.attemptsTo(Get.resource(path));
            case "POST" ->
                    actor.attemptsTo(Post.to(path).with(request -> request.header("Authorization", "Bearer " + user.getAuth()).body(bodyRequest).log().all()));
            case "PUT" -> actor.attemptsTo(Put.to(path).with(request -> request.body(bodyRequest).log().all()));
            case "DELETE" -> actor.attemptsTo(Delete.from(path));
            default -> throw new IllegalStateException("Unknown Method");
        }
    }

    @And("{actor} verify status code is {int}")
    public void userVerifyStatusCodeIs(int statusCode) {
        Response response = SerenityRest.lastResponse();
        response.then().statusCode(statusCode).log().all();
    }

    @Then("{actor} verify {string}")
    public void userVerify(String schema) {
        Response response = SerenityRest.lastResponse();
        response.then().body(matchesJsonSchemaInClasspath(schema));
    }

    @And("{actor} call api {string} with method {string}")
    public void userCallApiWithMethod(Actor actor, String path, String method) {

        actor.whoCan(CallAnApi.at(baseURL));

        switch (method) {
            case "GET" ->
                    actor.attemptsTo(Get.resource(path).with(request -> request.header("Authorization", "Bearer " + user.getAuth())));
            case "POST" -> actor.attemptsTo(Post.to(path));
            case "PUT" -> actor.attemptsTo(Put.to(path));
            case "DELETE" -> actor.attemptsTo(Delete.from(path));
            default -> throw new IllegalStateException("Unknown method");
        }
    }

    @And("{actor} get user information")
    public void userGetUserInformation(Actor actor) {
        actor.whoCan(CallAnApi.at(baseURL));

        actor.attemptsTo(Get.resource("/api/auth/info")
                .with(request -> request.header("Authorization", "Bearer " + user.getAuth()).log().all()));
    }

    @Then("{actor} verify response {string}")
    public void userVerifyResponse(String schema) {
        Response response = SerenityRest.lastResponse();
        response.then().body(matchesJsonSchemaInClasspath(schema));
    }

    @And("{actor} make a new product")
    public void userMakeANewProduct(Actor actor) {
        actor.whoCan(CallAnApi.at(baseURL));
        Faker faker = new Faker();

        JSONObject bodyRequest = new JSONObject();

        List<Integer> listCategories = new ArrayList<>();
        listCategories.add(0, 1);
        listCategories.add(1, 2);
        listCategories.add(2, 3);

        bodyRequest.put("name", faker.commerce().productName());
        bodyRequest.put("description", faker.lorem().sentence());
        bodyRequest.put("price", 100);
        bodyRequest.put("categories", listCategories);

        actor.attemptsTo(Post.to("/api/products").with(request -> request.header("Authorization", "Bearer "
                + user.getAuth()).body(bodyRequest)));

    }

    @And("{actor} assign ratings")
    public void userAssignRatings(Actor actor) {
        actor.whoCan(CallAnApi.at(baseURL));

        JSONObject bodyRequest = new JSONObject();

        bodyRequest.put("count", 4);

        actor.attemptsTo(Post.to("/api/products/33159/ratings").with(request -> request.header("Authorization", "Bearer "
                + user.getAuth()).body(bodyRequest)));

    }

    @And("{actor} create a comment")
    public void userCreateAComment(Actor actor) {
        actor.whoCan(CallAnApi.at(baseURL));
        Faker faker = new Faker();


        JSONObject bodyRequest = new JSONObject();

        bodyRequest.put("content", faker.harryPotter().house());

        actor.attemptsTo(Post.to("/api/products/2/comments").with(request -> request.header("Authorization", "Bearer "
                + user.getAuth()).body(bodyRequest)));
    }

    @And("{actor} make new order")
    public void userMakeNewOrder(Actor actor) {
        actor.whoCan(CallAnApi.at(baseURL));

        JSONObject bodyRequest = new JSONObject();
        JSONArray jsonArrayWrapper = new JSONArray();

        bodyRequest.put("product_id", 2);
        bodyRequest.put("quantity", 1);

        jsonArrayWrapper.add(bodyRequest);

        actor.attemptsTo(Post.to("/api/orders").with(request -> request.header("Authorization", "Bearer "
                + user.getAuth()).body(jsonArrayWrapper)));

    }

    @And("{actor} get auth token")
    public void userGetAuthToken(Actor actor) {
        Response response = SerenityRest.lastResponse();
        user.setAuth(response.path("data"));
    }

}
