package com.tiket.web.dashboard.page.refund;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RefundRulesPage<T extends WebDriver> extends BasePage<T> {
    public RefundRulesPage(Session<T> session) { super(session); }

    private static final Locator MENU_REFUND_RULES = new Locator("MENU_REFUND_RULES", new By.ByXPath("//span[contains(text(),'Rules')]"));
    private static final Locator TITLE_REFUND_RULES = new Locator("TITLE_REFUND_RULES", new By.ByXPath("//h1[contains(text(),'Rules')]"));
    private static final Locator FIRST_ACTION_BUTTON = new Locator("FIRST_ACTION_BUTTON", new By.ByXPath("(//div[@aria-roledescription='button for toggle menu'])[1]"));
    private static final Locator BUTTON_SEE_LOG = new Locator("BUTTON_SEE_LOG", new By.ByXPath("//div[@role='menuitem' and contains(., 'See Log')]"));
    private static final Locator TITLE_SEE_LOG = new Locator("TITLE_SEE_LOG", new By.ByXPath("//h2[contains(text(),'See Log')]"));
    private static final Locator MODAL_DATA_TABLE = new Locator("MODAL_DATA_TABLE", new By.ByXPath("//div[@data-testid='data-table']"));
    private static final Locator SEARCH_INPUT = new Locator("SEARCH_INPUT", new By.ByXPath("//input[@data-testid='filter-input-ruleName-field']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[contains(@class, 'Button_button') and .//span[text()='Search']]"));
    private static final Locator TITLE_RULE_DETAILS = new Locator("TITLE_RULE_DETAILS", new By.ByXPath("(//h2[contains(text(),'Details')])[1]"));
    private static final Locator RULE_NAME_VALUE_IN_DETAILS = new Locator("RULE_NAME_VALUE_IN_DETAILS", new By.ByXPath("//p[text()='Rule Name']/following-sibling::p"));

    private Locator ruleDetailsLocator(String ruleName) {
        return new Locator(
                "RULE_DETAILS_LOCATOR",
                new By.ByXPath(
                        "//tbody/tr[.//td[2][contains(normalize-space(.),'" + ruleName + "')]]"
                                + "//button[normalize-space(.)='Details']"
                )
        );
    }

    public void goToRefundRulesTab() {
        driverTools.waitForElementToAppear(MENU_REFUND_RULES).ifPresent(driverTools::click);
    }

    public boolean isOnRefundRulesTab() {
        driverTools.waitForElementToAppear(TITLE_REFUND_RULES);
        return driverTools.isElementDisplayed(TITLE_REFUND_RULES);
    }

    public void clickRefundRulesAction() {
        driverTools.waitForElementToAppear(FIRST_ACTION_BUTTON).ifPresent(driverTools::click);
    }

    public void clickRefundRulesSeeLog() {
        driverTools.waitForElementToAppear(BUTTON_SEE_LOG).ifPresent(driverTools::click);
    }

    public boolean isRefundSeeLogOpened() {
        driverTools.waitForElementToAppear(TITLE_SEE_LOG);
        boolean isTitleDisplayed = driverTools.isElementDisplayed(TITLE_SEE_LOG);

        driverTools.waitForElementToAppear(MODAL_DATA_TABLE);
        boolean isTableDisplayed = driverTools.isElementDisplayed(MODAL_DATA_TABLE);

        return isTitleDisplayed && isTableDisplayed;
    }
    public void inputSearchKeyword(String keyword) {
        WebElement input = driverTools.waitForElementToAppear(SEARCH_INPUT, 5).orElseThrow(() ->
                new IllegalStateException("Search input field was not found on the page."));
        driverTools.clickWithJavascriptExecutor(input);
        driverTools.clear(SEARCH_INPUT);
        driverTools.sendKeys(SEARCH_INPUT, keyword);
    }

    public void clickSearchButton() {
        WebElement searchBtn = driverTools.waitForElementToAppear(SEARCH_BUTTON, 5).orElseThrow(() ->
                new IllegalStateException("Search button was not found on the page."));
        driverTools.clickWithJavascriptExecutor(searchBtn);

    }

    public void clickDetailsByRuleName(String ruleName) {
        Locator detailsBtnLocator = ruleDetailsLocator(ruleName);
        WebElement detailsBtn = driverTools.waitForElementToAppear(detailsBtnLocator, 10).orElseThrow(() ->
                new IllegalStateException("Details button for rule '" + ruleName + "' was not found."));
        driverTools.clickWithJavascriptExecutor(detailsBtn);
    }

    public boolean isRuleDetailsDisplayed() {
        driverTools.waitForElementToAppear(TITLE_RULE_DETAILS, 10);
        return driverTools.isElementDisplayed(TITLE_RULE_DETAILS);
    }
    public String getRuleNameValueInDetails() {
        WebElement ruleNameElement = driverTools.waitForElementToAppear(RULE_NAME_VALUE_IN_DETAILS, 10).orElseThrow(() ->
                new IllegalStateException("Rule Name value element was not found in the Details modal."));
        return driverTools.getText(ruleNameElement).trim();
    }
}

