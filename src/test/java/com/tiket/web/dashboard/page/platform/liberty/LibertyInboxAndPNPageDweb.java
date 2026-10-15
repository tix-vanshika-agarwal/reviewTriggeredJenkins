package com.tiket.web.dashboard.page.platform.liberty;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class LibertyInboxAndPNPageDweb<T extends WebDriver> extends LibertyInboxAndPNPageWeb<T> {
    public LibertyInboxAndPNPageDweb(Session<T> session) {
        super(session);
    }

}