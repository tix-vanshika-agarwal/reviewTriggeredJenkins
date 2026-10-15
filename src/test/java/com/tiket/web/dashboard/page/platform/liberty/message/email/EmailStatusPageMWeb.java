package com.tiket.web.dashboard.page.platform.liberty.message.email;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class EmailStatusPageMWeb<T extends WebDriver> extends EmailStatusPageWeb<T> {
    public EmailStatusPageMWeb(Session<T> session) {
        super(session);
    }
}
