package com.tiket.analytics.app.home;

import com.tiket.appium.Swipe;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class AnalyticsHomePageAndroid <T extends WebDriver> extends AnalyticsHomePage<T> {

    public AnalyticsHomePageAndroid(Session<T> session) {
        super(session);
    }
    private static final Locator CAROUSEL = new Locator("CAROUSEL", AppiumBy.xpath("(//*[contains(@resource-id,'verticalImageView')]/ancestor::*[contains(@class,'recyclerview')])[last()]"));
    private static final Locator TIKET_POINTS_CONTAINER = new Locator("TIKET_POINT_CONTAINER", AppiumBy.id("currency_container"));
    public static final Locator BLIBLI_POINTS = new Locator("BLIBLI_POINTS", AppiumBy.xpath("(//*[contains(@resource-id,'currency_container')]//*[contains(@text,'Blibli Tiket Points') or contains(@text,'PayLater') or contains(@text,'Blipay')])[1]"));
    public static final Locator STAYS_GROUP = new Locator("STAYS_GROUP", AppiumBy.xpath("//*[contains(@resource-id, 'vertical_group')]//*[contains(@text,'Stays') or contains(@text,'Penginapan')]"));

    @Override
    public void restartApp() {
        driverTools.closeApp();
        Deeplink homeDeeplink = new Deeplink("Home",
                "tiket://m.gatotkaca.tiket.com",
                "tiket://m.preprod.tiket.com",
                "tiket://m.tiket.com");
        driverTools.deeplink(homeDeeplink);
    }
    private boolean isSessionInEnglish() {
        final Locator ACCOUNT = new Locator("ACCOUNT", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_bottom_navigation') and (@text='Masuk' or @text='Akun')]"));
        return !driverTools.waitForElementToAppear(ACCOUNT,15).isPresent();
    }
    public void openVerticalPage(String vertical) {
        final String verticalName;
        if (!isSessionInEnglish()) {
            verticalName = this.translateVerticalNameToBhs(vertical);
        } else {
            verticalName=vertical;
        }
        this.clickHomeProduct(verticalName);
    }

    private void clickHomeProduct(String vertical) {
        String ele_home_product_icon;
        switch(vertical.toLowerCase()) {
            case "referral" : ele_home_product_icon = "//*[contains(@resource-id,'verticalImageView')]//following-sibling::*[contains(@resource-id,'titleTextView') and (@text='"+vertical+"') or (contains(@text,'Refer & Earn'))]"; break;
            case "claim-center" : ele_home_product_icon = "//*[contains(@resource-id,'verticalImageView')]//following-sibling::*[contains(@resource-id,'titleTextView') and (@text='"+vertical+"') or (contains(@text,'Claim Center'))]"; break;
            case "blibli" : ele_home_product_icon = "//*[contains(@resource-id,'verticalImageView')]//following-sibling::*[contains(@resource-id,'titleTextView') and (@text='"+vertical+"') or (contains(@text,'Blibli'))]"; break;
            case "tiket+" : ele_home_product_icon = "//*[contains(@resource-id,'verticalImageView')]//following-sibling::*[contains(@resource-id,'titleTextView') and (@text='"+vertical+"') or (contains(@text,'tiket+ Service') or contains(@text,'Service') or contains(@text,'tiket+'))]"; break;
            case "attractions" : ele_home_product_icon = "//*[contains(@resource-id,'verticalImageView')]//following-sibling::*[contains(@resource-id,'titleTextView') and (@text='"+vertical+"') or (contains(@text,'Atraksi') or contains(@text,'Attraction') or contains(@text,'Attractions'))]"; break;
            default : ele_home_product_icon = "//*[contains(@resource-id,'verticalImageView')]//following-sibling::*[contains(@resource-id,'titleTextView') and contains(@text,'"+vertical+"')]"; break;
        }
        driverTools.swipeToElement(TIKET_POINTS_CONTAINER, Swipe.DOWN,30).orElseThrow();
        driverTools.waitForElementToAppear(BLIBLI_POINTS,10);
        Locator HOME_PRODUCT_ICON = new Locator("HOME_PRODUCT_ICON", AppiumBy.xpath(ele_home_product_icon));
        if (driverTools.waitForElementToAppear(STAYS_GROUP,5).isPresent()){
            logger.log("Grouped vertical detected");
            driverTools.click(HOME_PRODUCT_ICON);
        } else{
            driverTools.waitForElementToAppear(CAROUSEL,10).orElseThrow();
            driverTools.swipeByDistance(CAROUSEL,Swipe.RIGHT,0.5);
            driverTools.swipeByDistance(CAROUSEL,Swipe.RIGHT,0.5);
            WebElement e = driverTools.swipeByDistanceToElement(HOME_PRODUCT_ICON,CAROUSEL,Swipe.LEFT,0.5,5).orElseThrow();
            driverTools.click(e);
        }
    }

}