package com.tiket.web.page.order.OrderList;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;

import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class OrderListMweb <T extends WebDriver> extends OrderListWeb<T> {
    private static final Locator ELLIPSIS_CANCEL_BUTTON = new Locator("ELLIPSIS_CANCEL_BUTTON", By.xpath("//div[contains(@class, 'BottomSheet_children_wrapper')]//div//span[contains(@class, 'OrderCard_truncate')][normalize-space()='Cancel order' or normalize-space()='Batalkan pesanan']"));
    private static final Locator ORDER_HISTORY_BUTTON = new Locator("ORDER_HISTORY_BUTTON", By.xpath("//div[contains(@class, 'Header_heading_wrapper')]//*[name()='svg'][starts-with(@class, 'Header_icon')]"));

    @Override
    protected Locator getRefundIdByCurrency(String currency) {
        return new Locator("REFUNDID_CURRENCY_"+currency.toUpperCase(), By.xpath("//*[contains(@class,'RefundCard_text') and contains(text(),'"+currency+"')]/ancestor::*[contains(@class,'RefundCard_main_container')]//*[contains(@class,'RefundCard_header_container__')]"));
    }

    public  OrderListMweb(Session<T> session){
        super(session);
    }

    @Override
    public void openOrderHistoryPage() {
        WebElement orderHistoryButton = driverTools.waitForElementToAppear(ORDER_HISTORY_BUTTON).orElseThrow();
        driverTools.dummyWait(3);
        driverTools.click(orderHistoryButton);
    }

    @Override
    public boolean cancelOrder(){
        driverTools.waitForElementToAppear(RESERVATION_DETAIL_HEADER,3);
        driverTools.refreshWebpage();

        driverTools.waitForElementToAppear(ELLIPSIS_BUTTON);
        driverTools.click(ELLIPSIS_BUTTON);

        WebElement cancelBtn = driverTools.waitForElementToAppear(ELLIPSIS_CANCEL_BUTTON).orElseThrow();
        driverTools.click(cancelBtn);

        driverTools.waitForElementToAppear(CHECKBOX_CANCEL_BUTTON).orElseThrow();
        List<WebElement> checkBoxes = driverTools.elements(CHECKBOX_CANCEL_BUTTON).orElseThrow();
        for (WebElement checkbox : checkBoxes) {
            driverTools.click(checkbox);
        }
        logger.log("checkboxes size: " + checkBoxes.size());
        WebElement agreeBtn = driverTools.waitForElementToAppear(AGREE_CANCEL_BUTTON).orElseThrow();
        driverTools.click(agreeBtn);

        return driverTools.waitForElementToPresence(POPUP_SUCCESS_CANCEL).isPresent();
    }
}
