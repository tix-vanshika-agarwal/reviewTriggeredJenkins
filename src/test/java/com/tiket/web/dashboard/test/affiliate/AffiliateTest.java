package com.tiket.web.dashboard.test.affiliate;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.affiliate.*;
import com.tiket.web.dashboard.page.common.LoginAffiliate;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class AffiliateTest extends BaseTest {
    private static String refundChargeFromParam(Map<String, String> param) {
        String v = param.get("refundCharge");
        if (v != null && !v.isBlank()) {
            return v.trim();
        }
        v = param.get("refundChargeAmount");
        return v != null ? v.trim() : "";
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testHistoryTransactionPage() {
        Map<String, String> param = getParam("testHistoryTransactionPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateHistoryTransactionPage<?> affiliateHistoryTransactionPage = new AffiliateHistoryTransactionPage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);

        step("The user navigates to the Finance section and click on dropdown.");
        loginAffiliate.selectPartner();
        loginAffiliate.isLoggedIn();
        affiliateHistoryTransactionPage.clickFinanceDropdown();

        Assertion.assertThat("Dropdown appeared", affiliateHistoryTransactionPage.isFinanceDropdown(), is(true));

        step("The user clicks on the History Transaction API.");
        affiliateHistoryTransactionPage.clickHistoryTransactionAPI();

        Assertion.assertThat("Verify URL contains 'hotel-history-transaction'", affiliateHistoryTransactionPage.isHistoryTransactionAPI(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testBalancePage() {
        Map<String, String> param = getParam("testBalancePage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateBalancePage<?> affiliateBalancePage = new AffiliateBalancePage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);

        step("The user navigates to the Finance section and click on dropdown.");
        loginAffiliate.isLoggedIn();
        affiliateBalancePage.clickFinanceDropdown();

        Assertion.assertThat("Dropdown appeared", affiliateBalancePage.isFinanceDropdown(), is(true));

        step("The user clicks on the Balance Section.");
        affiliateBalancePage.clickBalance();

        Assertion.assertThat("Balance Page appeared", affiliateBalancePage.isBalancePage(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testSettingsDropdown() {
        Map<String, String> param = getParam("testSettingsDropdown");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateSettingsDropdown<?> affiliateSettingsDropdown = new AffiliateSettingsDropdown<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);

        step("The user navigate to Settings Section.");
        loginAffiliate.selectPartner();
        affiliateSettingsDropdown.clickSettingsDropdown();

        Assertion.assertThat("Balance Page appeared", affiliateSettingsDropdown.isSettingDropdown(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testForgotPassword() {
        Map<String, String> param = getParam("testForgotPassword");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateChangePasswordPage<?> affiliateChangePasswordPage = new AffiliateChangePasswordPage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        affiliateChangePasswordPage.clickForgotPassword();

        Assertion.assertThat("Redirected to Password Reset Page", affiliateChangePasswordPage.checkForgotPasswordPage(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testFinanceDropdown() {
        Map<String, String> param = getParam("testFinanceDropdown");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateFinanceDropdown<?> affiliateFinanceDropdown = new AffiliateFinanceDropdown<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);

        step("The user navigates to the Finance section and click on dropdown.");
        loginAffiliate.selectPartner();
        loginAffiliate.isLoggedIn();
        affiliateFinanceDropdown.clickFinanceDropdown();

        Assertion.assertThat("Dropdown appeared", affiliateFinanceDropdown.isFinanceDropdown(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testSearchByPartnerName() {
        Map<String, String> param = getParam("testSearchByPartnerName");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerName = param.get("partnerName");

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicked on search");
        affiliateHotelSearch.searchByPartnerName(partnerName);

        step("The user verify the search.");
        Assertion.assertThat("Search is showing in list", affiliateHotelDashboardPage.checkSearch(partnerName), is(true));

        affiliateHotelSearch.clickResetFilter();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testSearchByPartnerId() {
        Map<String, String> param = getParam("testSearchByPartnerId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicked on search");
        affiliateHotelSearch.searchByPartnerId(partnerId);

        step("The user verify the search.");
        Assertion.assertThat("Search is showing in list", affiliateHotelDashboardPage.checkSearch(partnerId), is(true));

        affiliateHotelSearch.clickResetFilter();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testSearchResetFilter() {
        Map<String, String> param = getParam("testSearchResetFilter");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);


        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);


        String partnerId = param.get("partnerId");

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicked on search");
        affiliateHotelSearch.searchByPartnerId(partnerId);

        affiliateHotelSearch.clickResetFilter();

        step("The user verifies that the search field is empty.");
        Assertion.assertThat("Search field is empty", affiliatePartnerListPage.isSearchFieldEmpty(), is(true));
    }

    @Author(names = {"Ram Pratap Singh", "Vidhi Jindal"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAddNewHotel() {
        Map<String, String> param = getParam("testAddNewHotel");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateNewPartnerFormPage<?> affiliateNewPartnerFormPage = new AffiliateNewPartnerFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Admin dashboard.");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(username, password);
        memberLoginPage.isAdminLogin();

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        step("The user clicks on the Add New button.");
        affiliatePartnerListPage.clickAddNewPartnerButton();

        String country = param.get("country");
        String settlementType = param.get("settlementType");
        String currency = param.get("currency");
        String product = param.get("product");
        String channel = param.get("channel");
        String loginStatus = param.get("loginStatus");
        String testingContent = param.get("testingContent").replace("*", ":");
        String productionContent = param.get("productionContent").replace("*", ":");
        String maxProperty = param.get("maxProperty");
        String emailRecipient = param.get("emailRecipient");
        String refundChargeAmount = refundChargeFromParam(param);
        String issuanceSLAHotel = param.get("issuanceSLAHotel");
        String tiketEntity = param.get("tiketEntity");
        String bookingSLA = param.get("bookingSLAHotel");

        step("The user creates new partner");
        String name = affiliateNewPartnerFormPage.createAndAddNewPartner(country, tiketEntity, product, settlementType, currency);

        step("The user add new hotel");
        affiliateNewPartnerFormPage.addHotelAPI(channel, loginStatus, testingContent, productionContent, maxProperty, emailRecipient, refundChargeAmount, issuanceSLAHotel, bookingSLA);

        step("The user clicked on search");
        affiliateHotelSearch.searchByPartnerName(name);

        step("The user verify the new hotel is created.");
        Assertion.assertThat("Search is showing in list", affiliateHotelDashboardPage.checkSearch(name), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testFinanceDetails() {
        Map<String, String> param = getParam("testFinanceDetails");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateNewPartnerFormPage<?> affiliateNewPartnerFormPage = new AffiliateNewPartnerFormPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        step("The user clicks on the Add New button.");
        affiliatePartnerListPage.clickAddNewPartnerButton();

        String country = param.get("country");
        String settlementType = param.get("settlementType");
        String currency = param.get("currency");
        String product = param.get("product");
        String channel = param.get("channel");
        String loginStatus = param.get("loginStatus");
        String testingContent = param.get("testingContent").replace("*", ":");
        String productionContent = param.get("productionContent").replace("*", ":");
        String maxProperty = param.get("maxProperty");
        String emailRecipient = param.get("emailRecipient");
        String refundCharge = refundChargeFromParam(param);
        String issuanceSLAHotel = param.get("issuanceSLAHotel");
        String tiketEntity = param.get("tiketEntity");
        String bookingSLA = param.get("bookingSLAHotel");

        step("The user creates new partner");
        String name = affiliateNewPartnerFormPage.createAndAddNewPartner(country, tiketEntity, product, settlementType, currency);

        step("The user add new hotel");
        affiliateNewPartnerFormPage.addHotelAPI(channel, loginStatus, testingContent, productionContent, maxProperty, emailRecipient, refundCharge, issuanceSLAHotel, bookingSLA);

        step("The user navigated to created hotel");
        affiliateHotelSearch.searchByPartnerName(name);

        step("The user verify finance details");
        affiliateHotelPartnerEdit.clickSettings();
        affiliateHotelPartnerEdit.clickEditFinancialDetails();

        step("To verify user navigated to Finance Details");
        Assertion.assertThat("User navigated to Finance Details", affiliateHotelDashboardPage.isFinanceDetailsPage(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testHotelToggleStatus() {
        Map<String, String> param = getParam("testHotelToggleStatus");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicks on the toggle button.");
        affiliatePartnerListPage.toggleStatusActive();
        Assertion.assertThat("Verify Status is Active", affiliatePartnerListPage.isStatusActive(), is(true));

        affiliatePartnerListPage.toggleStatusInactive();
        Assertion.assertThat("Verify Status is Inactive", affiliatePartnerListPage.isStatusInactive(), is(true));
    }

    @Author(names = {"Abhishek"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchPartnerByPartnerId() {
        Map<String, String> param = getParam("testSearchPartnerByPartnerId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        String partnerId = param.get("partnerId");

        step("The user searches for the partner by ID.");
        affiliatePartnerListPage.searchPartnerById(partnerId);

        step("The user verifies the searched partner is present");
        Assertion.assertThat("Verify Searched Id is showing in list", affiliatePartnerListPage.isPartnerListItemPresent(partnerId), is(true));

    }

    @Author(names = {"Abhishek"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchPartnerByPartnerName() {
        Map<String, String> param = getParam("testSearchPartnerByPartnerName");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        String partnerName = param.get("partnerName");

        step("The user searches for the partner by Name.");
        affiliatePartnerListPage.searchPartnerByName(partnerName);

        step("The user verifies the searched partner is present");
        Assertion.assertThat("Verify Searched name is showing in list", affiliatePartnerListPage.isPartnerListItemPresent(partnerName), is(true));

    }

    @Author(names = {"Akbar"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testHistoryTransactionTableuTableExist() {
        Map<String, String> param = getParam("testHistoryTransactionTableuTableExist");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);
        AffiliateHistoryTransactionPage<?> affiliateHistoryTransactionPage = new AffiliateHistoryTransactionPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);

        step("The user navigates to the History Transaction page.");
        loginAffiliate.isLoggedIn();
        affiliateHistoryTransactionPage.goToHistoryTransactionPage();

        step("The user verifies the Tableau table exists.");
        Assertion.assertThat("Verify Tableau table exists", affiliateHistoryTransactionPage.isTableauTableExists(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testFlightSearchByPartnerId() {
        Map<String, String> param = getParam("testFlightSearchByPartnerId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightDashboardPage<?> affiliateFlightDashboardPage = new AffiliateFlightDashboardPage<>(session);
        AffiliateFlightSearch<?> affiliateFlightSearch = new AffiliateFlightSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");

        step("The user navigated to Flight Page");
        affiliateFlightDashboardPage.clickFlight();

        step("The user clicked on search");
        affiliateFlightSearch.searchByFlightPartnerId(partnerId);

        step("The user verify the search.");
        Assertion.assertThat("Search is showing in list", affiliateFlightDashboardPage.checkFlightSearch(partnerId), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testFlightSearchByPartnerName() {
        Map<String, String> param = getParam("testFlightSearchByPartnerName");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightDashboardPage<?> affiliateFlightDashboardPage = new AffiliateFlightDashboardPage<>(session);
        AffiliateFlightSearch<?> affiliateFlightSearch = new AffiliateFlightSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerName = param.get("partnerName");

        step("The user navigated to Flight Page");
        affiliateFlightDashboardPage.clickFlight();

        step("The user clicked on search");
        affiliateFlightSearch.searchByFlightPartnerName(partnerName);

        step("The user verify the search.");
        Assertion.assertThat("Search is showing in list", affiliateFlightDashboardPage.checkFlightSearch(partnerName), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testFlightSearchResetFilter() {
        Map<String, String> param = getParam("testFlightSearchResetFilter");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightDashboardPage<?> affiliateFlightDashboardPage = new AffiliateFlightDashboardPage<>(session);
        AffiliateFlightSearch<?> affiliateFlightSearch = new AffiliateFlightSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");
        String partnerName = param.get("partnerName");

        step("The user navigated to Flight Page");
        affiliateFlightDashboardPage.clickFlight();

        step("The user searches for the flight partner by Name and ID.");
        affiliateFlightSearch.searchByFlightPartnerId(partnerId);
        affiliateFlightSearch.searchByFlightPartnerName(partnerName);

        step("The user clicks on Reset Filter button.");
        affiliateFlightSearch.clickFilterResetButton();

        step("The user verifies that the search field is empty.");
        Assertion.assertThat("Search field is empty", affiliateFlightSearch.isSearchFieldEmpty(), is(true));
    }

    @Author(names = {"Nishtha", "Vidhi Jindal"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAddNewFlight() {
        Map<String, String> param = getParam("testAddNewFlight");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightDashboardPage<?> affiliateFlightDashboardPage = new AffiliateFlightDashboardPage<>(session);
        AffiliateFlightSearch<?> affiliateFlightSearch = new AffiliateFlightSearch<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateNewPartnerFormPage<?> affiliateNewPartnerFormPage = new AffiliateNewPartnerFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        step("The user clicks on the Add New button.");
        affiliatePartnerListPage.clickAddNewPartnerButton();

        String country = param.get("country");
        String settlementType = param.get("settlementType");
        String channelId = param.get("channelId");
        String product = param.get("product");
        String currency = param.get("currency");
        String rateType = param.get("rateType");
        String searchSLA = param.get("searchSLA");
        String issuanceSLA = param.get("issuanceSLA");
        String refundCharge = refundChargeFromParam(param);
        String tiketEntity = param.get("tiketEntity");

        step("The user creates new partner");
        String name = affiliateNewPartnerFormPage.createAndAddNewPartner(country, tiketEntity, product, settlementType, currency);

        step("The user add new flight");
        affiliateNewPartnerFormPage.addFlightAPI(channelId, rateType, searchSLA, issuanceSLA, refundCharge);

        step("The user clicked on search");
        affiliateFlightSearch.searchByFlightPartnerName(name);

        step("The user verify the new flight is created.");
        Assertion.assertThat("Search is showing in list", affiliateFlightDashboardPage.checkFlightSearch(name), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testFlightToggleStatus() {
        Map<String, String> param = getParam("testFlightToggleStatus");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateFlightDashboardPage<?> affiliateFlightDashboardPage = new AffiliateFlightDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliateFlightDashboardPage.clickFlight();

        step("The user clicks on the toggle button.");
        affiliateFlightDashboardPage.toggleStatusActive();
        Assertion.assertThat("Verify Status is Active", affiliateFlightDashboardPage.isStatusActive(), is(true));

        affiliateFlightDashboardPage.toggleStatusInactive();
        Assertion.assertThat("Verify Status is Inactive", affiliateFlightDashboardPage.isStatusInactive(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testFinanceDetailsFlight() {
        Map<String, String> param = getParam("testFinanceDetailsFlight");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightDashboardPage<?> affiliateFlightDashboardPage = new AffiliateFlightDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliateFlightDashboardPage.clickFlight();

        step("The user verify finance details");
        affiliateFlightDashboardPage.clickSettings();
        affiliateFlightDashboardPage.clickEditFinancialDetails();

        step("To verify user save the current rate type and updated rate type");
        String currentRateType = affiliateFlightDashboardPage.getCurrentRateType();
        affiliateFlightDashboardPage.toggleRateTypeBasedOnCurrent(currentRateType);
        String updatedRateType = affiliateFlightDashboardPage.getCurrentRateType();

        step("To verify rate type is updated");
        Assertion.assertThat("Verify rate type is updated", affiliateFlightDashboardPage.isRateTypeUpdated(currentRateType, updatedRateType), is(true));
    }

    @Author(names = {"Abhishek"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testPartnerListResetFilterButton() {
        Map<String, String> param = getParam("testPartnerListResetFilterButton");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        String partnerId = param.get("partnerId");
        String partnerName = param.get("partnerName");

        step("The user searches for the partner by Name and ID.");
        affiliatePartnerListPage.searchPartnerById(partnerId);
        affiliatePartnerListPage.searchPartnerByName(partnerName);

        step("The user clicks on Reset Filter button.");
        affiliatePartnerListPage.clickFilterResetButton();

        step("The user verifies that the search field is empty.");
        Assertion.assertThat("Search field is empty", affiliatePartnerListPage.isSearchFieldEmpty(), is(true));

    }

    @Author(names = {"Abhishek"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testPartnerListToggleStatus() {
        Map<String, String> param = getParam("testPartnerListToggleStatus");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        step("The user clicks on the toggle button.");
        affiliatePartnerListPage.toggleStatusActive();
        Assertion.assertThat("Verify Status is Active", affiliatePartnerListPage.isStatusActive(), is(true));

        affiliatePartnerListPage.toggleStatusInactive();
        Assertion.assertThat("Verify Status is Inactive", affiliatePartnerListPage.isStatusInactive(), is(true));

    }

    @Author(names = {"Abhishek"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAddNewPartner() {
        Map<String, String> param = getParam("testAddNewPartner");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateNewPartnerFormPage<?> affiliateNewPartnerFormPage = new AffiliateNewPartnerFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Admin dashboard.");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(username, password);
        memberLoginPage.isAdminLogin();

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        step("The user clicks on the Add New button.");
        affiliatePartnerListPage.clickAddNewPartnerButton();

        String country = param.get("country");
        String tiketEntity = param.get("tiketEntity");
        String settlementType = param.get("settlementType");
        String currency = param.get("currency");

        step("The user creates new partner");
        String name = affiliateNewPartnerFormPage.createNewPartner(country, tiketEntity, settlementType, currency);

        step("The user verifies that the new partner is created.");
        affiliatePartnerListPage.searchPartnerByName(name);
        Assertion.assertThat("New Partner is created", affiliatePartnerListPage.isPartnerListItemPresent(name), is(true));
    }

    @Author(names = {"Abhishek"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testOpenManageProductPage() {
        Map<String, String> param = getParam("testOpenManageProductPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        step("The user opens the manageProduct Popup");
        affiliatePartnerListPage.openManageProductPopUp();

        step("The user verifies the manageProduct Popup is opened");
        Assertion.assertThat("Verify manageProduct Popup is opened", affiliatePartnerListPage.isManageProductPopUpDisplayed(), is(true));

    }

    @Author(names = {"Abhishek"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAddProductsInPartnerList() {
        Map<String, String> param = getParam("testAddProductsInPartnerList");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        String partnerName = param.get("partnerName");

        step("The user searches the partner by name");
        affiliatePartnerListPage.searchPartnerByName(partnerName);

        step("The user opens the manageProduct Popup");
        affiliatePartnerListPage.openManageProductPopUp();

        step("The user adds the product in the manageProduct Popup");
        String productName = affiliatePartnerListPage.clickAddMoreProductsButton();

        step("The user verifies the user is on add product page");
        Assertion.assertThat("Verify user is on add product page", affiliatePartnerListPage.isOnProductAddPage(productName), is(true));
    }

    @Author(names = {"Abhishek"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testConfigureProductsInPartnerList() {
        Map<String, String> param = getParam("testConfigureProductsInPartnerList");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        String partnerName = param.get("partnerName");

        step("The user searches the partner by name");
        affiliatePartnerListPage.searchPartnerByName(partnerName);

        step("The user opens the manageProduct Popup");
        affiliatePartnerListPage.openManageProductPopUp();

        step("The user configures the product in the manageProduct Popup");
        String productName = affiliatePartnerListPage.clickConfigureButton();

        step("The user verifies the user is on edit product page");
        Assertion.assertThat("Verify user is on edit product page", affiliatePartnerListPage.isOnProductEditPage(productName), is(true));

    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testEditContentDetailsHotel() {
        Map<String, String> param = getParam("testEditContentDetailsHotel");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateNewPartnerFormPage<?> affiliateNewPartnerFormPage = new AffiliateNewPartnerFormPage<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        step("The user clicks on the Add New button.");
        affiliatePartnerListPage.clickAddNewPartnerButton();

        String country = param.get("country");
        String settlementType = param.get("settlementType");
        String currency = param.get("currency");
        String product = param.get("product");
        String channel = param.get("channel");
        String loginStatus = param.get("loginStatus");
        String testingContent = param.get("testingContent").replace("*", ":");
        String productionContent = param.get("productionContent").replace("*", ":");
        String maxProperty = param.get("maxProperty");
        String emailRecipient = param.get("emailRecipient");
        String refundCharge = refundChargeFromParam(param);
        String issuanceSLAHotel = param.get("issuanceSLAHotel");
        String tiketEntity = param.get("tiketEntity");
        String bookingSLA = param.get("bookingSLAHotel");

        step("The user creates new partner");
        String name = affiliateNewPartnerFormPage.createAndAddNewPartner(country, tiketEntity, product, settlementType, currency);

        step("The user add new hotel");
        affiliateNewPartnerFormPage.addHotelAPI(channel, loginStatus, testingContent, productionContent, maxProperty, emailRecipient, refundCharge, issuanceSLAHotel, bookingSLA);

        step("The user clicked on search");
        affiliatePartnerListPage.searchPartnerByName(name);

        step("The user clicks edit content details");
        affiliateHotelPartnerEdit.clickSettings();
        affiliateHotelPartnerEdit.clickEditContentDetails();

        step("The user edits the details");
        affiliateHotelPartnerEdit.editInventory();
        affiliateHotelPartnerEdit.editPartnerEntity();
        affiliateHotelPartnerEdit.saveUpdateHotelDetails();

        step("The user verify the details are updated");
        Assertion.assertThat("User updated the hotel details", affiliateHotelDashboardPage.isHotelDetailsUpdated(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testEditPartnerEntityHotel() {
        Map<String, String> param = getParam("testEditPartnerEntityHotel");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateNewPartnerFormPage<?> affiliateNewPartnerFormPage = new AffiliateNewPartnerFormPage<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        step("The user clicks on the Add New button.");
        affiliatePartnerListPage.clickAddNewPartnerButton();

        String country = param.get("country");
        String settlementType = param.get("settlementType");
        String currency = param.get("currency");
        String product = param.get("product");
        String channel = param.get("channel");
        String loginStatus = param.get("loginStatus");
        String testingContent = param.get("testingContent").replace("*", ":");
        String productionContent = param.get("productionContent").replace("*", ":");
        String maxProperty = param.get("maxProperty");
        String emailRecipient = param.get("emailRecipient");
        String refundCharge = refundChargeFromParam(param);
        String issuanceSLAHotel = param.get("issuanceSLAHotel");
        String tiketEntity = param.get("tiketEntity");
        String bookingSLA = param.get("bookingSLAHotel");

        step("The user creates new partner");
        String name = affiliateNewPartnerFormPage.createAndAddNewPartner(country, tiketEntity, product, settlementType, currency);

        step("The user add new hotel");
        affiliateNewPartnerFormPage.addHotelAPI(channel, loginStatus, testingContent, productionContent, maxProperty, emailRecipient, refundCharge, issuanceSLAHotel, bookingSLA);

        step("The user clicked on search");
        affiliatePartnerListPage.searchPartnerByName(name);

        step("The user clicks edit content details");
        affiliateHotelPartnerEdit.clickSettings();
        affiliateHotelPartnerEdit.clickEditContentDetails();

        step("The user edits the details");
        affiliateHotelPartnerEdit.editPartnerEntity();
        affiliateHotelPartnerEdit.saveUpdateHotelDetails();

        step("The user verify the details are updated");
        Assertion.assertThat("User updated the hotel details", affiliateHotelDashboardPage.isHotelDetailsUpdated(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testEditInventoryHotel() {
        Map<String, String> param = getParam("testEditInventoryHotel");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateNewPartnerFormPage<?> affiliateNewPartnerFormPage = new AffiliateNewPartnerFormPage<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        step("The user clicks on the Add New button.");
        affiliatePartnerListPage.clickAddNewPartnerButton();

        String country = param.get("country");
        String settlementType = param.get("settlementType");
        String currency = param.get("currency");
        String product = param.get("product");
        String channel = param.get("channel");
        String loginStatus = param.get("loginStatus");
        String testingContent = param.get("testingContent").replace("*", ":");
        String productionContent = param.get("productionContent").replace("*", ":");
        String maxProperty = param.get("maxProperty");
        String emailRecipient = param.get("emailRecipient");
        String refundCharge = refundChargeFromParam(param);
        String issuanceSLAHotel = param.get("issuanceSLAHotel");
        String tiketEntity = param.get("tiketEntity");
        String bookingSLA = param.get("bookingSLAHotel");

        step("The user creates new partner");
        String name = affiliateNewPartnerFormPage.createAndAddNewPartner(country, tiketEntity, product, settlementType, currency);

        step("The user add new hotel");
        affiliateNewPartnerFormPage.addHotelAPI(channel, loginStatus, testingContent, productionContent, maxProperty, emailRecipient, refundCharge, issuanceSLAHotel, bookingSLA);

        step("The user clicked on search");
        affiliatePartnerListPage.searchPartnerByName(name);

        step("The user clicks edit content details");
        affiliateHotelPartnerEdit.clickSettings();
        affiliateHotelPartnerEdit.clickEditContentDetails();

        step("The user edits the details");
        affiliateHotelPartnerEdit.editInventory();
        affiliateHotelPartnerEdit.saveUpdateHotelDetails();

        step("The user verify the details are updated");
        Assertion.assertThat("User updated the hotel details", affiliateHotelDashboardPage.isHotelDetailsUpdated(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchByHotelId() {
        Map<String, String> param = getParam("testSearchByHotelId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");
        String hotelId = param.get("hotelId");
        String hotelName = param.get("hotelName");

        step("The user navigates to Hotel exclusion page");
        affiliateHotelDashboardPage.goToHotelExclusion(partnerId);

        step("The user search for Hotel with hotel id");
        affiliateHotelSearch.searchByHotelId(hotelId);

        step("The user verify the Hotel details");
        Assertion.assertThat("User verify the search", affiliateHotelDashboardPage.checkSearch(hotelName), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAddNewCommission() {
        Map<String, String> param = getParam("testAddNewCommission");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightDashboardPage<?> affiliateFlightDashboardPage = new AffiliateFlightDashboardPage<>(session);
        AffiliateFlightCommissionPage<?> affiliateFlightCommissionPage = new AffiliateFlightCommissionPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliateFlightDashboardPage.clickFlight();

        step("The user clicks on set up commission button.");
        affiliateFlightDashboardPage.clickSettings();
        affiliateFlightDashboardPage.clickSetupCommission();

        step("The user clicks on add new commission button");
        affiliateFlightCommissionPage.clickAddNewCommissionButton();

        step("The user passes and saves the details");
        String commissionValue = param.get("commissionValue");
        String descriptionInput = param.get("descriptionInput");
        String expectedSnackbarText = param.get("expectedSnackbarText");
        affiliateFlightCommissionPage.fillCommissionConfig(commissionValue, descriptionInput);

        step("To verify Flight commission successfully created!");
        Assertion.assertThat("Verify Flight commission successfully created!", affiliateFlightCommissionPage.isCommissionAdded(expectedSnackbarText), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchByAffiliateNameAndID() {
        Map<String, String> param = getParam("testSearchByAffiliateNameAndID");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateUTMManagerPage<?> affiliateUTMManagerPage = new AffiliateUTMManagerPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String affiliateId = param.get("affiliateID");
        String productType = param.get("productType");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to UTM Manager page.");
        affiliateUTMManagerPage.gotoUTMManagerPage();

        step("The user selects the Affiliate Name or ID: " + affiliateId);
        affiliateUTMManagerPage.selectAffiliateById(affiliateId);

        step("The user selects a Product Type: " + productType);
        affiliateUTMManagerPage.selectProductType(productType);

        step("The user clicks on Search button.");
        affiliateUTMManagerPage.clickSearchButton();

        step("The user verifies the search result contains the partner.");
        Assertion.assertThat("Partner search result is visible", affiliateUTMManagerPage.isPartnerVisible(affiliateId), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchBySupplierNameInAirlinesExclusion() {
        Map<String, String> param = getParam("testSearchBySupplierNameInAirlinesExclusion");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightDashboardPage<?> affiliateFlightDashboardPage = new AffiliateFlightDashboardPage<>(session);
        AffiliateFlightAirlineExclusionPage<?> affiliateFlightAirlineExclusionPage = new AffiliateFlightAirlineExclusionPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliateFlightDashboardPage.clickFlight();

        step("The user clicks on airlines exclusion button.");
        affiliateFlightDashboardPage.clickSettings();
        affiliateFlightDashboardPage.clickAirlinesExclusion();

        step("The user searches airline by supplier name");
        String supplierSearchInput = param.get("supplierSearchInput");
        affiliateFlightAirlineExclusionPage.clickSearchBtn(supplierSearchInput);

        step("To verify flight supplier name and iata code is present");
        String expectedSupplier = param.get("expectedSupplier");
        String expectedIataCode = param.get("expectedIataCode");
        Assertion.assertThat("Verify flight supplier name and iata code is present", affiliateFlightAirlineExclusionPage.checkSearchedAirline(expectedSupplier, expectedIataCode), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testResetAirlinesExclusion() {
        Map<String, String> param = getParam("testResetAirlinesExclusion");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightDashboardPage<?> affiliateFlightDashboardPage = new AffiliateFlightDashboardPage<>(session);
        AffiliateFlightAirlineExclusionPage<?> affiliateFlightAirlineExclusionPage = new AffiliateFlightAirlineExclusionPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliateFlightDashboardPage.clickFlight();

        step("The user clicks on airlines exclusion button.");
        affiliateFlightDashboardPage.clickSettings();
        affiliateFlightDashboardPage.clickAirlinesExclusion();

        step("The user searches airline by supplier name");
        String supplierSearchInput = param.get("supplierSearchInput");
        affiliateFlightAirlineExclusionPage.clickSearchBtn(supplierSearchInput);

        step("The user clicks on reset button");
        affiliateFlightAirlineExclusionPage.clickResetBtn();
        Assertion.assertThat("To verify reset button is working", affiliateFlightAirlineExclusionPage.checkResetSearchButton(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testOpenManageCredentials() {
        Map<String, String> param = getParam("testOpenManageCredentials");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightDashboardPage<?> affiliateFlightDashboardPage = new AffiliateFlightDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliateFlightDashboardPage.clickFlight();

        step("The user clicks on manage credentials button.");
        affiliateFlightDashboardPage.clickSettings();
        affiliateFlightDashboardPage.clickManageCredentials();
        Assertion.assertThat("To verify user navigates to flight credentials page", affiliateFlightDashboardPage.isNavigatedToCredentialPage(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAddAirlinesExclusion() {
        Map<String, String> param = getParam("testAddAirlinesExclusion");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightDashboardPage<?> affiliateFlightDashboardPage = new AffiliateFlightDashboardPage<>(session);
        AffiliateFlightAirlineExclusionPage<?> affiliateFlightAirlineExclusionPage = new AffiliateFlightAirlineExclusionPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliateFlightDashboardPage.clickFlight();

        step("The user clicks on airlines exclusion button.");
        affiliateFlightDashboardPage.clickSettings();
        affiliateFlightDashboardPage.clickAirlinesExclusion();

        step("The user clicks on add exclusion button.");
        affiliateFlightAirlineExclusionPage.clickAddExclusionButton();
        affiliateFlightAirlineExclusionPage.fillAddExclusionForm();
        affiliateFlightAirlineExclusionPage.clickAddAirlineButton();

        step("The user verifies the airline exclusion is added.");
        Assertion.assertThat("To verify airline exclusion is added", affiliateFlightAirlineExclusionPage.isAirlineExclusionAdded(), is(true));

        step("The user searches airline by supplier name");
        String supplierSearchInput = param.get("supplierSearchInput");
        affiliateFlightAirlineExclusionPage.clickSearchBtn(supplierSearchInput);

        step("The user clicks on delete exclusion button.");
        affiliateFlightAirlineExclusionPage.clickDeleteExclusionButton();
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testDeleteAirlinesExclusion() {
        Map<String, String> param = getParam("testDeleteAirlinesExclusion");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightDashboardPage<?> affiliateFlightDashboardPage = new AffiliateFlightDashboardPage<>(session);
        AffiliateFlightAirlineExclusionPage<?> affiliateFlightAirlineExclusionPage = new AffiliateFlightAirlineExclusionPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliateFlightDashboardPage.clickFlight();

        step("The user clicks on airlines exclusion button.");
        affiliateFlightDashboardPage.clickSettings();
        affiliateFlightDashboardPage.clickAirlinesExclusion();

        step("The user clicks on add exclusion button.");
        affiliateFlightAirlineExclusionPage.clickAddExclusionButton();
        affiliateFlightAirlineExclusionPage.fillAddExclusionForm();
        affiliateFlightAirlineExclusionPage.clickAddAirlineButton();

        step("The user searches airline by supplier name");
        String supplierSearchInput = param.get("supplierSearchInput");
        affiliateFlightAirlineExclusionPage.clickSearchBtn(supplierSearchInput);

        step("The user verifies the airline exclusion is deleted.");
        Assertion.assertThat("To verify airline exclusion is deleted", affiliateFlightAirlineExclusionPage.clickDeleteExclusionButton(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testTTDSearchByPartnerId() {
        Map<String, String> param = getParam("testTTDSearchByPartnerId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateTTDDashboardPage<?> affiliateTTDDashboardPage = new AffiliateTTDDashboardPage<>(session);
        AffiliateTTDSearch<?> affiliateTTDSearch = new AffiliateTTDSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");

        step("The user navigated to ttd Page");
        affiliateTTDDashboardPage.clickTTD();

        step("The user clicked on search and search by partner id");
        affiliateTTDSearch.searchByTTDPartnerId(partnerId);

        step("The user verify the search.");
        Assertion.assertThat("Search is showing in list", affiliateTTDDashboardPage.checkTTDSearch(partnerId), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testTTDSearchByPartnerName() {
        Map<String, String> param = getParam("testTTDSearchByPartnerName");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateTTDDashboardPage<?> affiliateTTDDashboardPage = new AffiliateTTDDashboardPage<>(session);
        AffiliateTTDSearch<?> affiliateTTDSearch = new AffiliateTTDSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerName = param.get("partnerName");

        step("The user navigated to ttd Page");
        affiliateTTDDashboardPage.clickTTD();

        step("The user clicked on search and search by partner name");
        affiliateTTDSearch.searchByTTDPartnerName(partnerName);

        step("The user verify the search.");
        Assertion.assertThat("Search is showing in list", affiliateTTDDashboardPage.checkTTDSearch(partnerName), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testTTDResetSearchButton() {
        Map<String, String> param = getParam("testTTDResetSearchButton");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateTTDDashboardPage<?> affiliateTTDDashboardPage = new AffiliateTTDDashboardPage<>(session);
        AffiliateTTDSearch<?> affiliateTTDSearch = new AffiliateTTDSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");

        step("The user navigated to ttd Page");
        affiliateTTDDashboardPage.clickTTD();

        step("The user clicked on search and search by partner id");
        affiliateTTDSearch.searchByTTDPartnerId(partnerId);

        step("The user clicks on Reset Filter button.");
        affiliateTTDSearch.clickFilterResetButton();

        step("The user verifies that the search field is empty.");
        Assertion.assertThat("Search field is empty", affiliateTTDSearch.isSearchFieldEmpty(), is(true));

        step("The user verify the search.");
        Assertion.assertThat("Search is showing in list", affiliateTTDSearch.hasMultipleSettingsButtons(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testResetSearchByHotelId() {
        Map<String, String> param = getParam("testResetSearchByHotelId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");
        String hotelId = param.get("hotelId");
        String hotelName = param.get("hotelName");

        step("The user navigates to Hotel exclusion page");
        affiliateHotelDashboardPage.goToHotelExclusion(partnerId);

        step("The user search for Hotel with hotel id");
        affiliateHotelSearch.searchByHotelId(hotelId);

        step("The user verify the Hotel details");
        Assertion.assertThat("User verify the search", affiliateHotelDashboardPage.checkSearch(hotelName), is(true));

        step("The user click on Reset Filter");
        affiliateHotelSearch.clickResetFilter();

        step("The user verifies that the search field is empty.");
        Assertion.assertThat("Search field is empty", affiliateHotelDashboardPage.isHotelIDSearchFieldEmpty(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testSearchContentSyncPage() {
        Map<String, String> param = getParam("testSearchContentSyncPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String status = param.get("status");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicked on search");
        affiliateHotelSearch.searchByPartnerId(partnerId);

        step("The user clicks on content sync");
        affiliateHotelPartnerEdit.clickSettings();
        affiliateHotelDashboardPage.clickContentSync();

        step("The user click on Upload Status");
        affiliateHotelDashboardPage.selectUploadStaus();
        Assertion.assertThat("User verify the Upload Status", affiliateHotelDashboardPage.checkAllStatuses(status), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testResetContentSyncPage() {
        Map<String, String> param = getParam("testResetContentSyncPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicked on search");
        affiliateHotelSearch.searchByPartnerId(partnerId);

        step("The user clicks on content sync");
        affiliateHotelPartnerEdit.clickSettings();
        affiliateHotelDashboardPage.clickContentSync();

        step("The user clicks on Upload Status");
        affiliateHotelDashboardPage.selectUploadStaus();

        step("The user clicks on Reset Filter");
        affiliateHotelSearch.clickResetFilter();

        step("The user verify that the Upload Status field is clear");
        Assertion.assertThat("Upload Status field is empty", affiliateHotelDashboardPage.isUploadStatusClear(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testDownloadBulkPushSummary() {
        Map<String, String> param = getParam("testDownloadBulkPushSummary");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicked on search");
        affiliateHotelSearch.searchByPartnerId(partnerId);

        step("The user clicks on content sync");
        affiliateHotelPartnerEdit.clickSettings();
        affiliateHotelDashboardPage.clickContentSync();

        step("The user clicks on Upload Status");
        affiliateHotelDashboardPage.selectUploadStaus();

        boolean isDownloaded;

        step("The user clicks on Download Summary");
        isDownloaded = affiliateHotelDashboardPage.downloadBulkPushSummary();

        step("The user verifies the download.");
        Assertion.assertThat("File is downloaded", isDownloaded, is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testAddCommission() {
        Map<String, String> param = getParam("testAddCommission");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String commissionValue = param.get("commissionValue");
        String commissionDescription = param.get("commissionDescription");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicked on search");
        affiliateHotelSearch.searchByPartnerId(partnerId);

        step("The user clicks on content sync");
        affiliateHotelPartnerEdit.clickSettings();

        step("The user add new commission");
        affiliateHotelPartnerEdit.addNewCommission(commissionValue, commissionDescription);

        step("The user verify that new commission is added.");
        Assertion.assertThat("New Commission is added", affiliateHotelDashboardPage.isCommissionAdded(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testRoomPushData() {
        Map<String, String> param = getParam("testRoomPushData");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");
        String filepath = param.get("filepath");

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicked on search");
        affiliateHotelSearch.searchByPartnerId(partnerId);

        step("The user clicks on content sync");
        affiliateHotelPartnerEdit.clickSettings();
        affiliateHotelDashboardPage.clickContentSync();

        step("The user push data for property type");
        affiliateHotelDashboardPage.pushData(filepath, AffiliateHotelDashboardPage.ExclusionAction.ACTIVATE);

        step("To user verify Hotel Room data is pushed");
        Assertion.assertThat("Verify Hotel Room data is pushed", affiliateHotelDashboardPage.isDataPushed(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testEditProductsPwaInPartnerList() {
        Map<String, String> param = getParam("testEditProductsPwaInPartnerList");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerProductPwa();

        String partnerName = param.get("partnerName");

        step("The user searches the partner by name");
        affiliatePartnerListPage.searchPartnerByName(partnerName);

        step("The user edits the Product pwa");
        affiliatePartnerListPage.editPWA();

        step("The user verifies it is on edit Product Pwa page");
        Assertion.assertThat("Verify user is on edit Product Pwa page", affiliatePartnerListPage.isOnEditProductPwaPage(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Co-Brand")
    @Vertical(name = "Affiliate")
    @Test
    public void testSearchCobrandPartnerByName() throws InterruptedException {
        Map<String, String> param = getParam("testSearchCobrandPartnerByName");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateCoBrandDashboardPage<?> affiliateCoBrandDashboardPage = new AffiliateCoBrandDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigated to Cobrand Dashboard Page");
        affiliateCoBrandDashboardPage.goToCoBrandDashboardPage();

        step("The user clicked on search");
        partnerName = partnerName.equalsIgnoreCase("random") ? affiliateCoBrandDashboardPage.getRandomDisplayedPartnerName() : partnerName;
        affiliateCoBrandDashboardPage.applyFilter(partnerName, "", "", "");

        step("The user verify the search");
        boolean isResultExist = affiliateCoBrandDashboardPage.checkFilterResult(partnerName, "", "", "");

        step("The user reset the search filter");
        affiliateCoBrandDashboardPage.clickResetFilterButton();

        step("Assertion");
        Assertion.assertThat("Search is showing in list", isResultExist, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Co-Brand")
    @Vertical(name = "Affiliate")
    @Test
    public void testSearchCobrandPartnerByStatus() throws InterruptedException {
        Map<String, String> param = getParam("testSearchCobrandPartnerByStatus");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateCoBrandDashboardPage<?> affiliateCoBrandDashboardPage = new AffiliateCoBrandDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String status = param.get("status");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigated to Hotel Page");
        affiliateCoBrandDashboardPage.goToCoBrandDashboardPage();

        step("The user clicked on search");
        affiliateCoBrandDashboardPage.applyFilter("", status, startDate, endDate);

        step("The user verify the search");
        boolean isResultExist = affiliateCoBrandDashboardPage.checkFilterResult("", status, "", "");

        step("The user reset the search filter");
        affiliateCoBrandDashboardPage.clickResetFilterButton();

        step("Assertion");
        Assertion.assertThat("Search is showing in list", isResultExist, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testEditProductOfferedPartnerPWA() throws InterruptedException {
        Map<String, String> param = getParam("testEditProductOfferedPartnerPWA");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePWAPage<?> affiliatePWAPage = new AffiliatePWAPage<>(session);
        AffiliatePWAPartnerEditPage<?> affiliatePWAPartnerEditPage = new AffiliatePWAPartnerEditPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");
        String productName = param.get("productName") + "-" + System.currentTimeMillis();
        String productIconURL = param.get("productIconURL");
        String productURL = param.get("productURL");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the PWA dashboard.");
        affiliatePWAPage.goToPage();

        step("The user search the target partner and click edit button");
        affiliatePWAPage.applyFilter(partnerName, "");
        affiliatePWAPage.clickEditButton(partnerName);

        step("The user clicks on add new product, fills the data, and save it");
        affiliatePWAPartnerEditPage.addNewProductOffered(productName, productIconURL, productURL);
        affiliatePWAPartnerEditPage.clickSaveButton();

        step("The user check the newly added product offer");
        boolean isNewProductOfferedExist = affiliatePWAPage.checkProductOffered(partnerName, productName);

        step("The user go to the target product edit page");
        affiliatePWAPage.clickEditButton(partnerName);

        step("The user delete the newly added product offer");
        affiliatePWAPartnerEditPage.deleteProductOffered(productName);
        affiliatePWAPartnerEditPage.clickSaveButton();

        step("Assertion");
        Assertion.assertThat("Verify newly added product offer is saved", isNewProductOfferedExist, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Co-Brand")
    @Vertical(name = "Affiliate")
    @Test
    public void testResetFilterSearchCobrand() throws InterruptedException {
        Map<String, String> param = getParam("testResetFilterSearchCobrand");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateCoBrandDashboardPage<?> affiliateCoBrandDashboardPage = new AffiliateCoBrandDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigated to Co-Brand Page");
        affiliateCoBrandDashboardPage.goToCoBrandDashboardPage();
        List<String> initialPartnerList = affiliateCoBrandDashboardPage.extractDisplayedCoBrandData();
        List<String> initialFilterField = affiliateCoBrandDashboardPage.extractFilterFieldData();

        step("The user clicked on search and apply filter");
        affiliateCoBrandDashboardPage.applyFilter(partnerName, "", startDate, endDate);
        List<String> afterApplyFilterField = affiliateCoBrandDashboardPage.extractFilterFieldData();
        Assertion.assertThat("Filter is applied correctly", initialFilterField, not(containsInAnyOrder(afterApplyFilterField.toArray(new String[0]))));

        step("The user reset the search filter");
        affiliateCoBrandDashboardPage.clickResetFilterButton();
        List<String> afterFilterResetPartnerList = affiliateCoBrandDashboardPage.extractDisplayedCoBrandData();
        List<String> afterFilterResetFilterField = affiliateCoBrandDashboardPage.extractFilterFieldData();

        step("Assertion");
        Assertion.assertThat("Filter Field before and after reset the filter is the same", initialFilterField, containsInAnyOrder(afterFilterResetFilterField.toArray(new String[0])));
        Assertion.assertThat("Partner List before and after reset the filter is the same", afterFilterResetPartnerList, containsInAnyOrder(initialPartnerList.toArray(new String[0])));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testTTDToggleStatus() {
        Map<String, String> param = getParam("testTTDToggleStatus");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateTTDDashboardPage<?> affiliateTTDDashboardPage = new AffiliateTTDDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliateTTDDashboardPage.clickTTD();

        step("The user clicks on the toggle button.");
        affiliateTTDDashboardPage.toggleStatusActive();
        Assertion.assertThat("Verify Status is Active", affiliateTTDDashboardPage.isStatusActive(), is(true));

        affiliateTTDDashboardPage.toggleStatusInactive();
        Assertion.assertThat("Verify Status is Inactive", affiliateTTDDashboardPage.isStatusInactive(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testTTDEditPartnerVoucher() {
        Map<String, String> param = getParam("testTTDEditPartnerVoucher");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateTTDDashboardPage<?> affiliateTTDDashboardPage = new AffiliateTTDDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliateTTDDashboardPage.clickTTD();

        step("The user clicks on the settings dropdown.");
        affiliateTTDDashboardPage.clickSettings();

        step("The user clicks on Edit Partner Voucher.");
        affiliateTTDDashboardPage.clickEditPartnerVoucher();

        step("The user navigates to the Edit Partner Voucher page.");
        affiliateTTDDashboardPage.isNavigatedToPartnerVoucherPage();

        step("The user updates the voucher details.");
        Assertion.assertThat("User updates the voucher details", affiliateTTDDashboardPage.isPartnerVoucherUpdated(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testRoomDeactivateExclusionPushData() {
        Map<String, String> param = getParam("testRoomDeactivateExclusionPushData");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");
        String filepath = param.get("filepath");

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicked on search");
        affiliateHotelSearch.searchByPartnerId(partnerId);

        step("The user clicks on content sync");
        affiliateHotelPartnerEdit.clickSettings();
        affiliateHotelDashboardPage.clickContentSync();

        step("The user push data for property type");
        affiliateHotelDashboardPage.pushData(filepath, AffiliateHotelDashboardPage.ExclusionAction.DEACTIVATE);

        step("To user verify Hotel Room data is pushed");
        Assertion.assertThat("Verify Hotel Room data is pushed", affiliateHotelDashboardPage.isDataPushed(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testHotelEditInventory() {
        Map<String, String> param = getParam("testHotelEditInventory");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicked on search");
        affiliatePartnerListPage.searchPartnerByName(partnerName);

        step("The user clicks edit content details");
        affiliateHotelPartnerEdit.clickSettings();
        affiliateHotelPartnerEdit.clickEditContentDetails();

        step("The user edits the details");
        affiliateHotelPartnerEdit.editPartnerEntity();
        affiliateHotelPartnerEdit.editInventory();
        affiliateHotelPartnerEdit.saveUpdateHotelDetails();

        step("The user verify the details are updated");
        Assertion.assertThat("User updated the hotel details", affiliateHotelDashboardPage.isHotelDetailsUpdated(), is(true));
    }

    @Author(names = {"Amit Bhardwaj"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateRateKey() {
        Map<String, String> param = getParam("testAffiliateRateKey");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigate to the Edit Key Rate page.");
        affiliateRateKeyPage.goToEditrateKey();
    }

    @Author(names = {"Mridul"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchByAffiliateProductType() {
        Map<String, String> param = getParam("testSearchByAffiliateProductType");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateUTMManagerPage<?> affiliateUTMManagerPage = new AffiliateUTMManagerPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String affiliateId = param.get("affiliateID");
        String productType = param.get("productType");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to UTM Manager page.");
        affiliateUTMManagerPage.gotoUTMManagerPage();

        step("The user selects the Affiliate Name or ID: " + affiliateId);
        affiliateUTMManagerPage.selectAffiliateById(affiliateId);

        step("The user selects a Product Type: " + productType);
        affiliateUTMManagerPage.selectProductType(productType);

        step("The user clicks on Search button.");
        affiliateUTMManagerPage.clickSearchButton();

        step("The user verifies the search result contains the Product Type : " + productType);
        Assertion.assertThat("Partner search result is similar to Product type", affiliateUTMManagerPage.getFirstRowProductType(), containsString(productType));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testHotelManageCredentialsAddNew() {
        Map<String, String> param = getParam("testHotelManageCredentialsAddNew");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicks on Manage Credentials");
        affiliateHotelDashboardPage.goToManageCredentials();

        step("The user adds new credentials");
        affiliateHotelDashboardPage.addNewCredentials();

        step("The user verifies the credentials are added");
        Assertion.assertThat("User verifies the credentials are added", affiliateHotelDashboardPage.isCredentialsAdded(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testHotelManageCredentialsDelete() {
        Map<String, String> param = getParam("testHotelManageCredentialsDelete");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicks on Manage Credentials");
        affiliateHotelDashboardPage.goToManageCredentials();

        step("The user deletes credential");
        affiliateHotelDashboardPage.deleteCredentials();

        step("The user verifies the credentials are deleted");
        Assertion.assertThat("User verifies the credentials are deleted", affiliateHotelDashboardPage.isCredentialsDeleted(), is(true));
    }

    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateHotelWhitelist() {
        Map<String, String> param = getParam("testAffiliateHotelWhitelist");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelWhitelist<?> AffiliateHotelWhitelist = new AffiliateHotelWhitelist<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String hotelId = param.get("hotelId");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigate to hotel API menu");
        AffiliateHotelWhitelist.clickHotelAPIManualWhitelist();

        step("Click the add single button");
        AffiliateHotelWhitelist.clickAddSingleButton();

        step("Insert hotelId");
        AffiliateHotelWhitelist.searchByHotelIdWhitelist(hotelId);

        step("Click search button");
        AffiliateHotelWhitelist.clickSearchHotelButton();

        step("Click add whitelist button");
        AffiliateHotelWhitelist.clickAddWhitelistButton();
    }


    @Author(names = {"Amit Bhardwaj"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateRateTypeNetRate() {
        Map<String, String> param = getParam("testAffiliateRateTypeNetRate");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateRateTypePage<?> affiliateRateTypePage = new AffiliateRateTypePage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigate to the Rate type page ");
        affiliateRateTypePage.goToEditRateType();

        step("User selects Net Rate ");
        affiliateRateTypePage.selectNetRate();

        step("User clicks save ");
        affiliateRateTypePage.saveEditRateType();

        step("Verify the bulk hotel is successfully removed");
        String expectedSnackbarText = param.get("expectedSnackbarText");
        Assertion.assertThat("Verify the bulk hotel is successfully removed", affiliateRateTypePage.checkGeneralConfigChangesSnackbar(expectedSnackbarText), is(true));

    }

    @Author(names = {"Amit Bhardwaj"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateRateTypeSellRate() {
        Map<String, String> param = getParam("testAffiliateRateTypeSellRate");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateRateTypePage<?> affiliateRateTypePage = new AffiliateRateTypePage<>(session);
        String commission = param.get("commission");

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigate to the Rate type page ");
        affiliateRateTypePage.goToEditRateType();

        step("User selects Sell Rate ");
        affiliateRateTypePage.selectSellRate();
        affiliateRateTypePage.inputCommissionValue(commission);

        step("User clicks save ");
        affiliateRateTypePage.saveEditRateType();
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchByPartnerNameNonAPI() {
        Map<String, String> param = getParam("testSearchByPartnerNameNonAPI");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");
        String firstPartnerName = param.get("firstPartnerName").replace("{", "(").replace("}", ")");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section:");
        affiliateNonAPIPage.nonAPISection();


        step("The enter the partner name: " + partnerName);
        affiliateNonAPIPage.partnerList(partnerName);

        step("The user clicks on Search button.");
        affiliateNonAPIPage.clickSearchButton();

        step("The user verifies the search result contains the Partner name : " + firstPartnerName);
        Assertion.assertThat("Searched partner name is displayed   ", affiliateNonAPIPage.getFirstRowProductType(firstPartnerName), containsStringIgnoringCase(firstPartnerName));
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchByPartnerIdNonAPI() {
        Map<String, String> param = getParam("testSearchByPartnerIdNonAPI");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");
        String firstPartnerName = param.get("firstPartnerName").replace("{", "(").replace("}", ")");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section:");
        affiliateNonAPIPage.nonAPISection();


        step("The enter the partner Id: " + partnerId);
        affiliateNonAPIPage.partnerList(partnerId);

        step("The user clicks on Search button.");
        affiliateNonAPIPage.clickSearchButton();

        step("The user verifies the search result contains the Partner Id : " + partnerId);
        Assertion.assertThat("Searched partner Id is displayed   ", affiliateNonAPIPage.getFirstRowProductType(firstPartnerName), containsStringIgnoringCase(partnerId));
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testResetNonAPIPartnerList() {
        Map<String, String> param = getParam("testResetNonAPIPartnerList");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section:");
        affiliateNonAPIPage.nonAPISection();

        step("The enter the partner Id: " + partnerId);
        affiliateNonAPIPage.partnerList(partnerId);

        step("The user clicks on Search button.");
        affiliateNonAPIPage.clickSearchButton();

        step("The user clicks on Reset button.");
        affiliateNonAPIPage.clickResetButton();

        step("The user verifies the search text box is reset and no value preset");
        Assertion.assertThat("Searched data is reset now", affiliateNonAPIPage.searchTextBoxIsEmpty(), emptyString());
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateRateKeyPartnerNameAndIdDisabled() {
        Map<String, String> param = getParam("testAffiliateRateKeyPartnerNameAndIdDisabled");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigate to the Edit Key Rate page.");
        affiliateRateKeyPage.goToEditrateKey();

        step("The user verifies that partner id is disabled");
        Assertion.assertThat("user verifies that partner id is disabled", affiliateRateKeyPage.checkPartnerIdIsDisabled(), is(false));

        step("The user verifies that partner name is disabled");
        Assertion.assertThat("user verifies that partner name is disabled", affiliateRateKeyPage.checkPartnerNameIsDisabled(), is(false));
    }

    @Author(names = {"Amit Bhardwaj"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchByItineraryId() {
        Map<String, String> param = getParam("testSearchByItineraryId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateOrderTable<?> affiliateOrderTable = new AffiliateOrderTable<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);
        String itinerary = param.get("itinerary");
        String username = param.get("username");
        String password = param.get("password");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);

        step("The user navigates to the ItineraryId tab.");
        affiliateOrderTable.goToItineraryId();

        step("The user enters ItineraryId .");
        affiliateOrderTable.inputItineraryId(itinerary);

        step("The user Search ItineraryId .");
        affiliateOrderTable.searchItineraryId();
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testPwaToggleOnAndOff() {
        Map<String, String> param = getParam("testPwaToggleOnAndOff");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section:");
        affiliateNonAPIPage.nonAPISection();

        step("Click on PWA section");
        affiliateNonAPIPage.clickPwaSection();

        step("Enter the partner Name in PWA Section: " + partnerName);
        affiliateNonAPIPage.clickPwaPartnerTextField(partnerName);

        step("The user clicks on Search button.");
        affiliateNonAPIPage.clickSearchButton();

        step("Check the Partner status on PWA list");
        String toogleStatus = affiliateNonAPIPage.getPartnerToggleStatusOnList();

        step("The user clicks edit Pwa button to toggle on and off ");
        affiliateNonAPIPage.clickEditPwaButton();

        step("The user clicks on toggle on and off ");
        affiliateNonAPIPage.clickToggleButton();

        step("The user clicks on save button of PWA ");
        affiliateNonAPIPage.clickSaveBtnOfPwa();

        step("After change to Save the status the status is not equal");
        Assertion.assertThat(" Status after saving is not equal", toogleStatus, not(equalTo(affiliateNonAPIPage.getPartnerToggleStatusOnList())));

    }

    @Author(names = {"Amit Bhardwaj"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testResetItineraryId() {
        Map<String, String> param = getParam("testResetItineraryId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateOrderTable<?> affiliateOrderTable = new AffiliateOrderTable<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);
        String itinerary = param.get("itinerary");
        String username = param.get("username");
        String password = param.get("password");

        step("The user navigate to Affiliate Dashboard Login.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);

        step("The user navigates to the ItineraryId tab.");
        loginAffiliate.selectPartner();
        affiliateOrderTable.goToItineraryId();

        step("The user enters ItineraryId.");
        affiliateOrderTable.inputItineraryId(itinerary);

        step("The user Search ItineraryId.");
        affiliateOrderTable.searchItineraryId();

        step("The user Reset ItineraryId");
        affiliateOrderTable.resetItineraryId();
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testEditTermsAndConditionPwa() {
        Map<String, String> param = getParam("testEditTermsAndConditionPwa");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section:");
        affiliateNonAPIPage.nonAPISection();

        step("Click on PWA section");
        affiliateNonAPIPage.clickPwaSection();

        step("The user clicks edit Pwa button to toggle on and off ");
        affiliateNonAPIPage.clickEditPwaButton();

        step("The user Scroll to bottom till save button");
        affiliateNonAPIPage.scrollToSaveBtnPwa();

        step("Edit terms and Condition Data for PWA");
        affiliateNonAPIPage.editTermsAndConditionData();

        step("Get Text for Url Description " + affiliateNonAPIPage.getUrlDescriptionTextFromTermsAndCondition());
        String urlDescriptiondata = affiliateNonAPIPage.getUrlDescriptionTextFromTermsAndCondition();

        step("Get Text for Url Text field");
        String urlTextData = affiliateNonAPIPage.getUrlTextFromTermsAndCondition();

        step("Get Text for Url field ");
        String urlData = affiliateNonAPIPage.getUrlFromTermsAndCondition();

        step("The user clicks on save button of PWA ");
        affiliateNonAPIPage.clickSaveBtnOfPwa();

        step("The user clicks edit Pwa button to toggle on and off ");
        affiliateNonAPIPage.clickEditPwaButton();

        step("The user Scroll to bottom till save button");
        affiliateNonAPIPage.scrollToSaveBtnPwa();

        step("Verify that data is updated or not after clicking on Save button");
        Assertion.assertThat("Verify Url Description in PWA for Terms and condition", affiliateNonAPIPage.getUrlDescriptionTextFromTermsAndCondition(), equalToIgnoringCase(urlDescriptiondata));
        Assertion.assertThat("Verify Url text data in PWA for Terms and condition", affiliateNonAPIPage.getUrlTextFromTermsAndCondition(), equalToIgnoringCase(urlTextData));
        Assertion.assertThat("Verify Url field in PWA for Terms and condition", affiliateNonAPIPage.getUrlFromTermsAndCondition(), equalToIgnoringCase(urlData));
    }

    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateSearchCoBrandByDates() throws InterruptedException {
        Map<String, String> param = getParam("testAffiliateSearchCoBrandByDates");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateNonAPIPage<?> affiliateSearchCoBrandByDates = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String status = param.get("status");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user go the affiliate main page");
        affiliateSearchCoBrandByDates.goToAffiliateMainPage();

        step("The user go to cobrand menu page");
        affiliateSearchCoBrandByDates.clickCobrandMenuPage();

        step("The user clicked on search");
        affiliateSearchCoBrandByDates.setTheFilterField(startDate, endDate, status);

        step("The user reset the search filter");
        affiliateSearchCoBrandByDates.clickResetFilterBTN();

        step("The user verify the search");
        boolean isResultExist = affiliateSearchCoBrandByDates.checkFilterDates("", "", "");

        step("Assertion");
        Assertion.assertThat("Search is showing in list", isResultExist, is(true));
    }

    @Author(names = {"Amit Bhardwaj"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testTTDWhitelist() {
        Map<String, String> param = getParam("testTTDWhitelist");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateTTDWhitelist<?> affiliateTTDWhitelist = new AffiliateTTDWhitelist<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);
        String productName = param.get("productName");
        String listedProductName = param.get("listedProductName");
        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigates to the whitelist page.");
        affiliateTTDWhitelist.clickTTDWhitelist();

        step("The user Select Product Name");
        affiliateTTDWhitelist.inputProductName(productName);

        step("The user Search ItineraryId.");
        affiliateTTDWhitelist.searchByProductNameTTD(listedProductName);
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testTtdApiProductIdSearch() {
        Map<String, String> param = getParam("testTtdApiProductIdSearch");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateTtdApiPage<?> affiliateTtdApiPage = new AffiliateTtdApiPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerID = param.get("partnerID");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        memberLoginPage.goToPage("/admin/affiliate");

        step("Click on TTD API");
        affiliateTtdApiPage.clickTtdApiSection();

        step("Click on TTD API Whitelist");
        affiliateTtdApiPage.clickTtdApiWhitelist();

        step("Click on TTD API Whitelist filter");
        affiliateTtdApiPage.clickTtdApiWhitelistFilter();

        step("Click on TTD API Whitelist Product ID filter");
        affiliateTtdApiPage.clickTtdApiWhitelistFilterProductId();

        step("Enter product id value: " + partnerID);
        affiliateTtdApiPage.enterProductIDValue(partnerID);

        step("Click on TTD API Whitelist  search button");
        affiliateTtdApiPage.clickTtdApiWhitelistSearch();

        step("Get first row data");
        affiliateTtdApiPage.getFirstRowProductTypeTTDAPI(partnerID);

        step("Verify that data is search with partner id");
        Assertion.assertThat("Verify that data is search with partner id", affiliateTtdApiPage.getFirstRowProductTypeTTDAPI(partnerID), equalToIgnoringCase(partnerID));
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testTtdApiAddSingleProduct() {
        Map<String, String> param = getParam("testTtdApiAddSingleProduct");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateTtdApiPage<?> affiliateTtdApiPage = new AffiliateTtdApiPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerID = param.get("partnerID");
        String AddProductWhitelistSnackBar = param.get("AddProductWhitelistSnackBar");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        memberLoginPage.goToPage("/admin/affiliate");

        step("Click on TTD API");
        affiliateTtdApiPage.clickTtdApiSection();

        step("Click on TTD API Whitelist");
        affiliateTtdApiPage.clickTtdApiWhitelist();

        step("Click on TTD API Add Single Product");
        affiliateTtdApiPage.clickTtdApiAddSingleProduct();

        step("Enter product id for TTD whitlist");
        affiliateTtdApiPage.enterTtdApiAddSingleProductProductId(partnerID);

        step("Enter on TTD API Add Single Product");
        affiliateTtdApiPage.clickTtdApiAddToWhiteListBtn();

        step("Verify that product is whitelisted");
        Assertion.assertThat("Verify that product is whitelisted", affiliateTtdApiPage.getTextAddProductSnackBar(),equalToIgnoringCase(AddProductWhitelistSnackBar));
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testOrderTableHotelItineraryId() {
        Map<String, String> param = getParam("testOrderTableHotelItineraryId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateOrderTableInternalDashboard<?> affiliateOrderTableInternalDashboard = new AffiliateOrderTableInternalDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String itinearyId = param.get("itinearyId");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        memberLoginPage.goToPage("/admin/affiliate");

        step("Click on Order Tabel:");
        affiliateOrderTableInternalDashboard.clickOrderTable();

        step("Click on Itinerary section:");
        affiliateOrderTableInternalDashboard.clickItinerary();

        step("Enter  Itinerary ID: " + itinearyId);
        affiliateOrderTableInternalDashboard.enterItineraryId(itinearyId);

        step("Click Seacrh button");
        affiliateOrderTableInternalDashboard.clickOrderTableSearchButton();

        step("Verify that data is displayed based on Itinerary ID");
        Assertion.assertThat("Verify that data is displayed based on Itinerary ID ", affiliateOrderTableInternalDashboard.checkDataDisplayed(),is(true));
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testOrderTableHotelOrderId() {
        Map<String, String> param = getParam("testOrderTableHotelOrderId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateOrderTableInternalDashboard<?> affiliateOrderTableInternalDashboard = new AffiliateOrderTableInternalDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        memberLoginPage.goToPage("/admin/affiliate");

        step("Click on Order Tabel:");
        affiliateOrderTableInternalDashboard.clickOrderTable();

        step("Enter on Order id: " +orderId);
        affiliateOrderTableInternalDashboard.enterOrderId(orderId);


        step("Click Seacrh button");
        affiliateOrderTableInternalDashboard.clickOrderTableSearchButton();

        step("Verify that data is displayed based on Order Id");
        Assertion.assertThat("Verify that data is displayed based on Order ID ", affiliateOrderTableInternalDashboard.checkDataDisplayed(),is(true));

    }
    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testHotelBizlogSearchPartnerByName() {
        Map<String, String> param = getParam("testHotelBizlogSearchPartnerByName");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateBizLogHotelPage<?> affiliateBizLogHotelPage = new AffiliateBizLogHotelPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");
        String partnerName = param.get("partnerName");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateBizLogHotelPage.goToAffiliateHotelBusinessLog();

        step("The user navigate to hotel API menu");
        affiliateBizLogHotelPage.clickHotelAPIBusinessLogMenu();

        step("The user insert the Partner ID");
        affiliateBizLogHotelPage.insertHotelAPIBizLogPartnerID(partnerId);

        step("Click search button");
        affiliateBizLogHotelPage.clickSearchHotelBizlogByID();

        step("The user verifies the search result contains the Partner ID : " + partnerName);
        Assertion.assertThat("Searched Order Id is displayed   ", affiliateBizLogHotelPage.getPartnerIDResult(partnerName) ,containsStringIgnoringCase(partnerId));
    }
    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateTTDOrderTableSearchByOrderId() {
        Map<String, String> param = getParam("testAffiliateTTDOrderTableSearchByOrderId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateTTDOrderTablePage<?> affiliateTTDOrderTablePage= new AffiliateTTDOrderTablePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateTTDOrderTablePage.goToAffiliateLandingPageMenu();

        step("The user navigate to the TTD Order Table page");
        affiliateTTDOrderTablePage.goToTTDOrderTableMenu();

        step("The user insert the orderId");
        affiliateTTDOrderTablePage.searchTTDOrderByOrderId(orderId);

        step("The user click the search button");
        affiliateTTDOrderTablePage.clickTTDOrderSearchBTN();

        step("The user verifies the search result contains the Order Id : " + orderId);
        Assertion.assertThat("Searched Order Id is displayed   ", affiliateTTDOrderTablePage.getTTDOrderIdResult(orderId) ,containsStringIgnoringCase(orderId));
    }

    @Author(names = {"Amit Bhardwaj"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchByOrderId() {
        Map<String, String> param = getParam("testSearchByOrderId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateOrderTable<?> affiliateOrderTable = new AffiliateOrderTable<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);
        String orderId = param.get("orderId");
        String username = param.get("username");
        String password = param.get("password");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);

        step("The user enters OrderId.");
        affiliateOrderTable.inputOrderId(orderId);

        step("The user Search Order.");
        affiliateOrderTable.searchOrderId(orderId);
    }
    @Author(names = {"Gaurav Juneja"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testOrderTableHotelReset() {
        Map<String, String> param = getParam("testOrderTableHotelReset");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateOrderTableInternalDashboard<?> affiliateOrderTableInternalDashboard = new AffiliateOrderTableInternalDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        memberLoginPage.goToPage("/admin/affiliate");

        step("Click on Order Table:");
        affiliateOrderTableInternalDashboard.clickOrderTable();

        step("Enter on Order id: " +orderId);
        affiliateOrderTableInternalDashboard.enterOrderId(orderId);


        step("Click Search button");
        affiliateOrderTableInternalDashboard.clickOrderTableSearchButton();

        step("Verify that data is displayed based on Order Id");
        Assertion.assertThat("Verify that data is displayed based on Order ID ", affiliateOrderTableInternalDashboard.checkDataDisplayed(),is(true));

        step("Click reset button");
        affiliateOrderTableInternalDashboard.clickOrderTableResetButton();

        step("After cliking on reset button it should become disabled");
        Assertion.assertThat("After cliking on reset button it should shoe disable", affiliateOrderTableInternalDashboard.isEnableOrderTableResetButton(),is(false));

    }
    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateWhitelistSearchByHotelName() {
        Map<String, String> param = getParam("testAffiliateWhitelistSearchByHotelName");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelWhitelist<?> AffiliateHotelWhitelist = new AffiliateHotelWhitelist<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String filterBy = param.get("filterBy");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        AffiliateHotelWhitelist.goToAffiliateLandingWhitelistPage();

        step("The user navigate to hotel API menu");
        AffiliateHotelWhitelist.clickHotelAPIManualWhitelistPage();

        step("The user navigate to Filter By Section.");
        AffiliateHotelWhitelist.clickFilter1DropDown();

        step("The user click the Hotel Name option on Filter By Section");
        AffiliateHotelWhitelist.selectHotelNameOption(filterBy);

        step("The user insert the Hotel Name value");
        AffiliateHotelWhitelist.insertHotelNameforEnterValueField(hotelName);

        step("The user click the Search Button after setup the filter");
        AffiliateHotelWhitelist.clickSearchButtonFilter();

        step("Get first row data");
        String res = AffiliateHotelWhitelist.getHotelNameResult(hotelName);

        step("Verify that hotelId displayed according to the search");
        Assertion.assertThat("Verify data is search with Hotel Name", res, equalToIgnoringCase(hotelName));
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testHotelAPIGenConfigPartnerNameDisabled() {
        Map<String, String> param = getParam("testHotelAPIGenConfigPartnerNameDisabled");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelApiPageGenConfig<?> affiliateHotelApiPageGenConfig = new AffiliateHotelApiPageGenConfig<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateHotelApiPageGenConfig.goToHotelAPIPage();

        step("The user navigate to the Hotel API page");
        affiliateHotelApiPageGenConfig.clickHotelApiSection();

        step("The user navigate to the Gen Config page");
        affiliateHotelApiPageGenConfig.clickGenConfigHotelAPI();

        step("The user verifies that partner name is disabled");
        Assertion.assertThat("user verifies that partner name is disabled",affiliateHotelApiPageGenConfig.checkPartnerNameIsDisabled(),is(false));
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testHotelAPIGenConfigPartnerIdDisabled() {
        Map<String, String> param = getParam("testHotelAPIGenConfigPartnerIdDisabled");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelApiPageGenConfig<?> affiliateHotelApiPageGenConfig = new AffiliateHotelApiPageGenConfig<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user logs in to the Admin dashboard.");
        affiliateHotelApiPageGenConfig.goToHotelAPIPage();

        step("The user navigate to the Hotel API page");
        affiliateHotelApiPageGenConfig.clickHotelApiSection();

        step("The user navigate to the Gen Config page");
        affiliateHotelApiPageGenConfig.clickGenConfigHotelAPI();

        step("The user verifies that partner id is disabled");
        Assertion.assertThat("user verifies that partner id is disabled",affiliateHotelApiPageGenConfig.checkPartnerIdIsDisabled(),is(false));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testCreateNewLinkWithRequiredParameters() {
        Map<String, String> param = getParam("testCreateNewLinkWithRequiredParameters");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateUTMManagerPage<?> affiliateUTMManagerPage = new AffiliateUTMManagerPage<>(session);

        AffiliateLinkCreationPage<?> affiliateLinkCreationPage = new AffiliateLinkCreationPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String urlName =  param.get("urlName");
        String destinationURL = param.get("destinationURL");
        String partnerID = param.get("partnerID");
        String utmSource = param.get("utmSource");
        String utmCampaign = param.get("utmCampaign");
        String testDescription = param.get("testDescription");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to UTM Manager page.");
        affiliateUTMManagerPage.gotoUTMManagerPage();

        step("The user clicks on Add New Button.");
        affiliateLinkCreationPage.clickAddNewButton();

        step("The user enters the URL Name");
        affiliateLinkCreationPage.addURLName(urlName);

        step("The user enters the Destination URL");
        affiliateLinkCreationPage.addDestinationURL(destinationURL);

        step("The user selects the Partner ID");
        affiliateLinkCreationPage.enterPartnerId(partnerID);

        step("The user enters the UTM Source.");
        affiliateLinkCreationPage.addUTMSource(utmSource);

        step("The user selects the Product Type & Product Term.");
        affiliateLinkCreationPage.selectProductTypeAndProductTerm();

        step("The user enters the UTM Campaign.");
        affiliateLinkCreationPage.enterUTMCampaign(utmCampaign);

        step("The user enters the Test Description.");
        affiliateLinkCreationPage.enterTestDescription(testDescription);

        step("The user generates the link.");
        affiliateLinkCreationPage.clickCreateButton();

        step("The user verifies the link is created.");
        Assertion.assertThat("user verifies Link is visible", affiliateLinkCreationPage.isLinkCreated(), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testDownloadCancellationLogsWithinCustomDateRange() {
        Map<String, String> param = getParam("testDownloadCancellationLogsWithinCustomDateRange");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User clicks on the Download Logs button.");
        affiliateHotelLogPage.clickDownloadLogsButton();

        step("User selects the Cancellation chip.");
        affiliateHotelLogPage.selectCancellationChip();

        step("User selects the Custom chip.");
        affiliateHotelLogPage.selectCustomTimeFrame();

        step("User selects the Start Date.");
        affiliateHotelLogPage.setStartDate();

        step("User selects the End Date.");
        affiliateHotelLogPage.setEndDate();

        step("Verify that Start Date is strictly before End Date.");
        Assertion.assertThat("Start date is strictly before end date", affiliateHotelLogPage.isStartStrictlyBeforeEnd(), is(true));

        step("User clicks the Next button.");
        affiliateHotelLogPage.clickNextButton();

        step("User clicks the Send button.");
        affiliateHotelLogPage.clickSendButton();

        step("Verify that the Cancellation Log Downloaded.");
        Assertion.assertThat("Compile snackbar is visible", affiliateHotelLogPage.isCompileSnackbarVisible(), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchByURLName() {
        Map<String, String> param = getParam("testSearchByURLName");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateUTMManagerPage<?> affiliateUTMManagerPage = new AffiliateUTMManagerPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String urlName = param.get("urlName");
        String affiliateName = param.get("affiliateName");
        String productType = param.get("productType");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to UTM Manager page.");
        affiliateUTMManagerPage.gotoUTMManagerPage();

        step("The user enters the URL Name.");
        affiliateUTMManagerPage.enterURLName(urlName);

        step("The user selects the Affiliate Name or ID.");
        affiliateUTMManagerPage.selectAffiliateById(affiliateName);

        step("The user selects a Product Type.");
        affiliateUTMManagerPage.selectProductType(productType);

        step("The user clicks on Search button.");
        affiliateUTMManagerPage.clickSearchButton();

        step("The user verifies the search result contains the URL Name.");
        Assertion.assertThat("URL Name is visible", affiliateUTMManagerPage.isURLNameVisible(urlName),is(true));
    }

    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateTTDOrderTableSearchByAffRefID() throws InterruptedException {
        Map<String, String> param = getParam("testAffiliateTTDOrderTableSearchByAffRefID");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateTTDOrderTablePage<?> affiliateTTDOrderTablePage= new AffiliateTTDOrderTablePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String affRefID = param.get("affRefID");
        String partnerId = param.get("partnerId");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateTTDOrderTablePage.goToAffiliateLandingPageMenu();

        step("The user navigate to the TTD Order Table page");
        affiliateTTDOrderTablePage.goToTTDOrderTableMenu();

        step("The user insert the partnerId");
        affiliateTTDOrderTablePage.searchTTDOrderByPartnerID(partnerId);

        step("The user insert the Affiliate Reference ID");
        affiliateTTDOrderTablePage.searchTTDOrderByAffRefID(affRefID);

        step("The user click the search button");
        affiliateTTDOrderTablePage.clickTTDOrderSearchBTN();

        step("The user verifies the search result contains the Affiliate Reference ID : " + affRefID);
        Assertion.assertThat("Searched Affiliate Reference ID is displayed   ", affiliateTTDOrderTablePage.getTTDAffRefIDResult(affRefID) ,containsStringIgnoringCase(affRefID));
    }

    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateResetTTDOrderTableResult() throws InterruptedException {
        Map<String, String> param = getParam("testAffiliateResetTTDOrderTableResult");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateTTDOrderTablePage<?> affiliateTTDOrderTablePage= new AffiliateTTDOrderTablePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String affRefID = param.get("affRefID");
        String partnerId = param.get("partnerId");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateTTDOrderTablePage.goToAffiliateLandingPageMenu();

        step("The user navigate to the TTD Order Table page");
        affiliateTTDOrderTablePage.goToTTDOrderTableMenu();

        step("The user insert the partnerId");
        affiliateTTDOrderTablePage.searchTTDOrderByPartnerID(partnerId);

        step("The user insert the Affiliate Reference ID");
        affiliateTTDOrderTablePage.searchTTDOrderByAffRefID(affRefID);

        step("The user click the search button");
        affiliateTTDOrderTablePage.clickTTDOrderSearchBTN();

        step("The user click the reset button");
        affiliateTTDOrderTablePage.clickTTDOrderResetBTN();

        step("The user verifies the search text box is reset and no value present");
        Assertion.assertThat("Searched data is reset now", affiliateTTDOrderTablePage.affRefIDFieldIsEmpty(), emptyString());
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testDownloadPrebookingLogsWithSelectedTimeFrame() {
        Map<String, String> param = getParam("testDownloadPrebookingLogsWithSelectedTimeFrame");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User clicks on the Download Logs button.");
        affiliateHotelLogPage.clickDownloadLogsButton();

        step("User selects the Pre-Booking chip.");
        affiliateHotelLogPage.selectPreBookingChip();

        step("User selects the Custom chip.");
        affiliateHotelLogPage.selectCustomTimeFrame();

        step("User selects the Start Date.");
        affiliateHotelLogPage.setStartDate();

        step("User selects the End Date.");
        affiliateHotelLogPage.setEndDate();

        step("Verify that Start Date is strictly before End Date.");
        Assertion.assertThat("Start date is strictly before end date", affiliateHotelLogPage.isStartStrictlyBeforeEnd(), is(true));

        step("User clicks the Next button.");
        affiliateHotelLogPage.clickNextButton();

        step("User clicks the Send button.");
        affiliateHotelLogPage.clickSendButton();

        step("Verify that the Pre Booking Log Downloaded.");
        Assertion.assertThat("Compile snack bar is visible", affiliateHotelLogPage.isCompileSnackbarVisible(), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testDownloadBookingLogsWithSelectedTimeFrame() {
        Map<String, String> param = getParam("testDownloadBookingLogsWithSelectedTimeFrame");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User clicks on the Download Logs button.");
        affiliateHotelLogPage.clickDownloadLogsButton();

        step("User selects the Booking chip.");
        affiliateHotelLogPage.selectBookingChip();

        step("User selects the Custom chip.");
        affiliateHotelLogPage.selectCustomTimeFrame();

        step("User selects the Start Date.");
        affiliateHotelLogPage.setStartDate();

        step("User selects the End Date.");
        affiliateHotelLogPage.setEndDate();

        step("Verify that Start Date is strictly before End Date.");
        Assertion.assertThat("Start date is strictly before end date", affiliateHotelLogPage.isStartStrictlyBeforeEnd(), is(true));

        step("User clicks the Next button.");
        affiliateHotelLogPage.clickNextButton();

        step("User clicks the Send button.");
        affiliateHotelLogPage.clickSendButton();

        step("Verify that the Booking Log Downloaded.");
        Assertion.assertThat("Compile snack bar is visible", affiliateHotelLogPage.isCompileSnackbarVisible(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testTTDWhitelistProductRemoveBtn() {
        Map<String, String> param = getParam("testTTDWhitelistProductRemoveBtn");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateTTDWhitelist<?> affiliateTTDWhitelist = new AffiliateTTDWhitelist<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigates to the whitelist page.");
        affiliateTTDWhitelist.clickTTDWhitelist();

        step("The user clicks on remove product button.");
        affiliateTTDWhitelist.removeProductFromWhitelist();

        step("The user verifies if the button is successfully clicked and the product is removed.");
        Assertion.assertThat("Verify the product is removed", affiliateTTDWhitelist.isSnackbarAppeared(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testTTDAddNewPartner() {
        Map<String, String> param = getParam("testTTDAddNewPartner");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateTTDDashboardPage<?> affiliateTTDDashboardPage = new AffiliateTTDDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliateTTDDashboardPage.clickTTD();

        String orderWebhookUrl = param.get("orderWebhookUrl").replace("*", ":");
        String voucherWebhookUrl = param.get("voucherWebhookUrl").replace("*", ":");
        String expectedMessage = param.get("expectedMessage");

        step("The user clicks on add new button and proceed to fill required details");
        affiliateTTDDashboardPage.addNewPartner(orderWebhookUrl, voucherWebhookUrl);

        step("The user verifies if new partner is successfully added.");
        Assertion.assertThat("Verify New Partner is added successfully", affiliateTTDDashboardPage.isNewPartnerAdded(expectedMessage), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testTTDEditWebhook() {
        Map<String, String> param = getParam("testTTDEditWebhook");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateTTDDashboardPage<?> affiliateTTDDashboardPage = new AffiliateTTDDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliateTTDDashboardPage.clickTTD();

        step("The user clicks on edit webhook button to update the webhook URLs");
        affiliateTTDDashboardPage.clickSettings();
        affiliateTTDDashboardPage.clickEditWebhookButton();

        String updatedOrderWebhook = param.get("updatedOrderWebhook").replace("*", ":");
        String updatedVoucherWebhook = param.get("updatedVoucherWebhook").replace("*", ":");
        String expectedMessage = param.get("expectedMessage");

        step("The user clears the existing webhook URLs and enters new ones");
        affiliateTTDDashboardPage.editWebhookUrls(updatedOrderWebhook, updatedVoucherWebhook);

        step("The user verifies if webhook is successfully updated.");
        Assertion.assertThat("Verify webhook is successfully updated", affiliateTTDDashboardPage.editedWebHookSuccessfully(expectedMessage), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testResetFilters() {
        Map<String, String> param = getParam("testResetFilters");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateUTMManagerPage<?> affiliateUTMManagerPage = new AffiliateUTMManagerPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String urlName = param.get("urlName");
        String affiliateName = param.get("affiliateName");
        String productType = param.get("productType");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to UTM Manager page.");
        affiliateUTMManagerPage.gotoUTMManagerPage();

        step("The user enters the URL Name.");
        affiliateUTMManagerPage.enterURLName(urlName);

        step("The user selects the Affiliate Name or ID.");
        affiliateUTMManagerPage.selectAffiliateById(affiliateName);

        step("The user selects a Product Type.");
        affiliateUTMManagerPage.selectProductType(productType);

        step("The user clicks on Search button.");
        affiliateUTMManagerPage.clickSearchButton();

        step("The user clicks on Reset button.");
        affiliateUTMManagerPage.clickResetFilterButton();

        step("The user verifies that all filter fields are cleared after clicking Reset Filter.");
        Assertion.assertThat("All filter fields should be empty", affiliateUTMManagerPage.areAllFilterFieldsEmpty(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateHotelWhitelistFileLogButton() {
        Map<String, String> param = getParam("testAffiliateHotelWhitelistFileLogButton");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelWhitelist<?> affiliateHotelWhitelist = new AffiliateHotelWhitelist<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigate to hotel API menu");
        affiliateHotelWhitelist.clickHotelAPIManualWhitelist();

        step("The user clicks on file log button on the top right of the page");
        affiliateHotelWhitelist.clickFileLogButton();

        step("Verify if the user is successfully navigated to the file log page");
        Assertion.assertThat("Verify user has successfully navigated to the file log page", affiliateHotelWhitelist.isOnFileLogPage(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateHotelVendorToggleButton() {
        Map<String, String> param = getParam("testAffiliateHotelVendorToggleButton");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelWhitelist<?> affiliateHotelWhitelist = new AffiliateHotelWhitelist<>(session);
        AffiliateHotelWhitelistVendorPage<?> affiliateHotelWhitelistVendorPage = new AffiliateHotelWhitelistVendorPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigate to vendor API menu");
        affiliateHotelWhitelist.clickHotelAPIManualWhitelist();

        step("The user clicks on vendor button");
        affiliateHotelWhitelist.clickVendorButton();

        step("The user clicks on vendor toggle button");
        affiliateHotelWhitelistVendorPage.clickVendorToggle1();

        step("The user clicks on yes button to change the toggle status");
        affiliateHotelWhitelistVendorPage.clickYesButton();

        step("Verify if the user is successfully navigated to the file log page");
        Assertion.assertThat("Verify user has successfully changes the vendor's toggle status", affiliateHotelWhitelistVendorPage.isExpediaWhitelistSnackbarToggleDisplayed(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateHotelSearchBy2FilterField() {
        Map<String, String> param = getParam("testAffiliateHotelSearchBy2FilterField");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelWhitelist<?> affiliateHotelWhitelist = new AffiliateHotelWhitelist<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String expectedCity = param.get("expectedCity");
        String expectedCountry = param.get("expectedCountry");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigate to hotel API menu");
        affiliateHotelWhitelist.clickHotelAPIManualWhitelist();

        step("The user clicks on filter by dropdown 1 button");
        affiliateHotelWhitelist.clickFilter1DropDown();

        step("The user selects the Country option on Filter By 1 Section and pass country");
        affiliateHotelWhitelist.filterByCountry(expectedCountry);

        step("The user clicks on filter by dropdown 2 button");
        affiliateHotelWhitelist.clickFilter2DropDown();

        step("The user selects the City option on Filter By 2 Section and pass city");
        affiliateHotelWhitelist.filterByCity();

        step("The user clicks on search button");
        affiliateHotelWhitelist.clickSearchButtonFilter();

        step("Verify the results from 2 filter search field");
        Assertion.assertThat("Verify the 2 filter field search results", affiliateHotelWhitelist.checkSearchBy2FilterField(expectedCity, expectedCountry), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateHotelWhitelistSearchByHotelId() {
        Map<String, String> param = getParam("testAffiliateHotelWhitelistSearchByHotelId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelWhitelist<?> affiliateHotelWhitelist = new AffiliateHotelWhitelist<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigate to hotel API menu");
        affiliateHotelWhitelist.clickHotelAPIManualWhitelist();

        step("The user clicks on filter by dropdown 1 button");
        affiliateHotelWhitelist.clickFilter1DropDown();

        step("The user selects the Country option on Filter By 1 Section and pass country");
        String hotelId = param.get("hotelId");
        affiliateHotelWhitelist.filterByHotelId(hotelId);

        step("The user clicks on search button");
        affiliateHotelWhitelist.clickSearchButtonFilter();

        step("Verify search by hotelId");
        Assertion.assertThat("Verify expected hotelId is same as actual", affiliateHotelWhitelist.checkSearchByHotelId(hotelId), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testResetSearchResultsWithFilledSearchField() {
        Map<String, String> param = getParam("testResetSearchResultsWithFilledSearchField");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchFiled = param.get("searchField");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User enters the search field.");
        affiliateHotelLogPage.enterSearchField(searchFiled);

        step("User clicks the Search button.");
        affiliateHotelLogPage.clickSearchButton();

        step("User clicks the Reset Search button.");
        affiliateHotelLogPage.clickResetSearchButton();

        step("Verify that Search Field is Empty");
        Assertion.assertThat("Empty Search Field is visible", affiliateHotelLogPage.isSearchFieldEmpty(), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testEmailConfirmationAfterSelectingEventAndTimeframeChips() {
        Map<String, String> param = getParam("testEmailConfirmationAfterSelectingEventAndTimeframeChips");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchFiled = param.get("searchField");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User enters the search field.");
        affiliateHotelLogPage.enterSearchField(searchFiled);

        step("User clicks on the Download Logs button.");
        affiliateHotelLogPage.clickDownloadLogsButton();

        step("User selects the Cancellation chip.");
        affiliateHotelLogPage.selectCancellationChip();

        step("User selects the Custom chip.");
        affiliateHotelLogPage.selectCustomTimeFrame();

        step("User selects the Start Date.");
        affiliateHotelLogPage.setStartDate();

        step("User selects the End Date.");
        affiliateHotelLogPage.setEndDate();

        step("Verify that Start Date is strictly before End Date.");
        Assertion.assertThat("Start date is strictly before end date", affiliateHotelLogPage.isStartStrictlyBeforeEnd(), is(true));

        step("User clicks the Next button.");
        affiliateHotelLogPage.clickNextButton();

        step("User clicks the Send button.");
        affiliateHotelLogPage.clickSendButton();

        step("Verify that Email confirmation will be displayed.");
        Assertion.assertThat("Email confirmation is visible", affiliateHotelLogPage.isCompileSnackbarVisible(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateHotelWhiteListAddBulk() {
        Map<String, String> param = getParam("testAffiliateHotelWhiteListAddBulk");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelWhitelist<?> affiliateHotelWhitelist = new AffiliateHotelWhitelist<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigate to hotel API menu");
        affiliateHotelWhitelist.clickHotelAPIManualWhitelist();

        step("The user clicks on add bulk button");
        affiliateHotelWhitelist.clickAddBulkButton();

        step("The user push the file");
        String filePath = param.get("filePath");
        affiliateHotelWhitelist.pushData(filePath);

        step("The user clicks on add whitelist button");
        affiliateHotelWhitelist.clickAddWhitelistButton();

        step("Verify the add bulk hotel Id is successfully added");
        String expectedSnackbarText = param.get("expectedSnackbarText");
        Assertion.assertThat("Verify the add bulk hotel Id is successfully added", affiliateHotelWhitelist.checkHotelWhitelistedSnackbar(expectedSnackbarText), is(true));
    }

    @Author(names = {"Nishtha", "Vidhi Jindal"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateHotelWhiteListRemoveBulk() throws InterruptedException {
        Map<String, String> param = getParam("testAffiliateHotelWhiteListRemoveBulk");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelWhitelist<?> affiliateHotelWhitelist = new AffiliateHotelWhitelist<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigate to hotel API menu");
        affiliateHotelWhitelist.clickHotelAPIManualWhitelist();

        step("The user clicks on remove button");
        affiliateHotelWhitelist.clickRemoveBulkButton();

        step("The user selects the country to remove bulk hotel");
        affiliateHotelWhitelist.removeBulkWhitelistHotel();

        step("Verify the bulk hotel is successfully removed");
        String expectedSnackbarText = param.get("expectedSnackbarText");
        Assertion.assertThat("Verify the bulk hotel is successfully removed", affiliateHotelWhitelist.checkHotelWhitelistedSnackbar(expectedSnackbarText), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateHotelWhiteListDownLoadData() {
        Map<String, String> param = getParam("testAffiliateHotelWhiteListDownLoadData");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelWhitelist<?> affiliateHotelWhitelist = new AffiliateHotelWhitelist<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigate to hotel API menu");
        affiliateHotelWhitelist.clickHotelAPIManualWhitelist();

        step("The user clicks on Download Data button");
        affiliateHotelWhitelist.clickDownloadDataButton();

        step("The user fill the details to download the data.");
        affiliateHotelWhitelist.fillDownLoadDataDetails();

        step("The user clicks on download button.");
        affiliateHotelWhitelist.clickDownloadButton();

        step("Verify the data download is successful");
        String expectedSnackbarText = param.get("expectedSnackbarText");
        Assertion.assertThat("Verify the data is successfully downloaded", affiliateHotelWhitelist.checkHotelWhitelistedSnackbar(expectedSnackbarText), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testGeneralConfigSaveButton() {
        Map<String, String> param = getParam("testGeneralConfigSaveButton");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelApiPageGenConfig<?> affiliateHotelApiPageGenConfig = new AffiliateHotelApiPageGenConfig<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String maxPropertyIds = param.get("maxPropertyIds");
        String amountInIdr = param.get("amountInIdr");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Hotel Dashboard page.");
        affiliateHotelDashboardPage.clickHotel();

        step("The user navigate to the Gen Config page");
        affiliateHotelApiPageGenConfig.goToGenerateConfigHotel();

        step("The user sets the general config values and clicks save button");
        affiliateHotelApiPageGenConfig.generateConfigHotelApi(maxPropertyIds, amountInIdr);

        step("The user verifies that the save button is successful");
        Assertion.assertThat("user verifies that save button is successful", affiliateHotelApiPageGenConfig.isSaveSuccess(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testGeneralConfigDisplaysCorrectPartnerData() {
        Map<String, String> param = getParam("testGeneralConfigDisplaysCorrectPartnerData");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelApiPageGenConfig<?> affiliateHotelApiPageGenConfig = new AffiliateHotelApiPageGenConfig<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Hotel Dashboard page.");
        affiliateHotelDashboardPage.clickHotel();

        step("The user navigate to the Gen Config page");
        affiliateHotelApiPageGenConfig.goToGenerateConfigHotel();

        step("The user verifies that partner data is displayed");
        Assertion.assertThat("user verifies that partner data is displayed",affiliateHotelApiPageGenConfig.isPartnerDataDisplayed(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testMaxNumberOfPropertyFieldCannotBeEmpty() {
        Map<String, String> param = getParam("testMaxNumberOfPropertyFieldCannotBeEmpty");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelApiPageGenConfig<?> affiliateHotelApiPageGenConfig = new AffiliateHotelApiPageGenConfig<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Hotel Dashboard page.");
        affiliateHotelDashboardPage.clickHotel();

        step("The user navigate to the Gen Config page");
        affiliateHotelApiPageGenConfig.goToGenerateConfigHotel();

        step("The user clears the Max Number of Property IDs field");
        affiliateHotelApiPageGenConfig.setMaxPropertyIdsInput("");
        affiliateHotelApiPageGenConfig.clickSaveButton();

        step("The user verifies that the Max Number of Property IDs field cannot be empty");
        Assertion.assertThat("user verifies the error message",affiliateHotelApiPageGenConfig.isMaxPropertyErrMsgDisplayed(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testMaxNumberOfPropertyFieldLimit() {
        Map<String, String> param = getParam("testMaxNumberOfPropertyFieldLimit");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelApiPageGenConfig<?> affiliateHotelApiPageGenConfig = new AffiliateHotelApiPageGenConfig<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String maxPropertyIds = param.get("maxPropertyIds");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Hotel Dashboard page.");
        affiliateHotelDashboardPage.clickHotel();

        step("The user navigate to the Gen Config page");
        affiliateHotelApiPageGenConfig.goToGenerateConfigHotel();

        step("The user clears the Max Number of Property IDs field");
        affiliateHotelApiPageGenConfig.setMaxPropertyIdsInput(maxPropertyIds);
        affiliateHotelApiPageGenConfig.clickSaveButton();

        step("The user verifies that the Max Number of Property IDs field cannot be empty");
        Assertion.assertThat("user verifies the error message",affiliateHotelApiPageGenConfig.isMaxPropertyErrMsgDisplayed(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testAffiliateTTDBusinessLogSearchByPartnerName() {
        Map<String, String> param = getParam("testAffiliateTTDBusinessLogSearchByPartnerName");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateTtdApiPage<?> affiliateTtdApiPage = new AffiliateTtdApiPage<>(session);
        AffiliateTTDBusinessLog<?> affiliateTTDBusinessLog = new AffiliateTTDBusinessLog<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("Click on TTD API");
        affiliateTtdApiPage.clickTtdApiSection();

        step("The user navigated to ttd api business log page");
        affiliateTtdApiPage.clickTtdApiBusinessLog();

        step("The user clicked on search and search by partner name");
        String partnerInput = param.get("partnerInput");
        affiliateTTDBusinessLog.searchByPartnerInput(partnerInput);

        step("The user clicked on search button");
        affiliateTTDBusinessLog.clickSearchButton();

        step("The user verify the search by partner name");
        Assertion.assertThat("Search is showing in list", affiliateTTDBusinessLog.checkTTDBusinessLogSearch(partnerInput), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testAffiliateTTDBusinessLogSearchByPartnerId() {
        Map<String, String> param = getParam("testAffiliateTTDBusinessLogSearchByPartnerId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateTtdApiPage<?> affiliateTtdApiPage = new AffiliateTtdApiPage<>(session);
        AffiliateTTDBusinessLog<?> affiliateTTDBusinessLog = new AffiliateTTDBusinessLog<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("Click on TTD API");
        affiliateTtdApiPage.clickTtdApiSection();

        step("The user navigated to ttd api business log page");
        affiliateTtdApiPage.clickTtdApiBusinessLog();

        step("The user clicked on search and search by partner id");
        String partnerInput = param.get("partnerInput");
        affiliateTTDBusinessLog.searchByPartnerInput(partnerInput);

        step("The user clicked on search button");
        affiliateTTDBusinessLog.clickSearchButton();

        step("The user verify the search by partner Id.");
        Assertion.assertThat("Search is showing in list", affiliateTTDBusinessLog.checkTTDBusinessLogSearch(partnerInput), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testAffiliateTTDBusinessLogResetSearch() {
        Map<String, String> param = getParam("testAffiliateTTDBusinessLogResetSearch");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateTtdApiPage<?> affiliateTtdApiPage = new AffiliateTtdApiPage<>(session);
        AffiliateTTDBusinessLog<?> affiliateTTDBusinessLog = new AffiliateTTDBusinessLog<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("Click on TTD API");
        affiliateTtdApiPage.clickTtdApiSection();

        step("The user navigated to ttd api business log page");
        affiliateTtdApiPage.clickTtdApiBusinessLog();

        step("The user clicked on search and search by partner name");
        String partnerInput = param.get("partnerInput");
        affiliateTTDBusinessLog.searchByPartnerInput(partnerInput);

        step("The user clicked on search button");
        affiliateTTDBusinessLog.clickSearchButton();

        step("The user clicked on reset button");
        affiliateTTDBusinessLog.clickResetButton();

        step("The user verifies the search text box is reset and no value present");
        Assertion.assertThat("Searched data is reset now", affiliateTTDBusinessLog.searchTextBoxIsEmpty(), emptyString());
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testAffiliateTTDBusinessLogEmailConfirmation() {
        Map<String, String> param = getParam("testAffiliateTTDBusinessLogEmailConfirmation");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateTtdApiPage<?> affiliateTtdApiPage = new AffiliateTtdApiPage<>(session);
        AffiliateTTDBusinessLog<?> affiliateTTDBusinessLog = new AffiliateTTDBusinessLog<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("Click on TTD API");
        affiliateTtdApiPage.clickTtdApiSection();

        step("The user navigated to ttd api business log page");
        affiliateTtdApiPage.clickTtdApiBusinessLog();

        step("The user clicked on download logs button");
        affiliateTTDBusinessLog.clickDownloadLogsButton();

        step("The user clicked on next button");
        affiliateTTDBusinessLog.clickNextButton();

        step("The user clicked on send button");
        affiliateTTDBusinessLog.clickSendButton();

        step("The user verifies that success message is displayed");
        String expectedSnackbarText = param.get("expectedSnackbarText");
        Assertion.assertThat("Verify success message is displayed", affiliateTTDBusinessLog.checkTTDBusinessLogSnackbar(expectedSnackbarText), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testAffiliateTTDBusinessLogStartTimePopup() {
        Map<String, String> param = getParam("testAffiliateTTDBusinessLogStartTimePopup");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateTtdApiPage<?> affiliateTtdApiPage = new AffiliateTtdApiPage<>(session);
        AffiliateTTDBusinessLog<?> affiliateTTDBusinessLog = new AffiliateTTDBusinessLog<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("Click on TTD API");
        affiliateTtdApiPage.clickTtdApiSection();

        step("The user navigated to ttd api business log page");
        affiliateTtdApiPage.clickTtdApiBusinessLog();

        step("The user clicked on download logs button");
        affiliateTTDBusinessLog.clickDownloadLogsButton();

        step("The user clicked on start time input box");
        affiliateTTDBusinessLog.clickStartTimeInput();

        step("The user verifies that set time popup is displayed");
        Assertion.assertThat("Verify that set time popup is displayed", affiliateTTDBusinessLog.isStartTimePopupDisplayed(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testAffiliateTTDBusinessLogStartDateCalendarDisplayed() {
        Map<String, String> param = getParam("testAffiliateTTDBusinessLogStartDateCalendarDisplayed");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateTtdApiPage<?> affiliateTtdApiPage = new AffiliateTtdApiPage<>(session);
        AffiliateTTDBusinessLog<?> affiliateTTDBusinessLog = new AffiliateTTDBusinessLog<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("Click on TTD API");
        affiliateTtdApiPage.clickTtdApiSection();

        step("The user navigated to ttd api business log page");
        affiliateTtdApiPage.clickTtdApiBusinessLog();

        step("The user clicked on download logs button");
        affiliateTTDBusinessLog.clickDownloadLogsButton();

        step("The user clicked on start date input box");
        affiliateTTDBusinessLog.clickStartDateInput();

        step("The user verifies that calendar container is displayed");
        Assertion.assertThat("Verify that calendar container is displayed", affiliateTTDBusinessLog.isCalendarContainerDisplayed(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testAffiliateTTDBusinessLogDownloadLogsButton() {
        Map<String, String> param = getParam("testAffiliateTTDBusinessLogDownloadLogsButton");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateTtdApiPage<?> affiliateTtdApiPage = new AffiliateTtdApiPage<>(session);
        AffiliateTTDBusinessLog<?> affiliateTTDBusinessLog = new AffiliateTTDBusinessLog<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("Click on TTD API");
        affiliateTtdApiPage.clickTtdApiSection();

        step("The user navigated to ttd api business log page");
        affiliateTtdApiPage.clickTtdApiBusinessLog();

        step("The user clicked on download logs button");
        affiliateTTDBusinessLog.clickDownloadLogsButton();

        step("The user verifies that the compiled log confirmation pop up is displayed");
        Assertion.assertThat("Verify that compiled log confirmation pop up is displayed", affiliateTTDBusinessLog.isCompiledLogsModalHeadingDisplayed(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testAffiliateTTDBusinessLogEmailSentEstimationDate() {
        Map<String, String> param = getParam("testAffiliateTTDBusinessLogEmailSentEstimationDate");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateTtdApiPage<?> affiliateTtdApiPage = new AffiliateTtdApiPage<>(session);
        AffiliateTTDBusinessLog<?> affiliateTTDBusinessLog = new AffiliateTTDBusinessLog<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("Click on TTD API");
        affiliateTtdApiPage.clickTtdApiSection();

        step("The user navigated to ttd api business log page");
        affiliateTtdApiPage.clickTtdApiBusinessLog();

        step("The user clicked on download logs button");
        affiliateTTDBusinessLog.clickDownloadLogsButton();

        step("The user clicked on next button");
        affiliateTTDBusinessLog.clickNextButton();

        step("The user verifies that estimated date to send email is current date");
        Assertion.assertThat("Verify estimated date to send email is current date", affiliateTTDBusinessLog.isSentDateToday(), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testAffiliateTTDBusinessLogConfirmationInfoVerification() {
        Map<String, String> param = getParam("testAffiliateTTDBusinessLogConfirmationInfoVerification");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateTtdApiPage<?> affiliateTtdApiPage = new AffiliateTtdApiPage<>(session);
        AffiliateTTDBusinessLog<?> affiliateTTDBusinessLog = new AffiliateTTDBusinessLog<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("Click on TTD API");
        affiliateTtdApiPage.clickTtdApiSection();

        step("The user navigated to ttd api business log page");
        affiliateTtdApiPage.clickTtdApiBusinessLog();

        step("The user clicked on download logs button");
        affiliateTTDBusinessLog.clickDownloadLogsButton();

        step("The user verifies the confirmation info are the same with the selected information");
        Assertion.assertThat("Verify the confirmation info are the same with the selected information", affiliateTTDBusinessLog.autoSetCompiledLogsData(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testMaxNumberOfPropertyFieldNoDecimal() {
        Map<String, String> param = getParam("testMaxNumberOfPropertyFieldNoDecimal");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelApiPageGenConfig<?> affiliateHotelApiPageGenConfig = new AffiliateHotelApiPageGenConfig<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String maxPropertyIds = param.get("maxPropertyIds");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Hotel Dashboard page.");
        affiliateHotelDashboardPage.clickHotel();

        step("The user navigate to the Gen Config page");
        affiliateHotelApiPageGenConfig.clickHotelApiSection();
        affiliateHotelApiPageGenConfig.clickGenConfigHotelAPI();

        step("The user enters the Max Number of Property IDs field");
        affiliateHotelApiPageGenConfig.setMaxPropertyIdsInput(maxPropertyIds);
        affiliateHotelApiPageGenConfig.clickSaveButton();

        step("The user verifies that the Max Number of Property IDs field does not accept decimal values");
        Assertion.assertThat("user verifies the error message",affiliateHotelApiPageGenConfig.isMaxPropertyErrMsgDisplayed(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testMaxNumberOfPropertyFieldNumberOnly() {
        Map<String, String> param = getParam("testMaxNumberOfPropertyFieldNumberOnly");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelApiPageGenConfig<?> affiliateHotelApiPageGenConfig = new AffiliateHotelApiPageGenConfig<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String maxPropertyIds = param.get("maxPropertyIds");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Hotel Dashboard page.");
        affiliateHotelDashboardPage.clickHotel();

        step("The user navigate to the Gen Config page");
        affiliateHotelApiPageGenConfig.clickHotelApiSection();
        affiliateHotelApiPageGenConfig.clickGenConfigHotelAPI();

        step("The user enters the Max Number of Property IDs field");
        affiliateHotelApiPageGenConfig.setMaxPropertyIdsInput(maxPropertyIds);
        affiliateHotelApiPageGenConfig.clickSaveButton();

        step("The user verifies that the Max Number of Property IDs field does not accept non-numeric values");
        Assertion.assertThat("user verifies the error message",affiliateHotelApiPageGenConfig.isMaxPropertyErrMsgDisplayed(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testMaxNumberOfPropertyFieldCanBeFilled() {
        Map<String, String> param = getParam("testMaxNumberOfPropertyFieldCanBeFilled");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelApiPageGenConfig<?> affiliateHotelApiPageGenConfig = new AffiliateHotelApiPageGenConfig<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String maxPropertyIds = param.get("maxPropertyIds");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Hotel Dashboard page.");
        affiliateHotelDashboardPage.clickHotel();

        step("The user navigate to the Gen Config page");
        affiliateHotelApiPageGenConfig.clickHotelApiSection();
        affiliateHotelApiPageGenConfig.clickGenConfigHotelAPI();

        step("The user enters the Max Number of Property IDs field");
        affiliateHotelApiPageGenConfig.setMaxPropertyIdsInput(maxPropertyIds);
        affiliateHotelApiPageGenConfig.clickSaveButton();

        step("The user verifies that the Max Number of Property IDs field can be filled with a valid number");
        Assertion.assertThat("user verifies that  Max Number of Property IDs is saved", affiliateHotelApiPageGenConfig.isSaveSuccess(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAccessMaxHotelIdConfigPage() {
        Map<String, String> param = getParam("testAccessMaxHotelIdConfigPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelApiPageGenConfig<?> affiliateHotelApiPageGenConfig = new AffiliateHotelApiPageGenConfig<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Hotel Dashboard page.");
        affiliateHotelDashboardPage.clickHotel();

        step("The user navigate to the Gen Config page");
        affiliateHotelApiPageGenConfig.clickHotelApiSection();
        affiliateHotelApiPageGenConfig.clickGenConfigHotelAPI();

        step("The user verifies that the Max Number of Property IDs field is present");
        Assertion.assertThat("user verified MaxPropertyIds is displayed", affiliateHotelApiPageGenConfig.isMaxPropertyIdsInputDisplayed(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAddNewPartnerPWA(){
        Map<String, String> param = getParam("testAddNewPartnerPWA");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePWAPage<?> affiliatePWAPage = new AffiliatePWAPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");
        String productName = param.get("productName") + "-" + System.currentTimeMillis();
        String productIconURL = param.get("productIconURL");
        String productURL = param.get("productURL");
        String partnerLogoURL = param.get("partnerLogoURL");
        String headerText = param.get("headerText");
        String urlDescription = param.get("urlDescription");
        String urlText = param.get("urlText");
        String url = param.get("url");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the PWA dashboard.");
        affiliatePWAPage.goToPage();

        step("The user clicks on Add New button and selects a Partner.");
        affiliatePWAPage.selectPartnerName(partnerName);

        step("The user fills all required fields and creates a new PWA by saving the form");
        affiliatePWAPage.createNewPWA(partnerLogoURL, headerText, productName, productIconURL, productURL, urlDescription, urlText, url);


        step("The user verifies if the new PWA is successfully created.");
        Assertion.assertThat("Verify newly added PWA  is saved", affiliatePWAPage.isPwaSuccessPopupDisplayed(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testOrderTableHotelAffiliateAndReferenceID() {
        Map<String, String> param = getParam("testOrderTableHotelAffiliateAndReferenceID");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateOrderTableInternalDashboard<?> affiliateOrderTableInternalDashboard = new AffiliateOrderTableInternalDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");
        String refId = param.get("refId");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        memberLoginPage.goToPage("/admin/affiliate");

        step("Click on Order Table:");
        affiliateOrderTableInternalDashboard.clickOrderTable();

        step("The user clicks on Affiliate and Reference ID button");
        affiliateOrderTableInternalDashboard.clickAffiliateAndReferenceIdButton();

        step("The user enters the Partner Name and Reference ID");
        affiliateOrderTableInternalDashboard.searchByPartnerAndReferenceId(partnerName, refId);

        step("The user clicks on Search button");
        affiliateOrderTableInternalDashboard.clickOrderTableSearchButton();

        step("The user verifies that the data is displayed based on Partner Name and Reference ID");
        Assertion.assertThat("Verify that data is displayed based on Partner Name and Reference ID", affiliateOrderTableInternalDashboard.isAffiliateRefIdDisplayed(refId),is(true));
    }

    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAffiliateSearchPartnerIdHotelBizlog() {
        Map<String, String> param = getParam("testAffiliateSearchPartnerIdHotelBizlog");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage= new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerID = param.get("partnerID");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Hotel API Business Log Page");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("The user insert the PartnerID");
        affiliateHotelLogPage.searchPartnerHotelBizlogByPartnerID(partnerID);

        step("The user click the Search Button");
        affiliateHotelLogPage.clickSearchFilterButton();

        step("The user verifies the search result contains the PartnerID : " + partnerID);
        Assertion.assertThat("Searched PartnerID is displayed   ", affiliateHotelLogPage.getPartnerIDResult(partnerID) ,containsStringIgnoringCase(partnerID));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testDownloadLogsShowsCompiledConfirmationPopup () {
        Map<String, String> param = getParam("testDownloadLogsShowsCompiledConfirmationPopup");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User clicks on the Download Logs button.");
        affiliateHotelLogPage.clickDownloadLogsButton();

        step("User selects the Event chip.");
        affiliateHotelLogPage.selectBookingChip();

        step("User selects the TimeFrame chip.");
        affiliateHotelLogPage.selectCustomTimeFrame();

        step("User selects the Start Date.");
        affiliateHotelLogPage.setStartDate();

        step("User selects the End Date.");
        affiliateHotelLogPage.setEndDate();

        step("Verify that Start Date is strictly before End Date.");
        Assertion.assertThat("Start date is strictly before end date", affiliateHotelLogPage.isStartStrictlyBeforeEnd(), is(true));

        step("User clicks the Next button.");
        affiliateHotelLogPage.clickNextButton();

        step("Verify that the Compiled log confirmation popup should be displayed.");
        Assertion.assertThat("Compiled log confirmation popup should be displayed", affiliateHotelLogPage.isCompiledLogPopupVisible(), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testStartDateFieldDisplaysCalendarPopup () {
        Map<String, String> param = getParam("testStartDateFieldDisplaysCalendarPopup");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User clicks on the Download Logs button.");
        affiliateHotelLogPage.clickDownloadLogsButton();

        step("User clicks on the Start Date field.");
        affiliateHotelLogPage.clickStartDateBox();

        step("Verify that the Start Date field displays a calendar popup.");
        Assertion.assertThat("Start Date field displays a calendar popup", affiliateHotelLogPage.isStartDateCalendarPopupVisible(), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testStartTimeFieldDisplaysTimePopup () {
        Map<String, String> param = getParam("testStartTimeFieldDisplaysTimePopup");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User clicks on the Download Logs button.");
        affiliateHotelLogPage.clickDownloadLogsButton();

        step("User clicks on the Start Time field.");
        affiliateHotelLogPage.clickStartTimeBox();

        step("Verify that the Start Time field displays a Time popup.");
        Assertion.assertThat("Start Time field displays a Time popup", affiliateHotelLogPage.isStartTimePOPupVisible(), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testConfirmationInfoMatchesSelectedInfo () {
        Map<String, String> param = getParam("testConfirmationInfoMatchesSelectedInfo");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User clicks on the Download Logs button.");
        affiliateHotelLogPage.clickDownloadLogsButton();

        step("User selects the Event chip.");
        affiliateHotelLogPage.selectBookingChip();

        step("User selects the TimeFrame chip.");
        affiliateHotelLogPage.selectCustomTimeFrame();

        step("User selects the Start Date.");
        affiliateHotelLogPage.setStartDate();

        step("User selects the End Date.");
        affiliateHotelLogPage.setEndDate();

        step("Verify that Start Date is strictly before End Date.");
        Assertion.assertThat("Start date is strictly before end date", affiliateHotelLogPage.isStartStrictlyBeforeEnd(), is(true));

        step("User clicks the Next button.");
        affiliateHotelLogPage.clickNextButton();

        step("Verify that the confirmation info should be Matched.");
        Assertion.assertThat("Confirmation Info should be Matched", affiliateHotelLogPage.isCompiledInfoMatches(username), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testEstimatedTimeShowsCurrentDateTime () {
        Map<String, String> param = getParam("testEstimatedTimeShowsCurrentDateTime");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User clicks on the Download Logs button.");
        affiliateHotelLogPage.clickDownloadLogsButton();

        step("User selects the Event chip.");
        affiliateHotelLogPage.selectBookingChip();

        step("User selects the TimeFrame chip.");
        affiliateHotelLogPage.selectCustomTimeFrame();

        step("User selects the Start Date.");
        affiliateHotelLogPage.setStartDate();

        step("User selects the End Date.");
        affiliateHotelLogPage.setEndDate();

        step("Verify that Start Date is strictly before End Date.");
        Assertion.assertThat("Start date is strictly before end date", affiliateHotelLogPage.isStartStrictlyBeforeEnd(), is(true));

        step("User clicks the Next button.");
        affiliateHotelLogPage.clickNextButton();

        step("Verify that the Estimated date & time match with current date and time.");
        Assertion.assertThat("Estimated date & time matched with current date and time", affiliateHotelLogPage.isDateTimeMatch(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testSearchTransactionPage() {
        Map<String, String> param = getParam("testSearchTransactionPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateHistoryTransactionPage<?> affiliateHistoryTransactionPage = new AffiliateHistoryTransactionPage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderType = param.get("orderType");
        String type = param.get("type");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);
        loginAffiliate.selectPartner();
        loginAffiliate.isLoggedIn();

        step("The user navigates to transaction page.");
        affiliateHistoryTransactionPage.goToTransactionPage();

        step("The user search for transaction by Order Type, Type and Search Date By");
        affiliateHistoryTransactionPage.searchTransactionByOrderType(orderType, type, startDate, endDate);

        step("The user verifies that the transaction dates are within the specified range");
        Assertion.assertThat("Verifying the transaction dates",affiliateHistoryTransactionPage.verifyTransactionDatesWithinRange(startDate, endDate),is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testResetTransactionPage() {
        Map<String, String> param = getParam("testResetTransactionPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateHistoryTransactionPage<?> affiliateHistoryTransactionPage = new AffiliateHistoryTransactionPage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderType = param.get("orderType");
        String type = param.get("type");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);
        loginAffiliate.selectPartner();
        loginAffiliate.isLoggedIn();

        step("The user navigates to transaction page.");
        affiliateHistoryTransactionPage.goToTransactionPage();

        step("The user search for transaction by Order Type, Type and Search Date By");
        affiliateHistoryTransactionPage.searchTransactionByOrderType(orderType, type, startDate, endDate);

        step("The user verifies that the transaction dates are within the specified range");
        Assertion.assertThat("Verifying the transaction dates",affiliateHistoryTransactionPage.verifyTransactionDatesWithinRange(startDate, endDate),is(true));

        step("The user clicks on reset button to reset the filter");
        affiliateHistoryTransactionPage.clickResetButton();

        step("The user verifies that the filter is reset");
        Assertion.assertThat("Verifying that the filter is reset", affiliateHistoryTransactionPage.isFilterReset(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testDownloadTransactionPage() {
        Map<String, String> param = getParam("testDownloadTransactionPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateHistoryTransactionPage<?> affiliateHistoryTransactionPage = new AffiliateHistoryTransactionPage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderType = param.get("orderType");
        String type = param.get("type");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);
        loginAffiliate.selectPartner();
        loginAffiliate.isLoggedIn();

        step("The user navigates to transaction page.");
        affiliateHistoryTransactionPage.goToTransactionPage();

        step("The user search for transaction by Order Type, Type and Search Date By");
        affiliateHistoryTransactionPage.searchTransactionByOrderType(orderType, type, startDate, endDate);

        step("The user verifies that the transaction dates are within the specified range");
        Assertion.assertThat("Verifying the transaction dates", affiliateHistoryTransactionPage.verifyTransactionDatesWithinRange(startDate, endDate), is(true));


        step("The user clicks on the download button to download the transaction data");
        affiliateHistoryTransactionPage.clickDownloadButton();

        step("The user verifies that the download was successful");
        Assertion.assertThat("Verifying the download success message", affiliateHistoryTransactionPage.isDownloadSuccessful(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testStartAndEndDateCalendarDisplayedOnClick() {
        Map<String, String> param = getParam("testStartAndEndDateCalendarDisplayedOnClick");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateHistoryTransactionPage<?> affiliateHistoryTransactionPage = new AffiliateHistoryTransactionPage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderType = param.get("orderType");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);
        loginAffiliate.selectPartner();
        loginAffiliate.isLoggedIn();

        step("The user navigates to transaction page.");
        affiliateHistoryTransactionPage.goToTransactionPage();

        step("The user clicks on the start date input field to open the calendar");
        affiliateHistoryTransactionPage.searchTransactionByProductAndDate(orderType, startDate, endDate);

        step("The user verifies that the transaction dates are within the specified range");
        Assertion.assertThat("Verifying the transaction dates", affiliateHistoryTransactionPage.verifyTransactionDatesWithinRange(startDate, endDate), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testSearchDateByFilterTransaction() {
        Map<String, String> param = getParam("testSearchDateByFilterTransaction");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateHistoryTransactionPage<?> affiliateHistoryTransactionPage = new AffiliateHistoryTransactionPage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderType = param.get("orderType");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);
        loginAffiliate.selectPartner();
        loginAffiliate.isLoggedIn();

        step("The user navigates to transaction page.");
        affiliateHistoryTransactionPage.goToTransactionPage();

        step("The user searches for transaction by Order Type and Search Date By");
        affiliateHistoryTransactionPage.searchDateBy(orderType, startDate, endDate);

        step("The user verifies that the transaction dates are within the specified range");
        Assertion.assertThat("Verifying the transaction dates", affiliateHistoryTransactionPage.verifyTransactionDatesWithinRange(startDate, endDate), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testTypeFilterDisplaysTransactionTypeOptions() {
        Map<String, String> param = getParam("testTypeFilterDisplaysTransactionTypeOptions");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateHistoryTransactionPage<?> affiliateHistoryTransactionPage = new AffiliateHistoryTransactionPage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderType = param.get("orderType");
        String type = param.get("type");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);
        loginAffiliate.selectPartner();
        loginAffiliate.isLoggedIn();

        step("The user navigates to transaction page.");
        affiliateHistoryTransactionPage.goToTransactionPage();

        step("The user searches for transaction by Order Type and Type");
        affiliateHistoryTransactionPage.searchTransactionByOrderType(orderType, type, startDate, endDate);

        step("The user verifies that the transaction dates are within the specified range");
        Assertion.assertThat("Verifying the transaction dates", affiliateHistoryTransactionPage.verifyTransactionDatesWithinRange(startDate, endDate), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testOrderTypeDropdownDisplaysProductAffiliateOption() {
        Map<String, String> param = getParam("testOrderTypeDropdownDisplaysProductAffiliateOption");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateHistoryTransactionPage<?> affiliateHistoryTransactionPage = new AffiliateHistoryTransactionPage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user navigates to Affiliate Dashboard Login.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);
        loginAffiliate.isLoggedIn();

        step("The user navigates to transaction page.");
        affiliateHistoryTransactionPage.goToTransactionPage();

        step("The user clicks the Order Type dropdown.");
        affiliateHistoryTransactionPage.clickOrderTypeDropdown();

        step("The user verifies that 'Product Affiliate' option is displayed.");
        Assertion.assertThat("'Product Affiliate' option is displayed", affiliateHistoryTransactionPage.isProductAffiliateOptionPresent(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testClickHistoryTransactionNew() {
        Map<String, String> param = getParam("testClickHistoryTransactionNew");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateHistoryTransactionPage<?> affiliateHistoryTransactionPage = new AffiliateHistoryTransactionPage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);

        step("The user navigates to the Finance section and click on dropdown.");
        loginAffiliate.selectPartner();
        loginAffiliate.isLoggedIn();
        affiliateHistoryTransactionPage.clickFinanceDropdown();

        step("The user clicks on History Transaction New.");
        affiliateHistoryTransactionPage.clickHistoryTransactionNew();

        step("The user verifies that the History Transaction New page is displayed.");
        Assertion.assertThat("Verify that the History Transaction New page is displayed", affiliateHistoryTransactionPage.isHistoryTransactionNew(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testSearchByAffiliateReferenceId() {
        Map<String, String> param = getParam("testSearchByAffiliateReferenceId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateHistoryTransactionPage<?> affiliateHistoryTransactionPage = new AffiliateHistoryTransactionPage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String affiliateReferenceId = param.get("affiliateReferenceId");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);

        step("The user navigates to the Finance section and click on dropdown.");
        loginAffiliate.selectPartner();
        loginAffiliate.isLoggedIn();
        affiliateHistoryTransactionPage.clickFinanceDropdown();

        step("The user clicks on Order Table.");
        affiliateHistoryTransactionPage.clickOrderTable();

        step("The user searches by Affiliate Reference ID: " + affiliateReferenceId);
        affiliateHistoryTransactionPage.searchByAffiliateReferenceId(affiliateReferenceId);

        step("The user verifies that the Affiliate Reference ID is displayed in the results.");
        Assertion.assertThat("Verify that the Affiliate Reference ID is displayed in the results", affiliateHistoryTransactionPage.isAffiliateRefIdValueDisplayed(affiliateReferenceId), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testSearchOrderId() {
        Map<String, String> param = getParam("testSearchOrderId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateHistoryTransactionPage<?> affiliateHistoryTransactionPage = new AffiliateHistoryTransactionPage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");

        step("The user navigate to Affiliate Dashboard dLogin.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);

        step("The user navigates to the Finance section and click on dropdown.");
        loginAffiliate.selectPartner();
        loginAffiliate.isLoggedIn();
        affiliateHistoryTransactionPage.clickFinanceDropdown();

        step("The user clicks on Order Table.");
        affiliateHistoryTransactionPage.clickOrderTable();

        step("The user searches by Order ID: " + orderId);
        affiliateHistoryTransactionPage.searchByOrderId(orderId);

        step("The user verifies that the Order ID is displayed in the results.");
        Assertion.assertThat("Verify that the Order ID is displayed in the results", affiliateHistoryTransactionPage.isOrderIdValueDisplayed(orderId), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testAffiliateHotelEditRateKeysDetails() {
        Map<String, String> param = getParam("testAffiliateHotelEditRateKeysDetails");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);
        AffiliateHotelEditRateKeys<?> affiliateHotelPartnerEditRateKeys = new AffiliateHotelEditRateKeys<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");
        String partnerName = param.get("partnerName");

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicked on search");
        affiliateHotelSearch.searchByPartnerId(partnerId);

        step("The user clicks on content sync");
        affiliateHotelPartnerEdit.clickSettings();

        step("The user clicks on edit rate keys dropdown.");
        affiliateHotelPartnerEdit.clickEditContentDetails();

        step("The user verify that edit rate key page contains the same partnerId and partnerName as the search result");
        Assertion.assertThat("Verify the Partner Data displayed correctly", affiliateHotelPartnerEditRateKeys.checkPartnerIdData(partnerId, partnerName), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testAffiliateHotelEditRateKeys() {
        Map<String, String> param = getParam("testAffiliateHotelEditRateKeys");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);
        AffiliateHotelEditRateKeys<?> affiliateHotelPartnerEditRateKeys = new AffiliateHotelEditRateKeys<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicked on search");
        affiliateHotelSearch.searchByPartnerId(partnerId);

        step("The user clicks on content sync");
        affiliateHotelPartnerEdit.clickSettings();

        step("The user clicks on edit rate keys dropdown.");
        affiliateHotelPartnerEditRateKeys.clickEditRatekey();

        step("The user clicks on Save Button.");
        affiliateHotelPartnerEditRateKeys.updateRateKeys();
        affiliateHotelPartnerEditRateKeys.saveUpdatedRateKeyDetails();

        step("The user verify the snackbar text ");
        String expectedSnackbarText = param.get("expectedSnackbarText");
        Assertion.assertThat("Verify the expected snackbar text", affiliateHotelPartnerEditRateKeys.checkSnackbarText(expectedSnackbarText), is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testCommissionRulesFieldsAreClickable() {
        Map<String, String> param = getParam("testCommissionRulesFieldsAreClickable");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateGeneralConfigPage<?> affiliateGeneralConfigPage = new AffiliateGeneralConfigPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section:");
        affiliateNonAPIPage.nonAPISection();

        step("The user clicks on Partner List menu.");
        affiliateNonAPIPage.clickPartnerListMenu();

        step("The user navigates to Commission menu.");
        affiliateGeneralConfigPage.goToCommissionMenu();

        step("The user clicks on Add New Commission button.");
        affiliateGeneralConfigPage.clickAddNewButton();

        step("The user fills in the required fields to create a new commission.");
        boolean isCommissionRulesClickable = affiliateGeneralConfigPage.checkIfCommissionRulesAreClickable();
        affiliateGeneralConfigPage.fillCommissionRules();

        step("The user verifies that the Commission Rules fields are clickable.");
        Assertion.assertThat("The user verifies that the Commission Rules fields are clickable.", isCommissionRulesClickable, is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testCommissionPeriodFieldsAreClickable() {
        Map<String, String> param = getParam("testCommissionPeriodFieldsAreClickable");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateGeneralConfigPage<?> affiliateGeneralConfigPage = new AffiliateGeneralConfigPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section:");
        affiliateNonAPIPage.nonAPISection();

        step("The user clicks on Partner List menu.");
        affiliateNonAPIPage.clickPartnerListMenu();

        step("The user navigates to Commission menu.");
        affiliateGeneralConfigPage.goToCommissionMenu();

        step("The user clicks on Add New Commission button.");
        affiliateGeneralConfigPage.clickAddNewButton();

        step("The user fills in the required fields to create a new commission.");
        boolean isCommissionPeriodDisplayed = affiliateGeneralConfigPage.checkIfCommissionPeriodChipsAreDisplayed();

        step("The user verifies that the Commission Period fields are displayed.");
        Assertion.assertThat("The user verifies that the Commission Period fields are displayed.", isCommissionPeriodDisplayed, is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testCommissionTypePercentageFieldsAreDisplayed() {
        Map<String, String> param = getParam("testCommissionTypePercentageFieldsAreDisplayed");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateGeneralConfigPage<?> affiliateGeneralConfigPage = new AffiliateGeneralConfigPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section:");
        affiliateNonAPIPage.nonAPISection();

        step("The user clicks on Partner List menu.");
        affiliateNonAPIPage.clickPartnerListMenu();

        step("The user navigates to Commission menu.");
        affiliateGeneralConfigPage.goToCommissionMenu();

        step("The user clicks on Add New Commission button.");
        affiliateGeneralConfigPage.clickAddNewButton();

        step("The user fills in the required fields to create a new commission.");
        affiliateGeneralConfigPage.clickTransactionAmountSharing();
        boolean isCommissionValueFieldDisplayed = affiliateGeneralConfigPage.checkIfCommissionValueFieldsAreDisplayed();

        step("The user verifies that the Commission Values fields are displayed.");
        Assertion.assertThat("The user verifies that the Commission Value fields are displayed.", isCommissionValueFieldDisplayed, is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testCommissionTypePercentageCanHaveDecimalValue() {
        Map<String, String> param = getParam("testCommissionTypePercentageCanHaveDecimalValue");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateGeneralConfigPage<?> affiliateGeneralConfigPage = new AffiliateGeneralConfigPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String commissionRuleName = CoreFunctions.randomName();

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section:");
        affiliateNonAPIPage.nonAPISection();

        step("The user clicks on Partner List menu.");
        affiliateNonAPIPage.clickPartnerListMenu();

        step("The user navigates to Commission menu.");
        affiliateGeneralConfigPage.goToCommissionMenu();

        step("The user clicks on Add New Commission button.");
        affiliateGeneralConfigPage.clickAddNewButton();

        step("The user fills in the required fields to create a new commission.");
        affiliateGeneralConfigPage.fillGeneralInfoFields(commissionRuleName);
        affiliateGeneralConfigPage.fillCommissionRules();
        affiliateGeneralConfigPage.clickTransactionAmountSharing();
        String value = param.get("commissionValue");
        affiliateGeneralConfigPage.fillCommissionDetails(value);

        step("The user clicks on Save button to save the commission rule.");
        affiliateGeneralConfigPage.clickSaveButton();

        step("The user verifies that the Commission Values fields are same as passed");
        Assertion.assertThat("The user verifies that the Commission Value fields are same as passed", affiliateGeneralConfigPage.verifyCommissionValue(value), is(true));
    }


    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testCommissionDetailChipsAreClickable() {
        Map<String, String> param = getParam("testCommissionDetailChipsAreClickable");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateGeneralConfigPage<?> affiliateGeneralConfigPage = new AffiliateGeneralConfigPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section:");
        affiliateNonAPIPage.nonAPISection();

        step("The user clicks on Partner List menu.");
        affiliateNonAPIPage.clickPartnerListMenu();

        step("The user navigates to Commission menu.");
        affiliateGeneralConfigPage.goToCommissionMenu();

        step("The user clicks on Add New Commission button.");
        affiliateGeneralConfigPage.clickAddNewButton();

        step("The user verifies that the Commission Detail chips are clickable.");
        Assertion.assertThat("The user verifies that the Commission Detail chips are clickable.", affiliateGeneralConfigPage.isAllCommissionDetailsClickable(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testGeneralInfoFieldsAreClickableAndFillable() {
        Map<String, String> param = getParam("testGeneralInfoFieldsAreClickableAndFillable");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateGeneralConfigPage<?> affiliateGeneralConfigPage = new AffiliateGeneralConfigPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String commissionRuleName = param.get("commissionRuleName");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section:");
        affiliateNonAPIPage.nonAPISection();

        step("The user clicks on Partner List menu.");
        affiliateNonAPIPage.clickPartnerListMenu();

        step("The user navigates to Commission menu.");
        affiliateGeneralConfigPage.goToCommissionMenu();

        step("The user clicks on Add New Commission button.");
        affiliateGeneralConfigPage.clickAddNewButton();

        step("The user fills in the required fields to create a new commission.");
        affiliateGeneralConfigPage.fillGeneralInfoFields(commissionRuleName);

        step("The user verifies that the General Info fields are clickable.");
        Assertion.assertThat("The user verifies that the General Info fields are clickable.", affiliateGeneralConfigPage.checkIfGeneralInfoFieldsAreClickable(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Co-Brand")
    @Vertical(name = "Affiliate")
    @Test
    public void testEditCobrand() throws InterruptedException {
        Map<String, String> param = getParam("testEditCobrand");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateCoBrandDashboardPage<?> affiliateCoBrandDashboardPage = new AffiliateCoBrandDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String cobrandName = param.get("cobrandName");


        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigated to Hotel Page");
        affiliateCoBrandDashboardPage.goToCoBrandDashboardPage();

        step("The user clicks on the Edit button for the CoBrand.");
        affiliateCoBrandDashboardPage.clickEditButton();

        step("The user updates the CoBrand name.");
        affiliateCoBrandDashboardPage.updateBrandName(cobrandName);

        step("The user clicks on the Save button.");
        affiliateCoBrandDashboardPage.clickSaveButton();

        step("The user verifies that the CoBrand is updated successfully.");
        boolean isCobrandUpdated = affiliateCoBrandDashboardPage.isCoBrandUpdated();
        Assertion.assertThat("Verify that the CoBrand is updated successfully", isCobrandUpdated, is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSendButtonDisplaysSuccessMessage() {
        Map<String, String> param = getParam("testSendButtonDisplaysSuccessMessage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User clicks on the Download Logs button.");
        affiliateHotelLogPage.clickDownloadLogsButton();

        step("User selects the Cancellation chip.");
        affiliateHotelLogPage.selectCancellationChip();

        step("User selects the Custom chip.");
        affiliateHotelLogPage.selectCustomTimeFrame();

        step("User selects the Start Date.");
        affiliateHotelLogPage.setStartDate();

        step("User selects the End Date.");
        affiliateHotelLogPage.setEndDate();

        step("Verify that Start Date is strictly before End Date.");
        Assertion.assertThat("Start date is strictly before end date", affiliateHotelLogPage.isStartStrictlyBeforeEnd(), is(true));

        step("User clicks the Next button.");
        affiliateHotelLogPage.clickNextButton();

        step("User clicks the Send button.");
        affiliateHotelLogPage.clickSendButton();

        step("Verify that Success message  will be displayed.");
        Assertion.assertThat("Success Message is visible", affiliateHotelLogPage.isSuccessMessageVisible(), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testResetClearsAndReloadsData() {
        Map<String, String> param = getParam("testResetClearsAndReloadsData");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchField = param.get("searchField");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User passes data on the Search ID field.");
        affiliateHotelLogPage.searchPartnerHotelBizlogByPartnerID(searchField);

        step("The user clicks on the Search button.");
        affiliateHotelLogPage.clickSearchFilterButton();

        step("User verifies that the search results are displayed.");
        Assertion.assertThat("Verify that the search results are displayed", affiliateHotelLogPage.isResultAppeared(searchField), is(true));

        step("User clicks on the Reset button.");
        affiliateHotelLogPage.clickResetSearchButton();

        step("User verifies that the search results are cleared..");
        Assertion.assertThat("Search results are cleared", affiliateHotelLogPage.isSearchFieldEmpty(), is(true));
    }


    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchByPartnerIdVerification() {
        Map<String, String> param = getParam("testSearchByPartnerIdVerification");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User passes data on the Search ID field.");
        affiliateHotelLogPage.searchPartnerHotelBizlogByPartnerID(partnerId);

        step("The user clicks on the Search button.");
        affiliateHotelLogPage.clickSearchFilterButton();

        step("User verifies that the search results With PartnerId are displayed.");
        Assertion.assertThat("Searched PartnerID is displayed   ", affiliateHotelLogPage.getPartnerIDResult(partnerId) ,containsStringIgnoringCase(partnerId));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testVerifyNewCommissionNonAPIPartnerList() {
        Map<String, String> param = getParam("testVerifyNewCommissionNonAPIPartnerList");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);
        AffiliateGeneralConfigPage<?> affiliateGeneralConfigPage = new AffiliateGeneralConfigPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String commissionRuleName = param.get("commissionRuleName") + CoreFunctions.randomName();
        String commissionValue = param.get("commissionValue");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on the Non API Partner");
        affiliateNonAPIPage.nonAPISection();

        step("Click on Partner List");
        affiliateNonAPIPage.clickPartnerListMenu();

        step("Click on Action button > Commission");
        affiliateGeneralConfigPage.goToCommissionMenu();

        step("Click on Add new button");
        affiliateGeneralConfigPage.clickAddNewButton();

        step("Fill the required fields for new Commission");
        affiliateGeneralConfigPage.fillGeneralInfoFields(commissionRuleName);
        affiliateGeneralConfigPage.fillCommissionRules();
        affiliateGeneralConfigPage.clickTransactionAmountSharing();
        affiliateGeneralConfigPage.fillCommissionDetails(commissionValue);

        step("Click on Save button");
        affiliateGeneralConfigPage.clickSaveButton();

        step("Verify Submit button is enabled and click Submit");
        boolean isSubmitEnabled = affiliateGeneralConfigPage.isSubmitButtonEnabled();
        Assertion.assertThat("Submit button should be enabled after filling required fields", isSubmitEnabled, is(true));
        affiliateGeneralConfigPage.clickSubmitButton();

        step("Verify the new Commission is created successfully");
        boolean isCommissionCreated = affiliateGeneralConfigPage.isGeneralConfigCreated();
        Assertion.assertThat("New Commission should be created successfully", isCommissionCreated, is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testUserCanSearchByPartnerName() {
        Map<String, String> param = getParam("testUserCanSearchByPartnerName");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User passes data on the Search Name field.");
        affiliateHotelLogPage.searchPartnerHotelBizlogByPartnerID(partnerName);

        step("The user clicks on the Search button.");
        affiliateHotelLogPage.clickSearchFilterButton();

        step("User verifies that the search results With PartnerName are displayed.");
        Assertion.assertThat("Searched PartnerName is displayed.", affiliateHotelLogPage.isResultAppeared(partnerName), is(true));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Internal DashBoard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testVerifyClickSendButtonTTDAPIBusinessLog() {
        Map<String, String> param = getParam("testVerifyClickSendButtonTTDAPIBusinessLog");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateTtdApiPage<?> affiliateTtdApiPage = new AffiliateTtdApiPage<>(session);
        AffiliateTTDBusinessLog<?> affiliateTTDBusinessLog = new AffiliateTTDBusinessLog<>(session);
        AffiliateCommonPageWeb<?> affiliateCommonPageWeb = AffiliateCommonPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");
        String snackbarText = param.get("snackbarText");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Navigate to Internal Dashboard");
        memberLoginPage.goToPage("/admin/affiliate/hotel");

        step("Click on TTD API");
        affiliateTtdApiPage.clickTtdApiSection();

        step("Click on Business Logs");
        affiliateTtdApiPage.clickTtdApiBusinessLog();

        step("Click on Download logs");
        affiliateTTDBusinessLog.searchByPartnerInput(partnerName);
        affiliateTTDBusinessLog.clickSearchButton();
        affiliateTTDBusinessLog.clickDownloadLogsButton();

        step("Select event, date, time");
        affiliateTTDBusinessLog.clickNextButton();

        step("Click on next > Send");
        affiliateTTDBusinessLog.clickSendButton();

        step("Assertions");
        Assertion.assertThat("Success message must be displayed", affiliateTTDBusinessLog.checkTTDBusinessLogSnackbar(snackbarText), is(true));

        step("Log out");
        affiliateCommonPageWeb.logout();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Co-brand")
    @Vertical(name = "Affiliate")
    @Test
    public void testOnboardNonApiCobrandFromNewPartner() {
        Map<String, String> param = getParam("testOnboardNonApiCobrandFromNewPartner");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateNewPartnerFormPage<?> affiliateNewPartnerFormPage = new AffiliateNewPartnerFormPage<>(session);
        AffiliateCoBrandDashboardPage<?> affiliateCoBrandDashboardPage = new AffiliateCoBrandDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        step("The user clicks on the Add New button.");
        affiliatePartnerListPage.clickAddNewPartnerButton();

        String country = param.get("country");
        String settlementType = param.get("settlementType");
        String currency = param.get("currency");
        String product = param.get("product");
        String tiketEntity = param.get("tiketEntity");

        step("The user onboards Non API Cobrand from new partner");
        String name = affiliateNewPartnerFormPage.createAndAddNewPartner(country, tiketEntity, product, settlementType, currency);

        step("The user verify that the CoBrand is onboarded successfully.");
        Assertion.assertThat("Successfully redirected to Cobrand Page", affiliateCoBrandDashboardPage.isCoBrandDashboardPage(), is(true));
        affiliateCoBrandDashboardPage.searchByPartnerName(name);
        affiliateCoBrandDashboardPage.getSearchedPartnerId();
        boolean isCobrandOnboarded = affiliateCoBrandDashboardPage.checkFilterResult(name,"", "", "");
        Assertion.assertThat("Verify that the CoBrand is onboarded successfully", isCobrandOnboarded, is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Co-Brand")
    @Vertical(name = "Affiliate")
    @Test
    public void testCreateCobrandPartner() throws InterruptedException {
        Map<String, String> param = getParam("testCreateCobrandPartner");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateCoBrandDashboardPage<?> affiliateCoBrandDashboardPage = new AffiliateCoBrandDashboardPage<>(session);
        AffiliateCoBrandDashboardPage<?> affiliateCoBrandFormPage = new AffiliateCoBrandDashboardPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateNewPartnerFormPage<?> affiliateNewPartnerFormPage = new AffiliateNewPartnerFormPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String utmSource = param.get("utmSource");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        step("The user clicks on the Add New button.");
        affiliatePartnerListPage.clickAddNewPartnerButton();

        String country = param.get("country");
        String settlementType = param.get("settlementType");
        String currency = param.get("currency");
        String product = param.get("product");
        String tiketEntity = param.get("tiketEntity");

        step("The user onboards Non API Cobrand from new partner");
        String name = affiliateNewPartnerFormPage.createAndAddNewPartner(country, tiketEntity, product, settlementType, currency);

        step("The user verify that the CoBrand is onboarded successfully.");
        Assertion.assertThat("Successfully redirected to Cobrand Page", affiliateCoBrandDashboardPage.isCoBrandDashboardPage(), is(true));

        step("The user search for the newly created partner and goes to the Co-Brand tab");
        affiliateCoBrandDashboardPage.searchByPartnerName(name);
        String partnerId = affiliateCoBrandDashboardPage.getSearchedPartnerId();

        step("The user navigates to the Co-Brand tab of the newly created partner");
        affiliateCoBrandDashboardPage.goToAttributionConfigPage(utmSource);
        affiliateCoBrandDashboardPage.goToPartnerCobrand(partnerId);

        step("The user clicked on Add New button and choose partner to create the Co-Brand");
        affiliateCoBrandDashboardPage.clickAddNewButton();

        step("The user fill all data needed and submit it");
        Map<String, String> createdCoBrand = affiliateCoBrandFormPage.fillData(param);
        affiliateCoBrandFormPage.clickSubmitButton();

        step("The user check if the newly created co-brand is exist");
        boolean isResultExist =  affiliateCoBrandDashboardPage.isPartnerCobrandCreated(createdCoBrand.get("productOfferNameID"));

        step("Assertion");
        Assertion.assertThat("Search is showing in list", isResultExist, is(true));
    }

    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal DashBoard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testVerifySearchFlightBizlogByPartnerName() {
        Map<String, String> param = getParam("testVerifySearchFlightBizlogByPartnerName");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightBusinessLog<?> affiliateFlightBizLogByName = new AffiliateFlightBusinessLog<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);
        affiliateFlightBizLogByName.goToAffiliatePage();

        step("Insert the partner name on the search field");
        affiliateFlightBizLogByName.searchByPartnerName(partnerName);

        step("The user verifies the search result contains the Partner Name : " + partnerName);
        Assertion.assertThat("Searched Partner Name is displayed   ", affiliateFlightBizLogByName.getPartnerNameResult(partnerName) ,containsStringIgnoringCase(partnerName));
    }

    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal DashBoard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testVerifySearchFlightBizlogByPartnerID() {
        Map<String, String> param = getParam("testVerifySearchFlightBizlogByPartnerID");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightBusinessLog<?> affiliateFlightBizLogByID = new AffiliateFlightBusinessLog<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerID = param.get("partnerID");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);
        affiliateFlightBizLogByID.goToAffiliatePage();

        step("Insert the partner ID on the search field");
        affiliateFlightBizLogByID.searchByPartnerID(partnerID);

        step("The user verifies the search result contains the PartnerID : " + partnerID);
        Assertion.assertThat("Searched PartnerID is displayed   ", affiliateFlightBizLogByID.getPartnerIDResult(partnerID) ,containsStringIgnoringCase(partnerID));
    }

    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testVerifyNumOfPropertyCannotBeZero() {
        Map<String, String> param = getParam("testVerifyNumOfPropertyCannotBeZero");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelApiPageGenConfig<?> affiliateHotelApiPageGenConfig = new AffiliateHotelApiPageGenConfig<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Hotel Dashboard page.");
        affiliateHotelDashboardPage.clickHotel();

        step("The user navigate to the Gen Config page");
        affiliateHotelApiPageGenConfig.goToGenerateConfigHotel();

        step("The user input max property ID to zero");
        affiliateHotelApiPageGenConfig.setMaxPropertyToZero("0");
        affiliateHotelApiPageGenConfig.clickSaveButton();

        step("The user verifies that the Max Number of Property IDs field cannot be empty");
        Assertion.assertThat("user verifies the error message",affiliateHotelApiPageGenConfig.isMaxPropertyErrMsgDisplayed(), is(true));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testUpdateFlightRefundCharge() {
        Map<String, String> param = getParam("testUpdateFlightRefundCharge");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightDashboardPage<?> affiliateFlightDashboardPage = new AffiliateFlightDashboardPage<>(session);
        AffiliateFlightDashboardGeneralConfigPageWeb<?> affiliateFlightDashboardGeneralConfigPageWeb = AffiliateFlightDashboardGeneralConfigPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");
        String refundChargeAmount =  param.get("refundChargeAmount");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user goes to Access API Product > Flights");
        affiliateFlightDashboardPage.clickFlight();

        step("Click the Settings button");
        affiliateFlightDashboardPage.searchByPartnerId(partnerId);
        affiliateFlightDashboardPage.checkFlightSearch(partnerId);
        affiliateFlightDashboardPage.clickSettings();

        step("Select General Config dropdown option");
        affiliateFlightDashboardPage.clickGeneralConfig();
        String oldRefundChargeAmount = affiliateFlightDashboardGeneralConfigPageWeb.getRefundChargeValue();

        step("Insert the Refund Charge Amount");
        affiliateFlightDashboardGeneralConfigPageWeb.inputRefundCharge(refundChargeAmount);

        step("Click the Save button");
        affiliateFlightDashboardGeneralConfigPageWeb.clickSaveButton();

        step("Assertions");
        affiliateFlightDashboardPage.clickFlight();
        affiliateFlightDashboardPage.searchByPartnerId(partnerId);
        affiliateFlightDashboardPage.checkFlightSearch(partnerId);
        affiliateFlightDashboardPage.clickSettings();
        affiliateFlightDashboardPage.clickGeneralConfig();

        Assertion.assertThat("Refund Charge Amount same as input value",
                affiliateFlightDashboardGeneralConfigPageWeb.getRefundChargeValue(),
                is(refundChargeAmount)
        );

        step("Reset data state");
        affiliateFlightDashboardGeneralConfigPageWeb.inputRefundCharge(oldRefundChargeAmount);
        affiliateFlightDashboardGeneralConfigPageWeb.clickSaveButton();

        step("Logout");
        memberLoginPage.logoutAdmin();

    }

    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testVerifyDownloadFlightBizLog() {
        Map<String, String> param = getParam("testVerifyDownloadFlightBizLog");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightBusinessLog<?> affiliateDownloadFlightBizlog = new AffiliateFlightBusinessLog<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerID = param.get("partnerID");
        String expectedConfirmationText = param.get("expectedConfirmationText");
        String orderStatus = param.get("orderStatus");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);
        affiliateDownloadFlightBizlog.goToAffiliatePage();

        step("Insert the partner ID on the search field");
        affiliateDownloadFlightBizlog.searchByPartnerID(partnerID);

        step("User click the download button");
        affiliateDownloadFlightBizlog.clickDownloadButton();

        step("User proceed to select compile log form");
        affiliateDownloadFlightBizlog.selectLogType(orderStatus);

        step("User click the next button");
        affiliateDownloadFlightBizlog.clickNextButton();

        step("The user verifies that compile message is displayed");
        Assertion.assertThat("Verify success message is displayed", affiliateDownloadFlightBizlog.checkCompileConfirmationText(expectedConfirmationText), is(true));
    }

    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testVerifyCalendarPopUpOnFlightBizlog() {
        Map<String, String> param = getParam("testVerifyCalendarPopUpOnFlightBizlog");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightBusinessLog<?> affiliateDownloadFlightBizlog = new AffiliateFlightBusinessLog<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerID = param.get("partnerID");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);
        affiliateDownloadFlightBizlog.goToAffiliatePage();

        step("Insert the partner ID on the search field");
        affiliateDownloadFlightBizlog.searchByPartnerID(partnerID);

        step("User click the download button");
        affiliateDownloadFlightBizlog.clickDownloadButton();

        step("User click the Start Date button");
        affiliateDownloadFlightBizlog.clickStartDateButton();

        step("The user verifies that the calendar pop up is displayed");
        Assertion.assertThat("Calendar pop up is visible", affiliateDownloadFlightBizlog.isCalendarPopUpDisplayed(), is(true));
    }

    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal DashBoard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testVerifySuccessDownloadBizlog() {
        Map<String, String> param = getParam("testVerifySuccessDownloadBizlog");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightBusinessLog<?> affiliateDownloadFlightBizlog = new AffiliateFlightBusinessLog<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerID = param.get("partnerID");
        String orderStatus = param.get("orderStatus");
        String timeframe = param.get("timeframe");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);
        affiliateDownloadFlightBizlog.goToAffiliatePage();

        step("Insert the partner ID on the search field");
        affiliateDownloadFlightBizlog.searchByPartnerID(partnerID);

        step("User click the download button");
        affiliateDownloadFlightBizlog.clickDownloadButton();

        step("User proceed to select compile log form");
        affiliateDownloadFlightBizlog.selectLogType(orderStatus);
        affiliateDownloadFlightBizlog.selectTimeframe(timeframe);

        step("User click the next button");
        affiliateDownloadFlightBizlog.clickNextButton();

        step("User click the send button");
        affiliateDownloadFlightBizlog.clickSendButton();

        step("The user verifies that success message is displayed");
        String expectedSnackbarText = param.get("expectedSnackbarText");
        Assertion.assertThat("Verify success message is displayed", affiliateDownloadFlightBizlog.checkFlightBusinessLogSnackbar(expectedSnackbarText), is(true));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Internal DashBoard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testVerifyHotelAPIEditPenaltyInfo() {
        Map<String, String> param = getParam("testVerifyHotelAPIEditPenaltyInfo");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelDashboardPenaltyInfoWeb<?> affiliateHotelDashboardPenaltyInfoWeb = AffiliateHotelDashboardPenaltyInfoWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");
        String reason = param.get("reason");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Clicks on Hotel under API Products");
        affiliateHotelDashboardPage.clickHotel();

        step("Clicks any partner click on drop down");
        affiliateHotelDashboardPage.searchByPartnerId(partnerId);
        affiliateHotelDashboardPage.goToPenaltyInfo();

        step("Click Edit Icon");
        affiliateHotelDashboardPenaltyInfoWeb.clickEditButton();
        String oldReason = affiliateHotelDashboardPenaltyInfoWeb.getReasonValue();

        step("Now update any data and click on save button");
        affiliateHotelDashboardPenaltyInfoWeb.selectReason(reason);
        affiliateHotelDashboardPenaltyInfoWeb.clickSave();
        affiliateHotelDashboardPenaltyInfoWeb.clickSaveChangesConfirmationModal();

        step("Assertion");
        Assertion.assertThat("The new Reason is the same as input",
                affiliateHotelDashboardPenaltyInfoWeb.getReasonFromDetailPage(),
                is(reason)
        );

        step("Reset state");
        affiliateHotelDashboardPenaltyInfoWeb.clickEditButton();
        affiliateHotelDashboardPenaltyInfoWeb.selectReason(oldReason);
        affiliateHotelDashboardPenaltyInfoWeb.clickSave();
        affiliateHotelDashboardPenaltyInfoWeb.clickSaveChangesConfirmationModal();

        step("Log out");
        memberLoginPage.logoutAdmin();

    }
    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchByPartnerNameNonAPIPartner() {
        Map<String, String> param = getParam("testSearchByPartnerNameNonAPIPartner");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section.");
        affiliateNonAPIPage.nonAPISection();

        step("The user enter the partner name.");
        affiliateNonAPIPage.partnerList(partnerName);

        step("The user clicks on Search button.");
        affiliateNonAPIPage.clickSearchButton();

        step("The user verifies the search result contains the Partner name." );
        Assertion.assertThat("Searched partner name is displayed.", affiliateNonAPIPage.getFirstRowProductType(partnerName), containsStringIgnoringCase(partnerName));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchByPartnerIDNonAPIPartner() {
        Map<String, String> param = getParam("testSearchByPartnerIDNonAPIPartner");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section.");
        affiliateNonAPIPage.nonAPISection();

        step("The user enter the partner ID.");
        affiliateNonAPIPage.partnerList(partnerId);

        step("The user clicks on Search button.");
        affiliateNonAPIPage.clickSearchButton();

        step("The user verifies the search result contains the Partner ID." );
        Assertion.assertThat("Searched partner ID is displayed.", affiliateNonAPIPage.isFirstRowContainsProductTypeID(partnerId), is(true));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testNonApiPartnerListSelectAttributionWindow() {
        Map<String, String> param = getParam("testNonApiPartnerListSelectAttributionWindow");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section");
        affiliateNonAPIPage.nonAPISection();

        step("Click on Partner list");
        affiliateNonAPIPage.clickPartnerListMenu();

        step("The user enter the partner ID.");
        affiliateNonAPIPage.partnerList(partnerId);
        affiliateNonAPIPage.clickSearchButton();

        step("Select any partner and click on drop down 'More Action'");
        affiliateNonAPIPage.goToMoreActionMenu("Attribution");

        step("Assertion");
        Assertion.assertThat("Verify that Attribution Window should show 24 hrs,7 days and 30 days option",
                affiliateNonAPIPage.validateAttributionWindowOptions(),
                is(true)
        );

        step("Log out");
        memberLoginPage.logoutAdmin();
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testNonApiPartnerListAttributionAddUTM() {
        Map<String, String> param = getParam("testNonApiPartnerListAttributionAddUTM");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");
        String utmSourceValue = param.get("utmSourceValue");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section");
        affiliateNonAPIPage.nonAPISection();

        step("Click on Partner list");
        affiliateNonAPIPage.clickPartnerListMenu();

        step("The user enter the partner Name.");
        affiliateNonAPIPage.partnerList(partnerName);
        affiliateNonAPIPage.clickSearchButton();

        step("Select any partner and click on drop down 'More Action'");
        affiliateNonAPIPage.goToMoreActionMenu("Attribution");

        step("Click Add Button for UTM Source");
        affiliateNonAPIPage.clickAddUTMSourceButton();

        step("Fill the data any and click on Save redirection");
        affiliateNonAPIPage.insertValueToLastUTMSourceInputs(utmSourceValue);
        affiliateNonAPIPage.clickSaveRedirection();

        step("Open again Attribution window");
        affiliateNonAPIPage.goToMoreActionMenu("Attribution");

        step("Assertion");
        Assertion.assertThat("User should get the UTM source data same as it was filled",
                affiliateNonAPIPage.getLastUTMSourceValue(),
                is(utmSourceValue)
        );

        step("Reset State");
        affiliateNonAPIPage.deleteLastUTMSourceInputs();

        step("Log Out");
        memberLoginPage.logoutAdmin();
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testNonApiPartnerListAttributionPreload() {
        Map<String, String> param = getParam("testNonApiPartnerListAttributionPreload");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");
        String mediaSourceValue = param.get("mediaSourceValue");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section");
        affiliateNonAPIPage.nonAPISection();

        step("Click on Partner list");
        affiliateNonAPIPage.clickPartnerListMenu();

        step("The user enter the partner ID.");
        affiliateNonAPIPage.partnerList(partnerId);
        affiliateNonAPIPage.clickSearchButton();

        step("Select any partner and click on drop down 'More Action'");
        affiliateNonAPIPage.goToMoreActionMenu("Attribution");

        step("Now click on Pre load tab and fill any data in Media source");
        affiliateNonAPIPage.clickPreloadTab();
        affiliateNonAPIPage.clickPreloadTab();
        String oldValue = affiliateNonAPIPage.getMediaSourceValue();
        affiliateNonAPIPage.inputPreloadMediaSource(mediaSourceValue);

        step("Click save Pre-load Button");
        affiliateNonAPIPage.savePreload();

        step("Assertion");
        affiliateNonAPIPage.goToMoreActionMenu("Attribution");
        affiliateNonAPIPage.clickPreloadTab();
        affiliateNonAPIPage.clickPreloadTab();
        Assertion.assertThat("The previously filled data should show as it was filled",
                affiliateNonAPIPage.getMediaSourceValue(),
                is(mediaSourceValue));

        step("Reset State");
        affiliateNonAPIPage.inputPreloadMediaSource(oldValue);
        affiliateNonAPIPage.savePreload();

        step("Log out");
        memberLoginPage.logoutAdmin();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAddNewUrlRedirectionFromAddNewButton() {
        Map<String, String> param = getParam("testAddNewUrlRedirectionFromAddNewButton");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");
        String name = "auth_" + CoreFunctions.randomName();
        String URL = param.get("URL");
        String campaign = param.get("campaign");
        String description = param.get("description");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Click on Non API Section.");
        affiliateNonAPIPage.nonAPISection();

        step("The user clicks on  the partner list.");
        affiliateNonAPIPage.clickPartnerListMenu();

        step("User clicks on more Action.");
        affiliateNonAPIPage.clickMoreAction();

        step("User opens redirected Link");
        affiliateNonAPIPage.openRedirectedLink();

        step("User Click on add new Button");
        affiliateNonAPIPage.clickAddNewButton();

        step("User fill link details.");
        affiliateNonAPIPage.fillLinkDetails(name,URL,campaign, description);

        step("User clicks on create link");
        affiliateNonAPIPage.clickCreateLink();

        step("User verifies that link is created.");
        Assertion.assertThat("Is link created.", affiliateNonAPIPage.isLinkCreated(), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testOnboardCobrandFromNewPartner() throws InterruptedException {
        Map<String, String> param = getParam("testOnboardCobrandFromNewPartner");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateNewPartnerFormPage<?> affiliateNewPartnerFormPage = new AffiliateNewPartnerFormPage<>(session);
        AffiliateNonAPIPage<?> affiliateNonAPIPage = new AffiliateNonAPIPage<>(session);
        AffiliateCoBrandDashboardPage<?> affiliateCoBrandDashboardPage = new AffiliateCoBrandDashboardPage<>(session);
        AffiliateCoBrandDashboardPage<?> affiliateCoBrandFormPage = new AffiliateCoBrandDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String country = param.get("country");
        String tiketEntity = param.get("tiketEntity");
        String settlementType = param.get("settlementType");
        String currency = param.get("currency");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        step("The user clicks on the Add New button.");
        affiliatePartnerListPage.clickAddNewPartnerButton();

        step("The user creates new partner");
        String name = affiliateNewPartnerFormPage.createNewPartner(country, tiketEntity, settlementType, currency);

        step("The user verifies that the new partner is created.");
        affiliatePartnerListPage.searchPartnerByName(name);
        Assertion.assertThat("New Partner is created", affiliatePartnerListPage.isPartnerListItemPresent(name), is(true));

        step("Click on Non API Section.");
        affiliateNonAPIPage.nonAPISection();

        step("The user clicks on  the partner list.");
        affiliateNonAPIPage.clickPartnerListMenu();

        step("User Click on add new Button");
        affiliateNonAPIPage.clickAddNewButton();

        step("User made partner for NON_API");
        affiliateNonAPIPage.createNON_APIPartner(name);

        step("The user enter the partner name.");
        affiliateNonAPIPage.partnerList(name);

        step("The user clicks on Search button.");
        affiliateNonAPIPage.clickSearchButton();

        step("The user verifies the search result contains the Partner name." );
        Assertion.assertThat("Searched partner name is displayed.", affiliateNonAPIPage.getFirstRowProductType(name), containsStringIgnoringCase(name));

        step("User clicks on more Action.");
        affiliateNonAPIPage.clickMoreAction();

        step("User click on Cobrand campaign");
        affiliateNonAPIPage.clickCobrandCampaign();

        step("User Click on add new Button");
        affiliateNonAPIPage.clickAddNewButton();

        step("The user fill all data needed and submit it");
        Map<String, String> createdCoBrand = affiliateCoBrandFormPage.fillData(param);
        affiliateCoBrandFormPage.clickSubmitButton();

        step("The user check if the newly created co-brand is exist");
        boolean isResultExist =  affiliateCoBrandDashboardPage.isPartnerCobrandCreated(createdCoBrand.get("productOfferNameID"));

        step("Assertion");
        Assertion.assertThat("Search is showing in list", isResultExist, is(true));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void verifySearchByThePartnerId() {
        Map<String, String> param = getParam("verifySearchByThePartnerId");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateMiniAppsPageWeb<?> affiliateMiniAppsPageWeb = AffiliateMiniAppsPageWeb.getInstance(session);

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Navigate to Miniapps Page");
        affiliateMiniAppsPageWeb.goToPage();
        affiliateMiniAppsPageWeb.searchMiniApps(partnerId);

        step("Assertion");
        Assertion.assertThat("Verify that partnerId " + partnerId + " shown in the search result", affiliateMiniAppsPageWeb.checkPartnerIdExist(partnerId), equalTo(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testVerifyHotelExclusionSearchByHotelName() {
        Map<String, String> param = getParam("testVerifyHotelExclusionSearchByHotelName");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelBlacklistPage<?> affiliateHotelBlacklistPage = new AffiliateHotelBlacklistPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String filterValue = param.get("filterValue");
        String searchValue = param.get("searchValue");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Hotel Dashboard page.");
        affiliateHotelDashboardPage.clickHotel();

        step("The user navigate to Setting page of Active Hotel");
        affiliateHotelBlacklistPage.openActiveSetting();

        step("User open the blacklist option");
        affiliateHotelBlacklistPage.openBlacklistHotel();

        step("The user verifies that blacklist page opens");
        Assertion.assertThat("user verifies that blacklist page opens",affiliateHotelBlacklistPage.isBlacklistPageOpens(), is(true));

        step("Click on filter option");
        affiliateHotelBlacklistPage.clickFilterByDropdown();

        step("Select Hotel Name filter");
        affiliateHotelBlacklistPage.selectHotelNameFilter(filterValue);

        step("Enter search value as Hotel");
        affiliateHotelBlacklistPage.enterSearchValue(searchValue);

        step("Click on Search button");
        affiliateHotelBlacklistPage.clickSearchButton();

        step("Verify that the result contains the searched hotel name");
        Assertion.assertThat("Result contains the searched hotel name", affiliateHotelBlacklistPage.verifyFirstRowHotelName(searchValue), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchByOrderIdInNonApiOrderTable() {
        Map<String, String> param = getParam("testSearchByOrderIdInNonApiOrderTable");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateOrderInternalDashboard<?> affiliateOrderInternalDashboard = new AffiliateOrderInternalDashboard<>(session);
        AffiliateOrderTableInternalDashboard<?> affiliateOrderTableInternalDashboard = new AffiliateOrderTableInternalDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        memberLoginPage.goToPage("/admin/affiliate");

        step("Click on Order menu from sidebar:");
        affiliateOrderInternalDashboard.clickOrderMenu();

        step("Click on Non-API Products:");
        affiliateOrderInternalDashboard.clickNonApiProducts();

        step("User selects on Order ID.");
        affiliateOrderInternalDashboard.selectSearchBy("Order ID");

        step("Enter on Order id.");
        affiliateOrderTableInternalDashboard.enterOrderId(orderId);

        step("Click Search button");
        affiliateOrderTableInternalDashboard.clickOrderTableSearchButton();

        step("Verify that data is displayed based on Order Id");
        Assertion.assertThat("Verify that data is displayed based on Order ID ", affiliateOrderInternalDashboard.isFirstRowColumnContains(AffiliateOrderInternalDashboard.COL_ORDER_ID, orderId), is(true));

        step("Log out");
        memberLoginPage.logoutAdmin();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testSearchByPartnerIdInNonApiOrderTable() {
        Map<String, String> param = getParam("testSearchByPartnerIdInNonApiOrderTable");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateOrderInternalDashboard<?> affiliateOrderInternalDashboard = new AffiliateOrderInternalDashboard<>(session);
        AffiliateOrderTableInternalDashboard<?> affiliateOrderTableInternalDashboard = new AffiliateOrderTableInternalDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        memberLoginPage.goToPage("/admin/affiliate");

        step("Click on Order menu from sidebar:");
        affiliateOrderInternalDashboard.clickOrderMenu();

        step("Click on Non-API Products:");
        affiliateOrderInternalDashboard.clickNonApiProducts();

        step("User selects on Partner ID.");
        affiliateOrderInternalDashboard.selectSearchBy("Partner ID");

        step("Enter Partner ID and select from dropdown.");
        affiliateOrderInternalDashboard.enterPartnerIdAndSelect(partnerId);

        step("Click Search button");
        affiliateOrderTableInternalDashboard.clickOrderTableSearchButton();

        step("Verify that data is displayed based on Partner ID");
        Assertion.assertThat("Verify that data is displayed based on Partner ID", affiliateOrderInternalDashboard.isFirstRowColumnContains(AffiliateOrderInternalDashboard.COL_PARTNER_NAME, partnerId), is(true));

        step("Log out");
        memberLoginPage.logoutAdmin();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testFilterByDateInNonApiOrderTable() {
        Map<String, String> param = getParam("testFilterByDateInNonApiOrderTable");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateOrderInternalDashboard<?> affiliateOrderInternalDashboard = new AffiliateOrderInternalDashboard<>(session);
        AffiliateOrderTableInternalDashboard<?> affiliateOrderTableInternalDashboard = new AffiliateOrderTableInternalDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");
        int daysOffset = Integer.parseInt(param.get("daysOffset"));

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        memberLoginPage.goToPage("/admin/affiliate");

        step("Click on Order menu from sidebar:");
        affiliateOrderInternalDashboard.clickOrderMenu();

        step("Click on Non-API Products:");
        affiliateOrderInternalDashboard.clickNonApiProducts();

        step("User selects on Partner ID.");
        affiliateOrderInternalDashboard.selectSearchBy("Partner ID");

        step("Enter Partner ID and select from dropdown.");
        affiliateOrderInternalDashboard.enterPartnerIdAndSelect(partnerId);

        step("User selects the Start Date.");
        affiliateOrderInternalDashboard.setStartDate(daysOffset);

        step("Click Search button");
        affiliateOrderTableInternalDashboard.clickOrderTableSearchButton();

        step("Verify that order date is between start and end date");
        Assertion.assertThat("Verify that order date is between start date and end date", affiliateOrderInternalDashboard.isOrderDateOnOrAfterStartDate(), is(true));

        step("Log out");
        memberLoginPage.logoutAdmin();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testFilterByCampaignTypeInNonApiOrderTable() {
        Map<String, String> param = getParam("testFilterByCampaignTypeInNonApiOrderTable");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateOrderInternalDashboard<?> affiliateOrderInternalDashboard = new AffiliateOrderInternalDashboard<>(session);
        AffiliateOrderTableInternalDashboard<?> affiliateOrderTableInternalDashboard = new AffiliateOrderTableInternalDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");
        String campaignType = param.get("campaignType");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        memberLoginPage.goToPage("/admin/affiliate");

        step("Click on Order menu from sidebar:");
        affiliateOrderInternalDashboard.clickOrderMenu();

        step("Click on Non-API Products:");
        affiliateOrderInternalDashboard.clickNonApiProducts();

        step("User selects on Partner ID.");
        affiliateOrderInternalDashboard.selectSearchBy("Partner ID");

        step("Enter Partner ID and select from dropdown.");
        affiliateOrderInternalDashboard.enterPartnerIdAndSelect(partnerId);

        step("User selects Campaign Type.");
        affiliateOrderInternalDashboard.selectCampaignType(campaignType);

        step("Click Search button");
        affiliateOrderTableInternalDashboard.clickOrderTableSearchButton();

        step("Verify that data is displayed based on Campaign Type");
        Assertion.assertThat("Verify that campaign type matches", affiliateOrderInternalDashboard.isFirstRowColumnContains(AffiliateOrderInternalDashboard.COL_TYPE, campaignType), is(true));

        step("Log out");
        memberLoginPage.logoutAdmin();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testResetFilterInNonApiOrderTable() {
        Map<String, String> param = getParam("testResetFilterInNonApiOrderTable");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateOrderInternalDashboard<?> affiliateOrderInternalDashboard = new AffiliateOrderInternalDashboard<>(session);
        AffiliateOrderTableInternalDashboard<?> affiliateOrderTableInternalDashboard = new AffiliateOrderTableInternalDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");
        String campaignType = param.get("campaignType");
        int daysOffset = Integer.parseInt(param.get("daysOffset"));

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        memberLoginPage.goToPage("/admin/affiliate");

        step("Click on Order menu from sidebar:");
        affiliateOrderInternalDashboard.clickOrderMenu();

        step("Click on Non-API Products:");
        affiliateOrderInternalDashboard.clickNonApiProducts();

        step("User selects on Partner ID.");
        affiliateOrderInternalDashboard.selectSearchBy("Partner ID");

        step("Enter Partner ID and select from dropdown.");
        affiliateOrderInternalDashboard.enterPartnerIdAndSelect(partnerId);

        step("User selects Campaign Type. ");
        affiliateOrderInternalDashboard.selectCampaignType(campaignType);

        step("User selects the Start Date.");
        affiliateOrderInternalDashboard.setStartDate(daysOffset);

        step("Click Search button");
        affiliateOrderTableInternalDashboard.clickOrderTableSearchButton();

        step("Verify that data is displayed after searching.");
        Assertion.assertThat("Verify that data is displayed after searching.", affiliateOrderInternalDashboard.isFirstRowColumnContains(AffiliateOrderInternalDashboard.COL_TYPE, campaignType), is(true));

        step("Click Reset Filter button");
        affiliateOrderInternalDashboard.clickResetFilter();

        step("Verify that Start Date is disabled after reset");
        Assertion.assertThat("Verify Start Date is disabled", affiliateOrderInternalDashboard.isStartDateDisabled(), is(true));

        step("Verify that Campaign Type is disabled after reset");
        Assertion.assertThat("Verify Campaign Type is disabled", affiliateOrderInternalDashboard.isFilterDisabled("Campaign Type"), is(true));

        step("Verify that Status is disabled after reset");
        Assertion.assertThat("Verify Status is disabled", affiliateOrderInternalDashboard.isFilterDisabled("Status"), is(true));

        step("Verify that Vertical is disabled after reset");
        Assertion.assertThat("Verify Vertical is disabled", affiliateOrderInternalDashboard.isFilterDisabled("Vertical"), is(true));

        step("Log out");
        memberLoginPage.logoutAdmin();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testHotelEditContentDetail() {
        Map<String, String> param = getParam("testHotelEditContentDetail");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicked on search");
        affiliatePartnerListPage.searchPartnerByName(partnerName);

        step("The user clicks edit content details");
        affiliateHotelPartnerEdit.clickSettings();
        affiliateHotelPartnerEdit.clickEditContentDetails();

        step("The user edits the content details - ");
        affiliateHotelPartnerEdit.editPartnerEntity();
        affiliateHotelPartnerEdit.editInventory();
        affiliateHotelPartnerEdit.saveUpdateHotelDetails();

        step("The user verify the details are updated");
        Assertion.assertThat("User updated the hotel details", affiliateHotelDashboardPage.isHotelDetailsUpdated(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testHotelCommissionSetup() {
        Map<String, String> param = getParam("testHotelCommissionSetup");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelDashboardPage<?> affiliateHotelDashboardPage = new AffiliateHotelDashboardPage<>(session);
        AffiliateHotelSearch<?> affiliateHotelSearch = new AffiliateHotelSearch<>(session);
        AffiliateHotelPartnerEdit<?> affiliateHotelPartnerEdit = new AffiliateHotelPartnerEdit<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String commissionValue = CoreFunctions.generateRandomInt(1, 99) + "." + CoreFunctions.generateRandomInt(10, 99);
        String commissionDescription = param.get("commissionDescription");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        String partnerId = param.get("partnerId");

        step("The user navigated to Hotel Page");
        affiliateHotelDashboardPage.clickHotel();

        step("The user clicked on search");
        affiliateHotelSearch.searchByPartnerId(partnerId);

        step("The user clicks on content sync");
        affiliateHotelPartnerEdit.clickSettings();

        step("The user add new commission");
        affiliateHotelPartnerEdit.addNewCommission(commissionValue, commissionDescription);

        step("The user verify that new commission is added.");
        Assertion.assertThat("New Commission is added", affiliateHotelDashboardPage.isCommissionAdded(), is(true));
        Assertion.assertThat("Commission value is same as input", affiliateHotelDashboardPage.getCommissionValue(), is(commissionValue));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAddFlightCommission() {
        Map<String, String> param = getParam("testAddFlightCommission");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateFlightDashboardPage<?> affiliateFlightDashboardPage = new AffiliateFlightDashboardPage<>(session);
        AffiliateFlightCommissionPage<?> affiliateFlightCommissionPage = new AffiliateFlightCommissionPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate dashboard.");
        affiliateFlightDashboardPage.clickFlight();

        step("The user clicks on set up commission button.");
        affiliateFlightDashboardPage.clickSettings();
        affiliateFlightDashboardPage.clickSetupCommission();

        step("The user clicks on add new commission button");
        affiliateFlightCommissionPage.clickAddNewCommissionButton();

        step("The user passes and saves the details");
        String commissionValue = CoreFunctions.generateRandomInt(1, 99) + "." + CoreFunctions.generateRandomInt(10, 99);
        String descriptionInput = param.get("descriptionInput");
        String expectedSnackbarText = param.get("expectedSnackbarText");
        affiliateFlightCommissionPage.fillCommissionConfig(commissionValue, descriptionInput);

        step("To verify Flight commission successfully created!");
        Assertion.assertThat("Verify Flight commission successfully created!", affiliateFlightCommissionPage.isCommissionAdded(expectedSnackbarText), is(true));
        Assertion.assertThat("Commission value is same as input", affiliateFlightCommissionPage.getCommissionValue(), is(commissionValue));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void verifyUserEditConfigContent() {
        Map<String, String> param = getParam("verifyUserEditConfigContent");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String partnerId = param.get("partnerId");
        String newChannelId = param.get("newChannelId");
        String option = param.get("option");

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateMiniAppsPageWeb<?> affiliateMiniAppsPageWeb = new AffiliateMiniAppsPageWeb<>(session);

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Navigate to Miniapps Page");
        affiliateMiniAppsPageWeb.goToPage();
        affiliateMiniAppsPageWeb.searchMiniApps(partnerId);
        Assertion.assertThat("Verify that partnerId " + partnerId + " shown in the search result", affiliateMiniAppsPageWeb.checkPartnerIdExist(partnerId), equalTo(true));

        step("Open Miniapps Content");
        affiliateMiniAppsPageWeb.selectSettings(partnerId, option);

        step("Edit Miniapps");
        String previousChannelId = affiliateMiniAppsPageWeb.getValueFromChannelIdInput();
        affiliateMiniAppsPageWeb.toggleContentStatus();
        affiliateMiniAppsPageWeb.insertChannelId(newChannelId);
        affiliateMiniAppsPageWeb.clickSave();
        Assertion.assertThat("Success snackbar appear when clicking the Save button", affiliateMiniAppsPageWeb.isSuccessSnackbarAppear(), equalTo(true));

        step("Verify updated content");
        affiliateMiniAppsPageWeb.searchMiniApps(partnerId);
        affiliateMiniAppsPageWeb.selectSettings(partnerId, option);
        Assertion.assertThat("Verify that Channel Id is the new value", affiliateMiniAppsPageWeb.getValueFromChannelIdInput(), equalTo(newChannelId));

        step("Reset State");
        affiliateMiniAppsPageWeb.toggleContentStatus();
        affiliateMiniAppsPageWeb.insertChannelId(previousChannelId);
        affiliateMiniAppsPageWeb.clickSave();
        Assertion.assertThat("Success snackbar appear when clicking the Save button", affiliateMiniAppsPageWeb.isSuccessSnackbarAppear(), equalTo(true));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void verifyUserCreateNewMiniappsConfig() {
        Map<String, String> param = getParam("verifyUserCreateNewMiniappsConfig");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String channelId = param.get("channelId");

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateMiniAppsPageWeb<?> affiliateMiniAppsPageWeb = new AffiliateMiniAppsPageWeb<>(session);

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Navigate to Miniapps Page");
        affiliateMiniAppsPageWeb.goToPage();

        step("Navigate to Create new Miniapps");
        affiliateMiniAppsPageWeb.clickAddNewButton();

        step("Fill necessary inputs");
        String selectedPartner = affiliateMiniAppsPageWeb.selectPartner();
        affiliateMiniAppsPageWeb.insertChannelId(channelId);
        affiliateMiniAppsPageWeb.togglePromoBannerStatus();
        affiliateMiniAppsPageWeb.clickSave();
        affiliateMiniAppsPageWeb.waitAfterSave();
        affiliateMiniAppsPageWeb.clickSaveCommission();

        step("Verify " + selectedPartner + " successfully created");
        affiliateMiniAppsPageWeb.searchMiniApps(selectedPartner);
        Assertion.assertThat("Verify that partnerId " + selectedPartner + " shown in the search result", affiliateMiniAppsPageWeb.checkPartnerIdExist(selectedPartner), equalTo(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testAddHotelToWhitelistSingle() {
        Map<String, String> param = getParam("testAddHotelToWhitelistSingle");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelWhitelist<?> AffiliateHotelWhitelist = new AffiliateHotelWhitelist<>(session);
        AffiliatePartnerListPage<?> affiliatePartnerListPage = new AffiliatePartnerListPage<>(session);
        AffiliateHotelWhitelist<?> affiliateHotelWhitelist = new AffiliateHotelWhitelist<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateCommonPageWeb<?> affiliateCommonPageWeb = AffiliateCommonPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String hotelId = param.get("hotelId");
        String expectedSnackbarText = param.get("expectedSnackbarText");


        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("The user navigates to the Affiliate dashboard.");
        affiliatePartnerListPage.goToPartnerListPage();

        step("The user navigate to hotel API menu");
        AffiliateHotelWhitelist.clickHotelAPIManualWhitelist();

        step("Click the add single button");
        AffiliateHotelWhitelist.clickAddSingleButton();

        step("Insert hotelId");
        AffiliateHotelWhitelist.searchByHotelIdWhitelist(hotelId);

        step("Click search button");
        AffiliateHotelWhitelist.clickSearchHotelButton();

        step("Click add whitelist button");
        AffiliateHotelWhitelist.clickAddWhitelistButton();

        step("Verify the add hotel Id is successfully added");
        Assertion.assertThat("Verify the add hotel Id is successfully added", affiliateHotelWhitelist.checkHotelWhitelistedSnackbar(expectedSnackbarText), is(true));

        step("Log out");
        affiliateCommonPageWeb.logout();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testHotelBizlogSuccessMessageOnSendClick() {
        Map<String, String> param = getParam("testHotelBizlogSuccessMessageOnSendClick");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);
        AffiliateCommonPageWeb<?> affiliateCommonPageWeb = AffiliateCommonPageWeb.getInstance(session);


        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User navigates to the Hotel Log page.");
        affiliateHotelLogPage.goToAffiliateHotelLogPage();

        step("User clicks on the Download Logs button.");
        affiliateHotelLogPage.clickDownloadLogsButton();

        step("User selects the Event.");
        affiliateHotelLogPage.selectCancellationChip();

        step("User selects the Time Frame.");
        affiliateHotelLogPage.selectCustomTimeFrame();

        step("User selects the Start Date.");
        affiliateHotelLogPage.setStartDate();

        step("User selects the End Date.");
        affiliateHotelLogPage.setEndDate();

        step("Verify that Start Date is strictly before End Date.");
        Assertion.assertThat("Start date is strictly before end date", affiliateHotelLogPage.isStartStrictlyBeforeEnd(), is(true));

        step("User clicks the Next button.");
        affiliateHotelLogPage.clickNextButton();

        step("User clicks the Send button.");
        affiliateHotelLogPage.clickSendButton();

        step("Verify that the Hotel Log Downloaded Successfully.");
        Assertion.assertThat("Compile snackbar is visible", affiliateHotelLogPage.isCompileSnackbarVisible(), is(true));

        step("Log out");
        affiliateCommonPageWeb.logout();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testTTDBizlogSuccessMessageOnSendClick() {
        Map<String, String> param = getParam("testTTDBizlogSuccessMessageOnSendClick");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateRateKeyPage<?> affiliateRateKeyPage = new AffiliateRateKeyPage<>(session);
        AffiliateTtdApiPage<?> affiliateTtdApiPage = new AffiliateTtdApiPage<>(session);
        AffiliateHotelLogPage<?> affiliateHotelLogPage = new AffiliateHotelLogPage<>(session);
        AffiliateCommonPageWeb<?> affiliateCommonPageWeb = AffiliateCommonPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigate to the Affiliate Landing Page.");
        affiliateRateKeyPage.goToAffiliateLandingPage();

        step("Click on TTD API");
        affiliateTtdApiPage.clickTtdApiSection();

        step("The user navigated to ttd api business log page");
        affiliateTtdApiPage.clickTtdApiBusinessLog();

        step("User clicks on the Download Logs button.");
        affiliateHotelLogPage.clickDownloadLogsButton();

        step("User clicks the Next button.");
        affiliateHotelLogPage.clickNextButton();

        step("User clicks the Send button.");
        affiliateHotelLogPage.clickSendButton();

        step("Verify that the API Log Downloaded Successfully.");
        Assertion.assertThat("Compile snackbar is visible", affiliateHotelLogPage.isCompileSnackbarVisible(), is(true));

        step("Log out");
        affiliateCommonPageWeb.logout();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "Affiliate")
    @Test
    public void testVerifyBalanceIsVisibleOnceClicked() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBalanceIsVisibleOnceClicked");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateBalancePage<?> affiliateBalancePage = new AffiliateBalancePage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");

        step("Navigate to Affiliate External Dashboard login page.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);
        loginAffiliate.isLoggedIn();

        step("Select the partner from the partner selection screen.");
        affiliateBalancePage.selectPartner(partnerName);

        step("Click on the Finance section to expand the dropdown.");
        affiliateBalancePage.clickFinanceDropdown();

        step("Click on Balance option from the Finance dropdown.");
        affiliateBalancePage.clickBalance();

        step("Verify Advance Deposit Balance value is displayed");
        Assertion.assertThat("Is Advance Deposit Balance value is displayed ?", affiliateBalancePage.isAdvanceDepositBalanceDisplayed(), is(true));

        step("Verify Credit Line Balance value is displayed");
        Assertion.assertThat("Is Credit Line Balance value is displayed", affiliateBalancePage.isCreditLineBalanceDisplayed(), is(true));
    }

    @Author(names = {"Sanah Putri"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testVerifyHotelRefundInfo() {
        Map<String, String> param = getParam("testVerifyHotelRefundInfo");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AffiliateOrderTableInternalDashboard<?> affiliateOrderTableInternalDashboard = new AffiliateOrderTableInternalDashboard<>(session);
        AffiliateRefundInfo<?> affiliateRefundInfo = new AffiliateRefundInfo<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");

        step("The user logs in to the dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("The user navigates to the Affiliate Landing Page");
        memberLoginPage.goToPage("/admin/affiliate");

        step("Click on Order Tabel:");
        affiliateOrderTableInternalDashboard.clickOrderTable();

        step("Enter on Order id: " +orderId);
        affiliateOrderTableInternalDashboard.enterOrderId(orderId);

        step("Click Seacrh button");
        affiliateOrderTableInternalDashboard.clickOrderTableSearchButton();

        step("The user searches Order ID and opens Refund Info Details");
        affiliateOrderTableInternalDashboard.goToRefundInfo();

        step("Verify that Hotel Refund Details page is opened in a new tab");
        Assertion.assertThat("Hotel refund details URL is opened", affiliateRefundInfo.getRefundDetailPageUrl(),
                containsString("/admin/affiliate/partner/order-table/refund/hotel/detail/"));
        Assertion.assertThat("Hotel Refund Details heading is displayed", affiliateRefundInfo.getPageHeading(),
                is("Hotel Refund Details"));
    }

    @Author(names = {"Sanah Putri"})
    @Module(name = "External Dashboard")
    @Tribe(name = "Affiliate")
    @Vertical(name = "B2B")
    @Test
    public void testVerifyHotelRefundInfoExt() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyHotelRefundInfoExt");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AffiliateBalancePage<?> affiliateBalancePage = new AffiliateBalancePage<>(session);
        LoginAffiliate<?> loginAffiliate = new LoginAffiliate<>(session);
        AffiliateOrderTable<?> affiliateOrderTable = new AffiliateOrderTable<>(session);
        AffiliateRefundInfo<?> affiliateRefundInfo = new AffiliateRefundInfo<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");
        String orderId = param.get("orderId");

        step("Navigate to Affiliate External Dashboard login page.");
        loginAffiliate.goToAffiliateDashboardLogin();
        loginAffiliate.inputLogin(username, password);
        loginAffiliate.isLoggedIn();

        step("Select the partner from the partner selection screen.");
        affiliateBalancePage.selectPartner(partnerName);

        step("Insert the order ID on the Order Table page: " + orderId);
        affiliateOrderTable.inputOrderId(orderId);

        step("The user searches Order ID and opens Refund Info Details");
        affiliateOrderTable.goToRefundInfo();

        step("Verify that Hotel Refund Details page is opened in a new tab");
        Assertion.assertThat("Hotel refund details URL is opened", affiliateRefundInfo.getRefundDetailPageUrl(),
                containsString("/business-partner/dashboard/order-table/refund/hotel/detail/"));
        Assertion.assertThat("Hotel Refund Details heading is displayed", affiliateRefundInfo.getPageHeading(),
                is("Hotel Refund Details"));
    }
}