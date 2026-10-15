package com.tiket.web.dashboard.page.flight;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FlightPriceAdjustmentDetailPage<T extends WebDriver> extends BasePage<T> {
    public FlightPriceAdjustmentDetailPage(Session<T> session) {
        super(session);
    }

    private static final Locator ADD_NEW_ADJUSTMENT_BUTTON = new Locator("ADD_NEW_ADJUSTMENT_BUTTON", By.xpath("//span[contains(text(), 'Add New')]"));
    private static final Locator INFO_BTN = new Locator("INFO_BTN", By.xpath("(//button[contains(@class, 'MuiIconButton-root _tooltip')])[1]"));
    private static final Locator DELETE_BTN = new Locator("DELETE_BTN", By.xpath("//div[contains(@class, '_btn-delete')]"));
    private static final Locator ADJUSTMENT_STATUS = new Locator("ADJUSTMENT_STATUS", By.xpath("(//div[contains(@class, 'text-draft')])[1]"));
    private static final Locator ADJUSTMENT_STATUS_BTN = new Locator("ADJUSTMENT_STATUS_BTN", By.xpath("(//div[contains(@class, '_btn_status')])[1]"));
    private static final Locator CONFIRM_DELETE_BTN = new Locator("CONFIRM_DELETE_BTN", By.xpath("//span[contains(text(), 'OK')]"));
    private static final Locator DETAIL_BTN = new Locator("DETAIL_BTN", By.xpath("(//span[contains(text(),'Detail')])[1]"));
    private static final Locator AIRLINE_DETAILS = new Locator("AIRLINE_DETAILS", By.xpath("//h6[contains(text(),'Airline Details')]/../following-sibling::*[1]"));
    private static final Locator TRIP_DETAILS = new Locator("TRIP_DETAILS", By.xpath("//h6[contains(text(),'Trip Details')]/../following-sibling::*[1]"));
    private static final Locator PRICE_ADJUSTMENT_DETAILS = new Locator("PRICE_ADJUSTMENT_DETAILS", By.xpath("//h6[contains(text(),'Adjustment Rule')]/../following-sibling::*[1]"));
    private static final Locator DELETE_SUCCESS_MESSAGE = new Locator("DELETE_SUCCESS_MESSAGE", By.id("client-snackbar"));
    private static final Locator CAMPAIGN_START_DATE = new Locator("CAMPAIGN_START_DATE", By.xpath("(//table)[1]//td[1]"));
    private static final Locator CAMPAIGN_END_DATE = new Locator("CAMPAIGN_END_DATE", By.xpath("(//table)[1]//td[2]"));
    private static final Locator CAMPAIGN_PLATFORM = new Locator("CAMPAIGN_PLATFORM", By.xpath("(//table)[1]//td[3]"));
    private static final Locator CAMPAIGN_USER_TYPE = new Locator("CAMPAIGN_USER_TYPE", By.xpath("(//table)[1]//td[4]"));
    private static final Locator PRIMARY_POPUP_BUTTON = new Locator("PRIMARY_POPUP_BUTTON", new By.ByXPath("//div[@class='button']/button[contains(@class,'btn-primary')]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("//div[@data-testid='btn-delete']"));

    public void clickAddNewAdjustmentButton() {
        try {
            driverTools.waitForElementToAppear(ADD_NEW_ADJUSTMENT_BUTTON, 5);
            driverTools.click(ADD_NEW_ADJUSTMENT_BUTTON);
            logger.log("Clicked on Add New Adjustment button successfully.");
        } catch (Exception e) {
            logger.fail("Failed to click on Add New Adjustment button.");
            throw e;
        }
    }

    public void deleteAdjustment() {
        driverTools.waitForElementToAppear(INFO_BTN, 5);
        driverTools.click(INFO_BTN);
        driverTools.waitForElementToAppear(DELETE_BTN, 5);
        driverTools.click(DELETE_BTN);
        driverTools.waitForElementToAppear(CONFIRM_DELETE_BTN, 5);
        driverTools.click(CONFIRM_DELETE_BTN);
    }

    public boolean checkIfAdjustmentCreated(String status, String adjustmentBtn) {
        driverTools.waitForElementToAppear(ADJUSTMENT_STATUS, 5);
        String statusText = driverTools.getText(ADJUSTMENT_STATUS).orElseThrow();
        String adjustmentBtnText = driverTools.getText(ADJUSTMENT_STATUS_BTN).orElseThrow();
        return statusText.equals(status) && adjustmentBtnText.equals(adjustmentBtn);
    }

    public boolean checkDetailAdjustment(String supplier,String airlineClass,String airportOriginKey,String airportDestinationKey,String minPrice, String maxPrice, String minDiscount, String maxDiscount, String markupValue ){
        driverTools.waitForElementToAppear(DETAIL_BTN, 5);
        driverTools.click(DETAIL_BTN);
        String airlineDetailsText = driverTools.getText(AIRLINE_DETAILS).orElseThrow();
        String tripDetailsText = driverTools.getText(TRIP_DETAILS).orElseThrow();
        String priceAdjustmentDetailsText = driverTools.getText(PRICE_ADJUSTMENT_DETAILS).orElseThrow();
        driverTools.refreshWebpage();
        return airlineDetailsText.contains(supplier) && airlineDetailsText.contains(airlineClass.toUpperCase()) && tripDetailsText.contains(airportOriginKey) && tripDetailsText.contains(airportDestinationKey) && priceAdjustmentDetailsText.contains(minPrice) && priceAdjustmentDetailsText.contains(maxPrice) && priceAdjustmentDetailsText.contains(minDiscount) && priceAdjustmentDetailsText.contains(maxDiscount) && priceAdjustmentDetailsText.contains(markupValue);
    }

    public boolean isAdjustmentDeleted() {
        driverTools.waitForElementToAppear(DELETE_SUCCESS_MESSAGE,5);
        return driverTools.isElementDisplayed(DELETE_SUCCESS_MESSAGE);
    }

    public Map<String, String> extractCampaignData() throws InterruptedException {
        Map<String, String> displayedCampaignData = new HashMap<>();
        displayedCampaignData.put("startDate", getCampaignStartDate());
        displayedCampaignData.put("endDate", getCampaignEndDate());
        displayedCampaignData.put("platform", getCampaignPlatform());
        displayedCampaignData.put("userType", getCampaignUserType());

        return displayedCampaignData;
    }

    private String getCampaignStartDate() throws InterruptedException {
        driverTools.waitForElementToAppear(CAMPAIGN_START_DATE).orElseThrow();
        Optional<String> optionalText = driverTools.getText(CAMPAIGN_START_DATE);
        if (optionalText.isEmpty() || optionalText.get().isEmpty()) {
            for (int i = 0; i < 3; i++) {
                optionalText = driverTools.getText(CAMPAIGN_START_DATE);
                if (!(optionalText.isEmpty() || optionalText.get().isEmpty())) {
                    break;
                }
                Thread.sleep(500);
            }
        }
        return optionalText.get().split(" ")[0];
    }

    private String getCampaignEndDate() throws InterruptedException {
        driverTools.waitForElementToAppear(CAMPAIGN_END_DATE).orElseThrow();
        Optional<String> optionalText = driverTools.getText(CAMPAIGN_END_DATE);
        if (optionalText.isEmpty() || optionalText.get().isEmpty()) {
            for (int i = 0; i < 3; i++) {
                optionalText = driverTools.getText(CAMPAIGN_END_DATE);
                if (!(optionalText.isEmpty() || optionalText.get().isEmpty())) {
                    break;
                }
                Thread.sleep(500);
            }
        }
        return optionalText.get().split(" ")[0];
    }

    private String getCampaignPlatform() {
        WebElement targetElement = driverTools.waitForElementToAppear(CAMPAIGN_PLATFORM).orElseThrow();
        return driverTools.getText(targetElement);
    }

    private String getCampaignUserType() {
        WebElement targetElement = driverTools.waitForElementToAppear(CAMPAIGN_USER_TYPE).orElseThrow();
        return driverTools.getText(targetElement);
    }

    public void deleteCampaign() {
        driverTools.click(DELETE_BUTTON);

        driverTools.click(PRIMARY_POPUP_BUTTON);
    }
}
