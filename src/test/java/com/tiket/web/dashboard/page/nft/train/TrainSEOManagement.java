package com.tiket.web.dashboard.page.nft.train;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.Matchers.is;

public class TrainSEOManagement<T extends WebDriver> extends BasePage<T> {
    private static final Locator TRAIN_SEO_MANAGEMENT_MENU = new Locator("TRAIN_SEO_MANAGEMENT_MENU", By.xpath("//div[@class='menu-list' and contains(text(),'SEO Page Management')]"));
    private static final Locator ADD_SECTION_BUTTON = new Locator("ADD_SECTION_BUTTON", By.xpath("//button[@class='add-section-button rounded-button']"));
    private static final Locator ADD_SUBTITLE_BUTTON = new Locator("ADD_SUBTITLE_BUTTON", By.xpath("//button[@data-test='add-subtitle-button']"));
    private static final Locator SECTION_NAME_FIELD = new Locator("SECTION_NAME_FIELD", By.xpath("//input[@data-test='section-title']"));
    private static final Locator TITLE_FIELD = new Locator("TITLE_FIELD", By.xpath("//input[@data-test='input-section-title']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//div[@class='button-save']"));
    private static final Locator SUCCESS_CREATE_BANNER = new Locator("SUCCESS_CREATE_BANNER", By.xpath("//p[contains(text(), 'Page succesfully change.')]"));
    private static final Locator SUCCESS_ADD_BANNER = new Locator("SUCCESS_ADD_BANNER", By.xpath("//p[contains(text(), 'Page succesfully added')]"));
    private static final Locator ADD_TRAIN_NAME_BUTTON = new Locator("ADD_TRAIN_NAME_BUTTON", By.xpath("//div[@class='btn-model-1' and contains(text(), '+ ADD NEW PAGE')]"));
    private static final Locator INPUT_NAME_FIELD = new Locator("INPUT_NAME_FIELD", By.xpath("//input[@name='inputName']"));
    private static final Locator INPUT_FROM = new Locator("INPUT_FROM", By.xpath("//input[@name='inputFrom']"));
    private static final Locator INPUT_TO = new Locator("INPUT_TO", By.xpath("//input[@name='inputTo']"));
    private static final Locator INPUT_SLUG = new Locator("INPUT_SLUG", By.xpath("//input[@name='inputSlug']"));
    private static final Locator PAGE_TITLE_FIELD = new Locator("PAGE_TITLE_FIELD", By.xpath("//div[@class='group-form']//div[@class='text-label title' and contains(text(), 'Page Title :')]/following-sibling::div//input[@class='form-control input-text-field ']"));
    private static final Locator META_TITLE_FIELD = new Locator("META_TITLE_FIELD", By.xpath("//div[@class='group-form']//div[@class='text-label title' and contains(text(), 'Meta Title :')]/following-sibling::div//input[@class='form-control input-text-field ']"));
    private static final Locator META_DESCRIPTION_FIELD = new Locator("META_DESCRIPTION_FIELD", By.xpath("//div[@class='group-form']//div[@class='text-label title' and contains(text(), 'Meta Description :')]/following-sibling::div//input[@class='form-control input-text-field ']"));
    private static final Locator CONTENT_TAB_TRIGGER = new Locator("CONTENT_TAB_TRIGGER", By.xpath("//span[@class='tab-list' and contains(text(), 'Content')]"));
    private static final Locator HEADER = new Locator("HEADER", By.xpath("//div[@class='form-header']"));
    private static final Locator SEARCH_INPUT = new Locator("SEARCH_INPUT", new By.ByXPath("//input[@class='form-control' and @type='text' and @placeholder='Search something here…']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[contains(text(), 'SEARCH')]"));

    public TrainSEOManagement(Session<T> session) {
        super(session);
    }

    public void goToTrainSEOManagementMenu() {
        driverTools.waitForElementToAppear(TRAIN_SEO_MANAGEMENT_MENU);
        driverTools.click(TRAIN_SEO_MANAGEMENT_MENU);
    }

    public void goToSubMenu(String subMenuName) {
        Locator subMenuLocator = new Locator("SUB_MENU", By.xpath(String.format("//div[@class='submenu-list' and contains(text(),'%s')]", subMenuName)));
        WebElement subMenu = driverTools.waitForElementToAppear(subMenuLocator).get();
        driverTools.click(subMenu);
    }

    public void createNewSEOHome(String sectionName, String title, String subtitle, String url) {
        driverTools.waitForElementToAppear(ADD_SECTION_BUTTON);
        driverTools.scrollIntoWebView(ADD_SECTION_BUTTON);
        driverTools.click(ADD_SECTION_BUTTON);
        fillSEOForm(sectionName, title, subtitle, url);
    }

    private void fillSEOForm(String sectionName, String title, String subtitle, String url) {
        driverTools.scrollIntoWebView(SECTION_NAME_FIELD);
        driverTools.sendKeys(SECTION_NAME_FIELD, sectionName);
        driverTools.sendKeys(TITLE_FIELD, title);
        fillMultipleSubtitlesAndUrls(subtitle, url, 3);
        driverTools.click(SAVE_BUTTON);
    }

    private void fillMultipleSubtitlesAndUrls(String subtitle, String url, int count) {
        for (int i = 1; i <= count; i++) {
            driverTools.click(ADD_SUBTITLE_BUTTON);
            fillField(new Locator("SUBTITLE_FIELD", By.xpath(String.format("(//input[@data-test='subtitle-label'])[%d]", i))), subtitle);
            fillField(new Locator("URL_FIELD", By.xpath(String.format("(//input[@data-test='subtitle-url'])[%d]", i))), url);
        }
    }

    private void fillField(Locator locator, String value) {
        WebElement field = driverTools.waitForElementToAppear(locator).get();
        driverTools.sendKeys(field, value);
    }

    public boolean verifyCreatedHomeSEO() {
        return driverTools.waitForElementToAppear(SUCCESS_CREATE_BANNER).isPresent();
    }

    public void createNewTrainName(String name, String from, String to, String slug, String pageTitle, String metaTitle, String metaDesc, String sectionName, String title, String subtitle, String url) {
        fillForm(name, from, to, slug, pageTitle, metaTitle, metaDesc, sectionName, title, subtitle, url);
    }

    public void createNewStationName(String name, String from, String to, String slug, String pageTitle, String metaTitle, String metaDesc, String sectionName, String title, String subtitle, String url) {
        fillForm(name, from, to, slug, pageTitle, metaTitle, metaDesc, sectionName, title, subtitle, url);
    }

    public void createNewDestination(String name, String from, String to, String slug, String pageTitle, String metaTitle, String metaDesc, String sectionName, String title, String subtitle, String url) {
        fillForm(name, from, to, slug, pageTitle, metaTitle, metaDesc, sectionName, title, subtitle, url);
    }

    public void createNewSeasonal(String name, String slug, String pageTitle, String metaTitle, String metaDesc, String sectionName, String title, String subtitle, String url) {
        fillFormSeasonal(name, slug, pageTitle, metaTitle, metaDesc, sectionName, title, subtitle, url);
    }

    private void fillForm(String name, String from, String to, String slug, String pageTitle, String metaTitle, String metaDesc, String sectionName, String title, String subtitle, String url) {
        driverTools.waitForElementToAppear(ADD_TRAIN_NAME_BUTTON);
        driverTools.click(ADD_TRAIN_NAME_BUTTON);

        driverTools.waitForElementToAppear(INPUT_NAME_FIELD);
        driverTools.sendKeys(INPUT_NAME_FIELD, name);
        driverTools.sendKeys(INPUT_FROM, from);
        driverTools.sendKeys(INPUT_TO, to);
        driverTools.sendKeys(INPUT_SLUG, slug);
        driverTools.sendKeys(PAGE_TITLE_FIELD, pageTitle);
        driverTools.sendKeys(META_TITLE_FIELD, metaTitle);
        driverTools.sendKeys(META_DESCRIPTION_FIELD, metaDesc);

        driverTools.scrollIntoWebView(HEADER);
        driverTools.click(CONTENT_TAB_TRIGGER);
        driverTools.waitForElementToAppear(ADD_SECTION_BUTTON);
        driverTools.click(ADD_SECTION_BUTTON);
        driverTools.sendKeys(SECTION_NAME_FIELD, sectionName);
        driverTools.sendKeys(TITLE_FIELD, title);
        fillMultipleSubtitlesAndUrls(subtitle, url, 3);

        driverTools.click(SAVE_BUTTON);
    }

    private void fillFormSeasonal(String name, String slug, String pageTitle, String metaTitle, String metaDesc, String sectionName, String title, String subtitle, String url) {
        driverTools.waitForElementToAppear(ADD_TRAIN_NAME_BUTTON);
        driverTools.click(ADD_TRAIN_NAME_BUTTON);

        driverTools.waitForElementToAppear(INPUT_NAME_FIELD);
        driverTools.sendKeys(INPUT_NAME_FIELD, name);
        driverTools.sendKeys(INPUT_SLUG, slug);
        driverTools.sendKeys(PAGE_TITLE_FIELD, pageTitle);
        driverTools.sendKeys(META_TITLE_FIELD, metaTitle);
        driverTools.sendKeys(META_DESCRIPTION_FIELD, metaDesc);

        driverTools.scrollIntoWebView(HEADER);
        driverTools.click(CONTENT_TAB_TRIGGER);
        driverTools.waitForElementToAppear(ADD_SECTION_BUTTON);
        driverTools.click(ADD_SECTION_BUTTON);
        driverTools.sendKeys(SECTION_NAME_FIELD, sectionName);
        driverTools.sendKeys(TITLE_FIELD, title);
        fillMultipleSubtitlesAndUrls(subtitle, url, 3);

        driverTools.click(SAVE_BUTTON);
    }


    public boolean verifySuccessCreation(String name){
        Locator tableCell = new Locator("TABLE_CELL", new By.ByXPath("//td[contains(text(), '" + name + "')]"));

        search(name);
        driverTools.waitForElementToAppear(tableCell);
        Assertion.assertThat("Table Cell is displayed", driverTools.isElementDisplayed(tableCell), is(true));
        Assertion.assertThat("Success Add Banner is displayed", driverTools.isElementDisplayed(SUCCESS_ADD_BANNER), is(true));
        return true;
    }

    public void search(String label){
        driverTools.click(SEARCH_INPUT);
        driverTools.sendKeys(SEARCH_INPUT, label);
        driverTools.click(SEARCH_BUTTON);
    }
}