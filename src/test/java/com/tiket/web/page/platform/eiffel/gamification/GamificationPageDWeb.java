package com.tiket.web.page.platform.eiffel.gamification;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

/**
 * BTM on desktop web. The game is one responsive bundle and the shared environments serve it from a
 * single host, so the production host is the only thing this has to name — everything else, locators
 * and flow alike, comes from {@link GamificationPageWeb}.
 */
public class GamificationPageDWeb<T extends WebDriver> extends GamificationPageWeb<T> {

    private static final String PROD_DWEB_URL = "https://www.tiket.com/";

    public GamificationPageDWeb(Session<T> session) {
        super(session);
    }

    @Override
    protected String getProdBaseUrl() {
        return PROD_DWEB_URL;
    }
}
