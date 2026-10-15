package com.tiket.page.order.reservation;


import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.tiket.core.CoreFunctions.*;

public class RedeemReservationContactDetailPage<T extends WebDriver> extends BasePage<T> {
    public RedeemReservationContactDetailPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> RedeemReservationContactDetailPage<T> getInstance(Session<T> session) {
        return new RedeemReservationContactDetailPage<>(session);
    }

    private static final Locator OPEN_GUEST_DETAIL = new Locator("OPEN_GUEST_DETAIL", AppiumBy.xpath("//*[@aria-label='open-bottomsheet']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.xpath("//button[contains(@class, 'variant_primary')]//*[text()='Save' or text()='Simpan']"));
    private static final Locator MR_SALUTATION_CHECKBOX_VISITOR = new Locator("MR_SALUTATION_CHECKBOX_VISITOR", AppiumBy.xpath("(//*[contains(@name,'salutation')])[1]"));
    private static final Locator MRS_SALUTATION_CHECKBOX_VISITOR = new Locator("MRS_SALUTATION_CHECKBOX_VISITOR", AppiumBy.xpath("(//*[contains(@name,'salutation')])[2]"));
    private static final Locator MS_SALUTATION_CHECKBOX_VISITOR = new Locator("MS_SALUTATION_CHECKBOX_VISITOR", AppiumBy.xpath("(//*[contains(@name,'salutation')])[3]"));
    private static final Locator FULL_NAME_INPUT = new Locator("FULL_NAME_INPUT", AppiumBy.xpath("//input[@id='nama-lengkap' or @id='full-name']"));
    private static final Locator PHONE_INPUT = new Locator("PHONE_INPUT", AppiumBy.xpath("//input[@id='nomor-ponsel' or @id='phone-number']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", AppiumBy.xpath("//input[@id='email']"));
    private static final Locator RESERVATION_BUTTON_CONTAINER = new Locator("RESERVATION_BUTTON_CONTAINER", AppiumBy.xpath("//*[contains(@class, 'container') and contains(@class, 'ButtonSlider') and not(contains(@class, 'box'))]"));
    private static final Locator RESERVATION_BUTTON = new Locator("RESERVATION_BUTTON", AppiumBy.xpath("//div[contains(@class, 'slider') and contains(@class, 'ButtonSlider') and not(contains(@class, 'container'))]"));

    private Locator selectedSalutationVisitor() {
        Locator[] salutationList = { MR_SALUTATION_CHECKBOX_VISITOR, MRS_SALUTATION_CHECKBOX_VISITOR, MS_SALUTATION_CHECKBOX_VISITOR };
        return getRandomObject(salutationList);
    }

    public void fillGuestDetail() {
        driverTools.switchToWebViewUrl("/visitor-detail");

        driverTools.click(OPEN_GUEST_DETAIL);

        driverTools.clickWithJavascriptExecutor(selectedSalutationVisitor());
        driverTools.sendKeys(FULL_NAME_INPUT, randomName()+" "+randomName());
        if (Configs.PLATFORM == Platform.IOS) {
            driverTools.sendKeys(PHONE_INPUT, randomPhone());
        } else {
            driverTools.sendKeysKeyboard(PHONE_INPUT, randomPhone());
        }
        driverTools.sendKeys(EMAIL_INPUT, randomEmail());
        clickSaveButton();

        driverTools.switchToNative();
    }

    public void clickSaveButton() {
        driverTools.switchToWebViewUrl("/visitor-detail");
        WebElement webElement = driverTools.waitForElementToBeClickable(SAVE_BUTTON).orElseThrow();
        driverTools.click(webElement);
        driverTools.switchToNative();
    }

    public void swipeToReserve() {
        driverTools.switchToWebViewUrl("/visitor-detail");
        driverTools.waitForElementToAppear(RESERVATION_BUTTON);
        driverTools.moveElementToEndOfContainer(RESERVATION_BUTTON, RESERVATION_BUTTON_CONTAINER);
        driverTools.switchToNative();
    }
}
