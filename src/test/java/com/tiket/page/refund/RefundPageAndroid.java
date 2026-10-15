package com.tiket.page.refund;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tiket.core.CoreFunctions.getIntFromString;
import static org.hamcrest.CoreMatchers.is;

public class RefundPageAndroid<T extends WebDriver> extends com.tiket.page.refund.RefundPage<T> {

    public RefundPageAndroid(Session<T> session) {
        super(session);
    }

    private static final Locator REASON_DROPDOWN = new Locator("REASON_DROPDOWN", AppiumBy.className("android.widget.EditText"));
    private static final Locator CHOSEN_REASON = new Locator("CHOSEN_REASON", AppiumBy.xpath("//android.widget.CheckBox[@checked='true']"));
    private static final Locator SAVE_REASON = new Locator("SAVE_REASON", AppiumBy.xpath("(//android.widget.Button)[last()]"));
    private static final Locator REFUND_ESTIMATION = new Locator("REFUND_ESTIMATION", AppiumBy.xpath("//*[contains(@text,'Sebelum') or contains(@text,'Before')]/following-sibling::*[contains(@text,'IDR')]"));
    private static final Locator HOTEL_NAME_ORDER = new Locator("HOTEL_NAME_ORDER", AppiumBy.xpath("//*[@class='b2 mobile high-emphasis bold hover tix-font v4']"));
    private static final Locator HOTEL_REFUND_STATUS_PROGRESS = new Locator("HOTEL_REFUND_STATUS_PROGRESS", AppiumBy.xpath("//*[contains(@class,'info-title') and (contains(text(),'Verified by hotel') or contains(text(), 'Diverifikasi hotel') or contains(@text,'Refund has completed') or contains(@text,'Refund selesai'))]"));
    private static final Locator HOTEL_REFUND_ESTIMATED_DATE_PROGRESS = new Locator("HOTEL_REFUND_ESTIMATED_DATE_PROGRESS", AppiumBy.xpath("//div[@class='content border-green']//div[@class='content-info']//div[@class='info-date']//div"));
    private static final Locator HOTEL_REFUND_ESTIMATED_DATE_DETAIL = new Locator("HOTEL_REFUND_ESTIMATED_DATE_DETAIL", AppiumBy.xpath("//*[@class='refund-estimation-date mobile high-emphasis bold hover tix-font v4']"));
    private static final Locator HOTEL_REFUND_ESTIMATED_PAYMENT_DETAIL = new Locator("HOTEL_REFUND_ESTIMATED_PAYMENT_DETAIL", AppiumBy.xpath("//*[contains(@class,'refund-amount')]"));
    private static final Locator APP_SCREEN = new Locator("APP_SCREEN", AppiumBy.xpath("//*[contains(@resource-id,'app')]"));
    private static final Locator REFUND_BTN_WEBVIEW = new Locator("PROCESS_REFUND_BTN_WEBVIEW", AppiumBy.xpath("//*[@class='next-button-container']//*[@type='button']"));
    private static final Locator ORDER_ID_WEBVIEW = new Locator("ORDER_ID_WEBVIEW", AppiumBy.xpath("//*[@class='refund-header-title']//following-sibling::div"));
    private static final Locator REFUND_AMOUNT = new Locator("REFUND_AMOUNT", AppiumBy.xpath("//*[contains(@class,'refund-amount')]"));
    private static final Locator SEE_ORDER_LIST = new Locator("SEE_ORDER_LIST", AppiumBy.xpath("//*[contains(@text, 'See Order List') or contains(@text, 'Lihat Daftar Pesanan')]"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("//android.widget.Button[contains(@text,'Continue') or contains(@text,'Lanjutkan')]"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BTN", AppiumBy.xpath("//*[@data-testid='icon-back']"));
    private static final Locator TOTAL_REFUND = new Locator("TOTAL_REFUND", AppiumBy.xpath("//*[contains(@class, 'styles_total')]/span"));
    private static final Locator TOTAL_PRICE_CHEV_BTN = new Locator("TOTAL_PRICE_CHEV_BTN", AppiumBy.xpath("//*[@data-testid='icon-chevron']"));
    private static final Locator PREVIOUS_ORDER_PRICE = new Locator("PREV_ORDER_PRICE", AppiumBy.xpath("(//*[contains(@class, 'styles_amount_label')]//following-sibling::p)[2]"));
    private static final Locator CONTINUE_RESCHEDULE_POPUP_BTN = new Locator("CONTINUE_RESCHEDULE_BUTTON", AppiumBy.xpath("//*[contains(@class, 'Figure_secondary') or contains(text(), 'Continue the reschedule')]"));
    private static final Locator DEDUCTED_FEE_PRICES = new Locator("DEDUCTED_FEE_PRICES", AppiumBy.xpath("//*[contains(@class, 'styles_amount_label')]//following-sibling::p"));
    private static final Locator CLOSE_POPUP_BTMSHEET_BUTTON = new Locator("CLOSE_POPUP_BTMSHEET_BUTTON", AppiumBy.xpath("//*[contains(@class, 'BottomSheet_close_button')]"));
    private static final Locator TERM_AND_CONDITION_CHECKBOX_NATIVE = new Locator("TERM_AND_CONDITION_CHECKBOX_NATIVE", AppiumBy.xpath("//*[contains(@class, 'Check_custom_checkbox__')]"));
    private static final Locator OPEN_TNC_POPUP = new Locator("OPEN_TNC_POPUP", AppiumBy.xpath("//*[@data-testid='tnc-text']"));
    private static final Locator SUBMIT_REFUND_REQUEST = new Locator("CONTINUE_SUBMIT_REFUND", AppiumBy.xpath("//*[@data-testid='continue-submit-refund']"));
    private static final Locator VIEW_REFUND_DETAIL = new Locator("VIEW_REFUND_DETAIL", AppiumBy.xpath("//button[.//span[normalize-space()='View Refund Detail']]"));
    private static final Locator REFUND_SENT_TO_TIKET_CHECK = new Locator("REFUND_SENT_TO_TIKET_CHECK", AppiumBy.xpath("//li[.//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sent to tiket.com')]]//*[@data-testid='check-icon']"));
    private static final Locator REFUND_REVIEWED_BY_TIKET_CHECK = new Locator("REFUND_REVIEWED_BY_TIKET_CHECK", AppiumBy.xpath("//li[.//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'reviewed by tiket.com')]]//*[@data-testid='check-icon']"));
    private static final Locator REFUND_VERIFIED_BY_TIKET_CHECK = new Locator("REFUND_VERIFIED_BY_TIKET_CHECK", AppiumBy.xpath("//li[.//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'verified by tiket.com')]]//*[@data-testid='check-icon']"));

    @Override
    public String chooseRefundReason() {
        WebElement reasonDropdown = driverTools.waitForElementToAppear(REASON_DROPDOWN,30).orElseThrow();
        driverTools.click(reasonDropdown);
        String reason = chooseRandomReason(CoreFunctions.generateRandomInt(1,5));
        driverTools.click(SAVE_REASON);
        logger.log("Reason: " + reason);
        return reason;
    }

    @Override
    public String choosePersonalReason() {
        return chooseRefundReasonWebView("Personal Reason");
    }

    private String chooseRandomReason(int index) {
        driverTools.click(getReasonLocator(index));
        return driverTools.getValueAttribute(CHOSEN_REASON,"name");
    }

    private Locator getReasonLocator(int index) {
        return new Locator("REFUND_REASON", AppiumBy.xpath("(//android.widget.CheckBox)["+index+"]"));
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
    public String getOrderIdRefundPage() {
        driverTools.waitForElementToAppear(APP_SCREEN,20).orElseThrow();
        driverTools.switchToWebView();
        WebElement orderId = driverTools.waitForElementToAppear(ORDER_ID_WEBVIEW,20).orElseThrow();
        String orderIDText = driverTools.getText(orderId);
        driverTools.switchToNative();
        return orderIDText;
    }

    @Override
    public boolean checkAllOrderId(String orderId1, String orderId2, String orderId3) {
        logger.log("OrderId detail page " + orderId1);
        logger.log("OrderId refund page1 " + orderId2);
        logger.log("OrderId refund page2 " + orderId3);
        boolean isOrderIdSame = orderId1.equals(orderId2) && orderId1.equals(orderId3);
        logger.log("is orderId same: " + isOrderIdSame);
        return isOrderIdSame;
    }

    @Override
    public void seeOrderList() {
        WebElement seeOrderList = driverTools.waitForElementToPresence(SEE_ORDER_LIST, 30).orElseThrow();
        driverTools.click(seeOrderList);
    }

    @Override
    public boolean isFullRefund(String price) {
        driverTools.switchToWebView();
        int refundAmount = CoreFunctions.getIntFromString(driverTools.getText(REFUND_AMOUNT).orElseThrow()).orElseThrow();
        logger.log("Order Price: " + price);
        logger.log("Refund Price: " + refundAmount);
        driverTools.switchToNative();
        return refundAmount == Integer.parseInt(price);
    }

    @Override
    public Optional<Integer> getTotalEstimatedRefund() {
        driverTools.switchToWebView();
        WebElement refundAmount =  driverTools.swipeToElement(REFUND_AMOUNT, Swipe.UP, 5).orElseThrow();
        Optional<Integer> refundAmountLabel = getIntFromString(driverTools.getText(refundAmount));
        logger.log("refundAmountLabel-> " + refundAmountLabel);
        driverTools.switchToNative();
        return refundAmountLabel;
    }

    @Override
    public void clickContinueButton() {
        driverTools.swipeToElement(CONTINUE_BUTTON, Swipe.UP, 5).orElseThrow();
        driverTools.click(CONTINUE_BUTTON);
    }

    @Override
    public boolean checkRefundReviewPage() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.waitForElementToAppear(BACK_BUTTON,15);
        Assertion.assertThat("Back button should be displayed", driverTools.isElementDisplayed(BACK_BUTTON), is(true));
        driverTools.click(BACK_BUTTON);
        Assertion.assertThat("CANCEL/CONTINUE RESCHEDULE order popup is displayed ", driverTools.isElementDisplayed(CONTINUE_RESCHEDULE_POPUP_BTN), is(true));
        driverTools.click(CONTINUE_RESCHEDULE_POPUP_BTN);
        driverTools.swipeToElement(TOTAL_REFUND,Swipe.UP,5);
        Assertion.assertThat("Total refund label should be displayed", driverTools.isElementDisplayed(TOTAL_REFUND), is(true));
        driverTools.click(TOTAL_PRICE_CHEV_BTN);
        String totalRefundString = String.valueOf(driverTools.getText(TOTAL_REFUND));
        double totalRefund = CoreFunctions.convertPrice(totalRefundString);
        Assertion.assertThat("verify price calculation is correct",calculatePriceBreakdownRefundDetails(totalRefund), is(true));
        Assertion.assertThat("Close Total refund label should be displayed",driverTools.isElementDisplayed(CLOSE_POPUP_BTMSHEET_BUTTON), is(true));
        driverTools.click(CLOSE_POPUP_BTMSHEET_BUTTON);
        Assertion.assertThat("Terms and conditions checkbox should be displayed", driverTools.isElementDisplayed(TERM_AND_CONDITION_CHECKBOX_NATIVE), is(true));
        driverTools.click(OPEN_TNC_POPUP);
        Assertion.assertThat("Close Terms and conditions checkbox should be displayed",driverTools.isElementDisplayed(CLOSE_POPUP_BTMSHEET_BUTTON), is(true));
        driverTools.click(CLOSE_POPUP_BTMSHEET_BUTTON);
        driverTools.swipeToElement(SUBMIT_REFUND_REQUEST,Swipe.UP,2);
        Assertion.assertThat("Submit refund request button should be displayed", driverTools.isElementDisplayed(SUBMIT_REFUND_REQUEST), is(true));
        driverTools.switchToNative();
        return true;
    }

    /**
     * Calculates the total refund by summing the previous order price and all deducted fees,
     * and compares it with the expected total refund value, excluding the first and second deducted fee prices.
     *
     * @param totalRefund The expected total refund value to be compared with the calculated refund.
     * @return true if the calculated refund is approximately equal to the expected total refund (within a tolerance of 0.01),
     *         false otherwise.
     * @throws RuntimeException if there is a significant mismatch between the calculated refund and the expected total refund.
     */
    public boolean calculatePriceBreakdownRefundDetails(double totalRefund) {
        double calculatedTotal = 0.0;
        ArrayList<Double> arrayPrice = new ArrayList<>();

        String prevPriceString = String.valueOf(driverTools.getText(PREVIOUS_ORDER_PRICE));
        logger.log("Previous Price (via getText): " + prevPriceString);

        double prevPrice = CoreFunctions.convertPrice(prevPriceString);

        logger.log("Previous Order Price: " + prevPrice);
        logger.log("Expected Total Refund: " + totalRefund);

        List<WebElement> priceElements = driverTools.elements(DEDUCTED_FEE_PRICES).orElseThrow();
        logger.log("Number of deducted fees found: " + priceElements.size());

        // Iterate over all deducted fee elements, but exclude the first two
        for (int i = 0; i < priceElements.size(); i++) {
            if (i == 0 || i == 1) {
                continue; // Skip first and second prices
            }

            WebElement element = priceElements.get(i);
            String text = driverTools.getText(element);
            double price = CoreFunctions.convertPrice(text);
            logger.log("Deducted Fee Price #" + (i + 1) + ": " + price);
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
        driverTools.switchToWebViewUrl("refund");
        driverTools.waitForElementToAppear(VIEW_REFUND_DETAIL, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(VIEW_REFUND_DETAIL);
        driverTools.switchToNative();
    }
    @Override
    public boolean isRefundSentToTiketCheckDisplayed() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refund");

        boolean displayed = driverTools
                .waitForElementToAppear(REFUND_SENT_TO_TIKET_CHECK, 30)
                .isPresent();

        driverTools.switchToNative();
        return displayed;
    }
    @Override
    public boolean isRefundReviewedByTiketCheckDisplayed() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refund");

        boolean displayed = driverTools
                .waitForElementToAppear(REFUND_REVIEWED_BY_TIKET_CHECK, 30)
                .isPresent();

        driverTools.switchToNative();
        return displayed;
    }
    @Override
    public boolean isRefundVerifiedByTiketCheckDisplayed() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("refund");

        boolean displayed = driverTools
                .waitForElementToAppear(REFUND_VERIFIED_BY_TIKET_CHECK, 30)
                .isPresent();

        driverTools.switchToNative();
        return displayed;
    }
}
