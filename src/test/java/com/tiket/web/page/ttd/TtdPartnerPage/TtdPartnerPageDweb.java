package com.tiket.web.page.ttd.TtdPartnerPage;

import com.tiket.model.Session;
import com.tiket.web.page.ttd.TtdDestinationPage.TtdDestinationPageWeb;
import org.openqa.selenium.WebDriver;

public class TtdPartnerPageDweb<T extends WebDriver> extends TtdPartnerPageWeb<T> {

    public TtdPartnerPageDweb(Session<T> session) {
        super(session);
    }
}
