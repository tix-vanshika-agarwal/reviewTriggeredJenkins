package com.tiket.web.dashboard.test.hotel.extranet;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.extranet.Navbar;
import com.tiket.web.dashboard.page.hotel.extranet.SideMenu;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;

public class ManageUserTest extends BaseTest {
    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "ManageUser")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyMenuForUserReservation() {
        Map<String, String> param = getParam("testVerifyMenuForUserReservation");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        SideMenu<?> sideMenu = new SideMenu<>(session);
        Navbar<?> navbar = new Navbar<>(session);

        AuthTest authTest = new AuthTest();

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Verify menu item");
        Assert.assertTrue(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.HOME));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.PROPERTY));
        Assert.assertTrue(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.RATES_AND_AVAILABILITY));
        Assert.assertTrue(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.PROMO_AND_CAMPAIGN));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.ADDONS));
        Assert.assertTrue(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.BOOKINGS));
        Assert.assertTrue(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.CHAT));
        Assert.assertTrue(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.ANALYTICS));
        Assert.assertTrue(sideMenu.isSubmenuDisplayed(SideMenu.SidebarSubmenu.PREFERRED_PARTNER));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.GUEST_REVIEWS));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.FINANCE));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.ACTIVITY_LOG));
        Assert.assertTrue(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.MANAGE_USERS));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.PROPERTY_CONTRACT));
        Assert.assertTrue(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.FAQ));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.CONNECTIVITY));

        authTest.testVerifyUserCanLogoutFromExtranet(session);
    }

    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "ManageUser")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyMenuForUserFinance() {
        Map<String, String> param = getParam("testVerifyMenuForUserFinance");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        SideMenu<?> sideMenu = new SideMenu<>(session);
        Navbar<?> navbar = new Navbar<>(session);

        AuthTest authTest = new AuthTest();

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Verify menu item");
        Assert.assertTrue(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.HOME));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.PROPERTY));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.RATES_AND_AVAILABILITY));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.PROMO_AND_CAMPAIGN));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.ADDONS));
        Assert.assertTrue(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.BOOKINGS));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.CHAT));
        Assert.assertTrue(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.ANALYTICS));
        Assert.assertTrue(sideMenu.isSubmenuDisplayed(SideMenu.SidebarSubmenu.PREFERRED_PARTNER));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.GUEST_REVIEWS));
        Assert.assertTrue(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.FINANCE));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.ACTIVITY_LOG));
        Assert.assertTrue(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.MANAGE_USERS));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.PROPERTY_CONTRACT));
        Assert.assertTrue(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.FAQ));
        Assert.assertFalse(sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.CONNECTIVITY));
    }

    @DataProvider(name = "testVerifyUserCanLogoutFromExtranetDataProvider")
    public Object[][] testVerifyUserCanLogoutFromExtranetDataProvider() {
        return getObjectParams("testVerifyUserCanLogoutFromExtranet");
    }

    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "ManageUser")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "testVerifyUserCanLogoutFromExtranetDataProvider", priority=1)
    public void testVerifyUserCanLogoutFromExtranet(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);
        Navbar<?> navbar = new Navbar<>(session);

        String email = map.get("email");
        String password = map.get("password");
        String hotelName = map.get("hotelName");

        AuthTest authTest = new AuthTest();

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);
    }
}