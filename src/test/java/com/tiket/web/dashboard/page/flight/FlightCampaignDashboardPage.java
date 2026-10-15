package com.tiket.web.dashboard.page.flight;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tiket.web.dashboard.page.flight.FlightEditCampaignFormPage.CAMPAIGN_NAME_FIELDS;

public class FlightCampaignDashboardPage<T extends WebDriver> extends BasePage<T> {
    public FlightCampaignDashboardPage(Session<T> session) {
        super(session);
    }

    private static final Locator PRICE_ADJUSTMENT_DETAILS = new Locator("PRICE_ADJUSTMENT_DETAILS", By.xpath("(//span[contains(text(), 'See Price Adjustment Details')])[2]"));
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[@class='account-card']"));
    private static final Locator ADD_NEW_CAMPAIGN_BUTTON = new Locator("ADD_NEW_CAMPAIGN_BUTTON", new By.ByXPath("//*[@class='custom-table__add-button-container']/a/button"));
    private static final Locator TABLE_ROW_COUNT = new Locator("TABLE_ROW_COUNT", new By.ByXPath("//tbody/tr"));
    private static final Locator CAMPAIGN_NAMES = new Locator("CAMPAIGN_NAMES", new By.ByXPath("//tbody//td[1]//b"));
    private static final Locator PRIMARY_POPUP_BUTTON = new Locator("PRIMARY_POPUP_BUTTON", new By.ByXPath("//div[@class='button']/button[contains(@class,'btn-primary')]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", new By.ByXPath("//div[contains(@class,'MuiSnackbar-root')]//span"));

    public void clickPriceAdjustmentDetails() {
        try {
            if (Configs.ENVIRONMENT == Environment.GK) {
                driver.get("https://gatotkaca.tiket.com/admin/flight-ms-v2/campaign");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driver.get("https://preprod.tiket.com/admin/flight-ms-v2/campaign");
            } else if (Configs.ENVIRONMENT == Environment.PROD) {
                driver.get("https://www.tiket.com/admin/flight-ms-v2/campaign");
            }
            driverTools.waitForElementToAppear(PRICE_ADJUSTMENT_DETAILS, 5);
            driverTools.click(PRICE_ADJUSTMENT_DETAILS);
            logger.log("Clicked on Price adjustment details button successfully.");
        } catch (Exception e) {
            logger.fail("Failed to click Price adjustment details button.");
            throw e;
        }
    }

    public void clickPriceAdjustmentDetails(String campaignName) {
        Locator seePriceAdjustmentDetailsButtonLocator = getSeePriceAdjustmentDetailsButtonLocatorByCampaignName(campaignName);
        driverTools.waitForElementToAppear(seePriceAdjustmentDetailsButtonLocator).orElseThrow();
        driverTools.click(seePriceAdjustmentDetailsButtonLocator);
    }

    private Locator getSeePriceAdjustmentDetailsButtonLocatorByCampaignName(String campaignName) {
        return new Locator("SEE_PRICE_ADJUSTMENT_DETAILS_BUTTON", new By.ByXPath("(//tr[.//b[text()='"+ campaignName +"']]/td[last()]/div//button)[1]"));
    }

    public boolean isFlightLoggedIn() {
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(USERNAME_GREETING, 3);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public void goToFlightCampaignDashboardPage() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/flight-ms-v2/campaign");
            driverTools.get("https://gatotkaca.tiket.com/admin/flight-ms-v2/campaign");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/flight-ms-v2/campaign");
            driverTools.get("https://preprod.tiket.com/admin/flight-ms-v2/campaign");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driverTools.get("https://www.tiket.com/admin/flight-ms-v2/campaign");
            driverTools.get("https://www.tiket.com/admin/flight-ms-v2/campaign");
        }
    }

    public void clickAddNewCampaignBtn() {
        driverTools.waitForElementToAppear(ADD_NEW_CAMPAIGN_BUTTON, 3);
        driverTools.click(ADD_NEW_CAMPAIGN_BUTTON);
    }

    public Map<String, String> getDisplayedCampaignData(Map<String, String> createdCampaign) {
        Map<String, String> result = new HashMap<>();

        String createdCampaignName = createdCampaign.get("campaignName");
        int rowCountDisplayed = driverTools.elements(TABLE_ROW_COUNT).orElseThrow().size();
        int targetIndex = -1;

        for (int i = 1; i < rowCountDisplayed + 1; i++) {
            driverTools.waitForElementToAppear(getCampaignNameLocatorByIndex(i));
            String campaignName = driverTools.getText(getCampaignNameLocatorByIndex(i)).orElseThrow();
            if (campaignName.equalsIgnoreCase(createdCampaignName)) {
                result.put("campaignName", campaignName);
                targetIndex = i;
            }
        }

        result.put("campaignType", driverTools.getText(getCampaignTypeLocatorByIndex(targetIndex)).orElseThrow());
        result.put("platform", getPlatformTypeByIndex(targetIndex));
        result.put("userType", driverTools.getText(getUserTypeLocatorByIndex(targetIndex)).orElseThrow());
        result.put("campaignBudget", getCampaignBudgetByIndex(targetIndex));
        Map<String, String> campaignPeriod = getCampaignPeriodByIndex(targetIndex);
        result.put("startDate", campaignPeriod.get("startDate"));
        result.put("endDate", campaignPeriod.get("endDate"));
        result.put("priority", driverTools.getText(getPriorityLocatorByIndex(targetIndex)).orElseThrow());

        return result;
    }

    private Locator getPriorityLocatorByIndex(int index) {
        return new Locator("CAMPAIGN_PRIORITY_" + index, new By.ByXPath("((((//tbody/tr)[" + index + "]/td)[4]/div[@class='_text-center']/parent::*/following-sibling::*)[1])"));
    }

    private Map<String, String> getCampaignPeriodByIndex(int index) {
        Map<String, String> result = new HashMap<>();

        String campaignStartDate = driverTools.getText(getCampaignPeriodStartDateLocatorByIndex(index)).orElseThrow();
        String campaignEndDate = driverTools.getText(getCampaignPeriodEndDateLocatorByIndex(index)).orElseThrow();

        result.put("startDate", campaignStartDate);
        result.put("endDate", campaignEndDate);

        return result;
    }

    private String getCampaignBudgetByIndex(int index) {
        String campaignBudget = driverTools.getText(getCampaignBudgetLocatorByIndex(index)).orElseThrow();

        return String.valueOf(Math.round(CoreFunctions.convertPrice(campaignBudget)));
    }

    private String getPlatformTypeByIndex(int index) {
        List<String> platforms = new ArrayList<>();
        List<WebElement> platformElements = driverTools.elements(getPlatformLocatorByIndex(index)).orElseThrow();
        String result = "";

        for (WebElement e : platformElements) {
            String platform = driverTools.getText(e);
            platforms.add(platform);

            if (e == platformElements.get(platformElements.size() - 1)) {
                result += platform;
            } else {
                result += platform + ",";
            }
        }

        return result;
    }

    private Locator getCampaignPeriodEndDateLocatorByIndex(int index) {
        return new Locator("CAMPAIGN_PERIOD_END_DATE" + index, new By.ByXPath("(((//tbody/tr)[" + index + "]/td)[4]/div[@class='_text-center'])[2]"));
    }

    private Locator getCampaignPeriodStartDateLocatorByIndex(int index) {
        return new Locator("CAMPAIGN_PERIOD_START_DATE" + index, new By.ByXPath("(((//tbody/tr)[" + index + "]/td)[4]/div[@class='_text-center'])[1]"));
    }

    private Locator getCampaignBudgetLocatorByIndex(int index) {
        return new Locator("CAMPAIGN_BUDGET" + index, new By.ByXPath("((//tbody/tr)[" + index + "]/td)[3]//b[@class='table-sub-text']"));
    }

    private Locator getUserTypeLocatorByIndex(int index) {
        return new Locator("USER_TYPE_" + index, new By.ByXPath("((//tbody/tr)[" + index + "]/td)[2]//b[@class='table-text']"));
    }

    private Locator getPlatformLocatorByIndex(int index) {
        return new Locator("PLATFORM_" + index, new By.ByXPath("((//tbody/tr)[" + index + "]/td)[2]//li"));
    }

    private Locator getCampaignTypeLocatorByIndex(int index) {
        return new Locator("CAMPAIGN_TYPE_" + index, new By.ByXPath("(((//tbody/tr)[" + index + "]/td)[1]//span[@class='table-sub-text'])[1]"));
    }

    private Locator getCampaignNameLocatorByIndex(int index) {
        return new Locator("CAMPAIGN_NAME_" + index, new By.ByXPath("((//tbody/tr)[" + index + "]/td)[1]//b[@class='table-text']"));
    }

    public void deleteCampaign(String campaignName) {
        int index = getCampaignIndexByName(campaignName);

        driverTools.click(getOptionButtonLocatorByIndex(index));
        driverTools.click(getDeleteButtonLocatorByIndex(index));

        driverTools.click(PRIMARY_POPUP_BUTTON);
    }

    private List<WebElement> getCampaignNames() {
        driverTools.waitForElementsToAppear(CAMPAIGN_NAMES);
        return driverTools.elementsUntilExist(CAMPAIGN_NAMES).orElseThrow();
    }

    private Locator getOptionButtonLocatorByIndex(int index) {
        return new Locator("CAMPAIGN_OPTION_BUTTON_" + index, new By.ByXPath("((//tbody/tr)[" + index + "]/td)[last()]/button"));
    }

    private Locator getDeleteButtonLocatorByIndex(int index) {
        return new Locator("CAMPAIGN_DELETE_BUTTON_" + index, new By.ByXPath("((//tbody/tr)[" + index + "]/td)[last()]/button//div[@class='_btn-delete']"));
    }

    public Boolean isSuccessSnackBarDisplayed() {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR);
        return driverTools.getText(SUCCESS_SNACKBAR).get().equalsIgnoreCase("success");
    }

    public Boolean checkCampaignNameExist(String campaignName) {
        List<WebElement> campaignNames = getCampaignNames();
        for (int i = 0; i < campaignNames.size(); i++) {
            if (campaignName.equalsIgnoreCase(driverTools.getText(campaignNames.get(i)))) {
                return true;
            }
        }

        return false;
    }

    public Boolean checkDeletedCampaignNameExist(String campaignName) {
        driverTools.waitForElementToDisappear(CAMPAIGN_NAMES);
        List<WebElement> campaignNames = getCampaignNames();
        for (int i = 0; i < campaignNames.size(); i++) {
            if (campaignName.equalsIgnoreCase(driverTools.getText(campaignNames.get(i)))) {
                return true;
            }
        }

        return false;
    }

    private int getCampaignIndexByName(String campaignName) {
        int index = -1;
        List<WebElement> campaignNames = getCampaignNames();
        for (int i = 0; i < campaignNames.size(); i++) {
            if (campaignName.equalsIgnoreCase(driverTools.getText(campaignNames.get(i)))) {
                index = i;
                break;
            }
        }

        return index + 1;
    }

    private Locator getCampaignApprovalStatusLocator(int index) {
        return new Locator("CAMPAIGN_APPROVAL_STATUS_TEXT_" + index, new By.ByXPath("((//tbody/tr)[" + index + "]/td)[last()-1]/div[1]"));
    }

    public void goToEditPage(String campaignName) {
        int index = getCampaignIndexByName(campaignName);

        driverTools.click(getOptionButtonLocatorByIndex(index));
        driverTools.click(getEditButtonLocatorByIndex(index));

        driverTools.waitForElementToAppear(CAMPAIGN_NAME_FIELDS);
    }

    private Locator getEditButtonLocatorByIndex(int index) {
        return new Locator("CAMPAIGN_DELETE_BUTTON_" + index, new By.ByXPath("((//tbody/tr)[" + index + "]/td)[last()]/button//a[contains(@href,'/campaign/edit/')]/div"));
    }
}

