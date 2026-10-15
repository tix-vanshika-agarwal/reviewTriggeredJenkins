package com.tiket.web.dashboard.test.hotel.extranet;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.extranet.HotelCreationPage;
import com.tiket.web.dashboard.page.hotel.extranet.Navbar;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class HotelCreationTest extends BaseTest {
    @Author(names = {"Raihan", "Vipul Kumar"})
    @Module(name = "HotelCreation")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelCreationNewPropertyWithAccommodationTypeNHAEntirePlace() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        HotelCreationPage<?> hotelCreationPage = new HotelCreationPage<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testHotelCreationNewPropertyWithAccommodationTypeNHAEntirePlace");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String propertyCategory = param.get("propertyCategory");
        String propertyType = param.get("propertyType");
        String placeType = param.get("placeType");
        String propertyName = param.get("propertyName") + " " + placeType + " " + propertyType + " " + System.currentTimeMillis();
        boolean isUseChain = Boolean.parseBoolean(param.get("isUseChain"));
        String currency = param.get("currency");
        String address = param.get("address");
        String contactRole = param.get("contactRole");
        String propertyPhoneNumber = param.get("propertyPhoneNumber");
        String otherPlatformName = param.get("otherPlatformName");
        String noOfFloor = param.get("noOfFloor");
        int facilityIndex1 = Integer.parseInt(param.get("facilityIndex1"));
        int facilityIndex2 = Integer.parseInt(param.get("facilityIndex2"));
        int facilityIndex3 = Integer.parseInt(param.get("facilityIndex3"));
        String propertyPhotoCategory =  param.get("propertyPhotoCategory");
        String minSellingPrice = param.get("minSellingPrice");
        String roomSize = param.get("roomSize");
        int adultOccupancy = Integer.parseInt(param.get("adultOccupancy"));
        int noOfBathroom = Integer.parseInt(param.get("noOfBathroom"));
        boolean hasMultipleRoom = Boolean.parseBoolean(param.get("hasMultipleRoom"));
        String bedType = param.get("bedType");
        int noOfBed = Integer.parseInt(param.get("noOfBed"));
        String paymentMethod = param.get("paymentMethod");
        String bankName = param.get("bankName");
        String branchName = param.get("branchName");
        String accountNumber = param.get("accountNumber");
        String accountHolderName = param.get("accountHolderName");
        String swiftCode = param.get("swiftCode");
        String channelSource = param.get("channelSource");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel and click add new hotel");
        navbar.clickProfile();
        navbar.clickChangeHotelMenu();
        navbar.clickAddNewHotel();
        Assertion.assertThat("Property category page is displayed", hotelCreationPage.isPropertyCategoryPageDisplayed(), is(true));

        step("Choose property category");
        hotelCreationPage.selectPropertyCategory(propertyCategory);
        hotelCreationPage.clickNextSubmit();
        Assertion.assertThat("Select category confirm modal is appeared", hotelCreationPage.isConfirmationPopupDialogAppeared(), is(true));
        hotelCreationPage.confirmPopupDialog();

        step("Fill in contact detail form");
        Assertion.assertThat("Contact detail form is displayed", hotelCreationPage.isNHAFormActive("CONTACT_DETAIL"), is(true));
        hotelCreationPage.selectNHAContactDetailRole(contactRole);
        hotelCreationPage.enterNHAContactDetailPropertyPhoneNumber(propertyPhoneNumber);
        hotelCreationPage.clickNextSubmit();

        step("Fill in general info form");
        Assertion.assertThat("General info form is displayed", hotelCreationPage.isNHAFormActive("GENERAL_INFO"), is(true));
        hotelCreationPage.searchAndSelectNHAPropertyType(propertyType);
        hotelCreationPage.selectNHAPlaceType(placeType);
        hotelCreationPage.enterNHAPropertyName(propertyName);
        hotelCreationPage.selectNHAPropertyUseChainRadio(isUseChain);
        hotelCreationPage.searchAndSelectNHAPropertyCurrency(currency);
        hotelCreationPage.searchAndSelectNHAMapLocation(address);
        hotelCreationPage.checkNHAOtherPlatformAvailability(otherPlatformName, null);
        hotelCreationPage.clickNextSubmit();

        step("Fill in partnership agreement form");
        Assertion.assertThat("Partnership agreement form is displayed", hotelCreationPage.isNHAFormActive("AGREEMENT_DETAIL"), is(true));
        hotelCreationPage.copyNHAPartnershipAgreementContact();
        hotelCreationPage.checkNHAPartnershipAgreement();
        hotelCreationPage.clickNextSubmit();
        hotelCreationPage.confirmNHAPartnershipAgreement();

        step("Fill in property facility form");
        Assertion.assertThat("Property facility form is displayed", hotelCreationPage.isNHAFormActive("PROPERTY_FACILITY"), is(true));
        hotelCreationPage.enterNHANoOfFloor(noOfFloor);
        hotelCreationPage.scrollForm();
        hotelCreationPage.checkNHAFacilityByIndex(facilityIndex1);
        hotelCreationPage.checkNHAFacilityByIndex(facilityIndex2);
        hotelCreationPage.checkNHAFacilityByIndex(facilityIndex3);
        hotelCreationPage.clickNextSubmit();

        step("Fill in upload property photo form");
        Assertion.assertThat("Upload property photo form is displayed", hotelCreationPage.isNHAFormActive("PROPERTY_PHOTO"), is(true));
        hotelCreationPage.scrollForm();
        hotelCreationPage.uploadNHAPhotoByUrl("sampleImage.jpg");
        hotelCreationPage.uploadNHAPhotoByUrl("sampleImage.jpg");
        hotelCreationPage.uploadNHAPhotoByUrl("sampleImage.jpg");
        hotelCreationPage.checkNHAAllUploadedPhoto(3);
        hotelCreationPage.managePhotoCategory(propertyPhotoCategory);
        hotelCreationPage.clickNextSubmit();

        step("Fill in room listing form");
        Assertion.assertThat("Room listing form is displayed", hotelCreationPage.isNHAFormActive("UNIT_DETAIL"), is(true));
        hotelCreationPage.enterRoomMinimumSellingPrice(minSellingPrice);
        hotelCreationPage.enterRoomSize(roomSize);
        hotelCreationPage.enterNHAAdultOccupancy(adultOccupancy);
        hotelCreationPage.enterNHANoOfBathroom(noOfBathroom);
        hotelCreationPage.selectNHARoomHasMultipleBedroom(hasMultipleRoom);
        hotelCreationPage.enterNHASingleBedroomBedConfiguration(1, noOfBed, bedType);
        hotelCreationPage.checkNHAFacilityByIndex(facilityIndex1);
        hotelCreationPage.checkNHAFacilityByIndex(facilityIndex2);
        hotelCreationPage.checkNHAFacilityByIndex(facilityIndex3);
        hotelCreationPage.clickNextSubmit();

        step("Fill in payment detail form");
        Assertion.assertThat("Payment detail form is displayed", hotelCreationPage.isNHAFormActive("PAYMENT_DETAIL"), is(true));
        hotelCreationPage.selectPaymentMethod(paymentMethod);
        hotelCreationPage.selectNHABankName(bankName);
        hotelCreationPage.enterBankBranchName(branchName);
        hotelCreationPage.enterBankAccountNumber(accountNumber);
        hotelCreationPage.enterBankAccountHolderName(accountHolderName);
        hotelCreationPage.enterBankSwiftCode(swiftCode);
        hotelCreationPage.clickNextSubmit();

        step("Fill in registration review form");
        Assertion.assertThat("Registration review form is displayed", hotelCreationPage.isNHAFormActive("REGISTRATION_REVIEW"), is(true));
        hotelCreationPage.selectNHAChannelSource(channelSource);
        hotelCreationPage.clickNextSubmit();

        step("Confirm registration");
        Assertion.assertThat("Registration confirmation modal is appeared", hotelCreationPage.isNHARegistrationConfirmModalAppeared(), is(true));
        hotelCreationPage.confirmNHASubmitRegistration();
        Assertion.assertThat("Hotel name is displayed correctly", navbar.isHotelNamePresented(propertyName), is(true));
    }

    @Author(names = {"Raihan", "Vipul Kumar"})
    @Module(name = "HotelCreation")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelCreationNewPropertyWithAccommodationTypeNHAUnit() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        HotelCreationPage<?> hotelCreationPage = new HotelCreationPage<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testHotelCreationNewPropertyWithAccommodationTypeNHAUnit");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String propertyCategory = param.get("propertyCategory");
        String propertyType = param.get("propertyType");
        String placeType = param.get("placeType");
        String propertyName = param.get("propertyName") + " " + placeType + " " + propertyType + " " + System.currentTimeMillis();
        boolean isUseChain = Boolean.parseBoolean(param.get("isUseChain"));
        String currency = param.get("currency");
        String address = param.get("address");
        String contactRole = param.get("contactRole");
        String propertyPhoneNumber = param.get("propertyPhoneNumber");
        String otherPlatformName = param.get("otherPlatformName");
        String noOfFloor = param.get("noOfFloor");
        int facilityIndex1 = Integer.parseInt(param.get("facilityIndex1"));
        int facilityIndex2 = Integer.parseInt(param.get("facilityIndex2"));
        int facilityIndex3 = Integer.parseInt(param.get("facilityIndex3"));
        String propertyPhotoCategory = param.get("propertyPhotoCategory");
        String roomName = param.get("roomName");
        String minSellingPrice = param.get("minSellingPrice");
        String roomSize = param.get("roomSize");
        int adultOccupancy = Integer.parseInt(param.get("adultOccupancy"));
        int noOfBathroom = Integer.parseInt(param.get("noOfBathroom"));
        boolean hasMultipleRoom = Boolean.parseBoolean(param.get("hasMultipleRoom"));
        String bedType = param.get("bedType");
        int noOfBed = Integer.parseInt(param.get("noOfBed"));
        String paymentMethod = param.get("paymentMethod");
        String channelSource = param.get("channelSource");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel and click add new hotel");
        navbar.clickProfile();
        navbar.clickChangeHotelMenu();
        navbar.clickAddNewHotel();
        Assertion.assertThat("Property category page is displayed", hotelCreationPage.isPropertyCategoryPageDisplayed(), is(true));

        step("Choose property category");
        hotelCreationPage.selectPropertyCategory(propertyCategory);
        hotelCreationPage.clickNextSubmit();
        Assertion.assertThat("Select category confirm modal is appeared", hotelCreationPage.isConfirmationPopupDialogAppeared(), is(true));
        hotelCreationPage.confirmPopupDialog();

        step("Fill in contact detail form");
        Assertion.assertThat("Contact detail form is displayed", hotelCreationPage.isNHAFormActive("CONTACT_DETAIL"), is(true));
        hotelCreationPage.selectNHAContactDetailRole(contactRole);
        hotelCreationPage.enterNHAContactDetailPropertyPhoneNumber(propertyPhoneNumber);
        hotelCreationPage.clickNextSubmit();

        step("Fill in general info form");
        Assertion.assertThat("General info form is displayed", hotelCreationPage.isNHAFormActive("GENERAL_INFO"), is(true));
        hotelCreationPage.searchAndSelectNHAPropertyType(propertyType);
        hotelCreationPage.selectNHAPlaceType(placeType);
        hotelCreationPage.enterNHAPropertyName(propertyName);
        hotelCreationPage.selectNHAPropertyUseChainRadio(isUseChain);
        hotelCreationPage.searchAndSelectNHAPropertyCurrency(currency);
        hotelCreationPage.searchAndSelectNHAMapLocation(address);
        hotelCreationPage.checkNHAOtherPlatformAvailability(otherPlatformName, null);
        hotelCreationPage.clickNextSubmit();

        step("Fill in partnership agreement form");
        Assertion.assertThat("Partnership agreement form is displayed", hotelCreationPage.isNHAFormActive("AGREEMENT_DETAIL"), is(true));
        hotelCreationPage.copyNHAPartnershipAgreementContact();
        hotelCreationPage.checkNHAPartnershipAgreement();
        hotelCreationPage.clickNextSubmit();
        hotelCreationPage.confirmNHAPartnershipAgreement();

        step("Fill in property facility form");
        Assertion.assertThat("Property facility form is displayed", hotelCreationPage.isNHAFormActive("PROPERTY_FACILITY"), is(true));
        hotelCreationPage.enterNHANoOfFloor(noOfFloor);
        hotelCreationPage.scrollForm();
        hotelCreationPage.checkNHAFacilityByIndex(facilityIndex1);
        hotelCreationPage.checkNHAFacilityByIndex(facilityIndex2);
        hotelCreationPage.checkNHAFacilityByIndex(facilityIndex3);
        hotelCreationPage.clickNextSubmit();

        step("Fill in upload property photo form");
        Assertion.assertThat("Upload property photo form is displayed", hotelCreationPage.isNHAFormActive("PROPERTY_PHOTO"), is(true));
        hotelCreationPage.scrollForm();
        hotelCreationPage.uploadNHAPhotoByUrl("sampleImage.jpg");
        hotelCreationPage.uploadNHAPhotoByUrl("sampleImage.jpg");
        hotelCreationPage.uploadNHAPhotoByUrl("sampleImage.jpg");
        hotelCreationPage.checkNHAAllUploadedPhoto(3);
        hotelCreationPage.managePhotoCategory(propertyPhotoCategory);
        hotelCreationPage.clickNextSubmit();

        step("Fill in room listing form");
        Assertion.assertThat("Room listing form is displayed", hotelCreationPage.isNHAFormActive("ROOM_DETAIL"), is(true));
        hotelCreationPage.enterRoomName(roomName);
        hotelCreationPage.enterRoomMinimumSellingPrice(minSellingPrice);
        hotelCreationPage.enterRoomSize(roomSize);
        hotelCreationPage.enterNHAAdultOccupancy(adultOccupancy);
        hotelCreationPage.enterNHANoOfBathroom(noOfBathroom);
        hotelCreationPage.selectNHARoomHasMultipleBedroom(hasMultipleRoom);
        hotelCreationPage.enterNHASingleBedroomBedConfiguration(1, noOfBed, bedType);
        hotelCreationPage.checkNHAFacilityByIndex(facilityIndex1);
        hotelCreationPage.checkNHAFacilityByIndex(facilityIndex2);
        hotelCreationPage.checkNHAFacilityByIndex(facilityIndex3);
        hotelCreationPage.clickNextSubmit();

        step("Fill in room listing review form");
        Assertion.assertThat("Room listing review form is displayed", hotelCreationPage.isNHAFormActive("ROOM_DETAIL"), is(true));
        hotelCreationPage.refreshPage();
        Assertion.assertThat("Room is added successfully", hotelCreationPage.isNHARoomNameDisplayedOnListingReviewPage(1, roomName), is(true));
        hotelCreationPage.clickNextSubmit();

        step("Fill in payment detail form");
        Assertion.assertThat("Payment detail form is displayed", hotelCreationPage.isNHAFormActive("PAYMENT_DETAIL"), is(true));
        hotelCreationPage.selectPaymentMethod(paymentMethod);
        hotelCreationPage.clickNextSubmit();

        step("Fill in registration review form");
        Assertion.assertThat("Registration review form is displayed", hotelCreationPage.isNHAFormActive("REGISTRATION_REVIEW"), is(true));
        hotelCreationPage.selectNHAChannelSource(channelSource);
        hotelCreationPage.clickNextSubmit();

        step("Confirm registration");
        Assertion.assertThat("Registration confirmation modal is appeared", hotelCreationPage.isNHARegistrationConfirmModalAppeared(), is(true));
        hotelCreationPage.confirmNHASubmitRegistration();
        Assertion.assertThat("Hotel name is displayed correctly", navbar.isHotelNamePresented(propertyName), is(true));
    }
}
