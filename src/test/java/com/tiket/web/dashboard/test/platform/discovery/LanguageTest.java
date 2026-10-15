package com.tiket.web.dashboard.test.platform.discovery;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.discovery.LanguagePage;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.emptyOrNullString;

public class LanguageTest extends BaseTest {

    @Author(names = {"Shailja"})
    @Module(name = "Language")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testEditLanguage() throws InterruptedException, ExecutionException {
        Map<String, String> map = getParam("testEditLanguage");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LanguagePage<?> languagePage = new LanguagePage<>(session);

        final String username = map.get("username");
        final String password = map.get("password");
        final String searchLanguage = map.get("searchLanguage");
        final String languageId = map.get("languageId");
        final String updatedSuffix = map.get("updatedSuffix");

        step("Step 1: Admin navigates to Language page");
        languagePage.goToLanguagePage(username, password);
        Assertion.assertThat("Language page loaded", languagePage.isLanguagePageLoaded(username), is(true));

        step("Step 2: Search language and edit English name");
        Map<String, String> beforeData = languagePage.searchAndExtract(searchLanguage);
        Assertion.assertThat("Language found", beforeData.get("enName"), not(emptyOrNullString()));

        final String originalEnName = beforeData.get("enName");
        final String originalNativeName = beforeData.get("nativeName");
        final String originalIso1 = beforeData.get("iso1");
        final String originalIso2 = beforeData.get("iso2");
        final String originalPrimaryCode = beforeData.get("primaryCode");
        final String editedEnName = originalEnName + " " + updatedSuffix;

        languagePage.clickEditAndUpdateEnName(editedEnName);

        step("Step 3: Verify success snackbar appears");
        Assertion.assertThat("Snackbar visible after editing", languagePage.isSnackbarVisible(), is(true));

        step("Step 4: Verify edited language in table");
        languagePage.navigateBackToLanguagePage();
        Map<String, String> afterData = languagePage.searchAndExtract(editedEnName);

        Assertion.assertThat("English Name updated", afterData.get("enName"), is(editedEnName));
        Assertion.assertThat("Native Name unchanged", afterData.get("nativeName"), is(originalNativeName));
        Assertion.assertThat("ISO 639-1 unchanged", afterData.get("iso1"), is(originalIso1));
        Assertion.assertThat("ISO 639-2 unchanged", afterData.get("iso2"), is(originalIso2));
        Assertion.assertThat("Primary Code unchanged", afterData.get("primaryCode"), is(originalPrimaryCode));

        step("Step 5: API verify after edit");
        Map<String, String> apiData = languagePage.getLanguageDataFromAPI(languageId);
        Assertion.assertThat("API returns SUCCESS", apiData.get("code"), equalTo("SUCCESS"));
        Assertion.assertThat("API englishName matches", apiData.get("englishName"), is(editedEnName));
        Assertion.assertThat("API nativeName unchanged", apiData.get("nativeName"), is(originalNativeName));
        Assertion.assertThat("API iso6391Code unchanged", apiData.get("iso6391Code"), is(originalIso1));
        Assertion.assertThat("API iso6392Code unchanged", apiData.get("iso6392Code"), is(originalIso2));

        step("Step 6: Revert language to original");
        languagePage.clickEditAndUpdateEnName(originalEnName);
        Assertion.assertThat("Snackbar visible after revert", languagePage.isSnackbarVisible(), is(true));

        step("Step 7: Verify reverted language in table and API");
        languagePage.navigateBackToLanguagePage();
        Map<String, String> revertData = languagePage.searchAndExtract(originalEnName);
        Assertion.assertThat("English Name reverted", revertData.get("enName"), is(originalEnName));
        Assertion.assertThat("Native Name still unchanged", revertData.get("nativeName"), is(originalNativeName));

        Map<String, String> apiRevert = languagePage.getLanguageDataFromAPI(languageId);
        Assertion.assertThat("API englishName reverted", apiRevert.get("englishName"), is(originalEnName));
        Assertion.assertThat("API nativeName unchanged", apiRevert.get("nativeName"), is(originalNativeName));

        log("testEditLanguage completed — edit verified via UI + API, reverted to original.");
    }
}