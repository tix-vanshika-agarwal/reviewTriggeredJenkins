package com.tiket.web.dashboard.test.ttd;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.logging.ILogger;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.dashboard.page.ttd.Ignis.LoginPage.IgnisLoginPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.OrderReportPage.IgnisOrderReportPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.PartnerPage.IgnisPartnerPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.SelectPartnerPage.IgnisSelectPartnerPageWeb;
import org.bson.assertions.Assertions;
import org.testng.annotations.Test;
import static org.hamcrest.core.Is.is;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class IgnisOrderReportTest extends WebTest {

    @Author(names = {"Rizki"})
    @Module(name = "OrderReport")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisFilterOrderReportByPaymentRedeemRefundWithTransaction() {
        Map<String, String> param = getParam("testIgnisFilterOrderReportByPaymentRedeemRefundWithTransaction");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String settlementFilter = param.get("settlementFilter");
        String redeemFilter = param.get("redeemFilter");
        String refundFilter = param.get("refundFilter");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);

        step("Login to Ignis");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to Order Report page");
        ignisOrderReportPageWeb.goToPage();

        step("Select any Product");
        ignisOrderReportPageWeb.setProductFilter(productName);

        step("Set the filter to By Transaction Date");

        step("Set the field filters PAYMENT STATUS, REDEEM STATUS , REFUND STATUS");
        ignisOrderReportPageWeb.setSettlementFilter(settlementFilter);
        ignisOrderReportPageWeb.setRedeemFilter(redeemFilter);
        ignisOrderReportPageWeb.setRefundFilter(refundFilter);

        step("Click Apply Button");
        ignisOrderReportPageWeb.clickApplyFilter();

        step("Assertions");
        Assertion.assertThat("Settlement Status is correct", ignisOrderReportPageWeb.verifySettlementStatus(settlementFilter), is(true));
        Assertion.assertThat("Redeem Status is correct", ignisOrderReportPageWeb.verifyRedeemStatus(redeemFilter), is(true));
        Assertion.assertThat("Refund Statis is correct", ignisOrderReportPageWeb.verifyRefundStatus(refundFilter), is(true));
    }

}
