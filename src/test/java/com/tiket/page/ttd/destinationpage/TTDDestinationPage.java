package com.tiket.page.ttd.destinationpage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.NoSuchElementException;

public abstract class TTDDestinationPage <T extends WebDriver> extends BasePage<T> {

    public TTDDestinationPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TTDDestinationPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new TTDDestinationPageAndroid<>(session);}
            case IOS -> {return new TTDDestinationPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract boolean isDestinationPageValid(String keyword, String type);
    public abstract String getDestinationName();
    public abstract String getDestinationType(String type);
    public abstract void chooseCategory(String type, String param);
    public abstract boolean checkSection(String section);

    public String selectRandomSectionToCheck() {
        int randomInt = CoreFunctions.generateRandomInt(0,2);
        switch (randomInt) {
            case 0 : return "campaign";
            case 1 : return "all cta";
            case 2 : return "banner";
        }
        return "fail";
    }

    public String getDestinationNameOrSRPLocation() {
        String result = "";

        try {
            result += getDestinationName();
        } catch (NoSuchElementException e) {
            TTDSearchResultPage<T> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
            result += ttdSearchResultPage.getLocation();
        }

        return result;
    }

    public abstract void clickBack();
    public boolean verifyDisplayedCurrency(String currency) {
        TTDLandingPage<T> ttdLandingPage = TTDLandingPage.getInstance(session);
        return ttdLandingPage.verifyDisplayedCurrency(currency);
    }
}
