package com.tiket.web.page.ttd.TtdPartnerPage;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class TtdPartnerPageMweb<T extends WebDriver> extends TtdPartnerPageWeb<T> {

    public TtdPartnerPageMweb(Session<T> session) {
        super(session);
    }
}
