package customer;

import base.url.BaseUrl;
import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Generator;

import java.util.Map;

import static org.junit.Assert.*;

public class CreateCustomerTests {

    private CustomerClient customerClient = new CustomerClient();
    Response response;
    private String token;
    @Before
    public  void setUp(){
        RestAssured.baseURI = BaseUrl.getBASE_URL();
    }
    @Test
    @DisplayName("Check status code after authorization")
    public void testCreateCustomer() {

        Customer customer = Generator.randomCustomer();
        Response response = customerClient.create(customer);

        assertEquals("Неверный статус код", HttpStatus.SC_OK, response.statusCode());

        String responseBody = response.getBody().asString();
        CustomerToken authResponse = CustomerToken.fromJson(responseBody);
        token = authResponse.getAccessToken();

    }
    @Test
    @DisplayName("Check response body structure after authorization")
    public void testCreateCustomerWithResponse() {

        Customer customer = Generator.randomCustomer();
        Response response = customerClient.create(customer);

        JsonPath jsonPath = response.jsonPath();
        assertTrue(jsonPath.getBoolean("success"));
        assertNotNull(jsonPath.getString("accessToken"));
        assertNotNull(jsonPath.getString("refreshToken"));

        // Проверяем структуру объекта "user"
        Map<String, String> user = jsonPath.getMap("user");
        assertNotNull(user);
        assertTrue(user.containsKey("email"));
        assertTrue(user.containsKey("name"));

        String responseBody = response.getBody().asString();
        CustomerToken authResponse = CustomerToken.fromJson(responseBody);
        token = authResponse.getAccessToken();
    }
    @Test
    @DisplayName("Check status code create duplicate customer")
    public void testCreateCustomerDuplicate() {
        Customer customer = Generator.randomCustomer();
        Response response = customerClient.create(customer);

        String responseBody = response.getBody().asString();
        CustomerToken authResponse = CustomerToken.fromJson(responseBody);
        token = authResponse.getAccessToken();

        Response responseDuplicate = customerClient.create(customer);

        assertEquals("Создание дубликата курьера", HttpStatus.SC_FORBIDDEN, responseDuplicate.statusCode());

    }
    @After
    public void deleteCustomer(){

        customerClient.delete(token);
    }
}
