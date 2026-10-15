package com.tiket.web.dashboard.page.platform.liberty;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class LibertyDashboardHomePageDweb<T extends WebDriver> extends LibertyDashboardHomePage<T> {
    public LibertyDashboardHomePageDweb(Session<T> session) {
        super(session);
    }

}
