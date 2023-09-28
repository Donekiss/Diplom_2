package order;

import base.url.BaseUrl;
import customer.Customer;
import customer.CustomerClient;
import customer.CustomerGenerator;
import customer.CustomerToken;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static mathods.MethodsCompare.*;
import static order.OrderGenerator.createInvalidIngredients;
import static order.OrderGenerator.createNullIngredients;


public class CreateOrderErrorTests {

    private static Customer customer;
    private final CustomerClient customerClient = new CustomerClient();
    private final OrderClient orderClient = new OrderClient();
    private String tokenExtract;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUrl.getBASE_URL();

        customer = CustomerGenerator.randomCustomer();
        Response response = customerClient.create(customer);
        tokenExtract = CustomerToken.extractAccessToken(response);
    }

    @Test
    @DisplayName("Check status code for invalid ingredients")
    @Description("Checking order invalid ingredient")
    public void testCreateOrderWithInvalidIngredients() {
        List<String> invalidIngredients = createInvalidIngredients();
        JSONObject requestBody = new JSONObject();

        String requestForBody = requestBody.put("ingredients", new JSONArray(invalidIngredients)).toString();
        Response response = orderClient.create(requestForBody, tokenExtract);

        checkStatusCode(500, response);
    }

    @Test
    @DisplayName("Check status code and body for null ingredients")
    @Description("Checking order without ingredients")
    public void testCreateOrderWithOutIngredients() {
        Response response = orderClient.create(createNullIngredients(), tokenExtract);

        checkStatusCode(400, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String errorMessage = jsonPath.getString("message");
        checkErrorMassage("Ingredient ids must be provided", errorMessage);
    }

    @After
    public void deleteCustomer() {
        customerClient.delete(tokenExtract);
    }
}
