package com.tiket.web.page.nft.airport_transfer.srp;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class SRPDWebAirportTransfer<T extends WebDriver> extends AirportTransferSRPWeb<T> {

    public SRPDWebAirportTransfer(Session<T> session) {
        super(session);
    }
}
