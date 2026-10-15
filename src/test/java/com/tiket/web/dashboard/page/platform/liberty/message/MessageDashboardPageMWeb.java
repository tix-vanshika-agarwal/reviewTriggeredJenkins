package com.tiket.web.dashboard.page.platform.liberty.message;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class MessageDashboardPageMWeb<T extends WebDriver> extends MessageDashboardPageWeb<T> {
    public MessageDashboardPageMWeb(Session<T> session) {
        super(session);
    }
}
