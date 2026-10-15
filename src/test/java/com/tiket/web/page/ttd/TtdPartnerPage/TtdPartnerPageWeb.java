package com.tiket.web.page.ttd.TtdPartnerPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class TtdPartnerPageWeb<T extends WebDriver> extends WebPage<T> {

    public TtdPartnerPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TtdPartnerPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new TtdPartnerPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new TtdPartnerPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    public static final Locator PARTNER_NAME = new Locator("PARTNER_NAME", new By.ByXPath("//*[@data-testid='partner-info-name']"));
    private static final Locator PRODUCT_CARD = new Locator("PRODUCT_CARD", new By.ByXPath("(//div[contains(@class,'ProductCardTTD_card_content_wrapper')])[1]"));

    public String getPartnerName() {
        driverTools.waitForElementToPresence(PARTNER_NAME);
        return driverTools.getText(PARTNER_NAME).orElseThrow();
    }

    public boolean isPageModuleDisplayed() {
        driverTools.waitForElementToAppear(PARTNER_NAME);
        driverTools.scrollIntoWebView(PRODUCT_CARD);
        return driverTools.waitForElementToAppear(PRODUCT_CARD, 10).isPresent();
    }

    public boolean isPromoSectionDisplayed() {
        boolean isPromoSectionShown = driverTools.waitForElementToAppear(TtdLandingPageWeb.PROMO_SECTION).isPresent();
        logger.log("is promo section displayed : " + isPromoSectionShown);
        return isPromoSectionShown;
    }
}
