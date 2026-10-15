package com.tiket.web.page.payment.SavedPaymentMethod;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public abstract class SavedPaymentMethodWeb<T extends WebDriver> extends WebPage<T> {

    public SavedPaymentMethodWeb(Session<T> session){
        super(session);
    }

    public static <T extends WebDriver> SavedPaymentMethodWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new SavedPaymentMethodDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new SavedPaymentMethodMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator  BLIBLI_PAY_CHECK= new Locator("BLIBLI_PAY_CHECK", new By.ByXPath("//*[contains(@id,'BLIPAY')]"));
    private static final Locator  BLIBLI_TIKET_PAY_CHECK= new Locator("BLIBLI_TIKET_PAY_CHECK", new By.ByXPath("//*[contains(@id,'PAY_LATER')]"));

    private static final Locator SAVED_PAYMENT_METHOD_TITLE_PAGE = new Locator("SAVED_PAYMENT_METHOD_TITLE", By.xpath("//span[contains(@class, 'Text_text__') and (contains(text(), 'Payment Methods') or contains(text(), 'Metode Pembayaran'))]"));
    private static final Locator ADD_BANK_ACCOUNT = new Locator("ADD_BANK_ACCOUNT", By.xpath("//span[contains(text(),'refund process') or contains(text(),'proses refund')]"));
    private static final Locator ADD_BANK_NAME = new Locator("ADD_BANK_NAME", By.xpath("//*[@id='bank-name']"));
    private static final Locator INPUT_BANK_NAME = new Locator("INPUT_BANK_NAME", By.xpath("//div[contains(@class,'SearchBox_placeholder')]//input"));
    private static final Locator FIRST_BANK = new Locator("FIRST_BANK", By.xpath("(//span[contains(@class,'BankList_text')])[1]"));
    private static final Locator ACCOUNT_NUMBER = new Locator("ACCOUNT_NUMBER", By.xpath("//input[@id='account-number']"));
    private static final Locator CHECK_BTN = new Locator("CHECK_BTN", By.xpath("//span[contains(@class,'Text_variant_disabled')]"));
    private static final Locator OTP_SECTION = new Locator("OTP_SECTION", By.xpath("//div[contains(@class,'FormOtp_otp_input')]//input"));
    private static final Locator SAVE_ACCOUNT = new Locator("SAVE_ACCOUNT", By.xpath("//button[contains(@class,'Button_variant_primary') and contains(@class,'Button_full_width')]"));
    private static final Locator ADD_CARD = new Locator("ADD_CARD", By.xpath("//div[contains(@class,'Items_item_tile')]//span[text()='Add Card' or text()='Tambah Kartu']"));
    private static final Locator CARD_NUMBER = new Locator("ADD_CARD", By.xpath("//input[@name='cardNumber']"));
    private static final Locator EXPIRY_DATE = new Locator("ADD_CARD", By.xpath("//input[@name='cardExpired']"));
    private static final Locator CVV = new Locator("ADD_CARD", By.xpath("//input[@name='cardCVV']"));
    private static final Locator CARD_HOLDER_NAME = new Locator("ADD_CARD", By.xpath("//input[@name='cardHolderName']"));
    private static final Locator POSTAL_CODE = new Locator("POSTAL_CODE", By.xpath("//input[@name='avsFields.postalCode']"));
    private static final Locator BILLING_ADDRESS = new Locator("BILLING_ADDRESS", By.xpath("//input[@name='cardBillingAddress']"));
    private static final Locator VERIFY_3DS = new Locator("VERIFY_3DS", By.xpath("//input[@id='acssubmit']"));
    private static final Locator CHECK_CARD = new Locator("CHECK_CARD", By.xpath("//span[contains(@class, 'Text_variant_activity') and (contains(text(), 'Credit') or contains(text(), 'Debit'))]"));
    private static final Locator REMOVE_CARD_BUTTON = new Locator("REMOVE_CARD_BUTTON", By.xpath("//span[contains(text(),'Remove card')]"));
    private static final Locator CONFIRM_REMOVE_BUTTON = new Locator("CONFIRM_REMOVE_BUTTON", By.xpath("//span[contains(text(),'Yes, remove it')]"));
    private static final Locator SAVE_CARD_NATIVE_POPUP = new Locator("SAVE_CARD_NATIVE_POPUP", By.xpath("//*[contains(@text,'Save card') or contains(@text,'Simpan kartu')]"));
    private static final Locator NO_THANKS_SAVE_CARD_NATIVE_BUTTON = new Locator("NO_THANKS_SAVE_CARD_NATIVE_BUTTON", By.xpath("//*[contains(@text,'No thanks') or contains(@text,'Tidak, terima kasih')]"));

    public boolean isSavedPaymentMethodTitleAppear(){
        driverTools.waitForElementToAppear(SAVED_PAYMENT_METHOD_TITLE_PAGE);
        return driverTools.isElementDisplayed(SAVED_PAYMENT_METHOD_TITLE_PAGE);
    }

    public boolean isPresentBlibliPay(){
        return driverTools.isElementPresent(BLIBLI_PAY_CHECK);
    }

    public boolean isPresentBlibliTiketPayLater(){
        return driverTools.isElementPresent(BLIBLI_TIKET_PAY_CHECK);
    }

    public boolean waitForSavedPaymentMethodTitleAppear(int timeoutInSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                    .until(driver -> isSavedPaymentMethodTitleAppear());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void addBankAccount(){
        driverTools.waitForElementToAppear(ADD_BANK_ACCOUNT);
        driverTools.clickWithJavascriptExecutor(ADD_BANK_ACCOUNT);
    }

    public void fillBankAccountDetails(String bankName, String otp, String accountNumber) {
        driverTools.waitForElementToAppear(ADD_BANK_NAME);
        driverTools.waitForElementToBeClickable(ADD_BANK_NAME);
        driverTools.clickWithJavascriptExecutor(ADD_BANK_NAME);
        driverTools.waitForElementToAppear(INPUT_BANK_NAME);

        driverTools.clickWithJavascriptExecutor(INPUT_BANK_NAME);
        driverTools.sendKeys(INPUT_BANK_NAME,bankName);

        driverTools.clickWithJavascriptExecutor(FIRST_BANK);

        driverTools.waitForElementToAppear(ACCOUNT_NUMBER);
        driverTools.clickWithJavascriptExecutor(ACCOUNT_NUMBER);

        driverTools.sendKeys(ACCOUNT_NUMBER, accountNumber);
        driverTools.clickWithJavascriptExecutor(CHECK_BTN);

        driverTools.waitForElementToAppear(OTP_SECTION);
        driverTools.clickWithJavascriptExecutor(OTP_SECTION);
        driverTools.sendKeys(OTP_SECTION, otp);

    }

    public void saveAccount() {
        driverTools.waitForElementToAppear(SAVE_ACCOUNT);
        driverTools.clickWithJavascriptExecutor(SAVE_ACCOUNT);
    }

    public void addCard(){
        driverTools.waitForElementToAppear(ADD_CARD);
        driverTools.clickWithJavascriptExecutor(ADD_CARD);
    }

    public void addCardDetails(String cardNumber, String cardDate, String cvv, String cardHolderName){
        driverTools.waitForElementToAppear(CARD_NUMBER);
        driverTools.clickWithJavascriptExecutor(CARD_NUMBER);
        driverTools.sendKeys(CARD_NUMBER,cardNumber);

        driverTools.waitForElementToAppear(EXPIRY_DATE);
        driverTools.clickWithJavascriptExecutor(EXPIRY_DATE);
        driverTools.sendKeys(EXPIRY_DATE, cardDate);

        driverTools.waitForElementToAppear(CVV);
        driverTools.clickWithJavascriptExecutor(CVV);
        driverTools.sendKeys(CVV, cvv);

        driverTools.waitForElementToAppear(CARD_HOLDER_NAME);
        driverTools.clickWithJavascriptExecutor(CARD_HOLDER_NAME);
        driverTools.sendKeys(CARD_HOLDER_NAME, cardHolderName);
    }

    public boolean isAvsPostalCodeDisplayed() {
        return driverTools.isElementDisplayed(POSTAL_CODE);
    }
    
    public void addAvsDetails(String postalCode, String billingAddress) {
        if (driverTools.waitForElementToPresence(POSTAL_CODE, 10).isPresent()) {
            driverTools.waitForElementToAppear(POSTAL_CODE);
            driverTools.clickWithJavascriptExecutor(POSTAL_CODE);
            driverTools.sendKeys(POSTAL_CODE, postalCode);
        }
        if (driverTools.waitForElementToPresence(BILLING_ADDRESS, 5).isPresent()) {
            driverTools.waitForElementToAppear(BILLING_ADDRESS);
            driverTools.clickWithJavascriptExecutor(BILLING_ADDRESS);
            driverTools.sendKeys(BILLING_ADDRESS, billingAddress);
        }
    }

    public void verify3DS(){
        if (driverTools.waitForElementToPresence(VERIFY_3DS, 10).isPresent()) {
            driverTools.waitForElementToAppear(VERIFY_3DS, 10);
            driverTools.clickWithJavascriptExecutor(VERIFY_3DS);
        }
        waitForSavedPaymentMethodTitleAppear(15);
    }

    public boolean isCardAdded() {
        driverTools.waitForElementToAppear(CHECK_CARD);
        return driverTools.isElementPresent(CHECK_CARD);
    }

    public boolean isCardPresent(String cardLastFourDigits) {
        driverTools.refreshWebpage();
        waitForSavedPaymentMethodTitleAppear(10);
        Locator cardLocator = getCardLocator(cardLastFourDigits);
        return driverTools.isElementPresent(cardLocator);
    }

    public void removeCard(String cardLastFourDigits) {
            Locator cardLocator = getCardLocator(cardLastFourDigits);
            if (!driverTools.isElementPresent(cardLocator)) {
                return;
            }
            driverTools.waitForElementToAppear(cardLocator, 10);
            driverTools.clickWithJavascriptExecutor(cardLocator);

            driverTools.waitForElementToAppear(REMOVE_CARD_BUTTON, 10);
            driverTools.waitForElementToBeClickable(REMOVE_CARD_BUTTON, 5).orElseThrow();
            driverTools.clickWithJavascriptExecutor(REMOVE_CARD_BUTTON);

            driverTools.waitForElementToAppear(CONFIRM_REMOVE_BUTTON, 10);
            driverTools.waitForElementToBeClickable(CONFIRM_REMOVE_BUTTON, 5).orElseThrow();
            driverTools.clickWithJavascriptExecutor(CONFIRM_REMOVE_BUTTON);

            driverTools.waitForElementToDisappear(cardLocator, 10);
    }

    private Locator getCardLocator(String cardLastFourDigits) {
        String xpath = String.format("//span[contains(text(),'%s')]", cardLastFourDigits);
        return new Locator("CARD_LOCATOR", By.xpath(xpath));
    }

    public void handleSaveCardPopupNativeIfAny() {
        try {
            driverTools.switchToNative();

            driverTools.waitForElementToAppear(NO_THANKS_SAVE_CARD_NATIVE_BUTTON, 5);
            driverTools.waitForElementToBeClickable(NO_THANKS_SAVE_CARD_NATIVE_BUTTON, 3).orElseThrow();
            driverTools.click(NO_THANKS_SAVE_CARD_NATIVE_BUTTON);
            driverTools.waitForElementToDisappear(SAVE_CARD_NATIVE_POPUP, 5);
        } finally {
            driverTools.switchToWebView();
        }
    }

    public boolean isBankAccountAdded(String accountNumber){
        Locator accountLocator = getAccountNumber(accountNumber) ;
        return driverTools.isElementPresent(accountLocator);
    }

    private Locator getAccountNumber(String accountNumber) {
        String xpath = String.format("//span[contains(@class,'Text_variant_highEmphasis') and text()='%s']", accountNumber);
        return new Locator("ACCOUNT_NUMBER", By.xpath(xpath));
    }

    private static final Locator SELECT_PAYMENT_METHOD(String paymentMethod){
        return new Locator("SELECT_PAYMENT_METHOD", By.xpath(String.format("(//*[text()='%s'])[last()]",paymentMethod)));
    }

    private static final Locator PAYMENT_METHOD_GROUP(String paymentGroup){
        return new Locator("INSTANT_PAYMENT_METHOD_GROUP", By.xpath(String.format("//div[contains(@class,'Group')]//*[text()='%s']",paymentGroup)));
    }
}
