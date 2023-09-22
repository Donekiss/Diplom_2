package customer;

import base.url.BaseUrl;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class LoginCustomerTests {
    private CustomerClient customerClient = new CustomerClient();
    private String token;
    private static Customer customer;

    @Before
    public  void setUp(){
        RestAssured.baseURI = BaseUrl.getBASE_URL();
        customer = CustomerGenerator.randomCustomer();
        customerClient.create(customer);
    }
    @Test
    @DisplayName("Check status code after login")
    @Description("Checking good login")
    public void testLoginCustomer() {
        CustomerPass courierPass = CustomerPass.passFrom(customer);
        Response response = customerClient.pass(courierPass);

        assertEquals("Неверный статус код", HttpStatus.SC_OK, response.statusCode());

        token = CustomerToken.extractAccessToken(response);
    }
    @Test
    @DisplayName("Check response body structure after login")
    @Description("Checking body response")
    public void testLoginCustomerWithResponse() {
        CustomerPass courierPass = CustomerPass.passFrom(customer);
        Response response = customerClient.pass(courierPass);

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
    @DisplayName("Check status code and body after login with mistake in email")
    @Description("Checking bad login")
    public void testLoginCustomerWithMistakeEmail() {
        CustomerPass courierPass = new CustomerPass("3" + customer.getEmail(), customer.getPassword());
        Response response = customerClient.pass(courierPass);

        assertEquals("Неверный статус код", HttpStatus.SC_UNAUTHORIZED, response.statusCode());

        JsonPath jsonPath = response.jsonPath();
        assertFalse(jsonPath.getBoolean("success"));
        String errorMessage = jsonPath.getString("message");
        assertEquals("Неверное сообщение об ошибке", "email or password are incorrect", errorMessage);
    }
    @Test
    @DisplayName("Check status code and body after login with mistake in password")
    @Description("Checking bad login")
    public void testLoginCustomerWithMistakePassword() {
        CustomerPass courierPass = new CustomerPass(customer.getEmail(), customer.getPassword() + "3");
        Response response = customerClient.pass(courierPass);

        assertEquals("Неверный статус код", HttpStatus.SC_UNAUTHORIZED, response.statusCode());

        JsonPath jsonPath = response.jsonPath();
        assertFalse(jsonPath.getBoolean("success"));
        String errorMessage = jsonPath.getString("message");
        assertEquals("Неверное сообщение об ошибке", "email or password are incorrect", errorMessage);
    }
    @After
    public void deleteCustomer(){
        if (token != null){
            customerClient.delete(token);
        }
    }
}
