package com.tiket.web.page.common.eTicket;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class ETicketDWeb <T extends WebDriver> extends ETicketWeb<T> {

    public ETicketDWeb(Session<T> session) {
        super(session);
    }
}
