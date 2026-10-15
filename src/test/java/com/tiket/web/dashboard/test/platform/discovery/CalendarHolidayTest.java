package com.tiket.web.dashboard.test.platform.discovery;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.discovery.CalendarHolidayPage;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CalendarHolidayTest extends BaseTest {
    @Author(names = {"Yanuar", "Fandy"})
    @Module(name = "Calendar")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testAddCalendarHoliday() throws ExecutionException, InterruptedException {
        Map<String, String> map = getParam("testAddCalendarHoliday");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        CalendarHolidayPage<?> calendarHolidayPage = new CalendarHolidayPage<>(session);
        final String username = map.get("username");
        final String password = map.get("password");
        final String prefix = map.get("prefix");
        final String expectedMsg = map.get("successMsg");
        final String expectedCode = map.get("codeApi");

        step("Given admin go to Calendar Holiday Dashboard Page");
        calendarHolidayPage.goToCalendarPage(username, password);
        Assertion.assertThat("Verify calendar holiday page", calendarHolidayPage.isCalendarHolidayPageLoaded(username), is(true));

        step("When admin add new holiday");
        calendarHolidayPage.addNewHoliday(prefix);
        Map<String, Boolean> verifySuccess = calendarHolidayPage.verifyActionSuccessSnackbar(expectedMsg);
        Assertion.assertThat("Success Snackbar is visible", verifySuccess.get("snackbar"), is(true));
        Assertion.assertThat("Verify snackbar text", verifySuccess.get("text"), is(true));

        step("Then created holiday should be visible in the holiday list");
        String expectedDate = calendarHolidayPage.getDate();
        String expectedDescEn = calendarHolidayPage.getDescEn();
        String expectedDescId = calendarHolidayPage.getDescId();

        List<String> dataFromDashboard = calendarHolidayPage.searchCreatedHoliday(expectedDescId);
        String actualDate = dataFromDashboard.get(1);
        String actualDescEn = dataFromDashboard.get(2);
        String actualDescId = dataFromDashboard.get(3);

        Assertion.assertThat("Verify date", calendarHolidayPage.verifyText(expectedDate, actualDate), is(true));
        Assertion.assertThat("Verify Description (EN)", calendarHolidayPage.verifyText(expectedDescEn, actualDescEn), is(true));
        Assertion.assertThat("Verify Description (ID)", calendarHolidayPage.verifyText(expectedDescId, actualDescId), is(true));

        step("And the new added holiday should be displayed on vertical product page - via API");
        Map<String, String> dataToValidate = calendarHolidayPage.getDataToValidateFromAPI();
        String responseApi = dataToValidate.get("response");
        String codeApi = dataToValidate.get("code");
        String actualDateApi = dataToValidate.get("date");
        String isDescENPresentInApi = dataToValidate.get("isDescENPresent");
        String isDescIDPresentInApi = dataToValidate.get("isDescIDPresent");
        String holidayId = dataToValidate.get("holidayId");
        log("holidayId = " + holidayId);

        Assertion.assertThat("Verify response from API is not null", responseApi, is(notNullValue()));
        Assertion.assertThat("Verify code from API", codeApi, is(expectedCode));
        Assertion.assertThat("Verify date from API", actualDateApi, is(expectedDate));
        Assertion.assertThat("Verify Description EN is present from API", Boolean.valueOf(isDescENPresentInApi), is(true));
        Assertion.assertThat("Verify Description ID is present from API", Boolean.valueOf(isDescIDPresentInApi), is(true));
    }

    @Author(names = {"Yanuar", "Fandy"})
    @Module(name = "Calendar")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test(dataProvider = "testCalendarHolidayListDashboardDataProvider")
    public void testCalendarHolidayListDashboard(Map<String, String> map) throws InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        CalendarHolidayPage<?> calendarHolidayPage = new CalendarHolidayPage<>(session);

        final String username = map.get("username");
        final String password = map.get("password");
        final String searchTitle = map.get("searchTitle");

        step("Given user access Calendar Holiday Dashboard Page");
        calendarHolidayPage.goToCalendarPage(username, password);

        step("When user is shown with holiday list");
        Assertion.assertThat("Verify calendar holiday page is loaded", calendarHolidayPage.isCalendarHolidayPageLoaded(username), is(true));

        step("And already Verify the 5 parameters of List are shown");
        List<String> expectedHeaders = Arrays.asList(map.get("title1"), map.get("title2"), map.get("title3"), map.get("title4"), map.get("title5"));
        Assertion.assertThat("Verify all calendar holiday table headers are present", calendarHolidayPage.verifyTableHeaders(expectedHeaders), is(true));

        step("Then user is able to do action: SCROLL HOLIDAY LIST");
        calendarHolidayPage.scrollInHolidayPage();

        step("And user is able to do action: SEARCH HOLIDAY");
        calendarHolidayPage.searchCalendar(searchTitle);
        Map<String, Object> result = calendarHolidayPage.verifySearchCalendarResult();
        List<String> resultId = (List<String>) result.get("id");
        List<String> resultEn = (List<String>) result.get("en");

        for (int i = 0; i < resultId.size(); i++) {
            String titleId = resultId.get(i);
            String titleEn = resultEn.get(i);

            if (resultId.get(i).contains(searchTitle)) {
                Assertion.assertThat(String.format("Verify that result: %s in Description (ID) is containing %s", titleId, searchTitle),
                        titleId, is(CoreMatchers.containsStringIgnoringCase(searchTitle)));
            } else if (resultEn.get(i).contains(searchTitle)) {
                Assertion.assertThat(String.format("Verify that result: %s in Description (EN) is containing %s", titleEn, searchTitle),
                        titleEn, is(CoreMatchers.containsStringIgnoringCase(searchTitle)));
            } else {
                Assertion.fail(String.format("Neither %s nor %s contain %s", titleId, titleEn, searchTitle));
            }
        }

        step("And user is able to do action: UPDATE HOLIDAY");
        calendarHolidayPage.updateCalendar();

        List<String> resultSearch = calendarHolidayPage.verifySuccessUpdatedHolidayData();
        String expectedDate = calendarHolidayPage.getDate();
        String expectedDescEn = calendarHolidayPage.getDescEn();
        String expectedDescId = calendarHolidayPage.getDescId();

        String actualDate = resultSearch.get(1);
        String actualDescEn = resultSearch.get(2);
        String actualDescId = resultSearch.get(3);

        Assertion.assertThat("Verify that the date is changed to as updated", actualDate, is(expectedDate));
        Assertion.assertThat("Verify that the Description (EN) is changed to as updated", actualDescEn, is(expectedDescEn));
        Assertion.assertThat("Verify that the Description (ID) is changed to as updated", actualDescId, is(expectedDescId));

        step("And user is able to do action: DELETE HOLIDAY");
        final String deleteMsg = map.get("deleteMsg");

        calendarHolidayPage.deleteCalendar();

        Map<String, Boolean> verifySuccess = calendarHolidayPage.verifyActionSuccessSnackbar(deleteMsg);
        Assertion.assertThat("Success Snackbar is visible", verifySuccess.get("snackbar"), is(true));

        List<String> resultDelete = calendarHolidayPage.verifySuccessDelete();
        Assertion.assertThat("Verify that search result is empty", resultDelete.isEmpty(), is(true));
    }

    @DataProvider(name = "testCalendarHolidayListDashboardDataProvider")
    public Object[][] testCalendarHolidayListDashboardDataProvider() {
        return getObjectParams("testCalendarHolidayListDashboard");
    }
}