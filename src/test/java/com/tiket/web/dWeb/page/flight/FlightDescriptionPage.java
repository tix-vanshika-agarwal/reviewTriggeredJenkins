package com.tiket.web.dWeb.page.flight;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FlightDescriptionPage <T extends WebDriver> extends BasePage<T> {
    public FlightDescriptionPage(Session<T> session) {
        super(session);
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
