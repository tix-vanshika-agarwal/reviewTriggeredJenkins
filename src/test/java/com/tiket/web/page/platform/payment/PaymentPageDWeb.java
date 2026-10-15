package com.tiket.web.page.platform.payment;

import com.tiket.api.accommodation.hotel.HotelPendingRetrySummaryNotifier;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.WebElement;

public class PaymentPageDWeb<T extends WebDriver> extends PaymentPageWeb<T> {
    
     public PaymentPageDWeb(Session<T> session) {
        super(session);
    }

    private static final Locator SEE_PROMOS_VOUCHERS = new Locator("SEE_PROMOS_VOUCHERS", new By.ByXPath("//div[contains(@class,'PromoCard_promo_row')]"));
    private static final Locator PROMO_CLOSE_DRAWER = new Locator("PROMO_CLOSE_DRAWER", new By.ByXPath("//button[contains(@class,'Modal_modal_close')]"));
    private static final Locator TRANSACTION_FEE = new Locator("TRANSACTION_FEE", new By.ByXPath("(//span[text()='Transaction fee' or text()='Biaya transaksi'])[2]"));
    private static final Locator FIRST_PAYMENT_METHOD = new Locator("FIRST_PAYMENT_METHOD", new By.ByXPath("//ul[contains(@class, 'EligibleMethods_groups')]//li[1]//span"));
    private static final Locator PAYMENT_SUCCESSFUL_DIV_HEADING = new Locator("PAYMENT_SUCCESSFUL_DIV_HEADING", new By.ByXPath("//h1[text()='Yay! Payment Successful' or text()='Hore! Pembayaran Berhasil']"));
    private static final Locator INSTALLMENT_TENURE = new Locator("INSTALLMENT_TENURE", new By.ByXPath("//input[@placeholder='Installment' or @placeholder='Cicilan']"));
    private static final Locator TENURE_3X = new Locator("TENURE_3X", new By.ByXPath("//label[contains(@class, 'CardInstallment_installment_option')][2]"));
    private static final Locator TENURE_FULL_PAYMENT = new Locator("TENURE_FULL_PAYMENT", new By.ByXPath("//label[contains(@class, 'CardInstallment_installment_option')]"));
    private static final Locator OPEN_SEAT_PRICE = new Locator("OPEN_SEAT_PRICE", By.xpath("(//span[contains(@class,'PriceDetailV4_accordion_header')])[2]"));
    private static final Locator INITIAL_ORDER_PRICE = new Locator("INITIAL_ORDER_PRICE", By.xpath("(//span[contains(@class,'Text_text') and contains(@class,'Text_size_b2') and contains(text(),'IDR')])[5]"));
    private static final Locator INSTALLMENT = new Locator("INSTALLMENT", new By.ByXPath("(//div[contains(@class, 'CardInstallment_installment_detail')])[2]"));

    private static Locator SELECT_PAYMENT_METHOD(String paymentMethod){
        return new Locator("SELECT_PAYMENT_METHOD", By.xpath(String.format(
                "//li[.//span[contains(@class,'Text_text') and normalize-space()='%s']]", paymentMethod)));
    }

    private static Locator SELECT_PAYMENT_METHOD_CC_TOKENIZE(String paymentMethod){
        return new Locator("SELECT_PAYMENT_METHOD_CC_TOKENIZE", By.xpath(String.format("//div[contains(@class,'Group')]//span[contains(text(), '%s')]",paymentMethod)));
    }

    public void maximize() {
        driverTools.maximizeWindow();
    }

    public void clickPromoSuggestion() {
        driverTools.waitForElementToBeClickable(SEE_PROMOS_VOUCHERS, 10);
        driverTools.click(SEE_PROMOS_VOUCHERS);
        driverTools.waitForElementToPresence(getIndexEligiblePromo(1));
    }

    public void goToPaymentMethodsGroup(String paymentGroup){
        driverTools.waitForElementToAppear(PAYMENT_DRAWER_HEADING);
        driverTools.waitForElementToAppear(paymentMethodGroup(paymentGroup));
        driverTools.scrollIntoWebView(paymentMethodGroup(paymentGroup));
    }

    @Override
    public void selectPaymentMethod(String paymentMethod){
        WebElement element = driverTools.waitForElementToPresence(SELECT_PAYMENT_METHOD(paymentMethod)).orElseThrow();
        driverTools.scrollIntoWebView(element);
        driverTools.clickWithJavascriptExecutor(element);
        isPaymentPageLoaded();
    }

    public void clickClosePromoSuggestion() {
        driverTools.waitForElementToAppear(PROMO_CLOSE_DRAWER).ifPresent(driverTools::click);
    }

    @Override
    public String getTextOnLoginNudge(){
        return null;
    }

    @Override
    public boolean isPromoInstantUnApplied(){
        driverTools.waitForElementToAppear(SEE_PROMOS_VOUCHERS);
        return driverTools.isElementDisplayed(SEE_PROMOS_VOUCHERS);
    }

    @Override
    public Locator getTransactionFeeLocator() {
        return TRANSACTION_FEE;
    }

    @Override
    public void clickOnFirstPaymentMethod() {
        driverTools.waitForElementToAppear(FIRST_PAYMENT_METHOD);
        driverTools.clickWithJavascriptExecutor(FIRST_PAYMENT_METHOD);
    }

    public void selectPaymentMethodCCTokenize(String paymentMethod){
        WebElement element = driverTools.waitForElementToAppear(SELECT_PAYMENT_METHOD_CC_TOKENIZE(paymentMethod)).orElseThrow();
        driverTools.scrollIntoWebView(element);
        driverTools.clickWithJavascriptExecutor(element);
        isPaymentPageLoaded();
    }

    @Override
    public boolean isPaymentSuccessfulVisible(){
        driverTools.waitForElementToAppear(PAYMENT_SUCCESSFUL_DIV_HEADING, 120);
        boolean visible = driverTools.isElementDisplayed(PAYMENT_SUCCESSFUL_DIV_HEADING);
        if (visible) {
            HotelPendingRetrySummaryNotifier.afterWebPaymentSuccess(session, apiTools, logger);
        }
        return visible;
    }

    public void changeTenure() {
        driverTools.waitForElementToAppear(INSTALLMENT_TENURE);
        driverTools.clickWithJavascriptExecutor(INSTALLMENT_TENURE);
        driverTools.waitForElementToBeClickable(TENURE_3X);
        driverTools.clickWithJavascriptExecutor(TENURE_3X);
    }

    @Override
    public void changeTenureToAnother() {
        driverTools.waitForElementToAppear(INSTALLMENT_TENURE);
        driverTools.clickWithJavascriptExecutor(INSTALLMENT_TENURE);
        driverTools.waitForElementToBeClickable(TENURE_FULL_PAYMENT);
        driverTools.clickWithJavascriptExecutor(TENURE_FULL_PAYMENT);
    }

    public String getInitialPrice() {
        driverTools.waitForElementToAppear(OPEN_SEAT_PRICE).ifPresent(driverTools::click);
        WebElement initialPrice = driverTools.waitForElementToAppear(INITIAL_ORDER_PRICE).orElseThrow();
        String initialPriceText = driverTools.getText(initialPrice);
        return initialPriceText.replaceAll("[^0-9]", "");
    }

    @Override
    public void selectInstallment(){
        if (isPaylaterInstallmentDropdownDisplayed()) {
            selectPaylaterInstallment();
            return;
        }
        driverTools.waitForElementToAppear(INSTALLMENT_CONTAINER);
        driverTools.clickWithJavascriptExecutor(INSTALLMENT_CONTAINER);
        driverTools.waitForElementToAppear(INSTALLMENT);
        driverTools.clickWithJavascriptExecutor(INSTALLMENT);
    }

    public void clickOnPaymentGroup(String paymentGroup){
        driverTools.clickWithJavascriptExecutor(paymentMethodGroup(paymentGroup));
    }

    static final Locator paymentMethodGroup(String paymentGroup) {
        String[] labels = splitPaymentGroupLabels(paymentGroup);
        String xpath = labels.length == 2
                ? String.format(
                "//span[contains(@class,'PaymentGroup_title') and (contains(normalize-space(.),'%s') or contains(normalize-space(.),'%s'))]",
                labels[0], labels[1])
                : String.format(
                "//span[contains(@class,'PaymentGroup_title') and contains(normalize-space(.),'%s')]",
                labels[0]);
        return new Locator("INSTANT_PAYMENT_METHOD_GROUP", By.xpath(xpath));
    }

    public void clickClosePaymentMethodGroup() {
        logger.log("On DWeb, no need to close payment method group");
    }
}
