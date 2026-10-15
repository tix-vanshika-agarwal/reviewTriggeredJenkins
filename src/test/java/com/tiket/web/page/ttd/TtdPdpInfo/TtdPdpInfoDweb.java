package com.tiket.web.page.ttd.TtdPdpInfo;

import com.tiket.model.Session;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import org.openqa.selenium.WebDriver;

public class TtdPdpInfoDweb<T extends WebDriver> extends TtdPdpInfoWeb<T> {

    public TtdPdpInfoDweb(Session<T> session) {
        super(session);
    }
}
