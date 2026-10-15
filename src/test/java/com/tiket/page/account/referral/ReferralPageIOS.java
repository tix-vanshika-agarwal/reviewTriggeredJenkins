package com.tiket.page.account.referral;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.hamcrest.Matchers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class ReferralPageIOS <T extends WebDriver> extends ReferralPage<T>{
    public ReferralPageIOS(Session<T> session) {
        super(session);
    }
    private static final Locator VERIFY_PHONE_TITLE = new Locator("VERIFY_PHONE_TITLE", AppiumBy.xpath("//div[@class='dialog-popup']//h2"));
    private static final Locator MOBILE_PHONE_NUMBER_INPUT = new Locator("MOBILE_PHONE_NUMBER_INPUT", AppiumBy.xpath("//div[@class='dialog-popup']//input"));
    private static final Locator VERIFY_PHONE_TITLE_NATIVE = new Locator("VERIFY_PHONE_TITLE_NATIVE", AppiumBy.xpath("//XCUIElementTypeButton[@name=\"Verify mobile number\" or @name=\"Verifikasi Nomor Ponsel\"]"));
    private static final Locator INVITATION_CONTACT_NAME = new Locator("INVITATION_CONTACT_NAME", AppiumBy.xpath("//XCUIElementTypeStaticText[following-sibling::XCUIElementTypeButton[contains(@name,'Invite') or contains(@name,'Ajak')]]"));
    private static final Locator INVITATION_CONTACT_INVITE_BTN = new Locator("INVITATION_CONTACT_INVITE_BTN", AppiumBy.xpath("//XCUIElementTypeButton[@name='Invite' or @name='Ajak']"));
    private static final Locator BIG_INVITATION_BTN = new Locator("BIG_INVITATION_BTN", AppiumBy.xpath("//XCUIElementTypeButton[@label=\"Ajak lebih banyak\" or @name=\"Invite more Friends\"]"));
    private static final Locator IOS_NATIVE_SHARE_LINK_BTM_SHEET = new Locator("IOS_NATIVE_SHARE_LINK_BTM_SHEET", AppiumBy.accessibilityId("ActivityListView"));
    private static final Locator IOS_NATIVE_WORDING_TEXT = new Locator("IOS_NATIVE_WORDING_TEXT", AppiumBy.xpath("//XCUIElementTypeNavigationBar[@name=\"UIActivityContentView\"]//*//*"));

    @Override
    public boolean verifyListOfContactPresent() {
        driverTools.waitForElementToAppear(INVITATION_CONTACT_NAME, 10);
        //test account should have at least 1 passenger data
        List<WebElement> listOfContact = driverTools.elements(INVITATION_CONTACT_NAME).orElseThrow();
        Assertion.assertThat("Verify contact name is larger 1", listOfContact.size(), greaterThan(0));

        List<WebElement> listOfContactPhoneNumber = driverTools.elements(INVITATION_CONTACT_INVITE_BTN).orElseThrow();
        Assertion.assertThat("Verify contact phone number is larger 1", listOfContactPhoneNumber.size(), greaterThan(0));
        return true;
    }

    @Override
    public boolean verifyNativeAppShareDialogPresent(String userFullName, String lang, String referralMessageId, String referralMessageEn) {
        driverTools.click(BIG_INVITATION_BTN);
        Assertion.assertThat("Verify android native share link btm sheet", driverTools.waitForElementToAppear(IOS_NATIVE_SHARE_LINK_BTM_SHEET, 5).isPresent(), is(true));
        String nativeWordingText = driverTools.getText(IOS_NATIVE_WORDING_TEXT).orElseThrow();
        String[] words = nativeWordingText.split("\\s+", 3);
        nativeWordingText = words[0] + " " + words[2];
        String defaultTexString = userFullName + " " + (lang.equalsIgnoreCase("id") ? referralMessageId : referralMessageEn);
        Assertion.assertThat("Verify native wording text", nativeWordingText, containsString(defaultTexString));
        return true;
    }

    @Override
    public boolean verifyPhoneVerificationChallengePopUp(String appLang, String titleEn, String titleId) {
        if(driverTools.isElementDisplayed(VERIFY_PHONE_TITLE_NATIVE)) {
            return true;
        } else if (driverTools.waitForWebview()) {
            driverTools.switchToWebView();
            driverTools.waitForElementToAppear(VERIFY_PHONE_TITLE, 10).orElseThrow();
            String chalanegDialogTitle = driverTools.getText(VERIFY_PHONE_TITLE).orElseThrow();
            Assertion.assertThat("Verify title is correct", chalanegDialogTitle, equalTo(appLang.equals("en") ? titleEn : titleId));
            boolean isPhoneVerifyAppear = driverTools.isElementDisplayed(MOBILE_PHONE_NUMBER_INPUT);
            Assertion.assertThat("Verify phone number appear", isPhoneVerifyAppear, Matchers.is(true));
            driverTools.switchToNative();
            return true;
        } else {
            logger.warn("Locator not found in either web view or native context.");
            return false;
        }
    }
}
