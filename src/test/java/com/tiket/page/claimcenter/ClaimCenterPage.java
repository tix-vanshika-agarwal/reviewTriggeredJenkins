package com.tiket.page.claimcenter;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.Matchers;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.core.Is.is;

public class ClaimCenterPage <T extends WebDriver> extends BasePage<T> {
    public ClaimCenterPage (Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> ClaimCenterPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {return new ClaimCenterPageIOS<>(session);}
            case ANDROID -> {return new ClaimCenterPageAndroid<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator WELCOME_INSURANCE_CLAIM_CENTER_CLOSE_BTN = new Locator("WELCOME_INSURANCE_CLAIM_CENTER_CLOSE_BTN", AppiumBy.xpath("//*[contains(@class,'close_button')]"));
    private static final Locator INSURANCE_CLAIM_CENTER_PAGE_TITLE = new Locator("INSURANCE_CLAIM_CENTER_PAGE_TITLE", AppiumBy.xpath("//*[contains(@class,'page_title') and (contains(text(),'Claim Center') or contains(text(),'Pusat Klaim'))]"));
    private static final Locator INSURANCE_CLAIM_CENTER_TITLE_LOGIN = new Locator("INSURANCE_CLAIM_CENTER_TITLE_LOGIN", AppiumBy.xpath("//*[contains(@class,'title') and (contains(text(),'Your insurance and claim will be shown here') or contains(text(),'Asuransi dan klaimmu akan ditampilkan di sini'))]"));
    private static final Locator INSURANCE_CLAIM_CENTER_TITLE_GUEST = new Locator("INSURANCE_CLAIM_CENTER_TITLE_GUEST", AppiumBy.xpath("//*[contains(@class,'title') and ((contains(text(),'Welcome to the') and contains(text(), 'Claim Center')) or (contains(text(),'Selamat datang di halaman') and contains(text(), 'Pusat Klaim Asuransi')))]"));
    private static final Locator INSURANCE_CLAIMS_CARDS = new Locator("INSURANCE_CLAIMS_CARDS", AppiumBy.xpath("//div[contains(@class,'OrderCard')]"));
    private static final Locator INSURANCE_CLAIM_CENTER_HISTORY_ICON_LOGIN = new Locator("INSURANCE_CLAIM_CENTER_HISTORY_ICON_LOGIN", AppiumBy.xpath("//*[contains(@class,'MobileRightColumn_menu_icons')]"));
    private static final Locator INSURANCE_CLAIM_CENTER_HISTORY_TAB = new Locator("INSURANCE_CLAIM_CENTER_HISTORY_TAB", AppiumBy.xpath("//*[contains(@class,'Tabs_tab') and (contains(text(),'History') or contains(text(),'Riwayat'))]"));

    public void removeWelcomePopup(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("insurance-claim");
        driverTools.waitForElementToAppear(WELCOME_INSURANCE_CLAIM_CENTER_CLOSE_BTN).ifPresent(driverTools::click);
        driverTools.switchToNative();
    }

    public boolean isOnInsuranceClaimLandingPage(boolean isLogin){
        driverTools.switchToWebViewUrl("insurance-claim");
        String url = driverTools.getCurrentURL();
        boolean isOnCorrectURL = url.toLowerCase().contains("insurance-claim");
        logger.log("is url contains insurance-claim " + isOnCorrectURL);
        boolean isLandingPageContainsCorrectTitle = driverTools.waitForElementToPresence(INSURANCE_CLAIM_CENTER_PAGE_TITLE).isPresent();
        logger.log("is landing page contains correct title " + isLandingPageContainsCorrectTitle);
        if(isLogin){
            boolean isTitleLoginCorrect = driverTools.waitForElementToPresence(INSURANCE_CLAIM_CENTER_TITLE_LOGIN).isPresent();
            boolean hasOrderCards = driverTools.waitForElementToPresence(INSURANCE_CLAIMS_CARDS).isPresent();

            logger.log("is page contains correct title expected for login " + isTitleLoginCorrect);
            logger.log("is page contains claim history " + hasOrderCards);

            boolean isPageContainsHistoryIcon = driverTools.waitForElementToPresence(INSURANCE_CLAIM_CENTER_HISTORY_ICON_LOGIN).isPresent();
            boolean isPageContainsHistoryTab = driverTools.waitForElementToPresence(INSURANCE_CLAIM_CENTER_HISTORY_TAB).isPresent();
            logger.log("is contains history icon " + isPageContainsHistoryIcon);
            logger.log("is contains history tab " + isPageContainsHistoryTab);

            driverTools.switchToNative();
            return isOnCorrectURL && isLandingPageContainsCorrectTitle && (isTitleLoginCorrect || hasOrderCards) && (isPageContainsHistoryIcon || isPageContainsHistoryTab);
        }else {
            boolean isTitleLoginCorrect = driverTools.waitForElementToPresence(INSURANCE_CLAIM_CENTER_TITLE_GUEST).isPresent();
            logger.log("is page contains correct title expected for guest " + isTitleLoginCorrect);
            driverTools.switchToNative();
            return isOnCorrectURL && isLandingPageContainsCorrectTitle && isTitleLoginCorrect;
        }
    }
}
