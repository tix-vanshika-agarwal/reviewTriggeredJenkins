package com.tiket.web.page.nft.bus.productDetail_Page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;

import static org.hamcrest.Matchers.containsString;

public abstract class BusPdpPageWeb<T extends WebDriver> extends WebPage<T> {
    private static final Locator PRICE_TEXT = new Locator("PRICE_TEXT", new By.ByXPath("(//div[contains(@class,'Footer_price_and_points')]//span[contains(@class,'Text_variant_price')])[1]"));
    private static final Locator SELECT_SEAT_BUTTON = new Locator("SELECT_SEAT_BUTTON", new By.ByXPath("(//button[.//span[contains(@class,'Text_text') and (contains(.,'Select seat') or contains(.,'Pilih kursi'))]])[1]"));
    private static final Locator SUBMIT_SEAT_BUTTON = new Locator("SUBMIT_SEAT_BUTTON", new By.ByXPath("//*[contains(@class, 'Footer_button_wrapper')]//button"));

    public BusPdpPageWeb(Session<T> session) { super(session); }

    public static <T extends WebDriver> BusPdpPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new BusPdpPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new BusPdpPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public void verifyPdpPriceInIDR() {
        String priceText = driverTools.waitForElementToAppear(PRICE_TEXT).orElseThrow().getText();
        Assertion.assertThat("Verify price in PDP is in IDR", priceText, containsString("IDR"));
    }

    public void selectSeat() {
        driverTools.click(SELECT_SEAT_BUTTON);
        driverTools.click(SUBMIT_SEAT_BUTTON);
    }
}