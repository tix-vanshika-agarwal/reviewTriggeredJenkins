package com.tiket.web.page.order.OrderHistory;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public abstract class OrderHistoryWeb<T extends WebDriver> extends BasePage<T> {
    static HashMap<String,Boolean> sessionToCoachmark = new HashMap<String,Boolean>();

    public OrderHistoryWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> OrderHistoryWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new OrderHistoryDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new OrderHistoryMweb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private final static Locator OK_COACHMARK_BUTTON = new Locator("OK_COACHMARK_BUTTON", new By.ByXPath("//div[contains(@class,'coachmark')]//button[contains(text(),'OK') or contains(text(),'Oke')]"));
    private final static Locator SKIP_COACHMARK_BUTTON = new Locator("SKIP_COACHMARK_BUTTON", new By.ByXPath("//div[contains(@class,'coachmark')]//button[contains(text(),'Skip') or contains(text(),'Lewati')]"));
    private static final Locator PAST_REFUND_TAB = new Locator("PAST_REFUND_TAB", By.xpath("//*[contains(text(),'Past Refund') or contains(text(),'Refund Lama')]"));
    private static final Locator PAST_REFUND_LIST_LOADING = new Locator("PAST_REFUND_LIST_LOADING", By.xpath("//*[contains(@class,'PastRefunds_loading')]"));
    public static final Locator FILTER_BADGE = new Locator("FILTER_BADGE", By.xpath("//button[contains(@class, 'ActionChip')][.//span[text()='Filter']]//span[contains(@class, 'Badge_badge')]"));
    public static final Locator FILTER_CHECKBOXES = new Locator("FILTER_CHECKBOXES", By.xpath("//span[contains (@class, 'Check_custom_checkbox')]"));
    public static final Locator APPLY_FILTER_BUTTON = new Locator("APPLY_FILTER_BUTTON", By.xpath("//button[contains(@data-testid, 'apply-filter')]"));
    public static final Locator SORT_BADGE = new Locator("SORT_BADGE", By.xpath("//button[contains (@data-testid, 'sort')]//span[contains (@class, 'Chip_badge')]"));
    private static final Locator CLOSE_MODAL_BUTTON = new Locator("CLOSE_MODAL_BUTTON", new By.ByXPath("//button[contains(@class,'modal_close')]"));
    public static final Locator ORDER_CARD = new Locator("ORDER_CARD", new By.ByXPath("//div[contains (@class, 'TiketCard_ticket_header')]"));
    private static final Locator FINISHED_ORDER = new Locator("FINISHED_ORDER", new By.ByXPath("(//span[contains (text(), 'Selesai') or contains(text(), 'Finished')])[1]"));
    private static final Locator CANCELLED_ORDER = new Locator("CANCELLED_ORDER", new By.ByXPath("(//span[contains (text(), 'Canceled') or contains(text(), 'Dibatalkan')])[1]"));
    public static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("//button[contains (text(), 'Hapus') or contains(text(), 'Delete')]"));
    public static final Locator DELETE_BUTTON_MODAL = new Locator("DELETE_BUTTON_MODAL", new By.ByXPath("//*[contains (text(), 'Hapus') or contains(text(), 'Delete')]"));
    public static final Locator DELETE_BUTTON_POPUP = new Locator("DELETE_BUTTON_POPUP", new By.ByXPath("//button[normalize-space()='Hapus'] | //button[normalize-space()='Delete']"));
    public static final Locator POPUP_SUCCESS_DELETE = new Locator("SNACKBAR_DELETE_ORDER", new By.ByXPath("//div[contains (@class,'Snackbar_text_content')]"));
    private static Locator getRefundIdByCurrency(String currency) {
        return new Locator("REFUNDID_CURRENCY_"+currency.toUpperCase(), By.xpath("//div[contains(@class,'RefundCard_content_container') and .//span[contains(text(),'" + currency + "')]]//div[contains(@class,'RefundCard_header')]//span"));
    }
    private static Locator getRefundStatusByRefundId(String refundId) {
        return new Locator("REFUNDID_" + refundId + "_STATUS", By.xpath("//div[contains(@class,'RefundCard_content_container')][.//span[contains(text(),'" + refundId + "')]]//span[contains(@class,'RefundCard_action_item')]"));
    }
    private static Locator getProductNameByRefundId(String refundId) {
        return new Locator("REFUNDID_" + refundId + "_PRODUCT_NAME", By.xpath("//div[contains(@class,'RefundCard_content_container')][.//span[contains(text(),'" + refundId + "')]]//div[contains(@class,'RefundCard_heading')]//span[contains(@class,'RefundCard_title')]"));
    }
    private static Locator getTotalRefundByRefundId(String refundId) {
        return new Locator("REFUNDID_" + refundId + "_TOTAL_REFUND", By.xpath("//div[contains(@class,'RefundCard_content_container')][.//span[contains(text(),'" + refundId + "')]]//span[contains(text(),'Total refund')]/ancestor::div[contains(@class,'RefundCard_sub_content_wrapper')]//span[contains(@class,'RefundCard_text_black')]"));
    }
    private static Locator getReceiveDateByRefundId(String refundId) {
        return new Locator("REFUNDID_" + refundId + "_RECEIVE_DATE", By.xpath("//div[contains(@class,'RefundCard_content_container')][.//span[contains(text(),'" + refundId + "')]]//span[contains(text(),'Payment received')]/ancestor::div[contains(@class,'RefundCard_sub_content_wrapper')]//span[contains(@class,'RefundCard_text_black')]"));
    }

    public void openPastRefundTab() {
        WebElement pastRefundTab = driverTools.waitForElementToAppear(PAST_REFUND_TAB).orElseThrow();
        driverTools.click(pastRefundTab);
    }

    public void scrollToPastRefundListLoader() {
        WebElement loader = driverTools.waitForElementToAppear(PAST_REFUND_LIST_LOADING, 2).orElseThrow();
        driverTools.scrollIntoWebView(loader);
    }

    public String getFirstRefundIdByCurrency(String currency) {
        Optional<WebElement> refundCard = driverTools.waitForElementToPresence(getRefundIdByCurrency(currency));

        if (refundCard.isEmpty()) {
            logger.log("Refund ID not found for currency: " + currency);
            return "";
        }

        String refundId = driverTools.getText(getRefundIdByCurrency(currency))
                .orElseThrow();
        logger.log("REFUND ID " + refundId);

        refundId = CoreFunctions.getIntFromString(refundId)
                .orElseThrow()
                .toString();

        logger.log("Got Refund Id by currency " + currency + " on past refund list: " + refundId);
        return refundId;
    }

    public String getRefundStatusCard(String refundId) {
        WebElement refundStatus = driverTools.waitForElementToAppear(getRefundStatusByRefundId(refundId)).orElseThrow();
        return driverTools.getText(refundStatus);
    }

    public String getProductNameCard(String refundId) {
        WebElement productName = driverTools.waitForElementToAppear(getProductNameByRefundId(refundId)).orElseThrow();
        return driverTools.getText(productName);
    }

    public String getReceiveDateCard(String refundId) {
        WebElement receiveDate = driverTools.waitForElementToAppear(getReceiveDateByRefundId(refundId)).orElseThrow();
        return driverTools.getText(receiveDate);
    }

    public String getTotalRefundAmountCard(String refundId) {
        WebElement refundAmount = driverTools.waitForElementToAppear(getTotalRefundByRefundId(refundId)).orElseThrow();
        return driverTools.getText(refundAmount);
    }

    public void clickOnRefundCard(String refundId) {
        driverTools.click(getProductNameByRefundId(refundId));
    }

    public boolean verifyPastRefundTab(){
        return driverTools.waitForElementToAppear(PAST_REFUND_TAB).isPresent();
    }

    public boolean isOrderCardExist(){
        return driverTools.waitForElementToAppear(ORDER_CARD).isPresent();
    }

    public abstract boolean selectFilterAndVerifyBadge();

    public abstract boolean applySortingAndVerifyBadge();

    public boolean isCancelledOrderShown(){
        driverTools.waitForElementToPresence(CANCELLED_ORDER);
        driverTools.scrollIntoWebView(CANCELLED_ORDER);
        return driverTools.element(CANCELLED_ORDER).isPresent();
    }

    public boolean isFinishedOrderShown(){
        driverTools.waitForElementToPresence(FINISHED_ORDER);
        driverTools.scrollIntoWebView(FINISHED_ORDER);
        return driverTools.element(FINISHED_ORDER).isPresent();
    }

    public void skipCoachmark() {
        if(sessionToCoachmark.get(session.sessionId()) != null && sessionToCoachmark.get(session.sessionId())){
            return;
        }
        Optional<WebElement> skipButton = driverTools.waitForElementToAppear(SKIP_COACHMARK_BUTTON,3);
        Optional<WebElement> okButton = driverTools.waitForElementToAppear(OK_COACHMARK_BUTTON,3);
        skipButton.ifPresent(element -> {
            if (driverTools.element(SKIP_COACHMARK_BUTTON).isPresent()) {
                driverTools.clickWithJavascriptExecutor(SKIP_COACHMARK_BUTTON);
                driverTools.waitForElementToDisappear(SKIP_COACHMARK_BUTTON);
                sessionToCoachmark.put(session.sessionId(), true);
            }
        });
        okButton.ifPresent(element -> {
            if (driverTools.element(OK_COACHMARK_BUTTON).isPresent()) {
                driverTools.clickWithJavascriptExecutor(OK_COACHMARK_BUTTON);
                driverTools.waitForElementToDisappear(OK_COACHMARK_BUTTON);
                sessionToCoachmark.put(session.sessionId(), true);
            } else {
                logger.log("No coachmark button spotted");
            }
        });
        driverTools.refreshWebpage();
    }

    public abstract boolean deleteOrder();
}
