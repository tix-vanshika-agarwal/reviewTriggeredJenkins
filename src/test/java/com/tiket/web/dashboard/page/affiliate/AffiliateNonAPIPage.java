package com.tiket.web.dashboard.page.affiliate;

import com.microsoft.playwright.P;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.*;
import com.tiket.core.CoreFunctions;
import org.testng.SkipException;

import java.util.List;


public class AffiliateNonAPIPage<T extends WebDriver> extends BasePage<T> {
    public AffiliateNonAPIPage(Session<T> session) {
        super(session);
    }

    private static final Locator PARTNER_LIST = new Locator("PARTNER_LIST", new By.ByXPath("//div[normalize-space()='Partner List']"));
    private static final Locator NON_API_SECTION = new Locator("NON_API_SECTION", new By.ByXPath("//p[normalize-space()='Non-API Partner']"));
    private static final Locator SEARCH_BUTTON= new Locator("SEARCH_BUTTON", new By.ByXPath("//button[normalize-space()='Search']"));
    private static final Locator PARTNER_NAME_FIELD = new Locator("PARTNER_NAME_FIELD", new By.ByXPath("//input[@id='partner-name-or-id']"));
    private static final Locator RESET_BTN = new Locator("RESET_BTN", new By.ByXPath("//button[normalize-space()='Reset']"));
    private static final Locator PWA_PARTNER_NAME_TEXT_BOX = new Locator("PWA_PARTNER_NAME_TEXT_BOX", new By.ByXPath("(//input[@id='partner-name'])[1]"));
    private static final Locator PWA_SECTION = new Locator("PWA_SECTION", new By.ByXPath("//div[normalize-space()='PWA']"));
    private static final Locator EDIT_PWA_BTN = new Locator("EDIT_PWA_BTN", new By.ByXPath("(//div[contains(text(),'Edit')])[1]"));
    private static final Locator TOGGLE_PWA = new Locator("TOGGLE_PWA", new By.ByXPath("//div[contains(@class, 'Toggle_knob')]"));
    private static final Locator SAVE_BTN_PWA = new Locator("SAVE_BTN_PWA", new By.ByXPath("//button[normalize-space()='Save']"));
    private static final Locator TOGGLE_STATUS_PWA_LIST = new Locator("TOGGLE_STATUS_PWA_LIST", new By.ByXPath("(//td[contains(@class,'text-sm') and contains(@class,'p-5')])[6]"));
    private static final Locator URL_DESCRIPTION = new Locator("URL_DESCRIPTION", new By.ByXPath("//input[@id='url-description']"));
    private static final Locator URL_TEXT = new Locator("URL_TEXT", new By.ByXPath("//input[@id='url-text']"));
    private static final Locator URL = new Locator("URL", new By.ByXPath("//input[@id='url']"));

    private static final Locator NON_API_PARTNERS = new Locator("NON_API_PARTNERS", By.xpath("//p[text()='Non-API Partner']"));
    private static final Locator CO_BRAND_SECTION = new Locator("CO_BRAND_SECTION", By.xpath("(//div[text()='Co-Brand'])[1]"));
    private static final Locator STATUS_TEXTS = new Locator("STATUS_TEXTS", By.xpath("//tbody/tr/td[7]/div"));
    private static final Locator START_DATE_TEXT = new Locator("START_DATE_TEXT", By.xpath("//input[@id='start-date']"));
    private static final Locator END_DATE_TEXT = new Locator("END_DATE_TEXT", By.xpath("//input[@id='end-date']"));
    private static final Locator STATUS_FIELDS = new Locator("STATUS_FIELDS", By.xpath("//input[@id='status']"));
    private static final Locator STATUS_DROPDOWN_BUTTON = new Locator("STATUS_DROPDOWN_BUTTON", By.xpath("//div[./ul[contains(@class,'InputDropdown_dropdown')]]//button[contains(@class,'styles_clickable_icon')]"));
    private static final Locator STATUS_DROPDOWN_OPTION = new Locator("STATUS_DROPDOWN_OPTION", By.xpath("//ul[contains(@class,'InputDropdown_dropdown')]/li"));
    private static final Locator CALENDAR_LEFT_MONTH_YEAR_TEXT = new Locator("CALENDAR_LEFT_MONTH_YEAR_TEXT", By.xpath("(//section[contains(@class,'CalendarDesktop_month')]//h2)[1]"));
    private static final Locator CALENDAR_PREV_BUTTON = new Locator("CALENDAR_PREV_BUTTON", By.xpath("//button[contains(@class,'CalendarDesktop_prev_button')]"));
    private static final Locator CALENDAR_NEXT_BUTTON = new Locator("CALENDAR_NEXT_BUTTON", By.xpath("//button[contains(@class,'CalendarDesktop_next_button')]"));
    private static final Locator SUBMIT_FILTER_BUTTON = new Locator("SUBMIT_FILTER_BUTTON", By.xpath("//button[contains(@class,'Button_variant_secondary')]"));
    private static final Locator SIDEBAR_LOGO = new Locator("SIDEBAR_LOGO", By.xpath("//div[contains(@class,'Sidebar_logo')]"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[normalize-space()='Reset']"));
    private static final Locator MORE_ACTION_DROPDOWN = new Locator("MORE_ACTION_DROPDOWN", By.xpath("(//input[@id='more-action'])[1]"));
    private static final Locator REDIRECT_LINK = new Locator("REDIRECT_LINK", By.xpath("//span[normalize-space()='Redirect Link']"));
    private static final Locator ADD_NEW_BTN = new Locator("ADD_NEW_BTN", By.xpath("//button[normalize-space()='Add New']"));
    private static final Locator URL_NAME_INPUT = new Locator("URL_NAME_INPUT", By.xpath("//input[@id='url-name']"));
    private static final Locator DESTINATION_URL_INPUT = new Locator("DESTINATION_URL_INPUT", By.xpath("//input[@id='destination-url']"));
    private static final Locator DROPDOWN_INPUT = new Locator("DROPDOWN_INPUT", By.xpath("(//div[contains(@class,'css-19bb58m')]//input[@role='combobox'])[1]"));
    private static final Locator UTM_SOURCE_FIRST_OPTION = new Locator("UTM_SOURCE_FIRST_OPTION", By.xpath("(//div[@role='option'])[1]"));
    private static final Locator UTM_CAMPAIGN_INPUT = new Locator("UTM_CAMPAIGN_INPUT", By.xpath("//input[@id='utm-campaign']"));
    private static final Locator CREATE_LINK_BTN = new Locator("CREATE_LINK_BTN", By.xpath("//button[normalize-space()='Create Link']"));
    private static final Locator DESCRIPTION_TEXTAREA = new Locator("DESCRIPTION_TEXTAREA", By.xpath("//textarea[@id='description']"));
    private static final Locator SUCCESS_MESSAGE = new Locator("SUCCESS_MESSAGE", By.xpath("//p[contains(text(),'successfully created a new URL')]"));
    private static final Locator NON_API_PARTNER=  new Locator("NON_API_PARTNER", By.xpath("(//input[@id='partner-name-or-id'])[2]"));
    private static final Locator CREATE_BUTTON = new Locator("CREATE_BUTTON", By.xpath("//button[contains(normalize-space(),'Create')]"));
    private static final Locator FIRST_DROPDOWN_ITEM = new Locator("FIRST_DROPDOWN_ITEM",By.xpath("(//ul[contains(@class,'InputSearch_dropdown')]//li)[1]"));
    private static final Locator COBRAND_CAMPAIGN = new Locator("COBRAND_CAMPAIGN", By.xpath("//span[normalize-space()='Cobrand Campaign']"));
    private static final Locator MORE_ACTION_SELECTIONS = new Locator("MORE_ACTION_SELECTIONS", By.xpath("//span[contains(@class,'select-none whitespace-nowrap')]"));
    private static final Locator ATTRIBUTION_MODAL = new Locator("ATTRIBUTION_MODAL", By.xpath("//section[contains(@class, 'Modal_modal_popup')]"));
    private static final Locator ATTRIBUTION_WINDOW_OPTION = new Locator("ATTRIBUTION_WINDOW_OPTION", By.xpath("//p[@class='Text_text__DSnue Text_size_b2__y3Q2E']"));
    private static final Locator ADD_UTM_SOURCE_BUTTON = new Locator("ADD_UTM_SOURCE_BUTTON", By.xpath("(//button[contains(@class, 'Button_variant_tertiary')])[2]"));
    private static final Locator UTM_SOURCE_INPUTS = new Locator("UTM_SOURCE_INPUTS", By.xpath("//input[@id='utm-source']"));
    private static final Locator SAVE_REDIRECTION_BUTTON = new Locator("SAVE_REDIRECTION_BUTTON", By.xpath("//button[text()='Save Redirection']"));
    private static final Locator UTM_SOURCE_INPUT_DELETE_BUTTONS = new Locator("UTM_SOURCE_INPUT_DELETE_BUTTONS", By.xpath("//input[@id='utm-source']/following-sibling::div[2]/button"));
    private static final Locator PRELOAD_TAB = new Locator("PRELOAD_TAB", By.xpath("//span[text()='Pre-load']"));
    private static final Locator PRELOAD_MEDIA_SOURCE_INPUT = new Locator("PRELOAD_MEDIA_SOURCE_INPUT", By.xpath("//input[@id='media-source']"));
    private static final Locator SAVE_PRELOAD_BUTTON = new Locator("SAVE_PRELOAD_BUTTON", By.xpath("//button[text()='Save Pre-load']"));

    private Locator getFirstDataRowLocator(String firstPartnerName) {
        String xpath = String.format("//span[contains(normalize-space(), '%s')]", firstPartnerName);
        return new Locator("FIRST_DATA_ROW", new By.ByXPath(xpath));
    }

    public void nonAPISection() {
        goToAffiliateMainPage();
        driverTools.waitForElementToAppear(NON_API_SECTION);
        driverTools.scrollElementToCenter(NON_API_SECTION);
        driverTools.click(NON_API_SECTION);
    }

    public void clickPartnerListMenu() {
        driverTools.waitForElementToAppear(PARTNER_LIST, 5);
        driverTools.click(PARTNER_LIST);
    }

    public void partnerList(String partnerName) {
        driverTools.click(PARTNER_NAME_FIELD);
        driverTools.sendKeys(PARTNER_NAME_FIELD, partnerName);
    }

    public void clickSearchButton() {
        driverTools.click(SEARCH_BUTTON);
    }

    public String getFirstRowProductType(String firstPartnerName) {
        Locator firstRowData = getFirstDataRowLocator(firstPartnerName);
        driverTools.waitForElementToAppear(firstRowData, 4).orElseThrow();
        return driverTools.getText(firstRowData).orElseThrow();
    }

    public void clickResetButton() {
        driverTools.click(RESET_BTN);
    }

    public String searchTextBoxIsEmpty(){
        return driverTools.getText(PARTNER_NAME_FIELD).orElseThrow();
    }

    public void clickPwaSection() {
        driverTools.click(PWA_SECTION);
    }

    public void clickPwaPartnerTextField(String partnerName) {
        driverTools.sendKeys(PWA_PARTNER_NAME_TEXT_BOX,partnerName);
    }


    public void clickEditPwaButton() {
        driverTools.click(EDIT_PWA_BTN);
    }

    public void clickToggleButton() {
        driverTools.click(TOGGLE_PWA);

    }

    public void clickSaveBtnOfPwa() {
        driverTools.scrollToElement(SAVE_BTN_PWA);
        driverTools.click(SAVE_BTN_PWA);
    }

    public String getPartnerToggleStatusOnList() {
        driverTools.waitForElementToAppear(TOGGLE_STATUS_PWA_LIST);
        return driverTools.getText(TOGGLE_STATUS_PWA_LIST).orElseThrow();

    }

    public void editTermsAndConditionData() {
        String urlDescription = "Automation " + CoreFunctions.randomNameWithoutTest() + CoreFunctions.randomInt(4);
        String urlText= "Automation " + CoreFunctions.randomNameWithoutTest() + CoreFunctions.randomInt(4);
        String urlLink= "https://www.tiket.com/" + CoreFunctions.randomNameWithoutTest() + CoreFunctions.randomInt(4);
        driverTools.clearInput(URL_DESCRIPTION);
        driverTools.clearInput(URL_TEXT);
        driverTools.clearInput(URL);
        driverTools.sendKeys(URL_DESCRIPTION,urlDescription);
        driverTools.sendKeys(URL_TEXT,urlText);
        driverTools.sendKeys(URL,urlLink);
    }


    public String getUrlDescriptionTextFromTermsAndCondition() {
        return driverTools.getAttribute(URL_DESCRIPTION.by(),"value");

    }

    public String getUrlTextFromTermsAndCondition() {
        return driverTools.getAttribute(URL_TEXT.by(),"value");
    }

    public String getUrlFromTermsAndCondition() {
        return driverTools.getAttribute(URL.by(),"value");
    }

    public void scrollToSaveBtnPwa() {
        driverTools.waitForElementToAppear(TOGGLE_PWA);
        driverTools.scrollToElement(SAVE_BTN_PWA);
    }

    public void goToAffiliateMainPage() {

        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/affiliate");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/affiliate");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://www.tiket.com/admin/affiliate");
        }
    }

    public void clickCobrandMenuPage() {
        driverTools.waitForElementToAppear(NON_API_PARTNERS, 5);
        driverTools.click(NON_API_PARTNERS);
        driverTools.waitForElementToAppear(CO_BRAND_SECTION, 5);
        driverTools.click(CO_BRAND_SECTION);
    }

    public void setTheFilterField(String startDate, String endDate, String status) {
        if (!status.isEmpty()) {
            driverTools.click(STATUS_DROPDOWN_BUTTON);
            List<WebElement> dropdownSelection = driverTools.elementsUntilExist(STATUS_DROPDOWN_OPTION).get();

            for (WebElement e : dropdownSelection) {
                String text = driverTools.getText(e);
                if (text.equalsIgnoreCase(status)) {
                    driverTools.click(e);
                }
            }
        }

        if (!(startDate.isEmpty() && endDate.isEmpty())) {
            driverTools.click(START_DATE_TEXT);

            String initialDisplayedMonthYearLeft = driverTools.getText(CALENDAR_LEFT_MONTH_YEAR_TEXT).get();
            int displayedMonthLeft = CoreFunctions.monthToMonthNumber(initialDisplayedMonthYearLeft.split(" ")[0]);
            int displayedYearLeft = Integer.parseInt(initialDisplayedMonthYearLeft.split(" ")[1]);

            int targetStartMonthParam = Integer.parseInt(startDate.split("-")[1]);
            int targetStartYearParam = Integer.parseInt(startDate.split("-")[2]);

            int startMonthDiff = (((displayedYearLeft - 1) * 12) + displayedMonthLeft) - (((targetStartYearParam - 1) * 12) + targetStartMonthParam);
            if (startMonthDiff > 0) {
                changeMonthButton(startMonthDiff, "prev");
            } else if (startMonthDiff < 0) {
                changeMonthButton(startMonthDiff * -1, "next");
            }
            driverTools.click(getDateLocator(startDate.split("-")[0], CoreFunctions.getMonthName(targetStartMonthParam, "en"), String.valueOf(targetStartYearParam)));

            int targetEndMonthParam = Integer.parseInt(endDate.split("-")[1]);
            int targetEndYearParam = Integer.parseInt(endDate.split("-")[2]);

            int endMonthDiff = (((targetEndYearParam - 1) * 12) + targetEndMonthParam) - (((targetStartYearParam - 1) * 12) + targetStartMonthParam);
            if (endMonthDiff > 0) {
                changeMonthButton(endMonthDiff, "next");
            } else if (endMonthDiff < 0) {
                throw new SkipException("End Date can't be earlier than start date, change testdata");
            }
            driverTools.click(getDateLocator(endDate.split("-")[0], CoreFunctions.getMonthName(targetEndMonthParam, "en"), String.valueOf(targetEndYearParam)));

            driverTools.clickElementByCoordinateDWeb(SIDEBAR_LOGO);
        }
        clickSubmitBTN();
    }

    private Locator getDateLocator(String date, String month, String year) {
        String setOfDate = date + " " + month + " " + year;
        return new Locator("CALENDAR_" + setOfDate, By.xpath("//button[contains(@class,'Day_day') and .//span[contains(@aria-label,'" + setOfDate + "') and text()='" + date + "']]"));
    }

    private void changeMonthButton(int monthDiff, String type) {
        for (int i = 0; i < monthDiff; i++) {
            switch (type.toLowerCase()) {
                case "prev" -> {
                    driverTools.click(CALENDAR_PREV_BUTTON);
                }
                case "next" -> {
                    driverTools.click(CALENDAR_NEXT_BUTTON);
                }
            }
        }
    }

    private void clickSubmitBTN() {
        driverTools.click(SUBMIT_FILTER_BUTTON);
    }

    public void clickResetFilterBTN() throws InterruptedException {
        driverTools.click(RESET_FILTER_BUTTON);
        String text = "";

        for (int i = 0; i < 5; i++) {
            text = driverTools.getText(driverTools.elementsUntilExist(START_DATE_TEXT).get().get(0));
            if (!text.isEmpty()) {
                break;
            }
        }
    }

    public boolean checkFilterDates(String status, String startDate, String endDate) {
        if (!status.isEmpty()) {
            List<WebElement> statusTexts = driverTools.elementsUntilExist(STATUS_TEXTS).get();

            for (int i = 0; i < statusTexts.size(); i++) {
                String displayedStatus = driverTools.getText(statusTexts.get(i));
                if (!displayedStatus.toLowerCase().contains(status.toLowerCase())) {
                    logger.log("index : " + i + "\ndisplayedStatus : " + displayedStatus);
                    return false;
                }
            }
        }

        if (!startDate.isEmpty()) {
            List<WebElement> startDateElements = driverTools.elementsUntilExist(START_DATE_TEXT).get();

            for (int i = 0; i < startDateElements.size(); i++) {
                String displayedStartDate = driverTools.getText(startDateElements.get(i));
                if (!displayedStartDate.equals(startDate)) {
                    logger.log("index : " + i + "\ndisplayedStartDate : " + displayedStartDate);
                    return false;
                }
            }
        }

        if (!endDate.isEmpty()) {
            List<WebElement> dateElements = driverTools.elementsUntilExist(END_DATE_TEXT).get();
            for (int i = 0; i < dateElements.size(); i++) {
                String displayedDate = driverTools.getText(dateElements.get(i));
                if (displayedDate.compareTo(endDate) > 0) {
                    logger.log("index : " + i + "\ndisplayedDate : " + displayedDate + "\nendDate : " + endDate);
                    return false;
                }
            }
        }
        return true;
    }

    public void clickMoreAction(){
        driverTools.waitForElementsToAppear(MORE_ACTION_DROPDOWN);
        driverTools.clickWithJavascriptExecutor(MORE_ACTION_DROPDOWN);
    }

    public void openRedirectedLink(){
        driverTools.waitForElementsToAppear(REDIRECT_LINK);
        driverTools.clickWithJavascriptExecutor(REDIRECT_LINK);
    }

    public void clickAddNewButton(){
        driverTools.dummyWait(4);
        driverTools.waitForElementToBeClickable(ADD_NEW_BTN,10);
        driverTools.clickElementByCoordinateDWeb(ADD_NEW_BTN);
    }

    public void fillLinkDetails( String name, String URL, String campaign, String description){
        driverTools.dummyWait(4);
        String destinationURL;
        switch (Configs.ENVIRONMENT) {
            case GK -> destinationURL = "https://gatotkaca.tiket.com/" + URL;
            case PREPROD -> destinationURL = "https://preprod.tiket.com/" + URL;
            case PROD -> destinationURL = "https://www.tiket.com/" + URL;
            default -> throw new IllegalStateException("Unsupported environment: " + Configs.ENVIRONMENT);
        }

        driverTools.waitForElementToPresence(URL_NAME_INPUT);
        driverTools.clearInput(URL_NAME_INPUT);
        driverTools.sendKeys(URL_NAME_INPUT, name);

        driverTools.waitForElementToPresence(DESTINATION_URL_INPUT);
        driverTools.clearInput(DESTINATION_URL_INPUT);
        driverTools.sendKeys(DESTINATION_URL_INPUT, destinationURL);

        driverTools.waitForElementsToAppear(DROPDOWN_INPUT);
        driverTools.click(DROPDOWN_INPUT);

        driverTools.waitForElementsToAppear(UTM_SOURCE_FIRST_OPTION);
        driverTools.clickWithJavascriptExecutor(UTM_SOURCE_FIRST_OPTION);

        driverTools.dummyWait(5);

        driverTools.clearInput(UTM_CAMPAIGN_INPUT);
        driverTools.waitForElementToBeClickable(UTM_CAMPAIGN_INPUT,5);
        driverTools.clickWithJavascriptExecutor(UTM_CAMPAIGN_INPUT);
        driverTools.sendKeys(UTM_CAMPAIGN_INPUT, campaign);

        driverTools.waitForElementToPresence(DESCRIPTION_TEXTAREA);
        driverTools.clearInput(DESCRIPTION_TEXTAREA);
        driverTools.sendKeys(DESCRIPTION_TEXTAREA, description);
    }

    public void clickCreateLink(){
        driverTools.waitForElementsToAppear(CREATE_LINK_BTN);
        driverTools.clickWithJavascriptExecutor(CREATE_LINK_BTN);
    }

    public boolean isLinkCreated(){
        return driverTools.isElementPresent(SUCCESS_MESSAGE);
    }

    public boolean isFirstRowContainsProductTypeID(String firstPartnerName) {
        Locator firstRowData = getFirstDataRowLocator(firstPartnerName);
        driverTools.waitForElementToAppear(firstRowData, 4).orElseThrow();
        return driverTools.getText(firstRowData).orElseThrow().contains(firstPartnerName);
    }

    public void goToMoreActionMenu(String target) {
        driverTools.waitForElementToAppear(MORE_ACTION_DROPDOWN);
        driverTools.scrollElementToCenter(MORE_ACTION_DROPDOWN);
        driverTools.click(MORE_ACTION_DROPDOWN);

        driverTools.waitForElementToAppear(MORE_ACTION_SELECTIONS, 3);
        List<WebElement> selections = driverTools.elementsUntilExist(MORE_ACTION_SELECTIONS).orElseThrow();
        for (WebElement selection : selections) {
            String elementText = driverTools.getText(selection);
            if (elementText.equalsIgnoreCase(target)) {
                try {
                    driverTools.click(selection);
                } catch (ElementClickInterceptedException | StaleElementReferenceException er1) {
                    driverTools.clickElementByCoordinateDWeb(selection);
                }
                break;
            }
        }
    }

    public boolean validateAttributionWindowOptions() {
        driverTools.waitForElementToAppear(ATTRIBUTION_MODAL, 2);

        driverTools.waitForElementToAppear(ATTRIBUTION_WINDOW_OPTION, 2);
        List<WebElement> selections = driverTools.elementsUntilExist(ATTRIBUTION_WINDOW_OPTION).orElseThrow();
        int[] arrCount = new int[3];

        for (WebElement selection : selections) {
            String text = driverTools.getText(selection);
            if (text.equalsIgnoreCase("24 hours")) arrCount[0]++;
            else if (text.equalsIgnoreCase("7 days")) arrCount[1]++;
            else if (text.equalsIgnoreCase("30 days")) arrCount[2]++;
        }

        for (int i : arrCount) {
            if (i != 1) return false;
        }
        return true;
    }

    public void clickAddUTMSourceButton() {
        driverTools.scrollElementToCenter(ADD_UTM_SOURCE_BUTTON);
        driverTools.click(ADD_UTM_SOURCE_BUTTON);
    }

    public void insertValueToLastUTMSourceInputs(String value) {
        driverTools.waitForElementToAppear(UTM_SOURCE_INPUTS, 3);
        List<WebElement> inputs = driverTools.elementsUntilExist(UTM_SOURCE_INPUTS).orElseThrow();

        driverTools.sendKeys(inputs.get(inputs.size()-1), value);
    }

    public void clickSaveRedirection() {
        driverTools.click(SAVE_REDIRECTION_BUTTON);
    }

    public String getLastUTMSourceValue() {
        driverTools.waitForElementToAppear(UTM_SOURCE_INPUTS, 3);
        List<WebElement> inputs = driverTools.elementsUntilExist(UTM_SOURCE_INPUTS).orElseThrow();

        return driverTools.getValue(inputs.get(inputs.size()-1));
    }

    public void deleteLastUTMSourceInputs() {
        driverTools.waitForElementToAppear(UTM_SOURCE_INPUT_DELETE_BUTTONS, 2);
        List<WebElement> buttons = driverTools.elementsUntilExist(UTM_SOURCE_INPUT_DELETE_BUTTONS).orElseThrow();

        driverTools.scrollElementToCenter(buttons.get(buttons.size()-1));
        driverTools.click(buttons.get(buttons.size()-1));
    }

    public void clickPreloadTab() {
        driverTools.waitForElementToAppear(ATTRIBUTION_MODAL, 2);
        driverTools.waitForElementToAppear(PRELOAD_TAB, 2);
        driverTools.clickElementByCoordinateDWeb(PRELOAD_TAB);
    }

    public void inputPreloadMediaSource(String value) {
        driverTools.waitForElementToAppear(PRELOAD_MEDIA_SOURCE_INPUT, 2);
        driverTools.clearInput(PRELOAD_MEDIA_SOURCE_INPUT);
        driverTools.sendKeys(PRELOAD_MEDIA_SOURCE_INPUT, value);
    }

    public void savePreload() {
        driverTools.click(SAVE_PRELOAD_BUTTON);
    }

    public String getMediaSourceValue() {
        driverTools.waitForElementToAppear(PRELOAD_MEDIA_SOURCE_INPUT, 2);
        return driverTools.getValue(PRELOAD_MEDIA_SOURCE_INPUT);
    }


    public void createNON_APIPartner(String name){
        driverTools.waitForElementToAppear(NON_API_PARTNER);
        driverTools.sendKeys(NON_API_PARTNER, name);

        driverTools.waitForElementToAppear(FIRST_DROPDOWN_ITEM);
        driverTools.clickWithJavascriptExecutor(FIRST_DROPDOWN_ITEM);

        driverTools.dummyWait(5);

        driverTools.waitForElementToBeClickable(CREATE_BUTTON);
        driverTools.clickWithJavascriptExecutor(CREATE_BUTTON);

        driverTools.dummyWait(5);
    }

    public void clickCobrandCampaign(){
        driverTools.waitForElementToAppear(COBRAND_CAMPAIGN);
        driverTools.clickWithJavascriptExecutor(COBRAND_CAMPAIGN);
    }

}
