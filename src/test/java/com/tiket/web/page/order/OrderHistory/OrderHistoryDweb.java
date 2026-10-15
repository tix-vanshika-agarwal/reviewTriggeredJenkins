package com.tiket.web.page.order.OrderHistory;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ElementClickInterceptedException;

public class OrderHistoryDweb<T extends WebDriver> extends OrderHistoryWeb<T> {
    private static final Locator CANCELLED_ORDER = new Locator("CANCELLED_ORDER", new By.ByXPath("//span[contains (text(), 'Canceled') or contains(text(), 'Dibatalkan')]"));
    private static final Locator FILTER_BUTTON = new Locator("FILTER_BUTTON", By.xpath("//button[contains (@class, 'Chip')]//*[text()='Filter']"));
    private static final Locator SORT_BUTTON = new Locator("SORT_BUTTON", By.xpath("//button[contains (@class, 'Chip')]//*[contains(text(),'Sort') or contains(text(),'Urutkan')]"));
    private static final Locator SORT_LATEST_OPTION = new Locator("SORT_LATEST_OPTION", By.xpath("//div[@aria-label='Latest transaction' or @aria-label='Transaksi terbaru' or @aria-label='Closest date' or @aria-label='Tanggal terdekat']"));

    public OrderHistoryDweb(Session<T> session) {
        super(session);
    }

    @Override
    public boolean isCancelledOrderShown(){
        return driverTools.scrollUntilElementFound(CANCELLED_ORDER, 800, 50).isPresent();
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
        driverTools.clickWithJavascriptExecutor(deleteButtonPopup);

        return driverTools.waitForElementToPresence(POPUP_SUCCESS_DELETE).isPresent();
    }

    @Override
    public boolean selectFilterAndVerifyBadge(){
        driverTools.click(FILTER_BUTTON);
        driverTools.waitForElementToBeClickable(FILTER_CHECKBOXES,2);
        WebElement checkbox = driverTools.elements(FILTER_CHECKBOXES).orElseThrow().get(1);
        driverTools.scrollElementToCenter(checkbox);
        try {
            driverTools.click(checkbox);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(checkbox);
        }
        driverTools.click(APPLY_FILTER_BUTTON);
        return driverTools.waitForElementToAppear(FILTER_BADGE, 3).isPresent();
    }

    @Override
    public boolean applySortingAndVerifyBadge(){
        driverTools.click(SORT_BUTTON);
        WebElement option = driverTools.waitForElementToAppear(SORT_LATEST_OPTION).orElseThrow();
        driverTools.clickWithJavascriptExecutor(option);
        return true;
    }
}
