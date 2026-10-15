package com.tiket.web.page.ttd.TtdProductDetailPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TtdProductDetailPageDweb<T extends WebDriver> extends TtdProductDetailPageWeb<T> {

    public TtdProductDetailPageDweb(Session<T> session) {
        super(session);
    }

    private static final Locator VALIDITY_PERIOD = new Locator("VALIDITY_PERIOD", AppiumBy.xpath("//*[contains(text(),'Validity period:') or contains(text(),'Masa berlaku')]//following-sibling::*"));
    private static final Locator BUY_TICKET_BUTTON = new Locator("BUY_TICKET_BUTTON", By.xpath("(//button[./span[contains(text(), 'Buy ticket now') or contains(text(), 'Beli tiket sekarang')]])[1]"));
    private static final Locator PROMO_BENEFIT_POPUP = new Locator("PROMO_BENEFIT_POPUP", AppiumBy.xpath("//h2[" + "contains(., 'Special Benefits') " + "or contains(., 'Benefit Spesial')" + "]"));

    @Override
    protected boolean isPromoBenefitsPopupDisplayed() {
        return driverTools.isElementDisplayed(PROMO_BENEFIT_POPUP);
    }

    @Override
    public String getValidityPeriodText () {
        driverTools.waitForElementToPresence(VALIDITY_PERIOD);
        return driverTools.getText(VALIDITY_PERIOD).orElseThrow();
    }

    @Override
    public void clickBuyTicketButton() {
        driverTools.waitForElementToPresence(BUY_TICKET_BUTTON);
        driverTools.clickWithJavascriptExecutor(BUY_TICKET_BUTTON);
    }
}
