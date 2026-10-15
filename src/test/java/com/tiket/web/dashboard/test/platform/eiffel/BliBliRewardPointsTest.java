package com.tiket.web.dashboard.test.platform.eiffel;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.eiffel.loyalty.LoyaltyPointsPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class BliBliRewardPointsTest extends BaseTest {
    @Author(names = { "Saurabh" })
    @Module(name = "Loyalty")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void searchBlibliTiketPointInformationFromEmail() {
        Map<String, String> param = getParam("searchBlibliTiketPointInformationFromEmail");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoyaltyPointsPage<?> loyaltyPointsPage = new LoyaltyPointsPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String inputEmailId = param.get("email_id");

        step("Open the Admin Care Page");
        loyaltyPointsPage.goToUserManagementPage(username, password);

        step("Open the Loyalty Page");
        loyaltyPointsPage.clickLoyaltyIcon();

        step("Open the Blibli Reward Dashboard Page");
        loyaltyPointsPage.bliblitiketrewardsDashboard();

        step("Select the drop down for Email/PhoneNumber");
        loyaltyPointsPage.selectDropDown();

        step("Select The email from the email");
        loyaltyPointsPage.selectEmail();

        step("Send the email Id");
        loyaltyPointsPage.inputEmailId(inputEmailId);

        step("Click on Search icon");
        loyaltyPointsPage.clickOnSearch();

        step("Verify email Should be same");
        Assertion.assertThat("Email_Id should be same", loyaltyPointsPage.verifyEmailId(inputEmailId),is(true));

        step("Verify Tiket Points are showing");
        Assertion.assertThat("Verify Tiket Points are showing", loyaltyPointsPage.checkTiketPoints(),is(true));

        step("Verify Tiket Points & Worth should be equal");
        Assertion.assertThat("Tiket Points & Worth amount should be equal", loyaltyPointsPage.checkTiketPointsWorth(), is(true));

        step("Verify that status points are showing");
        Assertion.assertThat("Verify that status points are showing", loyaltyPointsPage.checkStatusPoints(), is(true));
    }

    @Author(names = "Saurabh")
    @Module(name = "Loyalty")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void searchBlibliTiketPointInformationFromPhoneNumber() {
        Map<String, String> param = getParam("searchBlibliTiketPointInformationFromPhoneNumber");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoyaltyPointsPage<?> loyaltyPointsPage = new LoyaltyPointsPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String phoneNumber = param.get("phoneNumber");

        step("Open the Admin Care Page");
        loyaltyPointsPage.goToUserManagementPage(username, password);

        step("Open the Loyalty Page");
        loyaltyPointsPage.clickLoyaltyIcon();

        step("Open the Blibli Reward Dashboard Page");
        loyaltyPointsPage.bliblitiketrewardsDashboard();

        step("Select the drop down for Email/PhoneNumber");
        loyaltyPointsPage.selectDropDown();

        step("Select The email from the email");
        loyaltyPointsPage.selectPhoneNumber();

        step("Send the Phone Number");
        loyaltyPointsPage.inputPhoneNumber(phoneNumber);

        step("Click on Search icon");
        loyaltyPointsPage.clickOnSearch();

        step("Verify Phone Number");
        Assertion.assertThat("Email_Id should be same", loyaltyPointsPage.verifyPhoneNumber(phoneNumber),is(true));

        step("Verify Tiket Points are showing");
        Assertion.assertThat("Verify Tiket Points are showing", loyaltyPointsPage.checkTiketPoints(),is(true));

        step("Verify Tiket Points & Worth should be equal");
        Assertion.assertThat("Tiket Points & Worth amount should be equal", loyaltyPointsPage.checkTiketPointsWorth(), is(true));

        step("Verify that status points are showing");
        Assertion.assertThat("Verify that status points are showing", loyaltyPointsPage.checkStatusPoints(), is(true));
    }
}
