package com.tiket.web.dWeb.page.nft.train;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.NoSuchElementException;

public  class TrainBookingFormPage<T extends WebDriver> extends BasePage<T> {

    public TrainBookingFormPage(Session<T> session) {
        super(session);
    }

    private static final Locator LOGIN_BANNER = new Locator("LOGIN_BANNER", new By.ByXPath("//div[contains(@class,'InfoBanner_click')]"));
    private static final Locator TOGGLE_KNOB = new Locator("TOGGLE_KNOB", new By.ByXPath("//div[contains(@class, 'Toggle_knob__') and contains(@class, 'Toggle_knob_small__')]"));
    private static final Locator CONTINUE_BTN = new Locator("CONTINUE_BTN", new By.ByXPath("//button[contains(@class, 'booking_footer_button__') and contains(@class, 'Button_variant_primary__')]"));
    private static final Locator CONTINUE_TO_PAY_BTN = new Locator("CONTINUE_TO_PAY_BTN", new By.ByXPath("//button[normalize-space()='Lanjutkan' or normalize-space()='Continue to pay']"));
    private static final Locator CONTINUE_TO_PAYMENT_POPUP = new Locator("CONTINUE_TO_PAYMENT_POPUP", new By.ByXPath("//div[contains(@class, 'Dialog_pop_up')]//button[contains(@class, 'Button_button__') and contains(@class, 'Button_variant_primary__')]"));
    private static final Locator CONTINUE_TO_PAYMENT_CORPORATE_BOOKING_FORM = new Locator("CONTINUE_TO_PAYMENT_CORPORATE_BOOKING_FORM", new By.ByXPath("//button[contains(@class,'Button_button') and contains(text(),'Lanjut bayar') or contains(text(),'Continue to payment')]"));
    private static final Locator CONTINUE_TO_PAYMENT_INTERIM_PAGE = new Locator("CONTINUE_TO_PAYMENT_INTERIM_PAGE", new By.ByXPath("//span[contains(@class, 'rr___Button-module__truncation_not_applied') and contains(@class, 'rr___Text-module__text')]"));
    private static final Locator SAVE_BOOKING_FORM= new Locator("SAVE_BOOKING_FORM", new By.ByXPath("//button[normalize-space(text())='Simpan' or normalize-space(text())='Save']"));


    public void clickLoginBanner() {
        driverTools.waitForElementToAppear(LOGIN_BANNER).orElseThrow();
        driverTools.click(LOGIN_BANNER);
    }

    public void clickToggleKnob() {
        driverTools.waitForElementToAppear(TOGGLE_KNOB, 6).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(SAVE_BOOKING_FORM, 6).ifPresent(driverTools::click);

    }

    public void continueToSecondHalf() {
        if (driverTools.waitForElementToPresence(CONTINUE_BTN).isPresent()) {
            driverTools.clickWithJavascriptExecutor(CONTINUE_BTN);
        } else {
            throw new NoSuchElementException("Continue button (CONTINUE_BTN) is not present on the page.");
        }
    }

    public void continueToPayBtn() {
        driverTools.waitForElementToAppear(CONTINUE_TO_PAY_BTN);
        driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAY_BTN);
        driverTools.waitForElementToPresence(CONTINUE_TO_PAYMENT_POPUP, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
        if (driverTools.waitForElementToPresence(CONTINUE_TO_PAYMENT_INTERIM_PAGE).isPresent()) {
            driverTools.click(CONTINUE_TO_PAYMENT_INTERIM_PAGE);
        } else {
            throw new NoSuchElementException("Continue button (CONTINUE_BTN) is not present on the Interim page.");
        }
        driverTools.waitForElementToPresence(CONTINUE_TO_PAYMENT_CORPORATE_BOOKING_FORM).ifPresent(driverTools::click);
    }

    public boolean isLoginSuccessful(String username) {
        driverTools.waitForElementToAppear(getInputEmail(username),10);
        boolean isLoginBannerDisplayed = driverTools.isElementDisplayed(LOGIN_BANNER);
        logger.log("Login banner displayed: " + isLoginBannerDisplayed);

        boolean getInputEmailDisplayed = driverTools.isElementDisplayed(getInputEmail(username));
        logger.log("Input email displayed: " + getInputEmailDisplayed);
        return !isLoginBannerDisplayed && getInputEmailDisplayed;
    }

    private Locator getInputEmail(String username) {
        return new Locator("INPUT_EMAIL", new By.ByXPath("//*[@name='emailAddress' and @value='"+username+"']"));
    }
}
