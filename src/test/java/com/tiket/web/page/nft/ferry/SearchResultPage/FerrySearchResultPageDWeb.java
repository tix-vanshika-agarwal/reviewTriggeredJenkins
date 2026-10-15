package com.tiket.web.page.nft.ferry.SearchResultPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FerrySearchResultPageDWeb <T extends WebDriver> extends FerrySearchResultPageWeb<T> {

    private static final Locator CARD_IMAGE = new Locator("CARD_IMAGE", new By.ByXPath("//*[contains(@class, 'ScheduleCard_images')]"));
    private static final Locator IMAGE_POP_UP = new Locator("IMAGE_POP_UP", new By.ByXPath("//*[contains(@class, 'ImagePreview_image_preview_desktop_popup')]"));
    private static final Locator IMAGE_NUMBER_DIVIDER = new Locator("IMAGE_NUMBER_DIVIDER", new By.ByXPath("//*[contains(@class, 'ImageDescription_counter')]"));
    private static final Locator NEXT_IMAGE_BUTTON = new Locator("NEXT_IMAGE_BUTTON", new By.ByXPath("//*[contains(@class, 'ImageNavigation_right')]"));
    private static final Locator PREVIOUS_IMAGE_BUTTON = new Locator("PREVIOUS_IMAGE_BUTTON", new By.ByXPath("//*[contains(@class, 'ImageNavigation_left')]"));
    private static final Locator CLOSE_IMAGE_POP_UP = new Locator("CLOSE_IMAGE_POP_UP", new By.ByXPath("//*[contains(@class, 'ImagePreview_floating_close')]"));
    private static final Locator CHANGE_DEPARTURE_PORT = new Locator("CHANGE_DEPARTURE_PORT", new By.ByXPath("(//*[contains(@class, 'Text_variant_highEmphasis')])[1]"));
    private static final Locator CHANGE_DESTINATION_PORT = new Locator("CHANGE_DESTINATION_PORT", new By.ByXPath("//*[contains(@class, 'ChangeSearch_destination')]"));
    private static final Locator CHANGE_CHECK_IN_TIME = new Locator("CHANGE_CHECK_IN_TIME", new By.ByXPath("//*[contains(@class, 'ChangeSearch_time')]"));
    private static final Locator CHANGE_PASSENGER = new Locator("CHANGE_PASSENGER", new By.ByXPath("//*[contains(@class, 'ChangeSearch_passenger')]"));
    private static final Locator CHANGE_DATE_TIME = new Locator("CHANGE_DATE_TIME", new By.ByXPath("//*[contains(@class, 'ChangeSearch_date')]"));
    private static final Locator CHANGE_TICKET_TYPE = new Locator("CHANGE_TICKET_TYPE", new By.ByXPath("//*[contains(@class, 'ChangeSearch_vehicle_type')]"));
    private static final Locator SEARCH_FERRY = new Locator("SEARCH_FERRY", new By.ByXPath("//*[contains(@class, 'ChangeSearch_search_button')]"));

    public FerrySearchResultPageDWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void verifyOpenImage() {
        List<WebElement> cardImageList = driverTools.elements(CARD_IMAGE).orElseThrow();

        for (int i = 1; i <= cardImageList.size(); i++) {
            driverTools.click(getSRPCardImageLocator(i));

            driverTools.waitForElementToAppear(IMAGE_POP_UP);
            Assertion.assertThat("Image pop up " + i + " is displayed", driverTools.isElementDisplayed(IMAGE_POP_UP), is(true));

            String imageNumberDivider = driverTools.waitForElementToAppear(IMAGE_NUMBER_DIVIDER).orElseThrow().getText();
            String imageNumberString = imageNumberDivider.substring(imageNumberDivider.length() - 1);
            int imageNumber = Integer.parseInt(imageNumberString);

            Locator FIRST_IMAGE = getPopUpCardImageLocator(1);
            driverTools.waitForElementToAppear(FIRST_IMAGE);
            Assertion.assertThat("Image 1 is displayed", driverTools.isElementDisplayed(FIRST_IMAGE), is(true));

            for (int j = 2; j <= imageNumber; j++) {
                driverTools.click(NEXT_IMAGE_BUTTON);
                Locator NEXT_IMAGE = getPopUpCardImageLocator(j);
                driverTools.waitForElementToAppear(NEXT_IMAGE);
                Assertion.assertThat("Next image " + j + " is displayed", driverTools.isElementDisplayed(NEXT_IMAGE), is(true));
            }

            for (int k = imageNumber - 1; k >= 1; k--) {
                driverTools.click(PREVIOUS_IMAGE_BUTTON);
                Locator PREVIOUS_IMAGE = getPopUpCardImageLocator(k);
                driverTools.waitForElementToAppear(PREVIOUS_IMAGE);
                Assertion.assertThat("Previous image " + k + " is displayed", driverTools.isElementDisplayed(PREVIOUS_IMAGE), is(true));
            }

            driverTools.click(CLOSE_IMAGE_POP_UP);
        }
    }

    @Override
    public Locator getSRPCardImageLocator(int index) {
        return new Locator("SRP_CARD_IMAGE_" + index, new By.ByXPath("(//*[contains(@class, 'ScheduleCard_images')])[" + index + "]"));
    }

    @Override
    public Locator getPopUpCardImageLocator(int index) {
        return new Locator("POP_UP_CARD_IMAGE_" + index,
                new By.ByXPath("//*[starts-with(normalize-space(text()), '" + index + "/')]"));
    }

    @Override
    public void clickEditSearchButton() {
        // No edit search button on dweb
    }

    @Override
    public void changeDeparturePort() {
        WebElement departurePort = driverTools.waitForElementToAppear(CHANGE_DEPARTURE_PORT).orElseThrow();
        driverTools.scrollIntoWebView(departurePort);
        driverTools.clickWithJavascriptExecutor(departurePort);
    }

    @Override
    public void changeDestinationPort() {
        driverTools.clickWithJavascriptExecutor(CHANGE_DESTINATION_PORT);
    }

    @Override
    public void changeCheckInTime() {
        driverTools.clickWithJavascriptExecutor(CHANGE_CHECK_IN_TIME);
    }

    @Override
    public void changePassenger() {
        driverTools.clickWithJavascriptExecutor(CHANGE_PASSENGER);
    }

    @Override
    public void changeDateTime(){
        driverTools.clickWithJavascriptExecutor(CHANGE_DATE_TIME);
    }

    @Override
    public void changeTicketType(){
        driverTools.clickWithJavascriptExecutor(CHANGE_TICKET_TYPE);
    }

    @Override
    public void clickSaveSearchEditButton() {
    }

    @Override
    public void verifyChangeSearch(String departurePort, String destinationPort, String checkInTime, int totalPassenger, String departureDate, String ticketType) {
        driverTools.waitForElementToAppear(SEARCH_RESULT);
        Assertion.assertThat("Search result is displayed", driverTools.isElementDisplayed(SEARCH_RESULT), is(true));

        String srpDeparturePort = driverTools.waitForElementToAppear(CHANGE_DEPARTURE_PORT).orElseThrow().getText();
        Assertion.assertThat("Departure port is updated in SRP", srpDeparturePort, is(departurePort));

        String srpDestinationPort = driverTools.waitForElementToAppear(CHANGE_DESTINATION_PORT).orElseThrow().getText();
        Assertion.assertThat("Destination port is updated in SRP", srpDestinationPort, is(destinationPort));

        String srpCheckInTime = driverTools.waitForElementToAppear(CHANGE_CHECK_IN_TIME).orElseThrow().getText();
        Assertion.assertThat("Check-in time is updated in SRP", srpCheckInTime, is(checkInTime.replace(" ", ":")));

        String srpTotalPassenger = driverTools.waitForElementToAppear(CHANGE_PASSENGER).orElseThrow().getText();
        Assertion.assertThat("Total passenger is updated in SRP", srpTotalPassenger, containsString(Integer.toString(totalPassenger)));

        String srpDepartureDate = driverTools.waitForElementToAppear(CHANGE_DATE_TIME).orElseThrow().getText();
        Assertion.assertThat("Departure date is updated in SRP", srpDepartureDate, containsString(departureDate));

        String srpTicketType = driverTools.waitForElementToAppear(CHANGE_TICKET_TYPE).orElseThrow().getText();
        String expectedTicketType = ticketType;
        if (ferryTicketTypeIdToEn.containsKey(ticketType)) {
            expectedTicketType = ferryTicketTypeIdToEn.get(ticketType);
        } else if (ferryTicketTypeEnToId.containsKey(ticketType)) {
            expectedTicketType = ferryTicketTypeEnToId.get(ticketType);
        }
        Assertion.assertThat("Ticket type is updated in SRP", srpTicketType, containsString(expectedTicketType));
    }

    @Override
    public void changeSearchFerry() {
        driverTools.clickWithJavascriptExecutor(SEARCH_FERRY);
    }
}