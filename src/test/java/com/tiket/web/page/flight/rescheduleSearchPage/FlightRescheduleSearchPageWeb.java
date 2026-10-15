package com.tiket.web.page.flight.rescheduleSearchPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FlightRescheduleSearchPageWeb <T extends WebDriver> extends WebPage<T> {
    public FlightRescheduleSearchPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightRescheduleSearchPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new FlightRescheduleSearchPageDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new FlightRescheduleSearchPageMWeb<>(session);
            }
            default -> throw new UnsupportedOperationException("Platform not supported");
        }
    }

    private static final Locator FIRST_FLIGHT_CARD = new Locator("FIRST_FLIGHT_CARD", By.xpath("(//div[@data-testid='srp-flight-card'])[1]"));
    private static final Locator FIRST_FLIGHT_CARD_WITHOUT_DEALS = new Locator("FIRST_FLIGHT_CARD_WITHOUT_DEALS", By.xpath("(//div[@data-testid='flight-section-return_flights_secondary']//div[@data-testid='srp-flight-card'])[1]"));
    private static final By CARD_PRICE = By.xpath(".//div[contains(@class,'FlightCard_price')]//span[contains(@class,'Text_variant_price')]");

    private Locator getFirstMoreExpensiveFlightCardAndSpecificAirlines(String airlines) {
        return new Locator("FLIGHT_CART_BODY", By.xpath("//div[contains(@class, 'FlightCard_card_body_wrapper') and .//p[contains(@class, 'Text_variant_alert') and not(text()[contains(., 'IDR 0')])] and .//span[contains(@class, 'AirlinesAndFacilities_airline_text') and contains(., '" + airlines + "')]]"));
    }

    // Note: If any airlines is fine, pass the airlines value as empty string ("")
    public void selectMoreExpensiveFlight(String airlines) {
        Locator flightCard = getFirstMoreExpensiveFlightCardAndSpecificAirlines(airlines);
        driverTools.scrollUntilElementFound(flightCard);
        driverTools.scrollElementToCenter(flightCard);
        driverTools.clickWithJavascriptExecutor(flightCard);
    }

    /** Takes the first flight on the list and returns the price shown on it. */
    public long selectFirstFlightAndGetPrice() {
        return selectAndGetPrice(FIRST_FLIGHT_CARD, "first flight");
    }

    /** Takes the first return that sits outside the Round-trip Deals pair. */
    public long selectFirstFlightWithoutDealsAndGetPrice() {
        return selectAndGetPrice(FIRST_FLIGHT_CARD_WITHOUT_DEALS, "first return outside Round-trip Deals");
    }

    /**
     * Reads the price off the very card that gets clicked, because the list re-sorts while the
     * search streams in and its rows are virtualised.
     */
    private long selectAndGetPrice(Locator cardLocator, String description) {
        driverTools.scrollUntilElementFound(cardLocator);
        WebElement card = driverTools.waitForElementToAppear(cardLocator).orElseThrow();
        driverTools.scrollElementToCenter(card);

        String price = driverTools.getText(card.findElement(CARD_PRICE));
        logger.log("Price on the " + description + ": " + price);
        driverTools.clickWithJavascriptExecutor(card);

        return CoreFunctions.getLongFromString(price)
                .orElseThrow(() -> new IllegalStateException("Cannot read a price out of " + price));
    }
}
