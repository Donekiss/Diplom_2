package order;

import customer.Customer;
import customer.CustomerClient;
import customer.CustomerGenerator;
import customer.CustomerToken;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Order {

    private CustomerClient customerClient;
    private OrderClient orderClient;
    private String tokenExtract;
    private Customer customer;

    public Order() {
        customerClient = new CustomerClient();
        orderClient = new OrderClient();

        customer = CustomerGenerator.randomCustomer();
        Response response = customerClient.create(customer);
        tokenExtract = CustomerToken.extractAccessToken(response);
    }

    public Response createOrderWithRandomIngredients(int numberOfIngredients) {
        Response responseIngredients = orderClient.infoIngredients();
        List<String> randomIngredients = OrderGenerator.createRandomIngredients(responseIngredients, numberOfIngredients);

        JSONObject requestBody = new JSONObject();
        requestBody.put("ingredients", new JSONArray(randomIngredients));

        String requestForBody = requestBody.toString();
        return orderClient.create(requestForBody, tokenExtract);
    }

}
