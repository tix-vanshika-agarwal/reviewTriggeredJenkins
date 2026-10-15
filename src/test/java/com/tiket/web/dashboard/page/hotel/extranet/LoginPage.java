package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.core.Is.is;

public class LoginPage <T extends WebDriver> extends BasePage<T> {
    public LoginPage(Session<T> session) {
        super(session);
    }

    private static final String EXTRANET_LANDING_URL_GK = "https://tix-gatotkaca.tiket.com/v3";
    private static final String EXTRANET_LANDING_URL_PREPROD = "http://tix.preprod.tiket.com/v3";
    private static final Locator EXTRANET_LANDING_LABEL = new Locator("EXTRANET_LANDING_LABEL", new By.ByXPath("//div[@class=\"logo-name\"][text()='Extranet']"));
    private static final Locator LOGIN_NAVBAR_BUTTON = new Locator("LOGIN_NAVBAR_BUTTON", new By.ByXPath("//div[@class=\"nav-list\"][text()='Log in']"));
    private static final Locator LOGIN_TITLE = new Locator("LOGIN_TITLE", new By.ByXPath("//div[text()='Log In']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", new By.ByXPath("//input[@name='username']"));
    private static final Locator PASSWORD_INPUT = new Locator("PASSWORD_INPUT", new By.ByXPath("//input[@name='password']"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", new By.ByXPath(" //button//span[text()='Log In']"));
    private static final Locator OTP_TEXTFIELD1 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("//input[@type='number' and @name='0']"));
    private static final Locator OTP_TEXTFIELD2 = new Locator("OTP_TEXTFIELD2", new By.ByXPath("//input[@type='number' and @name='1']"));
    private static final Locator OTP_TEXTFIELD3 = new Locator("OTP_TEXTFIELD3", new By.ByXPath("//input[@type='number' and @name='2']"));
    private static final Locator OTP_TEXTFIELD4 = new Locator("OTP_TEXTFIELD4", new By.ByXPath("//input[@type='number' and @name='3']"));

    public void goToExtranetLandingPage() {
        if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.getWithDynamicTimeout(EXTRANET_LANDING_URL_PREPROD, 60);
        } else if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.getWithDynamicTimeout(EXTRANET_LANDING_URL_GK, 60);
        } else {
            throw new RuntimeException("Environment not found");
        }
        driverTools.maximizeWindow();
    }

    public boolean isExtranetLandingPageLoaded() {
        return driverTools.waitForElementToAppear(EXTRANET_LANDING_LABEL).isPresent();
    }

    public boolean isLoginButtonPresent() {
        return driverTools.waitForElementToAppear(LOGIN_NAVBAR_BUTTON, 3).isPresent();
    }

    public void clickNavbarLogin() {
        WebElement button = driverTools.waitForElementToBeClickable(LOGIN_NAVBAR_BUTTON).orElseThrow();
        driverTools.click(button);
        boolean loginPageLoaded = isExtranetLoginPageLoaded();
        Assertion.assertThat("User is in extranet login page. Expected: true Actual: " + loginPageLoaded, loginPageLoaded, is(true));
    }

    public boolean isExtranetLoginPageLoaded() {
        return driverTools.waitForElementToAppear(LOGIN_TITLE, 3).isPresent();
    }

    public void tryToLogin(String email, String password) {
        WebElement emailInput = driverTools.waitForElementToAppear(EMAIL_INPUT, 3).orElseThrow();
        driverTools.sendKeys(emailInput, email);
        WebElement passwordInput = driverTools.waitForElementToAppear(PASSWORD_INPUT, 3).orElseThrow();
        driverTools.sendKeys(passwordInput, password);
        WebElement button = driverTools.waitForElementToBeClickable(LOGIN_BUTTON, 3).orElseThrow();
        driverTools.click(button);
        if (driverTools.waitForElementToAppear(OTP_TEXTFIELD1, 3).isPresent()) {
            enterOTP("1234");
        } else {
            logger.log("OTP field not visible; skipping OTP entry (active MFA session)");
        }
    }

    public void enterOTP(String otp){
        WebElement otpField1 = driverTools.waitForElementToAppear(OTP_TEXTFIELD1, 5).orElseThrow();
        driverTools.sendKeys(otpField1, String.valueOf(otp.charAt(0)));

        WebElement otpField2 = driverTools.waitForElementToAppear(OTP_TEXTFIELD2, 1).orElseThrow();
        driverTools.sendKeys(otpField2, String.valueOf(otp.charAt(1)));

        WebElement otpField3 = driverTools.waitForElementToAppear(OTP_TEXTFIELD3, 1).orElseThrow();
        driverTools.sendKeys(otpField3, String.valueOf(otp.charAt(2)));

        WebElement otpField4 = driverTools.waitForElementToBeClickable(OTP_TEXTFIELD4,1).orElseThrow();
        driverTools.click(OTP_TEXTFIELD4);
        driverTools.sendKeys(otpField4, String.valueOf(otp.charAt(3)));
    }

    public void loginToExtranet(String email, String password) {
        tryToLogin(email, password);
        Navbar<?> navbar = new Navbar<>(session);
        navbar.skipAndCloseAnyPopUp();
        String actualEmail = navbar.getDisplayedUserEmail().orElse("—");
        if (actualEmail.equals("—")) {
            actualEmail = email;
        }
        boolean emailMatch = actualEmail.equals(email);
        Assertion.assertThat("User has been logged in to extranet. Expected: " + email + " Actual: " + actualEmail, emailMatch, is(true));
    }

    public void openExtranetPage(String email, String password) {
        goToExtranetLandingPage();
        Navbar<?> navbar = new Navbar<>(session);
        if (!isExtranetLandingPageLoaded()) {
            navbar.skipAndCloseAnyPopUp();
            if (!navbar.isUserEmailPresented(email)) {
                navbar.clickLogoutMenu();
                clickNavbarLogin();
                loginToExtranet(email, password);
            }
        } else {
            clickNavbarLogin();
            loginToExtranet(email, password);
        }
    }

}
