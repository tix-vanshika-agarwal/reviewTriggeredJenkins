package com.tiket.web.dashboard.page.platform.liberty.emailTemplate;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class EmailTemplatePageMWeb<T extends WebDriver> extends EmailTemplatePageWeb<T> {

    public EmailTemplatePageMWeb(Session<T> session) {
        super(session);
    }
}