package com.tiket.web.page.accommodation.hotels.LandingPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static org.hamcrest.Matchers.is;

public class LandingPageMweb<T extends WebDriver> extends LandingPageWeb<T> {

    public LandingPageMweb(Session<T> session) {
        super(session);
    }

    public Locator getSearchFormResultLocator(String location){
        return new Locator("SEARCH_FORM_RESULT", new By.ByXPath("//*[@data-testid='item-name']//*[text()='"+location+"' or text()='"+location.toLowerCase()+"']"));
    }

}