package com.tiket.web.dashboard.page.platform.liberty.helpCenter.helpTopic;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;

public class HelpTopicPageWeb <T extends WebDriver> extends WebPage<T> {
    public HelpTopicPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HelpTopicPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HelpTopicPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new HelpTopicPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Platform not supported");
        }
    }

    private static final Locator HELP_TOPIC_PAGE = new Locator("HELP_TOPIC_PAGE", By.xpath("//span[contains(@class,'title') and normalize-space()='Help Topic']"));
    private static final Locator ADD_CATEGORY_BUTTON = new Locator("ADD_CATEGORY_BUTTON", By.xpath("(//div[@class='add_btn']//span[normalize-space()='Add New Help Category'])[8]"));
    private static final Locator ENGLISH_LANGUAGE_BTN = new Locator("ENGLISH_LANGUAGE_BTN", By.xpath("//button[@data-testid='btn-lang-en']"));
    private static final Locator ADD_ARTICLE_BUTTON = new Locator("ADD_ARTICLE_BUTTON", By.xpath("//div[contains(@class,'add_article_button')]//button"));
    private static final Locator SEARCH_ARTICLE = new Locator("SEARCH_ARTICLE", By.xpath("//input[@placeholder='Search article']"));
    private static final Locator ARTICLE_OPTION = new Locator("ARTICLE_OPTION", By.xpath("(//div[@data-testid='auto-complete-article-option'])[1]"));
    private static final Locator ADD_BUTTON = new Locator("ADD_BUTTON", By.xpath("//button[contains(@class,'Button_button') and normalize-space()='Add']"));
    private static final Locator CATEGORY_SUCCESS_MESSAGE = new Locator("CATEGORY_SUCCESS_MESSAGE", By.xpath("//div[contains(@class,'Snackbar_text_content') and contains(normalize-space(),'Success ')]"));
    private static final Locator TOGGLE_BUTTON = new Locator("TOGGLE_BUTTON", By.xpath("//div[contains(@class,'Toggle_toggle')]"));
    private static final Locator TOGGLE_CHANGE_SUCCESS_MESSAGE = new Locator("TOGGLE_CHANGE_SUCCESS_MESSAGE", By.xpath("//div[contains(@class,'Snackbar_text_content') and contains(normalize-space(),'Your changes have been saved')]"));
    private static final Locator DELETE_CATEGORY_BUTTON = new Locator("DELETE_CATEGORY_BUTTON", By.xpath("(//div[contains(@class,'trash_icon')])[1]"));
    private static final Locator DELETE_CONFIRMATION_BUTTON = new Locator("DELETE_CONFIRMATION_BUTTON", By.xpath("//button[contains(@class,'Button_button') and normalize-space()='Yes']"));
    private static final Locator FIRST_OPTION = new Locator("FIRST_OPTION", By.xpath("//div[contains(@class,'react_select__menu-list')]/div[contains(@class,'react_select__option')][1]"));
    private static final Locator CATEGORY_NAME_CONTAINER = new Locator("CATEGORY_NAME_CONTAINER", By.xpath("//div[contains(@class,'react_select__placeholder') and normalize-space()='Choose Category']/ancestor::div[contains(@class,'react_select__control')][1]"));
    private static final Locator CATEGORY_NAME_INPUT = new Locator("CATEGORY_NAME_INPUT", By.xpath("//div[contains(@class,'react_select__placeholder') and normalize-space()='Choose Category']/following-sibling::div[contains(@class,'react_select__input-container')]//input[contains(@class,'react_select__input')]"));
    private static final Locator CATEGORY_TOPIC_CONTAINER = new Locator("CATEGORY_TOPIC_CONTAINER", By.xpath("//div[contains(@class,'react_select__placeholder') and normalize-space()='Choose Topic']/ancestor::div[contains(@class,'react_select__control')][1]"));
    private static final Locator CATEGORY_TOPIC_INPUT = new Locator("CATEGORY_TOPIC_INPUT", By.xpath("//div[contains(@class,'react_select__placeholder') and normalize-space()='Choose Topic']/following-sibling::div[contains(@class,'react_select__input-container')]//input[contains(@class,'react_select__input')]"));
    private static final Locator EDIT_HELP_TOPIC = new Locator("EDIT_HELP_TOPIC", By.xpath("(//div[contains(@class,'edit_area')])[last()]"));
    private static final Locator CLOSE_MODAL_BUTTON = new Locator("CLOSE_MODAL_BUTTON", By.xpath("//button[@data-testid='modal-close-button']"));

    public boolean isHelpTopicPageOpened() {
        driverTools.waitForElementToAppear(HELP_TOPIC_PAGE, 10);
        boolean isPageOpened = driverTools.isElementPresent(HELP_TOPIC_PAGE);
        if (!isPageOpened) {
            logger.log("Help Topic Page is not opened.");
        }
        return isPageOpened;
    }

    public void clickAddCategory() {
        driverTools.waitForElementToAppear(ADD_CATEGORY_BUTTON,10);
        driverTools.scrollToElement(ADD_CATEGORY_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADD_CATEGORY_BUTTON);
    }

    public void changeLanguageToEnglish() {
        driverTools.dummyWait(2);
        driverTools.waitForElementToAppear(ENGLISH_LANGUAGE_BTN, 10);
        driverTools.clickWithJavascriptExecutor(ENGLISH_LANGUAGE_BTN);
    }

    public void selectFirstDropdownOption() {
        driverTools.waitForElementToAppear(FIRST_OPTION, 10);
        driverTools.scrollToElement(FIRST_OPTION);
        driverTools.click(FIRST_OPTION);
    }

    public void enterCategoryName(String categoryName){
        driverTools.dummyWait(5);
        
        driverTools.waitForElementToAppear(CATEGORY_NAME_CONTAINER, 10);
        driverTools.click(CATEGORY_NAME_CONTAINER);
        
        driverTools.waitForElementToAppear(CATEGORY_NAME_INPUT, 10);
        driverTools.sendKeys(CATEGORY_NAME_INPUT, categoryName);
        
        driverTools.dummyWait(2);
        selectFirstDropdownOption();
    }

    public void chooseCategoryTopic(String categoryTopic) {
        driverTools.waitForElementToAppear(CATEGORY_TOPIC_CONTAINER, 10);
        driverTools.click(CATEGORY_TOPIC_CONTAINER);
        
        driverTools.waitForElementToAppear(CATEGORY_TOPIC_INPUT, 10);
        driverTools.sendKeys(CATEGORY_TOPIC_INPUT, categoryTopic);
        
        driverTools.dummyWait(2);
        selectFirstDropdownOption();
    }

    public void enterArticle(String articleTitle) {
        driverTools.dummyWait(2);
        driverTools.waitForElementToAppear(ADD_ARTICLE_BUTTON,10);
        driverTools.click(ADD_ARTICLE_BUTTON);

        driverTools.dummyWait(3);
        driverTools.sendKeys(SEARCH_ARTICLE, articleTitle);
        driverTools.click(ARTICLE_OPTION);
    }

    public void clickAddButton() {
        driverTools.waitForElementToAppear(ADD_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(ADD_BUTTON);
    }

    public boolean isCategorySuccessMessageDisplayed() {
        driverTools.waitForElementToAppear(EDIT_HELP_TOPIC, 10);
        boolean isMessageDisplayed = driverTools.isElementPresent(EDIT_HELP_TOPIC);
        if (isMessageDisplayed) {
            try {
                driverTools.click(EDIT_HELP_TOPIC);
            } catch (ElementClickInterceptedException e) {
                logger.log("Click intercepted on edit, retrying with JS executor");
                driverTools.clickWithJavascriptExecutor(EDIT_HELP_TOPIC);
            }
            driverTools.waitForElementToAppear(CLOSE_MODAL_BUTTON, 10);
            try {
                driverTools.click(CLOSE_MODAL_BUTTON);
            } catch (ElementClickInterceptedException e) {
                logger.log("Click intercepted on close button, retrying with JS executor");
                driverTools.clickWithJavascriptExecutor(CLOSE_MODAL_BUTTON);
            }
            driverTools.waitForElementToDisappear(CLOSE_MODAL_BUTTON, 10);
        } else {
            logger.log("Category is not created..");
        }
        return isMessageDisplayed;
    }

    public void clickToggleButton() {
        driverTools.waitForElementToAppear(TOGGLE_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(TOGGLE_BUTTON);
    }

    public boolean isToggleChangeSuccessMessageDisplayed() {
        driverTools.waitForElementToAppear(TOGGLE_CHANGE_SUCCESS_MESSAGE, 10);
        boolean isMessageDisplayed = driverTools.isElementPresent(TOGGLE_CHANGE_SUCCESS_MESSAGE);
        if (!isMessageDisplayed) {
            logger.log("Toggle change success message is not displayed.");
        }
        return isMessageDisplayed;
    }

    public void deleteCategory() {
        driverTools.waitForElementToAppear(DELETE_CATEGORY_BUTTON, 10);
        driverTools.click(DELETE_CATEGORY_BUTTON);

        driverTools.dummyWait(2);
        driverTools.waitForElementToAppear(DELETE_CONFIRMATION_BUTTON, 10);
        driverTools.click(DELETE_CONFIRMATION_BUTTON);
    }

    public boolean verifyDeleteCategory() {
        driverTools.waitForElementToAppear(DELETE_CATEGORY_BUTTON, 5);
        return driverTools.isElementDisplayed(DELETE_CATEGORY_BUTTON);
    }
}
