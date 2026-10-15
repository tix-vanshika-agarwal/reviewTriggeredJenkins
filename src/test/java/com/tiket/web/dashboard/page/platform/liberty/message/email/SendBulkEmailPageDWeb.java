package com.tiket.web.dashboard.page.platform.liberty.message.email;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class SendBulkEmailPageDWeb<T extends WebDriver> extends SendBulkEmailPageWeb<T> {
    public SendBulkEmailPageDWeb(Session<T> session) {
        super(session);
    }
}
