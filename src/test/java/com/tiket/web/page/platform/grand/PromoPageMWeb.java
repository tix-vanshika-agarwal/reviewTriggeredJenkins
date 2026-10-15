package com.tiket.web.page.platform.grand;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PromoPageMWeb<T extends WebDriver> extends PromoPageWeb<T> {

    public PromoPageMWeb(Session<T> session) {
        super(session);
    }

    private static Locator PROMOPAGE_TITLE = new Locator("PROMOPAGE_TITLE2", By.xpath("//a[contains(@class,'PromoBody_promo_card')]"));

    public boolean isPromoPageTitleAppear(){
        driverTools.waitForElementToAppear(PROMOPAGE_TITLE,2);
        return driverTools.isElementDisplayed(PROMOPAGE_TITLE);
    }
}