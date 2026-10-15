package com.tiket.web.page.flight.rescheduleReview;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Optional;

public class FlightRescheduleReviewPageWeb <T extends WebDriver> extends WebPage<T> {
    public FlightRescheduleReviewPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightRescheduleReviewPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new FlightRescheduleReviewPageDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new FlightRescheduleReviewPageMWeb<>(session);
            }
            default -> throw new UnsupportedOperationException("Platform not supported");
        }
    }

    private final static Locator FIRST_FARE = new Locator("FIRST_FARE", By.xpath("(//button[contains(@class, 'FareCard_select_btn')])[1]"));
    private final static Locator FARE_PRICES = new Locator("FARE_PRICES", By.xpath("//div[contains(@class,'FareCard_price_wrap')]//p[contains(@class,'FareCardPricing_text_label') and contains(@class,'Text_variant_price')]"));

    public void clickFirstFareOptions() {
        driverTools.waitForElementToPresence(FIRST_FARE);
        driverTools.clickWithJavascriptExecutor(FIRST_FARE);
    }

    /** Cheapest fare offered for the flights picked on the reschedule search result page. */
    public long getCheapestFarePrice() {
        driverTools.waitForElementToAppear(FARE_PRICES).orElseThrow();
        List<String> prices = driverTools.getTexts(FARE_PRICES);
        logger.log("Fares offered on the reschedule review page: " + prices);
        return prices.stream()
                .map(CoreFunctions::getLongFromString)
                .flatMap(Optional::stream)
                .min(Long::compareTo)
                .orElseThrow(() -> new IllegalStateException("No fare price on the reschedule review page"));
    }
}
