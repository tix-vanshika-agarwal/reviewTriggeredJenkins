package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RoomDataPage <T extends WebDriver> extends BasePage<T> {
    public RoomDataPage(Session<T> session) {
        super(session);
    }

    private static final Locator ROOM_DATA_PAGE = new Locator("ROOM_DATA_PAGE", new By.ByXPath("//div[@class='the-room-data']"));
    private static final Locator ADD_NEW_ROOM_BUTTON = new Locator("ADD_NEW_ROOM_BUTTON", new By.ByXPath("//button[contains(@class,'the-room-data-create-room')]"));
    private static final Locator SEARCH_ROOM_INPUT = new Locator("SEARCH_ROOM_INPUT", new By.ByXPath("//div[@class=\"the-room-data-search\"]//input[@type=\"text\"]"));
    private static final Locator SEARCH_ROOM_BUTTON = new Locator("SEARCH_ROOM_BUTTON", new By.ByXPath("//div[@class=\"the-room-data-search\"]//button"));
    private static final Locator CREATE_ROOM_DATA_PAGE = new Locator("CREATE_ROOM_DATA_PAGE", new By.ByXPath("//div[@class='the-create-room-data']"));
    private static final Locator ORIGINAL_ROOM_NAME_INPUT = new Locator("ORIGINAL_ROOM_NAME_INPUT", new By.ByXPath("//input[@name='name']"));
    private static final Locator ROOM_TYPE_INPUT = new Locator("ROOM_TYPE_INPUT", new By.ByXPath("//input[@name='roomType']"));
    private static final Locator EN_DESCRIPTION_IFRAME = new Locator("EN_DESCRIPTION_IFRAME", new By.ByXPath("//iframe[@id='descriptionEn_ifr']"));
    private static final Locator EN_DESCRIPTION_TEXTAREA = new Locator("EN_DESCRIPTION_TEXTAREA", new By.ByXPath("//body[@id='tinymce']/p"));
    private static final Locator ADD_NUM_OF_ROOM_BUTTON = new Locator("ADD_NUM_OF_ROOM_BUTTON", new By.ByXPath("(//button[contains(@class, 'right')])[1]"));
    private static final Locator MINIMUM_SELL_PRICE_INPUT = new Locator("MINIMUM_SELL_PRICE_INPUT", new By.ByXPath("//input[contains(@name, 'minimumSellPrice')]"));
    private static final Locator NO_SMOKING_RADIO = new Locator("NO_SMOKING_RADIO", new By.ByXPath("(//label[contains(@class, 'radio-container')])[1]"));
    private static final Locator AVG_ROOM_SIZE_INPUT = new Locator("AVG_ROOM_SIZE_INPUT", new By.ByXPath("(//input[@name='size'])"));
    private static final Locator ADD_NUM_OF_BATHROOM_BUTTON = new Locator("ADD_NUM_OF_BATHROOM_BUTTON", new By.ByXPath("(//button[contains(@class, 'btn-primary') and text()='+'])[3]"));
    private static final Locator BATHROOM_TYPE=new Locator("BATHROOM_TYPE",new By.ByXPath("//label[contains(@class,'radio-container') and .//input[@value='PRIVATE']]"));
    private static final Locator PRIVATE_BATHROOM_RADIO = new Locator("PRIVATE_BATHROOM_RADIO", new By.ByXPath("(//label[contains(@class, 'radio-container')])[4]"));
    private static final Locator ADULT_OCCUPANCY_TRIGGER_INPUT = new Locator("ADULT_OCCUPANCY_TRIGGER_INPUT", new By.ByXPath("//input[@name='maxOccupancy' and contains(@id, 'maxOccupancy')]"));
    private static final Locator TWO_ADULT_OCCUPANCY_INPUT = new Locator("TWO_ADULT_OCCUPANCY_INPUT", new By.ByXPath("(//span[contains(@class, 'popoverlist-list')]/label[text()='2'])[1]"));
    private static final Locator CHILD_OCCUPANCY_TRIGGER_INPUT = new Locator("CHILD_OCCUPANCY_TRIGGER_INPUT", new By.ByXPath("(//input[@name='childOccupancy'])"));
    private static final Locator ZERO_CHILD_OCCUPANCY_INPUT = new Locator("ZERO_CHILD_OCCUPANCY_INPUT", new By.ByXPath("(//span[contains(@class, 'popoverlist-list')]/label[text()='0'])[1]"));
    private static final Locator NO_MULTIPLE_BEDROOM = new Locator("NO_MULTIPLE_BEDROOM", new By.ByXPath("//input[@name='hasMultipleRoom' and @value='no']/ancestor::label"));
    private static final Locator BED_TYPE_TRIGGER_INPUT = new Locator("BED_TYPE_TRIGGER_INPUT", new By.ByXPath("(//input[@name='room-bed-room1-type0-bedroom1'])"));
    private static final Locator DOUBLE_BED_TYPE_INPUT = new Locator("DOUBLE_BED_TYPE_INPUT", new By.ByXPath("(//span[contains(@class, 'popoverlist-list')]/label[text()='Double'])[1]"));
    private static final Locator NUM_OF_BED_TRIGGER_INPUT = new Locator("NUM_OF_BED_TRIGGER_INPUT", new By.ByXPath("(//input[@name='room-amount-room1-type0-bedroom1'])"));
    private static final Locator ONE_NUM_OF_BED_INPUT = new Locator("ONE_NUM_OF_BED_INPUT", new By.ByXPath("(//span[contains(@class, 'popoverlist-list')]/label[text()='1'])[2]"));
    private static final Locator ROOM_IMAGE = new Locator("ROOM_IMAGE", new By.ByXPath("(//img[@draggable='true'])[1]"));
    private static final Locator ADD_PHOTO_BUTTON = new Locator("ADD_PHOTO_BUTTON", new By.ByXPath("//button[@class='the-btn-upload']"));
    private static final Locator ADD_PHOTO_MODAL_BUTTON = new Locator("ADD_PHOTO_MODAL_BUTTON", new By.ByXPath("//button[@class='the-button primary button']"));
    private static final Locator SET_AS_MAIN_PHOTO_BUTTON = new Locator("SET_AS_MAIN_PHOTO_BUTTON", new By.ByXPath("(//button[contains(@class, 'the-image-label-container')])[1]"));
    private static final Locator ADD_ROOM_BUTTON = new Locator("ADD_ROOM_BUTTON", new By.ByXPath("//button[@class='the-button yellow']"));
    private static final Locator DELETE_CONFIRM_BUTTON = new Locator("DELETE_CONFIRM_BUTTON", new By.ByXPath("//div[@data-testid=\"the-modal\"]//button[contains(@class,'ghost')]"));
    private static final Locator SUCCESS_TOAST = new Locator("SUCCESS_TOAST", new By.ByXPath("//div[@class='the-toast success opened']"));
    private static final Locator ACTIVATE_ROOM_CONFIRM_BUTTON = new Locator("ACTIVATE_ROOM_CONFIRM_BUTTON", new By.ByXPath("//button[@data-testid=\"activate-room-button\"]"));

    private static Locator getRoomTypeDropdownListByName(String text) {
        return new Locator("ROOM_TYPE_DROPDOWN", new By.ByXPath(String.format("//div[@id=\"roomType\"]/following-sibling::div[@class=\"the-popoverlist\"]//label[text()='%s']/parent::span", text)));
    }

    private static Locator getRoomNameLabel(String text) {
        return new Locator("ROOM_NAME_LABEL", new By.ByXPath(String.format("//td[text()='%s']", text)));
    }

    private static Locator getDeleteRoomButtonByRoomName(String text) {
        return new Locator("DELETE_ROOM_BUTTON", new By.ByXPath(String.format("//td[text()='%s']/parent::tr//div[contains(@class,'action-button-delete')]", text)));
    }

    private static Locator getRoomStatusToggleOff(String text) {
        return new Locator("TOGGLE_OFF", new By.ByXPath(String.format("//td[@class=\"table-cell\"][text()='%s']/following-sibling::td[@class=\"table-cell sticky-col-1\"]//span[@class=\"the-toggle-btn the-toggle-btn-off\"]", text)));
    }

    private static Locator getRoomStatusToggleOn(String text) {
        return new Locator("TOGGLE_ON", new By.ByXPath(String.format("//td[@class=\"table-cell\"][text()='%s']/following-sibling::td[@class=\"table-cell sticky-col-1\"]//span[@class=\"the-toggle-btn the-toggle-btn-on\"]", text)));
    }

    public boolean isRoomDataPageLoaded() {
        driverTools.waitForElementToAppear(ROOM_DATA_PAGE);
        return driverTools.isElementDisplayed(ROOM_DATA_PAGE);
    }
    
    public void clickAddNewRoomButton(){
        driverTools.waitForElementToAppear(ADD_NEW_ROOM_BUTTON);
        driverTools.click(ADD_NEW_ROOM_BUTTON);
    }

    public void searchRoomName(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(SEARCH_ROOM_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, text);
    }

    public void clickSearch() {
        WebElement button = driverTools.waitForElementToBeClickable(SEARCH_ROOM_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isCreateRoomDataPageLoaded() {
        driverTools.waitForElementToAppear(CREATE_ROOM_DATA_PAGE);
        return driverTools.isElementDisplayed(CREATE_ROOM_DATA_PAGE);
    }

    private void clickPhotoCheckbox(int index) {
        Locator PHOTO_CHECKBOX = new Locator("PHOTO_CHECKBOX", new By.ByXPath(String.format("(//span[@class='checkbox-style '])[%d]", index)));
        driverTools.waitForElementToPresence(PHOTO_CHECKBOX);
        driverTools.click(PHOTO_CHECKBOX);
    }

    public void fillOriginalRoomNameInput(String input){
        WebElement textField = driverTools.waitForElementToAppear(ORIGINAL_ROOM_NAME_INPUT).orElseThrow();
        driverTools.clearInput(textField);
        driverTools.sendKeys(textField, input);
    }

    public void fillRoomDetailForm(String roomName, String desc, String avgRoomSize, String roomType, String sellPrice){
        fillOriginalRoomNameInput(roomName);

        WebElement input = driverTools.waitForElementToBeClickable(ROOM_TYPE_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement dropdown = driverTools.waitForElementToBeClickable(getRoomTypeDropdownListByName(roomType)).orElseThrow();
        driverTools.click(dropdown);

        WebElement frame = driverTools.waitForElementToAppear(EN_DESCRIPTION_IFRAME).orElseThrow();
        driverTools.switchToIframe(frame);
        WebElement textarea = driverTools.waitForElementToAppear(EN_DESCRIPTION_TEXTAREA).orElseThrow();
        driverTools.sendKeys(textarea, desc);
        driverTools.switchBackFromIframe();
        
        driverTools.waitForElementToAppear(ADD_NUM_OF_ROOM_BUTTON);
        driverTools.click(ADD_NUM_OF_ROOM_BUTTON);

        driverTools.waitForElementToAppear(MINIMUM_SELL_PRICE_INPUT);
        driverTools.sendKeys(MINIMUM_SELL_PRICE_INPUT, sellPrice);
        
        driverTools.waitForElementToAppear(NO_SMOKING_RADIO);
        driverTools.click(NO_SMOKING_RADIO);
        
        driverTools.waitForElementToAppear(AVG_ROOM_SIZE_INPUT);
        driverTools.sendKeys(AVG_ROOM_SIZE_INPUT, avgRoomSize);

        driverTools.waitForElementToAppear(PRIVATE_BATHROOM_RADIO);
        driverTools.click(PRIVATE_BATHROOM_RADIO);

        driverTools.waitForElementToAppear(BATHROOM_TYPE);
        driverTools.click(BATHROOM_TYPE);

        driverTools.waitForElementToAppear(ADD_NUM_OF_BATHROOM_BUTTON);
        driverTools.click(ADD_NUM_OF_BATHROOM_BUTTON);
    }
    
    public void fillBedConfigurationForm() {
        driverTools.waitForElementToAppear(ADULT_OCCUPANCY_TRIGGER_INPUT);
        driverTools.click(ADULT_OCCUPANCY_TRIGGER_INPUT);
        driverTools.waitForElementToAppear(TWO_ADULT_OCCUPANCY_INPUT);
        driverTools.click(TWO_ADULT_OCCUPANCY_INPUT);

        driverTools.waitForElementToAppear(NO_MULTIPLE_BEDROOM);
        driverTools.click(NO_MULTIPLE_BEDROOM);
        
        driverTools.waitForElementToAppear(BED_TYPE_TRIGGER_INPUT);
        driverTools.click(BED_TYPE_TRIGGER_INPUT);
        driverTools.waitForElementToAppear(DOUBLE_BED_TYPE_INPUT);
        driverTools.click(DOUBLE_BED_TYPE_INPUT);
        
        driverTools.waitForElementToAppear(NUM_OF_BED_TRIGGER_INPUT);
        driverTools.click(NUM_OF_BED_TRIGGER_INPUT);
        driverTools.waitForElementToAppear(ONE_NUM_OF_BED_INPUT);
        driverTools.click(ONE_NUM_OF_BED_INPUT);
    }
    
    
    public void fillRoomPhotosForm(){
        driverTools.waitForElementToAppear(ADD_PHOTO_BUTTON);
        driverTools.click(ADD_PHOTO_BUTTON);
        
        driverTools.waitForElementToAppear(ROOM_IMAGE);
        clickPhotoCheckbox(1);
        
        driverTools.waitForElementToAppear(ADD_PHOTO_MODAL_BUTTON);
        driverTools.click(ADD_PHOTO_MODAL_BUTTON);
        
        driverTools.waitForElementToAppear(ROOM_IMAGE);
        clickPhotoCheckbox(1);
        
        driverTools.waitForElementToAppear(SET_AS_MAIN_PHOTO_BUTTON);
        driverTools.click(SET_AS_MAIN_PHOTO_BUTTON);
    }
    
    public void clickAddRoom(){
        driverTools.waitForElementToAppear(ADD_ROOM_BUTTON);
        driverTools.click(ADD_ROOM_BUTTON);
    }
    
    public boolean isRoomAppeared(String roomName){
        return driverTools.waitForElementToAppear(getRoomNameLabel(roomName)).isPresent();
    }
    
    public boolean isSuccessToastAppeared(){
        driverTools.waitForElementToAppear(SUCCESS_TOAST);
        return driverTools.isElementDisplayed(SUCCESS_TOAST);
    }

    public void clickEditButtonByRoomName(String roomName){
        Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath(String.format("//td[contains(text(),'%s')]/parent::tr//div[contains(@class, 'action-button-edit')]", roomName)));
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        driverTools.click(EDIT_BUTTON);
    }

    public WebElement getToggleButtonInputByRoomName(String roomName){
        Locator TOGGLE_BUTTON_INPUT = new Locator("TOGGLE_BUTTON_INPUT", By.xpath(String.format("//td[contains(text(),'%s')]/parent::tr//input[contains(@class, 'the-toggle')]", roomName)));
        driverTools.waitForElementToAppear(TOGGLE_BUTTON_INPUT);
        return driver.findElement(By.xpath(String.format("//td[contains(text(),'%s')]/parent::tr//input[contains(@class, 'the-toggle')]", roomName)));
    }

    public boolean clickAndVerifyToggleButtonByRoomName(String roomName, boolean isToggleOn)   {
        WebElement TOGGLE_BUTTON = driver.findElement(By.xpath(String.format("//td[contains(text(),'%s')]/parent::tr//div[contains(@class, 'the-toggle')]", roomName)));
        boolean initialState  = getToggleButtonInputByRoomName(roomName).isSelected();
        
        if ((initialState && isToggleOn) || (!initialState && !isToggleOn)) {
            return true;
        }
        
        driverTools.waitForElementToAppear(TOGGLE_BUTTON);
        TOGGLE_BUTTON.click();
        
        boolean finalState  = getToggleButtonInputByRoomName(roomName).isSelected();
        return isToggleOn ? finalState : !finalState;
    }

    public boolean isRoomActive(String name) {
        return driverTools.waitForElementToAppear(getRoomStatusToggleOn(name)).isPresent();
    }

    public void switchRoomStatusToggle(String name, boolean expectedStatus) {
        boolean initialState = isRoomActive(name);
        if ((!initialState) && expectedStatus) {
            WebElement toggle = driverTools.waitForElementToBeClickable(getRoomStatusToggleOff(name)).orElseThrow();
            driverTools.click(toggle);
        } else if (initialState && (!expectedStatus)) {
            WebElement toggle = driverTools.waitForElementToBeClickable(getRoomStatusToggleOn(name)).orElseThrow();
            driverTools.click(toggle);
        }
    }

    public void deleteRoom(String text) {
        WebElement button = driverTools.waitForElementToBeClickable(getDeleteRoomButtonByRoomName(text)).orElseThrow();
        driverTools.click(button);
        WebElement confirm = driverTools.waitForElementToBeClickable(DELETE_CONFIRM_BUTTON).orElseThrow();
        driverTools.click(confirm);
    }

    public void refreshPage() {
        driverTools.refreshWebpage();
    }

    public void confirmActivateRoom() {
        try {
            WebElement button = driverTools.waitForElementToBeClickable(ACTIVATE_ROOM_CONFIRM_BUTTON).orElseThrow();
            driverTools.click(button);
        } catch (Exception e) {
            logger.log("Button is not appeared");
        }
    }
}