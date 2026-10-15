package com.tiket.web.dashboard.page.platform.discovery;

import com.tiket.api.discovery.LanguageApi;
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
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class LanguagePage<T extends WebDriver> extends BasePage<T> {

    public LanguagePage(Session<T> session) {
        super(session);
    }

    ILogger logger = BaseTest.mainLogger.get();

    private static final String LANGUAGE_URL = "/admin/home/v2/general-config/language";
    private static final int COL_ISO1 = 0;
    private static final int COL_ISO2 = 1;
    private static final int COL_PRIMARY_CODE = 2;
    private static final int COL_EN_NAME = 3;
    private static final int COL_NATIVE_NAME = 4;
    private static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", By.id("search-langugae"));
    private static final Locator EMAIL_LABEL = new Locator("EMAIL_LABEL", By.xpath("//div[contains(@class,'TopNav_top_nav_actions')]"));
    private static final Locator BREADCRUMB_LANGUAGE = new Locator("BREADCRUMB_LANGUAGE", By.xpath("//p[text()='Language']"));
    private static final Locator ADD_LANGUAGE_BUTTON = new Locator("ADD_LANGUAGE_BUTTON", By.xpath("//button[normalize-space(.)='Add Language']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[normalize-space(.)='Submit']"));
    private static final Locator LANGUAGE_EN_NAME_INPUT = new Locator("LANGUAGE_EN_NAME_INPUT", By.xpath("//input[@id='english-name-for-the-language']"));
    private static final Locator SNACKBAR_ELEMENT = new Locator("SNACKBAR", By.xpath("//div[contains(@class,'SnackbarsPositioner_snackbars_positioner')]"));
    private static final Locator SEARCH_RESULT = new Locator("SEARCH_RESULT", By.xpath("//tr[contains(@class, 'style_general_config_wrapper')][1]"));
    private static final Locator TABLE_CELL = new Locator("TABLE_CELL", By.tagName("td"));
    private static final Locator FIRST_ROW_EDIT_BUTTON = new Locator("FIRST_ROW_EDIT_BUTTON", By.xpath("//tr[contains(@class, 'style_general_config_wrapper')][1]//span[@data-testid='button-edit']"));
    private static final Locator EDIT_MODAL = new Locator("EDIT_MODAL", By.xpath("//p[text()='Add Language']"));
    private static final Locator GET_IN_BUTTON = new Locator("GET_IN_BUTTON", By.xpath("//button[normalize-space(.)='GET IN']"));

    private Locator verifyElementAppear(String text) {
        return new Locator("VERIFY_TEXT", By.xpath(String.format("//p[text()='%s']", text)));
    }

    public void goToLanguagePage(String username, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(LANGUAGE_URL);
        handleGetInPage();
        driverTools.waitForElementToAppear(ADD_LANGUAGE_BUTTON, 10);
        logger.log("Navigated to Language URL. Current URL: " + driverTools.getCurrentURL());
    }

    public void navigateBackToLanguagePage() {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.goToPage(LANGUAGE_URL);
        driverTools.waitForElementToAppear(ADD_LANGUAGE_BUTTON, 10);
        logger.log("Navigated back to Language list page");
    }

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

    public boolean isLanguagePageLoaded(String email) {
        driverTools.waitForElementToAppear(BREADCRUMB_LANGUAGE, 5);
        boolean checkBreadCrumb = driverTools.isElementDisplayed(BREADCRUMB_LANGUAGE);
        boolean checkSearchBar  = driverTools.isElementDisplayed(SEARCH_BAR);
        boolean checkButton     = driverTools.isElementDisplayed(ADD_LANGUAGE_BUTTON);
        boolean checkEmail      = driverTools.getText(EMAIL_LABEL).isPresent() && !driverTools.getText(EMAIL_LABEL).get().isEmpty();

        logger.log("checkBreadCrumb: " + checkBreadCrumb);
        logger.log("checkSearchBar: "  + checkSearchBar);
        logger.log("checkButton: "     + checkButton);
        logger.log("checkEmail: "      + checkEmail);

        return checkButton && checkBreadCrumb && checkSearchBar && checkEmail;
    }

    public boolean isSnackbarVisible() {
        try {
            driverTools.waitForElementToAppear(SNACKBAR_ELEMENT, 5);
            boolean visible = driverTools.isElementDisplayed(SNACKBAR_ELEMENT);
            driverTools.waitForElementToDisappear(SNACKBAR_ELEMENT, 5);
            return visible;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, String> searchAndExtract(String searchText) {
        driverTools.waitForElementToDisappear(SNACKBAR_ELEMENT);
        inputStringOneAtTime(SEARCH_BAR, searchText, SEARCH_RESULT);
        driverTools.waitForElementToAppear(verifyElementAppear(searchText), 5);

        if (!driverTools.isElementDisplayed(verifyElementAppear(searchText))) {
            Assertion.fail("Language not found in search results: " + searchText);
        }
        WebElement firstRow = driverTools.element(SEARCH_RESULT).orElseThrow();
        List<WebElement> columns = driverTools.elements(TABLE_CELL, firstRow).orElseThrow();
        List<String> row = new ArrayList<>();
        for (WebElement td : columns) {
            row.add(td.getText());
        }
        logger.log("Search Result: " + row);

        Map<String, String> data = new LinkedHashMap<>();
        data.put("iso1", row.size() > COL_ISO1 ? row.get(COL_ISO1) : "");
        data.put("iso2", row.size() > COL_ISO2 ? row.get(COL_ISO2) : "");
        data.put("primaryCode", row.size() > COL_PRIMARY_CODE ? row.get(COL_PRIMARY_CODE) : "");
        data.put("enName", row.size() > COL_EN_NAME ? row.get(COL_EN_NAME) : "");
        data.put("nativeName", row.size() > COL_NATIVE_NAME ? row.get(COL_NATIVE_NAME) : "");
        logger.log("Extracted row data: " + data);
        return data;
    }

    public void clickEditAndUpdateEnName(String newEnName) {
        driverTools.click(FIRST_ROW_EDIT_BUTTON);
        driverTools.waitForElementToPresence(EDIT_MODAL, 2);
        driverTools.deleteUsingBackspace(LANGUAGE_EN_NAME_INPUT);
        driverTools.sendKeys(LANGUAGE_EN_NAME_INPUT, newEnName);

        driverTools.click(SUBMIT_BUTTON);
        driverTools.waitForElementToPresence(SNACKBAR_ELEMENT, 2);
        logger.log("Edited English name to: " + newEnName);
    }

    public Map<String, String> getLanguageDataFromAPI(String languageId) throws ExecutionException, InterruptedException {
        Map<String, String> result = new HashMap<>();

        LanguageApi api = new LanguageApi(languageId);
        ApiResponse apiResponse = api.hitApi(apiTools);
        JSONObject responseData = new JSONObject(apiResponse.getBody());

        String code = responseData.getString("code");
        result.put("code", code);

        if (!responseData.isNull("data")) {
            JSONObject data = responseData.getJSONObject("data");
            result.put("id", data.optString("id", ""));
            result.put("englishName", data.optString("englishName", ""));
            result.put("nativeName", data.optString("nativeName", ""));
            result.put("iso6392Code", data.optString("iso6392Code", ""));
            result.put("iso6391Code", data.optString("iso6391Code", ""));
            result.put("dialectCode", data.optString("dialectCode", ""));
            result.put("primaryCode", data.optString("primaryCode", ""));
        }

        logger.log("Language API Response: " + responseData.toString(4));
        return result;
    }

    private void waitForTooManyRequest() {
        if (driverTools.isElementDisplayed(SNACKBAR_ELEMENT)) {
            String txt = driverTools.getText(SNACKBAR_ELEMENT).isPresent()
                    ? driverTools.getText(SNACKBAR_ELEMENT).get() : "";
            if (txt.contains("429")) {
                driverTools.waitForElementToDisappear(SNACKBAR_ELEMENT, 5);
            }
        }
    }

    public void inputStringOneAtTime(Locator locator, String textToInput, Locator... waitElement) {
        WebElement e = driverTools.element(locator).orElseThrow();
        List<Locator> l = Arrays.stream(waitElement).toList();
        logger.log(String.format("Typing '%s' into '%s'...", textToInput, locator.name()));
        int splitIndex = Math.max(0, textToInput.length() - 5);
        String firstPart  = textToInput.substring(0, splitIndex);
        String secondPart = textToInput.substring(splitIndex);

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
        logger.log(String.format("Sent '%s' to '%s'", textToInput, locator.name()));
    }

    @Getter @Setter
    private String languageEnName, languageNativeName;
}