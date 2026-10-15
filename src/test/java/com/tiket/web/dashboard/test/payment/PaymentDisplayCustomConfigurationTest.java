package com.tiket.web.dashboard.test.payment;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.HotelApi;
import com.tiket.apiPage.PaymentApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.payment.PaymentDashboardPage;
import com.tiket.web.dashboard.page.payment.PaymentDisplayCustomConfiguration;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.is;

public class PaymentDisplayCustomConfigurationTest extends BaseTest {

    @Author(names = {"Amit Bhardwaj"})
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void testaddNewExpiryTime() throws InterruptedException, IOException {
        Map<String, String> map = getParam("testaddNewExpiryTime");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentDisplayCustomConfiguration<?> paymentDisplayCustomConfiguration = new PaymentDisplayCustomConfiguration<>(session);
        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");

        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Display Custom Config Page");
        paymentDisplayCustomConfiguration.openCustomConfiguration();

        step("Add New Expiry Time and save");
        paymentDisplayCustomConfiguration.createNewExpiryTime();
        paymentDisplayCustomConfiguration.enablePaymentOptions();
        Assertion.assertThat("Successfully saved expiry data.", paymentDisplayCustomConfiguration.successSaved(), is(true));

        step("Fetch New Expiry Time From MongoDB");
        boolean hasData = paymentDisplayCustomConfiguration.fetchNewExpiryTimeFromMongoDB(config, database, collectionName);
        Assertion.assertThat("Check if MongoDB return any data", hasData, is(true));

    }


    @Author(names = {"Amit Bhardwaj"})
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void testDeactivateNewExpiryTime() throws InterruptedException {
        Map<String, String> map = getParam("testDeactivateNewExpiryTime");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentDisplayCustomConfiguration<?> paymentDisplayCustomConfiguration = new PaymentDisplayCustomConfiguration<>(session);
        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");

        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Display Custom Config Page");
        paymentDisplayCustomConfiguration.openCustomConfiguration();

        step("Deactivate New Expiry Time page");
        paymentDisplayCustomConfiguration.deactivateCustomConfiguration();
        Assertion.assertThat("Successfully saved expiry data.", paymentDisplayCustomConfiguration.successSaved(), is(true));

        step("Verify the Deactivate status");
        paymentDisplayCustomConfiguration.checkExpiryPageStatus();

        step("Fetch Inactive New Expiry Time From MongoDB");
        boolean hasData = paymentDisplayCustomConfiguration.fetchInactiveExpiryTimeFromMongoDB(config, database, collectionName);
        Assertion.assertThat("Check if MongoDB return any data", hasData, is(true));
    }

    @Author(names = {"Amit Bhardwaj"})
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void testEditNewExpiryTime() throws InterruptedException, IOException {
        Map<String, String> map = getParam("testEditNewExpiryTime");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentDisplayCustomConfiguration<?> paymentDisplayCustomConfiguration = new PaymentDisplayCustomConfiguration<>(session);
        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");

        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Display Custom Config Page");
        paymentDisplayCustomConfiguration.openCustomConfiguration();

        step("Edit New Expiry Time page");
        paymentDisplayCustomConfiguration.editCustomConfiguration();
        Assertion.assertThat("Successfully saved expiry data.", paymentDisplayCustomConfiguration.successSaved(), is(true));

        step("Fetch Inactive New Expiry Time From MongoDB");
        boolean hasData = paymentDisplayCustomConfiguration.fetchEditedExpiryTimeFromMongoDB(config, database, collectionName);
        Assertion.assertThat("Check if MongoDB return any data", hasData, is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "Sphinx")
    @Test
    public void testVerifyIDRVerticalAndExpiryTimeFlow() throws InterruptedException, IOException, ExecutionException {
        Map<String, String> map = getParam("testVerifyIDRVerticalAndExpiryTimeFlow");
        BaseTest.paramsThreadLocal.set(map);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentDisplayCustomConfiguration<?> paymentDisplayCustomConfiguration = new PaymentDisplayCustomConfiguration<>(session);
        PaymentApi<?> paymentApi = new PaymentApi<>(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String productType = map.get("productType");
        String hotelId = map.get("hotelId");
        String startDate = CoreFunctions.getJakartaDateYYYY_MM_DD(3);
        String currency = map.get("currency");
        String bookingUsername = map.get("bookingUsername");
        String bookingPassword = map.get("bookingPassword");
        int expiryMinutes = 30 + (int) (Math.random() * 21); // random between 30 and 50
        log("Using expiry minutes -> " + expiryMinutes);

        step("Create TIXHOTEL waiting-payment order using static hotelId");
        String referenceId = hotelApi.createWaitingPaymentOrderApiByHotelId(bookingUsername, bookingPassword, startDate, hotelId, 1, 1, 1, currency, 0, "en");
        String orderHash = hotelApi.getOrderHash();
        log("referenceId (orderId) at runtime -> " + referenceId);
        log("orderHash at runtime -> " + orderHash);

        step("Login into Payment V2 Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Fetch initial payment method statuses before config change");
        Map<String, Boolean> statusBefore = paymentApi.getPaymentMethodStatuses(orderHash, referenceId, null);
        boolean creditCardStatusBefore = statusBefore.getOrDefault("CREDITCARD", false);
        boolean sakukuStatusBefore = statusBefore.getOrDefault("SAKUKU", false);
        boolean vaBcaStatusBefore = statusBefore.getOrDefault("VA_BCA", false);
        log("CREDITCARD status before -> " + creditCardStatusBefore);
        log("SAKUKU status before -> " + sakukuStatusBefore);
        log("VA_BCA status before -> " + vaBcaStatusBefore);

        step("Navigate to Payment Display Custom Configuration → IDR Vertical & Expiry Time");
        paymentDisplayCustomConfiguration.openCustomConfiguration();

        step("Click Add New Expiry Config and fill form");
        paymentDisplayCustomConfiguration.createNewExpiryTimeWithConfig(productType, expiryMinutes);

        step("Toggle to active state");
        paymentDisplayCustomConfiguration.changeToggleStatus();

        step("Save the new expiry config");
        paymentDisplayCustomConfiguration.clickSave();

        step("Click Submit");
        paymentDisplayCustomConfiguration.clickSubmit();

        step("Verify the Active status");
        paymentDisplayCustomConfiguration.verifyExpiryTimeRowStatus(expiryMinutes, "Active");


        step("Click Edit button for the newly created expiry config");
        paymentDisplayCustomConfiguration.clickEditForExpiryTime(expiryMinutes);

        step("Set CREDITCARD → Disable, VA_BCA → Hide, SAKUKU → Enable");
        paymentDisplayCustomConfiguration.setPaymentMethodStatus("CREDITCARD", "Disable");
        paymentDisplayCustomConfiguration.setPaymentMethodStatus("VA_BCA", "Hide");
        paymentDisplayCustomConfiguration.setPaymentMethodStatus("SAKUKU", "Enable");

        step("Save updated payment method config");
        paymentDisplayCustomConfiguration.clickSave();

        step("Click Save Changes");
        paymentDisplayCustomConfiguration.clickSaveChanges();

        step("Wait for payment service cache to propagate config change");
        sleep(10);

        step("Create new TIXHOTEL waiting-payment order to verify updated statuses");
        String referenceId2 = hotelApi.createWaitingPaymentOrderApiByHotelId(bookingUsername, bookingPassword, startDate, hotelId, 1, 1, 1, currency, 0, "en");
        String orderHash2 = hotelApi.getOrderHash();
        log("referenceId2 -> " + referenceId2);
        log("orderHash2 -> " + orderHash2);

        step("Fetch payment method statuses after config change");
        Map<String, Boolean> statusAfter = paymentApi.getPaymentMethodStatuses(orderHash2, referenceId2, null);
        log("Payment statuses after config -> " + statusAfter);

        step("Assert CREDITCARD is Disabled (status = false)");
        Assertion.assertThat("CREDITCARD should be Disabled", statusAfter.getOrDefault("CREDITCARD", false), is(false));

        step("Assert SAKUKU is Enabled (status = true)");
        Assertion.assertThat("SAKUKU should be Enabled", statusAfter.getOrDefault("SAKUKU", false), is(true));

        step("Assert VA_BCA is Hidden (not present in response)");
        Assertion.assertThat("VA_BCA should be Hidden (absent from response)", statusAfter.containsKey("VA_BCA"), is(false));

        step("Click Edit button to make config Inactive");
        paymentDisplayCustomConfiguration.clickEditForExpiryTime(expiryMinutes);

        step("Make inactive the newly created expiry config");
        paymentDisplayCustomConfiguration.changeToggleStatus();

        step("Save the new expiry config");
        paymentDisplayCustomConfiguration.clickSave();
        paymentDisplayCustomConfiguration.clickSaveChanges();

        step("Verify the inactive status");
        paymentDisplayCustomConfiguration.verifyExpiryTimeRowStatus(expiryMinutes, "Inactive");

        step("Create new order after config set to Inactive — to verify revert to original state");
        String referenceId3 = hotelApi.createWaitingPaymentOrderApiByHotelId(bookingUsername, bookingPassword, startDate, hotelId, 1, 1, 1, currency, 0, "en");
        String orderHash3 = hotelApi.getOrderHash();
        log("referenceId3 -> " + referenceId3);
        log("orderHash3 -> " + orderHash3);

        step("Fetch payment method statuses after config set to Inactive");
        Map<String, Boolean> statusAfterRevert = paymentApi.getPaymentMethodStatuses(orderHash3, referenceId3, null);

        boolean creditCardStatusAfterRevert = statusAfterRevert.getOrDefault("CREDITCARD", false);
        boolean sakukuStatusAfterRevert = statusAfterRevert.getOrDefault("SAKUKU", false);
        boolean vaBcaStatusAfterRevert = statusAfterRevert.getOrDefault("VA_BCA", false);
        log("CREDITCARD status after revert -> " + creditCardStatusAfterRevert);
        log("SAKUKU status after revert -> " + sakukuStatusAfterRevert);
        log("VA_BCA status after revert -> " + vaBcaStatusAfterRevert);

        step("Assert statuses are back to original (same as before config was applied)");
        Assertion.assertThat("CREDITCARD status should match before state", creditCardStatusAfterRevert, is(creditCardStatusBefore));
        Assertion.assertThat("SAKUKU status should match before state", sakukuStatusAfterRevert, is(sakukuStatusBefore));
        Assertion.assertThat("VA_BCA status should match before state", vaBcaStatusAfterRevert, is(vaBcaStatusBefore));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "Sphinx")
    @Test
    public void testVerifyNonIDRVerticalAndExpiryTimeFlow() throws InterruptedException, IOException, ExecutionException {
        Map<String, String> map = getParam("testVerifyNonIDRVerticalAndExpiryTimeFlow");
        BaseTest.paramsThreadLocal.set(map);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentDisplayCustomConfiguration<?> paymentDisplayCustomConfiguration = new PaymentDisplayCustomConfiguration<>(session);
        PaymentApi<?> paymentApi = new PaymentApi<>(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String productType = map.get("productType");
        String hotelId = map.get("hotelId");
        String currency = map.get("currency");
        String countryCode = map.get("countryCode");
        String startDate = CoreFunctions.getJakartaDateYYYY_MM_DD(3);
        String paymentMethod = map.get("paymentMethod");
        String bookingUsername = map.get("bookingUsername");
        String bookingPassword = map.get("bookingPassword");
        int expiryMinutes = 30 + (int) (Math.random() * 21);
        log("Using expiry minutes -> " + expiryMinutes);
        log("Using currency -> " + currency + " | countryCode -> " + countryCode);

        step("Create waiting-payment order in " + currency + " currency");
        String referenceId = hotelApi.createWaitingPaymentOrderApiByHotelId(bookingUsername, bookingPassword, startDate, hotelId, 1, 1, 1, currency, 0, "en");
        String orderHash = hotelApi.getOrderHash();
        log("referenceId -> " + referenceId);
        log("orderHash -> " + orderHash);

        step("Fetch initial payment method statuses before config change.");
        Map<String, Boolean> statusBefore = paymentApi.getPaymentMethodStatuses(orderHash, referenceId, null, currency, countryCode);
        log("Payment statuses before config -> " + statusBefore);

        step("Login into Payment V2 Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Navigate to Payment Display Custom Configuration → Non-IDR Vertical & Expiry Time");
        paymentDisplayCustomConfiguration.openNonIDRCustomConfiguration();

        step("Click Add New Expiry Config and fill form");
        paymentDisplayCustomConfiguration.createNewExpiryTimeWithConfig(productType, expiryMinutes);

        step("Toggle to active state");
        paymentDisplayCustomConfiguration.changeToggleStatus();

        step("Save the new expiry config");
        paymentDisplayCustomConfiguration.clickSave();

        step("Click Submit");
        paymentDisplayCustomConfiguration.clickSubmit();

        step("Verify the Active status");
        paymentDisplayCustomConfiguration.verifyExpiryTimeRowStatus(expiryMinutes, "Active");

        step("Click Edit button for the newly created expiry config");
        paymentDisplayCustomConfiguration.clickEditForExpiryTime(expiryMinutes);

        step("Expand currency section for " + paymentMethod);
        paymentDisplayCustomConfiguration.expandCurrencySection(paymentMethod);

        step("Set JUSPAY_CARD to Enable, GLOBAL_PAY to Hide, CAD_PAY to Disable");
        paymentDisplayCustomConfiguration.setPaymentMethodStatusForCurrency(paymentMethod, "JUSPAY_CARD", "Enable");
        paymentDisplayCustomConfiguration.setPaymentMethodStatusForCurrency(paymentMethod, "GLOBAL-PAYM", "Hide");
        paymentDisplayCustomConfiguration.setPaymentMethodStatusForCurrency(paymentMethod, "CAD-PAY", "Disable");

        step("Save updated payment method config");
        paymentDisplayCustomConfiguration.clickSave();

        step("Click Save Changes");
        paymentDisplayCustomConfiguration.clickSaveChanges();

        step("Wait for payment service cache to propagate config change");
        Thread.sleep(5000);

        step("Create new order in " + currency + " to verify updated statuses");
        String referenceId2 = hotelApi.createWaitingPaymentOrderApiByHotelId(bookingUsername, bookingPassword, startDate, hotelId, 1, 1, 1, currency, 0, "en");
        String orderHash2 = hotelApi.getOrderHash();
        log("referenceId2 -> " + referenceId2);
        log("orderHash2 -> " + orderHash2);

        step("Fetch payment method statuses after config change (" + currency + ")");
        Map<String, Boolean> statusAfter = paymentApi.getPaymentMethodStatuses(orderHash2, referenceId2, null, currency, countryCode);
        log("Payment statuses after config -> " + statusAfter);

        step("Assert CREDITCARD (maps to JUSPAY_CARD) is Enabled after config change");
        Assertion.assertThat("CREDITCARD should be Enabled", statusAfter.getOrDefault("CREDITCARD", false), is(true));

        step("Click Edit button to make config Inactive");
        paymentDisplayCustomConfiguration.clickEditForExpiryTime(expiryMinutes);

        step("Make Inactive the expiry config");
        paymentDisplayCustomConfiguration.changeToggleStatus();

        step("Save and Submit");
        paymentDisplayCustomConfiguration.clickSave();
        paymentDisplayCustomConfiguration.clickSaveChanges();

        step("Verify the Inactive status");
        paymentDisplayCustomConfiguration.verifyExpiryTimeRowStatus(expiryMinutes, "Inactive");

        step("Create new order after config set to Inactive — to verify revert to original state");
        String referenceId3 = hotelApi.createWaitingPaymentOrderApiByHotelId(bookingUsername, bookingPassword, startDate, hotelId, 1, 1, 1, currency, 0, "en");
        String orderHash3 = hotelApi.getOrderHash();
        log("referenceId3 -> " + referenceId3);
        log("orderHash3 -> " + orderHash3);

        step("Fetch payment method statuses after config set to Inactive");
        Map<String, Boolean> statusAfterRevert = paymentApi.getPaymentMethodStatuses(orderHash3, referenceId3, null, currency, countryCode);

        boolean creditCardStatusBefore = statusBefore.getOrDefault("CREDITCARD", false);
        boolean creditCardStatusAfterRevert = statusAfterRevert.getOrDefault("CREDITCARD", false);
        log("CREDITCARD status before -> " + creditCardStatusBefore);
        log("CREDITCARD status after revert -> " + creditCardStatusAfterRevert);

        step("Assert CREDITCARD status is back to original (same as before config was applied)");
        Assertion.assertThat("CREDITCARD status should match before state", creditCardStatusAfterRevert, is(creditCardStatusBefore));
    }
}