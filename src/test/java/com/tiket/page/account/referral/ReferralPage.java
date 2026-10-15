package com.tiket.page.account.referral;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class ReferralPage<T extends WebDriver> extends BasePage<T> {
    public ReferralPage(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> ReferralPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new ReferralPageAndroid<>(session);}
            case IOS -> {return new ReferralPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator VERIFY_PHONE_NUMBER_BTN = new Locator("VERIFY_PHONE_NUMBER_BTN", AppiumBy.xpath("//*[contains(@class,'variant_primary') and contains(text(),'Verifikasi nomor ponsel') or contains(text(),'Verify mobile number')]"));
    private static final Locator CANCEL_BTN = new Locator("CANCEL_BTN", AppiumBy.xpath("//*[contains(@class,'variant_secondary') and contains(text(),'Batal') or contains(text(),'Cancel')]"));
    private static final Locator VERIFY_PHONE_TITLE = new Locator("VERIFY_PHONE_TITLE", AppiumBy.xpath("//div[@class='dialog-popup']//h2"));
    private static final Locator INVITE_FRIENDS_BTN = new Locator("INVITE_FRIENDS_BTN", AppiumBy.xpath("//*[contains(@class,'tix-button') and (contains(text(),'Ajak Temanmu') or contains(text(),'Invite Friends'))]"));
    private static final Locator BACK_BTN = new Locator("BACK_BTN", AppiumBy.xpath("//*[@class='back-btn']//*[contains(@class,'tix-icon')]"));
    private static final Locator REFERRAL_PAGE_TITLE_WEB_VIEW = new Locator("REFERRAL_PAGE_TITLE_WEB_VIEW", AppiumBy.xpath("//*[contains(text(),'How to get your reward') or contains(text(),'Cara dapatkan hadiah')]"));
    private static final Locator OVERLAYS = new Locator("OVERLAYS", AppiumBy.xpath("//*[@class='overlay']"));

    public boolean verificationPopUpDisplayed(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();

        driverTools.waitForElementToAppear(VERIFY_PHONE_TITLE,25);

        boolean isVerifyPhoneNumberTitleDisplayed = driverTools.isElementDisplayed(VERIFY_PHONE_TITLE);
        logger.log("is verify phone number title displayed : " + isVerifyPhoneNumberTitleDisplayed);
        boolean isVerifyPhoneNumberBtnDisplayed = driverTools.isElementDisplayed(VERIFY_PHONE_NUMBER_BTN);
        logger.log("is verify phone number btn displayed : " + isVerifyPhoneNumberBtnDisplayed);
        boolean isCancelBtnDisplayed = driverTools.isElementDisplayed(CANCEL_BTN);
        logger.log("is verify phone number btn displayed : " + isCancelBtnDisplayed);
        driverTools.switchToNative();
        return isVerifyPhoneNumberTitleDisplayed && isVerifyPhoneNumberBtnDisplayed && isCancelBtnDisplayed;
    }

    public void clickCancelBtn(){
        driverTools.switchToWebView();
        driverTools.click(CANCEL_BTN);
        driverTools.switchToNative();
    }

    public void removeReferralPopup(){
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(OVERLAYS).ifPresent(driverTools::click);
        driverTools.switchToNative();
    }

    public void shareReferralInvitation(){
        removeReferralPopup();
        driverTools.switchToWebView();
        driverTools.click(INVITE_FRIENDS_BTN);
        driverTools.switchToNative();
    }

    public void clickBackBtn(){
        driverTools.switchToWebView();
        driverTools.click(BACK_BTN);
        driverTools.switchToNative();
    }

    public boolean isReferralPageOpen(){
        driverTools.waitForWebview();
        removeReferralPopup();
        driverTools.switchToWebViewUrl("referral");
        String getUrlOfPage = driverTools.getCurrentURL();
        boolean isUrlContainsReferralPage = getUrlOfPage.toLowerCase().contains("referral");
        logger.log("does url contains referral" + isUrlContainsReferralPage);

        boolean isReferralPageTitleDisplayed = driverTools.isElementDisplayed(REFERRAL_PAGE_TITLE_WEB_VIEW);
        logger.log("is referral page title displayed " + isReferralPageTitleDisplayed);

        boolean isInviteFriendsBtnDisplayed = driverTools.isElementDisplayed(INVITE_FRIENDS_BTN);
        logger.log("is invite friends btn displayed " + isInviteFriendsBtnDisplayed);
        driverTools.switchToNative();
        return isUrlContainsReferralPage && isReferralPageTitleDisplayed && isInviteFriendsBtnDisplayed;
    }
    
    public abstract boolean verifyPhoneVerificationChallengePopUp(String appLang,String titleEn,String titleId);
    public abstract boolean verifyListOfContactPresent();
    public abstract boolean verifyNativeAppShareDialogPresent(String userFullName, String lang,String expectedLangId,String expectedLangEn);
}
