package com.tiket.web.smart.Common;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class CommonSmartPageDWeb<T extends WebDriver> extends CommonSmartPageWeb<T> {
    public CommonSmartPageDWeb(Session<T> session) {
        super(session);
    }
}
