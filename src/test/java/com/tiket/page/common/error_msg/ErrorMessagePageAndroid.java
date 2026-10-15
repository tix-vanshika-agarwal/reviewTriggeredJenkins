package com.tiket.page.common.error_msg;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class ErrorMessagePageAndroid<T extends WebDriver> extends ErrorMessagePage<T> {
    public ErrorMessagePageAndroid(Session<T> session) {
        super(session);
    }


    private static final Locator ERROR_MESSAGE = new Locator("ERROR_MESSAGE", AppiumBy.id("tds_main_info_container_view"));
    private static final Locator ERROR_MESSAGE_TITLE = new Locator("ERROR_MESSAGE_TITLE", AppiumBy.id("tds_txt_header_info_view"));
    private static final Locator ERROR_MESSAGE_CONTENT = new Locator("ERROR_MESSAGE_CONTENT", AppiumBy.id("tds_txt_body_info_view"));
    private static final Locator ERROR_MESSAGE_BUTTON = new Locator("ERROR_MESSAGE_BUTTON", AppiumBy.id("tds_button_view"));

    @Override
    public boolean isErrorDisplayed() {
        return driverTools.waitForElementToAppear(ERROR_MESSAGE, 5).isPresent();
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
