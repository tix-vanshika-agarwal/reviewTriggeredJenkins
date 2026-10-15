package com.tiket.web.page.flight.rescheduleCart;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FlightRescheduleCartWeb <T extends WebDriver> extends WebPage<T> {
    public FlightRescheduleCartWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightRescheduleCartWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new FlightRescheduleCartDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new FlightRescheduleCartMWeb<>(session);
            }
            default -> throw new UnsupportedOperationException("Platform not supported");
        }
    }

    private final static Locator CONTINUE_TO_PAYMENT_BUTTON = new Locator("CONTINUE_TO_PAYMENT_BUTTON", By.xpath("//div[contains(@class, 'BookingFooter')]//button[contains(@class, 'Button_variant_primary')]"));
    private final static Locator CONTINUE_TO_PAYMENT_CONFIRM_BUTTON = new Locator("CONTINUE_TO_PAYMENT_CONFIRM_BUTTON", By.xpath("//div[contains(@class, 'Dialog_pop_up')]//button[contains(@class, 'Button_variant_primary')]"));
    private final static Locator CONFIRM_PRICE_CHANGES_BUTTON = new Locator("CONFIRM_PRICE_CHANGES_BUTTON", By.xpath("//button[.='OK, Mengerti' or .='OK, Got It']"));

    public void clickContinueToPayment() {
        driverTools.waitForElementToPresence(CONTINUE_TO_PAYMENT_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAYMENT_BUTTON);
    }

    public void confirmContinueToPayment() {
        driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT_CONFIRM_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONTINUE_TO_PAYMENT_CONFIRM_BUTTON);
    }

    public void confirmPriceChanges() {
        driverTools.waitForElementToAppear(CONFIRM_PRICE_CHANGES_BUTTON, 20).ifPresent(element -> driverTools.click(element));
    }
}
