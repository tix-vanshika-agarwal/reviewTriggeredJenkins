package com.tiket.page.whoosh.searchresultpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.Matchers.*;

public class WhooshSearchResultPageIOS <T extends WebDriver> extends WhooshSearchResultPage<T> {
    public WhooshSearchResultPageIOS(Session<T> session) {
        super(session);
    }
}
