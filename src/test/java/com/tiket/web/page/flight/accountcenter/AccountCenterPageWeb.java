package com.tiket.web.page.flight.accountcenter;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.flight.accountcenter.profile.ProfilePageWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.core.Is.is;

public  class AccountCenterPageWeb <T extends WebDriver> extends WebPage<T> {

    public AccountCenterPageWeb(Session<T> session) {
            super(session);
        }
        public static <T extends WebDriver> AccountCenterPageWeb<T> getInstance(Session<T> session) {
            switch (Configs.PLATFORM) {
                case DWEB -> {
                    return new AccountCenterPageDWEB<>(session);
                }
                case AMWEB, IMWEB -> {
                    return new AccountCenterPageMWEB<>(session);
                }
                default -> throw new UnsupportedOperationException("Platform not supported");
            }
        }

    private static final Locator TO_ACCOUNT_CENTER = new Locator("TO_ACCOUNT_CENTER", By.xpath("//span[text()='To Account Center']"));
    private final Locator EDIT_BTN = new Locator("EDIT_BTN", By.xpath("//span[contains(@class,'pointer Text_text')]"));
    public void clickToAccountCenter() {
        driverTools.waitForElementToAppear(TO_ACCOUNT_CENTER);
        driverTools.clickWithJavascriptExecutor(TO_ACCOUNT_CENTER);
    }

    public String updateEmail(String oldEmail){
        String newEmail = CoreFunctions.randomEmail().toLowerCase();
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("account");
          driverTools.click(EDIT_BTN);
        ProfilePageWeb<T> profilePage = ProfilePageWeb.getInstance(session);
        boolean isEmailChangeSuccess = profilePage.verifyAndChangeEmail(oldEmail.toLowerCase(),newEmail);
        Assertion.assertThat("is email change success for email :" + newEmail ,isEmailChangeSuccess,is(true));
        return newEmail;
    }
}
