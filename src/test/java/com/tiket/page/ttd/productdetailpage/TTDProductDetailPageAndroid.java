package com.tiket.page.ttd.productdetailpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TTDProductDetailPageAndroid<T extends WebDriver> extends TTDProductDetailPage<T> {

    public TTDProductDetailPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator PRODUCT_NAME = new Locator("PRODUCT_NAME", AppiumBy.id("tv_title"));
    public static final Locator PRODUCT_LOCATION = new Locator("PRODUCT_LOCATION", AppiumBy.id("tv_location"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'appbar_container')]//*[contains(@resource-id,'parent_back_icon_container')]"));

    @Override
    public String getPartnerName() {
        WebElement webElement = driverTools.waitForElementToPresence(PRODUCT_NAME,5).orElseThrow();
        return driverTools.getText(webElement);
    }

    @Override
    public String getProductLocation(String location) {
        return driverTools.waitForElementToAppear(PRODUCT_LOCATION,5).orElseThrow().getText();
    }

    @Override
    public boolean isPopularPartnerPageValid(String name){
        String partnerNameText = name.equals("-") ? "-" : driverTools.waitForElementToAppear(PRODUCT_NAME,5).orElseThrow().getText();
        boolean isPartnerNameValid = name.equals("-") || partnerNameText.equalsIgnoreCase(name);

        if (!isPartnerNameValid) {
            logger.warn("Partner name displayed is '"+ partnerNameText +"', should be '"+ name +"'");
        }

        return isPartnerNameValid;
    }

    @Override
    public boolean checkProductNameNative(String productName) {
        return driverTools.waitForElementToPresence(getProductNameLocator(productName)).isPresent();
    }

    private static Locator getProductNameLocator(String productName) {
        return new Locator("PRODUCT_NAME_"+productName, AppiumBy.xpath("//*[contains(@class,'TextView') and @text='"+productName+"']"));
    }

    @Override
    public void swipeUntilTabVisible() {
        //FIXME: Pending implementation
    }

    @Override
    public void clickViewMap() {
        //FIXME: Pending implementation
    }
}
