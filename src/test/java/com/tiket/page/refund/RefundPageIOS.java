package com.tiket.page.refund;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tiket.core.CoreFunctions.getIntFromString;
import static org.hamcrest.CoreMatchers.is;

public class RefundPageIOS<T extends WebDriver> extends com.tiket.page.refund.RefundPage<T> {

    public RefundPageIOS(Session<T> session) {
        super(session);
    }
    private static final Locator REASON_DROPDOWN = new Locator("REASON_DROPDOWN", AppiumBy.xpath("//*[@name='Select refund reason' and @type='XCUIElementTypeStaticText']"));
    private static final Locator PERSONAL_REASON = new Locator("PERSONAL_REASON_IOS", AppiumBy.xpath("//*[@name='Personal Reason' and @label='Personal Reason' and @type='XCUIElementTypeStaticText']"));
    private static final Locator CONTINUE_REFUND = new Locator("CONTINUE_REFUND_IOS", AppiumBy.xpath("//*[@name='Continue' and @label='Continue' and @type='XCUIElementTypeButton']"));
    private static final Locator CHOSEN_REASON = new Locator("CHOSEN_REASON", AppiumBy.xpath("//XCUIElementTypeSwitch[@value='1']"));
    private static final Locator SAVE_REASON = new Locator("SAVE_REASON", AppiumBy.xpath("(//XCUIElementTypeButton[contains(@name, \"Refund\")])[last()]"));
    private static final Locator REFUND_ESTIMATION = new Locator("REFUND_ESTIMATION", AppiumBy.xpath("//*[contains(@label,'Sebelum') or contains(@label,'Before')]/following-sibling::*[contains(@label,'IDR')]"));
    private static final Locator REFUND_BTN_NATIVE = new Locator("REFUND_BTN_NATIVE", AppiumBy.iOSClassChain("**/*[`label == 'Reschedule'`]/*[`type == 'XCUIElementTypeOther'`]/*[`type == 'XCUIElementTypeButton' AND label CONTAINS 'Submit' OR label CONTAINS 'Ajukan'`]"));
    private static final Locator REFUND_AMOUNT = new Locator("REFUND_AMOUNT", AppiumBy.xpath("//*[contains(@class,'refund-amount')]"));
    private static final Locator REFUND_AMOUNT_NATIVE = new Locator("REFUND_AMOUNT_NATIVE", AppiumBy.xpath("//*[contains(@label,'Estimated total payment') or contains(@label,'Estimasi total pembayaran')]//following::XCUIElementTypeStaticText[1]"));
    private static final Locator ORDER_ID_WEBVIEW = new Locator("ORDER_ID_WEBVIEW", AppiumBy.xpath("//*[@class='refund-header-title']//following-sibling::div//child::span"));
    private static final Locator TERM_AND_CONDITION_CHECKBOX_NATIVE = new Locator("TERM_AND_CONDITION_CHECKBOX_NATIVE", By.xpath("//XCUIElementTypeSwitch[@value='0']"));
    private static final Locator SEE_ORDER_LIST = new Locator("SEE_ORDER_LIST", AppiumBy.iOSClassChain("**/*[`type == 'XCUIElementTypeButton' AND name CONTAINS 'See Order List' OR name CONTAINS 'Lihat Daftar Pesanan'`]"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@label='Lanjutkan' or @label='Continue']"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"Reschedule\"])[1]/preceding-sibling::XCUIElementTypeImage"));
    private static final Locator TOTAL_REFUNND= new Locator("TOTAL_REFUNND", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"Total\"]/following-sibling::XCUIElementTypeStaticText)[1]"));
    private static final Locator SUBMIT_REFUND_REQUEST= new Locator("SUBMIT_REFUND_REQUEST", AppiumBy.xpath("//XCUIElementTypeButton[@label=\"Submit a Refund Request\" or @label=\"Ajukan Permohonan Refund\"]"));
    private static final Locator CONTINUE_RESCHEDULE_POPUP_BTN = new Locator("CONTINUE_RESCHEDULE_POPUP_BTN", AppiumBy.xpath("//XCUIElementTypeButton[@name='Continue the reschedule' or @name='Lanjutkan reschedule']"));
    private static final Locator TOTAL_PRICE_CHEV_BTN = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"Total\"]/following-sibling:: XCUIElementTypeImage)[1]"));
    private static final Locator CLOSE_POPUP_BTMSHEET_BUTTON = new Locator("CLOSE_POPUP_BTMSHEET_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='Close' or @name='Tutup']"));
    private static final Locator OPEN_TNC_POPUP = new Locator("OPEN_TNC_POPUP", By.xpath("//XCUIElementTypeStaticText[@name='Syarat dan Ketentuan' or @name='Terms and Conditions']"));
    private static final Locator PREVIOUS_ORDER_PRICE = new Locator("PREVIOUS_ORDER_PRICE", By.xpath("//XCUIElementTypeStaticText[@label='Previous Order Price' or @label='Pembayaran Sebelumnya']/parent::*/following-sibling::XCUIElementTypeOther[1]/XCUIElementTypeStaticText"));
    private static final Locator DEDUCTED_FEE_PRICES = new Locator("DEDUCT_FEE", By.xpath("//XCUIElementTypeStaticText[contains(@label, '-IDR')]"));
    private static final Locator VIEW_REFUND_DETAIL = new Locator("VIEW_REFUND_DETAIL", AppiumBy.xpath("//*[@name='View Refund Detail' and @label='View Refund Detail' and @type='XCUIElementTypeButton']"));
    private static final Locator REFUND_SENT_TO_TIKET_CHECK = new Locator("REFUND_SENT_TO_TIKET_CHECK", AppiumBy.xpath("//*[@name='Sent to tiket.com' and @type='XCUIElementTypeStaticText']"));
    private static final Locator REFUND_REVIEWED_BY_TIKET_CHECK = new Locator("REFUND_REVIEWED_BY_TIKET_CHECK", AppiumBy.xpath("//*[@name='Reviewed by tiket.com' and @type='XCUIElementTypeStaticText']"));
    private static final Locator REFUND_VERIFIED_BY_TIKET_CHECK = new Locator("REFUND_VERIFIED_BY_TIKET_CHECK", AppiumBy.xpath("//*[@name='Verified by tiket.com' and @type='XCUIElementTypeStaticText']"));

    @Override
    public String chooseRefundReason() {
        WebElement reasonDropdown = driverTools.waitForElementToPresence(REASON_DROPDOWN,20).orElseThrow();
        driverTools.click(reasonDropdown);
        String reason = chooseRandomReason(CoreFunctions.generateRandomInt(1,5));
        driverTools.click(SAVE_REASON);
        logger.log("Reason: " + reason);
        return reason;
    }
    @Override
    public String choosePersonalReason() {
        driverTools.waitForElementToPresence(REASON_DROPDOWN, 10).orElseThrow();
        driverTools.click(REASON_DROPDOWN);

        driverTools.waitForElementToPresence(PERSONAL_REASON, 10).orElseThrow();
        driverTools.click(PERSONAL_REASON);

        driverTools.swipeToElement(CONTINUE_REFUND, Swipe.UP, 5);
        driverTools.waitForElementToPresence(CONTINUE_REFUND, 10).orElseThrow();
        driverTools.clickElementByCoordinate(CONTINUE_REFUND);

        return "Personal Reason";
    }

    private String chooseRandomReason(int index) {
        driverTools.click(getReasonLocator(index));
        return driverTools.getValueAttribute(CHOSEN_REASON,"name");
    }

    private Locator getReasonLocator(int index) {
        return new Locator("REFUND_REASON", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`value == \"0\"`][" + index + "]"));
    }

    @Override
    public boolean isCorrectReasonShowing(String reason) {
        return driverTools.getText(REASON_DROPDOWN).orElseThrow().equals(reason);
    }

    @Override
    public boolean isEstimationShowing() {
        return driverTools.waitForElementToAppear(REFUND_ESTIMATION).isPresent();
    }

    @Override
    public boolean isFullRefund(String price){
        driverTools.switchToWebView();
        int refundAmount = getIntFromString(driverTools.getText(REFUND_AMOUNT).orElseThrow()).orElseThrow();
        logger.log("Order/Cashback Price: " + price);
        logger.log("Refund Price: " + refundAmount);
        driverTools.switchToNative();
        return refundAmount == Integer.parseInt(price);
    }

    @Override
    public Optional<Integer> getTotalEstimatedRefund() {
        WebElement refundAmount =  driverTools.swipeToElement(REFUND_AMOUNT_NATIVE, Swipe.UP, 5).orElseThrow();
        Optional<Integer> refundAmountLabel = getIntFromString(driverTools.getValueAttribute(refundAmount, "label"));
        logger.log("refundAmountLabel-> " + refundAmountLabel);
        return refundAmountLabel;
    }

    @Override
    public String getOrderIdRefundPage(){
        driverTools.switchToWebView();
        WebElement orderId = driverTools.waitForElementToAppear(ORDER_ID_WEBVIEW,20).orElseThrow();
        String orderIDText = driverTools.getText(orderId);
        driverTools.switchToNative();

        return orderIDText;
    }

    @Override
    public void submitRefundReschedule(){
        WebElement tncCheckbox = driverTools.swipeToElement(TERM_AND_CONDITION_CHECKBOX_NATIVE, Swipe.UP, 5).orElseThrow();
        driverTools.click(tncCheckbox);
        WebElement submitRefundBtn = driverTools.swipeToElement(REFUND_BTN_NATIVE, Swipe.UP, 3).orElseThrow();
        Optional<Integer> totalEstimatedRefund = getTotalEstimatedRefund();
        driverTools.click(submitRefundBtn);
    }

    @Override
    public boolean checkAllOrderId(String orderId1, String orderId2, String orderId3){
        logger.log("OrderId detail page " + orderId1);
        logger.log("OrderId refund page1 " + orderId2);
        logger.log("OrderId refund page2 " + orderId3);
        boolean isOrderIdSame = orderId1.equals(orderId2) && orderId1.equals(orderId3);
        logger.log("is orderId same: " + isOrderIdSame);
        return isOrderIdSame;
    }

    @Override
    public void seeOrderList() {
        WebElement seeOrderList = driverTools.waitForElementToPresence(SEE_ORDER_LIST, 20).orElseThrow();
        driverTools.click(seeOrderList);
    }

    @Override
    public void clickContinueButton() {
        return;
    }

    @Override
    public boolean checkRefundReviewPage() {
        Assertion.assertThat("Back button should be displayed", driverTools.isElementDisplayed(BACK_BUTTON), is(true));
        driverTools.click(BACK_BUTTON);
        Assertion.assertThat("CANCEL/CONTINUE RESCHEDULE order popup is displayed ", driverTools.isElementDisplayed(CONTINUE_RESCHEDULE_POPUP_BTN), is(true));
        driverTools.click(CONTINUE_RESCHEDULE_POPUP_BTN);
        driverTools.swipeToElement(TOTAL_REFUNND,Swipe.UP,5);
        Assertion.assertThat("Total refund label should be displayed", driverTools.isElementDisplayed(TOTAL_REFUNND), is(true));
        driverTools.click(TOTAL_PRICE_CHEV_BTN);
        String totalRefundString = driverTools.getValueAttribute(TOTAL_REFUNND, "value");
        double totalRefund = CoreFunctions.convertPrice(totalRefundString);
        Assertion.assertThat("verify price calculation is correct",calculatePriceBreakdownRefundDetails(totalRefund), is(true));
        Assertion.assertThat("Close Total refund label should be displayed",driverTools.isElementDisplayed(CLOSE_POPUP_BTMSHEET_BUTTON), is(true));
        driverTools.click(CLOSE_POPUP_BTMSHEET_BUTTON);
        Assertion.assertThat("Terms and conditions checkbox should be displayed", driverTools.isElementDisplayed(TERM_AND_CONDITION_CHECKBOX_NATIVE), is(true));
        driverTools.click(OPEN_TNC_POPUP);
        Assertion.assertThat("Close Terms and conditions checkbox should be displayed",driverTools.isElementDisplayed(TERM_AND_CONDITION_CHECKBOX_NATIVE), is(true));
        driverTools.click(TERM_AND_CONDITION_CHECKBOX_NATIVE);
        driverTools.swipeToElement(SUBMIT_REFUND_REQUEST,Swipe.UP,2);
        Assertion.assertThat("Submit refund request button should be displayed", driverTools.isElementDisplayed(SUBMIT_REFUND_REQUEST), is(true));
        return true;
    }

    /**
     * Calculates the total refund by summing the previous order price and all deducted fees,
     * and compares it with the expected total refund value.
     *
     * @param totalRefund The expected total refund value to be compared with the calculated refund.
     * @return true if the calculated refund is approximately equal to the expected total refund (within a tolerance of 0.01),
     *         false otherwise.
     * @throws RuntimeException if there is a significant mismatch between the calculated refund and the expected total refund.
     */
    public boolean calculatePriceBreakdownRefundDetails(double totalRefund) {
        double calculatedTotal = 0.0;
        ArrayList<Double> arrayPrice = new ArrayList<>();

        String prevPriceString = String.valueOf(driverTools.getValueAttribute(PREVIOUS_ORDER_PRICE, "value"));
        logger.log("Previous Price (via getText): " + prevPriceString);

        double prevPrice = CoreFunctions.convertPrice(prevPriceString);

        logger.log("Previous Order Price: " + prevPrice);
        logger.log("Expected Total Refund: " + totalRefund);

        List<WebElement> priceElements = driverTools.elements(DEDUCTED_FEE_PRICES).orElseThrow();
        logger.log("Number of deducted fees found: " + priceElements.size());
        for (WebElement element : priceElements) {
            String text = driverTools.getText(element);
            double price = CoreFunctions.convertPrice(text);
            logger.log("Deducted Fee Price: " + price);
            arrayPrice.add(price);
        }
        for (double price : arrayPrice) {
            calculatedTotal += price;
        }
        calculatedTotal += prevPrice;
        logger.log("Calculated Total Refund: " + calculatedTotal);

        if (Math.abs(calculatedTotal - totalRefund) > 0.01) { // Allow minor rounding differences
            throw new RuntimeException("Mismatch in calculated refund: Expected " + totalRefund + ", but got " + calculatedTotal);
        }
        Assertion.assertThat("Total refund price breakdown is correct", calculatedTotal, is(totalRefund));
        return true;
    }
    @Override
    public void getRefundDetails() {
        driverTools.switchToNative();
        driverTools.waitForElementToPresence(VIEW_REFUND_DETAIL, 10).orElseThrow();
        driverTools.click(VIEW_REFUND_DETAIL);
    }
    @Override
    public boolean isRefundSentToTiketCheckDisplayed() {
        driverTools.switchToNative();
        return driverTools
                .waitForElementToPresence(REFUND_SENT_TO_TIKET_CHECK, 30)
                .isPresent();
    }

    @Override
    public boolean isRefundReviewedByTiketCheckDisplayed() {
        driverTools.switchToNative();
        return driverTools
                .waitForElementToPresence(REFUND_REVIEWED_BY_TIKET_CHECK, 30)
                .isPresent();
    }

    @Override
    public boolean isRefundVerifiedByTiketCheckDisplayed() {
        driverTools.switchToNative();
        return driverTools
                .waitForElementToPresence(REFUND_VERIFIED_BY_TIKET_CHECK, 30)
                .isPresent();
    }
}