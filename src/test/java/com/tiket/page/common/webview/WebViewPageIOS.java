package com.tiket.page.common.webview;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class WebViewPageIOS<T extends WebDriver> extends WebViewPage<T> {

    public WebViewPageIOS(Session<T> session) {
        super(session);
    }
}
