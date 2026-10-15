package com.tiket.web.dashboard.page.platform.discovery;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DynamicLandingPage<T extends WebDriver> extends BasePage<T> {
    public DynamicLandingPage(Session<T> session) {
        super(session);
    }

    private static final String DLP_PAGE = "/admin/home/v2";
    private static final String DLP_LIST_PAGE = "/admin/home/v2/dynamic-landing-page";
    private static final String DLP_TABLE_ROW_XPATH = "//table[contains(@class,'Table')]//tbody/tr[count(td)>=4]";

    private static final Locator GET_IN_BUTTON = new Locator("GET_IN_BUTTON",
            By.xpath("//button[normalize-space(.)='GET IN']"));
    private static final Locator HOME_DASHBOARD_MARKER = new Locator("HOME_DASHBOARD_MARKER",
            By.xpath("//div[contains(@class,'TopNav_top_nav_inner')]"));
    private static final Locator HOME_SIDEBAR = new Locator("HOME_SIDEBAR",
            By.xpath("//span[normalize-space(.)='Dynamic Landing Page']"));
    private static final Locator DYNAMIC_LANDING_PAGE_MENU = new Locator("DYNAMIC_LANDING_PAGE_MENU",
            By.xpath("//span[normalize-space()='Dynamic Landing Page']"));
    private static final Locator DYNAMIC_LANDING_TAB_MENU = new Locator("DYNAMIC_LANDING_TAB_MENU",
            By.xpath("//p[normalize-space()='Dynamic Landing Page']"));
    private static final Locator ADD_NEW_PAGE_BUTTON = new Locator("ADD_NEW_PAGE_BUTTON",
            By.xpath("//button[contains(@class,'Button_button') and normalize-space()='Add New Page']"));
    private static final Locator GENERAL_CONTENT_PAGE = new Locator("GENERAL_CONTENT_PAGE",
            By.xpath("//p[contains(@class,'Text_text') and normalize-space()='General Content']"));
    private static final Locator PAGE_NAME_INPUT = new Locator("PAGE_NAME_INPUT", By.id("page-name"));
    private static final Locator PAGE_URL_SLUG_INPUT = new Locator("PAGE_URL_SLUG_INPUT", By.id("page-slug"));
    private static final Locator META_TITLE_EN = new Locator("META_TITLE_EN", By.id("title-en"));
    private static final Locator META_DESCRIPTION_EN = new Locator("META_DESCRIPTION_EN", By.id("description-en"));
    private static final Locator META_TITLE_ID = new Locator("META_TITLE_ID", By.id("title-id"));
    private static final Locator META_DESCRIPTION_ID = new Locator("META_DESCRIPTION_ID", By.id("description-id"));
    private static final Locator UPLOAD_BANNER_MOBILE_EN = new Locator("UPLOAD_BANNER_MOBILE_EN",
            By.xpath("(//div[contains(@class,'styles_icon_container')]//input[@type='file'])[1]"));
    private static final Locator UPLOAD_BANNER_DESKTOP_EN = new Locator("UPLOAD_BANNER_DESKTOP_EN",
            By.xpath("(//div[contains(@class,'styles_icon_container')]//input[@type='file'])[2]"));
    private static final Locator UPLOAD_BANNER_MOBILE_ID = new Locator("UPLOAD_BANNER_MOBILE_ID",
            By.xpath("(//div[contains(@class,'styles_icon_container')]//input[@type='file'])[3]"));
    private static final Locator UPLOAD_BANNER_DESKTOP_ID = new Locator("UPLOAD_BANNER_DESKTOP_ID",
            By.xpath("(//div[contains(@class,'styles_icon_container')]//input[@type='file'])[4]"));
    private static final Locator OVERLAY_COLOR_HEX = new Locator("OVERLAY_COLOR_HEX", By.id("hex-color"));
    private static final Locator TEXT_COLOR_PICK = new Locator("TEXT_COLOR_PICK",
            By.xpath("//div[contains(@class,'styles_radio')]//span[normalize-space()='Black']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON",
            By.xpath("//button[contains(@class,'Button_button') and normalize-space()='Submit']"));
    private static final Locator LOADING_ICON = new Locator("LOADING_ICON", By.cssSelector(".overlay-loading.show"));
    private static final Locator SEARCH_PAGE_INPUT = new Locator("SEARCH_PAGE_INPUT",
            By.xpath("//input[contains(@placeholder,'Search Page Name')]"));
    private static final Locator PERIOD_PAGE_TOGGLE = new Locator("PERIOD_PAGE_TOGGLE",
            By.xpath("//span[contains(@class,'Radio_children') and normalize-space()='Always ON']"));
    private static final Locator TOGGLE_ACTIVE = new Locator("TOGGLE_ACTIVE",
            By.xpath("//div[contains(@class,'Toggle_toggle')]"));
    private static final Locator ARCHIVE_BUTTON = new Locator("ARCHIVE_BUTTON",
            By.xpath("//p[contains(@class,'Text_text') and normalize-space()='Archive']"));
    private static final Locator CONFIRMATION_ARCHIVE_BUTTON = new Locator("CONFIRMATION_ARCHIVE_BUTTON",
            By.xpath("//button[contains(@class,'Button_button') and normalize-space()='Archive']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR",
            By.xpath("//div[contains(@class,'Snackbar_text_content') and normalize-space()='Successfully archived page.']"));

    private String page_name;
    private String slug_input;

    private Locator latestCreatedDlpPageName(String pageName) {
        return new Locator("LATEST_CREATED_DLP_PAGE_NAME",
                By.xpath(String.format("%s[.//td[normalize-space()='%s']]/td[2]", DLP_TABLE_ROW_XPATH, pageName)));
    }

    private Locator editButtonForPageName(String pageName) {
        return new Locator("EDIT_DLP_FOR_" + pageName,
                By.xpath(String.format("%s[.//td[normalize-space()='%s']]//td[last()]//a[1]",
                        DLP_TABLE_ROW_XPATH, pageName)));
    }

    private Locator moreMenuForPageName(String pageName) {
        return new Locator("MORE_MENU_FOR_" + pageName,
                By.xpath(String.format("%s[.//td[normalize-space()='%s']]//*[local-name()='path' and contains(@id,'more-horizontal')]/ancestor::*[local-name()='svg']/ancestor::div[1]",
                        DLP_TABLE_ROW_XPATH, pageName)));
    }

    public void goToDynamicLandingPage(String username, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(DLP_PAGE);

        if (!driverTools.getCurrentURL().contains(DLP_PAGE)) {
            memberLoginPage.goToPage(DLP_PAGE);
        }
    }

    public boolean isHomePageLoaded() {
        driverTools.waitForElementToAppear(HOME_DASHBOARD_MARKER, 5);
        driverTools.waitForElementToAppear(HOME_SIDEBAR, 5);
        boolean dashboardDisplayed = driverTools.isElementDisplayed(HOME_DASHBOARD_MARKER);
        boolean dynamicLandingMenuDisplayed = driverTools.isElementDisplayed(HOME_SIDEBAR);
        return dashboardDisplayed && dynamicLandingMenuDisplayed;
    }

    public void clickGetInButton() {
        driverTools.waitForElementToAppear(GET_IN_BUTTON, 5);
        if (driverTools.isElementDisplayed(GET_IN_BUTTON)) {
            if (driverTools.waitForElementToBeClickable(GET_IN_BUTTON).isEmpty()) {
                throw new java.util.NoSuchElementException("GET IN button was found but is not clickable. Current URL: " + driverTools.getCurrentURL());
            }
            try {
                driverTools.click(GET_IN_BUTTON);
            } catch (Exception e) {
                driverTools.clickWithJavascriptExecutor(GET_IN_BUTTON);
            }
        }
    }

    public void clickDynamicLandingPageSideMenu() {
        int tabs = driver.getWindowHandles().size();
        driverTools.click(DYNAMIC_LANDING_PAGE_MENU);
        try {
            driverTools.waitForNewTabAndSwitchToLatest(tabs);
        } catch (TimeoutException ignored) {
            if (driver.getWindowHandles().size() > tabs) {
                driverTools.switchToLatestTab();
            }
        }
    }

    public boolean isDynamicLandingPageLoaded() {
        driverTools.waitForElementToAppear(DYNAMIC_LANDING_TAB_MENU, 15);
        return driverTools.isElementDisplayed(DYNAMIC_LANDING_TAB_MENU);
    }

    public void clickAddNewPage() {
        driverTools.waitForElementToPresence(ADD_NEW_PAGE_BUTTON);
        driverTools.click(ADD_NEW_PAGE_BUTTON);
    }

    public boolean isGeneralContentPageLoaded() {
        driverTools.waitForElementToAppear(GENERAL_CONTENT_PAGE, 2);
        return driverTools.isElementDisplayed(GENERAL_CONTENT_PAGE);
    }

    public void turnOnToggleActive() {
        driverTools.clickWithJavascriptExecutor(TOGGLE_ACTIVE);
    }

    public void clickPeriodPageToggle() {
        driverTools.clickWithJavascriptExecutor(PERIOD_PAGE_TOGGLE);
    }

    public void inputPageName() {
        String randomCodeString = "automation-" + CoreFunctions.generateRandomString(7);
        driverTools.click(PAGE_NAME_INPUT);
        driverTools.sendKeys(PAGE_NAME_INPUT, randomCodeString);
        this.page_name = randomCodeString;
    }

    public void inputSlug() {
        String randomSlugString = "automate-slug-" + CoreFunctions.generateRandomString(5);
        driverTools.click(PAGE_URL_SLUG_INPUT);
        driverTools.sendKeys(PAGE_URL_SLUG_INPUT, randomSlugString);
        this.slug_input = randomSlugString;
    }

    public void fillAllContents(String metaTitleEn, String metaDescriptionEn, String metaTitleId,
                                String metaDescriptionId, String overLayColorHex) {
        driverTools.clickWithJavascriptExecutor(META_TITLE_EN);
        driverTools.sendKeys(META_TITLE_EN, metaTitleEn.trim());
        driverTools.clickWithJavascriptExecutor(META_DESCRIPTION_EN);
        driverTools.sendKeys(META_DESCRIPTION_EN, metaDescriptionEn.trim());
        driverTools.clickWithJavascriptExecutor(META_TITLE_ID);
        driverTools.sendKeys(META_TITLE_ID, metaTitleId.trim());
        driverTools.clickWithJavascriptExecutor(META_DESCRIPTION_ID);
        driverTools.sendKeys(META_DESCRIPTION_ID, metaDescriptionId.trim());
        driverTools.clickWithJavascriptExecutor(OVERLAY_COLOR_HEX);
        driverTools.sendKeys(OVERLAY_COLOR_HEX, overLayColorHex.trim());
    }

    public void uploadIcons(String mobileIcon, String desktopIcon) {
        String mobilePath = new File(mobileIcon).getAbsolutePath();
        String desktopPath = new File(desktopIcon).getAbsolutePath();
        driverTools.scrollIntoWebView(META_TITLE_EN);
        for (Object[] upload : new Object[][]{
                {UPLOAD_BANNER_MOBILE_EN, mobilePath}, {UPLOAD_BANNER_DESKTOP_EN, desktopPath},
                {UPLOAD_BANNER_MOBILE_ID, mobilePath}, {UPLOAD_BANNER_DESKTOP_ID, desktopPath}
        }) {
            Locator input = (Locator) upload[0];
            driverTools.waitForElementToPresence(input, 10);
            driverTools.scrollIntoWebView(input);
            driverTools.sendKeys(input, (String) upload[1]);
            driverTools.waitForElementToDisappear(LOADING_ICON, 5);
        }
    }

    public void chooseTextColor() {
        driverTools.waitForElementToAppear(TEXT_COLOR_PICK, 10);
        driverTools.scrollIntoWebView(TEXT_COLOR_PICK);
        driverTools.clickWithJavascriptExecutor(TEXT_COLOR_PICK);
    }

    public void clickSubmit() {
        driverTools.waitForElementToAppear(SUBMIT_BUTTON, 10);
        driverTools.scrollIntoWebView(SUBMIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
    }

    public void editLatestCreated() {
        searchCreatedDlpInList();
        Locator editButton = editButtonForPageName(page_name);
        driverTools.waitForElementToAppear(editButton, 15);
        driverTools.scrollIntoWebView(editButton);
        int tabs = driver.getWindowHandles().size();
        driverTools.clickWithJavascriptExecutor(editButton);
        if (driver.getWindowHandles().size() > tabs) {
            driverTools.switchToLatestTab();
        }
        waitForInputValue(PAGE_NAME_INPUT);
    }

    public Map<String, String> verifyInputtedData() {
        waitForInputValue(PAGE_NAME_INPUT);
        Map<String, String> result = new HashMap<>();
        waitForInputValue(META_TITLE_EN);
        driverTools.scrollIntoWebView(META_TITLE_EN);
        result.put("metaTitleEn", readInputValue(META_TITLE_EN));
        result.put("metaDescriptionEn", readInputValue(META_DESCRIPTION_EN));
        result.put("metaTitleId", readInputValue(META_TITLE_ID));
        result.put("metaDescriptionId", readInputValue(META_DESCRIPTION_ID));
        result.put("overLayColorHex", readInputValue(OVERLAY_COLOR_HEX));
        result.put("pageName", readInputValue(PAGE_NAME_INPUT));
        result.put("urlSlug", readInputValue(PAGE_URL_SLUG_INPUT));
        return result;
    }

    public boolean isTitleSame() {
        searchCreatedDlpInList();

        Locator pageNameLocator = latestCreatedDlpPageName(page_name);
        driverTools.waitForElementToBeVisible(pageNameLocator);
        return driverTools.getText(pageNameLocator)
                .map(String::trim)
                .map(text -> text.equals(page_name.trim()))
                .orElse(false);
    }

    public void archiveNewCreatedPage() {
        searchCreatedDlpInList();
        Locator moreMenu = moreMenuForPageName(page_name);
        WebElement moreMenuEl = driverTools.waitForElementToPresence(moreMenu, 10).orElseThrow();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'nearest',inline:'end'});", moreMenuEl);
        driverTools.clickWithJavascriptExecutor(moreMenuEl);
        driverTools.waitForElementToPresence(ARCHIVE_BUTTON);
        driverTools.click(ARCHIVE_BUTTON);
        driverTools.waitForElementToPresence(CONFIRMATION_ARCHIVE_BUTTON);
        driverTools.click(CONFIRMATION_ARCHIVE_BUTTON);
    }

    public boolean isSnackbarSuccessAppear(){
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR);
        return driverTools.getText(SUCCESS_SNACKBAR).isPresent();
    }

    public String getslugInput() {
        return this.slug_input;
    }

    public String getPageNameInput() {
        return this.page_name;
    }

    private String readInputValue(Locator locator) {
        WebElement input = driverTools.waitForElementToPresence(locator, 10).orElseThrow();
        Object value = ((JavascriptExecutor) driver).executeScript("return arguments[0].value;", input);
        return value == null ? "" : String.valueOf(value).trim();
    }

    private void waitForInputValue(Locator locator) {
        WebElement input = driverTools.waitForElementToPresence(locator, 15).orElseThrow();
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(d -> {
            try {
                Object value = ((JavascriptExecutor) d).executeScript("return arguments[0].value;", input);
                return value != null && !String.valueOf(value).trim().isBlank();
            } catch (StaleElementReferenceException e) {
                return false;
            }
        });
    }

    private void searchCreatedDlpInList() {
        if (page_name == null || page_name.isBlank()) {
            throw new IllegalStateException("page_name is not set. Call inputPageName() before searching the DLP list.");
        }
        if (!driverTools.isElementDisplayed(SEARCH_PAGE_INPUT)) {
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
                if (driverTools.isElementDisplayed(SEARCH_PAGE_INPUT)) {
                    break;
                }
            }
            if (!driverTools.isElementDisplayed(SEARCH_PAGE_INPUT)) {
                new MemberLoginPage<>(session).goToPage(DLP_LIST_PAGE);
            }
        }
        isDynamicLandingPageLoaded();
        driverTools.waitForElementToAppear(SEARCH_PAGE_INPUT, 15);
        if (!page_name.equals(readInputValue(SEARCH_PAGE_INPUT))) {
            driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(SEARCH_PAGE_INPUT, page_name);
            driverTools.sendKeys(SEARCH_PAGE_INPUT, String.valueOf(Keys.ENTER));
        }
        driverTools.waitForElementToAppear(editButtonForPageName(page_name), 15);
    }
}
