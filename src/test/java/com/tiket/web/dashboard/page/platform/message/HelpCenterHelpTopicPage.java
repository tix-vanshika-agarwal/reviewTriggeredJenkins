package com.tiket.web.dashboard.page.platform.message;

import com.tiket.core.CoreFunctions;
import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

public class HelpCenterHelpTopicPage<T extends WebDriver> extends BasePage<T> {
    public HelpCenterHelpTopicPage(Session<T> session){
        super(session);
    }
    ILogger logger = BaseTest.mainLogger.get();

    private static final String HELP_TOPIC_URL = "/admin/helpcenter/help-topics";
    private Locator TITLE_HC_NEW;
    private static final Locator EMAIL_LABEL = new Locator("EMAIL_LABEL", By.className("email_label"));
    private static final Locator HELP_TOPIC_MENU = new Locator("HELP_TOPIC_MENU", By.xpath("//span[contains(text(), 'Help Topics')]"));
    private static final Locator ADD_NEW_HT_BUTTON = new Locator("ADD_NEW_HT_BUTTON", By.xpath("//button[contains(text(), '+ Add Help Topic')]"));
    private static final Locator MODAL_HC = new Locator("MODAL_HC", By.xpath("//div[contains(@class, 'ModalWithLangSwithcer_title_label')]/span[contains(text(), 'Add Help Topic')]"));
    private static final Locator HELP_TOPIC_PAGE = new Locator("HELP_TOPIC_PAGE", By.xpath("//span[contains(@class, 'help-topics_title') and contains(text(), 'Help Topic')]"));
    private static final Locator NAME_COLUMN = new Locator("NAME_COLUMN", By.name("title"));
    private static final Locator META_TITLE_COLUMN = new Locator("META_TITLE_COLUMN", By.name("metaTitle"));
    private static final Locator META_DESCRIPTION_COLUMN = new Locator("META_DESCRIPTION_COLUMN", By.name("metaDescription"));
    private static final Locator CONTENT_EN_BUTTON = new Locator("CONTENT_EN_BUTTON", By.xpath("//button[@data-testid='btn-lang-en']"));
    private static final Locator GENERAL_TAB_BUTTON = new Locator("GENERAL_TAB_BUTTON", By.xpath("//span[contains(@class,'FormHelpTopic_tab_section_item')][contains(text(),'General')]"));
    private static final Locator ICON_TAB_BUTTON = new Locator("ICON_TAB_BUTTON", By.xpath("//span[contains(@class,'FormHelpTopic_tab_section_item')][contains(text(),'Icon')]"));
    private static final Locator SEO_TAB_BUTTON= new Locator("SEO_TAB_BUTTON", By.xpath("//span[contains(@class,'FormHelpTopic_tab_section_item')][contains(text(),'SEO')]"));
    private static final Locator ICON_VERTICAL = new Locator("ICON_VERTICAL", By.xpath("//div[contains(@data-icon,'train')]"));
    private static final Locator ADD_BUTTON = new Locator("ADD_BUTTON", By.xpath("//button[contains(@class,'Button_button')][text() = 'Add']"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", By.xpath("(//div[contains(@class,'HelpTopicItem_action_help_topic')]//div[contains(@class, 'Tooltip_tooltip__32kxf')])[last()]"));
    private static final Locator CONFIRM_DELETE_BUTTON = new Locator("DELETE_BUTTON", By.xpath("//button[contains(@class,'Button_button')][text() = 'Yes']"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("(//div[contains(@class,'HelpTopicItem_action_help_topic')]//div[contains(@class, 'Tooltip_tooltip__32kxf')])[last()-1]"));
    private static final Locator CLOSE_MODAL = new Locator("CLOSE_MODAL", By.xpath("//button[contains(@class, 'close')]"));
    private static final Locator EDIT_MODAL_HC = new Locator("EDIT_MODAL_HC", By.xpath("//div[contains(@class, 'ModalWithLangSwithcer_title_label')]/span[contains(text(), 'Edit Help Topic')]"));
    private static final Locator SUCCESS_DELETE_SNACKBAR = new Locator("SUCCESS_DELETE_SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content') and contains(text(), 'Success delete help topic')]"));
    private static final Locator GET_IN_COUNTRY_BUTTON = new Locator("GET_IN_COUNTRY_BUTTON", By.xpath("//button[@data-testid='button-submit']"));

    public boolean isLogin(){
        driverTools.waitForElementToAppear(EMAIL_LABEL, 2);
        return driverTools.isElementDisplayed(EMAIL_LABEL);
    }

    public void goToHelpTopicPage(String username, String password){
        driverTools.maximizeWindow();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(HELP_TOPIC_URL);
        if (driverTools.isElementDisplayed(GET_IN_COUNTRY_BUTTON)) {
            driverTools.clickWithJavascriptExecutor(GET_IN_COUNTRY_BUTTON);
        }
    }

    public void goToHelpTopicPageViaUI(String username, String password){
        driverTools.maximizeWindow();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.goToPage(HELP_TOPIC_URL);
        memberLoginPage.adminLogin(username, password);
        if (driverTools.isElementDisplayed(GET_IN_COUNTRY_BUTTON)) {
            driverTools.clickWithJavascriptExecutor(GET_IN_COUNTRY_BUTTON);
        }
    }

    public void addHelpTopic() {
        driverTools.click(HELP_TOPIC_MENU);
        driverTools.waitForElementToAppear(ADD_NEW_HT_BUTTON, 1);
        driverTools.click(ADD_NEW_HT_BUTTON);
        driverTools.waitForElementToAppear(MODAL_HC, 1);
        final boolean isHTPageShown = driverTools.isElementDisplayed(HELP_TOPIC_PAGE);
        Assertion.assertThat("Verify Help Topic Page shown", isHTPageShown, is(true));
    }

    public void createDummyValue(){
        final String randomAutomation = CoreFunctions.generateRandomString(6);
        final String helpTopicNameId = "helpTopicNameId-"+ randomAutomation;
        final String hc_metaTitlePageId = "hc_metaTitlePageId-"+ randomAutomation;
        final String hc_metaDescriptionPageId = "hc_metaDescriptionPageId-"+ randomAutomation;
        final String helpTopicNameEn = "helpTopicNameEn-"+ randomAutomation;
        final String hc_metaTitlePageEn = "hc_metaTitlePageEn-"+ randomAutomation;
        final String hc_metaDescriptionPageEn = "hc_metaDescriptionPageEn-"+ randomAutomation;

        TITLE_HC_NEW = new Locator("TITLE_HC_NEW", By.xpath("//div[contains(@class, 'HelpTopicItem_title')][.//text()[contains(., 'helpTopicNameId')]]"));

        sheetData.put("helpTopicNameId", helpTopicNameId.toLowerCase());
        sheetData.put("hc_metaTitlePageId", hc_metaTitlePageId.toLowerCase());
        sheetData.put("hc_metaDescriptionPageId", hc_metaDescriptionPageId.toLowerCase());
        sheetData.put("helpTopicNameEn", helpTopicNameEn.toLowerCase());
        sheetData.put("hc_metaTitlePageEn", hc_metaTitlePageEn.toLowerCase());
        sheetData.put("hc_metaDescriptionPageEn", hc_metaDescriptionPageEn.toLowerCase());
    }

    public void fillDetailsHelpTopic(){
        createDummyValue();
        driverTools.sendKeys(NAME_COLUMN, sheetData.get("helpTopicNameId"));
        driverTools.click(ICON_TAB_BUTTON);
        driverTools.click(ICON_VERTICAL);
        driverTools.click(SEO_TAB_BUTTON);
        driverTools.sendKeys(META_TITLE_COLUMN, sheetData.get("hc_metaTitlePageId"));
        driverTools.sendKeys(META_DESCRIPTION_COLUMN, sheetData.get("hc_metaDescriptionPageId"));
        driverTools.click(CONTENT_EN_BUTTON);
        driverTools.sendKeys(META_TITLE_COLUMN, sheetData.get("hc_metaTitlePageEn"));
        driverTools.sendKeys(META_DESCRIPTION_COLUMN, sheetData.get("hc_metaDescriptionPageEn"));
        driverTools.click(GENERAL_TAB_BUTTON);
        driverTools.sendKeys(NAME_COLUMN, sheetData.get("helpTopicNameEn"));
        driverTools.click(ADD_BUTTON);
    }

    public void scrollEditRecentCreatedHC(){
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(EDIT_BUTTON,1);
        driverTools.click(EDIT_BUTTON);
        driverTools.waitForElementToAppear(EDIT_MODAL_HC,1);
        final boolean modalNewlyCreatedHT = driverTools.isElementDisplayed(EDIT_MODAL_HC);
        Assertion.assertThat("Verify Edit Modal Help Topic Page shown", modalNewlyCreatedHT, is(true));
    }

    public Map<String, String> verifyContentDetails(){
        Map<String, String> resultContentData= new HashMap<>();
        String htNameId = driverTools.getValue(NAME_COLUMN);
        driverTools.click(SEO_TAB_BUTTON);
        String metaTitleHTID = driverTools.getValue(META_TITLE_COLUMN);
        String metaDescriptionHTID = driverTools.getValue(META_DESCRIPTION_COLUMN);
        resultContentData.put("htNameId", htNameId);
        resultContentData.put("metaTitleHTID", metaTitleHTID);
        resultContentData.put("metaDescriptionHTID", metaDescriptionHTID);

        driverTools.click(CONTENT_EN_BUTTON);
        String metaTitleHTEN = driverTools.getValue(META_TITLE_COLUMN);
        String metaDescriptionHTEN = driverTools.getValue(META_DESCRIPTION_COLUMN);
        driverTools.click(GENERAL_TAB_BUTTON);
        String htNameEn = driverTools.getValue(NAME_COLUMN);
        resultContentData.put("htNameEn", htNameEn);
        resultContentData.put("metaTitleHTEN", metaTitleHTEN);
        resultContentData.put("metaDescriptionHTEN", metaDescriptionHTEN);

        Assertion.assertThat("Verify content name ID ", htNameId,  is(sheetData.get("helpTopicNameId")));
        Assertion.assertThat("Verify meta title ID ", metaTitleHTID,  is(sheetData.get("hc_metaTitlePageId")));
        Assertion.assertThat("Verify meta description ID ", metaDescriptionHTID,  is(sheetData.get("hc_metaDescriptionPageId")));
        Assertion.assertThat("Verify content name EN ", htNameEn,  is(sheetData.get("helpTopicNameEn")));
        Assertion.assertThat("Verify meta title EN ", metaTitleHTEN,  is(sheetData.get("hc_metaTitlePageEn")));
        Assertion.assertThat("Verify meta description EN ", metaDescriptionHTEN,  is(sheetData.get("hc_metaDescriptionPageEn")));
        return resultContentData;
    }

    public void deleteHelpTopic(){
        driverTools.click(CLOSE_MODAL);
        driverTools.click(DELETE_BUTTON);
        driverTools.waitForElementToAppear(CONFIRM_DELETE_BUTTON, 1);
        driverTools.click(CONFIRM_DELETE_BUTTON);
        final boolean isSuccessSnackbarDisplayed = driverTools.isElementInView(SUCCESS_DELETE_SNACKBAR);
        Assertion.assertThat("Verify snackbar success delete", isSuccessSnackbarDisplayed, is(true));
    }

    @Setter @Getter
    Map<String, String> sheetData = new HashMap<>();
}