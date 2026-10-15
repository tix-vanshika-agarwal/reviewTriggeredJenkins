package com.tiket.web.page.platform.payment;

import com.tiket.api.accommodation.hotel.HotelPendingRetrySummaryNotifier;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.WebElement;

public class PaymentPageMWeb <T extends WebDriver> extends PaymentPageWeb<T>{
    public PaymentPageMWeb(Session<T> session) {
        super(session);
    }

    private static final Locator SEE_PROMOS_VOUCHERS = new Locator("SEE_PROMOS_VOUCHERS", new By.ByXPath("//span[text()='See all promos/voucher codes' or text()='Lihat semua promo/voucher code']"));
    private static final Locator TRANSACTION_FEE = new Locator("TRANSACTION_FEE", new By.ByXPath("(//span[text()='Transaction fee' or text()='Biaya transaksi'])[1]"));
    private static final Locator FIRST_PAYMENT_METHOD = new Locator("FIRST_PAYMENT_METHOD", new By.ByXPath("//div[contains(@class,'EligibleMethodItem_payment_method_wrapper')]"));
    private static final Locator INSTALLMENT_TENURE = new Locator("INSTALLMENT_TENURE", new By.ByXPath("//input[@placeholder='Installment' or @placeholder='Cicilan']"));
    private static final Locator TENURE_3X = new Locator("TENURE_3X", new By.ByXPath("//label[contains(@class, 'CardInstallmentBottomSheet_installment_option')][2]"));
    private static final Locator TENURE_FULL_PAYMENT = new Locator("TENURE_FULL_PAYMENT", new By.ByXPath("//label[contains(@class, 'CardInstallmentBottomSheet_installment_option')]"));
    private static final Locator CLICK_SAVE = new Locator("CLICK_SAVE", new By.ByXPath("//button[text()='Save' or text()='Simpan']"));
    private static final Locator OPEN_SEAT_PRICE = new Locator("OPEN_SEAT_PRICE", By.xpath("(//*[@data-testid='accordion-icon'])[1]"));
    private static final Locator INITIAL_ORDER_PRICE = new Locator("INITIAL_ORDER_PRICE", By.xpath("(//li[contains(@class, 'PriceDetail')]//span[contains(@class, 'Text') and contains(text(), 'IDR')])[1]"));
    private static final Locator PAYMENT_SUCCESSFUL_DIV_HEADING = new Locator("PAYMENT_SUCCESSFUL_DIV_HEADING", new By.ByXPath("//h1[text()='Pembayaranmu berhasil!' or text()='Payment successful!']"));
    private static final Locator LPG_LABEL_PAYMENT_PAGE = new Locator("LPG_LABEL_PAYMENT_PAGE", new By.ByXPath("//p[contains(@class, 'Text_text') and contains(@class, 'Text_variant_invert')]"));
    private static final Locator INSTALLMENT = new Locator("INSTALLMENT", new By.ByXPath("(//div[contains(@class, 'CardInstallmentBottomSheet_installment_title')])[2]"));
    private static final Locator PAYMENT_METHOD_GROUP_CLOSE_BUTTON = new Locator("PAYMENT_METHOD_GROUP_CLOSE_BUTTON", By.xpath("(//button[contains(@class, 'close')])[2]"));

    private static Locator PAYMENT_METHOD_GROUP(String paymentGroup) {
        String[] labels = splitPaymentGroupLabels(paymentGroup);
        String xpath = labels.length == 2
                ? String.format("//p[contains(@class,'Group') and (contains(text(),'%s') or contains(text(),'%s'))]", labels[0], labels[1])
                : String.format("//p[contains(@class,'Group') and contains(text(),'%s')]", labels[0]);
        return new Locator("INSTANT_PAYMENT_METHOD_GROUP", By.xpath(xpath));
    }
    private static final Locator PROMO_CLOSE_DRAWER = new Locator("PROMO_CLOSE_DRAWER", new By.ByXPath("//button[contains(@class,'BottomSheet_close')]"));
    private static final Locator LOGIN_NUDGE_TEXT = new Locator("LOGIN_NUDGE_TEXT", new By.ByXPath("//div[contains(@class,'LoginBox_gradient')]//p"));
    private static Locator PAYMENT_METHOD_GROUP_BOTTOM_SHEET(String paymentGroup) {
        String[] labels = splitPaymentGroupLabels(paymentGroup);
        String xpath = labels.length == 2
                ? String.format("//div[contains(@class,'BottomSheet')]//h2[text()='%s' or text()='%s']", labels[0], labels[1])
                : String.format("//div[contains(@class,'BottomSheet')]//h2[text()='%s']", labels[0]);
        return new Locator("PAYMENT_METHOD_GROUP_BOTTOM_SHEET", By.xpath(xpath));
    }
    private static final Locator SELECT_PAYMENT_METHOD(String paymentMethod){
        if(paymentMethod.equalsIgnoreCase("Add Card")){
            return new Locator("SELECT_PAYMENT_METHOD", By.xpath("//div[contains(@class, 'SavedCards_cc_wrapper')]//p[normalize-space()='Add Card']"));
        } else {
            return new Locator("SELECT_PAYMENT_METHOD", By.xpath(String.format("//p[normalize-space()='%s']/ancestor::li",paymentMethod)));
        }

    }

    private static final Locator SELECT_PAYMENT_METHOD_CC_TOKENIZE(String paymentMethod){
        return new Locator("SELECT_PAYMENT_METHOD_CC_TOKENIZE", By.xpath(String.format("//span[contains(text(), '%s')]",paymentMethod)));
    }

    @Override
    public boolean isLPGLabelPresentOnPaymentPage() {
        return driverTools.waitForElementsToAppear(LPG_LABEL_PAYMENT_PAGE).isPresent();
    }

    public void goToPaymentMethodsGroup(String paymentGroup){
        driverTools.waitForElementToAppear(PAYMENT_DRAWER_HEADING);
        driverTools.waitForElementToAppear(PAYMENT_METHOD_GROUP(paymentGroup));
        driverTools.scrollIntoWebView(PAYMENT_METHOD_GROUP(paymentGroup));
        driverTools.waitForElementToAppear(PAYMENT_METHOD_GROUP(paymentGroup));
        driverTools.clickWithJavascriptExecutor(PAYMENT_METHOD_GROUP(paymentGroup));
    }

    public void clickPromoSuggestion() {
        driverTools.waitForElementToAppear(SEE_PROMOS_VOUCHERS, 3);
        driverTools.waitForElementToBeClickable(SEE_PROMOS_VOUCHERS, 10);
        driverTools.scrollIntoWebView(SEE_PROMOS_VOUCHERS);
        driverTools.click(SEE_PROMOS_VOUCHERS);
        driverTools.waitForElementToPresence(getIndexEligiblePromo(1));
    }

    @Override
    public void maximize() {
        // Not needed for MWeb version
    }

    public void clickClosePromoSuggestion() {
        driverTools.waitForElementToAppear(PROMO_CLOSE_DRAWER).ifPresent(driverTools::click);
    }

    public void selectPaymentMethod(String paymentMethod){
        WebElement element = driverTools.waitForElementToAppear(SELECT_PAYMENT_METHOD(paymentMethod)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(element);
        isPaymentPageLoaded();
    }

    @Override
    public String getTextOnLoginNudge(){
        WebElement loginNudgeElement = driverTools.waitForElementToAppear(LOGIN_NUDGE_TEXT).orElseThrow();
        String loginNudgeElementText = driverTools.getText(loginNudgeElement);
        logger.log("loginNudgeElementText "+loginNudgeElementText);
        return loginNudgeElementText;
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

    @Override
    public boolean isPaymentSuccessfulVisible(){
        driverTools.waitForElementToAppear(PAYMENT_SUCCESSFUL_DIV_HEADING, 60);
        boolean visible = driverTools.isElementDisplayed(PAYMENT_SUCCESSFUL_DIV_HEADING);
        if (visible) {
            HotelPendingRetrySummaryNotifier.afterWebPaymentSuccess(session, apiTools, logger);
        }
        return visible;
    }

    @Override
    public void selectPaymentMethodCCTokenize(String paymentMethod){
        WebElement element = driverTools.waitForElementToAppear(SELECT_PAYMENT_METHOD_CC_TOKENIZE(paymentMethod)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(element);
        isPaymentPageLoaded();
    }

   @Override
    public void changeTenure() {
        driverTools.waitForElementToAppear(INSTALLMENT_TENURE);
        driverTools.clickWithJavascriptExecutor(INSTALLMENT_TENURE);
        driverTools.waitForElementToBeClickable(TENURE_3X);
        driverTools.clickWithJavascriptExecutor(TENURE_3X);
        driverTools.waitForElementToAppear(CLICK_SAVE);
        driverTools.clickWithJavascriptExecutor(CLICK_SAVE);
    }

    @Override
    public void changeTenureToAnother() {
        driverTools.waitForElementToAppear(INSTALLMENT_TENURE);
        driverTools.clickWithJavascriptExecutor(INSTALLMENT_TENURE);
        driverTools.waitForElementToBeClickable(TENURE_FULL_PAYMENT);
        driverTools.clickWithJavascriptExecutor(TENURE_FULL_PAYMENT);
        driverTools.waitForElementToAppear(CLICK_SAVE);
        driverTools.clickWithJavascriptExecutor(CLICK_SAVE);
    }

    public String getInitialPrice() {
        driverTools.waitForElementToAppear(OPEN_SEAT_PRICE).ifPresent(driverTools::click);
        WebElement initialPrice = driverTools.waitForElementToAppear(INITIAL_ORDER_PRICE).orElseThrow();
        String initialPriceText = driverTools.getText(initialPrice);
        return initialPriceText.replaceAll("[^0-9]", "");
    }

    @Override
    public void selectInstallment(){
        driverTools.waitForElementToAppear(INSTALLMENT_CONTAINER);
        driverTools.clickWithJavascriptExecutor(INSTALLMENT_CONTAINER);
        driverTools.waitForElementToAppear(INSTALLMENT);
        driverTools.clickWithJavascriptExecutor(INSTALLMENT);
    }

    public void clickOnPaymentGroup(String paymentGroup){
        logger.log("On MWeb, clicking payment group is done on goToPaymentMethodsGroup(), paymentGroup is: " + paymentGroup);
    }

    public void clickClosePaymentMethodGroup() {
        driverTools.waitForElementToAppear(PAYMENT_METHOD_GROUP_CLOSE_BUTTON).ifPresent(driverTools::click);
    }
}
