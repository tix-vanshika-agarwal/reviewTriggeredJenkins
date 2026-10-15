package com.tiket.web.dashboard.page.platform.liberty.emailTemplate;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class EmailTemplatePageDWeb <T extends WebDriver> extends EmailTemplatePageWeb<T> {
    public EmailTemplatePageDWeb(Session<T> session) {
        super(session);
    }
}