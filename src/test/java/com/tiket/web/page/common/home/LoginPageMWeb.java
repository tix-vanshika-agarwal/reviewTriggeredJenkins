package com.tiket.web.page.common.home;

import static org.hamcrest.Matchers.is;
import com.tiket.apiPage.Unm;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.security.auth.login.FailedLoginException;
import java.util.ArrayList;
import java.util.List;

public class LoginPageMWeb<T extends WebDriver> extends LoginPageWeb<T> {
    
    public LoginPageMWeb(Session<T> session) {
        super(session);
    }

    @Override
    public boolean isRegisterPageOpened() {
        return false;
    }

    @Override
    public void registerIntoDweb(String email) throws FailedLoginException, InterruptedException {

    }

    @Override
    public String forgotPasswordDweb(String email, String wrongPassword, String otp, int retryCounter) throws FailedLoginException, InterruptedException {
        return null;
    }

    private static final Locator LOGIN_PROFILE_ICON = new Locator("LOGIN_PROFILE_ICON", new By.ByXPath("//img[contains(@class, 'menu_icon') and contains(@alt, 'Akun') or contains(@alt,'Account')]"));
    private static final Locator LOGOUT_DIV_LINK = new Locator("LOGOUT_DIV_LINK", new By.ByXPath("//p[contains(text(), 'Log out') or contains(text(), 'Keluar')]"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", new By.ByXPath("//button[.//span[normalize-space(text()) = 'Log out' or normalize-space(text())='Keluar']]"));
    private static final Locator RELOAD_BUTTON = new Locator("RELOAD_BUTTON", new By.ByXPath("//button[contains(@class, 'ErrorView_btn')]"));
    private static final Locator ACCOUNT_BUTTON = new Locator("ACCOUNT_BUTTON", new By.ByXPath("//a[@data-testid='nav-item' and (.//p[normalize-space(text())='Account'] or .//p[normalize-space(text())='Akun'])]"));
    private static final Locator ACCOUNT_HOLDER_NAME = new Locator("ACCOUNT_HOLDER_NAME", new By.ByXPath("//span[contains(@class, 'Greetings_greeting_name') and text()!='Hi, t-mate'] | //div[contains(@class,'Profile_list')]//span[contains(@class,'Text')][3]"));
    private static final Locator EDIT_PROFILE_DETAILS = new Locator("EDIT_PROFILE_DETAILS", new By.ByXPath("//div[contains(@class,'Greetings_elipsis_redirect_text')]"));
    private static final String STAGING_ACCOUNT_DETAILS_PAGE = "https://gatotkaca.tiket.com/myaccount/passengers/detail?ref=myaccount";
    private static final String PROD_ACCOUNT_DETAILS_PAGE = "https://tiket.com/myaccount/passengers/detail?ref=myaccount";
    private static final String PREPROD_ACCOUNT_DETAILS_PAGE = "https://preprod.tiket.com/myaccount/passengers/detail?ref=myaccount";
    private static final String STAGING_MY_ACCOUNT_PAGE = "https://gatotkaca.tiket.com/myaccount";
    private static final String PREPROD_MY_ACCOUNT_PAGE = "https://preprod.tiket.com/myaccount";
    private static final String PROD_MY_ACCOUNT_PAGE = "https://www.tiket.com/myaccount";

    public void loginAsGuest() {
        if (checkIfLoggedIn()) {
            logoutFromDweb();
        }
    }

    public void goToAccountDetailsPage() {
        switch (Configs.ENVIRONMENT) {
            case GK:
                driverTools.get(STAGING_ACCOUNT_DETAILS_PAGE);
                break;
            case PREPROD:
                driverTools.getWithDynamicTimeout(PREPROD_ACCOUNT_DETAILS_PAGE, 60);
                break;
            case PROD:
                driverTools.get(PROD_ACCOUNT_DETAILS_PAGE);
                break;
            default: throw new NotImplementedException("Unimplemented ENV type: " + Configs.ENVIRONMENT.name());
        }
    }

    public void goToMyAccountPage() {
        switch (Configs.ENVIRONMENT) {
            case GK:
                driverTools.get(STAGING_MY_ACCOUNT_PAGE);
                break;
            case PREPROD:
                driverTools.get(PREPROD_MY_ACCOUNT_PAGE);
                break;
            case PROD:
                driverTools.get(PROD_MY_ACCOUNT_PAGE);
                break;
            default: throw new NotImplementedException("Unimplemented ENV type: " + Configs.ENVIRONMENT.name());
        }
    }

    @Override
    public void goToAccountPage() {
        if (Configs.PLATFORM == Platform.AMWEB) {
            driverTools.waitForElementToPresence(ACCOUNT_BUTTON, 15);
            driverTools.scrollIntoWebView(ACCOUNT_BUTTON);
            driverTools.clickWithJavascriptExecutor(ACCOUNT_BUTTON);
        } else {
            driverTools.waitForElementToPresence(ACCOUNT_BUTTON, 3);
            driverTools.click(ACCOUNT_BUTTON);
        }
    }

    public void continueIfAlreadyLoggedIn(String email, String password, String otp, int retryCounter) throws FailedLoginException, InterruptedException {
        goToMyAccountPage();
        if (!isAccountHolderNameDisplayed()) {
            loginIfNotWithSameEmail(email, password, otp, retryCounter);
        }
    }

    @Override
    public void loginIfNotWithSameEmail(String email, String password, String otp, int retryCounter) throws FailedLoginException, InterruptedException {
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        goToAccountDetailsPage();
        if(!isAccountHolderNameDisplayed()){
            homePage.goToHomePage();
            this.loginIntoWeb(email,password,otp,retryCounter);
        }
        else{
            String accountHolderName = accountHolderName();
            if(accountHolderName.equals(email)){
                homePage.goToHomePage();
            }
            else{
                homePage.goToHomePage();
                logoutFromDweb();
                this.loginIntoWeb(email,password,otp,retryCounter);
            }
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.waitForElementToAppear(RELOAD_BUTTON, 5);
            int retryCount = 0;
            int maxRetries = 5;
            while (driverTools.isElementDisplayed(RELOAD_BUTTON) && retryCount < maxRetries) {
                driver.navigate().refresh();
                retryCount++;
            }
        }

    }

    public boolean checkIfLoggedIn(){
        goToAccountPage();
        driverTools.waitForElementToAppear(EDIT_PROFILE_DETAILS, 3);
        boolean isDisplayed = driverTools.isElementDisplayed(EDIT_PROFILE_DETAILS);
        driverTools.waitForElementToAppear(LOGIN_PROFILE_ICON,3);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        homePage.goToHomePage();
        return isDisplayed;
    }

    @Override
    public void loginStepsWithoutRetry(String username, String password, String otp) {

    }

    public void logoutFromDweb(){
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        homePage.isHomePageOpened();
        checkIfLoggedIn();

        WebElement loginProfileIcon = driverTools.waitForElementToAppear(LOGIN_PROFILE_ICON).orElseThrow();
        driverTools.click(loginProfileIcon);

        WebElement logoutLink = driverTools.waitForElementToAppear(LOGOUT_DIV_LINK).orElseThrow();
        driverTools.scrollIntoWebView(logoutLink);
        driverTools.clickWithJavascriptExecutor(logoutLink);

        WebElement logoutButton = driverTools.waitForElementToAppear(LOGOUT_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(logoutButton);

        if (driverTools.waitForElementToAppear(RELOAD_BUTTON, 5).isPresent()) {
            driverTools.click(RELOAD_BUTTON);
        }
        int retryCount = 0;
        int maxRetries = 5;
        while (checkIfLoggedIn() && retryCount < maxRetries) {
            driver.navigate().refresh();
            retryCount++;
        }
    }

    @Override
    public boolean isAccountHolderNameDisplayed() {
        driverTools.waitForElementToAppear(ACCOUNT_HOLDER_NAME,10);
        return driverTools.isElementDisplayed(ACCOUNT_HOLDER_NAME);
    }

    @Override
    public void loginSteps(String email, String password, String otp, int retryCounter) throws FailedLoginException, InterruptedException {
        LoginPageWeb<?> loginPageWeb = new LoginPageMWeb<>(session);
        loginPageWeb.inputEmail(email);
        loginPageWeb.inputPassword(password);
        try{
            loginPageWeb.inputOTPCode(otp, retryCounter);
            driverTools.swipeUp(1);
            loginPageWeb.skipPassKey();
        }catch (Exception e){
            logger.log(e.getMessage());
        }
    }

    @Override
    public void skipUNMPageAfterLogin(String timeOut) {

    }

    @Override
    public String accountHolderName() {
        WebElement accountHolderName = driverTools.waitForElementToAppear(ACCOUNT_HOLDER_NAME).orElseThrow();
        return accountHolderName.getText();
    }

    public void goTillLoginPageWithoutLoggingIn(String email) throws FailedLoginException, InterruptedException {
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        goToAccountDetailsPage();
        if(!isAccountHolderNameDisplayed()){
            homePage.goToHomePage();
            homePage.clickOnLoginButton();
        }
        else{
            String accountHolderName = accountHolderName();
            if(accountHolderName.equals(email)){
                homePage.goToHomePage();
            }
            else{
                homePage.goToHomePage();
                logoutFromDweb();
                homePage.clickOnLoginButton();
            }
        }
    }

    @Override
    public void injectSessionCookiesForApiCreatedOrder(String email, String password)
            throws FailedLoginException, InterruptedException {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            logger.log("[Cookie Login] Lean injection unavailable on PROD; falling back to the standard login");
            super.injectSessionCookiesForApiCreatedOrder(email, password);
            return;
        }

        Unm<T> unm = new Unm<>(session);
        injectSession(email, password, unm);
        if (checkIfLoggedIn()) {
            return;
        }

        logger.log("[Cookie Login] Injected token did not authenticate. Invalidating cache and retrying with fresh token.");
        unm.invalidateCachedTokens(email);
        injectSession(email, password, unm);
        Assertion.assertThat("Session cookie injection produced logged-in UI for " + email, checkIfLoggedIn(), is(true));
    }

    private void injectSession(String email, String password, Unm<T> unm) {
        String rawToken = unm.getAccessToken(email, password, "B2C").replace("Bearer ", "");

        List<String> bliblitiketSetCookies = unm.getBliblitiketCookies(email);
        if (bliblitiketSetCookies.isEmpty()) {
            logger.log("[Cookie Login] No bliblitiket cookies cached for " + email + "; forcing a fresh token fetch");
            unm.invalidateCachedTokens(email);
            rawToken = unm.getAccessToken(email, password, "B2C").replace("Bearer ", "");
            bliblitiketSetCookies = unm.getBliblitiketCookies(email);
        }
        injectBliblitiketSetCookies(bliblitiketSetCookies);

        driverTools.get(getTiketWebBaseUrl());

        driver.manage().deleteCookieNamed("session_access_token");
        driver.manage().deleteCookieNamed("session_refresh_token");
        driver.manage().deleteCookieNamed("_auto_session_email");
        driver.manage().addCookie(new Cookie.Builder("session_access_token", rawToken)
                .domain(".tiket.com")
                .path("/")
                .build());
        driver.manage().addCookie(new Cookie.Builder("_auto_session_email", email.toLowerCase())
                .domain(".tiket.com")
                .path("/")
                .build());
        logger.log("[Cookie Login] Injected session_access_token cookie for " + email);
    }

    private void injectBliblitiketSetCookies(List<String> rawSetCookies) {
        if (rawSetCookies.isEmpty()) {
            logger.log("[Cookie Login] No bliblitiket Set-Cookie headers to inject");
            return;
        }

        List<Cookie> cookiesToInject = new ArrayList<>();
        for (String raw : rawSetCookies) {
            String[] parts = raw.split(";");
            String nameValue = parts[0].trim();
            int eqIdx = nameValue.indexOf('=');
            if (eqIdx < 0) continue;

            String name = nameValue.substring(0, eqIdx).trim();
            String value = nameValue.substring(eqIdx + 1).trim();

            String domain = null;
            String path = "/";
            boolean secure = false;
            for (int i = 1; i < parts.length; i++) {
                String part = parts[i].trim();
                if (part.toLowerCase().startsWith("domain=")) {
                    domain = part.substring("domain=".length()).trim();
                } else if (part.toLowerCase().startsWith("path=")) {
                    path = part.substring("path=".length()).trim();
                } else if (part.equalsIgnoreCase("secure")) {
                    secure = true;
                }
            }

            if (domain != null && domain.contains("bliblitiket")) {
                cookiesToInject.add(new Cookie.Builder(name, value)
                        .domain(domain)
                        .path(path)
                        .isSecure(secure)
                        .build());
            }
        }

        if (cookiesToInject.isEmpty()) {
            logger.log("[Cookie Login] No bliblitiket cookies found in Set-Cookie headers");
            return;
        }

        String bliblitiketUrl = switch (Configs.ENVIRONMENT) {
            case GK -> "https://account.test.bliblitiket.dev/";
            case PREPROD -> "https://account.preprod.bliblitiket.dev/";
            case PROD -> throw new NotImplementedException("Bliblitiket cookie injection is not supported on PROD");
        };

        try {
            driverTools.get(bliblitiketUrl);
        } catch (Exception e) {
            logger.log("[Cookie Login] Navigation to " + bliblitiketUrl + " threw: " + e.getMessage()
                    + "; attempting cookie injection anyway");
        }

        for (Cookie c : cookiesToInject) {
            try {
                driver.manage().deleteCookieNamed(c.getName());
                driver.manage().addCookie(c);
            } catch (Exception e) {
                logger.log("[Cookie Login] Failed to inject bliblitiket cookie " + c.getName() + ": " + e.getMessage());
            }
        }
        logger.log("[Cookie Login] Bliblitiket cookie injection complete (" + cookiesToInject.size() + " cookies)");
    }
}
