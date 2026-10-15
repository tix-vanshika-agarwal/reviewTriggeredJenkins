package com.tiket.page.utilities.discovery.promo;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.core.Is.is;

public class PromoPageAndroid <T extends WebDriver> extends PromoPage<T> {

    public PromoPageAndroid(Session<T> session) {
        super(session);
    }

    private static final Locator PROMO_BANNER = new Locator("PROMO_BANNER", AppiumBy.xpath("//*[contains(@resource-id,'card_view_promo')]"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.id("appbar_back_icon_container"));
    private static final Locator SHARE_BUTTON = new Locator("SHARE_BUTTON", AppiumBy.id("tds_appbar_items_container"));
    private static final Locator MAIN_BANNER = new Locator("MAIN_BANNER", AppiumBy.id("banner_container"));


    @Override
    public void verifyOnPromoLandingPage(){} // FIXME: Implementation pending
    @Override
    public void verifyOnPromoListPage(){} // FIXME: Implementation pending
    @Override
    public void verifyPromoDetail(){} // FIXME: Implementation pending

    @Override
    public void clickPromoBanner(int index) {
        index = index == 0 ? 1 : index;
        if(index > 1) {
            driverTools.swipeUp();
        }
        driverTools.waitForElementToAppear(PROMO_BANNER);
        int promoBannerCount = driverTools.elements(PROMO_BANNER).orElseThrow().size();
        index = (index % promoBannerCount) == 0 ? index : index % promoBannerCount;
        WebElement promoBannerElement = driverTools.swipeToElement(getPromoBannerLocatorByIndex(index), Swipe.UP, index + 1).orElseThrow();
        driverTools.click(promoBannerElement);
    }

    @Override
    public boolean isPromoListPageValid() {
        boolean isBackButtonDisplayed = driverTools.waitForElementToAppear(BACK_BUTTON).isPresent();
        boolean isShareButtonButtonDisplayed = driverTools.waitForElementToAppear(SHARE_BUTTON).isPresent();
        boolean isBannerDisplayed = driverTools.waitForElementToAppear(MAIN_BANNER).isPresent();
        boolean isBannerContentDisplayed = driverTools.waitForElementToAppear(PROMO_BANNER).isPresent();

        return isBackButtonDisplayed && isShareButtonButtonDisplayed && isBannerDisplayed && isBannerContentDisplayed;
    }

    private Locator getPromoBannerLocatorByIndex(int index) {
        return new Locator("PROMO_BANNER_"+index, By.xpath("(//*[contains(@resource-id,'card_view_promo')]//*[contains(@resource-id,'iv_image')])["+index+"]"));
    }

    private Locator getPromoTabFlight() {
        return new Locator("PROMO_TAB", AppiumBy.xpath("//*[@content-desc='Flights' or @content-desc='Pesawat']"));
    }

    @Override
    public boolean isFlightTabDisplayedOnPromoPage() {
        WebElement flightTab = driverTools.waitForElementToAppear(getPromoTabFlight()).orElseThrow();
        boolean isFlightTabDisplayed = driverTools.isElementDisplayed(flightTab);
        Assertion.assertThat("is flight tab displayed ",isFlightTabDisplayed,is(true));

        boolean isFlightTabSelected = driverTools.getValueAttribute(flightTab,"selected").equals("true");
        Assertion.assertThat("is flight tab selected ",isFlightTabSelected,is(true));
        return isFlightTabDisplayed && isFlightTabSelected;
    }

    @Override
    public void clickBack() {
        driverTools.waitForElementToAppear(BACK_BUTTON);
        driverTools.clickElementByCoordinate(BACK_BUTTON);
    }

}
