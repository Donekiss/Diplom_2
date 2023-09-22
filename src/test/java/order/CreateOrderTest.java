package order;

import base.url.BaseUrl;
import customer.Customer;
import customer.CustomerClient;
import customer.CustomerToken;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Generator;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class CreateOrderTest {

    private CustomerClient customerClient = new CustomerClient();
    private OrderClient orderClient = new OrderClient();
    private String tokenExtract;
    private static Customer customer;
    private List<String> idList;
    List<String> ingredientsList;
    @Before
    public  void setUp(){
        RestAssured.baseURI = BaseUrl.getBASE_URL();

        customer = Generator.randomCustomer();
        Response response = customerClient.create(customer);
        tokenExtract = CustomerToken.extractAccessToken(response);

        Response responseIngredients = orderClient.infoIngredients();
        idList = Generator.extractIdsFromResponse(responseIngredients);

        ingredientsList = new ArrayList<>();
        ingredientsList.add(idList.get(0));
        ingredientsList.add(idList.get(5));
    }
    @Test
    @DisplayName("Check status code and body after create order")
    public void testCreateOrder() {
        JSONObject requestBody = new JSONObject();

        String requestForBody = requestBody.put("ingredients", new JSONArray(ingredientsList)).toString();
        Response response = orderClient.create(requestForBody,tokenExtract);

        assertEquals("Неверный статус код", HttpStatus.SC_OK, response.statusCode());

        JsonPath jsonPath = response.jsonPath();
        assertTrue(jsonPath.getBoolean("success"));
        assertNotNull(jsonPath.getString("name"));

        Map<String, String> order = jsonPath.getMap("order");
        assertNotNull(order);
        assertTrue(order.containsKey("number"));
    }
    @Test
    @DisplayName("Check status code for create order with out token")
    public void testCreateOrderWithOutToken() {
        JSONObject requestBody = new JSONObject();

        String request = requestBody.put("ingredients", new JSONArray(ingredientsList)).toString();
        Response response = orderClient.create(request, "");

        assertEquals("Неверный статус код", HttpStatus.SC_OK, response.statusCode());
        JsonPath jsonPath = response.jsonPath();
        assertTrue(jsonPath.getBoolean("success"));
        assertNotNull(jsonPath.getString("name"));

        Map<String, String> order = jsonPath.getMap("order");
        assertNotNull(order);
        assertTrue(order.containsKey("number"));
    }
    @After
    public void deleteCustomer(){
            customerClient.delete(tokenExtract);

    }
}
