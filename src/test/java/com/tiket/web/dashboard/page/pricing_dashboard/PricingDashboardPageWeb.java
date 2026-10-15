package com.tiket.web.dashboard.page.pricing_dashboard;

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

    private static final Locator PRICING_DASHBOARD_LOGO = new Locator("PRICING_DASHBOARD_LOGO", By.xpath("//a[normalize-space(text())='Pricing Dashboard']"));
    private static final Locator PROFILE_MENU = new Locator("PROFILE_MENU", By.xpath("//header/div[2]//span[1]"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", By.xpath("//button[@aria-label='Toggle Menu Button']"));
    private static final Locator CRITERION_GROUP_MENU = new Locator("CRITERION_GROUP_MENU", By.xpath("//span[@title='Criterion Group']"));
    private static final Locator NUDGES_SYSTEM_MENU = new Locator("NUDGES_SYSTEM_MENU", By.xpath("//span[@title='Nudges System']"));
    private static final Locator ADD_NUDGE_BUTTON = new Locator("ADD_NUDGE_BUTTON", By.xpath("//button[text()='Add Nudge']"));
    
    // General Form Elements
    private static final Locator VERTICAL_DISTRIBUTION_CHANNEL = new Locator("VERTICAL_DISTRIBUTION_CHANNEL", By.xpath("//input[@id='vertical--distribution-channel']"));
    private static final Locator NUDGE_NAME_INPUT = new Locator("NUDGE_NAME_INPUT", By.xpath("//input[@name='name']"));
    private static final Locator SEARCH_BY_NUDGE_NAME = new Locator("SEARCH_BY_NUDGE_NAME", By.xpath("//input[@id='search-by-nudge-name']"));
    private static final Locator LAYER_INPUT = new Locator("LAYER_INPUT", By.xpath("//input[@id='layer']"));
    private static final Locator PRIORITY_INPUT = new Locator("PRIORITY_INPUT", By.xpath("//input[@name='priority']"));
    private static final Locator TARGET_ACTION_BUTTON = new Locator("TARGET_ACTION_BUTTON", By.xpath("//button[@data-testid='next-page-button']"));
    private static final Locator TESTING_OPTION = new Locator("TESTING_OPTION", By.xpath("//span[text()='TESTING']"));
    private static final Locator SRP_1_OPTION = new Locator("SRP_1_OPTION", By.xpath("//div[@title='SRP 1 (1)']"));
    private static final Locator ANDROID_CHIP = new Locator("ANDROID_CHIP", By.xpath("//button[.//span[text()='Android']]"));

    // Target & Action Section
    private static final Locator FUNNEL_BUTTON = new Locator("FUNNEL_BUTTON", By.xpath("//input[@id='funnel']"));
    private static final Locator SRP_OPTION = new Locator("SRP_OPTION", By.xpath("//span[text()='SRP']"));
    private static final Locator NUDGE_PLACEMENT_BUTTON = new Locator("NUDGE_PLACEMENT_BUTTON", By.xpath("//input[@id='nudge-placement']"));
    private static final Locator ABOVE_PRICE_OPTION = new Locator("ABOVE_PRICE_OPTION", By.xpath("//div[@title='Above Price']"));
    private static final Locator CONDITIONS_AND_SCHEDULE_BUTTON = new Locator("CONDITIONS_AND_SCHEDULE_BUTTON", By.xpath("//button[normalize-space()='Condition & Schedule']"));

    // Style Section Locators
    private static final Locator ICON_LABEL = new Locator("ICON_LABEL", By.xpath("//input[@id='icon']"));
    private static final Locator ICON_COLOR_LABEL = new Locator("ICON_COLOR_LABEL", By.xpath("//input[@id='icon-color']"));
    private static final Locator STYLE_LABEL = new Locator("STYLE_LABEL", By.xpath("//input[@id='style']"));
    private static final Locator COLOR_LABEL = new Locator("COLOR_LABEL", By.xpath("//input[@id='color']"));
    private static final Locator SIZE_LABEL = new Locator("SIZE_LABEL", By.xpath("//input[@id='size']"));
    
    //Metadata 
    private static final Locator CAMPAIGN_ID_INPUT = new Locator("CAMPAIGN_ID_INPUT", By.xpath("//input[@placeholder='Campaign ID']"));
    
    // Message Input Fields
    private static final Locator MESSAGE_INPUT_ENG = new Locator("MESSAGE_INPUT_ENG", By.xpath("//textarea[@placeholder='Message - English']"));
    private static final Locator MESSAGE_INPUT_INDO = new Locator("MESSAGE_INPUT_INDO", By.xpath("//textarea[@placeholder='Message - Indonesia']"));
    
    // Condition & Schedule Button
    private static final Locator STATUS = new Locator("STATUS", By.xpath("//input[@id='status']"));
    private static final Locator ONGOING_STATUS = new Locator("ONGOING_STATUS", By.xpath("//div[text()='Ongoing']"));
    private static final Locator CHANNEL_CONDITION_INPUT = new Locator("CHANNEL_CONDITION_INPUT", By.xpath("//input[@id='condition']"));
    private static final Locator CHANNEL_CONDITION = new Locator("CHANNEL_CONDITION", By.xpath("//span[text()='Channel']"));
    private static final Locator CHANNEL_INPUT = new Locator("CHANNEL_INPUT", By.xpath("//input[@placeholder='Channel']"));
    private static final Locator ALL_CHANNEL_OPTION = new Locator("ALL_CHANNEL_OPTION", By.xpath("//span[text()='All']"));
    private static final Locator ADD_MORE_CONDITIONS_BUTTON = new Locator("ADD_MORE_CONDITIONS_BUTTON", By.xpath("//button[normalize-space()='+ Add more condition']"));
    private static final Locator STORE_ID_CONDITION_INPUT = new Locator("STORE_ID_CONDITION_INPUT", By.xpath("//*[text()='Condition 2']/following::input[1]"));
    private static final Locator STORE_ID_CONDITION = new Locator("STORE_ID_CONDITION", By.xpath("//span[normalize-space(text())='Store']"));
    private static final Locator STORE_ID_INPUT = new Locator("STORE_ID_INPUT", By.xpath("//input[@placeholder='Store ID']"));
    private static final Locator TIKETCOM_OPTION = new Locator("TIKETCOM_OPTION", By.xpath("//*[normalize-space(text())='TIKETCOM']"));
    private static final Locator SUMMARY_BUTTON = new Locator("SUMMARY_BUTTON", By.xpath("//button[normalize-space()='Summary']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[normalize-space()='Save']"));
    private static final Locator CONFIRM_SAVE_MODAL_YES_BUTTON = new Locator("SAVE_CONFIRM_MODAL_YES_BUTTON", By.xpath("//button[normalize-space()='Yes, save']"));
    private static final Locator CONFIRM_DELETE_MODAL_YES_BUTTON = new Locator("SAVE_CONFIRM_MODAL_YES_BUTTON", By.xpath("//button[normalize-space()='Yes, delete']"));
    private static final Locator SEARCH_NUDGE_NAME = new Locator("SEARCH_NUDGE_NAME", By.xpath("//input[@name='ruleName']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator SETTINGS_BUTTON = new Locator("SETTINGS_BUTTON", By.xpath("//button[.//span[text()='Settings']]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("//span[text()='Edit']"));
    private static final Locator EDIT_FOUR_BUTTON = new Locator("EDIT_FOUR_BUTTON", By.xpath("(//button[text()='Edit'])[4]"));
    private static final Locator PAUSED_STATUS = new Locator("PAUSED_STATUS", By.xpath("//div[text()='Paused']"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", By.xpath("//span[text()='Delete']"));
    private static final Locator ON_GOING_STATUS_CHECK = new Locator("ON_GOING_STATUS", By.xpath("//span[text()='ON_GOING']"));
    private static final Locator PAUSED_STATUS_CHECK = new Locator("PAUSED_STATUS_CHECK", By.xpath("//span[text()='PAUSED']"));
    private static final Locator ALL_TIME_CHECK = new Locator("ALL_TIME_CHECK", By.xpath("//span[text()='All Time']"));
    private static final Locator NO_DATA_FOUND = new Locator("NO_DATA_FOUND", By.xpath("//td[normalize-space()='No data found']"));
    private static final Locator SCHEDULE_BUTTON = new Locator("SCHEDULE_BUTTON", By.xpath("//span[normalize-space()='Scheduled']"));
    private static final Locator CROSS_BUTTON = new Locator("CROSS_BUTTON", By.xpath("//button[@data-testid='modal-close-button']"));
    private static final Locator START_DATE_INPUT = new Locator("START_DATE_INPUT", By.xpath("//input[@id='start-date']"));
    private static final Locator END_DATE_INPUT = new Locator("END_DATE_INPUT", By.xpath("//input[@id='end-date']"));
    private static final Locator TODAY_BUTTON = new Locator("TODAY_BUTTON", By.xpath("//small[normalize-space(.)='Today']"));
    private static final Locator LAST_DAY_OF_MONTH_BUTTON = new Locator("LAST_DAY_OF_MONTH_BUTTON", By.xpath("(//button[contains(@class,'Day_day')][.//span[@aria-label]])[last()]"));

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
        driverTools.dummyWait(1);
        driverTools.click(NUDGES_SYSTEM_MENU);
        driverTools.dummyWait(1);
    }

    public void logout() {
        driverTools.dummyWait(1);
        driverTools.click(PROFILE_MENU);
        driverTools.click(LOGOUT_BUTTON);
        driverTools.dummyWait(1);
    }

    public boolean isPricingDashboardLoaded() {
        driverTools.dummyWait(1);
        driverTools.waitForElementToAppear(PRICING_DASHBOARD_LOGO, 3);
        return driverTools.getTitle().contains("Pricing") && driverTools.isElementDisplayed(PRICING_DASHBOARD_LOGO);
    }

    public void clickAddNudgeButton() {
        driverTools.dummyWait(1);
        driverTools.click(ADD_NUDGE_BUTTON);
        driverTools.dummyWait(1);
    }

    public void selectVerticalDistributionChannel(String vertical) {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(VERTICAL_DISTRIBUTION_CHANNEL);
        driverTools.sendKeys(VERTICAL_DISTRIBUTION_CHANNEL, vertical);
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(TESTING_OPTION);
    }

    public void inputNudgeName(String name) {
        driverTools.dummyWait(1);
        driverTools.click(NUDGE_NAME_INPUT);
        driverTools.clearInput(NUDGE_NAME_INPUT);
        driverTools.sendKeys(NUDGE_NAME_INPUT, name);
    }

    public void selectLayer(String layer) {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(LAYER_INPUT);
        driverTools.sendKeys(LAYER_INPUT, layer);
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(SRP_1_OPTION);
        driverTools.dummyWait(1);
    }

    public void inputPriority(String priority) {
        driverTools.dummyWait(1);
        driverTools.click(PRIORITY_INPUT);
        driverTools.clearInput(PRIORITY_INPUT);
        driverTools.sendKeys(PRIORITY_INPUT, priority);
    }

    public void clickAndroidPlatform() {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(ANDROID_CHIP);
    }

    public void clickTargetAndActionButton() {
        driverTools.dummyWait(1);
        driverTools.scrollElementToCenter(TARGET_ACTION_BUTTON);
        driverTools.click(TARGET_ACTION_BUTTON);
        driverTools.dummyWait(2);
    }

    public void clickFunnelButton() {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(FUNNEL_BUTTON);
    }

    public void clickSRPOption() {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(SRP_OPTION);
    }

    public void clickNudgePlacementButton() {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(NUDGE_PLACEMENT_BUTTON);
    }

    public void clickAbovePriceOption() {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(ABOVE_PRICE_OPTION);
    }

    public void clickIconField(String icon) {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(ICON_LABEL);
        driverTools.sendKeys(ICON_LABEL, icon);
        Locator iconOption = new Locator("ICON_OPTION_" + icon, By.xpath("//*[contains(text(), " + safeXpathText(icon) + ")]"));
        driverTools.clickWithJavascriptExecutor(iconOption);
    }

    public void clickIconColorField(String iconColor) {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(ICON_COLOR_LABEL);
        driverTools.sendKeys(ICON_COLOR_LABEL, iconColor);
        Locator iconColorOption = new Locator("ICON_COLOR_OPTION_" + iconColor, By.xpath("//span[text()='" + iconColor + "']"));
        driverTools.clickWithJavascriptExecutor(iconColorOption);
    }

    public void clickStyleField(String style) {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(STYLE_LABEL);
        driverTools.sendKeys(STYLE_LABEL, style);
        Locator styleOption = new Locator("STYLE_OPTION_" + style, By.xpath("//span[text()='" + style + "']"));
        driverTools.clickWithJavascriptExecutor(styleOption);
    }

    public void clickColorField(String color) {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(COLOR_LABEL);
        driverTools.sendKeys(COLOR_LABEL, color);
        Locator colorOption = new Locator("COLOR_OPTION_" + color, By.xpath("//*[contains(text(), '" + color + "')]"));
        driverTools.clickWithJavascriptExecutor(colorOption);
    }

    public void clickSizeField(String size) {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(SIZE_LABEL);
        driverTools.sendKeys(SIZE_LABEL, size);
        Locator sizeOption = new Locator("SIZE_OPTION_" + size, By.xpath("//span[text()='" + size + "']"));
        driverTools.clickWithJavascriptExecutor(sizeOption);
    }

    public void fillStyleForm(String icon, String iconColor, String style, String color, String size) {
        driverTools.dummyWait(1);
        clickIconField(icon);
        clickIconColorField(iconColor);
        clickStyleField(style);
        clickColorField(color);
        clickSizeField(size);
    }

    public void fillTargetAndActionForm() {
        driverTools.dummyWait(1);
        clickFunnelButton();
        clickSRPOption();
        clickNudgePlacementButton();
        clickAbovePriceOption();
    }

    public void inputCampaignId(String campaignId) {
        driverTools.dummyWait(1);
        driverTools.click(CAMPAIGN_ID_INPUT);
        driverTools.clearInput(CAMPAIGN_ID_INPUT);
        driverTools.sendKeys(CAMPAIGN_ID_INPUT, campaignId);
    }

    public void enterMessageEng(String message) {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(MESSAGE_INPUT_ENG);
        driverTools.clearInput(MESSAGE_INPUT_ENG);
        driverTools.sendKeys(MESSAGE_INPUT_ENG, message);
    }

    public void enterMessageIndo(String message) {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(MESSAGE_INPUT_INDO);
        driverTools.clearInput(MESSAGE_INPUT_INDO);
        driverTools.sendKeys(MESSAGE_INPUT_INDO, message);
    }

    public void enterMessage(String message) {
        driverTools.dummyWait(1);
        enterMessageIndo(message);
        enterMessageEng(message);
    }

    public void clickConditionsAndScheduleButton() {
        driverTools.dummyWait(1);
        driverTools.click(CONDITIONS_AND_SCHEDULE_BUTTON);
    }

    public void clickOnOngoingStatus() {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(STATUS);
        driverTools.clickWithJavascriptExecutor(ONGOING_STATUS);
    }

    public void fillChannelCondition() {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(CHANNEL_CONDITION_INPUT);
        driverTools.sendKeys(CHANNEL_CONDITION_INPUT, "Channel");
        driverTools.clickWithJavascriptExecutor(CHANNEL_CONDITION);
        driverTools.click(CHANNEL_INPUT);
        driverTools.clickWithJavascriptExecutor(ALL_CHANNEL_OPTION);
    }

    private static String safeXpathText(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        } else if (!value.contains("\"")) {
            return "\"" + value + "\"";
        } else {
            StringBuilder sb = new StringBuilder("concat(");
            String[] parts = value.split("'", -1);
            for (int i = 0; i < parts.length; i++) {
                if (i > 0) sb.append(",\"'\",");
                sb.append("'").append(parts[i]).append("'");
            }
            sb.append(")");
            return sb.toString();
        }
    }

    public void fillStoreCondition(String storeId) {
        driverTools.waitForElementToAppear(STORE_ID_CONDITION_INPUT, 5);
        driverTools.clickWithJavascriptExecutor(STORE_ID_CONDITION_INPUT);
        driverTools.sendKeys(STORE_ID_CONDITION_INPUT, "Store");
        driverTools.waitForElementToAppear(STORE_ID_CONDITION, 5);
        driverTools.clickWithJavascriptExecutor(STORE_ID_CONDITION);
        driverTools.click(STORE_ID_INPUT);
        driverTools.sendKeys(STORE_ID_INPUT, storeId);
        Locator storeValueOption = new Locator("STORE_VALUE_OPTION_" + storeId, By.xpath("//*[text()=" + safeXpathText(storeId) + "]"));
        driverTools.waitForElementToAppear(storeValueOption, 5);
        driverTools.clickWithJavascriptExecutor(storeValueOption);
        driverTools.clickWithJavascriptExecutor(TIKETCOM_OPTION);
    }

    public void fillConditionsAndScheduleForm(String storeId) {
        driverTools.dummyWait(1);
        clickOnOngoingStatus();
        fillChannelCondition();
        driverTools.click(ADD_MORE_CONDITIONS_BUTTON);
        fillStoreCondition(storeId);
    }

    public void fillConditionsAndScheduleFormWithPausedStatus(String storeId) {
        driverTools.dummyWait(1);
        clickOnPausedStatus();
        fillChannelCondition();
        fillStoreCondition(storeId);
        driverTools.click(ADD_MORE_CONDITIONS_BUTTON);
    }

    public void clickSummaryButton() {
        driverTools.dummyWait(1);
        driverTools.click(SUMMARY_BUTTON);
    }

    public void clickSaveButton() {
        driverTools.dummyWait(1);
        driverTools.click(SAVE_BUTTON);
    }

    public void clickSaveConfirmModalYesButton() {
        driverTools.dummyWait(1);
        driverTools.click(CONFIRM_SAVE_MODAL_YES_BUTTON);
        driverTools.dummyWait(3);
    }

    public void inputSearchNudgeName(String nudgeName) {
        driverTools.dummyWait(1);
        driverTools.click(SEARCH_BY_NUDGE_NAME);
        driverTools.sendKeys(SEARCH_BY_NUDGE_NAME, nudgeName);
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean isNudgeNameDisplayed(String nudgeName) {
        driverTools.dummyWait(1);
        return !driverTools.isElementDisplayed(NO_DATA_FOUND);
    }

    public void clickDeleteConfirmModalYesButton() {
        driverTools.dummyWait(1);
        driverTools.click(CONFIRM_DELETE_MODAL_YES_BUTTON);
        driverTools.dummyWait(3);
    }

    public void clearSearchNudgeName(String nudgeName) {
        driverTools.dummyWait(1);
        driverTools.click(SEARCH_NUDGE_NAME);
        driverTools.clearInput(SEARCH_NUDGE_NAME);
        driverTools.click(SEARCH_BUTTON);
    }

    public void clickSettingsButton() {
        driverTools.dummyWait(1);
        driverTools.click(SETTINGS_BUTTON);
    }

    public void clickEditButton() {
        driverTools.dummyWait(1);
        driverTools.click(EDIT_BUTTON);
    }

    public void clickEditFourButton() {
        driverTools.dummyWait(1);
        driverTools.click(EDIT_FOUR_BUTTON);
    }

    public void clickOnPausedStatus() {
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(STATUS);
        driverTools.clickWithJavascriptExecutor(PAUSED_STATUS);
    }

    public void clickDeleteButton() {
        driverTools.dummyWait(1);
        driverTools.click(DELETE_BUTTON);
    }

    public boolean isOnGoingStatusChecked() {
        driverTools.dummyWait(1);
        return driverTools.isElementDisplayed(ON_GOING_STATUS_CHECK);
    }

    public boolean isAllTimeChecked() {
        driverTools.dummyWait(1);
        return driverTools.isElementDisplayed(ALL_TIME_CHECK);
    }

    public boolean isPausedStatusChecked() {
        driverTools.dummyWait(1);
        return driverTools.isElementDisplayed(PAUSED_STATUS_CHECK);
    }

    public void clickOnSchedule() {
        driverTools.dummyWait(1);
        driverTools.click(SCHEDULE_BUTTON);
    }

    public void selectToday() {
        driverTools.dummyWait(1);
        driverTools.click(START_DATE_INPUT);
        driverTools.click(TODAY_BUTTON);
        driverTools.click(CROSS_BUTTON);
        driverTools.dummyWait(1);
    }

    public void selectLastDayOfMonth() {
        driverTools.dummyWait(1);
        driverTools.click(END_DATE_INPUT);
        driverTools.click(LAST_DAY_OF_MONTH_BUTTON);
        driverTools.click(CROSS_BUTTON);
        driverTools.dummyWait(1);
    }

}
