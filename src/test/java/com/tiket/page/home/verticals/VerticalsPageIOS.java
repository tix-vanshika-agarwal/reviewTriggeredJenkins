package com.tiket.page.home.verticals;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class VerticalsPageIOS <T extends WebDriver> extends VerticalsPage<T> {
    public VerticalsPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void openVerticalContainingName(String verticalName) {
        final Locator VERTICAL_ICONS = new Locator("VERTICAL_ICONS", AppiumBy.xpath("//*[@name='verticalView']"));
        final Locator SECONDARY_VERTICAL_ICON = new Locator("SECONDARY_VERTICAL_ICON", AppiumBy.xpath("//XCUIElementTypeCollectionView//XCUIElementTypeCollectionView"));
        final Locator TITLE = new Locator("TITLE", AppiumBy.xpath(String.format("//*[@name='titleLabel' and contains(@label,'%s')]", verticalName)));
        final Locator SHOW_ALL = new Locator("SHOW_ALL", AppiumBy.xpath("//*[@name='seeAllText']"));

        driverTools.waitForElementToAppear(VERTICAL_ICONS);
        if(!driverTools.isElementInView(TITLE)) {
            Optional<WebElement> icon = driverTools.swipeToPresentedElement(TITLE, SECONDARY_VERTICAL_ICON, Swipe.RIGHT, 10);
            if(icon.isPresent()) {
                icon.get().click();
            } else {
                WebElement iconElement = driverTools.swipeToPresentedElement(TITLE, SECONDARY_VERTICAL_ICON, Swipe.LEFT, 10).orElseThrow();
                driverTools.click(iconElement);
            }
        } else {
            driverTools.clickElementByCoordinate(TITLE);
        }
    }
}
