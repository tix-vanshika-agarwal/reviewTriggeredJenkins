package com.tiket.web.dashboard.page.affiliate;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;

public class AffiliateTTDSearch<T extends WebDriver> extends BasePage<T> {
    public AffiliateTTDSearch(Session<T> session) {
        super(session);
    }

    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[(text()='Search' or text()='Cari') and contains(@class, 'Button_variant_secondary')]"));
    private static final Locator PARTNER_ID_INPUT = new Locator("PARTNER_ID_INPUT", By.xpath("//input[@id='partner-id']"));
    private static final Locator PARTNER_NAME_INPUT = new Locator("PARTNER_NAME_INPUT", By.id("partner-name"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[contains(text(),'Reset Filter') or contains(text(),'Atur Ulang Filter')]"));
    private static final Locator SETTINGS_BUTTON = new Locator("SETTINGS_BUTTON", By.xpath("//div[contains(@class, 'relative') and .//input[@placeholder='Settings' or @placeholder='Pengaturan']]"));
  
    public void searchByTTDPartnerId(String partnerId){
        driverTools.waitForElementToAppear(PARTNER_ID_INPUT, 4).orElseThrow();
        driverTools.sendKeys(PARTNER_ID_INPUT, partnerId);
        clickSearchButton();
    }

    public void searchByTTDPartnerName(String partnerName){
        driverTools.waitForElementToAppear(PARTNER_NAME_INPUT, 4).orElseThrow();
        driverTools.click(PARTNER_NAME_INPUT);
        driverTools.sendKeys(PARTNER_NAME_INPUT, partnerName);
        clickSearchButton();
    }

    public boolean isSearchFieldEmpty() {
        String partnerName = driverTools.getText(PARTNER_NAME_INPUT).orElseThrow();
        String partnerId =driverTools.getText(PARTNER_ID_INPUT).orElseThrow();
        return partnerName.isEmpty() && partnerId.isEmpty();
    }

    public void clickFilterResetButton() {
        driverTools.waitForElementToAppear(RESET_FILTER_BUTTON, 5);
        driverTools.click(RESET_FILTER_BUTTON);
    }

    public boolean hasMultipleSettingsButtons() {
        List<WebElement> settingsButtons = driverTools.elements(SETTINGS_BUTTON).orElse(Collections.emptyList());
        logger.log("Settings buttons found: " + settingsButtons.size());
        return settingsButtons.size() > 1;
    }


    public void clickSearchButton(){
        driverTools.click(SEARCH_BUTTON);
    }
}

