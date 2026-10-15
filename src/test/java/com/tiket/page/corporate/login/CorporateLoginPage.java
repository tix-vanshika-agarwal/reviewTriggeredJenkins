package com.tiket.page.corporate.login;

import com.google.gson.Gson;
import com.tiket.api.unm.*;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.login.LoginPageIOS;
import com.tiket.pojo.auth.MfaMethodsResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.randomInt;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public abstract class CorporateLoginPage<T extends WebDriver> extends BasePage<T> {

    public CorporateLoginPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CorporateLoginPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new CorporateLoginPageAndroid<>(session);
            }
            case IOS -> {
                return new CorporateLoginPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static Locator LOGIN_BUTTON_FIRST_FORM = new Locator("LOGIN_BUTTON_FIRST_FORM", AppiumBy.xpath("//button[contains(@class,'InitialForm_cta_button')]"));
    private static Locator FORGOT_PASSWORD = new Locator("FORGOT_PASSWORD", AppiumBy.xpath("//div[contains(@class,'LoginPasswordForm_forgot_password')]//span"));
    private static Locator FORGOT_PASSWORD_NEXT = new Locator("FORGOT_PASSWORD_NEXT", AppiumBy.xpath("//div[contains(@class,'ForgotPassword_footer')]//button"));
    private static Locator FORGOT_PASSWORD_INPUT = new Locator("FORGOT_PASSWORD_INPUT", AppiumBy.xpath("//div[contains(@class,'ForgotPassword_content')]//input[@name='password']"));
    private static Locator PASSWORD_VIEW_BUTTON = new Locator("PASSWORD_VIEW_BUTTON", AppiumBy.xpath("//button[contains(@class,'styles_clickable_icon')]"));
    private static final Locator CREATE_ACCOUNT_BUTTON = new Locator("CREATE_ACCOUNT_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Buat Akun') or contains(@text,'Log In') or contains(@text,'Create Account') or contains(@text,'Continue') or contains(@text,'Lanjutkan')) and not(contains(@text,'Payment') or contains(@text,'Pembayaranmu'))]"));
    private static final Locator TIKET_LOGO_LOGIN_PAGE_NATIVE_IOS = new Locator("TIKET_LOGO_LOGIN_PAGE_NATIVE_IOS", AppiumBy.accessibilityId("entity-logo-tiket"));
    
    
    @Deprecated
    public abstract boolean isCorporateLoginPageVisible();
    public abstract String getEmailValue();
    @Deprecated
    public abstract void inputEmail(String email);
    @Deprecated
    public abstract void inputPassword(String password);
    public abstract void clickContinueButton();
    @Deprecated
    public abstract boolean isUnregisteredMessageVisible();
    @Deprecated
    public abstract void forgotPasswordB2B(String username, String password); //TODO fix this

    @SneakyThrows
    public String getAccessTokenB2BCorporate(String username, String password) {
        POSTAuthLoginUNMApi.Inputs authCodeInputs = new POSTAuthLoginUNMApi.Inputs(username,password);
        String authCode = getAuthCode(authCodeInputs);
        logger.log("auth code : " + authCode);

        GETAuthLoginUNMApi.Inputs getServiceTicketInputs = new GETAuthLoginUNMApi.Inputs(authCode);
        String serviceTicket = getServiceTicket(getServiceTicketInputs);
        logger.log("service ticket : " + serviceTicket);

        POSTServiceTicketUNMApi.Inputs accessTokenInputs = new POSTServiceTicketUNMApi.Inputs(serviceTicket);
        String accessToken = getAccessToken(accessTokenInputs,username);
        logger.log("access token : " + accessToken);
        return accessToken;
    }

    @SneakyThrows
    public String getUserDataB2B(String username, String password, String dataRequired) {
        POSTAuthLoginUNMApi.Inputs authCodeInputs = new POSTAuthLoginUNMApi.Inputs(username,password);
        String authCode = getAuthCode(authCodeInputs);
        logger.log("auth code : " + authCode);

        GETAuthLoginUNMApi.Inputs getServiceTicketInputs = new GETAuthLoginUNMApi.Inputs(authCode);
        String serviceTicket = getServiceTicket(getServiceTicketInputs);
        logger.log("service ticket : " + serviceTicket);

        POSTServiceTicketUNMApi.Inputs accessTokenInputs = new POSTServiceTicketUNMApi.Inputs(serviceTicket);
        String data = getDataFromAPI(accessTokenInputs,username, dataRequired);
        logger.log(dataRequired + " : " + data);
        return data;
    }

    private String getDataFromAPI(POSTServiceTicketUNMApi.Inputs inputs,String username, String dataRequired) throws ExecutionException, InterruptedException {
        POSTServiceTicketUNMApi postGenerateAccessTokenB2BApi = new POSTServiceTicketUNMApi(inputs);
        ApiResponse accessTokenResponse = postGenerateAccessTokenB2BApi.hitApi(apiTools);
        JSONObject accessTokenObject = new JSONObject(accessTokenResponse.getBody());
        logger.log(dataRequired + " -> " + accessTokenObject);
        if(accessTokenObject.getJSONObject("data").getBoolean("isB2BCorp")){
            return accessTokenObject.getJSONObject("data").getString(dataRequired);
        }
        else {
            throw new NotImplementedException("the account use " + username + " is not b2b account");
        }
    }

    @SneakyThrows
    public String getAccessTokenB2B(String username,String password){
        POSTAuthLoginUNMApi.Inputs authCodeInputs = new POSTAuthLoginUNMApi.Inputs(username,password);
        String authCode = getAuthCode(authCodeInputs);
        logger.log("auth code : " + authCode);
        return authCode;
    }

    private String getAuthCode(POSTAuthLoginUNMApi.Inputs inputs) throws ExecutionException, InterruptedException {
        POSTAuthLoginUNMApi postGenerateAuthCodeB2BApi = new POSTAuthLoginUNMApi(inputs);
        ApiResponse authCodeResponse = postGenerateAuthCodeB2BApi.hitApi(apiTools);
        JSONObject authCodeObject = new JSONObject(authCodeResponse.getBody());
        logger.log("auth code res -> " + authCodeObject);
        if(authCodeObject.getString("code").contains("MFA_REQUIRED")){
            logger.log("==MFA_REQUIRED==");
            return removeExtraFromUrlToGetAuthCode(removeMFA(authCodeObject.getJSONObject("data").getString("token")));
        }
        String redirectURL = authCodeObject.getJSONObject("data").getString("redirectUrl");
        return removeExtraFromUrlToGetAuthCode(redirectURL);
    }

    private String removeExtraFromUrlToGetAuthCode(String  url){
        String part1 = url.split("authCode=")[1];
        return part1.split("&ref=")[0];
    }

    private String getServiceTicket(GETAuthLoginUNMApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GETAuthLoginUNMApi getServiceTicketB2BApi = new GETAuthLoginUNMApi(inputs);
        ApiResponse serviceTicketResponse = getServiceTicketB2BApi.hitApi(apiTools);
        JSONObject serviceTicketObject = new JSONObject(serviceTicketResponse.getBody());
        logger.log("service ticket res -> " + serviceTicketObject);

        return serviceTicketObject.getJSONObject("data").getString("serviceTicket");
    }

    private String getAccessToken(POSTServiceTicketUNMApi.Inputs inputs,String username) throws ExecutionException, InterruptedException {
        POSTServiceTicketUNMApi postGenerateAccessTokenB2BApi = new POSTServiceTicketUNMApi(inputs);
        ApiResponse accessTokenResponse = postGenerateAccessTokenB2BApi.hitApi(apiTools);
        JSONObject accessTokenObject = new JSONObject(accessTokenResponse.getBody());
        logger.log("access token res -> " + accessTokenObject);
        if(accessTokenObject.getJSONObject("data").getBoolean("isB2BCorp")){
            return accessTokenObject.getJSONObject("data").getString("accessToken");
        }
        else {
            throw new NotImplementedException("the account use " + username + " is not b2b account");
        }
    }

    private String getChannelId(String type){
        return switch (type) {
            case "WA_OTP" -> "WHATS_APP";
            case "SMS_OTP" -> "SMS";
            case "EMAIL_OTP" -> "EMAIL";
            default -> throw new NotImplementedException("The type " + type + " is not implemented");
        };
    }

    @SneakyThrows
    private String removeMFA(String token){
        logger.log("==proceedings with mfa==");

        GETMfaMethodsUNMApi.Inputs getMfaMethodsInputs = new GETMfaMethodsUNMApi.Inputs(token);
        MfaMethodsResponse mfaMethod = getMfaMethods(getMfaMethodsInputs);
        logger.log("otpRecipientId : " + mfaMethod.data.otpRecipientId);
        logger.log("token : " + mfaMethod.data.token);
        String type = mfaMethod.data.methods.get(0).type;
        logger.log("type : " + type);
        String channel = getChannelId(type);

        POSTOtpGenerateUNMApi.Inputs postOtpGenerateUnmInputs = new POSTOtpGenerateUNMApi.Inputs(mfaMethod.data.otpRecipientId,"LOGIN_OTP",channel);

        String otpId = postOtpGenerateUnm(postOtpGenerateUnmInputs);
        logger.log("otpID : " + otpId);

        POSTOtpVerifyUNMApi.Inputs postOtpVerifyUNMApiInputs = new POSTOtpVerifyUNMApi.Inputs(otpId,"LOGIN_OTP");
        String passCode = postOtpVerifyUNMApi(postOtpVerifyUNMApiInputs);
        logger.log("passCode : " + passCode);

        POSTMfaSubmitUNMApi.Inputs postMfaSubmitUNMApiInputs = new POSTMfaSubmitUNMApi.Inputs(passCode,mfaMethod.data.token,type);
        String redirectUrl = postMfaSubmitUNMApi(postMfaSubmitUNMApiInputs);
        logger.log("redirect url : " + redirectUrl);
        return redirectUrl;
    }

    private MfaMethodsResponse getMfaMethods(GETMfaMethodsUNMApi.Inputs inputs)throws ExecutionException, InterruptedException{
        GETMfaMethodsUNMApi getMfaMethodsUNMApi = new GETMfaMethodsUNMApi(inputs);
        ApiResponse getMfaMethodsUNMApiResponse = getMfaMethodsUNMApi.hitApi(apiTools);
        JSONObject postOtpGenerateUNMApiObject = new JSONObject(getMfaMethodsUNMApiResponse.getBody());
        logger.log("code mfa methods res : -> " + postOtpGenerateUNMApiObject);

        Gson gson = new Gson();
        MfaMethodsResponse mfaMethodsResponse = gson.fromJson(getMfaMethodsUNMApiResponse.getBody(), MfaMethodsResponse.class);
        logger.log("code mfa methods : " + mfaMethodsResponse.code);
        return mfaMethodsResponse;
    }

    private String postOtpGenerateUnm(POSTOtpGenerateUNMApi.Inputs inputs)throws ExecutionException, InterruptedException{
        POSTOtpGenerateUNMApi postOtpGenerateUNMApi = new POSTOtpGenerateUNMApi(inputs);
        ApiResponse postOtpGenerateUNMApiResponse = postOtpGenerateUNMApi.hitApi(apiTools);
        JSONObject postOtpGenerateUNMApiObject = new JSONObject(postOtpGenerateUNMApiResponse.getBody());
        logger.log("code otp generate : -> " + postOtpGenerateUNMApiObject.getString("code"));
        return postOtpGenerateUNMApiObject.getJSONObject("data").getString("otpId");
    }

    private String postOtpVerifyUNMApi(POSTOtpVerifyUNMApi.Inputs inputs)throws ExecutionException, InterruptedException{
        POSTOtpVerifyUNMApi postOtpVerifyUNMApi = new POSTOtpVerifyUNMApi(inputs);
        ApiResponse postOtpVerifyUNMApiResponse = postOtpVerifyUNMApi.hitApi(apiTools);
        JSONObject postOtpVerifyUNMApiObject = new JSONObject(postOtpVerifyUNMApiResponse.getBody());
        logger.log("code verify otp methods : -> " + postOtpVerifyUNMApiObject.getString("code"));
        return postOtpVerifyUNMApiObject.getJSONObject("data").getString("passCode");
    }

    private String postMfaSubmitUNMApi(POSTMfaSubmitUNMApi.Inputs inputs)throws ExecutionException, InterruptedException{
        POSTMfaSubmitUNMApi postMfaSubmitUNMApi = new POSTMfaSubmitUNMApi(inputs);
        ApiResponse postMfaSubmitUNMApiResponse = postMfaSubmitUNMApi.hitApi(apiTools);
        JSONObject postMfaSubmitUNMApiObject = new JSONObject(postMfaSubmitUNMApiResponse.getBody());
        logger.log("code mfa submit : -> " + postMfaSubmitUNMApiObject.getString("code"));
        return postMfaSubmitUNMApiObject.getJSONObject("data").getString("redirectUrl");
    }

     public void corporateLogin(String username,String password){
         LoginPage<T> loginPage = LoginPage.getInstance(session);
        loginPage.loginWith("email",username,password);
     }

    public void corporateLoginB2BViaDifferentPage(String username,String password) {
        LoginPage<T> loginPage = LoginPage.getInstance(session);
        loginPage.loginFromDifferentPages(username,password);
    }

    public String forgetPasswordB2B(String username, String password) {
        String updatedPassword = password + randomInt(1);

        LoginPage<T> loginPage = LoginPage.getInstance(session);
        openLoginPageWithDeeplink();
        if (Configs.PLATFORM == Platform.IOS) {
            LoginPageIOS<T> loginPageIOS = new LoginPageIOS<>(session);
            loginPageIOS.closeUnmPageRedirection();
        }
        loginPage.inputEmailOrPhoneNumber(username);
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("bliblitiket.com");
        driverTools.waitForElementToAppear(LOGIN_BUTTON_FIRST_FORM).ifPresent(driverTools::click);
        driverTools.click(FORGOT_PASSWORD);
        driverTools.waitForElementToAppear(FORGOT_PASSWORD_NEXT).ifPresent(driverTools::click);
        driverTools.switchToNative();
        loginPage.inputOtpForgotPassword(1);
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("bliblitiket.com/login");  
        driverTools.waitForElementToAppear(FORGOT_PASSWORD_INPUT).ifPresent(driverTools::click);
        driverTools.sendKeys(FORGOT_PASSWORD_INPUT,updatedPassword);
        driverTools.click(FORGOT_PASSWORD_NEXT);
        Boolean isOnLoginPage = driverTools.isElementDisplayed(PASSWORD_VIEW_BUTTON);
        driverTools.switchToNative();
        Assertion.assertThat("Fogot password Successful", isOnLoginPage, is(true));
        return updatedPassword;
    }

    protected boolean openLoginPageWithDeeplink() {
        Deeplink loginPage = new Deeplink("login page",
                "tiket://m.gatotkaca.tiket.com/login",
                "tiket://m.preprod.tiket.com/login",
                "tiket://m.tiket.com/login");
        try {
            driverTools.deeplink(loginPage);
        }catch (Exception e){
            logger.log("error openning deeplink : " + e.getLocalizedMessage());
        }
        return isOnLoginPageNative();
    }

    private boolean isOnLoginPageNative() {        
        boolean isElementDisplayed = driverTools.waitForElementToAppear(CREATE_ACCOUNT_BUTTON,5).isPresent();
        return isElementDisplayed;
    }
}
