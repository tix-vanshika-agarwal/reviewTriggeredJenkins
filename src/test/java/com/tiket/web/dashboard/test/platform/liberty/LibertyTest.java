package com.tiket.web.dashboard.test.platform.liberty;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.dashboard.page.platform.liberty.LibertyDashboardHomePage;
import com.tiket.web.dashboard.page.platform.liberty.LibertyInboxAndPNPageWeb;
import com.tiket.web.dashboard.page.platform.liberty.LibertySendBulkEmailPageWeb;
import com.tiket.web.dashboard.page.platform.liberty.helpCenter.helpTopic.HelpTopicPageWeb;
import com.tiket.appium.DriverTools;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.tiket.web.dashboard.page.platform.liberty.emailTemplate.EmailTemplatePageWeb;
import com.tiket.web.dashboard.page.platform.liberty.helpCenter.HelpCenterPageWeb;
import java.lang.reflect.Method;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class LibertyTest extends BaseTest {

    @AfterMethod
    public void cleanUpAfterTest(ITestContext context, Method method) {
        Session<?> session = sessionThreadLocal.get();
        if (session == null || session.driver() == null) return;

        WebDriver webDriver = (WebDriver) session.driver();
        new DriverTools<>(webDriver).closeCurrentTab();
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Message")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testMessageSendBulkEmail() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMessageSendBulkEmail");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<WebDriver>(session);
        LibertyDashboardHomePage<?> libertyDashboardHomePage = LibertyDashboardHomePage.getInstance(session);
        LibertySendBulkEmailPageWeb<?> libertySendBulkEmailPage = new LibertySendBulkEmailPageWeb<WebDriver>(session);

        step("User has access to Liberty Dashboard");
        loginMemberAdmin.gotoHome();
        loginMemberAdmin.loginAsGuest();
        loginMemberAdmin.inputEmail(email);
        loginMemberAdmin.inputPassword(password);
        loginMemberAdmin.clickLogin();

        step("User navigates to message Dashboard");
        libertyDashboardHomePage.goToMessageDashboardPage();

        step("User navigates to send bulk email page");
        libertySendBulkEmailPage.goToSendBulkEmailPage();

        step("User selects category");
        libertySendBulkEmailPage.clickSelectCategoryOption();

        step("User selects template");
        libertySendBulkEmailPage.clickSelectTemplateOption();

        step("User selects language");
        libertySendBulkEmailPage.clickSelectLanguageOption();

        step("User uploads the tsv file");
        String filePath = param.get("filePath");
        libertySendBulkEmailPage.pushData(filePath);

        step("Is download tsv button clickable");
        Assertion.assertThat("Verify download tsv button is clickable", libertySendBulkEmailPage.clickDownloadTsvButton(), is(true));

        step("User turn on the schedule toggle");
        libertySendBulkEmailPage.clickScheduleToggle();

        step("Is Select Date Schedule Displayed");
        Assertion.assertThat("Verify Select Date Schedule Is Displayed", libertySendBulkEmailPage.isSelectDateScheduleDisplayed(), is(true));

        step("User clicks on submit button");
        libertySendBulkEmailPage.clickSubmitButton();

        step("Verify email is in waiting for approval state");
        String templateName = param.get("templateName");
        String statusText = param.get("statusText");
        Assertion.assertThat("Verify email is in waiting for approval state", libertySendBulkEmailPage.checkEmailStatusIsInApprovalState(templateName, statusText), is(true));

        step("Log out");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Message")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testSuperAdminCanCreateEmailTemplate() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testSuperAdminCanCreateEmailTemplate");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String templateType = param.get("templateType");
        String layoutName = param.get("layoutName");
        String type = param.get("type");
        String recipient = param.get("recipient");
        String category = param.get("category");
        String categoryDetails = param.get("categoryDetails");
        String vertical = param.get("vertical");
        String templateName = param.get("templateName");
        String emailSubject = param.get("emailSubject");
        String header = param.get("header");
        String footer = param.get("footer");
        String newName = param.get("newName");

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<WebDriver>(session);
        LibertyDashboardHomePage<?> libertyDashboardHomePage = LibertyDashboardHomePage.getInstance(session);
        EmailTemplatePageWeb<?> emailTemplatePageWeb = EmailTemplatePageWeb.getInstance(session);

        step("User has access to Liberty Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);

        step("User navigates to message Dashboard");
        libertyDashboardHomePage.goToMessageDashboardPage();

        step("User navigates to email template page");
        emailTemplatePageWeb.goToEmailTemplatePage();

        step("Check is email template page is opened");
        boolean isPageOpened = emailTemplatePageWeb.isEmailTemplatePageOpened();
        if(!isPageOpened){
            Assertion.skip("Verify email template page is not opened");
        }

        step("User clicks on create email template button");
        emailTemplatePageWeb.clickCreateEmailTemplateButton();

        step("User selects Email Template Type");
        emailTemplatePageWeb.clickEmailTemplate(templateType);

        step("User clicks on next button");
        emailTemplatePageWeb.clickNextButton();

        step("User selects Email Layout");
        emailTemplatePageWeb.clickEmailLayout(layoutName);

        step("User Enters and Select Type.");
        emailTemplatePageWeb.enterSelectType(type);

        step("User Enters and Select Recipient.");
        emailTemplatePageWeb.enterRecipient(recipient);

        step("User Enters and Select Category.");
        emailTemplatePageWeb.enterCategory(category);

        step("User Enters and Select Category Details.");
        emailTemplatePageWeb.enterCategoryDetails(categoryDetails);

        step("User Enters and Select Vertical.");
        emailTemplatePageWeb.enterVertical(vertical);

        step("User enters template name");
        emailTemplatePageWeb.enterTemplateName(templateName);

        step("User checks if template name is available with retry logic of increasing version number.");
        Assertion.assertThat("Verify template name is available", emailTemplatePageWeb.isTemplateNameAvailable(newName), is(true));

        step("User Enters email Subject for English.");
        emailTemplatePageWeb.enterEmailSubject(emailSubject);

        step("Users Enters header for English.");
        emailTemplatePageWeb.enterHeader(header);

        step("User Enters footer for English.");
        emailTemplatePageWeb.enterFooter(footer);

        step("User selects language as Indonesia.");
        emailTemplatePageWeb.clickIndonesiaTab();

        step("User Enters email Subject for Indonesia.");
        emailTemplatePageWeb.enterEmailSubject(emailSubject);

        step("User Clicks on Submit button");
        emailTemplatePageWeb.clickSubmitButton();

        step("Verify template is created successfully");
        Assertion.assertThat("Verify template is created successfully", emailTemplatePageWeb.isSuccessMessageDisplayed(), is(true));

        step("Verify template is present in the list");
        Assertion.assertThat("Verify template is present in the list", emailTemplatePageWeb.isTemplateRowPresent(templateName, newName), is(true));

        step("User delete the created template");
        emailTemplatePageWeb.clickMoreButton();
        emailTemplatePageWeb.clickDeleteButton();
        emailTemplatePageWeb.clickSubmitButton();

        step("Verify template is deleted successfully");
        Assertion.assertThat("Verify template is deleted successfully", emailTemplatePageWeb.isDeleteConfirmationMessageDisplayed(), is(true));

        step("Log out");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Message")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testMessageSendBulkInboxAndPN() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMessageSendBulkInboxAndPN");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<WebDriver>(session);
        LibertyDashboardHomePage<?> libertyDashboardHomePage = LibertyDashboardHomePage.getInstance(session);
        LibertyInboxAndPNPageWeb<?> libertySendBulkInboxAndPN = new LibertyInboxAndPNPageWeb<WebDriver>(session);

        step("User has access to Liberty Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);

        step("User navigates to message Dashboard");
        libertyDashboardHomePage.goToMessageDashboardPage();

        step("User navigates to send bulk email inbox and PN page");
        libertySendBulkInboxAndPN.goToSendBulkInboxAndPN();

        step("User selects category");
        String selectTemplate = param.get("selectTemplate");
        libertySendBulkInboxAndPN.clickOnSelectTemplateInput(selectTemplate);

        step("User clicks on submit button");
        libertySendBulkInboxAndPN.clickSubmitButton();

        step("User uploads the tsv file");
        String filePath = param.get("filePath");
        libertySendBulkInboxAndPN.pushData(filePath);

        step("User clicks on submit button");
        libertySendBulkInboxAndPN.clickSubmitButton();

        step("Verify bulk inbox and PN sent in the status");
        libertySendBulkInboxAndPN.goToInboxAndPNStatus();
        Assertion.assertThat("Verify selected template is shown in inbox and PN status", libertySendBulkInboxAndPN.checkEmailStatusIsInApprovalState(selectTemplate), is(true));

        step("Log out");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "HelpCenter")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testAdminCanPublishAndUnpublishHelpTopic() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testAdminCanPublishAndUnpublishHelpTopic");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String categoryName = param.get("categoryName");
        String topicName = param.get("topicName");
        String articleTitle = param.get("articleTitle");

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<WebDriver>(session);
        HelpCenterPageWeb<?> helpCenterPageWeb = HelpCenterPageWeb.getInstance(session);
        HelpTopicPageWeb<?> helpTopicPageWeb = HelpTopicPageWeb.getInstance(session);

        step("User has access to Liberty Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);

        step("User navigates to Help Centre Dashboard");
        helpCenterPageWeb.goToHelpCenter();

        step("User clicks on Get In button");
        helpCenterPageWeb.clickGetIn();

        step("Verify Help Center page is opened");
        Assertion.assertThat("Verify Help Center page is opened", helpCenterPageWeb.isHelpCenterPageOpened(), is(true));

        step("User opens Help Topic page.");
        helpCenterPageWeb.clickSideBarOptions("Help Topics");

        step("Verify Help Topic page is opened");
        Assertion.assertThat("Verify Help Topic page is opened", helpTopicPageWeb.isHelpTopicPageOpened(), is(true));

        step("User clicks on Add Category button");
        helpTopicPageWeb.clickAddCategory();

        step("User changes language to English");
        helpTopicPageWeb.changeLanguageToEnglish();

        step("User selects category name");
        helpTopicPageWeb.enterCategoryName(categoryName);

        step("User selects category topic");
        helpTopicPageWeb.chooseCategoryTopic(topicName);

        step("User Enters Article Name");
        helpTopicPageWeb.enterArticle(articleTitle);

        step("User clicks on Add button");
        helpTopicPageWeb.clickAddButton();

        step("Verify category is created successfully");
        Assertion.assertThat("Verify category is created successfully", helpTopicPageWeb.isCategorySuccessMessageDisplayed(), is(true));

        step("User clicks on toggle button to publish the topic");
        helpTopicPageWeb.clickToggleButton();

        step("Verify toggle change success message is displayed");
        Assertion.assertThat("Verify toggle change success message is displayed", helpTopicPageWeb.isToggleChangeSuccessMessageDisplayed(), is(true));

        step("User again clicks on toggle button to Un-publish the topic");
        helpTopicPageWeb.clickToggleButton();

        step("Verify toggle change success message is displayed");
        Assertion.assertThat("Verify toggle change success message is displayed", helpTopicPageWeb.isToggleChangeSuccessMessageDisplayed(), is(true));

        step("Reset to Normal state");
        helpTopicPageWeb.deleteCategory();

        step("Verify category is deleted successfully");
        Assertion.assertThat("Verify category is deleted successfully", helpTopicPageWeb.isCategorySuccessMessageDisplayed(), is(true));

        step("Log out");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "HelpCenter")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testAddHelpCategory() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testAddHelpCategory");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String categoryName = param.get("categoryName");
        String topicName = param.get("topicName");
        String articleTitle = param.get("articleTitle");

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<WebDriver>(session);
        HelpCenterPageWeb<?> helpCenterPageWeb = HelpCenterPageWeb.getInstance(session);
        HelpTopicPageWeb<?> helpTopicPageWeb = HelpTopicPageWeb.getInstance(session);

        step("User has access to Liberty Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);

        step("User navigates to Help Centre Dashboard");
        helpCenterPageWeb.goToHelpCenter();

        step("User clicks on Get In button");
        helpCenterPageWeb.clickGetIn();

        step("Verify Help Center page is opened");
        Assertion.assertThat("Verify Help Center page is opened", helpCenterPageWeb.isHelpCenterPageOpened(), is(true));

        step("User opens Help Topic page.");
        helpCenterPageWeb.clickSideBarOptions("Help Topics");

        step("Verify Help Topic page is opened");
        Assertion.assertThat("Verify Help Topic page is opened", helpTopicPageWeb.isHelpTopicPageOpened(), is(true));

        step("Verify category is created successfully");
        Assertion.assertThat("Verify category is created successfully", helpTopicPageWeb.isCategorySuccessMessageDisplayed(), is(true));

        step("Verify category is deleted successfully");
        Assertion.assertThat("Verify category is deleted successfully", helpTopicPageWeb.verifyDeleteCategory(), is(true));

        step("Log out");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "HelpCenter")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testAdminCreateArticle() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testAdminCreateArticle");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String title = "Automation" + CoreFunctions.randomName();
        String content = "Automation" + CoreFunctions.randomName();
        String caseCategory = "Automation" + CoreFunctions.randomName();
        String slug = "automation-" + CoreFunctions.randomName();
        String metaTitle = "Automation" + CoreFunctions.randomName();
        String metaDescription = "Automation" + CoreFunctions.randomName();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<WebDriver>(session);
        HelpCenterPageWeb<?> helpCenterPageWeb = HelpCenterPageWeb.getInstance(session);
        HelpTopicPageWeb<?> helpTopicPageWeb = HelpTopicPageWeb.getInstance(session);

        step("User has access to Liberty Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);

        step("User navigates to Help Centre Dashboard");
        helpCenterPageWeb.goToHelpCenter();

        step("User clicks on Get In button");
        helpCenterPageWeb.clickGetIn();

        step("Verify Help Center page is opened");
        Assertion.assertThat("Verify Help Center page is opened", helpCenterPageWeb.isHelpCenterPageOpened(), is(true));

        step("User clicks on any sub topic");
        helpCenterPageWeb.clickAkunSayaTopic();

        step("User clicks on Add Article button");
        helpCenterPageWeb.clickAddArticleButton();

        step("User Enters Article Details");
        helpCenterPageWeb.enterArticleDetailsID(title, content, caseCategory, slug, metaTitle, metaDescription);

        step("User changes language to English");
        helpTopicPageWeb.changeLanguageToEnglish();

        step("User Enters Article Details in English");
        helpCenterPageWeb.enterArticleDetailsEN(title, content, caseCategory, slug, metaTitle, metaDescription);

        step("User clicks on Save button");
        helpCenterPageWeb.clickSaveButton();

        step("User verifies article is created");
        Assertion.assertThat("Verify article is created successfully", helpCenterPageWeb.isArticleCreated(), is(true));

        step("User deletes the created article");
        helpCenterPageWeb.clickDeleteButton();

        step("Log out");
        loginMemberAdmin.logout();
    }
}
