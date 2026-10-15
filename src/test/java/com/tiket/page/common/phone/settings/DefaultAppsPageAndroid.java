package com.tiket.page.common.phone.settings;

import com.tiket.appium.ListScrollHandler;
import com.tiket.appium.Swipe;
import com.tiket.model.DeviceFarm;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultAppsPageAndroid  <T extends WebDriver> extends DefaultAppsPage<T> {

    public DefaultAppsPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator OPENING_LINKS = new Locator("OPENING_LINKS", By.xpath("//*[@text=\"Opening links\"]"));
    public static final Locator IN_THE_APP = new Locator("IN_THE_APP", By.xpath("//*[@text=\"In the app\"]"));
    public static final Locator SWITCH_WIDGET = new Locator("SWITCH_WIDGET", By.id("android:id/switch_widget"));

    private static Locator browserLocator(String browser) {
        return new Locator(browser, By.xpath("//*[contains(@text, '" + browser + "')]"));
    }

    @Override
    public void clearBrowserDefaultPreferenceOnLambdaTest() {
        if (Configs.PLATFORM == Platform.ANDROID && Configs.DEVICE_FARM == DeviceFarm.LAMBDA_TEST) {
            logger.log("Clearing browser data");
            try {
                Map<String, Object> params = new HashMap<>();
                params.put("command", "shell");
                params.put("text", "am start -n com.google.android.permissioncontroller/com.android.permissioncontroller.role.ui.DefaultAppListActivity");
                Object o = ((JavascriptExecutor) driver).executeScript("lambda-adb", params);
                logger.log("result: " + o.toString());

                driverTools.waitForElementToAppear(OPENING_LINKS);
                driverTools.click(OPENING_LINKS);
                for (String browser : new String[]{"Edge", "Firefox"}) {
                    try {
                        final Locator BROWSER = browserLocator(browser);
                        final ListScrollHandler<T> lsh = new ListScrollHandler<>(driver, BROWSER);
                        lsh.gotoFirstElement(Swipe.UP, 2);
                        driverTools.click(BROWSER);
                        Optional<WebElement> webElement = driverTools.waitForElementToAppear(IN_THE_APP);
                        if (webElement.isPresent()) {
                            driverTools.click(webElement.get());
                        } else {
                            driverTools.waitForElementToAppear(SWITCH_WIDGET);
                            driverTools.click(SWITCH_WIDGET);
                        }
                        driverTools.back();
                    } catch (Exception ignored) {
                        logger.log(browser + " not found, skip");
                    }
                }
            } catch (Exception e) {
                logger.log("Failed to clear browsers data: " + e.getMessage());
                return;
            }
            logger.log("Cleared browsers data");
        }
    }
}
