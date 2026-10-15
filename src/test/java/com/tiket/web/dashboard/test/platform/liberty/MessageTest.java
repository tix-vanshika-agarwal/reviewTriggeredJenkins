package com.tiket.web.dashboard.test.platform.liberty;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import com.tiket.web.dashboard.page.platform.liberty.message.inbox.InboxPNStatusPageWeb;
import com.tiket.web.dashboard.page.platform.liberty.message.inbox.SendBulkInboxPNPageWeb;
import com.tiket.web.dashboard.page.platform.liberty.message.MessageDashboardPageMWeb;
import com.tiket.web.dashboard.page.platform.liberty.message.MessageDashboardPageWeb;
import com.tiket.web.dashboard.page.platform.liberty.message.email.EmailStatusPageWeb;
import com.tiket.web.dashboard.page.platform.liberty.message.email.SendBulkEmailPageWeb;
import com.tiket.appium.DriverTools;
import com.tiket.web.dashboard.page.platform.liberty.message.live_activity.LiveActivityPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;

import static com.tiket.core.CoreFunctions.randomName;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;

import static com.tiket.testdata.TestData.getParam;

public class MessageTest extends BaseTest {

    @AfterMethod
    public void cleanUpAfterTest(ITestContext context, Method method) {
        Session<?> session = sessionThreadLocal.get();
        if (session == null || session.driver() == null) return;

        WebDriver webDriver = (WebDriver) session.driver();
        new DriverTools<>(webDriver).closeCurrentTab();
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Message")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testMessageSubmitToSendBulkEmail() {
        Map<String, String> param = getParam("testMessageSubmitToSendBulkEmail");
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MessageDashboardPageWeb<?> messageDashboardPageWeb = MessageDashboardPageWeb.getInstance(session);
        SendBulkEmailPageWeb<?> sendBulkEmailPage = new SendBulkEmailPageWeb<>(session);
        EmailStatusPageWeb<?> emailStatusPage = new EmailStatusPageWeb<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String category = param.get("category");
        String template = param.get("template");
        String language = param.get("language");
        String[] emails = param.get("emails").split(" ");

        step("Login to dashboard message");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(username, password);

        step("Open menu Send Bulk Email");
        sendBulkEmailPage.goToPage();

        step("Select category");
        sendBulkEmailPage.selectCategory(category);

        step("Select template");
        sendBulkEmailPage.selectTemplate(template);

        step("Select language");
        sendBulkEmailPage.selectLanguage(language);

        step("Download file.tsv for selected template");
        sendBulkEmailPage.downloadTSV();

        step("Process .tsv file");
        sendBulkEmailPage.writeTSVWithEmails(emails);

        step("Upload file .tsv");
        sendBulkEmailPage.uploadTSVToFileInput();

        step("Click button Submit");
        sendBulkEmailPage.clickSubmitButton();

        step("Assertions");
        emailStatusPage.searchWithTemplateName(template);
        Assertion.assertThat("Submitted bulk will show on Email Status Bulk list with status “Waiting for Approval”",
                emailStatusPage.validateFirstRowStatusWithTemplateNameAndRequestor(template, username, "Waiting for Approval"),
                is(true));

        step("Log out");
        messageDashboardPageWeb.logout();
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Message")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testMessageApproveBulkEmailOnEmailStatus() {
        Map<String, String> param = getParam("testMessageApproveBulkEmailOnEmailStatus");
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MessageDashboardPageWeb<?> messageDashboardPageWeb = MessageDashboardPageWeb.getInstance(session);
        SendBulkEmailPageWeb<?> sendBulkEmailPage = SendBulkEmailPageWeb.getInstance(session);
        EmailStatusPageWeb<?> emailStatusPage = EmailStatusPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String category = param.get("category");
        String template = param.get("template");
        String language = param.get("language");
        String[] emails = param.get("emails").split(" ");
        String status = param.get("status");

        step("Login to Dashboard Message");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Successfully submit the email bulk");
        sendBulkEmailPage.goToPage();
        sendBulkEmailPage.selectCategory(category);
        sendBulkEmailPage.selectTemplate(template);
        sendBulkEmailPage.selectLanguage(language);
        sendBulkEmailPage.downloadTSV();
        sendBulkEmailPage.writeTSVWithEmails(emails);
        sendBulkEmailPage.uploadTSVToFileInput();
        sendBulkEmailPage.clickSubmitButton();

        step("Open menu Email Status Bulk");
        emailStatusPage.goToPage();

        step("See the submitted bulk with status 'Waiting for Approval'");
        emailStatusPage.setStatusFilter(status);

        step("Click button Approve on action list");
        String id = emailStatusPage.getRandomBulkId();
        emailStatusPage.clickApproveButtonWithBulkId(id);

        step("Click button Approve on popup confirmation");
        emailStatusPage.approveConfirmation();

        step("Assertions");
        Assertion.assertThat("The status of email bulk will show status 'Sent'",
                emailStatusPage.verifyStatusWithBulkId("Sent", id),
                is(true)
        );

        step("Log out");
        messageDashboardPageWeb.logout();
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Message")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testMessageSendBulkInboxOnInboxAndPN() {
        Map<String, String> param = getParam("testMessageSendBulkInboxOnInboxAndPN");
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MessageDashboardPageWeb<?> messageDashboardPageWeb = MessageDashboardPageMWeb.getInstance(session);
        SendBulkInboxPNPageWeb<?> sendBulkInboxPNPage = SendBulkInboxPNPageWeb.getInstance(session);
        InboxPNStatusPageWeb<?> inboxPNStatusPage = InboxPNStatusPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String template = param.get("template");
        String content = param.get("content");

        step("User login dashboard");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("User go to Inbox & Push Notification");
        sendBulkInboxPNPage.goToPage();

        step("User choose template");
        sendBulkInboxPNPage.selectTemplate(template);

        step("User upload matched template to be sent");
        sendBulkInboxPNPage.uploadFiles(content);

        step("User click submit");
        sendBulkInboxPNPage.waitIfNecessary();
        LocalDateTime submitTime = LocalDateTime.now();
        sendBulkInboxPNPage.clickSubmitButton();

        step("Assertions");
        inboxPNStatusPage.goToPage();
        inboxPNStatusPage.searchTemplate(template);
        Assertion.assertThat("User successfully sent inbox bulk",
                inboxPNStatusPage.verifyStatus(template, submitTime, content),
                is(true)
        );

        step("Log out");
        messageDashboardPageWeb.logout();
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Message")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testMessageDownloadTSVToSendBulkEmail() {
        Map<String, String> param = getParam("testMessageDownloadTSVToSendBulkEmail");
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MessageDashboardPageWeb<?> messageDashboardPageWeb = MessageDashboardPageWeb.getInstance(session);
        SendBulkEmailPageWeb<?> sendBulkEmailPage = SendBulkEmailPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String category = param.get("category");
        String template = param.get("template");
        String language = param.get("language");

        step("Login to Dashboard Message");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Admin open meny Send Bulk Email");
        sendBulkEmailPage.goToPage();

        step("Select category");
        sendBulkEmailPage.selectCategory(category);

        step("Select template");
        sendBulkEmailPage.selectTemplate(template);

        step("Select language");
        sendBulkEmailPage.selectLanguage(language);

        step("Download file .tsv for selected template");
        sendBulkEmailPage.downloadTSV();

        step("Assertions");
        Assertion.assertThat("Successfully download the file .tsv",
                sendBulkEmailPage.verifyTsvHasBeenDownloaded(template),
                is(true));

        step("Log out");
        messageDashboardPageWeb.logout();
    }

    @Author(names = {"Vanshika Agarwal"})
    @Module(name = "Message")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testMessageDuplicateWaitingForApprovalTemplate() {
        Map<String, String> param = getParam("testMessageDuplicateWaitingForApprovalTemplate");
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LiveActivityPageWeb<?> liveActivityPage = new LiveActivityPageWeb<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String newTemplate = "automation_" + randomName();
        String fullTemplate = "order_" + newTemplate;
        String templateType = param.get("type");
        String dynamicLink = param.get("link");
        String subtextId = param.get("link") + " id";
        String subtextEn = param.get("link") + " en";

        try {
            step("Login to dashboard message");
            memberLoginPage.gotoMemberLoginPage();
            memberLoginPage.adminLogin(username, password);

            step("Open Live Activity List Page");
            liveActivityPage.goToPage();

            step("Click 3 dots icon on the template with status 'Waiting for approval'");
            liveActivityPage.getFirstWaitingForApprovalTemplateName();
            liveActivityPage.clickActionOnWaitingForApprovalTemplate();

            step("Select Duplicate");
            liveActivityPage.clickDuplicate();

            step("Update template name");
            liveActivityPage.updateTemplateName(newTemplate);

            step("Update template type");
            liveActivityPage.selectTemplateType(templateType);

            step("Click to next button (Step 1 -> Step 2)");
            liveActivityPage.clickNextOrSubmitButton();

            step("Fill all the mandatory fields (Link and Subtext) for Step 'Start'");
            liveActivityPage.fillLink(dynamicLink);
            liveActivityPage.fillSubtext(subtextId, subtextEn);

            step("Click 'Same as above' for End step");
            liveActivityPage.clickSameAsAbove();

            step("Click to next button (Step 2 -> Step 3)");
            liveActivityPage.clickNextOrSubmitButton();

            step("Verify JSON payload preview matches configuration");
            String jsonText = liveActivityPage.getJsonPreviewText();

            Assertion.assertThat("JSON contains expected templateCode",
                    jsonText, containsStringIgnoringCase("\"templateCode\": \"" + fullTemplate + "\""));
            Assertion.assertThat("JSON contains expected clickUrl",
                    jsonText, containsStringIgnoringCase("\"clickUrl\": \"https://tiket.com/" + dynamicLink + "\""));
            Assertion.assertThat("JSON contains expected primarySubtext text",
                    jsonText, containsStringIgnoringCase("\"text\": \"" + subtextId + "\""));

            step("Verify visual mockup previews match user input");
            Assertion.assertThat("Indonesian high emphasis preview matches",
                    liveActivityPage.getPreviewHighEmphasisText(1), is(subtextId));
            Assertion.assertThat("Indonesian low emphasis preview matches",
                    liveActivityPage.getPreviewLowEmphasisText(1), is(subtextId));

            Assertion.assertThat("English high emphasis preview matches",
                    liveActivityPage.getPreviewHighEmphasisText(2), is(subtextEn));
            Assertion.assertThat("English low emphasis preview matches",
                    liveActivityPage.getPreviewLowEmphasisText(2), is(subtextEn));

            step("Click Save button");
            liveActivityPage.clickSaveButton();

            step("Verify duplicated template exists in the list");
            liveActivityPage.goToPage();

            Assertion.assertThat("Duplicated template should be visible in the list",
                    liveActivityPage.isTemplateExist(fullTemplate.toLowerCase()), equalTo(true));

        } finally {
            step("Clean up: Delete the newly duplicated template");
            liveActivityPage.safeDeleteTemplate(fullTemplate.toLowerCase());
        }
    }
}
