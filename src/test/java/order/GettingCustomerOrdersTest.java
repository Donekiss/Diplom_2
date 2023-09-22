package order;

import base.url.BaseUrl;
import customer.Customer;
import customer.CustomerClient;
import customer.CustomerGenerator;
import customer.CustomerToken;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GettingCustomerOrdersTest {
    private CustomerClient customerClient = new CustomerClient();
    private OrderClient orderClient = new OrderClient();
    private String tokenExtract;
    private static Customer customer;
    private List<String> randomIngredients;

    @Before
    public void setUp() {
        RestAssured.baseURI = BaseUrl.getBASE_URL();

        customer = CustomerGenerator.randomCustomer();
        Response response = customerClient.create(customer);
        tokenExtract = CustomerToken.extractAccessToken(response);

        Response responseIngredients = orderClient.infoIngredients();
        randomIngredients = OrderGenerator.createRandomIngredients(responseIngredients, 3);

        // Создайте несколько заказов для этого клиента здесь
        // Используйте orderClient.create для создания заказов
    }

    @Test
    @DisplayName("Check status code and body for taken orders of the customer")
    public void testGetCustomerOrders() {
        Response response = orderClient.customerOrders(tokenExtract);

        assertEquals("Неверный статус код", HttpStatus.SC_OK, response.statusCode());

        JsonPath jsonPath = response.jsonPath();
        assertTrue(jsonPath.getBoolean("success"));
        assertNotNull(jsonPath.getList("orders"));

        // Проверьте структуру ответа, чтобы убедиться, что она соответствует ожиданиям
        // Можете также проверить, что количество заказов и их детали соответствуют созданным заказам
    }

    @After
    public void deleteCustomer() {
        customerClient.delete(tokenExtract);
    }
}
