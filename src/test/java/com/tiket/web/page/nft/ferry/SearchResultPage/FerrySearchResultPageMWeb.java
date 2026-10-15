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

public class FerrySearchResultPageMWeb <T extends WebDriver> extends FerrySearchResultPageWeb<T> {

    private static final Locator CARD_IMAGE = new Locator("CARD_IMAGE", new By.ByXPath("//*[contains(@class, 'ScheduleCard_images')]"));
    private static final Locator IMAGE_POP_UP = new Locator("IMAGE_POP_UP", new By.ByXPath("//*[contains(@class, 'ImagePreview_image_preview_mobile_wrapper')]"));
    private static final Locator IMAGE_NUMBER_DIVIDER = new Locator("IMAGE_NUMBER_DIVIDER", new By.ByXPath("//*[contains(@class, 'ImageDescription_counter')]"));
    private static final Locator CLOSE_IMAGE_POP_UP = new Locator("CLOSE_IMAGE_POP_UP", new By.ByXPath("//*[contains(@class, 'ImagePreview_floating_close')]"));
    private static final Locator CHANGE_DEPARTURE_PORT = new Locator("CHANGE_DEPARTURE_PORT", new By.ByXPath("(//div[contains(@class, 'PortSegment_container')]/div[contains(@class, 'PortSegment_input_text')])[1]"));
    private static final Locator CHANGE_DESTINATION_PORT = new Locator("CHANGE_DESTINATION_PORT", new By.ByXPath("(//div[contains(@class, 'PortSegment_container')]/div[contains(@class, 'PortSegment_input_text')])[2]"));
    private static final Locator CHANGE_CHECK_IN_TIME = new Locator("CHANGE_CHECK_IN_TIME", new By.ByXPath("//div[contains(@class, 'DateTimeInput_container_time') and @data-testid='sf-time-input']"));
    private static final Locator CHANGE_PASSENGER = new Locator("CHANGE_PASSENGER", new By.ByXPath("//div[@id='coachmark-landing-step4' and contains(@class, 'PassengerInput_container')]"));
    private static final Locator CHANGE_DATE_TIME = new Locator("CHANGE_DATE_TIME", new By.ByXPath("(//div[contains(@class, 'DateTimeInput_container_date')])[2]"));
    private static final Locator CHANGE_TICKET_TYPE = new Locator("CHANGE_TICKET_TYPE", new By.ByXPath("//div[@id='coachmark-landing-step3' and contains(@class, 'VehicleInput_container')]"));
    private static final Locator EDIT_SEARCH_BUTTON = new Locator("EDIT_SEARCH_BUTTON", new By.ByXPath("//button[contains(@class, 'Button_button') and (contains(text(), 'Change') or contains(text(), 'Ubah'))]"));
    private static final Locator SAVE_SEARCH_EDIT_BUTTON = new Locator("SAVE_SEARCH_EDIT_LOCATOR", new By.ByXPath("//button[contains(@class, 'Button_button') and (contains(text(), 'Save') or contains(text(), 'Simpan'))]"));
    private static final Locator SEARCH_FERRY = new Locator("SEARCH_FERRY", new By.ByXPath("//button[contains(@class, 'SearchForm_submit_button') and (contains(text(), 'Cari ferry') or contains(text(), 'Search ferry'))]"));

    public FerrySearchResultPageMWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void verifyOpenImage() {
        Locator cardImageLocator = getSRPCardImageLocator(1);
        driverTools.waitForElementToAppear(cardImageLocator).ifPresent(driverTools::clickWithJavascriptExecutor);

        driverTools.waitForElementToAppear(IMAGE_POP_UP);
        Assertion.assertThat("Image pop up " + 1 + " is displayed", driverTools.isElementDisplayed(IMAGE_POP_UP), is(true));

        String imageNumberDivider = driverTools.waitForElementToAppear(IMAGE_NUMBER_DIVIDER).orElseThrow().getText();
        String imageNumberString = imageNumberDivider.substring(imageNumberDivider.length() - 1);
        int imageNumber = Integer.parseInt(imageNumberString);

        Locator FIRST_IMAGE = getPopUpCardImageLocator(1);
        driverTools.waitForElementToAppear(FIRST_IMAGE);
        Assertion.assertThat("Image " + 1 + " is displayed", driverTools.isElementDisplayed(FIRST_IMAGE), is(true));

        for (int j = 2; j <= imageNumber; j++) {
            driverTools.click(getNextImageButtonLocator(j));
            Locator NEXT_IMAGE = getPopUpCardImageLocator(j);
            driverTools.waitForElementToAppear(NEXT_IMAGE);
            Assertion.assertThat("Next image " + j + " is displayed", driverTools.isElementDisplayed(NEXT_IMAGE), is(true));
        }

        for (int k = imageNumber - 1; k >= 1; k--) {
            driverTools.click(getPreviousImageButtonLocator(k));
            Locator PREVIOUS_IMAGE = getPopUpCardImageLocator(k);
            driverTools.waitForElementToAppear(PREVIOUS_IMAGE);
            Assertion.assertThat("Previous image " + k + " is displayed", driverTools.isElementDisplayed(PREVIOUS_IMAGE), is(true));
        }

        driverTools.click(CLOSE_IMAGE_POP_UP);
    }

    @Override
    public Locator getSRPCardImageLocator(int index) {
    return new Locator(
        "SRP_CARD_IMAGE_" + index,
        new By.ByXPath("(//div[contains(@class, 'ScheduleCard_multiple_images') and contains(@class, 'keen-slider__slide')])[" + index + "]"));
    }

    @Override
    public Locator getPopUpCardImageLocator(int index) {
        return new Locator("POP_UP_CARD_IMAGE_" + index,
                new By.ByXPath("//*[starts-with(normalize-space(text()), '" + index + "/')]"));
    }

    private Locator getNextImageButtonLocator(int index) {
        return new Locator("NEXT_IMAGE_BUTTON_" + index,new By.ByXPath("(//button[contains(@class, 'ImageThumbnailMobile_image_thumbnail')])[" + index + "]"));
    }

    private Locator getPreviousImageButtonLocator(int index) {
        return new Locator("PREVIOUS_IMAGE_BUTTON_" + index,new By.ByXPath("(//button[contains(@class, 'ImageThumbnailMobile_image_thumbnail')])[" + index + "]"));
    }

    @Override
    public void clickEditSearchButton() {
        driverTools.clickWithJavascriptExecutor(EDIT_SEARCH_BUTTON);
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
        driverTools.clickWithJavascriptExecutor(SAVE_SEARCH_EDIT_BUTTON);
    }

    @Override
    public void verifyChangeSearch(String departurePort, String destinationPort, String checkInTime, int totalPassenger, String departureDate, String ticketType) {
        driverTools.waitForElementToAppear(SEARCH_RESULT);
        Assertion.assertThat("Search result is displayed", driverTools.isElementDisplayed(SEARCH_RESULT), is(true));

        clickEditSearchButton();

        String srpDeparturePort = driverTools.waitForElementToAppear(CHANGE_DEPARTURE_PORT).orElseThrow().getText();
        Assertion.assertThat("Departure port is updated in SRP", srpDeparturePort, is(departurePort));

        String srpDestinationPort = driverTools.waitForElementToAppear(CHANGE_DESTINATION_PORT).orElseThrow().getText();
        Assertion.assertThat("Destination port is updated in SRP", srpDestinationPort, is(destinationPort));

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
