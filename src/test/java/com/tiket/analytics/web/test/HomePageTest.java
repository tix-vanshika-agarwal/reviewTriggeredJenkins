package com.tiket.analytics.web.test;

import com.tiket.analytics.BaseAnalyticsTest;
import com.tiket.analytics.assertions.BaseAssertions;
import com.tiket.analytics.web.page.AnalyticsUrlPageWeb;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.home.HomePageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class HomePageTest extends BaseAnalyticsTest {

    @Author(names = {"Areeb"})
    @Module(name = "HomePage")
    @Tribe(name = "Analytics")
    @Vertical(name = "Core")
    @Test
    public void testHomePageEventGuest() {
        Map<String, String> testProperties = getParam("testHomePageEventGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String utmSource = testProperties.get("utmSource");
        String utmMedium = testProperties.get("utmMedium");
        String utmCampaign = testProperties.get("utmCampaign");
        String utmId = testProperties.get("utmId");
        String utmTerm = testProperties.get("utmTerm");
        String utmContent = testProperties.get("utmContent");
        String utmExternal = testProperties.get("utmExternal");
        String language = testProperties.get("language");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        AnalyticsUrlPageWeb<?> analyticsUrlPage = AnalyticsUrlPageWeb.getInstance(session);

        step("Goto tiket web home page");
        analyticsUrlPage.openHomePageEn();
        homePage.waitForWebPageToLoad();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions analyticsAssertions = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmSource is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmMedium is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmCampaign is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmId is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmTerm is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("utmContent is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is organic", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("language is en", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "language", language), is(true));

    }
}
