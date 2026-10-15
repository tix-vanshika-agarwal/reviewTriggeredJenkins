package com.tiket.web.page.ttd.TtdBookingFormPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TtdBookingFormPageDweb<T extends WebDriver> extends TtdBookingFormPageWeb<T> {

    public TtdBookingFormPageDweb(Session<T> session) {
        super(session);
    }

    private static final Locator CONTINUE_PAYMENT_BUTTON = new Locator("CONTINUE_PAYMENT_BUTTON", AppiumBy.xpath("//button[contains(@class, 'KV-nSG_variant_primary') and (normalize-space()='Lanjutkan pembayaran' or normalize-space()='Continue payment' or normalize-space()='Continue to Payment')] | //button[@type='submit']"));

    /**
     * Overrides the parent method to use JavaScript scroll for desktop web instead of Appium swipe.
     * @param times number of times to scroll (each scroll moves 200 pixels)
     */
    @Override
    public void scrollPageDown(int times) {
        for (int i = 0; i < times; i++) {
            driverTools.scrollDown(200);
        }
    }

    @Override
    public void selectSalutationContactDetailsLogin(String salutation) {
        selectSalutation(salutation);
    }

    @Override
    public void selectCountryContactDetailsLogin(String countryName) {
        selectCountry(countryName);
    }

    @Override
    public void continueToPaymentPage() {
        driverTools.waitForElementToPresence(CONTINUE_PAYMENT_BUTTON);
        driverTools.scrollElementToCenter(CONTINUE_PAYMENT_BUTTON);
        driverTools.click(CONTINUE_PAYMENT_BUTTON);
    }
}
