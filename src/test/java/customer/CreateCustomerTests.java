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
import static utils.Generator.*;

public class CreateCustomerTests {

    private CustomerClient customerClient = new CustomerClient();
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

        token = CustomerToken.extractAccessToken(response);
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

        Map<String, String> user = jsonPath.getMap("user");
        assertNotNull(user);
        assertTrue(user.containsKey("email"));
        assertTrue(user.containsKey("name"));

        token = CustomerToken.extractAccessToken(response);
    }
    @Test
    @DisplayName("Check status code create duplicate customer")
    public void testCreateCustomerDuplicate() {
        Customer customer = Generator.randomCustomer();
        Response response = customerClient.create(customer);

        token = CustomerToken.extractAccessToken(response);

        Response responseDuplicate = customerClient.create(customer);

        assertEquals("Создание дубликата клиента", HttpStatus.SC_FORBIDDEN, responseDuplicate.statusCode());
    }
    @Test
    @DisplayName("Check response body after try duplicate authorization")
    public void testCreateCustomerDuplicateWithResponse() {

        Customer customer = Generator.randomCustomer();
        Response response = customerClient.create(customer);
        token = CustomerToken.extractAccessToken(response);
        Response responseDuplicate = customerClient.create(customer);

        JsonPath jsonPath = responseDuplicate.jsonPath();
        assertFalse(jsonPath.getBoolean("success"));
        String errorMessage = jsonPath.getString("message");
        assertEquals("Неверное сообщение об ошибке", "User already exists", errorMessage);
    }
    @Test
    @DisplayName("Check status code and body after authorization with out name")
    public void testCreateCustomerWithOutName() {
        Customer customer = new Customer()
                .withEmail(randomEmail())
                .withPassword(randomPassword());
        Response response = customerClient.create(customer);

        assertEquals("Неверный статус код", HttpStatus.SC_FORBIDDEN, response.statusCode());

        JsonPath jsonPath = response.jsonPath();
        assertFalse(jsonPath.getBoolean("success"));
        String errorMessage = jsonPath.getString("message");
        assertEquals("Неверное сообщение об ошибке", "Email, password and name are required fields", errorMessage);
    }
    @Test
    @DisplayName("Check status code and body after authorization with out email")
    public void testCreateCustomerWithOutEmail() {
        Customer customer = new Customer()
                .withEmail(randomName())
                .withPassword(randomPassword());
        Response response = customerClient.create(customer);

        assertEquals("Неверный статус код", HttpStatus.SC_FORBIDDEN, response.statusCode());

        JsonPath jsonPath = response.jsonPath();
        assertFalse(jsonPath.getBoolean("success"));
        String errorMessage = jsonPath.getString("message");
        assertEquals("Неверное сообщение об ошибке", "Email, password and name are required fields", errorMessage);
    }
    @Test
    @DisplayName("Check status code and body after authorization with out password")
    public void testCreateCustomerWithOutPassword() {
        Customer customer = new Customer()
                .withPassword(randomName())
                .withEmail(randomEmail());
        Response response = customerClient.create(customer);

        assertEquals("Неверный статус код", HttpStatus.SC_FORBIDDEN, response.statusCode());

        JsonPath jsonPath = response.jsonPath();
        assertFalse(jsonPath.getBoolean("success"));
        String errorMessage = jsonPath.getString("message");
        assertEquals("Неверное сообщение об ошибке", "Email, password and name are required fields", errorMessage);
    }
    @After
    public void deleteCustomer(){
        if (token != null){
        customerClient.delete(token);
        }
    }
}
