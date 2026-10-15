package com.tiket.page.utilities.discovery.promo;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class PromoPageIOS <T extends WebDriver> extends PromoPage<T>{

    public PromoPageIOS(Session<T> session) {
        super(session);
    }

    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='back' or @name='Back' or @label='Back' or @label='Kembali'] | //*[@name='tds_ic_back' or @name='tds ic back' or @name='back_button' or @name='back_btn' or @name='TDSNavbarButton' or @name='back']"));
    private static final Locator SHARE_BUTTON = new Locator("SHARE_BUTTON", AppiumBy.accessibilityId("share_promo_button"));
    private static final Locator MAIN_BANNER = new Locator("MAIN_BANNER", AppiumBy.accessibilityId("promo_page_banner"));
    private static final Locator PROMO_BANNER = new Locator("PROMO_BANNER", AppiumBy.xpath("(//*[contains(@name, 'promo_content_banner_')])[1]"));

    @Override
    public void verifyOnPromoLandingPage(){} // FIXME: Implementation pending
    @Override
    public void verifyOnPromoListPage(){} // FIXME: Implementation pending
    @Override
    public void verifyPromoDetail(){} // FIXME: Implementation pending

    @Override
    public void clickPromoBanner(int index) {
        index = index == 0 ? 1 : index;
        Locator promoBannerElement = getPromoBannerLocatorByIndex(index);
        if(!driverTools.isElementDisplayed(getPromoBannerLocatorByIndex(index)))
            driverTools.swipeToElement(getPromoBannerLocatorByIndex(index), Swipe.UP, index+1).orElseThrow();
        driverTools.click(promoBannerElement);
    }

    @Override
    public boolean isPromoListPageValid() {
        boolean isBackButtonDisplayed = driverTools.waitForElementToAppear(BACK_BUTTON, 5).isPresent();
        boolean isShareButtonButtonDisplayed = driverTools.waitForElementToAppear(SHARE_BUTTON).isPresent();
        boolean isBannerDisplayed = driverTools.waitForElementToAppear(MAIN_BANNER).isPresent();
        boolean isBannerContentDisplayed = driverTools.waitForElementToAppear(PROMO_BANNER).isPresent();

        return isBackButtonDisplayed && isShareButtonButtonDisplayed && isBannerDisplayed && isBannerContentDisplayed;
    }

    private Locator getPromoBannerLocatorByIndex(int index) {
        return new Locator("PROMO_BANNER", AppiumBy.xpath("//*[@type='XCUIElementTypeOther' and ./parent::*[@name='promo_content_banner_1']]"));
    }

    private Locator getPromoTabFlight() {
        return new Locator("PROMO_TAB", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Pesawat') or contains(@label,'Flight')]"));
    }

    @Override
    public boolean isFlightTabDisplayedOnPromoPage() {
        WebElement flightTab = driverTools.waitForElementToAppear(getPromoTabFlight()).orElseThrow();
        boolean isFlightTabDisplayed = driverTools.isElementDisplayed(flightTab);
        Assertion.assertThat("is flight tab displayed ",isFlightTabDisplayed,is(true));
        return isFlightTabDisplayed;
    }

    @Override
    public void clickBack() {
        driverTools.waitForElementToAppear(BACK_BUTTON);
        driverTools.clickElementByCoordinate(BACK_BUTTON);
    }
}
