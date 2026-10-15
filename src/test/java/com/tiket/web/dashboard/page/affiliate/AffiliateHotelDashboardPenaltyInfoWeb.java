package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.*;

import java.util.List;

public class AffiliateHotelDashboardPenaltyInfoWeb<T extends WebDriver> extends WebPage<T> {
    public AffiliateHotelDashboardPenaltyInfoWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AffiliateHotelDashboardPenaltyInfoWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new AffiliateHotelDashboardPenaltyInfoDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new AffiliateHotelDashboardPenaltyInfoMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("//button[text()='Edit']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[text()='Save']"));
    private static final Locator REASON_DROPDOWN = new Locator("REASON_DROPDOWN", By.xpath("(//div[contains(@class, 'InputSearch_select_search')])[1]"));
    private static final Locator REASON_DROPDOWN_SELECTION =new Locator("REASON_DROPDOWN_SELECTION", By.xpath("//div[@role='listbox']/div"));
    private static final Locator REASON_DROPDOWN_VALUE  = new Locator("REASON_DROPDOWN_VALUE", By.xpath("(//div[contains(@class, 'InputSearch_select_search')])[1]/div/div[1]"));
    private static final Locator SAVE_CHANGES_MODAL = new Locator("SAVE_CHANGES_MODAL", By.xpath("//button[text()='Save changes']"));
    private static final Locator REASON_VALUE_TABLE = new Locator("REASON_VALUE_TABLE", By.xpath("//table/tbody/tr/td[2]"));

    public void clickEditButton() {
        driverTools.waitForElementToAppear(EDIT_BUTTON, 2);
        driverTools.click(EDIT_BUTTON);
    }

    public void clickSave() {
        driverTools.scrollElementToCenter(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    public void selectReason(String reason) {
        driverTools.click(REASON_DROPDOWN);

        List<WebElement> selections = driverTools.elementsUntilExist(REASON_DROPDOWN_SELECTION).orElseThrow();
        for (WebElement selection : selections) {
            String elementText = driverTools.getText(selection);
            if (elementText.equalsIgnoreCase(reason)) {
                try {
                    driverTools.click(selection);
                } catch (ElementClickInterceptedException | StaleElementReferenceException er1) {
                    driverTools.clickElementByCoordinateDWeb(selection);
                }
                break;
            }
        }
    }

    public String getReasonValue() {
        driverTools.waitForElementToAppear(REASON_DROPDOWN_VALUE);
        return driverTools.getText(REASON_DROPDOWN_VALUE).get();
    }

    public void clickSaveChangesConfirmationModal() {
        driverTools.waitForElementToAppear(SAVE_CHANGES_MODAL, 3);
        driverTools.click(SAVE_CHANGES_MODAL);
    }

    public String getReasonFromDetailPage() {
        driverTools.refreshWebpage();
        return driverTools.getText(REASON_VALUE_TABLE).get();
    }
}
