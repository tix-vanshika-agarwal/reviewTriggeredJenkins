package com.tiket.page.claimcenter;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class ClaimCenterPageAndroid <T extends WebDriver> extends ClaimCenterPage<T> {
    public ClaimCenterPageAndroid(Session<T> session) {
        super(session);
    }
}
