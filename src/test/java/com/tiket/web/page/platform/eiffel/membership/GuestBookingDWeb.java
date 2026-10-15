package com.tiket.web.page.platform.eiffel.membership;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class GuestBookingDWeb <T extends WebDriver> extends GuestBookingWeb<T> {
    public GuestBookingDWeb(Session<T> session) { super(session); }
}
