package com.tiket.web.page.common.rewards_page;

import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.common.home.LoginPageDWeb;
import com.tiket.web.page.common.home.LoginPageMWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.Matchers.equalTo;

public abstract class RewardsPageWeb <T extends WebDriver> extends WebPage<T> {

    private static final String PENDING_POINT_HISTORY_PATH = "/member/point-history?tab=pending";
    private static final String STAGING_REWARDS_BASE_URL = "https://rewards.test.bliblitiket.dev";
    private static final String PREPROD_REWARDS_BASE_URL = "https://rewards.preprod.bliblitiket.dev";
    private static final String PROD_REWARDS_BASE_URL = "https://rewards.bliblitiket.com";

    public RewardsPageWeb(Session<T> session) {
        super(session);
    }

    public static String getPendingPointHistoryUrl() {
        String baseUrl = switch (Configs.ENVIRONMENT) {
            case GK -> STAGING_REWARDS_BASE_URL;
            case PREPROD -> PREPROD_REWARDS_BASE_URL;
            case PROD -> PROD_REWARDS_BASE_URL;
            default -> throw new NotImplementedException("Unsupported environment: " + Configs.ENVIRONMENT);
        };
        return baseUrl + PENDING_POINT_HISTORY_PATH;
    }

    public static <T extends WebDriver> RewardsPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new RewardsPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new RewardsPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract String getTotalPoints();

    public abstract void openRewardsPage();

    public abstract boolean isOnRewardsPage();

    public abstract void clickLoginButton();

    public abstract boolean isPointsBalanceVisible();

    public abstract void scrollToBottomOfRewardsPage();

    public abstract boolean isScrolledToBottomOfRewardsPage();

    public abstract void openPendingPointHistoryPage();

    public abstract boolean isPendingPointHistoryPageLoaded();

    public abstract String getPendingPointsValue();

}
