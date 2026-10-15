package com.tiket.web.dashboard.page.platform.liberty;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class LibertySendBulkEmailPageMweb<T extends WebDriver> extends LibertySendBulkEmailPageWeb<T> {
    public LibertySendBulkEmailPageMweb(Session<T> session) {
        super(session);
    }

}
