package com.tiket.web.page.accommodation.hotels.hotelRoomListPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HotelRoomListPageMWeb<T extends WebDriver> extends HotelRoomListPageWeb<T> {

    public HotelRoomListPageMWeb(Session<T> session) {
        super(session);
    }


    private static final Locator GUEST_DONE = new Locator("GUEST_DONE", new By.ByXPath("//button[contains(@class, 'Button_button')]//span[text()='Done' or text()='Selesai']"));
    private static final Locator CHANGE_BUTTON = new Locator("CHANGE_BUTTON", new By.ByXPath("//button[@data-testid='room-list-search-button-open-mobile-ui']"));
    private static final Locator CHECK_BUTTON = new Locator("CHECK_BUTTON", new By.ByXPath("//button[@data-testid='room-list-search-button-open-mobile-ui']"));
    private static final Locator ROOM_N_GUEST = new Locator("ROOM_N_GUEST", new By.ByXPath("//div[contains(@data-testid, 'room-list-search-button-open-room-guest-popup')]"));
    private static final Locator CURRENT_ADULT_AMOUNT = new Locator("CURRENT_ADULT_AMOUNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[2]"));
    private static final Locator ADULT_PLUS = new Locator("ADULT_PLUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')][2])[2]"));
    private static final Locator ADULT_MINUS = new Locator("ADULT_MINUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')][2])[1]"));
    private static final Locator ROOM_BOOKING_BUTTON = new Locator("ROOM_BOOKING_BUTTON", new By.ByXPath("(//button[@data-testid='room-card-button-book'])[1]"));
    private static final Locator SELECT_CHEVRON_BUTTON = new Locator("SELECT_CHEVRON_BUTTON", new By.ByXPath("//p[contains(@class, 'PriceFooter_final_price_text')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("(//button[@data-testid='room-list-search-button-submit'])"));
    private static final Locator MULTI_BOOK_BUTTON = new Locator("MULTI_BOOK_BUTTON", new By.ByXPath("//button[@data-testid='multi-order-price-footer-button-book']"));

    @Override
    public void clickRoomNGuestButton() {
        driverTools.waitForElementToAppear(CHECK_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CHECK_BUTTON);
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
        driverTools.waitForElementToAppear(SEARCH_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    @Override
    public void updateRoomAndGuest(int guests, int rooms) {
        driverTools.waitForElementToAppear(CHANGE_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CHANGE_BUTTON);
        driverTools.waitForElementToAppear(ROOM_N_GUEST).orElseThrow();
        driverTools.clickWithJavascriptExecutor(ROOM_N_GUEST);

        setTotalGuest(guests);
        setTotalRoom(rooms);

        driverTools.waitForElementToAppear(GUEST_DONE).orElseThrow();
        driverTools.clickWithJavascriptExecutor(GUEST_DONE);

        driverTools.waitForElementToAppear(SEARCH_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }
    public void clickChevronButton() {
        driverTools.waitForElementsToAppear(SELECT_CHEVRON_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_CHEVRON_BUTTON);
    }

    public void clickNext(int count){
        for (int i = 1; i <= count; i++) {
            String dynamicXPath = "(//div[contains(@class, 'Carousel_image_overlay')])[" + i + "]";
            Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", new By.ByXPath(dynamicXPath));
            driverTools.waitForElementToAppear(NEXT_BUTTON, 5);
            driverTools.clickWithJavascriptExecutor(NEXT_BUTTON);
        }
    }
    
    public String getBookingButtonText() {
        driverTools.waitForElementToAppear(ROOM_BOOKING_BUTTON, 5).orElseThrow();
        return driverTools.getText(ROOM_BOOKING_BUTTON).orElseThrow();
    }

    @Override
    public void clickMultiBook() {
        driverTools.waitForElementToAppear(MULTI_BOOK_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(MULTI_BOOK_BUTTON);
    }
}
