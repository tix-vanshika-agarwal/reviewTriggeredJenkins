package com.tiket.page.cartransfer;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class CarTransferPageAndroid <T extends WebDriver> extends CarTransferPage<T> {

    public CarTransferPageAndroid(Session<T> session) {
        super(session);
    }
}
