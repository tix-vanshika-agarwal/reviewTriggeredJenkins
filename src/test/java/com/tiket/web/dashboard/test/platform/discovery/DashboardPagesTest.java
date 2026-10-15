package com.tiket.web.dashboard.test.platform.discovery;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.discovery.DashboardPages;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class DashboardPagesTest extends BaseTest {
    @Author(names = {"Yanuar", "Fandy"})
    @Module(name = "Pages")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testAddNewUpcomingBookingWidget() {
        Map<String, String> data = getParam("testAddNewUpcomingBookingWidget");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        DashboardPages<?> dashboardPages = new DashboardPages<>(session);

        dashboardPages.setSheetData(data);
        log("initial Data from sheet: " + data);
        final String username = data.get("username");
        final String pwd = data.get("password");
        final String pageName = data.get("pageName");
        final String expectedTemplateCode = data.get("expectedTemplateCode");
        final String expectedPlatform = data.get("expectedPlatform").replaceAll(" ", ", ");
        final String expectedCurrency = data.get("expectedCurrency");
        final String expectedPeriod = data.get("expectedPeriod");
        final String expectedShownTo = data.get("expectedShownTo").replace(" ", ",");

        step("Given user open Pages Dashboard");
        dashboardPages.goToHomePageModule(username, pwd);
        dashboardPages.clickGetInButton();
        Assertion.assertThat("verify dashboard pages are loaded", dashboardPages.isPageModuleDashboardLoaded(username), is(true));

        step(String.format("And user go to mobile modules for page: %s", pageName));
        dashboardPages.goToMobileModules(pageName);

        step("When admin tried to create UPCOMING_WIDGET_V2");
        dashboardPages.addNewUpcomingBookingWidget();

        step("Then admin should be able to create it successfully");

        dashboardPages.searchNewlyCreatedWidget(data.get("widgetName"));
        Map<String, String> actualData = dashboardPages.getDataAfterSuccess();

        Assertion.assertThat("Verify template name", actualData.get("actualWidgetName"), equalTo(data.get("widgetName")));
        Assertion.assertThat("Verify template code", actualData.get("actualTemplateCode"), equalTo(expectedTemplateCode));
        Assertion.assertThat("Verify template shown to", actualData.get("actualShownTo"), equalTo(expectedShownTo));
        Assertion.assertThat("Verify platform shown", actualData.get("actualPlatform"), equalTo(expectedPlatform));
        Assertion.assertThat("Verify currency shown", actualData.get("actualCurrency"), equalTo(expectedCurrency));
        Assertion.assertThat("Verify widget shown period", actualData.get("actualPeriod"), equalTo(expectedPeriod));
        Assertion.assertThat("Verify widget segmentation", actualData.get("segmentation"), equalTo("General"));
        Assertion.assertThat("Verify widget status should be Active, live", actualData.get("widgetStatus"), equalTo("Active, live"));

        step("And archive the created widget");
        dashboardPages.archiveUpdatedUpcomingBookingWidget();
    }

    @Author(names = {"Yanuar", "Ankit"})
    @Module(name = "Pages")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testEditGeneralUpcomingBooking() {
        Map<String, String> data = getParam("testEditGeneralUpcomingBooking");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        DashboardPages<?> dashboardPages = new DashboardPages<>(session);

        dashboardPages.setSheetData(data);
        final String username = data.get("username");
        final String pwd = data.get("password");
        final String pageName = data.get("pageName");
        final String expectedPlatform = data.get("expectedPlatform").replace(" ", ", ");
        final String expectedShownTo = data.get("expectedShownTo").replace(" ", ",");
        final String period = data.get("expectedPeriod");

        step("Given user is in mobile module page dashboard");
        dashboardPages.goToHomePageModule(username, pwd);
        dashboardPages.clickGetInButton();
        Assertion.assertThat("verify dashboard pages are loaded", dashboardPages.isPageModuleDashboardLoaded(username), is(true));
        dashboardPages.goToMobileModules(pageName);

        step("When user click edit general in an UPCOMING_WIDGET");
        dashboardPages.clickOnEditGeneral();

        step("And modify general options and submit");
        dashboardPages.modifyGeneralUpcomingWidget();

        step("And verify updated data should be saved");
        String updatedWidgetName = dashboardPages.getSheetData().get("widgetName");
        dashboardPages.searchNewlyCreatedWidget(updatedWidgetName);
        Map<String, String> sheetData = dashboardPages.getSheetData();
        Map<String, String> updatedData = dashboardPages.getDataAfterSuccess();

        Assertion.assertThat("Verify title is as set", sheetData.get("widgetName"), is(updatedData.get("actualWidgetName")));
        Assertion.assertThat("Verify template code is disabled", updatedData.get("templateCodeIsDisabled"), is("YES"));
        Assertion.assertThat("Verify Platform Shown", expectedPlatform, is(updatedData.get("actualPlatform")));
        Assertion.assertThat("Verify Shown To", expectedShownTo, is(updatedData.get("actualShownTo")));

        switch (period) {
            case "RANGE":
                Assertion.assertThat("Verify that period type is RANGE", updatedData.get("actualPeriod"), is(period));
                Assertion.assertThat("Verify Start date is as set", updatedData.get("actualDrpStartDate"), is(sheetData.get("expectedStartDate")));
                Assertion.assertThat("Verify End date is as set", updatedData.get("actualDrpEndDate"), is(sheetData.get("expectedEndDate")));
                break;
            case "CUSTOM":
                String startTime = sheetData.get("startTime").replace("-", ":");
                String endTime = sheetData.get("endTime").replace("-", ":");
                String expectedSelectedTime = String.format("%s - %s", startTime, endTime);

                Assertion.assertThat("Verify that period type is CUSTOM", updatedData.get("actualPeriod"), is(period));
                Assertion.assertThat("Verify repeat every value", updatedData.get("actualRepeatEvery"), is(sheetData.get("expectedRepeatEvery")));
                Assertion.assertThat("Verify start date", updatedData.get("actualStartDaily"), is(sheetData.get("expectedStartDaily")));
                Assertion.assertThat("Verify time", updatedData.get("actualSelectTime"), is(expectedSelectedTime));
                break;
            default:
                Assertion.assertThat("Verify that period type is Always On", updatedData.get("actualPeriod"), equalTo(period));
        }

        step("And archive the updated widget");
        dashboardPages.archiveUpdatedUpcomingBookingWidget();
    }

    @Author(names = {"Yanuar", "Ankit"})
    @Module(name = "Pages")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testAddCustomMobileModule() {
        Map<String, String> data = getParam("testAddCustomMobileModule");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        DashboardPages<?> dashboardPages = new DashboardPages<>(session);
        dashboardPages.setSheetData(data);

        final String username = data.get("username");
        final String pwd = data.get("password");
        final String pageName = data.get("pageName");
        final String templateCode = data.get("templateCode");
        final String expectedTemplateCode = data.get("templateCode");
        final String expectedPlatform = data.get("platforms").replaceAll(" ", ", ");
        final String expectedCurrency = data.get("currency");
        final String expectedPeriod = data.get("period");
        final String expectedShownTo = data.get("shownTo").replace(" ", ",");
        final int maxTemplate = Integer.parseInt(data.get("maxTemplate"));

        step("Given user is in mobile module pages");
        dashboardPages.goToHomePageModule(username, pwd);
        dashboardPages.clickGetInButton();
        Assertion.assertThat("verify dashboard pages are loaded", dashboardPages.isPageModuleDashboardLoaded(username), is(true));
        dashboardPages.goToMobileModules(pageName);

        step("When click button Add Custom Module");
        dashboardPages.clickAddCustomModule();

        step("And input mandatory field - General");
        dashboardPages.inputGeneralSettingsModule();

        step("And input mandatory field - Content");
        dashboardPages.inputContentModule(templateCode);

        step("And new custom module will appear in active module page with correct data");
        final String templateName = dashboardPages.getSheetData().get("widgetName");
        dashboardPages.searchNewlyCreatedWidget(templateName);
        Map<String, String> dataToValidate = dashboardPages.getDataAfterSuccess();

        Assertion.assertThat("Verify that the newly created module is found in active module", dataToValidate.get("totalSearchResult"), is("1"));
        Assertion.assertThat("Verify that the name is correct", dataToValidate.get("actualWidgetName"), is(templateName));
        Assertion.assertThat("Verify template code", dataToValidate.get("actualTemplateCode"), equalTo(expectedTemplateCode));
        Assertion.assertThat("Verify template shown to", dataToValidate.get("actualShownTo"), equalTo(expectedShownTo));
        Assertion.assertThat("Verify platform shown", dataToValidate.get("actualPlatform"), equalTo(expectedPlatform));
        Assertion.assertThat("Verify currency shown", dataToValidate.get("actualCurrency"), equalTo(expectedCurrency));
        Assertion.assertThat("Verify widget shown period", dataToValidate.get("actualPeriod"), equalTo(expectedPeriod));
        Assertion.assertThat("Verify widget status should be Active, live", dataToValidate.get("widgetStatus"), equalTo("Active, live"));

        step(String.format("Normalize total template into '%s' only", maxTemplate));
        dashboardPages.normalizeTemplateNumber(maxTemplate, templateCode);
    }

    @Author(names = {"Fandy"})
    @Module(name = "Pages")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testBannerCarouselV2MultipleChipsCustomDataSource() {
        Map<String, String> data = getParam("testBannerCarouselV2MultipleChipsCustomDataSource");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        DashboardPages<?> dashboardPages = new DashboardPages<>(session);

        dashboardPages.setSheetData(data);
        final String username = data.get("username");
        final String password = data.get("password");
        final String pageName = data.get("pageName");
        final String dataSource = data.get("dataSource");

        step("Given user is in desktop module page dashboard");
        dashboardPages.goToHomePageModule(username, password);
        dashboardPages.clickGetInButton();
        Assertion.assertThat("Verify dashboard pages are loaded", dashboardPages.isPageModuleDashboardLoaded(username), is(true));
        dashboardPages.goToMobileModules(pageName);

        step("And user click add custom module & create module BANNER CAROUSEL V2");
        dashboardPages.clickAddCustomModule();
        dashboardPages.inputGeneralSettingsModule();
        Assertion.assertThat("Verify success save general setting", dashboardPages.isSuccessSnackbarAppear(), is(true));

        step("Add specific >1 chips with data source");
        dashboardPages.inputMandatoryBannerCarouselV2CustomDataSource();
        dashboardPages.inputChipsBannerCarouselV2(dataSource);
        Assertion.assertThat("Verify success submit the module", dashboardPages.isSuccessSnackbarAppear(), is(true));
    }

    @Author(names = {"Ankit"})
    @Module(name = "Homepage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void verifyDynamicWidgetOnMobilePanel() {
        Map<String, String> data = getParam("verifyDynamicWidgetOnMobilePanel");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        DashboardPages<?> dashboardPages = new DashboardPages<>(session);

        dashboardPages.setSheetData(data);
        final String username = data.get("username");
        final String password = data.get("password");
        final String pageName = data.get("pageName");
        final String templateCode = data.get("templateCode");

        step("Given user logs in to Panel Pages dashboard");
        dashboardPages.goToHomePageModule(username, password);
        dashboardPages.clickGetInButton();
        Assertion.assertThat("Verify dashboard pages are loaded", dashboardPages.isPageModuleDashboardLoaded(username), is(true));

        step("And user navigates to Mobile Modules for page: " + pageName);
        dashboardPages.goToMobileModules(pageName);

        step("When click button Add Custom Module and input mandatory field - General");
        dashboardPages.clickAddCustomModule();
        dashboardPages.inputGeneralSettingsModule();

        step(String.format("And input mandatory field - Content for %s", templateCode));
        dashboardPages.inputContentModule(templateCode);

        step(String.format("Then the %s module should be created successfully", templateCode));
        String widgetName = dashboardPages.getSheetData().get("widgetName");
        dashboardPages.searchNewlyCreatedWidget(widgetName);
        Map<String, String> actualData = dashboardPages.getDataAfterSuccess();

        Assertion.assertThat("Verify widget name", actualData.get("actualWidgetName"), equalTo(widgetName));
        Assertion.assertThat("Verify template code", actualData.get("actualTemplateCode"), equalTo(templateCode));
        Assertion.assertThat("Verify widget status should be Active, live", actualData.get("widgetStatus"), equalTo("Active, live"));

        step("And archive the created widget");
        dashboardPages.archiveDynamicWidget();
    }
}
