package com.tiket.page.ttd.explorepage;

import com.tiket.model.Session;
import com.tiket.page.ttd.TTDPage;
import com.tiket.page.ttd.destinationpage.TTDDestinationPage;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public abstract class TTDExplorePage<T extends WebDriver> extends BasePage<T> {

    public TTDExplorePage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TTDExplorePage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new TTDExplorePageAndroid<>(session);}
            case IOS -> {return new TTDExplorePageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract boolean isExplorePageValid();
    public abstract String searchAndClickDestinationLabel(String keyword, String type);
    public abstract String selectRandomDestination();
    public abstract String pickAndClickDestinationCard();
    public abstract String pickAndClickDestinationLabel();
    public abstract String selectDestination(String keyword, String type);
    public abstract void inputSearchKeyword(String keyword);
    public abstract boolean isNearbyLocationSectionVisible();
    public abstract int getNearbyDestinationChips();
    public abstract List<String> getNearbyDestinationChipText();
    public abstract String clickNearbyDestinationChips(String index);
    public abstract String chooseLocationChips(int index);

    public Boolean verifyAllNearbyLocationChips(String language) {
        TTDPage<T> ttdPage = TTDPage.getInstance(session);
        TTDDestinationPage<T> ttdDestinationPage = TTDDestinationPage.getInstance(session);
        TTDSearchResultPage<T> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        int nearbyLocationChipsCount = getNearbyDestinationChips();
        for(int i = 0; i < nearbyLocationChipsCount; i++) {
            String clickedNearbyDestinationChipText = clickNearbyDestinationChips(""+i);

            Assertion.assertThat("Assert if Destination Name is valid", clickedNearbyDestinationChipText, is(containsString(ttdSearchResultPage.getLocation())));

            ttdSearchResultPage.clickBack();
        }
        return true;
    }

    public String chooseAreaLevel(String location, String type) {
        int totalChip = getNearbyDestinationChips();
        logger.log("Total Nearby Location Chips: " + totalChip);
        if (totalChip == 4) {
            logger.log("Choosing the first location chips");
            location = chooseLocationChips(1);
        } else {
            logger.log("Choosing location via search");
            inputSearchKeyword(location);
            TTDSearchResultPage<T> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
            location = ttdSearchResultPage.clickLocationSearchResult(location, type);
        }
        return location;
    }
}
