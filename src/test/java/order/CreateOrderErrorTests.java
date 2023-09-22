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
import org.junit.*;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.*;

import static org.junit.Assert.*;



public class CreateOrderErrorTests {

    private CustomerClient customerClient = new CustomerClient();
    private OrderClient orderClient = new OrderClient();
    private String tokenExtract;

    private static Customer customer;
    private List<String> idList;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUrl.getBASE_URL();

        customer = CustomerGenerator.randomCustomer();
        Response response = customerClient.create(customer);
        tokenExtract = CustomerToken.extractAccessToken(response);

        Response responseIngredients = orderClient.infoIngredients();
        idList = OrderGenerator.extractIdsFromResponse(responseIngredients);
    }

    @Test
    @DisplayName("Check status code for invalid ingredients")
    @Description("Checking order invalid ingredient")
    public void testCreateOrderWithInvalidIngredients() {
        List<String> invalidIngredients = new ArrayList<>();
        invalidIngredients.add("error000id000ingredient");

        JSONObject requestBody = new JSONObject();

        String requestForBody = requestBody.put("ingredients", new JSONArray(invalidIngredients)).toString();
        Response response = orderClient.create(requestForBody, tokenExtract);

        assertEquals("Неверный статус код", HttpStatus.SC_INTERNAL_SERVER_ERROR, response.statusCode());
    }
    @Test
    @DisplayName("Check status code and body for null ingredients")
    @Description("Checking order without ingredients")
    public void testCreateOrderWithOutIngredients() {
        Response response = orderClient.create("", tokenExtract);

        assertEquals("Неверный статус код", HttpStatus.SC_BAD_REQUEST, response.statusCode());

        JsonPath jsonPath = response.jsonPath();
        assertFalse(jsonPath.getBoolean("success"));
        assertEquals("Неверное сообщение об ошибке", "Ingredient ids must be provided", jsonPath.getString("message"));
    }

    @After
    public void deleteCustomer() {
        customerClient.delete(tokenExtract);
    }
}
