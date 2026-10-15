package com.tiket.web.dashboard.test.platform.discovery;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import com.tiket.web.dashboard.page.platform.discovery.DynamicLandingPage;
import com.tiket.web.dashboard.page.platform.discovery.VerticalProductPage;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class HomeTest extends BaseTest {

    @Author(names = {"Fandy"})
    @Module(name = "VerticalProduct")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testAddVerticalProductHome() throws InterruptedException {
        Map<String, String> param = getParam("testAddVerticalProductHome");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        VerticalProductPage<?> verticalProductPage = new VerticalProductPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String trackerCode = param.get("trackerCode");
        String currency = param.get("currency");
        String platform = param.get("platform");
        String typeAction = param.get("typeAction");
        String contentLabelId = param.get("contentLabelId");
        String contentLabelEn = param.get("contentLabelEn");
        String hexColor  = param.get("hexColor");
        String appVersionStart = param.get("appVersionStart");
        String appVersionEnd = param.get("appVersionEnd");
        String urlSlug = param.get("urlSlug");
        String nameToUserId = param.get("nameToUserId");
        String nameToUserEn = param.get("nameToUserEn");
        String nameToUserVersion = param.get("nameToUserVersion");
        String file = System.getProperty("user.dir") + "/src/test/resources/nft/image.png";
        List<String> files = Arrays.asList(file.split(" "));

        step("Go to home vertical product dashboard");
        verticalProductPage.goToVerticalProductPage(username, password);
        verticalProductPage.confirmationCountryButton();

        step("Verify home dashboard loaded");
        Assertion.assertThat("Verify home dashboard loaded", verticalProductPage.isHomeDashboardLoaded(), is(true));

        step("Go to Vertical Product menu");
        verticalProductPage.clickVerticalMenuButton();
        Assertion.assertThat("Verify vertical product page loaded", verticalProductPage.isVerticalProductPageLoaded(), is(true));

        step("Go to Add Vertical Page");
        verticalProductPage.newTabLoaded();
        Assertion.assertThat("Verify add vertical product page loaded", verticalProductPage.isAddVerticalProductPageLoaded(), is(true));

        step("Input Code");
        verticalProductPage.inputCode();

        step("Input Tracker Code");
        verticalProductPage.inputTrackerCode(trackerCode);

        step("Choose Currency");
        verticalProductPage.clickCurrencyType();

        step("Turn On Labels");
        verticalProductPage.clickToggleLabel();

        step("Input Content Labels");
        verticalProductPage.inputContentLabel(contentLabelId, contentLabelEn);

        step("Input Hex Color");
        verticalProductPage.inputColorHex(hexColor);

        step("Choose period: Always On");
        verticalProductPage.choosePeriodAlwaysOn();

        step("Choose Platform");
        verticalProductPage.choosePlatform();

        step("Input App Version");
        verticalProductPage.inputAppVersion(appVersionStart, appVersionEnd);

        step("Choose Type Action & Input URL slug");
        verticalProductPage.chooseTypeAction();
        verticalProductPage.inputUrlSlug(urlSlug);

        step("Upload Icon");
        verticalProductPage.uploadIcon(files);

        step("Input Name to User");
        verticalProductPage.inputNameToUser(nameToUserId, nameToUserEn, nameToUserVersion);

        step("Click Submit");
        verticalProductPage.clickSubmitButton();

        step("Click Edit Vertical");
        verticalProductPage.goToEditVertical();

        step("Assertion Inputted Data");
        Map<String, String> data = verticalProductPage.verifyAllData();
        Assertion.assertThat("Verify code input", data.get("uniqueCode"), is(verticalProductPage.getUniqueCodeValue()));
        Assertion.assertThat("Verify tracker code input", data.get("trackerCode"), is(trackerCode));
        Assertion.assertThat("Verify currency  input", data.get("currency"), is(currency));
        Assertion.assertThat("Verify content label Id input", data.get("contentLabelId"), is(contentLabelId));
        Assertion.assertThat("Verify content label En input", data.get("contentLabelEn"), is(contentLabelEn));
        Assertion.assertThat("Verify hexColor input", data.get("hexColor"), is(hexColor));
        Assertion.assertThat("Verify platform input", data.get("platform"), is(platform));
        Assertion.assertThat("Verify app version start input", data.get("appVersionStart"), is(appVersionStart));
        Assertion.assertThat("Verify app version end input", data.get("appVersionEnd"), is(appVersionEnd));
        Assertion.assertThat("Verify type action input mweb redirection checked", verticalProductPage.isTypeActionRedirectChosen(), is(true));
        Assertion.assertThat("Verify url slug input", data.get("urlSlug"), is(urlSlug));
        Assertion.assertThat("Verify name to user Id input", data.get("nameToUserId"), is(nameToUserId));
        Assertion.assertThat("Verify name to user En input", data.get("nameToUserEn"), is(nameToUserEn));
        Assertion.assertThat("Verify name to user Version input", data.get("nameToUserVersion"), is(nameToUserVersion));
    }


    @Author(names = {"Fandy"})
    @Module(name = "DLP")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testAddDynamicLandingPage() {
        Map<String, String> param = getParam("testAddDynamicLandingPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        DynamicLandingPage<?> dynamicLandingPage = new DynamicLandingPage<>(session);
        String username = Objects.requireNonNull(param.get("username"), "Missing test data key: username");
        String password = Objects.requireNonNull(param.get("password"), "Missing test data key: password");
        String metaTitleEn = Objects.requireNonNull(param.get("metaTitleEn"), "Missing test data key: metaTitleEn").trim();
        String metaDescriptionEn = Objects.requireNonNull(param.get("metaDescriptionEn"), "Missing test data key: metaDescriptionEn").trim();
        String metaTitleId = Objects.requireNonNull(param.get("metaTitleId"), "Missing test data key: metaTitleId").trim();
        String metaDescriptionId = Objects.requireNonNull(param.get("metaDescriptionId"), "Missing test data key: metaDescriptionId").trim();
        String overLayColorHex = Objects.requireNonNull(param.get("overLayColorHex"), "Missing test data key: overLayColorHex").trim();
        String mobileIcon = "src/test/resources/nft/image.png";
        String desktopIcon = "src/test/resources/nft/desktop.jpg";

        step("Go to Home Page dashboard");
        dynamicLandingPage.goToDynamicLandingPage(username,password);
        dynamicLandingPage.clickGetInButton();
        Assertion.assertThat("Verify home page loaded", dynamicLandingPage.isHomePageLoaded(), is(true));

        step("Go to Dynamic Landing Page dashboard");
        dynamicLandingPage.clickDynamicLandingPageSideMenu();
        Assertion.assertThat("Verify dynamic landing page loaded", dynamicLandingPage.isDynamicLandingPageLoaded(), is(true));

        step("Click add new page");
        dynamicLandingPage.clickAddNewPage();
        Assertion.assertThat("Verify general content page loaded", dynamicLandingPage.isGeneralContentPageLoaded(), is(true));

        step("Fill input page name");
        dynamicLandingPage.turnOnToggleActive();
        dynamicLandingPage.inputPageName();

        step("Input slug");
        dynamicLandingPage.inputSlug();

        step("Click toggle Always ON");
        dynamicLandingPage.clickPeriodPageToggle();

        step("Fill all contents");
        dynamicLandingPage.fillAllContents(
                metaTitleEn
                ,metaDescriptionEn
                ,metaTitleId
                ,metaDescriptionId
                ,overLayColorHex
        );

        step("Upload Header Image");
        dynamicLandingPage.uploadIcons(mobileIcon, desktopIcon);

        step("Choose Text Color");
        dynamicLandingPage.chooseTextColor();

        step("Click Submit");
        dynamicLandingPage.clickSubmit();
        Assertion.assertThat("Verify Go back to Dynamic Landing Page after Submit", dynamicLandingPage.isDynamicLandingPageLoaded(), is(true));

        step("Go to latest created DLP ");
        dynamicLandingPage.editLatestCreated();

        step("Assertion Inputted Data");
        Map<String, String> dataDisplayed = dynamicLandingPage.verifyInputtedData();
        Assertion.assertThat("Verify meta title en", dataDisplayed.get("metaTitleEn"), is(metaTitleEn));
        Assertion.assertThat("Verify meta description en", dataDisplayed.get("metaDescriptionEn"), is(metaDescriptionEn));
        Assertion.assertThat("Verify meta title id", dataDisplayed.get("metaTitleId"), is(metaTitleId));
        Assertion.assertThat("Verify meta description id", dataDisplayed.get("metaDescriptionId"), is(metaDescriptionId));
        Assertion.assertThat("Verify overLay Color Hex input", dataDisplayed.get("overLayColorHex"), is(overLayColorHex));
        Assertion.assertThat("Verify page name", dataDisplayed.get("pageName"), is(dynamicLandingPage.getPageNameInput()));
        Assertion.assertThat("Verify URL Slug", dataDisplayed.get("urlSlug"), is(dynamicLandingPage.getslugInput()));

        step("Archive latest created DLP");
        Assertion.assertThat("Verify title is same before archiving", dynamicLandingPage.isTitleSame(), is(true));
        dynamicLandingPage.archiveNewCreatedPage();
        Assertion.assertThat("Verify Snackbar Success Archive Appear", dynamicLandingPage.isSnackbarSuccessAppear(), is(true));
    }
}

