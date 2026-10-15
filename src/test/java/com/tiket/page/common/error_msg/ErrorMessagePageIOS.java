package com.tiket.page.common.error_msg;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class ErrorMessagePageIOS<T extends WebDriver> extends ErrorMessagePage<T> {
    public ErrorMessagePageIOS(Session<T> session) {
        super(session);
    }

    private static final Locator ERROR_MESSAGE_TITLE = new Locator("ERROR_MESSAGE_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label, \"Code\")or contains(@label, \"Kode\")]/preceding-sibling::XCUIElementTypeStaticText[1]"));
    private static final Locator ERROR_MESSAGE_CONTENT = new Locator("ERROR_MESSAGE_CONTENT", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label, \"Code\")or contains(@label, \"Kode\")]"));
    private static final Locator ERROR_MESSAGE_BUTTON = new Locator("ERROR_MESSAGE_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@label=\"Reload\"]"));

    @Override
    public boolean isErrorDisplayed() {
        return driverTools.waitForElementToAppear(ERROR_MESSAGE_BUTTON, 5).isPresent();
    }

    @Override
    public String getMessageTitle() {
        return driverTools.getText(ERROR_MESSAGE_TITLE).orElseThrow();
    }

    @Override
    public String getMessageContent() {
        return driverTools.getText(ERROR_MESSAGE_CONTENT).orElseThrow();
    }

    @Override
    public boolean isButtonDisplayed() {
        return driverTools.waitForElementToAppear(ERROR_MESSAGE_BUTTON, 5).isPresent();
    }

    @Override
    public void clickButton() {
        driverTools.click(ERROR_MESSAGE_BUTTON);
    }
}
