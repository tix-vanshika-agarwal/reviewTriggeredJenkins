package com.tiket.web.dashboard.page.platform.discovery;

import com.tiket.api.discovery.EntryPointInfoPageApi;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.nio.file.Paths;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

public class EntryPointInfoPage<T extends WebDriver> extends BasePage<T> {

    public EntryPointInfoPage(Session<T> session) {
        super(session);
    }

    ILogger logger = BaseTest.mainLogger.get();

    private static final String ENTRY_POINT_URL = "/admin/home/v2/entry-point";
    private static final Locator GET_IN_BTN = new Locator("GET_IN_BTN", By.xpath("//button[normalize-space(.)='GET IN']"));
    private static final Locator REGION_SELECT_BTN = new Locator("REGION_SELECT_BTN", By.xpath("//span[@data-testid='placeholder-select-region']"));
    private static final Locator INDONESIA_OPTION = new Locator("INDONESIA_OPTION", By.xpath("//div[contains(@id,'react-select') and contains(@id,'-option-') and normalize-space(text())='INDONESIA']"));
    private static final Locator EMAIL_LABEL = new Locator("EMAIL_LABEL", By.xpath("//div[contains(@class,'TopNav_top_nav_actions')]"));
    private static final Locator ADD_NEW_TAB_BUTTON = new Locator("ADD_NEW_TAB_BUTTON", By.xpath("//span[text()='Add New Tab']"));
    private static final Locator ID_TAB = new Locator("ID_TAB", By.xpath("//button[contains(@class,'Tabs_tab')][.//span[normalize-space(text())='ID']]"));
    private static final Locator TAB_NAME_EN_INPUT = new Locator("TAB_NAME_EN_INPUT", By.xpath("//input[@id='enter-name' and @name='EN']"));
    private static final Locator TAB_NAME_ID_INPUT = new Locator("TAB_NAME_ID_INPUT", By.xpath("//input[@id='enter-name' and @name='ID']"));
    private static final Locator TAB_TITLE_EN_INPUT = new Locator("TAB_TITLE_EN_INPUT", By.xpath("//input[@id='title-name-en']"));
    private static final Locator TAB_TITLE_ID_INPUT = new Locator("TAB_TITLE_ID_INPUT", By.xpath("//input[@id='title-name-id']"));
    private static final Locator DONE_BUTTON = new Locator("DONE_BUTTON", By.xpath("//span[text()='Done']"));
    private static final Locator SNACKBAR_ELEMENT = new Locator("SNACKBAR_ELEMENT", By.xpath("//div[contains(@class,'SnackbarsPositioner_snackbars_positioner')]"));
    private static final Locator YES_DELETE_BUTTON = new Locator("YES_DELETE_BUTTON", By.xpath("//span[text()='Yes']"));
    private static final Locator POP_UP_ACTION = new Locator("POP_UP_ACTION", By.xpath("//footer[contains(@class,'Modal_modal_footer')]"));
    private static final Locator NEW_INFO_PAGE_BUTTON = new Locator("NEW_INFO_PAGE_BUTTON", By.xpath("//span[text()='New Info Page List']"));
    private static final Locator INFO_TITLE_EN_INPUT = new Locator("INFO_TITLE_EN_INPUT", By.xpath("(//div[contains(@class,'Tabs_tab_panel_active')]//input[@id='content-title'])"));
    private static final Locator INFO_TITLE_ID_INPUT = new Locator("INFO_TITLE_ID_INPUT", By.xpath("(//div[contains(@class,'Tabs_tab_panel_active')]//input[@id='content-title'])"));
    private static final Locator INFO_DESC_EN_INPUT = new Locator("INFO_DESC_EN_INPUT", By.xpath("//div[contains(@class,'Tabs_tab_panel_active')]//textarea[@id='content-description-optional']"));
    private static final Locator INFO_DESC_ID_INPUT = new Locator("INFO_DESC_ID_INPUT", By.xpath("//div[contains(@class,'Tabs_tab_panel_active')]//textarea[@id='content-description-optional']"));
    private static final Locator INFO_PAGE_DROPDOWN = new Locator("INFO_PAGE_DROPDOWN", By.xpath("//input[@id='select-page-name']"));
    private static final Locator ICON_UPLOAD_INPUT = new Locator("ICON_UPLOAD_INPUT", By.xpath("//input[@name='iconUrl']"));
    private static final Locator INFO_SUBMIT_BTN = new Locator("INFO_SUBMIT_BTN", By.xpath("//span[text()='Submit']"));
    private static final Locator EDIT_INFO_PAGE_BUTTON = new Locator("EDIT_INFO_PAGE_BUTTON", By.xpath("(//tr[@role='button'])[last()]//p[normalize-space()='Edit']"));

    private Locator lastTabRow() {
        return new Locator("LAST_TAB_ROW", By.xpath("(//tr[contains(@class, 'entry-tab-sort-item')])[last()]"));
    }

    private Locator lastTabEditButton() {
        return new Locator("LAST_TAB_EDIT_BUTTON", By.xpath("//tr[@role='button'][last()]//div[contains(@class,'styles_ic_edit')]"));
    }

    private Locator lastTabDeleteButton() {
        return new Locator("LAST_TAB_DELETE_BUTTON", By.xpath("//tr[@role='button'][last()]//div[contains(@class,'styles_ic_delete')]"));
    }

    private Locator verifyElementAppear(String text) {
        return new Locator("VERIFY_TEXT", By.xpath(String.format("//*[contains(text(),'%s')]", text)));
    }

    private Locator pageNameOption(String pageName) {
        return new Locator("PAGE_NAME_OPTION", By.xpath(String.format("//input[@data-pagename='%s']/following-sibling::label[1]", pageName)));
    }

    private void handleRegionSelectionIfPresent() {
        try {
            driverTools.waitForElementToAppear(REGION_SELECT_BTN, 3);
            if (!driverTools.isElementDisplayed(REGION_SELECT_BTN)) {
                logger.log("Region selector is not displayed; skipping region selection");
                return;
            }
            String currentRegion = driverTools.getText(REGION_SELECT_BTN).orElse("").trim();
            if (!currentRegion.equalsIgnoreCase("INDONESIA")) {
                driverTools.click(REGION_SELECT_BTN);
                driverTools.waitForElementToAppear(INDONESIA_OPTION, 5);
                driverTools.click(INDONESIA_OPTION);
                logger.log("Region was '" + currentRegion + "', switched to INDONESIA");
            } else {
                logger.log("INDONESIA already selected");
            }
        } catch (Exception e) {
            logger.log("Region selector was not present; continuing without " + "region selection. Reason: " + e.getMessage());
        }
    }

    public void goToEntryPointPage(String username, String password) {
        driverTools.maximizeWindow();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(ENTRY_POINT_URL);
        handleRegionSelectionIfPresent();
        logger.log("Navigated to Entry Point URL. Current URL: " + driverTools.getCurrentURL());
    }

    public void clickGetInButton() {
        if (driverTools.waitForElementToBeClickable(GET_IN_BTN).isEmpty()) {
            throw new NoSuchElementException("GET IN button was not found or is not clickable. " + "Current URL: " + driverTools.getCurrentURL());
        }
        try {
            driverTools.click(GET_IN_BTN);
            logger.log("Clicked GET IN button");
        } catch (Exception e) {
            logger.log("Normal click on GET IN failed. " + "Trying DriverTools JavaScript fallback. " + "Reason: " + e.getMessage());
            driverTools.clickWithJavascriptExecutor(GET_IN_BTN);
            logger.log("Clicked GET IN button using fallback");
        }
    }

    public boolean isEntryPointPageLoaded(String email) {
        driverTools.waitForElementToAppear(ADD_NEW_TAB_BUTTON, 10);
        boolean checkAddButton = driverTools.isElementDisplayed(ADD_NEW_TAB_BUTTON);
        boolean getInStillDisplayed = driverTools.isElementDisplayed(GET_IN_BTN);
        boolean checkEmail = driverTools.getText(EMAIL_LABEL).map(text -> !text.trim().isEmpty()).orElse(false);
        logger.log("Current URL: " + driverTools.getCurrentURL());
        logger.log("checkAddButton: " + checkAddButton);
        logger.log("checkEmail: " + checkEmail);
        logger.log("getInStillDisplayed: " + getInStillDisplayed);
        return checkAddButton && checkEmail && !getInStillDisplayed;
    }

    public int getTabRowCount() {
        try {
            List<WebElement> rows = driver.findElements(lastTabRow().by());
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void addNewTab(String prefix) throws InterruptedException {
        driverTools.waitForElementToAppear(ADD_NEW_TAB_BUTTON, 10);
        this.initialTabCount = getTabRowCount();
        logger.log("Initial tab count: " + this.initialTabCount);
        driverTools.click(ADD_NEW_TAB_BUTTON);
        driverTools.waitForElementToAppear(TAB_NAME_EN_INPUT, 5);
        long suffix = CoreFunctions.randomInt(5);
        this.tabNameEN = prefix + "_EN_" + suffix;
        this.tabNameID = prefix + "_ID_" + suffix;
        driverTools.sendKeys(TAB_NAME_EN_INPUT, this.tabNameEN);
        driverTools.sendKeys(TAB_NAME_ID_INPUT, this.tabNameID);
        driverTools.waitForElementToBeClickable(DONE_BUTTON);
        driverTools.click(DONE_BUTTON);
        logger.log("Added new tab — EN: " + this.tabNameEN + ", ID: " + this.tabNameID);
    }

    public boolean verifyTabAddedInUI() {
        driverTools.waitForElementToAppear(lastTabRow(), 5);
        try {
            driverTools.scrollIntoWebView(lastTabRow());
        } catch (Exception e) {
            logger.log("Could not scroll to last tab row: " + e.getMessage());
        }
        boolean tabVisible = driverTools.isElementDisplayed(verifyElementAppear(this.tabNameEN));
        logger.log("Tab visible in UI: " + tabVisible + " (looking for: " + this.tabNameEN + ")");
        return tabVisible;
    }

    public void clickEditOnLastTab() {
        driverTools.waitForElementToAppear(lastTabEditButton(), 5);
        driverTools.clickWithJavascriptExecutor(lastTabEditButton());
        driverTools.waitForElementToAppear(NEW_INFO_PAGE_BUTTON, 10);
        String currentUrl = driverTools.getCurrentURL();
        String[] urlParts = currentUrl.split("/");
        this.tabIndex = urlParts[urlParts.length - 1];
        logger.log("Entered tab detail page. Tab index: " + this.tabIndex);
    }

    public void addInfoPageInTab(String prefix, String imagePath, String pageName) throws InterruptedException {
        driverTools.waitForElementToAppear(NEW_INFO_PAGE_BUTTON, 5);
        driverTools.click(NEW_INFO_PAGE_BUTTON);
        long suffix = CoreFunctions.randomInt(5);
        this.infoPageTitleEN = prefix + "_EN_" + suffix;
        this.infoPageTitleID = prefix + "_ID_" + suffix;
        this.infoPageDescEN = prefix + "_DescEN_" + suffix;
        this.infoPageDescID = prefix + "_DescID_" + suffix;
        try {
            driverTools.waitForElementToPresence(ICON_UPLOAD_INPUT, 5);
            String absoluteImagePath = Paths.get(imagePath).toAbsolutePath().toString();
            driverTools.sendKeys(ICON_UPLOAD_INPUT, absoluteImagePath);
            Thread.sleep(2000);
            logger.log("Uploaded icon: " + imagePath);
        } catch (Exception e) {
            logger.log("Icon upload failed or not required: " + e.getMessage());
        }
        driverTools.waitForElementToAppear(INFO_TITLE_EN_INPUT);
        driverTools.sendKeys(INFO_TITLE_EN_INPUT, this.infoPageTitleEN);
        driverTools.sendKeys(INFO_DESC_EN_INPUT, this.infoPageDescEN);
        driverTools.click(ID_TAB);
        Thread.sleep(500);
        driverTools.waitForElementToAppear(INFO_TITLE_ID_INPUT, 5);
        driverTools.sendKeys(INFO_TITLE_ID_INPUT, this.infoPageTitleID);
        driverTools.sendKeys(INFO_DESC_ID_INPUT, this.infoPageDescID);
        driverTools.scrollIntoWebView(INFO_PAGE_DROPDOWN);
        Thread.sleep(500);
        driverTools.scrollIntoWebView(INFO_PAGE_DROPDOWN);
        driverTools.waitForElementToAppear(INFO_PAGE_DROPDOWN, 5);
        driverTools.click(INFO_PAGE_DROPDOWN);
        driverTools.sendKeys(INFO_PAGE_DROPDOWN, pageName);
        driverTools.waitForElementToAppear(pageNameOption(pageName), 5);
        driverTools.click(pageNameOption(pageName));
        Thread.sleep(500);
        String selectedValue = driver.findElement(INFO_PAGE_DROPDOWN.by()).getAttribute("value");
        if (!pageName.equals(selectedValue)) {
            throw new IllegalStateException("Page name dropdown selection failed. Expected: '" + pageName + "', but input value is: '" + selectedValue + "'");
        }
        logger.log("Page name selection confirmed: " + pageName);
        driverTools.waitForElementToBeClickable(INFO_SUBMIT_BTN);
        driverTools.click(INFO_SUBMIT_BTN);
        Thread.sleep(500);
        logger.log("Added info page — EN: " + this.infoPageTitleEN + ", ID: " + this.infoPageTitleID);
    }

    public void editInfoPageInTab(String editSuffix) {
        driverTools.waitForElementToAppear(EDIT_INFO_PAGE_BUTTON, 5);
        driverTools.click(EDIT_INFO_PAGE_BUTTON);
        driverTools.waitForElementToAppear(TAB_TITLE_EN_INPUT, 5);
        this.infoPageTitleEN = this.infoPageTitleEN + "_" + editSuffix;
        deleteUsingBackspace(TAB_TITLE_EN_INPUT);
        driverTools.sendKeys(TAB_TITLE_EN_INPUT, this.infoPageTitleEN);
        this.infoPageTitleID = this.infoPageTitleID + "_" + editSuffix;
        deleteUsingBackspace(TAB_TITLE_ID_INPUT);
        driverTools.sendKeys(TAB_TITLE_ID_INPUT, this.infoPageTitleID);
        logger.log("Edited info page — EN: " + this.infoPageTitleEN + ", ID: " + this.infoPageTitleID);
        driverTools.clickWithJavascriptExecutor(INFO_SUBMIT_BTN);
    }

    public void navigateBackToEntryPoint() {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.goToPage(ENTRY_POINT_URL);
        driverTools.waitForElementToAppear(ADD_NEW_TAB_BUTTON, 10);
        logger.log("Navigated back to entry point tab list");
    }

    public void deleteLastTab() {
        driverTools.waitForElementToAppear(lastTabDeleteButton(), 5);
        driverTools.clickWithJavascriptExecutor(lastTabDeleteButton());
        driverTools.waitForElementToAppear(POP_UP_ACTION, 3);
        driverTools.waitForElementToAppear(YES_DELETE_BUTTON, 3);
        driverTools.click(YES_DELETE_BUTTON);
        driverTools.waitForElementToDisappear(POP_UP_ACTION, 5);
        logger.log("Deleted the last tab");
    }

    public boolean verifyTabDeletedInUI() {
        try {
            driverTools.waitForElementToDisappear(SNACKBAR_ELEMENT, 5);
        } catch (Exception e) {
            // Snackbar might already be gone
        }
        int currentCount = getTabRowCount();
        boolean deleted = currentCount == this.initialTabCount;
        logger.log("Tab deleted check — initial: " + this.initialTabCount + ", current: " + currentCount + ", deleted: " + deleted);
        return deleted;
    }

    private JSONObject callEntryPointApi() throws ExecutionException, InterruptedException {
        if (tabIndex == null || tabIndex.isEmpty()) {
            throw new IllegalStateException("tabIndex is not set — click edit on a tab first");
        }
        EntryPointInfoPageApi api = new EntryPointInfoPageApi(tabIndex);
        ApiResponse apiResponse = api.hitApi(apiTools);
        return new JSONObject(apiResponse.getBody());
    }

    public Map<String, String> getEntryPointDataFromAPI() throws ExecutionException, InterruptedException {
        Map<String, String> result = new HashMap<>();
        JSONObject responseData = callEntryPointApi();
        String responseResult = responseData.toString(4);
        String code = responseData.getString("code");
        result.put("response", responseResult);
        result.put("code", code);
        if (responseData.isNull("data")) {
            result.put("hasData", "false");
            result.put("infoPagesCount", "0");
            return result;
        }
        JSONObject data = responseData.getJSONObject("data");
        result.put("hasData", "true");
        if (data.has("title")) {
            JSONObject title = data.getJSONObject("title");
            result.put("titleEN", title.optString("EN", ""));
            result.put("titleID", title.optString("ID", ""));
        }
        JSONArray infoPages = data.optJSONArray("infoPages");
        int infoPagesCount = (infoPages != null) ? infoPages.length() : 0;
        result.put("infoPagesCount", String.valueOf(infoPagesCount));

        if (infoPagesCount > 0) {
            JSONObject firstInfoPage = infoPages.getJSONObject(0);
            result.put("infoPageName", firstInfoPage.optString("name", ""));
            result.put("infoPageId", firstInfoPage.optString("id", ""));
            result.put("infoPagePageName", firstInfoPage.optString("pageName", ""));
            result.put("infoPageStatus", firstInfoPage.optString("status", ""));
            result.put("infoPageSlug", firstInfoPage.optString("slug", ""));
            result.put("infoPageIconUrl", firstInfoPage.optString("iconUrl", ""));

            if (firstInfoPage.has("content")) {
                JSONObject content = firstInfoPage.getJSONObject("content");
                if (content.has("EN")) {
                    result.put("contentTitleEN", content.getJSONObject("EN").optString("title", ""));
                    result.put("contentDescEN", content.getJSONObject("EN").optString("description", ""));
                }
                if (content.has("ID")) {
                    result.put("contentTitleID", content.getJSONObject("ID").optString("title", ""));
                    result.put("contentDescID", content.getJSONObject("ID").optString("description", ""));
                }
            }
        }
        logger.log("API Response: " + responseResult);
        return result;
    }

    public Map<String, String> getEntryPointDataAfterDeletion() throws ExecutionException, InterruptedException {
        Map<String, String> result = new HashMap<>();

        JSONObject responseData = callEntryPointApi();
        String code = responseData.getString("code");
        String message = responseData.getString("message");

        result.put("code", code);
        result.put("message", message);
        result.put("hasData", String.valueOf(!responseData.isNull("data")));

        logger.log("API after deletion — code: " + code + ", message: " + message);
        return result;
    }

    private void deleteUsingBackspace(Locator locator) {
        Actions actions = new Actions(driver);
        WebElement e = driver.findElement(locator.by());

        String textToDelete = e.getAttribute("value");
        logger.log(String.format("Trying to delete '%s' from locator '%s' using backspace...", textToDelete, locator.name()));
        e.click();
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            actions.keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND).perform();
        } else {
            actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform();
        }
        actions.sendKeys(Keys.BACK_SPACE).perform();
        logger.log("Deleted successfully");
    }

    public boolean isSnackbarVisible() {
        try {
            driverTools.waitForElementToAppear(SNACKBAR_ELEMENT, 5);
            return driverTools.isElementDisplayed(SNACKBAR_ELEMENT);
        } catch (Exception e) {
            return false;
        }
    }

    @Getter @Setter
    private String tabNameEN, tabNameID, tabIndex;

    @Getter @Setter
    private String infoPageTitleEN, infoPageTitleID, infoPageDescEN, infoPageDescID;

    @Getter @Setter
    private int initialTabCount;
}