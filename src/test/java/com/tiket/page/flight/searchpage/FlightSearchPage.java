package com.tiket.page.flight.searchpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.flight.searchresultpage.FlightSearchResultPage;
import com.tiket.pojo.flight.FlightDynamicData;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public abstract class FlightSearchPage<T extends WebDriver> extends BasePage<T> {

    public FlightSearchPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightSearchPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new FlightSearchPageAndroid<>(session);}
            case IOS -> {return new FlightSearchPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void openFlightDeeplink();
    public abstract void backToHomePage();
    public abstract void selectOrigin(String origin);
    public abstract void selectOriginViaAirPortCode(String origin);
    public abstract void selectDestination(String destination);
    public abstract void selectDestinationViaAirPortCode(String destination);
    public abstract void toggleTriptype(String trip); // FIXME: Implementation pending
    public abstract void selectDate(String date); // FIXME: Implementation pending
    public abstract void selectToday(String date);
    public abstract void selectDate(String departureDate, String returnDate); //FIXME: Implementation pending IOS
    public abstract void addPassenger(Integer adult, Integer child, Integer infant);
    public abstract void searchFlight();
    public abstract void selectSelfTransferFilter();
    public abstract boolean isLowestPriceGuaranteePagePresent();
    public abstract void closePopup();
    public abstract void clearRecentSearch();
    public abstract void clickOnRecentSearch();
    public abstract boolean checkLPGBanner();
    public abstract void clickLPGBanner();
    public abstract void verifySelectedDate(String dateSelectedDepart,String dateSelectedReturn,String lang);
    public abstract void verifySelectedDate(String dateSelectedDepart,String lang);
    public abstract String  CheckSearchAirportResultsAlwaysOnTop(String location);
    public abstract void selectFlightClass(String flightClass);
    public abstract boolean isOnFlightLandingPage();
    public abstract void deeplinkWithSelectedDateSrp(String origin,String destination,String originCityName,String destinationCityName,int daysToDepartureDate,int adult,int child,int infant);
    public abstract void deeplinkWithSelectedDateSrpRoundTrip(String origin,String destination,String originCityName,String destinationCityName,int daysToDepartureDate,int duration,int adult,int child,int infant);

    /**
     * Opens the multi-city flight SRP via deeplink, on both Android and iOS.
     *
     * @param routes               routes in {@code ORIGIN-DESTINATION} airport-code format, in travel order,
     *                             e.g. {@code ["CGK-SUB", "SUB-DPS", "DPS-CGK"]}
     * @param daysToDepartureDates departure date of each route as days from today, same order and size as {@code routes},
     *                             e.g. {@code [3, 6, 9]}
     * @param cabinClass           e.g. {@code economy}
     */
    public void deeplinkMultiCitySrp(List<String> routes, List<Integer> daysToDepartureDates, int adult, int child, int infant, String cabinClass) {
        if (routes == null || routes.isEmpty() || daysToDepartureDates == null || routes.size() != daysToDepartureDates.size()) {
            throw new IllegalArgumentException("Multi-city routes and departure dates must be non-empty and the same size, but were: "
                    + routes + " / " + daysToDepartureDates);
        }
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        StringBuilder url = new StringBuilder("/en-sg/flights/multi-city/search?");
        for (int i = 0; i < routes.size(); i++) {
            String[] airports = routes.get(i).trim().split("\\s*-\\s*");
            if (airports.length != 2) {
                throw new IllegalArgumentException("Multi-city route must be in ORIGIN-DESTINATION format, but was: " + routes.get(i));
            }
            int segment = i + 1;
            String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDates.get(i));
            url.append("d").append(segment).append("=").append(airports[0].toUpperCase())
                    .append("&a").append(segment).append("=").append(airports[1].toUpperCase())
                    .append("&date").append(segment).append("=").append(departureDate)
                    .append("&");
        }
        url.append("adult=").append(adult)
                .append("&child=").append(child)
                .append("&infant=").append(infant)
                .append("&class=").append(cabinClass.toLowerCase());

        Deeplink multiCitySrp = new Deeplink("MultiCitySrp",
                "tiket://gatotkaca.tiket.com" + url,
                "tiket://preprod.tiket.com" + url,
                "tiket://www.tiket.com" + url);
        driverTools.deeplink(multiCitySrp);
        // deeplink() does not restore native context; subsequent native locator waits need it explicitly.
        driverTools.switchToNative();
    }

    /**
     * Performs a flight search based on the specified trip type.
     * <p>
     * This method is overloaded so that for one‑way trips no duration is needed,
     * while round‑trip searches require a duration value indicating the number of days between the departure and return flights.
     *
     * @param daysToDepartureDate  The number of days from today until the departure date.
     * @param tripType         The type of trip ("oneway" or "roundtrip").
     */
    public void changeDateViaDeeplink(int daysToDepartureDate,String tripType) {
        // For one‑way, simply call the combined method with a null duration.
        changeDateViaDeeplink(daysToDepartureDate, null,tripType);
    }

    /**
     * Performs a flight search deeplink using the actual dates resolved in {@link FlightDynamicData}
     * (as returned by {@code FlightApi.findDate}/{@code findDateRT}) instead of the hard-coded
     * {@code today + daysToDepartureDate} offset.
     * <p>
     * Derives {@code daysToDepartureDate} and {@code duration} from
     * {@link FlightDynamicData#departureDate} and {@link FlightDynamicData#returnDate}
     * (both in {@code yyyy-MM-dd} format) and delegates to
     * {@link #changeDateViaDeeplink(int, Integer, String)} so no platform-specific code changes
     * are required.
     *
     * @param flightDynamicData dynamic data carrying the real departure/return dates.
     * @param tripType          The type of trip ("oneway" or "roundtrip").
     */
    public void changeDateViaDeeplink(FlightDynamicData flightDynamicData, String tripType) {
        logger.log("Resolving daysToDepartureDate and duration from FlightDynamicData for trip type: " + tripType);
        logger.log("FlightDynamicData - Departure Date: " + flightDynamicData.departureDate);
        LocalDate today = LocalDate.now();
        LocalDate depDate = LocalDate.parse(flightDynamicData.departureDate); // YYYY-MM-DD
        int daysToDepartureDate = (int) ChronoUnit.DAYS.between(today, depDate);

        Integer duration = null;
        if (flightDynamicData.returnDate != null && !flightDynamicData.returnDate.isBlank()) {
            logger.log("FlightDynamicData - Return Date: " + flightDynamicData.returnDate);
            LocalDate retDate = LocalDate.parse(flightDynamicData.returnDate);
            duration = (int) ChronoUnit.DAYS.between(depDate, retDate);
        }
        changeDateViaDeeplink(daysToDepartureDate, duration, tripType);
    }

    /**
     * Performs a flight search based on the specified trip type.
     * <p>
     * For a one‑way trip, the duration parameter is ignored.
     * For a round‑trip, a valid duration must be provided.
     *
     * @param daysToDepartureDate  The number of days from today until the departure date.
     * @param duration         The number of days between departure and return (required for round‑trip searches).
     * @param tripType         The type of trip ("oneway" or "roundtrip").
     * @throws IllegalArgumentException if a round‑trip search is requested but no duration is provided.
     */

    public void changeDateViaDeeplink(int daysToDepartureDate,Integer duration,String tripType){
        String methodName = "changeDateViaDeeplink";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            if(Configs.PLATFORM == Platform.ANDROID)
                return;

            FlightSearchPage<T> flightSearchPage = FlightSearchPage.getInstance(session);
            FlightSearchResultPage<T> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
            if (tripType.toLowerCase().contains("oneway")) {
                // Execute the one‑way deep link; duration is not used.
                flightSearchPage.deeplinkWithSelectedDateSrp(
                        "CGK", "DPS", "Jakarta", "Denpasar-Bali",
                        daysToDepartureDate, 1, 0, 0
                );
            } else {
                // For round‑trip, ensure duration is provided.
                if (duration == null) {
                    throw new IllegalArgumentException("Duration must be provided for round-trip searches.");
                }
                // Execute the round‑trip deep link, which includes the duration parameter.
                flightSearchPage.deeplinkWithSelectedDateSrpRoundTrip(
                        "CGK", "DPS", "Jakarta", "Denpasar-Bali",
                        daysToDepartureDate, duration, 1, 0, 0
                );
            }
            // Wait for the search results page to load.
            flightSearchResultPage.waitForPageLoad();
            // Navigate back to the main flight search page.
            flightSearchResultPage.backToFlightPage();
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    //WebView
    private static final Locator DEPART_CALENDAR_WEBVIEW = new Locator("DEPART_CALENDAR_WEBVIEW",AppiumBy.xpath("//*[contains(@class,'SearchForm_date_selector')]"));
    private static final Locator SELECTED_DATE_DEAPRT_WEBVIEW = new Locator("SELECTED_DATE_DEAPRT_WEBVIEW",AppiumBy.xpath("(//*[contains(@class,'DatePicker_confirmation_date')]//h3[contains(@class,'Text_text')])[1]"));
    private static final Locator SAVE_BUTTON_CALENDAR = new Locator("SAVE_BUTTON_CALENDAR",AppiumBy.xpath("//*[contains(@class,'CalendarMobile_confirmation_section')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator SAVE_BUTTON_WEBVIEW = new Locator("SAVE_BUTTON_WEBVIEW",AppiumBy.xpath("//button[contains(@class,'SearchForm_btn_submit')]"));

    public void selectDateWebView(String departureDate) {
        driverTools.switchToWebViewUrl("pesawat");
        driverTools.click(DEPART_CALENDAR_WEBVIEW);
        driverTools.switchToNative();

        logger.log("Departing on: "+ departureDate);
        clickDate(departureDate);

        driverTools.switchToWebViewUrl("pesawat");
        driverTools.click(SAVE_BUTTON_CALENDAR);
        driverTools.switchToNative();
    }

    private Swipe getDirection(String dateToSelect){
        driverTools.switchToWebViewUrl("pesawat");
        WebElement selectedDateElement = driverTools.waitForElementToAppear(SELECTED_DATE_DEAPRT_WEBVIEW).orElseThrow();
        String selectedDate = selectedDateElement.getText();
        driverTools.switchToNative();

        logger.log("date to select : " + dateToSelect);
        logger.log("selected date : " + selectedDate);

        String[] dateToSelectArr = dateToSelect.split(" ");
        int dateToSelectDay = Integer.parseInt(dateToSelectArr[0]);
        String dateToSelectMonth = dateToSelectArr[1];
        int dateToSelectYear = Integer.parseInt(dateToSelectArr[2]);

        String[] selectedDateArr = selectedDate.split(" ");
        int selectedDateDay = Integer.parseInt(selectedDateArr[0]);
        String selectedDateMonth = selectedDateArr[1];
        int SelectedDateYear = Integer.parseInt(selectedDateArr[2]);

        int selectedMonthNum = CoreFunctions.monthToMonthNumber(selectedDateMonth);
        logger.log("selected month number: " + selectedMonthNum);
        int dateToSelectMonthNum = CoreFunctions.monthToMonthNumber(dateToSelectMonth);
        logger.log("date to select month number: " + dateToSelectMonthNum);

        int yearDif = dateToSelectYear - SelectedDateYear;
        if(yearDif == 0){
            int monthDif = dateToSelectMonthNum - selectedMonthNum;
            if(monthDif == 0){
                return dateToSelectDay - selectedDateDay < 0 ? Swipe.DOWN: Swipe.UP;
            }
            return monthDif < 0 ? Swipe.DOWN : Swipe.UP;
        }
        return yearDif < 0 ? Swipe.DOWN : Swipe.UP;
    }

    private void clickDate(String date){
        Swipe direction = getDirection(date);
        logger.log("direction : " + direction);

        String year = CoreFunctions.getSectionOfString(date," ", 2);
        String monthEng = CoreFunctions.getSectionOfString(date," ", 1);
        String monthId = CoreFunctions.monthEngToId(monthEng);
        String day= CoreFunctions.getSectionOfString(date," ", 0);
        Locator dateLocator = getDateLocator(year,monthId,monthEng,day);

        driverTools.switchToWebViewUrl("pesawat");
        for(int i = 10; i > 0; i--){
            if(driverTools.waitForElementToAppear(dateLocator,1).isPresent()){
                driverTools.clickWithJavascriptExecutor(dateLocator);
                break;
            }
            if(direction == Swipe.UP)
                driverTools.swipeUp();
            else
                driverTools.swipeDown();
        }
        driverTools.switchToNative();
    }

    private Locator getDateLocator(String year,String monthId, String monthEng, String day){
        String monthYearEn = monthEng + " " + year;
        String monthYearId = monthId + " " + year;
        return new Locator("Date Locator", AppiumBy.xpath("//*[contains(@class,'month_heading') and (contains(text(),'"+monthYearEn+"') or contains(text(),'"+monthYearId+"'))]//following-sibling::*//*[contains(@class,'day')]//*[text()='"+day+"']"));
    }

    public void searchFlightWebView(){
        driverTools.switchToWebViewUrl("pesawat");
        driverTools.click(SAVE_BUTTON_WEBVIEW);
        driverTools.switchToNative();
    }
}
