package com.tiket.web.page.platform.eiffel.loyalty;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class LoyaltyPointPageMWeb <T extends WebDriver> extends LoyaltyPointPageWeb<T> {
    public LoyaltyPointPageMWeb(Session<T> session) {
        super(session);
    }
}
