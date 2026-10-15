package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EditPropertyPage <T extends WebDriver> extends BasePage<T> {
    private static final Logger log = LoggerFactory.getLogger(EditPropertyPage.class);

    public EditPropertyPage(Session<T> session) {
        super(session);
    }

    private static final int MAX_RETRY = 10;
    private static final Locator EDIT_PROPERTY_HEADER_LABEL = new Locator("EDIT_PROPERTY_HEADER_LABEL", new By.ByXPath("//span[contains(@class,'Text_size_h3')][contains(.,'Edit Propert')]"));
    private static final Locator GENERAL_INFO_EDIT_BUTTON = new Locator("GENERAL_INFO_EDIT_BUTTON", new By.ByXPath("//a[@data-testid='card-edit-link'][contains(@href,'/edit/general-information/')]"));
    private static final Locator GENERAL_INFO_HOTEL_NAME_LABEL = new Locator("GENERAL_INFO_HOTEL_NAME_LABEL", new By.ByXPath("//span[normalize-space()='Hotel Name' or normalize-space()='Nama Hotel']/following-sibling::div//span[contains(@class,'min-w-0') and contains(@class,'break-words')]"));
    private static final Locator GENERAL_INFO_PUBLIC_ID_LABEL = new Locator("GENERAL_INFO_PUBLIC_ID_LABEL", new By.ByXPath("//span[normalize-space()='Public ID']/following-sibling::div//span[contains(@class,'min-w-0') and contains(@class,'break-words')]"));
    private static final Locator GENERAL_INFO_TIKET_CLEAN_STATUS_LABEL = new Locator("GENERAL_INFO_TIKET_CLEAN_STATUS_LABEL", new By.ByXPath("//span[normalize-space()='tiket CLEAN Status' or normalize-space()='Status tiket CLEAN']/following-sibling::div//span[contains(@class,'min-w-0') and contains(@class,'break-words')]"));
    private static final Locator GENERAL_SUBTEXT_SPAN = new Locator("GENERAL_INFO_TIKET_CLEAN_STATUS_LABEL", new By.ByXPath("//span[contains(text(),'change the property details') or contains(text(),'mengubah detail properti')]"));
    private static final Locator EDIT_GENERAL_INFO_TITLE_LABEL = new Locator("EDIT_GENERAL_INFO_TITLE_LABEL", new By.ByXPath("//h3[text()='Informasi Umum']"));
    private static final Locator EDIT_GENERAL_INFO_PROPERTY_NAME_SOURCE_INPUT = new Locator("EDIT_GENERAL_INFO_PROPERTY_NAME_SOURCE_INPUT", new By.ByXPath("//input[@name='propertyNameSource']"));
    private static final Locator EDIT_GENERAL_INFO_CHAIN_AND_BRAND_SOURCE_INPUT = new Locator("EDIT_GENERAL_INFO_CHAIN_AND_BRAND_SOURCE_INPUT", new By.ByXPath("//input[@name='chainBrandSource']"));
    private static final Locator EDIT_GENERAL_INFO_PROPERTY_TYPE_AND_CATEGORY_SOURCE_INPUT = new Locator("EDIT_GENERAL_INFO_PROPERTY_TYPE_AND_CATEGORY_SOURCE_INPUT", new By.ByXPath("//input[@name='propertyTypeCategorySource']"));
    private static final Locator EDIT_GENERAL_INFO_PROPERTY_STAR_SOURCE_INPUT = new Locator("EDIT_GENERAL_INFO_PROPERTY_STAR_SOURCE_INPUT", new By.ByXPath("//input[@name='propertyStarSource']"));
    private static final Locator EDIT_GENERAL_INFO_PROPERTY_STAR_VALUE_INPUT = new Locator("EDIT_GENERAL_INFO_PROPERTY_STAR_VALUE_INPUT", new By.ByXPath("//input[@name='propertyStar']"));
    private static final Locator HOTEL_STAR_DETAIL = new Locator("HOTEL_STAR_DETAIL", new By.ByXPath("//span[normalize-space()='Hotel Star' or normalize-space()='Bintang Hotel']/following-sibling::div//span[contains(@class,'min-w-0') and contains(@class,'break-words')]"));
    private static final Locator EDIT_GENERAL_INFO_PROPERTY_NAME_INPUT = new Locator("EDIT_GENERAL_INFO_PROPERTY_NAME_INPUT", new By.ByXPath("//div[@id='name']//input[@name='name']"));
    private static final Locator EDIT_GENERAL_INFO_GUEST_TYPE_PREFERENCES_SOURCE_INPUT = new Locator("EDIT_GENERAL_INFO_GUEST_TYPE_PREFERENCES_SOURCE_INPUT", new By.ByXPath("//input[@name='guestTypePreferences']"));
    private static final Locator EDIT_GENERAL_INFO_TIKET_CLEAN_STATUS_TOGGLE = new Locator("EDIT_GENERAL_INFO_TIKET_CLEAN_STATUS_TOGGLE", new By.ByXPath("//*[@name=\"tiketCleanActivation\"]//parent::div[contains(@class,'tix-toggle')]"));
    private static final Locator LOCATION_AND_CONTACT_INFO_EDIT_BUTTON = new Locator("LOCATION_AND_CONTACT_INFO_EDIT_BUTTON", new By.ByXPath("//a[@data-testid='card-edit-link'][contains(@href,'/edit/location-contact/')]"));
    private static final Locator PROPERTY_LOCATION_LONG_LAT_COORDINATE_LABEL = new Locator("PROPERTY_LOCATION_LONG_LAT_COORDINATE_LABEL", new By.ByXPath("//span[normalize-space()='Long Lat Coordinate' or normalize-space()='Koordinat Long Lat']/following-sibling::div//span[contains(@class,'min-w-0') and contains(@class,'break-words')]"));
    private static final Locator PROPERTY_LOCATION_MAIN_AREA_LABEL = new Locator("PROPERTY_LOCATION_MAIN_AREA_LABEL", new By.ByXPath("//span[normalize-space()='Main Area' or normalize-space()='Area Utama']/following-sibling::div//span[contains(@class,'min-w-0') and contains(@class,'break-words')]"));
    private static final Locator PROPERTY_LOCATION_ADDRESS_LABEL = new Locator("PROPERTY_LOCATION_ADDRESS_LABEL", new By.ByXPath("//span[normalize-space()='Address' or normalize-space()='Alamat']/following-sibling::div//span[contains(@class,'min-w-0') and contains(@class,'break-words')]"));
    private static final Locator PROPERTY_LOCATION_ZIP_CODE_LABEL = new Locator("PROPERTY_LOCATION_ZIP_CODE_LABEL", new By.ByXPath("//span[normalize-space()='Zip Code' or normalize-space()='Kode Pos']/following-sibling::div//span[contains(@class,'min-w-0') and contains(@class,'break-words')]"));
    private static final Locator PROPERTY_CONTACT_PHONE_NUMBER_LABEL = new Locator("PROPERTY_CONTACT_PHONE_NUMBER_LABEL", new By.ByXPath("//span[normalize-space()='Phone Number' or normalize-space()='Nomor Telepon']/following-sibling::div//span[contains(@class,'min-w-0') and contains(@class,'break-words')]"));
    private static final Locator PROPERTY_CONTACT_EMAIL_LABEL = new Locator("PROPERTY_CONTACT_EMAIL_LABEL", new By.ByXPath("//span[normalize-space()='Email' or normalize-space()='Email']/following-sibling::div//span[contains(@class,'min-w-0') and contains(@class,'break-words')]"));
    private static final Locator PROPERTY_LOCATION_LONG_LAT_INPUT = new Locator("PROPERTY_LOCATION_LONG_LAT_INPUT", By.xpath("//input[@name='longLatCoordinate']"));
    private static final Locator PROPERTY_LOCATION_MAIN_AREA_INPUT = new Locator("PROPERTY_LOCATION_MAIN_AREA_INPUT", By.xpath("//input[@name='mainAreaId']"));
    private static final Locator PROPERTY_LOCATION_ADDRESS_SOURCE_INPUT = new Locator("PROPERTY_LOCATION_ADDRESS_SOURCE_INPUT", By.xpath("//input[@name='addressSource']"));
    private static final Locator PROPERTY_LOCATION_ADDRESS_INPUT = new Locator("PROPERTY_LOCATION_ADDRESS_INPUT", By.xpath("//input[@name='address-0']"));
    private static final Locator PROPERTY_LOCATION_ZIP_CODE_SOURCE_INPUT = new Locator("PROPERTY_LOCATION_ZIP_CODE_SOURCE_INPUT", By.xpath("//input[@name='postalCodeSource']"));
    private static final Locator PROPERTY_LOCATION_ZIP_CODE_INPUT = new Locator("PROPERTY_LOCATION_ZIP_CODE_INPUT", By.xpath("//input[@name='postalCode']"));
    private static final Locator PROPERTY_CONTACT_INFO_PHONE_NUMBER_SOURCE_INPUT = new Locator("PROPERTY_CONTACT_INFO_PHONE_NUMBER_SOURCE_INPUT", By.xpath("//input[@name='phoneNumberSource']"));
    private static final Locator PROPERTY_CONTACT_INFO_PHONE_NUMBER_INPUT = new Locator("PROPERTY_CONTACT_INFO_PHONE_NUMBER_INPUT", By.xpath("//input[@name='phoneNumber-0']"));
    private static final Locator PROPERTY_CONTACT_INFO_EMAIL_SOURCE_INPUT = new Locator("PROPERTY_CONTACT_INFO_EMAIL_SOURCE_INPUT", By.xpath("//input[@name='emailSource']"));
    private static final Locator PROPERTY_CONTACT_INFO_EMAIL_INPUT = new Locator("PROPERTY_CONTACT_INFO_EMAIL_INPUT", By.xpath("//input[@name='email-0']"));
    private static final Locator PROPERTY_CONTACT_INFO_EMAIL_CLEAR_BUTTON = new Locator("PROPERTY_CONTACT_INFO_EMAIL_CLEAR_BUTTON", new By.ByXPath("(//div[@class='py-16 px-24'][3]//input)[last()]/following-sibling::div[@class='btn-area']"));
    private static final Locator EDIT_LONG_LAT_BUTTON = new Locator("EDIT_LONG_LAT_BUTTON", new By.ByXPath("(//div[@class='btn-area'])[1]"));
    private static final Locator EDIT_LONG_LAT_POPUP_DIALOG = new Locator("EDIT_LONG_LAT_POPUP_DIALOG", new By.ById("modal-edit-geo"));
    private static final Locator EDIT_LONG_LAT_SOURCE_INPUT = new Locator("EDIT_LONG_LAT_SOURCE_INPUT", new By.ByXPath("(//div[@id='modal-edit-geo']//input)[1]"));
    private static final Locator EDIT_LONG_LAT_SELECT_LOCATION_BUTTON = new Locator("EDIT_LONG_LAT_SELECT_LOCATION_BUTTON", new By.ByXPath("//div[@id='modal-edit-geo']//button[contains(@class,'waves-default')]"));
    private static final Locator PROPERTY_DESCRIPTION_AND_POLICY_EDIT_BUTTON = new Locator("PROPERTY_DESCRIPTION_AND_POLICY_EDIT_BUTTON", new By.ByXPath("//a[@data-testid='card-edit-link'][contains(@href,'/edit/description-policy/')]"));
    private static final Locator PROPERTY_DESCRIPTION_ID_LABEL = new Locator("PROPERTY_DESCRIPTION_ID_LABEL", new By.ByXPath("//div[@class='tix-card v4 padding-none desktop hotel-preview-v4__section-card'][3]//div[@class='the-row'][2]/div[@class='the-col-9 font-b1 pb-16 color-N800 pl-8']"));
    private static final Locator PROPERTY_DESCRIPTION_EN_LABEL = new Locator("PROPERTY_DESCRIPTION_EN_LABEL", new By.ByXPath("//div[@class='tix-card v4 padding-none desktop hotel-preview-v4__section-card'][3]//div[@class='the-row'][1]/div[@class='the-col-9 font-b1 pb-16 color-N800 pl-8']"));
    private static final Locator PROPERTY_POLICY_ID_LABEL = new Locator("PROPERTY_POLICY_ID_LABEL", new By.ByXPath("(//div[@class='tix-card v4 padding-none desktop hotel-preview-v4__section-card'][3]//div[contains(@class,'dashed-separator')]//preceding-sibling::div/div[@class='the-col-9 font-b1 pb-16 color-N800 pl-8'])[last()]"));
    private static final Locator PROPERTY_POLICY_EN_LABEL = new Locator("PROPERTY_POLICY_EN_LABEL", new By.ByXPath("(//div[@class='tix-card v4 padding-none desktop hotel-preview-v4__section-card'][3]//div[contains(@class,'dashed-separator')]//preceding-sibling::div/div[@class='the-col-9 font-b1 pb-16 color-N800 pl-8'])[last()-1]"));
    private static final Locator PROPERTY_CHECK_IN_TIME_LABEL = new Locator("PROPERTY_CHECK_IN_TIME_LABEL", new By.ByXPath("(//span[normalize-space()='Check-in'][not(ancestor::div[contains(@class,'opacity-0')])]/following-sibling::div//span[contains(@class,'whitespace-normal')][contains(@class,'break-words')])[1]"));
    private static final Locator PROPERTY_CHECK_OUT_TIME_LABEL = new Locator("PROPERTY_CHECK_OUT_TIME_LABEL", new By.ByXPath("(//span[normalize-space()='Check-out'][not(ancestor::div[contains(@class,'opacity-0')])]/following-sibling::div//span[contains(@class,'whitespace-normal')][contains(@class,'break-words')])[1]"));
    private static final Locator PROPERTY_DESCRIPTION_SOURCE_INPUT = new Locator("PROPERTY_DESCRIPTION_SOURCE_INPUT", By.name("descriptionSource"));
    private static final Locator PROPERTY_DESCRIPTION_ID_TEXTAREA = new Locator("PROPERTY_DESCRIPTION_ID_TEXTAREA", new By.ByXPath("//div[@id='propertyDescription_id']//div[@class='textarea']"));
    private static final Locator PROPERTY_DESCRIPTION_EN_TEXTAREA = new Locator("PROPERTY_DESCRIPTION_EN_TEXTAREA", new By.ByXPath("//div[@id='propertyDescription_en']//div[@class='textarea']"));
    private static final Locator PROPERTY_POLICY_SOURCE_INPUT = new Locator("PROPERTY_POLICY_SOURCE_INPUT", By.xpath("//div[@id='propertySource']//input[@name='policySource']"));
    private static final Locator PROPERTY_POLICY_ID_TEXTAREA = new Locator("PROPERTY_POLICY_ID_TEXTAREA", new By.ByXPath("//div[@id='propertyPolicy_id']//div[@class='textarea']"));
    private static final Locator PROPERTY_POLICY_EN_TEXTAREA = new Locator("PROPERTY_POLICY_EN_TEXTAREA", new By.ByXPath("//div[@id='propertyPolicy_en']//div[@class='textarea']"));
    private static final Locator PROPERTY_CHECK_IN_AND_OUT_SOURCE_INPUT = new Locator("PROPERTY_CHECK_IN_AND_OUT_SOURCE_INPUT", By.xpath("//input[@name='checkInOutSource']"));
    private static final Locator PROPERTY_CHECK_IN_START_TIME_INPUT = new Locator("PROPERTY_CHECK_IN_START_TIME_INPUT", new By.ByXPath("//div[@data-testid='check-in-from']//input"));
    private static final Locator PROPERTY_CHECK_IN_END_TIME_INPUT = new Locator("PROPERTY_CHECK_IN_END_TIME_INPUT", new By.ByXPath("//div[@data-testid='check-in-to']//input"));
    private static final Locator PROPERTY_CHECK_OUT_TIME_INPUT = new Locator("PROPERTY_CHECK_OUT_TIME_INPUT", new By.ByXPath("//div[@data-testid='check-out']//input"));
    private static final Locator PROPERTY_FACILITY_EDIT_BUTTON = new Locator("PROPERTY_FACILITY_EDIT_BUTTON", By.xpath("//div[@data-testid='card-facilities']//a[@data-testid='card-edit-link']"));
    private static final Locator EDIT_PROPERTY_FACILITY_HEADER_LABEL = new Locator("EDIT_PROPERTY_FACILITY_HEADER_LABEL", By.xpath("//div[normalize-space()='Facilities' or normalize-space()='Fasilitas']"));
    private static final Locator PROPERTY_FACILITY_POPULAR_FACILITY_LABEL = new Locator("PROPERTY_FACILITY_POPULAR_FACILITY_LABEL", By.xpath("//ul[@class='grid-4-columns'][1]//span[@class='font-b1']"));
    private static final Locator PROPERTY_FACILITY_ALL_SELECTED_FACILITY_LABEL = new Locator("PROPERTY_FACILITY_ALL_SELECTED_FACILITY_LABEL", By.xpath("//span[@class='font-b1']"));
    private static final Locator PROPERTY_FACILITY_SOURCE_INPUT = new Locator("PROPERTY_FACILITY_SOURCE_INPUT", By.xpath("//input[@name='facilitySource']"));
    private static final Locator PROPERTY_FACILITY_TOTAL_SELECTED_INPUT = new Locator("PROPERTY_FACILITY_TOTAL_SELECTED_INPUT", By.xpath("//div[@class='dropdown-multi-v4']//input[not(contains(@type,'checkbox'))]"));
    private static final Locator PROPERTY_STATUS_EDIT_BUTTON = new Locator("PROPERTY_STATUS_EDIT_BUTTON", By.xpath("//a[@data-testid='card-edit-link'][contains(@href,'/edit/property-status/')]"));
    private static final Locator PROPERTY_STATUS_DETAIL_LABEL = new Locator("PROPERTY_STATUS_DETAIL_LABEL", new By.ByXPath("(//div[.//span[normalize-space()='Property Status' or normalize-space()='Status Properti']]//span[contains(@class,'rounded-[4px]') and (normalize-space()='ON' or normalize-space()='OFF')])[1]"));
    private static final Locator PROPERTY_STATUS_PROPERTY_LEVEL_TOGGLE = new Locator("PROPERTY_STATUS_PROPERTY_LEVEL_TOGGLE", By.xpath("//input[@aria-label='property-status-toggle']"));
    private static final Locator PROPERTY_STATUS_PROPERTY_LEVEL_TOGGLE_CLICKABLE = new Locator("PROPERTY_STATUS_PROPERTY_LEVEL_TOGGLE_CLICKABLE", By.xpath("//input[@aria-label='property-status-toggle']/parent::label"));
    private static final Locator PROPERTY_STATUS_PROPERTY_TOGGLE_OFF_REASON_INPUT = new Locator("PROPERTY_STATUS_PROPERTY_TOGGLE_OFF_REASON_INPUT", new By.ByName("reason"));
    private static final Locator PROPERTY_ANNOUNCEMENT_EDIT_BUTTON = new Locator("PROPERTY_ANNOUNCEMENT_EDIT_BUTTON", By.xpath("//a[@data-testid='card-edit-link'][contains(@href,'/edit/announcement/')]"));
    private static final Locator PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_EN_LABEL = new Locator("PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_EN_LABEL", new By.ByXPath("//div[@data-testid='card-announcement']//div[contains(@class,'flex')][span[normalize-space()='English' or normalize-space()='Bahasa Inggris']][preceding-sibling::div[contains(@class,'Text_weight_bold')][1][contains(.,'Property Detail Page') or contains(.,'Halaman Property Detail')]]//span[contains(@class,'whitespace-normal')][contains(@class,'break-words')]"));
    private static final Locator PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_ID_LABEL = new Locator("PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_ID_LABEL", new By.ByXPath("//div[@data-testid='card-announcement']//div[contains(@class,'flex')][span[normalize-space()='Indonesian' or normalize-space()='Bahasa Indonesia']][preceding-sibling::div[contains(@class,'Text_weight_bold')][1][contains(.,'Property Detail Page') or contains(.,'Halaman Property Detail')]]//span[contains(@class,'whitespace-normal')][contains(@class,'break-words')]"));
    private static final Locator PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_EN_LABEL = new Locator("PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_EN_LABEL", new By.ByXPath("//div[@data-testid='card-announcement']//div[contains(@class,'flex')][span[normalize-space()='English' or normalize-space()='Bahasa Inggris']][preceding-sibling::div[contains(@class,'Text_weight_bold')][1][contains(.,'Room List Section')]]//span[contains(@class,'whitespace-normal')][contains(@class,'break-words')]"));
    private static final Locator PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_ID_LABEL = new Locator("PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_ID_LABEL", new By.ByXPath("//div[@data-testid='card-announcement']//div[contains(@class,'flex')][span[normalize-space()='Indonesian' or normalize-space()='Bahasa Indonesia']][preceding-sibling::div[contains(@class,'Text_weight_bold')][1][contains(.,'Room List Section')]]//span[contains(@class,'whitespace-normal')][contains(@class,'break-words')]"));
    private static final Locator PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_SOURCE_INPUT = new Locator("PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_SOURCE_INPUT", new By.ByXPath("//input[@name='hotelSource']"));
    private static final Locator PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_ID_TEXTAREA = new Locator("PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_ID_TEXTAREA", new By.ByXPath("//div[@data-testid='hotel-id']//div[contains(@class,'tiptap')]"));
    private static final Locator PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_EN_TEXTAREA = new Locator("PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_EN_TEXTAREA", new By.ByXPath("//div[@data-testid='hotel-en']//div[contains(@class,'tiptap')]"));
    private static final Locator PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_SOURCE_INPUT = new Locator("PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_SOURCE_INPUT", new By.ByXPath("//input[@name='roomSource']"));
    private static final Locator PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_ID_TEXTAREA = new Locator("PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_ID_TEXTAREA", new By.ByXPath("//div[@data-testid='room-id']//div[contains(@class,'tiptap')]"));
    private static final Locator PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_EN_TEXTAREA = new Locator("PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_EN_TEXTAREA", new By.ByXPath("//div[@data-testid='room-en']//div[contains(@class,'tiptap')]"));
    private static final Locator EDIT_PROPERTY_SUBMIT_BUTTON = new Locator("EDIT_PROPERTY_SUBMIT_BUTTON", new By.ByXPath("//button[@type='submit'][contains(@class,'waves-default')]"));
    private static final Locator EDIT_GENERAL_INFO_SAVE_BUTTON = new Locator("EDIT_GENERAL_INFO_SAVE_BUTTON", new By.ByXPath("//button[@data-testid='edit-save']"));
    private static final Locator EDIT_GENERAL_INFO_CANCEL_BUTTON = new Locator("EDIT_GENERAL_INFO_CANCEL_BUTTON", new By.ByXPath("//button[@data-testid='edit-cancel']"));
    private static final Locator EDIT_GENERAL_INFO_SAVE_CONFIRMATION_BUTTON = new Locator("EDIT_GENERAL_INFO_SAVE_CONFIRMATION_BUTTON", new By.ByXPath("//button[@data-testid='save-confirmation-confirm']"));
    private static final Locator EDIT_LOCATION_AND_CONTACT_SAVE_BUTTON = new Locator("EDIT_LOCATION_AND_CONTACT_SAVE_BUTTON", new By.ByXPath("//div[contains(@class,'justify-end')]//button[contains(@class,'Button_variant_primary')][normalize-space()='Save' or normalize-space()='Simpan']"));
    private static final Locator EDIT_LOCATION_AND_CONTACT_CANCEL_BUTTON = new Locator("EDIT_LOCATION_AND_CONTACT_CANCEL_BUTTON", new By.ByXPath("(//div[contains(@class,'justify-end')]//button[contains(@class,'Button_variant_secondary')])[last()]"));
//    private static final Locator CONFIRM_POPUP_DIALOG = new Locator("CONFIRM_POPUP_DIALOG", new By.ByXPath("//*[@id='save-prop-prompt-dialog' or @id='modal-expiry-prompt']"));
    private static final Locator CONFIRM_POPUP_DIALOG = new Locator("CONFIRM_POPUP_DIALOG", new By.ByXPath("//div[@id='modal-expiry-prompt']//div[@class='modal-popup'] | //div[@id='save-prop-prompt-dialog']//div[@class='dialog-content-body']"));
    private static final Locator CONFIRM_POPUP_SUBMIT_BUTTON = new Locator("CONFIRM_POPUP_SUBMIT_BUTTON", new By.ByXPath("(//div[contains(@id,'prompt')]//button[contains(@class,'waves-default')])[1]"));
    private static final Locator CONFIRM_POPUP_SUBMIT_LOADING = new Locator("CONFIRM_POPUP_SUBMIT_LOADING", new By.ByXPath("//div[contains(@id,'prompt')]//button[contains(@class,'waves-default')]//div[@class=\"tix-loading v4\"]"));
    private static final Locator SUBMIT_SUCCESSFUL_SNACKBAR = new Locator("SUBMIT_SUCCESSFUL_SNACKBAR", By.xpath("//div[@class='tix-snackbar v4 type-default out-slide visible desktop']"));

    private static String getSourceVendorMatchCondition(String vendorName) {
        if (vendorName.equalsIgnoreCase("Restore to Default System")) {
            return "@data-value='RESTORE_TO_DEFAULT_SYSTEM'";
        } else if (vendorName.equalsIgnoreCase("User Input")) {
            return "@data-value='USER_INPUT'";
        } else {
            return String.format("contains(@data-label,'%s')", vendorName);
        }
    }

    private static Locator getPropertyNameSourceVendorOption(String vendorName) {
        String matchCondition = getSourceVendorMatchCondition(vendorName);
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//li[@data-name='propertyNameSource'][%s][not(ancestor::div[contains(@class,'opacity-0')])]", matchCondition)));
    }

    public String getUrlForEditCurrentProperty() {
        return driverTools.getCurrentURL();
    }

    private static Locator getChainAndBrandSourceVendorOption(String vendorName) {
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//li[contains(@data-label,'%s')][not(ancestor::div[contains(@class,'opacity-0')])]", vendorName)));
    }

    private static Locator getPropertyTypeAndCategorySourceVendorOption(String vendorName) {
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//li[contains(@data-label,'%s')][not(ancestor::div[contains(@class,'opacity-0')])]", vendorName)));
    }

    private static Locator getPropertyStarSourceVendorOption(String vendorName) {
        String matchCondition = getSourceVendorMatchCondition(vendorName);
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//li[@data-name='propertyStarSource'][%s][not(ancestor::div[contains(@class,'opacity-0')])]", matchCondition)));
    }

    private static Locator getPropertyStarValueOption(String starValue) {
        return new Locator("PROPERTY_STAR_VALUE_OPTION", new By.ByXPath(String.format("//li[@data-name='propertyStar'][@data-value='%s'][not(ancestor::div[contains(@class,'opacity-0')])]", getPropertyStarDataValue(starValue))));
    }

    private static String getPropertyStarDataValue(String starValue) {
        if (starValue.equalsIgnoreCase("Unrated")) {
            return "0";
        }
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\d+").matcher(starValue);
        if (matcher.find()) {
            return matcher.group();
        }
        return starValue.trim();
    }

    private static Locator getGuestTypePreferencesSourceVendorOption(String vendorName) {
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//div[@class='hotel-edit-v4--section'][6]//div[@class='the-popoverlist']//label[contains(text(),'%s')]", vendorName)));
    }

    private static Locator getEditLongLatSourceVendorOption(String vendorName) {
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//div[@id='modal-edit-geo']//div[@class='the-popoverlist']//label[contains(text(),'%s')]", vendorName)));
    }

    private static Locator getMainAreaSourceOption(String mainArea) {
        return new Locator("MAIN_AREA_OPTION", new By.ByXPath(String.format("//li[@data-name='mainAreaId'][contains(@data-label,'%s')][not(ancestor::div[contains(@class,'opacity-0')])]", mainArea)));
    }

    private static Locator getPropertyAddressSourceVendorOption(String vendorName) {
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//li[@data-name='addressSource'][contains(@data-label,'%s')][not(ancestor::div[contains(@class,'opacity-0')])]", vendorName)));
    }

    private static Locator getPropertyZipCodeSourceVendorOption(String vendorName) {
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//li[@data-name='postalCodeSource'][contains(@data-label,'%s')][not(ancestor::div[contains(@class,'opacity-0')])]", vendorName)));
    }

    private static Locator getPropertyPhoneNumberSourceVendorOption(String vendorName) {
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//li[@data-name='phoneNumberSource'][contains(@data-label,'%s')][not(ancestor::div[contains(@class,'opacity-0')])]", vendorName)));
    }

    private static Locator getPropertyEmailSourceVendorOption(String vendorName) {
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//li[@data-name='emailSource'][contains(@data-label,'%s')][not(ancestor::div[contains(@class,'opacity-0')])]", vendorName)));
    }

    private static Locator getPropertyDescriptionSourceVendorOption(String vendorName) {
        return new Locator("PROPERTY_DESCRIPTION_VENDOR_NAME_OPTION", new By.ByXPath(String.format("//div[@id='descriptionSource']//div[@class='the-popoverlist']//label[contains(text(),'%s')]", vendorName)));
    }

    private static Locator getPropertyPolicySourceVendorOption(String vendorName) {
        return new Locator("PROPERTY_POLICY_VENDOR_NAME_OPTION", new By.ByXPath(String.format("//div[@id='propertySource']//div[@class='the-popoverlist']//label[contains(text(),'%s')]", vendorName)));
    }

    private static Locator getPropertyCheckInAndOutSourceVendorOption(String vendorName) {
        String matchCondition = getSourceVendorMatchCondition(vendorName);
        return new Locator("PROPERTY_CHECK_IN_AND_OUT_VENDOR_NAME_OPTION", new By.ByXPath(String.format("//li[@data-name='checkInOutSource'][%s][not(ancestor::div[contains(@class,'opacity-0')])]", matchCondition)));
    }

    private static Locator getPropertyFacilitySourceVendorOption(String vendorName) {
        String matchCondition = getSourceVendorMatchCondition(vendorName);
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//li[@data-name='facilitySource'][%s][not(ancestor::div[contains(@class,'opacity-0')])]", matchCondition)));
    }

    private static Locator getPropertyFacilityLabelInDetailView(String facilityName) {
        String alias = FACILITY_NAME_LOCALE_ALIASES.get(facilityName);
        String matchCondition = alias != null
                ? String.format("normalize-space()='%s' or normalize-space()='%s'", facilityName, alias)
                : String.format("normalize-space()='%s'", facilityName);
        return new Locator("PROPERTY_FACILITY_DETAIL_LABEL", new By.ByXPath(String.format("//div[@data-testid='card-facilities']//*[not(*)][%s]", matchCondition)));
    }

    private static final Map<String, String> FACILITY_NAME_LOCALE_ALIASES = Map.of(
            "NIB Registered", "NIB Terdaftar"
    );

    private static Locator getPropertyFacilityCheckbox(String facilityName) {
        String alias = FACILITY_NAME_LOCALE_ALIASES.get(facilityName);
        String matchCondition = alias != null
                ? String.format("contains(normalize-space(.),'%s') or contains(normalize-space(.),'%s')", facilityName, alias)
                : String.format("contains(normalize-space(.),'%s')", facilityName);
        return new Locator("PROPERTY_FACILITY_CHECKBOX", new By.ByXPath(String.format("//div[@data-testid='facility-group']//label[%s]//input[@type='checkbox']", matchCondition)));
    }

    private static Locator getPropertyStatusVendorLevelToggle(String vendorName) {
        return new Locator("VENDOR_STATUS_TOGGLE", new By.ByXPath(String.format("//div[contains(@class,'flex') and contains(@class,'items-center') and contains(@class,'gap-2')][.//span[starts-with(normalize-space(.),'%s:')]]//input[@type='checkbox']", vendorName)));
    }

    private static Locator getPropertyStatusPropertyToggleOffReasonOption(String reason) {
        String matchCondition;
        if (reason.equalsIgnoreCase("Temporarily closed") || reason.equalsIgnoreCase("Tutup sementara")) {
            matchCondition = "@data-value='TEMPORARILY_CLOSED'";
        } else if (reason.equalsIgnoreCase("Permanently closed") || reason.equalsIgnoreCase("Tutup permanen")) {
            matchCondition = "@data-value='PERMANENTLY_CLOSED'";
        } else if (reason.equalsIgnoreCase("Change management") || reason.equalsIgnoreCase("Change of management") || reason.equalsIgnoreCase("Perubahan manajemen")) {
            matchCondition = "@data-value='CHANGE_MANAGEMENT'";
        } else {
            matchCondition = String.format("contains(@data-label,'%s')", reason);
        }
        return new Locator("PROPERTY_STATUS_TOGGLE_OFF_REASON_OPTION", new By.ByXPath(String.format("//li[@data-name='reason'][%s]", matchCondition)));
    }

    private static Locator getPropertyAnnouncementHotelLevelSourceVendorOption(String vendorName) {
        String matchCondition = getSourceVendorMatchCondition(vendorName);
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//li[@data-name='hotelSource'][%s][not(ancestor::div[contains(@class,'opacity-0')])]", matchCondition)));
    }

    private static Locator getPropertyAnnouncementRoomLevelSourceVendorOption(String vendorName) {
        String matchCondition = getSourceVendorMatchCondition(vendorName);
        return new Locator("VENDOR_NAME_OPTION", new By.ByXPath(String.format("//li[@data-name='roomSource'][%s][not(ancestor::div[contains(@class,'opacity-0')])]", matchCondition)));
    }

    private String getNativeVendorNameLowerCase(String vendorName) {
        if (vendorName.equals("TIKET")) {
            vendorName = vendorName.toLowerCase();
        }
        return vendorName;
    }

    private String getNativeVendorNameByCase(String vendorName, String caseType) {
        if (vendorName.equals("TIKET")) {
            switch (caseType) {
                case "UPPER" -> vendorName = vendorName.toUpperCase();
                case "LOWER" -> vendorName = vendorName.toLowerCase();
                case "NORMAL" -> vendorName = StringUtils.capitalize(vendorName.toLowerCase());
                default -> throw new NotImplementedException("Unimplemented case type!");
            }
        }
        return vendorName;
    }

    private String getNativeVendorNameCapitalized(String vendorName) {
        if (vendorName.equals("TIKET")) {
            vendorName = StringUtils.capitalize(vendorName.toLowerCase());
        }
        return vendorName;
    }

    public boolean isEditPropertyPageLoaded(String url) {
        driverTools.waitForElementsToAppear(EDIT_PROPERTY_HEADER_LABEL);
        boolean editPropertyHeaderLabel = driverTools.isElementDisplayed(EDIT_PROPERTY_HEADER_LABEL);
        if (!editPropertyHeaderLabel) {
            try {
                driverTools.get(url);
            } catch (Exception timeOutException) {
                logger.log("Got timeout, but let's check if we successfully opened the MM Module");
            }
        }
        driverTools.waitForElementsToAppear(EDIT_PROPERTY_HEADER_LABEL, 20);
        boolean generalInfoHotelNameLabel = driverTools.isElementDisplayed(GENERAL_INFO_HOTEL_NAME_LABEL);
        boolean generalInfoPublicIdLabel = driverTools.isElementDisplayed(GENERAL_INFO_PUBLIC_ID_LABEL);
        boolean generalSubtextSpan = driverTools.isElementDisplayed(GENERAL_SUBTEXT_SPAN);

        return editPropertyHeaderLabel || generalInfoHotelNameLabel || generalInfoPublicIdLabel || generalSubtextSpan;
    }


    public boolean isEditPropertyPageLoaded() {
        driverTools.waitForElementsToAppear(EDIT_PROPERTY_HEADER_LABEL, 20);
        boolean editPropertyHeaderLabel = driverTools.isElementDisplayed(EDIT_PROPERTY_HEADER_LABEL);
        if (!editPropertyHeaderLabel) {
            driverTools.waitForElementsToAppear(GENERAL_INFO_HOTEL_NAME_LABEL, 20);
        }
        boolean generalInfoHotelNameLabel = driverTools.isElementDisplayed(GENERAL_INFO_HOTEL_NAME_LABEL);
        boolean generalInfoPublicIdLabel = driverTools.isElementDisplayed(GENERAL_INFO_PUBLIC_ID_LABEL);
        boolean generalSubtextSpan = driverTools.isElementDisplayed(GENERAL_SUBTEXT_SPAN);

        return editPropertyHeaderLabel || generalInfoHotelNameLabel || generalInfoPublicIdLabel || generalSubtextSpan;
    }

    public boolean isHotelNameDisplayed(String hotelName) {
        WebElement generalInfoHotelNameLabel = driverTools.waitForElementToPresence(GENERAL_INFO_HOTEL_NAME_LABEL).orElseThrow();
        return driverTools.getText(generalInfoHotelNameLabel).contains(hotelName);
    }

    public boolean isHotelPublicIdDisplayed(String publicId) {
        WebElement generalInfoPublicIdLabel = driverTools.waitForElementToPresence(GENERAL_INFO_PUBLIC_ID_LABEL).orElseThrow();
        return driverTools.getText(generalInfoPublicIdLabel).contains(publicId);
    }

    public boolean isHotelTiketCleanStatusDisplayed(boolean expectedStatus) {
        WebElement label = driverTools.waitForElementToAppear(GENERAL_INFO_TIKET_CLEAN_STATUS_LABEL).orElseThrow();
        if (expectedStatus) {
            return driverTools.getText(label).equals("ON");
        } else {
            return driverTools.getText(label).equals("OFF");
        }
    }

    public void clickEditGeneralInfo() {
        WebElement generalInfoEditButton = driverTools.waitForElementToPresence(GENERAL_INFO_EDIT_BUTTON).orElseThrow();
        driverTools.click(generalInfoEditButton);
    }

    public boolean isGeneralInfoPropertyNameDisplayed(String hotelName) {
        WebElement editGeneralInfoPropertyNameInput = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_PROPERTY_NAME_INPUT).orElseThrow();
        return driverTools.getValueAttribute(editGeneralInfoPropertyNameInput, "value").contains(hotelName);
    }

    public void selectGeneralInfoPropertyNameSourceVendor(String vendorName) {
        WebElement editGeneralInfoPropertyNameSourceInput = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_PROPERTY_NAME_SOURCE_INPUT).orElseThrow();
        driverTools.click(editGeneralInfoPropertyNameSourceInput);
        WebElement propertyNameSourceVendorOption = driverTools.waitForElementToPresence(getPropertyNameSourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL"))).orElseThrow();
        driverTools.clickWithJavascriptExecutor(propertyNameSourceVendorOption);
    }

    public void selectGeneralInfoChainAndBrandSourceVendor(String vendorName) {
        WebElement editGeneralInfoChainAndBrandSourceInput = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_CHAIN_AND_BRAND_SOURCE_INPUT).orElseThrow();
        driverTools.click(editGeneralInfoChainAndBrandSourceInput);
        driverTools.click(getChainAndBrandSourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL")));
    }

    public boolean isGeneralInfoChainAndBrandVendorNameDisplayed(String vendorName) {
        driverTools.waitForElementsToAppear(EDIT_GENERAL_INFO_CHAIN_AND_BRAND_SOURCE_INPUT);
        WebElement editGeneralInfoChainAndBrandSourceInput = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_CHAIN_AND_BRAND_SOURCE_INPUT).orElseThrow();
        return driverTools.getValueAttribute(editGeneralInfoChainAndBrandSourceInput, "value").contains(getNativeVendorNameByCase(vendorName, "NORMAL"));
    }

    public void selectGeneralInfoPropertyTypeAndCategorySourceVendor(String vendorName) {
        WebElement editGeneralInfoPropertyTypeAndCategorySourceInput = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_PROPERTY_TYPE_AND_CATEGORY_SOURCE_INPUT).orElseThrow();
        driverTools.click(editGeneralInfoPropertyTypeAndCategorySourceInput);
        driverTools.click(getPropertyTypeAndCategorySourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL")));
    }

    public boolean isGeneralInfoPropertyTypeAndCategoryVendorNameDisplayed(String vendorName) {
        WebElement editGeneralInfoPropertyTypeAndCategorySourceInput = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_PROPERTY_TYPE_AND_CATEGORY_SOURCE_INPUT).orElseThrow();
        return driverTools.getValueAttribute(editGeneralInfoPropertyTypeAndCategorySourceInput, "value").contains(getNativeVendorNameByCase(vendorName, "NORMAL"));
    }

    public void selectGeneralInfoPropertyStarSourceVendor(String vendorName) {
        WebElement editGeneralInfoPropertyStarSourceInput = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_PROPERTY_STAR_SOURCE_INPUT).orElseThrow();
        Locator propertyStarSourceVendorOptionLocator = getPropertyStarSourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL"));
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editGeneralInfoPropertyStarSourceInput);
        // Reuse the element found here instead of issuing a second, independent wait:
        // the dropdown can close itself moments after opening, so a fresh re-poll can
        // miss an option that was already found present.
        Optional<WebElement> propertyStarSourceVendorOption = driverTools.waitForElementToPresence(propertyStarSourceVendorOptionLocator, 5);
        if (propertyStarSourceVendorOption.isEmpty()) {
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(editGeneralInfoPropertyStarSourceInput);
            propertyStarSourceVendorOption = driverTools.waitForElementToPresence(propertyStarSourceVendorOptionLocator, 5);
        }
        driverTools.clickWithJavascriptExecutor(propertyStarSourceVendorOption.orElseThrow());
    }

    public boolean isGeneralInfoPropertyStarVendorNameDisplayed(String vendorName) {
        WebElement editGeneralInfoPropertyStarSourceInput = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_PROPERTY_STAR_SOURCE_INPUT).orElseThrow();
        return driverTools.getValueAttribute(editGeneralInfoPropertyStarSourceInput, "value").contains(getNativeVendorNameByCase(vendorName, "NORMAL"));
    }

    public String getCurrentPropertyStarFromDetailView() {
        WebElement starLabel = driverTools.waitForElementToPresence(HOTEL_STAR_DETAIL).orElseThrow();
        return driverTools.getText(starLabel).trim().split("\n")[0].trim();
    }

    public String getCurrentMainAreaFromDetailView() {
        WebElement mainAreaLabel = driverTools.waitForElementToPresence(PROPERTY_LOCATION_MAIN_AREA_LABEL).orElseThrow();
        return driverTools.getText(mainAreaLabel).trim().split("\n")[0].trim();
    }

    public void selectGeneralInfoPropertyStarValue(String starValue) {
        driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_PROPERTY_STAR_VALUE_INPUT).orElseThrow();
        driverTools.clickWithActions(EDIT_GENERAL_INFO_PROPERTY_STAR_VALUE_INPUT);
        driverTools.waitForElementsToAppear(new Locator("ANY_PROPERTY_STAR_VALUE_OPTION", new By.ByXPath("//li[@data-name='propertyStar'][not(ancestor::div[contains(@class,'opacity-0')])]"))).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getPropertyStarValueOption(starValue));
    }

    public boolean isGeneralInfoPropertyStarValueDisplayed(String starValue) {
        WebElement starDetail = driverTools.waitForElementToPresence(HOTEL_STAR_DETAIL).orElseThrow();
        return driverTools.getText(starDetail).trim().toLowerCase().contains(starValue.toLowerCase());
    }

    public void selectGeneralInfoGuestTypePreferencesSourceVendor(String vendorName) {
        WebElement editGeneralInfoGuestTypePreferencesSourceInput = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_GUEST_TYPE_PREFERENCES_SOURCE_INPUT).orElseThrow();
        driverTools.click(editGeneralInfoGuestTypePreferencesSourceInput);
        driverTools.click(getGuestTypePreferencesSourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL")));
    }

    public boolean isGeneralInfoGuestTypePreferencesVendorNameDisplayed(String vendorName) {
        WebElement editGeneralInfoGuestTypePreferencesSourceInput = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_GUEST_TYPE_PREFERENCES_SOURCE_INPUT).orElseThrow();
        return driverTools.getValueAttribute(editGeneralInfoGuestTypePreferencesSourceInput, "value").contains(getNativeVendorNameByCase(vendorName, "NORMAL"));
    }

    public boolean isGeneralInfoTiketCleanStatusToggleDisplayed(boolean expectedStatus) {
        WebElement toggle = driverTools.waitForElementToAppear(EDIT_GENERAL_INFO_TIKET_CLEAN_STATUS_TOGGLE).orElseThrow();
        if (expectedStatus) {
            return driverTools.getValueAttribute(toggle, "class").contains("checked");
        } else {
            return !driverTools.getValueAttribute(toggle, "class").contains("checked");
        }
    }

    public void switchTiketCleanStatusToggle(boolean expectedStatus) {
        WebElement toggle = driverTools.waitForElementToAppear(EDIT_GENERAL_INFO_TIKET_CLEAN_STATUS_TOGGLE).orElseThrow();
        boolean isEnabled = driverTools.getValueAttribute(toggle, "class").contains("checked");
        if (expectedStatus != isEnabled) {
            driverTools.click(toggle);
        }
    }

    public boolean isPropertyLocationLongLatCoordinateLabelDisplayed() {
        WebElement propertyLocationLongLatCoordinateLabel = driverTools.waitForElementToPresence(PROPERTY_LOCATION_LONG_LAT_COORDINATE_LABEL).orElseThrow();
        return !driverTools.getValueAttribute(propertyLocationLongLatCoordinateLabel, "value").isBlank();
    }

    public boolean isPropertyLocationLongLatCoordinateLabelUpdated(String expectedLongLat) {
        WebElement propertyLocationLongLatCoordinateLabel = driverTools.waitForElementToPresence(PROPERTY_LOCATION_LONG_LAT_COORDINATE_LABEL).orElseThrow();
        return driverTools.getText(propertyLocationLongLatCoordinateLabel).contains(expectedLongLat);
    }

    public void clickEditLocationAndContactInfo() {
        WebElement locationAndContactInfoEditButton = driverTools.waitForElementToBeClickable(LOCATION_AND_CONTACT_INFO_EDIT_BUTTON).orElseThrow();
        driverTools.click(locationAndContactInfoEditButton);
    }

    public boolean isPropertyLocationEditLongLatDisplayed() {
        WebElement propertyLocationLongLatInput = driverTools.waitForElementToPresence(PROPERTY_LOCATION_LONG_LAT_INPUT).orElseThrow();
        return !driverTools.getValueAttribute(propertyLocationLongLatInput, "value").isBlank();
    }

    public void clickEditLocationLongLat() {
        WebElement editLongLatButton = driverTools.waitForElementToBeClickable(EDIT_LONG_LAT_BUTTON).orElseThrow();
        driverTools.click(editLongLatButton);
    }

    public boolean isEditLongLatPopUpDialogAppeared() {
        WebElement editLongLatPopUpDialog = driverTools.waitForElementToPresence(EDIT_LONG_LAT_POPUP_DIALOG).orElseThrow();
        return driverTools.isElementDisplayed(editLongLatPopUpDialog);
    }

    public void selectEditLongLatSourceVendor(String vendorName) {
        WebElement editLongLatSourceInput = driverTools.waitForElementToBeClickable(EDIT_LONG_LAT_SOURCE_INPUT).orElseThrow();
        try {
            driverTools.click(editLongLatSourceInput);
            driverTools.waitForElementToBeClickable(getEditLongLatSourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL"))).orElseThrow();
        } catch (Exception e) {
            driverTools.click(editLongLatSourceInput);
            driverTools.waitForElementToBeClickable(getEditLongLatSourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL"))).orElseThrow();
        }
        WebElement option = driverTools.waitForElementToBeClickable(getEditLongLatSourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL"))).orElseThrow();
        driverTools.click(option);
    }

    public void clickEditLongLatSelectLocation() {
        WebElement editLongLatSelectLocationButton = driverTools.waitForElementToBeClickable(EDIT_LONG_LAT_SELECT_LOCATION_BUTTON).orElseThrow();
        driverTools.click(editLongLatSelectLocationButton);
    }

    public String getSelectedLongLatCoordinateValue() {
        WebElement propertyLocationLongLatInput = driverTools.waitForElementToPresence(PROPERTY_LOCATION_LONG_LAT_INPUT).orElseThrow();
        return driverTools.getValueAttribute(propertyLocationLongLatInput, "value");
    }

    public void selectPropertyLocationMainArea(String mainArea) {
        driverTools.waitForElementToPresence(PROPERTY_LOCATION_MAIN_AREA_INPUT).orElseThrow();
        Locator mainAreaOptionLocator = getMainAreaSourceOption(mainArea);
        driverTools.clickWithSyntheticEvents(PROPERTY_LOCATION_MAIN_AREA_INPUT);
        // Look for the specific option directly and reuse it immediately: the dropdown
        // panel can close itself moments after opening, so checking for "any" option first
        // and then re-querying for this specific one leaves a race where it can vanish
        // in between.
        Optional<WebElement> mainAreaOption = driverTools.waitForElementToPresence(mainAreaOptionLocator, 5);
        if (mainAreaOption.isEmpty()) {
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(PROPERTY_LOCATION_MAIN_AREA_INPUT);
            mainAreaOption = driverTools.waitForElementToPresence(mainAreaOptionLocator, 5);
        }
        driverTools.clickWithJavascriptExecutor(mainAreaOption.orElseThrow());
    }

    public boolean isPropertyLocationMainAreaDisplayed(String mainArea) {
        WebElement propertyLocationMainAreaLabel = driverTools.waitForElementToAppear(PROPERTY_LOCATION_MAIN_AREA_LABEL).orElseThrow();
        return driverTools.getText(propertyLocationMainAreaLabel).contains(mainArea);
    }

    public void selectPropertyLocationAddressSourceVendor(String vendorName) {
        driverTools.waitForElementToPresence(PROPERTY_LOCATION_ADDRESS_SOURCE_INPUT).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(PROPERTY_LOCATION_ADDRESS_SOURCE_INPUT);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getPropertyAddressSourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL")));
    }

    public String getPropertyLocationSelectedAddress() {
        WebElement propertyLocationAddressInput = driverTools.waitForElementToPresence(PROPERTY_LOCATION_ADDRESS_INPUT).orElseThrow();
        return driverTools.getValueAttribute(propertyLocationAddressInput, "value");
    }
    public boolean isPropertyLocationAddressDisplayed(String address) {
        WebElement propertyLocationAddressLabel = driverTools.waitForElementToAppear(PROPERTY_LOCATION_ADDRESS_LABEL).orElseThrow();
        return driverTools.getText(propertyLocationAddressLabel).contains(address);
    }

    public void selectPropertyLocationZipCodeSourceVendor(String vendorName) {
        driverTools.waitForElementToPresence(PROPERTY_LOCATION_ZIP_CODE_SOURCE_INPUT).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(PROPERTY_LOCATION_ZIP_CODE_SOURCE_INPUT);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getPropertyZipCodeSourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL")));
    }

    public String getPropertyLocationSelectedZipCode() {
        WebElement propertyLocationZipCodeInput = driverTools.waitForElementToPresence(PROPERTY_LOCATION_ZIP_CODE_INPUT).orElseThrow();
        return driverTools.getValueAttribute(propertyLocationZipCodeInput, "value");
    }
    public boolean isPropertyLocationZipCodeDisplayed(String zipCode) {
        WebElement propertyLocationZipCodeLabel = driverTools.waitForElementToAppear(PROPERTY_LOCATION_ZIP_CODE_LABEL).orElseThrow();
        return driverTools.getText(propertyLocationZipCodeLabel).contains(zipCode);
    }

    public void selectPropertyContactInfoPhoneNumberSourceVendor(String vendorName) {
        driverTools.waitForElementToPresence(PROPERTY_CONTACT_INFO_PHONE_NUMBER_SOURCE_INPUT).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(PROPERTY_CONTACT_INFO_PHONE_NUMBER_SOURCE_INPUT);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getPropertyPhoneNumberSourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL")));
    }

    public String getPropertyContactInfoSelectedPhoneNumber() {
        WebElement propertyContactInfoPhoneNumberInput = driverTools.waitForElementToPresence(PROPERTY_CONTACT_INFO_PHONE_NUMBER_INPUT).orElseThrow();
        return driverTools.getValueAttribute(propertyContactInfoPhoneNumberInput, "value");
    }
    public boolean isPropertyContactInfoPhoneNumberDisplayed(String phoneNumber) {
        WebElement propertyContactPhoneNumberLabel = driverTools.waitForElementToAppear(PROPERTY_CONTACT_PHONE_NUMBER_LABEL).orElseThrow();
        return driverTools.getText(propertyContactPhoneNumberLabel).contains(phoneNumber);
    }

    public void selectPropertyContactInfoEmailSourceVendor(String vendorName) {
        driverTools.waitForElementToPresence(PROPERTY_CONTACT_INFO_EMAIL_SOURCE_INPUT).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(PROPERTY_CONTACT_INFO_EMAIL_SOURCE_INPUT);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getPropertyEmailSourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL")));
    }

    public void inputPropertyContactInfoEmail(String email) {
        WebElement propertyContactInfoEmailInput = driverTools.waitForElementToPresence(PROPERTY_CONTACT_INFO_EMAIL_INPUT).orElseThrow();
        if (driverTools.isElementEnabled(propertyContactInfoEmailInput)) {
            driverTools.sendKeys(propertyContactInfoEmailInput, email);
        }
    }

    public void clearPropertyContactInfoEmail() {
        WebElement propertyContactInfoEmail = driverTools.waitForElementToPresence(PROPERTY_CONTACT_INFO_EMAIL_CLEAR_BUTTON).orElseThrow();
        driverTools.click(propertyContactInfoEmail);
    }

    public String getPropertyContactInfoSelectedEmail() {
        WebElement propertyContactInfoEmailInput = driverTools.waitForElementToPresence(PROPERTY_CONTACT_INFO_EMAIL_INPUT).orElseThrow();
        return driverTools.getValueAttribute(propertyContactInfoEmailInput, "value");
    }
    public boolean isPropertyContactInfoEmailDisplayed(String email) {
        WebElement propertyContactEmailLabel = driverTools.waitForElementToAppear(PROPERTY_CONTACT_EMAIL_LABEL).orElseThrow();
        return driverTools.getText(propertyContactEmailLabel).contains(email);
    }

    public void clickEditPropertyDescriptionAndPolicy() {
        WebElement propertyDescriptionAndPolicyEditButton = driverTools.waitForElementToPresence(PROPERTY_DESCRIPTION_AND_POLICY_EDIT_BUTTON).orElseThrow();
        driverTools.click(propertyDescriptionAndPolicyEditButton);
    }

    public boolean isPropertyDescriptionAndPolicyPageDisplayed() {
        return driverTools.waitForElementToAppear(PROPERTY_DESCRIPTION_SOURCE_INPUT)
                .map(el -> !driverTools.getValueAttribute(el, "value").isBlank())
                .orElse(false);
    }

    public void selectPropertyDescriptionSourceVendor(String vendorName) {
        WebElement propertyDescriptionSourceInput = driverTools.waitForElementToPresence(PROPERTY_DESCRIPTION_SOURCE_INPUT).orElseThrow();
        driverTools.click(propertyDescriptionSourceInput);
        driverTools.click(getPropertyDescriptionSourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL")));
    }

    public String getEditPropertyDescriptionIdValue() {
        WebElement propertyDescriptionIdTextarea = driverTools.waitForElementToPresence(PROPERTY_DESCRIPTION_ID_TEXTAREA).orElseThrow();
        return driverTools.getText(propertyDescriptionIdTextarea);
    }

    public String getEditPropertyDescriptionEnValue() {
        WebElement propertyDescriptionEnTextarea = driverTools.waitForElementToPresence(PROPERTY_DESCRIPTION_EN_TEXTAREA).orElseThrow();
        return driverTools.getText(propertyDescriptionEnTextarea);
    }
    public boolean isPropertyDescriptionIdDisplayed(String description) {
        WebElement propertyDescriptionIdLabel = driverTools.waitForElementToAppear(PROPERTY_DESCRIPTION_ID_LABEL).orElseThrow();
        return driverTools.getText(propertyDescriptionIdLabel).contains(description);
    }

    public boolean isPropertyDescriptionEnDisplayed(String description) {
        WebElement propertyDescriptionEnLabel = driverTools.waitForElementToAppear(PROPERTY_DESCRIPTION_EN_LABEL).orElseThrow();
        return driverTools.getText(propertyDescriptionEnLabel).contains(description);
    }

    public void selectPropertyPolicySourceVendor(String vendorName) {
        WebElement propertyPolicySourceInput = driverTools.waitForElementToPresence(PROPERTY_POLICY_SOURCE_INPUT).orElseThrow();
        driverTools.click(propertyPolicySourceInput);
        driverTools.click(getPropertyPolicySourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL")));
    }

    private void clearTextarea(WebElement element) {
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(element);
        String selectAllModifier = CoreFunctions.isMacSystem() ? Keys.COMMAND.toString() : Keys.CONTROL.toString();
        driverTools.sendKeys(element, selectAllModifier + "a");
        driverTools.sendKeys(element, String.valueOf(Keys.DELETE));
    }

    public void inputPropertyPolicyId(String policy) {
        WebElement propertyPolicyIdTextarea = driverTools.waitForElementToPresence(PROPERTY_POLICY_ID_TEXTAREA).orElseThrow();
        clearTextarea(propertyPolicyIdTextarea);
        driverTools.sendKeys(propertyPolicyIdTextarea, policy);
    }

    public void inputPropertyPolicyEn(String policy) {
        WebElement propertyPolicyEnTextarea = driverTools.waitForElementToPresence(PROPERTY_POLICY_EN_TEXTAREA).orElseThrow();
        clearTextarea(propertyPolicyEnTextarea);
        driverTools.sendKeys(propertyPolicyEnTextarea, policy);
    }

    public String getEditPropertyPolicyIdValue() {
        WebElement propertyPolicyIdTextarea = driverTools.waitForElementToPresence(PROPERTY_POLICY_ID_TEXTAREA).orElseThrow();
        return driverTools.getText(propertyPolicyIdTextarea);
    }

    public String getEditPropertyPolicyEnValue() {
        WebElement propertyPolicyEnTextarea = driverTools.waitForElementToPresence(PROPERTY_POLICY_EN_TEXTAREA).orElseThrow();
        return driverTools.getText(propertyPolicyEnTextarea);
    }
    public boolean isPropertyPolicyIdDisplayed(String policy) {
        WebElement propertyPolicyIdLabel = driverTools.waitForElementToAppear(PROPERTY_POLICY_ID_LABEL).orElseThrow();
        return driverTools.getText(propertyPolicyIdLabel).contains(policy);
    }

    public boolean isPropertyPolicyEnDisplayed(String policy) {
        WebElement propertyPolicyEnLabel = driverTools.waitForElementToAppear(PROPERTY_POLICY_EN_LABEL).orElseThrow();
        return driverTools.getText(propertyPolicyEnLabel).contains(policy);
    }

    public void selectPropertyCheckInAndOutSourceVendor(String vendorName) {
        WebElement propertyCheckInAndOutSourceInput = driverTools.waitForElementToPresence(PROPERTY_CHECK_IN_AND_OUT_SOURCE_INPUT).orElseThrow();
        if (driverTools.getValueAttribute(propertyCheckInAndOutSourceInput, "value").contains(getNativeVendorNameByCase(vendorName, "NORMAL"))) {
            return;
        }
        Locator anyCheckInAndOutSourceOption = new Locator("ANY_CHECK_IN_AND_OUT_SOURCE_OPTION", new By.ByXPath("//li[@data-name='checkInOutSource'][not(ancestor::div[contains(@class,'opacity-0')])]"));
        driverTools.clickWithSyntheticEvents(PROPERTY_CHECK_IN_AND_OUT_SOURCE_INPUT);
        if (!driverTools.waitForElementsToAppear(anyCheckInAndOutSourceOption, 5).isPresent()) {
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(PROPERTY_CHECK_IN_AND_OUT_SOURCE_INPUT);
            driverTools.waitForElementsToAppear(anyCheckInAndOutSourceOption).orElseThrow();
        }
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getPropertyCheckInAndOutSourceVendorOption(getNativeVendorNameByCase(vendorName, "NORMAL")));
    }

    public String getEditPropertyCheckInStartTime() {
        WebElement propertyCheckInStartTimeInput = driverTools.waitForElementToPresence(PROPERTY_CHECK_IN_START_TIME_INPUT).orElseThrow();
        return driverTools.getValueAttribute(propertyCheckInStartTimeInput, "value");
    }

    public String getEditPropertyCheckInEndTime() {
        WebElement propertyCheckInEndTimeInput = driverTools.waitForElementToPresence(PROPERTY_CHECK_IN_END_TIME_INPUT).orElseThrow();
        return driverTools.getValueAttribute(propertyCheckInEndTimeInput, "value");
    }

    public String getEditPropertyCheckOutTime() {
        WebElement propertyCheckOutTimeInput = driverTools.waitForElementToPresence(PROPERTY_CHECK_OUT_TIME_INPUT).orElseThrow();
        return driverTools.getValueAttribute(propertyCheckOutTimeInput, "value");
    }

    private String[] getPropertyCheckInTimeLabel() {
        String checkInTime = "";
        int retryCounter = 0;
        do {
            WebElement propertyCheckInTimeLabel = driverTools.waitForElementToAppear(PROPERTY_CHECK_IN_TIME_LABEL, 5).orElseThrow();
            checkInTime = driverTools.getText(propertyCheckInTimeLabel).trim().split("\n")[0].trim();
            retryCounter++;
        } while ((checkInTime.startsWith("(") || checkInTime.isEmpty()) && retryCounter < MAX_RETRY);
        checkInTime = checkInTime.replace("(", "").replace(")", "").trim();
        if (checkInTime.contains("-")) {
            String[] parts = checkInTime.split("-");
            return new String[]{parts[0].trim(), parts[1].trim()};
        }
        return new String[]{checkInTime, checkInTime};
    }

    public String getCurrentCheckInStartTimeFromDetailView() {
        return getPropertyCheckInTimeLabel()[0].trim();
    }

    public String getCurrentCheckInEndTimeFromDetailView() {
        return getPropertyCheckInTimeLabel()[1].trim();
    }

    private String getPropertyCheckOutTimeLabelText() {
        String checkOutTime = "";
        int retryCounter = 0;
        do {
            WebElement propertyCheckOutTimeLabel = driverTools.waitForElementToAppear(PROPERTY_CHECK_OUT_TIME_LABEL, 5).orElseThrow();
            checkOutTime = driverTools.getText(propertyCheckOutTimeLabel).trim().split("\n")[0].trim();
            retryCounter++;
        } while ((checkOutTime.startsWith("(") || checkOutTime.isEmpty()) && retryCounter < MAX_RETRY);
        return checkOutTime.replace("(", "").replace(")", "").trim();
    }

    public String getCurrentCheckOutTimeFromDetailView() {
        return getPropertyCheckOutTimeLabelText();
    }

    public boolean isPropertyCheckInStartTimeDisplayed(String time) {
        try {
            new WebDriverWait(driverTools.getDriver(), Duration.ofSeconds(20))
                    .until(driver -> getPropertyCheckInTimeLabel()[0].contains(time));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isPropertyCheckInEndTimeDisplayed(String time) {
        try {
            new WebDriverWait(driverTools.getDriver(), Duration.ofSeconds(20))
                    .until(driver -> getPropertyCheckInTimeLabel()[1].contains(time));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isPropertyCheckOutTimeDisplayed(String time) {
        int retryCounter = 0;
        boolean matches;
        do {
            matches = getPropertyCheckOutTimeLabelText().contains(time);
            if (!matches) {
                sleepQuietly(500);
            }
            retryCounter++;
        } while (!matches && retryCounter < MAX_RETRY);
        return matches;
    }

    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void dismissAlertIfPresent() {
        int alertAcceptedCount = 0;
        int missCounter = 0;
        int maxMisses = 5;
        while (missCounter < maxMisses) {
            try {
                Alert alert = driverTools.getDriver().switchTo().alert();
                alert.accept();
                alertAcceptedCount++;
                missCounter = 0;
                sleepQuietly(300);
            } catch (NoAlertPresentException e) {
                missCounter++;
                if (alertAcceptedCount > 0) {
                    break;
                }
                sleepQuietly(300);
            }
        }
    }

    public void setPropertyCheckInStartTime(String time) {
        int retryCounter = 0;
        String actualValue;
        String selectAllModifier = CoreFunctions.isMacSystem() ? Keys.COMMAND.toString() : Keys.CONTROL.toString();
        do {
            WebElement input = driverTools.waitForElementToPresence(PROPERTY_CHECK_IN_START_TIME_INPUT).orElseThrow();
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(input);
            driverTools.sendKeys(input, selectAllModifier + "a");
            driverTools.sendKeys(input, String.valueOf(Keys.DELETE));
            driverTools.sendKeys(input, time);
            driverTools.sendKeys(input, Keys.TAB.toString());
            input = driverTools.waitForElementToPresence(PROPERTY_CHECK_IN_START_TIME_INPUT).orElseThrow();
            actualValue = driverTools.getValueAttribute(input, "value");
            retryCounter++;
        } while (!actualValue.equals(time) && retryCounter < MAX_RETRY);
    }

    public void setPropertyCheckOutTime(String time) {
        int retryCounter = 0;
        String actualValue;
        String selectAllModifier = CoreFunctions.isMacSystem() ? Keys.COMMAND.toString() : Keys.CONTROL.toString();
        do {
            WebElement input = driverTools.waitForElementToPresence(PROPERTY_CHECK_OUT_TIME_INPUT).orElseThrow();
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(input);
            driverTools.sendKeys(input, selectAllModifier + "a");
            driverTools.sendKeys(input, String.valueOf(Keys.DELETE));
            driverTools.sendKeys(input, time);
            driverTools.sendKeys(input, Keys.TAB.toString());
            input = driverTools.waitForElementToPresence(PROPERTY_CHECK_OUT_TIME_INPUT).orElseThrow();
            actualValue = driverTools.getValueAttribute(input, "value");
            retryCounter++;
        } while (!actualValue.equals(time) && retryCounter < MAX_RETRY);
    }

    public void clickEditPropertyFacility() {
        driverTools.waitForElementToAppear(PROPERTY_FACILITY_EDIT_BUTTON).orElseThrow();
        driverTools.click(PROPERTY_FACILITY_EDIT_BUTTON);
    }

    public int getNumberOfSelectedPropertyFacilityLabel() {
        WebElement label = driverTools.waitForElementToAppear(PROPERTY_FACILITY_POPULAR_FACILITY_LABEL).orElseThrow();
        List<WebElement> propertyFacilityPopularFacilityLabel = driverTools.elements(PROPERTY_FACILITY_POPULAR_FACILITY_LABEL).orElseThrow();
        List<WebElement> propertyFacilityAllSelectedFacilityLabel = driverTools.elements(PROPERTY_FACILITY_ALL_SELECTED_FACILITY_LABEL).orElseThrow();
        return propertyFacilityAllSelectedFacilityLabel.size() - propertyFacilityPopularFacilityLabel.size();
    }

    public boolean isEditPropertyFacilityPageDisplayed() {
        return driverTools.waitForElementToAppear(EDIT_PROPERTY_FACILITY_HEADER_LABEL).isPresent();
    }

    public void selectPropertyFacilitySourceVendor(String vendorName) {
        driverTools.waitForElementToAppear(PROPERTY_FACILITY_SOURCE_INPUT).orElseThrow();
        driverTools.click(PROPERTY_FACILITY_SOURCE_INPUT);

        driverTools.waitForElementToAppear(getPropertyFacilitySourceVendorOption(getNativeVendorNameCapitalized(vendorName))).orElseThrow();
        driverTools.click(getPropertyFacilitySourceVendorOption(getNativeVendorNameCapitalized(vendorName)));
    }

    public int getNumberOfSelectedPropertyFacilityFromInput() {
        int retryCounter = 0;
        String selectedPropertyFacilityLabel = driverTools.getValueAttribute(PROPERTY_FACILITY_TOTAL_SELECTED_INPUT, "value");
        while ((selectedPropertyFacilityLabel.isEmpty()) && (retryCounter < MAX_RETRY) ) {
            selectedPropertyFacilityLabel = driverTools.getValueAttribute(PROPERTY_FACILITY_TOTAL_SELECTED_INPUT, "value");
            retryCounter++;
        }
        return Integer.parseInt(selectedPropertyFacilityLabel.split(" ")[0]);
    }

    public boolean isPropertyFacilityDisplayed(String facilityName) {
        boolean current = driverTools.waitForElementToAppear(getPropertyFacilityLabelInDetailView(facilityName), 3).isPresent();
        int retryCounter = 0;
        while (retryCounter < MAX_RETRY) {
            sleepQuietly(500);
            boolean recheck = driverTools.waitForElementToAppear(getPropertyFacilityLabelInDetailView(facilityName), 3).isPresent();
            if (recheck == current) {
                break;
            }
            current = recheck;
            retryCounter++;
        }
        return current;
    }

    public boolean isPropertyFacilityCheckboxSelected(String facilityName) {
        WebElement checkbox = waitForPropertyFacilityCheckboxWithScroll(facilityName);
        return driverTools.isElementSelected(checkbox);
    }

    public void setPropertyFacilityCheckbox(String facilityName, boolean expectedSelected) {
        WebElement checkbox = waitForPropertyFacilityCheckboxWithScroll(facilityName);
        if (expectedSelected != driverTools.isElementSelected(checkbox)) {
            driverTools.clickWithJavascriptExecutor(checkbox);
        }
    }

    private WebElement waitForPropertyFacilityCheckboxWithScroll(String facilityName) {
        Optional<WebElement> checkbox = driverTools.waitForElementToPresence(getPropertyFacilityCheckbox(facilityName), 3);
        int retryCounter = 0;
        while (!checkbox.isPresent() && retryCounter < MAX_RETRY) {
            driverTools.scrollToBottomOfPage(1);
            checkbox = driverTools.waitForElementToPresence(getPropertyFacilityCheckbox(facilityName), 3);
            retryCounter++;
        }
        return checkbox.orElseThrow();
    }

    public void clickEditPropertyStatus() {
        WebElement propertyStatusEditButton = driverTools.waitForElementToAppear(PROPERTY_STATUS_EDIT_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(propertyStatusEditButton);
    }

    private String getPropertyStatusDetailText() {
        String statusText = "";
        int retryCounter = 0;
        do {
            // Use a short per-attempt wait so this loop can actually retry across MAX_RETRY
            // attempts instead of dying on the first miss (a single element, if absent, used
            // to fail the whole method via orElseThrow before the loop got a chance to retry).
            Optional<WebElement> statusLabel = driverTools.waitForElementToAppear(PROPERTY_STATUS_DETAIL_LABEL, 3);
            if (statusLabel.isPresent()) {
                statusText = driverTools.getText(statusLabel.get()).trim();
            }
            retryCounter++;
        } while ((statusText.startsWith("(") || statusText.isEmpty()) && retryCounter < MAX_RETRY);
        return statusText.replace("(", "").replace(")", "").trim();
    }

    public boolean isPropertyStatusOnInDetailView() {
        return getPropertyStatusDetailText().equalsIgnoreCase("ON");
    }

    public boolean isPropertyStatusDisplayed(boolean expectedOn) {
        String expectedText = expectedOn ? "ON" : "OFF";
        int retryCounter = 0;
        boolean matches;
        do {
            matches = getPropertyStatusDetailText().equalsIgnoreCase(expectedText);
            if (!matches) {
                sleepQuietly(500);
            }
            retryCounter++;
        } while (!matches && retryCounter < MAX_RETRY);
        return matches;
    }

    public boolean isEditPropertyStatusPageDisplayed() {
        return driverTools.waitForElementToPresence(PROPERTY_STATUS_PROPERTY_LEVEL_TOGGLE).isPresent();
    }

    public boolean isEditPropertyStatusToggleOn() {
        WebElement propertyStatusPropertyLevelToggle = driverTools.waitForElementToPresence(PROPERTY_STATUS_PROPERTY_LEVEL_TOGGLE).orElseThrow();
        return driverTools.isElementSelected(propertyStatusPropertyLevelToggle);
    }

    public void clickPropertyStatusToggle() {
        // Click the visible label wrapping the toggle input, not the hidden input itself
        // (Toggle_hidden_checkbox is visually hidden; a JS-forced click on it does not
        // reliably trigger the same state change as a real click on the label).
        boolean wasSelected = isEditPropertyStatusToggleOn();
        WebElement propertyStatusPropertyLevelToggleClickable = driverTools.waitForElementToBeClickable(PROPERTY_STATUS_PROPERTY_LEVEL_TOGGLE_CLICKABLE).orElseThrow();
        driverTools.click(propertyStatusPropertyLevelToggleClickable);
        // The toggle state does not flip synchronously with the click (likely an async
        // API call / React re-render behind it), so wait for the actual state to change
        // rather than letting the caller assert on it immediately.
        try {
            new WebDriverWait(driverTools.getDriver(), Duration.ofSeconds(10))
                    .until(driver -> isEditPropertyStatusToggleOn() != wasSelected);
        } catch (TimeoutException e) {
            log.warn("Property status toggle state did not change within 10s after click");
        }
    }

    public void selectPropertyStatusToggleOffReason(String reason) {
        WebElement propertyStatusPropertyToggleOffReasonInput = driverTools.waitForElementToPresence(PROPERTY_STATUS_PROPERTY_TOGGLE_OFF_REASON_INPUT).orElseThrow();
        driverTools.click(propertyStatusPropertyToggleOffReasonInput);
        driverTools.click(getPropertyStatusPropertyToggleOffReasonOption(reason));
    }

    public boolean isEditPropertyVendorStatusToggleOn(String vendorName) {
        WebElement propertyStatusVendorLevelToggle = driverTools.waitForElementToPresence(getPropertyStatusVendorLevelToggle(vendorName)).orElseThrow();
        return driverTools.isElementSelected(propertyStatusVendorLevelToggle);
    }

    public void clickPropertyVendorStatusToggle(String vendorName) {
        WebElement propertyStatusVendorLevelToggle = driverTools.waitForElementToPresence(getPropertyStatusVendorLevelToggle(vendorName)).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(propertyStatusVendorLevelToggle);
    }

    public void clickEditPropertyAnnouncement() {
        WebElement propertyAnnouncementEditButton = driverTools.waitForElementToAppear(PROPERTY_ANNOUNCEMENT_EDIT_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(propertyAnnouncementEditButton);
    }

    public boolean isEditPropertyAnnouncementPageDisplayed() {
        return driverTools.waitForElementToAppear(PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_SOURCE_INPUT).isPresent();
    }

    public String getCurrentPropertyAnnouncementHotelLevelEnFromDetailView() {
        WebElement propertyAnnouncementHotelLevelEnLabel = driverTools.waitForElementToAppear(PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_EN_LABEL).orElseThrow();
        return driverTools.getText(propertyAnnouncementHotelLevelEnLabel).trim();
    }

    public String getCurrentPropertyAnnouncementHotelLevelIdFromDetailView() {
        WebElement propertyAnnouncementHotelLevelIdLabel = driverTools.waitForElementToAppear(PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_ID_LABEL).orElseThrow();
        return driverTools.getText(propertyAnnouncementHotelLevelIdLabel).trim();
    }

    public boolean isPropertyAnnouncementHotelLevelIEnDisplayed(String text) {
        int retryCounter = 0;
        boolean matches;
        do {
            WebElement propertyAnnouncementHotelLevelEnLabel = driverTools.waitForElementToAppear(PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_EN_LABEL).orElseThrow();
            matches = driverTools.getText(propertyAnnouncementHotelLevelEnLabel).contains(text);
            if (!matches) {
                sleepQuietly(500);
            }
            retryCounter++;
        } while (!matches && retryCounter < MAX_RETRY);
        return matches;
    }

    public boolean isPropertyAnnouncementHotelLevelIdDisplayed(String text) {
        int retryCounter = 0;
        boolean matches;
        do {
            WebElement propertyAnnouncementHotelLevelIdLabel = driverTools.waitForElementToAppear(PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_ID_LABEL).orElseThrow();
            matches = driverTools.getText(propertyAnnouncementHotelLevelIdLabel).contains(text);
            if (!matches) {
                sleepQuietly(500);
            }
            retryCounter++;
        } while (!matches && retryCounter < MAX_RETRY);
        return matches;
    }

    public boolean isPropertyAnnouncementRoomLevelEnDisplayed(String text) {
        int retryCounter = 0;
        boolean matches;
        do {
            WebElement propertyAnnouncementRoomLevelEnLabel = driverTools.waitForElementToAppear(PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_EN_LABEL).orElseThrow();
            matches = driverTools.getText(propertyAnnouncementRoomLevelEnLabel).contains(text);
            if (!matches) {
                sleepQuietly(500);
            }
            retryCounter++;
        } while (!matches && retryCounter < MAX_RETRY);
        return matches;
    }

    public boolean isPropertyAnnouncementRoomLevelIdDisplayed(String text) {
        int retryCounter = 0;
        boolean matches;
        do {
            WebElement propertyAnnouncementRoomLevelIdLabel = driverTools.waitForElementToAppear(PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_ID_LABEL).orElseThrow();
            matches = driverTools.getText(propertyAnnouncementRoomLevelIdLabel).contains(text);
            if (!matches) {
                sleepQuietly(500);
            }
            retryCounter++;
        } while (!matches && retryCounter < MAX_RETRY);
        return matches;
    }

    public void selectPropertyAnnouncementHotelLevelSourceVendor(String vendorName) {
        WebElement propertyAnnouncementHotelLevelSourceInput = driverTools.waitForElementToPresence(PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_SOURCE_INPUT).orElseThrow();
        driverTools.click(propertyAnnouncementHotelLevelSourceInput);
        WebElement hotelSourceOption = driverTools.waitForElementToAppear(getPropertyAnnouncementHotelLevelSourceVendorOption(vendorName)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(hotelSourceOption);
    }

    public void inputPropertyAnnouncementHotelLevelId(String text) {
        WebElement propertyAnnouncementHotelLevelIdTextarea = driverTools.waitForElementToPresence(PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_ID_TEXTAREA).orElseThrow();
        clearTextarea(propertyAnnouncementHotelLevelIdTextarea);
        driverTools.sendKeys(propertyAnnouncementHotelLevelIdTextarea, text);
    }

    public void inputPropertyAnnouncementHotelLevelEn(String text) {
        WebElement propertyAnnouncementHotelLevelEnTextarea = driverTools.waitForElementToPresence(PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_EN_TEXTAREA).orElseThrow();
        clearTextarea(propertyAnnouncementHotelLevelEnTextarea);
        driverTools.sendKeys(propertyAnnouncementHotelLevelEnTextarea, text);
    }

    public String getEditPropertyAnnouncementHotelLevelIdValue() {
        WebElement propertyAnnouncementHotelLevelIdTextarea = driverTools.waitForElementToPresence(PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_ID_TEXTAREA).orElseThrow();
        return driverTools.getText(propertyAnnouncementHotelLevelIdTextarea);
    }

    public String getEditPropertyAnnouncementHotelLevelEnValue() {
        WebElement propertyAnnouncementHotelLevelEnTextarea = driverTools.waitForElementToPresence(PROPERTY_ANNOUNCEMENT_HOTEL_LEVEL_EN_TEXTAREA).orElseThrow();
        return driverTools.getText(propertyAnnouncementHotelLevelEnTextarea);
    }

    public void selectPropertyAnnouncementRoomLevelSourceVendor(String vendorName) {
        WebElement propertyAnnouncementRoomLevelSourceInput = driverTools.waitForElementToPresence(PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_SOURCE_INPUT).orElseThrow();
        driverTools.click(propertyAnnouncementRoomLevelSourceInput);
        WebElement roomSourceOption = driverTools.waitForElementToAppear(getPropertyAnnouncementRoomLevelSourceVendorOption(vendorName)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(roomSourceOption);
    }

    public void inputPropertyAnnouncementRoomLevelId(String text) {
        WebElement propertyAnnouncementRoomLevelIdTextarea = driverTools.waitForElementToPresence(PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_ID_TEXTAREA).orElseThrow();
        clearTextarea(propertyAnnouncementRoomLevelIdTextarea);
        driverTools.sendKeys(propertyAnnouncementRoomLevelIdTextarea, text);
    }

    public void inputPropertyAnnouncementRoomLevelEn(String text) {
        WebElement propertyAnnouncementRoomLevelEnTextarea = driverTools.waitForElementToPresence(PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_EN_TEXTAREA).orElseThrow();
        clearTextarea(propertyAnnouncementRoomLevelEnTextarea);
        driverTools.sendKeys(propertyAnnouncementRoomLevelEnTextarea, text);
    }

    public String getEditPropertyAnnouncementRoomLevelIdValue() {
        WebElement propertyAnnouncementRoomLevelIdTextarea = driverTools.waitForElementToPresence(PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_ID_TEXTAREA).orElseThrow();
        return driverTools.getText(propertyAnnouncementRoomLevelIdTextarea);
    }

    public String getEditPropertyAnnouncementRoomLevelEnValue() {
        WebElement propertyAnnouncementRoomLevelEnTextarea = driverTools.waitForElementToPresence(PROPERTY_ANNOUNCEMENT_ROOM_LEVEL_EN_TEXTAREA).orElseThrow();
        return driverTools.getText(propertyAnnouncementRoomLevelEnTextarea);
    }

    public void clickSubmit() {
        WebElement editPropertySubmitButton = driverTools.waitForElementToPresence(EDIT_PROPERTY_SUBMIT_BUTTON).orElseThrow();
        driverTools.click(editPropertySubmitButton);
    }

    public void clickEditGeneralInfoSave() {
        WebElement editGeneralInfoSaveButton = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_SAVE_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editGeneralInfoSaveButton);
    }

    public void clickEditGeneralInfoCancel() {
        WebElement editGeneralInfoCancelButton = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_CANCEL_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editGeneralInfoCancelButton);
    }

    public void clickEditPropertyDescriptionAndPolicySave() {
        WebElement editPropertyDescriptionAndPolicySaveButton = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_SAVE_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editPropertyDescriptionAndPolicySaveButton);
    }

    public boolean isEditPropertyDescriptionAndPolicySaveConfirmationAppeared() {
        return driverTools.waitForElementToAppear(EDIT_GENERAL_INFO_SAVE_CONFIRMATION_BUTTON, 3).isPresent();
    }

    public void clickEditPropertyDescriptionAndPolicySaveConfirmation() {
        WebElement editPropertyDescriptionAndPolicySaveConfirmationButton = driverTools.waitForElementToBeClickable(EDIT_GENERAL_INFO_SAVE_CONFIRMATION_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editPropertyDescriptionAndPolicySaveConfirmationButton);
        dismissAlertIfPresent();
    }

    public boolean isEditLocationAndContactSaveConfirmationAppeared() {
        return driverTools.waitForElementToAppear(EDIT_GENERAL_INFO_SAVE_CONFIRMATION_BUTTON, 3).isPresent();
    }

    public void clickEditLocationAndContactSaveConfirmation() {
        WebElement editLocationAndContactSaveConfirmationButton = driverTools.waitForElementToBeClickable(EDIT_GENERAL_INFO_SAVE_CONFIRMATION_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editLocationAndContactSaveConfirmationButton);
        dismissAlertIfPresent();
    }

    public boolean isEditGeneralInfoSaveConfirmationAppeared() {
        return driverTools.waitForElementToAppear(EDIT_GENERAL_INFO_SAVE_CONFIRMATION_BUTTON, 3).isPresent();
    }

    public void clickEditGeneralInfoSaveConfirmation() {
        WebElement editGeneralInfoSaveConfirmationButton = driverTools.waitForElementToBeClickable(EDIT_GENERAL_INFO_SAVE_CONFIRMATION_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editGeneralInfoSaveConfirmationButton);
        dismissAlertIfPresent();
    }

    public void clickEditLocationAndContactSave() {
        WebElement editLocationAndContactSaveButton = driverTools.waitForElementToPresence(EDIT_LOCATION_AND_CONTACT_SAVE_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editLocationAndContactSaveButton);
    }

    public void clickEditPropertyFacilitySave() {
        WebElement editPropertyFacilitySaveButton = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_SAVE_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editPropertyFacilitySaveButton);
    }

    public boolean isEditPropertyFacilitySaveConfirmationAppeared() {
        return driverTools.waitForElementToAppear(EDIT_GENERAL_INFO_SAVE_CONFIRMATION_BUTTON, 3).isPresent();
    }

    public void clickEditPropertyFacilitySaveConfirmation() {
        WebElement editPropertyFacilitySaveConfirmationButton = driverTools.waitForElementToBeClickable(EDIT_GENERAL_INFO_SAVE_CONFIRMATION_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editPropertyFacilitySaveConfirmationButton);
        dismissAlertIfPresent();
    }

    public void clickEditPropertyStatusSave() {
        WebElement editPropertyStatusSaveButton = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_SAVE_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editPropertyStatusSaveButton);
    }

    public boolean isEditPropertyStatusSaveConfirmationAppeared() {
        return driverTools.waitForElementToAppear(EDIT_GENERAL_INFO_SAVE_CONFIRMATION_BUTTON, 3).isPresent();
    }

    public void clickEditPropertyStatusSaveConfirmation() {
        WebElement editPropertyStatusSaveConfirmationButton = driverTools.waitForElementToBeClickable(EDIT_GENERAL_INFO_SAVE_CONFIRMATION_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editPropertyStatusSaveConfirmationButton);
        dismissAlertIfPresent();
    }

    public void clickEditPropertyAnnouncementSave() {
        WebElement editPropertyAnnouncementSaveButton = driverTools.waitForElementToPresence(EDIT_GENERAL_INFO_SAVE_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editPropertyAnnouncementSaveButton);
    }

    public boolean isEditPropertyAnnouncementSaveConfirmationAppeared() {
        return driverTools.waitForElementToAppear(EDIT_GENERAL_INFO_SAVE_CONFIRMATION_BUTTON, 3).isPresent();
    }

    public void clickEditPropertyAnnouncementSaveConfirmation() {
        WebElement editPropertyAnnouncementSaveConfirmationButton = driverTools.waitForElementToBeClickable(EDIT_GENERAL_INFO_SAVE_CONFIRMATION_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editPropertyAnnouncementSaveConfirmationButton);
        dismissAlertIfPresent();
    }

    public void clickEditLocationAndContactCancel() {
        WebElement editLocationAndContactCancelButton = driverTools.waitForElementToPresence(EDIT_LOCATION_AND_CONTACT_CANCEL_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(editLocationAndContactCancelButton);
    }

    public boolean isConfirmPopUpDialogAppeared() {
        return driverTools.waitForElementToAppear(CONFIRM_POPUP_DIALOG, 3).isPresent();
    }

    public void clickSubmitConfirmPopUp() {
        WebElement confirmPopupSubmitButton = driverTools.waitForElementToBeClickable(CONFIRM_POPUP_SUBMIT_BUTTON, 3).orElseThrow();
        try {
            driverTools.click(confirmPopupSubmitButton);
            driverTools.waitForElementToAppear(CONFIRM_POPUP_SUBMIT_LOADING, 10).orElseThrow();
        } catch (Exception e) {
            driverTools.click(confirmPopupSubmitButton);
        }
    }

    public boolean isSubmitSuccessfulSnackbarDisappeared() {
        WebElement submitSuccessfulSnackbar = driverTools.waitForElementToAppear(SUBMIT_SUCCESSFUL_SNACKBAR).orElseThrow();
        return driverTools.waitForElementToDisappear(submitSuccessfulSnackbar);
    }

}
