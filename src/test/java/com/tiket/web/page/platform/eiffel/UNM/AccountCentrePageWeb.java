package com.tiket.web.page.platform.eiffel.UNM;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Locator;
import org.openqa.selenium.By;
import com.tiket.web.WebPage;

public abstract class AccountCentrePageWeb<T extends WebDriver> extends WebPage<T>{

    public AccountCentrePageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AccountCentrePageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new AccountCentrePageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new AccountCentrePageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator CHANGE_PASSWORD = new Locator("CHANGE_PASSWORD", By.xpath("//span[contains(text(), 'Change Password')]"));
    private static final Locator CURRENT_PASSWORD_TEXT_BOX = new Locator("CURRENT_PASSWORD_TEXT_BOX", By.xpath("//input[@id='current-password']"));
    private static final Locator NEW_PASSWORD_TEXT_BOX = new Locator("NEW_PASSWORD_TEXT_BOX", By.xpath("//input[@id='new-password']"));
    private static final Locator CONFIRM_BUTTON = new Locator("CONFIRM_BUTTON", By.xpath("//button[text()='Confirm']"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", By.xpath("//button[text()='Lanjutkan' or text()='Continue']")) ;
    private static final Locator BRIDGE_LOCATOR = new Locator("BRIDGE_LOCATOR", By.xpath("//div[contains(@class,'bridge_card')]")) ;


    public void openChangePasswordPage() {
        driverTools.waitForElementsToAppear(CHANGE_PASSWORD).orElseThrow();
        driverTools.click(CHANGE_PASSWORD);
    }

    public void changePassword(String oldPassword, String newPassword) {
        driverTools.waitForElementsToAppear(CURRENT_PASSWORD_TEXT_BOX).orElseThrow();
        driverTools.sendKeys(CURRENT_PASSWORD_TEXT_BOX,oldPassword);
        driverTools.waitForElementToPresence(CONTINUE_BUTTON) ;
        driverTools.click(CONTINUE_BUTTON) ;
        driverTools.waitForElementsToAppear(NEW_PASSWORD_TEXT_BOX).orElseThrow();
        driverTools.sendKeys(NEW_PASSWORD_TEXT_BOX,newPassword);
        driverTools.click(CONFIRM_BUTTON);
    }

    public String verifyRedirection() {
        driverTools.waitForElementToPresence(BRIDGE_LOCATOR) ;
        String getUrl = driverTools.getCurrentURL();
        logger.log("Current URL->" + getUrl) ;
        return getUrl;
    }
}
