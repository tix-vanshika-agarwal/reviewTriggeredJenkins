package com.tiket.web.dashboard.page.platform.discovery;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.Optional;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;

public class CreateGlobalSearchModule<T extends WebDriver> extends BasePage<T> {

    public CreateGlobalSearchModule(Session<T> session) {
        super(session);
    }

    private static final String GLOBAL_SEARCH_URL = "admin/home/v2/global-search-page";
    private static final Locator GET_IN = new Locator("GET_IN", By.xpath("//button[contains(@class,'Button_button__') and (normalize-space()='GET IN' or .//span[normalize-space()='GET IN'])]"));
    private static final Locator DESKTOP_MODULE = new Locator("DESKTOP_MODULE", By.xpath("(//a[contains(@href,'desktop-modules')])[1]"));
    private static final Locator ADD_CUSTOM_MODULE = new Locator("ADD_CUSTOM_MODULE", By.xpath("//a[normalize-space()='Add Custom Module']"));
    private static final Locator MODULE_NAME = new Locator("MODULE_NAME", By.xpath("//input[@name='moduleName']"));
    private static final Locator TEMPLATE_CODE = new Locator("TEMPLATE_CODE", By.xpath("//input[@id='template-code']"));
    private static final Locator TEMPLATE_CODE2 = new Locator("TEMPLATE_CODE2", By.xpath("//p[normalize-space()='GS_POP']"));
    private static final Locator CHOOSE_THE_CURRENCY = new Locator("CHOOSE_THE_CURRENCY", By.xpath("//input[@id='currency']"));
    private static final Locator SELECT_THE_CURRENCY = new Locator("SELECT_THE_CURRENCY", By.xpath("//p[normalize-space()='All']"));
    private static final Locator SHOW_LOGIN_USER = new Locator("SHOW_LOGIN_USER", By.xpath("//span[normalize-space()='Login']"));
    private static final Locator SHOW_LOGOUT_USER = new Locator("SHOW_LOGOUT_USER", By.xpath("//span[normalize-space()='Logout']"));
    private static final Locator SELECT_PERIOD = new Locator("SELECT_PERIOD", By.xpath("//span[normalize-space()='Always On']"));
    private static final Locator HEADING_TITLE_EN = new Locator("HEADING_TITLE_EN", By.xpath("//input[@id='title' and @name='headingEnTitle']"));
    private static final Locator HEADING_TITLE_ID = new Locator("HEADING_TITLE_ID", By.xpath("//input[@id='title' and @name='headingIdTitle']"));
    private static final Locator HEADING_SUBTITLE_EN = new Locator("HEADING_SUBTITLE_EN", By.xpath("//input[@id='subtitle' and @name='headingEnSubtitle']"));
    private static final Locator HEADING_SUBTITLE_ID = new Locator("HEADING_SUBTITLE_ID", By.xpath("//input[@id='subtitle' and @name='headingIdSubtitle']"));
    private static final Locator SUCCESS_SUBMIT = new Locator("SUCCESS_SUBMIT", By.xpath("//div[(contains(@class,'Snackbar_text_content') or contains(@class,'snackbar-container'))" + " and (contains(normalize-space(.),'Successfully edited')" + " or contains(normalize-space(.),'Success add custom'))]"));
    private static final Locator SAVE_AND_GO_TO_CONTENT = new Locator("SAVE_AND_GO_TO_CONTENT", By.xpath("//button[normalize-space()='Save and go to content']"));
    private static final Locator SEE_ALL_BUTTON = new Locator("SEE_ALL_BUTTON", By.xpath("(//div[contains(@class,'Toggle_toggle')])[1]"));
    private static final Locator SEE_ALL_URL_ID = new Locator("SEE_ALL_URL_ID", By.xpath("//input[@placeholder='See all URL (ID)']"));
    private static final Locator SEE_ALL_URL_EN = new Locator("SEE_ALL_URL_EN", By.xpath("//input[@placeholder='See all URL (EN)']"));
    private static final Locator IMAGE_ID = new Locator("IMAGE_ID", By.xpath("(//input[@type='file'])[1]"));
    private static final Locator LOADING_ICON = new Locator("LOADING_ICON", By.cssSelector(".overlay-loading.show"));
    private static final Locator IMAGE_EN = new Locator("IMAGE_EN", By.xpath("(//input[@type='file'])[2]"));
    private static final Locator IMAGE_URL_LINK_ID = new Locator("IMAGE_URL_LINK_ID", By.xpath("(//input[@type='file'])[1]/preceding::input[@placeholder='Image URL Link'][1]"));
    private static final Locator IMAGE_URL_LINK_EN = new Locator("IMAGE_URL_LINK_EN", By.xpath("(//input[@type='file'])[2]/preceding::input[@placeholder='Image URL Link'][1]"));
    private static final Locator TITLE_ID = new Locator("TITLE_ID", By.xpath("//input[@placeholder='Title (ID)']"));
    private static final Locator TITLE_EN = new Locator("TITLE_EN", By.xpath("//input[@placeholder='Title (EN)']"));
    private static final Locator SUBTITLE_RED = new Locator("SUBTITLE_RED", By.xpath("(//div[contains(@class,'Toggle_knob')])[2]"));
    private static final Locator SUBTITLE_ID = new Locator("SUBTITLE_ID", By.xpath("//input[@placeholder='Subtitle (ID)']"));
    private static final Locator SUBTITLE_EN = new Locator("SUBTITLE_EN", By.xpath("//input[@placeholder='Subtitle (EN)']"));
    private static final Locator SUBMIT_CONTENT = new Locator("SUBMIT_CONTENT", By.xpath("//button[contains(@class,'Button_button') and contains(@class,'Button_variant_primary') and normalize-space()='Submit']"));
    private static final Locator SUCCESS_SUBMIT_CONTENT = new Locator("SUCCESS_SUBMIT_CONTENT", By.xpath("//div[contains(@class,'Snackbar_text_content')" + " and (contains(normalize-space(.),'Success submit')" + " or contains(normalize-space(.),'Successfully Update'))]"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", By.xpath("//div[contains(@class,'Snackbar_text_content')]"));
    private static final Locator TEMPLATE_NAME = new Locator("TEMPLATE_NAME", By.xpath("(//input[@type='text'])[1]"));
    private static final Locator SEARCH = new Locator("SEARCH", By.xpath("//span[text()='Search']"));
    private static final Locator ARCHIVE = new Locator("ARCHIVE", By.xpath("//p[contains(normalize-space(),'Archive Module')]"));
    private static final Locator ARCHIVED_MODULE_TAB = new Locator("ARCHIVED_MODULE_TAB", By.xpath("//button[.//span[normalize-space()='Archived Module']]"));
    private static final Locator SUCCESS_ARCHIVE = new Locator("SUCCESS_ARCHIVE", By.xpath("//button[normalize-space()='Archive']"));
    private static final Locator DESKTOP_MODULES_BREADCRUMB = new Locator("DESKTOP_MODULES_BREADCRUMB", By.xpath("//span[contains(@class,'BreadcrumbItem_text') and contains(.,'Desktop Modules')]"));

    private static Locator moduleRowFor(String moduleName) {
        String name = moduleName.replace("\"", "");
        return new Locator("MODULE_ROW", By.xpath("//tr[.//td[contains(normalize-space(.), \"" + name + "\")]]"));
    }

    private String ModuleNameSlug;
    private String generalWindowHandle;
    private String contentWindowHandle;
    private final String seeAllUrlEn = "https://en.gatotkaca.tiket.com/to-do/category/event?utm_page=toDoHome&utm_section=categoryIcon";
    private final String seeAllUrlId = "https://gatotkaca.tiket.com/to-do/category/event?utm_page=toDoHome&utm_section=categoryIcon";
    private final String imgUrlId = "https://www.tiket.com/kereta-api?productType=whoosh";
    private final String imgUrlEn = "https://en.tiket.com/kereta-api?productType=whoosh";

    public void goToGlobalSearchEditGeneral(String username, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(GLOBAL_SEARCH_URL);
        if (!driverTools.getCurrentURL().contains(GLOBAL_SEARCH_URL)) {
            memberLoginPage.goToPage(GLOBAL_SEARCH_URL);
        }
    }

    public void addNewGlobalSearchSection() {
        driverTools.waitForElementToAppear(GET_IN, 5);
        driverTools.clickWithJavascriptExecutor(GET_IN);
        driverTools.waitForElementToAppear(DESKTOP_MODULE, 10);
        driverTools.click(DESKTOP_MODULE);
    }

    public void addModuleName() {
        driverTools.waitForElementToAppear(ADD_CUSTOM_MODULE, 10);
        driverTools.click(ADD_CUSTOM_MODULE);
        driverTools.waitForElementToAppear(MODULE_NAME, 10);
        ModuleNameSlug = "ModuleNameSlug_" + System.currentTimeMillis();
        driverTools.clear(MODULE_NAME);
        driverTools.sendKeys(MODULE_NAME, ModuleNameSlug);
    }

    public void selectTemplateCode() {
        driverTools.click(TEMPLATE_CODE);
        driverTools.waitForElementToAppear(TEMPLATE_CODE2);
        driverTools.click(TEMPLATE_CODE2);
    }

    public void selectCurrency() {
        driverTools.click(CHOOSE_THE_CURRENCY);
        driverTools.waitForElementToAppear(SELECT_THE_CURRENCY);
        driverTools.click(SELECT_THE_CURRENCY);
    }

    public void selectShowUsers() {
        driverTools.click(SHOW_LOGIN_USER);
        driverTools.waitForElementToAppear(SHOW_LOGOUT_USER);
        driverTools.click(SHOW_LOGOUT_USER);
    }

    public void selectPeriod() {
        driverTools.waitForElementToAppear(SELECT_PERIOD, 10);
        driverTools.scrollUp(800);
        driverTools.clickWithJavascriptExecutor(SELECT_PERIOD);
    }

    public void setHeadingTitleEn() {
        driverTools.clickWithJavascriptExecutor(HEADING_TITLE_EN);
        driverTools.sendKeys(HEADING_TITLE_EN, "HeadingTitleEnSlug" + CoreFunctions.randomInt(5));
    }

    public void setHeadingTitleId() {
        driverTools.clickWithJavascriptExecutor(HEADING_TITLE_ID);
        driverTools.sendKeys(HEADING_TITLE_ID, "HeadingTitleIdSlug" + CoreFunctions.randomInt(5));
    }

    public void setHeadingSubTitleEn() {
        driverTools.clickWithJavascriptExecutor(HEADING_SUBTITLE_EN);
        driverTools.sendKeys(HEADING_SUBTITLE_EN, "HeadingSubTitleEnSlug" + CoreFunctions.randomInt(5));
    }

    public void setHeadingSubTitleId() {
        driverTools.clickWithJavascriptExecutor(HEADING_SUBTITLE_ID);
        driverTools.sendKeys(HEADING_SUBTITLE_ID, "HeadingSubTitleIdSlug" + CoreFunctions.randomInt(5));
    }

    public boolean clickSaveAndGotoContent() {
        generalWindowHandle = driver.getWindowHandle();
        int handlesBefore = driver.getWindowHandles().size();
        WebElement saveAndGo = driverTools.waitForElementToAppear(SAVE_AND_GO_TO_CONTENT, 10).orElseThrow(() -> new IllegalStateException("Save and go to content button not found."));
        driverTools.scrollElementToCenter(saveAndGo);
        driverTools.clickWithJavascriptExecutor(saveAndGo);
        boolean saved = driverTools.waitForElementToAppear(SUCCESS_SUBMIT, 15).isPresent();
        try {
            driverTools.waitForNewTabAndSwitchToLatest(handlesBefore);
        } catch (TimeoutException e) {
            if (driver.getWindowHandles().size() > handlesBefore) {
                driverTools.switchToLatestTab();
            }
        }
        contentWindowHandle = driver.getWindowHandle();
        closeGeneralFormTabIfStillOpen();
        driverTools.waitForWebToLoad();
        return saved;
    }

    private void closeGeneralFormTabIfStillOpen() {
        if (generalWindowHandle == null
                || generalWindowHandle.equals(contentWindowHandle)
                || !driver.getWindowHandles().contains(generalWindowHandle)) {
            return;
        }
        String contentTab = contentWindowHandle;
        driver.switchTo().window(generalWindowHandle);
        driver.close();
        if (driver.getWindowHandles().contains(contentTab)) {
            driver.switchTo().window(contentTab);
        } else {
            driverTools.switchToLatestTab();
            contentWindowHandle = driver.getWindowHandle();
        }
    }

    private void switchToContentWindow() {
        if (contentWindowHandle != null && driver.getWindowHandles().contains(contentWindowHandle)) {
            driver.switchTo().window(contentWindowHandle);
        } else if (driver.getWindowHandles().size() > 1) {
            driverTools.switchToLatestTab();
            contentWindowHandle = driver.getWindowHandle();
        }
    }

    public void toggleSeeAllButton() {
        switchToContentWindow();
        WebElement el = driverTools.waitForElementToAppear(SEE_ALL_BUTTON, 20)
                .or(() -> driverTools.waitForElementToPresence(SEE_ALL_BUTTON, 10))
                .orElseThrow(() -> new IllegalStateException("See All toggle not found."));
        driverTools.scrollElementToCenter(el);
        driverTools.clickWithJavascriptExecutor(el);
        driverTools.waitForElementToDisappear(LOADING_ICON, 5);
    }

    public void enterSeeAllUrl() {
        switchToContentWindow();
        sendKeysToUrlField(SEE_ALL_URL_ID, seeAllUrlId);
        sendKeysToUrlField(SEE_ALL_URL_EN, seeAllUrlEn);
    }

    private void sendKeysToUrlField(Locator field, String url) {
        WebElement el = driverTools.waitForElementToAppear(field, 25)
                .or(() -> driverTools.waitForElementToPresence(field, 15))
                .orElseThrow(() -> new IllegalStateException(field.name() + " not found."));
        driverTools.scrollElementToCenter(el);
        driverTools.clickWithJavascriptExecutor(el);
        driverTools.sendKeys(el, url);
    }

    public void uploadImageId(String files) {
        switchToContentWindow();
        fillImageUrlLinkThenUploadFile(IMAGE_URL_LINK_ID, IMAGE_ID, files, imgUrlId);
    }

    public void uploadImageEn(String files) {
        switchToContentWindow();
        fillImageUrlLinkThenUploadFile(IMAGE_URL_LINK_EN, IMAGE_EN, files, imgUrlEn);
    }

    private void fillImageUrlLinkThenUploadFile(Locator imageUrlLink, Locator fileInput, String filePath, String url) {
        WebElement urlEl = driverTools.waitForElementToAppear(imageUrlLink, 25)
                .or(() -> driverTools.waitForElementToPresence(imageUrlLink, 15))
                .orElseThrow(() -> new IllegalStateException("Image URL Link field not found: " + imageUrlLink.name()));
        driverTools.scrollElementToCenter(urlEl);
        driverTools.clickWithJavascriptExecutor(urlEl);
        driverTools.clearWithJsExecutor(urlEl);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(imageUrlLink, url);
        WebElement fileEl = driverTools.waitForElementToPresence(fileInput, 15)
                .orElseThrow(() -> new IllegalStateException("File input not found: " + fileInput.name()));
        driverTools.scrollElementToCenter(fileEl);
        driverTools.sendKeys(fileEl, new File(filePath).getAbsolutePath());
        driverTools.waitForElementToDisappear(LOADING_ICON, 30);
    }

    public void enterTitleId() {
        switchToContentWindow();
        fillTextField(TITLE_ID, "TitleIdSlug" + CoreFunctions.randomInt(5));
    }

    public void enterTitleEn() {
        switchToContentWindow();
        fillTextField(TITLE_EN, "TitleEnSlug" + CoreFunctions.randomInt(5));
    }

    private void fillTextField(Locator field, String value) {
        WebElement el = driverTools.waitForElementToAppear(field, 20)
                .orElseThrow(() -> new IllegalStateException(field.name() + " not found."));
        driverTools.scrollElementToCenter(el);
        driverTools.clickWithJavascriptExecutor(el);
        driverTools.sendKeys(el, value);
    }

    public void toggleSubtitleRed() {
        switchToContentWindow();
        WebElement knob = driverTools.waitForElementToAppear(SUBTITLE_RED, 20)
                .orElseThrow(() -> new IllegalStateException("Subtitle red toggle not found."));
        driverTools.scrollElementToCenter(knob);
        driverTools.clickWithJavascriptExecutor(knob);
        driverTools.waitForElementToDisappear(LOADING_ICON, 5);
    }

    public void enterSubTitleId() {
        switchToContentWindow();
        fillTextField(SUBTITLE_ID, "SubTitleIdSlug" + CoreFunctions.randomInt(5));
    }

    public void enterSubTitleEn() {
        switchToContentWindow();
        fillTextField(SUBTITLE_EN, "SubTitleEnSlug" + CoreFunctions.randomInt(5));
    }

    public void clickSubmitContent() {
        switchToContentWindow();
        WebElement submit = driverTools.waitForElementToAppear(SUBMIT_CONTENT, 20)
                .orElseThrow(() -> new IllegalStateException("Submit content button not found."));
        driverTools.scrollElementToCenter(submit);
        driverTools.clickWithJavascriptExecutor(submit);
    }

    public boolean submitSuccessContent() {
        switchToContentWindow();
        Optional<WebElement> snack = driverTools.waitForElementToAppear(SUCCESS_SUBMIT_CONTENT, 15);
        if (snack.isEmpty()) {
            return false;
        }
        Optional<String> actualMessage = driverTools.getText(SUCCESS_SUBMIT_CONTENT);
        if (actualMessage.isEmpty()) {
            return false;
        }
        Assertion.assertThat(
                "Submit content successfully",
                actualMessage.get().trim(),
                anyOf(containsString("Success submit module"), containsString("Successfully Update Module")));
        return true;
    }

    private static Locator moreMenuForModule(String moduleName) {
        String name = moduleName.replace("\"", "");
        return new Locator("MORE_FOR_MODULE", By.xpath("//tr[.//td[contains(normalize-space(.), \"" + name + "\")]]" + "//div[@aria-label='More actions']"));
    }

    public void archiveGlobalSearch() {
        if (driverTools.isElementDisplayed(SNACKBAR)) {
            driverTools.waitForElementToDisappear(SNACKBAR);
        }
        driverTools.scrollElementToCenter(DESKTOP_MODULES_BREADCRUMB);
        driverTools.click(DESKTOP_MODULES_BREADCRUMB);
        driverTools.waitForElementToAppear(TEMPLATE_NAME);
        driverTools.clear(TEMPLATE_NAME);
        driverTools.sendKeys(TEMPLATE_NAME, ModuleNameSlug);
        driverTools.click(SEARCH);
        Locator moreForModule = moreMenuForModule(ModuleNameSlug);
        driverTools.waitForElementToAppear(moreForModule, 15);
        driverTools.scrollElementToCenter(moreForModule);
        driverTools.clickWithJavascriptExecutor(moreForModule);
        driverTools.waitForElementToAppear(ARCHIVE);
        driverTools.clickWithJavascriptExecutor(ARCHIVE);
        WebElement archiveConfirm = driverTools.waitForElementToAppear(SUCCESS_ARCHIVE, 10).orElseThrow(() -> new IllegalStateException("Archive confirm button not found."));
        driverTools.scrollElementToCenter(archiveConfirm);
        driverTools.clickWithJavascriptExecutor(archiveConfirm);
    }

    public boolean successArchive() {
        WebElement archivedModuleTab = driverTools.waitForElementToAppear(ARCHIVED_MODULE_TAB, 3).or(() -> driverTools.waitForElementToPresence(ARCHIVED_MODULE_TAB, 5)).orElseThrow(() -> new IllegalStateException("Archived Module tab was not found."));

        driverTools.scrollElementToCenter(archivedModuleTab);
        driverTools.clickWithJavascriptExecutor(archivedModuleTab);
        WebElement templateName = driverTools.waitForElementToAppear(TEMPLATE_NAME, 5).orElseThrow(() -> new IllegalStateException("Template Name field was not found in Archived Module tab."));
        driverTools.scrollElementToCenter(templateName);
        driverTools.clear(templateName);
        driverTools.sendKeys(templateName, ModuleNameSlug);

        WebElement searchButton = driverTools.waitForElementToAppear(SEARCH, 5).orElseThrow(() -> new IllegalStateException("Search button was not found."));
        driverTools.scrollElementToCenter(searchButton);
        driverTools.clickWithJavascriptExecutor(searchButton);
        driverTools.waitForElementToDisappear(LOADING_ICON, 5);
        Locator archivedModuleRow = moduleRowFor(ModuleNameSlug);
        Optional<WebElement> archivedModule = driverTools.waitForElementToAppear(archivedModuleRow, 5).or(() -> driverTools.waitForElementToPresence(archivedModuleRow, 5));
        return archivedModule.isPresent();
    }
}
