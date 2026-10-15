package com.tiket.web.page.platform.eiffel.UNM;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class UNMPageDWeb<T extends WebDriver> extends UNMPageWeb<T> {
    public UNMPageDWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void ensureUserIsLoggedInOnBlibli() {
        this.clickSSOPopUpButton();
    }
}
