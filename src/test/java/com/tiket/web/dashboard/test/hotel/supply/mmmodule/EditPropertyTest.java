package com.tiket.web.dashboard.test.hotel.supply.mmmodule;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.EditPropertyPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.HotelLibraryPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.LoginPage;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class EditPropertyTest extends BaseTest {
    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyLocationLongLatCoordinate() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyLocationLongLatCoordinate");
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

        step("Edit source of property long lat coordinate and submit");
        editPropertyPage.clickEditLocationAndContactInfo();
        Assertion.assertThat("Edit location and contact info page is loaded", editPropertyPage.isPropertyLocationEditLongLatDisplayed(), is(true));
        editPropertyPage.clickEditLocationLongLat();
        Assertion.assertThat("Edit long lat pop up dialog appeared", editPropertyPage.isEditLongLatPopUpDialogAppeared(), is(true));
        editPropertyPage.selectEditLongLatSourceVendor(vendorName2);
        editPropertyPage.clickEditLongLatSelectLocation();
        String expectedLongLatCoordinate = editPropertyPage.getSelectedLongLatCoordinateValue();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat("Property location long lat coordinate is updated", editPropertyPage.isPropertyLocationLongLatCoordinateLabelUpdated(expectedLongLatCoordinate), is(true));

        step("Revert source of property long lat coordinate back to initial state and submit");
        editPropertyPage.clickEditLocationAndContactInfo();
        Assertion.assertThat("Edit location and contact info page is loaded", editPropertyPage.isPropertyLocationEditLongLatDisplayed(), is(true));
        editPropertyPage.clickEditLocationLongLat();
        Assertion.assertThat("Edit long lat pop up dialog appeared", editPropertyPage.isEditLongLatPopUpDialogAppeared(), is(true));
        editPropertyPage.selectEditLongLatSourceVendor("Input");
        editPropertyPage.clickEditLongLatSelectLocation();
        expectedLongLatCoordinate = editPropertyPage.getSelectedLongLatCoordinateValue();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat("Property location long lat coordinate is updated", editPropertyPage.isPropertyLocationLongLatCoordinateLabelUpdated(expectedLongLatCoordinate), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyLocationMainArea() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyLocationMainArea");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String mainArea = param.get("mainArea");
        String mainArea2 = param.get("mainArea2");

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

        step("Edit property main area and submit");
        editPropertyPage.clickEditLocationAndContactInfo();
        Assertion.assertThat("Edit location and contact info page is loaded", editPropertyPage.isPropertyLocationEditLongLatDisplayed(), is(true));
        editPropertyPage.selectPropertyLocationMainArea(mainArea2);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("Property location main area is updated to %s", mainArea2), editPropertyPage.isPropertyLocationMainAreaDisplayed(mainArea2), is(true));

        step("Revert property main area back to initial state and submit");
        editPropertyPage.clickEditLocationAndContactInfo();
        Assertion.assertThat("Edit location and contact info page is loaded", editPropertyPage.isPropertyLocationEditLongLatDisplayed(), is(true));
        editPropertyPage.selectPropertyLocationMainArea(mainArea);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("Property location main area is updated to %s", mainArea), editPropertyPage.isPropertyLocationMainAreaDisplayed(mainArea), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyLocationAddress() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyLocationAddress");
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

        step("Edit property address and submit");
        editPropertyPage.clickEditLocationAndContactInfo();
        Assertion.assertThat("Edit location and contact info page is loaded", editPropertyPage.isPropertyLocationEditLongLatDisplayed(), is(true));
        editPropertyPage.selectPropertyLocationAddressSourceVendor(vendorName2);
        String expectedAddress = editPropertyPage.getPropertyLocationSelectedAddress();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("Property location address is updated to %s", expectedAddress), editPropertyPage.isPropertyLocationAddressDisplayed(expectedAddress), is(true));

        step("Revert property address back to initial state and submit");
        editPropertyPage.clickEditLocationAndContactInfo();
        Assertion.assertThat("Edit location and contact info page is loaded", editPropertyPage.isPropertyLocationEditLongLatDisplayed(), is(true));
        editPropertyPage.selectPropertyLocationAddressSourceVendor(vendorName);
        expectedAddress = editPropertyPage.getPropertyLocationSelectedAddress();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("Property location address is updated to %s", expectedAddress), editPropertyPage.isPropertyLocationAddressDisplayed(expectedAddress), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyLocationZipCode() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyLocationZipCode");
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

        step("Edit property zip code and submit");
        editPropertyPage.clickEditLocationAndContactInfo();
        Assertion.assertThat("Edit location and contact info page is loaded", editPropertyPage.isPropertyLocationEditLongLatDisplayed(), is(true));
        editPropertyPage.selectPropertyLocationZipCodeSourceVendor(vendorName2);
        String expectedZipCode = editPropertyPage.getPropertyLocationSelectedZipCode();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("Property location zipcode is updated to %s", expectedZipCode), editPropertyPage.isPropertyLocationZipCodeDisplayed(expectedZipCode), is(true));

        step("Revert property zip code back to initial state and submit");
        editPropertyPage.clickEditLocationAndContactInfo();
        Assertion.assertThat("Edit location and contact info page is loaded", editPropertyPage.isPropertyLocationEditLongLatDisplayed(), is(true));
        editPropertyPage.selectPropertyLocationZipCodeSourceVendor(vendorName);
        expectedZipCode = editPropertyPage.getPropertyLocationSelectedZipCode();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("Property location zipcode is updated to %s", expectedZipCode), editPropertyPage.isPropertyLocationZipCodeDisplayed(expectedZipCode), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyContactPhoneNumber() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyContactPhoneNumber");
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

        step("Edit property phone number and submit");
        editPropertyPage.clickEditLocationAndContactInfo();
        Assertion.assertThat("Edit location and contact info page is loaded", editPropertyPage.isPropertyLocationEditLongLatDisplayed(), is(true));
        editPropertyPage.selectPropertyContactInfoPhoneNumberSourceVendor(vendorName2);
        String expectedPhoneNumber = editPropertyPage.getPropertyContactInfoSelectedPhoneNumber();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("Property contact phone number is updated to %s", expectedPhoneNumber), editPropertyPage.isPropertyContactInfoPhoneNumberDisplayed(expectedPhoneNumber), is(true));

        step("Revert property phone number back to initial state and submit");
        editPropertyPage.clickEditLocationAndContactInfo();
        Assertion.assertThat("Edit location and contact info page is loaded", editPropertyPage.isPropertyLocationEditLongLatDisplayed(), is(true));
        editPropertyPage.selectPropertyContactInfoPhoneNumberSourceVendor(vendorName);
        expectedPhoneNumber = editPropertyPage.getPropertyContactInfoSelectedPhoneNumber();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("Property contact phone number is updated to %s", expectedPhoneNumber), editPropertyPage.isPropertyContactInfoPhoneNumberDisplayed(expectedPhoneNumber), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyContactEmail() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyContactEmail");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String vendorName2 = param.get("vendorName2");
        String email = param.get("email");

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

        step("Edit property email and submit");
        editPropertyPage.clickEditLocationAndContactInfo();
        Assertion.assertThat("Edit location and contact info page is loaded", editPropertyPage.isPropertyLocationEditLongLatDisplayed(), is(true));
        editPropertyPage.selectPropertyContactInfoEmailSourceVendor(vendorName2);
        editPropertyPage.clearPropertyContactInfoEmail();
        editPropertyPage.inputPropertyContactInfoEmail(email);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("Property contact email is updated to %s", email), editPropertyPage.isPropertyContactInfoEmailDisplayed(email), is(true));

        step("Revert property email back to initial state and submit");
        editPropertyPage.clickEditLocationAndContactInfo();
        Assertion.assertThat("Edit location and contact info page is loaded", editPropertyPage.isPropertyLocationEditLongLatDisplayed(), is(true));
        editPropertyPage.selectPropertyContactInfoEmailSourceVendor(vendorName);
        String expectedPhoneNumber = editPropertyPage.getPropertyContactInfoSelectedEmail();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("Property contact email is updated to %s", expectedPhoneNumber), editPropertyPage.isPropertyContactInfoEmailDisplayed(expectedPhoneNumber), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyDescription() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyDescription");
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

        step("Edit property description");
        editPropertyPage.clickEditPropertyDescriptionAndPolicy();
        Assertion.assertThat("Edit property description and policy page is loaded", editPropertyPage.isPropertyDescriptionAndPolicyPageDisplayed(), is(true));
        editPropertyPage.selectPropertyDescriptionSourceVendor(vendorName2);
        String propertyDescriptionEn = editPropertyPage.getEditPropertyDescriptionEnValue();
        String propertyDescriptionId = editPropertyPage.getEditPropertyDescriptionIdValue();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("Property description EN is updated. Vendor source: %s", vendorName2), editPropertyPage.isPropertyDescriptionEnDisplayed(propertyDescriptionEn), is(true));
        Assertion.assertThat(String.format("Property description ID is updated. Vendor source: %s", vendorName2), editPropertyPage.isPropertyDescriptionIdDisplayed(propertyDescriptionId), is(true));

        step("Revert property description back to initial state and submit");
        editPropertyPage.clickEditPropertyDescriptionAndPolicy();
        Assertion.assertThat("Edit property description and policy page is loaded", editPropertyPage.isPropertyDescriptionAndPolicyPageDisplayed(), is(true));
        editPropertyPage.selectPropertyDescriptionSourceVendor(vendorName);
        propertyDescriptionEn = editPropertyPage.getEditPropertyDescriptionEnValue();
        propertyDescriptionId = editPropertyPage.getEditPropertyDescriptionIdValue();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("Property description EN is updated. Vendor source: %s", vendorName), editPropertyPage.isPropertyDescriptionEnDisplayed(propertyDescriptionEn), is(true));
        Assertion.assertThat(String.format("Property description ID is updated. Vendor source: %s", vendorName), editPropertyPage.isPropertyDescriptionIdDisplayed(propertyDescriptionId), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyPolicy() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyPolicy");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String vendorName2 = param.get("vendorName2");
        String policyId = param.get("policyId");
        String policyEn = param.get("policyEn");

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

        step("Edit property policy");
        editPropertyPage.clickEditPropertyDescriptionAndPolicy();
        Assertion.assertThat("Edit property description and policy page is loaded", editPropertyPage.isPropertyDescriptionAndPolicyPageDisplayed(), is(true));
        editPropertyPage.selectPropertyPolicySourceVendor(vendorName2);
        editPropertyPage.inputPropertyPolicyId(policyId);
        editPropertyPage.inputPropertyPolicyEn(policyEn);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat("Submit successful snackbar is disappeared", editPropertyPage.isSubmitSuccessfulSnackbarDisappeared(), is(true));
        Assertion.assertThat(String.format("Property policy EN is updated. Vendor source: %s", vendorName2), editPropertyPage.isPropertyPolicyEnDisplayed(policyEn), is(true));
        Assertion.assertThat(String.format("Property policy ID is updated. Vendor source: %s", vendorName2), editPropertyPage.isPropertyPolicyIdDisplayed(policyId), is(true));

        step("Revert property policy back to initial state and submit");
        editPropertyPage.clickEditPropertyDescriptionAndPolicy();
        Assertion.assertThat("Edit property description and policy page is loaded", editPropertyPage.isPropertyDescriptionAndPolicyPageDisplayed(), is(true));
        editPropertyPage.selectPropertyPolicySourceVendor(vendorName);
        String propertyPolicyEn = editPropertyPage.getEditPropertyPolicyEnValue();
        String propertyPolicyId = editPropertyPage.getEditPropertyPolicyIdValue();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat("Submit successful snackbar is disappeared", editPropertyPage.isSubmitSuccessfulSnackbarDisappeared(), is(true));
        Assertion.assertThat(String.format("Property policy EN is updated. Vendor source: %s", vendorName), editPropertyPage.isPropertyPolicyEnDisplayed(propertyPolicyEn), is(true));
        Assertion.assertThat(String.format("Property policy ID is updated. Vendor source: %s", vendorName), editPropertyPage.isPropertyPolicyIdDisplayed(propertyPolicyId), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyCheckInAndOutTime() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyCheckInAndOutTime");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String vendorName2 = param.get("vendorName2");
        String vendorName3 = param.get("vendorName3");

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

        step("Edit property check in and out time");
        editPropertyPage.clickEditPropertyDescriptionAndPolicy();
        Assertion.assertThat("Edit property description and policy page is loaded", editPropertyPage.isPropertyDescriptionAndPolicyPageDisplayed(), is(true));
        editPropertyPage.selectPropertyCheckInAndOutSourceVendor(vendorName2);
        String checkInStartTime = editPropertyPage.getEditPropertyCheckInStartTime();
        String checkOutTime = editPropertyPage.getEditPropertyCheckOutTime();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat("Submit successful snackbar is disappeared", editPropertyPage.isSubmitSuccessfulSnackbarDisappeared(), is(true));
        Assertion.assertThat(String.format("Property check in time is updated. Vendor source: %s", vendorName2), editPropertyPage.isPropertyCheckInStartTimeDisplayed(checkInStartTime), is(true));
        Assertion.assertThat(String.format("Property check out time is updated. Vendor source: %s", vendorName2), editPropertyPage.isPropertyCheckOutTimeDisplayed(checkOutTime), is(true));

        step("Revert property check in and out back to initial state and submit");
        editPropertyPage.clickEditPropertyDescriptionAndPolicy();
        Assertion.assertThat("Edit property description and policy page is loaded", editPropertyPage.isPropertyDescriptionAndPolicyPageDisplayed(), is(true));
        editPropertyPage.selectPropertyCheckInAndOutSourceVendor(vendorName3);
        checkInStartTime = editPropertyPage.getEditPropertyCheckInStartTime();
        String checkInEndTime = editPropertyPage.getEditPropertyCheckInEndTime();
        checkOutTime = editPropertyPage.getEditPropertyCheckOutTime();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat("Submit successful snackbar is disappeared", editPropertyPage.isSubmitSuccessfulSnackbarDisappeared(), is(true));
        Assertion.assertThat(String.format("Property check in start time is updated. Vendor source: %s", vendorName3), editPropertyPage.isPropertyCheckInStartTimeDisplayed(checkInStartTime), is(true));
        Assertion.assertThat(String.format("Property check in end time is updated. Vendor source: %s", vendorName3), editPropertyPage.isPropertyCheckInEndTimeDisplayed(checkInEndTime), is(true));
        Assertion.assertThat(String.format("Property check out time is updated. Vendor source: %s", vendorName3), editPropertyPage.isPropertyCheckOutTimeDisplayed(checkOutTime), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyFacility() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyFacility");
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

        step("Edit property facility and submit");
        editPropertyPage.clickEditPropertyFacility();
        Assertion.assertThat("Edit facility page is loaded", editPropertyPage.isEditPropertyFacilityPageDisplayed(), is(true));
        editPropertyPage.selectPropertyFacilitySourceVendor(vendorName2);
        int selectedFacility = editPropertyPage.getNumberOfSelectedPropertyFacilityFromInput();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        int facilityLabel = editPropertyPage.getNumberOfSelectedPropertyFacilityLabel();
        Assertion.assertThat(String.format("Property facility is updated to vendor %s, total facility is %s", vendorName2, facilityLabel), selectedFacility == facilityLabel, is(true));

        step("Revert property facility back to initial state and submit");
        editPropertyPage.clickEditPropertyFacility();
        Assertion.assertThat("Edit facility page is loaded", editPropertyPage.isEditPropertyFacilityPageDisplayed(), is(true));
        editPropertyPage.selectPropertyFacilitySourceVendor(vendorName);
        selectedFacility = editPropertyPage.getNumberOfSelectedPropertyFacilityFromInput();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat(String.format("Property facility is updated to vendor %s, total facility is %s", vendorName, editPropertyPage.getNumberOfSelectedPropertyFacilityLabel()), selectedFacility == editPropertyPage.getNumberOfSelectedPropertyFacilityLabel(), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyStatusToggleOff() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyStatusToggleOff");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String reason = param.get("reason");

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

        step("Edit property status toggle to off and submit");
        editPropertyPage.clickEditPropertyStatus();
        Assertion.assertThat("Edit property status page is loaded", editPropertyPage.isEditPropertyStatusPageDisplayed(), is(true));
        Assertion.assertThat("Current property status is on", editPropertyPage.isEditPropertyStatusToggleOn(), is(true));
        editPropertyPage.clickPropertyStatusToggle();
        editPropertyPage.selectPropertyStatusToggleOffReason(reason);
        Assertion.assertThat("Current property status is off", editPropertyPage.isEditPropertyStatusToggleOn(), is(false));
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        editPropertyPage.clickEditPropertyStatus();
        Assertion.assertThat("Edit property status page is loaded", editPropertyPage.isEditPropertyStatusPageDisplayed(), is(true));
        Assertion.assertThat("Property status is updated to off", editPropertyPage.isEditPropertyStatusToggleOn(), is(false));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyStatus() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyStatus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String reason = param.get("reason");

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

        step("Edit property status toggle to off and submit");
        editPropertyPage.clickEditPropertyStatus();
        Assertion.assertThat("Edit property status page is loaded", editPropertyPage.isEditPropertyStatusPageDisplayed(), is(true));
        Assertion.assertThat("Current property status is on", editPropertyPage.isEditPropertyStatusToggleOn(), is(true));
        editPropertyPage.clickPropertyStatusToggle();
        Assertion.assertThat("Current property status is off", editPropertyPage.isEditPropertyStatusToggleOn(), is(false));
        editPropertyPage.selectPropertyStatusToggleOffReason(reason);
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        editPropertyPage.clickEditPropertyStatus();
        Assertion.assertThat("Edit property status page is loaded", editPropertyPage.isEditPropertyStatusPageDisplayed(), is(true));
        Assertion.assertThat("Property status is updated to on", editPropertyPage.isEditPropertyStatusToggleOn(), is(false));

        step("Revert property status toggle to on and submit");
        editPropertyPage.clickPropertyStatusToggle();
        Assertion.assertThat("Current property status is on", editPropertyPage.isEditPropertyStatusToggleOn(), is(true));
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        editPropertyPage.clickEditPropertyStatus();
        Assertion.assertThat("Edit property status page is loaded", editPropertyPage.isEditPropertyStatusPageDisplayed(), is(true));
        Assertion.assertThat("Property status is updated to on", editPropertyPage.isEditPropertyStatusToggleOn(), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyVendorStatusToggleOff() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyVendorStatusToggleOff");
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

        step("Edit property vendor status toggle to off and submit");
        editPropertyPage.clickEditPropertyStatus();
        Assertion.assertThat("Edit property status page is loaded", editPropertyPage.isEditPropertyStatusPageDisplayed(), is(true));
        Assertion.assertThat(String.format("Current property vendor status is on, vendor: %s)", vendorName2), editPropertyPage.isEditPropertyVendorStatusToggleOn(vendorName2), is(true));
        editPropertyPage.clickPropertyVendorStatusToggle(vendorName2);
        Assertion.assertThat(String.format("Current property vendor status is off, vendor: %s)", vendorName2), editPropertyPage.isEditPropertyVendorStatusToggleOn(vendorName2), is(false));
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        editPropertyPage.clickEditPropertyStatus();
        Assertion.assertThat("Edit property status page is loaded", editPropertyPage.isEditPropertyStatusPageDisplayed(), is(true));
        Assertion.assertThat(String.format("Property vendor status is updated to off, vendor: %s)", vendorName2), editPropertyPage.isEditPropertyVendorStatusToggleOn(vendorName2), is(false));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyVendorStatus() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyVendorStatus");
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

        step("Edit property vendor status toggle to off and submit");
        editPropertyPage.clickEditPropertyStatus();
        Assertion.assertThat("Edit property status page is loaded", editPropertyPage.isEditPropertyStatusPageDisplayed(), is(true));
        Assertion.assertThat(String.format("Current property vendor status is on, vendor: %s)", vendorName2), editPropertyPage.isEditPropertyVendorStatusToggleOn(vendorName2), is(true));
        editPropertyPage.clickPropertyVendorStatusToggle(vendorName2);
        Assertion.assertThat(String.format("Current property vendor status is off, vendor: %s)", vendorName2), editPropertyPage.isEditPropertyVendorStatusToggleOn(vendorName2), is(false));
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        editPropertyPage.clickEditPropertyStatus();
        Assertion.assertThat("Edit property status page is loaded", editPropertyPage.isEditPropertyStatusPageDisplayed(), is(true));
        Assertion.assertThat(String.format("Property vendor status is updated to off, vendor: %s)", vendorName2), editPropertyPage.isEditPropertyVendorStatusToggleOn(vendorName2), is(false));

        step("Revert edit property vendor status toggle to on and submit");
        editPropertyPage.clickPropertyVendorStatusToggle(vendorName2);
        Assertion.assertThat(String.format("Current property vendor status is on, vendor: %s)", vendorName2), editPropertyPage.isEditPropertyVendorStatusToggleOn(vendorName2), is(true));
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        editPropertyPage.clickEditPropertyStatus();
        Assertion.assertThat("Edit property status page is loaded", editPropertyPage.isEditPropertyStatusPageDisplayed(), is(true));
        Assertion.assertThat(String.format("Property vendor status is updated to on, vendor: %s)", vendorName2), editPropertyPage.isEditPropertyVendorStatusToggleOn(vendorName2), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyAnnouncementHotelLevel() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyAnnouncementHotelLevel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String vendorName2 = param.get("vendorName2");
        String announcementId = param.get("announcementId");
        String announcementEn = param.get("announcementEn");
        String vendorName3 = param.get("vendorName3");

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

        step("Edit property announcement hotel level and submit");
        editPropertyPage.clickEditPropertyAnnouncement();
        Assertion.assertThat("Edit announcement page is loaded", editPropertyPage.isEditPropertyAnnouncementPageDisplayed(), is(true));
        editPropertyPage.selectPropertyAnnouncementHotelLevelSourceVendor(vendorName2);
        editPropertyPage.inputPropertyAnnouncementHotelLevelId(announcementId);
        editPropertyPage.inputPropertyAnnouncementHotelLevelEn(announcementEn);
        String expectedAnnouncementId = editPropertyPage.getEditPropertyAnnouncementHotelLevelIdValue();
        String expectedAnnouncementEn = editPropertyPage.getEditPropertyAnnouncementHotelLevelEnValue();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat("Property announcement hotel level EN is updated", editPropertyPage.isPropertyAnnouncementHotelLevelIEnDisplayed(expectedAnnouncementEn), is(true));
        Assertion.assertThat("Property announcement hotel level ID is updated", editPropertyPage.isPropertyAnnouncementHotelLevelIdDisplayed(expectedAnnouncementId), is(true));

        step("Revert property announcement hotel level back to initial state and submit");
        editPropertyPage.clickEditPropertyAnnouncement();
        Assertion.assertThat("Edit announcement page is loaded", editPropertyPage.isEditPropertyAnnouncementPageDisplayed(), is(true));
        editPropertyPage.selectPropertyAnnouncementHotelLevelSourceVendor(vendorName3);
        expectedAnnouncementId = editPropertyPage.getEditPropertyAnnouncementHotelLevelIdValue();
        expectedAnnouncementEn = editPropertyPage.getEditPropertyAnnouncementHotelLevelEnValue();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat("Property announcement hotel level EN is updated", editPropertyPage.isPropertyAnnouncementHotelLevelIEnDisplayed(expectedAnnouncementEn), is(true));
        Assertion.assertThat("Property announcement hotel level ID is updated", editPropertyPage.isPropertyAnnouncementHotelLevelIdDisplayed(expectedAnnouncementId), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "EditProperty")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryEditPropertyAnnouncementRoomLevel() {
        Map<String, String> param = getParam("testHotelLibraryEditPropertyAnnouncementRoomLevel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditPropertyPage<?> editPropertyPage = new EditPropertyPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String vendorName2 = param.get("vendorName2");
        String announcementId = param.get("announcementId");
        String announcementEn = param.get("announcementEn");

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

        step("Edit property announcement room level and submit");
        editPropertyPage.clickEditPropertyAnnouncement();
        Assertion.assertThat("Edit announcement page is loaded", editPropertyPage.isEditPropertyAnnouncementPageDisplayed(), is(true));
        editPropertyPage.selectPropertyAnnouncementRoomLevelSourceVendor(vendorName2);
        editPropertyPage.inputPropertyAnnouncementRoomLevelId(announcementId);
        editPropertyPage.inputPropertyAnnouncementRoomLevelEn(announcementEn);
        String expectedAnnouncementId = editPropertyPage.getEditPropertyAnnouncementRoomLevelIdValue();
        String expectedAnnouncementEn = editPropertyPage.getEditPropertyAnnouncementRoomLevelEnValue();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat("Property announcement room level EN is updated", editPropertyPage.isPropertyAnnouncementRoomLevelEnDisplayed(expectedAnnouncementEn), is(true));
        Assertion.assertThat("Property announcement room level ID is updated", editPropertyPage.isPropertyAnnouncementRoomLevelIdDisplayed(expectedAnnouncementId), is(true));

        step("Revert property announcement room level back to initial state and submit");
        editPropertyPage.clickEditPropertyAnnouncement();
        Assertion.assertThat("Edit announcement page is loaded", editPropertyPage.isEditPropertyAnnouncementPageDisplayed(), is(true));
        editPropertyPage.selectPropertyAnnouncementRoomLevelSourceVendor(StringUtils.capitalize(vendorName.toLowerCase()));
        expectedAnnouncementId = editPropertyPage.getEditPropertyAnnouncementRoomLevelIdValue();
        expectedAnnouncementEn = editPropertyPage.getEditPropertyAnnouncementRoomLevelEnValue();
        editPropertyPage.clickSubmit();
        Assertion.assertThat("Confirm pop up dialog appeared", editPropertyPage.isConfirmPopUpDialogAppeared(), is(true));
        editPropertyPage.clickSubmitConfirmPopUp();
        Assertion.assertThat("Edit property page is loaded", editPropertyPage.isEditPropertyPageLoaded(), is(true));
        Assertion.assertThat("Property announcement room level EN is updated", editPropertyPage.isPropertyAnnouncementRoomLevelEnDisplayed(expectedAnnouncementEn), is(true));
        Assertion.assertThat("Property announcement room level ID is updated", editPropertyPage.isPropertyAnnouncementRoomLevelIdDisplayed(expectedAnnouncementId), is(true));
    }
}
