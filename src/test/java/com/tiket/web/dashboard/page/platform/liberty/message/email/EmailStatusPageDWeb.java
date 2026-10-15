package com.tiket.web.dashboard.page.platform.liberty.message.email;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class EmailStatusPageDWeb<T extends WebDriver> extends EmailStatusPageWeb<T> {
    public EmailStatusPageDWeb(Session<T> session) {
        super(session);
    }
}
