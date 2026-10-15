package com.tiket.web.dashboard.page.platform.liberty.helpCenter;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class HelpCenterPageDWeb <T extends WebDriver> extends HelpCenterPageWeb<T> {
    public HelpCenterPageDWeb(Session<T> session) {
        super(session);
    }
}
