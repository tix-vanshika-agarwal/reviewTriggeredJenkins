package com.tiket.web.page.platform.eiffel.loyalty;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class LoyaltyPointPageDWeb <T extends WebDriver> extends LoyaltyPointPageWeb<T> {
    public LoyaltyPointPageDWeb(Session<T> session) {
        super(session);
    }
}
