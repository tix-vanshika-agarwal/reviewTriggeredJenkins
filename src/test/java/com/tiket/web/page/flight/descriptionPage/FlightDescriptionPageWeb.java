package com.tiket.web.page.flight.descriptionPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.flight.descriptionPage.FlightDescriptionPageDWeb;
import com.tiket.web.page.flight.descriptionPage.FlightDescriptionPageMWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FlightDescriptionPageWeb <T extends WebDriver> extends WebPage<T> {
    public FlightDescriptionPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightDescriptionPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new FlightDescriptionPageDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new FlightDescriptionPageMWeb<>(session);
            }
            default -> throw new UnsupportedOperationException("Platform not supported");
        }
    }

    private static final Locator SELECT_TICKET = new Locator("SELECT_TICKET", new By.ByXPath("//*[contains(@class,'Button_variant_primary') and (contains(text(),'Select Ticket') or contains(text(),'Pilih Tiket') )]"));
    private static final Locator LOGIN_PROFILE_ICON = new Locator("LOGIN_PROFILE_ICON", new By.ByXPath("//div[contains(@class,'blink_icon')]"));

    public void selectTicket(){
        driverTools.waitForElementsToAppear(SELECT_TICKET);
        driverTools.clickWithJavascriptExecutor(SELECT_TICKET);
    }

    public void clickLoginProfileIcon() {
        driverTools.click(LOGIN_PROFILE_ICON) ;
    }

}
