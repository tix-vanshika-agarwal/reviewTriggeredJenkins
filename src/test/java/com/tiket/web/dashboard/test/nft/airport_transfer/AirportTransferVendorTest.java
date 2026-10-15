package com.tiket.web.dashboard.test.nft.airport_transfer;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.nft.airport_transfer.AirportTransferVendorPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class AirportTransferVendorTest extends BaseTest {
    @Author(names = { "Okta" , "Clement" })
    @Module(name = "Finance")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testViewTransactionData() {
        Map<String, String> param = getParam("testViewTransactionData");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AirportTransferVendorPage<?> atPage = new AirportTransferVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String menu = param.get("menu");
        String orderId = param.get("orderId");

        step("User open airport transfer vendor dashboard");
        atPage.openAirportTransferVendorPage(username, password, menu);

        step("User search transaction data");
        atPage.filterTransactionData(orderId);
    }

    @Author(names = { "Okta" , "Clement" })
    @Module(name = "Finance")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testResetTransactionData() {
        Map<String, String> param = getParam("testResetTransactionData");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AirportTransferVendorPage<?> atPage = new AirportTransferVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String menu = param.get("menu");
        String orderId = param.get("orderId");

        step("User open airport transfer vendor dashboard");
        atPage.openAirportTransferVendorPage(username, password, menu);

        step("User search transaction data");
        atPage.filterTransactionData(orderId);

        step("User reset search transaction data");
        atPage.resetSearchTransactionData();

        step("User verify reset search transaction data");
        atPage.verifyResetSearchTransactionData();
    }

    @Author(names = "Okta")
    @Module(name = "Finance")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testExportTransactionData() {
        Map<String, String> param = getParam("testExportTransactionData");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AirportTransferVendorPage<?> atPage = new AirportTransferVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String menu = param.get("menu");

        String orderId = param.get("orderId");
        String fileName = param.get("fileName");
        String timeOut = param.get("timeOut");

        step("User open airport transfer vendor dashboard");
        atPage.openAirportTransferVendorPage(username, password, menu);

        step("User search transaction data");
        atPage.filterTransactionData(orderId);

        step("User export transaction data");
        atPage.exportTransactionData();

        step("User check exported file");
        Assertion.assertThat("Verify file has been downloaded", atPage.verifyDownloadedExport(fileName, timeOut), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "OrderManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testViewIncomingOrderAll() {
        Map<String, String> param = getParam("testViewIncomingOrderAll");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AirportTransferVendorPage<?> atPage = new AirportTransferVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String airport = param.get("airport");

        step("User open airport transfer vendor dashboard");
        atPage.openAirportTransferVendorPage(username, password, airport);

        step("User open incoming order tab");
        atPage.openOrderTypeTab("incoming");

        step("User open All tab");
        atPage.filterByTransferType("All");
        Assertion.assertThat("The results for all incoming order are displayed", atPage.verifyOrderView(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "OrderManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testViewIncomingOrderTransferIn() {
        Map<String, String> param = getParam("testViewIncomingOrderTransferIn");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AirportTransferVendorPage<?> atPage = new AirportTransferVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String airport = param.get("airport");

        step("User open airport transfer vendor dashboard");
        atPage.openAirportTransferVendorPage(username, password, airport);

        step("User open incoming order tab");
        atPage.openOrderTypeTab("incoming");

        step("User open Transfer-In tab");
        atPage.filterByTransferType("In");
        Assertion.assertThat("The results for transfer-in incoming order are displayed", atPage.verifyOrderView(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "OrderManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testViewIncomingOrderTransferOut() {
        Map<String, String> param = getParam("testViewIncomingOrderTransferOut");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AirportTransferVendorPage<?> atPage = new AirportTransferVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String airport = param.get("airport");

        step("User open airport transfer vendor dashboard");
        atPage.openAirportTransferVendorPage(username, password, airport);

        step("User open incoming order tab");
        atPage.openOrderTypeTab("incoming");

        step("User open Transfer-Out tab");
        atPage.filterByTransferType("Out");
        Assertion.assertThat("The results for transfer-out incoming order are displayed", atPage.verifyOrderView(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "OrderManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testViewSpecificCompletedOrder() {
        Map<String, String> param = getParam("testViewSpecificCompletedOrder");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AirportTransferVendorPage<?> atPage = new AirportTransferVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String airport = param.get("airport");
        String orderId = param.get("orderId");

        step("User open airport transfer vendor dashboard");
        atPage.openAirportTransferVendorPage(username, password, airport);

        step("User open completed order tab");
        atPage.openOrderTypeTab("complete");

        step("User filter by order ID on completed order");
        Assertion.assertThat("The result of filter by order ID is displayed", atPage.filterByOrderId(orderId), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "OrderManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testAddNoteIncomingOrderTransferIn() {
        Map<String, String> param = getParam("testAddNoteIncomingOrderTransferIn");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AirportTransferVendorPage<?> atPage = new AirportTransferVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String airport = param.get("airport");
        String note = param.get("note");

        step("User open airport transfer vendor dashboard");
        atPage.openAirportTransferVendorPage(username, password, airport);

        step("User open incoming order tab");
        atPage.openOrderTypeTab("incoming");

        step("User open Transfer-In tab");
        atPage.filterByTransferType("In");

        step("User add note on incoming order");
        Assertion.assertThat("Note is displayed on incoming order", atPage.addNoteIncomingOrder(note), is(true));
        Assertion.assertThat("Snackbar is displayed for successful add note", atPage.verifySuccessSnackbar(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "OrderManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testAddNoteIncomingOrderTransferOut() {
        Map<String, String> param = getParam("testAddNoteIncomingOrderTransferOut");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AirportTransferVendorPage<?> atPage = new AirportTransferVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String airport = param.get("airport");
        String note = param.get("note");

        step("User open airport transfer vendor dashboard");
        atPage.openAirportTransferVendorPage(username, password, airport);

        step("User open incoming order tab");
        atPage.openOrderTypeTab("incoming");

        step("User open Transfer-Out tab");
        atPage.filterByTransferType("Out");

        step("User add note on incoming order");
        Assertion.assertThat("Note is displayed on incoming order", atPage.addNoteIncomingOrder(note), is(true));
        Assertion.assertThat("Snackbar is displayed for successful add note", atPage.verifySuccessSnackbar(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "OrderManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testConfirmIncomingOrderTransferIn() {
        Map<String, String> param = getParam("testConfirmIncomingOrderTransferIn");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AirportTransferVendorPage<?> atPage = new AirportTransferVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String airport = param.get("airport");

        step("User open airport transfer vendor dashboard");
        atPage.openAirportTransferVendorPage(username, password, airport);

        step("User open incoming order tab");
        atPage.openOrderTypeTab("incoming");

        step("User open Transfer-In tab");
        atPage.filterByTransferType("In");

        step("User confirm incoming order");
        atPage.confirmIncomingOrder();
        Assertion.assertThat("Snackbar is displayed for successful confirmation", atPage.verifySuccessSnackbar(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "OrderManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testConfirmIncomingOrderTransferOut() {
        Map<String, String> param = getParam("testConfirmIncomingOrderTransferOut");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AirportTransferVendorPage<?> atPage = new AirportTransferVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String airport = param.get("airport");

        step("User open airport transfer vendor dashboard");
        atPage.openAirportTransferVendorPage(username, password, airport);

        step("User open incoming order tab");
        atPage.openOrderTypeTab("incoming");

        step("User open Transfer-Out tab");
        atPage.filterByTransferType("Out");

        step("User confirm incoming order");
        atPage.confirmIncomingOrder();
        Assertion.assertThat("Snackbar is displayed for successful confirmation", atPage.verifySuccessSnackbar(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "OrderManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testViewVoucherIncomingOrderTransferIn() {
        Map<String, String> param = getParam("testViewVoucherIncomingOrderTransferIn");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AirportTransferVendorPage<?> atPage = new AirportTransferVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String airport = param.get("airport");
        String fileName = param.get("fileName");
        String timeOut = param.get("timeOut");

        step("User open airport transfer vendor dashboard");
        atPage.openAirportTransferVendorPage(username, password, airport);

        step("User open incoming order tab");
        atPage.openOrderTypeTab("incoming");

        step("User open Transfer-In tab");
        atPage.filterByTransferType("In");

        step("User download voucher");
        atPage.downloadVoucher();

        step("User check downloaded file");
        Assertion.assertThat("Verify file has been downloaded", atPage.verifyDownloadedExport(fileName, timeOut), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "OrderManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testViewVoucherIncomingOrderTransferOut() {
        Map<String, String> param = getParam("testViewVoucherIncomingOrderTransferOut");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AirportTransferVendorPage<?> atPage = new AirportTransferVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String airport = param.get("airport");
        String fileName = param.get("fileName");
        String timeOut = param.get("timeOut");

        step("User open airport transfer vendor dashboard");
        atPage.openAirportTransferVendorPage(username, password, airport);

        step("User open incoming order tab");
        atPage.openOrderTypeTab("incoming");

        step("User open Transfer-Out tab");
        atPage.filterByTransferType("Out");

        step("User download voucher");
        atPage.downloadVoucher();

        step("User check downloaded file");
        Assertion.assertThat("Verify file has been downloaded", atPage.verifyDownloadedExport(fileName, timeOut), is(true));
    }

    @Author(names = { "Okta" , "Clement" })
    @Module(name = "OrderManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testExportCompletedOrderData() {
        Map<String, String> param = getParam("testExportCompletedOrderData");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AirportTransferVendorPage<?> atPage = new AirportTransferVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String airport = param.get("airport");
        String fileName = param.get("fileName");
        String timeOut = param.get("timeOut");

        step("User open airport transfer vendor dashboard");
        atPage.openAirportTransferVendorPage(username, password, airport);

        step("User open completed order tab");
        atPage.openOrderTypeTab("complete");

        step("User export completed order");
        atPage.exportOrderData();

        step("User check downloaded file");
        Assertion.assertThat("Verify file has been downloaded", atPage.verifyDownloadedExport(fileName, timeOut), is(true));
    }
}