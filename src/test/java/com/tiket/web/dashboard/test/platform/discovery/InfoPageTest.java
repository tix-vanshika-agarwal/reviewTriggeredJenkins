package com.tiket.web.dashboard.test.platform.discovery;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.discovery.InfoPage;
import org.testng.annotations.Test;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public class InfoPageTest extends BaseTest {
    @Author(names = {"Amit Bhardwaj"})
    @Module(name = "InfoPage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testAddAndApproveInfoPage() {
        Map<String, String> map = getParam("testAddAndApproveInfoPage");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        InfoPage<?> infoPage = new InfoPage<>(session);
        final String username = map.get("username");
        final String password = map.get("password");

        step("Given admin go to Info Page");
        infoPage.goToInfoPageViaUI(username, password);

        step("When admin add new Info page");
        infoPage.addNewInfoPage();
        infoPage.addPageUrlSlug();

        step("When admin fill Info Page Title");
        infoPage.infoPageTitleEn();
        infoPage.infoPageTitleId();

        step("When admin fill Info Page Meta Description");
        infoPage.metaDescriptionEn();
        infoPage.metaDescriptionId();

        step("When admin fill Info Page Section");
        infoPage.infoSectionName();
        infoPage.infoId();

        step("When admin fill Info Page Type and Style");
        infoPage.infoType();
        infoPage.infoSelectType();
        infoPage.infoStyle();
        infoPage.infoSelectStyle();

        step("When admin fill Info Page Title text");
        infoPage.titleTextEn();
        infoPage.titleTextId();
        infoPage.bodyTextEn();
        infoPage.bodyTextIdSelection();
        infoPage.bodyTextId();

        step("Submit the Info Page");
        infoPage.infoSubmit();

        step("Verify that the Info Page was created successfully");
        boolean successMsg=  infoPage.infoPageSuccessMsg();

        step("Approve the created Info Page");
        infoPage.approveInfoPage();
        infoPage.approveSelectedInfoPage();

        Assertion.assertThat("Successfully create the info page", successMsg,is(true));
        Assertion.assertThat("Approve the created Info Page", infoPage.successApproveInfoPage(),is(true));
        step("Check Info Page Status");
        infoPage.checkInfoStatus();
    }

    @Author(names = {"Fandy"})
    @Module(name = "InfoPage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testAddEditInfoPage() throws ExecutionException, InterruptedException {
        Map<String, String> map = getParam("testAddEditInfoPage");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        InfoPage<?> infoPage = new InfoPage<>(session);

        final String username = map.get("username");
        final String password = map.get("password");

        step("Given admin go to Info Page");
        infoPage.goToInfoPage(username, password);

        step("When admin add new Info page");
        infoPage.addNewInfoPage();
        infoPage.addPageUrlSlug();

        step("When admin fill all the necessary Info Page Title & click submit");
        infoPage.infoPageTitleEn();
        infoPage.infoPageTitleId();
        infoPage.metaDescriptionEn();
        infoPage.metaDescriptionId();
        infoPage.infoSectionName();
        infoPage.infoId();
        infoPage.infoType();
        infoPage.infoSelectType();
        infoPage.infoStyle();
        infoPage.infoSelectStyle();
        infoPage.titleTextEn();
        infoPage.titleTextId();
        infoPage.bodyTextEn();
        infoPage.bodyTextId();
        infoPage.infoSubmit();
        boolean successMsg = infoPage.infoPageSuccessMsg();

        step("Approve the created Info Page");
        infoPage.approveInfoPage();
        infoPage.approveSelectedInfoPage();
        Assertion.assertThat("Successfully create the info page", successMsg,is(true));
        Assertion.assertThat("Approve the created Info Page", infoPage.successApproveInfoPage(),is(true));
        infoPage.checkInfoStatus();

        step("Admin can edit & submit recently added Info page");
        infoPage.editRecentlyAddedInfoPage();
        infoPage.infoSubmit();
        infoPage.verifyEditedInfoPage();

        step("Admin can verify the content");
        Map<String, String> dataToValidate = infoPage.getInfoPageDataFromAPI();
        String responseApi = dataToValidate.get("response");
        String codeApi = dataToValidate.get("code");
        String dataIdApi = dataToValidate.get("dataId");
        String pageSlugIDApi = dataToValidate.get("pageSlugID");
        String pageSlugENApi = dataToValidate.get("pageSlugEN");
        String pageTitleIDApi = dataToValidate.get("pageTitleID");
        String pageTitleENApi = dataToValidate.get("pageTitleEN");
        String contentIdApi = dataToValidate.get("contentId");
        String languageIDTextApi = dataToValidate.get("languageIDText");
        String languageENTextApi = dataToValidate.get("languageENText");
        String pageNameApi = dataToValidate.get("pageName");
        String statusApi = dataToValidate.get("status");
        log("dataIdApi = " + dataIdApi);

        Assertion.assertThat("Verify response from API is not null", responseApi, is(notNullValue()));
        Assertion.assertThat("Verify code from API is not null", codeApi, is(notNullValue()));
        Assertion.assertThat("Verify dataId from API is not null", dataIdApi, is(notNullValue()));
        Assertion.assertThat("Verify pageSlug ID from API is not null", pageSlugIDApi, is(notNullValue()));
        Assertion.assertThat("Verify pageSlug EN from API is not null", pageSlugENApi, is(notNullValue()));
        Assertion.assertThat("Verify pageTitle ID from API is not null", pageTitleIDApi, is(notNullValue()));
        Assertion.assertThat("Verify pageTitle EN from API is not null", pageTitleENApi, is(notNullValue()));
        Assertion.assertThat("Verify contentId from API is not null", contentIdApi, is(notNullValue()));
        Assertion.assertThat("Verify languageIDText from API is not null", languageIDTextApi, is(notNullValue()));
        Assertion.assertThat("Verify languageENText from API is not null", languageENTextApi, is(notNullValue()));
        Assertion.assertThat("Verify status from API is not null", statusApi, is(notNullValue()));
        Assertion.assertThat("Verify pageName contains 'edited'", pageNameApi, containsStringIgnoringCase("edited"));
    }
}