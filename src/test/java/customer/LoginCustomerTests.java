package customer;

import base.url.BaseUrl;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static customer.CustomerPass.passFromWithMistakeEmail;
import static customer.CustomerPass.passFromWithMistakePassword;
import static mathods.MethodsCompare.*;

public class LoginCustomerTests {
    private static Customer customer;
    private final CustomerClient customerClient = new CustomerClient();
    private String token;

    @Before
    public void setUp() {
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

        checkStatusCode(200, response);

        token = CustomerToken.extractAccessToken(response);
    }

    @Test
    @DisplayName("Check response body structure after login")
    @Description("Checking body response")
    public void testLoginCustomerWithResponse() {
        CustomerPass courierPass = CustomerPass.passFrom(customer);
        Response response = customerClient.pass(courierPass);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusTrue(jsonPath);
        checkNotNullAccessToken(jsonPath);
        checkNotNullRefreshToken(jsonPath);
        checkPresenceMailAndName(jsonPath);

        token = CustomerToken.extractAccessToken(response);
    }

    @Test
    @DisplayName("Check status code and body after login with mistake in email")
    @Description("Checking bad login")
    public void testLoginCustomerWithMistakeEmail() {
        CustomerPass courierPass = passFromWithMistakeEmail("3", customer);
        Response response = customerClient.pass(courierPass);

        checkStatusCode(401, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String errorMessage = jsonPath.getString("message");
        checkErrorMassage("email or password are incorrect", errorMessage);
    }

    @Test
    @DisplayName("Check status code and body after login with mistake in password")
    @Description("Checking bad login")
    public void testLoginCustomerWithMistakePassword() {
        CustomerPass courierPass = passFromWithMistakePassword("3", customer);
        Response response = customerClient.pass(courierPass);

        checkStatusCode(401, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String errorMessage = jsonPath.getString("message");
        checkErrorMassage("email or password are incorrect", errorMessage);
    }

    @After
    public void deleteCustomer() {
        if (token != null) {
            customerClient.delete(token);
        }
    }
}
