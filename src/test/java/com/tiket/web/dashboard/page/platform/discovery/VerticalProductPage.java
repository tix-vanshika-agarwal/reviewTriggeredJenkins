package com.tiket.web.dashboard.page.platform.discovery;

import com.tiket.core.CoreFunctions;
import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import java.util.*;

public class VerticalProductPage<T extends WebDriver> extends BasePage<T> {
    public VerticalProductPage(Session<T> session) {
        super(session);
    }
    ILogger logger = BaseTest.mainLogger.get();
    private String uniqueCode;
    private static final String HOME_URL = "/admin/home/v2";
    private static final Locator HOME_DASHBOARD_MARKER = new Locator("HOME_DASHBOARD_MARKER", By.xpath("//div[contains(@class,'TopNav_top_nav_inner')]"));
    private static final Locator VERTICAL_PRODUCT_SIDEBAR_BUTTON = new Locator("VERTICAL_PRODUCT_SIDEBAR_BUTTON", By.xpath("//span[normalize-space()='Vertical Product']"));
    private static final Locator VERTICAL_ICON_NEW = new Locator("VERTICAL_ICON_NEW", By.xpath("//span[normalize-space()='New Vertical Product']"));
    private static final Locator LOGGED_IN_INFO = new Locator("LOGGED_IN_INFO", new By.ByXPath("//div[@class='Header_user_info__CnwLv']"));
    private static final Locator VERTICAL_PRODUCT_HEADER = new Locator("VERTICAL_PRODUCT_HEADER", By.xpath("//p[contains(text(),'Vertical Product')]"));
    private static final Locator LOGGED_IN_INFO_OLD = new Locator("LOGGED_IN_INFO_OLD", new By.ByXPath("//div[@class='account-toolbar']"));
    private static final Locator EDIT_VERTICAL_BUTTON = new Locator("EDIT_VERTICAL_BUTTON", By.xpath("//button[.//span[normalize-space()='Edit Vertical']]"));
    private static final Locator ADD_VERTICAL_BUTTON = new Locator("ADD_VERTICAL_BUTTON", By.xpath("//button[.//span[normalize-space()='Add Vertical']]"));
    private static final Locator VERTICAL_PRODUCT_BUTTON = new Locator("VERTICAL_PRODUCT_BUTTON", new By.ByXPath("//button[contains(text(),'Edit Vertical')]"));
    private static final Locator ADD_VERTICAL_LOGO = new Locator("ADD_VERTICAL_LOGO", new By.ByXPath("//span[contains(text(),'Add Product')]"));
    private static final Locator CODE_INPUT = new Locator("CODE_INPUT", new By.ByXPath("//input[@id='code']"));
    private static final Locator TRACKER_CODE_INPUT = new Locator("TRACKER_CODE_INPUT", new By.ByXPath("//input[@name='trackerCode']"));
    private static final Locator DROP_DOWN_CURRENCY = new Locator("DROP_DOWN_CURRENCY", By.xpath("//input[@id=\"currency\" and @value=\"IDR\"]"));
    private static final Locator CURRENCY_ALL = new Locator("CURRENCY_ALL", new By.ByXPath("//p[normalize-space()='ALL']"));
    private static final Locator TOGGLE_LABEL = new Locator("TOGGLE_LABEL", new By.ByXPath("//div[contains(@class, 'Toggle_toggle')]"));
    private static final Locator PLATFORM_DROPDOWN = new Locator("PLATFORM_DROPDOWN", new By.ByXPath("//input[@id='platform']"));
    private static final Locator ALL_PLATFORM_BUTTON = new Locator("ALL_PLATFORM_BUTTON", new By.ByXPath("//div[@class = 'cta-add-platform']/span[text()='+ Add All Remaining Platform']"));
    private static final Locator PLATFORM_ANDROID = new Locator("PLATFORM_ANDROID", new By.ByXPath("//div[contains(@class,'styles_dropdown_options')]//div[1]"));
    private static final Locator APP_VERSION_START = new Locator("APP_VERSION_START", By.xpath("//*[@id=\"version-start\"]"));
    private static final Locator APP_VERSION_END = new Locator("APP_VERSION_END", By.xpath("//input[@name = 'versionEnd']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", new By.ByXPath("//span[text()='Submit']"));
    private static final Locator TYPE_ACTION_DROPDOWN = new Locator("TYPE_ACTION_DROPDOWN", new By.ByXPath("//input[@data-error-message = 'Select one of the options.']"));
    private static final Locator TYPE_ACTION_MWEB = new Locator("TYPE_ACTION_MWEB", new By.ByXPath("//span[normalize-space()='Mweb & Dweb - Redirect']"));
    private static final Locator URL_SLUG_INPUT = new Locator("URL_SLUG_INPUT", By.xpath("//input[@id='full-urlslug']"));
    private static final Locator NAME_TO_USER_ID = new Locator("NAME_TO_USER_ID", new By.ByXPath("//input[@id= 'name-to-user-id']"));
    private static final Locator NAME_TO_USER_EN = new Locator("NAME_TO_USER_EN", new By.ByXPath("//input[@id= 'name-to-user-en']"));
    private static final Locator NAME_TO_USER_VERSION = new Locator("NAME_TO_USER_VERSION", new By.ByXPath("//input[@name='versionsPromptUpdate']"));
    private static final Locator CONTENT_LABEL_ID = new Locator("CONTENT_LABEL_ID", new By.ByXPath("//input[@id='content-label-id']"));
    private static final Locator CONTENT_LABEL_EN = new Locator("CONTENT_LABEL_EN", new By.ByXPath("//input[@id='content-label-en']"));
    private static final Locator UPLOAD_ICON = new Locator("UPLOAD_ICON", new By.ByXPath("//input[@type='file' and @name='iconUrl']"));
    private static final Locator COLOR_LABEL = new Locator("COLOR_LABEL", By.xpath("//input[@id='color-label-hex']"));
    private static final Locator PERIOD_ALWAYS_ON = new Locator("PERIOD_ALWAYS_ON", By.xpath("//span[normalize-space()='Always ON']"));
    private static final Locator ERROR_MODAL = new Locator("ERROR_MODAL", By.xpath("//section[contains(@class,'Modal_modal_popup_small')]"));
    private static final Locator ERROR_MODAL_CLOSE = new Locator("ERROR_MODAL_CLOSE", By.xpath("//button[contains(@data-testid,'modal-close-button')"));
    private static final Locator EDIT_VERTICAL_CURRENCY = new Locator("EDIT_VERTICAL_CURRENCY", new By.ByXPath("//input[@id = 'currency' and @value = 'ALL']"));
    private static final Locator EDIT_VERTICAL_PLATFORM = new Locator("EDIT_VERTICAL_PLATFORM", new By.ByXPath("//input[@id = 'platform' and @value = 'Android']"));
    private static final Locator EDIT_VERTICAL_TYPE_ACTION = new Locator("EDIT_VERTICAL_TYPE_ACTION", new By.ByXPath("//span[contains(normalize-space(),'Mweb & Dweb')]"));
    private static final Locator SELECTED_TYPE_ACTION = new Locator("SELECTED_TYPE_ACTION", By.xpath("//span[contains(normalize-space(),'Mweb & Dweb - Redirect')]/preceding-sibling::span//input[@type='radio']"));
    private static final Locator WELCOME_DASHBOARD_LANGUAGE_BUTTON = new Locator("WELCOME_DASHBOARD_LANGUAGE_BUTTON", new By.ByXPath("//button[@data-testid='button-submit']"));
    private static final Locator WELCOME_DASHBOARD_LANGUAGE_BUTTON_NEW_TAB = new Locator("WELCOME_DASHBOARD_LANGUAGE_BUTTON_NEW_TAB", new By.ByXPath ("//button[text() = 'GET IN']"));
    private static final Locator DELETE_VERTICAL_BUTTON = new Locator("DELETE_VERTICAL_BUTTON", new By.ByXPath ("//button[text() = 'DELETE VERTICAL']"));
    private static final Locator INPUT_CODE_TO_DELETE = new Locator("INPUT_CODE_TO_DELETE", new By.ByXPath ("//input[@name='verticalDeleteCode']"));
    private static final Locator SUBMIT_DELETE_BUTTON = new Locator("SUBMIT_DELETE_BUTTON", new By.ByXPath ("//button[text() = 'DELETE']"));
    private static final Locator SNACKBAR_SUCCESS_DELETE = new Locator("SNACKBAR_SUCCESS_DELETE", By.xpath("//div[@class = 'snackbar-container' and text() = 'Success Delete Vertical']"));
    private static final Locator GET_IN_BUTTON = new Locator("GET_IN_BUTTON", By.xpath("//button[normalize-space(.)='GET IN']"));


    public void goToVerticalProductPage(String username, String password) throws InterruptedException{
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(HOME_URL);

        if (!driverTools.getCurrentURL().contains(HOME_URL)) {
            memberLoginPage.goToPage(HOME_URL);
            driver.manage().addCookie(new Cookie.Builder("dashboardAreaHOME","IDN")
                    .sameSite("None")
                    .domain(".tiket.com")
                    .path("/").build());
            driver.manage().addCookie(new Cookie.Builder("dashboardArea","IDN")
                    .sameSite("None")
                    .domain(".tiket.com")
                    .path("/").build());
            driver.manage().addCookie(new Cookie.Builder("x-ls-checker","true")
                    .sameSite("None")
                    .domain(".tiket.com")
                    .path("/").build());
        }
    }

    public void clickVerticalMenuButton() {
        if (driverTools.waitForElementToBeClickable(VERTICAL_PRODUCT_SIDEBAR_BUTTON).isEmpty()) {
            throw new NoSuchElementException("Vertical Product sidebar button was not found or not clickable. " + "Current URL: " + driverTools.getCurrentURL());
        }
        driverTools.click(VERTICAL_PRODUCT_SIDEBAR_BUTTON);
        if (driverTools.waitForElementToBeClickable(VERTICAL_ICON_NEW).isEmpty()) {
            throw new NoSuchElementException("New Vertical Product menu was not found or not clickable. " + "Current URL: " + driverTools.getCurrentURL());
        }
        driverTools.click(VERTICAL_ICON_NEW);
    }

    public void clickGetInButton() {
        // GET IN prompt may not be shown in every session; skip when absent.
        if (driverTools.waitForElementToAppear(GET_IN_BUTTON, 2).isEmpty()) {
            logger.log("GET IN button not present; skipping. Current URL: " + driverTools.getCurrentURL());
            return;
        }
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(GET_IN_BUTTON);
    }

    public void confirmationCountryButton(){
        clickGetInButton();
    }

    public void clickAddVerticalButton(){
        driverTools.waitForElementToAppear(ADD_VERTICAL_BUTTON, 2);
        driverTools.click(ADD_VERTICAL_BUTTON);
    }

    public boolean isVerticalProductPageLoaded(){
        driverTools.waitForElementToAppear(VERTICAL_PRODUCT_HEADER, 2);
        return driverTools.isElementDisplayed(VERTICAL_PRODUCT_HEADER);
    }

    public void newTabLoaded(){
        String mainWindowHandle = driver.getWindowHandle();
        clickAddVerticalButton();
        for(String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }

    public boolean isAddVerticalProductPageLoaded(){
        driverTools.waitForElementToAppear(ADD_VERTICAL_LOGO, 4);
        return (driverTools.isElementDisplayed(ADD_VERTICAL_LOGO));
    }

    public void inputCode(){
        String randomCode = CoreFunctions.generateRandomString(7);
        String randomCodeString = "automation-"+ randomCode;
        driverTools.click(CODE_INPUT);
        driverTools.sendKeys(CODE_INPUT, randomCodeString);
        this.uniqueCode = randomCodeString;
    }

    public void inputTrackerCode(String trackerCode){
        driverTools.click(TRACKER_CODE_INPUT);
        driverTools.sendKeys(TRACKER_CODE_INPUT, trackerCode);
    }

    public void clickCurrencyType(){
        driverTools.clickWithJavascriptExecutor(DROP_DOWN_CURRENCY);
        driverTools.waitForElementToAppear(CURRENCY_ALL, 2);
        driverTools.clickWithJavascriptExecutor(CURRENCY_ALL);
    }

    public void inputContentLabel(String contentLabelId, String contentLabelEn){
        driverTools.clickWithJavascriptExecutor(CONTENT_LABEL_EN);
        driverTools.sendKeys(CONTENT_LABEL_EN, contentLabelEn);
        driverTools.clickWithJavascriptExecutor(CONTENT_LABEL_ID);
        driverTools.sendKeys(CONTENT_LABEL_ID, contentLabelId);
    }

    public void clickToggleLabel(){
        driverTools.clickWithJavascriptExecutor(TOGGLE_LABEL);
    }

    public void inputColorHex(String hexColor){
        driverTools.waitForElementToBeClickable(COLOR_LABEL);
        driverTools.scrollToElement(COLOR_LABEL);
        driverTools.click(COLOR_LABEL);
        driverTools.sendKeys(COLOR_LABEL, hexColor);
    }

    public void choosePeriodAlwaysOn(){
        driverTools.scrollToElement(PERIOD_ALWAYS_ON);
        driverTools.click(PERIOD_ALWAYS_ON);
    }

    public void choosePlatform(){
        driverTools.scrollToElement(PLATFORM_DROPDOWN);
        driverTools.click(PLATFORM_DROPDOWN);
        driverTools.click(PLATFORM_ANDROID);
    }

    public void inputAppVersion(String appVersionStart, String appVersionEnd){
        driverTools.clickWithJavascriptExecutor(APP_VERSION_START);
        driverTools.sendKeys(APP_VERSION_START, appVersionStart);
        driverTools.clickWithJavascriptExecutor(APP_VERSION_END);
        driverTools.sendKeys(APP_VERSION_END, appVersionEnd);
    }

    public void chooseTypeAction(){
        driverTools.scrollToElement(TYPE_ACTION_MWEB);
        driverTools.click(TYPE_ACTION_MWEB);
    }

    public void uploadIcon(List<String> files){
        for (String file: files) {
            driverTools.sendKeys(UPLOAD_ICON, file);
        }
    }

    public void inputUrlSlug(String urlSlug){
        driverTools.scrollToElement(URL_SLUG_INPUT);
        driverTools.click(URL_SLUG_INPUT);
        driverTools.sendKeys(URL_SLUG_INPUT, urlSlug);
    }

    public void inputNameToUser(String nameToUserId, String nameToUserEn, String nameToUserVersion){
        driverTools.scrollToElement(NAME_TO_USER_EN);
        driverTools.click(NAME_TO_USER_ID);
        driverTools.sendKeys(NAME_TO_USER_ID, nameToUserId);
        driverTools.click(NAME_TO_USER_EN);
        driverTools.sendKeys(NAME_TO_USER_EN, nameToUserEn);
        driverTools.scrollToElement(NAME_TO_USER_VERSION);
        driverTools.click(NAME_TO_USER_VERSION);
        driverTools.sendKeys(NAME_TO_USER_VERSION, nameToUserVersion);
    }
     public void clickSubmitButton() {
         driverTools.scrollToElement(SUBMIT_BUTTON);
         driverTools.click(SUBMIT_BUTTON);
     }

    private Locator codeValue(String cl) {
        return new Locator ("LATEST_CODE_CREATED", By.xpath("//button[text() = '"+ cl +"']"));
    }

    public void clickEditVertical(){
        driverTools.click(EDIT_VERTICAL_BUTTON);
        driverTools.waitForElementToPresence(codeValue(this.uniqueCode.toLowerCase()), 2);
        driverTools.scrollIntoWebView(codeValue(this.uniqueCode.toLowerCase()));
        driverTools.click(codeValue(this.uniqueCode.toLowerCase()));
    }

    public void goToEditVertical(){
        clickEditVertical();
        Set<String> allWindowHandles = driver.getWindowHandles();
        List<String> windowHandlesList = new ArrayList<>(allWindowHandles);
        if (windowHandlesList.size() > 2) {
            driver.switchTo().window(windowHandlesList.get(2));
        }
        driverTools.getCurrentURL();
    }

     public Map<String, String> verifyAllData() {
        Map<String, String> result = new HashMap<>();
         driverTools.waitForElementToPresence(TRACKER_CODE_INPUT, 3);
         String uniqueCode = driverTools.getValue(CODE_INPUT);
         String trackerCode = driverTools.getValue(TRACKER_CODE_INPUT);
         String currency = driverTools.getValue(EDIT_VERTICAL_CURRENCY);
         String contentLabelId = driverTools.getValue(CONTENT_LABEL_ID);
         String contentLabelEn = driverTools.getValue(CONTENT_LABEL_EN);
         String hexColor = driverTools.getValue(COLOR_LABEL);
         String platform = driverTools.getValue(EDIT_VERTICAL_PLATFORM);
         String appVersionStart = driverTools.getValue(APP_VERSION_START);
         String appVersionEnd = driverTools.getValue(APP_VERSION_END);
         String typeAction = driverTools.getValue(EDIT_VERTICAL_TYPE_ACTION);
         String urlSlug = driverTools.getValue(URL_SLUG_INPUT);
         String nameToUserId = driverTools.getValue(NAME_TO_USER_ID);
         String nameToUserEn = driverTools.getValue(NAME_TO_USER_EN);
         String nameToUserVersion = driverTools.getValue(NAME_TO_USER_VERSION);
         result.put("uniqueCode", uniqueCode);
         result.put("trackerCode", trackerCode);
         result.put("currency", currency);
         result.put("contentLabelId", contentLabelId);
         result.put("contentLabelEn", contentLabelEn);
         result.put("hexColor", hexColor);
         result.put("platform", platform);
         result.put("appVersionStart", appVersionStart);
         result.put("appVersionEnd", appVersionEnd);
         result.put("urlSlug", urlSlug);
         result.put("nameToUserId", nameToUserId);
         result.put("nameToUserEn", nameToUserEn);
         result.put("nameToUserVersion", nameToUserVersion);
         return result;
     }

    public boolean isTypeActionRedirectChosen(){
        return driverTools.isRadioButtonChecked(SELECTED_TYPE_ACTION);
    }

    public String getUniqueCodeValue(){
         return uniqueCode.toLowerCase();
     }

     public void clickDeleteButton(){
        driverTools.click(DELETE_VERTICAL_BUTTON);
     }

     public void submitDelete(){
        driverTools.waitForElementToPresence(INPUT_CODE_TO_DELETE);
        driverTools.sendKeys(INPUT_CODE_TO_DELETE, this.uniqueCode.toLowerCase());
        driverTools.click(SUBMIT_DELETE_BUTTON);
     }
     public boolean isSnackbarDeleteAppear(){
        driverTools.waitForElementToPresence(SNACKBAR_SUCCESS_DELETE);
        return (driverTools.isElementDisplayed(SNACKBAR_SUCCESS_DELETE));
     }

    public boolean isHomeDashboardLoaded() {
        driverTools.waitForElementToAppear(HOME_DASHBOARD_MARKER, 5);
        driverTools.waitForElementToAppear(VERTICAL_PRODUCT_SIDEBAR_BUTTON, 5);

        boolean headerDisplayed = driverTools.isElementDisplayed(HOME_DASHBOARD_MARKER);
        boolean sidebarDisplayed = driverTools.isElementDisplayed(VERTICAL_PRODUCT_SIDEBAR_BUTTON);

        return headerDisplayed && sidebarDisplayed;
    }
}