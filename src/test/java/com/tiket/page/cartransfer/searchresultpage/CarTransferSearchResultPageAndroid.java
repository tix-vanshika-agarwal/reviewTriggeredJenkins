package com.tiket.page.cartransfer.searchresultpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CarTransferSearchResultPageAndroid <T extends WebDriver> extends CarTransferSearchResultPage<T>{
    private static final Locator DATE_TIME_SRP = new Locator("DATE_TIME_SRP", AppiumBy.id("tds_sub_header_navigation_appbar"));

    public CarTransferSearchResultPageAndroid(Session<T> session) {
        super(session);
    }


    @Override
    public String getDateSearchResultPage(String time){
        WebElement dateElement = driverTools.waitForElementToAppear(DATE_TIME_SRP).orElseThrow();
        String dateTime = driverTools.getText(dateElement);
        int idx = dateTime.indexOf("•") - 1 ;
        String date = dateTime.substring(0,idx);
        logger.log("DATE :" + date);
        return date;
    }

}
