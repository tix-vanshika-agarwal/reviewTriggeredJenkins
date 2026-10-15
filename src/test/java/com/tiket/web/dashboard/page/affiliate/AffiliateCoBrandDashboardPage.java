package com.tiket.web.dashboard.page.affiliate;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;

import java.util.*;

public class AffiliateCoBrandDashboardPage<T extends WebDriver> extends BasePage<T> {
    public AffiliateCoBrandDashboardPage(Session<T> session) {
        super(session);
    }
    private static final Locator MORE_ACTIONS_BUTTON = new Locator("MORE_ACTIONS_BUTTON", By.xpath("//input[@id='more-action']/following-sibling::div[contains(@class, 'right_slot_wrapper')]"));
    private static final Locator ATTRIBUTION_BUTTON = new Locator("ATTRIBUTION_BUTTON", By.xpath("//span[text()='Attribution']/parent::div"));
    private static final Locator ATTRIBUTION_POPUP = new Locator("ATTRIBUTION_POPUP", By.xpath("//section[contains(@class, 'Modal_modal_popup')]"));
    private static final Locator ATTRIBUTION_COBRAND_TAB = new Locator("ATTRIBUTION_COBRAND_TAB", By.xpath("//span[text()='Co-Brand']/parent::button"));
    private static final Locator UTM_SOURCE_INPUT = new Locator("UTM_SOURCE_INPUT", By.xpath("//input[@id='utm-source']"));
    private static final Locator SAVE_ATTRIBUTION_BUTTON = new Locator("SAVE_ATTRIBUTION_BUTTON", By.xpath("//button[text()='Save Co-Brand']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//*[contains(@class, 'Snackbar_type_success')]"));

    private static final Locator PARTNER_NAMES = new Locator("PARTNER_NAMES", By.xpath("//tbody/tr/td[1]"));
    private static final Locator STATUS_TEXTS = new Locator("STATUS_TEXTS", By.xpath("//tbody/tr/td[7]/div"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[contains(@class,'Button_variant_tertiary')]"));
    private static final Locator SUBMIT_FILTER_BUTTON = new Locator("SUBMIT_FILTER_BUTTON", By.xpath("//button[contains(@class,'Button_variant_secondary')]"));
    private static final Locator PARTNER_NAME_FIELDS = new Locator("PARTNER_NAME_FIELDS", By.xpath("//input[@id='partner-name-or-id']"));
    private static final Locator STATUS_DROPDOWN_BUTTON = new Locator("STATUS_DROPDOWN_BUTTON", By.xpath("//div[./ul[contains(@class,'InputDropdown_dropdown')]]//button[contains(@class,'styles_clickable_icon')]"));
    private static final Locator STATUS_DROPDOWN_OPTION = new Locator("STATUS_DROPDOWN_OPTION", By.xpath("//ul[contains(@class,'InputDropdown_dropdown')]/li"));
    private static final Locator STATUS_FIELDS = new Locator("STATUS_FIELDS", By.xpath("//input[@id='status']"));
    private static final Locator START_DATE_FIELDS = new Locator("START_DATE_FIELDS", By.xpath("//input[@id='start-date']"));
    private static final Locator END_DATE_FIELDS = new Locator("END_DATE_FIELDS", By.xpath("//input[@id='end-date']"));
    private static final Locator CALENDAR_LEFT_MONTH_YEAR_TEXT = new Locator("CALENDAR_LEFT_MONTH_YEAR_TEXT", By.xpath("(//section[contains(@class,'CalendarDesktop_month')]//h2)[1]"));
    private static final Locator CALENDAR_PREV_BUTTON = new Locator("CALENDAR_PREV_BUTTON", By.xpath("//button[contains(@class,'CalendarDesktop_prev_button')]"));
    private static final Locator CALENDAR_NEXT_BUTTON = new Locator("CALENDAR_NEXT_BUTTON", By.xpath("//button[contains(@class,'CalendarDesktop_next_button')]"));
    private static final Locator SIDEBAR_LOGO = new Locator("SIDEBAR_LOGO", By.xpath("//div[contains(@class,'Sidebar_logo')]"));

    private static final Locator UTM_SOURCE_BUTTON = new Locator("UTM_SOURCE_BUTTON", By.xpath("//input[@id='utm-source']/following-sibling::div"));
    private static final Locator UTM_SOURCE_OPTION = new Locator("UTM_SOURCE_OPTION", By.xpath("//div[contains(@class,'InputSearch__select_search')]"));

    private static final Locator ADD_NEW_BUTTON = new Locator("ADD_NEW_BUTTON", By.xpath("//button[contains(@class,'Button_variant_primary')]"));
    public static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("(//div[contains(text(),'Edit') and contains(@class, 'cursor-pointer')])[1]"));
    public static final Locator BUTTON_SAVE = new Locator("BUTTON_SAVE", By.xpath("//button[normalize-space(text())='Save']"));
    public static final Locator TOAST_DATA_SAVED = new Locator("TOAST_DATA_SAVED", By.xpath("//div[contains(@class, 'Snackbar_text_content') and text()='Your data has been saved.']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator BRAND_NAME_INPUT = new Locator("BRAND_NAME_INPUT", By.xpath("//input[@name='brandName']"));
    private static final Locator PARTNER_WEB_INPUT = new Locator("PARTNER_WEB_INPUT", By.xpath("//input[@name='partnerWeb']"));
    private static final Locator CAMPAIGN_START_DATE_INPUT = new Locator("CAMPAIGN_START_DATE_INPUT", By.xpath("//input[@id='start-date']"));
    private static final Locator CAMPAIGN_STATUS_TOGGLE = new Locator("CAMPAIGN_STATUS_TOGGLE", By.xpath("//input[@type='checkbox']"));
    private static final Locator CAMPAIGN_NAME_ID_INPUT = new Locator("CAMPAIGN_NAME_ID_INPUT", By.xpath("//textarea[@name='campaignNameId']"));
    private static final Locator CAMPAIGN_NAME_EN_INPUT = new Locator("CAMPAIGN_NAME_EN_INPUT", By.xpath("//textarea[@name='campaignNameEn']"));
    private static final Locator CAMPAIGN_URL_INPUT = new Locator("CAMPAIGN_URL_INPUT", By.xpath("//input[@name='campaignSlug']"));
    private static final Locator CAMPAIGN_HEADER_LOGO_UPLOAD_INPUT = new Locator("CAMPAIGN_HEADER_LOGO_UPLOAD_INPUT", By.xpath("(//input[@type='file'])[1]"));
    private static final Locator CAMPAIGN_HEADER_BACKGROUND_BANNER_UPLOAD_INPUT = new Locator("CAMPAIGN_HEADER_BACKGROUND_BANNER_UPLOAD_INPUT", By.xpath("(//input[@type='file'])[2]"));
    private static final Locator CAMPAIGN_TITLE_ID_INPUT = new Locator("CAMPAIGN_TITLE_ID_INPUT", By.xpath("//textarea[@name='campaignTitleId']"));
    private static final Locator CAMPAIGN_TITLE_EN_INPUT = new Locator("CAMPAIGN_TITLE_EN_INPUT", By.xpath("//textarea[@name='campaignTitleEn']"));
    private static final Locator PRODUCT_OFFER_NAME_ID_INPUT = new Locator("PRODUCT_OFFER_NAME_ID_INPUT", By.xpath("//input[@name='productNameId']"));
    private static final Locator PRODUCT_OFFER_NAME_EN_INPUT = new Locator("PRODUCT_OFFER_NAME_EN_INPUT", By.xpath("//input[@name='productNameEn']"));
    private static final Locator PRODUCT_OFFER_URL_INPUT = new Locator("PRODUCT_OFFER_URL_INPUT", By.xpath("//input[@name='productUrl']"));
    private static final Locator PRODUCT_OFFER_ICON_UPLOAD_INPUT = new Locator("PRODUCT_OFFER_ICON_UPLOAD_INPUT", By.xpath("(//input[@type='file'])[3]"));
    private static final Locator HIGHLIGHT_CARD_SECTION_NAME_ID_INPUT = new Locator("HIGHLIGHT_CARD_SECTION_NAME_ID_INPUT", By.xpath("//input[@name='sectionNameId']"));
    private static final Locator HIGHLIGHT_CARD_SECTION_NAME_EN_INPUT = new Locator("HIGHLIGHT_CARD_SECTION_NAME_EN_INPUT", By.xpath("//input[@name='sectionNameEn']"));
    private static final Locator HIGHLIGHT_CARD1_TITLE_ID_INPUT = new Locator("HIGHLIGHT_CARD1_TITLE_ID_INPUT", By.xpath("(//input[@name='titleId'])[1]"));
    private static final Locator HIGHLIGHT_CARD1_TITLE_EN_INPUT = new Locator("HIGHLIGHT_CARD1_TITLE_EN_INPUT", By.xpath("(//input[@name='titleEn'])[1]"));
    private static final Locator HIGHLIGHT_CARD1_DESC_ID_INPUT = new Locator("HIGHLIGHT_CARD1_DESC_ID_INPUT", By.xpath("(//textarea[@name='descriptionId'])[1]"));
    private static final Locator HIGHLIGHT_CARD1_DESC_EN_INPUT = new Locator("HIGHLIGHT_CARD1_DESC_EN_INPUT", By.xpath("(//textarea[@name='descriptionEn'])[1]"));
    private static final Locator HIGHLIGHT_CARD1_FILE_UPLOAD_INPUT = new Locator("HIGHLIGHT_CARD1_FILE_UPLOAD_INPUT", By.xpath("(//input[@type='file'])[4]"));
    private static final Locator HIGHLIGHT_CARD2_TITLE_ID_INPUT = new Locator("HIGHLIGHT_CARD2_TITLE_ID_INPUT", By.xpath("(//input[@name='titleId'])[2]"));
    private static final Locator HIGHLIGHT_CARD2_TITLE_EN_INPUT = new Locator("HIGHLIGHT_CARD2_TITLE_EN_INPUT", By.xpath("(//input[@name='titleEn'])[2]"));
    private static final Locator HIGHLIGHT_CARD2_DESC_ID_INPUT = new Locator("HIGHLIGHT_CARD2_DESC_ID_INPUT", By.xpath("(//textarea[@name='descriptionId'])[2]"));
    private static final Locator HIGHLIGHT_CARD2_DESC_EN_INPUT = new Locator("HIGHLIGHT_CARD2_DESC_EN_INPUT", By.xpath("(//textarea[@name='descriptionEn'])[2]"));
    private static final Locator HIGHLIGHT_CARD2_FILE_UPLOAD_INPUT = new Locator("HIGHLIGHT_CARD2_FILE_UPLOAD_INPUT", By.xpath("(//input[@type='file'])[5]"));
    private static final Locator HOW_TO_CONTENT_TITLE_ID_INPUT = new Locator("HOW_TO_CONTENT_TITLE_ID_INPUT", By.xpath("(//input[@name='titleId'])[3]"));
    private static final Locator HOW_TO_CONTENT_TITLE_EN_INPUT = new Locator("HOW_TO_CONTENT_TITLE_EN_INPUT", By.xpath("(//input[@name='titleEn'])[3]"));
    private static final Locator HOW_TO_CONTENT_DESC_ID_INPUT = new Locator("HOW_TO_CONTENT_DESC_ID_INPUT", By.xpath("(//textarea[@name='descriptionId'])[3]"));
    private static final Locator HOW_TO_CONTENT_DESC_EN_INPUT = new Locator("HOW_TO_CONTENT_DESC_EN_INPUT", By.xpath("(//textarea[@name='descriptionEn'])[3]"));
    private static final Locator HOW_TO_CONTENT_FILE_UPLOAD_INPUT = new Locator("HOW_TO_CONTENT_FILE_UPLOAD_INPUT", By.xpath("(//input[@type='file'])[6]"));
    private static final Locator TNC_ID_INPUT = new Locator("TNC_ID_INPUT", By.xpath("(//div[@class='ql-editor ql-blank']/p)[1]"));
    private static final Locator TNC_EN_INPUT = new Locator("TNC_EN_INPUT", By.xpath("//div[@class='ql-editor ql-blank']/p"));


    public void goToCoBrandDashboardPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {
                driver.get("https://gatotkaca.tiket.com/admin/affiliate/nona");
            }
            case PREPROD -> {
                driver.get("https://preprod.tiket.com/admin/affiliate/nona");
            }
            case PROD -> {
                driver.get("https://www.tiket.com/admin/affiliate/nona");
            }
        }
    }

    public String getRandomDisplayedPartnerName() {
        List<WebElement> listOfPartnerNames = driverTools.elementsUntilExist(PARTNER_NAMES).get();
        int randomInt = CoreFunctions.generateRandomInt(0,listOfPartnerNames.size()-1);

        return driverTools.getText(listOfPartnerNames.get(randomInt));
    }

    public void applyFilter(String partnerName, String status, String startDate, String endDate) throws InterruptedException {
        clickResetFilterButton();

        if (!partnerName.isEmpty()) {
            driverTools.clearInput(PARTNER_NAME_FIELDS);
            driverTools.sendKeys(PARTNER_NAME_FIELDS, partnerName.toLowerCase());
        }

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
            driverTools.click(START_DATE_FIELDS);

            String initialDisplayedMonthYearLeft = driverTools.getText(CALENDAR_LEFT_MONTH_YEAR_TEXT).get();
            int displayedMonthLeft = CoreFunctions.monthToMonthNumber(initialDisplayedMonthYearLeft.split(" ")[0]);
            int displayedYearLeft = Integer.parseInt(initialDisplayedMonthYearLeft.split(" ")[1]);

            int targetStartMonthParam = Integer.parseInt(startDate.split("-")[1]);
            int targetStartYearParam = Integer.parseInt(startDate.split("-")[2]);

            int startMonthDiff = (((displayedYearLeft - 1) * 12) + displayedMonthLeft) - (((targetStartYearParam - 1) * 12) + targetStartMonthParam);
            if (startMonthDiff > 0) {
                clickMoveMonthButton(startMonthDiff, "prev");
            } else if (startMonthDiff < 0) {
                clickMoveMonthButton(startMonthDiff*-1, "next");
            }
            driverTools.click(getDateLocator(startDate.split("-")[0], CoreFunctions.getMonthName(targetStartMonthParam, "en"), String.valueOf(targetStartYearParam)));

            int targetEndMonthParam = Integer.parseInt(endDate.split("-")[1]);
            int targetEndYearParam = Integer.parseInt(endDate.split("-")[2]);

            int endMonthDiff = (((targetEndYearParam - 1) * 12) + targetEndMonthParam) - (((targetStartYearParam - 1) * 12) + targetStartMonthParam);
            if (endMonthDiff > 0) {
                clickMoveMonthButton(endMonthDiff, "next");
            } else if (endMonthDiff < 0) {
                throw new SkipException("End Date can't be earlier than start date, change testdata");
            }
            driverTools.click(getDateLocator(endDate.split("-")[0], CoreFunctions.getMonthName(targetEndMonthParam, "en"), String.valueOf(targetEndYearParam)));

            driverTools.clickElementByCoordinateDWeb(SIDEBAR_LOGO);
        }

        clickSubmitFilterButton();
        Thread.sleep(500);
    }

    private void clickSubmitFilterButton() {
        driverTools.click(SUBMIT_FILTER_BUTTON);
    }

    public void clickResetFilterButton() throws InterruptedException {
        driverTools.click(RESET_FILTER_BUTTON);
        String text = "";

        for(int i = 0; i < 5; i++) {
            Thread.sleep(500);
            text = driverTools.getText(driverTools.elementsUntilExist(PARTNER_NAMES).get().get(0));
            if (!text.isEmpty()) {
                break;
            }
        }
    }

    public boolean checkFilterResult(String partnerName, String status, String startDate, String endDate) {
        if (!partnerName.isEmpty()) {
            List<WebElement> partnerNames = driverTools.elementsUntilExist(PARTNER_NAMES).get();

            for (int i = 0; i < partnerNames.size(); i++) {
                String displayedPartnerName = driverTools.getText(partnerNames.get(i));
                if (!displayedPartnerName.toLowerCase().contains(partnerName.toLowerCase())) {
                    logger.log("index : "+ i +"\ndisplayedPartnerName : " + displayedPartnerName);
                    return false;
                }
            }
        }

        if (!status.isEmpty()) {
            List<WebElement> statusTexts = driverTools.elementsUntilExist(STATUS_TEXTS).get();

            for (int i = 0; i < statusTexts.size(); i++) {
                String displayedStatus = driverTools.getText(statusTexts.get(i));
                if (!displayedStatus.toLowerCase().contains(status.toLowerCase())) {
                    logger.log("index : "+ i +"\ndisplayedStatus : " + displayedStatus);
                    return false;
                }
            }
        }

        if (!startDate.isEmpty()) {
            //TODO: Need to implement if needed
        }

        if (!endDate.isEmpty()) {
            //TODO: Need to implement if needed
        }

        return true;
    }

    public void clickAddNewButton() {
        driverTools.click(ADD_NEW_BUTTON);
    }

    public List<String> extractDisplayedCoBrandData() {
        driverTools.elementsUntilExist(PARTNER_NAMES);

        return driverTools.getTexts(PARTNER_NAMES);
    }

    public List<String> extractFilterFieldData() {
        List<String> filterFieldValue = new ArrayList<>();

        filterFieldValue.add(driverTools.getValue(PARTNER_NAME_FIELDS));
        filterFieldValue.add(driverTools.getValue(STATUS_FIELDS));
        filterFieldValue.add(driverTools.getValue(START_DATE_FIELDS));
        filterFieldValue.add(driverTools.getValue(END_DATE_FIELDS));

        return filterFieldValue;
    }

    public void clickEditButton() {
        driverTools.waitForElementToAppear(EDIT_BUTTON, 1);
        driverTools.click(EDIT_BUTTON);
    }

    public void updateBrandName(String brandName) {
        driverTools.waitForElementToAppear(BRAND_NAME_INPUT, 1);
        driverTools.clearInput(BRAND_NAME_INPUT);
        driverTools.sendKeys(BRAND_NAME_INPUT, brandName);
    }

    public void clickSaveButton() {
        driverTools.clickWithJavascriptExecutor(BUTTON_SAVE);
    }

    public boolean isCoBrandUpdated() {
            driverTools.waitForElementToAppear(TOAST_DATA_SAVED, 1);
            return driverTools.isElementDisplayed(TOAST_DATA_SAVED);
    }

    public void searchByPartnerName(String partnerName) {
        driverTools.waitForElementToAppear(PARTNER_NAME_FIELDS, 5);
        driverTools.clearInput(PARTNER_NAME_FIELDS);
        driverTools.sendKeys(PARTNER_NAME_FIELDS, partnerName);
        driverTools.click(SUBMIT_FILTER_BUTTON);
        driverTools.dummyWait(1);
    }

    public void goToAttributionConfigPage(String utmSource) {
        driverTools.waitForElementToAppear(MORE_ACTIONS_BUTTON, 5);
        driverTools.click(MORE_ACTIONS_BUTTON);
        driverTools.click(ATTRIBUTION_BUTTON);
        driverTools.waitForElementToAppear(ATTRIBUTION_POPUP, 5);

        driverTools.waitForElementToAppear(ATTRIBUTION_COBRAND_TAB, 5);
        driverTools.click(ATTRIBUTION_COBRAND_TAB);
        driverTools.waitForElementToAppear(UTM_SOURCE_INPUT, 5);
        String utmSourceWithRandom = utmSource + "-" + CoreFunctions.randomName();
        driverTools.sendKeys(UTM_SOURCE_INPUT, utmSourceWithRandom);
        driverTools.click(SAVE_ATTRIBUTION_BUTTON);
    }

    public boolean isAttributionConfigPage() {
        driverTools.waitForElementToAppear(ATTRIBUTION_POPUP, 5);
        driverTools.waitForElementToAppear(ATTRIBUTION_COBRAND_TAB, 5);
        driverTools.waitForElementToAppear(UTM_SOURCE_INPUT, 5);
        return driverTools.isElementDisplayed(ATTRIBUTION_POPUP)
                && driverTools.isElementDisplayed(ATTRIBUTION_COBRAND_TAB)
                && driverTools.isElementDisplayed(UTM_SOURCE_INPUT);
    }


    public boolean isCoBrandDashboardPage() {
        driverTools.dummyWait(1);
       return driverTools.getCurrentURL().contains("admin/affiliate/nona");
    }

    public void clickSubmitButton() throws InterruptedException {
        driverTools.scrollIntoWebView(SUBMIT_BUTTON);
        try {
            driverTools.click(SUBMIT_BUTTON);
        } catch (ElementClickInterceptedException e) {
            if (driverTools.waitForElementToAppear(RESET_FILTER_BUTTON).isEmpty()) {
                driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
                driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
            }

            if (driverTools.waitForElementToAppear(RESET_FILTER_BUTTON).isEmpty()) {
                driverTools.clickElementByCoordinateDWeb(SUBMIT_BUTTON);
                driverTools.clickElementByCoordinateDWeb(SUBMIT_BUTTON);
            }

            if (driverTools.waitForElementToAppear(RESET_FILTER_BUTTON).isEmpty()) {
                throw new NoSuchElementException();
            }
        }
    }

    public Map<String, String> fillData(Map<String, String> param) {
        Map<String, String> createdCoBrand = new HashMap<>();

        createdCoBrand.put("brandName", fillBrandName(param.get("brandName") + "-" + System.currentTimeMillis()));
        createdCoBrand.put("partnerWebsite", fillPartnerWebsite(param.get("partnerWebsite")));

        Map<String, String> campaignDate = fillCampaignStartDate(param.get("campaignStartDateRange"), param.get("campaignDays"));
        createdCoBrand.put("campaignStartDate", campaignDate.get("campaignStartDate"));
        createdCoBrand.put("campaignEndDate", campaignDate.get("campaignEndDate"));

        createdCoBrand.put("campaignStatus", toggleCampaignStatus(Boolean.parseBoolean(param.get("campaignStatus"))));
        createdCoBrand.put("campaignNameID", fillCampaignNameID(param.get("campaignNameID") + "-" + System.currentTimeMillis()));
        createdCoBrand.put("campaignNameEN", fillCampaignNameEN(param.get("campaignNameEN") + "-" + System.currentTimeMillis()));
        createdCoBrand.put("campaignURL", fillCampaignURL(param.get("campaignURL") + "-" + System.currentTimeMillis()));
        createdCoBrand.put("campaignHeaderLogo", uploadCampaignHeaderLogo(param.get("campaignHeaderLogo")));
        createdCoBrand.put("campaignHeaderBackgroundBanner", uploadCampaignHeaderBackgroundBanner(param.get("campaignHeaderBackgroundBanner")));
        createdCoBrand.put("campaignTitleID", fillCampaignTitleID(param.get("campaignTitleID") + "-" + System.currentTimeMillis()));
        createdCoBrand.put("campaignTitleEN", fillCampaignTitleEN(param.get("campaignTitleEN") + "-" + System.currentTimeMillis()));

        createdCoBrand.put("productOfferNameID", fillProductOfferNameID(param.get("productOfferNameID") + "-" + System.currentTimeMillis()));
        createdCoBrand.put("productOfferNameEN", fillProductOfferNameEN(param.get("productOfferNameEN") + "-" + System.currentTimeMillis()));
        createdCoBrand.put("productOfferURL", fillProductOfferURL(param.get("productOfferURL")));
        createdCoBrand.put("productOfferIcon", uploadProductOfferIcon(param.get("productOfferIcon")));

        createdCoBrand.put("highlightSectionCardNameID", fillHighlightSectionCardNameID(param.get("highlightSectionCardNameID") + "-" + System.currentTimeMillis()));
        createdCoBrand.put("highlightSectionCardNameEN", fillHighlightSectionCardNameEN(param.get("highlightSectionCardNameEN") + "-" + System.currentTimeMillis()));

        createdCoBrand.put("highlightCardTitleID1", fillHighlightCardTitleID1(param.get("highlightCardTitleID1") + "-" + System.currentTimeMillis()));
        createdCoBrand.put("highlightCardTitleEN1", fillHighlightCardTitleEN1(param.get("highlightCardTitleEN1") + "-" + System.currentTimeMillis()));
        createdCoBrand.put("highlightCardDescID1", fillHighlightCardDescID1(param.get("highlightCardDescID1")));
        createdCoBrand.put("highlightCardDescEN1", fillHighlightCardDescEN1(param.get("highlightCardDescEN1")));
        createdCoBrand.put("highlightCardFile1", uploadHighlightCardFile1(param.get("highlightCardFile1")));

        createdCoBrand.put("highlightCardTitleID2", fillHighlightCardTitleID2(param.get("highlightCardTitleID2") + "-" + System.currentTimeMillis()));
        createdCoBrand.put("highlightCardTitleEN2", fillHighlightCardTitleEN2(param.get("highlightCardTitleEN2") + "-" + System.currentTimeMillis()));
        createdCoBrand.put("highlightCardDescID2", fillHighlightCardDescID2(param.get("highlightCardDescID2")));
        createdCoBrand.put("highlightCardDescEN2", fillHighlightCardDescEN2(param.get("highlightCardDescEN2")));
        createdCoBrand.put("highlightCardFile2", uploadHighlightCardFile2(param.get("highlightCardFile2")));

        createdCoBrand.put("howToContentTitleID", fillHowToContentTitleID(param.get("howToContentTitleID") + "-" + System.currentTimeMillis()));
        createdCoBrand.put("howToContentTitleEN", fillHowToContentTitleEN(param.get("howToContentTitleEN") + "-" + System.currentTimeMillis()));
        createdCoBrand.put("howToContentDescID", fillHowToContentDescID(param.get("howToContentDescID")));
        createdCoBrand.put("howToContentDescEN", fillHowToContentDescEN(param.get("howToContentDescEN")));
        createdCoBrand.put("howToContentFile", uploadHowToContentFile(param.get("howToContentFile")));

        createdCoBrand.put("tncID", fillTncID(param.get("tncID")));
        createdCoBrand.put("tncEN", fillTncEN(param.get("tncEN")));

        return createdCoBrand;
    }

    private String fillTncEN(String tncEN) {
        return fillInput(TNC_EN_INPUT, tncEN);
    }

    private String fillTncID(String tncID) {
        return fillInput(TNC_ID_INPUT, tncID);
    }

    private String uploadHowToContentFile(String howToContentFile) {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/dashboard/affiliate/" + howToContentFile;
        return fillInput(HOW_TO_CONTENT_FILE_UPLOAD_INPUT, filePath);
    }

    private String fillHowToContentDescEN(String howToContentDescEN) {
        return fillInput(HOW_TO_CONTENT_DESC_EN_INPUT, howToContentDescEN);
    }

    private String fillHowToContentDescID(String howToContentDescID) {
        return fillInput(HOW_TO_CONTENT_DESC_ID_INPUT, howToContentDescID);
    }

    private String fillHowToContentTitleEN(String howToContentTitleEN) {
        return fillInput(HOW_TO_CONTENT_TITLE_EN_INPUT, howToContentTitleEN);
    }

    private String fillHowToContentTitleID(String howToContentTitleID) {
        return fillInput(HOW_TO_CONTENT_TITLE_ID_INPUT, howToContentTitleID);
    }

    private String uploadHighlightCardFile2(String highlightCardFile2) {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/dashboard/affiliate/" + highlightCardFile2;
        return fillInput(HIGHLIGHT_CARD2_FILE_UPLOAD_INPUT, filePath);
    }

    private String fillHighlightCardDescEN2(String highlightCardDescEN2) {
        return fillInput(HIGHLIGHT_CARD2_DESC_EN_INPUT, highlightCardDescEN2);
    }

    private String fillHighlightCardDescID2(String highlightCardDescID2) {
        return fillInput(HIGHLIGHT_CARD2_DESC_ID_INPUT, highlightCardDescID2);
    }

    private String fillHighlightCardTitleEN2(String highlightCardTitleEN2) {
        return fillInput(HIGHLIGHT_CARD2_TITLE_EN_INPUT, highlightCardTitleEN2);
    }

    private String fillHighlightCardTitleID2(String highlightCardTitleID2) {
        return fillInput(HIGHLIGHT_CARD2_TITLE_ID_INPUT, highlightCardTitleID2);
    }

    private String uploadHighlightCardFile1(String highlightCardFile1) {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/dashboard/affiliate/" + highlightCardFile1;
        return fillInput(HIGHLIGHT_CARD1_FILE_UPLOAD_INPUT, filePath);
    }

    private String fillHighlightCardDescEN1(String highlightCardDescEN1) {
        return fillInput(HIGHLIGHT_CARD1_DESC_EN_INPUT, highlightCardDescEN1);
    }

    private String fillHighlightCardDescID1(String highlightCardDescID1) {
        return fillInput(HIGHLIGHT_CARD1_DESC_ID_INPUT, highlightCardDescID1);
    }

    private String fillHighlightCardTitleEN1(String highlightCardTitleEN1) {
        return fillInput(HIGHLIGHT_CARD1_TITLE_EN_INPUT, highlightCardTitleEN1);
    }

    private String fillHighlightCardTitleID1(String highlightCardTitleID1) {
        return fillInput(HIGHLIGHT_CARD1_TITLE_ID_INPUT, highlightCardTitleID1);
    }

    private String fillHighlightSectionCardNameEN(String highlightSectionCardNameEN) {
        return fillInput(HIGHLIGHT_CARD_SECTION_NAME_EN_INPUT, highlightSectionCardNameEN);
    }

    private String fillHighlightSectionCardNameID(String highlightSectionCardNameID) {
        return fillInput(HIGHLIGHT_CARD_SECTION_NAME_ID_INPUT, highlightSectionCardNameID);
    }

    private String uploadProductOfferIcon(String productOfferIcon) {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/dashboard/affiliate/" + productOfferIcon;
        return fillInput(PRODUCT_OFFER_ICON_UPLOAD_INPUT, filePath);
    }

    private String fillProductOfferURL(String productOfferURL) {
        productOfferURL = "https://" + productOfferURL;
        return fillInput(PRODUCT_OFFER_URL_INPUT, productOfferURL);
    }

    private String fillProductOfferNameEN(String productOfferNameEN) {
        return fillInput(PRODUCT_OFFER_NAME_EN_INPUT, productOfferNameEN);
    }

    private String fillProductOfferNameID(String productOfferNameID) {
        return fillInput(PRODUCT_OFFER_NAME_ID_INPUT, productOfferNameID);
    }

    private String fillCampaignTitleEN(String campaignTitleEN) {
        return fillInput(CAMPAIGN_TITLE_EN_INPUT, campaignTitleEN);
    }

    private String fillCampaignTitleID(String campaignTitleID) {
        return fillInput(CAMPAIGN_TITLE_ID_INPUT, campaignTitleID);
    }

    private String uploadCampaignHeaderBackgroundBanner(String campaignHeaderBackgroundBanner) {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/dashboard/affiliate/" + campaignHeaderBackgroundBanner;
        return fillInput(CAMPAIGN_HEADER_BACKGROUND_BANNER_UPLOAD_INPUT, filePath);
    }

    private String uploadCampaignHeaderLogo(String campaignHeaderLogo) {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/dashboard/affiliate/" + campaignHeaderLogo;
        return fillInput(CAMPAIGN_HEADER_LOGO_UPLOAD_INPUT, filePath);
    }

    private String fillCampaignURL(String campaignURL) {
        return fillInput(CAMPAIGN_URL_INPUT, campaignURL);
    }

    private String fillCampaignNameEN(String campaignNameEN) {
        return fillInput(CAMPAIGN_NAME_EN_INPUT, campaignNameEN);
    }

    private String fillCampaignNameID(String campaignNameID) {
        return fillInput(CAMPAIGN_NAME_ID_INPUT, campaignNameID);
    }

    private String toggleCampaignStatus(boolean campaignStatus) {
        if (campaignStatus) {
            driverTools.clickElementByCoordinateDWeb(CAMPAIGN_STATUS_TOGGLE);
        }

        return String.valueOf(campaignStatus);
    }

    private Map<String, String> fillCampaignStartDate(String campaignStartDateRange, String campaignDays) {
        Map<String, String> campaignDate = new HashMap<>();

        String startDate = CoreFunctions.generateRandomDate(campaignStartDateRange, "dd-MM-yyyy");
        campaignDate.put("campaignStartDate", startDate);
        String endDate = CoreFunctions.convertDateFormat(CoreFunctions.addToDateYYYY_MM_DD(CoreFunctions.convertDateFormat(startDate, "dd-MM-yyyy", "yyyy-MM-dd"), Integer.parseInt(campaignDays)), "yyyy-MM-dd", "dd-MM-yyyy");
        campaignDate.put("campaignEndDate", endDate);

        driverTools.click(CAMPAIGN_START_DATE_INPUT);

        String initialDisplayedMonthYearLeft = driverTools.getText(CALENDAR_LEFT_MONTH_YEAR_TEXT).get();
        int displayedMonthLeft = CoreFunctions.monthToMonthNumber(initialDisplayedMonthYearLeft.split(" ")[0]);
        int displayedYearLeft = Integer.parseInt(initialDisplayedMonthYearLeft.split(" ")[1]);

        // expected date format is dd-mm-yyyy from the testdata (ex. 26-5-2025)
        int targetStartMonthParam = Integer.parseInt(startDate.split("-")[1]);
        int targetStartYearParam = Integer.parseInt(startDate.split("-")[2]);

        int startMonthDiff = (((displayedYearLeft - 1) * 12) + displayedMonthLeft) - (((targetStartYearParam - 1) * 12) + targetStartMonthParam);
        if (startMonthDiff > 0) {
            clickMoveMonthButton(startMonthDiff, "prev");
        } else if (startMonthDiff < 0) {
            clickMoveMonthButton(startMonthDiff*-1, "next");
        }
        driverTools.click(getDateLocator(startDate.split("-")[0], CoreFunctions.getMonthName(targetStartMonthParam, "en"), String.valueOf(targetStartYearParam)));

        int targetEndMonthParam = Integer.parseInt(endDate.split("-")[1]);
        int targetEndYearParam = Integer.parseInt(endDate.split("-")[2]);

        int endMonthDiff = (((targetEndYearParam - 1) * 12) + targetEndMonthParam) - (((targetStartYearParam - 1) * 12) + targetStartMonthParam);
        if (endMonthDiff > 0) {
            clickMoveMonthButton(endMonthDiff, "next");
        } else if (endMonthDiff < 0) {
            throw new SkipException("End Date can't be earlier than start date, change testdata");
        }
        driverTools.click(getDateLocator(endDate.split("-")[0], CoreFunctions.getMonthName(targetEndMonthParam, "en"), String.valueOf(targetEndYearParam)));

        driverTools.clickElementByCoordinateDWeb(SIDEBAR_LOGO);

        return campaignDate;
    }

    public void clickUtmSourceButton() {
        driverTools.scrollIntoWebView(UTM_SOURCE_BUTTON);
        driverTools.waitForElementToAppear(UTM_SOURCE_BUTTON, 5);
        driverTools.click(UTM_SOURCE_BUTTON);
        driverTools.waitForElementToAppear(UTM_SOURCE_OPTION, 5);
        driverTools.click(UTM_SOURCE_OPTION);
    }

    private void clickMoveMonthButton(int monthDiff, String type) {
        for (int i = 0; i < monthDiff; i++) {
            switch (type.toLowerCase()) {
                case "prev" -> {driverTools.click(CALENDAR_PREV_BUTTON);}
                case "next" -> {driverTools.click(CALENDAR_NEXT_BUTTON);}
            }
        }
    }

    private Locator getDateLocator(String date, String month, String year) {
        date = date.charAt(0) == '0'  ? date.substring(1) : date;
        String setOfDate = date + " " + month + " " + year;
        return new Locator("CALENDAR_" + setOfDate, By.xpath("//button[contains(@class,'Day_day') and .//span[contains(@aria-label,'"+setOfDate+"') and text()='"+date+"']]"));
    }

    private String fillPartnerWebsite(String partnerWebsite) {
        partnerWebsite = "https://" + partnerWebsite;
        return fillInput(PARTNER_WEB_INPUT, partnerWebsite);
    }

    private String fillBrandName(String brandName) {
        return fillInput(BRAND_NAME_INPUT, brandName);
    }

    private String fillInput(Locator locator, String value) {
        if (locator.toString().contains("@type='file'")) {
            driverTools.sendKeys(locator, value);
        } else {
            driverTools.waitForElementToAppear(locator);
            driverTools.sendKeys(locator, value);
        }
        return value;
    }

    public String getSearchedPartnerId() {
        List<WebElement> partners = driverTools.elementsUntilExist(PARTNER_NAMES).orElse(new ArrayList<>());
        if (partners.isEmpty()) return "";
        
        String fullName = driverTools.getText(partners.get(0));
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\((\\d+)\\)").matcher(fullName);
        return matcher.find() ? matcher.group(1) : "";
    }

    public void goToPartnerCobrand(String partnerId) {
        switch (Configs.ENVIRONMENT) {
            case GK -> {
                driver.get("https://gatotkaca.tiket.com/admin/affiliate/nona/"+ partnerId + "/co-brand");
            }
            case PREPROD -> {
                driver.get("https://preprod.tiket.com/admin/affiliate/nona/" + partnerId + "/co-brand");
            }
            case PROD -> {
                driver.get("https://www.tiket.com/admin/affiliate/nona/" + partnerId + "/co-brand");
            }
        }
    }

    public boolean isPartnerCobrandCreated(String brandName) {
        driverTools.refreshWebpage();
        driverTools.dummyWait(1);
       return driverTools.isElementDisplayed(new Locator("COBRAND_NAME", By.xpath("//span[normalize-space(text())='"+brandName+"']")));
    }
}
