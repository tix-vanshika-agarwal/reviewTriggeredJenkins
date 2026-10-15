package com.tiket.web.test.platform.sphinx;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.FlightApi;
import com.tiket.apiPage.FlightIssuedOrderFactory;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.pojo.flight.FlightDynamicData;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.model.Platform;
import com.tiket.web.WebTest;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelDetailPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.dWeb.page.reschedule.ReschedulePage;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelBookingsRebookPage.HotelBookingsRebookPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelRoomListPage.HotelRoomListPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.flight.reschedule.FlightReschedulePageWeb;
import com.tiket.web.page.flight.rescheduleCart.FlightRescheduleCartWeb;
import com.tiket.web.page.flight.rescheduleReview.FlightRescheduleReviewPageWeb;
import com.tiket.web.page.flight.rescheduleSearchPage.FlightRescheduleSearchPageWeb;
import com.tiket.apiPage.HotelApi;
import com.tiket.web.page.accommodation.hotels.reschedulePage.HotelReschedulePageWeb;
import com.tiket.web.page.accommodation.hotels.roomDetailsPage.RoomDetailsPageWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import com.tiket.apiPage.CorporateApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.page.order.orderlist.OrderListPage.OrderStatus;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.web.dWeb.page.account.AccountPage;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderHistory.OrderHistoryWeb;
import com.tiket.web.dashboard.page.refund.RefundCreation;
import com.tiket.web.page.platform.sphinx.refund.RefundDetailWeb;
import com.tiket.web.page.platform.sphinx.refund.RefundPageWeb;
import com.tiket.web.dashboard.page.b2b_external.EmployeeListPage;
import com.tiket.web.dashboard.page.b2b_external.PurchaseListPage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import javax.security.auth.login.FailedLoginException;
import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class RefundTest extends WebTest {

    @Author(names = {"NaufalF", "Rizki Maulana"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testPastRefundListDataProvider")
    public void testPastRefundList(Map<String, String> param) throws FailedLoginException, InterruptedException {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderHistoryWeb<?> orderHistoryPage = OrderHistoryWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String copySnackbarEn = param.get("copySnackbarEn");
        String copySnackbarId = param.get("copySnackbarId");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Open Your Order Page");
        String langCode = homePage.getPageLanguageCode();
        homePage.openYourOrderViaURL();

        step("Open Order History");
        orderListPage.openOrderHistoryPage();

        step("Open Past Refund List");
        orderHistoryPage.openPastRefundTab();

        step("Scroll to the Refund Card");
        String refundId = orderHistoryPage.getFirstRefundIdByCurrency(currency);
        Assertion.assertThat("Refund ID should not be empty for currency: " + currency, refundId, not(emptyOrNullString()));

        step("Verify Refund Card on the list");
        String refundStatus = orderHistoryPage.getRefundStatusCard(refundId);
        String productName = orderHistoryPage.getProductNameCard(refundId);
        String receiveDate = orderHistoryPage.getReceiveDateCard(refundId);
        String totalRefund = orderHistoryPage.getTotalRefundAmountCard(refundId);
        Assertion.assertThat("Refund status not empty", refundStatus, not(emptyOrNullString()));
        Assertion.assertThat("Refund product name not empty", productName, not(emptyOrNullString()));
        Assertion.assertThat("Refund receive date not empty", receiveDate, not(emptyOrNullString()));

        step("Click on the refund card");
        orderHistoryPage.clickOnRefundCard(refundId);

        step("Verify refund detail");
        int countRefundProgress = refundDetailPage.countRefundProgressTracker();
        String lastProgressStatus = refundDetailPage.getRefundProgressTracker(countRefundProgress-1);
        String receiveDateConverted = CoreFunctions.convertDateFormat(receiveDate, "d MMMM yyyy", "EEE, d MMM yyyy");
        String receiveDateFormatted = CoreFunctions.convertDateLang(receiveDateConverted, "EEE, d MMM yyyy", langCode);
        Assertion.assertThat("Refund detail status same as list", refundDetailPage.getRefundStatus(), containsStringIgnoringCase(refundStatus));
        Assertion.assertThat("Refund progress tracker more than 1", countRefundProgress, greaterThan(1));
        Assertion.assertThat("Last Refund Progress status same as list", lastProgressStatus, containsStringIgnoringCase(refundStatus));
        Assertion.assertThat("Refund receive date is correct", lastProgressStatus, containsStringIgnoringCase(receiveDateFormatted));
        Assertion.assertThat("Refund Id is same as list", refundDetailPage.getRefundId(), is(refundId));
        if (langCode.equals("en")) {
            Assertion.assertThat("Refund Id can be copied", refundDetailPage.copyRefundId(), is(copySnackbarEn));
        } else {
            Assertion.assertThat("Refund Id can be copied", refundDetailPage.copyRefundId(), is(copySnackbarId));
        }

        step("Verify Refund Method and Amount");
        for (String method : refundDetailPage.getRefundMethod()) {
            Assertion.assertThat("Refund method not empty", method, not(emptyOrNullString()));
        }
        Assertion.assertThat("Total refund same as list", refundDetailPage.getTotalRefund(), is(totalRefund));
        refundDetailPage.openRefundBreakdown();
        Assertion.assertThat("Refund amount details summary is correct", refundDetailPage.getTotalRefundAmountDetails(currency), is(CoreFunctions.removeText(totalRefund)));
        Assertion.assertThat("Refund method amount summary is correct", refundDetailPage.getTotalRefundMethodAmount(currency), is(CoreFunctions.removeText(totalRefund)));
        refundDetailPage.closeModal();

        step("Verify product section");
        Assertion.assertThat("Product detail shown", refundDetailPage.getOrderDetailSection(), not(emptyOrNullString()));

        step("Verify back button to order list");
        refundDetailPage.clickBackButton();
        Assertion.assertThat("Current page is order list", orderListPage.getCurrentUrl(), containsString("/yourorder"));
    }

    @DataProvider(name = "testPastRefundListDataProvider")
    public Object[][] testPastRefundListDataProvider() { return getObjectParams("testPastRefundList"); }

    @Author(names = {"NaufalF", "Manan Kumar"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testOngoingRefundListDataProvider")
    public void testOngoingRefundList(Map<String, String> param) throws FailedLoginException, InterruptedException {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String copySnackbarEn = param.get("copySnackbarEn");
        String copySnackbarId = param.get("copySnackbarId");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Open Your Order Page");
        String langCode = homePage.getPageLanguageCode();
        homePage.goToMyOrderPage();

        step("Open Order History");
        orderListPage.openOngoingRefundList();

        step("Scroll to the Refund Card");
        String refundId = orderListPage.getFirstRefundIdByCurrency(currency);

        step("Verify Refund Card on the list");
        String refundStatus = orderListPage.getRefundStatusCard(refundId);
        String productName = orderListPage.getProductNameCard(refundId);
        String receiveDate = orderListPage.getReceiveDateCard(refundId);
        String totalRefund = orderListPage.getTotalRefundAmountCard(refundId);
        Assertion.assertThat("Refund status not empty", refundStatus, not(emptyOrNullString()));
        Assertion.assertThat("Refund product name not empty", productName, not(emptyOrNullString()));
        Assertion.assertThat("Refund receive date not empty", receiveDate, not(emptyOrNullString()));
        Assertion.assertThat("Refund amount contain currency: "+currency.toUpperCase(), totalRefund, containsString(currency));

        step("Click on the refund card");
        orderListPage.clickOnRefundCard(refundId);

        step("Verify refund detail");
        int countRefundProgress = refundDetailPage.countRefundProgressTracker();
        String lastProgressStatus = refundDetailPage.getRefundProgressTracker(countRefundProgress-1);
        String receiveDateConverted = CoreFunctions.convertDateFormat(receiveDate, "d MMMM yyyy", "d MMM yyyy");
        String receiveDateFormatted = CoreFunctions.convertDateLang(receiveDateConverted, "d MMM yyyy", langCode);
        Assertion.assertThat("Refund progress tracker more than 1", countRefundProgress, greaterThan(1));
        Assertion.assertThat("Estimate refund receive date is correct", lastProgressStatus, containsString(receiveDateFormatted));
        Assertion.assertThat("Refund Id is same as list", refundDetailPage.getRefundId(), is(refundId));
        if (langCode.equals("en")) {
            Assertion.assertThat("Refund Id can be copied", refundDetailPage.copyRefundId(), is(copySnackbarEn));
        } else {
            Assertion.assertThat("Refund Id can be copied", refundDetailPage.copyRefundId(), is(copySnackbarId));
        }

        step("Verify Refund Method and Amount");
        for (String method : refundDetailPage.getRefundMethod()) {
            Assertion.assertThat("Refund method not empty", method, not(emptyOrNullString()));
        }
        Assertion.assertThat("Total refund same as list", refundDetailPage.getTotalRefund(), is(totalRefund));
        refundDetailPage.openRefundBreakdown();
        Assertion.assertThat("Refund amount details summary is correct", refundDetailPage.getTotalRefundAmountDetails(currency), is(CoreFunctions.removeText(totalRefund)));
        Assertion.assertThat("Refund method amount summary is correct", refundDetailPage.getTotalRefundMethodAmount(currency), is(CoreFunctions.removeText(totalRefund)));
        refundDetailPage.closeModal();

        step("Verify product section");
        Assertion.assertThat("Product detail shown", refundDetailPage.getOrderDetailSection(), not(emptyOrNullString()));

        step("Verify back button to order list");
        refundDetailPage.clickBackButton();
        Assertion.assertThat("Current page is order list", orderListPage.getCurrentUrl(), containsString("/yourorder")); }

    @DataProvider(name = "testOngoingRefundListDataProvider")
    public Object[][] testOngoingRefundListDataProvider() { return getObjectParams("testOngoingRefundList"); }

    @Author(names = {"NaufalF","Haniah"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testERefundTTDDataProvider")
    public void testERefundTTD(Map<String, String> param) throws FailedLoginException, InterruptedException, ExecutionException {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String currency = param.get("currency");
        String productUrlName = param.get("productUrlName");
        String timeOut = param.get("timeOut");
        int decimal = Integer.parseInt(param.get("decimal"));
        String refundMethod = param.get("refundMethod");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = CoreFunctions.randomName() + " autorder";
        String orderNameTitle = CoreFunctions.getRandomObject(salutationList);
        String guestFullName = CoreFunctions.randomName() + " autguest";
        String guestTitle = CoreFunctions.getRandomObject(salutationList);

        String cardNumber = param.get("cardNumber");
        String lastDigitCard = cardNumber.substring(Math.max(0, cardNumber.length() - 4));
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken,
                username, accountId, "ttd", currency, true, false, OrderStatus.ETICKET_ISSUED);

        step("Fetch Todo order with issued status, else create order");
        String bookedOrderID = orderListPage.getTodoBookedOrderId( orderCreationPage, activeOrderData, username, password,
                currency, productUrlName, true, orderFullName, orderNameTitle, guestFullName, username, guestTitle,
                cardNumber, expiry, cvv, cardName);

        step("Open order list page");
        homePage.goToHomePage();
        String langCode = homePage.getPageLanguageCode();
        homePage.goToMyOrderPage();

        step("Open order detail for the order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();
        JSONObject verticalData = orderDetailPage.getVerticalDataObject(bookedOrderID);

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();

        step("Verify order details section");
        Assertion.assertThat("Order ID on refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(bookedOrderID));
        Assertion.assertThat("Refund progress banner same as refund step", refundPage.getActiveProgress(), is(1));
        String orderDetailRefund = refundPage.getOrderDetail();
        String ticketNameLocal = verticalData.getJSONObject("detailEventConnectMs").getString("ticketNameLocal");
        String ticketNameEn = verticalData.getJSONObject("detailEventConnectMs").getString("ticketNameEn");
        String ticketQty = String.valueOf(verticalData.getJSONObject("orderEventConnectMs").getInt("totalTicketQty"))+" Ticket";
        Assertion.assertThat("TTD product name is correct", orderDetailRefund, containsString(verticalData.getJSONObject("detailEventConnectMs").getString("eventName")));
        Assertion.assertThat("TTD package name is correct", orderDetailRefund, either(containsString(ticketNameEn)).or(containsString(ticketNameLocal)));
        Assertion.assertThat("TTD ticket quantity is correct", orderDetailRefund, containsString(ticketQty));
        Assertion.assertThat("Refund Price Matrix not shown", refundPage.isShownEstimationOtherDate(), is(false));

        step("Select random refund reason");
        String reason = refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear(timeOut);
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotalAndCurrency();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund estimation format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive)+CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();
        refundPage.clickEstimationOtherDates();
        Assertion.assertThat("Estimation other dates count data", refundPage.countProgressEstimationOtherDates(), greaterThanOrEqualTo(1));
        refundPage.closeRefundBreakdown();

        step("Verify refund guide and important info");
        Assertion.assertThat("Refund submission guide card appear", refundPage.isRefundGuideShown(), is(true));
        refundPage.openRefundImportantInfo();
        Assertion.assertThat("Important info modal header", refundPage.getModalHeaderText(), containsString("Important info"));
        Assertion.assertThat("Important info modal content", refundPage.getImportantInfoContent(), not(emptyOrNullString()));
        refundPage.closeRefundBreakdown();

        step("Continue to next page");
        refundPage.clickPrimaryButton("Continue");
        refundPage.waitRefundSecondPageLoaded();
        Assertion.assertThat("Order ID on 2nd refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(bookedOrderID));
        Assertion.assertThat("Refund progress banner same as refund step", refundPage.getActiveProgress(), is(2));

        step("Verify refund reason on 2nd page");
        Assertion.assertThat("Refund reason on 2nd page", refundPage.getReasonWrapper(), containsString(reason));

        step("Verify refund method and amount");
        String refundMethodSection = refundPage.getRefundMethod();
        Assertion.assertThat("Refund method is correct", refundMethodSection, not(emptyOrNullString()));
        Assertion.assertThat("Refund amount is same as prev page", refundTotal, is(refundPage.getRefundTotalAndCurrency()));
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Check Refund TnC");
        refundPage.openTnc();
        Assertion.assertThat("TNC modal header", refundPage.getModalHeaderText(), containsString("Terms & Conditions"));
        Assertion.assertThat("TNC modal content", refundPage.getTncContent(), not(emptyOrNullString()));
        refundPage.closeRefundBreakdown();
        Assertion.assertThat("Submit refund button is disabled", refundPage.isDisabledSubmitButton(), is(true));
        refundPage.clickChecklistTnc();
        refundPage.clickPrimaryButton("Submit a Refund Request");

        step("Submit refund");
        refundPage.clickPrimaryButton("Re-Check");
        refundPage.clickPrimaryButton("Submit a Refund Request");
        refundPage.clickPrimaryButton("Process Refund");
        refundPage.waitForSubmitRefundLoader();
        Assertion.assertThat("Success submit refund figure content", refundPage.getSuccessRefundFigureContent(), containsString("Success submit the request"));

        step("Go to refund detail page");
        refundPage.clickPrimaryButton("View Refund Detail");

        step("Verify Refund Detail");
        int countRefundProgress = refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(), not(emptyOrNullString()));
        Assertion.assertThat("Refund progress tracker more than 1", countRefundProgress, greaterThan(1));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(), not(emptyOrNullString()));
        if (langCode.equals("en")) {
            Assertion.assertThat("Refund Id can be copied", refundDetailPage.copyRefundId(), is("Refund ID has been copied"));
        } else {
            Assertion.assertThat("Refund Id can be copied", refundDetailPage.copyRefundId(), is("Refund ID berhasil disalin"));
        }

        step("Verify Refund Method and Amount");
        for (String method : refundDetailPage.getRefundMethod()) {
            Assertion.assertThat("Refund method not empty", method, not(emptyOrNullString()));
        }
        Assertion.assertThat("Total refund is correct", refundDetailPage.getTotalRefund(), is(refundTotal));
        refundDetailPage.openRefundBreakdown();
        Assertion.assertThat("Refund amount details summary is correct", refundDetailPage.getTotalRefundAmountDetails(currency), is(CoreFunctions.removeText(refundTotal)));
        Assertion.assertThat("Refund method amount summary is correct", refundDetailPage.getTotalRefundMethodAmount(currency), is(CoreFunctions.removeText(refundTotal)));
        refundDetailPage.closeModal();

        step("Verify product section");
        Assertion.assertThat("Product detail shown", refundDetailPage.getOrderDetailSection(), not(emptyOrNullString()));

        step("Verify back button to order list");
        refundDetailPage.clickBackButton();
        sleep(5);
        Assertion.assertThat("Current page is order list", orderListPage.getCurrentUrl(), containsString("tiket.com/yourorder"));
    }

    @DataProvider(name = "testERefundTTDDataProvider")
    public Object[][] testERefundTTDDataProvider() { return getObjectParams("testERefundTTD"); }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testRefundMethodPaidWithCardDataProvider")
    public void testRefundMethodPaidWithCard(Map<String, String> param) throws FailedLoginException, InterruptedException {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String currency = param.get("currency");
        String productUrlName = param.get("productUrlName");
        String timeOut = param.get("timeOut");
        int decimal = Integer.parseInt(param.get("decimal"));
        String refundMethod = param.get("refundMethod");
        String refundMethodOther = Optional.ofNullable(param.get("refundMethodOther")).orElse("");
        String paymentSource = param.get("paymentSource");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = CoreFunctions.randomName() + " autorder";
        String orderNameTitle = CoreFunctions.getRandomObject(salutationList);
        String guestFullName = CoreFunctions.randomName() + " autguest";
        String guestTitle = CoreFunctions.getRandomObject(salutationList);

        String cardNumber = param.get("cardNumber");
        String lastDigitCard = cardNumber.substring(Math.max(0, cardNumber.length() - 4));
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("Login into dweb");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 0);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Check order present in order list with refund method "+refundMethod);
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByPaymentSource(apiAccessToken, username, accountId, paymentSource, currency, true, false, OrderStatus.ETICKET_ISSUED);

        step("Create ToDo order paid using card");
        String bookedOrderID = orderListPage.getTodoBookedOrderId(orderCreationPage, activeOrderData, username, password,
                currency, productUrlName, true, orderFullName, orderNameTitle, guestFullName, username, guestTitle,
                cardNumber, expiry, cvv, cardName);

        step("Open order list page");
        homePage.goToHomePage();
        String langCode = homePage.getPageLanguageCode();
        homePage.goToMyOrderPage();

        step("Open order detail for the order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();
        Assertion.assertThat("Order ID on refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(bookedOrderID));
        Assertion.assertThat("Refund Price Matrix not shown", refundPage.isShownEstimationOtherDate(), is(false));

        step("Select random refund reason");
        String reason = refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear(timeOut);
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotalAndCurrency();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund estimation format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive)+CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Continue to next page");
        refundPage.clickButton("Continue", "Lanjut");
        refundPage.waitRefundSecondPageLoaded();
        Assertion.assertThat("Order ID on 2nd refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(bookedOrderID));

        step("Verify refund method and amount");
        String bankAccountNumber = "";
        String bankAccountName = "";
        String bankName = "";
        if (refundMethod.equalsIgnoreCase("credit")) {
            String refundMethodSection = refundPage.getRefundMethod();
            Assertion.assertThat("Refund method is correct", refundMethodSection, containsString(refundMethod));
            Assertion.assertThat("Card expiry is correct", refundMethodSection, matchesPattern("\\b(0[1-9]|1[0-2])\\/(0[0-9]|[1-9][0-9])\\b"));
        } else {
            Assertion.assertThat("Refund method is "+refundMethodOther, refundPage.getRefundMethod(), containsString(refundMethodOther));

            step("Change refund method to "+refundMethod);
            refundPage.changeRefundMethod(refundMethod);

            bankAccountNumber = refundPage.getRefundBankAccountNumber();
            bankAccountName = refundPage.getRefundBankAccountName();
            bankName = refundPage.getRefundBankName();
            Assertion.assertThat("Refund method bank transfer account number", bankAccountNumber, not(emptyOrNullString()));
            Assertion.assertThat("Refund method bank transfer account name", bankAccountName, not(emptyOrNullString()));
            Assertion.assertThat("Refund method bank transfer account name", bankName, not(emptyOrNullString()));
        }
        refundTotal = refundPage.getRefundTotalAndCurrency();
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();
    }

    @DataProvider(name = "testRefundMethodPaidWithCardDataProvider")
    public Object[][] testRefundMethodPaidWithCardDataProvider() { return getObjectParams("testRefundMethodPaidWithCard"); }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testRefundMethodBankTransferDataProvider")
    public void testRefundMethodBankTransfer(Map<String, String> param) throws InterruptedException {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String productUrlName = param.get("productUrlName");
        String timeOut = param.get("timeOut");
        String firstRefundMethod = param.get("firstRefundMethod");
        String secondRefundMethod = param.get("secondRefundMethod");
        String bankFee = param.get("bankFee");
        int decimal = Integer.parseInt(param.get("decimal"));
        String saveBankOtp = param.get("saveBankOtp");
        boolean isSubmit = Boolean.parseBoolean(param.get("isSubmit"));
        String bankName = Optional.ofNullable(param.get("bankName")).orElse("");
        String accountNumber = Optional.ofNullable(param.get("accountNumber")).orElse("");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = CoreFunctions.randomName() + " autorder";
        String orderNameTitle = CoreFunctions.getRandomObject(salutationList);
        String guestFullName = CoreFunctions.randomName() + " autguest";
        String guestTitle = CoreFunctions.getRandomObject(salutationList);
        String currency = "IDR";

        step("Login into dweb");
        try {
            loginPage.loginIfNotWithSameEmail(username, password, otp, 0);
        } catch (FailedLoginException | InterruptedException e) {
            Assertion.skip("Failed to login. error: "+e.getLocalizedMessage());
        }

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findRefundableOrderByRefundMethod(apiAccessToken, username, accountId, true, "bank_transfer");

        step("Fetch or create ToDo order");
        String bookedOrderID = orderListPage.getTTDBookedOrderId(orderCreationPage, activeOrderData, username, password,
                currency, productUrlName, true, orderFullName, orderNameTitle, guestFullName, username, guestTitle);

        step("Open order list page");
        homePage.goToHomePage();
        String langCode = homePage.getPageLanguageCode();
        homePage.goToMyOrderPage();

        step("Open order detail for the order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();
        refundPage.waitForContentLoaderDisappear();
        Assertion.assertThat("Order ID on refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(bookedOrderID));

        step("Select random refund reason");
        String reason = refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear(timeOut);
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotalAndCurrency();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund estimation format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive)+CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));
        Assertion.assertThat("Refund Fee equal or more than 0", CoreFunctions.removeText(refundFee), lessThanOrEqualTo(CoreFunctions.removeText("0")));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Continue to next page");
        refundPage.clickButton("Continue", "Lanjutkan");
        refundPage.waitRefundSecondPageLoaded();

        step("Verify refund method "+firstRefundMethod+" and amount");
        Assertion.assertThat("Refund method is "+firstRefundMethod, refundPage.getRefundMethod(), containsString(firstRefundMethod));
        Assertion.assertThat("Refund amount is same as prev page", refundPage.getRefundTotalAndCurrency(), is(refundTotal));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Change refund method to "+secondRefundMethod);
        refundPage.changeRefundMethod(secondRefundMethod);
        refundPage.clickEditBankInfo();
        String savedBank = "";
        String inputAccountName = "";

        if (!saveBankOtp.equalsIgnoreCase("0")) {
            step("Input new bank account");
            inputAccountName = refundPage.inputNewBankAccount(bankName, accountNumber, saveBankOtp);
            Assertion.assertThat("Refund method bank account name is correct", refundPage.getRefundBankAccountName(), is(inputAccountName));
        } else {
            step("Select saved bank account");
            savedBank = refundPage.selectSavedBankAccount();
        }

        step("Verify refund method "+secondRefundMethod+" and amount");
        String bankAccountNumber = refundPage.getRefundBankAccountNumber();
        String bankAccountName = refundPage.getRefundBankAccountName();
        String refundBankName = refundPage.getRefundBankName();
        if (!saveBankOtp.equalsIgnoreCase("0")) {
            Assertion.assertThat("Refund method "+secondRefundMethod+" account number", bankAccountNumber, containsString(accountNumber));
            Assertion.assertThat("Refund method "+secondRefundMethod+" account name", bankAccountName, containsString(inputAccountName));
            Assertion.assertThat("Refund method "+secondRefundMethod+" transfer account name", refundBankName, containsString(bankName));
        } else {
            Assertion.assertThat("Refund method "+secondRefundMethod+" account number", savedBank, containsString(bankAccountNumber));
            Assertion.assertThat("Refund method "+secondRefundMethod+" account name", savedBank, containsString(bankAccountName));
            Assertion.assertThat("Refund method "+secondRefundMethod+" transfer account name", savedBank, containsString(refundBankName));
        }
        long refundTotalBank = CoreFunctions.removeText(refundTotal)+CoreFunctions.removeText(bankFee);
        Assertion.assertThat("Refund amount is correct with "+bankFee+" bank fee", CoreFunctions.removeText(refundPage.getRefundTotal()), is(refundTotalBank));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(refundTotalBank)));
        refundPage.closeRefundBreakdown();

        if (isSubmit) {
            step("Submit refund");
            refundPage.clickChecklistTnc();
            refundPage.clickButton("Submit a Refund Request", "Ajukan Permohonan Refund");
            refundPage.clickButton("Process Refund", "Proses Refund");
            refundPage.waitForSubmitRefundLoader();
            Assertion.assertThat("Success submit refund figure content", refundPage.getSuccessRefundFigureContent(), containsString("Success submit the request"));

            step("Go to refund detail page");
            refundPage.clickButton("View Refund Detail", "Lihat Refund Detail");

            step("Verify refund detail refund method and amount");
            for (String method : refundDetailPage.getRefundMethod()) {
                Assertion.assertThat("Refund method is correct", method, containsString(secondRefundMethod));
                Assertion.assertThat("Refund method "+secondRefundMethod+" account number is shown", method, containsString(bankAccountNumber));
                Assertion.assertThat("Refund method "+secondRefundMethod+" account name is shown", method, containsString(bankAccountName));
                Assertion.assertThat("Refund method "+secondRefundMethod+" name is shown", method, containsString(refundBankName));
            }
            Assertion.assertThat("Total refund is correct", CoreFunctions.removeText(refundDetailPage.getTotalRefund()), is(refundTotalBank));
            refundDetailPage.openRefundBreakdown();
            Assertion.assertThat("Refund amount details summary is correct", refundDetailPage.getTotalRefundAmountDetails(currency), is(refundTotalBank));
            Assertion.assertThat("Refund method amount summary is correct", refundDetailPage.getTotalRefundMethodAmount(currency), is(refundTotalBank));
            refundDetailPage.closeModal();

            step("Verify back button to order list");
            refundDetailPage.clickBackButton();
            Assertion.assertThat("Current page is order list", orderListPage.getCurrentUrl(), containsString("tiket.com/yourorder"));
        }
    }

    @DataProvider(name = "testRefundMethodBankTransferDataProvider")
    public Object[][] testRefundMethodBankTransferDataProvider() { return getObjectParams("testRefundMethodBankTransfer"); }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testRefundMethodNonCCPaylaterDataProvider")
    public void testRefundMethodNonCCPaylater(Map<String, String> param) throws InterruptedException {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String productUrlName = param.get("productUrlName");
        String timeOut = param.get("timeOut");
        String firstRefundMethod = param.get("firstRefundMethod");
        String secondRefundMethod = param.get("secondRefundMethod");
        String bankFee = param.get("bankFee");
        int decimal = Integer.parseInt(param.get("decimal"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = CoreFunctions.randomName() + " autorder";
        String orderNameTitle = CoreFunctions.getRandomObject(salutationList);
        String guestFullName = CoreFunctions.randomName() + " autguest";
        String guestTitle = CoreFunctions.getRandomObject(salutationList);
        String currency = "IDR";

        step("Login into dweb");
        try {
            loginPage.loginIfNotWithSameEmail(username, password, otp, 0);
        } catch (FailedLoginException | InterruptedException e) {
            Assertion.skip("Failed to login. error: "+e.getLocalizedMessage());
        }

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findRefundableOrderByRefundMethod(apiAccessToken, username, accountId, true, "blipay");

        step("Create ToDo order paid using card");
        String bookedOrderID = orderListPage.getTTDBookedOrderId( orderCreationPage, activeOrderData, username, password,
                currency, productUrlName, true, orderFullName, orderNameTitle, guestFullName, username, guestTitle);

        step("Open order list page");
        homePage.goToHomePage();
        String langCode = homePage.getPageLanguageCode();
        homePage.goToMyOrderPage();

        step("Open order detail for the order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();
        refundPage.waitForContentLoaderDisappear();
        Assertion.assertThat("Order ID on refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(bookedOrderID));

        step("Select random refund reason");
        String reason = refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear(timeOut);
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotal();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund estimation format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive)+CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));
        Assertion.assertThat("Refund Fee equal or more than 0", CoreFunctions.removeText(refundFee), lessThanOrEqualTo(CoreFunctions.removeText("0")));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Continue to next page");
        refundPage.clickPrimaryButton("Continue");
        refundPage.waitRefundSecondPageLoaded();

        step("Verify refund method "+firstRefundMethod+" and amount");
        Assertion.assertThat("Refund method is "+firstRefundMethod, refundPage.getRefundMethod(), containsString(firstRefundMethod));
        Assertion.assertThat("Refund amount is same as prev page", refundPage.getRefundTotal(), is(refundTotal));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Change refund method to "+secondRefundMethod);
        refundPage.changeRefundMethod(secondRefundMethod);
        refundPage.clickEditBankInfo();
        String savedBank = "";

        step("Select saved bank account");
        savedBank = refundPage.selectSavedBankAccount();

        step("Verify refund method bank transfer");
        String bankAccountNumber = refundPage.getRefundBankAccountNumber();
        String bankAccountName = refundPage.getRefundBankAccountName();
        String bankName = refundPage.getRefundBankName();
        Assertion.assertThat("Refund method bank transfer account number", savedBank, containsString(bankAccountNumber));
        Assertion.assertThat("Refund method bank transfer account name", savedBank, containsString(bankAccountName));
        Assertion.assertThat("Refund method bank transfer account name", savedBank, containsString(bankName));
        long refundTotalBank = CoreFunctions.removeText(refundTotal)+CoreFunctions.removeText(bankFee);
        Assertion.assertThat("Refund amount is correct with "+bankFee+" bank fee", CoreFunctions.removeText(refundPage.getRefundTotal()), is(refundTotalBank));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(refundTotalBank)));
        refundPage.closeRefundBreakdown();

        step("Change refund method back to "+firstRefundMethod);
        refundPage.changeRefundMethod(firstRefundMethod);

        step("Submit refund");
        refundPage.clickChecklistTnc();
        refundPage.clickPrimaryButton("Submit a Refund Request");
        refundPage.clickPrimaryButton("Process Refund");
        refundPage.waitForSubmitRefundLoader();
        Assertion.assertThat("Success submit refund figure content", refundPage.getSuccessRefundFigureContent(), containsString("Success submit the request"));

        step("Go to refund detail page");
        refundPage.clickPrimaryButton("View Refund Detail");

        step("Verify refund detail refund method and amount");
        for (String method : refundDetailPage.getRefundMethod()) {
            Assertion.assertThat("Refund method is correct", method, containsString(firstRefundMethod));
        }
        Assertion.assertThat("Total refund is correct", refundDetailPage.getTotalRefund(), is(refundTotal));

        step("Verify refund amount breakdown");
        refundDetailPage.openRefundBreakdown();
        Assertion.assertThat("Refund amount details summary is correct", refundDetailPage.getTotalRefundAmountDetails(currency), is(CoreFunctions.removeText(refundTotal)));
        Assertion.assertThat("Refund method amount summary is correct", refundDetailPage.getTotalRefundMethodAmount(currency), is(CoreFunctions.removeText(refundTotal)));
        refundDetailPage.closeModal();

        step("Verify back button to order list");
        refundDetailPage.clickBackButton();
        Assertion.assertThat("Current page is order list", orderListPage.getCurrentUrl(), containsString("tiket.com/yourorder"));
    }

    @DataProvider(name = "testRefundMethodNonCCPaylaterDataProvider")
    public Object[][] testRefundMethodNonCCPaylaterDataProvider() { return getObjectParams("testRefundMethodNonCCPaylater"); }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testRefundMethodPaylaterDataProvider")
    public void testRefundMethodPaylater(Map<String, String> param) throws InterruptedException {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String accountId = param.get("accountId");
        String timeOut = param.get("timeOut");
        String paymentSource = param.get("paymentSource");
        String refundMethod = param.get("refundMethod");
        String currency = "IDR";

        step("Login into dweb");
        try {
            loginPage.loginIfNotWithSameEmail(username, password, otp, 0);
        } catch (FailedLoginException | InterruptedException e) {
            Assertion.skip("Failed to login. error: "+e.getLocalizedMessage());
        }

        step("get access token via API");
        String accessToken = "Bearer "+accountPage.getAccessTokenUNM(username, password);

        step("Check order present in order list or else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByPaymentSource(accessToken, username, accountId, paymentSource, currency, true, false, OrderStatus.ETICKET_ISSUED);
        if (activeOrderData == null) {
            Assertion.skip("can't find any order id with payment source: "+refundMethod.toLowerCase()+". please create the order manually");
        }

        step("Open order list page");
        homePage.goToHomePage();
        homePage.goToMyOrderPage();

        step("Open order detail for the order");
        orderListPage.openActiveTicketViaURL(activeOrderData.orderId);
        orderDetailPage.skipCoachmark();

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();
        refundPage.waitForContentLoaderDisappear();
        Assertion.assertThat("Order ID on refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(activeOrderData.orderId));

        step("Select random refund reason");
        String reason = refundPage.selectRandomRefundReason();
        refundPage.waitForEstimationAmountAppear(timeOut);
        String refundTotal = refundPage.getRefundTotal();

        step("Continue to next page");
        refundPage.clickPrimaryButton("Continue");
        refundPage.waitRefundSecondPageLoaded();

        step("Verify refund method "+refundMethod+" and amount");
        Assertion.assertThat("Refund method is "+refundMethod, refundPage.getRefundMethod(), containsString(refundMethod));
        Assertion.assertThat("Refund amount is same as prev page", refundPage.getRefundTotal(), is(refundTotal));

        step("Verify refund breakdown");
        refundPage.openRefundBreakdown();
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        Assertion.assertThat("Refund method breakdown contains "+refundMethod, refundPage.getRefundMethodOnBreakdown(), containsString(refundMethod));
        refundPage.closeRefundBreakdown();
    }

    @DataProvider(name = "testRefundMethodPaylaterDataProvider")
    public Object[][] testRefundMethodPaylaterDataProvider() { return getObjectParams("testRefundMethodPaylater"); }

    @Author(names = {"Daffa","Haniah"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void testERefundCarRental() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("testERefundCarRental");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String currency = param.get("currency");
        String timeOut = param.get("timeOut");
        int decimal = Integer.parseInt(param.get("decimal"));

        String phoneNumber = param.get("phoneNumber");
        String driverOption = param.get("driverOption");
        String location = param.get("location");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String type = param.get("type");
        int rentalDuration = Integer.parseInt(param.get("rentalDuration"));
        String pickupTestData = param.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = CoreFunctions.randomName() + " autorder";
        String orderNameTitle = CoreFunctions.getRandomObject(salutationList);
        String adminName = param.get("adminName");
        String adminPhoneNumber = param.get("adminPhone");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken,
                username, accountId, "car", currency, true, false, OrderStatus.ETICKET_ISSUED);

        step("Fetch Car Rental order with issued status, else create order");
        String bookedOrderID = orderListPage.getCarRentalBookedOrderId( orderCreationPage, activeOrderData, username, phoneNumber, password,
                driverOption, location, startDate, pickupTime, type, orderFullName, orderNameTitle, adminName, adminPhoneNumber, rentalDuration);

        step("Open order list page");
        homePage.goToHomePage();
        String langCode = homePage.getPageLanguageCode();
        homePage.goToMyOrderPage();

        step("Open order detail for the order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();

        step("Verify order details section");
        Assertion.assertThat("Order ID on refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(bookedOrderID));
        Assertion.assertThat("Refund progress banner same as refund step", refundPage.getActiveProgress(), is(1));
        String orderDetailRefund = refundPage.getOrderDetail();
        String carRentalName = orderDetailPage.getVerticalData(bookedOrderID).get("fleetName");
        String carRentalPickUpTime = orderDetailPage.getVerticalData(bookedOrderID).get("pickupTime");
        String carRentalDropOffTime = orderDetailPage.getVerticalData(bookedOrderID).get("dropOffTime");
        String pickUpLocation = orderDetailPage.getVerticalData(bookedOrderID).get("regionalName");
        Assertion.assertThat("Car Rental product name is correct", orderDetailRefund, containsString(carRentalName));
        Assertion.assertThat("Car Rental pick up time is correct", orderDetailRefund, containsString(carRentalPickUpTime));
        Assertion.assertThat("Car Rental drop off time is correct", orderDetailRefund, containsString(carRentalDropOffTime));
        Assertion.assertThat("Car Rental regional name is correct", orderDetailRefund, containsString(pickUpLocation));

        step("Select random refund reason");
        String reason = refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear(timeOut);
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotalAndCurrency();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund estimation format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive)+CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();
        refundPage.clickEstimationOtherDates();
        Assertion.assertThat("Estimation other dates count data", refundPage.countProgressEstimationOtherDates(), greaterThanOrEqualTo(1));
        refundPage.closeRefundBreakdown();

        step("Verify refund guide and important info");
        Assertion.assertThat("Refund submission guide card appear", refundPage.isRefundGuideShown(), is(true));
        refundPage.openRefundImportantInfo();
        Assertion.assertThat("Important info modal header", refundPage.getModalHeaderText(), containsString("Important info"));
        Assertion.assertThat("Important info modal content", refundPage.getImportantInfoContent(), not(emptyOrNullString()));
        refundPage.closeRefundBreakdown();

        step("Continue to next page");
        refundPage.clickButton("Continue", "Lanjutkan");
        refundPage.waitRefundSecondPageLoaded();
        Assertion.assertThat("Order ID on 2nd refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(bookedOrderID));
        Assertion.assertThat("Refund progress banner same as refund step", refundPage.getActiveProgress(), is(2));

        step("Verify refund reason on 2nd page");
        Assertion.assertThat("Refund reason on 2nd page", refundPage.getReasonWrapper(), containsString(reason));

        step("Verify refund method and amount");
        String refundMethodSection = refundPage.getRefundMethod();
        Assertion.assertThat("Refund method is correct", refundMethodSection, not(emptyOrNullString()));
        Assertion.assertThat("Refund amount is same as prev page", refundTotal, is(refundPage.getRefundTotalAndCurrency()));
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Check Refund TnC");
        refundPage.openTnc();
        Assertion.assertThat("TNC modal header", refundPage.getModalHeaderText(), containsString("Terms & Conditions"));
        Assertion.assertThat("TNC modal content", refundPage.getTncContent(), not(emptyOrNullString()));
        refundPage.closeRefundBreakdown();
        Assertion.assertThat("Submit refund button is disabled", refundPage.isDisabledSubmitButton(), is(true));
        refundPage.clickChecklistTnc();
        refundPage.clickPrimaryButton("Submit a Refund Request");

        step("Submit refund");
        refundPage.clickPrimaryButton("Re-Check");
        refundPage.clickPrimaryButton("Submit a Refund Request");
        refundPage.clickPrimaryButton("Process Refund");
        refundPage.waitForSubmitRefundLoader();
        Assertion.assertThat("Success submit refund figure content", refundPage.getSuccessRefundFigureContent(), containsString("Success submit the request"));

        step("Go to refund detail page");
        refundPage.clickPrimaryButton("View Refund Detail");

        step("Verify Refund Detail");
        int countRefundProgress = refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(), not(emptyOrNullString()));
        Assertion.assertThat("Refund progress tracker more than 1", countRefundProgress, greaterThan(1));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(), not(emptyOrNullString()));
        if (langCode.equals("en")) {
            Assertion.assertThat("Refund Id can be copied", refundDetailPage.copyRefundId(), is("Refund ID has been copied"));
        } else {
            Assertion.assertThat("Refund Id can be copied", refundDetailPage.copyRefundId(), is("Refund ID berhasil disalin"));
        }

        step("Verify Refund Method and Amount");
        for (String method : refundDetailPage.getRefundMethod()) {
            Assertion.assertThat("Refund method not empty", method, not(emptyOrNullString()));
        }
        Assertion.assertThat("Total refund is correct", refundDetailPage.getTotalRefund(), is(refundTotal));
        refundDetailPage.openRefundBreakdown();
        Assertion.assertThat("Refund amount details summary is correct", refundDetailPage.getTotalRefundAmountDetails(currency), is(CoreFunctions.removeText(refundTotal)));
        Assertion.assertThat("Refund method amount summary is correct", refundDetailPage.getTotalRefundMethodAmount(currency), is(CoreFunctions.removeText(refundTotal)));
        refundDetailPage.closeModal();

        step("Verify product section");
        Assertion.assertThat("Product detail shown", refundDetailPage.getOrderDetailSection(), not(emptyOrNullString()));

        step("Verify back button to order list");
        refundDetailPage.clickBackButton();
        sleep(5);
        Assertion.assertThat("Current page is order list", orderListPage.getCurrentUrl(), containsString("tiket.com/yourorder"));
    }

    @Author(names = {"Daffa","Haniah"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testERefundHotelNonIDR() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("testERefundHotelNonIDR");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String otp = param.get("otp");
        String timeOut = param.get("timeOut");
        int decimal = Integer.parseInt(param.get("decimal"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        String currency = param.get("currency");
        String cardNumber = param.get("cardNumber");
        String lastDigitCard = cardNumber.substring(Math.max(0, cardNumber.length() - 4));
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken,
                username, accountId, "hotel", currency, true, false, OrderStatus.ETICKET_ISSUED);

        step("Fetch Hotel order with issued status, else create order");
        String bookedOrderID = orderListPage.getHotelBookedOrderIdCard(orderCreationPage, activeOrderData, username, password, startDate, hotelName, adult, night, room,
                currency, orderFullName, orderNameTitle, guestFullName, guestTitle, cardNumber, expiry, cvv, cardName);

        step("Open order list page");
        homePage.goToHomePage();
        String langCode = homePage.getPageLanguageCode();
        homePage.goToMyOrderPage();

        step("Open order detail for the order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();
        refundPage.skipRescheduleNudgeIfShown();

        step("Verify order details section");
        Assertion.assertThat("Order ID on refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(bookedOrderID));
        Assertion.assertThat("Refund progress banner same as refund step", refundPage.getActiveProgress(), is(1));
        String orderDetailRefund = refundPage.getOrderDetail();
        String hotelRoom = orderDetailPage.getVerticalData(bookedOrderID).get("roomName");
        String hotelTotalRoom = orderDetailPage.getVerticalData(bookedOrderID).get("bookingRoom");
        String hotelTotalNight = orderDetailPage.getVerticalData(bookedOrderID).get("bookingNight");
        Assertion.assertThat("Hotel room name is correct", orderDetailRefund, containsString(hotelRoom));
        Assertion.assertThat("Hotel total room is correct", orderDetailRefund, containsString(hotelTotalRoom));
        Assertion.assertThat("Hotel total night is correct", orderDetailRefund, containsString(hotelTotalNight));

        step("Select random refund reason");
        String reason = refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear(timeOut);
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotalAndCurrency();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund estimation format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive)+CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();
        refundPage.clickEstimationOtherDates();
        Assertion.assertThat("Estimation other dates count data", refundPage.countProgressEstimationOtherDates(), greaterThanOrEqualTo(1));
        refundPage.closeRefundBreakdown();

        step("Verify refund guide and important info");
        Assertion.assertThat("Refund submission guide card appear", refundPage.isRefundGuideShown(), is(true));
        refundPage.openRefundImportantInfo();
        Assertion.assertThat("Important info modal header", refundPage.getModalHeaderText(), containsString("Important info"));
        Assertion.assertThat("Important info modal content", refundPage.getImportantInfoContent(), not(emptyOrNullString()));
        refundPage.closeRefundBreakdown();

        step("Continue to next page");
        refundPage.clickButton("Continue", "Lanjutkan");
        refundPage.waitRefundSecondPageLoaded();
        Assertion.assertThat("Order ID on 2nd refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(bookedOrderID));
        Assertion.assertThat("Refund progress banner same as refund step", refundPage.getActiveProgress(), is(2));

        step("Verify refund reason on 2nd page");
        Assertion.assertThat("Refund reason on 2nd page", refundPage.getReasonWrapper(), containsString(reason));

        step("Verify refund method and amount");
        String refundMethodSection = refundPage.getRefundMethod();
        Assertion.assertThat("Refund method is correct", refundMethodSection, not(emptyOrNullString()));
        Assertion.assertThat("Refund amount is same as prev page", refundTotal, is(refundPage.getRefundTotalAndCurrency()));
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Check Refund TnC");
        refundPage.openTnc();
        Assertion.assertThat("TNC modal header", refundPage.getModalHeaderText(), containsString("Terms & Conditions"));
        Assertion.assertThat("TNC modal content", refundPage.getTncContent(), not(emptyOrNullString()));
        refundPage.closeRefundBreakdown();
        Assertion.assertThat("Submit refund button is disabled", refundPage.isDisabledSubmitButton(), is(true));
        refundPage.clickChecklistTnc();
        refundPage.clickPrimaryButton("Submit a Refund Request");

        step("Submit refund");
        refundPage.clickPrimaryButton("Re-Check");
        refundPage.clickPrimaryButton("Submit a Refund Request");
        refundPage.clickPrimaryButton("Process Refund");
        refundPage.waitForSubmitRefundLoader();
        Assertion.assertThat("Success submit refund figure content", refundPage.getSuccessRefundFigureContent(), containsString("Success submit the request"));

        step("Go to refund detail page");
        refundPage.clickPrimaryButton("View Refund Detail");

        step("Verify Refund Detail");
        int countRefundProgress = refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(), not(emptyOrNullString()));
        Assertion.assertThat("Refund progress tracker more than 1", countRefundProgress, greaterThan(1));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(), not(emptyOrNullString()));
        if (langCode.equals("en")) {
            Assertion.assertThat("Refund Id can be copied", refundDetailPage.copyRefundId(), is("Refund ID has been copied"));
        } else {
            Assertion.assertThat("Refund Id can be copied", refundDetailPage.copyRefundId(), is("Refund ID berhasil disalin"));
        }

        step("Verify Refund Method and Amount");
        for (String method : refundDetailPage.getRefundMethod()) {
            Assertion.assertThat("Refund method not empty", method, not(emptyOrNullString()));
        }
        Assertion.assertThat("Total refund is correct", refundDetailPage.getTotalRefund(), is(refundTotal));
        refundDetailPage.openRefundBreakdown();
        Assertion.assertThat("Refund amount details summary is correct", refundDetailPage.getTotalRefundAmountDetails(currency), is(CoreFunctions.removeText(refundTotal)));
        Assertion.assertThat("Refund method amount summary is correct", refundDetailPage.getTotalRefundMethodAmount(currency), is(CoreFunctions.removeText(refundTotal)));
        refundDetailPage.closeModal();

        step("Verify product section");
        Assertion.assertThat("Product detail shown", refundDetailPage.getOrderDetailSection(), not(emptyOrNullString()));

        step("Verify back button to order list");
        refundDetailPage.clickBackButton();
        sleep(5);
        Assertion.assertThat("Current page is order list", orderListPage.getCurrentUrl(), containsString("tiket.com/yourorder"));
    }

    @Author(names = {"Daffa"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testERefundMultiOrderHotelPartial() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("testERefundMultiOrderHotelPartial");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int qtyEachRoom = Integer.parseInt(param.get("qtyEachRoom"));

        String otp = param.get("otp");
        String timeOut = param.get("timeOut");
        int decimal = Integer.parseInt(param.get("decimal"));
        String currency = param.get("currency");

        step("Login into dweb");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 0);
        loginPage.skipUNMPageAfterLogin(timeOut);

        step("Create Multi-Order Hotel with types of room paid using " + currency + " currency");
        String bookedOrderID = orderCreationPage.createOrderHotelMultiRoomApi(
                username,password,hotelName,adult,night,room,startDate,qtyEachRoom,currency);

        step("Open order list page");
        homePage.goToHomePage();
        String langCode = homePage.getPageLanguageCode();
        homePage.goToMyOrderPage();

        step("Open order detail for the order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        orderDetailPage.clickRefundMultiOrderRoom();
        if(orderDetailPage.isPopUpNonRefundableShown()){
            Assertion.skip("Room not refundable");
        }
        refundPage.waitForContentLoaderDisappear();
        refundPage.skipRescheduleNudgeIfShown();

        step("Verify order details section");
        Assertion.assertThat("Order ID on refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(bookedOrderID));
        Assertion.assertThat("Refund progress banner same as refund step", refundPage.getActiveProgress(), is(1));
        String orderDetailRefund = refundPage.getOrderDetail();
        String hotelTotalRoom = orderDetailPage.getVerticalData(bookedOrderID).get("bookingRoom");
        String hotelTotalNight = orderDetailPage.getVerticalData(bookedOrderID).get("bookingNight");
        Assertion.assertThat("Hotel total room is correct", orderDetailRefund, containsString(hotelTotalRoom));
        Assertion.assertThat("Hotel total night is correct", orderDetailRefund, containsString(hotelTotalNight));

        step("Select random refund reason");
        String reason = refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear(timeOut);
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotal();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund estimation format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive)+CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();
        refundPage.clickEstimationOtherDates();
        Assertion.assertThat("Estimation other dates count data", refundPage.countProgressEstimationOtherDates(), greaterThanOrEqualTo(1));
        refundPage.closeRefundBreakdown();

        step("Verify refund guide");
        Assertion.assertThat("Refund submission guide card appear", refundPage.isRefundGuideShown(), is(true));
        refundPage.closeRefundBreakdown();

        step("Continue to next page");
        refundPage.clickButton("Continue", "Lanjutkan");
        refundPage.waitRefundSecondPageLoaded();
        Assertion.assertThat("Order ID on 2nd refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(bookedOrderID));
        Assertion.assertThat("Refund progress banner same as refund step", refundPage.getActiveProgress(), is(2));

        step("Verify refund reason on 2nd page");
        Assertion.assertThat("Refund reason on 2nd page", refundPage.getReasonWrapper(), containsString(reason));

        step("Verify refund method and amount");
        String refundMethodSection = refundPage.getRefundMethod();
        Assertion.assertThat("Refund method is correct", refundMethodSection, not(emptyOrNullString()));
        Assertion.assertThat("Refund amount is same as prev page", refundTotal, is(refundPage.getRefundTotal()));
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund estimation breakdown format is correct", CoreFunctions.verifySeparatorRegex(amounts, langCode, "-?"+currency, decimal), is(true));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Check Refund TnC");
        refundPage.openTnc();
        Assertion.assertThat("TNC modal header", refundPage.getModalHeaderText(), containsString("Terms & Conditions"));
        Assertion.assertThat("TNC modal content", refundPage.getTncContent(), not(emptyOrNullString()));
        refundPage.closeRefundBreakdown();
        Assertion.assertThat("Submit refund button is disabled", refundPage.isDisabledSubmitButton(), is(true));
        refundPage.clickChecklistTnc();
        refundPage.clickPrimaryButton("Submit a Refund Request");

        step("Submit refund");
        refundPage.clickPrimaryButton("Re-Check");
        refundPage.clickPrimaryButton("Submit a Refund Request");
        refundPage.clickPrimaryButton("Process Refund");
        refundPage.waitForSubmitRefundLoader();
        Assertion.assertThat("Success submit refund figure content", refundPage.getSuccessRefundFigureContent(), containsString("Success submit the request"));

        step("Go to refund detail page");
        refundPage.clickPrimaryButton("View Refund Detail");

        step("Verify Refund Detail");
        int countRefundProgress = refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(), not(emptyOrNullString()));
        Assertion.assertThat("Refund progress tracker more than 1", countRefundProgress, greaterThan(1));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(), not(emptyOrNullString()));
        if (langCode.equals("en")) {
            Assertion.assertThat("Refund Id can be copied", refundDetailPage.copyRefundId(), is("Refund ID has been copied"));
        } else {
            Assertion.assertThat("Refund Id can be copied", refundDetailPage.copyRefundId(), is("Refund ID berhasil disalin"));
        }

        step("Verify Refund Method and Amount");
        for (String method : refundDetailPage.getRefundMethod()) {
            Assertion.assertThat("Refund method not empty", method, not(emptyOrNullString()));
        }
        Assertion.assertThat("Total refund is correct", refundDetailPage.getTotalRefund(), is(refundTotal));
        refundDetailPage.openRefundBreakdown();
        Assertion.assertThat("Refund amount details summary is correct", refundDetailPage.getTotalRefundAmountDetails(currency), is(CoreFunctions.removeText(refundTotal)));
        Assertion.assertThat("Refund method amount summary is correct", refundDetailPage.getTotalRefundMethodAmount(currency), is(CoreFunctions.removeText(refundTotal)));
        refundDetailPage.closeModal();

        step("Verify product section");
        Assertion.assertThat("Product detail shown", refundDetailPage.getOrderDetailSection(), not(emptyOrNullString()));

        step("Verify back button to order list");
        refundDetailPage.clickBackButton();
        Assertion.assertThat("Current page is order list", orderListPage.getCurrentUrl(), containsString("tiket.com/yourorder"));
    }

    @Author(names = {"Ishika"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testRefundMethodBlipayDataProvider")
    public void testRefundMethodBlipay(Map<String, String> param) throws FailedLoginException, InterruptedException, ExecutionException {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String timeOut = param.get("timeOut");
        String paymentSource = param.get("paymentSource");
        String refundMethod = param.get("refundMethod");
        String currency = "IDR";
        String productUrlName = param.get("productUrlName");
        String[] salutationList = {"Mr", "Mrs", "Ms"} ;
        String orderFullName = CoreFunctions.randomName() + " autorder";
        String orderNameTitle = CoreFunctions.getRandomObject(salutationList);
        String guestFullName = CoreFunctions.randomName() + " autguest";
        String guestTitle = CoreFunctions.getRandomObject(salutationList);

        step("Login into dweb");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 0);
        loginPage.skipUNMPageAfterLogin(timeOut);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findRefundableOrderByRefundMethod(apiAccessToken, username, accountId, true, "blipay");

        step("Fetch or create ToDo order");
        String bookedOrderID = orderListPage.getTTDBookedOrderId(orderCreationPage, activeOrderData, username, password,
                currency, productUrlName, true, orderFullName, orderNameTitle, guestFullName, username, guestTitle);

        step("Open order list page");
        homePage.goToHomePage();
        homePage.goToMyOrderPage();

        step("Open order detail for the order");
        orderListPage.openActiveTicketViaURL(activeOrderData.orderId);
        orderDetailPage.skipCoachmark();

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();
        refundPage.waitForContentLoaderDisappear();
        Assertion.assertThat("Order ID on refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(activeOrderData.orderId));

        step("Select random refund reason");
        String reason = refundPage.selectRandomRefundReason();
        refundPage.waitForEstimationAmountAppear(timeOut);
        String refundTotal = refundPage.getRefundTotal();

        step("Continue to next page");
        refundPage.clickPrimaryButton("Continue");
        refundPage.waitRefundSecondPageLoaded();

        step("Verify refund method "+refundMethod+" and amount");
        Assertion.assertThat("Refund method is "+refundMethod, refundPage.getRefundMethod(), containsString(refundMethod));
        Assertion.assertThat("Refund amount is same as prev page", refundPage.getRefundTotal(), is(refundTotal));

        step("Verify refund breakdown");
        refundPage.openRefundBreakdown();
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        Assertion.assertThat("Refund method breakdown contains "+refundMethod, refundPage.getRefundMethodOnBreakdown(), containsString(refundMethod));
        refundPage.closeRefundBreakdown();
    }

    @DataProvider(name = "testRefundMethodBlipayDataProvider")
    public Object[][] testRefundMethodBlipayDataProvider() { return getObjectParams("testRefundMethodBlipay"); }

    @Author(names = {"Ishika"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testRefundMethodShopeepayDataProvider")
    public void testRefundMethodShopeepay(Map<String, String> param) throws FailedLoginException, InterruptedException, ExecutionException {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String timeOut = param.get("timeOut");
        String paymentSource = param.get("paymentSource");
        String refundMethod = param.get("refundMethod");
        String currency = "IDR";
        String productUrlName = param.get("productUrlName");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = CoreFunctions.randomName() + " autorder";
        String orderNameTitle = CoreFunctions.getRandomObject(salutationList);
        String guestFullName = CoreFunctions.randomName() + " autguest";
        String guestTitle = CoreFunctions.getRandomObject(salutationList);

        step("Login into dweb");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 0);
        loginPage.skipUNMPageAfterLogin(timeOut);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken,
                username, accountId, "ttd", currency, true, false, OrderStatus.ETICKET_ISSUED);

        step("Fetch or create ToDo order");
        String bookedOrderID = orderListPage.getTTDBookedOrderId(orderCreationPage, activeOrderData, username, password,
                currency, productUrlName, true, orderFullName, orderNameTitle, guestFullName, username, guestTitle);

        step("Open order list page");
        homePage.goToHomePage();
        homePage.goToMyOrderPage();

        step("Open order detail for the order");
        orderListPage.openActiveTicketViaURL(activeOrderData.orderId);
        orderDetailPage.skipCoachmark();

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();
        refundPage.waitForContentLoaderDisappear();
        Assertion.assertThat("Order ID on refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(activeOrderData.orderId));

        step("Select random refund reason");
        String reason = refundPage.selectRandomRefundReason();
        refundPage.waitForEstimationAmountAppear(timeOut);
        String refundTotal = refundPage.getRefundTotal();

        step("Continue to next page");
        refundPage.clickPrimaryButton("Continue");
        refundPage.waitRefundSecondPageLoaded();

        step("Verify refund method "+refundMethod+" and amount");
        Assertion.assertThat("Refund method is "+refundMethod, refundPage.getRefundMethod(), containsString(refundMethod));
        Assertion.assertThat("Refund amount is same as prev page", refundPage.getRefundTotal(), is(refundTotal));

        step("Verify refund breakdown");
        refundPage.openRefundBreakdown();
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        Assertion.assertThat("Refund method breakdown contains "+refundMethod, refundPage.getRefundMethodOnBreakdown(), containsString(refundMethod));
        refundPage.closeRefundBreakdown();
    }

    @DataProvider(name = "testRefundMethodShopeepayDataProvider")
    public Object[][] testRefundMethodShopeepayDataProvider() { return getObjectParams("testRefundMethodShopeepay"); }

    @Author(names = {"Yoan"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyRefundMethodCorporateBillingDataProvider")
    public void verifyRefundMethodCorporateBilling(Map<String, String> param) throws FailedLoginException, InterruptedException, ExecutionException {

        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        PurchaseListPage<?> purchaseListPage = new PurchaseListPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userTraveler = param.get("userTraveler");
        String passTraveler = param.get("passTraveler");
        String userApprover = param.get("userApprover");
        String passApprover = param.get("passApprover");
        String refundMethod = param.get("refundMethod");
        String otp = param.get("otp");
        String timeOut = param.get("timeOut");
        String currency = param.get("currency");

        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String query = param.get("hotelName");
        String[] salutationList = { "Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);
        int sleep = Integer.parseInt(param.get("sleep"));

        step("Get api access token");
        String apiAccessToken = "Bearer "+accountPage.getAccessTokenUNM(userTraveler, passTraveler);
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken.split(" ",2)[1]));

        step("Check order present in order list");
        String orderId = "";
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken, userTraveler, accountId, "hotel", currency, true, false, OrderStatus.ETICKET_ISSUED);

        if(activeOrderData == null){
            step("Create new B2B Corp hotel issued order");
            orderCreationPage.createIssuedHotelOrderApiB2BCorp(userTraveler, passTraveler, startDate, query, 1, 1, 1, orderFullName, orderNameTitle, guestFullName, guestTitle, employeeListPage.getEmployeeData(userTraveler), true, currency);
            orderId = String.valueOf(orderCreationPage.getOrderId());
            sleep(sleep);

            step("Approve booking by approver account for Order ID: " + orderId);
            Boolean bookingStatus = purchaseListPage.approveBookingB2BApi(userApprover, passApprover, orderId);
            Assertion.assertThat("Booking approved by B2B approver", bookingStatus, is(true));
        } else {
            orderId = activeOrderData.orderId;
        }

        step("Login to DWEB");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 0);
        loginPage.skipUNMPageAfterLogin(timeOut);

        step("Open order list page");
        homePage.goToHomePage();
        homePage.goToMyOrderPage();

        step("Open order detail for the order");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();
        refundPage.waitForContentLoaderDisappear();
        Assertion.assertThat("Order ID on refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(orderId));

        step("Select random refund reason");
        String reason = refundPage.selectRandomRefundReason();

        step("Continue to the next page");
        refundPage.clickPrimaryButton("Continue");
        refundPage.waitRefundSecondPageLoaded();

        step("Verify refund method "+refundMethod);
        Assertion.assertThat("Refund method is "+refundMethod, refundPage.getRefundMethod(), containsString(refundMethod));

    }
    @DataProvider(name = "verifyRefundMethodCorporateBillingDataProvider")
    public Object[][] verifyRefundMethodCorporateBillingDataProvider() { return getObjectParams("verifyRefundMethodCorporateBilling");}

    @Author(names = {"NaufalF"})
    @Module(name = "Refund")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testRefundDetailsOpenFromRefundList() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testRefundDetailsOpenFromRefundList");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListWeb = OrderListWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailWeb = RefundDetailWeb.getInstance(session);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String currency = param.get("currency");

        step("Login to Tiket");
        loginPageWeb.loginIfNotWithSameEmail(username, password, otp, 0);

        step("Open your order page");
        homePageWeb.goToMyOrderPage();

        step("Open ongoing refund list");
        orderListWeb.openOngoingRefundList();

        step("Open the refund details");
        String refundId = orderListWeb.getFirstRefundIdByCurrency(currency);
        String totalRefund = orderListWeb.getTotalRefundAmountCard(refundId);
        orderListWeb.clickOnRefundCard(refundId);

        if (!Configs.PLATFORM.equals(Platform.DWEB)) {
            step("Open and close vertical details popup");
            refundDetailWeb.openVerticalDetails();
            Assertion.assertThat("Vertical details popup is opened", refundDetailWeb.isVerticalDetailsOpened(), is(true));
            refundDetailWeb.closeModal();
        }

        step("Open and close refund amount breakdown popup");
        refundDetailWeb.openRefundBreakdown();
        Assertion.assertThat("Refund amount breakdown is correct", refundDetailWeb.getTotalRefundAmountDetails(currency), is(CoreFunctions.removeText(totalRefund)));
        refundDetailWeb.closeModal();

        step("Back to refund list page");
        refundDetailWeb.clickBackButton();
        Assertion.assertThat("Current page is order list", orderListWeb.getCurrentUrl(), containsString("tiket.com/yourorder"));
    }

    @Author(names = {"Haniah"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void testRefundRescheduleNudgesNotHaveReschedule() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testRefundRescheduleNudgesNotHaveReschedule");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String currency = param.get("currency");
        String timeOut = param.get("timeOut");

        String phoneNumber = param.get("phoneNumber");
        String driverOption = param.get("driverOption");
        String location = param.get("location");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String type = param.get("type");
        int rentalDuration = Integer.parseInt(param.get("rentalDuration"));
        String pickupTestData = param.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = CoreFunctions.randomName() + " autorder";
        String orderNameTitle = CoreFunctions.getRandomObject(salutationList);
        String adminName = param.get("adminName");
        String adminPhoneNumber = param.get("adminPhone");

        step("Login into web");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 0);
        loginPage.skipUNMPageAfterLogin(timeOut);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken,
                username, accountId, "car", currency, true, false, OrderStatus.ETICKET_ISSUED);

        step("Fetch Car Rental order with issued status, else create order");
        String bookedOrderID = orderListPage.getCarRentalBookedOrderId( orderCreationPage, activeOrderData, username, phoneNumber, password,
                driverOption, location, startDate, pickupTime, type, orderFullName, orderNameTitle, adminName, adminPhoneNumber, rentalDuration);

        step("Open order list page");
        homePage.goToHomePage();
        homePage.goToMyOrderPage();

        step("Open order detail for the order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();

        step("Verify that reschedule nudges is not showing");
        Assertion.assertThat("Reschedule nudges not showing", refundPage.isNudgeShow(), is(false));
    }

    @Author(names = {"Haniah"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void testRefundRescheduleNudgesReschedulableTrue() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testRefundRescheduleNudgesReschedulableTrue");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String otp = param.get("otp");
        String timeOut = param.get("timeOut");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        String currency = param.get("currency");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("Login into dweb");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 0);
        loginPage.skipUNMPageAfterLogin(timeOut);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken,
                username, accountId, "hotel", currency, true, false, OrderStatus.ETICKET_ISSUED);

        step("Fetch Hotel order with issued status, else create order");
        String bookedOrderID = orderListPage.getHotelBookedOrderIdCard(orderCreationPage, activeOrderData, username, password, startDate, hotelName, adult, night, room,
                currency, orderFullName, orderNameTitle, guestFullName, guestTitle, cardNumber, expiry, cvv, cardName);

        step("Open order list page");
        homePage.goToHomePage();
        homePage.goToMyOrderPage();

        step("Open order detail for the order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();

        step("Verify that reschedule nudges is showing");
        Assertion.assertThat("Reschedule nudges showing", refundPage.isNudgeShow(), is(true));

        step("Verify back button");
        refundPage.backFromRescheduleNudge();
        Assertion.assertThat("Verify landing in order detail with refund button shown", orderDetailPage.isRefundButtonDisplayed(), is(true));

        step("Open refund page again");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();

        step("Verify continue to refund page");
        refundPage.continueRefundFromRescheduleNudge();
        Assertion.assertThat("Verify landing in refund detail page",refundPage.isRefundPageOpened(), is(true));

        step("Back to order page again");
        refundPage.clickBackFromRefund();

        step("Open refund page again");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();

        step("Verify click reschedule button");
        refundPage.clickRescheduleOnNudges();
        Assertion.assertThat("Verify landing not in refund page (reschedule page)",refundPage.isRefundPageOpened(), is(false));
    }

    @Author(names = {"Haniah"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void testRefundDeeplinkAT() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testRefundDeeplinkAT");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundCreation<?> refundCreation = new RefundCreation<>(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String auth = param.get("auth");
        String otp = param.get("otp");
        String timeOut = param.get("timeOut");
        String orderId = param.get("orderId");
        String orderDetailId = param.get("orderDetailId");
        String refundReason = param.get("refundReason");
        String bankName = param.get("bankName");
        String accountNumber = param.get("accountNumber");
        String saveBankOtp = param.get("saveBankOtp");
        String accountId = param.get("accountId");

        String refundSubmissionExpiry = OffsetDateTime.now(ZoneOffset.of("+07:00"))
                .plusDays(7)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Create Auto Refund for AT by API");
        String autoRefundCode = refundCreation.createAutoRefund(auth, orderId, orderDetailId, refundReason, refundSubmissionExpiry);
        Assertion.assertThat("Auto Refund created successfully", autoRefundCode, is(notNullValue()));

        step("Fetch and construct refund deeplink from Get Refund Status API");
        String requestHash = refundCreation.getRequestHash(auth, orderId, accountId);
        Assertion.assertThat("Request Hash is correct", requestHash, is(notNullValue()));

        step("Open refund deeplink page");
        refundPage.openAutoRefundURL(requestHash);

        step("Input new bank account");
        String inputAccountName = refundPage.inputNewBankAccount(bankName, accountNumber, saveBankOtp);
        Assertion.assertThat("Refund method bank account name is correct", refundPage.getRefundBankAccountName(), is(inputAccountName));
        String bankAccountName = refundPage.getRefundBankAccountName();
        String refundBankName = refundPage.getRefundBankName();
        Assertion.assertThat("Refund method account number", accountNumber, containsString(accountNumber));
        Assertion.assertThat("Refund method account name", bankAccountName, containsString(inputAccountName));
        Assertion.assertThat("Refund method transfer account name", refundBankName, containsString(bankName));

        step("Submit the refund deeplink");
        refundPage.clickChecklistTnc();
        refundPage.clickPrimaryButton("Submit a Refund Request");
        refundPage.clickPrimaryButton("Process Refund");
        refundPage.waitForSubmitRefundLoader();
        Assertion.assertThat("Success submit refund figure content", refundPage.getSuccessRefundFigureContent(), containsString("Success submit the request"));

        step("Go to refund detail page");
        refundPage.clickPrimaryButton("View Refund Detail");

        step("Verify Refund Detail");
        int countRefundProgress = refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(), not(emptyOrNullString()));
        Assertion.assertThat("Refund progress tracker more than 1", countRefundProgress, greaterThan(1));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(), not(emptyOrNullString()));
    }

    @Author(names = {"Haniah"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void testRefundDeeplinkFerry() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testRefundDeeplinkFerry");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundCreation<?> refundCreation = new RefundCreation<>(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String auth = param.get("auth");
        String otp = param.get("otp");
        String timeOut = param.get("timeOut");
        String orderId = param.get("orderId");
        String orderDetailId = param.get("orderDetailId");
        String refundReason = param.get("refundReason");
        String bankName = param.get("bankName");
        String accountNumber = param.get("accountNumber");
        String saveBankOtp = param.get("saveBankOtp");
        String accountId = param.get("accountId");

        String refundSubmissionExpiry = OffsetDateTime.now(ZoneOffset.of("+07:00"))
                .plusDays(7)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));

        step("Login into dweb");
        loginPage.logoutNeeded();
        loginPage.loginIfNotWithSameEmail(username, password, otp, 0);
        loginPage.skipUNMPageAfterLogin(timeOut);

        step("Create Auto Refund for Ferry by API");
        String autoRefundCode = refundCreation.createAutoRefund(auth, orderId, orderDetailId, refundReason, refundSubmissionExpiry);
        Assertion.assertThat("Auto Refund created successfully", autoRefundCode, is(notNullValue()));

        step("Fetch and construct refund deeplink from Get Refund Status API");
        String requestHash = refundCreation.getRequestHash(auth, orderId, accountId);
        Assertion.assertThat("Request Hash is correct", requestHash, is(notNullValue()));

        step("Open refund deeplink page");
        refundPage.openAutoRefundURL(requestHash);

        step("Input new bank account");
        String inputAccountName = refundPage.inputNewBankAccount(bankName, accountNumber, saveBankOtp);
        Assertion.assertThat("Refund method bank account name is correct", refundPage.getRefundBankAccountName(), is(inputAccountName));
        String bankAccountName = refundPage.getRefundBankAccountName();
        String refundBankName = refundPage.getRefundBankName();
        Assertion.assertThat("Refund method account number", accountNumber, containsString(accountNumber));
        Assertion.assertThat("Refund method account name", bankAccountName, containsString(inputAccountName));
        Assertion.assertThat("Refund method transfer account name", refundBankName, containsString(bankName));

        step("Submit the refund deeplink");
        refundPage.clickChecklistTnc();
        refundPage.clickPrimaryButton("Submit a Refund Request");
        refundPage.clickPrimaryButton("Process Refund");
        refundPage.waitForSubmitRefundLoader();
        Assertion.assertThat("Success submit refund figure content", refundPage.getSuccessRefundFigureContent(), containsString("Success submit the request"));

        step("Go to refund detail page");
        refundPage.clickPrimaryButton("View Refund Detail");

        step("Verify Refund Detail");
        int countRefundProgress = refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(), not(emptyOrNullString()));
        Assertion.assertThat("Refund progress tracker more than 1", countRefundProgress, greaterThan(1));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(), not(emptyOrNullString()));
    }

    @Author(names = {"Haniah"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void testRefundDeeplinkBus() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testRefundDeeplinkBus");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundCreation<?> refundCreation = new RefundCreation<>(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String auth = param.get("auth");
        String otp = param.get("otp");
        String timeOut = param.get("timeOut");
        String orderId = param.get("orderId");
        String orderDetailId = param.get("orderDetailId");
        String refundReason = param.get("refundReason");
        String bankName = param.get("bankName");
        String accountNumber = param.get("accountNumber");
        String saveBankOtp = param.get("saveBankOtp");
        String accountId = param.get("accountId");

        String refundSubmissionExpiry = OffsetDateTime.now(ZoneOffset.of("+07:00"))
                .plusDays(7)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Create Auto Refund for Bus by API");
        String autoRefundCode = refundCreation.createAutoRefund(auth, orderId, orderDetailId, refundReason, refundSubmissionExpiry);
        Assertion.assertThat("Auto Refund created successfully", autoRefundCode, is(notNullValue()));

        step("Fetch and construct refund deeplink from Get Refund Status API");
        String requestHash = refundCreation.getRequestHash(auth, orderId, accountId);
        Assertion.assertThat("Request Hash is correct", requestHash, is(notNullValue()));
    
        step("Open refund deeplink page");
        refundPage.openAutoRefundURL(requestHash);

        step("Input new bank account");
        String inputAccountName = refundPage.inputNewBankAccount(bankName, accountNumber, saveBankOtp);
        Assertion.assertThat("Refund method bank account name is correct", refundPage.getRefundBankAccountName(), is(inputAccountName));
        String bankAccountName = refundPage.getRefundBankAccountName();
        String refundBankName = refundPage.getRefundBankName();
        Assertion.assertThat("Refund method account number", accountNumber, containsString(accountNumber));
        Assertion.assertThat("Refund method account name", bankAccountName, containsString(inputAccountName));
        Assertion.assertThat("Refund method transfer account name", refundBankName, containsString(bankName));

        step("Submit the refund deeplink");
        refundPage.clickChecklistTnc();
        refundPage.clickPrimaryButton("Submit a Refund Request");
        refundPage.clickPrimaryButton("Process Refund");
        refundPage.waitForSubmitRefundLoader();
        Assertion.assertThat("Success submit refund figure content", refundPage.getSuccessRefundFigureContent(), containsString("Success submit the request"));

        step("Go to refund detail page");
        refundPage.clickPrimaryButton("View Refund Detail");

        step("Verify Refund Detail");
        int countRefundProgress = refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(), not(emptyOrNullString()));
        Assertion.assertThat("Refund progress tracker more than 1", countRefundProgress, greaterThan(1));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(), not(emptyOrNullString()));
    }

    @Author(names = {"Haniah"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void testRefundDeeplinkTrain() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testRefundDeeplinkTrain");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundCreation<?> refundCreation = new RefundCreation<>(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String auth = param.get("auth");
        String otp = param.get("otp");
        String timeOut = param.get("timeOut");
        String orderId = param.get("orderId");
        String orderDetailId = param.get("orderDetailId");
        String refundReason = param.get("refundReason");
        String bankName = param.get("bankName");
        String accountNumber = param.get("accountNumber");
        String saveBankOtp = param.get("saveBankOtp");
        String accountId = param.get("accountId");

        String refundSubmissionExpiry = OffsetDateTime.now(ZoneOffset.of("+07:00"))
                .plusDays(7)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Create Auto Refund for Train by API");
        String autoRefundCode = refundCreation.createAutoRefund(auth, orderId, orderDetailId, refundReason, refundSubmissionExpiry);
        Assertion.assertThat("Auto Refund created successfully", autoRefundCode, is(notNullValue()));

        step("Fetch and construct refund deeplink from Get Refund Status API");
        String requestHash = refundCreation.getRequestHash(auth, orderId, accountId);
        Assertion.assertThat("Request Hash is correct", requestHash, is(notNullValue()));

        step("Open refund deeplink page");
        refundPage.openAutoRefundURL(requestHash);

        step("Input new bank account");
        String inputAccountName = refundPage.inputNewBankAccount(bankName, accountNumber, saveBankOtp);
        Assertion.assertThat("Refund method bank account name is correct", refundPage.getRefundBankAccountName(), is(inputAccountName));
        String bankAccountName = refundPage.getRefundBankAccountName();
        String refundBankName = refundPage.getRefundBankName();
        Assertion.assertThat("Refund method account number", accountNumber, containsString(accountNumber));
        Assertion.assertThat("Refund method account name", bankAccountName, containsString(inputAccountName));
        Assertion.assertThat("Refund method transfer account name", refundBankName, containsString(bankName));

        step("Submit the refund deeplink");
        refundPage.clickChecklistTnc();
        refundPage.clickPrimaryButton("Submit a Refund Request");
        refundPage.clickPrimaryButton("Process Refund");
        refundPage.waitForSubmitRefundLoader();
        Assertion.assertThat("Success submit refund figure content", refundPage.getSuccessRefundFigureContent(), containsString("Success submit the request"));

        step("Go to refund detail page");
        refundPage.clickPrimaryButton("View Refund Detail");

        step("Verify Refund Detail");
        int countRefundProgress = refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(), not(emptyOrNullString()));
        Assertion.assertThat("Refund progress tracker more than 1", countRefundProgress, greaterThan(1));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(), not(emptyOrNullString()));
    }

    @Author(names = {"Haniah"})
    @Module(name = "Refund Deeplink")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void testNonLoginRefundDeeplinkRequest() {
        Map<String, String> param = getParam("testNonLoginRefundDeeplinkRequest");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);

        String requestHash = param.get("requestHash");

        step("User go to homepage in logout condition");
        loginPage.logoutNeeded();

        step("Open refund deeplink request page");
        refundPage.openAutoRefundURL(requestHash);

        step("Verify Login Page showing");
        Assertion.assertThat("Login Page showing", loginPage.isLoginV2PageOpened(), is(true));
    }

    @Author(names = {"Haniah"})
    @Module(name = "Refund Deeplink")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void testNonLoginRefundDeeplinkTrack() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testNonLoginRefundDeeplinkTrack");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);

        String requestHash = param.get("requestHash");

        step("Open refund deeplink tracker page");
        refundPage.openRefundTrackerURL(requestHash);

        step("Verify Login Page showing");
        Assertion.assertThat("Login Page showing", loginPage.isLoginV2PageOpened(), is(true));
    }


    @Author(names = {"Rizki Maulana"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void verifyRescheduleWithCashback() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRescheduleWithCashback");
        BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPageWeb = HotelLandingPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String hotelId = param.get("hotelId");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Create order");
        orderCreationPage.createHotelSakukuIssuedOrderApiWithHotelIdAndTheMostExpensive(
                username, password, startDate, hotelId, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, "IDR"
        );
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Check reschedule status");
        reschedulePage.checkRescheduleableStatus(username);

        step("Click check availability button");
        reschedulePage.clickCheckAvailabilityButton();
        hotelLandingPageWeb.closeContinueOnWebPopup();

        step("Click select room with same amount");
        reschedulePage.clickSelectRoomWithSameAmount();

        step("Click Book button");
        reschedulePage.clickBookButton();

        step("Click checkTnC button");
        reschedulePage.checkTnC();

        step("Click submit reschedule button");
        reschedulePage.clickSubmitRescheduleButton();
        hotelLandingPageWeb.closeContinueOnWebPopup();

        step("Click pay with Refund deposit button");
        reschedulePage.clickPayWithRefundDeposit();

        step("Click submit refund request");
        reschedulePage.clickSubmitRefundRequest();

        step("New order is issued and have old order linked in order detail");
        reschedulePage.verifyHotelStatus();
        reschedulePage.getNewOrderText();
        reschedulePage.verifyOrderIds();
        reschedulePage.compareOrderIds();

        step("Verify reschedule page currency is same with old order currency");
        reschedulePage.getOrderCurrency();
        reschedulePage.compareCurrencies();

        step("Verify Total and breakdown calculation are matched");
        reschedulePage.comparePrices();

        step("Verify Old order having section that there is order detail/pax already rescheduled");
        reschedulePage.rescheduleCompleted();
    }

    @Author(names = {"Rizki Maulana" ,"Mridul Gupta"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void verifyRescheduleWithMorePayment() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRescheduleWithMorePayment");
        BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        FlightReschedulePageWeb<?> flightReschedulePageWeb = FlightReschedulePageWeb.getInstance(session);
        FlightRescheduleSearchPageWeb<?> flightRescheduleSearchPageWeb = FlightRescheduleSearchPageWeb.getInstance(session);
        FlightRescheduleReviewPageWeb<?> flightRescheduleReviewPageWeb = FlightRescheduleReviewPageWeb.getInstance(session);
        FlightRescheduleCartWeb<?> flightRescheduleCartWeb = FlightRescheduleCartWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String searchType = param.get("searchType");
        String airline = param.get("airline");
        String rescheduleAirline = param.get("rescheduleAirline");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String phoneNumber = param.get("phoneNumber");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departDateApi = CoreFunctions.getRandomLongDateOnRange(daysToDepartureDate,30,"yyyy-MM-dd");
        daysToDepartureDate = CoreFunctions.getDaysToDepartureDate(departDateApi);

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adult, child, infant, searchType, airline,"", "", "", ""
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        airline = flightDynamicData.airlineDepart;
        departDateApi = flightDynamicData.departureDate;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData2 = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adult, child, infant, searchType, rescheduleAirline,"", "", "", ""
        );

        step("Go to home page");
        homePage.goToHomePage();
        homePage.selectEnglishLanguage();

        step("Check login state and log in");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, originAirport, destinationAirport);
        boolean isDepartSuppliersMockedRescheduled = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData2.airlineCodeDepart, originAirport, destinationAirport);


        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked +", " + "isDepartSuppliersMockedRescheduled: " + isDepartSuppliersMockedRescheduled + " }");
        if(!isDepartSuppliersMocked || !isDepartSuppliersMockedRescheduled) {
            throw new SkipException("Skipped because not all suppliers are mocked, isDepartSuppliersMocked: " + isDepartSuppliersMocked +", " + "isDepartSuppliersMockedRescheduled: " + isDepartSuppliersMockedRescheduled);
        }

        step("Create order");
        String orderId = flightApi.createOrderFlightB2CDomestic(username, password, originAirport, destinationAirport, departDateApi, searchType, adult, child, infant, airline,new String[]{firstName + " " + lastName},phoneNumber);
        Assertion.assertThat("Flight Order ID is created", orderId, notNullValue());

        String bookingCode = flightApi.getBookingCode(orderId, username);
        boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderId, bookingCode, username,firstName + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("Go to Order Detail Page");
        orderListPage.openActiveTicketViaURL(orderId);

        step("Go to Reschedule page");
        orderDetailPage.clickRescheduleButton();

        step("Pick pax that will be reschedule and search the product");
        flightReschedulePageWeb.selectAllPassengers();
        flightReschedulePageWeb.pressFindNewScheduleButton();

        step("Pick the product that have more expensive price");
        flightRescheduleSearchPageWeb.selectMoreExpensiveFlight(rescheduleAirline);

        step("Continue to the prebook");
        flightRescheduleReviewPageWeb.clickFirstFareOptions();

        step("Continue to payment page");
        flightRescheduleCartWeb.clickContinueToPayment();
        flightRescheduleCartWeb.confirmContinueToPayment();
        flightRescheduleCartWeb.confirmPriceChanges();

        step("Pick any payment method needed and paid");
        String newOrderId = paymentPage.getOrderId();
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), Matchers.is(true));

        step("Assertions");
        String oldOrderUrl = orderListPage.getActiveTicketURL(orderId);
        String newOrderUrl = orderListPage.getActiveTicketURL(newOrderId);
        Assertion.assertThat("New reschedule page currency is same with old order currency", reschedulePage.compareCurrenciesWithUrl(oldOrderUrl, newOrderUrl), is(true));

        orderListPage.openActiveTicketViaURL(newOrderId);
        reschedulePage.comparePricesForFlight();
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void partialPaxERefundOneWayVIRoundTrip() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("partialPaxERefundOneWayVIRoundTrip");
        BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String searchType = param.get("searchType");
        String airline = param.get("airline");
        String refundMethod = param.get("refundMethod");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String phoneNumber = param.get("phoneNumber");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int[] refundPassengerIndex = Arrays.stream(param.get("refundPassengerIndex").split("-")).mapToInt(Integer::parseInt).toArray();

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departDateApi = CoreFunctions.getRandomLongDateOnRange(daysToDepartureDate,30,"yyyy-MM-dd");
        daysToDepartureDate = CoreFunctions.getDaysToDepartureDate(departDateApi);

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adult, child, infant, searchType, airline,"", "", "", ""
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        airline = flightDynamicData.airlineDepart;
        departDateApi = flightDynamicData.departureDate;

        step("Go to home page");
        homePage.goToHomePage();
        homePage.selectEnglishLanguage();

        step("Check login state and log in");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, originAirport, destinationAirport);

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked +" }");
        if(!isDepartSuppliersMocked) {
            throw new SkipException("Skipped because not all suppliers are mocked issued by airline");
        }

        step("Create order");
        List<String> passengerNames = new ArrayList<>();
        passengerNames.add(firstName + " " + lastName);
        passengerNames.addAll(generateMultipleRandomName(adult - 1));

        String orderId = flightApi.createOrderFlightB2CDomestic(username, password, originAirport, destinationAirport, departDateApi, searchType, adult, child, infant, airline, passengerNames.toArray(String[]::new), phoneNumber);
        Assertion.assertThat("Flight Order ID is created", orderId, notNullValue());

        String bookingCode = flightApi.getBookingCode(orderId, username);
        boolean isTicketIssued = flightApi.flightEticketIssuedRequestMultipleGuest(orderId, bookingCode, username, passengerNames, 20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("Go to Order Detail Page");
        orderListPage.openActiveTicketViaURL(orderId);
        String currency = orderDetailPage.getCustomerCurrency();

        step("Go to Refund page");
        orderDetailPage.clickRefundButton();

        step("If there is reschedule nudge page opened, click refund");
        refundPage.continueToRefund();

        step("Pick refund reason that have partial Pax");
        refundPage.verifyFlightDetails();
        refundPage.selectRefundReasonPersonalReasonFlight();
        refundPage.checkPassengerCheckbox(refundPassengerIndex);
        refundPage.selectSpecificReason(refundPassengerIndex);

        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotalAndCurrency();
        Assertion.assertThat("Refund Total and breakdown calculation are matched", CoreFunctions.removeText(refundReceive) + CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));
        Assertion.assertThat("Price matrix shown and the currency is same with user paid", refundPage.getRefundTotalAndCurrency().split(" ")[0], is(currency));

        step("Click Continue");
        refundPage.continueRefund();

        step("Input refund method");
        refundPage.changeRefundMethod(refundMethod);

        step("Check the TnC agreement box");
        refundPage.clickChecklistTnc();

        step("Submit refund");
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();
        Assertion.assertThat("Verify that Refund is successfully submitted", refundDetailPage.getRefundId(), notNullValue());

        step("Verify Submitted Refund");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.verifyOrderUpdate();
        isTicketIssued = flightApi.flightEticketIssuedRequestMultipleGuest(orderId, bookingCode, username, passengerNames, 20);
        Assertion.assertThat("Verify that E-Ticket is still issued after partial refund", isTicketIssued, is(true));
    }

    @Author(names = {"Rizki Maulana", "Manan Kumar"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void partialPaxERefundSmartTrip() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("partialPaxERefundSmartTrip");
        BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String searchType = param.get("searchType");
        String airline = param.get("airline");
        String refundMethod = param.get("refundMethod");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String phoneNumber = param.get("phoneNumber");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int[] refundPassengerIndex = Arrays.stream(param.get("refundPassengerIndex").split("-")).mapToInt(Integer::parseInt).toArray();

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departDateApi = CoreFunctions.getRandomLongDateOnRange(daysToDepartureDate,0,"yyyy-MM-dd");
        daysToDepartureDate = CoreFunctions.getDaysToDepartureDate(departDateApi);
        String returnDate = LocalDate.parse(departDateApi, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(1).toString();

        int duration = CoreFunctions.getDaysBetweenDates(departDateApi, returnDate);

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate, duration,
                adult, child, infant, searchType, airline,"", "", "", "", "", ""
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        airline = flightDynamicData.airlineDepart;
        departDateApi = flightDynamicData.departureDate;
        returnDate = flightDynamicData.returnDate;

        step("Go to home page");
        homePage.goToHomePage();
        homePage.selectEnglishLanguage();

        step("Check login state and log in");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Create order");
        List<String> passengerNames = new ArrayList<>();
        passengerNames.add(firstName + " " + lastName);
        passengerNames.addAll(generateMultipleRandomName(adult-1));

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, originAirport, destinationAirport);
        boolean isReturnSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeReturn, destinationAirport, originAirport);

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked + ", isReturnSuppliersMocked: " + isReturnSuppliersMocked +" }");
        if(!isDepartSuppliersMocked || !isReturnSuppliersMocked) {
            throw new SkipException("Skipped because not all suppliers are mocked issued by airline");
        }

        String orderId = flightApi.createOrderFlightB2CRoundTrip(
                username,
                password,
                originAirport,
                destinationAirport,
                departDateApi,
                returnDate,
                searchType,
                adult,
                child,
                infant,
                airline,
                passengerNames.toArray(String[]::new),
                phoneNumber
                );
        Assertion.assertThat("Flight Order ID is created", orderId, notNullValue());

        HashMap<String, String> orderData = orderListPage.getOrderIdHashAndDetailId(orderId);
        paymentPage.goToPaymentPage(orderData.get("orderId"), orderData.get("orderHash"));

        paymentPage.selectRecommendedCreditPayment();
        paymentPage.clickOnPaymentButton();
        paymentPage.handle3DSPage(30);
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));
        paymentPage.waitAfterPayment();

        step("Go to Order Detail Page");
        orderListPage.openActiveTicketViaURL(orderId);
        String currency = orderDetailPage.getCustomerCurrency();

        step("Go to Refund page");
        orderDetailPage.clickRefundButton();

        step("If there is reschedule nudge page opened, click refund");
        refundPage.continueToRefund();

        step("Pick refund reason that have partial Pax");
        refundPage.verifyFlightDetails();
        refundPage.selectRefundReasonPersonalReasonFlight();
        refundPage.checkPassengerCheckbox(refundPassengerIndex);
        refundPage.selectSpecificReason(refundPassengerIndex);

        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotalAndCurrency();
        Assertion.assertThat("Refund Total and breakdown calculation are matched", CoreFunctions.removeText(refundReceive) + CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));
        Assertion.assertThat("Price matrix shown and the currency is same with user paid", refundPage.getRefundTotalAndCurrency().split(" ")[0], is(currency));

        step("Click Continue");
        refundPage.continueRefund();

        step("Input refund method");
        refundPage.changeRefundMethod(refundMethod);

        step("Check the TnC agreement box");
        refundPage.clickChecklistTnc();

        step("Submit refund");
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();
        Assertion.assertThat("Verify that Refund is successfully submitted", refundDetailPage.getRefundId(), notNullValue());

        step("Verify Submitted Refund");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.verifyOrderUpdate();
        String bookingCode = flightApi.getBookingCode(orderId, username);
        boolean isTicketIssued = flightApi.flightEticketIssuedRequestMultipleGuest(orderId, bookingCode, username, passengerNames, 20);
        Assertion.assertThat("Verify that E-Ticket is still issued after partial refund", isTicketIssued, is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Reschedule")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyRescheduleSamePrice() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyRescheduleSamePrice");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String currency = param.get("currency");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        OrderHistoryWeb<?> orderHistoryPage = OrderHistoryWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        HotelReschedulePageWeb<?> hotelReschedulePage = HotelReschedulePageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("Check login state and log in");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Select currency: " + currency);
        if (!currency.isEmpty()) {
            homePage.selectCurrency(currency);
        }

        step("Open my order page");
        homePage.goToMyOrderPage();

        step("Create hotel order if needed");
        String bookedOrderID = orderListPage.getHotelBookedOrderId(
                orderCreationPage,
                null,
                email,
                password,
                startDate,
                hotelName,
                adult,
                night,
                room,
                orderFullName,
                orderNameTitle,
                guestFullName,
                guestTitle
        );

        step("Get internal data for the issued order");
        String hotelNameData = orderDetailPage.getVerticalData(bookedOrderID).get("orderName");
        String oldOrderPrice = orderDetailPage.getVerticalData(bookedOrderID).get("orderAmount");
        String oldOrderCurrency = orderDetailPage.getVerticalData(bookedOrderID).get("currency");

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(), is(bookedOrderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(), is(true));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(hotelNameData), is(true));

        step("Navigate to reschedule page");
        orderDetailPage.clickRescheduleButton();
        Assertion.assertThat("Reschedule page is loaded", hotelReschedulePage.isInReschedulePage(), is(true));

        step("Choose room with same price as old order");
        hotelReschedulePage.chooseRoomWithSamePrice(oldOrderPrice);

        step("Submit reschedule booking form");
        String totalPriceBookingForm = hotelReschedulePage.getTotalPriceBookingForm();
        Assertion.assertThat("Total and breakdown calculation are matched", totalPriceBookingForm, containsString(oldOrderPrice));
        hotelReschedulePage.submitRescheduleBookingForm();

        step("Verify redirected to payment page");
        Assertion.assertThat("Payment page is loaded", paymentPage.isPaymentPageLoaded(), is(true));

        step("Check order id on payment page");
        String newOrderId = paymentPage.getOrderId();
        Assertion.assertThat("New order ID is different from old order", newOrderId, is(not(bookedOrderID)));

        step("Verify reschedule page currency is same with old order currency");
        String paymentPageCurrency = paymentPage.getCurrency();
        Assertion.assertThat("Reschedule page currency is same with old order currency", paymentPageCurrency, containsString(oldOrderCurrency));

        step("Pay with refund deposit");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.selectPaymentMethod("Refund Deposit");
        paymentPage.clickOnPaymentButton();

        step("Verify payment successful");
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify new order is issued and have old order linked in order detail");
        orderListPage.openActiveTicketViaURL(newOrderId);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("E-ticket is issued for new order", orderDetailPage.isETicketCardDisplayed(), is(true));

        step("Verify old order having section that order detail already rescheduled");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("Old order shows reschedule card", orderDetailPage.isRescheduleCardDisplayed(), is(true));
        Assertion.assertThat("Old order shows new schedule button", orderDetailPage.isNewScheduleButtonDisplayed(), is(true));

        step("Verify no refund created in refund list");
        homePage.openYourOrderViaURL();
        orderListPage.openOrderHistoryPage();
        orderHistoryPage.openPastRefundTab();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Reschedule")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyRescheduleMorePayment() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyRescheduleMorePayment");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);
        String currency = param.get("currency");
        boolean isRefundable = Boolean.parseBoolean(param.get("isRefundable"));
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cvv = param.get("cvv");
        String cardHolderName = param.get("cardHolderName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePage = HotelReschedulePageWeb.getInstance(session);
        RoomDetailsPageWeb<?> roomDetailsPage = RoomDetailsPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderHistoryWeb<?> orderHistoryPage = OrderHistoryWeb.getInstance(session);

        step("Go to home page");
        homePage.goToHomePage();

        step("Check login state and log in");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Select currency: " + currency);
        homePage.selectCurrency(currency);

        step("Create hotel order if needed");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        Boolean result = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, isRefundable,
                false, "", "en",
                true);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();
        String oldOrderPrice = CoreFunctions.roundPrice(orderDetailPage.getVerticalTotalPrice());

        step("Verify hotel order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(), is(bookedOrderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(), is(true));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(hotelName), is(true));

        step("Navigate to reschedule page");
        orderDetailPage.clickRescheduleButton();
        Assertion.assertThat("Reschedule page is loaded", hotelReschedulePage.isInReschedulePage(), is(true));

        step("Open room detail with additional price");
        hotelReschedulePage.openRoomWithAdditionalPrice();
        roomDetailsPage.clickOnPriceDetails();

        step("Verify current balance equals initial order price");
        String currentBalance = roomDetailsPage.getCurrentBalance(true);
        Assertion.assertThat("Current balance should be equal with initial total price",
                currentBalance, is(oldOrderPrice));
        roomDetailsPage.closePriceDetails();

        step("Book the room with additional Price");
        roomDetailsPage.clickBookButton();

        step("Submit reschedule booking form");
        hotelReschedulePage.submitRescheduleBookingForm();

        step("Verify redirected to payment page");
        Assertion.assertThat("Payment page is loaded", paymentPage.isPaymentPageLoaded(), is(true));

        step("Check order id on payment page");
        String newOrderID = paymentPage.getOrderId();
        Assertion.assertThat("New order ID is different from old order", newOrderID, is(not(bookedOrderID)));

        step("Verify reschedule page currency is same with old order currency");
        String paymentCurrency = paymentPage.getCurrency();
        Assertion.assertThat("Reschedule page currency is same with old order currency",
                paymentCurrency, is(currency));

        step("Get price difference to pay");
        String totalPayment = CoreFunctions.roundPrice(paymentPage.getFinalPrice());
        Assertion.assertThat("Total payment should be greater than 0 (price difference)",
                Double.parseDouble(totalPayment), is(greaterThan(0.0)));

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Finish the payment process by clicking the payment button");
        paymentPage.clickOnPaymentButton();
        paymentPage.handle3DSPage(30);

        step("Verify payment successful");
        Assertion.assertThat("Payment successful page is visible",
                paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify new order is issued and have old order linked in order detail");
        orderListPage.openActiveTicketViaURL(newOrderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("E-ticket is issued for new order", orderDetailPage.isETicketCardDisplayed(), is(true));
        Assertion.assertThat("New order shows reschedule notification banner",
                orderDetailPage.isRescheduleCardDisplayed(), is(true));

        step("Verify old order having section that order detail already rescheduled");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("Old order shows reschedule card", orderDetailPage.isRescheduleCardDisplayed(), is(true));
        Assertion.assertThat("Old order shows new schedule button", orderDetailPage.isNewScheduleButtonDisplayed(), is(true));

        step("Verify no refund created in refund list");
        homePage.openYourOrderViaURL();
        orderListPage.openOrderHistoryPage();
        orderHistoryPage.openPastRefundTab();
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void testCompleteFlowSSRRHomes() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteFlowSSRRHomes");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String location = param.get("location");
        String salutation = param.get("salutation");
        int rooms = Integer.parseInt(param.get("rooms"));
        int guests = Integer.parseInt(param.get("guests"));
        String dateRange = param.get("dateRange");
        String dateType = param.get("dateType");
        String currency = param.get("currency");
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelBookingsRebookPageWeb<?> hotelBookingsRebookPageWeb = HotelBookingsRebookPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Select currency: " + currency);
        homePage.selectCurrency(currency);

        step("Navigate to Hotel vertical and search for stay");
        homePage.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType,dateRange);
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("PDP is loaded", detailPage.isPdpLoaded(), Matchers.is(true));
        detailPage.seeHotelRoom();

        step("Set guest and room count");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(guests);
        roomListPage.setTotalRoom(rooms);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Click select on Room List");
        roomListPage.clickBookRoomButton();

        step("Click book button");
        bookingFormPage.fillSalutations(salutation);
        bookingFormPage.fillGuestDetailsCombinedRandom(guests);
        bookingFormPage.continueToPayment();
        bookingFormPage.continueToPaymentPage();
        String orderId = paymentPage.getOrderId();

        step("Submit payment method");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();

        step("Click book again or refund");
        orderDetailPage.clickBookAgainOrRefund();

        step("Click refund button");
        hotelBookingsRebookPageWeb.clickRefundButton();

        step("Check the TnC agreement box");
        refundPage.clickChecklistTnc();

        step("Submit refund");
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();

        step("Go to refund details");
        refundPage.clickGoToOrders();
        Assertion.assertThat("Verify that Refund is successfully submitted", refundDetailPage.getRefundId(), notNullValue());
        refundDetailPage.verifyReservationDetails(location);
        refundDetailPage.verifyBreakdownAmountIsCorrect(currency);

        step("Go to order detail after refund");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.verifyOrderUpdate();
    }

    @Author(names = {"Nasha"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void verifyERefundMultiCity() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyERefundMultiCity");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        FlightIssuedOrderFactory<?> issuedOrderFactory = new FlightIssuedOrderFactory<>(session);

        String username = param.get("username");
        String password = param.get("password");

        List<String[]> routes = CoreFunctions.splitPairs(param.get("routes"), "\\|", "-");
        int legCount = routes.size();
        List<String> departureDates = CoreFunctions.generateAscendingRandomLongDates(param.get("rangeAddDate"), param.get("gapRange"), legCount);
        List<String> airlineCodes = CoreFunctions.expandToSize(param.get("airlines"), "\\|", legCount);

        step("Create a refundable multi-city order for " + legCount + " journeys with " + param.get("adult") + " adult pax, paid and issued");
        log("Routes: " + param.get("routes") + ", airlines: " + airlineCodes + ", departure dates: " + departureDates);
        FlightApi.MultiCityOrder order = issuedOrderFactory.createIssuedMultiCity(new FlightIssuedOrderFactory.MultiCityInputs(
                username, password, routes, departureDates, airlineCodes,
                Integer.parseInt(param.get("adult")), Integer.parseInt(param.get("child")),
                Integer.parseInt(param.get("infant")), param.get("cabinClass"), param.get("phoneNumber")));
        Assertion.assertThat("Multi-city order is created, paid and issued", order, notNullValue());
        String orderId = order.orderId();

        step("Reuse the API session in the browser");
        loginPage.loginWithSpecificAccountViaCookie(param.get("username"), param.get("password"));

        step("Go to your order and open the order detail that will be refunded");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();
        String currency = orderDetailPage.getCustomerCurrency();

        step("Click refund button");
        orderDetailPage.clickRefundButton();

        step("If there is reschedule nudge page opened, click refund");
        refundPage.skipRescheduleNudgeIfShown();

        step("Order review in refund page match with the flight being refunded");
        refundPage.verifyFlightDetails(orderDetailPage.getVerticalData(orderId, refundPage.getRefundedOrderDetailId()));

        step("Pick refund reason and choose all pax");
        refundPage.selectRefundReasonPersonalReasonFlight();
        int[] refundPassengerIndex = refundPage.checkAllPassengerCheckbox();
        refundPage.selectSpecificReason(refundPassengerIndex);

        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotalAndCurrency();
        Assertion.assertThat("Refund Total and breakdown calculation are matched", CoreFunctions.removeText(refundReceive) + CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));
        Assertion.assertThat("Price matrix shown and the currency is same with user paid", refundTotal.split(" ")[0], is(currency));

        step("Click Continue");
        refundPage.continueRefund();

        step("Estimation page shown match with the previous page");
        Assertion.assertThat("Estimation total matches the total shown on the previous page", refundPage.getRefundTotalAndCurrency(), is(refundTotal));

        step("Input refund method, skipped when the refund method is not changeable");
        refundPage.changeRefundMethod(param.get("refundMethod"));

        step("Check the TnC agreement box");
        refundPage.clickChecklistTnc();

        step("Submit refund and confirm the confirmation pop up");
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.closeRelatedOrderPopupIfShown();

        step("Go to refund details");
        refundPage.clickViewRefundDetail();
        Assertion.assertThat("Refund successfully submitted", refundDetailPage.getRefundId(), notNullValue());

        step("Back to order page and check the order detail for the refunding order");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.verifyOrderUpdate();
    }
}

