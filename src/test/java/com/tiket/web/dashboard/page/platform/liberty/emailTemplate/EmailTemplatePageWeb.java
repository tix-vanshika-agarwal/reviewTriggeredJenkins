package com.tiket.web.dashboard.page.platform.liberty.emailTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;

public class EmailTemplatePageWeb <T extends WebDriver> extends WebPage<T> {
    public EmailTemplatePageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> EmailTemplatePageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new EmailTemplatePageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new EmailTemplatePageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Platform not supported");
        }
    }

    private static final Locator EMAIL_TEMPLATE_PAGE = new Locator("EMAIL_TEMPLATE_PAGE", By.xpath("//div[contains(@class,'email_template_header')]//span[normalize-space()='Email Template']"));
    private static final Locator CREATE_EMAIL_TEMPLATE_BUTTON = new Locator("CREATE_EMAIL_TEMPLATE_BUTTON", By.xpath("//button[.//span[normalize-space()='Create New Template']]"));
    private static final Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", By.xpath("//button[@data-testid='button-next']"));
    private static final Locator SELECT_TYPE_INPUT = new Locator("SELECT_TYPE_INPUT", By.xpath("//input[@placeholder='Select Type']"));
    private static final Locator SELECT_RECIPIENT_INPUT = new Locator("SELECT_RECIPIENT_INPUT", By.xpath("//input[@placeholder='Select Recipient']"));
    private static final Locator SELECT_CATEGORY_INPUT = new Locator("SELECT_CATEGORY_INPUT", By.xpath("//input[@placeholder='Select Category']"));
    private static final Locator SELECT_CATEGORY_DETAILS_INPUT = new Locator("SELECT_CATEGORY_DETAILS_INPUT", By.xpath("//input[@placeholder='Select Category Details']"));
    private static final Locator SELECT_VERTICAL_INPUT = new Locator("SELECT_VERTICAL_INPUT", By.xpath("//input[@placeholder='Select Vertical']"));
    private static final Locator TEMPLATE_NAME_INPUT = new Locator("TEMPLATE_NAME_INPUT", By.xpath("//input[@data-testid='template-name-input']"));
    private static final Locator EMAIL_SUBJECT_EN = new Locator("EMAIL_SUBJECT_EN", By.cssSelector("[data-testid='en_subject']"));
    private static final Locator EMAIL_SUBJECT_ID = new Locator("EMAIL_SUBJECT_ID", By.cssSelector("[data-testid='id_subject']"));
    private static final Locator SELECT_HEADER_INPUT = new Locator("SELECT_HEADER_INPUT", By.xpath("//input[@placeholder='Select Header']"));
    private static final Locator SELECT_FOOTER_INPUT = new Locator("SELECT_FOOTER_INPUT", By.xpath("//input[@placeholder='Select Footer']"));
    private static final Locator INDONESIA_TAB = new Locator("INDONESIA_TAB", By.xpath("//div[contains(@class,'tab_menu_title') and normalize-space()='Indonesia']"));
    private static final Locator INDONESIA_TAB_ACTIVE = new Locator("INDONESIA_TAB_ACTIVE", By.xpath("//div[contains(@class,'tab_menu_title') and contains(@class,'active') and normalize-space()='Indonesia']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[.//span[normalize-space()='Submit']]"));
    private static final Locator NAME_CHECK = new Locator("NAME_CHECK", By.xpath("//input[@data-testid='template-name-input']/following-sibling::div[contains(@class,'right_slot_wrapper')]//*[name()='path' and @fill='#00a474']"));
    private static final Locator SUCCESS_MESSAGE = new Locator("SUCCESS_MESSAGE", By.xpath("//div[contains(@class,'snackbars_positioner') and contains(., 'Success create new template')]"));
    private static final Locator MORE_BUTTON = new Locator("MORE_BUTTON", By.xpath("//div[contains(@class,'action_btn')][.//div[normalize-space()='More']]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", By.xpath("//div[contains(@class,'action_row') and contains(.,'Delete')]"));
    private static final Locator DELETE_CONFIRMATION_MSG = new Locator("DELETE_CONFIRMATION_MSG", By.xpath("//div[contains(@class,'snackbars_positioner') and contains(., 'Success delete email template.')]"));

    public void goToEmailTemplatePage(){
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/message/email/template");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/message/email/template");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://tiket.com/admin/message/email/template");
        }
    }

    public boolean isEmailTemplatePageOpened() {
        driverTools.waitForElementToAppear(EMAIL_TEMPLATE_PAGE, 10);
        return driverTools.isElementPresent(EMAIL_TEMPLATE_PAGE);
    }

    public void clickCreateEmailTemplateButton() {
        driverTools.waitForElementToAppear(CREATE_EMAIL_TEMPLATE_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(CREATE_EMAIL_TEMPLATE_BUTTON);
    }

    public Locator getEmailTemplateOption(String templateName) {
        return new Locator("EMAIL_TEMPLATE_OPTION_" + templateName.toUpperCase(), By.xpath("//div[@data-testid='item-email-content-type' and .//p[normalize-space()='" + templateName + "']]"));
    }

    public void clickEmailTemplate(String templateName) {
        Locator EMAIL_TEMPLATE = getEmailTemplateOption(templateName);
        driverTools.waitForElementToAppear(EMAIL_TEMPLATE, 10);
        driverTools.clickWithJavascriptExecutor(EMAIL_TEMPLATE);
    }

    public void clickNextButton() {
        driverTools.waitForElementToAppear(NEXT_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(NEXT_BUTTON);
    }

    public Locator getEmailLayout(String layoutName) {
        return new Locator("EMAIL_LAYOUT_OPTION_" + layoutName.toUpperCase(), By.xpath("//div[@data-testid='item-email-template-container' and .//p[normalize-space()='" + layoutName + "']]"));
    }

    public void clickEmailLayout(String layoutName) {
        Locator EMAIL_LAYOUT = getEmailLayout(layoutName);
        driverTools.waitForElementToAppear(EMAIL_LAYOUT, 10);
        driverTools.clickWithJavascriptExecutor(EMAIL_LAYOUT);

        clickNextButton();
    }

    public void fillOptions(Locator optionLocator, String optionName){
        driverTools.waitForElementToAppear(optionLocator, 10);
        driverTools.sendKeys(optionLocator, optionName);

        Locator TYPE_OPTION = new Locator("TYPE_OPTION", By.xpath("//div[@data-testid='autocomplete_option_label' and normalize-space()='" + optionName + "']"));
        driverTools.scrollToElement(TYPE_OPTION);
        driverTools.clickWithJavascriptExecutor(TYPE_OPTION);
    }

    public void enterSelectType(String type) {
        fillOptions(SELECT_TYPE_INPUT, type);
    }

    public void enterRecipient(String recipient) {
        fillOptions(SELECT_RECIPIENT_INPUT, recipient);
    }

    public void enterCategory(String category) {
        fillOptions(SELECT_CATEGORY_INPUT, category);
    }

    public void enterCategoryDetails(String categoryDetails) {
        fillOptions(SELECT_CATEGORY_DETAILS_INPUT, categoryDetails);
    }

    public void enterVertical(String vertical) {
        fillOptions(SELECT_VERTICAL_INPUT, vertical);
    }

    public void enterTemplateName(String templateName) {
        driverTools.waitForElementToAppear(TEMPLATE_NAME_INPUT, 10);
        driverTools.scrollIntoWebView(TEMPLATE_NAME_INPUT);
        driverTools.clear(TEMPLATE_NAME_INPUT);
        driverTools.sendKeys(TEMPLATE_NAME_INPUT, templateName);
    }

    public boolean isTemplateNameAvailable(String newName) {
        driverTools.waitForElementToAppear(NAME_CHECK, 10);
        if ( driverTools.isElementPresent(NAME_CHECK)) return true;
        else {
            logger.log("Template name is not available.");
            enterTemplateName(newName);
            try{
                driverTools.waitForElementToAppear(NAME_CHECK, 10);
                boolean isNewNameAvailable = driverTools.isElementPresent(NAME_CHECK);
                if(!isNewNameAvailable){
                    logger.log("New Template name is not available.");
                } else {
                    logger.log("New Template name is available.");
                }
                return isNewNameAvailable;
            }
            catch(Exception e){
                logger.log("Retry with new name failed. Exception: " + e.getMessage());
                return false;
            }
        }
    }

    public void enterEmailSubject(String emailSubject) {
        Locator subject = driverTools.isElementPresent(INDONESIA_TAB_ACTIVE) ? EMAIL_SUBJECT_ID : EMAIL_SUBJECT_EN;
        driverTools.waitForElementToAppear(subject, 10);
        driverTools.clear(subject);
        driverTools.sendKeys(subject, emailSubject);
    }

    public void enterHeader(String header) {
        fillOptions(SELECT_HEADER_INPUT, header);
    }

    public void enterFooter(String footer) {
        fillOptions(SELECT_FOOTER_INPUT, footer);
    }

    public void clickIndonesiaTab() {
        driverTools.waitForElementToAppear(INDONESIA_TAB, 10);
        driverTools.clickWithJavascriptExecutor(INDONESIA_TAB);
        driverTools.waitForElementToAppear(EMAIL_SUBJECT_ID, 10);
    }

    public void clickSubmitButton() {
        driverTools.waitForElementToAppear(SUBMIT_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            driverTools.waitForElementToAppear(SUCCESS_MESSAGE, 10);
            return driverTools.isElementPresent(SUCCESS_MESSAGE);
        } catch (Exception e) {
            logger.log("Success message did not appear within the expected time.");
            return false;
        }
    }

    public boolean isTemplateRowPresent(String templateName, String newName) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String todayDate = today.format(formatter);

        Locator NEW_TEMPLATE = new Locator("NEW_TEMPLATE", By.xpath("//tr[td[1][contains(text(),'" + templateName + "') or contains(text(),'" + newName + "')] and td[3][contains(text(),'" + todayDate + "')]]"));

        try {
            driverTools.waitForElementToAppear(NEW_TEMPLATE, 10);
            return driverTools.isElementPresent(NEW_TEMPLATE);
        } catch (Exception e) {
            logger.log("Template row not found. Exception: " + e.getMessage());
            return false;
        }
    }

    public void clickMoreButton() {
        driverTools.waitForElementToAppear(MORE_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(MORE_BUTTON);
    }

    public void clickDeleteButton() {
        driverTools.waitForElementToAppear(DELETE_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(DELETE_BUTTON);
    }

    public boolean isDeleteConfirmationMessageDisplayed() {
        try {
            driverTools.waitForElementToAppear(DELETE_CONFIRMATION_MSG, 10);
            return driverTools.isElementPresent(DELETE_CONFIRMATION_MSG);
        } catch (Exception e) {
            logger.log("Delete confirmation message did not appear within the expected time.");
            return false;
        }
    }
}