package com.tiket.web.dashboard.test.platform.discovery;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import com.tiket.web.dashboard.page.platform.discovery.PromoPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class PromoTest extends BaseTest {

    @Author(names = {"Fandy"})
    @Module(name = "Promo")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testAddPromo() throws InterruptedException {
        Map<String, String> param = getParam("testAddPromo");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        PromoPage<?> promoPage = new PromoPage(session);

        String username = param.get("username");
        String password = param.get("password");
        String promoName = param.get("promoName");
        String category = param.get("category");
        String subCategory = param.get("subCategory");
        String promoSlug = param.get("promoSlug");
        String currency = param.get("currency");
        String promoMetaTitleEn = param.get("promoMetaTitleEn");
        String promoMetaDescEn = param.get("promoMetaDescEn");
        String promoShareTextEn = param.get("promoShareTextEn");
        String promoMetaTitleId = param.get("promoMetaTitleId");
        String promoMetaDescId = param.get("promoMetaDescId");
        String promoShareTextId = param.get("promoShareTextId");
        String contentTitleId = param.get("contentTitleId");
        String contentDescriptionId = param.get("contentDescriptionId");
        String contentTitleEn = param.get("contentTitleEn");
        String contentDescriptionEn = param.get("contentDescriptionEn");
        String expectedContentTitleId = param.get("expectedContentTitleId");
        String expectedContentDescId = param.get("expectedContentDescId");
        String expectedContentTitleEn = param.get("expectedContentTitleEn");
        String expectedContentDescEn = param.get("expectedContentDescEn");
        String file = "src/test/resources/nft/bannerpromopage.png";

        step("Go to Promo dashboard");
        promoPage.goToPromoDashboard(username, password);
        promoPage.clickPromoSideMenu();
        Assertion.assertThat("Verify Promo Page loaded", promoPage.isPromoPageLoaded(), is(true));

        step("Click create promo");
        promoPage.createNewPromo();
        Assertion.assertThat("Verify General Setting Loaded", promoPage.isGeneralSettingPageLoaded(), is(true));

        step("Input details general settings dashboard");
        promoPage.inputPromoName(promoName);
        promoPage.inputCategories();
        promoPage.chooseAllPlatform();
        promoPage.chooseActivePeriod();
        promoPage.showBannerCustomData();

        step("Input general settings SEO");
        promoPage.inputSlug();
        promoPage.inputGeneralSettingSEO(
                promoMetaTitleEn
                ,promoMetaDescEn
                ,promoShareTextEn
                ,promoMetaTitleId
                ,promoMetaDescId
                ,promoShareTextId
        );

        step("Click save and go to content");
        promoPage.clickSaveGoToContent();

        step("Verify general content page is loaded");
        Assertion.assertThat("General content page is loaded", promoPage.isGeneralContentLoaded(), is(true));

        step("Verify upload banner & its contents");
        promoPage.uploadBanner(file);
        promoPage.fillContents(
                contentTitleId
                ,contentDescriptionId
                ,contentTitleEn
                ,contentDescriptionEn
        );

        step("Additional Info Turned OFF");
        promoPage.clickAdditionalInfoOff();

        step("Turn countdown and promo code value No");
        promoPage.clickCountDown();
        promoPage.clickUsePromoCode();
        promoPage.clickSubmit();

        step("Snackbar success save content shown");
        Assertion.assertThat("Success save content ", promoPage.isSnackBarSuccessGeneralContentsAppear(), is(true));

        step("Verify recent created promo");
        promoPage.clickSaveAndGoToSettings();

        step("Verify inputted data on General Settings");
        Map<String, String> generalSettingsData = promoPage.verifyGeneralSettingsData();
        Assertion.assertThat("Verify Promo Name", generalSettingsData.get("promoName"), is(promoName));
        Assertion.assertThat("Verify Category Value", generalSettingsData.get("categoryValue"), is(category));
        Assertion.assertThat("Verify Sub Category Value", generalSettingsData.get("subCategoryValue"), is(subCategory));
        Assertion.assertThat("Verify Currency Value", generalSettingsData.get("currencyValue"), is(currency));
        Assertion.assertThat("Verify Promo Slug", generalSettingsData.get("promoSlug"), is(promoPage.getGeneratedSlug()));
        Assertion.assertThat("Verify Promo Meta Title EN", generalSettingsData.get("promoMetaTitleEn"), is(promoMetaTitleEn));
        Assertion.assertThat("Verify Promo Meta Description EN", generalSettingsData.get("promoMetaDescEn"), is(promoMetaDescEn));
        Assertion.assertThat("Verify Promo Share Text EN", generalSettingsData.get("promoShareTextEn"), is(promoShareTextEn));
        Assertion.assertThat("Verify Promo Meta Title ID", generalSettingsData.get("promoMetaTitleId"), is(promoMetaTitleId));
        Assertion.assertThat("Verify Promo Meta Description ID", generalSettingsData.get("promoMetaDescId"), is(promoMetaDescId));
        Assertion.assertThat("Verify Promo Share Text ID", generalSettingsData.get("promoShareTextId"), is(promoShareTextId));
        promoPage.clickSaveGoToContent();

        step("Verify inputted data on General Contents Page");
        Map<String, String> generalContentData = promoPage.verifyGeneralContentData();
        Assertion.assertThat("Verify Content Title ID", generalContentData.get("contentTitleId"), is(contentTitleId));
        Assertion.assertThat("Verify Content Description ID", generalContentData.get("contentDescriptionId"), is(contentDescriptionId));
        Assertion.assertThat("Verify Content Title EN", generalContentData.get("contentTitleEn"), is(contentTitleEn));
        Assertion.assertThat("Verify Content Description EN", generalContentData.get("contentDescriptionEn"), is(contentDescriptionEn));
    }
}
