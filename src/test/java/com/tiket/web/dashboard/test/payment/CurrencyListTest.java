package com.tiket.web.dashboard.test.payment;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.payment.CurrencyListDashboard;
import com.tiket.web.dashboard.page.payment.MultiCurrencyDashboard;
import com.tiket.web.dashboard.page.payment.PaymentDashboardPage;
import com.tiket.web.dashboard.page.promo.PromoDashboard;
import com.tiket.web.page.common.home.HomePageWeb;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class CurrencyListTest extends BaseTest {
    @Author(names = {"Hiten"})
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void VerifyTextActiveToInactive() throws InterruptedException {
        Map<String, String> map = getParam("VerifyTextActiveToInactive");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        CurrencyListDashboard<?> mDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String currencyFilter = map.get("currencyFilter");
        String filterStatus = map.get("filterStatus");
        String statusChange = map.get("statusChange");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String verificationText = map.get("verificationText");
        String verificationTextMiddle = map.get("verificationTextMiddle");
        String verification_text_middle = map.get("verification_text_middle");
        String verificationTextEnd = map.get("verificationTextEnd");;

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Login to CurrencyList dashboard");
        mDAsh.goToCurrencyList();

        step("Click on filter Currency and select vertical currency");
        mDAsh.clickOnFilterCurrency();
        mDAsh.clickOnVerticalCurrency(currencyFilter);

        step("Click on filter Status and select Active status");
        mDAsh.clickOnFilterStatus();
        mDAsh.changeFilterStatus(filterStatus);

        String currencySelected = mDAsh.getVerticalCurrencySelected();
        step("Click on vertical currency button");
        mDAsh.verticalCurrencyButton();

        step("Click on Input Field");
        mDAsh.verticalCurrencyButtonInput();

        step("Set the vertical currency to Inactive");
        mDAsh.verticalCurrencyButtonInputValue(statusChange);

        step("Click on save Modal");
        mDAsh.clickSave();

        String verification_text = verificationText + " " + currencySelected + " " + verificationTextMiddle + "," +verification_text_middle + " " + currencySelected + verificationTextEnd;
        Assertion.assertThat("Verify the status Change Text", mDAsh.getStatusChangeText(), containsString(verification_text));
        mDAsh.clickYesandwaitForVerificationMessage();

        step("Click on filter Status and select Inactive");
        mDAsh.clickOnFilterStatus();
        mDAsh.changeFilterStatus(statusChange);

        String dropdownText = mDAsh.getFilterStatusText(statusChange);
        Assertion.assertThat("Verify the text of inactive status",dropdownText,is(statusChange));

        step("Search the currency in input field");
        mDAsh.searchCurrencyInput(currencySelected);
        Assertion.assertThat("Verify the text", mDAsh.getVerticalCurrencyButtonText(), is(statusChange));

        String query = "{verticalIsActive:0,code:\"" + currencySelected + "\"}";
        int countFromDB = mDAsh.getNumberOfCurrencyMongoDB(config,database,collectionName,query);
        Assertion.assertThat("Verify the currency from db", countFromDB, is(1));
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "CurrencyManagement")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void changeVerticalCurrencyInactiveToActive() throws InterruptedException {
        Map<String, String> map = getParam("changeVerticalCurrencyInactiveToActive");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        CurrencyListDashboard<?> mDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String currencyFilter = map.get("currencyFilter");
        String filterStatus = map.get("filterStatus");
        String statusChange = map.get("statusChange");
        String verificationText = map.get("verificationText");
        String caseName = map.get("caseName");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Login to CurrencyList dashboard");
        mDAsh.goToCurrencyList();

        step("Click on filter Currency and select vertical currency");
        mDAsh.clickOnFilterCurrency();
        mDAsh.clickOnVerticalCurrency(currencyFilter);

        step("Click on filter Status and select Inactive status");
        mDAsh.clickOnFilterStatus();
        mDAsh.changeFilterStatus(filterStatus);

        step("Confirm search inactive currency");
        mDAsh.clickOnSearch();

        step("Click on vertical currency button");
        mDAsh.verticalCurrencyButton();

        step("Click on Input Field");
        mDAsh.verticalCurrencyButtonInput();

        step("Set the vertical currency to Active");
        mDAsh.verticalCurrencyButtonInputValue(statusChange);

        step("Click on save Modal");
        mDAsh.clickSave();

        Assertion.assertThat("Verify the status Change Text", mDAsh.getStatusChangeText(caseName), containsString(verificationText));
        mDAsh.clickOnOkButton();
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "CurrencyManagement")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void changeVerticalCurrencyInactiveToReview() throws InterruptedException {
        Map<String, String> map = getParam("changeVerticalCurrencyInactiveToReview");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        CurrencyListDashboard<?> mDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String currencyFilter = map.get("currencyFilter");
        String filterStatus = map.get("filterStatus");
        String filterStatusReview = map.get("filterStatusReview");
        String statusChange = map.get("statusChange");
        String verificationText = map.get("verificationText");
        String verificationTextMiddle = map.get("verificationTextMiddle");
        String verification_text_middle = map.get("verification_text_middle");
        String verificationTextEnd = map.get("verificationTextEnd");
        String caseName = map.get("caseName");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Login to CurrencyList dashboard");
        mDAsh.goToCurrencyList();

        step("Click on filter Currency and select vertical currency");
        mDAsh.clickOnFilterCurrency();
        mDAsh.clickOnVerticalCurrency(currencyFilter);

        step("Click on filter Status and select Inactive status");
        mDAsh.clickOnFilterStatus();
        mDAsh.changeFilterStatus(filterStatus);

        step("Confirm search inactive currency");
        mDAsh.clickOnSearch();

        step("Click on vertical currency button");
        mDAsh.verticalCurrencyButton();

        step("Get the Currency Code Value");
        String currencyCode = mDAsh.getSelectedVerticalCurrencyCode();

        step("Click on Input Field");
        mDAsh.verticalCurrencyButtonInput();

        step("Set the vertical currency to Review");
        mDAsh.verticalCurrencyButtonInputValue(statusChange);

        step("Click on save Modal");
        mDAsh.clickSave();

        String verification_text = verificationText + " " + currencyCode + " " + verificationTextMiddle + "," +
                verification_text_middle + " " + currencyCode + verificationTextEnd;
        Assertion.assertThat("Verify the status Change Text", mDAsh.getStatusChangeText(caseName), containsString(verification_text));

        step("Click on save Modal");
        mDAsh.clickYesandwaitForVerificationMessage();

        step("Click on filter Status and select Review");
        mDAsh.clickOnFilterStatus();
        mDAsh.changeFilterStatus(filterStatusReview);

        step("Confirm search review currency");
        mDAsh.clickOnSearch();

        String currencyStatus = mDAsh.getCurrencyStatusInFilter();
        Assertion.assertThat("Verify currency shown is in Review status", currencyStatus, is(filterStatusReview));

        step("Search the updated currency in input field");
        mDAsh.searchCurrencyInput(currencyCode);
        mDAsh.clickOnSearch();

        step("Verify the updated currency status is in Review");
        Assertion.assertThat("Verify the text", mDAsh.getSelectedVerticalCurrencyText(), is(filterStatusReview));
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "CurrencyManagement")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void changeVerticalCurrencyReviewToInactive() throws InterruptedException {
        Map<String, String> map = getParam("changeVerticalCurrencyReviewToInactive");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        CurrencyListDashboard<?> mDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String currencyFilter = map.get("currencyFilter");
        String filterStatus = map.get("filterStatus");
        String filterStatusInactive = map.get("filterStatusInactive");
        String statusChange = map.get("statusChange");
        String verificationText = map.get("verificationText");
        String verificationTextMiddle = map.get("verificationTextMiddle");
        String verification_text_middle = map.get("verification_text_middle");
        String verificationTextEnd = map.get("verificationTextEnd");
        String caseName = map.get("caseName");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Login to CurrencyList dashboard");
        mDAsh.goToCurrencyList();

        step("Click on filter Currency and select vertical currency");
        mDAsh.clickOnFilterCurrency();
        mDAsh.clickOnVerticalCurrency(currencyFilter);

        step("Click on filter Status and select Review status");
        mDAsh.clickOnFilterStatus();
        mDAsh.changeFilterStatus(filterStatus);

        step("Confirm search review currency");
        mDAsh.clickOnSearch();

        step("Click on vertical currency button");
        mDAsh.verticalCurrencyButton();

        step("Get the Currency Code Value");
        String currencyCode = mDAsh.getSelectedVerticalCurrencyCode();

        step("Click on Input Field");
        mDAsh.verticalCurrencyButtonInput();

        step("Set the vertical currency to Inactive");
        mDAsh.verticalCurrencyButtonInputValue(statusChange);

        step("Click on save Modal");
        mDAsh.clickSave();

        String verification_text = verificationText + " " + currencyCode + " " + verificationTextMiddle + "," +
                verification_text_middle + " " + currencyCode + verificationTextEnd;

        Assertion.assertThat("Verify the status Change Text", mDAsh.getStatusChangeText(caseName), containsString(verification_text));

        step("Click on save Modal");
        mDAsh.clickYesandwaitForVerificationMessage();

        step("Click on filter Status and select Inactive");
        mDAsh.clickOnFilterStatus();
        mDAsh.changeFilterStatus(filterStatusInactive);

        step("Confirm search inactive currency");
        mDAsh.clickOnSearch();

        String currencyStatus = mDAsh.getCurrencyStatusInFilter();
        Assertion.assertThat("Verify currency shown is in Inactive status", currencyStatus, is(filterStatusInactive));

        step("Search the updated currency in input field");
        mDAsh.searchCurrencyInput(currencyCode);
        mDAsh.clickOnSearch();

        step("Verify the updated currency status is in Inactive");
        Assertion.assertThat("Verify the text", mDAsh.getSelectedVerticalCurrencyText(), is(filterStatusInactive));
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "CurrencyManagement")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void changeVerticalCurrencyActiveToReview() throws InterruptedException {
        Map<String, String> map = getParam("changeVerticalCurrencyActiveToReview");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        CurrencyListDashboard<?> mDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String currencyFilter = map.get("currencyFilter");
        String filterStatus = map.get("filterStatus");
        String filterStatusReview = map.get("filterStatusReview");
        String statusChange = map.get("statusChange");
        String verificationText = map.get("verificationText");
        String verificationTextMiddle = map.get("verificationTextMiddle");
        String verification_text_middle = map.get("verification_text_middle");
        String verificationTextEnd = map.get("verificationTextEnd");
        String caseName = map.get("caseName");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Login to CurrencyList dashboard");
        mDAsh.goToCurrencyList();

        step("Click on filter Currency and select vertical currency");
        mDAsh.clickOnFilterCurrency();
        mDAsh.clickOnVerticalCurrency(currencyFilter);

        step("Click on filter Status and select Active status");
        mDAsh.clickOnFilterStatus();
        mDAsh.changeFilterStatus(filterStatus);

        step("Confirm search active currency");
        mDAsh.clickOnSearch();

        step("Click on vertical currency button");
        mDAsh.verticalCurrencyButton();

        step("Get the Currency Code Value");
        String currencyCode = mDAsh.getVerticalCurrencySelected();

        step("Click on Input Field");
        mDAsh.verticalCurrencyButtonInput();

        step("Set the vertical currency to Review");
        mDAsh.verticalCurrencyButtonInputValue(statusChange);

        step("Click on save Modal");
        mDAsh.clickSave();

        String verification_text = verificationText + " " + currencyCode + " " + verificationTextMiddle + "," +
                verification_text_middle + " " + currencyCode + verificationTextEnd;
        Assertion.assertThat("Verify the status Change Text", mDAsh.getStatusChangeText(caseName), containsString(verification_text));

        step("Click on save Modal");
        mDAsh.clickYesandwaitForVerificationMessage();

        step("Click on filter Status and select Review");
        mDAsh.clickOnFilterStatus();
        mDAsh.changeFilterStatus(filterStatusReview);

        step("Confirm search review currency");
        mDAsh.clickOnSearch();

        String currencyStatus = mDAsh.getCurrencyStatusInFilter();
        Assertion.assertThat("Verify currency shown is in Review status", currencyStatus, is(filterStatusReview));

        step("Search the updated currency in input field");
        mDAsh.searchCurrencyInput(currencyCode);
        mDAsh.clickOnSearch();

        step("Verify the updated currency status is in Review");
        Assertion.assertThat("Verify the text", mDAsh.getSelectedVerticalCurrencyText(), is(filterStatusReview));
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "CurrencyManagement")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void changeVerticalCurrencyReviewToActive() throws InterruptedException {
        Map<String, String> map = getParam("changeVerticalCurrencyReviewToActive");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        CurrencyListDashboard<?> mDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String currencyFilter = map.get("currencyFilter");
        String filterStatus = map.get("filterStatus");
        String filterStatusActive = map.get("filterStatusActive");
        String statusChange = map.get("statusChange");
        String verificationText = map.get("verificationText");
        String verificationTextMiddle = map.get("verificationTextMiddle");
        String verification_text_middle = map.get("verification_text_middle");
        String verificationTextEnd = map.get("verificationTextEnd");
        String caseName = map.get("caseName");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Login to CurrencyList dashboard");
        mDAsh.goToCurrencyList();

        step("Click on filter Currency and select vertical currency");
        mDAsh.clickOnFilterCurrency();
        mDAsh.clickOnVerticalCurrency(currencyFilter);

        step("Click on filter Status and select Review status");
        mDAsh.clickOnFilterStatus();
        mDAsh.changeFilterStatus(filterStatus);

        step("Confirm search review currency");
        mDAsh.clickOnSearch();

        step("Click on vertical currency button");
        mDAsh.verticalCurrencyButton();

        step("Get the Currency Code Value");
        String currencyCode = mDAsh.getVerticalCurrencySelected();

        step("Click on Input Field");
        mDAsh.verticalCurrencyButtonInput();

        step("Set the vertical currency to Active");
        mDAsh.verticalCurrencyButtonInputValue(statusChange);

        step("Click on save Modal");
        mDAsh.clickSave();

        String verification_text = verificationText + " " + currencyCode + " " + verificationTextMiddle + "," +
                verification_text_middle + " " + currencyCode + verificationTextEnd;
        Assertion.assertThat("Verify the status Change Text", mDAsh.getStatusChangeText(caseName), containsString(verification_text));

        step("Click on save Modal");
        mDAsh.clickYesandwaitForVerificationMessage();

        step("Click on filter Status and select Active");
        mDAsh.clickOnFilterStatus();
        mDAsh.changeFilterStatus(filterStatusActive);

        step("Confirm search active currency");
        mDAsh.clickOnSearch();

        String currencyStatus = mDAsh.getCurrencyStatusInFilter();
        Assertion.assertThat("Verify currency shown is in Active status", currencyStatus, is(filterStatusActive));

        step("Search the updated currency in input field");
        mDAsh.searchCurrencyInput(currencyCode);
        mDAsh.clickOnSearch();

        step("Verify the updated currency status is in Active");
        Assertion.assertThat("Verify the text", mDAsh.getSelectedVerticalCurrencyText(), is(filterStatusActive));
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "CurrencyManagement")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void suggestedCurrencyMoreThanThree() throws InterruptedException {
        Map<String, String> map = getParam("suggestedCurrencyMoreThanThree");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        CurrencyListDashboard<?> mDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String verificationText = map.get("verificationText");
        String caseName = map.get("caseName");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Login to CurrencyList dashboard");
        mDAsh.goToCurrencyList();

        step("Click to Suggested Currency Button");
        mDAsh.suggestedCurrencyButton();

        step("Click to Suggested Currency Text Box");
        mDAsh.suggestedCurrencyTextBox();

        step("Select Suggested Currency");
        mDAsh.suggestedCurrency();

        step("Verify that user can't add more than 3 currencies");
        Assertion.assertThat("Verify that User should not be able to add the currency", mDAsh.getStatusChangeText(caseName), containsString(verificationText));
        mDAsh.clickOnOkButton();
    }

    @Author(names = {"Hiten"})
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void VerifyTextOnDisableUserCurrency() throws InterruptedException {
        Map<String, String> map = getParam("VerifyTextOnDisableUserCurrency");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        CurrencyListDashboard<?> mDAsh = new CurrencyListDashboard<>(session);
        MultiCurrencyDashboard<?> multiDAsh = new MultiCurrencyDashboard<>(session);
        HomePageWeb<?> dWebHome = HomePageWeb.getInstance(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String verification_message_starting = map.get("verification_message_starting");
        String verification_message_mid1 = map.get("verification_message_mid1");
        String verification_message_mid2 = map.get("verification_message_mid2");
        String verification_message_ending = map.get("verification_message_ending");
        
        step("Login into member dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();
        multiDAsh.maximize();
        
        step("Login to Currency List payment dashboard");
        mDAsh.goToCurrencyList();

        step("Get the Currency Code Value");
        String currencyCode = mDAsh.getVerticalCurrencySelected();

        step("Click on disable the user currency");
        mDAsh.clickOnUserCurrencyEnableDisableButton();

        String verificationMessage = mDAsh.getUserCurrencyEnableModalText();
        Assertion.assertThat("Verify the message is correct or not", verificationMessage, is(verification_message_starting +" " + currencyCode + verification_message_mid1 + ", " + verification_message_mid2+" "+ currencyCode +" "+verification_message_ending));

        step("Click on disable button on modal");
        mDAsh.clickOnUserCurrencyDisableButton();

        step("Verify from db consumer side the currency is inactive");
        String query = "{userIsActive:true,\"code\":\"" + currencyCode + "\"}";
        int countFromDB = mDAsh.getNumberOfCurrencyMongoDB(config,database,collectionName,query);
        Assertion.assertThat("Verify the count is zero on db (means the currency is inactive)", countFromDB, is(0));

        step("Go to consumer side home page");
        dWebHome.goToHomePage();
        Assertion.assertThat("Verify the home page is opened", dWebHome.isHomePageOpened(), is(true));

        step("Open the language selector drawer");
        dWebHome.clickLanguageSelector();

        step("Verify currency on consumer side");
        boolean isCurrencyAvailable = dWebHome.isCurrencyAvailable(currencyCode);
        Assertion.assertThat("Verify currency is inactive", isCurrencyAvailable, is(false));
    }

    @Author(names = {"Hiten"})
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void VerifyTextOnEnableUserCurrency() throws InterruptedException {
        Map<String, String> map = getParam("VerifyTextOnEnableUserCurrency");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        CurrencyListDashboard<?> mDAsh = new CurrencyListDashboard<>(session);
        MultiCurrencyDashboard<?> multiDAsh = new MultiCurrencyDashboard<>(session);
        HomePageWeb<?> dWebHome = HomePageWeb.getInstance(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String verification_message_starting = map.get("verification_message_starting");
        String verification_message_mid1 = map.get("verification_message_mid1");
        String verification_message_mid2 = map.get("verification_message_mid2");
        String verification_message_ending = map.get("verification_message_ending");
        String filterCurrency = map.get("filterCurrency");
        String filterStatus = map.get("filterStatus");
        
        step("Login into member dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();
        multiDAsh.maximize();
        
        step("Login to Currency List payment dashboard");
        mDAsh.goToCurrencyList();

        step("Now go to inactive currencies");
        mDAsh.clickOnFilterStatus();
        mDAsh.clickOnFilterStatusInactive("INACTIVE");

        String dropdownTextFilter = mDAsh.getFilterText();
        Assertion.assertThat("Verify the text of User currency",dropdownTextFilter,is(filterCurrency));

        String dropdownText = mDAsh.getFilterStatusText("INACTIVE");
        Assertion.assertThat("Verify the text of inactive status",dropdownText,is(filterStatus));

        boolean flag = mDAsh.isNoRecordDisplayed();
        if(!flag){
            step("Get the Currency Code Value");
            String currencyCode = mDAsh.getVerticalCurrencySelected();

            step("Click on enable the user currency");
            mDAsh.clickOnUserCurrencyEnableDisableButton();

            step("Make the currency enabled");
            mDAsh.clickOnUserCurrencyEnableButton();

            step("Verify the text when we enable the user currency");
            String verificationMessage = mDAsh.getUserCurrencyEnableModalText();
            Assertion.assertThat("Verify the message is correct or not", verificationMessage, is(verification_message_starting +" " + currencyCode + verification_message_mid1 + ", " + verification_message_mid2+" "+ currencyCode +" "+verification_message_ending));

            step("Verify from db consumer side the currency is inactive");
            String query = "{userIsActive:true,\"code\":\"" + currencyCode + "\"}";
            int countFromDB = mDAsh.getNumberOfCurrencyMongoDB(config,database,collectionName,query);
            Assertion.assertThat("Verify the count is zero on db (means the currency is inactive)", countFromDB, is(1));

            step("Go to consumer side home page");
            dWebHome.goToHomePage();
            Assertion.assertThat("Verify the home page is opened", dWebHome.isHomePageOpened(), is(true));

            step("Open the language selector drawer");
            dWebHome.clickLanguageSelector();

            step("Verify currency on consumer side");
            boolean isCurrencyAvailable = dWebHome.isCurrencyAvailable(currencyCode);
            Assertion.assertThat("Verify currency is active", isCurrencyAvailable, is(true));
        }
        else{
            Logger.getLogger("No inacitve currency available");
        }
    }

    @Author(names = {"Hiten", "Hafizh"})
    @Module(name = "CurrencyManagement")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyInputOneSuggestedCurrency() throws InterruptedException {
        Map<String, String> map = getParam("verifyInputOneSuggestedCurrency");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        CurrencyListDashboard<?> mDAsh = new CurrencyListDashboard<>(session);
        HomePageWeb<?> dWebHome = HomePageWeb.getInstance(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String query = "{userIsActive:true,\"isSuggested\" : true}";
        String verificationText = map.get("verificationText");
        String caseName = map.get("caseName");
        
        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();
        
        step("Login to Currency List payment dashboard");
        mDAsh.goToCurrencyList();

        step("Click to Suggested Currency Button");
        mDAsh.suggestedCurrencyButton();

        step("Ensure 3 suggested currencies exist as default state");
        mDAsh.ensureThreeSuggestedCurrencies();

        step("Click to Suggested Currency Text Box");
        mDAsh.suggestedCurrencyTextBox();

        step("Check the number of currency available");
        List<String> numberOfSuggestedCurrencyAvailable = mDAsh.getSuggestedCurrencyNames();
        if(numberOfSuggestedCurrencyAvailable.size()<3){
            step("Check and add the currency which is not available");
            mDAsh.addSuggestedCurrency();

            step("Click on Yes button to save the currency");
            mDAsh.clickYes();
        }
        else{
            step("Select Suggested Currency");
            mDAsh.suggestedCurrency();
            Assertion.assertThat("Verify that User should not be able to add the currency", mDAsh.getStatusChangeText(caseName), containsString(verificationText));
        }

        step("Verify number of currency after adding the currency on database");
        List<String> numberOfSuggestedCurrencyInDB = mDAsh.getSuggestedCurrencyFromMongoDB(config,database,collectionName,query);

        step("Verify number of currency after adding the currency on panel side");
        List<String> numberOfSuggestedCurrencyAvailableOnPanelSide = mDAsh.getSuggestedCurrencyNames();
        
        step("Go to consumer side home page");
        dWebHome.goToHomePage();
        Assertion.assertThat("Verify the home page is opened", dWebHome.isHomePageOpened(), is(true));

        step("Open the language selector drawer");
        dWebHome.clickLanguageSelector();

        step("Verify number of currency after adding the currency on consumer side");
        List<String> numberOfSuggestedCurrencyOnConsumerSide = mDAsh.getConsumerSuggestedCurrencyNames();

        Assertion.assertThat("Verify the currency on panel side or on database is same", numberOfSuggestedCurrencyInDB, is(numberOfSuggestedCurrencyAvailableOnPanelSide));
        Assertion.assertThat("Verify the currency on panel side or on consumer is same", numberOfSuggestedCurrencyOnConsumerSide, is(numberOfSuggestedCurrencyAvailableOnPanelSide));
    }

    @Author(names = {"Hiten", "Hafizh"})
    @Module(name = "CurrencyManagement")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dependsOnMethods = "verifyInputOneSuggestedCurrency")
    public void verifyInputThreeSuggestedCurrency() throws InterruptedException {
        Map<String, String> map = getParam("verifyInputThreeSuggestedCurrency");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        CurrencyListDashboard<?> mDAsh = new CurrencyListDashboard<>(session);
        HomePageWeb<?> dWebHome = HomePageWeb.getInstance(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String query = "{userIsActive:true,\"isSuggested\" : true}";
        
        step("Login into member dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();
        
        step("Login to Currency List payment dashboard");
        mDAsh.goToCurrencyList();

        step("Click to Suggested Currency Button");
        mDAsh.suggestedCurrencyButton();

        step("Ensure 3 suggested currencies exist as default state");
        mDAsh.ensureThreeSuggestedCurrencies();

        step("Check the number of currency available");
        List<String> numberOfSuggestedCurrencyAvailable = mDAsh.getSuggestedCurrencyNames();
        int count = numberOfSuggestedCurrencyAvailable.size();

        if (count >= 3) {
            step("Delete all existing suggested currencies");
            while (count > 0) {
                mDAsh.deleteSuggestedCurrency();
                count--;
            }
            List<String> remaining = mDAsh.getSuggestedCurrencyNames();
            Assertion.assertThat("All currencies should be deleted", remaining.size(), is(0));
        }

        while (count < 3) {
            step("Add currency until count reaches 3");
            mDAsh.suggestedCurrencyTextBox();
            mDAsh.addSuggestedCurrency();
            mDAsh.clickYes();
            mDAsh.waitForDisappearingVerificationMessage();
            mDAsh.waitForSuccessToastToDisappear();
            count++;
        }

        step("Verify number of currency after adding the currency on database");
        List<String> numberOfSuggestedCurrencyInDB = mDAsh.getSuggestedCurrencyFromMongoDB(config,database,collectionName,query);

        step("Verify number of currency after adding the currency on panel side");
        List<String> numberOfSuggestedCurrencyAvailableOnPanelSide = mDAsh.getSuggestedCurrencyNames();
        
        step("Go to consumer side home page");
        dWebHome.goToHomePage();
        Assertion.assertThat("Verify the home page is opened", dWebHome.isHomePageOpened(), is(true));

        step("Open the language selector drawer");
        dWebHome.clickLanguageSelector();

        step("Verify number of currency after adding the currency on consumer side");
        List<String> numberOfSuggestedCurrencyOnConsumerSide = mDAsh.getConsumerSuggestedCurrencyNames();

        Assertion.assertThat("Verify the currency on panel side or on database is same", numberOfSuggestedCurrencyInDB, is(numberOfSuggestedCurrencyAvailableOnPanelSide));
        Assertion.assertThat("Verify the currency on panel side or on consumer is same", numberOfSuggestedCurrencyOnConsumerSide, is(numberOfSuggestedCurrencyAvailableOnPanelSide));
    }

    @Author(names = {"GibranD", "Hafizh"})
    @Module(name = "CurrencyManagement")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyUserCurrencyIsDisabled() throws InterruptedException {
        Map<String, String> map = getParam("verifyUserCurrencyIsDisabled");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        CurrencyListDashboard<?> cDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String currencyType = map.get("currencyType");
        String currencyName = map.get("currencyName");
        String filterStatus = map.get("filterStatus");
        String currencyTypeVertical = map.get("currencyTypeVertical");
        String selectedVerticalCurrencyStatus = map.get("selectedVerticalCurrencyStatus");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);
        payDash.openPaymentsPage();

        step("Go to Currency List dashboard");
        cDAsh.goToCurrencyList();

        step("Click on currency filter and select User Currency");
        cDAsh.clickOnFilterCurrency();
        cDAsh.clickOnUserCurrency(currencyType);

        step("Click on status filter and select All status");
        cDAsh.clickOnFilterStatus();
        cDAsh.clickOnFilterStatusAll(filterStatus);

        step("Search for Currency");
        cDAsh.searchCurrencyInput(currencyName);

        step("Confirm search [" + currencyName + "] currency");
        cDAsh.clickOnSearch();

        if(cDAsh.getCurrencyStatus()){
            step("Click disable currency");
            cDAsh.clickOnUserCurrencyEnableDisableButton();
            cDAsh.clickOnUserCurrencyDisableButton();
        }

        step("Click on currency filter");
        cDAsh.clickOnFilterCurrency();

        step("Click on Vertical Currency");
        cDAsh.clickOnUserCurrency(currencyTypeVertical);

        step("Set Status Filter to All");
        cDAsh.clickOnFilterStatus();
        cDAsh.clickOnFilterStatusAll(filterStatus);

        step("Search for Currency");
        cDAsh.searchCurrencyInput(currencyName);

        step("Confirm search [" + currencyName + "] currency");
        cDAsh.clickOnSearch();

        step("Verify Currency in Vertical Currency Status is Inactive/Review");
        Assertion.assertThat("Verify vertical currency status is not in Active", cDAsh.getSelectedVerticalCurrencyText(), is(selectedVerticalCurrencyStatus));

        step("Click enable currency");
        cDAsh.clickOnUserCurrencyEnableDisableButton();
        cDAsh.clickOnUserCurrencyEnableButton();

        step("Click on currency filter");
        cDAsh.clickOnFilterCurrency();

        step("Click on User Currency");
        cDAsh.clickOnUserCurrency(currencyType);

        step("Set Status Filter to All");
        cDAsh.clickOnFilterStatus();
        cDAsh.clickOnFilterStatusAll(filterStatus);

        step("Search for Currency");
        cDAsh.searchCurrencyInput(currencyName);

        step("Verify User Currency Status is Successfully Changed Back to Active");
        Assertion.assertThat("Verify vertical currency status is back in Active", cDAsh.getCurrencyStatus(), is(true));
    }

    @Author(names = {"GibranD", "Hafizh"})
    @Module(name = "CurrencyManagement")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyUserVerticalCurrencyVisibility() throws InterruptedException {
        Map<String, String> map = getParam("verifyUserVerticalCurrencyVisibility");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        CurrencyListDashboard<?> cDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String currencyType = map.get("currencyType");
        String currencyName = map.get("currencyName");
        String filterStatus = map.get("filterStatus");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);
        payDash.openPaymentsPage();

        step("Go to Currency List dashboard");
        cDAsh.goToCurrencyList();

        step("Click on currency filter and select Vertical Currency");
        cDAsh.clickOnFilterCurrency();
        cDAsh.clickOnUserCurrency(currencyType);

        step("Click on status filter and select All status");
        cDAsh.clickOnFilterStatus();
        cDAsh.clickOnFilterStatusAll(filterStatus);

        step("Search for Currency");
        cDAsh.searchCurrencyInput(currencyName);

        step("Confirm search [" + currencyName + "] currency");
        cDAsh.clickOnSearch();

        if(cDAsh.getCurrencyStatus()){
            step("Click disable currency");
            cDAsh.clickOnUserCurrencyEnableDisableButton();
            cDAsh.clickOnUserCurrencyDisableButton();
        }

        step("Verify from db consumer side the currency is inactive");
        String query = "{userIsActive:true,\"code\":\"" + currencyName + "\"}";
        int countFromDB = cDAsh.getNumberOfCurrencyMongoDB(config,database,collectionName,query);
        Assertion.assertThat("Verify the count is zero on db (means the currency is inactive)", countFromDB, is(0));

        step("Click enable currency");
        cDAsh.clickOnUserCurrencyEnableDisableButton();
        cDAsh.clickOnUserCurrencyEnableButton();

        step("Verify User Currency Status is Successfully Changed Back to Active");
        cDAsh.waitForCurrencyStatus(true, 5);
        Assertion.assertThat("Verify vertical currency status is back in Active", cDAsh.getCurrencyStatus(), is(true));
    }
}

