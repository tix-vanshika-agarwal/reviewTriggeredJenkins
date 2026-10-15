package com.tiket.web.page.platform.eiffel.UNM;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class UNMPageMWeb<T extends WebDriver> extends UNMPageWeb<T> {
    public UNMPageMWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void ensureUserIsLoggedInOnBlibli() {
        driverTools.waitForElementToAppear(BLIBLI_LOGIN_BUTTON, 5).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(BLIBLI_LOGIN_BUTTON, 10);
    }
}
