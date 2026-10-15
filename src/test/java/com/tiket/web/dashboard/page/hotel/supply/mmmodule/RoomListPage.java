package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RoomListPage <T extends WebDriver> extends BasePage<T> {
    public RoomListPage(Session<T> session) {
        super(session);
    }

    private static final Locator ROOM_LIST_TITLE_LABEL = new Locator("ROOM_LIST_TITLE_LABEL", new By.ByXPath("//span[normalize-space()='Room List' or normalize-space()='Daftar Kamar']"));
    private static final Locator FILTER_COUNTRY_INPUT = new Locator("FILTER_COUNTRY_INPUT", new By.ByName("country"));
    private static final Locator FILTER_REGION_INPUT = new Locator("FILTER_REGION_INPUT", new By.ById("select-province"));
    private static final Locator FILTER_CITY_INPUT = new Locator("FILTER_CITY_INPUT", new By.ByName("city"));
    private static final Locator FILTER_AREA_INPUT = new Locator("FILTER_AREA_INPUT", new By.ByName("area"));
    private static final Locator SEE_ADVANCED_FILTER_BUTTON = new Locator("SEE_ADVANCED_FILTER_BUTTON", new By.ByXPath("//div[contains(@class,'Text_variant_activity') and contains(@class,'cursor-pointer')]"));
    private static final Locator FILTER_ROOM_TYPE_INPUT = new Locator("FILTER_ROOM_TYPE_INPUT", new By.ByXPath("(//div[contains(@class,'tmdf-autocomplete-multiselect')])[1]//input[@type=\"text\"]"));
    private static final Locator FILTER_BED_TYPE_INPUT = new Locator("FILTER_BED_TYPE_INPUT", new By.ByXPath("(//div[contains(@class,'tmdf-autocomplete-multiselect')])[7]//input[@type=\"text\"]"));
    private static final Locator FILTER_NO_OF_ROOMS_INPUT = new Locator("FILTER_NO_OF_ROOMS_INPUT", new By.ByXPath("(//div[contains(@class,'tmdf-autocomplete-multiselect')])[9]//input[@type=\"text\"]"));
    private static final Locator FILTER_SMOKING_POLICY_INPUT = new Locator("FILTER_SMOKING_POLICY_INPUT", new By.ByXPath("(//div[contains(@class,'tmdf-autocomplete-multiselect')])[last()-2]//input[@type=\"text\"]"));
    private static final Locator FILTER_PROPERTY_TYPE_INPUT = new Locator("FILTER_PROPERTY_TYPE_INPUT", new By.ByXPath("(//div[contains(@class,'tmdf-autocomplete-multiselect')])[last()-1]//input[@type=\"text\"]"));
    private static final Locator FILTER_HOTEL_NAME_INPUT = new Locator("FILTER_HOTEL_NAME_INPUT", new By.ByName("hotelName"));
    private static final Locator FILTER_ROOM_NAME_TYPE_INPUT = new Locator("FILTER_ROOM_NAME_TYPE_INPUT", new By.ByXPath("//div[contains(@class,'styles_inline_input_wrapper')]//input[@id='room-name']"));
    private static final Locator FILTER_ROOM_NAME_TYPE_ICON = new Locator("FILTER_ROOM_NAME_TYPE_ICON", new By.ByXPath("//div[contains(@class,'styles_inline_input_wrapper')]//input[@id='room-name']/following::div[contains(@class,'DropdownFormDisplay_icon_container')][1]"));
    private static final Locator FILTER_ROOM_NAME_INPUT = new Locator("FILTER_ROOM_NAME_INPUT", new By.ByXPath("//input[@name='roomNameKeyword']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[contains(@class,'Button_variant_primary') and normalize-space()='Search']"));
    private static final Locator EXPORT_TO_CSV_BUTTON = new Locator("EXPORT_TO_CSV_BUTTON", new By.ByXPath("//button[@data-testid=\"export-btn\"]"));
    private static final Locator ROOM_LIST_TABLE = new Locator("ROOM_LIST_TABLE", new By.ByXPath("//table[contains(@class,'border-separate') and contains(@class,'w-full')]"));
    private static final Locator EDIT_ROOM_MASTER_DATA_BUTTON = new Locator("EDIT_ROOM_MASTER_DATA_BUTTON", new By.ByXPath("//button[contains(@class,'Button_variant_primary') and normalize-space()='Edit Room Master Data']"));
    private static final Locator EDIT_ROOM_MASTER_DATA_MODAL = new Locator("EDIT_ROOM_MASTER_DATA_MODAL", new By.ByXPath("//h2[contains(normalize-space(.),'Edit Room Master Data')] | //button[@data-testid='modal-close-button'] | //input[@id='category']"));
    private static final Locator ADD_ANOTHER_CATEGORY_BUTTON = new Locator("ADD_ANOTHER_CATEGORY_BUTTON", new By.ByXPath("//button[contains(@class,'add-another-category-btn')]"));
    private static final Locator EDIT_ROOM_MASTER_DATA_SUBMIT_BUTTON = new Locator("EDIT_ROOM_MASTER_DATA_SUBMIT_BUTTON", new By.ByXPath("//button[contains(@class,'btn-submit-edit-rm') or @test-id='submit-edit-rm']"));
    private static final Locator SUCCESSFUL_SNACKBAR = new Locator("SUCCESSFUL_SNACKBAR", new By.ByXPath("//div[contains(@class,'Snackbar_type_success')]//div[contains(@class,'Snackbar_text_content')]"));

    private static Locator getNamedFilterOption(String dataName, String text) {
        return new Locator(dataName.toUpperCase() + "_DROPDOWN_LIST", new By.ByXPath(String.format("//li[@data-name=%s and (@data-label=%s or normalize-space(.)=%s)]", CoreFunctions.toXPathLiteral(dataName), CoreFunctions.toXPathLiteral(text), CoreFunctions.toXPathLiteral(text))));
    }

    private static Locator getFilterRoomTypeDropdownList(String text) {
        return new Locator("ROOM_TYPE_DROPDOWN_LIST", new By.ByXPath(String.format("(//div[contains(@class,'tmdf-autocomplete-multiselect')])[1]//label[text()='%s']", text)));
    }

    private static Locator getFilterBedTypeDropdownList(String text) {
        return new Locator("BED_TYPE_DROPDOWN_LIST", new By.ByXPath(String.format("(//div[contains(@class,'tmdf-autocomplete-multiselect')])[7]//label[text()='%s']", text)));
    }

    private static Locator getFilterNoOfRoomsDropdownList(String text) {
        return new Locator("NO_OF_ROOMS_DROPDOWN_LIST", new By.ByXPath(String.format("(//div[contains(@class,'tmdf-autocomplete-multiselect')])[9]//label[text()='%s']", text)));
    }

    private static Locator getFilterSmokingPolicyDropdownList(String text) {
        return new Locator("SMOKING_POLICY_DROPDOWN_LIST", new By.ByXPath(String.format("(//div[contains(@class,'tmdf-autocomplete-multiselect')])[last()-2]//label[text()='%s']", text)));
    }

    private static Locator getFilterPropertyTypeDropdownList(String text) {
        return new Locator("PROPERTY_TYPE_DROPDOWN_LIST", new By.ByXPath(String.format("(//div[contains(@class,'tmdf-autocomplete-multiselect')])[last()-1]//label[text()='%s']", text)));
    }

    private static Locator getSearchResultCheckboxCell(int index) {
        return new Locator("SEARCH_RESULT_CHECKBOX", new By.ByXPath(String.format("(//tr[@data-testid='tr-table-body'])[%d]//span[contains(@class,'Check_custom_checkbox')]", index)));
    }

    private static Locator getTableCellContaining(String text) {
        return new Locator("TABLE_CELL", new By.ByXPath("//tr[@data-testid='tr-table-body']//td[contains(normalize-space(.)," + CoreFunctions.toXPathLiteral(text) + ")]"));
    }

    private static Locator getSearchResultRowCell(int index, int column) {
        return new Locator("SEARCH_RESULT_CELL", new By.ByXPath(String.format("(//tr[@data-testid='tr-table-body'])[%d]/td[%d]", index, column)));
    }

    private static Locator getSearchResultHotelNameCell(int index) {
        return getSearchResultRowCell(index, 5);
    }

    private static Locator getSearchResultPropertyTypeCell(int index) {
        return getSearchResultRowCell(index, 6);
    }

    private static Locator getSearchResultOriginalRoomNameCell(int index) {
        return getSearchResultRowCell(index, 9);
    }

    private static Locator getSearchResultRoomTypeCell(int index) {
        return getSearchResultRowCell(index, 11);
    }

    private static Locator getSearchResultBedTypeCell(int index) {
        return getSearchResultRowCell(index, 15);
    }

    private static Locator getSearchResultNoOfRoomsCell(int index) {
        return getSearchResultRowCell(index, 24);
    }

    private static Locator getSearchResultSmokingPolicyCell(int index) {
        return getSearchResultRowCell(index, 26);
    }

    private static Locator getSearchResultCellByAttributeTypeIndex(String attribute, int index) {
        int column = switch (attribute) {
            case "PROPERTY_NAME" -> 5;
            case "PROPERTY_TYPE" -> 6;
            case "ORIGINAL_ROOM_NAME" -> 9;
            case "ROOM_TYPE" -> 11;
            case "BED_TYPE" -> 15;
            case "NO_OF_ROOM" -> 24;
            case "SMOKING_POLICY" -> 26;
            default -> throw new IllegalStateException("Unexpected value: " + attribute);
        };
        return getSearchResultRowCell(index, column);
    }

    private static Locator getEditRoomMasterDataCategoryTypeInputByIndex(int index) {
        return new Locator("EDIT_ROOM_MASTER_DATA_CATEGORY_TYPE_INPUT", new By.ByXPath(String.format("(//input[@id='category' or @name='category'])[%d]", index)));
    }

    private static Locator getEditRoomMasterDataCategoryValueInputByIndex(int index) {
        return new Locator("EDIT_ROOM_MASTER_DATA_CATEGORY_VALUE_INPUT", new By.ByXPath(String.format("(//input[@id='category' or @name='category'])[%d]/following::input[contains(@class,'styles_has_clickable_icon')][1]", index)));
    }

    private static Locator getEditRoomMasterDataCategoryValueIconByIndex(int index) {
        return new Locator("EDIT_ROOM_MASTER_DATA_CATEGORY_VALUE_ICON", new By.ByXPath(String.format("(//input[@id='category' or @name='category'])[%d]/following::input[contains(@class,'styles_has_clickable_icon')][1]/following::button[contains(@class,'styles_clickable_icon')][1]", index)));
    }

    private static Locator getDropdownItem(String label) {
        return new Locator("DROPDOWN_ITEM", new By.ByXPath("//li[@data-testid='dropdown-item' and @aria-label=" + CoreFunctions.toXPathLiteral(label) + "]"));
    }

    private static Locator getEditRoomMasterDataCategoryTypeDropdownListByIndex(int index, String text) {
        String value = switch (text) {
            case "LISTING_TYPE" -> "Listing Type";
            case "ROOM_TYPE" -> "Room Type";
            case "ROOM_BENEFIT" -> "Room Benefit";
            case "BRAND" -> "Brand";
            case "BED_TYPE" -> "Bed Type";
            case "NO_OF_ROOM" -> "No of Room";
            case "NO_OF_PAX" -> "No of Pax & Occupancy Name";
            case "ROOM_VIEW" -> "Room View";
            case "PACKAGE" -> "Package";
            case "SMOKING_POLICY" -> "Smoking Policy";
            case "BEDROOM_COUNTS" -> "Bedroom Counts";
            case "NO_OF_BEDS" -> "No of Beds";
            case "ROOM_SIZE" -> "Room Size";
            case "BED_INFORMATION" -> "Bed Information";
            case "ROOM_USAGE" -> "Room Usage";
            default -> text;
        };
        return getDropdownItem(value);
    }

    private static Locator getEditRoomMasterDataCategoryValueDropdownList(String text) {
        return getDropdownItem(text);
    }

    public boolean isRoomListTitleDisplayed() {
        return driverTools.waitForElementToAppear(ROOM_LIST_TITLE_LABEL).isPresent();
    }

    public void selectFilterCountry(String text) {
        selectNamedFilter(FILTER_COUNTRY_INPUT, getNamedFilterOption("country", text), text);
    }

    public void selectFilterRegion(String text) {
        selectNamedFilter(FILTER_REGION_INPUT, getNamedFilterOption("region", text), text);
    }

    public void selectFilterCity(String text) {
        selectNamedFilter(FILTER_CITY_INPUT, getNamedFilterOption("city", text), text);
    }

    public void selectFilterArea(String text) {
        selectNamedFilter(FILTER_AREA_INPUT, getNamedFilterOption("area", text), text);
    }

    private void selectNamedFilter(Locator inputLocator, Locator optionLocator, String text) {
        driverTools.waitForElementToPresence(inputLocator).orElseThrow();
        for (int attempt = 0; attempt < 2; attempt++) {
            driverTools.clickWithJavascriptExecutor(inputLocator);
            try {
                driverTools.sendKeys(inputLocator, text);
            } catch (Exception e) {
                driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(inputLocator, text);
            }
            if (driverTools.waitForElementToPresence(optionLocator, 8).isEmpty()) {
                continue;
            }
            try {
                driverTools.clickWithJavascriptExecutor(driverTools.waitForElementToPresence(optionLocator).orElseThrow());
            } catch (StaleElementReferenceException e) {
                driverTools.clickWithJavascriptExecutor(driverTools.waitForElementToPresence(optionLocator).orElseThrow());
            }
            return;
        }
        driverTools.waitForElementToPresence(optionLocator, 5).orElseThrow();
    }

    public void seeAdvancedFilter() {
        driverTools.waitForElementToPresence(SEE_ADVANCED_FILTER_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEE_ADVANCED_FILTER_BUTTON);
    }

    public void inputFilterHotelName(String text) {
        driverTools.waitForElementToPresence(FILTER_HOTEL_NAME_INPUT).orElseThrow();
        driverTools.scrollIntoWebView(FILTER_HOTEL_NAME_INPUT);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(FILTER_HOTEL_NAME_INPUT, text);
    }

    public void selectFilterRoomType(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(FILTER_ROOM_TYPE_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getFilterRoomTypeDropdownList(text), 10).orElseThrow();
        driverTools.click(dropdown);
    }

    public void selectFilterBedType(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(FILTER_BED_TYPE_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getFilterBedTypeDropdownList(text), 10).orElseThrow();
        driverTools.click(dropdown);
    }

    public void selectFilterNoOfRooms(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(FILTER_NO_OF_ROOMS_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getFilterNoOfRoomsDropdownList(text), 10).orElseThrow();
        driverTools.click(dropdown);
    }

    public void selectFilterSmokingPolicy(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(FILTER_SMOKING_POLICY_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getFilterSmokingPolicyDropdownList(text), 10).orElseThrow();
        driverTools.click(dropdown);
    }

    public void selectFilterPropertyType(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(FILTER_PROPERTY_TYPE_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getFilterPropertyTypeDropdownList(text), 10).orElseThrow();
        driverTools.click(dropdown);
    }

    public void selectFilterRoomNameType(String text) {
        String value = switch (text) {
            case "CUSTOM" -> "Custom Room Name";
            case "STANDARD" -> "Standard Room Name";
            case "ORIGINAL" -> "Original Room Name";
            default -> text;
        };
        Locator option = new Locator("ROOMNAMETYPE_DROPDOWN_LIST", new By.ByXPath(String.format("//div[@data-testid='dropdown-item' and @aria-label='%s']", value)));
        driverTools.waitForElementToPresence(FILTER_ROOM_NAME_TYPE_INPUT).orElseThrow();
        driverTools.scrollIntoWebView(FILTER_ROOM_NAME_TYPE_INPUT);
        driverTools.clickWithJavascriptExecutor(FILTER_ROOM_NAME_TYPE_ICON);
        driverTools.waitForElementToPresence(option, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(option);
    }

    public void inputFilterRoomName(String text) {
        if (driverTools.waitForElementToPresence(FILTER_ROOM_NAME_INPUT, 5).isEmpty()) {
            return;
        }
        driverTools.scrollIntoWebView(FILTER_ROOM_NAME_INPUT);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(FILTER_ROOM_NAME_INPUT, text);
    }

    public void clickSearch() {
        driverTools.waitForElementToPresence(SEARCH_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public void clickExportToCsv() {
        WebElement button = driverTools.waitForElementToBeClickable(EXPORT_TO_CSV_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isSearchResultAvailableByHotelName(String text) {
WebElement cell = driverTools.waitForElementToPresence(getSearchResultHotelNameCell(1), 20).orElseThrow();
        return driverTools.getText(cell).equals(text);
    }

    public boolean isSearchResultAvailableByPropertyType(String text) {
        WebElement cell = driverTools.waitForElementToPresence(getSearchResultPropertyTypeCell(1), 20).orElseThrow();
        return driverTools.getText(cell).equals(text);
    }

    public boolean isSearchResultAvailableByOriginalRoomName(String text) {
        return driverTools.waitForElementToPresence(getTableCellContaining(text), 20).isPresent();
    }

    public boolean isSearchResultAvailableByRoomType(String text) {
        WebElement cell = driverTools.waitForElementToPresence(getSearchResultRoomTypeCell(1), 20).orElseThrow();
        return driverTools.getText(cell).equals(text);
    }

    public boolean isSearchResultAvailableByBedType(String text) {
        WebElement cell = driverTools.waitForElementToPresence(getSearchResultBedTypeCell(1), 20).orElseThrow();
        return driverTools.getText(cell).equals(text);
    }

    public boolean isSearchResultAvailableByNoOfRooms(String text) {
        WebElement cell = driverTools.waitForElementToPresence(getSearchResultNoOfRoomsCell(1), 20).orElseThrow();
        return driverTools.getText(cell).equals(text);
    }

    public boolean isSearchResultAvailableBySmokingPolicy(String text) {
        WebElement cell = driverTools.waitForElementToPresence(getSearchResultSmokingPolicyCell(1), 20).orElseThrow();
        return driverTools.getText(cell).equals(text);
    }

    public boolean isSearchResultAvailableByAttribute(String attribute, String text) {
        return driverTools.waitForElementToPresence(getTableCellContaining(text), 20).isPresent();
    }

    public void checkRoomListSearchResultByIndex(int index) {
        driverTools.waitForElementToPresence(getSearchResultCheckboxCell(index)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(getSearchResultCheckboxCell(index));
    }

    public void clickEditRoomMasterData() {
        driverTools.waitForElementToPresence(EDIT_ROOM_MASTER_DATA_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(EDIT_ROOM_MASTER_DATA_BUTTON);
        driverTools.waitForElementToAppear(EDIT_ROOM_MASTER_DATA_MODAL, 10);
    }

    public boolean isEditRoomMasterDataModalDisplayed() {
        return driverTools.waitForElementToAppear(EDIT_ROOM_MASTER_DATA_MODAL, 10).isPresent();
    }

    public void selectCategoryByIndex(int index, String type, String value) {
        Locator typeInput = getEditRoomMasterDataCategoryTypeInputByIndex(index);
        driverTools.waitForElementToPresence(typeInput).orElseThrow();
        driverTools.clickWithJavascriptExecutor(typeInput);
        driverTools.waitForElementToPresence(getEditRoomMasterDataCategoryTypeDropdownListByIndex(index, type), 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(getEditRoomMasterDataCategoryTypeDropdownListByIndex(index, type));
        Locator valueInput = getEditRoomMasterDataCategoryValueInputByIndex(index);
        driverTools.waitForElementToPresence(valueInput, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(getEditRoomMasterDataCategoryValueIconByIndex(index));
        driverTools.waitForElementToPresence(getEditRoomMasterDataCategoryValueDropdownList(value), 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(getEditRoomMasterDataCategoryValueDropdownList(value));
    }

    public void clickAddAnotherCategory() {
        WebElement button = driverTools.waitForElementToBeClickable(ADD_ANOTHER_CATEGORY_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void submitEditRoomMasterData() {
        driverTools.waitForElementToPresence(EDIT_ROOM_MASTER_DATA_SUBMIT_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(EDIT_ROOM_MASTER_DATA_SUBMIT_BUTTON);
    }

    public boolean isSuccessfulSnackbarDisplayed() {
        return driverTools.waitForElementToAppear(SUCCESSFUL_SNACKBAR, 10).isPresent();
    }

    public void scrollToRoomListTable() {
        WebElement table = driverTools.waitForElementToPresence(ROOM_LIST_TABLE).orElseThrow();
        driverTools.scrollIntoWebView(table);
    }
}
