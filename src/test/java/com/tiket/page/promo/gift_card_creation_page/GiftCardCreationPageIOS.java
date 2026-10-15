package com.tiket.page.promo.gift_card_creation_page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

import static com.tiket.core.CoreFunctions.randomName;
import static com.tiket.core.CoreFunctions.randomPhone;

public class GiftCardCreationPageIOS<T extends WebDriver> extends GiftCardCreationPage<T> {
    public GiftCardCreationPageIOS(Session<T> session) {
        super(session);
    }

    private static final Locator EDIT_RECIPIENT_BUTTON = new Locator("EDIT_RECIPIENT_BUTTON", AppiumBy.xpath("//*[contains(@label, 'Recipient') or contains(@label, 'Penerima')]//following-sibling::XCUIElementTypeImage"));
    private static final Locator RECIPIENT_NAME_INPUT = new Locator("RECIPIENT_NAME_INPUT", AppiumBy.xpath("(//*[(@value='Recipient Name' or @value='Nama Penerima') and @type='XCUIElementTypeTextField'])[1]"));
    private static final Locator PHONE_INPUT = new Locator("PHONE_INPUT", AppiumBy.xpath("(//*[(@value='Mobile Number' or @value='Nomor HP' or @value='Phone Number') and @type='XCUIElementTypeTextField'])[1]"));
    private static final Locator FIRST_PRICE_CHIP = new Locator("FIRST_PRICE_CHIP", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@name, 'nominal') or contains(@name, 'amount')]//following-sibling::XCUIElementTypeButton)[1]"));
    private static final Locator AMOUNT_INPUT = new Locator("AMOUNT_INPUT", AppiumBy.xpath("//XCUIElementTypeTextField[contains(@name,'Enter the amount') or contains(@name,'Masukkan Nominal') or contains(@value,'Enter the amount') or contains(@value,'Masukkan Nominal')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name, 'Save') or contains(@name, 'Simpan')]"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='Continue' or @name='Lanjutkan']"));
    private static final Locator AMOUNT_ERROR = new Locator("AMOUNT_ERROR", AppiumBy.xpath("//*[contains(@name, 'amount must not exceed') or contains(@name, 'Nominal gift card tidak boleh lebih dari')]"));

    @Override
    public void fillInCardDetail() {
        driverTools.click(EDIT_RECIPIENT_BUTTON);
        driverTools.waitForElementToPresence(RECIPIENT_NAME_INPUT);
        driverTools.sendKeys(RECIPIENT_NAME_INPUT, randomName());
        driverTools.sendKeys(PHONE_INPUT, randomPhone());
        driverTools.hideKeyboard();
        driverTools.click(FIRST_PRICE_CHIP);
        driverTools.click(SAVE_BUTTON);
    }

    @Override
    public void fillInCardDetail(int specificAmount) {
        driverTools.click(EDIT_RECIPIENT_BUTTON);
        driverTools.waitForElementToPresence(RECIPIENT_NAME_INPUT);
        driverTools.sendKeys(RECIPIENT_NAME_INPUT, randomName());
        driverTools.sendKeys(PHONE_INPUT, randomPhone());
        driverTools.hideKeyboard();
        driverTools.click(AMOUNT_INPUT);
        driverTools.sendKeys(AMOUNT_INPUT, String.valueOf(specificAmount));
        driverTools.hideKeyboard();
        driverTools.click(SAVE_BUTTON);
    }

    @Override
    public boolean isAmountErrorShown() {
        return driverTools.waitForElementToAppear(AMOUNT_ERROR, 8).isPresent();
    }

    @Override
    public void clickContinueButtonFooter() {
        driverTools.click(CONTINUE_BUTTON);
    }
}
