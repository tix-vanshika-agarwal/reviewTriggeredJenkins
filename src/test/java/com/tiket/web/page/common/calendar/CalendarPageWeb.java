package com.tiket.web.page.common.calendar;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class CalendarPageWeb<T extends WebDriver> extends WebPage<T> {
    public CalendarPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CalendarPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CalendarPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CalendarPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    //Selects departure date only (Single Trip).
    public void selectDate(String departureDate) {
        selectDate(departureDate, null);
    }

    public abstract void selectDate(String departureDate, String returnDate);

    public abstract void selectSingleDate(String date);

    public abstract void navigateToMonthYear(String targetMonthEng, String targetYear);

    public abstract boolean isMonthVisible(String monthEng, String year);

    public abstract int compareMonthYear(String targetMonthEng, String targetYear);

    public Locator getDateLocator(String day, String monthEng, String monthId, String year) {
        String xpath = "(//span[contains(@aria-label, '" + day + " " + monthEng + " " + year +
                "') or contains(@aria-label, '" + day + " " + monthId + " " + year + "')])[1]";
        logger.log("Generated XPath: " + xpath);
        return new Locator("DATE_LOCATOR", new By.ByXPath(xpath));
    }
}