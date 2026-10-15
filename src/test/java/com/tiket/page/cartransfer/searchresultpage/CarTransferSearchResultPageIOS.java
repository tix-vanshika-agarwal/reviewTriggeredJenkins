package com.tiket.page.cartransfer.searchresultpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CarTransferSearchResultPageIOS <T extends WebDriver> extends CarTransferSearchResultPage<T> {

    public CarTransferSearchResultPageIOS(Session<T> session) {
        super(session);
    }

    private Locator getDateAndTimeLocator(String time){
        return new Locator("DATE_TIME_LOCATOR", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'"+time+"')]"));
    }

    @Override
    public String getDateSearchResultPage(String time){
        WebElement dateElement = driverTools.waitForElementToAppear(getDateAndTimeLocator(time)).orElseThrow();
        String dateTime = driverTools.getText(dateElement);
        String date = dateTime.substring(0,dateTime.length()-7);
        logger.log("DATE :" + date);
        return date;
    }

}
