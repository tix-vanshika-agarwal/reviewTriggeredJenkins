package com.tiket.web.page.common.rewards_page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RewardsPageMWeb <T extends WebDriver> extends RewardsPageWeb<T> {
    public RewardsPageMWeb(Session<T> session) {
        super(session);
    }

    private static final Locator TOTAL_REWARDS_POINTS = new Locator("TOTAL_REWARDS_POINTS", new By.ByXPath("(//*[contains(@class,'middle-section')]//span[contains(@class,'text')])[1]"));
    private static final Locator SKIP_TO_REWARDS = new Locator("SKIP_TO_REWARDS", new By.ByXPath("//*[contains(@id,'bli-button')]"));
    private static final Locator LOADING_INDICATOR = new Locator("LOADING_INDICATOR", new By.ByCssSelector("[class*='LoadingIndicator_loading_indicator_container']"));
    private static final Locator PENDING_POINT_HISTORY_TAB = new Locator("PENDING_POINT_HISTORY_TAB", By.xpath("//div[contains(@class,'entry-point-card__content__item')][.//span[normalize-space()='Points']]"));
    private static final Locator PENDING_POINTS_AMOUNT = new Locator("PENDING_POINTS_AMOUNT", By.xpath("(//li[contains(@class,'list__child')]//div[contains(@class,'child__point')])[1]"));
    private static final Locator PENDING_POINT = new Locator("PENDING_POINT", By.xpath("//div[contains(@class,'blu-tab__container')][.//span[normalize-space()='Pending']]"));

    @Override
    public String getTotalPoints() {
        skipToRewards();
        waitForLoadingToFinish();
        WebElement element = driverTools.waitForElementToAppear(TOTAL_REWARDS_POINTS, 15).orElseThrow();
        String totalTiketPoints = driverTools.getText(element).replaceAll("[^0-9]", "");
        logger.log("Total Tiket Points: " + totalTiketPoints);
        return totalTiketPoints.isBlank() ? "0" : totalTiketPoints;
    }

    @Override
    public void openRewardsPage() {
        throw new NotImplementedException("Open rewards page is implemented for DWEB only");
    }

    @Override
    public boolean isOnRewardsPage() {
        throw new NotImplementedException("Open rewards page is implemented for DWEB only");
    }

    @Override
    public void clickLoginButton() {
        throw new NotImplementedException("Open rewards page is implemented for DWEB only");
    }

    @Override
    public boolean isPointsBalanceVisible() {
        skipToRewards();
        waitForLoadingToFinish();
        return driverTools.waitForElementToAppear(TOTAL_REWARDS_POINTS, 15).isPresent();
    }

    @Override
    public void scrollToBottomOfRewardsPage() {
        waitForLoadingToFinish();
        driverTools.scrollToBottomOfPage(20);
    }

    @Override
    public boolean isScrolledToBottomOfRewardsPage() {
        waitForLoadingToFinish();
        return driverTools.isPageScrolledToBottom();
    }

    @Override
    public void openPendingPointHistoryPage() {
        logger.log("Clicking the pending point history tab in Mobile WebView");
        driverTools.waitForElementToAppear(PENDING_POINT_HISTORY_TAB, 15).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToAppear(PENDING_POINT, 15).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForWebToLoad();
    }

    @Override
    public String getPendingPointsValue() {
        logger.log("Extracting pending points value from Mobile WebView");
        driverTools.waitForElementToAppear(PENDING_POINTS_AMOUNT, 15);
        return driverTools.getText(PENDING_POINTS_AMOUNT).orElse("0");
    }

    @Override
    public boolean isPendingPointHistoryPageLoaded() {
        driverTools.waitForWebToLoad();
        String currentUrl = driverTools.getCurrentURL();
        return currentUrl != null && (currentUrl.contains("point-history") || currentUrl.contains("tab=pending"));
    }

    private void skipToRewards() {
        if(driverTools.isElementDisplayed(TOTAL_REWARDS_POINTS))
            return;

        driverTools.waitForElementToPresence(SKIP_TO_REWARDS).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToDisappear(SKIP_TO_REWARDS);
        driverTools.waitForElementToPresence(SKIP_TO_REWARDS);
        driverTools.waitForElementToPresence(SKIP_TO_REWARDS).ifPresent(driverTools::clickWithJavascriptExecutor);

    }

    private void waitForLoadingToFinish() {
        driverTools.waitForElementToDisappear(LOADING_INDICATOR, 30);
    }
}
