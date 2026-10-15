package com.tiket.web.page.common.eTicket;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class ETicketMWeb <T extends WebDriver> extends ETicketWeb<T> {

    public ETicketMWeb(Session<T> session) {
        super(session);
    }
}
