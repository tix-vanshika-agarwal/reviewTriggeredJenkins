package com.tiket.page.bus.searchresultpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.bus.BusSrpOneWayData;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class BusSearchResultPage <T extends WebDriver> extends BasePage<T> {

    private static final Locator BUS_LIST_PRICES = new Locator("BUS_LIST_PRICES",AppiumBy.xpath("//div//span[contains(@class,'ScheduleCard_final_price')]"));
    private static final Locator FIRST_BUS_SCHEDULE_CONTAINER = new Locator("FIRST_BUS_SCHEDULE_CONTAINER", AppiumBy.xpath("(//div[@data-testid='srp-schedule-card'])[1]"));
    private static final Locator BUS_SCHEDULE_TITLE = new Locator("BUS_SCHEDULE_TITLE", AppiumBy.xpath("//*[contains(@class,'ScheduleCard_bus_operator')]"));
    private static final Locator BUS_SCHEDULE_CLASS = new Locator("BUS_SCHEDULE_CLASS", AppiumBy.xpath("//*[contains(@class,'ScheduleCard_bus_operator')]//following-sibling::*"));
    private static final Locator BUS_DEPARTURE_TIME = new Locator("BUS_DEPARTURE_TIME", AppiumBy.xpath("//*[contains(@class,'TripRoute_departure_checkpoint')]//div[contains(@class,'TripRoute_trip_route')]/div[contains(@class,'TripRoute_time')][1]"));

    private static final Locator SRP_PASSENGER_AND_DATE_INFORMATION = new Locator("SRP_PASSENGER_AND_DATE_INFORMATION", AppiumBy.xpath("//span[contains(@class,'SRPHeader_schedule_passenger_count')]"));
    private static final Locator SKIP_COACHMARK = new Locator("SKIP_COACHMARK",AppiumBy.xpath("//*[contains(text(),'Lewati') or contains(text(),'Skip')]"));
    private static final Locator FILTER_AND_SORT_COACHMARK = new Locator("FILTER_AND_SORT_COACHMARK",AppiumBy.xpath("//button[*[contains(text(),'Filter')]]"));
    private static final Locator BUS_OPERATOR_SECTION = new Locator("BUS_OPERATOR_SECTION", AppiumBy.xpath("//section[@data-section='operator']"));
    private static final Locator APPLY_FILTER_BTN = new Locator("APPLY_FILTER_BTN", AppiumBy.xpath("//div[contains(@class,'submit')]//button"));
    private static final Locator SRP_CHANGE_BTN = new Locator("SRP_CHANGE_BTN", AppiumBy.xpath("//*[contains(@class,'SRPHeader_change_button')]"));
    private static final Locator SCHEDULE_LIST_CARD = new Locator("SCHEDULE_LIST_CARD", AppiumBy.xpath("//*[contains(@class,'ScheduleList_card')]"));
    private static final Locator SRP_HEADER_ORIGIN_DESTINATION = new Locator("SRP_HEADER_ORIGIN_DESTINATION", AppiumBy.xpath("//span[contains(@class,'SRPHeader_full_line_truncate')]"));
    private static final Locator SRP_HEADER_ORIGIN = new Locator("SRP_HEADER_ORIGIN", AppiumBy.xpath("//span[contains(@class,'SRPHeader_origin')]"));
    private static final Locator SRP_HEADER_DESTINATION = new Locator("SRP_HEADER_DESTINATION", AppiumBy.xpath("//span[contains(@class,'SRPHeader_destination')]"));
    private static final Locator SRP_HEADER_PATH = new Locator("SRP_HEADER_PATH", AppiumBy.xpath("//span[contains(@class,'SRPHeader_full_line_truncate')]"));
    private static final Locator SRP_HEADER_CHANGE_BTN = new Locator("SRP_HEADER_CHANGE_BTN", AppiumBy.xpath("//button[contains(@class,'SRPHeader_change_button')]"));
    private static final Locator SRP_HEADER_CHANGE_SEARCH_BTN = new Locator("SRP_HEADER_CHANGE_SEARCH_BTN", AppiumBy.xpath("//span[contains(text(),'Search bus') or contains(text(),'Cari bus')]//parent::button"));
    private static final Locator SRP_HEADER_BACK_BTN = new Locator("SRP_HEADER_BACK_BTN", AppiumBy.xpath("//button[contains(@class,'SRPHeader_back_button')]"));

    public BusSearchResultPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> BusSearchResultPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new BusSearchResultPageAndroid<>(session);}
            case IOS -> {return new BusSearchResultPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public boolean verifyPricesAreInIdrOnly(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("bus-travel/search");
        driverTools.waitForElementToAppear(BUS_LIST_PRICES,10);
        List<WebElement> finalPrices = driverTools.elements(BUS_LIST_PRICES).orElseThrow();
        Assertion.assertThat("Verify srp is not empty", finalPrices.size(), greaterThan(0));
        finalPrices.forEach(e -> {
            String completePriceValue = driverTools.getText(e);
            logger.log("Complete price value : " + completePriceValue);
            String[] splitCurrencyAndPrice = completePriceValue.split(" ");
            Assertion.assertThat("Verify price is in idr", splitCurrencyAndPrice[0], equalTo("IDR"));
        });
        driverTools.switchToNative();
        return true;
    }

    public void selectFirstBus(){
        driverTools.switchToWebViewUrl("bus-travel/search");
        WebElement firstBusScheduleContainer = driverTools.waitForElementToAppear(FIRST_BUS_SCHEDULE_CONTAINER,10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(firstBusScheduleContainer);
        driverTools.switchToNative();
    }

    private String getSrpDate(){
        WebElement srpHeaderInfoElement = driverTools.waitForElementToAppear(SRP_PASSENGER_AND_DATE_INFORMATION,10).orElseThrow();
        String srpHeaderInfoText = driverTools.getText(srpHeaderInfoElement);
        logger.log("srp header text : " + srpHeaderInfoText);
        Pattern pattern = Pattern.compile("^(.*?Jan|.*?Feb|.*?Mar|.*?Apr|.*?May|.*?Jun|.*?Jul|.*?Aug|.*?Agt|.*?Sep|.*?Oct|.*?Okt|.*?Nov|.*?Des|.*?Dec)");
        Matcher matcher = pattern.matcher(srpHeaderInfoText);
        matcher.find();
        return matcher.group(1);
    };

    public BusSrpOneWayData getFirstBusData(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("bus-travel/search");
        BusSrpOneWayData busSrpData = new BusSrpOneWayData();
        String srpDate = getSrpDate();
        WebElement busTitleElement = driverTools.elements(BUS_SCHEDULE_TITLE).orElseThrow().get(0);
        WebElement busClassElement = driverTools.elements(BUS_SCHEDULE_CLASS).orElseThrow().get(0);
        WebElement busDepartTimeElement = driverTools.elements(BUS_DEPARTURE_TIME).orElseThrow().get(0);

        String busOperatorName = driverTools.getText(busTitleElement);
        String busClass = driverTools.getText(busClassElement);
        String busDepartTime = driverTools.getText(busDepartTimeElement);
        String origin = "";
        String destination = "";

        if(driverTools.waitForElementToAppear(SRP_HEADER_ORIGIN_DESTINATION).isPresent()) {
            String rawText = driverTools.getText(SRP_HEADER_ORIGIN_DESTINATION).orElseThrow();
            String[] stringArray = rawText.contains(" ke ") ? rawText.split(" ke ") : rawText.split(" to ");
            origin += stringArray[0];
            destination += stringArray[1];
        } else {
            origin += driverTools.getText(driverTools.element(SRP_HEADER_ORIGIN).orElseThrow());
            destination += driverTools.getText(driverTools.elements(SRP_HEADER_DESTINATION).orElseThrow().get(0));
        }

        logger.log("SRP date : " + srpDate);
        //To add 0 if the date is only 1 digit
        if(srpDate.split(" ")[1].length() == 1){
            String[] textArray = srpDate.split(" ");
            textArray[1] = "0" + textArray[1];
            srpDate = String.join(" ",textArray);
            logger.log("SRP date : " + srpDate);
        }

        logger.log("Srp bus class : " + busClass);
        logger.log("Srp bus depart time : " + busDepartTime);
        logger.log("Srp bus operator : " + busOperatorName);
        logger.log("Srp bus origin : " + origin);
        logger.log("Srp bus destination : " + destination);
        logger.log("SRP date : " + srpDate);

        busSrpData.busClass = busClass;
        busSrpData.operatorName = busOperatorName;
        busSrpData.departDate = srpDate;
        busSrpData.origin = origin;
        busSrpData.departTime = busDepartTime;
        busSrpData.destination = destination;
        driverTools.switchToNative();
        return busSrpData;
    }

    public void skipCoachMark(){
        driverTools.switchToWebViewUrl("bus-travel/search");
        driverTools.waitForElementToAppear(SKIP_COACHMARK).ifPresent(driverTools::click);
        driverTools.switchToNative();
    }

    public void openFilterAndSort(){
        driverTools.switchToWebViewUrl("bus-travel/search");
        driverTools.click(FILTER_AND_SORT_COACHMARK);
        driverTools.switchToNative();
    }

    private Locator getLocatorForOperator(String[] operatorNames){
        StringBuilder locatorBuilder = new StringBuilder();
        locatorBuilder.append( "//section[@data-section='operator']//label//span[contains(@class,'sublabel_container') and (");
        for(int i=0;i<operatorNames.length;i++){
            if(i==operatorNames.length-1)
            locatorBuilder.append("span[contains(text(),'"+operatorNames[i]+"')]");
            else 
             locatorBuilder.append("span[contains(text(),'"+operatorNames[i]+"')] or ");
        }
        locatorBuilder.append(")]//span[1]");
        return new Locator("OPERATOR_NAME",AppiumBy.xpath(locatorBuilder.toString()));
    }

    public String selectFilterByOperator(String operatorNames){
        driverTools.switchToWebViewUrl("bus-travel/search");
        String[] operatorNameSplitted = operatorNames.split("\\|");
        logger.log("splitted name: " + operatorNameSplitted);
        driverTools.swipeToElement(BUS_OPERATOR_SECTION, Swipe.UP_LONG, 3).orElseThrow();
        Locator busOperatorFilter = getLocatorForOperator(operatorNameSplitted);
        String selectedOperator = driverTools.getText(busOperatorFilter).orElseThrow();
        driverTools.clickWithJavascriptExecutor(busOperatorFilter);
        driverTools.switchToNative();
        return selectedOperator;
    }

    public void applyFilter(){
        driverTools.switchToWebViewUrl("bus-travel/search");
        driverTools.click(APPLY_FILTER_BTN);
        driverTools.switchToNative();
    }

    public boolean verifyAllCardsAreFromSelectedOperator(String operatorName){
        driverTools.switchToWebViewUrl("bus-travel/search");
        driverTools.waitForElementToAppear(BUS_SCHEDULE_TITLE,10);
        List<WebElement> busCards = driverTools.elements(BUS_SCHEDULE_TITLE).orElseThrow();
        Assertion.assertThat("Verify srp is not empty", busCards.size(), greaterThan(0));
        busCards.forEach(e -> {
            String operatorNameValue = driverTools.getText(e);
            logger.log("Operator name value : " + operatorNameValue);
            Assertion.assertThat("Verify operator name is correct", operatorNameValue.toLowerCase(), equalTo(operatorName.toLowerCase()));
        });
        driverTools.switchToNative();
        return true;
    }

    public boolean isOnSrp(){
        driverTools.switchToWebViewUrl("bus-travel/search");
        boolean isChangeBtnDisplayedOnSRP = driverTools.isElementDisplayed(SRP_CHANGE_BTN);
        logger.log("is change btn displayed on srp : " + isChangeBtnDisplayedOnSRP);
        boolean isFilterBtnDisplayedOnSRP = driverTools.isElementDisplayed(FILTER_AND_SORT_COACHMARK);
        logger.log("is filter btn displayed on srp : " + isFilterBtnDisplayedOnSRP);
        driverTools.switchToNative();
        return isChangeBtnDisplayedOnSRP && isFilterBtnDisplayedOnSRP;
    }

    private Locator getSRPOrignAndDestinationHeaderLocator(String location){
        return new Locator("SRP_HEADER_ORIGIN_OR_DESTINATION",AppiumBy.xpath("//*[contains(@class,'SRPHeader') and contains(text(),'"+location+"')]"));
    }

    private Locator getSRPDateHeaderLocator(String dateEn,String dateId){
        return new Locator("SRP_HEADER_DATE",AppiumBy.xpath("//*[contains(@class,'SRPHeader_schedule_passenger_count') and (contains(text(),'"+dateEn+"') or contains(text(),'"+dateId+"'))]"));
    }

    public boolean verifySearchedDetail(String origin, String destination, String date){
        driverTools.switchToWebViewUrl("bus-travel/search");
        String[] originSplit = origin.split(" ");
        boolean isOriginDisplayed = driverTools.isElementDisplayed(getSRPOrignAndDestinationHeaderLocator(originSplit[originSplit.length-1]));
        logger.log("is origin displayed on srp header : " + isOriginDisplayed);

        String[] destinationSplit = destination.split(" ");
        boolean isDestinationDisplayed = driverTools.isElementDisplayed(getSRPOrignAndDestinationHeaderLocator(destinationSplit[destinationSplit.length-1]));
        logger.log("is destination displayed on srp header : " + isDestinationDisplayed);

        String[] dateArr = date.split(" ");
        String day = dateArr[0];
        String month = dateArr[1];
        String monthEng = Optional.ofNullable(CoreFunctions.monthIdToEng(month)).orElse(month);
        String monthId = Optional.ofNullable(CoreFunctions.monthEngToId(monthEng)).orElse(monthEng);
        // in bahasa the August is termed as "Agustus" but in short form written as "Agt"
        String monthIdShort = monthId.substring(0,3).replace("Agu","Agt");

        boolean isDateDisplayed = driverTools.isElementDisplayed(getSRPDateHeaderLocator(day + " " + monthEng.substring(0,3), day + " " + monthIdShort));
        logger.log("is date displayed on srp header : " + isDateDisplayed);
        boolean isScheduleListCardDisplayed = driverTools.isElementDisplayed(SCHEDULE_LIST_CARD);
        logger.log("is schedule list card displayed on srp header : " + isScheduleListCardDisplayed);
        driverTools.switchToNative();
        return isOriginDisplayed && isDestinationDisplayed && isDateDisplayed && isScheduleListCardDisplayed;
    }

    public boolean verifyOriginAndDestinationCrossSell(String originTrain, String destination) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("bus-travel/search?");
        driverTools.waitForElementToAppear(SRP_HEADER_PATH,30).orElseThrow();
        Optional<String> pathOptional = Optional.ofNullable(driverTools.getText(SRP_HEADER_PATH)).orElseThrow();
        String[] pathSegments = pathOptional.map(pathText -> pathText.split(" to | ke "))
                .orElseThrow(() -> new RuntimeException("Invalid path format"));
        String originBus = pathSegments.length > 0 ? pathSegments[0] : "";
        String destinationBus = pathSegments.length > 1 ? pathSegments[1] : "";
        logger.log("originBus: " + originBus);
        logger.log("destinationBus: " + destinationBus);
        driverTools.switchToNative();
        return Objects.equals(originBus, originTrain) && Objects.equals(destinationBus, destination);
    }

    public boolean verifyOriginAndDestinationCrossSell(TrainAndWhooshGeneralData trainAndWhooshGeneralData) {
        String departDateEnExpected = trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_En;
        String departDateIdExpected = trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_Id;
        String originCity = trainAndWhooshGeneralData.originCityName.toLowerCase();
        String destinationCity = trainAndWhooshGeneralData.destinationCityName.toLowerCase();

        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("bus-travel/search?",2);
        driverTools.waitForWebToLoad();

        driverTools.waitForElementToAppear(SRP_HEADER_PATH,30).orElseThrow();
        WebElement routeInfoElement = driverTools.waitForElementToAppear(SRP_HEADER_PATH).orElseThrow();
        String routeInfoText = driverTools.getText(routeInfoElement);
        logger.log("Route info text: " + routeInfoText);
        String[] routeParts = CoreFunctions.getOriginAndDestinationFromRouteInfo(routeInfoText);
        String originInApp = routeParts[0];
        String destinationInApp = routeParts[1];
        boolean isRouteInfoCorrect = originInApp.contains(originCity) && destinationInApp.contains(destinationCity);
        logger.log("is route info correct: " + isRouteInfoCorrect + " (Expected: " + originCity + " to " + destinationCity + ", Actual: " + originInApp + " to " + destinationInApp + ")");
        Assertion.assertThat("Origin station should match", originInApp,containsString(originCity));
        Assertion.assertThat("Destination station should match", destinationInApp,containsString(destinationCity));

        Locator dateLocator = getSRPDateHeaderLocator(departDateEnExpected, departDateIdExpected);
        WebElement dateElement = driverTools.waitForElementToAppear(dateLocator).orElseThrow();
        String actualDateAndPaxInfo = driverTools.getText(dateElement);
        boolean isDateCorrect = actualDateAndPaxInfo.contains(departDateEnExpected) || actualDateAndPaxInfo.contains(departDateIdExpected);
        logger.log("is date correct: " + isDateCorrect + " (Expected: " + departDateEnExpected + " or " + departDateIdExpected + ", Actual: " + actualDateAndPaxInfo + ")");
        Assertion.assertThat("Date should match", actualDateAndPaxInfo, anyOf(containsString(departDateEnExpected), containsString(departDateIdExpected)));

        int totalPaxInApp = getNumberOfPassengers(actualDateAndPaxInfo);
        boolean isPaxInfoCorrect = totalPaxInApp == trainAndWhooshGeneralData.numberOfPassenger;
        logger.log("is pax info correct: " + isPaxInfoCorrect + " (Expected: " + trainAndWhooshGeneralData.numberOfPassenger + ", Actual: " + totalPaxInApp + ")");
        Assertion.assertThat("Total number of passengers should match", totalPaxInApp, is(trainAndWhooshGeneralData.numberOfPassenger));

        driverTools.switchToNative();
        return isRouteInfoCorrect && isDateCorrect && isPaxInfoCorrect;
    }

    private int getNumberOfPassengers(String paxInfoText) {
        // Match digits followed by known passenger-related words (case-insensitive)
        Matcher matcher = Pattern.compile("(\\d+)\\s*(Passenger|Penumpang|Kursi|Seat)", Pattern.CASE_INSENSITIVE).matcher(paxInfoText);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        throw new IllegalArgumentException("No passenger count found in text: " + paxInfoText);
    }

    public void changePaxViaDeeplink(int passenger){
        driverTools.switchToWebView();
        String currentUrl = driver.getCurrentUrl();
        //adult=passenger
        String newUrl = CoreFunctions.updateQueryParameter(currentUrl, "adult", String.valueOf(passenger));
        logger.log("Change pax via deeplink to: " + newUrl);
        driver.get(newUrl);

        String newUrlAfterLoad = driver.getCurrentUrl();
        logger.log("New URL after load: " + newUrlAfterLoad);
        String adultValue = CoreFunctions.getQueryParameterValue(newUrlAfterLoad, "adult");
        Assertion.assertThat("url contains updated pax", adultValue, is(String.valueOf(passenger)));
        driverTools.switchToNative();
    }

    public void saveResultAfterDeeplinkPaxChange(){
        driverTools.switchToWebView();
        driverTools.waitForElementToPresence(SRP_HEADER_CHANGE_BTN);
        driverTools.click(SRP_HEADER_CHANGE_BTN);
        driverTools.waitForElementToPresence(SRP_HEADER_CHANGE_SEARCH_BTN);
        driverTools.click(SRP_HEADER_CHANGE_SEARCH_BTN);
        driverTools.waitForElementToDisappear(SRP_HEADER_CHANGE_SEARCH_BTN);
        driverTools.switchToNative();
    }

    public void clickBackToBusSearchPage(){
        driverTools.switchToWebView();
        driverTools.clickWithJavascriptExecutor(SRP_HEADER_BACK_BTN);
        driverTools.switchToNative();
    }
}
