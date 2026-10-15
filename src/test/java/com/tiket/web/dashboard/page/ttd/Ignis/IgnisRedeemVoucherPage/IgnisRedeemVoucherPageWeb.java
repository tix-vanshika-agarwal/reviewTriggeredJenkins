package com.tiket.web.dashboard.page.ttd.Ignis.IgnisRedeemVoucherPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IgnisRedeemVoucherPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisRedeemVoucherPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisRedeemVoucherPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisRedeemVoucherPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisRedeemVoucherPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator ORDER_ID = new Locator("ORDER_ID", By.xpath("//div[@class='core-order-info-container']/div[@class='information-container'][1]/div[@class='information-item'][1]/p"));
    private static final Locator TICKET_ID = new Locator("TICKET_ID", By.xpath("//div[@class='ticket-item']//h3"));
    private static final Locator REDEEM_BUTTON = new Locator("REDEEM_BUTTON", By.xpath("//button[@class='btn btn-primary']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[@class='snackbar success visible']"));
    private static final Locator FAIL_SNACKBAR = new Locator("FAIL_SNACKBAR", By.xpath("//div[@class='snackbar error visible']"));
    private static final Locator SUCCESS_NOTIFICATION_BANNER = new Locator("SUCCESS_NOTIFICATION_BANNER", By.xpath("//div[@class='notification-banner success open with-close with-transition']"));
    private static final Locator FAIL_NOTIFICATION_BANNER = new Locator("FAIL_NOTIFICATION_BANNER", By.xpath("//div[@class='notification-banner urgent open with-close with-transition']"));
    private static final Locator GREEN_BADGE = new Locator("GREEN_BADGE", By.xpath("//div[@class='badge badge-green']"));
    private static final Locator QUEUE_NUMBER_TEXT = new Locator("QUEUE_NUMBER_TEXT", By.xpath("//span[@class='section-text--value'][last()]"));

    public String getOrderId() {
        driverTools.waitForElementToAppear(ORDER_ID);
        return driverTools.getText(ORDER_ID).orElseThrow();
    }

    public void clickRedeemVoucherButton() {
        driverTools.click(REDEEM_BUTTON);
    }

    public Boolean isSuccessSnackBarVisible() {
        return driverTools.waitForElementToAppear(SUCCESS_SNACKBAR).isPresent();
    }

    public Boolean isSuccessNotifBannerVisible() {
        return driverTools.waitForElementToAppear(SUCCESS_NOTIFICATION_BANNER).isPresent();
    }

    public Boolean isSuccessBadgeVisible() {
        return driverTools.waitForElementToAppear(GREEN_BADGE).isPresent();
    }

    public String getTicketId() {
        driverTools.waitForElementToAppear(TICKET_ID);
        return driverTools.getText(TICKET_ID).orElseThrow();
    }

    public Boolean isFailNotifBannerVisible() {
        return driverTools.waitForElementToAppear(FAIL_NOTIFICATION_BANNER).isPresent();
    }

    public Boolean isFailSnackBarVisible() {
        return driverTools.waitForElementToAppear(FAIL_SNACKBAR).isPresent();
    }

    public String getQueueNumber() {
        driverTools.waitForElementToAppear(QUEUE_NUMBER_TEXT);
        return driverTools.getText(QUEUE_NUMBER_TEXT).orElseThrow();
    }
}
