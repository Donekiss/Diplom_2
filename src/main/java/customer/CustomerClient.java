package customer;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CustomerClient {
    private static final String CREATE_CUSTOMER = "/api/auth/register";
    private static final String PASS_CUSTOMER = "/api/auth/login";
    private static final String MODIFY_CUSTOMER = "/api/auth/user";
    private static final String DELETE_CUSTOMER = "/api/auth/user";


    public Response create(Customer customer){
        return given()
                .header("Content-type", "application/json")
                .body(customer)
                .when()
                .post(CREATE_CUSTOMER);
    }
    public Response pass(CustomerPass pass){
        return  given()
                .header("Content-type", "application/json")
                .and().body(pass)
                .when()
                .post(PASS_CUSTOMER);
    }
    public Response modify(CustomerModify modify, CustomerToken token){
        return  given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .and().body(modify)
                .when()
                .post(MODIFY_CUSTOMER);
    }

    public Response delete(String token){
        return given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .when()
                .delete(DELETE_CUSTOMER);
    }
}
