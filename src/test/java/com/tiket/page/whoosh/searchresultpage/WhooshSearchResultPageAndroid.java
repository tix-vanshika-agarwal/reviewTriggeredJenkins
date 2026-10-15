package com.tiket.page.whoosh.searchresultpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;

public class WhooshSearchResultPageAndroid <T extends WebDriver> extends WhooshSearchResultPage<T> {
    public WhooshSearchResultPageAndroid(Session<T> session) {
        super(session);
    }
}
