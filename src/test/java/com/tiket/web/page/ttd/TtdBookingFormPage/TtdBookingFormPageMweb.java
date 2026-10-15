package com.tiket.web.page.ttd.TtdBookingFormPage;

import com.tiket.appium.Swipe;
import com.tiket.model.Exception.ForbiddenException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TtdBookingFormPageMweb<T extends WebDriver> extends TtdBookingFormPageWeb<T> {

    public TtdBookingFormPageMweb(Session<T> session) {
        super(session);
    }

    private static final Locator CONTACT_DETAILS_CONTAINER = new Locator("CONTACT_DETAILS_CONTAINER", By.xpath("//div[@data-testid='contact-detail-card']"));
    // Label lives in a child span; prefer child text over button[contains(text(),...)] (fails on nested spans).
    // Prefer scoped to contact card; fallback for sticky Save rendered outside the card.
    private static final Locator SAVE_CONTACT_DETAILS = new Locator("SAVE_CONTACT_DETAILS", By.xpath("//div[@data-testid='contact-detail-card']//button[contains(@class,'variant_primary') and (.//span[normalize-space()='Simpan' or normalize-space()='Save'] or normalize-space()='Simpan' or normalize-space()='Save')]"));
    private static final Locator SAVE_CONTACT_DETAILS_FALLBACK = new Locator("SAVE_CONTACT_DETAILS_FALLBACK", By.xpath("//button[contains(@class,'variant_primary') and (.//span[normalize-space()='Simpan' or normalize-space()='Save'] or normalize-space()='Simpan' or normalize-space()='Save')]"));
    private static final Locator LPG_LABEL_BOOKING_PAGE = new Locator("LPG_LABEL_BOOKING_PAGE", new By.ByXPath("//span[contains(@class, 'Text_text') and contains(@class, 'Text_variant_invert')]//span[contains(@class, 'Text_weight_regular')]"));
    private static final Locator RADIO_BUTTON_INSURANCE = new Locator("RADIO_BUTTON_INSURANCE", AppiumBy.xpath("//*[contains(@name,'selectedInsurance')]"));
    private static final Locator CONTINUE_PAYMENT_BUTTON = new Locator("CONTINUE_PAYMENT_BUTTON", AppiumBy.xpath("//button[contains(@class, 'KV-nSG_variant_primary') and (normalize-space()='Lanjutkan pembayaran' or normalize-space()='Continue payment' or normalize-space()='Continue to Payment')] | //button[@type='submit']"));

    @Override
    public boolean isLPGLabelPresentOnBookingPage() {
        driverTools.waitForElementsToAppear(LPG_LABEL_BOOKING_PAGE);
        return driverTools.waitForElementsToAppear(LPG_LABEL_BOOKING_PAGE).isPresent();
    }

    @Override
    public void selectSalutationContactDetailsLogin(String salutation) {
        try {
            driverTools.waitForElementToAppear(CONTACT_DETAILS_CONTAINER).ifPresent(driverTools::click);
            selectSalutation(salutation);
            // Save stays disabled until country is set on some login profiles; fill it before clicking.
            if (driverTools.waitForElementToBeClickable(resolveSaveContactDetails(), 5).isEmpty()) {
                selectCountry("indonesia");
            }
            saveContactDetails();
        } catch (ForbiddenException e) {
            logger.softWarning("ForbiddenException occurred: " + e.getMessage());
        }
    }
    @Override
    public void selectCountryContactDetailsLogin(String countryName) {
        try {
            driverTools.waitForElementToAppear(CONTACT_DETAILS_CONTAINER).ifPresent(driverTools::waitForElementToBeClickable);
            driverTools.click(driverTools.element(CONTACT_DETAILS_CONTAINER).orElseThrow());
            selectCountry(countryName);
            if (driverTools.waitForElementToBeClickable(resolveSaveContactDetails(), 5).isEmpty()) {
                selectSalutation("mr");
            }
            saveContactDetails();
        } catch (ForbiddenException e) {
            logger.softWarning("ForbiddenException occurred: " + e.getMessage());
        }
    }

    private Locator resolveSaveContactDetails() {
        if (driverTools.waitForElementToAppear(SAVE_CONTACT_DETAILS, 2).isPresent()) {
            return SAVE_CONTACT_DETAILS;
        }
        return SAVE_CONTACT_DETAILS_FALLBACK;
    }

    private void saveContactDetails() {
        Locator saveBtn = resolveSaveContactDetails();
        driverTools.click(saveBtn);
        driverTools.waitForElementToDisappear(saveBtn, 2);
        if (driverTools.isElementDisplayed(saveBtn)) {
            driverTools.click(saveBtn);
            driverTools.waitForElementToDisappear(saveBtn, 2);
        }
    }

    @Override
    public  void continueToPaymentPage(){
        driverTools.swipeToElement(RADIO_BUTTON_INSURANCE, Swipe.UP,2);
        if (driverTools.isElementDisplayed(RADIO_BUTTON_INSURANCE)) driverTools.click(RADIO_BUTTON_INSURANCE);
        WebElement webElement = driverTools.swipeToElement(CONTINUE_PAYMENT_BUTTON,Swipe.UP,2).orElseThrow();
        driverTools.clickWithJavascriptExecutor(webElement);
    }
}
