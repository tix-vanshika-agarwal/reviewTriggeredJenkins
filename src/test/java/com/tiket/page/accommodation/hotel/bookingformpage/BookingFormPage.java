package com.tiket.page.accommodation.hotel.bookingformpage;

import com.tiket.model.AddOnsType;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.reschedulepage.HotelReschedulePage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static com.tiket.core.CoreFunctions.convertPrice;
import static com.tiket.core.CoreFunctions.doubleRound;
import static com.tiket.core.CoreFunctions.randomName;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public abstract class BookingFormPage <T extends WebDriver> extends BasePage<T> {

    public BookingFormPage(Session<T> session) { super(session); }

    public static <T extends WebDriver> BookingFormPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new BookingFormAndroid<>(session); }
            case IOS -> { return new BookingFormIOS<>(session); }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    /** Matches an existing salutation (Mr/Mrs/Ms/Tuan/Nyonya/Nona) in a contact name; same for Android and iOS. */
    protected static final Pattern SALUTATION_PATTERN = Pattern.compile("(?i)\\b(mr|mrs|ms|tuan|nyonya|nona)\\b");
    public abstract String getLocalPriceValue();
    public abstract String detectAndDismissPriceChangeSnackbar();
    public abstract String cancelRatePLanBottomSheetBookingPart1();
    public abstract String getLocalPriceOnPriceDetails();
    public abstract void fillContactOrderDetail();
    public abstract void fillContactOrderDetail(String email);
    public abstract String fillGuestDetail(boolean checkRoomName);
    public abstract void openGuestDetailForm();
    public abstract String verifyGuestDetailNameSize(int numberOfNameChar);
    public abstract String verifyGuestDetailDefaultNationality(String expectedNationality);
    public abstract String getNameOnGuestDetail(String expectedName);
    public abstract ArrayList<String> verifyGuestDetailNameSizeMO(int numberOfRoom,String expectedNationality);
    public abstract boolean selectSuggestedNameAndVerifyNationality(String expectedName, String expectedNationality);
    public abstract boolean fillGuestDetailNoSalutation();
    public abstract void fillSpecialRequest(String request);
    /** Page 1 sticky footer: Pay upon check-in / Bayar saat check-in label + price both present. */
    public abstract boolean isPayUponCheckInLabelAndPriceDisplayed();
    /** Page 2: swipe to gift-voucher claim, click, assert button gone, wait snackbar appear then disappear. */
    public abstract void claimGiftVoucherOnBookingFormPage2();
    /** Page 2 Continue / Select Payment CTA label (e.g. You will pay IDR 0 today). */
    public abstract String getContinueToPaymentButtonText();
    /**
     * Clicks the continue to payment CTA whose label is read by getContinueToPaymentButtonText().
     * When the CTA is pay IDR 0 today, also confirms the booking on the bottom sheet.
     */
    public abstract void clickContinueToPaymentButton();

    /** Continue to payment CTA texts when the user pays IDR 0 today (flexible payment). */
    public static final List<String> PAY_ZERO_TODAY_TEXTS = List.of(
            "You will pay IDR 0 today",
            "Bayar IDR 0 hari ini",
            "Kamu bayar IDR 0 hari ini",
            "pay IDR 0 today",
            "Bayar IDR 0 sekarang",
            "pay IDR 0 now");

    /** True when the text contains any of PAY_ZERO_TODAY_TEXTS, ignoring uppercase and lowercase. */
    public static boolean isPayZeroTodayText(String text) {
        String lowerText = text == null ? "" : text.toLowerCase();
        return PAY_ZERO_TODAY_TEXTS.stream().anyMatch(payZeroText -> lowerText.contains(payZeroText.toLowerCase()));
    }
    /** Page 1 flexible payment (DownPayment 0%) label. */
    public abstract boolean isFlexiblePaymentLabelDisplayed();
    /** Page 1 clicks the flexible payment (DownPayment 0%) section to open its content. */
    public abstract void clickFlexiblePaymentSection();
    public abstract boolean isDownPaymentContentDisplayed();
    public abstract void clickDownPaymentContentCloseButton();
    /** Page 2 Pay by option is selected (name attribute contains "selected"). */
    public abstract boolean isPayByButtonSelected();
    public abstract List<String> getGuestNameList();
    /**
     * @deprecated This method is deprecated because the reschedule flow has changed to a webview.
     * Use {@link HotelReschedulePage#submitRescheduleBookingForm()} instead.
     */
    @Deprecated public abstract void submitRescheduleBookingForm(); //reschedule flow change to webview
    public abstract String getHotelNameBookingForm();
    public abstract String getCheckinBookingForm();
    public abstract String getDurationBookingForm();
    public abstract String getTotalPriceBookingForm();
    public abstract String getTotalPriceBeforeCashback();
    public abstract void addInsuranceOnBookingFormPage2(String estimatedTotal);

    public abstract String getTotalToPayBookingForm();
    public abstract String getTotalRefundPriceBookingForm();
    public abstract String getCrossedPriceBookingForm();
    public abstract String getTotalPriceInPriceDetail();
    public abstract String getContactNameDetail();
    public abstract String getContactPhoneNumberDetail();
    public abstract String getGuestNameDetail();
    public abstract void continueToPaymentPage();
    public  abstract  void continueToAddOnPage();
    public abstract void submitBooking();
    public abstract void chooseProtection(String protectionType);
    @Deprecated
    public abstract void openPriceDetail();
    public abstract void openSubtotal();
    public abstract int getAddOnQty(String addOnName);
    public abstract boolean isProtectionAdded(String protectionPrice);
    public abstract String getProtectionPrice();
    public abstract boolean isLabelTotalPrice();
    public boolean isProtectionPriceCorrect(String priceBefore, String protectionPrice, String priceAfter) {
        double priceBeforeDouble = convertPrice(priceBefore);
        logger.log("priceBeforeDouble -> " + priceBeforeDouble);
        double protectionPriceDouble = convertPrice(protectionPrice);
        logger.log("protectionPriceDouble -> " + protectionPriceDouble);
        double priceAfterDouble = convertPrice(priceAfter);
        logger.log("priceAfterDouble -> " + priceAfterDouble);
        return (priceBeforeDouble + protectionPriceDouble) == priceAfterDouble;
    }
    public abstract void addProtection();
    public abstract boolean  isAddonThereOnBookingForm();
    public abstract boolean isBookingFormPart2Loaded();
    public abstract void clickBackButton();
    public abstract void clickCancelButton();
    public abstract void clickSearchOtherRoom();
    public abstract void editContactOrderDetail();
    public abstract void selectRandomSalutationIfMissing();
    public abstract boolean isContactAndGuestDetailsFilled();
    public abstract boolean isContactAndGuestDetailsChanged(String contactName, String contactPhoneNumber, String guestName);
    public abstract String PriceChangesPopUp();
    public abstract String priceChangePopUpText();
    public abstract void clickPriceChangeGoPayment();
    public abstract void closeRatePlanBottomSheet();
    public abstract String getRoomNameBookingForm();
    public abstract String getCheckInDateBookingForm();
    public abstract String getCheckOutDateBookingForm();
    public abstract String getCheckInTimeBookingForm();
    public abstract String getRoomGuestBookingForm();
    public abstract String getRoomBedBookingForm();
    public abstract String getCancellationBookingForm();
    public abstract void openBookingDetailPage();
    public abstract boolean isPriceDetailCorrect(String totalPayment);
    public abstract boolean isPriceDetailRescheduleBookingFormCorrect(String totalPayment);
    public abstract void closePriceDetail();
    public abstract boolean isBookingFormShown(boolean loginStatus);
    public abstract void clickExtraFacilityAddOn();
    public abstract void cancelProtection();
    public abstract HashMap<Object, Object> clickExtraFacilityAddOn(int itemPerRoom, AddOnsType addOnsType, HashMap<Integer, String> selectedRoomName);
    void selectItem(int maxAttempt, Locator findLocator, Locator swipeLocator) {
        if (driverTools.isElementDisplayed(findLocator)) {
            driverTools.click(findLocator);
        } else {
            findItem(maxAttempt, findLocator, swipeLocator);
        }
    }

    private void findItem(int maxAttempt, Locator findLocator, Locator swipeLocator) {
        for (int i = 0; i < maxAttempt; i++) {
            driverTools.swipeLeft(swipeLocator);
            if (driverTools.isElementDisplayed(findLocator)) {
                driverTools.click(findLocator);
                break;
            }
        }
    }

    public abstract boolean checkAddOnPriceDetail(int totalRoomType, String addOnsName, String singlePrice, int itemPerRoom);
    public abstract String getAddOnPrice();
    public abstract String getAddonName();
    public abstract String getAddonTitle();
    public abstract Boolean MatchAddOnPriceDetail(Double addOnPrice, String addOnCategory);
    public abstract String getPayNowPriceBookingForm();

    public abstract String getPayUponCheckInPriceBookingForm();

    public void verifyPayNowPriceMatchesRoomPrice(String roomPrice) {
        String payNowPrice = getPayNowPriceBookingForm();
        logger.log("roomPrice from room list -> " + roomPrice);
        logger.log("payNowPrice on booking form -> " + payNowPrice);
        assertPriceEquals("Pay now price on booking form matches room list price", payNowPrice, roomPrice);
    }

    public abstract String getEstimatedTotalFromBookingFormPage1();

    public void verifyEstimatedTotalIsCorrect(String roomPrice, String payUponCheckInPrice,
                                              String estimatedTotal) {
        double expected = doubleRound(convertPrice(payUponCheckInPrice) + convertPrice(roomPrice));
        double actual = convertPrice(estimatedTotal);
        logger.log("roomPrice -> " + roomPrice);
        logger.log("payUponCheckInPrice -> " + payUponCheckInPrice);
        logger.log("expected estimated total (pay upon check-in + room price) -> " + expected);
        logger.log("actual estimated total -> " + estimatedTotal + " (" + actual + ")");
        assertPriceEquals("Estimated total equals pay upon check-in plus room price", actual, expected);
    }

    public abstract String getTotalPriceBookingFormPage2();

    public void verifyTotalPriceMatchesPriceOnBookingForm(String roomPrice) {
        String totalPricePage2 = getTotalPriceBookingFormPage2();
        logger.log("roomPrice from room list -> " + roomPrice);
        logger.log("total price on booking form page 2 -> " + totalPricePage2);
        assertPriceEquals("Total price on booking form page 2 matches room list price", totalPricePage2, roomPrice);
    }

    public abstract String getPayUponCheckInPriceBookingFormPage2();

    public abstract String getEstimatedTotalFromBookingFormPage2();

    public void verifyEstimatedTotalIsCorrectOnBF2(String estimatedPage1, String estimatedPage2) {
        logger.log("estimated total booking form page 1 -> " + estimatedPage1);
        logger.log("estimated total booking form page 2 -> " + estimatedPage2);
        assertPriceEquals("Estimated total on booking form page 1 matches page 2", estimatedPage2, estimatedPage1);
    }

    void assertPriceEquals(String message, String actualPriceText, String expectedPriceText) {
        assertPriceEquals(message, convertPrice(actualPriceText), convertPrice(expectedPriceText));
    }

    private void assertPriceEquals(String message, double actual, double expected) {
        Assertion.assertThat(message, actual, is(closeTo(doubleRound(expected), 0.001)));
    }

    public abstract String getTotalPricePayAtHotelBookingForm();
    public abstract String loyaltyBenefitValueText();
    public abstract String getHotelRoomNameBookingForm();
    public abstract boolean checkChildGuest();
    public abstract boolean checkChildMeal();
    public abstract String getHotelRoomCountBookingForm();
    public abstract void openLoginPage();
    public abstract boolean isLoggedIn();
    public abstract boolean canNotBookMultiOrderCorporatePopUp();
    public abstract boolean canNotBookMultiOrderTravellerPopUp();
    public abstract String getEstimationRefund();
    public abstract List<String> getPriceDetailPrices(String totalPayment);
    public abstract List<String> getPriceDetailPricesReschedule();
    public abstract void addPromo(String promo);
    public abstract boolean isPayingThisAmountTextInGreen();
    public void handleRoundOffNonIDR(String priceRoomDetail, String priceBookingForm,String currency){
        if(currency.equals("IDR")){
            Assertion.assertThat("verify price on booking form", convertPrice(priceBookingForm), equalTo(convertPrice(priceRoomDetail)));
        }else {
            double priceRoomDetailDouble = convertPrice(priceRoomDetail);
            double priceBookingFormDouble = convertPrice(priceBookingForm);
            logger.log("priceRoomDetailDouble -> " + priceRoomDetailDouble);
            logger.log("priceBookingFormDouble -> " + priceBookingFormDouble);
            double difference = Math.abs(priceRoomDetailDouble - priceBookingFormDouble);
            logger.log("difference -> " + difference);
            boolean isPriceCorrect = difference < 1;
            Assertion.assertThat("verify price on booking form", isPriceCorrect, is(true));
        }
    }
   public abstract List<String> getPriceTitles();

    protected ArrayList<String> generateGuestName(int numberOfRoom, boolean ifRequiredWrongName){
        ArrayList<String> guestNameList = new ArrayList<>();
        boolean flagFor2OrMoreThen50 = true;
        for(int i=1;i<=numberOfRoom;i++){
            if(ifRequiredWrongName && i!=numberOfRoom){
                String guestName = flagFor2OrMoreThen50?randomName(2):randomName(51);
                guestNameList.add(guestName);
                flagFor2OrMoreThen50 = !flagFor2OrMoreThen50;
                continue;
            }
            String guestName = randomName();
            guestNameList.add(guestName);
        }
        return guestNameList;
    }
    public void verifyEstimatedTotalUnchangedAfterAddOns(String estimatedTotalBeforeInsurance) {
        String estimatedTotalAfter = getEstimatedTotalFromBookingFormPage2();
        logger.log("estimated total before insurance -> " + estimatedTotalBeforeInsurance);
        logger.log("estimated total after insurance -> " + estimatedTotalAfter);
        assertPriceEquals("Estimated total unchanged after adding insurance on booking form page 2",
                estimatedTotalAfter, estimatedTotalBeforeInsurance);
    }

    protected ArrayList<String> correctTheGuestNameList(ArrayList<String> guestNameList){
        ArrayList<String> correctGuestNameList = new ArrayList<>();
        for(String guestName : guestNameList){
            String correctedName = guestName;
            if(guestName.length() < 3 || guestName.length() > 50){
                correctedName = randomName();
            }
            correctGuestNameList.add(correctedName);
        }
        return correctGuestNameList;
    }
}