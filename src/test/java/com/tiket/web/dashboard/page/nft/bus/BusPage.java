package com.tiket.web.dashboard.page.nft.bus;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.io.File;

public class BusPage <T extends WebDriver> extends BasePage<T> {
    private static final Locator TOGGLE_ACTIVE_OR_INACTIVE_STATUS = new Locator("TOGGLE_ACTIVE_OR_INACTIVE_STATUS", new By.ByXPath("(//button[contains(@class,'ant-switch')])[1]"));
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator AGREE_BUTTON = new Locator("AGREE_BUTTON", new By.ByXPath("//div[@class='wrapper_button_confirm']/child::button[contains(@class,'KV-nSG_variant_primary')]"));
    private static final Locator BUS_FIRST_ITEM = new Locator("BUS_FIRST_ITEM", new By.ByXPath("//td[@class='ant-table-cell'][@class='ant-table-cell'][contains(text(),'01')]"));
    private static final Locator BUS_LOCATION_FIRST_ITEM_OF_EDIT_BUTTON = new Locator ("BUS_LOCATION_FIRST_ITEM_OF_EDIT_BUTTON", new By.ByXPath("//td[@class='ant-table-cell' and contains(text(),'01')]/following-sibling::td[contains(@class, 'ant-table-cell-fix-right-first')]//a[contains(text(),'Edit')]"));
    private static final Locator BUS_LOCATION_SAVE_BUTTON_TO_UPDATE = new Locator ("BUS_LOCATION_SAVE_BUTTON_TO_UPDATE", new By.ByXPath("//button[contains(@type,'submit')]"));
    private static final Locator BUS_LOCATION_SEARCH_TEXT_INPUT = new Locator ("BUS_LOCATION_SEARCH_TEXT_INPUT", new By.ByXPath("//input[contains(@placeholder,'Find Location Name')]"));
    private static final Locator BUS_LOCATION_SUCCESS_SNACKBAR= new Locator("BUS_LOCATION_UPDATE_SNACKBAR", new By.ByXPath("//div[contains(@class,'ant-notification-notice-description') and contains(text(),'berhasil')]"));
    private static final Locator BUS_TABLE= new Locator("BUS_TABLE", new By.ByXPath("//div[contains(@class,'wrapper-table-v4')]"));
    private static final Locator BUS_OPERATOR_TOGGLE_SWITCH_STATUS_FINDER= new Locator("BUS_OPERATOR_TOGGLE_SWITCH_STATUS_FINDER", new By.ByXPath("(//span[contains(@class,'ant-switch-inner')])[3]"));
    private static final Locator LOADING_STATE = new Locator("LOADING_STATE", new By.ByXPath("//span[contains(@class,'ant-spin-dot')]"));
    private static final Locator BUS_VEHICLE_MENU = new Locator("BUS_VEHICLE_MENU", new By.ByXPath("//span[@class='ant-menu-title-content'][contains(text(),'Vehicle')]"));
    private static final Locator BUS_VEHICLE_TOGGLE_SWITCH_STATUS_FINDER= new Locator("BUS_VEHICLE_TOGGLE_SWITCH_STATUS_FINDER", new By.ByXPath("(//span[contains(@class,'ant-switch-inner')])[1]"));
    private static final Locator BUS_VEHICLE_TOGGLE_ACTIVE_OR_INACTIVE_STATUS = new Locator("BUS_VEHICLE_TOGGLE_ACTIVE_OR_INACTIVE_STATUS", new By.ByXPath("(//button[contains(@class,'ant-switch')])[1]"));
    private static final Locator BUS_EDIT_BUTTON = new Locator ("BUS_EDIT_BUTTON", new By.ByXPath("//td[@class='ant-table-cell']/following-sibling::td[contains(@class, 'ant-table-cell-fix-right-first')]//a[contains(text(),'Edit')]"));
    private static final Locator BUS_VEHICLE_SEARCH_INPUT = new Locator("BUS_VEHICLE_SEARCH_INPUT", new By.ByXPath("//input[@id='sb-text-input']"));
    private static final Locator BUS_VEHICLE_DESCRIPTION_ID_INPUT = new Locator("BUS_VEHICLE_DESCRIPTION_ID_INPUT", new By.ByXPath("//input[@id='description-id']"));
    private static final Locator BUS_VEHICLE_DESCRIPTION_EN_INPUT = new Locator("BUS_VEHICLE_DESCRIPTION_EN_INPUT", new By.ByXPath("//input[@id='description-en']"));
    private static final Locator BUS_VEHICLE_UPLOAD_IMAGE = new Locator("BUS_VEHICLE_UPLOAD_IMAGE", new By.ByXPath("//input[@type='file']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", new By.ByXPath("//div[contains(@class,'ant-notification-notice') and contains(text(),'berhasil')]"));
    private static final Locator IMAGE_UPLOAD_SUCCESSFUL_SNACKBAR = new Locator("IMAGE_UPLOAD_SUCCESSFUL_SNACKBAR", new By.ByXPath("//div[contains(@class,'ant-notification-notice-description') and contains(text(),'Successfully image upload')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[contains(@type,'submit')]"));
    private static final Locator FIRST_ITEM_AFTER_SEARCHED = new Locator("FIRST_ITEM_AFTER_SEARCHED", new By.ByXPath("//td[@class='ant-table-cell'][@class='ant-table-cell'][contains(text(),'01')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[contains(text(),'Search')]"));
    private static final Locator LABEL_INPUT = new Locator("LABEL_INPUT", new By.ByXPath("//input[@name='label']"));
    private static final Locator CREATE_FACILITY_BUTTON = new Locator("CREATE_FACILITY_BUTTON", new By.ByXPath("//a[contains(text(),'Add Facility')]"));
    private static final Locator PRIORITY_SCORING_INPUT = new Locator("PRIORITY_SCORING_INPUT", new By.ByXPath("//input[@name='priorityScore']"));
    private static final Locator FACILITY_NAME_ID_INPUT = new Locator("FACILITY_NAME_ID_INPUT", new By.ByXPath("//input[@id='facility-id']"));
    private static final Locator FACILITY_NAME_EN_INPUT = new Locator("FACILITY_NAME_EN_INPUT", new By.ByXPath("//input[@id='facility-en']"));
    private static final Locator UPLOAD_FACILITY_ICON_DEFAULT = new Locator("UPLOAD_FACILITY_ICON_DEFAULT", new By.ByXPath("//span[contains(text(), 'Upload Facility Icon (Default)')]/following-sibling::input[@type='file']"));
    private static final Locator UPLOAD_FACILITY_ICON_SELECTED = new Locator("UPLOAD_FACILITY_ICON_SELECTED", new By.ByXPath("//span[contains(text(), 'Upload Facility Icon (Selected)')]/following-sibling::input[@type='file']"));
    private static final Locator BUS_FACILITIES_SEARCH_INPUT = new Locator("BUS_FACILITIES_SEARCH_INPUT", new By.ByXPath("//input[@id='sb-text-input']"));
    private static final Locator BUS_FACILITIES_TOGGLE_ACTIVE_OR_INACTIVE_STATUS = new Locator("BUS_FACILITIES_TOGGLE_ACTIVE_OR_INACTIVE_STATUS", new By.ByXPath("(//button[contains(@class,'ant-switch')])[1]"));
    private static final Locator BUS_FACILITIES_SUCCESS_SNACKBAR= new Locator("BUS_FACILITIES_SUCCESS_SNACKBAR", new By.ByXPath("//div[contains(@class,'ant-notification-notice') and contains(text(),'berhasil')]"));
    private static final Locator DELETE_FACILITY_BUTTON = new Locator("DELETE_FACILITY_BUTTON", new By.ByXPath("//td[@class='ant-table-cell']/following-sibling::td[contains(@class, 'ant-table-cell-fix-right-first')]//a[contains(text(),'Delete')]"));
    private static final Locator BUS_LOCATION_UPDATE_SNACKBAR= new Locator("BUS_LOCATION_UPDATE_SNACKBAR", new By.ByXPath("//div[contains(@class,'ant-notification-notice-description')]"));
    private static final Locator BUS_LOCATION_POINT_MENU = new Locator("BUS_LOCATION_POINT_MENU", new By.ByXPath("//span[@class='ant-menu-title-content'][contains(text(),'Location Points')]"));
    private static final Locator BUS_LOCATION_POINT_SUCCESS_ACTIVATION_SNACKBAR = new Locator("BUS_LOCATION_POINT_SUCCESS_ACTIVATION_SNACKBAR", new By.ByXPath("//div[contains(@class,'ant-notification-notice-description')][contains(text(),'berhasil di Aktifkan')]"));
    private static final Locator BUS_LOCATION_POINT_SUCCESS_INACTIVATION_SNACKBAR = new Locator("BUS_LOCATION_POINT_SUCCESS_INACTIVATION_SNACKBAR", new By.ByXPath("//div[contains(@class,'ant-notification-notice-description')][contains(text(),'berhasil di Nonaktifkan')]"));
    private static final Locator BUS_LOCATION_POINT_SEARCH_TEXT_INPUT = new Locator ("BUS_LOCATION_SEARCH_TEXT_INPUT", new By.ByXPath("//input[contains(@placeholder,'Find Label')]"));
    private static final Locator BUS_OPERATOR_MENU = new Locator("BUS_OPERATOR_MENU", new By.ByXPath("//span[@class='ant-menu-title-content'][contains(text(),'Operator')]"));
    private static final Locator BUS_OPERATOR_ITEM = new Locator("BUS_OPERATOR_ITEM", new By.ByXPath("//tr[@class='ant-table-row ant-table-row-level-0']"));
    private static final Locator BUS_OPERATOR_EDIT_BUTTON = new Locator ("BUS_OPERATOR_EDIT_BUTTON", new By.ByXPath("(//td[@class='ant-table-cell']/following-sibling::td[contains(@class, 'ant-table-cell-fix-right-first')]//a[contains(text(),'Edit')])[1]"));
    private static final Locator BUS_SAVE_BUTTON = new Locator("BUS_SAVE_BUTTON", new By.ByXPath("//button[contains(@class, 'KV-nSG_button') and contains(@class, 'KV-nSG_size_large') and contains(@class, 'KV-nSG_horizontal_padding') and contains(@class, 'KV-nSG_variant_primary') and text()='Save']"));
    private static final Locator BUS_SUCCESS_SNACKBAR= new Locator("BUS_SUCCESS_SNACKBAR", new By.ByXPath("//div[contains(@class,'ant-notification-notice-description') and contains(text(),'berhasil')]"));
    private static final Locator BUS_OPERATOR_SUCCESS_ACTIVATION_OR_INACTIVATION_SNACKBAR= new Locator("BUS_OPERATOR_SUCCESS_ACTIVATION_OR_INACTIVATION_SNACKBAR", new By.ByXPath("//div[contains(@class,'ant-notification-notice-description') and contains(text(),'berhasil')]"));
    private static final Locator BUS_OPERATOR_TOGGLE_INACTIVE_OR_ACTIVE_STATUS = new Locator("BUS_OPERATOR_TOGGLE_INACTIVE_TO_ACTIVE_STATUS", new By.ByXPath("(//button[contains(@class,'ant-switch')])[3]"));
    private static final Locator BUS_OPERATOR_TOGGLE_INACTIVE_OR_ACTIVE_RESCHEDULE = new Locator("BUS_OPERATOR_TOGGLE_INACTIVE_OR_ACTIVE_RESCHEDULE", new By.ByXPath("(//button[contains(@class,'ant-switch')])[2]"));
    private static final Locator BUS_OPERATOR_TOGGLE_INACTIVE_OR_ACTIVE_REFUND = new Locator("BUS_OPERATOR_TOGGLE_INACTIVE_OR_ACTIVE_REFUND", new By.ByXPath("(//button[contains(@class,'ant-switch')])[1]"));
    private static final Locator DATA_MAPPING_MENU = new Locator("DATA_MAPPING_MENU", new By.ByXPath("//span[@class='ant-menu-title-content'][contains(text(),'Data Mapping')]"));
    private static final Locator VENDOR_OPERATOR_MENU = new Locator("VENDOR_OPERATOR_MENU", new By.ByXPath("//span[@class='ant-menu-title-content'][contains(text(),'Vendor Operator')]"));
    private static final Locator VENDOR_LOCATION_MENU = new Locator("VENDOR_LOCATION_MENU", new By.ByXPath("//span[@class='ant-menu-title-content'][contains(text(),'Vendor Location')]"));
    private static final Locator DATA_MAPPING_SEARCH_BUTTON = new Locator("DATA_MAPPING_SEARCH_BUTTON", new By.ByXPath("//button[contains(text(),'Search')]"));
    private static final Locator MAP_BUTTON = new Locator("MAP_BUTTON", new By.ByXPath("//a[@role='link'][contains(text(),'Map')]"));
    private static final Locator SUCCESSFULLY_MAPPED_SNACKBAR = new Locator("SUCCESSFULLY_MAPPED_SNACKBAR", new By.ByXPath("//div[contains(@class,'ant-notification-notice') and contains(text(),'Successfully Mapped')]"));
    private static final Locator SUCCESSFULLY_UNMAPPED_SNACKBAR = new Locator("SUCCESSFULLY_UNMAPPED_SNACKBAR", new By.ByXPath("//div[contains(@class,'ant-notification-notice') and contains(text(),'Successfully Unmapped')]"));
    private static final Locator FOOTER = new Locator("FOOTER", new By.ByXPath("//span[contains(text(),'2018 PT. Global Tiket Network. All Rights Reserved')]"));
    private static final Locator PREVIOUS_PAGE_BUTTON = new Locator("PREVIOUS_PAGE_BUTTON", new By.ByXPath("//li[@title='Previous Page']/button[@class='ant-pagination-item-link']"));
    private static final Locator NEXT_PAGE_BUTTON = new Locator("NEXT_PAGE", new By.ByXPath("//li[@title='Next Page']/button[@class='ant-pagination-item-link']"));
    private static final Locator UNMAP_BUTTON = new Locator("UNMAP_BUTTON", new By.ByXPath("//a[contains(text(),'Unmap')]"));
    private static final Locator AGREE_TO_UNMAP_BUTTON = new Locator("UNMAP_BUTTON", new By.ByXPath("//button[contains(text(),'Setuju')]"));
    private static final Locator SEARCH_TIKETS_VEHICLE_DROPDOWN = new Locator("SEARCH_TIKETS_VEHICLE_DROPDOWN", new By.ByXPath("//p[contains(text(),'Search Vehicle')]"));
    private static final Locator SEARCH_TIKETS_LOCATION_DROPDOWN = new Locator("SEARCH_TIKETS_LOCATION_DROPDOWN", new By.ByXPath("//p[contains(text(),'Location')]"));
    private static final Locator SEARCH_VENDORS_VENDOR_DROPDOWN = new Locator("SEARCH_VENDORS_VENDOR_DROPDOWN", new By.ByXPath("//p[contains(text(),'Choose Vendor')]"));
    private static final Locator SEARCH_VENDORS_OPERATOR_DROPDOWN = new Locator("SEARCH_VENDORS_OPERATOR_DROPDOWN", new By.ByXPath("//p[contains(text(),'Choose Operator')]"));
    private static final Locator SEARCH_VENDORS_LOCATION_DROPDOWN = new Locator("SEARCH_VENDORS_LOCATION_DROPDOWN", new By.ByXPath("//p[contains(text(),'Choose Location')]"));
    private static final Locator SEARCH_VENDORS_VEHICLE_DROPDOWN = new Locator("SEARCH_VENDORS_VEHICLE_DROPDOWN", new By.ByXPath("//p[contains(text(),'Choose Vehicle')]"));
    private static final Locator SEARCH_TO_SELECT_INPUT = new Locator("SEARCH_TO_SELECT_INPUT", new By.ByXPath("//input[@class='tix-dashboard-select__input']"));
    private static final Locator MAP_AND_SAVE_BUTTON = new Locator("MAP_AND_SAVE_BUTTON", new By.ByXPath("//button[contains(text(),'Map & Save')]"));
    private static final Locator DATA_MAPPING_SEARCH_INPUT = new Locator("VENDOR_OPERATOR_SEARCH_INPUT", new By.ByXPath("//input[@id='sb-text-input']"));
    private static final Locator TIKETS_DATA = new Locator("TIKETS_DATA", new By.ByXPath("//span[contains(text(), 'Tiket') and contains(text(), 'Data')]"));
    private static final Locator VENDORS_DATA = new Locator("VENDORS_DATA", new By.ByXPath("//span[contains(text(), 'Vendor') and contains(text(), 'Data')]"));
    private static final Locator ADD_ANOTHER_MAPPING_BUTTON = new Locator("ADD_ANOTHER_MAPPING_BUTTON", By.xpath("//button[contains(text(), 'Add Another Mapping')]"));
    private static final Locator DELETE_ICON = new Locator("DELETE_ICON", By.xpath("//div[@style='background-color: rgb(255, 223, 223); padding: 11px 20px; border-radius: 8px; line-height: 1; cursor: pointer;']"));
    private static final Locator CREATE_VEHICLE_BUTTON = new Locator("CREATE_VEHICLE_BUTTON", new By.ByXPath("//a[contains(text(),'Add Vehicle')]"));
    private static final Locator CARI_TYPE_CLICK_INPUT = new Locator("CARI_TYPE_INPUT", new By.ByXPath("//span[contains(text(),'Cari Type')]"));
    private static final Locator CARI_TYPE_SEARCH_INPUT = new Locator("CARI_TYPE_SEARCH_INPUT", new By.ByXPath("//input[(contains(@class,'tix-dashboard-select__input'))]"));
    private static final Locator SELECT_BUS_INPUT = new Locator("SELECT_BUS_INPUT", new By.ByXPath("//div[contains(text(),'Bus')]"));
    private static final Locator CLASS_NAME_ID_INPUT = new Locator("CLASS_NAME_ID_INPUT", new By.ByXPath("//input[@id='class-name-id']"));
    private static final Locator CLASS_NAME_EN_INPUT = new Locator("CLASS_NAME_EN_INPUT", new By.ByXPath("//input[@id='class-name-en']"));
    private static final Locator DELETE_VEHICLE_BUTTON = new Locator("DELETE_VEHICLE_BUTTON", new By.ByXPath("//td[@class='ant-table-cell']/following-sibling::td[contains(@class, 'ant-table-cell-fix-right-first')]//a[contains(text(),'Delete')]"));
    private static final Locator ADD_OPERATOR_BUTTON = new Locator("ADD_OPERATOR_BUTTON", By.xpath("//a[contains(text(), 'Add Operator')]"));
    private static final Locator SEARCH_INPUT = new Locator("SEARCH_INPUT", new By.ByXPath("//input[@id='sb-text-input']"));
    private static final Locator SUCCESS_IMAGE_UPLOAD_SNACKBAR = new Locator("SUCCESS_IMAGE_UPLOAD_SNACKBAR", new By.ByXPath("//div[contains(@class,'ant-notification-notice-description') and contains(text(),'Successfully image upload')]"));
    private static final Locator OPERATOR_NAME_INPUT = new Locator("OPERATOR_NAME_INPUT", new By.ByXPath("//input[@id='operator-name']"));
    private static final Locator OPERATOR_LOGO_INPUT = new Locator("OPERATOR_LOGO_INPUT", new By.ByXPath("//span[contains(text(),'Upload Operator Logo')]/following-sibling::input[@type='file']"));
    private static final Locator ALLOW_REFUND_BUTTON = new Locator("ALLOW_REFUND_BUTTON", new By.ByXPath("//p[contains(text(),'Allow Refund?')]/following-sibling::button[@type='button' and @aria-checked='false']"));
    private static final Locator ALLOW_RESCHEDULE_BUTTON = new Locator("ALLOW_RESCHEDULE_BUTTON", new By.ByXPath("//p[contains(text(),'Allow Reschedule?')]/following-sibling::button[@type='button' and @aria-checked='false']"));
    private static final Locator TERMS_AND_CONDITIONS_BUTTON = new Locator("TERMS_AND_CONDITIONS_BUTTON", new By.ByXPath("//span[contains(text(), 'Terms & Conditions')]"));
    private static final Locator REFUND_AND_RESCHEDULE_BUTTON = new Locator("REFUND_AND_RESCHEDULE_BUTTON", new By.ByXPath("//span[contains(text(), 'Refund & Reschedule')]"));
    private static final Locator USE_DEFAULT_TEMPLATE_TOGGLE_BUTTON = new Locator("USE_DEFAULT_TEMPLATE_TOGGLE_BUTTON", new By.ByXPath("//p[contains(text(),'Use Default Template?')]/following-sibling::button[@type='button' and @aria-checked='false']"));
    private static final File operatorLogo = new File("src/test/resources/nft/image.png");
    private static final Locator BUS_ROUTE_MANAGEMENT_MENU = new Locator("BUS_ROUTE_MANAGEMENT_MENU", new By.ByXPath("//span[@class='ant-menu-title-content'][contains(text(),'Route Management')]"));
    private static final Locator ADD_ROUTE_BUTTON = new Locator("ADD_ROUTE_BUTTON", By.xpath("//a[contains(text(), 'Add Route')]"));
    private static final Locator SELECT_ORIGIN_DROPDOWN = new Locator("SELECT_ORIGIN_DROPDOWN", new By.ByXPath("//p[contains(text(), 'Select Origin')]"));
    private static final Locator SELECT_DESTINATION_DROPDOWN = new Locator("SELECT_DESTINATION_DROPDOWN", new By.ByXPath("//p[contains(text(), 'Select Destination')]"));
    private static final Locator SEARCH_ORIGIN_INPUT = new Locator("SEARCH_ORIGIN_INPUT", new By.ByXPath("//input[@name='origin']"));
    private static final Locator SEARCH_ORIGIN_BUTTON = new Locator("SEARCH_ORIGIN_BUTTON", new By.ByXPath("//div[input[@placeholder='Find Origin Name']]/following-sibling::button[contains(text(), 'Search')]"));
    private static final Locator SEARCH_DESTINATION_INPUT = new Locator("SEARCH_DESTINATION_INPUT", new By.ByXPath("//input[@name='destination']"));
    private static final Locator SEARCH_DESTINATION_BUTTON = new Locator("SEARCH_DESTINATION_BUTTON", new By.ByXPath("//div[input[@placeholder='Find Destination Name']]/following-sibling::button[contains(text(), 'Search')]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("//a[@class='action-item' and contains(text(),'Delete')]"));
    private static final Locator ADD_ANOTHER_ROUTE_BUTTON = new Locator("ADD_ANOTHER_ROUTE_BUTTON", By.xpath("//button[contains(text(), 'Add Another Route')]"));
    private static final Locator ADD_LOCATION_BUTTON = new Locator("ADD_LOCATION_BUTTON", By.xpath("//a[contains(text(), 'Add Location')]"));
    private static final Locator NAME_INPUT = new Locator("NAME_INPUT", By.xpath("//input[@id='name']"));
    private static final Locator LOCATION_TYPE_DROPDOWN_INPUT = new Locator("LOCATION_TYPE_DROPDOWN_INPUT", By.xpath("//span[contains(text(),'Type')]"));
    private static final Locator LATITUDE_INPUT = new Locator("LATITUDE_INPUT", By.xpath("//input[@id='latitude']"));
    private static final Locator LONGITUDE_INPUT = new Locator("LONGITUDE_INPUT", By.xpath("//input[@id='longitude']"));
    private static final Locator CITY_INPUT = new Locator("CITY_INPUT", By.xpath("//input[@id='city']"));
    private static final Locator LOCATION_ADDRESS_INPUT = new Locator("LOCATION_ADDRESS_INPUT", By.xpath("//input[@id='location-address']"));
    private static final Locator BUS_LOCATION_SEARCH_INPUT = new Locator("BUS_LOCATION_SEARCH_INPUT", By.xpath("//input[@id='sb-text-input']"));

    public BusPage(Session<T> session) {
        super(session);
    }
    public void goToBusPage(String username, String password) throws InterruptedException {
        driverTools.maximizeWindow();

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/member");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/member");
        }

        if (!isBusLoggedIn()) {
            BusLoginPage<?> busLoginPage = new BusLoginPage<>(session);
            busLoginPage.loginToBusPage(username, password);
            driverTools.waitForElementToAppear(USERNAME_GREETING, 2);

            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get("https://gatotkaca.tiket.com/admin/bus");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driverTools.get("https://preprod.tiket.com/admin/bus");
            }
        }
    }

    public void openBusPage(){
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/bus");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/bus");
        }
    }

    public void clickToggleActiveInactive() {
        driverTools.waitForElementToDisappear(LOADING_STATE);
        busLocationViewToggleStatus();

        WebElement toggle =  driverTools.waitForElementToAppear(TOGGLE_ACTIVE_OR_INACTIVE_STATUS).orElseThrow();
        driverTools.waitForElementToBeClickable(toggle);
        driverTools.clickWithJavascriptExecutor(toggle);
        WebElement agreeButton = driverTools.waitForElementToAppear(AGREE_BUTTON).orElseThrow();
        driverTools.waitForElementToBeClickable(agreeButton);
        driverTools.clickWithJavascriptExecutor(agreeButton);
        driverTools.waitForElementToAppear(BUS_LOCATION_SUCCESS_SNACKBAR);
    }

    public boolean isBusLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 2);
        return (driverTools.isElementDisplayed(USERNAME_GREETING));
    }

    public void locationBusView(){
        driverTools.waitForElementToAppear(BUS_FIRST_ITEM);
    }

    public boolean verifyLocationBusView(){
        locationBusView();
        return driverTools.isElementDisplayed(BUS_FIRST_ITEM);
    }

    public void locationBusUpdate(){
        locationBusView();
        driverTools.click(BUS_LOCATION_FIRST_ITEM_OF_EDIT_BUTTON);
        driverTools.click(BUS_LOCATION_SAVE_BUTTON_TO_UPDATE);
    }

    public boolean verifyLocationBusUpdate(){
        locationBusUpdate();
        return driverTools.isElementDisplayed(BUS_LOCATION_SUCCESS_SNACKBAR);
    }

    public void locationBusSearch(String search) {
        driverTools.sendKeys(BUS_LOCATION_SEARCH_TEXT_INPUT, search);
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean verifyLocationBusSearch(){
        driverTools.waitForElementToAppear(BUS_FIRST_ITEM);
        return driverTools.isElementDisplayed(BUS_FIRST_ITEM);
    }

    public void locationPointBusView(){
        driverTools.click(BUS_LOCATION_POINT_MENU);
        driverTools.waitForElementToAppear(BUS_FIRST_ITEM);
    }

    public boolean verifyLocationPointBusView(){
        return driverTools.isElementDisplayed(BUS_FIRST_ITEM);
    }

    public void locationPointBusActivateInactivate(){
        driverTools.clickWithJavascriptExecutor(BUS_LOCATION_POINT_MENU);
        clickToggleActiveInactive();
    }
    public boolean verifyLocationPointBusActivateInactivate(){
        locationPointBusActivateInactivate();
        return  driverTools.isElementDisplayed(BUS_LOCATION_POINT_SUCCESS_ACTIVATION_SNACKBAR) || driverTools.isElementDisplayed(BUS_LOCATION_POINT_SUCCESS_INACTIVATION_SNACKBAR);
    }

    public void locationPointBusUpdate(){
        locationPointBusView();
        driverTools.click(BUS_LOCATION_FIRST_ITEM_OF_EDIT_BUTTON);
        driverTools.waitForElementToDisappear(LOADING_STATE);
        driverTools.waitForElementToBeClickable(BUS_LOCATION_SAVE_BUTTON_TO_UPDATE).orElseThrow();
        driverTools.click(BUS_LOCATION_SAVE_BUTTON_TO_UPDATE);
    }

    public boolean verifyLocationPointBusUpdate(){
        locationPointBusUpdate();
        return driverTools.isElementDisplayed(BUS_LOCATION_UPDATE_SNACKBAR);
    }

    public void locationPointBusSearch(String locationPoint){
        locationPointBusView();
        driverTools.sendKeys(BUS_LOCATION_POINT_SEARCH_TEXT_INPUT, locationPoint);
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean verifyLocationPointBusSearch(){
        driverTools.waitForElementToDisappear(LOADING_STATE);
        driverTools.waitForElementToAppear(BUS_FIRST_ITEM);
        return driverTools.waitForElementToAppear(BUS_FIRST_ITEM).isPresent();
    }

    public void operatorBusView(){
        driverTools.click(BUS_OPERATOR_MENU);
        driverTools.waitForElementToAppear(BUS_OPERATOR_ITEM);
    }

    public boolean verifyOperatorBusView(){
        return driverTools.isElementDisplayed(BUS_OPERATOR_ITEM);
    }

    public void operatorBusUpdate(){
        operatorBusView();
        driverTools.click(BUS_OPERATOR_EDIT_BUTTON);
        driverTools.waitForElementToDisappear(LOADING_STATE);
        driverTools.waitForElementToBeClickable(BUS_SAVE_BUTTON).orElseThrow();
        driverTools.click(BUS_SAVE_BUTTON);
    }

    public boolean verifyOperatorBusUpdate(){
        operatorBusUpdate();
        return driverTools.isElementDisplayed(BUS_SUCCESS_SNACKBAR);
    }

    public void operatorBusClickToggleToActivateOrInactivateStatus(){
        WebElement toggle =  driverTools.waitForElementToAppear(BUS_OPERATOR_TOGGLE_INACTIVE_OR_ACTIVE_STATUS).orElseThrow();
        driverTools.waitForElementToBeClickable(toggle);
        driverTools.clickWithJavascriptExecutor(toggle);
        WebElement agreeButton = driverTools.waitForElementToAppear(AGREE_BUTTON).orElseThrow();
        driverTools.waitForElementToBeClickable(agreeButton);
        driverTools.clickWithJavascriptExecutor(agreeButton);
        driverTools.waitForElementToAppear(BUS_OPERATOR_SUCCESS_ACTIVATION_OR_INACTIVATION_SNACKBAR);
    }

    public void operatorBusClickToggleToActivateOrInactivateReschedule(){
        WebElement toggle =  driverTools.waitForElementToAppear(BUS_OPERATOR_TOGGLE_INACTIVE_OR_ACTIVE_RESCHEDULE).orElseThrow();
        driverTools.waitForElementToBeClickable(toggle);
        driverTools.clickWithJavascriptExecutor(toggle);
        WebElement agreeButton = driverTools.waitForElementToAppear(AGREE_BUTTON).orElseThrow();
        driverTools.waitForElementToBeClickable(agreeButton);
        driverTools.clickWithJavascriptExecutor(agreeButton);
        driverTools.waitForElementToAppear(BUS_OPERATOR_SUCCESS_ACTIVATION_OR_INACTIVATION_SNACKBAR);
    }

    public void operatorBusClickToggleToActivateOrInactivateRefund(){
        WebElement toggle =  driverTools.waitForElementToAppear(BUS_OPERATOR_TOGGLE_INACTIVE_OR_ACTIVE_REFUND).orElseThrow();
        driverTools.waitForElementToBeClickable(toggle);
        driverTools.clickWithJavascriptExecutor(toggle);
        WebElement agreeButton = driverTools.waitForElementToAppear(AGREE_BUTTON).orElseThrow();
        driverTools.waitForElementToBeClickable(agreeButton);
        driverTools.clickWithJavascriptExecutor(agreeButton);
        driverTools.waitForElementToAppear(BUS_OPERATOR_SUCCESS_ACTIVATION_OR_INACTIVATION_SNACKBAR);
    }

    public void operatorBusClickToggleActiveInactiveStatus(){
        operatorBusView();
        driverTools.waitForElementToDisappear(LOADING_STATE);
        busOperatorViewToggleStatus();
        driverTools.isElementDisplayed(BUS_OPERATOR_TOGGLE_INACTIVE_OR_ACTIVE_STATUS);
        operatorBusClickToggleToActivateOrInactivateStatus();
    }

    public void operatorBusClickToggleActiveInactiveReschedule(){
        operatorBusView();
        driverTools.waitForElementToDisappear(LOADING_STATE);
        busOperatorViewToggleStatus();
        driverTools.isElementDisplayed(BUS_OPERATOR_TOGGLE_INACTIVE_OR_ACTIVE_RESCHEDULE);
        operatorBusClickToggleToActivateOrInactivateReschedule();
    }

    public void operatorBusClickToggleActiveInactiveRefund(){
        operatorBusView();
        driverTools.waitForElementToDisappear(LOADING_STATE);
        busOperatorViewToggleStatus();
        driverTools.isElementDisplayed(BUS_OPERATOR_TOGGLE_INACTIVE_OR_ACTIVE_REFUND);
        operatorBusClickToggleToActivateOrInactivateRefund();
    }

    public void busOperatorViewToggleStatus(){
        driverTools.click(BUS_TABLE);
        Actions action = new Actions(driver);
        int maxAttempts = 30;
        int attempt = 0;

        while (attempt < maxAttempts) {
            action.sendKeys(Keys.ARROW_RIGHT);
            action.perform();
            attempt++;
            if (driverTools.isElementDisplayed(BUS_OPERATOR_TOGGLE_SWITCH_STATUS_FINDER)){
                return;
            }
        }

        if (!driverTools.isElementDisplayed(BUS_OPERATOR_TOGGLE_INACTIVE_OR_ACTIVE_STATUS)){
            throw new NoSuchElementException("The target element was not found after swiping.");
        }
    }

    public boolean verifyOperatorBusActivateOrInactivateStatus(){
        operatorBusClickToggleActiveInactiveStatus();
        if (driverTools.isElementDisplayed(BUS_OPERATOR_SUCCESS_ACTIVATION_OR_INACTIVATION_SNACKBAR)) {
            driverTools.waitForElementToDisappear(BUS_OPERATOR_SUCCESS_ACTIVATION_OR_INACTIVATION_SNACKBAR);
            return true;
        }
        return false;
    }

    public boolean verifyOperatorBusActivateOrInactivateReschedule(){
        operatorBusClickToggleActiveInactiveReschedule();
        if (driverTools.isElementDisplayed(BUS_OPERATOR_SUCCESS_ACTIVATION_OR_INACTIVATION_SNACKBAR)) {
            driverTools.waitForElementToDisappear(BUS_OPERATOR_SUCCESS_ACTIVATION_OR_INACTIVATION_SNACKBAR);
            return true;
        }
        return false;
    }

    public boolean verifyOperatorBusActivateOrInactivateRefund(){
        operatorBusClickToggleActiveInactiveRefund();
        if (driverTools.isElementDisplayed(BUS_OPERATOR_SUCCESS_ACTIVATION_OR_INACTIVATION_SNACKBAR)) {
            driverTools.waitForElementToDisappear(BUS_OPERATOR_SUCCESS_ACTIVATION_OR_INACTIVATION_SNACKBAR);
            return true;
        }
        return false;
    }

    public void vendorOperatorBusView(){
        driverTools.click(DATA_MAPPING_MENU);
        driverTools.waitForElementToAppear(VENDOR_OPERATOR_MENU);
        driverTools.click(VENDOR_OPERATOR_MENU);
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    public void dataMappingBusSearch(String search) {
        performSearchWithRetry(search, 2);
    }

    private void performSearchWithRetry(String search, int retryCount) {
        for (int i = 0; i < retryCount; i++) {
            try {
                performSearch(search);
                return;
            } catch (org.openqa.selenium.StaleElementReferenceException ex) {
                if (i == retryCount - 1) {
                    throw ex; // Rethrow the exception if all retries fail
                }
            }
        }
    }

    private void performSearch(String search) {
        driverTools.waitForElementToAppear(DATA_MAPPING_SEARCH_INPUT);

        driverTools.click(DATA_MAPPING_SEARCH_INPUT);
        driverTools.clearWithJsExecutor(DATA_MAPPING_SEARCH_INPUT);

        driverTools.sendKeys(DATA_MAPPING_SEARCH_INPUT, search);
        driverTools.click(DATA_MAPPING_SEARCH_BUTTON);

    }

    public boolean verifyDataMappingBusSearch(String search) {
        Locator SEARCHED_ITEM = generateDynamicLocator(search, "//span[contains(text(),'%s')]");
        driverTools.waitForElementToAppear(SEARCHED_ITEM);
        return driverTools.isElementDisplayed(SEARCHED_ITEM);
    }

    public void busLocationViewToggleStatus(){
        driverTools.clickWithJavascriptExecutor(BUS_TABLE);
        Actions action = new Actions(driver);
        int maxAttempts = 30;
        int attempt = 0;

        while (attempt < maxAttempts) {
            action.sendKeys(Keys.ARROW_RIGHT);
            action.perform();
            attempt++;
            if (driverTools.isElementDisplayed(BUS_OPERATOR_TOGGLE_SWITCH_STATUS_FINDER)){
                return;
            }
        }

        if (!driverTools.isElementDisplayed(TOGGLE_ACTIVE_OR_INACTIVE_STATUS)){
            throw new NoSuchElementException("The target element was not found after swiping.");
        }
    }

    public boolean verifyLocationBusActivateOrInactivateStatus(){
        clickToggleActiveInactive();
        if (driverTools.isElementDisplayed(BUS_LOCATION_SUCCESS_SNACKBAR)) {
            driverTools.waitForElementToDisappear(BUS_LOCATION_SUCCESS_SNACKBAR);
            return true;
        }
        return false;
    }

    public void vehicleBusView(){
        driverTools.click(BUS_VEHICLE_MENU);
        driverTools.waitForElementToAppear(BUS_FIRST_ITEM);
    }

    public void searchVehicle(String vehicle){
        driverTools.click(BUS_VEHICLE_SEARCH_INPUT);
        driverTools.clear(BUS_VEHICLE_SEARCH_INPUT);
        driverTools.sendKeys(BUS_VEHICLE_SEARCH_INPUT, vehicle);
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean verifyVehicleBusView(){
        return driverTools.isElementDisplayed(BUS_FIRST_ITEM);
    }

    public boolean verifyVehicleBusActivateInactivate(){
        vehicleBusView();
        driverTools.waitForElementToDisappear(LOADING_STATE);
        busVehicleViewToggleStatus();
        driverTools.isElementDisplayed(BUS_VEHICLE_TOGGLE_ACTIVE_OR_INACTIVE_STATUS);
        vehicleBusClickToggleToActivateOrInactivateStatus();
        if (driverTools.isElementDisplayed(SUCCESS_SNACKBAR)) {
            driverTools.waitForElementToDisappear(SUCCESS_SNACKBAR);
            return true;
        }
        return false;
    }

    public void busVehicleViewToggleStatus(){
        driverTools.scrollIntoWebView(BUS_TABLE);
        driverTools.click(BUS_TABLE);
        Actions action = new Actions(driver);
        int maxAttempts = 30;
        int attempt = 0;

        while (attempt < maxAttempts) {
            action.sendKeys(Keys.ARROW_RIGHT);
            action.perform();
            attempt++;
            if (driverTools.isElementDisplayed(BUS_VEHICLE_TOGGLE_SWITCH_STATUS_FINDER)){
                return;
            }
        }

        if (!driverTools.isElementDisplayed(BUS_VEHICLE_TOGGLE_ACTIVE_OR_INACTIVE_STATUS)){
            throw new NoSuchElementException("The target element was not found after swiping.");
        }
    }

    public void vehicleBusClickToggleToActivateOrInactivateStatus(){
        WebElement toggle =  driverTools.waitForElementToAppear(BUS_VEHICLE_TOGGLE_ACTIVE_OR_INACTIVE_STATUS).orElseThrow();
        driverTools.waitForElementToBeClickable(toggle);
        driverTools.click(toggle);
        WebElement agreeButton = driverTools.waitForElementToAppear(AGREE_BUTTON).orElseThrow();
        driverTools.waitForElementToBeClickable(agreeButton);
        driverTools.click(agreeButton);
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR);
    }

    public void vehicleBusUpdate(String newIDDescription, String newENDescription){
        File vehicleImage = new File ("src/test/resources/nft/image.png");

        driverTools.click(BUS_EDIT_BUTTON);
        driverTools.waitForElementToDisappear(LOADING_STATE);
        driverTools.sendKeys(BUS_VEHICLE_DESCRIPTION_ID_INPUT, newIDDescription);
        driverTools.sendKeys(BUS_VEHICLE_DESCRIPTION_EN_INPUT, newENDescription);

        driverTools.scrollIntoWebView(BUS_VEHICLE_UPLOAD_IMAGE);
        driverTools.sendKeys(BUS_VEHICLE_UPLOAD_IMAGE, vehicleImage.getAbsolutePath());
        driverTools.waitForElementToDisappear(IMAGE_UPLOAD_SUCCESSFUL_SNACKBAR);

        driverTools.scrollIntoWebView(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    public boolean verifySuccessSnackbarIsDisplayed() {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 2);
        return driverTools.isElementDisplayed(SUCCESS_SNACKBAR);
    }

    public void createVehicle(String label, String searchType,
                              String classNameID, String classNameEN){
        File vehicleImage = new File ("src/test/resources/nft/image.png");

        driverTools.clickWithJavascriptExecutor(CREATE_VEHICLE_BUTTON);
        driverTools.waitForElementToDisappear(LOADING_STATE);
        driverTools.waitForElementToAppear(LABEL_INPUT).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.sendKeys(LABEL_INPUT, label);
        driverTools.clickWithJavascriptExecutor(CARI_TYPE_CLICK_INPUT);
        driverTools.sendKeys(CARI_TYPE_SEARCH_INPUT, searchType);
        driverTools.clickWithJavascriptExecutor(SELECT_BUS_INPUT);
        driverTools.sendKeys(CLASS_NAME_ID_INPUT, classNameID);
        driverTools.sendKeys(CLASS_NAME_EN_INPUT, classNameEN);

        driverTools.scrollIntoWebView(BUS_VEHICLE_UPLOAD_IMAGE);
        driverTools.sendKeys(BUS_VEHICLE_UPLOAD_IMAGE, vehicleImage.getAbsolutePath());
        driverTools.waitForElementToAppear(IMAGE_UPLOAD_SUCCESSFUL_SNACKBAR);
        driverTools.waitForElementToDisappear(IMAGE_UPLOAD_SUCCESSFUL_SNACKBAR);

        driverTools.scrollIntoWebView(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void deleteVehicle() {
        driverTools.click(DELETE_VEHICLE_BUTTON);
        driverTools.waitForElementToAppear(AGREE_BUTTON);
        driverTools.click(AGREE_BUTTON);
    }

    public void operatorBusCreate(String operatorName){
        driverTools.clickWithJavascriptExecutor(ADD_OPERATOR_BUTTON);
        driverTools.waitForElementToDisappear(LOADING_STATE);

        driverTools.sendKeys(OPERATOR_NAME_INPUT, operatorName);
        driverTools.sendKeys(OPERATOR_LOGO_INPUT, operatorLogo.getAbsolutePath());
        driverTools.waitForElementToAppear(SUCCESS_IMAGE_UPLOAD_SNACKBAR);
        driverTools.waitForElementToDisappear(SUCCESS_IMAGE_UPLOAD_SNACKBAR);

        driverTools.clickWithJavascriptExecutor(ALLOW_REFUND_BUTTON);
        driverTools.clickWithJavascriptExecutor(ALLOW_RESCHEDULE_BUTTON);

        driverTools.clickWithJavascriptExecutor(TERMS_AND_CONDITIONS_BUTTON);
        driverTools.scrollIntoWebView(USE_DEFAULT_TEMPLATE_TOGGLE_BUTTON);
        driverTools.clickWithJavascriptExecutor(USE_DEFAULT_TEMPLATE_TOGGLE_BUTTON);
        driverTools.waitForElementToDisappear(LOADING_STATE);

        driverTools.clickWithJavascriptExecutor(REFUND_AND_RESCHEDULE_BUTTON);
        driverTools.waitForElementToAppear(USE_DEFAULT_TEMPLATE_TOGGLE_BUTTON);
        driverTools.scrollIntoWebView(USE_DEFAULT_TEMPLATE_TOGGLE_BUTTON);
        driverTools.clickWithJavascriptExecutor(USE_DEFAULT_TEMPLATE_TOGGLE_BUTTON);
        driverTools.waitForElementToDisappear(LOADING_STATE);

        driverTools.scrollIntoWebView(BUS_SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(BUS_SAVE_BUTTON);
    }

    public boolean verifyOperatorBusCreate(){
        driverTools.waitForElementToAppear(BUS_SUCCESS_SNACKBAR);
        return driverTools.isElementDisplayed(BUS_SUCCESS_SNACKBAR);
    }

    public void operatorBusSearch(String search){
        driverTools.waitForElementToAppear(SEARCH_INPUT);

        driverTools.click(SEARCH_INPUT);
        driverTools.clear(SEARCH_INPUT);

        driverTools.sendKeys(SEARCH_INPUT, search);
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean verifyOperatorBusSearch(String search) {
        Locator SEARCHED_ITEM = new Locator("SEARCHED_ITEM", new By.ByXPath("//td[contains(text(), '" + search + "')]"));
        driverTools.waitForElementToAppear(SEARCHED_ITEM);
        return driverTools.isElementDisplayed(SEARCHED_ITEM);
    }

    public boolean verifyDataMappingBusMap(){
        driverTools.waitForElementToAppear(SUCCESSFULLY_MAPPED_SNACKBAR);
        return driverTools.isElementDisplayed(SUCCESSFULLY_MAPPED_SNACKBAR);
    }

    public void dataMappingBusUnmap(){
        driverTools.click(UNMAP_BUTTON);
        driverTools.waitForElementToAppear(AGREE_TO_UNMAP_BUTTON);
        driverTools.click(AGREE_TO_UNMAP_BUTTON);
    }

    public boolean verifyDataMappingBusUnmap(){
        driverTools.waitForElementToAppear(SUCCESSFULLY_UNMAPPED_SNACKBAR);
        return driverTools.isElementDisplayed(SUCCESSFULLY_UNMAPPED_SNACKBAR);
    }

    public void dataMappingBusNextPage(Integer nextPageCount){
        for (int i=0; i<nextPageCount; i++){
            driverTools.waitForElementToAppear(FOOTER);
            driverTools.scrollIntoWebView(FOOTER);
            driverTools.waitForElementToAppear(NEXT_PAGE_BUTTON, 2);
            driverTools.waitForElementToBeClickable(NEXT_PAGE_BUTTON);
            driverTools.click(NEXT_PAGE_BUTTON);
        }
    }

    public boolean verifyDataMappingBusNextPage(Integer nextPageCount){
        int startItem = (nextPageCount) * 15 + 1;
        int endItem = (nextPageCount + 1) * 15;

        Locator TOTAL_ITEMS = generateDynamicLocator("Showing " + startItem + "-" + endItem, "//li[@class='ant-pagination-total-text' and contains(text(),'%s')]");

        int startItemBuffer = (nextPageCount + 1) * 15 + 1;
        int endItemBuffer = (nextPageCount + 2) * 15;
        Locator TOTAL_ITEMS_BUFFER = generateDynamicLocator("Showing " + startItemBuffer + "-" + endItemBuffer, "//li[@class='ant-pagination-total-text' and contains(text(),'%s')]");

        driverTools.scrollIntoWebView(FOOTER);
        return driverTools.isElementDisplayed(TOTAL_ITEMS) || driverTools.isElementDisplayed(TOTAL_ITEMS_BUFFER);
    }

    public void dataMappingBusPreviousPage(Integer previousPageCount){
        for (int i=0; i<previousPageCount; i++){
            driverTools.waitForElementToAppear(FOOTER);
            driverTools.scrollIntoWebView(FOOTER);
            driverTools.waitForElementToAppear(PREVIOUS_PAGE_BUTTON, 2);
            driverTools.waitForElementToBeClickable(PREVIOUS_PAGE_BUTTON);
            driverTools.click(PREVIOUS_PAGE_BUTTON);
        }
    }

    public boolean verifyDataMappingBusPreviousPage(Integer nextPageCount, Integer previousPageCount){
        int startItem = (nextPageCount - previousPageCount) * 15 + 1;
        int endItem = (nextPageCount - previousPageCount + 1) * 15;

        Locator TOTAL_ITEMS = generateDynamicLocator("Showing " + startItem + "-" + endItem, "//li[@class='ant-pagination-total-text' and contains(text(),'%s')]");

        int startItemBuffer = (nextPageCount - previousPageCount + 1) * 15 + 1;
        int endItemBuffer = (nextPageCount - previousPageCount + 2) * 15;
        Locator TOTAL_ITEMS_BUFFER = generateDynamicLocator("Showing " + startItemBuffer + "-" + endItemBuffer, "//li[@class='ant-pagination-total-text' and contains(text(),'%s')]");

        driverTools.scrollIntoWebView(FOOTER);
        return driverTools.isElementDisplayed(TOTAL_ITEMS) || driverTools.isElementDisplayed(TOTAL_ITEMS_BUFFER);
    }

    public void dataMappingBusDeleteMapping(Integer addAnotherMappingCount, Integer deleteMappingCount) {
        driverTools.click(MAP_BUTTON);
        for (int i=0; i < addAnotherMappingCount; i++){
            driverTools.scrollIntoWebView(ADD_ANOTHER_MAPPING_BUTTON);
            driverTools.click(ADD_ANOTHER_MAPPING_BUTTON);
        }

        for (int i = 0; i < deleteMappingCount; i++) {
            driverTools.scrollIntoWebView(DELETE_ICON);
            driverTools.click(DELETE_ICON);
        }
    }

    public boolean verifyDataMappingBusDeleteMapping(){
        return driverTools.isElementDisplayed(TIKETS_DATA) && driverTools.isElementDisplayed(VENDORS_DATA);
    }

    public void vendorLocationBusView(){
        driverTools.click(DATA_MAPPING_MENU);
        driverTools.waitForElementToAppear(VENDOR_LOCATION_MENU);
        driverTools.click(VENDOR_LOCATION_MENU);
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    public void vendorOperatorBusMap(String searchTiketsVehicle, String searchVendorsVendor,
                                     String searchVendorsOperator, String searchVendorsVehicle) {
        driverTools.click(MAP_BUTTON);
        mapSearchCriteria(searchTiketsVehicle, SEARCH_TIKETS_VEHICLE_DROPDOWN);
        mapSearchCriteria(searchVendorsVendor, SEARCH_VENDORS_VENDOR_DROPDOWN);
        mapSearchCriteria(searchVendorsOperator, SEARCH_VENDORS_OPERATOR_DROPDOWN);
        mapSearchCriteria(searchVendorsVehicle, SEARCH_VENDORS_VEHICLE_DROPDOWN);
        driverTools.click(MAP_AND_SAVE_BUTTON);
    }

    public void vendorLocationBusMap(String searchTiketsLocation, String searchVendorsVendor,
                                     String searchVendorsLocation) {
        driverTools.click(MAP_BUTTON);
        mapSearchCriteria(searchTiketsLocation, SEARCH_TIKETS_LOCATION_DROPDOWN);
        mapSearchCriteria(searchVendorsVendor, SEARCH_VENDORS_VENDOR_DROPDOWN);
        mapSearchCriteria(searchVendorsLocation, SEARCH_VENDORS_LOCATION_DROPDOWN);
        driverTools.click(MAP_AND_SAVE_BUTTON);
    }

    private void mapSearchCriteria(String searchCriteria, Locator dropdownLocator) {
        Locator selectLocator = generateDynamicLocator(searchCriteria, "//div[contains(text(),'%s')]");

        driverTools.waitForElementToAppear(dropdownLocator);
        driverTools.click(dropdownLocator);
        driverTools.sendKeys(SEARCH_TO_SELECT_INPUT, searchCriteria);
        driverTools.click(selectLocator);
    }

    public void vendorLocationBusSearchDuringMap(String searchTiketsLocation, String searchVendorsVendor, String searchVendorsLocation){
        driverTools.click(MAP_BUTTON);
        mapSearchCriteria(searchTiketsLocation, SEARCH_TIKETS_LOCATION_DROPDOWN);
        mapSearchCriteria(searchVendorsVendor, SEARCH_VENDORS_VENDOR_DROPDOWN);
        mapSearchCriteria(searchVendorsLocation, SEARCH_VENDORS_LOCATION_DROPDOWN);
    }

    public boolean verifyVendorLocationBusSearchDuringMap(String searchTiketsLocation, String searchVendorsVendor, String searchVendorsLocation){
        Locator SELECTED_TIKETS_LOCATION_INPUT = generateDynamicLocator(searchTiketsLocation, "//span[contains(text(),'%s')]");
        Locator SELECTED_VENDORS_VENDOR_INPUT = generateDynamicLocator(searchVendorsVendor, "//span[contains(text(),'%s')]");
        Locator SELECTED_VENDORS_LOCATION_INPUT = generateDynamicLocator(searchVendorsLocation, "//span[contains(text(),'%s')]");

        return driverTools.isElementDisplayed(SELECTED_TIKETS_LOCATION_INPUT) &&
                driverTools.isElementDisplayed(SELECTED_VENDORS_VENDOR_INPUT) &&
                driverTools.isElementDisplayed(SELECTED_VENDORS_LOCATION_INPUT);
    }

    public void routeManagementBusView(){
        driverTools.click(BUS_ROUTE_MANAGEMENT_MENU);
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    public void routeManagementBusSearchOrigin(String origin){
        driverTools.click(SEARCH_ORIGIN_INPUT);
        driverTools.clearWithJsExecutor(SEARCH_ORIGIN_INPUT);
        driverTools.sendKeys(SEARCH_ORIGIN_INPUT, origin);
        driverTools.click(SEARCH_ORIGIN_BUTTON);
    }

    public void routeManagementBusSearchDestination(String destination){
        driverTools.click(SEARCH_DESTINATION_INPUT);
        driverTools.clearWithJsExecutor(SEARCH_DESTINATION_INPUT);
        driverTools.sendKeys(SEARCH_DESTINATION_INPUT, destination);
        driverTools.click(SEARCH_DESTINATION_BUTTON);
    }

    public void routeManagementBusDelete(){
        driverTools.waitForElementToAppear(DELETE_BUTTON);
        driverTools.click(DELETE_BUTTON);

        driverTools.waitForElementToAppear(AGREE_BUTTON);
        driverTools.click(AGREE_BUTTON);
    }

    public boolean verifySearch() {
        return driverTools.isElementDisplayed(FIRST_ITEM_AFTER_SEARCHED);
    }

    public void facilitiesBusView(){
        Locator FACILITIES_MENU = new Locator("FACILITIES_MENU", new By.ByXPath("//span[@class='ant-menu-title-content'][contains(text(),'Facilities')]"));
        driverTools.click(FACILITIES_MENU);
        driverTools.waitForElementToAppear(BUS_FIRST_ITEM);
    }

    public boolean verifyFacilitiesBusView(){
        return driverTools.isElementDisplayed(BUS_FIRST_ITEM);
    }

    public void createFacility(String label, Integer priorityScoring, String facilityNameID, String facilityNameEN){
        File facilityIcon = new File ("src/test/resources/nft/image.png");

        driverTools.click(CREATE_FACILITY_BUTTON);
        driverTools.waitForElementToDisappear(LOADING_STATE);

        driverTools.sendKeys(LABEL_INPUT, label);
        driverTools.sendKeys(PRIORITY_SCORING_INPUT, String.valueOf(priorityScoring));
        driverTools.sendKeys(FACILITY_NAME_ID_INPUT, facilityNameID);
        driverTools.sendKeys(FACILITY_NAME_EN_INPUT, facilityNameEN);
        driverTools.sendKeys(UPLOAD_FACILITY_ICON_DEFAULT, facilityIcon.getAbsolutePath());
        driverTools.waitForElementToAppear(IMAGE_UPLOAD_SUCCESSFUL_SNACKBAR);
        driverTools.waitForElementToDisappear(IMAGE_UPLOAD_SUCCESSFUL_SNACKBAR);
        driverTools.sendKeys(UPLOAD_FACILITY_ICON_SELECTED, facilityIcon.getAbsolutePath());
        driverTools.waitForElementToAppear(IMAGE_UPLOAD_SUCCESSFUL_SNACKBAR);
        driverTools.waitForElementToDisappear(IMAGE_UPLOAD_SUCCESSFUL_SNACKBAR);

        driverTools.scrollIntoWebView(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    public void searchFacility(String facility){
        driverTools.clear(BUS_FACILITIES_SEARCH_INPUT);
        driverTools.sendKeys(BUS_FACILITIES_SEARCH_INPUT, facility);
        driverTools.click(SEARCH_BUTTON);
        driverTools.clear(BUS_FACILITIES_SEARCH_INPUT);
    }

    public void activateOrInactivateFacility() {
        busVehicleViewToggleStatus();

        clickElementWhenClickable(BUS_FACILITIES_TOGGLE_ACTIVE_OR_INACTIVE_STATUS);
        clickElementWhenClickable(AGREE_BUTTON);
        driverTools.waitForElementToAppear(BUS_FACILITIES_SUCCESS_SNACKBAR);
    }

    private void clickElementWhenClickable(Locator elementLocator) {
        driverTools.waitForElementToAppear(elementLocator);
        driverTools.waitForElementToBeClickable(elementLocator);
        driverTools.click(elementLocator);
    }

    public boolean verifySuccessActivationOrInactivationFacility() {
        if (isSuccessFacilitySnackbarDisplayed()) {
            driverTools.waitForElementToDisappear(BUS_FACILITIES_SUCCESS_SNACKBAR);
            return true;
        }
        return false;
    }

    private boolean isSuccessFacilitySnackbarDisplayed() {
        return driverTools.isElementDisplayed(BUS_FACILITIES_SUCCESS_SNACKBAR);
    }

    public void facilityBusUpdate(){
        driverTools.click(BUS_EDIT_BUTTON);
        driverTools.waitForElementToDisappear(LOADING_STATE);
        driverTools.click(SAVE_BUTTON);
    }

    public void deleteFacility(){
        driverTools.click(DELETE_FACILITY_BUTTON);
        driverTools.waitForElementToAppear(AGREE_BUTTON);
        driverTools.click(AGREE_BUTTON);
    }

    public boolean verifyVehicleSearchAfterDeletion(){
        return !driverTools.isElementDisplayed(FIRST_ITEM_AFTER_SEARCHED);
    }

    public boolean verifyFacilitySearchAfterDeletion() {
        return !driverTools.isElementDisplayed(FIRST_ITEM_AFTER_SEARCHED);
    }

    public boolean verifyRouteManagementBusSearch(String search){
        Locator SEARCHED_ITEM = new Locator("SEARCHED_ITEM", new By.ByXPath("//td[contains(text(),'" + search + "')]"));

        driverTools.waitForElementToAppear(SEARCHED_ITEM);
        return driverTools.isElementDisplayed(SEARCHED_ITEM);
    }

    public boolean verifyRouteManagementDelete() {
        driverTools.waitForElementToAppear(BUS_SUCCESS_SNACKBAR);
        return driverTools.isElementDisplayed(BUS_SUCCESS_SNACKBAR);
    }

    public void routeManagementBusAdd(String origin, String destination){
        driverTools.click(ADD_ROUTE_BUTTON);

        mapSearchCriteria(origin, SELECT_ORIGIN_DROPDOWN);
        mapSearchCriteria(destination, SELECT_DESTINATION_DROPDOWN);
        driverTools.click(BUS_SAVE_BUTTON);
    }

    public boolean verifyRouteManagementBusAdd(){
        driverTools.waitForElementToAppear(BUS_SUCCESS_SNACKBAR);
        return driverTools.isElementDisplayed(BUS_SUCCESS_SNACKBAR);
    }

    public void routeManagementBusNextPage(Integer nextPageCount){
        for (int i=0; i<nextPageCount; i++){
            driverTools.waitForElementToAppear(FOOTER);
            driverTools.scrollIntoWebView(FOOTER);
            driverTools.waitForElementToAppear(NEXT_PAGE_BUTTON, 2);
            driverTools.waitForElementToBeClickable(NEXT_PAGE_BUTTON);
            driverTools.click(NEXT_PAGE_BUTTON);
        }
    }

    public boolean verifyRouteManagementBusNextPage(Integer nextPageCount){
        int startItem = (nextPageCount) * 10 + 1;
        int endItem = (nextPageCount + 1) * 10;
        Locator TOTAL_ITEMS = new Locator("TOTAL_ITEMS", By.xpath("//li[@class='ant-pagination-total-text' and contains(text(), 'Showing " + startItem + "-" + endItem + "')]"));

        driverTools.scrollIntoWebView(FOOTER);
        return driverTools.isElementDisplayed(TOTAL_ITEMS);
    }

    public void routeManagementBusPreviousPage(Integer previousPageCount){
        for (int i=0; i<previousPageCount; i++){
            driverTools.scrollIntoWebView(FOOTER);
            driverTools.scrollIntoWebView(PREVIOUS_PAGE_BUTTON);
            driverTools.waitForElementToAppear(PREVIOUS_PAGE_BUTTON, 2);
            driverTools.waitForElementToBeClickable(NEXT_PAGE_BUTTON);
            driverTools.click(PREVIOUS_PAGE_BUTTON);
        }
    }

    public boolean verifyRouteManagementBusPreviousPage(Integer nextPageCount, Integer previousPageCount){
        int startItem = (nextPageCount - previousPageCount) * 10 + 1;
        int endItem = (nextPageCount - previousPageCount + 1) * 10;
        Locator TOTAL_ITEMS = new Locator("TOTAL_ITEMS", By.xpath("//li[@class='ant-pagination-total-text' and contains(text(), 'Showing " + startItem + "-" + endItem + "')]"));

        driverTools.scrollIntoWebView(FOOTER);
        return driverTools.isElementDisplayed(TOTAL_ITEMS);
    }

    public void routeManagementBusSearchDuringAdd(String origin, String destination, Integer addAnotherRouteCount, Integer deleteAddAnotherRouteCount){
        driverTools.click(ADD_ROUTE_BUTTON);

        for (int i=0; i < addAnotherRouteCount; i++){
            driverTools.scrollIntoWebView(ADD_ANOTHER_ROUTE_BUTTON);
            driverTools.click(ADD_ANOTHER_ROUTE_BUTTON);
        }

        for (int i = 0; i < deleteAddAnotherRouteCount; i++) {
            driverTools.scrollIntoWebView(DELETE_ICON);
            driverTools.click(DELETE_ICON);
        }

        mapSearchCriteria(origin, SELECT_ORIGIN_DROPDOWN);
        mapSearchCriteria(destination, SELECT_DESTINATION_DROPDOWN);
    }

    public boolean verifyRouteManagementBusSearchDuringAdd(String origin, String destination){
        Locator SELECTED_ORIGIN_INPUT = new Locator("SELECTED_ORIGIN_INPUT", By.xpath("//span[contains(text(),'" + origin + "')]"));
        Locator SELECTED_DESTINATION_INPUT = new Locator("SELECTED_DESTINATION_INPUT", By.xpath("//span[contains(text(),'" + destination + "')]"));

        return driverTools.isElementDisplayed(SELECTED_ORIGIN_INPUT) && driverTools.isElementDisplayed(SELECTED_DESTINATION_INPUT);
    }

    public void locationBusAdd(String name, String locationType, Integer latitude, Integer longitude, String city, String locationAddress){
        driverTools.click(ADD_LOCATION_BUTTON);

        mapSearchCriteria(locationType, LOCATION_TYPE_DROPDOWN_INPUT);

        driverTools.sendKeys(NAME_INPUT, name);
        driverTools.sendKeys(LATITUDE_INPUT, String.valueOf(latitude));
        driverTools.sendKeys(LONGITUDE_INPUT, String.valueOf(longitude));
        driverTools.sendKeys(CITY_INPUT, city);
        driverTools.sendKeys(LOCATION_ADDRESS_INPUT, locationAddress);

        driverTools.click(BUS_SAVE_BUTTON);
    }

    public boolean verifyLocationBusAdd() {
        driverTools.waitForElementToAppear(BUS_SUCCESS_SNACKBAR);
        return driverTools.isElementDisplayed(BUS_SUCCESS_SNACKBAR);
    }

    private Locator generateDynamicLocator(String searchText, String xpathPattern) {
        return new Locator("DYNAMIC_LOCATOR", By.xpath(String.format(xpathPattern, searchText)));
    }

    public void searchLocation(String location){
        driverTools.clear(BUS_LOCATION_SEARCH_INPUT);
        driverTools.sendKeys(BUS_LOCATION_SEARCH_INPUT, location);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
        driverTools.clear(BUS_LOCATION_SEARCH_INPUT);
    }

    public void deleteLocation(){
        driverTools.clickWithJavascriptExecutor(DELETE_BUTTON);
        driverTools.clickWithJavascriptExecutor(AGREE_BUTTON);
    }

    public void clickToggleInactive(){
        driverTools.clickWithJavascriptExecutor(TOGGLE_ACTIVE_OR_INACTIVE_STATUS);
        driverTools.clickWithJavascriptExecutor(AGREE_BUTTON);
    }

    public void operatorBusDelete(){
        driverTools.clickWithJavascriptExecutor(DELETE_BUTTON);
        driverTools.clickWithJavascriptExecutor(AGREE_BUTTON);
    }

    public void searchOperator(String operator){
        driverTools.clear(SEARCH_INPUT);
        driverTools.sendKeys(SEARCH_INPUT, operator);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
        driverTools.clear(SEARCH_INPUT);
    }

    public boolean verifyBusEmptyList(){
        driverTools.waitForElementToDisappear(BUS_FIRST_ITEM);
        return !driverTools.isElementDisplayed(BUS_FIRST_ITEM);
    }
}