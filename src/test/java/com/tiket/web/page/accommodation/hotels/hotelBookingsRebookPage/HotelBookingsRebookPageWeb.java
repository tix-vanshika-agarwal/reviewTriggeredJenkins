package com.tiket.web.page.accommodation.hotels.hotelBookingsRebookPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HotelBookingsRebookPageWeb <T extends WebDriver> extends WebPage<T> {
    public HotelBookingsRebookPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HotelBookingsRebookPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HotelBookingsRebookPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new HotelBookingsRebookPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator REFUND_AND_REBOOK_BUTTON = new Locator("REFUND_AND_REBOOK_BUTTON", By.xpath("//button[@data-testid='refund-option-button']"));

    public void clickRefundButton() {
        driverTools.waitForElementToPresence(REFUND_AND_REBOOK_BUTTON);
        driverTools.clickWithJavascriptExecutor(REFUND_AND_REBOOK_BUTTON);
    }
}
