package com.tiket.web.page.platform.eiffel.gamification;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

/**
 * The tiket CUAN leaderboard on mobile web. Same bundle and same hosts as
 * {@link TiketCuanLeaderboardPageDWeb} everywhere but production, where the mobile site answers
 * without the {@code www} prefix — as {@link GamificationPageMWeb} has it for the rest of
 * {@code /game}.
 */
public class TiketCuanLeaderboardPageMWeb<T extends WebDriver> extends TiketCuanLeaderboardPageWeb<T> {

    private static final String PROD_MWEB_URL = "https://tiket.com/";

    public TiketCuanLeaderboardPageMWeb(Session<T> session) {
        super(session);
    }

    @Override
    protected String getProdBaseUrl() {
        return PROD_MWEB_URL;
    }

    /**
     * Nothing to size: the run is a real mobile browser, and its own viewport is the layout the
     * leaderboard is read at.
     */
    @Override
    protected void sizeWindowForLayout() {
    }
}
