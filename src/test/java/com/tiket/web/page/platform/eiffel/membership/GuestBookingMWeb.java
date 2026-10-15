package com.tiket.web.page.platform.eiffel.membership;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class GuestBookingMWeb <T extends WebDriver> extends GuestBookingWeb<T> {
    public  GuestBookingMWeb(Session<T> session) { super(session); }
}
