package com.tiket.web.dashboard.test.platform.discovery;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.discovery.CreateGlobalSearchModule;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.is;

public class CreateGlobalSearchModuleTest extends BaseTest {
    @Author(names = {"Ankit Bahadur"})
    @Module(name = "GlobalSearch")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testCreateGlobalSearchModule() {
        String file = "src/test/resources/dashboard/platform.discovery/purple.png";
        Map<String, String> map = getParam("testCreateGlobalSearchModule");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        CreateGlobalSearchModule<?> createGlobalSearchModule = new CreateGlobalSearchModule<>(session);
        String username = map.get("username");
        String password = map.get("password");

        step("Given admin go to Global Search Section");
        createGlobalSearchModule.goToGlobalSearchEditGeneral(username, password);

        step("When admin add new Global Search Section");
        createGlobalSearchModule.addNewGlobalSearchSection();

        step("When admin add Module Name");
        createGlobalSearchModule.addModuleName();

        step("When admin select Template Code");
        createGlobalSearchModule.selectTemplateCode();

        step("When admin Select Currency");
        createGlobalSearchModule.selectCurrency();

        step("When admin Select Show users");
        createGlobalSearchModule.selectShowUsers();

        step("When admin Select Period");
        createGlobalSearchModule.selectPeriod();

        step("When admin Set Heading Title");
        createGlobalSearchModule.setHeadingTitleEn();
        createGlobalSearchModule.setHeadingTitleId();

        step("When admin Set Heading SubTitle");
        createGlobalSearchModule.setHeadingSubTitleEn();
        createGlobalSearchModule.setHeadingSubTitleId();

        step("When admin save and go to content");
        boolean successMsg = createGlobalSearchModule.clickSaveAndGotoContent();
        Assertion.assertThat("Success add custom module", successMsg, is(true));

        step("When admin toggle see all button");
        createGlobalSearchModule.toggleSeeAllButton();

        step("When admin enter See All Url");
        createGlobalSearchModule.enterSeeAllUrl();

        step("When admin upload Image");
        createGlobalSearchModule.uploadImageId(file);
        createGlobalSearchModule.uploadImageEn(file);

        step("When admin add Title");
        createGlobalSearchModule.enterTitleId();
        createGlobalSearchModule.enterTitleEn();

        step("When admin toggle subtitle Red");
        createGlobalSearchModule.toggleSubtitleRed();

        step("When admin add Subtitle");
        createGlobalSearchModule.enterSubTitleId();
        createGlobalSearchModule.enterSubTitleEn();

        step("When admin click submit content");
        createGlobalSearchModule.clickSubmitContent();

        step("Verify that the Global Search Edit Content page is submitted successfully");
        boolean successMsg1 = createGlobalSearchModule.submitSuccessContent();
        Assertion.assertThat("Success submit module", successMsg1, is(true));

        step("When admin archive the module");
        createGlobalSearchModule.archiveGlobalSearch();

        step("Verify that the Global Search module is archived successfully");
        boolean successMsg2 = createGlobalSearchModule.successArchive();
        Assertion.assertThat("Success Archived Module", successMsg2, is(true));
    }
}
