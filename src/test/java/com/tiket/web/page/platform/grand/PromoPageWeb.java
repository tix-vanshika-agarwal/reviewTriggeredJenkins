package com.tiket.web.page.platform.grand;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class PromoPageWeb<T extends WebDriver> extends WebPage<T> {

    public PromoPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> PromoPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new PromoPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new PromoPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator PROMO_BACK_BUTTON = new Locator("PROMO_BACK_BUTTON", new By.ByXPath("//button[contains(@class,'MobileBackButton_transparent_header')]"));
    private static final Locator LOGIN_BUTTON_IN_PROMO_PAGE = new Locator("LOGIN_BUTTON_IN_PROMO_PAGE", new By.ByXPath("//div[contains(@class, 'VoucherComponent_voucher_box_entry_point')]//span[text()='Voucher Box']/ancestor::div[@data-testid='voucher-card']"));
    private static final Locator PROMO_DETAIL_BANNER_ENTRYPOINT = new Locator("PROMO_DETAIL_BANNER_ENTRYPOINT", new By.ByXPath("//a[@data-testid=\"promo-item\" and contains(@class, \"PromoBody_promo_card\")]"));
    private static final Locator PROMO_DETAIL_CONTENT = new Locator("PROMO_DETAIL_CONTENT", new By.ByXPath("//div[@data-testid=\"revamped-promo-detail-content\"]"));
    private static final Locator PROMO_LIST = new Locator("PROMO_LIST", new By.ByXPath("//*[text()='Voucher Box']"));



    public abstract boolean isPromoPageTitleAppear();
    public void clickBackBtn(){
        driverTools.waitForElementToPresence(PROMO_BACK_BUTTON).orElseThrow();
        try {
            driverTools.clickWithJavascriptExecutor(PROMO_BACK_BUTTON);
        } catch (Exception e) {
            driverTools.click(PROMO_BACK_BUTTON);
        }
    }

    public boolean isLoginButtoninPromoPageAppear() {
        return driverTools.isElementPresent(LOGIN_BUTTON_IN_PROMO_PAGE);
    }

    public void clickPromoDetailPage() {
        driverTools.waitForElementToBeClickable(PROMO_DETAIL_BANNER_ENTRYPOINT);
        driverTools.click(PROMO_DETAIL_BANNER_ENTRYPOINT);
    }

    public  boolean  checkPromoDetailPage() {
        driverTools.waitForElementToAppear(PROMO_DETAIL_CONTENT);
        return driverTools.isElementDisplayed(PROMO_DETAIL_CONTENT);
    }

    public boolean isPromoPageVisible() {
        driverTools.waitForElementToAppear(PROMO_LIST);
        return driverTools.isElementDisplayed(PROMO_LIST);
    }

    public boolean isPromoBannerVisible() {
        driverTools.waitForElementToAppear(PROMO_DETAIL_BANNER_ENTRYPOINT);
        return driverTools.isElementDisplayed(PROMO_DETAIL_BANNER_ENTRYPOINT);
    }
}
