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
import org.testng.annotations.Test;

import java.text.DecimalFormat;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.containsString;

public class MultiCurrencyDashboardTest extends BaseTest {
    @Author(names = {"Hiten", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void multiCurrencyRefresh() throws InterruptedException {
        Map<String, String> map = getParam("multiCurrencyRefresh");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String loginUsername = map.get("loginUsername");

        step("Login into payment dashboard");
        payDash.goToAdminCarePageViaUI(username,password);
        payDash.openPaymentsPage();

        step("Go to Multicurrency dashboard");
        mDAsh.multiCurrencyDashboard();

        step("Check if it already refreshed or not");
        if(!mDAsh.isElementRefreshed()){
            step("Refresh the Multicurrency rate icon");
            mDAsh.refresh_MC_V1();

            Assertion.assertThat("Multicurrency dashboard is refreshed", mDAsh.isElementRefreshed(),is(true));
            String doneByUsername = mDAsh.fetchDoneByNameFromMongoDB(config, database, collectionName);
            Assertion.assertThat("Verify the username is same or not from db and panel", loginUsername, is(doneByUsername));
        }
        else{
            String modalInfo = mDAsh.getRefreshModalInfo();
            Assertion.assertThat("Verify the text on panel side", modalInfo.contains("wait"), is(true));
        }

    }

    @Author(names = {"Hiten", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyBuyAndSell() throws InterruptedException {
        Map<String, String> map = getParam("verifyBuyAndSell");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Go to multicurrency dashboard");
        mDAsh.multiCurrencyDashboard();

        step("Sleep for 2 seconds for loading the data");
        sleep(2);

        step("Getting the buy and sell Info");
        String buy  = mDAsh.getInfo();
        Double buyValuePanel = Double.parseDouble(buy);
        String sell = mDAsh.getSellInfo();
        Double sellValuePanel = Double.parseDouble(sell);

        Map<String, Double> buySellValues = mDAsh.fetchLatestBuySellValuesFromMongoDB(config, database, collectionName);
        Double buyValue = buySellValues.get("Buy");
        Double sellValue = buySellValues.get("Sell");

        Assertion.assertThat("Verify the data from panel and db is same for buyValue", buyValue, is(buyValuePanel));
        Assertion.assertThat("Verify the data from panel and db is same for sellValue", sellValue, is(sellValuePanel));
    }

    @Author(names = {"Hiten", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void selectUserCurrency() throws InterruptedException {
        Map<String, String> map = getParam("selectUserCurrency");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);
        CurrencyListDashboard<?> cDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String currencytext = map.get("currencyText");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String query = "{fromCurrency:{$in:[\"EUR\", \"GBP\", \"AUD\", \"NZD\", \"USD\", \"CAD\", \"CHF\", \"JPY\", \"CNY\", \"HKD\", \"SGD\",\n" + //
                "          \"MYR\", \"THB\", \"PHP\", \"VND\", \"IDR\", \"BRL\"]},toCurrency:\"IDR\"}";

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Go to multicurrency dashboard");
        mDAsh.multiCurrencyDashboard();

        step("Confirm search FX rate");
        mDAsh.confirmSearch();

        step("Assertion if the correct element is selected in list or not");
        String dropdowntext = mDAsh.dropdownText();
        Assertion.assertThat("Currency element is displayed",mDAsh.isCurrencyElementDisplayed(),is(true));
        Assertion.assertThat("User Currency is selected",dropdowntext,is(currencytext));

        step("Get the number of currency on db side");
        int numberofCurrencyOnDBSide = cDAsh.getNumberOfCurrencyMongoDB(config,database,collectionName,query);

        step("Get the number of currency from the panel side");
        int numberofCurrencyOnPanelSide = cDAsh.getNumberOfCurrencyOnPanelSide();
        Assertion.assertThat("Verify the number of currency on panel side and db side is same or not",numberofCurrencyOnPanelSide ,is(numberofCurrencyOnDBSide));
    }

    @Author(names = {"Hiten", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void selectAllCurrency() throws InterruptedException {
        Map<String, String> map = getParam("selectAllCurrency");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);
        CurrencyListDashboard<?> cDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String currencytext = map.get("currencyText");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String query = "{ \"toCurrency\": \"IDR\" }";

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Go to multicurrency dashboard");
        mDAsh.multiCurrencyDashboard();

        step("Select the all currency from dropdown list");
        mDAsh.clickAllCurrencyFROM();

        step("Confirm search FX rate");
        mDAsh.confirmSearch();

        step("Assertion if the correct element is selected in list or not");
        String dropdowntext = mDAsh.dropdownText();
        Assertion.assertThat("Currency element is displayed",mDAsh.isCurrencyElementDisplayed(),is(true));
        Assertion.assertThat("All Currency is selected",dropdowntext,is(currencytext));

        step("Get the number of currency on db side");
        int numberofCurrencyOnDBSide = cDAsh.getNumberOfCurrencyMongoDB(config,database,collectionName,query);

        step("Get the number of currency from the panel side");
        int numberofCurrencyOnPanelSide = cDAsh.getNumberOfCurrencyOnPanelSide();
        Assertion.assertThat("Verify the number of currency on panel side and db side is same or not",numberofCurrencyOnPanelSide ,is(numberofCurrencyOnDBSide));
    }

    @Author(names = {"Hiten", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void inputCurrencyField() throws InterruptedException {
        Map<String, String> map = getParam("inputCurrencyField");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);
        CurrencyListDashboard<?> cDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String query = "{fromCurrency:{$in:[\"EUR\", \"GBP\", \"AUD\", \"NZD\", \"USD\", \"CAD\", \"CHF\", \"JPY\", \"CNY\", \"HKD\", \"SGD\",\n" + //
                "          \"MYR\", \"THB\", \"PHP\", \"VND\", \"IDR\", \"BRL\"]},toCurrency:\"IDR\"}";

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Go to multicurrency dashboard");
        mDAsh.multiCurrencyDashboard();

        step("Wait for the dashboard to load the details");
        sleep(3);

        step("Input the currency in the field");
        cDAsh.searchCurrencyInput(currency);

        step("Confirm search FX rate");
        mDAsh.confirmSearch();

        step("Get the number of currency on db side");
        int numberofCurrencyOnDBSide = mDAsh.getNumberOfCurrencyForSearchKey(config,database,collectionName,query,currency);

        step("Get the number of currency from the panel side");
        int numberofCurrencyOnPanelSide = cDAsh.getNumberOfCurrencyOnPanelSide();
        Assertion.assertThat("Verify the number of currency on panel side and db side is same or not",numberofCurrencyOnPanelSide ,is(numberofCurrencyOnDBSide));
    }

    @Author(names = {"Hiten", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void inputByWrongCurrency() throws InterruptedException {
        Map<String, String> map = getParam("inputByWrongCurrency");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);
        CurrencyListDashboard<?> cDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String query = "{ \"toCurrency\": \"IDR\" }";

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Go to multicurrency dashboard");
        mDAsh.multiCurrencyDashboard();

        step("wait for the dashboard to load the details");
        sleep(3);

        step("Input the wrong currency");
        cDAsh.searchCurrencyInput(currency);

        step("Confirm search FX rate");
        mDAsh.confirmSearch();

        Assertion.assertThat("Currency List is not displayed", mDAsh.isCurrencyElementDisplayed(),is(false));

        step("Get the number of currency from the panel side");
        int numberofCurrencyOnPanelSide = 0;

        step("Get the number of currency on db side");
        int numberofCurrencyOnDBSide = mDAsh.getNumberOfCurrencyForSearchKey(config,database,collectionName,query,currency);
        Assertion.assertThat("Verify the number of currency on panel side and db side is same or not",numberofCurrencyOnPanelSide ,is(numberofCurrencyOnDBSide));
    }

    @Author(names = {"Hiten", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void selectToField() throws InterruptedException {
        Map<String, String> map = getParam("selectToField");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);
        CurrencyListDashboard<?> cDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String query = "{ \"toCurrency\": \"IDR\" }";

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Go to multicurrency dashboard");
        mDAsh.multiCurrencyDashboard();

        step("Select any currency in TO Field");
        mDAsh.selectToField();

        step("Select the All currency in From dropdown");
        mDAsh.clickAllCurrencyFROM();

        step("Confirm search FX rate");
        mDAsh.confirmSearch();

        step("Get the number of currency on db side");
        int numberofCurrencyOnDBSide = cDAsh.getNumberOfCurrencyMongoDB(config,database,collectionName,query);

        step("Get the number of currency from the panel side");
        int numberofCurrencyOnPanelSide = cDAsh.getNumberOfCurrencyOnPanelSide();
        Assertion.assertThat("Verify the number of currency on panel side and db side is same or not",numberofCurrencyOnPanelSide ,is(numberofCurrencyOnDBSide));
    }

    @Author(names = {"Hiten", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void inputAnyCurrencyInToDropdown() throws InterruptedException {
        Map<String, String> map = getParam("inputAnyCurrencyInToDropdown");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);
        CurrencyListDashboard<?> cDAsh = new CurrencyListDashboard<>(session);

        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String query = "{ \"toCurrency\": \"AUD\" }";

        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Go to Multicurrency dashboard");
        mDAsh.multiCurrencyDashboard();

        step("Select the currency in TO field");
        mDAsh.selectInTODropdown();

        step("Select the All currency in From dropdown");
        mDAsh.clickAllCurrencyFROM();

        step("Check assertion is Dropdown selected or not");
        String dropdownText = mDAsh.IsselectInTODropdown();
        Assertion.assertThat("Dropdown is selected",dropdownText,is(currency));

        step("Confirm search FX rate");
        mDAsh.confirmSearch();

        step("Get the number of currency on db side");
        int numberofCurrencyOnDBSide = cDAsh.getNumberOfCurrencyMongoDB(config,database,collectionName,query);

        step("Get the number of currency from the panel side");
        int numberofCurrencyOnPanelSide = cDAsh.getNumberOfCurrencyOnPanelSide();
        Assertion.assertThat("Verify the number of currency on panel side and db side is same or not",numberofCurrencyOnPanelSide ,is(numberofCurrencyOnDBSide));
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void checkComparisonThresholdValue() throws InterruptedException {
        Map<String, String> map = getParam("checkComparisonThresholdValue");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String overrideRateDefaultValue = map.get("overrideRateDefaultValue");
        String deviationThresholdDefaultValue = map.get("deviationThresholdDefaultValue");
        String comparisonThresholdDefaultValue = map.get("comparisonThresholdDefaultValue");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);
        payDash.openPaymentsPage();

        step("Go to Multi-currency dashboard");
        mDAsh.multiCurrencyDashboard();

        mDAsh.maximize();

        step("Select USD in From Currency ");
        mDAsh.selectUSDCurrency();

        step("Reset FX rate values to default");
        mDAsh.resetAllFxRateToDefault(overrideRateDefaultValue, deviationThresholdDefaultValue, comparisonThresholdDefaultValue);

        step("Check the value should be 0.05%");
        Assertion.assertThat("Threshold value is correct", mDAsh.comparingComparisonThresholdValue(comparisonThresholdDefaultValue),
                is(true));
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void checkDeviationThresholdValue() throws InterruptedException {
        Map<String, String> map = getParam("checkDeviationThresholdValue");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String overrideRateDefaultValue = map.get("overrideRateDefaultValue");
        String deviationThresholdDefaultValue = map.get("deviationThresholdDefaultValue");
        String comparisonThresholdDefaultValue = map.get("comparisonThresholdDefaultValue");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);
        payDash.openPaymentsPage();

        step("Go to Multi-currency dashboard");
        mDAsh.multiCurrencyDashboard();

        step("Select USD in From Currency ");
        mDAsh.selectUSDCurrency();

        step("Reset FX rate values to default");
        mDAsh.resetAllFxRateToDefault(overrideRateDefaultValue, deviationThresholdDefaultValue, comparisonThresholdDefaultValue);

        step("Check the value should be 3%");
        Assertion.assertThat("Threshold value is correct", mDAsh.getDeviationThresholdValue(deviationThresholdDefaultValue),
                is(true));
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void changeComparisonRateToLowerLimit() throws InterruptedException {
        Map<String, String> map = getParam("changeComparisonRateToLowerLimit");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String overrideRateDefaultValue = map.get("overrideRateDefaultValue");
        String deviationThresholdDefaultValue = map.get("deviationThresholdDefaultValue");
        String comparisonThresholdDefaultValue = map.get("comparisonThresholdDefaultValue");
        String comparisonThresholdValue = map.get("comparisonThresholdValue");;
        String verificationMessage = map.get("verificationMessage");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);
        payDash.openPaymentsPage();

        step("Go to Multi-currency dashboard");
        mDAsh.multiCurrencyDashboard();

        mDAsh.maximize();

        step("Select USD in From Currency ");
        mDAsh.selectUSDCurrency();

        step("Reset FX rate values to default");
        mDAsh.resetAllFxRateToDefault(overrideRateDefaultValue, deviationThresholdDefaultValue, comparisonThresholdDefaultValue);

        step("Change Comparison threshold Value to Below Lower Limit");
        mDAsh.changeComparisonThresholdValue(comparisonThresholdValue);
        String actualComparisonMessage = mDAsh.getVerificationMessageText("Comparison");
        Assertion.assertThat("Verify the Override verification message contains the expected text", actualComparisonMessage, containsString(verificationMessage));
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void changeComparisonRateToAboveHigherLimit() throws InterruptedException {
        Map<String, String> map = getParam("changeComparisonRateToAboveHigherLimit");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String overrideRateDefaultValue = map.get("overrideRateDefaultValue");
        String deviationThresholdDefaultValue = map.get("deviationThresholdDefaultValue");
        String comparisonThresholdDefaultValue = map.get("comparisonThresholdDefaultValue");
        String comparisonThresholdValue = map.get("comparisonThresholdValue");
        String verificationMessage = map.get("verificationMessage");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);
        payDash.openPaymentsPage();

        step("Go to Multi-currency dashboard");
        mDAsh.multiCurrencyDashboard();

        mDAsh.maximize();

        step("Select USD in From Currency ");
        mDAsh.selectUSDCurrency();

        step("Reset FX rate values to default");
        mDAsh.resetAllFxRateToDefault(overrideRateDefaultValue, deviationThresholdDefaultValue, comparisonThresholdDefaultValue);

        step("Change Comparison threshold Value to Above Higher Limit");
        mDAsh.changeComparisonThresholdValue(comparisonThresholdValue);
        String actualComparisonMessage = mDAsh.getVerificationMessageText("Comparison");
        Assertion.assertThat("Verify the Override verification message contains the expected text", actualComparisonMessage, containsString(verificationMessage));
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void changeComparisonThresholdWithinLimit() throws InterruptedException {
        Map<String, String> map = getParam("changeComparisonThresholdWithinLimit");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String actual_value = map.get("actual_value");
        double addingValue = Double.parseDouble(map.get("addingValue"));
        String original_value = "";
        String new_value = "";
        double newDoubleValue;
        String overrideRateDefaultValue = map.get("overrideRateDefaultValue");
        String deviationThresholdDefaultValue = map.get("deviationThresholdDefaultValue");
        String comparisonThresholdDefaultValue = map.get("comparisonThresholdDefaultValue");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);
        payDash.openPaymentsPage();

        step("Go to MultiCurrency dashboard");
        mDAsh.multiCurrencyDashboard();

        step("Select USD in From Currency ");
        mDAsh.selectUSDCurrency();

        step("Reset FX rate values to default");
        mDAsh.resetAllFxRateToDefault(overrideRateDefaultValue, deviationThresholdDefaultValue, comparisonThresholdDefaultValue);

        step("Get the new Value by adding the value in original value");
        original_value = mDAsh.getComparisonThresholdValue().replaceAll("[^\\d.]", "");
        newDoubleValue = Double.parseDouble(original_value) + addingValue;
        new_value = String.format("%.2f", newDoubleValue);
        mDAsh.clickClose();

        step("Change Comparison threshold Value within Limit");
        mDAsh.changeComparisonThresholdValue(new_value);

        step("Click on Override Icon");
        mDAsh.clickOnYesOverride_icon();
        sleep(2);
        Assertion.assertThat("Threshold value is correct", mDAsh.comparingComparisonThresholdValue(new_value),
                is(true));

        step("Change Comparison threshold value to default value");

        step("Change Comparison threshold Value to actual Value");
        if(original_value != actual_value){
            original_value = actual_value;
        }
        mDAsh.refreshTheWebpage();
        mDAsh.changeComparisonThresholdValue(original_value);
        mDAsh.clickOnYesOverride_icon();
        sleep(2);
        Assertion.assertThat("Comparison Threshold value is changed to actual value 0.05",
                mDAsh.comparingComparisonThresholdValue(original_value),is(true));
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void changeDeviationToAboveHigherLimit() throws InterruptedException {
        Map<String, String> map = getParam("changeDeviationToAboveHigherLimit");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String deviationThresholdValue = map.get("deviationThresholdValue");
        String overrideRateDefaultValue = map.get("overrideRateDefaultValue");
        String deviationThresholdDefaultValue = map.get("deviationThresholdDefaultValue");
        String comparisonThresholdDefaultValue = map.get("comparisonThresholdDefaultValue");
        String verificationMessage = map.get("verificationMessage");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);
        payDash.openPaymentsPage();

        step("Go to Multi-Currency dashboard");
        mDAsh.multiCurrencyDashboard();
        mDAsh.maximize();

        step("Select USD in From Currency ");
        mDAsh.selectUSDCurrency();

        step("Reset FX rate values to default");
        mDAsh.resetAllFxRateToDefault(overrideRateDefaultValue, deviationThresholdDefaultValue, comparisonThresholdDefaultValue);

        step("Change the Deviation Value to above Higher Limit");
        mDAsh.changeDeviationThresholdValue(deviationThresholdValue);
        String actualDeviationMessage = mDAsh.getVerificationMessageText("Deviation");
        Assertion.assertThat("Verify the Override verification message contains the expected text", actualDeviationMessage, containsString(verificationMessage));
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void changeOverrideValueToAboveHigherValue() throws InterruptedException {
        Map<String, String> map = getParam("changeOverrideValueToAboveHigherValue");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String override_rate = map.get("override_rate");
        String overrideRateDefaultValue = map.get("overrideRateDefaultValue");
        String deviationThresholdDefaultValue = map.get("deviationThresholdDefaultValue");
        String comparisonThresholdDefaultValue = map.get("comparisonThresholdDefaultValue");
        String out_of_range_message = map.get("verification_message_out_of_range");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);
        payDash.openPaymentsPage();

        step("Go to Multi-Currency dashboard");
        mDAsh.multiCurrencyDashboard();
        mDAsh.maximize();

        step("Select USD in From Currency ");
        mDAsh.selectUSDCurrency();

        step("Reset FX rate values to default");
        mDAsh.resetAllFxRateToDefault(overrideRateDefaultValue, deviationThresholdDefaultValue, comparisonThresholdDefaultValue);

        step("Change the Override Rate");
        mDAsh.changeOverrideRate(override_rate);
        sleep(2);
        String actualOverrideMessage = mDAsh.getVerificationMessageText("Override");
        Assertion.assertThat("Verify the Override verification message contains the expected text", actualOverrideMessage, containsString(out_of_range_message));
    }

    @Author(names = {"Saurabh"})
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void changeOverrideValueToBelowLowerValue() throws InterruptedException {
        Map<String, String> map = getParam("changeOverrideValueToBelowLowerValue");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String override_rate = map.get("override_rate");
        String overrideRateDefaultValue = map.get("overrideRateDefaultValue");
        String deviationThresholdDefaultValue = map.get("deviationThresholdDefaultValue");
        String comparisonThresholdDefaultValue = map.get("comparisonThresholdDefaultValue");
        String out_of_range_message = map.get("verification_message_out_of_range");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);
        payDash.openPaymentsPage();

        step("Go to Multi-Currency dashboard");
        mDAsh.multiCurrencyDashboard();
        mDAsh.maximize();

        step("Select USD in From Currency ");
        mDAsh.selectUSDCurrency();

        step("Reset FX rate values to default");
        mDAsh.resetAllFxRateToDefault(overrideRateDefaultValue, deviationThresholdDefaultValue, comparisonThresholdDefaultValue);

        step("Change the Override Rate & verify the verification message");
        mDAsh.changeOverrideRate(override_rate);
        sleep(2);
        String actualOverrideMessage = mDAsh.getVerificationMessageText("Override");
        Assertion.assertThat("Verify the Override verification message contains the expected text", actualOverrideMessage, containsString(out_of_range_message));
    }

    @Author(names = {"Hiten", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void inputOnlyNumbers() throws InterruptedException {
        Map<String, String> map = getParam("inputOnlyNumbers");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String overrideInput = map.get("overrideInput");
        String deviationInput = map.get("deviationInput");
        String comparisonInput = map.get("comaprisonInput");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Go to Multicurrency dashboard");
        mDAsh.multiCurrencyDashboard();

        step("Select the USD currency in From dropdown");
        mDAsh.selectUSDCurrency();

        step("Input the value in Override button");
        mDAsh.changeOverrideRateAlphabetic(overrideInput);
        String overrideInputText = mDAsh.getTextFromTextBox();
        mDAsh.clickClose();

        step("Input the value in Deviation button");
        mDAsh.changeDeviationRateAlphabetic(deviationInput);
        String deviationInputText = mDAsh.getTextFromTextBox();
        mDAsh.clickClose();

        step("Input the value in Comparison button");
        mDAsh.changeComparisonRateAlphabetic(comparisonInput);
        String comparisonInputText = mDAsh.getTextFromTextBox();
        mDAsh.clickClose();

        Assertion.assertThat("Verify the input field contains only numeric for override",  mDAsh.extractNumericValue(overrideInput), is(overrideInputText));
        Assertion.assertThat("Verify the input field contains only numeric for deviation", mDAsh.extractNumericValue(deviationInput) , is(deviationInputText));
        Assertion.assertThat("Verify the input field contains only numeric  for comparison", mDAsh.extractNumericValue(comparisonInput) , is(comparisonInputText));
    }

    @Author(names = {"Hiten", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void changeUserCurrencyToAllCurrency() throws InterruptedException {
        Map<String, String> map = getParam("changeUserCurrencyToAllCurrency");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);
        CurrencyListDashboard<?> cDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String query = "{fromCurrency:{$in:[\"EUR\", \"GBP\", \"AUD\", \"NZD\", \"USD\", \"CAD\", \"CHF\", \"JPY\", \"CNY\", \"HKD\", \"SGD\",\n" + //
                "          \"MYR\", \"THB\", \"PHP\", \"VND\", \"IDR\", \"BRL\"]},toCurrency:\"IDR\"}";
        String query2 = "{ \"toCurrency\": \"IDR\" }";

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Go to Multicurrency dashboard");
        mDAsh.multiCurrencyDashboard();

        step("Confirm search FX rate");
        mDAsh.confirmSearch();

        step("Get the number of currency on db side");
        int numberofUserCurrencyOnDBSide = cDAsh.getNumberOfCurrencyMongoDB(config,database,collectionName,query);

        step("Get the number of currency from the panel side");
        int numberofUserCurrencyOnPanelSide = cDAsh.getNumberOfCurrencyOnPanelSide();
        Assertion.assertThat("Verify the number of currency on panel side and db side is same or not",numberofUserCurrencyOnPanelSide ,is(numberofUserCurrencyOnDBSide));

        step("Now change the curency from User to All currency");
        mDAsh.clickAllCurrencyFROM();

        step("Confirm search FX rate");
        mDAsh.confirmSearch();

        step("Get the number of currency on db side");
        int numberAllCurrencyOnDBSide = cDAsh.getNumberOfCurrencyMongoDB(config,database,collectionName,query2);

        step("Get the number of currency from the panel side");
        int numberofAllCurrencyOnPanelSide = cDAsh.getNumberOfCurrencyOnPanelSide();
        Assertion.assertThat("Verify the number of currency on panel side and db side is same or not",numberofAllCurrencyOnPanelSide ,is(numberAllCurrencyOnDBSide));
    }

    @Author(names = {"Hiten", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void changeAllCurrencyToUserCurrency() throws InterruptedException {
        Map<String, String> map = getParam("changeAllCurrencyToUserCurrency");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);
        CurrencyListDashboard<?> cDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String query = "{fromCurrency:{$in:[\"EUR\", \"GBP\", \"AUD\", \"NZD\", \"USD\", \"CAD\", \"CHF\", \"JPY\", \"CNY\", \"HKD\", \"SGD\",\n" + //
                "          \"MYR\", \"THB\", \"PHP\", \"VND\", \"IDR\", \"BRL\"]},toCurrency:\"IDR\"}";
        String query2 = "{ \"toCurrency\": \"IDR\" }";

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Go to Multicurrency dashboard");
        mDAsh.multiCurrencyDashboard();

        step("Select the All currency in From field");
        mDAsh.clickAllCurrencyFROM();

        step("Confirm search FX rate");
        mDAsh.confirmSearch();

        step("Get the number of currency on db side");
        int numberAllCurrencyOnDBSide = cDAsh.getNumberOfCurrencyMongoDB(config,database,collectionName,query2);

        step("Get the number of currency from the panel side");
        int numberofAllCurrencyOnPanelSide = cDAsh.getNumberOfCurrencyOnPanelSide();
        Assertion.assertThat("Verify the number of currency on panel side and db side is same or not",numberofAllCurrencyOnPanelSide ,is(numberAllCurrencyOnDBSide));

        step("Now change the all currency to user currency");
        mDAsh.clickUserCurrency();

        step("Confirm search FX rate");
        mDAsh.confirmSearch();

        step("Get the number of currency on db side");
        int numberofUserCurrencyOnDBSide = cDAsh.getNumberOfCurrencyMongoDB(config,database,collectionName,query);

        step("Get the number of currency from the panel side");
        int numberofUserCurrencyOnPanelSide = cDAsh.getNumberOfCurrencyOnPanelSide();
        Assertion.assertThat("Verify the number of currency on panel side and db side is same or not",numberofUserCurrencyOnPanelSide ,is(numberofUserCurrencyOnDBSide));
    }

    @Author(names = {"Hiten", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyCurrencyInToFieldNotInFromField() throws InterruptedException {
        Map<String, String> map = getParam("verifyCurrencyInToFieldNotInFromField");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String query = "{toCurrency:\"IDR\"}";

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username,password);
        payDash.openPaymentsPage();

        step("Go to Multicurrency dashboard");
        mDAsh.multiCurrencyDashboard();

        step("Select the All currency in From field");
        mDAsh.clickAllCurrencyFROM();

        step("Get the number of currency on db side");
        int numberofUserCurrencyOnDBSide = mDAsh.getNumberOfCurrencyinToField(config,database,collectionName,query);

        step("Get the number of currency on panel side");
        int numberOfCurrencyonPanelSide = mDAsh.getNumberOfFromFieldMathcingCurrency();

        Assertion.assertThat("Verify there is no currency in From field on panel side", numberOfCurrencyonPanelSide, is(0));
        Assertion.assertThat("Verify there is no currency in From field on db side", numberofUserCurrencyOnDBSide, is(0));
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void changeOverrideValueToThreshold() throws InterruptedException {
        Map<String, String> map = getParam("changeOverrideValueToThreshold");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);
        CurrencyListDashboard<?> cDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String override_rate = map.get("overrideRate");
        String usd_to_idr_rate = "";
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String query = "{ \"fromCurrency\": \"USD\", \"toCurrency\": \"IDR\"  }";
        String defaultValue = map.get("defaultValue");
        String overrideRateDefaultValue = map.get("overrideRateDefaultValue");
        String deviationThresholdDefaultValue = map.get("deviationThresholdDefaultValue");
        String comparisonThresholdDefaultValue = map.get("comparisonThresholdDefaultValue");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);
        payDash.openPaymentsPage();

        step("Go to Multi-Currency dashboard");
        mDAsh.multiCurrencyDashboard();
        mDAsh.maximize();

        step("Select USD in From Currency ");
        mDAsh.selectUSDCurrency();

        step("Reset FX rate values to default");
        mDAsh.resetAllFxRateToDefault(overrideRateDefaultValue, deviationThresholdDefaultValue, comparisonThresholdDefaultValue);

        usd_to_idr_rate = mDAsh.getUSDToIDRRate().replaceAll("[\\D]", "");
        int newValue = Integer.parseInt(override_rate) + Integer.parseInt(usd_to_idr_rate);
        override_rate = String.valueOf(newValue);

        step("Change the Override Rate to Threshold");
        mDAsh.changeOverrideRate(override_rate);

        step("Verify the override rate from mongo database");
        double result  = cDAsh.getThresholdOverrideValue(config,database,collectionName,query);
        DecimalFormat resultFormat = new DecimalFormat("####");
        String override_rate_db = resultFormat.format(result);
        Assertion.assertThat("Verify the value should be", override_rate_db,is(override_rate));
        if (mDAsh.changeOverrideRateToDefaultValue(defaultValue)) mDAsh.clickOnYesOverride_icon();
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void removeOverrideRate() throws InterruptedException {
        Map<String, String> map = getParam("removeOverrideRate");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);
        CurrencyListDashboard<?> cDAsh = new CurrencyListDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String query = "{ \"fromCurrency\": \"USD\", \"toCurrency\": \"IDR\"  }";
        String defaultValue = map.get("defaultValue");
        String overrideRateDefaultValue = map.get("overrideRateDefaultValue");
        String deviationThresholdDefaultValue = map.get("deviationThresholdDefaultValue");
        String comparisonThresholdDefaultValue = map.get("comparisonThresholdDefaultValue");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);
        payDash.openPaymentsPage();

        step("Go to Multi-Currency dashboard");
        mDAsh.multiCurrencyDashboard();
        mDAsh.maximize();

        step("Select USD in From Currency");
        mDAsh.selectUSDCurrency();

        step("Reset FX rate values to default");
        mDAsh.resetAllFxRateToDefault(overrideRateDefaultValue, deviationThresholdDefaultValue, comparisonThresholdDefaultValue);

        step("Select USD in From Currency");
        mDAsh.selectUSDCurrency();

        step("Reset the override icon");
        mDAsh.resetIcon();

        step("Click on yes,Reset Icon");
        mDAsh.clickOnYesOverride_icon();

        step("Verify the override rate from mongo database");
        boolean result  = cDAsh.getOverrideValue(config,database,collectionName,query);
        Assertion.assertThat("Verify the value is null", result,is(true));

        step("Reset override to default state");
        mDAsh.resetOverrideToDefaultState(defaultValue);
    }

    @Author(names = {"Saurabh", "Hafizh"})
    @Module(name = "FXRate")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void downloadExchangeRate() throws InterruptedException {
        Map<String, String> map = getParam("downloadExchangeRate");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        MultiCurrencyDashboard<?> mDAsh = new MultiCurrencyDashboard<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String timeOutMiliSec = map.get("timeOutMiliSec");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);
        payDash.openPaymentsPage();

        step("Go to Multi-Currency dashboard");
        mDAsh.multiCurrencyDashboard();
        mDAsh.maximize();

        step("Click on Download Icon");
        mDAsh.downloadIcon();

        step("Click on yes,Download the report");
        mDAsh.clickOnYesOverride_icon();

        Assertion.assertThat("File is Downloaded", mDAsh.verifyDownloadedExportCSV(timeOutMiliSec),is(true));
    }
}
