package com.tiket.web.dashboard.test.intools;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.common.RetryUtils;
import com.tiket.core.CoreFunctions;
import com.tiket.pojo.intools.*;
import com.tiket.web.dashboard.page.platform.sphinx.intools.b2b_affiliate.B2BResellerFinanceDashboardDetailPageWeb;
import com.tiket.web.dashboard.page.platform.sphinx.intools.b2b_affiliate.B2BResellerFinanceDashboardPageWeb;
import com.tiket.web.dashboard.page.member_dashboard.FinanceDashboardPage;
import com.tiket.web.dashboard.page.member_dashboard.intools.CreditMemoPage;
import com.tiket.dto.MonthlyReportIntoolsDTO;
import com.tiket.dto.SummaryReportListIntoolsDTO;
import com.tiket.dto.SummaryReportDetailIntoolsDTO;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.sphinx.intools.commission_dashboard.FlightInternationalPageWeb;
import com.tiket.web.dashboard.page.platform.sphinx.intools.request.ResellerRequestPageWeb;
import com.tiket.web.dashboard.page.order.orderCreationPage;


import org.hamcrest.Matchers;
import org.hamcrest.core.IsNot;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;
import org.postgresql.util.GT;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.Optional;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;

public class IntoolsTest extends BaseTest {

    private static final String API_PRICE_FORMAT_REGEX = "-?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[Ee][+-]?\\d+)?";
    private static final String API_NUMERIC_ID_FORMAT_REGEX = "\\d+";
    private static final String API_SOURCE_FORMAT_REGEX = "[a-zA-Z0-9_\\-]+";
    private static final String API_CURRENCY_FORMAT_REGEX = "[A-Z]{3}";

    @Author(names = {"Maida"})
    @Module(name = "Login")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testLoginIntoolsDashboard() {
        Map<String, String> param = getParam("testLoginIntoolsDashboard");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboardViaUI(username, password);

    }

    @Author(names = {"Maida", "Brian"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testAddTopUpFlightDomestic() throws Exception {
        Map<String, String> param = getParam("testAddTopUpFlightDomestic");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String vertical = param.get("vertical");
        String vendorName = param.get("vendorName");
        String topUpAmount = param.get("topUpAmount");
        String file = "src/test/resources/nft/image.png";
        String description = param.get("description");
        String softblockId = param.get("softblockId");
        String entityId = param.get("entityId");
        String vendorId = param.get("vendorId");
        String source = param.get("source");
        String typeOfAdjustment = param.get("typeOfAdjustment");
        String typeOfTransaction = param.get("typeOfTransaction");
        String searchDateBy = param.get("searchDateBy");
        String toleranceStr = param.get("tolerance");
        double tolerance = Double.parseDouble(toleranceStr);

        List<String> files = Arrays.asList(file.split(" "));

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Open Commission Dashboard Accordion Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("Go To Flight Domestic Menu");
        financeDashboardPage.goToFlightDomesticMenu1();

        step("Filter by Vendor ID and Search");
        financeDashboardPage.searchVendorId(vendorId);
        financeDashboardPage.clickSearchButtonVendorListingB2C();

        step("Click Top Up Form");
        financeDashboardPage.clickMoreButton();
        financeDashboardPage.clickTopUpButton();
        Assertion.assertThat("Top Up Modal is loaded", financeDashboardPage.isTopUpModalLoaded(), is(true));

        step("Input top up amount");
        financeDashboardPage.setTopUpAmountInput(topUpAmount);

        step("Input payment timestamp");
        String date = CoreFunctions.getCurrentDateYYYYMMDD();
        financeDashboardPage.setPaymentTimestampInput(date);

        step("Upload file");
        financeDashboardPage.uploadTransferProof(files);

        step("Input description");
        financeDashboardPage.setDescriptionTextarea(description);

        step("Submit the top up");
        financeDashboardPage.clickSubmitTopUpButton();
        Assertion.assertThat("Snackbar is loaded", financeDashboardPage.isTopUpSubmitted(), is(true));

        step("Go to daily report");
        financeDashboardPage.clickMoreButton();
        financeDashboardPage.clickTransactionsButton();
        Assertion.assertThat("Daily tab is loaded", financeDashboardPage.isDailyTabLoaded(), is(true));

        step("Get The Entries in Daily Report Page");
        B2CDailyDetail b2CDailyDetail = financeDashboardPage.getB2CDailyDetail(vertical, softblockId, searchDateBy);

        step("Get the Daily Data");
        B2CDailyDetail.Data b2CDailyData = b2CDailyDetail.data.get(0);

        step("Get the Daily Data Detail");
        B2CDailyDetail.Detail b2CDailyDetailData = b2CDailyData.detail.get(0);

        step("Verify Entity is Match");
        Assertion.assertThat("Entity ID is Matched", financeDashboardPage.verifyEqual(b2CDailyData.entityId, entityId), is(true));

        step("Verify Vendor ID is Match");
        Assertion.assertThat("Vendor ID is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailData.vendorId, vendorId), is(true));

        step("Verify Vendor Name is Match");
        Assertion.assertThat("Reseller Name is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailData.vendorName, vendorName), is(true));

        step("Verify Source is Match");
        Assertion.assertThat("Reseller Name is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailData.source, source), is(true));

        step("Verify Top Up Amount is Match");
        boolean istopUpAmountMatch = Math.abs(b2CDailyDetailData.topUpAmount - Double.parseDouble(topUpAmount)) < tolerance;
        Assertion.assertThat("Base Fare Order Detail 1 is Matched", istopUpAmountMatch, is(true));

        step("Verify Type Of Adjustment Match");
        Assertion.assertThat("Top Up Amount is Matched", financeDashboardPage.verifyEqual(String.valueOf(b2CDailyDetailData.typeOfAdjustment), typeOfAdjustment), is(true));

        step("Verify Type Of Transaction is Match");
        Assertion.assertThat("Top Up Amount is Matched", financeDashboardPage.verifyEqual(String.valueOf(b2CDailyDetailData.typeOfTransaction), typeOfTransaction), is(true));

        step("Verify Calculation Top Up on Addition");
        double endDepositBalanceExpected = financeDashboardPage.calculateAddition(b2CDailyDetailData.settlementType, Double.parseDouble(topUpAmount), b2CDailyDetailData.startDepositBalance);

        step("Verify End Deposit Balance on Top Up Addition");
        Assertion.assertThat("End Deposit Balance is Correctly Calculated", financeDashboardPage.verifyEqual(b2CDailyDetailData.endDepositBalance,endDepositBalanceExpected), is(true));
    }

    @Author(names = {"Maida"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testAddTrxBulkFlightInternational() {
        Map<String, String> param = getParam("testAddTrxBulkFlightInternational");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String vendorName = param.get("vendorName");
        String file = param.get("file");

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Open Commission Dashboard Accordion Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("Go To Flight International Menu");
        financeDashboardPage.goToFlightInternationalMenu();

        step("Filter by Vendor Name and Search");
        financeDashboardPage.searchVendorName(vendorName);
        financeDashboardPage.clickSearchButton();

        step("Set Vendor ID");
        financeDashboardPage.setVendorId();

        step("Set entity");
        financeDashboardPage.setEntity();

        step("Set Settlement Type");
        financeDashboardPage.setSettlementType();

        step("Set Vendor Currency");
        financeDashboardPage.setVendorCurrency();

        step("Go to daily report");
        financeDashboardPage.clickMoreButton();
        financeDashboardPage.clickTransactionsButton();
        Assertion.assertThat("Daily tab is loaded", financeDashboardPage.isDailyTabLoaded(), is(true));

        step("Click Add Transaction");
        financeDashboardPage.clickAddTransactionsButton();

        step("Click Bulk Upload Option");
        financeDashboardPage.clickBulkUploadOption();

        step("Upload the file");
        financeDashboardPage.uploadBulkFile(file);

        step("Click Add Transaction for Submit the Bulk Upload");
        financeDashboardPage.clickAddTransactionsButtonModal();
        Assertion.assertThat("Snackbar is loaded", financeDashboardPage.isBulkUploadSubmitted(), is(true));

        step("Check & Validate in Daily Report");
        financeDashboardPage.listOfTransactionFromFile(file, vendorName);
        Assertion.assertThat("List of transaction is matched", financeDashboardPage.listOfTransactionFromCFD(), is(true));

    }

    @Author(names = {"Maida"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyMonthlyReportFlightInternational() throws Exception {
        Map<String, String> param = getParam("testVerifyMonthlyReportFlightInternational");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String vendorName = param.get("vendorName");
        String month = param.get("month");
        String year = param.get("year");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String softblockId = param.get("softblockId");

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Open Commission Dashboard Accordion Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("Go To Flight International Menu");
        financeDashboardPage.goToFlightInternationalMenu();

        step("Filter by Vendor Name and Search");
        financeDashboardPage.searchVendorName(vendorName);
        financeDashboardPage.clickSearchButton();

        step("Go to Daily Report");
        financeDashboardPage.clickMoreButton();
        financeDashboardPage.clickTransactionsButton();

        step("Go to Monthly Report");
        financeDashboardPage.clickMonthlyTab();

        step("Click Dropdown Select Month");
        financeDashboardPage.clickDropdownSelectMonth();

        step("Click Month");
        financeDashboardPage.clickMonth(month);

        step("Click Dropdown Select Year");
        financeDashboardPage.clickDropdownSelectYear();

        step("Click Year");
        financeDashboardPage.clickYear(year);

        step("Click Search");
        financeDashboardPage.clickSearchButton();

        step("Get Data From Dashboard");
        MonthlyReportDetail result = financeDashboardPage.getMonthlyReportDetail(endDate, softblockId);

        step("Get Data From DB");
        MonthlyReportIntoolsDTO resultDTO = financeDashboardPage.getMonthlyDataInDB(softblockId, startDate, endDate);

        step("Verify Total Quantity Sold");
        Assertion.assertThat("Total Quantity Sold is Matched", financeDashboardPage.verifyEqual(result.data.totalQuantitySold, resultDTO.getQuantity()), is(true));

        step("Verify Revenue from Orders");
        Assertion.assertThat("Total Revenue from Orders is Matched", financeDashboardPage.verifyEqual(result.data.revenueFromOrders, resultDTO.getRevenue()), is(true));

        step("Verify Total Value Added Tax (VAT)");
        Assertion.assertThat("Total Value Added Tax (VAT) is Matched", financeDashboardPage.verifyEqual(result.data.totalVat, resultDTO.getVAT()), is(true));

        step("Verify Total Withholding Tax");
        Assertion.assertThat("Total Withholding Tax is Matched", financeDashboardPage.verifyEqual(result.data.totalWht, resultDTO.getWHT()), is(true));

        step("Verify Total Commission/ Mark up for the month");
        Assertion.assertThat("Total Commission/ Mark up for the month is Matched", financeDashboardPage.verifyEqual(result.data.totalCommissionAndMarkUp, resultDTO.getCommissionAndMarkup()), is(true));

        step("Verify Total Vendor Share");
        Assertion.assertThat("Total Vendor Share is Matched", financeDashboardPage.verifyEqual(result.data.totalVendorShare, resultDTO.getVendorShare()), is(true));

        step("Verify Direct Cost");
        Assertion.assertThat("Direct Cost is Matched", financeDashboardPage.verifyEqual(result.data.directCosts, resultDTO.getDirectCost()), is(true));

        step("Verify Profit");
        Assertion.assertThat("Profit is Matched", financeDashboardPage.verifyEqual(result.data.profit, resultDTO.getProfit()), is(true));

    }

    @Author(names = {"Maida","Yanuar"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyUploadDataReconciled() throws ExecutionException, InterruptedException, IOException {
        Map<String, String> param = getParam("testVerifyUploadDataReconciled");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String paymentMethod = param.get("paymentMethod");
        String file = param.get("file");
        String reportMonth = param.get("reportMonth");
        String reportDay = param.get("reportDay");

        step("Show CSV file to upload");
        financeDashboardPage.showUpdatedCsvFile(file);

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Go To Payment Menu and Click Bank Statement Upload Menu");
        financeDashboardPage.goToPaymentMenu();
        financeDashboardPage.goToBankStatementUplopadMenu();

        step("Choose Payment Method");
        financeDashboardPage.clickPaymentMethodDropdown();
        financeDashboardPage.choosePaymentMethod(paymentMethod);

        step("Set Payment Timestamp");
        String date = CoreFunctions.getCurrentDateYYYYMMDD();
        financeDashboardPage.setPaymentTimestampInput(date);

        step("Upload file");
        financeDashboardPage.uploadCsvFile(file);

        step("Click Upload Button");
        financeDashboardPage.clickUploadButton();
    

        step("Get all the Values From Dashboard");
        Optional<List<WebElement>> valuesLocator = financeDashboardPage.valuesLocator();
        String orderID = "";
        String referenceID = "";
        String dateTime = "";
        int amount = 0;
        if (valuesLocator.isPresent()) {
            orderID = valuesLocator.get().get(1).getText();
            referenceID = valuesLocator.get().get(2).getText();
            dateTime = valuesLocator.get().get(3).getText();
            log("datetime " + dateTime);
            amount = (int) CoreFunctions.convertPriceSafely(valuesLocator.get().stream()
                    .map(WebElement::getText)
                    .filter(text -> text.trim().matches("[\\d,.]+\\.\\d{2}"))
                    .findFirst()
                    .orElse("0"));
        }

        step("Get All Values From File");
        String[] values = financeDashboardPage.valuesBankStatementFromFile(file);
        String orderIDActual = values[0];
        String referenceIDActual = values[1];
        String dateTimeActual = values[2].replaceAll("-","/");
        log("Actual Date: " + dateTimeActual);

        int amountActual = (int) CoreFunctions.convertPriceSafely(values[16]);

        step("Verify Order ID");
        Assertion.assertThat("Order ID is Matched", financeDashboardPage.verifyEqual(orderIDActual, orderID), is(true));

        step("Verify Reference ID");
        Assertion.assertThat("Reference ID is Matched", financeDashboardPage.verifyEqual(referenceIDActual, referenceID), is(true));

        step("Amount");
        Assertion.assertThat("Amount is Matched", financeDashboardPage.verifyEqual(amountActual, amount), is(true));

        step("Click Confirm");
        financeDashboardPage.clickConfirmButton();

        step("Click Confirm Modal");
        financeDashboardPage.clickConfirmButtonModal();

        step("Verify File Uploaded");
        financeDashboardPage.clickOkButton();

        step("See the Details");
        financeDashboardPage.clickDetailsButton();

        step("Extract Details Data");
        Map<String, String> detailsValues = financeDashboardPage.getValuesDetails();

        step("Verify Upload Date");
        String uploadDate = detailsValues.get("Upload Date");
        Assertion.assertThat("Verify Upload Date is Match ", financeDashboardPage.checkDate(date, uploadDate), is(true));

        step("Verify Upload By");
        String uploadBy = detailsValues.get("Upload By");
        Assertion.assertThat("Verify Upload By is Match ", financeDashboardPage.verifyEqual(uploadBy, username), is(true));

        step("Verify Upload Status");
        String uploadStatus = detailsValues.get("Upload Status");
        Assertion.assertThat("Verify Upload Status is Match ", financeDashboardPage.verifyEqual(uploadStatus, "Success"), is(true));
        financeDashboardPage.clickCloseButtonModal();

        step("Go to Bank Payment Recon Page");
        financeDashboardPage.goToBankPaymentReconMenu();

        step("Search by Order ID");
        financeDashboardPage.setOrderIDInvoiceInput(orderIDActual);
        financeDashboardPage.clickSearchInvoiceButton();

        step("Extract Values from Invoice in Bank Payment Recon Page");
        Map<String, String> valuesInvoice = financeDashboardPage.getValuesInvoice();

        step("Verify Amount");
        int amountInvoice = (int) CoreFunctions.convertPriceSafely(valuesInvoice.get("Amount"));
        Assertion.assertThat("Verify Amount is Match", financeDashboardPage.verifyEqual(amountInvoice, amountActual), is(true));

        step("Verify Recon Status");
        String recon = valuesInvoice.get("Recon");
        Assertion.assertThat("Verify Recon is Fully Reconciled", financeDashboardPage.verifyEqual(recon, "Fully Reconciled"), is(true));

        step("Verify Order ID");
        String orderIdInvoice = valuesInvoice.get("Order ID");
        Assertion.assertThat("Verify Recon is Fully Reconciled", financeDashboardPage.verifyEqual(orderIdInvoice, orderIDActual), is(true));

        step("Verify Status");
        String statusInvoice = valuesInvoice.get("Status");
        Assertion.assertThat("Verify Status id paid", financeDashboardPage.verifyEqual(statusInvoice, "Paid"), is(true));

        step("Go to Bank Recon Reports Page");
        financeDashboardPage.goToBankReconReportsMenu();

        step("Click on order id input filed");
        financeDashboardPage.clickSearchByPaymentId();
        financeDashboardPage.setOrderIDInput(referenceIDActual);

        step("Select report date");
        financeDashboardPage.selectReconReportDate(reportMonth, reportDay);
        financeDashboardPage.clickSearchButton();

        step("Verify in Bank Recon Reports Page");
        Map<String, String> valuesReport = financeDashboardPage.getValuesReconReport();

        step("Verify Order ID");
        String orderIdReport = valuesReport.get("Order ID");
        Assertion.assertThat("Verify Order ID is Match", financeDashboardPage.verifyEqual(orderIdReport, orderIDActual), is(true));

        step("Verify Customer Price");
        int customerPriceReport = (int) CoreFunctions.convertPriceSafely(valuesReport.get("Customer Price"));
        Assertion.assertThat("Verify Customer Price is Match", financeDashboardPage.verifyEqual(customerPriceReport, amountActual), is(true));

        step("Verify Reconciliation Method");
        String reconciliationMethod = valuesReport.get("Reconciliation Method");
        Assertion.assertThat("Verify Reconciliation Method is Auto", financeDashboardPage.verifyEqual(reconciliationMethod, "auto"), is(true));

        step("Verify Reconciled By");
        String reconciledBy = valuesReport.get("Reconciled By");
        Assertion.assertThat("Verify Reconciled By system", financeDashboardPage.verifyEqual(reconciledBy, "system"), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Intools")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Sphinx")
    @Test
    public void testUploadMultipleRowDataReconciled(){
        Map<String, String> param = getParam("testUploadMultipleRowDataReconciled");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String paymentMethod = param.get("paymentMethod");
        String file = param.get("file");
        String reportMonth = param.get("reportMonth");
        String reportDay = param.get("reportDay");

        String[] orderIds = {param.get("orderIds1"), param.get("orderIds2"),  param.get("orderIds3")};
        String[] referenceIds = {param.get("referenceId1"), param.get("referenceId2"),  param.get("referenceId3")};
        String[] paymentIds = {param.get("paymentId1"), param.get("paymentId2"),  param.get("paymentId3")};

        step("Show CSV file to upload");
        financeDashboardPage.showUpdatedCsvFile(file);

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step(" When: User Choose Dropdown Value Payment Method And Select BCA Virtual Account H2H");
        financeDashboardPage.goToPaymentMenu();
        financeDashboardPage.goToBankStatementUplopadMenu();
        financeDashboardPage.clickPaymentMethodDropdown();
        financeDashboardPage.choosePaymentMethod(paymentMethod);

        step("When: User Choose Datepicker for Today");
        String date = CoreFunctions.getCurrentDateYYYYMMDD();
        financeDashboardPage.setPaymentTimestampInput(date);

        step("When: User Select File");
        financeDashboardPage.uploadCsvFile(file);

        step("When: User Click Button Upload");
        financeDashboardPage.clickUploadButton();

        step("When: Verify Reference ID, Date & Time Transaction, & Amount have correct values");
        Optional<List<WebElement>> valuesLocator = financeDashboardPage.valuesLocator();

        if (valuesLocator.isPresent()) {
            String orderID = valuesLocator.get().get(1).getText();
            String referenceID = valuesLocator.get().get(3).getText();

            Assertion.assertThat("Order ID is from test data",
                    Arrays.asList(orderIds).contains(orderID), is(true));
            Assertion.assertThat("Reference ID matches",
                    financeDashboardPage.verifyEqual(referenceID, referenceIds[0]), is(true));
        }

        step("When: User Click \"CONFIRM\"");
        financeDashboardPage.clickConfirmButton();
        step("Then: Show Pop Up Message");

        step("When: User Click \"CONFIRM\" in Pop Up Message");
        financeDashboardPage.clickConfirmButtonModal();
        step("Then: Loading Button Appear And Go to Payment Reconciliation Upload");

        step("Verify File Uploaded");
        financeDashboardPage.clickOkButton();

        step("When: User Click Details on History Upload File");
        financeDashboardPage.clickDetailsButton();
        Map<String, String> detailsValues = financeDashboardPage.getValuesDetails();

        step("Then: Pop Up Displayed with Details");
        String uploadStatus = detailsValues.get("Upload Status");
        Assertion.assertThat("Upload Status is Success", uploadStatus.equals("Success"), is(true));
        financeDashboardPage.clickCloseButtonModal();

        step("When: User Click Menu \"Bank Payment Recon\"");
        financeDashboardPage.goToBankPaymentReconMenu();

        step("When: User Input Order IDs on Invoice Section And User Click Search");
        for (String orderId : orderIds) {
            financeDashboardPage.setOrderIDInvoiceInput(orderId);
            financeDashboardPage.clickSearchInvoiceButton();

            Map<String, String> valuesInvoice = financeDashboardPage.getValuesInvoice();
            String recon = valuesInvoice.get("Recon");
            Assertion.assertThat("Recon is Fully Reconciled for order " + orderId,
                    recon.equals("Fully Reconciled"), is(true));

            String statusInvoice = valuesInvoice.get("Status");
            Assertion.assertThat("Status is Paid for order " + orderId,
                    statusInvoice.equals("Paid"), is(true));                
        }

        step("When: User Click Menu \"Bank Recon Reports\"");
        financeDashboardPage.goToBankReconReportsMenu();

        step("When: User Input Order IDs, Start Date, End Date, Currency");
       
        financeDashboardPage.selectReconReportDate(reportMonth, reportDay);
        financeDashboardPage.clickSearchByPaymentId();
        financeDashboardPage.orderIdClickReconReport();
        for (String orderId : paymentIds) {
        
            financeDashboardPage.setOrderIDInputForReconReport(orderId);
            financeDashboardPage.clickSearchButton();

            step("Then: Display Order IDs with correct field values");
            Map<String, String> valuesReport = financeDashboardPage.getValuesReconReport();
            String paymentMethodReport = valuesReport.get("Payment Method");
            Assertion.assertThat("Payment Method is BCA Virtual Account",
            paymentMethodReport.contains("BCA Virtual Account"), is(true));
        }

        step("When: User Click Menu \"Export Report\" and download the CSV File");
        financeDashboardPage.exportReport();

        step("When: User Click Menu \"Bank Statement Upload\" And User Click CTA Action → Revoked");
        financeDashboardPage.goToBankStatementUplopadMenu();

        step("Then: All Order IDs will Revoked / Back to Non Reconciled");
        financeDashboardPage.reconcile();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Intools")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Sphinx")
    @Test
    public void testStripePartialReconcileUpload() throws ExecutionException, InterruptedException, IOException {
        Map<String, String> param = getParam("testStripePartialReconcileUpload");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String paymentMethod = param.get("paymentMethod");
        String file = param.get("file");

        String orderId = param.get("orderId");
        String currency = param.get("currency");
        String settlementAmount = param.get("settlementAmount");

        step("Show CSV file to upload");
        financeDashboardPage.showUpdatedCsvFile(file);

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("When: User Choose Dropdown Value Payment Method And Select Stripe Credit Card");
        financeDashboardPage.goToPaymentMenu();
        financeDashboardPage.goToBankStatementUplopadMenu();
        financeDashboardPage.clickPaymentMethodDropdown();
        financeDashboardPage.choosePaymentMethod(paymentMethod);

        step("When: User Choose Datepicker for Today");
        String date = CoreFunctions.getCurrentDateYYYYMMDD();
        financeDashboardPage.setPaymentTimestampInput(date);

        step("When: User Select File");
        financeDashboardPage.uploadCsvFile(file);

        step("When: User Click Button Upload");
        financeDashboardPage.clickUploadButton();

        step("When: Verify Order ID");
        Optional<List<WebElement>> valuesLocator = financeDashboardPage.valuesLocator();
        if (valuesLocator.isPresent()) {
            String orderID = valuesLocator.get().get(1).getText();
            Assertion.assertThat("Order ID matches test data", orderID.equals(orderId), is(true));
        }

        step("When: User Click \"CONFIRM\"");
        financeDashboardPage.clickConfirmButton();

        step("When: User Click \"CONFIRM\" in Pop Up Message");
        financeDashboardPage.clickConfirmButtonModal();

        step("Verify File Uploaded");
        financeDashboardPage.clickOkButton();

        step("When: User Click Details on History Upload File");
        financeDashboardPage.clickDetailsButton();
        Map<String, String> detailsValues = financeDashboardPage.getValuesDetails();

        step("Then: Pop Up Displayed with Details");
        String uploadStatus = detailsValues.get("Upload Status");
        Assertion.assertThat("Upload Status is Success", uploadStatus.equals("Success"), is(true));

        String totalUploadedData = detailsValues.get("Total Uploaded Data");
        Assertion.assertThat("Total Uploaded Data is 1", totalUploadedData.equals("1"), is(true));

        financeDashboardPage.clickCloseButtonModal();

        step("When: User Click Menu \"Bank Payment Recon\"");
        financeDashboardPage.goToBankPaymentReconMenu();

        step("When: User Input Order ID: " + orderId + " on Settlement Section And User Click Search");
        financeDashboardPage.setOrderIdSettlementInput(orderId);
        financeDashboardPage.searchSettlementButton();

        log("Settlement search for order ID: " + orderId + " - should show amount: " + settlementAmount + " " + currency);
        step("When: User Input Order ID: " + orderId + " on Invoice Section And User Click Search");
        financeDashboardPage.setOrderIDInvoiceInput(orderId);
        financeDashboardPage.clickSearchInvoiceButton();

        step("Then: Display Order ID with status Partial Reconciled");
        Map<String, String> finalValuesInvoice = financeDashboardPage.getValuesInvoice();
        String finalRecon = finalValuesInvoice.get("Recon");

        Assertion.assertThat("Recon status is Partial Reconciled",
                finalRecon.contains("Partial"), is(true));

        step("When: User converts into partial reconcile scenario");
        financeDashboardPage.selectCheckBox();
        financeDashboardPage.clickConfirmButton();
        financeDashboardPage.confirm();

        Map<String, String> valuesInvoice = financeDashboardPage.getValuesInvoice();
        String statusInvoice = valuesInvoice.get("Status");
        finalValuesInvoice = financeDashboardPage.getValuesInvoice();
        finalRecon = finalValuesInvoice.get("Recon");
        Assertion.assertThat("Status is Paid", statusInvoice.equals("Paid"), is(true));
        Assertion.assertThat("Recon status is Fully Reconciled",
                finalRecon.contains("Fully"), is(true));

        step("When: User Click Menu \"Bank Recon Reports\"");
        financeDashboardPage.goToBankReconReportsMenu();

        step("When: User Input Order ID, Start Date, End Date, Currency for reports");
        financeDashboardPage.setOrderIDInput(orderId);
        financeDashboardPage.selectUSD();
        financeDashboardPage.clickSearchButton();

        step("Then: Display Order ID with correct report fields");
        Map<String, String> valuesReport = financeDashboardPage.getValuesReconReport();
        String orderIdReport = valuesReport.get("Order ID");
        Assertion.assertThat("Order ID matches in report", orderIdReport.equals(orderId), is(true));

        step("When: User Click Menu \"Export Report\"");
        financeDashboardPage.exportReport();

        step("When: User Click Menu \"Bank Statement Upload\" And Click Revoke");
        financeDashboardPage.goToBankStatementUplopadMenu();
        financeDashboardPage.reconcile();
    }

    @Author(names = {"Maida", "Fandy", "Brian"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testAddTopUpDepositB2BReseller() throws Exception {
        Map<String, String> param = getParam("testAddTopUpDepositB2BReseller");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String resellerName = param.get("resellerName");
        String bankName = param.get("bankName");
        String topUpAmount = param.get("topUpAmount");
        String description = param.get("description");
        String remarks = param.get("remarks");
        String file = "src/test/resources/dweb/intools/topUp.jpg";
        String searchDateBy = param.get("searchDateBy");

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("CLick B2B Affiliate Dropdown Menu");
        financeDashboardPage.clickB2BAffiliateDropdownMenu();

        step("Go to Reseller Menu");
        financeDashboardPage.goToB2BAResellerMenu();

        step("Filter by Reseller Name and Search");
        financeDashboardPage.searchResellerName(resellerName);
        financeDashboardPage.clickSearchButton();

        step("Get Reseller Detail");
        B2BResellerDetail b2BResellerDetail = financeDashboardPage.getB2BResellerDetail(resellerName);

        step("Click Action More");
        financeDashboardPage.clickMoreButton();

        step("Click Top-Up Button");
        financeDashboardPage.clickTopUpButton();

        step("Fill all the field to do add top-up deposit");
        step("Input Bank Name");
        financeDashboardPage.setBankNameInput(bankName);

        step("Input Transaction Id");
        String trxId = financeDashboardPage.randomOrderIDGenerator();
        financeDashboardPage.setTransactionIdInput(trxId);

        step("Input Top Up Amount");
        financeDashboardPage.setTopUpAmountInput(topUpAmount);

        step("Input Proof of Transfer");
        financeDashboardPage.uploadTopUpImage(file);

        step("Input Description");
        financeDashboardPage.setDescriptionTextarea(description);

        step("Submit The Top Up");
        financeDashboardPage.clickSubmitTopUpButtonB2B();
        Assertion.assertThat("Snackbar is loaded", financeDashboardPage.isTopUpSubmitted(), is(true));

        step("Approve The Top Up in Reseller Request Page");
        step("Go To Reseller Request Page");
        financeDashboardPage.clickRequestDropdownMenu();

        step("Click Reseller Menu");
        financeDashboardPage.clickResellerMenu();

        step("Filter by Reseller Name and Search");
        financeDashboardPage.searchResellerName(resellerName);
        financeDashboardPage.clickSearchButton();

        step("Approve The Top Up");
        financeDashboardPage.clickApproveButton();

        step("Fill Remarks Input");
        financeDashboardPage.setRemarkInput(remarks);

        step("Upload Proof");
        financeDashboardPage.uploadTopUpImage(file);

        step("Click Approve Button");
        financeDashboardPage.clickApproveButtonModal();

        step("Go to Request History Tab to Check The Status");
        financeDashboardPage.clickRequestHistoryTab();

        step("Get The Entry of The Request History");
        Map<String, Object> entries = financeDashboardPage.getEntriesRequestHistory();

        step("Check Approved By");
        String approvedBy = (String) entries.get("Approved By");
        Assertion.assertThat("Approved By is Matched", financeDashboardPage.verifyEqual(approvedBy, username), is(true));

        step("Check Remarks");
        String remark = (String) entries.get("Remarks");
        Assertion.assertThat("Remarks is Matched", financeDashboardPage.verifyEqual(remark, remarks), is(true));

        step("Check The Status of The Trx");
        String status = (String) entries.get("Status");
        Assertion.assertThat("Status of The Trx is Approved", financeDashboardPage.verifyEqual(status, "Approved"), is(true));

        step("Go to Daily Page For Validate");
        financeDashboardPage.goToB2BDailyReport(b2BResellerDetail.data.get(0).creditLine.resellerId);

        step("Get The Entries in Daily Report Page");
        B2BDailyDetail b2BDailyDetail = financeDashboardPage.getB2BDailyDetail(resellerName, searchDateBy);

        step("Get the Daily Detail Data");
        B2BDailyDetail.Data b2bDailyDetailData = b2BDailyDetail.data.get(0);

        step("Check Vertical Name is Match");
        Assertion.assertThat("Vertical is Matched", financeDashboardPage.verifyEqual(b2bDailyDetailData.vertical, b2BDailyDetail.data.get(0).vertical), is(true));

        step("Check Reseller Name is Match");
        Assertion.assertThat("Reseller Name is Matched", financeDashboardPage.verifyEqual(b2bDailyDetailData.resellerName, b2BResellerDetail.data.get(0).resellerName), is(true));

        step("Check Top Up Amount is Match");
        Assertion.assertThat("Top Up Amount is Matched", financeDashboardPage.verifyEqual(String.valueOf(b2bDailyDetailData.topUpAmount), topUpAmount), is(true));

        step("Check Calculation is true");
        Assertion.assertThat("Calculation matched", financeDashboardPage.calculateBySettlementType(b2bDailyDetailData.agreementType, "Addition", Integer.parseInt(topUpAmount), b2bDailyDetailData.details.get(0).startDepositBalance,
                b2bDailyDetailData.details.get(0).endDepositBalance, b2bDailyDetailData.details.get(0).startCreditLineBalance, b2bDailyDetailData.details.get(0).endCreditLineBalance, b2bDailyDetailData.details.get(0).creditLineUtil), is(true));
    }

    @Author(names = {"Brian"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testDownloadTemplateRecon() throws ExecutionException, InterruptedException, IOException {
        Map<String, String> param = getParam("testDownloadTemplateRecon");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String fileNameTemplateRecon = param.get("fileNameTemplateRecon");
        String timeOutMiliSec = param.get("timeOutMiliSec");

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Go To Payment Menu and Click Bank Statement Upload Menu");
        financeDashboardPage.goToPaymentMenu();
        financeDashboardPage.goToBankStatementUplopadMenu();

        step("Click Download Template Button");
        financeDashboardPage.clickDownloadTemplateButton();

        step("Check Downloaded File");
        Assertion.assertThat("Verify downloaded file", financeDashboardPage.verifyDownloadedExportCSV(fileNameTemplateRecon,timeOutMiliSec), is(true));

    }

    @Author(names = {"Brian"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testVerifySummaryReportFlightInternational() throws Exception {
        Map<String, String> param = getParam("testVerifySummaryReportFlightInternational");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String year = param.get("year");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String flightType = param.get("flightType");
        String months = param.get("months");
        String month = param.get("month");
        String entity2 = param.get("entity2");
        String vendorCurrency = param.get("vendorCurrency");
        String dateMonth = param.get("dateMonth");
        String page = param.get("page");
        String limit = param.get("limit");
        String entityList = param.get("entityList");
        String currencyList = param.get("currencyList");
        String totalNumberOfVendors = param.get("totalNumberOfVendors");
        String totalRevenue = param.get("totalRevenue");
        String numberOfOrders = param.get("numberOfOrders");
        String totalMargin = param.get("totalMargin");
        String basePrice = param.get("basePrice");
        String tiketPointValue = param.get("tiketPointValue");
        String giftVoucher = param.get("giftVoucher");
        String promoValue = param.get("promoValue");
        String platformFee = param.get("platformFee");
        String directCost = param.get("directCost");
        String profit = param.get("profit");
        String vendorName = param.get("vendorName");
        String currencyDetail = param.get("currencyDetail");
        String totalQuantitySold = param.get("totalQuantitySold");
        String revenueFromOrders = param.get("revenueFromOrders");
        String additionalMargin = param.get("additionalMargin");
        String additionalCommission = param.get("additionalCommission");
        String vat = param.get("vat");
        String wht = param.get("wht");
        String topupVendor = param.get("topupVendor");
        String totalCommission = param.get("totalCommission");
        String totalVendorShare = param.get("totalVendorShare");
        String directCostDetail = param.get("directCostDetail");
        String profitDetail = param.get("profitDetail");
        String settlementType = param.get("settlementType");
        String startDepositBalance = param.get("startDepositBalance");
        String endDepositBalance = param.get("endDepositBalance");
        String fileNameSummaryDetail = param.get("fileNameSummaryDetail");
        String timeOutMiliSec = param.get("timeOutMiliSec");

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Open Commission Dashboard Accordion Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("Go To Flight International Menu");
        financeDashboardPage.goToFlightInternationalMenu();

        step("Go to Summary Report List Page");
        financeDashboardPage.clickSummaryReportListTab();

        step("Click Dropdown Select Month");
        financeDashboardPage.clickDropdownSelectMonth();

        step("Click Month");
        financeDashboardPage.clickMonth(month);

        step("Click Dropdown Select Year");
        financeDashboardPage.clickDropdownSelectYear();

        step("Click Year");
        financeDashboardPage.clickYear(year);

        step("Click Search");
        financeDashboardPage.clickSearchButton();

        step("Get Data Summary Report List From API");
        SummaryReportList result = financeDashboardPage.getSummaryReportList(flightType, year, months);

        step("Get Data Summary Report List From DB");
        SummaryReportListIntoolsDTO resultDTO = financeDashboardPage.getSummaryReportListDataInDB(flightType, startDate, endDate);

        step("Get The Entry of The Summary Report List");
        Map<String, Object> entryValuesList = financeDashboardPage.getEntriesSummaryReportList();

        step("Verify Entity in Summary Report List Between Test Data & Dashboard");
        String entityL = (String) entryValuesList.get("Entity");
        Assertion.assertThat("Entity is Matched", financeDashboardPage.verifyEqualString(entityL, entityList), is(true));

        step("Verify Entity in Summary Report List Between API & DB");
        Assertion.assertThat("Entity is Matched", financeDashboardPage.verifyEqualString(result.data.vertical_monthly_summary.get(0).entity, resultDTO.getEntity_id()), is(true));

        step("Verify Currency in Summary Report List Between Test Data & Dashboard");
        String currencyL = (String) entryValuesList.get("Currency");
        Assertion.assertThat("Currency is Matched", financeDashboardPage.verifyEqualString(currencyL, currencyList), is(true));

        step("Verify Currency in Summary Report List Between API & DB");
        Assertion.assertThat("Currency is Matched", financeDashboardPage.verifyEqualString(result.data.vertical_monthly_summary.get(0).currency, resultDTO.getCurrency()), is(true));

        step("Verify Total Number of Vendors in Summary Report List Between Test Data & Dashboard");
        int totalVendors= (int) CoreFunctions.convertPriceSafely((String) entryValuesList.get("Total Number Of Vendors"));
        int amountTotalVendors = Integer.parseInt(totalNumberOfVendors);
        Assertion.assertThat("Total Number of Vendors is Matched", financeDashboardPage.verifyEqual(totalVendors, amountTotalVendors), is(true));

        step("Verify Total Number of Vendors in Summary Report List Between API & DB");
        Assertion.assertThat("Total Number of Vendors is Matched", financeDashboardPage.verifyEqualLong(result.data.vertical_monthly_summary.get(0).total_vendor, resultDTO.getTotal_Number_of_Vendors()), is(true));

        step("Verify Total Revenue in Summary Report List Between Test Data & Dashboard");
        double totalR= CoreFunctions.convertPriceSafely((String) entryValuesList.get("Total Revenue"));
        double amountTotalR = Double.parseDouble(totalRevenue);
        Assertion.assertThat("Total Revenue is Matched", financeDashboardPage.verifyEqual(totalR, amountTotalR), is(true));

        step("Verify Total Revenue in Summary Report List Between API & DB");
        Assertion.assertThat("Total Revenue is Matched", financeDashboardPage.verifyEqual(result.data.vertical_monthly_summary.get(0).total_revenue, resultDTO.getTotal_Revenue()), is(true));

        step("Verify Total Number of Orders in Summary Report List Between Test Data & Dashboard");
        String numberOrders = (String) entryValuesList.get("Number of Orders");
        Assertion.assertThat("Total Number of Orders is Matched", financeDashboardPage.verifyEqual(numberOrders, numberOfOrders), is(true));

        step("Verify Total Number of Orders in Summary Report List Between API & DB");
        Assertion.assertThat("Total Number of Orders is Matched", financeDashboardPage.verifyEqual(result.data.vertical_monthly_summary.get(0).total_quantity_sold, resultDTO.getNumber_of_Orders()), is(true));

        step("Verify Total Margin in Summary Report List Between Test Data & Dashboard");
        double totalM= CoreFunctions.convertPriceSafely((String) entryValuesList.get("Total Margin"));
        double amountTotalM = Double.parseDouble(totalMargin);
        Assertion.assertThat("Total Margin is Matched", financeDashboardPage.verifyEqual(totalM, amountTotalM), is(true));

        step("Verify Total Margin in Summary Report List Between API & DB");
        Assertion.assertThat("Total Margin is Matched", financeDashboardPage.verifyEqual(result.data.vertical_monthly_summary.get(0).total_margin, resultDTO.getTotal_Margin()), is(true));

        step("Verify Base Price in Summary Report List Between Test Data & Dashboard");
        double baseP= CoreFunctions.convertPriceSafely((String) entryValuesList.get("Base Price"));
        double amountBaseP = Double.parseDouble(basePrice);
        Assertion.assertThat("Base Price is Matched", financeDashboardPage.verifyEqual(baseP, amountBaseP), is(true));

        step("Verify Base Price in Summary Report List Between API & DB");
        Assertion.assertThat("Base Price is Matched", financeDashboardPage.verifyEqual(result.data.vertical_monthly_summary.get(0).total_base_price, resultDTO.getBase_Price()), is(true));

        step("Verify Tiket Point Value in Summary Report List Between Test Data & Dashboard");
        double tiketPoint= CoreFunctions.convertPriceSafely((String) entryValuesList.get("Tiket Point Value"));
        double amountTiketPoint = Double.parseDouble(tiketPointValue);
        Assertion.assertThat("Tiket Point Value is Matched", financeDashboardPage.verifyEqual(tiketPoint, amountTiketPoint), is(true));

        step("Verify Tiket Point Value in Summary Report List Between API & DB");
        Assertion.assertThat("Tiket Point Value is Matched", financeDashboardPage.verifyEqual(result.data.vertical_monthly_summary.get(0).total_tiket_point_value, resultDTO.getTiket_Point_Value()), is(true));

        step("Verify Gift Voucher in Summary Report List Between Test Data & Dashboard");
        double giftV= CoreFunctions.convertPriceSafely((String) entryValuesList.get("Gift Voucher"));
        double amountGiftV = Double.parseDouble(giftVoucher);
        Assertion.assertThat("Gift Voucher is Matched", financeDashboardPage.verifyEqual(giftV, amountGiftV), is(true));

        step("Verify Gift Voucher in Summary Report List Between API & DB");
        Assertion.assertThat("Gift Voucher is Matched", financeDashboardPage.verifyEqual(result.data.vertical_monthly_summary.get(0).total_gift_voucher, resultDTO.getGift_Voucher()), is(true));

        step("Verify Promo Value in Summary Report List Between Test Data & Dashboard");
        double promoV= CoreFunctions.convertPriceSafely((String) entryValuesList.get("Promo Value"));
        double amountPromoV = Double.parseDouble(promoValue);
        Assertion.assertThat("Promo Value is Matched", financeDashboardPage.verifyEqual(promoV, amountPromoV), is(true));

        step("Verify Promo Value in Summary Report List Between API & DB");
        Assertion.assertThat("Promo Value is Matched", financeDashboardPage.verifyEqual(result.data.vertical_monthly_summary.get(0).total_promo_code, resultDTO.getPromo_Value()), is(true));

        step("Verify Platform Fee in Summary Report List Between Test Data & Dashboard");
        double platformF= CoreFunctions.convertPriceSafely((String) entryValuesList.get("Platform Fee"));
        double amountPlatformF = Double.parseDouble(platformFee);
        Assertion.assertThat("Platform Fee is Matched", financeDashboardPage.verifyEqual(platformF, amountPlatformF), is(true));

        step("Verify Platform Fee in Summary Report List Between API & DB");
        Assertion.assertThat("Platform Fee is Matched", financeDashboardPage.verifyEqual(result.data.vertical_monthly_summary.get(0).total_platform_fee, resultDTO.getPlatform_Fee()), is(true));

        step("Verify Direct Cost in Summary Report List Between Test Data & Dashboard");
        double directC= CoreFunctions.convertPriceSafely((String) entryValuesList.get("Direct Cost"));
        double amountDirectC= Double.parseDouble(directCost);
        Assertion.assertThat("Direct Cost is Matched", financeDashboardPage.verifyEqual(directC, amountDirectC), is(true));

        step("Verify Direct Costs in Summary Report List Between API & DB");
        Assertion.assertThat("Direct Costs is Matched", financeDashboardPage.verifyEqual(result.data.vertical_monthly_summary.get(0).total_direct_cost, resultDTO.getDirect_Costs()), is(true));

        step("Verify Profit in Summary Report List Between Test Data & Dashboard");
        double profitL= CoreFunctions.convertPriceSafely((String) entryValuesList.get("Profit"));
        double amountProfitL= Double.parseDouble(profit);
        Assertion.assertThat("Profit is Matched", financeDashboardPage.verifyEqual(profitL, amountProfitL), is(true));

        step("Verify Profit in Summary Report List Between API & DB");
        Assertion.assertThat("Profit is Matched", financeDashboardPage.verifyEqual(result.data.vertical_monthly_summary.get(0).total_profit, resultDTO.getProfit()), is(true));

        step("Go to Summary Report Detail Page");
        financeDashboardPage.clickSummaryReportDetail();

        step("Get Data Summary Report Detail From API");
        SummaryReportDetail result2 = financeDashboardPage.getSummaryReportDetail(flightType, entity2, vendorCurrency, year, months, page, limit);

        step("Get Data Summary Report Detail From DB");
        SummaryReportDetailIntoolsDTO resultDTO2 = financeDashboardPage.getSummaryReportDetailDataInDB(flightType, startDate, endDate, entity2, vendorCurrency, dateMonth);

        step("Get The Entry of The Summary Report Detail");
        Map<String, Object> entryValuesDetail = financeDashboardPage.getEntriesSummaryReportDetail();

        step("Verify Vendor Name in Summary Report Detail Between Test Data & Dashboard");
        String vendorN = (String) entryValuesDetail.get("Vendor Name");
        Assertion.assertThat("Vendor Name is Matched", financeDashboardPage.verifyEqual(vendorN, vendorName), is(true));

        step("Verify Vendor Name in Summary Report Detail Between API & DB");
        Assertion.assertThat("Vendor Name is Matched", financeDashboardPage.verifyEqualString(result2.data.get(2).vendorName, resultDTO2.getVendor_Name()), is(true));

        step("Verify Currency in Summary Report Detail Between Test Data & Dashboard");
        String currencyD = (String) entryValuesDetail.get("Currency");
        Assertion.assertThat("Currency is Matched", financeDashboardPage.verifyEqual(currencyD, currencyDetail), is(true));

        step("Verify Currency in Summary Report Detail Between API & DB");
        Assertion.assertThat("Currency is Matched", financeDashboardPage.verifyEqualString(result2.data.get(2).currency, resultDTO2.getCurrency()), is(true));

        step("Verify Total Quantity Sold in Summary Report Detail Between Test Data & Dashboard");
        String totalQS= (String) entryValuesDetail.get("Total Quantity Sold");
        Assertion.assertThat("Total Quantity Sold is Matched", financeDashboardPage.verifyEqual(totalQS, totalQuantitySold), is(true));

        step("Verify Total Quantity Sold in Summary Report Detail Between API & DB");
        Assertion.assertThat("Total Quantity Sold is Matched", financeDashboardPage.verifyEqualString(result2.data.get(2).totalQuantitySold, resultDTO2.getTotal_Quantity_Sold()), is(true));

        step("Verify Revenue from Orders in Summary Report Detail Between Test Data & Dashboard");
        double revenueOrders= CoreFunctions.convertPriceSafely((String) entryValuesDetail.get("Revenue From Orders"));
        double amountRevenueOrders= Double.parseDouble(revenueFromOrders);
        Assertion.assertThat("Revenue from Orders is Matched", financeDashboardPage.verifyEqual(revenueOrders, amountRevenueOrders), is(true));

        step("Verify Revenue from Orders in Summary Report Detail Between API & DB");
        Assertion.assertThat("Revenue from Orders is Matched", financeDashboardPage.verifyEqual(result2.data.get(2).revenueFromOrders, resultDTO2.getRevenue_From_Orders()), is(true));

        step("Verify Additional Margin in Summary Report Detail Between Test Data & Dashboard");
        int additionalM= (int) CoreFunctions.convertPriceSafely((String) entryValuesDetail.get("Additional Margin"));
        int amountAdditionalM = Integer.parseInt(additionalMargin);
        Assertion.assertThat("Additional Margin is Matched", financeDashboardPage.verifyEqual(additionalM, amountAdditionalM), is(true));

        step("Verify Additional Margin in Summary Report Detail Between API & DB");
        Assertion.assertThat("Additional Margin is Matched", financeDashboardPage.verifyEqual(result2.data.get(2).additionalMargin, resultDTO2.getAdditional_Margin()), is(true));

        step("Verify Additional Commission in Summary Report List Between Test Data & Dashboard");
        double additionalC= CoreFunctions.convertPriceSafely((String) entryValuesDetail.get("Additional Commission"));
        double amountAdditionalC= Double.parseDouble(additionalCommission);
        Assertion.assertThat("Additional Commission is Matched", financeDashboardPage.verifyEqual(additionalC, amountAdditionalC), is(true));

        step("Verify Additional Commission in Summary Report Detail Between API & DB");
        Assertion.assertThat("Additional Commission is Matched", financeDashboardPage.verifyEqual(result2.data.get(2).additionalCommission, resultDTO2.getAdditional_Commission()), is(true));

        step("Verify Total Value Added Tax (VAT) in Summary Report List Between Test Data & Dashboard");
        double vatD= CoreFunctions.convertPriceSafely((String) entryValuesDetail.get("Total Value Added Tax (VAT)"));
        double amountVatD= Double.parseDouble(vat);
        Assertion.assertThat("Total Value Added Tax (VAT) is Matched", financeDashboardPage.verifyEqual(vatD, amountVatD), is(true));

        step("Verify Total Value Added Tax (VAT) in Summary Report Detail Between API & DB");
        Assertion.assertThat("Total Value Added Tax (VAT) is Matched", financeDashboardPage.verifyEqual(result2.data.get(2).totalVAT, resultDTO2.getVAT()), is(true));

        step("Verify Total Withholding Tax in Summary Report List Between Test Data & Dashboard");
        double whtD= CoreFunctions.convertPriceSafely((String) entryValuesDetail.get("Total Withholding Tax"));
        double amountWhtD= Double.parseDouble(wht);
        Assertion.assertThat("Total Withholding Tax (VAT) is Matched", financeDashboardPage.verifyEqual(whtD, amountWhtD), is(true));

        step("Verify Total Withholding Tax in Summary Report Detail Between API & DB");
        Assertion.assertThat("Total Withholding Tax is Matched", financeDashboardPage.verifyEqual(result2.data.get(2).totalWHT, resultDTO2.getWHT()), is(true));

        step("Verify Top up Amount from Vendor  in Summary Report List Between Test Data & Dashboard");
        double topUpV= CoreFunctions.convertPriceSafely((String) entryValuesDetail.get("Top Up Vendor"));
        double amountTopUpV= Double.parseDouble(topupVendor);
        Assertion.assertThat("Top up Amount from Vendor  is Matched", financeDashboardPage.verifyEqual(topUpV, amountTopUpV), is(true));

        step("Verify Top up Amount from Vendor in Summary Report Detail Between API & DB");
        Assertion.assertThat("Top up Amount from Vendor is Matched", financeDashboardPage.verifyEqual(result2.data.get(2).topUpAmountFromVendor, resultDTO2.getTop_Up_Amount_From_Vendor()), is(true));

        step("Verify Total Commission Earned for the Month in Summary Report List Between Test Data & Dashboard");
        double totalC= CoreFunctions.convertPriceSafely((String) entryValuesDetail.get("Total Commission"));
        double amountTotalC= Double.parseDouble(totalCommission);
        Assertion.assertThat("Total Commission Earned for the Month is Matched", financeDashboardPage.verifyEqual(totalC, amountTotalC), is(true));

        step("Verify Total Commission Earned for the Month in Summary Report Detail Between API & DB");
        Assertion.assertThat("Total Commission Earned for the Month is Matched", financeDashboardPage.verifyEqual(result2.data.get(2).totalCommissionEarned, resultDTO2.getTotal_Commission_Earned_For_The_Month()), is(true));

        step("Verify Total Vendor Share in Summary Report List Between Test Data & Dashboard");
        double totalVS= CoreFunctions.convertPriceSafely((String) entryValuesDetail.get("Total Vendor Share"));
        double amountTotalVS= Double.parseDouble(totalVendorShare);
        Assertion.assertThat("Total Vendor Share is Matched", financeDashboardPage.verifyEqual(totalVS, amountTotalVS), is(true));

        step("Verify Total Vendor Share in Summary Report Detail Between API & DB");
        Assertion.assertThat("Total Vendor Share is Matched", financeDashboardPage.verifyEqual(result2.data.get(2).totalVendorShare, resultDTO2.getVendor_Share()), is(true));

        step("Verify Direct Cost in Summary Report List Between Test Data & Dashboard");
        double directCostD= CoreFunctions.convertPriceSafely((String) entryValuesDetail.get("Direct Cost"));
        double amountDirectCostD= Double.parseDouble(directCostDetail);
        Assertion.assertThat("Direct Cost is Matched", financeDashboardPage.verifyEqual(directCostD, amountDirectCostD), is(true));

        step("Verify Direct Costs in Summary Report Detail Between API & DB");
        Assertion.assertThat("Direct Costs is Matched", financeDashboardPage.verifyEqual(result2.data.get(2).directCosts, resultDTO2.getDirect_Costs()), is(true));

        step("Verify Profit in Summary Report List Between Test Data & Dashboard");
        double profitD= CoreFunctions.convertPriceSafely((String) entryValuesDetail.get("Profit"));
        double amountProfitD= Double.parseDouble(profitDetail);
        Assertion.assertThat("Profit is Matched", financeDashboardPage.verifyEqual(profitD, amountProfitD), is(true));

        step("Verify Profit in Summary Report Detail Between API & DB");
        Assertion.assertThat("Profit is Matched", financeDashboardPage.verifyEqual(result2.data.get(2).profit, resultDTO2.getProfit()), is(true));

        step("Verify Settlement Type in Summary Report Detail Between Test Data & Dashboard");
        String settlementT = (String) entryValuesDetail.get("Settlement Type");
        Assertion.assertThat("Settlement Type is Matched", financeDashboardPage.verifyEqual(settlementT, settlementType), is(true));

        step("Verify Settlement Type in Summary Report Detail Between API & DB");
        Assertion.assertThat("Settlement Type is Matched", financeDashboardPage.verifyEqual(result2.data.get(2).settlementType, resultDTO2.getSettlement_Type()), is(true));

        step("Verify Start Deposit Balance  in Summary Report List Between Test Data & Dashboard");
        double startDB= CoreFunctions.convertPriceSafely((String) entryValuesDetail.get("Start Deposit Balance"));
        double amountStartDB= Double.parseDouble(startDepositBalance);
        Assertion.assertThat("Start Deposit Balance  is Matched", financeDashboardPage.verifyEqual(amountStartDB, startDB), is(true));

        step("Verify Start Deposit Balance in Summary Report Detail Between API & DB");
        Assertion.assertThat("Start Deposit Balance is Matched", financeDashboardPage.verifyEqual(result2.data.get(2).startDepositBalance, resultDTO2.getStart_Deposit_Balance()), is(true));

        step("Verify End Deposit Balance  in Summary Report List Between Test Data & Dashboard");
        double endDB= CoreFunctions.convertPriceSafely((String) entryValuesDetail.get("End Deposit Balance"));
        double amountEndDB= Double.parseDouble(endDepositBalance);
        Assertion.assertThat("End Deposit Balance  is Matched", financeDashboardPage.verifyEqual(endDB, amountEndDB), is(true));

        step("Verify End Deposit Balance in Summary Report Detail Between API & DB");
        Assertion.assertThat("End Deposit Balance is Matched", financeDashboardPage.verifyEqual(result2.data.get(2).endDepositBalance, resultDTO2.getEnd_Deposit_Balance()), is(true));

        step("Click Download CSV in Summary Detail Page");
        financeDashboardPage.clickDownloadCSVinSummaryDetail();

        step("Check Downloaded File");
        Assertion.assertThat("Verify downloaded file", financeDashboardPage.verifyDownloadedSummaryDetailCSV(fileNameSummaryDetail,timeOutMiliSec), is(true));

        step("Verify Snackbar is Appeared when Success Downloading CSV File on Summary Report Detail");
        Assertion.assertThat("Snackbar is Appeared", financeDashboardPage.isSummaryDetailDownloaded(), is(true));

    }

    @Author(names = {"Brian"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testB2CMultiOrderFlightInternational() throws Exception {
        Map<String, String> param = getParam("testB2CMultiOrderFlightInternational");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String vertical = param.get("vertical");
        String kafkaBrokers = param.get("kafkaBrokers");
        String kafkaTopic = param.get("kafkaTopic");
        String kafkaPort = param.get("kafkaPort");
        String waitTimeInMillis = param.get("waitTimeInMillis");
        String searchDateBy = param.get("searchDateBy");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String verticalTitle = param.get("verticalTitle");
        String softblockId = param.get("softblockId");
        String orderId = param.get("orderId");
        String vendorId1 = param.get("vendorId1");
        String orderDetailId1 = param.get("orderDetailId1");
        String source1 = param.get("source1");
        String baseFare1 = param.get("baseFare1");
        String basicPrice1 = param.get("basicPrice1");
        String basicPriceInCustomerCurrency1 = param.get("basicPriceInCustomerCurrency1");
        String basicPriceInReportCurrency1 = param.get("basicPriceInReportCurrency1");
        String otherFees1 = param.get("otherFees1");
        String basicPriceTicket1 = param.get("basicPriceTicket1");
        String basicPriceBaggage1 = param.get("basicPriceBaggage1");
        String basicPriceMeals1 = param.get("basicPriceMeals1");
        String basicPriceSeat1 = param.get("basicPriceSeat1");
        String basicPriceTicketInCustomerCurrency1 = param.get("basicPriceTicketInCustomerCurrency1");
        String basicPriceBaggageInCustomerCurrency1 = param.get("basicPriceBaggageInCustomerCurrency1");
        String basicPriceMealsInCustomerCurrency1 = param.get("basicPriceMealsInCustomerCurrency1");
        String basicPriceSeatInCustomerCurrency1 = param.get("basicPriceSeatInCustomerCurrency1");
        String basicPriceTicketInReportCurrency1 = param.get("basicPriceTicketInReportCurrency1");
        String basicPriceBaggageInReportCurrency1 = param.get("basicPriceBaggageInReportCurrency1");
        String basicPriceMealsInReportCurrency1 = param.get("basicPriceMealsInReportCurrency1");
        String basicPriceSeatInReportCurrency1 = param.get("basicPriceSeatInReportCurrency1");
        String markupTotal1 = param.get("markupTotal1");
        String markupValueTicket1 = param.get("markupValueTicket1");
        String markupValueBaggage1 = param.get("markupValueBaggage1");
        String markupValueMeals1 = param.get("markupValueMeals1");
        String markupValueSeat1 = param.get("markupValueSeat1");
        String totalMarkUpOnTicketInCustomerCurrency1 = param.get("totalMarkUpOnTicketInCustomerCurrency1");
        String totalMarkUpOnBaggageInCustomerCurrency1 = param.get("totalMarkUpOnBaggageInCustomerCurrency1");
        String totalMarkUpOnMealsInCustomerCurrency1 = param.get("totalMarkUpOnMealsInCustomerCurrency1");
        String totalMarkUpOnSeatInCustomerCurrency1 = param.get("totalMarkUpOnSeatInCustomerCurrency1");
        String commissionPercentage1 = param.get("commissionPercentage1");
        String commissionPercentageValue1 = param.get("commissionPercentageValue1");
        String commissionTotal1 = param.get("commissionTotal1");
        String commissionValueInCustomerCurrency1 = param.get("commissionValueInCustomerCurrency1");
        String netMarkup1 = param.get("netMarkup1");
        String publishedFare1 = param.get("publishedFare1");
        String ticketPriceInCustomerCurrency1 = param.get("ticketPriceInCustomerCurrency1");
        String baggagePriceInCustomerCurrency1 = param.get("baggagePriceInCustomerCurrency1");
        String mealPriceInCustomerCurrency1 = param.get("mealPriceInCustomerCurrency1");
        String seatPriceInCustomerCurrency1 = param.get("seatPriceInCustomerCurrency1");
        String baseFareInReportCurrency1 = param.get("baseFareInReportCurrency1");
        String vendorCurrency1 = param.get("vendorCurrency1");
        String settlementAmountInVendorCurrency1 = param.get("settlementAmountInVendorCurrency1");
        String settlementAmountInCustomerCurrency1 = param.get("settlementAmountInCustomerCurrency1");
        String settlementAmountInReportCurrency1 = param.get("settlementAmountInReportCurrency1");
        String vat1 = param.get("vat1");
        String withholdingTax1 = param.get("withholdingTax1");
        String vatValue1 = param.get("vatValue1");
        String withholdingTaxValue1 = param.get("withholdingTaxValue1");
        String vendorId2 = param.get("vendorId2");
        String orderDetailId2 = param.get("orderDetailId2");
        String source2 = param.get("source2");
        String baseFare2 = param.get("baseFare2");
        String basicPrice2 = param.get("basicPrice2");
        String basicPriceInCustomerCurrency2 = param.get("basicPriceInCustomerCurrency2");
        String basicPriceInReportCurrency2 = param.get("basicPriceInReportCurrency2");
        String otherFees2 = param.get("otherFees2");
        String basicPriceTicket2 = param.get("basicPriceTicket2");
        String basicPriceBaggage2 = param.get("basicPriceBaggage2");
        String basicPriceMeals2 = param.get("basicPriceMeals2");
        String basicPriceSeat2 = param.get("basicPriceSeat2");
        String basicPriceTicketInCustomerCurrency2 = param.get("basicPriceTicketInCustomerCurrency2");
        String basicPriceBaggageInCustomerCurrency2 = param.get("basicPriceBaggageInCustomerCurrency2");
        String basicPriceMealsInCustomerCurrency2 = param.get("basicPriceMealsInCustomerCurrency2");
        String basicPriceSeatInCustomerCurrency2 = param.get("basicPriceSeatInCustomerCurrency2");
        String basicPriceTicketInReportCurrency2 = param.get("basicPriceTicketInReportCurrency2");
        String basicPriceBaggageInReportCurrency2 = param.get("basicPriceBaggageInReportCurrency2");
        String basicPriceMealsInReportCurrency2 = param.get("basicPriceMealsInReportCurrency2");
        String basicPriceSeatInReportCurrency2 = param.get("basicPriceSeatInReportCurrency2");
        String markupTotal2 = param.get("markupTotal2");
        String markupValueTicket2 = param.get("markupValueTicket2");
        String markupValueBaggage2 = param.get("markupValueBaggage2");
        String markupValueMeals2 = param.get("markupValueMeals2");
        String markupValueSeat2 = param.get("markupValueSeat2");
        String totalMarkUpOnTicketInCustomerCurrency2 = param.get("totalMarkUpOnTicketInCustomerCurrency2");
        String totalMarkUpOnBaggageInCustomerCurrency2 = param.get("totalMarkUpOnBaggageInCustomerCurrency2");
        String totalMarkUpOnMealsInCustomerCurrency2 = param.get("totalMarkUpOnMealsInCustomerCurrency2");
        String totalMarkUpOnSeatInCustomerCurrency2 = param.get("totalMarkUpOnSeatInCustomerCurrency2");
        String commissionPercentage2 = param.get("commissionPercentage2");
        String commissionPercentageValue2 = param.get("commissionPercentageValue2");
        String commissionTotal2 = param.get("commissionTotal2");
        String commissionValueInCustomerCurrency2 = param.get("commissionValueInCustomerCurrency2");
        String netMarkup2 = param.get("netMarkup2");
        String publishedFare2 = param.get("publishedFare2");
        String ticketPriceInCustomerCurrency2 = param.get("ticketPriceInCustomerCurrency2");
        String baggagePriceInCustomerCurrency2 = param.get("baggagePriceInCustomerCurrency2");
        String mealPriceInCustomerCurrency2 = param.get("mealPriceInCustomerCurrency2");
        String seatPriceInCustomerCurrency2 = param.get("seatPriceInCustomerCurrency2");
        String baseFareInReportCurrency2 = param.get("baseFareInReportCurrency2");
        String vendorCurrency2 = param.get("vendorCurrency2");
        String settlementAmountInVendorCurrency2 = param.get("settlementAmountInVendorCurrency2");
        String settlementAmountInCustomerCurrency2 = param.get("settlementAmountInCustomerCurrency2");
        String settlementAmountInReportCurrency2 = param.get("settlementAmountInReportCurrency2");
        String vat2 = param.get("vat2");
        String withholdingTax2 = param.get("withholdingTax2");
        String vatValue2 = param.get("vatValue2");
        String withholdingTaxValue2 = param.get("withholdingTaxValue2");
        double tolerance = 0.0000000000000001;

        step("Hit Cron to Clear Lock Transaction");
        financeDashboardPage.getClearLockTransaction();

        step("Publish Kafka 1st Order Detail");
        financeDashboardPage.kafkaPublishFlight1stOrderDetail(kafkaBrokers, kafkaTopic, kafkaPort);

        step("Wait time");
        sleep(Integer.parseInt(waitTimeInMillis) / 1000);

        step("Publish Kafka 2nd Order Detail");
        financeDashboardPage.kafkaPublishFlight2ndOrderDetail(kafkaBrokers, kafkaTopic, kafkaPort);

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Open Commission Dashboard Accordion Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("Go To Flight International Menu");
        financeDashboardPage.goToFlightInternationalMenu();

        step("Filter by Vendor ID and Search");
        financeDashboardPage.searchVendorId(vendorId1);
        financeDashboardPage.clickSearchButtonVendorListingB2C();

        step("Go to Daily Report");
        financeDashboardPage.clickMoreButton();
        financeDashboardPage.clickTransactionsButton();
        
        step("Get The Title of Vertical");
        String dailyVerticalTitleFromDashboard = financeDashboardPage.getDailyVerticalTitleOfFlightInternational();

        step("Verify The Title of Vertical is Correct");
        Assertion.assertThat("Entity is Matched", financeDashboardPage.verifyEqualString(dailyVerticalTitleFromDashboard, verticalTitle), is(true));

        step("Verify Landing on Tab Daily Report");
        Assertion.assertThat("Daily tab is loaded", financeDashboardPage.isDailyTabLoaded(), is(true));

        step("Get The Entries in Daily Report Page");
        B2CDailyDetailOrder b2CDailyDetailOrder = financeDashboardPage.getB2CDailyDetailOrder(vertical, softblockId, searchDateBy, startDate, endDate);

        step("Get the Daily Data");
        B2CDailyDetailOrder.Data b2CDailyDataOrder = b2CDailyDetailOrder.data.get(0);

        step("Get the Daily Data Detail 1");
        B2CDailyDetailOrder.Detail b2CDailyDetailDataOrder1 = b2CDailyDataOrder.detail.get(0);

        step("Get the Daily Data Detail 2");
        B2CDailyDetailOrder.Detail b2CDailyDetailDataOrder2 = b2CDailyDataOrder.detail.get(1);

        step("Get the Daily Data Detail 1 Items 1");
        B2CDailyDetailOrder.Detail.Items b2CDailyDetail1Items1 = b2CDailyDetailDataOrder1.items.get(0);

        step("Get the Daily Data Detail 1 Items 2");
        B2CDailyDetailOrder.Detail.Items b2CDailyDetail1Items2 = b2CDailyDetailDataOrder1.items.get(1);

        step("Get the Daily Data Detail 1 Items 3");
        B2CDailyDetailOrder.Detail.Items b2CDailyDetail1Items3 = b2CDailyDetailDataOrder1.items.get(2);

        step("Get the Daily Data Detail 1 Items 4");
        B2CDailyDetailOrder.Detail.Items b2CDailyDetail1Items4 = b2CDailyDetailDataOrder1.items.get(3);

        step("Get the Daily Data Detail 2 Items 1");
        B2CDailyDetailOrder.Detail.Items b2CDailyDetail2Items1 = b2CDailyDetailDataOrder2.items.get(0);

        step("Get the Daily Data Detail 2 Items 2");
        B2CDailyDetailOrder.Detail.Items b2CDailyDetail2Items2 = b2CDailyDetailDataOrder2.items.get(1);

        step("Get the Daily Data Detail 2 Items 3");
        B2CDailyDetailOrder.Detail.Items b2CDailyDetail2Items3 = b2CDailyDetailDataOrder2.items.get(2);

        step("Get the Daily Data Detail 2 Items 4");
        B2CDailyDetailOrder.Detail.Items b2CDailyDetail2Items4 = b2CDailyDetailDataOrder2.items.get(3);

        step("Verify Vendor ID Order Detail 1 is Match");
        Assertion.assertThat("Verify Vendor ID Order Detail 1 is not null", b2CDailyDetailDataOrder1.vendorId, notNullValue());
        Assertion.assertThat("Verify Vendor ID Order Detail 1 has valid source format", b2CDailyDetailDataOrder1.vendorId, matchesPattern(API_SOURCE_FORMAT_REGEX));

        step("Verify Vendor ID Order Detail 2 is Match");
        Assertion.assertThat("Verify Vendor ID Order Detail 2 is not null", b2CDailyDetailDataOrder2.vendorId, notNullValue());
        Assertion.assertThat("Verify Vendor ID Order Detail 2 has valid source format", b2CDailyDetailDataOrder2.vendorId, matchesPattern(API_SOURCE_FORMAT_REGEX));

        step("Verify Order ID is Match");
        Assertion.assertThat("Order ID is Matched", financeDashboardPage.verifyEqual(b2CDailyDataOrder.orderId, orderId), is(true));

        step("Verify Order Detail ID 1 is Match");
        Assertion.assertThat("Verify Order Detail ID 1 is not null", b2CDailyDetailDataOrder1.orderDetailId, notNullValue());
        Assertion.assertThat("Verify Order Detail ID 1 has valid numeric id format", b2CDailyDetailDataOrder1.orderDetailId, matchesPattern(API_NUMERIC_ID_FORMAT_REGEX));

        step("Verify Order Detail ID 2 is Match");
        Assertion.assertThat("Verify Order Detail ID 2 is not null", b2CDailyDetailDataOrder2.orderDetailId, notNullValue());
        Assertion.assertThat("Verify Order Detail ID 2 has valid numeric id format", b2CDailyDetailDataOrder2.orderDetailId, matchesPattern(API_NUMERIC_ID_FORMAT_REGEX));

        step("Verify Source Order Detail 1 is Match");
        Assertion.assertThat("Verify Source Order Detail 1 is not null", b2CDailyDetailDataOrder1.source, notNullValue());
        Assertion.assertThat("Verify Source Order Detail 1 has valid source format", b2CDailyDetailDataOrder1.source, matchesPattern(API_SOURCE_FORMAT_REGEX));

        step("Verify Source Order Detail 2 is Match");
        Assertion.assertThat("Verify Source Order Detail 2 is not null", b2CDailyDetailDataOrder2.source, notNullValue());
        Assertion.assertThat("Verify Source Order Detail 2 has valid source format", b2CDailyDetailDataOrder2.source, matchesPattern(API_SOURCE_FORMAT_REGEX));

        step("Verify Base Fare Order Detail 1 is Match");
        Assertion.assertThat("Verify Base Fare Order Detail 1 is not null", b2CDailyDetailDataOrder1.baseFare, notNullValue());
        Assertion.assertThat("Verify Base Fare Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.baseFare), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Base Fare Order Detail 2 is Match");
        Assertion.assertThat("Verify Base Fare Order Detail 2 is not null", b2CDailyDetailDataOrder2.baseFare, notNullValue());
        Assertion.assertThat("Verify Base Fare Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.baseFare), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price Order Detail 1 is not null", b2CDailyDetailDataOrder1.basicPrice, notNullValue());
        Assertion.assertThat("Verify Basic Price Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.basicPrice), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price Order Detail 2 is not null", b2CDailyDetailDataOrder2.basicPrice, notNullValue());
        Assertion.assertThat("Verify Basic Price Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.basicPrice), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price In Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price In Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.basicPriceInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price In Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.basicPriceInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price In Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price In Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.basicPriceInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price In Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.basicPriceInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price In Report Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price In Report Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.basicPriceInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price In Report Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.basicPriceInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price In Report Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price In Report Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.basicPriceInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price In Report Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.basicPriceInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Other Fees Order Detail 1 is Match");
        Assertion.assertThat("Verify Other Fees Order Detail 1 is not null", b2CDailyDetailDataOrder1.otherFees, notNullValue());
        Assertion.assertThat("Verify Other Fees Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.otherFees), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Other Fees Order Detail 2 is Match");
        Assertion.assertThat("Verify Other Fees Order Detail 2 is not null", b2CDailyDetailDataOrder2.otherFees, notNullValue());
        Assertion.assertThat("Verify Other Fees Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.otherFees), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Ticket Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price Ticket Order Detail 1 is not null", b2CDailyDetail1Items1.basicPrice, notNullValue());
        Assertion.assertThat("Verify Basic Price Ticket Order Detail 1 has valid price format", String.valueOf(b2CDailyDetail1Items1.basicPrice), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Ticket Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price Ticket Order Detail 2 is not null", b2CDailyDetail2Items1.basicPrice, notNullValue());
        Assertion.assertThat("Verify Basic Price Ticket Order Detail 2 has valid price format", String.valueOf(b2CDailyDetail2Items1.basicPrice), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Baggage Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price Baggage Order Detail 1 is not null", b2CDailyDetail1Items2.basicPrice, notNullValue());
        Assertion.assertThat("Verify Basic Price Baggage Order Detail 1 has valid price format", String.valueOf(b2CDailyDetail1Items2.basicPrice), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Baggage Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price Baggage Order Detail 2 is not null", b2CDailyDetail2Items2.basicPrice, notNullValue());
        Assertion.assertThat("Verify Basic Price Baggage Order Detail 2 has valid price format", String.valueOf(b2CDailyDetail2Items2.basicPrice), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Meals Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price Meals Order Detail 1 is not null", b2CDailyDetail1Items3.basicPrice, notNullValue());
        Assertion.assertThat("Verify Basic Price Meals Order Detail 1 has valid price format", String.valueOf(b2CDailyDetail1Items3.basicPrice), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Meals Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price Meals Order Detail 2 is not null", b2CDailyDetail2Items3.basicPrice, notNullValue());
        Assertion.assertThat("Verify Basic Price Meals Order Detail 2 has valid price format", String.valueOf(b2CDailyDetail2Items3.basicPrice), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Seat Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price Seat Order Detail 1 is not null", b2CDailyDetail1Items4.basicPrice, notNullValue());
        Assertion.assertThat("Verify Basic Price Seat Order Detail 1 has valid price format", String.valueOf(b2CDailyDetail1Items4.basicPrice), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Seat Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price Seat Order Detail 2 is not null", b2CDailyDetail2Items4.basicPrice, notNullValue());
        Assertion.assertThat("Verify Basic Price Seat Order Detail 2 has valid price format", String.valueOf(b2CDailyDetail2Items4.basicPrice), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Ticket In Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price Ticket In Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.basicPriceTicketInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Ticket In Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.basicPriceTicketInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Ticket In Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price Ticket In Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.basicPriceTicketInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Ticket In Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.basicPriceTicketInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Baggage In Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price Baggage In Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.basicPriceBaggageInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Baggage In Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.basicPriceBaggageInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Baggage In Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price Baggage In Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.basicPriceBaggageInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Baggage In Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.basicPriceBaggageInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Meals In Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price Meals In Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.basicPriceMealsInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Meals In Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.basicPriceMealsInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Meals In Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price Meals In Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.basicPriceMealsInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Meals In Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.basicPriceMealsInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Seat In Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price Seat In Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.basicPriceSeatInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Seat In Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.basicPriceSeatInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Seat In Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price Seat In Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.basicPriceSeatInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Seat In Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.basicPriceSeatInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Ticket In Report Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price Ticket In Report Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.basicPriceTicketInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Ticket In Report Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.basicPriceTicketInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Ticket In Report Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price Ticket In Report Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.basicPriceTicketInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Ticket In Report Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.basicPriceTicketInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Baggage In Report Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price Baggage In Report Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.basicPriceBaggageInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Baggage In Report Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.basicPriceBaggageInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Baggage In Report Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price Baggage In Report Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.basicPriceBaggageInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Baggage In Report Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.basicPriceBaggageInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Meals In Report Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price Meals In Report Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.basicPriceMealsInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Meals In Report Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.basicPriceMealsInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Meals In Report Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price Meals In Report Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.basicPriceMealsInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Meals In Report Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.basicPriceMealsInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Seat In Report Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Basic Price Seat In Report Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.basicPriceSeatInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Seat In Report Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.basicPriceSeatInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price Seat In Report Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Basic Price Seat In Report Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.basicPriceSeatInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price Seat In Report Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.basicPriceSeatInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Ticket Order Detail 1 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Ticket Order Detail 1 is not null", b2CDailyDetail1Items1.markupValue, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Ticket Order Detail 1 has valid price format", String.valueOf(b2CDailyDetail1Items1.markupValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Ticket  Order Detail 2 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Ticket  Order Detail 2 is not null", b2CDailyDetail2Items1.markupValue, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Ticket  Order Detail 2 has valid price format", String.valueOf(b2CDailyDetail2Items1.markupValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Baggage Order Detail 1 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Baggage Order Detail 1 is not null", b2CDailyDetail1Items2.markupValue, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Baggage Order Detail 1 has valid price format", String.valueOf(b2CDailyDetail1Items2.markupValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Baggage Order Detail 2 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Baggage Order Detail 2 is not null", b2CDailyDetail2Items2.markupValue, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Baggage Order Detail 2 has valid price format", String.valueOf(b2CDailyDetail2Items2.markupValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Meals Order Detail 1 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Meals Order Detail 1 is not null", b2CDailyDetail1Items3.markupValue, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Meals Order Detail 1 has valid price format", String.valueOf(b2CDailyDetail1Items3.markupValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Meals Order Detail 2 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Meals Order Detail 2 is not null", b2CDailyDetail2Items3.markupValue, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Meals Order Detail 2 has valid price format", String.valueOf(b2CDailyDetail2Items3.markupValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Seat Order Detail 1 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Seat Order Detail 1 is not null", b2CDailyDetail1Items4.markupValue, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Seat Order Detail 1 has valid price format", String.valueOf(b2CDailyDetail1Items4.markupValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Seat Order Detail 2 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Seat Order Detail 2 is not null", b2CDailyDetail2Items4.markupValue, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Seat Order Detail 2 has valid price format", String.valueOf(b2CDailyDetail2Items4.markupValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Ticket in Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Ticket in Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.totalMarkUpOnTicketInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Ticket in Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.totalMarkUpOnTicketInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Ticket in Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Ticket in Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.totalMarkUpOnTicketInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Ticket in Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.totalMarkUpOnTicketInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Baggage in Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Baggage in Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.totalMarkUpOnBaggageInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Baggage in Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.totalMarkUpOnBaggageInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Baggage in Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Baggage in Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.totalMarkUpOnBaggageInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Baggage in Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.totalMarkUpOnBaggageInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Meals in Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Meals in Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.totalMarkUpOnMealsInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Meals in Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.totalMarkUpOnMealsInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Meals in Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Meals in Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.totalMarkUpOnMealsInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Meals in Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.totalMarkUpOnMealsInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Seat in Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Seat in Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.totalMarkUpOnSeatInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Seat in Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.totalMarkUpOnSeatInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up on Seat in Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Total Mark-Up on Seat in Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.totalMarkUpOnSeatInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up on Seat in Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.totalMarkUpOnSeatInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Commission Percentage Order Detail 1 is Match");
        Assertion.assertThat("Verify Commission Percentage Order Detail 1 is not null", b2CDailyDetailDataOrder1.commissionPercentage, notNullValue());
        Assertion.assertThat("Verify Commission Percentage Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.commissionPercentage), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Commission Percentage Order Detail 2 is Match");
        Assertion.assertThat("Verify Commission Percentage Order Detail 2 is not null", b2CDailyDetailDataOrder2.commissionPercentage, notNullValue());
        Assertion.assertThat("Verify Commission Percentage Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.commissionPercentage), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Commission Percentage Value Order Detail 1 is Match");
        Assertion.assertThat("Verify Commission Percentage Value Order Detail 1 is not null", b2CDailyDetailDataOrder1.commissionPercentageValue, notNullValue());
        Assertion.assertThat("Verify Commission Percentage Value Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.commissionPercentageValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Commission Percentage Value Order Detail 2 is Match");
        Assertion.assertThat("Verify Commission Percentage Value Order Detail 2 is not null", b2CDailyDetailDataOrder2.commissionPercentageValue, notNullValue());
        Assertion.assertThat("Verify Commission Percentage Value Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.commissionPercentageValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Commission Value Order Detail 1 is Match");
        Assertion.assertThat("Verify Commission Value Order Detail 1 is not null", b2CDailyDetailDataOrder1.commissionTotal, notNullValue());
        Assertion.assertThat("Verify Commission Value Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.commissionTotal), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Commission Value Order Detail 2 is Match");
        Assertion.assertThat("Verify Commission Value Order Detail 2 is not null", b2CDailyDetailDataOrder2.commissionTotal, notNullValue());
        Assertion.assertThat("Verify Commission Value Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.commissionTotal), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Commission Value in Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Commission Value in Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.commissionValueInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Commission Value in Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.commissionValueInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Commission Value in Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Commission Value in Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.commissionValueInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Commission Value in Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.commissionValueInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Net Mark-Up + Commission Order Detail 1 is Match");
        Assertion.assertThat("Verify Net Mark-Up + Commission Order Detail 1 is not null", b2CDailyDetailDataOrder1.netMarkup, notNullValue());
        Assertion.assertThat("Verify Net Mark-Up + Commission Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.netMarkup), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Net Mark-Up + Commission Order Detail 2 is Match");
        Assertion.assertThat("Verify Net Mark-Up + Commission Order Detail 2 is not null", b2CDailyDetailDataOrder2.netMarkup, notNullValue());
        Assertion.assertThat("Verify Net Mark-Up + Commission Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.netMarkup), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Published Fare (Tiket Price) Order Detail 1 is Match");
        Assertion.assertThat("Verify Published Fare (Tiket Price) Order Detail 1 is not null", b2CDailyDetailDataOrder1.publishedFare, notNullValue());
        Assertion.assertThat("Verify Published Fare (Tiket Price) Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.publishedFare), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Published Fare (Tiket Price) Order Detail 2 is Match");
        Assertion.assertThat("Verify Published Fare (Tiket Price) Order Detail 2 is not null", b2CDailyDetailDataOrder2.publishedFare, notNullValue());
        Assertion.assertThat("Verify Published Fare (Tiket Price) Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.publishedFare), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Ticket Price in Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Ticket Price in Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.ticketPriceInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Ticket Price in Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.ticketPriceInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Ticket Price in Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Ticket Price in Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.ticketPriceInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Ticket Price in Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.ticketPriceInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Baggage Price in Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Baggage Price in Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.baggagePriceInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Baggage Price in Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.baggagePriceInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Baggage Price in Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Baggage Price in Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.baggagePriceInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Baggage Price in Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.baggagePriceInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Meals Price in Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Meals Price in Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.mealPriceInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Meals Price in Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.mealPriceInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Meals Price in Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Meals Price in Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.mealPriceInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Meals Price in Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.mealPriceInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Seat Price in Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Seat Price in Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.seatPriceInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Seat Price in Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.seatPriceInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Verify Seat Price in Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Verify Seat Price in Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.seatPriceInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Verify Seat Price in Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.seatPriceInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Base Fare in Report Currency Order Detail 1 is Match");
        Assertion.assertThat("Base Fare in Report Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.baseFareInReportCurrency, notNullValue());
        Assertion.assertThat("Base Fare in Report Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.baseFareInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Base Fare in Report Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Base Fare in Report Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.baseFareInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Base Fare in Report Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.baseFareInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Vendor Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Vendor Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.vendorCurrency, notNullValue());

        step("Verify Vendor Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Vendor Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.vendorCurrency, notNullValue());

        step("Settlement Amount in Vendor Currency Order Detail 1 is Match");
        Assertion.assertThat("Settlement Amount in Vendor Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.settlementAmountInVendorCurrency, notNullValue());
        Assertion.assertThat("Settlement Amount in Vendor Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.settlementAmountInVendorCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Settlement Amount in Vendor Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Settlement Amount in Vendor Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.settlementAmountInVendorCurrency, notNullValue());
        Assertion.assertThat("Verify Settlement Amount in Vendor Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.settlementAmountInVendorCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Settlement Amount in Customer Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Settlement Amount in Customer Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.settlementAmountInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Settlement Amount in Customer Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.settlementAmountInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Settlement Amount in Customer Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Settlement Amount in Customer Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.settlementAmountInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Settlement Amount in Customer Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.settlementAmountInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Settlement Amount in Report Currency Order Detail 1 is Match");
        Assertion.assertThat("Verify Settlement Amount in Report Currency Order Detail 1 is not null", b2CDailyDetailDataOrder1.settlementAmountInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Settlement Amount in Report Currency Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.settlementAmountInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Settlement Amount in Report Currency Order Detail 2 is Match");
        Assertion.assertThat("Verify Settlement Amount in Report Currency Order Detail 2 is not null", b2CDailyDetailDataOrder2.settlementAmountInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Settlement Amount in Report Currency Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.settlementAmountInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Withholding Order Detail 1 is Match");
        Assertion.assertThat("Verify Withholding Order Detail 1 is not null", b2CDailyDetailDataOrder1.withholdingTax, notNullValue());
        Assertion.assertThat("Verify Withholding Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.withholdingTax), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Withholding Order Detail 2 is Match");
        Assertion.assertThat("Verify Withholding Order Detail 2 is not null", b2CDailyDetailDataOrder2.withholdingTax, notNullValue());
        Assertion.assertThat("Verify Withholding Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.withholdingTax), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify VAT on Total Mark-Up + Commission Order Detail 1 is Match");
        Assertion.assertThat("Verify VAT on Total Mark-Up + Commission Order Detail 1 is not null", b2CDailyDetailDataOrder1.vatValue, notNullValue());
        Assertion.assertThat("Verify VAT on Total Mark-Up + Commission Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.vatValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify VAT on Total Mark-Up + Commission Order Detail 2 is Match");
        Assertion.assertThat("Verify VAT on Total Mark-Up + Commission Order Detail 2 is not null", b2CDailyDetailDataOrder2.vatValue, notNullValue());
        Assertion.assertThat("Verify VAT on Total Mark-Up + Commission Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.vatValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Withholding Tax Value Order Detail 1 is Match");
        Assertion.assertThat("Verify Withholding Tax Value Order Detail 1 is not null", b2CDailyDetailDataOrder1.withholdingTaxValue, notNullValue());
        Assertion.assertThat("Verify Withholding Tax Value Order Detail 1 has valid price format", String.valueOf(b2CDailyDetailDataOrder1.withholdingTaxValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Withholding Tax Value Order Detail 2 is Match");
        Assertion.assertThat("Verify Withholding Tax Value Order Detail 2 is not null", b2CDailyDetailDataOrder2.withholdingTaxValue, notNullValue());
        Assertion.assertThat("Verify Withholding Tax Value Order Detail 2 has valid price format", String.valueOf(b2CDailyDetailDataOrder2.withholdingTaxValue), matchesPattern(API_PRICE_FORMAT_REGEX));
 
    }

    @Author(names = {"Brian"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testB2CMultiOrderTTD() throws Exception {
        Map<String, String> param = getParam("testB2CMultiOrderTTD");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String vertical = param.get("vertical");
        String kafkaBrokers = param.get("kafkaBrokers");
        String kafkaTopic = param.get("kafkaTopic");
        String kafkaPort = param.get("kafkaPort");
        String waitTimeInMillis = param.get("waitTimeInMillis");
        String searchDateBy = param.get("searchDateBy");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String verticalTitle = param.get("verticalTitle");
        String softblockId = param.get("softblockId");
        String orderId = param.get("orderId");
        String vendorId1 = param.get("vendorId1");
        String orderDetailId1 = param.get("orderDetailId1");
        String source1 = param.get("source1");
        String basicPrice1 = param.get("basicPrice1");
        String basicPriceInCustomerCurrency1 = param.get("basicPriceInCustomerCurrency1");
        String basicPriceInReportCurrency1 = param.get("basicPriceInReportCurrency1");
        String markupValue1 = param.get("markupValue1");
        String totalTiketRefundRatesMarkUp1 = param.get("totalTiketRefundRatesMarkUp1");
        String totalPartnerRefundRatesMarkUp1 = param.get("totalPartnerRefundRatesMarkUp1");
        String markupTotal1 = param.get("markupTotal1");
        String netMarkup1 = param.get("netMarkup1");
        String lowestPrice1 = param.get("lowestPrice1");
        String lowestPriceSubsidy1 = param.get("lowestPriceSubsidy1");
        String publishedFare1 = param.get("publishedFare1");
        String actualPublishedFare1 = param.get("actualPublishedFare1");
        String totalAmountDiff1 = param.get("totalAmountDiff1");
        String vendorCurrency1 = param.get("vendorCurrency1");
        String settlementAmountInVendorCurrency1 = param.get("settlementAmountInVendorCurrency1");
        String settlementAmountInCustomerCurrency1 = param.get("settlementAmountInCustomerCurrency1");
        String settlementAmountInReportCurrency1 = param.get("settlementAmountInReportCurrency1");
        String vendorSubsidyCampaignPercentage1 = param.get("vendorSubsidyCampaignPercentage1");
        String vendorSubsidyLoyaltyPercentage1 = param.get("vendorSubsidyLoyaltyPercentage1");
        String vendorSubsidyCampaignValue1 = param.get("vendorSubsidyCampaignValue1");
        String vendorSubsidyLoyaltyValue1 = param.get("vendorSubsidyLoyaltyValue1");
        String vendorSubsidyBuyXGetY1 = param.get("vendorSubsidyBuyXGetY1");
        String partnerSubsidyBundlingValue1 = param.get("partnerSubsidyBundlingValue1");
        String tiketSubsidyCampaignPercentage1 = param.get("tiketSubsidyCampaignPercentage1");
        String tiketSubsidyLoyaltyPercentage1 = param.get("tiketSubsidyLoyaltyPercentage1");
        String tiketSubsidyBundlingValue1 = param.get("tiketSubsidyBundlingValue1");
        String tiketSubsidyCampaignValue1 = param.get("tiketSubsidyCampaignValue1");
        String tiketSubsidyCampaignValueInCustomerCurrency1 = param.get("tiketSubsidyCampaignValueInCustomerCurrency1");
        String tiketSubsidyLoyaltyValue1 = param.get("tiketSubsidyLoyaltyValue1");
        String tiketSubsidyLoyaltyValueInCustomerCurrency1 = param.get("tiketSubsidyLoyaltyValueInCustomerCurrency1");
        String tiketSubsidyBuyXGetY1 = param.get("tiketSubsidyBuyXGetY1");
        String vat1 = param.get("vat1");
        String withholdingTax1 = param.get("withholdingTax1");
        String vatValue1 = param.get("vatValue1");
        String withholdingTaxValue1 = param.get("withholdingTaxValue1");
        String vendorId2 = param.get("vendorId2");
        String orderDetailId2 = param.get("orderDetailId2");
        String source2 = param.get("source2");
        String basicPrice2 = param.get("basicPrice2");
        String basicPriceInCustomerCurrency2 = param.get("basicPriceInCustomerCurrency2");
        String basicPriceInReportCurrency2 = param.get("basicPriceInReportCurrency2");
        String markupValue2 = param.get("markupValue2");
        String totalTiketRefundRatesMarkUp2 = param.get("totalTiketRefundRatesMarkUp2");
        String totalPartnerRefundRatesMarkUp2 = param.get("totalPartnerRefundRatesMarkUp2");
        String markupTotal2 = param.get("markupTotal2");
        String netMarkup2 = param.get("netMarkup2");
        String lowestPrice2 = param.get("lowestPrice2");
        String lowestPriceSubsidy2 = param.get("lowestPriceSubsidy2");
        String publishedFare2 = param.get("publishedFare2");
        String actualPublishedFare2 = param.get("actualPublishedFare2");
        String totalAmountDiff2 = param.get("totalAmountDiff2");
        String vendorCurrency2 = param.get("vendorCurrency2");
        String settlementAmountInVendorCurrency2 = param.get("settlementAmountInVendorCurrency2");
        String settlementAmountInCustomerCurrency2 = param.get("settlementAmountInCustomerCurrency2");
        String settlementAmountInReportCurrency2 = param.get("settlementAmountInReportCurrency2");
        String vendorSubsidyCampaignPercentage2 = param.get("vendorSubsidyCampaignPercentage2");
        String vendorSubsidyLoyaltyPercentage2 = param.get("vendorSubsidyLoyaltyPercentage2");
        String vendorSubsidyCampaignValue2 = param.get("vendorSubsidyCampaignValue2");
        String vendorSubsidyLoyaltyValue2 = param.get("vendorSubsidyLoyaltyValue2");
        String vendorSubsidyBuyXGetY2 = param.get("vendorSubsidyBuyXGetY2");
        String partnerSubsidyBundlingValue2 = param.get("partnerSubsidyBundlingValue2");
        String tiketSubsidyCampaignPercentage2 = param.get("tiketSubsidyCampaignPercentage2");
        String tiketSubsidyLoyaltyPercentage2 = param.get("tiketSubsidyLoyaltyPercentage2");
        String tiketSubsidyBundlingValue2 = param.get("tiketSubsidyBundlingValue2");
        String tiketSubsidyCampaignValue2 = param.get("tiketSubsidyCampaignValue2");
        String tiketSubsidyCampaignValueInCustomerCurrency2 = param.get("tiketSubsidyCampaignValueInCustomerCurrency2");
        String tiketSubsidyLoyaltyValue2 = param.get("tiketSubsidyLoyaltyValue2");
        String tiketSubsidyLoyaltyValueInCustomerCurrency2 = param.get("tiketSubsidyLoyaltyValueInCustomerCurrency2");
        String tiketSubsidyBuyXGetY2 = param.get("tiketSubsidyBuyXGetY2");
        String vat2 = param.get("vat2");
        String withholdingTax2 = param.get("withholdingTax2");
        String vatValue2 = param.get("vatValue2");
        String withholdingTaxValue2 = param.get("withholdingTaxValue2");
        String convenienceFee = param.get("convenienceFee");
        String customerCurrency = param.get("customerCurrency");
        String profit = param.get("profit");
        String netMargin = param.get("netMargin");
        String totalTax = param.get("totalTax");
        String customerFinalPrice = param.get("customerFinalPrice");
        String customerFinalPriceInVendorCurrency = param.get("customerFinalPriceInVendorCurrency");

        double tolerance = 0.0000000000000001;

        step("Hit Cron to Clear Lock Transaction");
        financeDashboardPage.getClearLockTransaction();

        step("Publish Kafka 1st Order Detail");
        financeDashboardPage.kafkaPublishTTD1stOrderDetail(kafkaBrokers, kafkaTopic, kafkaPort);

        step("Wait time");
        sleep(Integer.parseInt(waitTimeInMillis) / 1000);

        step("Publish Kafka 2nd Order Detail");
        financeDashboardPage.kafkaPublishTTD2ndOrderDetail(kafkaBrokers, kafkaTopic, kafkaPort);

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Open Commission Dashboard Accordion Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("Go To TTD Menu");
        financeDashboardPage.goToTTDMenu();

        step("Filter by Vendor ID and Search");
        financeDashboardPage.searchVendorId(vendorId1);
        financeDashboardPage.clickSearchButtonVendorListingB2C();

        step("Go to Daily Report");
        financeDashboardPage.clickMoreButton();
        financeDashboardPage.clickTransactionsButton();
        
        step("Get The Title of Vertical");
        String dailyVerticalTitleFromDashboard = financeDashboardPage.getDailyVerticalTitleOfTTD();

        step("Verify The Title of Vertical is Correct");
        Assertion.assertThat("Entity is Matched", financeDashboardPage.verifyEqualString(dailyVerticalTitleFromDashboard, verticalTitle), is(true));

        step("Verify Landing on Tab Daily Report");
        Assertion.assertThat("Daily tab is loaded", financeDashboardPage.isDailyTabLoaded(), is(true));

        step("Get The Entries in Daily Report Page");
        B2CDailyDetailOrder b2CDailyDetailOrder = financeDashboardPage.getB2CDailyDetailOrder(vertical, softblockId, searchDateBy, startDate, endDate);

        step("Get the Daily Data");
        B2CDailyDetailOrder.Data b2CDailyDataOrder = b2CDailyDetailOrder.data.get(0);

        step("Get the Daily Data Detail 1");
        B2CDailyDetailOrder.Detail b2CDailyDetailDataOrder1 = b2CDailyDataOrder.detail.get(0);

        step("Get the Daily Data Detail 2");
        B2CDailyDetailOrder.Detail b2CDailyDetailDataOrder2 = b2CDailyDataOrder.detail.get(1);

        step("Verify Vendor ID Order Detail 1 is Match");
        Assertion.assertThat("Vendor ID is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.vendorId, vendorId1), is(true));

        step("Verify Vendor ID Order Detail 2 is Match");
        Assertion.assertThat("Vendor ID is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder2.vendorId, vendorId2), is(true));

        step("Verify Order ID is Match");
        Assertion.assertThat("Order ID is Matched", financeDashboardPage.verifyEqual(b2CDailyDataOrder.orderId, orderId), is(true));

        step("Verify Order Detail ID 1 is Match");
        Assertion.assertThat("Order Detail ID 1 is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.orderDetailId, orderDetailId1), is(true));

        step("Verify Order Detail ID 2 is Match");
        Assertion.assertThat("Order Detail ID 2 is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder2.orderDetailId, orderDetailId2), is(true));

        step("Verify Source Order Detail 1 is Match");
        Assertion.assertThat("Source Order Detail 1 is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.source, source1), is(true));

        step("Verify Source Order Detail 2 is Match");
        Assertion.assertThat("Source Order Detail 2 is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder2.source, source2), is(true));

        step("Verify Basic Price (Vendor) Order Detail 1 is Match");
        boolean isBasicPriceMatch1 = Math.abs(b2CDailyDetailDataOrder1.basicPrice - Double.parseDouble(basicPrice1)) < tolerance;
        Assertion.assertThat("Basic Price Order Detail 1 is Matched", isBasicPriceMatch1, is(true));

        step("Verify Basic Price (Vendor)  Order Detail 2 is Match");
        boolean isBasicPriceMatch2 = Math.abs(b2CDailyDetailDataOrder2.basicPrice - Double.parseDouble(basicPrice2)) < tolerance;
        Assertion.assertThat("Basic Price Order Detail 2 is Matched", isBasicPriceMatch2, is(true));

        step("Verify Basic Price In Customer Currency Order Detail 1 is Match");
        boolean isbasicPriceInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.basicPriceInCustomerCurrency - Double.parseDouble(basicPriceInCustomerCurrency1)) < tolerance;
        Assertion.assertThat("Basic Price In Customer Currency Order Detail 1 is Matched", isbasicPriceInCustomerCurrencyMatch1, is(true));

        step("Verify Basic Price In Customer Currency Order Detail 2 is Match");
        boolean isbasicPriceInCustomerCurrencyMatch2 = Math.abs(b2CDailyDetailDataOrder2.basicPriceInCustomerCurrency - Double.parseDouble(basicPriceInCustomerCurrency2)) < tolerance;
        Assertion.assertThat("Basic Price In Customer Currency Order Detail 2 is Matched", isbasicPriceInCustomerCurrencyMatch2, is(true));

        step("Verify Basic Price In Report Currency Order Detail 1 is Match");
        boolean isbasicPriceInReportCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.basicPriceInReportCurrency - Double.parseDouble(basicPriceInReportCurrency1)) < tolerance;
        Assertion.assertThat("Basic Price In Report Currency Order Detail 1 is Matched", isbasicPriceInReportCurrencyMatch1, is(true));

        step("Verify Basic Price In Report Currency Order Detail 2 is Match");
        boolean isbasicPriceInReportCurrencyMatch2 = Math.abs(b2CDailyDetailDataOrder2.basicPriceInReportCurrency - Double.parseDouble(basicPriceInReportCurrency2)) < tolerance;
        Assertion.assertThat("Basic Price In Report Currency Order Detail 2 is Matched", isbasicPriceInReportCurrencyMatch2, is(true));

        step("Verify Mark-Up Value Order Detail 1 is Match");
        boolean ismarkupValueMatch1 = Math.abs(b2CDailyDetailDataOrder1.markupValue - Double.parseDouble(markupValue1)) < tolerance;
        Assertion.assertThat("Total Mark-Up Value (DPP) Order Detail 1 is Matched", ismarkupValueMatch1, is(true));

        step("Verify Mark-Up Value Order Detail 2 is Match");
        boolean ismarkupValueMatch2 = Math.abs(b2CDailyDetailDataOrder2.markupValue - Double.parseDouble(markupValue2)) < tolerance;
        Assertion.assertThat("Total Mark-Up Value (DPP) Order Detail 2 is Matched", ismarkupValueMatch2, is(true));

        step("Verify Tiket Refund Rates Order Detail 1 is Match");
        boolean istotalTiketRefundRatesMarkUpMatch1 = Math.abs(b2CDailyDetailDataOrder1.totalTiketRefundRatesMarkUp - Double.parseDouble(totalTiketRefundRatesMarkUp1)) < tolerance;
        Assertion.assertThat("Tiket Refund Rates Order Detail 1 is Matched", istotalTiketRefundRatesMarkUpMatch1, is(true));

        step("Verify Tiket Refund Rates Order Detail 2 is Match");
        boolean istotalTiketRefundRatesMarkUpMatch2 = Math.abs(b2CDailyDetailDataOrder2.totalTiketRefundRatesMarkUp - Double.parseDouble(totalTiketRefundRatesMarkUp2)) < tolerance;
        Assertion.assertThat("Tiket Refund Rates Order Detail 2 is Matched", istotalTiketRefundRatesMarkUpMatch2, is(true));

        step("Verify Partner Refund Rates Order Detail 1 is Match");
        boolean istotalPartnerRefundRatesMarkUpMarkUpMatch1 = Math.abs(b2CDailyDetailDataOrder1.totalPartnerRefundRatesMarkUp - Double.parseDouble(totalPartnerRefundRatesMarkUp1)) < tolerance;
        Assertion.assertThat("Partner Refund Rates Order Detail 1 is Matched", istotalPartnerRefundRatesMarkUpMarkUpMatch1, is(true));

        step("Verify Partner Refund Rates Order Detail 2 is Match");
        boolean istotalPartnerRefundRatesMarkUpMarkUpMatch2 = Math.abs(b2CDailyDetailDataOrder2.totalPartnerRefundRatesMarkUp - Double.parseDouble(totalPartnerRefundRatesMarkUp2)) < tolerance;
        Assertion.assertThat("Partner Refund Rates Order Detail 2 is Matched", istotalPartnerRefundRatesMarkUpMarkUpMatch2, is(true));

        step("Verify Total Mark-Up Value (DPP) Order Detail 1 is Match");
        boolean ismarkupTotalMatch1 = Math.abs(b2CDailyDetailDataOrder1.markupTotal - Double.parseDouble(markupTotal1)) < tolerance;
        Assertion.assertThat("Total Mark-Up Value (DPP) Order Detail 1 is Matched", ismarkupTotalMatch1, is(true));

        step("Verify Total Mark-Up Value (DPP) Order Detail 2 is Match");
        boolean ismarkupTotalMatch2 = Math.abs(b2CDailyDetailDataOrder2.markupTotal - Double.parseDouble(markupTotal2)) < tolerance;
        Assertion.assertThat("Total Mark-Up Value (DPP) Order Detail 2 is Matched", ismarkupTotalMatch2, is(true));

        step("Verify Net Mark-Up + Commission Order Detail 1 is Match");
        boolean isnetMarkupMatch1 = Math.abs(b2CDailyDetailDataOrder1.netMarkup - Double.parseDouble(netMarkup1)) < tolerance;
        Assertion.assertThat("Net Mark-Up + Commission Order Detail 1 is Matched", isnetMarkupMatch1, is(true));

        step("Verify Net Mark-Up + Commission Order Detail 2 is Match");
        boolean isnetMarkupMatch2 = Math.abs(b2CDailyDetailDataOrder2.netMarkup - Double.parseDouble(netMarkup2)) < tolerance;
        Assertion.assertThat("Net Mark-Up + Commission Order Detail 2 is Matched", isnetMarkupMatch2, is(true));

        step("Verify Tiket Convenience Fee / Platform Fee is Match");
        boolean isconvenienceFeeMatch = Math.abs(b2CDailyDataOrder.convenienceFee - Double.parseDouble(convenienceFee)) < tolerance;
        Assertion.assertThat("Tiket Convenience Fee / Platform Fee Order Detail 1 is Matched", isconvenienceFeeMatch, is(true));

        step("Verify Net Margin is Match");
        boolean isnetMarginMatch = Math.abs(b2CDailyDataOrder.netMargin - Double.parseDouble(netMargin)) < tolerance;
        Assertion.assertThat("Net Margin is Matched", isnetMarginMatch, is(true));

        step("Verify Lowest Price Guarantee (Base Price) Order Detail 1 is Match");
        boolean islowestPriceMatch1 = Math.abs(b2CDailyDetailDataOrder1.lowestPrice - Double.parseDouble(lowestPrice1)) < tolerance;
        Assertion.assertThat("Lowest Price Guarantee (Base Price) Order Detail 1 is Matched", islowestPriceMatch1, is(true));

        step("Verify Lowest Price Guarantee (Base Price) Order Detail 2 is Match");
        boolean islowestPriceMatch2 = Math.abs(b2CDailyDetailDataOrder2.lowestPrice - Double.parseDouble(lowestPrice2)) < tolerance;
        Assertion.assertThat("Lowest Price Guarantee (Base Price) Order Detail 2 is Matched", islowestPriceMatch2, is(true));

        step("Verify Tiket Subsidy - Lowest Price Guarantee in Customer Currency Order Detail 1 is Match");
        boolean islowestPriceSubsidyMatch1 = Math.abs(b2CDailyDetailDataOrder1.lowestPriceSubsidy - Double.parseDouble(lowestPriceSubsidy1)) < tolerance;
        Assertion.assertThat("Tiket Subsidy - Lowest Price Guarantee in Customer Currency Order Detail 1 is Matched", islowestPriceSubsidyMatch1, is(true));

        step("Verify Tiket Subsidy - Lowest Price Guarante in Customer Currency Order Detail 2 is Match");
        boolean islowestPriceSubsidyMatch2 = Math.abs(b2CDailyDetailDataOrder2.lowestPriceSubsidy - Double.parseDouble(lowestPriceSubsidy2)) < tolerance;
        Assertion.assertThat("Tiket Subsidy - Lowest Price Guarante in Customer Currency Order Detail 2 is Matched", islowestPriceSubsidyMatch2, is(true));

        step("Verify Profit is Match");
        boolean isprofitMatch = Math.abs(b2CDailyDataOrder.profit - Double.parseDouble(profit)) < tolerance;
        Assertion.assertThat("Profit in Customer Currency Order Detail 1 is Matched", isprofitMatch, is(true));

        step("Verify Published Fare (Tiket Price) Order Detail 1 is Match");
        boolean ispublishedFareMatch1 = Math.abs(b2CDailyDetailDataOrder1.publishedFare - Double.parseDouble(publishedFare1)) < tolerance;
        Assertion.assertThat("Published Fare (Tiket Price) Order Detail 1 is Matched", ispublishedFareMatch1, is(true));

        step("Verify Published Fare (Tiket Price) Order Detail 2 is Match");
        boolean ispublishedFareMatch2 = Math.abs(b2CDailyDetailDataOrder2.publishedFare - Double.parseDouble(publishedFare2)) < tolerance;
        Assertion.assertThat("Published Fare (Tiket Price) Order Detail 2 is Matched", ispublishedFareMatch2, is(true));

        step("Verify Actual Publish Fare Order Detail 1 is Match");
        boolean isactualPublishedFareMatch1 = Math.abs(b2CDailyDetailDataOrder1.actualPublishedFare - Double.parseDouble(actualPublishedFare1)) < tolerance;
        Assertion.assertThat("Actual Publish Fare in Customer Currency Order Detail 1 is Matched", isactualPublishedFareMatch1, is(true));

        step("Verify Actual Publish Fare Order Detail 2 is Match");
        boolean isactualPublishedFareMatch2 = Math.abs(b2CDailyDetailDataOrder2.actualPublishedFare - Double.parseDouble(actualPublishedFare2)) < tolerance;
        Assertion.assertThat("Tiket Subsidy - Lowest Price Guarante in Customer Currency Order Detail 2 is Matched", isactualPublishedFareMatch2, is(true));

        step("Verify Total Amount Difference in Published Fare Order Detail 1 is Match");
        boolean istotalAmountDiffMatch1 = Math.abs(b2CDailyDetailDataOrder1.totalAmountDiff - Double.parseDouble(totalAmountDiff1)) < tolerance;
        Assertion.assertThat("Total Amount Difference in Published Fare in Customer Currency Order Detail 1 is Matched", istotalAmountDiffMatch1, is(true));

        step("Verify Total Amount Difference in Published Fare Order Detail 2 is Match");
        boolean istotalAmountDiffMatch2 = Math.abs(b2CDailyDetailDataOrder2.totalAmountDiff - Double.parseDouble(totalAmountDiff2)) < tolerance;
        Assertion.assertThat("Total Amount Difference in Published Fare Order Detail 2 is Matched", istotalAmountDiffMatch2, is(true));

        step("Verify Customer Currency is Match");
        Assertion.assertThat("Customer Currency is Matched", financeDashboardPage.verifyEqual(b2CDailyDataOrder.customerCurrency, customerCurrency), is(true));

        step("Verify Customer Final Price in Customer Currency Order Detail 1 is Match");
        boolean iscustomerFinalPriceMatch = Math.abs(b2CDailyDataOrder.customerFinalPrice - Double.parseDouble(customerFinalPrice)) < tolerance;
        Assertion.assertThat("Customer Final Price in Customer Currency Order Detail 1 is Matched", iscustomerFinalPriceMatch, is(true));

        step("Verify Vendor Currency Order Detail 1 is Match");
        Assertion.assertThat("Vendor Currency Detail 1 is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.vendorCurrency, vendorCurrency1), is(true));

        step("Verify Vendor Currency Order Detail 2 is Match");
        Assertion.assertThat("Vendor Currency Detail 2 is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder2.vendorCurrency, vendorCurrency2), is(true));

        step("Verify Customer Final Price in Vendor Currency Order Detail 1 is Match");
        boolean iscustomerFinalPriceInVendorCurrencyMatch2 = Math.abs(b2CDailyDataOrder.customerFinalPriceInVendorCurrency - Double.parseDouble(customerFinalPriceInVendorCurrency)) < tolerance;
        Assertion.assertThat("Customer Final Price in Vendor Currency Order Detail 1 is Matched", iscustomerFinalPriceInVendorCurrencyMatch2, is(true));

        step("Settlement Amount in Vendor Currency Order Detail 1 is Match");
        boolean issettlementAmountMatch1 = Math.abs(b2CDailyDetailDataOrder1.settlementAmountInVendorCurrency - Double.parseDouble(settlementAmountInVendorCurrency1)) < tolerance;
        Assertion.assertThat("Settlement Amount in Vendor Currency Order Detail 1 is Matched", issettlementAmountMatch1, is(true));

        step("Verify Settlement Amount in Vendor Currency Order Detail 2 is Match");
        boolean issettlementAmountMatch2 = Math.abs(b2CDailyDetailDataOrder2.settlementAmountInVendorCurrency - Double.parseDouble(settlementAmountInVendorCurrency2)) < tolerance;
        Assertion.assertThat("Settlement Amount in Vendor Currency Order Detail 2 is Matched", issettlementAmountMatch2, is(true));

        step("Verify Settlement Amount in Customer Currency Order Detail 1 is Match");
        boolean issettlementAmountInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.settlementAmountInCustomerCurrency - Double.parseDouble(settlementAmountInCustomerCurrency1)) < tolerance;
        Assertion.assertThat("Settlement Amount in Customer Currency Order Detail 1 is Matched", issettlementAmountInCustomerCurrencyMatch1, is(true));

        step("Verify Settlement Amount in Customer Currency Order Detail 2 is Match");
        boolean issettlementAmountInCustomerCurrencyMatch2 = Math.abs(b2CDailyDetailDataOrder2.settlementAmountInCustomerCurrency - Double.parseDouble(settlementAmountInCustomerCurrency2)) < tolerance;
        Assertion.assertThat("Settlement Amount in Customer Currency Order Detail 2 is Matched", issettlementAmountInCustomerCurrencyMatch2, is(true));

        step("Verify Settlement Amount in Report Currency Order Detail 1 is Match");
        boolean issettlementAmountInReportCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.settlementAmountInReportCurrency - Double.parseDouble(settlementAmountInReportCurrency1)) < tolerance;
        Assertion.assertThat("Settlement Amount in Report Currency Order Detail 1 is Matched", issettlementAmountInReportCurrencyMatch1, is(true));

        step("Verify Settlement Amount in Report Currency Order Detail 2 is Match");
        boolean issettlementAmountInReportCurrencyMatch2 = Math.abs(b2CDailyDetailDataOrder2.settlementAmountInReportCurrency - Double.parseDouble(settlementAmountInReportCurrency2)) < tolerance;
        Assertion.assertThat("Settlement Amount in Report Currency Order Detail 2 is Matched", issettlementAmountInReportCurrencyMatch2, is(true));

        step("Verify Vendor Subsidy % - Campaign (Generic) Order Detail 1 is Match");
        boolean isvendorSubsidyCampaignPercentageMatch1 = Math.abs(b2CDailyDetailDataOrder1.vendorSubsidyCampaignPercentage - Double.parseDouble(vendorSubsidyCampaignPercentage1)) < tolerance;
        Assertion.assertThat("Vendor Subsidy % - Campaign (Generic) Order Detail 1 is Matched", isvendorSubsidyCampaignPercentageMatch1, is(true));

        step("Verify Vendor Subsidy % - Campaign (Generic) Order Detail 2 is Match");
        boolean isvendorSubsidyCampaignPercentageMatch2 = Math.abs(b2CDailyDetailDataOrder2.vendorSubsidyCampaignPercentage - Double.parseDouble(vendorSubsidyCampaignPercentage2)) < tolerance;
        Assertion.assertThat("Vendor Subsidy % - Campaign (Generic) Order Detail 2 is Matched", isvendorSubsidyCampaignPercentageMatch2, is(true));

        step("Verify Vendor Subsidy % - Loyalty Order Detail 1 is Match");
        boolean isvendorSubsidyLoyaltyPercentageMatch1 = Math.abs(b2CDailyDetailDataOrder1.vendorSubsidyLoyaltyPercentage - Double.parseDouble(vendorSubsidyLoyaltyPercentage1)) < tolerance;
        Assertion.assertThat("Vendor Subsidy % - Loyalty Order Detail 1 is Matched", isvendorSubsidyLoyaltyPercentageMatch1, is(true));

        step("Verify Vendor Subsidy % - Loyalty Order Detail 2 is Match");
        boolean isvendorSubsidyLoyaltyPercentageMatch2 = Math.abs(b2CDailyDetailDataOrder2.vendorSubsidyLoyaltyPercentage - Double.parseDouble(vendorSubsidyLoyaltyPercentage2)) < tolerance;
        Assertion.assertThat("Vendor Subsidy % - Loyalty Order Detail 2 is Matched", isvendorSubsidyLoyaltyPercentageMatch2, is(true));

        step("Verify Vendor Subsidy Value - Campaign (Generic) Order Detail 1 is Match");
        boolean isvendorSubsidyCampaignValueMatch1 = Math.abs(b2CDailyDetailDataOrder1.vendorSubsidyCampaignValue - Double.parseDouble(vendorSubsidyCampaignValue1)) < tolerance;
        Assertion.assertThat("Vendor Subsidy Value - Campaign (Generic) Order Detail 1 is Matched", isvendorSubsidyCampaignValueMatch1, is(true));

        step("Verify Vendor Subsidy Value - Campaign (Generic) Order Detail 2 is Match");
        boolean isvendorSubsidyCampaignValueMatch2 = Math.abs(b2CDailyDetailDataOrder2.vendorSubsidyCampaignValue - Double.parseDouble(vendorSubsidyCampaignValue2)) < tolerance;
        Assertion.assertThat("Vendor Subsidy Value - Campaign (Generic) Order Detail 2 is Matched", isvendorSubsidyCampaignValueMatch2, is(true));

        step("Verify Vendor Subsidy Value - Loyalty Order Detail 1 is Match");
        boolean isvendorSubsidyLoyaltyValueMatch1 = Math.abs(b2CDailyDetailDataOrder1.vendorSubsidyLoyaltyValue - Double.parseDouble(vendorSubsidyLoyaltyValue1)) < tolerance;
        Assertion.assertThat("Vendor Subsidy Value - Loyalty Order Detail 1 is Matched", isvendorSubsidyLoyaltyValueMatch1, is(true));

        step("Verify Vendor Subsidy Value - Loyalty Order Detail 2 is Match");
        boolean isvendorSubsidyLoyaltyValueMatch2 = Math.abs(b2CDailyDetailDataOrder2.vendorSubsidyLoyaltyValue - Double.parseDouble(vendorSubsidyLoyaltyValue2)) < tolerance;
        Assertion.assertThat("Vendor Subsidy Value - Loyalty Order Detail 2 is Matched", isvendorSubsidyLoyaltyValueMatch2, is(true));

        step("Verify Vendor Subsidy - Buy X Get Y Order Detail 1 is Match");
        boolean isvendorSubsidyBuyXGetYMatch1 = Math.abs(b2CDailyDetailDataOrder1.vendorSubsidyBuyXGetY - Double.parseDouble(vendorSubsidyBuyXGetY1)) < tolerance;
        Assertion.assertThat("Vendor Subsidy - Buy X Get Y Order Detail 1 is Matched", isvendorSubsidyBuyXGetYMatch1, is(true));

        step("Verify Vendor Subsidy - Buy X Get Y Order Detail 2 is Match");
        boolean isvendorSubsidyBuyXGetYMatch2 = Math.abs(b2CDailyDetailDataOrder2.vendorSubsidyBuyXGetY - Double.parseDouble(vendorSubsidyBuyXGetY2)) < tolerance;
        Assertion.assertThat("Vendor Subsidy - Buy X Get Y Order Detail 2 is Matched", isvendorSubsidyBuyXGetYMatch2, is(true));

        step("Verify Vendor Subsidy Value - Bundling Order Detail 1 is Match");
        boolean ispartnerSubsidyBundlingValueMatch1 = Math.abs(b2CDailyDetailDataOrder1.partnerSubsidyBundlingValue - Double.parseDouble(partnerSubsidyBundlingValue1)) < tolerance;
        Assertion.assertThat("Vendor Subsidy Value - Bundling Order Detail 1 is Matched", ispartnerSubsidyBundlingValueMatch1, is(true));

        step("Verify Vendor Subsidy Value - Bundling Order Detail 2 is Match");
        boolean ispartnerSubsidyBundlingValueMatch2 = Math.abs(b2CDailyDetailDataOrder2.partnerSubsidyBundlingValue - Double.parseDouble(partnerSubsidyBundlingValue2)) < tolerance;
        Assertion.assertThat("Vendor Subsidy Value - Bundling Order Detail 2 is Matched", ispartnerSubsidyBundlingValueMatch2, is(true));

        step("Verify Tiket Subsidy % - Campaign (Generic) Order Detail 1 is Match");
        boolean istiketSubsidyCampaignPercentageMatch1 = Math.abs(b2CDailyDetailDataOrder1.tiketSubsidyCampaignPercentage - Double.parseDouble(tiketSubsidyCampaignPercentage1)) < tolerance;
        Assertion.assertThat("Tiket Subsidy % - Campaign (Generic) Order Detail 1 is Matched", istiketSubsidyCampaignPercentageMatch1, is(true));

        step("Verify Tiket Subsidy % - Campaign (Generic) Order Detail 2 is Match");
        boolean istiketSubsidyCampaignPercentageMatch2 = Math.abs(b2CDailyDetailDataOrder2.tiketSubsidyCampaignPercentage - Double.parseDouble(tiketSubsidyCampaignPercentage2)) < tolerance;
        Assertion.assertThat("Tiket Subsidy % - Campaign (Generic) Order Detail 2 is Matched", istiketSubsidyCampaignPercentageMatch2, is(true));

        step("Verify Tiket Subsidy % - Loyalty Order Detail 1 is Match");
        boolean istiketSubsidyLoyaltyPercentageMatch1 = Math.abs(b2CDailyDetailDataOrder1.tiketSubsidyLoyaltyPercentage - Double.parseDouble(tiketSubsidyLoyaltyPercentage1)) < tolerance;
        Assertion.assertThat("Tiket Subsidy % - Loyalty Order Detail 1 is Matched", istiketSubsidyLoyaltyPercentageMatch1, is(true));

        step("Verify Tiket Subsidy % - Loyalty Order Detail 2 is Match");
        boolean istiketSubsidyLoyaltyPercentageMatch2 = Math.abs(b2CDailyDetailDataOrder2.tiketSubsidyLoyaltyPercentage - Double.parseDouble(tiketSubsidyLoyaltyPercentage2)) < tolerance;
        Assertion.assertThat("Tiket Subsidy % - Loyalty Order Detail 2 is Matched", istiketSubsidyLoyaltyPercentageMatch2, is(true));

        step("Verify Tiket Subsidy Value - Campaign (Generic) Order Detail 1 is Match");
        boolean istiketSubsidyCampaignValueMatch1 = Math.abs(b2CDailyDetailDataOrder1.tiketSubsidyCampaignValue - Double.parseDouble(tiketSubsidyCampaignValue1)) < tolerance;
        Assertion.assertThat("Tiket Subsidy Value - Campaign (Generic) Order Detail 1 is Matched", istiketSubsidyCampaignValueMatch1, is(true));

        step("Verify Tiket Subsidy Value - Campaign (Generic) Order Detail 2 is Match");
        boolean istiketSubsidyCampaignValueMatch2 = Math.abs(b2CDailyDetailDataOrder2.tiketSubsidyCampaignValue - Double.parseDouble(tiketSubsidyCampaignValue2)) < tolerance;
        Assertion.assertThat("Tiket Subsidy Value - Campaign (Generic) Order Detail 2 is Matched", istiketSubsidyCampaignValueMatch2, is(true));

        step("Verify Tiket Subsidy Value - Loyalty Order Detail 1 is Match");
        boolean istiketSubsidyLoyaltyValueMatch1 = Math.abs(b2CDailyDetailDataOrder1.tiketSubsidyLoyaltyValue - Double.parseDouble(tiketSubsidyLoyaltyValue1)) < tolerance;
        Assertion.assertThat("Tiket Subsidy Value - Loyalty Order Detail 1 is Matched", istiketSubsidyLoyaltyValueMatch1, is(true));

        step("Verify Tiket Subsidy Value - Loyalty Order Detail 2 is Match");
        boolean istiketSubsidyLoyaltyValueMatch2 = Math.abs(b2CDailyDetailDataOrder2.tiketSubsidyLoyaltyValue - Double.parseDouble(tiketSubsidyLoyaltyValue2)) < tolerance;
        Assertion.assertThat("Tiket Subsidy Value - Loyalty Order Detail 2 is Matched", istiketSubsidyLoyaltyValueMatch2, is(true));

        step("Verify Tiket Subsidy - Buy X Get Y Order Detail 1 is Match");
        boolean istiketSubsidyBuyXGetYMatch1 = Math.abs(b2CDailyDetailDataOrder1.tiketSubsidyBuyXGetY - Double.parseDouble(tiketSubsidyBuyXGetY1)) < tolerance;
        Assertion.assertThat("Tiket Subsidy - Buy X Get Y Order Detail 1 is Matched", istiketSubsidyBuyXGetYMatch1, is(true));

        step("Verify Tiket Subsidy - Buy X Get Y Order Detail 2 is Match");
        boolean istiketSubsidyBuyXGetYMatch2 = Math.abs(b2CDailyDetailDataOrder2.tiketSubsidyBuyXGetY - Double.parseDouble(tiketSubsidyBuyXGetY2)) < tolerance;
        Assertion.assertThat("Tiket Subsidy - Buy X Get Y Order Detail 2 is Matched", istiketSubsidyBuyXGetYMatch2, is(true));

        step("Verify Tiket Subsidy Value - Bundling Order Detail 1 is Match");
        boolean istiketSubsidyBundlingValueMatch1 = Math.abs(b2CDailyDetailDataOrder1.tiketSubsidyBundlingValue - Double.parseDouble(tiketSubsidyBundlingValue1)) < tolerance;
        Assertion.assertThat("Tiket Subsidy Value - Bundling Order Detail 1 is Matched", istiketSubsidyBundlingValueMatch1, is(true));

        step("Verify Tiket Subsidy Value - Bundling Order Detail 2 is Match");
        boolean istiketSubsidyBundlingValueMatch2 = Math.abs(b2CDailyDetailDataOrder2.tiketSubsidyBundlingValue - Double.parseDouble(tiketSubsidyBundlingValue2)) < tolerance;
        Assertion.assertThat("Tiket Subsidy Value - Bundling Order Detail 2 is Matched", istiketSubsidyBundlingValueMatch2, is(true));

        step("Verify Tiket Subsidy Value - Campaign (Generic) in Customer Currency Order Detail 1 is Match");
        boolean istiketSubsidyCampaignValueInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.tiketSubsidyCampaignValue - Double.parseDouble(tiketSubsidyCampaignValueInCustomerCurrency1)) < tolerance;
        Assertion.assertThat("Tiket Subsidy Value - Campaign (Generic) in Customer Currency Order Detail 1 is Matched", istiketSubsidyCampaignValueInCustomerCurrencyMatch1, is(true));

        step("Verify Tiket Subsidy Value - Campaign (Generic) in Customer Currency Order Detail 2 is Match");
        boolean istiketSubsidyCampaignValueInCustomerCurrencyMatch2 = Math.abs(b2CDailyDetailDataOrder2.tiketSubsidyCampaignValue - Double.parseDouble(tiketSubsidyCampaignValueInCustomerCurrency2)) < tolerance;
        Assertion.assertThat("Tiket Subsidy Value - Campaign (Generic) in Customer Currency Order Detail 2 is Matched", istiketSubsidyCampaignValueInCustomerCurrencyMatch2, is(true));

        step("Verify Tiket Subsidy Value - Loyalty in Customer Currency Order Detail 1 is Match");
        boolean istiketSubsidyLoyaltyValueInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.tiketSubsidyLoyaltyValue - Double.parseDouble(tiketSubsidyLoyaltyValueInCustomerCurrency1)) < tolerance;
        Assertion.assertThat("Tiket Subsidy Value - Loyalty in Customer Currency Order Detail 1 is Matched", istiketSubsidyLoyaltyValueInCustomerCurrencyMatch1, is(true));

        step("Verify Tiket Subsidy Value - Loyalty in Customer Currency Order Detail 2 is Match");
        boolean istiketSubsidyLoyaltyValueInCustomerCurrencyMatch2 = Math.abs(b2CDailyDetailDataOrder2.tiketSubsidyLoyaltyValue - Double.parseDouble(tiketSubsidyLoyaltyValueInCustomerCurrency2)) < tolerance;
        Assertion.assertThat("Tiket Subsidy Value - Loyalty in Customer Currency Order Detail 2 is Matched", istiketSubsidyLoyaltyValueInCustomerCurrencyMatch2, is(true));

        step("Verify VAT Order Detail 1 is Match");
        boolean isvatMatch1 = Math.abs(b2CDailyDetailDataOrder1.vat - Double.parseDouble(vat1)) < tolerance;
        Assertion.assertThat("VAT Order Detail 1 is Matched", isvatMatch1, is(true));

        step("Verify VAT Order Detail 2 is Match");
        boolean isvatMatch2 = Math.abs(b2CDailyDetailDataOrder2.vat - Double.parseDouble(vat2)) < tolerance;
        Assertion.assertThat("VAT Order Detail 2 is Matched", isvatMatch2, is(true));

        step("Verify Withholding Order Detail 1 is Match");
        boolean iswithholdingTaxMatch1 = Math.abs(b2CDailyDetailDataOrder1.withholdingTax - Double.parseDouble(withholdingTax1)) < tolerance;
        Assertion.assertThat("Withholding Order Detail 1 is Matched", iswithholdingTaxMatch1, is(true));

        step("Verify Withholding Order Detail 2 is Match");
        boolean iswithholdingTaxMatch2 = Math.abs(b2CDailyDetailDataOrder2.withholdingTax - Double.parseDouble(withholdingTax2)) < tolerance;
        Assertion.assertThat("Withholding Order Detail 2 is Matched", iswithholdingTaxMatch2, is(true));

        step("Verify VAT on Total Mark-Up + Commission Order Detail 1 is Match");
        boolean isvatValueMatch1 = Math.abs(b2CDailyDetailDataOrder1.vatValue - Double.parseDouble(vatValue1)) < tolerance;
        Assertion.assertThat("VAT on Total Mark-Up + Commission Order Detail 1 is Matched", isvatValueMatch1, is(true));

        step("Verify VAT on Total Mark-Up + Commission Order Detail 2 is Match");
        boolean isvatValueMatch2 = Math.abs(b2CDailyDetailDataOrder2.vatValue - Double.parseDouble(vatValue2)) < tolerance;
        Assertion.assertThat("VAT on Total Mark-Up + Commission Order Detail 2 is Matched", isvatValueMatch2, is(true));

        step("Verify Withholding Tax Value Order Detail 1 is Match");
        boolean iswithholdingTaxValueMatch1 = Math.abs(b2CDailyDetailDataOrder1.withholdingTaxValue - Double.parseDouble(withholdingTaxValue1)) < tolerance;
        Assertion.assertThat("Withholding Tax Value Order Detail 1 is Matched", iswithholdingTaxValueMatch1, is(true));

        step("Verify Withholding Tax Value Order Detail 2 is Match");
        boolean iswithholdingTaxValueMatch2 = Math.abs(b2CDailyDetailDataOrder2.withholdingTaxValue - Double.parseDouble(withholdingTaxValue2)) < tolerance;
        Assertion.assertThat("Withholding Tax Value Order Detail 2 is Matched", iswithholdingTaxValueMatch2, is(true));

        step("Verify Total Tax on Order Level is Match");
        boolean istotalTaxMatch = Math.abs(b2CDailyDataOrder.totalTax - Double.parseDouble(totalTax)) < tolerance;
        Assertion.assertThat("Total Tax on Order Level is Matched", istotalTaxMatch, is(true));

    }

    @Author(names = {"Brian"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testB2CSingleOrderTTD() throws Exception {
        Map<String, String> param = getParam("testB2CSingleOrderTTD");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String vertical = param.get("vertical");
        String kafkaBrokers = param.get("kafkaBrokers");
        String kafkaTopic = param.get("kafkaTopic");
        String kafkaPort = param.get("kafkaPort");
        String waitTimeInMillis = param.get("waitTimeInMillis");
        String searchDateBy = param.get("searchDateBy");
        String verticalTitle = param.get("verticalTitle");
        String softblockId = param.get("softblockId");
        String orderId = param.get("orderId");
        String vendorId = param.get("vendorId");
        String orderDetailId = param.get("orderDetailId");
        String source = param.get("source");
        String basicPrice = param.get("basicPrice");
        String basicPriceInCustomerCurrency = param.get("basicPriceInCustomerCurrency");
        String basicPriceInReportCurrency = param.get("basicPriceInReportCurrency");
        String markupValue = param.get("markupValue");
        String totalTiketRefundRatesMarkUp = param.get("totalTiketRefundRatesMarkUp");
        String totalPartnerRefundRatesMarkUp = param.get("totalPartnerRefundRatesMarkUp");
        String markupTotal = param.get("markupTotal");
        String netMarkup = param.get("netMarkup");
        String lowestPrice = param.get("lowestPrice");
        String lowestPriceSubsidy = param.get("lowestPriceSubsidy");
        String publishedFare = param.get("publishedFare");
        String actualPublishedFare = param.get("actualPublishedFare");
        String totalAmountDiff = param.get("totalAmountDiff");
        String vendorCurrency = param.get("vendorCurrency");
        String settlementAmountInVendorCurrency = param.get("settlementAmountInVendorCurrency");
        String settlementAmountInCustomerCurrency = param.get("settlementAmountInCustomerCurrency");
        String settlementAmountInReportCurrency = param.get("settlementAmountInReportCurrency");
        String vendorSubsidyCampaignPercentage = param.get("vendorSubsidyCampaignPercentage");
        String vendorSubsidyLoyaltyPercentage = param.get("vendorSubsidyLoyaltyPercentage");
        String vendorSubsidyCampaignValue = param.get("vendorSubsidyCampaignValue");
        String vendorSubsidyLoyaltyValue = param.get("vendorSubsidyLoyaltyValue");
        String vendorSubsidyBuyXGetY = param.get("vendorSubsidyBuyXGetY");
        String partnerSubsidyBundlingValue = param.get("partnerSubsidyBundlingValue");
        String tiketSubsidyCampaignPercentage = param.get("tiketSubsidyCampaignPercentage");
        String tiketSubsidyLoyaltyPercentage = param.get("tiketSubsidyLoyaltyPercentage");
        String tiketSubsidyBundlingValue = param.get("tiketSubsidyBundlingValue");
        String tiketSubsidyCampaignValue = param.get("tiketSubsidyCampaignValue");
        String tiketSubsidyCampaignValueInCustomerCurrency = param.get("tiketSubsidyCampaignValueInCustomerCurrency");
        String tiketSubsidyLoyaltyValue = param.get("tiketSubsidyLoyaltyValue");
        String tiketSubsidyLoyaltyValueInCustomerCurrency = param.get("tiketSubsidyLoyaltyValueInCustomerCurrency");
        String tiketSubsidyBuyXGetY = param.get("tiketSubsidyBuyXGetY");
        String vat = param.get("vat");
        String withholdingTax = param.get("withholdingTax");
        String vatValue = param.get("vatValue");
        String withholdingTaxValue = param.get("withholdingTaxValue");
        String convenienceFee = param.get("convenienceFee");
        String customerCurrency = param.get("customerCurrency");
        String profit = param.get("profit");
        String netMargin = param.get("netMargin");
        String totalTax = param.get("totalTax");
        String customerFinalPrice = param.get("customerFinalPrice");
        String customerFinalPriceInVendorCurrency = param.get("customerFinalPriceInVendorCurrency");

        step("Delete Order ID");
        financeDashboardPage.deleteOrderId(softblockId, orderId);

        step("Hit Cron to Clear Lock Transaction");
        financeDashboardPage.getClearLockTransaction();

        step("Publish Kafka 1st Order Detail");
        financeDashboardPage.kafkaPublishTTD3rdOrderDetail(kafkaBrokers, kafkaTopic, kafkaPort);

        step("Wait time");
        sleep(Integer.parseInt(waitTimeInMillis) / 1000);

        step("Publish Kafka 2nd Order Detail");
        financeDashboardPage.kafkaPublishTTD3rdOrderDetail(kafkaBrokers, kafkaTopic, kafkaPort);

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Open Commission Dashboard Accordion Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("Go To TTD Menu");
        financeDashboardPage.goToTTDMenu();

        step("Filter by Vendor ID and Search");
        financeDashboardPage.searchVendorId(vendorId);
        financeDashboardPage.clickSearchButtonVendorListingB2C();

        step("Go to Daily Report");
        financeDashboardPage.clickMoreButton();
        financeDashboardPage.clickTransactionsButton();
        step("Get The Title of Vertical");
        String dailyVerticalTitleFromDashboard = financeDashboardPage.getDailyVerticalTitleOfTTD();

        step("Verify The Title of Vertical is Correct");
        Assertion.assertThat("Entity is Matched", financeDashboardPage.verifyEqualString(dailyVerticalTitleFromDashboard, verticalTitle), is(true));

        step("Verify Landing on Tab Daily Report");
        Assertion.assertThat("Daily tab is loaded", financeDashboardPage.isDailyTabLoaded(), is(true));

        step("Get The Entries in Daily Report Page");
        String dailyReportDate = CoreFunctions.getJakartaDateYYYY_MM_DD(0);
        B2CDailyDetailOrder b2CDailyDetailOrder = financeDashboardPage.getB2CDailyDetailOrder(
                vertical, softblockId, searchDateBy, dailyReportDate, dailyReportDate);

        step("Get the Daily Data");
        B2CDailyDetailOrder.Data b2CDailyDataOrder = b2CDailyDetailOrder.data.get(0);

        step("Get the Daily Data Detail");
        B2CDailyDetailOrder.Detail b2CDailyDetailDataOrder1 = b2CDailyDataOrder.detail.get(0);

        step("Verify Vendor ID Order Detail  is Match");
        Assertion.assertThat("Vendor ID is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.vendorId, vendorId), is(true));
       
        step("Verify Order ID is Match");
        Assertion.assertThat("Order ID is Matched", financeDashboardPage.verifyEqual(b2CDailyDataOrder.orderId, orderId), is(true));

        step("Verify Order Detail ID  is Match");
        Assertion.assertThat("Order Detail ID  is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.orderDetailId, orderDetailId), is(true));

        step("Verify Source Order Detail  is Match");
        Assertion.assertThat("Source Order Detail  is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.source, source), is(true));

        step("Verify Basic Price (Vendor) Order Detail  is Match");
        Assertion.assertThat("Verify Basic Price (Vendor) Order Detail is not null", b2CDailyDetailDataOrder1.basicPrice, notNullValue());
        Assertion.assertThat("Verify Basic Price (Vendor) Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.basicPrice), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price In Customer Currency Order Detail  is Match");
        Assertion.assertThat("Verify Basic Price In Customer Currency Order Detail is not null", b2CDailyDetailDataOrder1.basicPriceInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price In Customer Currency Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.basicPriceInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Basic Price In Report Currency Order Detail is Match");
        Assertion.assertThat("Verify Basic Price In Report Currency Order Detail is not null", b2CDailyDetailDataOrder1.basicPriceInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Basic Price In Report Currency Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.basicPriceInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Mark-Up Value Order Detail  is Match");
        Assertion.assertThat("Verify Mark-Up Value Order Detail is not null", b2CDailyDetailDataOrder1.markupValue, notNullValue());
        Assertion.assertThat("Verify Mark-Up Value Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.markupValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Tiket Refund Rates Order Detail  is Match");
        Assertion.assertThat("Verify Tiket Refund Rates Order Detail is not null", b2CDailyDetailDataOrder1.totalTiketRefundRatesMarkUp, notNullValue());
        Assertion.assertThat("Verify Tiket Refund Rates Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.totalTiketRefundRatesMarkUp), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Partner Refund Rates Order Detail  is Match");
        Assertion.assertThat("Verify Partner Refund Rates Order Detail is not null", b2CDailyDetailDataOrder1.totalPartnerRefundRatesMarkUp, notNullValue());
        Assertion.assertThat("Verify Partner Refund Rates Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.totalPartnerRefundRatesMarkUp), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Mark-Up Value (DPP) Order Detail  is Match");
        Assertion.assertThat("Verify Total Mark-Up Value (DPP) Order Detail is not null", b2CDailyDetailDataOrder1.markupTotal, notNullValue());
        Assertion.assertThat("Verify Total Mark-Up Value (DPP) Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.markupTotal), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Net Mark-Up + Commission Order Detail  is Match");
        Assertion.assertThat("Verify Net Mark-Up + Commission Order Detail is not null", b2CDailyDetailDataOrder1.netMarkup, notNullValue());
        Assertion.assertThat("Verify Net Mark-Up + Commission Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.netMarkup), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Tiket Convenience Fee / Platform Fee is Match");
        Assertion.assertThat("Verify Tiket Convenience Fee / Platform Fee is not null", b2CDailyDataOrder.convenienceFee, notNullValue());
        Assertion.assertThat("Verify Tiket Convenience Fee / Platform Fee has valid price format", String.valueOf(b2CDailyDataOrder.convenienceFee), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Net Margin is Match");
        Assertion.assertThat("Verify Net Margin is not null", b2CDailyDataOrder.netMargin, notNullValue());
        Assertion.assertThat("Verify Net Margin has valid price format", String.valueOf(b2CDailyDataOrder.netMargin), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Lowest Price Guarantee (Base Price) Order Detail  is Match");
        Assertion.assertThat("Verify Lowest Price Guarantee (Base Price) Order Detail is not null", b2CDailyDetailDataOrder1.lowestPrice, notNullValue());
        Assertion.assertThat("Verify Lowest Price Guarantee (Base Price) Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.lowestPrice), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Tiket Subsidy - Lowest Price Guarantee in Customer Currency Order Detail  is Match");
        Assertion.assertThat("Verify Tiket Subsidy - Lowest Price Guarantee in Customer Currency Order Detail is not null", b2CDailyDetailDataOrder1.lowestPriceSubsidy, notNullValue());
        Assertion.assertThat("Verify Tiket Subsidy - Lowest Price Guarantee in Customer Currency Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.lowestPriceSubsidy), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Profit is Match");
        Assertion.assertThat("Verify Profit is not null", b2CDailyDataOrder.profit, notNullValue());
        Assertion.assertThat("Verify Profit has valid price format", String.valueOf(b2CDailyDataOrder.profit), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Published Fare (Tiket Price) Order Detail  is Match");
        Assertion.assertThat("Verify Published Fare (Tiket Price) Order Detail is not null", b2CDailyDetailDataOrder1.publishedFare, notNullValue());
        Assertion.assertThat("Verify Published Fare (Tiket Price) Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.publishedFare), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Actual Publish Fare Order Detail  is Match");
        Assertion.assertThat("Verify Actual Publish Fare Order Detail is not null", b2CDailyDetailDataOrder1.actualPublishedFare, notNullValue());
        Assertion.assertThat("Verify Actual Publish Fare Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.actualPublishedFare), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Amount Difference in Published Fare Order Detail  is Match");
        Assertion.assertThat("Verify Total Amount Difference in Published Fare Order Detail is not null", b2CDailyDetailDataOrder1.totalAmountDiff, notNullValue());
        Assertion.assertThat("Verify Total Amount Difference in Published Fare Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.totalAmountDiff), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Customer Currency is Match");
        Assertion.assertThat("Customer Currency is Matched", financeDashboardPage.verifyEqual(b2CDailyDataOrder.customerCurrency, customerCurrency), is(true));

        step("Verify Customer Final Price in Customer Currency Order Detail  is Match");
        Assertion.assertThat("Verify Customer Final Price in Customer Currency Order Detail is not null", b2CDailyDataOrder.customerFinalPrice, notNullValue());
        Assertion.assertThat("Verify Customer Final Price in Customer Currency Order Detail has valid price format", String.valueOf(b2CDailyDataOrder.customerFinalPrice), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Vendor Currency Order Detail  is Match");
        Assertion.assertThat("Verify Vendor Currency Order Detail is not null", b2CDailyDetailDataOrder1.vendorCurrency, notNullValue());

        step("Verify Customer Final Price in Vendor Currency Order Detail  is Match");
        Assertion.assertThat("Verify Customer Final Price in Vendor Currency Order Detail is not null", b2CDailyDataOrder.customerFinalPriceInVendorCurrency, notNullValue());
        Assertion.assertThat("Verify Customer Final Price in Vendor Currency Order Detail has valid price format", String.valueOf(b2CDailyDataOrder.customerFinalPriceInVendorCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Settlement Amount in Vendor Currency Order Detail  is Match");
        Assertion.assertThat("Verify Settlement Amount in Vendor Currency Order Detail is not null", b2CDailyDetailDataOrder1.settlementAmountInVendorCurrency, notNullValue());
        Assertion.assertThat("Verify Settlement Amount in Vendor Currency Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.settlementAmountInVendorCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Settlement Amount in Customer Currency Order Detail  is Match");
        Assertion.assertThat("Verify Settlement Amount in Customer Currency Order Detail is not null", b2CDailyDetailDataOrder1.settlementAmountInCustomerCurrency, notNullValue());
        Assertion.assertThat("Verify Settlement Amount in Customer Currency Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.settlementAmountInCustomerCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Settlement Amount in Report Currency Order Detail  is Match");
        Assertion.assertThat("Verify Settlement Amount in Report Currency Order Detail is not null", b2CDailyDetailDataOrder1.settlementAmountInReportCurrency, notNullValue());
        Assertion.assertThat("Verify Settlement Amount in Report Currency Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.settlementAmountInReportCurrency), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Vendor Subsidy % - Campaign (Generic) Order Detail  is Match");
        Assertion.assertThat("Verify Vendor Subsidy % - Campaign (Generic) Order Detail is not null", b2CDailyDetailDataOrder1.vendorSubsidyCampaignPercentage, notNullValue());
        Assertion.assertThat("Verify Vendor Subsidy % - Campaign (Generic) Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.vendorSubsidyCampaignPercentage), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Vendor Subsidy % - Loyalty Order Detail  is Match");
        Assertion.assertThat("Verify Vendor Subsidy % - Loyalty Order Detail is not null", b2CDailyDetailDataOrder1.vendorSubsidyLoyaltyPercentage, notNullValue());
        Assertion.assertThat("Verify Vendor Subsidy % - Loyalty Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.vendorSubsidyLoyaltyPercentage), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Vendor Subsidy Value - Campaign (Generic) Order Detail  is Match");
        Assertion.assertThat("Verify Vendor Subsidy Value - Campaign (Generic) Order Detail is not null", b2CDailyDetailDataOrder1.vendorSubsidyCampaignValue, notNullValue());
        Assertion.assertThat("Verify Vendor Subsidy Value - Campaign (Generic) Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.vendorSubsidyCampaignValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Vendor Subsidy Value - Loyalty Order Detail is Match");
        Assertion.assertThat("Verify Vendor Subsidy Value - Loyalty Order Detail is not null", b2CDailyDetailDataOrder1.vendorSubsidyLoyaltyValue, notNullValue());
        Assertion.assertThat("Verify Vendor Subsidy Value - Loyalty Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.vendorSubsidyLoyaltyValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Vendor Subsidy - Buy X Get Y Order Detail is Match");
        Assertion.assertThat("Verify Vendor Subsidy - Buy X Get Y Order Detail is not null", b2CDailyDetailDataOrder1.vendorSubsidyBuyXGetY, notNullValue());
        Assertion.assertThat("Verify Vendor Subsidy - Buy X Get Y Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.vendorSubsidyBuyXGetY), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Vendor Subsidy Value - Bundling Order Detail is Match");
        Assertion.assertThat("Verify Vendor Subsidy Value - Bundling Order Detail is not null", b2CDailyDetailDataOrder1.partnerSubsidyBundlingValue, notNullValue());
        Assertion.assertThat("Verify Vendor Subsidy Value - Bundling Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.partnerSubsidyBundlingValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Tiket Subsidy % - Campaign (Generic) Order Detail  is Match");
        Assertion.assertThat("Verify Tiket Subsidy % - Campaign (Generic) Order Detail is not null", b2CDailyDetailDataOrder1.tiketSubsidyCampaignPercentage, notNullValue());
        Assertion.assertThat("Verify Tiket Subsidy % - Campaign (Generic) Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.tiketSubsidyCampaignPercentage), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Tiket Subsidy % - Loyalty Order Detail  is Match");
        Assertion.assertThat("Verify Tiket Subsidy % - Loyalty Order Detail is not null", b2CDailyDetailDataOrder1.tiketSubsidyLoyaltyPercentage, notNullValue());
        Assertion.assertThat("Verify Tiket Subsidy % - Loyalty Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.tiketSubsidyLoyaltyPercentage), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Tiket Subsidy Value - Campaign (Generic) Order Detail is Match");
        Assertion.assertThat("Verify Tiket Subsidy Value - Campaign (Generic) Order Detail is not null", b2CDailyDetailDataOrder1.tiketSubsidyCampaignValue, notNullValue());
        Assertion.assertThat("Verify Tiket Subsidy Value - Campaign (Generic) Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.tiketSubsidyCampaignValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Tiket Subsidy Value - Loyalty Order Detail is Match");
        Assertion.assertThat("Verify Tiket Subsidy Value - Loyalty Order Detail is not null", b2CDailyDetailDataOrder1.tiketSubsidyLoyaltyValue, notNullValue());
        Assertion.assertThat("Verify Tiket Subsidy Value - Loyalty Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.tiketSubsidyLoyaltyValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Tiket Subsidy - Buy X Get Y Order Detail is Match");
        Assertion.assertThat("Verify Tiket Subsidy - Buy X Get Y Order Detail is not null", b2CDailyDetailDataOrder1.tiketSubsidyBuyXGetY, notNullValue());
        Assertion.assertThat("Verify Tiket Subsidy - Buy X Get Y Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.tiketSubsidyBuyXGetY), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Tiket Subsidy Value - Bundling Order Detail  is Match");
        Assertion.assertThat("Verify Tiket Subsidy Value - Bundling Order Detail is not null", b2CDailyDetailDataOrder1.tiketSubsidyBundlingValue, notNullValue());
        Assertion.assertThat("Verify Tiket Subsidy Value - Bundling Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.tiketSubsidyBundlingValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Tiket Subsidy Value - Campaign (Generic) in Customer Currency Order Detail  is Match");
        Assertion.assertThat("Verify Tiket Subsidy Value - Campaign (Generic) in Customer Currency Order Detail is not null", b2CDailyDetailDataOrder1.tiketSubsidyCampaignValue, notNullValue());
        Assertion.assertThat("Verify Tiket Subsidy Value - Campaign (Generic) in Customer Currency Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.tiketSubsidyCampaignValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Tiket Subsidy Value - Loyalty in Customer Currency Order Detail  is Match");
        Assertion.assertThat("Verify Tiket Subsidy Value - Loyalty in Customer Currency Order Detail is not null", b2CDailyDetailDataOrder1.tiketSubsidyLoyaltyValue, notNullValue());
        Assertion.assertThat("Verify Tiket Subsidy Value - Loyalty in Customer Currency Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.tiketSubsidyLoyaltyValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify VAT Order Detail  is Match");
        Assertion.assertThat("Verify VAT Order Detail is not null", b2CDailyDetailDataOrder1.vat, notNullValue());
        Assertion.assertThat("Verify VAT Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.vat), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Withholding Order Detail  is Match");
        Assertion.assertThat("Verify Withholding Order Detail is not null", b2CDailyDetailDataOrder1.withholdingTax, notNullValue());
        Assertion.assertThat("Verify Withholding Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.withholdingTax), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify VAT on Total Mark-Up + Commission Order Detail  is Match");
        Assertion.assertThat("Verify VAT on Total Mark-Up + Commission Order Detail is not null", b2CDailyDetailDataOrder1.vatValue, notNullValue());
        Assertion.assertThat("Verify VAT on Total Mark-Up + Commission Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.vatValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Withholding Tax Value Order Detail is Match");
        Assertion.assertThat("Verify Withholding Tax Value Order Detail is not null", b2CDailyDetailDataOrder1.withholdingTaxValue, notNullValue());
        Assertion.assertThat("Verify Withholding Tax Value Order Detail has valid price format", String.valueOf(b2CDailyDetailDataOrder1.withholdingTaxValue), matchesPattern(API_PRICE_FORMAT_REGEX));

        step("Verify Total Tax on Order Level is Match");
        Assertion.assertThat("Verify Total Tax on Order Level is not null", b2CDailyDataOrder.totalTax, notNullValue());
        Assertion.assertThat("Verify Total Tax on Order Level has valid price format", String.valueOf(b2CDailyDataOrder.totalTax), matchesPattern(API_PRICE_FORMAT_REGEX));

    }

    @Author(names = {"Brian"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testB2CSingleOrderFlightDomestic() throws Exception {
        Map<String, String> param = getParam("testB2CSingleOrderFlightDomestic");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String vertical = param.get("vertical");
        String kafkaBrokers = param.get("kafkaBrokers");
        String kafkaTopic = param.get("kafkaTopic");
        String kafkaPort = param.get("kafkaPort");
        String searchDateBy = param.get("searchDateBy");
        String verticalTitle = param.get("verticalTitle");
        String softblockId = param.get("softblockId");
        String orderId = param.get("orderId");
        String vendorId = param.get("vendorId");
        String orderDetailId = param.get("orderDetailId");
        String source = param.get("source");
        String baseFare = param.get("baseFare");
        String basicPrice = param.get("basicPrice");
        String basicPriceInCustomerCurrency = param.get("basicPriceInCustomerCurrency");
        String basicPriceInReportCurrency = param.get("basicPriceInReportCurrency");
        String otherFees = param.get("otherFees");
        String basicPriceTicket = param.get("basicPriceTicket");
        String basicPriceBaggage = param.get("basicPriceBaggage");
        String basicPriceMeals = param.get("basicPriceMeals");
        String basicPriceSeat = param.get("basicPriceSeat");
        String basicPriceTicketInCustomerCurrency = param.get("basicPriceTicketInCustomerCurrency");
        String basicPriceBaggageInCustomerCurrency = param.get("basicPriceBaggageInCustomerCurrency");
        String basicPriceMealsInCustomerCurrency = param.get("basicPriceMealsInCustomerCurrency");
        String basicPriceSeatInCustomerCurrency = param.get("basicPriceSeatInCustomerCurrency");
        String basicPriceTicketInReportCurrency = param.get("basicPriceTicketInReportCurrency");
        String basicPriceBaggageInReportCurrency = param.get("basicPriceBaggageInReportCurrency");
        String basicPriceMealsInReportCurrency = param.get("basicPriceMealsInReportCurrency");
        String basicPriceSeatInReportCurrency = param.get("basicPriceSeatInReportCurrency");
        String markupTotal = param.get("markupTotal");
        String markupValueTicket = param.get("markupValueTicket");
        String markupValueBaggage = param.get("markupValueBaggage");
        String markupValueMeals = param.get("markupValueMeals");
        String markupValueSeat = param.get("markupValueSeat");
        String totalMarkUpOnTicketInCustomerCurrency = param.get("totalMarkUpOnTicketInCustomerCurrency");
        String totalMarkUpOnBaggageInCustomerCurrency = param.get("totalMarkUpOnBaggageInCustomerCurrency");
        String totalMarkUpOnMealsInCustomerCurrency = param.get("totalMarkUpOnMealsInCustomerCurrency");
        String totalMarkUpOnSeatInCustomerCurrency = param.get("totalMarkUpOnSeatInCustomerCurrency");
        String commissionPercentage = param.get("commissionPercentage");
        String commissionPercentageValue = param.get("commissionPercentageValue");
        String commissionTotal = param.get("commissionTotal");
        String commissionValueInCustomerCurrency = param.get("commissionValueInCustomerCurrency");
        String netMarkup = param.get("netMarkup");
        String publishedFare = param.get("publishedFare");
        String ticketPriceInCustomerCurrency = param.get("ticketPriceInCustomerCurrency");
        String baggagePriceInCustomerCurrency = param.get("baggagePriceInCustomerCurrency");
        String mealPriceInCustomerCurrency = param.get("mealPriceInCustomerCurrency");
        String seatPriceInCustomerCurrency = param.get("seatPriceInCustomerCurrency");
        String baseFareInReportCurrency = param.get("baseFareInReportCurrency");
        String vendorCurrency = param.get("vendorCurrency");
        String settlementAmountInVendorCurrency = param.get("settlementAmountInVendorCurrency");
        String settlementAmountInCustomerCurrency = param.get("settlementAmountInCustomerCurrency");
        String settlementAmountInReportCurrency = param.get("settlementAmountInReportCurrency");
        String vat = param.get("vat");
        String withholdingTax = param.get("withholdingTax");
        String vatValue = param.get("vatValue");
        String withholdingTaxValue = param.get("withholdingTaxValue");

        double tolerance = 0.0000000000000001;

        step("Delete Order ID");
        financeDashboardPage.deleteOrderId(softblockId, orderId);

        step("Hit Cron to Clear Lock Transaction");
        financeDashboardPage.getClearLockTransaction();

        step("Publish Kafka 1st Order Detail");
        financeDashboardPage.kafkaPublishFlight3rdOrderDetail(kafkaBrokers, kafkaTopic, kafkaPort);

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Open Commission Dashboard Accordion Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("Go To Flight Domestic Menu");
        financeDashboardPage.goToFlightDomesticMenu1();

        step("Filter by Vendor ID and Search");
        financeDashboardPage.searchVendorId(vendorId);
        financeDashboardPage.clickSearchButtonVendorListingB2C();

        step("Go to Daily Report");
        financeDashboardPage.clickMoreButton();
        financeDashboardPage.clickTransactionsButton();
        step("Get The Title of Vertical");
        String dailyVerticalTitleFromDashboard = financeDashboardPage.getDailyVerticalTitleOfDomestic();

        step("Verify The Title of Vertical is Correct");
        Assertion.assertThat("Entity is Matched", financeDashboardPage.verifyEqualString(dailyVerticalTitleFromDashboard, verticalTitle), is(true));

        step("Verify Landing on Tab Daily Report");
        Assertion.assertThat("Daily tab is loaded", financeDashboardPage.isDailyTabLoaded(), is(true));

        step("Get The Entries in Daily Report Page");
        String dailyReportDate = CoreFunctions.getJakartaDateYYYY_MM_DD(0);
        B2CDailyDetailOrder b2CDailyDetailOrder = financeDashboardPage.getB2CDailyDetailOrder(
                vertical, softblockId, searchDateBy, dailyReportDate, dailyReportDate);

        step("Get the Daily Data");
        B2CDailyDetailOrder.Data b2CDailyDataOrder = b2CDailyDetailOrder.data.get(0);

        step("Get the Daily Data Detail 1");
        B2CDailyDetailOrder.Detail b2CDailyDetailDataOrder1 = b2CDailyDataOrder.detail.get(0);

        step("Get the Daily Data Detail 1 Items 1");
        B2CDailyDetailOrder.Detail.Items b2CDailyDetail1Items1 = b2CDailyDetailDataOrder1.items.get(0);

        step("Get the Daily Data Detail 1 Items 2");
        B2CDailyDetailOrder.Detail.Items b2CDailyDetail1Items2 = b2CDailyDetailDataOrder1.items.get(1);

        step("Get the Daily Data Detail 1 Items 3");
        B2CDailyDetailOrder.Detail.Items b2CDailyDetail1Items3 = b2CDailyDetailDataOrder1.items.get(2);

        step("Get the Daily Data Detail 1 Items 4");
        B2CDailyDetailOrder.Detail.Items b2CDailyDetail1Items4 = b2CDailyDetailDataOrder1.items.get(3);

        step("Verify Order ID is Match");
        Assertion.assertThat("Order ID is Matched", financeDashboardPage.verifyEqual(b2CDailyDataOrder.orderId, orderId), is(true));

        step("Verify Order Detail ID 1 is Match");
        Assertion.assertThat("Order Detail ID 1 is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.orderDetailId, orderDetailId), is(true));

        step("Verify Source Order Detail 1 is Match");
        Assertion.assertThat("Source Order Detail 1 is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.source, source), is(true));

        step("Verify Base Fare Order Detail 1 is Match");
        boolean isBaseFareMatch1 = Math.abs(b2CDailyDetailDataOrder1.baseFare - Double.parseDouble(baseFare)) < tolerance;
        Assertion.assertThat("Base Fare Order Detail 1 is Matched", isBaseFareMatch1, is(true));

        step("Verify Basic Price Order Detail 1 is Match");
        boolean isBasicPriceMatch1 = Math.abs(b2CDailyDetailDataOrder1.basicPrice - Double.parseDouble(basicPrice)) < tolerance;
        Assertion.assertThat("Basic Price Order Detail 1 is Matched", isBasicPriceMatch1, is(true));

        step("Verify Basic Price In Customer Currency Order Detail 1 is Match");
        boolean isbasicPriceInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.basicPriceInCustomerCurrency - Double.parseDouble(basicPriceInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Basic Price In Customer Currency Order Detail 1 is Matched", isbasicPriceInCustomerCurrencyMatch1, is(true));

        step("Verify Basic Price In Report Currency Order Detail 1 is Match");
        boolean isbasicPriceInReportCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.basicPriceInReportCurrency - Double.parseDouble(basicPriceInReportCurrency)) < tolerance;
        Assertion.assertThat("Basic Price In Report Currency Order Detail 1 is Matched", isbasicPriceInReportCurrencyMatch1, is(true));

        step("Verify Other Fees Order Detail 1 is Match");
        boolean isotherFeesMatch1 = Math.abs(b2CDailyDetailDataOrder1.otherFees - Double.parseDouble(otherFees)) < tolerance;
        Assertion.assertThat("Other Fees Order Detail 1 is Matched", isotherFeesMatch1, is(true));

        step("Verify Basic Price Ticket Order Detail 1 is Match");
        boolean isbasicPriceTicketMatch1 = Math.abs(b2CDailyDetail1Items1.basicPrice - Double.parseDouble(basicPriceTicket)) < tolerance;
        Assertion.assertThat("Basic Price Ticket Order Detail 1 is Matched", isbasicPriceTicketMatch1, is(true));

        step("Verify Basic Price Baggage Order Detail 1 is Match");
        boolean isbasicPriceBaggageMatch1 = Math.abs(b2CDailyDetail1Items2.basicPrice - Double.parseDouble(basicPriceBaggage)) < tolerance;
        Assertion.assertThat("Basic Price Baggage Order Detail 1 is Matched", isbasicPriceBaggageMatch1, is(true));

        step("Verify Basic Price Meals Order Detail 1 is Match");
        boolean isbasicPriceMealsMatch1 = Math.abs(b2CDailyDetail1Items3.basicPrice - Double.parseDouble(basicPriceMeals)) < tolerance;
        Assertion.assertThat("Basic Price Meals Order Detail 1 is Matched", isbasicPriceMealsMatch1, is(true));

        step("Verify Basic Price Seat Order Detail 1 is Match");
        boolean isbasicPriceSeatMatch1 = Math.abs(b2CDailyDetail1Items4.basicPrice - Double.parseDouble(basicPriceSeat)) < tolerance;
        Assertion.assertThat("Basic Price Seat Order Detail 1 is Matched", isbasicPriceSeatMatch1, is(true));

        step("Verify Basic Price Ticket In Customer Currency Order Detail 1 is Match");
        boolean isbasicPriceTicketInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.basicPriceTicketInCustomerCurrency - Double.parseDouble(basicPriceTicketInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Basic Price Ticket In Customer Currency Order Detail 1 is Matched", isbasicPriceTicketInCustomerCurrencyMatch1, is(true));

        step("Verify Basic Price Baggage In Customer Currency Order Detail 1 is Match");
        boolean isbasicPriceBaggageInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.basicPriceBaggageInCustomerCurrency - Double.parseDouble(basicPriceBaggageInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Basic Price Baggage In Customer Currency Order Detail 1 is Matched", isbasicPriceBaggageInCustomerCurrencyMatch1, is(true));

        step("Verify Basic Price Meals In Customer Currency Order Detail 1 is Match");
        boolean isbasicPriceMealsInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.basicPriceMealsInCustomerCurrency - Double.parseDouble(basicPriceMealsInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Basic Price Meals In Customer Currency Order Detail 1 is Matched", isbasicPriceMealsInCustomerCurrencyMatch1, is(true));

        step("Verify Basic Price Seat In Customer Currency Order Detail 1 is Match");
        boolean isbasicPriceSeatInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.basicPriceSeatInCustomerCurrency - Double.parseDouble(basicPriceSeatInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Basic Price Order Detail 1 is Matched", isbasicPriceSeatInCustomerCurrencyMatch1, is(true));

        step("Verify Basic Price Ticket In Report Currency Order Detail 1 is Match");
        boolean isbasicPriceTicketInReportCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.basicPriceTicketInReportCurrency - Double.parseDouble(basicPriceTicketInReportCurrency)) < tolerance;
        Assertion.assertThat("Basic Price Ticket In Report Currency Order Detail 1 is Matched", isbasicPriceTicketInReportCurrencyMatch1, is(true));

        step("Verify Basic Price Baggage In Report Currency Order Detail 1 is Match");
        boolean isbasicPriceBaggageInReportCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.basicPriceBaggageInReportCurrency - Double.parseDouble(basicPriceBaggageInReportCurrency)) < tolerance;
        Assertion.assertThat("Basic Price Baggage In Report Currency Order Detail 1 is Matched", isbasicPriceBaggageInReportCurrencyMatch1, is(true));

        step("Verify Basic Price Meals In Report Currency Order Detail 1 is Match");
        boolean isbasicPriceMealsInReportCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.basicPriceMealsInReportCurrency - Double.parseDouble(basicPriceMealsInReportCurrency)) < tolerance;
        Assertion.assertThat("Basic Price Meals In Report Currency Order Detail 1 is Matched", isbasicPriceMealsInReportCurrencyMatch1, is(true));

        step("Verify Basic Price Seat In Report Currency Order Detail 1 is Match");
        boolean isbasicPriceSeatInReportCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.basicPriceSeatInReportCurrency - Double.parseDouble(basicPriceSeatInReportCurrency)) < tolerance;
        Assertion.assertThat("Basic Price Seat In Report Currency Order Detail 1 is Matched", isbasicPriceSeatInReportCurrencyMatch1, is(true));

        step("Verify Total Mark-Up Value (DPP) Order Detail 1 is Match");
        boolean ismarkupTotalMatch1 = Math.abs(b2CDailyDetailDataOrder1.markupTotal - Double.parseDouble(markupTotal)) < tolerance;
        Assertion.assertThat("Total Mark-Up Value (DPP) Order Detail 1 is Matched", ismarkupTotalMatch1, is(true));

        step("Verify Total Mark-Up on Ticket Order Detail 1 is Match");
        boolean ismarkupValueTicketMatch1 = Math.abs(b2CDailyDetail1Items1.markupValue - Double.parseDouble(markupValueTicket)) < tolerance;
        Assertion.assertThat("Total Mark-Up on Ticket Order Detail 1 is Matched", ismarkupValueTicketMatch1, is(true));

        step("Verify Total Mark-Up on Baggage Order Detail 1 is Match");
        boolean ismarkupValueBaggage1Match1 = Math.abs(b2CDailyDetail1Items2.markupValue - Double.parseDouble(markupValueBaggage)) < tolerance;
        Assertion.assertThat("Total Mark-Up on Baggage Order Detail 1 is Matched", ismarkupValueBaggage1Match1, is(true));

        step("Verify Total Mark-Up on Meals Order Detail 1 is Match");
        boolean ismarkupValueMealsMatch1 = Math.abs(b2CDailyDetail1Items3.markupValue - Double.parseDouble(markupValueMeals)) < tolerance;
        Assertion.assertThat("Total Mark-Up on Meals Order Detail 1 is Matched", ismarkupValueMealsMatch1, is(true));

        step("Verify Total Mark-Up on Seat Order Detail 1 is Match");
        boolean ismarkupValueSeateMatch1 = Math.abs(b2CDailyDetail1Items4.markupValue - Double.parseDouble(markupValueSeat)) < tolerance;
        Assertion.assertThat("Total Mark-Up on Seat Order Detail 1 is Matched", ismarkupValueSeateMatch1, is(true));

        step("Verify Total Mark-Up on Ticket in Customer Currency Order Detail 1 is Match");
        boolean istotalMarkUpOnTicketInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.totalMarkUpOnTicketInCustomerCurrency - Double.parseDouble(totalMarkUpOnTicketInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Total Mark-Up on Ticket in Customer Currency Order Detail 1 is Matched", istotalMarkUpOnTicketInCustomerCurrencyMatch1, is(true));

        step("Verify Total Mark-Up on Baggage in Customer Currency Order Detail 1 is Match");
        boolean istotalMarkUpOnBaggageInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.totalMarkUpOnBaggageInCustomerCurrency - Double.parseDouble(totalMarkUpOnBaggageInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Total Mark-Up on Baggage in Customer Currency Order Detail 1 is Matched", istotalMarkUpOnBaggageInCustomerCurrencyMatch1, is(true));

        step("Verify Total Mark-Up on Meals in Customer Currency Order Detail 1 is Match");
        boolean istotalMarkUpOnMealsInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.totalMarkUpOnMealsInCustomerCurrency - Double.parseDouble(totalMarkUpOnMealsInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Total Mark-Up on Meals in Customer Currency Order Detail 1 is Matched", istotalMarkUpOnMealsInCustomerCurrencyMatch1, is(true));

        step("Verify Total Mark-Up on Seat in Customer Currency Order Detail 1 is Match");
        boolean istotalMarkUpOnSeatInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.totalMarkUpOnSeatInCustomerCurrency - Double.parseDouble(totalMarkUpOnSeatInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Total Mark-Up on Seat in Customer Currency Order Detail 1 is Matched", istotalMarkUpOnSeatInCustomerCurrencyMatch1, is(true));

        step("Verify Commission Percentage Order Detail 1 is Match");
        boolean iscommissionPercentageMatch1 = Math.abs(b2CDailyDetailDataOrder1.commissionPercentage - Double.parseDouble(commissionPercentage)) < tolerance;
        Assertion.assertThat("Commission Percentage Order Detail 1 is Matched", iscommissionPercentageMatch1, is(true));

        step("Verify Commission Percentage Value Order Detail 1 is Match");
        boolean iscommissionPercentageValueMatch1 = Math.abs(b2CDailyDetailDataOrder1.commissionPercentageValue - Double.parseDouble(commissionPercentageValue)) < tolerance;
        Assertion.assertThat("Commission Percentage Value Order Detail 1 is Matched", iscommissionPercentageValueMatch1, is(true));

        step("Verify Commission Value Order Detail 1 is Match");
        boolean iscommissionTotalMatch1 = Math.abs(b2CDailyDetailDataOrder1.commissionTotal - Double.parseDouble(commissionTotal)) < tolerance;
        Assertion.assertThat("Commission Value Order Detail 1 is Matched", iscommissionTotalMatch1, is(true));

        step("Verify Commission Value in Customer Currency Order Detail 1 is Match");
        boolean iscommissionValueInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.commissionValueInCustomerCurrency - Double.parseDouble(commissionValueInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Commission Value in Customer Currency Order Detail 1 is Matched", iscommissionValueInCustomerCurrencyMatch1, is(true));

        step("Verify Net Mark-Up + Commission Order Detail 1 is Match");
        boolean isnetMarkupMatch1 = Math.abs(b2CDailyDetailDataOrder1.netMarkup - Double.parseDouble(netMarkup)) < tolerance;
        Assertion.assertThat("Net Mark-Up + Commission Order Detail 1 is Matched", isnetMarkupMatch1, is(true));

        step("Verify Published Fare (Tiket Price) Order Detail 1 is Match");
        boolean ispublishedFareMatch1 = Math.abs(b2CDailyDetailDataOrder1.publishedFare - Double.parseDouble(publishedFare)) < tolerance;
        Assertion.assertThat("Published Fare (Tiket Price) Order Detail 1 is Matched", ispublishedFareMatch1, is(true));

        step("Verify Ticket Price in Customer Currency Order Detail 1 is Match");
        boolean isticketPriceInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.ticketPriceInCustomerCurrency - Double.parseDouble(ticketPriceInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Ticket Price in Customer Currency Order Detail 1 is Matched", isticketPriceInCustomerCurrencyMatch1, is(true));

        step("Verify Baggage Price in Customer Currency Order Detail 1 is Match");
        boolean isbaggagePriceInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.baggagePriceInCustomerCurrency - Double.parseDouble(baggagePriceInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Baggage Price in Customer Currency Order Detail 1 is Matched", isbaggagePriceInCustomerCurrencyMatch1, is(true));

        step("Verify Meals Price in Customer Currency Order Detail 1 is Match");
        boolean ismealPriceInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.mealPriceInCustomerCurrency - Double.parseDouble(mealPriceInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Meals Price in Customer Currency Order Detail 1 is Matched", ismealPriceInCustomerCurrencyMatch1, is(true));

        step("Verify Seat Price in Customer Currency Order Detail 1 is Match");
        boolean isseatPriceInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.seatPriceInCustomerCurrency - Double.parseDouble(seatPriceInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Verify Seat Price in Customer Currency Order Detail 1 is Matched", isseatPriceInCustomerCurrencyMatch1, is(true));

        step("Base Fare in Report Currency Order Detail 1 is Match");
        boolean isbaseFareInReportCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.baseFareInReportCurrency - Double.parseDouble(baseFareInReportCurrency)) < tolerance;
        Assertion.assertThat("Base Fare in Report Currency Order Detail 1 is Matched", isbaseFareInReportCurrencyMatch1, is(true));

        step("Settlement Amount in Vendor Currency Order Detail 1 is Match");
        boolean issettlementAmountMatch1 = Math.abs(b2CDailyDetailDataOrder1.settlementAmountInVendorCurrency - Double.parseDouble(settlementAmountInVendorCurrency)) < tolerance;
        Assertion.assertThat("Settlement Amount in Vendor Currency Order Detail 1 is Matched", issettlementAmountMatch1, is(true));

        step("Verify Settlement Amount in Customer Currency Order Detail 1 is Match");
        boolean issettlementAmountInCustomerCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.settlementAmountInCustomerCurrency - Double.parseDouble(settlementAmountInCustomerCurrency)) < tolerance;
        Assertion.assertThat("Settlement Amount in Customer Currency Order Detail 1 is Matched", issettlementAmountInCustomerCurrencyMatch1, is(true));

        step("Verify Settlement Amount in Report Currency Order Detail 1 is Match");
        boolean issettlementAmountInReportCurrencyMatch1 = Math.abs(b2CDailyDetailDataOrder1.settlementAmountInReportCurrency - Double.parseDouble(settlementAmountInReportCurrency)) < tolerance;
        Assertion.assertThat("Settlement Amount in Report Currency Order Detail 1 is Matched", issettlementAmountInReportCurrencyMatch1, is(true));

        step("Verify VAT Order Detail 1 is Match");
        boolean isvatMatch1 = Math.abs(b2CDailyDetailDataOrder1.vat - Double.parseDouble(vat)) < tolerance;
        Assertion.assertThat("VAT Order Detail 1 is Matched", isvatMatch1, is(true));

        step("Verify Withholding Order Detail 1 is Match");
        boolean iswithholdingTaxMatch1 = Math.abs(b2CDailyDetailDataOrder1.withholdingTax - Double.parseDouble(withholdingTax)) < tolerance;
        Assertion.assertThat("Withholding Order Detail 1 is Matched", iswithholdingTaxMatch1, is(true));

        step("Verify VAT on Total Mark-Up + Commission Order Detail 1 is Match");
        boolean isvatValueMatch1 = Math.abs(b2CDailyDetailDataOrder1.vatValue - Double.parseDouble(vatValue)) < tolerance;
        Assertion.assertThat("VAT on Total Mark-Up + Commission Order Detail 1 is Matched", isvatValueMatch1, is(true));

        step("Verify Withholding Tax Value Order Detail 1 is Match");
        boolean iswithholdingTaxValueMatch1 = Math.abs(b2CDailyDetailDataOrder1.withholdingTaxValue - Double.parseDouble(withholdingTaxValue)) < tolerance;
        Assertion.assertThat("Withholding Tax Value Order Detail 1 is Matched", iswithholdingTaxValueMatch1, is(true));

        step("Verify Calculation Deposit Deduction Amount Order Detail 2 (flight only)");
        double endDepositBalanceExpectedOrderDetail1 = financeDashboardPage.calculateB2COrder(b2CDailyDetailDataOrder1.settlementType, Double.parseDouble(settlementAmountInVendorCurrency), b2CDailyDetailDataOrder1.startDepositBalance);

        step("Verify Deduction on Order Detail 2 is Correct (flight only)");
        Assertion.assertThat("Calculation Order Detail 1 is Correct", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.endDepositBalance,endDepositBalanceExpectedOrderDetail1), is(true));

    }

    @Author(names = {"Brian"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testB2CSingleOrderFerry() throws Exception {
        Map<String, String> param = getParam("testB2CSingleOrderFerry");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String vertical = param.get("vertical");
        String kafkaBrokers = param.get("kafkaBrokers");
        String kafkaTopic = param.get("kafkaTopic");
        String kafkaPort = param.get("kafkaPort");
        String searchDateBy = param.get("searchDateBy");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String verticalTitle = param.get("verticalTitle");
        String softblockId = param.get("softblockId");
        String orderId = param.get("orderId");
        String vendorId = param.get("vendorId");
        String orderDetailId = param.get("orderDetailId");
        String source = param.get("source");
        String basicPrice = param.get("basicPrice");
        String markupValue = param.get("markupValue");
        String markupPercentageValue = param.get("markupPercentageValue");
        String markupTotal = param.get("markupTotal");
        String netMarkup = param.get("netMarkup");
        String convenienceFee = param.get("convenienceFee");
        String netMargin = param.get("netMargin");
        String profit = param.get("profit");
        String publishedFare = param.get("publishedFare");
        String customerCurrency = param.get("customerCurrency");
        String vendorCurrency = param.get("vendorCurrency");
        String customerFinalPriceInVendorCurrency = param.get("customerFinalPriceInVendorCurrency");
        String settlementAmountInVendorCurrency = param.get("settlementAmountInVendorCurrency");
        String vat = param.get("vat");
        String withholdingTax = param.get("withholdingTax");
        String vatValue = param.get("vatValue");
        String withholdingTaxValue = param.get("withholdingTaxValue");
        String totalTax = param.get("totalTax");
        String deductionDeposit = param.get("deductionDeposit");

        double tolerance = 0.0000000000000001;

        step("Delete Order ID");
        financeDashboardPage.deleteOrderId(softblockId, orderId);

        step("Hit Cron to Clear Lock Transaction");
        financeDashboardPage.getClearLockTransaction();

        step("Publish Kafka 1 Order Detail");
        financeDashboardPage.kafkaPublishFerryOrderDetail(kafkaBrokers, kafkaTopic, kafkaPort);

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Open Commission Dashboard Accordion Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("Go To Ferry Menu");
        financeDashboardPage.goToFerryMenu();

        step("Filter by Vendor ID and Search");
        financeDashboardPage.searchVendorId(vendorId);
        financeDashboardPage.clickSearchButtonVendorListingB2C();

        step("Go to Daily Report");
        financeDashboardPage.clickMoreButton();
        financeDashboardPage.clickTransactionsButton();
        step("Get The Title of Vertical");
        String dailyVerticalTitleFromDashboard = financeDashboardPage.getDailyVerticalTitleOfFerry();

        step("Verify The Title of Vertical is Correct");
        Assertion.assertThat("Entity is Matched", financeDashboardPage.verifyEqualString(dailyVerticalTitleFromDashboard, verticalTitle), is(true));

        step("Verify Landing on Tab Daily Report");
        Assertion.assertThat("Daily tab is loaded", financeDashboardPage.isDailyTabLoaded(), is(true));

        step("Get The Entries in Daily Report Page");
        B2CDailyDetailOrder b2CDailyDetailOrder = financeDashboardPage.getB2CDailyDetailOrder(vertical, softblockId, searchDateBy, startDate, endDate);

        step("Get the Daily Data");
        B2CDailyDetailOrder.Data b2CDailyDataOrder = b2CDailyDetailOrder.data.get(0);

        step("Get the Daily Data Detail");
        B2CDailyDetailOrder.Detail b2CDailyDetailDataOrder1 = b2CDailyDataOrder.detail.get(0);

        step("Get the Daily Data Detail Items 1");
        B2CDailyDetailOrder.Detail.Items b2CDailyDetail1Items1 = b2CDailyDetailDataOrder1.items.get(0);

        step("Verify Vendor ID Order Detail  is Match");
        Assertion.assertThat("Vendor ID is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.vendorId, vendorId), is(true));

        step("Verify Order ID is Match");
        Assertion.assertThat("Order ID is Matched", financeDashboardPage.verifyEqual(b2CDailyDataOrder.orderId, orderId), is(true));

        step("Verify Order Detail ID  is Match");
        Assertion.assertThat("Order Detail ID  is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.orderDetailId, orderDetailId), is(true));

        step("Verify Source Order Detail  is Match");
        Assertion.assertThat("Source Order Detail  is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.source, source), is(true));

        step("Verify Basic Price (Vendor) Order Detail  is Match");
        boolean isBasicPriceMatch = Math.abs(b2CDailyDetailDataOrder1.basicPrice - Double.parseDouble(basicPrice)) < tolerance;
        Assertion.assertThat("Basic Price Order Detail  is Matched", isBasicPriceMatch, is(true));

        step("Verify Mark-Up Value Order Detail  is Match");
        boolean ismarkupValueMatch = Math.abs(b2CDailyDetailDataOrder1.markupValue - Double.parseDouble(markupValue)) < tolerance;
        Assertion.assertThat("Mark-Up Value Order Detail  is Matched", ismarkupValueMatch, is(true));

        step("Verify Mark-Up Value from Mark-Up Percentage Order Detail  is Match");
        boolean iscommissionPercentageValueMatch = Math.abs(b2CDailyDetail1Items1.markupPercentageValue - Double.parseDouble(markupPercentageValue)) < tolerance;
        Assertion.assertThat("Mark-Up Value from Mark-Up Percentage Order Detail  is Matched", iscommissionPercentageValueMatch, is(true));

        step("Verify Total Mark-Up Value (DPP) Order Detail  is Match");
        boolean ismarkupTotalMatch = Math.abs(b2CDailyDetailDataOrder1.markupTotal - Double.parseDouble(markupTotal)) < tolerance;
        Assertion.assertThat("Total Mark-Up Value (DPP) Order Detail  is Matched", ismarkupTotalMatch, is(true));

        step("Verify Net Mark-Up + Commission Order Detail  is Match");
        boolean isnetMarkupMatch = Math.abs(b2CDailyDetailDataOrder1.netMarkup - Double.parseDouble(netMarkup)) < tolerance;
        Assertion.assertThat("Net Mark-Up + Commission Order Detail  is Matched", isnetMarkupMatch, is(true));

        step("Verify Tiket Convenience Fee / Platform Fee is Match");
        boolean isconvenienceFeeMatch = Math.abs(b2CDailyDataOrder.convenienceFee - Double.parseDouble(convenienceFee)) < tolerance;
        Assertion.assertThat("Tiket Convenience Fee / Platform Fee is Matched", isconvenienceFeeMatch, is(true));

        step("Verify Net Margin is Match");
        boolean isnetMarginMatch = Math.abs(b2CDailyDataOrder.netMargin - Double.parseDouble(netMargin)) < tolerance;
        Assertion.assertThat("Net Margin is Matched", isnetMarginMatch, is(true));

        step("Verify Profit is Match");
        boolean isprofitMatch = Math.abs(b2CDailyDataOrder.profit - Double.parseDouble(profit)) < tolerance;
        Assertion.assertThat("Profit in Customer Currency Order Detail  is Matched", isprofitMatch, is(true));

        step("Verify Published Fare (Tiket Price) Order Detail  is Match");
        boolean ispublishedFareMatch = Math.abs(b2CDailyDetailDataOrder1.publishedFare - Double.parseDouble(publishedFare)) < tolerance;
        Assertion.assertThat("Published Fare (Tiket Price) Order Detail  is Matched", ispublishedFareMatch, is(true));

        step("Verify Customer Currency is Match");
        Assertion.assertThat("Customer Currency is Matched", financeDashboardPage.verifyEqual(b2CDailyDataOrder.customerCurrency, customerCurrency), is(true));

        step("Verify Vendor Currency Order Detail  is Match");
        Assertion.assertThat("Vendor Currency Detail  is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.vendorCurrency, vendorCurrency), is(true));

        step("Verify Customer Final Price in Vendor Currency Order Detail  is Match");
        boolean iscustomerFinalPriceInVendorCurrencyMatch2 = Math.abs(b2CDailyDataOrder.customerFinalPriceInVendorCurrency - Double.parseDouble(customerFinalPriceInVendorCurrency)) < tolerance;
        Assertion.assertThat("Customer Final Price in Vendor Currency Order Detail  is Matched", iscustomerFinalPriceInVendorCurrencyMatch2, is(true));

        step("Settlement Amount in Vendor Currency Order Detail  is Match");
        boolean issettlementAmountMatch = Math.abs(b2CDailyDetailDataOrder1.settlementAmountInVendorCurrency - Double.parseDouble(settlementAmountInVendorCurrency)) < tolerance;
        Assertion.assertThat("Settlement Amount in Vendor Currency Order Detail  is Matched", issettlementAmountMatch, is(true));

        step("Verify VAT Order Detail  is Match");
        boolean isvatMatch = Math.abs(b2CDailyDetailDataOrder1.vat - Double.parseDouble(vat)) < tolerance;
        Assertion.assertThat("VAT Order Detail  is Matched", isvatMatch, is(true));

        step("Verify Withholding Order Detail  is Match");
        boolean iswithholdingTaxMatch = Math.abs(b2CDailyDetailDataOrder1.withholdingTax - Double.parseDouble(withholdingTax)) < tolerance;
        Assertion.assertThat("Withholding Order Detail  is Matched", iswithholdingTaxMatch, is(true));

        step("Verify VAT on Total Mark-Up + Commission Order Detail  is Match");
        boolean isvatValueMatch = Math.abs(b2CDailyDetailDataOrder1.vatValue - Double.parseDouble(vatValue)) < tolerance;
        Assertion.assertThat("VAT on Total Mark-Up + Commission Order Detail  is Matched", isvatValueMatch, is(true));

        step("Verify Withholding Tax Value Order Detail is Match");
        boolean iswithholdingTaxValueMatch = Math.abs(b2CDailyDetailDataOrder1.withholdingTaxValue - Double.parseDouble(withholdingTaxValue)) < tolerance;
        Assertion.assertThat("Withholding Tax Value Order Detail  is Matched", iswithholdingTaxValueMatch, is(true));

        step("Verify Total Tax on Order Level is Match");
        boolean istotalTaxMatch = Math.abs(b2CDailyDataOrder.totalTax - Double.parseDouble(totalTax)) < tolerance;
        Assertion.assertThat("Total Tax on Order Level is Matched", istotalTaxMatch, is(true));

        step("Verify Deposit Deduction Amount Order Detail  is Match");
        boolean isdeductionDepositMatch = Math.abs(b2CDailyDetailDataOrder1.deductionDeposit - Double.parseDouble(deductionDeposit)) < tolerance;
        Assertion.assertThat("Deposit Deduction Amount Order Detail  is Matched", isdeductionDepositMatch, is(true));

        step("Verify Calculation Deposit Deduction Amount Order Detail 1");
        double endDepositBalanceExpectedOrderDetail1 = financeDashboardPage.calculateB2COrder(b2CDailyDetailDataOrder1.settlementType, Double.parseDouble(settlementAmountInVendorCurrency), b2CDailyDetailDataOrder1.startDepositBalance);

        step("Verify Deduction on Order Detail 1 is Correct");
        Assertion.assertThat("Calculation Order Detail 1 is Correct", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.endDepositBalance,endDepositBalanceExpectedOrderDetail1), is(true));

    }

    @Author(names = {"Brian"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testB2CSingleOrderBus() throws Exception {
        Map<String, String> param = getParam("testB2CSingleOrderBus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String vertical = param.get("vertical");
        String kafkaBrokers = param.get("kafkaBrokers");
        String kafkaTopic = param.get("kafkaTopic");
        String kafkaPort = param.get("kafkaPort");
        String searchDateBy = param.get("searchDateBy");
        String verticalTitle = param.get("verticalTitle");
        String softblockId = param.get("softblockId");
        String orderId = param.get("orderId");
        String vendorId = param.get("vendorId");
        String orderDetailId = param.get("orderDetailId");
        String source = param.get("source");
        String basicPrice = param.get("basicPrice");
        String commissionPercentage = param.get("commissionPercentage");
        String commissionPercentageValue = param.get("commissionPercentageValue");
        String commissionTotal = param.get("commissionTotal");
        String netMarkup = param.get("netMarkup");
        String netMargin = param.get("netMargin");
        String dynamicPricingRevenue = param.get("dynamicPricingRevenue");
        String dynamicPricingCost = param.get("dynamicPricingCost");
        String profit = param.get("profit");
        String publishedFare = param.get("publishedFare");
        String customerCurrency = param.get("customerCurrency");
        String vendorCurrency = param.get("vendorCurrency");
        String customerFinalPriceInVendorCurrency = param.get("customerFinalPriceInVendorCurrency");
        String settlementAmountInVendorCurrency = param.get("settlementAmountInVendorCurrency");
        String vat = param.get("vat");
        String withholdingTax = param.get("withholdingTax");
        String vatValue = param.get("vatValue");
        String withholdingTaxValue = param.get("withholdingTaxValue");
        String totalTax = param.get("totalTax");
        String deductionDeposit = param.get("deductionDeposit");
        String toleranceStr = param.get("tolerance");
        double tolerance = Double.parseDouble(toleranceStr);


        step("Delete Order ID");
        financeDashboardPage.deleteOrderId(softblockId, orderId);

        step("Hit Cron to Clear Lock Transaction");
        financeDashboardPage.getClearLockTransaction();

        step("Publish Kafka 1 Order Detail");
        financeDashboardPage.kafkaPublishBusOrderDetail(kafkaBrokers, kafkaPort, kafkaTopic);

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Open Commission Dashboard Accordion Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("Go To Bus & Shuttle Menu");
        financeDashboardPage.goToBusMenu();

        step("Filter by Vendor ID and Search");
        financeDashboardPage.searchVendorId(vendorId);
        financeDashboardPage.clickSearchButtonVendorListingB2C();

        step("Go to Daily Report");
        financeDashboardPage.clickMoreButton();
        financeDashboardPage.clickTransactionsButton();
        step("Get The Title of Vertical");
        String dailyVerticalTitleFromDashboard = financeDashboardPage.getDailyVerticalTitleOfBus();

        step("Verify The Title of Vertical is Correct");
        Assertion.assertThat("Entity is Matched", financeDashboardPage.verifyEqualString(dailyVerticalTitleFromDashboard, verticalTitle), is(true));

        step("Verify Landing on Tab Daily Report");
        Assertion.assertThat("Daily tab is loaded", financeDashboardPage.isDailyTabLoaded(), is(true));

        step("Get The Entries in Daily Report Page");
        String dailyReportDate = CoreFunctions.getJakartaDateYYYY_MM_DD(0);
        B2CDailyDetailOrder b2CDailyDetailOrder = financeDashboardPage.getB2CDailyDetailOrder(
                vertical, softblockId, searchDateBy, dailyReportDate, dailyReportDate);

        step("Get the Daily Data");
        B2CDailyDetailOrder.Data b2CDailyDataOrder = b2CDailyDetailOrder.data.get(0);

        step("Get the Daily Data Detail");
        B2CDailyDetailOrder.Detail b2CDailyDetailDataOrder1 = b2CDailyDataOrder.detail.get(0);

        step("Verify Vendor ID Order Detail  is Match");
        Assertion.assertThat("Vendor ID is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.vendorId, vendorId), is(true));

        step("Verify Order ID is Match");
        Assertion.assertThat("Order ID is Matched", financeDashboardPage.verifyEqual(b2CDailyDataOrder.orderId, orderId), is(true));

        step("Verify Order Detail ID  is Match");
        Assertion.assertThat("Order Detail ID  is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.orderDetailId, orderDetailId), is(true));

        step("Verify Source Order Detail  is Match");
        Assertion.assertThat("Source Order Detail  is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.source, source), is(true));

        step("Verify Basic Price (Vendor) Order Detail  is Match");
        boolean isBasicPriceMatch = Math.abs(b2CDailyDetailDataOrder1.basicPrice - Double.parseDouble(basicPrice)) < tolerance;
        Assertion.assertThat("Basic Price Order Detail  is Matched", isBasicPriceMatch, is(true));

        step("Verify Commission Percentage Order Detail  is Match");
        boolean iscommissionPercentageMatch = Math.abs(b2CDailyDetailDataOrder1.commissionPercentage - Double.parseDouble(commissionPercentage)) < tolerance;
        Assertion.assertThat("Commission Percentage Order Detail  is Matched", iscommissionPercentageMatch, is(true));

        step("Verify Commission Percentage Value Order Detail  is Match");
        boolean iscommissionPercentageValueMatch = Math.abs(b2CDailyDetailDataOrder1.commissionPercentageValue - Double.parseDouble(commissionPercentageValue)) < tolerance;
        Assertion.assertThat("Commission Percentage Value Order Detail  is Matched", iscommissionPercentageValueMatch, is(true));

        step("Verify Commission Value Order Detail  is Match");
        boolean iscommissionValueMatch = Math.abs(b2CDailyDetailDataOrder1.commissionTotal - Double.parseDouble(commissionTotal)) < tolerance;
        Assertion.assertThat("Commission Value Order Detail  is Matched", iscommissionValueMatch, is(true));

        step("Verify Net Mark-Up + Commission Order Detail  is Match");
        boolean isnetMarkupMatch = Math.abs(b2CDailyDetailDataOrder1.netMarkup - Double.parseDouble(netMarkup)) < tolerance;
        Assertion.assertThat("Net Mark-Up + Commission Order Detail  is Matched", isnetMarkupMatch, is(true));

        step("Verify Net Margin is Match");
        boolean isnetMarginMatch = Math.abs(b2CDailyDataOrder.netMargin - Double.parseDouble(netMargin)) < tolerance;
        Assertion.assertThat("Net Margin is Matched", isnetMarginMatch, is(true));

        step("Verify Dynamic Pricing: Revenue Order Detail  is Match");
        boolean isdynamicPricingRevenueMatch = Math.abs(b2CDailyDetailDataOrder1.dynamicPricingRevenue - Double.parseDouble(dynamicPricingRevenue)) < tolerance;
        Assertion.assertThat("Dynamic Pricing: Revenue Order Detail  is Matched", isdynamicPricingRevenueMatch, is(true));

        step("Verify Dynamic Pricing: Cost Order Detail  is Match");
        boolean isdynamicPricingCostMatch = Math.abs(b2CDailyDetailDataOrder1.dynamicPricingCost - Double.parseDouble(dynamicPricingCost)) < tolerance;
        Assertion.assertThat("Dynamic Pricing: Cost Order Detail  is Matched", isdynamicPricingCostMatch, is(true));

        step("Verify Profit is Match");
        boolean isprofitMatch = Math.abs(b2CDailyDataOrder.profit - Double.parseDouble(profit)) < tolerance;
        Assertion.assertThat("Profit in Customer Currency Order Detail  is Matched", isprofitMatch, is(true));

        step("Verify Published Fare (Tiket Price) Order Detail  is Match");
        boolean ispublishedFareMatch = Math.abs(b2CDailyDetailDataOrder1.publishedFare - Double.parseDouble(publishedFare)) < tolerance;
        Assertion.assertThat("Published Fare (Tiket Price) Order Detail  is Matched", ispublishedFareMatch, is(true));

        step("Verify Customer Currency is Match");
        Assertion.assertThat("Customer Currency is Matched", financeDashboardPage.verifyEqual(b2CDailyDataOrder.customerCurrency, customerCurrency), is(true));

        step("Verify Vendor Currency Order Detail  is Match");
        Assertion.assertThat("Vendor Currency Detail  is Matched", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.vendorCurrency, vendorCurrency), is(true));

        step("Verify Customer Final Price in Vendor Currency Order Detail  is Match");
        boolean iscustomerFinalPriceInVendorCurrencyMatch2 = Math.abs(b2CDailyDataOrder.customerFinalPriceInVendorCurrency - Double.parseDouble(customerFinalPriceInVendorCurrency)) < tolerance;
        Assertion.assertThat("Customer Final Price in Vendor Currency Order Detail  is Matched", iscustomerFinalPriceInVendorCurrencyMatch2, is(true));

        step("Settlement Amount in Vendor Currency Order Detail  is Match");
        boolean issettlementAmountMatch = Math.abs(b2CDailyDetailDataOrder1.settlementAmountInVendorCurrency - Double.parseDouble(settlementAmountInVendorCurrency)) < tolerance;
        Assertion.assertThat("Settlement Amount in Vendor Currency Order Detail  is Matched", issettlementAmountMatch, is(true));

        step("Verify VAT Order Detail  is Match");
        boolean isvatMatch = Math.abs(b2CDailyDetailDataOrder1.vat - Double.parseDouble(vat)) < tolerance;
        Assertion.assertThat("VAT Order Detail  is Matched", isvatMatch, is(true));

        step("Verify Withholding Order Detail  is Match");
        boolean iswithholdingTaxMatch = Math.abs(b2CDailyDetailDataOrder1.withholdingTax - Double.parseDouble(withholdingTax)) < tolerance;
        Assertion.assertThat("Withholding Order Detail  is Matched", iswithholdingTaxMatch, is(true));

        step("Verify VAT on Total Mark-Up + Commission Order Detail  is Match");
        boolean isvatValueMatch = Math.abs(b2CDailyDetailDataOrder1.vatValue - Double.parseDouble(vatValue)) < tolerance;
        Assertion.assertThat("VAT on Total Mark-Up + Commission Order Detail  is Matched", isvatValueMatch, is(true));

        step("Verify Withholding Tax Value Order Detail is Match");
        boolean iswithholdingTaxValueMatch = Math.abs(b2CDailyDetailDataOrder1.withholdingTaxValue - Double.parseDouble(withholdingTaxValue)) < tolerance;
        Assertion.assertThat("Withholding Tax Value Order Detail  is Matched", iswithholdingTaxValueMatch, is(true));

        step("Verify Total Tax on Order Level is Match");
        boolean istotalTaxMatch = Math.abs(b2CDailyDataOrder.totalTax - Double.parseDouble(totalTax)) < tolerance;
        Assertion.assertThat("Total Tax on Order Level is Matched", istotalTaxMatch, is(true));

        step("Verify Deposit Deduction Amount Order Detail  is Match");
        boolean isdeductionDepositMatch = Math.abs(b2CDailyDetailDataOrder1.deductionDeposit - Double.parseDouble(deductionDeposit)) < tolerance;
        Assertion.assertThat("Deposit Deduction Amount Order Detail  is Matched", isdeductionDepositMatch, is(true));

        step("Verify Calculation Deposit Deduction Amount Order Detail 1");
        double endDepositBalanceExpectedOrderDetail1 = financeDashboardPage.calculateB2COrder(b2CDailyDetailDataOrder1.settlementType, Double.parseDouble(settlementAmountInVendorCurrency), b2CDailyDetailDataOrder1.startDepositBalance);

        step("Verify Deduction on Order Detail 1 is Correct");
        Assertion.assertThat("Calculation Order Detail 1 is Correct", financeDashboardPage.verifyEqual(b2CDailyDetailDataOrder1.endDepositBalance,endDepositBalanceExpectedOrderDetail1), is(true));

    }

    @Author(names = {"Brian"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testEditConfigBus() throws Exception {
        Map<String, String> param = getParam("testEditConfigBus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String vendorId = param.get("vendorId");
        String vat = param.get("vat");
        String wht = param.get("wht");
        String verticalTitle = param.get("verticalTitle");

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Open Commission Dashboard Accordion Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("Go To Bus & Shuttle Menu");
        financeDashboardPage.goToBusMenu();

        step("Get The Title of Vertical");
        String dailyVerticalTitleFromDashboard = financeDashboardPage.getDailyVerticalTitleOfBus();

        step("Verify The Title of Vertical is Correct");
        Assertion.assertThat("Entity is Matched", financeDashboardPage.verifyEqualString(dailyVerticalTitleFromDashboard, verticalTitle), is(true));

        step("Filter by Vendor ID and Search");
        financeDashboardPage.searchVendorId(vendorId);
        financeDashboardPage.clickSearchButtonVendorListingB2C();

        step("Go to Edit Configuration Page");
        financeDashboardPage.clickMoreButton();
        financeDashboardPage.clickEditConfigurationButton();

        step("Input VAT");
        financeDashboardPage.inputVATEditConfigurationPage(vat);
        financeDashboardPage.blurActiveElement();

        step("Input WHT");
        financeDashboardPage.inputWHTEditConfigurationPage(wht);
        financeDashboardPage.blurActiveElement();

        step("Click Save Button on Edit Configuration Page");
        financeDashboardPage.clickEditConfigurationSaveButton();

        step("Verify Snackbar Edit Configuration Page is Success");
        Assertion.assertThat("Snackbar is Appeared", financeDashboardPage.isEditConfigurationPageSuccess(), is(true));

    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Reseller")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testAddTransactionB2BReseller() {
        Map<String, String> param = getParam("testAddTransactionB2BReseller");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);
        B2BResellerFinanceDashboardPageWeb<?> b2BResellerFinanceDashboardPage = B2BResellerFinanceDashboardPageWeb.getInstance(session);
        B2BResellerFinanceDashboardDetailPageWeb<?> b2BResellerFinanceDashboardDetailPage = B2BResellerFinanceDashboardDetailPageWeb.getInstance(session);
        ResellerRequestPageWeb<?> resellerRequestPage = ResellerRequestPageWeb.getInstance(session);

        String username = param.get("email");
        String password = param.get("password");
        String id = param.get("id");
        String amount = param.get("amount");
        String remarks = param.get("remarks");

        step("User open intools dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);

        step("User click on B2B Affiliate Menu");
        financeDashboardPage.clickB2BAffiliateDropdownMenu();

        step("User click on Reseller API Menu");
        financeDashboardPage.goToB2BAResellerMenu();

        step("User click filter by Reseller ID");
        b2BResellerFinanceDashboardPage.inputResellerId(id);

        step("User click search");
        b2BResellerFinanceDashboardPage.clickSearchButton();

        step("User click Action More");
        b2BResellerFinanceDashboardPage.clickFirstAction();

        step("User click Transaction");
        b2BResellerFinanceDashboardPage.clickMoreActionTransactions();

        step("User click add transaction");
        b2BResellerFinanceDashboardDetailPage.clickAddTransactionButton();

        step("User fill required fields and submit");
        long orderId = CoreFunctions.generateRandomInt(8);
        b2BResellerFinanceDashboardDetailPage.addNewTransaction(amount, orderId + "");

        step("User click Request Menu");
        financeDashboardPage.clickRequestDropdownMenu();

        step("User click Reseller");
        financeDashboardPage.clickResellerMenu();

        step("User click Approve on Pending Request Tab");
        resellerRequestPage.clickApproveByResellerId(id);

        step("User fill required fields and click submit");
        resellerRequestPage.approveRequestInModal(remarks);

        step("User verify data already inserted on Request History Tab");
        resellerRequestPage.clickRequestHistoryTab();
        resellerRequestPage.setSearchByToResellerId();
        resellerRequestPage.setResellerId(id);
        resellerRequestPage.clickSearch();

        DecimalFormat formatter = new DecimalFormat("###,###.00");

        Assertion.assertThat("Data inserted on Request History Tab",
                resellerRequestPage.verifyDataInsertedIntoRequestHistory(id, formatter.format(Integer.parseInt(amount)), remarks),
                is(true));

        step("User click on Reseller API Menu");
        financeDashboardPage.goToB2BAResellerMenu();

        step("User click filter by Reseller ID");
        b2BResellerFinanceDashboardPage.inputResellerId(id);

        step("User click search");
        b2BResellerFinanceDashboardPage.clickSearchButton();

        step("User click Action More");
        b2BResellerFinanceDashboardPage.clickFirstAction();

        step("User click Transaction");
        b2BResellerFinanceDashboardPage.clickMoreActionTransactions();

        step("Assertions");
        Assertion.assertThat("Then user verify add transaction data already inserted  & balance added on daily report",
                b2BResellerFinanceDashboardDetailPage.verifyAddTransactionDataAlreadyInserted(id, formatter.format(Integer.parseInt(amount))),
                is(true));

        step("Delete the newly created Data");
        String configId = b2BResellerFinanceDashboardPage.getCurrentConfigId();
        JSONObject response = financeDashboardPage.deleteOrderIdApiB2b(configId, orderId + "");
        Assertion.assertThat("Sucess deleting data", response.getInt("code"), is(200));

        step("Logout");
        financeDashboardPage.logout();
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Flight International")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void addTransactionFlightInternational() {
        Map<String, String> param = getParam("addTransactionFlightInternational");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);
        FlightInternationalPageWeb<?> flightInternationalPage = FlightInternationalPageWeb.getInstance(session);

        String username = param.get("email");
        String password = param.get("password");
        String vendorId = param.get("vendorId");
        String amount = param.get("amount");

        step("User open Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);

        step("User click On Commission Dashboard Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();
        financeDashboardPage.goToFlightInternationalMenu();

        step("User click filter by Vendor ID: tiketcomPKFare");
        flightInternationalPage.inputVendorIdFilter(vendorId);

        step("User click search");
        flightInternationalPage.clickSearchButton();

        step("User click Action More");
        flightInternationalPage.clickMoreButtonWithVendorId(vendorId);

        step("User click Transaction");
        flightInternationalPage.clickMoreTransactionsButton();

        step("User click Add Transaction on Daily Report");
        flightInternationalPage.clickAddTransactionButton();

        step("User fill all the required fields");
        String orderId = CoreFunctions.generateRandomInt(8) + "";
        flightInternationalPage.inputAddTransactionOrderIdInput(orderId);
        flightInternationalPage.inputAddTransactionAmountInput(amount);

        step("User submit");
        flightInternationalPage.saveTransactionModal();

        step("Assertions");
        Assertion.assertThat("User verify add transaction row & balance added on daily report And Balance",
                flightInternationalPage.validateRowTransactionWithOrderId(orderId, vendorId, amount),
                is(true));

        step("Delete newly created data");
        String configId = flightInternationalPage.getConfigIdFromUrl();
        DeleteOrderId response = financeDashboardPage.deleteOrderId(configId, orderId);
        Assertion.assertThat("Created data sucessfully deleted", response.code, is("200"));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testOrderB2BResellerOneWaySinglePax() throws Exception {
        Map<String, String> param = getParam("testOrderB2BResellerOneWaySinglePax");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);
        B2BResellerFinanceDashboardPageWeb<?> b2BResellerFinanceDashboardPage = B2BResellerFinanceDashboardPageWeb.getInstance(session);

        String username = param.get("email");
        String password = param.get("password");
        String id = param.get("id");
        String transactionId = param.get("transactionId");
        String orderID = param.get("orderId");
        String orderDetailID = param.get("orderDetailId");
        int baseFare = Integer.parseInt(param.get("baseFare"));
        int tiketCommissionValue = Integer.parseInt(param.get("tiketCommissionValue"));
        int publishedFareToReseller = Integer.parseInt(param.get("publishedFareToReseller"));
        int resellerSubsidyCampaignValue = Integer.parseInt(param.get("resellerSubsidyCampaignValue"));
        int customerFinalPriceInVendorPreferredCurrency =  Integer.parseInt(param.get("customerFinalPriceInVendorPreferredCurrency"));
        int airlineCommission = Integer.parseInt(param.get("airlineCommission"));

        step("Hit apis to publish the order through kafka");
        b2BResellerFinanceDashboardPage.hitPrerequisiteApiToRepublishOrder(transactionId, orderID, orderDetailID, baseFare, tiketCommissionValue,
                publishedFareToReseller, resellerSubsidyCampaignValue, customerFinalPriceInVendorPreferredCurrency, airlineCommission);

        step("User open intools dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);

        step("User click on B2B Affiliate Menu");
        financeDashboardPage.clickB2BAffiliateDropdownMenu();

        step("User click on Reseller API Menu");
        financeDashboardPage.goToB2BAResellerMenu();

        step("User click filter by Reseller ID");
        b2BResellerFinanceDashboardPage.inputResellerId(id);

        step("User click search");
        b2BResellerFinanceDashboardPage.clickSearchButton();

        step("Click Action More");
        financeDashboardPage.clickMoreButton();

        step("User click Transaction");
        b2BResellerFinanceDashboardPage.clickMoreActionTransactions();

        step("Get The Entries in Daily Report Page");
        B2BDailyDetail b2BDailyDetail = financeDashboardPage.getB2BDailyDetailGateway(
                param.get("searchDateBy"), param.get("startDate"), param.get("endDate"), id, orderID);

        step("Get the Daily Data");
        B2BDailyDetail.Data b2BDailyData = b2BDailyDetail.data.get(0);

        step("Get the Daily Data Detail");
        B2BDailyDetail.Detail b2BDailyDetailData = b2BDailyData.details.get(0);

        step("Verify Order ID is Match");
        Assertion.assertThat("Order ID is Matched", financeDashboardPage.verifyEqual(b2BDailyData.orderId, orderID), is(true));

        step("Verify Reseller ID is not null");
        Assertion.assertThat("Verify Reseller ID is not null", b2BDailyData.resellerId, notNullValue());
        Assertion.assertThat("Verify Reseller ID has valid numeric id format", b2BDailyData.resellerId, matchesPattern(API_NUMERIC_ID_FORMAT_REGEX));

        step("Verify Order Status is not null");
        Assertion.assertThat("Verify Order Status is not null", b2BDailyData.orderStatus, notNullValue());

        step("Verify Booking Code is not null");
        Assertion.assertThat("Verify Booking Code is not null", b2BDailyDetailData.bookingCode, notNullValue());

        step("Verify Order Detail ID is Match");
        Assertion.assertThat("Order Detail ID should be > 0", b2BDailyDetailData.orderDetailID, Matchers.greaterThan(0L));
        Assertion.assertThat("Verify Order Detail ID has valid numeric id format", String.valueOf(b2BDailyDetailData.orderDetailID), matchesPattern(API_NUMERIC_ID_FORMAT_REGEX));

        step("Log out");
        financeDashboardPage.logout();
    }

    @Author(names = {"Nishtha"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testOrderB2BResellerRoundTripMultiPax() throws Exception {
        Map<String, String> param = getParam("testOrderB2BResellerRoundTripMultiPax");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);
        B2BResellerFinanceDashboardPageWeb<?> b2BResellerFinanceDashboardPage = B2BResellerFinanceDashboardPageWeb.getInstance(session);

        String username = param.get("email");
        String password = param.get("password");
        String id = param.get("id");
        String transactionId = param.get("transactionId");
        String orderID = param.get("orderId");
        String orderDetailID = param.get("orderDetailId");
        int baseFare = Integer.parseInt(param.get("baseFare"));
        int tiketCommissionValue = Integer.parseInt(param.get("tiketCommissionValue"));
        int publishedFareToReseller = Integer.parseInt(param.get("publishedFareToReseller"));
        int resellerSubsidyCampaignValue = Integer.parseInt(param.get("resellerSubsidyCampaignValue"));
        int customerFinalPriceInVendorPreferredCurrency =  Integer.parseInt(param.get("customerFinalPriceInVendorPreferredCurrency"));
        int airlineCommission = Integer.parseInt(param.get("airlineCommission"));

        step("Hit apis to publish the order through kafka");
        b2BResellerFinanceDashboardPage.hitPrerequisiteApiToRepublishOrder(transactionId, orderID, orderDetailID, baseFare, tiketCommissionValue,
                publishedFareToReseller, resellerSubsidyCampaignValue, customerFinalPriceInVendorPreferredCurrency, airlineCommission);

        step("User open intools dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);

        step("User click on B2B Affiliate Menu");
        financeDashboardPage.clickB2BAffiliateDropdownMenu();

        step("User click on Reseller API Menu");
        financeDashboardPage.goToB2BAResellerMenu();

        step("User click filter by Reseller ID");
        b2BResellerFinanceDashboardPage.inputResellerId(id);

        step("User click search");
        b2BResellerFinanceDashboardPage.clickSearchButton();

        step("Click Action More");
        financeDashboardPage.clickMoreButton();

        step("User click Transaction");
        b2BResellerFinanceDashboardPage.clickMoreActionTransactions();

        step("Get The Entries in Daily Report Page");
        B2BDailyDetail b2BDailyDetail = financeDashboardPage.getB2BDailyDetailGateway(
                param.get("searchDateBy"), param.get("startDate"), param.get("endDate"), id, orderID);

        step("Get the Daily Data");
        B2BDailyDetail.Data b2BDailyData = b2BDailyDetail.data.get(0);

        step("Get the Daily Data Detail");
        B2BDailyDetail.Detail b2BDailyDetailData = b2BDailyData.details.get(0);

        step("Verify Order ID is Match");
        Assertion.assertThat("Order ID is Matched", financeDashboardPage.verifyEqual(b2BDailyData.orderId, orderID), is(true));

        step("Verify Reseller ID is not null");
        Assertion.assertThat("Verify Reseller ID is not null", b2BDailyData.resellerId, notNullValue());
        Assertion.assertThat("Verify Reseller ID has valid numeric id format", b2BDailyData.resellerId, matchesPattern(API_NUMERIC_ID_FORMAT_REGEX));

        step("Verify Order Status is not null");
        Assertion.assertThat("Verify Order Status is not null", b2BDailyData.orderStatus, notNullValue());

        step("Verify Booking Code is not null");
        Assertion.assertThat("Verify Booking Code is not null", b2BDailyDetailData.bookingCode, notNullValue());

        step("Verify Order Detail ID is Match");
        Assertion.assertThat("Verify Order Detail ID is not null", String.valueOf(b2BDailyDetailData.orderDetailID), notNullValue());
        Assertion.assertThat("Verify Order Detail ID has valid numeric id format", String.valueOf(b2BDailyDetailData.orderDetailID), matchesPattern(API_NUMERIC_ID_FORMAT_REGEX));

        step("Log out");
        financeDashboardPage.logout();
    }

    @Author(names = {"Akshat"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyCreationAndUpdationOfVCC() throws Exception {
        Map<String, String> param = getParam("testVerifyCreationAndUpdationOfVCC");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String purchaseType = param.get("purchaseType");
        String nameOnCard = param.get("nameOnCard");
        String issuedCardType = param.get("issuedCardType");
        String creditLimit = param.get("creditLimit");
        String currencyType = param.get("currencyType");
        String maximumTransactionsAllowed = param.get("maximumTransactionsAllowed");
        String period = param.get("period");
        String updatedCreditLimit = param.get("updatedCreditLimit");

        step("User open intools dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);

        step("User clicks on Settlement Option and select VCC");
        financeDashboardPage.selectVCCOption();

        step("User clicks on add new VCC");
        financeDashboardPage.addNewVCC();

        step("User verifies whether the page is navigated to add new VCC page");
        Assertion.assertThat("User is navigated to add new VCC page", financeDashboardPage.isAddNewVCCPageLoaded(), is(true));

        step("User fills the VCC information");
        financeDashboardPage.fillNewVCCInformation(purchaseType,nameOnCard,issuedCardType);

        step("User fills the VCC details");
        financeDashboardPage.fillNewVCCDetails(creditLimit, currencyType, maximumTransactionsAllowed, period);

        step("User clicks on save button");
        financeDashboardPage.saveVCC();

        step("User verifies whether he is back to VCC listing page");
        Assertion.assertThat("User is navigated back to VCC listing page", financeDashboardPage.isVCCListingPageLoaded(), is(true));

        step("User noted the card number of newly created VCC");
        String cardNumber = financeDashboardPage.getCardNumberOfFirstVCCInList();

        step("User verifies the credit limit of newly created VCC");
        Assertion.assertThat("Credit limit of newly created VCC is correct", financeDashboardPage.getCreditLimitOfFirstVCCInList(creditLimit), is(true));

        step("User clicks on view balance of newly created VCC");
        financeDashboardPage.viewFirstVCCInList();

        step("User verifies whether the page is navigated to view VCC page");
        Assertion.assertThat("User is navigated to view VCC page", financeDashboardPage.isVCCDetailsPageLoaded(), is(true));

        step("User clicks on edit button in view VCC page");
        financeDashboardPage.clickEditButtonInViewVCCPage();

        step("User edits the credit limit");
        financeDashboardPage.updateCreditLimitInEditVCCPage(updatedCreditLimit);

        step("User saves the changes");
        financeDashboardPage.saveVCCAfterUpdation();

        step("User verifies whether he is back to VCC listing page");
        Assertion.assertThat("User is navigated back to VCC listing page", financeDashboardPage.isVCCListingPageLoaded(), is(true));

        step("User verifies whether the updated credit limit is reflected in view VCC page");
        Assertion.assertThat("Updated credit limit is reflected in view VCC page", financeDashboardPage.getUpdatedCreditLimitOfFirstVCCInList(cardNumber,updatedCreditLimit), is(true));

        step("Log out");
        financeDashboardPage.logout();
    }

    @Author(names = {"Akshat"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testEditConfigB2CGTAndVerify() throws Exception {
        Map<String, String> param = getParam("testEditConfigB2CGTAndVerify");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String vendorName = param.get("vendorName");
        String tierOneMinSeatsSold = param.get("tierOneMinSeatsSold");
        String tierOneMaxSeatsSold = param.get("tierOneMaxSeatsSold");
        String tierOneCommission = param.get("tierOneCommission");
        String tierTwoMinSeatsSold = param.get("tierTwoMinSeatsSold");
        String tierTwoMaxSeatsSold = param.get("tierTwoMaxSeatsSold");
        String tierTwoCommission = param.get("tierTwoCommission");
        String tierThreeMinSeatsSold = param.get("tierThreeMinSeatsSold");
        String tierThreeMaxSeatsSold = param.get("tierThreeMaxSeatsSold");
        String tierThreeCommission = param.get("tierThreeCommission");
        String tierFourMinSeatsSold = param.get("tierFourMinSeatsSold");
        String tierFourMaxSeatsSold = param.get("tierFourMaxSeatsSold");
        String tierFourCommission = param.get("tierFourCommission");

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(email, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Open Commission Dashboard Accordion Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("Go to Bus and Shuttle Menu");
        financeDashboardPage.goToBusMenu();

        step("Filter by Vendor Name and Search");
        financeDashboardPage.searchVendorName(vendorName);
        financeDashboardPage.clickSearchButton();

        step("User clicks on more button");
        financeDashboardPage.clickMoreButton();

        step("User clicks on Edit Configuration button");
        financeDashboardPage.clickEditConfigurationButton();

        step("User clicks on Single slab option");
        financeDashboardPage.clickSingleSlab();

        step("User verifies that Tier One Commission appears");
        Assertion.assertThat("Verifies that Tier One Commission appears", financeDashboardPage.isTierOneCommissionAppears(), is(true));

        step("User clicks on Multi slab option");
        financeDashboardPage.clickMultiSlab();

        step("User verifies that Tier One Commission appears");
        Assertion.assertThat("Verifies that Tier One Commission appears", financeDashboardPage.isTierOneCommissionAppears(), is(true));

        step("User clicks on Add Tier Option");
        financeDashboardPage.clickAddTier();

        step("User fills details for Tier Commission one");
        financeDashboardPage.fillTierCommission(1,tierOneMinSeatsSold,tierOneMaxSeatsSold,tierOneCommission);

        step("User fills details for Tier Commission two");
        financeDashboardPage.fillTierCommission(2,tierTwoMinSeatsSold,tierTwoMaxSeatsSold,tierTwoCommission);

        step("User clicks on Add Tier Option");
        financeDashboardPage.clickAddTier();

        step("User fills details for Tier Commission three");
        financeDashboardPage.fillTierCommission(3,tierThreeMinSeatsSold,tierThreeMaxSeatsSold,tierThreeCommission);

        step("User clicks on Add Tier Option");
        financeDashboardPage.clickAddTier();

        step("User fills details for Tier Commission four");
        financeDashboardPage.fillTierCommission(4,tierFourMinSeatsSold,tierFourMaxSeatsSold,tierFourCommission);

        step("User toggles on the Applicable button");
        financeDashboardPage.clickToggleBtn();

        step("User clicks on Save button");
        financeDashboardPage.clickSave();

        step("Filter by Vendor Name and Search");
        financeDashboardPage.searchVendorName(vendorName);
        financeDashboardPage.clickSearchButton();

        step("User clicks on more button");
        financeDashboardPage.clickMoreButton();

        step("User clicks on Edit Configuration button");
        financeDashboardPage.clickEditConfigurationButton();

        step("User verifies that the details for Tier Commission one is correct");
        financeDashboardPage.assertTierCommission(1,tierOneMinSeatsSold,tierOneMaxSeatsSold,tierOneCommission);

        step("User verifies that the details for Tier Commission two is correct");
        financeDashboardPage.assertTierCommission(2,tierTwoMinSeatsSold,tierTwoMaxSeatsSold,tierTwoCommission);

        step("User verifies that the details for Tier Commission three is correct");
        financeDashboardPage.assertTierCommission(3,tierThreeMinSeatsSold,tierThreeMaxSeatsSold,tierThreeCommission);

        step("User verifies that the details for Tier Commission four is correct");
        financeDashboardPage.assertTierCommission(4,tierFourMinSeatsSold,tierFourMaxSeatsSold,tierFourCommission);

        step("User toggles on the Applicable button");
        financeDashboardPage.clickToggleBtn();

        step("User deletes the created tier commissions");
        financeDashboardPage.deleteTiers(4);

        step("User clicks on Save button");
        financeDashboardPage.clickSave();

        step("Logout");
        financeDashboardPage.logout();
    }

    @Author(names = {"Nasha"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testFlightOrderWithVendorThatHaveSubVendorAndVerifyDepositDeductionUsingTheSameDepositWithMainVendorID() throws Exception{
        Map<String, String> param = getParam("testFlightOrderWithVendorThatHaveSubVendorAndVerifyDepositDeductionUsingTheSameDepositWithMainVendorID");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String vendorName = param.get("vendorName");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Open Commission Dashboard Accordion Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("Go To Flight Domestic Menu");
        financeDashboardPage.goToFlightDomesticMenu1();

        step("Filter by Vendor name and Search");
        financeDashboardPage.searchVendorNameV2(vendorName);

        step("Click Search Button");
        financeDashboardPage.clickSearchButton();

        step("verify vendor name is same with vendorName input parameter");
        boolean isVendorNameValid = financeDashboardPage.isVendorNameVisible(vendorName);
        Assertion.assertThat("Vendor name is visible in the table", isVendorNameValid, is(true));

        step("user click More button");
        financeDashboardPage.clickMoreButton();

        step("user click Transaction button");
        financeDashboardPage.clickTransactionsButton();

        step("verify transaction page loaded");
        Assertion.assertThat("View list of transactions by is loaded", financeDashboardPage.isViewListOfTransactionsByLoaded(), is(true));

        step("user filter start date and end date");
        financeDashboardPage.searchDateBy(startDate, endDate);

        step("user click Search button");
        financeDashboardPage.clickSearchButton();

        step("verify there are 3 rows of transaction on filtered date");
        int rowCount = financeDashboardPage.getTransactionRowCount();
        Assertion.assertThat("Row count is 3", rowCount, is(3));

        step("verify deposit calculation first row is correct");
        String startBal = financeDashboardPage.getStartDepositBalanceColumnValue(1);
        String dedAmount = financeDashboardPage.getDepositDeductionAmountColumnValue(1);
        String endBal = financeDashboardPage.getEndDepositBalanceColumnValue(1);

        step("verify for first row : start deposit balance - deposit deduction amount = end deposit balance");
        Assertion.assertThat("Deposit calculation is correct", financeDashboardPage.verifyDeductionIsCorrect(startBal, dedAmount, endBal), is(true));

        step("verify for second row : start deposit balance - deposit deduction amount = end deposit balance");
        String startBal2 = financeDashboardPage.getStartDepositBalanceColumnValue(2);
        String dedAmount2 = financeDashboardPage.getDepositDeductionAmountColumnValue(2);
        String endBal2 = financeDashboardPage.getEndDepositBalanceColumnValue(2);
        Assertion.assertThat("Deposit calculation is correct", financeDashboardPage.verifyDeductionIsCorrect(startBal2, dedAmount2, endBal2), is(true));

        step("verify for third row : start deposit balance - deposit deduction amount = end deposit balance");
        String startBal3 = financeDashboardPage.getStartDepositBalanceColumnValue(3);
        String dedAmount3 = financeDashboardPage.getDepositDeductionAmountColumnValue(3);
        String endBal3 = financeDashboardPage.getEndDepositBalanceColumnValue(3);
        Assertion.assertThat("Deposit calculation is correct", financeDashboardPage.verifyDeductionIsCorrect(startBal3, dedAmount3, endBal3), is(true));

        step("Log out");
        financeDashboardPage.logout();
    }


    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void verifyTopUpAndApproveDepositB2BResellerBLISABDA(){
        Map<String, String> param = getParam("verifyTopUpAndApproveDepositB2BResellerBLISABDA");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);
        B2BResellerFinanceDashboardPageWeb<?> b2BResellerFinanceDashboardPage = B2BResellerFinanceDashboardPageWeb.getInstance(session);
        ResellerRequestPageWeb<?> resellerRequestPage = ResellerRequestPageWeb.getInstance(session);
        String username = param.get("email");
        String password = param.get("password");
        String amount = param.get("amt");
        String requestSnack = param.get("requestSnack");
        String addSnack = param.get("addSnack");

        step("User open intools dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);

        step("User click on B2B Affiliate Menu");
        financeDashboardPage.clickB2BAffiliateDropdownMenu();

        step("User click on Reseller API Menu");
        financeDashboardPage.goToB2BAResellerMenu();

        step("Click Action More");
        financeDashboardPage.clickMoreButton();

        step("User click Transaction and verify transaction page is loaded");
        b2BResellerFinanceDashboardPage.clickMoreActionTransactions();
        b2BResellerFinanceDashboardPage.verifyTransactionPageLoaded();

        step("Click on the 'Add transaction' button");
        b2BResellerFinanceDashboardPage.clickMainAddTransactionButton();

        step("Verify the 'Add Transaction' pop-up appears");
        b2BResellerFinanceDashboardPage.verifyAddTransactionModalAppears();
        String extractedResellerName = b2BResellerFinanceDashboardPage.getResellerNameFromModal();

        step("Enter transaction amount");
        b2BResellerFinanceDashboardPage.enterTransactionAmount(amount);

        step("Select 'Addition' as Type of Amount");
        b2BResellerFinanceDashboardPage.selectAdditionTypeOfAmount();

        step("Click the 'Add transaction' submit button");
        b2BResellerFinanceDashboardPage.clickSubmitTransactionButton();

        step("Verify the success snackbar appears and disappears");
        b2BResellerFinanceDashboardPage.verifyTransactionSuccessSnackbar(addSnack);

        step("Click on Request Menu and Select reseller option");
        financeDashboardPage.clickRequestDropdownMenu();
        financeDashboardPage.clickResellerMenu();

        step("Verify new data is displayed and matches the inputted values");
        resellerRequestPage.verifyNewTransactionInGrid(extractedResellerName, amount);

        step("Click on the Approve button for the newly created transaction (Row 1)");
        resellerRequestPage.clickApproveButtonWithRowIndex(1);

        step("Verify pop-up appears, enter remarks, and click Approve");
        resellerRequestPage.approveRequestInModal("Approved by Automation");

        step("Verify the 'Request approved.' snackbar appears and disappears");
        resellerRequestPage.verifySnackbarMessage(requestSnack);

        step("click Request History Tab");
        resellerRequestPage.clickRequestHistoryTab();

        step("Verify new data is displayed, matches inputted data, and is marked as Approved");
        resellerRequestPage.verifyApprovedTransactionInHistoryGrid(extractedResellerName, amount);

        step("Logout");
        financeDashboardPage.logout();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testOrderFlightDomesticOnewayVendorIDRCustSGDDailyReport() throws Exception {
        Map<String, String> param = getParam("testOrderFlightDomesticOnewayVendorIDRCustSGDDailyReport");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String vendorName = param.get("vendorName");
        String searchBy = param.get("searchBy");
        String orderId = param.get("orderId");
        String reportDay = param.get("reportDay");
        String reportMonth = param.get("reportMonth");
        String vendorCurrency = param.get("vendorCurrency");
        String customerCurrency = param.get("customerCurrency");
        String adultCount = param.get("adultCount");
        String basicPriceBaggage = param.get("basicPriceBaggage");
        String basicPriceMeal = param.get("basicPriceMeal");
        String basicPriceSeat = param.get("basicPriceSeat");

        step("GIVEN: User navigates to CFD dashboard (preprod.tiket.com/admin/intools)");
        financeDashboardPage.goToFinanceDashboard(email, password);
        Assertion.assertThat("CFD dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("WHEN: User clicks Commission Dashboard menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("AND: User clicks Flight (Domestic) menu");
        financeDashboardPage.goToFlightDomesticMenu1();

        step("AND: User filters by Vendor Name");
        financeDashboardPage.searchVendorName(vendorName);

        step("AND: User clicks Search button");
        financeDashboardPage.clickSearchButton();

        step("THEN: Verify the correct vendor is displayed in the first row of the table");
        Assertion.assertThat("Vendor Name in first row matches filter", financeDashboardPage.getFirstRowVendorName(vendorName), containsString(vendorName));

        step("AND: User clicks Action More button");
        financeDashboardPage.clickMoreButton();

        step("AND: User clicks Transaction button");
        financeDashboardPage.clickTransactionsButton();

        step("AND: User selects '" + searchBy + "' from Search by dropdown");
        financeDashboardPage.selectSearchByOption(searchBy);

        step("AND: User enters Order ID in search field");
        financeDashboardPage.enterTransactionSearchOrderId(orderId);

        step("AND: User opens start date calendar and selects date");
        financeDashboardPage.clickStartDateCalendar();
        financeDashboardPage.selectCalendarDay(reportMonth, reportDay);

        step("AND: User clicks Search button");
        financeDashboardPage.clickSearchButton();

        step("AND: User verify Order ID is already correct");
        Assertion.assertThat("Order ID is correct", financeDashboardPage.getTxRow2OrderId(orderId), is(orderId));

        step("AND: User verify vendor currency is IDR");
        Assertion.assertThat("Vendor currency is IDR", financeDashboardPage.getTxRow1VendorCurrency(), is(vendorCurrency));

        step("AND: User verify customer currency is SGD");
        Assertion.assertThat("Customer currency is SGD", financeDashboardPage.getTxRow1CustomerCurrency(), is(customerCurrency));

        step("AND: User verify count of adult is 1");
        Assertion.assertThat("Count of adult is 1", financeDashboardPage.getTxRow1CountOfAdult(), is(adultCount));

        step("AND: User verify basic price baggage is 0.00");
        Assertion.assertThat("Basic price baggage is 0.00", financeDashboardPage.getTxRow1BasicPriceBaggage(), containsString(basicPriceBaggage));

        step("AND: User verify basic price meal is 0.00");
        Assertion.assertThat("Basic price meal is 0.00", financeDashboardPage.getTxRow1BasicPriceMeals(), containsString(basicPriceMeal));

        step("AND: User verify basic price seat is 0.00");
        Assertion.assertThat("Basic price seat is 0.00", financeDashboardPage.getTxRow1BasicPriceSeat(), containsString(basicPriceSeat));

        step("AND: User clicks Download button");
        financeDashboardPage.clickDownloadButton();

        step("THEN: Verify snackbar shows 'Successfully download daily report'");
        Assertion.assertThat("Download snackbar message is correct", financeDashboardPage.getDownloadSnackbarText(), is("Successfully download daily report"));
    }

    @Author(names = {"Nasha"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testOrderRoundTripGTWithPCInExistingConfigAndVerifyInDailyReport() throws Exception{
        Map<String, String> param = getParam("testOrderRoundTripGTWithPCInExistingConfigAndVerifyInDailyReport");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String vendorName = param.get("vendorName");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String orderId = param.get("orderId");
        String columnOrderDetailIdIdxDep = param.get("columnOrderDetailIdIdxDep");
        String columnOrderDetailIdIdxRet = param.get("columnOrderDetailIdIdxRet");
        String columnIdxAdultDep = param.get("columnIdxAdultDep");
        String columnIdxAdultRet = param.get("columnIdxAdultRet");
        String expectedDepId = param.get("expectedDepId");
        String expectedRetId = param.get("expectedRetId");
        String countOfAdultDep = param.get("countOfAdultDep");
        String countOfAdultRet = param.get("countOfAdultRet");


        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("user click Commission Dashboard menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("user click Train menu");
        financeDashboardPage.goToTrainMenu();

        step("go to vendor name");
        step("user click More button");
        financeDashboardPage.clickMoreButtonByVendorName(vendorName);

        step("user click Transaction button");
        financeDashboardPage.clickTransactionsButton();

        step("verify vendor transaction page loaded");
        Assertion.assertThat("View list of transactions by is loaded", financeDashboardPage.isViewListOfTransactionsByLoaded(), is(true));

        step("user select filter search by Tiket.com Order ID");
        financeDashboardPage.selectSearchByOption("Tiket.com Order ID");
       
        step("user enter filter Order ID");
        financeDashboardPage.enterTransactionSearchOrderId(orderId);

        step("user filter start date and end date");
        financeDashboardPage.searchDateBy(startDate, endDate);

        step("user click Search button");
        financeDashboardPage.clickSearchButton();

        step("user verify there are 2 rows with the same Order ID for roundtrip");
        Assertion.assertThat("There are 2 rows with the same Order ID for roundtrip", financeDashboardPage.getTransactionRowCount(), is(2));

        step("user verify order detail ID for departure");
        Assertion.assertThat("Order detail ID for departure is correct", financeDashboardPage.getOrderDetailId("1", columnOrderDetailIdIdxDep), is(expectedDepId));

        step("user verify order detail ID for return");
        Assertion.assertThat("Order detail ID for return is correct", financeDashboardPage.getOrderDetailId("2", columnOrderDetailIdIdxRet), is(expectedRetId));

        step("user verify count of adult");
        Assertion.assertThat("Count of adult departure is correct", financeDashboardPage.getCountOfAdult("1", columnIdxAdultDep), is(countOfAdultDep));
        Assertion.assertThat("Count of adult return is correct", financeDashboardPage.getCountOfAdult("2", columnIdxAdultRet), is(countOfAdultRet));

        step("user clicks Download button");
        financeDashboardPage.clickDownloadButton();

        step("user verify file is downloaded");
        Assertion.assertThat("Download success snackbar message is displayed", financeDashboardPage.getDownloadSnackbarText(), is("Successfully download daily report"));

        step("user verify there are 2 rows with the same Order ID for roundtrip on downloaded file");
        Assertion.assertThat("There are 2 rows with the same Order ID for roundtrip on downloaded file", financeDashboardPage.getOrderIdCountFromLatestDownloadedCSV("dailyreport_", orderId), is(2));

    }


    @Author(names = {"Nasha"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testOrderHotelWithVendorCurrencyIDRAndCustCurrencySGDAndVerifyInDailyReportWithoutDiscAndAddons() throws Exception{
        Map<String, String> param = getParam("testOrderHotelWithVendorCurrencyIDRAndCustCurrencySGDAndVerifyInDailyReportWithoutDiscAndAddons");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String vendorCurrency = param.get("vendorCurrency");
        String customerCurrency = param.get("customerCurrency");
        String addOnDetails = param.get("addOnDetails");
        String vendorName = param.get("vendorName");
        String searchBy = param.get("searchBy");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String rowCount = param.get("rowCount");
        
        step("User goes to CFD dashboard");
        financeDashboardPage.goToFinanceDashboard(email, password);

        step("User clicks Commission Dashboard menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("User clicks Accommodation (Third Party) menu");
        financeDashboardPage.goToAccommodationThirdPartyMenu();

        step("User will be directed to Accommodation (Third Party) page");
        Assertion.assertThat("Accommodation (Third Party) page is loaded", financeDashboardPage.isAccommodationThirdPartyPageLoaded(), is(true));

        step("User click vendor name filter");
        financeDashboardPage.searchVendorNameV2(vendorName);

        step("User click Search button");
        financeDashboardPage.clickSearchButton();

        step("User can see " + vendorName + " vendor row");
        Assertion.assertThat("" + vendorName + " vendor row is displayed", financeDashboardPage.getFirstRowVendorName(vendorName), containsString(vendorName));

        step("User click More button");
        financeDashboardPage.clickMoreButton();

        step("User click Transaction button");
        financeDashboardPage.clickTransactionsButton();

        step("User can see " + vendorName + " transaction page");
        Assertion.assertThat("" + vendorName + " transaction page is displayed", financeDashboardPage.isViewListOfTransactionsByLoaded(), is(true));

        step("User filter by Tiket.com Order ID");
        financeDashboardPage.selectSearchByOption(searchBy);

        step("User enters Order ID in search field");
        financeDashboardPage.enterTransactionSearchOrderId(orderId);

         step("User filter start date and end date");
        financeDashboardPage.searchDateBy(startDate, endDate);

        step("User click Search button");
        financeDashboardPage.clickSearchButton();

        step("User can see transaction data based on filter");
        Assertion.assertThat("Transaction data count is correct", financeDashboardPage.getTransactionRowCount(), is(Integer.parseInt(rowCount)));

        step("User verify Order ID is correct");
        Assertion.assertThat("Order ID is correct", financeDashboardPage.getTxRow2OrderId(orderId), is(orderId));

        step("User verify vendor currency is correct");
        Assertion.assertThat("Vendor currency is IDR", financeDashboardPage.getAccommodationRow1VendorCurrency(), is(vendorCurrency));

        step("User verify customer currency is correct");
        Assertion.assertThat("Customer currency is SGD", financeDashboardPage.getAccommodationRow1CustomerCurrency(), is(customerCurrency));

        step("User verify add-on details is correct");
        Assertion.assertThat("Add-on details is correct", financeDashboardPage.getAccommodationRow1AddOnDetails(), is(addOnDetails));

        step("User click Download button");
        financeDashboardPage.clickDownloadButton();

        step("User verify file can be downloaded");
        Assertion.assertThat("File can be downloaded", financeDashboardPage.getDownloadSnackbarText(), is("Successfully download daily report"));
    }

    @Author(names = {"Harshit"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testCreditMemoCreatedSuccessfully() {
        Map<String, String> param = getParam("testCreditMemoCreatedSuccessfully");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);
        CreditMemoPage<?> creditMemoPage = new CreditMemoPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String supplierPath = param.get("supplierPath");
        String snackbarText = param.get("snackbarText");
        String amount = String.valueOf(CoreFunctions.generateRandomInt(2));

        step("Open Finance Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);

        step("Open Settlement menu and then click Credit Memo submenu");
        financeDashboardPage.goToCreditMemoMenu();

        step("Go to supplier that has credit memo Enabled");
        financeDashboardPage.goToSupplierCreditMemo(supplierPath);

        step("Click New Credit Memo button");
        creditMemoPage.clickNewCreditMemoButton();

        step("Verify Add Credit memo popup is displayed");
        Assertion.assertThat("Add Credit memo popup is displayed", creditMemoPage.isAddCreditMemoPopupDisplayed(), is(true));

        step("Select Source");
        creditMemoPage.selectSource();

        step("Select Source ID");
        creditMemoPage.selectSourceId();

        step("Select Type");
        creditMemoPage.selectType();

        step("Enter Amount");
        creditMemoPage.enterAmount(amount);

        step("Click Create button");
        creditMemoPage.clickCreateButton();

        step("Verify credit memo creation passed");
        Assertion.assertThat("Snackbar shows creation passed", creditMemoPage.getSnackbarText(), containsString(snackbarText));

        step("Verify new credit memo row is added with current date");
        Assertion.assertThat("New row added with current date", creditMemoPage.getCreatedRowDateTime(), containsString(CoreFunctions.getCurrentDateTime("M/d/yyyy")));

        step("Verify created credit memo amount matches entered amount");
        Assertion.assertThat("Amount matches entered value", creditMemoPage.getCreatedRowPrice(), containsString(amount));
    }

    @Author(names = {"Harshit"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testCreditMemoCannotBeCreatedWhenDisabled() {
        Map<String, String> param = getParam("testCreditMemoCannotBeCreatedWhenDisabled");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);
        CreditMemoPage<?> creditMemoPage = new CreditMemoPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String supplierPath = param.get("supplierPath");
        String snackbarText = param.get("snackbarText");
        String amount = String.valueOf(CoreFunctions.generateRandomInt(2));

        step("Open Finance Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);

        step("Open Settlement menu and then click Credit Memo submenu");
        financeDashboardPage.goToCreditMemoMenu();

        step("Go to supplier that has credit memo disabled");
        financeDashboardPage.goToSupplierCreditMemo(supplierPath);

        step("Click New Credit Memo button");
        creditMemoPage.clickNewCreditMemoButton();

        step("Verify Add Credit memo popup is displayed");
        Assertion.assertThat("Add Credit memo popup is displayed", creditMemoPage.isAddCreditMemoPopupDisplayed(), is(true));

        step("Select Source");
        creditMemoPage.selectSource();

        step("Select Source ID");
        creditMemoPage.selectSourceId();

        step("Select Type");
        creditMemoPage.selectType();

        step("Enter Amount");
        creditMemoPage.enterAmount(amount);

        step("Click Create button");
        creditMemoPage.clickCreateButton();

        step("Verify credit memo creation failed");
        Assertion.assertThat("Snackbar shows creation failure", creditMemoPage.getSnackbarText(), containsString(snackbarText));
    }

    @Author(names = {"Harshit"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testCreditMemoCanBeEdited() {
        Map<String, String> param = getParam("testCreditMemoCanBeEdited");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);
        CreditMemoPage<?> creditMemoPage = new CreditMemoPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String supplierPath = param.get("supplierPath");
        String snackbarText = param.get("snackbarText");

        step("Open Finance Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);

        step("Open Settlement menu and then click Credit Memo submenu");
        financeDashboardPage.goToCreditMemoMenu();

        step("Go to supplier that has credit memo enabled");
        financeDashboardPage.goToSupplierCreditMemo(supplierPath);

        step("Filter credit memo by status Available");
        creditMemoPage.filterByStatusAvailable();

        step("Verify first row status is Available before editing");
        Assertion.assertThat("First row status is Available", creditMemoPage.isFirstRowStatusAvailable(), is(true));

        step("Extract amount before update");
        String amountBeforeUpdate = creditMemoPage.getCreatedRowPrice();

        String updatedAmount = String.valueOf(CoreFunctions.generateRandomInt(2));
        while(amountBeforeUpdate.contains(updatedAmount)) {
            updatedAmount = String.valueOf(CoreFunctions.generateRandomInt(2));
        }

        step("Hover to first row and click ellipsis action");
        creditMemoPage.clickRowActionMore();

        step("Click Edit action");
        creditMemoPage.clickEditAction();

        step("Verify Edit Credit memo popup is displayed");
        Assertion.assertThat("Edit Credit memo popup is displayed", creditMemoPage.isEditCreditMemoPopupDisplayed(), is(true));

        step("Enter updated amount");
        creditMemoPage.updateAmount(updatedAmount);

        step("Click Save button");
        creditMemoPage.clickCreateButton();

        step("Verify Confirm Credit Memo Update popup is displayed");
        Assertion.assertThat("Confirm update popup is displayed", creditMemoPage.isConfirmUpdatePopupDisplayed(), is(true));

        step("Enter update notes");
        creditMemoPage.enterUpdateReason("updating memo");

        step("Click Confirm Update button");
        creditMemoPage.clickConfirmUpdateButton();

        step("Verify credit memo update passed");
        Assertion.assertThat("Snackbar shows update success", creditMemoPage.getSnackbarText(), containsString(snackbarText));

        step("Extract amount after update and verify it changed");
        String amountAfterUpdate = creditMemoPage.getCreatedRowPrice();
        Assertion.assertThat("Amount is updated to new value", amountAfterUpdate, containsString(updatedAmount));
        Assertion.assertThat("Amount changed after update", amountAfterUpdate, not(amountBeforeUpdate));
    }

    @Author(names = {"Harshit"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testCreditMemoCanBeCancelled() {
        Map<String, String> param = getParam("testCreditMemoCanBeCancelled");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);
        CreditMemoPage<?> creditMemoPage = new CreditMemoPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String supplierPath = param.get("supplierPath");
        String snackbarText = param.get("snackbarText");

        step("Open Finance Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);

        step("Open Settlement menu and then click Credit Memo submenu");
        financeDashboardPage.goToCreditMemoMenu();

        step("Go to supplier that has credit memo enabled");
        financeDashboardPage.goToSupplierCreditMemo(supplierPath);

        step("Filter credit memo by status Available");
        creditMemoPage.filterByStatusAvailable();

        step("Verify first row status is Available before cancelling");
        Assertion.assertThat("First row status is Available", creditMemoPage.isFirstRowStatusAvailable(), is(true));

        step("Hover to first row and click ellipsis action");
        creditMemoPage.clickRowActionMore();

        step("Click Edit action");
        creditMemoPage.clickEditAction();

        step("Click Void button");
        creditMemoPage.clickVoidButton();

        step("Verify Void Credit Memo popup is displayed");
        Assertion.assertThat("Void Credit memo popup is displayed", creditMemoPage.isVoidPopupDisplayed(), is(true));

        step("Enter void notes");
        creditMemoPage.enterVoidReason("voiding memo");

        step("Click Confirm Void button");
        creditMemoPage.clickConfirmVoidButton();

        step("Verify credit memo void passed");
        Assertion.assertThat("Snackbar shows void success", creditMemoPage.getSnackbarText(), containsString(snackbarText));

        step("Reset the status filter to show all credit memos");
        creditMemoPage.resetStatusFilter();

        step("Verify credit memo status is updated to cancelled");
        Assertion.assertThat("Credit memo status is cancelled", creditMemoPage.isFirstRowStatusCancelled(), is(true));
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testCFDMultipleDeposit() throws Exception {
        Map<String, String> param = getParam("testCFDMultipleDeposit");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("email");
        String password = param.get("password");
        String productId1 = param.get("productId1");
        String productId2 = param.get("productId2");
        String username1 = param.get("username1");
        String password1 = param.get("password1");
        String vendorId1 = param.get("vendorId1");
        String productName1 = param.get("productName1");
        String productName2 = param.get("productName2");
        String searchBy = param.get("searchBy");

        String packageCode = param.get("packageCode");
        String sortAttributes = param.get("sortAttributes");
        String sortDirection = param.get("sortDirection");
        String dateTimeFrom = param.get("dateTimeFrom");
        String dateTimeTo = param.get("dateTimeTo");
        String pageSize = param.get("pageSize");
        String pageNumber = param.get("pageNumber");

        String salutation = param.get("salutation");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String contactEmail = param.get("contactEmail");
        String countryCode = param.get("countryCode");
        String phoneNumber = param.get("phoneNumber");
        String nationality = param.get("nationality");
        long profileId = Long.parseLong(param.get("profileId"));
        String priceTierCode = param.get("priceTierCode");
        int priceTierQuantity = Integer.parseInt(param.get("priceTierQuantity"));
        String currency = param.get("currency");


        step("Pass data for TTD Product");
        orderCreationPage.TTDScheduleSearchParams scheduleSearch = new orderCreationPage.TTDScheduleSearchParams(
                packageCode, sortAttributes, sortDirection, dateTimeFrom, dateTimeTo, pageSize, pageNumber);

        step("Pass the  the traveler details for booking");
        orderCreationPage.TTDTravellerDetails traveller = new orderCreationPage.TTDTravellerDetails(
                salutation, firstName, lastName, contactEmail, countryCode, phoneNumber, nationality,
                profileId, priceTierCode, priceTierQuantity, currency);

        step("Create 1st TTD Booking via API");
        orderCreationPage.TTDBookingResult booking1 = orderCreationPage.createTTDBookingViaApi(username, password, productId1, scheduleSearch, traveller);

        step("Verify 1st TTD Booking via API is successful");
        Assertion.assertThat("1st TTD Booking payment status code is 200 or 201", booking1.paymentResponse().getStatusCode(), Matchers.anyOf(is(200), is(201)));

        step("Create 2nd TTD Booking via API");
        orderCreationPage.TTDBookingResult booking2 = orderCreationPage.createTTDBookingViaApi(username, password, productId2, scheduleSearch, traveller);

        step("Verify 2nd TTD Booking via API is successful");
        Assertion.assertThat("2nd TTD Booking payment status code is 200 or 201", booking2.paymentResponse().getStatusCode(), Matchers.anyOf(is(200), is(201)));

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username1, password1);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Open Commission Dashboard Accordion Menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("Go To TTD Menu");
        financeDashboardPage.goToTTDMenu();

        step("Filter by Vendor ID and Search");
        financeDashboardPage.searchVendorId(vendorId1);
        financeDashboardPage.clickSearchButtonVendorListingB2C();

        step("Go to Daily Report");
        financeDashboardPage.clickMoreButton();
        financeDashboardPage.clickTransactionsButton();

        step("User filter by Tiket.com Order ID");
        financeDashboardPage.selectSearchByOption(searchBy);

        step("Verify 2nd Order Id is displayed in Transactions table");
        String firstOrderId = String.valueOf(booking1.orderId());
        financeDashboardPage.enterTransactionSearchOrderId(firstOrderId);
        financeDashboardPage.clickSearchButton();
        Assertion.assertThat("2nd Order Id matches", financeDashboardPage.getTxRow2OrderId(firstOrderId), is(firstOrderId));

        step("Verify first Product Name is displayed in row  of Transactions table");
        Assertion.assertThat("Row 1 Product Name matches", financeDashboardPage.getTxRow1ProductName(productName1), is(productName1));

        step("Click on Reset Filter");
        financeDashboardPage.clickResetFilterButton();

        step("User filter by Tiket.com Order ID");
        financeDashboardPage.selectSearchByOption(searchBy);

        step("Verify 1st Order Id is displayed in Transactions table");
        String secondOrderId = String.valueOf(booking2.orderId());
        financeDashboardPage.enterTransactionSearchOrderId(secondOrderId);
        String actualOrderId = RetryUtils.retryUntil(
                5, 3000,
                () -> {
                    financeDashboardPage.clickSearchButton();
                    return financeDashboardPage.getTxRow2OrderId(secondOrderId);
                },
                result -> secondOrderId.equals(result),
                "Search transaction for order " + secondOrderId
        );
        Assertion.assertThat("1st Order Id matches", actualOrderId, is(secondOrderId));

        step("Verify 2nd Product Name is displayed in row  of Transactions table");
        Assertion.assertThat("Row 1 Product Name matches", financeDashboardPage.getTxRow1ProductName(productName2), is(productName2));

    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testCFDSalesOrderMiniapps() throws Exception {
        Map<String, String> param = getParam("testCFDSalesOrderMiniapps");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String salesOrderId = param.get("salesOrderId");
        String salesOrderSource = param.get("salesOrderSource");

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Click Sales Menu");
        financeDashboardPage.clickSalesMenu();

        step("Click Sales Order Menu");
        financeDashboardPage.clickSalesOrderMenu();

        step("Click Sales Order Filter");
        financeDashboardPage.clickSalesOrderFilter();

        step("Enter Sales Order Id in Search field");
        financeDashboardPage.enterSalesOrderIdSearch(salesOrderId);

        step("Click Sales Order Id Checkbox");
        financeDashboardPage.clickSalesOrderIdCheckbox();

        step("Click Apply button");
        financeDashboardPage.clickSalesOrderApplyButton();

        step("Verify Order Id is displayed in the results table");
        Assertion.assertThat("Order Id matches", financeDashboardPage.getSalesOrderResultOrderId(salesOrderId), is(salesOrderId));

        step("Verify Source is displayed in the results table");
        Assertion.assertThat("Source matches", financeDashboardPage.getSalesOrderResultSource(salesOrderSource), is(salesOrderSource));

        step("Verify Order Price is not null");
        Assertion.assertThat("Order Price is not null", financeDashboardPage.getSalesOrderResultPrice(), notNullValue());
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testCFDSalesOrderAffiliate() throws Exception {
        Map<String, String> param = getParam("testCFDSalesOrderAffiliate");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String salesOrderId = param.get("salesOrderId");
        String salesOrderSource = param.get("salesOrderSource");

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Click Sales Menu");
        financeDashboardPage.clickSalesMenu();

        step("Click Sales Order Menu");
        financeDashboardPage.clickSalesOrderMenu();

        step("Click Sales Order Filter");
        financeDashboardPage.clickSalesOrderFilter();

        step("Enter Sales Order Id in Search field");
        financeDashboardPage.enterSalesOrderIdSearch(salesOrderId);

        step("Click Sales Order Id Checkbox");
        financeDashboardPage.clickSalesOrderIdCheckbox();

        step("Click Apply button");
        financeDashboardPage.clickSalesOrderApplyButton();

        step("Verify Order Id is displayed in the results table");
        Assertion.assertThat("Order Id matches", financeDashboardPage.getSalesOrderResultOrderId(salesOrderId), is(salesOrderId));

        step("Verify Source is displayed in the results table");
        Assertion.assertThat("Source matches", financeDashboardPage.getSalesOrderResultSource(salesOrderSource), is(salesOrderSource));

        step("Verify Order Price is not null");
        Assertion.assertThat("Order Price is not null", financeDashboardPage.getSalesOrderResultPrice(), notNullValue());
    }

    @Author(names = {"Nasha"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testVerifySettlementTypeBankTransferForSinarjayaBus() throws Exception {
        Map<String, String> param = getParam("testVerifySettlementTypeBankTransferForSinarjayaBus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String vendorId = param.get("vendorId");
        String orderId = param.get("orderId");
        String searchBy = param.get("searchBy");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String settlementType = param.get("settlementType");
        String orderIdColumn = param.get("orderIdColumn");
        String vendorIdColumn = param.get("vendorIdColumn");
        String settlementTypeColumn = param.get("settlementTypeColumn");

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("user click Commission Dashboard menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("user click Bus & Shuttle menu");
        financeDashboardPage.goToBusMenu();

        step("user filter by Vendor ID and search");
        financeDashboardPage.searchVendorId(vendorId);
        financeDashboardPage.clickSearchButtonVendorListingB2C();

        step("user hover to " + vendorId + " row");
        financeDashboardPage.hoverToVendorRowByVendorId(vendorId);

        step("user verify settlement type on commission dashboard is " + settlementType);
        Assertion.assertThat("Settlement type on commission dashboard is correct", financeDashboardPage.getSettlementTypeByVendorId(vendorId), is(settlementType));

        step("user click More button");
        financeDashboardPage.clickMoreButtonByVendorId(vendorId);

        step("user click Transaction button");
        financeDashboardPage.clickTransactionsButton();

        step("verify vendor transaction page loaded");
        Assertion.assertThat("View list of transactions by is loaded", financeDashboardPage.isViewListOfTransactionsByLoaded(), is(true));

        step("user select filter search by " + searchBy);
        financeDashboardPage.selectSearchByOption(searchBy);

        step("user enter filter Order ID");
        financeDashboardPage.enterTransactionSearchOrderId(orderId);

        step("user filter start date and end date");
        financeDashboardPage.searchDateBy(startDate, endDate);

        step("user click Search button");
        financeDashboardPage.clickSearchButton();

        step("user verify Order ID on transaction page");
        Assertion.assertThat("Order ID is correct", financeDashboardPage.getTransactionColumnValueByName("1", orderIdColumn, orderId), is(orderId));

        step("user verify Vendor ID on transaction page");
        Assertion.assertThat("Vendor ID is correct", financeDashboardPage.getTransactionColumnValueByName("1", vendorIdColumn), is(vendorId));

        step("user verify settlement type on transaction page is " + settlementType);
        Assertion.assertThat("Settlement type on transaction page is correct", financeDashboardPage.getTransactionColumnValueByName("1", settlementTypeColumn), is(settlementType));
    }


    @Author(names = {"Manan Kumar"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testCFDSalesRefundLinkFromSalesOrder() throws Exception {
        Map<String, String> param = getParam("testCFDSalesRefundLinkFromSalesOrder");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String salesOrderId = param.get("salesOrderId");

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Click Sales Menu");
        financeDashboardPage.clickSalesMenu();

        step("Click Sales Order Menu");
        financeDashboardPage.clickSalesOrderMenu();

        step("Click Sales Order Filter");
        financeDashboardPage.clickSalesOrderFilter();

        step("Enter Sales Order Id in Search field");
        financeDashboardPage.enterSalesOrderIdSearch(salesOrderId);

        step("Click Sales Order Id Checkbox");
        financeDashboardPage.clickSalesOrderIdCheckbox();

        step("Click Apply button");
        financeDashboardPage.clickSalesOrderApplyButton();

        step("Verify Order Id is displayed in the results table");
        Assertion.assertThat("Order Id matches", financeDashboardPage.getSalesOrderResultOrderId(salesOrderId), is(salesOrderId));

        step("Click Sales Order result row to open Sales Order detail page");
        financeDashboardPage.clickSalesOrderResultRow();

        step("Fetch Refund Id from Sales Order detail page");
        String salesOrderDetailRefundId = financeDashboardPage.getSalesOrderDetailRefundId();

        step("Click Sales Refund link inside Sales Order detail page");
        financeDashboardPage.clickSalesOrderDetailRefundLink();

        step("Fetch Order Id and Refund Id from Sales Refund detail page");
        String salesRefundDetailRefundId = financeDashboardPage.getSalesRefundDetailRefundId();
        String salesRefundDetailOrderId = financeDashboardPage.getSalesRefundDetailOrderId();

        step("Verify Sales Refund detail page is opened");
        Assertion.assertThat("Sales Refund detail page URL is opened", financeDashboardPage.getCurrentUrl(), containsString("/admin/intools/sales/refund/"));

        step("Verify Sales Refund detail page is opened with the same Refund Id");
        Assertion.assertThat("Refund Id matches", salesRefundDetailRefundId, is(salesOrderDetailRefundId));

        step("Verify Order Id in Sales Refund detail page matches Sales Order detail page");
        Assertion.assertThat("Order Id matches", salesRefundDetailOrderId, is(salesOrderId));
    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testCFDFinderData() throws Exception {
        Map<String, String> param = getParam("testCFDFinderData");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String OrderDetailId = param.get("OrderDetailId");
        String searchBy = param.get("searchBy");

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("Go To FInder Section");
        financeDashboardPage.clickFinderMenu();

        step("Click on  Search Transactions");
        financeDashboardPage.clickSearchTransactionsMenu();

        step("Click on  Search by ");
        financeDashboardPage.clickSearchByInput();

        step("Select Search By Option " + searchBy);
        financeDashboardPage.clickSearchByOption(searchBy);

        step("Enter Search Data " + OrderDetailId);
        financeDashboardPage.enterSearchData(OrderDetailId);

        step("Click Search Button");
        financeDashboardPage.clickSearchButton();

        step("Verify Order Detail ID " + OrderDetailId + " exists in result table");
        Assertion.assertThat("Order Detail ID is found in table", financeDashboardPage.isOrderDetailIdVisible(OrderDetailId), is(true));

    }

    @Author(names = {"Gaurav Juneja"})
    @Module(name = "CFD")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testCFDHotelBookingDailyReport() throws Exception {
        Map<String, String> param = getParam("testCFDHotelBookingDailyReport");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String hotelUsername = param.get("hotelUsername");
        String hotelPassword = param.get("hotelPassword");
        String hotelStartDate = param.get("hotelStartDate");
        String hotelQuery = param.get("hotelQuery");
        int hotelAdult = Integer.parseInt(param.get("hotelAdult"));
        int hotelNight = Integer.parseInt(param.get("hotelNight"));
        int hotelRoom = Integer.parseInt(param.get("hotelRoom"));
        String hotelFullName = param.get("hotelFullName");
        String hotelNameTitle = param.get("hotelNameTitle");
        String hotelGuestFullName = param.get("hotelGuestFullName");
        String hotelGuestTitle = param.get("hotelGuestTitle");
        String hotelCurrency = param.get("hotelCurrency");
        String vendorName = param.get("vendorName");
        String searchBy = param.get("searchBy");

        step("Create Hotel Booking via API");
        orderCreationPage.createHotelSakukuIssuedOrderApi(hotelUsername, hotelPassword, hotelStartDate, hotelQuery,
                hotelAdult, hotelNight, hotelRoom, hotelFullName, hotelNameTitle, hotelGuestFullName, hotelGuestTitle, hotelCurrency);

        int hotelOrderId = orderCreationPage.getOrderId();
        step("Hotel Order Id created: " + hotelOrderId);

        step("Verify Hotel Order Id is created");
        Assertion.assertThat("Hotel Order Id is not zero", hotelOrderId, not(is(0)));

        step("Go To Intools Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);
        Assertion.assertThat("Finance Dashboard is loaded", financeDashboardPage.isFinanceDashboardLoaded(), is(true));

        step("User clicks Commission Dashboard menu");
        financeDashboardPage.openCommissionDashboardAccordionMenu();

        step("User clicks Accommodation (Native) menu");
        financeDashboardPage.goToAccommodationNativeMenu();

        step("User click vendor name filter");
        financeDashboardPage.searchVendorNameV2(vendorName);

        step("User click Search button");
        financeDashboardPage.clickSearchButton();
        
        step("User click More button");
        financeDashboardPage.clickMoreButton();

        step("User click Transaction button");
        financeDashboardPage.clickTransactionsButton();

        step("User can see transaction page");
        Assertion.assertThat("Transaction page is displayed", financeDashboardPage.isViewListOfTransactionsByLoaded(), is(true));

        step("User filter by " + searchBy);
        financeDashboardPage.selectSearchByOption(searchBy);

        step("User enters Order ID in search field");
        financeDashboardPage.enterTransactionSearchOrderId(String.valueOf(hotelOrderId));

        step("User verify Order ID " + hotelOrderId + " exists in transaction table");
        String actualOrderId = RetryUtils.retryUntil(
                5, 3000,
                () -> {
                    financeDashboardPage.clickSearchButton();
                    return financeDashboardPage.getTxRow2OrderId(String.valueOf(hotelOrderId));
                },
                result -> String.valueOf(hotelOrderId).equals(result),
                "Search transaction for order " + hotelOrderId
        );
        Assertion.assertThat("Order Id matches", actualOrderId, is(String.valueOf(hotelOrderId)));

    }

    @Author(names = {"Nasha"})
    @Module(name = "Reconciliation")
    @Tribe(name = "CFD")
    @Vertical(name = "CFD")
    @Test
    public void testReconciliationUploadDetail() {
        Map<String, String> param = getParam("testReconciliationUploadDetail");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Open Finance Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);

        step("Open Reconciliation menu");
        financeDashboardPage.goToReconciliationMenu();

        step("Open Uploads menu");
        financeDashboardPage.goToReconciliationUploadsMenu();

        step("Get data of first row on Uploads page");
        Map<String, String> uploadsRowData = financeDashboardPage.getReconUploadsFirstRowData("Upload ID", "File Name", "Template Used", "Uploaded By", "Unreconciled Count");

        step("Click View button on first row");
        financeDashboardPage.clickFirstRowViewButton();

        step("Verify Uploads document detail page is opened");
        Assertion.assertThat("Upload detail page is opened", financeDashboardPage.isUploadDetailPageDisplayed(), is(true));

        step("Verify detail page data is the same as in Uploads page");
        uploadsRowData.forEach((label, expected) -> Assertion.assertThat(label + " matches Uploads page", financeDashboardPage.getUploadDetailValueByLabel(label), is(expected)));
    }

    @Author(names = {"Nasha"})
    @Module(name = "Reconciliation")
    @Tribe(name = "CFD")
    @Vertical(name = "CFD")
    @Test
    public void testReconciliationUploadsFilterByUploadedBy() {
        Map<String, String> param = getParam("testReconciliationUploadsFilterByUploadedBy");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        FinanceDashboardPage<?> financeDashboardPage = new FinanceDashboardPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String uploadedBy = param.get("uploadedBy");

        step("Open Finance Dashboard");
        financeDashboardPage.goToFinanceDashboard(username, password);

        step("Open Reconciliation menu");
        financeDashboardPage.goToReconciliationMenu();

        step("Open Uploads menu");
        financeDashboardPage.goToReconciliationUploadsMenu();

        step("Apply filter uploaded by: " + uploadedBy);
        financeDashboardPage.clickUploadedByFilter();
        financeDashboardPage.searchUploadedByFilter(uploadedBy);
        financeDashboardPage.selectUploadsFilterOptionByValue(uploadedBy);
        financeDashboardPage.clickUploadsFilterApplyButton();

        step("Verify green dot icon is displayed on Uploaded By filter");
        Assertion.assertThat("Green dot icon is displayed on Uploaded By filter", financeDashboardPage.isUploadedByFilterActiveDotDisplayed(), is(true));

        step("Verify all rows are filtered by uploaded by: " + uploadedBy);
        List<String> uploadedByValues = financeDashboardPage.getReconUploadsColumnValuesByHeading("Uploaded By");
        Assertion.assertThat("Filter result has at least one row", uploadedByValues, is(not(empty())));
        for (int i = 0; i < uploadedByValues.size(); i++) {
            Assertion.assertThat("Uploaded By for row %d".formatted(i + 1), uploadedByValues.get(i), is(uploadedBy));
        }
    }

}

