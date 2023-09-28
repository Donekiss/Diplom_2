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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static mathods.MethodsCompare.*;

public class GettingCustomerOrdersTest {
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
        randomIngredients = OrderGenerator.createRandomIngredients(responseIngredients, 4);
    }

    @Test
    @DisplayName("Check status code for taken orders of the customer")
    @Description("Checking orders from customer")
    public void testGetCustomerOrdersCode() {
        Response response = orderClient.customerOrders(tokenExtract);

        checkStatusCode(200, response);
    }

    @Test
    @DisplayName("Check status body for taken orders of the customer")
    @Description("Checking response body")
    public void testGetCustomerOrdersBody() {
        Response response = orderClient.customerOrders(tokenExtract);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusTrue(jsonPath);
        checkNotNullValueList("orders", jsonPath);
        checkNotNullValueString("total", jsonPath);
        checkNotNullValueString("totalToday", jsonPath);
    }

    @Test
    @DisplayName("Check status code and body for taken orders of the customer with out authorization")
    @Description("Checking bad response")
    public void testGetCustomerOrdersWithOutAuthorization() {
        Response response = orderClient.customerOrders("");

        checkStatusCode(401, response);

        JsonPath jsonPath = response.jsonPath();
        checkSuccessStatusFalse(jsonPath);
        String errorMessage = jsonPath.getString("message");
        checkErrorMassage("You should be authorised", errorMessage);
    }

    @After
    public void deleteCustomer() {
        customerClient.delete(tokenExtract);
    }
}
