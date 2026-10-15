package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class HotelCreationPage <T extends WebDriver> extends BasePage<T> {
    public HotelCreationPage(Session<T> session) {
        super(session);
    }

    private static final int SCROLL_MAX_RETRY = 5;
    private static final Locator NEXT_SUBMIT_BUTTON_PROPERTY_CATEGORY_PAGE = new Locator("NEXT_SUBMIT_BUTTON_PROPERTY_CATEGORY_PAGE", new By.ByXPath("(//button[@type='submit'][contains(@class,'primary')])"));
    private static final Locator NEXT_SUBMIT_BUTTON = new Locator("NEXT_SUBMIT_BUTTON", new By.ByXPath("//div[contains(@class,'button-wrap')]//button[@type='submit' and contains(@class,'primary')]"));
    private static final Locator MAIN_FORM_SECTION = new Locator("MAIN_FORM_SECTION", new By.ByXPath("//main"));
    private static final Locator CONFIRMATION_POPUP_DIALOG = new Locator("CONFIRMATION_POPUP_DIALOG", new By.ByXPath("//div[contains(@class,'Dialog_pop_up')]"));
    private static final Locator CONFIRMATION_CONTINUE_BUTTON = new Locator("CONFIRMATION_CONTINUE_BUTTON", new By.ByXPath("//div[contains(@class,'Dialog_pop_up')]//button[2]"));
    private static final Locator PROPERTY_CATEGORY_HOTEL_CONTAINER = new Locator("PROPERTY_CATEGORY_HOTEL_CONTAINER", new By.ByXPath("//*[contains(@class,'property-category-container')]/div[1]"));
    private static final Locator PROPERTY_CATEGORY_NHA_CONTAINER = new Locator("PROPERTY_CATEGORY_NHA_CONTAINER", new By.ByXPath("//*[contains(@class,'property-category-container')]/div[2]"));
    private static final Locator GUEST_AND_PROPERTY_TYPE_FORM = new Locator("GUEST_AND_PROPERTY_TYPE_FORM", new By.ByXPath("//div[@class='property-type-wrap']"));
    private static final Locator PROPERTY_TYPE_DROPDOWN_INPUT = new Locator("PROPERTY_TYPE_DROPDOWN_INPUT", new By.ByXPath("//div[@id='property_type']"));
    private static final Locator PROPERTY_TYPE_SEARCH_INPUT = new Locator("PROPERTY_TYPE_SEARCH_INPUT", new By.ByXPath("//div[@class='dropdown-list-wrap']//input[@type='text']"));
    private static final Locator GENERAL_INFO_FORM = new Locator("GENERAL_INFO_FORM", new By.ByXPath("//div[@class='the-card general-info-section']"));
    private static final Locator GENERAL_INFO_PROPERTY_NAME_INPUT = new Locator("GENERAL_INFO_PROPERTY_NAME_INPUT", new By.ByXPath("//input[@name='name']"));
    private static final Locator GENERAL_INFO_PROPERTY_NAME_ALIAS_INPUT = new Locator("GENERAL_INFO_PROPERTY_NAME_ALIAS_INPUT", new By.ByXPath("//input[@name='alias']"));
    private static final Locator GENERAL_INFO_CURRENCY_DROPDOWN_INPUT = new Locator("GENERAL_INFO_CURRENCY_DROPDOWN_INPUT", new By.ByXPath("//div[@id='currency']//div[contains(@class,'dropdown-input')]"));
    private static final Locator GENERAL_INFO_CURRENCY_SEARCH_INPUT = new Locator("GENERAL_INFO_CURRENCY_SEARCH_INPUT", new By.ByXPath("//div[@id='currency']//div[@class='searchbar']//input"));
    private static final Locator GENERAL_INFO_ADDRESS_INPUT = new Locator("GENERAL_INFO_ADDRESS_INPUT", new By.ByXPath("//input[@name='address']"));
    private static final Locator GENERAL_INFO_MAP_FIRST_SUGGESTION = new Locator("GENERAL_INFO_MAP_FIRST_SUGGESTION", new By.ByXPath("(//div[contains(@class,'pac-container')]//div[contains(@class,'pac-item')])[1]"));
    private static final Locator GENERAL_INFO_ZIP_CODE = new Locator("GENERAL_INFO_ZIP_CODE", new By.ByXPath("//input[@name='postalCode']"));
    private static final Locator GENERAL_INFO_MAP_SEARCH_INPUT = new Locator("GENERAL_INFO_MAP_SEARCH_INPUT", new By.ByXPath("//input[@name='searchPlace']"));
    private static final Locator NHA_GENERAL_INFO_PROPERTY_TYPE_INPUT = new Locator("GENERAL_INFO_PROPERTY_NAME_TYPE", new By.ByXPath("(//input[@id='dropdown'])[1]"));
    private static final Locator NHA_GENERAL_INFO_PROPERTY_NAME_INPUT = new Locator("GENERAL_INFO_PROPERTY_NAME_INPUT", new By.ById("nama-properti"));
    private static final Locator NHA_GENERAL_INFO_CURRENCY_DROPDOWN_INPUT = new Locator("GENERAL_INFO_CURRENCY_DROPDOWN_INPUT", new By.ByXPath("(//input[@id='dropdown'])[2]"));
    private static final Locator NHA_GENERAL_INFO_MAP_SEARCH_INPUT = new Locator("GENERAL_INFO_MAP_SEARCH_INPUT", new By.ByXPath("//div[contains(@class,'SearchBox_input_box')]//input"));
    private static final Locator NHA_GENERAL_INFO_MAP_CONFIRM_BUTTON = new Locator("GENERAL_INFO_MAP_CONFIRM_BUTTON", new By.ByXPath("(//button[contains(@class,'primary')])[1]"));
    private static final Locator CONTACT_DETAIL_FORM = new Locator("CONTACT_DETAIL_FORM", new By.ByXPath("//div[contains(@class,'contact-details-wrap')]"));
    private static final Locator CONTACT_DETAIL_PIC_NAME_INPUT = new Locator("CONTACT_DETAIL_PIC_NAME_INPUT", new By.ByXPath("//input[@name='name']"));
    private static final Locator CONTACT_DETAIL_ROLE_DROPDOWN_INPUT = new Locator("CONTACT_DETAIL_ROLE_DROPDOWN_INPUT", new By.ByXPath("//div[@id='positionId']//div[contains(@class,'dropdown-input')]"));
    private static final Locator CONTACT_DETAIL_EMAIL_INPUT = new Locator("CONTACT_DETAIL_EMAIL_INPUT", new By.ByXPath("//input[@name='Email0']"));
    private static final Locator CONTACT_DETAIL_MOBILE_PHONE_NUMBER_INPUT = new Locator("CONTACT_DETAIL_MOBILE_PHONE_NUMBER_INPUT", new By.ByXPath("//input[@name='Mobile1Phone']"));
    private static final Locator CONTACT_DETAIL_PROPERTY_PHONE_NUMBER_INPUT = new Locator("CONTACT_DETAIL_PROPERTY_PHONE_NUMBER_INPUT", new By.ByXPath("//input[@name='PropertyPhone1Phone']"));
    private static final Locator NHA_CONTACT_DETAIL_ROLE_DROPDOWN_INPUT = new Locator("CONTACT_DETAIL_ROLE_DROPDOWN_INPUT", new By.ById("dropdown"));
    private static final Locator NHA_CONTACT_DETAIL_PROPERTY_PHONE_NUMBER_INPUT = new Locator("CONTACT_DETAIL_PROPERTY_PHONE_NUMBER_INPUT", new By.ById("nomor-ponsel-properti"));
    private static final Locator PARTNERSHIP_AGREEMENT_FORM = new Locator("PARTNERSHIP_AGREEMENT_FORM", new By.ByXPath("//div[contains(@class,'agreement-wrap')]"));
    private static final Locator PARTNERSHIP_AGREEMENT_COMPANY_NAME_INPUT = new Locator("PARTNERSHIP_AGREEMENT_COMPANY_NAME_INPUT", new By.ByXPath("//input[@name='companyName']"));
    private static final Locator PARTNERSHIP_AGREEMENT_COMPANY_ADDRESS_INPUT = new Locator("PARTNERSHIP_AGREEMENT_COMPANY_ADDRESS_INPUT", new By.ByXPath("//input[@name='address']"));
    private static final Locator NHA_PARTNERSHIP_AGREEMENT_COMPANY_ADDRESS_INPUT = new Locator("PARTNERSHIP_AGREEMENT_COMPANY_ADDRESS_INPUT", new By.ByXPath("//input[@name='companyAddress']"));
    private static final Locator PARTNERSHIP_AGREEMENT_COUNTRY_DROPDOWN_INPUT = new Locator("PARTNERSHIP_AGREEMENT_COUNTRY_DROPDOWN_INPUT", new By.ByXPath("//div[@id='country']//div[contains(@class,'dropdown-input')]"));
    private static final Locator NHA_PARTNERSHIP_AGREEMENT_COUNTRY_DROPDOWN_INPUT = new Locator("PARTNERSHIP_AGREEMENT_COUNTRY_DROPDOWN_INPUT", new By.ByXPath("(//input[@id='dropdown'])[1]"));
    private static final Locator PARTNERSHIP_AGREEMENT_COPY_CONTACT_BUTTON = new Locator("PARTNERSHIP_AGREEMENT_COPY_CONTACT_BUTTON", new By.ByXPath("//div[@id='copyContact']"));
    private static final Locator NHA_PARTNERSHIP_AGREEMENT_COPY_CONTACT_BUTTON = new Locator("PARTNERSHIP_AGREEMENT_COPY_CONTACT_BUTTON", new By.ByXPath("(//input[@id='nama']/ancestor::div[@class='flex flex-col']//button)[1]"));
    private static final Locator PARTNERSHIP_AGREEMENT_CHECKBOX = new Locator("PARTNERSHIP_AGREEMENT_CHECKBOX", new By.ByXPath("//div[@class='checkbox-agreement']//input[@type='checkbox']/following-sibling::span[contains(@class,'checkbox')]"));
    private static final Locator NHA_PARTNERSHIP_AGREEMENT_CHECKBOX = new Locator("PARTNERSHIP_AGREEMENT_CHECKBOX", new By.ByXPath("//input[contains(@name,'isAgreement')]/following-sibling::span[1]"));
    private static final Locator PARTNERSHIP_AGREEMENT_CONFIRM_MODAL = new Locator("PARTNERSHIP_AGREEMENT_CONFIRM_MODAL", new By.ByXPath("//div[@class='the-modal-content confirm-modal']"));
    private static final Locator NHA_PARTNERSHIP_AGREEMENT_CONFIRM_MODAL = new Locator("PARTNERSHIP_AGREEMENT_CONFIRM_MODAL", new By.ByXPath("//div[contains(@class,'Dialog_pop_up')]"));
    private static final Locator PARTNERSHIP_AGREEMENT_CONFIRM_BUTTON = new Locator("PARTNERSHIP_AGREEMENT_CONFIRM_BUTTON", new By.ByXPath("//div[@class='modal-panel-action']/button[text()='OK']"));
    private static final Locator NHA_PARTNERSHIP_AGREEMENT_CONFIRM_BUTTON = new Locator("PARTNERSHIP_AGREEMENT_CONFIRM_BUTTON", new By.ByXPath("//button[contains(@class,'Button_variant_secondary')]"));
    private static final Locator OTHER_PLATFORM_FORM = new Locator("OTHER_PLATFORM_FORM", new By.ByXPath("//div[contains(@class,'other-platform-wrap')]"));
    private static final Locator PROPERTY_FACILITY_FORM = new Locator("PROPERTY_FACILITY_FORM", new By.ByXPath("//div[contains(@class,'facilities-section')]"));
    private static final Locator NHA_NO_OF_FLOOR_INPUT = new Locator("NHA_TOTAL_FLOOR_INPUT", new By.ByXPath("//input[@type='number']"));
    private static final Locator UPLOAD_PROPERTY_PHOTO_FORM = new Locator("UPLOAD_PROPERTY_PHOTO_FORM", new By.ByXPath("//div[contains(@class,'property-photo-wrap')]"));
    private static final Locator UPLOAD_PHOTO_UPLOADED_MODAL = new Locator("UPLOAD_PHOTO_UPLOADED_MODAL", new By.ByXPath("//div[@class='the-image-uploader']"));
    private static final Locator UPLOAD_PHOTO_MODAL_CLOSE_BUTTON = new Locator("UPLOAD_PHOTO_MODAL_CLOSE_BUTTON", new By.ByXPath("//div[@class='the-modal-content upload-image-modal']/span[@class='tixicon tixicon-close']"));
    private static final Locator UPLOAD_PHOTO_URL_INPUT = new Locator("UPLOAD_PHOTO_URL_INPUT", new By.ByXPath("//input[@name='uploadImage']"));
    private static final Locator NHA_UPLOAD_PHOTO_URL_INPUT = new Locator("UPLOAD_PHOTO_URL_INPUT", new By.ByXPath("//div[contains(@class,'FileUpload')]/input[@type='file']"));
    private static final Locator UPLOAD_PHOTO_PROGRESS_BAR = new Locator("UPLOAD_PHOTO_PROGRESS_BAR", new By.ByXPath("//div[contains(@class,'progress-bar')]"));
    private static final Locator NHA_UPLOAD_PHOTO_PROGRESS_BAR = new Locator("UPLOAD_PHOTO_PROGRESS_BAR", new By.ByXPath("//div[contains(@class,'ProgressBar_bar_content')]"));
    private static final Locator UPLOAD_PHOTO_UPLOADED_IMAGE_CHECKBOX = new Locator("UPLOAD_PHOTO_UPLOADED_IMAGE_CHECKBOX", new By.ByXPath("//div[@class='the-image-uploader']//div[contains(@class,'the-image')]/img/following-sibling::div[@class='the-image-checkbox']//span"));
    private static final Locator NHA_UPLOAD_PHOTO_UPLOADED_IMAGE_CHECKBOX = new Locator("UPLOAD_PHOTO_UPLOADED_IMAGE_CHECKBOX", new By.ByXPath("//img[@alt='img-upload']/parent::span"));
    private static final Locator UPLOAD_PHOTO_UPLOADED_MODAL_ADD_BUTTON = new Locator("UPLOAD_PHOTO_UPLOADED_MODAL_ADD_BUTTON", new By.ByXPath("//div[@class='the-container-add']//button"));
    private static final Locator NHA_UPLOAD_PHOTO_MANAGE_CATEGORY_BUTTON = new Locator("NHA_UPLOAD_PHOTO_MANAGE_CATEGORY_BUTTON", new By.ByXPath("//div[@class='cursor-pointer']/button[contains(@class,'Button_variant_primary')]"));
    private static final Locator NHA_UPLOAD_PHOTO_SEARCH_CATEGORY_INPUT = new Locator("NHA_UPLOAD_PHOTO_SEARCH_CATEGORY_INPUT", new By.ByXPath("//div[contains(@class,'SearchBox')]//input"));
    private static final Locator NHA_UPLOAD_PHOTO_SELECT_CATEGORY_BUTTON = new Locator("NHA_UPLOAD_PHOTO_SELECT_CATEGORY_BUTTON", new By.ByXPath("//div[contains(@class,'absolute bottom')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator GENERAL_POLICY_FORM = new Locator("GENERAL_POLICY_FORM", new By.ByXPath("//div[contains(@class,'general-policy-wrap')]"));
    private static final Locator ROOM_LISTING_FORM = new Locator("ROOM_LISTING_FORM", new By.ByXPath("//div[@class='listing-detail-form-wrap']"));
    private static final Locator ROOM_TYPE_DROPDOWN_INPUT = new Locator("ROOM_TYPE_DROPDOWN_INPUT", new By.ByName("roomType"));
    private static final Locator ROOM_TYPE_DROPDOWN_DISPLAY = new Locator("ROOM_TYPE_DROPDOWN_DISPLAY", new By.ByXPath("//div[@data-testid='the-dropdown'][@id='roomType']//div[contains(@class,'dropdown-input')]"));
    private static final Locator ROOM_NAME_INPUT = new Locator("ROOM_NAME_INPUT", new By.ByName("name"));
    private static final Locator ROOM_MINIMUM_SELLING_PRICE_INPUT = new Locator("ROOM_MINIMUM_SELLING_PRICE_INPUT", new By.ByName("minimumSellPrice"));
    private static final Locator ROOM_SIZE_INPUT = new Locator("ROOM_SIZE_INPUT", new By.ByName("size"));
    private static final Locator ROOM_ADULT_OCCUPANCY_INPUT = new Locator("ROOM_ADULT_OCCUPANCY_INPUT", new By.ByName("adultOccupancy"));
    private static final Locator NHA_ROOM_ADULT_OCCUPANCY_LABEL = new Locator("ROOM_ADULT_OCCUPANCY_LABEL", new By.ByXPath("(//input[contains(@class,'QuantityEditor_quantity_input')])[1]"));
    private static final Locator NHA_ROOM_ADULT_OCCUPANCY_PLUS_BUTTON = new Locator("NHA_ROOM_ADULT_OCCUPANCY_PLUS_BUTTON", new By.ByXPath("(//*[contains(@class,'plus')]/parent::button)[1]"));
    private static final Locator ROOM_CHILD_OCCUPANCY_INPUT = new Locator("ROOM_CHILD_OCCUPANCY_INPUT", new By.ByName("childOccupancy"));
    private static final Locator ROOM_MAX_OCCUPANCY_INPUT = new Locator("ROOM_MAX_OCCUPANCY_INPUT", new By.ByName("roomMaxOccupancy"));
    private static final Locator NHA_ROOM_NO_OF_BATHROOM_LABEL = new Locator("NHA_ROOM_NO_OF_BATHROOM_LABEL", new By.ByXPath("(//input[contains(@class,'QuantityEditor_quantity_input')])[2]"));
    private static final Locator NHA_ROOM_NO_OF_BATHROOM_PLUS_BUTTON = new Locator("NHA_ROOM_NO_OF_BATHROOM_PLUS_BUTTON", new By.ByXPath("(//*[contains(@class,'plus')]/parent::button)[2]"));
    private static final Locator NHA_ROOM_NO_OF_BATHROOM_MINUS_BUTTON = new Locator("NHA_ROOM_NO_OF_BATHROOM_MINUS_BUTTON", new By.ByXPath("(//*[contains(@class,'minus')]/parent::button)[2]"));
    private static final Locator ROOM_ADD_BEDROOM_CONFIGURATION_BUTTON = new Locator("ROOM_ADD_BEDROOM_CONFIGURATION_BUTTON", new By.ByXPath("(//div[@class='add-btn'])[last()]"));
    private static final Locator NHA_ROOM_SINGLE_BEDROOM_ADD_BED_CONFIGURATION_BUTTON = new Locator("NHA_ROOM_SINGLE_BEDROOM_ADD_BED_CONFIGURATION_BUTTON", new By.ByXPath("//div[contains(@class,'ml-auto')]/span[contains(@class,'Text_weight_bold')]"));
    private static final Locator ROOM_LISTING_REVIEW_FORM = new Locator("ROOM_LISTING_REVIEW_FORM", new By.ByXPath("//div[@class='the-card listing-detail-review-card']"));
    private static final Locator NHA_ROOM_LISTING_REVIEW_ADD_ROOM_BUTTON = new Locator("NHA_ROOM_LISTING_REVIEW_ADD_ROOM_BUTTON", new By.ByXPath("//a[contains(@class,'Button')]"));
    private static final Locator PAYMENT_DETAIL_FORM = new Locator("PAYMENT_DETAIL_FORM", new By.ByXPath("//div[@class='the-card payment-detail-card']"));
    private static final Locator PAYMENT_DETAIL_BANK_NAME_DROPDOWN_INPUT = new Locator("PAYMENT_DETAIL_BANK_NAME_DROPDOWN_INPUT", new By.ByName("bankId"));
    private static final Locator PAYMENT_DETAIL_BANK_NAME_DROPDOWN_DISPLAY = new Locator("PAYMENT_DETAIL_BANK_NAME_DROPDOWN_DISPLAY", new By.ByXPath("//div[@data-testid='the-dropdown'][@id='bankId']//div[contains(@class,'dropdown-input')]"));
    private static final Locator NHA_PAYMENT_DETAIL_BANK_NAME_DROPDOWN_INPUT = new Locator("PAYMENT_DETAIL_BANK_NAME_DROPDOWN_INPUT", new By.ByXPath("//input[@id='dropdown']"));
    private static final Locator PAYMENT_DETAIL_BANK_BRANCH_INPUT = new Locator("PAYMENT_DETAIL_BANK_BRANCH_INPUT", new By.ByName("bankBranch"));
    private static final Locator PAYMENT_DETAIL_ACCOUNT_NUMBER_INPUT = new Locator("PAYMENT_DETAIL_ACCOUNT_NUMBER_INPUT", new By.ByName("accountNumber"));
    private static final Locator PAYMENT_DETAIL_ACCOUNT_HOLDER_NAME_INPUT = new Locator("PAYMENT_DETAIL_ACCOUNT_HOLDER_NAME_INPUT", new By.ByName("accountHolderName"));
    private static final Locator PAYMENT_DETAIL_SWIFT_CODE_INPUT = new Locator("PAYMENT_DETAIL_SWIFT_CODE_INPUT", new By.ByName("swiftCode"));
    private static final Locator REGISTRATION_REVIEW_FORM = new Locator("REGISTRATION_REVIEW_FORM", new By.ByXPath("//div[@class='the-card review-detail-card']"));
    private static final Locator REGISTRATION_REVIEW_CHANNEL_SOURCE_DROPDOWN_INPUT = new Locator("REGISTRATION_REVIEW_CHANNEL_SOURCE_DROPDOWN_INPUT", new By.ByName("channelSourceId"));
    private static final Locator REGISTRATION_REVIEW_CHANNEL_SOURCE_DROPDOWN_DISPLAY = new Locator("REGISTRATION_REVIEW_CHANNEL_SOURCE_DROPDOWN_DISPLAY", new By.ByXPath("//div[@data-testid='the-dropdown'][@id='channelSourceId']//div[contains(@class,'dropdown-input')]"));
    private static final Locator REGISTRATION_CONFIRM_MODAL = new Locator("REGISTRATION_CONFIRM_MODAL", new By.ByXPath("//div[@class='the-modal-content confirm-modal']"));
    private static final Locator NHA_REGISTRATION_CONFIRM_MODAL = new Locator("REGISTRATION_CONFIRM_MODAL", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')]"));
    private static final Locator REGISTRATION_CONFIRM_SUBMIT_BUTTON = new Locator("REGISTRATION_CONFIRM_SUBMIT_BUTTON", new By.ByXPath("(//div[@class='the-modal-content confirm-modal']//button[@class='the-button primary'])[1]"));
    private static final Locator NHA_REGISTRATION_CONFIRM_SUBMIT_BUTTON = new Locator("REGISTRATION_CONFIRM_SUBMIT_BUTTON", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator REGISTRATION_SUCCESS_PAGE = new Locator("REGISTRATION_SUCCESS_PAGE", new By.ByXPath("//div[@class='finish-container']"));
    private static final Locator REGISTRATION_SUCCESS_TO_EXTRANET_BUTTON = new Locator("REGISTRATION_SUCCESS_TO_EXTRANET_BUTTON", new By.ByXPath("//button[@class='the-button primary']"));
    private static final Locator REGISTRATION_SUCCESS_LOGOUT_BUTTON = new Locator("REGISTRATION_SUCCESS_LOGOUT_BUTTON", new By.ByXPath("//button[@class='the-button secondary']"));
    private static final Locator BANK_NAME_DROPDOWN_OPTION_AREA = new Locator("BANK_NAME_DROPDOWN_OPTION_AREA", new By.ByXPath("//div[contains(@class,'Dropdown')]/div[last()]"));
    private static final Locator PROVIDE_LATER_RADIO = new Locator("PROVIDE_LATER_RADIO", By.xpath("//label[contains(@class, 'radio-container') and (contains(., 'Saya akan mengisinya nanti') or contains(., 'provide it later'))]"));

    private static Locator getPropertyCategoryByName(String text) {
        int index;
        switch (text) {
            case "HOTEL" -> index = 1;
            case "NHA" -> index = 2;
            default -> throw new IllegalStateException("Unexpected value: " + text);
        };
        return new Locator("PROPERTY_CATEGORY", new By.ByXPath(String.format("//*[contains(@class,'property-category-container')]/div[%d]", index)));
    }

    private static Locator getNHAMenuCursorPointer(String text) {
        String menu;
        switch (text) {
            case "CONTACT_DETAIL" -> menu = "contactDetails";
            case "GENERAL_INFO" -> menu = "generalInfo";
            case "AGREEMENT_DETAIL" -> menu = "agreementDetails";
            case "PROPERTY_FACILITY" -> menu = "propertyFacilities";
            case "PROPERTY_PHOTO" -> menu = "propertyPhotos";
            case "UNIT_DETAIL" -> menu = "unitDetails";
            case "ROOM_DETAIL" -> menu = "roomDetails";
            case "PAYMENT_DETAIL" -> menu = "paymentDetails";
            case "REGISTRATION_REVIEW" -> menu = "registrationReview";
            default -> throw new IllegalStateException("Unexpected value: " + text);
        }
        return new Locator("FORM_MENU", new By.ByXPath(String.format("//div[@id='menu-%s'][contains(@class,'cursor-pointer')]", menu)));
    }

    private static Locator getGuestTypeByName(String text) {
        int index;
        switch (text) {
            case "FAMILY" -> index = 1;
            case "SOLO_TRAVELER" -> index = 2;
            case "GROUP_TRAVELER" -> index = 3;
            case "COUPLE" -> index = 4;
            default -> throw new IllegalStateException("Unexpected value: " + text);
        };
        return new Locator("GUEST_TYPE", new By.ByXPath(String.format("//div[@class='guest-type '][%d]", index)));
    }

    private static Locator getPropertyTypeDropdownOptionByName(String text) {
        return new Locator("PROPERTY_TYPE_OPTION", new By.ByXPath(String.format("//div[@class='dropdown-options']/div[@class='option-item '][text()='%s']", text)));
    }

    private static Locator getNHAPropertyTypeDropdownOptionByName(String text) {
        return new Locator("PROPERTY_TYPE_OPTION", new By.ByXPath(String.format("//li[@data-name='propertyType'][text()='%s']", text)));
    }

    private static Locator getNHAPlaceTypeRadioButton(String text) {
        String placeType;
        switch (text) {
            case "ENTIRE_PLACE_PRIVATE" -> placeType = "ENTIRE_PLACE:private";
            case "ENTIRE_PLACE_SHARED" -> placeType = "ENTIRE_PLACE:shared";
            case "UNIT" -> placeType = "UNIT";
            default -> throw new IllegalStateException("Unexpected value: " + text);
        }
        return new Locator("PLACE_TYPE_RADIO", new By.ByXPath(String.format("//input[@name='placeType'][contains(@value,'%s')]/following-sibling::span", placeType)));
    }

    private static Locator getPropertyStarRatingOption(int starRating) {
        return new Locator("STAR_RATING_OPTION", new By.ByXPath(String.format("(//div[@id='star']//div[contains(@class,'custom-radio-container')])[%d]", starRating+1)));
    }

    private static Locator getPropertyUseChainRadioOption(boolean isUseChain) {
        int index = 2;
        if (isUseChain) {
            index = 1;
        }
        return new Locator("USE_CHAIN_RADIO", new By.ByXPath(String.format("(//input[@name='useChain']/following-sibling::span)[%d]", index)));
    }

    private static Locator getNHAPropertyUseChainRadioOption(boolean isUseChain) {
        String value = "false";
        if (isUseChain) {
            value = "true";
        }
        return new Locator("USE_CHAIN_RADIO", new By.ByXPath(String.format("//input[@name='isChain'][contains(@value,'%s')]/following-sibling::span", value)));
    }

    private static Locator getPropertyCurrency(String currencyCode) {
        return new Locator("CURRENCY_CODE", new By.ByXPath(String.format("//div[@id='%s']", currencyCode.toLowerCase())));
    }

    private static Locator getNHAPropertyCurrency(String currencyCode) {
        return new Locator("CURRENCY_CODE", new By.ByXPath(String.format("//li[@data-name='currency'][@data-value='%s']", currencyCode)));
    }

    private static Locator getNHAMapSearchResultList(String text) {
        return new Locator("MAP_SEARCH_RESULT", new By.ByXPath(String.format("//span[contains(@class,'highlighted Text')][text()='%s']", text.toUpperCase())));
    }

    private static Locator getContactDetailRoleDropdownOption(String text) {
        return new Locator("CONTACT_ROLE_OPTION", new By.ByXPath(String.format("//div[@class='dropdown-options']/div[contains(@class,'option-item')][text()='%s']", text)));
    }

    private static Locator getNHAContactDetailRoleDropdownOption(String text) {
        return new Locator("CONTACT_ROLE_OPTION", new By.ByXPath(String.format("//li[@data-name='position'][text()='%s']", text)));
    }

    private static Locator getPartnershipAgreementCountryDropdownOption(String text) {
        return new Locator("COUNTRY_OPTION", new By.ByXPath(String.format("//div[@class='dropdown-options']/div[contains(@class,'option-item')][text()='%s']", text)));
    }

    private static Locator getNHAPartnershipAgreementCountryDropdownOption(String text) {
        return new Locator("COUNTRY_OPTION", new By.ByXPath(String.format("//li[@data-name='countryId'][text()='%s']", text)));
    }

    private static String getOtherPlatformName(String platformName) {
        return switch (platformName) {
            case "AIRBNB" -> "Airbnb";
            case "TRIPADVISOR" -> "Tripadvisor";
            case "BOOKINGCOM" -> "Booking.com";
            case "AGODA" -> "Agoda.com";
            case "TRAVELOKA" -> "Traveloka";
            case "PEGIPEGI" -> "Pegipegi";
            case "OTHER" -> "Other";
            case "UNREGISTERED" -> "Properti saya belum terdaftar di platform lain";
            default -> throw new IllegalStateException("Unexpected value: " + platformName);
        };
    }

    private static Locator getOtherPlatformCheckboxState(String platformName) {
        return new Locator("PLATFORM_CHECKBOX_STATE", new By.ByXPath(String.format("//div[@id='otherPlatform']//input[@type='checkbox'][@name='%s']", getOtherPlatformName(platformName))));
    }

    private static Locator getOtherPlatformCheckbox(String platformName) {
        return new Locator("PLATFORM_CHECKBOX", new By.ByXPath(String.format("//div[@id='otherPlatform']//input[@type='checkbox'][@name='%s']/following-sibling::span", getOtherPlatformName(platformName))));
    }

    private static Locator getNHAOtherPlatformCheckbox(String platformName) {
        return new Locator("PLATFORM_CHECKBOX", new By.ByXPath(String.format("//input[contains(@name,'platformEnabled') or (@type='checkbox')]/following-sibling::span[contains(@class,'Text')]/span[text()='%s']", getOtherPlatformName(platformName))));
    }

    private static Locator getNHAOtherPlatformPropertyLinkInput(String platformName) {
        return new Locator("PROPERTY_LINK_INPUT", new By.ByXPath(String.format("//input[contains(@id,'link-properti-anda-di-%S')]", getOtherPlatformName(platformName).replaceAll("[.]", ""))));
    }

    private static Locator getPropertyFacilityCheckboxByIndex(int index) {
        return new Locator("FACILITY_CHECKBOX", new By.ByXPath(String.format("(//div[contains(@class,'facility-checkbox')]//span)[%d]", index)));
    }

    private static Locator getNHAFacilityChipByIndex(int index) {
        return new Locator("FACILITY_CHIP", new By.ByXPath(String.format("(//button[@type='button'][contains(@class,'Chip')])[%d]", index)));
    }

    private static Locator getUploadPhotoCategoryDropdownInputByIndex(int index) {
        return new Locator("PHOTO_CATEGORY_INPUT", new By.ByXPath(String.format("(//input[contains(@name,'category')])[%d]", index)));
    }

    private static Locator getUploadPhotoCategoryDropdownOptionByIndex(int index, String text) {
        return new Locator("PHOTO_CATEGORY_OPTION", new By.ByXPath(String.format("(//div[@data-testid='the-dropdown'][contains(@id,'category')])[%d]//label[text()='%s']/parent::span[contains(@class,'popoverlist-list')]", index, text)));
    }

    private static Locator getNHAUploadPhotoCategoryDropdownOption(String text) {
        return new Locator("PHOTO_CATEGORY_OPTION", new By.ByXPath(String.format("//div[contains(@class,'white rounded')]//div[contains(@class,'cursor-pointer') and contains(@class,'Text')][text()='%s']", text)));
    }

    private static Locator getUploadPhotoButtonByIndex(int index) {
        return new Locator("UPLOAD_PHOTO_BUTTON", new By.ByXPath(String.format("(//div[contains(@class,'upload-btn')])[%d]", index)));
    }

    private static Locator getUploadPhotoUploadedImageCheckboxByIndex(int index) {
        return new Locator("UPLOADED_IMAGE_CHECKBOX", new By.ByXPath(String.format("(//div[@class='the-image-uploader']//div[contains(@class,'the-image')]/img)[%d]/following-sibling::div[@class='the-image-checkbox']//span", index)));
    }

    private static Locator getNHAUploadPhotoUploadedImageCheckboxByIndex(int index) {
        return new Locator("UPLOADED_IMAGE_CHECKBOX", new By.ByXPath(String.format("(//img[@alt='img-upload'][@sizes='100vw']/parent::span/parent::div)[%d]", index)));
    }

    private static Locator getUploadPhotoMainPhotoButtonByIndex(int index) {
        return new Locator("MAIN_PHOTO_BUTTON", new By.ByXPath(String.format("(//button[@class='the-image-label-container'])[%d]", index)));
    }

    private static int resolveGenderTypeIndex(String text) {
        return switch (text) {
            case "WOMEN_ONLY" -> 1;
            case "MEN_ONLY" -> 2;
            case "BOTH" -> 3;
            default -> throw new IllegalStateException("Unexpected value: " + text);
        };
    }

    private static Locator getGeneralPolicyGenderTypeRadioButton(String text) {
        int index = resolveGenderTypeIndex(text);
        return new Locator("GENDER_RADIO", new By.ByXPath(String.format("(//input[@name='genderType']/following-sibling::span)[%d]", index)));
    }

    private static Locator getGeneralPolicyGenderTypeInput(String text) {
        int index = resolveGenderTypeIndex(text);
        return new Locator("GENDER_INPUT", new By.ByXPath(String.format("(//input[@name='genderType'])[%d]", index)));
    }

    private static String resolveGeneralPolicyInputName(String type) {
        return switch (type) {
            case "MARRIAGE_REGULATION" -> "marriageRegulation";
            case "PAY_DEPOSIT" -> "payDeposit";
            case "ALLOW_ALL_AGE" -> "allowAllAge";
            case "BREAKFAST" -> "breakfastTime";
            case "ALLOW_SMOKING" -> "allowSmoking";
            case "ALLOW_ALCOHOL" -> "allowAlcoholicDrink";
            case "ALLOW_PETS" -> "allowPets";
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    private static Locator getGeneralPolicyRadioButtonByType(String type, boolean isEligible) {
        String name = resolveGeneralPolicyInputName(type);
        int index = isEligible ? 1 : 2;
        return new Locator("GENERAL_POLICY_RADIO_TYPE", new By.ByXPath(String.format("(//input[@name='%s']/following-sibling::span)[%d]", name, index)));
    }

    private static Locator getGeneralPolicyInputByType(String type, boolean isEligible) {
        String name = resolveGeneralPolicyInputName(type);
        int index = isEligible ? 1 : 2;
        return new Locator("GENERAL_POLICY_INPUT_TYPE", new By.ByXPath(String.format("(//input[@name='%s'])[%d]", name, index)));
    }

    private static Locator getRoomTypeDropdownOption(String text) {
        return new Locator("ROOM_TYPE_OPTION", new By.ByXPath(String.format("//div[@data-testid='the-dropdown'][@id='roomType']//label[text()='%s']/parent::span[contains(@class,'popoverlist-list')]", text)));
    }

    private static Locator getRoomSmokingPreferenceRadioButton(boolean isSmokingAllowed) {
        String value = "NS";
        if (isSmokingAllowed) {
            value = "S";
        }
        return new Locator("SMOKING_PREFERENCE_RADIO", new By.ByXPath(String.format("//input[@name='smokingPreference'][@value='%s']/following-sibling::span", value)));
    }

    private static Locator getRoomHasMultipleBedroomRadioButton(boolean hasMultipleBedroom) {
        String value = "no";
        if (hasMultipleBedroom) {
            value = "yes";
        }
        return new Locator("HAS_MULTIPLE_BEDROOM_RADIO", new By.ByXPath(String.format("//input[@name='hasMultipleRoom'][@value='%s']/following-sibling::span", value)));
    }

    private static Locator getNHARoomHasMultipleBedroomRadioButton(boolean hasMultipleBedroom) {
        String value = "no";
        if (hasMultipleBedroom) {
            value = "yes";
        }
        return new Locator("HAS_MULTIPLE_BEDROOM_RADIO", new By.ByXPath(String.format("//input[@name='bedConfig'][contains(@value,'%s')]/parent::span", value)));
    }

    private static List<Locator> getRoomConfigurationDropdownInputByIndex(int bedroomIndex, int bedTypeIndex) {
        List<Locator> locators = new ArrayList<>();
        locators.add(new Locator("BED_TYPE_INPUT", new By.ByXPath(String.format("//div[@data-testid='the-dropdown']//input[contains(@name,'roomBedType-room%d-type%d')]", bedroomIndex, bedTypeIndex))));
        locators.add(new Locator("NO_OF_BED_INPUT", new By.ByXPath(String.format("//div[@data-testid='the-dropdown']//input[contains(@name,'amount-room%d-type%d')]", bedroomIndex, bedTypeIndex))));
        return locators;
    }

    private static List<Locator> getRoomConfigurationDropdownOptionByIndex(int bedroomIndex, int bedTypeIndex, String bedType, String bedAmount) {
        List<Locator> locators = new ArrayList<>();
        locators.add(new Locator("BED_TYPE_OPTION", new By.ByXPath(String.format("//div[@data-testid='the-dropdown'][contains(@id,'roomBedType-room%d-type%d')]//label[text()='%s']/parent::span[contains(@class,'popoverlist-list')]", bedroomIndex, bedTypeIndex, bedType))));
        locators.add(new Locator("NO_OF_BED_OPTION", new By.ByXPath(String.format("//div[@data-testid='the-dropdown'][contains(@id,'amount-room%d-type%d')]//label[text()='%s']/parent::span[contains(@class,'popoverlist-list')]", bedroomIndex, bedTypeIndex, bedAmount))));
        return locators;
    }

    private static Locator getRoomAddBedTypeConfigurationByBedroomIndex(int index) {
        return new Locator("ADD_BED_TYPE_BUTTON", new By.ByXPath(String.format("(//div[@class='add-btn'])[%d]", index)));
    }

    private static Locator getNHASingleBedroomBedTypeDropdownInputByIndex(int index) {
        return new Locator("BED_TYPE_DROPDOWN_INPUT", new By.ByXPath(String.format("(//input[@id='dropdown'])[%d]", index)));
    }

    private static Locator getNHASingleBedroomBedTypeDropdownOptionByIndex(int index, String text) {
        return new Locator("BED_TYPE_DROPDOWN_OPTION", new By.ByXPath(String.format("(//li[@data-name='bedType-0-bedroom-%d'])[text()='%s']", index, text)));
    }

    private static Locator getNHASingleBedroomNoOfBedLabelByIndex(int index) {
        return new Locator("NO_OF_BED_LABEL", new By.ByXPath(String.format("(//div[contains(@class,'bg-N50')]//input[contains(@class,'QuantityEditor_quantity_input')])[%d]", index)));
    }

    private static Locator getNHASingleBedroomNoOfBedPlusButtonByIndex(int index) {
        return new Locator("NO_OF_BED_PLUS_BUTTON", new By.ByXPath(String.format("(//div[contains(@class,'bg-N50')]//*[contains(@class,'plus')]/parent::button)[%d]", index)));
    }

    private static Locator getNHAListingReviewRoomNameLabelByIndex(int index) {
        return new Locator("ROOM_NAME_LABEL", new By.ByXPath(String.format("(//form//div[contains(@class,'Card_padding_medium') and contains(@class,'Card_variant_outline')][%d]//span)[1]", index)));
    }

    private static Locator getRoomFacilityCheckboxByIndex(int index) {
        return new Locator("FACILITY_CHECKBOX", new By.ByXPath(String.format("(//div[@id='facilityIds0']//input[@type='checkbox']/following-sibling::span)[%d]", index)));
    }

    private static Locator getPaymentMethodRadioButton(String text) {
        return new Locator("PAYMENT_METHOD_RADIO", new By.ByXPath(String.format("//input[@name='paymentMethod'][contains(@value,'%s')]/following-sibling::span", text)));
    }

    private static Locator getPaymentMethodInput(String text) {
        return new Locator("PAYMENT_METHOD_INPUT", new By.ByXPath(String.format("//input[@name='paymentMethod'][contains(@value,'%s')]", text)));
    }

    private static Locator getPaymentDetailBankNameDropdownOption(String text) {
        return new Locator("BANK_NAME_OPTION", new By.ByXPath(String.format("//div[@data-testid='the-dropdown'][@id='bankId']//label[text()='%s']/parent::span[contains(@class,'popoverlist-list')]", text)));
    }

    private static Locator getNHAPaymentDetailBankNameDropdownOption(String text) {
        return new Locator("BANK_NAME_OPTION", new By.ByXPath(String.format("//li[@data-name='bankId'][text()='%s']", text)));
    }

    private static int getChannelSourceNameByIndex(String channelName) {
        return switch (channelName) {
            case "MARKET_MANAGER" -> 1;
            case "ADVERTISEMENT" -> 2;
            case "POST" -> 3;
            case "APPS" -> 4;
            case "FRIENDS_FAMILY" -> 5;
            case "OTHER" -> 6;
            default -> throw new IllegalStateException("Unexpected value: " + channelName);
        };
    }

    private static Locator getRegistrationReviewChannelSourceDropdownOption(String text) {
        return new Locator("CHANNEL_SOURCE_OPTION", new By.ByXPath(String.format("(//div[@data-testid='the-dropdown'][@id='channelSourceId']//span[contains(@class,'popoverlist-list')])[%d]", getChannelSourceNameByIndex(text))));
    }

    private static Locator getNHARegistrationReviewChannelSourceDropdownOption(String text) {
        return new Locator("CHANNEL_SOURCE_OPTION", new By.ByXPath(String.format("(//input[@name='referralRadio']/following-sibling::span)[%d]", getChannelSourceNameByIndex(text))));
    }

    public void clickNextSubmitOnPropertyCategoryPage() {
        WebElement button = driverTools.waitForElementToBeClickable(NEXT_SUBMIT_BUTTON_PROPERTY_CATEGORY_PAGE).orElseThrow();
        driverTools.click(button);
    }
    
    public void clickNextSubmit() {
        WebElement button = driverTools.waitForElementToBeClickable(NEXT_SUBMIT_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isConfirmationPopupDialogAppeared() {
        return driverTools.waitForElementToAppear(CONFIRMATION_POPUP_DIALOG).isPresent();
    }

    public void confirmPopupDialog() {
        WebElement button = driverTools.waitForElementToBeClickable(CONFIRMATION_CONTINUE_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isPropertyCategoryPageDisplayed() {
        return driverTools.waitForElementToAppear(PROPERTY_CATEGORY_HOTEL_CONTAINER).isPresent();
    }

    public void selectPropertyCategory(String text) {
        WebElement element = driverTools.waitForElementToBeClickable(getPropertyCategoryByName(text)).orElseThrow();
        driverTools.clickElementByCoordinateDWeb(element);
    }

    public boolean isGuestAndPropertyTypeFormDisplayed() {
        return driverTools.waitForElementToAppear(GUEST_AND_PROPERTY_TYPE_FORM).isPresent();
    }

    public void selectGuestType(String text) {
        WebElement radio = driverTools.waitForElementToAppear(getGuestTypeByName(text)).orElseThrow();
        driverTools.click(radio);
    }

    public void searchAndSelectPropertyType(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(PROPERTY_TYPE_DROPDOWN_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement search = driverTools.waitForElementToAppear(PROPERTY_TYPE_SEARCH_INPUT).orElseThrow();
        driverTools.sendKeys(search, text);
        WebElement option = driverTools.waitForElementToBeClickable(getPropertyTypeDropdownOptionByName(text)).orElseThrow();
        driverTools.click(option);
    }

    public void searchAndSelectNHAPropertyType(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(NHA_GENERAL_INFO_PROPERTY_TYPE_INPUT).orElseThrow();
        driverTools.click(input);
        if (!driverTools.waitForElementToBeClickable(getNHAPropertyTypeDropdownOptionByName(text)).isPresent()) {
            driverTools.click(input);
        }
        driverTools.sendKeys(input, text);
        WebElement option = driverTools.waitForElementToBeClickable(getNHAPropertyTypeDropdownOptionByName(text)).orElseThrow();
        driverTools.click(option);
    }

    public boolean isGeneralInfoFormDisplayed() {
        return driverTools.waitForElementToAppear(GENERAL_INFO_FORM).isPresent();
    }

    public void selectNHAPlaceType(String text) {
        WebElement radio = driverTools.waitForElementToBeClickable(getNHAPlaceTypeRadioButton(text)).orElseThrow();
        driverTools.click(radio);
    }

    public void enterPropertyName(String text) {
        WebElement input = driverTools.waitForElementToAppear(GENERAL_INFO_PROPERTY_NAME_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, text);
    }

    public void enterNHAPropertyName(String text) {
        WebElement input = driverTools.waitForElementToAppear(NHA_GENERAL_INFO_PROPERTY_NAME_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, text);
    }

    public void enterPropertyNameAlias(String text) {
        WebElement input = driverTools.waitForElementToAppear(GENERAL_INFO_PROPERTY_NAME_ALIAS_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, text);
    }

    public void selectPropertyStarRating(int rating) {
        WebElement button = driverTools.waitForElementToAppear(getPropertyStarRatingOption(rating)).orElseThrow();
        driverTools.click(button);
    }

    public void selectPropertyUseChainRadio(boolean isUseChain) {
        WebElement radio = driverTools.waitForElementToAppear(getPropertyUseChainRadioOption(isUseChain)).orElseThrow();
        driverTools.click(radio);
    }

    public void selectNHAPropertyUseChainRadio(boolean isUseChain) {
        boolean isClicked = false;
        int count = 1;
        while (!isClicked && count <= SCROLL_MAX_RETRY) {
            try {
                WebElement radio = driverTools.waitForElementToBeClickable(getNHAPropertyUseChainRadioOption(isUseChain)).orElseThrow();
                driverTools.click(radio);
                isClicked = true;
            } catch (Exception e) {
                scrollForm();
                count++;
            }
        }
    }

    public void searchAndSelectPropertyCurrency(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(GENERAL_INFO_CURRENCY_DROPDOWN_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement search = driverTools.waitForElementToAppear(GENERAL_INFO_CURRENCY_SEARCH_INPUT).orElseThrow();
        driverTools.clearInput(search);
        driverTools.sendKeys(search, text);
        WebElement option = driverTools.waitForElementToBeClickable(getPropertyCurrency(text)).orElseThrow();
        driverTools.click(option);
    }

    public void searchAndSelectNHAPropertyCurrency(String text) {
        boolean isClicked = false;
        int count = 1;
        while (!isClicked && count <= SCROLL_MAX_RETRY) {
            try {
                WebElement input = driverTools.waitForElementToBeClickable(NHA_GENERAL_INFO_CURRENCY_DROPDOWN_INPUT).orElseThrow();
                driverTools.click(input);
                isClicked = true;
            } catch (Exception e) {
                scrollForm();
                count++;
            }
        }
        WebElement search = driverTools.waitForElementToAppear(NHA_GENERAL_INFO_CURRENCY_DROPDOWN_INPUT).orElseThrow();
        driverTools.clearInput(search);
        driverTools.sendKeys(search, text);
        WebElement option = driverTools.waitForElementToBeClickable(getNHAPropertyCurrency(text)).orElseThrow();
        driverTools.click(option);
    }

    public void enterPropertyAddress(String text) {
        WebElement input = driverTools.waitForElementToAppear(GENERAL_INFO_ADDRESS_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void enterPropertyZipCode(String text) {
        WebElement input = driverTools.waitForElementToAppear(GENERAL_INFO_ZIP_CODE).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void searchAndSelectMapLocation(String text) {
        WebElement input = driverTools.waitForElementToAppear(GENERAL_INFO_MAP_SEARCH_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);

        Optional<WebElement> suggestion = driverTools.waitForElementToBeClickable(GENERAL_INFO_MAP_FIRST_SUGGESTION, 5);
        int remainingChars = text.length();
        while (suggestion.isEmpty() && remainingChars > 0) {
            driverTools.sendKeys(input, Keys.chord(Keys.BACK_SPACE));
            remainingChars--;
            suggestion = driverTools.waitForElementToBeClickable(GENERAL_INFO_MAP_FIRST_SUGGESTION, 3);
        }

        suggestion.ifPresentOrElse(driverTools::click, () -> {
            driverTools.sendKeys(input, Keys.chord(Keys.DOWN));
            driverTools.sendKeys(input, Keys.chord(Keys.ENTER));
        });
    }

    public void searchAndSelectNHAMapLocation(String text) {
        boolean isClicked = false;
        int count = 1;
        while (!isClicked && count <= SCROLL_MAX_RETRY) {
            try {
                WebElement input = driverTools.waitForElementToAppear(NHA_GENERAL_INFO_MAP_SEARCH_INPUT).orElseThrow();
                driverTools.click(input);
                driverTools.sendKeys(input, text);
                isClicked = true;
            } catch (Exception e) {
                scrollForm();
                count++;
            }
        }
        WebElement result = driverTools.waitForElementToBeClickable(getNHAMapSearchResultList(text)).orElseThrow();
        driverTools.click(result);
        WebElement confirm = driverTools.waitForElementToBeClickable(NHA_GENERAL_INFO_MAP_CONFIRM_BUTTON).orElseThrow();
        driverTools.click(confirm);
    }

    public boolean isContactDetailFormDisplayed() {
        return driverTools.waitForElementToAppear(CONTACT_DETAIL_FORM).isPresent();
    }

    public void selectContactDetailRole(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(CONTACT_DETAIL_ROLE_DROPDOWN_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement option = driverTools.waitForElementToBeClickable(getContactDetailRoleDropdownOption(text)).orElseThrow();
        driverTools.click(option);
    }

    public void selectNHAContactDetailRole(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(NHA_CONTACT_DETAIL_ROLE_DROPDOWN_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement option = driverTools.waitForElementToBeClickable(getNHAContactDetailRoleDropdownOption(text)).orElseThrow();
        driverTools.click(option);
    }

    public void enterContactDetailPropertyPhoneNumber(String text) {
        WebElement input = driverTools.waitForElementToAppear(CONTACT_DETAIL_PROPERTY_PHONE_NUMBER_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void enterNHAContactDetailPropertyPhoneNumber(String text) {
        WebElement input = driverTools.waitForElementToAppear(NHA_CONTACT_DETAIL_PROPERTY_PHONE_NUMBER_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public boolean isPartnershipAgreementFormDisplayed() {
        return driverTools.waitForElementToAppear(PARTNERSHIP_AGREEMENT_FORM).isPresent();
    }

    public void enterPartnershipAgreementCompanyName(String text) {
        fillInputWithRetry(PARTNERSHIP_AGREEMENT_COMPANY_NAME_INPUT, text);
    }

    public void enterPartnershipAgreementCompanyAddress(String text) {
        fillInputWithRetry(PARTNERSHIP_AGREEMENT_COMPANY_ADDRESS_INPUT, text);
    }

    public void selectPartnershipAgreementCompanyCountry(String text) {
        selectDropdownOptionWithRetry(
                PARTNERSHIP_AGREEMENT_COUNTRY_DROPDOWN_INPUT,
                getPartnershipAgreementCountryDropdownOption(text),
                text);
    }

    public void copyPartnershipAgreementContact() {
        WebElement button = driverTools.waitForElementToBeClickable(PARTNERSHIP_AGREEMENT_COPY_CONTACT_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    public void checkPartnershipAgreement() {
        List<WebElement> checkboxes = driverTools.waitForElementsToAppear(PARTNERSHIP_AGREEMENT_CHECKBOX).orElseThrow();
        for (WebElement checkbox : checkboxes) {
            driverTools.click(checkbox);
        }
    }

    public void confirmPartnershipAgreement() {
        driverTools.waitForElementToAppear(PARTNERSHIP_AGREEMENT_CONFIRM_MODAL);
        WebElement button = driverTools.waitForElementToBeClickable(PARTNERSHIP_AGREEMENT_CONFIRM_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void enterNHAPartnershipAgreementCompanyAddress(String text) {
        WebElement input = driverTools.waitForElementToAppear(NHA_PARTNERSHIP_AGREEMENT_COMPANY_ADDRESS_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void selectNHAPartnershipAgreementCompanyCountry(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(NHA_PARTNERSHIP_AGREEMENT_COUNTRY_DROPDOWN_INPUT).orElseThrow();
        driverTools.click(input);
        driverTools.sendKeys(input, text);
        WebElement option = driverTools.waitForElementToBeClickable(getNHAPartnershipAgreementCountryDropdownOption(text)).orElseThrow();
        driverTools.click(option);
    }

    public void copyNHAPartnershipAgreementContact() {
        boolean isClicked = false;
        int count = 1;
        while (!isClicked && count <= SCROLL_MAX_RETRY) {
            try {
                WebElement button = driverTools.waitForElementToBeClickable(NHA_PARTNERSHIP_AGREEMENT_COPY_CONTACT_BUTTON).orElseThrow();
                driverTools.clickWithJavascriptExecutor(button);
                isClicked = true;
            } catch (Exception e) {
                scrollForm();
                count++;
            }
        }
    }

    public void checkNHAPartnershipAgreement() {
        boolean isClicked = false;
        int count = 1;
        while (!isClicked && count <= SCROLL_MAX_RETRY) {
            try {
                List<WebElement> checkboxes = driverTools.waitForElementsToAppear(NHA_PARTNERSHIP_AGREEMENT_CHECKBOX).orElseThrow();
                for (WebElement checkbox : checkboxes) {
                    driverTools.click(checkbox);
                }
                isClicked = true;
            } catch (Exception e) {
                scrollForm();
                count++;
            }
        }
    }

    public void confirmNHAPartnershipAgreement() {
        driverTools.waitForElementToAppear(NHA_PARTNERSHIP_AGREEMENT_CONFIRM_MODAL);
        WebElement button = driverTools.waitForElementToBeClickable(NHA_PARTNERSHIP_AGREEMENT_CONFIRM_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isOtherPlatformFormDisplayed() {
        return driverTools.waitForElementToAppear(OTHER_PLATFORM_FORM).isPresent();
    }

    public void checkOtherPlatformAvailability(String text) {
        boolean initialState = driverTools.isElementAttributePresent(getOtherPlatformCheckboxState(text), "checked");
        if (!initialState) {
            WebElement checkbox = driverTools.waitForElementToBeClickable(getOtherPlatformCheckbox(text)).orElseThrow();
            checkbox.click();
        }
    }

    public void checkNHAOtherPlatformAvailability(String text, String url) {
        boolean isClicked = false;
        int count = 1;
        while (!isClicked && count <= SCROLL_MAX_RETRY) {
            try {
                WebElement checkbox = driverTools.waitForElementToBeClickable(getNHAOtherPlatformCheckbox(text)).orElseThrow();
                driverTools.click(checkbox);
                isClicked = true;
            } catch (Exception e) {
                scrollForm();
                count++;
            }
        }
        if (!text.equals("UNREGISTERED")) {
            WebElement input = driverTools.waitForElementToAppear(getNHAOtherPlatformPropertyLinkInput(text)).orElseThrow();
            driverTools.sendKeys(input, url);
        }
    }

    public boolean isPropertyFacilityFormDisplayed() {
        return driverTools.waitForElementToAppear(PROPERTY_FACILITY_FORM).isPresent();
    }

    public void checkPropertyFacilityByIndex(int index) {
        WebElement checkbox = driverTools.waitForElementToBeClickable(getPropertyFacilityCheckboxByIndex(index)).orElseThrow();
        driverTools.click(checkbox);
    }

    public void enterNHANoOfFloor(String total) {
        WebElement input = driverTools.waitForElementToAppear(NHA_NO_OF_FLOOR_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, total);
    }

    public void checkNHAFacilityByIndex(int index) {
        WebElement checkbox = driverTools.waitForElementToBeClickable(getNHAFacilityChipByIndex(index)).orElseThrow();
        boolean isClicked = false;
        int count = 1;
        while (!isClicked && count <= SCROLL_MAX_RETRY) {
            try {
                driverTools.click(checkbox);
                isClicked = true;
            } catch (Exception e) {
                scrollForm();
                count++;
            }
        }
    }

    public boolean isUploadPropertyPhotoFormDisplayed() {
        return driverTools.waitForElementToAppear(UPLOAD_PROPERTY_PHOTO_FORM).isPresent();
    }

    public void selectPhotoCategoryByIndex(int index, String text) {
        WebElement input = driverTools.waitForElementToBeClickable(getUploadPhotoCategoryDropdownInputByIndex(index)).orElseThrow();
        driverTools.click(input);
        WebElement option = driverTools.waitForElementToBeClickable(getUploadPhotoCategoryDropdownOptionByIndex(index, text)).orElseThrow();
        driverTools.click(option);
    }

    public void clickUploadPhotoByIndex(int index) {
        WebElement button = driverTools.waitForElementToBeClickable(getUploadPhotoButtonByIndex(index)).orElseThrow();
        driverTools.click(button);
    }

    public boolean isUploadPhotoModalAppeared() {
        return driverTools.waitForElementToAppear(UPLOAD_PHOTO_UPLOADED_MODAL).isPresent();
    }

    public void closeUploadPhotoModal() {
        WebElement button = driverTools.waitForElementToBeClickable(UPLOAD_PHOTO_MODAL_CLOSE_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public int getUploadedPhotoOnModal() {
        try {
            return driverTools.waitForElementsToAppear(UPLOAD_PHOTO_UPLOADED_IMAGE_CHECKBOX).orElseThrow().size();
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    public void uploadPhotoByUrl(String fileName) {
        WebElement input = driverTools.waitForElementToPresence(UPLOAD_PHOTO_URL_INPUT).orElseThrow();
        String path = System.getProperty("user.dir") + "/src/test/resources/dashboard/hotel/";
        driverTools.sendKeys(input, path+fileName);
        driverTools.waitForElementToAppear(UPLOAD_PHOTO_PROGRESS_BAR).orElseThrow();
        driverTools.waitForElementToDisappear(UPLOAD_PHOTO_PROGRESS_BAR);
    }

    public void uploadNHAPhotoByUrl(String fileName) {
        WebElement input = driverTools.waitForElementToPresence(NHA_UPLOAD_PHOTO_URL_INPUT).orElseThrow();
        String path = System.getProperty("user.dir") + "/src/test/resources/dashboard/hotel/";
        driverTools.sendKeys(input, path+fileName);
        driverTools.waitForElementToDisappear(NHA_UPLOAD_PHOTO_PROGRESS_BAR);
    }

    public void checkUploadedPhotoByIndex(int index) {
        WebElement checkbox = driverTools.waitForElementToBeClickable(getUploadPhotoUploadedImageCheckboxByIndex(index)).orElseThrow();
        driverTools.click(checkbox);
    }

    public void checkNHAUploadedPhotoByIndex(int index) {
        WebElement checkbox = driverTools.waitForElementToBeClickable(getNHAUploadPhotoUploadedImageCheckboxByIndex(index)).orElseThrow();
        driverTools.click(checkbox);
    }

    public void checkAllUploadedPhoto() {
        List<WebElement> checkboxes = driverTools.waitForElementsToAppear(UPLOAD_PHOTO_UPLOADED_IMAGE_CHECKBOX).orElseThrow();
        for (WebElement checkbox : checkboxes) {
            driverTools.click(checkbox);
        }
    }

    public void checkAllUploadedPhoto(int total) {
        for (int i=total; i>0; i--) {
            checkUploadedPhotoByIndex(i);
        }
    }

    public void checkNHAAllUploadedPhoto() {
        List<WebElement> checkboxes = driverTools.waitForElementsToAppear(NHA_UPLOAD_PHOTO_UPLOADED_IMAGE_CHECKBOX).orElseThrow();
        for (WebElement checkbox : checkboxes) {
            driverTools.click(checkbox);
        }
    }

    public void checkNHAAllUploadedPhoto(int total) {
        for (int i=total; i>0; i--) {
            checkNHAUploadedPhotoByIndex(i);
        }
    }

    public void clickAddUploadedPhoto() {
        WebElement button = driverTools.waitForElementToBeClickable(UPLOAD_PHOTO_UPLOADED_MODAL_ADD_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void setMainPhotoByIndex(int index) {
        WebElement button = driverTools.waitForElementToBeClickable(getUploadPhotoMainPhotoButtonByIndex(index)).orElseThrow();
        driverTools.click(button);
    }

    public void managePhotoCategory(String text) {
        WebElement manage = driverTools.waitForElementToBeClickable(NHA_UPLOAD_PHOTO_MANAGE_CATEGORY_BUTTON).orElseThrow();
        driverTools.click(manage);
        WebElement search = driverTools.waitForElementToAppear(NHA_UPLOAD_PHOTO_SEARCH_CATEGORY_INPUT).orElseThrow();
        driverTools.sendKeys(search, text);
        WebElement option = driverTools.waitForElementToBeClickable(getNHAUploadPhotoCategoryDropdownOption(text)).orElseThrow();
        driverTools.click(option);
        WebElement select = driverTools.waitForElementToBeClickable(NHA_UPLOAD_PHOTO_SELECT_CATEGORY_BUTTON).orElseThrow();
        driverTools.click(select);
    }

    public boolean isGeneralPolicyFormDisplayed() {
        return driverTools.waitForElementToAppear(GENERAL_POLICY_FORM).isPresent();
    }

    public void selectGeneralPolicyGenderType(String text) {
        selectRadioWithVerify(
                getGeneralPolicyGenderTypeRadioButton(text),
                getGeneralPolicyGenderTypeInput(text),
                "gender type [" + text + "]");
    }

    public void selectGeneralPolicyByType(String type, boolean isEligible) {
        selectRadioWithVerify(
                getGeneralPolicyRadioButtonByType(type, isEligible),
                getGeneralPolicyInputByType(type, isEligible),
                "general policy [" + type + "] to " + isEligible);
    }

    public boolean isRoomListingFormDisplayed() {
        return driverTools.waitForElementToAppear(ROOM_LISTING_FORM).isPresent();
    }

    public void selectRoomType(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(ROOM_TYPE_DROPDOWN_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement option = driverTools.waitForElementToBeClickable(getRoomTypeDropdownOption(text)).orElseThrow();
        driverTools.click(option);
    }

    public void enterRoomName(String text) {
        WebElement input = driverTools.waitForElementToAppear(ROOM_NAME_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, text);
    }

    public void enterRoomMinimumSellingPrice(String text) {
        WebElement input = driverTools.waitForElementToAppear(ROOM_MINIMUM_SELLING_PRICE_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void enterRoomSize(String text) {
        WebElement input = driverTools.waitForElementToAppear(ROOM_SIZE_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void selectRoomSmokingPreference(boolean isSmokingAllowed) {
        WebElement radio = driverTools.waitForElementToAppear(getRoomSmokingPreferenceRadioButton(isSmokingAllowed)).orElseThrow();
        driverTools.click(radio);
    }

    public void enterRoomOccupancy(String adultOccupancy, String childOccupancy) {
        WebElement adult = driverTools.waitForElementToAppear(ROOM_ADULT_OCCUPANCY_INPUT).orElseThrow();
        driverTools.clearInput(adult);
        driverTools.sendKeys(adult, adultOccupancy);
        WebElement child = driverTools.waitForElementToAppear(ROOM_CHILD_OCCUPANCY_INPUT).orElseThrow();
        driverTools.clearInput(child);
        driverTools.sendKeys(child, childOccupancy);
    }

    public void enterRoomMaxOccupancy(String maxOccupancy) {
        WebElement max = driverTools.waitForElementToAppear(ROOM_MAX_OCCUPANCY_INPUT).orElseThrow();
        driverTools.clearInput(max);
        driverTools.sendKeys(max, maxOccupancy);
    }

    public void enterNHAAdultOccupancy(int adultOccupancy) {
        WebElement label = driverTools.waitForElementToAppear(NHA_ROOM_ADULT_OCCUPANCY_LABEL).orElseThrow();
        WebElement plus = driverTools.waitForElementToBeClickable(NHA_ROOM_ADULT_OCCUPANCY_PLUS_BUTTON).orElseThrow();
        int diff = adultOccupancy - Integer.parseInt(driverTools.getValueAttribute(label, "value"));
        int count = 1;
        while (count <= SCROLL_MAX_RETRY && diff > 0) {
            try {
                driverTools.click(plus);
                diff--;
            } catch (Exception e) {
                scrollForm();
                count++;
            }
        }
    }

    public void enterNHANoOfBathroom(int noOfBathroom) {
        WebElement label = driverTools.waitForElementToAppear(NHA_ROOM_NO_OF_BATHROOM_LABEL).orElseThrow();
        WebElement plus = driverTools.waitForElementToBeClickable(NHA_ROOM_NO_OF_BATHROOM_PLUS_BUTTON).orElseThrow();
        WebElement minus = driverTools.waitForElementToAppear(NHA_ROOM_NO_OF_BATHROOM_MINUS_BUTTON).orElseThrow();
        int diff = noOfBathroom - Integer.parseInt(driverTools.getValueAttribute(label, "value"));
        int count = 1;
        if (diff > 0) {
            while (count <= SCROLL_MAX_RETRY && diff > 0) {
                try {
                    driverTools.click(plus);
                    diff--;
                } catch (Exception e) {
                    scrollForm();
                    count++;
                }
            }
        } else if (diff < 0) {
            while (count <= SCROLL_MAX_RETRY && diff < 0) {
                try {
                    driverTools.click(minus);
                    diff++;
                } catch (Exception e) {
                    scrollForm();
                    count++;
                }
            }
        }
    }

    public void selectRoomHasMultipleBedroom(boolean hasMultipleBedroom) {
        WebElement radio = driverTools.waitForElementToAppear(getRoomHasMultipleBedroomRadioButton(hasMultipleBedroom)).orElseThrow();
        driverTools.click(radio);
    }

    public void selectNHARoomHasMultipleBedroom(boolean hasMultipleBedroom) {
        WebElement radio = driverTools.waitForElementToBeClickable(getNHARoomHasMultipleBedroomRadioButton(hasMultipleBedroom)).orElseThrow();
        boolean isClicked = false;
        int count = 1;
        while (!isClicked && count < SCROLL_MAX_RETRY) {
            try {
                driverTools.click(radio);
                isClicked = true;
            } catch (Exception e) {
                scrollForm();
                count++;
            }
        }
    }

    public void addBedroomConfiguration() {
        WebElement button = driverTools.waitForElementToBeClickable(ROOM_ADD_BEDROOM_CONFIGURATION_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void addBedTypeConfigurationByBedroomIndex(int bedroomIndex) {
        WebElement button = driverTools.waitForElementToBeClickable(getRoomAddBedTypeConfigurationByBedroomIndex(bedroomIndex)).orElseThrow();
        driverTools.click(button);
    }

    public void addNHASingleBedroomAddBedConfiguration() {
        WebElement button = driverTools.waitForElementToBeClickable(NHA_ROOM_SINGLE_BEDROOM_ADD_BED_CONFIGURATION_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void enterRoomConfiguration(int bedroomIndex, int bedTypeIndex, String bedType, String amount) {
        ArrayList<Locator> inputs = (ArrayList<Locator>) getRoomConfigurationDropdownInputByIndex(bedroomIndex, bedTypeIndex);
        ArrayList<Locator> options = (ArrayList<Locator>) getRoomConfigurationDropdownOptionByIndex(bedroomIndex, bedTypeIndex, bedType, amount);
        WebElement bedTypeInput = driverTools.waitForElementToBeClickable(inputs.get(0)).orElseThrow();
        driverTools.click(bedTypeInput);
        WebElement bedTypeOption = driverTools.waitForElementToBeClickable(options.get(0)).orElseThrow();
        driverTools.click(bedTypeOption);
        WebElement amountInput = driverTools.waitForElementToBeClickable(inputs.get(1)).orElseThrow();
        driverTools.click(amountInput);
        WebElement amountOption = driverTools.waitForElementToBeClickable(options.get(1)).orElseThrow();
        driverTools.click(amountOption);
    }

    public void enterNHASingleBedroomBedConfiguration(int index, int noOfBed, String bedType) {
        WebElement label = driverTools.waitForElementToAppear(getNHASingleBedroomNoOfBedLabelByIndex(index)).orElseThrow();
        WebElement plus = driverTools.waitForElementToBeClickable(getNHASingleBedroomNoOfBedPlusButtonByIndex(index)).orElseThrow();
        int diff = noOfBed - Integer.parseInt(driverTools.getValueAttribute(label, "value"));
        int count = 1;
        while (count < SCROLL_MAX_RETRY && diff > 0) {
            try {
                driverTools.click(plus);
                diff--;
            } catch (Exception e) {
                scrollForm();
                count++;
            }
        }
        WebElement dropdown = driverTools.waitForElementToBeClickable(getNHASingleBedroomBedTypeDropdownInputByIndex(index)).orElseThrow();
        try {
            driverTools.click(dropdown);
        } catch (Exception e) {
            scrollForm();
            driverTools.click(dropdown);
        }
        WebElement option = driverTools.waitForElementToBeClickable(getNHASingleBedroomBedTypeDropdownOptionByIndex(index, bedType)).orElseThrow();
        driverTools.click(option);
    }

    public boolean isNHARoomNameDisplayedOnListingReviewPage(int index, String roomName) {
        boolean b = driverTools.waitForElementToBeClickable(NHA_ROOM_LISTING_REVIEW_ADD_ROOM_BUTTON).isPresent();
        String label = driverTools.getText(driverTools.waitForElementToAppear(getNHAListingReviewRoomNameLabelByIndex(index)).orElseThrow());
        return b && label.equals(roomName);
    }

    public void selectRoomFacility(int index) {
        WebElement checkbox = driverTools.waitForElementToBeClickable(getRoomFacilityCheckboxByIndex(index)).orElseThrow();
        driverTools.click(checkbox);
    }

    public boolean isRoomListingReviewFormDisplayed() {
        return driverTools.waitForElementToAppear(ROOM_LISTING_REVIEW_FORM).isPresent();
    }

    public boolean isPaymentDetailFormDisplayed() {
        return driverTools.waitForElementToAppear(PAYMENT_DETAIL_FORM).isPresent();
    }

    public void selectPaymentMethod(String text) {
        selectRadioWithVerify(
                getPaymentMethodRadioButton(text),
                getPaymentMethodInput(text),
                "payment method [" + text + "]");
    }

    /**
     * Returns true only when the radio is checked AND stays checked across a brief settle window.
     * Guards against the race where the form re-renders just after a click and silently
     * unsets the selection (which would otherwise let an instant-check return false success).
     */
    private boolean isRadioCheckedStable(Locator inputLocator) {
        if (!driverTools.isRadioButtonChecked(inputLocator)) {
            return false;
        }
        driverTools.dummyWait(1);
        return driverTools.isRadioButtonChecked(inputLocator);
    }

    /**
     * Clicks a radio label and verifies its underlying input is checked and stays checked.
     * Re-clicks (up to {@code maxAttempts} times) if the page re-renders and silently
     * unsets the selection just after click.
     *
     * @param labelLocator    locator of the clickable label/span sibling of the input.
     * @param inputLocator    locator of the underlying {@code <input>} whose {@code checked}
     *                        DOM property reflects true selection state.
     * @param description     human-readable label for the failure message (e.g. "payment method [BANK_TRANSFER]").
     */
    private void selectRadioWithVerify(Locator labelLocator, Locator inputLocator, String description) {
        int maxAttempts = 5;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            if (isRadioCheckedStable(inputLocator)) {
                return;
            }
            WebElement radio = driverTools.waitForElementToBeClickable(labelLocator).orElseThrow();
            driverTools.click(radio);
            driverTools.dummyWait(2);
            if (isRadioCheckedStable(inputLocator)) {
                return;
            }
        }
        throw new IllegalStateException("Failed to select " + description + " after " + maxAttempts + " attempts");
    }

    public void selectBankName(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(PAYMENT_DETAIL_BANK_NAME_DROPDOWN_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement option = driverTools.waitForElementToBeClickable(getPaymentDetailBankNameDropdownOption(text)).orElseThrow();
        driverTools.click(option);
    }

    public void selectNHABankName(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(NHA_PAYMENT_DETAIL_BANK_NAME_DROPDOWN_INPUT).orElseThrow();
        boolean isClicked = false;
        int count = 1;
        while (!isClicked && count <= SCROLL_MAX_RETRY) {
            try {
                driverTools.click(input);
                isClicked = true;
            } catch (Exception e) {
                scrollForm();
                count++;
            }
        }
        driverTools.click(input);
        WebElement option = driverTools.waitForElementToBeClickable(getNHAPaymentDetailBankNameDropdownOption(text)).orElseThrow();
        isClicked = false;
        count = 1;
        while (!isClicked && count <= SCROLL_MAX_RETRY) {
            try {
                driverTools.click(option);
                isClicked = true;
            } catch (Exception e) {
                scrollDropdownOption();
                count++;
            }
        }
    }

    public void enterBankBranchName(String text) {
        WebElement input = driverTools.waitForElementToAppear(PAYMENT_DETAIL_BANK_BRANCH_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void enterBankAccountNumber(String text) {
        WebElement input = driverTools.waitForElementToAppear(PAYMENT_DETAIL_ACCOUNT_NUMBER_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void enterBankAccountHolderName(String text) {
        WebElement input = driverTools.waitForElementToAppear(PAYMENT_DETAIL_ACCOUNT_HOLDER_NAME_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void enterBankSwiftCode(String text) {
        WebElement input = driverTools.waitForElementToAppear(PAYMENT_DETAIL_SWIFT_CODE_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public boolean isRegistrationReviewFormDisplayed() {
        return driverTools.waitForElementToAppear(REGISTRATION_REVIEW_FORM).isPresent();
    }

    public void selectChannelSource(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(REGISTRATION_REVIEW_CHANNEL_SOURCE_DROPDOWN_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement option = driverTools.waitForElementToBeClickable(getRegistrationReviewChannelSourceDropdownOption(text)).orElseThrow();
        driverTools.click(option);
    }

    public void selectNHAChannelSource(String text) {
        WebElement radio = driverTools.waitForElementToBeClickable(getNHARegistrationReviewChannelSourceDropdownOption(text)).orElseThrow();
        boolean isClicked = false;
        int count = 1;
        while (!isClicked && count <= SCROLL_MAX_RETRY) {
            try {
                driverTools.click(radio);
                isClicked = true;
            } catch (Exception e) {
                scrollForm();
                count++;
            }
        }
    }

    public boolean isRegistrationConfirmModalAppeared() {
        return driverTools.waitForElementToAppear(REGISTRATION_CONFIRM_MODAL).isPresent();
    }

    public boolean isNHARegistrationConfirmModalAppeared() {
        return driverTools.waitForElementToAppear(NHA_REGISTRATION_CONFIRM_MODAL).isPresent();
    }

    public void confirmSubmitRegistration() {
        WebElement button = driverTools.waitForElementToBeClickable(REGISTRATION_CONFIRM_SUBMIT_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void confirmNHASubmitRegistration() {
        WebElement button = driverTools.waitForElementToBeClickable(NHA_REGISTRATION_CONFIRM_SUBMIT_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isRegistrationSuccessPageDisplayed() {
        return driverTools.waitForElementToAppear(REGISTRATION_SUCCESS_PAGE).isPresent();
    }

    public void logoutFromRegistrationSuccessPage() {
        WebElement button = driverTools.waitForElementToBeClickable(REGISTRATION_SUCCESS_LOGOUT_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void goToExtranetHomeFromRegistrationSuccessPage() {
        WebElement button = driverTools.waitForElementToBeClickable(REGISTRATION_SUCCESS_TO_EXTRANET_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void refreshPage() {
        driverTools.refreshWebpage();
    }

    public void clickBack() {
        driverTools.back();
    }

    public boolean isNHAFormActive(String text) {
        return driverTools.waitForElementToAppear(getNHAMenuCursorPointer(text)).isPresent();
    }

    public void scrollForm() {
        WebElement main = driverTools.waitForElementToAppear(MAIN_FORM_SECTION).orElseThrow();
        driverTools.scrollInsideElement(main);
    }

    public void scrollDropdownOption() {
        WebElement dropdown = driverTools.waitForElementToAppear(BANK_NAME_DROPDOWN_OPTION_AREA).orElseThrow();
        driverTools.scrollInsideElement(dropdown);
    }

    // ------------------------------------------------------------------
    // Read-back getters: read the value/text currently shown in the UI so
    // tests can do proper "expected vs actual" assertions after every fill.
    // ------------------------------------------------------------------

    /** Returns the current value of an <input>/<textarea> as the user sees it (DOM property, attribute, or text fallback). */
    private String readInputValue(Locator locator) {
        return driverTools.waitForElementToAppear(locator)
                .map(driverTools::getValue)
                .map(String::trim)
                .orElse("");
    }

    /** Returns the visible text of a dropdown-style container (selected option label). */
    private String readDropdownDisplayText(Locator locator) {
        return driverTools.waitForElementToAppear(locator)
                .map(driverTools::getText)
                .map(String::trim)
                .orElse("");
    }

    // ------------------------------------------------------------------
    // Fill-with-verify helpers: re-find the element, clear, type, then read
    // back. Retry up to N times to defeat races where the form re-renders
    // (e.g. async auto-fill from "copy contact") and silently wipes the value
    // immediately after sendKeys.
    // ------------------------------------------------------------------

    private void fillInputWithRetry(Locator locator, String expectedText) {
        int maxAttempts = 3;
        String lastSeen = "";
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            WebElement input = driverTools.waitForElementToAppear(locator).orElseThrow();
            driverTools.clearInput(input);
            driverTools.sendKeys(input, expectedText);
            lastSeen = readInputValue(locator);
            if (lastSeen.equals(expectedText)) {
                return;
            }
        }
        throw new IllegalStateException("Failed to fill " + locator.name() + " with '" + expectedText
                + "' after " + maxAttempts + " attempts. Last seen value: '" + lastSeen + "'");
    }

    private void selectDropdownOptionWithRetry(Locator inputLocator, Locator optionLocator, String expectedText) {
        int maxAttempts = 3;
        String lastSeen = "";
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            lastSeen = readDropdownDisplayText(inputLocator);
            if (lastSeen.contains(expectedText)) {
                return;
            }
            WebElement inputEl = driverTools.waitForElementToBeClickable(inputLocator).orElseThrow();
            driverTools.click(inputEl);
            WebElement optionEl = driverTools.waitForElementToBeClickable(optionLocator).orElseThrow();
            driverTools.click(optionEl);
            lastSeen = readDropdownDisplayText(inputLocator);
            if (lastSeen.contains(expectedText)) {
                return;
            }
        }
        throw new IllegalStateException("Failed to select '" + expectedText + "' in " + inputLocator.name()
                + " after " + maxAttempts + " attempts. Last displayed: '" + lastSeen + "'");
    }

    public String getEnteredPropertyName()                  { return readInputValue(GENERAL_INFO_PROPERTY_NAME_INPUT); }
    public String getEnteredPropertyNameAlias()             { return readInputValue(GENERAL_INFO_PROPERTY_NAME_ALIAS_INPUT); }
    public String getEnteredPropertyAddress()               { return readInputValue(GENERAL_INFO_ADDRESS_INPUT); }
    public String getEnteredPropertyZipCode()               { return readInputValue(GENERAL_INFO_ZIP_CODE); }
    public String getSelectedPropertyCurrency()             { return readDropdownDisplayText(GENERAL_INFO_CURRENCY_DROPDOWN_INPUT); }
    public String getSelectedPropertyType()                 { return readDropdownDisplayText(PROPERTY_TYPE_DROPDOWN_INPUT); }
    public String getSelectedContactDetailRole()            { return readDropdownDisplayText(CONTACT_DETAIL_ROLE_DROPDOWN_INPUT); }
    public String getEnteredContactDetailPropertyPhone()    { return readInputValue(CONTACT_DETAIL_PROPERTY_PHONE_NUMBER_INPUT); }
    public String getEnteredRoomAdultOccupancy()            { return readInputValue(ROOM_ADULT_OCCUPANCY_INPUT); }
    public String getEnteredRoomChildOccupancy()            { return readInputValue(ROOM_CHILD_OCCUPANCY_INPUT); }
    public String getSelectedBankName()                     { return readDropdownDisplayText(PAYMENT_DETAIL_BANK_NAME_DROPDOWN_DISPLAY); }
    public String getEnteredBankBranchName()                { return readInputValue(PAYMENT_DETAIL_BANK_BRANCH_INPUT); }
    public String getEnteredBankAccountNumber()             { return readInputValue(PAYMENT_DETAIL_ACCOUNT_NUMBER_INPUT); }
    public String getEnteredBankAccountHolderName()         { return readInputValue(PAYMENT_DETAIL_ACCOUNT_HOLDER_NAME_INPUT); }
    public String getEnteredBankSwiftCode()                 { return readInputValue(PAYMENT_DETAIL_SWIFT_CODE_INPUT); }
    public String getSelectedChannelSource()                { return readDropdownDisplayText(REGISTRATION_REVIEW_CHANNEL_SOURCE_DROPDOWN_DISPLAY); }

    public void selectProvideItLater() {
        WebElement radio = driverTools.waitForElementToBeClickable(PROVIDE_LATER_RADIO).orElseThrow();
        driverTools.click(radio);
    }
}
