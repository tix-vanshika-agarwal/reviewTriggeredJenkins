package com.tiket.web.page.nft.airport_transfer.landing_page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class AirportTransferLandingPageDWeb<T extends WebDriver> extends AirportTransferLandingPageWeb<T> {

    public AirportTransferLandingPageDWeb(Session<T> session) {
        super(session);
    }

    public void selectDate(String departureDate) {
        try {
            driverTools.click(DATE_SECTION);
        }catch (Exception e){
            driverTools.clickWithJavascriptExecutor(DATE_SECTION);
        }

        String year = CoreFunctions.getSectionOfString(departureDate, " ", 2);
        String monthEng = CoreFunctions.getSectionOfString(departureDate, " ", 1);
        String monthId = CoreFunctions.monthEngToId(monthEng);
        String day = CoreFunctions.getSectionOfString(departureDate, " ", 0);
        Locator dateLocator = getDateLocator(day, monthEng, monthId, year);
        driverTools.click(dateLocator);
    }
}
