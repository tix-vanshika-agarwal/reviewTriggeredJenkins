package com.tiket.web.dashboard.page.platform.liberty.message;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class MessageDashboardPageDWeb<T extends WebDriver> extends MessageDashboardPageWeb<T> {
    public MessageDashboardPageDWeb(Session<T> session) {
        super(session);
    }
}
