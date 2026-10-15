package com.tiket.web.page.platform.eiffel.loyalty;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class LoyaltyDependencyPageDWeb<T extends WebDriver> extends LoyaltyDependencyPageWeb<T> {
    public LoyaltyDependencyPageDWeb(Session<T> session) {
        super(session);
    }
}
