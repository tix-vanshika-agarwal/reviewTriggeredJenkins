package com.tiket.web.page.nft.train.searchResult_Page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.train.TrainClass;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TrainSearchResultPageWeb<T extends WebDriver> extends WebPage<T> {
    public TrainSearchResultPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TrainSearchResultPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new TrainSearchResultPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new TrainSearchResultPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void selectTrainNameSectionInFilter();
    public abstract void selectClassType();

    private static final Locator FILTER_CHIPS = new Locator("FILTER_CHIPS", new By.ByXPath("(//*[contains(@class, 'chip')])[1]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[contains(@class, 'Button_variant_primary')]//span[normalize-space(text())='Save']"));
    public static final Locator FILTER_CLASS_TYPE = new Locator("FILTER_CLASS_TYPE", new By.ByXPath("//span[contains(@class, 'Text') and (text()='Tipe Kelas' or text()='Class Type')]"));
    public static final Locator TRAIN_CARD = new Locator("TRAIN_CARD", new By.ByXPath("//*[contains(@class, 'schedule_card')]"));
    public static final Locator FILTER_TRAIN_NAME = new Locator("FILTER_TRAIN_NAME", new By.ByXPath("//span[contains(@class, 'Text') and (text()='Nama Kereta Api' or text()='Train Name')]"));
    private static final Locator TRAIN_CAR_LIST_CONTAINER = new Locator("TRAIN_CAR_LIST_CONTAINER", new By.ByXPath("//*[contains(@class, 'SearchResult_container')]"));
    private static final Locator TRAIN_RECENTLY_SEARCHED = new Locator("TRAIN_RECENTLY_SEARCHED", new By.ByXPath("//*[contains(@class, 'SliderRecentSearch_slider_wrapper')]"));
    private static final Locator FILTER_CHIPS_APPLIED = new Locator("FILTER_CHIPS_APPLIED", new By.ByXPath("//span[contains(@class, 'Badge')]"));
    private static final Locator TRAIN_CARD_PRICE_SRP = new Locator("TRAIN_CARD_PRICE", new By.ByXPath("//*[contains(@class,'PriceModuleV2_wrapper_main_price')]//div[contains(text(),'IDR')]"));
    private static final Locator PRICE_OF_TRAIN = new Locator("PRICE_OF_TRAIN", new By.ByXPath("(//*[contains(@class,'main_price')]/*[contains(@class,'Text_variant_price')])[1]"));
    private static final Locator RETURN_CHECK = new Locator("RETURN_CHECK", new By.ByXPath("//div[normalize-space(text())='Select Return Train' or normalize-space(text())='Pilih Kereta Pulang']"));
    private static final Locator SEE_MORE_SORT_OPTIONS = new Locator("SEE_MORE_SORT_OPTIONS", new By.ByXPath("//button[contains(@class, 'SortForm')]"));
    private static final Locator SORT_BY_LOWEST_PRICE = new Locator("SORT_BY_LOWEST_PRICE", new By.ByXPath("//input[contains(@value, 'lowestPrice') and @name='sort-in-expanded']"));
    private static final Locator SORT_BY_SHORTEST_DURATION = new Locator("SORT_BY_SHORTEST_DURATION", new By.ByXPath("//input[contains(@value, 'shortestDuration') and @name='sort-in-expanded']"));
    private static final Locator SORT_BY_EARLIEST_DEPARTURE = new Locator("SORT_BY_EARLIEST_DEPARTURE", new By.ByXPath("//input[contains(@value, 'earliestDeparture') and @name='sort-in-expanded']"));
    private static final Locator SORT_BY_LATEST_DEPARTURE = new Locator("SORT_BY_LATEST_DEPARTURE", new By.ByXPath("//input[contains(@value, 'latestDeparture') and @name='sort-in-expanded']"));
    private static final Locator SORT_BY_EARLIEST_ARRIVAL = new Locator("SORT_BY_EARLIEST_ARRIVAL", new By.ByXPath("//input[contains(@value, 'earliestArrival') and @name='sort-in-expanded']"));
    private static final Locator SORT_BY_LATEST_ARRIVAL = new Locator("SORT_BY_LATEST_ARRIVAL", new By.ByXPath("//input[contains(@value, 'latestArrival') and @name='sort-in-expanded']"));
    private static final String AVAILABLE_TRAIN_CARD = "//div[contains(@class, 'schedule_card') and not(contains(@class, 'ActionBanner')) and not(.//text()[contains(., 'Tickets sold out') or contains(., 'Tiket habis terjual')])]";
    private static final String DURATION_SELECTOR = "//div[contains(@class, 'duration') and normalize-space(text()) != '']";
    private static final Locator AVAILABLE_TRAIN_DURATION = new Locator("AVAILABLE_TRAIN_DURATION", new By.ByXPath(AVAILABLE_TRAIN_CARD + DURATION_SELECTOR));
    private static final Locator TRAIN_DEPARTURE_TIME = new Locator("TRAIN_DEPARTURE_TIME", new By.ByXPath("//div[contains(@class, 'journey_location')][1]//div[contains(@class, 'highEmphasis')]"));
    private static final Locator TRAIN_ARRIVAL_TIME = new Locator("TRAIN_ARRIVAL_TIME", new By.ByXPath("//div[contains(@class, 'journey_location')][2]//div[contains(@class, 'highEmphasis')]"));

    public enum SortType {
        LOWEST_PRICE,
        SHORTEST_DURATION,
        EARLIEST_DEPARTURE,
        LATEST_DEPARTURE,
        EARLIEST_ARRIVAL,
        LATEST_ARRIVAL
    }

    public void clickFilterChips() {
        driverTools.clickWithJavascriptExecutor(FILTER_CHIPS);
    }

    public void clickSaveButton() {
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void filterByClass(TrainClass trainClass) {
        driverTools.clickWithJavascriptExecutor(getClassLocatorInFilter(trainClass));
    }

    private Locator getClassLocatorInFilter(TrainClass trainClass) {
        String classId = trainClass.id;
        String classEng = trainClass.eng;
        return new Locator("TRAIN_CLASS_FILTER", new By.ByXPath("//span[contains(@class, 'Text') and (text()='"+classId+"') or (text()='"+classEng+"')]"));
    }

    public boolean verifyClass(TrainClass trainClass) {
        driverTools.waitForElementToAppear(TRAIN_CARD).orElseThrow();

        List<WebElement> listOfTrainClass = driverTools.elements(getClassLocatorInSRP(trainClass)).orElseThrow();

        WebElement firstTrainClassElement = listOfTrainClass.get(0);
        String firstTrainClassValue = driverTools.getText(firstTrainClassElement);
        String firstTrainClass = CoreFunctions.getSectionOfString(firstTrainClassValue, "\\(", 0).trim();
        logger.log("First train class in SRP: " + firstTrainClass);

        WebElement lastTrainClassElement = listOfTrainClass.get(listOfTrainClass.size() - 1);
        String lastTrainClassValue = driverTools.getText(lastTrainClassElement);
        String lastTrainClass = CoreFunctions.getSectionOfString(lastTrainClassValue, "\\(", 0).trim();
        logger.log("Last train class in SRP: " + lastTrainClass);

        return firstTrainClass.equalsIgnoreCase(lastTrainClass);
    }

    private Locator getClassLocatorInSRP(TrainClass trainClass) {
        String classId = trainClass.id;
        String classEng = trainClass.eng;
        return new Locator("TRAIN_CLASS_SRP", new By.ByXPath("//*[contains(@class, 'Text') and (text()='"+classId+"') or (text()='"+classEng+"')]"));
    }

    private Locator getTrainNameLocatorInFilter(String trainName) {
        return new Locator("TRAIN_NAME_FILTER", new By.ByXPath("//span[contains(@class, 'Text') and (text()='" + trainName + "')]"));
    }

    public void filterByName(String trainName) {
        driverTools.clickWithJavascriptExecutor(getTrainNameLocatorInFilter(trainName));
    }

    public Locator getTrainNameLocatorInSRP(String trainName) {
        return new Locator("TRAIN_NAME_SRP", new By.ByXPath("//*[contains(@class, 'Text') and contains(text(), '" + trainName + "')]"));
    }

    public boolean verifyName(String trainName) {
        driverTools.waitForElementToAppear(TRAIN_CARD).orElseThrow();

        WebElement targetTrainNameElement = driverTools.waitForElementToAppear(getTrainNameLocatorInSRP(trainName)).orElseThrow();
        String targetTrainNameValue = driverTools.getText(targetTrainNameElement).trim();

        String[] parts = targetTrainNameValue.split("\\s+");

        String completeTargetTrainName;
        if (parts.length >= 2) {
            completeTargetTrainName = parts[0] + " " + parts[1];
        } else {
            completeTargetTrainName = parts[0];
        }

        logger.log("Trimmed target train name in SRP: " + completeTargetTrainName);

        return completeTargetTrainName.equalsIgnoreCase(trainName);
    }


    public void selectTrain(){
        driverTools.waitForElementToAppear(TRAIN_CARD);
        driverTools.clickWithJavascriptExecutor(TRAIN_CARD);
    }

    public boolean checkTrainCard(){
        driverTools.waitForElementToAppear(TRAIN_CAR_LIST_CONTAINER);
        return driverTools.isElementDisplayed(TRAIN_CAR_LIST_CONTAINER);
    }

    public boolean goBackToLandingTrainPage(){
        driverTools.back();
        driverTools.waitForElementToAppear(TRAIN_RECENTLY_SEARCHED);
        return driverTools.isElementDisplayed(TRAIN_RECENTLY_SEARCHED);
    }

    public boolean isFilterChipsApplied() {
        driverTools.waitForElementToAppear(FILTER_CHIPS_APPLIED);
        return driverTools.isElementDisplayed(FILTER_CHIPS_APPLIED);
    }

    public boolean isSrpPriceShownIdr() {
        List<WebElement> listOfTrainCardPrice = driverTools.elements(TRAIN_CARD_PRICE_SRP).orElseThrow();

        WebElement firstTrainCardPriceElement = listOfTrainCardPrice.get(0);
        driverTools.scrollIntoWebView(firstTrainCardPriceElement);
        String firstTrainCardPriceValue = driverTools.getText(firstTrainCardPriceElement);
        String firstTrainCardPriceCurrency = CoreFunctions.getSectionOfString(firstTrainCardPriceValue, " ", 0).trim();
        logger.log("First train card price currency: " + firstTrainCardPriceCurrency);

        WebElement lastTrainCardPriceElement = listOfTrainCardPrice.get(listOfTrainCardPrice.size() - 1);
        driverTools.scrollIntoWebView(lastTrainCardPriceElement);
        String lastTrainCardPriceValue = driverTools.getText(lastTrainCardPriceElement);
        String lastTrainCardPriceCurrency = CoreFunctions.getSectionOfString(lastTrainCardPriceValue, " ", 0).trim();
        logger.log("Last train card price currency: " + lastTrainCardPriceCurrency);

        return firstTrainCardPriceCurrency.equalsIgnoreCase(lastTrainCardPriceCurrency);
    }

    public void editInfantPaxInSrp(String queryParam, String newValue) {
        driverTools.editUrlQueryParam(queryParam, newValue);
    }

    public String getPrice(){
        driverTools.waitForElementToAppear(PRICE_OF_TRAIN);
        String priceText = driverTools.getText(PRICE_OF_TRAIN).orElseThrow();
        return priceText.replaceAll("[^0-9]", "");
    }

    public boolean checkReturn() {
        try {
            driverTools.waitForElementToAppear(RETURN_CHECK, 5);
            return driverTools.isElementPresent(RETURN_CHECK);
        } catch (Exception e) {
            logger.warn("FILTER_CHECK not found: " + e.getMessage());
            return false;
        }
    }

    public void openAllSortOption() {
        driverTools.waitForElementToAppear(SEE_MORE_SORT_OPTIONS).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEE_MORE_SORT_OPTIONS);
    }

    private void clickSortOption(Locator locator) {
        driverTools.clickWithJavascriptExecutor(locator);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
        driverTools.dummyWait(1); // wait to settle the page after sorting
    }

    public void selectSortType(SortType sortType) {
        switch (sortType) {
            case LOWEST_PRICE -> clickSortOption(SORT_BY_LOWEST_PRICE);
            case SHORTEST_DURATION -> clickSortOption(SORT_BY_SHORTEST_DURATION);
            case EARLIEST_DEPARTURE -> clickSortOption(SORT_BY_EARLIEST_DEPARTURE);
            case LATEST_DEPARTURE -> clickSortOption(SORT_BY_LATEST_DEPARTURE);
            case EARLIEST_ARRIVAL -> clickSortOption(SORT_BY_EARLIEST_ARRIVAL);
            case LATEST_ARRIVAL -> clickSortOption(SORT_BY_LATEST_ARRIVAL);
            default -> throw new NotImplementedException("Sort not implemented");
        }
    }

    public List<Integer> verifySortByLowestPrice() {
        driverTools.waitForElementToAppear(TRAIN_CARD_PRICE_SRP).orElseThrow();

        List<WebElement> listOfTrainCardPrice = driverTools.elements(TRAIN_CARD_PRICE_SRP).orElseThrow();
        List<Integer> prices = new ArrayList<>();

        for (WebElement priceElement : listOfTrainCardPrice) {
            driverTools.scrollIntoWebView(priceElement);
            String priceValue = driverTools.getText(priceElement);
            String priceAmount = CoreFunctions.getSectionOfString(priceValue, " ", 1).trim();
            int price = Integer.parseInt(priceAmount.replace(",", "").replace(".", ""));
            prices.add(price);
        }
        return prices;
    }

    public List<Integer> verifySortByShortestDuration() {
        driverTools.waitForElementToAppear(AVAILABLE_TRAIN_DURATION).orElseThrow();
        List<WebElement> elements = driverTools.elements(AVAILABLE_TRAIN_DURATION).orElseThrow();
        List<Integer> durations = new ArrayList<>();
        for (WebElement el : elements) {
            driverTools.scrollIntoWebView(el);
            durations.add(convertDurationToMinutes(driverTools.getText(el)));
        }
        return durations;
    }

    /*
    This method converts a duration string into total minutes.
    It's used to standardize time durations for comparison when sorting train schedules by duration.
    "2h 30m" (2 hours 30 minutes)
    "1j 45m" (1 jam/hour 45 minutes - Indonesian format)
    "45m" (45 minutes only)
     */
    private int convertDurationToMinutes(String duration) {
        int totalMinutes = 0;
        duration = duration.trim();

        Pattern hoursPattern = Pattern.compile("(\\d+)[hj]");
        Matcher hoursMatcher = hoursPattern.matcher(duration);
        if (hoursMatcher.find()) {
            int hours = Integer.parseInt(hoursMatcher.group(1));
            totalMinutes += hours * 60;
        }

        Pattern minutesPattern = Pattern.compile("(\\d+)m");
        Matcher minutesMatcher = minutesPattern.matcher(duration);
        if (minutesMatcher.find()) {
            int minutes = Integer.parseInt(minutesMatcher.group(1));
            totalMinutes += minutes;
        }

        return totalMinutes;
    }

    public List<Integer> getTrainDepartureTimes() {
        driverTools.waitForElementToAppear(TRAIN_DEPARTURE_TIME).orElseThrow();
        List<WebElement> elements = driverTools.elements(TRAIN_DEPARTURE_TIME).orElseThrow();
        List<Integer> times = new ArrayList<>();
        for (WebElement el : elements) {
            driverTools.scrollIntoWebView(el);
            times.add(parseTimeToMinutes(driverTools.getText(el).trim()));
        }
        return times;
    }

    /*
    This method converts a time string in 24-hour format (HH:MM) into total minutes.
    It's used to standardize times for comparing departure and arrival times when verifying the sorting order of train schedules.
    "14:30" -> 870 minutes (14 * 60 + 30)
    "09:15" -> 555 minutes (9 * 60 + 15)
    "23:45" -> 1425 minutes (23 * 60 + 45)
     */
    private int parseTimeToMinutes(String timeString) {
        try {
            timeString = timeString.trim();
            String[] parts = timeString.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            return hours * 60 + minutes;
        } catch (Exception e) {
            logger.log("Error parsing time: " + timeString + " - " + e.getMessage());
            throw new RuntimeException("Failed to parse departure time: " + timeString, e);
        }
    }

    public List<Integer> getTrainArrivalTimes() {
        driverTools.waitForElementToAppear(TRAIN_ARRIVAL_TIME).orElseThrow();
        List<WebElement> elements = driverTools.elements(TRAIN_ARRIVAL_TIME).orElseThrow();
        List<Integer> times = new ArrayList<>();
        for (WebElement el : elements) {
            driverTools.scrollIntoWebView(el);
            times.add(parseTimeToMinutes(driverTools.getText(el).trim()));
        }
        return times;
    }
}