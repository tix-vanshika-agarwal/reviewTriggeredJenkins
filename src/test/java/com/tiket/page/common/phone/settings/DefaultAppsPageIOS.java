package com.tiket.page.common.phone.settings;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class DefaultAppsPageIOS  <T extends WebDriver> extends DefaultAppsPage<T> {

    public DefaultAppsPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void clearBrowserDefaultPreferenceOnLambdaTest() {
        logger.log("Not required for iOS");
    }
}
