package com.tiket.page.claimcenter;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class ClaimCenterPageIOS <T extends WebDriver> extends ClaimCenterPage<T> {
    public ClaimCenterPageIOS(Session<T> session) {
        super(session);
    }
}
