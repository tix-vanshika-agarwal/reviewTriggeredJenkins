package com.tiket.SmartUI.test;

import com.tiket.SmartUI.SmartUtils;
import com.tiket.SmartUI.page.order.OrderSmartPage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderhistory.OrderHistoryPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.testbase.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class OrderSmartTest extends BaseTest {
    @Author(names = {"Naufal Hafiz"})
    @Module(name = "Order")
    @Tribe(name = "Order")
    @Vertical(name = "Order")
    @Test
    public void testOrderSmartGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testOrderSmartGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderHistoryPage<WebDriver> orderHistoryPage = OrderHistoryPage.getInstance(session);
        OrderSmartPage<WebDriver> orderSmartPage = OrderSmartPage.getInstance(session);

        String vertical1 = param.get("vertical1");
        String vertical2 = param.get("vertical2");

        step("User open app");
        homePage.gotoHome();

        step("User open app as guest user");
        step("Guest user");
        loginPage.loginAsGuest();

        step("Open my order page");
        String lang = homePage.getLanguageApp();
        homePage.openMyOrderPage();

        step("open order history");
        orderListPage.openOrderHistory();

        step("Apply filter");
        orderHistoryPage.clickFilter();
        orderHistoryPage.selectFilterVertical(vertical1,lang);
        orderHistoryPage.selectFilterVertical(vertical2,lang);
        SmartUtils.takeSmartScreenshot(session.driver(), "Order_[Guest] Verify User can choose vertical on Checkbox");
        orderHistoryPage.setPeriod();
        orderHistoryPage.clickApplyFilter();
        orderSmartPage.waitForFilterToBeApplied();
        SmartUtils.takeSmartScreenshot(session.driver(), "Order_[Guest] Verify User apply filter");

        step("Apply Sort");
        orderHistoryPage.applySort();
        SmartUtils.takeSmartScreenshot(session.driver(), "Order_[Guest] Verify User apply sorting");
    }

    @Author(names = {"Naufal Hafiz"})
    @Module(name = "Order")
    @Tribe(name = "Order")
    @Vertical(name = "Order")
    @Test
    public void testOrderSmartLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testOrderSmartLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderHistoryPage<WebDriver> orderHistoryPage = OrderHistoryPage.getInstance(session);
        OrderSmartPage<WebDriver> orderSmartPage = OrderSmartPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String vertical1 = param.get("vertical1");
        String vertical2 = param.get("vertical2");

        step("User open app");
        homePage.gotoHome();

        step("User open app as login user " +username);
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        String lang = homePage.getLanguageApp();
        homePage.openMyOrderPage();

        step("open order history");
        orderListPage.openOrderHistory();

        step("Apply filter");
        orderHistoryPage.clickFilter();
        orderHistoryPage.selectFilterVertical(vertical1,lang);
        orderHistoryPage.selectFilterVertical(vertical2,lang);
        SmartUtils.takeSmartScreenshot(session.driver(), "Order_[Login] Verify User can choose vertical on Checkbox");
        orderHistoryPage.setPeriod();
        orderHistoryPage.clickApplyFilter();
        orderSmartPage.waitForFilterToBeApplied();
        SmartUtils.takeSmartScreenshot(session.driver(), "Order_[Login] Verify User apply filter");
        step("Apply Sort");
        orderHistoryPage.applySort();
        SmartUtils.takeSmartScreenshot(session.driver(), "Order_[Login] Verify User apply sorting");
    }
}
