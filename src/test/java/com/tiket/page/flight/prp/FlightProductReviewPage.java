package com.tiket.page.flight.prp;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public abstract class FlightProductReviewPage <T extends WebDriver> extends BasePage<T> {
    public FlightProductReviewPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightProductReviewPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new FlightProductReviewPageAndroid<>(session);}
            case IOS -> {return new FlightProductReviewPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void swipeTo100pSection();
    public abstract boolean check100pRefundAttribute();
    public abstract boolean checkPrice();
    public abstract void continueToBookingPageUsing100pSelectBtn();
    public abstract void swipeToRegularSection();
    public abstract boolean isCorporateDealsPresent();
    public abstract void continueToBookingForm(String tripType);
    public abstract boolean isProductReviewPageDisplayed();
    public abstract void checkRefundAsAddOnPRP(int pax, String currency);
    public abstract void clickSelectRegularBySwipingDown();
    public abstract void clickSelectBtnRegular();
    public abstract boolean isAirlineLogoDisplayedPRP(); //Fixme Implementation pending iOS-> missing unique id
    public abstract boolean isAirlineRouteCorrectPRP(String route); //Fixme Implementation pending iOS-> missing unique id
    public abstract boolean verifyPriceBreakdownPRP();
    public abstract boolean isNotMeetTravelPolicyPopupDisplayed();
    public abstract boolean verifyPromoInfinitySection();
    public abstract boolean isLabelPromoInfinityDisplayed();
    public abstract boolean isCheckInAgainTextDisplayed();
    public abstract boolean isConnectingFlightProtection();
    public abstract void clickDetailPRP();
    public abstract void clickRefundReschedulePolicyTab();
    public abstract boolean verifyRefundInfo();
    public abstract boolean verifyRescheduleInfo();
    /** Open the "Refund" sub-tab inside the policy details popup. */
    public abstract void clickRefundTab();
    /** Open the "Reschedule" sub-tab inside the policy details popup. */
    public abstract void clickRescheduleTab();
    /** Switch the visible flight leg to "Depart" inside the active policy sub-tab. */
    public abstract void clickDepartChip();
    /** Switch the visible flight leg to "Return" inside the active policy sub-tab. */
    public abstract void clickReturnChip();
    /**
     * For the currently-active policy sub-tab and chip, returns true when EITHER a populated row
     * container is rendered (info available) OR the "...information is not available" copy is shown.
     */
    public abstract boolean isPolicyInfoVisible();

    /**
     * Peeks at the policy details popup and returns {@code true} ONLY when the populated row
     * container ({@code INFO_ROW_CONTAINER}) is rendered — i.e. actual refund/reschedule policy
     * data is shown, not the "information is not available" empty-state fallback.
     * <p>
     * Unlike {@link #isPolicyInfoVisible()} this method has NO side effects and must be called
     * BEFORE {@code isPolicyInfoVisible()} (which closes the popup) when both are needed in the
     * same chip/leg check. Used by tests to gate downstream assertions (e.g. payment-page refund
     * banners) on whether the PRP actually showed policy data for the given leg.
     */
    public abstract boolean isPolicyRowVisible();
    public abstract boolean isRefundNotAllowedTextDisplayed();
    public abstract boolean isRescheduleNotAllowedTextDisplayed();
    /** Prefer No upgrade Select when present; otherwise the individual fare Select/Pilih. */
    public abstract void continueToBookingForm();

    /**
     * Returns {@code true} when the PRP is rendered in multi-fare mode — i.e. the airline
     * exposes per-leg insurance fares ("tiket 100% Refund DP", "tiket 100% Refund Reschedule DP",
     * etc.) selected through a two-step stepper ("1. Select depart ticket — 2. Select return ticket").
     * In multi-fare mode the policy details popup is opened per fare card and shows ONE leg only —
     * there are no Depart/Return chips on the popup, unlike the legacy single-fare flow.
     */
    public abstract boolean isMultiFarePRP();

    /**
     * Multi-fare counterpart of the legacy chip-based Refund/Reschedule verification.
     * <p>
     * For each leg of the multi-fare stepper this method:
     * <ol>
     *   <li>Opens the fare card's {@code See details} popup</li>
     *   <li>Asserts policy info is rendered under the Refund tab</li>
     *   <li>Asserts policy info is rendered under the Reschedule tab</li>
     *   <li>Closes the popup and clicks Select to advance to the next step</li>
     * </ol>
     * For {@code roundtrip} both Depart and Return legs are verified; for {@code oneway} only
     * Depart. The method ends on the booking form (handles {@code skipFullProtectionPrp} just like
     * {@link #continueToBookingForm(String)}) so callers should NOT also call
     * {@code continueToBookingForm} afterwards.
     * <p>
     * Returns a map keyed by leg label ({@code "depart"}, {@code "return"}) whose values report
     * whether the populated row container was rendered on EITHER the Refund or Reschedule tab of
     * that leg's popup. {@code false} means only the "information is not available" empty-state
     * was shown. Callers use this to gate downstream assertions (e.g. payment-page refund
     * banners) that would not be rendered when PRP has no policy data. For one-way trips the
     * {@code "return"} entry is set to {@code false}.
     */
    public abstract Map<String, Boolean> verifyRefundRescheduleInfoMultiFareAndContinue(String tripType);

    /**
     * Selects a fare card matching {@code criteria} on the current PRP fare step.
     * Supported criteria (case-insensitive, hyphen-separated): {@code regular},
     * {@code free meal}, {@code free seat} (e.g. {@code free meal-free seat}).
     */
    public abstract void selectFare(String criteria);

    /**
     * Selects depart (and return for roundtrip) fares by criteria, then lands on booking form.
     */
    public abstract void continueToBookingFormWithFareCriteria(String tripType, String departFareCriteria, String returnFareCriteria);
}
