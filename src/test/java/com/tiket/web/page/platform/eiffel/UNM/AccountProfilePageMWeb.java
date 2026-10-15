package com.tiket.web.page.platform.eiffel.UNM;

import org.openqa.selenium.WebDriver;
import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.unm.*;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.auth.MfaMethodsResponse;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class AccountProfilePageMWeb<T extends WebDriver> extends AccountProfilePageWeb<T> {


    public AccountProfilePageMWeb(Session<T> session) {
        super(session);
    }
    
}
