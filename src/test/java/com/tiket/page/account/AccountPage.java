package com.tiket.page.account;

import com.google.gson.Gson;
import com.tiket.api.auth.GETProfileApi;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.member.GetAccountDetailsApi;
import com.tiket.dbclient.TiketMysqlDBManager;
import com.tiket.dto.MemberDTO;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.setting.SettingPage;
import com.tiket.pojo.auth.ProfileDetailResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import nl.jiankai.mapper.ResultSetMapperFactory;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.monthEngToId;
import static org.hamcrest.Matchers.is;

public abstract class AccountPage<T extends WebDriver> extends BasePage<T> {

    // -------------------------------------------------------------------------
    // Static locators
    // -------------------------------------------------------------------------
    // All static locator fields are defined at the top of the class so that the
    // <clinit> order is obvious and any failure here is easy to spot. They were
    // previously interleaved with instance methods further down; that worked
    // but made class-init failures harder to diagnose - any single throwing
    // initializer marks the whole class as unloadable for the JVM lifetime and
    // every subsequent use surfaces only as `NoClassDefFoundError`.
    //
    // Each field is constructed via `safeLocator(...)`, which catches and logs
    // any constructor failure (e.g. an invalid xpath shipped in a future copy
    // refresh) and returns a sentinel locator instead of letting the exception
    // poison the entire class.
    private static final Locator BACK_BUTTON_INFO_PAGE = safeLocator("BACK_BUTTON_INFO_PAGE",
            "//*[contains(@class,'MobileBackButton_back_button')]");
    private static final Locator PRIVACY_POLICY_PAGE_TEXT = safeLocator("PRIVACY_POLICY_PAGE_TEXT",
            "//*[@data-testid='content']//h1[contains(.,'Privacy Policy') or contains(.,'Kebijakan Privasi')]");
    private static final Locator TERM_AND_CONDITION_PAGE = safeLocator("TERM_AND_CONDITION_PAGE",
            "//*[contains(@class,'FullText_content')]//h1[contains(text(),'Terms and Condition') or contains(text(),'Syarat dan Ketentuan Umum')]");
    private static final Locator PRIVACY_POLICY_PAGE = safeLocator("PRIVACY_POLICY_PAGE",
            "(//*[contains(@class,'FullText_title_text') and (contains(.,'Privacy Notice') or contains(.,'Kebijakan Privasi'))])[1]");
    private static final Locator PRIVACY_POLICY_TOOLBAR = safeLocator("PRIVACY_POLICY_TOOLBAR",
            "//*[contains(@class,'page_title') and (contains(.,'Privacy Policy') or contains(.,'Kebijakan Privasi'))]");

    // see all payment - OVO linkage dialog locators
    private static final Locator OVO_LINKAGE_BTN = safeLocator("OVO_LINKAGE_BTN",
            "//*[contains(@class,'Dialog_body')]//button[contains(@class,'Button_variant_primary') and (contains(text(),'Yes, connect it') or contains(text(),'Ya, hubungkan'))]");
    private static final Locator OVO_CONNECT_DIALOG_MENU = safeLocator("OVO_CONNECT_DIALOG_MENU",
            "//*[contains(@class,'Dialog_body')]//*[contains(@class,'Figure_title') and (contains(text(),'Connect your OVO account?') or contains(text(),'Hubungkan akun OVO-mu?'))]");
    private static final Locator OVO_CAN_NOT_CONNECT_DIALOG_MENU = safeLocator("OVO_CAN_NOT_CONNECT_DIALOG_MENU",
            "//*[contains(@class,'Dialog_body')]//*[contains(@class,'Figure_title') and (contains(text(),'Your OVO account can\u2019t be connected for now') or contains(text(),'Akun OVO-mu belum bisa terhubung, nih'))]");
    private static final Locator OVO_CONNECT_OK_DIALOG_MENU = safeLocator("OVO_CONNECT_OK_DIALOG_MENU",
            "//*[contains(@class,'Dialog_body')]//button[contains(@class,'Button_variant_primary') and contains(text(),'OK')]");

    /**
     * Builds a Locator while guaranteeing that no individual constructor failure
     * can take down the entire class loading. If xpath compilation or
     * {@link Locator}-record validation throws, we log the failing field and
     * fall back to a sentinel xpath that simply will not match anything - that
     * way the class still loads, the tests that don't use this particular field
     * still run, and the tests that DO use it fail with a clear locator-level
     * error instead of an opaque {@code NoClassDefFoundError}.
     * <p>
     * <b>Only {@link Exception}-typed failures are caught.</b> {@link Error}
     * subclasses such as {@link OutOfMemoryError}, {@link StackOverflowError},
     * {@link LinkageError} and {@link VirtualMachineError} are intentionally
     * left to propagate - they signal a corrupt JVM / classpath state where
     * silently substituting a sentinel locator would let the test suite run
     * in an undefined state. The previous {@code catch (Throwable)} masked
     * exactly those fatal cases.
     */
    private static Locator safeLocator(String name, String xpath) {
        try {
            return new Locator(name, AppiumBy.xpath(xpath));
        } catch (Exception e) {
            // Use System.err here because the SLF4J/ILogger plumbing may itself
            // not be initialized when <clinit> runs.
            System.err.println("[AccountPage <clinit>] failed to build static locator "
                    + name + " : " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return new Locator(name, AppiumBy.xpath("//never-matches[@" + name + "='unbuildable']"));
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    protected String translateDobToBhs(String DOB) {
        String DOB_split[] = DOB.split(" ");

        String mothInBhs = monthEngToId(DOB_split[1]);
        return mothInBhs == null ? DOB : DOB_split[0] + " " + mothInBhs + " " + DOB_split[2];
    }

    protected String translateSalutation(String salutation) {
        Map<String, String> mapSalutationEnToBhs = new HashMap<String, String>() {
            {
                put("Mr", "Tuan");
                put("Mrs", "Nyonya");
                put("Ms", "Nona");
            }
        };
        String salutationInBhs = mapSalutationEnToBhs.get(salutation);
        return salutationInBhs == null ? salutation : salutationInBhs;
    }

    public AccountPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AccountPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new AccountPageAndroid<>(session);
            }
            case IOS -> {
                return new AccountPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    @SneakyThrows
    public String getToken() {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);

        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");

        String finalToken = "Bearer " + accessToken;
        logger.log("PayExpress access token -> " + finalToken);
        return finalToken;
    }

    /**
     * @param token Token need to be used for login first
     * @return ProfileDetailResponse
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public ProfileDetailResponse getProfileData(String token) throws InterruptedException, ExecutionException {
        GETProfileApi.Inputs input = new GETProfileApi.Inputs(token);
        GETProfileApi getProfileApi = new GETProfileApi(input);
        ApiResponse res = getProfileApi.hitApi(apiTools);
        Gson gson = new Gson();
        ProfileDetailResponse resDeserialized = gson.fromJson(res.getBody(), ProfileDetailResponse.class);
        return resDeserialized;
    }

    public abstract void chooseCountry(String country);

    public abstract void refresh();
    
    public abstract boolean isAccountDetailOpened();

    public abstract void openSettings();

    public abstract void clickEditProfileDetail();

    public abstract void selectLanguage(String language);

    public abstract void clickBackButton();

    public abstract void closeAccountPage(); // FIXME: Implementation pending

    public abstract boolean isLanguageSelected(String language);

    public abstract void clickAccountDetail();

    public abstract boolean verifyAccountDetail(String firstName, String lastName, String dateOfBirth,
            String salutation);

    public abstract boolean checkAccountName(String username);

    public abstract void openHelpCenterPage();

    public abstract void openWishlistPage();

    public abstract void openMyReviewPage();

    public abstract void openSmartProfilePage(); // FIXME: Implementation pending
    public abstract void openLoyaltyPartnerPage();

    public abstract boolean checkVerifiedPhone();

    public abstract boolean checkAccountEmailAndName(String expectedEmail, String expectedName);

    public abstract boolean checkAccountEmailAndPhone(String expectedEmail, String phoneNumber);
    public abstract String getAccountDetailEmail();

    public abstract boolean checkAccountEntity(String entity);

    public abstract String userEditAccountDetails();

    public abstract void clickEditPhoneAndEmailBtn();

    //public abstract void changePhoneNumberDetailStep(String newPhoneNumber); //TODO fix this

    public abstract boolean verifyEditedAccountDetail(String expectedData);

    public abstract boolean isLoggedIn();

    @Deprecated
    public abstract void skipSavePassengerCoachMarkWithCondition(String username);

    public abstract void openVoucherPage();

    public abstract boolean isVoucherPageOpen();

    public abstract void openOthers();

    public abstract void openDeviceInfoMenu();

    public abstract boolean verifyDeviceInfoShown();

    public abstract boolean verifyCopyDeviceInfoButton();

    public abstract boolean verifyLoyaltyPointValue(String exepctedValue);

    public abstract String getLoyaltyPointValue();

    public abstract void clickLoyaltyBanner();

    public abstract boolean canClickTiketLoyaltyBanner();

    public abstract void clickLoginRegisterButtonLoyaltyPopup();

    public abstract String getAccountName();

    public abstract void openTermAndCondition();

    public abstract void openPrivacyPolicyMenu();

    public abstract void openAboutTiketDotCom();

    public abstract boolean verifyAboutTiketDotCom();

    public abstract void clickSeeAllPayment();

    public abstract void clickAddBcaCard();

    public abstract boolean verifyBCAForm(String email);

    public abstract String getAccountDetailPhoneNumber();

    public abstract String getPhoneNumberInChangeEmailOrPhonePage();

    public abstract void closeChangeEmailOrPhonePage();
    public abstract void openReferralPage();
    public abstract void openCurrencyPage();
    public abstract void openPaylaterPage();
    public abstract boolean verifyPaylaterPageOpen();
    public abstract boolean verifyIsOnAccountPage();
    public abstract boolean verifyBottomSheetIntroducingFeatureForMemberOnly();
    public abstract void clickLaterCTABtn();
    public abstract boolean verifyBottomSheetDisappear();
    public abstract void openAccountPageUsingDeeplink();
    public abstract void openAccountPageUsingDeeplink(String countryCode, String currency);
    public abstract String getSelectedCountry();
    public abstract String getSelectedCurrency();
    public abstract boolean isAccountPageDisplayed();
    public abstract boolean clickContinueAsGuest();
    public abstract void openAffiliate();

    public void clickSavePassengerData(){
        SettingPage<T> settingPage = SettingPage.getInstance(session);
        settingPage.openSavePassengersData();
    }

     public boolean verifyEmailPresentInDB(String email) throws Exception {
        //PII changes
        String sqlQuery = "SELECT * FROM tix_member_core.members_account_b2c WHERE accountUsernameBlindIdx = sha2('"+email.toLowerCase()+"', 256)";
        var db = TiketMysqlDBManager.getInstance("member");
        var dbExecute = ResultSetMapperFactory.getResultSetMapperIdentity().map(db.executeQuery(sqlQuery),
                MemberDTO.class);
        MemberDTO result;
        if (dbExecute.stream().count() < 1) {
            String msg = "Data not found in Database!";
            logger.fail(msg);
            throw new Exception(msg);
        } else {
            result = dbExecute.get(0);
            Assertion.assertThat("is user recorded as guest?", result.isGuest(), is(true));
        }
        logger.log("Data found in Database : \n" + result.toString());
        return result.getAccountUsername() != null;
    }

    @SneakyThrows
    public String getEmailFromAccountDetails(String paramType, String paramValue) throws ExecutionException, InterruptedException {
        GetAccountDetailsApi.Inputs inputs = new GetAccountDetailsApi.Inputs(paramType, paramValue.replace("+",""));
        GetAccountDetailsApi getAccountDetailsApi = new GetAccountDetailsApi(inputs);
        ApiResponse response = getAccountDetailsApi.hitApi(apiTools);
        JSONObject responseJson = new JSONObject(response.getBody());
        return responseJson.getJSONObject("data").getString("email");
    }

    public boolean verifyTermAndConditionPage(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("tos");
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToAppear(TERM_AND_CONDITION_PAGE,10);
        boolean isTermAndConditionPageDisplayed = driverTools.isElementDisplayed(TERM_AND_CONDITION_PAGE);
        logger.log("Term and Condition page displayed: " + isTermAndConditionPageDisplayed);
        driverTools.switchToNative();
        return isTermAndConditionPageDisplayed;
    }

    public boolean verifyPrivacyPolicy() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("privacy-policy");
        driverTools.waitForWebToLoad();
        WebElement element = driverTools.waitForElementToAppear(PRIVACY_POLICY_TOOLBAR, 10).orElseThrow();
        boolean backButton = driverTools.isElementDisplayed(BACK_BUTTON_INFO_PAGE);
        logger.log("back button shown" + backButton);
        boolean toolBar = driverTools.isElementDisplayed(element);
        logger.log("tool bar shown" + toolBar);
        driverTools.waitForElementToAppear(PRIVACY_POLICY_PAGE_TEXT);
        boolean pageText = driverTools.isElementDisplayed(PRIVACY_POLICY_PAGE) || driverTools.isElementDisplayed(PRIVACY_POLICY_PAGE_TEXT);
        logger.log("page text have privacy policy " + pageText);
        driverTools.switchToNative();
        return backButton && toolBar && pageText;
    }

    public void selectPaymentMethod(String paymentMethod) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("smart_pay");
        driverTools.waitForWebToLoad();
        driverTools.click(getPaymentMethodLocator(paymentMethod));
        logger.log("Selected payment method: " + paymentMethod);
        driverTools.switchToNative();
    }

    private Locator getPaymentMethodLocator(String paymentMethod) {
        return new Locator("PAYMENT_METHOD_" + paymentMethod.toUpperCase(),
                AppiumBy.xpath("//button[contains(@id,'"+paymentMethod.toUpperCase()+"')]"));
    }

    public boolean isOvoPaymentMenuOpen() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("smart_pay");
        driverTools.waitForWebToLoad();
        boolean isOvoConnectDialogDisplayed = driverTools.isElementDisplayed(OVO_CONNECT_DIALOG_MENU);
        logger.log("OVO Connect Dialog displayed: " + isOvoConnectDialogDisplayed);
        boolean isLinkageOvoButtonDisplayed = driverTools.isElementDisplayed(OVO_LINKAGE_BTN);
        logger.log("Linkage OVO button displayed: " + isLinkageOvoButtonDisplayed);
        driverTools.switchToNative();
        return isOvoConnectDialogDisplayed && isLinkageOvoButtonDisplayed;
    }

    public void clickOvoLinkageButton() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("smart_pay");
        driverTools.waitForWebToLoad();
        driverTools.click(OVO_LINKAGE_BTN);
        logger.log("Clicked OVO Linkage button");
        driverTools.switchToNative();
    }

    public boolean isOvoPhoneNotRegistered() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("smart_pay");
        driverTools.waitForWebToLoad();
        boolean isOvoPhoneNotRegistered = driverTools.isElementDisplayed(OVO_CAN_NOT_CONNECT_DIALOG_MENU);
        logger.log("OVO Phone Not Registered Dialog displayed: " + isOvoPhoneNotRegistered);
        boolean isOvoConnectOkDialogDisplayed = driverTools.isElementDisplayed(OVO_CONNECT_OK_DIALOG_MENU);
        logger.log("OVO Connect OK Dialog displayed: " + isOvoConnectOkDialogDisplayed);
        driverTools.click(OVO_CONNECT_OK_DIALOG_MENU);
        driverTools.switchToNative();
        return isOvoPhoneNotRegistered && isOvoConnectOkDialogDisplayed;
    }

public boolean verifyAffiliate() {
        final var heroBanner = new Locator("AFFILIATE_HERO_BANNER", AppiumBy.xpath("//*[@data-testid='hero-banner-content']"));
        driverTools.waitForWebview();
        try {
            driverTools.switchToWebViewUrl("affiliate");
            driverTools.waitForWebToLoad();
            driverTools.waitForElementToAppear(heroBanner).orElseThrow();
            return driverTools.getCurrentURL().contains("blibli-tiket-affiliate");
        } finally {
            driverTools.switchToNative();
        }
    }
}
