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

import static customer.CustomerGenerator.randomName;
import static customer.CustomerGenerator.randomPassword;
import static mathods.MethodsCompare.*;

public class ChangCustomerDataTests {
    private static Customer customer;
    private final CustomerClient customerClient = new CustomerClient();
    private String tokenExtract;
    private String tokenRefresh;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUrl.getBASE_URL();
        customer = CustomerGenerator.randomCustomer();
        Response response = customerClient.create(customer);
        tokenExtract = CustomerToken.extractAccessToken(response);
        tokenRefresh = CustomerToken.extractRefreshToken(response);
    }

    @Test
    @DisplayName("Check status code and body after changing all customer data")
    @Description("Checking chang email and name and password")
    public void testChangAllCustomerData() {
        Customer customer = CustomerGenerator.randomCustomer();
        Response response = customerClient.modify(customer, tokenExtract);

        checkStatusCode(200, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusTrue(jsonPath);
        checkPresenceMailAndName(jsonPath);
    }

    @Test
    @DisplayName("Check status code and body after changing customer data with out authorization")
    @Description("Checking bad chang")
    public void testChangCustomerDataWithOutAuth() {
        customerClient.logout(tokenRefresh);

        Customer customer = CustomerGenerator.randomCustomer();
        Response response = customerClient.modify(customer, "");

        checkStatusCode(401, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String errorMessage = jsonPath.getString("message");
        checkErrorMassage("You should be authorised", errorMessage);
    }

    @Test
    @DisplayName("Check status code and body after changing customer data with duplicate email")
    @Description("Checking bad chang")
    public void testChangCustomerDataWithDuplicateEmail() {
        Customer customer2 = CustomerGenerator.randomCustomer();
        Response response2 = customerClient.create(customer2);
        String tokenExtract2;
        tokenExtract2 = CustomerToken.extractAccessToken(response2);

        Customer customer = new Customer()
                .withEmail(customer2.getEmail())
                .withPassword(randomPassword())
                .withName(randomName());
        Response response = customerClient.modify(customer, tokenExtract);

        checkStatusCode(403, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String errorMessage = jsonPath.getString("message");
        checkErrorMassage("User with such email already exists", errorMessage);
        customerClient.delete(tokenExtract2);
    }

    @After
    public void deleteCustomer() {
        customerClient.delete(tokenExtract);
    }
}
