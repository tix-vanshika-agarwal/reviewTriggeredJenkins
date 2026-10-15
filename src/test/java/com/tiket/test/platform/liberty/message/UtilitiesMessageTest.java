package com.tiket.test.platform.liberty.message;

import com.tiket.api.utilities.NoDeeplinkInboxApi;
import com.tiket.apiPage.*;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.page.account.AccountPage;
import com.tiket.page.cartransfer.CarTransferPage;
import com.tiket.page.cartransfer.searchpage.CarTransferSearchPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.refund.RefundPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.utilities.message.chat.ChatBoxPage;
import com.tiket.page.utilities.message.help_center.HelpCenterPage;
import com.tiket.page.utilities.message.inbox.InboxPage;
import com.tiket.page.utilities.message.inbox.InboxPage.HotelDataSummary;
import com.tiket.pojo.order.HotelInternalOrderDetail;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.accommodation.hotels.reschedulePage.HotelReschedulePageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class UtilitiesMessageTest extends BaseTest {
    @Author(names = {"Dendi"})
    @Module(name = "InboxDetail")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testInboxDetailPageNoDeeplink() throws InterruptedException, ExecutionException{
        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        var memberApi = new MemberApi<>(session);

        Map<String, String> testProperties = getParam("testInboxDetailPageNoDeeplink");
        BaseTest.paramsThreadLocal.set(testProperties);

        String loginType = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String accountID = testProperties.get("accountID");
        String message = testProperties.get("message");

        step("Hit noDeeplink inbox api");
        int statusCode = memberApi.createNoDeeplinkMsg(accountID, message);
        log("Status code for creating noDeeplinkMsg: " + statusCode);

        step("User goto home");
        homePage.gotoHome();

        step("User login into their account having inbox message with no deeplink");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("user open inbox page");
        homePage.clickInboxIcon();

        step("inbox list will be open");
        Assertion.assertThat("verify inbox list open", homePage.isInboxListOpen(), is(true));

        step("click inbox that has no deeplink");
        homePage.clickInboxMessage(message);

        step("Redirect to the inbox detail page");
        Assertion.assertThat("Check if redirect to inbox detail page",homePage.isInboxDetailHasSubtitle(),is(true) );
    }

    @Author(names = {"Dendi","Jashan"})
    @Module(name = "InboxDetail")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testInboxDetailPageDeeplink(){
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        Map<String, String> testProperties = getParam("testInboxDetailPageDeeplink");
        BaseTest.paramsThreadLocal.set(testProperties);

        String username = testProperties.get("username");
        String password = testProperties.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Get langguage app");
        String lang = homePage.getLanguageApp();
        String message = lang.equalsIgnoreCase("id") ?  testProperties.get("messageId") : testProperties.get("messageEn");

        step("user open inbox page");
        homePage.clickInboxIcon();

        step("inbox list will be open");
        Assertion.assertThat("verify inbox list open", homePage.isInboxListOpen(), is(true));

        step("click inbox that has deeplink");
        homePage.clickInboxMessage(message);

        step("Redirect to the webview page");
        Assertion.assertThat("Check if redirect to webview page with url " + message,
                homePage.isOnWebviewPage(message), is(true));
    }

    @Author(names = {"Akbar"})
    @Vertical(name = "Platform")
    @Tribe(name = "Message")
    @Module(name = "HelpCenter")
    @Test
    public void verifyOtherInfoPageListOnHelpCenterNonLogin(){
        Map<String, String> param = getParam("verifyOtherInfoPageListOnHelpCenterNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);

        step("User goto home");
        homePage.gotoHome();

        step("User open app as a guest user");
        loginPage.loginAsGuest();
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
        }

        step("Go to Help Center");
        homePage.openAccount();
        accountPage.openHelpCenterPage();

        step("Verify Step");
        Assertion.assertThat("Redirected to help center page", helpCenterPage.isHelpCenterOpen(), is(true));
        Assertion.assertThat("Other Info/Info Page List is displayed and redirected to the correct page",
                helpCenterPage.verifyHelpCenterOtherInformation(), is(true));
    }

    @Author(names = {"Akbar"})
    @Vertical(name = "Platform")
    @Tribe(name = "Message")
    @Module(name = "HelpCenter")
    @Test
    public void verifyOtherInfoPageListOnHelpCenterLogin(){
        Map<String, String> param = getParam("verifyOtherInfoPageListOnHelpCenterLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as a login user");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("en")) {
            step("Change Language to 'ENG'");
            homePage.changeLanguageUsingDeeplink("ENG");
        }

        step("Go to Help Center");
        homePage.openAccount();
        accountPage.openHelpCenterPage();

        step("Verify Step");
        Assertion.assertThat("Redirected to help center page", helpCenterPage.isHelpCenterOpen(), is(true));
        Assertion.assertThat("Other Info/Info Page List is displayed and redirected to the correct page",
                helpCenterPage.verifyHelpCenterOtherInformation(), is(true));
    }

    @Author(names = {"Akbar"})
    @Vertical(name = "Platform")
    @Tribe(name = "Message")
    @Module(name = "HelpCenter")
    @Test
    public void verifyLoginViaEntryPointOnHelpCenter(){
        Map<String, String> param = getParam("verifyLoginViaEntryPointOnHelpCenter");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as a guest user");
        loginPage.loginAsGuest();

        step("Go to Help Center");
        homePage.openAccount();
        accountPage.openHelpCenterPage();
        helpCenterPage.isHelpCenterOpen();

        step("Click log in banner and login using credential");
        helpCenterPage.clickLoginBanner();
        loginPage.loginWith(loginType,username,password);

        step("Get the user first name");
        helpCenterPage.skipCoachmark();
        helpCenterPage.clickBack();
        String firstName = accountPage.getAccountName().split(" ")[0];
        accountPage.openHelpCenterPage();

        step("Verify Help Center Page is on Logged in state");
        Assertion.assertThat("User name should be displayed", helpCenterPage.isUserAccountNameDisplayedCorrectly(firstName), is(true));
        Assertion.assertThat("Log in banner should disappeared", helpCenterPage.isLoginBannerDisappeared(), is(true));
        Assertion.assertThat("Travis icon should be displayed", helpCenterPage.isTravisIconDisplayed(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "verifyLoginViaEntryPointOnArticleDislikeHelpCenter")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test(dataProvider="testVerifyLoginViaEntryPointOnArticleDislikeHelpCenterDataProvider")
    public void testVerifyLoginViaEntryPointOnArticleDislikeHelpCenter(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Go to home page");
        homePage.gotoHome();

        step("Login in using credentials");
        loginPage.loginAsGuest();

        step("Open account");
        homePage.openAccount();

        step("Open help center");
        accountPage.openHelpCenterPage();

        step("click and get popular article");
        helpCenterPage.clickPopularArticle();

        step("click dislike article icon");
        helpCenterPage.clickDislikeBtnPopularArticle();

        step("do the login");
        loginPage.loginFromDifferentPages(username,password);

        step("is directed back to correct popular article page");
        Assertion.assertThat("is on correct popular article page",helpCenterPage.isOnClickedPopularArticle(),is(true));
    }

    @DataProvider(name="testVerifyLoginViaEntryPointOnArticleDislikeHelpCenterDataProvider")
    public Object[][] testVerifyLoginViaEntryPointOnArticleDislikeHelpCenterDataProvider(){
        return getObjectParams("testVerifyLoginViaEntryPointOnArticleDislikeHelpCenter");
    }

    @Author(names = {"Ashish"})
    @Module(name = "verifyLoginViaEntryPointOnArticleLikeHelpCenter")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test(dataProvider="testVerifyLoginViaEntryPointOnArticleLikeHelpCenterDataProvider")
    public void testVerifyLoginViaEntryPointOnArticleLikeHelpCenter(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Go to home page");
        homePage.gotoHome();

        step("Login in using credentials");
        loginPage.loginAsGuest();

        step("Open account");
        homePage.openAccount();

        step("Open help center");
        accountPage.openHelpCenterPage();

        step("click and get popular article");
        helpCenterPage.clickPopularArticle();

        step("click like article icon");
        helpCenterPage.clickLikeBtnPopularArticle();

        step("do the login");
        loginPage.loginFromDifferentPages(username,password);

        step("is directed back to correct popular article page");
        Assertion.assertThat("is on correct popular article page",helpCenterPage.isOnClickedPopularArticle(),is(true));
    }

    @DataProvider(name="testVerifyLoginViaEntryPointOnArticleLikeHelpCenterDataProvider")
    public Object[][] testVerifyLoginViaEntryPointOnArticleLikeHelpCenterDataProvider(){
        return getObjectParams("testVerifyLoginViaEntryPointOnArticleLikeHelpCenter");
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Platform")
    @Tribe(name = "Message")
    @Module(name = "HelpCenter")
    @Test
    public void testArticleRelatedOrder() throws ExecutionException, InterruptedException{
        Map<String, String> param = getParam("testArticleRelatedOrder");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);
        CarTransferPage<WebDriver> carTransferPage= CarTransferPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);
        CarTransferApi<?> carTransferApi = new CarTransferApi<>(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String origin = param.get("origin");
        String destination = param.get("destination");
        String type = param.get("type");
        String pickupTime = param.get("pickupTime");
        int pax = Integer.parseInt(param.get("pax"));
        String rangeAddDate = param.get("rangeAddDate");

        String cardNumber = param.get("cardNumber");
        String cardHolderName = param.get("cardName");
        String expiry = param.get("expiry");
        String[] expirySplit = expiry.split("/");
        String cardExpiredMonth = expirySplit[0];
        String cardExpiredYear = expirySplit[1];
        String cardCvvNumber = param.get("cvv");
        String adminName = param.get("adminName");
        String adminPhone = param.get("adminPhone");

        step("Get api access token");
        String apiAccessToken = unm.getAccessToken(username,password,"B2C");

        step("check order present in order");
        MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrderByVertical("train", apiAccessToken);
        String bookedOrderID = "";
        if(activeOrderData.isEmpty || activeOrderData.train.isEmpty()){
            step("Create new car transfer issued order");
            bookedOrderID  = carTransferApi.createIssuedOrderApi(username, password, origin,destination,type,rangeAddDate,pickupTime,pax,cardNumber,cardHolderName,cardExpiredMonth,cardExpiredYear,cardCvvNumber,adminName,adminPhone);
            Assertion.assertThat("Car transfer booking via API is successful", bookedOrderID, not(blankOrNullString()));
        }else{
            bookedOrderID = activeOrderData.train.get(0).orderId;
        }

        step("User goto home");
        homePage.gotoHome();

        step("User login into their account");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Go to Help Center");
        homePage.openAccount();
        accountPage.openHelpCenterPage();

        step("Verify related article to order");
        Assertion.assertThat("Article title about order is the same",helpCenterPage.verifyRelatedArticle(bookedOrderID),is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "LegalBannerInInboxChat")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testLegalBannerInInboxChat(){
        Map<String, String> param = getParam("testLegalBannerInInboxChat");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        InboxPage<WebDriver> inboxPage = InboxPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Go to home page");
        homePage.gotoHome();

        step("Login in using credentials");
        loginPage.loginWith(loginType,username,password);

        step("open inbox");
        homePage.gotoHome();
        homePage.clickInboxIcon();

        step("click inbox chat section");
        inboxPage.openInboxChatSection();
        Assertion.assertThat("Verify legal banner present", inboxPage.verifyLegalBannerAppear(), is(true));

        step("click legal banner");
        inboxPage.clickLegalBanner();

        step("Verify legal banner");
        Assertion.assertThat("Verify legal popup present", inboxPage.verifyLegalBannerBottomSheetAppear(), is(true));

    }

    @Author(names = {"Dendi"})
    @Module(name = "HelpCenter")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testHelpCenterPopularArticle() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testHelpCenterPopularArticle");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginType = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String expectedUrlOfPopularArticle = testProperties.get("expectedPopularArticleLink");
        String popularArticleTitleId = testProperties.get("popularArticleTitleId");
        String popularArticleTitleEn = testProperties.get("popularArticleTitleEn");


        step("Go to home page");
        homePage.gotoHome();

        step("Login in using credentials");
        loginPage.loginWith(loginType, username, password);

        step("Open Account");
        homePage.openAccount();
            
        step("get current app langguage");
        String appLang = homePage.getLanguageApp();

        step("open help center");
        accountPage.openHelpCenterPage();

        step("Click popular article");
        helpCenterPage.clickPopularArticle(popularArticleTitleId,popularArticleTitleEn);

        step("Verify popular article is open");
        Assertion.assertThat("Verify popular article title correct", helpCenterPage.getPopularArticlePagTitle(),appLang.equalsIgnoreCase("En") ? equalTo(popularArticleTitleEn) : equalTo(popularArticleTitleId));
        Assertion.assertThat("Verify popular article url", helpCenterPage.getOpenedPopularArticleUrl(), containsString(expectedUrlOfPopularArticle));
    }

    @Author(names = {"Ashish"})
    @Module(name = "VerifyContactUsPageOnHelpCenter")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test(dataProvider="testVerifyContactUsPageOnHelpCenterLoginDataProvider")
    public void testVerifyContactUsPageOnHelpCenterLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Go to home page");
        homePage.gotoHome();

        step("Login in using credentials");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType,username,password);

        step("Open account");
        homePage.openAccount();

        step("Open help center");
        accountPage.openHelpCenterPage();

        step("click and get popular article");
        helpCenterPage.clickPopularArticle();

        step("click Contact Customer Care");
        helpCenterPage.clickContactCustomerCare();

        step("is customer care open");
        Assertion.assertThat("is Contact Customer Care options displayed correctly",helpCenterPage.isAllContactCustomerCareOptionPresent(),is(true));
    }

    @DataProvider(name="testVerifyContactUsPageOnHelpCenterLoginDataProvider")
    public Object[][] testVerifyContactUsPageOnHelpCenterLoginDataProvider(){
        return getObjectParams("testVerifyContactUsPageOnHelpCenterLogin");
    }

    @Author(names = {"Ashish"})
    @Module(name = "VerifyContactUsPageOnHelpCenter")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test(dataProvider="testVerifyContactUsPageOnHelpCenterGuestDataProvider")
    public void testVerifyContactUsPageOnHelpCenterGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login in using credentials");
        loginPage.loginAsGuest();

        step("Open account");
        homePage.openAccount();

        step("Open help center");
        accountPage.openHelpCenterPage();

        step("click and get popular article");
        helpCenterPage.clickPopularArticle();

        step("click Contact Customer Care");
        helpCenterPage.clickContactCustomerCare();

        step("is directed back to correct popular article page");
        Assertion.assertThat("is Contact Customer Care options displayed correctly",helpCenterPage.isAllContactCustomerCareOptionPresent(),is(true));
    }

    @DataProvider(name="testVerifyContactUsPageOnHelpCenterGuestDataProvider")
    public Object[][] testVerifyContactUsPageOnHelpCenterGuestDataProvider(){
        return getObjectParams("testVerifyContactUsPageOnHelpCenterGuest");
    }

    @Author(names = {"Dendi"})
    @Module(name = "HelpCenter")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testHelpCenterDynamicForm() throws InterruptedException{
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testHelpCenterDynamicForm");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        CarTransferPage<WebDriver> carTransferPage= CarTransferPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);
        CarTransferApi<?> carTransferApi = new CarTransferApi<>(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String origin = map.get("origin");
        String destination = map.get("destination");
        String type = map.get("type");
        String pickupTime = map.get("pickupTime");
        int pax = Integer.parseInt(map.get("pax"));
        String rangeAddDate = map.get("rangeAddDate");

        String cardNumber = map.get("cardNumber");
        String cardHolderName = map.get("cardHolderName");
        String cardExpiredMonth = map.get("cardExpiredMonth");
        String cardExpiredYear = map.get("cardExpiredYear");
        String cardCvvNumber = map.get("cardCvvNumber");
        String adminName = map.get("adminName");
        String adminPhone = map.get("adminPhone");

        step("user open app");
        homePage.gotoHome();

        step("Create new car transfer issued order");
        String bookedOrderID  = carTransferApi.createIssuedOrderApi(username, password, origin,destination,type,rangeAddDate,pickupTime,pax,cardNumber,cardHolderName,cardExpiredMonth,cardExpiredYear,cardCvvNumber,adminName,adminPhone);
        Assertion.assertThat("Car transfer booking via API is successful", bookedOrderID, not(blankOrNullString()));
        int orderId = Integer.parseInt(bookedOrderID);

        step("User login into their account");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Go to Help Center");
        homePage.openAccount();
        accountPage.openHelpCenterPage();

        step("open popular article");
        helpCenterPage.clickPopularArticleAndGetTitle();

        step("Click send question section");
        helpCenterPage.clickSendQuestionFromArticle();
        
        step("verify report complaints");
        Assertion.assertThat("Verify product and question type field present",  helpCenterPage.verifyReportComplaintField(), is(true));
       
        step("fill complaint field");
        helpCenterPage.fillReportComplaint(String.valueOf(orderId));

        step("verify complaint submited");
        Assertion.assertThat("Verify complaint submited", helpCenterPage.isRequestSubmitted(), is(true));

    }

    @Author(names = {"Ashish"})
    @Module(name = "ChatRoom")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test(dataProvider="testVerifyChatRoomViaEntryPointOrderDetailDataProvider")
    public void testVerifyChatRoomViaEntryPointOrderDetail(Map<String, String> param) throws InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        ChatBoxPage<WebDriver> chatBoxPage = ChatBoxPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String rangeDate = param.get("rangeAddDate");
        String startDate = generateRandomDate(rangeDate);
        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = param.get("currency");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get api access token");
        String apiAccessToken = unm.getAccessToken(username,password,"B2C");

        step("check order present in order");
        MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrderByVertical("hotel", apiAccessToken);
        activeOrderData = orderListPage.filterHotelOrderWithChat(activeOrderData, apiAccessToken, username);
        String bookedOrderID = "";
        if(activeOrderData.isEmpty || activeOrderData.hotel.size()==0){
            step("Create new hotel issued order");
            Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
            Assertion.assertThat("Hotel Booking via API is successful", result, is(notNullValue()));
            bookedOrderID = String.valueOf(hotelApi.getOrderId());
        }else{
            bookedOrderID = activeOrderData.hotel.get(0).orderId;
        }

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);

        step("click chat btn");
        orderDetailPage.clickChatBtn();

        step("verify chat page");
        Assertion.assertThat("is chat box correctly displayed",chatBoxPage.verifyChatBox(query, bookedOrderID),is(true));
    }

    @DataProvider(name="testVerifyChatRoomViaEntryPointOrderDetailDataProvider")
    public Object[][] testVerifyChatRoomViaEntryPointOrderDetailDataProvider(){
        return getObjectParams("testVerifyChatRoomViaEntryPointOrderDetail");
    }

    @Author(names = {"Dendi"})
    @Module(name = "Util - Message")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testSummaryOrderBoxInChat() throws InterruptedException, ExecutionException{
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testSummaryOrderBoxInChat");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        InboxPage<WebDriver> inboxPage = InboxPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderDetailPage<WebDriver> orderDetail = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String query = map.get("hotelName");
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String currency = map.get("currency");
        String cardName = map.get("cardName");
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(startDate, night);

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];
        step("user open app");
        homePage.gotoHome();

        step("get lanngguage");
        String appLang = homePage.getLanguageApp();

        step("User create hotel order with special request");
         String acsUrl= hotelApi.createIssuedOrderWithSpecialRequestApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,currency,cardNumber,expiry,cvv,cardName);
        String specialRequest = hotelApi.getOrderSpecialRequest();
        int orderId = hotelApi.getOrderId();
        String hotelName = query;

        step("open acs url deeplink");
        paymentPage.openUrl(acsUrl);

        step("get exepcted data from internal order detail");
        String orderIdString = String.valueOf(orderId);
        HotelInternalOrderDetail hotelInternalOrderDetail = orderDetail.getHotelInternalOrderData(String.valueOf(orderId));
        String hotelIternaryNumber = orderDetail.getHotelItineraryFromInternalOrder(hotelInternalOrderDetail,orderIdString);
        String hotelTimeZone = orderDetail.getHotelTimeZoneFromInternalOrder(hotelInternalOrderDetail,orderIdString);

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("user open inbox page");
        homePage.clickInboxIcon();

        step("user open chat");
        inboxPage.openInboxChatSection();

        step("user open summary order chat");
        inboxPage.openSummaryOrderByTitle(hotelName);

        step("verify order summary");
        HotelDataSummary hotelData = new HotelDataSummary(String.valueOf(orderId), hotelIternaryNumber, startDate, checkOutDate,hotelTimeZone,specialRequest,appLang);
        Assertion.assertThat("Verify hotel summary",  inboxPage.verifyHotelOrderSummaryData(hotelData), is(true));
    }
    
    @Author(names = {"Dendi", "Prerna"})
    @Module(name = "Util - Message")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyUnreadCount() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testVerifyUnreadCount");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        InboxPage<WebDriver> inboxPage = InboxPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Goto home");
        homePage.gotoHome();

        step("Hit API to get new inbox");
        String inboxIdentifier = "automated inbox " + CoreFunctions.getCurrentDateTime();
        inboxPage.postNewInbox(username, password, inboxIdentifier);

        String lang = homePage.getLanguageApp();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        
        step("Hitting API to get unread count");
        Map<String,Integer> unreadCount = homePage.getUnreadCount(lang, "IDR", username, password);

        step("Verify count in home page is correct");
        Assertion.assertThat("Verify count in homepage is correct", homePage.verifyUnreadCount(unreadCount), is(true));

        step("Verify unread count in inbox page");
        homePage.clickInboxIcon();
        Assertion.assertThat("Verify count in inbox page is correct", inboxPage.verifyUnreadCount(unreadCount), is(true));
        
        step("clicking Created Notification");
        inboxPage.clickNewInboxAndBack(inboxIdentifier);

        step("Hitting API to get updated unread count");
        Map<String,Integer> unreadCountUpdated = homePage.getUnreadCount(lang, "IDR", username, password);

        Assertion.assertThat("Verify unread count decreased after opening inbox", inboxPage.verifyUnreadCountDecreased(unreadCountUpdated, unreadCount), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "Util - Message")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testCheckChatPlatformListPage() throws InterruptedException, ExecutionException{
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCheckChatPlatformListPage");

        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        InboxPage<WebDriver> inboxPage = InboxPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String query = map.get("hotelName");
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String currency = map.get("currency");
        String cardName = map.get("cardName");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("User create hotel order with special request");
        String acsUrl= hotelApi.createIssuedOrderWithSpecialRequestApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,currency,cardNumber,expiry,cvv,cardName);
        String hotelName = query;

        step("user open app");
        homePage.gotoHome();
        
        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("open acs url deeplink");
        paymentPage.openUrl(acsUrl);

        step("user open inbox page");
        carTransferSearchPage.allowLocationAccess();
        homePage.clickInboxIcon();

        step("user open chat");
        inboxPage.openInboxChatSection();

        step("user open summary order chat");
        Assertion.assertThat("Verify order summary present", inboxPage.checkOrderSummaryByTitle(hotelName), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "Util - Message")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testCheckChatFunctionality() throws InterruptedException, ExecutionException{
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCheckChatFunctionality");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        InboxPage<WebDriver> inboxPage = InboxPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String query = map.get("hotelName");
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String currency = map.get("currency");
        String cardName = map.get("cardName");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];
        int longPressDuration = 3;
        String hotelName = query;
        String message =  "Test chat hotel " + hotelName + " " + CoreFunctions.randomSalutation() + " " + CoreFunctions.randomName();
        String replyMessage =  "Test reply my own chat " + hotelName + " " + CoreFunctions.randomSalutation() + " " + CoreFunctions.randomName();

        step("user open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("User create hotel order with special request");
        String acsUrl= hotelApi.createIssuedOrderWithSpecialRequestApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,currency,cardNumber,expiry,cvv,cardName);
        
        step("open acs url deeplink");
        paymentPage.openUrl(acsUrl);

        step("user open inbox page");
        carTransferSearchPage.allowLocationAccess();
        homePage.clickInboxIcon();

        step("user open chat");
        inboxPage.openInboxChatSection();

        step("user open summary order chat");
        inboxPage.openSummaryOrderByTitle(hotelName);
        
        step("User check send chat functionality");
        inboxPage.sendChat(message);
        Assertion.assertThat("Verify send message functionality",inboxPage.checkMessageSent(message), is(true));

        step("User check reply chat functionality");
        boolean isReplyContainerAppear = inboxPage.replyChatInChatRoom(longPressDuration, replyMessage);
        Assertion.assertThat("Verify reply container appear", isReplyContainerAppear, is(true));

        step("Verify user able to send reply message");
        Assertion.assertThat("Verify send message functionality",inboxPage.cehckReplyMessageSent(replyMessage), is(true));

        step("Verify user able to close three dot elipsis btm sheet");
        boolean isBtmSheetClosed = inboxPage.closeThreeElipsisBtmSheet();
        Assertion.assertThat("Verify close btm sheet functionality", isBtmSheetClosed, is(true));

        step("Verify three dots button redirecting to help center");
        boolean isHelpCenterPageOpen = inboxPage.openHelpCenterFromChatRoom();
        Assertion.assertThat("Verify help center page is open", isHelpCenterPageOpen, is(true));

        step("Verify back from help center to chat room");
        helpCenterPage.clickBack();
        boolean isOnChatPage = inboxPage.isChatRoomOpen();
        Assertion.assertThat("Verify user back to chat room after clicking back from help center page", isOnChatPage, is(true));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Platform")
    @Tribe(name = "Message")
    @Module(name = "HelpCenter")
    @Test
    public void verifyChatBotEntryPoint(){
        Map<String, String> param = getParam("verifyChatBotEntryPoint");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("User login");
        loginPage.loginWith(loginType, username, password);

        step("Go to Help Center");
        homePage.openAccount();
        String name = accountPage.getAccountName();
        accountPage.openHelpCenterPage();

        step("Open Chat Bot");
        Assertion.assertThat("Chat bot icon appear", helpCenterPage.isTravisIconDisplayed(), is(true));
        Assertion.assertThat("Redirect to chat platform and have chat from bot containing "+name, helpCenterPage.verifyChatBot(name), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Util - Message")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyLoginStatusHelpCenterLandingPageAfterLoginFromMyAccount(){
        Map<String, String> param = getParam("verifyLoginStatusHelpCenterLandingPageAfterLoginFromMyAccount");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("User login");
        loginPage.loginWith(loginType, username, password);

        step("Go to Help Center");
        homePage.openAccount();
        accountPage.openHelpCenterPage();

        step("verify login status");
        Assertion.assertThat("Chat bot icon appear", helpCenterPage.isTravisIconDisplayed(), is(true));
        Assertion.assertThat("order displayed", helpCenterPage.checkListOfOrders(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Util - Message")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyLoginStatusOrderListHelpCenterAfterLoginFromMyAccount(){
        Map<String, String> param = getParam("verifyLoginStatusOrderListHelpCenterAfterLoginFromMyAccount");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("User login");
        loginPage.loginWith(loginType, username, password);

        step("Go to Help Center");
        homePage.openAccount();
        accountPage.openHelpCenterPage();

        step("verify login status");
        Assertion.assertThat("order displayed", helpCenterPage.checkListOfOrders(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "Util - Message")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testCheckInboxListAndPagination() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCheckInboxListAndPagination");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        InboxPage<WebDriver> inboxPage = InboxPage.getInstance(session);

        String username = map.get("username");
        String password = map.get("password");

        step("Goto home");
        homePage.gotoHome();

        if(Configs.ENVIRONMENT != Environment.PROD){
            String lang = homePage.getLanguageApp();
            Map<String, Integer> unreadCount = homePage.getUnreadCount(lang, "IDR", username, password);
            Assertion.assertThat("Verify count in inbox page is more than 10", unreadCount.get("inboxTotalUnreadCount") > 10, is(true));
        }

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Open inbox page");
        homePage.clickInboxIcon();

        step("Hit API to get new inbox");
        String inboxIdentifier = "automated inbox " + CoreFunctions.getCurrentDateTime();
        inboxPage.postNewInbox(username, password, inboxIdentifier);

        step("Wait for new message to arrive");
        sleep(5);

        step("Refresh inbox page");
        inboxPage.refresh();

        step("Verify newly added inbox");
        Assertion.assertThat("Verify newly added inbox exist with identifier: " + inboxIdentifier, inboxPage.isNewInboxAdded(inboxIdentifier), is(true));

        step("Verify scroll and more than 10 inbox being loaded");
        Assertion.assertThat("Verify scroll and more than 10 inbox being loaded", inboxPage.verifyScrollAndTriggerPagination(), is(true));

        step("verify header and navigation bar");
        Assertion.assertThat("is header and navigation bar displayed", inboxPage.checkInboxListHeaderAndNavigationBar(), is(true));
        Assertion.assertThat("is navigation back button working", inboxPage.checkBackButton(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "NotificationSetting")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testToggleNotificationOn() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testToggleNotificationOn");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Go to home page");
        homePage.gotoHome();

        step("Login in using credentials");
        loginPage.loginWith(loginType,username,password);

        step("Open notification setting");
        homePage.openAccount();
        settingPage.openNotificationSettingPage();
        String language = settingPage.getNotificationSettingLanguage();
        boolean isNotifSettingNew = settingPage.isNotificationSettingNewVersion();
        String[] settings = {"Activity","Special For You", "Reminders","Membership"};
        if(isNotifSettingNew){
            settings = new String[]{"Account Activity", "Exclusive Info", "Reminders", "Blibli Tiket Rewards Info"};
        }

        step("Toggle Notification ON for all settings");
        Assertion.assertThat("Toggle is set to ON for all",settingPage.toggleNotifAll(settings,true, language),is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "NotificationSetting")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testToggleNotificationOnOff() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testToggleNotificationOnOff");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Go to home page");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("Open notification setting");
        homePage.openAccount();
        settingPage.openNotificationSettingPage();
        String language = settingPage.getNotificationSettingLanguage();
        String[] settings = new String[]{"Account Activity", "Exclusive Info", "Reminders", "Blibli Tiket Rewards Info"};

        step("Toggle Notification ON for all settings");
        Assertion.assertThat("Toggle is set to ON for all",settingPage.toggleNotifAll(settings,true, language),is(true));

        settingPage.clickBackBtnOnNotificationMenu();
        settingPage.openNotificationSettingPage();

        step("Toggle Notification OFF for all settings");
        language = settingPage.getNotificationSettingLanguage();
        Assertion.assertThat("Toggle is set to OFF for all",settingPage.toggleNotifAll(settings,false, language),is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "NotificationSetting")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyNotificationSettings() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyNotificationSettings");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Go to home page");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("Open notification setting");
        homePage.openAccount();
        settingPage.openNotificationSettingPage();
        String language = settingPage.getNotificationSettingLanguage();
        boolean isNotifSettingNew = settingPage.isNotificationSettingNewVersion();
        String[] settings = {"Activity","Special For You", "Reminders","Membership"};
        if(isNotifSettingNew){
            settings = new String[]{"Account Activity", "Exclusive Info", "Reminders", "Blibli Tiket Rewards Info"};
        }

        step("Verify 2 toggle exist for all settings");
        Assertion.assertThat("All toggle exist for all settings",settingPage.verifyAllToggle(settings, language),is(true));

        step("click back btn");
        settingPage.clickBackBtnOnNotificationMenu();

        step("verify that we are on setting page");
        Assertion.assertThat("is on settings menu",settingPage.isOnSettingPage(),is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Platform")
    @Tribe(name = "Message")
    @Module(name = "HelpCenter")
    @Test
    public void verifyChatBotFunctionality (){
        Map<String, String> param = getParam("verifyChatBotFunctionality");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("User login");
        loginPage.loginWith(loginType, username, password);

        step("Go to Help Center");
        homePage.openAccount();
        String name = accountPage.getAccountName();
        accountPage.openHelpCenterPage();

        step("Open Chat Bot");
        Assertion.assertThat("Chat bot icon appear", helpCenterPage.isTravisIconDisplayed(), is(true));
        Assertion.assertThat("Redirect to chat platform and have chat from bot containing "+name, helpCenterPage.verifyChatBot(name), is(true));
    }


    @Author(names = {"Dendi"})
    @Module(name = "HelpCenter")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testLoginFromDynamicHelpCenter(){
        Map<String, String> param = getParam("testLoginFromDynamicHelpCenter");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("User logout");
        loginPage.loginAsGuest();

        step("Open account");
        homePage.openAccount();

        step("Open help center");
        accountPage.openHelpCenterPage();

        step("Click popular article");
        helpCenterPage.clickPopularArticle();

        step("Get current popular article title");
        String articleTitle = helpCenterPage.getPopularArticlePagTitle();
        
        step("Click ask question (dynamic help center)");
        helpCenterPage.clickSendQuestionFromArticle();

        step("login with credentials");
        loginPage.loginFromDifferentPages(username,password);

        step("Check user back to aritcle page");
        String articleTitleAfterLogin = helpCenterPage.getPopularArticlePagTitle();
        Assertion.assertThat(articleTitle, articleTitleAfterLogin, equalTo(articleTitle));

        step("user go to account page");
        homePage.gotoHome();
        homePage.openAccount();
        String accountName = accountPage.getAccountName();

        step("Verify user is logged in");
        Assertion.assertThat("User is logged in", accountName, notNullValue());
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Platform")
    @Tribe(name = "Message")
    @Module(name = "HelpCenter")
    @Test
    public void verifyContextualButtonHelpCenter(){
        Map<String, String> param = getParam("verifyContextualButtonHelpCenter");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        step("User goto home");
        homePage.gotoHome();

        step("User open app and login");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));
        String bookedOrderID = String.valueOf(hotelApi.getOrderId());
        Assertion.assertThat("Order in issued state", orderListPage.waitForOrderToBeInIssuedState(bookedOrderID,10), is("ETICKET_ISSUED"));

        step("Go to Help Center");
        homePage.openAccount();
        accountPage.openHelpCenterPage();
        Assertion.assertThat("Redirected to help center page", helpCenterPage.isHelpCenterOpen(), is(true));

        step("Click refund button");
        helpCenterPage.clickRefundButton(bookedOrderID);
        Assertion.assertThat("Is refund page open ",refundPage.isInRefundPage(),is(true));
        refundPage.clickBackButton();
        refundPage.confirmBack();

        step("Click reschedule button");
        helpCenterPage.clickRescheduleButton(bookedOrderID);
        helpCenterPage.clickSeeDetailOrderButton();
        
        orderDetailPage.clickReschedule();
        webViewPage.switchToWebViewUrl("reschedule");
        Assertion.assertThat("Is reschedule page open ",hotelReschedulePageWeb.isInReschedulePage(),is(true));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Platform")
    @Tribe(name = "Message")
    @Module(name = "HelpCenter")
    @Test
    public void verifyContextualDetailHelpCenter(){
        Map<String, String> param = getParam("verifyContextualDetailHelpCenter");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        step("User goto home");
        homePage.gotoHome();

        step("User open app and login");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));
        String bookedOrderID = String.valueOf(hotelApi.getOrderId());
        Assertion.assertThat("Order in issued state", orderListPage.waitForOrderToBeInIssuedState(bookedOrderID,10), is("ETICKET_ISSUED"));

        step("Go to Help Center");
        homePage.openAccount();
        accountPage.openHelpCenterPage();
        Assertion.assertThat("Redirected to help center page", helpCenterPage.isHelpCenterOpen(), is(true));

        step("Click complete help for order id "+bookedOrderID);
        helpCenterPage.clickSeeCompleteHelpButton(bookedOrderID);

        step("Check changing order");
        Assertion.assertThat("Can change order from contextual page",helpCenterPage.checkChangeOrderFromContextual(bookedOrderID),not(containsString(bookedOrderID)));

        step("Check article");
        Assertion.assertThat("Can check related article from contextual page",helpCenterPage.checkArticleContextual(),not(blankOrNullString()));

        step("Click refund button");
        helpCenterPage.clickRefundButton(bookedOrderID);
        Assertion.assertThat("Is refund page open ",refundPage.isInRefundPage(),is(true));
        refundPage.clickBackButton();
        refundPage.confirmBack();

        step("Click reschedule button");
        helpCenterPage.clickRescheduleButton(bookedOrderID);
        helpCenterPage.clickSeeDetailOrderButton();
        
        orderDetailPage.clickReschedule();
        webViewPage.switchToWebViewUrl("reschedule");
        Assertion.assertThat("Is reschedule page open ",hotelReschedulePageWeb.isInReschedulePage(),is(true));

        step("Click back");
        hotelReschedulePageWeb.clickBackButton();
        webViewPage.switchToNative();
        orderDetailPage.clickBack();
        helpCenterPage.clickBackPopup();
        helpCenterPage.clickBack();
        Assertion.assertThat("Redirected to help center page", helpCenterPage.isHelpCenterOpen(), is(true));
    }

    @Author(names = {"Areeb"})
    @Vertical(name = "Liberty")
    @Tribe(name = "Message")
    @Module(name = "HelpCenter")
    @Test
    public void verifyReplyPopupAfterChatIsFinished(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyReplyPopupAfterChatIsFinished");
        BaseTest.paramsThreadLocal.set(map);

        String username = map.get("username");
        String password = map.get("password");
        String hotelName = map.get("hotelName");
        int longPressDuration = 3;

        HomePage<?> homePage = HomePage.getInstance(session);
        InboxPage<?> inboxPage = InboxPage.getInstance(session);

        step("user open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("user open inbox page");
        homePage.clickInboxIcon();

        step("user open chat");
        inboxPage.openInboxChatSection();

        step("user open summary order chat");
        inboxPage.openSummaryOrderByTitle(hotelName);

        step("User check reply chat doesn't appear after chat is finished");
        boolean isReplyContainerAppear = inboxPage.isReplyChatOptionAvailable(longPressDuration);
        Assertion.assertThat("Verify reply container doesn't appear", isReplyContainerAppear, is(false));
    }
}
