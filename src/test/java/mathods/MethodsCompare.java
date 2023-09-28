package mathods;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static org.junit.Assert.*;


public class MethodsCompare {
    @Step("Checking the presence mail and name in the response body")
    public static void checkPresenceMailAndName(JsonPath jsonPath) {
        Map<String, String> user = jsonPath.getMap("user");
        assertNotNull(user);
        assertTrue(user.containsKey("email"));
        assertTrue(user.containsKey("name"));
    }

    @Step("Checking in the success response")
    public static void checkSuccessStatusTrue(JsonPath jsonPath) {
        assertTrue(jsonPath.getBoolean("success"));
    }

    @Step("Checking in the false success response")
    public static void checkSuccessStatusFalse(JsonPath jsonPath) {
        assertFalse(jsonPath.getBoolean("success"));
    }

    @Step("Checking in body not null AccessToken")
    public static void checkNotNullAccessToken(JsonPath jsonPath) {
        assertNotNull(jsonPath.getString("accessToken"));
    }

    @Step("Checking in body not null RefreshToken")
    public static void checkNotNullRefreshToken(JsonPath jsonPath) {
        assertNotNull(jsonPath.getString("refreshToken"));
    }

    @Step("Checking error massage")
    public static void checkErrorMassage(String expected, String actual) {
        assertEquals("Неверное сообщение об ошибке", expected, actual);
    }

    @Step("Checking the presence number order in the response body")
    public static void checkPresenceNumberOrder(JsonPath jsonPath) {
        Map<String, String> order = jsonPath.getMap("order");
        assertNotNull(order);
        assertTrue(order.containsKey("number"));
    }

    @Step("Checking status code")
    public static void checkStatusCode(int code, Response response) {
        assertEquals("Неверный статус код", code, response.statusCode());
    }

    @Step("Checking in body not null value of the String")
    public static void checkNotNullValueString(String value, JsonPath jsonPath) {
        assertNotNull(jsonPath.getString(value));
    }

    @Step("Checking in body not null value of the List")
    public static void checkNotNullValueList(String value, JsonPath jsonPath) {
        assertNotNull(jsonPath.getList(value));
    }


}
