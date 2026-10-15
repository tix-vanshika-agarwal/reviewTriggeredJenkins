package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PropertyGalleryPage <T extends WebDriver> extends BasePage<T> {
    public PropertyGalleryPage(Session<T> session) {
        super(session);
    }

    private static final Locator HOTEL_PHOTOS_TAB = new Locator("HOTEL_PHOTOS_TAB", new By.ByXPath("//span[contains(@class,'Tabs_tab_text') and normalize-space()='Hotel Photos']"));
    private static final Locator SEND_HOTEL_IMAGE_TO_AURORA_BUTTON = new Locator("SEND_HOTEL_IMAGE_TO_AURORA_BUTTON", new By.ByXPath("//button[@data-testid='send-to-aurora' or @data-testid='toggle-trigger-aurora-btn']"));
    private static final Locator SEND_HOTEL_IMAGE_TO_AURORA_MODAL = new Locator("SEND_HOTEL_IMAGE_TO_AURORA_MODAL", new By.ByXPath("//div[@class='the-modal show']//div[@testid='check-box-form-module'] | //section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Publish to Aurora']]"));
    private static final Locator SCENE_DETECTION_MODULE_CHECKBOX = new Locator("SCENE_DETECTION_MODULE_CHECKBOX", new By.ByXPath("(//div[@testid='check-box-form-module'] | //section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Publish to Aurora']])//input[@name='scene']/following-sibling::span"));
    private static final Locator SCENE_DETECTION_MODULE_CHECKBOX_STATE = new Locator("SCENE_DETECTION_MODULE_CHECKBOX_STATE", new By.ByXPath("(//div[@testid='check-box-form-module'] | //section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Publish to Aurora']])//input[@name='scene']"));
    private static final Locator QUALITY_CHECKING_MODULE_CHECKBOX = new Locator("QUALITY_CHECKING_MODULE_CHECKBOX", new By.ByXPath("(//div[@testid='check-box-form-module'] | //section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Publish to Aurora']])//input[@name='quality']/following-sibling::span"));
    private static final Locator QUALITY_CHECKING_MODULE_CHECKBOX_STATE = new Locator("QUALITY_CHECKING_MODULE_CHECKBOX_STATE", new By.ByXPath("(//div[@testid='check-box-form-module'] | //section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Publish to Aurora']])//input[@name='quality']"));
    private static final Locator SIMILARITY_CHECKING_MODULE_CHECKBOX = new Locator("SIMILARITY_CHECKING_MODULE_CHECKBOX", new By.ByXPath("(//div[@testid='check-box-form-module'] | //section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Publish to Aurora']])//input[@name='similar']/following-sibling::span"));
    private static final Locator SIMILARITY_CHECKING_MODULE_CHECKBOX_STATE = new Locator("SIMILARITY_CHECKING_MODULE_CHECKBOX_STATE", new By.ByXPath("(//div[@testid='check-box-form-module'] | //section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Publish to Aurora']])//input[@name='similar']"));
    private static final Locator OBJECT_DETECTION_MODULE_CHECKBOX = new Locator("OBJECT_DETECTION_MODULE_CHECKBOX", new By.ByXPath("(//div[@testid='check-box-form-module'] | //section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Publish to Aurora']])//input[@name='object_detection']/following-sibling::span"));
    private static final Locator OBJECT_DETECTION_MODULE_CHECKBOX_STATE = new Locator("OBJECT_DETECTION_MODULE_CHECKBOX_STATE", new By.ByXPath("(//div[@testid='check-box-form-module'] | //section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Publish to Aurora']])//input[@name='object_detection']"));
    private static final Locator SEND_HOTEL_IMAGE_TO_AURORA_CANCEL_BUTTON = new Locator("SEND_HOTEL_IMAGE_TO_AURORA_CANCEL_BUTTON", new By.ByXPath("//button[@data-testid='close-trigger-aurora-btn'] | //button[@data-testid='submit-send-to-aurora']/preceding-sibling::button[normalize-space()='Back']"));
    private static final Locator SEND_HOTEL_IMAGE_TO_AURORA_SUBMIT_BUTTON = new Locator("SEND_HOTEL_IMAGE_TO_AURORA_SUBMIT_BUTTON", new By.ByXPath("//button[@data-testid='submit-send-to-aurora' or @data-testid='submit-trigger-aurora-btn']"));
    private static final Locator UPLOAD_HOTEL_IMAGE_BUTTON = new Locator("UPLOAD_HOTEL_IMAGE_BUTTON", new By.ByXPath("//button[@data-testid='upload-photos' or @data-testid='toggle-upload-photos-btn']"));
    private static final Locator UPLOAD_HOTEL_IMAGE_MODAL = new Locator("UPLOAD_HOTEL_IMAGE_MODAL", new By.ByXPath("//div[@class='the-modal-content upload-photos-modal'] | //section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Upload Photos']]"));
    private static final Locator UPLOAD_HOTEL_IMAGE_INPUT = new Locator("UPLOAD_HOTEL_IMAGE_INPUT", new By.ByXPath("//input[@data-testid='dropzone-input' or @id='upload-photos-input']"));
    private static final Locator UPLOAD_HOTEL_IMAGE_PREVIEW_FILE_TITLE_LABEL = new Locator("UPLOAD_HOTEL_IMAGE_PREVIEW_FILE_TITLE_LABEL", new By.ByXPath("//div[@class='upload-photos-modal__img-preview-container']//span[contains(@class,'text')] | //img[starts-with(@data-testid,'upload-preview-')]"));
    private static final Locator UPLOAD_HOTEL_IMAGE_SAVE_BUTTON = new Locator("UPLOAD_HOTEL_IMAGE_SAVE_BUTTON", new By.ByXPath("//button[@data-testid='button-next-uploader']"));
    private static final Locator UPLOAD_HOTEL_IMAGE_SELECT_CATEGORY_AND_LABEL_MODAL = new Locator("UPLOAD_HOTEL_IMAGE_SELECT_CATEGORY_AND_LABEL_MODAL", new By.ByXPath("//div[@class='the-modal-content tix-modal-select-category'] | //section[contains(@class,'Modal_modal_popup')][.//h2[contains(normalize-space(),'Select Category')]]"));
    private static final Locator UPLOAD_HOTEL_IMAGE_CATEGORY_DROPDOWN_FIELD = new Locator("UPLOAD_HOTEL_IMAGE_CATEGORY_DROPDOWN_FIELD", new By.ByXPath("//div[@class='the-dropdown'][@id='categoryId'] | //section[contains(@class,'Modal_modal_popup')][.//h2[contains(normalize-space(),'Select Category')]]//input[@id='photo-category']"));
    private static final Locator UPLOAD_HOTEL_IMAGE_LABEL_DROPDOWN_FIELD = new Locator("UPLOAD_HOTEL_IMAGE_LABEL_DROPDOWN_FIELD", new By.ByXPath("//div[@class='the-dropdown'][@id='labelId'] | //section[contains(@class,'Modal_modal_popup')][.//h2[contains(normalize-space(),'Select Category')]]//input[@id='photo-label']"));
    private static final Locator UPLOAD_HOTEL_IMAGE_CATEGORY_SAVE_BUTTON = new Locator("UPLOAD_HOTEL_IMAGE_CATEGORY_SAVE_BUTTON", new By.ByXPath("//button[@data-testid='save-category-image']"));
    private static final Locator MOVE_HOTEL_IMAGE_BUTTON = new Locator("MOVE_HOTEL_IMAGE_BUTTON", new By.ByXPath("//button[@data-testid='edit-photo' or @data-testid='toggle-move-btn']"));
    private static final Locator MOVE_HOTEL_IMAGE_MODAL = new Locator("MOVE_HOTEL_IMAGE_MODAL", new By.ByXPath("//form[@data-testid='form-test-id'] | //section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Move Section']]"));
    private static final Locator INCLUDE_EXCLUDE_DROPDOWN_INPUT = new Locator("INCLUDE_EXCLUDE_DROPDOWN_INPUT", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Move Section']]//div[contains(@class,'dropdown-move-section') or contains(@class,'dropdown-move-room-section')]//input[starts-with(@id,'includeexclude')]"));
    private static final Locator EDIT_PHOTO_CATEGORY_DROPDOWN = new Locator("EDIT_PHOTO_CATEGORY_DROPDOWN", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Move Section']]//div[contains(@class,'dropdown-move-category')]//input[@id='photo-category']"));
    private static final Locator EDIT_PHOTO_LABEL_DROPDOWN = new Locator("EDIT_PHOTO_LABEL_DROPDOWN", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Move Section']]//div[contains(@class,'dropdown-move-label')]//input[@id='photo-label']"));
    private static final Locator EDIT_PHOTO_QUALITY_DROPDOWN = new Locator("EDIT_PHOTO_QUALITY_DROPDOWN", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Move Section']]//div[contains(@class,'dropdown-move-quality')]//input[@id='goodbad']"));
    private static final Locator MOVE_HOTEL_IMAGE_CANCEL_BUTTON = new Locator("MOVE_HOTEL_IMAGE_CANCEL_BUTTON", new By.ByXPath("//form[@data-testid='form-test-id']//button[1] | //section[contains(@class,'Modal_modal_popup')][.//h2[normalize-space()='Move Section']]//button[normalize-space()='Back']"));
    private static final Locator MOVE_HOTEL_IMAGE_SUBMIT_BUTTON = new Locator("MOVE_HOTEL_IMAGE_SUBMIT_BUTTON", new By.ByXPath("//form[@data-testid='form-test-id']//button[@data-testid='confirm-move-btn'] | //button[@data-testid='confirm-move-section'] | //button[@data-testid='confirm-move-room-section']"));
    private static final Locator CANCEL_HOTEL_IMAGE_BUTTON = new Locator("CANCEL_HOTEL_IMAGE_BUTTON", new By.ByXPath("//button[@data-testid='cancel-gallery-changes' or @data-testid='toggle-cancel-btn']"));
    private static final Locator SAVE_HOTEL_IMAGE_BUTTON = new Locator("SAVE_HOTEL_IMAGE_BUTTON", new By.ByXPath("//button[@data-testid='save-gallery-changes' or @data-testid='toggle-save-btn']"));
    private static final Locator IMAGE = new Locator("IMAGE", new By.ByXPath("//img[@data-nimg='fill' and contains(@src,'tiket.photos')]"));
    private static final Locator UNRATED_HOTEL_IMAGE_CHECKBOX = new Locator("UNRATED_HOTEL_IMAGE_CHECKBOX", new By.ByXPath("(//div[contains(@class,'text-medium-gray')])[1]/following-sibling::div[contains(@class,'img-grid-container')]//div[contains(@class,'grid-content')]//span[@class='checkbox-style '] | //span[normalize-space()='Not scored yet']/ancestor::*[.//input[starts-with(@data-testid,'photo-check-')]][1]//input[starts-with(@data-testid,'photo-check-')]/following-sibling::span[contains(@class,'custom_checkbox')]"));
    private static final Locator FIRST_UNRATED_HOTEL_IMAGE_CHECKBOX = new Locator("FIRST_UNRATED_HOTEL_IMAGE_CHECKBOX", new By.ByXPath("(//div[contains(@class,'text-medium-gray')])[1]/following-sibling::div[contains(@class,'img-grid-container')]//div[contains(@class,'grid-content')][1]//span[@class='checkbox-style '] | (//span[normalize-space()='Not scored yet']/ancestor::*[.//input[starts-with(@data-testid,'photo-check-')]][1]//input[starts-with(@data-testid,'photo-check-')]/following-sibling::span[contains(@class,'custom_checkbox')])[1]"));
    private static final Locator LAST_USER_UPLOADED_IMAGE_CHECKBOX = new Locator("LAST_USER_UPLOADED_IMAGE_CHECKBOX", new By.ByXPath("((//span[@class='sorting-source'][text()='USER']/ancestor::div[contains(@class,'grid-content')]//span[@class='checkbox-style ']) | //div[starts-with(@data-testid,'photo-card-')][.//div[@data-testid='photo-meta']/span[normalize-space()='USER']]//span[contains(@class,'Check_custom_checkbox')])[last()]"));
    private static final Locator MANAGE_HOTEL_PHOTOS_TAB = new Locator("MANAGE_HOTEL_PHOTOS_TAB", new By.ByXPath("//span[contains(@class,'Tabs_tab_text') and normalize-space()='Manage Hotel Photos']"));
    private static final Locator LEAVE_PAGE_MODAL_LEAVE_BUTTON = new Locator("LEAVE_PAGE_MODAL_LEAVE_BUTTON", new By.ByXPath("(//div[contains(@class,'leavepage-modal')]//button[contains(@class,'the-button primary')])[1]"));
    private static final Locator CANCEL_MANAGE_HOTEL_IMAGE_BUTTON = new Locator("CANCEL_MANAGE_HOTEL_IMAGE_BUTTON", new By.ByXPath("//button[contains(@class,'the-button ghost discard-button mr-16')]"));
    private static final Locator SAVE_MANAGE_HOTEL_IMAGE_BUTTON = new Locator("SAVE_MANAGE_HOTEL_IMAGE_BUTTON", new By.ByXPath("//button[@data-testid='save-photo-order']"));
    private static final Locator ROOM_PHOTOS_TAB = new Locator("ROOM_PHOTOS_TAB", new By.ByXPath("//span[contains(@class,'Tabs_tab_text') and normalize-space()='Room Photos']"));
    private static final Locator ROOM_PHOTOS_ROOM_GROUP_DROPDOWN = new Locator("ROOM_PHOTOS_ROOM_GROUP_DROPDOWN", new By.ById("room-group"));
    private static final Locator ROOM_PHOTOS_FILTER_SEARCH_BUTTON = new Locator("ROOM_PHOTOS_FILTER_SEARCH_BUTTON", new By.ByXPath("//button[@data-testid='room-photos-filter-search'] | (//input[@id='room-group']/ancestor::*[.//button[normalize-space()='Search']][1]//button[normalize-space()='Search'])[1]"));
    private static final Locator ROOM_PHOTOS_SAVE_BUTTON = new Locator("ROOM_PHOTOS_SAVE_BUTTON", new By.ByXPath("//button[@data-testid='room-photos-save'] | //button[@data-testid='save-gallery-changes' or @data-testid='toggle-save-btn']"));
    private static final Locator MANAGE_ROOM_PHOTOS_TAB = new Locator("MANAGE_ROOM_PHOTOS_TAB", new By.ByXPath("//span[contains(@class,'Tabs_tab_text') and normalize-space()='Manage Room Photos']"));
    private static final Locator MANAGE_ROOM_PHOTOS_SEND_TO_AURORA_BUTTON = new Locator("MANAGE_ROOM_PHOTOS_SEND_TO_AURORA_BUTTON", new By.ByXPath("//button[@data-testid='manage-room-photos-send-to-aurora']"));
    private static final Locator MANAGE_ROOM_PHOTOS_REMOVE_PHOTO_BUTTON = new Locator("MANAGE_ROOM_PHOTOS_REMOVE_PHOTO_BUTTON", new By.ByXPath("//button[@data-testid='manage-room-photos-remove']"));
    private static final Locator MANAGE_ROOM_PHOTOS_COPY_PHOTOS_BUTTON = new Locator("MANAGE_ROOM_PHOTOS_COPY_PHOTOS_BUTTON", new By.ByXPath("//button[@data-testid='manage-room-photos-copy']"));
    private static final Locator MANAGE_ROOM_PHOTOS_ADD_PHOTOS_BUTTON = new Locator("MANAGE_ROOM_PHOTOS_ADD_PHOTOS_BUTTON", new By.ByXPath("//button[@data-testid='manage-room-photos-add']"));
    private static final Locator MANAGE_ROOM_PHOTOS_MOVE_PHOTOS_BUTTON = new Locator("MANAGE_ROOM_PHOTOS_MOVE_PHOTOS_BUTTON", new By.ByXPath("//button[@data-testid='manage-room-photos-move']"));
    private static final Locator MANAGE_ROOM_PHOTOS_FILTER_ROOM_GROUP_DROPDOWN_INPUT = new Locator("MANAGE_ROOM_PHOTOS_FILTER_ROOM_GROUP_DROPDOWN_INPUT", new By.ByXPath("//div[@data-testid='manage-room-photos-filter']//input[@id='room-group']"));
    private static final Locator MANAGE_ROOM_PHOTOS_FILTER_SEARCH_BUTTON = new Locator("MANAGE_ROOM_PHOTOS_FILTER_SEARCH_BUTTON", new By.ByXPath("//div[@data-testid='manage-room-photos-filter']//button[@data-testid='manage-room-photos-filter-search']"));
    private static final Locator ROOM_GROUP_PHOTOS = new Locator("ROOM_GROUP_PHOTOS", new By.ByXPath("(//div[@class='room-group-image-section']//div[@class='room-photo-item'])[1] | (//div[@data-testid='room-group-images']//div[starts-with(@data-testid,'room-photo-card-')])[1]"));
    private static final Locator MANAGE_ROOM_GROUP_PHOTOS_INCLUDE_EXCLUDE_MODAL = new Locator("MANAGE_ROOM_GROUP_PHOTOS_INCLUDE_EXCLUDE_MODAL", new By.ByXPath("//div[contains(@class,'ModalBody_content')][.//button[starts-with(@data-testid,'confirm-')]]"));
    private static final Locator MANAGE_ROOM_GROUP_PHOTOS_INCLUDE_EXCLUDE_CONFIRM_BUTTON = new Locator("MANAGE_ROOM_GROUP_PHOTOS_INCLUDE_EXCLUDE_CONFIRM_BUTTON", new By.ByXPath("//div[contains(@class,'ModalBody_content')]//button[starts-with(@data-testid,'confirm-') or contains(@class,'Button_variant_primary') or contains(@class,'Button_variant_danger')]"));
    private static final Locator MANAGE_ROOM_GROUP_PHOTOS_ROOM_GROUP_ARRANGE_PHOTO_MODAL = new Locator("MANAGE_ROOM_GROUP_PHOTOS_ROOM_GROUP_ARRANGE_PHOTO_MODAL", new By.ByXPath("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'room-group-image-panel-')][.//div[@data-testid='arrange-photo-grid']]"));
    private static final Locator MANAGE_ROOM_GROUP_PHOTOS_ROOM_VENDOR_ARRANGE_PHOTO_MODAL = new Locator("MANAGE_ROOM_GROUP_PHOTOS_ROOM_VENDOR_ARRANGE_PHOTO_MODAL", new By.ByXPath("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'room-section-')][.//div[@data-testid='arrange-photo-grid']]"));
    private static final Locator MANAGE_ROOM_GROUP_PHOTOS_ARRANGE_PHOTO_SAVE_BUTTON = new Locator("MANAGE_ROOM_GROUP_PHOTOS_ARRANGE_PHOTO_SAVE_BUTTON", new By.ByXPath("//div[@data-testid='manage-room-photos-list']//button[starts-with(@data-testid,'arrange-room-') and contains(@data-testid,'-save')]"));
    private static final Locator SUCCESSFUL_TOAST_SNACKBAR = new Locator("SUCCESSFUL_TOAST_SNACKBAR", new By.ByXPath("//div[contains(@class,'Snackbar_snackbar') and contains(@class,'Snackbar_type_success')]"));
    private static final Locator EDIT_PHOTO_BUTTON = new Locator("EDIT_PHOTO_BUTTON", new By.ByXPath("//button[@data-testid='room-photos-btn-edit' or @data-testid='room-photos-edit-photo']"));
    private static final Locator LAST_UNPINNED_HOTEL_IMAGE_PIN = new Locator("LAST_UNPINNED_HOTEL_IMAGE_PIN", new By.ByXPath("(//div[@data-testid='sortable-photo-grid']//button[starts-with(@data-testid,'sortable-photo-pin-')][@aria-pressed='false'])[last()]"));
    private static final Locator ROOM_GROUP_TYPE_ALL_FILTER_DROPDOWN = new Locator("ROOM_GROUP_TYPE_ALL_FILTER_DROPDOWN", By.xpath("//div[@data-testid='manage-room-photos-filter']//div[contains(@class,'dropdown-filter-room-group-pop-over')]//li[.//input[contains(@class,'multiple-select-items')]]//label"));

    private WebElement getIncludedHotelImageCheckboxByImageUrl(String url) {
        return driverTools.waitForElementToPresence(new Locator("INCLUDED_HOTEL_IMAGE_CHECKBOX", new By.ByXPath(String.format("//div[@class='the-col-6 pr-20']//img[@src='https:%s']/preceding-sibling::div[@class='grid-cb']//span", url)))).orElseThrow();
    }

    private WebElement getExcludedHotelImageCheckboxByImageUrl(String url) {
        return driverTools.waitForElementToPresence(new Locator("EXCLUDED_HOTEL_IMAGE_CHECKBOX", new By.ByXPath(String.format("//div[@class='the-col-6 pl-20']//img[@src='https:%s']/preceding-sibling::div[@class='grid-cb']//span", url)))).orElseThrow();
    }
    private WebElement getIncludeExcludeDropdownList(String value) {
        return driverTools.waitForElementToBeClickable(
                new Locator("INCLUDE_EXCLUDE_DROPDOWN_LIST", new By.ByXPath(String.format(
                                "//div[contains(@class,'popoverlist-menu')]//label[normalize-space()='%s']/parent::span | //li[@data-label='%s' and @data-name='section']", value, value)))).orElseThrow();
    }
    private WebElement getSortedHotelImageByIndex(int index) {
        return driverTools.waitForElementToPresence(new Locator("SORTED_HOTEL_IMAGE", new By.ByXPath(String.format("(//div[@data-testid='sortable-photo-grid']/div)[%d]", index)))).orElseThrow();
    }
    private WebElement getPinHotelImageButtonByImageUrl(String url) {
        return driverTools.waitForElementToPresence(new Locator("PIN_HOTEL_IMAGE_BUTTON", By.xpath(String.format("//div[@data-testid='sortable-photo-grid']/div[.//img[contains(@src,'%s')]]//button[starts-with(@data-testid,'sortable-photo-pin-')]", url)))).orElseThrow() ;
    }

    private WebElement getPinHotelImageButtonByIndex(int index) {
        return driverTools.waitForElementToPresence(new Locator("PIN_HOTEL_IMAGE_BUTTON", new By.ByXPath(String.format("(//div[@data-testid='sortable-photo-grid']/div)[%d]//button[starts-with(@data-testid,'sortable-photo-pin-')]", index)))).orElseThrow();
    }

    private WebElement getUploadHotelImageCategoryDropdown(String value) {
        return driverTools.waitForElementToAppear(new Locator("UPLOAD_HOTEL_IMAGE_CATEGORY_DROPDOWN", new By.ByXPath(String.format("//div[@class='the-dropdown'][@id='categoryId']//div[@class='the-popoverlist']//label[text()='%s']/parent::span | //li[@data-label='%s' and starts-with(@data-name,'categoryId')]", value, value)))).orElseThrow();
    }

    private WebElement getUploadHotelImageLabelDropdown(String value) {
        return driverTools.waitForElementToAppear(new Locator("UPLOAD_HOTEL_IMAGE_LABEL_DROPDOWN", new By.ByXPath(String.format("//div[@class='the-dropdown'][@id='labelId']//div[@class='the-popoverlist']//label[text()='%s']/parent::span | //li[@data-label='%s' and starts-with(@data-name,'labelId')]", value, value)))).orElseThrow();
    }

    private Optional<List<WebElement>> getHotelImageFromUser() {
        return driverTools.elements(new Locator("HOTEL_IMAGE_FROM_USER", new By.ByXPath("//span[@class='sorting-source'][text()='USER'] | //div[starts-with(@data-testid,'photo-card-')][.//div[@data-testid='photo-meta']/span[normalize-space()='USER']]")));
    }

    public boolean isUserUploadedImageDisplayed() {
        return driverTools.waitForElementToAppear(
                new Locator("HOTEL_IMAGE_FROM_USER", new By.ByXPath("//span[@class='sorting-source'][text()='USER'] | //div[starts-with(@data-testid,'photo-card-')][.//div[@data-testid='photo-meta']/span[normalize-space()='USER']]"))
        ).isPresent();
    }

    public int getUserUploadedImageCount() {
        // the gallery loads asynchronously: wait for photo cards to render before counting, otherwise a
        // count taken right after the page opens reads 0 even when USER photos already exist
        driverTools.waitForElementToAppear(new Locator("ANY_GALLERY_PHOTO_CARD", new By.ByXPath("//div[starts-with(@data-testid,'photo-card-')] | //span[@class='sorting-source']")), 15);
        driverTools.dummyWait(1);
        return getHotelImageFromUser().map(List::size).orElse(0);
    }

    private WebElement getRoomGroupTypeDropdown(String value) {
        return driverTools.waitForElementToPresence(new Locator("ROOM_GROUP_TYPE_FILTER_DROPDOWN", new By.ByXPath(String.format("//div[@data-testid='manage-room-photos-filter']//div[contains(@class,'dropdown-filter-room-group-pop-over')]//li//label[normalize-space()='%s']", value)))).orElseThrow();
    }

    private WebElement getRoomGroupImageCheckboxByImageUrl(String url) {
        return driverTools.waitForElementToAppear(new Locator("ROOM_GROUP_IMAGE_CHECKBOX", new By.ByXPath(String.format("//div[starts-with(@data-testid,'room-group-image-panel-')]//div[starts-with(@data-testid,'room-photo-card-')][.//img[contains(@src,'%s')]]//input[starts-with(@data-testid,'room-photo-check-')]/following-sibling::span", url)))).orElseThrow();
    }
    private List<WebElement> getRoomGroupImageByRoomNameAndImageUrl(String roomName, String url) {
        return driverTools.elements(new Locator("ROOM_GROUP_IMAGE", new By.ByXPath(String.format("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'manage-room-group-card-')][.//div[@data-testid='card-title'][normalize-space()='%s']]//div[starts-with(@data-testid,'room-group-image-panel-')]//div[starts-with(@data-testid,'room-photo-card-')]//img[contains(@src,'%s')]", roomName, url)))).orElse(new ArrayList<>());
    }

    private WebElement getRoomVendorImageCheckboxByRoomNameAndImageUrl(String roomName, String url) {
        return driverTools.waitForElementToAppear(new Locator("ROOM_VENDOR_IMAGE", new By.ByXPath(String.format("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'manage-room-group-card-')][.//div[@data-testid='card-title'][normalize-space()='%s']]//div[starts-with(@data-testid,'room-section-')]//div[starts-with(@data-testid,'room-photo-card-')][.//img[contains(@src,'%s')]]//input[starts-with(@data-testid,'room-photo-check-')]/following-sibling::span", roomName, url)))).orElseThrow();
    }
    private Locator getAllRoomVendorImageCheckboxByRoomName(String roomName) {
        return new Locator("ROOM_VENDOR_IMAGE", new By.ByXPath(String.format("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'manage-room-group-card-')][.//div[@data-testid='card-title'][normalize-space()='%s']]//div[starts-with(@data-testid,'room-section-')]//div[starts-with(@data-testid,'room-photo-card-')]//img", roomName)));
    }
    private Locator getRoomVendorImageCheckboxByRoomNameAndVendorRoomName(String roomName, String vendorRoomName) {
        return new Locator("ROOM_VENDOR_IMAGE", new By.ByXPath(String.format("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'manage-room-group-card-')][.//div[@data-testid='card-title'][normalize-space()='%s']]//div[starts-with(@data-testid,'room-section-')][.//div[@data-testid='room-label'][normalize-space()='%s']]//div[starts-with(@data-testid,'room-photo-card-')]//img", roomName, vendorRoomName)));
    }
    private Locator getRoomGroupImageCheckboxByRoomName(String roomName) {
        return new Locator("ROOM_GROUP_IMAGE", new By.ByXPath(String.format("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'manage-room-group-card-')][.//div[@data-testid='card-title'][normalize-space()='%s']]//div[starts-with(@data-testid,'room-group-image-panel-')]//div[starts-with(@data-testid,'room-photo-card-')]//img", roomName)));
    }
    private Locator getAllRoomNamesByRoomGroupLocator(String  roomGroupName) {
        return new Locator("ROOM_NAMES", new By.ByXPath(String.format("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'manage-room-group-card-')][.//div[@data-testid='card-title'][normalize-space()='%s']]//div[starts-with(@data-testid,'room-section-')]//div[@data-testid='room-label']", roomGroupName)));
    }

    private WebElement getRoomGroupCardByRoomName(String roomName) {
        return driverTools.waitForElementToAppear(new Locator("ROOM_GROUP_CARD", new By.ByXPath(String.format("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'manage-room-group-card-')][.//div[@data-testid='card-title'][normalize-space()='%s']]", roomName))), 10).orElseThrow();
    }
    private WebElement getRoomVendorCardByRoomNameAndRoomVendorName(String roomName, String roomVendorName) {
        return driverTools.waitForElementToAppear(new Locator("ROOM_VENDOR_CARD", new By.ByXPath(String.format("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'manage-room-group-card-')][.//div[@data-testid='card-title'][normalize-space()='%s']]//div[starts-with(@data-testid,'room-section-')][.//div[@data-testid='room-label'][normalize-space()='%s']]", roomName, roomVendorName))), 10).orElseThrow();
    }

    private WebElement getRoomVendorImageCardByRoomName(String roomName) {
        return driverTools.waitForElementToAppear(new Locator("ROOM_VENDOR_IMAGE_CARD", new By.ByXPath(String.format("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'manage-room-group-card-')][.//div[@data-testid='card-title'][normalize-space()='%s']]//button[starts-with(@data-testid,'toggle-collapse-include-')]", roomName)))).orElseThrow();
    }

    private WebElement getShowAllImageButtonByRoomName(String roomName) {
        return driverTools.waitForElementToBeClickable(new Locator("SHOW_ALL_BUTTON", new By.ByXPath(String.format("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'manage-room-group-card-')][.//div[@data-testid='card-title'][normalize-space()='%s']]//button[starts-with(@data-testid,'toggle-collapse-include-')]", roomName)))).orElseThrow();
    }

    private static final Locator SHOW_ALL_EXCLUDE_BUTTON = new Locator("SHOW_ALL_EXCLUDE_BUTTON", new By.ByXPath("(//button[starts-with(@data-testid,'toggle-collapse-exclude')][not(ancestor::div[@data-testid='manage-room-photos-list'])])[1]"));
    private static final Locator SHOW_ALL_INCLUDE_BUTTON = new Locator("SHOW_ALL_INCLUDE_BUTTON", new By.ByXPath("(//button[starts-with(@data-testid,'toggle-collapse-include')][not(ancestor::div[@data-testid='manage-room-photos-list'])])[1]"));
    private static final Locator FIRST_NOT_SCORED_ROOM_PHOTO_CHECKBOX = new Locator("FIRST_NOT_SCORED_ROOM_PHOTO_CHECKBOX", new By.ByXPath("(//span[contains(@class,'room-photos-quality-title') and text()='Not Scored Yet']/following-sibling::div//div[@class='room-photo-item'][1]//span[@class='checkbox-style '])[1]"));

    private WebElement getRoomGroupArrangePhotoButtonByRoomName(String roomName) {
        return driverTools.waitForElementToBeClickable(new Locator("ROOM_GROUP_ARRANGE_PHOTO_BUTTON", new By.ByXPath(String.format("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'manage-room-group-card-')][.//div[@data-testid='card-title'][normalize-space()='%s']]//div[starts-with(@data-testid,'room-group-image-panel-')]//button[starts-with(@data-testid,'arrange-room-group-')]", roomName))), 10).orElseThrow();
    }

    private WebElement getRoomVendorArrangePhotoButtonByRoomNameAndVendorName(String roomName, String vendorName) {
        return driverTools.waitForElementToBeClickable(new Locator("ROOM_VENDOR_ARRANGE_PHOTO_BUTTON", new By.ByXPath(String.format("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'manage-room-group-card-')][.//div[@data-testid='card-title'][normalize-space()='%s']]//div[starts-with(@data-testid,'room-section-')][.//div[@data-testid='room-label'][contains(normalize-space(),'%s')]]//button[starts-with(@data-testid,'arrange-room-')][not(starts-with(@data-testid,'arrange-room-group'))]", roomName, vendorName)))).orElseThrow();
    }

    private WebElement getRoomGroupImageInArrangePhotoModalByIndex(int index) {
        return driverTools.element(new Locator("ROOM_GROUP_IMAGE_ARRANGE_PHOTO_MODAL", new By.ByXPath(String.format("(//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'room-group-image-panel-')]//div[@data-testid='arrange-photo-grid']/div[starts-with(@data-testid,'arrange-photo-')])[%d]//div[starts-with(@data-testid,'arrange-photo-handle-')]", index)))).orElseThrow();
    }

    private WebElement getRoomGroupImageArrangeItemByIndex(int index) {
        return driverTools.element(new Locator("ROOM_GROUP_IMAGE_ARRANGE_ITEM", new By.ByXPath(String.format("(//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'room-group-image-panel-')]//div[@data-testid='arrange-photo-grid']/div[starts-with(@data-testid,'arrange-photo-')])[%d]", index)))).orElseThrow();
    }

    private WebElement getRoomVendorImageInArrangePhotoModalByIndex(int index) {
        return driverTools.element(new Locator("ROOM_VENDOR_IMAGE_ARRANGE_PHOTO_MODAL", new By.ByXPath(String.format("(//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'room-section-')]//div[@data-testid='arrange-photo-grid']/div[starts-with(@data-testid,'arrange-photo-')])[%d]//div[starts-with(@data-testid,'arrange-photo-handle-')]", index)))).orElseThrow();
    }

    private WebElement getRoomVendorImageArrangeItemByIndex(int index) {
        return driverTools.element(new Locator("ROOM_VENDOR_IMAGE_ARRANGE_ITEM", new By.ByXPath(String.format("(//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'room-section-')]//div[@data-testid='arrange-photo-grid']/div[starts-with(@data-testid,'arrange-photo-')])[%d]", index)))).orElseThrow();
    }

    public boolean isHotelPhotosTabActive() {
        driverTools.waitForElementToPresence(HOTEL_PHOTOS_TAB, 60).orElseThrow();
        return driverTools.waitUntil(driver -> {
            try {
                String classAttr = driver.findElement(HOTEL_PHOTOS_TAB.by()).getAttribute("class");
                return classAttr != null && classAttr.contains("Text_variant_activity") ? Boolean.TRUE : null;
            } catch (Exception e) {
                return null;
            }}, 10).orElse(false);
    }

    public boolean isImageLoaded() {
        WebElement image = driverTools.waitForElementToPresence(IMAGE).orElseThrow();
        return driverTools.isElementDisplayed(image);
    }

    public void clickSendHotelImageToAurora() {
        WebElement sendHotelImageToAuroraButton = driverTools.waitForElementToPresence(SEND_HOTEL_IMAGE_TO_AURORA_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(sendHotelImageToAuroraButton);
    }

    public boolean isSendHotelImageToAuroraModalDisplayed() {
        return driverTools.waitForElementToAppear(SEND_HOTEL_IMAGE_TO_AURORA_MODAL, 10).isPresent();
    }

    private void checkHotelImageAuroraModule(String module, boolean expectedlyChecked) {
        WebElement moduleCheckboxState = null;
        WebElement moduleCheckbox = switch (module) {
            case "SCENE_DETECTION" -> {
                moduleCheckboxState = driverTools.waitForElementToPresence(SCENE_DETECTION_MODULE_CHECKBOX_STATE).orElseThrow();
                yield driverTools.waitForElementToPresence(SCENE_DETECTION_MODULE_CHECKBOX).orElseThrow();
            }
            case "QUALITY_CHECKING" -> {
                moduleCheckboxState = driverTools.waitForElementToPresence(QUALITY_CHECKING_MODULE_CHECKBOX_STATE).orElseThrow();
                yield driverTools.waitForElementToPresence(QUALITY_CHECKING_MODULE_CHECKBOX).orElseThrow();
            }
            case "SIMILARITY_CHECKING" -> {
                moduleCheckboxState = driverTools.waitForElementToPresence(SIMILARITY_CHECKING_MODULE_CHECKBOX_STATE).orElseThrow();
                yield driverTools.waitForElementToPresence(SIMILARITY_CHECKING_MODULE_CHECKBOX).orElseThrow();
            }
            case "OBJECT_DETECTION" -> {
                moduleCheckboxState = driverTools.waitForElementToPresence(OBJECT_DETECTION_MODULE_CHECKBOX_STATE).orElseThrow();
                yield driverTools.waitForElementToPresence(OBJECT_DETECTION_MODULE_CHECKBOX).orElseThrow();
            }
            default -> null;
        };
        boolean isCheckboxChecked = Objects.requireNonNull(moduleCheckboxState).isSelected();
        if (isCheckboxChecked && !expectedlyChecked) {
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(moduleCheckbox);
        } else if (!isCheckboxChecked && expectedlyChecked) {
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(moduleCheckbox);
        }
    }

    public void checkHotelImageSceneDetectionModule(boolean expectedlyChecked) {
        checkHotelImageAuroraModule("SCENE_DETECTION", expectedlyChecked);
    }

    public void checkHotelImageQualityCheckingModule(boolean expectedlyChecked) {
        checkHotelImageAuroraModule("QUALITY_CHECKING", expectedlyChecked);
    }

    public void checkHotelImageSimilarityCheckingModule(boolean expectedlyChecked) {
        checkHotelImageAuroraModule("SIMILARITY_CHECKING", expectedlyChecked);
    }

    public void checkHotelImageObjectDetectionModule(boolean expectedlyChecked) {
        checkHotelImageAuroraModule("OBJECT_DETECTION", expectedlyChecked);
    }

    public void clickCancelSendHotelImageToAurora() {
        WebElement sendHotelImageToAuroraCancelButton = driverTools.waitForElementToPresence(SEND_HOTEL_IMAGE_TO_AURORA_CANCEL_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(sendHotelImageToAuroraCancelButton);
    }

    public void clickSubmitSendHotelImageToAurora() {
        WebElement sendHotelImageToAuroraSubmitButton = driverTools.waitForElementToPresence(SEND_HOTEL_IMAGE_TO_AURORA_SUBMIT_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(sendHotelImageToAuroraSubmitButton);
    }

    public void clickMoveHotelImage() {
        WebElement moveHotelImageButton = driverTools.waitForElementToPresence(MOVE_HOTEL_IMAGE_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(moveHotelImageButton);
    }

    public boolean isMoveHotelImageModalDisplayed() {
        return driverTools.waitForElementToAppear(MOVE_HOTEL_IMAGE_MODAL, 10).isPresent();
    }
    public void selectIncludeExcludeConfiguration(String value) {
        WebElement includeExcludeDropdownInput = driverTools.waitForElementToBeClickable(INCLUDE_EXCLUDE_DROPDOWN_INPUT).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(includeExcludeDropdownInput);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getIncludeExcludeDropdownList(value));
    }
    /**
     * Opens a dropdown and clicks one option. If the list does not show up (the click can toggle an already
     * open list shut, or the list is still animating) the dropdown is clicked once more; as a last resort the
     * option is clicked via JavaScript even if it is not reported as visible.
     */
    private void selectDropdownOption(WebElement dropdown, Locator option) {
        WebElement item = null;
        for (int attempt = 0; attempt < 3 && item == null; attempt++) {
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(dropdown);
            item = findVisibleOption(dropdown, option, 4);
        }
        if (item == null) {
            throw new IllegalStateException("No visible dropdown option found for " + option.by());
        }
        try {
            item.click();
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(item);
        }
        driverTools.dummyWait(1);
        boolean stillOpen;
        try {
            stillOpen = item.isDisplayed();
        } catch (StaleElementReferenceException e) {
            stillOpen = false;
        }
        if (stillOpen) {
            String script = "var el=arguments[0];"
                    + "var targets=[el].concat(Array.prototype.slice.call(el.querySelectorAll('*')));"
                    + "targets.forEach(function(t){['pointerdown','mousedown','pointerup','mouseup','click'].forEach(function(type){"
                    + "t.dispatchEvent(new MouseEvent(type,{bubbles:true,cancelable:true,view:window}));});});";
            ((org.openqa.selenium.JavascriptExecutor) ((org.openqa.selenium.WrapsDriver) item).getWrappedDriver())
                    .executeScript(script, item);
            driverTools.dummyWait(1);
        }
    }

    private WebElement findVisibleOption(WebElement dropdown, Locator option, int seconds) {
        WebDriver wd = ((org.openqa.selenium.WrapsDriver) dropdown).getWrappedDriver();
        long end = System.currentTimeMillis() + seconds * 1000L;
        while (System.currentTimeMillis() < end) {
            for (WebElement candidate : wd.findElements(option.by())) {
                try {
                    if (candidate.isDisplayed()) {
                        return candidate;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // re-query on next poll
                }
            }
            driverTools.dummyWait(1);
        }
        return null;
    }

    public void selectEditPhotoCategory(String value) {
        WebElement categoryDropdown = driverTools.waitForElementToBeClickable(EDIT_PHOTO_CATEGORY_DROPDOWN).orElseThrow();
        selectDropdownOption(categoryDropdown, new Locator("EDIT_PHOTO_CATEGORY_OPTION", new By.ByXPath(String.format("(//div[@id='category']//div[@class='the-popoverlist']//label[text()='%s']/parent::span)[2] | //li[@data-label='%s' and @data-name='category']", value, value))));
    }

    public void selectEditPhotoLabel(String value) {
        WebElement labelDropdown = driverTools.waitForElementToBeClickable(EDIT_PHOTO_LABEL_DROPDOWN).orElseThrow();
        selectDropdownOption(labelDropdown, new Locator("EDIT_PHOTO_LABEL_OPTION", new By.ByXPath(String.format("(//div[@id='label']//div[@class='the-popoverlist']//label[text()='%s']/parent::span)[2] | //li[@data-label='%s' and @data-name='label']", value, value))));
    }

    public void selectEditPhotoQuality(String value) {
        WebElement qualityDropdown = driverTools.waitForElementToBeClickable(EDIT_PHOTO_QUALITY_DROPDOWN).orElseThrow();
        selectDropdownOption(qualityDropdown, new Locator("EDIT_PHOTO_QUALITY_OPTION", new By.ByXPath(String.format("//div[@id='quality']//div[@class='the-popoverlist']//label[text()='%s']/parent::span | //li[@data-label='%s' and @data-name='quality']", value, value))));
    }

    public void clickCancelMoveHotelImage() {
        WebElement moveHotelImageCancelButton = driverTools.waitForElementToPresence(MOVE_HOTEL_IMAGE_CANCEL_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(moveHotelImageCancelButton);
    }
    public void clickSubmitMoveHotelImage() {
        WebElement moveHotelImageSubmitButton = driverTools.waitForElementToBeClickable(MOVE_HOTEL_IMAGE_SUBMIT_BUTTON).orElseThrow();

        driverTools.clickOrFallbackToClickWithJavascriptExecutor(moveHotelImageSubmitButton);
    }
    public boolean isHotelImageIncluded(String url) {
        return driverTools.isElementDisplayed(getIncludedHotelImageCheckboxByImageUrl(url));
    }

    public boolean isHotelImageExcluded(String url) {
        return driverTools.isElementDisplayed(getExcludedHotelImageCheckboxByImageUrl(url));
    }

    public void checkIncludedHotelImage(String url) {
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getIncludedHotelImageCheckboxByImageUrl(url));
    }

    public void checkExcludedHotelImage(String url) {
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getExcludedHotelImageCheckboxByImageUrl(url));
    }

    public void clickSaveHotelImage() {
        WebElement saveHotelImageButton = driverTools.waitForElementToPresence(SAVE_HOTEL_IMAGE_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(saveHotelImageButton);
    }

    public void checkUnratedHotelImage() {
        WebElement unratedHotelImageCheckbox = driverTools.waitForElementToPresence(UNRATED_HOTEL_IMAGE_CHECKBOX).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(unratedHotelImageCheckbox);
    }

    public void checkFirstUnratedHotelImage() {
        WebElement firstUnratedHotelImageCheckbox = driverTools.waitForElementToPresence(FIRST_UNRATED_HOTEL_IMAGE_CHECKBOX).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(firstUnratedHotelImageCheckbox);
    }

    public void selectLastUserUploadedImage() {
        WebElement lastUserImageCheckbox = driverTools.waitForElementToPresence(LAST_USER_UPLOADED_IMAGE_CHECKBOX).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(lastUserImageCheckbox);
    }

    public void clickManageHotelPhotosTab() {
        WebElement manageHotelPhotosTab = driverTools.waitForElementToPresence(MANAGE_HOTEL_PHOTOS_TAB).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(manageHotelPhotosTab);
    }
    public boolean isManageHotelPhotosTabActive() {
        driverTools.waitForElementToPresence(MANAGE_HOTEL_PHOTOS_TAB).orElseThrow();
        return driverTools.waitUntil(driver -> {
            try {
                String classAttr = driver.findElement(MANAGE_HOTEL_PHOTOS_TAB.by()).getAttribute("class");
                return classAttr != null && classAttr.contains("Text_variant_activity") ? Boolean.TRUE : null;
            } catch (Exception e) {
                return null;
            }
        }, 10).orElse(false);
    }

    public void clickLeavePageModal() {
        WebElement leaveButton = driverTools.waitForElementToPresence(LEAVE_PAGE_MODAL_LEAVE_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(leaveButton);
    }

    public boolean isSaveManageHotelPhotoDisabled() {
        WebElement saveManageHotelPhoto = driverTools.waitForElementToPresence(SAVE_MANAGE_HOTEL_IMAGE_BUTTON).orElseThrow();
        return driverTools.getValueAttribute(saveManageHotelPhoto, "disabled") != null;
    }

    public void clickSaveManageHotelPhoto() {
        driverTools.waitForElementToPresence(SAVE_MANAGE_HOTEL_IMAGE_BUTTON).orElseThrow();
        driverTools.waitUntil(driver -> {
            try {
                WebElement button = driver.findElement(SAVE_MANAGE_HOTEL_IMAGE_BUTTON.by());
                return button.getAttribute("disabled") == null ? Boolean.TRUE : null;
            } catch (Exception e) {
                return null;
            }
        }, 10);
        WebElement saveManageHotelPhoto = driverTools.waitForElementToPresence(SAVE_MANAGE_HOTEL_IMAGE_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(saveManageHotelPhoto);
    }

    public void clickPinHotelImageByImageUrl(String url) {
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getPinHotelImageButtonByImageUrl(url));
    }

    public void clickPinHotelImageByIndex(int index) {
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getPinHotelImageButtonByIndex(index));
    }

    public void clickPinLastUnpinnedSortedImage() {
        WebElement pinButton = driverTools.waitForElementToPresence(LAST_UNPINNED_HOTEL_IMAGE_PIN).orElseThrow();
        driverTools.clickWithJavascriptExecutor(pinButton);
    }

    public String getLastUserUploadedImageKey() {
        WebElement img = driverTools.waitForElementToPresence(new Locator("LAST_USER_UPLOADED_IMAGE_IMG", new By.ByXPath("(//div[starts-with(@data-testid,'photo-card-')][.//div[@data-testid='photo-meta']/span[normalize-space()='USER']]//img | //span[@class='sorting-source'][text()='USER']/ancestor::div[contains(@class,'grid-content')]//img)[last()]"))).orElseThrow();
        String src = img.getAttribute("src");
        if (src == null || src.isBlank()) {
            throw new IllegalStateException("Last uploaded image has no 'src' yet; it may not have finished loading");
        }
        String path = src.split("\\?")[0];
        return path.substring(path.lastIndexOf('/') + 1);
    }

    public boolean pinHotelImageByKeyWithRetry(String key) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                if (!isHotelImagePinnedByUrl(key)) {
                    WebElement pinButton = getPinHotelImageButtonByImageUrl(key);
                    driverTools.scrollIntoWebViewCenter(pinButton);
                    driverTools.clickWithJavascriptExecutor(pinButton);
                }
            } catch (Exception e) {
                logger.log("Pin click attempt " + attempt + " failed: " + e.getMessage());
            }
            boolean pressed = driverTools.waitUntil(d -> {
                try {
                    return isHotelImagePinnedByUrl(key) ? Boolean.TRUE : null;
                } catch (Exception e) {
                    return null;
                }
            }, 4).isPresent();
            if (pressed) {
                return true;
            }
            logger.log("Image " + key + " not pinned after attempt " + attempt);
        }
        return false;
    }

    public boolean isHotelImagePinnedByUrl(String url) {
        return "true".equals(driverTools.getValueAttribute(getPinHotelImageButtonByImageUrl(url), "aria-pressed"));
    }

    public boolean isHotelImagePinnedByIndex(int index) {
        return "true".equals(driverTools.getValueAttribute(getPinHotelImageButtonByIndex(index), "aria-pressed"));
    }

    public boolean isHotelImagePinnedByIdentifier(String cardIdentifier) {
        String pinButtonTestId = cardIdentifier.replace("sortable-photo-", "sortable-photo-pin-");
        WebElement pinButton = driverTools.waitForElementToPresence(new Locator("PIN_HOTEL_IMAGE_BUTTON_BY_ID", new By.ByXPath(String.format("//button[@data-testid='%s']", pinButtonTestId)))).orElseThrow();
        return "true".equals(driverTools.getValueAttribute(pinButton, "aria-pressed"));
    }

    public void pinHotelImageByIdentifierWithRetry(String cardIdentifier) {
        String pinButtonTestId = cardIdentifier.replace("sortable-photo-", "sortable-photo-pin-");
        for (int attempt = 0; attempt < 3; attempt++) {
            // Hardcoded on purpose: after a dnd-kit drop the sortable list re-renders and swallows a pin click made too early.
            // There is no DOM state to wait on (the pin button is present and enabled the whole time, only the re-render is racing), so an explicit condition is not possible.
            sleepQuietly(1000);
            if (isHotelImagePinnedByIdentifier(cardIdentifier)) {
                return;
            }
            WebElement pinButton = driverTools.waitForElementToPresence(new Locator("PIN_HOTEL_IMAGE_BUTTON_BY_ID", new By.ByXPath(String.format("//button[@data-testid='%s']", pinButtonTestId)))).orElseThrow();
            driverTools.clickWithJavascriptExecutor(pinButton);
        }
    }

    public String getSortedHotelImageIdentifierByIndex(int index) {
        return driverTools.getValueAttribute(getSortedHotelImageByIndex(index), "data-testid");
    }

    public void dragAndDropSortedHotelImage(int sourceIndex, int targetIndex) {
        driverTools.dragAndDrop(getSortedHotelImageByIndex(sourceIndex), getSortedHotelImageByIndex(targetIndex));
    }

    public boolean isSortedHotelImageHasIdentifierByIndex(int index, String expected) {
        return getSortedHotelImageIdentifierByIndex(index).equals(expected);
    }

    public void clickUploadHotelImage() {
        WebElement uploadHotelImageButton = driverTools.waitForElementToPresence(UPLOAD_HOTEL_IMAGE_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(uploadHotelImageButton);
    }

    public boolean isUploadHotelImageModalDisplayed() {
        return driverTools.waitForElementToAppear(UPLOAD_HOTEL_IMAGE_MODAL).isPresent();
    }

    public void uploadHotelImage(File file) {
        WebElement uploadHotelImageInput = driverTools.waitForElementToPresence(UPLOAD_HOTEL_IMAGE_INPUT).orElseThrow();
        driverTools.sendKeys(uploadHotelImageInput, file.getAbsolutePath());
        driverTools.dummyWait(4);   // check this
    }

    public boolean isUploadHotelImagePreviewDisplayed() {
        WebElement uploadHotelImagePreview = driverTools.waitForElementToPresence(UPLOAD_HOTEL_IMAGE_PREVIEW_FILE_TITLE_LABEL).orElseThrow();
        return driverTools.isElementDisplayed(uploadHotelImagePreview);
    }

    public void clickSaveUploadHotelImageButton() {
        WebElement saveHotelImageButton = driverTools.waitForElementToPresence(UPLOAD_HOTEL_IMAGE_SAVE_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(saveHotelImageButton);
    }

    public boolean isUploadHotelImageSelectCategoryAndLabelModalDisplayed() {
        WebElement uploadHotelImageSelectCategoryAndLabelModal = driverTools.waitForElementToPresence(UPLOAD_HOTEL_IMAGE_SELECT_CATEGORY_AND_LABEL_MODAL).orElseThrow();
        return driverTools.isElementDisplayed(uploadHotelImageSelectCategoryAndLabelModal);
    }

    public void selectHotelImageCategory(String value) {
        WebElement uploadHotelImageCategoryDropdown = driverTools.waitForElementToPresence(UPLOAD_HOTEL_IMAGE_CATEGORY_DROPDOWN_FIELD).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(uploadHotelImageCategoryDropdown);
        driverTools.waitForElementToAppear(getUploadHotelImageCategoryDropdown(value));
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getUploadHotelImageCategoryDropdown(value));
    }

    public void selectHotelImageLabel(String value) {
        WebElement uploadHotelImageLabelDropdown = driverTools.waitForElementToPresence(UPLOAD_HOTEL_IMAGE_LABEL_DROPDOWN_FIELD).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(uploadHotelImageLabelDropdown);
        driverTools.waitForElementToAppear(getUploadHotelImageLabelDropdown(value));
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getUploadHotelImageLabelDropdown(value));
    }

    public void clickSaveUploadHotelImageCategory() {
        WebElement uploadHotelImageCategorySaveButton = driverTools.waitForElementToPresence(UPLOAD_HOTEL_IMAGE_CATEGORY_SAVE_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(uploadHotelImageCategorySaveButton);
    }

    public int getTotalHotelImageFromUser() {
        return getHotelImageFromUser().orElseThrow().size();
    }

    public void clickRoomPhotosTab() {
        WebElement roomPhotosTab = driverTools.waitForElementToPresence(ROOM_PHOTOS_TAB).orElseThrow();
        driverTools.clickWithJavascriptExecutor(roomPhotosTab);
    }

    public void selectRoomPhotoRoomGroup(String value) {
        WebElement roomGroupDropdown = driverTools.waitForElementToPresence(ROOM_PHOTOS_ROOM_GROUP_DROPDOWN).orElseThrow();
        driverTools.clickWithActions(roomGroupDropdown);
        WebElement option = driverTools.waitForElementToPresence(new Locator("ROOM_PHOTOS_ROOM_GROUP_OPTION", new By.ByXPath(String.format("(//div[contains(@class,'tmdf-autocomplete-multiselect-dropdown')]//div[@class='dropdown-checkbox-list']//label[contains(.,'%s')]//span[@class='checkbox-style '])[1] | //div[contains(@class,'dropdown-filter-room-group-pop-over')]//li//label[.//span[normalize-space()='%s']]//span[contains(@class,'Check_custom_checkbox')]", value, value)))).orElseThrow();
        driverTools.clickWithJavascriptExecutor(option);
    }

    public void clickRoomPhotosSearchFilter() {
        driverTools.waitForElementToPresence(ROOM_PHOTOS_FILTER_SEARCH_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(ROOM_PHOTOS_FILTER_SEARCH_BUTTON);
    }

    public void clickManageRoomPhotosTab() {
        WebElement manageRoomPhotosTab = driverTools.waitForElementToPresence(MANAGE_ROOM_PHOTOS_TAB).orElseThrow();
        driverTools.scrollIntoWebViewCenter(manageRoomPhotosTab);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(manageRoomPhotosTab);
        // The click can land before the tab bar finishes rendering; retry once via JS if the tab did not activate.
        if (!isManageRoomPhotosTabActive()) {
            WebElement retryTab = driverTools.waitForElementToPresence(MANAGE_ROOM_PHOTOS_TAB).orElseThrow();
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(retryTab);
        }
    }
    public boolean isManageRoomPhotosTabActive() {
        driverTools.waitForElementToPresence(MANAGE_ROOM_PHOTOS_TAB).orElseThrow();
        return driverTools.waitUntil(driver -> {
            try {
                String classAttr = driver.findElement(MANAGE_ROOM_PHOTOS_TAB.by()).getAttribute("class");
                return classAttr != null && classAttr.contains("Text_variant_activity") ? Boolean.TRUE : null;
            } catch (Exception e) {
                return null;
            }
        }, 10).orElse(false);
    }

    public boolean isManageRoomImageSendToAuroraButtonEnabled() {
        WebElement manageRoomImageSendToAuroraButton = driverTools.waitForElementToPresence(MANAGE_ROOM_PHOTOS_SEND_TO_AURORA_BUTTON).orElseThrow();
        return driverTools.isElementEnabled(manageRoomImageSendToAuroraButton);
    }

    public boolean isManageRoomImageSendToAuroraButtonClickable() {
        WebElement manageRoomImageSendToAuroraButton = driverTools.waitForElementToPresence(MANAGE_ROOM_PHOTOS_SEND_TO_AURORA_BUTTON).orElseThrow();
        return driverTools.isElementEnabled(manageRoomImageSendToAuroraButton);
    }

    public boolean isRoomGroupImagesDisplayed() {
        WebElement roomGroupImages = driverTools.waitForElementToPresence(ROOM_GROUP_PHOTOS).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(roomGroupImages);
        return driverTools.isElementDisplayed(roomGroupImages);
    }

    public void checkRoomGroupImageByUrl(String url) {
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getRoomGroupImageCheckboxByImageUrl(url));
    }

    public void clickRoomGroupImageSendToAurora() {
        WebElement manageRoomImageSendToAuroraButton = driverTools.waitForElementToPresence(MANAGE_ROOM_PHOTOS_SEND_TO_AURORA_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(driverTools.element(MANAGE_ROOM_PHOTOS_TAB).orElseThrow());
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(manageRoomImageSendToAuroraButton);
    }

    private void checkRoomGroupImageAuroraModule(String module) {
        WebElement moduleCheckbox = switch (module) {
            case "SCENE_DETECTION" -> {
                yield driverTools.waitForElementToPresence(SCENE_DETECTION_MODULE_CHECKBOX).orElseThrow();
            }
            case "QUALITY_CHECKING" -> {
                yield driverTools.waitForElementToPresence(QUALITY_CHECKING_MODULE_CHECKBOX).orElseThrow();
            }
            case "SIMILARITY_CHECKING" -> {
                yield driverTools.waitForElementToPresence(SIMILARITY_CHECKING_MODULE_CHECKBOX).orElseThrow();
            }
            case "OBJECT_DETECTION" -> {
                yield driverTools.waitForElementToPresence(OBJECT_DETECTION_MODULE_CHECKBOX).orElseThrow();
            }
            default -> null;
        };
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(Objects.requireNonNull(moduleCheckbox));
    }

    public void checkRoomGroupImageSceneDetectionModule() {
        checkRoomGroupImageAuroraModule("SCENE_DETECTION");
    }

    public void checkRoomGroupImageQualityCheckingModule() {
        checkRoomGroupImageAuroraModule("QUALITY_CHECKING");
    }

    public void checkRoomGroupImageSimilarityCheckingModule() {
        checkRoomGroupImageAuroraModule("SIMILARITY_CHECKING");
    }

    public void checkRoomGroupImageObjectDetectionModule() {
        checkRoomGroupImageAuroraModule("OBJECT_DETECTION");
    }

    public void clickRemoveRoomImage() {
        WebElement manageRoomPhotosRemoveButton = driverTools.waitForElementToPresence(MANAGE_ROOM_PHOTOS_REMOVE_PHOTO_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(driverTools.waitForElementToPresence(MANAGE_ROOM_PHOTOS_TAB).orElseThrow());
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(manageRoomPhotosRemoveButton);
    }

    public void clickAddRoomImage() {
        WebElement manageRoomPhotosAddButton = driverTools.waitForElementToPresence(MANAGE_ROOM_PHOTOS_ADD_PHOTOS_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(driverTools.waitForElementToPresence(MANAGE_ROOM_PHOTOS_TAB).orElseThrow());
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(manageRoomPhotosAddButton);
    }

    public List<String> getAllRoomGroupTypeFilters() {
        WebElement roomGroupDropdownInput = driverTools.waitForElementToBeClickable(MANAGE_ROOM_PHOTOS_FILTER_ROOM_GROUP_DROPDOWN_INPUT).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(roomGroupDropdownInput);
        driverTools.waitForElementsToAppear(ROOM_GROUP_TYPE_ALL_FILTER_DROPDOWN);
        List<WebElement> elements = driverTools.elements(ROOM_GROUP_TYPE_ALL_FILTER_DROPDOWN).orElseThrow();
        List<String> allRoomGroupTypeFilters = new ArrayList<>();
        for (WebElement element : elements) {
            String text = element.getText().trim();
            if (text.isEmpty()){
                text = driverTools.getAttribute(element, "textContent").trim();
            }
            allRoomGroupTypeFilters.add(text);
        }

        logger.log("Room Group filters => " +  allRoomGroupTypeFilters);
        return allRoomGroupTypeFilters;
    }
    public List<String> getAllVendorRoomNamesByRoomGroup(String roomGroup) {
        Locator roomNameLocator = getAllRoomNamesByRoomGroupLocator(roomGroup);
        driverTools.waitForElementsToAppear(roomNameLocator);
        List<WebElement> elements = driverTools.elements(roomNameLocator).orElseThrow();
        List<String> roomNames = new ArrayList<>();
        for (WebElement element : elements) {
            String text = element.getText().trim();
            if (text.isEmpty()){
                text = driverTools.getAttribute(element, "textContent").trim();
            }
            roomNames.add(text);
        }

        logger.log("Room Group filters => " +  roomNames);
        return roomNames;
    }

    public void searchAndSelectRoomGroupTypeFilter(String roomName) {
        WebElement roomGroupDropdownInput = driverTools.waitForElementToBeClickable(MANAGE_ROOM_PHOTOS_FILTER_ROOM_GROUP_DROPDOWN_INPUT).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(roomGroupDropdownInput);
        driverTools.clearInput(roomGroupDropdownInput);
        driverTools.sendKeys(roomGroupDropdownInput, roomName);
        // the popover can close/re-render while typing; the option stays in the DOM, so click it via JS when not interactable
        WebElement option = getRoomGroupTypeDropdown(roomName);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(option);
    }

    public void clickManageRoomPhotosSearchFilter() {
        driverTools.waitForElementToPresence(MANAGE_ROOM_PHOTOS_FILTER_SEARCH_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(MANAGE_ROOM_PHOTOS_FILTER_SEARCH_BUTTON);
    }

    public boolean isRoomGroupCardDisplayed(String roomName) {
        return driverTools.isElementDisplayed(getRoomGroupCardByRoomName(roomName));
    }

    public void showAllRoomImageByRoomName(String roomName) {
        // The "show all" toggle only exists when the group has more photos than the collapsed limit, so it is optional.
        Locator showAllLocator = new Locator("SHOW_ALL_BUTTON", new By.ByXPath(String.format("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'manage-room-group-card-')][.//div[@data-testid='card-title'][normalize-space()='%s']]//button[starts-with(@data-testid,'toggle-collapse-include-')]", roomName)));
        driverTools.waitForElementToBeClickable(showAllLocator, 5).ifPresent(showAllButton -> {
            String showAllText = showAllButton.getText().trim();
            if (showAllText.contains("Show") || showAllText.contains("Tampilkan")) {
                driverTools.clickOrFallbackToClickWithJavascriptExecutor(showAllButton);
            }
        });
        expandCollapsedRoomSections(roomName);
    }

    /** Vendor room sections render their photos only once expanded; open every collapsed section of the room group card. */
    private void expandCollapsedRoomSections(String roomName) {
        String collapsedToggles = String.format("//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'manage-room-group-card-')][.//div[@data-testid='card-title'][normalize-space()='%s']]//div[starts-with(@data-testid,'room-section-')]//button[@aria-label='Show' or @aria-label='Tampilkan']", roomName);
        for (int i = 0; i < 20; i++) {
            List<WebElement> toggles = driverTools.elements(new Locator("COLLAPSED_ROOM_SECTION_TOGGLE", new By.ByXPath(collapsedToggles))).orElse(new ArrayList<>());
            if (toggles.isEmpty()) {
                return;
            }
            WebElement toggle = toggles.get(0);
            driverTools.scrollIntoWebViewCenter(toggle);
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(toggle);
            // wait until this toggle flipped (its aria-label is no longer Show/Tampilkan) or was re-rendered
            driverTools.waitUntil(d -> {
                try {
                    String label = toggle.getAttribute("aria-label");
                    return !("Show".equals(label) || "Tampilkan".equals(label));
                } catch (StaleElementReferenceException e) {
                    return true;
                }
            }, 3);
        }
    }

    public void clickShowAllExcludeButton() {
        clickShowAllInNotScoredBand("exclude", SHOW_ALL_EXCLUDE_BUTTON);
    }

    public void clickShowAllIncludeButton() {
        clickShowAllInNotScoredBand("include", SHOW_ALL_INCLUDE_BUTTON);
    }

    /**
     * Clicks every "Show All" inside the column's Not-scored band (real click first, JS click as fallback).
     * If the band has none, falls back to the first "Show All" of that column on the page.
     */
    private void clickShowAllInNotScoredBand(String column, Locator fallback) {
        // No waiting here on purpose: on the old UI this band never exists and a wait would cost seconds on every call.
        // If the band is not rendered yet, the fallback below waits for the column's own Show All instead.
        Locator bandButtons = new Locator("NOT_SCORED_SHOW_ALL_BUTTONS", new By.ByXPath(notScoredBand(column) + "//button[starts-with(@data-testid,'toggle-collapse-')]"));
        List<WebElement> buttons = driverTools.elements(bandButtons).orElse(new ArrayList<>());
        boolean clicked = false;
        for (WebElement button : buttons) {
            String text = button.getAttribute("textContent");
            if (text != null && text.trim().toLowerCase().startsWith("show")) {
                driverTools.scrollIntoWebViewCenter(button);
                driverTools.clickOrFallbackToClickWithJavascriptExecutor(button);
                clicked = true;
            }
        }
        if (!clicked) {
            WebElement showAllButton = driverTools.waitForElementToPresence(fallback).orElseThrow();
            driverTools.scrollIntoWebViewCenter(showAllButton);
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(showAllButton);
        }
    }

    /**
     * XPath of the "Not scored yet" band of a room-photos column ("include" or "exclude").
     * The FE gives this band {@code data-band=""} (an EMPTY value, while the scored bands use GOOD / BAD) and no
     * dedicated test id, so we match on the empty attribute. This is fragile: if FE adds a test id such as
     * {@code room-photos-band-not-scored}, switch to it here (the only place the band is located).
     */
    private String notScoredBand(String column) {
        return String.format("//div[@data-testid='room-photos-column-%s']//div[@data-testid='room-photos-band'][@data-band='']", column);
    }

    /** New UI: photos of a room row only render once the row is expanded (no-op on the old UI, which has no such toggles). */
    private void expandNotScoredRooms(String column) {
        List<WebElement> toggles = driverTools.elements(new Locator("NOT_SCORED_ROOM_TOGGLES", new By.ByXPath(notScoredBand(column) + "//button[starts-with(@data-testid,'toggle-room-')]"))).orElse(new ArrayList<>());
        for (WebElement toggle : toggles) {
            String label = toggle.getAttribute("aria-label");
            String expanded = toggle.getAttribute("aria-expanded");
            if ("Show".equalsIgnoreCase(label) || "false".equalsIgnoreCase(expanded)) {
                driverTools.scrollIntoWebView(toggle);
                driverTools.clickOrFallbackToClickWithJavascriptExecutor(toggle);
            }
        }
    }

    /** Ticks the first Not Scored Yet room photo of the given column and returns its photo id (null on the old UI). */
    private String checkFirstNotScoredRoomPhoto(String column) {
        expandNotScoredRooms(column);
        // Old UI locator OR new UI locator (scoped to the selected include/exclude column).
        String oldXpath = "(//span[contains(@class,'room-photos-quality-title') and text()='Not Scored Yet']/following-sibling::div//div[@class='room-photo-item'][1]//span[@class='checkbox-style '])[1]";
        String newXpath = "(" + notScoredBand(column) + "//input[starts-with(@data-testid,'room-photo-check-')]/following-sibling::span)[1]";
        WebElement checkbox = driverTools.waitForElementToPresence(new Locator("FIRST_NOT_SCORED_ROOM_PHOTO_CHECKBOX", new By.ByXPath(oldXpath + " | " + newXpath))).orElseThrow();
        String photoId = null;
        try {
            String testId = checkbox.findElement(By.xpath("./preceding-sibling::input")).getAttribute("data-testid");
            photoId = testId == null ? null : testId.replace("room-photo-check-", "");
        } catch (Exception ignored) {
            // old UI: no input/test id to remember
        }
        driverTools.scrollIntoWebView(checkbox);
        driverTools.clickWithJavascriptExecutor(checkbox);
        return photoId;
    }

    /** Ticks one specific Not Scored Yet room photo (by the id returned from checkFirstNotScoredRoomPhoto) in the given column. */
    private void checkNotScoredRoomPhotoById(String column, String photoId) {
        expandNotScoredRooms(column);
        WebElement checkbox = driverTools.waitForElementToPresence(new Locator("NOT_SCORED_ROOM_PHOTO_CHECKBOX_BY_ID", new By.ByXPath(String.format("%s//input[@data-testid='room-photo-check-%s']/following-sibling::span", notScoredBand(column), photoId)))).orElseThrow();
        driverTools.scrollIntoWebView(checkbox);
        driverTools.clickWithJavascriptExecutor(checkbox);
    }

    private void showAllForColumn(String column) {
        if ("include".equalsIgnoreCase(column)) {
            clickShowAllIncludeButton();
        } else {
            clickShowAllExcludeButton();
        }
    }

    /** Ticks the first "Not Scored Yet" room photo in the given column ("include" or "exclude") and returns its id. */
    public String checkFirstNotScoredRoomPhotoFrom(String column) {
        showAllForColumn(column);
        return checkFirstNotScoredRoomPhoto(column);
    }

    /** Ticks the given "Not Scored Yet" room photo (by id) in the given column. */
    public void checkNotScoredRoomPhotoFrom(String column, String photoId) {
        showAllForColumn(column);
        checkNotScoredRoomPhotoById(column, photoId);
    }

    /**
     * Cleanup helper: moves a Not Scored Yet room photo from Exclude back to Include. Best effort and never throws,
     * so it cannot hide the real test failure when used in a {@code finally} block.
     */
    public void restoreRoomPhotoToInclude(String photoId) {
        try {
            checkNotScoredRoomPhotoFrom("exclude", photoId);
            clickEditPhoto();
            selectIncludeExcludeConfiguration("Include");
            clickSubmitMoveHotelImage();
            clickSaveRoomPhotos();
            logger.log("restoreRoomPhotoToInclude: moved photo " + photoId + " back to Include");
        } catch (Exception e) {
            logger.log("WARNING restoreRoomPhotoToInclude failed for photo " + photoId + ": " + e);
        }
    }

    public void clickSaveRoomPhotos() {
        WebElement saveRoomPhotosButton = driverTools.waitForElementToPresence(ROOM_PHOTOS_SAVE_BUTTON).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(saveRoomPhotosButton);
    }

    public void checkRoomVendorImageByUrl(String roomName, String url) {
        driverTools.scrollIntoWebView(getRoomGroupCardByRoomName(roomName));
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getRoomVendorImageCheckboxByRoomNameAndImageUrl(roomName, url));
    }
    public void checkRoomVendorImageByRoomNameAndVendorRoomAndUrl(String roomName, String vendorRoomName, String url) {
        driverTools.scrollIntoWebView(getRoomGroupCardByRoomName(roomName));
        driverTools.scrollIntoWebView(getRoomVendorCardByRoomNameAndRoomVendorName(roomName, vendorRoomName));
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getRoomVendorImageCheckboxByRoomNameAndImageUrl(roomName, url));
    }
    public List<String> getUrlsFromImageStyles(Locator imageLocator) {
        List<String> extractedUrls = new ArrayList<>();
        try {
            driverTools.waitForElementsToAppear(imageLocator);
        } catch (Exception e) {
            logger.log("No images found for locator: " + imageLocator);
            return extractedUrls;
        }
        List<WebElement> elements = driverTools.elements(imageLocator).orElse(new ArrayList<>());
        for (WebElement element : elements) {
            driverTools.scrollIntoWebView(element);
            String url = element.getAttribute("src");
            if (url == null || url.isEmpty()) {
                String style = element.getCssValue("background-image");
                if (style != null && style.contains("url(")) {
                    int startIdx = style.indexOf("url(") + 4;
                    int endIdx = style.indexOf(")", startIdx);
                    if (endIdx > startIdx) {
                        url = style.substring(startIdx, endIdx).replace("&quot;", "").replace("\"", "").replace("'", "").trim();
                    }
                }
            }
            if (url != null && !url.isEmpty() && !url.equalsIgnoreCase("none")) {
                extractedUrls.add(url);
            }
        }
        logger.log("Extracted image URLs: " + extractedUrls);
        return extractedUrls;
    }
    public List<String> getAllRoomVendorImageUrlsByRoomNameAndVendorRoomName(String roomName, String vendorRoomName) {
        return getUrlsFromImageStyles(getRoomVendorImageCheckboxByRoomNameAndVendorRoomName(roomName, vendorRoomName));
    }
    public List<String> getAllRoomGroupImageUrlsByRoomName(String roomName) {
        return getUrlsFromImageStyles(getRoomGroupImageCheckboxByRoomName(roomName));
    }

    public boolean isRoomImageInRoomGroupSectionByImageUrl(String roomName, String url) {
        driverTools.scrollIntoWebView(getRoomGroupCardByRoomName(roomName));
        return !getRoomGroupImageByRoomNameAndImageUrl(roomName, url).isEmpty();
    }

    /** Waits for the photo to show up in the room group section after Add; if the list did not update in place, reloads once and looks again. */
    public boolean waitForRoomImageInRoomGroupSectionByImageUrl(String roomName, String url) {
        boolean found = driverTools.waitUntil(d -> isRoomImageInRoomGroupSectionByImageUrl(roomName, url) ? Boolean.TRUE : null, 8).isPresent();
        if (found) {
            return true;
        }
        refreshPage();
        showAllRoomImageByRoomName(roomName);
        return isRoomImageInRoomGroupSectionByImageUrl(roomName, url);
    }

    public boolean isIncludeExcludeRoomGroupImageModalDisplayed() {
        return driverTools.waitForElementToAppear(MANAGE_ROOM_GROUP_PHOTOS_INCLUDE_EXCLUDE_MODAL, 10).isPresent();
    }

    /** Removes the vendor photo from the room group section (refresh, expand, tick, Remove, confirm). Returns true if it was in the group and the removal was confirmed. */
    public boolean removeRoomGroupImageByUrl(String roomName, String url) {
        refreshPage();
        showAllRoomImageByRoomName(roomName);
        if (!isRoomImageInRoomGroupSectionByImageUrl(roomName, url)) {
            return false;
        }
        checkRoomGroupImageByUrl(url);
        clickRemoveRoomImage();
        if (!isIncludeExcludeRoomGroupImageModalDisplayed()) {
            return false;
        }
        clickConfirmIncludeExcludeRoomGroupImage();
        return isSuccessfulToastSnackbarDisplayed();
    }

    public void clickConfirmIncludeExcludeRoomGroupImage() {
        WebElement manageRoomPhotosIncludeExcludeConfirmButton = driverTools.waitForElementToAppear(MANAGE_ROOM_GROUP_PHOTOS_INCLUDE_EXCLUDE_CONFIRM_BUTTON, 10).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(manageRoomPhotosIncludeExcludeConfirmButton);
    }

    public void clickRoomGroupArrangePhoto(String roomName) {
        WebElement arrangeButton = getRoomGroupArrangePhotoButtonByRoomName(roomName);
        driverTools.scrollIntoWebViewCenter(arrangeButton);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(arrangeButton);
    }

    public boolean isRoomGroupArrangePhotoModalDisplayed() {
        return driverTools.isElementDisplayed(driverTools.element(MANAGE_ROOM_GROUP_PHOTOS_ROOM_GROUP_ARRANGE_PHOTO_MODAL).orElseThrow());
    }

    public String getRoomGroupImageIdentifierByIndex(int index) {
        return getRoomGroupImageArrangeItemByIndex(index).getAttribute("data-testid");
    }

    public void dragAndDropRoomGroupImage(int sourceIndex, int targetIndex) {
        String originalSourceId = getRoomGroupImageIdentifierByIndex(sourceIndex);
        driverTools.dragAndDropWithSteps(getRoomGroupImageInArrangePhotoModalByIndex(sourceIndex), getRoomGroupImageInArrangePhotoModalByIndex(targetIndex));
        if (!waitUntilArrangeIndexChanged(() -> getRoomGroupImageIdentifierByIndex(sourceIndex), originalSourceId)) {
            logger.log("Pointer drag did not reorder room group images, retrying with keyboard");
            driverTools.dragAndDropWithKeyboard(getRoomGroupImageInArrangePhotoModalByIndex(sourceIndex), targetIndex - sourceIndex);
            waitUntilArrangeIndexChanged(() -> getRoomGroupImageIdentifierByIndex(sourceIndex), originalSourceId);
        }
    }

    public boolean isRoomGroupImageHasIdentifierByIndex(int index, String expected) {
        return getRoomGroupImageIdentifierByIndex(index).equals(expected);
    }

    public boolean isRoomGroupImagePinned(int index) {
        try {
            String pressed = driverTools.getValueAttribute(arrangePinButton(GROUP_ARRANGE_ITEM_XPATH, index), "aria-pressed");
            return "true".equals(pressed);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRoomGroupImageUnpinned(int index) {
        try {
            String pressed = driverTools.getValueAttribute(arrangePinButton(GROUP_ARRANGE_ITEM_XPATH, index), "aria-pressed");
            return "false".equals(pressed);
        } catch (Exception e) {
            return false;
        }
    }

    public void clickRoomGroupImagePinIcon(int index, boolean isExpectedlyPinned) {
        driverTools.scrollIntoWebViewCenter(getRoomGroupImageArrangeItemByIndex(index));
        setArrangePinState(() -> arrangePinButton(GROUP_ARRANGE_ITEM_XPATH, index), isExpectedlyPinned);
    }

    public void clickRoomVendorArrangePhoto(String roomName, String vendorName) {
        driverTools.scrollIntoWebView(getRoomVendorArrangePhotoButtonByRoomNameAndVendorName(roomName, vendorName));
        WebElement button = driverTools.waitForElementToBeClickable(getRoomVendorArrangePhotoButtonByRoomNameAndVendorName(roomName, vendorName), 3).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(button);
    }

    public boolean isRoomVendorArrangePhotoModalDisplayed() {
        return driverTools.isElementDisplayed(driverTools.element(MANAGE_ROOM_GROUP_PHOTOS_ROOM_VENDOR_ARRANGE_PHOTO_MODAL).orElseThrow());
    }

    public String getRoomVendorImageIdentifierByIndex(int index) {
        return getRoomVendorImageArrangeItemByIndex(index).getAttribute("data-testid");
    }

    public void dragAndDropRoomVendorImage(int sourceIndex, int targetIndex) {
        String originalSourceId = getRoomVendorImageIdentifierByIndex(sourceIndex);
        driverTools.dragAndDropWithSteps(getRoomVendorImageInArrangePhotoModalByIndex(sourceIndex), getRoomVendorImageInArrangePhotoModalByIndex(targetIndex));
        if (!waitUntilArrangeIndexChanged(() -> getRoomVendorImageIdentifierByIndex(sourceIndex), originalSourceId)) {
            logger.log("Pointer drag did not reorder room vendor images, retrying with keyboard");
            driverTools.dragAndDropWithKeyboard(getRoomVendorImageInArrangePhotoModalByIndex(sourceIndex), targetIndex - sourceIndex);
            waitUntilArrangeIndexChanged(() -> getRoomVendorImageIdentifierByIndex(sourceIndex), originalSourceId);
        }
    }

    private boolean waitUntilArrangeIndexChanged(java.util.function.Supplier<String> currentId, String originalId) {
        return driverTools.waitUntil(d -> {
            try {
                String now = currentId.get();
                return now != null && !now.equals(originalId) ? Boolean.TRUE : null;
            } catch (Exception e) {
                return null; // grid re-rendering, keep polling
            }
        }, 3).isPresent();
    }

    public boolean isRoomVendorImageHasIdentifierByIndex(int index, String expected) {
        return getRoomVendorImageIdentifierByIndex(index).equals(expected);
    }

    /** Single read, no waiting: for deciding whether a leftover pin from an earlier run needs clearing. */
    public boolean isRoomVendorImagePinnedNow(int index) {
        try {
            return "true".equals(driverTools.getValueAttribute(arrangePinButton(VENDOR_ARRANGE_ITEM_XPATH, index), "aria-pressed"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRoomVendorImagePinned(int index) {
        return waitForVendorPinState(index, true);
    }

    public boolean isRoomVendorImageUnpinned(int index) {
        return waitForVendorPinState(index, false);
    }

    /** Polls aria-pressed for up to 10s, because the arrange grid re-renders after a save/refresh. */
    private boolean waitForVendorPinState(int index, boolean expectedPinned) {
        return driverTools.waitUntil(d -> {
            try {
                String pressed = driverTools.getValueAttribute(arrangePinButton(VENDOR_ARRANGE_ITEM_XPATH, index), "aria-pressed");
                return expectedPinned == "true".equals(pressed) ? Boolean.TRUE : null;
            } catch (Exception e) {
                return null;
            }
        }, 10).isPresent();
    }

    public void clickRoomVendorImagePinIcon(int index, boolean isExpectedlyPinned) {
        driverTools.scrollIntoWebViewCenter(getRoomVendorImageArrangeItemByIndex(index));
        setArrangePinState(() -> arrangePinButton(VENDOR_ARRANGE_ITEM_XPATH, index), isExpectedlyPinned);
    }

    private static final String GROUP_ARRANGE_ITEM_XPATH = "(//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'room-group-image-panel-')]//div[@data-testid='arrange-photo-grid']/div[starts-with(@data-testid,'arrange-photo-')])[%d]";
    private static final String VENDOR_ARRANGE_ITEM_XPATH = "(//div[@data-testid='manage-room-photos-list']//div[starts-with(@data-testid,'room-section-')]//div[@data-testid='arrange-photo-grid']/div[starts-with(@data-testid,'arrange-photo-')])[%d]";

    private WebElement arrangePinButton(String itemXpath, int index) {
        return driverTools.waitForElementToPresence(new Locator("ARRANGE_PIN_BUTTON",
                new By.ByXPath(String.format(itemXpath, index) + "//button[starts-with(@data-testid,'arrange-photo-pin-')]"))).orElseThrow();
    }

    /** Sets the pin to the wanted state and verifies aria-pressed actually flipped, retrying with a JS click. */
    private void setArrangePinState(java.util.function.Supplier<WebElement> pinButton, boolean wantPinned) {
        for (int attempt = 0; attempt < 3; attempt++) {
            boolean pinned = "true".equals(driverTools.getValueAttribute(pinButton.get(), "aria-pressed"));
            if (pinned == wantPinned) {
                return;
            }
            WebElement button = pinButton.get();
            if (attempt == 0) {
                driverTools.clickOrFallbackToClickWithJavascriptExecutor(button);
            } else {
                driverTools.clickWithJavascriptExecutor(button);
            }
            boolean expected = wantPinned;
            boolean flipped = driverTools.waitUntil(d -> {
                try {
                    return expected == "true".equals(driverTools.getValueAttribute(pinButton.get(), "aria-pressed")) ? Boolean.TRUE : null;
                } catch (Exception e) {
                    return null; // item re-rendering, keep polling
                }
            }, 8).isPresent();
            if (flipped) {
                return;
            }
        }
        logger.log("Arrange pin did not reach the expected state (pinned=" + wantPinned + ")");
    }

    public void clickSaveRoomImageArrangePhoto() {
        WebElement saveButton = driverTools.waitForElementToPresence(MANAGE_ROOM_GROUP_PHOTOS_ARRANGE_PHOTO_SAVE_BUTTON).orElseThrow();
        driverTools.scrollIntoWebViewCenter(saveButton);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(saveButton);
    }

    private static final Locator ANY_SNACKBAR_TEXT = new Locator("ANY_SNACKBAR_TEXT", new By.ByXPath("//div[contains(@class,'Snackbar_text_content')]"));

    public boolean isSuccessfulToastSnackbarDisplayed() {
        String[] lastSeen = {""};
        boolean found = driverTools.waitUntil(d -> {
            try {
                if (driverTools.isElementDisplayed(SUCCESSFUL_TOAST_SNACKBAR)) {
                    return Boolean.TRUE;
                }
                for (WebElement toast : driverTools.elements(ANY_SNACKBAR_TEXT).orElse(new ArrayList<>())) {
                    String text = toast.getAttribute("textContent");
                    text = text == null ? "" : text.trim();
                    if (!text.isEmpty()) {
                        lastSeen[0] = text;
                        String lower = text.toLowerCase();
                        if (lower.contains("success") || lower.contains("berhasil")) {
                            return Boolean.TRUE;
                        }
                    }
                }
            } catch (Exception ignored) {
                // snackbar re-rendering, keep polling
            }
            return null;
        }, 15).isPresent();
        if (!found) {
            logger.log("No success snackbar found. Last snackbar text seen: '" + lastSeen[0] + "'");
        }
        return found;
    }

    /** Only for waits that have no observable DOM condition; always say why at the call site. */
    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void waitForSuccessfulToastSnackbarToDisappear() {
        driverTools.waitForElementToDisappear(SUCCESSFUL_TOAST_SNACKBAR, 15);
    }

    public String getSuccessfulToastSnackbarText() {
        // The snackbar can exist in the DOM before its text is rendered (or while animating in),
        // so poll until the text is non-empty instead of reading it once.
        String[] lastSeen = {""};
        driverTools.waitUntil(d -> {
            try {
                for (WebElement toast : driverTools.elements(ANY_SNACKBAR_TEXT).orElse(new ArrayList<>())) {
                    String text = toast.getAttribute("textContent");
                    text = text == null ? "" : text.trim();
                    if (!text.isEmpty()) {
                        lastSeen[0] = text;
                        return Boolean.TRUE;
                    }
                }
            } catch (Exception ignored) {
                // snackbar re-rendering, keep polling
            }
            return null;
        }, 10);
        return lastSeen[0];
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public void clickEditPhoto() {
        driverTools.waitForElementToBeClickable(EDIT_PHOTO_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(EDIT_PHOTO_BUTTON);
    }
}
