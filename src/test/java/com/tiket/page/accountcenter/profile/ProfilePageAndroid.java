package com.tiket.page.accountcenter.profile;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class ProfilePageAndroid <T extends WebDriver> extends ProfilePage<T> {
    public ProfilePageAndroid(Session<T> session) {
        super(session);
    }
}
