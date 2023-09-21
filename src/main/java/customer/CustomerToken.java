package customer;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import io.restassured.response.Response;

public class CustomerToken {
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("refreshToken")
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }

    public static CustomerToken fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, CustomerToken.class);
    }
    public static String extractAccessToken(Response response) {
        String responseBody = response.getBody().asString();
        CustomerToken authResponse = CustomerToken.fromJson(responseBody);
        return authResponse.getAccessToken();
    }
    public static String extractRefreshToken(Response response) {
        String responseBody = response.getBody().asString();
        CustomerToken authResponse = CustomerToken.fromJson(responseBody);
        return authResponse.getRefreshToken();
    }
}
