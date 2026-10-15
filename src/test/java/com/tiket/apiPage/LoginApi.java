package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.auth.AuthResponse;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;

public class LoginApi<T extends WebDriver> extends BasePage<T> {

    public LoginApi(Session<T> session) {
        super(session);
    }

    @SneakyThrows
    public AuthResponse getAllToken() {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(payExpressResponse.getBody(), AuthResponse.class);
    }
}
