package com.tiket.page.accountcenter.profile;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.login.LoginPage;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class ProfilePage <T extends WebDriver> extends BasePage<T> {
    public ProfilePage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> ProfilePage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new ProfilePageAndroid<>(session);
            }
            case IOS -> {
                return new ProfilePageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator CHANGE_OR_VERIFY_BTN = new Locator("CHANGE_OR_VERIFY_BTN", AppiumBy.xpath("//span[contains(@data-testid,'contact-preview-change-email')]"));
    private static final Locator CONTINUE_BTN = new Locator("CONTINUE_BTN", AppiumBy.xpath("//button[contains(@class,'styles_primary_cta')]"));
    private static final Locator NEW_EMAIL_FIELD = new Locator("NEW_EMAIL_FIELD", AppiumBy.xpath("//input[@name='email']"));
    private static final Locator VERIFY_BTN_EMAIL_CHANGE = new Locator("VERIFY_BTN_EMAIL_CHANGE", AppiumBy.xpath("//button[contains(@data-testid,'btnSubmitChangeEmail')]"));

    public boolean verifyAndChangeEmail(String oldEmail,String newEmail){
        LoginPage<T> loginPage = LoginPage.getInstance(session);
        driverTools.switchToWebViewUrl("account/edit");
        boolean isOldEmailExist = driverTools.isElementDisplayed(getCheckEmailLocator(oldEmail));
        logger.log("is old email exist : " + isOldEmailExist);

        if(!driverTools.isElementDisplayed(getCheckEmailVerification(oldEmail))){
            driverTools.click(CHANGE_OR_VERIFY_BTN);
            driverTools.click(VERIFY_BTN_EMAIL_CHANGE);
            loginPage.inputOtp("account");
            driverTools.switchToWebViewUrl("account/edit");
        }
        driverTools.click(CHANGE_OR_VERIFY_BTN);
        driverTools.click(CONTINUE_BTN);
        loginPage.inputOtp("account");

        driverTools.switchToWebViewUrl("account/edit");
        WebElement enterNewEmail = driverTools.waitForElementToAppear(NEW_EMAIL_FIELD).orElseThrow();
        driverTools.sendKeys(enterNewEmail,newEmail);
        driverTools.click(VERIFY_BTN_EMAIL_CHANGE);
        loginPage.inputOtp("account");

        driverTools.switchToWebViewUrl("account/edit");
        boolean isNewEmailExist = driverTools.isElementDisplayed(getCheckEmailLocator(newEmail));
        logger.log("is new email exist : " + isNewEmailExist);
        driverTools.switchToNative();
        return isNewEmailExist;
    }

    private Locator getCheckEmailLocator(String email){
        return new Locator("CHECK_EMAIL", AppiumBy.xpath("//span[contains(@class,'Text_text') and contains(text(),'"+email+"')]"));
    }

    private Locator getCheckEmailVerification(String email){
        return new Locator("CHECK_EMAIL_VERIFICATION", AppiumBy.xpath("//span[contains(@class,'Text_text') and contains(text(),'"+email+"')]//following-sibling::*[contains(@class,'ContactPreview_verified')]"));
    }
}
