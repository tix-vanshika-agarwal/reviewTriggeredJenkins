package com.tiket.page.home.verticals;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class VerticalsPageAndroid<T extends WebDriver> extends VerticalsPage<T> {
    public VerticalsPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void openVerticalContainingName(String verticalName) {
        final Locator VERTICAL_ICONS = new Locator("VERTICAL_ICONS", AppiumBy.xpath("//*[contains(@resource-id, 'verticals')]//*[contains(@resource-id, 'vertical_icons')]"));
        final Locator SECONDARY_VERTICAL_ICON = new Locator("SECONDARY_VERTICAL_ICON", AppiumBy.xpath("//*[contains(@resource-id, 'secondary_vertical_recycler')]"));
        final Locator TITLE = new Locator("TITLE", AppiumBy.xpath(String.format("//*[contains(@resource-id, 'tv_title') and contains(@text,'%s')]", verticalName)));
        final Locator SHOW_ALL = new Locator("SHOW_ALL", AppiumBy.xpath("//*[contains(@resource-id, 'tv_see_more')]"));

        driverTools.waitForElementToAppear(VERTICAL_ICONS);
        if(!driverTools.isElementDisplayed(TITLE)) {
            Optional<WebElement> icon = driverTools.swipeToElement(TITLE, SECONDARY_VERTICAL_ICON, Swipe.RIGHT, 10);
            if(icon.isPresent()) {
                icon.get().click();
            } else {
                WebElement iconElement = driverTools.swipeToElement(TITLE, SECONDARY_VERTICAL_ICON, Swipe.LEFT, 10).orElseThrow();
                driverTools.click(iconElement);
            }
        } else {
            driverTools.click(TITLE);
        }
    }
}
