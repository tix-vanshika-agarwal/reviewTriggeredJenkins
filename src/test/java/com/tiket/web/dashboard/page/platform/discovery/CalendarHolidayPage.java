package com.tiket.web.dashboard.page.platform.discovery;

import com.tiket.api.discovery.CalendarHolidayApi;
import com.tiket.core.CoreFunctions;
import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class CalendarHolidayPage<T extends WebDriver> extends BasePage<T> {
    public CalendarHolidayPage(Session<T> session) {
        super(session);
    }
    ILogger logger = BaseTest.mainLogger.get();
    private static final String CALENDAR_HOLIDAY_URL = "admin/home/v2/general-config/calendar";
    private static final Locator GET_IN_BUTTON = new Locator("GET_IN_BUTTON", By.xpath("//button[contains(@class,'Button_button__') or normalize-space()='GET IN']"));
    private static final Locator ADD_HOLIDAY_BUTTON = new Locator("ADD_HOLIDAY_BUTTON", By.xpath("//span[text()='Add New Calendar Holiday']"));
    private static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", By.id("search-description"));
    private static final Locator EMAIL_LABEL = new Locator("EMAIL_LABEL", By.xpath("//div[contains(@class,'TopNav_top_nav_actions')]"));
    private static final Locator BREADCRUMB_CALENDAR_HOLIDAY = new Locator("BREADCRUMB_CALENDAR_HOLIDAY", By.xpath("//p[text()='Calendar Holiday']"));
    private static final Locator TABLE_HEADER_ROW = new Locator("TABLE_HEADER_ROW", By.xpath("//tr[contains(@class,'Table_table_tr__') and contains(@class,'Table_striped__')]"));
    private static final Locator COUNTRY_CONTAINER = new Locator("COUNTRY_CONTAINER", By.xpath("//div[contains(@class,'country_container')]//input[@id='country']"));
    private static final Locator INDONESIA_CALENDAR = new Locator("INDONESIA_CALENDAR", By.xpath("//div[contains(@class,'dropdown_options')]//p[contains(text(),'INDONESIA')]"));
    private static final Locator SELECT_DATE_FIELD = new Locator("SELECT_DATE_FIELD", By.xpath("//input[@id='select-date']"));
    private static final Locator DESCRIPTION_EN = new Locator("DESCRIPTION_EN", By.id("description-en"));
    private static final Locator DESCRIPTION_ID = new Locator("DESCRIPTION_ID", By.id("description-id"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//span[text()='Submit']"));
    private static final Locator DONE_BUTTON = new Locator("DONE_BUTTON", By.xpath("//span[text()='Done']"));
    private static final Locator SNACKBAR_ELEMENT = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class, 'snackbars_positioner')]/div"));
    private static final Locator SEARCH_RESULT = new Locator("SEARCH_RESULT", By.xpath("//tr[contains(@class, 'style_general_config_wrapper')][1]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("//span[@data-testid='button-edit']"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", By.xpath("//span[@data-testid='button-delete']"));
    private static final Locator YES_DELETE_BUTTON = new Locator("YES_DELETE_BUTTON", By.xpath("//span[text()='Yes']"));
    private static final Locator TABLE_RESULT = new Locator("TABLE_RESULT", By.xpath("//table[contains(@class, 'style_table')]"));
    private static final Locator PAGINATION = new Locator("PAGINATION", By.xpath("//div[contains(@class,'styles_pagination__')]"));
    private static final Locator POP_UP_ACTION = new Locator("POP_UP_ACTION", By.xpath("//div[@id='modal-root']//section"));
    private static final Locator EDIT_MODAL = new Locator("EDIT_MODAL", By.xpath("//h2[contains(text(),'Edit Calendar Holiday')]"));
    private static final Locator NEXT_MONTH_BTN = new Locator("NEXT_MONTH_BTN", By.xpath("//button[contains(@class,'Navigation_next_button__')]"));
    private static final Locator PREV_MONTH_BTN = new Locator("PREV_MONTH_BTN", By.xpath("//button[contains(@class,'Navigation_prev_button__')]"));
    private static final Locator ADD_MODAL_TITLE = new Locator("ADD_MODAL_TITLE", By.xpath("//div[@id='modal-root']//h2[contains(text(),'Add Calendar Holiday')]"));
    private static final Locator MODAL_COUNTRY_CONTAINER = new Locator("MODAL_COUNTRY_CONTAINER", By.xpath("//div[@id='modal-root']//div[contains(@class,'country_container')]"));
    private static final Locator MODAL_COUNTRY_INPUT = new Locator("MODAL_COUNTRY_INPUT", By.xpath("//div[@id='modal-root']//input[@id='country']"));

    public boolean handleGetInPage() {
        try {
            driverTools.waitForElementToAppear(GET_IN_BUTTON, 2);
            if (driverTools.isElementDisplayed(GET_IN_BUTTON)) {
                logger.log("GET IN page detected — clicking GET IN button");
                driverTools.click(GET_IN_BUTTON);
                driverTools.waitForElementToDisappear(GET_IN_BUTTON, 2);
                logger.log("GET IN page handled successfully");
                return true;
            }
        } catch (Exception e) {
            logger.log("No GET IN page appeared — continuing");
        }
        return false;
    }

    private Locator setSelectedDate(LocalDate date) {
        String month = CoreFunctions.getMonthName(date.getMonthValue(),"id");
        String label = String.format("%d %s %d", date.getDayOfMonth(), month, date.getYear());
        return new Locator("SELECTED_DATE", By.xpath(String.format("//span[contains(@aria-label,'%s')]", label)));
    }

    private Locator verifyElementAppear(String text) {
        return new Locator("DESC_ID", By.xpath(String.format("//p[text()='%s']", text)));
    }

    private WebElement tableElement() {
        return driver.findElement(By.tagName("table"));
    }

    public void goToCalendarPage(String username, String password) {
        driverTools.maximizeWindow();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(CALENDAR_HOLIDAY_URL);
        handleGetInPage();
        memberLoginPage.goToPage(CALENDAR_HOLIDAY_URL);
        driverTools.waitForElementToAppear(EMAIL_LABEL);
    }

    public void goToCalendarPageViaUI(String username, String password) {
        driverTools.maximizeWindow();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.goToPage(CALENDAR_HOLIDAY_URL);
        memberLoginPage.adminLogin(username, password);
        memberLoginPage.goToPage(CALENDAR_HOLIDAY_URL);
        handleGetInPage();
        driverTools.waitForElementToAppear(EMAIL_LABEL);
    }

    public boolean isCalendarHolidayPageLoaded(String email) {
        driverTools.waitForElementToAppear(BREADCRUMB_CALENDAR_HOLIDAY, 5);
        boolean checkBreadCrumb = driverTools.isElementDisplayed(BREADCRUMB_CALENDAR_HOLIDAY);
        boolean checkSearchBar = driverTools.isElementDisplayed(SEARCH_BAR);
        boolean checkButton = driverTools.isElementDisplayed(ADD_HOLIDAY_BUTTON);
        boolean checkEmail = driverTools.getText(EMAIL_LABEL).isPresent() && !driverTools.getText(EMAIL_LABEL).get().isEmpty();

        logger.log("checkBreadCrumb: " + checkBreadCrumb);
        logger.log("checkSearchBar: " + checkSearchBar);
        logger.log("checkButton: " + checkButton);
        logger.log("checkEmail: " + checkEmail);

        return checkButton && checkBreadCrumb && checkSearchBar && checkEmail;
    }

    public void addNewHoliday(String title) {
        LocalDate tomorrow = LocalDate.parse(CoreFunctions.getCurrentDateYYYY_MM_DD()).plusDays(1);
        this.date = String.valueOf(tomorrow);
        Locator dateLocator = setSelectedDate(tomorrow);

        driverTools.click(ADD_HOLIDAY_BUTTON);
        driverTools.waitForElementToAppear(ADD_MODAL_TITLE, 5);
        driverTools.waitForElementToAppear(MODAL_COUNTRY_CONTAINER, 5);

        String currentCountry = driverTools.getValue(MODAL_COUNTRY_INPUT);
        if (!"INDONESIA".equalsIgnoreCase(currentCountry)) {
            driverTools.click(MODAL_COUNTRY_CONTAINER);
            driverTools.waitForElementToAppear(INDONESIA_CALENDAR, 5);
            driverTools.click(INDONESIA_CALENDAR);
        } else {
            logger.log("Country is already INDONESIA in modal, skipping dropdown selection.");
        }

        driverTools.waitForElementToAppear(SELECT_DATE_FIELD, 5);
        driverTools.click(SELECT_DATE_FIELD);
        driverTools.waitForElementToAppear(NEXT_MONTH_BTN, 5);
        driverTools.waitForElementToPresence(dateLocator, 5);
        driverTools.click(dateLocator);

        driverTools.waitForElementToAppear(DONE_BUTTON, 3);
        driverTools.click(DONE_BUTTON);

        long suffix = CoreFunctions.randomInt(8);
        descId = "id_" + title + suffix;
        descEn = "en_" + title + suffix;

        driverTools.sendKeys(DESCRIPTION_EN, descEn);
        driverTools.sendKeys(DESCRIPTION_ID, descId);

        driverTools.scrollIntoWebView(SUBMIT_BUTTON);
        driverTools.waitForElementToBeClickable(SUBMIT_BUTTON);
        driverTools.click(SUBMIT_BUTTON);
        driverTools.waitForElementToDisappear(SELECT_DATE_FIELD, 10);
    }

    public void updateCalendar() {
        driverTools.waitForElementToPresence(SEARCH_BAR, 2);

        driverTools.waitForElementToAppear(EDIT_BUTTON);
        driverTools.click(EDIT_BUTTON);
        driverTools.waitForElementToPresence(EDIT_MODAL, 2);

        driverTools.waitForElementToBeClickable(DESCRIPTION_ID);
        driverTools.waitForElementToBeClickable(DESCRIPTION_EN);

        String textId = driverTools.getValue(DESCRIPTION_ID);
        String textEn = driverTools.getValue(DESCRIPTION_EN);
        this.descId = textId + " updated";
        this.descEn = textEn + " updated";

        LocalDate futureDate = LocalDate.parse(CoreFunctions.getCurrentDateYYYY_MM_DD())
                .plusMonths(1)
                .withDayOfMonth(15);
        this.date = String.valueOf(futureDate);
        Locator dateLocator = setSelectedDate(futureDate);
        logger.log("Target date: " + this.date);

        driverTools.click(SELECT_DATE_FIELD);
        driverTools.waitForElementToAppear(NEXT_MONTH_BTN, 5);

        boolean found = !driver.findElements(dateLocator.by()).isEmpty();
        logger.log("Date found on initial view: " + found);

        for (int i = 0; i < 24 && !found; i++) {
            driverTools.click(NEXT_MONTH_BTN);
            try {
                Thread.sleep(400);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
            found = !driver.findElements(dateLocator.by()).isEmpty();
            logger.log("After next click " + (i + 1) + ", found: " + found);
        }

        if (!found) {
            for (int i = 0; i < 48 && !found; i++) {
                driverTools.click(PREV_MONTH_BTN);
                try { Thread.sleep(400); } catch (InterruptedException ignored) {}
                found = !driver.findElements(dateLocator.by()).isEmpty();
                logger.log("After prev click " + (i + 1) + ", found: " + found);
            }
        }

        if (!found) {
            Assertion.fail("Target date '" + this.date + "' was not found in the date picker after navigating months");
        }

        logger.log("Clicking date: " + driverTools.getValueAttribute(dateLocator, "aria-label"));
        driverTools.click(dateLocator);

        driverTools.waitForElementToAppear(DONE_BUTTON, 3);
        driverTools.click(DONE_BUTTON);

        deleteUsingBackspace(DESCRIPTION_ID);
        driverTools.sendKeys(DESCRIPTION_ID, this.descId);
        deleteUsingBackspace(DESCRIPTION_EN);
        driverTools.sendKeys(DESCRIPTION_EN, this.descEn);

        driverTools.scrollIntoWebView(SUBMIT_BUTTON);
        driverTools.waitForElementToBeClickable(SUBMIT_BUTTON);
        driverTools.click(SUBMIT_BUTTON);
        driverTools.waitForElementToDisappear(EDIT_MODAL, 5);
    }

    public Map<String, Boolean> verifyActionSuccessSnackbar(String expectedMsg) {
        driverTools.waitForElementToAppear(SNACKBAR_ELEMENT);
        Optional<String> snackbarText = driverTools.getText(SNACKBAR_ELEMENT);
        boolean snackBarVisible = snackbarText.isPresent();
        boolean verifySuccessText = verifyText(expectedMsg, snackbarText.orElse(""));
        Map<String, Boolean> result = new HashMap<>();
        result.put("snackbar", snackBarVisible);
        result.put("text", verifySuccessText);
        return result;
    }

    public boolean verifyText(String expected, String actual) {
        logger.log("Expected Text: " + expected + " | Actual Text: " + actual);
        return actual.equalsIgnoreCase(expected);
    }

    public List<String> searchCreatedHoliday(String title) {
        inputStringOneAtTime(SEARCH_BAR, title, SEARCH_RESULT);

        if (!isSearchFiltered(title)) {
            logger.log("Search not filtered — deleting last char to re-trigger");
            driverTools.sendKeys(SEARCH_BAR, Keys.BACK_SPACE.toString());
            String shortened = title.substring(0, title.length() - 1);
            waitForFirstRowToContain(shortened);
        }

        driverTools.waitForElementToAppear(verifyElementAppear(descId));
        if (!driverTools.isElementDisplayed(verifyElementAppear(descId))) {
            Assertion.fail("desired result not found.");
        }

        List<WebElement> column = driver.findElement(SEARCH_RESULT.by()).findElements(By.tagName("td"));
        List<String> searchResult = new ArrayList<>();
        for (WebElement td : column) {
            searchResult.add(td.getText());
        }
        logger.log("Search Result: " + searchResult);
        return searchResult;
    }

    public List<String> verifySuccessUpdatedHolidayData() {
        deleteUsingBackspace(SEARCH_BAR);
        inputStringOneAtTime(SEARCH_BAR, this.descId, SEARCH_RESULT);

        if (!isSearchFiltered(this.descId)) {
            logger.log("Search not filtered — deleting last char to re-trigger");
            driverTools.sendKeys(SEARCH_BAR, Keys.BACK_SPACE.toString());
            String shortened = this.descId.substring(0, this.descId.length() - 1);
            waitForFirstRowToContain(shortened);
        }

        driverTools.waitForElementToAppear(verifyElementAppear(this.descId));
        if (!driverTools.isElementDisplayed(verifyElementAppear(this.descId))) {
            Assertion.fail("desired result not found.");
        }

        List<WebElement> column = driver.findElement(SEARCH_RESULT.by()).findElements(By.tagName("td"));
        List<String> searchResult = new ArrayList<>();
        for (WebElement td : column) {
            searchResult.add(td.getText());
        }
        logger.log("Search Result: " + searchResult);
        return searchResult;
    }

    public Map<String, String> getDataToValidateFromAPI() throws ExecutionException, InterruptedException {
        Map<String, String> result = new HashMap<>();
        JSONObject responseData = callCalenderHolidayApi();

        String responseResult = responseData.toString(4);
        String code = responseData.getString("code");
        JSONObject data = responseData.getJSONObject("data");
        JSONArray contentArray = data.getJSONArray("content");

        String dateInResponse = null;
        String holidayId = null;
        boolean isDescENPresent = false;
        boolean isDescIDPresent = false;

        for (int i = 0; i < contentArray.length(); i++) {
            JSONObject contentItem = contentArray.getJSONObject(i);
            JSONArray descriptions = contentItem.getJSONArray("descriptions");

            for (int j = 0; j < descriptions.length(); j++) {
                JSONObject description = descriptions.getJSONObject(j);
                String content = description.getString("content");
                if (content.equals(descEn)) {
                    isDescENPresent = true;
                    logger.log("Found: " + descEn + " in the response");
                }
                if (content.equals(descId)) {
                    isDescIDPresent = true;
                    logger.log("Found: " + descId + " in the response");
                }
            }
            if (isDescENPresent && isDescIDPresent) {
                holidayId = contentItem.getString("id");
                dateInResponse = contentItem.getString("date");
                break;
            }
        }

        if (!isDescENPresent && !isDescIDPresent) {
            logger.log(String.format("Both %s and %s are not found in the response.", descEn, descId));
            logger.log("Here is the response data: " + data);
        }

        result.put("response", responseResult);
        result.put("code", code);
        result.put("date", dateInResponse);
        result.put("isDescENPresent", String.valueOf(isDescENPresent));
        result.put("isDescIDPresent", String.valueOf(isDescIDPresent));
        result.put("holidayId", holidayId);
        return result;
    }

    public boolean verifyTableHeaders(List<String> expectedHeaders) {
        driverTools.waitForElementToPresence(TABLE_HEADER_ROW, 10);
        String headerText = driverTools.getText(TABLE_HEADER_ROW).orElseThrow(() -> new IllegalStateException("Calendar holiday table header text could not be read"));
        List<String> actualHeaders = Arrays.stream(headerText.split("\\R")).map(String::trim).filter(header -> !header.isEmpty()).map(header -> header.replaceAll("[()]", "")).toList();
        logger.log("Expected headers: " + expectedHeaders);
        logger.log("Actual headers: " + actualHeaders);
        return actualHeaders.equals(expectedHeaders);
    }

    public Map<String, Object> getDataFromTable() {
        Map<String, Object> result = new HashMap<>();
        List<Object> country = new ArrayList<>(), date = new ArrayList<>(), descEn = new ArrayList<>(), descId = new ArrayList<>();
        driverTools.waitForElementToPresence(SEARCH_RESULT, 10);
        List<WebElement> rows = tableElement().findElements(By.tagName("tr"));
        for (int i = 1; i < rows.size(); i++) {
            String[] s = rows.get(i).getText().split("\n");
            if (s.length < 4) { continue; }
            country.add(s[0]);
            date.add(s[1]);
            descEn.add(s[2]);
            descId.add(s[3]);
        }

        boolean action = false;
        if (rows.size() > 1) {
            new Actions(driver).moveToElement(rows.get(1)).perform();
            action = !driver.findElements(EDIT_BUTTON.by()).isEmpty() && !driver.findElements(DELETE_BUTTON.by()).isEmpty();
        }

        result.put("country", country);
        result.put("date", date);
        result.put("descEn", descEn);
        result.put("descId", descId);
        result.put("action", action);
        return result;
    }

    public void scrollInHolidayPage() {
        driverTools.waitForElementToPresence(PAGINATION);
        driverTools.scrollIntoWebView(PAGINATION);
        driverTools.waitForElementToAppear(PAGINATION, 2);
        driverTools.scrollIntoWebView(BREADCRUMB_CALENDAR_HOLIDAY);
        driverTools.waitForElementToAppear(BREADCRUMB_CALENDAR_HOLIDAY, 2);
    }

    public void searchCalendar(String title) {
        inputStringOneAtTime(SEARCH_BAR, title, SEARCH_RESULT);

        if (!isSearchFiltered(title)) {
            logger.log("Search not filtered — deleting last char to re-trigger");
            driverTools.sendKeys(SEARCH_BAR, Keys.BACK_SPACE.toString());
            String shortened = title.substring(0, title.length() - 1);
            waitForFirstRowToContain(shortened);
        }
    }

    public Map<String, Object> verifySearchCalendarResult() {
        Map<String, Object> result = new HashMap<>();
        List<WebElement> rows = tableElement().findElements(By.tagName("tr"));
        List<String> descEn = new ArrayList<>(), descId = new ArrayList<>();

        for (int i = 2; i < rows.size(); i++) {
            String[] s = rows.get(i).getText().split("\n");
            descEn.add(s[2]);
            descId.add(s[3]);
        }
        result.put("en", descEn);
        result.put("id", descId);

        logger.log("Result ID: " + descId);
        logger.log("Result EN: " + descEn);
        return result;
    }

    public void deleteCalendar() {
        waitForTooManyRequest();
        String currentSearch = driverTools.getValue(SEARCH_BAR);
        if (currentSearch == null || currentSearch.isBlank() || !currentSearch.equals(this.descId)) {
            deleteUsingBackspace(SEARCH_BAR);
            inputStringOneAtTime(SEARCH_BAR, this.descId, SEARCH_RESULT);
        }
        driverTools.waitForElementToAppear(DELETE_BUTTON);
        driverTools.click(DELETE_BUTTON);
        driverTools.waitForElementToAppear(POP_UP_ACTION, 2);
        driverTools.waitForElementToAppear(YES_DELETE_BUTTON, 5);
        driverTools.click(YES_DELETE_BUTTON);
        driverTools.waitForElementToDisappear(POP_UP_ACTION, 2);
        driverTools.waitForElementToAppear(SNACKBAR_ELEMENT, 2);
        driverTools.waitForElementToDisappear(SNACKBAR_ELEMENT, 2);
    }

    public List<String> verifySuccessDelete() {
        deleteUsingBackspace(SEARCH_BAR);
        inputStringOneAtTime(SEARCH_BAR, this.descId, TABLE_RESULT);
        Locator deletedRow = verifyElementAppear(this.descId);
        driverTools.waitForElementToDisappear(deletedRow, 10);
        List<String> searchResult = new ArrayList<>();
        if (driverTools.isElementDisplayed(SEARCH_RESULT)) {
            searchResult.add("Search result found.");
        }
        logger.log("Search Result: " + searchResult);
        return searchResult;
    }

    private boolean isSearchFiltered(String text) {
        return driverTools.waitForElementTextToContainIgnoreCase(SEARCH_RESULT, text, 5);
    }

    private void waitForFirstRowToContain(String text) {
        logger.log("Waiting for first data row to contain: " + text);
        boolean found = driverTools.waitForElementTextToContainIgnoreCase(SEARCH_RESULT, text, 15);
        if (!found) {
            Assertion.fail("First row did not contain expected text: " + text);
        }
        logger.log("Search filter confirmed for: " + text);
    }

    private void waitForTooManyRequest() {
        if (!driverTools.isElementDisplayed(SNACKBAR_ELEMENT)) {
            return;
        }
        String snackbarText = driverTools.getText(SNACKBAR_ELEMENT).orElse("").trim();
        String normalizedText = snackbarText.toLowerCase(Locale.ROOT);
        boolean rateLimitDetected = snackbarText.contains("429") || normalizedText.contains("too many request") || normalizedText.contains("rate limit");
        if (rateLimitDetected) {
            logger.log("Rate-limit snackbar detected: " + snackbarText);
            driverTools.waitForElementToDisappear(SNACKBAR_ELEMENT, 5);
        }
    }

    private void deleteUsingBackspace(Locator locator) {
        Actions actions = new Actions(driver);
        WebElement e = driver.findElement(locator.by());
        String textToDelete = e.getAttribute("value");
        logger.log(String.format("Trying to delete '%s' from locator '%s' using backspace...", textToDelete, locator.name()));
        driverTools.click(locator); // replaced e.click()
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            actions.keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND).perform();
        } else {
            actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
        }
        actions.sendKeys(Keys.BACK_SPACE).perform();
        logger.log("Deleted successfully");
    }

    public void inputStringOneAtTime(Locator locator, String textToInput, Locator... waitElement) {
        WebElement e = driver.findElement(locator.by());
        List<Locator> l = Arrays.stream(waitElement).toList();

        logger.log(String.format("Trying to input '%s' one char at a time into '%s'...", textToInput, locator.name()));

        String firstPart = textToInput.substring(0, textToInput.length() - 5);
        String secondPart = textToInput.substring(textToInput.length() - 5);

        e.sendKeys(firstPart);
        for (char c : secondPart.toCharArray()) {
            e.sendKeys(String.valueOf(c));
            if (!l.isEmpty()) {
                for (Locator value : l) {
                    driverTools.waitForElementToAppear(value, 2);
                }
            }
            waitForTooManyRequest();
        }
        logger.log(String.format("sent '%s' to '%s'", textToInput, locator.name()));
    }

    private JSONObject callCalenderHolidayApi() throws ExecutionException, InterruptedException {
        CalendarHolidayApi.Inputs inputs = new CalendarHolidayApi.Inputs(date);
        CalendarHolidayApi calendarHolidayApi = new CalendarHolidayApi(inputs);
        ApiResponse apiResponse = calendarHolidayApi.hitApi(apiTools);
        return new JSONObject(apiResponse.getBody());
    }

    @Getter @Setter
    private String country, descId, descEn, date;
}