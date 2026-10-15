package com.tiket.web.dashboard.page.platform.message;

import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class HelpCenterSubtopicPage<T extends WebDriver> extends BasePage<T> {
    public HelpCenterSubtopicPage(Session<T> session){
        super(session);
    }
    ILogger logger = BaseTest.mainLogger.get();

    private static final Locator ADD_SUBTOPIC_BUTTON = new Locator("ADD_SUBTOPIC_BUTTON", By.xpath("//button[@data-testid='btn-form' and contains(text(), 'Add Sub Topic')]"));
    private static final Locator MODAL_NEW_SUBTOPIC = new Locator("MODAL_NEW_SUBTOPIC", By.xpath("//div[contains(@class, 'ModalWithLangSwithcer')]/span[contains(text(), 'SubTopic')]"));
    private static final Locator SEO_TAB_BUTTON = new Locator("SEO_TAB_BUTTON", By.xpath("//span[contains(@class,'FormSubTopic_tab_section_item__8uK5z') and contains(text(), 'SEO')]"));
    private static final Locator GENERAL_TAB_BUTTON = new Locator("GENERAL_TAB_BUTTON", By.xpath("//span[contains(@class,'FormSubTopic_tab_section_item__8uK5z') and contains(text(), 'General')]"));
    private static final Locator CONTENT_EN_BUTTON = new Locator("CONTENT_EN_BUTTON", By.xpath("//button[@data-testid='btn-lang-en']"));
    private static final Locator URL_SLUG_INPUT = new Locator("URL_SLUG_INPUT", By.xpath("//input[@id='url-slug']"));
    private static final Locator SUBTOPIC_NAME_INPUT = new Locator("TOPIC_NAME_INPUT", By.xpath("//input[@id='subtopic-name']"));
    private static final Locator META_TITLE = new Locator("META_TITLE", By.xpath("//input[@name='metaTitle']"));
    private static final Locator META_DESCRIPTION = new Locator("META_TITLE", By.xpath("//input[@name='metaDescription']"));
    private static final Locator TOGGLE_PUBLISH = new Locator("TOGGLE_PUBLISH", By.xpath("//div[@class='Toggle_toggle__naszi Toggle_toggle_small__6EQsQ']"));
    private static final Locator TOGGLE_PUBLISHED_ON = new Locator("TOGGLE_PUBLISHED_ON", By.xpath("//input[@data-testid=\"toggle-publish\"]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[text()='Save']"));
    private static final Locator SUCCESS_CREATE_SNACKBAR = new Locator("SUCCESS_CREATE_SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_type_success')]"));
    private static final Locator NEW_CREATED_TOPIC = new Locator("NEW_CREATED_TOPIC", By.xpath("//span[contains(@class, '_topicId__topic_title__7svSe') and contains(text(), 'subtopic')]"));
    private static final Locator LATEST_CREATED_SUBTOPIC = new Locator("LATEST_CREATED_SUBTOPIC", By.xpath("//div[@class='SubTopicItem_title_sub_topic_area__tVvha']//div[contains(text(),'[Subtopic] testing')]"));
    private static final Locator EDIT_BUTTON = new Locator("LATEST_CREATED_SUBTOPIC", By.xpath("//div[@class='SubTopicItem_action_btn__tKfr8'][2]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", By.xpath("//button[text()='Delete']"));
    private static final Locator SNACKBAR_SUCCESS_DELETE = new Locator("SNACKBAR_SUCCESS_DELETE", By.xpath("//div[contains(@class, 'Snackbar_type_success')]/div[contains(text(), 'Success delete topic')]"));

    public boolean verifyNewTopicNameExist(){
        driverTools.waitForElementToAppear(NEW_CREATED_TOPIC,2);
        return driverTools.isElementDisplayed(NEW_CREATED_TOPIC);
    }

    public void clickAddSubtopic(){
        driverTools.waitForElementToPresence(ADD_SUBTOPIC_BUTTON);
        driverTools.click(ADD_SUBTOPIC_BUTTON);
    }

    public boolean isModalSubtopicAppear(){
        driverTools.waitForElementToAppear(MODAL_NEW_SUBTOPIC, 1);
        return driverTools.isElementDisplayed(MODAL_NEW_SUBTOPIC);
    }

    public void inputDetailsSubtopic(
            String urlSlugSubtopicId,
            String subtopicNameId,
            String metaTitleSubtopicId,
            String metaDescSubtopicId,
            String urlSlugSubtopicEn,
            String subtopicNameEn,
            String metaTitleSubtopicEn,
            String metaDescSubtopicEn
    ){
        driverTools.waitForElementToPresence(MODAL_NEW_SUBTOPIC, 2);
        driverTools.click(URL_SLUG_INPUT);
        driverTools.sendKeys(URL_SLUG_INPUT, urlSlugSubtopicId);
        driverTools.sendKeys(SUBTOPIC_NAME_INPUT, subtopicNameId);
        driverTools.click(SEO_TAB_BUTTON);
        driverTools.click(META_TITLE);
        driverTools.sendKeys(META_TITLE, metaTitleSubtopicId);
        driverTools.sendKeys(META_DESCRIPTION, metaDescSubtopicId);

        driverTools.click(CONTENT_EN_BUTTON);
        driverTools.click(META_TITLE);
        driverTools.sendKeys(META_TITLE, metaTitleSubtopicEn);
        driverTools.sendKeys(META_DESCRIPTION, metaDescSubtopicEn);
        driverTools.click(GENERAL_TAB_BUTTON);
        driverTools.click(URL_SLUG_INPUT);
        driverTools.sendKeys(URL_SLUG_INPUT, urlSlugSubtopicEn);
        driverTools.sendKeys(SUBTOPIC_NAME_INPUT, subtopicNameEn);
        driverTools.click(TOGGLE_PUBLISH);
    }

    public void clickSaveButton(){
        driverTools.click(SAVE_BUTTON);
    }

    public boolean saveSuccessSnackbar(){
        return driverTools.isElementDisplayed(SUCCESS_CREATE_SNACKBAR);
    }

    public void clickEditSubtopic(){
        driverTools.waitForElementToAppear(LATEST_CREATED_SUBTOPIC, 2);
        driverTools.click(EDIT_BUTTON);
    }

    public Map<String, String> verifySubtopicContentId(){
        Map<String, String> resultSubtopicContentId = new HashMap<>();
        driverTools.waitForElementToAppear(MODAL_NEW_SUBTOPIC,2);
        driverTools.waitForElementToAppear(URL_SLUG_INPUT,2);
        String urlSlugSubtopicId = driverTools.getValue(URL_SLUG_INPUT);
        String subtopicNameId = driverTools.getValue(SUBTOPIC_NAME_INPUT);
        resultSubtopicContentId.put("urlSlugSubtopicId", urlSlugSubtopicId);
        resultSubtopicContentId.put("subtopicNameId", subtopicNameId);
        driverTools.click(SEO_TAB_BUTTON);
        String metaTitleSubtopicId = driverTools.getValue(META_TITLE);
        String metaDescSubtopicId = driverTools.getValue(META_DESCRIPTION);
        resultSubtopicContentId.put("metaTitleSubtopicId", metaTitleSubtopicId);
        resultSubtopicContentId.put("metaDescSubtopicId", metaDescSubtopicId);
        return resultSubtopicContentId;
    }

    public Map<String, String> verifySubtopicContentEn(){
        Map<String, String> resultSubtopicContentEn = new HashMap<>();
        driverTools.click(CONTENT_EN_BUTTON);
        String metaTitleSubtopicEn = driverTools.getValue(META_TITLE);
        String metaDescSubtopicEn = driverTools.getValue(META_DESCRIPTION);
        resultSubtopicContentEn.put("metaTitleSubtopicEn", metaTitleSubtopicEn);
        resultSubtopicContentEn.put("metaDescSubtopicEn", metaDescSubtopicEn);
        driverTools.click(GENERAL_TAB_BUTTON);
        String urlSlugSubtopicEn = driverTools.getValue(URL_SLUG_INPUT);
        String subtopicNameEn = driverTools.getValue(SUBTOPIC_NAME_INPUT);
        resultSubtopicContentEn.put("urlSlugSubtopicEn", urlSlugSubtopicEn);
        resultSubtopicContentEn.put("subtopicNameEn", subtopicNameEn);
        return resultSubtopicContentEn;
    }

    public boolean verifyToggleButtonActive(){
        driverTools.waitForElementToAppear(TOGGLE_PUBLISH);
        boolean togglebuttonOn = driverTools.isElementEnabled(TOGGLE_PUBLISH);
        return togglebuttonOn;
    }

    public void clickDeleteButton(){
        driverTools.click(DELETE_BUTTON);
    }
}