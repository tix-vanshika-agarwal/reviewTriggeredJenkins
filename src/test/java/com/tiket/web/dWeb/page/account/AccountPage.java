package com.tiket.web.dWeb.page.account;

import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.unm.*;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.auth.MfaMethodsResponse;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.concurrent.ExecutionException;

public class AccountPage<T extends WebDriver> extends BasePage<T> {
    public AccountPage(Session<T> session) {
        super(session);
    }

    private static final Locator ADD_SMART_PROFILE = new Locator("ADD_SMART_PROFILE", new By.ByXPath("//span[contains(@class,'Header_profile_add')]"));
    private static final Locator ACCOUNT_SALUTATION = new Locator("ACCOUNT_SALUTATION", new By.ByXPath("(//input[@name='accountSalutationName'])[1]"));
    private static final Locator ACCOUNT_FIRST_NAME = new Locator("ACCOUNT_FIRST_NAME", new By.ByXPath("//input[@id='account-first-name']"));
    private static final Locator ACCOUNT_LAST_NAME = new Locator("ACCOUNT_LAST_NAME", new By.ByXPath("//input[@id='account-last-name']"));
    private static final Locator DATE = new Locator("DATE", new By.ByXPath("//input[@id='date-of-birth']"));
    private static final Locator SAVE_DATE = new Locator("SAVE_DATE", new By.ByXPath("//footer[contains(@class,'Date')]//button"));
    private static final Locator ACCOUNT_NATIONALITY_CODE = new Locator("ACCOUNT_NATIONALITY_CODE", new By.ByXPath("//input[@id='account-nationality-code']"));
    private static final Locator SELECT_NATIONALITY = new Locator("SELECT_NATIONALITY", new By.ByXPath("//*[contains(@class,'CountryListSelection_list_item')]"));
    private static final Locator NIN_CODE = new Locator("NIN_CODE", new By.ByXPath("//input[@id='identity-number']"));
    private static final Locator ACCOUNT_EMAIL = new Locator("ACCOUNT_EMAIL", new By.ByXPath("//input[@id='account-email']"));
    private static final Locator ACCOUNT_PHONE = new Locator("ACCOUNT_PHONE", new By.ByXPath("//input[@id='account-phone']"));
    private static final Locator SAVE_PASSENGER = new Locator("SAVE_PASSENGER", new By.ByXPath("//div[contains(@class,'styles_save_passenger')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator DELETE_PASSENGER = new Locator("DELETE_PASSENGER", new By.ByXPath("//button[contains(@class,'Button_variant_danger')]"));
    private static final Locator YES_DELETE_PASSENGER = new Locator("YES_DELETE_PASSENGER", new By.ByXPath("//button[contains(@class,'Button_variant_primary') and contains(text(),',')]"));
    private static final Locator ERROR_MESSAGE = new Locator("ERROR_MESSAGE", new By.ByXPath("(//div[contains(@class,'styles_state_danger')])[1]"));
    private static final Locator EMERGENCY_CONTACT = new Locator("EMERGENCY_CONTACT", new By.ByXPath("//input[@name='emergencyContactName']"));
    private static final Locator EMERGENCY_NUMBER = new Locator("EMERGENCY_NUMBER", new By.ByXPath("//input[@type='tel' and @id='emergency-country-phone-number']"));
    private static final Locator PASSENGER_INFO = new Locator("PASSENGER_INFO", new By.ByXPath("(//span[contains(@class, 'Text_size_b1') and contains(@class, 'Text_weight_bold')])[1]"));

    private Locator smartProfileLocator(String name) {
        return new Locator("SMART_PROFILE", new By.ByXPath("//div[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'" + name.toLowerCase() + "')]"));
    }

    public boolean verifySmartProfileByName(String firstName, String lastName) {
        driverTools.waitForElementToAppear(smartProfileLocator(firstName + " " + lastName));
        return driverTools.isElementDisplayed(smartProfileLocator(firstName + " " + lastName));
    }

    public void deleteSmartProfile(String firstName, String lastName) {
        driverTools.clickWithJavascriptExecutor(smartProfileLocator(firstName + " " + lastName));
        driverTools.waitForElementToPresence(DELETE_PASSENGER);
        driverTools.clickWithJavascriptExecutor(DELETE_PASSENGER);
        driverTools.clickWithJavascriptExecutor(YES_DELETE_PASSENGER);
    }

    @SneakyThrows
    public String getToken() {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer " + accessToken;
    }

    private String getChannelId(String type) {
        return switch (type) {
        case "WA_OTP" -> "WHATS_APP";
        case "SMS_OTP" -> "SMS";
        case "EMAIL_OTP" -> "EMAIL";
        default -> throw new NotImplementedException("The type " + type + " is not implemented");
        };
    }

    @SneakyThrows
    private String removeMFA(String token) {
        logger.log("==proceedings with mfa==");

        GETMfaMethodsUNMApi.Inputs getMfaMethodsInputs = new GETMfaMethodsUNMApi.Inputs(token);
        MfaMethodsResponse mfaMethod = getMfaMethods(getMfaMethodsInputs);
        logger.log("otpRecipientId : " + mfaMethod.data.otpRecipientId);
        logger.log("token : " + mfaMethod.data.token);
        String type = mfaMethod.data.methods.get(0).type;
        logger.log("type : " + type);
        String channel = getChannelId(type);

        POSTOtpGenerateUNMApi.Inputs postOtpGenerateUnmInputs = new POSTOtpGenerateUNMApi.Inputs(mfaMethod.data.otpRecipientId, "LOGIN_OTP", channel);

        String otpId = postOtpGenerateUnm(postOtpGenerateUnmInputs);
        logger.log("otpID : " + otpId);

        POSTOtpVerifyUNMApi.Inputs postOtpVerifyUNMApiInputs = new POSTOtpVerifyUNMApi.Inputs(otpId, "LOGIN_OTP");
        String passCode = postOtpVerifyUNMApi(postOtpVerifyUNMApiInputs);
        logger.log("passCode : " + passCode);

        POSTMfaSubmitUNMApi.Inputs postMfaSubmitUNMApiInputs = new POSTMfaSubmitUNMApi.Inputs(passCode, mfaMethod.data.token, type);
        String redirectUrl = postMfaSubmitUNMApi(postMfaSubmitUNMApiInputs);
        logger.log("redirect url : " + redirectUrl);
        return redirectUrl;
    }

    private MfaMethodsResponse getMfaMethods(GETMfaMethodsUNMApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GETMfaMethodsUNMApi getMfaMethodsUNMApi = new GETMfaMethodsUNMApi(inputs);
        ApiResponse getMfaMethodsUNMApiResponse = getMfaMethodsUNMApi.hitApi(apiTools);
        JSONObject postOtpGenerateUNMApiObject = new JSONObject(getMfaMethodsUNMApiResponse.getBody());
        logger.log("code mfa methods res : -> " + postOtpGenerateUNMApiObject);

        Gson gson = new Gson();
        MfaMethodsResponse mfaMethodsResponse = gson.fromJson(getMfaMethodsUNMApiResponse.getBody(), MfaMethodsResponse.class);
        logger.log("code mfa methods : " + mfaMethodsResponse.code);
        return mfaMethodsResponse;
    }

    private String postOtpGenerateUnm(POSTOtpGenerateUNMApi.Inputs inputs) throws ExecutionException, InterruptedException {
        POSTOtpGenerateUNMApi postOtpGenerateUNMApi = new POSTOtpGenerateUNMApi(inputs);
        ApiResponse postOtpGenerateUNMApiResponse = postOtpGenerateUNMApi.hitApi(apiTools);
        JSONObject postOtpGenerateUNMApiObject = new JSONObject(postOtpGenerateUNMApiResponse.getBody());
        logger.log("code otp generate : -> " + postOtpGenerateUNMApiObject.getString("code"));
        return postOtpGenerateUNMApiObject.getJSONObject("data").getString("otpId");
    }

    private String postOtpVerifyUNMApi(POSTOtpVerifyUNMApi.Inputs inputs) throws ExecutionException, InterruptedException {
        POSTOtpVerifyUNMApi postOtpVerifyUNMApi = new POSTOtpVerifyUNMApi(inputs);
        ApiResponse postOtpVerifyUNMApiResponse = postOtpVerifyUNMApi.hitApi(apiTools);
        JSONObject postOtpVerifyUNMApiObject = new JSONObject(postOtpVerifyUNMApiResponse.getBody());
        logger.log("code verify otp methods : -> " + postOtpVerifyUNMApiObject.getString("code"));
        return postOtpVerifyUNMApiObject.getJSONObject("data").getString("passCode");
    }

    private String postMfaSubmitUNMApi(POSTMfaSubmitUNMApi.Inputs inputs) throws ExecutionException, InterruptedException {
        POSTMfaSubmitUNMApi postMfaSubmitUNMApi = new POSTMfaSubmitUNMApi(inputs);
        ApiResponse postMfaSubmitUNMApiResponse = postMfaSubmitUNMApi.hitApi(apiTools);
        JSONObject postMfaSubmitUNMApiObject = new JSONObject(postMfaSubmitUNMApiResponse.getBody());
        logger.log("code mfa submit : -> " + postMfaSubmitUNMApiObject.getString("code"));
        return postMfaSubmitUNMApiObject.getJSONObject("data").getString("redirectUrl");
    }

    private String removeExtraFromUrlToGetAuthCode(String url) {
        String part1 = url.split("authCode=")[1];
        return part1.split("&ref=")[0];
    }

    private String getAuthCode(POSTAuthLoginUNMApi.Inputs inputs) throws ExecutionException, InterruptedException {
        POSTAuthLoginUNMApi postAuthLoginUNMApi = new POSTAuthLoginUNMApi(inputs);
        ApiResponse authCodeResponse = postAuthLoginUNMApi.hitApi(apiTools);
        JSONObject authCodeObject = new JSONObject(authCodeResponse.getBody());
        logger.log("auth code res -> " + authCodeObject);
        if (authCodeObject.getString("code").contains("MFA_REQUIRED")) {
            logger.log("==MFA_REQUIRED==");
            return removeExtraFromUrlToGetAuthCode(removeMFA(authCodeObject.getJSONObject("data").getString("token")));
        }
        String redirectURL = authCodeObject.getJSONObject("data").getString("redirectUrl");
        return removeExtraFromUrlToGetAuthCode(redirectURL);
    }

    private String getServiceTicket(GETAuthLoginUNMApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GETAuthLoginUNMApi getAuthLoginUNMApi = new GETAuthLoginUNMApi(inputs);
        ApiResponse serviceTicketResponse = getAuthLoginUNMApi.hitApi(apiTools);
        JSONObject serviceTicketObject = new JSONObject(serviceTicketResponse.getBody());
        logger.log("service ticket res -> " + serviceTicketObject);
        return serviceTicketObject.getJSONObject("data").getString("serviceTicket");
    }

    private String getAccessToken(POSTServiceTicketUNMApi.Inputs inputs, String username) throws ExecutionException, InterruptedException {
        POSTServiceTicketUNMApi postServiceTicketUNMApi = new POSTServiceTicketUNMApi(inputs);
        ApiResponse accessTokenResponse = postServiceTicketUNMApi.hitApi(apiTools);
        JSONObject accessTokenObject = new JSONObject(accessTokenResponse.getBody());
        logger.log("access token res -> " + accessTokenObject);
        return accessTokenObject.getJSONObject("data").getString("accessToken");
    }

    @SneakyThrows
    public String getAccessTokenUNM(String username, String password) {
        POSTAuthLoginUNMApi.Inputs authCodeInputs = new POSTAuthLoginUNMApi.Inputs(username, password);
        String authCode = getAuthCode(authCodeInputs);
        logger.log("auth code : " + authCode);

        GETAuthLoginUNMApi.Inputs getServiceTicketInputs = new GETAuthLoginUNMApi.Inputs(authCode);
        String serviceTicket = getServiceTicket(getServiceTicketInputs);
        logger.log("service ticket : " + serviceTicket);

        POSTServiceTicketUNMApi.Inputs accessTokenInputs = new POSTServiceTicketUNMApi.Inputs(serviceTicket);
        String accessToken = getAccessToken(accessTokenInputs, username);
        logger.log("access token : " + accessToken);
        return accessToken;
    }
}