package com.tiket.web.page.accommodation.hotels.hotelRoomListPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HotelRoomListPageDWeb<T extends WebDriver> extends HotelRoomListPageWeb<T> {

    public HotelRoomListPageDWeb(Session<T> session) {
        super(session);
    }


    private static final Locator GUEST_DONE = new Locator("GUEST_DONE", new By.ByXPath("//footer[contains(@class, 'Modal_modal_footer')]//button"));
    private static final Locator CHANGE_BUTTON = new Locator("CHANGE_BUTTON", new By.ByXPath("//button[contains(@data-testid, 'room-list-search-button-submit')]"));
    private static final Locator ROOM_N_GUEST = new Locator("ROOM_N_GUEST", new By.ByXPath("//div[contains(@data-testid, 'room-list-search-button-open-room-guest-popup')]"));
    private static final Locator CURRENT_ADULT_AMOUNT = new Locator("CURRENT_ADULT_AMOUNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[2]"));
    private static final Locator ADULT_PLUS = new Locator("ADULT_PLUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[4]"));
    private static final Locator ADULT_MINUS = new Locator("ADULT_MINUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[3]"));
    private static final Locator BOOK_ROOM = new Locator("BOOK_ROOM", new By.ByXPath("//div[@data-testid='rate-plan-card-container']//button[starts-with(@data-testid,'book-button')]"));
    private static final Locator SELECT_CHEVRON_BUTTON = new Locator("SELECT_CHEVRON_BUTTON", new By.ByXPath("//span[@data-testid='total-price-text' and contains(@class, 'Text_text')]"));
    private static final Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", new By.ByXPath("//div[contains(@data-testid, 'image-gallery-desktop-navigation-button-next')]"));
    private static final Locator MULTI_BOOK_BUTTON = new Locator("MULTI_BOOK_BUTTON", new By.ByXPath("//button[@data-testid='multi-order-price-footer-button-book']"));

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
  
    public void clickNext(int count){
        for(int i = 0; i < count - 1;i++){
            driverTools.waitForElementToAppear(NEXT_BUTTON, 5);
            driverTools.clickWithJavascriptExecutor(NEXT_BUTTON);
        }
    }

    public String getBookingButtonText() {
        driverTools.waitForElementToAppear(BOOK_ROOM, 5).orElseThrow();
        return driverTools.getText(BOOK_ROOM).orElseThrow();
    }


    @Override
    public void updateRoomAndGuest(int guests, int rooms) {
        clickRoomNGuestButton();
        setTotalGuest(guests);
        setTotalRoom(rooms);
        clickGuestDoneButton();
        clickRoomListChangeButton();
    }

    @Override
    public void clickMultiBook() {
        driverTools.waitForElementToAppear(MULTI_BOOK_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(MULTI_BOOK_BUTTON);
    }
}
