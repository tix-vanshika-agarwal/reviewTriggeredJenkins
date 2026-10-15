package com.tiket.web.smart.Common;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class CommonSmartPageWeb<T extends WebDriver> extends WebPage<T> {
    public static final Locator VOUCHER_BOX_DIV = new Locator("VOUCHER_BOX_DIV", AppiumBy.xpath("//*[contains(@class,'voucher-box_empty_voucher_box')]"));
    public CommonSmartPageWeb(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> CommonSmartPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CommonSmartPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CommonSmartPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }
    public void waitForVoucherBox() {
        driverTools.waitForElementToAppear(VOUCHER_BOX_DIV);
    }

}
