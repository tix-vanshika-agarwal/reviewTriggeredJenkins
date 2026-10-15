package com.tiket.web.dashboard.page.platform.liberty.helpCenter;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;

public class HelpCenterPageWeb  <T extends WebDriver> extends WebPage<T> {
    public HelpCenterPageWeb (Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HelpCenterPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HelpCenterPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new HelpCenterPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Platform not supported");
        }
    }

    private static final Locator GET_IN_BUTTON = new Locator("GET_IN_BUTTON", By.xpath("//button[@data-testid='button-submit']"));
    private static final Locator HELP_CENTER_PAGE = new Locator("HELP_CENTER_PAGE", By.xpath("//h2[contains(@class,'title') and normalize-space()='Welcome to Dashboard']"));
    private static final Locator AKUN_SAYA_TOPIC = new Locator("AKUN_SAYA_TOPIC", By.xpath("//div[contains(@class,'CategoryMenus_drag_handler') and .//div[text()='Akun Saya']]"));
    private static final Locator ADD_ARTICLE_BUTTON = new Locator("ADD_ARTICLE_BUTTON", By.xpath("(//span[text()='Add Article'])[1]"));
    private static final Locator TITLE_INPUT = new Locator("TITLE_INPUT", By.xpath("//input[@data-testid='input-title-id']"));
    private static final Locator TITLE_INPUT_EN = new Locator("TITLE_INPUT_EN", By.xpath("//input[@data-testid='input-title-en']"));
    private static final Locator RICH_TEXT_EDITOR = new Locator("RICH_TEXT_EDITOR", By.xpath("//body[@contenteditable='true']"));
    private static final Locator CASE_CATEGORY_INPUT = new Locator("CASE_CATEGORY_INPUT", By.xpath("//input[@data-testid='input-case-category-id']"));
    private static final Locator CASE_CATEGORY_INPUT_EN = new Locator("CASE_CATEGORY_INPUT_EN", By.xpath("//input[@data-testid='input-case-category-en']"));
    private static final Locator SLUG_INPUT = new Locator("SLUG_INPUT", By.xpath("//input[@data-testid='input-slug-id']"));
    private static final Locator SLUG_INPUT_EN = new Locator("SLUG_INPUT_EN", By.xpath("//input[@data-testid='input-slug-en']"));
    private static final Locator META_TITLE_INPUT = new Locator("META_TITLE_INPUT", By.xpath("//input[@data-testid='input-metaTitle-id']"));
    private static final Locator META_TITLE_INPUT_EN = new Locator("META_TITLE_INPUT_EN", By.xpath("//input[@data-testid='input-metaTitle-en']"));
    private static final Locator META_DESCRIPTION_INPUT = new Locator("META_DESCRIPTION_INPUT", By.xpath("//input[@data-testid='input-metaDescription-id']"));
    private static final Locator META_DESCRIPTION_INPUT_EN = new Locator("META_DESCRIPTION_INPUT_EN", By.xpath("//input[@data-testid='input-metaDescription-en']"));
    private static final Locator CKE_EDITOR_IFRAME = new Locator("CKE_EDITOR_IFRAME", By.xpath("//iframe[@class='cke_wysiwyg_frame cke_reset']"));
    private static final Locator CKE_EDITOR_IFRAME_EN = new Locator("CKE_EDITOR_IFRAME_EN", By.xpath("(//iframe[@class='cke_wysiwyg_frame cke_reset'])[3]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[normalize-space(text())='Save']"));
    private static final Locator CONFIRM_SAVE_BUTTON = new Locator("CONFIRM_SAVE_BUTTON", By.xpath("(//button[normalize-space(text())='Save'])[2]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", By.xpath("//button[@data-testid='btn-delete']"));
    private static final Locator YES_DELETE_BUTTON = new Locator("YES_DELETE_BUTTON", By.xpath("//button[normalize-space(text())='Yes, Delete']"));

    public void goToHelpCenter(){
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/helpcenter");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/helpcenter");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://tiket.com/admin/helpcenter");
        }
    }

    public void clickGetIn(){
        if (driverTools.waitForElementToAppear(GET_IN_BUTTON, 10).isPresent()) {
            driverTools.clickWithJavascriptExecutor(GET_IN_BUTTON);
        } else {
            logger.log("Get In button is not displayed, skipping click.");
        }
    }

    public boolean isHelpCenterPageOpened() {
        driverTools.waitForElementToAppear(HELP_CENTER_PAGE, 10);
        boolean isPageOpened = driverTools.isElementPresent(HELP_CENTER_PAGE);
        if (!isPageOpened) {
            logger.log("Help Center Page is not opened.");
        }
        return isPageOpened;
    }

    public void clickSideBarOptions(String option) {
        Locator SIDE_BAR_OPTION = new Locator("SIDE_BAR_OPTION", By.xpath("//div[@data-testid='menu-item-text' and @aria-label='" + option + "']"));
        driverTools.waitForElementToAppear(SIDE_BAR_OPTION, 10);
        driverTools.clickWithJavascriptExecutor(SIDE_BAR_OPTION);
    }

    public void clickAkunSayaTopic(){
        driverTools.waitForElementToAppear(AKUN_SAYA_TOPIC,10);
        driverTools.click(AKUN_SAYA_TOPIC);
    }

    public void clickAddArticleButton(){
        driverTools.waitForElementToAppear(ADD_ARTICLE_BUTTON,10);
        driverTools.clickWithJavascriptExecutor(ADD_ARTICLE_BUTTON);
    }

    public void enterArticleDetailsID(String title, String content, String caseCategory, String slug, String metaTitle, String metaDescription){
        driverTools.waitForElementToAppear(TITLE_INPUT);
        driverTools.sendKeys(TITLE_INPUT,title);
        driverTools.dummyWait(5);
        driverTools.scrollToElement(CKE_EDITOR_IFRAME);
        WebElement iframeElement = driverTools.element(CKE_EDITOR_IFRAME).orElseThrow();
        driverTools.switchToIframe(iframeElement);
        driverTools.sendKeys(RICH_TEXT_EDITOR,content);
        driverTools.switchBackFromIframe();
        driverTools.sendKeys(CASE_CATEGORY_INPUT,caseCategory);
        driverTools.sendKeys(SLUG_INPUT,slug);
        driverTools.sendKeys(META_TITLE_INPUT,metaTitle);
        driverTools.sendKeys(META_DESCRIPTION_INPUT,metaDescription);
    }

    public void enterArticleDetailsEN(String title, String content, String caseCategory, String slug, String metaTitle, String metaDescription){
        driverTools.waitForElementToAppear(TITLE_INPUT_EN);
        driverTools.sendKeys(TITLE_INPUT_EN,title);
        driverTools.waitForElementToAppear(CKE_EDITOR_IFRAME_EN);
        driverTools.scrollToElement(CKE_EDITOR_IFRAME_EN);
        WebElement iframeElement = driverTools.element(CKE_EDITOR_IFRAME_EN).orElseThrow();
        driverTools.switchToIframe(iframeElement);
        driverTools.sendKeys(RICH_TEXT_EDITOR,content);
        driverTools.switchBackFromIframe();
        driverTools.scrollToElement(SLUG_INPUT_EN);
        driverTools.sendKeys(CASE_CATEGORY_INPUT_EN,caseCategory);
        driverTools.sendKeys(SLUG_INPUT_EN,slug);
        driverTools.scrollToElement(META_DESCRIPTION_INPUT_EN);
        driverTools.sendKeys(META_TITLE_INPUT_EN,metaTitle);
        driverTools.sendKeys(META_DESCRIPTION_INPUT_EN,metaDescription);
    }

    public void clickSaveButton(){
        driverTools.waitForElementToAppear(SAVE_BUTTON,20);
        driverTools.click(SAVE_BUTTON);
        driverTools.waitForElementToAppear(CONFIRM_SAVE_BUTTON, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public boolean isArticleCreated(){
        driverTools.waitForElementToAppear(DELETE_BUTTON);
        return driverTools.isElementPresent(DELETE_BUTTON);
    }

    public void clickDeleteButton(){
        driverTools.waitForElementToAppear(DELETE_BUTTON);
        driverTools.click(DELETE_BUTTON);
        driverTools.waitForElementToAppear(YES_DELETE_BUTTON, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
    }
}