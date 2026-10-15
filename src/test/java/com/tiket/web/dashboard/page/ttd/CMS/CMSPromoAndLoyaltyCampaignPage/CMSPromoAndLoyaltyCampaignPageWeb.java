package com.tiket.web.dashboard.page.ttd.CMS.CMSPromoAndLoyaltyCampaignPage;

import com.tiket.apiPage.TTDApi;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.apache.commons.lang3.NotImplementedException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class CMSPromoAndLoyaltyCampaignPageWeb<T extends WebDriver> extends WebPage<T> {
    public CMSPromoAndLoyaltyCampaignPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSPromoAndLoyaltyCampaignPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSPromoAndLoyaltyCampaignPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSPromoAndLoyaltyCampaignPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator ACTIONS_BUTTON = new Locator("ACTIONS_BUTTON", By.xpath("//button[contains(@class, 'btn-action-table') and contains(., 'Actions')]"));
    private static final Locator EDIT_ACTION = new Locator("EDIT_ACTION", By.xpath("//li[contains(@class,'MuiMenuItem-root') and .//span[text()='Edit']]"));
    private static final Locator INCLUDE_THINGS_TO_DO = new Locator("INCLUDE_THINGS_TO_DO", By.xpath("//button[.//span[text()='Included Things To Do']]"));
    private static final Locator ADD_TTD_BUTTON = new Locator("ADD_TTD_BUTTON", By.xpath("//button[.//span[text()='Add Things To Do']]"));
    private static final Locator PACKAGE_INPUT = new Locator("PACKAGE_INPUT", By.xpath("//div[contains(@class, 'input-field')][.//div[text()='Things To Do Name']]//input"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("(//button[normalize-space(text())='Search' and contains(@class,'tix-button')])[2]"));
    private static final Locator PARTNER_SUBSIDY = new Locator("PARTNER_SUBSIDY", By.xpath("//div[div[text()='Partner Subsidy']]//input[@type='number']"));
    private static final Locator TIKET_SUBSIDY = new Locator("TIKET_SUBSIDY", By.xpath("//div[div[text()='tiket.com Subsidy']]//input[@type='number']"));
    private static final Locator PREVIEW_BUTTON = new Locator("PREVIEW_BUTTON", By.xpath("//button[normalize-space()='See Preview']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[normalize-space()='Save']"));
    private static final Locator ADD_CAMPAIGN_BUTTON = new Locator("ADD_CAMPAIGN_BUTTON", By.xpath("//button[normalize-space()='Add New Campaign']"));
    private static final Locator QUOTA = new Locator("QUOTA", By.xpath("//input[@name='quota' and @type='number']"));
    private static final Locator CAMPAIGN_NAME_INPUT_1 = new Locator("CAMPAIGN_NAME_INPUT_1", By.xpath("//div[@class='input-field']//div[text()='Campaign Name']/following-sibling::div//input[@name='name.ID']"));
    private static final Locator CAMPAIGN_NAME_INPUT_2 = new Locator("CAMPAIGN_NAME_INPUT_2", By.xpath("//div[@class='input-field']//div[text()='Campaign Name']/following-sibling::div//input[@name='name.EN']"));
    private static final Locator CAMPAIGN_LABEL_INPUT_1 = new Locator("CAMPAIGN_LABEL_INPUT_1", By.xpath("//div[@class='tix-textarea-v2']//span[text()='Campaign Label']/following-sibling::textarea[@name='label.ID']"));
    private static final Locator  CAMPAIGN_LABEL_INPUT_2 = new Locator("CAMPAIGN_LABEL_INPUT_2", By.xpath("//div[@class='tix-textarea-v2']//span[text()='Campaign Label']/following-sibling::textarea[@name='label.EN']"));
    private static final Locator PRIORITY = new Locator("PRIORITY", By.xpath("//input[@name='priority.to' and @type='number']"));
    private static final Locator PLATFORM_BOX = new Locator("PLATFORM_BOX", By.xpath("(//div[contains(@class, 'tc-icon')])[3]"));
    private static final Locator ADD_PIC_BUTTON = new Locator("ADD_PIC_BUTTON", By.xpath("//button[.//span[text()='Add PIC']]"));
    private static final Locator PIC_NAME_INPUT = new Locator("PIC_NAME_INPUT", By.xpath("//div[@class='input-field']//div[text()='Name']/following-sibling::div//input[@name='personInCharge[0].name']"));
    private static final Locator PIC_EMAIL_INPUT = new Locator("PIC_EMAIL_INPUT", By.xpath("//div[@class='input-field']//div[text()='Email']/following-sibling::div//input[@name='personInCharge[0].emailAddress']"));
    private static final Locator PIC_NUMBER_INPUT = new Locator("PIC_NUMBER_INPUT", By.xpath("//div[@class='input-field']//div[text()='Phone Number']/following-sibling::div//input[@name='personInCharge[0].phoneNumber']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[@type='submit' and contains(text(), 'Submit')]"));
    private static final Locator UPLOAD_ICON = new Locator("UPLOAD_ICON", By.xpath("(//input[@type='file'])[2]"));
    private static final Locator SAVE_AND_CONTINUE_BUTTON = new Locator("SAVE_AND_CONTINUE_BUTTON", By.xpath("//button[normalize-space()='Save & Continue']"));
    private static final Locator START_DATE_BOX = new Locator("START_DATE_BOX", By.xpath("//div[contains(@class, 'input-name') and text()='Start Date']/following-sibling::div//input[@name='bookingPeriod.startDate']"));
    private static final Locator TODAY_DATE = new Locator("TODAY_DATE", By.xpath("//div[contains(@class, 'DayPicker-Day--today')]"));
    private static final Locator START_TIME = new Locator("START_TIME", By.xpath("(//div[@data-testid='timepicker-input'])[1]"));
    private static final Locator END_DATE_BOX = new Locator("END_DATE_BOX", By.xpath("//div[.//div[text()='End Date']]//input[@name='bookingPeriod.endDate']"));
    private static final Locator END_TIME = new Locator("END_TIME", By.xpath("(//div[@data-testid='timepicker-input'])[2]"));
    private static final Locator NOTIFY_PIC_BTN = new Locator("NOTIFY_PIC_BTN", By.xpath("//button[normalize-space(text())='Yes, Submit and Notify All PICs']"));
    private static final Locator B2C_PACKAGE = new Locator("B2C_PACKAGE", By.xpath("//button[.//span[normalize-space(text())='Packages']]"));
    private static final Locator CAMPAIGN_STATUS = new Locator("CAMPAIGN_STATUS", By.xpath("(//div[contains(@class,'tix-react-select__control')])[1]"));
    private static final Locator DROPDOWN_MENU = new Locator("DROPDOWN_MENU", By.className("tix-react-select__menu"));
    private static final Locator ACTIVE_STATUS = new Locator("ACTIVE_STATUS", By.xpath("//div[contains(@class,'tix-react-select__option') and normalize-space(text())='Active']"));
    private static final Locator INPUT_HOURS = new Locator("INPUT_HOURS", By.xpath("(//input[@name='hours'])[1]"));
    private static final Locator INPUT_MINUTES = new Locator("INPUT_MINUTES", By.xpath("(//input[@name='minutes'])[1]"));
    private static final Locator INPUT_SECONDS = new Locator("INPUT_SECONDS", By.xpath("(//input[@name='seconds'])[1]"));
    private static final Locator END_INPUT_HOURS = new Locator("END_INPUT_HOURS", By.xpath("(//input[@name='hours'])[2]"));
    private static final Locator END_INPUT_MINUTES = new Locator("END_INPUT_MINUTES", By.xpath("(//input[@name='minutes'])[2]"));
    private static final Locator END_INPUT_SECONDS = new Locator("END_INPUT_SECONDS", By.xpath("(//input[@name='seconds'])[2]"));
    private static final Locator SEATING_AUTAMATION_PACKAGE = new Locator("SEATING_AUTAMATION_PACKAGE", By.xpath("//div[contains(@class,'master-label')]//span[normalize-space(text())='Seating_automation']"));
    private static final Locator MULTIGUEST_PACKAGE = new Locator("MULTIGUEST_PACKAGE", By.xpath("//p[normalize-space()='Multiguest']"));
    private static final Locator ADD_TTD_CAMPAIGN_BUTTON = new Locator("ADD_TTD_CAMPAIGN_BUTTON", By.xpath("//button[normalize-space(text())='Add Things To Do to Campaign']"));
    private static final Locator PROMO_AND_LOYALTY_PAGE = new Locator("PROMO_AND_LOYALTY_PAGE", By.xpath("//div[contains(@class, 'header__nav-item')]//span[text()='Promo & Loyalty']"));
    private static final Locator CAMPAIGN_PAGE = new Locator("CAMPAIGN_PAGE", By.xpath("//a[@class='header__nav-menu-item' and text()='Campaign']"));
    private static final Locator SEARCH_CAMPAIGN_NAME = new Locator("SEARCH_CAMPAIGN_NAME", By.xpath("//div[contains(text(),'Campaign Name')]/following::input[@type='text'][1]"));
    private static final Locator SEARCH_CAMPAIGN = new Locator("SEARCH_CAMPAIGN", By.xpath("//form//button[@type='submit']//span"));
    // Using parent container classes
    private static final Locator CAMPAIGN_DESCRIPTION_TEXTAREA1 = new Locator("CAMPAIGN_DESCRIPTION_TEXTAREA1",
            By.xpath("//textarea[@name='description.ID']"));

    private static final Locator CAMPAIGN_DESCRIPTION_TEXTAREA2 = new Locator("CAMPAIGN_DESCRIPTION_TEXTAREA2",
            By.xpath("//textarea[@name='description.EN']"));

    public void goToPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> driver.get("https://gatotkaca.tiket.com/admin/event-ms/to-do");
            case PREPROD -> driver.get("https://preprod.tiket.com/admin/event-ms/to-do");
            case PROD -> driver.get("https://www.tiket.com/admin/event-ms/to-do");
        }
        driverTools.waitForWebToLoad();
    }

    public void goToPromoAndLoyaltyPage() {
        driverTools.waitForElementToAppear(PROMO_AND_LOYALTY_PAGE,10);
        driverTools.click(PROMO_AND_LOYALTY_PAGE);

        driverTools.waitForElementToAppear(CAMPAIGN_PAGE,10);
        driverTools.click(CAMPAIGN_PAGE);
    }

    public void clickActionsButton(){
        driverTools.waitForWebToLoad();

        driverTools.waitForElementToAppear(ACTIONS_BUTTON,10);
        driverTools.click(ACTIONS_BUTTON);
    }

    public void clickEditAction() {
        driverTools.waitForElementToAppear(EDIT_ACTION, 10);
        driverTools.click(EDIT_ACTION);
    }

    public void goToIncludeThingsToDo() {
        driverTools.waitForElementToAppear(INCLUDE_THINGS_TO_DO, 10);
        driverTools.click(INCLUDE_THINGS_TO_DO);
    }

    public void clickAddThingsToDoButton() {
        driverTools.waitForElementToAppear(ADD_TTD_BUTTON, 10);
        driverTools.scrollToElement(ADD_TTD_BUTTON);
        driverTools.click(ADD_TTD_BUTTON);
    }

    public void searchPackage(String packageName) {
        driverTools.waitForElementToAppear(PACKAGE_INPUT, 10);
        driverTools.sendKeys(PACKAGE_INPUT, packageName);
        driverTools.waitForWebToLoad();

        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    private Locator getProductLocator(String name) {
        return new Locator("PRODUCT_" + name.toUpperCase().replace(" ", "_"),
                By.xpath("//span[contains(normalize-space(),'" + name + "')]"));
    }

    private Locator getPackageLocator(String name) {
        return new Locator("PACKAGE_" + name.toUpperCase().replace(" ", "_"),
                By.xpath("//p[contains(text(), '" + name + "')]"));
    }

    public void selectPackage(String productName,String Package ) {
        Locator PRODUCT = getProductLocator(productName);
        Locator PACKAGE = getPackageLocator(Package);

        driverTools.waitForElementToAppear(PRODUCT, 20);
        driverTools.clickWithJavascriptExecutor(PRODUCT);

        driverTools.waitForElementToAppear(PACKAGE, 10);
        driverTools.click(PACKAGE);
    }

    public void enterSubsidy(String subsidy) {
        driverTools.sendKeys(PARTNER_SUBSIDY, subsidy);
        driverTools.sendKeys(TIKET_SUBSIDY, subsidy);

        driverTools.click(PREVIEW_BUTTON);

        driverTools.waitForElementToAppear(ACTIONS_BUTTON,10);
        driverTools.click(ACTIONS_BUTTON);

        driverTools.waitForElementToAppear(EDIT_ACTION,10);
        driverTools.click(EDIT_ACTION);

        clickSaveButton();
    }

    public void clickSaveButton() {
        driverTools.waitForElementToAppear(SAVE_BUTTON, 10);
        driverTools.scrollToElement(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public boolean isSubsidyAdded(String subsidy) {
        Locator PACKAGE = new Locator("PACKAGE",By.xpath("(//div[contains(@class, 'campaign-product__list-table-item-package') and contains(., '" + subsidy + "')])[1]"));
        driverTools.waitForElementToAppear(PACKAGE, 10);

        return driverTools.isElementDisplayed(PACKAGE);
    }

    public void clicksAddCampaignButton() {
        driverTools.waitForElementToAppear(ADD_CAMPAIGN_BUTTON,10);
        driverTools.click(ADD_CAMPAIGN_BUTTON);
    }

    public void enterCampaignNameAndQuota(String quota,String campaignName) {
        driverTools.waitForElementToAppear(QUOTA,10);

        driverTools.sendKeys(QUOTA, quota);
        driverTools.sendKeys(CAMPAIGN_NAME_INPUT_1, campaignName);
        driverTools.sendKeys(CAMPAIGN_NAME_INPUT_2, campaignName);
        driverTools.dummyWait(5);
    }

    public void enterCampaignLabel(String label) {
        driverTools.sendKeys(CAMPAIGN_LABEL_INPUT_1, label);
        driverTools.sendKeys(CAMPAIGN_LABEL_INPUT_2, label);
        driverTools.dummyWait(5);
    }

    public void selectDateAndTime(String time){
        driverTools.clickWithJavascriptExecutor(START_DATE_BOX);
        driverTools.clickWithJavascriptExecutor(TODAY_DATE);

        driverTools.clickWithJavascriptExecutor(START_TIME);
        driverTools.sendKeys(INPUT_HOURS, time);
        driverTools.sendKeys(INPUT_MINUTES, time);
        driverTools.sendKeys(INPUT_SECONDS, time);

        driverTools.clickWithJavascriptExecutor(END_DATE_BOX);
        LocalDate targetDate = LocalDate.now().plusDays(10);
        int targetDay = targetDate.getDayOfMonth();
        Locator END_DATE = new Locator("END_DATE", By.xpath("//div[contains(@class,'DayPicker-Day')]//div[text()='" + targetDay + "']"));
        driverTools.clickWithJavascriptExecutor(END_DATE);

        driverTools.clickWithJavascriptExecutor(END_TIME);
        driverTools.sendKeys(END_INPUT_HOURS, time);
        driverTools.sendKeys(END_INPUT_MINUTES, time);
        driverTools.sendKeys(END_INPUT_SECONDS, time);
        driverTools.dummyWait(5);
    }

    public void selectCampaignStatus() {
        driverTools.waitForElementToBeClickable(CAMPAIGN_STATUS, 10);
        driverTools.scrollToElement(CAMPAIGN_STATUS);
        driverTools.click(CAMPAIGN_STATUS);

        driverTools.waitForElementToAppear(DROPDOWN_MENU, 10);

        driverTools.waitForElementToBeClickable(ACTIVE_STATUS, 10);
        driverTools.clickWithJavascriptExecutor(ACTIVE_STATUS);
        driverTools.dummyWait(5);
    }

    public void selectPriorityAndPlatform( String priority){
        driverTools.sendKeys(PRIORITY, priority);

        driverTools.waitForElementToAppear(PLATFORM_BOX,10);
        driverTools.clickWithJavascriptExecutor(PLATFORM_BOX);
    }

    public void enterPICDetails(String picName, String picMail, String picNumber){
        driverTools.waitForElementToAppear(ADD_PIC_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(ADD_PIC_BUTTON);

        driverTools.waitForElementToAppear(PIC_NAME_INPUT, 10);
        driverTools.sendKeys(PIC_NAME_INPUT, picName);
        driverTools.sendKeys(PIC_EMAIL_INPUT, picMail);
        driverTools.sendKeys(PIC_NUMBER_INPUT, picNumber);

        driverTools.waitForElementToBeClickable(SUBMIT_BUTTON,5);
        driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
        driverTools.dummyWait(5);
    }

    public void uploadCampaignIcon() {
        String directoryPath = System.getProperty("user.dir")
                + File.separator + "src"
                + File.separator + "test"
                + File.separator + "resources"
                + File.separator + "dashboard"
                + File.separator + "ttd"
                + File.separator;

        String fileName = "demo icon 72x72.jpg";
        String filePath = directoryPath + fileName;

        Path absolutePath = Paths.get(filePath).toAbsolutePath();

        if (!Files.exists(absolutePath)) {
            throw new RuntimeException("File not found: " + absolutePath);
        }

        driverTools.sendKeys(UPLOAD_ICON, absolutePath.toString());
        driverTools.waitForWebToLoad();
        driverTools.dummyWait(5);
    }

    public void clickSaveAndContinueButton(){
        driverTools.waitForElementToAppear(SAVE_AND_CONTINUE_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(SAVE_AND_CONTINUE_BUTTON);
        driverTools.dummyWait(5);
    }

    public void selectSeatingPackage(){
        driverTools.waitForElementToAppear(SEATING_AUTAMATION_PACKAGE, 10);
        driverTools.clickWithJavascriptExecutor(SEATING_AUTAMATION_PACKAGE);

        driverTools.waitForElementToAppear(MULTIGUEST_PACKAGE,10);
        driverTools.clickWithJavascriptExecutor(MULTIGUEST_PACKAGE);
    }

    public void clickAddTTDButton(){
        driverTools.waitForElementToAppear(ADD_TTD_CAMPAIGN_BUTTON,10);
        driverTools.clickWithJavascriptExecutor(ADD_TTD_CAMPAIGN_BUTTON);

        driverTools.waitForElementToBeClickable(NOTIFY_PIC_BTN, 10);
        driverTools.clickWithJavascriptExecutor(NOTIFY_PIC_BTN);
    }

    public void openB2CPageInNewTab() {
        String url ="";
        switch (Configs.ENVIRONMENT) {
            case GK -> url = "https://gatotkaca.tiket.com/to-do/seatingautomation";
            case PREPROD -> url = "https://preprod.tiket.com/to-do/seatingautomation";
            case PROD -> url = "https://www.tiket.com/to-do/seatingautomation";
        }

        driverTools.openNewTabAndPasteUrl(url);
        driverTools.waitForElementToAppear(B2C_PACKAGE, 10);
        driverTools.click(B2C_PACKAGE);
    }

    public boolean isCampaignAdded(String label) {
        Locator CAMPAIGN = new Locator("CAMPAIGN", By.xpath("//div[contains(@class, 'Label_truncation_lines') and normalize-space(text())='" + label + "']"));
        driverTools.waitForElementToAppear(CAMPAIGN, 10);
        driverTools.scrollToElement(CAMPAIGN);
        return driverTools.isElementDisplayed(CAMPAIGN);
    }

    public void searchCampaign(String campaignName) {
        driverTools.waitForElementToAppear(SEARCH_CAMPAIGN_NAME, 10);
        driverTools.sendKeys(SEARCH_CAMPAIGN_NAME, campaignName);

        driverTools.waitForElementToBeClickable(SEARCH_CAMPAIGN, 10);
        driverTools.clickWithJavascriptExecutor(SEARCH_CAMPAIGN);
    }

    public void updateCampaignDescription(String campaignDescription) {
        driverTools.waitForElementToAppear(CAMPAIGN_DESCRIPTION_TEXTAREA1, 10);
        driverTools.waitForElementToBeClickable(CAMPAIGN_DESCRIPTION_TEXTAREA1, 10);
        driverTools.clearInput(CAMPAIGN_DESCRIPTION_TEXTAREA1);
        driverTools.waitForElementToBeClickable(CAMPAIGN_DESCRIPTION_TEXTAREA1, 10);
        driverTools.sendKeys(CAMPAIGN_DESCRIPTION_TEXTAREA1, campaignDescription);
        driverTools.waitForElementToBeClickable(CAMPAIGN_DESCRIPTION_TEXTAREA2, 10);
        driverTools.clearInput(CAMPAIGN_DESCRIPTION_TEXTAREA2);
        driverTools.sendKeys(CAMPAIGN_DESCRIPTION_TEXTAREA2, campaignDescription);
    }

    public boolean validateCampaignDescriptionInB2CAPI(String productUrl, String expectedDescription) {
        try {
            TTDApi<T> ttdApi = new TTDApi<>(this.session);
            ApiResponse response = ttdApi.validateCampaignDescriptionInB2C(productUrl);
            
            if (response.getStatusCode() == 200) {
                // Parse the response to check if the campaign description exists
                JSONObject responseJson = new JSONObject(response.getBody());
                String responseBody = responseJson.toString();
                
                // Check if the expected description is present in the API response
                boolean descriptionFound = responseBody.contains(expectedDescription);
                logger.log("Expected description '" + expectedDescription + "' found in API response: " + descriptionFound);
                
                return descriptionFound;
            } else {
                logger.log("API call failed with status code: " + response.getStatusCode());
                return false;
            }
        } catch (Exception e) {
            logger.log("Error validating campaign description in B2C API: " + e.getMessage());
            return false;
        }
    }
}
