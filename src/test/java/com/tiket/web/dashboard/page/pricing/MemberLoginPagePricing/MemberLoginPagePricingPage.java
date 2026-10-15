package com.tiket.web.dashboard.page.pricing.MemberLoginPagePricing;

import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberPage;
import com.tiket.web.dashboard.page.member_dashboard.PricingDashboardHome;
import java.util.Map;

import static org.hamcrest.Matchers.is;

public class MemberLoginPagePricingPage {

    private final Session<?> session;
    private final MemberLoginPage<?> memberLoginPage;
    private final MemberPage<?> memberPage;
    private final PricingDashboardHome<?> pricingDashboardHome;

    public MemberLoginPagePricingPage() {
        this.session = BaseTest.sessionThreadLocal.get();
        this.memberLoginPage = new MemberLoginPage<>(session);
        this.memberPage = new MemberPage<>(session);
        this.pricingDashboardHome = new PricingDashboardHome<>(session);
    }

    public void ensurePricingDashboardIsLoaded(Map<String, String> param) {
        if (!pricingDashboardHome.isPricingDashboardLoaded()) {
            String username = param.get("username");
            String password = param.get("password");

            step("Login via cookie injection");
            memberLoginPage.adminLoginViaCookie(username, password);

            step("Verify Member Page is Loaded or not");
            Assertion.assertThat("Member page is loaded", memberPage.isMemberDashboardLoaded(), is(true));

            step("Click to open Pricing Dashboard");
            memberPage.goToPricingDashboard(username, password);
            Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }
    }

    private void step(String message) {
        System.out.println("STEP: " + message); // or use your logger or test framework step method
    }
}
