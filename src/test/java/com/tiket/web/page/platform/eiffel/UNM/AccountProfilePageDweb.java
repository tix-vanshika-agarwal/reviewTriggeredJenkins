package com.tiket.web.page.platform.eiffel.UNM;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Locator;
import org.openqa.selenium.By;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.unm.*;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.auth.MfaMethodsResponse;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;


public class AccountProfilePageDweb<T extends WebDriver> extends AccountProfilePageWeb<T> {

    public AccountProfilePageDweb(Session<T> session) {
        super(session);
    }

}
