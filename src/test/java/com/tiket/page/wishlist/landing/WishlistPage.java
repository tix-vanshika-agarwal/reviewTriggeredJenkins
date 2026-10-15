package com.tiket.page.wishlist.landing;

import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;

public class WishlistPage<T extends WebDriver> extends BasePage<T> {

    public WishlistPage(Session<T> session) {
        super(session);
    }

    //POPUP
    private static final Locator CLOSE_POPUP_PRICE_DROP = new Locator("CLOSE_POPUP_PRICE_DROP", AppiumBy.xpath("//*[contains(@class,'PriceDropsNotification_header_wrapper')]//*[@viewBox]"));
    private static final Locator CLOSE_POPUP_COACHMARK = new Locator("CLOSE_POPUP_COACHMARK", AppiumBy.xpath("//*[contains(@class,'Coachmark_coachmark_button_wrapper')]//button"));
    //Collection
    private static final Locator CREATE_COLLECTION_BUTTON = new Locator("CREATE_COLLECTION_BUTTON", AppiumBy.xpath("//*[contains(@class,'Collection_create_button_wrapper')]"));
    private static final Locator COLLECTION_NAME_FIELD = new Locator("COLLECTION_NAME_FIELD", AppiumBy.xpath("//textarea"));
    private static final Locator SAVE_COLLECTION_BUTTON = new Locator("SAVE_COLLECTION_BUTTON", AppiumBy.xpath("//button[contains(@class,'CreateEditCollectionPopup_save_button')]"));
    private static final Locator COLLECTION_NAMES = new Locator("COLLECTION_NAMES", AppiumBy.xpath("//*[contains(@class,'Collection_text_wrapper')]//*[contains(@class,'Collection_truncated_text')][1]"));
    private static final Locator SEE_ALL_BUTTON = new Locator("SEE_ALL_BUTTON", AppiumBy.xpath("(//*[contains(@class,'SectionWrapper_wrapper')])[1]//*[contains(@class,'SectionWrapper_section_button')]//button"));
    private static final Locator COLLECTION_OPTION = new Locator("COLLECTION_OPTION", AppiumBy.xpath("//*[@data-testid='icon-more']"));
    private static final Locator DELETE_COLLECTION_BUTTON = new Locator("DELETE_COLLECTION_BUTTON", AppiumBy.xpath("//*[@data-testid='menu-delete']"));
    private static final Locator CONFIRM_DELETE_COLLECTION_BUTTON = new Locator("CONFIRM_DELETE_COLLECTION_BUTTON", AppiumBy.xpath("//*[contains(@class,'DeleteConfirmation_button')]//button[1]"));
    private static final Locator EDIT_COLLECTION_BUTTON = new Locator("EDIT_COLLECTION_BUTTON", AppiumBy.xpath("(//*[contains(@class,'ManageCollection_menu_text')])[1]"));
    private static final Locator COLLECTION_DETAIL_NAME = new Locator("COLLECTION_DETAIL_NAME", AppiumBy.xpath("//*[contains(@class,'CoverImageBanner_banner_title')]"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//*[contains(@class,'back_button') and contains(@class,'Rounded')]"));
    private static final Locator ALL_TAB = new Locator("ALL_TAB", AppiumBy.xpath("//*[contains(@class,'index_page_product_tabs')]//button[1]"));
    private static final Locator HOTEL_TAB = new Locator("HOTEL_TAB", AppiumBy.xpath("//*[contains(@class,'index_page_product_tabs')]//button[2]"));
    private static final Locator VILLA_TAB = new Locator("VILLA_TAB", AppiumBy.xpath("//*[contains(@class,'index_page_product_tabs')]//button[3]"));
    private static final Locator ALL_BANNER = new Locator("ALL_BANNER", AppiumBy.xpath("//*[contains(@class,'AllWishlist_banner_list_wrapper')]"));
    private static final Locator HOTEL_BUTTON = new Locator("HOTEL_BUTTON", AppiumBy.xpath("//*[contains(@class,'CollectionTabContent')]//button[contains(text(),'Hotel')]"));
    private static final Locator VILLA_BUTTON = new Locator("VILLA_BUTTON", AppiumBy.xpath("//*[contains(@class,'CollectionTabContent')]//button[contains(text(),'Vil')]"));
    private static final Locator PRODUCT_NAME = new Locator("PRODUCT_NAME", AppiumBy.xpath("//*[contains(@class,'Tabs_tab_panel_active')]//*[contains(@class,'ProductInfo_product_title') and contains(@class,'bold')]"));
    private static final Locator MOVE_COLLECTION_BUTTON = new Locator("MOVE_COLLECTION_BUTTON", AppiumBy.xpath("//*[contains(@class,'ManageWishlist_menu_item')][1]"));
    private static final Locator CHANGE_DATE_BUTTON = new Locator("CHANGE_DATE_BUTTON", AppiumBy.xpath("//*[contains(@class,'ManageWishlist_menu_item')][2]"));
    private static final Locator CHANGE_GUEST_BUTTON = new Locator("CHANGE_GUEST_BUTTON", AppiumBy.xpath("//*[contains(@class,'ManageWishlist_menu_item')][3]"));
    private static final Locator CURRENT_ROOM_AMOUNT = new Locator("CURRENT_ROOM_AMOUNT", AppiumBy.xpath("(//*[contains(@class,'QuantityEditor_quantity_input')])[1]"));
    private static final Locator CURRENT_ADULT_AMOUNT = new Locator("CURRENT_ADULT_AMOUNT", AppiumBy.xpath("(//*[contains(@class,'QuantityEditor_quantity_input')])[2]"));
    private static final Locator ADD_ROOM_AMOUNT = new Locator("ADD_ROOM_AMOUNT", AppiumBy.xpath("(//*[contains(@class,'QuantityEditor_plus_icon')])[1]"));
    private static final Locator ADD_ADULT_AMOUNT = new Locator("ADD_ADULT_AMOUNT", AppiumBy.xpath("(//*[contains(@class,'QuantityEditor_plus_icon')])[2]"));
    private static final Locator SUB_ROOM_AMOUNT = new Locator("SUB_ROOM_AMOUNT", AppiumBy.xpath("(//*[contains(@class,'QuantityEditor_minus_icon')])[1]"));
    private static final Locator SUB_ADULT_AMOUNT = new Locator("SUB_ADULT_AMOUNT", AppiumBy.xpath("(//*[contains(@class,'QuantityEditor_minus_icon')])[2]"));
    private static final Locator SAVE_CALENDAR_BUTTON = new Locator("SAVE_CALENDAR_BUTTON", AppiumBy.xpath("//*[contains(@class,'DatePicker_button_save')]"));
    private static final Locator SAVE_MANAGE_BUTTON = new Locator("SAVE_MANAGE_BUTTON", AppiumBy.xpath("//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator SNACKBAR_ACTION_BUTTON = new Locator("SNACKBAR_ACTION_BUTTON", AppiumBy.xpath("//*[contains(@class,'Snackbar_action_button')]"));
    private static final Locator SNACKBAR_EDITED = new Locator("SNACKBAR_EDITED", AppiumBy.xpath("//*[contains(@class,'Snackbar_text_content')]"));

    //manage wishlist
    private static final Locator VIEW_ALL_WISHLIST_BUTTON = new Locator("VIEW_ALL_WISHLIST_BUTTON", AppiumBy.xpath("//*[contains(@class,'ViewAllWishlistButton')]"));
    private static final Locator WISHLIST_PRODUCTS_ALL = new Locator("WISHLIST_PRODUCTS_ALL", AppiumBy.xpath("(//*[contains(@class,'all_wishlist_contents')])[1]//p[contains(@class,'ProductInfo_product_title')]"));
    private static final Locator MANAGE_BUTTON_ALL = new Locator("MANAGE_BUTTON_ALL", AppiumBy.xpath("(//*[contains(@class,'WishlistFilter_manage_button')])[1]"));
    private static final Locator MANAGE_WISHLIST_BUTTON_ALL = new Locator("MANAGE_WISHLIST_BUTTON_ALL", AppiumBy.xpath("(//*[contains(@class,'all_wishlist_contents')])[1]//button[contains(@class,'ManageWishlistConfirmation')][2]"));
    private static final Locator MANAGE_WISHLIST_DELETE_ALL = new Locator("MANAGE_WISHLIST_DELETE_ALL", AppiumBy.xpath("//*[contains(@class,'BottomSheet_bottom_sheet')]//*[contains(@class,'ManageWishlist_menu_item')][2]"));
    private static final Locator ADD_TO_COLLECTION_MANAGE_BUTTON = new Locator("ADD_TO_COLLECTION_MANAGE_BUTTON", AppiumBy.xpath("//button[contains(@class,'AddToCollectionButton')]"));
    //landing page
    private static final Locator WISHLIST_DELETE_BUTTON = new Locator("WISHLIST_DELETE_BUTTON", AppiumBy.xpath("//*[contains(@class,'ManageWishlist_menu_item')][2]"));
    private static final Locator WISHLIST_PRODUCTS = new Locator("WISHLIST_PRODUCTS", AppiumBy.xpath("//p[contains(@class,'ProductInfo_product_title')]"));
    private static final Locator RECOMMENDED_PRODUCT_NAME = new Locator("RECOMMENDED_PRODUCT_NAME", AppiumBy.xpath("//*[contains(@class,'ProductCard_content_container')]//h2"));

    public void skipPriceDropsPopup() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist");

        if (driverTools.waitForElementToPresence(CLOSE_POPUP_PRICE_DROP).isPresent()) {
            driverTools.click(CLOSE_POPUP_PRICE_DROP);
            driverTools.waitForElementToPresence(CLOSE_POPUP_COACHMARK).ifPresent(driverTools::click);
        }
        driverTools.switchToNative();
    }

    public void createCollection(String collectionName) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist");

        driverTools.waitForElementToPresence(CREATE_COLLECTION_BUTTON);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(CREATE_COLLECTION_BUTTON);

        driverTools.waitForElementToPresence(COLLECTION_NAME_FIELD, 3);
        driverTools.sendKeys(COLLECTION_NAME_FIELD, collectionName);

        driverTools.clickWithJavascriptExecutor(SAVE_COLLECTION_BUTTON);
        driverTools.switchToNative();
    }

    public List<String> getCollectionNames() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist");

        driverTools.waitForElementToPresence(COLLECTION_NAMES, 3);
        List<String> collections = driverTools.getTexts(COLLECTION_NAMES);

        driverTools.switchToNative();
        return collections;
    }

    public void deleteCollection(String collectionName) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist");
        driverTools.clickWithJavascriptExecutor(getCollectionLocator(collectionName));

        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist/collection/");

        driverTools.waitForElementToPresence(COLLECTION_OPTION, 3);
        driverTools.click(COLLECTION_OPTION);

        driverTools.waitForElementToPresence(DELETE_COLLECTION_BUTTON, 3);
        driverTools.clickWithJavascriptExecutor(DELETE_COLLECTION_BUTTON);

        driverTools.waitForElementToPresence(CONFIRM_DELETE_COLLECTION_BUTTON, 3);
        driverTools.clickWithJavascriptExecutor(CONFIRM_DELETE_COLLECTION_BUTTON);

        driverTools.switchToNative();
    }

    @SneakyThrows
    public void editCollection(String collectionName, String newCollectionName) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist");
        driverTools.clickWithJavascriptExecutor(getCollectionLocator(collectionName));

        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist/collection");

        driverTools.waitForElementToPresence(COLLECTION_OPTION, 3);
        driverTools.click(COLLECTION_OPTION);

        driverTools.waitForElementToPresence(EDIT_COLLECTION_BUTTON, 3);
        driverTools.clickWithJavascriptExecutor(EDIT_COLLECTION_BUTTON);

        driverTools.click(COLLECTION_NAME_FIELD);
        driverTools.waitForElementToPresence(COLLECTION_NAME_FIELD, 3);
        driverTools.clearWithJsExecutor(COLLECTION_NAME_FIELD);
        Thread.sleep(500); //need some delay or else
        driverTools.sendKeys(COLLECTION_NAME_FIELD, newCollectionName);
        driverTools.hideKeyboard();

        driverTools.click(SAVE_COLLECTION_BUTTON);

        Assertion.assertThat("Snackbar appears", driverTools.waitForElementToPresence(SNACKBAR_EDITED, 3).isPresent(), is(true));

        driverTools.switchToNative();
    }

    public String getCollectionNameDetail() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist/collection");
        String collectionName = driverTools.getText(COLLECTION_DETAIL_NAME).orElseThrow();
        driverTools.switchToNative();
        return collectionName;
    }

    public void clickBackButton() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist/collection");
        driverTools.clickWithJavascriptExecutor(BACK_BUTTON);
        driverTools.switchToNative();
    }

    public void cleanUp(String collectionName) {
        try {
            deleteCollection(collectionName);
        } catch (Exception e) {
            logger.warn("Wishlist product clean up failed " + e.getMessage());
        }
        driverTools.switchToNative();
    }

    private Locator getCollectionLocator(String collectionName) {
        return new Locator("COLLECTION_NAME", AppiumBy.xpath("//*[contains(@class,'Collection_text_wrapper')]//*[contains(@class,'Collection_truncated_text')][1][text()='" + collectionName + "']"));
    }

    public void openCollection(String collectionName) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist");
        if (driverTools.waitForElementToPresence(SEE_ALL_BUTTON, 3).isPresent()) {
            driverTools.clickWithJavascriptExecutor(SEE_ALL_BUTTON);
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("collection");
        }
        driverTools.waitForElementToPresence(getCollectionLocator(collectionName));
        driverTools.clickWithJavascriptExecutor(getCollectionLocator(collectionName));
        driverTools.switchToNative();
    }

    public boolean verifyTabbing() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist/collection");

        driverTools.click(HOTEL_TAB);
        Assertion.assertThat("Hotel tab is working", driverTools.waitForElementToPresence(HOTEL_BUTTON, 3).isPresent(), is(true));

        driverTools.click(VILLA_TAB);
        Assertion.assertThat("Villa tab is working", driverTools.waitForElementToPresence(VILLA_BUTTON, 3).isPresent(), is(true));

        driverTools.click(ALL_TAB);
        Assertion.assertThat("All tab is working", driverTools.waitForElementToPresence(ALL_BANNER, 3).isPresent(), is(true));

        driverTools.switchToNative();
        return true;
    }

    public void clickSeeAllWishlistAfterAdding() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist/manage");
        driverTools.click(VIEW_ALL_WISHLIST_BUTTON);
        driverTools.switchToNative();
    }

    public List<String> getWishlistNamesAll() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist/all");
        if(!driverTools.isElementDisplayed(WISHLIST_PRODUCTS)){
            return new ArrayList<String>();
        }
        List<String> productNames = driverTools.getTexts(WISHLIST_PRODUCTS_ALL);
        driverTools.switchToNative();
        return productNames;
    }

    public void cleanUpAllPage(String wishlistProductName) {
        try {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("wishlist/all");

            driverTools.click(MANAGE_BUTTON_ALL);
            driverTools.waitForElementToPresence(WISHLIST_PRODUCTS_ALL);

            driverTools.clickWithJavascriptExecutor(getWishlistProductLocator(wishlistProductName));
            driverTools.clickWithJavascriptExecutor(MANAGE_WISHLIST_BUTTON_ALL);

            driverTools.waitForElementToPresence(MANAGE_WISHLIST_DELETE_ALL);
            driverTools.click(MANAGE_WISHLIST_DELETE_ALL);
        } catch (Exception e) {
            logger.warn("Wishlist product clean up failed " + e.getMessage());
        }
        driverTools.switchToNative();
    }

    private Locator getWishlistProductLocator(String wishlistProductName) {
        return new Locator("WISHLIST_PRODUCT_NAME", AppiumBy.xpath("(//*[contains(@class,'all_wishlist_contents')]//p[contains(@class,'ProductInfo_product_title')][contains(text(),'" + wishlistProductName + "')])[1]"));
    }

    public String clickProductInCollection() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("collection");
        String productName = driverTools.getText(PRODUCT_NAME).orElseThrow();
        driverTools.click(PRODUCT_NAME);
        driverTools.switchToNative();
        return productName;
    }

    public void clickTab(String tabName) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("collection");
        switch (tabName.toLowerCase()) {
            case "all":
                driverTools.click(ALL_TAB);
                break;
            case "hotel":
                driverTools.click(HOTEL_TAB);
                break;
            case "villa":
                driverTools.click(VILLA_TAB);
                break;
        }
        driverTools.switchToNative();
    }

    public String getProductDate(String productName) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("collection");
        driverTools.waitForElementToPresence(PRODUCT_NAME);

        String productDate = driverTools.getText(getProductDateLocator(productName)).orElseThrow();
        logger.log("Product date: " + productDate);

        driverTools.switchToNative();
        return productDate;
    }

    public void clickManageButtonCollection(String productName) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("collection");
        driverTools.waitForElementToPresence(getProductManageCollectionLocator(productName),5);
        if(Configs.PLATFORM == Platform.ANDROID){
            driverTools.scrollToElement(getProductManageCollectionLocator(productName));
            driverTools.clickElementByCoordinateDWeb(getProductManageCollectionLocator(productName));
        } else {
            driverTools.click(getProductManageCollectionLocator(productName));
        }
        driverTools.switchToNative();
    }

    public void clickManageButtonCollectionViaVerticalSection(String productName) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("collection");
        driverTools.waitForElementToPresence(getProductManageCollectionLocator2(productName),5);
        if(Configs.PLATFORM == Platform.ANDROID){
            driverTools.scrollToElement(getProductManageCollectionLocator2(productName));
            driverTools.clickElementByCoordinateDWeb(getProductManageCollectionLocator2(productName));
        } else {
            driverTools.click(getProductManageCollectionLocator2(productName));
        }
        driverTools.switchToNative();
    }

    public String getFirstProductName() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("collection");
        String productName = driverTools.getText(PRODUCT_NAME).orElseThrow();

        logger.log("Product name: " + productName);

        driverTools.switchToNative();
        return productName;
    }

    public List<String> getGuestAndRoomInfo(String productName) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("collection");
        driverTools.waitForElementToPresence(PRODUCT_NAME);
        List<String> guestAndRoom = driverTools.getTexts(getGuestAndRoomLocator(productName));
        logger.log("Guest and room info: " + guestAndRoom);
        driverTools.switchToNative();
        return guestAndRoom;
    }

    public Locator getProductDateLocator(String productName) {
        return new Locator("PRODUCT_DATE", AppiumBy.xpath("//*[contains(@class,'Tabs_tab_panel_active')]//*[contains(@class,'ProductInfo_product_title') and contains(@class,'bold') and text()='" + productName + "']//following-sibling::*//*[contains(@class,'InfoEngineRenderer') and contains(text(),'-')]"));
    }

    public Locator getProductManageCollectionLocator(String productName) {
        return new Locator("PRODUCT_MANAGE", AppiumBy.xpath("(//p[contains(.,'"+productName+"')]/ancestor::div[contains(@class,'WishCard')][1] //div[contains(@class,'wish_card_button') or contains(@class,'WishCard_wish_card_button')]/*[name()='svg'])[1]"));
    }

    public Locator getProductManageCollectionLocator2(String productName) {
        return new Locator("PRODUCT_MANAGE", AppiumBy.xpath("(//p[contains(.,'"+productName+"')]/ancestor::div[contains(@class,'WishCard')][1] //div[contains(@class,'wish_card_button') or contains(@class,'WishCard_wish_card_button')]/*[name()='svg'])[2]"));
    }

    public Locator getProductManageLocator(String productName) {
        return new Locator("PRODUCT_MANAGE", AppiumBy.xpath("(//*[contains(@class,'ProductInfo_product_title') and contains(@class,'bold') and text()='"+productName+"']//ancestor::*[contains(@class,'WishCard_wish_card_wrapper')]//*[contains(@class,'WishCard_wish_card_button')]/*)[1]"));
    }

    public Locator getGuestAndRoomLocator(String productName) {
        return new Locator("GUEST_ROOM", AppiumBy.xpath("//*[contains(@class,'Tabs_tab_panel_active')]//*[contains(@class,'ProductInfo_product_title') and contains(@class,'bold') and text()='" + productName + "']//following-sibling::*//*[@data-testid='icon-description-2']//*[text()]"));
    }

    public void changeDate(int firstDate, int secondDate){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("collection");
        driverTools.click(CHANGE_DATE_BUTTON);
        driverTools.waitForElementToPresence(getDateLocator(firstDate));
        driverTools.clickWithJavascriptExecutor(getDateLocator(firstDate));
        driverTools.clickWithJavascriptExecutor(getDateLocator(secondDate));
        driverTools.click(SAVE_CALENDAR_BUTTON);
        driverTools.switchToNative();
    }

    private Locator getDateLocator(int date){
        return new Locator("DATE", AppiumBy.xpath("//*[contains(@class,'Day_day_number')and not(contains(@class,'disabled')) and text()='"+date+"']"));
    }

    public void changeGuestAndRoom(int guest, int room){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("collection");
        driverTools.click(CHANGE_GUEST_BUTTON);

        logger.log("Changing guest info to " + guest + " and room info to " + room);

        driverTools.waitForElementToPresence(CURRENT_ROOM_AMOUNT);
        int currentRoom = Integer.parseInt(driverTools.getValue(CURRENT_ROOM_AMOUNT));

        if(room > currentRoom){
            for(int i = 0; i < room - currentRoom; i++){
                driverTools.click(ADD_ROOM_AMOUNT);
            }
        } else if(room < currentRoom){
            for(int i = 0; i < currentRoom - room; i++){
                driverTools.click(SUB_ROOM_AMOUNT);
            }
        }

        int currentGuest= Integer.parseInt(driverTools.getValue(CURRENT_ADULT_AMOUNT));

        if(guest > currentGuest){
            for(int i = 0; i < guest - currentGuest; i++){
                driverTools.click(ADD_ADULT_AMOUNT);
            }
        } else if(guest < currentGuest){
            for(int i = 0; i < currentGuest - guest; i++){
                driverTools.click(SUB_ADULT_AMOUNT);
            }
        }
        driverTools.click(SAVE_MANAGE_BUTTON);
        driverTools.switchToNative();
    }

    public void moveToAnotherCollection(String collectionName,boolean redirect){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("collection");
        driverTools.click(MOVE_COLLECTION_BUTTON);
        driverTools.clickWithJavascriptExecutor(getCollectionMoveLocator(collectionName));
        driverTools.click(SAVE_MANAGE_BUTTON);
        if(redirect){
            driverTools.waitForElementToPresence(SNACKBAR_ACTION_BUTTON);
            driverTools.clickWithJavascriptExecutor(SNACKBAR_ACTION_BUTTON);
        }
        driverTools.switchToNative();
    }

    private Locator getCollectionMoveLocator(String collectionName){
        return new Locator("COLLECTION_MOVE", AppiumBy.xpath("//*[contains(@class,'CollectionItem_collection_item_name') and text()='"+collectionName+"']"));
    }

    public List<String> getProductsInCollection(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("collection");
        List<String> productNames = driverTools.getTexts(PRODUCT_NAME);
        driverTools.switchToNative();
        return productNames;
    }

    public void clickManageWishlist(String productName){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist");
        driverTools.click(getProductManageLocator(productName));
        driverTools.switchToNative();
    }

    public void removeFromAllWishlist(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist");
        driverTools.click(WISHLIST_DELETE_BUTTON);
        Assertion.assertThat("Snackbar appears", driverTools.waitForElementToPresence(SNACKBAR_EDITED, 3).isPresent(), is(true));
        driverTools.switchToNative();
    }

    public List<String> getWishlistNamesLanding(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist");
        List<String> productNames = driverTools.getTexts(WISHLIST_PRODUCTS);
        driverTools.switchToNative();
        return productNames;
    }

    public String clickRecommendedCard(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist");
        String productName = driverTools.getText(RECOMMENDED_PRODUCT_NAME).orElseThrow();
        driverTools.click(RECOMMENDED_PRODUCT_NAME);
        driverTools.switchToNative();
        return productName;
    }

    public void addToCollectionManage(String collectionName){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist/manage");
        driverTools.clickWithJavascriptExecutor(getCollectionNameManageLocator(collectionName));
        driverTools.click(ADD_TO_COLLECTION_MANAGE_BUTTON);
        driverTools.switchToNative();
    }

    private Locator getCollectionNameManageLocator(String collectionName){
        return new Locator("COLLECTION_NAME_MANAGE", AppiumBy.xpath("//*[contains(@class,'CollectionItem_collection_item_name') and text()='"+collectionName+"']"));
    }

    public void removeFromCollection(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("collection");
        driverTools.click(WISHLIST_DELETE_BUTTON);
        driverTools.switchToNative();
    }

    public void addToCollectionManageFromWishlistPage(String collectionName) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("wishlist");
        driverTools.clickWithJavascriptExecutor(MOVE_COLLECTION_BUTTON);
        driverTools.clickWithJavascriptExecutor(getCollectionNameManageLocator(collectionName));
        driverTools.click(SAVE_MANAGE_BUTTON);
        driverTools.switchToNative();
    }

}