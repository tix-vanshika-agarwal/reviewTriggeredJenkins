package com.tiket.web.page.order.OrderHistory;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class OrderHistoryMweb <T extends WebDriver> extends OrderHistoryWeb<T> {
    private static final Locator CANCELLED_ORDER = new Locator("CANCELLED_ORDER", new By.ByXPath("//div[contains(@class, 'TiketCard_bottom_row')]//p[contains(@class, 'TiketCard_truncated_text')][normalize-space()='Dibatalkan' or normalize-space()='Canceled']"));
    private static final Locator FILTER_BUTTON = new Locator("FILTER_BUTTON", By.xpath("(//div[contains(@class,'HistoryFooter_main_container')]//*[name()='svg'])[1]"));
    private static final Locator SORT_BUTTON = new Locator("SORT_BUTTON", By.xpath("(//div[contains(@class,'HistoryFooter_main_container')]//*[name()='svg'])[2]"));
    private static final Locator SORT_OPTIONS = new Locator("SORT_OPTIONS", By.xpath("//div[contains(@class,'BottomSheet_children_wrapper')]//span[contains(@class, 'HistorySortOptions_option')][normalize-space()='Tanggal terdekat' or normalize-space()='Closest date']"));

    public OrderHistoryMweb(Session<T> session) {
        super(session);
    }

    @Override
    public boolean isCancelledOrderShown(){
        driverTools.waitForElementToPresence(CANCELLED_ORDER);
        driverTools.scrollIntoWebView(CANCELLED_ORDER);
        return driverTools.element(CANCELLED_ORDER).isPresent();
    }

    @Override
    public boolean deleteOrder(){
        driverTools.waitForElementToPresence(CANCELLED_ORDER);
        driverTools.scrollIntoWebView(CANCELLED_ORDER);
        driverTools.waitForElementToBeClickable(ORDER_CARD);
        driverTools.click(ORDER_CARD);
        skipCoachmark();

        WebElement deleteButton = driverTools.waitForElementToAppear(DELETE_BUTTON_MODAL).orElseThrow();
        driverTools.click(deleteButton);
        WebElement deleteButtonPopup = driverTools.waitForElementToAppear(DELETE_BUTTON_POPUP).orElseThrow();
        driverTools.click(deleteButtonPopup);

        return driverTools.waitForElementToPresence(POPUP_SUCCESS_DELETE).isPresent();
    }

    @Override
    public boolean selectFilterAndVerifyBadge(){
        driverTools.click(FILTER_BUTTON);
        driverTools.waitForElementToBeClickable(FILTER_CHECKBOXES,2);
        List<WebElement> elements = driverTools.elements(FILTER_CHECKBOXES).get();
        driverTools.click(elements.get(1));
        driverTools.click(APPLY_FILTER_BUTTON);
        return driverTools.element(FILTER_BADGE).isPresent();
    }

    @Override
    public boolean applySortingAndVerifyBadge(){
        driverTools.click(SORT_BUTTON);
        driverTools.waitForElementToBeClickable(SORT_OPTIONS,2);
        driverTools.click(SORT_OPTIONS);
        return driverTools.element(SORT_BADGE).isPresent();
    }
}
