package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class AffiliateCommonPageMWeb<T extends WebDriver> extends AffiliateCommonPageWeb<T> {
    public AffiliateCommonPageMWeb(Session<T> session) {
        super(session);
    }
}
