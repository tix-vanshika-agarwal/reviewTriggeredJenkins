package com.tiket.web.page.platform.sphinx.refund;

import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;


public abstract class RefundDetailWeb<T extends WebDriver> extends BasePage<T> {
    private static final Locator REFUND_BREAKDOWN_ICON = new Locator("REFUND_BREAKDOWN_ICON", By.xpath("//*[contains(@class,'refund_total_wrapper')]//*[@data-testid='icon-chevron']"));
    private static final Locator REFUND_BREAKDOWN_PLUS = new Locator("REFUND_BREAKDOWN_PLUS", By.xpath("//*[contains(@class,'amount_details_item')]/*[contains(@class,'variant_high')]"));
    private static final Locator REFUND_BREAKDOWN_MINUS = new Locator("REFUND_BREAKDOWN_MINUS", By.xpath("//*[contains(@class,'amount_details_item')]/*[contains(@class,'variant_alert')]"));
    private static final Locator REFUND_STATUS_HEADLINE = new Locator("REFUND_STATUS_HEADLINE", By.xpath("//*[contains(@class,'headline')]//*[contains(@class,'activity')]"));
    private static final Locator REFUND_ID = new Locator("REFUND_ID", By.xpath("//*[contains(@class,'refund_id__')]"));
    private static final Locator REFUND_ID_COPY_BUTTON = new Locator("REFUND_ID_COPY_BUTTON", By.xpath("//*[contains(@class,'refund_id__')]//*[@data-testid='icon-copy']"));
    private static final Locator SNACKBAR_CONTENT = new Locator("SNACKBAR_CONTENT", By.xpath("//*[contains(@class,'Snackbar_text')]"));
    private static final Locator REFUND_PROGRESS_TRACKER = new Locator("REFUND_PROGRESS_TRACKER", By.xpath("//ol[contains(@class,'ProgressStepVertical')]/li"));
    private static final Locator REFUND_PROGRESS_TRACKER_CIRCLE = new Locator("REFUND_PROGRESS_TRACKER_CIRCLE", By.xpath("//*[@data-testid='step-circle']"));
    private static final Locator FINAL_STATUS_SLA = new Locator("FINAL_STATUS_SLA", By.xpath("//*[contains(@class,'final_status_wrapper')]"));
    private static final Locator REFUND_METHOD_ITEM = new Locator("REFUND_METHOD_ITEM", By.xpath("//*[contains(@class,'method_item')]"));
    private static final Locator TOTAL_REFUND = new Locator("TOTAL_REFUND", By.xpath("//*[contains(@class,'refund_total_wrapper')]"));
    private static final Locator REFUND_METHOD_BREAKDOWN = new Locator("REFUND_METHOD_BREAKDOWN", By.xpath("//*[contains(@class,'refund_method_section')]//strong"));
    private static final Locator REFUND_METHOD_SINGLE_BREAKDOWN = new Locator("REFUND_METHOD_SINGLE_BREAKDOWN", By.xpath("//*[contains(@class,'total_refund')]//span"));
    private static final Locator ORDER_DETAIL = new Locator("ORDER_DETAIL", By.xpath("//*[contains(@data-testid,'order-detail')]"));
    private static final Locator ORDER_DETAIL_SPAN = new Locator("ORDER_DETAIL_SPAN", By.xpath("//*[contains(@class,'wrapper_order_detail')]"));
    private static final String STAGING_YOUR_ORDER = "https://gatotkaca.tiket.com/your-order";
    private static final String PREPROD_YOUR_ORDER = "https://preprod.tiket.com/your-order";
    private static final String PROD_YOUR_ORDER = "https://www.tiket.com/your-order";
    private static final Locator CHECKBOX_REFUND = new Locator("CHECKBOX_REFUND", By.xpath("//*[contains(@class,'Check_custom_checkbox')]"));
    private static final Locator SUBMIT_REFUND = new Locator("SUBMIT_REFUND", By.xpath("//*[(contains(@class,'Button_size_large') or @data-testid='continue-submit-refund')]//*[contains(text(),'Refund')]"));
    public static final Locator HOTEL_ORDER_TITLE = new Locator("HOTEL_ORDER_TITLE", By.xpath("//p[contains(@class, 'styles_order_detail_title__')]"));
    public static final Locator ROOM_NAME = new Locator("ROOM_NAME", By.xpath("//span[contains(@class, 'styles_ordered_room__')]"));
    public static final Locator CHECKIN_DATE = new Locator("CHECKIN_DATE", By.xpath("//div[contains(@class, 'styles_addons_detail__')]"));


    public RefundDetailWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> RefundDetailWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new RefundDetailDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new RefundDetailMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public void submitRefundCashBackFreePayment() {
        driverTools.waitForElementToAppear(CHECKBOX_REFUND);
        driverTools.clickWithJavascriptExecutor(CHECKBOX_REFUND);
        driverTools.click(SUBMIT_REFUND);
        driverTools.waitForElementToDisappear(SUBMIT_REFUND);
    }

    public String getRefundStatus() {
        WebElement status = driverTools.waitForElementToAppear(REFUND_STATUS_HEADLINE).orElseThrow();
        return driverTools.getText(status);
    }

    public int countRefundProgressTracker() {
        List<WebElement> listProgress = driverTools.waitForElementsToAppear(REFUND_PROGRESS_TRACKER).orElseThrow();
        return listProgress.size();
    }

    public int getActiveProgressCircle(String timeOutSec) {
        List<WebElement> listProgressCircle = driverTools.waitForElementsToAppear(REFUND_PROGRESS_TRACKER_CIRCLE).orElseThrow();
        boolean found = false;
        int index = 0;
        long timeOut = System.currentTimeMillis() + (Long.parseLong(timeOutSec)*1000);
        logger.log("time out at epoch: "+timeOut);
        while (System.currentTimeMillis() < timeOut) {
            index = 0;
            for (WebElement circle : listProgressCircle) {
                String circleClass = driverTools.getValueAttribute(circle, "class");
                if (circleClass.contains("circle_active")) {
                    found = true;
                    break;
                }
                index++;
            }
            logger.log("Found active circle: "+found);
            if (found) break;
            listProgressCircle = driverTools.elements(REFUND_PROGRESS_TRACKER_CIRCLE).orElseThrow();
        }
        if (!found) {
            Assertion.fail("No active circle found on refund progress tracker");
        }
        return index;
    }

    public String getRefundProgressTracker(int index) {
        List<WebElement> listProgress = driverTools.waitForElementsToAppear(REFUND_PROGRESS_TRACKER).orElseThrow();
        WebElement progress = listProgress.get(index);
        return driverTools.getText(progress);
    }

    public List<String> getRefundMethod() {
        List<WebElement> listRefundMethod = driverTools.waitForElementsToAppear(REFUND_METHOD_ITEM).orElseThrow();
        return listRefundMethod.stream().map(driverTools::getText).toList();
    }

    public String getRefundId() {
        WebElement refundId = driverTools.waitForElementToAppear(REFUND_ID).orElseThrow();
        return driverTools.getText(refundId);
    }

    public String copyRefundId() {
        driverTools.click(REFUND_ID_COPY_BUTTON);
        WebElement snackbar = driverTools.waitForElementToAppear(SNACKBAR_CONTENT).orElseThrow();
        return driverTools.getText(snackbar);
    }

    public String getTotalRefund() {
        WebElement totalRefund = driverTools.waitForElementToAppear(TOTAL_REFUND).orElseThrow();
        return driverTools.getText(totalRefund);
    }

    public void openRefundBreakdown(){
        driverTools.click(REFUND_BREAKDOWN_ICON);
    };

    public abstract void closeModal();

    public long getTotalRefundAmountDetails(String currency) {
        List<WebElement> breakdownPlus = driverTools.waitForElementsToAppear(REFUND_BREAKDOWN_PLUS, 3).orElseThrow();
        Optional<List<WebElement>> breakdownMinus = driverTools.waitForElementsToAppear(REFUND_BREAKDOWN_MINUS, 1);
        long totalBreakdown = 0;
        for (WebElement breakdown : breakdownPlus) {
            driverTools.waitForElementToAppear(breakdown);
            if (!driverTools.getText(breakdown).contains(currency)) {
                Assertion.fail("breakdown text: "+driverTools.getText(breakdown)+", not contain currency: "+currency);
            }
            totalBreakdown = totalBreakdown + CoreFunctions.removeText(driverTools.getText(breakdown));
        }
        if (breakdownMinus.isPresent()) {
            for (WebElement breakdown : breakdownMinus.get()) {
                totalBreakdown = totalBreakdown + CoreFunctions.removeText(driverTools.getText(breakdown));
            }
        }
        return totalBreakdown;
    }

    public long getTotalRefundMethodAmount(String currency) {
        Optional<List<WebElement>> refundMethodAmountOptional = driverTools.waitForElementsToAppear(REFUND_METHOD_SINGLE_BREAKDOWN, 3);
        if (refundMethodAmountOptional.isEmpty()) {
            refundMethodAmountOptional = driverTools.waitForElementsToAppear(REFUND_METHOD_BREAKDOWN, 3);
        }
        long totalRefund = 0;
        for (WebElement method : refundMethodAmountOptional.orElseThrow()) {
            driverTools.waitForElementToAppear(method);
            if (!driverTools.getText(method).contains(currency)) {
                if (!driverTools.getText(method).contains("poin")) {
                    Assertion.fail("breakdown text: "+driverTools.getText(method)+", not contain currency: "+currency+" or poin");
                }
            }
            totalRefund = totalRefund + CoreFunctions.removeText(driverTools.getText(method));
        }
        return totalRefund;
    }

    public boolean isOrderIdPresentinYourOrder(String orderId) {
        Locator order = new Locator("REFUND_BREAKDOWN_ICON", By.xpath("//*[contains(text(), '"+orderId+"')]"));
        return  driverTools.isElementDisplayed(order);
    }

    public String getOrderDetailSection() {
        WebElement orderDetail = driverTools.waitForElementToAppear(ORDER_DETAIL_SPAN).orElseThrow();
        driverTools.scrollIntoWebView(ORDER_DETAIL);
        return driverTools.getText(orderDetail);
    }

    public abstract void clickBackButton();

    public boolean isOrderDetailSection(){
        driverTools.waitForElementToAppear(ORDER_DETAIL_SPAN);
        return driverTools.isElementDisplayed(ORDER_DETAIL_SPAN);
    }

    public abstract void openVerticalDetails();

    public abstract boolean isVerticalDetailsOpened();

    public void goToYourOrders() {

        switch (Configs.ENVIRONMENT) {
            case GK:
                driverTools.get(STAGING_YOUR_ORDER);
                break;
            case PREPROD:
                driverTools.get(PREPROD_YOUR_ORDER);
                break;
            case PROD:
                driverTools.get(PROD_YOUR_ORDER);
                break;
            default: throw new NotImplementedException("Unimplemented ENV type: " + Configs.ENVIRONMENT.name());
        }
    }

    public abstract void verifyReservationDetails(String hotelName);

    public void verifyBreakdownAmountIsCorrect(String currency) {
        openRefundBreakdown();
        long totalAmount = getTotalRefundAmountDetails(currency);
        long totalActualRefundAmount = CoreFunctions.removeText(getTotalRefund());
        Assertion.assertThat("Verify breakdown amount is correct", totalAmount, is(totalActualRefundAmount));
    }
}
