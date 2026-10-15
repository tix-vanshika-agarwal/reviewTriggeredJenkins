package com.tiket.web.dashboard.page.ttd.CMS.CMSLoginPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CMSLoginPageWeb<T extends WebDriver> extends WebPage<T> {
    public CMSLoginPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSLoginPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSLoginPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSLoginPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator EXPAND_LOGOUT_BUTTON = new Locator("EXPAND_LOGOUT_BUTTON", By.xpath("//div[contains(@class,'HeaderAccountInfo_email_container__')]//span"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", By.xpath("//a[contains(@href,'logout')]"));
    private static final Locator USERNAME_INPUT_TEXT = new Locator("USERNAME_INPUT_TEXT", By.xpath("//input[@id='sb-input-username']"));
    private static final Locator PASSWORD_INPUT_TEXT = new Locator("PASSWORD_INPUT_TEXT", By.xpath("//input[@id='sb-input-password']"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", By.xpath("//button[@type='submit']"));
    private static final Locator USERNAME_HOMEPAGE = new Locator("USERNAME_HOMEPAGE", By.xpath("//div[contains(@class,'HeaderAccountInfo_email__')]"));

    /**
     * Consolidated CMS login: authenticates the member-core admin via session-cookie injection
     * ({@link MemberLoginPage#adminLoginViaCookie}) instead of the {@code /admin/member/v2/login} UI
     * form. CMS shares the same member-core session, so the injected {@code .tiket.com} cookies are
     * sufficient to reach the CMS content pages.
     */
    public void loginAsSpecificUser(String username, String password) {
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);
    }

    /** Original UI form-based CMS login, retained for the one end-to-end UI smoke test. */
    public void loginAsSpecificUserViaUI(String username, String password) {
        goToCMSLoginPage();

        if (isCMSLogin() && !isUserSame(username)) {
            logoutCMS();
            goToCMSLoginPage();
        }

        if (!isCMSLogin()){
            inputUsernamePassword(username, password);
            pressLoginButton();
        }
    }

    private boolean isUserSame(String username) {
        driverTools.waitForElementToAppear(USERNAME_HOMEPAGE);
        return driverTools.getText(USERNAME_HOMEPAGE).get().equalsIgnoreCase(username);
    }

    private void pressLoginButton() {
        driverTools.waitForElementToAppear(LOGIN_BUTTON);
        driverTools.click(LOGIN_BUTTON);
        driverTools.waitForElementToAppear(USERNAME_HOMEPAGE);
    }

    public void goToCMSLoginPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {driver.get("https://gatotkaca.tiket.com/admin/member/v2/login");}
            case PREPROD -> {driver.get("https://preprod.tiket.com/admin/member/v2/login");}
            case PROD -> {driver.get("https://www.tiket.com/admin/member/v2/login");}
        }
        driverTools.waitForWebToLoad();
    }

    private boolean isCMSLogin() {
        try {
            driverTools.waitForElementToAppear(USERNAME_HOMEPAGE, 3);
            return driverTools.element(USERNAME_HOMEPAGE).isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    public void logoutCMS() {
        goToCMSLoginPage();

        driverTools.waitForElementToAppear(EXPAND_LOGOUT_BUTTON);
        driverTools.click(EXPAND_LOGOUT_BUTTON);
        driverTools.waitForElementToAppear(LOGOUT_BUTTON);
        driverTools.clickElementByCoordinateDWeb(LOGOUT_BUTTON);

        driverTools.waitForElementToAppear(USERNAME_INPUT_TEXT,5);
    }

    private void inputUsernamePassword(String username, String password) {
        driverTools.waitForElementToAppear(USERNAME_INPUT_TEXT);

        if (driverTools.element(USERNAME_INPUT_TEXT).isEmpty()) {
            throw new RuntimeException("Username input not found");
        }

        driverTools.clearInput(USERNAME_INPUT_TEXT);
        driverTools.sendKeys(USERNAME_INPUT_TEXT, username);

        driverTools.clearInput(PASSWORD_INPUT_TEXT);
        driverTools.sendKeys(PASSWORD_INPUT_TEXT, password);
    }
}
