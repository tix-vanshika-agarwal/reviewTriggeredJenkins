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

public class PurchaseListPage<T extends WebDriver> extends BasePage<T> {
    private static final String GK_CORP_DASHBOARD = "https://corporate-gatotkaca.tiket.com";
    private static final String PREPROD_CORP_DASHBOARD = "https://corporate.preprod.tiket.com";
    private static final String PROD_CORP_DASHBOARD = "https://corporate.tiket.com";
    private static final Locator PURCHASE_LIST_HEADER = new Locator("PURCHASE_LIST_HEADER", new By.ByXPath("//span[contains(text(), 'Purchase List')]"));
    private static final Locator SEARCH_TEXTFIELD = new Locator("SEARCH_TEXTFIELD", new By.ByXPath("//input[@name='search' or @id='email-or-order-id']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[normalize-space(.)='Search' or normalize-space(.)='SEARCH']"));
    private static final Locator CLOSE_DIALOG = new Locator("CLOSE_DIALOG", new By.ByXPath("//i[@class='tix tix-cancel']"));
    private static final Locator APPROVE_BUTTON = new Locator("APPROVE_BUTTON", new By.ByXPath("//*[text()='Approve']"));
    private static final Locator APPROVE_BUTTON_MODAL_DIALOG = new Locator("APPROVE_BUTTON_MODAL_DIALOG", new By.ByXPath("//div[contains(@class, 'modal_footer')]//button[contains(@class, 'primary')]"));
    private static final Locator EXPIRED_PURCHASE_LIST = new Locator("EXPIRED_PURCHASE_LIST", new By.ByXPath("//span[contains(text(), \"Expired Purchase\")]"));
    private static final Locator PURCHASE_LIST_CARD = new Locator("PURCHASE_LIST_CARD", new By.ByXPath("//*[contains(@class, 'card_container')]"));
    private static final Locator EXPIRED_PURCHASE_CARD = new Locator("EXPIRED_PURCHASE_CARD", new By.ByXPath("//*[contains(text(), 'Booking Expired') or contains(text(), 'Canceled')]"));
    private static final Locator REFRESH = new Locator("REFRESH", new By.ByXPath("//button[normalize-space()='Refresh']"));
    private static final Locator WAITING_TEXT = new Locator("WAITING_TEXT", new By.ByXPath("//p[contains(text(), 'Waiting for payment')]"));
    private static final Locator BOOKINGS_SIDEBAR = new Locator("BOOKINGS_SIDEBAR", By.xpath("//span[text()='Bookings']"));
    private static final Locator PENDING_APPROVALS = new Locator("PENDING_APPROVALS", By.xpath("//*[text()='Pending Approvals']"));

    public PurchaseListPage(Session<T> session) {
        super(session);
    }

    private String getEnvironmentUrl() {
        return switch (Configs.ENVIRONMENT) {
            case PROD -> PROD_CORP_DASHBOARD;
            case PREPROD -> PREPROD_CORP_DASHBOARD;
            case GK -> GK_CORP_DASHBOARD;
            default -> "";
        };
    }

    public void goToPurchaseListPage(){
        driverTools.waitForElementToAppear(BOOKINGS_SIDEBAR);
        driverTools.click(BOOKINGS_SIDEBAR);

        driverTools.waitForElementToAppear(PENDING_APPROVALS);
        driverTools.click(PENDING_APPROVALS);

        driverTools.waitForElementToAppear(PURCHASE_LIST_HEADER).orElseThrow();
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

    public boolean checkWaitingForPayment(String orderID) {
        Locator ORDERID_TEXT = getOrderDetailText(orderID);

        driverTools.waitForElementToAppear(ORDERID_TEXT);
        return driverTools.isElementPresent(WAITING_TEXT) && driverTools.isElementPresent(ORDERID_TEXT);
    }

    public Locator getOrderDetailText(String orderID){
        return new Locator("ORDERID_TEXT", new By.ByXPath("//div[text()='" + orderID + "']"));
    }
    public void approvePurchase(String orderID){
        driverTools.scrollToElement(APPROVE_BUTTON);
        driverTools.waitForElementToBeClickable(APPROVE_BUTTON);
       driverTools.clickWithJavascriptExecutor(APPROVE_BUTTON);
       driverTools.waitForElementToAppear(APPROVE_BUTTON_MODAL_DIALOG, 120);
       driverTools.click(APPROVE_BUTTON_MODAL_DIALOG);
       checkIfOrderRemovedFromPurchaseList(orderID);
    }

    public boolean checkIfOrderRemovedFromPurchaseList(String orderID){
        Locator ORDERID_DETAIL_TEXT = getOrderDetailText(orderID);
        driverTools.waitForElementToDisappear(ORDERID_DETAIL_TEXT);
        return driverTools.isElementDisplayed(ORDERID_DETAIL_TEXT);
    }

    public void headToBookingListPage(){
        String url = getEnvironmentUrl() + "/booking-list";
        driverTools.get(url);
    }

    public void openExpiredPurchaseList(){
        driverTools.click(EXPIRED_PURCHASE_LIST);
        driverTools.waitForElementToAppear(EXPIRED_PURCHASE_CARD);
    }

    public boolean checkExpiredPurchaseList(){
        return(driverTools.isElementDisplayed(EXPIRED_PURCHASE_CARD) || driverTools.isElementDisplayed(REFRESH));
    }

    public boolean isPurchaseListOpened(){
        return driverTools.isElementDisplayed(PURCHASE_LIST_CARD);
    }

    private Boolean acceptBooking(AcceptBookingDashboardApi.Inputs inputs) throws ExecutionException, InterruptedException {
        AcceptBookingDashboardApi acceptBookingApi = new AcceptBookingDashboardApi(inputs);
        ApiResponse acceptApiResponse = acceptBookingApi.hitApi(apiTools);
        JSONObject acceptStatus = new JSONObject(acceptApiResponse.getBody());
        logger.log("res->" + acceptStatus);
        return acceptStatus.getJSONObject("data").getInt("affectedRow") == 1;
    }
    
    @SneakyThrows
    public Boolean approveBookingB2BApi(
            String username,
            String password,
            String orderID
    ) {
        AccountPage<?> accountPage = new AccountPage<>(session);
        String accessToken = accountPage.getAccessTokenUNM(username, password);
        logger.log("accessToken-> " + accessToken);

        String loginAccessToken = accountPage.getAccessTokenUNM(username, password);
        logger.log("accessToken for dashboard request->" + loginAccessToken);

        AcceptBookingDashboardApi.Inputs acceptBookingInputs = new AcceptBookingDashboardApi.Inputs(accessToken, loginAccessToken, orderID);
        Boolean bookingStatus = acceptBooking(acceptBookingInputs);
        logger.log("Bokking approved" + bookingStatus);

        return bookingStatus;
    }

    public boolean isOrderCardPresent(String orderId) {
        String cardXpath = String.format("//div[contains(., '%s')]/ancestor::div[contains(@class, 'Card') or contains(@class, 'card') or contains(@class, 'container')][1]", orderId);
        Locator orderCard = new Locator("Order Card " + orderId, By.xpath(cardXpath));
        return driverTools.waitForElementToAppear(orderCard, 10).isPresent();
    }

    public void clickRefundAndReschedulePolicyForSpecificCard(String orderId) {
        String xpath = String.format("//div[contains(., '%s')]/ancestor::div[contains(@class, 'Card') or contains(@class, 'card')][1]//span[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'refund') and contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'reschedule')]", orderId);
        Locator policyBtn = new Locator("Refund & Reschedule Policy Btn for " + orderId, By.xpath(xpath));

        driverTools.waitForElementToBeClickable(policyBtn);
        driverTools.scrollElementToCenter(driverTools.waitForElementToAppear(policyBtn).orElseThrow());
        driverTools.clickWithJavascriptExecutor(policyBtn);
        driverTools.dummyWait(2);
    }

    public boolean isSpecificTextVisibleInCard(String orderId, String expectedText) {
        String cardXpath = String.format("//div[contains(., '%s')]/ancestor::div[contains(@class, 'Card') or contains(@class, 'card')][1]", orderId);
        Locator textElement = new Locator("Text [" + expectedText + "] inside " + orderId, By.xpath(cardXpath + "//*[contains(text(), '" + expectedText + "') or contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + expectedText.toLowerCase() + "')]"));

        return driverTools.waitForElementToAppear(textElement, 5).isPresent();
    }

    public boolean isRoomTypeHeaderVisibleInPolicyTab(String orderId) {
        String cardXpath = String.format("//div[contains(., '%s')]/ancestor::div[contains(@class, 'Card') or contains(@class, 'card')][1]", orderId);
        Locator roomTypeHeader = new Locator("Room Type Header for " + orderId,
                By.xpath(cardXpath + "//th[contains(@class, 'PolicyDetail_table') and normalize-space(text())='Room Type']"));
        return driverTools.waitForElementToAppear(roomTypeHeader, 5).isPresent();
    }

    public boolean isCheckInDateVisibleInPolicyTab(String orderId, String expectedCheckInDate) {
        String cardXpath = String.format("//div[contains(., '%s')]/ancestor::div[contains(@class, 'Card') or contains(@class, 'card')][1]", orderId);
        Locator dateLocator = new Locator("Check-in Date inside Policy Tab for " + orderId, By.xpath(cardXpath + "//div[contains(@class, 'p-[5px] flex')]//p[contains(text(), '" + expectedCheckInDate + "')]"));
        return driverTools.waitForElementToAppear(dateLocator, 5).isPresent();
    }

    public boolean isFlightRouteVisibleInCard(String orderId) {
        String cardXpath = String.format("//div[contains(., '%s')]/ancestor::div[contains(@class, 'Card') or contains(@class, 'card')][1]", orderId);
        Locator routeElement = new Locator("Flight Route Label inside " + orderId, By.xpath(cardXpath + "//*[contains(text(), 'Departure Flight') or contains(text(), 'Penerbangan Pergi')]"));
        return driverTools.waitForElementToAppear(routeElement, 5).isPresent();
    }

    public boolean isMultiCitySummaryVisible(String orderId, int flights) {
        String cardXpath = String.format("//div[contains(., '%s')]/ancestor::div[contains(@class, 'Card') or contains(@class, 'card')][1]", orderId);
        Locator summary = new Locator("Multi-city summary inside " + orderId, By.xpath(cardXpath + "//span[contains(., 'Multi-city') and contains(., '" + flights + " flights')]"));
        return driverTools.waitForElementToAppear(summary, 5).isPresent();
    }

    public boolean isMultiCitySegmentRouteVisible(String orderId, int segment, String origin, String destination) {
        String cardXpath = String.format("//div[contains(., '%s')]/ancestor::div[contains(@class, 'Card') or contains(@class, 'card')][1]", orderId);
        Locator route = new Locator("Multi-city Flight " + segment + " route inside " + orderId, By.xpath(cardXpath + "//div[normalize-space()='Multi-city Flight " + segment + "']/following-sibling::span[contains(., '(" + origin + ")') and contains(., '(" + destination + ")')]"));
        return driverTools.waitForElementToAppear(route, 5).isPresent();
    }

    public String getMultiCitySegmentAirline(String orderId, int segment) {
        String cardXpath = String.format("//div[contains(., '%s')]/ancestor::div[contains(@class, 'Card') or contains(@class, 'card')][1]", orderId);
        Locator airline = new Locator("Multi-city Flight " + segment + " airline inside " + orderId, By.xpath(cardXpath + "//div[normalize-space()='Multi-city Flight " + segment + "']/../following-sibling::div//td[@rowspan]/span"));
        driverTools.waitForElementToAppear(airline, 5);
        return driverTools.getText(airline).orElse("");
    }

    public void rejectSpecificOrder(String orderId, String rejectReason) {
        String cardXpath = String.format("//div[contains(., '%s')]/ancestor::div[contains(@class, 'Card') or contains(@class, 'card')][1]", orderId);
        Locator rejectBtn = new Locator("Reject Button for " + orderId, By.xpath(cardXpath + "//button[contains(normalize-space(.), 'Reject') or contains(normalize-space(.), 'Tolak')]"));

        driverTools.waitForElementToBeClickable(rejectBtn);
        driverTools.scrollElementToCenter(driverTools.waitForElementToAppear(rejectBtn).orElseThrow());
        driverTools.clickWithJavascriptExecutor(rejectBtn);
        driverTools.dummyWait(1);

        Locator reasonInput = new Locator("Reject Reason Input", By.xpath("//textarea | //input[@id='reason' or @placeholder='Reason']"));
        if(driverTools.waitForElementToAppear(reasonInput, 3).isPresent()){
            driverTools.clearInput(reasonInput);
            driverTools.sendKeys(reasonInput, rejectReason);

            Locator confirmRejectBtn = new Locator("Confirm Reject Button", By.xpath("//div[contains(@class, 'Modal') or @role='dialog']//button[contains(normalize-space(.), 'Reject') or contains(normalize-space(.), 'Save')]"));
            driverTools.clickWithJavascriptExecutor(confirmRejectBtn);
        }
        driverTools.dummyWait(3);
    }
}