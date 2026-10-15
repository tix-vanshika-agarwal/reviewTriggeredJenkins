package com.tiket.analytics.app.test.order;

import com.tiket.analytics.BaseAnalyticsTest;
import com.tiket.analytics.assertions.BaseAssertions;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class OrderPageTest extends BaseAnalyticsTest {
    @Author(names = {"Okta"})
    @Module(name = "OrderList")
    @Tribe(name = "PRO")
    @Vertical(name = "Core")
    @Test
    public void testOrderListPageVisitGuest() {
        Map<String, String> testProperties = getParam("testOrderListPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open order list page");
        homePage.openMyOrderPage();

        sleep(40);

        BaseAssertions orderPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", orderPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "OrderList")
    @Tribe(name = "PRO")
    @Vertical(name = "Core")
    @Test
    public void testOrderListPageVisitLogin() {
        Map<String, String> testProperties = getParam("testOrderListPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginMethod = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginMethod, email, password);

        step("Open order list page");
        homePage.openMyOrderPage();

        sleep(40);

        BaseAssertions orderPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", orderPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "OrderDetail")
    @Tribe(name = "PRO")
    @Vertical(name = "Core")
    @Test
    public void testOrderDetailPageVisitLogin() {
        Map<String, String> testProperties = getParam("testOrderDetailPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginMethod = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginMethod, email, password);

        step("Open order list page");
        homePage.openMyOrderPage();

        step("Open order detail page");
        orderListPage.openFirstWaitingPaymentOrder();

        sleep(40);

        BaseAssertions orderPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", orderPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "OrderHistory")
    @Tribe(name = "PRO")
    @Vertical(name = "Core")
    @Test
    public void testOrderHistoryPageVisitGuest() {
        Map<String, String> testProperties = getParam("testOrderHistoryPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open order list page");
        homePage.openMyOrderPage();

        step("Open order history page");
        orderListPage.openOrderHistory();

        sleep(40);

        BaseAssertions orderPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", orderPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "OrderHistory")
    @Tribe(name = "PRO")
    @Vertical(name = "Core")
    @Test
    public void testOrderHistoryPageVisitLogin() {
        Map<String, String> testProperties = getParam("testOrderHistoryPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginMethod = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginMethod, email, password);

        step("Open order list page");
        homePage.openMyOrderPage();

        step("Open order history page");
        orderListPage.openOrderHistory();

        sleep(40);

        BaseAssertions orderPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", orderPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", orderPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
    }
}