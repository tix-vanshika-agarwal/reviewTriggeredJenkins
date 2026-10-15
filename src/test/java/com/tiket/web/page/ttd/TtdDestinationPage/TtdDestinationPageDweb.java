package com.tiket.web.page.ttd.TtdDestinationPage;

import com.tiket.model.Session;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import org.openqa.selenium.WebDriver;

public class TtdDestinationPageDweb<T extends WebDriver> extends TtdDestinationPageWeb<T> {

    public TtdDestinationPageDweb(Session<T> session) {
        super(session);
    }
}
