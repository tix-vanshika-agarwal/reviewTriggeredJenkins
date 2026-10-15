package com.tiket.web.dashboard.test.platform.message;

import java.lang.reflect.Method;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.appium.DriverTools;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import static com.tiket.testdata.TestData.getParam;
import com.tiket.web.dashboard.page.platform.message.HelpCenterCategoriesPage;
import com.tiket.web.dashboard.page.platform.message.HelpCenterHelpTopicPage;
import com.tiket.web.dashboard.page.platform.message.HelpCenterSubtopicPage;
import com.tiket.web.dashboard.page.platform.message.HelpCenterTopicPage;

public class HelpCenterTest extends BaseTest {

    @AfterMethod
    public void cleanUpAfterTest(ITestContext context, Method method) {
        Session<?> session = sessionThreadLocal.get();
        if (session == null || session.driver() == null) return;

        WebDriver webDriver = (WebDriver) session.driver();
        new DriverTools<>(webDriver).closeCurrentTab();
    }

    @Author(names = {"Fandy"})
    @Module(name = "HelpCenter")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testAddCategoryHelpCenter() throws InterruptedException {
        Map<String, String> map = getParam("testAddCategoryHelpCenter");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HelpCenterCategoriesPage<?> helpCenterCategoriesPage = new HelpCenterCategoriesPage<>(session);

        final String username = map.get("username");
        final String password = map.get("password");
        final String urlSlugId = helpCenterCategoriesPage.generateUniqueSlug("automation-category-id");
        final String urlSlugEn = helpCenterCategoriesPage.generateUniqueSlug("automation-category-en");
        final String categoryNameId = map.get("categoryNameId");
        final String categoryNameEn = map.get("categoryNameEn");
        final String metaTitleId = map.get("metaTitleId");
        final String metaTitleEn = map.get("metaTitleEn");
        final String metaDescriptionId = map.get("metaDescriptionId");
        final String metaDescriptionEn = map.get("metaDescriptionEn");

        step("Admin go to Help Center Page");
        helpCenterCategoriesPage.goToHelpCenterPage(username, password);
        Assertion.assertThat("Verify help center page loaded", helpCenterCategoriesPage.isHelpCenterPageLoaded(username), is(true));

        step("Admin click Add Category Button");
        helpCenterCategoriesPage.clickAddNewCategoriesButton();

        step("Input details field");
        helpCenterCategoriesPage.inputContentCategory(
                urlSlugId,
                urlSlugEn,
                categoryNameId,
                categoryNameEn,
                metaTitleId,
                metaTitleEn,
                metaDescriptionId,
                metaDescriptionEn
        );

        step("Click save button");
        helpCenterCategoriesPage.clickSaveButton();

        step("Scroll Down and Verify Newly Added Category");
        helpCenterCategoriesPage.scrollAndEdit();

        step("Verify Indonesia Content");
        Map<String, String> resultContentDataId = helpCenterCategoriesPage.verifyContentDataId();
        Assertion.assertThat("Verify slug url ID", resultContentDataId.get("urlSlugId"), is(notNullValue()));
        Assertion.assertThat("Verify category name ID", resultContentDataId.get("categoryNameId"), is(notNullValue()));
        Assertion.assertThat("Verify meta title ID", resultContentDataId.get("metaTitleId"), is(metaTitleId));
        Assertion.assertThat("Verify meta description ID", resultContentDataId.get("metaDescriptionId"), is(metaDescriptionId));

        step("Verify English Content");
        Map<String, String> resultContentDataEn = helpCenterCategoriesPage.verifyContentDataEn();
        Assertion.assertThat("Verify slug url EN", resultContentDataEn.get("urlSlugEn"), is(notNullValue()));
        Assertion.assertThat("Verify category name EN", resultContentDataEn.get("categoryNameEn"), is(notNullValue()));
        Assertion.assertThat("Verify meta title EN", resultContentDataEn.get("metaTitleEn"), is(metaTitleEn));
        Assertion.assertThat("Verify meta description EN", resultContentDataEn.get("metaDescriptionEn"), is(metaDescriptionEn));

        step("Verify Toggle Button ON");
        helpCenterCategoriesPage.verifyToggleButtonActive();
        Assertion.assertThat("Verify toggle published on", helpCenterCategoriesPage.verifyToggleButtonActive(), is(true));

        step("Verify Delete Category");
        helpCenterCategoriesPage.clickDeleteButton();
        Assertion.assertThat("Verify category deleted snackbar ", helpCenterCategoriesPage.verifySnackbarSuccessDelete(), is(true));
    }
  
    @Author(names = {"Fandy"})
    @Module(name = "HelpCenter")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testAddTopicHelpCenter() throws InterruptedException {
        Map<String, String> map = getParam("testAddTopicHelpCenter");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HelpCenterCategoriesPage<?> helpCenterCategoriesPage = new HelpCenterCategoriesPage<>(session);
        HelpCenterTopicPage<?> helpCenterTopicPage = new HelpCenterTopicPage<>(session);

        final String username = map.get("username");
        final String password = map.get("password");
        final String urlSlugId = helpCenterCategoriesPage.generateUniqueSlug("automation-category-id");
        final String urlSlugEn = helpCenterCategoriesPage.generateUniqueSlug("automation-category-en");
        final String categoryNameId = map.get("categoryNameId");
        final String categoryNameEn = map.get("categoryNameEn");
        final String metaTitleId = map.get("metaTitleId");
        final String metaTitleEn = map.get("metaTitleEn");
        final String metaDescriptionId = map.get("metaDescriptionId");
        final String metaDescriptionEn = map.get("metaDescriptionEn");

        final String urlSlugTopicId = helpCenterCategoriesPage.generateUniqueSlug("automation-topic-id");
        final String urlSlugTopicEn = helpCenterCategoriesPage.generateUniqueSlug("automation-topic-en");
        final String topicNameId = map.get("topicNameId");
        final String metaTitleTopicId = map.get("metaTitleTopicId");
        final String metaDescTopicId = map.get("metaDescTopicId");
        final String topicNameEn = map.get("topicNameEn");
        final String metaTitleTopicEn = map.get("metaTitleTopicEn");
        final String metaDescTopicEn = map.get("metaDescTopicEn");

        step("Admin go to Help Center Page");
        helpCenterCategoriesPage.goToHelpCenterPage(username, password);
        Assertion.assertThat("Verify help center page loaded", helpCenterCategoriesPage.isHelpCenterPageLoaded(username), is(true));

        step("Admin Create Category");
        helpCenterCategoriesPage.clickAddNewCategoriesButton();
        helpCenterCategoriesPage.inputContentCategory(
                urlSlugId,
                urlSlugEn,
                categoryNameId,
                categoryNameEn,
                metaTitleId,
                metaTitleEn,
                metaDescriptionId,
                metaDescriptionEn
        );
        helpCenterCategoriesPage.clickSaveButton();
        Assertion.assertThat("Verify New Category Exist", helpCenterTopicPage.newCategoryExist(), is(true));

        step("Admin Click Add New Topic");
        helpCenterTopicPage.clickAddTopic();
        Assertion.assertThat("Verify Modal Topic Appear", helpCenterTopicPage.modalTopicButton(), is(true));

        step("Admin input details Topic");
        helpCenterTopicPage.inputDetailsTopic(
            urlSlugTopicId,
            topicNameId,
            metaTitleTopicId,
            metaDescTopicId,
            urlSlugTopicEn,
            topicNameEn,
            metaTitleTopicEn,
            metaDescTopicEn
        );

        step("Click Save Button");
        helpCenterTopicPage.clickSaveButton();
        Assertion.assertThat("Verify success snackbar appear", helpCenterTopicPage.saveSuccessSnackbar(), is(true));

        step("Click New Topic");
        helpCenterTopicPage.clickNewTopic();
        Assertion.assertThat("Verify Topic Title Appears", helpCenterTopicPage.verifyTitleTopic(), is(true));

        step("Click Edit Topic Button");
        helpCenterTopicPage.editLatestTopic();

        step("Verify Topic with Indonesia Content");
        Map<String, String> resultTopicDataId = helpCenterTopicPage.verifyTopicContentId();
        Assertion.assertThat("Verify Topic Slug Url ID", resultTopicDataId.get("urlSlugTopicId"), is(notNullValue()));
        Assertion.assertThat("Verify Topic Name ID", resultTopicDataId.get("topicNameId"), is(topicNameId));
        Assertion.assertThat("Verify Topic Meta Title ID", resultTopicDataId.get("metaTitleTopicId"), is(metaTitleTopicId));
        Assertion.assertThat("Verify Topic Meta Desc ID", resultTopicDataId.get("metaDescTopicId"), is(metaDescTopicId));

        step("Verify Topic with English Content");
        Map<String, String> resultTopicDataEn = helpCenterTopicPage.verifyTopicContentEn();
        Assertion.assertThat("Verify Topic Slug Url EN", resultTopicDataEn.get("urlSlugTopicEn"), is(notNullValue()));
        Assertion.assertThat("Verify Topic Name EN", resultTopicDataEn.get("topicNameEn"), is(topicNameEn));
        Assertion.assertThat("Verify Topic Meta Title EN", resultTopicDataEn.get("metaTitleTopicEn"), is(metaTitleTopicEn));
        Assertion.assertThat("Verify Topic Meta Desc EN", resultTopicDataEn.get("metaDescTopicEn"), is(metaDescTopicEn));

        step("Verify Toggle Button Active");
        Assertion.assertThat("Toggle Button ON", helpCenterTopicPage.verifyToggleButtonActive(), is(true));

        step("Click Delete Topic Button");
        helpCenterTopicPage.clickDeleteButton();

        step("Verify Edit & Delete Category");
        helpCenterTopicPage.scrollAndEdit();
        helpCenterTopicPage.clickDeleteCategory();
        Assertion.assertThat("Verify category deleted snackbar ", helpCenterTopicPage.verifySnackbarSuccessDelete(), is(true));
    }

    @Author(names = {"Fandy"})
    @Module(name = "HelpCenter")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testAddSubtopicHelpCenter() throws InterruptedException, NullPointerException {
        Map<String, String> map = getParam("testAddSubtopicHelpCenter");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HelpCenterCategoriesPage<?> helpCenterCategoriesPage = new HelpCenterCategoriesPage<>(session);
        HelpCenterTopicPage<?> helpCenterTopicPage = new HelpCenterTopicPage<>(session);
        HelpCenterSubtopicPage<?> helpCenterSubtopicPage = new HelpCenterSubtopicPage<>(session);

        final String username = map.get("username");
        final String password = map.get("password");
        final String urlSlugId = helpCenterCategoriesPage.generateUniqueSlug("automation-category-id");
        final String urlSlugEn = helpCenterCategoriesPage.generateUniqueSlug("automation-category-en");
        final String categoryNameId = map.get("categoryNameId");
        final String categoryNameEn = map.get("categoryNameEn");
        final String metaTitleId = map.get("metaTitleId");
        final String metaTitleEn = map.get("metaTitleEn");
        final String metaDescriptionId = map.get("metaDescriptionId");
        final String metaDescriptionEn = map.get("metaDescriptionEn");

        final String urlSlugTopicId = helpCenterCategoriesPage.generateUniqueSlug("automation-topic-id");
        final String urlSlugTopicEn = helpCenterCategoriesPage.generateUniqueSlug("automation-topic-en");
        final String topicNameId = map.get("topicNameId");
        final String metaTitleTopicId = map.get("metaTitleTopicId");
        final String metaDescTopicId = map.get("metaDescTopicId");
        final String topicNameEn = map.get("topicNameEn");
        final String metaTitleTopicEn = map.get("metaTitleTopicEn");
        final String metaDescTopicEn = map.get("metaDescTopicEn");
        final String urlSlugSubTopicId = helpCenterCategoriesPage.generateUniqueSlug("automation-Subtopic-id");
        final String urlSlugSubTopicEn = helpCenterCategoriesPage.generateUniqueSlug("automation-Subtopic-en");
        final String subtopicNameId = map.get("topicNameId");
        final String metaTitleSubtopicId = map.get("metaTitleTopicId");
        final String metaDescSubtopicId = map.get("metaDescTopicId");
        final String subtopicNameEn = map.get("topicNameEn");
        final String metaTitleSubtopicEn = map.get("metaTitleTopicEn");
        final String metaDescSubtopicEn = map.get("metaDescTopicEn");

        step("Admin go to Help Center Page");
        helpCenterCategoriesPage.goToHelpCenterPage(username, password);
        Assertion.assertThat("Verify help center page loaded", helpCenterCategoriesPage.isHelpCenterPageLoaded(username), is(true));

        step("Admin Create Category");
        helpCenterCategoriesPage.clickAddNewCategoriesButton();
        helpCenterCategoriesPage.inputContentCategory(
                urlSlugId,
                urlSlugEn,
                categoryNameId,
                categoryNameEn,
                metaTitleId,
                metaTitleEn,
                metaDescriptionId,
                metaDescriptionEn
        );
        helpCenterCategoriesPage.clickSaveButton();
        Assertion.assertThat("Verify New Category Exist", helpCenterTopicPage.newCategoryExist(), is(true));

        step("Admin Create New Topic");
        helpCenterTopicPage.clickAddTopic();
        Assertion.assertThat("Verify Modal Topic Appear", helpCenterTopicPage.modalTopicButton(), is(true));
        helpCenterTopicPage.inputDetailsTopic(
                urlSlugTopicId,
                topicNameId,
                metaTitleTopicId,
                metaDescTopicId,
                urlSlugTopicEn,
                topicNameEn,
                metaTitleTopicEn,
                metaDescTopicEn
        );
        helpCenterTopicPage.clickSaveButton();
        Assertion.assertThat("Verify success snackbar appear", helpCenterTopicPage.saveSuccessSnackbar(), is(true));
        helpCenterTopicPage.clickNewTopic();
        Assertion.assertThat("Verify Topic Title Appears", helpCenterTopicPage.verifyTitleTopic(), is(true));

        step("Admin Create New Subtopic");
        helpCenterSubtopicPage.clickAddSubtopic();
        Assertion.assertThat("Verify modal is visible", helpCenterSubtopicPage.isModalSubtopicAppear(), is(true));

        step("Input New Subtopic");
        helpCenterSubtopicPage.inputDetailsSubtopic(
                urlSlugSubTopicId,
                subtopicNameId,
                metaTitleSubtopicId,
                metaDescSubtopicId,
                urlSlugSubTopicEn,
                subtopicNameEn,
                metaTitleSubtopicEn,
                metaDescSubtopicEn
        );
        Assertion.assertThat("Verify modal is visible", helpCenterSubtopicPage.isModalSubtopicAppear(), is(true));
        helpCenterSubtopicPage.clickSaveButton();

        step("Click Edit Subtopic Button");
        helpCenterSubtopicPage.clickEditSubtopic();

        step("Verify Subtopic with Indonesia Content");
        Map<String, String> resultSubtopicDataId = helpCenterSubtopicPage.verifySubtopicContentId();
        Assertion.assertThat("Verify Subtopic Slug Url ID", resultSubtopicDataId.get("urlSlugSubtopicId"), is(urlSlugSubTopicId));
        Assertion.assertThat("Verify Subtopic Name ID", resultSubtopicDataId.get("subtopicNameId"), is(subtopicNameId));
        Assertion.assertThat("Verify Subtopic Meta Title ID", resultSubtopicDataId.get("metaTitleSubtopicId"), is(metaTitleSubtopicId));
        Assertion.assertThat("Verify Subtopic Meta Desc ID", resultSubtopicDataId.get("metaDescSubtopicId"), is(metaDescSubtopicId));

        step("Verify Subtopic with English Content");
        Map<String, String> resultSubtopicDataEn = helpCenterSubtopicPage.verifySubtopicContentEn();
        Assertion.assertThat("Verify Subtopic Slug Url EN", resultSubtopicDataEn.get("urlSlugSubtopicEn"), is(urlSlugSubTopicEn));
        Assertion.assertThat("Verify Subtopic Name EN", resultSubtopicDataEn.get("subtopicNameEn"), is(subtopicNameEn));
        Assertion.assertThat("Verify Subtopic Meta Title EN", resultSubtopicDataEn.get("metaTitleSubtopicEn"), is(metaTitleSubtopicEn));
        Assertion.assertThat("Verify Subtopic Meta Desc EN", resultSubtopicDataEn.get("metaDescSubtopicEn"), is(metaDescSubtopicEn));

        step("Verify Toggle Button Subtopic is Active");
        Assertion.assertThat("Toggle Button Subtopic is ON", helpCenterSubtopicPage.verifyToggleButtonActive(), is(true));

        step("Click Delete Subtopic Button");
        helpCenterSubtopicPage.clickDeleteButton();

        step("Click Edit & Delete Topic Button");
        helpCenterTopicPage.editLatestTopic();
        helpCenterTopicPage.clickDeleteButton();

        step("Verify Edit & Delete Category");
        helpCenterTopicPage.scrollAndEdit();
        helpCenterTopicPage.clickDeleteCategory();
        Assertion.assertThat("Verify category deleted snackbar ", helpCenterTopicPage.verifySnackbarSuccessDelete(), is(true));
    }

    @Author(names = {"Fandy"})
    @Module(name = "HelpCenter")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testCreateHelpTopicHelpCenter() throws InterruptedException, NullPointerException{
        Map<String, String> map = getParam("testCreateHelpTopicHelpCenter");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HelpCenterHelpTopicPage<?> helpCenterHelpTopicPage = new HelpCenterHelpTopicPage(session);

        final String username = map.get("username");
        final String password = map.get("password");

        step("Go to Help Topic Page");
        helpCenterHelpTopicPage.goToHelpTopicPageViaUI(username, password);

        step("Add help topic");
        helpCenterHelpTopicPage.addHelpTopic();

        step("Fill details help topic");
        helpCenterHelpTopicPage.fillDetailsHelpTopic();

        step("Verify content");
        helpCenterHelpTopicPage.scrollEditRecentCreatedHC();
        helpCenterHelpTopicPage.verifyContentDetails();

        step("Delete help topic");
        helpCenterHelpTopicPage.deleteHelpTopic();
    }
}