package utils;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import customer.Customer;
import io.restassured.response.Response;
import order.Ingredients;

import java.util.ArrayList;
import java.util.List;

public class Generator {
    public static String randomEmail() {
        Faker faker = new Faker();
        return faker.internet().safeEmailAddress();
    }
    public static String randomPassword() {
        Faker faker = new Faker();
        return faker.internet().password(8,9);
    }
    public static String randomName() {
        Faker faker = new Faker();
        return faker.name().fullName();
    }
    public static Customer randomCustomer() {
        return new Customer()
                .withEmail(randomEmail())
                .withPassword(randomPassword())
                .withName(randomName());
        }
    public static List<String> extractIdsFromResponse(Response response) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response.getBody().asString(), JsonObject.class);

        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        List<String> idList = new ArrayList<>();
        for (JsonElement element : dataArray) {
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                if (obj.has("_id")) {
                    String id = obj.get("_id").getAsString();
                    idList.add(id);
                }
            }
        }
        return idList;
    }

}
