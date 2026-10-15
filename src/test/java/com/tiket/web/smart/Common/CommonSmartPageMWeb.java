package com.tiket.web.smart.Common;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class CommonSmartPageMWeb<T extends WebDriver> extends CommonSmartPageWeb<T> {
    public CommonSmartPageMWeb(Session<T> session) {
        super(session);
    }
}
