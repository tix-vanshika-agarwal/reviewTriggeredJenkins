package com.tiket.web.page.flight.flightStatusPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class FlightStatusPageWeb<T extends WebDriver> extends WebPage<T> {
    public FlightStatusPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightStatusPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new FlightStatusPageDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new FlightStatusPageMWeb<>(session);
            }
            default -> throw new UnsupportedOperationException("Platform not supported");
        }
    }

    private static final Locator FLIGHT_STATUS_ON_SCHEDULE = new Locator("FLIGHT_STATUS_ON_SCHEDULE", By.xpath("//span[text()='On schedule' or text()='Sesuai jadwal']"));

    public boolean getFlightStatusOnSchedule() {
        return driverTools.waitForElementsToAppear(FLIGHT_STATUS_ON_SCHEDULE).isPresent();
    }
}
