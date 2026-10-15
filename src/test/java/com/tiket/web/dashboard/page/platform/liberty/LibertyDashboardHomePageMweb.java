package com.tiket.web.dashboard.page.platform.liberty;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class LibertyDashboardHomePageMweb<T extends WebDriver> extends LibertyDashboardHomePage<T> {
    public LibertyDashboardHomePageMweb(Session<T> session) {
        super(session);
    }

}
