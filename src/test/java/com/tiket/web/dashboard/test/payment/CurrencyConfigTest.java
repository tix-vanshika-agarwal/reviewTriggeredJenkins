package com.tiket.web.dashboard.test.payment;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.tiket.web.dashboard.page.payment.ForeignExchangeRatePage;
import org.testng.annotations.Test;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.PaymentApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.dashboard.page.payment.CurrencyManagement;
import com.tiket.web.dashboard.page.payment.PaymentDashboardPage;
import com.tiket.web.page.common.home.HomePageWeb;

public class CurrencyConfigTest extends WebTest {
    @Author(names = {"NaufalF"})
    @Module(name = "CurrencyConfig")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform - Sphinx")
    @Test
    public void filterUserCurrency() {
        Map<String, String> param = getParam("filterUserCurrency");
        BaseTest.paramsThreadLocal.set(param);
        
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        CurrencyManagement<?> currencyManagement = new CurrencyManagement<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String filterName = param.get("filterName");
        String dropdownName = param.get("dropdownName");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String query = "{userIsActive:true}";

        step("Login to Admin Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);
        
        step("Open Currency Management Dashboard");
        currencyManagement.openCurrencyConfigPage();

        step("Filter by "+filterName+" with value "+dropdownName);
        currencyManagement.selectFilterinCurrencyList(filterName, dropdownName);
        currencyManagement.clickButton("Search");
        int totalCurrency = currencyManagement.getTotalDataList();

        step("verify total data same as DB");
        int countFromDB = currencyManagement.getNumberOfCurrencyMongoDB(config,database,collectionName,query);
        Assertion.assertThat("Verify total currency data in panel and DB same", totalCurrency, is(countFromDB));

        step("Go to tiket homepage");
        homePage.goToHomePage();
        
        step("Verify number of currency in homepage");
        homePage.clickLanguageSelector();
        int countFromHome = homePage.numberOfAvailableCurrency();
        Assertion.assertThat("Verify total currency data in panel and homepage same", totalCurrency, is(countFromHome));

    }

    @Author(names = {"NaufalF"})
    @Module(name = "CurrencyConfig")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform - Sphinx")
    @Test
    public void filterVerticalCurrency() {
        Map<String, String> param = getParam("filterVerticalCurrency");
        BaseTest.paramsThreadLocal.set(param);
        
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        CurrencyManagement<?> currencyManagement = new CurrencyManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String filterName = param.get("filterName");
        String dropdownName = param.get("dropdownName");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String query = "{verticalIsActive:1}";

        step("Login to Admin Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);
        
        step("Open Currency Management Dashboard");
        currencyManagement.openCurrencyConfigPage();

        step("Filter by "+filterName+" with value "+dropdownName);
        currencyManagement.selectFilterinCurrencyList(filterName, dropdownName);
        currencyManagement.clickButton("Search");
        int totalCurrency = currencyManagement.getTotalDataList();

        step("verify total data same as DB");
        int countFromDB = currencyManagement.getNumberOfCurrencyMongoDB(config,database,collectionName,query);
        Assertion.assertThat("Verify total currency data in panel and DB same", totalCurrency, is(countFromDB));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "CurrencyConfig")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform - Sphinx")
    @Test
    public void filterActiveStatus() {
        Map<String, String> param = getParam("filterActiveStatus");
        BaseTest.paramsThreadLocal.set(param);
        
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        CurrencyManagement<?> currencyManagement = new CurrencyManagement<>(session);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String filter = param.get("filter");
        String status = param.get("status");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String query = "{userIsActive:true}";

        step("Login into Admin dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open Currency Management Dashboard");
        currencyManagement.openCurrencyConfigPage();

        step("Filter by Currency Filter with value USER CURRENCY");
        currencyManagement.selectFilterinCurrencyList("Currency Filter", "USER CURRENCY");

        step("Filter by "+filter+" with status "+status);
        currencyManagement.selectFilterinCurrencyList(filter, status);
        currencyManagement.clickButton("Search");
        int totalCurrency = currencyManagement.getTotalDataList();

        step("verify total data same as DB");
        int countFromDB = currencyManagement.getNumberOfCurrencyMongoDB(config,database,collectionName,query);
        Assertion.assertThat("Verify total currency data in panel and DB same", totalCurrency, is(countFromDB));

        step("Go to tiket homepage");
        homePageWeb.goToHomePage();
        
        step("Verify number of currency in homepage");
        homePageWeb.clickLanguageSelector();
        int countFromHome = homePageWeb.numberOfAvailableCurrency();
        Assertion.assertThat("Verify total currency data in panel and homepage same", totalCurrency, is(countFromHome));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "CurrencyConfig")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform - Sphinx")
    @Test
    public void filterInactiveStatus() {
        Map<String, String> param = getParam("filterInactiveStatus");
        BaseTest.paramsThreadLocal.set(param);
        
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        CurrencyManagement<?> currencyManagement = new CurrencyManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String filter = param.get("filter");
        String status = param.get("status");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String query = "{userIsActive:false,isDeleted:0,code:{$in:[\"EUR\", \"GBP\", \"AUD\", \"NZD\", \"USD\", \"CAD\", \"CHF\", \"JPY\", \"CNY\", \"HKD\", \"SGD\", \"MYR\", \"THB\", \"PHP\", \"VND\", \"IDR\", \"BRL\"]}}";

        step("Login into Admin dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open Currency Management Dashboard");
        currencyManagement.openCurrencyConfigPage();

        step("Filter by Currency Filter with value USER CURRENCY");
        currencyManagement.selectFilterinCurrencyList("Currency Filter", "USER CURRENCY");

        step("Filter by "+filter+" with status "+status);
        currencyManagement.selectFilterinCurrencyList(filter, status);
        currencyManagement.clickButton("Search");
        int totalCurrency = currencyManagement.getTotalDataList();

        step("verify total data same as DB");
        int countFromDB = currencyManagement.getNumberOfCurrencyMongoDB(config,database,collectionName,query);
        Assertion.assertThat("Verify total currency data in panel and DB same", totalCurrency, is(countFromDB));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "CurrencyConfig")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform - Sphinx")
    @Test
    public void filterAllStatus() {
        Map<String, String> param = getParam("filterAllStatus");
        BaseTest.paramsThreadLocal.set(param);
        
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        CurrencyManagement<?> currencyManagement = new CurrencyManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String filter = param.get("filter");
        String status = param.get("status");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String query = "{isDeleted:0,code:{$in:[\"EUR\", \"GBP\", \"AUD\", \"NZD\", \"USD\", \"CAD\", \"CHF\", \"JPY\", \"CNY\", \"HKD\", \"SGD\", \"MYR\", \"THB\", \"PHP\", \"VND\", \"IDR\", \"BRL\"]}}";

        step("Login into Admin dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open Currency Management Dashboard");
        currencyManagement.openCurrencyConfigPage();

        step("Filter by Currency Filter with value USER CURRENCY");
        currencyManagement.selectFilterinCurrencyList("Currency Filter", "USER CURRENCY");

        step("Filter by "+filter+" with status "+status);
        currencyManagement.selectFilterinCurrencyList(filter, status);
        currencyManagement.clickButton("Search");
        int totalCurrency = currencyManagement.getTotalDataList();

        step("verify total data same as DB");
        int countFromDB = currencyManagement.getNumberOfCurrencyMongoDB(config,database,collectionName,query);
        Assertion.assertThat("Verify total currency data in panel and DB same", totalCurrency, is(countFromDB));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "CurrencyConfig")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform - Sphinx")
    @Test
    public void filterGbvALL() {
        Map<String, String> param = getParam("filterGbvALL");
        BaseTest.paramsThreadLocal.set(param);
        
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        CurrencyManagement<?> currencyManagement = new CurrencyManagement<>(session);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String filter = param.get("filter");
        String item = param.get("item");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String query = "{userIsActive:true}";

        step("Login into Admin dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open Currency Management Dashboard");
        currencyManagement.openCurrencyConfigPage();

        step("Filter by Currency Filter with value USER CURRENCY");
        currencyManagement.selectFilterinCurrencyList("Currency Filter", "USER CURRENCY");

        step("Filter by "+filter+" with value "+item);
        currencyManagement.selectFilterinCurrencyList(filter, item);
        currencyManagement.clickButton("Search");
        int totalCurrency = currencyManagement.getTotalDataList();

        step("verify total data same as DB");
        int countFromDB = currencyManagement.getNumberOfCurrencyMongoDB(config,database,collectionName,query);
        Assertion.assertThat("Verify total currency data in panel and DB same", totalCurrency, is(countFromDB));

        step("Go to tiket homepage");
        homePageWeb.goToHomePage();
        
        step("Verify number of currency in homepage");
        homePageWeb.clickLanguageSelector();
        int countFromHome = homePageWeb.numberOfAvailableCurrency();
        Assertion.assertThat("Verify total currency data in panel and homepage same", totalCurrency, is(countFromHome));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "CurrencyConfig")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform - Sphinx")
    @Test
    public void filterGbvID() {
        Map<String, String> param = getParam("filterGbvID");
        BaseTest.paramsThreadLocal.set(param);
        
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        CurrencyManagement<?> currencyManagement = new CurrencyManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String filter = param.get("filter");
        String item = param.get("item");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String query = "{userIsActive:true,gbvCode:'ID'}";

        step("Login into Admin dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open Currency Management Dashboard");
        currencyManagement.openCurrencyConfigPage();

        step("Filter by Currency Filter with value USER CURRENCY");
        currencyManagement.selectFilterinCurrencyList("Currency Filter", "USER CURRENCY");

        step("Filter by "+filter+" with value "+item);
        currencyManagement.selectFilterinCurrencyList(filter, item);
        currencyManagement.clickButton("Search");
        int totalCurrency = currencyManagement.getTotalDataList();

        step("verify total data same as DB");
        int countFromDB = currencyManagement.getNumberOfCurrencyMongoDB(config,database,collectionName,query);
        Assertion.assertThat("Verify total currency data in panel and DB same", totalCurrency, is(countFromDB));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "CurrencyConfig")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform - Sphinx")
    @Test
    public void filterGbvSG() {
        Map<String, String> param = getParam("filterGbvSG");
        BaseTest.paramsThreadLocal.set(param);
        
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        CurrencyManagement<?> currencyManagement = new CurrencyManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String filter = param.get("filter");
        String item = param.get("item");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String query = "{userIsActive:true,gbvCode:'SG'}";

        step("Login into Admin dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open Currency Management Dashboard");
        currencyManagement.openCurrencyConfigPage();

        step("Filter by Currency Filter with value USER CURRENCY");
        currencyManagement.selectFilterinCurrencyList("Currency Filter", "USER CURRENCY");

        step("Filter by "+filter+" with value "+item);
        currencyManagement.selectFilterinCurrencyList(filter, item);
        currencyManagement.clickButton("Search");
        int totalCurrency = currencyManagement.getTotalDataList();

        step("verify total data same as DB");
        int countFromDB = currencyManagement.getNumberOfCurrencyMongoDB(config,database,collectionName,query);
        Assertion.assertThat("Verify total currency data in panel and DB same", totalCurrency, is(countFromDB));
    }

    @Author(names = {"NaufalF", "Hafizh"})
    @Module(name = "CurrencyConfig")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform - Sphinx")
    @Test
    public void messageEdit() {
        Map<String, String> param = getParam("messageEdit");
        BaseTest.paramsThreadLocal.set(param);
        
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        CurrencyManagement<?> currencyManagement = new CurrencyManagement<>(session);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        PaymentApi<?> paymentApi = new PaymentApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        int randomLength = CoreFunctions.generateRandomInt(20, 30);
        String messageID = "Auto ID "+CoreFunctions.generateRandomString(randomLength);
        String messageEN = "Auto EN "+CoreFunctions.generateRandomString(randomLength);
        List<String> defaultEn = Arrays.asList(param.get("defaultEn").split("#"));
        List<String> defaultId = Arrays.asList(param.get("defaultId").split("#"));

        step("Login into Admin dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open Currency Management Dashboard");
        currencyManagement.openCurrencyConfigPage();

        step("Open Message on dashboard");
        currencyManagement.clickButton("Message");

        step("Input the message");
        currencyManagement.inputMessage(messageID, messageEN);

        step("Verify the ID message on panel");
        Assertion.assertThat("Verify currency message ID on panel", currencyManagement.getSubmittedMessageID(), is(messageID));

        step("Saving the new message on panel");
        currencyManagement.clickButton("Save");
        currencyManagement.clickButton("Yes, save");

        step("Go to tiket homepage");
        homePageWeb.goToHomePage();

        step("Verify the currency message in EN language");
        String infoEN = homePageWeb.getImportantInfoMessage();
        Assertion.assertThat("Verify currency message in EN language", infoEN, is(messageEN));

        step("Cleanup the currency message to default via API");
        paymentApi.updateCurrencyMessage(defaultEn, defaultId);
    }

    @Author(names = {"NaufalF", "Hafizh"})
    @Module(name = "CurrencyConfig")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform - Sphinx")
    @Test(dependsOnMethods = "messageEdit")
    public void messageEditSpecialCharacters() {
        Map<String, String> param = getParam("messageEditSpecialCharacters");
        BaseTest.paramsThreadLocal.set(param);
        
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        CurrencyManagement<?> currencyManagement = new CurrencyManagement<>(session);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        PaymentApi<?> paymentApi = new PaymentApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        int randomLength = CoreFunctions.generateRandomInt(20, 30);
        String messageID = "Auto ID "+CoreFunctions.generateRandomAlphanumericsymbol(randomLength).trim();
        String messageEN = "Auto EN "+CoreFunctions.generateRandomAlphanumericsymbol(randomLength).trim();
        List<String> defaultEn = Arrays.asList(param.get("defaultEn").split("#"));
        List<String> defaultId = Arrays.asList(param.get("defaultId").split("#"));

        step("Login into Admin dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open Currency Management Dashboard");
        currencyManagement.openCurrencyConfigPage();

        step("Open Message on dashboard");
        currencyManagement.clickButton("Message");

        step("Input the message");
        currencyManagement.inputMessage(messageID, messageEN);

        step("Verify the ID message on panel");
        Assertion.assertThat("Verify currency message ID on panel", currencyManagement.getSubmittedMessageID(), is(messageID));

        step("Saving the new message on panel");
        currencyManagement.clickButton("Save");
        currencyManagement.clickButton("Yes, save");

        step("Go to tiket homepage");
        homePageWeb.goToHomePage();

        step("Verify the currency message in EN language");
        String infoEN = homePageWeb.getImportantInfoMessage();
        Assertion.assertThat("Verify currency message in EN language", infoEN, is(messageEN));

        step("Cleanup the currency message to default via API");
        paymentApi.updateCurrencyMessage(defaultEn, defaultId);
    }

    @Author(names = {"Ashish"})
    @Module(name = "CurrencyConfig")
    @Tribe(name = "Payment")
    @Vertical(name = "Sphinx")
    @Test
    public void verifyCurrencyManagementFlow() {
        Map<String, String> param = getParam("verifyCurrencyManagementFlow");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        CurrencyManagement<?> currencyManagement = new CurrencyManagement<>(session);
        PaymentApi<?> paymentApi = new PaymentApi<>(session);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        List<String> components = Arrays.asList(param.get("components").split("#"));
        String currencyCode = param.get("currencyCode");
        String statusFilter = param.get("statusFilter");
        List<String> transitions = Arrays.asList(param.get("transitions").split("#"));
        // Baseline ticker message restored at cleanup (removes the test message). Overridable via sheet columns.
        String defaultEnParam = param.getOrDefault("defaultEn", "iie testing this is for EN");
        String defaultIdParam = param.getOrDefault("defaultId", "iie testing this is for ID");
        List<String> defaultEn = Arrays.asList(defaultEnParam.split("#"));
        List<String> defaultId = Arrays.asList(defaultIdParam.split("#"));
        int randomLength = CoreFunctions.generateRandomInt(20, 30);
        // Step 58: exercise special characters, numbers, and text in one message.
        String specialChars = "0123456789 !@#$%&*()-_+";
        String messageID = "Auto ID "+specialChars+" "+CoreFunctions.generateRandomString(randomLength);
        String messageEN = "Auto EN "+specialChars+" "+CoreFunctions.generateRandomString(randomLength);

        step("Step 1: Log in to the Admin Panel");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Step 2: Navigate to Currency Configuration -> Currency Management");
        currencyManagement.openCurrencyConfigPage();

        step("Step 3: Verify Currency Management page loads successfully");
        Assertion.assertThat("Currency Management page title is displayed",
                currencyManagement.isPageComponentDisplayed("page title"), is(true));

        step("Step 4: Verify all components are displayed");
        for (String component : components) {
            Assertion.assertThat("Component '"+component+"' is displayed",
                    currencyManagement.isPageComponentDisplayed(component), is(true));
        }

        step("[Setup] Filter by Status = "+statusFilter+" so all currencies (including inactive) are visible");
        currencyManagement.selectFilterByVisibleText("Status", statusFilter);

        step("Step 5: Search using Currency Code "+currencyCode);
        currencyManagement.searchCurrencyCode(currencyCode);

        step("Step 6: Verify matching currency record is displayed");
        Assertion.assertThat("Search result contains "+currencyCode,
                currencyManagement.getDisplayedCurrencyCodes(), hasItem(currencyCode));

        step("[Setup] Clear Currency Code search before exercising filters");
        currencyManagement.clearCurrencyCodeSearch();

        step("Steps 7-8: Filter by User Currency and verify matching records are displayed");
        currencyManagement.applyListFilter("Currency Filter", "User Currency");
        Assertion.assertThat("Records displayed for User Currency filter",
                currencyManagement.getDisplayedCurrencyCodes(), not(empty()));

        step("Steps 9-10: Filter by Vertical Currency and verify matching records are displayed");
        currencyManagement.applyListFilter("Currency Filter", "Vertical Currency");
        Assertion.assertThat("Records displayed for Vertical Currency filter",
                currencyManagement.getDisplayedCurrencyCodes(), not(empty()));

        step("Steps 11-12: Filter by Active status and verify matching records are displayed");
        currencyManagement.applyListFilter("Status", "Active");
        Assertion.assertThat("Records displayed for Active status filter",
                currencyManagement.getDisplayedCurrencyCodes(), not(empty()));

        step("Steps 13-14: Filter by Inactive status and verify matching records are displayed");
        currencyManagement.applyListFilter("Status", "Inactive");
        Assertion.assertThat("Status filter value reflects Inactive",
                currencyManagement.getStatusFilterValue(), containsStringIgnoringCase("Inactive"));

        step("Steps 15-16: Filter by All status and verify all applicable records are displayed");
        currencyManagement.applyListFilter("Status", "All");
        Assertion.assertThat("Records displayed for All status filter",
                currencyManagement.getDisplayedCurrencyCodes(), not(empty()));

        step("Steps 17-18: Filter by a GBV value and verify matching records are displayed");
        String gbvValue = currencyManagement.getFilterOptions("GBV Filter").stream()
                .filter(option -> !option.toLowerCase().contains("all"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No specific GBV option available"));
        currencyManagement.applyListFilter("GBV Filter", gbvValue);
        Assertion.assertThat("Records displayed for GBV filter "+gbvValue,
                currencyManagement.getDisplayedCurrencyCodes(), not(empty()));

        step("Step 19: Click Reset Filter");
        currencyManagement.clickResetFilter();

        step("Step 20: Verify all filters are cleared and full currency list is displayed (search field cleared)");
        Assertion.assertThat("Search currency code field is cleared",
                currencyManagement.getSearchCurrencyCodeValue(), is(""));

        step("Step 20: Verify full currency list is displayed after Reset Filter");
        Assertion.assertThat("Full currency list is displayed",
                currencyManagement.getDisplayedCurrencyCodes(), not(empty()));

        step("[Setup] Re-apply Status filter "+statusFilter+" and select currency record "+currencyCode);
        currencyManagement.selectFilterByVisibleText("Status", statusFilter);
        currencyManagement.searchCurrencyCode(currencyCode);

        step("Steps 21-23: Open Vertical Currency Status dropdown and verify available statuses (Active, Inactive, Review)");
        List<String> availableStatuses = currencyManagement.getAvailableVerticalStatuses(currencyCode);
        Assertion.assertThat("Active status is available", availableStatuses, hasItem("ACTIVE"));
        Assertion.assertThat("Inactive status is available", availableStatuses, hasItem("INACTIVE"));
        Assertion.assertThat("Review status is available", availableStatuses, hasItem("REVIEW"));

        step("[Setup] Snapshot original configuration via API for restore at the end");
        String originalVerticalStatus = paymentApi.getVerticalCurrencyStatusViaApi(currencyCode);
        boolean originalUserCurrency = paymentApi.isUserCurrencyActiveViaApi(currencyCode);
        boolean originalConsumerVisible = paymentApi.isConsumerVisibleViaApi(currencyCode);
        List<String> originalSuggested = paymentApi.getSuggestedCurrenciesViaApi();
        CoreFunctions.log("Original config for "+currencyCode+" -> status="+originalVerticalStatus
                +", userCurrency="+originalUserCurrency+", consumerVisible="+originalConsumerVisible
                +", suggested="+originalSuggested);

        // Everything below mutates the shared preprod config. Wrap it so a mid-test failure still triggers the
        // API restore in the finally block, leaving the environment clean for the next run.
        boolean restored = false;
        try {
            step("[Setup] Set baseline vertical currency status to ACTIVE");
            if (!currencyManagement.getVerticalCurrencyStatus(currencyCode).equalsIgnoreCase("ACTIVE")) {
                currencyManagement.changeVerticalCurrencyStatus(currencyCode, "Active");
            }

            for (String targetStatus : transitions) {
                step("Steps 24-25: Update vertical currency status of "+currencyCode+" to "+targetStatus+" and save");
                currencyManagement.changeVerticalCurrencyStatus(currencyCode, targetStatus);

                step("Step 26: Verify success message is displayed for "+targetStatus);
                Assertion.assertThat("Success toast for vertical currency status update",
                        currencyManagement.getToastMessage(), containsStringIgnoringCase("vertical currency status"));

                step("Step 27: Verify updated status "+targetStatus+" is reflected in listing");
                Assertion.assertThat("Vertical currency status in listing for "+currencyCode,
                        currencyManagement.getVerticalCurrencyStatus(currencyCode), is(targetStatus.toUpperCase()));

                step("Steps 28-29: Trigger Client API and verify it returns updated status "+targetStatus);
                Assertion.assertThat("Vertical currency status via API for "+currencyCode,
                        paymentApi.waitForVerticalCurrencyStatusViaApi(currencyCode, targetStatus.toUpperCase()),
                        is(targetStatus.toUpperCase()));
            }

            step("[Setup] Disable User Currency for "+currencyCode+" before enabling");
            currencyManagement.setUserCurrency(currencyCode, false);

            step("Step 30: Enable User Currency for "+currencyCode);
            currencyManagement.setUserCurrency(currencyCode, true);

            step("Step 31: Verify success message is displayed for enabling User Currency");
            Assertion.assertThat("Success toast for enabling User Currency",
                    currencyManagement.getToastMessage(), containsStringIgnoringCase("currency"));

            step("Steps 32-34: Verify updated User Currency value in listing and via Client API");
            Assertion.assertThat("User Currency toggle enabled in listing",
                    currencyManagement.isUserCurrencyEnabled(currencyCode), is(true));
            Assertion.assertThat("User Currency enabled via API",
                    paymentApi.waitForUserCurrencyActiveViaApi(currencyCode, true), is(true));

            step("Step 35: Disable User Currency for "+currencyCode);
            currencyManagement.setUserCurrency(currencyCode, false);

            step("Step 36: Verify User Currency disabled value is reflected in listing and via API");
            Assertion.assertThat("User Currency toggle disabled in listing",
                    currencyManagement.isUserCurrencyEnabled(currencyCode), is(false));
            Assertion.assertThat("User Currency disabled via API",
                    paymentApi.waitForUserCurrencyActiveViaApi(currencyCode, false), is(false));

            step("[Setup] Disable Consumer Visible for "+currencyCode+" before enabling");
            currencyManagement.setConsumerVisible(currencyCode, false);

            step("Step 37: Enable Consumer Visible for "+currencyCode);
            currencyManagement.setConsumerVisible(currencyCode, true);

            step("Step 38: Verify success message is displayed for enabling Consumer Visible");
            Assertion.assertThat("Success toast for enabling Consumer Visible",
                    currencyManagement.getToastMessage(), containsStringIgnoringCase("consumer visibility status"));

            step("Steps 39-41: Verify updated Consumer Visible value in listing and via Client API");
            Assertion.assertThat("Consumer Visible toggle enabled in listing",
                    currencyManagement.isConsumerVisibleEnabled(currencyCode), is(true));
            Assertion.assertThat("Consumer Visible enabled via API",
                    paymentApi.waitForConsumerVisibleViaApi(currencyCode, true), is(true));

            step("Step 42: Disable Consumer Visible for "+currencyCode);
            currencyManagement.setConsumerVisible(currencyCode, false);

            step("Step 43: Verify Consumer Visible disabled value is reflected in listing and via API");
            Assertion.assertThat("Consumer Visible toggle disabled in listing",
                    currencyManagement.isConsumerVisibleEnabled(currencyCode), is(false));
            Assertion.assertThat("Consumer Visible disabled via API",
                    paymentApi.waitForConsumerVisibleViaApi(currencyCode, false), is(false));

            step("Steps 44-45: Click Suggested Currency and verify popup is displayed");
            currencyManagement.openSuggestedCurrency();
            Assertion.assertThat("Suggested Currency popup is displayed",
                    currencyManagement.isSuggestedCurrencyPopupDisplayed(), is(true));

            step("[Setup] Remove any pre-existing suggested currencies for a clean start");
            for (String existing : new ArrayList<>(currencyManagement.getSelectedSuggestedCurrencies())) {
                currencyManagement.removeSuggestedCurrency(existing);
            }
            Assertion.assertThat("No suggested currencies selected after reset",
                    currencyManagement.getSelectedSuggestedCurrencies(), is(empty()));

            step("[Setup] Read available suggested currency options from the dropdown");
            List<String> options = currencyManagement.getAvailableSuggestedOptions();
            Assertion.assertThat("At least 4 suggested currency options are available to exercise the max-3 rule",
                    options.size(), greaterThanOrEqualTo(4));
            List<String> toAdd = new ArrayList<>(options.subList(0, 3));
            String exceedCode = options.get(3);

            step("Steps 46-47: Add 1 suggested currency ("+toAdd.get(0)+") and verify save is successful");
            currencyManagement.addSuggestedCurrency(toAdd.get(0));
            Assertion.assertThat("Success toast for adding suggested currency "+toAdd.get(0),
                    currencyManagement.getToastMessage(), containsStringIgnoringCase("suggested currencies"));
            Assertion.assertThat("Selected suggested currencies contain "+toAdd.get(0),
                    currencyManagement.getSelectedSuggestedCurrencies(), hasItem(toAdd.get(0)));

            step("Steps 48-49: Add up to 3 suggested currencies ("+toAdd+") and verify save is successful");
            for (int i = 1; i < toAdd.size(); i++) {
                currencyManagement.addSuggestedCurrency(toAdd.get(i));
                Assertion.assertThat("Success toast for adding suggested currency "+toAdd.get(i),
                        currencyManagement.getToastMessage(), containsStringIgnoringCase("suggested currencies"));
            }
            Assertion.assertThat("Three suggested currencies are selected",
                    currencyManagement.getSelectedSuggestedCurrencies(), hasSize(3));

            step("Step 50: Verify more than 3 suggested currencies cannot be configured (attempt "+exceedCode+")");
            Assertion.assertThat("Max 3 suggested currencies restriction is enforced",
                    currencyManagement.attemptAddSuggestedBeyondLimit(exceedCode), is(true));
            Assertion.assertThat("Still only 3 suggested currencies after exceeding attempt",
                    currencyManagement.getSelectedSuggestedCurrencies(), hasSize(3));

            step("Steps 54-55: Trigger Client API and verify configured suggested currency "+toAdd.get(0));
            Assertion.assertThat("Suggested flag via API for "+toAdd.get(0),
                    paymentApi.waitForSuggestedViaApi(toAdd.get(0), true), is(true));

            step("Steps 51-52: Edit suggested currencies by removing the ones added and save changes");
            for (String code : toAdd) {
                currencyManagement.removeSuggestedCurrency(code);
                Assertion.assertThat("Success toast for removing suggested currency "+code,
                        currencyManagement.getToastMessage(), containsStringIgnoringCase("suggested currencies"));
            }

            step("Step 53: Verify updated suggested configuration is reflected via Client API for "+toAdd.get(0));
            Assertion.assertThat("Suggested flag removed via API for "+toAdd.get(0),
                    paymentApi.waitForSuggestedViaApi(toAdd.get(0), false), is(false));

            step("[Setup] Close Suggested Currency popup");
            currencyManagement.closeSuggestedCurrency();
            currencyManagement.waitForToastToDisappear();

            step("Step 56: Click Message");
            currencyManagement.clickButton("Message");

            step("Step 57: Update ticker/message configuration with text, numbers and special characters");
            currencyManagement.inputMessage(messageID, messageEN);

            step("Step 58: Verify special characters, numbers and text are accepted as per business rules");
            Assertion.assertThat("Submitted message ID (with special chars/numbers) on panel",
                    currencyManagement.getSubmittedMessageID(), is(messageID));

            step("Step 59: Save the message configuration changes");
            currencyManagement.clickButton("Save");
            currencyManagement.clickButton("Yes, save");

            step("Step 60: Verify success message is displayed for message configuration");
            Assertion.assertThat("Success toast for message configuration",
                    currencyManagement.getToastMessage(), containsStringIgnoringCase("message"));

            step("Steps 61-62: Trigger Client API and verify updated message configuration is returned on the storefront");
            homePageWeb.goToHomePage();
            // The storefront renders the message in the locale it loads (EN or ID); accept either configured value.
            Assertion.assertThat("Configured currency message returned on storefront",
                    homePageWeb.getImportantInfoMessage(), anyOf(is(messageEN), is(messageID)));

            step("[Cleanup] Restore original configuration to its pre-test state via API");
            restoreCurrencyConfigViaApi(paymentApi, currencyCode, originalVerticalStatus, originalUserCurrency,
                    originalConsumerVisible, originalSuggested, defaultEn, defaultId);
            restored = true;
        } finally {
            if (!restored) {
                // The test failed before the happy-path restore ran; restore best-effort so we don't leave the
                // shared environment dirty, but never let a cleanup error mask the real test failure.
                try {
                    step("[Cleanup] Best-effort restore of original configuration after failure");
                    restoreCurrencyConfigViaApi(paymentApi, currencyCode, originalVerticalStatus, originalUserCurrency,
                            originalConsumerVisible, originalSuggested, defaultEn, defaultId);
                } catch (Exception cleanupError) {
                    CoreFunctions.log("[Cleanup] Best-effort restore after failure did not complete: "+cleanupError.getMessage());
                }
            }
        }

        step("[Cleanup] Verify original configuration was restored via API");
        Assertion.assertThat("Vertical currency status restored",
                paymentApi.getVerticalCurrencyStatusViaApi(currencyCode), is(originalVerticalStatus));
        Assertion.assertThat("User Currency restored",
                paymentApi.isUserCurrencyActiveViaApi(currencyCode), is(originalUserCurrency));
        Assertion.assertThat("Consumer Visible restored",
                paymentApi.isConsumerVisibleViaApi(currencyCode), is(originalConsumerVisible));
        Assertion.assertThat("Suggested currencies restored",
                paymentApi.getSuggestedCurrenciesViaApi(), containsInAnyOrder(originalSuggested.toArray(new String[0])));
    }

    /** Restores a currency's vertical status, toggles, suggested flags and the ticker message to the snapshot via API. */
    private void restoreCurrencyConfigViaApi(PaymentApi<?> paymentApi, String currencyCode,
            String originalVerticalStatus, boolean originalUserCurrency, boolean originalConsumerVisible,
            List<String> originalSuggested, List<String> defaultEn, List<String> defaultId) {
        paymentApi.setVerticalCurrencyStatusViaApi(currencyCode, originalVerticalStatus);
        paymentApi.setUserCurrencyViaApi(currencyCode, originalUserCurrency);
        paymentApi.setConsumerVisibleViaApi(currencyCode, originalConsumerVisible);
        for (String code : paymentApi.getSuggestedCurrenciesViaApi()) {
            if (!originalSuggested.contains(code)) {
                paymentApi.setSuggestedViaApi(code, false);
            }
        }
        for (String code : originalSuggested) {
            paymentApi.setSuggestedViaApi(code, true);
        }
        paymentApi.updateCurrencyMessage(defaultEn, defaultId);
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "CurrencyConfig")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform - Sphinx")
    @Test
    public void verifyTheWholeFlowOfFXRate() throws IOException, InterruptedException {
        Map<String, String> param = getParam("verifyTheWholeFlowOfFXRate");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        ForeignExchangeRatePage<?> foreignExchangeRatePage = new ForeignExchangeRatePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String fromField = param.get("fromField");
        String toField = param.get("toField");
        String validCode = param.get("validCode");
        String invalidCode = param.get("invalidCode");
        String deviationMsg = param.get("deviationMsg");
        String comparisonMsg = param.get("comparisonMsg");
        String refreshMsg = param.get("refreshMsg");
        String fileStart = param.get("fileStart");
        String fileExtension = param.get("fileExtension");
        step("Login into Admin dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open FX Rate Dashboard");
        foreignExchangeRatePage.openFXRatePage();

        step("Verify FX Rate page components, filters, and Buy/Sell data");
        foreignExchangeRatePage.verifyFxRatePageComponents();

        step("Capture the default filter states");
        Map<String, String> initialDefaults = foreignExchangeRatePage.captureDefaultFilterValues();

        step("verify the Refresh Rate flow");
        foreignExchangeRatePage.conditionalRefreshRate(refreshMsg);
        step("Select From currency as: "+fromField +" and To currency as: "+toField+", then click on search button, and verify matching records");
        foreignExchangeRatePage.filterAndVerifyFxRates(fromField, toField);

        step("Enter a valid currency code as: "+validCode+", click search, and verify configuration");
        foreignExchangeRatePage.searchAndVerifyValidCurrency(validCode);

        step("Enter an invalid currency code as: "+invalidCode+", click search, and verify empty state");
        foreignExchangeRatePage.searchAndVerifyInvalidCurrency(invalidCode);

        step("Click Reset and verify the filters returned to their original state");
        foreignExchangeRatePage.clickResetAndVerifyRestoredValues(initialDefaults);

        String initialDeviationValue = foreignExchangeRatePage.getFirstRowThresholdValue("Deviation");
        double devNumericValue = Double.parseDouble(initialDeviationValue.replace("%", "").trim());
        String initialComparisonValue = foreignExchangeRatePage.getFirstRowThresholdValue("Comparison");
        String updatedDeviation = "50";
        if(devNumericValue==50.0){
            updatedDeviation="100";
        }
        double comparisonNumericValue = Double.parseDouble(initialComparisonValue.replace("%", "").trim());
        String updatedComparison ="0.05";
        if(comparisonNumericValue==0.05){
            updatedComparison = "1";
        }
        step("Update the deviation threshold for the first row and confirm override");
        foreignExchangeRatePage.updateFirstRowDeviationThreshold(updatedDeviation);

        step("Verify the deviation threshold success snackbar message");
        foreignExchangeRatePage.verifySuccessSnackbar(deviationMsg);

        step("Update the comparison threshold for the first row and confirm override");
        foreignExchangeRatePage.updateFirstRowComparisonThreshold(updatedComparison);

        step("Verify the comparison threshold success snackbar");
        foreignExchangeRatePage.verifySuccessSnackbar(comparisonMsg);

        step("Verify the updated Deviation Threshold value is reflected in the listing");
        String updatedDeviationValue = foreignExchangeRatePage.getFirstRowThresholdValue("Deviation");
        Assertion.assertThat("Verify the Deviation value updated correctly. Expected: "+updatedDeviation+" Actual: "+updatedDeviationValue, updatedDeviationValue, is(updatedDeviation + "%"));

        step("Verify the updated Comparison Threshold value is reflected in the listing");
        String updatedComparisonValue = foreignExchangeRatePage.getFirstRowThresholdValue("Comparison");
        Assertion.assertThat("Verify the Comparison value updated correctly. Expected: "+updatedComparisonValue+" Actual: "+updatedComparisonValue, updatedComparisonValue, is(updatedComparison + "%"));

        step("Click Download Exchange Rate and confirm modal");
        foreignExchangeRatePage.clickDownloadExchangeRate();

        step("Verify exchange rate report is downloaded successfully");
        foreignExchangeRatePage.verifyDownloadedFile(fileStart, fileExtension);
    }
}
