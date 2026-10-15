package com.tiket.web.page.platform.eiffel.loyalty;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class LoyaltyDependencyPageMWeb<T extends WebDriver> extends LoyaltyDependencyPageWeb<T> {
    public LoyaltyDependencyPageMWeb(Session<T> session) {
        super(session);
    }
}
