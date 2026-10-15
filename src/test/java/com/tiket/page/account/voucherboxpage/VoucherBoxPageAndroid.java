package com.tiket.page.account.voucherboxpage;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class VoucherBoxPageAndroid <T extends WebDriver> extends VoucherBoxPage<T> {
    public VoucherBoxPageAndroid(Session<T> session) {
        super(session);
    }
}
