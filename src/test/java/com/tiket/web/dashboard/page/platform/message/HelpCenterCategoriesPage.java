package com.tiket.web.dashboard.page.platform.message;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;

import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;

public class HelpCenterCategoriesPage<T extends WebDriver> extends BasePage<T> {
    public HelpCenterCategoriesPage(Session<T> session){
        super(session);
    }
    ILogger logger = BaseTest.mainLogger.get();

    private static final String HELP_CENTER_URL = "/admin/helpcenter";
    private static final Locator EMAIL_LABEL = new Locator("EMAIL_LABEL", By.xpath("//div[contains(@class,'email_label')]"));
    private static final Locator ADD_NEW_CATEGORIES_BUTTON = new Locator("ADD_NEW_CATEGORIES_BUTTON", By.xpath("//div[@class='add_btn'][.//span[text()='Add Category']]"));
    private static final Locator MODAL_NEW_CATEGORY = new Locator("MODAL_NEW_CATEGORY", By.xpath("//div[contains(@class,'title_label')]//span[text()='Category']"));
    private static final Locator URL_SLUG_INPUT = new Locator("URL_SLUG_INPUT", By.xpath("//input[@id='url-slug']"));
    private static final Locator CATEGORY_NAME_INPUT = new Locator("CATEGORY_NAME_INPUT", By.xpath("//input[@id='category-name']"));
    private static final Locator GENERAL_TAB_BUTTON = new Locator("GENERAL_TAB_BUTTON", By.xpath("//div[contains(@class,'tab_section')]/span[text()='General']"));
    private static final Locator ICON_TAB_BUTTON = new Locator("ICON_TAB_BUTTON", By.xpath("//div[contains(@class,'tab_section')]/span[text()='Icon']"));
    private static final Locator ICON_VERTICAL = new Locator("ICON_VERTICAL", By.xpath("//div[contains(@class,'category_icon_item')]/span[text()='general']"));
    private static final Locator SEO_TAB_BUTTON = new Locator("SEO_TAB_BUTTON", By.xpath("//div[contains(@class,'tab_section')]/span[text()='SEO']"));
    private static final Locator META_TITLE = new Locator("META_TITLE", By.xpath("//input[@name='metaTitle']"));
    private static final Locator META_DESCRIPTION = new Locator("META_DESCRIPTION", By.xpath("//input[@name='metaDescription']"));
    private static final Locator TOGGLE_PUBLISH = new Locator("TOGGLE_PUBLISH", By.xpath("//div[contains(@class,'switch_form')]//div[contains(@class,'toggle') and contains(@class,'toggle_small')]"));
    private static final Locator TOGGLE_PUBLISHED_ON = new Locator("TOGGLE_PUBLISHED_ON", By.xpath("//input[@data-testid='toggle-publish']"));
    private static final Locator CONTENT_EN_BUTTON = new Locator("CONTENT_EN_BUTTON", By.xpath("//button[@data-testid='btn-lang-en']"));
    private static final Locator CONTENT_ID_BUTTON = new Locator("CONTENT_ID_BUTTON", By.xpath("//button[@data-testid='btn-lang-id']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[.//span[text()='Save']]"));
    private static final Locator SUCCESS_CREATE_SNACKBAR = new Locator("SUCCESS_CREATE_SNACKBAR", By.xpath("//div[contains(@class, 'type_success')]"));
    private static final Locator MODAL_OVERLAY = new Locator("MODAL_OVERLAY", By.xpath("//div[contains(@class,'modal_overlay')]"));
    private static final Locator HELP_CENTER_LOGO = new Locator("HELP_CENTER_LOGO", By.xpath("//*[contains(@class,'logo_area')]//span[text()='Help Center']"));
    private static final Locator GET_IN = new Locator("GET_IN", By.xpath("//*[contains(@class,'RegionPicker')]//*[@data-testid='button-submit']"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("(//div[contains(@class,'edit_area')])[last()]"));
    private static final Locator CHOSEN_ICON = new Locator("CHOSEN_ICON", By.xpath("//div[contains(@class,'category_icon_item')]/img[contains(@class,'icon_img') and contains(@class,'active')]"));
    private static final Locator NEW_INPUT_CATEGORY = new Locator("NEW_INPUT_CATEGORY", By.xpath("//div[contains(@class,'label_content') and contains(text(), 'Automation Category Id')]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", By.xpath("//button[.//span[normalize-space()='Delete']]"));
    private static final Locator SNACKBAR_SUCCESS_DELETE = new Locator("SNACKBAR_SUCCESS_DELETE", By.xpath("//div[contains(@class, 'type_success') or contains(@class, 'text_content')]"));
    private static final Locator GET_IN_BUTTON = new Locator("GET_IN_BUTTON", By.xpath("//button[@data-testid='button-submit']"));

    public String generateUniqueSlug(String baseSlug) {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        return baseSlug + "-" + suffix;
    }

    public boolean isLogin(){
        driverTools.waitForElementToAppear(EMAIL_LABEL, 2);
        return driverTools.isElementDisplayed(EMAIL_LABEL);
    }

    public void goToHelpCenterPage(String username, String password){
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(HELP_CENTER_URL);
        driver.manage().addCookie(new Cookie.Builder("dashboardAreaHC","IDN")
                .sameSite("None")
                .domain(".tiket.com")
                .path("/").build());
        driver.manage().addCookie(new Cookie.Builder("dashboardLang","id")
                .sameSite("None")
                .domain(".tiket.com")
                .path("/").build());
        driver.manage().addCookie(new Cookie.Builder("userlang","id")
                .sameSite("None")
                .domain(".tiket.com")
                .path("/").build());
        driver.navigate().refresh();
        driverTools.waitForElementToAppear(GET_IN_BUTTON,5);
        if(driverTools.isElementDisplayed(GET_IN_BUTTON)){
            driverTools.clickWithJavascriptExecutor(GET_IN_BUTTON);
        }
    }

    public boolean isHelpCenterPageLoaded(String email){
        if(driverTools.isElementDisplayed(GET_IN)){
            driverTools.click(GET_IN);
        }
        driverTools.waitForElementToAppear(HELP_CENTER_LOGO, 2);
        boolean checkHelpCenterLogo = driverTools.isElementDisplayed(HELP_CENTER_LOGO);
        boolean buttonAddCategories = driverTools.isElementDisplayed(ADD_NEW_CATEGORIES_BUTTON);
        boolean checkLogin = isLogin();
        return checkHelpCenterLogo && buttonAddCategories && checkLogin;
    }

    public void clickAddNewCategoriesButton(){
        driverTools.click(ADD_NEW_CATEGORIES_BUTTON);
        driverTools.waitForElementToAppear(MODAL_NEW_CATEGORY, 1);
    }

    public void inputContentCategory(
            String urlSlugId,
            String urlSlugEn,
            String categoryNameId,
            String categoryNameEn,
            String metaTitleId,
            String metaTitleEn,
            String metaDescriptionId,
            String metaDescriptionEn
    ) {
        driverTools.waitForElementToPresence(MODAL_NEW_CATEGORY, 2);
        driverTools.waitForElementToAppear(URL_SLUG_INPUT, 5);
        driverTools.click(URL_SLUG_INPUT);
        driverTools.clearInput(URL_SLUG_INPUT);
        driverTools.sendKeys(URL_SLUG_INPUT, urlSlugId);
        driverTools.clearInput(CATEGORY_NAME_INPUT);
        driverTools.sendKeys(CATEGORY_NAME_INPUT, categoryNameId);

        driverTools.click(ICON_TAB_BUTTON);
        driverTools.waitForElementToAppear(ICON_VERTICAL, 5);
        driverTools.click(ICON_VERTICAL);
        driverTools.waitForElementToAppear(CHOSEN_ICON, 5);

        driverTools.click(SEO_TAB_BUTTON);
        driverTools.waitForElementToAppear(META_TITLE, 5);
        driverTools.click(META_TITLE);
        driverTools.clearInput(META_TITLE);
        driverTools.sendKeys(META_TITLE, metaTitleId);
        driverTools.clearInput(META_DESCRIPTION);
        driverTools.sendKeys(META_DESCRIPTION, metaDescriptionId);

        driverTools.click(CONTENT_EN_BUTTON);
        driverTools.waitForElementToAppear(META_TITLE, 5);
        driverTools.waitForElementToSettle(META_TITLE, 3);
        driverTools.click(META_TITLE);
        driverTools.clearInput(META_TITLE);
        driverTools.sendKeys(META_TITLE, metaTitleEn);
        driverTools.clearInput(META_DESCRIPTION);
        driverTools.sendKeys(META_DESCRIPTION, metaDescriptionEn);

        driverTools.click(GENERAL_TAB_BUTTON);
        driverTools.waitForElementToAppear(URL_SLUG_INPUT, 5);
        driverTools.waitForElementToSettle(URL_SLUG_INPUT, 3);
        driverTools.click(URL_SLUG_INPUT);
        driverTools.clearInput(URL_SLUG_INPUT);
        driverTools.sendKeys(URL_SLUG_INPUT, urlSlugEn);
        driverTools.clearInput(CATEGORY_NAME_INPUT);
        driverTools.sendKeys(CATEGORY_NAME_INPUT, categoryNameEn);
        driverTools.click(TOGGLE_PUBLISH);
        driverTools.waitForElementToSettle(TOGGLE_PUBLISH, 3);
    }

    public void clickSaveButton(){
        driverTools.waitForElementToBeClickable(SAVE_BUTTON, 5);
        driverTools.waitForElementToSettle(SAVE_BUTTON, 3);
        driverTools.click(SAVE_BUTTON);
        driverTools.waitForElementToAppear(SUCCESS_CREATE_SNACKBAR, 10);
        driverTools.waitForElementToDisappear(MODAL_OVERLAY, 10);
        driverTools.waitForElementToDisappear(MODAL_NEW_CATEGORY, 10);
    }

    public void scrollAndEdit(){
        driverTools.waitForElementToDisappear(MODAL_OVERLAY, 10);
        driverTools.scrollToElement(EDIT_BUTTON);
        driverTools.waitForElementToAppear(NEW_INPUT_CATEGORY, 5);
        driverTools.waitForElementToBeClickable(EDIT_BUTTON, 10);
        try {
            driverTools.click(EDIT_BUTTON);
        } catch (ElementClickInterceptedException e) {
            logger.log("Edit click intercepted by overlay, retrying with JS executor");
            driverTools.clickWithJavascriptExecutor(EDIT_BUTTON);
        }
    }

    public Map<String, String> verifyContentDataId(){
        Map<String, String> resultContentDataId= new HashMap<>();
        driverTools.waitForElementToAppear(MODAL_NEW_CATEGORY, 5);
        driverTools.click(CONTENT_ID_BUTTON);
        driverTools.click(GENERAL_TAB_BUTTON);
        driverTools.waitForElementToAppear(URL_SLUG_INPUT, 5);
        driverTools.waitForElementToSettle(URL_SLUG_INPUT, 3);
        String urlSlugId = driverTools.getValue(URL_SLUG_INPUT);
        String categoryNameId = driverTools.getValue(CATEGORY_NAME_INPUT);
        resultContentDataId.put("urlSlugId", urlSlugId);
        resultContentDataId.put("categoryNameId", categoryNameId);
        driverTools.click(SEO_TAB_BUTTON);
        driverTools.waitForElementToAppear(META_TITLE, 5);
        driverTools.waitForElementToSettle(META_TITLE, 3);
        String metaTitleId = driverTools.getValue(META_TITLE);
        String metaDescriptionId = driverTools.getValue(META_DESCRIPTION);
        resultContentDataId.put("metaTitleId", metaTitleId);
        resultContentDataId.put("metaDescriptionId", metaDescriptionId);
        return resultContentDataId;
    }
    public Map<String, String> verifyContentDataEn(){
        Map<String, String> resultContentDataEn= new HashMap<>();
        driverTools.click(CONTENT_EN_BUTTON);
        driverTools.waitForElementToAppear(META_TITLE, 5);
        driverTools.waitForElementToSettle(META_TITLE, 3);
        String metaTitleEn = driverTools.getValue(META_TITLE);
        String metaDescriptionEn = driverTools.getValue(META_DESCRIPTION);
        resultContentDataEn.put("metaTitleEn", metaTitleEn);
        resultContentDataEn.put("metaDescriptionEn", metaDescriptionEn);
        driverTools.click(GENERAL_TAB_BUTTON);
        driverTools.waitForElementToAppear(URL_SLUG_INPUT, 5);
        driverTools.waitForElementToSettle(URL_SLUG_INPUT, 3);
        String urlSlugEn = driverTools.getValue(URL_SLUG_INPUT);
        String categoryNameEn = driverTools.getValue(CATEGORY_NAME_INPUT);
        resultContentDataEn.put("urlSlugEn", urlSlugEn);
        resultContentDataEn.put("categoryNameEn", categoryNameEn);
        return resultContentDataEn;
    }

    public boolean verifyToggleButtonActive(){
        driverTools.waitForElementToAppear(TOGGLE_PUBLISH);
        boolean togglebuttonOn = driverTools.isElementEnabled(TOGGLE_PUBLISHED_ON);
        return togglebuttonOn;
    }

    public void clickDeleteButton() {
        driverTools.waitForElementToPresence(DELETE_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(DELETE_BUTTON);
        driverTools.waitForElementToAppear(SNACKBAR_SUCCESS_DELETE, 10);
    }

    public boolean verifySnackbarSuccessDelete(){
        return (driverTools.isElementDisplayed(SNACKBAR_SUCCESS_DELETE));
    }

    public void clearSession(){
        driver.manage().deleteAllCookies();
        driver.get("about:blank");
    }
}