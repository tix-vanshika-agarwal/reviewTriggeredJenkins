package com.tiket.web.dashboard.page.platform.pricing;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class PricingDashboardPageDWeb<T extends WebDriver> extends PricingDashboardPageWeb<T> {
    public PricingDashboardPageDWeb(Session<T> session) {
        super(session);
    }
}
