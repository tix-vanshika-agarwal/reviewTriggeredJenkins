package com.tiket.web.page.common.home;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public abstract class InfoPageWeb<T extends WebDriver> extends WebPage<T> {
    public InfoPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> InfoPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new InfoPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new InfoPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }
    private static final Locator INFO_PAGE_TITLE = new Locator("INFO_PAGE_TITLE", AppiumBy.xpath("//*[@data-testid='info-page-content']//*[contains(@class,'FullText_title_text')]"));

    public boolean verifyTitleInfopage() {
        return driverTools.waitForElementToAppear(INFO_PAGE_TITLE,20).isPresent();
    }

    public String getInfoPageTitle() {
        WebElement infoPageTitle = driverTools.waitForElementToAppear(INFO_PAGE_TITLE).orElseThrow();
        String result = driverTools.getText(infoPageTitle);
        return result;
    }

    public abstract void clickBackButton();
}
