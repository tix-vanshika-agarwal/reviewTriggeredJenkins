package com.tiket.page.accommodation.homes.homessearchresultpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPageIOS;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPageIOS.LOYALTY_BADGE_HOTEL_CARD;
import static com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPageIOS.QUICK_FILTER_CONTAINER;
import static org.hamcrest.CoreMatchers.is;

public class HomesSearchResultPageIOS<T extends WebDriver> extends HomesSearchResultPage<T> {
    private static final Locator BEDROOM_INCREMENT_BUTTON = new Locator("BEDROOM_INCREMENT_BUTTON", AppiumBy.iOSClassChain("**/*[`name == 'TVCell<THMTitleStepperView>'`][1]/XCUIElementTypeButton"));
    private static final Locator BEDROOM_FILTER = new Locator("BEDROOM_FILTER", AppiumBy.xpath("//*[@name='bottomActionItem_room'] | //XCUIElementTypeButton[@name='tds ic bottom more']//ancestor::*[(@name='Jumlah Kamar' or @name='No. of Bedroom') and @visible='true']"));

    public HomesSearchResultPageIOS(Session<T> session) {
        super(session);
    }
    
    public boolean verifySrpElements() {
        return driverTools.waitForElementToPresence(BEDROOM_FILTER).isPresent();
    }

    @Override
    public void clickBedroomsFilterButton() {
        WebElement bedroomsFilterButton = driverTools.waitForElementToAppear(BEDROOM_FILTER, 10).orElseThrow();
        driverTools.click(bedroomsFilterButton);
    }

    public void addTotalBedroom(String totalBedroom) {
        WebElement addButton = driverTools.swipeToElement(BEDROOM_INCREMENT_BUTTON, Swipe.UP, 2).orElseThrow();
        logger.log("adding "+totalBedroom+" for number bedroom");
        for (int i=1;i<Integer.parseInt(totalBedroom);i++) {
            driverTools.click(addButton);
        }
    }

    private Locator getHomesNameElement(int index) {
        return new Locator("HOMES_NAME", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == 'srpCard.hotelName'`]["+index+"]"));
    }

    /**
     * @param homesCardName name of the product card
     * @param param expected number of bedrooms after bedrooms filter is applied
     */
    
    public void homesCardCheckTotalBedrooms(String homesCardName, String[] param) {
        // only 1st object on initial test data is need, 2nd object will be ignored
        int expectedNumberOfBedrooms = Integer.parseInt(param[0]);

        // get number of bedrooms from homes label
        List<String> labelsList = driverTools.getTexts(new SearchResultPageIOS<>(session).getHomesOtherBenefitsLabelElement(homesCardName));
        String labels = String.join(" ", labelsList);
        int actualNumberOfBedrooms = getNumberOfBedrooms(labels);

        logger.log("number of actual bed : " + actualNumberOfBedrooms);
        logger.log("number of expected bed : " + expectedNumberOfBedrooms);

        // check data
        Assertion.assertThat("Number of homes bedrooms of '" + homesCardName + "' is equal or more than expected ", actualNumberOfBedrooms >= expectedNumberOfBedrooms, is(true));
    }

    /**
     * homes method
     * @param labels string of benefit label from homes card
     * @return number of bedroom on homes card
     */

    private int getNumberOfBedrooms(String labels) {
        labels = labels.replace('\u00A0', ' ').replaceAll("\\s+", " "); // replace non-breaking space and multiple spaces with single space
        String[] splitLabels = labels.split(" ");
        logger.log("splitLabels: " + Arrays.toString(splitLabels));

        int index = findIndexContains(splitLabels, "Kamar");
        logger.log("Index of Kamar in labels: " + index);

        if (index == -1) index = findIndexContains(splitLabels, "Bedroom");
        logger.log("Index of Bedroom in labels: " + index);

        if (index < 0) {
            logger.warn("Cannot find a valid number before Kamar/Bedrooms in labels: " + labels);
            return 0;
        } else {
            logger.log("Kamar/Bedrooms is in \"" + index + "\" index");
            String bedrooms = splitLabels[index-1];
            logger.log("Number Of Bedrooms -> " + bedrooms);
            return Integer.parseInt(bedrooms);
        }
    }

    @Override
    public void selectAccomodationTypeFilter(String accomadationType) {
        WebElement accomodationOption = driverTools.swipeToElement(homesAccomodationTypeLocator(accomadationType),Swipe.UP,2).orElseThrow();
        driverTools.click(accomodationOption);
    }

    private Locator homesAccomodationTypeLocator(String type) {
        String accomadationTypeIN = splitLanguageData(type, "/")[0];
        String accomadationTypeENG = splitLanguageData(type, "/")[1];
        return new Locator("ACCOMODATION_TYPE", AppiumBy.xpath("//*[@name='TVCell<TACMFilterAccommodationTypeGroupView>+groupPropertyTypes']//*[@label='" + accomadationTypeIN + "' or @label='" + accomadationTypeENG + "']"));

    }
    
    @Override
    public  void setBedroomsFilterHomes(String totalBedRoom) {
        for (int i = 0; i < 5; i++) {
            if (driverTools.waitForElementToPresence(BEDROOM_FILTER).isEmpty()) driverTools.swipeByDistance(QUICK_FILTER_CONTAINER, Swipe.LEFT, 0.8);
            if (driverTools.waitForElementToPresence(BEDROOM_FILTER).isPresent()) break;
        }
        driverTools.click(BEDROOM_FILTER);
        for(int i = 1 ; i< Integer.parseInt(totalBedRoom); i++) {
            WebElement bedroomIncrementButton = driverTools.waitForElementToAppear(BEDROOM_INCREMENT_BUTTON).orElseThrow();
            driverTools.click(bedroomIncrementButton);
        }
    }

    @Override
    public void selectHomesWithLoyaltyBenefit() {
        WebElement loyaltyBadge = driverTools.swipeToElement(LOYALTY_BADGE_HOTEL_CARD, Swipe.UP, 5).orElseThrow();
        driverTools.click(loyaltyBadge);
    }

    @Override
    public boolean isFlexibleStayDatesSectionShown() {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }

    @Override
    public void swipeToFlexibleStayDates() {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }

    @Override
    public String getFirstFlexibleStayHotelName() {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }

    @Override
    public String getFirstFlexibleStayLocation() {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }

    @Override
    public void selectFirstFlexibleStayCard() {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }
}
