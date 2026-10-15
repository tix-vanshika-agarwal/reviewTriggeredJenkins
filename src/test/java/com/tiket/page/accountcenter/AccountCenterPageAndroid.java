package com.tiket.page.accountcenter;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class AccountCenterPageAndroid <T extends WebDriver> extends AccountCenterPage<T> {
    public AccountCenterPageAndroid(Session<T> session) {
        super(session);
    }
}
