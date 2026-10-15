package com.tiket.web.page.platform.eiffel.gamification;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

/**
 * The tiket CUAN leaderboard on desktop web. The page is one responsive bundle, so this names the
 * production host and maximizes the window it is read at — everything else, locators and flow alike,
 * comes from {@link TiketCuanLeaderboardPageWeb}.
 */
public class TiketCuanLeaderboardPageDWeb<T extends WebDriver> extends TiketCuanLeaderboardPageWeb<T> {

    private static final String PROD_DWEB_URL = "https://www.tiket.com/";

    public TiketCuanLeaderboardPageDWeb(Session<T> session) {
        super(session);
    }

    @Override
    protected String getProdBaseUrl() {
        return PROD_DWEB_URL;
    }

    /**
     * The desktop layout is what the status card and the tier tables are addressed against, and a
     * headless Chrome starts narrow enough to be served the mobile one instead.
     */
    @Override
    protected void sizeWindowForLayout() {
        driverTools.maximizeWindow();
    }
}
