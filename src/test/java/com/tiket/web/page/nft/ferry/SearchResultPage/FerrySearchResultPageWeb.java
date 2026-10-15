package com.tiket.web.page.nft.ferry.SearchResultPage;

import com.microsoft.playwright.ab;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public abstract class FerrySearchResultPageWeb<T extends WebDriver> extends WebPage<T> {

    public static <T extends WebDriver> FerrySearchResultPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new FerrySearchResultPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new FerrySearchResultPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator CARD_PRICE = new Locator("CARD_PRICE", new By.ByXPath("//div[contains(@class, 'ScheduleCard_price_label_container')]//span[contains(@class, 'Text_variant_alert')]"));
    private static final Locator PRICE_DETAILS = new Locator("PRICE_DETAILS", new By.ByXPath("//*[contains(@class, 'PriceDetail_price__') and contains(text(), 'IDR')]"));
    private static final Locator CLOSE_PRICE_POP_UP = new Locator("CLOSE_PRICE_POP_UP", new By.ByXPath("//*[contains(@class, 'BottomSheet_close_button')]"));
    private static final Locator SELECT_FIRST_CARD = new Locator("SELECT_FIRST_CARD", new By.ByXPath("(//*[contains(@class, 'ScheduleCard_select_button')])[1]"));
    private static final Locator CLASS_FIRST_CARD = new Locator("CLASS_FIRST_CARD", new By.ByXPath("(//*[contains(@class, 'ScheduleCard_ship_class')])[1]"));
    public static final Locator SEARCH_RESULT = new Locator("SEARCH_RESULT", new By.ByXPath("//*[contains(@class, 'search_schedules')]"));

    public FerrySearchResultPageWeb(Session<T> session) { super(session); }

    public abstract void verifyOpenImage();

    public abstract Locator getSRPCardImageLocator(int index);

    public abstract Locator getPopUpCardImageLocator(int index);
    
    public abstract void clickEditSearchButton();

    public void verifyPriceDetail() {
        List<WebElement> cardPriceList = driverTools.elements(CARD_PRICE).orElseThrow();

        for (int i = 1; i <= cardPriceList.size(); i++) {
            Locator CARD_PRICE_TEXT = getCardPriceLocator(i);
            String cardPriceText = driverTools.waitForElementToAppear(CARD_PRICE_TEXT).orElseThrow().getText();
            double cardPrice = CoreFunctions.convertPriceSafely(cardPriceText);

            driverTools.click(CARD_PRICE_TEXT);
            driverTools.waitForElementToAppear(PRICE_DETAILS);

            List<WebElement> priceElements = driverTools.elements(PRICE_DETAILS).orElseThrow();
            List<Double> priceList = new ArrayList<>();
            for (WebElement priceElement: priceElements) {
                String priceText = driverTools.getText(priceElement);
                double price = CoreFunctions.convertPriceSafely(priceText);
                priceList.add(price);
            }

            double totalPrice = priceList.subList(0, priceList.size() - 1).stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();

            Assertion.assertThat("Verify price details " + i + " are displayed correctly", totalPrice, is(cardPrice));
            driverTools.click(CLOSE_PRICE_POP_UP);
        }
    }

    private Locator getCardPriceLocator(int index) {
        return new Locator("CARD_PRICE_" + index, new By.ByXPath("(//div[contains(@class, 'ScheduleCard_price_label_container')]//span[contains(@class, 'Text_variant_alert')])[" + index + "]"));
    }

    public abstract void changeDeparturePort();

    public abstract void changeDestinationPort();

    public abstract void changeCheckInTime();

    public abstract void changePassenger();

    public abstract void changeDateTime();

    public abstract void changeTicketType();

    public abstract void clickSaveSearchEditButton();

    public abstract void changeSearchFerry();

    public abstract void verifyChangeSearch(String departurePort, String destinationPort, String checkInTime, int totalPassenger, String departureDate, String ticketType);

    public void selectFirstSrpCard() {
        driverTools.waitForElementToAppear(SELECT_FIRST_CARD);
        driverTools.clickWithJavascriptExecutor(SELECT_FIRST_CARD);
    }

    public boolean verifySrpPriceInIDR() {
        List<WebElement> priceList = driverTools.elements(CARD_PRICE).orElseThrow();
        for (WebElement priceElement : priceList) {
            String priceText = driverTools.getText(priceElement);
            if (!priceText.contains("IDR")) {
                return false;
            }
        }
        return true;
    }

    public String getFerryClassFirstCard() {
        return driverTools.waitForElementToAppear(CLASS_FIRST_CARD).orElseThrow().getText();
    }

    protected static Map<String, String> ferryTicketTypeIdToEn = Map.of(
            "Mobil Penumpang", "Passenger Car",
            "Pejalan Kaki", "Pedestrian",
            "Sepeda", "Bicycle",
            "Truk Pickup", "Pickup Truck"
    );

    protected static Map<String, String> ferryTicketTypeEnToId = Map.of(
            "Passenger Car", "Mobil Penumpang",
            "Pedestrian", "Pejalan Kaki",
            "Bicycle", "Sepeda",
            "Pickup Truck", "Truk Pickup"
    );
}