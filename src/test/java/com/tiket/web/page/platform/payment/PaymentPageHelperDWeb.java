package com.tiket.web.page.platform.payment;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class PaymentPageHelperDWeb <T extends WebDriver> extends PaymentPageHelperWeb<T>{
    public PaymentPageHelperDWeb(Session<T> session) {
        super(session);
    }
}