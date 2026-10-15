package com.tiket.page.flight.lpgPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public abstract class LpgPage<T extends WebDriver> extends BasePage<T> {
    public LpgPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> LpgPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {
                return new LpgPageIOS<>(session);
            }
            case ANDROID -> {
                return new LpgPageAndroid<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public static final Locator LPG_TITLE = new Locator("LPG_TITLE", AppiumBy.xpath("//*[contains(@class,'FullText_title') and (contains(text(),'Jaminan Harga Termurah') or contains(text(),'Lowest Price Guarantee'))]"));
    private static final Locator LPG_CONTENT = new Locator("LPG_CONTENT", AppiumBy.xpath("//*[contains(@class,'FullText_title_text')]"));

    public void checkLPGBanner(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("info");
        String url = driverTools.getCurrentURL();
        Assertion.assertThat("redirected to different page that is "+ url,url,is(containsString("jaminan-harga-termurah")));
        Assertion.assertThat("lpg content on on webview page",driverTools.isElementDisplayed(LPG_CONTENT),is(true));
        driverTools.switchToNative();
    }
}
