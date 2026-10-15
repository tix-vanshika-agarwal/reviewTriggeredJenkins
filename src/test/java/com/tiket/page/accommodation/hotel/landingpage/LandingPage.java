package com.tiket.page.accommodation.hotel.landingpage;

import static org.hamcrest.Matchers.is;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Deeplink;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
import com.tiket.page.common.child_age.ChildAgePickerPage;
import com.tiket.pojo.accommodation.homes.HomesData;
import com.tiket.pojo.accommodation.hotel.LandingPageModulePOJO;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class LandingPage <T extends WebDriver> extends BasePage<T> {

    /** Fallback child-age upper bound used when the test data sheet does not supply one. */
    public static final int DEFAULT_CHILD_AGE_BUG_UPPER_BOUND = 5;

    public LandingPage(Session<T> session) { super(session); }

    public static <T extends WebDriver> LandingPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new LandingPageAndroid<>(session); }
            case IOS -> { return new LandingPageIOS<>(session); }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void clickSeeMoreFloatingButton();
    public abstract String getModuleTitleSection();
    public abstract void clickSeeAllButton();
    public abstract void clickFirstSeeAllButton();
    public abstract void clickFirstHotelCard();
    public abstract void searchHotelList(List<String> hotelList);
    public abstract void selectRecentViewedTab();
    public abstract int deleteRecentViewItems(List<String> hotelList, int maxRecentlyViewed);
    public abstract boolean isRecentViewedLandingPageEmpty();
    public abstract void clearAllRecentViewed();
    public abstract int getRecentViewedItemCount(List<String> hotelList, int maxRecentlyViewed);
    public abstract void clickSearchField();
    public abstract boolean isRecentViewedSearchPopupEmpty();
    public abstract void searchStayLocation(String location);
    public abstract String[] setStayDate(String type, String date);
    public abstract String getSelectedCheckInOut();
    public abstract void clickDone();
    public abstract String getRoomAndGuestText();
    public abstract void clickSubmit();
    public abstract void clickRoomAndGuestButton();
    public abstract void setTotalRoom(int total);
    public abstract void setTotalGuest(int total);
    public abstract void setTotalChildren(int total);
    public abstract void removePopUp();
    public abstract void backToHomePage();
    public abstract void fillNightCheckInForm();
    public abstract String getAreaText(String location);
    public abstract boolean recentlySearchedStatus(String dateRoomGuestDetail, String searchText, String area);
    public abstract boolean isRecentlySearchAvailable(String location);
    public abstract boolean isRecentlySearchAvailable();
    public abstract String getRecentlySearchLocation();
    public abstract boolean isPageModulePresent();
    public abstract String getPageModuleTitle();
    public abstract String getPageModuleProductCard();
    public abstract String getRecentTotalGuest(String location, String[] checkInOutDate);
    public abstract void openCalendarPage();
    public abstract void goToTimedPromoSection();
    public abstract String getTimedPromoTitle();
    public abstract String selectTimedPromoLocation(int index);
    public abstract String clickSeeAllButtonTimedPromo();
    public abstract boolean compareTimedPromoHotelName(ArrayList<String> hotelName);
    public abstract String getMasterTitle();
    public abstract String getMasterSubTitle();
    public abstract boolean checkLocationTagsForMultipleHotelId(LandingPageModulePOJO landingPageModuleApi);
    public abstract String getButtonText();
    public abstract void seeAllMultipleID();
    public abstract void goToMultipleSquareCard();
    public abstract String getMultipleSquareCardTitle();
    public abstract String getMultipleSquareCardSubTitle();
    public abstract String[] getMultipleSquareCardTitleList();
    public abstract LandingPageModulePOJO.List chooseRandomMultipleSquareCard(LandingPageModulePOJO inputApi);
    public abstract boolean verifyRedirectSearchForm(LandingPageModulePOJO.Redirect redirectData);
    public abstract void enableLocation();
    public abstract void clickNearMeIcon();
    public abstract String getCurrentSearchLocation();
    public abstract void clickBackButton();
    public abstract String getFirstHotelName();
    public abstract String checkHotelPrice(boolean isCrossPrice, String hotelName, String price);
    public abstract boolean checkHotelPrice(String hotelName,String price);
    public abstract void selectModuleChip(String chipName);

    /**
     * Verifies that at least one hotel card on the SRP shows a "from {poi}" / "dari {poi}" location info,
     * confirming the search result is correctly bound to the selected POI.
     *
     * @param poi the POI name selected on the landing page (e.g. "National Monument")
     * @return true if at least one hotel card's location info contains the POI text in EN or ID
     */
    public abstract boolean verifyPoiOnHotelCards(String poi);

    public void setTotalChildren(int total, List<String> childAges) {
        setTotalChildren(total);
        logger.log("Setting child ages: " + childAges);
        ChildAgePickerPage<T> childAgePickerPage = ChildAgePickerPage.getInstance(session);
        for (int i = 0; i < childAges.size(); i++) {
            String age = childAges.get(i).trim();
            logger.log("Setting child age index: " + (i + 1) + " with age: " + age);
            int index = i + 1;
            childAgePickerPage.setChildAgeByIndex(index, age);
        }
    }

    /**
     * On the Rooms &amp; Guests bottom sheet, sets the children ages within the bug range — exactly
     * one child aged at {@code childAgeUpperBound} and every other child a random age below it — then
     * verifies the picker keeps the selected age when re-editing each child.
     *
     * @param totalChildren     number of children currently selected on the sheet
     * @param childAgeUpperBound inclusive upper bound of the bug range (driven from the test data sheet)
     */
    public void setChildAgesWithinBugRangeAndVerifyReEdit(int totalChildren, int childAgeUpperBound) {
        requireValidChildAgeUpperBound(childAgeUpperBound);
        withChildAgePickerIdleWaitDisabled(() -> {
            ChildAgePickerPage<T> childAgePickerPage = ChildAgePickerPage.getInstance(session);
            int childAtUpperBound = CoreFunctions.generateRandomInt(1, totalChildren);
            for (int index = 1; index <= totalChildren; index++) {
                int age = (index == childAtUpperBound)
                        ? childAgeUpperBound
                        : CoreFunctions.generateRandomInt(0, childAgeUpperBound - 1);
                logger.log("Setting child " + index + " age to " + age);
                childAgePickerPage.setChildAgeByIndex(index, String.valueOf(age));
            }
            verifyChildAgePickerDefaultAndReEdit(totalChildren, childAgeUpperBound);
        });
    }

    /**
     * Runs a child age picker interaction with the Android UiAutomator2 idle wait disabled. The
     * picker is a continuously "busy" wheel, so every command otherwise blocks on the default idle
     * wait (each scroll/read took 5-15s). The idle wait is always restored afterwards so the rest of
     * the suite is unaffected. Safe to nest — the inner scope restores the default and no picker work
     * happens between the inner and outer restore.
     */
    private void withChildAgePickerIdleWaitDisabled(Runnable pickerInteraction) {
        long previousIdleTimeout = driverTools.getWaitForIdleTimeout();
        driverTools.setWaitForIdleTimeout(0);
        try {
            pickerInteraction.run();
        } finally {
            driverTools.setWaitForIdleTimeout(previousIdleTimeout);
        }
    }

    /**
     * On the Rooms &amp; Guests bottom sheet, verifies that the child age picker always re-opens on
     * the previously selected age and never silently resets it. Reproduces the prod bug
     * "[Android] Child age resets to 16 when re-editing age &lt;= 5" for every selected child.
     *
     * For each of the {@code totalChildren}:
     * 1. Reads the age shown on the sheet, opens the picker, and asserts the picker's highlighted
     *    (default) age matches that selected age.
     * 2. Picks a new random age within the bug range (0..{@code childAgeUpperBound}), applies it, and
     *    asserts the label reflects the new age once the picker is submitted.
     * 3. Re-opens the picker and asserts it re-opens on the newly selected age (this is what the
     *    prod bug breaks by jumping to 16).
     *
     * @param totalChildren      number of children currently selected on the sheet
     * @param childAgeUpperBound inclusive upper bound of the bug range (driven from the test data sheet)
     */
    public void verifyChildAgePickerDefaultAndReEdit(int totalChildren, int childAgeUpperBound) {
        requireValidChildAgeUpperBound(childAgeUpperBound);
        withChildAgePickerIdleWaitDisabled(() -> {
            ChildAgePickerPage<T> childAgePickerPage = ChildAgePickerPage.getInstance(session);
            for (int index = 1; index <= totalChildren; index++) {
                int labelAge = childAgePickerPage.getSelectedChildAgeFromLabel(index);
                childAgePickerPage.openChildAgePicker(index);
                int pickerDefaultAge = childAgePickerPage.getPickerSelectedAge();
                Assertion.assertThat(
                        "child " + index + " age picker should open on the previously selected age of " + labelAge
                                + " years, but opened on " + pickerDefaultAge + " years",
                        pickerDefaultAge, is(labelAge));

                int newAge = generateNewChildAge(pickerDefaultAge, childAgeUpperBound);
                childAgePickerPage.setPickerAgeTo(newAge);
                childAgePickerPage.submitChildAgePicker();

                int labelAgeAfterEdit = childAgePickerPage.getSelectedChildAgeFromLabel(index);
                Assertion.assertThat(
                        "child " + index + " label should show the newly selected age of " + newAge
                                + " years after editing, but shows " + labelAgeAfterEdit + " years",
                        labelAgeAfterEdit, is(newAge));

                childAgePickerPage.openChildAgePicker(index);
                int pickerReopenAge = childAgePickerPage.getPickerSelectedAge();
                Assertion.assertThat(
                        "child " + index + " age picker should re-open on the last selected age of " + newAge
                                + " years, but opened on " + pickerReopenAge
                                + " years (prod bug: age <= 5 resets to 16 on re-edit)",
                        pickerReopenAge, is(newAge));
                childAgePickerPage.submitChildAgePicker();
            }
        });
    }

    /**
     * The re-edit check must be able to pick a NEW age different from the current one, so the range
     * {@code [0, childAgeUpperBound]} needs at least two values. A bound below 1 (only {@code 0} is
     * possible) makes {@link #generateNewChildAge(int, int)} loop forever when the current age is 0,
     * so reject it early with a clear message instead of hanging the test.
     */
    private void requireValidChildAgeUpperBound(int childAgeUpperBound) {
        if (childAgeUpperBound < 1) {
            throw new DriverToolsException("childAgeBugUpperBound must be >= 1 so a different re-edit age exists, but got "
                    + childAgeUpperBound);
        }
    }

    private int generateNewChildAge(int currentAge, int childAgeUpperBound) {
        int newAge;
        do {
            newAge = CoreFunctions.generateRandomInt(0, childAgeUpperBound);
        } while (newAge == currentAge);
        return newAge;
    }

    public boolean verifyRedirectTarget(String redirectTarget, LandingPageModulePOJO.Redirect redirectData, String language , String vertical) {
        switch (redirectTarget) {
            case "SRP" -> {
                fillNightCheckInForm();
                return SearchResultPage.getInstance(session).verifyRedirectSRP(redirectData, language, vertical);
            }
            case "SEARCH_FORM" -> {
                return verifyRedirectSearchForm(redirectData);
            }
            case "URL" -> {
                return SearchResultPage.getInstance(session).verifyRedirectUrl(redirectData);
            }
            default -> {logger.warn("There is no method for '"+redirectTarget+"' redirectTarget");}
        }

        return false;
    }

    public boolean verifyRedirectTargetWithPojo(HomesData.ContentData list, String language , String vertical) {
        switch (list.redirectTarget) {
            case "SRP" -> {
                fillNightCheckInForm();
                return SearchResultPage.getInstance(session).verifyRedirectSRPWithPojo(list.redirectLocation, language, vertical);
            }
            case "SEARCH_FORM" -> {
                return SearchResultPage.getInstance(session).verifySearchFormWithPojo(list);
            }
            case "URL" -> {
                return SearchResultPage.getInstance(session).verifyURLWithPojo(list);
            }
            default -> {
                throw new NotImplementedException("There is no method for '"+ list.redirectTarget +"' redirectTarget");
            }
        }
    }

    public String setCurrentDate(String currentDate) {
        int dates = Integer.parseInt(currentDate.split("-")[2]);
        int endDate = dates+1;
        return ""+dates+"-"+endDate+"";
    }

    public boolean compareMultipleSquareCard(String[] apiData, String[] appData) {
        logger.log("apiData.length : " + apiData.length);
        logger.log("appData.length : " + appData.length);
        if (apiData.length != appData.length) {
            return false;
        }

        logger.log("apiData : " + Arrays.toString(apiData));
        logger.log("appData : " + Arrays.toString(appData));
        List<String> appDataList = Arrays.asList(appData);
        for(String s : apiData) {
            if (!appDataList.contains(s)) {
                return false;
            }
        }

        return true;
    }

    public String getExpectedCheckInOut(String[] checkInOut){
        String[] checkIn = checkInOut[0].split(" ");
        int checkInDate = Integer.parseInt(checkIn[0]);
        String checkInMonth = checkIn[1];
        String expectedCheckInDate = checkInDate + " " + checkInMonth;

        String[] checkOut = checkInOut[1].split(" ");
        int checkOutDate = Integer.parseInt(checkOut[0]);
        String checkOutMonth = checkOut[1];
        String expectedcheckOutDate = checkOutDate + " " + checkOutMonth;

        return expectedCheckInDate + " - " + expectedcheckOutDate;
    }

    public abstract Boolean isLandingPageValid();

    public void changeDateViaDeeplink(String dateRange,String dateType,int room,int adult){
        String methodName = "changeDateViaDeeplink";
        long startTime = CoreFunctions.startTiming(methodName);
        try {
            SearchResultPage<T> searchResultPage = SearchResultPage.getInstance(session);
            if(dateType.equals("range date")){
                int startDate = Integer.parseInt(dateRange.split("-")[0].trim());
                int endDate = Integer.parseInt(dateRange.split("-")[1].trim());

                int startRandomDate = CoreFunctions.generateRandomInt(startDate, endDate - 1);
                int endRandomDate = startRandomDate + 1;
                dateRange = startRandomDate + "-" + endRandomDate;
                logger.log("Generated random date range: " + dateRange);
            }
            deeplinkWithSelectedDate(room,adult,"jakarta-108001534490276204","REGION","Jakarta",dateRange);
            searchResultPage.checkPopupAndClickBackBtn();
        } finally {
            CoreFunctions.endTiming(methodName, startTime);
        }
    }

    public void deeplinkWithSelectedDate(int room, int adult,String locationId, String type,String region,String dateRange) {
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String[] dateParts = dateRange.split("-");
        int daysToAddStart = Integer.parseInt(dateParts[0].trim());
        int daysToAddEnd = Integer.parseInt(dateParts[1].trim());
        String checkInDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToAddStart);
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToAddEnd);
        region = region.replace(" ","+");

        String url = "hotel/search?room="+room+"&adult="+adult+"&id="+locationId+"&type="+type+"&q="+region+"&checkin="+checkInDate+"&checkout="+checkOutDate;
        String deeplinkGK = "tiket://m.gatotkaca.tiket.com/" + url;
        String deepLinkPreProd = "tiket://m.preprod.tiket.com/" + url;
        String deeplinkProd = "tiket://m.tiket.com/" + url;
        Deeplink verticalPage = new Deeplink("VerticalPage", deeplinkGK, deepLinkPreProd, deeplinkProd);
        try {
            driverTools.deeplink(verticalPage);
        } catch (Exception e) {
            logger.log(e.getLocalizedMessage());
        }
    }
}
