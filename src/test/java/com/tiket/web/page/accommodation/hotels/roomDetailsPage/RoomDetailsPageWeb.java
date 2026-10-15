package com.tiket.web.page.accommodation.hotels.roomDetailsPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class RoomDetailsPageWeb<T extends WebDriver> extends WebPage<T> {

    public RoomDetailsPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> RoomDetailsPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case AMWEB, IMWEB, ANDROID, IOS -> { return new RoomDetailsPageMweb<>(session); }
            case DWEB -> { return new RoomDetailsPageDweb<>(session); }
            default -> throw new IllegalStateException("Unexpected value: " + Configs.PLATFORM);
        }
    }

    private static final Locator CHEVRON_BUTTON = new Locator("CHEVRON_BUTTON", new By.ByXPath("//*[contains(@data-testid,'footer-button-view-price-detail')]"));
    private static final Locator CURRENT_BALANCE_PRICE_DETAIL = new Locator("CURRENT_BALANCE_PRICE_DETAIL", new By.ByXPath("//*[@data-testid='current-balance-container']//*[contains(@class,'Text_weight_bold')]"));
    private static final Locator ROOM_NAME = new Locator("ROOM_NAME", new By.ByXPath("//*[contains(@class,'room_overview_container')]/div[1]/*[contains(@class,'Text')]"));
    private static final Locator BOOK_BUTTON = new Locator("BOOK_BUTTON", new By.ByXPath("//*[@data-testid='footer-button-book']//*[contains(@class,'Button')]"));
    private static final Locator ROOM_PRICE_BESIDE_ROOM_NAME = new Locator("ROOM_PRICE_BESIDE_ROOM_NAME", new By.ByXPath("//span[  contains(@class,'Text_weight_bold') and  contains(@class,'Text_variant_highEmphasis') and  contains(@class,'Text_size_h2')]"));
    private static final Locator TOTAL_RECOMMENDED_ROOMS = new Locator("TOTAL_RECOMMENDED_ROOMS", new By.ByXPath("//*[@data-testid='footer-button-book']//span"));
    private static final Locator PAYING_AMOUNT_TEXT_GREEN = new Locator("PAYING_AMOUNT_TEXT_GREEN", new By.ByXPath("//div[contains(@class,'TixPointCashbackInfoBanner_price_detail_cashback_info_banner')]"));

    private Locator initialTotalPrice(String currency) {
        return new Locator("INITIAL_TOTAL_PRICE", new By.ByXPath("//*[@data-testid='total-price-container']//*[contains(@class,'Text_weight_bold') and contains(text(),'" + currency + "')]"));
    }

    private Locator additionalTotalPrice(String currency) {
        return new Locator("ADDITIONAL_TOTAL_PRICE", new By.ByXPath("//*[@data-testid='total-additional-payment-container']//*[contains(@class,'Text_weight_bold') and contains(text(),'" + currency + "')]"));
    }

    public void clickOnPriceDetails(){
        driverTools.waitForElementToAppear(CHEVRON_BUTTON);
        driverTools.click(CHEVRON_BUTTON);
    }

    public String getCurrentBalance(boolean isPositive) {
        WebElement currentBalance = driverTools.waitForElementToAppear(CURRENT_BALANCE_PRICE_DETAIL).orElseThrow();
        String price = driverTools.getText(currentBalance);
        if (isPositive) return price.replace("-", "").trim();
        return price;
    }

    public String getInitialTotalPrice(String currency) {
        return driverTools.getText(initialTotalPrice(currency)).orElseThrow();
    }

    public String getAdditionalTotalPrice(String currency) {
        return driverTools.getText(additionalTotalPrice(currency)).orElseThrow();
    }

    public abstract void closePriceDetails();

    public abstract boolean isImageDetailViewDisplayed();

    public abstract void clickImageDetailCloseButton();

    public abstract void clickRoomCard();

    public abstract void clickRoomImage();

    public abstract boolean isRoomDetailsDisplayed();


    public String getRoomNameDetails() {
        return driverTools.getText(ROOM_NAME).orElseThrow();
    }

    public void clickBookButton() {
        driverTools.waitForElementsToAppear(BOOK_BUTTON);
        driverTools.clickWithJavascriptExecutor(BOOK_BUTTON);
    }

    public String getRoomNumberBesideRoomName(){
        WebElement webElement=driverTools.waitForElementToAppear(ROOM_PRICE_BESIDE_ROOM_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getTotalRecommendedRooms(){
        WebElement webElement=driverTools.waitForElementToAppear(TOTAL_RECOMMENDED_ROOMS).orElseThrow();
        return driverTools.getText(webElement);
    }

    public boolean isPayingThisAmountTextInGreen() {
        return driverTools.waitForElementToPresence(PAYING_AMOUNT_TEXT_GREEN, 10).isPresent();
    }
}
