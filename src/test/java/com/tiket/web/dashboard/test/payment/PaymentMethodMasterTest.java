package com.tiket.web.dashboard.test.payment;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;

import static com.tiket.core.CoreFunctions.generateRandomDate;
import static com.tiket.core.CoreFunctions.getRandomObject;
import static com.tiket.core.CoreFunctions.randomName;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.Map;

import org.testng.annotations.Test;

import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.dashboard.page.payment.PaymentDashboardPage;
import com.tiket.web.dashboard.page.payment.PaymentMethods;

public class PaymentMethodMasterTest extends BaseTest {
    @Author(names = {"NaufalF"})
    @Module(name = "PaymentMethodMaster")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform - Sphinx")
    @Test
    public void testEditImportantInfo() throws InterruptedException {
        Map<String, String> param = getParam("testEditImportantInfo");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentMethods<?> paymentMethods = new PaymentMethods<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String paymentMethod = param.get("paymentMethod");
        String messageEnglish = param.get("message")+" - English - "+System.currentTimeMillis();
        String messageIndo = param.get("message")+ " - Indonesia - "+System.currentTimeMillis();

        step("Login to Payment Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);
        paymentDashboardPage.openPaymentsPage();

        step("Open Payment Methods Configuration");
        paymentMethods.openPaymentMethodsConfig();

        step("Open Configuration for Payment Method: "+paymentMethod);
        paymentMethods.openConfigPaymentMethod(paymentMethod);

        step("Edit Important Info");
        paymentMethods.editImportantInfo("English", messageEnglish);
        paymentMethods.editImportantInfo("Indonesia", messageIndo);

        step("Submit Changes");
        paymentMethods.saveChanges();
        paymentMethods.clickButton("Submit");

        step("Open Configuration for Payment Method: "+paymentMethod);
        paymentMethods.openConfigPaymentMethod(paymentMethod);

        step("Verify Important Info after Edit");
        Assertion.assertThat("Verify important info for English", paymentMethods.getImportantInfoText("English"), is(messageEnglish));
        Assertion.assertThat("Verify important info for Indonesia", paymentMethods.getImportantInfoText("Indonesia"), is(messageIndo));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "PaymentMethodMaster")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform - Sphinx")
    @Test
    public void testPaymentActivationPlatform() throws InterruptedException {
        Map<String, String> param = getParam("testPaymentActivationPlatform");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentMethods<?> paymentMethods = new PaymentMethods<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String paymentMethod = param.get("paymentMethod");
        String platform = param.get("platform");
        String message = param.get("message");

        step("Login to Payment Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);
        paymentDashboardPage.openPaymentsPage();

        step("Open Payment Methods Configuration");
        paymentMethods.openPaymentMethodsConfig();

        step("Click toggle for the first time");
        String query = "{ \"paymentName\": \""+paymentMethod+"\", \"type\": \"TIKET\" }";
        boolean isEnabled = paymentMethods.getStatusPerChannel(query, platform);
        paymentMethods.clickPlatformToggle(paymentMethod, platform);
        Assertion.assertThat("Success snackbar shown", paymentMethods.getSnackbarMessage(), containsString(message));
        Assertion.assertThat("Toggle status now is enabled: "+isEnabled, paymentMethods.getStatusPerChannel(query, platform), not(isEnabled));

        step("Click toggle for the second time");
        paymentMethods.clickPlatformToggle(paymentMethod, platform);
        Assertion.assertThat("Success snackbar shown", paymentMethods.getSnackbarMessage(), containsString(message));
        Assertion.assertThat("Toggle status now is enabled: "+isEnabled, paymentMethods.getStatusPerChannel(query, platform), is(isEnabled));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "PaymentMethodMaster")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform - Sphinx")
    @Test
    public void testPaymentActivationScheduler() throws InterruptedException {
        Map<String, String> param = getParam("testPaymentActivationScheduler");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentMethods<?> paymentMethods = new PaymentMethods<>(session);
        
        String username = param.get("username");
        String password = param.get("password");
        String paymentMethod = param.get("paymentMethod");
        String reason = param.get("reason") + "-" + System.currentTimeMillis();
        String message = param.get("message");
        
        step("Login to Payment Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);
        paymentDashboardPage.openPaymentsPage();

        step("Open Payment Methods Configuration");
        paymentMethods.openPaymentMethodsConfig();

        step("Open Configuration for Payment Method: "+paymentMethod);
        paymentMethods.openConfigPaymentMethod(paymentMethod);

        step("Click toggle Active Inactive");
        paymentMethods.clickToggleActiveInactive();
        String query = "{ \"paymentName\": \""+paymentMethod+"\", \"type\": \"TIKET\" }";
        boolean statusNow = paymentMethods.getPaymentStatus(query);
        
        if (statusNow == true) {
            step("Add reason for deactivation");
            paymentMethods.setReasonDeactivation(reason+"-bahasa", reason+"-english");
        }

        step("Submit Changes");
        paymentMethods.saveChanges();
        paymentMethods.clickButton("Submit");

        step("Verify changes");
        Assertion.assertThat("Success snackbar shown", paymentMethods.getSnackbarMessage(), containsString(message));
        Assertion.assertThat("Verify payment status", paymentMethods.getPaymentStatus(query), not(statusNow));

        step("Open Configuration for Payment Method: "+paymentMethod);
        paymentMethods.openConfigPaymentMethod(paymentMethod);

        step("Click toggle Active Inactive");
        paymentMethods.clickToggleActiveInactive();

        if (statusNow == false) {
            step("Add reason for deactivation");
            paymentMethods.setReasonDeactivation(reason+"-bahasa", reason+"-english");
        }

        step("Submit Changes");
        paymentMethods.saveChanges();
        paymentMethods.clickButton("Submit");

        step("Verify changes");
        Assertion.assertThat("Success snackbar shown", paymentMethods.getSnackbarMessage(), containsString(message));
        Assertion.assertThat("Verify payment status", paymentMethods.getPaymentStatus(query), is(statusNow));
    }

    @Author(names = {"Shubham Bansal"})
    @Module(name = "Payment")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Payment")
    @Test
    public void testPaymentMethodsInPaymentMethodMasterConfiguration() throws InterruptedException {
        Map<String, String> param = getParam("testPaymentMethodsInPaymentMethodMasterConfiguration");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentMethods<?> paymentMethods = new PaymentMethods<>(session);
        orderCreationPage<?> orderCreation = new orderCreationPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String currency = param.getOrDefault("currency", "IDR");
        String countryCode = param.getOrDefault("countryCode", "id");
        String paymentMethod = param.get("paymentMethod");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Login to Payment Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);
        paymentDashboardPage.openPaymentsPage();

        step("Open Payment Methods Configuration");
        paymentMethods.openPaymentMethodsConfig();

        step("Prepare payment method with active status and all platforms enabled");
        paymentMethod = paymentMethods.preparePaymentMethodForConfigurationTest(currency, paymentMethod);
        step("Using payment method: " + paymentMethod);

        step("Create waiting payment hotel order");
        orderCreation.createWaitingPaymentOrderHotel(userConsumer, passConsumer, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);
        String orderId = String.valueOf(orderCreation.getOrderId());
        String orderHash = orderCreation.getOrderHash();
        String accessToken = orderCreation.getAccessToken();

        step("Step 1 - Verify payment method status is true on all platforms via payment-group API");
        paymentMethods.verifyPaymentMethodEnabledOnAllPlatforms(
                orderHash, orderId, accessToken, paymentMethod, currency, countryCode
        );

        step("Step 2 - Disable each platform on panel and verify payment-group API status");
        paymentMethods.disableEachPlatformCumulativelyAndVerifyGroupApi(
                paymentMethod, orderHash, orderId, accessToken, currency, countryCode
        );

        step("Step 3 - Verify all platforms disabled on panel and payment-group API status is false on all platforms");
        paymentMethods.verifyAllPlatformTogglesEnabled(paymentMethod, false);
        paymentMethods.verifyPaymentMethodDisabledOnAllPlatforms(
                orderHash, orderId, accessToken, paymentMethod, currency, countryCode
        );

        step("Step 4 - Deactivate payment method via Set schedule when all platforms are enabled");
        paymentMethods.setAllPlatformToggles(paymentMethod, true);
        paymentMethods.openSetSchedule(paymentMethod);
        Assertion.assertThat(
                "Verify Active / Inactive toggle is enabled when all platforms are enabled",
                paymentMethods.isActiveInactiveToggleClickable(),
                is(true)
        );
        Assertion.assertThat(
                "Verify Active / Inactive toggle is on before deactivation",
                paymentMethods.isActiveInactiveToggleOn(),
                is(true)
        );
        paymentMethods.deactivatePaymentMethodOnSchedule("deactivate-bahasa", "deactivate-english");
        paymentMethods.verifyPaymentListingStatus(paymentMethod, "Inactive");
        paymentMethods.verifyPaymentMethodDisabledOnAllPlatforms(
                orderHash, orderId, accessToken, paymentMethod, currency, countryCode
        );

        step("Step 5 - Activate payment method via Set schedule");
        paymentMethods.openSetSchedule(paymentMethod);
        paymentMethods.activatePaymentMethodOnSchedule();
        paymentMethods.verifyPaymentListingStatus(paymentMethod, "Active");
        paymentMethods.verifyPaymentMethodEnabledOnAllPlatforms(
                orderHash, orderId, accessToken, paymentMethod, currency, countryCode
        );

        step("Step 6 - Verify Set Deactivation Schedule and Important Info");
        String messageEnglish = "English - " + System.currentTimeMillis();
        String messageIndo = "Indonesia - " + System.currentTimeMillis();
        paymentMethods.openSetSchedule(paymentMethod);
        paymentMethods.setSetDeactivationScheduleToggle(true);
        paymentMethods.verifyDeactivationScheduleElementsVisible();
        paymentMethods.setSetDeactivationScheduleToggle(false);
        paymentMethods.verifyImportantInfoSectionVisible();
        paymentMethods.editImportantInfo("English", messageEnglish);
        paymentMethods.editImportantInfo("Indonesia", messageIndo);
        paymentMethods.submitScheduleChanges();
    }
}
