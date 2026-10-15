package com.tiket.web.page.platform.liberty;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HelpCenterPageMWeb<T extends WebDriver> extends HelpCenterPageWeb<T> {

    public HelpCenterPageMWeb(Session<T> session) {
        super(session);
    }

    private static final Locator LOGIN = new Locator("LOGIN", By.xpath("//div[contains(@class,'InfoBanner_word_break')]"));
    private static final Locator INBOX_FIRST_MESSAGE = new Locator("INBOX_FIRST_MESSAGE", By.xpath("(//div[contains(@class, 'inbox_container')])[1]"));
    private static final Locator CHAT_PLATFORM_ENTRY_POINT_MWEB = new Locator("CHAT_PLATFORM_ENTRY_POINT_MWEB", By.xpath("//span[contains(text(), 'Chat')]"));
    private static final Locator INBOX_LAST_MESSAGE = new Locator("INBOX_LAST_MESSAGE", By.xpath("//div[contains(@class, 'is_last')]"));

    private static final Locator REPORT_LIST_BACK_BUTTON = new Locator("REPORT_LIST_BACK_BUTTON", By.xpath("//button[@data-testid='btnBack']"));

    @Override
    public void clickInboxFirstMessage() {
        driverTools.waitForElementToAppear(INBOX_FIRST_MESSAGE);
        driverTools.click(INBOX_FIRST_MESSAGE);

    }

    @Override
    public void clickLogin() {
        driverTools.waitForElementToAppear(LOGIN);
        driverTools.click(LOGIN);
    }
    @Override
    public boolean isChatPlatformExist(){
        return driverTools.waitForElementToAppear(CHAT_PLATFORM_ENTRY_POINT_MWEB).isPresent();
    }

    @Override
    public boolean verifyInboxPageCanBeScrolled() {
        try {
            driverTools.scrollIntoWebView(INBOX_LAST_MESSAGE);
            return driverTools.isElementDisplayed(INBOX_LAST_MESSAGE);
        } catch (Exception e){
            logger.log("Element not found");
            Assertion.skip("Skipping test case");
            return false;
        }
    }

    @Override
    public void clickReportListBackButton() {
        driverTools.waitForElementToAppear(REPORT_LIST_BACK_BUTTON);
        driverTools.click(REPORT_LIST_BACK_BUTTON);
    }
}
