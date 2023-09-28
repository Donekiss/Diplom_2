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

public class CreateOrderTest {

    private static Customer customer;
    private final CustomerClient customerClient = new CustomerClient();
    private final OrderClient orderClient = new OrderClient();
    private String tokenExtract;
    private List<String> randomIngredients;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUrl.getBASE_URL();

        customer = CustomerGenerator.randomCustomer();
        Response response = customerClient.create(customer);
        tokenExtract = CustomerToken.extractAccessToken(response);

        Response responseIngredients = orderClient.infoIngredients();
        randomIngredients = OrderGenerator.createRandomIngredients(responseIngredients, 3);
    }

    @Test
    @DisplayName("Check status code and body after create order")
    @Description("Checking create order")
    public void testCreateOrder() {
        JSONObject requestBody = new JSONObject();

        String requestForBody = requestBody.put("ingredients", new JSONArray(randomIngredients)).toString();
        Response response = orderClient.create(requestForBody, tokenExtract);

        checkStatusCode(200, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusTrue(jsonPath);
        checkNotNullValueString("name", jsonPath);

        checkPresenceNumberOrder(jsonPath);
    }

    @Test
    @DisplayName("Check status code for create order with out token")
    @Description("Checking bad response")
    public void testCreateOrderWithOutToken() {
        JSONObject requestBody = new JSONObject();

        String request = requestBody.put("ingredients", new JSONArray(randomIngredients)).toString();
        Response response = orderClient.create(request, "");

        checkStatusCode(200, response);
        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusTrue(jsonPath);
        checkNotNullValueString("name", jsonPath);

        checkPresenceNumberOrder(jsonPath);
    }

    @After
    public void deleteCustomer() {
        customerClient.delete(tokenExtract);

    }
}
