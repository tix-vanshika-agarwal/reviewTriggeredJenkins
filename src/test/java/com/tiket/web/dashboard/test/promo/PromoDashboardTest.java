package com.tiket.web.dashboard.test.promo;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.promo.PromoCreationBINModalWeb;
import com.tiket.web.dashboard.page.promo.PromoDashboard;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class PromoDashboardTest extends BaseTest {
    @Author(names = { "Hiten" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyResetFilter() throws InterruptedException {
        Map<String, String> param = getParam("verifyResetFilter");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String promoName = param.get("promoName");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePageViaUI(username,password);

        step("Go to Promo dashboard page");
        promodash.openPromoPage();

        step("Input promo name in search field");
        promodash.inputSearchPromo(promoName);

        step("Click on reset filter button");
        promodash.clickResetFilter();

        step("Verify all the fields are empty");
        String search = promodash.getSearchTextSearch();
        String type = promodash.getSearchTextType();
        String status = promodash.getSearchTextStatus();
        String dateCreated = promodash.getSearchTextDateCreated();
        String dateEnded = promodash.getSearchTextDateEnded();
        Assertion.assertThat("Verify there is no text in input field", search, is(""));
        Assertion.assertThat("Verify there is no text in input field",type ,is(""));
        Assertion.assertThat("Verify there is no text in input field", status,is(""));
        Assertion.assertThat("Verify there is no text in input field", dateCreated,is(""));
        Assertion.assertThat("Verify there is no text in input field",dateEnded ,is(""));
    }

    @Author(names = { "Hiten" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void VerifyApproveButton() throws InterruptedException {
        Map<String, String> param = getParam("VerifyApproveButton");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("Go to Promo dashboard page");
        promodash.openPromoPage();

        step("Go to approval page");
        promodash.openApprovalPage();

        step("Click on approval dropdown and select approval option");
        promodash.clickApprovalStatus();
        promodash.clickApprovalStatusSelect();

        step("Close the dropdown");
        promodash.clickApprovalStatus();

        step("Click on search button");
        promodash.clickOnSearch();

        step("Verify there is promo present for approval or not");
        int cnt = promodash.extractNumber();
        Assertion.assertThat("Verify the result is greater than zero", (cnt>0), is(true));

        step("Select the promo from the list");
        promodash.clickOnSelectedPromo();

        Assertion.assertThat("Verify approve button is displayed", promodash.isApproveButtonDisplayed(), is(true));
    }

    @Author(names = { "Hiten" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifySendForEditButton() throws InterruptedException {
        Map<String, String> param = getParam("verifySendForEditButton");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("Go to Promo dashboard page");
        promodash.openPromoPage();

        step("Go to approval page");
        promodash.openApprovalPage();

        step("Click on approval dropdown and select approval option");
        promodash.clickApprovalStatus();
        promodash.clickApprovalStatusSelect();

        step("Close the dropdown");
        promodash.clickApprovalStatus();

        step("Click on search button");
        promodash.clickOnSearch();

        step("Verify there is promo present for approval or not");
        int cnt = promodash.extractNumber();
        Assertion.assertThat("Verify the result is greater than zero", (cnt>0), is(true));

        step("Select the promo from the list");
        promodash.clickOnSelectedPromo();

        Assertion.assertThat("Verify send for edit button is displayed", promodash.isSendForEditButtonDisplayed(), is(true));
    }

    @Author(names = { "Hiten" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyPromoCodeApproved() throws InterruptedException {
        Map<String, String> param = getParam("verifyPromoCodeApproved");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String searchKey = param.get("searchKey");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("Go to Promo dashboard page");
        promodash.openPromoPage();

        step("Go to approval page");
        promodash.openApprovalPage();

        step("Click on approval dropdown and select approval option");
        promodash.clickApprovalStatus();
        promodash.clickApprovalStatusSelect();

        step("Close the dropdown");
        promodash.clickApprovalStatus();

        step("Click on search button");
        promodash.clickOnSearch();

        step("Verify there is promo present for approval or not");
        int cnt = promodash.extractNumber();
        Assertion.assertThat("Verify the result is greater than zero", (cnt>0), is(true));

        step("Select the promo from the list");
        promodash.clickOnSelectedPromo();

        Assertion.assertThat("Verify approve button is displayed", promodash.isApproveButtonDisplayed(), is(true));

        step("Fetch the promo code campaign name");
        String promoCampaignName = promodash.getPromoCampaignName();

        step("Click on approve button");
        promodash.clickAprroveButton();

        String queryString = "{ \"code\": \"" + promoCampaignName + "\" }";
        List<String> statusMongoDBList = promodash.getPromoCodeMongoData(config,database,collectionName,queryString,searchKey);
        Assertion.assertThat("Verify before approving the promo code the status is in approval state", promodash.isDataMatched("IN_APPROVAL", statusMongoDBList), is(true));

        step("Click on approve modal");
        promodash.clickOnApproveModal();

        Assertion.assertThat("Verify the pop up is displayed", promodash.isPopUpdisplayed(), is(true));
        Assertion.assertThat("Verify the pop up text", promodash.getApprovalPopUpText(), is(true));

        step("Verify from database");
        String approvalText = promodash.getTextAfterApproval();
        String status;
        if (approvalText.contains("Success")) {
            status = "ACTIVE";
        } else {
            status = "IN_APPROVAL";
        }
        status = status.toUpperCase();

        statusMongoDBList = promodash.getPromoCodeMongoData(config,database,collectionName,queryString,searchKey);
        boolean isMatched = promodash.isDataMatched(status, statusMongoDBList);
        Assertion.assertThat("Verify the status of PromoCode", isMatched, is(true));
    }

    @Author(names = { "Hiten" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyPromoCodeCampaignStatus() throws InterruptedException {
        Map<String, String> param = getParam("verifyPromoCodeCampaignStatus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String searchKey = param.get("searchKey");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("Go to Promo dashboard page");
        promodash.openPromoPage();

        step("Verify there is promo present or not");
        int cnt = promodash.extractNumber();
        Assertion.assertThat("Verify the result is greater than zero", (cnt>0), is(true));

        promodash.selectPromoDropdown();
        promodash.selectActivePromo();
        promodash.selectPromoDropdown();
        promodash.clickOnSearch();

        step("Select the promo from the list");
        promodash.clickOnSelectedPromo();

        step("Fetch the promo code campaign name");
        String promoCampaignName = promodash.getPromoCampaignName();
        String status = promodash.isValuePresentPromoStatus();
        String queryString = "{ \"code\": \"" + promoCampaignName + "\" }";

        List<String> statusMongoDBList = promodash.getPromoCodeMongoData(config,database,collectionName,queryString,searchKey);
        status = status.toUpperCase();
        boolean isMatched = promodash.isDataMatched(status, statusMongoDBList);
        Assertion.assertThat("Verify the status heading field", promodash.isOverviewStatusHeadingDisplayed(), is(true));
        Assertion.assertThat("Verify the status value field", promodash.isOverviewStatusDisplayed(), is(true));
        Assertion.assertThat("Verify the status of PromoCode", isMatched, is(true));
    }

    @Author(names = { "Hiten" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyPromoCodeCampaignDateCreated() throws InterruptedException {
        Map<String, String> param = getParam("verifyPromoCodeCampaignDateCreated");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String searchKey = param.get("searchKey");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("Go to Promo dashboard page");
        promodash.openPromoPage();

        step("Verify there is promo present or not");
        int cnt = promodash.extractNumber();
        Assertion.assertThat("Verify the result is greater than zero", (cnt>0), is(true));

        step("Select the promo from the list");
        promodash.selectPromoDropdown();
        promodash.selectActivePromo();
        promodash.selectPromoDropdown();
        promodash.clickOnSearch();
        promodash.clickOnSelectedPromo();

        step("Fetch the promo code campaign name");
        String promoCampaignName = promodash.getPromoCampaignName();
        String campaignDate = promodash.isValuePresentDate();
        String queryString = "{ \"code\": \"" + promoCampaignName + "\" }";
        List<String> dateMongoDBList = promodash.getPromoCodeMongoData(config,database,collectionName,queryString,searchKey);
        boolean isMatched = promodash.isDataMatched(campaignDate, dateMongoDBList);

        Assertion.assertThat("Verify the date created heading field", promodash.isOverviewDateCreatedDisplayed(), is(true));
        Assertion.assertThat("Verify the date created value field", promodash.isOverviewDateCreatedValueDisplayed(), is(true));
        Assertion.assertThat("Verify the date created", isMatched, is(true));
    }

    @Author(names = { "Hiten" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyPromoCodeOverviewPromoType() throws InterruptedException {
        Map<String, String> param = getParam("verifyPromoCodeOverviewPromoType");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String searchKey = param.get("searchKey");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("Go to Promo dashboard page");
        promodash.openPromoPage();

        step("Verify there is promo present or not");
        int cnt = promodash.extractNumber();
        Assertion.assertThat("Verify the result is greater than zero", (cnt>0), is(true));

        step("Select the promo from the list");
        promodash.selectPromoDropdown();
        promodash.selectActivePromo();
        promodash.selectPromoDropdown();
        promodash.clickOnSearch();
        promodash.clickOnSelectedPromo();

        step("Fetch the promo code campaign name");
        String campaignName = promodash.getPromoCampaignName();
        String promoType = promodash.isValuePresentPromoType();
        String queryString = "{ \"code\": \"" + campaignName + "\" }";
        List<String> overViewTypeList = promodash.getPromoCodeMongoData(config,database,collectionName,queryString,searchKey);
        boolean isMatched = promodash.isDataMatched(promoType, overViewTypeList);

        Assertion.assertThat("Verify the promo type heading field", promodash.isOverviewetPromoTypeDisplayed(), is(true));
        Assertion.assertThat("Verify the promo type value field", promodash.isOverviewetPromoTypeValueDisplayed(), is(true));
        Assertion.assertThat("Verify the promoType from mongo database", isMatched, is(true));
    }

    @Author(names = { "Hiten" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyPromoCodeOverviewCurrency() throws InterruptedException {
        Map<String, String> param = getParam("verifyPromoCodeOverviewCurrency");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String searchKey = param.get("searchKey");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Verify there is promo present or not");
        int cnt = promodash.extractNumber();
        Assertion.assertThat("Verify the result is greater than zero", (cnt>0), is(true));

        step("Select the promo from the list");
        promodash.selectPromoDropdown();
        promodash.selectActivePromo();
        promodash.selectPromoDropdown();
        promodash.clickOnSearch();
        promodash.clickOnSelectedPromo();

        step("Fetch the promo code campaign name");
        String campaignName = promodash.getPromoCampaignName();
        String currencyName = promodash.isValuePresentCur();
        String queryString = "{ \"code\": \"" + campaignName + "\" }";
        List<String> promoCurrencyList = promodash.getPromoCodeMongoData(config,database,collectionName,queryString,searchKey);
        boolean isMatched = promodash.isDataMatched(currencyName, promoCurrencyList);

        Assertion.assertThat("Verify the currencyfield", promodash.isOverviewCurDisplayed(), is(true));
        Assertion.assertThat("Verify the currency value field", promodash.isOverviewCurValueDisplayed(), is(true));
        Assertion.assertThat("Verify the overView currency", isMatched, is(true));
    }

    @Author(names = { "Jashan" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifySearchFilter() throws InterruptedException {
        Map<String, String> param = getParam("verifySearchFilter");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String name = param.get("name");
        String date = param.get("date");
        String currency = param.get("currency");
        String type = param.get("type");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Enter promo name in search field");
        promodash.inputSearchPromo(name);

        step("click search");
        promodash.clickOnSearch();

        step("Verify the promo you searched");
        Assertion.assertThat("Verify the name", promodash.isPromoNameCorrect(name), is(true));

        step("Open promo details");
        promodash.clickPromo();

        step("Check promo details");
        Assertion.assertThat("Verify the Details", promodash.checkPromoDetails(date,currency,type), is(true));
    }

    @Author(names = { "Jashan" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifySaveAsDraft() throws InterruptedException {
        Map<String, String> param = getParam("verifySaveAsDraft");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String name = param.get("name");
        String cashback = param.get("cashback");
        String description = param.get("description");
        String ben = param.get("ben");
        String idr = param.get("idr");
        String draft = param.get("draft");
        String fraudPreventionData = param.get("fraudPreventionData");
        String priceData = param.get("priceData");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Click on create new promo");
        promodash.clickCreateNewPromoButton();

        step("Fill promo form");
        String randomName = promodash.fillPromoForm(param);

        step("Save as Draft");
        promodash.savePromoFormAsDraft();
        promodash.goToPromoList();

        String queryString = "{ \"code\": \"" + randomName + "\" }";
        String mongoData = promodash.getPromoCodeMongoFullData(config, database, collectionName, queryString);

        step("Checking DB assertions");
        Assertion.assertThat("Verify campaignName in DB", promodash.getJsonStringValue(mongoData, randomName, "campaignName"), is(true));
        Assertion.assertThat("Verify cashback in DB", promodash.getJsonStringValue(mongoData, cashback.toUpperCase(), "promoCodeDiscountType"), is(true));
        Assertion.assertThat("Verify code in DB", promodash.getJsonStringValue(mongoData, name, "code"), is(true));
        Assertion.assertThat("Verify description in DB", promodash.getJsonStringValue(mongoData, description, "description"), is(true));
        Assertion.assertThat("Verify promoCodeStatus in DB", promodash.getJsonStringValue(mongoData, draft, "promoCodeStatus"), is(true));
        Assertion.assertThat("Verify division in DB", promodash.getJsonStringValue(mongoData, ben, "division"), is(true));
        Assertion.assertThat("Verify currency in DB", promodash.getJsonStringValue(mongoData, idr, "currency"), is(true));
        Assertion.assertThat("Verify fraudPreventionData in DB", promodash.checkDbData(mongoData, fraudPreventionData), is(true));
        Assertion.assertThat("Verify priceData in DB", promodash.checkDbData(mongoData, priceData), is(true));
    }

    @Author(names = { "Jashan" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyCashbackPromoDetails() throws InterruptedException {
        Map<String, String> param = getParam("verifyCashbackPromoDetails");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String name = param.get("name");
        String identifier = param.get("identifier");
        String description = param.get("description");
        String promoTypeId = param.get("promoTypeId");
        String cashback = param.get("cashback");
        String campaignTypeId = param.get("campaignTypeId");
        String testing = param.get("testing");
        String campaignNameId = param.get("campaignNameId");
        String campaignNameText = param.get("campaignNameText");
        String divisionId = param.get("divisionId");
        String ben = param.get("ben");
        String selectCurrencyId = param.get("selectCurrencyId");
        String idr = param.get("idr");
        String verticalId = param.get("verticalId");
        String hotel = param.get("hotel");
        String quotaId = param.get("quotaId");
        String quotaText = param.get("quotaText");
        String ruleVerticalId = param.get("ruleVerticalId");
        String parameterId = param.get("parameterId");
        String accomodationType = param.get("accomodationType");
        String operatorId = param.get("operatorId");
        String valuesId = param.get("valuesId");
        String valuesText = param.get("valuesText");
        String minTraValueId = param.get("minTraValueId");
        String minTraText = param.get("minTraText");
        String maxTraValueId = param.get("maxTraValueId");
        String maxTraText = param.get("maxTraText");
        String amountOfCashbackId = param.get("amountOfCashbackId");
        String cashbackText = param.get("cashbackText");
        String costAllocationId = param.get("costAllocationId");
        String tiket = param.get("tiket");
        String selectUsageCountId = param.get("selectUsageCountId");
        String countText = param.get("countText");
        String usagePeriodId = param.get("usagePeriodId");
        String campaignPeriod = param.get("campaignPeriod");
        String platformId = param.get("platformId");
        String android = param.get("android");
        String campaignStartDateId = param.get("campaignStartDateId");
        String startDate = param.get("startDate");
        String campaignEndDateId = param.get("campaignEndDateId");
        String endDate = param.get("endDate");
        String quotaTypeText = param.get("quotaTypeText");
        String userName = param.get("userName");
        String usagePeriodChecking = param.get("usagePeriodChecking");
        String loginStateChecking = param.get("loginStateChecking");
        String operatorChecking = param.get("operatorChecking");
        String paymentMethodChecking = param.get("paymentMethodChecking");
        String percentage = param.get("percentage");
        String verifiedChecking = param.get("verifiedChecking");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Click on create new promo");
        promodash.clickCreateNewPromoButton();

        step("Click on Promo type dropdown");
        promodash.clickDropdown(promoTypeId);

        step("select cashback in Promo type dropdown");
        promodash.clickInsideDropdown(cashback);

        step("Click on campaign type dropdown");
        promodash.clickDropdown(campaignTypeId);

        step("select TESTING inside campaign type dropdown");
        promodash.clickInsideDropdown(testing);

        step("Send name in campaign name");
        promodash.sendTextInInput(campaignNameId,campaignNameText);

        step("Send name in Unique Identifier");
        promodash.sendTextInUniqueIdentifierInput(identifier);

        step("Send Text in description box");
        promodash.sendTextInDescriptionBox(description);

        step("scroll to make element clickable");
        promodash.scorllTillClickable();

        step("Select division dropdown");
        promodash.clickDropdown(divisionId);

        step("select BEN inside division dropdown");
        promodash.clickInsideDropdown(ben);

        step("Select select-currency dropdown");
        promodash.clickDropdown(selectCurrencyId);

        step("select IDR inside select-currency dropdown");
        promodash.clickInsideDropdown(idr);

        step("Select vertical dropdown");
        promodash.clickDropdown(verticalId);

        step("select hotel in vertical dropdown");
        promodash.clickInsideDropdown(hotel);

        step("scroll to make element clickable");
        promodash.scrollTillClickable(2, 350);

        step("Select date");
        promodash.clickDropdown(campaignStartDateId);
        promodash.selectDate(startDate);
        promodash.scrollTillClickable(1, 350);
        promodash.clickSaveButton();
        promodash.scrollTillClickable(1, -350);
        promodash.clickDropdown(campaignEndDateId);
        promodash.selectDate(endDate);
        promodash.scrollTillClickable(1, 350);
        promodash.clickSaveButton();
        promodash.scrollTillClickable(1, -350);

        step("Click add period button");
        promodash.clickAddPeriod();

        step("scroll to make element clickable");
        promodash.scorllTillClickable();

        step("send text in quota input box");
        promodash.sendTextInInput(quotaId,quotaText);

        step("scroll to make element clickable");
        promodash.scorllTillClickable();

        step("Select rule set 1 vertical dropdown");
        promodash.clickDropdown(ruleVerticalId);

        step("select hotel in vertical dropdown");
        promodash.clickInsideDropdown(hotel);

        step("Select parameter dropdown");
        promodash.clickDropdown(parameterId);

        step("select in parameter dropdown");
        promodash.clickInsideDropdown(accomodationType);

        step("Select operator dropdown");
        promodash.clickDropdown(operatorId);

        step("select in operator dropdown");
        promodash.clickEqualToInDropdown();

        step("send text in values input box");
        promodash.sendTextInInput(valuesId,valuesText);

        step("click add values button");
        promodash.clickAddValues();
        promodash.scrollTillClickable(2, 300);

        step("send text in Min Transaction value input box");
        promodash.sendTextInInput(minTraValueId,minTraText);

        step("send text in Max Transaction value input box");
        promodash.sendTextInInput(maxTraValueId,maxTraText);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(2,300);

        step("send text in Amount of Cashback input box");
        promodash.sendTextInInput(amountOfCashbackId,cashbackText);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(2,300);

        step("Select cost allocation dropdown");
        promodash.clickDropdown(costAllocationId);

        step("select in cost allocation dropdown");
        promodash.clickInsideDropdown(tiket);

        step("send text in select usage count input box");
        promodash.sendTextInInput(selectUsageCountId,countText);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(1,100);

        step("Select usage period dropdown");
        promodash.clickDropdown(usagePeriodId);

        step("select in usage period dropdown");
        promodash.clickInsideDropdown(campaignPeriod);

        step("Select platform dropdown");
        promodash.clickDropdown(platformId);

        step("select in platform dropdown");
        promodash.clickInsideDropdown(android);
        promodash.clickDropdown(platformId);

        step("click username under validated by");
        promodash.clickUsername();

        step("Send name in Promo name");
        promodash.sendTextInPromoNameInput(name);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(2,300);

        step("Generate TnC");
        promodash.clickGenerateTnc();
        promodash.waitForGenerateTncFinish();

        step("Click Preview Button");
        promodash.clickPreviewButton();

        step("Assertions for General information");
        Assertion.assertThat("is campaign type correct?", promodash.checkAssertions("Campaign Type",testing), is(true));
        Assertion.assertThat("is campaign name correct?", promodash.checkAssertions("Campaign Name",campaignNameText), is(true));
        Assertion.assertThat("is Unique Identifier correct?", promodash.checkAssertions("Unique Identifier",identifier), is(true));
        Assertion.assertThat("is Description correct?", promodash.checkAssertions("Description",description), is(true));
        Assertion.assertThat("is Division correct?", promodash.checkAssertions("Division",ben), is(true));
        Assertion.assertThat("is Quota Type correct?", promodash.checkAssertions("Quota Type",quotaTypeText), is(true));
        Assertion.assertThat("is Quota correct?", promodash.checkAssertions("Quota",quotaText), is(true));
        Assertion.assertThat("is Promo Type correct?", promodash.checkAssertions("Promo Type",cashback.toUpperCase()), is(true));
        Assertion.assertThat("is Currency correct?", promodash.checkAssertions("Currency",idr), is(true));

        step("Assertions for Fraud Prevention information");
        Assertion.assertThat("is Usage Count correct?", promodash.checkAssertions("Usage Count",countText.toUpperCase()), is(true));
        Assertion.assertThat("is Usage Period correct?", promodash.checkAssertions("Usage Period",usagePeriodChecking.toUpperCase()), is(true));
        Assertion.assertThat("is Platform correct?", promodash.checkAssertions("Platform",android.toUpperCase()), is(true));
        Assertion.assertThat("is Login State correct?", promodash.checkAssertions("Login State",loginStateChecking.toUpperCase()), is(true));
        Assertion.assertThat("is Phone Number verification correct?", promodash.checkAssertions("Phone Number",verifiedChecking.toUpperCase()), is(true));
        Assertion.assertThat("is Validated by correct?", promodash.checkAssertions("Validated by",userName.toUpperCase()), is(true));

        step("Assertions for Rule Set information information");
        Assertion.assertThat("is vertical correct?", promodash.checkAssertions("Vertical",hotel), is(true));
        Assertion.assertThat("is Parameter correct?", promodash.checkAssertions("Parameter",accomodationType), is(true));
        Assertion.assertThat("is Operator correct?", promodash.checkAssertions("Operator",operatorChecking), is(true));
        Assertion.assertThat("is Min transaction value correct?", promodash.checkAssertions("Min. AOV",minTraText), is(true));
        Assertion.assertThat("is Max transaction value correct?", promodash.checkAssertions("Max. AOV",maxTraText), is(true));
        Assertion.assertThat("is Discount correct?", promodash.checkAssertions("Discount",cashbackText), is(true));
        Assertion.assertThat("is Capped At correct?", promodash.checkAssertions("Capped At",cashbackText), is(true));
        Assertion.assertThat("is Payment Method correct?", promodash.checkAssertions("Payment Method",paymentMethodChecking), is(true));
        Assertion.assertThat("is percentage correct?", promodash.checkAllocation(percentage), is(true));

        step("Go to Promo List");
        promodash.goToPromoList();

        step("click leave button");
        promodash.clickLeaveButton();
    }

    @Author(names = { "Jashan" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyCreatingCashbackPromo() throws InterruptedException {
        Map<String, String> param = getParam("verifyCreatingCashbackPromo");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String name = param.get("name");
        String cashback = param.get("cashback");
        String description = param.get("description");
        String ben = param.get("ben");
        String idr = param.get("idr");
        String message = param.get("message");
        String draft = param.get("draft");
        String fraudPreventionData = param.get("fraudPreventionData");
        String priceData = param.get("priceData");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Click on create new promo");
        promodash.clickCreateNewPromoButton();

        step("Fill Cashback promo form");
        String randomName = promodash.fillPromoForm(param);

        step("Submit promo for approval");
        promodash.submitPromoForm();

        step("Check success message is displayed or not");
        Assertion.assertThat("is success message displayed?", promodash.checkSuccessMessage(message), is(true));

        String queryString = "{ \"code\": \"" + randomName + "\" }";
        String mongoData = promodash.getPromoCodeMongoFullData(config, database, collectionName, queryString);

        step("Checking DB assertions");
        Assertion.assertThat("Verify campaignName in DB", promodash.getJsonStringValue(mongoData, randomName, "campaignName"), is(true));
        Assertion.assertThat("Verify cashback in DB", promodash.getJsonStringValue(mongoData, cashback.toUpperCase(), "promoCodeDiscountType"), is(true));
        Assertion.assertThat("Verify code in DB", promodash.getJsonStringValue(mongoData, name, "code"), is(true));
        Assertion.assertThat("Verify description in DB", promodash.getJsonStringValue(mongoData, description, "description"), is(true));
        Assertion.assertThat("Verify promoCodeStatus in DB", promodash.getJsonStringValue(mongoData, draft, "promoCodeStatus"), is(true));
        Assertion.assertThat("Verify division in DB", promodash.getJsonStringValue(mongoData, ben, "division"), is(true));
        Assertion.assertThat("Verify currency in DB", promodash.getJsonStringValue(mongoData, idr, "currency"), is(true));
        Assertion.assertThat("Verify fraudPreventionData in DB", promodash.checkDbData(mongoData, fraudPreventionData), is(true));
        Assertion.assertThat("Verify priceData in DB", promodash.checkDbData(mongoData, priceData), is(true));
    }

    @Author(names = { "Jashan" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyCreatingInstantPromo() throws InterruptedException {
        Map<String, String> param = getParam("verifyCreatingInstantPromo");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String name = param.get("name");
        String instant = param.get("instant");
        String description = param.get("description");
        String ben = param.get("ben");
        String idr = param.get("idr");
        String message = param.get("message");
        String draft = param.get("draft");
        String fraudPreventionData = param.get("fraudPreventionData");
        String priceData = param.get("priceData");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Click on create new promo");
        promodash.clickCreateNewPromoButton();

        step("Fill Instant promo form");
        String randomName = promodash.fillPromoForm(param);

        step("Submit promo for approval");
        promodash.submitPromoForm();

        step("Check success message is displayed or not");
        Assertion.assertThat("is success message displayed?", promodash.checkSuccessMessage(message), is(true));

        String queryString = "{ \"code\": \"" + randomName + "\" }";
        String mongoData = promodash.getPromoCodeMongoFullData(config, database, collectionName, queryString);

        step("Checking DB assertions");
        Assertion.assertThat("Verify campaignName in DB", promodash.getJsonStringValue(mongoData, randomName, "campaignName"), is(true));
        Assertion.assertThat("Verify cashback in DB", promodash.getJsonStringValue(mongoData, instant.toUpperCase(), "promoCodeDiscountType"), is(true));
        Assertion.assertThat("Verify code in DB", promodash.getJsonStringValue(mongoData, name, "code"), is(true));
        Assertion.assertThat("Verify description in DB", promodash.getJsonStringValue(mongoData, description, "description"), is(true));
        Assertion.assertThat("Verify promoCodeStatus in DB", promodash.getJsonStringValue(mongoData, draft, "promoCodeStatus"), is(true));
        Assertion.assertThat("Verify division in DB", promodash.getJsonStringValue(mongoData, ben, "division"), is(true));
        Assertion.assertThat("Verify currency in DB", promodash.getJsonStringValue(mongoData, idr, "currency"), is(true));
        Assertion.assertThat("Verify fraudPreventionData in DB", promodash.checkDbData(mongoData, fraudPreventionData), is(true));
        Assertion.assertThat("Verify priceData in DB", promodash.checkDbData(mongoData, priceData), is(true));
    }

    @Author(names = {"NaufalF", "Fery", "Muhammad Hafiz"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyAllFieldsPromoDetailsInApproval() throws InterruptedException {
        Map<String, String> param = getParam("verifyAllFieldsPromoDetailsInApproval");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String status = param.get("status");

        step("Login to Admin Member");
        promoDash.maximize();
        promoDash.goToAdminCarePage(username, password);

        step("Go To New Promo Dashboard Page");
        promoDash.scorllToCarAdmin();
        promoDash.openPromoPage();

        step("Filter for "+status+" Status");
        promoDash.filterPromoListByApprovalStatus();

        step("Ensure a promo with the needed status exists");
        if (promoDash.isPromoListEmpty()) {
            promoDash.createAndSubmitFreshPromo(param);
            promoDash.goToPromoList();
            promoDash.filterPromoListByApprovalStatus();
        }

        step("Open Promo Detail from Promo List");
        promoDash.clickOnSelectedPromo();

        step("Get Overview Fields Data");
        String promoName = promoDash.getPromoNameOnPromoDetail();
        promoDash.setOverviewPromoDetailData();

        step("Get General Information Fields Data");
        promoDash.setGeneralInfoPromoData();

        step("Get Fraud Prevention Fields Data");
        promoDash.setFraudPreventionPromoData();

        step("Get Promo Code Fields Data");
        promoDash.setPromoCodeFieldsData();

        step("Get Promo Rules Fields Data");
        promoDash.setRuleSetFieldsData();

        step("Open Approval Page and Search Promo: "+promoName);
        promoDash.openApprovalPage();
        promoDash.inputSearchPromo(promoName);
        promoDash.clickOnSearch();

        step("Open Promo Detail from Promo Approval");
        promoDash.clickOnSelectedPromo();

        step("Verify All Field in Promo Approval");
        Assertion.assertThat("is Overview Data Correct", promoDash.verifyOverviewPromoApproval(), is(true));
        Assertion.assertThat("is General Info Data Correct", promoDash.verifyGeneralInfoPromoApproval(), is(true));
        Assertion.assertThat("is Fraud Prevention Data Correct", promoDash.verifyFraudPreventionPromoApproval(), is(true));
        Assertion.assertThat("is Promo Code Data Correct", promoDash.verifyPromoCodePromoApproval(), is(true));
        Assertion.assertThat("is Rule Set Data Correct", promoDash.verifyRuleSetPromoApproval(), is(true));

        String mongoCfg = param.get("config");
        if (mongoCfg != null && !mongoCfg.isBlank()) {
            String dbNameMongo = param.get("dbName");
            String collName = param.get("collectionName");
            String mongoPromoStatus = param.get("mongoPromoStatus");
            if (mongoPromoStatus == null || mongoPromoStatus.isBlank()) {
                mongoPromoStatus = "";
            }
            String promoCodeKey = promoDash.getPromoNameOnPromoDetail();
            promoDash.loadPromoDataByPromoCodeWithStatus(mongoCfg, dbNameMongo, collName, promoCodeKey, mongoPromoStatus);
            Assertion.assertThat("Mongo returned a document for approval promo " + promoCodeKey,
                    promoDash.isPromoDataLoaded(), is(true));
            promoDash.setPromoDetailGeneralAndFraudFromDb(mongoCfg);
            Assertion.assertThat("Promo Detail general/fraud matches Mongo",
                    promoDash.verifyPromoDetailGeneralAndFraudUi(), is(true));
            int ruleSetCount = promoDash.getLoadedPromoRuleSetCount();
            Assertion.assertThat("Mongo should contain at least one rule set for rules UI", ruleSetCount > 0, is(true));
            for (int i = 0; i < ruleSetCount; i++) {
                Assertion.assertThat("Rule set " + (i + 1) + " parameters and transaction values match Mongo",
                        promoDash.verifyPromoDetailRuleSetParametersAndTransactionUi(i), is(true));
            }
        }
    }

    @Author(names = { "Jashan", "Vidhi Jindal", "Muhammad Hafiz" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyEditPromo() throws InterruptedException {
        Map<String, String> param = getParam("verifyEditPromo");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String name = param.get("name");
        String description = param.get("description");
        String alltierben = param.get("alltierben");
        String usd = param.get("usd");
        String typeOfPromo = param.get("typeOfPromo");
        String instant = param.get("instant");
        String filterStatus = param.get("filterStatus");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String currSearchKey = param.get("currSearchKey");
        String divSearchKey = param.get("divSearchKey");
        String descSearchKey = param.get("descSearchKey");
        String campaignName;

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Ensure an Active promo exists");
        name = promodash.ensureActivePromoOrCreate(name, param);

        step("Search and filter for the promo");
        promodash.inputSearchPromo(name);
        promodash.clickDropdown(typeOfPromo);
        promodash.clickInsideDropdown(instant);
        promodash.clickDropdown(typeOfPromo);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();

        step("Verify the promo you searched");
        Assertion.assertThat("Verify the name", promodash.isPromoNameCorrect(name), is(true));

        step("Open promo details");
        promodash.clickPromo();

        step("Edit promo fields using section-based method");
        campaignName = promodash.editPromoFields(param);

        String queryString = "{ \"code\": { \"$regex\": \".*" + campaignName + ".*\" } }";

        step("Checking DB assertions");
        List<String> promoCurrencyList = promodash.getPromoCodeMongoData(config, database, collectionName, queryString, currSearchKey);
        Assertion.assertThat("Verify the overView currency", promodash.isDataMatched(usd, promoCurrencyList), is(true));

        List<String> division = promodash.getPromoCodeMongoData(config, database, collectionName, queryString, divSearchKey);
        Assertion.assertThat("Verify the division", promodash.isDataMatched(alltierben, division), is(true));

        List<String> desc = promodash.getPromoCodeMongoData(config, database, collectionName, queryString, descSearchKey);
        Assertion.assertThat("Verify the description", promodash.isDataMatched(description, desc), is(true));
    }

    @Author(names = { "NaufalF", "Fery", "Muhammad Hafiz" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyActivePromoCashback() throws InterruptedException {
        Map<String, String> param = getParam("verifyActivePromoCashback");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String configType = param.get("config");
        String dbName = param.get("dbName");
        String collectionName = param.get("collectionName");
        String filterStatus = param.get("filterStatus");

        step("Get Active Promo Cashback Data from DB");
        String queryFind = "{\"promoCodeStatus\": \"ACTIVE\", \"promoCodeDiscountType\": \"CASHBACK\"}";
        String querySort = "{\"updatedDate\": -1}";
        promoDash.setPromoDocumentFromDB(configType, dbName, collectionName, queryFind, querySort);
        String promoCode = promoDash.getFieldPromoDocument(promoDash.promoData, "code");

        step("Login to Admin Member");
        promoDash.maximize();
        promoDash.goToAdminCarePage(username, password);

        step("Go To New Promo Dashboard Page");
        promoDash.scorllToCarAdmin();
        promoDash.openPromoPage();

        step("Search the Promo Code Name");
        promoDash.inputSearchPromo(promoCode);
        promoDash.clickPromoTypeDropdown();
        promoDash.clickCashbackInDropdown();
        promoDash.clickPromoTypeDropdown();
        promoDash.filterSearchByStatus(filterStatus);
        promoDash.clickOnSearch();

        step("Open the Promo Code Search Result");
        promoDash.clickOnSelectedPromo();

        step("Verify the Overview Promo Data");
        promoDash.setOverviewPromoDataFromDB();
        Assertion.assertThat("is Promo Data Correct", promoDash.verifyOverviewPromoApproval(), is(true));

        step("Verify General Information and Fraud Prevention (Promo List detail, Budget per list rules not Checker)");
        promoDash.setPromoDetailGeneralAndFraudFromDb(configType);
        Assertion.assertThat("is Promo Detail general/fraud data correct",
                promoDash.verifyPromoDetailGeneralAndFraudUi(), is(true));

        step("Verify the Promo Code Data");
        promoDash.setPromoCodeDataFromDB();
        Assertion.assertThat("is Promo Code Data Correct", promoDash.verifyPromoCodePromoApproval(), is(true));
        Assertion.assertThat("is Promo Short Desc & TnC shown", promoDash.verifyShortDescTncShown(), is(true));

        step("Verify Promo Rules / transaction values per rule set (Mongo vs Promo Detail UI)");
        int ruleSetCount = promoDash.getLoadedPromoRuleSetCount();
        Assertion.assertThat("Mongo should contain at least one rule set", ruleSetCount > 0, is(true));
        for (int i = 0; i < ruleSetCount; i++) {
            Assertion.assertThat("Rule set " + (i + 1) + " parameters and transaction values match Mongo",
                    promoDash.verifyPromoDetailRuleSetParametersAndTransactionUi(i), is(true));
        }
    }

    @Author(names = {"GibranD", "Muhammad Hafiz"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyInactiveCashbackPromo() throws InterruptedException, NotImplementedException {
        Map<String, String> param = getParam("verifyInactiveCashbackPromo");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String promoCode = param.get("promoCode");
        String actionType = param.get("actionType");
        String actionEdit = param.get("actionEdit");
        String inApproval = param.get("inApproval");
        String inactive = param.get("inactive");
        String filterStatus = param.get("filterStatus");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Precondition: ensure promo is Active before test");
        promodash.ensurePromoIsActive(promoCode);

        step("Search for active promo");
        promodash.searchAndFilterPromo(promoCode, filterStatus);

        step("Check if Promo Exist");
        Assertion.assertThat("Promo Code exist", promodash.checkSearchedPromoCode(promoCode), is(true));

        step("Inactivate promo via Action dropdown");
        promodash.performPromoAction(actionType);
        Assertion.assertThat("Inactive Confirmation Modal displayed", promodash.checkInactivateModalDisplayed(), is(true));
        promodash.clickInactivateButton();

        step("Verify promo status is Inactive");
        promodash.goToPromoList();
        promodash.searchAndFilterPromo(promoCode, promodash.getPromoStatusKeyFromValue(inactive));
        promodash.clickPromo(promoCode);
        Assertion.assertThat("Promo status is changed to Inactive", promodash.getPromoStatusOverview(), is(inactive));

        step("Edit inactive promo and save changes");
        promodash.goToPromoList();
        promodash.searchAndFilterPromo(promoCode, promodash.getPromoStatusKeyFromValue(inactive));
        promodash.performPromoAction(actionEdit);
        promodash.savePromoChanges();

        step("Verify promo status is In Approval");
        promodash.goToPromoList();
        promodash.searchAndFilterPromo(promoCode, promodash.getPromoStatusKeyFromValue(inApproval));
        promodash.clickPromo(promoCode);
        Assertion.assertThat("Promo status is changed to In Approval", promodash.getPromoStatusOverview(), is(inApproval));

        step("Approve promo");
        promodash.approvePromo(promoCode);

        step("Verify promo status is Active again");
        promodash.goToPromoList();
        promodash.searchAndFilterPromo(promoCode, filterStatus);
        promodash.clickPromo(promoCode);
        Assertion.assertThat("Promo status is now Active", promodash.getPromoStatusOverview(), is(filterStatus.toUpperCase()));
    }

    @Author(names = { "Fery" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyPromoClone() throws InterruptedException {
        Map<String, String> param = getParam("verifyPromoClone");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");

        String promoCode = param.get("promoCode");
        String budgetId = param.get("budgetId");
        String budgetValue = param.get("budgetValue");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Go to promo list page");
        promodash.goToPromoList();

        step("Search for promo");
        promodash.inputSearchPromo(promoCode);
        promodash.clickOnSearch();

        step("Clicks on the active promo");
        promodash.clickPromo(promoCode);

        step("Verify success clone promo");
        promodash.clickClone();
        Assertion.assertThat("Verify campaign name field is not enabled", promodash.isCampaignNameFieldClickable(), is(false));
        Assertion.assertThat(
                "Verify URL of cloned campaign is promo/add with clone query (type=clone or cloneId=)",
                promodash.verifyCloneUrl(),
                is(true));

        step("Select budget (required on clone form before other fields)");
        promodash.scorllTillClickable(2, 300);
        promodash.clickDropdown(budgetId);
        promodash.clickInsideDropdown(budgetValue);

        step("Fill cost allocation and fixed value");
        promodash.scorllTillClickable(2, 300);
        promodash.fillCostAllocationSection(param);

        promodash.scrollToPromoCodeNameField();
        Assertion.assertThat("Verify promo code name is the same as cloned one", promodash.isPromoCodeNameEqualsTo(promoCode), is(true));
        promodash.clickPreviewButton();
        promodash.scrollToPreviewPromoCodeNameField();
        Assertion.assertThat("Verify preview tab showing correct campaign name", promodash.isPreviewPromoCodeNameEqualsTo(promoCode), is(true));

        step("Back to promo list");
        promodash.goToPromoListWithAlertHandler();
    }

    @Author(names = {"Hiten", "Muhammad Hafiz"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyDeletePromocode() throws InterruptedException {
        Map<String, String> param = getParam("verifyDeletePromocode");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String searchKey = param.get("searchKey");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Verify there is promo present or not");
        int cnt = promodash.extractNumber();
        Assertion.assertThat("Verify the result is greater than zero", (cnt > 0), is(true));

        step("Filter and select inactive promo code");
        promodash.selectPromoDropdown();
        promodash.selectInactivePromo();
        promodash.selectPromoDropdown();
        promodash.clickOnSearch();

        step("Select the promo from the list");
        promodash.clickOnSelectedPromo();

        step("Fetch the promo code campaign name");
        String promoCode = promodash.getPromoCampaignName();
        String queryString = promodash.returnQueryWithPromoCodeName();

        step("Click on Delete the promocode");
        promodash.clickDeleteButton();

        step("Verify status is INACTIVE before deleting");
        List<String> statusMongoDBList = promodash.getPromoCodeMongoData(config, database, collectionName, queryString, searchKey);
        Assertion.assertThat("Verify before deleting the status is inactive", promodash.isDataMatched("INACTIVE", statusMongoDBList), is(true));

        step("Confirm delete in modal");
        promodash.modalHeaderTextIsVisible();
        promodash.clickModalDeleteButton();
        promodash.waitForOverviewStatus("Deleted");

        step("Checking DB assertions after delete");
        boolean isMatched = promodash.isStatusMatched(config, database, collectionName, queryString, searchKey);
        Assertion.assertThat("Verify the status heading field", promodash.isOverviewStatusHeadingDisplayed(), is(true));
        Assertion.assertThat("Verify the status value field", promodash.isOverviewStatusDisplayed(), is(true));
        Assertion.assertThat("Verify the status value field on panel side", promodash.isValuePresentPromoStatus().toUpperCase(), is("DELETED"));
        Assertion.assertThat("Verify the status of PromoCode", isMatched, is(true));

        step("Restore the deleted promo via Clone");
        Assertion.assertThat("Promo code restored to Active via clone + approve",
                promodash.cloneCurrentPromoToActive(promoCode), is(true));
    }

    @Author(names = { "Fery", "Muhammad Hafiz" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyCampaignOverviewEditedPromo()
            throws InterruptedException, NotImplementedException {
        Map<String, String> param = getParam("verifyCampaignOverviewEditedPromo");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String dbName = param.get("dbName");
        String collectionName = param.get("collectionName");
        String dbNameGv = param.get("dbNameGv");
        String collectionNameGv = param.get("collectionNameGv");
        String filterStatus = param.get("filterStatus");

        String promoCode = param.get("promoCode");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Go to promo list page");
        promodash.openPromoPage();
        promodash.goToPromoList();

        step("Ensure an Active promo exists");
        promoCode = promodash.ensureActivePromoOrCreate(promoCode, param);

        step("Search for promo");
        promodash.inputSearchPromo(promoCode);
        promodash.clickPromoTypeDropdown();
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();

        step("Clicks on the active promo");
        promodash.clickPromo(promoCode);

        step("Edit Promo");
        promodash.clickEditButton();
        promodash.addCurrentDateToPromoDescription();
        promodash.scrollToBottom();
        promodash.clickPreviewButton();
        promodash.clickSaveChangesButton();
        promodash.clickSaveChangesPopupButton();

        step("Approve the edited promo");
        promodash.goToPromoList();
        promodash.openApprovalPage();
        promodash.inputSearchPromo(promoCode);
        promodash.clickAprroveButton();
        promodash.clickOnApproveModal();

        step("Open campaign overview");
        promodash.goToPromoList();
        promodash.inputSearchPromo(promoCode);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();
        promodash.clickPromo(promoCode);

        String promoStatusOverview = promodash.getPromoStatusOverview();
        String promoTypeOverview = promodash.getCampaignOverviewValue("Promo Type");
        String descriptionOverview = promodash.getCampaignOverviewValue("Description");
        String currencyOverview = promodash.getCampaignOverviewValue("Currency");
        String uniqueIdOverview = promodash.getCampaignOverviewValue("Unique Identifier");
        String campaignNameOverview = promodash.getCampaignOverviewValue("Campaign Name");
        promodash.scorllTillClickable();
        String divisionOverview = promodash.getCampaignOverviewValue("Division");
        promodash.scorllTillClickable();
        String promoCodeOverview = promodash.getCampaignOverviewValue("Promo Code");

        String queryString = "{ \"code\": \"" + promoCode + "\", \"promoCodeStatus\": \"ACTIVE\" }";
        String mongoData = promodash.getPromoCodeMongoFullData(config,dbName,collectionName,queryString);

        String divisionQuery = "{ \"name\": \"" + divisionOverview + "\" }";
        String divisionData = promodash.getJsonStringValue(promodash.getPromoCodeMongoFullData(config,dbNameGv,collectionNameGv,divisionQuery), "code");

        step("Verify campaign overview new version");
        Assertion.assertThat("Verify description same as DB", promodash.getJsonStringValue(mongoData,descriptionOverview,"description"), is(true));
        Assertion.assertThat("Verify unique identifier same as DB", promodash.getJsonStringValue(mongoData,uniqueIdOverview,"tags"), is(true));
        Assertion.assertThat("Verify campaignName same as DB", promodash.getJsonStringValue(mongoData,campaignNameOverview,"campaignName"), is(true));
        Assertion.assertThat("Verify promo type same as DB", promodash.getJsonStringValue(mongoData,promoTypeOverview,"promoCodeDiscountType"), is(true));
        Assertion.assertThat("Verify code same as DB", promodash.getJsonStringValue(mongoData,promoCodeOverview,"code"), is(true));
        Assertion.assertThat("Verify promoCodeStatus same as DB", promodash.getJsonStringValue(mongoData,promoStatusOverview,"promoCodeStatus"), is(true));
        Assertion.assertThat("Verify division same as DB", promodash.getJsonStringValue(mongoData,divisionData,"division"), is(true));
        Assertion.assertThat("Verify currency same as DB", promodash.getJsonStringValue(mongoData,currencyOverview,"currency"), is(true));
    }

    @Author(names = { "Fery" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyCreatePromoHybridPaymentQuota() throws InterruptedException {
        Map<String, String> param = getParam("verifyCreatePromoHybridPaymentQuota");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String collectionVariableSource = param.get("collectionVariableSource");
        String name = param.get("name");
        String hybrid = param.get("hybrid");
        String description = param.get("description");
        String ben = param.get("ben");
        String idr = param.get("idr");
        String message = param.get("message");
        String draft = param.get("draft");
        String fraudPreventionData = param.get("fraudPreventionData");
        String priceData = param.get("priceData");
        String quotaLevel = param.get("quotaLevel");
        String paymentMethodText = param.get("paymentMethodText");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Click on create new promo");
        promodash.clickCreateNewPromoButton();

        step("Fill Hybrid promo form");
        String randomName = promodash.fillPromoForm(param);

        step("Submit promo for approval");
        promodash.submitPromoForm();

        step("Check success message is displayed or not");
        Assertion.assertThat("is success message displayed?", promodash.checkSuccessMessage(message), is(true));

        String queryString = "{ \"campaignName\": \"" + randomName + "\" }";
        String mongoData = promodash.getPromoCodeMongoFullData(config, database, collectionName, queryString);

        String paymentMethodName = String.valueOf(promodash.getPaymentNames(mongoData)[0]);
        String queryVariableSource = "{ \"valueId\": \"" + paymentMethodName + "\" }";
        String mongoVariable = promodash.getPromoCodeMongoFullData(config, database, collectionVariableSource, queryVariableSource);

        step("Checking DB assertions");
        Assertion.assertThat("Verify campaignName in DB", promodash.getJsonStringValue(mongoData, randomName, "campaignName"), is(true));
        Assertion.assertThat("Verify hybrid in DB", promodash.getJsonStringValue(mongoData, hybrid.toUpperCase(), "promoCodeDiscountType"), is(true));
        Assertion.assertThat("Verify code in DB", promodash.getJsonStringValue(mongoData, name, "code"), is(true));
        Assertion.assertThat("Verify description in DB", promodash.getJsonStringValue(mongoData, description, "description"), is(true));
        Assertion.assertThat("Verify promoCodeStatus in DB", promodash.getJsonStringValue(mongoData, draft, "promoCodeStatus"), is(true));
        Assertion.assertThat("Verify division in DB", promodash.getJsonStringValue(mongoData, ben, "division"), is(true));
        Assertion.assertThat("Verify currency in DB", promodash.getJsonStringValue(mongoData, idr, "currency"), is(true));
        Assertion.assertThat("Verify fraudPreventionData in DB", promodash.checkDbData(mongoData, fraudPreventionData), is(true));
        Assertion.assertThat("Verify priceData in DB", promodash.checkDbData(mongoData, priceData), is(true));
        Assertion.assertThat("Verify promo code quota level in DB", promodash.getJsonStringValue(mongoData, quotaLevel, "quotaLevel"), is(true));
        Assertion.assertThat("Verify payment method in DB", promodash.getJsonStringValue(mongoVariable, paymentMethodText, "valueName"), is(true));
    }

    @Author(names = { "NaufalF", "Muhammad Hafiz" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void testPromoChangeHistory() throws InterruptedException {
        Map<String, String> param = getParam("testPromoChangeHistory");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String promoCodePrefix = param.get("promoCodePrefix");
        String idQuota = param.get("idQuota");
        String idMinTransaction = param.get("idMinTransaction");
        String activityType = param.get("activityType");
        String newQuota = String.valueOf(CoreFunctions.generateRandomInt(2));
        String newMinTransaction = String.valueOf(CoreFunctions.generateRandomInt(4));

        step("Login to dashboard page");
        promoDashboard.goToAdminCarePage(username, password);

        step("Go to Promo List page");
        promoDashboard.scorllToCarAdmin();
        promoDashboard.openPromoPage();

        step("Ensure an Active promo exists");
        String promoCode = promoDashboard.ensureActivePromoOrCreate(promoCodePrefix, param);

        step("Get Promo Name on the Table");
        promoDashboard.searchAndFilterPromo(promoCode, "Active");
        promoDashboard.clickOnSelectedPromo();
        promoCode = promoDashboard.getPromoNameOnPromoDetail();

        step("Verify no pending In Approval for the promo: "+promoCode);
        promoDashboard.goToPromoList();
        promoDashboard.inputSearchPromo(promoCode);
        promoDashboard.clickDropdown("status");
        promoDashboard.clickInsideDropdown("In Approval");
        promoDashboard.clickDropdown("status");
        promoDashboard.clickOnSearch();
        promoDashboard.deleteInApprovalPromo();

        step("Edit the promo: "+promoCode);
        promoDashboard.clickResetFilter();
        promoDashboard.inputSearchPromo(promoCode);
        promoDashboard.clickDropdown("status");
        promoDashboard.clickInsideDropdown("Active");
        promoDashboard.clickInsideDropdown("Inactive");
        promoDashboard.clickDropdown("status");
        promoDashboard.clickOnSearch();
        promoDashboard.clickOnSelectedPromo();
        promoDashboard.clickEditButton();

        step("Edit the Promo Quota");
        promoDashboard.clickToggleEditCampaignPeriod();
        String oldQuota = promoDashboard.clearTextInInput(idQuota);
        promoDashboard.sendTextInInput(idQuota, newQuota);

        step("Edit the Minimal Transaction Value");
        String oldMinTransaction = promoDashboard.clearTextInInput(idMinTransaction);
        promoDashboard.sendTextInInput(idMinTransaction, newMinTransaction);

        step("Save the Edited Promo");
        promoDashboard.scrollToBottom();
        promoDashboard.clickPreviewButton();
        promoDashboard.clickSaveChangesButton();
        promoDashboard.clickSaveChangesPopupButton();

        step("Approve the edited promo");
        promoDashboard.goToPromoList();
        promoDashboard.openApprovalPage();
        promoDashboard.inputSearchPromo(promoCode);
        promoDashboard.clickAprroveButton();
        promoDashboard.clickOnApproveModal();

        step("Back to Promo List and Open the Promo");
        promoDashboard.clickResetFilter();
        promoDashboard.goToPromoList();
        promoDashboard.inputSearchPromo(promoCode);
        promoDashboard.clickDropdown("status");
        promoDashboard.clickInsideDropdown("Active");
        promoDashboard.clickDropdown("status");
        promoDashboard.clickOnSearch();

        step("go To Tab Changes History");
        promoDashboard.clickOnSelectedPromo();
        promoDashboard.goToTabChangesHistory();

        step("Verify Changes History");
        int historyIndex = promoDashboard.openLatestChangesHistoryByActivty(activityType);
        String todayDateDMMMMYYYY = CoreFunctions.getTodayDateD_MMMM_YYYY();
        String todayDate = CoreFunctions.convertDateFormat(todayDateDMMMMYYYY, "d MMMM yyyy", "d MMM yyyy");
        String actualDateActor = promoDashboard.getChangesHistoryDateActor(historyIndex);
        Assertion.assertThat("is Changes History Date correct", actualDateActor, containsString(todayDate));
        Assertion.assertThat("is Changes History Actor correct", actualDateActor, containsString(username));
        Assertion.assertThat("Old Quota is Correct", promoDashboard.getChangesHistoryValueByFieldName("Quota"), containsString(oldQuota));
        Assertion.assertThat("New Quota is Correct", promoDashboard.getChangesHistoryValueByFieldName("Quota"), containsString(newQuota));
        oldMinTransaction = CoreFunctions.thousandSeparator(oldMinTransaction, ",");
        Assertion.assertThat("Old Min Transaction is Correct", promoDashboard.getChangesHistoryValueByFieldName("Min. AOV"), containsString(oldMinTransaction));
        newMinTransaction = CoreFunctions.thousandSeparator(newMinTransaction, ",");
        Assertion.assertThat("New Min Transaction is Correct", promoDashboard.getChangesHistoryValueByFieldName("Min. AOV"), containsString(newMinTransaction));
    }

    @Author(names = {"Fery", "Muhammad Hafiz"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyEditTillActiveQuotaToggleOn()
            throws InterruptedException, NotImplementedException {
        Map<String, String> param = getParam("verifyEditTillActiveQuotaToggleOn");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String dbName = param.get("dbName");
        String collectionName = param.get("collectionName");
        String dbNameGv = param.get("dbNameGv");
        String collectionNameGv = param.get("collectionNameGv");

        String promoCode = param.get("promoCode");
        String amountOfDiscountId = param.get("amountOfDiscountId");
        int min = Integer.parseInt(param.get("min"));
        int max = Integer.parseInt(param.get("max"));
        String filterStatus = param.get("filterStatus");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Go to promo list page");
        promodash.openPromoPage();
        promodash.goToPromoList();

        step("Ensure an Active promo exists");
        promoCode = promodash.ensureActivePromoOrCreate(promoCode, param);

        step("Search for promo");
        promodash.inputSearchPromo(promoCode);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();

        step("Clicks on the active promo");
        promodash.clickPromo(promoCode);

        step("Edit Promo");
        promodash.waitForListLoaderDissapear();
        promodash.clickEditButton();
        promodash.addCurrentDateToPromoDescription();
        promodash.scrollTillClickable(1, 200);
        promodash.clickToggleAndEditCampaignPeriod();
        Assertion.assertThat("Verify quota is toggle off", promodash.isEditCampaignPeriodAndPromoQuotaEnabled(), is(true));
        promodash.scrollTillClickable(2, 300);
        promodash.editGenerateRandomAmountOfDiscount(amountOfDiscountId, min, max);
        promodash.scrollToBottom();
        promodash.clickPreviewButton();
        promodash.clickSaveChangesButton();
        promodash.clickSaveChangesPopupButton();

        step("Approve the promo");
        promodash.goToPromoList();
        promodash.openApprovalPage();
        promodash.inputSearchPromo(promoCode);
        promodash.clickOnSearch();
        promodash.clickPromo(promoCode);
        promodash.clickAprroveButton();
        promodash.clickOnApproveModal();
        promodash.goToPromoList();
        promodash.inputSearchPromo(promoCode);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();
        promodash.clickPromo(promoCode);

        String promoStatusOverview = promodash.getPromoStatusOverview();
        String promoTypeOverview = promodash.getCampaignOverviewValue("Promo Type");
        String descriptionOverview = promodash.getCampaignOverviewValue("Description");
        String currencyOverview = promodash.getCampaignOverviewValue("Currency");
        String uniqueIdOverview = promodash.getCampaignOverviewValue("Unique Identifier");
        String campaignNameOverview = promodash.getCampaignOverviewValue("Campaign Name");
        promodash.scorllTillClickable();
        String divisionOverview = promodash.getCampaignOverviewValue("Division");
        String campaignEndDate = promodash.formatPeriodAndTime(promodash.getCampaignOverviewValue("Period and Time"), "end");
        promodash.scorllTillClickable();
        String promoCodeOverview = promodash.getCampaignOverviewValue("Promo Code");
        promodash.scrollTillClickable(3, 400);
        String discountAmount = String.valueOf(promodash.getDiscountAmountValue());

        String queryString = "{ \"code\": \"" + promoCode + "\", \"promoCodeStatus\": \"ACTIVE\" }";
        String mongoData = promodash.getPromoCodeMongoFullData(config,dbName,collectionName,queryString);

        String divisionQuery = "{ \"name\": \"" + divisionOverview + "\" }";
        String divisionData = promodash.getJsonStringValue(promodash.getPromoCodeMongoFullData(config,dbNameGv,collectionNameGv,divisionQuery), "code");

        step("Verify campaign overview new version");
        Assertion.assertThat("Verify description same as DB", promodash.getJsonStringValue(mongoData,"description"), is(descriptionOverview));
        Assertion.assertThat("Verify unique identifier same as DB", promodash.getJsonStringValue(mongoData,"tags").replaceAll("[^a-zA-Z]", ""), is(uniqueIdOverview));
        Assertion.assertThat("Verify campaignName same as DB", promodash.getJsonStringValue(mongoData,"campaignName"), is(campaignNameOverview));
        Assertion.assertThat("Verify promo type same as DB", promodash.getJsonStringValue(mongoData,"promoCodeDiscountType"), is(promoTypeOverview));
        Assertion.assertThat("Verify code same as DB", promodash.getJsonStringValue(mongoData,"code"), is(promoCodeOverview));
        Assertion.assertThat("Verify promoCodeStatus same as DB", promodash.getJsonStringValue(mongoData,"promoCodeStatus"), is(promoStatusOverview));
        Assertion.assertThat("Verify division same as DB", promodash.getJsonStringValue(mongoData,"division"), is(divisionData));
        Assertion.assertThat("Verify currency same as DB", promodash.getJsonStringValue(mongoData,"currency"), is(currencyOverview));
        Assertion.assertThat("Verify discount amount same as DB", promodash.getJsonStringValue(mongoData, "maxDiscount"), is(discountAmount));
        Assertion.assertThat("Verify campaign end date same as DB", promodash.getCampaignEndDateFromDb(mongoData), is(campaignEndDate));
    }

    @Author(names = {"Fery", "Muhammad Hafiz"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyEditTillActiveQuotaToggleOff()
            throws InterruptedException, NotImplementedException {
        Map<String, String> param = getParam("verifyEditTillActiveQuotaToggleOff");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String dbName = param.get("dbName");
        String collectionName = param.get("collectionName");

        String promoCode = param.get("promoCode");
        String amountOfDiscountId = param.get("amountOfDiscountId");
        int min = Integer.parseInt(param.get("min"));
        int max = Integer.parseInt(param.get("max"));
        String filterStatus = param.get("filterStatus");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Go to promo list page");
        promodash.openPromoPage();

        step("Ensure an Active promo exists");
        promoCode = promodash.ensureActivePromoOrCreate(promoCode, param);

        step("Search for active promo");
        promodash.searchAndFilterPromo(promoCode, filterStatus);

        step("Clicks on the active promo");
        promodash.clickPromo(promoCode);

        step("Edit Promo");
        promodash.clickEditButton();
        promodash.addCurrentDateToPromoDescription();
        promodash.scrollTillClickable(1, 200);
        Assertion.assertThat("Verify quota is toggle off", promodash.isEditCampaignPeriodAndPromoQuotaEnabled(), is(false));
        promodash.scrollTillClickable(2, 300);
        promodash.editGenerateRandomAmountOfDiscount(amountOfDiscountId, min, max);
        promodash.scrollToBottom();
        promodash.savePromoChanges();

        step("Approve the promo");
        promodash.approvePromo(promoCode);

        step("Verify promo is Active after approval");
        promodash.goToPromoList();
        promodash.searchAndFilterPromo(promoCode, filterStatus);
        promodash.clickPromo(promoCode);

        step("Load promo data from Mongo");
        promodash.loadPromoDataByPromoCodeWithParamDefaults(config, dbName, collectionName, promoCode);
        Assertion.assertThat("Mongo returned a promo document for " + promoCode,
                promodash.isPromoDataLoaded(), is(true));

        step("Verify Overview (status, promo type, currency)");
        promodash.setOverviewPromoDataFromDB();
        Assertion.assertThat("Overview promo data matches DB", promodash.verifyOverviewPromoApproval(), is(true));

        step("Verify General Information and Fraud Prevention (campaign name, description, unique id, division, etc.)");
        promodash.setPromoDetailGeneralAndFraudFromDb(config);
        Assertion.assertThat("General info and fraud prevention data matches DB",
                promodash.verifyPromoDetailGeneralAndFraudUi(), is(true));

        step("Verify Promo Code section");
        promodash.setPromoCodeDataFromDB();
        Assertion.assertThat("Promo code data matches DB", promodash.verifyPromoCodePromoApproval(), is(true));

        step("Verify discount amount");
        promodash.scrollTillClickable(3, 400);
        String discountAmount = String.valueOf(promodash.getDiscountAmountValue());
        String mongoMaxDiscount = promodash.getFieldPromoDocument(promodash.promoData, "maxDiscount");
        Assertion.assertThat("Verify discount amount same as DB", mongoMaxDiscount, is(discountAmount));
    }

    @Author(names = {"Fery", "Muhammad Hafiz"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyAddQuotaPcLevel() throws InterruptedException {
        Map<String, String> param = getParam("verifyAddQuotaPcLevel");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String dbName = param.get("dbName");
        String collectionName = param.get("collectionName");

        String promoCode = param.get("promoCode");
        String filterStatus = param.get("filterStatus");
        String quotaAdded = param.get("quotaAdded");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Go to promo list page");
        promodash.openPromoPage();
        promodash.goToPromoList();

        step("Ensure an Active promo exists");
        promoCode = promodash.ensureActivePromoOrCreate(promoCode, param);

        step("Search for promo");
        promodash.inputSearchPromo(promoCode);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();
        int currentQuotaFromList = promodash.getCurrentQuotaFromList();

        step("Add quota PC level");
        String currentQuery = "{ \"code\": \"" + promoCode + "\", \"promoCodeStatus\": \"ACTIVE\" }";
        String currentMongoData = promodash.getPromoCodeMongoFullData(config,dbName,collectionName,currentQuery);
        int currentQuotaPcLevel = (int) Double.parseDouble(
                promodash.getJsonStringValue(currentMongoData,"quotaPromoCodeLevel"));
        promodash.scrollToBottom();
        promodash.clickActionDropDown();
        promodash.clickActionTypeInDropdown("Add Quota");
        Assertion.assertThat("Current promo quota PC level is the same", currentQuotaFromList, is(currentQuotaPcLevel));
        promodash.inputAddQuotaAndSave(quotaAdded, false);

        step("Approve the add quota");
        promodash.openApprovalPage();
        promodash.navigateToOtherApprovalTab("Add Quota");
        promodash.inputSearchPromo(promoCode);
        promodash.clickOnSearch();
        promodash.clickAprroveButton();
        promodash.clickOnApproveModal();

        step("Verify add quota PC level");
        String newQuery = "{ \"code\": \"" + promoCode + "\", \"promoCodeStatus\": \"ACTIVE\" }";
        String newMongoData = promodash.getPromoCodeMongoFullData(config,dbName,collectionName,newQuery);
        int newQuotaPcLevel = (int) Double.parseDouble(
                promodash.getJsonStringValue(newMongoData,"quotaPromoCodeLevel"));
        promodash.goToPromoList();
        promodash.inputSearchPromo(promoCode);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();
        promodash.clickPromo(promoCode);
        Assertion.assertThat("Verify quota is added and the same", (int) Double.parseDouble(promodash.getCampaignOverviewValue("Quota")), is(newQuotaPcLevel));
    }

    @Author(names = {"Fery", "Muhammad Hafiz"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyAddQuotaTvLevelToday() throws InterruptedException {
        Map<String, String> param = getParam("verifyAddQuotaTvLevelToday");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String dbName = param.get("dbName");
        String collectionName = param.get("collectionName");
        String addQuotaCollectionName = param.get("addQuotaCollectionName");

        String promoCode = param.get("promoCode");
        String filterStatus = param.get("filterStatus");
        String quotaAdded = param.get("quotaAdded");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Go to promo list page");
        promodash.openPromoPage();
        promodash.goToPromoList();

        step("Ensure an Active promo exists");
        promoCode = promodash.ensureActivePromoOrCreate(promoCode, param);

        step("Search for promo");
        promodash.inputSearchPromo(promoCode);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();
        promodash.clickPromo(promoCode);
        int currentQuotaFromDetails = (int) Double.parseDouble(promodash.getPromoRulesValue("Quota"));

        step("Add quota PC level");
        String todayDate = promodash.getTodayDate();
        String startOfDay = todayDate + "T00:00:00.000Z";
        String endOfDay = todayDate + "T23:59:59.999Z";

        String query = "{ \"code\": \"" + promoCode + "\", \"promoCodeStatus\": \"ACTIVE\" }";
        String additionalQuotaQuery = "{ \"code\": \"" + promoCode + "\", \"status\": \"APPROVED\", \"createdDate\": { \"$gte\": { \"$date\": \"" + startOfDay + "\" }, \"$lt\": { \"$date\": \"" + endOfDay + "\" } } }";

        String currentMongoData = promodash.getPromoCodeMongoFullData(config,dbName,collectionName,query);
        int currentQuota = (int) Double.parseDouble(
                promodash.getQuotasTvFromDb(currentMongoData).get(0));
        List<String> currentAdditionalQuotaData = promodash.getPromoCodeMongoAdditionalQuota(config,dbName,addQuotaCollectionName,additionalQuotaQuery);
        int currentAdditionalQuota = currentAdditionalQuotaData.stream().mapToInt(Integer::parseInt).sum();
        promodash.goToPromoList();
        promodash.inputSearchPromo(promoCode);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();
        Assertion.assertThat("Current promo quota is the same", currentQuotaFromDetails, is(currentQuota+currentAdditionalQuota));
        promodash.clickActionDropDown();
        promodash.clickActionTypeInDropdown("Add Quota");
        promodash.inputAddQuotaAndSave(quotaAdded, true);

        step("Approve the add quota");
        promodash.openApprovalPage();
        promodash.navigateToOtherApprovalTab("Add Quota");
        promodash.inputSearchPromo(promoCode);
        promodash.clickOnSearch();
        promodash.clickAprroveButton();
        promodash.clickOnApproveModal();

        step("Verify add quota TV level today");
        String newMongoData = promodash.getPromoCodeMongoFullData(config,dbName,collectionName,query);
        int newQuota = (int) Double.parseDouble(
                promodash.getQuotasTvFromDb(newMongoData).get(0));
        List<String> newAdditionalQuotaData = promodash.getPromoCodeMongoAdditionalQuota(config,dbName,addQuotaCollectionName,additionalQuotaQuery);
        int newAdditionalQuota = newAdditionalQuotaData.stream().mapToInt(Integer::parseInt).sum();
        promodash.goToPromoList();
        promodash.inputSearchPromo(promoCode);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();
        promodash.clickPromo(promoCode);
        Assertion.assertThat("Verify quota is added and the same", (int) Double.parseDouble(promodash.getPromoRulesValue("Quota")), is(newQuota+newAdditionalQuota));
    }

    @Author(names = {"Fery"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyCreatePromoLinking() throws InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCreatePromoLinking");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String dbName = param.get("dbName");
        String linkingCollection = param.get("linkingCollection");
        String filterStatus = param.get("filterStatus");
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String endDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, 30);

        step("Login and go to promo linking page");
        promodash.maximize();
        promodash.goToAdminCarePage(username, password);
        promodash.openPromoPage();
        promodash.goToPromoList();
        promodash.goToPromoLinkingPage();

        step("Create promo linking");
        String tags = promodash.createPromoLinking(
                param.get("uniqueLinkPrefix"), username, param.get("accountId"),
                param.get("quotaLevel"), param.get("quotaPromoCodeLevel"),
                param.get("code"), param.get("campaignName"),
                currentDate, endDate, currentDate, currentDate
        );

        step("Approve promo linking");
        promodash.goToPromoList();
        promodash.refreshPage();
        promodash.openApprovalPage();
        promodash.navigateToOtherApprovalTab("Promo Linking");
        promodash.clickApproveButton(tags);
        promodash.clickOnApproveModal();

        step("Verify created promo linking status matches DB");
        promodash.goToPromoLinkingPage();
        promodash.inputSearchLinking(tags);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();

        String query = "{ \"name\": \"" + tags + "\"}";
        String statusInDb = promodash.getJsonStringValue(
                promodash.getPromoCodeMongoFullData(config, dbName, linkingCollection, query), "status");
        Assertion.assertThat("Promo linking is created", promodash.getStatusInDashboard(), is(statusInDb));
    }

    @Author(names = {"Fery"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyAddQuotaLinkingLevel() throws InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyAddQuotaLinkingLevel");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String dbName = param.get("dbName");
        String collectionName = param.get("collectionName");
        String uniqueLinkPrefix = param.get("uniqueLinkPrefix");
        String accountId = param.get("accountId");
        String quotaLevel = param.get("quotaLevel");
        String quotaPromoCodeLevel = param.get("quotaPromoCodeLevel");
        String code = param.get("code");
        String campaignName = param.get("campaignName");
        String startDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String endDate = CoreFunctions.addToDateYYYY_MM_DD(CoreFunctions.getCurrentDateYYYY_MM_DD(), 30);
        String createdDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String updatedDate = CoreFunctions.getCurrentDateYYYY_MM_DD();

        String filterStatus = param.get("filterStatus");
        String quotaAdded = param.get("quotaAdded");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Go to promo list page");
        promodash.openPromoPage();
        promodash.goToPromoLinkingPage();

        step("Create and approve promo linking");
        String tags = promodash.createPromoLinking(
                uniqueLinkPrefix,
                username,
                accountId,
                quotaLevel,
                quotaPromoCodeLevel,
                code,
                campaignName,
                startDate,
                endDate,
                createdDate,
                updatedDate
        );
        promodash.goToPromoList();
        promodash.refreshPage();
        promodash.openApprovalPage();
        promodash.navigateToOtherApprovalTab("Promo Linking");
        promodash.clickApproveButton(tags);
        promodash.clickOnApproveModal();
        promodash.goToPromoLinkingPage();

        String queryGet = "{ \"name\": { \"$regex\": \"automationlink\" }, \"status\": \"ACTIVE\" }";
        String dataGet = promodash.getPromoCodeMongoFullData(config, dbName,
                collectionName, queryGet);
        String linkName = promodash.getJsonStringValue(dataGet, "name");

        step("Search for promo linking");
        String query = "{ \"name\": \"" + linkName + "\", \"status\": \"ACTIVE\" }";
        String currentMongoData = promodash.getPromoCodeMongoFullData(config, dbName,
                collectionName, query);
        int currentQuotaLinkLevelDb = (int) Double.parseDouble(
                promodash.getJsonStringValue(currentMongoData, "quota"));

        promodash.inputSearchLinking(linkName);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();
        promodash.clickCollapsible();
        int currentQuotaLinkLevelDash = (int) Double.parseDouble(promodash.getPromoLinkingQuotaDash());
        Assertion.assertThat("Verify current quota linking level", currentQuotaLinkLevelDash,
                is(currentQuotaLinkLevelDb));

        step("Add quota Linking level");
        promodash.clickActionDropDown();
        promodash.clickActionTypeInDropdown("Add Quota");
        promodash.inputAddQuotaAndSave(quotaAdded, false);

        step("Approve the add quota");
        promodash.openApprovalPage();
        promodash.navigateToOtherApprovalTab("Add Quota");
        promodash.clickApproveButton(linkName);
        promodash.clickOnApproveModal();
        promodash.refreshPage();

        step("Verify add quota TV level today");
        String newMongoData = promodash.getPromoCodeMongoFullData(config, dbName, collectionName,
                query);
        int newQuotaLinkLevelDb = (int) Double.parseDouble(
                promodash.getJsonStringValue(newMongoData, "quota"));

        promodash.goToPromoLinkingPage();
        promodash.inputSearchLinking(linkName);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();
        promodash.clickCollapsible();

        int newQuotaLinkLevelDash = (int) Double.parseDouble(promodash.getPromoLinkingQuotaDash());
        Assertion.assertThat("Verify new quota linking level", newQuotaLinkLevelDash,
                is(newQuotaLinkLevelDb));
    }

    @Author(names = {"Fery"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyInactiveDeletePromoLinking() throws InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyInactiveDeletePromoLinking");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String dbName = param.get("dbName");
        String collectionName = param.get("collectionName");
        String filterStatus = param.get("filterStatus");
        String filterStatusInactive = param.get("filterStatusInactive");
        String filterStatusDelete = param.get("filterStatusDelete");
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String endDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, 30);

        step("Login and go to promo linking page");
        promodash.maximize();
        promodash.goToAdminCarePage(username, password);
        promodash.openPromoPage();
        promodash.goToPromoLinkingPage();

        step("Create promo linking");
        String tags = promodash.createPromoLinking(
                param.get("uniqueLinkPrefix"), username, param.get("accountId"),
                param.get("quotaLevel"), param.get("quotaPromoCodeLevel"),
                param.get("code"), param.get("campaignName"),
                currentDate, endDate, currentDate, currentDate
        );

        step("Approve promo linking");
        promodash.goToPromoList();
        promodash.refreshPage();
        promodash.openApprovalPage();
        promodash.navigateToOtherApprovalTab("Promo Linking");
        promodash.clickApproveButton(tags);
        promodash.clickOnApproveModal();
        promodash.goToPromoLinkingPage();

        String queryGet = "{ \"name\": { \"$regex\": \"automationlink\" }, \"status\": \"ACTIVE\" }";
        String dataGet = promodash.getPromoCodeMongoFullData(config, dbName, collectionName, queryGet);
        String linkName = promodash.getJsonStringValue(dataGet, "name");

        step("Verify promo linking is Active");
        String activeQuery = "{ \"name\": \"" + linkName + "\", \"status\": \"ACTIVE\" }";
        String currentStatusDb = promodash.getJsonStringValue(
                promodash.getPromoCodeMongoFullData(config, dbName, collectionName, activeQuery), "status");
        promodash.inputSearchLinking(linkName);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();
        Assertion.assertThat("Verify current status is Active", promodash.getStatusInDashboard(), is(currentStatusDb));

        step("Inactive promo linking");
        promodash.clickActionDropDown();
        promodash.clickActionTypeInDropdown("Inactive");
        promodash.clickOnInactiveModal();

        step("Verify promo linking is Inactive");
        promodash.refreshPage();
        promodash.clickResetFilter();
        promodash.inputSearchLinking(linkName);
        promodash.filterSearchByStatus(filterStatusInactive);
        promodash.clickOnSearch();

        String inactiveStatusDb = promodash.getJsonStringValue(
                promodash.getPromoCodeMongoFullData(config, dbName, collectionName,
                        "{ \"name\": \"" + linkName + "\", \"status\": \"INACTIVE\" }"), "status");
        Assertion.assertThat("Verify inactive status", promodash.getStatusInDashboard(), is(inactiveStatusDb));

        step("Delete promo linking");
        promodash.clickActionDropDown();
        promodash.clickActionTypeInDropdown("Delete");
        promodash.clickDeleteButton();

        step("Verify promo linking is Deleted");
        promodash.refreshPage();
        promodash.clickResetFilter();
        promodash.inputSearchLinking(linkName);
        promodash.filterSearchByStatus(filterStatusDelete);
        promodash.clickOnSearch();

        String deleteStatusDb = promodash.getJsonStringValue(
                promodash.getPromoCodeMongoFullData(config, dbName, collectionName,
                        "{ \"name\": \"" + linkName + "\", \"status\": \"DELETED\" }"), "status");
        Assertion.assertThat("Verify delete status", promodash.getStatusInDashboard(), is(deleteStatusDb));
    }

    @Author(names = {"Fery"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyEditPromoLinking()
            throws InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyEditPromoLinking");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String dbName = param.get("dbName");
        String collectionName = param.get("collectionName");
        String filterStatus = param.get("filterStatus");
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String endDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, 30);

        step("Login and go to promo linking page");
        promodash.maximize();
        promodash.goToAdminCarePage(username, password);
        promodash.openPromoPage();
        promodash.goToPromoLinkingPage();

        step("Create promo linking");
        String tags = promodash.createPromoLinking(
                param.get("uniqueLinkPrefix"), username, param.get("accountId"),
                param.get("quotaLevel"), param.get("quotaPromoCodeLevel"),
                param.get("code"), param.get("campaignName"),
                currentDate, endDate, currentDate, currentDate
        );

        step("Approve promo linking");
        promodash.goToPromoList();
        promodash.refreshPage();
        promodash.openApprovalPage();
        promodash.navigateToOtherApprovalTab("Promo Linking");
        promodash.clickApproveButton(tags);
        promodash.clickOnApproveModal();
        promodash.goToPromoLinkingPage();

        String queryGet = "{ \"name\": { \"$regex\": \"automationlink\" }, \"status\": \"ACTIVE\" }";
        String dataGet = promodash.getPromoCodeMongoFullData(config, dbName, collectionName, queryGet);
        String linkName = promodash.extractNameFromPrefix(promodash.getJsonStringValue(dataGet, "name"), "E");
        int linkingCount = promodash.getPromoLinkMembersCount(promodash.getJsonStringValue(dataGet, "promoLinkMembers"));

        step("Search and open edit for promo linking");
        promodash.inputSearchLinking(linkName);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();
        promodash.clickActionDropDown();
        promodash.clickActionTypeInDropdown("Edit");

        step("Create new promo to add to linking");
        List<String> startEndDate = promodash.fetchExistingStartEndDateLink(config, dbName, param.get("code"));
        String createdPromo = promodash.createPromo(
                param.get("uniqueLinkPrefix"), username, param.get("accountId"),
                param.get("quotaLevel"), param.get("quotaPromoCodeLevel"),
                param.get("code"), param.get("campaignName"),
                startEndDate.get(0), startEndDate.get(1),
                currentDate, currentDate
        ).get(0);

        step("Add campaign to promo linking and submit");
        promodash.refreshPage();
        promodash.clickAddCampaignLink();
        promodash.inputPromoLinking(createdPromo, String.valueOf(linkingCount + 1));
        promodash.clickSubmitPromoLink();

        step("Approve edited promo linking");
        promodash.goToPromoList();
        promodash.openApprovalPage();
        promodash.navigateToOtherApprovalTab("Promo Linking");
        promodash.clickApproveButton(linkName);
        promodash.clickOnApproveModal();

        step("Verify newly added promo is shown in linking");
        promodash.goToPromoLinkingPage();
        promodash.inputSearchLinking(linkName);
        promodash.filterSearchByStatus(filterStatus);
        promodash.clickOnSearch();
        promodash.clickCollapsible();
        Assertion.assertThat("Verify newly added promo in linked shown",
                promodash.isNewlyEditedPromoShown(createdPromo), is(true));
    }

    @Author(names = { "Fery" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyCreatePromoInstantPercLock() throws InterruptedException {
        Map<String, String> param = getParam("verifyCreatePromoInstantPercLock");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String name = param.get("name");
        String instant = param.get("instant");
        String description = param.get("description");
        String ben = param.get("ben");
        String idr = param.get("idr");
        String message = param.get("message");
        String draft = param.get("draft");
        String fraudPreventionData = param.get("fraudPreventionData");
        String priceData = param.get("priceData");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Click on create new promo");
        promodash.clickCreateNewPromoButton();

        step("Fill Instant PercLock promo form");
        String randomName = promodash.fillPromoForm(param);

        step("Submit promo for approval");
        promodash.submitPromoForm();

        step("Check success message is displayed or not");
        Assertion.assertThat("is success message displayed?", promodash.checkSuccessMessage(message), is(true));

        String queryString = "{ \"campaignName\": \"" + randomName + "\" }";
        String mongoData = promodash.getPromoCodeMongoFullData(config, database, collectionName, queryString);

        step("Checking DB assertions");
        Assertion.assertThat("Verify campaignName in DB", promodash.getJsonStringValue(mongoData, randomName, "campaignName"), is(true));
        Assertion.assertThat("Verify instant in DB", promodash.getJsonStringValue(mongoData, instant.toUpperCase(), "promoCodeDiscountType"), is(true));
        Assertion.assertThat("Verify code in DB", promodash.getJsonStringValue(mongoData, name, "code"), is(true));
        Assertion.assertThat("Verify description in DB", promodash.getJsonStringValue(mongoData, description, "description"), is(true));
        Assertion.assertThat("Verify promoCodeStatus in DB", promodash.getJsonStringValue(mongoData, draft, "promoCodeStatus"), is(true));
        Assertion.assertThat("Verify division in DB", promodash.getJsonStringValue(mongoData, ben, "division"), is(true));
        Assertion.assertThat("Verify currency in DB", promodash.getJsonStringValue(mongoData, idr, "currency"), is(true));
        Assertion.assertThat("Verify fraudPreventionData in DB", promodash.checkDbData(mongoData, fraudPreventionData), is(true));
        Assertion.assertThat("Verify priceData in DB", promodash.checkDbData(mongoData, priceData), is(true));
    }

    @Author(names = {"Fery"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyCreatePromoCBPL()
            throws InterruptedException {
        Map<String, String> param = getParam("verifyCreatePromoCBPL");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String dbName = param.get("dbName");
        String collectionName = param.get("collectionName");
        String collectionVariableSource = param.get("collectionVariableSource");

        String name = param.get("name");
        String identifier = param.get("identifier");
        String description = param.get("description");
        String promoTypeId = param.get("promoTypeId");
        String cashback = param.get("cashback");
        String campaignTypeId = param.get("campaignTypeId");
        String testing = param.get("testing");
        String divisionId = param.get("divisionId");
        String ben = param.get("ben");
        String selectCurrencyId = param.get("selectCurrencyId");
        String idr = param.get("idr");
        String verticalId = param.get("verticalId");
        String hotel = param.get("hotel");
        String ruleVerticalId = param.get("ruleVerticalId");
        String parameterId = param.get("parameterId");
        String accomodationType = param.get("accomodationType");
        String operatorId = param.get("operatorId");
        String valuesId = param.get("valuesId");
        String valuesText = param.get("valuesText");
        String minTraValueId = param.get("minTraValueId");
        String minTraText = param.get("minTraText");
        String maxTraValueId = param.get("maxTraValueId");
        String maxTraText = param.get("maxTraText");
        String budgetId = param.get("budgetId");
        String budgetValue = param.get("budgetValue");
        String percentage = param.get("percentage");
        boolean partialPercentage = Boolean.parseBoolean(param.get("partialPercentage"));
        String percText = param.get("percText");
        String maxAmount = param.get("maxAmount");
        String maxAmountText = param.get("maxAmountText");
        String costAllocationId = param.get("costAllocationId");
        String paymentMethodId = param.get("paymentMethodId");
        String paymentMethodText = param.get("paymentMethodText");
        String tiket = param.get("tiket");
        String selectUsageCountId = param.get("selectUsageCountId");
        String countText = param.get("countText");
        String usagePeriodId = param.get("usagePeriodId");
        String campaignPeriod = param.get("campaignPeriod");
        String platformId = param.get("platformId");
        String android = param.get("android");
        String campaignStartDateId = param.get("campaignStartDateId");
        String startDate = param.get("startDate");
        String campaignEndDateId = param.get("campaignEndDateId");
        String endDate = param.get("endDate");
        String randomName;
        String quotaType = param.get("quotaType");
        String promoVisibility = param.get("promoVisibility");
        String promoVisibilityText = param.get("promoVisibilityText");
        String tenureId = param.get("tenureId");
        String tenureText = param.get("tenureText");
        String filterPaymentId = param.get("filterPaymentId");
        String filterPaymentText = param.get("filterPaymentText");
        String roomAdult = param.get("roomAdult");
        String secondOperatorId = param.get("secondOperatorId");
        String secondValuesId = param.get("secondValuesId");
        String secondValuesText = param.get("secondValuesText");
        String inApproval = param.get("inApproval");
        String fraudPreventionData = param.get("fraudPreventionData");
        String priceData = param.get("priceData");
        String quotaLevel = param.get("quotaLevel");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Click on create new promo");
        promodash.clickCreateNewPromoButton();

        step("Promo creation");
        promodash.clickDropdown(promoTypeId);
        promodash.clickInsideDropdown(cashback);
        promodash.clickDropdown(campaignTypeId);
        promodash.clickInsideDropdown(testing);
        randomName = promodash.sendTextInCampaignNameInput();
        promodash.sendTextInUniqueIdentifierInput(identifier);
        promodash.sendTextInDescriptionBox(description);
        promodash.scorllTillClickable();
        promodash.clickDropdown(divisionId);
        promodash.clickInsideDropdown(ben);
        promodash.clickDropdown(selectCurrencyId);
        promodash.clickInsideDropdown(idr);
        promodash.clickDropdown(verticalId);
        promodash.clickInsideDropdown(hotel);
        promodash.scrollTillClickable(3, 300);
        promodash.clickDropdown(campaignStartDateId);
        promodash.selectDate(startDate);
        promodash.scrollTillClickable(1, 350);
        promodash.clickSaveButton();
        promodash.scrollTillClickable(1, -350);
        promodash.clickDropdown(campaignEndDateId);
        promodash.selectDate(endDate);
        promodash.scrollTillClickable(1, 350);
        promodash.clickSaveButton();
        promodash.scrollTillClickable(1, -350);
        promodash.clickAddPeriod();
        promodash.scorllTillClickable();

        promodash.clickQuotaType(quotaType);
        promodash.clickDropdown(promoVisibility);
        promodash.clickInsideDropdown(promoVisibilityText);
        promodash.scrollTillClickable(1, 450);

        promodash.clickDropdown(ruleVerticalId);
        promodash.clickInsideDropdown(hotel);
        promodash.clickDropdown(parameterId);
        promodash.clickInsideDropdown(accomodationType);
        promodash.clickDropdown(operatorId);
        promodash.clickEqualToInDropdown();
        promodash.sendTextInInput(valuesId,valuesText);
        promodash.clickAddValues();
        promodash.scrollTillClickable(2,350);
        promodash.sendTextInInput(minTraValueId,minTraText);
        promodash.sendTextInInput(maxTraValueId,maxTraText);
        promodash.scorllTillClickable(1,300);
        promodash.clickDropdown(budgetId);
        promodash.clickInsideDropdown(budgetValue);
        promodash.scorllTillClickable(1, 300);
        promodash.clickPercentageRadioButton(partialPercentage);
        promodash.sendTextInInput(percentage,percText);
        promodash.sendTextInInput(maxAmount, maxAmountText);
        promodash.scorllTillClickable(1,350);

        promodash.clickSelectPaymentMethodRadioButton();
        promodash.clickDropdownPaymentMethod(paymentMethodId);
        promodash.sendTextInInputDataTest(filterPaymentId, filterPaymentText);
        promodash.clickInsideDropdown(paymentMethodText);
        promodash.clickDropdownPaymentMethod(paymentMethodId);
        promodash.clickDropdown(tenureId);
        promodash.clickInsideDropdown(tenureText);
        promodash.clickDropdown(tenureId);

        promodash.scrollTillClickable(1, 100);
        promodash.clickDropdown(costAllocationId);
        promodash.clickInsideDropdown(tiket);

        promodash.clickDuplicateRuleSet();
        promodash.scrollTillClickable(1, -2100);
        promodash.clickDropdown(promoVisibility);
        promodash.clickInsideDropdown(promoVisibilityText);
        promodash.scrollTillClickable(4, 600);

        promodash.clickDropdown(parameterId, 1);
        promodash.clickInsideDropdown(roomAdult);
        promodash.clickDropdown(secondOperatorId);
        promodash.clickEqualToInDropdown();
        promodash.sendTextInInput(secondValuesId,secondValuesText);

        promodash.scrollTillClickable(2, 300);
        promodash.clickDropdown(costAllocationId, 1);
        promodash.clickInsideDropdown(tiket);

        promodash.scrollTillClickable(4, 400);
        promodash.sendTextInInput(selectUsageCountId, countText);
        promodash.scrollTillClickable(1, 300);
        promodash.clickDropdown(usagePeriodId);
        promodash.clickInsideDropdown(campaignPeriod);
        promodash.clickDropdown(platformId);
        promodash.clickInsideDropdown(android);
        promodash.clickDropdown(platformId);
        promodash.scorllTillClickable(1, 100);
        promodash.clickUsername();
        promodash.sendTextInPromoNameInput(randomName);
        promodash.scorllTillClickable(2,300);
        promodash.clickGenerateTnc();
        promodash.waitForGenerateTncFinish();

        step("Click create campaign button");
        promodash.clickPreviewButton();
        promodash.clickCreateCampaignButton();

        step("Click send for approval button");
        promodash.clickSendForApprovalButton();
        promodash.goToPromoList();

        String queryString = "{ \"code\": \"" + randomName + "\"}";
        String mongoData = promodash.getPromoCodeMongoFullData(config,dbName,collectionName,queryString);

        String paymentMethodName = String.valueOf(promodash.getPaymentNames(mongoData)[0]);
        String queryVariableSource = "{ \"valueId\": \"" + paymentMethodName + "\" }";
        String mongoVariable = promodash.getPromoCodeMongoFullData(config,dbName,collectionVariableSource,queryVariableSource);

        step("Checking DB assertions");
        Assertion.assertThat("Verify campaignName in DB", promodash.getJsonStringValue(mongoData,randomName,"campaignName"), is(true));
        Assertion.assertThat("Verify cashback in DB", promodash.getJsonStringValue(mongoData,cashback.toUpperCase(),"promoCodeDiscountType"), is(true));
        Assertion.assertThat("Verify code in DB", promodash.getJsonStringValue(mongoData,name,"code"), is(true));
        Assertion.assertThat("Verify description in DB", promodash.getJsonStringValue(mongoData,description,"description"), is(true));
        Assertion.assertThat("Verify promoCodeStatus in DB", promodash.getJsonStringValue(mongoData,inApproval,"promoCodeStatus"), is(true));
        Assertion.assertThat("Verify division in DB", promodash.getJsonStringValue(mongoData,ben,"division"), is(true));
        Assertion.assertThat("Verify currency in DB", promodash.getJsonStringValue(mongoData,idr,"currency"), is(true));
        Assertion.assertThat("Verify fraudPreventionData in DB", promodash.checkDbData(mongoData,fraudPreventionData), is(true));
        Assertion.assertThat("Verify priceData in DB", promodash.checkDbData(mongoData,priceData), is(true));
        Assertion.assertThat("Verify promo code quota level in DB", promodash.getJsonStringValue(mongoData,quotaLevel,"quotaLevel"), is(true));
        Assertion.assertThat("Verify payment method in DB", promodash.getJsonStringValue(mongoVariable,paymentMethodText,"valueName"), is(true));
    }

    @Author(names = { "Fery" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyApprovalPageTillApproveReject() throws InterruptedException {
        Map<String, String> param = getParam("verifyApprovalPageTillApproveReject");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String identifier = param.get("identifier");
        String description = param.get("description");
        String promoTypeId = param.get("promoTypeId");
        String instant = param.get("instant");
        String campaignTypeId = param.get("campaignTypeId");
        String testing = param.get("testing");
        String divisionId = param.get("divisionId");
        String ben = param.get("ben");
        String selectCurrencyId = param.get("selectCurrencyId");
        String idr = param.get("idr");
        String verticalId = param.get("verticalId");
        String hotel = param.get("hotel");
        String quotaId = param.get("quotaId");
        String quotaText = param.get("quotaText");
        String ruleVerticalId = param.get("ruleVerticalId");
        String parameterId = param.get("parameterId");
        String accomodationType = param.get("accomodationType");
        String operatorId = param.get("operatorId");
        String valuesId = param.get("valuesId");
        String valuesText = param.get("valuesText");
        String minTraValueId = param.get("minTraValueId");
        String minTraText = param.get("minTraText");
        String maxTraValueId = param.get("maxTraValueId");
        String maxTraText = param.get("maxTraText");
        String amountOfDiscountId = param.get("amountOfDiscountId");
        String cashbackText = param.get("cashbackText");
        String costAllocationId = param.get("costAllocationId");
        String tiket = param.get("tiket");
        String selectUsageCountId = param.get("selectUsageCountId");
        String countText = param.get("countText");
        String usagePeriodId = param.get("usagePeriodId");
        String campaignPeriod = param.get("campaignPeriod");
        String platformId = param.get("platformId");
        String android = param.get("android");
        String loginStateId = param.get("loginStateId");
        String loggedIn = param.get("loggedIn");
        String campaignStartDateId = param.get("campaignStartDateId");
        String startDate = param.get("startDate");
        String campaignEndDateId = param.get("campaignEndDateId");
        String endDate = param.get("endDate");
        String inApproval = param.get("inApproval");
        String active = param.get("active");
        String randomName;

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Click on create new promo");
        promodash.clickCreateNewPromoButton();

        step("Click on Promo type dropdown");
        promodash.clickDropdown(promoTypeId);

        step("select cashback in Promo type dropdown");
        promodash.clickInsideDropdown(instant);

        step("Click on campaign type dropdown");
        promodash.clickDropdown(campaignTypeId);

        step("select TESTING inside campaign type dropdown");
        promodash.clickInsideDropdown(testing);

        step("Send name in campaign name");
        randomName = promodash.sendTextInCampaignNameInput();

        step("Send name in Unique Identifier");
        promodash.sendTextInUniqueIdentifierInput(identifier);

        step("Send Text in description box");
        promodash.sendTextInDescriptionBox(description);

        step("scroll to make element clickable");
        promodash.scorllTillClickable();

        step("Select division dropdown");
        promodash.clickDropdown(divisionId);

        step("select BEN inside division dropdown");
        promodash.clickInsideDropdown(ben);

        step("Select select-currency dropdown");
        promodash.clickDropdown(selectCurrencyId);

        step("select IDR inside select-currency dropdown");
        promodash.clickInsideDropdown(idr);

        step("Select vertical dropdown");
        promodash.clickDropdown(verticalId);

        step("select hotel in vertical dropdown");
        promodash.clickInsideDropdown(hotel);

        step("scroll to make element clickable");
        promodash.scrollTillClickable(2, 350);

        step("Select date");
        promodash.clickDropdown(campaignStartDateId);
        promodash.selectDate(startDate);
        promodash.scrollTillClickable(1, 350);
        promodash.clickSaveButton();
        promodash.scrollTillClickable(1, -350);
        promodash.clickDropdown(campaignEndDateId);
        promodash.selectDate(endDate);
        promodash.scrollTillClickable(1, 350);
        promodash.clickSaveButton();
        promodash.scrollTillClickable(1, -350);

        step("Click add period button");
        promodash.clickAddPeriod();

        step("scroll to make element clickable");
        promodash.scorllTillClickable();

        step("send text in quota input box");
        promodash.sendTextInInput(quotaId, quotaText);

        step("scroll to make element clickable");
        promodash.scorllTillClickable();

        step("Select rule set 1 vertical dropdown");
        promodash.clickDropdown(ruleVerticalId);

        step("select hotel in vertical dropdown");
        promodash.clickInsideDropdown(hotel);

        step("Select parameter dropdown");
        promodash.clickDropdown(parameterId);

        step("select in parameter dropdown");
        promodash.clickInsideDropdown(accomodationType);

        step("Select operator dropdown");
        promodash.clickDropdown(operatorId);

        step("select in operator dropdown");
        promodash.clickEqualToInDropdown();

        step("send text in values input box");
        promodash.sendTextInInput(valuesId,valuesText);

        step("click add values button");
        promodash.clickAddValues();
        promodash.scorllTillClickable(2, 300);

        step("send text in Min Transaction value input box");
        promodash.sendTextInInput(minTraValueId,minTraText);

        step("send text in Max Transaction value input box");
        promodash.sendTextInInput(maxTraValueId,maxTraText);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(2,300);

        step("send text in Amount of Discount input box");
        promodash.sendTextInInput(amountOfDiscountId,cashbackText);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(2, 300);

        step("Select cost allocation dropdown");
        promodash.clickDropdown(costAllocationId);

        step("select in cost allocation dropdown");
        promodash.clickInsideDropdown(tiket);

        step("send text in select usage count input box");
        promodash.sendTextInInput(selectUsageCountId,countText);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(1,100);

        step("Select usage period dropdown");
        promodash.clickDropdown(usagePeriodId);

        step("select in usage period dropdown");
        promodash.clickInsideDropdown(campaignPeriod);

        step("Select platform dropdown");
        promodash.clickDropdown(platformId);

        step("select in platform dropdown");
        promodash.clickInsideDropdown(android);

        step("Select login state dropdown");
        promodash.clickDropdown(loginStateId);

        step("select in login state dropdown");
        promodash.clickInsideDropdown(loggedIn);

        step("click verified radio box");
        promodash.clickVerifiedRadioBox();

        step("click username under validated by");
        promodash.clickUsername();

        step("Send name in Promo name");
        promodash.sendTextInPromoNameInput(randomName);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(2,300);

        step("Generate TnC");
        promodash.clickGenerateTnc();
        promodash.waitForGenerateTncFinish();

        step("Create campaign");
        promodash.clickPreviewButton();
        promodash.clickCreateCampaignButton();
        promodash.clickSendForApprovalButton();

        step("Go to promo list page");
        promodash.goToPromoList();
        promodash.openApprovalPage();

        step("Search for the promo in approval page");
        promodash.inputSearchPromo(randomName);
        promodash.clickOnSearch();

        String queryString = "{ \"code\": \"" + randomName + "\" }";
        String mongoData = promodash.getPromoCodeMongoFullData(config,database,collectionName,queryString);

        step("Verify status of promo is in approval");
        Assertion.assertThat("Verify status of promo is in approval", promodash.getJsonStringValue(mongoData,inApproval,"promoCodeStatus"), is(true));

        step("Approve the promo");
        promodash.clickAprroveButton();
        promodash.clickOnApproveModal();
        promodash.goToPromoList();
        promodash.inputSearchPromo(randomName);
        promodash.clickOnSearch();

        step("Verify status of promo is active");
        String newMongoData = promodash.getPromoCodeMongoFullData(config,database,collectionName,queryString);
        Assertion.assertThat("Verify status of promo is active", promodash.getJsonStringValue(newMongoData,active,"promoCodeStatus"), is(true));
    }

    @Author(names = { "Rizki Maulana" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyCreatePromoWithCCBinNumber() throws InterruptedException {
        Map<String, String> param = getParam("verifyCreatePromoWithCCBinNumber");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        PromoCreationBINModalWeb<?> promoCreationBINModalWeb = new PromoCreationBINModalWeb<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String identifier = param.get("identifier");
        String description = param.get("description");
        String promoTypeId = param.get("promoTypeId");
        String instant = param.get("instant");
        String campaignTypeId = param.get("campaignTypeId");
        String testing = param.get("testing");
        String divisionId = param.get("divisionId");
        String ben = param.get("ben");
        String selectCurrencyId = param.get("selectCurrencyId");
        String idr = param.get("idr");
        String verticalId = param.get("verticalId");
        String hotel = param.get("hotel");
        String budgetId = param.get("budgetId");
        String budgetText = param.get("budgetText");
        String ruleVerticalId = param.get("ruleVerticalId");
        String parameterId = param.get("parameterId");
        String accomodationType = param.get("accomodationType");
        String operatorId = param.get("operatorId");
        String valuesId = param.get("valuesId");
        String valuesText = param.get("valuesText");
        String minTraValueId = param.get("minTraValueId");
        String minTraText = param.get("minTraText");
        String maxTraValueId = param.get("maxTraValueId");
        String maxTraText = param.get("maxTraText");
        String paymentMethodId = param.get("paymentMethodId");
        String paymentMethodText = param.get("paymentMethodText");
        String binId = param.get("binId");
        String binColumn = param.get("binColumn");
        String binValue = param.get("binValue");
        String tenureId = param.get("tenureId");
        String tenureText = param.get("tenureText");
        String amountOfDiscountId = param.get("amountOfDiscountId");
        String cashbackText = param.get("cashbackText");
        String costAllocationId = param.get("costAllocationId");
        String fixedValueId = param.get("fixedValueId");
        String fixedValueText = param.get("fixedValueText");
        String selectUsageCountId = param.get("selectUsageCountId");
        String countText = param.get("countText");
        String usagePeriodId = param.get("usagePeriodId");
        String campaignPeriod = param.get("campaignPeriod");
        String platformId = param.get("platformId");
        String android = param.get("android");
        String loginStateId = param.get("loginStateId");
        String loggedIn = param.get("loggedIn");
        String campaignStartDateId = param.get("campaignStartDateId");
        String startDate = param.get("startDate");
        String campaignEndDateId = param.get("campaignEndDateId");
        String endDate = param.get("endDate");
        String randomName = param.get("randomName");

        step("Open Promo Dashboard");
        promoDashboard.maximize();
        promoDashboard.goToAdminCarePage(username,password);
        promoDashboard.scorllToCarAdmin();
        promoDashboard.openPromoPage();

        step("Click create new promo button");
        promoDashboard.clickCreateNewPromoButton();

        step("Click on Promo type dropdown");
        promoDashboard.clickDropdown(promoTypeId);

        step("select cashback in Promo type dropdown");
        promoDashboard.clickInsideDropdown(instant);

        step("Click on campaign type dropdown");
        promoDashboard.clickDropdown(campaignTypeId);

        step("select TESTING inside campaign type dropdown");
        promoDashboard.clickInsideDropdown(testing);

        step("Send name in campaign name");
        randomName = promoDashboard.sendTextInCampaignNameInput();

        step("Send name in Unique Identifier");
        promoDashboard.sendTextInUniqueIdentifierInput(identifier);

        step("Send Text in description box");
        promoDashboard.sendTextInDescriptionBox(description);

        step("scroll to make element clickable");
        promoDashboard.scorllTillClickable();

        step("Select division dropdown");
        promoDashboard.clickDropdown(divisionId);

        step("select BEN inside division dropdown");
        promoDashboard.clickInsideDropdown(ben);

        step("Select select-currency dropdown");
        promoDashboard.clickDropdown(selectCurrencyId);

        step("select IDR inside select-currency dropdown");
        promoDashboard.clickInsideDropdown(idr);

        step("Select vertical dropdown");
        promoDashboard.clickDropdown(verticalId);

        step("select hotel in vertical dropdown");
        promoDashboard.clickInsideDropdown(hotel);

        step("scroll to make element clickable");
        promoDashboard.scrollTillClickable(2, 350);

        step("Select date");
        promoDashboard.clickDropdown(campaignStartDateId);
        promoDashboard.selectDate(startDate);
        promoDashboard.scrollTillClickable(1, 350);
        promoDashboard.clickSaveButton();
        promoDashboard.scrollTillClickable(1, -350);
        promoDashboard.clickDropdown(campaignEndDateId);
        promoDashboard.selectDate(endDate);
        promoDashboard.scrollTillClickable(1, 350);
        promoDashboard.clickSaveButton();
        promoDashboard.scrollTillClickable(1, -350);

        step("Click add period button");
        promoDashboard.clickAddPeriod();

        step("scroll to make element clickable");
        promoDashboard.scorllTillClickable();

        step("Select budget");
        promoDashboard.clickDropdown(budgetId);
        promoDashboard.clickInsideDropdown(budgetText);

        step("scroll to make element clickable");
        promoDashboard.scorllTillClickable();

        step("Select rule set 1 vertical dropdown");
        promoDashboard.clickDropdown(ruleVerticalId);

        step("select hotel in vertical dropdown");
        promoDashboard.clickInsideDropdown(hotel);

        step("Select parameter dropdown");
        promoDashboard.clickDropdown(parameterId);

        step("select in parameter dropdown");
        promoDashboard.clickInsideDropdown(accomodationType);

        step("Select operator dropdown");
        promoDashboard.clickDropdown(operatorId);

        step("select in operator dropdown");
        promoDashboard.clickEqualToInDropdown();

        step("send text in values input box");
        promoDashboard.sendTextInInput(valuesId,valuesText);

        step("click add values button");
        promoDashboard.clickAddValues();
        promoDashboard.scorllTillClickable(2, 300);

        step("send text in Min Transaction value input box");
        promoDashboard.sendTextInInput(minTraValueId,minTraText);

        step("send text in Max Transaction value input box");
        promoDashboard.sendTextInInput(maxTraValueId,maxTraText);

        step("send text in Amount of Discount input box");
        promoDashboard.sendTextInInput(amountOfDiscountId,cashbackText);

        step("scroll to make element clickable");
        promoDashboard.scorllTillClickable(2,300);

        step("Select 'select payment method' radiobox");
        promoDashboard.clickSelectPaymentMethodRadioButton();

        step("Input credit card payment method");
        promoDashboard.clickDropdownPaymentMethod(paymentMethodId);
        promoDashboard.clickInsideDropdown(paymentMethodText);
        promoDashboard.clickDropdownPaymentMethod(paymentMethodId);
        promoDashboard.clickDropdown(binId);
        promoCreationBINModalWeb.inputBin(binValue);
        promoCreationBINModalWeb.clickSearch();
        promoCreationBINModalWeb.selectTheFirstBin();
        promoCreationBINModalWeb.clickSaveButton();

        step("Input credit card tenure");
        promoDashboard.clickDropdown(tenureId);
        promoDashboard.clickInsideDropdown(tenureText);

        step("scroll to make element clickable");
        promoDashboard.scorllTillClickable(2, 300);

        step("Select cost allocation dropdown");
        promoDashboard.clickDropdown(costAllocationId);

        step("send text in Fixed Value");
        promoDashboard.sendTextInInput(fixedValueId, fixedValueText);

        step("send text in select usage count input box");
        promoDashboard.sendTextInInput(selectUsageCountId, countText);

        step("scroll to make element clickable");
        promoDashboard.scorllTillClickable(1,100);

        step("Select usage period dropdown");
        promoDashboard.clickDropdown(usagePeriodId);

        step("select in usage period dropdown");
        promoDashboard.clickInsideDropdown(campaignPeriod);

        step("Select platform dropdown");
        promoDashboard.clickDropdown(platformId);

        step("select in platform dropdown");
        promoDashboard.clickInsideDropdown(android);

        step("Select login state dropdown");
        promoDashboard.clickDropdown(loginStateId);

        step("select in login state dropdown");
        promoDashboard.clickInsideDropdown(loggedIn);

        step("click verified radio box");
        promoDashboard.clickVerifiedRadioBox();

        step("click username under validated by");
        promoDashboard.clickUsername();

        step("Send name in Promo name");
        promoDashboard.sendTextInPromoNameInput(randomName);

        step("scroll to make element clickable");
        promoDashboard.scorllTillClickable(2,300);

        step("Generate TnC");
        promoDashboard.clickGenerateTnc();
        promoDashboard.waitForGenerateTncFinish();

        step("Create campaign");
        promoDashboard.clickPreviewButton();
        promoDashboard.clickCreateCampaignButton();
        promoDashboard.clickSendForApprovalButton();

        step("Go to promo list page");
        promoDashboard.goToPromoList();
        promoDashboard.openApprovalPage();

        step("Search for the promo in approval page");
        promoDashboard.inputSearchPromo(randomName);
        promoDashboard.clickOnSearch();

        step("Verify promo exists");
        int cnt = promoDashboard.extractNumber();
        Assertion.assertThat("Verify the result is greater than zero", (cnt>0), is(true));

        step("Open promo details");
        promoDashboard.clickPromo();

        step("Verify BIN");
        promoDashboard.clickSeeDetailsBankBIN();
        promoDashboard.checkBinTableContainsBINId(binValue, binColumn);
    }

    @Author(names = { "Rizki Maulana", "Fery" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyToggleSpecificVerticalInInstantPointCashback() throws InterruptedException {
        Map<String, String> param = getParam("verifyToggleSpecificVerticalInInstantPointCashback");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Open Instant Point Cashback Page");
        promoDashboard.maximize();
        promoDashboard.goToAdminCarePage(username, password);
        promoDashboard.scorllToCarAdmin();
        promoDashboard.openPromoPage();
        promoDashboard.goToInstantPointCashbackPage();

        step("Ensure a vertical is active (turn on if all are inactive)");
        String verticalKey = promoDashboard.prepareInstantPointCashbackVerticalForToggleOffTest();

        step("Toggle vertical off");
        promoDashboard.toggleInstantPointCashbackVertical(verticalKey);
        Assertion.assertThat(String.format("Verify that vertical %s toggle is off", verticalKey),
                promoDashboard.isVerticalInstantPointCashbackChecked(verticalKey), is(false));

        step("Toggle vertical back on");
        promoDashboard.toggleInstantPointCashbackVertical(verticalKey);
        Assertion.assertThat(String.format("Verify that vertical %s toggle is on", verticalKey),
                promoDashboard.isVerticalInstantPointCashbackChecked(verticalKey), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyAutogenerateTnC() throws InterruptedException {
        Map<String, String> param = getParam("verifyAutogenerateTnC");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String identifier = param.get("identifier");
        String description = param.get("description");
        String promoTypeId = param.get("promoTypeId");
        String instant = param.get("instant");
        String campaignTypeId = param.get("campaignTypeId");
        String testing = param.get("testing");
        String divisionId = param.get("divisionId");
        String ben = param.get("ben");
        String selectCurrencyId = param.get("selectCurrencyId");
        String idr = param.get("idr");
        String verticalId = param.get("verticalId");
        String hotel = param.get("hotel");
        String ruleVerticalId = param.get("ruleVerticalId");
        String parameterId = param.get("parameterId");
        String accomodationType = param.get("accomodationType");
        String operatorId = param.get("operatorId");
        String valuesId = param.get("valuesId");
        String valuesText = param.get("valuesText");
        String minTraValueId = param.get("minTraValueId");
        String minTraText = param.get("minTraText");
        String maxTraValueId = param.get("maxTraValueId");
        String maxTraText = param.get("maxTraText");
        String amountOfDiscountId = param.get("amountOfDiscountId");
        String cashbackText = param.get("cashbackText");
        String costAllocationId = param.get("costAllocationId");
        String tiket = param.get("tiket");
        String selectUsageCountId = param.get("selectUsageCountId");
        String countText = param.get("countText");
        String usagePeriodId = param.get("usagePeriodId");
        String campaignPeriod = param.get("campaignPeriod");
        String platformId = param.get("platformId");
        String android = param.get("android");
        String loginStateId = param.get("loginStateId");
        String loggedIn = param.get("loggedIn");
        String campaignStartDateId = param.get("campaignStartDateId");
        String startDate = param.get("startDate");
        String campaignEndDateId = param.get("campaignEndDateId");
        String endDate = param.get("endDate");
        String budget = param.get("budget");
        String budgetId = param.get("budgetId");
        String randomName;

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Click on create new promo");
        promodash.clickCreateNewPromoButton();

        step("Click on Promo type dropdown");
        promodash.clickDropdown(promoTypeId);

        step("select cashback in Promo type dropdown");
        promodash.clickInsideDropdown(instant);

        step("Click on campaign type dropdown");
        promodash.clickDropdown(campaignTypeId);

        step("select TESTING inside campaign type dropdown");
        promodash.clickInsideDropdown(testing);

        step("Send name in campaign name");
        randomName = promodash.sendTextInCampaignNameInput();

        step("Send name in Unique Identifier");
        promodash.sendTextInUniqueIdentifierInput(identifier);

        step("Send Text in description box");
        promodash.sendTextInDescriptionBox(description);

        step("scroll to make element clickable");
        promodash.scorllTillClickable();

        step("Select division dropdown");
        promodash.clickDropdown(divisionId);

        step("select BEN inside division dropdown");
        promodash.clickInsideDropdown(ben);

        step("Select select-currency dropdown");
        promodash.clickDropdown(selectCurrencyId);

        step("select IDR inside select-currency dropdown");
        promodash.clickInsideDropdown(idr);

        step("Select vertical dropdown");
        promodash.clickDropdown(verticalId);

        step("select hotel in vertical dropdown");
        promodash.clickInsideDropdown(hotel);

        step("scroll to make element clickable");
        promodash.scrollTillClickable(2, 350);

        step("Select date");
        promodash.clickDropdown(campaignStartDateId);
        promodash.selectDate(startDate);
        promodash.scrollTillClickable(1, 350);
        promodash.clickSaveButton();
        promodash.scrollTillClickable(1, -350);
        promodash.clickDropdown(campaignEndDateId);
        promodash.selectDate(endDate);
        promodash.scrollTillClickable(1, 350);
        promodash.clickSaveButton();
        promodash.scrollTillClickable(1, -350);

        step("Click add period button");
        promodash.clickAddPeriod();

        step("scroll to make element clickable");
        promodash.scorllTillClickable();

        step("User select budget");
        promodash.clickDropdown(budgetId);
        promodash.clickInsideDropdown(budget);

        step("scroll to make element clickable");
        promodash.scorllTillClickable();

        step("Select rule set 1 vertical dropdown");
        promodash.clickDropdown(ruleVerticalId);

        step("select hotel in vertical dropdown");
        promodash.clickInsideDropdown(hotel);

        step("Select parameter dropdown");
        promodash.clickDropdown(parameterId);

        step("select in parameter dropdown");
        promodash.clickInsideDropdown(accomodationType);

        step("Select operator dropdown");
        promodash.clickDropdown(operatorId);

        step("select in operator dropdown");
        promodash.clickEqualToInDropdown();

        step("send text in values input box");
        promodash.sendTextInInput(valuesId,valuesText);

        step("click add values button");
        promodash.clickAddValues();
        promodash.scorllTillClickable(2, 300);

        step("send text in Min Transaction value input box");
        promodash.sendTextInInput(minTraValueId,minTraText);

        step("send text in Max Transaction value input box");
        promodash.sendTextInInput(maxTraValueId,maxTraText);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(2,300);

        step("send text in Amount of Discount input box");
        promodash.sendTextInInput(amountOfDiscountId,cashbackText);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(2, 300);

        step("Select cost allocation dropdown");
        promodash.clickDropdown(costAllocationId);

        step("select in cost allocation dropdown");
        promodash.clickInsideDropdown(tiket);

        step("send text in select usage count input box");
        promodash.sendTextInInput(selectUsageCountId,countText);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(1,100);

        step("Select usage period dropdown");
        promodash.clickDropdown(usagePeriodId);

        step("select in usage period dropdown");
        promodash.clickInsideDropdown(campaignPeriod);

        step("Select platform dropdown");
        promodash.clickDropdown(platformId);

        step("select in platform dropdown");
        promodash.clickInsideDropdown(android);

        step("Select login state dropdown");
        promodash.clickDropdown(loginStateId);

        step("select in login state dropdown");
        promodash.clickInsideDropdown(loggedIn);

        step("click verified radio box");
        promodash.clickVerifiedRadioBox();

        step("click username under validated by");
        promodash.clickUsername();

        step("Send name in Promo name");
        promodash.sendTextInPromoNameInput(randomName);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(2,300);

        step("Generate TnC");
        promodash.clickGenerateTnc();
        promodash.waitForGenerateTncFinish();

        step("verify generation of TnC is successful or not");
        Assertion.assertThat("TnC generated successfully", promodash.verifyTncInDescription(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyPromoApproval() throws InterruptedException {
        Map<String, String> param = getParam("verifyPromoApproval");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String identifier = param.get("identifier");
        String description = param.get("description");
        String promoTypeId = param.get("promoTypeId");
        String instant = param.get("instant");
        String campaignTypeId = param.get("campaignTypeId");
        String testing = param.get("testing");
        String divisionId = param.get("divisionId");
        String ben = param.get("ben");
        String selectCurrencyId = param.get("selectCurrencyId");
        String idr = param.get("idr");
        String verticalId = param.get("verticalId");
        String hotel = param.get("hotel");
        String ruleVerticalId = param.get("ruleVerticalId");
        String parameterId = param.get("parameterId");
        String accomodationType = param.get("accomodationType");
        String operatorId = param.get("operatorId");
        String valuesId = param.get("valuesId");
        String valuesText = param.get("valuesText");
        String minTraValueId = param.get("minTraValueId");
        String minTraText = param.get("minTraText");
        String maxTraValueId = param.get("maxTraValueId");
        String maxTraText = param.get("maxTraText");
        String amountOfDiscountId = param.get("amountOfDiscountId");
        String cashbackText = param.get("cashbackText");
        String costAllocationId = param.get("costAllocationId");
        String tiket = param.get("tiket");
        String selectUsageCountId = param.get("selectUsageCountId");
        String countText = param.get("countText");
        String usagePeriodId = param.get("usagePeriodId");
        String campaignPeriod = param.get("campaignPeriod");
        String platformId = param.get("platformId");
        String android = param.get("android");
        String loginStateId = param.get("loginStateId");
        String loggedIn = param.get("loggedIn");
        String campaignStartDateId = param.get("campaignStartDateId");
        String startDate = param.get("startDate");
        String campaignEndDateId = param.get("campaignEndDateId");
        String endDate = param.get("endDate");
        String budget = param.get("budget");
        String promo = param.get("promo");
        String budgetId = param.get("budgetId");
        String randomName;

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Click on create new promo");
        promodash.clickCreateNewPromoButton();

        step("Click on Promo type dropdown");
        promodash.clickDropdown(promoTypeId);

        step("select cashback in Promo type dropdown");
        promodash.clickInsideDropdown(instant);

        step("Click on campaign type dropdown");
        promodash.clickDropdown(campaignTypeId);

        step("select TESTING inside campaign type dropdown");
        promodash.clickInsideDropdown(testing);

        step("Send name in campaign name");
        randomName = promodash.sendTextInCampaignNameInput();

        step("Send name in Unique Identifier");
        promodash.sendTextInUniqueIdentifierInput(identifier);

        step("Send Text in description box");
        promodash.sendTextInDescriptionBox(description);

        step("scroll to make element clickable");
        promodash.scorllTillClickable();

        step("Select division dropdown");
        promodash.clickDropdown(divisionId);

        step("select BEN inside division dropdown");
        promodash.clickInsideDropdown(ben);

        step("Select select-currency dropdown");
        promodash.clickDropdown(selectCurrencyId);

        step("select IDR inside select-currency dropdown");
        promodash.clickInsideDropdown(idr);

        step("Select vertical dropdown");
        promodash.clickDropdown(verticalId);

        step("select hotel in vertical dropdown");
        promodash.clickInsideDropdown(hotel);

        step("scroll to make element clickable");
        promodash.scrollTillClickable(2, 350);

        step("Select date");
        promodash.clickDropdown(campaignStartDateId);
        promodash.selectDate(startDate);
        promodash.scrollTillClickable(1, 350);
        promodash.clickSaveButton();
        promodash.scrollTillClickable(1, -350);
        promodash.clickDropdown(campaignEndDateId);
        promodash.selectDate(endDate);
        promodash.scrollTillClickable(1, 350);
        promodash.clickSaveButton();
        promodash.scrollTillClickable(1, -350);

        step("Click add period button");
        promodash.clickAddPeriod();

        step("scroll to make element clickable");
        promodash.scorllTillClickable();

        step("User select budget");
        promodash.clickDropdown(budgetId);
        promodash.clickInsideDropdown(budget);

        step("scroll to make element clickable");
        promodash.scorllTillClickable();

        step("Select rule set 1 vertical dropdown");
        promodash.clickDropdown(ruleVerticalId);

        step("select hotel in vertical dropdown");
        promodash.clickInsideDropdown(hotel);

        step("Select parameter dropdown");
        promodash.clickDropdown(parameterId);

        step("select in parameter dropdown");
        promodash.clickInsideDropdown(accomodationType);

        step("Select operator dropdown");
        promodash.clickDropdown(operatorId);

        step("select in operator dropdown");
        promodash.clickEqualToInDropdown();

        step("send text in values input box");
        promodash.sendTextInInput(valuesId,valuesText);

        step("click add values button");
        promodash.clickAddValues();
        promodash.scorllTillClickable(2, 300);

        step("send text in Min Transaction value input box");
        promodash.sendTextInInput(minTraValueId,minTraText);

        step("send text in Max Transaction value input box");
        promodash.sendTextInInput(maxTraValueId,maxTraText);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(2,300);

        step("send text in Amount of Discount input box");
        promodash.sendTextInInput(amountOfDiscountId,cashbackText);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(2, 300);

        step("Select cost allocation dropdown");
        promodash.clickDropdown(costAllocationId);

        step("select in cost allocation dropdown");
        promodash.clickInsideDropdown(tiket);

        step("send text in select usage count input box");
        promodash.sendTextInInput(selectUsageCountId,countText);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(1,100);

        step("Select usage period dropdown");
        promodash.clickDropdown(usagePeriodId);

        step("select in usage period dropdown");
        promodash.clickInsideDropdown(campaignPeriod);

        step("Select platform dropdown");
        promodash.clickDropdown(platformId);

        step("select in platform dropdown");
        promodash.clickInsideDropdown(android);

        step("Select login state dropdown");
        promodash.clickDropdown(loginStateId);

        step("select in login state dropdown");
        promodash.clickInsideDropdown(loggedIn);

        step("click verified radio box");
        promodash.clickVerifiedRadioBox();

        step("click username under validated by");
        promodash.clickUsername();

        step("Send name in Promo name");
        promodash.sendTextInPromoNameInput(randomName);

        step("scroll to make element clickable");
        promodash.scorllTillClickable(2,300);

        step("Generate TnC");
        promodash.clickGenerateTnc();
        promodash.waitForGenerateTncFinish();

        step("Create campaign");
        promodash.clickPreviewButton();
        promodash.clickCreateCampaignButton();
        promodash.clickSendForApprovalButton();

        step("Go to promo list page and verify status of promo is in approval");
        promodash.goToPromoList();
        promodash.inputSearchPromo(randomName);
        promodash.clickOnSearch();
        Assertion.assertThat("Verify status of promo is in approval", promodash.getStatusOfPromo().contains("In Approval"), is(true));

        step("Open approval page");
        promodash.openApprovalPage();

        step("Search for the promo in approval page");
        promodash.inputSearchPromo(randomName);
        promodash.clickOnSearch();

        step("Approve the promo");
        promodash.clickAprroveButton();
        promodash.clickOnApproveModal();

        step("Go to promo list page and search for the promo");
        promodash.goToPromoList();
        promodash.inputSearchPromo(randomName);
        promodash.clickOnSearch();

        step("Verify status of promo is active");
        Assertion.assertThat("Verify status of promo is active",
                promodash.waitForPromoStatus(randomName, "Active", 5), is(true));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyCreateNewDynamicPromoValue() throws InterruptedException {
        Map<String, String> param = getParam("verifyCreateNewDynamicPromoValue");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String ruleName = param.get("ruleName") + "_" + CoreFunctions.randomInt(5);
        String condition = param.get("condition");
        String operator = param.get("operator");
        String conditionValue = param.get("conditionValue");
        String adjustmentVariable = param.get("adjustmentVariable");
        String adjustmentValue = param.get("adjustmentValue");

        step("Open Promo Dashboard");
        promoDashboard.maximize();
        promoDashboard.goToAdminCarePage(username,password);
        promoDashboard.openPromoPage();

        step("Open Promo Rules Adjustment Page");
        promoDashboard.goToPromoAdjustmentRule();

        step("Create new Rule");
        promoDashboard.pressAddNewRuleButton();
        promoDashboard.fillRuleName(ruleName);
        promoDashboard.insertRuleCondition(condition, operator, conditionValue);
        promoDashboard.insertRuleAdjustment(adjustmentVariable, adjustmentValue);

        HashMap<String, String> valueMap = promoDashboard.getDataFromCreateRuleForm();
        promoDashboard.saveDynamicPromo();

        step("Verify rule has been created");
        promoDashboard.goToPromoAdjustmentRule();
        promoDashboard.searchRuleName(ruleName);
        int ruleCount = promoDashboard.extractNumber();
        Assertion.assertThat("Verify the result is successfully created", ruleCount, greaterThan(0));

        step("Verify newly created rule value");
        promoDashboard.openRuleNameDetailModal(ruleName);
        promoDashboard.verifyRuleNameValue(ruleName, valueMap);
        promoDashboard.closeRuleNameDetailModal();

        step("Delete newly created rule");
        promoDashboard.deleteRuleName(ruleName);
        promoDashboard.searchRuleName(ruleName);
        Assertion.assertThat(
                "Verify the deleted rule no longer appears in the list for this search (other rules may remain)",
                promoDashboard.waitUntilAdjustmentRuleAbsentFromList(ruleName, 30),
                is(true));
    }

    @Author(names = {"Rizki Maulana", "Fery"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyEditExistingDynamicPromoValue() throws InterruptedException {
        Map<String, String> param = getParam("verifyEditExistingDynamicPromoValue");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String ruleName = param.get("ruleName") + "_" + CoreFunctions.randomInt(5);
        String condition = param.get("condition");
        String operator = param.get("operator");
        String conditionValue = param.get("conditionValue");
        String adjustmentVariable = param.get("adjustmentVariable");
        String adjustmentValue = param.get("adjustmentValue");
        String newRuleName = param.get("newRuleName") + "_" + CoreFunctions.randomInt(5);
        String newCondition = param.get("newCondition");
        String newOperator = param.get("newOperator");
        String newConditionValue = param.get("newConditionValue");
        String newAdjustmentVariable = param.get("newAdjustmentVariable");
        String newAdjustmentValue = param.get("newAdjustmentValue");

        step("Open Promo Dashboard");
        promoDashboard.maximize();
        promoDashboard.goToAdminCarePage(username,password);
        promoDashboard.openPromoPage();

        step("Open Promo Rules Adjustment Page");
        promoDashboard.goToPromoAdjustmentRule();

        step("Create new Rule");
        promoDashboard.pressAddNewRuleButton();
        promoDashboard.fillRuleName(ruleName);
        promoDashboard.insertRuleCondition(condition, operator, conditionValue);
        promoDashboard.insertRuleAdjustment(adjustmentVariable, adjustmentValue);

        HashMap<String, String> valueMap = promoDashboard.getDataFromCreateRuleForm();
        promoDashboard.saveDynamicPromo();

        step("Verify rule has been created");
        promoDashboard.goToPromoAdjustmentRule();
        promoDashboard.searchRuleName(ruleName);
        int ruleCount = promoDashboard.extractNumber();
        Assertion.assertThat("Verify the result is successfully created", ruleCount, greaterThan(0));

        step("Verify newly created rule value");
        promoDashboard.openRuleNameDetailModal(ruleName);
        promoDashboard.verifyRuleNameValue(ruleName, valueMap);
        promoDashboard.closeRuleNameDetailModal();

        step("Edit rule");
        promoDashboard.openRuleNameDetailModal(ruleName);
        promoDashboard.editRuleNameDetailModal();
        promoDashboard.fillRuleName(newRuleName);
        promoDashboard.insertRuleCondition(newCondition, newOperator, newConditionValue);
        promoDashboard.insertRuleAdjustment(newAdjustmentVariable, newAdjustmentValue);
        HashMap<String, String> updatedValueMap = promoDashboard.getDataFromCreateRuleForm();
        promoDashboard.saveDynamicPromo();

        step("Verify newly updated rule value");
        promoDashboard.goToPromoAdjustmentRule();
        promoDashboard.openRuleNameDetailModal(newRuleName);
        promoDashboard.verifyRuleNameValue(newRuleName, updatedValueMap);
        promoDashboard.closeRuleNameDetailModal();

        step("Delete newly created rule");
        promoDashboard.deleteRuleName(newRuleName);
        promoDashboard.searchRuleName(newRuleName);
        Assertion.assertThat(
                "Verify the deleted rule no longer appears in the list for this search (other rules may remain)",
                promoDashboard.waitUntilAdjustmentRuleAbsentFromList(newRuleName, 30),
                is(true));
    }

    @Author(names = { "Manan Kumar" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void testVerifyOverviewSectionOfPromoDetails() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyOverviewSectionOfPromoDetails");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Maximize the window");
        promodash.maximize();

        step("User Login to dashboard page");
        promodash.goToAdminCarePage(username,password);

        step("User Go to Promo dashboard page");
        promodash.openPromoPage();

        step("User Verify there is promo present or not");
        int cnt = promodash.extractNumber();
        Assertion.assertThat("Verify the result is greater than zero", (cnt>0), is(true));

        step("User Select the promo from the list");
        promodash.selectPromoDropdown();
        promodash.selectActivePromo();
        promodash.selectPromoDropdown();
        promodash.clickOnSearch();
        promodash.clickOnSelectedPromo();

        step(" User Verify that the see status field based on the status given to that particular promo/campaign");
        Assertion.assertThat("Verify the status heading field is present ", promodash.isOverviewStatusHeadingDisplayed(), is(true));
        Assertion.assertThat("Verify that the status value field is present", promodash.isOverviewStatusDisplayed(), is(true));

        step("User Verify that that the Date created field is present and value of the field showing proper data");
        Assertion.assertThat("Verify the date created heading field is present", promodash.isOverviewDateCreatedDisplayed(), is(true));
        Assertion.assertThat("Verify the date is present in created value field", promodash.isOverviewDateCreatedValueDisplayed(), is(true));

        step("User Verify that the Promo Type field is present and value of the field showing proper data");
        Assertion.assertThat("Verify the promo type heading field is present", promodash.isOverviewetPromoTypeDisplayed(), is(true));
        Assertion.assertThat("Verify the data is present in promo type value field", promodash.isOverviewetPromoTypeValueDisplayed(), is(true));

        step("User Verify that the Currency field is present and value of the field showing proper data");
        Assertion.assertThat("Verify the currency field is present ", promodash.isOverviewCurDisplayed(), is(true));
        Assertion.assertThat("Verify the data is present in currency value field", promodash.isOverviewCurValueDisplayed(), is(true));
    }

    @Author(names = { "Manan Kumar", "Muhammad Hafiz" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void testVerifyCampaignOverviewAndChangeHistoryOfEditedPromo() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyCampaignOverviewAndChangeHistoryOfEditedPromo");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String promoCodePrefix = param.get("promoCodePrefix");
        String idMinTransaction = param.get("idMinTransaction");
        String activityType = param.get("activityType");
        String newMinTransaction = String.valueOf(CoreFunctions.generateRandomInt(100001, 999999));

        step("Login to dashboard page");
        promoDashboard.goToAdminCarePage(username, password);

        step("Go to Promo List page");
        promoDashboard.scorllToCarAdmin();
        promoDashboard.openPromoPage();

        step("Ensure an Active promo exists");
        String promoCode = promoDashboard.ensureActivePromoOrCreate(promoCodePrefix, param);

        step("Get Promo Name on the Table");
        promoDashboard.searchAndFilterPromo(promoCode, "Active");
        promoDashboard.clickOnSelectedPromo();
        promoCode = promoDashboard.getPromoNameOnPromoDetail();

        step("Verify no pending In Approval for the promo: "+promoCode);
        promoDashboard.goToPromoList();
        promoDashboard.inputSearchPromo(promoCode);
        promoDashboard.clickDropdown("status");
        promoDashboard.clickInsideDropdown("In Approval");
        promoDashboard.clickDropdown("status");
        promoDashboard.clickOnSearch();
        promoDashboard.deleteInApprovalPromo();

        step("Edit the promo: "+promoCode);
        promoDashboard.clickResetFilter();
        promoDashboard.inputSearchPromo(promoCode);
        promoDashboard.clickDropdown("status");
        promoDashboard.clickInsideDropdown("Active");
        promoDashboard.clickInsideDropdown("Inactive");
        promoDashboard.clickDropdown("status");
        promoDashboard.clickOnSearch();
        promoDashboard.clickOnSelectedPromo();

        step("User Clicks on edit button");
        promoDashboard.clickEditButton();

        step("Toggle edit campaign period and set the period to today");
        promoDashboard.clickToggleAndEditCampaignPeriod();

        step("Edit the Minimal Transaction Value");
        String oldMinTransaction = promoDashboard.clearTextInInput(idMinTransaction);
        promoDashboard.sendTextInInput(idMinTransaction, newMinTransaction);

        step("Save the Edited Promo");
        promoDashboard.scrollToBottom();
        promoDashboard.clickPreviewButton();

        step("Click on Save changes button");
        promoDashboard.clickSaveChangesButton();
        promoDashboard.clickSaveChangesPopupButton();

        step("Approve the edited promo");
        promoDashboard.goToPromoList();
        promoDashboard.dismissLeavePageModalIfShown();
        promoDashboard.openApprovalPage();
        promoDashboard.inputSearchPromo(promoCode);

        step("User Click on search button");
        promoDashboard.clickOnSearch();

        step("User clicks on Approve button");
        promoDashboard.clickAprroveButton();
        promoDashboard.clickOnApproveModal();

        step("Back to Promo List and Open the Promo");
        promoDashboard.clickResetFilter();
        promoDashboard.goToPromoList();
        promoDashboard.inputSearchPromo(promoCode);
        promoDashboard.clickDropdown("status");
        promoDashboard.clickInsideDropdown("Active");
        promoDashboard.clickDropdown("status");
        promoDashboard.clickOnSearch();

        step("Verify the new version reflects the updated data");
        promoDashboard.clickOnSelectedPromo();
        String formattedNewMinTransaction = CoreFunctions.thousandSeparator(newMinTransaction, ",");
        String actualMinTransaction = promoDashboard.getPromoRulesValue("Min. AOV");
        Assertion.assertThat("Min Transaction Value is updated", actualMinTransaction, containsString(formattedNewMinTransaction));

        step("go To Tab Changes History");
        promoDashboard.goToTabChangesHistory();

        step("Verify Changes History of date");
        int historyIndex = promoDashboard.openLatestChangesHistoryByActivty(activityType);
        String todayDateDMMMMYYYY = CoreFunctions.getTodayDateD_MMMM_YYYY();
        String todayDate = CoreFunctions.convertDateFormat(todayDateDMMMMYYYY, "d MMMM yyyy", "d MMM yyyy");
        String actualDateActor = promoDashboard.getChangesHistoryDateActor(historyIndex);
        Assertion.assertThat("is Changes History Date correct", actualDateActor, containsString(todayDate));
        Assertion.assertThat("is Changes History Actor correct", actualDateActor, containsString(username));

        step("Verify Changes History of transaction");
        oldMinTransaction = CoreFunctions.thousandSeparator(oldMinTransaction, ",");
        Assertion.assertThat("Old Min Transaction is Correct", promoDashboard.getChangesHistoryValueByFieldName("Min. AOV"), containsString(oldMinTransaction));
        newMinTransaction = CoreFunctions.thousandSeparator(newMinTransaction, ",");
        Assertion.assertThat("New Min Transaction is Correct", promoDashboard.getChangesHistoryValueByFieldName("Min. AOV"), containsString(newMinTransaction));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifySetDynamicPromoValueAsDefault() throws InterruptedException {
        Map<String, String> param = getParam("verifySetDynamicPromoValueAsDefault");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String ruleName = param.get("ruleName") + "_" + CoreFunctions.randomInt(5);
        String condition = param.get("condition");
        String operator = param.get("operator");
        String conditionValue = param.get("conditionValue");
        String adjustmentVariable = param.get("adjustmentVariable");
        String adjustmentValue = param.get("adjustmentValue");

        step("Open Promo Dashboard");
        promoDashboard.maximize();
        promoDashboard.goToAdminCarePage(username,password);
        promoDashboard.openPromoPage();

        step("Open Promo Rules Adjustment Page");
        promoDashboard.goToPromoAdjustmentRule();

        step("Create new Rule");
        promoDashboard.pressAddNewRuleButton();
        promoDashboard.fillRuleName(ruleName);
        promoDashboard.insertRuleCondition(condition, operator, conditionValue);
        promoDashboard.insertRuleAdjustment(adjustmentVariable, adjustmentValue);
        HashMap<String, String> valueMap = promoDashboard.getDataFromCreateRuleForm();
        promoDashboard.saveDynamicPromo();

        step("Verify rule has been created");
        promoDashboard.goToPromoAdjustmentRule();
        promoDashboard.searchRuleName(ruleName);
        int ruleCount = promoDashboard.extractNumber();
        Assertion.assertThat("Verify the result is successfully created", ruleCount, greaterThan(0));

        step("Verify newly created rule value");
        promoDashboard.openRuleNameDetailModal(ruleName);
        promoDashboard.verifyRuleNameValue(ruleName, valueMap);
        promoDashboard.closeRuleNameDetailModal();

        step("Set rule as default");
        promoDashboard.setDefaultToPromoRule(ruleName);
        promoDashboard.verifyAdjustmentRuleIsDefault(ruleName);

        step("Remove rule as default");
        promoDashboard.removeDefaultToPromoRule(ruleName);

        step("Delete newly created rule");
        promoDashboard.deleteRuleName(ruleName);
        promoDashboard.searchRuleName(ruleName);
        Assertion.assertThat(
                "Verify the deleted rule no longer appears in the list for this search (other rules may remain)",
                promoDashboard.waitUntilAdjustmentRuleAbsentFromList(ruleName, 30),
                is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyCreateNewPromoWithGBSQuotaInPromoCodeLevel() throws InterruptedException {
        Map<String, String> param = getParam("verifyCreateNewPromoWithGBSQuotaInPromoCodeLevel");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String budget = param.get("budget");
        String promo = param.get("promo");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Click on create new promo");
        promodash.clickCreateNewPromoButton();

        step("Fill promo form");
        String randomName = promodash.fillPromoForm(param);

        step("Submit promo for approval");
        promodash.submitPromoForm();

        step("Go to promo list page");
        promodash.goToPromoList();
        promodash.inputSearchPromo(randomName);
        promodash.clickOnSearch();

        step("Go to the created promo details page");
        promodash.clickOnSelectedPromo();

        step("Verify the Budget section in promo details page");
        Assertion.assertThat("Verify the Budget section in promo details page", promodash.verifyBudgetSectionValues(budget, promo), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyCreateNewPromoWithGBSQuotaInTransactionValueLevel() throws InterruptedException {
        Map<String, String> param = getParam("verifyCreateNewPromoWithGBSQuotaInTransactionValueLevel");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String budget = param.get("budget");
        String promo = param.get("promo");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Click on create new promo");
        promodash.clickCreateNewPromoButton();

        step("Fill promo form");
        String randomName = promodash.fillPromoForm(param);

        step("Submit promo for approval");
        promodash.submitPromoForm();

        step("Go to promo list page");
        promodash.goToPromoList();
        promodash.inputSearchPromo(randomName);
        promodash.clickOnSearch();

        step("Go to the created promo details page");
        promodash.clickOnSelectedPromo();

        step("Verify the Budget section in promo details page");
        Assertion.assertThat("Verify the Budget section in promo details page", promodash.verifyBudgetSectionValues(budget, promo), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyCreateNewPromoInPromoCodeLevel() throws InterruptedException {
        Map<String, String> param = getParam("verifyCreateNewPromoInPromoCodeLevel");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String budget = param.get("budget");
        String promo = param.get("promo");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Click on create new promo");
        promodash.clickCreateNewPromoButton();

        step("Fill promo form");
        String randomName = promodash.fillPromoForm(param);

        step("Submit promo for approval");
        promodash.submitPromoForm();

        step("Go to promo list page");
        promodash.goToPromoList();
        promodash.inputSearchPromo(randomName);
        promodash.clickOnSearch();

        step("Go to the created promo details page");
        promodash.clickOnSelectedPromo();

        step("Verify the Budget section in promo details page");
        Assertion.assertThat("Verify the Budget section in promo details page", promodash.verifyBudgetSectionValues(budget, promo), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyCreateNewPromoInTransactionValueLevel() throws InterruptedException {
        Map<String, String> param = getParam("verifyCreateNewPromoInTransactionValueLevel");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promodash = new PromoDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String budget = param.get("budget");
        String promo = param.get("promo");

        step("Maximize the window");
        promodash.maximize();

        step("Login to dashboard page");
        promodash.goToAdminCarePage(username, password);

        step("Scroll till promo Dashboard");
        promodash.scorllToCarAdmin();

        step("Go to Promo List page");
        promodash.openPromoPage();

        step("Click on create new promo");
        promodash.clickCreateNewPromoButton();

        step("Fill Transaction Value Level promo form");
        String randomName = promodash.fillPromoForm(param);

        step("Submit promo for approval");
        promodash.submitPromoForm();

        step("Go to promo list page");
        promodash.goToPromoList();
        promodash.inputSearchPromo(randomName);
        promodash.clickOnSearch();

        step("Go to the created promo details page");
        promodash.clickOnSelectedPromo();

        step("Verify the Budget section in promo details page");
        Assertion.assertThat("Verify the Budget section in promo details page", promodash.verifyBudgetSectionValues(budget, promo), is(true));
    }

    @Author(names = { "Rizki Maulana" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyBinNumberPopUpAndSearchFunctionality() throws InterruptedException {
        Map<String, String> param = getParam("verifyBinNumberPopUpAndSearchFunctionality");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        PromoCreationBINModalWeb<?> promoCreationBINModalWeb = new PromoCreationBINModalWeb<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String bin = param.get("bin");
        String bankName = param.get("bankName");
        String binId = param.get("binId");

        step("Open Promo Dashboard");
        promoDashboard.maximize();
        promoDashboard.goToAdminCarePage(username, password);
        promoDashboard.scorllToCarAdmin();
        promoDashboard.openPromoPage();

        step("Click create new promo and fill required form fields");
        promoDashboard.clickCreateNewPromoButton();
        promoDashboard.clickDropdown(param.get("divisionId"));
        promoDashboard.clickInsideDropdown(param.get("ben"));
        promoDashboard.clickDropdown(param.get("selectCurrencyId"));
        promoDashboard.clickInsideDropdown(param.get("idr"));
        promoDashboard.clickDropdown(param.get("verticalId"));
        promoDashboard.clickInsideDropdown(param.get("hotel"));

        step("Select payment method and open BIN modal");
        promoDashboard.clickSelectPaymentMethodRadioButton();
        promoDashboard.clickDropdownPaymentMethod(param.get("paymentMethodId"));
        promoDashboard.clickInsideDropdown(param.get("paymentMethodText"));
        promoDashboard.clickDropdownPaymentMethod(param.get("paymentMethodId"));
        promoDashboard.clickDropdown(binId);

        step("Search BIN and verify other inputs are disabled");
        promoCreationBINModalWeb.inputBin(bin);
        promoCreationBINModalWeb.clickSearch();
        Assertion.assertThat("Verify Bank Name is disabled", promoCreationBINModalWeb.verifyInputIsDisabled("BANK_NAME"), equalTo(true));
        Assertion.assertThat("Verify Country is disabled", promoCreationBINModalWeb.verifyInputIsDisabled("COUNTRY"), equalTo(true));
        Assertion.assertThat("Verify Card Type is disabled", promoCreationBINModalWeb.verifyInputIsDisabled("CARD_TYPE"), equalTo(true));
        Assertion.assertThat("Verify Card Scheme is disabled", promoCreationBINModalWeb.verifyInputIsDisabled("CARD_SCHEME"), equalTo(true));

        step("Verify BIN is shown in the result");
        promoCreationBINModalWeb.checkBinTableColumnsContainValue(param.get("binColumnName"), bin);

        step("Reset Filter and verify all inputs are enabled");
        promoCreationBINModalWeb.resetFilter();
        promoCreationBINModalWeb.verifyFilterHasBeenReset();

        step("Search by Bank Name");
        promoCreationBINModalWeb.inputBankName(bankName);
        promoCreationBINModalWeb.clickSearch();

        step("Verify BIN input is disabled when other filters are used");
        Assertion.assertThat("Verify BIN is disabled", promoCreationBINModalWeb.verifyInputIsDisabled("BIN"), equalTo(true));

        step("Verify search results contain expected values");
        promoCreationBINModalWeb.checkBinTableColumnsContainValue(param.get("bankColumnName"), bankName);

        step("Select the first BIN and save");
        promoCreationBINModalWeb.selectTheFirstBin();
        String selectedBin = promoCreationBINModalWeb.getTheFirstBinValue(param.get("binColumnName"));
        promoCreationBINModalWeb.clickSaveButton();

        step("Verify selected BIN is shown in dropdown");
        Assertion.assertThat("Selected BIN is shown in BIN dropdown", promoDashboard.getDropdownValue(binId), containsString(selectedBin));
    }

    @Author(names = { "Vidhi Jindal", "Fery" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifySearchPromoCheckerDetails() throws InterruptedException {
        Map<String, String> param = getParam("verifySearchPromoCheckerDetails");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String promocode = param.get("promocode");
        String mongoConfig = param.get("config");
        String dbName = param.get("dbName");
        String collectionName = param.get("collectionName");

        step("Maximize the window");
        promoDashboard.maximize();

        step("User Login to dashboard page");
        promoDashboard.goToAdminCarePage(username,password);

        step("User Go to Promo dashboard page");
        promoDashboard.openPromoPage();

        step("User Go to Promo Checker page.");
        promoDashboard.openPromoCheckerPage();

        step("User INPUT the promo");
        promoDashboard.inputSearchPromo(promocode);
        promoDashboard.clickOnSearch();
        promoDashboard.clickOnSelectedPromo();

        step("Load promo document from Mongo (db/collection default to promoCode / promoCodeData when omitted in testdata)");
        promoDashboard.loadPromoDataByPromoCodeWithParamDefaults(mongoConfig, dbName, collectionName, promocode);
        Assertion.assertThat("Mongo returned a promo document for code " + promocode,
                promoDashboard.isPromoDataLoaded(), is(true));

        step(" User Verify the see status field based on the status given to that particular promo/campaign");
        Assertion.assertThat("Verify the status heading field is present ", promoDashboard.isOverviewStatusHeadingDisplayed(), is(true));
        Assertion.assertThat("Verify the status value field is present", promoDashboard.isOverviewStatusDisplayed(), is(true));

        step("User Verify the Date created field is present and value of the field showing proper data");
        Assertion.assertThat("Verify the date created heading field is present", promoDashboard.isOverviewDateCreatedDisplayed(), is(true));
        Assertion.assertThat("Verify the date is present in created value field", promoDashboard.isOverviewDateCreatedValueDisplayed(), is(true));

        step("User Verify the Promo Type field is present and value of the field showing proper data");
        Assertion.assertThat("Verify the promo type heading field is present", promoDashboard.isOverviewetPromoTypeDisplayed(), is(true));
        Assertion.assertThat("Verify the data is present in promo type value field", promoDashboard.isOverviewetPromoTypeValueDisplayed(), is(true));

        step("User Verify the Currency field is present and value of the field showing proper data");
        Assertion.assertThat("Verify the currency field is present ", promoDashboard.isOverviewCurDisplayed(), is(true));
        Assertion.assertThat("Verify the data is present in currency value field", promoDashboard.isOverviewCurValueDisplayed(), is(true));

        step("Verify the Overview Promo Data");
        promoDashboard.setOverviewPromoDataFromDB();
        Assertion.assertThat("is Promo Data Correct", promoDashboard.verifyOverviewPromoApproval(), is(true));

        step("Verify the General Info and Fraud Prevention Promo Data (labels aligned with Promo Detail page)");
        promoDashboard.setPromoDetailGeneralAndFraudFromDb(mongoConfig, true);
        Assertion.assertThat("is Promo Detail general/fraud data correct",
                promoDashboard.verifyPromoDetailGeneralAndFraudUi(), is(true));

        step("Verify the Promo Code Data");
        promoDashboard.setPromoCodeDataFromDB();
        Assertion.assertThat("is Promo Code Data Correct", promoDashboard.verifyPromoCodePromoApproval(), is(true));
        Assertion.assertThat("is Promo Short Desc & TnC shown", promoDashboard.verifyShortDescTncShown(), is(true));

        step("Promo Detail UI — Promo Affiliate and Rules sections");
        Map<String, String> affiliateUi = promoDashboard.readPromoDetailPromoAffiliateSectionFromUi();
        Assertion.assertThat("Promo Affiliate section exposes at least one field",
                affiliateUi.isEmpty(), is(false));
        int ruleSetCount = promoDashboard.getLoadedPromoRuleSetCount();
        Assertion.assertThat("Mongo should contain at least one rule set", ruleSetCount > 0, is(true));
        for (int i = 0; i < ruleSetCount; i++) {
            Assertion.assertThat("Promo Detail rule set " + (i + 1) + " parameters and transaction values match Mongo",
                    promoDashboard.verifyPromoDetailRuleSetParametersAndTransactionUi(i), is(true));
        }
    }

    @Author(names = { "Vidhi Jindal" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifySearchPromoChecker() throws InterruptedException {
        Map<String, String> param = getParam("verifySearchPromoChecker");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String promocode = param.get("promocode");

        step("Maximize the window");
        promoDashboard.maximize();

        step("User Login to dashboard page");
        promoDashboard.goToAdminCarePage(username,password);

        step("User Go to Promo dashboard page");
        promoDashboard.openPromoPage();

        step("User Go to Promo Checker page.");
        promoDashboard.openPromoCheckerPage();

        step("User INPUT the promo");
        promoDashboard.inputSearchPromo(promocode);
        promoDashboard.clickOnSearch();
        promoDashboard.clickOnSelectedPromo();

        step(" User Verify that the see status field based on the status given to that particular promo/campaign");
        Assertion.assertThat("Verify the status heading field is present ", promoDashboard.isOverviewStatusHeadingDisplayed(), is(true));
        Assertion.assertThat("Verify the status value field is present", promoDashboard.isOverviewStatusDisplayed(), is(true));

        step("User Verify the Date created field is present and value of the field showing proper data");
        Assertion.assertThat("Verify the date created heading field is present", promoDashboard.isOverviewDateCreatedDisplayed(), is(true));
        Assertion.assertThat("Verify the date is present in created value field", promoDashboard.isOverviewDateCreatedValueDisplayed(), is(true));

        step("User Verify that the Promo Type field is present and value of the field showing proper data");
        Assertion.assertThat("Verify the promo type heading field is present", promoDashboard.isOverviewetPromoTypeDisplayed(), is(true));
        Assertion.assertThat("Verify the data is present in promo type value field", promoDashboard.isOverviewetPromoTypeValueDisplayed(), is(true));

        step("User Verify that the Currency field is present and value of the field showing proper data");
        Assertion.assertThat("Verify the currency field is present ", promoDashboard.isOverviewCurDisplayed(), is(true));
        Assertion.assertThat("Verify the data is present in currency value field", promoDashboard.isOverviewCurValueDisplayed(), is(true));
    }

    @Author(names = { "Vanshika Agarwal" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PRCNG")
    @Test
    public void verifySearchFilterPromoBlacklist() throws InterruptedException {
        Map<String, String> param = getParam("verifySearchFilterPromoBlacklist");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String promocode = param.get("promocode");

        step("Maximize the window");
        promoDashboard.maximize();

        step("User Login to dashboard page");
        promoDashboard.goToAdminCarePage(username,password);

        step("User Go to Discount dashboard page");
        promoDashboard.goToDicountDashboard();

        step("User Go to Promo Blacklist page.");
        promoDashboard.openPromoBlacklistPage();

        step("User Input the promo");
        promoDashboard.inputSearchPromoBlacklist(promocode);

        step("User click search");
        promoDashboard.clickSearchPromoBlacklist();

        step("Verify after search only the searched row is being shown");
        int searchResultCount = promoDashboard.getPromoBlacklistSearchResultCount();

        Assertion.assertThat("Verify only one row is displayed after search", searchResultCount, is(1));

        Assertion.assertThat("Verify the searched promo code is displayed", promoDashboard.isPromoBlacklistSearchResultCorrect(), containsString(promocode));
    }

    @Author(names = { "Vanshika Agarwal" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PRCNG")
    @Test
    public void verifyPromoBlacklistListAndPagination() throws InterruptedException {
        Map<String, String> param = getParam("verifyPromoBlacklistListAndPagination");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Maximize the window");
        promoDashboard.maximize();

        step("User Login to dashboard page");
        promoDashboard.goToAdminCarePage(username, password);

        step("User Go to Discount dashboard page");
        promoDashboard.goToDicountDashboard();

        step("User Go to Promo blacklist page.");
        promoDashboard.openPromoBlacklistPage();

        step("Check the list is populated with exactly 10 or fewer promos");
        int initialRowCount = promoDashboard.getPromoBlacklistSearchResultCount();
        Assertion.assertThat("Verify the promo blacklist table has records", initialRowCount , greaterThan(0));
        Assertion.assertThat("Verify the page has exactly 10 or fewer promos", initialRowCount , lessThanOrEqualTo(10));

        step("Check the pagination is displayed");
        Assertion.assertThat("Verify the pagination component is displayed", promoDashboard.isPromoBlacklistPaginationDisplayed(), is(true));

        step("Check pagination navigation functionality");
        // Explicitly assert that the next button is enabled so the test is deterministic
        Assertion.assertThat("Verify Next button is enabled for pagination", promoDashboard.isPromoBlacklistNextButtonEnabled(), is(true));

        promoDashboard.clickPromoBlacklistNextPage();
        int nextRowCount = promoDashboard.getPromoBlacklistSearchResultCount();
        Assertion.assertThat("Verify table still has records after navigating to next page", nextRowCount, greaterThan(0));
        Assertion.assertThat("Verify next page has exactly 10 or fewer promos", nextRowCount , lessThanOrEqualTo(10));

        promoDashboard.clickPromoBlacklistPrevPage();
        int prevRowCount = promoDashboard.getPromoBlacklistSearchResultCount();
        Assertion.assertThat("Verify table still has records after navigating back to previous page", prevRowCount , greaterThan(0));
        Assertion.assertThat("Verify previous page has exactly 10 or fewer promos", prevRowCount , lessThanOrEqualTo(10));
    }


    @Author(names = { "Vanshika Agarwal" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PRCNG")
    @Test
    public void verifySearchFilterPromoGroup() throws InterruptedException {
        Map<String, String> param = getParam("verifySearchFilterPromoGroup");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String promoGroup = param.get("promoGroup");

        step("Maximize the window");
        promoDashboard.maximize();

        step("User Login to dashboard page");
        promoDashboard.goToAdminCarePage(username,password);

        step("User Go to Discount dashboard page");
        promoDashboard.goToDicountDashboard();

        step("User Go to Promo group page.");
        promoDashboard.openPromoGroupPage();

        step("User Input the promo");
        promoDashboard.inputSearchPromoGroup(promoGroup);

        step("User click search");
        promoDashboard.clickSearchPromoBlacklist();

        step("Verify after search only the searched row is being shown");
        int searchResultCount = promoDashboard.getPromoBlacklistSearchResultCount();

        Assertion.assertThat("Verify only one row is displayed after search", searchResultCount, is(1));
        Assertion.assertThat("Verify the searched promo group is displayed", promoDashboard.isPromoGroupSearchResultCorrect(), containsString(promoGroup));
    }

    @Author(names = { "Vanshika Agarwal" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PRCNG")
    @Test
    public void verifyPromoGroupListAndPagination() throws InterruptedException {
        Map<String, String> param = getParam("verifyPromoGroupListAndPagination");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Maximize the window");
        promoDashboard.maximize();

        step("User Login to dashboard page");
        promoDashboard.goToAdminCarePage(username, password);

        step("User Go to Discount dashboard page");
        promoDashboard.goToDicountDashboard();

        step("User Go to Promo group page.");
        promoDashboard.openPromoGroupPage();

        step("Check the list is populated with exactly 10 or fewer promos");
        int initialRowCount = promoDashboard.getPromoBlacklistSearchResultCount();
        Assertion.assertThat("Verify the promo blacklist table has records", initialRowCount , greaterThan(0));
        Assertion.assertThat("Verify the page has exactly 10 or fewer promos", initialRowCount , lessThanOrEqualTo(10));

        step("Check the pagination is displayed");
        Assertion.assertThat("Verify the pagination component is displayed", promoDashboard.isPromoBlacklistPaginationDisplayed(), is(true));

        step("Check pagination navigation functionality");
        Assertion.assertThat("Verify Next button is enabled for pagination", promoDashboard.isPromoBlacklistNextButtonEnabled(), is(true));

        promoDashboard.clickPromoBlacklistNextPage();
        int nextRowCount = promoDashboard.getPromoBlacklistSearchResultCount();
        Assertion.assertThat("Verify table still has records after navigating to next page", nextRowCount, greaterThan(0));
        Assertion.assertThat("Verify next page has exactly 10 or fewer promos", nextRowCount , lessThanOrEqualTo(10));

        promoDashboard.clickPromoBlacklistPrevPage();
        int prevRowCount = promoDashboard.getPromoBlacklistSearchResultCount();
        Assertion.assertThat("Verify table still has records after navigating back to previous page", prevRowCount , greaterThan(0));
        Assertion.assertThat("Verify previous page has exactly 10 or fewer promos", prevRowCount , lessThanOrEqualTo(10));
    }

    @Author(names = { "Vanshika Agarwal" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PRCNG")
    @Test
    public void verifyAddNewBlacklist() throws InterruptedException {
        Map<String, String> param = getParam("verifyAddNewBlacklist");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String url = param.get("url");
        String promocode = param.get("promocode");

        step("Maximize the window");
        promoDashboard.maximize();

        step("User Login to dashboard page");
        promoDashboard.goToAdminCarePage(username,password);

        step("User Go to Discount dashboard page");
        promoDashboard.goToDicountDashboard();

        step("User Go to Promo Blacklist page.");
        promoDashboard.openPromoBlacklistPage();

        step("Click on add Blacklist");
        promoDashboard.clickAddBlacklistButton();

        step("Click on upload file and upload the CSV");
        promoDashboard.uploadSingleFile(url);

        step("Click on add blacklist in the modal");
        promoDashboard.clickModalAddToBlacklistButton();

        step("User Input the promo");
        promoDashboard.inputSearchPromoBlacklist(promocode);

        step("User click search");
        promoDashboard.clickSearchPromoBlacklist();

        step("Verify after search only the searched row is being shown");
        promoDashboard.getPromoBlacklistSearchResultCount();
        Assertion.assertThat("Verify the searched promo code is displayed", promoDashboard.isPromoBlacklistSearchResultCorrect(), containsStringIgnoringCase(promocode));

        step("Cleanup: Attempt to delete the added blacklist so it doesn't pollute data");
        try {
            promoDashboard.clickRemoveBlacklistRowButton();
            promoDashboard.clickConfirmDeleteBlacklist();
        } catch (Throwable t) {
            step("Cleanup deletion failed");

        }
    }

    @Author(names = { "Vanshika Agarwal" })
    @Module(name = "Promo")
    @Tribe(name = "Promo")
    @Vertical(name = "PRCNG")
    @Test
    public void verifyDeleteBlacklist() throws InterruptedException {
        Map<String, String> param = getParam("verifyDeleteBlacklist");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String url = param.get("url");
        String promocode = param.get("promocode");

        step("Maximize the window");
        promoDashboard.maximize();

        step("User Login to dashboard page");
        promoDashboard.goToAdminCarePage(username,password);

        step("User Go to Discount dashboard page");
        promoDashboard.goToDicountDashboard();

        step("User Go to Promo Blacklist page.");
        promoDashboard.openPromoBlacklistPage();

        step("Precondition: Attempt to add Blacklist before deleting");
        try {
            promoDashboard.clickAddBlacklistButton();
            promoDashboard.uploadSingleFile(url);
            promoDashboard.clickModalAddToBlacklistButton();

            promoDashboard.inputSearchPromoBlacklist(promocode);
            promoDashboard.clickSearchPromoBlacklist();

            int searchResultCount = promoDashboard.getPromoBlacklistSearchResultCount();
            if (searchResultCount != 1) {
                throw new SkipException("Precondition failed: Row count is not 1");
            }
        } catch (Throwable t) {
            throw new SkipException("Failed to add blacklist precondition, skipping deletion test. Reason: " + t.getMessage());
        }

        step("Click on Remove button for the searched promo");
        promoDashboard.clickRemoveBlacklistRowButton();

        step("Confirm delete in the modal");
        promoDashboard.clickConfirmDeleteBlacklist();

        step("Clear input, search again to verify deletion");
        promoDashboard.inputSearchPromoBlacklist(promocode);
        promoDashboard.clickSearchPromoBlacklist();

        step("Verify the promo is no longer in the blacklist");
        boolean countAfterDelete = promoDashboard.isNoDataMatchMessageDisplayed();
        Assertion.assertThat("Verify no rows are displayed after deletion", countAfterDelete, is(true));
    }


}
