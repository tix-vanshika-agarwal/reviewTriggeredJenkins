package com.tiket.web.dashboard.page.b2b_external;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.security.auth.login.FailedLoginException;

public class HomePage <T extends WebDriver> extends BasePage<T> {
    private static final Locator TIKET_LOGO = new Locator("TIKET_LOGO", new By.ByXPath("//img[@alt='Tiket.com logo']"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", new By.ByXPath("//a[normalize-space()='Login' or normalize-space()='Masuk']"));
    private static final Locator CORPORATE_LOGIN_INDICATOR = new Locator("CORPORATE_LOGIN_INDICATOR", new By.ByXPath("//h1[contains(text(),'Corporate Dashboard')]"));
    private static final Locator GO_TO_TIKETB2C_PAGE = new Locator("GO_TO_TIKETB2C_PAGE", new By.ByXPath("//button[text()='Go To tiket.com page']"));
    private static final Locator LOGOUT = new Locator("LOGOUT", new By.ByXPath("//*[text()='Log Out']"));
    private static final Locator PROFILE_COMPONENT = new Locator("PROFILE_COMPONENT", new By.ByXPath("//span[contains(@class,'md:w-11 md:h-11 md:text-lg')]"));
    private static final Locator LOGOUT_ON_DASHBOARD = new Locator("LOGOUT_ON_DASHBOARD", new By.ByXPath("(//span[text()='Log Out'])[1]"));
    private static final Locator LOGIN_PROFILE_ICON = new Locator("LOGIN_PROFILE_ICON", new By.ByXPath("//div[contains(@class,'blink_icon')]"));
    private static final Locator LOGOUT_DIV_LINK = new Locator("LOGOUT_DIV_LINK", new By.ByXPath("//a[text() = 'Log Out' or text()='Keluar']"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", new By.ByXPath("//button[normalize-space()='Log out' or normalize-space()='Keluar']"));
    private static final Locator RELOAD = new Locator("RELOAD", new By.ByXPath("//button[normalize-space()='Reload']"));
    private static final String STAGING_ADMIN_LOGOUT= "https://gatotkaca.tiket.com/admin/member/logout";
    private static final String PROD_ADMIN_LOGOUT = "https://www.tiket.com/admin/member/logout";
    private static final String PREPROD_ADMIN_LOGOUT = "https://preprod.tiket.com/admin/member/logout";

    public HomePage(Session<T> session) {
        super(session);
    }

    private boolean hitHomeDeeplink(Deeplink deeplink) {
        switch (Configs.ENVIRONMENT){
            case PROD:
                driverTools.get(deeplink.prodDeeplink());
                break;
            case PREPROD:
                driverTools.get(deeplink.preprodDeeplink());
                break;
            case GK:
                driverTools.get(deeplink.gkDeeplink());
                break;
        }
        return driverTools.isElementDisplayed(TIKET_LOGO);
    }

    public void gotoHome() {
        Deeplink deeplink = new Deeplink("HOME_DEEPLINK_" + Configs.ENVIRONMENT,"https://gatotkaca.tiket.com","https://preprod.tiket.com/","https://en.tiket.com/");
        boolean isRedirectedToHomePage =  hitHomeDeeplink(deeplink);
        if(!isRedirectedToHomePage){
            //to handle if the not redirected to home page
            logger.log("Not redirected to home page, retrying");
            hitHomeDeeplink(deeplink);
        }

        if(driverTools.isElementDisplayed(RELOAD)){
            driverTools.click(RELOAD);
        }
        driverTools.waitForElementToAppear(TIKET_LOGO, 30).orElseThrow();
    }

    /**
     * Logs in the given account via UNM cookie injection (no UI form/OTP). The user ends up
     * logged in on the tiket.com consumer home page. Use this for traveler / B2C flows.
     */
    public void loginViaCookie(String email, String password) throws FailedLoginException, InterruptedException {
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        loginPage.loginWithSpecificAccountViaCookie(email, password);
    }

    /**
     * Logs in the given corporate account via UNM cookie injection and then opens the corporate
     * dashboard. Use this for admin/approver/super-admin flows that operate on the dashboard.
     */
    public void loginViaCookieAndOpenCorporateDashboard(String email, String password) throws FailedLoginException, InterruptedException {
        loginViaCookie(email, password);
        goToCorporateDashboard();
    }

    public void goToCorporateDashboard() {
        driverTools.maximizeWindow();
        switch (Configs.ENVIRONMENT) {
            case GK:
                driverTools.get("https://corporate-gatotkaca.tiket.com/");
                break;
            case PREPROD:
                driverTools.get("https://corporate.preprod.tiket.com/");
                break;
            case PROD:
                driverTools.get("https://corporate.tiket.com/");
                break;
            default: throw new NotImplementedException("Unimplemented ENV type: " + Configs.ENVIRONMENT.name());
        }
    }

    public void loginAsGuest(){

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePage<?> homePage = new HomePage<>(session);
        loginPage.goToAccountPage();
        if(!loginPage.isAccountHolderNameDisplayed()){
            // to ensure internal admin gets logged out always if due failing case it remains logged in
            goToAdminLogoutPage();
            driverTools.dummyWait(1);
        }
        homePage.gotoHome();
        boolean isLoginButtonPresent = driverTools.isElementDisplayed(LOGIN_BUTTON);
        if (!isLoginButtonPresent) {
            logoutFromDweb();
        }
    }

    public void goToAdminLogoutPage() {
        switch (Configs.ENVIRONMENT) {
            case GK:
                driverTools.get(STAGING_ADMIN_LOGOUT);
                break;
            case PREPROD:
                driverTools.get(PREPROD_ADMIN_LOGOUT);
                break;
            case PROD:
                driverTools.get(PROD_ADMIN_LOGOUT);
                break;
            default: throw new NotImplementedException("Unimplemented ENV type: " + Configs.ENVIRONMENT.name());
        }
    }

    public void logout(){
        driverTools.waitForElementToAppear(LOGOUT);
        driverTools.click(LOGOUT);
        driverTools.waitForElementToAppear(LOGIN_BUTTON);
    }

    public void logoutFromAdminDashboard(){
        WebElement element = driver.findElement(By.xpath("//span[contains(@class,'md:w-11 md:h-11 md:text-lg')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        driverTools.clickWithJavascriptExecutor(PROFILE_COMPONENT);
        driverTools.waitForElementToAppear(LOGOUT_ON_DASHBOARD);
        driverTools.clickWithJavascriptExecutor(LOGOUT_ON_DASHBOARD);
    }

    public void logoutFromApproverDashboard(){
        driverTools.waitForElementToAppear(PROFILE_COMPONENT);
        driverTools.click(PROFILE_COMPONENT);
        driverTools.waitForElementToAppear(LOGOUT_ON_DASHBOARD);
        driverTools.click(LOGOUT_ON_DASHBOARD);
    }

    public void logoutFromDweb(){
        if(driverTools.isElementDisplayed(RELOAD)){
            driverTools.click(RELOAD);
        }
        driverTools.waitForElementToAppear(TIKET_LOGO, 30).orElseThrow();
        driverTools.waitForElementToAppear(LOGIN_PROFILE_ICON).orElseThrow();
        driverTools.click(LOGIN_PROFILE_ICON);
        driverTools.click(LOGOUT_DIV_LINK);
        if ( driverTools.isElementPresent(LOGOUT_BUTTON)){
            driverTools.click(LOGOUT_BUTTON);
        }
    }

    public void openLoginPage() {
        driverTools.waitForElementToAppear(LOGIN_BUTTON);
        driverTools.click(LOGIN_BUTTON);
        driverTools.waitForElementToDisappear(LOGIN_BUTTON);
    }

    public void goToTiketb2cPage() {
        driverTools.click(GO_TO_TIKETB2C_PAGE);
    }

    public boolean checkIfLoggedIn(){
        driverTools.waitForElementToAppear(CORPORATE_LOGIN_INDICATOR);
        return driverTools.isElementDisplayed(CORPORATE_LOGIN_INDICATOR);
    }
}