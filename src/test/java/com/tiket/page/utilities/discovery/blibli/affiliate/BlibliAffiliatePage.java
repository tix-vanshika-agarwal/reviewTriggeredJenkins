package com.tiket.page.utilities.discovery.blibli.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Map;
import java.util.Optional;

public class BlibliAffiliatePage  <T extends WebDriver> extends BasePage<T> {

    public BlibliAffiliatePage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> BlibliAffiliatePage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID, IOS -> {return new BlibliAffiliatePage<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }


    public static final Locator JOIN_AFFILIATE = new Locator("JOIN_AFFILIATE", AppiumBy.xpath("//*[@data-testid='pagemodule-view-1']"));
    public static final Locator HERO_BANNER = new Locator("HERO_BANNER", AppiumBy.xpath("//*[@data-testid='hero-banner-content']"));
    public static final Locator NAVIGATION_BAR = new Locator("NAVIGATION_BAR", AppiumBy.id("home-header-top-navigation"));
    public static final Locator PERFORMANCE_HEADER = new Locator("PERFORMANCE_HEADER", AppiumBy.xpath("//*[contains(@class,'header__performance')]"));

    public boolean isAffiliatePageDisplayed() {
        return driverTools.isElementDisplayed(JOIN_AFFILIATE) || driverTools.isElementDisplayed(HERO_BANNER) || driverTools.isElementDisplayed(NAVIGATION_BAR) || driverTools.isElementDisplayed(PERFORMANCE_HEADER);
    }

    public boolean isJoinAffiliatePageDisplayed() {
        return driverTools.isElementDisplayed(JOIN_AFFILIATE) || driverTools.isElementDisplayed(HERO_BANNER);
    }

    public boolean isAffiliateHomePageDisplayed() {
        return driverTools.isElementDisplayed(PERFORMANCE_HEADER);
    }

    public boolean checkAffiliateHomePageForAffiliateUser() {
        driverTools.waitForElementToAppear(JOIN_AFFILIATE).ifPresent(driverTools::click);
        WebElement performanceHeader = driverTools.waitForElementToAppear(PERFORMANCE_HEADER).orElseThrow();
        return performanceHeader.isDisplayed();
    }

    public void joinAffiliate() {
        final Locator UAP_UI = new Locator("UAP_UI", AppiumBy.xpath("//*[@id='uap-ui']"));
        final Locator BLU_CHECKBOX = new Locator("BLU_CHECKBOX", AppiumBy.xpath("//*[@class='blu-checkbox']"));
        final Locator START_BUTTON = new Locator("START_BUTTON", AppiumBy.xpath("//button[contains(@class, 'proceed-button')]"));
        final Locator LANJUT = new Locator("LANJUT", AppiumBy.xpath("//*[contains(@id,'bli-button')]"));

        driverTools.waitForElementToAppear(JOIN_AFFILIATE).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(UAP_UI).orElseThrow();
        WebElement bluCheckbox = driverTools.waitForElementToAppear(BLU_CHECKBOX).orElseThrow();
        driverTools.click(bluCheckbox);
        driverTools.click(START_BUTTON);
        for(int i = 0; i < 3; i++) {
            driverTools.waitForElementToAppear(LANJUT).ifPresent(driverTools::click);
        }
        driverTools.waitForElementToAppear(PERFORMANCE_HEADER).orElseThrow();
    }


}
