package customer;

import base.url.BaseUrl;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Generator;

import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static utils.Generator.*;

public class ChangCustomerDataTests {
    private CustomerClient customerClient = new CustomerClient();
    private String tokenExtract;
    private String tokenRefresh;
    private static Customer customer;
    @Before
    public  void setUp(){
        RestAssured.baseURI = BaseUrl.getBASE_URL();
        customer = Generator.randomCustomer();
        Response response = customerClient.create(customer);
        tokenExtract = CustomerToken.extractAccessToken(response);
        tokenRefresh = CustomerToken.extractRefreshToken(response);
    }
    @Test
    @DisplayName("Check status code and body after changing all customer data")
    public void testChangAllCustomerData() {
        Customer customer = Generator.randomCustomer();
        Response response = customerClient.modify(customer, tokenExtract);

        assertEquals("Неверный статус код", HttpStatus.SC_OK, response.statusCode());

        JsonPath jsonPath = response.jsonPath();
        assertTrue(jsonPath.getBoolean("success"));

        Map<String, String> user = jsonPath.getMap("user");
        assertNotNull(user);
        assertTrue(user.containsKey("email"));
        assertTrue(user.containsKey("name"));
    }
    @Test
    @DisplayName("Check status code and body after changing customer data with out authorization")
    public void testChangCustomerDataWithOutAuth() {
        customerClient.logout(tokenRefresh);

        Customer customer = Generator.randomCustomer();
        Response response = customerClient.modify(customer, "");

        assertEquals("Неверный статус код", HttpStatus.SC_UNAUTHORIZED, response.statusCode());

        JsonPath jsonPath = response.jsonPath();
        assertFalse(jsonPath.getBoolean("success"));
        String errorMessage = jsonPath.getString("message");
        assertEquals("Неверное сообщение об ошибке", "You should be authorised", errorMessage);
    }
    @Test
    @DisplayName("Check status code and body after changing customer data with duplicate email")
    public void testChangCustomerDataWithDuplicateEmail() {
        Customer customer2 = Generator.randomCustomer();
        Response response2 = customerClient.create(customer2);
        String tokenExtract2;
        tokenExtract2 = CustomerToken.extractAccessToken(response2);

        Customer customer = new Customer()
                .withEmail(customer2.getEmail())
                .withPassword(randomPassword())
                .withName(randomName());
        Response response = customerClient.modify(customer, tokenExtract);

        assertEquals("Неверный статус код", HttpStatus.SC_FORBIDDEN, response.statusCode());

        JsonPath jsonPath = response.jsonPath();
        assertFalse(jsonPath.getBoolean("success"));
        String errorMessage = jsonPath.getString("message");
        assertEquals("Неверное сообщение об ошибке", "User with such email already exists", errorMessage);

        customerClient.delete(tokenExtract2);
    }
    @After
    public void deleteCustomer(){
            customerClient.delete(tokenExtract);
    }
}
