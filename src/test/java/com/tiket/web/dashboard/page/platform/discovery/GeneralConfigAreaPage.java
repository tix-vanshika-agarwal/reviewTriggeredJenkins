package com.tiket.web.dashboard.page.platform.discovery;

import com.tiket.api.discovery.GeneralConfigAreaAPI;
import com.tiket.core.CoreFunctions;
import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import static com.tiket.testdata.TestData.getParam;

public class GeneralConfigAreaPage<T extends WebDriver> extends BasePage<T> {
    public GeneralConfigAreaPage(Session<T> session) { super(session); }
    ILogger logger = BaseTest.mainLogger.get();

    private static final String AREA_URL = "/admin/home/v2/general-config/area";
    private static final Locator EMAIL_LABEL = new Locator("EMAIL_LABEL", By.xpath("//div[contains(@class,'TopNav_top_nav_actions')]"));
    private static final Locator AREA_BREADCRUMB = new Locator("AREA_BREADCRUMB", By.xpath("//p[normalize-space()='Area']"));
    private static final Locator ADD_NEW_AREA = new Locator("ADD_NEW_AREA", By.xpath("//span[contains(text(),'Add New Area')]"));
    private static final Locator SEARCH_AREA = new Locator("SEARCH_AREA", By.xpath("//input[@id='search-area-name']"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("(//span[contains(@class,'style_edit_btn__')])[1]"));
    private static final Locator AREA_ORDER_BY = new Locator("AREA_ORDER_BY", By.xpath("//input[@id='area-order-by']"));
    private static final Locator ORDER_BY_DESC = new Locator("ORDER_BY_DESC", By.xpath("//p[normalize-space()='DESC']"));
    private static final Locator GET_IN_BUTTON = new Locator("GET_IN_BUTTON", By.xpath("//button[normalize-space(.)='GET IN']"));

    // Main Attributes
    private static final Locator AREA_CODE_NAME_INPUT = new Locator("AREA_CODE_NAME_INPUT", By.id("area-codename"));
    private static final Locator ENGLISH_NAME_INPUT = new Locator("ENGLISH_NAME_INPUT", By.xpath("//input[@id='english-name']"));
    private static final Locator AREA_TYPE_DROPDOWN = new Locator("AREA_TYPE_DROPDOWN", By.xpath("//input[@id='area-type']"));
    private static final Locator AREA_TYPE_COUNTRY = new Locator("AREA_TYPE_COUNTRY", By.xpath("//div[contains(@class,'styles_dropdown_option')][.//p[normalize-space()='COUNTRY']]"));
    private static final Locator TWO_DIGIT_ABBREVIATION = new Locator("TWO_DIGIT_ABBREVIATION", By.xpath("//input[@id='2-digits-abbreviation-iso-31661']"));
    private static final Locator THREE_DIGIT_ABBREVIATION = new Locator("THREE_DIGIT_ABBREVIATION", By.xpath("//input[@id='3-digits-abbreviation-iso-31661']"));
    private static final Locator SHORT_ABBREVIATION = new Locator("SHORT_ABBREVIATION", By.xpath("//input[@id='short-abbreviation']"));

    // Supporting Attributes
    private static final Locator SUPPORTING_ATTRIBUTE_TAB = new Locator("SUPPORTING_ATTRIBUTE_TAB", By.xpath("//span[normalize-space()='Supporting Attributes']"));
    private static final Locator TIME_SEPARATOR_INPUT = new Locator("TIME_SEPARATOR_INPUT", By.xpath("//div[contains(@class,'container')]//input[contains(@name,'supportingAttributes.timeSeparator')]"));
    private static final Locator THOUSANDS_SEPARATOR_INPUT = new Locator("THOUSANDS_SEPARATOR_INPUT", By.xpath("//input[@id='thousands-separator']"));
    private static final Locator DECIMAL_SEPARATOR_INPUT = new Locator("DECIMAL_SEPARATOR_INPUT", By.xpath("//input[@id='decimal-separator']"));
    private static final Locator DIALING_CODE_INPUT = new Locator("DIALING_CODE_INPUT", By.xpath("//div[contains(@class,'inline_input')]//input[contains(@id,'dialing-code')]"));
    private static final Locator TIME_FORMATING_DROPDOWN = new Locator("TIME_FORMATING_DROPDOWN", By.xpath("//input[@id='time-formatting']"));
    private static final Locator TIME_FORMAT_GENERAL = new Locator("TIME_FORMAT_GENERAL", By.xpath("//div[contains(@class,'styles_dropdown_option')]//p[normalize-space()='dd-mm-yyyy']/parent::div"));

    // Usage
    private static final Locator USAGE_TAB = new Locator("USAGE_TAB", By.xpath("//span[normalize-space()='Usage']"));
    private static final Locator LANGUAGE_DROPDOWN = new Locator("LANGUAGE_DROPDOWN", By.xpath("//p[contains(.,'Language List')]/following::div[contains(@class,'styles_dropdown_button')][1]"));
    private static final Locator LANGUAGE_EN = new Locator("LANGUAGE_EN", By.xpath("//div[contains(@class,'dropdown_content')]//p[contains(@class,'size_b2') and contains(text(),'en')]"));
    private static final Locator LANGUAGE_ID = new Locator("LANGUAGE_ID", By.xpath("//div[contains(@class,'dropdown_content')]//p[contains(@class,'size_b2') and contains(text(),'id')]"));
    private static final Locator CURRENCY_DROPDOWN = new Locator("CURRENCY_DROPDOWN", By.xpath("//p[contains(.,'Language List')]/following::div[contains(@class,'styles_dropdown_button')][2]"));
    private static final Locator CURRENCY_USD = new Locator("CURRENCY_USD", By.xpath("(//p[contains(.,'List')]/following::div[contains(@class,'styles_dropdown_button')])[2]"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[contains(.,'Submit')]"));

    // Utilities
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class,'text_content') and contains(text(),'Success')]"));
    private static final Locator SEARCH_RESULT = new Locator("SEARCH_RESULT", By.xpath("//tr[contains(@class, 'style_general_config_wrapper')][1]"));

    private String timeSeparatorHard = ":";
    private String thousandSeparatorHard = ".";
    private String decimalSeparatorHard = ",";

    public void handleGetInPage() {
        try {
            driverTools.waitForElementToAppear(GET_IN_BUTTON, 5);
            if (driverTools.isElementDisplayed(GET_IN_BUTTON)) {
                logger.log("GET IN page detected — clicking GET IN button");
                driverTools.click(GET_IN_BUTTON);
                driverTools.waitForElementToDisappear(GET_IN_BUTTON, 5);
                logger.log("GET IN page handled successfully");
            }
        } catch (Exception e) {
            logger.log("No GET IN page appeared — continuing");
        }
    }

    public void goToAreaPage(String username, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(AREA_URL);
        handleGetInPage();
        driverTools.waitForElementToAppear(ADD_NEW_AREA, 10);
        logger.log("Navigated to Area URL. Current URL: " + driverTools.getCurrentURL());
    }

    public boolean isAreaPageOpened(String email) {
        // Note: email/login verification is implicitly covered by adminLoginViaCookie.
        driverTools.waitForElementToAppear(ADD_NEW_AREA, 3);
        driverTools.waitForElementToAppear(EDIT_BUTTON, 5);
        driverTools.scrollIntoWebView(EDIT_BUTTON);
        boolean orderByDropDown = driverTools.isElementDisplayed(AREA_ORDER_BY);
        boolean searchColumn = driverTools.isElementDisplayed(SEARCH_AREA);
        boolean editButton = driverTools.isElementDisplayed(EDIT_BUTTON);
        boolean checkEmail = driverTools.getText(EMAIL_LABEL)
                .map(text -> !text.isEmpty())
                .orElse(false);

        logger.log("Check email: " + checkEmail);
        logger.log("Check order by section: " + orderByDropDown);
        logger.log("Check search column section: " + searchColumn);
        logger.log("Check edit button on the list: " + editButton);

        return orderByDropDown && searchColumn && editButton && checkEmail;
    }

    private void selectLanguageIfPresent(Locator language) {
        driverTools.click(LANGUAGE_DROPDOWN);
        driverTools.waitForElementToBeClickable(language);
        if (driverTools.isElementDisplayed(language)) {
            driverTools.click(language);
        }
    }

    @SneakyThrows
    public void addNewArea(String dialingCode) {
        driverTools.click(ADD_NEW_AREA);
        this.areaCodeName = "AreaAutomation_" + CoreFunctions.randomInt(5);
        this.englishName = "EnglishAuto_" + CoreFunctions.randomInt(5);
        this.twoDigitAbbreviation = CoreFunctions.randomName(2);
        this.threeDigitAbbreviation = CoreFunctions.randomName(3);

        logger.log("Unique Area Code Name: " + areaCodeName);
        logger.log("Unique English Code Name: " + englishName);
        logger.log("2 Abbreviation: " + this.twoDigitAbbreviation);
        logger.log("3 Abbreviation: " + this.threeDigitAbbreviation);

        // Area Code
        driverTools.click(AREA_CODE_NAME_INPUT);
        driverTools.sendKeys(AREA_CODE_NAME_INPUT, areaCodeName);
        driverTools.sendKeys(ENGLISH_NAME_INPUT, englishName);
        driverTools.click(AREA_TYPE_DROPDOWN);
        driverTools.waitForElementToPresence(AREA_TYPE_COUNTRY);
        driverTools.click(AREA_TYPE_COUNTRY);
        driverTools.sendKeys(TWO_DIGIT_ABBREVIATION, twoDigitAbbreviation);
        driverTools.sendKeys(THREE_DIGIT_ABBREVIATION, threeDigitAbbreviation);
        driverTools.sendKeys(SHORT_ABBREVIATION, twoDigitAbbreviation);

        // Formatting
        driverTools.click(SUPPORTING_ATTRIBUTE_TAB);
        driverTools.waitForElementToBeClickable(TIME_SEPARATOR_INPUT);
        driverTools.sendKeys(TIME_SEPARATOR_INPUT, timeSeparatorHard);
        driverTools.sendKeys(THOUSANDS_SEPARATOR_INPUT, thousandSeparatorHard);
        driverTools.sendKeys(DECIMAL_SEPARATOR_INPUT, decimalSeparatorHard);
        driverTools.sendKeys(DIALING_CODE_INPUT, dialingCode);
        driverTools.waitForElementToBeClickable(TIME_FORMATING_DROPDOWN);
        driverTools.click(TIME_FORMATING_DROPDOWN);
        driverTools.scrollToElement(TIME_FORMAT_GENERAL);
        driverTools.waitForElementToBeClickable(TIME_FORMAT_GENERAL);
        driverTools.click(TIME_FORMAT_GENERAL);

        // Usage
        driverTools.click(USAGE_TAB);
        selectLanguageIfPresent(LANGUAGE_EN);
        selectLanguageIfPresent(LANGUAGE_ID);
        driverTools.waitForElementToBeClickable(CURRENCY_DROPDOWN);
        driverTools.click(CURRENCY_DROPDOWN);
        if (driverTools.isElementDisplayed(CURRENCY_USD)) {
            driverTools.click(CURRENCY_USD);
        }
        driverTools.click(SUBMIT_BUTTON);
    }

    public boolean isSnackbarSuccessAppear() {
        return driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 3).isPresent();
    }

    public void searchLatestCreated() {
        driverTools.click(AREA_BREADCRUMB);
        if (driverTools.isElementDisplayed(SEARCH_AREA)) {
            driverTools.click(SEARCH_AREA);
        }
        driverTools.inputStringOneAtTime(SEARCH_AREA, areaCodeName);
    }

    public String editCreatedArea() {
        driverTools.click(EDIT_BUTTON);
        driverTools.inputStringOneAtTime(AREA_CODE_NAME_INPUT, "edited");
        this.editedAreaCodeName = String.valueOf(driver.findElement(AREA_CODE_NAME_INPUT.by()).getAttribute("value"));

        logger.log("Value is: " + this.editedAreaCodeName);
        driverTools.click(SUBMIT_BUTTON);
        return this.editedAreaCodeName;
    }

    private JSONObject callGeneralConfigAreaApi() throws ExecutionException, InterruptedException {
        GeneralConfigAreaAPI generalConfigAreaAPI = new GeneralConfigAreaAPI();
        ApiResponse apiResponse = generalConfigAreaAPI.hitApi(apiTools);
        return new JSONObject(apiResponse.getBody());
    }

    public Map<String, String> getDataToValidateFromAPI() throws ExecutionException, InterruptedException {
        Map<String, String> result = new HashMap<>();
        JSONObject responseData = callGeneralConfigAreaApi();

        String responseResult = responseData.toString(4);
        String code = responseData.optString("code", "");

        JSONObject data = responseData.getJSONObject("data");
        JSONArray contentArray = data.getJSONArray("content");

        boolean isIdPresent = false;
        boolean isOrderPresent = false;
        boolean isTypePresent = false;
        boolean isCodePresent = false;
        boolean isAbbreviationPresent = false;
        boolean isDialingCodePresent = false;
        boolean isTimeSeparatorPresent = false;
        boolean isDecimalSeparatorPresent = false;
        boolean isThousandsSeparatorPresent = false;
        boolean isDateFormatPresent = false;
        boolean isActivePresent = false;

        String apiResultType = null;
        String apiResultAreaCode = null;
        String apiResultAbbreviations = null;
        String apiResultDialingCode = null;
        String apiResultTimeSeparator = null;
        String apiResultDecimalSeparator = null;
        String apiResultThousandsSeparator = null;
        String apiResultDateFormat = null;
        String apiResultActive = null;

        for (int i = 0; i < contentArray.length(); i++) {
            JSONObject contentItem = contentArray.getJSONObject(i);

            if (contentItem.has("id") && !contentItem.isNull("id")) {
                isIdPresent = true;
                logger.log("Found id: " + isIdPresent);
            }
            if (contentItem.has("order") && !contentItem.isNull("order")) {
                isOrderPresent = true;
                logger.log("Found order: " + isOrderPresent);
            }
            if (contentItem.has("type") && !contentItem.isNull("type")) {
                apiResultType = contentItem.getString("type");
                isTypePresent = true;
                logger.log("Found type: " + apiResultType);
            }
            if (contentItem.has("code") && !contentItem.isNull("code")) {
                apiResultAreaCode = contentItem.getString("code");
                isCodePresent = true;
                logger.log("Found code: " + apiResultAreaCode);
            }
            if (contentItem.has("abbreviations") && !contentItem.isNull("abbreviations")) {
                JSONArray abbreviationArray = contentItem.getJSONArray("abbreviations");
                apiResultAbbreviations = abbreviationArray.toString();
                isAbbreviationPresent = abbreviationArray.length() > 0;
                logger.log("Found abbreviations: " + apiResultAbbreviations);
            }
            if (contentItem.has("dialingCode") && !contentItem.isNull("dialingCode")) {
                apiResultDialingCode = contentItem.getString("dialingCode");
                isDialingCodePresent = true;
                logger.log("Found dialingCode: " + apiResultDialingCode);
            }
            if (contentItem.has("timeSeparator") && !contentItem.isNull("timeSeparator")) {
                apiResultTimeSeparator = contentItem.getString("timeSeparator");
                isTimeSeparatorPresent = true;
                logger.log("Found timeSeparator: " + apiResultTimeSeparator);
            }
            if (contentItem.has("decimalSeparator") && !contentItem.isNull("decimalSeparator")) {
                apiResultDecimalSeparator = contentItem.getString("decimalSeparator");
                isDecimalSeparatorPresent = true;
                logger.log("Found decimalSeparator: " + apiResultDecimalSeparator);
            }
            if (contentItem.has("thousandsSeparator") && !contentItem.isNull("thousandsSeparator")) {
                apiResultThousandsSeparator = contentItem.getString("thousandsSeparator");
                isThousandsSeparatorPresent = true;
                logger.log("Found thousandsSeparator: " + apiResultThousandsSeparator);
            }
            if (contentItem.has("dateFormat") && !contentItem.isNull("dateFormat")) {
                apiResultDateFormat = contentItem.getString("dateFormat");
                isDateFormatPresent = true;
                logger.log("Found dateFormat: " + apiResultDateFormat);
            }
            if (contentItem.has("active") && !contentItem.isNull("active")) {
                apiResultActive = String.valueOf(contentItem.getBoolean("active"));
                isActivePresent = true;
                logger.log("Found active: " + apiResultActive);
            }
        }

        result.put("response", responseResult);
        result.put("code", code);
        result.put("type", apiResultType);
        result.put("areaCode", apiResultAreaCode);
        result.put("abbreviations", apiResultAbbreviations);
        result.put("dialingCode", apiResultDialingCode);
        result.put("timeSeparator", apiResultTimeSeparator);
        result.put("decimalSeparator", apiResultDecimalSeparator);
        result.put("thousandsSeparator", apiResultThousandsSeparator);
        result.put("dateFormat", apiResultDateFormat);
        result.put("active", apiResultActive);

        result.put("isIdPresent", String.valueOf(isIdPresent));
        result.put("isOrderPresent", String.valueOf(isOrderPresent));
        result.put("isTypePresent", String.valueOf(isTypePresent));
        result.put("isCodePresent", String.valueOf(isCodePresent));
        result.put("isAbbreviationPresent", String.valueOf(isAbbreviationPresent));
        result.put("isDialingCodePresent", String.valueOf(isDialingCodePresent));
        result.put("isTimeSeparatorPresent", String.valueOf(isTimeSeparatorPresent));
        result.put("isDecimalSeparatorPresent", String.valueOf(isDecimalSeparatorPresent));
        result.put("isThousandsSeparatorPresent", String.valueOf(isThousandsSeparatorPresent));
        result.put("isDateFormatPresent", String.valueOf(isDateFormatPresent));
        result.put("isActivePresent", String.valueOf(isActivePresent));
        return result;
    }

    public boolean isGeneralConfigAreaApiDataValid() throws ExecutionException, InterruptedException {
        Map<String, String> map = getParam("testAddEditArea");
        Map<String, String> actual = getDataToValidateFromAPI();
        boolean isValid = true;

        String actualAbbreviations = actual.get("abbreviations");
        String actualDialingCode = actual.get("dialingCode");
        String actualTimeSeparator = actual.get("timeSeparator");
        String actualDecimalSeparator = actual.get("decimalSeparator");
        String actualThousandsSeparator = actual.get("thousandsSeparator");
        String actualDateFormat = actual.get("dateFormat");
        String actualActive = actual.get("active");
        String actualAreaCode = actual.get("areaCode");
        String actualType = actual.get("type");

        String expectedDialingCode = map.get("dialingCode");
        String expectedDateFormat = map.get("dateFormatting");
        String expectedActive = map.get("active");
        String expectedType = map.get("typeValue");

        logger.log("Actual abbreviations from API: " + actualAbbreviations);
        logger.log("Expected two digit abbreviation: " + twoDigitAbbreviation);
        logger.log("Expected three digit abbreviation: " + threeDigitAbbreviation);
        logger.log("Actual dialingCode from API: " + actualDialingCode);
        logger.log("Expected dialingCode: " + expectedDialingCode);
        logger.log("Actual timeSeparator from API: " + actualTimeSeparator);
        logger.log("Expected timeSeparator: " + timeSeparatorHard);
        logger.log("Actual decimalSeparator from API: " + actualDecimalSeparator);
        logger.log("Expected decimalSeparator: " + decimalSeparatorHard);
        logger.log("Actual thousandsSeparator from API: " + actualThousandsSeparator);
        logger.log("Expected thousandsSeparator: " + thousandSeparatorHard);
        logger.log("Actual dateFormat from API: " + actualDateFormat);
        logger.log("Expected dateFormat: " + expectedDateFormat);
        logger.log("Actual active from API: " + actualActive);
        logger.log("Expected active: " + expectedActive);
        logger.log("Actual areaCode from API: " + actualAreaCode);
        logger.log("Expected areaCode: " + editedAreaCodeName);
        logger.log("Actual type from API: " + actualType);
        logger.log("Expected type: " + expectedType);

        isValid &= actualAbbreviations != null
                && actualAbbreviations.contains(twoDigitAbbreviation)
                && actualAbbreviations.contains(threeDigitAbbreviation);
        isValid &= Objects.equals(actualDialingCode, expectedDialingCode);
        isValid &= Objects.equals(actualTimeSeparator, timeSeparatorHard);
        isValid &= Objects.equals(actualDecimalSeparator, decimalSeparatorHard);
        isValid &= Objects.equals(actualThousandsSeparator, thousandSeparatorHard);
        isValid &= Objects.equals(actualDateFormat, expectedDateFormat);
        isValid &= Objects.equals(actualActive, expectedActive);
        isValid &= Objects.equals(actualAreaCode, editedAreaCodeName);
        isValid &= Objects.equals(actualType, expectedType);

        logger.log("Final General Config Area API validation result: " + isValid);
        return isValid;
    }

    @Setter @Getter
    private String areaCodeName, englishName, areaTypeCountry, twoDigitAbbreviation, threeDigitAbbreviation, editedAreaCodeName;
}