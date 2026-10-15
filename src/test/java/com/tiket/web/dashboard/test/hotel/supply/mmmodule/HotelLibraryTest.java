package com.tiket.web.dashboard.test.hotel.supply.mmmodule;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.accommodation.internal_supplier.dashboard.InternalSupplierDashboardPage;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.EditPropertyPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.HotelLibraryPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.LoginPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class HotelLibraryTest extends BaseTest {

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryGetListOfCountry() {
        Map<String, String> param = getParam("testHotelLibraryGetListOfCountry");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String searchType = param.get("searchType");
        String country = param.get("country");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step(String.format("Select %s as search type", searchType));
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step(String.format("Search %s as country", country));
        hotelLibraryPage.searchCountry(country);
        Assertion.assertThat(String.format("Country %s is displayed", country), hotelLibraryPage.isCountryDisplayed(country), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryGetListOfHotelByCountry() {
        Map<String, String> param = getParam("testHotelLibraryGetListOfHotelByCountry");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String searchType = param.get("searchType");
        String country = param.get("country");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step(String.format("Select %s as search type", searchType));
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step(String.format("Select %s as country", country));
        hotelLibraryPage.searchAndSelectCountry(country);
        Assertion.assertThat(String.format("Country selected: %s", country), hotelLibraryPage.isCountrySelected(country), is(true));

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryGetListOfHotelByFreeDistrict() {
        Map<String, String> param = getParam("testHotelLibraryGetListOfHotelByFreeDistrict");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String searchType = param.get("searchType");
        String freeDistrict = param.get("freeDistrict");
        String city = param.get("city");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step(String.format("Select %s as search type", searchType));
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step(String.format("Select %s as free district", freeDistrict));
        hotelLibraryPage.searchAndSelectFreeDistrict(freeDistrict);
        Assertion.assertThat(String.format("Free district selected: %s", freeDistrict), hotelLibraryPage.isFreeDistrictSelected(freeDistrict), is(true));

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", city), hotelLibraryPage.isSearchResultCityLabelDisplayed(city), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryGetListOfHotelByPropertyNameFromOfficialDistrict() {
        Map<String, String> param = getParam("testHotelLibraryGetListOfHotelByPropertyNameFromOfficialDistrict");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String searchType = param.get("searchType");
        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");
        String propertyName = param.get("propertyName");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step(String.format("Select %s as search type", searchType));
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step("Select location");
        hotelLibraryPage.searchAndSelectCountry(country);
        hotelLibraryPage.searchAndSelectRegion(region);
        hotelLibraryPage.searchAndSelectCity(city);
        hotelLibraryPage.searchAndSelectArea(area);

        step(String.format("Input %s as property name", propertyName));
        hotelLibraryPage.inputPropertyName(propertyName);

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryGetListOfHotelByVendorNameAndVendorExternalId() {
        Map<String, String> param = getParam("testHotelLibraryGetListOfHotelByVendorNameAndVendorExternalId");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Change tab to search by vendor Id");
        hotelLibraryPage.clickSearchByVendorId();

        step(String.format("Select %s as vendor name", vendorName));
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));

        step(String.format("Input %s as vendor external ID", vendorExternalId));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryGetListOfHotelByPropertyNameFromFreeDistrict() {
        Map<String, String> param = getParam("testHotelLibraryGetListOfHotelByPropertyNameFromFreeDistrict");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String searchType = param.get("searchType");
        String freeDistrict = param.get("freeDistrict");
        String propertyName = param.get("propertyName");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step(String.format("Select %s as search type", searchType));
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step(String.format("Select %s as free district", freeDistrict));
        hotelLibraryPage.searchAndSelectFreeDistrict(freeDistrict);
        Assertion.assertThat(String.format("Free district selected: %s", freeDistrict), hotelLibraryPage.isFreeDistrictSelected(freeDistrict), is(true));

        step(String.format("Input %s as property name", propertyName));
        hotelLibraryPage.inputPropertyName(propertyName);

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibrarySeePropertyDetailInformation() {
        Map<String, String> param = getParam("testHotelLibrarySeePropertyDetailInformation");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Change tab to search by vendor Id");
        hotelLibraryPage.clickSearchByVendorId();

        step(String.format("Select %s as vendor name", vendorName));
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));

        step(String.format("Input %s as vendor external ID", vendorExternalId));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and edit property button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickEditProperty();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("%s is displayed as hotel name", propertyName), editPropertyPage.isHotelNameDisplayed(propertyName), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditSourceOfPropertyName() {
        Map<String, String> param = getParam("testHotelLibraryEditSourceOfPropertyName");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String publicId = param.get("publicId");
        String vendorName2 = param.get("vendorName2");
        String propertyName2 = param.get("propertyName2");
        String publicId2 = param.get("publicId2");

        String username = param.get("username");
        String password = param.get("password");

        step("Activate Hotel Change Name Listener");
        String message = hotelLibraryPage.activateHotelChangeNameListener();
        Assertion.assertThat("Hotel Change Name Listener Activation is successful", message, is(message));


        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Change tab to search by vendor Id");
        hotelLibraryPage.clickSearchByVendorId();

        step(String.format("Select %s as vendor name", vendorName));
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));

        step(String.format("Input %s as vendor external ID", vendorExternalId));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and edit property button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickEditProperty();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        String urlForEditProperty = editPropertyPage.getUrlForEditCurrentProperty();
        Assertion.assertThat(String.format("%s is displayed as hotel name", propertyName), editPropertyPage.isHotelNameDisplayed(propertyName), is(true));

        step("Edit source of property name and submit");
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat("Edit general info page is loaded", editPropertyPage.isGeneralInfoPropertyNameDisplayed(propertyName), is(true));
        editPropertyPage.selectGeneralInfoPropertyNameSourceVendor(vendorName2);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));


        step("Revert source of property name back to initial state and submit");
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat("Edit general info page is loaded", editPropertyPage.isGeneralInfoPropertyNameDisplayed(propertyName2), is(true));
        editPropertyPage.selectGeneralInfoPropertyNameSourceVendor(vendorName);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(urlForEditProperty), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditSourceOfChainAndBrand() {
        Map<String, String> param = getParam("testHotelLibraryEditSourceOfChainAndBrand");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String vendorName2 = param.get("vendorName2");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Change tab to search by vendor Id");
        hotelLibraryPage.clickSearchByVendorId();

        step(String.format("Select %s as vendor name", vendorName));
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));

        step(String.format("Input %s as vendor external ID", vendorExternalId));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and edit property button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickEditProperty();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        String urlForEditProperty = editPropertyPage.getUrlForEditCurrentProperty();
        Assertion.assertThat(String.format("%s is displayed as hotel name", propertyName), editPropertyPage.isHotelNameDisplayed(propertyName), is(true));

        step("Edit source of property name and submit");
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat("Edit general info page is loaded", editPropertyPage.isGeneralInfoPropertyNameDisplayed(propertyName), is(true));
        editPropertyPage.selectGeneralInfoChainAndBrandSourceVendor(vendorName2);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat(String.format("%s is selected as source of chain and brand", vendorName2), editPropertyPage.isGeneralInfoChainAndBrandVendorNameDisplayed(vendorName2), is(true));

        step("Revert source of chain and brand back to initial state and submit");
        editPropertyPage.selectGeneralInfoChainAndBrandSourceVendor(vendorName);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(urlForEditProperty), is(true));
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat(String.format("%s is selected as source of chain and brand", vendorName), editPropertyPage.isGeneralInfoChainAndBrandVendorNameDisplayed(vendorName), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditSourceOfPropertyTypeAndCategory() {
        Map<String, String> param = getParam("testHotelLibraryEditSourceOfPropertyTypeAndCategory");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String vendorName2 = param.get("vendorName2");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Change tab to search by vendor Id");
        hotelLibraryPage.clickSearchByVendorId();

        step(String.format("Select %s as vendor name", vendorName));
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));

        step(String.format("Input %s as vendor external ID", vendorExternalId));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and edit property button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickEditProperty();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("%s is displayed as hotel name", propertyName), editPropertyPage.isHotelNameDisplayed(propertyName), is(true));

        step("Edit source of property type and category and submit");
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat("Edit general info page is loaded", editPropertyPage.isGeneralInfoPropertyNameDisplayed(propertyName), is(true));
        editPropertyPage.selectGeneralInfoPropertyTypeAndCategorySourceVendor(vendorName2);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat(String.format("%s is selected as source of property type and category", vendorName2), editPropertyPage.isGeneralInfoPropertyTypeAndCategoryVendorNameDisplayed(vendorName2), is(true));

        step("Revert source of property type and category back to initial state and submit");
        editPropertyPage.selectGeneralInfoPropertyTypeAndCategorySourceVendor(vendorName);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat(String.format("%s is selected as source of property type and category", vendorName), editPropertyPage.isGeneralInfoPropertyTypeAndCategoryVendorNameDisplayed(vendorName), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditSourceOfPropertyStar() {
        Map<String, String> param = getParam("testHotelLibraryEditSourceOfPropertyStar");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String vendorName2 = param.get("vendorName2");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Change tab to search by vendor Id");
        hotelLibraryPage.clickSearchByVendorId();

        step(String.format("Select %s as vendor name", vendorName));
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));

        step(String.format("Input %s as vendor external ID", vendorExternalId));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and edit property button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickEditProperty();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("%s is displayed as hotel name", propertyName), editPropertyPage.isHotelNameDisplayed(propertyName), is(true));

        step("Edit source of property star and submit");
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat("Edit general info page is loaded", editPropertyPage.isGeneralInfoPropertyNameDisplayed(propertyName), is(true));
        editPropertyPage.selectGeneralInfoPropertyStarSourceVendor(vendorName2);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat(String.format("%s is selected as source of property star", vendorName2), editPropertyPage.isGeneralInfoPropertyStarVendorNameDisplayed(vendorName2), is(true));

        step("Revert source of property star back to initial state and submit");
        editPropertyPage.selectGeneralInfoPropertyStarSourceVendor(vendorName);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat(String.format("%s is selected as source of property star", vendorName), editPropertyPage.isGeneralInfoPropertyStarVendorNameDisplayed(vendorName), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditSourceOfGuestTypePreferences() {
        Map<String, String> param = getParam("testHotelLibraryEditSourceOfGuestTypePreferences");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String vendorName2 = param.get("vendorName2");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Change tab to search by vendor Id");
        hotelLibraryPage.clickSearchByVendorId();

        step(String.format("Select %s as vendor name", vendorName));
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));

        step(String.format("Input %s as vendor external ID", vendorExternalId));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and edit property button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickEditProperty();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("%s is displayed as hotel name", propertyName), editPropertyPage.isHotelNameDisplayed(propertyName), is(true));

        step("Edit source of guest type preferences and submit");
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat("Edit general info page is loaded", editPropertyPage.isGeneralInfoPropertyNameDisplayed(propertyName), is(true));
        editPropertyPage.selectGeneralInfoGuestTypePreferencesSourceVendor(vendorName2);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat(String.format("%s is selected as source of guest type preferences", vendorName2), editPropertyPage.isGeneralInfoGuestTypePreferencesVendorNameDisplayed(vendorName2), is(true));

        step("Revert source of guest type preferences back to initial state and submit");
        editPropertyPage.selectGeneralInfoGuestTypePreferencesSourceVendor(vendorName);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat(String.format("%s is selected as source of guest type preferences", vendorName), editPropertyPage.isGeneralInfoGuestTypePreferencesVendorNameDisplayed(vendorName), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryGetListOfHotelByRegion() {
        Map<String, String> param = getParam("testHotelLibraryGetListOfHotelByRegion");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String searchType = param.get("searchType");
        String country = param.get("country");
        String region = param.get("region");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step(String.format("Select %s as search type", searchType));
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step(String.format("Select %s as country", country));
        hotelLibraryPage.searchAndSelectCountry(country);
        Assertion.assertThat(String.format("Country selected: %s", country), hotelLibraryPage.isCountrySelected(country), is(true));

        step(String.format("Select %s as region", region));
        hotelLibraryPage.searchAndSelectRegion(region);
        Assertion.assertThat(String.format("Region selected: %s", country), hotelLibraryPage.isRegionSelected(region), is(true));

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Verify search result");
        Assertion.assertThat(String.format("Property from region %s are displayed", region), hotelLibraryPage.isSearchResultRegionMatched(region), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryGetListOfHotelByCity() {
        Map<String, String> param = getParam("testHotelLibraryGetListOfHotelByCity");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String searchType = param.get("searchType");
        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step(String.format("Select %s as search type", searchType));
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step(String.format("Select %s as country", country));
        hotelLibraryPage.searchAndSelectCountry(country);
        Assertion.assertThat(String.format("Country selected: %s", country), hotelLibraryPage.isCountrySelected(country), is(true));

        step(String.format("Select %s as region", region));
        hotelLibraryPage.searchAndSelectRegion(region);
        Assertion.assertThat(String.format("Region selected: %s", region), hotelLibraryPage.isRegionSelected(region), is(true));

        step(String.format("Select %s as city", city));
        hotelLibraryPage.searchAndSelectCity(city);
        Assertion.assertThat(String.format("City selected: %s", city), hotelLibraryPage.isCitySelected(city), is(true));

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Verify search result");
        Assertion.assertThat(String.format("Property from city %s are displayed", city), hotelLibraryPage.isSearchResultCityMatched(city), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "testHotelLibraryGetListOfHotelByStatusAndDistrictTypeDataProvider")
    public void testHotelLibraryGetListOfHotelByStatusAndDistrictType(Map<String, String> param) {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String searchType = param.get("searchType");
        String country = param.get("country");
        boolean hotelStatus = Boolean.parseBoolean(param.get("hotelStatus"));

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step(String.format("Select %s as search type", searchType));
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step(String.format("Select %s as country", country));
        hotelLibraryPage.searchAndSelectCountry(country);
        Assertion.assertThat(String.format("Country selected: %s", country), hotelLibraryPage.isCountrySelected(country), is(true));

        step("Select hotel status");
        hotelLibraryPage.selectFilterHotelStatus(hotelStatus);
        Assertion.assertThat("Hotel status selected", hotelLibraryPage.isHotelStatusSelected(hotelStatus), is(true));

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Verify search result");
        Assertion.assertThat("List of hotel is displayed based on status correctly", hotelLibraryPage.isSearchResultHotelStatusMatched(hotelStatus), is(true));
    }

    @DataProvider
    public static Object[][] testHotelLibraryGetListOfHotelByStatusAndDistrictTypeDataProvider() {
        return getObjectParams("testHotelLibraryGetListOfHotelByStatusAndDistrictType");
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "testHotelLibraryGetListOfHotelByChainAndDistrictTypeDataProvider")
    public void testHotelLibraryGetListOfHotelByChainAndDistrictType(Map<String, String> param) {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String searchType = param.get("searchType");
        String country = param.get("country");
        String chain = param.get("chain");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step(String.format("Select %s as search type", searchType));
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step(String.format("Select %s as country", country));
        hotelLibraryPage.searchAndSelectCountry(country);
        Assertion.assertThat(String.format("Country selected: %s", country), hotelLibraryPage.isCountrySelected(country), is(true));

        step(String.format("Select %s as chain", chain));
        hotelLibraryPage.searchAndSelectChain(chain);
        Assertion.assertThat(String.format("Chain selected: %s", chain), hotelLibraryPage.isChainSelected(chain), is(true));

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Verify search result");
        Assertion.assertThat("List of hotel is displayed based on chain correctly", hotelLibraryPage.isSearchResultChainMatched(chain), is(true));
    }

    @DataProvider
    public static Object[][] testHotelLibraryGetListOfHotelByChainAndDistrictTypeDataProvider() {
        return getObjectParams("testHotelLibraryGetListOfHotelByChainAndDistrictType");
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryGetListOfRegion() {
        Map<String, String> param = getParam("testHotelLibraryGetListOfRegion");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String searchType = param.get("searchType");
        String country = param.get("country");
        String region = param.get("region");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step(String.format("Select %s as search type", searchType));
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step(String.format("Select %s as country", country));
        hotelLibraryPage.searchAndSelectCountry(country);
        Assertion.assertThat(String.format("Country selected: %s", country), hotelLibraryPage.isCountrySelected(country), is(true));

        step(String.format("Select %s as region", region));
        hotelLibraryPage.searchAndSelectRegion(region);
        Assertion.assertThat(String.format("Region selected: %s", country), hotelLibraryPage.isRegionSelected(region), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryGetListOfCity() {
        Map<String, String> param = getParam("testHotelLibraryGetListOfCity");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String searchType = param.get("searchType");
        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step(String.format("Select %s as search type", searchType));
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step(String.format("Select %s as country", country));
        hotelLibraryPage.searchAndSelectCountry(country);
        Assertion.assertThat(String.format("Country selected: %s", country), hotelLibraryPage.isCountrySelected(country), is(true));

        step(String.format("Select %s as region", region));
        hotelLibraryPage.searchAndSelectRegion(region);
        Assertion.assertThat(String.format("Region selected: %s", region), hotelLibraryPage.isRegionSelected(region), is(true));

        step(String.format("Select %s as city", city));
        hotelLibraryPage.searchAndSelectCity(city);
        Assertion.assertThat(String.format("City selected: %s", city), hotelLibraryPage.isCitySelected(city), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryGetListOfHotelByArea() {
        Map<String, String> param = getParam("testHotelLibraryGetListOfHotelByArea");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String searchType = param.get("searchType");
        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step(String.format("Select %s as search type", searchType));
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step(String.format("Select %s as country", country));
        hotelLibraryPage.searchAndSelectCountry(country);
        Assertion.assertThat(String.format("Country selected: %s", country), hotelLibraryPage.isCountrySelected(country), is(true));

        step(String.format("Select %s as region", region));
        hotelLibraryPage.searchAndSelectRegion(region);
        Assertion.assertThat(String.format("Region selected: %s", region), hotelLibraryPage.isRegionSelected(region), is(true));

        step(String.format("Select %s as city", city));
        hotelLibraryPage.searchAndSelectCity(city);
        Assertion.assertThat(String.format("City selected: %s", city), hotelLibraryPage.isCitySelected(city), is(true));

        step(String.format("Select %s as area", area));
        hotelLibraryPage.searchAndSelectArea(area);
        Assertion.assertThat(String.format("Area selected: %s", area), hotelLibraryPage.isAreaSelected(area), is(true));

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Verify search result");
        Assertion.assertThat(String.format("Property from area %s are displayed", area), hotelLibraryPage.isSearchResultAreaMatched(area), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryGetListOfArea() {
        Map<String, String> param = getParam("testHotelLibraryGetListOfArea");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String searchType = param.get("searchType");
        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step(String.format("Select %s as search type", searchType));
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step(String.format("Select %s as country", country));
        hotelLibraryPage.searchAndSelectCountry(country);
        Assertion.assertThat(String.format("Country selected: %s", country), hotelLibraryPage.isCountrySelected(country), is(true));

        step(String.format("Select %s as region", region));
        hotelLibraryPage.searchAndSelectRegion(region);
        Assertion.assertThat(String.format("Region selected: %s", region), hotelLibraryPage.isRegionSelected(region), is(true));

        step(String.format("Select %s as city", city));
        hotelLibraryPage.searchAndSelectCity(city);
        Assertion.assertThat(String.format("City selected: %s", city), hotelLibraryPage.isCitySelected(city), is(true));

        step(String.format("Select %s as area", area));
        hotelLibraryPage.searchAndSelectArea(area);
        Assertion.assertThat(String.format("Area selected: %s", area), hotelLibraryPage.isAreaSelected(area), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryGetListOfChainAndDistrictType() {
        Map<String, String> param = getParam("testHotelLibraryGetListOfChainAndDistrictType");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String searchType = param.get("searchType");
        String country = param.get("country");
        String chain = param.get("chain");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step(String.format("Select %s as search type", searchType));
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step(String.format("Select %s as country", country));
        hotelLibraryPage.searchAndSelectCountry(country);
        Assertion.assertThat(String.format("Country selected: %s", country), hotelLibraryPage.isCountrySelected(country), is(true));

        step(String.format("Select %s as chain", chain));
        hotelLibraryPage.searchAndSelectChain(chain);
        Assertion.assertThat(String.format("Chain selected: %s", chain), hotelLibraryPage.isChainSelected(chain), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test (dataProvider = "testHotelLibraryEditTiketCleanStatusDataProvider")
    public void testHotelLibraryEditTiketCleanStatus(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        boolean expectedStatus = Boolean.parseBoolean(param.get("expectedStatus"));

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Change tab to search by vendor Id");
        hotelLibraryPage.clickSearchByVendorId();

        step(String.format("Select %s as vendor name", vendorName));
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));

        step(String.format("Input %s as vendor external ID", vendorExternalId));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and edit property button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickEditProperty();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("%s is displayed as hotel name", propertyName), editPropertyPage.isHotelNameDisplayed(propertyName), is(true));

        step("Switch off Tiket CLEAN status toggle");
        editPropertyPage.clickEditGeneralInfo();
        Assertion.assertThat("Edit general info page is loaded", editPropertyPage.isGeneralInfoPropertyNameDisplayed(propertyName), is(true));
        editPropertyPage.switchTiketCleanStatusToggle(expectedStatus);
        Assertion.assertThat(String.format("Tiket CLEAN status toggle is %s", expectedStatus), editPropertyPage.isGeneralInfoTiketCleanStatusToggleDisplayed(expectedStatus), is(true));

        step("Submit edit property form");
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));

        step("Verify Tiket CLEAN status label");
        Assertion.assertThat(String.format("Tiket CLEAN status is %s", expectedStatus), editPropertyPage.isHotelTiketCleanStatusDisplayed(expectedStatus), is(true));
    }

    @DataProvider(name = "testHotelLibraryEditTiketCleanStatusDataProvider")
    public Object[][] testHotelLibraryEditTiketCleanStatusDataProvider() {
        return getObjectParams("testHotelLibraryEditTiketCleanStatus");
    }

    @Author(names = {"Manan","Riya Bajpai"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "verifyGetHotelListByFreeDistrictAndEditPropertySourcesDataProvider")
    public void verifyGetHotelListByFreeDistrictAndEditPropertySources(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String propertyName = param.get("propertyName");
        String sourceVendor = param.get("sourceVendor");
        String starSourceVendor = param.get("starSourceVendor");
        String facilityName = param.get("facilityName");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));

        step("Filter Hotel Library by free district");
        hotelLibraryPage.selectSearchType("free");
        Assertion.assertThat("Free district search type is selected", hotelLibraryPage.isSearchTypeSelected("free"), is(true));

        step(String.format("Input property name: %s", propertyName));
        hotelLibraryPage.inputPropertyName(propertyName);

        step("Click search");
        hotelLibraryPage.clickSearch();

        step("Check the state of Hotel Status Toggle");
        if (!hotelLibraryPage.isOnToggleState(propertyName)) {

            step("Toggle Hotel Status is OFF so make it ON and validate");
            hotelLibraryPage.clickResultHotelStatusToggle(propertyName);
            Assertion.assertThat("Hotel Status toggle is ON", hotelLibraryPage.isResultHotelStatusToggleOn(propertyName), is(true));
        } else {
            step("Hotel Status toggle is Already ON so toggle it OFF and back ON");
            Assertion.assertThat("Hotel Status toggle is Already ON", hotelLibraryPage.isOnToggleState(propertyName), is(true));

            step("Toggle Hotel Status OFF - select Temporarily Closed reason and deactivate");
            hotelLibraryPage.clickResultHotelStatusToggle(propertyName);

            step("Select Temporarily Closed reason and deactivate");
            hotelLibraryPage.selectHotelStatusDeactivateReasonTemporarilyClosed();
            hotelLibraryPage.clickDeactivateHotelStatus();

            step("Verify Hotel Status deactivated successfully");
            Assertion.assertThat("Is Toggle off", hotelLibraryPage.isOffToggleState(propertyName), is(true));

            step("Make Toggle Hotel Status ON and validate");
            hotelLibraryPage.clickResultHotelStatusToggle(propertyName);
            Assertion.assertThat("Hotel Status toggle is ON", hotelLibraryPage.isResultHotelStatusToggleOn(propertyName), is(true));
        }

        step("Check the state of Show Alternative Allotment Toggle");
        if (!hotelLibraryPage.isResultShowAlternativeAllotmentToggleOn(propertyName)) {

            step("Show Alternative Allotment toggle is OFF so make it ON and validate");
            hotelLibraryPage.clickResultShowAlternativeAllotmentToggle(propertyName);
            Assertion.assertThat("Show Alternative Allotment toggle is ON", hotelLibraryPage.isResultShowAlternativeAllotmentToggleOn(propertyName), is(true));
        }
        else {
            step("Show Alternative Allotment toggle is Already ON so toggle it OFF and back ON");
            Assertion.assertThat("Show Alternative Allotment toggle is Already ON", hotelLibraryPage.isResultShowAlternativeAllotmentToggleOn(propertyName), is(true));

            step("Toggle Show Alternative Allotment OFF and validate");
            hotelLibraryPage.clickResultShowAlternativeAllotmentToggle(propertyName);
            Assertion.assertThat("Show Alternative Allotment toggle is OFF", hotelLibraryPage.isResultShowAlternativeAllotmentToggleOn(propertyName), is(false));

            step("Make Toggle Show Alternative Allotment ON and validate");
            hotelLibraryPage.clickResultShowAlternativeAllotmentToggle(propertyName);
            Assertion.assertThat("Show Alternative Allotment toggle is ON", hotelLibraryPage.isResultShowAlternativeAllotmentToggleOn(propertyName), is(true));
        }

        step("Check the state of Vendor Status Toggle");
        if (!hotelLibraryPage.isResultVendorStatusToggleOn(propertyName)) {

            step("Vendor Status toggle is OFF so make it ON and validate");
            hotelLibraryPage.clickResultVendorStatusToggle(propertyName);
            Assertion.assertThat("Vendor Status toggle is ON", hotelLibraryPage.isResultVendorStatusToggleOn(propertyName), is(true));
        }
        else {
            step("Vendor Status toggle is Already ON so toggle it OFF and back ON");
            Assertion.assertThat("Vendor Status toggle is Already ON", hotelLibraryPage.isResultVendorStatusToggleOn(propertyName), is(true));

            step("Toggle Vendor Status OFF and validate");
            hotelLibraryPage.clickResultVendorStatusToggle(propertyName);
            Assertion.assertThat("Vendor Status toggle is OFF", hotelLibraryPage.isResultVendorStatusToggleOn(propertyName), is(false));

            step("Make Toggle Vendor Status ON and validate");
            hotelLibraryPage.clickResultVendorStatusToggle(propertyName);
            Assertion.assertThat("Vendor Status toggle is ON", hotelLibraryPage.isResultVendorStatusToggleOn(propertyName), is(true));
        }

        step("Click Action");
        hotelLibraryPage.clickAction();

        step("Click Edit Property");
        hotelLibraryPage.clickEditProperty();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("%s is displayed as hotel name", propertyName), editPropertyPage.isHotelNameDisplayed(propertyName), is(true));

        step("Note current hotel star rating from detail view");
        String currentPropertyStar = editPropertyPage.getCurrentPropertyStarFromDetailView();
        String targetPropertyStar = currentPropertyStar.toLowerCase().contains("4") ? "3 Star" : "4 Star";

        step("Select source vendor for property name");
        editPropertyPage.clickEditGeneralInfo();
        editPropertyPage.selectGeneralInfoPropertyNameSourceVendor(sourceVendor);

        step(String.format("Set source to '%s', then change star from '%s' to '%s'", sourceVendor, currentPropertyStar, targetPropertyStar));
        editPropertyPage.selectGeneralInfoPropertyStarSourceVendor(starSourceVendor);
        editPropertyPage.selectGeneralInfoPropertyStarValue(targetPropertyStar);

        step("Submit Save property form");
        editPropertyPage.clickEditGeneralInfoSave();
        Assertion.assertThat("Save confirmation appeared", editPropertyPage.isEditGeneralInfoSaveConfirmationAppeared(), is(true));
        editPropertyPage.clickEditGeneralInfoSaveConfirmation();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));

        step(String.format("Verify hotel star changed from '%s' to '%s'", currentPropertyStar, targetPropertyStar));
        Assertion.assertThat(String.format("Hotel star is updated to %s", targetPropertyStar), editPropertyPage.isGeneralInfoPropertyStarValueDisplayed(targetPropertyStar), is(true));

        step("Note current Main Area from detail view");
        String currentMainArea = editPropertyPage.getCurrentMainAreaFromDetailView();
        String targetMainArea = currentMainArea.contains("Blok M") ? "Kebayoran Baru" : "Blok M";

        step("Click on Edit Location and Contact Info");
        editPropertyPage.clickEditLocationAndContactInfo();
        Assertion.assertThat("Edit location and contact info page is loaded", editPropertyPage.isPropertyLocationEditLongLatDisplayed(), is(true));

        step(String.format("Change Main Area from '%s' to '%s'", currentMainArea, targetMainArea));
        editPropertyPage.selectPropertyLocationMainArea(targetMainArea);

        step("Submit Save location form");
        editPropertyPage.clickEditLocationAndContactSave();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isEditLocationAndContactSaveConfirmationAppeared(), is(true));
        editPropertyPage.clickEditLocationAndContactSaveConfirmation();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));

        step(String.format("Verify Main Area changed from '%s' to '%s'", currentMainArea, targetMainArea));
        Assertion.assertThat(String.format("Main Area is updated to %s", targetMainArea), editPropertyPage.isPropertyLocationMainAreaDisplayed(targetMainArea), is(true));

        step("Store current Check-in and Check-out times from detail view");
        String currentCheckInStartTime = editPropertyPage.getCurrentCheckInStartTimeFromDetailView();
        String currentCheckInEndTime = editPropertyPage.getCurrentCheckInEndTimeFromDetailView();
        String currentCheckOutTime = editPropertyPage.getCurrentCheckOutTimeFromDetailView();
        String targetCheckInStartTime = currentCheckInStartTime.contains("12") ? "11:00" : "12:00";
        String targetCheckInEndTime = currentCheckInEndTime.contains("12") ? "11:00" : "12:00";
        String targetCheckOutTime = currentCheckOutTime.contains("12") ? "11:00" : "12:00";

        step("Click on Edit Property Description and Policy");
        editPropertyPage.clickEditPropertyDescriptionAndPolicy();
        Assertion.assertThat("Edit property description and policy page is loaded", editPropertyPage.isPropertyDescriptionAndPolicyPageDisplayed(), is(true));

        step(String.format("Select '%s' as Check-in & Check-out source", starSourceVendor));
        editPropertyPage.selectPropertyCheckInAndOutSourceVendor(starSourceVendor);

        step(String.format("Set Check-in time from '%s-%s' to '%s-%s'", currentCheckInStartTime, currentCheckInEndTime, targetCheckInStartTime, targetCheckInEndTime));
        editPropertyPage.setPropertyCheckInStartTime(targetCheckInStartTime);

        step(String.format("Set Check-out time from '%s' to '%s'", currentCheckOutTime, targetCheckOutTime));
        editPropertyPage.setPropertyCheckOutTime(targetCheckOutTime);

        step("Submit Save description & policy form");
        editPropertyPage.clickEditPropertyDescriptionAndPolicySave();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isEditPropertyDescriptionAndPolicySaveConfirmationAppeared(), is(true));
        editPropertyPage.clickEditPropertyDescriptionAndPolicySaveConfirmation();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));

        step(String.format("Verify Check-in time changed to '%s-%s'", targetCheckInStartTime, targetCheckInEndTime));
        Assertion.assertThat(String.format("Check-in start time is updated to %s", targetCheckInStartTime), editPropertyPage.isPropertyCheckInStartTimeDisplayed(targetCheckInStartTime), is(true));
        Assertion.assertThat(String.format("Check-in end time is updated to %s", targetCheckInEndTime), editPropertyPage.isPropertyCheckInEndTimeDisplayed(targetCheckInEndTime), is(true));

        step(String.format("Verify Check-out time changed to '%s'", targetCheckOutTime));
        Assertion.assertThat(String.format("Check-out time is updated to %s", targetCheckOutTime), editPropertyPage.isPropertyCheckOutTimeDisplayed(targetCheckOutTime), is(true));

        step(String.format("Store whether '%s' facility is present in detail view", facilityName));
        boolean isFacilityPresent = editPropertyPage.isPropertyFacilityDisplayed(facilityName);

        step("Click on Edit Property Facility");
        editPropertyPage.clickEditPropertyFacility();
        Assertion.assertThat("Edit property facility page is loaded", editPropertyPage.isEditPropertyFacilityPageDisplayed(), is(true));

        step(isFacilityPresent ? String.format("Disable '%s' facility", facilityName) : String.format("Enable '%s' facility", facilityName));
        editPropertyPage.setPropertyFacilityCheckbox(facilityName, !isFacilityPresent);

        step("Submit Save facility form");
        editPropertyPage.clickEditPropertyFacilitySave();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isEditPropertyFacilitySaveConfirmationAppeared(), is(true));
        editPropertyPage.clickEditPropertyFacilitySaveConfirmation();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));

        step(String.format("Verify '%s' facility is %s", facilityName, !isFacilityPresent ? "displayed" : "removed"));
        Assertion.assertThat(
                String.format("%s facility is %s", facilityName, !isFacilityPresent ? "added" : "removed"),
                editPropertyPage.isPropertyFacilityDisplayed(facilityName), is(!isFacilityPresent));

        step("Store current Property Status from detail view");
        boolean isPropertyStatusOn = editPropertyPage.isPropertyStatusOnInDetailView();

        step("Click on Edit Property Status");
        editPropertyPage.clickEditPropertyStatus();
        Assertion.assertThat("Edit property status page is loaded", editPropertyPage.isEditPropertyStatusPageDisplayed(), is(true));

        step(isPropertyStatusOn ? "Toggle Property Status OFF" : "Toggle Property Status ON");
        editPropertyPage.clickPropertyStatusToggle();
        if (isPropertyStatusOn) {
            editPropertyPage.selectPropertyStatusToggleOffReason("Temporarily closed");
            Assertion.assertThat("Property status toggle is OFF", editPropertyPage.isEditPropertyStatusToggleOn(), is(false));
        } else {
            Assertion.assertThat("Property status toggle is ON", editPropertyPage.isEditPropertyStatusToggleOn(), is(true));
        }

        step("Submit Save status form");
        editPropertyPage.clickEditPropertyStatusSave();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isEditPropertyStatusSaveConfirmationAppeared(), is(true));
        editPropertyPage.clickEditPropertyStatusSaveConfirmation();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));

        step(String.format("Verify Property Status changed to %s", !isPropertyStatusOn ? "ON" : "OFF"));
        Assertion.assertThat(
                String.format("Property status is updated to %s", !isPropertyStatusOn ? "ON" : "OFF"),
                editPropertyPage.isPropertyStatusDisplayed(!isPropertyStatusOn), is(true));

        step("Revert Property Status to previous state");
        editPropertyPage.clickEditPropertyStatus();
        Assertion.assertThat("Edit property status page is loaded", editPropertyPage.isEditPropertyStatusPageDisplayed(), is(true));

        editPropertyPage.clickPropertyStatusToggle();
        if (isPropertyStatusOn) {
            Assertion.assertThat("Property status toggle is ON", editPropertyPage.isEditPropertyStatusToggleOn(), is(true));
        } else {
            editPropertyPage.selectPropertyStatusToggleOffReason("Temporarily closed");
            Assertion.assertThat("Property status toggle is OFF", editPropertyPage.isEditPropertyStatusToggleOn(), is(false));
        }

        step("Submit Save status form to revert");
        editPropertyPage.clickEditPropertyStatusSave();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isEditPropertyStatusSaveConfirmationAppeared(), is(true));
        editPropertyPage.clickEditPropertyStatusSaveConfirmation();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));

        step(String.format("Verify Property Status reverted to %s", isPropertyStatusOn ? "ON" : "OFF"));
        Assertion.assertThat(
                String.format("Property status is reverted to %s", isPropertyStatusOn ? "ON" : "OFF"),
                editPropertyPage.isPropertyStatusDisplayed(isPropertyStatusOn),is(true));

        step("Store current Hotel Level announcement text from detail view");
        String currentAnnouncementEn = editPropertyPage.getCurrentPropertyAnnouncementHotelLevelEnFromDetailView();
        String currentAnnouncementId = editPropertyPage.getCurrentPropertyAnnouncementHotelLevelIdFromDetailView();
        String announcementEn = "Automation " + CoreFunctions.randomNameWithoutTest();
        String announcementId = "Automation "  + CoreFunctions.randomNameWithoutTest();

        step("Click on Edit Property Announcement");
        editPropertyPage.clickEditPropertyAnnouncement();
        Assertion.assertThat("Edit announcement page is loaded", editPropertyPage.isEditPropertyAnnouncementPageDisplayed(), is(true));

        step(String.format("Select '%s' as hotel level announcement source", starSourceVendor));
        editPropertyPage.selectPropertyAnnouncementHotelLevelSourceVendor(starSourceVendor);

        step(String.format("Input new English announcement from '%s' to '%s'", currentAnnouncementEn, announcementEn));
        editPropertyPage.inputPropertyAnnouncementHotelLevelEn(announcementEn);

        step(String.format("Input new Indonesian announcement from '%s' to '%s'", currentAnnouncementId, announcementId));
        editPropertyPage.inputPropertyAnnouncementHotelLevelId(announcementId);

        step("Submit Save announcement form");
        editPropertyPage.clickEditPropertyAnnouncementSave();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isEditPropertyAnnouncementSaveConfirmationAppeared(), is(true));
        editPropertyPage.clickEditPropertyAnnouncementSaveConfirmation();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));

        step("Verify English announcement changed successfully");
        Assertion.assertThat(String.format("English announcement is updated to %s", announcementEn), editPropertyPage.isPropertyAnnouncementHotelLevelIEnDisplayed(announcementEn), is(true));

        step("Verify Indonesian announcement changed successfully");
        Assertion.assertThat(String.format("Indonesian announcement is updated to %s", announcementId), editPropertyPage.isPropertyAnnouncementHotelLevelIdDisplayed(announcementId), is(true));

    }

    @DataProvider(name = "verifyGetHotelListByFreeDistrictAndEditPropertySourcesDataProvider")
    public Object[][] verifyGetHotelListByFreeDistrictAndEditPropertySourcesDataProvider() {
        return getObjectParams("verifyGetHotelListByFreeDistrictAndEditPropertySources");
    }
}