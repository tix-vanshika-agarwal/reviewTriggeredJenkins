package com.tiket.web.page.platform.sphinx.insurance;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ClaimCenterPageMWeb<T extends WebDriver> extends ClaimCenterPageWeb<T> {
    public ClaimCenterPageMWeb(Session<T> session) {
        super(session);
    }
    private static final Locator WELCOME_INSURANCE_CLAIM_CENTER_CLOSE_BTN = new Locator("WELCOME_INSURANCE_CLAIM_CENTER_CLOSE_BTN", new By.ByXPath("//*[contains(@class,'close_button')]"));

    @Override
    protected Locator CheckClaimCenterPageTitle() {
        return new Locator("INSURANCE_CLAIM_CENTER_PAGE_TITLE", new By.ByXPath("//*[contains(@class,'page_title') and (contains(text(),'Insurance Claim Center') or contains(text(),'Pusat Klaim Asuransi'))]"));
    }

    @Override
    public void removeWelcomePopup(){
        driverTools.waitForElementToAppear(WELCOME_INSURANCE_CLAIM_CENTER_CLOSE_BTN).ifPresent(driverTools::click);
    }

}