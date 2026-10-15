package com.tiket.web.test.platform.liberty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.FailedLoginException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.testng.annotations.Test;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.HotelApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import static com.tiket.testdata.TestData.getParam;
import com.tiket.web.dWeb.page.account.InboxPage;
import com.tiket.web.dWeb.page.home.SettingsPage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.common.home.Setting_Page.SettingsPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.platform.liberty.HelpCenterPageWeb;
import com.tiket.web.page.platform.sphinx.refund.RefundPageWeb;

public class HCMessageTest extends BaseTest {

    @Author(names = {"Daffa", "Jashan"})
    @Module(name = "Message")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testChatBotFunctionalityLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testChatBotFunctionalityLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open help center on home page (coachmark dismissed inside flow if present)");
        homePage.openHelpCenter();

        step("click chatbot entry point");
        settingsPage.openChatBot();

        Assertion.assertThat("Initial chat is visible", settingsPage.isInsideChatBot(), is(true));
    }

    @Author(names = "Daffa")
    @Module(name = "HelpCenter")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyOtherInfoPageListGuest() {
        Map<String, String> map = getParam("verifyOtherInfoPageListGuest");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as non logged in user");
        loginPage.loginAsGuest();

        step("Open help center on settings page");
        homePage.openHelpCenter();

        step("Verify other info page list");
        List<String> otherInfoTitles = helpCenterPage.getOtherInfoTitleList();
        Assertion.assertThat("Other info page list is not empty", otherInfoTitles.size(), greaterThan(0));

        step("Verify redirection of info page");
        Assertion.assertThat("Redirection of info page is successful", helpCenterPage.verifyRedirectionOfInfoPage(), is(true));
    }

    @Author(names = "Aditi")
    @Module(name = "Message")
    @Tribe(name = "Homepage")
    @Vertical(name = "Platform")
    @Test
    public void testCheckHomePageIconsPresent() {
        Map<String, String> param = getParam("testCheckHomePageIconsPresent");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("Verify if the icons are present on homepage and clickable");
        boolean isVerticalTitleAppeared = homePage.verifyHomeIconTitle();
        Assertion.assertThat("Verify All Verticals Icon Titles are Appeared", isVerticalTitleAppeared, is(true));
    }

    @Author(names = "Jashan")
    @Module(name = "HelpCenter")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyInboxDetailPagePopup() throws FailedLoginException, InterruptedException {

        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyInboxDetailPagePopup");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String accountId = param.get("accountId");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        step("Post new inbox");
        helpCenterPage.postNewInbox(accountId);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User click inbox entry point");
        homePage.openInbox();

        step("User click inbox first message");
        helpCenterPage.clickInboxFirstMessage();

        step("Verify Popup");
        Assertion.assertThat("Verify Popup", helpCenterPage.verifyPopup(), is(true));
    }

    @Author(names = "Jashan")
    @Module(name = "HelpCenter")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyInboxDetailPageDeeplink() throws FailedLoginException, InterruptedException {

        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyInboxDetailPageDeeplink");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String accountIdString = param.get("accountId");
        long accountId = Long.parseLong(accountIdString);
        String subtitle = param.get("subtitle");
        String url = param.get("url");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        step("Post new inbox");
        helpCenterPage.postNewVerticalInbox(accountId, subtitle);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User click inbox entry point");
        homePage.openInbox();

        step("User click inbox first message");
        helpCenterPage.clickInboxFirstMessage();

        step("Verify redirection to Help Center page");
        Assertion.assertThat("Redirection to Help Center page is successful", helpCenterPage.redirectionToHelpCenterPage(url), is(true));
    }

    @Author(names = "Rizky")
    @Module(name = "HelpCenter")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyOtherInfoPageListLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyOtherInfoPageListLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open help center from home page");
        homePage.openHelpCenter();

        step("User click to skip on coachmark");
        settingsPage.skipCoachmark();

        step("Verify other info page list");
        List<String> otherInfoTitles = helpCenterPage.getOtherInfoTitleList();
        Assertion.assertThat("Other info page list is not empty", otherInfoTitles.size(), greaterThan(0));

        step("Verify redirection of info page");
        Assertion.assertThat("Redirection of info page is successful", helpCenterPage.verifyRedirectionOfInfoPage(), is(true));
    }

    @Author(names = "Daffa")
    @Module(name = "Message")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testContactUsOnHelpCenter() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testContactUsOnHelpCenter");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPageWeb = HelpCenterPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open help center on home page");
        homePage.openHelpCenter();

        step("Check if channel management section is visible");
        Assertion.assertThat("is Channel Management section visible", helpCenterPageWeb.verifyChannelManagement(), is(true));
    }

    @Author(names = "Daffa")
    @Module(name = "Message")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testLoginViaHelpCenterLandingPage() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testLoginViaHelpCenterLandingPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged out user");
        loginPage.loginAsGuest();

        step("Open help center on home page");
        homePage.openHelpCenter();

        step("Click login widget");
        helpCenterPage.clickLogin();

        step("Verify if user is redirected to login page");
        loginPage.loginSteps(email, password, otp, 1);

        step("Verify in help center in logged in condition");
        Assertion.assertThat("User is logged in", helpCenterPage.isLoggedInCondition(), is(true));
    }

    @Author(names = "Jashan")
    @Module(name = "HelpCenter")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyLoginStatusHelpCenter() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyLoginStatusHelpCenter");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String firstName = param.get("firstName");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open help center on home page");
        homePage.openHelpCenter();

        step("click chatbot entry point");
        settingsPage.openChatBot();

        Assertion.assertThat("Initial chat is visible", settingsPage.initialChatVisible(firstName), is(true));

        step("Check that the New Orders section is present");
        Assertion.assertThat("List of orders is present", helpCenterPage.verifyNewOrderSectionPresent(), is(true));
    }

    @Author(names = "Daffa")
    @Module(name = "Chat Platform")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void checkChatPlatformListPage() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkChatPlatformListPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User click inbox entry point");
        homePage.openInbox();

        step("User click chat platform list");
        helpCenterPage.clickChatPlatform();

        step("Verify active chat platform list element");
        Assertion.assertThat("Verify active chat platform list element", helpCenterPage.verifyActiveChatPlatform(), is(true));
    }

    @Author(names = "Daffa")
    @Module(name = "Chat Platform")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyLegalBannerChatListPage() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyLegalBannerChatListPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User click inbox entry point");
        homePage.openInbox();

        step("User click chat platform list");
        helpCenterPage.clickChatPlatform();

        step("Verify legal banner chat list page");
        Assertion.assertThat("Verify legal banner chat list page", helpCenterPage.verifyLegalBannerChatPlatform(), is(true));
    }

    @Author(names = "Benny, Daffa")
    @Module(name = "HelpCenter")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testCsTicketTrackerPage() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCsTicketTrackerPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open help center on home page");
        homePage.openHelpCenter();

        step("User click Skip on coachmark");
        helpCenterPage.clickSkipCoachmark();

        Assertion.assertThat("Help Center should remain opened after dismiss the coachmark", helpCenterPage.isHelpCenterOpened(), is(true));

        step("click see all report list");
        helpCenterPage.clickReportList();

        Assertion.assertThat("Is Report List Page opened", helpCenterPage.isReportListOpened(), is(true));

        step("click see Done status");
        helpCenterPage.clickReportListDone();

        Assertion.assertThat("Is Report List Done Status Page opened", helpCenterPage.isReportListDoneOpened(), is(true));

        step("click back in report list page");
        helpCenterPage.clickReportListBackButton();

        Assertion.assertThat("Is Help center Page opened", helpCenterPage.isHelpCenterOpened(), is(true));
    }

    @Author(names = "Daffa")
    @Module(name = "Chat Platform")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void checkFunctionalityActiveChatRoom() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkFunctionalityActiveChatRoom");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String textField = param.get("textField");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User click inbox entry point");
        homePage.openInbox();

        step("User click chat platform list");
        helpCenterPage.clickChatPlatform();

        step("User click chat platform chat room");
        helpCenterPage.clickChatPlatformActiveChat();

        step("Verify active chat platform window name");
        Assertion.assertThat("Verify active chat platform window name", helpCenterPage.verifyChatPlatformWindowName(), is(true));

        step("Verify active chat platform window ellipsis");
        Assertion.assertThat("Verify active chat platform window ellipsis", helpCenterPage.verifyPlatformWindowEllipsis(), is(true));

        step("Verify chat platform active or freeze");
        if(helpCenterPage.verifyPlatformWindowTextField()){
            step("Insert text chat platform in text field");
            helpCenterPage.checkInsertTextField(textField);
        }else {
            step("Verify chat freeze container is displayed");
            Assertion.assertThat("Verify chat freeze container", helpCenterPage.verifyChatFreezeContainer(), is(true));
        }
    }

    @Author(names = "Rizky")
    @Module(name = "NotificationSetting")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyNotificationSettingTurnOnOff() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyNotificationSettingTurnOnOff");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open settings page");
        homePage.openSettings();

        step("Open notification setting page");
        settingsPage.openNotificationSetting();

        step("Turn off all toggle");
        settingsPage.turnOffAllToggle();

        step("Verify all toggle is unchecked");
        int checkedToggle = settingsPage.getCheckedToggleCount();
        Assertion.assertThat("All toggle is unchecked", checkedToggle, is(0));

        step("Turn on all toggle");
        settingsPage.turnOnAllToggle();

        step("Verify all toggle is checked");
        checkedToggle = settingsPage.getCheckedToggleCount();
        Assertion.assertThat("All toggle is checked", checkedToggle, greaterThan(3));
    }

    @Author(names = "Daffa")
    @Module(name = "Inbox")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyUnreadInboxAndChat() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyUnreadInboxAndChat");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String accountId = param.get("accountId");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Post new inbox and verify");
        helpCenterPage.postNewInbox(accountId);
        homePage.openInbox();
        Assertion.assertThat("Check if chat platform exist", helpCenterPage.isChatPlatformExist(), is(true));

        step("Go back to homepage");
        homePage.goToHomePage();

        step("Verify unread inbox and chat");
        Assertion.assertThat("Verify unread inbox and chat", helpCenterPage.verifyUnreadInboxAndChat(), is(true));
    }

    @Author(names = "Daffa")
    @Module(name = "Chat Platform")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void checkFunctionalityChatFromPartner() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkFunctionalityChatFromPartner");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String messageId = param.get("messageId");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User click inbox entry point");
        homePage.openInbox();

        step("User click chat platform list");
        helpCenterPage.clickChatPlatform();

        step("User click chat platform chat room");
        helpCenterPage.clickChatPlatformActiveChat();

        step("Verify active chat platform window name");
        Assertion.assertThat("Verify active chat platform window name", helpCenterPage.verifyChatPlatformWindowName(), is(true));

        step("Verify active chat platform window ellipsis");
        Assertion.assertThat("Verify active chat platform window ellipsis", helpCenterPage.verifyPlatformWindowEllipsis(), is(true));

        step("Verify active chat from partner");
        Assertion.assertThat("Verify active chat from partner", helpCenterPage.verifyChatFromPartner(messageId), is(true));
    }

    @Author(names = "Benny")
    @Module(name = "Inbox")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyInboxListPage() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyInboxListPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User click inbox entry point");
        homePage.openInbox();

        step("Verify inbox page is opened");
        Assertion.assertThat("Verify inbox page is opened", helpCenterPage.verifyInboxPageIsOpened(), is(true));

        step("Verify inbox page can be scrolled");
        Assertion.assertThat("Verify inbox page can be scrolled", helpCenterPage.verifyInboxPageCanBeScrolled(), is(true));
  }

    @Author(names = "Daffa")
    @Module(name = "Chat Platform")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyChatRoomViaEntryPointOrderDetail () throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyChatRoomViaEntryPointOrderDetail");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String orderId = param.get("orderId");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User go to your order page");
        homePage.goToMyOrderPage();

        step("Open hotel order detail");
        orderListPage.openActiveTicketViaURL(orderId);

        step("Verify chat/call button is showing in order detail");
        orderDetailPage.skipCoachmark();
        orderDetailPage.isCallButtonDisplayed();

        step("User click inbox entry point");
        homePage.openInbox();

        step("User click chat platform list");
        helpCenterPage.clickChatPlatform();

        step("Verify active chat platform list element");
        Assertion.assertThat("Verify active chat platform list element", helpCenterPage.verifyActiveChatPlatform(), is(true));
    }

    @Author(names = "Daffa, Hafiz")
    @Module(name = "Inbox")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyInboxAfterRefund() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyInboxAfterRefund");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String startDate = CoreFunctions.generateRandomDate("7-14");
        String orderFullName = CoreFunctions.randomName() + " autorder";
        String orderNameTitle = CoreFunctions.getRandomObject(salutationList);
        String guestFullName = CoreFunctions.randomName() + " autguest";
        String guestTitle = CoreFunctions.getRandomObject(salutationList);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Book refundable hotel order for prerequisite");
        orderCreationPage<?> orderCreation = new orderCreationPage<>(session);
        orderCreation.createHotelSakukuIssuedOrderApi(email, password, startDate, hotelName, 1, 1, 1,
                orderFullName, orderNameTitle, guestFullName, guestTitle, "IDR");
        int orderId = orderCreation.getOrderId();

        step("Open order detail and initiate refund");
        orderListPage.openActiveTicketViaURL(String.valueOf(orderId));
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();
        refundPage.continueToRefund();

        step("Submit refund");
        refundPage.selectRandomRefundReason();
        refundPage.clickButton("Continue");
        refundPage.waitRefundSecondPageLoaded();
        refundPage.clickChecklistTnc();
        refundPage.clickButton("Submit");
        refundPage.clickButton("Process Refund");
        refundPage.clickConfirmationRefundPopup();
        refundPage.waitForSubmitRefundLoader();
        refundPage.SuccessSubmitRefundPopup();

        step("User click inbox entry point");
        homePage.goToHomePage();
        homePage.openInbox();

        step("Verify inbox page is opened");
        Assertion.assertThat("Verify inbox page is opened", helpCenterPage.verifyInboxPageIsOpened(), is(true));

        step("Verify refund inbox count is greater than 0");
        Assertion.assertThat("Verify refund inbox count is greater than 0", helpCenterPage.verifyRefundInbox(), greaterThan(0));

        step("Click refund inbox and verify redirect to refund page for the created order");
        Assertion.assertThat("Click refund inbox and verify page", helpCenterPage.checkRefundInboxRedirection(),
                containsString("refund"));
    }

    @Author(names = "Jashan")
    @Module(name = "HelpCenter")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyContextualActionButton() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyContextualActionButton");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open help center on home page");
        homePage.openHelpCenter();

        step("Check contextual button");
        Assertion.assertThat("Contextual button showing in HC", helpCenterPage.checkContextualButton(), is(notNullValue()));
    }

    @Author(names = "Ram Pratap Singh")
    @Module(name = "Push Notification")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyPushNotificationAfterPartnerSendChat() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyPushNotificationAfterPartnerSendChat");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);
        InboxPage<?> inboxPage = new InboxPage<>(session);
        
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = CoreFunctions.generateRandomDate(param.get("rangeAddDate"));
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = CoreFunctions.randomName() + " autorder";
        String orderNameTitle = CoreFunctions.getRandomObject(salutationList);
        String guestFullName = CoreFunctions.randomName() + " autguest";
        String guestTitle = CoreFunctions.getRandomObject(salutationList);
        String currency = param.get("currency");
        boolean isRefundable = Boolean.parseBoolean(param.get("isRefundable"));
        String partnerReplyMessageId = param.get("partnerReplyMessageId");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Create hotel/homes order via API");
        HotelApi<?> hotelApi = new HotelApi<>(session);
        boolean orderCreated = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, isRefundable,
                false, "", "en", false
        );
        Assertion.assertThat("Hotel order successfully created", orderCreated, is(true));
        String orderId = String.valueOf(hotelApi.getOrderId());

        step("Navigate to order detail and verify order is issued");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("E-ticket card is displayed", orderDetailPage.isETicketCardDisplayed(), is(true));

        step("Open inbox to verify push notification received from chat platform");
        homePage.goToHomePage();
        homePage.openInbox();
        
        step("Verify push notification content from partner chat");
        String notificationTitle = inboxPage.getInboxTitle(0);
        Assertion.assertThat("Push notification is related to chat/message", 
                inboxPage.isChatRelatedNotification(notificationTitle, hotelName), is(true));

        step("Click push notification to verify redirect to chat platform");
        inboxPage.clickInbox(0);
        
        step("Verify user is redirected to chat platform");
        helpCenterPage.clickChatPlatform();
        Assertion.assertThat("Chat platform is active", helpCenterPage.verifyActiveChatPlatform(), is(true));

        step("Open active chat room and verify partner reply message");
        helpCenterPage.clickChatPlatformActiveChat();
        Assertion.assertThat("Chat window name is displayed", 
                helpCenterPage.verifyChatPlatformWindowName(), is(true));
        
        step("Verify partner reply message is visible in chat");
        Assertion.assertThat("Partner reply message is visible in chat room", 
                helpCenterPage.verifyChatFromPartner(partnerReplyMessageId), is(true));

        step("Verify chat window functionality");
        Assertion.assertThat("Chat text field is functional", 
                helpCenterPage.verifyPlatformWindowTextField(), is(true));
        Assertion.assertThat("Chat ellipsis menu is available", 
                helpCenterPage.verifyPlatformWindowEllipsis(), is(true));
    }

    @Author(names = "Daffa")
    @Module(name = "Message")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testLoginViaArticleDislike() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testLoginViaArticleDislike");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPage<?> settingsPage = new SettingsPage<>(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginAsGuest();

        step("Open help center on home page");
        homePage.openHelpCenter();

        step("click on any popular article randomly");
        String selectedArticle = helpCenterPage.selectRandomArticle();

        step("click dislike article");
        helpCenterPage.clickDislikeArticle();

        step("Verify if user is redirected to login page");
        loginPage.loginSteps(email, password, otp, 1);

        step("verify if the article is opened");
        Assertion.assertThat("is Popular article section visible", settingsPage.checkSelectedPopularArticle(selectedArticle), is(true));
    }

    @Author(names = "Daffa")
    @Module(name = "NotificationSetting")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyNotificationSettingPageAndBackButton() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyNotificationSettingPageAndBackButton");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPage<?> settingsPage = new SettingsPage<>(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");
        int toggleCount = Integer.parseInt(map.get("toggleCount"));

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        String lang = homePage.getPageLanguageCode();

        step("Open settings page");
        homePage.openSettings();

        step("Open notification setting page");
        settingsPage.openNotificationSetting();

        step("Verify all toggle exist");
        Assertion.assertThat("All toggle exist", settingsPage.verifyAllToggleExist(), is(true));

        step("Verify all toggle is checked");
        int checkedToggle = settingsPage.getCheckedToggleCount();
        Assertion.assertThat("All toggle is checked", checkedToggle, is(toggleCount));

        step("Verify wording of all setting");
        String[] expectedWording = { "Account Activity", "Exclusive Info", "Reminders", "Blibli Tiket Rewards Info" };
        if (lang.equals("id")) {
            expectedWording = new String[] { "Aktivitas Akun", "Info Eksklusif", "Pengingat", "Info Blibli Tiket Rewards" };
        }
        List<String> expectedWordingList = new ArrayList<>(Arrays.asList(expectedWording));
        List<String> notificationSettingWordings = settingsPage.getNotificationSettingWordings();
        Assertion.assertThat("Wording of setting is correct", notificationSettingWordings, is(expectedWordingList));

        step("Click back button");
        settingsPage.clickBackButtonNotificationSettings();

        step("Verify if user is redirected to settings page");
        Assertion.assertThat("User is redirected to settings page", settingsPage.isInSettingsPage(), is(true));
    }

    @Author(names = "Daffa")
    @Module(name = "Message")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyLoginStatusFromHelpCenter() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyLoginStatusFromHelpCenter");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();
        homePage.waitForHomePageToOpen();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open help center on home page");
        homePage.openHelpCenter();

        step("User click to skip on coachmark");
        settingsPage.skipCoachmark();

        step("Check that the New Orders section is present");
        Assertion.assertThat("List of orders is present", helpCenterPage.verifyNewOrderSectionPresent(), is(true));

        step("click chatbot entry point");
        settingsPage.openChatBot();

        Assertion.assertThat("Initial chat is visible", settingsPage.isInsideChatBot(), is(true));
    }

    @Author(names = "Daffa")
    @Module(name = "Message")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testContactUsOnHelpCenterGuest() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testContactUsOnHelpCenterGuest");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HelpCenterPageWeb<?> helpCenterPageWeb = HelpCenterPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Logout via URL");
        loginPage.logoutViaUrl();

        step("Open help center on home page");
        homePage.openHelpCenter();

        step("Check if channel management section is visible");
        Assertion.assertThat("is Channel Management section visible", helpCenterPageWeb.verifyChannelManagement(), is(true));
    }

    @Author(names = "Daffa")
    @Module(name = "Message")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testLoginViaArticleLike() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testLoginViaArticleLike");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPage<?> settingsPage = new SettingsPage<>(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginAsGuest();

        step("Open help center on home page");
        homePage.openHelpCenter();

        step("click on any popular article randomly");
        String selectedArticle = helpCenterPage.selectRandomArticle();

        step("click like article");
        helpCenterPage.clickLikeArticle();

        step("Verify if user is redirected to login page");
        loginPage.loginSteps(email, password, otp, 1);

        step("verify if the article is opened");
        Assertion.assertThat("is Popular article section visible", settingsPage.checkSelectedPopularArticle(selectedArticle), is(true));
    }

    @Author(names = "Daffa")
    @Module(name = "Message")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testLoginViaArticleDynamicForm() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testLoginViaArticleDynamicForm");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPage<?> settingsPage = new SettingsPage<>(session);
        HelpCenterPageWeb<?> helpCenterPage = HelpCenterPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginAsGuest();

        step("Open help center on home page");
        homePage.openHelpCenter();

        step("click on any popular article randomly");
        String selectedArticle = helpCenterPage.selectRandomArticle();

        step("Go to send message tab");
        settingsPage.clickSendMessage();

        step("Verify if user is redirected to login page");
        loginPage.loginSteps(email, password, otp, 1);

        step("verify if the article is opened");
        Assertion.assertThat("is Popular article section visible", settingsPage.checkSelectedPopularArticle(selectedArticle), is(true));
    }
}