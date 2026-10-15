package com.tiket.analytics.web.test.grand;

import com.tiket.analytics.BaseAnalyticsTest;
import com.tiket.analytics.assertions.BaseAssertions;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;

public class GrandPageTest extends BaseAnalyticsTest {

    @Author(names = {"Arbi JS"})
    @Module(name = "HomePage")
    @Tribe(name = "Platform")
    @Vertical(name = "Grand")
    @Test
    public void verifyViewQrCodePopUpEventPropertiesOnHomePage() {
        Map<String, String> testProperties = getParam("verifyViewQrCodePopUpEventPropertiesOnHomePage");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String eventCategory = testProperties.get("eventCategory");
        String eventDescription = testProperties.get("eventDescription");
        String sectionName = testProperties.get("sectionName");
        String screenName = testProperties.get("screenName");
        String screenOwner = testProperties.get("screenOwner");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page as guest");
        homePage.goToHomePage();
        loginPage.loginAsGuest();

        step("Scroll down until Install App banner is visible");
        homePage.scrollDownToShowHomeAppInstallBanner();

        step("Click Install App banner to trigger QR code pop up");
        homePage.clickHomeAppInstallBannerImage();
        Assertion.assertThat("QR code pop up is displayed", homePage.isQrCodePopupModalDisplayed(), is(true));

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions analyticsAssertions = new BaseAssertions(mainLogger.get());
        // sectionName alone isn't unique: every sub-action of this popup (view/copy/store/close) shares the
        // same event+sectionName, so eventAction must be part of the filter to isolate this one impression.
        Map<String, String> filters = Map.of("sectionName", sectionName, "eventAction", eventAction);

        step("Verify popUpQRShow impression event tracker properties");
        Assertion.assertThat("impression event is found",
                analyticsAssertions.countFilteredEventByProperties(Configs.MESSAGES, event, filters), is(1));
        Assertion.assertThat("eventCategory is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "eventCategory", eventCategory), is(true));
        Assertion.assertThat("eventDescription is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "eventDescription", eventDescription), is(true));
        Assertion.assertThat("screenName is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "screenName", screenName), is(true));
        Assertion.assertThat("screenOwner is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "screenOwner", screenOwner), is(true));
    }

    @Author(names = {"Arbi JS"})
    @Module(name = "HomePage")
    @Tribe(name = "Platform")
    @Vertical(name = "Grand")
    @Test
    public void verifyClickCopyQrCodePopUpEventPropertiesOnHomePage() {
        Map<String, String> testProperties = getParam("verifyClickCopyQrCodePopUpEventPropertiesOnHomePage");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String eventCategory = testProperties.get("eventCategory");
        String eventDescription = testProperties.get("eventDescription");
        String sectionName = testProperties.get("sectionName");
        String screenName = testProperties.get("screenName");
        String screenOwner = testProperties.get("screenOwner");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page as guest");
        homePage.goToHomePage();
        loginPage.loginAsGuest();

        step("Scroll down until Install App banner is visible");
        homePage.scrollDownToShowHomeAppInstallBanner();

        step("Click Install App banner to trigger QR code pop up");
        homePage.clickHomeAppInstallBannerImage();
        Assertion.assertThat("QR code pop up is displayed", homePage.isQrCodePopupModalDisplayed(), is(true));

        step("Click Copy button on QR code pop up");
        homePage.clickQrCodePopupCopyButton();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions analyticsAssertions = new BaseAssertions(mainLogger.get());
        // The popup's impression event (fired when it opened) shares event+sectionName with this click event,
        // so eventAction must be part of the filter to isolate exactly this one click.
        Map<String, String> filters = Map.of("sectionName", sectionName, "eventAction", eventAction);

        step("Verify clickCopyPopUpQR click event tracker properties");
        Assertion.assertThat("click event is found",
                analyticsAssertions.countFilteredEventByProperties(Configs.MESSAGES, event, filters), is(1));
        Assertion.assertThat("eventCategory is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "eventCategory", eventCategory), is(true));
        Assertion.assertThat("eventDescription is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "eventDescription", eventDescription), is(true));
        Assertion.assertThat("screenName is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "screenName", screenName), is(true));
        Assertion.assertThat("screenOwner is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "screenOwner", screenOwner), is(true));
    }

    @Author(names = {"Arbi JS"})
    @Module(name = "HomePage")
    @Tribe(name = "Platform")
    @Vertical(name = "Grand")
    @Test
    public void verifyClickStoreQrCodePopUpEventPropertiesOnHomePage() {
        Map<String, String> testProperties = getParam("verifyClickStoreQrCodePopUpEventPropertiesOnHomePage");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String eventCategory = testProperties.get("eventCategory");
        String eventDescription = testProperties.get("eventDescription");
        String sectionName = testProperties.get("sectionName");
        String screenName = testProperties.get("screenName");
        String screenOwner = testProperties.get("screenOwner");
        String storeLabel = testProperties.get("storeLabel");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page as guest");
        homePage.goToHomePage();
        loginPage.loginAsGuest();

        step("Scroll down until Install App banner is visible");
        homePage.scrollDownToShowHomeAppInstallBanner();

        step("Click Install App banner to trigger QR code pop up");
        homePage.clickHomeAppInstallBannerImage();
        Assertion.assertThat("QR code pop up is displayed", homePage.isQrCodePopupModalDisplayed(), is(true));

        step("Capture store link and click Store button on QR code pop up");
        String storeUrl = homePage.getQrCodePopupStoreLinkUrl(storeLabel);
        homePage.clickQrCodePopupStoreButtonAndRestoreTab(storeLabel);

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions analyticsAssertions = new BaseAssertions(mainLogger.get());
        // The popup's impression event (fired when it opened) shares event+sectionName with this click event,
        // so eventAction must be part of the filter to isolate exactly this one click.
        Map<String, String> filters = Map.of("sectionName", sectionName, "eventAction", eventAction);

        step("Verify clickStorePopUpQR click event tracker properties");
        // Clicking a store badge navigates away from tiket.com, which can trigger a duplicate send of the same
        // click event via a beforeunload/sendBeacon delivery fallback; unlike copy/close, at-least-once is expected.
        Assertion.assertThat("click event is found",
                analyticsAssertions.countFilteredEventByProperties(Configs.MESSAGES, event, filters), greaterThanOrEqualTo(1));
        Assertion.assertThat("eventCategory is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "eventCategory", eventCategory), is(true));
        Assertion.assertThat("eventDescription is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "eventDescription", eventDescription), is(true));
        // eventLabel is expected to be the store link's own href, which is captured at runtime rather than
        // pinned down as a static value (it can carry env-specific campaign query params).
        Assertion.assertThat("eventLabel is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "eventLabel", storeUrl), is(true));
        Assertion.assertThat("screenName is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "screenName", screenName), is(true));
        Assertion.assertThat("screenOwner is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "screenOwner", screenOwner), is(true));
    }

    @Author(names = {"Arbi JS"})
    @Module(name = "HomePage")
    @Tribe(name = "Platform")
    @Vertical(name = "Grand")
    @Test
    public void verifyClickCloseQrCodePopUpEventPropertiesOnHomePage() {
        Map<String, String> testProperties = getParam("verifyClickCloseQrCodePopUpEventPropertiesOnHomePage");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String eventCategory = testProperties.get("eventCategory");
        String eventDescription = testProperties.get("eventDescription");
        String sectionName = testProperties.get("sectionName");
        String screenName = testProperties.get("screenName");
        String screenOwner = testProperties.get("screenOwner");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page as guest");
        homePage.goToHomePage();
        loginPage.loginAsGuest();

        step("Scroll down until Install App banner is visible");
        homePage.scrollDownToShowHomeAppInstallBanner();

        step("Click Install App banner to trigger QR code pop up");
        homePage.clickHomeAppInstallBannerImage();
        Assertion.assertThat("QR code pop up is displayed", homePage.isQrCodePopupModalDisplayed(), is(true));

        step("Click close button on QR code pop up");
        homePage.closeQrCodePopupModal();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions analyticsAssertions = new BaseAssertions(mainLogger.get());
        // The popup's impression event (fired when it opened) shares event+sectionName with this click event,
        // so eventAction must be part of the filter to isolate exactly this one click.
        Map<String, String> filters = Map.of("sectionName", sectionName, "eventAction", eventAction);

        step("Verify clickClosePopUpQR click event tracker properties");
        Assertion.assertThat("click event is found",
                analyticsAssertions.countFilteredEventByProperties(Configs.MESSAGES, event, filters), is(1));
        Assertion.assertThat("eventCategory is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "eventCategory", eventCategory), is(true));
        Assertion.assertThat("eventDescription is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "eventDescription", eventDescription), is(true));
        Assertion.assertThat("screenName is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "screenName", screenName), is(true));
        Assertion.assertThat("screenOwner is correct",
                analyticsAssertions.filteredByPropertiesHaveCorrectEventProperty(Configs.MESSAGES, event, filters, "screenOwner", screenOwner), is(true));
    }
}
