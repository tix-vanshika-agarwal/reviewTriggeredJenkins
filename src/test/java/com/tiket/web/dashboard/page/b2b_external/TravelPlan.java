package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static org.hamcrest.Matchers.is;

public class TravelPlan<T extends WebDriver> extends BasePage<T> {
    private static final Locator CREATE_TRAVEL_PLAN =  new Locator("CREATE_TRAVEL_PLAN", new By.ByXPath("//button//span[contains(text(), 'Create new')]"));
    private static final Locator TRAVEL_PLAN_HEADER =  new Locator("TRAVEL_PLAN_HEADER", new By.ByXPath("//*[contains(text(), 'Create Travel Plan')]"));
    private static final Locator TRAVEL_PLAN =  new Locator("TRAVEL_PLAN", new By.ByXPath("//input[contains(@id, 'travel-plan')]"));
    private static final Locator TRAVEL_PRODUCT =  new Locator("TRAVEL_PRODUCT", new By.ByXPath("//input[contains(@id, 'add-product')]"));
    private static final Locator SAVE_PRODUCT=  new Locator("SAVE_PRODUCT", new By.ByXPath("//*[text()='Save']"));
    private static final Locator CHOOSE_DESTINATION=  new Locator("CHOOSE_DESTINATION", new By.ByXPath("//input[@placeholder='Select rental location']"));
    private static final Locator SUGGESTED_DESTINATION=  new Locator("SUGGESTED_DESTINATION", new By.ByXPath("//div[contains(@class, 'AutoCompleteSearch_list')]//div"));
    private static final Locator CHOOSE_DURATION=  new Locator("CHOOSE_DURATION", new By.ByXPath("//*[contains(text(), 'Choose')]"));
    private static final Locator SELECT_DURATION=new Locator("SELECT_DURATION", new By.ByXPath("//*[contains(text(), '1 day')]"));
    private static final Locator APPROVER_DROPDOWN=new Locator("APPROVER_DROPDOWN", new By.ByXPath("//input[contains(@id, 'select-approver')]"));
    private static final Locator SELECT_APPROVER=new Locator("SELECT_APPROVER", new By.ByXPath("(//label[contains(@class,'Check_check')])[1]"));
    private static final Locator REQUEST_APPROVAL=new Locator("REQUEST_APPROVAL", new By.ByXPath("//*[text()='Request approval']"));
    private static final Locator SUBMIT_APPROVAL=new Locator("SUBMIT_APPROVAL", new By.ByXPath("//*[contains(text(), 'Submit request')]"));
    private static final Locator START=new Locator("START", new By.ByXPath("//*[contains(text(), 'Start date')]"));
    private static final Locator END=new Locator("END", new By.ByXPath("//*[contains(text(), 'End date')]"));
    private static final Locator INPUT_ARRIVAL=new Locator("INPUT_ARRIVAL", new By.ByXPath("//input[contains(@placeholder, 'Going')]"));
    private static final Locator INPUT_DEPARTURE=new Locator("INPUT_DEPARTURE", new By.ByXPath("//input[contains(@placeholder, 'Select city')]"));
    private static final Locator SEARCH_BAR=new Locator("SEARCH_BAR", new By.ByXPath("//input[@id='search-by-keyword']"));
    private static final Locator SEARCH_BUTTON=new Locator("SEARCH_BUTTON", new By.ByXPath("//button//span[contains(text(), 'Search')]"));
    private static final Locator OPEN_BUTTON=new Locator("OPEN_BUTTON", new By.ByXPath("//button//span[contains(text(), 'Open')]"));
    private static final Locator CANCEL_BUTTON=new Locator("CANCEL_BUTTON", new By.ByXPath("//button//span[contains(text(), 'Cancel')]"));
    private static final Locator POPUP_CANCEL_BUTTON=new Locator("POPUP_CANCEL_BUTTON", new By.ByXPath("//section[contains(@class, 'popup')]//button//span[contains(text(), 'Cancel')]"));
    private static final Locator TRAVEL_PLAN_REMOVE=new Locator("TRAVEL_PLAN_REMOVE", new By.ByXPath("//button//span[contains(text(), 'Remove')]"));
    private static final Locator ADD_ANOTHER=new Locator("ADD_ANOTHER", new By.ByXPath("//button[contains(., 'Add another')]"));
    private static final Locator NAVIGATE_DATE=new Locator("NAVIGATE_DATE", new By.ByXPath("//button[contains(@class, 'Navigation_next_button__')]"));
    private static final Locator TRAVEL_PLAN_BUTTON =  new Locator("TRAVEL_PLAN_BUTTON", new By.ByXPath("//button[.//span[text()='Travel Plan']]"));
    private static final Locator YOUR_TRAVEL_PLAN =  new Locator("YOUR_TRAVEL_PLAN", new By.ByXPath("//*[contains(text(), 'Your Travel Plan')]"));
    private static final Locator START_DATE_BOX = new Locator("START_DATE_BOX", new By.ByXPath("//input[@id='start-date' and @placeholder='Start date']"));
    private static final Locator END_DATE_BOX = new Locator("END_DATE_BOX", new By.ByXPath("//input[@id='end-date' and @placeholder='End date']"));
    private static final Locator DATE_BOX = new Locator("DATE_BOX", new By.ByXPath("//div[contains(@class, 'cursor-pointer') and contains(@class, 'font-bold')]"));
    private static final Locator TRAVEL_PLAN_DROPDOWN_BUTTON = new Locator("TRAVEL_PLAN_DROPDOWN_BUTTON", By.xpath("(//button[contains(@class, 'styles_clickable_icon__KOGhB')])[4]"));
    private static final Locator CHECKBOX_FOR_JAKARTA = new Locator("CHECKBOX_FOR_JAKARTA", By.xpath("//span[text()='jakarta']/preceding::span[contains(@class, 'Check_custom_checkbox')][1]"));
    private static final Locator SELECT_CITY = new Locator("SELECT_CITY", By.xpath("//label[text()='Select city']"));
    private static final Locator SELECT_CITY_FIELD = new Locator("SELECT_CITY_FIELD", By.xpath("//span[contains(@class, 'Text_text') and normalize-space(text())='Select city']"));
    private static final Locator CUSTOM_FIELD_VALUE = new Locator("CUSTOM_FIELD_VALUE", By.xpath("//div[contains(@class,'whitespace-nowrap')]//span[contains(text(),'jakarta')]"));
    private static final Locator CUSTOM_FIELD_NAME = new Locator("CUSTOM_FIELD_NAME", By.xpath("(//div[contains(@class,'mb-1')])[2]"));
    private static final Locator EDIT_BUTTON=new Locator("EDIT_BUTTON", new By.ByXPath("//button//span[contains(text(), 'Edit')]"));
    private static final Locator SAVE_CHANGES_BUTTON=new Locator("SAVE_CHANGES_BUTTON", new By.ByXPath("//button//span[contains(text(), 'Save changes')]"));
    private static final Locator PRODUCT_TYPE=new Locator("PRODUCT_TYPE", new By.ByXPath("//div[contains(@class,'mb-5')]//span[contains(@class,'ml-2')]"));

    private static final Locator ONEWAY_ROUND_TRIP_TAB = new Locator("ONEWAY_ROUND_TRIP_TAB", By.cssSelector("[role='tab'][title='Oneway/Round-trip']"));
    private static final Locator MULTI_CITY_TAB = new Locator("MULTI_CITY_TAB", By.cssSelector("[role='tab'][title='Multi-city']"));

    private static final Locator ADD_ANOTHER_ROUTE = new Locator("ADD_ANOTHER_ROUTE", By.cssSelector("button[class*='MultiCityFlightRoutes_add_route_button']"));

    private static final Locator SET_EXCEPTIONS = new Locator("SET_EXCEPTIONS", By.xpath("//button[.//span[normalize-space(text())='Set exceptions']]"));

    private static final Locator ADD_FLIGHT_EXCEPTION_HEADER = new Locator("ADD_FLIGHT_EXCEPTION_HEADER", By.xpath("//h2[normalize-space(text())='Add flight exception']"));

    private static final Locator ADD_ACCOM_EXCEPTION_HEADER = new Locator("ADD_ACCOM_EXCEPTION_HEADER", By.xpath("//h2[normalize-space(text())='Add accommodation exception']"));
    private static final Locator HOTEL_STAR_DROPDOWN = new Locator("HOTEL_STAR_DROPDOWN", By.xpath("//div[contains(@class,'cursor-pointer')][.//span[normalize-space(text())='Hotel star']]"));

    private static final Locator CHECK_DETAILS = new Locator("CHECK_DETAILS", By.xpath("//button[.//span[normalize-space(text())='Check details']]"));
    private static final Locator EXCEPTION_POPUP_CLOSE = new Locator("EXCEPTION_POPUP_CLOSE", By.xpath("//section[.//h2[normalize-space(text())='Travel plan exception']]//button[@data-testid='modal-close-button']"));

    private static final Locator AIRLINES_DROPDOWN = new Locator("AIRLINES_DROPDOWN", By.xpath("(//div[contains(@class,'min-w-[24px]')])[1]"));
    private static final Locator UNSELECT_ALL = new Locator("UNSELECT_ALL", By.xpath("//button[normalize-space()='Unselect all']"));
    private static final Locator AIRLINE_SEARCH = new Locator("AIRLINE_SEARCH", By.xpath("//input[@placeholder='Search']"));
    private static final Locator AIRLINE_CHECKBOX = new Locator("AIRLINE_CHECKBOX", By.xpath("(//div[contains(@class,'max-h-60')]//input[@type='checkbox'])[1]"));
    private static final Locator SAVE_AIRLINES = new Locator("SAVE_AIRLINES", By.xpath("(//button[normalize-space(.)='Save'])[1]"));

    private static final Locator CABIN_CLASS_DROPDOWN = new Locator("CABIN_CLASS_DROPDOWN", By.xpath("//div[contains(@class,'cursor-pointer')][.//span[normalize-space(text())='Cabin class']]"));
    private static final Locator SAVE_CANBIN = new Locator("SAVE_CANBIN", By.xpath("(//button[normalize-space(.)='Save'])[2]"));
    private static final Locator MAX_BUDGET = new Locator("MAX_BUDGET", By.xpath("//input[contains(@placeholder, 'Max. budget per')]"));
    private static final Locator EXCEPTION_ADDED_SNACKBAR = new Locator("EXCEPTION_ADDED_SNACKBAR", By.xpath("(//div[contains(@class,'Snackbar_text_content') and normalize-space(text())='Exception added successfully!'])[1]"));
    private static final Locator EXCEPTION_BUDGET = new Locator("EXCEPTION_BUDGET", By.xpath("//p[contains(.,'exceptions with an estimated budget of')]/span[contains(text(),'IDR')]"));
    private static final Locator TRAVEL_PLAN_DELETED_SNACKBAR = new Locator("TRAVEL_PLAN_DELETED_SNACKBAR", By.xpath("//div[contains(@class,'Snackbar_text_content') and contains(text(),'deleted your travel plan request')]"));
    private static final Locator SAVE_EXCEPTION =new Locator("SAVE_EXCEPTION", By.xpath("//button[normalize-space(.)='Save exception']"));

    private static final Locator ENABLED_ROUTE_DATE = new Locator("ENABLED_ROUTE_DATE", By.xpath("//button[not(@disabled)][.//span[@aria-label]]"));

    private String startDate;
    private String endDate;


    public TravelPlan(Session<T> session) {
        super(session);
    }

    public void clickCreateTravel(){
        driverTools.waitForElementToAppear(CREATE_TRAVEL_PLAN);
        Assertion.assertThat("Create New Travel plan Button is visible",driverTools.isElementDisplayed(CREATE_TRAVEL_PLAN),is(true));
        driverTools.click(CREATE_TRAVEL_PLAN);
    }

    public boolean isCreateTravelPageOpened(){
        driverTools.waitForElementToAppear(TRAVEL_PLAN_HEADER);
        return driverTools.isElementDisplayed(TRAVEL_PLAN_HEADER);
    }

    private Locator getSearchProductLocator(String product){
        return new Locator("SELECT_TRAVEL_PRODUCT", new By.ByXPath("//*[text()='"+product+"']"));
    }

    public void CreateTravelPlan(String name, String dateRange, String product, String location, String departure) throws InterruptedException {

        driverTools.waitForElementToAppear(TRAVEL_PLAN);

        Assertion.assertThat("Create Travel Plan Input Box is visible",driverTools.isElementDisplayed(TRAVEL_PLAN),is(true));

        driverTools.sendKeys(TRAVEL_PLAN, name);

        int[] randomDates = generateRandomDateRange(dateRange);
        int startDate = randomDates[0];
        int endDate = randomDates[1];

        selectDate(START, startDate);
        selectDate(END, endDate);

        driverTools.waitForElementToAppear(TRAVEL_PRODUCT, 5);
        driverTools.click(TRAVEL_PRODUCT);

        Locator locator = getSearchProductLocator(product);
        driverTools.waitForElementToAppear(locator, 5);

        Assertion.assertThat("Verify that Travel Product Dropdowns are visible",driverTools.isElementDisplayed(locator),is(true));

        driverTools.clickWithJavascriptExecutor(locator);
        driverTools.clickWithJavascriptExecutor(SAVE_PRODUCT);

        if (departure==null || departure.isEmpty()){
            if (product.equalsIgnoreCase("Accommodations")) {
                enterLocationWithSuggestion(INPUT_ARRIVAL, location);
            } else if (product.equalsIgnoreCase("Car Rentals")) {
                enterLocationWithSuggestion(CHOOSE_DESTINATION, location);
            }
        }
        else {
            if (product.equalsIgnoreCase("Trains") || product.equalsIgnoreCase("Flights")) {
                enterLocationWithSuggestion(INPUT_ARRIVAL, location);
                enterLocationWithSuggestion(INPUT_DEPARTURE, departure);
            }
        }

        if (product.equalsIgnoreCase("Car Rentals")) {
            driverTools.click(CHOOSE_DURATION);
            driverTools.click(SELECT_DURATION);
        }
    }

    public boolean isTripTypeTabDisplayed(String tab) {
        Locator locator = tab.equalsIgnoreCase("Multi-city") ? MULTI_CITY_TAB : ONEWAY_ROUND_TRIP_TAB;
        driverTools.waitForElementToAppear(locator, 5);
        return driverTools.isElementDisplayed(locator);
    }

    public void selectMultiCity() {
        driverTools.waitForElementToAppear(MULTI_CITY_TAB, 5);
        driverTools.click(MULTI_CITY_TAB);
    }

    private Locator getRouteInputLocator(int route, String placeholder) {
        return new Locator("ROUTE_" + route + "_INPUT", By.xpath("(//input[@placeholder='" + placeholder + "'])[" + route + "]"));
    }

    public void clickSetExceptions() {
        driverTools.waitForElementToAppear(SET_EXCEPTIONS, 5);
        driverTools.clickWithJavascriptExecutor(SET_EXCEPTIONS);
    }

    public boolean isFlightExceptionDialogOpened() {
        driverTools.waitForElementToAppear(ADD_FLIGHT_EXCEPTION_HEADER, 5);
        return driverTools.isElementDisplayed(ADD_FLIGHT_EXCEPTION_HEADER);
    }

    public boolean isAccomExceptionDialogOpened() {
        driverTools.waitForElementToAppear(ADD_ACCOM_EXCEPTION_HEADER, 5);
        return driverTools.isElementDisplayed(ADD_ACCOM_EXCEPTION_HEADER);
    }

    public void selectExceptionHotelStar(String starRating) {
        driverTools.waitForElementToAppear(HOTEL_STAR_DROPDOWN, 5);
        driverTools.click(HOTEL_STAR_DROPDOWN);

        driverTools.waitForElementToAppear(AIRLINE_SEARCH, 5);
        driverTools.sendKeys(AIRLINE_SEARCH, starRating);

        Locator starOption = new Locator("STAR_OPTION", By.xpath("//div[contains(@class,'cursor-pointer')][.//span[normalize-space(.)='" + starRating + "-star']]"));
        driverTools.waitForElementToAppear(starOption, 5);
        driverTools.clickWithJavascriptExecutor(starOption);

        driverTools.click(SAVE_AIRLINES);
    }

    public void selectExceptionAirline(String airline) {
        driverTools.waitForElementToAppear(AIRLINES_DROPDOWN, 5);
        driverTools.click(AIRLINES_DROPDOWN);

        driverTools.waitForElementToAppear(UNSELECT_ALL, 5);
        driverTools.click(UNSELECT_ALL);

        driverTools.waitForElementToAppear(AIRLINE_SEARCH, 5);
        driverTools.sendKeys(AIRLINE_SEARCH, airline);

        driverTools.waitForElementToAppear(AIRLINE_CHECKBOX, 5);
        driverTools.clickWithJavascriptExecutor(AIRLINE_CHECKBOX);

        driverTools.click(SAVE_AIRLINES);
    }

    public void selectExceptionCabin(String cabin) {
        driverTools.waitForElementToAppear(CABIN_CLASS_DROPDOWN, 5);
        driverTools.click(CABIN_CLASS_DROPDOWN);

        Locator cabinOption = new Locator("CABIN_OPTION", By.xpath("//div[contains(@class,'Dialog_body')]//span[normalize-space(translate(text(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'))='" + cabin.toUpperCase() + "']"));
        driverTools.waitForElementToAppear(cabinOption, 5);
        driverTools.clickWithJavascriptExecutor(cabinOption);

        driverTools.click(SAVE_CANBIN);
    }

    public void enterMaxBudget(String budget) {
        driverTools.waitForElementToAppear(MAX_BUDGET, 5);
        driverTools.sendKeys(MAX_BUDGET, budget);
    }

    public boolean isRoutePresent(int route, String origin, String destination) {
        Locator originText = new Locator("ROUTE_" + route + "_ORIGIN", By.xpath("//div[normalize-space(.)='Route " + route + "']/following-sibling::div[1]"));
        Locator destinationText = new Locator("ROUTE_" + route + "_DESTINATION", By.xpath("//div[normalize-space(.)='Route " + route + "']/ancestor::div[.//div[contains(@class,'text-right')]][1]//div[contains(@class,'text-right')][1]"));
        driverTools.waitForElementToAppear(originText, 10);
        return driverTools.getText(originText).orElseThrow().contains("(" + origin + ")")
                && driverTools.getText(destinationText).orElseThrow().contains("(" + destination + ")");
    }

    public String getExceptionBudget() {
        driverTools.waitForElementToAppear(EXCEPTION_BUDGET, 10);
        return driverTools.getText(EXCEPTION_BUDGET).orElseThrow();
    }

    public void clickCheckDetails() {
        driverTools.waitForElementToAppear(CHECK_DETAILS, 10);
        driverTools.clickWithJavascriptExecutor(CHECK_DETAILS);
    }

    public String getExceptionPopupValue(String label) {
        Locator value = new Locator("EXCEPTION_POPUP_" + label, By.xpath("//section[.//h2[normalize-space(text())='Travel plan exception']]//span[normalize-space(text())='" + label + "']/following-sibling::div/span[2]"));
        driverTools.waitForElementToAppear(value, 10);
        return driverTools.getText(value).orElseThrow();
    }

    public void closeExceptionPopup() {
        driverTools.waitForElementToAppear(EXCEPTION_POPUP_CLOSE, 5);
        driverTools.clickWithJavascriptExecutor(EXCEPTION_POPUP_CLOSE);
    }

    public boolean isTravelPlanDeletedSuccessfully() {
        driverTools.waitForElementToAppear(TRAVEL_PLAN_DELETED_SNACKBAR, 5);
        return driverTools.isElementDisplayed(TRAVEL_PLAN_DELETED_SNACKBAR);
    }

    public boolean isExceptionAddedSuccessfully() {
        driverTools.waitForElementToAppear(EXCEPTION_ADDED_SNACKBAR, 5);
        return driverTools.isElementDisplayed(EXCEPTION_ADDED_SNACKBAR);
    }

    public void saveException() {
        driverTools.waitForElementToBeClickable(SAVE_EXCEPTION, 5);
        driverTools.click(SAVE_EXCEPTION);
    }

    public void addAnotherRoute() {
        driverTools.waitForElementToAppear(ADD_ANOTHER_ROUTE, 5);
        driverTools.clickWithJavascriptExecutor(ADD_ANOTHER_ROUTE);
    }

    private void enterRouteCity(Locator inputField, String code) {
        driverTools.waitForElementToAppear(inputField);
        driverTools.click(inputField);
        driverTools.clearInput(inputField);
        driverTools.sendKeys(inputField, code);

        driverTools.dummyWait(2);
        Locator suggestion = new Locator("SUGGESTION_" + code, By.xpath("//div[contains(@class,'AutoCompleteSearch_list_box')]//div[contains(@class,'Label_truncation_lines') and normalize-space(text())='" + code + "']"));
        driverTools.waitForElementToAppear(suggestion, 5);
        driverTools.click(suggestion);
        driverTools.waitForElementToDisappear(suggestion, 5);
    }

    public void selectRouteDate(int route) {
        if (driverTools.waitForElementToAppear(ENABLED_ROUTE_DATE, 3).isEmpty()) {
            Locator dateBox = new Locator("ROUTE_" + route + "_DATE", By.xpath("(//div[@id='single-date-picker-search-form'])[" + route + "]//div[contains(@class,'cursor-pointer')]"));
            driverTools.waitForElementToAppear(dateBox, 5);
            driverTools.clickWithJavascriptExecutor(dateBox);
            driverTools.waitForElementToAppear(ENABLED_ROUTE_DATE, 5);
        }
        List<WebElement> dates = driverTools.elements(ENABLED_ROUTE_DATE).orElseThrow();
        driverTools.click(route == 2 ? dates.get(0) : dates.get(dates.size() - 1));
    }

    public void addMultiCityRoute(int route, String origin, String destination) {
        enterRouteCity(getRouteInputLocator(route, "Select city or airport"), origin);
        enterRouteCity(getRouteInputLocator(route, "Going anywhere?"), destination);
    }

    public void travelPlanAdditionalDetails(){
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(SELECT_CITY);
        driverTools.waitForElementToAppear(CHECKBOX_FOR_JAKARTA, 5);
        driverTools.click(CHECKBOX_FOR_JAKARTA);
    }

    private void selectDate(Locator dateButton, int day) {
        Locator dateValue = getDynamicDateLocator("DATE_" + day, day);
        driverTools.clickWithJavascriptExecutor(dateButton);
        driverTools.click(NAVIGATE_DATE);
        driverTools.clickWithJavascriptExecutor(dateValue);
        driverTools.clickWithJavascriptExecutor(SAVE_PRODUCT);
    }

    private Locator getDynamicDateLocator(String name, int date) {
            return new Locator(name, new By.ByXPath("//button[not(@disabled)]/span[text()='" + date + "']"));
    }

    private int[] generateRandomDateRange(String rangeAddDate) {
        String[] range = rangeAddDate.split("-");
        int start = Integer.parseInt(range[0]);
        int end = Integer.parseInt(range[1]);

        if (start <= 0 || end > 31 || start > end) {
            throw new RuntimeException("Invalid date range provided: " + rangeAddDate);
        }

        int randStart = generateRandomInt(start, end - 1);
        int randEnd = generateRandomInt(randStart + 1, end);

        return new int[]{randStart, randEnd};
    }

    private void enterLocationWithSuggestion(Locator inputField, String value) throws InterruptedException {
        driverTools.waitForElementToAppear(inputField);
        driverTools.click(inputField);
        driverTools.sendKeys(inputField, value);
        driverTools.waitForElementToAppear(SUGGESTED_DESTINATION, 5);
        driverTools.click(SUGGESTED_DESTINATION);
    }

    public void selectApproverAndSubmit() {
       selectApprover();
       clickRequestApproval();
       clickSubmitApproval();
    }

    public void selectApprover(){
        driverTools.clickWithJavascriptExecutor(APPROVER_DROPDOWN);
        driverTools.waitForElementToAppear(SELECT_APPROVER);
        driverTools.click(SELECT_APPROVER);
    }

    public void clickRequestApproval() {
        driverTools.waitForElementToAppear(REQUEST_APPROVAL, 5);
        driverTools.click(REQUEST_APPROVAL);
    }

    public void clickSubmitApproval() {
        driverTools.waitForElementToAppear(SUBMIT_APPROVAL, 5);
        driverTools.click(SUBMIT_APPROVAL);
    }

    private Locator getTravelPlanLocator(String name){
        return new Locator("TRAVEL_PLAN", new By.ByXPath("//*[text()='"+name+"']"));
    }

    public boolean isTravelPlanCreated(String name){
        driverTools.click(SEARCH_BAR);
        driverTools.sendKeys(SEARCH_BAR,name);
        driverTools.click(SEARCH_BUTTON);
        Locator travelPlan=getTravelPlanLocator(name);
        driverTools.waitForElementToAppear(travelPlan,2);
        return driverTools.isElementDisplayed(travelPlan);
    }

    public void deleteTravelPlan(){
        clickOpenButton();
        clickCancelButton();
    }

    public void clickCancelButton(){
        driverTools.waitForElementToAppear(CANCEL_BUTTON);
        driverTools.click(CANCEL_BUTTON);
        driverTools.waitForElementToAppear(POPUP_CANCEL_BUTTON,5);
        driverTools.click(POPUP_CANCEL_BUTTON);
    }

    public void clickOpenButton(){
        driverTools.waitForElementToAppear(OPEN_BUTTON);
        driverTools.click(OPEN_BUTTON);
    }

    public void addFiveConfig(){
        driverTools.waitForElementToAppear(ADD_ANOTHER);

        for(int i=1;i<=4;i++){
            driverTools.clickWithJavascriptExecutor(ADD_ANOTHER);
            driverTools.waitForElementToAppear(ADD_ANOTHER);
        }
    }

    public boolean isAddButtonClickable() {
        return driverTools.waitForElementToBeClickable(ADD_ANOTHER, 5).isPresent();
    }

    public int noOfConfig() {
        List<WebElement> elements = driverTools.elements(TRAVEL_PLAN_REMOVE).orElse(List.of());
        return elements.size();
    }

    public void selectTravelPlan(){
        driverTools.waitForElementToBeClickable(TRAVEL_PLAN_BUTTON,10);
        driverTools.click(TRAVEL_PLAN_BUTTON);

        driverTools.waitForElementToAppear(YOUR_TRAVEL_PLAN,10);
        driverTools.click(YOUR_TRAVEL_PLAN);
    }

    public void setTravelPlan(String travelPlan){
        clickCreateTravel();
        Assertion.assertThat("Create Travel Plan Page is opened", isCreateTravelPageOpened(), is(true));

        driverTools.waitForElementToAppear(TRAVEL_PLAN);
        driverTools.sendKeys(TRAVEL_PLAN, travelPlan);

    }

    public void setStartDate() {
        driverTools.waitForElementToAppear(START_DATE_BOX);
        driverTools.click(START_DATE_BOX);

        Calendar calendar = Calendar.getInstance();
        startDate = new SimpleDateFormat("d MMMM yyyy EEEE", Locale.ENGLISH).format(calendar.getTime());
        Locator START_DATE = new Locator("START_DATE", By.xpath("//span[@aria-label='" + startDate + "']"));
        driverTools.waitForElementToAppear(START_DATE, 10);
        driverTools.click(START_DATE);

        driverTools.clickWithJavascriptExecutor(SAVE_PRODUCT);

    }

    public void setEndDate(int daysDifference) {
        driverTools.waitForElementToAppear(END_DATE_BOX);
        driverTools.click(END_DATE_BOX);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, daysDifference);
        endDate = new SimpleDateFormat("d MMMM yyyy EEEE").format(calendar.getTime());
        Locator END_DATE = new Locator("END_DATE", By.xpath("//span[@aria-label='" + endDate + "']"));
        driverTools.waitForElementToAppear(END_DATE, 10);
        driverTools.click(END_DATE);
        driverTools.clickWithJavascriptExecutor(SAVE_PRODUCT);
    }

    public void addTravelProduct(String product){
        driverTools.waitForElementToAppear(TRAVEL_PRODUCT, 5);
        driverTools.click(TRAVEL_PRODUCT);

        Locator locator = getSearchProductLocator(product);
        driverTools.waitForElementToAppear(locator, 5);

        Assertion.assertThat("Verify that Travel Product Dropdowns are visible",driverTools.isElementDisplayed(locator),is(true));

        driverTools.clickWithJavascriptExecutor(locator);
        driverTools.clickWithJavascriptExecutor(SAVE_PRODUCT);
    }

    public boolean isPastStartDateSelectable(){
        driverTools.waitForElementToAppear(DATE_BOX,10);
        driverTools.click(DATE_BOX);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String pastDate = new SimpleDateFormat("d MMMM yyyy EEEE", Locale.ENGLISH).format(calendar.getTime());

        Locator PAST_DATE = new Locator("PAST_DATE", By.xpath("//span[@aria-label='" + pastDate + "']"));

        driverTools.waitForElementToAppear(PAST_DATE, 5);
        return ! driverTools.isElementEnabled(PAST_DATE);
    }

    public boolean isAfterEndDateSelectable(){
        driverTools.waitForElementToAppear(DATE_BOX,10);
        driverTools.click(DATE_BOX);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        String futureDate = new SimpleDateFormat("d MMMM yyyy EEEE", Locale.ENGLISH).format(calendar.getTime());

        Locator FUTURE_DATE = new Locator("FUTURE_DATE", By.xpath("//span[@aria-label='" + futureDate + "']"));

        driverTools.waitForElementToAppear(FUTURE_DATE, 5);
        return ! driverTools.isElementEnabled(FUTURE_DATE);
    }

    public boolean checkIfCustomFieldsArePresent() {
        driverTools.waitForElementToAppear(SELECT_CITY_FIELD, 5);
        return driverTools.isElementPresent(SELECT_CITY_FIELD);
    }

    public boolean checkIfTravelPlanRequestIsPresent(String expectedAdditionalDetails) {
        driverTools.waitForElementToAppear(CUSTOM_FIELD_VALUE, 5);
        driverTools.waitForElementToAppear(CUSTOM_FIELD_NAME, 5);
        String additionalDetails = driverTools.getText(CUSTOM_FIELD_NAME).orElseThrow();
        boolean isCustomFieldValuePresent = driverTools.isElementPresent(CUSTOM_FIELD_VALUE);
        return additionalDetails.contains(expectedAdditionalDetails) && isCustomFieldValuePresent;
    }

    public void editTravelPlan(String newName,String newProduct,String newArrival,String newDeparture) throws InterruptedException {
        clickOpenButton();

        driverTools.click(EDIT_BUTTON);

        driverTools.waitForElementToAppear(TRAVEL_PLAN);

        driverTools.clearInput(TRAVEL_PLAN);
        driverTools.sendKeys(TRAVEL_PLAN, newName);

        driverTools.waitForElementToAppear(TRAVEL_PRODUCT, 5);
        driverTools.click(TRAVEL_PLAN_REMOVE);
        driverTools.click(TRAVEL_PRODUCT);

        Locator locator = getSearchProductLocator(newProduct);
        driverTools.waitForElementToAppear(locator, 5);

        driverTools.clickWithJavascriptExecutor(locator);
        driverTools.clickWithJavascriptExecutor(SAVE_PRODUCT);

        enterLocationWithSuggestion(INPUT_DEPARTURE, newDeparture);
        enterLocationWithSuggestion(INPUT_ARRIVAL, newArrival);

        driverTools.click(SAVE_CHANGES_BUTTON);
        driverTools.click(SUBMIT_APPROVAL);
    }

    public boolean isTravelPlanNameExist(String newName){
        Locator travelPlan=getTravelPlanLocator(newName);
        driverTools.waitForElementToAppear(travelPlan,2);
        return driverTools.isElementDisplayed(travelPlan);
    }

    public String getProductType(){
        driverTools.waitForElementToAppear(PRODUCT_TYPE,2);
        return driverTools.getText(PRODUCT_TYPE).orElseThrow();
    }

    public void cancelTravelPlan(){
        clickCancelButton();
    }

}
