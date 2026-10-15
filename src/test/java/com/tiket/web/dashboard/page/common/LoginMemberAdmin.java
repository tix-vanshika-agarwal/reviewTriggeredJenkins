package com.tiket.web.dashboard.page.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;

public class LoginMemberAdmin<T extends WebDriver> extends BasePage<T> {
    private static final Locator HELLO_GREETING = new Locator("HELLO_GREETING", new By.ByXPath("//div[normalize-space()='Hello,']"));
    private static final Locator LOGOUT = new Locator("LOGOUT", new By.ByXPath("//a[normalize-space()='Logout']"));
    private static final Locator LOGOUT_DROPDOWN = new Locator("LOGOUT_DROPDOWN", new By.ByXPath("//div[contains(@class,'HeaderAccountInfo_email_container')]//span//*[name()='svg']"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", new By.ByXPath("//*[text()='LOGIN' or text()='Hello,']"));
    private static final Locator EMAIL_TEXTFIELD = new Locator("EMAIL_TEXTFIELD", new By.ByXPath("//input[@id='sb-input-username']"));
    private static final Locator PASSWORD_TEXTFIELD = new Locator("PASSWORD_TEXTFIELD", new By.ByXPath("//input[@id='sb-input-password']"));
    private static final Locator B2B_CORPORATE_DASHBOARD = new Locator("B2B_CORPORATE_DASHBOARD", new By.ByXPath("//a[normalize-space()='B2B Corporate']"));
    private static final Locator INSURANCE_DASHBOARD_LINK = new Locator("INSURANCE_DASHBOARD_LINK", By.xpath("//a[normalize-space()='Insurance Dashboard']"));
    private static final Locator PRICING_DASHBOARD_LINK = new Locator("PRICING_DASHBOARD_LINK", By.xpath("//a[normalize-space()='Pricing Dashboard']"));
    private static final Locator ACCOMMODATION_SUPPLIER_DASHBOARD = new Locator("ACCOMMODATION_SUPPLIER_DASHBOARD", By.xpath("//a[normalize-space()='Accommodation Supplier Dashboard']"));
    private static final Locator MM_MODULE_DASHBOARD = new Locator("MM_MODULE_DASHBOARD", By.xpath("//a[normalize-space()='MM Module']"));
    
    public LoginMemberAdmin(Session<T> session) {
        super(session);
    }

    public void gotoHome() {
        if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/member/login");
        } else if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/member/login");
        } else {
            throw new RuntimeException("Environment not found");
        }
    }

    public void loginAsGuest(){
        boolean isLoginButtonPresent = driverTools.isElementDisplayed(HELLO_GREETING);
        if (isLoginButtonPresent) {
            logout();
        }
    }

    /**
     * Authenticates the internal admin via session-cookie injection (the single consolidated
     * {@link MemberLoginPage#adminLoginViaCookie} entry point) instead of the UI form. Use this in
     * place of the {@code gotoHome()/inputEmail()/inputPassword()/clickLogin()} sequence; follow it
     * with the relevant {@code goToXxxDashboard()} navigation, which still works from the
     * {@code /admin/member} landing page reached after injection.
     */
    public void loginViaCookie(String email, String password) {
        new MemberLoginPage<>(session).adminLoginViaCookie(email, password);
    }

    public boolean isUserLoggedIn(String email, String password){
        boolean isLogoutButtonPresent = driverTools.isElementDisplayed(LOGOUT_DROPDOWN);
        if (!isLogoutButtonPresent) {
            inputEmail(email);
            inputPassword(password);
            clickLogin();
        }
        return driverTools.isElementDisplayed(LOGOUT_DROPDOWN);
    }

    public void logout(){
        gotoHome();
        driverTools.waitForElementToAppear(LOGOUT_DROPDOWN);
        driverTools.scrollIntoWebView(LOGOUT_DROPDOWN);
        driverTools.click(LOGOUT_DROPDOWN);
        driverTools.click(LOGOUT);
        driverTools.dummyWait(1);
        if(driverTools.isElementDisplayed(LOGOUT)){
            driverTools.clickWithJavascriptExecutor(LOGOUT);
        }
    }

    public void inputEmail(String email) {
        try {
            driverTools.sendKeys(EMAIL_TEXTFIELD, email);
        }catch (Exception e){
            logger.log("Email field not found, trying to click login button first");
            driverTools.click(EMAIL_TEXTFIELD);
            driverTools.sendKeys(EMAIL_TEXTFIELD, email);
        }
    }

    public void inputPassword(String password) {
        driverTools.sendKeys(PASSWORD_TEXTFIELD, password);
    }

    public boolean clickLogin() {
        driverTools.click(LOGIN_BUTTON);
        driverTools.waitForElementToAppear(HELLO_GREETING);
        return driverTools.isElementDisplayed(HELLO_GREETING);
    }

    public void goToDashboard() {
        Locator CHOSEN_DASHBOARD = new Locator("CHOSEN_DASHBOARD", new By.ByXPath("//a[normalize-space()='B2B Corporate']"));
        driverTools.click(CHOSEN_DASHBOARD);
    }

    public void goToInsuranceDashboard() {
        driverTools.click(INSURANCE_DASHBOARD_LINK);
    }

    public void goToDashboard(String url) {
        driverTools.get("https://"+url);
    }

    public void goToB2BCorporateDashboard() {
        driverTools.waitForElementToAppear(B2B_CORPORATE_DASHBOARD);
        driverTools.clickWithJavascriptExecutor(B2B_CORPORATE_DASHBOARD);
    }

    public void goToPricingDashboard() {
        driverTools.waitForElementToAppear(PRICING_DASHBOARD_LINK);
        driverTools.clickWithJavascriptExecutor(PRICING_DASHBOARD_LINK);
    }

    public void goToAccommodationSupplierDashboard(){
        driverTools.waitForElementToAppear(ACCOMMODATION_SUPPLIER_DASHBOARD);
        driverTools.clickWithJavascriptExecutor(ACCOMMODATION_SUPPLIER_DASHBOARD);
    }

    public void goToMMModuleDashboard(){
        driverTools.waitForElementToAppear(MM_MODULE_DASHBOARD);
        driverTools.clickWithJavascriptExecutor(MM_MODULE_DASHBOARD);
    }
}