package com.tiket.page.accountcenter;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class AccountCenterPageIOS <T extends WebDriver> extends AccountCenterPage<T> {
    public AccountCenterPageIOS(Session<T> session) {
        super(session);
    }
}
