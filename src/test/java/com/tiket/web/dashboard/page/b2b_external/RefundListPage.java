package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.api.b2b.AcceptBookingDashboardApi;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dWeb.page.account.AccountPage;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.ExecutionException;

public class RefundListPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator SEARCH_TEXTFIELD = new Locator("SEARCH_TEXTFIELD", new By.ByXPath("//input[@name='search' or @id='email-or-order-id']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[contains(text(), 'Search') or contains(text(), 'SEARCH')]"));
    private static final Locator CLOSE_DIALOG = new Locator("CLOSE_DIALOG", new By.ByXPath("//i[@class='tix tix-cancel']"));
    private static final Locator REFUND_LIST_BUTTON = new Locator("REFUND_LIST_BUTTON", By.xpath("//button[.//span[text()='Refund List']]"));
    public static final Locator REFUND_REJECT = new Locator("REFUND_REJECT", By.xpath("//button[text()='Reject']"));
    public static final Locator BUTTON_REFUND_APPROVE = new Locator("BUTTON_REFUND_APPROVE", By.xpath("//button[text()='Approve']"));
    public static final Locator CONFIRM_YES_BUTTON = new Locator("CONFIRM_YES_BUTTON", By.xpath("//button[text()='Yes']"));
    public static final Locator REJECT_REASON = new Locator("REJECT_REASON", By.id("reason"));
    public static final Locator TOAST_REJECT_SUCCESS = new Locator("TOAST_REJECT_SUCCESS", By.xpath("//*[contains(text(), \"You've successfully reject the refund\")]"));
    public static final Locator TOAST_APPROVE_SUCCESS = new Locator("TOAST_APPROVE_SUCCESS", By.xpath("//div[contains(@class,'Snackbar_text_content') and contains(text(),\"You've successfully approve the refund.\")]"));


    public RefundListPage(Session<T> session) {
        super(session);
    }

    public void goToRefundListPage(){
        driverTools.waitForElementToAppear(REFUND_LIST_BUTTON);
        driverTools.click(REFUND_LIST_BUTTON);
        driverTools.waitForElementToAppear(REFUND_LIST_BUTTON).orElseThrow();
        closeOnBoardingDialog();
    }

    public void closeOnBoardingDialog(){
        driverTools.waitForElementToAppear(CLOSE_DIALOG, 2);
        if(driverTools.isElementDisplayed(CLOSE_DIALOG)){
            driverTools.click(CLOSE_DIALOG);
        }
    }

    public void searchByOrderID(String orderID){
        setSearchTextfield(orderID);
        hitSearchButton();
        checkIfSearchedOrderExists(orderID);
    }

    public void setSearchTextfield(String orderID){
        driverTools.click(SEARCH_TEXTFIELD);
        driverTools.sendKeys(SEARCH_TEXTFIELD, orderID);
    }

    public void hitSearchButton(){
        driverTools.waitForElementToBeClickable(SEARCH_BUTTON).orElseThrow();
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean checkIfSearchedOrderExists(String orderID){
        Locator ORDERID_DETAIL_TEXT = getOrderDetailText(orderID);
        driverTools.waitForElementToAppear(ORDERID_DETAIL_TEXT);
        return driverTools.isElementDisplayed(ORDERID_DETAIL_TEXT);
    }

    public boolean verifyRefund(String orderID){
        Locator ORDERID_VERIFICATION_TEXT = new Locator("ORDERID_VERIFICATION_TEXT",By.xpath("//span[@class='cursor-pointer' and contains(text(), '"+orderID+"')]"));
        driverTools.waitForElementToAppear(ORDERID_VERIFICATION_TEXT);
        return driverTools.isElementDisplayed(ORDERID_VERIFICATION_TEXT);
    }

    public Locator getOrderDetailText(String orderID){
        Locator ORDERID_DETAIL_TEXT = new Locator("ORDERID_DETAIL_TEXT", new By.ByXPath("//*[contains(text(),'Order ID')]"));
        return ORDERID_DETAIL_TEXT;
    }

    public void approveRefund(){
        driverTools.waitForElementToBeClickable(BUTTON_REFUND_APPROVE).orElseThrow();
        driverTools.clickWithJavascriptExecutor(BUTTON_REFUND_APPROVE);
        driverTools.waitForElementToBeClickable(CONFIRM_YES_BUTTON).orElseThrow();
        driverTools.click(CONFIRM_YES_BUTTON);
        driverTools.waitForElementToAppear(TOAST_APPROVE_SUCCESS);
    }

    public void rejectRefund(String reason){
        driverTools.waitForElementToBeClickable(REFUND_REJECT).orElseThrow();
        driverTools.clickWithJavascriptExecutor(REFUND_REJECT);
        driverTools.waitForElementToAppear(REJECT_REASON);
        driverTools.sendKeys(REJECT_REASON, reason);
        driverTools.waitForElementToBeClickable(CONFIRM_YES_BUTTON).orElseThrow();
        driverTools.click(CONFIRM_YES_BUTTON);
        driverTools.waitForElementToAppear(TOAST_REJECT_SUCCESS);
    }

    public boolean isRefundRejected(){
        driverTools.waitForElementToAppear(TOAST_REJECT_SUCCESS);
        return driverTools.isElementDisplayed(TOAST_REJECT_SUCCESS);
    }

    public boolean isRefundApproved(){
        driverTools.waitForElementToAppear(TOAST_APPROVE_SUCCESS);
        return driverTools.isElementDisplayed(TOAST_APPROVE_SUCCESS);
    }

    public boolean isRefundListPageDisplayed(){
        return driverTools.getCurrentURL().contains("refund-list");
    }
}