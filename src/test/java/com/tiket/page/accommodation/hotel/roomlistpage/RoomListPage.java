package com.tiket.page.accommodation.hotel.roomlistpage;

import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.reschedulepage.HotelReschedulePage;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
import com.tiket.pojo.accommodation.hotel.ChooseFirstRoomData;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RoomListPage <T extends WebDriver> extends BasePage<T> {

    public RoomListPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> RoomListPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new RoomListPageAndroid<>(session); }
            case IOS -> { return new RoomListPageIOS<>(session); }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void clickChangeButton();
    public abstract void clickBookButtonFooter();
    public abstract double sumPriceBreakdown();
    public abstract boolean priceBreakdownIsDisplayed();
    public abstract void clickPriceFooter();
    public abstract String getPriceFooter();
    public abstract double selectRoom(int totalGuest, int roomIndex);
    public static HashMap<Integer, String> selectedRoomName = new HashMap<>();

    /**
     * Selects a given number of rooms and distributes total guests across them.
     * Use when test data specifies "total rooms" and "total guests" (e.g. 3 rooms, 3 adults).
     * Example: 3 rooms, 3 guests → 1 guest per room; 3 rooms, 4 guests → 2, 1, 1.
     *
     * @param totalRooms  number of rooms to select
     * @param totalGuests total number of guests to distribute across the rooms
     * @return sum of (price × guests) for each selected room
     */
    public double selectRoomsByCount(int totalRooms, int totalGuests) {
        return selectRoomsByCount(totalRooms, totalGuests, null);
    }

    /**
     * Selects rooms with a given distribution: first room type N times, then next type M times, etc.
     * Example: totalRooms=3, totalGuests=3, distribution=[2,1] → select first room type 2 times, second room type 1 time (2+1=3 rooms).
     *
     * @param totalRooms   number of rooms to select (must equal sum of distribution)
     * @param totalGuests  total number of guests to distribute
     * @param distribution e.g. [2, 1] = 2 rooms of first type, 1 room of second type; null = any order
     * @return sum of (price × guests) for each selected room
     */
    public double selectRoomsByCount(int totalRooms, int totalGuests, int[] distribution) {
        if (totalRooms < 1) {
            throw new IllegalArgumentException("totalRooms (" + totalRooms + ") must be >= 1");
        }
        if (totalGuests < totalRooms) {
            throw new IllegalArgumentException("totalGuests (" + totalGuests + ") must be >= totalRooms (" + totalRooms + ")");
        }
        if (distribution != null) {
            int sum = 0;
            for (int d : distribution) sum += d;
            if (sum != totalRooms) {
                throw new IllegalArgumentException("distribution sum (" + sum + ") must equal totalRooms (" + totalRooms + ")");
            }
        }
        int baseGuests = totalGuests / totalRooms;
        int remainder = totalGuests % totalRooms;
        double totalPrice = 0;
        int distributionIndex = 0;
        int roomsSelectedForCurrentType = 0;
        for (int i = 1; i <= totalRooms; i++) {
            int guestsThisRoom = (i <= remainder) ? (baseGuests + 1) : baseGuests;
            String preferRoomName = null;
            if (distribution != null && distributionIndex < distribution.length) {
                if (i > 1 && roomsSelectedForCurrentType > 0 && roomsSelectedForCurrentType < distribution[distributionIndex]) {
                    preferRoomName = selectedRoomName.get(i - 1);
                }
            }
            double subTotalPrice = selectRoomWithPrefer(guestsThisRoom, i, preferRoomName);
            totalPrice += subTotalPrice;
            if (distribution != null && distributionIndex < distribution.length) {
                roomsSelectedForCurrentType++;
                if (roomsSelectedForCurrentType >= distribution[distributionIndex]) {
                    distributionIndex++;
                    roomsSelectedForCurrentType = 0;
                }
            }
        }
        logger.log("selectedRoomName -> " + selectedRoomName);
        logger.log("totalPrice -> " + totalPrice);
        return totalPrice;
    }

    /**
     * Selects one room; when preferRoomName is non-null, tries to select an unselected room of that type first (e.g. to select same room type twice).
     * Default: preferRoomName == null behaves like {@link #selectRoom(int, int)}.
     */
    public abstract double selectRoomWithPrefer(int totalGuest, int roomIndex, String preferRoomName);

    /**
     * Fills a given number of guests into rooms with up to guestPerRoom per room.
     * First parameter is total GUESTS to accommodate, not number of rooms.
     * For "N rooms, M guests" use {@link #selectRoomsByCount(int, int)} instead.
     */
    public double selectMultiRoom(int totalRoom, int guestPerRoom) {
        int occupiedRoom = 0;
        double totalPrice = 0;
        for (int i = 1; i <= totalRoom; i++) {
            int remainGuest = totalRoom - occupiedRoom;
            if (remainGuest < guestPerRoom) {
                guestPerRoom = remainGuest;
                totalRoom = occupiedRoom;
            }
            double subTotalPrice = selectRoom(guestPerRoom, i);
            totalPrice += subTotalPrice;
            occupiedRoom += guestPerRoom;
        }
        logger.log("selectedRoomName -> " + selectedRoomName);
        logger.log("totalPrice -> " + totalPrice);
        return totalPrice;
    }

    /**
     * Books rooms of a single room type in one flow.
     * When isSameRoom is true, taps the footer button whose label equals "Book {totalRoom} rooms"
     * (or "Pesan {totalRoom} kamar"), which books totalRoom rooms of the same type and redirects
     * straight to the booking form page.
     * When isSameRoom is false, selects totalRoom different rooms one at a time via
     * {@link #selectMultiRoom(int, int)}, using 1 guest per room by default.
     * Applicable for both Android and iOS.
     *
     * @param isSameRoom true to book totalRoom rooms of the same type in one tap; false to select rooms individually
     * @param totalRoom  number of rooms to book
     */
    public void selectMultiRoom(boolean isSameRoom, int totalRoom) {
        if (isSameRoom) {
            clickMultiRoomBookButton(totalRoom);
        } else {
            selectMultiRoom(totalRoom, 1);
        }
    }

    /**
     * Clicks the footer book button whose label equals "Book {totalRoom} rooms" (or "Pesan {totalRoom} kamar"),
     * shown once totalRoom rooms of the same room type are queued for booking. Redirects to the booking form page.
     *
     * @param totalRoom number of rooms shown on the button label
     */
    public abstract void clickMultiRoomBookButton(int totalRoom);

    public abstract boolean moBannerIsDisplayed();
    public abstract String getTheFirstHotelRoomName();
    public abstract String getTheFirstHotelRoomPrice();
    public abstract String getTheFirstHotelRoomPrice(String currency);
    public abstract String getTheFirstHotelRoomPrice(String vendor, boolean childMealPlan,String currency);
    /**
     * @deprecated This method is deprecated because the reschedule flow has changed to a webview.
     * Use {@link HotelReschedulePage#getTheFirstRoomRefundEstimation()} instead.
     */
    @Deprecated public abstract String getTheFirstRoomRefundEstimation();
    public abstract void chooseRoomWithCashback();
    public abstract void openRoomDetailPage();
    public abstract void openRoomDetailPage(String price);
    public abstract void clickBackButton();
    /**
     * @deprecated This method is deprecated because the reschedule flow has changed to a webview.
     * Use {@link HotelReschedulePage#chooseRoomWithAdditionalPrice()} instead.
     */
    @Deprecated public abstract String chooseRoomWithAdditionalPrice();
    public abstract String getPriceRoomWithAdditionalPrice();
    public abstract ChooseFirstRoomData chooseFirstRoom();
    public abstract String getPriceAfterTax();
    public abstract void clickBookRoom();
    public abstract void clickExtraRoomBottomSheetIfAppears();
    /** @return true if the fully booked bottomsheet was present and clicked */
    public abstract boolean clickFullyBookedBottomSheetIfAppears();

    /**
     * If room is fully booked after book: dismiss bottomsheet, back to SRP via room list + PDP,
     * select the 2nd hotel card, open room list, and book again.
     * @return true if recovery ran; false if bottomsheet was not shown
     */
    public boolean reselectAnotherHotelIfFullyBooked() {
        if (!clickFullyBookedBottomSheetIfAppears()) {
            return false;
        }
        clickBackButton();
        DetailPage.getInstance(session).clickBackButton();
        SearchResultPage.getInstance(session).selectHotelList("2");
        DetailPage.getInstance(session).seeHotelRoom();
        clickBookRoom();
        return true;
    }

    public abstract void clickBookRoomWithoutSkippingRatePlan();
    public abstract boolean checkChildGuest();
    public abstract boolean checkChildMeal();
    public abstract void openRoomWithRefundReschedulePolicy();
    public abstract void openRoomWithRefundRescheduleChargePolicy();
    public abstract void bookRoomWithRefundRescheduleAndFlexiblePayment();
    public abstract String chooseRoomWithRefundReschedule();
    public abstract void clickMastertag(String masterTagTitle);
    public abstract Boolean checkLoyaltyTierBenefit();
    public abstract Boolean verifyLoyaltyBenefitPopUp(String tierLevel);
    public abstract void chooseRoomWithFreePaymentNoCashback();
    public abstract String chooseRoomWithFreePayment();
    public abstract boolean verifyAlternateRoomShowingCorrectly(int guest);
    public abstract boolean verifyExactAlternateRoom(String expectedTextLabel, int totalLoop);
    public abstract List<String> getPrices();
    public abstract void openFirstRoomDetail(String vendor, boolean childMealPlan);
    public abstract String getRoomNudges(String nudgesText);
    public abstract void clickNudgesRoom(String nudgesText);
    public abstract boolean assertThatPriceCurrency(String currency);
    public abstract boolean isNudgesPresent(String nudgesText);
    public abstract String getRecommendedRoomAmount();
    public abstract String getRecommendationAdditionalRoom();
    public abstract void clickRecommendedRoom();
    public abstract boolean checkRoomRecommendation(int totalGuest,int roomCapacity,String searchCriteria);
    public abstract void openRoomDetailForRecommendedRoom();
    public abstract int getRecommendedRoomCapacity();
    public abstract boolean checkHotelPrice(boolean isCrossPrice, String price);
    public abstract boolean checkHotelPrice(String price);
    public abstract boolean isLabelTotalPrice(boolean isTax);
    public abstract boolean isLabelTotalPrice();
    public abstract boolean isLabelPerRoom(boolean isTax);
    public abstract boolean isLabelPerRoom();
    public abstract boolean doesNotMeetTravelPolicyPopUpAppears();
    public abstract boolean meetTravelPolicyDisplayed();
    public abstract boolean isLabelPerRoomPerNight();
    public abstract void selectRoomByPriceMore(int price);
    /** Whether user is currently on the room list page (e.g. first-room card / room list container visible). */
    public abstract boolean isRoomListShown();

    /**
     * Whether the first room card on the room list shows any meal / breakfast indicator
     * (e.g. "Breakfast (1 pax)", "No breakfast", "Sarapan termasuk", "Tanpa sarapan"),
     * regardless of whether breakfast is included or excluded.
     */
    public abstract boolean isMealInfoOnFirstRoomCardVisible();

    /**
     * Whether the first room card on the room list shows refund / reschedule policy info,
     * regardless of whether it's allowed, charged, or not allowed.
     */
    public abstract boolean isRefundRescheduleInfoOnFirstRoomCardVisible();

    /**
     * Whether the first room card on the room list shows max-people occupancy info
     * (e.g. "1 Adult", "2 Tamu", "1 Anak").
     */
    public abstract boolean isMaxOccupancyInfoOnFirstRoomCardVisible();

    /**
     * Whether the given exclusive-nudge text is rendered specifically inside the
     * Room Recommendation card (and not anywhere else on the page).
     *
     * @param nudgesText exact nudge text to look for inside the recommendation card
     */
    public abstract boolean isExclusiveNudgePresentOnRoomRecommendationCard(String nudgesText);

    /**
     * Whether the Room Recommendation card shows meal/food info (e.g. breakfast included / not included).
     */
    public abstract boolean isMealInfoOnRoomRecommendationCardVisible();

    /**
     * Whether the Room Recommendation card shows refund &amp; reschedule info (allowed or not allowed).
     */
    public abstract boolean isRefundRescheduleInfoOnRoomRecommendationCardVisible();

    /**
     * Whether the Room Recommendation card shows max-people occupancy info (e.g. "1 Adult", "2 Tamu").
     */
    public abstract boolean isMaxOccupancyInfoOnRoomRecommendationCardVisible();

    /**
     * Whether the Room Recommendation card container is visible on the room list.
     */
    public abstract boolean isRoomRecommendationCardOnTop();

    /**
     * Whether the "Best pick for you!" / "Pilihan terbaik untukmu!" exclusive-nudge header
     * is visible on the room list (typically appears on top of the room recommendation card
     * after the user taps "See rooms" on PDP).
     */
    public abstract boolean isBestPickForYouHeaderVisible();

    /** Visible text of the Best pick header on the room list (empty if not found). */
    public abstract String getBestPickForYouHeaderText();

    // Whether the announcement banner is visible above the Best pick header.
    public abstract boolean isAnnouncementBannerVisible();

    public abstract void openRoomDetailFromRoomList();

    public abstract void openPriceSectionOnRoomDetail();

    public abstract String getRoomPriceFromPriceSection();

    public void goToRoomDetailPriceSection() {
        openRoomDetailFromRoomList();
        openPriceSectionOnRoomDetail();
    }
}