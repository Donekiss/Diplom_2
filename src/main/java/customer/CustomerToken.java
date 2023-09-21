package customer;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

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
}
