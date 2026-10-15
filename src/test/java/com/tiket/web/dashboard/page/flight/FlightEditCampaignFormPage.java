package com.tiket.web.dashboard.page.flight;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class FlightEditCampaignFormPage<T extends WebDriver> extends BasePage<T> {
    public FlightEditCampaignFormPage(Session<T> session) {
        super(session);
    }

    public static final Locator CAMPAIGN_NAME_FIELDS = new Locator("CAMPAIGN_NAME_FIELDS", new By.ByXPath("//input[@name='name']"));
    private static final Locator PRIORITY_FIELDS = new Locator("PRIORITY_FIELDS", new By.ByXPath("//input[@name='campaignPriority']"));
    private static final Locator CAMPAIGN_TEMPLATE_NAME_DROPDOWN = new Locator("CAMPAIGN_TEMPLATE_NAME_DROPDOWN", new By.ByXPath("//label[@for='templateId']/following-sibling::div/div"));
    private static final Locator CAMPAIGN_TEMPLATE_NAME_DROPDOWN_SELECTION = new Locator("CAMPAIGN_TEMPLATE_NAME_DROPDOWN_SELECTION", new By.ByXPath("//label[@for='templateId']/following::div/div[@class='Select-menu-outer']//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator MASTER_TAG_DROPDOWN = new Locator("MASTER_TAG_DROPDOWN", new By.ByXPath("//label[@for='tag']/following::div/div"));
    private static final Locator MASTER_TAG_DROPDOWN_SELECTION = new Locator("MASTER_TAG_DROPDOWN_SELECTION", new By.ByXPath("//label[@for='tag']/following::div/div[@class='Select-menu-outer']//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator CAMPAIGN_DATE_START_DATE_FIELDS = new Locator("CAMPAIGN_DATE_START_DATE_FIELDS", new By.ByXPath("(//div[contains(@class,'campaign-period-datepicker')])[1]//input"));
    private static final Locator CAMPAIGN_DATE_END_DATE_FIELDS = new Locator("CAMPAIGN_DATE_END_DATE_FIELDS", new By.ByXPath("(//div[contains(@class,'campaign-period-datepicker')])[2]//input"));
    private static final Locator CAMPAIGN_DATE_FULL_DAY_FIELDS = new Locator("CAMPAIGN_DATE_FULL_DAY_FIELDS", new By.ByXPath("//label[contains(@class,'date-allday-checkbox-form')]"));
    private static final Locator DATE_PICKER_NEXT_MONTH = new Locator("DATE_PICKER_NEXT_MONTH", new By.ByXPath("(//div[@class='MuiPickersCalendarHeader-switchHeader']/button)[2]"));
    private static final Locator DATE_PICKER_PREV_MONTH = new Locator("DATE_PICKER_PREV_MONTH", new By.ByXPath("(//div[@class='MuiPickersCalendarHeader-switchHeader']/button)[1]"));
    private static final Locator DATE_PICKER_OK_BUTTON = new Locator("DATE_PICKER_OK_BUTTON", new By.ByXPath("//button[./span[text()='OK']]"));
    private static final Locator ALL_DAY_BUDGET_CHECK = new Locator("ALL_DAY_BUDGET_CHECK", new By.ByXPath("//label[contains(@class,'check-all-date')]"));
    private static final Locator BUDGET_FOR_ALL_DATES_FIELDS = new Locator("BUDGET_FOR_ALL_DATES_FIELDS", new By.ByXPath("//input[@class='variable-for-all-field']"));
    private static final Locator APPLY_TO_ALL_DATE_BUTTON = new Locator("APPLY_TO_ALL_DATE_BUTTON", new By.ByXPath("//button[contains(@class,'MuiButton-containedPrimary')]"));
    private static final Locator CLEAR_ALL_BUDGET = new Locator("CLEAR_ALL_BUDGET", new By.ByXPath("//button[contains(@class,'MuiButton-containedSecondary')]"));
    private static final Locator TOTAL_BUDGET_TEXT = new Locator("TOTAL_BUDGET_TEXT", new By.ByXPath("//div[@class='total-budget']"));
    private static final Locator PLATFORM_DROPDOWN = new Locator("PLATFORM_DROPDOWN", new By.ByXPath("//label[@for='platforms']/following::div[1]/div"));
    private static final Locator CLEAR_PLATFORM_VALUE = new Locator("CLEAR_PLATFORM_VALUE", new By.ByXPath("//label[@for='platforms']/following::div[1]/div/span[@aria-label='Clear all']"));
    private static final Locator PLATFORM_DROPDOWN_SELECTION = new Locator("PLATFORM_DROPDOWN_SELECTION", new By.ByXPath("//label[@for='platforms']/following::div/div[@class='Select-menu-outer']//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator USER_TYPE_OPTIONS = new Locator("USER_TYPE_OPTIONS", new By.ByXPath("//div[@class='user-type']//label/span[2]"));
    private static final Locator LOYALTY_LEVEL_DROPDOWN = new Locator("LOYALTY_LEVEL_DROPDOWN", new By.ByXPath("//label[@for='loyaltyLevel']/following::div/div"));
    private static final Locator LOYALTY_LEVEL_DROPDOWN_SELECTION = new Locator("LOYALTY_LEVEL_DROPDOWN_SELECTION", new By.ByXPath("//label[@for='loyaltyLevel']/following::div/div[@class='Select-menu-outer']//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", new By.ByXPath("//button[@type='submit']"));
    private static final Locator SUBMIT_BUTTON_2 = new Locator("SUBMIT_BUTTON_2", new By.ByXPath("//div[@class='button']/button[contains(@class,'btn-primary')]"));

    public Map<String, String> editCampaignDetails(String priority, String campaignTemplateName, String masterTag, String campaignStartDateRange, String campaignDuration, String campaignBudget, String platform, String userType, String loyaltyLevel) {
        Map<String, String> editedCampaign = new HashMap<>();

        priority = editPriority(priority);
        editedCampaign.put("priority", priority);

        campaignTemplateName = chooseCampaignTemplateName(campaignTemplateName);
        editedCampaign.put("campaignTemplateName", campaignTemplateName);

        masterTag = chooseMasterTag(masterTag);
        editedCampaign.put("masterTag", masterTag);

        Map<String, String> campaignPeriod = editCampaignDate(campaignStartDateRange, campaignDuration);
        String startDate = campaignPeriod.get("startDate");
        String endDate = campaignPeriod.get("endDate");
        editedCampaign.put("startDate", startDate);
        editedCampaign.put("endDate", endDate);

        campaignBudget = editCampaignBudget(campaignBudget, campaignDuration);
        editedCampaign.put("campaignBudget", campaignBudget);

        platform = editPlatform(platform);
        editedCampaign.put("platform", platform);

        userType = chooseUserType(userType, loyaltyLevel);
        editedCampaign.put("userType", userType);

        return editedCampaign;
    }

    private String chooseUserType(String userType, String loyaltyLevel) {
        List<WebElement> userTypeOption = driverTools.elementsUntilExist(USER_TYPE_OPTIONS).orElseThrow();
        int targetIndex = -1;
        String targetUserType = "";
        switch (userType.toLowerCase()) {
            case "random" -> {
                targetIndex = CoreFunctions.generateRandomInt(0, userTypeOption.size()-1);
                targetUserType = driverTools.getText(userTypeOption.get(targetIndex));
                if (targetUserType.toLowerCase().contains("public")) {
                    targetUserType = "Public";
                }
            }
            case "public" -> {
                targetIndex = 0;
                targetUserType = "Public";
            }
            case "login" -> {
                targetIndex= 1;
                targetUserType = driverTools.getText(userTypeOption.get(targetIndex));
            }
            case "loyalty" -> {
                targetIndex = 2;
                targetUserType = driverTools.getText(userTypeOption.get(targetIndex));
                fillLoyaltyLevel(loyaltyLevel);
            }
            default -> {
                throw new NoSuchElementException("There is no '"+userType+"' user type");
            }
        }

        driverTools.click(userTypeOption.get(targetIndex));

        return targetUserType;
    }

    private void fillLoyaltyLevel(String loyaltyLevel) {
        driverTools.waitForElementToAppear(LOYALTY_LEVEL_DROPDOWN,1);
        driverTools.click(LOYALTY_LEVEL_DROPDOWN);

        List<WebElement> loyaltyLevelSelection = driverTools.elementsUntilExist(LOYALTY_LEVEL_DROPDOWN_SELECTION).orElseThrow();
        WebElement targetElement = null;
        if (loyaltyLevel.equalsIgnoreCase("random")) {
            int targetIndex = CoreFunctions.generateRandomInt(0,loyaltyLevelSelection.size()-1);
            targetElement = loyaltyLevelSelection.get(targetIndex);
        } else {
            for (WebElement e : loyaltyLevelSelection) {
                if (e.getText().equalsIgnoreCase(loyaltyLevel)) {
                    targetElement = e;
                    break;
                }
            }

            if (targetElement == null) {
                throw new NoSuchElementException("There is no '"+loyaltyLevel+"' loyalty level");
            }
        }

        driverTools.click(targetElement);
    }

    private String editPlatform(String platform) {
        driverTools.waitForElementToAppear(CLEAR_PLATFORM_VALUE,1);
        driverTools.click(CLEAR_PLATFORM_VALUE);

        driverTools.waitForElementToAppear(PLATFORM_DROPDOWN,1);
        driverTools.click(PLATFORM_DROPDOWN);

        List<WebElement> customerTargetSelection = driverTools.elementsUntilExist(PLATFORM_DROPDOWN_SELECTION).orElseThrow();
        WebElement targetElement = null;
        String targetElementText = "";
        if (platform.equalsIgnoreCase("random")) {
            int targetIndex = CoreFunctions.generateRandomInt(0,customerTargetSelection.size()-1);
            targetElement = customerTargetSelection.get(targetIndex);
            targetElementText += targetElement.getText();
        } else {
            for (WebElement e : customerTargetSelection) {
                if (e.getText().equalsIgnoreCase(platform)) {
                    targetElement = e;
                    targetElementText += targetElement.getText();
                    break;
                }
            }

            if (targetElement == null) {
                throw new NoSuchElementException("There is no '"+platform+"' platform type");
            }
        }

        driverTools.click(targetElement);
        return targetElementText;
    }

    private String editCampaignBudget(String campaignBudget, String campaignDuration) {
        driverTools.waitForElementToAppear(CLEAR_ALL_BUDGET,1);
        driverTools.click(CLEAR_ALL_BUDGET);

        driverTools.waitForElementToAppear(ALL_DAY_BUDGET_CHECK,1);
        driverTools.click(ALL_DAY_BUDGET_CHECK);

        driverTools.waitForElementToAppear(BUDGET_FOR_ALL_DATES_FIELDS,1);
        driverTools.sendKeys(BUDGET_FOR_ALL_DATES_FIELDS, String.valueOf(Math.round((float) Integer.parseInt(campaignBudget) /Integer.parseInt(campaignDuration))));

        driverTools.waitForElementToAppear(APPLY_TO_ALL_DATE_BUTTON,1);
        driverTools.click(APPLY_TO_ALL_DATE_BUTTON);

        double totalPrice = CoreFunctions.convertPrice(driverTools.getText(TOTAL_BUDGET_TEXT).orElseThrow());

        return String.valueOf(Math.round(totalPrice));
    }

    private Map<String, String> editCampaignDate(String campaignStartDateRange, String campaignDuration) {
        Map<String, String> result = new HashMap<>();
        String startDate = CoreFunctions.generateRandomDate(campaignStartDateRange);
        String endDate = CoreFunctions.addToDateYYYY_MM_DD(startDate, Integer.parseInt(campaignDuration));

        String currentStartDate = driverTools.getValue(CAMPAIGN_DATE_START_DATE_FIELDS).split(" ")[0];
        String currentEndDate = driverTools.getValue(CAMPAIGN_DATE_END_DATE_FIELDS).split(" ")[0];

        int monthDiffsStartDate = CoreFunctions.compareMonth(currentStartDate, startDate, "yyyy-MM-dd");
        int monthDiffsEndDate = CoreFunctions.compareMonth(currentEndDate, endDate, "yyyy-MM-dd");

        driverTools.waitForElementToAppear(CAMPAIGN_DATE_START_DATE_FIELDS,1);
        driverTools.click(CAMPAIGN_DATE_START_DATE_FIELDS);
        if (monthDiffsStartDate < 0) {
            for (int i = 0; i < monthDiffsStartDate * -1; i++) {
                driverTools.waitForElementToAppear(DATE_PICKER_NEXT_MONTH, 1);
                driverTools.click(DATE_PICKER_NEXT_MONTH);
            }
        } else if (monthDiffsStartDate > 0) {
            for (int i = 0; i < monthDiffsStartDate; i++) {
                driverTools.waitForElementToAppear(DATE_PICKER_PREV_MONTH, 1);
                driverTools.click(DATE_PICKER_PREV_MONTH);
            }
        }
        driverTools.waitForElementToAppear(getDatePicker(startDate.split("-")[2]));
        driverTools.click(getDatePicker(startDate.split("-")[2]));
        driverTools.waitForElementToAppear(DATE_PICKER_OK_BUTTON,1);
        driverTools.click(DATE_PICKER_OK_BUTTON);

        driverTools.waitForElementToAppear(CAMPAIGN_DATE_END_DATE_FIELDS,3);
        driverTools.click(CAMPAIGN_DATE_END_DATE_FIELDS);
        if (monthDiffsEndDate < 0) {
            for (int i = 0; i < monthDiffsEndDate * -1; i++) {
                driverTools.waitForElementToAppear(DATE_PICKER_NEXT_MONTH, 1);
                driverTools.click(DATE_PICKER_NEXT_MONTH);
            }
        } else if (monthDiffsEndDate > 0) {
            for (int i = 0; i < monthDiffsEndDate; i++) {
                driverTools.waitForElementToAppear(DATE_PICKER_PREV_MONTH, 1);
                driverTools.click(DATE_PICKER_PREV_MONTH);
            }
        }
        driverTools.waitForElementToAppear(getDatePicker(endDate.split("-")[2]));
        driverTools.click(getDatePicker(endDate.split("-")[2]));
        driverTools.waitForElementToAppear(DATE_PICKER_OK_BUTTON,1);
        driverTools.click(DATE_PICKER_OK_BUTTON);

        driverTools.waitForElementToAppear(CAMPAIGN_DATE_FULL_DAY_FIELDS,3);
        driverTools.click(CAMPAIGN_DATE_FULL_DAY_FIELDS);

        result.put("startDate", startDate);
        result.put("endDate", endDate);
        return result;
    }

    private Locator getDatePicker(String date) {
        return new Locator("DATE_PICKER", new By.ByXPath("(//button[contains(@class,'MuiPickersDay-day') and not(contains(@class,'hidden'))])["+date+"]"));
    }

    private String chooseMasterTag(String masterTag) {
        driverTools.waitForElementToAppear(MASTER_TAG_DROPDOWN,1);
        driverTools.click(MASTER_TAG_DROPDOWN);

        List<WebElement> masterTagSelection = driverTools.elementsUntilExist(MASTER_TAG_DROPDOWN_SELECTION).orElseThrow();
        WebElement targetElement = null;
        String targetElementText = "";
        if (masterTag.equalsIgnoreCase("random")) {
            int targetIndex = CoreFunctions.generateRandomInt(0,masterTagSelection.size()-1);
            targetElement = masterTagSelection.get(targetIndex);
            targetElementText += targetElement.getText();
        } else {
            for (WebElement e : masterTagSelection) {
                if (e.getText().equalsIgnoreCase(masterTag)) {
                    targetElement = e;
                    targetElementText += targetElement.getText();
                    break;
                }
            }

            if (targetElement == null) {
                throw new NoSuchElementException("There is no '"+masterTag+"' master tag");
            }
        }

        driverTools.click(targetElement);
        return targetElementText;
    }

    private String chooseCampaignTemplateName(String campaignTemplateName) {
        driverTools.waitForElementToAppear(CAMPAIGN_TEMPLATE_NAME_DROPDOWN,1);
        driverTools.click(CAMPAIGN_TEMPLATE_NAME_DROPDOWN);

        driverTools.waitForElementToAppear(CAMPAIGN_TEMPLATE_NAME_DROPDOWN_SELECTION);
        List<WebElement> campaignTemplateNameSelection = driverTools.elementsUntilExist(CAMPAIGN_TEMPLATE_NAME_DROPDOWN_SELECTION).orElseThrow();

        WebElement targetElement = null;
        String targetElementText = "";
        if (campaignTemplateName.equalsIgnoreCase("random")) {
            int targetIndex = CoreFunctions.generateRandomInt(0,campaignTemplateNameSelection.size()-1);
            targetElement = campaignTemplateNameSelection.get(targetIndex);
            targetElementText += targetElement.getText();
        } else {
            for (WebElement e : campaignTemplateNameSelection) {
                if (e.getText().equalsIgnoreCase(campaignTemplateName)) {
                    targetElement = e;
                    targetElementText += targetElement.getText();
                    break;
                }
            }

            if (targetElement == null) {
                throw new NoSuchElementException("There is no '"+campaignTemplateName+"' campaign template name");
            }
        }

        driverTools.click(targetElement);
        return targetElementText;
    }

    private String editPriority(String priority) {
        driverTools.clearInput(PRIORITY_FIELDS);
        driverTools.sendKeys(PRIORITY_FIELDS, priority);
        return driverTools.getValue(PRIORITY_FIELDS);
    }

    public void clickSubmit() {
        driverTools.waitForElementToAppear(SUBMIT_BUTTON,1);
        driverTools.click(SUBMIT_BUTTON);

        if (driverTools.waitForElementToAppear(SUBMIT_BUTTON_2,1).isPresent()) {
            driverTools.click(SUBMIT_BUTTON_2);
        }
    }
}
