package com.tiket.web.page.platform.eiffel.gamification;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

/**
 * BTM on mobile web. Same bundle and same hosts as {@link GamificationPageDWeb} everywhere but
 * production, where the mobile site answers without the {@code www} prefix.
 */
public class GamificationPageMWeb<T extends WebDriver> extends GamificationPageWeb<T> {

    private static final String PROD_MWEB_URL = "https://tiket.com/";

    public GamificationPageMWeb(Session<T> session) {
        super(session);
    }

    @Override
    protected String getProdBaseUrl() {
        return PROD_MWEB_URL;
    }
}
