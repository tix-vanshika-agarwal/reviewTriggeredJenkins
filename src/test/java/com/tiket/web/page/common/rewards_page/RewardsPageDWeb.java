package com.tiket.web.page.common.rewards_page;

import com.tiket.appium.PopupObserver;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class RewardsPageDWeb  <T extends WebDriver> extends RewardsPageWeb<T>{
    public RewardsPageDWeb(Session<T> session) {
        super(session);
    }

    private static final String STAGING_REWARDS_URL = "https://gatotkaca.tiket.com/rewards";
    private static final String PREPROD_REWARDS_URL = "https://preprod.tiket.com/rewards";
    private static final String PROD_REWARDS_URL = "https://www.tiket.com/rewards";

    private static final Locator REWARDS_LOGIN_BUTTON = new Locator("REWARDS_LOGIN_BUTTON", new By.ByXPath("//button[.//span[normalize-space()='Login' or normalize-space()='Masuk' or normalize-space()='Log in'] or normalize-space()='Login' or normalize-space()='Masuk' or normalize-space()='See your benefits' or normalize-space()='Lihat benefit buatmu'  ]"));
    private static final Locator POINTS_BALANCE = new Locator("POINTS_BALANCE", new By.ByXPath("//*[self::span or self::p or self::div or self::h1 or self::h2 or self::h3]" + "[(contains(.,'Poin') or contains(.,'poin') or contains(.,'Point') or contains(.,'TIX')) " + "and string-length(normalize-space(.)) < 40]"));
    private static final Locator LOADING_INDICATOR = new Locator("LOADING_INDICATOR", new By.ByCssSelector("[class*='LoadingIndicator_loading_indicator_container']"));

    private static final Locator PENDING_POINT_HISTORY_TAB = new Locator("PENDING_POINT_HISTORY_TAB", By.xpath("//*[self::button or self::a or self::span][contains(normalize-space(),'Pending')" + " or contains(normalize-space(),'Menunggu')]"));
    private static final Locator PENDING_POINTS_AMOUNT = new Locator("PENDING_POINTS_AMOUNT", By.xpath("//*[contains(@class, 'pending-point-amount-class')]"));

    // "A new partner joins" / "Partner baru hadir" promo modal that can appear over the rewards page
    // (e.g. right after login) and covers the points balance, blocking POINTS_BALANCE from ever
    // appearing. Dismissed via its "Awesome!" / "Asik!" CTA by a PopupObserver so it is auto-closed
    // whenever it shows up, since it is not tied to a specific, predictable point in the flow.
    private static final Locator NEW_PARTNER_POPUP_DISMISS = new Locator("NEW_PARTNER_POPUP_DISMISS",
            By.xpath("//button[normalize-space()='Awesome!' or normalize-space()='Asik!'"
                    + " or .//span[normalize-space()='Awesome!' or normalize-space()='Asik!']]"));

    @Override
    public void openPendingPointHistoryPage() {
        String pendingPointHistoryUrl = getPendingPointHistoryUrl();
        driverTools.maximizeWindow();
        logger.log("Opening pending point history page: " + pendingPointHistoryUrl);
        driverTools.get(pendingPointHistoryUrl);
        driverTools.waitForWebToLoad();
        waitForLoadingToFinish();
    }

    @Override
    public String getPendingPointsValue() {
        driverTools.waitForElementToAppear(PENDING_POINTS_AMOUNT, 15);
        return driverTools.getText(PENDING_POINTS_AMOUNT).orElse("0");
    }

    @Override
    public boolean isPendingPointHistoryPageLoaded() {
        driverTools.waitForWebToLoad();
        waitForLoadingToFinish();
        String currentUrl = driverTools.getCurrentURL();
        return currentUrl != null
                && currentUrl.contains("point-history")
                && (currentUrl.contains("tab=pending") || driverTools.waitForElementToAppear(PENDING_POINT_HISTORY_TAB, 10).isPresent());
    }

    @Override
    public String getTotalPoints() {
        // Implementation for DWeb to retrieve total points
        // This is a placeholder, actual implementation will depend on the DWeb structure
        return null; // Example return value
    }

    @Override
    public void openRewardsPage() {
        driverTools.maximizeWindow();
        String url = getRewardsUrl();
        logger.log("Opening rewards page via URL: " + url);
        driverTools.get(url);
        driverTools.waitForWebToLoad();
        waitForLoadingToFinish();
        PopupObserver.inject(driver, List.of(NEW_PARTNER_POPUP_DISMISS));
    }

    private void waitForLoadingToFinish() {
        // The rewards microsite shows a full-page loading spinner that intercepts clicks
        // and renders content lazily; wait for it to disappear before interacting.
        driverTools.waitForElementToDisappear(LOADING_INDICATOR, 30);
    }

    @Override
    public boolean isOnRewardsPage() {
        driverTools.waitForWebToLoad();
        String currentUrl = driverTools.getCurrentURL();
        logger.log("Current URL: " + currentUrl);
        return currentUrl != null && currentUrl.contains("rewards");
    }

    @Override
    public void clickLoginButton() {
        driverTools.waitForElementToAppear(REWARDS_LOGIN_BUTTON, 15).orElseThrow();
        driverTools.clickWithJavascriptExecutor(REWARDS_LOGIN_BUTTON);
        // The login setup page renders a loading spinner that intercepts the subsequent
        // "Continue with phone/email" click in the shared login flow; wait it out here.
        waitForLoadingToFinish();
    }

    @Override
    public boolean isPointsBalanceVisible() {
        driverTools.waitForWebToLoad();
        waitForLoadingToFinish();
        // Login navigates/reloads the page, which drops any observer injected earlier (e.g. in
        // openRewardsPage()), so re-inject here too: this is exactly where the "new partner" promo
        // modal can be sitting on top of the points balance after a fresh page load post-login.
        PopupObserver.inject(driver, List.of(NEW_PARTNER_POPUP_DISMISS));
        return driverTools.waitForElementToAppear(POINTS_BALANCE, 15).isPresent();
    }

    @Override
    public void scrollToBottomOfRewardsPage() {
        driverTools.waitForWebToLoad();
        driverTools.scrollToBottomOfPage();
    }

    @Override
    public boolean isScrolledToBottomOfRewardsPage() {
        driverTools.waitForWebToLoad();
        return driverTools.isPageScrolledToBottom();
    }

    private String getRewardsUrl() {
        return switch (Configs.ENVIRONMENT) {
            case GK -> STAGING_REWARDS_URL;
            case PREPROD -> PREPROD_REWARDS_URL;
            case PROD -> PROD_REWARDS_URL;
            default -> throw new NotImplementedException("Unsupported environment: " + Configs.ENVIRONMENT);
        };
    }
}
