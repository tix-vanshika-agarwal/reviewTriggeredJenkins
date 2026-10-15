package com.tiket.web.dashboard.page.platform.sphinx.insurance.credential;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InsuranceCredentialDashboardPageWeb<T extends WebDriver> extends WebPage<T> {

    public InsuranceCredentialDashboardPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> InsuranceCredentialDashboardPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new InsuranceCredentialDashboardPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new InsuranceCredentialDashboardPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Platform not supported");
        }
    }

    private static final Locator EXPAND_CHEVRON = new Locator("EXPAND_CHEVRON", By.xpath("(//div[contains(@class, 'credential_action_group_list')]//div[contains(@class, 'Tooltip_tooltip')][last()])[2]"));
    private static final Locator TOGGLE_STATUS = new Locator("TOGGLE_STATUS", By.xpath("(//div[contains(@class, 'Toggle_toggle')])[1]"));
    private static final Locator TOGGLE_ACTIVE = new Locator("TOGGLE_ACTIVE", By.xpath("//div[contains(@class, 'Toggle_toggle') and contains(@class, 'Toggle_toggle_small')]"));
    private static final Locator CONFIRMATION_POPUP = new Locator("CONFIRMATION_POPUP", By.xpath("//section[contains(@class, 'Modal_modal_popup')]"));
    private static final Locator ACTIVE_BUTTON = new Locator("ACTIVE_BUTTON", By.xpath("//button[normalize-space(.)='Active']"));
    private static final Locator DEACTIVATE_BUTTON = new Locator("DEACTIVATE_BUTTON", By.xpath("//button[contains(text(), 'Deactivate')]"));
    private static final Locator STATUS_TEXT = new Locator("STATUS_TEXT", By.xpath("(//span[contains(@class, 'Text_text') and contains(@class, 'Text_variant_highEmphasis')])[3]"));
    
    public void clickExpandChevronSecond() {
        driverTools.dummyWait(3);
        driverTools.click(EXPAND_CHEVRON);
        driverTools.dummyWait(2);
    }

    public boolean isToggleActive() {
        return driverTools.isElementPresent(TOGGLE_ACTIVE);
    }

    public void clickToggleStatus() {
        driverTools.click(TOGGLE_STATUS);
        driverTools.dummyWait(2);
    }

    public boolean isConfirmationPopupVisible() {
        return driverTools.isElementPresent(CONFIRMATION_POPUP);
    }

    public void clickActivateButton() {
        driverTools.click(ACTIVE_BUTTON);
        driverTools.dummyWait(2);
    }

    public void clickDeactivateButton() {
        if (driverTools.isElementPresent(CONFIRMATION_POPUP)) {
            driverTools.click(DEACTIVATE_BUTTON);
            driverTools.dummyWait(2);
        }
    }

    public void toggleCredentialStatusToOn() {
        String status = driverTools.getText(STATUS_TEXT).orElse("").trim();

        if (status.equalsIgnoreCase("Active")) {
            // Deactivate first
            clickToggleStatus();
            isConfirmationPopupVisible();
            driverTools.click(DEACTIVATE_BUTTON);
            driverTools.dummyWait(2);

            // Activate again
            clickToggleStatus();
        } else if (status.equalsIgnoreCase("Inactive")) {
            // Just activate
            clickToggleStatus();
        } else {
            throw new IllegalStateException("Unknown credential status: " + status);
        }
    }

    public boolean verifyCredentialIsActivated() {
        driverTools.dummyWait(2);
        return isToggleActive();
    }
}
