package com.tiket.web.dashboard.test.hotel.extranet;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.extranet.AddOnsPage;
import com.tiket.web.dashboard.page.hotel.extranet.Navbar;
import com.tiket.web.dashboard.page.hotel.extranet.SideMenu;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class AddOnsTest extends BaseTest {
    @Author(names = {"Raihan", "Vipul Kumar"})
    @Module(name = "AddOns")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testAddOnsCreatePerItemType() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        AddOnsPage<?> addOnsPage = new AddOnsPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        Map<String, String> param = getParam("testAddOnsCreatePerItemType");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String nameEn = param.get("nameEn");
        String descEn = param.get("descEn");
        String nameId = param.get("nameId");
        String descId = param.get("descId");
        String category = param.get("category");
        String subcategory = param.get("subcategory");
        String pricingType = param.get("pricingType");
        int price = Integer.parseInt(param.get("price"));
        int minQty = Integer.parseInt(param.get("minQty"));
        int maxQty = Integer.parseInt(param.get("maxQty"));
        boolean isActive = Boolean.parseBoolean(param.get("isActive"));
        int photoIndex = Integer.parseInt(param.get("photoIndex"));

        AuthTest authTest = new AuthTest();
        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to add ons list page");
        sideMenu.clickAddOnsMenu();
        sideMenu.clickAddOnsListSubmenu();
        Assertion.assertThat("Add ons list page is loaded", addOnsPage.isAddOnsPageDisplayed(), is(true));

        step("Create new add ons");
        addOnsPage.clickCreateNewAddOns();
        Assertion.assertThat("Add ons form page is loaded", addOnsPage.isCreateAddOnsPageDisplayed(), is(true));
        addOnsPage.enterAddOnsNameByLanguage("EN", nameEn);
        addOnsPage.enterAddOnsNameByLanguage("ID", nameId);
        addOnsPage.selectAddOnsCategoryAndSubcategory(category, subcategory);
        addOnsPage.selectAndEnterAddOnsPrice(pricingType, price);
        addOnsPage.enterMinAndMaxAddOnsQuantity(minQty, maxQty);
        addOnsPage.selectAddOnsAvailability(isActive);
        addOnsPage.addAddOnsPhotoByIndex(photoIndex);
        addOnsPage.enterAddOnsDescriptionByLanguage("EN", descEn);
        addOnsPage.enterAddOnsDescriptionByLanguage("ID", descId);
        addOnsPage.submitAddOnsForm();
        Assertion.assertThat("Successful snackbar is appeared", addOnsPage.isSuccessSnackbarAppeared(), is(true));

        step("Verify add ons is presented on the list");
        addOnsPage.enterFilterAddOnsName(nameId);
        addOnsPage.submitFilterAddOns();
        Assertion.assertThat("Add ons is presented on the list", addOnsPage.isAddOnsSearchResultFoundByName(nameId), is(true));
    }

    @Author(names = {"Raihan", "Vipul Kumar"})
    @Module(name = "AddOns")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test(dependsOnMethods = {"testAddOnsCreatePerItemType"}, priority = 1)
    public void testAddOnsEdit() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        AddOnsPage<?> addOnsPage = new AddOnsPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        Map<String, String> param = getParam("testAddOnsEdit");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String newNameEn = param.get("newNameEn");
        String newNameId = param.get("newNameId");
        String oldNameEn = param.get("oldNameEn");
        String oldNameId = param.get("oldNameId");
        String pricingType = param.get("pricingType");
        int newPrice = Integer.parseInt(param.get("newPrice"));
        int oldPrice = Integer.parseInt(param.get("oldPrice"));

        AuthTest authTest = new AuthTest();
        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to add ons list page");
        sideMenu.clickAddOnsMenu();
        sideMenu.clickAddOnsListSubmenu();
        Assertion.assertThat("Add ons list page is loaded", addOnsPage.isAddOnsPageDisplayed(), is(true));

        step("Search add ons");
        addOnsPage.enterFilterAddOnsName(oldNameId);
        addOnsPage.submitFilterAddOns();
        Assertion.assertThat("Add ons is presented on the list", addOnsPage.isAddOnsSearchResultFoundByName(oldNameId), is(true));

        step("Edit add ons");
        addOnsPage.clickAddOnsActionByName("EDIT", oldNameId);
        Assertion.assertThat("Add ons form page is loaded", addOnsPage.isCreateAddOnsPageDisplayed(), is(true));
        addOnsPage.enterAddOnsNameByLanguage("EN", newNameEn);
        addOnsPage.enterAddOnsNameByLanguage("ID", newNameId);
        addOnsPage.selectAndEnterAddOnsPrice(pricingType, newPrice);
        addOnsPage.submitAddOnsForm();
        Assertion.assertThat("Successful snackbar is appeared", addOnsPage.isSuccessSnackbarAppeared(), is(true));

        step("Verify add ons is presented on the list");
        addOnsPage.enterFilterAddOnsName(newNameId);
        addOnsPage.submitFilterAddOns();
        Assertion.assertThat("Add ons is presented on the list", addOnsPage.isAddOnsSearchResultFoundByName(newNameId), is(true));

        step("Revert test data");
        addOnsPage.clickAddOnsActionByName("EDIT", newNameId);
        Assertion.assertThat("Add ons form page is loaded", addOnsPage.isCreateAddOnsPageDisplayed(), is(true));
        addOnsPage.enterAddOnsNameByLanguage("EN", oldNameEn);
        addOnsPage.enterAddOnsNameByLanguage("ID", oldNameId);
        addOnsPage.selectAndEnterAddOnsPrice(pricingType, oldPrice);
        addOnsPage.submitAddOnsForm();

        step("Verify add ons is presented on the list");
        addOnsPage.enterFilterAddOnsName(oldNameId);
        addOnsPage.submitFilterAddOns();
        Assertion.assertThat("Add ons is presented on the list", addOnsPage.isAddOnsSearchResultFoundByName(oldNameId), is(true));
    }

    @DataProvider(name = "testAddOnsUpdateStatus")
    public Object[][] testAddOnsUpdateStatusDataProvider() {
        return getObjectParams("testAddOnsUpdateStatus");
    }

    @Author(names = {"Raihan", "Vipul Kumar"})
    @Module(name = "AddOns")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "testAddOnsUpdateStatus")
    public void testAddOnsUpdateStatus(Map<String, String> param) {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        AddOnsPage<?> addOnsPage = new AddOnsPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String nameId = param.get("nameId");
        boolean isActive = Boolean.parseBoolean(param.get("isActive"));

        AuthTest authTest = new AuthTest();
        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to add ons list page");
        sideMenu.clickAddOnsMenu();
        sideMenu.clickAddOnsListSubmenu();
        Assertion.assertThat("Add ons list page is loaded", addOnsPage.isAddOnsPageDisplayed(), is(true));

        step("Search add ons");
        addOnsPage.enterFilterAddOnsName(nameId);
        addOnsPage.submitFilterAddOns();
        Assertion.assertThat("Add ons is presented on the list", addOnsPage.isAddOnsSearchResultFoundByName(nameId), is(true));

        step("Switch add ons toggle");
        addOnsPage.switchAddOnsToggleByName(nameId, isActive);
    }

    @Author(names = {"Raihan", "Vipul Kumar"})
    @Module(name = "AddOns")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testAddOnsDelete() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        AddOnsPage<?> addOnsPage = new AddOnsPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        Map<String, String> param = getParam("testAddOnsDelete");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String nameId = param.get("nameId");

        AuthTest authTest = new AuthTest();
        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to add ons list page");
        sideMenu.clickAddOnsMenu();
        sideMenu.clickAddOnsListSubmenu();
        Assertion.assertThat("Add ons list page is loaded", addOnsPage.isAddOnsPageDisplayed(), is(true));

        step("Search add ons");
        addOnsPage.enterFilterAddOnsName(nameId);
        addOnsPage.submitFilterAddOns();
        Assertion.assertThat("Add ons is presented on the list", addOnsPage.isAddOnsSearchResultFoundByName(nameId), is(true));

        step("Delete add ons");
        addOnsPage.clickAddOnsActionByName("DELETE", nameId);
        addOnsPage.confirmDeleteAddOns();
        addOnsPage.closeSuccessDeleteAddOnsModal();

        step("Verify add ons is NOT presented on the list");
        addOnsPage.enterFilterAddOnsName(nameId);
        addOnsPage.submitFilterAddOns();
        Assertion.assertThat("Add ons is NOT presented on the list", addOnsPage.isAddOnsSearchResultFoundByName(nameId), is(false));
    }

    @Author(names = {"Ketan", "Vipul Kumar"})
    @Module(name = "AddOns")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testFilterByNameAddOns() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        AddOnsPage<?> addOnsPage = new AddOnsPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        Map<String, String> param = getParam("testFilterByNameAddOns");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String nameId = param.get("nameId");

        AuthTest authTest = new AuthTest();
        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to add ons list page");
        sideMenu.clickAddOnsMenu();
        sideMenu.clickAddOnsListSubmenu();
        Assertion.assertThat("Add ons list page is loaded", addOnsPage.isAddOnsPageDisplayed(), is(true));

        step("Search add ons");
        addOnsPage.enterFilterAddOnsName(nameId);
        addOnsPage.submitFilterAddOns();
        Assertion.assertThat("Add ons is presented on the list", addOnsPage.isAddOnsSearchResultFoundByName(nameId), is(true));

    }

}
