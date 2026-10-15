package com.tiket.web.dashboard.page.nft.car_rental;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CarRentalLoginPage <T extends WebDriver> extends BasePage<T> {
    private static final Locator LOGIN_FORM = new Locator("LOGIN_FORM", By.xpath("//img[@src='/external/assets/login-logo.png']"));
    private static final Locator USER_NAME = new Locator("USER_NAME", By.xpath("//input[@id='sb-input-username']"));
    private static final Locator PASSWORD = new Locator("PASSWORD", By.xpath("//input[@id='sb-input-password']"));
    private static final Locator LOGIN = new Locator("LOGIN", By.xpath("//button[@type='submit']"));

    public CarRentalLoginPage(Session<T> session) {
        super(session);
    }

    public void loginToCarRentalPage(String username, String password) {
        driverTools.waitForElementToAppear(LOGIN_FORM);
        driverTools.sendKeys(USER_NAME, username);
        driverTools.sendKeys(PASSWORD, password);
        driverTools.click(LOGIN);
    }
}