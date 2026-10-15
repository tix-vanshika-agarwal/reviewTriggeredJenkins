package com.tiket.web.dashboard.test.payment;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.pojo.payment.PromoNudgeResponse;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.dashboard.page.payment.DynamicPromoNudgesPage;
import com.tiket.web.dashboard.page.payment.PaymentDashboardPage;
import com.tiket.web.dashboard.page.payment.PaymentMethods;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static com.tiket.core.CoreFunctions.generateRandomDate;
import static com.tiket.core.CoreFunctions.getRandomObject;
import static com.tiket.core.CoreFunctions.randomName;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class DynamicPromoNudgesTest extends BaseTest {

    @Author(names = {"Shubham Bansal"})
    @Module(name = "Payment")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Payment")
    @Test
    public void testDynamicPromoNudgesConfiguration() {
        Map<String, String> param = getParam("testDynamicPromoNudgesConfiguration");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        DynamicPromoNudgesPage<?> dynamicPromoNudgesPage = new DynamicPromoNudgesPage<>(session);
        PaymentMethods<?> paymentMethods = new PaymentMethods<>(session);
        orderCreationPage<?> orderCreation = new orderCreationPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String currency = param.getOrDefault("currency", "IDR");
        String countryCode = param.getOrDefault("countryCode", "id");
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

        step("Open Dynamic Promo Nudges page");
        dynamicPromoNudgesPage.openDynamicPromoNudgesPage();

        step("Create waiting payment hotel order");
        orderCreation.createWaitingPaymentOrderHotel(userConsumer, passConsumer, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);
        String orderId = String.valueOf(orderCreation.getOrderId());
        String orderHash = orderCreation.getOrderHash();
        String accessToken = orderCreation.getAccessToken();

        PromoNudgeResponse.PromoNudge activePromoNudge = dynamicPromoNudgesPage.findFirstActivePromoNudge(currency);
        String activePaymentMethod;
        String updatedMessageEn;

        if (activePromoNudge != null) {
            activePaymentMethod = activePromoNudge.paymentMethod;

            String prepareMessageEn = dynamicPromoNudgesPage.getCustomTextEn(activePromoNudge);
            if (!dynamicPromoNudgesPage.isFullyActive(activePromoNudge) || prepareMessageEn.isBlank()) {
                step("Enable all platforms and activate custom message for: " + activePaymentMethod);
                if (prepareMessageEn.isBlank()) {
                    prepareMessageEn = "Custom EN - " + System.currentTimeMillis();
                }
                String prepareMessageId = "Custom ID - " + System.currentTimeMillis();
                dynamicPromoNudgesPage.prepareActivePromoNudgeConfiguration(activePaymentMethod, prepareMessageEn, prepareMessageId);
                activePromoNudge = dynamicPromoNudgesPage.getPromoNudgeByPaymentMethod(currency, activePaymentMethod);
            }

            String initialMessageEn = dynamicPromoNudgesPage.getCustomTextEn(activePromoNudge);

            step("Verify custom message on enabled platforms via payment-group API");
            paymentMethods.verifyCustomPromoMessageOnPlatforms(
                    orderHash, orderId, accessToken, activePaymentMethod, initialMessageEn, currency, countryCode,
                    true, true, true, true
            );

            step("Disable iOS and Mobile, update custom message");
            dynamicPromoNudgesPage.openEditConfiguration(activePaymentMethod);
            updatedMessageEn = "Updated EN - " + System.currentTimeMillis();
            String updatedMessageId = "Updated ID - " + System.currentTimeMillis();
            dynamicPromoNudgesPage.setCustomMessage(updatedMessageEn, updatedMessageId);
            dynamicPromoNudgesPage.saveConfiguration();

            step("Disable iOS and Mobile from listing page");
            dynamicPromoNudgesPage.setPlatformToggle(activePaymentMethod, "iOS", false, currency);
            dynamicPromoNudgesPage.setPlatformToggle(activePaymentMethod, "Mobile", false, currency);

            step("Verify updated configuration on listing page");
            Assertion.assertThat("Custom message EN on listing", dynamicPromoNudgesPage.getListingCustomMessageEn(activePaymentMethod), is(updatedMessageEn));
            Assertion.assertThat("Custom message ID on listing", dynamicPromoNudgesPage.getListingCustomMessageId(activePaymentMethod), is(updatedMessageId));
            dynamicPromoNudgesPage.verifyShowCustomMessageStatus(activePaymentMethod, "Active");
            Assertion.assertThat("iOS platform disabled on listing", dynamicPromoNudgesPage.isPlatformToggleEnabled(activePaymentMethod, "iOS"), is(false));
            Assertion.assertThat("Mobile platform disabled on listing", dynamicPromoNudgesPage.isPlatformToggleEnabled(activePaymentMethod, "Mobile"), is(false));

            step("Verify updated custom message on enabled platforms via payment-group API");
            paymentMethods.verifyCustomPromoMessageOnPlatforms(
                    orderHash, orderId, accessToken, activePaymentMethod, updatedMessageEn, currency, countryCode,
                    false, true, false, true
            );
        } else {
            PromoNudgeResponse.PromoNudge inactivePromoNudge = dynamicPromoNudgesPage.findFirstInactivePromoNudge(currency);
            Assertion.assertThat("At least one inactive payment method exists", inactivePromoNudge != null, is(true));
            activePaymentMethod = inactivePromoNudge.paymentMethod;

            step("Activate nudge config by enabling custom message for: " + activePaymentMethod);
            dynamicPromoNudgesPage.openEditConfiguration(activePaymentMethod);
            dynamicPromoNudgesPage.setAllPlatformTogglesOnEditPage(true);
            String prepareMessageEn = dynamicPromoNudgesPage.getCustomTextEn(inactivePromoNudge);
            if (prepareMessageEn.isBlank()) {
                prepareMessageEn = "Custom EN - " + System.currentTimeMillis();
            }
            String prepareMessageId = "Custom ID - " + System.currentTimeMillis();
            dynamicPromoNudgesPage.setCustomMessage(prepareMessageEn, prepareMessageId);
            dynamicPromoNudgesPage.saveConfiguration();

            updatedMessageEn = prepareMessageEn;

            step("Verify custom message on all platforms via payment-group API");
            paymentMethods.verifyCustomPromoMessageOnPlatforms(
                    orderHash, orderId, accessToken, activePaymentMethod, prepareMessageEn, currency, countryCode,
                    true, true, true, true
            );
        }

        step("Add new payment method and cancel without saving");
        List<String> configuredMethods = dynamicPromoNudgesPage.getConfiguredPaymentMethodsFromApi(currency);
        dynamicPromoNudgesPage.clickAddPaymentMethod();
        dynamicPromoNudgesPage.selectFirstUnconfiguredPaymentMethod(configuredMethods);
        String draftMessageEn = "Draft EN - " + System.currentTimeMillis();
        String draftMessageId = "Draft ID - " + System.currentTimeMillis();
        dynamicPromoNudgesPage.setCustomMessage(draftMessageEn, draftMessageId);
        dynamicPromoNudgesPage.setPlatformToggleOnEditPage("Android", true);
        dynamicPromoNudgesPage.setPlatformToggleOnEditPage("Desktop", true);
        dynamicPromoNudgesPage.setPlatformToggleOnEditPage("iOS", false);
        dynamicPromoNudgesPage.setPlatformToggleOnEditPage("Mobile", false);
        dynamicPromoNudgesPage.clickSaveAndCancelWithoutSaving();

        step("Disable all platforms from listing page on active payment method: " + activePaymentMethod);
        dynamicPromoNudgesPage.setAllPlatformToggles(activePaymentMethod, false, currency);
        dynamicPromoNudgesPage.verifyAllPlatformTogglesEnabled(activePaymentMethod, false);
        paymentMethods.verifyCustomPromoMessageOnPlatforms(
                orderHash, orderId, accessToken, activePaymentMethod, updatedMessageEn, currency, countryCode,
                false, false, false, false
        );

        step("Enable all platforms from listing page on active payment method: " + activePaymentMethod);
        dynamicPromoNudgesPage.setAllPlatformToggles(activePaymentMethod, true, currency);
        dynamicPromoNudgesPage.verifyAllPlatformTogglesEnabled(activePaymentMethod, true);
        paymentMethods.verifyCustomPromoMessageOnPlatforms(
                orderHash, orderId, accessToken, activePaymentMethod, updatedMessageEn, currency, countryCode,
                true, true, true, true
        );

        step("Deactivate custom message config when all platforms are enabled");
        dynamicPromoNudgesPage.deactivateCustomMessageConfiguration(activePaymentMethod);
        dynamicPromoNudgesPage.verifyShowCustomMessageStatus(activePaymentMethod, "Inactive");
        PromoNudgeResponse.PromoNudge deactivatedPromoNudge = dynamicPromoNudgesPage.getPromoNudgeByPaymentMethod(currency, activePaymentMethod);
        Assertion.assertThat("enableCustom is false in promo-nudge API", deactivatedPromoNudge.enableCustom, is(false));
        paymentMethods.verifyCustomPromoMessageOnPlatforms(
                orderHash, orderId, accessToken, activePaymentMethod, updatedMessageEn, currency, countryCode,
                false, false, false, false
        );

        step("Activate custom message config again");
        dynamicPromoNudgesPage.activateCustomMessageConfiguration(activePaymentMethod);
        dynamicPromoNudgesPage.verifyShowCustomMessageStatus(activePaymentMethod, "Active");
        PromoNudgeResponse.PromoNudge reactivatedPromoNudge = dynamicPromoNudgesPage.getPromoNudgeByPaymentMethod(currency, activePaymentMethod);
        Assertion.assertThat("enableCustom is true in promo-nudge API", reactivatedPromoNudge.enableCustom, is(true));
        paymentMethods.verifyCustomPromoMessageOnPlatforms(
                orderHash, orderId, accessToken, activePaymentMethod, updatedMessageEn, currency, countryCode,
                true, true, true, true
        );
    }
}
