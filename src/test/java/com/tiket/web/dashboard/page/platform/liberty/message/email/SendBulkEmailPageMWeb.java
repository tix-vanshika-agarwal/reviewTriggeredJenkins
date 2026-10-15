package com.tiket.web.dashboard.page.platform.liberty.message.email;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class SendBulkEmailPageMWeb<T extends WebDriver> extends SendBulkEmailPageWeb<T> {
    public SendBulkEmailPageMWeb(Session<T> session) {
        super(session);
    }
}
