package com.tiket.page.account.referral;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class ReferralPageAndroid <T extends WebDriver> extends ReferralPage<T>{
    public ReferralPageAndroid(Session<T> session) {
        super(session);
    }

    private static final Locator INVITATION_CONTACT_NAME = new Locator("INVITATION_CONTACT_NAME", AppiumBy.id("tv_name"));
    private static final Locator INVITATION_CONTACT_INVITE_BTN = new Locator("INVITATION_CONTACT_INVITE_BTN", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (@text='Invite' or @text='Ajak')]"));
    private static final Locator BIG_INVITATION_BTN = new Locator("BIG_INVITATION_BTN", AppiumBy.xpath("(//*[contains(@resource-id,'tds_button_view')])[last()]"));
    private static final Locator ANDROID_NATIVE_SHARE_LINK_BTM_SHEET = new Locator("ANDROID_NATIVE_SHARE_LINK_BTM_SHEET", AppiumBy.xpath("//android.widget.ScrollView[@resource-id='android:id/contentPanel']"));
    private static final Locator VERIFY_PHONE_TITLE = new Locator("VERIFY_PHONE_TITLE", AppiumBy.id("tv_title"));
    private static final Locator MOBILE_PHONE_NUMBER_INPUT = new Locator("MOBILE_PHONE_NUMBER_INPUT", AppiumBy.id("tds_box_input_text"));
    private static final Locator VERIFY_PHONE_NUMBER_BTN = new Locator("VERIFY_PHONE_NUMBER_BTN", AppiumBy.id("tds_button_view"));
    private static final Locator CANCEL_BTN = new Locator("CANCEL_BTN", AppiumBy.id("iv_close"));
    private static final Locator QUICK_SHARE_POPUP = new Locator("QUICK_SHARE_POPUP", AppiumBy.xpath("//*[contains(@text,'Quick Share')]"));

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

    private Locator getNativeShareLinkBtmSheetLocator(String userFullname) {
        return  new Locator("ANDROID_NATIVE_SHARE_LINK_BTM_SHEET", AppiumBy
                .xpath("//android.widget.TextView[contains(@text,'"+userFullname+"')]"));
    }

    @Override
    public boolean verifyNativeAppShareDialogPresent(String userFullName,String lang,String referralMessageId,String referralMessageEn) {
        driverTools.click(BIG_INVITATION_BTN);
        if(driverTools.isElementDisplayed(QUICK_SHARE_POPUP)) driverTools.clickElementByCoordinate(QUICK_SHARE_POPUP);
        Assertion.assertThat("Verify android native share link btm sheet", driverTools.isElementDisplayed(ANDROID_NATIVE_SHARE_LINK_BTM_SHEET),is(true));
        String nativeWordingText = driverTools.getText(getNativeShareLinkBtmSheetLocator(userFullName)).orElseThrow();
        String defaultTexString = userFullName + " " + (lang.equalsIgnoreCase("id") ? referralMessageId : referralMessageEn); 
        Assertion.assertThat("Verify native wording text", nativeWordingText.replaceAll("\\s+|[\\t\\n\\r]", " "), containsString(defaultTexString));
        return true;
    }

    @Override
    public boolean verifyPhoneVerificationChallengePopUp(String appLang, String titleEn, String titleId) {
        driverTools.waitForElementToPresence(VERIFY_PHONE_TITLE,15).orElseThrow();
        String chalanegDialogTitle = driverTools.getText(VERIFY_PHONE_TITLE).orElseThrow();
        Assertion.assertThat("Verify title is correct", chalanegDialogTitle, equalTo(appLang.equals("en") ? titleEn : titleId));
        boolean isPhoneVerifyAppear = driverTools.isElementDisplayed(MOBILE_PHONE_NUMBER_INPUT);
        Assertion.assertThat("Verify phone number appear", isPhoneVerifyAppear, is(true));
        return true;
    }

    @Override
    public boolean verificationPopUpDisplayed(){
        driverTools.waitForElementToAppear(VERIFY_PHONE_TITLE,25);
        boolean isVerifyPhoneNumberTitleDisplayed = driverTools.isElementDisplayed(VERIFY_PHONE_TITLE);
        logger.log("is verify phone number title displayed : " + isVerifyPhoneNumberTitleDisplayed);
        boolean isVerifyPhoneNumberBtnDisplayed = driverTools.isElementDisplayed(VERIFY_PHONE_NUMBER_BTN);
        logger.log("is verify phone number btn displayed : " + isVerifyPhoneNumberBtnDisplayed);
        boolean isCancelBtnDisplayed = driverTools.isElementDisplayed(CANCEL_BTN);
        logger.log("is verify phone number cancel btn displayed : " + isCancelBtnDisplayed);
        return isVerifyPhoneNumberTitleDisplayed && isVerifyPhoneNumberBtnDisplayed && isCancelBtnDisplayed;
    }

    @Override
    public void clickCancelBtn(){
        driverTools.click(CANCEL_BTN);
    }

}
