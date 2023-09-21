package customer;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CustomerClient {
    private static final String CREATE_CUSTOMER = "/api/auth/register";
    private static final String PASS_CUSTOMER = "/api/auth/login";
    private static final String MODIFY_CUSTOMER = "/api/auth/user";
    private static final String INFO_CUSTOMER = "/api/auth/user";
    private static final String LOGOUT_CUSTOMER = "/api/auth/logout";
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
    public Response modify(Customer customer, String token){
        return  given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .and().body(customer)
                .when()
                .patch(MODIFY_CUSTOMER);
    }
    public Response info(String token){
        return  given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(INFO_CUSTOMER);
    }
    public Response logout(String token){
        return  given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(LOGOUT_CUSTOMER);
    }

    public Response delete(String token){
        return given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .when()
                .delete(DELETE_CUSTOMER);
    }
}
