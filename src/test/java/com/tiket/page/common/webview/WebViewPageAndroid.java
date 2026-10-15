package com.tiket.page.common.webview;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class WebViewPageAndroid<T extends WebDriver> extends WebViewPage<T> {

    public WebViewPageAndroid(Session<T> session) {
        super(session);
    }
}
