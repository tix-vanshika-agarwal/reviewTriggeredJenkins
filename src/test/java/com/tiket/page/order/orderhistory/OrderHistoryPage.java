package com.tiket.page.order.orderhistory;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public abstract class OrderHistoryPage <T extends WebDriver> extends BasePage<T> {
    public OrderHistoryPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> OrderHistoryPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new OrderHistoryPageAndroid<>(session);}
            case IOS -> {return new OrderHistoryPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract boolean verifyPastOrderList();
    public abstract boolean verifyPastRefundList(String Case);
    public abstract boolean verifyCancelStatusOnOrderHistoryPage(String orderId, String testTypeFilter, String durationTypeFilter);
    public abstract void deleteOrder(String orderId);
    public abstract boolean isOrderDeleted(String orderID);
    public abstract String getRefundId(String currency);
    public abstract String getRefundProductName(String currency);
    public abstract String getRefundReceiveDate(String currency);
    public abstract String getTotalRefund(String currency);
    public abstract String getRefundStatus(String currency);
    public abstract void selectRefundItem(String currency);
    public abstract void clickFilter();
    public abstract void selectFilterVertical(String vertical, String lang);
    public abstract void setPeriod();
    public abstract void clickApplyFilter();
    public abstract void applySort();
    public abstract Boolean isFinishedOrderShown();
    public abstract Boolean isRescheduleOrderShown();
    public abstract Boolean isRefundCompletedOrderShown();
    public abstract Boolean bothSortAndFilterApplied();
    public abstract Boolean isCancelledOrderShown();
    public abstract void openPastRefund();
    public abstract String getPastRefundCurrency(String refundId);
    public abstract String getPastRefundId(int index);
    public abstract String getPastRefundOrderTitle(String refundId);
    public abstract void swipeToPastRefund(String pastRefundId);
    public abstract boolean verifyPastRefundCard(String refundId,String expectedCurrency);
    public abstract void openPastRefundDetail(String refundId);
    public abstract boolean isRedirectedToCorrectPage();
    public abstract boolean isTwelveMonthsFilterSelected();
    public abstract void clickSeePreviousBookings();
    public abstract boolean isFilterTabOpened();

    public String getCurrency(String price) {
        if (price == null || price.isEmpty()) {
            throw new IllegalArgumentException("Price string cannot be null or empty");
        }
        // Split the string by spaces
        String[] parts = price.split(" ");
        if (parts.length > 1) {
            return parts[0]; // Return the first part as currency
        }
        throw new IllegalArgumentException("Invalid price format: " + price);
    }

    public String getOtherCurrencyThenSaid(String saidCurrency){
        if(saidCurrency.equals("IDR")){
            return "USD";
        }else
            return "IDR";
    }

    private static final Locator PAST_REFUND_DETAIL_REVIEW_IMG = new Locator("PAST_REFUND_DETAIL_REVIEW_IMG", AppiumBy.xpath("//*[contains(@class,'styles_final_status')]//img"));
    private static final Locator PAST_REFUND_DETAIL_REVIEW_TEXT = new Locator("PAST_REFUND_DETAIL_REVIEW_TEXT", AppiumBy.xpath("//*[contains(@class,'styles_final_status')]//*[contains(@class,'Text_text') and contains(@style,'color: rgb(11, 174, 84);')]"));
    private static final Locator REFUND_DETAIL_STATUS_PROGRESS_BAR = new Locator("REFUND_DETAIL_STATUS_PROGRESS_BAR", AppiumBy.xpath("//*[contains(@class,'ProgressStepVertical_progress_step_vertical')]"));
    private static final Locator REFUND_DETAIL_ESTIMATE = new Locator("REFUND_DETAIL_ESTIMATE", AppiumBy.xpath("//*[contains(@class,'Step_content')]//span[contains(text(),'Estimation') or contains(text(),'Estimasi')]"));
    private static final Locator REFUND_DETAIL_STATUS_PROGRESS_BAR_REFUND_STATUS = new Locator("REFUND_DETAIL_STATUS_PROGRESS_BAR_REFUND_STATUS", AppiumBy.xpath("//span[contains(@class,'Step_title') and contains(@class,'highEmphasis')]"));
    private static final Locator REFUND_DETAIL_REFUND_ID = new Locator("REFUND_DETAIL_REFUND_ID", AppiumBy.xpath("//*[contains(@class,'styles_refund_id') and not(contains(@class,'total'))]//*[contains(@class,'Text_text')]"));
    private static final Locator REFUND_DETAIL_TOTAL_REFUND_AMOUNT = new Locator("REFUND_DETAIL_TOTAL_REFUND_AMOUNT", AppiumBy.xpath("(//*[contains(@class,'styles_total')]//*[contains(@class,'Text_text')])[1]"));
    private static final Locator PAST_REFUND_DETAIL_OPEN_REFUND_BREAKDOWN = new Locator("PAST_REFUND_DETAIL_OPEN_REFUND_BREAKDOWN", AppiumBy.xpath("//*[@data-testid='icon-chevron']"));
    private static final Locator PAST_REFUND_DETAIL_REFUND_BREAKDOWN_CLOSE_BTN = new Locator("PAST_REFUND_DETAIL_REFUND_BREAKDOWN_CLOSE_BTN", AppiumBy.xpath("//*[@data-testid='bottom-sheet-close-handle']"));
    private static final Locator REFUND_DETAIL_REFUND_METHOD = new Locator("REFUND_DETAIL_REFUND_METHOD", AppiumBy.xpath("//*[contains(@class,'RefundMethodTrack_container')]"));
    private static final Locator PAST_REFUND_DETAIL_BACK_BTN = new Locator("PAST_REFUND_DETAIL_BACK_BTN", AppiumBy.xpath("//*[@data-testid='icon-back']"));
    private static final Locator ORDER_TITLE = new Locator("ORDER_TITLE", AppiumBy.xpath("//*[contains(@class,'styles_title_wrapper') or contains(@class,'styles_text_wrapper')]//*[text()]"));


    public boolean verifyPastRefundDetail(String refundId,String currency, String orderTitle){
        driverTools.switchToWebViewUrl("my-refund");
        boolean pastRefundDetailReviewImg = driverTools.waitForElementToPresence(PAST_REFUND_DETAIL_REVIEW_IMG).isPresent();
        logger.log("pastRefundDetailReviewImg: " + pastRefundDetailReviewImg);
        Assertion.assertThat("is past refund detail contains review img ",pastRefundDetailReviewImg,is(true));

        boolean pastRefundDetailStatusBar = driverTools.waitForElementToPresence(REFUND_DETAIL_STATUS_PROGRESS_BAR).isPresent();
        logger.log("pastRefundDetailStatusBar: " + pastRefundDetailStatusBar);
        Assertion.assertThat("is past refund detail contains status bar ",pastRefundDetailStatusBar,is(true));

        String pastRefundDetailStatusBarRefundStatus = driverTools.getText(REFUND_DETAIL_STATUS_PROGRESS_BAR_REFUND_STATUS).orElseThrow();
        logger.log("pastRefundDetailStatusBarRefundStatus: " + pastRefundDetailStatusBarRefundStatus);
        Assertion.assertThat("is past refund detail contains status bar refund status",pastRefundDetailStatusBarRefundStatus,not(nullValue()));

        WebElement pastRefundDetailRefundIdElement = driverTools.waitForElementToPresence(REFUND_DETAIL_REFUND_ID).orElseThrow();
        String pastRefundDetailRefundId = driverTools.getText(pastRefundDetailRefundIdElement);
        logger.log("pastRefundDetailRefundId: " + pastRefundDetailRefundId);
        Assertion.assertThat("is past refund detail id correct",refundId,is(containsString(pastRefundDetailRefundId)));

        WebElement pastRefundDetailPriceElement = driverTools.waitForElementToPresence(REFUND_DETAIL_TOTAL_REFUND_AMOUNT).orElseThrow();
        String currentCurrency = getCurrency(driverTools.getText(pastRefundDetailPriceElement));
        logger.log("current currency : " + currentCurrency);
        Assertion.assertThat("is current currency is same as expected currency",currentCurrency,equalTo(currency));

        boolean pastRefundDetailRefundMethod = driverTools.waitForElementToPresence(REFUND_DETAIL_REFUND_METHOD).isPresent();
        logger.log("pastRefundDetailRefundMethod: " + pastRefundDetailRefundMethod);
        Assertion.assertThat("is past refund detail contains refund method",pastRefundDetailRefundMethod,is(true));

        String orderTitleDetail = driverTools.getText(ORDER_TITLE).orElseThrow();
        logger.log("title: " + orderTitleDetail);
        Assertion.assertThat("is refund detail title correct ",orderTitleDetail,is(orderTitle));

        driverTools.switchToNative();
        openBreakDownDetail();
        isCalculationOnBreakdownCorrect(currency);
        closeBreakDown();
        return true;
    }

    public void openBreakDownDetail(){
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.click(PAST_REFUND_DETAIL_OPEN_REFUND_BREAKDOWN);
        driverTools.switchToNative();
    }

    public void isCalculationOnBreakdownCorrect(String currency){
        driverTools.switchToWebViewUrl("my-refund");
        List<WebElement> prices = driverTools.elements(pastRefundDetailPriceBreakdownPrice(currency)).orElseThrow();
        ArrayList<Double> arrayPrice = new ArrayList<Double>();

        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            try {
                double price = CoreFunctions.convertPrice(text);
                arrayPrice.add(price);
            } catch (NumberFormatException ignored) {
            }
        }

        double sum = 0;
        for (double i : arrayPrice) {
            sum += i;
        }

        WebElement totalPriceElement = driverTools.waitForElementToPresence(pastRefundDetailPriceBreakdownTotalPrice(currency)).orElseThrow();
        String totalPriceText = driverTools.getText(totalPriceElement);

        double totalPrice = CoreFunctions.convertPrice(totalPriceText);
        logger.log("Total price text:  " + totalPrice);
        logger.log("Total price got:  " + sum);
        driverTools.switchToNative();
        Assertion.assertThat("is sum price equal to total price",totalPrice,is(equalTo(sum)));
    }

    public void closeBreakDown(){
        driverTools.switchToWebViewUrl("my-refund");
        boolean closed = driverTools.tryClickWhenPresent(PAST_REFUND_DETAIL_REFUND_BREAKDOWN_CLOSE_BTN);
        if (!closed) {
        logger.softWarning("closeBreakDown: bottom sheet close button not found — assuming bottom sheet is not shown, skipping close.");
        }
        driverTools.switchToNative();
    }

    public void clickBackBtnOnRefundDetailPage(){
        driverTools.switchToWebViewUrl("my-refund");
        driverTools.click(PAST_REFUND_DETAIL_BACK_BTN);
        driverTools.switchToNative();
    }

    private Locator pastRefundDetailPriceBreakdownPrice(String currency) {
        return new Locator("PAST_REFUND_DETAIL_REFUND_BREAKDOWN_PRICE" + currency.toUpperCase(), AppiumBy.xpath("//*[contains(@class,'styles_amount')]//*[contains(@class,'Text_text') and contains(text(),'"+currency+"')]"));
    }

    private Locator pastRefundDetailPriceBreakdownTotalPrice(String currency) {
        return new Locator("PAST_REFUND_DETAIL_REFUND_BREAKDOWN_TOTAL_PRICE" + currency.toUpperCase(), AppiumBy.xpath("//*[contains(@class,'styles_total_refund')]//*[contains(@class,'Text_text') and contains(text(),'"+currency+"')]"));
    }

    public boolean verifyActiveRefundDetail(String refundId,String currency, String orderTitle){
        driverTools.switchToWebViewUrl("my-refund");
        boolean activeRefundDetailEstimate = driverTools.waitForElementToPresence(REFUND_DETAIL_ESTIMATE).isPresent();
        logger.log("activeRefundDetailEstimate: " + activeRefundDetailEstimate);
        Assertion.assertThat("is refund detail contains estimate ",activeRefundDetailEstimate,is(true));

        boolean activeRefundDetailStatusBar = driverTools.waitForElementToPresence(REFUND_DETAIL_STATUS_PROGRESS_BAR).isPresent();
        logger.log("activeRefundDetailStatusBar: " + activeRefundDetailStatusBar);
        Assertion.assertThat("is active refund detail contains status bar ",activeRefundDetailStatusBar,is(true));

        String activeRefundDetailStatusBarRefundStatus = driverTools.getText(REFUND_DETAIL_STATUS_PROGRESS_BAR_REFUND_STATUS).orElseThrow();
        logger.log("activeRefundDetailStatusBarRefundStatus: " + activeRefundDetailStatusBarRefundStatus);
        Assertion.assertThat("is active refund detail contains status bar refund status",activeRefundDetailStatusBarRefundStatus,not(nullValue()));

        WebElement activeRefundDetailRefundIdElement = driverTools.waitForElementToPresence(REFUND_DETAIL_REFUND_ID).orElseThrow();
        String activeRefundDetailRefundId = driverTools.getText(activeRefundDetailRefundIdElement);
        logger.log("activeRefundDetailRefundId: " + activeRefundDetailRefundId);
        Assertion.assertThat("is active refund detail id correct",refundId,is(containsString(activeRefundDetailRefundId)));

        WebElement activeRefundDetailPriceElement = driverTools.waitForElementToPresence(REFUND_DETAIL_TOTAL_REFUND_AMOUNT).orElseThrow();
        String currentCurrency = getCurrency(driverTools.getText(activeRefundDetailPriceElement));
        logger.log("current currency : " + currentCurrency);
        Assertion.assertThat("is current currency is same as expected currency",currentCurrency,equalTo(currency));

        boolean activeRefundDetailRefundMethod = driverTools.waitForElementToPresence(REFUND_DETAIL_REFUND_METHOD).isPresent();
        logger.log("activeRefundDetailRefundMethod: " + activeRefundDetailRefundMethod);
        Assertion.assertThat("is active refund detail contains refund method",activeRefundDetailRefundMethod,is(true));

        String orderTitleDetail = driverTools.getText(ORDER_TITLE).orElseThrow();
        logger.log("title: " + orderTitleDetail);
        Assertion.assertThat("is refund detail title correct ",orderTitleDetail,is(orderTitle));

        driverTools.switchToNative();
        openBreakDownDetail();
        isCalculationOnBreakdownCorrect(currency);
        closeBreakDown();
        return true;
    }
}
