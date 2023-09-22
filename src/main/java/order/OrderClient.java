package order;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String INFO_INGREDIENTS = "/api/ingredients";
    private static final String CREATE_ORDERS = "/api/orders";
    private static final String ALL_ORDERS = "/api/orders/all";
    private static final String CUSTOMER_ORDERS = "/api/orders";

    public Response infoIngredients(){
        return  given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(INFO_INGREDIENTS);
    }
    public Response create(String Ids, String token){

        return given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .body(Ids)
                .when()
                .post(CREATE_ORDERS);
    }
    public Response infoAllOrders(){
        return  given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(ALL_ORDERS);
    }
    public Response customerOrders(String token){
        return  given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(CUSTOMER_ORDERS);
    }

}
