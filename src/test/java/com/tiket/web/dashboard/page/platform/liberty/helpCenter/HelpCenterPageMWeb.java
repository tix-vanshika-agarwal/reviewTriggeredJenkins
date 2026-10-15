package com.tiket.web.dashboard.page.platform.liberty.helpCenter;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class HelpCenterPageMWeb <T extends WebDriver> extends  HelpCenterPageWeb<T> {

    public HelpCenterPageMWeb(Session<T> session) {
        super(session);
    }
}
