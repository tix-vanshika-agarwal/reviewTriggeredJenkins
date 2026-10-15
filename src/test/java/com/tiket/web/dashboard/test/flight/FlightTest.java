package com.tiket.web.dashboard.test.flight;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.flight.*;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

public class FlightTest extends BaseTest {
    @Author(names = {"Abhishek"})
    @Module(name = "CountryDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testCreateNewCountry() {
        Map<String, String> param = getParam("testCreateNewCountry");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCountryDashboardPage<?> flightCountryDashboardPage = new FlightCountryDashboardPage<>(session);
        FlightAddCountryFormPage<?> flightAddCountryFormPage = new FlightAddCountryFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard.");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(username, password);

        step("The user navigates to the Flight Country dashboard and click on add new country button.");
        flightCountryDashboardPage.isFlightLoggedIn();
        flightCountryDashboardPage.clickAddCountryBtn();

        step("The user fills the country details and saves the country.");
        flightAddCountryFormPage.fillCountryDetails(param.get("countryCode"), param.get("countryName"), param.get("countryCurrency"));

        step("The user verify checks the added country.");
        Assertion.assertThat("New Added Country is showing in list", flightCountryDashboardPage.checkCountry(param.get("countryCode"), param.get("countryName"), param.get("countryCurrency")), is(true));

        step("The user deletes the added country.");
        flightCountryDashboardPage.deleteCountry();
    }

    @Author(names = {"Abhishek"})
    @Module(name = "CountryDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testDeleteCountry() {
        Map<String, String> param = getParam("testDeleteCountry");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCountryDashboardPage<?> flightCountryDashboardPage = new FlightCountryDashboardPage<>(session);
        FlightAddCountryFormPage<?> flightAddCountryFormPage = new FlightAddCountryFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Country dashboard and click on add new country button.");
        flightCountryDashboardPage.isFlightLoggedIn();
        flightCountryDashboardPage.clickAddCountryBtn();

        step("The user fills the country details and saves the country.");
        flightAddCountryFormPage.fillCountryDetails(param.get("countryCode"), param.get("countryName"), param.get("countryCurrency"));

        step("The user deletes the added country.");
        flightCountryDashboardPage.deleteCountry();

        step("The user verify checks the country is deleted");
        Assertion.assertThat("Verify Country is not showing in list", flightCountryDashboardPage.checkCountryDeleted(param.get("countryCode"), param.get("countryName"), param.get("countryCurrency")), is(true));
    }

    @Author(names = {"Ram Pratap Singh","Shubham Bansal"})
    @Module(name = "AIRPORT DASHBOARD")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testCreateNewAirport() {
        Map<String, String> param = getParam("testCreateNewAirport");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightAddAirport<?> flightAddAirport = new FlightAddAirport<>(session);
        FlightAirportDashboardPage<?> flightAirportDashboardPage = new FlightAirportDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Airport dashboard, deletes existing airport by code if present, then clicks add new.");
        flightAirportDashboardPage.clickAirport();
        flightAirportDashboardPage.deleteAirportIfExists(param.get("code"));
        flightAirportDashboardPage.clickAddCountryBtn();

        step("The user fill the details.");
        flightAddAirport.fillAirportDetails(param.get("content"),param.get("alias"),param.get("code"),param.get("latitude"),param.get("longitude"));

        step("The user verify the added airport.");
        Assertion.assertThat("New Added airport is showing in list", flightAirportDashboardPage.checkAddedAirport(param.get("code"), param.get("content")), is(true));

        step("The user deletes the added airport.");
        flightAirportDashboardPage.deleteAirport(param.get("code"));
    }

    @Author(names = {"Ram Pratap Singh","Shubham Bansal"})
    @Module(name = "AIRPORT DASHBOARD")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testUpdateAirport() {
        Map<String, String> param = getParam("testUpdateAirport");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightAddAirport<?> flightAddAirport = new FlightAddAirport<>(session);
        FlightAirportDashboardPage<?> flightAirportDashboardPage = new FlightAirportDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Airport dashboard, deletes existing airport by code if present, then clicks add new.");
        flightAirportDashboardPage.clickAirport();
        flightAirportDashboardPage.deleteAirportIfExists(param.get("code"));
        flightAirportDashboardPage.clickAddCountryBtn();

        step("The user fill the details.");
        flightAddAirport.fillAirportDetails(param.get("content"),param.get("alias"),param.get("code"),param.get("latitude"),param.get("longitude"));

        step("The user updated the Airport details.");
        flightAddAirport.updateAirport(param.get("content1"),param.get("alias1"),param.get("code"),param.get("latitude1"),param.get("longitude1"));

        step("The user verify updated airport.");
        Assertion.assertThat("Updated airport is showing in list", flightAirportDashboardPage.checkAddedAirport(param.get("code"), param.get("content1")), is(true));

        step("The user deletes the added airport.");
        flightAirportDashboardPage.deleteAirport(param.get("code"));
    }

    @Author(names = {"Ram Pratap Singh","Shubham Bansal"})
    @Module(name = "AIRPORT DASHBOARD")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testDeletedAirport() {
        Map<String, String> param = getParam("testDeletedAirport");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightAddAirport<?> flightAddAirport = new FlightAddAirport<>(session);
        FlightAirportDashboardPage<?> flightAirportDashboardPage = new FlightAirportDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Airport dashboard, deletes existing airport by code if present, then clicks add new.");
        flightAirportDashboardPage.clickAirport();
        flightAirportDashboardPage.deleteAirportIfExists(param.get("code"));
        flightAirportDashboardPage.clickAddCountryBtn();

        step("The user fill the details.");
        flightAddAirport.fillAirportDetails(param.get("content"),param.get("alias"),param.get("code"),param.get("latitude"),param.get("longitude"));

        step("The user deletes the added airport.");
        flightAirportDashboardPage.deleteAirport(param.get("code"));

        step("The new added airport is not showing");
        Assertion.assertThat("New Added airport is not showing in list", flightAirportDashboardPage.checkAirportDeleted(param.get("code"), param.get("content")), is(true));
    }

    @Author(names = {"Ram Pratap Singh","Shubham Bansal"})
    @Module(name = "AIRPORT DASHBOARD")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testReadAirport() {
        Map<String, String> param = getParam("testReadAirport");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightAddAirport<?> flightAddAirport = new FlightAddAirport<>(session);
        FlightAirportDashboardPage<?> flightAirportDashboardPage = new FlightAirportDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Airport dashboard, deletes existing airport by code if present, then clicks add new.");
        flightAirportDashboardPage.clickAirport();
        flightAirportDashboardPage.deleteAirportIfExists(param.get("code"));
        flightAirportDashboardPage.clickAddCountryBtn();

        step("The user fill the details.");
        flightAddAirport.fillAirportDetails(param.get("content"),param.get("alias"),param.get("code"),param.get("latitude"),param.get("longitude"));

        step("The user reads airport.");
        flightAirportDashboardPage.readAirport();

        step("The user deletes the added airport.");
        flightAirportDashboardPage.deleteAirport(param.get("code"));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "DashboardAdjustment")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testUpdateAdjustment() {
        Map<String, String> param = getParam("testUpdateAdjustment");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCampaignDashboardPage<?> flightCampaignDashboardPage = new FlightCampaignDashboardPage<>(session);
        FlightPriceAdjustmentDetailPage<?> flightPriceAdjustmentDetailPage = new FlightPriceAdjustmentDetailPage<>(session);
        FlightPriceAdjustmentAddNewFormPage<?> flightPriceAdjustmentAddNewFormPage = new FlightPriceAdjustmentAddNewFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Campaigns Dashboard");
        flightCampaignDashboardPage.isFlightLoggedIn();
        flightCampaignDashboardPage.clickPriceAdjustmentDetails();

        step("The user click on Add new adjustment button");
        flightPriceAdjustmentDetailPage.clickAddNewAdjustmentButton();

        String supplier = param.get("supplier");
        String airlineClass = param.get("airlineClass");
        String airline = param.get("airline");
        String countryOrigin = param.get("countryOrigin");
        String countryDestination = param.get("countryDestination");
        String transit = param.get("transit");
        String airportOrigin = param.get("airportOrigin");
        String airportDestination = param.get("airportDestination");
        String minPrice = param.get("minPrice");
        String maxPrice = param.get("maxPrice");
        String minDiscount = param.get("minDiscount");
        String maxDiscount = param.get("maxDiscount");
        String markupValue = param.get("markupValue");

        step("The user fill in the form with valid data");
        flightPriceAdjustmentAddNewFormPage.fillAirlineDetails(supplier, airlineClass, airline);
        flightPriceAdjustmentAddNewFormPage.fillTripDetails(countryOrigin, countryDestination, transit, airportOrigin, airportDestination);
        flightPriceAdjustmentAddNewFormPage.fillAdjustmentRule(minPrice, maxPrice, minDiscount, maxDiscount, markupValue);

        step("The user click on submit button");
        flightPriceAdjustmentAddNewFormPage.clickSubmitBtn();

        String status = param.get("status");
        String adjustmentBtn = param.get("adjustmentBtn");

        String supplier1 = param.get("supplier1");
        String airlineClass1 = param.get("airlineClass1");
        String airline1 = param.get("airline1");
        String countryOrigin1 = param.get("countryOrigin1");
        String countryDestination1 = param.get("countryDestination1");
        String transit1 = param.get("transit1");
        String airportOrigin1 = param.get("airportOrigin1");
        String airportDestination1 = param.get("airportDestination1");
        String minPrice1 = param.get("minPrice1");
        String maxPrice1 = param.get("maxPrice1");
        String minDiscount1 = param.get("minDiscount1");
        String maxDiscount1 = param.get("maxDiscount1");
        String markupValue1 = param.get("markupValue1");

        step("The user updates the Adjustment");
        flightPriceAdjustmentAddNewFormPage.updateAdjustment();

        flightPriceAdjustmentAddNewFormPage.fillAirlineDetails(supplier1, airlineClass1, airline1);
        flightPriceAdjustmentAddNewFormPage.fillTripDetails(countryOrigin1, countryDestination1, transit1, airportOrigin1, airportDestination1);
        flightPriceAdjustmentAddNewFormPage.editAdjustmentRule(minPrice1, maxPrice1, minDiscount1, maxDiscount1, markupValue1);
        flightPriceAdjustmentAddNewFormPage.clickSubmitBtn();

        step("The user checks whether adjustment is updated");
        Assertion.assertThat("Updated Adjustment is showing in list", flightPriceAdjustmentDetailPage.checkIfAdjustmentCreated(status, adjustmentBtn), is(true));

        step("The user delete the adjustment");
        flightPriceAdjustmentDetailPage.deleteAdjustment();
    }

    @Author(names = {"Akbar"})
    @Module(name = "CampaignDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testCreateNewCampaign() {
        Map<String, String> param = getParam("testCreateNewCampaign");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCampaignDashboardPage<?> flightCampaignDashboardPage = new FlightCampaignDashboardPage<>(session);
        FlightAddCampaignFormPage<?> flightAddCampaignFormPage = new FlightAddCampaignFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String campaignName = param.get("campaignName") + " " + System.currentTimeMillis();
        String priority = param.get("priority");
        String campaignType = param.get("campaignType");
        String campaignTemplateName = param.get("campaignTemplateName");
        String masterTag = param.get("masterTag");
        String campaignStartDateRange = param.get("campaignStartDateRange");
        String campaignDuration = param.get("campaignDuration");
        String campaignBudget = param.get("campaignBudget");
        String customerTarget = param.get("customerTarget");
        String userType = param.get("userType");
        String loyaltyLevel = param.get("loyaltyLevel");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Campaign dashboard and click on add new campaign button.");
        flightCampaignDashboardPage.goToFlightCampaignDashboardPage();
        flightCampaignDashboardPage.isFlightLoggedIn();
        flightCampaignDashboardPage.clickAddNewCampaignBtn();

        step("The user fills the campaign details and submit it.");
        Map<String, String> createdCampaign = flightAddCampaignFormPage.fillCampaignDetails(campaignName, priority, campaignType, campaignTemplateName, masterTag, campaignStartDateRange, campaignDuration, campaignBudget, customerTarget, userType, loyaltyLevel);
        flightAddCampaignFormPage.clickSubmit();

        Map<String, String> displayedCampaign = flightCampaignDashboardPage.getDisplayedCampaignData(createdCampaign);

        step("The user deletes the added campaign.");
        flightCampaignDashboardPage.deleteCampaign(campaignName);

        step("The user verify checks the added campaign.");
        Assertion.assertThat("Campaign Name is exists on page", flightCampaignDashboardPage.checkCampaignNameExist(createdCampaign.get("campaignName")), is(true));
        Assertion.assertThat("Campaign Type is matched", displayedCampaign.get("campaignType"), is(createdCampaign.get("campaignType")));
        Assertion.assertThat("User Type is matched", displayedCampaign.get("userType"), containsString(createdCampaign.get("userType")));
        Assertion.assertThat("Platform is matched", displayedCampaign.get("platform"), is(createdCampaign.get("platform")));
        Assertion.assertThat("Campaign Budget is matched", displayedCampaign.get("campaignBudget"), is(createdCampaign.get("campaignBudget")));
        Assertion.assertThat("Start Date is matched", displayedCampaign.get("startDate"), containsString(createdCampaign.get("startDate")));
        Assertion.assertThat("End Date is matched", displayedCampaign.get("endDate"), containsString(createdCampaign.get("endDate")));
        Assertion.assertThat("Priority is matched", displayedCampaign.get("priority"), is(createdCampaign.get("priority")));
    }

    @Author(names = {"Akbar"})
    @Module(name = "CampaignDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testDeleteCampaign() {
        Map<String, String> param = getParam("testDeleteCampaign");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCampaignDashboardPage<?> flightCampaignDashboardPage = new FlightCampaignDashboardPage<>(session);
        FlightAddCampaignFormPage<?> flightAddCampaignFormPage = new FlightAddCampaignFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String campaignName = param.get("campaignName") + " " + System.currentTimeMillis();
        String priority = param.get("priority");
        String campaignType = param.get("campaignType");
        String campaignTemplateName = param.get("campaignTemplateName");
        String masterTag = param.get("masterTag");
        String campaignStartDateRange = param.get("campaignStartDateRange");
        String campaignDuration = param.get("campaignDuration");
        String campaignBudget = param.get("campaignBudget");
        String customerTarget = param.get("customerTarget");
        String userType = param.get("userType");
        String loyaltyLevel = param.get("loyaltyLevel");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Campaign dashboard and click on add new campaign button.");
        flightCampaignDashboardPage.goToFlightCampaignDashboardPage();
        flightCampaignDashboardPage.isFlightLoggedIn();
        flightCampaignDashboardPage.clickAddNewCampaignBtn();

        step("The user fills the campaign details and submit it.");
        Map<String, String> createdCampaign = flightAddCampaignFormPage.fillCampaignDetails(campaignName, priority, campaignType, campaignTemplateName, masterTag, campaignStartDateRange, campaignDuration, campaignBudget, customerTarget, userType, loyaltyLevel);
        flightAddCampaignFormPage.clickSubmit();
        if (!flightCampaignDashboardPage.checkCampaignNameExist(createdCampaign.get("campaignName"))) {
            Assertion.skip("Campaign is not exist");
        }

        step("The user deletes the added campaign.");
        flightCampaignDashboardPage.deleteCampaign(campaignName);
        Boolean snackBarDisplayed = flightCampaignDashboardPage.isSuccessSnackBarDisplayed();

        step("The user verify checks the added campaign.");
        Assertion.assertThat("Success Snackbar is displayed after successfully created new campaign", snackBarDisplayed, is(true));
        Assertion.assertThat("Campaign Name is exists on page", flightCampaignDashboardPage.checkDeletedCampaignNameExist(createdCampaign.get("campaignName")), is(false));
    }

    @Author(names = {"Akbar"})
    @Module(name = "CampaignDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testUpdateCampaign() {
        Map<String, String> param = getParam("testUpdateCampaign");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCampaignDashboardPage<?> flightCampaignDashboardPage = new FlightCampaignDashboardPage<>(session);
        FlightAddCampaignFormPage<?> flightAddCampaignFormPage = new FlightAddCampaignFormPage<>(session);
        FlightEditCampaignFormPage<?> flightEditCampaignFormPage = new FlightEditCampaignFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String campaignName = param.get("campaignName") + " " + System.currentTimeMillis();
        String priority = param.get("priority");
        String campaignType = param.get("campaignType");
        String campaignTemplateName = param.get("campaignTemplateName");
        String masterTag = param.get("masterTag");
        String campaignStartDateRange = param.get("campaignStartDateRange");
        String campaignDuration = param.get("campaignDuration");
        String campaignBudget = param.get("campaignBudget");
        String customerTarget = param.get("customerTarget");
        String userType = param.get("userType");
        String loyaltyLevel = param.get("loyaltyLevel");

        String changedPriority = param.get("changedPriority");
        String changedCampaignTemplateName = param.get("changedCampaignTemplateName");
        String changedMasterTag = param.get("changedMasterTag");
        String changedCampaignStartDateRange = param.get("changedCampaignStartDateRange");
        String changedCampaignDuration = param.get("changedCampaignDuration");
        String changedCampaignBudget = param.get("changedCampaignBudget");
        String changedCustomerTarget = param.get("changedCustomerTarget");
        String changedUserType = param.get("changedUserType");
        String changedLoyaltyLevel = param.get("changedLoyaltyLevel");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Campaign dashboard and click on add new campaign button.");
        flightCampaignDashboardPage.goToFlightCampaignDashboardPage();
        flightCampaignDashboardPage.isFlightLoggedIn();
        flightCampaignDashboardPage.clickAddNewCampaignBtn();

        step("The user fills the campaign details and submit it.");
        Map<String, String> createdCampaign = flightAddCampaignFormPage.fillCampaignDetails(campaignName, priority, campaignType, campaignTemplateName, masterTag, campaignStartDateRange, campaignDuration, campaignBudget, customerTarget, userType, loyaltyLevel);
        flightAddCampaignFormPage.clickSubmit();
        if (!flightCampaignDashboardPage.checkCampaignNameExist(createdCampaign.get("campaignName"))) {
            Assertion.skip("Campaign is not exist");
        }

        step("The user update the campaign details and submit it.");
        flightCampaignDashboardPage.goToEditPage(createdCampaign.get("campaignName"));
        Map<String, String> updatedCampaign = flightEditCampaignFormPage.editCampaignDetails(changedPriority, changedCampaignTemplateName, changedMasterTag, changedCampaignStartDateRange, changedCampaignDuration, changedCampaignBudget, changedCustomerTarget, changedUserType, changedLoyaltyLevel);
        flightEditCampaignFormPage.clickSubmit();

        Map<String, String> displayedUpdatedCampaign = flightCampaignDashboardPage.getDisplayedCampaignData(createdCampaign);

        step("The user deletes the updated campaign.");
        flightCampaignDashboardPage.deleteCampaign(campaignName);

        step("The user verify checks the updated campaign.");
        Assertion.assertThat("Campaign Name is exists on page", flightCampaignDashboardPage.checkCampaignNameExist(createdCampaign.get("campaignName")), is(true));
        Assertion.assertThat("Campaign Type is matched", displayedUpdatedCampaign.get("campaignType"), is(createdCampaign.get("campaignType")));
        Assertion.assertThat("User Type is matched", displayedUpdatedCampaign.get("userType"), containsString(updatedCampaign.get("userType")));
        Assertion.assertThat("Platform is matched", displayedUpdatedCampaign.get("platform"), is(updatedCampaign.get("platform")));
        Assertion.assertThat("Campaign Budget is matched", displayedUpdatedCampaign.get("campaignBudget"), is(updatedCampaign.get("campaignBudget")));
        Assertion.assertThat("Start Date is matched", displayedUpdatedCampaign.get("startDate"), containsString(updatedCampaign.get("startDate")));
        Assertion.assertThat("End Date is matched", displayedUpdatedCampaign.get("endDate"), containsString(updatedCampaign.get("endDate")));
        Assertion.assertThat("Priority is matched", displayedUpdatedCampaign.get("priority"), is(updatedCampaign.get("priority")));
    }
    @Author(names = {"Abhishek"})
    @Module(name = "DashboardAdjustment")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testCreateAdjustment() {
        Map<String, String> param = getParam("testCreateAdjustment");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCampaignDashboardPage<?> flightCampaignDashboardPage = new FlightCampaignDashboardPage<>(session);
        FlightPriceAdjustmentDetailPage<?> flightPriceAdjustmentDetailPage = new FlightPriceAdjustmentDetailPage<>(session);
        FlightPriceAdjustmentAddNewFormPage<?> flightPriceAdjustmentAddNewFormPage = new FlightPriceAdjustmentAddNewFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Campaigns Dashboard");
        flightCampaignDashboardPage.isFlightLoggedIn();
        flightCampaignDashboardPage.clickPriceAdjustmentDetails();

        step("The user click on Add new adjustment button");
        flightPriceAdjustmentDetailPage.clickAddNewAdjustmentButton();

        String supplier = param.get("supplier");
        String airlineClass = param.get("airlineClass");
        String airline = param.get("airline");
        String countryOrigin = param.get("countryOrigin");
        String countryDestination = param.get("countryDestination");
        String transit = param.get("transit");
        String airportOrigin = param.get("airportOrigin");
        String airportDestination = param.get("airportDestination");
        String minPrice = param.get("minPrice");
        String maxPrice = param.get("maxPrice");
        String minDiscount = param.get("minDiscount");
        String maxDiscount = param.get("maxDiscount");
        String markupValue = param.get("markupValue");

        step("The user fill in the form with valid data");
        flightPriceAdjustmentAddNewFormPage.fillAirlineDetails(supplier, airlineClass, airline);
        flightPriceAdjustmentAddNewFormPage.fillTripDetails(countryOrigin, countryDestination, transit, airportOrigin, airportDestination);
        flightPriceAdjustmentAddNewFormPage.fillAdjustmentRule(minPrice, maxPrice, minDiscount, maxDiscount, markupValue);

        step("The user click on submit button");
        flightPriceAdjustmentAddNewFormPage.clickSubmitBtn();

        String status = param.get("status");
        String adjustmentBtn = param.get("adjustmentBtn");

        step("The user checks whether adjustment is created");
        Assertion.assertThat("New Adjustment is showing in list", flightPriceAdjustmentDetailPage.checkIfAdjustmentCreated(status, adjustmentBtn), is(true));

        step("The user delete the adjustment");
        flightPriceAdjustmentDetailPage.deleteAdjustment();
    }

    @Author(names = {"Abhishek"})
    @Module(name = "DashboardAdjustment")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testReadAdjustment() {

        Map<String, String> param = getParam("testReadAdjustment");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCampaignDashboardPage<?> flightCampaignDashboardPage = new FlightCampaignDashboardPage<>(session);
        FlightPriceAdjustmentDetailPage<?> flightPriceAdjustmentDetailPage = new FlightPriceAdjustmentDetailPage<>(session);
        FlightPriceAdjustmentAddNewFormPage<?> flightPriceAdjustmentAddNewFormPage = new FlightPriceAdjustmentAddNewFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Campaigns Dashboard");
        flightCampaignDashboardPage.isFlightLoggedIn();
        flightCampaignDashboardPage.clickPriceAdjustmentDetails();

        step("The user click on Add new adjustment button");
        flightPriceAdjustmentDetailPage.clickAddNewAdjustmentButton();

        String supplier = param.get("supplier");
        String airlineClass = param.get("airlineClass");
        String airline = param.get("airline");
        String countryOrigin = param.get("countryOrigin");
        String countryDestination = param.get("countryDestination");
        String transit = param.get("transit");
        String airportOrigin = param.get("airportOrigin");
        String airportDestination = param.get("airportDestination");
        String minPrice = param.get("minPrice");
        String maxPrice = param.get("maxPrice");
        String minDiscount = param.get("minDiscount");
        String maxDiscount = param.get("maxDiscount");
        String markupValue = param.get("markupValue");

        step("The user fill in the form with valid data");
        flightPriceAdjustmentAddNewFormPage.fillAirlineDetails(supplier, airlineClass, airline);
        flightPriceAdjustmentAddNewFormPage.fillTripDetails(countryOrigin, countryDestination, transit, airportOrigin, airportDestination);
        flightPriceAdjustmentAddNewFormPage.fillAdjustmentRule(minPrice, maxPrice, minDiscount, maxDiscount, markupValue);

        step("The user click on submit button");
        flightPriceAdjustmentAddNewFormPage.clickSubmitBtn();

        String airportOriginKey = param.get("airportOriginKey");
        String airportDestinationKey = param.get("airportDestinationKey");

        step("The user checks all the details of the adjustment");
        Assertion.assertThat("Check all the details of the adjustment", flightPriceAdjustmentDetailPage.checkDetailAdjustment(supplier, airlineClass, airportOriginKey, airportDestinationKey, minPrice, maxPrice, minDiscount, maxDiscount, markupValue), is(true));

        step("The user delete the adjustment");
        flightPriceAdjustmentDetailPage.deleteAdjustment();
    }

    @Author(names = {"Abhishek"})
    @Module(name = "DashboardAdjustment")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testDeleteAdjustment() {

        Map<String, String> param = getParam("testDeleteAdjustment");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCampaignDashboardPage<?> flightCampaignDashboardPage = new FlightCampaignDashboardPage<>(session);
        FlightPriceAdjustmentDetailPage<?> flightPriceAdjustmentDetailPage = new FlightPriceAdjustmentDetailPage<>(session);
        FlightPriceAdjustmentAddNewFormPage<?> flightPriceAdjustmentAddNewFormPage = new FlightPriceAdjustmentAddNewFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Campaigns Dashboard");
        flightCampaignDashboardPage.isFlightLoggedIn();
        flightCampaignDashboardPage.clickPriceAdjustmentDetails();

        step("The user click on Add new adjustment button");
        flightPriceAdjustmentDetailPage.clickAddNewAdjustmentButton();

        String supplier = param.get("supplier");
        String airlineClass = param.get("airlineClass");
        String airline = param.get("airline");
        String countryOrigin = param.get("countryOrigin");
        String countryDestination = param.get("countryDestination");
        String transit = param.get("transit");
        String airportOrigin = param.get("airportOrigin");
        String airportDestination = param.get("airportDestination");
        String minPrice = param.get("minPrice");
        String maxPrice = param.get("maxPrice");
        String minDiscount = param.get("minDiscount");
        String maxDiscount = param.get("maxDiscount");
        String markupValue = param.get("markupValue");

        step("The user fill in the form with valid data");
        flightPriceAdjustmentAddNewFormPage.fillAirlineDetails(supplier, airlineClass, airline);
        flightPriceAdjustmentAddNewFormPage.fillTripDetails(countryOrigin, countryDestination, transit, airportOrigin, airportDestination);
        flightPriceAdjustmentAddNewFormPage.fillAdjustmentRule(minPrice, maxPrice, minDiscount, maxDiscount, markupValue);

        step("The user click on submit button");
        flightPriceAdjustmentAddNewFormPage.clickSubmitBtn();

        step("The user delete the adjustment");
        flightPriceAdjustmentDetailPage.deleteAdjustment();

        step("The user verify if adjustment is deleted");
        Assertion.assertThat("Check all the details of the adjustment", flightPriceAdjustmentDetailPage.isAdjustmentDeleted(), is(true));
    }
    @Author(names = {"Akbar"})
    @Module(name = "CampaignDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testSeePriceAdjustmentDetails() throws InterruptedException {
        Map<String, String> param = getParam("testSeePriceAdjustmentDetails");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCampaignDashboardPage<?> flightCampaignDashboardPage = new FlightCampaignDashboardPage<>(session);
        FlightAddCampaignFormPage<?> flightAddCampaignFormPage = new FlightAddCampaignFormPage<>(session);
        FlightPriceAdjustmentDetailPage<?> flightPriceAdjustmentDetailPage = new FlightPriceAdjustmentDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String campaignName = param.get("campaignName") + " " + System.currentTimeMillis();
        String priority = param.get("priority");
        String campaignType = param.get("campaignType");
        String campaignTemplateName = param.get("campaignTemplateName");
        String masterTag = param.get("masterTag");
        String campaignStartDateRange = param.get("campaignStartDateRange");
        String campaignDuration = param.get("campaignDuration");
        String campaignBudget = param.get("campaignBudget");
        String customerTarget = param.get("customerTarget");
        String userType = param.get("userType");
        String loyaltyLevel = param.get("loyaltyLevel");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Campaign dashboard and click on add new campaign button.");
        flightCampaignDashboardPage.goToFlightCampaignDashboardPage();
        flightCampaignDashboardPage.isFlightLoggedIn();
        flightCampaignDashboardPage.clickAddNewCampaignBtn();

        step("The user fills the campaign details and submit it.");
        Map<String, String> createdCampaign = flightAddCampaignFormPage.fillCampaignDetails(campaignName, priority, campaignType, campaignTemplateName, masterTag, campaignStartDateRange, campaignDuration, campaignBudget, customerTarget, userType, loyaltyLevel);
        flightAddCampaignFormPage.clickSubmit();

        step("The user click 'see price adjustment details' button for the newly created campaign");
        flightCampaignDashboardPage.clickPriceAdjustmentDetails(createdCampaign.get("campaignName"));

        step("Extract displayed data on the page and then delete the campaign");
        Map<String, String> displayedCampaignData = flightPriceAdjustmentDetailPage.extractCampaignData();
        flightPriceAdjustmentDetailPage.deleteCampaign();

        step("The user verify checks the displayed campaign data");
        for (String key : displayedCampaignData.keySet()) {
            Assertion.assertThat("Verify '"+ key +"' is matched", displayedCampaignData.get(key).toLowerCase(), is(createdCampaign.get(key).toLowerCase()));
        }
    }

    @Author(names = {"Akbar"})
    @Module(name = "RescheduleChargeDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testAddRescheduleCharge() throws InterruptedException {
        Map<String, String> param = getParam("testAddRescheduleCharge");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightRescheduleChargePage<?> flightRescheduleChargePage = new FlightRescheduleChargePage<>(session);
        FlightAddRescheduleChargeFormPage<?> flightAddRescheduleChargeFormPage = new FlightAddRescheduleChargeFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String airlineCodes = param.get("airlineCodes");
        String supplierCode = param.get("supplierCode");
        String currency = param.get("currency");
        String chargeType = param.get("chargeType");
        String value = param.get("value");
        String journeyType = param.get("journeyType");
        String route = param.get("route");

        int targetIndex = Integer.parseInt(param.get("targetIndex"));

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Reschedule Charge Dashboard and click on add new reschedule charge button.");
        flightRescheduleChargePage.goToReschedulePageDashboard();
        flightRescheduleChargePage.isLoggedIn();
        flightRescheduleChargePage.clickAddRescheduleChargeBtn();

        step("The user fills the reschedule charge details and submit it.");
        Map<String, String> createdRescheduleCharge = flightAddRescheduleChargeFormPage.fillDetails(airlineCodes, supplierCode, currency, chargeType, value, journeyType, route);
        flightAddRescheduleChargeFormPage.clickSubmitButton();

        step("Extract data from the newly created reschedule charge");
        Map<String, String> displayedRescheduleCharge = flightRescheduleChargePage.getDisplayedData(targetIndex);

        step("The user deletes the newly created reschedule charge.");
        flightRescheduleChargePage.deleteRescheduleCharge(targetIndex);

        step("The user verify between created and displayed reschedule charge data");
        for (String key : createdRescheduleCharge.keySet()) {
            Assertion.assertThat("Verify '"+ key +"' is matched", displayedRescheduleCharge.get(key).toLowerCase(), is(createdRescheduleCharge.get(key).toLowerCase()));
        }
    }

    @Author(names = {"Ram Pratap Singh","Shubham Bansal"})
    @Module(name = "AIRLINE DASHBOARD")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testReadAirline() {
        Map<String, String> param = getParam("testReadAirline");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightAirlineDashboardPage<?> flightAirlineDashboardPage = new FlightAirlineDashboardPage<>(session);
        FlightAddAirlineFormPage<?> flightAddAirlineFormPage = new FlightAddAirlineFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.isAdminLogin();

        String airlineName = param.get("airlineName");
        String airlineCode = param.get("airlineCode");
        String airlineDisplayName = param.get("airlineDisplayName");
        String airlineShortName = param.get("airlineShortName");
        String airlineUrl = param.get("airlineUrl");
        String legacyAirlineName = param.get("legacyAirlineName");
        String legacyId = param.get("legacyId");
        String imagePath = param.get("imagePath");

        step("The user navigates to the Airline Dashboard, deletes existing airline by code if present, then clicks add new airline.");
        flightAirlineDashboardPage.deleteAirlineIfExists(airlineCode);
        flightAirlineDashboardPage.clickAddAirlineBtn();

        step("The user fills the airline details and saves the airline.");
        flightAddAirlineFormPage.addAirlineForm(airlineName, airlineCode, airlineDisplayName, airlineShortName, airlineUrl, legacyAirlineName, legacyId, imagePath);
        String addedAirline = flightAirlineDashboardPage.getNewAddedAirline();

        step("The user verify checks the added airline.");
        Assertion.assertThat("New Added airline is showing in list", flightAirlineDashboardPage.isAirlineAdded(airlineName, airlineCode, airlineDisplayName, airlineShortName, addedAirline), is(true));

        step("The user navigates to the Flight Airline dashboard.");
        flightAirlineDashboardPage.clickAirlinePage();

        step("The user clicks on view airline button.");
        flightAirlineDashboardPage.readAirline();

        step("To verify the view Airline page");
        Assertion.assertThat("Verify view Airline page", flightAirlineDashboardPage.checkAirlineDetailsText(airlineName, airlineCode, airlineDisplayName, airlineShortName), is(true));

        step("To user clicks on cancel view button");
        flightAirlineDashboardPage.cancelReadAirline();

        step("The user deletes the airline");
        flightAirlineDashboardPage.deleteAirline(airlineCode);
    }

    @Author(names = {"Nishtha","Shubham Bansal"})
    @Module(name = "CityDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testCreateNewCity() {
        Map<String, String> param = getParam("testCreateNewCity");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCityDashboardPage<?> flightCityDashboardPage = new FlightCityDashboardPage<>(session);
        FlightAddCityFormPage<?> flightAddCityFormPage = new FlightAddCityFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight City dashboard, deletes existing city by code if present, then clicks add new city.");
        flightCityDashboardPage.isFlightLoggedIn();
        flightCityDashboardPage.deleteCityIfExists(param.get("cityCode"));
        flightCityDashboardPage.clickAddCityBtn();

        step("The user fills the city details and saves the city.");
        flightAddCityFormPage.fillCityDetails(param.get("cityCode"), param.get("cityName"), param.get("cityLatitude"), param.get("cityLongitude"), param.get("cityTimezone"), param.get("cityZone"));

        step("The user verify checks the added city.");
        Assertion.assertThat("New Added City is showing in list", flightCityDashboardPage.checkAddedCity(param.get("cityCode"), param.get("cityName"), param.get("cityLatitude"), param.get("cityLongitude"), param.get("cityTimezone")), is(true));

        step("The user deletes the added city.");
        flightCityDashboardPage.deleteCity();
    }

    @Author(names = {"Nishtha","Shubham Bansal"})
    @Module(name = "CityDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testDeleteCity() {
        Map<String, String> param = getParam("testDeleteCity");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCityDashboardPage<?> flightCityDashboardPage = new FlightCityDashboardPage<>(session);
        FlightAddCityFormPage<?> flightAddCityFormPage = new FlightAddCityFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight City dashboard, deletes existing city by code if present, then clicks add new city.");
        flightCityDashboardPage.isFlightLoggedIn();
        flightCityDashboardPage.deleteCityIfExists(param.get("cityCode"));
        flightCityDashboardPage.clickAddCityBtn();

        step("The user fills the city details and saves the city.");
        flightAddCityFormPage.fillCityDetails(param.get("cityCode"), param.get("cityName"), param.get("cityLatitude"), param.get("cityLongitude"), param.get("cityTimezone"), param.get("cityZone"));

        step("The user deletes the added city.");
        flightCityDashboardPage.deleteCity();

        step("The user verify checks the city is deleted");
        Assertion.assertThat("Verify City is not showing in list", flightCityDashboardPage.checkCityDeleted(param.get("cityCode"), param.get("cityName"), param.get("cityLatitude"), param.get("cityLongitude"), param.get("cityTimezone")), is(true));
    }

    @Author(names = {"Nishtha","Shubham Bansal"})
    @Module(name = "CityDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testUpdateCity() {
        Map<String, String> param = getParam("testUpdateCity");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCityDashboardPage<?> flightCityDashboardPage = new FlightCityDashboardPage<>(session);
        FlightAddCityFormPage<?> flightAddCityFormPage = new FlightAddCityFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight City dashboard, deletes existing city by code if present, then clicks add new city.");
        flightCityDashboardPage.isFlightLoggedIn();
        flightCityDashboardPage.deleteCityIfExists(param.get("cityCode"));
        flightCityDashboardPage.clickAddCityBtn();

        step("The user fills the city details and saves the city.");
        flightAddCityFormPage.fillCityDetails(param.get("cityCode"), param.get("cityName"), param.get("cityLatitude"), param.get("cityLongitude"), param.get("cityTimezone"), param.get("cityZone"));

        step("The user navigates to the Flight City dashboard and click on edit city button.");
        flightCityDashboardPage.clickEditCityBtn();
        flightCityDashboardPage.isFlightLoggedIn();

        step("The user updates the city details and saves the city.");
        flightAddCityFormPage.updateCityDetails(param.get("updatedCityName"));

        step("The user verifies that the city has been updated successfully");
        Assertion.assertThat("City has been updated successfully", flightCityDashboardPage.isUpdatedCity(param.get("updatedCityName")), is(true));

        step("The user deletes the updated city.");
        flightCityDashboardPage.deleteCity();
    }

    @Author(names = {"Nishtha"})
    @Module(name = "CityDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testReadCity() {
        Map<String, String> param = getParam("testReadCity");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCityDashboardPage<?> flightCityDashboardPage = new FlightCityDashboardPage<>(session);
        FlightAddCityFormPage<?> flightAddCityFormPage = new FlightAddCityFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight City dashboard and click on add new city button.");
        flightCityDashboardPage.isFlightLoggedIn();
        flightCityDashboardPage.clickAddCityBtn();

        step("The user fills the city details and saves the city.");
        flightAddCityFormPage.fillCityDetails(param.get("cityCode"), param.get("cityName"), param.get("cityLatitude"), param.get("cityLongitude"), param.get("cityTimezone"), param.get("cityZone"));

        step("The user navigates to the Flight City dashboard and click on view city button.");
        flightCityDashboardPage.clickViewCityBtn();
        flightCityDashboardPage.isFlightLoggedIn();

        step("The user verifies that the city details have been viewed successfully");
        Assertion.assertThat("City has been viewed successfully", flightCityDashboardPage.checkCityDetails(param.get("cityCode"), param.get("cityName"), param.get("cityLatitude"), param.get("cityLongitude"), param.get("cityTimezone")), is(true));

        step("The user refreshes and closes the view popup.");
        flightCityDashboardPage.refreshPage();

        step("The user deletes the viewed city.");
        flightCityDashboardPage.deleteCity();
    }

    @Author(names = {"Shubham Bansal"})
    @Module(name = "CityDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testCreateCityWithMultiTimezones() {
        Map<String, String> param = getParam("testCreateCityWithMultiTimezones");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCityDashboardPage<?> flightCityDashboardPage = new FlightCityDashboardPage<>(session);
        FlightAddCityFormPage<?> flightAddCityFormPage = new FlightAddCityFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight City dashboard, deletes existing city by code if present, then clicks add new city.");
        flightCityDashboardPage.isFlightLoggedIn();
        flightCityDashboardPage.deleteCityIfExists(param.get("cityCode"));
        flightCityDashboardPage.clickAddCityBtn();

        step("The user fills mandatory city fields, adds multiple timezones via Add More, and submits.");
        flightAddCityFormPage.fillCityDetailsWithMultiTimezones(param.get("cityCode"), param.get("cityName"), param.get("cityLatitude"), param.get("cityLongitude"), param.get("cityTimezone"), param.get("cityZone"), param.get("secondTimezone"));

        step("The user verifies the added city is showing in the list.");
        Assertion.assertThat("New city with multi timezones is showing in list", flightCityDashboardPage.checkAddedCity(param.get("cityCode"), param.get("cityName"), param.get("cityLatitude"), param.get("cityLongitude"), param.get("cityTimezone")), is(true));

        step("The user hits cities API and verifies multi timezones are stored.");
        Assertion.assertThat("City is stored with multiple timezones in API response", flightCityDashboardPage.verifyCityStoredWithMultiTimezones(param.get("cityCode"), param.get("cityName"), param.get("cityTimezone"), param.get("secondTimezone")), is(true));

        step("The user deletes the added city.");
        flightCityDashboardPage.deleteCity(param.get("cityCode"));
    }

    @Author(names = {"Abhishek","Shubham Bansal"})
    @Module(name = "AIRLINE DASHBOARD")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testAddAirline() {
        Map<String, String> param = getParam("testAddAirline");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightAirlineDashboardPage<?> flightAirlineDashboardPage = new FlightAirlineDashboardPage<>(session);
        FlightAddAirlineFormPage<?> flightAddAirlineFormPage = new FlightAddAirlineFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.isAdminLogin();

        String airlineName = param.get("airlineName");
        String airlineCode = param.get("airlineCode");
        String airlineDisplayName = param.get("airlineDisplayName");
        String airlineShortName = param.get("airlineShortName");
        String airlineUrl = param.get("airlineUrl");
        String legacyAirlineName = param.get("legacyAirlineName");
        String legacyId = param.get("legacyId");
        String imagePath = param.get("imagePath");

        step("The user navigates to the Airline Dashboard, deletes existing airline by code if present, then clicks add new airline.");
        flightAirlineDashboardPage.deleteAirlineIfExists(airlineCode);
        flightAirlineDashboardPage.clickAddAirlineBtn();

        step("The user fills the airline details and saves the airline.");
        flightAddAirlineFormPage.addAirlineForm(airlineName, airlineCode, airlineDisplayName, airlineShortName, airlineUrl, legacyAirlineName, legacyId, imagePath);
        String addedAirline = flightAirlineDashboardPage.getNewAddedAirline();

        step("The user verify checks the added airline.");
        Assertion.assertThat("New Added airline is showing in list", flightAirlineDashboardPage.isAirlineAdded(airlineName, airlineCode, airlineDisplayName, airlineShortName, addedAirline), is(true));

        step("The user deletes the airline");
        flightAirlineDashboardPage.deleteAirline(airlineCode);

    }

    @Author(names = {"Abhishek","Shubham Bansal"})
    @Module(name = "AIRLINE DASHBOARD")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testDeleteAirline() {
        Map<String, String> param = getParam("testDeleteAirline");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightAirlineDashboardPage<?> flightAirlineDashboardPage = new FlightAirlineDashboardPage<>(session);
        FlightAddAirlineFormPage<?> flightAddAirlineFormPage = new FlightAddAirlineFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.isAdminLogin();

        String airlineName = param.get("airlineName");
        String airlineCode = param.get("airlineCode");
        String airlineDisplayName = param.get("airlineDisplayName");
        String airlineShortName = param.get("airlineShortName");
        String airlineUrl = param.get("airlineUrl");
        String legacyAirlineName = param.get("legacyAirlineName");
        String legacyId = param.get("legacyId");
        String imagePath = param.get("imagePath");

        step("The user navigates to the Airline Dashboard, deletes existing airline by code if present, then clicks add new airline.");
        flightAirlineDashboardPage.deleteAirlineIfExists(airlineCode);
        flightAirlineDashboardPage.clickAddAirlineBtn();

        step("The user fills the airline details and saves the airline.");
        flightAddAirlineFormPage.addAirlineForm(airlineName, airlineCode, airlineDisplayName, airlineShortName, airlineUrl, legacyAirlineName, legacyId, imagePath);

        step("The user deletes the airline");
        flightAirlineDashboardPage.deleteAirline(airlineCode);

        step("The user verifies if the airline is deleted");
        Assertion.assertThat("Deleted airline is not showing in filtered list", flightAirlineDashboardPage.isAirlineDeleted(airlineCode), is(true));
    }

    @Author(names = {"Abhishek","Shubham Bansal"})
    @Module(name = "AIRLINE DASHBOARD")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testUpdateAirline() {
        Map<String, String> param = getParam("testUpdateAirline");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightAirlineDashboardPage<?> flightAirlineDashboardPage = new FlightAirlineDashboardPage<>(session);
        FlightAddAirlineFormPage<?> flightAddAirlineFormPage = new FlightAddAirlineFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.isAdminLogin();

        String airlineName = param.get("airlineName");
        String airlineCode = param.get("airlineCode");
        String airlineDisplayName = param.get("airlineDisplayName");
        String airlineShortName = param.get("airlineShortName");
        String airlineUrl = param.get("airlineUrl");
        String legacyAirlineName = param.get("legacyAirlineName");
        String legacyId = param.get("legacyId");
        String imagePath = param.get("imagePath");

        String updatedAirlineName = param.get("updatedAirlineName");
        String updatedAirlineCode = param.get("updatedAirlineCode");
        String updatedAirlineDisplayName = param.get("updatedAirlineDisplayName");
        String updatedAirlineShortName = param.get("updatedAirlineShortName");
        String updatedAirlineUrl = param.get("updatedAirlineUrl");
        String updatedLegacyAirlineName = param.get("updatedLegacyAirlineName");
        String updatedLegacyId = param.get("updatedLegacyId");

        step("The user navigates to the Airline Dashboard, deletes existing airline by code if present, then clicks add new airline.");
        flightAirlineDashboardPage.deleteAirlineIfExists(airlineCode);
        if (updatedAirlineCode != null && !updatedAirlineCode.isBlank() && !updatedAirlineCode.equalsIgnoreCase(airlineCode)) {
            flightAirlineDashboardPage.deleteAirlineIfExists(updatedAirlineCode);
        }
        flightAirlineDashboardPage.clickAddAirlineBtn();

        step("The user fills the airline details and saves the airline.");
        flightAddAirlineFormPage.addAirlineForm(airlineName, airlineCode, airlineDisplayName, airlineShortName, airlineUrl, legacyAirlineName, legacyId, imagePath);

        step("The user updates the airline");
        flightAirlineDashboardPage.clickUpdateAirlineBtn();
        flightAddAirlineFormPage.updateAirlineForm(updatedAirlineName, updatedAirlineCode, updatedAirlineDisplayName, updatedAirlineShortName, updatedAirlineUrl, updatedLegacyAirlineName, updatedLegacyId);
        String updatedAddedAirline = flightAirlineDashboardPage.getNewAddedAirline();
        String cleanupCode = (updatedAirlineCode != null && !updatedAirlineCode.isBlank()) ? updatedAirlineCode : airlineCode;

        step("The user verify checks the updated airline.");
        Assertion.assertThat("Updated airline is showing in list", flightAirlineDashboardPage.isAirlineAdded(updatedAirlineName, cleanupCode, updatedAirlineDisplayName, updatedAirlineShortName, updatedAddedAirline), is(true));

        step("The user deletes the airline");
        flightAirlineDashboardPage.deleteAirline(cleanupCode);
    }

    @Author(names = {"Akbar"})
    @Module(name = "RefundMatrixDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testAddRefundMatrix() throws InterruptedException {
        Map<String, String> param = getParam("testAddRefundMatrix");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightRefundMatrixPage<?> flightRefundMatrixPage = new FlightRefundMatrixPage<>(session);
        FlightAddRefundMatrixFormPage<?> flightAddRefundMatrixFormPage = new FlightAddRefundMatrixFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String priority = param.get("priority");
        String supplierCode = param.get("supplierCode");
        String airlineCode = param.get("airlineCode");
        String origins = param.get("origins");
        String destinations = param.get("destinations");
        String matrixType = param.get("matrixType");
        String originCountry = param.get("originCountry");
        String destinationCountry = param.get("destinationCountry");
        String refundMethod = param.get("refundMethod");
        String cabinClasses = param.get("cabinClasses");
        String fareClasses = param.get("fareClasses") + "-" + System.currentTimeMillis();
        String fareBasis = param.get("fareBasis");
        String reasonId = param.get("reasonId");
        String description = param.get("description") + "-" + System.currentTimeMillis();
        String startDateRangeIssuance = param.get("startDateRangeIssuance");
        String dayCountIssuance = param.get("dayCountIssuance");
        String startDateRangeTravel = param.get("startDateRangeTravel");
        String dayCountTravel = param.get("dayCountTravel");
        String itemTypes = param.get("itemTypes");
        String relationOperator = param.get("relationOperator");
        String timeFrame = param.get("timeFrame");
        String minutes = param.get("minutes");
        String formula = param.get("formula");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Refund Matrix Dashboard and click on add new refund matrix button.");
        flightRefundMatrixPage.goToRefundMatrixDashboard();
        flightRefundMatrixPage.isLoggedIn();
        flightRefundMatrixPage.clickAddRefundMatrixBtn();

        step("The user fills the refund matrix details and submit it.");

        Map<String, String> createdRefundMatrix = flightAddRefundMatrixFormPage.fillDetails(priority, supplierCode, airlineCode, origins, destinations, matrixType, originCountry, destinationCountry, refundMethod, cabinClasses, fareClasses, fareBasis, reasonId, description, startDateRangeIssuance, dayCountIssuance, startDateRangeTravel, dayCountTravel, itemTypes, relationOperator, timeFrame, minutes, formula);
        flightAddRefundMatrixFormPage.clickSubmitButton();

        step("The newly created refund matrix is the first row of the list, which is ordered newest first.");
        Assertion.assertThat("Newly created refund matrix is the first row on the first page",
                flightRefundMatrixPage.getFirstRowFareClasses().toLowerCase(), is(fareClasses.toLowerCase()));

        step("Extract data from the newly created refund matrix");
        int targetIndex = Integer.parseInt(param.get("targetIndex"));
        flightRefundMatrixPage.filterByFareClasses(fareClasses);
        Map<String, String> displayedRefundMatrix = flightRefundMatrixPage.getDisplayedData(targetIndex);
        Assertion.assertThat("Verify the newly created refund matrix is displayed",
                displayedRefundMatrix.get("fareClasses").toLowerCase(), is(fareClasses.toLowerCase()));

        step("The user deletes the newly created refund matrix.");
        flightRefundMatrixPage.deleteRefundMatrix(targetIndex);

        step("The user verify between created and displayed refund matrix data");
        Assertion.assertThat("Verify status is DRAFT", displayedRefundMatrix.get("status").toLowerCase(), is("draft"));
        Assertion.assertThat("Verify matrixType is matched", displayedRefundMatrix.get("matrixType").toLowerCase(), is(createdRefundMatrix.get("matrixType").toLowerCase()));
        Assertion.assertThat("Verify priority is matched", displayedRefundMatrix.get("priority").toLowerCase(), is(createdRefundMatrix.get("priority").toLowerCase()));
        Assertion.assertThat("Verify action for review is appeared", Boolean.parseBoolean(displayedRefundMatrix.get("reviewAction")), is(true));

    }

    @Author(names = {"Akbar"})
    @Module(name = "RefundMatrixDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testDeleteRefundMatrix() throws InterruptedException {
        Map<String, String> param = getParam("testDeleteRefundMatrix");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightRefundMatrixPage<?> flightRefundMatrixPage = new FlightRefundMatrixPage<>(session);
        FlightAddRefundMatrixFormPage<?> flightAddRefundMatrixFormPage = new FlightAddRefundMatrixFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String priority = param.get("priority");
        String supplierCode = param.get("supplierCode");
        String airlineCode = param.get("airlineCode");
        String origins = param.get("origins");
        String destinations = param.get("destinations");
        String matrixType = param.get("matrixType");
        String originCountry = param.get("originCountry");
        String destinationCountry = param.get("destinationCountry");
        String refundMethod = param.get("refundMethod");
        String cabinClasses = param.get("cabinClasses");
        String fareClasses = param.get("fareClasses") + "-" + System.currentTimeMillis();
        String fareBasis = param.get("fareBasis");
        String reasonId = param.get("reasonId");
        String description = param.get("description") + "-" + System.currentTimeMillis();
        String startDateRangeIssuance = param.get("startDateRangeIssuance");
        String dayCountIssuance = param.get("dayCountIssuance");
        String startDateRangeTravel = param.get("startDateRangeTravel");
        String dayCountTravel = param.get("dayCountTravel");
        String itemTypes = param.get("itemTypes");
        String relationOperator = param.get("relationOperator");
        String timeFrame = param.get("timeFrame");
        String minutes = param.get("minutes");
        String formula = param.get("formula");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Refund Matrix Dashboard and click on add new refund matrix button.");
        flightRefundMatrixPage.goToRefundMatrixDashboard();
        flightRefundMatrixPage.isLoggedIn();
        flightRefundMatrixPage.clickAddRefundMatrixBtn();

        step("The user fills the refund matrix details and submit it.");
        Map<String, String> createdRefundMatrix = flightAddRefundMatrixFormPage.fillDetails(priority, supplierCode, airlineCode, origins, destinations, matrixType, originCountry, destinationCountry, refundMethod, cabinClasses, fareClasses, fareBasis, reasonId, description, startDateRangeIssuance, dayCountIssuance, startDateRangeTravel, dayCountTravel, itemTypes, relationOperator, timeFrame, minutes, formula);
        flightAddRefundMatrixFormPage.clickSubmitButton();

        step("The newly created refund matrix is the first row of the list, which is ordered newest first.");
        Assertion.assertThat("Newly created refund matrix is the first row on the first page",
                flightRefundMatrixPage.getFirstRowFareClasses().toLowerCase(), is(fareClasses.toLowerCase()));

        step("Filter to the created refund matrix and read its row — this is the row we delete next.");
        int targetIndex = Integer.parseInt(param.get("targetIndex"));
        flightRefundMatrixPage.filterByFareClasses(fareClasses);
        Map<String, String> displayedRefundMatrix = flightRefundMatrixPage.getDisplayedData(targetIndex);

        Assertion.assertThat("Filtered row fare classes match the record we just created",
                displayedRefundMatrix.get("fareClasses").toLowerCase(), is(fareClasses.toLowerCase()));
        Assertion.assertThat("Filtered row matrix type matches the created refund matrix",
                displayedRefundMatrix.get("matrixType").toLowerCase(), is(createdRefundMatrix.get("matrixType").toLowerCase()));
        Assertion.assertThat("Filtered row priority matches the created refund matrix",
                displayedRefundMatrix.get("priority").toLowerCase(), is(createdRefundMatrix.get("priority").toLowerCase()));
        Assertion.assertThat("Review action is available on the row before delete",
                Boolean.parseBoolean(displayedRefundMatrix.get("reviewAction")), is(true));

        step("The user deletes that refund matrix row.");
        flightRefundMatrixPage.deleteRefundMatrix(targetIndex);

        step("Verify the refund matrix no longer appears when matched by fare classes.");
        Assertion.assertThat("Verify the newly created refund matrix is deleted",
                flightRefundMatrixPage.isRefundMatrixDeleted(fareClasses), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "RefundMatrixDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testUpdateRefundMatrix() throws InterruptedException {
        Map<String, String> param = getParam("testUpdateRefundMatrix");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightRefundMatrixPage<?> flightRefundMatrixPage = new FlightRefundMatrixPage<>(session);
        FlightAddRefundMatrixFormPage<?> flightAddRefundMatrixFormPage = new FlightAddRefundMatrixFormPage<>(session);
        FlightEditRefundMatrixFormPage<?> flightEditRefundMatrixFormPage = new FlightEditRefundMatrixFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String priority = param.get("priority");
        String supplierCode = param.get("supplierCode");
        String airlineCode = param.get("airlineCode");
        String origins = param.get("origins");
        String destinations = param.get("destinations");
        String matrixType = param.get("matrixType");
        String originCountry = param.get("originCountry");
        String destinationCountry = param.get("destinationCountry");
        String refundMethod = param.get("refundMethod");
        String cabinClasses = param.get("cabinClasses");
        String fareClasses = param.get("fareClasses") + "-" + System.currentTimeMillis();
        String fareBasis = param.get("fareBasis");
        String reasonId = param.get("reasonId");
        String description = param.get("description") + "-" + System.currentTimeMillis();
        String startDateRangeIssuance = param.get("startDateRangeIssuance");
        String dayCountIssuance = param.get("dayCountIssuance");
        String startDateRangeTravel = param.get("startDateRangeTravel");
        String dayCountTravel = param.get("dayCountTravel");
        String itemTypes = param.get("itemTypes");
        String relationOperator = param.get("relationOperator");
        String timeFrame = param.get("timeFrame");
        String minutes = param.get("minutes");
        String formula = param.get("formula");

        String changedPriority = param.get("changedPriority");
        String changedMatrixType = param.get("changedMatrixType");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Refund Matrix Dashboard and click on add new refund matrix button.");
        flightRefundMatrixPage.goToRefundMatrixDashboard();
        flightRefundMatrixPage.isLoggedIn();
        flightRefundMatrixPage.clickAddRefundMatrixBtn();

        step("The user fills the refund matrix details and submit it.");
        Map<String, String> createdRefundMatrix = flightAddRefundMatrixFormPage.fillDetails(priority, supplierCode, airlineCode, origins, destinations, matrixType, originCountry, destinationCountry, refundMethod, cabinClasses, fareClasses, fareBasis, reasonId, description, startDateRangeIssuance, dayCountIssuance, startDateRangeTravel, dayCountTravel, itemTypes, relationOperator, timeFrame, minutes, formula);
        flightAddRefundMatrixFormPage.clickSubmitButton();

        step("The newly created refund matrix is the first row of the list, which is ordered newest first.");
        Assertion.assertThat("Newly created refund matrix is the first row on the first page",
                flightRefundMatrixPage.getFirstRowFareClasses().toLowerCase(), is(fareClasses.toLowerCase()));

        step("Filter to the created refund matrix and confirm its row matches the created entry.");
        int targetIndex = Integer.parseInt(param.get("targetIndex"));
        flightRefundMatrixPage.filterByFareClasses(fareClasses);
        Map<String, String> displayed1RefundMatrix = flightRefundMatrixPage.getDisplayedData(targetIndex);
        Assertion.assertThat("Verify fare classes on the filtered row", displayed1RefundMatrix.get("fareClasses").toLowerCase(), is(fareClasses.toLowerCase()));
        Assertion.assertThat("Verify status is DRAFT", displayed1RefundMatrix.get("status").toLowerCase(), is("draft"));
        Assertion.assertThat("Verify matrixType is matched", displayed1RefundMatrix.get("matrixType").toLowerCase(), is(createdRefundMatrix.get("matrixType").toLowerCase()));
        Assertion.assertThat("Verify priority is matched", displayed1RefundMatrix.get("priority").toLowerCase(), is(createdRefundMatrix.get("priority").toLowerCase()));
        Assertion.assertThat("Verify action for review is appeared", Boolean.parseBoolean(displayed1RefundMatrix.get("reviewAction")), is(true));

        step("Read the details modal for the created record.");
        flightRefundMatrixPage.clickDetailsRefundMatrixBtn(targetIndex);
        Map<String, String> displayedDetailsAfterCreate = flightRefundMatrixPage.getDisplayedDetailsData();
        flightRefundMatrixPage.clickCancelDetailsBtn();

        step("The user updates priority, matrix type, and fare classes");
        String changedFareClasses = param.get("changedFareClasses") + "-" + System.currentTimeMillis();
        flightRefundMatrixPage.clickEditRefundMatrixBtn(targetIndex);
        Map<String, String> updatedRefundMatrix = flightEditRefundMatrixFormPage.updateDetailsForGridVerification(
                changedPriority, changedMatrixType, changedFareClasses);
        flightEditRefundMatrixFormPage.clickSubmitButton();

        step("Extract data from the updated refund matrix");
        flightRefundMatrixPage.filterByFareClasses(changedFareClasses);
        Map<String, String> displayed2RefundMatrix = flightRefundMatrixPage.getDisplayedData(targetIndex);

        step("The user deletes the newly created refund matrix.");
        flightRefundMatrixPage.deleteRefundMatrix(targetIndex);

        step("The user verify between created and displayed refund matrix data");
        flightRefundMatrixPage.assertRefundMatrixDetailsModalMatchesCreated(displayedDetailsAfterCreate, createdRefundMatrix, fareClasses, fareBasis);
        Assertion.assertThat("Verify the updated refund matrix is displayed", displayed2RefundMatrix.get("fareClasses").toLowerCase(), is(changedFareClasses.toLowerCase()));
        Assertion.assertThat("Verify status is DRAFT", displayed2RefundMatrix.get("status").toLowerCase(), is("draft"));
        Assertion.assertThat("Verify matrixType is matched", displayed2RefundMatrix.get("matrixType").toLowerCase(), is(updatedRefundMatrix.get("matrixType").toLowerCase()));
        Assertion.assertThat("Verify priority is matched", displayed2RefundMatrix.get("priority").toLowerCase(), is(updatedRefundMatrix.get("priority").toLowerCase()));
        Assertion.assertThat("Verify action for review is appeared", Boolean.parseBoolean(displayed2RefundMatrix.get("reviewAction")), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "RefundMatrixDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testSeeDetailsRefundMatrix() throws InterruptedException {
        Map<String, String> param = getParam("testSeeDetailsRefundMatrix");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightRefundMatrixPage<?> flightRefundMatrixPage = new FlightRefundMatrixPage<>(session);
        FlightAddRefundMatrixFormPage<?> flightAddRefundMatrixFormPage = new FlightAddRefundMatrixFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String priority = param.get("priority");
        String supplierCode = param.get("supplierCode");
        String airlineCode = param.get("airlineCode");
        String origins = param.get("origins");
        String destinations = param.get("destinations");
        String matrixType = param.get("matrixType");
        String originCountry = param.get("originCountry");
        String destinationCountry = param.get("destinationCountry");
        String refundMethod = param.get("refundMethod");
        String cabinClasses = param.get("cabinClasses");
        String fareClasses = param.get("fareClasses") + "-" + System.currentTimeMillis();
        String fareBasis = param.get("fareBasis");
        String reasonId = param.get("reasonId");
        String description = param.get("description") + "-" + System.currentTimeMillis();
        String startDateRangeIssuance = param.get("startDateRangeIssuance");
        String dayCountIssuance = param.get("dayCountIssuance");
        String startDateRangeTravel = param.get("startDateRangeTravel");
        String dayCountTravel = param.get("dayCountTravel");
        String itemTypes = param.get("itemTypes");
        String relationOperator = param.get("relationOperator");
        String timeFrame = param.get("timeFrame");
        String minutes = param.get("minutes");
        String formula = param.get("formula");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Refund Matrix Dashboard and click on add new refund matrix button.");
        flightRefundMatrixPage.goToRefundMatrixDashboard();
        flightRefundMatrixPage.isLoggedIn();
        flightRefundMatrixPage.clickAddRefundMatrixBtn();

        step("The user fills the refund matrix details and submit it.");

        Map<String, String> createdRefundMatrix = flightAddRefundMatrixFormPage.fillDetails(priority, supplierCode, airlineCode, origins, destinations, matrixType, originCountry, destinationCountry, refundMethod, cabinClasses, fareClasses, fareBasis, reasonId, description, startDateRangeIssuance, dayCountIssuance, startDateRangeTravel, dayCountTravel, itemTypes, relationOperator, timeFrame, minutes, formula);
        flightAddRefundMatrixFormPage.clickSubmitButton();

        step("The newly created refund matrix is the first row of the list, which is ordered newest first.");
        Assertion.assertThat("Newly created refund matrix is the first row on the first page",
                flightRefundMatrixPage.getFirstRowFareClasses().toLowerCase(), is(fareClasses.toLowerCase()));

        step("Filter to the created refund matrix and confirm its row matches the created entry.");
        int targetIndex = Integer.parseInt(param.get("targetIndex"));
        flightRefundMatrixPage.filterByFareClasses(fareClasses);
        Map<String, String> displayedGridRow = flightRefundMatrixPage.getDisplayedData(targetIndex);
        Assertion.assertThat("Verify fare classes on the filtered row", displayedGridRow.get("fareClasses").toLowerCase(), is(fareClasses.toLowerCase()));
        Assertion.assertThat("Verify status is DRAFT", displayedGridRow.get("status").toLowerCase(), is("draft"));
        Assertion.assertThat("Verify matrixType is matched", displayedGridRow.get("matrixType").toLowerCase(), is(createdRefundMatrix.get("matrixType").toLowerCase()));
        Assertion.assertThat("Verify priority is matched", displayedGridRow.get("priority").toLowerCase(), is(createdRefundMatrix.get("priority").toLowerCase()));
        Assertion.assertThat("Verify action for review is appeared", Boolean.parseBoolean(displayedGridRow.get("reviewAction")), is(true));

        step("Open details for that row and read the modal.");
        flightRefundMatrixPage.clickDetailsRefundMatrixBtn(targetIndex);
        Map<String, String> displayedRefundMatrix = flightRefundMatrixPage.getDisplayedDetailsData();
        flightRefundMatrixPage.clickCancelDetailsBtn();

        step("The user deletes the newly created refund matrix.");
        flightRefundMatrixPage.deleteRefundMatrix(targetIndex);

        step("The user verify between created and displayed refund matrix data");
        flightRefundMatrixPage.assertRefundMatrixDetailsModalMatchesCreated(displayedRefundMatrix, createdRefundMatrix, fareClasses, fareBasis);
    }

    @Author(names = {"Akbar"})
    @Module(name = "RescheduleChargeDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testDeleteRescheduleCharge() throws InterruptedException {
        Map<String, String> param = getParam("testDeleteRescheduleCharge");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightRescheduleChargePage<?> flightRescheduleChargePage = new FlightRescheduleChargePage<>(session);
        FlightAddRescheduleChargeFormPage<?> flightAddRescheduleChargeFormPage = new FlightAddRescheduleChargeFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String airlineCodes = param.get("airlineCodes");
        String supplierCode = param.get("supplierCode");
        String currency = param.get("currency");
        String chargeType = param.get("chargeType");
        String value = param.get("value");
        String journeyType = param.get("journeyType");
        String route = param.get("route");

        int targetIndex = Integer.parseInt(param.get("targetIndex"));

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Reschedule Charge Dashboard and click on add new reschedule charge button.");
        flightRescheduleChargePage.goToReschedulePageDashboard();
        flightRescheduleChargePage.isLoggedIn();
        flightRescheduleChargePage.clickAddRescheduleChargeBtn();

        step("The user fills the reschedule charge details and submit it.");
        Map<String, String> createdRescheduleCharge = flightAddRescheduleChargeFormPage.fillDetails(airlineCodes, supplierCode, currency, chargeType, value, journeyType, route);
        flightAddRescheduleChargeFormPage.clickSubmitButton();

        step("The user verify if the reschedule charge is created");
        Assertion.assertThat("Verify reschedule charge is created", flightRescheduleChargePage.isExist(createdRescheduleCharge, true), is(true));

        step("The user deletes the newly created reschedule charge.");
        flightRescheduleChargePage.deleteRescheduleCharge(targetIndex);

        step("The user verifies the reschedule charge is deleted.");
        Assertion.assertThat("Verify reschedule charge is deleted", flightRescheduleChargePage.isExist(createdRescheduleCharge, false), is(false));
    }

     @Author(names = {"Akbar"})
     @Module(name = "RescheduleChargeDashboard")
     @Tribe(name = "Flight")
     @Vertical(name = "Flight")
     @Test
     public void testUpdateRescheduleCharge() throws InterruptedException {
         Map<String, String> param = getParam("testUpdateRescheduleCharge");
         BaseTest.paramsThreadLocal.set(param);
         Session<?> session = BaseTest.sessionThreadLocal.get();

         MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
         FlightRescheduleChargePage<?> flightRescheduleChargePage = new FlightRescheduleChargePage<>(session);
         FlightAddRescheduleChargeFormPage<?> flightAddRescheduleChargeFormPage = new FlightAddRescheduleChargeFormPage<>(session);
         FlightEditRescheduleChargeFormPage<?> flightEditRescheduleChargeFormPage = new FlightEditRescheduleChargeFormPage<>(session);

         String username = param.get("username");
         String password = param.get("password");
         String airlineCodes = param.get("airlineCodes");
         String supplierCode = param.get("supplierCode");
         String currency = param.get("currency");
         String chargeType = param.get("chargeType");
         String value = param.get("value");
         String journeyType = param.get("journeyType");
         String route = param.get("route");

         String updatedAirlineCodes = param.get("updatedAirlineCodes");
         String updatedSupplierCode = param.get("updatedSupplierCode");
         String updatedCurrency = param.get("updatedCurrency");
         String updatedChargeType = param.get("updatedChargeType");
         String updatedValue = param.get("updatedValue");
         String updatedJourneyType = param.get("updatedJourneyType");
         String updatedRoute = param.get("updatedRoute");

         int targetIndex = Integer.parseInt(param.get("targetIndex"));

         step("The user logins to the Flight dashboard via cookie injection.");
         memberLoginPage.adminLoginViaCookie(username, password);

         step("The user navigates to the Flight Reschedule Charge Dashboard and click on add new reschedule charge button.");
         flightRescheduleChargePage.goToReschedulePageDashboard();
         flightRescheduleChargePage.isLoggedIn();
         flightRescheduleChargePage.clickAddRescheduleChargeBtn();

         step("The user fills the reschedule charge details and submit it.");
         Map<String, String> createdRescheduleCharge = flightAddRescheduleChargeFormPage.fillDetails(airlineCodes, supplierCode, currency, chargeType, value, journeyType, route);
         flightAddRescheduleChargeFormPage.clickSubmitButton();

         step("The user updates the reschedule charge details.");
         flightRescheduleChargePage.clickEditButton(targetIndex);
         Map<String, String> updatedRescheduleCharge = flightEditRescheduleChargeFormPage.updateDetails(createdRescheduleCharge, updatedAirlineCodes, updatedSupplierCode, updatedCurrency, updatedChargeType, updatedValue, updatedJourneyType, updatedRoute);
         flightEditRescheduleChargeFormPage.clickSubmitButton();

         step("Extract data from the updated reschedule charge");
         Map<String, String> displayedRescheduleCharge = flightRescheduleChargePage.getDisplayedData(targetIndex);

         step("The user deletes the updated reschedule charge.");
         flightRescheduleChargePage.deleteRescheduleCharge(targetIndex);

         step("The user verify the updated reschedule charge data");
         for (String key : updatedRescheduleCharge.keySet()) {
             Assertion.assertThat("Verify '"+ key +"' is matched", displayedRescheduleCharge.get(key).toLowerCase(), is(updatedRescheduleCharge.get(key).toLowerCase()));
         }
         Assertion.assertThat("Verify the created reschedule charge is not exists", flightRescheduleChargePage.isExist(createdRescheduleCharge, false), is(false));
     }

    @Author(names = {"Akbar"})
    @Module(name = "CountryDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testReadCountry() {
        Map<String, String> param = getParam("testReadCountry");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCountryDashboardPage<?> flightCountryDashboardPage = new FlightCountryDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Country dashboard and click on add new country button.");
        flightCountryDashboardPage.isFlightLoggedIn();
        flightCountryDashboardPage.goToFlightCountryPage();

        step("The user verify checks the added country.");
        Assertion.assertThat("Countries is showing in list", flightCountryDashboardPage.checkCountryTableIsExist(), is(true));
    }

    @Author(names = {"Akbar","Shubham Bansal"})
    @Module(name = "CountryDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testUpdateCountry() {
        Map<String, String> param = getParam("testUpdateCountry");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCountryDashboardPage<?> flightCountryDashboardPage = new FlightCountryDashboardPage<>(session);
        FlightAddCountryFormPage<?> flightAddCountryFormPage = new FlightAddCountryFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String createCountryCode = param.get("createCountryCode");
        String createCountryName = param.get("createCountryName") + "-" + System.currentTimeMillis();
        String createCountryCurrency = param.get("createCountryCurrency");

        String updateCountryCode = param.get("updateCountryCode");
        String updateCountryName = param.get("updateCountryName") + "-" + System.currentTimeMillis();
        String updateCountryCurrency = param.get("updateCountryCurrency");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Country dashboard, deletes existing country by code if present, then clicks add new country.");
        flightCountryDashboardPage.isFlightLoggedIn();
        flightCountryDashboardPage.deleteCountryIfExists(createCountryCode);
        flightCountryDashboardPage.clickAddCountryBtn();

        step("The user fills the country details and saves the country.");
        flightAddCountryFormPage.fillCountryDetails(createCountryCode, createCountryName, createCountryCurrency);

        step("The user verify checks the added country.");
        Assertion.assertThat("New Added Country is showing in list", flightCountryDashboardPage.checkCountry(createCountryCode, createCountryName, createCountryCurrency), is(true));

        step("The user update the newly added country.");
        flightCountryDashboardPage.clickUpdateCountryBtn(createCountryCode);
        flightAddCountryFormPage.fillCountryDetails(updateCountryCode, updateCountryName, updateCountryCurrency);

        step("The user extract the updated country data.");
        boolean checkResult = flightCountryDashboardPage.checkCountry(updateCountryCode, updateCountryName, updateCountryCurrency);

        step("The user deletes the added country.");
        flightCountryDashboardPage.deleteCountry(updateCountryCode);

        step("The user checks the updated country.");
        Assertion.assertThat("Updated Country is showing in list", checkResult, is(true));
    }

    @Author(names = {"Vanshika Agarwal"})
    @Module(name = "PageModule")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void editPageModule() {
        Map<String, String> param = getParam("editPageModule");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightModulePage<?> flightModulePage = new FlightModulePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String module = param.get("module");
        String randomNum = CoreFunctions.generateRandomNumeric(8);

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Page Module dashboard.");
        flightModulePage.isFlightLoggedIn();
        flightModulePage.goToFlightPageModule();

        step("User searches for module filter: " + module);
        flightModulePage.searchModuleFilter(module);

        step("User clicks on the edit button for the specific module: " + module);
        flightModulePage.clickEditButton(module);

        step("User generates a random number and updates the module name");
        String updatedModuleName = module + " " + randomNum;
        flightModulePage.updateModuleName(updatedModuleName);

        step("User searches for the NEW updated module filter to verify the change: " + updatedModuleName);
        flightModulePage.searchModuleFilter(updatedModuleName);

        step("Assert that the name changed successfully");
        Assertion.assertThat("Updated module name is showing in the list", flightModulePage.isModulePresentInTable(updatedModuleName), is(true));

        step("User clicks on the edit button for the updated module to revert it");
        flightModulePage.clickEditButton(updatedModuleName);

        step("User changes the module name back to the original value");
        flightModulePage.updateModuleName(module);

        step("User searches for the ORIGINAL module filter to verify the revert: " + module);
        flightModulePage.searchModuleFilter(module);

        step("Assert that the original module name is restored successfully");
        Assertion.assertThat("Original module name is showing in the list after revert", flightModulePage.isModulePresentInTable(module), is(true));
    }

    @Author(names = {"Gavin"})
    @Module(name = "CommissionRegionDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testViewCommissionRegion() {
        Map<String, String> param = getParam("testViewCommissionRegion");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCommissionRegionPage<?> flightCommissionRegionPage = new FlightCommissionRegionPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String pageTitle = param.get("pageTitle");
        String columns = param.get("columns");

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Commission Region dashboard.");
        flightCommissionRegionPage.goToCommissionRegionDashboard();
        flightCommissionRegionPage.isLoggedIn();

        step("Verify the landing page title is displayed.");
        Assertion.assertThat("Verify the page title", flightCommissionRegionPage.getPageTitle(), is(pageTitle));

        step("Verify every grid column is displayed.");
        for (String column : columns.split("\\|")) {
            String columnName = column.trim();
            Assertion.assertThat("Verify '"+ columnName +"' column is displayed", flightCommissionRegionPage.isColumnDisplayed(columnName), is(true));
        }

        step("Verify the add new and the filter buttons are displayed.");
        Assertion.assertThat("Verify 'Add New' button is displayed", flightCommissionRegionPage.isAddNewBtnDisplayed(), is(true));
        Assertion.assertThat("Verify 'Apply Filter' button is displayed", flightCommissionRegionPage.isApplyFilterBtnDisplayed(), is(true));
        Assertion.assertThat("Verify 'Reset Filter' button is displayed", flightCommissionRegionPage.isResetFilterBtnDisplayed(), is(true));

        step("Verify every listed commission region offers the edit, delete and status toggle actions.");
        int rowCount = flightCommissionRegionPage.getRowCount();
        Assertion.assertThat("Verify the grid lists at least one commission region", rowCount, greaterThan(0));
        for (int row = 1; row <= rowCount; row++) {
            Assertion.assertThat("Verify row "+ row +" has an 'Edit' button", flightCommissionRegionPage.isEditButtonDisplayed(row), is(true));
            Assertion.assertThat("Verify row "+ row +" has a 'Delete' button", flightCommissionRegionPage.isDeleteButtonDisplayed(row), is(true));
            Assertion.assertThat("Verify row "+ row +" has an 'Activate' or 'Inactivate' button", flightCommissionRegionPage.isStatusToggleButtonDisplayed(row), is(true));
        }
    }

    @Author(names = {"Gavin"})
    @Module(name = "CommissionRegionDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testAddCommissionRegion() {
        Map<String, String> param = getParam("testAddCommissionRegion");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCommissionRegionPage<?> flightCommissionRegionPage = new FlightCommissionRegionPage<>(session);
        FlightAddCommissionRegionFormPage<?> flightAddCommissionRegionFormPage = new FlightAddCommissionRegionFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String name = param.get("name");
        String countries = param.get("countries");

        int targetIndex = Integer.parseInt(param.get("targetIndex"));

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Commission Region dashboard.");
        step("The user navigates to the Flight Commission Region dashboard.");
        flightCommissionRegionPage.goToCommissionRegionDashboard();
        flightCommissionRegionPage.isLoggedIn();

        step("The user deletes the existing commission region if present.");
        name = flightCommissionRegionPage.generateRandomName(name);
        flightCommissionRegionPage.deleteCommissionRegionIfExists(name);

        step("The user click on add new button, fills the commission region details and submit it.");
        flightCommissionRegionPage.clickAddNewBtn();
        Map<String, String> createdCommissionRegion = flightAddCommissionRegionFormPage.fillDetails(name, countries);
        flightAddCommissionRegionFormPage.clickSubmitButton();

        step("The user searches the newly created commission region on the landing page.");
        flightCommissionRegionPage.searchByName(createdCommissionRegion.get("name"));

        step("Extract data from the newly created commission region");
        Map<String, String> displayedCommissionRegion = flightCommissionRegionPage.getDisplayedData(targetIndex);

        step("The user deletes the newly created commission region.");
        flightCommissionRegionPage.deleteCommissionRegion(targetIndex);

        step("The user verify between created and displayed commission region data");
        for (String key : createdCommissionRegion.keySet()) {
            Assertion.assertThat("Verify '"+ key +"' is matched", displayedCommissionRegion.get(key).toLowerCase(), is(createdCommissionRegion.get(key).toLowerCase()));
        }
    }

    @Author(names = {"Gavin"})
    @Module(name = "CommissionRegionDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testUpdateCommissionRegion() {
        Map<String, String> param = getParam("testUpdateCommissionRegion");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCommissionRegionPage<?> flightCommissionRegionPage = new FlightCommissionRegionPage<>(session);
        FlightAddCommissionRegionFormPage<?> flightAddCommissionRegionFormPage = new FlightAddCommissionRegionFormPage<>(session);
        FlightEditCommissionRegionFormPage<?> flightEditCommissionRegionFormPage = new FlightEditCommissionRegionFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String name = param.get("name");
        String countries = param.get("countries");

        String updatedName = param.get("updatedName");
        String updatedCountries = param.get("updatedCountries");

        int targetIndex = Integer.parseInt(param.get("targetIndex"));

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Commission Region dashboard.");
        flightCommissionRegionPage.goToCommissionRegionDashboard();
        flightCommissionRegionPage.isLoggedIn();

        step("The user deletes the existing commission region if present.");
        name = flightCommissionRegionPage.generateRandomName(name);
        flightCommissionRegionPage.deleteCommissionRegionIfExists(name);

        step("The user click on add new button, fills the commission region details and submit it.");
        flightCommissionRegionPage.clickAddNewBtn();
        Map<String, String> createdCommissionRegion = flightAddCommissionRegionFormPage.fillDetails(name, countries);
        flightAddCommissionRegionFormPage.clickSubmitButton();

        step("The user searches the newly created commission region and verify it is created.");
        Assertion.assertThat("Verify commission region is created", flightCommissionRegionPage.isExist(createdCommissionRegion, true), is(true));

        step("The user updates the commission region details.");
        updatedName = flightCommissionRegionPage.generateRandomName(updatedName);
        flightCommissionRegionPage.clickEditButton(targetIndex);
        Map<String, String> updatedCommissionRegion = flightEditCommissionRegionFormPage.updateDetails(updatedName, updatedCountries);
        flightEditCommissionRegionFormPage.clickSubmitButton();

        step("The user searches the previous commission region name and verify it is no longer found.");
        boolean isCreatedCommissionRegionExist = flightCommissionRegionPage.isExist(createdCommissionRegion, false);

        step("The user searches the updated commission region on the landing page.");
        flightCommissionRegionPage.searchByName(updatedCommissionRegion.get("name"));

        step("Extract data from the updated commission region");
        Map<String, String> displayedCommissionRegion = flightCommissionRegionPage.getDisplayedData(targetIndex);

        step("The user deletes the updated commission region.");
        flightCommissionRegionPage.deleteCommissionRegion(targetIndex);

        step("The user verify the updated commission region data");
        for (String key : updatedCommissionRegion.keySet()) {
            Assertion.assertThat("Verify '"+ key +"' is matched", displayedCommissionRegion.get(key).toLowerCase(), is(updatedCommissionRegion.get(key).toLowerCase()));
        }
        Assertion.assertThat("Verify the created commission region is not exists", isCreatedCommissionRegionExist, is(false));
    }


    @Author(names = {"Gavin"})
    @Module(name = "CommissionRegionDashboard")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testDeleteCommissionRegion() {
        Map<String, String> param = getParam("testDeleteCommissionRegion");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        FlightCommissionRegionPage<?> flightCommissionRegionPage = new FlightCommissionRegionPage<>(session);
        FlightAddCommissionRegionFormPage<?> flightAddCommissionRegionFormPage = new FlightAddCommissionRegionFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String name = param.get("name");
        String countries = param.get("countries");

        int targetIndex = Integer.parseInt(param.get("targetIndex"));

        step("The user logins to the Flight dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Flight Commission Region dashboard and click on add new button.");
        flightCommissionRegionPage.goToCommissionRegionDashboard();
        flightCommissionRegionPage.isLoggedIn();

        step("The user deletes the existing commission region if present.");
        name = flightCommissionRegionPage.generateRandomName(name);
        flightCommissionRegionPage.deleteCommissionRegionIfExists(name);

        step("The user click on add new button, fills the commission region details and submit it.");
        flightCommissionRegionPage.clickAddNewBtn();
        Map<String, String> createdCommissionRegion = flightAddCommissionRegionFormPage.fillDetails(name, countries);
        flightAddCommissionRegionFormPage.clickSubmitButton();

        step("The user searches the newly created commission region and verify it is created.");
        Assertion.assertThat("Verify commission region is created", flightCommissionRegionPage.isExist(createdCommissionRegion, true), is(true));

        step("The user deletes the newly created commission region.");
        flightCommissionRegionPage.deleteCommissionRegion(targetIndex);

        step("The user searches the deleted commission region and verify 'Data Not Exist' is displayed.");
        flightCommissionRegionPage.searchByName(createdCommissionRegion.get("name"));
        Assertion.assertThat("Verify 'Data' Not Exist' is displayed", flightCommissionRegionPage.isDataNotExist(), is(true));
    }
}
