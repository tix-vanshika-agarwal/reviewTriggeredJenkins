package com.tiket.web.page.payment.SavedPaymentMethod;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Locator;
import com.tiket.model.Session;

public class SavedPaymentMethodDWeb<T extends WebDriver> extends SavedPaymentMethodWeb<T> {

    public SavedPaymentMethodDWeb(Session<T> session) {
        super(session);
    }

}
