package com.tiket.web.page.nft.airport_transfer.landing_page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class AirportTransferLandingPageWeb<T extends WebDriver> extends WebPage<T> {

    public AirportTransferLandingPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AirportTransferLandingPageWeb<T> getInstance(Session<T> session) {
        return switch (Configs.PLATFORM) {
            case DWEB -> new AirportTransferLandingPageDWeb<>(session);
            case AMWEB, IMWEB, ANDROID, IOS -> new AirportTransferLandingPageMWeb<>(session);
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        };
    }

    private static final Locator SKIP_BUTTON = new Locator("SKIP_BUTTON", new By.ByXPath("//button[contains(@class,'Coachmark_coachmark_skip_bt') or contains(@class,'Coachmark_coachmark_done_btn')] | //*[contains(text(),'Lewati') or contains(text(),'Skip')]"));
    private static final Locator SKIP_POPUP = new Locator("SKIP_POPUP", AppiumBy.xpath("//span[contains(text(),'OK')]"));
    private static final Locator FROM_AIRPORT = new Locator("FROM_AIRPORT", AppiumBy.xpath("//button//*[text()='From Airport' or text()='Dari Bandara']"));
    private static final Locator AIRPORT_SEARCH = new Locator("AIRPORT_SEARCH", AppiumBy.xpath("//*[contains(@class,'AirportInput_airport_label')]"));
    private static final Locator DESTINATION_SEARCH = new Locator("DESTINATION_SEARCH", AppiumBy.cssSelector("*[data-testid='search-form-location-input']"));
    private static final Locator SEARCH_INPUT = new Locator("SEARCH_INPUT", AppiumBy.xpath("//*[contains(@class,'SearchBox_placeholder')]//input"));
    private static final Locator SEARCH_LIST = new Locator("SEARCH_LIST", AppiumBy.xpath("//*[contains(@class,'List_title')]"));
    private static final Locator SAVE_LOCATION = new Locator("SAVE_LOCATION", AppiumBy.xpath("//*[contains(@class,'MapsPinPicker_addrees')]//following-sibling::button"));
    private static final Locator SEARCH_CARS = new Locator("SEARCH_CARS", AppiumBy.xpath("//*[contains(@class,'SearchForm_search_form')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator SAVE_DESTINATION = new Locator("SAVE_DESTINATION", new By.ByXPath("//*[contains(@class,'MapsPinPicker_destination') or contains(@class,'MapsPinPicker_desktop_footer_buttons')]/button[contains(@class,'Button_variant_primary')]"));
    private static final Locator SEE_YOUR_CAR = new Locator("SEE_YOUR_CAR", new By.ByXPath("(//button[contains(@class, 'Button_button') and contains(@class, 'Button_horizontal_padding')])[2]"));
    private static final Locator POPULAR_AIRPORT_LIST = new Locator("POPULAR_AIRPORT_LIST", new By.ByXPath("(//div[contains(@class, 'AirportPicker_popular_cities')])[2]"));
    private static final Locator POPULAR_DESTINATION_LIST = new Locator("POPULAR_DESTINATION_LIST", new By.ByXPath("(//div[contains(@class, 'LocationPicker_popular_cities')])[2]"));
    private static final Locator INPUT_AIRPORT = new Locator("INPUT_AIRPORT", new By.ByXPath("(//div[contains(@class, 'SearchBox_placeholder')]//input)[1]"));
    private static final Locator INPUT_DESTINATION = new Locator("INPUT_DESTINATION", new By.ByXPath("(//div[contains(@class, 'SearchBox_placeholder')]//input)[1]"));
    public static final Locator DATE_SECTION = new Locator("DATE_SECTION", new By.ByXPath("//*[contains(@class, 'DateTimeInputRevamp_date_input')]"));
    private static final Locator CHOOSE_TIME_INPUT = new Locator("CHOOSE_TIME_INPUT", new By.ByXPath("//*[contains(@class, 'DateTimeInputRevamp_time_input')]"));
    private static final Locator SAVE_CHOSEN_TIME = new Locator("SAVE_CHOSEN_TIME", new By.ByXPath("//*[@id='modal-root']//button[.//span[contains(normalize-space(),'Save') or contains(normalize-space(),'Simpan')]]"));
    private static final Locator PASSENGER_INPUT = new Locator("PASSENGER_INPUT", new By.ByXPath("//*[contains(@class, 'PassengerInput_input_text')]"));
    private static final Locator PASSENGER_SAVE_BUTTON = new Locator("PASSENGER_SAVE_BUTTON", new By.ByXPath("(//*[contains(@class, 'PassengerInput_save_button')])//button"));
    private static final Locator BOOKMARK_ADDRESS = new Locator("BOOKMARK_ADDRESS", new By.ByXPath("//div[@id='coachmark-portal']//button[.//span[normalize-space()='OK' or normalize-space()='Oke']]"));
    private static final Locator DEPART_DATE_CALENDAR = new Locator("DEPART_DATE_CALENDAR", new By.ByXPath("//span[contains(@class, 'Date')]"));
    private static final Locator LATEST_RECENT_SEARCH = new Locator("LATEST_RECENT_SEARCH", new By.ByXPath("//*[contains(@class,'recent_maplocation_title')]/following-sibling::*[contains(@class,'maplocation')]//*[contains(@class,'List_title')]"));

    public void clickSkipButton() {
        driverTools.waitForElementToAppear(SKIP_BUTTON, 5);
        if (driverTools.isElementDisplayed(SKIP_BUTTON)) {
            driverTools.clickWithJavascriptExecutor(SKIP_BUTTON);
        }
        driverTools.waitForElementToAppear(SKIP_POPUP, 5).ifPresent(driverTools::click);
    }

    public void closePopup() {
        clickSkipButton();
    }

    public void chooseFromAirport() {
        driverTools.click(FROM_AIRPORT);
    }

    private void chooseSearchListItem(String itemText) {
        List<WebElement> searchList = driverTools.waitForElementsToAppear(SEARCH_LIST).orElseThrow();
        logger.log("Search list items total: " + searchList.size());
        for (WebElement listItem : searchList) {
            if (driverTools.getText(listItem).toLowerCase().contains(itemText.toLowerCase())) {
                driverTools.click(listItem);
                break;
            } else {
                logger.log("Item not found in search list: " + itemText);
            }
        }
    }

    public void searchAirport(String airportNameOrCode) {
        driverTools.click(AIRPORT_SEARCH);
        driverTools.sendKeys(SEARCH_INPUT, airportNameOrCode);
        chooseSearchListItem(airportNameOrCode);
    }

    public void searchDestination(String destination) {
        driverTools.click(DESTINATION_SEARCH);
        driverTools.sendKeys(SEARCH_INPUT, destination);
        chooseSearchListItem(destination);
        driverTools.click(SAVE_LOCATION);
    }

    public void searchCars() {
        driverTools.click(SEARCH_CARS);
    }

    public void selectTransferType(String type) {
        Locator TRANSFER_TYPE_TAB = getTransferTypeTabLocator(type);
        driverTools.click(TRANSFER_TYPE_TAB);
    }

    private Locator getTransferTypeTabLocator(String type) {
        String normalized = type.replace('-', '_').toLowerCase(Locale.ROOT);
        String idText;
        String enText;
        switch (normalized) {
            case "transfer_out" -> { idText = "Dari Bandara";   enText = "From Airport"; }
            case "transfer_in"  -> { idText = "Menuju Bandara"; enText = "To Airport";   }
            default -> throw new NotImplementedException(
                    String.format("Given transfer type : %s not exist", type));
        }
        String xpath = String.format(
                "//button//span[normalize-space()='%s' or normalize-space()='%s']",
                enText, idText);
        return new Locator("TRANSFER_TYPE_TAB", AppiumBy.xpath(xpath));
    }

    public Map<String, String> fillTransferFields(String type, String airport, String destination) {
        Map<String, String> selected = new HashMap<>();
        String normalized = type.replace('-', '_').toLowerCase(Locale.ROOT);
        if (normalized.equals("transfer_in")) {
            // Pickup = non-airport (1st input), drop-off = airport (2nd input)
            setSelectDestination(normalized);
            selected.put("destination", setInputDestination(destination));

            setSelectAirport(normalized);
            selected.put("airport", setInputAirport(airport));
        } else if (normalized.equals("transfer_out")) {
            // Pickup = airport (1st input), drop-off = non-airport (2nd input)
            setSelectAirport(normalized);
            selected.put("airport", setInputAirport(airport));

            setSelectDestination(normalized);
            selected.put("destination", setInputDestination(destination));
        } else {
            throw new IllegalArgumentException(
                    String.format("Given transfer type : %s not supported", type));
        }
        return selected;
    }

    public void setSelectAirport(String currentTransferType) {
        currentTransferType = currentTransferType.replace('-', '_').toLowerCase(Locale.ROOT);
        logger.log("Current transfer type for setting: " + currentTransferType);
        Locator airportLocator = currentTransferType.equalsIgnoreCase("transfer_out")
                ? new Locator("SELECT_AIRPORT", new By.ByXPath("(//*[contains(@class, 'LocationInputRevamp_input_text')])[1]"))
                : new Locator("SELECT_AIRPORT", new By.ByXPath("(//*[contains(@class, 'LocationInputRevamp_input_text')])[2]"));
        driverTools.click(airportLocator);
    }

    public void setSelectDestination(String currentTransferType) {
        currentTransferType = currentTransferType.replace('-', '_').toLowerCase(Locale.ROOT);
        logger.log("Current transfer type for setting: " + currentTransferType);
        Locator destinationLocator = currentTransferType.equalsIgnoreCase("transfer_out")
                ? new Locator("SELECT_DESTINATION", new By.ByXPath("(//*[contains(@class, 'LocationInputRevamp_input_text')])[2]"))
                : new Locator("SELECT_DESTINATION", new By.ByXPath("(//*[contains(@class, 'LocationInputRevamp_input_text')])[1]"));
        driverTools.click(destinationLocator);
    }

    public boolean checkPopularAirportList() {
        driverTools.waitForElementToAppear(POPULAR_AIRPORT_LIST);
        return driverTools.isElementDisplayed(POPULAR_AIRPORT_LIST);
    }

    public boolean checkPopularDestinationList() {
        driverTools.waitForElementToAppear(POPULAR_DESTINATION_LIST);
        return driverTools.isElementDisplayed(POPULAR_DESTINATION_LIST);
    }

    public String selectPopularDestination(String name) {
        Locator popularItem = new Locator("POPULAR_DESTINATION_ITEM_" + name,
                new By.ByXPath("((//div[contains(@class, 'LocationPicker_popular_cities')])[2]"
                        + "//*[self::a or self::button or self::div or self::span or self::p]"
                        + "[contains(normalize-space(.), '" + name + "')])[1]"));
        driverTools.waitForElementToAppear(popularItem);
        driverTools.scrollIntoWebView(popularItem);
        driverTools.clickWithJavascriptExecutor(popularItem);
        driverTools.waitForElementToAppear(BOOKMARK_ADDRESS).ifPresent(driverTools::click);
        WebElement saveDestination = driverTools.waitForElementToPresence(SAVE_DESTINATION).orElseThrow();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(saveDestination);
        return name;
    }

    public String setInputAirport(String location) {
        driverTools.waitForElementToAppear(INPUT_AIRPORT);
        driverTools.clickWithJavascriptExecutor(INPUT_AIRPORT);
        driverTools.sendKeys(INPUT_AIRPORT, location);
        WebElement searchFormResult = waitForSearchFormResult(location);
        String text = driverTools.getText(searchFormResult);
        driverTools.clickWithJavascriptExecutor(searchFormResult);
        String[] lines = text.split("\n");
        String[] words = lines[0].trim().split(" ");
        return (words.length >= 2) ? words[0] + " " + words[1] : lines[0];
    }

    public String setInputDestination(String location) {
        driverTools.waitForElementToAppear(INPUT_DESTINATION);
        driverTools.clickWithJavascriptExecutor(INPUT_DESTINATION);
        driverTools.sendKeys(INPUT_DESTINATION, location);
        WebElement searchFormResult = waitForSearchFormResult(location);
        String text = driverTools.getText(searchFormResult);
        driverTools.clickWithJavascriptExecutor(searchFormResult);
        driverTools.waitForElementToAppear(BOOKMARK_ADDRESS).ifPresent(driverTools::click);
        driverTools.click(SAVE_DESTINATION);
        String[] lines = text.split("\n");
        String[] words = lines[0].trim().split(" ");
        return (words.length >= 2) ? words[0] + " " + words[1] : lines[0];
    }


    /** Opens the destination picker and returns the newest recent-search entry shown inside it. */
    public String getLatestRecentSearch(String transferType) {
        setSelectDestination(transferType);
        String latestRecentSearch = driverTools.getText(driverTools.waitForElementToAppear(LATEST_RECENT_SEARCH).orElseThrow());
        logger.log("Latest recent search in destination picker: " + latestRecentSearch);
        return latestRecentSearch;
    }

    /** Picks the newest recent-search entry in the already opened destination picker. */
    public void selectLatestRecentSearch() {
        driverTools.clickWithJavascriptExecutor(LATEST_RECENT_SEARCH);
        driverTools.waitForElementToAppear(BOOKMARK_ADDRESS, 5).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(SAVE_DESTINATION, 5).ifPresent(driverTools::click);
    }

    private WebElement waitForSearchFormResult(String location) {
        return driverTools.waitForElementToAppear(getSearchFormResultLocator(location), 10)
                .or(() -> driverTools.waitForElementToAppear(getSearchFormResultFallbackLocator(location)))
                .orElseThrow();
    }

    private Locator getSearchFormResultFallbackLocator(String location) {
        return new Locator("SEARCH_FORM_RESULT_FALLBACK", new By.ByXPath("//p[contains(@class, 'List_title')]//*[contains(text(), '" + location + "')] | //p[contains(@class, 'List_title')][contains(text(), '" + location + "')]"));
    }

    private Locator getSearchFormResultLocator(String location) {
        return new Locator("SEARCH_FORM_RESULT", new By.ByXPath("//p[contains(@class, 'List_title')]//*[contains(text(), '" + location + "')]"));
    }

    public abstract void selectDate(String departureDate);

    protected Locator getDateLocator(String day, String monthEng, String monthId, String year) {
        return new Locator("DATE_LOCATOR", new By.ByXPath("(//span[contains(@aria-label, '" + day + " " + monthEng + " " + year + "') or contains(@aria-label, '" + day + " " + monthId + " " + year + "')])[1]"));
    }

    public void selectTime(String chooseTime) {
        driverTools.click(CHOOSE_TIME_INPUT);
        driverTools.click(SAVE_CHOSEN_TIME);
    }

    public void selectPassenger(String passengerCount) {
        driverTools.click(PASSENGER_INPUT);
        driverTools.click(PASSENGER_SAVE_BUTTON);
    }

    public void seeCarListOnSRP() {
        driverTools.click(SEE_YOUR_CAR);
        driverTools.waitForElementToDisappear(SEE_YOUR_CAR);
    }

    public void clickOnCalendarWidgetToOpen() {
        logger.log("Opening calendar widget...");
        driverTools.clickWithJavascriptExecutor(DEPART_DATE_CALENDAR);
    }
}