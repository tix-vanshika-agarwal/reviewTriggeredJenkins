package com.tiket.web.page.platform.liberty;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HelpCenterPageDWeb<T extends WebDriver> extends HelpCenterPageWeb<T> {

    public HelpCenterPageDWeb(Session<T> session) {
        super(session);
    }

    private static final Locator LOGIN = new Locator("LOGIN", By.xpath("//*[contains(@class,'HeaderBottom-module')]//a[contains(@href,'login')]"));
    private static final Locator INBOX_FIRST_MESSAGE = new Locator("INBOX_FIRST_MESSAGE", By.xpath("(//div[@data-testid='inboxItemDesktop'])[1]"));
    private static final Locator FOOTER = new Locator("FOOTER", By.xpath("//footer"));

    @Override
    public void clickInboxFirstMessage() {
        driverTools.waitForElementToAppear(INBOX_FIRST_MESSAGE);
        driverTools.click(INBOX_FIRST_MESSAGE);
    }

    @Override
    public void clickLogin() {
        driverTools.scrollIntoWebView(LOGIN);
        driverTools.click(LOGIN);
    }

    @Override
    public boolean isChatPlatformExist(){
        return driverTools.waitForElementToAppear(CHAT_PLATFORM_ENTRY_POINT).isPresent();
    }

    @Override
    public boolean verifyInboxPageCanBeScrolled() {
        driverTools.scrollUntilElementFound(FOOTER);
        return driverTools.isElementDisplayed(FOOTER);
    }

    @Override
    public void clickReportListBackButton() {
        driverTools.waitForElementToAppear(REPORT_LIST_BACK_BUTTON);
        driverTools.click(REPORT_LIST_BACK_BUTTON);
    }
}
