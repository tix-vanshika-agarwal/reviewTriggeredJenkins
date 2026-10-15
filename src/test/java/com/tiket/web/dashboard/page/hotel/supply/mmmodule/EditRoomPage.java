package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EditRoomPage<T extends WebDriver> extends BasePage<T> {

    public EditRoomPage(Session<T> session) {
        super(session);
    }

    private static final Locator ROOM_STATUS_TOGGLE = new Locator("ROOM_STATUS_TOGGLE", new By.ByXPath("(//label[input[@aria-label='toggle']]/div[contains(@class,'Toggle_toggle')])[1]"));
    private static final Locator SUCCESSFUL_SNACKBAR = new Locator("SUCCESSFUL_SNACKBAR", new By.ByXPath("//div[contains(@class,'Snackbar_text_content')]"));

    public void clickRoomStatusToggle() {
        WebElement toggle = driverTools.waitForElementToPresence(ROOM_STATUS_TOGGLE).orElseThrow();
        driverTools.scrollIntoWebView(toggle);
        driverTools.clickWithJavascriptExecutor(toggle);
    }

    public boolean isSuccessfulSnackbarDisplayed() {
        WebElement snackbar = driverTools.waitForElementToAppear(SUCCESSFUL_SNACKBAR, 10).orElseThrow();
        return driverTools.isElementDisplayed(snackbar);
    }
}
