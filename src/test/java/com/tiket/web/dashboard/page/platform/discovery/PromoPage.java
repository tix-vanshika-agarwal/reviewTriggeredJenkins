package com.tiket.web.dashboard.page.platform.discovery;

import com.tiket.core.CoreFunctions;
import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PromoPage<T extends WebDriver> extends BasePage<T> {
    public PromoPage(Session<T> session){
        super(session);
    }
    ILogger logger = BaseTest.mainLogger.get();
    private String generatedSlug;
    private static final String PROMO_URL = "/admin/promo";
    private static final Locator LOGGED_IN_INFO = new Locator("LOGGED_IN_INFO", By.xpath("//div[@class='account-toolbar']"));
    private static final Locator CREATE_PROMO_BUTTON = new Locator("CREATE_PROMO_BUTTON", By.xpath("//button[@type='button' and text() = 'CREATE PROMO']"));
    private static final Locator PROMO_PAGE_BUTTON = new Locator("PROMO_PAGE_BUTTON", By.xpath("//div[@class = 'label' and text() = 'Promo']"));
    private static final Locator CURRENCY_DROPDOWN = new Locator("CURRENCY_DROPDOWN", By.xpath("//input[@data-error-message='Select one of the options.']"));
    private static final Locator CURRENCY_IDR = new Locator("CURRENCY_IDR", By.xpath("//div[@class ='container-options']/label[contains(text(), 'IDR')]"));
    private static final Locator SAVE_CURRENCY = new Locator("SAVE_CURRENCY", By.xpath("//button[@type ='button'and text() = 'Save']"));
    private static final Locator GENERAL_SETTING_COMPONENT = new Locator("GENERAL_SETTING_COMPONENT", By.xpath("//div[@class ='p1 title text-darkest-gray' and text() = 'General Settings']"));
    private static final Locator GENERAL_CONTENT_COMPONENT = new Locator("GENERAL_CONTENT_COMPONENT", By.xpath("//div[@class ='p1 title text-darkest-gray' and text() = 'General Contents']"));
    private static final Locator PROMO_NAME_INPUT = new Locator("PROMO_NAME_INPUT", By.xpath("//input[@name='name']"));
    private static final Locator PARENT_CATEGORY_DROPDOWN = new Locator("PARENT_CATEGORY_DROPDOWN", By.xpath("(//div[@class = 'dropdown-menu'])[2]"));
    private static final Locator PARENT_CATEGORY_TESTING = new Locator("PARENT_HOTEL", By.xpath("//label[@class = 'option' and text() = 'Auto-name']"));
    private static final Locator CATEGORY_VALUE = new Locator("CATEGORY_VALUE", By.xpath("(//div[@class = 'p3 item'])[1]"));
    private static final Locator SUB_CATEGORY_DROPDOWN = new Locator("SUB_CATEGORY_DROPDOWN", By.xpath("(//div[@class = 'dropdown-menu'])[3]"));
    private static final Locator SUB_CATEGORY_TEST = new Locator("SUB_CATEGORY_TEST", By.xpath("//div[@class = 'container-options']/label[contains(text(), 'auto_testing')]"));
    private static final Locator SUB_CATEGORY_VALUE = new Locator("SUB_CATEGORY_VALUE", By.xpath("(//div[@class = 'p3 item'])[2]"));
    private static final Locator PLATFORM_ALL = new Locator("PLATFORM_ALL", By.xpath("//label[@for = 'availablePlatform-ALL']"));
    private static final Locator CURRENCY_VALUE = new Locator("CURRENCY_VALUE", By.xpath("//input[@value = 'IDR']"));
    private static final Locator PAGE_ACTIVE_PERIOD_RADIO_BUTTON = new Locator("PAGE_ACTIVE_PERIOD_RADIO_BUTTON", By.xpath("//div[@class='radio-item']/label[@for = 'promoPagePeriodType-0']"));
    private static final Locator PROMO_LIST_PERIOD_TYPE = new Locator("PROMO_LIST_PERIOD_TYPE", By.xpath("//div[@class='radio-item']/label[@for = 'promoListPeriodType-0']"));
    private static final Locator SHOW_ON_BANNER = new Locator("SHOW_ON_BANNER", By.xpath("//div[@class='radio-item']/label[@for = 'desktopBannerPeriodType-0']"));
    private static final Locator SHOW_ON_CUSTOM_DATA_SOURCE = new Locator("SHOW_ON_CUSTOM_DATA_SOURCE", By.xpath("//div[@class='radio-item']/label[@for = 'showOnCustomDataSource-0']"));
    private static final Locator PROMO_SLUG_INPUT = new Locator("PROMO_SLUG_INPUT", By.xpath("//input[@name='slug']"));
    private static final Locator PROMO_META_TITLE_EN = new Locator("PROMO_META_TITLE_EN", By.xpath("(//div[@class='text-input hasLabel']/input[@data-error-message='Enter promo meta title.'])[1]"));
    private static final Locator PROMO_META_DESC_EN = new Locator("PROMO_META_DESC_EN", By.xpath("(//textarea[@name='description'])[1]"));
    private static final Locator PROMO_SHARE_TEXT_EN = new Locator("PROMO_SHARE_TEXT_EN", By.xpath("(//textarea[@name='shareText'])[1]"));
    private static final Locator PROMO_META_TITLE_ID = new Locator("PROMO_META_TITLE_ID", By.xpath("(//div[@class='text-input hasLabel']/input[@data-error-message='Enter promo meta title.'])[2]"));
    private static final Locator PROMO_META_DESC_ID = new Locator("PROMO_META_DESC_ID", By.xpath("(//textarea[@name='description'])[2]"));
    private static final Locator PROMO_SHARE_TEXT_ID = new Locator("PROMO_SHARE_TEXT_ID", By.xpath("(//textarea[@name='shareText'])[2]"));
    private static final Locator SAVE_AND_GO_TO_CONTENT_BUTTON = new Locator("SAVE_AND_GO_TO_CONTENT_BUTTON", By.xpath("//button[@type = 'button' and text() = 'SAVE AND GO TO CONTENT']"));
    private static final Locator UPLOAD_PROMO_BANNER_ID = new Locator("UPLOAD_PROMO_BANNER_ID", By.xpath("(//input[@type = 'file' and @name = 'mainImage'])[1]"));
    private static final Locator CONTENT_TITLE_BANNER_ID = new Locator("CONTENT_TITLE_BANNER_ID", By.xpath("//input[@for ='contentTitleID']"));
    private static final Locator CONTENT_DESCRIPTION_BANNER_ID = new Locator("CONTENT_DESCRIPTION_BANNER_ID", By.xpath("(//div[@class = 'cke_contents cke_reset'])[2]"));
    private static final Locator UPLOAD_PROMO_BANNER_EN = new Locator("UPLOAD_PROMO_BANNER_EN", By.xpath("(//input[@type = 'file' and @name = 'mainImage'])[2]"));
    private static final Locator CONTENT_TITLE_BANNER_EN = new Locator("CONTENT_TITLE_BANNER_EN", By.xpath("(//div[@class = 'cke_contents cke_reset'])[3]"));
    private static final Locator CONTENT_DESCRIPTION_BANNER_EN = new Locator("CONTENT_DESCRIPTION_BANNER_EN", By.xpath("(//div[@class = 'cke_contents cke_reset'])[4]"));
    private static final Locator ADDITIONAL_INPUT_BUTTON = new Locator("ADDITIONAL_INPUT_BUTTON", By.xpath("//div[@class = 'switch switch-input']"));
    private static final Locator ADDITIONAL_INFO_SHOWN = new Locator("ADDITIONAL_INFO_SHOWN",By.xpath("//div[@class = 'switch-area']/label"));
    private static final Locator ADDITIONAL_INFO_SHOW = new Locator("ADDITIONAL_INFO_SHOW",By.xpath("//div[@class = 'switch-area']/label[text() = 'show']"));
    private static final Locator SHOW_COUNTDOWN_NO = new Locator("SHOW_COUNTDOWN_NO", By.id("showCountDown-no"));
    private static final Locator USE_PROMO_CODE_NO = new Locator("USE_PROMO_CODE_NO", By.id("usePromoCode-no"));
    private static final Locator SAVE_AND_GO_TO_SETTINGS_BUTTON = new Locator("SAVE_AND_GO_TO_SETTINGS_BUTTON", By.xpath("//button[@type = 'button' and text() = 'SAVE AND GO TO SETTINGS']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[@type = 'submit']"));
    private static final Locator SNACKBAR_SUCCESS_GENERAL_SETTINGS = new Locator("SNACKBAR_SUCCESS_GENERAL_SETTINGS", By.xpath("//div[@class='snackbar-notification hide SUCCESS']/div[@class = 'snackbar-container' and text() = 'Success Create Promo General Settings']"));
    private static final Locator SNACKBAR_SUCCESS_GENERAL_CONTENTS = new Locator("SNACKBAR_SUCCESS_GENERAL_CONTENTS", By.xpath("//div[@class = 'snackbar-container' and text() = 'Success Update Promo General Contents']"));
    private static final Locator GET_IN_INDONESIA = new Locator("GET_IN_INDONESIA", By.xpath("//button[@type='button' and contains(text(), 'GET IN')]"));
    private static final Locator ADDITIONAL_INFO_HIDE = new Locator("ADDITIONAL_INFO_HIDE",By.xpath("//div[@class = 'switch-area']/label[text() = 'hide']"));
    private static final Locator LOADING_ICON = new Locator("LOADING_ICON", By.cssSelector(".overlay-loading.show"));
    private static final Locator EDIT_LAST_CREATED_PROMO = new Locator("EDIT_LAST_CREATED_PROMO", By.xpath("(//div[@class = 'action-item']/i[@class='tix tixicon tixicon-edit2'])[last()]"));
    private static final Locator CONTENT_DESC_ID_VALUE = new Locator("CONTENT_DESC_ID_VALUE", By.xpath("//input[@for = 'contentDescriptionID']"));
    private static final Locator CONTENT_TITLE_EN_VALUE = new Locator("CONTENT_DESC_ID_VALUE", By.xpath("//input[@for = 'contentTitleEN']"));
    private static final Locator CONTENT_DESC_EN_VALUE = new Locator("CONTENT_DESC_ID_VALUE", By.xpath("//input[@for = 'contentDescriptionEN']"));
    private static final Locator EMAIL_LABEL = new Locator("EMAIL_LABEL", By.className("email_label"));


    public void goToPromoDashboard(String username, String password)throws InterruptedException{
        if (!isPromoPageLoggedIn()) {
            MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
            memberLoginPage.adminLoginViaCookie(username, password);
            memberLoginPage.goToPage(PROMO_URL);
            driverTools.waitForElementToAppear(EMAIL_LABEL);
            if(driverTools.isElementDisplayed(GET_IN_INDONESIA)){
                driverTools.click(GET_IN_INDONESIA);
            }
        }
    }

    public boolean isPromoPageLoggedIn() {
        driverTools.waitForElementToAppear(LOGGED_IN_INFO, 2);
        return (driverTools.isElementDisplayed(LOGGED_IN_INFO));
    }

    public void clickPromoSideMenu(){
        driverTools.click(PROMO_PAGE_BUTTON);
    }

    public boolean isPromoPageLoaded(){
        driverTools.waitForElementToAppear(LOGGED_IN_INFO,10);
        return (driverTools.isElementDisplayed(LOGGED_IN_INFO));
    }

    public void createNewPromo(){
        driverTools.click(CREATE_PROMO_BUTTON);
        driverTools.click(CURRENCY_DROPDOWN);
        driverTools.click(CURRENCY_IDR);
        driverTools.click(SAVE_CURRENCY);
    }

    public boolean isGeneralSettingPageLoaded(){
        driverTools.waitForElementToAppear(GENERAL_SETTING_COMPONENT, 5);
        return (driverTools.isElementDisplayed(GENERAL_SETTING_COMPONENT));
    }

    public void inputPromoName(String promoName) {
        driverTools.click(PROMO_NAME_INPUT);
        driverTools.sendKeys(PROMO_NAME_INPUT, promoName);
    }

    public void inputCategories(){
        driverTools.click(PARENT_CATEGORY_DROPDOWN);
        driverTools.click(PARENT_CATEGORY_TESTING);
        driverTools.click(SUB_CATEGORY_DROPDOWN);
        driverTools.click(SUB_CATEGORY_TEST);
    }

    public void chooseAllPlatform(){
        driverTools.click(PLATFORM_ALL);
    }

    public void chooseActivePeriod(){
        driverTools.click(PAGE_ACTIVE_PERIOD_RADIO_BUTTON);
        driverTools.click(PROMO_LIST_PERIOD_TYPE);
    }

    public void showBannerCustomData(){
        driverTools.click(SHOW_ON_BANNER);
        driverTools.click(SHOW_ON_CUSTOM_DATA_SOURCE);
    }

    public void inputSlug(){
        String randomCode = CoreFunctions.generateRandomString(7);
        String randomCodeString = "automation-slug-"+ randomCode;
        driverTools.click(PROMO_SLUG_INPUT);
        driverTools.sendKeys(PROMO_SLUG_INPUT, randomCodeString);
        this.generatedSlug = randomCodeString;
    }

    public String getGeneratedSlug(){
        logger.log("Value inputted : " + this.generatedSlug);
        return this.generatedSlug;
    }

    public void inputGeneralSettingSEO(
            String promoMetaTitleEn,
            String promoMetaDescEn,
            String promoShareTextEn,
            String promoMetaTitleId,
            String promoMetaDescId,
            String promoShareTextId){
        driverTools.click(PROMO_META_TITLE_EN);
        driverTools.sendKeys(PROMO_META_TITLE_EN, promoMetaTitleEn);
        driverTools.click(PROMO_META_DESC_EN);
        driverTools.sendKeys(PROMO_META_DESC_EN, promoMetaDescEn);
        driverTools.click(PROMO_SHARE_TEXT_EN);
        driverTools.sendKeys(PROMO_SHARE_TEXT_EN, promoShareTextEn);
        driverTools.click(PROMO_META_TITLE_ID);
        driverTools.sendKeys(PROMO_META_TITLE_ID, promoMetaTitleId);
        driverTools.click(PROMO_META_DESC_ID);
        driverTools.sendKeys(PROMO_META_DESC_ID, promoMetaDescId);
        driverTools.click(PROMO_SHARE_TEXT_ID);
        driverTools.sendKeys(PROMO_SHARE_TEXT_ID, promoShareTextId);
    }

    public void clickSaveGoToContent(){
        driverTools.click(SAVE_AND_GO_TO_CONTENT_BUTTON);
    }

    public boolean isSnackBarSuccessGeneralSettingAppear(){
        driverTools.waitForElementToPresence(SNACKBAR_SUCCESS_GENERAL_SETTINGS, 2);
        return (driverTools.isElementDisplayed(SNACKBAR_SUCCESS_GENERAL_SETTINGS));
    }

    public boolean isGeneralContentLoaded(){
        driverTools.waitForElementToPresence(GENERAL_CONTENT_COMPONENT, 5);
        return (driverTools.isElementDisplayed(GENERAL_CONTENT_COMPONENT));
    }

    public void uploadBanner(String files){
        File bannerImage = new File(files);
        driverTools.sendKeys(UPLOAD_PROMO_BANNER_ID, bannerImage.getAbsolutePath());
        driverTools.waitForElementToDisappear(LOADING_ICON, 3);
        driverTools.sendKeys(UPLOAD_PROMO_BANNER_EN, bannerImage.getAbsolutePath());
        driverTools.waitForElementToDisappear(LOADING_ICON, 3);
    }

    public void fillContents(
            String contentTitleId,
            String contentDescriptionId,
            String contentTitleEn,
            String contentDescriptionEn
    )throws InterruptedException{
        Thread.sleep(1000);
        switchToIframeAndSendKeys("Rich Text Editor, editor1", contentTitleId);
        switchToIframeAndSendKeys("Rich Text Editor, editor2", contentDescriptionId);
        switchToIframeAndSendKeys("Rich Text Editor, editor3", contentTitleEn);
        switchToIframeAndSendKeys("Rich Text Editor, editor4", contentDescriptionEn);
    }

    private void switchToIframeAndSendKeys(String iframeTitle, String content) {
        driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@title='" + iframeTitle + "']")))
                .findElement(By.xpath("//body[@class='cke_editable cke_editable_themed cke_contents_ltr cke_show_borders']"))
                .sendKeys(content);
    }

    public void clickAdditionalInfoOff(){
        driver.switchTo().defaultContent();
        driverTools.waitForElementToPresence(ADDITIONAL_INFO_SHOW,2);
        if(driverTools.isElementDisplayed(ADDITIONAL_INFO_SHOW)){
            driverTools.click(ADDITIONAL_INPUT_BUTTON);
        } else {
            System.out.println("Additional info already hide.");
            logger.log("current condition : " + driverTools.getValue(ADDITIONAL_INFO_SHOWN));
        }
    }

    public void clickCountDown(){
        driverTools.waitForElementToAppear(SHOW_COUNTDOWN_NO, 1);
        driverTools.click(SHOW_COUNTDOWN_NO);
    }
    public void clickUsePromoCode(){
        driverTools.click(USE_PROMO_CODE_NO);
    }

    public void clickSubmit(){
        driverTools.click(SUBMIT_BUTTON);
    }

    public boolean isSnackBarSuccessGeneralContentsAppear(){
        driverTools.waitForElementToAppear(SNACKBAR_SUCCESS_GENERAL_CONTENTS, 1);
        return (driverTools.isElementDisplayed(SNACKBAR_SUCCESS_GENERAL_CONTENTS));
    }

    public void clickSaveAndGoToSettings(){
        driverTools.click(SAVE_AND_GO_TO_SETTINGS_BUTTON);
    }

    public Map<String, String> verifyGeneralSettingsData() {
        Map<String, String> resultGeneralSettings = new HashMap<>();
        driverTools.waitForElementToPresence(PROMO_NAME_INPUT,1);
        String promoName = driverTools.getValue(PROMO_NAME_INPUT);
        String categoryValue = driver.findElement(By.xpath("(//div[@class = 'p3 item'])[1]")).getText();
        String subCategoryValue = driver.findElement(By.xpath("(//div[@class = 'p3 item'])[2]")).getText();;
        String currencyValue = driverTools.getValue(CURRENCY_VALUE);
        String promoSlug = driverTools.getValue(PROMO_SLUG_INPUT);
        String promoMetaTitleEn = driverTools.getValue(PROMO_META_TITLE_EN);
        String promoMetaDescEn = driverTools.getValue(PROMO_META_DESC_EN);
        String promoShareTextEn = driverTools.getValue(PROMO_SHARE_TEXT_EN);
        String promoMetaTitleId = driverTools.getValue(PROMO_META_TITLE_ID);
        String promoMetaDescId = driverTools.getValue(PROMO_META_DESC_ID);
        String promoShareTextId = driverTools.getValue(PROMO_SHARE_TEXT_ID);
        resultGeneralSettings.put("promoName", promoName);
        resultGeneralSettings.put("categoryValue", categoryValue);
        resultGeneralSettings.put("subCategoryValue", subCategoryValue);
        resultGeneralSettings.put("currencyValue", currencyValue);
        resultGeneralSettings.put("promoSlug", promoSlug);
        resultGeneralSettings.put("promoMetaTitleEn", promoMetaTitleEn);
        resultGeneralSettings.put("promoMetaDescEn", promoMetaDescEn);
        resultGeneralSettings.put("promoShareTextEn", promoShareTextEn);
        resultGeneralSettings.put("promoMetaTitleId", promoMetaTitleId);
        resultGeneralSettings.put("promoMetaDescId", promoMetaDescId);
        resultGeneralSettings.put("promoShareTextId", promoShareTextId);
        return resultGeneralSettings;
    }

    public Map<String, String> verifyGeneralContentData() {
        Map<String, String> resultGeneralContent = new HashMap<>();
        driverTools.waitForElementToPresence(CONTENT_TITLE_BANNER_ID,1);
        String contentTitleId = driverTools.getValue(CONTENT_TITLE_BANNER_ID).replace("<p>", "").replace("</p>", "");
        String contentDescriptionId = driverTools.getValue(CONTENT_DESC_ID_VALUE).replace("<p>", "").replace("</p>", "");
        String contentTitleEn = driverTools.getValue(CONTENT_TITLE_EN_VALUE).replace("<p>", "").replace("</p>", "");
        String contentDescriptionEn = driverTools.getValue(CONTENT_DESC_EN_VALUE).replace("<p>", "").replace("</p>", "");
        resultGeneralContent.put("contentTitleId", contentTitleId);
        resultGeneralContent.put("contentDescriptionId", contentDescriptionId);
        resultGeneralContent.put("contentTitleEn", contentTitleEn);
        resultGeneralContent.put("contentDescriptionEn", contentDescriptionEn);
        return resultGeneralContent;
    }
}