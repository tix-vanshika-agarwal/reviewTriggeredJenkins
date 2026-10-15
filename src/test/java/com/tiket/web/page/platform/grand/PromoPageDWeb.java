package com.tiket.web.page.platform.grand;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PromoPageDWeb<T extends WebDriver> extends PromoPageWeb<T> {

    public PromoPageDWeb(Session<T> session) {
        super(session);
    }

    private static Locator PROMOPAGE_TITLE = new Locator("PROMOPAGE_TITLE", By.xpath("//*[contains(@class,'ResponsiveTabComponent_title')]"));

    public boolean isPromoPageTitleAppear(){
        driverTools.waitForElementToAppear(PROMOPAGE_TITLE,2);
        return driverTools.isElementDisplayed(PROMOPAGE_TITLE);
    }
}
