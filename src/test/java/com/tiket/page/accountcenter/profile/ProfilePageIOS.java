package com.tiket.page.accountcenter.profile;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class ProfilePageIOS <T extends WebDriver> extends ProfilePage<T> {
    public ProfilePageIOS(Session<T> session) {
        super(session);
    }
}

