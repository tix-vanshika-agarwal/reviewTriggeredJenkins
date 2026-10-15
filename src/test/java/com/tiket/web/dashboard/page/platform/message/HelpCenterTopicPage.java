package com.tiket.web.dashboard.page.platform.message;

import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class HelpCenterTopicPage<T extends WebDriver> extends BasePage<T> {
    public HelpCenterTopicPage(Session<T> session){
        super(session);
    }
    ILogger logger = BaseTest.mainLogger.get();

    private static final String HELP_CENTER_URL = "https://gatotkaca.tiket.com/admin/helpcenter";
    private static final Locator EMAIL_LABEL = new Locator("EMAIL_LABEL", By.xpath("//div[contains(@class, 'Header_email_label')]"));
    private static final Locator LATEST_CATEGORY= new Locator("LATEST_CATEGORY", By.xpath("//div[contains(@class, 'CategoryMenus_label_content') and contains(text(), 'Automation Cat Id')]"));
    private static final Locator ADD_TOPIC_BUTTON= new Locator("ADD_TOPIC_BUTTON", By.xpath("//div[contains(@class, 'CategoryMenus_label_content') and contains(text(), '[TOPIC]')]//following::div[contains(@class, 'CategoryMenus_topic_list_container')][contains(@class, 'CategoryMenus_show')]//div[contains(text(), 'Add Topic')]"));
    private static final Locator MODAL_NEW_TOPIC = new Locator("MODAL_NEW_TOPIC", By.xpath("//div[contains(@class,'ModalWithLangSwithcer_title_label')]"));
    private static final Locator SEO_TAB_BUTTON = new Locator("SEO_TAB_BUTTON", By.xpath("//div[contains(@class, 'FormMasterData_tab_section')]/span[text()='SEO']"));
    private static final Locator GENERAL_TAB_BUTTON = new Locator("GENERAL_TAB_BUTTON", By.xpath("//div[contains(@class,'FormMasterData_tab_section')]/span[text()='General']"));
    private static final Locator CONTENT_EN_BUTTON = new Locator("CONTENT_EN_BUTTON", By.xpath("//button[@data-testid='btn-lang-en']"));
    private static final Locator URL_SLUG_INPUT = new Locator("URL_SLUG_INPUT", By.xpath("//input[@id='url-slug']"));
    private static final Locator TOPIC_NAME_INPUT = new Locator("TOPIC_NAME_INPUT", By.xpath("//input[@id='topic-name']"));
    private static final Locator META_TITLE = new Locator("META_TITLE", By.xpath("//input[@name='metaTitle']"));
    private static final Locator META_DESCRIPTION = new Locator("META_TITLE", By.xpath("//input[@name='metaDescription']"));
    private static final Locator TOGGLE_PUBLISH = new Locator("TOGGLE_PUBLISH", By.xpath("//div[contains(@class, 'Toggle_toggle')]"));
    private static final Locator TOGGLE_PUBLISHED_ON = new Locator("TOGGLE_PUBLISHED_ON", By.xpath("//input[@data-testid=\"toggle-publish\"]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[text()='Save']"));
    private static final Locator SUCCESS_CREATE_SNACKBAR = new Locator("SUCCESS_CREATE_SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));
    private static final Locator LATEST_CREATED_TOPIC = new Locator("LATEST_CREATED_TOPIC", By.xpath("//div[contains(@class, 'CategoryMenus_name_topic') and contains(text(), 'topic name id')]"));
    private static final Locator TITLE_TOPIC = new Locator("TITLE_TOPIC", By.xpath("//span[contains(@class, 'topic_title') and contains(text(), 'topic name id')]"));
    private static final Locator EDIT_LATEST_TOPIC = new Locator("EDIT_LATEST_TOPIC", By.xpath("//div[contains(@class, 'CategoryMenus_name_topic') and contains(text(), 'topic name id')]/following::div[contains(@class, 'CategoryMenus_edit_area')]//div[contains(@class, 'Tooltip')][1]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", By.xpath("//button[text()='Delete']"));
    private static final Locator SNACKBAR_SUCCESS_DELETE = new Locator("SNACKBAR_SUCCESS_DELETE", By.xpath("//div[contains(@class, 'Snackbar_type_success')]/div[contains(text(), 'Success delete topic')]"));
    private static final Locator EDIT_CATEGORY = new Locator("EDIT_LOCATOR",  By.xpath("(//div[contains(@class,'CategoryMenus_edit_area')])[last()]"));
    private static final Locator NEW_INPUT_CATEGORY = new Locator("NEW_INPUT_CATEGORY", By.xpath("//div[contains(@class, 'CategoryMenus_label_content') and contains(text(), 'Automation Cat Id')]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("(//div[contains(@class,'CategoryMenus_edit_area')])[last()]"));

    public boolean newCategoryExist(){
        driverTools.waitForElementToAppear(LATEST_CATEGORY, 2);
        driverTools.scrollToElement(LATEST_CATEGORY);
        boolean latestCategory = driverTools.isElementDisplayed(LATEST_CATEGORY);
        return  latestCategory;
    }

    public void clickAddTopic(){
        driverTools.waitForElementToAppear(ADD_TOPIC_BUTTON, 2);
        driverTools.click(ADD_TOPIC_BUTTON);
    }


    public boolean modalTopicButton(){
        driverTools.waitForElementToAppear(MODAL_NEW_TOPIC, 2);
        boolean modalTopic = driverTools.isElementDisplayed(MODAL_NEW_TOPIC);
        return modalTopic;
    }

    public void inputDetailsTopic(
            String urlSlugTopicId,
            String topicNameId,
            String metaTitleTopicId,
            String metaDescTopicId,
            String urlSlugTopicEn,
            String topicNameEn,
            String metaTitleTopicEn,
            String metaDescTopicEn
    ){
        driverTools.waitForElementToPresence(MODAL_NEW_TOPIC, 2);
        driverTools.click(URL_SLUG_INPUT);
        driverTools.sendKeys(URL_SLUG_INPUT, urlSlugTopicId);
        driverTools.sendKeys(TOPIC_NAME_INPUT, topicNameId);
        driverTools.click(SEO_TAB_BUTTON);
        driverTools.click(META_TITLE);
        driverTools.sendKeys(META_TITLE, metaTitleTopicId);
        driverTools.sendKeys(META_DESCRIPTION, metaDescTopicId);

        driverTools.click(CONTENT_EN_BUTTON);
        driverTools.click(META_TITLE);
        driverTools.sendKeys(META_TITLE, metaTitleTopicEn);
        driverTools.sendKeys(META_DESCRIPTION, metaDescTopicEn);
        driverTools.click(GENERAL_TAB_BUTTON);
        driverTools.click(URL_SLUG_INPUT);
        driverTools.sendKeys(URL_SLUG_INPUT, urlSlugTopicEn);
        driverTools.sendKeys(TOPIC_NAME_INPUT, topicNameEn);
        driverTools.click(TOGGLE_PUBLISH);
    }

    public void clickSaveButton(){
        driverTools.click(SAVE_BUTTON);
    }

    public boolean saveSuccessSnackbar(){
        return driverTools.isElementDisplayed(SUCCESS_CREATE_SNACKBAR);
    }

    public void clickNewTopic(){
        driverTools.waitForElementToAppear(LATEST_CREATED_TOPIC, 2);
        driverTools.click(LATEST_CREATED_TOPIC);
    }

    public boolean verifyTitleTopic(){
        driverTools.waitForElementToAppear(TITLE_TOPIC, 2);
        boolean titleTopic = driverTools.isElementDisplayed(TITLE_TOPIC);
        return titleTopic;
    }

    public void editLatestTopic(){
        driverTools.waitForElementToPresence(LATEST_CREATED_TOPIC);
        driverTools.waitForElementToBeClickable(EDIT_LATEST_TOPIC, 15);
        try {
            driverTools.click(EDIT_LATEST_TOPIC);
        } catch (ElementClickInterceptedException e) {
            logger.log("Click intercepted, retrying with JS executor");
            driverTools.clickWithJavascriptExecutor(EDIT_LATEST_TOPIC);
        }
    }

    public Map<String, String> verifyTopicContentId(){
        Map<String, String> resultTopicContentId = new HashMap<>();
        driverTools.waitForElementToAppear(MODAL_NEW_TOPIC,2);
        driverTools.waitForElementToAppear(URL_SLUG_INPUT,2);
        driverTools.waitForElementToAppear(TOPIC_NAME_INPUT,2);
        String urlSlugTopicId = driverTools.getValue(URL_SLUG_INPUT);
        String topicNameId = driverTools.getValue(TOPIC_NAME_INPUT);
        resultTopicContentId.put("urlSlugTopicId", urlSlugTopicId);
        resultTopicContentId.put("topicNameId", topicNameId);
        driverTools.dummyWait(2);
        driverTools.click(SEO_TAB_BUTTON);
        String metaTitleTopicId = driverTools.getValue(META_TITLE);
        String metaDescTopicId = driverTools.getValue(META_DESCRIPTION);
        resultTopicContentId.put("metaTitleTopicId", metaTitleTopicId);
        resultTopicContentId.put("metaDescTopicId", metaDescTopicId);
        return resultTopicContentId;
    }

    public Map<String, String> verifyTopicContentEn(){
        Map<String, String> resultTopicContentEn = new HashMap<>();
        driverTools.click(CONTENT_EN_BUTTON);
        String metaTitleTopicEn = driverTools.getValue(META_TITLE);
        String metaDescTopicEn = driverTools.getValue(META_DESCRIPTION);
        resultTopicContentEn.put("metaTitleTopicEn", metaTitleTopicEn);
        resultTopicContentEn.put("metaDescTopicEn", metaDescTopicEn);
        driverTools.click(GENERAL_TAB_BUTTON);
        String urlSlugTopicEn = driverTools.getValue(URL_SLUG_INPUT);
        String topicNameEn = driverTools.getValue(TOPIC_NAME_INPUT);
        resultTopicContentEn.put("urlSlugTopicEn", urlSlugTopicEn);
        resultTopicContentEn.put("topicNameEn", topicNameEn);
        return resultTopicContentEn;
    }

    public boolean verifyToggleButtonActive(){
        driverTools.waitForElementToAppear(TOGGLE_PUBLISH);
        boolean togglebuttonOn = driverTools.isElementEnabled(TOGGLE_PUBLISHED_ON);
        return togglebuttonOn;
    }

    public void clickDeleteButton(){
        driverTools.click(DELETE_BUTTON);
    }

    public void scrollAndEdit(){
        driverTools.scrollToElement(EDIT_BUTTON);
        driverTools.waitForElementToAppear(NEW_INPUT_CATEGORY, 2);
    }

    public void clickDeleteCategory(){
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(EDIT_BUTTON,3);
        driverTools.click(EDIT_CATEGORY);
        driverTools.waitForElementToAppear(DELETE_BUTTON, 2);
        try {
            driverTools.click(DELETE_BUTTON);
        } catch (ElementClickInterceptedException e) {
            logger.log("Click intercepted, retrying with JS executor");
            driverTools.clickWithJavascriptExecutor(DELETE_BUTTON);
        }
    }

    public boolean verifySnackbarSuccessDelete(){
        return (driverTools.isElementDisplayed(SNACKBAR_SUCCESS_DELETE));
    }
}