package com.tiket.web.dashboard.test.hotel.extranet;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.extranet.ExclusivePrivateDealsPage;
import com.tiket.web.dashboard.page.hotel.extranet.Navbar;
import com.tiket.web.dashboard.page.hotel.extranet.SideMenu;
import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.is;

public class ExclusivePrivateDealsTest extends BaseTest {
    @Author(names = {"Raihan", "Vipul Kumar"})
    @Module(name = "EPD")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testEPDUpdateCombinable() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        ExclusivePrivateDealsPage<?> exclusivePrivateDealsPage = new ExclusivePrivateDealsPage<>(session);

        Map<String, String> param = getParam("testEPDUpdateCombinable");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");

        AuthTest authTest = new AuthTest();
        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to EPD menu page");
        sideMenu.clickPromoAndCampaignMenu();
        sideMenu.clickExclusivePrivateDealsMenu();
        Assertion.assertThat("EPD page is loaded", exclusivePrivateDealsPage.isEPDPageDisplayed(), is(true));

        step("Set combinable false and submit");
        exclusivePrivateDealsPage.selectCombinableDiscount(false);
        exclusivePrivateDealsPage.clickSave();
        Assertion.assertThat("Successful snackbar is appeared", exclusivePrivateDealsPage.isSuccessfulSnackbarDisplayed(), is(true));

        exclusivePrivateDealsPage.refreshPage();

        step("Set combinable true and submit");
        exclusivePrivateDealsPage.selectCombinableDiscount(true);
        exclusivePrivateDealsPage.clickSave();
        Assertion.assertThat("Successful snackbar is appeared", exclusivePrivateDealsPage.isSuccessfulSnackbarDisplayed(), Matchers.is(true));
    }

    @DataProvider(name = "testEPDUpdateStatus")
    public Object[][] testEPDUpdateStatusDataProvider() {
        return getObjectParams("testEPDUpdateStatus");
    }

    @Author(names = {"Raihan", "Vipul Kumar"})
    @Module(name = "EPD")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "testEPDUpdateStatus")
    public void testEPDUpdateStatus(Map<String, String> param) {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        ExclusivePrivateDealsPage<?> exclusivePrivateDealsPage = new ExclusivePrivateDealsPage<>(session);

        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        boolean isActive = Boolean.parseBoolean(param.get("isActive"));

        AuthTest authTest = new AuthTest();
        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to EPD menu page");
        sideMenu.clickPromoAndCampaignMenu();
        sideMenu.clickExclusivePrivateDealsMenu();
        Assertion.assertThat("EPD page is loaded", exclusivePrivateDealsPage.isEPDPageDisplayed(), is(true));

        step("Switch EPD status toggle");
        exclusivePrivateDealsPage.switchStatusToggle(isActive);
        exclusivePrivateDealsPage.clickSave();
        Assertion.assertThat("Successful snackbar is appeared", exclusivePrivateDealsPage.isSuccessfulSnackbarDisplayed(), is(true));
    }

    @Author(names = {"Ketan", "Vipul Kumar"})
    @Module(name = "EPD")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test

    public void testUserAbleToUseDiscountSlider() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        ExclusivePrivateDealsPage<?> exclusivePrivateDealsPage = new ExclusivePrivateDealsPage<>(session);

        Map<String, String> param = getParam("testUserAbleToUseDiscountSlider");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String minDiscount = param.get("minDiscount");
        String discount = param.get("discount");
        boolean isActive = Boolean.parseBoolean(param.get("isActive"));

        AuthTest authTest = new AuthTest();
        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to EPD menu page");
        sideMenu.clickPromoAndCampaignMenu();
        sideMenu.clickExclusivePrivateDealsMenu();
        Assertion.assertThat("EPD page is loaded", exclusivePrivateDealsPage.isEPDPageDisplayed(), is(true));

        step("Switch EPD status toggle");
        exclusivePrivateDealsPage.switchStatusToggle(isActive);
        exclusivePrivateDealsPage.clickSave();
        Assertion.assertThat("Successful snackbar is appeared", exclusivePrivateDealsPage.isSuccessfulSnackbarDisplayed(), is(true));

        step("Change the Discount Values");
        exclusivePrivateDealsPage.enterMinDiscountValue(minDiscount);
        exclusivePrivateDealsPage.enterDiscountValue(discount);
        exclusivePrivateDealsPage.selectCombinableDiscount(true);
        exclusivePrivateDealsPage.clickSave();
        Assertion.assertThat("Successful snackbar is appeared", exclusivePrivateDealsPage.isSuccessfulSnackbarDisplayed(), Matchers.is(true));

    }
}
