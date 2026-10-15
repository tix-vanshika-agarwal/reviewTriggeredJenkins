package com.tiket.web.dashboard.page.platform.pricing;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.NoSuchElementException;

public class PricingDashboardPageWeb<T extends WebDriver> extends WebPage<T> {
    public PricingDashboardPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> PricingDashboardPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new PricingDashboardPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new PricingDashboardPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator PROFILE_MENU = new Locator("PROFILE_MENU", By.xpath("//header/div[2]//span[1]"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", By.xpath("//button[@aria-label='Toggle Menu Button']"));
    private static final Locator CRITERION_GROUP_MENU = new Locator("CRITERION_GROUP_MENU", By.xpath("//span[@title='Criterion Group']"));
    private static final Locator NUDGES_SYSTEM_MENU = new Locator("NUDGES_SYSTEM_MENU", By.xpath("//span[@title='Nudges System']"));
    private static final Locator SUBSIDY_BUDGETING = new Locator("SUBSIDY_BUDGETING", By.xpath("//span[@title='Subsidy Budgeting']"));
    private static final Locator SETTINGS_BUTTON = new Locator("SETTINGS_BUTTON", By.xpath("(//button[.//span[normalize-space(text())='Settings']])[1]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("//div[@data-testid='action-button-option-undefined-Edit']"));
    private static final Locator QUOTA_INPUT = new Locator("QUOTA_INPUT", By.xpath("//input[@id='quota' and @name='quota']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[normalize-space(text())='Save']"));
    private static final Locator CONFIRM_SAVE_BUTTON = new Locator("CONFIRM_SAVE_BUTTON", By.xpath("//button[.//span[@data-testid='confirm-submit-button']]"));
    private static final Locator SUCCESS_MESSAGE = new Locator("SUCCESS_MESSAGE", By.xpath("//div[normalize-space(text())='Successfully edited the budget.']"));
    private static final Locator SEARCH_SUBSIDY = new Locator("SEARCH_SUBSIDY", By.xpath("//input[@id='search-by-budget-name']"));

    public void goToPage() {
        driverTools.dummyWait(3);
        switch (Configs.ENVIRONMENT) {
            case GK -> {driverTools.get("https://gatotkaca.tiket.com/admin/pricing-dashboard");}
            case PREPROD -> {driverTools.get("https://preprod.tiket.com/admin/pricing-dashboard");}
            case PROD -> {driverTools.get("https://www.tiket.com/admin/pricing-dashboard");}
            default -> {throw new NoSuchElementException("There is no '"+ Configs.ENVIRONMENT.name() +"' environment");}
        }
        driverTools.waitForWebToLoad();
    }

    public void clickCriterionGroupMenu() {
        driverTools.click(CRITERION_GROUP_MENU);
    }

    public void clickNudgesSystemMenu() {
        driverTools.click(NUDGES_SYSTEM_MENU);
    }

    public void logout() {
        driverTools.click(PROFILE_MENU);
        driverTools.click(LOGOUT_BUTTON);
    }

    public void searchSubsidyBudgeting(String subsidyName) {
        driverTools.waitForElementToAppear(SEARCH_SUBSIDY);
        driverTools.sendKeys(SEARCH_SUBSIDY, subsidyName);
    }

    public void clickSubsidyBudgetingMenu() {
        driverTools.waitForElementToAppear(SUBSIDY_BUDGETING);
        driverTools.click(SUBSIDY_BUDGETING);
    }

    public void clickSettingsButton() {
        driverTools.waitForElementToAppear(SETTINGS_BUTTON);
        driverTools.click(SETTINGS_BUTTON);
    }

    public void clickEditButton() {
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        driverTools.click(EDIT_BUTTON);
    }

    public void updateQuota(String quota) {
        driverTools.waitForElementToAppear(QUOTA_INPUT);
        driverTools.clearInput(QUOTA_INPUT);
        driverTools.sendKeys(QUOTA_INPUT, quota);
    }

    public void clickSaveButton() {
        driverTools.click(SAVE_BUTTON);
        driverTools.waitForElementToAppear(CONFIRM_SAVE_BUTTON);
        if(driverTools.isElementDisplayed(CONFIRM_SAVE_BUTTON)){
            driverTools.click(CONFIRM_SAVE_BUTTON);
        }
    }

    public boolean verifyBudgetUpdated() {
        driverTools.waitForElementToAppear(SUCCESS_MESSAGE);
        return  driverTools.isElementDisplayed(SUCCESS_MESSAGE);
    }
}
