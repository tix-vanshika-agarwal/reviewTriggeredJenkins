package com.tiket.web.dashboard.test.payment;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.api.orderDweb.LandingPageApi;
import com.tiket.api.payment.LandingPageApiV2;
import com.tiket.api.payment.SubmitCreditCardV2;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.pojo.payment.BankBinResponse;
import com.tiket.pojo.orderDweb.LandingPageResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.apiPage.HotelApi;
import com.tiket.apiPage.PaymentApi;
import com.tiket.apiPage.Unm;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.dashboard.page.payment.*;
import com.tiket.web.dashboard.page.payment.BankBinConfiguration;
import com.tiket.web.dashboard.page.payment.CustomPaymentRoutingPage;
import com.tiket.web.dashboard.page.payment.CardValidation;
import com.tiket.web.dashboard.page.payment.DefaultCardRouting;
import com.tiket.web.dashboard.page.payment.PaymentDashboardPage;
import com.tiket.web.dashboard.page.payment.PaymentInstallmentPage;

import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter.White;
import org.bson.Document;
import org.json.JSONObject;
import org.json.JSONArray;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class CardPaymentConfigurationTest extends BaseTest {
    @Author(names = {"NaufalF"})
    @Module(name = "CardPaymentConfiguration")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void testAddNewBankBin() throws InterruptedException {
        Map<String, String> param = getParam("testAddNewBankBin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        BankBinConfiguration<?> bankConfiguration = new BankBinConfiguration<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String bin = param.get("bin");
        String cardType = param.get("cardType");
        String cardName = param.get("cardName");
        String bankName = param.get("bankName");
        String bankAlias = param.get("bankAlias");
        String whiteLabel = param.get("whiteLabel");
        String country = param.get("country");

        step("Login to Admin Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);
        
        step("Open Bank Configuration Dashboard");
        bankConfiguration.openBankBinConfigPage();
        Assertion.assertThat("Is empty Bank BIN list", bankConfiguration.isEmptyBinList(), is(false));

        step("Open Add New BIN page");
        bankConfiguration.clickButton("Add New BIN");

        step("Click Cancel Button Back to BIN List");
        bankConfiguration.clickButton("Cancel");
        Assertion.assertThat("Bank BIN List opened", bankConfiguration.isBankBinConfigPageOpened(), is(true));

        step("Open Add New BIN page");
        bankConfiguration.clickButton("Add New BIN");

        step("Select Country");
        bankConfiguration.clickOnDropdownByLabel("Country");
        bankConfiguration.clickOnDropdownItem(country);

        step("Input Bank BIN");
        bankConfiguration.inputOnFieldByLabel("BIN", bin);
        Assertion.assertThat("Is disabled save button", bankConfiguration.isDisabledButton("Save"), is(true));

        step("Input Bank Name");
        bankConfiguration.inputOnFieldByLabel("Bank Name", bankName);

        step("Input Bank Alias");
        bankConfiguration.inputOnFieldByLabel("Bank Alias", bankAlias);

        step("Select Card Type");
        bankConfiguration.clickOnDropdownByLabel("Card Type");
        bankConfiguration.clickOnDropdownItem(cardType);
        Assertion.assertThat("Is disabled save button", bankConfiguration.isDisabledButton("Save"), is(true));

        step("Select White Label");
        bankConfiguration.clickOnDropdownByLabel("White Label");
        bankConfiguration.clickOnDropdownItem(whiteLabel);

        step("Input Card Name");
        bankConfiguration.inputOnFieldByLabel("Card Name", cardName);

        step("Click Save Bank BIN");
        bankConfiguration.clickButton("Save");
        bankConfiguration.clickButtonUsingJsExecutor("Submit");

        step("Verify New Bank BIN shown on top of the list");
        Assertion.assertThat("Verify snackbar message", bankConfiguration.getSnackbarText(), equalToIgnoringCase("Successfully added new BIN data."));
        String currentDateTime = CoreFunctions.convertDateFormat(CoreFunctions.getCurrentDateTime(), "yyyy/MM/dd HH:mm:ss", "dd/MM/yyyy HH:mm");
        Assertion.assertThat("BIN value shown on list is correct", bankConfiguration.getRowValueFromColumn("0", "BIN"), is(bin));
        Assertion.assertThat("Bank Name value shown on list is correct", bankConfiguration.getRowValueFromColumn("0", "Bank Name"), is(bankName));
        Assertion.assertThat("Bank Alias value shown on list is correct", bankConfiguration.getRowValueFromColumn("0", "Bank Alias"), is(bankAlias));
        Assertion.assertThat("Country value shown on list is correct", bankConfiguration.getRowValueFromColumn("0", "Country"), is(country));
        Assertion.assertThat("Card Type value shown on list is correct", bankConfiguration.getRowValueFromColumn("0", "Card Type"), is(cardType.toUpperCase()));
        Assertion.assertThat("Card Name value shown on list is correct", bankConfiguration.getRowValueFromColumn("0", "Card Name"), is(cardName));
        Assertion.assertThat("Whitelabel value shown on list is correct", bankConfiguration.getRowValueFromColumn("0", "Whitelabel"), is(whiteLabel));

        step("Verify changelog on Bank BIN List");
        bankConfiguration.openChangelogOnBinList("0");
        Assertion.assertThat("Is Changelog Date correct", bankConfiguration.getChangelogPopupBinData("Date"), containsStringIgnoringCase(currentDateTime));
        Assertion.assertThat("Is Changelog Type correct", bankConfiguration.getChangelogPopupBinData("Type"), containsStringIgnoringCase("create"));
        Assertion.assertThat("Is Changelog Actor correct", bankConfiguration.getChangelogPopupBinData("Admin"), containsStringIgnoringCase(username));
        Assertion.assertThat("Is Changelog Old Value empty", bankConfiguration.getChangelogPopupBinData("Old Value"), containsStringIgnoringCase("-"));
        String newValue = bankConfiguration.getChangelogPopupBinData("New Value");
        Assertion.assertThat("Is Changelog New Value bin correct", newValue, containsStringIgnoringCase(bin));
        Assertion.assertThat("Is Changelog New Value country correct", newValue, containsStringIgnoringCase(country));
        Assertion.assertThat("Is Changelog New Value card name correct", newValue, containsStringIgnoringCase(cardName));
        Assertion.assertThat("Is Changelog New Value whitelabel correct", newValue, containsStringIgnoringCase(whiteLabel));
        Assertion.assertThat("Is Changelog New Value issuer correct", newValue, containsStringIgnoringCase(bankName));
        Assertion.assertThat("Is Changelog New Value issuer alias correct", newValue, containsStringIgnoringCase(bankAlias));
        Assertion.assertThat("Is Changelog New Value card type correct", newValue, containsStringIgnoringCase(cardType));
        bankConfiguration.clickButtonUsingJsExecutor("Close");

        step("Verify changelog on BIN Change Log List");
        bankConfiguration.refreshPage();
        bankConfiguration.clickTab("BIN Change Log");
        Assertion.assertThat("Is Changelog Date correct", bankConfiguration.getRowValueFromColumnBinChangeLog("0", "Date"), containsStringIgnoringCase(currentDateTime));
        Assertion.assertThat("Is Changelog Type correct", bankConfiguration.getRowValueFromColumnBinChangeLog("0", "Type"), containsStringIgnoringCase("create"));
        Assertion.assertThat("Is Changelog Actor correct", bankConfiguration.getRowValueFromColumnBinChangeLog("0", "Admin"), containsStringIgnoringCase(username));
        Assertion.assertThat("Is Changelog Old Value empty", bankConfiguration.getRowValueFromColumnBinChangeLog("0", "Old Value"), containsStringIgnoringCase("-"));
        newValue = bankConfiguration.getRowValueFromColumnBinChangeLog("0", "New Value");
        Assertion.assertThat("Is Changelog New Value bin correct", newValue, containsStringIgnoringCase(bin));
        Assertion.assertThat("Is Changelog New Value country correct", newValue, containsStringIgnoringCase(country));
        Assertion.assertThat("Is Changelog New Value card name correct", newValue, containsStringIgnoringCase(cardName));
        Assertion.assertThat("Is Changelog New Value whitelabel correct", newValue, containsStringIgnoringCase(whiteLabel));
        Assertion.assertThat("Is Changelog New Value issuer correct", newValue, containsStringIgnoringCase(bankName));
        Assertion.assertThat("Is Changelog New Value issuer alias correct", newValue, containsStringIgnoringCase(bankAlias));
        Assertion.assertThat("Is Changelog New Value card type correct", newValue, containsStringIgnoringCase(cardType));

        step("Verify new added BIN on DB");
        List<Document> resultDocs = bankConfiguration.getDocumentOnCollectionPgConfig("{ \"bin\": \""+bin+"\", \"isDeleted\": 0 }");
        Assertion.assertThat("Document result must be 1", resultDocs.size(), is(1));
        Assertion.assertThat("Bank Name value on DB is correct", resultDocs.get(0).getString("issuer"), is(bankName));
        Assertion.assertThat("Bank Alias value on DB is correct", resultDocs.get(0).getString("issuerAlias"), is(bankAlias));
        Assertion.assertThat("Card Type value on DB is correct", resultDocs.get(0).getString("cardType"), equalToIgnoringCase(cardType));
        Assertion.assertThat("Card Name value on DB is correct", resultDocs.get(0).getString("cardName"), is(cardName));
        Assertion.assertThat("Whitelabel value on DB is correct", resultDocs.get(0).getBoolean("whiteLabel"), is(Boolean.parseBoolean(whiteLabel)));
        bankConfiguration.clickTab("Bank BIN");

        step("Search using filter by BIN");
        bankConfiguration.inputFilterSearchBy("BIN", bin);

        step("Delete the BIN");
        bankConfiguration.clickActionRow("0", "Delete");
        bankConfiguration.clickButtonUsingJsExecutor("Yes, delete");
        Assertion.assertThat("BIN deleted and list empty", bankConfiguration.isEmptyBinList(), is(true));

        step("Reset filter show all BIN list");
        bankConfiguration.clickButton("Reset Filter");
        Assertion.assertThat("BIN list shown", bankConfiguration.isEmptyBinList(), is(false));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "CardPaymentConfiguration")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void testEditExistingBankBin() throws InterruptedException {
        Map<String, String> param = getParam("testEditExistingBankBin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        BankBinConfiguration<?> bankConfiguration = new BankBinConfiguration<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String bin = param.get("bin");

        step("Login to Admin Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open Bank Configuration Dashboard");
        bankConfiguration.openBankBinConfigPage();
        Assertion.assertThat("Is empty Bank BIN list", bankConfiguration.isEmptyBinList(), is(false));

        step("Search by BIN then edit");
        bankConfiguration.inputFilterSearchBy("BIN", bin);
        String bankName = bankConfiguration.getRowValueFromColumn("0", "Bank Name");
        String bankNameEdit = bankName.replaceAll("\\S+$", randomName());
        String bankAlias = bankConfiguration.getRowValueFromColumn("0", "Bank Alias");
        String bankAliasEdit = bankAlias.replaceAll("\\S+$", randomName());
        String cardType = CoreFunctions.titleize(bankConfiguration.getRowValueFromColumn("0", "Card Type").toLowerCase());
        String cardTypeEdit = cardType.equalsIgnoreCase("credit") ? "Debit" : "Credit";
        String country = bankConfiguration.getRowValueFromColumn("0", "Country");
        String cardName = bankConfiguration.getRowValueFromColumn("0", "Card Name");
        String whitelabel = bankConfiguration.getRowValueFromColumn("0", "Whitelabel");
        bankConfiguration.clickActionRow("0", "Edit");

        step("Bank BIN can't be edited");
        Assertion.assertThat("BIN value is correct", bankConfiguration.getInputValueOnFieldByLabel("BIN"), is(bin));
        Assertion.assertThat("BIN field is disabled", bankConfiguration.isDisabledInputField("BIN"), is(true));

        step("Edit Bank Name and Bank Alias");
        bankConfiguration.inputOnFieldByLabel("Bank Name", bankNameEdit);
        bankConfiguration.inputOnFieldByLabel("Bank Alias", bankAliasEdit);

        step("Edit Card Type from "+cardType+" to "+cardTypeEdit);
        bankConfiguration.clickOnDropdownByLabel("Card Type");
        bankConfiguration.clickOnDropdownItem(cardTypeEdit);

        step("Click Save Bank BIN");
        bankConfiguration.clickButton("Save");
        bankConfiguration.clickButtonUsingJsExecutor("Submit");

        step("Verify edited Bank BIN shown on top of the list");
        Assertion.assertThat("Verify snackbar message", bankConfiguration.getSnackbarText(), equalToIgnoringCase("Successfully updated BIN data."));
        String currentDateTime = CoreFunctions.convertDateFormat(CoreFunctions.getCurrentDateTime(), "yyyy/MM/dd HH:mm:ss", "dd/MM/yyyy HH:mm");
        Assertion.assertThat("BIN value shown on list is correct", bankConfiguration.getRowValueFromColumn("0", "BIN"), is(bin));
        Assertion.assertThat("Bank Name value shown on list is correct", bankConfiguration.getRowValueFromColumn("0", "Bank Name"), is(bankNameEdit));
        Assertion.assertThat("Bank Alias value shown on list is correct", bankConfiguration.getRowValueFromColumn("0", "Bank Alias"), is(bankAliasEdit));
        Assertion.assertThat("Country value shown on list is correct", bankConfiguration.getRowValueFromColumn("0", "Country"), is(country));
        Assertion.assertThat("Card Type value shown on list is correct", bankConfiguration.getRowValueFromColumn("0", "Card Type"), is(cardTypeEdit.toUpperCase()));
        Assertion.assertThat("Card Name value shown on list is correct", bankConfiguration.getRowValueFromColumn("0", "Card Name"), is(cardName));
        Assertion.assertThat("Whitelabel value shown on list is correct", bankConfiguration.getRowValueFromColumn("0", "Whitelabel"), is(whitelabel));

        step("Verify edited bank BIN on DB");
        List<Document> resultDocs = bankConfiguration.getDocumentOnCollectionPgConfig("{ \"bin\": \""+bin+"\", \"isDeleted\": 0 }");
        Assertion.assertThat("Document result must be 1", resultDocs.size(), is(1));
        Assertion.assertThat("Bank Name value on DB is correct", resultDocs.get(0).getString("issuer"), is(bankNameEdit));
        Assertion.assertThat("Bank Alias value on DB is correct", resultDocs.get(0).getString("issuerAlias"), is(bankAliasEdit));
        Assertion.assertThat("Card Type value on DB is correct", resultDocs.get(0).getString("cardType"), equalToIgnoringCase(cardTypeEdit));
        Assertion.assertThat("Card Name value on DB is correct", resultDocs.get(0).getString("cardName"), is(cardName));
        Assertion.assertThat("Whitelabel value on DB is correct", resultDocs.get(0).getBoolean("whiteLabel"), is(Boolean.parseBoolean(whitelabel)));

        step("Verify changelog on Bank BIN List");
        bankConfiguration.openChangelogOnBinList("0");
        Assertion.assertThat("Is Changelog Date correct", bankConfiguration.getChangelogPopupBinData("Date"), containsStringIgnoringCase(currentDateTime));
        Assertion.assertThat("Is Changelog Type correct", bankConfiguration.getChangelogPopupBinData("Type"), containsStringIgnoringCase("update"));
        Assertion.assertThat("Is Changelog Actor correct", bankConfiguration.getChangelogPopupBinData("Admin"), containsStringIgnoringCase(username));
        String oldValue = bankConfiguration.getChangelogPopupBinData("Old Value");
        Assertion.assertThat("Is Changelog Old Value bin still same", oldValue, containsStringIgnoringCase(bin));
        Assertion.assertThat("Is Changelog Old Value country still same", oldValue, containsStringIgnoringCase(country));
        Assertion.assertThat("Is Changelog Old Value card name still same", oldValue, containsStringIgnoringCase(cardName));
        Assertion.assertThat("Is Changelog Old Value whitelabel still same", oldValue, containsStringIgnoringCase(whitelabel));
        Assertion.assertThat("Is Changelog Old Value issuer correct", oldValue, containsStringIgnoringCase(bankName));
        Assertion.assertThat("Is Changelog Old Value issuer alias correct", oldValue, containsStringIgnoringCase(bankAlias));
        Assertion.assertThat("Is Changelog Old Value card type correct", oldValue, containsStringIgnoringCase(cardType.toLowerCase()));
        String newValue = bankConfiguration.getChangelogPopupBinData("New Value");
        Assertion.assertThat("Is Changelog New Value bin still same", newValue, containsStringIgnoringCase(bin));
        Assertion.assertThat("Is Changelog New Value country still same", newValue, containsStringIgnoringCase(country));
        Assertion.assertThat("Is Changelog New Value card name still same", newValue, containsStringIgnoringCase(cardName));
        Assertion.assertThat("Is Changelog New Value whitelabel still same", newValue, containsStringIgnoringCase(whitelabel));
        Assertion.assertThat("Is Changelog New Value issuer correct", newValue, containsStringIgnoringCase(bankNameEdit));
        Assertion.assertThat("Is Changelog New Value issuer alias correct", newValue, containsStringIgnoringCase(bankAliasEdit));
        Assertion.assertThat("Is Changelog New Value card type correct", newValue, containsStringIgnoringCase(cardTypeEdit));
        bankConfiguration.clickButtonUsingJsExecutor("Close");

        step("Verify changelog on BIN Change Log List");
        bankConfiguration.refreshPage();
        bankConfiguration.clickTab("BIN Change Log");
        Assertion.assertThat("Is Changelog Date correct", bankConfiguration.getRowValueFromColumnBinChangeLog("0", "Date"), containsStringIgnoringCase(currentDateTime));
        Assertion.assertThat("Is Changelog Type correct", bankConfiguration.getRowValueFromColumnBinChangeLog("0", "Type"), containsStringIgnoringCase("update"));
        Assertion.assertThat("Is Changelog Actor correct", bankConfiguration.getRowValueFromColumnBinChangeLog("0", "Admin"), containsStringIgnoringCase(username));
        oldValue = bankConfiguration.getRowValueFromColumnBinChangeLog("0", "Old Value");
        Assertion.assertThat("Is Changelog Old Value bin still same", oldValue, containsStringIgnoringCase(bin));
        Assertion.assertThat("Is Changelog Old Value country still same", oldValue, containsStringIgnoringCase(country));
        Assertion.assertThat("Is Changelog Old Value card name still same", oldValue, containsStringIgnoringCase(cardName));
        Assertion.assertThat("Is Changelog Old Value whitelabel still same", oldValue, containsStringIgnoringCase(whitelabel));
        Assertion.assertThat("Is Changelog Old Value issuer correct", oldValue, containsStringIgnoringCase(bankName));
        Assertion.assertThat("Is Changelog Old Value issuer alias correct", oldValue, containsStringIgnoringCase(bankAlias));
        Assertion.assertThat("Is Changelog Old Value card type correct", oldValue, containsStringIgnoringCase(cardType.toLowerCase()));
        newValue = bankConfiguration.getRowValueFromColumnBinChangeLog("0", "New Value");
        Assertion.assertThat("Is Changelog New Value bin still same", newValue, containsStringIgnoringCase(bin));
        Assertion.assertThat("Is Changelog New Value country still same", newValue, containsStringIgnoringCase(country));
        Assertion.assertThat("Is Changelog New Value card name still same", newValue, containsStringIgnoringCase(cardName));
        Assertion.assertThat("Is Changelog New Value whitelabel still same", newValue, containsStringIgnoringCase(whitelabel));
        Assertion.assertThat("Is Changelog New Value issuer correct", newValue, containsStringIgnoringCase(bankNameEdit));
        Assertion.assertThat("Is Changelog New Value issuer alias correct", newValue, containsStringIgnoringCase(bankAliasEdit));
        Assertion.assertThat("Is Changelog New Value card type correct", newValue, containsStringIgnoringCase(cardTypeEdit));
    }
    
    @Author(names = {"NaufalF"})
    @Module(name = "CardPaymentConfiguration")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testConfigDefaultPGDataProvider")
    public void testConfigDefaultPG(Map<String, String> param) throws InterruptedException {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        DefaultCardRouting<?> defaultCardRouting = new DefaultCardRouting<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String[] listPg = param.get("listPg").split("-");
        String latestRoutedPg;
        String latestUpdate;
        String currentDateTime;

        step("Login to Admin Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open Default Card Routing dashboard");
        defaultCardRouting.openDefaultCardRoutingPage(3);

        step("Get current default PG "+currency);
        String defaultPg = defaultCardRouting.getRoutedPgByColumn("Default "+currency);

        for (String pg : listPg) {
            if (!pg.equalsIgnoreCase(defaultPg)){
                step("Verify set default PG to "+pg);
                if (defaultCardRouting.getColumnValueByPg(pg, "Status").equalsIgnoreCase("active")) {
                    Assertion.assertThat("Is snackbar message correct", defaultCardRouting.routePg(pg, "Default "+currency), equalToIgnoringCase("Successfully set "+pg+" as the default routing for "+currency+"."));
                    currentDateTime = CoreFunctions.convertDateFormat(CoreFunctions.getCurrentDateTime(), "yyyy/MM/dd HH:mm:ss", "dd/MM/yyyy HH:mm");
                    Assertion.assertThat("Routed pg is "+pg, defaultCardRouting.getRoutedPgByColumn("Default "+currency), is(pg));
                    Assertion.assertThat("Latest update date time is correct", defaultCardRouting.getColumnValueByPg(pg, "Latest Update"), containsString(currentDateTime));
                    Assertion.assertThat("Latest update actor is correct", defaultCardRouting.getColumnValueByPg(pg, "Latest Update"), containsString(username));

                    step("Verify Data on DB");
                    String field = currency.equalsIgnoreCase("IDR") ? "defaultPG" : "defaultNonIdrPg";
                    List<Document> listDocument = defaultCardRouting.getDocumentOnCollectionPgConfig("{ \""+field+"\": "+true+"}");
                    Assertion.assertThat("Document result must be 1", listDocument.size(), is(1));
                    Assertion.assertThat("Routed PG value is correct", listDocument.get(0).getString("paymentGateway"), equalToIgnoringCase(pg));
                } else {
                    latestRoutedPg = defaultCardRouting.getRoutedPgByColumn("Default "+currency);
                    latestUpdate = defaultCardRouting.getColumnValueByPg(pg, "Latest Update");
                    defaultCardRouting.clickOnInputColumnByPg(pg, "Default "+currency);
                    Assertion.assertThat("show modal body can't activate", defaultCardRouting.getModalBodyContent(), equalToIgnoringCase("Can't set "+pg+" as default for "+currency));
                    defaultCardRouting.clickOnModalFooterButton("OK");
                    Assertion.assertThat("Inactive PG can't toggled on", defaultCardRouting.getRoutedPgByColumn("Default "+currency), is(latestRoutedPg));
                    Assertion.assertThat("Latest update not changed", defaultCardRouting.getColumnValueByPg(pg, "Latest Update"), containsString(latestUpdate));
                }
            }
        }

        step("Route back to the default PG");
        defaultCardRouting.routePg(defaultPg, "Default "+currency);
    }

    @DataProvider(name = "testConfigDefaultPGDataProvider")
    public Object[][] testConfigDefaultPGDataProvider() {
        return getObjectParams("testConfigDefaultPG");
    }

    @Author(names = {"NaufalF"})
    @Module(name = "CardPaymentConfiguration")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void testPGActivation() throws InterruptedException {
        Map<String, String> param = getParam("testPGActivation");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        DefaultCardRouting<?> defaultCardRouting = new DefaultCardRouting<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String[] listPg = param.get("listPg").split("-");
        String pgStatus;
        String action;
        String currentDateTime;
        String latestUpdate;

        step("Login to Admin Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open Default Card Routing dashboard");
        defaultCardRouting.openDefaultCardRoutingPage(3);
        String defaultIdr = defaultCardRouting.getRoutedPgByColumn("Default IDR");
        String defaultNonIdr = defaultCardRouting.getRoutedPgByColumn("Default Non IDR");

        for (String pg : listPg) {
            if (pg.equalsIgnoreCase(defaultIdr) || pg.equalsIgnoreCase(defaultNonIdr)) {
                step("Toggle Deactivate Routed PG -> "+pg);
                latestUpdate = defaultCardRouting.getColumnValueByPg(pg, "Latest Update");
                defaultCardRouting.clickOnInputColumnByPg(pg, "Gateway Activation");
                Assertion.assertThat("Can't deactivate routed PG", defaultCardRouting.getModalBodyContent(), equalToIgnoringCase("Can't deactivate "+pg));
                defaultCardRouting.clickOnModalFooterButton("OK");
                Assertion.assertThat("Latest update not changed", defaultCardRouting.getColumnValueByPg(pg, "Latest Update"), containsString(latestUpdate));
            } else {
                pgStatus = defaultCardRouting.getColumnValueByPg(pg, "Status");
                action = pgStatus.equalsIgnoreCase("active") ? "Deactivate" : "Activate";
                step("Toggle "+action+" PG -> "+pg);
                Assertion.assertThat("Is snackbar message correct", defaultCardRouting.togglePgActivation(pg, action), equalToIgnoringCase("Successfully "+action.toLowerCase()+" "+pg+"."));
                Assertion.assertThat("Verify PG status", defaultCardRouting.getColumnValueByPg(pg, "Status"), equalToIgnoringCase(pgStatus.equalsIgnoreCase("active") ? "Inactive" : "Active"));

                step("Verify Data on DB");
                List<Document> listDocument = defaultCardRouting.getDocumentOnCollectionPgConfig("{ \"paymentGateway\": \""+pg.toLowerCase()+"\"}");
                Assertion.assertThat("Document result must be 1", listDocument.size(), is(1));
                Assertion.assertThat("PG status is correct", listDocument.get(0).getBoolean("active"), is(!pgStatus.equalsIgnoreCase("active")));

                pgStatus = defaultCardRouting.getColumnValueByPg(pg, "Status");
                action = pgStatus.equalsIgnoreCase("active") ? "Deactivate" : "Activate";
                step("Toggle "+action+" back PG -> "+pg);
                Assertion.assertThat("Is snackbar message correct", defaultCardRouting.togglePgActivation(pg, action), equalToIgnoringCase("Successfully "+action.toLowerCase()+" "+pg+"."));
                currentDateTime = CoreFunctions.convertDateFormat(CoreFunctions.getCurrentDateTime(), "yyyy/MM/dd HH:mm:ss", "dd/MM/yyyy HH:mm");
                Assertion.assertThat("Verify PG status", defaultCardRouting.getColumnValueByPg(pg, "Status"), equalToIgnoringCase(pgStatus.equalsIgnoreCase("active") ? "Inactive" : "Active"));
                Assertion.assertThat("Latest update date time is correct", defaultCardRouting.getColumnValueByPg(pg, "Latest Update"), containsString(currentDateTime));
                Assertion.assertThat("Latest update actor is correct", defaultCardRouting.getColumnValueByPg(pg, "Latest Update"), containsString(username));
            }
        }
    }
    
    @Author(names = {"Saurabh","NaufalF"})
    @Module(name = "CardPaymentConfiguration")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyFiltersOnInstallmentPage() throws InterruptedException {
        Map<String, String> map = getParam("verifyFiltersOnInstallmentPage");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        PaymentInstallmentPage<?> installmentPage = new PaymentInstallmentPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String bank = map.get("bank");
        String tenor = map.get("tenor");
        String tenorItem = tenor+" Months";
        String orderBy = map.get("orderBy");
        String randomMinDate = map.get("randomMinDate");

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);

        step("Open the Installment Page");
        installmentPage.openPaymentInstallmentPage();

        step("Open Installment Report Page");
        installmentPage.clickButton("Installment Report");
        Assertion.assertThat("Is installment report page opened", installmentPage.isPaymentInstallmentReportPageOpened(), is(true));
        Assertion.assertThat("Is installment report empty", installmentPage.isEmptyInstallmentReport(), is(true));

        step("Select filter start dates");
        String startDate = CoreFunctions.generateRandomDateMinus(randomMinDate, "d MMMM yyyy");
        installmentPage.filterDateInstallmentReport("Start Date", startDate, Integer.parseInt(randomMinDate.split("-")[1]));

        step("Select filter bank name");
        installmentPage.filterDropdownInstallmentReport("Bank", bank);

        step("Select filter tenor");
        installmentPage.filterDropdownInstallmentReport("Tenor", tenorItem);

        step("Select filter order by");
        installmentPage.filterDropdownInstallmentReport("Order By", orderBy);

        step("Click on Search");
        installmentPage.clickButton("Search");

        step("Verify data on the list");
        String endDate = CoreFunctions.getCurrentDate("dd-MM-yyyy");
        String firstRowDate = installmentPage.getRowValueFromColumnReport("0", "Date").split(" ")[0];
        Assertion.assertThat("Is first row date equal or less than end date", CoreFunctions.compareDate(firstRowDate, endDate, "dd-MM-yyyy"), lessThanOrEqualTo(0));
        Assertion.assertThat("Is first row bank correct", installmentPage.getRowValueFromColumnReport("0", "Bank"), is(bank));
        Assertion.assertThat("Is first row tenor correct", installmentPage.getRowValueFromColumnReport("0", "Tenor"), is(tenor));

        installmentPage.selectLastPageInstallmentReport();
        String lastRowDate = installmentPage.getRowValueFromColumnReport(String.valueOf(installmentPage.getRowCountOnTable()-1), "Date").split(" ")[0];
        Assertion.assertThat("Is last row date equal or more than start date", CoreFunctions.compareDate(lastRowDate, CoreFunctions.convertDateFormat(startDate, "d MMMM yyyy", "dd-MM-yyyy"), "dd-MM-yyyy"), greaterThanOrEqualTo(0));
        Assertion.assertThat("Is "+orderBy+" sorted descending", CoreFunctions.compareDate(lastRowDate, firstRowDate, "dd-MM-yyyy"), lessThanOrEqualTo(0));
        Assertion.assertThat("Is last row bank correct", installmentPage.getRowValueFromColumnReport(String.valueOf(installmentPage.getRowCountOnTable()-1), "Bank"), is(bank));
        Assertion.assertThat("Is last row tenor correct", installmentPage.getRowValueFromColumnReport(String.valueOf(installmentPage.getRowCountOnTable()-1), "Tenor"), is(tenor));
        
        step("Verify summary box");
        Assertion.assertThat("Summary Total Invoice not 0", installmentPage.getCardSummaryData("Total Invoice"), is(not("0")));
        Assertion.assertThat("Summary Total Base Price not 0", installmentPage.getCardSummaryData("Total Base Price"), is(not("0")));
        Assertion.assertThat("Summary Total FDR not 0", installmentPage.getCardSummaryData("Total FDR"), is(not("0")));
        Assertion.assertThat("Summary Total Payment Cost not 0", installmentPage.getCardSummaryData("Total Payment Cost"), is(not("0")));
        Assertion.assertThat("Summary Installment & FDR not 0", installmentPage.getCardSummaryData("Installment & FDR"), is(not("0")));
        Assertion.assertThat("Summary Total Profit not 0", installmentPage.getCardSummaryData("Total Profit"), is(not("0")));
        Assertion.assertThat("Summary Total Bank Invoice not 0", installmentPage.getCardSummaryData("Total Bank Invoice"), is(not("0")));
        Assertion.assertThat("Summary Total Data not 0", installmentPage.getCardSummaryData("Total Data"), is(not("0")));
    }

    @Author(names = {"Saurabh","NaufalF"})
    @Module(name = "CardPaymentConfiguration")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(priority = 0)
    public void createInstallment() throws InterruptedException {
        Map<String, String> map = getParam("createInstallment");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        PaymentInstallmentPage<?> installmentPage = new PaymentInstallmentPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        LocalDate localDate = LocalDate.now();
        String bankName = map.get("bankName") + localDate;
        String cardName = map.get("cardName") + localDate;
        String cardType = map.get("cardType");
        String binValue = map.get("binValue");
        String recipientEmail = map.get("recipientEmail");
        String emailAction = map.get("emailAction");
        String promoTitle = map.get("promoTitle") + localDate;
        String promoStatus = map.get("promoStatus");
        String addDuration = map.get("addDuration");
        String financeValue = map.get("financeValue");
        String bankValue = map.get("bankValue");
        String minimumPayment = map.get("minimumPayment");
        String reconcileRate = map.get("reconcileRate");
        int maxIter = 10;

        String collectionName = map.get("collectionName");
        String query = "{\"bankName\"" + ":" + "'"+ bankName + "'" + "," + "\"isDeleted\":0}";

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);

        step("Open the Installment Page");
        installmentPage.openPaymentInstallmentPage();

        step("Open Create-Installment-Page");
        installmentPage.clickOnCreateInstallment();

        step("Enter the Bank Name");
        installmentPage.setBankName(bankName);

        step("Enter the Card Name");
        installmentPage.setCardName(cardName);

        step("Select Card Type");
        installmentPage.selectCardType(cardType);

        step("Select Bin value");
        installmentPage.setBin(binValue);

        step("Enter the Recipient Name ");
        installmentPage.selectRecipientEmail(recipientEmail);

        step("Select the Email Action");
        installmentPage.selectEmailAction(emailAction);

        step("Select Promo Title");
        installmentPage.setPromoTitle(promoTitle);

        step("Select start date");
        installmentPage.selectDateOnCalendar(true, String.valueOf(localDate.getDayOfMonth()), CoreFunctions.getMonthName(localDate.getMonthValue(), "en"), String.valueOf(localDate.getYear()), maxIter);

        step("Select end date");
        LocalDate endDate = localDate.plusDays(3);
        installmentPage.selectDateOnCalendar(false, String.valueOf(endDate.getDayOfMonth()), CoreFunctions.getMonthName(endDate.getMonthValue(), "en"), String.valueOf(endDate.getYear()), maxIter);

        step("Select Promo Status");
        installmentPage.selectPromoStatus(promoStatus);

        step("Click on Add Duration");
        installmentPage.clickAddDuration();

        step("Add the Installment Duration");
        installmentPage.setAddDuration(addDuration);

        step("Add the Finance Percentage");
        installmentPage.setForFinanceValue(financeValue);

        step("Add the Bank Percentage");
        installmentPage.setForBankValue(bankValue);

        step("Add Minimum Payment Value");
        installmentPage.setMinimumPayment(minimumPayment);

        step("Add Reconcile Rate");
        installmentPage.setReconcileRate(reconcileRate);

        step("Click on Save");
        installmentPage.saveInstallment("Submit");

        step("Search the created installment");
        installmentPage.filterSearchByInstallmentList("Bank Name", bankName);
        installmentPage.clickButton("Search");

        step("Check the Installment is created");
        Assertion.assertThat("",installmentPage.getRowValueFromColumn("0", "Bank Name").replace("\n", ""), is(bankName));

        step("Check the value in Db");
        String bank_name = installmentPage.getBankName(config,database,collectionName,query);
        Assertion.assertThat("Bank Name should be same", bank_name, is(bankName));
    }

    @Author(names = {"Saurabh","NaufalF"})
    @Module(name = "CardPaymentConfiguration")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(priority = 2)
    public void deleteInstallment() throws InterruptedException {
        Map<String, String> map = getParam("deleteInstallment");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        PaymentInstallmentPage<?> installmentPage = new PaymentInstallmentPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        LocalDate localDate = LocalDate.now();
        String bankName = map.get("bankName");
        String newBankName = "Edit"+bankName+localDate;
        String query = "{\"bankName\"" + ":" + "'"+ newBankName + "'" + "," + "\"isDeleted\":1}";

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);

        step("Open the Installment Page");
        installmentPage.openPaymentInstallmentPage();

        step("Search the created installment");
        installmentPage.filterSearchByInstallmentList("Bank Name", newBankName);
        installmentPage.clickButton("Search");

        step("Click on the Delete Icon");
        installmentPage.clickOnDeleteIcon();

        step("Search the deleted installment");
        installmentPage.filterSearchByInstallmentList("Bank Name", newBankName);
        installmentPage.clickButton("Search");

        step("Installment should not shown");
        Assertion.assertThat("Empty installment list", installmentPage.isEmptyInstallmentReport(), is(true));

        step("Check the value in Db");
        Assertion.assertThat("Bank Name should be same", installmentPage.getBankName(config,database,collectionName,query), is(newBankName));
    }

    @Author(names = {"Saurabh","NaufalF"})
    @Module(name = "CardPaymentConfiguration")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(priority = 1)
    public void editInstallment() throws InterruptedException {
        Map<String, String> map = getParam("editInstallment");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> payDash = new PaymentDashboardPage<>(session);
        PaymentInstallmentPage<?> installmentPage = new PaymentInstallmentPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        LocalDate localDate = LocalDate.now();
        String bankName = map.get("bankName");
        String newBankName = "Edit"+bankName+localDate;
        String cardName = map.get("cardName")+localDate;
        String addDuration = map.get("addDuration");
        String financeValue = map.get("financeValue");
        String bankValue = map.get("bankValue");
        String minimumPayment = map.get("minimumPayment");
        String reconcileRate = map.get("reconcileRate");

        String collectionName = map.get("collectionName");
        String query = "{\"bankName\"" + ":" + "'"+ newBankName + "'" + "," + "\"isDeleted\":0}";

        step("Login into payment dashboard");
        payDash.goToAdminCarePage(username, password);

        step("Open the Installment Page");
        installmentPage.openPaymentInstallmentPage();

        step("Search the bankName");
        installmentPage.filterSearchByInstallmentList("Bank Name", bankName);
        installmentPage.clickButton("Search");

        step("Click on the Edit Icon");
        installmentPage.clickOnEditIcon();

        step("Enter the Bank Name");
        installmentPage.setBankName(newBankName);

        step("Enter the Card Name");
        installmentPage.setCardName(cardName);

        step("Click on Add Duration");
        installmentPage.scrollToConfigurationLog();
        installmentPage.clickAddDuration();

        step("Add the Installment Duration");
        installmentPage.setAddDuration(addDuration);

        step("Add the Finance Percentage");
        installmentPage.setForFinanceValue(financeValue);

        step("Add the Bank Percentage");
        installmentPage.setForBankValue(bankValue);

        step("Add Minimum Payment Value");
        installmentPage.setMinimumPayment(minimumPayment);

        step("Add Reconcile Rate");
        installmentPage.setReconcileRate(reconcileRate);

        step("click On the save Installment Icon");
        installmentPage.saveInstallment("Save Changes");

        step("Search the edited installment");
        installmentPage.filterSearchByInstallmentList("Bank Name", newBankName);
        installmentPage.clickButton("Search");

        step("Check the Installment is edited");
        Assertion.assertThat("",installmentPage.getRowValueFromColumn("0", "Bank Name").replace("\n", ""), is(newBankName));
        
        step("Check the value in Db");
        Map<String, String> mongoData = installmentPage.getInstallmentDetails(config, database, collectionName, query);
        Assertion.assertThat("Bank Name should be same after edit", mongoData.get("bankName"), is(newBankName));
    }

    @Author(names = {"NaufalF", "Hafizh"})
    @Module(name = "CardPaymentConfiguration")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void testExportInstallmentReport() throws InterruptedException, FileNotFoundException {
        Map<String, String> param = getParam("testExportInstallmentReport");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentInstallmentPage<?> paymentInstallmentPage = new PaymentInstallmentPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String randomMinDate = param.get("randomMinDate");
        String columnName = param.get("columnName");

        step("Login to Admin Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open Installment Report Configuration Dashboard");
        paymentInstallmentPage.openPaymentInstallmentPage();
        paymentInstallmentPage.clickButton("Installment Report");
        boolean isInstallmentReportEmpty = paymentInstallmentPage.isEmptyInstallmentReport();
        Assertion.assertThat("Is installment report page opened", paymentInstallmentPage.isPaymentInstallmentReportPageOpened(), is(true));
        Assertion.assertThat("Is installment report empty", isInstallmentReportEmpty, is(true));

        step("Select filter start date");
        String startDate = CoreFunctions.generateRandomDateMinus(randomMinDate, "d MMMM yyyy");
        String todayFormatted = CoreFunctions.getCurrentDate("d MMMM yyyy");
        String startDateConverted = CoreFunctions.convertDateFormat(startDate, "d MMMM yyyy", "dd-MM-yyyy");
        String todayConverted = CoreFunctions.getCurrentDate("dd-MM-yyyy");
        if (CoreFunctions.compareDate(startDateConverted, todayConverted, "dd-MM-yyyy") > 0) {
            startDate = todayFormatted;
        }
        paymentInstallmentPage.filterDateInstallmentReport("Start Date", startDate, Math.abs(Integer.parseInt(randomMinDate.split("_")[1])));

        step("Select filter order by");
        paymentInstallmentPage.filterDropdownInstallmentReport("Order By", "Transaction Date");

        step("Search installment report");
        paymentInstallmentPage.clickButton("Search");

        if (isInstallmentReportEmpty) {
            step("Verify installment report is empty after search");
            Assertion.assertThat("Installment report is empty after applying filters",
                    paymentInstallmentPage.isEmptyInstallmentReport(), is(true));
        } else {
            step("Verify data correctly shown");
            String endDate = CoreFunctions.getCurrentDate("dd-MM-yyyy");
            String firstRowDate = paymentInstallmentPage.getRowValueFromColumnReport("0", "Date").split(" ")[0];
            Assertion.assertThat("Is first row date equal or less than end date", CoreFunctions.compareDate(firstRowDate, endDate, "dd-MM-yyyy"), lessThanOrEqualTo(0));
            paymentInstallmentPage.selectLastPageInstallmentReport();
            String lastRowDate = paymentInstallmentPage.getRowValueFromColumnReport(String.valueOf(paymentInstallmentPage.getRowCountOnTable()-1), "Date").split(" ")[0];
            Assertion.assertThat("Is last row date equal or more than start date", CoreFunctions.compareDate(lastRowDate, CoreFunctions.convertDateFormat(startDate, "d MMMM yyyy", "dd-MM-yyyy"), "dd-MM-yyyy"), greaterThanOrEqualTo(0));
            Assertion.assertThat("First row date must be greater than last row date", CoreFunctions.compareDate(firstRowDate, lastRowDate, "dd-MM-yyyy"), greaterThanOrEqualTo(0));

            step("Download installment report data");
            paymentInstallmentPage.clickButton("Export to Excel");
            File file = CoreFunctions.getDownloadedFileMacOS("installment_report");
            Assertion.assertThat("Is file downloaded", file.getName(), containsString("installment_report"));

            step("Verify downloaded CSV content");
            List<List<String>> csvContent = CoreFunctions.getCSVContent(file);
            int rowSize = csvContent.size();
            Assertion.assertThat("Is total CSV data same as panel", rowSize-2, is(paymentInstallmentPage.getTotalDataInstallmentReport()));
            CoreFunctions.deleteFile(file.getAbsolutePath());
        }

    }

    @Author(names = {"Nasha"})
    @Module(name = "CardPaymentConfiguration")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyTheWholeFlowOfBINConfiguration() throws InterruptedException {
        Map<String, String> param = getParam("verifyTheWholeFlowOfBINConfiguration");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        BankBinConfiguration<?> bankBinConfigurationPage = new BankBinConfiguration<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String binEightDigit = param.get("binEightDigit");
        String binSixDigit = param.get("binSixDigit");
        String cardType = param.get("cardType");
        String cardSchemeEightDigit = param.get("cardSchemeEightDigit");
        String cardSchemeSixDigit = param.get("cardSchemeSixDigit");
        String cardName = param.get("cardName");
        String bankName = param.get("bankName");
        String bankAlias = param.get("bankAlias");
        String whiteLabel = param.get("whiteLabel");
        String country = param.get("country");
        String countryApiCode = param.get("countryApiCode");
        String currency = param.get("currency");
        String deviceId = CoreFunctions.generateRandomAlphanumeric(8);
        String lang = param.get("lang");
        String countryCode = param.get("countryCode");

        step("Login into admin dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open bin configuration");
        bankBinConfigurationPage.openBankBinConfigPage();
        Assertion.assertThat("Is bin configuration page opened", bankBinConfigurationPage.isBankBinConfigPageOpened(), is(true));

        step("flow for 8 Digit BIN");
        step("Search using filter by BIN");
        bankBinConfigurationPage.inputSearchByBIN(binEightDigit);
        bankBinConfigurationPage.clickButton("Search");

        if (!bankBinConfigurationPage.isEmptyBinList()) {
            step("Delete the BIN because there is existing BIN");
            bankBinConfigurationPage.clickActionRowThenScroll("0", "Delete");
            bankBinConfigurationPage.clickButtonUsingJsExecutor("Yes, delete");
            Assertion.assertThat("BIN deleted and list empty", bankBinConfigurationPage.isEmptyBinList(), is(true));
        }

        step("Open Add New BIN page");
        bankBinConfigurationPage.clickButton("Add New BIN");

        step("Input Bank Name");
        bankBinConfigurationPage.inputOnFieldByLabel("Bank Name", bankName);

        step("Select Country");
        bankBinConfigurationPage.clickOnDropdownByLabel("Country");
        bankBinConfigurationPage.clickOnDropdownItem(country);

        step("Input Bank Alias");
        bankBinConfigurationPage.inputOnFieldByLabel("Bank Alias", bankAlias);

        step("Select BIN Type");
        bankBinConfigurationPage.clickOnDropdownByLabel("BIN Type");
        bankBinConfigurationPage.clickOnDropdownItem("8_Digit");

        step("Input Bank BIN Range Min");
        bankBinConfigurationPage.inputOnFieldByLabel("BIN Range Min", binEightDigit);

        step("Input Bank BIN Range Max");
        bankBinConfigurationPage.inputOnFieldByLabel("BIN Range Max", binEightDigit);

        step("Select Card Type");
        bankBinConfigurationPage.clickOnDropdownByLabel("Card Type");
        bankBinConfigurationPage.clickOnDropdownItem(cardType);

        step("Select White Label");
        bankBinConfigurationPage.clickOnDropdownByLabel("White Label");
        bankBinConfigurationPage.clickOnDropdownItem(whiteLabel);

        step("Input Card Name");
        bankBinConfigurationPage.inputOnFieldByLabel("Card Name", cardName);

        step("Select Card Scheme");
        bankBinConfigurationPage.clickOnDropdownByLabel("Card Scheme");
        bankBinConfigurationPage.clickOnDropdownItemUsingActions(cardSchemeEightDigit);

        step("Click Save Bank BIN");
        bankBinConfigurationPage.clickButton("Save");
        bankBinConfigurationPage.clickButtonUsingJsExecutor("Submit");

        step("Verify the BIN is created successfully");
        step("Search using filter by BIN");
        bankBinConfigurationPage.inputSearchByBIN(binEightDigit);
        bankBinConfigurationPage.clickButton("Search");

        step("Verify the BIN is displayed in the listing");
        bankBinConfigurationPage.refreshPage();
        Assertion.assertThat("Is BIN displayed in the listing", bankBinConfigurationPage.isEmptyBinList(), is(false));

        step("Verify all configured values are displayed correctly");
        Assertion.assertThat("BIN Range Min value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "BIN Range Min"), is(binEightDigit));
        Assertion.assertThat("BIN Range Max value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "BIN Range Max"), is(binEightDigit));
        Assertion.assertThat("Bank Name value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Bank Name"), is(bankName));
        Assertion.assertThat("Bank Alias value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Bank Alias"), is(bankAlias));
        Assertion.assertThat("Country value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Country"), is(country));
        Assertion.assertThat("Card Type value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Card Type"), is(cardType.toUpperCase()));
        Assertion.assertThat("Card Name value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Card Name"), is(cardName));
        Assertion.assertThat("Whitelabel value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Whitelabel"), is(whiteLabel));

        step("Trigger the BIN API");
        BankBinResponse bankBinResponse = bankBinConfigurationPage.getBankBinFromApi(binEightDigit, currency, deviceId, lang, countryCode);

        step("Verify the API response");
        Long binRangeMin = bankBinResponse.data.binMin;
        Long binRangeMax = bankBinResponse.data.binMax;
        Assertion.assertThat("BIN Range MIN from API", binRangeMin, is(Long.valueOf(binEightDigit)));
        Assertion.assertThat("BIN Range MAX from API", binRangeMax, is(Long.valueOf(binEightDigit)));
        String bankNameApi = bankBinResponse.data.issuer;
        Assertion.assertThat("Bank Name from API", bankNameApi, is(bankName));
        String bankAliasApi = bankBinResponse.data.issuerAlias;
        Assertion.assertThat("Bank Alias from API", bankAliasApi, is(bankAlias));
        String countryApi = bankBinResponse.data.country;
        Assertion.assertThat("Country from API", countryApi, is(countryApiCode));
        String cardTypeApi = bankBinResponse.data.cardType;
        Assertion.assertThat("Card Type from API", cardTypeApi, is(cardType.toLowerCase()));
        String cardSchemeApi = bankBinResponse.data.cardScheme;
        Assertion.assertThat("Card Scheme from API", cardSchemeApi, is(cardSchemeEightDigit.toLowerCase()));
        String binTypeApi = bankBinResponse.data.binType;
        Assertion.assertThat("BIN Type from API", binTypeApi, is("EIGHT_DIGIT"));
        Boolean isWhiteLabel = bankBinResponse.data.whiteLabel;
        Assertion.assertThat("Whitelabel from API", isWhiteLabel, is(Boolean.valueOf(whiteLabel)));
        String cardNameApi = bankBinResponse.data.cardName;
        Assertion.assertThat("Card Name from API", cardNameApi, is(cardName));

        step("Edit BIN");
        String bankNameEdit = bankName.replaceAll("\\S+$", CoreFunctions.randomName());
        String bankAliasEdit = bankAlias.replaceAll("\\S+$", CoreFunctions.randomName());
        String cardNameEdit = cardName.replaceAll("\\S+$", CoreFunctions.randomName());
        String whitelabelEdit = whiteLabel.equals("FALSE") ? "TRUE" : "FALSE";
        bankBinConfigurationPage.clickActionRow("0", "Edit");

        step("Bank BIN Range Min and Bank BIN Range Max can't be edited");
        Assertion.assertThat("BIN Range Min value is correct", bankBinConfigurationPage.getInputValueOnFieldByLabel("BIN Range Min"), is(binEightDigit));
        Assertion.assertThat("BIN Range Min field is disabled", bankBinConfigurationPage.isDisabledInputField("BIN Range Min"), is(true));
        Assertion.assertThat("BIN Range Max value is correct", bankBinConfigurationPage.getInputValueOnFieldByLabel("BIN Range Max"), is(binEightDigit));
        Assertion.assertThat("BIN Range Max field is disabled", bankBinConfigurationPage.isDisabledInputField("BIN Range Max"), is(true));

        step("Edit Bank Name");
        bankBinConfigurationPage.inputOnFieldByLabel("Bank Name", bankNameEdit);
        step("Edit Bank Alias");
        bankBinConfigurationPage.inputOnFieldByLabel("Bank Alias", bankAliasEdit);
        step("Edit Card Name");
        bankBinConfigurationPage.inputOnFieldByLabel("Card Name", cardNameEdit);
        step("Edit White Label");
        bankBinConfigurationPage.clickOnDropdownByLabel("White Label");
        bankBinConfigurationPage.clickOnDropdownItem(whitelabelEdit);

        step("Click Save Bank BIN");
        bankBinConfigurationPage.clickButton("Save");
        bankBinConfigurationPage.clickButtonUsingJsExecutor("Submit");

        step("Verify the BIN is updated successfully");
        step("Search using filter by BIN");
        bankBinConfigurationPage.inputSearchByBIN(binEightDigit);
        bankBinConfigurationPage.clickButton("Search");

        step("Verify the BIN is displayed in the listing");
        Assertion.assertThat("Is BIN displayed in the listing", bankBinConfigurationPage.isEmptyBinList(), is(false));

        step("Verify all configured values updated");
        Assertion.assertThat("Bank Name value updated on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Bank Name"), is(bankNameEdit));
        Assertion.assertThat("Bank Alias value updated on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Bank Alias"), is(bankAliasEdit));
        Assertion.assertThat("Card Name value updated on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Card Name"), is(cardNameEdit));
        Assertion.assertThat("Whitelabel value updated on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Whitelabel"), is(whitelabelEdit));

        step("Verify Updated BIN API");
        bankBinResponse = bankBinConfigurationPage.getBankBinFromApi(binEightDigit, currency, deviceId, lang, countryCode);

        step("Verify the updated values are returned correctly");
        Assertion.assertThat("Bank Name from API", bankBinResponse.data.issuer, is(bankNameEdit));
        Assertion.assertThat("Bank Alias from API", bankBinResponse.data.issuerAlias, is(bankAliasEdit));
        Assertion.assertThat("Card Name from API", bankBinResponse.data.cardName, is(cardNameEdit));
        Assertion.assertThat("Whitelabel from API", bankBinResponse.data.whiteLabel, is(Boolean.valueOf(whitelabelEdit)));

        step("Verify BIN Range Min and BIN Range Max remain unchanged");
        Assertion.assertThat("BIN Range Min from API", bankBinResponse.data.binMin, is(Long.valueOf(binEightDigit)));
        Assertion.assertThat("BIN Range Max from API", bankBinResponse.data.binMax, is(Long.valueOf(binEightDigit)));

        step("Search using filter by BIN");
        bankBinConfigurationPage.inputSearchByBIN(binEightDigit);
        bankBinConfigurationPage.clickButton("Search");

        step("Delete the BIN");
        bankBinConfigurationPage.clickActionRowThenScroll("0", "Delete");
        bankBinConfigurationPage.clickButtonUsingJsExecutor("Yes, delete");

        step("Verify the BIN is removed from the listing");
        Assertion.assertThat("BIN deleted and list empty", bankBinConfigurationPage.isEmptyBinList(), is(true));

        step("Verify BIN API After Deletion");
        bankBinResponse = bankBinConfigurationPage.getBankBinFromApi(binEightDigit, currency, deviceId, lang, countryCode);

        binRangeMin = bankBinResponse.data.binMin;
        binRangeMax = bankBinResponse.data.binMax;
        bankNameApi = bankBinResponse.data.issuer;
        bankAliasApi = bankBinResponse.data.issuerAlias;
        countryApi = bankBinResponse.data.country;
        cardTypeApi = bankBinResponse.data.cardType;
        binTypeApi = bankBinResponse.data.binType;
        isWhiteLabel = bankBinResponse.data.whiteLabel;
        cardNameApi = bankBinResponse.data.cardName;

        Assertion.assertThat("BIN Range MIN from API is null", binRangeMin, is(nullValue()));
        Assertion.assertThat("BIN Range MAX from API is null", binRangeMax, is(nullValue()));
        Assertion.assertThat("Bank Name from API is null", bankNameApi, is(nullValue()));
        Assertion.assertThat("Bank Alias from API is null", bankAliasApi, is(nullValue()));
        Assertion.assertThat("Country from API is null", countryApi, is(nullValue()));
        Assertion.assertThat("Card Type from API is null", cardTypeApi, is(nullValue()));
        Assertion.assertThat("BIN Type from API is null", binTypeApi, is(nullValue()));
        Assertion.assertThat("Whitelabel from API is null", isWhiteLabel, is(nullValue()));
        Assertion.assertThat("Card Name from API is null", cardNameApi, is(nullValue()));

        step("flow for 6 digit BIN");
        step("Search using filter by BIN");
        bankBinConfigurationPage.clickButton("Reset Filter");
        bankBinConfigurationPage.inputSearchByBIN(binSixDigit);
        bankBinConfigurationPage.clickButton("Search");

        if (!bankBinConfigurationPage.isEmptyBinList()) {
            step("Delete the BIN because there is existing BIN");
            bankBinConfigurationPage.clickActionRowThenScroll("0", "Delete");
            bankBinConfigurationPage.clickButtonUsingJsExecutor("Yes, delete");
            Assertion.assertThat("BIN deleted and list empty", bankBinConfigurationPage.isEmptyBinList(), is(true));
        }

        step("Open Add New BIN page");
        bankBinConfigurationPage.clickButtonUsingJsExecutor("Add New BIN");

        step("Select Country");
        bankBinConfigurationPage.clickOnDropdownByLabel("Country");
        bankBinConfigurationPage.clickOnDropdownItem(country);

        step("Input Bank BIN Range Min");
        bankBinConfigurationPage.inputOnFieldByLabel("BIN Range Min", binSixDigit);

        step("Input Bank BIN Range Max");
        bankBinConfigurationPage.inputOnFieldByLabel("BIN Range Max", binSixDigit);

        step("Select BIN Type");
        bankBinConfigurationPage.clickOnDropdownByLabel("BIN Type");
        bankBinConfigurationPage.clickOnDropdownItem("6_Digit");

        step("Input Bank Name");
        bankBinConfigurationPage.inputOnFieldByLabel("Bank Name", bankName);

        step("Input Bank Alias");
        bankBinConfigurationPage.inputOnFieldByLabel("Bank Alias", bankAlias);

        step("Select Card Type");
        bankBinConfigurationPage.clickOnDropdownByLabel("Card Type");
        bankBinConfigurationPage.clickOnDropdownItem(cardType);

        step("Select White Label");
        bankBinConfigurationPage.clickOnDropdownByLabel("White Label");
        bankBinConfigurationPage.clickOnDropdownItem(whiteLabel);

        step("Input Card Name");
        bankBinConfigurationPage.inputOnFieldByLabel("Card Name", cardName);

        step("Select Card Scheme");
        bankBinConfigurationPage.clickOnDropdownByLabel("Card Scheme");
        bankBinConfigurationPage.clickOnDropdownItemUsingActions(cardSchemeSixDigit);

        step("Click Save Bank BIN");
        bankBinConfigurationPage.clickButtonUsingJsExecutor("Save");
        bankBinConfigurationPage.clickButtonUsingJsExecutor("Submit");

        step("Verify the BIN is created successfully");
        step("Search using filter by BIN");
        bankBinConfigurationPage.inputSearchByBIN(binSixDigit);
        bankBinConfigurationPage.clickButton("Search");

        step("Verify the BIN is displayed in the listing");
        Assertion.assertThat("Is BIN displayed in the listing", bankBinConfigurationPage.isEmptyBinList(), is(false));

        step("Verify all configured values are displayed correctly");
        Assertion.assertThat("BIN Range Min value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "BIN Range Min"), is(binSixDigit));
        Assertion.assertThat("BIN Range Max value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "BIN Range Max"), is(binSixDigit));
        Assertion.assertThat("Bank Name value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Bank Name"), is(bankName));
        Assertion.assertThat("Bank Alias value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Bank Alias"), is(bankAlias));
        Assertion.assertThat("Country value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Country"), is(country));
        Assertion.assertThat("Card Type value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Card Type"), is(cardType.toUpperCase()));
        Assertion.assertThat("Card Name value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Card Name"), is(cardName));
        Assertion.assertThat("Whitelabel value shown on list is correct", bankBinConfigurationPage.getRowValueFromColumn("0", "Whitelabel"), is(whiteLabel));

        step("Trigger the BIN API");
        bankBinResponse = bankBinConfigurationPage.getBankBinFromApi(binSixDigit, currency, deviceId, lang, countryCode);

        step("Verify the API response");
        binRangeMin = bankBinResponse.data.binMin;
        binRangeMax = bankBinResponse.data.binMax;
        Assertion.assertThat("BIN Range MIN from API", binRangeMin, is(Long.valueOf(binSixDigit)));
        Assertion.assertThat("BIN Range MAX from API", binRangeMax, is(Long.valueOf(binSixDigit)));
        bankNameApi = bankBinResponse.data.issuer;
        Assertion.assertThat("Bank Name from API", bankNameApi, is(bankName));
        bankAliasApi = bankBinResponse.data.issuerAlias;
        Assertion.assertThat("Bank Alias from API", bankAliasApi, is(bankAlias));
        countryApi = bankBinResponse.data.country;
        Assertion.assertThat("Country from API", countryApi, is(countryApiCode));
        cardTypeApi = bankBinResponse.data.cardType;
        Assertion.assertThat("Card Type from API", cardTypeApi, is(cardType.toLowerCase()));
        cardSchemeApi = bankBinResponse.data.cardScheme;
        Assertion.assertThat("Card Scheme from API", cardSchemeApi, is(cardSchemeSixDigit.toLowerCase()));
        binTypeApi = bankBinResponse.data.binType;
        Assertion.assertThat("BIN Type from API", binTypeApi, is("SIX_DIGIT"));
        isWhiteLabel = bankBinResponse.data.whiteLabel;
        Assertion.assertThat("Whitelabel from API", isWhiteLabel, is(Boolean.valueOf(whiteLabel)));
        cardNameApi = bankBinResponse.data.cardName;
        Assertion.assertThat("Card Name from API", cardNameApi, is(cardName));

        bankBinConfigurationPage.refreshPage();
        step("Search using filter by BIN");
        bankBinConfigurationPage.inputSearchByBIN(binSixDigit);
        bankBinConfigurationPage.clickButton("Search");

        step("Delete the BIN");
        bankBinConfigurationPage.clickActionRowThenScroll("0", "Delete");
        bankBinConfigurationPage.clickButtonUsingJsExecutor("Yes, delete");

        step("Verify the BIN is removed from the listing");
        Assertion.assertThat("BIN deleted and list empty", bankBinConfigurationPage.isEmptyBinList(), is(true));

        step("Verify BIN API After Deletion");
        bankBinResponse = bankBinConfigurationPage.getBankBinFromApi(binSixDigit, currency, deviceId, lang, countryCode);

        binRangeMin = bankBinResponse.data.binMin;
        binRangeMax = bankBinResponse.data.binMax;
        bankNameApi = bankBinResponse.data.issuer;
        bankAliasApi = bankBinResponse.data.issuerAlias;
        countryApi = bankBinResponse.data.country;
        cardTypeApi = bankBinResponse.data.cardType;
        binTypeApi = bankBinResponse.data.binType;
        isWhiteLabel = bankBinResponse.data.whiteLabel;
        cardNameApi = bankBinResponse.data.cardName;

        Assertion.assertThat("BIN Range MIN from API is null", binRangeMin, is(nullValue()));
        Assertion.assertThat("BIN Range MAX from API is null", binRangeMax, is(nullValue()));
        Assertion.assertThat("Bank Name from API is null", bankNameApi, is(nullValue()));
        Assertion.assertThat("Bank Alias from API is null", bankAliasApi, is(nullValue()));
        Assertion.assertThat("Country from API is null", countryApi, is(nullValue()));
        Assertion.assertThat("Card Type from API is null", cardTypeApi, is(nullValue()));
        Assertion.assertThat("BIN Type from API is null", binTypeApi, is(nullValue()));
        Assertion.assertThat("Whitelabel from API is null", isWhiteLabel, is(nullValue()));
        Assertion.assertThat("Card Name from API is null", cardNameApi, is(nullValue()));
    }

    @Author(names = {"Ashish"})
    @Module(name = "CardPaymentConfiguration")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyCardValidationFlow() throws InterruptedException {
        Map<String, String> param = getParam("verifyCardValidationFlow");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        CardValidation<?> cardValidation = new CardValidation<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String cardBrand = param.get("cardBrand");
        String[] cardBrands = param.get("cardBrands").split("-");

        step("Login to Admin Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open Card Validation dashboard");
        cardValidation.openCardValidationPage();
        Assertion.assertThat("Card Validation page loads successfully", cardValidation.isCardValidationPageOpened(), is(true));

        step("Verify Card Validation matrix and columns are displayed");
        Assertion.assertThat("Card Validation matrix is displayed", cardValidation.isCardValidationMatrixDisplayed(), is(true));
        List<String> columnHeaders = cardValidation.getColumnHeaders();
        Assertion.assertThat("Payment Currency column is displayed", columnHeaders, hasItem("Payment Currency"));
        for (String brand : cardBrands) {
            Assertion.assertThat(brand + " column is displayed", columnHeaders, hasItem(brand));
        }

        step("Verify available status options in the action menu for " + currency + " - " + cardBrand);
        Assertion.assertThat("Available status options are SUPPORTED and NOT_SUPPORTED", cardValidation.getAvailableStatusOptions(currency, cardBrand), containsInAnyOrder(CardValidation.SUPPORTED, CardValidation.NOT_SUPPORTED));

        List<String> availableCurrencies = new ArrayList<>(new LinkedHashSet<>(cardValidation.getCurrencies()));
        List<String> availableBrands = new ArrayList<>(new LinkedHashSet<>(Arrays.asList(cardBrands)));
        Assertion.assertThat("At least 2 distinct currencies are available in the matrix", availableCurrencies.size(), greaterThanOrEqualTo(2));
        Assertion.assertThat("At least 2 distinct card brands are available", availableBrands.size(), greaterThanOrEqualTo(2));

        Collections.shuffle(availableCurrencies);
        Collections.shuffle(availableBrands);
        List<String> selectedCurrencies = new ArrayList<>(availableCurrencies.subList(0, 2));
        List<String> selectedBrands = new ArrayList<>(availableBrands.subList(0, 2));

        step("Randomly selected currencies " + selectedCurrencies + " and card brands " + selectedBrands + " for verification");

        List<String[]> combinations = new ArrayList<>();
        for (String selectedCurrency : selectedCurrencies) {
            for (String selectedBrand : selectedBrands) {
                combinations.add(new String[]{selectedCurrency, selectedBrand});
            }
        }

        Map<String, String> originalStatuses = new LinkedHashMap<>();
        Map<String, String> targetStatuses = new LinkedHashMap<>();

        try {
            for (String[] combo : combinations) {
                String comboCurrency = combo[0];
                String comboBrand = combo[1];
                String label = comboCurrency + " - " + comboBrand;
                String key = comboCurrency + "|" + comboBrand;

                step("Verify current (before) support status for " + label);
                String beforeStatus = cardValidation.getCardStatus(comboCurrency, comboBrand);
                Assertion.assertThat("Before status for " + label + " is a valid value", beforeStatus, anyOf(is(CardValidation.SUPPORTED), is(CardValidation.NOT_SUPPORTED)));
                originalStatuses.put(key, beforeStatus);

                String targetStatus = beforeStatus.equals(CardValidation.SUPPORTED) ? CardValidation.NOT_SUPPORTED : CardValidation.SUPPORTED;
                targetStatuses.put(key, targetStatus);

                step("Update " + label + " from " + beforeStatus + " to " + targetStatus);
                Assertion.assertThat("Success message after updating " + label, cardValidation.updateCardStatus(comboCurrency, comboBrand, targetStatus), containsStringIgnoringCase("success"));

                step("Verify (after) status for " + label + " is " + targetStatus + " in the matrix");
                Assertion.assertThat("After status for " + label + " is reflected in the matrix", cardValidation.getCardStatusWhenMatching(comboCurrency, comboBrand, targetStatus), is(targetStatus));
            }

            step("Verify Client API reflects all updated statuses");
            JSONArray configAfterUpdate = cardValidation.getCardValidationConfigWhenMatching(combinations, targetStatuses);
            for (String[] combo : combinations) {
                String label = combo[0] + " - " + combo[1];
                String expected = targetStatuses.get(combo[0] + "|" + combo[1]);
                Assertion.assertThat("Client API value matches updated Admin Panel config for " + label, cardValidation.getCardStatusFromConfig(configAfterUpdate, combo[0], combo[1]), equalToIgnoringCase(expected));
            }

            step("Reset all updated card validation statuses back to their original values");
            for (String[] combo : combinations) {
                String comboCurrency = combo[0];
                String comboBrand = combo[1];
                String label = comboCurrency + " - " + comboBrand;
                String original = originalStatuses.get(comboCurrency + "|" + comboBrand);

                step("Revert " + label + " back to " + original);
                Assertion.assertThat("Success message after reverting " + label, cardValidation.updateCardStatus(comboCurrency, comboBrand, original), containsStringIgnoringCase("success"));
                Assertion.assertThat("Original status for " + label + " is restored in the matrix", cardValidation.getCardStatusWhenMatching(comboCurrency, comboBrand, original), is(original));
            }

            step("Verify Client API reflects all reverted statuses");
            JSONArray configAfterRevert = cardValidation.getCardValidationConfigWhenMatching(combinations, originalStatuses);
            for (String[] combo : combinations) {
                String label = combo[0] + " - " + combo[1];
                String original = originalStatuses.get(combo[0] + "|" + combo[1]);
                Assertion.assertThat("Client API value matches reverted Admin Panel config for " + label, cardValidation.getCardStatusFromConfig(configAfterRevert, combo[0], combo[1]), equalToIgnoringCase(original));
            }
        } finally {
            step("Safety reset: ensure all updated card validation statuses are restored to original");
            try {
                cardValidation.refreshPage();
            } catch (Exception e) {
                CoreFunctions.log("Safety reset: failed to refresh before restoring card validation statuses: " + e.getMessage());
            }
            for (Map.Entry<String, String> entry : originalStatuses.entrySet()) {
                String[] parts = entry.getKey().split("\\|");
                String comboCurrency = parts[0];
                String comboBrand = parts[1];
                try {
                    if (!cardValidation.getCardStatus(comboCurrency, comboBrand).equals(entry.getValue())) {
                        cardValidation.updateCardStatus(comboCurrency, comboBrand, entry.getValue());
                        cardValidation.refreshPage();
                        if (!cardValidation.getCardStatus(comboCurrency, comboBrand).equals(entry.getValue())) {
                            CoreFunctions.log("Safety reset FAILED to restore " + comboCurrency + " - " + comboBrand
                                    + " to " + entry.getValue() + "; manual cleanup of card validation config may be required");
                        }
                    }
                } catch (Exception e) {
                    cardValidation.logException(comboCurrency, comboBrand, e);
                }
            }
        }
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "CardPaymentConfiguration")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyTheWholeFlowOfDefaultCardRouting() throws ExecutionException, InterruptedException {
        Map<String, String> param = getParam("verifyTheWholeFlowOfDefaultCardRouting");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        DefaultCardRouting<?> defaultCardRouting = new DefaultCardRouting<>(session);
        CustomPaymentRouting<?>  customPaymentRouting = new CustomPaymentRouting<>(session);
        orderCreationPage<?> orderCreation = new orderCreationPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String currency = param.getOrDefault("currency", "IDR");
        String nonIdrCurrency = param.getOrDefault("nonIdrCurrency", "SGD");
        String paymentKey = param.get("paymentKey");
        String nonIdrPaymentKey = param.get("nonIdrPaymentKey");
        String nonIdrGateway = param.get("nonIdrGateway");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String cardNumber = param.get("cardNumber");
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String cardName = param.get("cardName");
        String cardExpiredMonth = param.get("cardExpiredMonth");
        String cardExpiredYear = param.get("cardExpiredYear");
        String cvv = param.get("cvv");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];
        step("Create waiting payment hotel order for IDR cases");
        orderCreation.createWaitingPaymentOrderHotel(userConsumer, passConsumer, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);
        String orderId = String.valueOf(orderCreation.getOrderId());
        String orderHash = orderCreation.getOrderHash();
        String accessToken = orderCreation.getAccessToken();

        step("Login to Admin Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Open Custom Card Routing dashboard");
        customPaymentRouting.openCustomPaymentRoutingPage(3);

        step("Step: Verify Rule Status and deactivate all active rules");
        customPaymentRouting.deactivateAllActiveRules();

        step("Step: Verify all Rules are successfully deactivated");
        customPaymentRouting.verifyAllRulesAreInactive();

        step("Navigate to Default Card Routing");
        defaultCardRouting.openDefaultCardRoutingPage(3);

        List<String> targetGateways = Arrays.asList("CCPP","NICEPAY","XENDIT");
        String previousGateway = null;
        for (String gateway : targetGateways) {
            step("Step: Configure '" + gateway + "' as Default IDR and verify deactivation is blocked");
            defaultCardRouting.activateGatewayIfInactive(gateway);

            boolean isDefaultIdr = defaultCardRouting.isGatewayDefaultIdr(gateway);
            if (!isDefaultIdr) {
                defaultCardRouting.setGatewayDefaultIdr(gateway);
                defaultCardRouting.clickModalYesChange();
            }

            defaultCardRouting.toggleGatewayAction(gateway);
            boolean isErrorDisplayed = defaultCardRouting.isCantDeactivateModalDisplayed();
            Assertion.assertThat("Verify 'Can't deactivate' modal appeared", isErrorDisplayed, is(true));
            defaultCardRouting.clickModalOk();

            String actualCardNumber = switch (gateway.toUpperCase()) {
                case "NICEPAY" -> param.get("nicePayCardNumber");
                default        -> param.get("cardNumber");
            };
            step("Hit the Submit Credit Card API for IDR case: "+gateway);
            SubmitCreditCardV2.Inputs submitInputs = new SubmitCreditCardV2.Inputs(
                    accessToken, orderHash, Long.parseLong(orderId), actualCardNumber, cardName,
                    cardExpiredMonth, cardExpiredYear, cvv,
                    paymentKey,
                    gateway,
                    currency
            );
            JSONObject responseJson = orderCreation.submitCreditCardV2ResponseJSON(submitInputs);
            boolean hitLandingAPI = true;
            step("Verify the Submit Credit Card API response for gateway: " + gateway);
            switch (gateway.toUpperCase()) {
                case "NICEPAY" -> {
                    String code = responseJson.optString("code", "");
                    String message = responseJson.optString("message", "");

                    Assertion.assertThat("Verify NICEPAY error code", code, is(equalTo("ERROR")));
                    Assertion.assertThat("Verify NICEPAY error message", message, is(equalTo("Nicepay Error")));
                }

                case "CCPP" -> {
                    String acsUrl = defaultCardRouting.extractAcsUrlSafely(responseJson);
                    Assertion.assertThat("Verify CCPP acsUrl contains 2c2p", acsUrl, containsString("2c2p"));
                }

                case "XENDIT" -> {
                    String acsUrl = defaultCardRouting.extractAcsUrlSafely(responseJson);
                    if (acsUrl.contains("XENDIT")) { // If acsUrl contains XENDIT, we have to skip hitting the landing page API
                        hitLandingAPI = false;
                        break;
                    }
                    Assertion.assertThat("Verify XENDIT acsUrl is empty or null", acsUrl, is(emptyOrNullString()));
                }
                default -> throw new IllegalArgumentException("No validation rules defined for gateway: " + gateway);
            }
            String landingCode = switch (gateway.toUpperCase()) {
                case "XENDIT" -> param.get("xenditLandingCode");
                default        -> param.get("landingCode");
            };

            if(hitLandingAPI){
                step("Hit the landing page API for " + gateway);
                LandingPageApiV2.Input landingApi = new LandingPageApiV2.Input(accessToken,orderId, orderHash);
                JSONObject landingApiV2ResponseJson = orderCreation.submitBookingLanding(landingApi);
                String actualLandingCode = landingApiV2ResponseJson.optString("code", "");
                Assertion.assertThat("Verify the landing page API code is "+landingCode, actualLandingCode, is(equalTo(landingCode)));
            }


            if (previousGateway != null) {
                step("Step: Successfully deactivate the previously tested gateway: '" + previousGateway + "'");
                defaultCardRouting.toggleGatewayAction(previousGateway);
                defaultCardRouting.clickModalYesDeactivate();
                defaultCardRouting.verifySnackbarMessage("deactivate", previousGateway);
                String previousStatus = defaultCardRouting.getGatewayStatus(previousGateway);
                Assertion.assertThat("Verify " + previousGateway + " successfully deactivated", previousStatus, is(equalToIgnoringCase("Inactive")));
            }
            previousGateway = gateway;
        }
        if (previousGateway != null && targetGateways.size() > 1) {
            step("Step: Successfully deactivate the previously tested gateway: '" + previousGateway + "'");
            String fallbackGateway = targetGateways.get(0);
            String fallbackStatus = defaultCardRouting.getGatewayStatus(fallbackGateway);
            if (fallbackStatus.equalsIgnoreCase("Inactive")) {
                defaultCardRouting.toggleGatewayAction(fallbackGateway);
                defaultCardRouting.clickModalYesActivate();
            }
            boolean isFallbackDefault = defaultCardRouting.isGatewayDefaultIdr(fallbackGateway);
            if (!isFallbackDefault) {
                defaultCardRouting.setGatewayDefaultIdr(fallbackGateway);
                defaultCardRouting.clickModalYesChange();
            }
            defaultCardRouting.toggleGatewayAction(previousGateway);
            defaultCardRouting.clickModalYesDeactivate();
            String finalStatus = defaultCardRouting.getGatewayStatus(previousGateway);
            Assertion.assertThat("Verify " + previousGateway + " successfully deactivated", finalStatus, is(equalToIgnoringCase("Inactive")));
        }

        step("Create Non-IDR waiting payment hotel order");
        orderCreation.createWaitingPaymentOrderHotel(userConsumer, passConsumer, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, nonIdrCurrency);
        orderId = String.valueOf(orderCreation.getOrderId());
        orderHash = orderCreation.getOrderHash();
        accessToken = orderCreation.getAccessToken();

        step("Hit the Submit Credit Card API for Non-IDR case: " + nonIdrGateway);
        SubmitCreditCardV2.Inputs submitInputs = new SubmitCreditCardV2.Inputs(
                accessToken, orderHash, Long.parseLong(orderId), cardNumber, cardName,
                cardExpiredMonth, cardExpiredYear, cvv,
                nonIdrPaymentKey,
                nonIdrGateway,
                nonIdrCurrency
        );
        JSONObject responseJson = orderCreation.submitCreditCardV2ResponseJSON(submitInputs);

        step("Verify the Submit API response for Non-IDR case");
        String acsUrl = defaultCardRouting.extractAcsUrlSafely(responseJson);
        Assertion.assertThat("Verify Stripe acsUrl contains stripe or juspay", acsUrl, anyOf(containsStringIgnoringCase("stripe"), containsStringIgnoringCase("juspay")));

        step("Set the XENDIT and CCPP as active and set Default IDR for XENDIT");
        String gateway = targetGateways.get(2);
        defaultCardRouting.activateGatewayIfInactive(gateway);

        boolean isDefaultIdr = defaultCardRouting.isGatewayDefaultIdr(gateway);
        if (!isDefaultIdr) {
            defaultCardRouting.setGatewayDefaultIdr(gateway);
            defaultCardRouting.clickModalYesChange();
        }

        // 0 is for CCPP we only need to activate it and no need to set Default as IDR for CCPP
        gateway = targetGateways.get(0);
        defaultCardRouting.activateGatewayIfInactive(gateway);
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "CustomPaymentRouting")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyRoutingToCorrectPaymentGatewayBasedOnCPR() throws Exception {
        Map<String, String> param = getParam("verifyRoutingToCorrectPaymentGatewayBasedOnCPR");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        CustomPaymentRoutingPage<?> customPaymentRoutingPage = new CustomPaymentRoutingPage<>(session);
        DefaultCardRouting<?> defaultCardRouting = new DefaultCardRouting<>(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        PaymentApi<?> paymentApi = new PaymentApi<>(session);
        Unm<?> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String b2cEmail = param.get("b2cEmail");
        String b2cPassword = param.get("b2cPassword");
        String hotelQuery = param.get("hotelQuery");
        String startDate = param.get("startDate");
        String cardNumber = param.get("cardNumber");
        String cardHolderName = param.get("cardHolderName");
        String cardExpiredMonth = param.get("cardExpiredMonth");
        String cardExpiredYear = param.get("cardExpiredYear");
        String cardCvvNumber = param.get("cardCvvNumber");
        String nicepayCardNumber = param.get("nicepayCardNumber");
        String nicepayCardHolderName = param.get("nicepayCardHolderName");
        String nicepayCardExpiredMonth = param.get("nicepayCardExpiredMonth");
        String nicepayCardExpiredYear = param.get("nicepayCardExpiredYear");
        String nicepayCardCvvNumber = param.get("nicepayCardCvvNumber");

        String accessToken = unm.getAccessToken(b2cEmail, b2cPassword, "B2C");
        String orderId;
        String orderHash;
        String paymentUrl;
        String currentDefaultIdr;

        step("Login to Admin Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Go to Custom Payment Routing page");
        customPaymentRoutingPage.openCustomPaymentRoutingPage();
        Assertion.assertThat("Custom Payment Routing page is opened",
                customPaymentRoutingPage.isCustomPaymentRoutingPageOpened(), is(true));

        // ── Default Card Routing: ensure XENDIT is Default IDR before CCPP test ──────
        step("Navigate to Default Card Routing page");
        defaultCardRouting.openDefaultCardRoutingPage(5);

        step("Ensure XENDIT is set as Default IDR (CCPP must not be the default while testing CCPP CPR routing)");
        currentDefaultIdr = defaultCardRouting.getRoutedPgByColumn("Default IDR");
        if (!currentDefaultIdr.equalsIgnoreCase("XENDIT")) {
            defaultCardRouting.routePg("XENDIT", "Default IDR");
        }

        step("Navigate back to Custom Payment Routing page");
        customPaymentRoutingPage.openCustomPaymentRoutingPage();

        // // ── CCPP ──────────────────────────────────────────────────────────────

        step("Check current CCPP status - skip activate toggle if already Active");
        if (!customPaymentRoutingPage.getStatusText("CCPP").equalsIgnoreCase("Active")) {
            step("Click Action dropdown for CCPP");
            customPaymentRoutingPage.clickActionDropdown("CCPP");

            step("Click Edit for CCPP");
            customPaymentRoutingPage.clickEditOption();

            step("Click toggle to activate CCPP");
            customPaymentRoutingPage.clickStatusToggle();

            step("Save CCPP active status");
            customPaymentRoutingPage.saveStatusChange();
        }

        step("Verify CCPP status in table is Active");
        Assertion.assertThat("CCPP status should be Active", customPaymentRoutingPage.getStatusText("CCPP"), is("Active"));

        step("Create order using CC via API");
        orderId = hotelApi.createWaitingPaymentOrderApi(b2cEmail, b2cPassword, startDate, hotelQuery, 1, 1, 1, "IDR", 0, "hotel", "id");
        orderHash = hotelApi.getOrderHash();

        step("Submit CC payment and validate paymentUrl is of CCPP PG");
        paymentUrl = paymentApi.submitCreditCardPayment(accessToken, orderHash, Long.parseLong(orderId), cardNumber, cardHolderName, cardExpiredMonth, cardExpiredYear, cardCvvNumber);
        Assertion.assertThat("paymentUrl should point to CCPP (2c2p)", paymentUrl.toLowerCase(), containsString("2c2p"));

        step("Check current CCPP status - skip deactivate toggle if already Inactive");
        if (!customPaymentRoutingPage.getStatusText("CCPP").equalsIgnoreCase("Inactive")) {
            step("Click Action dropdown for CCPP");
            customPaymentRoutingPage.clickActionDropdown("CCPP");

            step("Click Edit for CCPP");
            customPaymentRoutingPage.clickEditOption();

            step("Click toggle to deactivate CCPP");
            customPaymentRoutingPage.clickStatusToggle();

            step("Save CCPP inactive status");
            customPaymentRoutingPage.saveStatusChange();
        }

        step("Verify CCPP status in table is Inactive");
        Assertion.assertThat("CCPP status should be Inactive", customPaymentRoutingPage.getStatusText("CCPP"), is("Inactive"));

        step("Navigate to Default Card Routing page");
        defaultCardRouting.openDefaultCardRoutingPage(5);

        step("Check current Default IDR - skip routing if CCPP is already set");
        currentDefaultIdr = defaultCardRouting.getRoutedPgByColumn("Default IDR");
        if (!currentDefaultIdr.equalsIgnoreCase("CCPP")) {
            defaultCardRouting.routePg("CCPP", "Default IDR");
        }

        step("Navigate back to Custom Payment Routing page");
        customPaymentRoutingPage.openCustomPaymentRoutingPage();

        // ── Xendit ────────────────────────────────────────────────────────────

        step("Check current Xendit status - skip activate toggle if already Active");
        if (!customPaymentRoutingPage.getStatusText("Xendit").equalsIgnoreCase("Active")) {
            step("Click Action dropdown for Xendit");
            customPaymentRoutingPage.clickActionDropdown("Xendit");

            step("Click Edit for Xendit");
            customPaymentRoutingPage.clickEditOption();

            step("Click toggle to activate Xendit");
            customPaymentRoutingPage.clickStatusToggle();

            step("Save Xendit active status");
            customPaymentRoutingPage.saveStatusChange();
        }

        step("Verify Xendit status in table is Active");
        Assertion.assertThat("Xendit status should be Active", customPaymentRoutingPage.getStatusText("Xendit"), is("Active"));

        step("Create order using CC via API");
        orderId = hotelApi.createWaitingPaymentOrderApi(b2cEmail, b2cPassword, startDate, hotelQuery, 1, 1, 1, "IDR", 0, "hotel", "id");
        orderHash = hotelApi.getOrderHash();

        step("Submit CC payment and validate paymentUrl is of Xendit PG");
        paymentUrl = paymentApi.submitCreditCardPayment(accessToken, orderHash, Long.parseLong(orderId), cardNumber, cardHolderName, cardExpiredMonth, cardExpiredYear, cardCvvNumber);
        Assertion.assertThat("paymentUrl should point to Xendit or be empty on success", paymentUrl.toLowerCase(), anyOf(containsString("xendit"), is("")));

        step("Check current Xendit status - skip deactivate toggle if already Inactive");
        if (!customPaymentRoutingPage.getStatusText("Xendit").equalsIgnoreCase("Inactive")) {
            step("Click Action dropdown for Xendit");
            customPaymentRoutingPage.clickActionDropdown("Xendit");

            step("Click Edit for Xendit");
            customPaymentRoutingPage.clickEditOption();

            step("Click toggle to deactivate Xendit");
            customPaymentRoutingPage.clickStatusToggle();

            step("Save Xendit inactive status");
            customPaymentRoutingPage.saveStatusChange();
        }

        step("Verify Xendit status in table is Inactive");
        Assertion.assertThat("Xendit status should be Inactive", customPaymentRoutingPage.getStatusText("Xendit"), is("Inactive"));

        // // ── Nicepay ───────────────────────────────────────────────────────────

        step("Check current Nicepay status - skip activate toggle if already Active");
        if (!customPaymentRoutingPage.getStatusText("Nicepay").equalsIgnoreCase("Active")) {
            step("Click Action dropdown for Nicepay");
            customPaymentRoutingPage.clickActionDropdown("Nicepay");

            step("Click Edit for Nicepay");
            customPaymentRoutingPage.clickEditOption();

            step("Click toggle to activate Nicepay");
            customPaymentRoutingPage.clickStatusToggle();

            step("Save Nicepay active status");
            customPaymentRoutingPage.saveStatusChange();
        }

        step("Verify Nicepay status in table is Active");
        Assertion.assertThat("Nicepay status should be Active", customPaymentRoutingPage.getStatusText("Nicepay"), is("Active"));

        step("Create order using CC via API");
        orderId = hotelApi.createWaitingPaymentOrderApi(b2cEmail, b2cPassword, startDate, hotelQuery, 1, 1, 1, "IDR", 0, "hotel", "id");
        orderHash = hotelApi.getOrderHash();

        step("Submit CC payment and validate Nicepay Error response");
        paymentUrl = paymentApi.submitCreditCardPayment(accessToken, orderHash, Long.parseLong(orderId), nicepayCardNumber, nicepayCardHolderName, nicepayCardExpiredMonth, nicepayCardExpiredYear, nicepayCardCvvNumber);
        Assertion.assertThat("Payment response should contain Nicepay Error", paymentUrl.toLowerCase(), containsString("nicepay error"));

        step("Check current Nicepay status - skip deactivate toggle if already Inactive");
        if (!customPaymentRoutingPage.getStatusText("Nicepay").equalsIgnoreCase("Inactive")) {
            step("Click Action dropdown for Nicepay");
            customPaymentRoutingPage.clickActionDropdown("Nicepay");

            step("Click Edit for Nicepay");
            customPaymentRoutingPage.clickEditOption();

            step("Click toggle to deactivate Nicepay");
            customPaymentRoutingPage.clickStatusToggle();

            step("Save Nicepay inactive status");
            customPaymentRoutingPage.saveStatusChange();
        }

        step("Verify Nicepay status in table is Inactive");
        Assertion.assertThat("Nicepay status should be Inactive", customPaymentRoutingPage.getStatusText("Nicepay"), is("Inactive"));

        // ── Default Card Routing: restore XENDIT as Default IDR ────────────────
        step("Navigate to Default Card Routing page and restore XENDIT as Default IDR");
        defaultCardRouting.openDefaultCardRoutingPage(5);
        defaultCardRouting.routePg("XENDIT", "Default IDR");
    }
    
}
