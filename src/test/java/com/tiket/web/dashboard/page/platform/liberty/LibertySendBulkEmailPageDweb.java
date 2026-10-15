package com.tiket.web.dashboard.page.platform.liberty;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class LibertySendBulkEmailPageDweb<T extends WebDriver> extends LibertySendBulkEmailPageWeb<T> {
    public LibertySendBulkEmailPageDweb(Session<T> session) {
        super(session);
    }

}
