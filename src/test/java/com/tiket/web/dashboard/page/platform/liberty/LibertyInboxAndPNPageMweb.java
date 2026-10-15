package com.tiket.web.dashboard.page.platform.liberty;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class LibertyInboxAndPNPageMweb<T extends WebDriver> extends LibertyInboxAndPNPageWeb<T> {
    public LibertyInboxAndPNPageMweb(Session<T> session) {
        super(session);
    }

}
