package order;

import customer.Customer;
import customer.CustomerClient;
import customer.CustomerGenerator;
import customer.CustomerToken;
import io.restassured.response.Response;

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
        List<String> randomOrders = OrderGenerator.createRandomIngredients(responseIngredients, numberOfIngredients);

        StringBuilder requestBody = new StringBuilder();
        requestBody.append("{ \"ingredients\": [");

        for (int i = 0; i < randomOrders.size(); i++) {
            requestBody.append("\"").append(randomOrders.get(i)).append("\"");
            if (i < randomOrders.size() - 1) {
                requestBody.append(",");
            }
        }
        requestBody.append("] }");

        String requestForBody = requestBody.toString();
        return orderClient.create(requestForBody, tokenExtract);
    }

}
