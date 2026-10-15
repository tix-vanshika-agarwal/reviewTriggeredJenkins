package com.tiket.web.dashboard.test.ttd;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.dashboard.page.ttd.Ignis.LoginPage.IgnisLoginPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.SalesReportPage.IgnisSalesReportPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.SelectPartnerPage.IgnisSelectPartnerPageWeb;
import org.testng.annotations.Test;

import java.util.Map;
import static org.hamcrest.core.Is.is;

import static com.tiket.testdata.TestData.getParam;

public class SalesReportTtdTest extends WebTest {

    @Author(names = {"Rizki"})
    @Module(name = "SalesReport")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisFilterSalesReportByTransactionDateWeekly() {
        Map<String, String> param = getParam("testIgnisFilterSalesReportByTransactionDateWeekly");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        String timePeriod = param.get("timePeriod");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisSalesReportPageWeb<?> ignisSalesReportPageWeb = IgnisSalesReportPageWeb.getInstance(session);

        step("Login to Ignis");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select any partners");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to Sales Report page");
        ignisSalesReportPageWeb.goToSalesReportPage();

        step("Select any product and its packages");
        ignisSalesReportPageWeb.selectProduct(productName);
        ignisSalesReportPageWeb.selectPackage(packageName);

        step("Set the Time Period as Last 7 Days, click Save");
        ignisSalesReportPageWeb.selectTimePeriod(timePeriod, "", "");

        step("Set the filter as By Transaction Date");
        ignisSalesReportPageWeb.setVisitOrTransactionFilter("transaction");

        step("Click Search");
        ignisSalesReportPageWeb.clickSearch();

        step("Assertions");
        Assertion.assertThat("Filter By result is correct", ignisSalesReportPageWeb.validateFilterByMatchFilterResultLabel("transaction"), is(true));
        Assertion.assertThat("Date range result is correct", ignisSalesReportPageWeb.validateFilterDateMatchWithResultDate(), is(true));
    }

    @Author(names = {"Rizki"})
    @Module(name = "SalesReport")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisFilterSalesReportByVisitDateCustom() {
        Map<String, String> param = getParam("testIgnisFilterSalesReportByVisitDateCustom");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisSalesReportPageWeb<?> ignisSalesReportPageWeb = IgnisSalesReportPageWeb.getInstance(session);

        step("Login to Ignis");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select any partners");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to Sales Report page");
        ignisSalesReportPageWeb.goToSalesReportPage();

        step("Select any product and its packages");
        ignisSalesReportPageWeb.selectProduct(productName);
        ignisSalesReportPageWeb.selectPackage(packageName);

        step("Set the Time Period as Custom and select the start and end date (max. 30 days), click Save");
        ignisSalesReportPageWeb.selectTimePeriod("pilih periode waktu", startDate, endDate);

        step("Set the filter as By Visit Date");
        ignisSalesReportPageWeb.setVisitOrTransactionFilter("visit");

        step("Click Search");
        ignisSalesReportPageWeb.clickSearch();

        step("Assertions");
        Assertion.assertThat("Filter By result is correct", ignisSalesReportPageWeb.validateFilterByMatchFilterResultLabel("visit"), is(true));
        Assertion.assertThat("Date range result is correct", ignisSalesReportPageWeb.validateFilterDateMatchWithResultDate(), is(true));
    }
}