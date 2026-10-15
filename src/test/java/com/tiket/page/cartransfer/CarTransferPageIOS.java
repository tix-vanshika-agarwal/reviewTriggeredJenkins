package com.tiket.page.cartransfer;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class CarTransferPageIOS <T extends WebDriver> extends CarTransferPage<T> {

    public CarTransferPageIOS(Session<T> session) {
        super(session);
    }
}
