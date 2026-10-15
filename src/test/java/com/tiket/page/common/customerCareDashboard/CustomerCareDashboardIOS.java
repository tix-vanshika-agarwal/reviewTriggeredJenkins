package com.tiket.page.common.customerCareDashboard;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class CustomerCareDashboardIOS<T extends WebDriver> extends CustomerCareDashboard<T> {

    public CustomerCareDashboardIOS(Session<T> session) {
        super(session);
    }
}
