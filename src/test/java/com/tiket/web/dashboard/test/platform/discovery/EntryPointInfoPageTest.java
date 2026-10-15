package com.tiket.web.dashboard.test.platform.discovery;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.discovery.EntryPointInfoPage;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.*;

public class EntryPointInfoPageTest extends BaseTest {

    @Author(names = {"Shailja"})
    @Module(name = "EntryPoint")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testAddEditDeleteEntryPointTab() throws ExecutionException, InterruptedException {
        Map<String, String> map = getParam("testAddEditDeleteEntryPointTab");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        EntryPointInfoPage<?> entryPointPage = new EntryPointInfoPage<>(session);

        final String username = map.get("username");
        final String password = map.get("password");
        final String prefix = map.get("prefix");
        final String imagePath = map.get("imagePath");
        final String pageName = map.get("pageName");
        final String editSuffix = map.get("editSuffix");

        step("Step 1: Admin navigates to Entry Point dashboard");
        entryPointPage.goToEntryPointPage(username, password);
        entryPointPage.clickGetInButton();
        Assertion.assertThat("Entry Point page loaded successfully", entryPointPage.isEntryPointPageLoaded(username), is(true));

        step("Step 2: Admin adds a new tab");
        entryPointPage.addNewTab(prefix);
        Assertion.assertThat("Snackbar is visible after adding tab", entryPointPage.isSnackbarVisible(), is(true));

        step("Step 3: Verify new tab appears in the table");
        Assertion.assertThat("New tab is visible in the table", entryPointPage.verifyTabAddedInUI(), is(true));

        step("Step 4: Admin clicks Edit to enter tab detail page");
        entryPointPage.clickEditOnLastTab();

        step("Step 5: API verify tab exists with 0 info pages");
        Map<String, String> apiDataEmpty = entryPointPage.getEntryPointDataFromAPI();
        Assertion.assertThat("API returns SUCCESS for new tab", apiDataEmpty.get("code"), equalTo("SUCCESS"));
        Assertion.assertThat("API shows 0 info pages", apiDataEmpty.get("infoPagesCount"), equalTo("0"));

        step("Step 6: Admin adds a new info page");
        entryPointPage.addInfoPageInTab(prefix, imagePath, pageName);

        step("Step 7: API verify info page was added");
        Map<String, String> apiDataAdded = entryPointPage.getEntryPointDataFromAPI();
        Assertion.assertThat("API returns SUCCESS", apiDataAdded.get("code"), equalTo("SUCCESS"));
        Assertion.assertThat("API shows info pages count > 0", Integer.parseInt(apiDataAdded.get("infoPagesCount")), is(not(0)));
        Assertion.assertThat("API info page name matches prefix", apiDataAdded.get("infoPageName"), containsString(prefix));
        Assertion.assertThat("API info page has pageName", apiDataAdded.get("infoPagePageName"), is(notNullValue()));
        Assertion.assertThat("API info page has iconUrl", apiDataAdded.get("infoPageIconUrl"), is(notNullValue()));
        Assertion.assertThat("API info page status is ACTIVE", apiDataAdded.get("infoPageStatus"), equalTo("ACTIVE"));

        step("Step 8: Admin edits the tab title");
        entryPointPage.navigateBackToEntryPoint();
        entryPointPage.editInfoPageInTab(editSuffix);
        Assertion.assertThat("Snackbar is visible after editing", entryPointPage.isSnackbarVisible(), is(true));

        step("Step 9: API verify edited tab title");
        entryPointPage.navigateBackToEntryPoint();
        Map<String, String> apiDataEdited = entryPointPage.getEntryPointDataFromAPI();
        Assertion.assertThat("API returns SUCCESS after edit", apiDataEdited.get("code"), equalTo("SUCCESS"));
        Assertion.assertThat("API tab title EN contains edit suffix", apiDataEdited.get("titleEN"), containsString(editSuffix));
        Assertion.assertThat("API tab title ID contains edit suffix", apiDataEdited.get("titleID"), containsString(editSuffix));

        step("Step 10: Admin deletes the tab");
        entryPointPage.navigateBackToEntryPoint();
        entryPointPage.deleteLastTab();
        Assertion.assertThat("Snackbar is visible after deleting", entryPointPage.isSnackbarVisible(), is(true));
        Assertion.assertThat("Tab count returned to initial", entryPointPage.verifyTabDeletedInUI(), is(true));

        step("Step 11: API verify tab deleted");
        Map<String, String> apiDataDeleted = entryPointPage.getEntryPointDataAfterDeletion();
        Assertion.assertThat("API returns DATA_NOT_EXIST", apiDataDeleted.get("code"), equalTo("DATA_NOT_EXIST"));
        Assertion.assertThat("API data is null after deletion", apiDataDeleted.get("hasData"), equalTo("false"));

        log("Test completed — full CRUD flow with API verification passed");
    }
}