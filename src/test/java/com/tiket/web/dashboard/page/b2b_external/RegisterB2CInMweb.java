package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class RegisterB2CInMweb<T extends WebDriver> extends RegisterB2CInWEB<T> {
    public RegisterB2CInMweb(Session<T> session) {
        super(session);
    }
}