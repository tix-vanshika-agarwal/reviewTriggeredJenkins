package com.tiket.page.accommodation.hotel.detailpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.List;
import static org.hamcrest.core.Is.is;

public abstract class DetailPage <T extends WebDriver> extends BasePage<T> {

    public DetailPage(Session<T> session) { super(session); }

    public static <T extends WebDriver> DetailPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new DetailPageAndroid<>(session); }
            case IOS -> { return new DetailPageIOS<>(session); }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract String checkPolicyAccommodation();
    public abstract void openAccommodationRules();
    public abstract boolean isAccommodationRulesOpen();
    public abstract void closePdpDrawer();
    public abstract boolean isHotelDetailShown();
    public abstract boolean isHaveHotelImage();
    public abstract String getHotelNameDetail();
    public abstract String getHotelName();
    public abstract boolean getHotelStar();
    public abstract String getHotelReview();
    public abstract boolean checkLoyaltyTierBenefit();

    /**
     * Whether a {@code Preferred Partner} / {@code Preferred Partner Plus} indicator
     * is rendered on the hotel PDP (label / badge / list item). Locale-agnostic and
     * tier-agnostic: returns {@code true} when either variant is present.
     */
    public abstract boolean isPreferredPartnerVisibleOnPdp();
    public abstract String getHotelPrice();
    public abstract String getHotelLocationDetail();
    public abstract void seeHotelRoom();
    public abstract boolean isRoomAvailable();
    public abstract void RemovePopUp();
    public abstract boolean isRoomDetailShown();
    public abstract void clickBookButton(int totalRoom);
    public abstract void clickBookButton100pRefund(int totalRoom);
    public abstract void clickBackButton();
    public abstract String getRefundPolicyValue();
    public abstract void openPriceDetail();
    public abstract void closePriceDetail();
    public abstract boolean priceMatch(Double price, int totalRoom);
    public abstract boolean checkPriceBreakdown(Double price, int totalRoom);
    public abstract void clickLocationTab();
    public abstract void clickSeeMapsButton();
    public abstract boolean isNativeMapsShown();
    public abstract String getHotelNamePoi();
    public abstract boolean verifyRedirectToMaps();
    public abstract void openReviewSection();
    public abstract boolean checkHotelReview();
    public abstract boolean checkReviewPhotoCount();
    public abstract boolean verifyLoyaltyBenefitPopUp(String tierLevel);
    public abstract boolean checkHotelName(String name);
    public abstract void selectRoomMultiOrder(int roomcount);
    public abstract List<String> getCancellationCharges();
    public abstract List<String> getPriceDetailPrices();
    public abstract void seeAllReview();
    public abstract boolean isReviewSectionShown();
    public abstract boolean isFacilitiesSectionShown();
    public abstract boolean isLocationSectionShown();
    public abstract boolean isAboutAccommodationSectionShown();
    public abstract boolean clickImageAndVerifyGallery();
    public abstract boolean checkReviewWidgetSwipe();
    public abstract boolean checkReviewImageBottomSheetSwipe();
    public abstract void clickAllReviewsBtnOnReviewBottomSheet();
    public abstract boolean isBottomSheetReviewShown();
    public abstract boolean isLabelTotalPrice();
    public abstract boolean isLabelPerRoomPerNight();
    public abstract void clickHeartIcon();
    public abstract void clickManageWishlist();
    public abstract String isWishlistDialogShown();
    public abstract boolean isWishlistActive();
    public abstract void clickLoginFromPDP();
    public abstract boolean isLoginButtonDisplayed();
    public abstract boolean isMeetTravelPolicy();
    public abstract void clickYesContinue();
    public abstract void TurnOffHeartIcon();
    public abstract String getTotalCashback();


    private static final Locator REVIEW_PAGE_BACK_BTN = new Locator("REVIEW_PAGE_BACK_BTN", AppiumBy.xpath("//*[contains(@class,'ModifiedHeaderMobile_left_slot')]"));
    private static final Locator REVIEW_PAGE_TITLE = new Locator("REVIEW_PAGE_TITLE", AppiumBy.xpath("//*[contains(@class,'ModifiedHeaderMobile_content_slot')]//span[contains(text(),'Review')]"));
    private static final Locator REVIEW_PAGE_FILTER = new Locator("REVIEW_PAGE_FILTER", AppiumBy.xpath("//*[contains(@class,'FilterNavigationBottom_config_icon')]"));

    public boolean verifyRedirectedToReviewPage(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("review");
        logger.log("Verify redirected to review page");
        boolean isReviewFilter = driverTools.waitForElementToPresence(REVIEW_PAGE_FILTER).isPresent();
        Assertion.assertThat("verify review page filter ", isReviewFilter,is(true));
        driverTools.switchToNative();
        return isReviewFilter;
    }

    public void clickBackButtonReviewPage(){
        driverTools.switchToWebViewUrl("review");
        logger.log("Click back button on review page");
        driverTools.click(REVIEW_PAGE_BACK_BTN);
        driverTools.switchToNative();
    }

    public void clickBack(){
        //remove bottom sheet review;
        if(isBottomSheetReviewShown()){
            driverTools.back();
        }
    }

    // ---- Infinity promo cards on PDP ----
    public abstract boolean isInfinityPromoSectionDisplayed();
    /**
     * Counts distinct infinity-promo codes on the PDP carousel by swiping horizontally: each time the visible
     * promo code changes (vs the previous card), the count increases until the carousel wraps to the first code again.
     */
    public abstract int getInfinityPromoCardCount();
    /** Click copy on the first promo card on the PDP carousel. Returns the promo code text shown on the card. */
    public abstract String copyFirstInfinityPromoCode();
    /** Whether the "See all" promo button is visible (typically when many distinct promo codes exist on the carousel). */
    public abstract boolean isSeeAllPromoButtonDisplayed();
    /** Click the "See all" / "Lihat semua" promo control on PDP to open the full promo list. */
    public abstract void clickSeeAllPromoButton();
    /** Whether the "See all promo" bottom sheet/section is shown after clicking "See all". */
    public abstract boolean isSeeAllPromoSectionShown();
    /** Click copy on the first promo card inside the "See all" promo section. Returns the promo code text. */
    public abstract String copyFirstPromoCodeFromSection();
    /** Close the "See all" promo bottom sheet/section. */
    public abstract void closeSeeAllPromoSection();

    // ---- In-app map (on PDP) ----
    /** Open the in-app map by tapping the location map preview on PDP (or by tapping "Maps" / "Lihat peta"). */
    public abstract void openInAppMap();
    /** Whether the in-app map popup / bottom sheet is shown. */
    public abstract boolean isInAppMapPopupShown();
    /** Whether the chosen hotel is highlighted on the in-app map (POI/marker visible). */
    public abstract boolean isChosenHotelHighlightedOnMap(String hotelName);
    /** Whether nearby points of interest (e.g. shopping / culinary spots) are shown on the in-app map. */
    public abstract boolean areNearbyPointsShownOnMap(String hotelName);
    /** Click the "Accommodations" / "Akomodasi" chip in the in-app map nearby section. */
    public abstract void clickNearbyChip();
    /** Whether the list of nearby accommodations / hotels is shown. */
    public abstract boolean isNearbyAccommodationListShown();
    /** Whether nearby data (e.g. POIs for that chip) is shown after a chip is selected. */
    public abstract boolean isNearbyChipDataShown();
    /** Scroll PDP quickly until the see-rooms footer is visible (room list about to open). */
    public abstract void scrollRapidlyUntilRoomListAboutToOpen();
    /** Whether the in-app map back button is within the device safe area. */
    public abstract boolean isInAppMapBackButtonWithinSafeArea();
    /** Whether the accommodations-nearby section is within the safe area and responds to tap. */
    public abstract boolean isAccommodationNearbySectionWithinSafeAreaAndTappable();
    /** Whether the nearby accommodation hotel card for the given hotel name is shown on the in-app map. */
    public abstract boolean isFirstNearbyAccommodationHotelCardShown(String hotelName);
    /** Whether the nearby accommodation hotel card for the given hotel name is hidden on the in-app map. */
    public abstract boolean isFirstNearbyAccommodationHotelCardHidden(String hotelName);
    /** Tap back on the in-app map screen. */
    public abstract void clickInAppMapBackButton();
    /** Whether a cross sell nudge is shown on any nearby accommodation card (scrolls the nearby list if needed). */
    public abstract boolean isCrossSellNudgeShownOnNearbyAccommodation();
    /** Hotel name of the first nearby accommodation card that has a cross sell nudge. */
    public abstract String getNearbyAccommodationHotelNameWithCrossSell();
    /** Tap the first nearby accommodation card that has a cross sell nudge. */
    public abstract void clickNearbyAccommodationWithCrossSell();

    protected boolean isElementCenterWithinSafeArea(WebElement element) {
        Dimension screen = driver.manage().window().getSize();
        int topSafe = (int) (screen.getHeight() * 0.08);
        int bottomSafe = (int) (screen.getHeight() * 0.92);
        int leftSafe = (int) (screen.getWidth() * 0.02);
        int rightSafe = (int) (screen.getWidth() * 0.98);
        int centerX = element.getLocation().getX() + element.getSize().getWidth() / 2;
        int centerY = element.getLocation().getY() + element.getSize().getHeight() / 2;
        return centerX >= leftSafe && centerX <= rightSafe && centerY >= topSafe && centerY <= bottomSafe;
    }

    public String getClipboardText() {
        return driverTools.getClipboardText();
    }

}