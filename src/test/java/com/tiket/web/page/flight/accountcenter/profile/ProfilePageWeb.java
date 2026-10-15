package com.tiket.web.page.flight.accountcenter.profile;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class ProfilePageWeb <T extends WebDriver> extends WebPage<T> {
    public ProfilePageWeb(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> ProfilePageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new ProfilePageWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new ProfilePageWeb<>(session);
            }
            default -> throw new UnsupportedOperationException("Platform not supported");
        }

    }
    private static final Locator CHANGE_EMAIL = new Locator("CHANGE_EMAIL", By.xpath("(//span[text()='Change'])[2]"));
    private static final Locator CONTINUE_CHANGE_EMAIL = new Locator("CONTINUE_CHANGE_EMAIL", By.xpath("//button[text()='Continue']"));
    private static final Locator NEW_EMAIL_FIELD = new Locator("NEW_EMAIL_FIELD", By.xpath("//input[@name='email']"));
    private static final Locator VERIFY_BTN_EMAIL_CHANGE = new Locator("VERIFY_BTN_EMAIL_CHANGE", By.xpath("//button[contains(@data-testid,'btnSubmitChangeEmail')]"));
    private static final Locator FIND_PHONE_NUMBER = new Locator("FIND_PHONE_NUMBER", By.xpath("//span[text()='Phone Number']"));
    private static final Locator CHANGE_OR_VERIFY_BTN = new Locator("CHANGE_OR_VERIFY_BTN", By.xpath("//span[contains(@data-testid,'contact-preview-change-email')]"));
    private static final Locator CONTINUE_BTN = new Locator("CONTINUE_BTN", By.xpath("//button[contains(@class,'styles_primary_cta')]"));

   
    public boolean verifyAndChangeEmail(String oldEmail,String newEmail){
        LoginPageWeb<T> loginPage = LoginPageWeb.getInstance(session);
        driverTools.switchToWebViewUrl("account");
        boolean isOldEmailExist = driverTools.isElementDisplayed(getCheckEmailLocator(oldEmail));
        logger.log("is old email exist : " + isOldEmailExist);

        if(!driverTools.isElementDisplayed(getCheckEmailVerification(oldEmail))){
            driverTools.click(CHANGE_OR_VERIFY_BTN);
            driverTools.click(VERIFY_BTN_EMAIL_CHANGE);
            loginPage.inputOtp("account");
            driverTools.switchToWebViewUrl("account");
        }
        driverTools.click(CHANGE_OR_VERIFY_BTN);
        driverTools.click(CONTINUE_BTN);
        loginPage.inputOtp("account");

        driverTools.switchToWebViewUrl("account");
        WebElement enterNewEmail = driverTools.waitForElementToAppear(NEW_EMAIL_FIELD).orElseThrow();
        driverTools.sendKeys(enterNewEmail,newEmail);
        driverTools.click(VERIFY_BTN_EMAIL_CHANGE);
        loginPage.inputOtp("account");

        driverTools.switchToWebViewUrl("account");
        boolean isNewEmailExist = driverTools.isElementDisplayed(getCheckEmailLocator(newEmail));
        logger.log("is new email exist : " + isNewEmailExist);
        driverTools.switchToNative();
        return isNewEmailExist;
    }

    private Locator getCheckEmailVerification(String email){
        return new Locator("CHECK_EMAIL_VERIFICATION", By.xpath("//span[contains(@class,'Text_text') and contains(text(),'"+email+"')]//following-sibling::*[contains(@class,'ContactPreview_verified')]"));
    }

    private Locator getCheckEmailLocator(String email){
        return new Locator("CHECK_EMAIL", By.xpath("//span[contains(@class,'Text_text') and contains(text(),'"+email+"')]"));
    }
}
