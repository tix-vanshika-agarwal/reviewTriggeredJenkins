package com.tiket.web.dashboard.page.platform.sphinx.intools.request;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class ResellerRequestPageMWeb<T extends WebDriver> extends ResellerRequestPageWeb<T>{
    public ResellerRequestPageMWeb(Session<T> session) {
        super(session);
    }
}
