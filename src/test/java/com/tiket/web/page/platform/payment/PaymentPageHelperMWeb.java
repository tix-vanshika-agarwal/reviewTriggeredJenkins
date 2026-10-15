package com.tiket.web.page.platform.payment;

import org.openqa.selenium.WebDriver;

import com.tiket.model.Session;

public class PaymentPageHelperMWeb<T extends WebDriver> extends PaymentPageHelperWeb<T> {
    public PaymentPageHelperMWeb(Session<T> session) {
        super(session);
    }
}