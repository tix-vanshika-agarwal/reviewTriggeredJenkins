package com.tiket.web.page.accommodation.homes.homesRoomListPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomesRoomListPageDWeb<T extends WebDriver> extends HomesRoomListPageWeb<T> {

    public HomesRoomListPageDWeb(Session<T> session) {
        super(session);
    }


    private static final Locator GUEST_DONE = new Locator("GUEST_DONE", new By.ByXPath("//footer[contains(@class, 'Modal_modal_footer')]//button"));
    private static final Locator CHANGE_BUTTON = new Locator("CHANGE_BUTTON", new By.ByXPath("//button[contains(@data-testid, 'room-list-search-button-submit')]"));
    private static final Locator ROOM_N_GUEST = new Locator("ROOM_N_GUEST", new By.ByXPath("//div[contains(@data-testid, 'room-list-search-button-open-room-guest-popup')]"));
    private static final Locator CURRENT_ADULT_AMOUNT = new Locator("CURRENT_ADULT_AMOUNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[2]"));
    private static final Locator ADULT_PLUS = new Locator("ADULT_PLUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[4]"));
    private static final Locator ADULT_MINUS = new Locator("ADULT_MINUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[3]"));
    private static final Locator CURRENT_ROOM_AMOUNT = new Locator("CURRENT_ROOM_AMOUNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[1]"));
    private static final Locator ROOM_PLUS = new Locator("ROOM_PLUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[2]"));
    private static final Locator ROOM_MINUS = new Locator("ROOM_MINUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[1]"));
    private static final Locator SELECT_CHEVRON_BUTTON = new Locator("SELECT_CHEVRON_BUTTON", new By.ByXPath("//span[@data-testid='total-price-text' and contains(@class, 'Text_text')]"));
    private static final Locator SEE_ROOMS_BUTTON = new Locator("SEE_ROOMS_BUTTON", new By.ByXPath("//button[@data-testid='button-see-room']"));
    public static final Locator SELECT_FILTER_ROOM_LIST = new Locator("SELECT_FILTER_ROOM_LIST", new By.ByXPath("//button[.//span[text()='All inclusive meals']]"));

    public void clickSeeRooms(){
        driverTools.scrollElementToCenter(SEE_ROOMS_BUTTON);
        driverTools.click(SEE_ROOMS_BUTTON);
    }

    public void  clickFilter() {
        driverTools.scrollElementToCenter(SELECT_FILTER_ROOM_LIST);
        driverTools.waitForElementToAppear(SELECT_FILTER_ROOM_LIST,4);
        driverTools.clickWithJavascriptExecutor(SELECT_FILTER_ROOM_LIST);
    }

    @Override
    public void clickRoomNGuestButton() {
        driverTools.waitForElementToAppear(ROOM_N_GUEST).orElseThrow();
        driverTools.clickWithJavascriptExecutor(ROOM_N_GUEST);
    }

    public void clickGuestDoneButton() {
        driverTools.waitForElementToAppear(GUEST_DONE).orElseThrow();
        driverTools.clickWithJavascriptExecutor(GUEST_DONE);
    }

    public void clickRoomListChangeButton() {
        driverTools.waitForElementToAppear(CHANGE_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CHANGE_BUTTON);
    }

    public void setTotalGuest(int adult) {
        int currentAdult = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ADULT_AMOUNT, "value"));
        setTotal(currentAdult, adult, ADULT_PLUS, ADULT_MINUS, "Adult");
    }
    public void clickGuestDoneAndSubmitButton() {
        driverTools.waitForElementToAppear(GUEST_DONE).orElseThrow();
        driverTools.clickWithJavascriptExecutor(GUEST_DONE);
        driverTools.waitForElementToAppear(CHANGE_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CHANGE_BUTTON);
    }

    public void clickChevronButton() {
        driverTools.waitForElementsToAppear(SELECT_CHEVRON_BUTTON);
        driverTools.click(SELECT_CHEVRON_BUTTON);
    }
}