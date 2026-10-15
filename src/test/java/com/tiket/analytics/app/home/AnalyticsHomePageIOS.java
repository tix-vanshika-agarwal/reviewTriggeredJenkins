package com.tiket.analytics.app.home;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.service.BundleID;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AnalyticsHomePageIOS<T extends WebDriver> extends AnalyticsHomePage<T> {
    private static final Locator HERO_BANNER = new Locator("HERRO_BANNER", AppiumBy.name("hero_banner"));
    private static final Locator PRODUCT_LIST = new Locator("PRODUCT_LIST", AppiumBy.xpath("(//XCUIElementTypeCollectionView//XCUIElementTypeCollectionView)[1]"));
    private static final Locator PRODUCT_LIST_NON_IDR = new Locator("PRODUCT_LIST_NON_IDR", AppiumBy.xpath("(((//*[contains(@name,'hero_banner')]//following-sibling::XCUIElementTypeCell)[1])//XCUIElementTypeOther)[4]"));
    private static final Locator PRODUCT_LIST_IDR = new Locator("PRODUCT_LIST_NON_IDR", AppiumBy.id("vertical_icon_container"));

    public AnalyticsHomePageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void restartApp() {
        IOSDriver iosDriver = (IOSDriver) driver;
        iosDriver.terminateApp(BundleID.getID());
        iosDriver.activateApp(BundleID.getID());
    }
    
    private boolean isSessionInBHS() {
        final Locator ACCOUNT = new Locator("ACCOUNT", AppiumBy.xpath("//XCUIElementTypeButton[@name='Masuk' or @name='Akun']"));
        return driverTools.waitForElementToAppear(ACCOUNT,15).isPresent();
    }
    
    private void swipeToProductsMenu(Locator product) {
        driverTools.swipeByDistance(PRODUCT_LIST,Swipe.RIGHT,0.8);
        driverTools.swipeByDistance(PRODUCT_LIST,Swipe.RIGHT,0.8);
        WebElement e = driverTools.swipeByDistanceToElement(product,PRODUCT_LIST,Swipe.LEFT,0.9,5).orElseThrow();
        driverTools.click(e);
    }

    @Override
    public void openVerticalPage(String vertical) {
        String verticalName = convertVerticalNameIfRequired(vertical);
        clickHomeProduct(verticalName);
    }

    private String convertVerticalNameIfRequired(String verticalName){
        if (isSessionInBHS()) {
            logger.log("session is in bhs");
            verticalName = this.translateVerticalNameToBhs(verticalName);
        }
        return verticalName;
    }

    private void clickHomeProduct(String vertical) {
        final Locator PRODUCT_ELEMENT_LOCATOR = this.getVerticalLocator(vertical);
        driverTools.swipeToElement(HERO_BANNER, Swipe.DOWN, 30).orElseThrow();

        driverTools.element(PRODUCT_ELEMENT_LOCATOR).ifPresentOrElse(
                e -> driverTools.clickElementByCoordinate(e),
                () -> {
                    if (!swipeToFindAndClickElement(PRODUCT_ELEMENT_LOCATOR)) {
                        this.swipeToProductsMenu(PRODUCT_ELEMENT_LOCATOR);
                    }
                }
        );
    }

    private boolean swipeToFindAndClickElement(Locator locator) {
        // Perform multiple swipes to locate the element dynamically
        for (int i = 0; i < 5; i++) {
            Locator container = PRODUCT_LIST_NON_IDR;
            if(driverTools.waitForElementToPresence(PRODUCT_LIST_IDR).isPresent()){
                container = PRODUCT_LIST_IDR;
            }
            driverTools.swipeByDistance(container, Swipe.RIGHT, 0.6);
            driverTools.swipeByDistanceToElement(locator, container, Swipe.LEFT, 0.6, 5);
            if (driverTools.element(locator).isPresent()) {
                driverTools.clickElementByCoordinate(driverTools.element(locator).orElseThrow());
                return true;
            }
        }
        return false;
    }

    private Locator getVerticalLocator(String verticalName) {
        return new Locator("HOME_PRODUCT_ICON", AppiumBy.xpath(
                switch (verticalName) {
                    case "referral" ->
                            "(//XCUIElementTypeImage[@name='hero_banner']//following-sibling::XCUIElementTypeCell)[1]//XCUIElementTypeStaticText[@value='" + verticalName + "' or contains(@value, 'Refer & Earn')]";
                    case "claim-center" ->
                            "(//XCUIElementTypeImage[@name='hero_banner']//following-sibling::XCUIElementTypeCell)[1]//XCUIElementTypeStaticText[@value='" + verticalName + "' or contains(@value, 'Claim Center')]";
                    case "Blibli" ->
                            "(//XCUIElementTypeImage[@name='hero_banner']//following-sibling::XCUIElementTypeCell)[1]//XCUIElementTypeStaticText[@value='" + verticalName + "']";
                    case "tiket+" ->
                            "(//XCUIElementTypeImage[@name='hero_banner']//following-sibling::XCUIElementTypeCell)[1]//XCUIElementTypeStaticText[@value='" + verticalName + "' or contains(@value,'tiket+ Service') or contains(@value,'Service') or contains(@value,'tiket+')]";
                    case "attractions" ->
                            "(//XCUIElementTypeImage[@name='hero_banner']//following-sibling::XCUIElementTypeCell)[1]//XCUIElementTypeStaticText[@value='" + verticalName + "' or contains(@value,'Atraksi') or contains(@value,'Attraction') or contains(@value,'Attractions')]";
                    default ->
                            "(//XCUIElementTypeImage[@name='hero_banner']//following-sibling::XCUIElementTypeCell)[1]//XCUIElementTypeStaticText[starts-with(@value,'" + verticalName + "')]";
                }
        ));
    }
}