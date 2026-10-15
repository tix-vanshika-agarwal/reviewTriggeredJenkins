package com.tiket.page.common.customerCareDashboard;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class CustomerCareDashboardAndroid<T extends WebDriver> extends CustomerCareDashboard<T> {

    public CustomerCareDashboardAndroid(Session<T> session) {
        super(session);
    }
}
