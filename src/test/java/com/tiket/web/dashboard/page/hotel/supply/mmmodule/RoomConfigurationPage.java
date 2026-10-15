package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RoomConfigurationPage <T extends WebDriver> extends BasePage<T> {
    public RoomConfigurationPage(Session<T> session) {
        super(session);
    }

    private static final Locator ROOM_CONFIGURATION_TITLE_LABEL = new Locator("ROOM_CONFIGURATION_TITLE_LABEL", new By.ByXPath("//label[contains(@class,'title')][text()='Room Configuration']"));
    private static final Locator CUSTOM_ROOM_NAME_PAGE = new Locator("CUSTOM_ROOM_NAME_PAGE", new By.ById("custom-room-name-list"));
    private static final Locator CUSTOM_ROOM_NAME_FILTER_INPUT = new Locator("CUSTOM_ROOM_NAME_FILTER_INPUT", new By.ByXPath("//input[@data-testid='custom-room-name-filter-input']"));
    private static final Locator CUSTOM_ROOM_NAME_SEARCH_BUTTON = new Locator("CUSTOM_ROOM_NAME_SEARCH_BUTTON", new By.ByXPath("//button[@data-testid='custom-room-name-search-btn']"));
    private static final Locator CUSTOM_ROOM_NAME_NO_DATA = new Locator("CUSTOM_ROOM_NAME_NO_DATA", new By.ByXPath("//div[@class='no-data']"));
    private static final Locator ADD_CUSTOM_ROOM_NAME_BUTTON = new Locator("ADD_CUSTOM_ROOM_NAME_BUTTON", new By.ByXPath("//button[@class='the-button primary add-crn-btn  ']"));
    private static final Locator ADD_CUSTOM_ROOM_NAME_FORM = new Locator("ADD_CUSTOM_ROOM_NAME_FORM", new By.ById("custom-room-name-create"));
    private static final Locator ADD_CUSTOM_ROOM_NAME_CHAIN_AND_BRAND_TAB = new Locator("ADD_CUSTOM_ROOM_NAME_CHAIN_AND_BRAND_TAB", new By.ByXPath("//div[@class='tmdf-button-group primary']/button[text()='Chain & Brand']"));
    private static final Locator ADD_CUSTOM_ROOM_NAME_HOTEL_CHAIN_INPUT = new Locator("ADD_CUSTOM_ROOM_NAME_HOTEL_CHAIN_INPUT", new By.ByXPath("//div[@id='chainId']//input"));
    private static final Locator ADD_CUSTOM_ROOM_NAME_HOTEL_NAME_TAB = new Locator("ADD_CUSTOM_ROOM_NAME_HOTEL_NAME_TAB", new By.ByXPath("//div[@class='tmdf-button-group primary']/button[contains(text(),'Hotel')]"));
    private static final Locator ADD_CUSTOM_ROOM_NAME_COUNTRY_INPUT = new Locator("ADD_CUSTOM_ROOM_NAME_COUNTRY_INPUT", new By.ByXPath("//div[@id='countryId']//input[@class='input   ']"));
    private static final Locator ADD_CUSTOM_ROOM_NAME_REGION_INPUT = new Locator("ADD_CUSTOM_ROOM_NAME_REGION_INPUT", new By.ByXPath("(//div[@class='tmdf-location-select'])[2]//input[@class='input   ']"));
    private static final Locator ADD_CUSTOM_ROOM_NAME_CITY_INPUT = new Locator("ADD_CUSTOM_ROOM_NAME_CITY_INPUT", new By.ByXPath("(//div[@class='tmdf-location-select'])[3]//input[@class='input   ']"));
    private static final Locator ADD_CUSTOM_ROOM_NAME_AREA_INPUT = new Locator("ADD_CUSTOM_ROOM_NAME_AREA_INPUT", new By.ByXPath("(//div[@class='tmdf-location-select'])[4]//input[@class='input   ']"));
    private static final Locator ADD_CUSTOM_ROOM_NAME_HOTEL_NAME_INPUT = new Locator("ADD_CUSTOM_ROOM_NAME_HOTEL_NAME_INPUT", new By.ByXPath("//div[@id='hotelId']//input[@class='input   ']"));
    private static final Locator ADD_CUSTOM_ROOM_NAME_KEYWORD_INPUT = new Locator("ADD_CUSTOM_ROOM_NAME_KEYWORD_INPUT", new By.ByXPath("//input[@class='tag-input']"));
    private static final Locator ADD_CUSTOM_ROOM_NAME_SEARCH_ROOM_BUTTON = new Locator("ADD_CUSTOM_ROOM_NAME_SEARCH_ROOM_BUTTON", new By.ByXPath("(//button[@type='submit'])[1]"));
    private static final Locator CUSTOM_ROOM_NAME_EN_INPUT = new Locator("CUSTOM_ROOM_NAME_EN_INPUT", new By.ByXPath("//input[@name='en']"));
    private static final Locator CUSTOM_ROOM_NAME_ID_INPUT = new Locator("CUSTOM_ROOM_NAME_ID_INPUT", new By.ByXPath("//input[@name='id']"));
    private static final Locator ADD_CUSTOM_ROOM_NAME_SUBMIT_BUTTON = new Locator("ADD_CUSTOM_ROOM_NAME_SUBMIT_BUTTON", new By.ByXPath("(//button[@type='submit'])[2]"));
    private static final Locator UPDATE_CUSTOM_ROOM_NAME_FORM = new Locator("UPDATE_CUSTOM_ROOM_NAME_FORM", new By.ById("custom-room-name-update"));
    private static final Locator UPDATE_CUSTOM_ROOM_NAME_EDIT_NAME_BUTTON = new Locator("UPDATE_CUSTOM_ROOM_NAME_EDIT_NAME_BUTTON", new By.ByXPath("//button[contains(text(),'Edit')]"));
    private static final Locator UPDATE_CUSTOM_ROOM_NAME_SUBMIT_BUTTON = new Locator("UPDATE_CUSTOM_ROOM_NAME_SUBMIT_BUTTON", new By.ByXPath("(//button[contains(@class,'the-button primary')])[1]"));
    private static final Locator REMOVE_CUSTOM_ROOM_NAME_MODAL = new Locator("REMOVE_CUSTOM_ROOM_NAME_MODAL", new By.ByXPath("//div[@class='the-modal show remove-custom-room-name-modal']"));
    private static final Locator CONFIRM_REMOVE_CUSTOM_ROOM_NAME_BUTTON = new Locator("CONFIRM_REMOVE_CUSTOM_ROOM_NAME_BUTTON", new By.ByXPath("//button[@class='the-button ghost confirm-remove-crn-btn text-red  ']"));
    private static final Locator SUCCESSFUL_SNACKBAR = new Locator("SUCCESSFUL_SNACKBAR", new By.ByXPath("//div[contains(@class,'Snackbar_text_content')]"));
    private static final Locator PAGE_TITLE = new Locator("PAGE_TITLE", new By.ByXPath("(//div[contains(@class, 'grid w-full')]//span)[1]"));
    private static final Locator CREATE_NEW = new Locator("CREATE_NEW", new By.ByXPath("//button[contains(text(),'Create new')]"));
    private static final Locator EN_INPUT = new Locator("EN_INPUT", new By.ByXPath("//input[@name='name.en']"));
    private static final Locator ID_INPUT = new Locator("ID_INPUT", new By.ByXPath("//input[@name='name.id']"));
    private static final Locator SELECT_TYPE = new Locator("SELECT_TYPE", new By.ByXPath("(//input[@id='type'])[4]"));
    private static final Locator TYPE_DROPDOWN = new Locator("TYPE_DROPDOWN", new By.ByXPath("//div[@data-testid='dropdown-item']//span[text()='Nationality']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", new By.ByXPath("//button[@type='submit']"));

    private static final Locator SEARCH_INPUT = new Locator("SEARCH_INPUT", new By.ByXPath("//input[@placeholder='Type to Search']"));
    private static final Locator SUBMIT_SEARCH = new Locator("SUBMIT_SEARCH", new By.ByXPath("//button[normalize-space()='Search']"));
    private static final Locator ACTION_SETTING = new Locator("ACTION_SETTING", new By.ByXPath("//input[@id='settings']"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", new By.ByXPath("//span[normalize-space()='Edit']"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("//span[normalize-space()='Delete']"));
    private static final Locator DELETE = new Locator("DELETE", new By.ByXPath("//button[normalize-space()='Delete']"));
    private static final Locator NO_DATA = new Locator("NO_DATA", new By.ByXPath("//p[contains(text(),'No data available')]"));
    private enum RestrictionColumn {
        EN(2),
        ID(3),
        TYPE(4),
        LAST_UPDATED(5),
        UPDATED_BY(6),
        ACTION(7);

        private final int position;

        RestrictionColumn(int position) {
            this.position = position;
        }
    }

    private Locator getRestrictionColumnHeader(RestrictionColumn column) {
        return new Locator("RESTRICTION_COLUMN_HEADER", new By.ByXPath(String.format("(//th//div[contains(@class, 'flex items-center')])[%d]", column.position)));
    }

    private Locator getRestrictionColumnValue(RestrictionColumn column) {
        return new Locator("RESTRICTION_COLUMN_VALUE", new By.ByXPath(String.format("(//tr//td[contains(@class, 'leading')])[%d]", column.position)));
    }

    private Locator getRoomConfigurationTab(String value) {
        return new Locator("ROOM_CONFIGURATION_TAB", new By.ByXPath(String.format("//div[@class='the-tab-edit']/div[contains(@class,'the-tab ')][text()='%s']", value)));
    }

    private Locator getCustomRoomNameENByRowIndex(int index) {
        return new Locator("CUSTOM_ROOM_NAME_EN_LABEL", new By.ByXPath(String.format("((//div[@class='body']/div[contains(@class,'row ')])[%d]/div[contains(@class,'cell false ')])[2]", index)));
    }

    private Locator getCustomRoomNameIDByRowIndex(int index) {
        return new Locator("CUSTOM_ROOM_NAME_ID_LABEL", new By.ByXPath(String.format("((//div[@class='body']/div[contains(@class,'row ')])[%d]/div[contains(@class,'cell false ')])[3]", index)));
    }

    private Locator getCustomRoomNameSettingButtonByRowIndex(int index) {
        return new Locator("CUSTOM_ROOM_NAME_SETTING_BUTTON", new By.ByXPath(String.format("((//div[@class='body']/div[contains(@class,'row ')])[%d]//img[@data-testid='toggle-action-list-btn'])", index)));
    }

    private Locator getCustomRoomNameViewButtonByRowIndex(int index) {
        return new Locator("CUSTOM_ROOM_NAME_VIEW_BUTTON", new By.ByXPath(String.format("(//div[@class='body']/div[contains(@class,'row ')])[%d]//div[@class='the-popoverlist']//span[@class='tixicon tixicon-edit ']/parent::span", index)));
    }

    private Locator getUpdateCustomRoomNameEnLabel(String name) {
        return new Locator("UPDATE_CUSTOM_ROOM_NAME_EN_LABEL", new By.ByXPath(String.format("//div[text()='ENG']/following-sibling::div[text()='%s']", name)));
    }

    private Locator getUpdateCustomRoomNameIdLabel(String name) {
        return new Locator("UPDATE_CUSTOM_ROOM_NAME_ID_LABEL", new By.ByXPath(String.format("//div[text()='IND']/following-sibling::div[text()='%s']", name)));
    }

    private Locator getCustomRoomNameDeleteButtonByRowIndex(int index) {
        return new Locator("CUSTOM_ROOM_NAME_DELETE_BUTTON", new By.ByXPath(String.format("(//div[@class='body']/div[contains(@class,'row ')])[%d]//div[@class='the-popoverlist']//span[@class='tixicon tixicon-delete-bucket ']/parent::span", index)));
    }

    private Locator getAddCustomRoomNameHotelChainDropdownValue(String value) {
        return new Locator("HOTEL_CHAIN_DROPDOWN_VALUE", new By.ByXPath(String.format("//div[@id='chainId']//div[contains(@class,'the-popoverlist')]//label[text()='%s']/parent::span", value)));
    }

    private Locator getAddCustomRoomNameCountryDropdownValue(String value) {
        return new Locator("COUNTRY_DROPDOWN_VALUE", new By.ByXPath(String.format("//div[@id='countryId']//div[contains(@class,'the-popoverlist')]//label[text()='%s']/parent::span", value)));
    }

    private Locator getAddCustomRoomNameRegionDropdownValue(String value) {
        return new Locator("REGION_DROPDOWN_VALUE", new By.ByXPath(String.format("(//div[@class='tmdf-location-select'])[2]//div[contains(@class,'the-popoverlist')]//label[text()='%s']/parent::span", value)));
    }

    private Locator getAddCustomRoomNameCityDropdownValue(String value) {
        return new Locator("CITY_DROPDOWN_VALUE", new By.ByXPath(String.format("(//div[@class='tmdf-location-select'])[3]//div[contains(@class,'the-popoverlist')]//label[text()='%s']/parent::span", value)));
    }

    private Locator getAddCustomRoomNameAreaDropdownValue(String value) {
        return new Locator("AREA_DROPDOWN_VALUE", new By.ByXPath(String.format("(//div[@class='tmdf-location-select'])[4]//div[contains(@class,'the-popoverlist')]//label[text()='%s']/parent::span", value)));
    }

    private Locator getAddCustomRoomNameHotelNameDropdownValue(String value) {
        return new Locator("HOTEL_NAME_DROPDOWN_VALUE", new By.ByXPath(String.format("//div[@id='hotelId']//div[contains(@class,'the-popoverlist')]//label[text()='%s']/parent::span", value)));
    }

    public boolean isRoomConfigurationPageDisplayed() {
        WebElement label = driverTools.waitForElementToPresence(ROOM_CONFIGURATION_TITLE_LABEL).orElseThrow();
        return driverTools.isElementDisplayed(label);
    }

    public void clickRoomConfigurationTab(String value) {
        WebElement tab = driverTools.waitForElementToPresence(getRoomConfigurationTab(value)).orElseThrow();
        driverTools.click(tab);
    }

    public boolean isCustomRoomNameListDisplayed() {
        WebElement page = driverTools.waitForElementToPresence(CUSTOM_ROOM_NAME_PAGE).orElseThrow();
        return driverTools.isElementDisplayed(page);
    }

    public void inputCustomRoomNameFilter(String value) {
        WebElement input = driverTools.waitForElementToPresence(CUSTOM_ROOM_NAME_FILTER_INPUT).orElseThrow();
        driverTools.clear(input);
        driverTools.sendKeys(input, value);
    }

    public void clickSearchCustomRoomName() {
        WebElement searchButton = driverTools.waitForElementToPresence(CUSTOM_ROOM_NAME_SEARCH_BUTTON).orElseThrow();
        driverTools.click(searchButton);
    }

    public String getEnCustomRoomNameLabel(int index) {
        WebElement label = driverTools.waitForElementToPresence(getCustomRoomNameENByRowIndex(index)).orElseThrow();
        return driverTools.getText(label);
    }

    public String getIdCustomRoomNameLabel(int index) {
        WebElement label = driverTools.waitForElementToPresence(getCustomRoomNameIDByRowIndex(index)).orElseThrow();
        return driverTools.getText(label);
    }

    public boolean isCustomRoomNameSearchResultAppeared(int index, String en, String id) {
        WebElement enLabel = driverTools.waitForElementToPresence(getCustomRoomNameENByRowIndex(index)).orElseThrow();
        WebElement idLabel = driverTools.waitForElementToPresence(getCustomRoomNameIDByRowIndex(index)).orElseThrow();
        return driverTools.getText(enLabel).contains(en) && driverTools.getText(idLabel).contains(id);
    }

    public boolean isCustomRoomNameSearchResultNotFound() {
        WebElement element = driverTools.waitForElementToPresence(CUSTOM_ROOM_NAME_NO_DATA).orElseThrow();
        return driverTools.isElementDisplayed(element);
    }

    public void clickAddCustomRoomName() {
        WebElement addButton = driverTools.waitForElementToPresence(ADD_CUSTOM_ROOM_NAME_BUTTON).orElseThrow();
        driverTools.click(addButton);
    }

    public boolean isAddCustomRoomNameFormDisplayed() {
        WebElement form = driverTools.waitForElementToPresence(ADD_CUSTOM_ROOM_NAME_FORM).orElseThrow();
        return driverTools.isElementDisplayed(form);
    }

    public void clickChainAndBrandTab() {
        WebElement tab = driverTools.waitForElementToPresence(ADD_CUSTOM_ROOM_NAME_CHAIN_AND_BRAND_TAB).orElseThrow();
        driverTools.click(tab);
    }

    public void searchAndSelectHotelChain(String value) {
        WebElement input = driverTools.waitForElementToPresence(ADD_CUSTOM_ROOM_NAME_HOTEL_CHAIN_INPUT).orElseThrow();
        driverTools.click(input);
        driverTools.clear(input);
        driverTools.sendKeys(input, value);
        WebElement dropdown = driverTools.waitForElementToPresence(getAddCustomRoomNameHotelChainDropdownValue(value)).orElseThrow();
        driverTools.click(dropdown);
    }

    public void clickHotelNameTab() {
        WebElement tab = driverTools.waitForElementToPresence(ADD_CUSTOM_ROOM_NAME_HOTEL_NAME_TAB).orElseThrow();
        driverTools.click(tab);
    }

    public void searchAndSelectCountry(String value) {
        WebElement input = driverTools.waitForElementToPresence(ADD_CUSTOM_ROOM_NAME_COUNTRY_INPUT).orElseThrow();
        driverTools.click(input);
        driverTools.clear(input);
        driverTools.sendKeys(input, value);
        WebElement dropdown = driverTools.waitForElementToPresence(getAddCustomRoomNameCountryDropdownValue(value)).orElseThrow();
        driverTools.click(dropdown);
    }

    public void searchAndSelectRegion(String value) {
        WebElement input = driverTools.waitForElementToPresence(ADD_CUSTOM_ROOM_NAME_REGION_INPUT).orElseThrow();
        driverTools.click(input);
        driverTools.clear(input);
        driverTools.sendKeys(input, value);
        WebElement dropdown = driverTools.waitForElementToPresence(getAddCustomRoomNameRegionDropdownValue(value)).orElseThrow();
        driverTools.click(dropdown);
    }

    public void searchAndSelectCity(String value) {
        WebElement input = driverTools.waitForElementToPresence(ADD_CUSTOM_ROOM_NAME_CITY_INPUT).orElseThrow();
        driverTools.click(input);
        driverTools.clear(input);
        driverTools.sendKeys(input, value);
        WebElement dropdown = driverTools.waitForElementToPresence(getAddCustomRoomNameCityDropdownValue(value)).orElseThrow();
        driverTools.click(dropdown);
    }

    public void searchAndSelectArea(String value) {
        WebElement input = driverTools.waitForElementToPresence(ADD_CUSTOM_ROOM_NAME_AREA_INPUT, 30).orElseThrow();
        driverTools.click(input);
        driverTools.clear(input);
        driverTools.sendKeys(input, value);
        WebElement dropdown = driverTools.waitForElementToPresence(getAddCustomRoomNameAreaDropdownValue(value)).orElseThrow();
        driverTools.click(dropdown);
    }

    public void searchAndSelectHotelName(String value) {
        WebElement input = driverTools.waitForElementToPresence(ADD_CUSTOM_ROOM_NAME_HOTEL_NAME_INPUT).orElseThrow();
        driverTools.click(input);
        driverTools.clear(input);
        driverTools.sendKeys(input, value);
        WebElement dropdown = driverTools.waitForElementToPresence(getAddCustomRoomNameHotelNameDropdownValue(value)).orElseThrow();
        driverTools.click(dropdown);
    }

    public void inputKeyword(String value) {
        WebElement input = driverTools.waitForElementToPresence(ADD_CUSTOM_ROOM_NAME_KEYWORD_INPUT).orElseThrow();
        driverTools.clear(input);
        driverTools.sendKeys(input, value);
    }

    public void clickSearchRoom() {
        WebElement searchButton = driverTools.waitForElementToPresence(ADD_CUSTOM_ROOM_NAME_SEARCH_ROOM_BUTTON).orElseThrow();
        driverTools.click(searchButton);
    }

    public void inputCustomRoomName(String en, String id) {
        WebElement enInput = driverTools.waitForElementToPresence(CUSTOM_ROOM_NAME_EN_INPUT).orElseThrow();
        WebElement idInput = driverTools.waitForElementToPresence(CUSTOM_ROOM_NAME_ID_INPUT).orElseThrow();
        driverTools.clear(enInput);
        driverTools.sendKeys(enInput, en);
        driverTools.clear(idInput);
        driverTools.sendKeys(idInput, id);
    }

    public void clickSubmitAddNewCustomRoomName() {
        WebElement submit = driverTools.waitForElementToBeClickable(ADD_CUSTOM_ROOM_NAME_SUBMIT_BUTTON).orElseThrow();
        driverTools.click(submit);
    }

    public void clickSettingCustomRoomName(int index) {
        WebElement settingButton = driverTools.waitForElementToBeClickable(getCustomRoomNameSettingButtonByRowIndex(index)).orElseThrow();
        driverTools.click(settingButton);
    }

    public void clickViewCustomRoomName(int index) {
        WebElement viewButton = driverTools.waitForElementToBeClickable(getCustomRoomNameViewButtonByRowIndex(index)).orElseThrow();
        driverTools.click(viewButton);
    }

    public boolean isUpdateCustomRoomNameFormDisplayed() {
        WebElement form = driverTools.waitForElementToPresence(UPDATE_CUSTOM_ROOM_NAME_FORM).orElseThrow();
        return driverTools.isElementDisplayed(form);
    }

    public boolean isUpdateCustomRoomNameLabelDisplayedCorrectly(String en, String id) {
        WebElement enLabel = driverTools.waitForElementToPresence(getUpdateCustomRoomNameEnLabel(en), 10).orElseThrow();
        WebElement idLabel = driverTools.waitForElementToPresence(getUpdateCustomRoomNameIdLabel(id), 10).orElseThrow();
        return driverTools.getText(enLabel).equals(en) && driverTools.getText(idLabel).equals(id);
    }

    public void clickEditCustomRoomName() {
        WebElement editButton = driverTools.waitForElementToPresence(UPDATE_CUSTOM_ROOM_NAME_EDIT_NAME_BUTTON).orElseThrow();
        driverTools.click(editButton);
    }

    public void clickSubmitUpdateCustomRoomName() {
        WebElement submit = driverTools.waitForElementToPresence(UPDATE_CUSTOM_ROOM_NAME_SUBMIT_BUTTON).orElseThrow();
        driverTools.click(submit);
    }

    public void clickDeleteCustomRoomName(int index) {
        WebElement deleteButton = driverTools.waitForElementToBeClickable(getCustomRoomNameDeleteButtonByRowIndex(index)).orElseThrow();
        driverTools.click(deleteButton);
    }

    public boolean isDeleteCustomRoomNameConfirmModalDisplayed() {
        WebElement modal = driverTools.waitForElementToAppear(REMOVE_CUSTOM_ROOM_NAME_MODAL).orElseThrow();
        return driverTools.isElementDisplayed(modal);
    }

    public void clickSubmitDeleteCustomRoomName() {
        WebElement submit = driverTools.waitForElementToPresence(CONFIRM_REMOVE_CUSTOM_ROOM_NAME_BUTTON).orElseThrow();
        driverTools.click(submit);
    }

    public boolean isSuccessfulSnackbarAppeared() {
        WebElement snackbar = driverTools.waitForElementToAppear(SUCCESSFUL_SNACKBAR).orElseThrow();
        return driverTools.isElementDisplayed(snackbar);
    }
    public boolean isRoomConfigurationPageOpened(){
        driverTools.dummyWait(2);
        String title=driverTools.getText(PAGE_TITLE).orElseThrow();
        return title.equalsIgnoreCase("room configuration");
    }

    public void createNewConfiguration(String eng,String id){
        driverTools.waitForElementToAppear(CREATE_NEW);
        driverTools.click(CREATE_NEW);

        driverTools.waitForElementToAppear(EN_INPUT);
        driverTools.sendKeys(EN_INPUT,eng);
        driverTools.sendKeys(ID_INPUT,id);

        driverTools.click(SELECT_TYPE);

        driverTools.waitForElementToAppear(TYPE_DROPDOWN);
        driverTools.click(TYPE_DROPDOWN);
        driverTools.click(SUBMIT_BUTTON);
    }

    public String getTextFromSnackbar(){
        driverTools.waitForElementToAppear(SUCCESSFUL_SNACKBAR);
        return driverTools.getText(SUCCESSFUL_SNACKBAR).orElseThrow();
    }

    public void searchRestriction(String name){
        driverTools.waitForElementToAppear(SEARCH_INPUT);
        driverTools.clearInput(SEARCH_INPUT);
        driverTools.sendKeys(SEARCH_INPUT,name);
        driverTools.click(SUBMIT_SEARCH);

        driverTools.dummyWait(2);
    }

    public boolean verifyColumnHeaders(){
        boolean flag=false;

        String res_en=driverTools.getText(getRestrictionColumnHeader(RestrictionColumn.EN)).orElseThrow();
        String res_id=driverTools.getText(getRestrictionColumnHeader(RestrictionColumn.ID)).orElseThrow();
        String res_type=driverTools.getText(getRestrictionColumnHeader(RestrictionColumn.TYPE)).orElseThrow();
        String res_last_updated=driverTools.getText(getRestrictionColumnHeader(RestrictionColumn.LAST_UPDATED)).orElseThrow();
        String res_updated_by=driverTools.getText(getRestrictionColumnHeader(RestrictionColumn.UPDATED_BY)).orElseThrow();
        String res_action=driverTools.getText(getRestrictionColumnHeader(RestrictionColumn.ACTION)).orElseThrow();


        flag = res_en.equalsIgnoreCase("Restrictions (EN)")
                && res_id.equalsIgnoreCase("Restrictions (ID)")
                && res_type.equalsIgnoreCase("Type")
                && res_last_updated.equalsIgnoreCase("Last Updated")
                && res_updated_by.equalsIgnoreCase("Updated by")
                && res_action.equalsIgnoreCase("Action");

        return flag;
    }

    public String getRestrictionEnName() {
        Locator enValue = getRestrictionColumnValue(RestrictionColumn.EN);
        driverTools.waitForElementToAppear(enValue);
        return driverTools.getText(enValue).orElseThrow();
    }

    public String getRestrictionIdName() {
        Locator idValue = getRestrictionColumnValue(RestrictionColumn.ID);
        driverTools.waitForElementToAppear(idValue);
        return driverTools.getText(idValue).orElseThrow();
    }

    public String getRestrictionType() {
        Locator typeValue = getRestrictionColumnValue(RestrictionColumn.TYPE);
        driverTools.waitForElementToAppear(typeValue);
        return driverTools.getText(typeValue).orElseThrow();
    }

    public String getLastUpdated() {
        Locator lastUpdatedValue = getRestrictionColumnValue(RestrictionColumn.LAST_UPDATED);
        driverTools.waitForElementToAppear(lastUpdatedValue);
        return driverTools.getText(lastUpdatedValue).orElseThrow();
    }

    public String getUpdatedBy() {
        Locator updatedByValue = getRestrictionColumnValue(RestrictionColumn.UPDATED_BY);
        driverTools.waitForElementToAppear(updatedByValue);
        return driverTools.getText(updatedByValue).orElseThrow();
    }

    public void editRoomConfiguration(String eng,String id){
        driverTools.waitForElementToDisappear(SUCCESSFUL_SNACKBAR);
        driverTools.waitForElementToAppear(ACTION_SETTING);
        driverTools.click(ACTION_SETTING);
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        driverTools.click(EDIT_BUTTON);


        driverTools.waitForElementToAppear(EN_INPUT,5);
        driverTools.click(EN_INPUT);
        driverTools.clearInput(EN_INPUT);
        driverTools.sendKeys(EN_INPUT,"Edited"+eng);

        driverTools.click(ID_INPUT);
        driverTools.clearInput(ID_INPUT);
        driverTools.sendKeys(ID_INPUT,"Edited"+id);
        driverTools.click(SUBMIT_BUTTON);
    }

    public void deleteRoomRestriction(){
        driverTools.waitForElementToDisappear(SUCCESSFUL_SNACKBAR);
        driverTools.waitForElementToAppear(ACTION_SETTING);
        driverTools.click(ACTION_SETTING);
        driverTools.waitForElementToAppear(DELETE_BUTTON);
        driverTools.click(DELETE_BUTTON);

        driverTools.waitForElementToAppear(DELETE);
        driverTools.click(DELETE);
    }

    public boolean verifyNoData(){
        driverTools.waitForElementToAppear(NO_DATA,1);
        return driverTools.isElementDisplayed(NO_DATA);

    }
}
