package com.tiket.web.page.wishlist;

import com.mongodb.client.FindIterable;
import com.tiket.api.order.InternalOrderDetailApi;
import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.bus.BusSrpOneWayData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static com.tiket.core.CoreFunctions.randomName;
import static com.tiket.testbase.Configs.PLATFORM;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import com.tiket.web.WebPage;
import java.util.List;

public abstract class WishlistLandingWeb<T extends WebDriver> extends WebPage<T> {

    public WishlistLandingWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> WishlistLandingWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new WishlistLandingDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new WishlistLandingMweb<>(session);
            }
            default ->
                throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator CROSS_BUTTON = new Locator("CROSS_BUTTON", By.xpath("//button[contains(@class,'Modal_modal_close')]"));
    private static final Locator OK_BUTTON = new Locator("OK_BUTTON", By.xpath("//button[text()='OK' or text()='Oke']"));
    private static final Locator WISHLIST_HEADING = new Locator("WISHLIST_HEADING", By.xpath("//p[text()='Wishlist']"));
    private static final Locator COLLECTION_HEADING = new Locator("COLLECTION_HEADING", By.xpath("//p[text()='Collection' or text()='Koleksi']"));
    private static final Locator ALL_WISHLIST_HEADING = new Locator("ALL_WISHLIST_HEADING", By.xpath("//*[text()='All Wishlist' or text()='Semua Wishlist']"));
    private static final Locator RECENTLY_HEADING = new Locator("RECENTLY_HEADING", By.xpath("//div[text()='Recently searched' or text()='Pencarian terakhir']"));
    private static final Locator USE_WISHLIST_HEADING = new Locator("USE_WISHLIST_HEADING", By.xpath("//h3[text()='Use Wishlist to the fullest' or text()='Pakai Wishlist semaksimal mungkin']"));
    private static final Locator SEE_ALL_COLLECTION_BUTTON = new Locator("SEE_ALL_COLLECTION_BUTTON", By.xpath("(//*[self::a or self::button or self::span][contains(normalize-space(),'See all') or contains(normalize-space(),'Lihat semua')])[1]"));
    private static final Locator SEE_ALL_WISHLIST_BUTTON = new Locator("SEE_ALL_WISHLIST_BUTTON", By.xpath("(//div[contains(@class,'collection') or contains(@class,'wishlist')]//span[contains(normalize-space(),'See all') or contains(normalize-space(),'Lihat semua')])[2]"));
    private static final Locator CLEAR_BUTTON = new Locator("CLEAR_BUTTON", By.xpath("//button[contains(text(),'Clear') or contains(text(),'Hapus')]"));
    private static final Locator HOTEL_BUTTON = new Locator("HOTEL_BUTTON", By.xpath("//button[contains (@class, 'Tabs_tab')]//*[text()='Hotel' or text()='Hotels']"));
    private static final Locator VILLA_BUTTON = new Locator("VILLA_BUTTON", By.xpath(" //button//span[(contains(text(),'Villa') or contains(text(),'Vila')) and contains(text(),'& Apt')]"));
    private static final Locator VILLA_APT_TAB = new Locator("VILLA_APT_TAB", By.xpath("//button[contains(@class,'Tabs_tab')]//*[(contains(text(),'Villa') or contains(text(),'Vila')) and contains(text(),'& Apt')]"));
    private static final Locator COLLECTION_T0_EDIT = new Locator("COLLECTION_T0_EDIT", By.xpath("//p[contains(text(),'For Automation (Do not Delete/Edit)')]"));
    private static final Locator COLLECTION_THREE_DOTS = new Locator("COLLECTION_THREE_DOTS", By.xpath("//div[contains(@class,'RoundedButton_rounded_button_wrapper')]"));
    private static final Locator EDIT_COVER_AND_COLLECTION_NAME_OPTION = new Locator("EDIT_COVER_AND_COLLECTION_NAME_OPTION", By.xpath("//*[contains(@class,'ManageCollection_menu_text') and contains(text(),'Edit')]"));
    private static final Locator COLLECTION_NAME_INPUT = new Locator("COLLECTION_NAME_INPUT", By.xpath("//textarea[@id='collection-name' or @id='nama-koleksi']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[contains(@class, 'CreateEditCollectionPopup_save_button')]"));
    private static final Locator EDIT_COLLECTION_SNACKBAR = new Locator("SAVE_BUTTON", By.xpath("//div[text()='Cover and collection name edited.' or text()='Sampul dan nama koleksi diubah.']"));
    private static final Locator DELETE_COLLECTION_SNACKBAR = new Locator("DELETE_COLLECTION_SNACKBAR", By.xpath("//div[contains(text(),'removed') or contains(text(),'dihapus')]"));
    private static final Locator MOVE_COLLECTION_SNACKBAR = new Locator("MOVE_COLLECTION_SNACKBAR", By.xpath("//div[contains(text(),'Moved') or contains(text(),'Dipindah')]"));
    private static final Locator BACK_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//span[text()='Back' or text()='Kembali']"));
    private static final Locator WISHLIST_HEART_ICON = new Locator("WISHLIST_HEART_ICON", By.xpath("//button//span[contains(@class,'IconWishlistAnimated_wishlist')]"));
    private static final Locator MANAGE_CTA = new Locator("MANAGE_CTA", By.xpath("//button[text()='Manage' or text()='Atur']"));
    private static final Locator VIEW_ALL_WISHLIST = new Locator("VIEW_ALL_WISHLIST", By.xpath("//span[text()='View All Wishlist' or text()='Lihat Semua Wishlist']"));
    private static final Locator ALL_WISHLIST_CARD = new Locator("ALL_WISHLIST_CARD", By.xpath("(//p[contains(@class,'ProductInfo_product_title')])[1]"));
    private static final Locator REMOVE_SNACKBAR = new Locator("REMOVE_SNACKBAR", By.xpath("//div[text()='Removed from All Wishlist.' or text()='Dihapus dari Semua Wishlist.']"));
    private static final Locator EMPTY_COLLECTION_HEADING = new Locator("EMPTY_COLLECTION_HEADING", By.xpath("(//p[contains(@class,'Text_size_b1') and (contains(normalize-space(.),'start adding wishlist') or contains(normalize-space(.),'mulai tambah wishlist'))])[1]"));
    private static final Locator HOTEL_ENTRY_POINT = new Locator("HOTEL_ENTRY_POINT", By.xpath("(//p[text()='Hotel' or text()='Hotels'])[1]"));
    private static final Locator HOMES_ENTRY_POINT = new Locator("HOMES_ENTRY_POINT", By.xpath("(//p[text()='Villas & Apt.' or text()='Vila & Apt.'])[1]"));
    private static final Locator FILLED_COLLECTION = new Locator("FILLED_COLLECTION", By.xpath("//p[contains(text(),'For Automation 2 (Do not Delete/Edit)')]"));
    private static final Locator PRODUCT_CARD = new Locator("PRODUCT_CARD", By.xpath("(//p[contains(@class,'ProductInfo_product_title')])[1]"));
    private static final Locator PRODUCTS_COUNT = new Locator("PRODUCTS_COUNT", By.xpath("(//div[contains(@class,'Tabs_tab_panel_active')]//span[contains(@class,'WishlistFilter_amount')])[1]"));
    private static final Locator WISHLIST_SIDE_MENU = new Locator("WISHLIST_SIDE_MENU", By.xpath("//*[contains(@class,'Sidebar_text') and text()='Wishlist']"));
    private static final Locator ALL_WISHLIST_THREE_DOTS = new Locator("ALL_WISHLIST_THREE_DOTS", By.xpath("(//*[local-name()='svg' and @viewBox='0 0 24 24'])[20]"));
    private static final Locator ADD_TO_COLLECTION_MENU_BUTTON = new Locator("ADD_TO_COLLECTION_MENU_BUTTON", By.xpath("(//div[contains(@class,'ManageWishlist_menu_item')])[1]"));
    private static final Locator REMOVE_FROM_COLLECTION_MENU_BUTTON = new Locator("REMOVE_FROM_COLLECTION_MENU_BUTTON", By.xpath("(//div[contains(@class,'ManageWishlist_menu_item')])[2]"));
    private static final Locator SNACKBAR_TEXT_AFTER_ADDING = new Locator("SNACKBAR_TEXT", By.xpath("//div[contains(text(),'Added') or contains(text(),'Ditambah')]"));
    private static final Locator HOTEL_TAB = new Locator("HOTEL_TAB", By.xpath("//button[span[contains(text(), 'Hotels') or contains(text(), 'Hotel')]]"));
    private static final Locator VILLA_TAB = new Locator("VILLA_TAB", By.xpath("//button[span[contains(text(), 'Villas & Apt.') or contains(text(), 'Vila & Apt.')]]"));
    private static final Locator TODO_TAB = new Locator("TODO_TAB", By.xpath("//button[span[contains(text(), 'To Dos') or contains(text(), 'To Do')]]"));
    private static final Locator WISHLIST_PRODUCT = new Locator("WISHLIST_PRODUCT", By.xpath("(//div[contains(@class,'WishCard_thumbnail_product_info')])[1]"));
    private static final Locator WISHLIST_PRODUCT_INFO = new Locator("WISHLIST_PRODUCT_INFO", By.xpath("(//p[contains(@class,'ProductInfo_product_title')])[1]"));
    private static final Locator WISHLIST_PRODUCT_PAGE_HEADING = new Locator("WISHLIST_PRODUCT_PAGE_HEADING", By.xpath("(//h1[contains(@class,'Text_text')])"));
    private static final Locator SNACKBAR_TEXT_AFTER_REMOVING = new Locator("SNACKBAR_TEXT_AFTER_REMOVING", By.xpath("//div[contains(text(),'Removed') or contains(text(),'Dihapus')]"));
    private static final Locator PRODUCT_IN_WISHLIST = new Locator("PRODUCT_IN_WISHLIST", By.xpath("//span[contains(@class,'Radio_icon__')]"));
    private static final Locator ADD_TO_COLLECTION_BUTTON = new Locator("ADD_TO_COLLECTION_BUTTON", By.xpath("//*[contains(@class,'Modal_modal_footer')]/*/button"));
    private static final Locator CREATE_COLLECTION_BUTTON = new Locator("CREATE_COLLECTION_BUTTON", new By.ByXPath("//*[contains(@class,'Collection_create_button_wrapper')]"));
    private static final Locator MANAGE_DELETE_BUTTON = new Locator("MANAGE_DELETE_BUTTON", new By.ByXPath("//span[contains(text(),'Delete') or contains(text(), 'Hapus')]"));
    private static final Locator POPUP_DELETE_BUTTON = new Locator("POPUP_DELETE_BUTTON", new By.ByXPath("//span[contains(text(),'Delete') or contains(text(),'Hapus')]"));
    private static final Locator SAVE_COLLECTION_BUTTON = new Locator("SAVE_COLLECTION_BUTTON", new By.ByXPath("//button[contains(@class,'CreateEditCollectionPopup_save_button')]"));
    private static final Locator ALL_COLLECTION_NAME = new Locator("ALL_COLLECTION_NAME", new By.ByXPath("//div[contains (@class, 'Collection_card_wrapper')]//p[contains (@class, 'Text_size_b2')]"));
    private static final Locator ELLIPSIS_SAVED_HOTELS = new Locator("ELLIPSIS_SAVED_HOTELS", new By.ByXPath("(//*[local-name()='svg' and .//*[@id='more-vertical-qqa26pmcia']])[2]"));
    private static final Locator BUTTON_MOVE_TO_ANOTHER_COLLECTION = new Locator("BUTTON_MOVE_TO_ANOTHER_COLLECTION", new By.ByXPath("//div[contains(@class, 'ManageWishlist_menu_item')]//*[contains(text(),'Move') or contains(text(),'Pindahin')]"));
    private static final Locator LIST_OF_COLLECTION_MOVE = new Locator("LIST_OF_COLLECTION_MOVE", new By.ByXPath("//span[contains (@class, 'CollectionItem_collection_item_name')]"));
    private static final Locator BUTTON_CONFIRM_MOVE_TO_ANOTHER_COLLECTION = new Locator("BUTTON_CONFIRM_MOVE_TO_ANOTHER_COLLECTION", new By.ByXPath("(//button[contains (@class, ' Button_variant_primary')])[5]"));
    private static final Locator BUTTON_CHANGE_DATE = new Locator("BUTTON_CHANGE_DATE", new By.ByXPath("//div[contains(@class, 'ManageWishlist_menu_item')]//*[contains(text(),'Change date') or contains(text(),'Ubah tanggal')]"));
    private static final Locator BUTTON_CHANGE_GUEST = new Locator("BUTTON_CHANGE_GUEST", new By.ByXPath("//div[contains(@class, 'ManageWishlist_menu_item')]//*[contains(text(),'Change guest') or contains(text(),'Ubah tamu')]"));
    private static final Locator AVAILABLE_DATE = new Locator("AVAILABLE_DATE", new By.ByXPath("//span[contains (@class, 'Day_day_number_container')]//span[contains (@class, 'Text_variant_highEmphasis')]"));
    private static final Locator HOTEL_DATE = new Locator("HOTEL_DATE", new By.ByXPath("(//div[@data-testid='icon-description-1-0']//div[contains(@class, 'InfoEngineRenderer_truncated_text_inline')])[2]"));
    private static final Locator HOTEL_GUEST = new Locator("HOTEL_GUEST", new By.ByXPath("(//div[contains (@class, 'Tabs_tab_panel_active')]//div[@data-testid='icon-description-2-1']//div[contains(@class, 'InfoEngineRenderer')])[3]"));
    private static final Locator BUTTON_ADD_GUEST = new Locator("BUTTON_ADD_GUEST", new By.ByXPath("//*[contains (@class, 'QuantityEditor_plus_icon')]"));
    private static final Locator ADULTS_PLUS_BUTTON = new Locator("ADULTS_PLUS_BUTTON", new By.ByXPath("//div[@data-testid='adults-section']//button[.//*[contains(@class,'QuantityEditor_plus_icon')]]"));
    private static final Locator ADULTS_MINUS_BUTTON = new Locator("ADULTS_MINUS_BUTTON", new By.ByXPath("//div[@data-testid='adults-section']//button[.//*[contains(@class,'QuantityEditor_minus_icon')]]"));
    private static final Locator ADULTS_INPUT = new Locator("ADULTS_INPUT", new By.ByXPath("//div[@data-testid='adults-section']//input[contains(@class,'QuantityEditor_quantity_input')]"));
    private static final Locator ROOM_PLUS_BUTTON = new Locator("ROOM_PLUS_BUTTON", new By.ByXPath("//div[@data-testid='room-section']//button[.//*[contains(@class,'QuantityEditor_plus_icon')]]"));
    private static final Locator ROOM_MINUS_BUTTON = new Locator("ROOM_MINUS_BUTTON", new By.ByXPath("//div[@data-testid='room-section']//button[.//*[contains(@class,'QuantityEditor_minus_icon')]]"));
    private static final Locator ROOM_INPUT = new Locator("ROOM_INPUT", new By.ByXPath("//div[@data-testid='room-section']//input[contains(@class,'QuantityEditor_quantity_input')]"));
    private static final Locator ADD_COVER_TOGGLE = new Locator("ADD_COVER_TOGGLE", new By.ByXPath("//*[contains(@class, 'Toggle_knob')]"));
    private static final Locator SINGLE_IMAGE = new Locator("SINGLE_IMAGE", new By.ByXPath("//img[contains(@class, 'CreateEditCollectionPopup')]"));
    private static final Locator MANAGE_COLLECTION = new Locator("MANAGE_COLLECTION", new By.ByXPath("//*[contains(text(),'Manage') or contains(text(), 'Atur')]"));
    private static final Locator MODAL_CROSS_COLLECTION = new Locator("MODAL_CROSS_COLLECTION", new By.ByXPath("//*[contains(@class,'Collection_close_button')]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("//button[contains(text(),'Delete') or contains(text(),'Hapus')]"));
    private static final Locator CHECK_BOX = new Locator("CHECK_BOX", new By.ByXPath("(//div[contains(@class, 'WishCard_thumbnail_product_info')])[1]"));
    private static final Locator PROMO_VOUCHER = new Locator("PROMO_VOUCHER", new By.ByXPath("//button[@data-testid='modal-close-button']/*[local-name() = 'svg']"));
    private static final Locator PROMO_VOUCHER_MWEB = new Locator("PROMO_VOUCHER", new By.ByXPath("//button[@data-qa-id='secondaryCtaClose-button']"));
    private static final Locator ADD_COLLECTION = new Locator("ADD_COLLECTION", new By.ByXPath("//span[text()='For Add product to collection do not delete']"));
    private static final Locator ADD_BUTTON = new Locator("ADD_BUTTON", new By.ByXPath("//button[contains(@class, 'add_to_collection_button')]"));
    private static final Locator PROMO_BANNER = new Locator("PROMO_BANNER", new By.ByXPath("//button[@data-role='secondaryCtaClose']"));
    private static final Locator SAVE_BUTTON_MWEB = new Locator("SAVE_BUTTON_MWEB", new By.ByXPath("//button[text()='Save' or text()='Simpan']"));
    private static final Locator HOTEL_BUTTON_WEB = new Locator("HOTEL_BUTTON_DWEB", By.xpath("//button[contains (@class, 'Tabs_tab')]//*[text()='Hotel' or text()='Hotels']"));
    private static final Locator HOTEL_BUTTON_MWEB = new Locator("HOTEL_BUTTON_MWEB", By.xpath("(//button[contains (@class, 'Tabs_tab')]//*[text()='Hotel' or text()='Hotels'])[2]"));
    private static final Locator WISHLIST_HEART_ICON_MWEB = new Locator("WISHLIST_HEART_ICON_MWEB", By.xpath("//span[contains(@class,'WishIcon_icon_wishlist')]"));
    private static final Locator ALL_TABBINGS_BUTTON = new Locator("ALL_TABBINGS_BUTTON", By.xpath("//button[contains(@class, 'Tabs_tab')]//*[text()='All' or text()='Semua']"));
    private static final Locator VIEW_CTA = new Locator("VIEW_CTA", By.xpath("//button[text()='View' or text()='Lihat']"));
    private static final Locator PLACEHOLDER_COLLECTION = new Locator("PLACEHOLDER_COLLECTION", By.xpath("//span[text()='[DO NOT DELETE] Placeholder Collection Daffa 1']"));
    private static final Locator DONE_BUTTON = new Locator("DONE_BUTTON", By.xpath("//button[contains(@class, 'Button_button')]//span[contains(text(), 'Selesai') or contains(text(), 'Done')]"));
    private static final Locator MANAGE_WISHLIST = new Locator("MANAGE_WISHLIST", By.xpath("(//span[contains(text(),'Manage') or contains(text(),'Atur')])[2]"));
    private static final Locator MANAGE_WISHLIST_CHECKBOX = new Locator("MANAGE_WISHLIST_CHECKBOX", By.xpath("(//div[contains(@class,'Tabs_tab_panel_active')]//div[contains(@class,'CollectionTabContent_cards_list__')]//div[contains(@class,'WishCard_wish_card_button__') and (contains(@class,'WishCard_mobile_wish_card_button__') or contains(@class,'WishCard_desktop_wish_card_button__'))]//label[contains(@class,'Check_check__')]//span[contains(@class,'Check_custom_checkbox__')])[1]"));
    private static final Locator MANAGE_WISHLIST_BUTTON = new Locator("MANAGE_WISHLIST_BUTTON", By.xpath("(//span[contains(text(),'Manage Wishlist') or contains(text(),'Atur Wishlist')])[2]"));
    private static final Locator PDP_HEART_ACTIVE = new Locator("PDP_HEART_ACTIVE", By.xpath("//button[contains(@class,'wishlist_icon_btn')][.//*[name()='circle' and @fill-opacity='1']]"));

    public void clickManageButton() throws InterruptedException {
        driverTools.waitForElementToAppear(MANAGE_COLLECTION);
        driverTools.clickWithJavascriptExecutor(MANAGE_COLLECTION);
    }

    public void clickModalCrossCollection() {
        driverTools.waitForElementToAppear(MODAL_CROSS_COLLECTION);
        driverTools.clickWithJavascriptExecutor(MODAL_CROSS_COLLECTION);
    }

    public void clickDeleteButton() {
        driverTools.waitForElementToAppear(DELETE_BUTTON);
        driverTools.clickWithJavascriptExecutor(DELETE_BUTTON);
    }

    public void addCoverToggleOn() {
        driverTools.waitForElementToAppear(ADD_COVER_TOGGLE);
        driverTools.clickWithJavascriptExecutor(ADD_COVER_TOGGLE);
    }

    public void deleteCollection() throws InterruptedException {
        clickManageButton();
        clickModalCrossCollection();
        clickDeleteButton();
    }

    public void addImageToCollection() {
        driverTools.waitForElementToAppear(SINGLE_IMAGE);

        WebElement element;
        List<WebElement> webElementList = driverTools.elements(SINGLE_IMAGE).orElseThrow();
        logger.log("Total package: " + webElementList.size());
        int randomInt = generateRandomInt(0, webElementList.size() - 1);
        element = webElementList.get(randomInt);

        driverTools.clickWithJavascriptExecutor(element);
    }

    public void addCollection(String collectionName) {
        clickCreateCollectionButton();
        addCollectionName(collectionName);
        addCoverToggleOn();
        addImageToCollection();
        clickSaveButton();
    }

    public void createCollectionWithCover(String collectionName) throws InterruptedException {

        if (driverTools.waitForElementToAppear(MANAGE_COLLECTION).isPresent()) {
            deleteCollection();
            clickBackButton();
        }

        addCollection(collectionName);

    }

    public abstract Locator getCollectionName(String name);

    public Locator getCollectionNameOutside(String name) {
        // Use toXpathLiteral so names containing an apostrophe don't break the xpath, and match on the
        // collection name element by its trimmed text rather than any node, to avoid incidental matches.
        return new Locator("COLLECTION_NAME_AFTER_EDIT",
                By.xpath("//*[normalize-space(text())=" + toXpathLiteral(name) + "]"));
    }

    public abstract void clickCross();

    public boolean checkWishlistHeading() {
        driverTools.waitForElementsToAppear(WISHLIST_HEADING);
        return driverTools.isElementDisplayed(WISHLIST_HEADING);
    }

    public boolean checkCollectionHeading() {
        driverTools.waitForElementsToAppear(COLLECTION_HEADING);
        return driverTools.isElementDisplayed(COLLECTION_HEADING);
    }

    public boolean checkAllWishlistHeading() throws InterruptedException {
        driverTools.waitForElementsToAppear(ALL_WISHLIST_HEADING);
        return driverTools.isElementDisplayed(ALL_WISHLIST_HEADING);
    }

    public boolean checkRecentlySearchedHeading() {
        driverTools.waitForElementsToAppear(RECENTLY_HEADING);
        return driverTools.isElementDisplayed(RECENTLY_HEADING);
    }

    public boolean checkUseWishlistHeading() {
        driverTools.waitForElementsToAppear(USE_WISHLIST_HEADING);
        return driverTools.isElementDisplayed(USE_WISHLIST_HEADING);
    }

    public boolean checkCollectionSeeAllButton() throws InterruptedException {
        driverTools.waitForElementsToAppear(SEE_ALL_COLLECTION_BUTTON);
        return driverTools.isElementDisplayed(SEE_ALL_COLLECTION_BUTTON);
    }

    public boolean checkWishlistSeeAllButton() {
        driverTools.waitForElementsToAppear(SEE_ALL_WISHLIST_BUTTON);
        return driverTools.isElementDisplayed(SEE_ALL_WISHLIST_BUTTON);
    }

    public boolean checkClearButton() {
        driverTools.waitForElementsToAppear(CLEAR_BUTTON);
        return driverTools.isElementDisplayed(CLEAR_BUTTON);
    }

    public boolean checkHotelButton() {
        driverTools.waitForElementsToAppear(HOTEL_BUTTON);
        return driverTools.isElementDisplayed(HOTEL_BUTTON);
    }

    public boolean checkVillaButton() {
        driverTools.waitForElementsToAppear(VILLA_BUTTON);
        return driverTools.isElementDisplayed(VILLA_BUTTON);
    }

    public void clickCollectionSeeAll() throws InterruptedException {
        boolean isFound = driverTools.isElementDisplayed(SEE_ALL_COLLECTION_BUTTON);
    
        if (!isFound) {
            for (int i = 0; i < 4; i++) {
                driverTools.scroll(1, 250);
                if (driverTools.isElementDisplayed(SEE_ALL_COLLECTION_BUTTON)) {
                    isFound = true;
                    break;
                }
            }
        }
    
        if (isFound) {
            try {
                driverTools.click(SEE_ALL_COLLECTION_BUTTON);
            } catch (Exception e) {
                logger.log("Fail regular click, retrying with JS Click for See All Collection");
                driverTools.clickWithJavascriptExecutor(SEE_ALL_COLLECTION_BUTTON);
            }
        } else {
            try {
                driverTools.clickWithJavascriptExecutor(SEE_ALL_COLLECTION_BUTTON);
            } catch (Exception e) {
                throw new RuntimeException("SEE_ALL_COLLECTION_BUTTON tetap tidak ditemukan. Periksa apakah akun sudah memiliki Collection.");
            }
        }
    }

    public void openCollectionToEdit() {
        driverTools.waitForElementsToAppear(COLLECTION_T0_EDIT);
        driverTools.click(COLLECTION_T0_EDIT);
    }

    public void clickThreeDotsOnCollection() {
        driverTools.waitForElementsToAppear(COLLECTION_THREE_DOTS);
        driverTools.click(COLLECTION_THREE_DOTS);
    }

    public void clickEditCollection() {
        driverTools.waitForElementsToAppear(EDIT_COVER_AND_COLLECTION_NAME_OPTION);
        driverTools.click(EDIT_COVER_AND_COLLECTION_NAME_OPTION);
    }

    public void clickThreeDotsOnAllWishlist() {
        driverTools.waitForElementsToAppear(ALL_WISHLIST_THREE_DOTS);
        driverTools.waitForElementToBeClickable(ALL_WISHLIST_THREE_DOTS);
        driverTools.click(ALL_WISHLIST_THREE_DOTS);
    }

    public void fillNewCollectionNameAndSave(String name) {
        WebElement input = driverTools.waitForElementToAppear(COLLECTION_NAME_INPUT).orElseThrow();
        driverTools.waitForElementToAppear(input);
        driverTools.clearInput(input);
        driverTools.sendKeys(input, name);
        driverTools.click(SAVE_BUTTON);
        if (Configs.PLATFORM == PLATFORM.AMWEB || Configs.PLATFORM == PLATFORM.IMWEB) {
            driverTools.click(SAVE_BUTTON);
        }
    }

    public boolean checkEditCollectionSnackbar() {
        driverTools.waitForElementToAppear(EDIT_COLLECTION_SNACKBAR);
        return driverTools.isElementDisplayed(EDIT_COLLECTION_SNACKBAR);
    }

    public boolean checkDeleteCollectionSnackbar() {
        driverTools.waitForElementsToAppear(DELETE_COLLECTION_SNACKBAR);
        return driverTools.isElementDisplayed(DELETE_COLLECTION_SNACKBAR);
    }

    public boolean checkMoveCollectionSnackbar() {
        driverTools.waitForElementsToAppear(MOVE_COLLECTION_SNACKBAR);
        return driverTools.isElementDisplayed(MOVE_COLLECTION_SNACKBAR);
    }

    public boolean checkCollectionName(String name) {
        Locator loc = getCollectionName(name);
        driverTools.waitForElementToAppear(loc, 15).orElseThrow();
        return driverTools.isElementDisplayed(loc);
    }

    public boolean checkCollectionNameOutside(String name) {
        Locator loc = getCollectionNameOutside(name);
        return driverTools.waitForElementToAppear(loc).isPresent();
    }

    public void checkAndOpenCollection(String name) {
        Locator loc = getCollectionNameOutside(name);
        driverTools.waitForElementToBeClickable(loc, 15).orElseThrow();
        driverTools.click(loc);
    }

    public abstract void clickBackButton();

    public void clickDeleteManageCollectionButton() {
        driverTools.waitForElementToAppear(MANAGE_DELETE_BUTTON);
        driverTools.click(MANAGE_DELETE_BUTTON);
        driverTools.waitForElementToAppear(MANAGE_DELETE_BUTTON);
        driverTools.click(MANAGE_DELETE_BUTTON);
    }

    public void clickCreateCollectionButton() {
        driverTools.waitForElementToAppear(CREATE_COLLECTION_BUTTON);
        driverTools.click(CREATE_COLLECTION_BUTTON);
    }

    public void addCollectionName(String collectionName) {
        driverTools.waitForElementToAppear(COLLECTION_NAME_INPUT);
        driverTools.sendKeys(COLLECTION_NAME_INPUT, collectionName);
    }

    public void clickSaveButton() {
        driverTools.waitForElementToAppear(SAVE_COLLECTION_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_COLLECTION_BUTTON);
    }

    public void openAndDeleteNewCollection() throws InterruptedException {
        clickCollectionSeeAll();
        driverTools.waitForElementToAppear(ALL_COLLECTION_NAME);
        List<WebElement> elements = driverTools.elements(ALL_COLLECTION_NAME).get();
        String chosenCollection = driverTools.getText(elements.get(0));
        driverTools.waitForElementToBeClickable(getCollectionNameOutside(chosenCollection));
        driverTools.click(getCollectionNameOutside(chosenCollection));

        clickThreeDotsOnCollection();
        clickDeleteManageCollectionButton();
        logger.log("Deleted collection: " + chosenCollection);
        logger.log("Total collection after delete: " + elements.size());
    }

    public void moveToAnotherCollection() throws InterruptedException {
        clickHotelButton();
        driverTools.waitForElementsToAppear(MANAGE_WISHLIST);
        driverTools.waitForElementToBeClickable(MANAGE_WISHLIST);
        driverTools.click(MANAGE_WISHLIST);
        driverTools.waitForElementsToAppear(MANAGE_WISHLIST_CHECKBOX);
        driverTools.waitForElementToBeClickable(MANAGE_WISHLIST_CHECKBOX);
        driverTools.clickWithJavascriptExecutor(MANAGE_WISHLIST_CHECKBOX);
        driverTools.waitForElementsToAppear(MANAGE_WISHLIST_BUTTON);
        driverTools.waitForElementToBeClickable(MANAGE_WISHLIST_BUTTON);
        driverTools.clickWithJavascriptExecutor(MANAGE_WISHLIST_BUTTON);
        driverTools.waitForElementToAppear(BUTTON_MOVE_TO_ANOTHER_COLLECTION);
        driverTools.click(BUTTON_MOVE_TO_ANOTHER_COLLECTION);

        driverTools.waitForElementToAppear(LIST_OF_COLLECTION_MOVE);
        List<WebElement> collectionElements = driverTools.elements(LIST_OF_COLLECTION_MOVE).get();
        logger.log("Total active collections: " + collectionElements.size());
        driverTools.click(collectionElements.get(0));
        driverTools.waitForElementToBeClickable(BUTTON_CONFIRM_MOVE_TO_ANOTHER_COLLECTION);
        driverTools.click(BUTTON_CONFIRM_MOVE_TO_ANOTHER_COLLECTION);
    }

    public void resetCollection() throws InterruptedException {
        driverTools.waitForElementToAppear(VIEW_CTA);
        driverTools.click(VIEW_CTA);
        clickHotelButton();
        driverTools.waitForElementsToAppear(MANAGE_WISHLIST);
        driverTools.waitForElementToBeClickable(MANAGE_WISHLIST);
        driverTools.click(MANAGE_WISHLIST);
        driverTools.waitForElementsToAppear(MANAGE_WISHLIST_CHECKBOX);
        driverTools.waitForElementToBeClickable(MANAGE_WISHLIST_CHECKBOX);
        driverTools.clickWithJavascriptExecutor(MANAGE_WISHLIST_CHECKBOX);
        driverTools.waitForElementsToAppear(MANAGE_WISHLIST_BUTTON);
        driverTools.waitForElementToBeClickable(MANAGE_WISHLIST_BUTTON);
        driverTools.clickWithJavascriptExecutor(MANAGE_WISHLIST_BUTTON);
        driverTools.waitForElementToAppear(BUTTON_MOVE_TO_ANOTHER_COLLECTION);
        driverTools.click(BUTTON_MOVE_TO_ANOTHER_COLLECTION);
        driverTools.click(PLACEHOLDER_COLLECTION);
        driverTools.waitForElementToBeClickable(BUTTON_CONFIRM_MOVE_TO_ANOTHER_COLLECTION);
        driverTools.click(BUTTON_CONFIRM_MOVE_TO_ANOTHER_COLLECTION);
    }

    public void clickHotelButton() {
        if (Configs.PLATFORM == PLATFORM.AMWEB || Configs.PLATFORM == PLATFORM.IMWEB) {
            driverTools.click(HOTEL_BUTTON_MWEB);
        } else {
            driverTools.click(HOTEL_BUTTON_WEB);
        }
    }

    public Locator getCollectionTabLocator(String tabName) {
        return new Locator("COLLECTION_TAB_" + tabName,
                By.xpath("//button[contains(@class,'Tabs_tab')]//span[" + tabLabelCondition(tabName) + "]"));
    }

    public void openCollectionTab(String tabName) {
        Locator tab = getCollectionTabLocator(tabName);
        driverTools.waitForElementToAppear(tab);
        driverTools.clickWithJavascriptExecutor(tab);
        // Wait until this tab is actually selected (its label gets the active "activity" variant)
        // so the panel switch completes before we interact with the card / three-dots menu.
        Locator activeTab = new Locator("COLLECTION_TAB_ACTIVE_" + tabName,
                By.xpath("//button[contains(@class,'Tabs_tab')]//span[(" + tabLabelCondition(tabName) + ") and contains(@class,'Text_variant_activity')]"));
        driverTools.waitForElementToAppear(activeTab);
        // The panel marks itself active before the cards inside it finish (re)rendering. Give the
        // freshly selected panel a moment to settle so subsequent card reads and the three-dots
        // manage menu act on fully rendered cards (avoids the intermittent "menu never opens").
        sleepQuietly(5000);
    }

    /**
     * Builds an OR-ed {@code normalize-space()} predicate so a tab matches in either language. The
     * test data passes the EN and Bahasa labels as a single slash-separated value (e.g.
     * {@code "Hotels/Hotel"}, {@code "Villas & Apt./Vila & Apt."}); each part is matched exactly.
     */
    private String tabLabelCondition(String tabName) {
        StringBuilder condition = new StringBuilder();
        String[] labels = tabName.split("/");
        for (int i = 0; i < labels.length; i++) {
            String label = labels[i].trim();
            if (label.isEmpty()) {
                continue;
            }
            if (condition.length() > 0) {
                condition.append(" or ");
            }
            condition.append("normalize-space()=").append(toXpathLiteral(label));
        }
        return condition.toString();
    }

    public Locator getManageMenuOptionLocator(String... optionLabels) {
        StringBuilder condition = new StringBuilder();
        for (int i = 0; i < optionLabels.length; i++) {
            if (i > 0) {
                condition.append(" or ");
            }
            condition.append("contains(text(), ").append(toXpathLiteral(optionLabels[i])).append(")");
        }
        return new Locator("MANAGE_MENU_OPTION_" + String.join("/", optionLabels),
                By.xpath("//div[contains(@class,'ManageWishlist_menu_item')]//*[" + condition + "]"));
    }

    private String toXpathLiteral(String value) {
        return value.contains("'")
                ? "concat('" + value.replace("'", "',\"'\",'") + "')"
                : "'" + value + "'";
    }

    /**
     * Builds an xpath for a wishlist product card, matched by its exact title, scoped to the currently
     * active tab panel. The collection page renders the same card in every tab panel (Semua/Hotel/...),
     * but only the active panel is visible; scoping to {@code Tabs_tab_panel_active} guarantees we target
     * the one visible card instead of a hidden duplicate (which would exist in the DOM but never appear).
     * This is order/index independent — it follows whichever tab is active rather than document order.
     */
    private String activeCardContainerXpath(String productTitle) {
        return "//div[contains(@class,'Tabs_tab_panel_active')]//div[contains(@class,'WishCard_wish_card_contents')][.//p[contains(@class,'ProductInfo_product_title') and normalize-space()=" + toXpathLiteral(productTitle) + "]]";
    }

    public String getFirstProductTitle() {
        Locator firstTitle = new Locator("FIRST_PRODUCT_TITLE",
                By.xpath("(//div[contains(@class,'Tabs_tab_panel_active')]//p[contains(@class,'ProductInfo_product_title')])[1]"));
        WebElement element = driverTools.waitForElementToAppear(firstTitle).orElseThrow();
        String title = driverTools.getText(element);
        logger.log("Target product title: " + title);
        return title;
    }

    public Locator getCardDateLocator(String productTitle) {
        return new Locator("CARD_DATE_" + productTitle, By.xpath(activeCardContainerXpath(productTitle)
                + "//div[@data-testid='icon-description-1-0']//div[contains(@class,'InfoEngineRenderer_truncated_text_inline')]"));
    }

    public Locator getCardGuestLocator(String productTitle) {
        return new Locator("CARD_GUEST_" + productTitle, By.xpath(activeCardContainerXpath(productTitle)
                + "//div[@data-testid='icon-description-2-1']//div[contains(@class,'InfoEngineRenderer_truncated_text_inline')]"));
    }

    public Locator getCardRoomLocator(String productTitle) {
        return new Locator("CARD_ROOM_" + productTitle, By.xpath(activeCardContainerXpath(productTitle)
                + "//div[@data-testid='icon-description-2-0']//div[contains(@class,'InfoEngineRenderer_truncated_text_inline')]"));
    }

    public Locator getCardThreeDotsLocator(String productTitle) {
        String xpath = String.format("//div[contains(@class,'WishCard_wish_card') and .//*[contains(normalize-space(),'%s')]]//div[contains(@class,'WishCard_wish_card_button')]//*[local-name()='svg']",productTitle);
        return new Locator("CARD_THREE_DOTS_" + productTitle, By.xpath(xpath));
    }

    /**
     * Reloads the collection page and re-activates {@code tabName} so subsequent card reads happen on
     * the correct, visible panel. A raw {@code refreshWebpage()} reverts the client-side tab selection
     * back to the default ("Semua"/All), which would leave card locators (scoped to
     * {@code Tabs_tab_panel_active}) reading the wrong panel. Call this once after a change persists,
     * then read the "after" values; the read getters no longer refresh on their own.
     */
    public void reloadAndOpenCollectionTab(String tabName) {
        driverTools.refreshWebpage();
        openCollectionTab(tabName);
    }

    public String getHotelDate(String productTitle) {
        Locator dateLocator = getCardDateLocator(productTitle);
        WebElement element = driverTools.waitForElementToAppear(dateLocator).orElseThrow();
        return driverTools.getText(element);
    }

    public String getHotelGuest(String productTitle) {
        Locator guestLocator = getCardGuestLocator(productTitle);
        WebElement element = driverTools.waitForElementToAppear(guestLocator).orElseThrow();
        return driverTools.getText(element);
    }

    public String getHotelRoom(String productTitle) {
        Locator roomLocator = getCardRoomLocator(productTitle);
        WebElement element = driverTools.waitForElementToAppear(roomLocator).orElseThrow();
        return driverTools.getText(element);
    }

    public void openProductManageMenu(String productTitle, String... menuOptionLabels) {
        openProductThreeDotsMenu(productTitle);
        Locator option = getManageMenuOptionLocator(menuOptionLabels);
        int attempts = 3;
        for (int i = 0; i < attempts; i++) {
            if (driverTools.waitForElementToAppear(option, 5).isPresent()) {
                driverTools.click(option);
                return;
            }
            logger.log("Manage menu option '" + String.join("/", menuOptionLabels)
                    + "' did not appear; re-clicking three-dots (attempt " + (i + 1) + ")");
            openProductThreeDotsMenu(productTitle);
        }
        driverTools.click(option);
    }

    /**
     * Opens the three-dots manage menu on a wishlist product card in the active collection tab.
     * Used by {@link #openProductManageMenu(String, String...)} and add/remove-from-collection flows.
     *
     * @param productTitle exact product title as shown on the card (from {@link #getWishlistProduct()} or {@link #getFirstProductTitle()})
     */
    public void openProductThreeDotsMenu(String productTitle) {
        Locator threeDots = getCardThreeDotsLocator(productTitle);
        WebElement btn = driverTools.waitForElementToPresence(threeDots).orElseThrow();
        driverTools.clickSvgOrNonHtmlElements(btn);
    }

    /**
     * Selects a collection by name in the "Add to collection" dialog and confirms with Add.
     * Caller must already have opened the add-to-collection dialog (e.g. via
     * {@link #addToCollectionMenuButton()} after {@link #openProductThreeDotsMenu(String)}).
     *
     * @param collectionName display name of the target collection (must match {@link #checkCollectionNameOutside(String)})
     */
    public void selectCollectionByNameAndAdd(String collectionName) throws InterruptedException {
        Locator collectionItem = new Locator("COLLECTION_ITEM_" + collectionName,
                By.xpath("//span[contains(@class,'CollectionItem_collection_item_name') and normalize-space()="
                        + toXpathLiteral(collectionName) + "]"));
        driverTools.waitForElementToAppear(collectionItem);
        driverTools.click(collectionItem);
        clickOnAddToCollection();
    }

    /**
     * Deletes the collection currently open in detail view via the top-right three-dots menu.
     * Expects the collection detail page to be displayed; does not navigate back afterward.
     */
    public void deleteOpenedCollection() throws InterruptedException {
        clickThreeDotsOnCollection();
        clickDeleteManageCollectionButton();
    }

    public int getProductCount() {
        if (driverTools.waitForElementToAppear(PRODUCTS_COUNT, 10).isEmpty()) {
            logger.log("Product count element not found, defaulting to 0");
            return 0;
        }
        String count = driverTools.getText(PRODUCTS_COUNT).orElse("0").trim();
        try {
            return Integer.parseInt(count);
        } catch (NumberFormatException e) {
            logger.log("Unable to parse product count: " + count);
            return 0;
        }
    }

    public void chooseAvailableDate() {
        driverTools.waitForElementToAppear(AVAILABLE_DATE);
        List<WebElement> dateElements = driverTools.elements(AVAILABLE_DATE).get();
        logger.log("available date size: " + dateElements.size());
        int startDate = generateRandomInt(0, dateElements.size() - 1);
        int endDate = startDate + 1;
        if (!(dateElements.isEmpty())) {
            driverTools.clickWithJavascriptExecutor(dateElements.get(startDate));
            driverTools.clickWithJavascriptExecutor(dateElements.get(endDate));
        }
    }

    /**
     * Picks a check-in/check-out range that differs from {@code currentDate} and returns the selected
     * day numbers as {@code [checkInDay, checkOutDay]} (e.g. {@code [8, 10]}) so the caller can assert
     * that the exact days chosen on the calendar show up on the product card afterwards.
     */
    public List<Integer> chooseAvailableDate(String currentDate) {
        driverTools.waitForElementToAppear(AVAILABLE_DATE);
        List<WebElement> dateElements = driverTools.elements(AVAILABLE_DATE).get();
        logger.log("available date size: " + dateElements.size());
        if (dateElements.size() < 2) {
            Assertion.skip("Need at least 2 selectable days (check-in + check-out) to change date; found "
                    + dateElements.size());
        }
        // Compare by numeric day, not raw text: the card shows "08" while calendar cells render "8",
        // so a string compare would treat them as different and let us re-pick the same check-in day
        // (making dateAfter == dateBefore). parseLeadingInt("08 - 10 Jul 2026") -> 8 (the check-in day).
        int currentCheckInDay = (currentDate == null || currentDate.isBlank())
                ? -1
                : parseLeadingInt(currentDate);
        logger.log("current check-in day to avoid: " + currentCheckInDay);
        int startDate = -1;
        for (int attempt = 0; attempt < 20; attempt++) {
            int candidate = generateRandomInt(0, dateElements.size() - 2);
            int candidateDay = parseLeadingInt(driverTools.getText(dateElements.get(candidate)));
            if (candidateDay != currentCheckInDay) {
                startDate = candidate;
                break;
            }
        }
        if (startDate == -1) {
            // Random picks kept landing on the current check-in day: scan for the first different one
            // so we never re-select the same date (which would make dateAfter equal dateBefore).
            for (int i = 0; i < dateElements.size() - 1; i++) {
                if (parseLeadingInt(driverTools.getText(dateElements.get(i))) != currentCheckInDay) {
                    startDate = i;
                    break;
                }
            }
        }
        if (startDate == -1) {
            // No selectable day differs from the current check-in: re-selecting the same range would
            // make dateAfter equal dateBefore, so skip with a clear reason instead of letting the test
            // fail on a date-unchanged assertion that the page object never actually changed.
            Assertion.skip("No available date differs from current check-in '" + currentCheckInDay
                    + "'; cannot change date to a different value");
        }
        int endDate = startDate + 1;
        int checkInDay = parseLeadingInt(driverTools.getText(dateElements.get(startDate)));
        int checkOutDay = parseLeadingInt(driverTools.getText(dateElements.get(endDate)));
        driverTools.clickWithJavascriptExecutor(dateElements.get(startDate));
        driverTools.clickWithJavascriptExecutor(dateElements.get(endDate));
        // mWeb keeps the date picker open until an explicit Save; without this the change never persists
        // (dWeb applies on selection). Mirrors changeDateOfProduct()'s mWeb handling.
        if (Configs.PLATFORM == PLATFORM.AMWEB || Configs.PLATFORM == PLATFORM.IMWEB) {
            driverTools.click(SAVE_BUTTON_MWEB);
        }
        logger.log("Selected check-in day: " + checkInDay + ", check-out day: " + checkOutDay);
        return new ArrayList<>(List.of(checkInDay, checkOutDay));
    }

    /**
     * Extracts the day numbers from a product-card date string such as {@code "08 - 10 Jul 2026"},
     * returning {@code [8, 10]}. Used to assert the exact calendar days selected appear on the card.
     */
    public List<Integer> extractDayNumbers(String cardDate) {
        List<Integer> days = new ArrayList<>();
        if (cardDate == null) {
            return days;
        }
        // Split on any non-digit run and keep only tokens in the 1..31 range; this drops the year
        // (e.g. 2026) and works for formats like "08 - 10 Jul 2026" / "08 Jul - 10 Aug 2026".
        // Note: the month is a name (not numeric) on the card, so no numeric month is present;
        // if the card text ever adds other small numbers (e.g. "2 nights") this would also pick them up.
        for (String token : cardDate.split("[^0-9]+")) {
            if (!token.isEmpty()) {
                int value = Integer.parseInt(token);
                if (value >= 1 && value <= 31) {
                    days.add(value);
                }
            }
        }
        return days;
    }

    /** Returns the numeric count parsed from a card label such as {@code "2 guests"} / {@code "1 kamar"}. */
    public int extractCount(String text) {
        return parseLeadingInt(text);
    }

    public void changeDateOfProduct() throws InterruptedException {
        clickHotelButton();
        driverTools.waitForElementToAppear(POPUP_DELETE_BUTTON, 2);
        WebElement btn = driverTools.waitForElementToPresence(ELLIPSIS_SAVED_HOTELS).orElseThrow();
        driverTools.clickSvgOrNonHtmlElements(btn);
        driverTools.waitForElementToAppear(BUTTON_CHANGE_DATE);
        driverTools.click(BUTTON_CHANGE_DATE);
        chooseAvailableDate();

        if (Configs.PLATFORM == PLATFORM.AMWEB || Configs.PLATFORM == PLATFORM.IMWEB) {
            driverTools.click(SAVE_BUTTON_MWEB);
        }
    }

    public void addGuestNumber() {
        driverTools.waitForElementToAppear(BUTTON_ADD_GUEST);
        List<WebElement> buttonElements = driverTools.elements(BUTTON_ADD_GUEST).get();
        logger.log("total add button: " + buttonElements.size());
        driverTools.click(buttonElements.get(1));
        driverTools.click(DONE_BUTTON);
        driverTools.waitForElementToDisappear(DONE_BUTTON,3);
        driverTools.refreshWebpage();
    }

    private int parseLeadingInt(String text) {
        if (text == null) {
            return 0;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : text.trim().toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
            } else if (sb.length() > 0) {
                break;
            }
        }
        return sb.length() == 0 ? 0 : Integer.parseInt(sb.toString());
    }

    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private int readModalValue(Locator inputLocator) {
        WebElement input = driverTools.waitForElementToAppear(inputLocator).orElseThrow();
        return parseLeadingInt(input.getAttribute("value"));
    }

    private int readModalMax(Locator inputLocator) {
        WebElement input = driverTools.waitForElementToAppear(inputLocator).orElseThrow();
        String maxAttr = input.getAttribute("max");
        return (maxAttr == null || maxAttr.isBlank()) ? Integer.MAX_VALUE : parseLeadingInt(maxAttr);
    }

    /**
     * The "Rooms & Guests" modal can briefly show a default before it settles. Polls until the input
     * value stops changing so we never act on a transient value.
     */
    private void waitForStableModalValue(Locator inputLocator) {
        int last = Integer.MIN_VALUE;
        int stableReads = 0;
        for (int i = 0; i < 20; i++) {
            Optional<WebElement> input = driverTools.waitForElementToPresence(inputLocator, 2);
            int value = input.map(el -> parseLeadingInt(el.getAttribute("value"))).orElse(Integer.MIN_VALUE);
            if (value == last && value != Integer.MIN_VALUE) {
                if (++stableReads >= 2) {
                    return;
                }
            } else {
                stableReads = 0;
                last = value;
            }
            sleepQuietly(150);
        }
    }

    /** A +/- button is unusable at a min/max limit (marked disabled). Treat that as "at limit". */
    private boolean isQuantityButtonEnabled(Locator buttonLocator) {
        Optional<WebElement> maybe = driverTools.waitForElementToPresence(buttonLocator, 2);
        if (maybe.isEmpty() || !maybe.get().isEnabled()) {
            return false;
        }
        WebElement button = maybe.get();
        if (button.getAttribute("disabled") != null) {
            return false;
        }
        String cls = button.getAttribute("class");
        return cls == null || !cls.toLowerCase().contains("disabled");
    }

    /**
     * Clicks plus/minus one step at a time to drive the modal quantity to {@code target}, stopping if
     * the button is disabled (limit) or a click no longer moves the value (clamped by a constraint).
     * Returns the number of clicks performed.
     */
    private int driveModalTowards(Locator inputLocator, Locator plusButton, Locator minusButton, int target) {
        int clicks = 0;
        for (int guard = 0; guard < 100; guard++) {
            int value = readModalValue(inputLocator);
            if (value == target) {
                break;
            }
            Locator button = value < target ? plusButton : minusButton;
            if (!isQuantityButtonEnabled(button)) {
                break;
            }
            driverTools.click(button);
            clicks++;
            if (readModalValue(inputLocator) == value) {
                break; // value did not move (limit or constraint clamp)
            }
        }
        return clicks;
    }

    /**
     * Changes both rooms and guests in the modal so each differs from the value on the card, randomly
     * increasing or decreasing (never below 1, never above the input's max). The card persists the
     * modal value on Done (card-after == modal-final), so we target {@code cardBefore +/- 1}. The
     * control enforces rooms &lt;= guests and the modal opens on defaults (not the card values), so we
     * always set guests (adults) before rooms; since target rooms &lt;= target guests, this never trips
     * the constraint.
     *
     * @return the values the modal actually settled on as {@code [room, guest]} (after any min/max or
     *         rooms&lt;=guests clamping) so the caller can assert the exact counts reflected on the card.
     */
    public List<Integer> changeRoomAndGuestToDifferent(String currentRoomText, String currentGuestText) {
        int roomBefore = parseLeadingInt(currentRoomText);
        int guestBefore = parseLeadingInt(currentGuestText);

        waitForStableModalValue(ROOM_INPUT);

        int maxRoom = readModalMax(ROOM_INPUT);
        int maxGuest = readModalMax(ADULTS_INPUT);

        boolean canIncrease = roomBefore < maxRoom && guestBefore < maxGuest;
        boolean canDecrease = roomBefore > 1 && guestBefore > 1;
        if (!canIncrease && !canDecrease) {
            // Both room and guest are pinned at a limit (e.g. 1 room with guests at max), so neither
            // +1 nor -1 is reachable. Skip with a clear reason rather than leaving the values
            // unchanged and failing the room/guest-changed assertions.
            Assertion.skip("Cannot change room/guest to a different value: room=" + roomBefore
                    + " (max " + maxRoom + "), guest=" + guestBefore + " (max " + maxGuest + ")");
        }
        boolean increase = (canIncrease && canDecrease) ? generateRandomInt(0, 1) == 1 : canIncrease;

        int guestTarget = increase ? guestBefore + 1 : guestBefore - 1;
        int roomTarget = increase ? roomBefore + 1 : roomBefore - 1;

        int clicks = driveModalTowards(ADULTS_INPUT, ADULTS_PLUS_BUTTON, ADULTS_MINUS_BUTTON, guestTarget);
        clicks += driveModalTowards(ROOM_INPUT, ROOM_PLUS_BUTTON, ROOM_MINUS_BUTTON, roomTarget);

        // Modal already on both targets without any interaction: nudge so Done registers the change,
        // then re-settle both guests and rooms on their targets (nudging adults can transiently clamp
        // rooms via the rooms <= guests rule).
        if (clicks == 0) {
            driverTools.click(isQuantityButtonEnabled(ADULTS_PLUS_BUTTON) ? ADULTS_PLUS_BUTTON : ADULTS_MINUS_BUTTON);
            driveModalTowards(ADULTS_INPUT, ADULTS_PLUS_BUTTON, ADULTS_MINUS_BUTTON, guestTarget);
            driveModalTowards(ROOM_INPUT, ROOM_PLUS_BUTTON, ROOM_MINUS_BUTTON, roomTarget);
        }

        // Read the values the modal actually settled on (a +/- step can be clamped by a min/max or the
        // rooms <= guests rule, so the final value may differ from the intended target). The card
        // persists exactly these on Done, so returning the actuals keeps the caller's strict count
        // assertions truthful: a genuine no-change is then caught by the before/after assertion.
        int finalRoom = readModalValue(ROOM_INPUT);
        int finalGuest = readModalValue(ADULTS_INPUT);
        logger.log("Change room/guest -> roomBefore: " + roomBefore + ", guestBefore: " + guestBefore
                + ", increase: " + increase + ", clicks: " + clicks
                + ", roomTarget: " + roomTarget + ", guestTarget: " + guestTarget
                + ", finalRoom: " + finalRoom + ", finalGuest: " + finalGuest);

        driverTools.click(DONE_BUTTON);
        driverTools.waitForElementToDisappear(DONE_BUTTON);

        return new ArrayList<>(List.of(finalRoom, finalGuest));
    }

    public void changeGuestOfProduct() {
        clickHotelButton();
        driverTools.waitForElementToAppear(POPUP_DELETE_BUTTON, 2);
        driverTools.waitForElementToAppear(ELLIPSIS_SAVED_HOTELS);
        driverTools.click(ELLIPSIS_SAVED_HOTELS);
        driverTools.waitForElementToAppear(BUTTON_CHANGE_GUEST);
        driverTools.click(BUTTON_CHANGE_GUEST);
        addGuestNumber();
    }

    public String getHotelDate() throws InterruptedException {
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(HOTEL_DATE);
        List<WebElement> elements = driverTools.elements(HOTEL_DATE).get();
        return driverTools.getText(elements.get(0));
    }

    public String getHotelGuest() {
        driverTools.waitForElementToAppear(HOTEL_GUEST);
        List<WebElement> elements = driverTools.elements(HOTEL_GUEST).get();

        if (elements != null && !elements.isEmpty()) {
            return driverTools.getText(elements.get(0)).split(" ")[0];
        } else {
            logger.log("No guest found.");
            return null;
        }
    }

    public String getRandomCollectionName() {
        return "For Automation (Do not Delete/Edit) " + randomName();
    }

    public void goToHotelPDP(String hotel) {
        String url = "https:" + hotel;
        driverTools.get(url);
    }

    public void clickWishlistHeartIcon() {
        if (Configs.PLATFORM == PLATFORM.AMWEB || Configs.PLATFORM == PLATFORM.IMWEB) {
            driverTools.waitForElementsToAppear(WISHLIST_HEART_ICON_MWEB);
            driverTools.click(WISHLIST_HEART_ICON_MWEB);
        } else {
            driverTools.waitForElementsToAppear(WISHLIST_HEART_ICON);
            driverTools.waitForElementToPresence(WISHLIST_HEART_ICON, 5);
            driverTools.waitForElementToBeClickable(WISHLIST_HEART_ICON);
            driverTools.click(WISHLIST_HEART_ICON);
            driverTools.waitForElementToDisappear(SNACKBAR_TEXT_AFTER_ADDING);
        }
    }

    public void clickManageOnSnackbar() {
        if (Configs.PLATFORM == PLATFORM.AMWEB || Configs.PLATFORM == PLATFORM.IMWEB) {
            if (driverTools.isElementDisplayed(PROMO_BANNER)) {
                driverTools.click(PROMO_BANNER);
            }
        }
        driverTools.waitForElementsToAppear(MANAGE_CTA);
        driverTools.clickWithJavascriptExecutor(MANAGE_CTA);
    }

    public void clickViewAllWishlist() {
        driverTools.waitForElementsToAppear(VIEW_ALL_WISHLIST);
        driverTools.click(VIEW_ALL_WISHLIST);
    }

    public void goToAllWishlist(String allWishlist) throws InterruptedException {
        driverTools.get("https:" + allWishlist);
        driverTools.waitForElementsToAppear(ALL_WISHLIST_HEADING, 3);
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(ALL_TABBINGS_BUTTON);
        driverTools.click(ALL_TABBINGS_BUTTON);
    }

    public void goToAllWishlistForHomes(String allWishlist) throws InterruptedException {
        driverTools.get("https:" + allWishlist);
        driverTools.waitForElementsToAppear(ALL_WISHLIST_HEADING, 3);
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(VILLA_APT_TAB);
        driverTools.click(VILLA_APT_TAB);
    }

    private Locator getWishlistCardByTitleLocator(String title) {
        String xpathTitle = title.contains("'")
                ? "concat('" + title.replace("'", "',\"'\",'") + "')"
                : "'" + title + "'";
        return new Locator("WISHLIST_CARD_" + title,
                By.xpath("(//p[contains(@class,'ProductInfo_product_title') and normalize-space()=" + xpathTitle + "])[3]"));
    }

    public boolean checkCardTitle(String title) {
        Locator cardTitleLocator = getWishlistCardByTitleLocator(title);
        for (int attempt = 1; attempt <= 10; attempt++) {
            if (driverTools.waitForElementToAppear(cardTitleLocator, 5).isPresent()
                    && driverTools.isElementDisplayed(cardTitleLocator)) {
                return true;
            }
            if (attempt < 10) {
                logger.log("Wishlist card '" + title + "' not visible, refreshing (attempt " + attempt + "/10)");
                driverTools.refreshWebpage();
            }
        }
        return false;
    }

    public boolean checkRemoveSnackbar() {
        driverTools.waitForElementToAppear(REMOVE_SNACKBAR);
        return driverTools.isElementDisplayed(REMOVE_SNACKBAR);
    }

    public Boolean checkCard(String title) {
        driverTools.waitForElementToAppear(ALL_WISHLIST_CARD);
        return driverTools.getText(ALL_WISHLIST_CARD).orElse("").equals(title);
    }

    public Boolean checkHeadingText() {
        driverTools.waitForElementToAppear(EMPTY_COLLECTION_HEADING);
        return driverTools.isElementDisplayed(EMPTY_COLLECTION_HEADING);
    }

    public Boolean checkHotelsEntryPoint() {
        driverTools.waitForElementToAppear(HOTEL_ENTRY_POINT);
        return driverTools.isElementDisplayed(HOTEL_ENTRY_POINT);
    }

    public Boolean checkHomesEntryPoint() {
        driverTools.waitForElementToAppear(HOMES_ENTRY_POINT);
        return driverTools.isElementDisplayed(HOMES_ENTRY_POINT);
    }

    public abstract void openEmptyCollection();

    public void openFilledCollection() {
        driverTools.waitForElementsToAppear(FILLED_COLLECTION);
        driverTools.click(FILLED_COLLECTION);
    }

    public Boolean checkProductVisible() {
        driverTools.waitForElementToAppear(PRODUCT_CARD);
        return driverTools.isElementDisplayed(PRODUCT_CARD);
    }

    public Boolean checkProductCountVisible() {
        driverTools.waitForElementToAppear(PRODUCTS_COUNT);
        return driverTools.isElementDisplayed(PRODUCTS_COUNT);
    }

    public void addToCollectionMenuButton() {
        driverTools.waitForElementsToAppear(ADD_TO_COLLECTION_MENU_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADD_TO_COLLECTION_MENU_BUTTON);
    }

    public abstract void removeFromCollectionMenuButton();

    public boolean isSnackbarVisibleAfterAdding() {
        WebElement snackbarElement = driverTools.waitForElementToAppear(SNACKBAR_TEXT_AFTER_ADDING, 30).orElseThrow();
        String snackbarText = driverTools.getText(snackbarElement);
        logger.log("snackbarText " + snackbarText);
        return driverTools.isElementDisplayed(snackbarElement);
    }

    public boolean isSnackbarVisibleAfterDeleting() {
        WebElement snackbarElement = driverTools.waitForElementToAppear(SNACKBAR_TEXT_AFTER_REMOVING, 30).orElseThrow();
        String snackbarText = driverTools.getText(snackbarElement);
        logger.log("snackbarText " + snackbarText);
        return driverTools.isElementDisplayed(snackbarElement);
    }

    public void clickOnWishlistSideMenu() {
        driverTools.waitForElementToAppear(WISHLIST_SIDE_MENU);
        driverTools.clickWithJavascriptExecutor(WISHLIST_SIDE_MENU);
    }

    public void clickOnWishlistSeeAllButton() throws InterruptedException {
        if (Configs.PLATFORM == PLATFORM.AMWEB || Configs.PLATFORM == PLATFORM.IMWEB) {
            driverTools.waitForElementsToAppear(PROMO_BANNER);
            if (driverTools.isElementDisplayed(PROMO_BANNER)) {
                driverTools.click(PROMO_BANNER);
            }
        }
        
        boolean isFound = false;
        for (int i = 0; i < 5; i++) {
            driverTools.scroll(1, 250);
            if (driverTools.isElementDisplayed(SEE_ALL_WISHLIST_BUTTON)) {
                isFound = true;
                break;
            }
        }
    
        if (isFound) {
            driverTools.clickWithJavascriptExecutor(SEE_ALL_WISHLIST_BUTTON);
        } else {
            throw new RuntimeException("SEE_ALL_WISHLIST_BUTTON was not found after scrolling.");
        }
    }

    public String getURl() {
        String url = driverTools.getCurrentURL();
        logger.log("url " + url);
        return url;
    }

    public void clickOnHotelTab() throws InterruptedException {
        driverTools.waitForElementToAppear(HOTEL_TAB);
        driverTools.clickWithJavascriptExecutor(HOTEL_TAB);
    }

    public void clickOnTodoTab() throws InterruptedException {
        driverTools.waitForElementToAppear(TODO_TAB);
        driverTools.clickWithJavascriptExecutor(TODO_TAB);
    }

    public void clickOnVillaTab() throws InterruptedException {
        driverTools.waitForElementToAppear(VILLA_TAB);
        driverTools.clickWithJavascriptExecutor(VILLA_TAB);
    }

    public void clickOnWishlistProduct() {
        driverTools.waitForElementToAppear(WISHLIST_PRODUCT).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public String getWishlistProduct() {
        WebElement wishlistElement = driverTools.waitForElementToAppear(WISHLIST_PRODUCT_INFO, 30).orElseThrow();
        String wishlistProduct = driverTools.getText(wishlistElement);
        logger.log("wishlistProduct " + wishlistProduct);
        return wishlistProduct;
    }

    public String getWishlistProductPageHeading() {
        if (Configs.PLATFORM == PLATFORM.AMWEB || Configs.PLATFORM == PLATFORM.IMWEB) {
            if (driverTools.isElementDisplayed(PROMO_BANNER)) {
                driverTools.click(PROMO_BANNER);
            }
        }
        WebElement wishlistElement = driverTools.waitForElementToAppear(WISHLIST_PRODUCT_PAGE_HEADING, 30).orElseThrow();
        String wishlistProduct = driverTools.getText(wishlistElement);
        logger.log("wishlistProduct " + wishlistProduct);
        return wishlistProduct;
    }

    public void addProductToCollection() throws InterruptedException {
        driverTools.waitForElementsToAppear(PRODUCT_IN_WISHLIST);
        WebElement product = driverTools.element(PRODUCT_IN_WISHLIST).orElseThrow();
        if (product.isDisplayed() && product.isEnabled()) {
            product.click();
        } else {
            driverTools.clickWithJavascriptExecutor(PRODUCT_IN_WISHLIST);
        }
        driverTools.waitForElementToAppear(ADD_TO_COLLECTION_BUTTON);
        WebElement addToCollectionButton = driverTools.element(ADD_TO_COLLECTION_BUTTON).orElseThrow();
        if (addToCollectionButton.isDisplayed() && addToCollectionButton.isEnabled()) {
            addToCollectionButton.click();
        } else {
            driverTools.clickWithJavascriptExecutor(ADD_TO_COLLECTION_BUTTON);
        }
    }

    public void clickOnAddToCollection() throws InterruptedException {
        driverTools.waitForElementsToAppear(ADD_TO_COLLECTION_BUTTON);
        WebElement addToCollectionButton = driverTools.element(ADD_TO_COLLECTION_BUTTON).orElseThrow();
        if (addToCollectionButton.isDisplayed() && addToCollectionButton.isEnabled()) {
            driverTools.click(addToCollectionButton);
        } else {
            driverTools.clickWithJavascriptExecutor(ADD_TO_COLLECTION_BUTTON);
        }
    }

    public String isSnackbarVisibleAfterRemoving() {
        WebElement snackbarElement = driverTools.waitForElementToAppear(SNACKBAR_TEXT_AFTER_REMOVING, 30).orElseThrow();
        String snackbarText = driverTools.getText(snackbarElement);
        logger.log("snackbarText " + snackbarText);
        return snackbarText;

    }

    public void clickCheckBox() throws InterruptedException {
        driverTools.waitForElementsToAppear(CHECK_BOX);
        driverTools.waitForElementToBeClickable(CHECK_BOX);
        driverTools.clickWithJavascriptExecutor(CHECK_BOX);
    }

    public void clickCrossOnPromoVoucher() {
        if (driverTools.isElementDisplayed(PROMO_VOUCHER)) {
            driverTools.clickWithJavascriptExecutor(PROMO_VOUCHER);
        }
    }

    public void clickCrossOnPromoVoucherMweb() {
        if (driverTools.isElementDisplayed(PROMO_VOUCHER_MWEB)) {
            driverTools.clickWithJavascriptExecutor(PROMO_VOUCHER_MWEB);
        }
    }

    public void selectCollectionAndAdd() {
        if (Configs.PLATFORM == PLATFORM.AMWEB || Configs.PLATFORM == PLATFORM.IMWEB) {
            if (driverTools.isElementDisplayed(PROMO_BANNER)) {
                driverTools.click(PROMO_BANNER);
            }
        }
        driverTools.waitForElementsToAppear(ADD_COLLECTION);
        driverTools.click(ADD_COLLECTION);
        driverTools.waitForElementsToAppear(ADD_BUTTON);
        driverTools.click(ADD_BUTTON);

    }

    public boolean isHeartIconActiveOnPdp() {
        return driverTools.waitForElementToAppear(PDP_HEART_ACTIVE, 10).isPresent()
                && driverTools.isElementDisplayed(PDP_HEART_ACTIVE);
    }

    public void manageWishlist() {
        clickHotelButton();
        driverTools.waitForElementsToAppear(MANAGE_WISHLIST);
        driverTools.waitForElementToBeClickable(MANAGE_WISHLIST);
        driverTools.click(MANAGE_WISHLIST);
        driverTools.waitForElementsToAppear(MANAGE_WISHLIST_CHECKBOX);
        driverTools.waitForElementToBeClickable(MANAGE_WISHLIST_CHECKBOX);
        driverTools.clickWithJavascriptExecutor(MANAGE_WISHLIST_CHECKBOX);
        driverTools.waitForElementsToAppear(MANAGE_WISHLIST_BUTTON);
        driverTools.waitForElementToBeClickable(MANAGE_WISHLIST_BUTTON);
        driverTools.clickWithJavascriptExecutor(MANAGE_WISHLIST_BUTTON);
        driverTools.waitForElementToAppear(BUTTON_CHANGE_GUEST);
        driverTools.click(BUTTON_CHANGE_GUEST);
        addGuestNumber();
    }
}
