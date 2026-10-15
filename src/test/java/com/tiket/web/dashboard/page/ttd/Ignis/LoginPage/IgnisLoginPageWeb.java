package com.tiket.web.dashboard.page.ttd.Ignis.LoginPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IgnisLoginPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisLoginPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisLoginPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisLoginPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisLoginPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator USER_PICTURE = new Locator("USER_PICTURE", By.xpath("//div[@class='header-options header-username']/div[@class='profile-picture']"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", By.xpath("//div[@class='logout']"));
    private static final Locator USERNAME_INPUT_TEXT = new Locator("USERNAME_INPUT_TEXT", By.xpath("//input[@name='username']"));
    private static final Locator PASSWORD_INPUT_TEXT = new Locator("PASSWORD_INPUT_TEXT", By.xpath("//input[@name='password']"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", By.xpath("//button[@class='btn btn-pill login-btn' and not(@disabled)]"));

    public void loginAsSpecificUser(String username, String password) {
        goToIgnisLoginPage();

        if (isIgnisLogin()) {
            logoutIgnis();
        }

        inputUsernamePassword(username, password);
        pressLoginButton();
    }

    private void pressLoginButton() {
        driverTools.waitForElementToAppear(LOGIN_BUTTON);
        driverTools.click(LOGIN_BUTTON);
    }

    public void loginAsAny(String username, String password) {
        if (!isIgnisLogin()) {
            loginAsSpecificUser(username, password);
        }
    }

    public void goToIgnisLoginPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {driver.get("https://gatotkaca.tiket.com/ignis/login");}
            case PREPROD -> {driver.get("https://preprod.tiket.com/ignis/login");}
            case PROD -> {driver.get("https://www.tiket.com/ignis/login");}
        }
    }

    private boolean isIgnisLogin() {
        driverTools.waitForElementToAppear(USER_PICTURE, 5);

        return driverTools.element(USER_PICTURE).isPresent();
    }

    public void logoutIgnis() {
        driverTools.click(USER_PICTURE);
        driverTools.click(LOGOUT_BUTTON);
    }

    private void inputUsernamePassword(String username, String password) {
        driverTools.waitForElementToAppear(USERNAME_INPUT_TEXT);

        driverTools.clearInput(USERNAME_INPUT_TEXT);
        driverTools.sendKeys(USERNAME_INPUT_TEXT, username);

        driverTools.clearInput(PASSWORD_INPUT_TEXT);
        driverTools.sendKeys(PASSWORD_INPUT_TEXT, password);
    }
}
