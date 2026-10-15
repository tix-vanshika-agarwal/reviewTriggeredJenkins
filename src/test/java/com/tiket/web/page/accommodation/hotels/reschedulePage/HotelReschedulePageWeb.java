package com.tiket.web.page.accommodation.hotels.reschedulePage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.reschedulepage.HotelReschedulePage;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class HotelReschedulePageWeb<T extends WebDriver> extends WebPage<T> {

    public HotelReschedulePageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HotelReschedulePageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HotelReschedulePageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new HotelReschedulePageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    public static final Locator SELECT_NEW_DATE_BUTTON = new Locator("SELECT_NEW_DATE_BUTTON", AppiumBy.xpath("//*[@data-testid='checkin-checkout-date-value']"));
    public static final Locator CHECK_AVAILABILITY_BUTTON = new Locator("CHECK_AVAILABILITY_BUTTON", AppiumBy.xpath("//*[@data-testid='date-filter-container']//button"));
    public Locator nextDateLocator(int addDays) { return new Locator("ADD_DATE_" + addDays, AppiumBy.xpath("//button[contains(@class,'day_selected')]//following::button[not(contains(@class,'day_selected')) and not(contains(@class,'next_button'))]["+addDays+"]")); }
    private static final Locator ROOM_REFUND_ESTIMATION = new Locator("ROOM_REFUND_ESTIMATION", AppiumBy.xpath("//*[contains(@class,'PriceModule_container')]//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'refund')]"));
    private static final Locator BOOK_BUTTON_ROOM_REFUND = new Locator("BOOK_BUTTON_ROOM_REFUND", AppiumBy.xpath("(//*[contains(@class,'PriceModule_container')]//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'refund')]//following::button[contains(@data-testid,'book-button')])[2]"));
    // First reschedule rate plan card's price module + its two children (main price line and
    // the subtitle line below it). Used by {@link #getFirstRoomPriceInfo()} to classify the
    // rendered state without timing out when a "Get refund …" subtitle isn't present.
    private static final String FIRST_PRICE_MODULE_XPATH = "(//*[contains(@class,'PriceModule_container')])[1]";
    private static final Locator FIRST_ROOM_PRICE_MAIN = new Locator("FIRST_ROOM_PRICE_MAIN",
            AppiumBy.xpath(FIRST_PRICE_MODULE_XPATH + "//*[contains(@class,'Text_variant_price')]"));
    private static final Locator FIRST_ROOM_PRICE_SUBTITLE = new Locator("FIRST_ROOM_PRICE_SUBTITLE",
            AppiumBy.xpath(FIRST_PRICE_MODULE_XPATH + "/*[not(contains(@class,'final_price_wrapper'))]"));
    // Book button of the same rate plan card as FIRST_PRICE_MODULE: in the DOM the price
    // module is followed by the CTA wrapper containing exactly one book-button-* button,
    // so document-order following::button[1] reliably hits the card we just read.
    private static final Locator FIRST_RATE_PLAN_BOOK_BUTTON = new Locator("FIRST_RATE_PLAN_BOOK_BUTTON",
            AppiumBy.xpath(FIRST_PRICE_MODULE_XPATH + "/following::button[contains(@data-testid,'book-button')][1]"));
    private static final Locator SECTION_ADDITIONAL_PRICE = new Locator("SECTION_ADDITIONAL_PRICE", AppiumBy.xpath("//*[contains(@class,'RatePlanPrice_final_price')]//*[contains(@class,'Text_variant_price') and contains(text(),'+')]"));
    private static final Locator SECTION_CASHBACK_PRICE = new Locator("SECTION_CASHBACK_PRICE", AppiumBy.xpath("//*[contains(@class,'RatePlanPrice_reschedule_refundable_price_module')]//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'dapat refund') or contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'get refund')]"));
    private static final Locator BOOK_BUTTON_ADDITIONAL_PRICE = new Locator("BOOK_BUTTON_ADDITIONAL_PRICE", AppiumBy.xpath("//*[contains(@class,'RatePlanPrice_final_price')]//*[contains(@class,'Text_variant_price') and contains(text(),'+')]//ancestor::*[contains(@class,'RatePlanPrice_reschedule_refundable_price_module')]//following-sibling::*[contains(@class, 'RatePlanPrice_cta')]//*[contains(@data-testid, 'book-button')]"));
    private static final Locator LOADING = new Locator("LOADING", AppiumBy.xpath("//*[contains(@class,'LoadingWithIllustration_title')]"));
    private static final Locator AGREE_TNC_CHECK = new Locator("AGREE_TNC_CHECK", By.cssSelector("[data-testid='check-agreement-reschedule']"));
    private static final Locator SUBMIT_RESCHEDULE_BOOK_BUTTON = new Locator("SUBMIT_RESCHEDULE_BOOK_BUTTON", AppiumBy.xpath("//*[contains(@class,'PriceFooter_submit_reschedule_info')]//button"));
    private static final Locator CONFIRM_RESCHEDULE = new Locator("CONFIRM_RESCHEDULE", AppiumBy.xpath("//*[contains(@class,'Dialog_pop_up')]//button[contains(@class,'variant_primary')]"));
    private static final Locator TOTAL_PRICE_TEXT = new Locator("TOTAL_PRICE_TEXT", AppiumBy.xpath("//*[contains(@class,'PriceFooter_price_detail_content')]//span"));
    private static final Locator CONTACT_DETAIL_NAME = new Locator("CONTACT_DETAIL_NAME", AppiumBy.xpath("//*[contains(@class,'RoomGuestRequestItem_guest_item_name_text')]"));
    private static final Locator GUEST_NAME_INPUT = new Locator("GUEST_NAME_INPUT", AppiumBy.xpath("//*[@data-testid='guest-stay-detail-full-name-input']"));
    private static final Locator SAVE_GUEST_BUTTON = new Locator("SAVE_GUEST_BUTTON", AppiumBy.xpath("//button[contains(@class,'Button_button') and (.//span[normalize-space()='Save' or normalize-space()='Simpan'] or normalize-space()='Save' or normalize-space()='Simpan')]"));
    private static final Locator FREE_PAYMENT_NO_CASHBACK_ROOM = new Locator("FREE_PAYMENT_NO_CASHBACK_ROOM", AppiumBy.xpath("//*[contains(.,'Gratis') or contains(.,'Free')]//parent::*[contains(@class,'PriceInformation_price_wrapper')]//following-sibling::*[not(contains(text(),'Dapat refund') or contains(text(),'Get refund'))]//parent::div//parent::*[contains(@class,'PriceInformation_price_information_container')]//parent::*[contains(@class,'RoomCard_details_container')]//preceding-sibling::*//*[contains(@class,'room_name_title')]"));
    private static final Locator FREE_PAYMENT_NO_CASHBACK_BOOK_BUTTON = new Locator("FREE_PAYMENT_NO_CASHBACK_BOOK_BUTTON", AppiumBy.xpath("//*[contains(.,'Gratis') or contains(.,'Free')]//parent::*[contains(@class,'PriceInformation_price_wrapper')]//following-sibling::*[not(contains(text(),'Dapat refund') or contains(text(),'Get refund'))]//parent::div//following-sibling::*//button"));
    private static final Locator FREE_PAYMENT_CASHBACK_ROOM = new Locator("FREE_PAYMENT_CASHBACK_ROOM", AppiumBy.xpath("//*[contains(.,'Gratis') or contains(.,'Free')]//parent::*[contains(@class,'PriceInformation_price_wrapper')]//following-sibling::*[contains(text(),'Dapat refund') or contains(text(),'Get refund')]//parent::div//parent::*[contains(@class,'PriceInformation_price_information_container')]//parent::*[contains(@class,'RoomCard_details_container')]//preceding-sibling::*//*[contains(@class,'room_name_title')]"));
    private static final Locator FREE_PAYMENT_CASHBACK_BOOK_BUTTON = new Locator("FREE_PAYMENT_CASHBACK_BOOK_BUTTON", AppiumBy.xpath("//*[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'gratis') or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'free')]//parent::*[contains(@class,'RatePlanPrice_reschedule_refundable_price_module')]//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'dapat refund') or contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'get refund')]//ancestor::*[contains(@class,'RatePlanPrice_reschedule_refundable_price_module')]//following::button[contains(@data-testid, 'book-button')]"));
    private static final Locator PRICE_DETAIL_BUTTON = new Locator("PRICE_DETAIL_BUTTON", AppiumBy.xpath("//*[@data-testid='icon-chevron-down-price-detail']"));
    private static final Locator TOTAL_PRICE_DETAIL = new Locator("TOTAL_PRICE_DETAIL", AppiumBy.xpath("//*[@data-testid='total-price-container']//span[last()]"));
    private Locator totalPriceDetail(String totalPrice) { return new Locator("TOTAL_PRICE_DETAIL", AppiumBy.xpath("//*[@data-testid='total-price-container']//span[text()='"+totalPrice+"']")); }
    private static final Locator CLOSE_PRICE_DETAIL = new Locator("CLOSE_PRICE_DETAIL", AppiumBy.xpath("//button[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator BOOK_BUTTON_ROOM_DETAIL = new Locator("BOOK_BUTTON_ROOM_DETAIL", AppiumBy.xpath("//*[@data-testid='purchase-btn-wrapper']//button"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//*[@data-testid='mobile-back-chevron-icon']"));
    private static final Locator STAY = new Locator("STAY", AppiumBy.xpath("//h3[contains(.,'Stay Details') or contains(.,'Detail Menginap')]"));
    private static final Locator LOADING_WITH_ILLUSTRATION_CARD = new Locator("LOADING_WITH_ILLUSTRATION_CARD", AppiumBy.xpath("//*[contains(@class,'LoadingWithIllustration_card')]"));
    private static final Locator FIRST_BOOK_BUTTON = new Locator("FIRST_BOOK_BUTTON", AppiumBy.xpath("(//*[contains(@data-testid,'book-button')])[1]"));


    // Default parameter valueMethod with no parameters (Uses the default 200ms)
    public int clickAnyBookButton() {
        return clickAnyBookButton(200); // Calls the method below with default value
    }

    // The actual method that takes the parameter
    public int clickAnyBookButton(int delayMs) {
        driverTools.scrollToElement(FIRST_BOOK_BUTTON);
        WebElement bookButton = driverTools.waitForElementToAppear(FIRST_BOOK_BUTTON).orElseThrow();
    
        int clickSuccessCount = 0;

        try {
            for (int i = 1; i <= 5; i++) {
                logger.log("Clicking book button attempt: " + i);
                
                // Need to click directly to bypass double-booking prevention delays
                driverTools.click(bookButton);
                
                logger.log("Delay " + delayMs + "ms");
                Thread.sleep(delayMs);
                
                logger.log("Successfully clicked book button: " + i + " times");
                clickSuccessCount = i;
            }
        } catch (Exception e) {
            // If it fails on the 2nd click, this means the UI correctly blocked it (Overlay or Disabled state)
            logger.log("Click intercepted/failed on attempt: " + (clickSuccessCount + 1));
        }
    
        // If clickSuccessCount == 0, the very first click failed. 
        // We shouldn't wait for a loading card that will never come.
        if (clickSuccessCount == 1) {
            try {
                // Verify it appears (Wait instead of just checking boolean)
                // Use a short timeout here (e.g., 2-3 seconds) so it doesn't hang if it loads too fast
                driverTools.waitForElementToAppear(LOADING_WITH_ILLUSTRATION_CARD);
            } catch (Exception e) {
                logger.log("Loading card did not appear or disappeared too quickly.");
            } finally {
                // ALWAYS wait for it to disappear so the UI is clean for the next steps/assertions,
                // even if the user successfully spam-clicked it 2 or 3 times.
                driverTools.waitForElementToDisappear(LOADING_WITH_ILLUSTRATION_CARD, 20);
            }
        }
        
        return clickSuccessCount;
    }   

    public boolean isBookingPageShown(){
        return driverTools.waitForElementToAppear(STAY).isPresent();
    }

    public void clickBackButton(){
        driverTools.waitForElementToAppear(BACK_BUTTON);
        driverTools.click(BACK_BUTTON);
    }

    public boolean isInReschedulePage(){
        return driverTools.waitForElementsToAppear(SELECT_NEW_DATE_BUTTON).isPresent();
    }

    public abstract String changeNightDuration(int nightDuration);

    public abstract String getRoomName();

    public String getTheFirstRoomRefundEstimation() {
        driverTools.waitForElementToAppear(ROOM_REFUND_ESTIMATION);
        String getRefundEstimation = driverTools.getText(ROOM_REFUND_ESTIMATION).orElseThrow()
                .replace("Get refund ", "")
                .replace("Dapat refund ", "");
        logger.log("getRefundEstimation: " + getRefundEstimation);
        return getRefundEstimation;
    }

    /**
     * Mutually-exclusive states the price module of the first reschedule rate plan card can
     * render in. The reschedule-refundable flow always shows exactly one of these.
     */
    public enum RoomPriceState {
        /**
         * Main price reads {@code "IDR 0 (Free)"} / {@code "IDR 0 (Gratis)"}, subtitle is
         * empty. The new room costs the same as (or is fully covered by) the original
         * booking — no extra payment and no refund.
         */
        FREE,
        /**
         * Main price starts with {@code "+"} (e.g. {@code "+IDR 8,653"}) and the subtitle
         * reads {@code "Total price (include taxes)"} / {@code "Total harga (termasuk pajak)"}.
         * User must pay the difference.
         */
        ADDITIONAL,
        /**
         * Subtitle reads {@code "Get refund <amount>"} / {@code "Dapat refund <amount>"}.
         * User is refunded the difference because the new room is cheaper than the original.
         */
        REFUND
    }

    /**
     * Snapshot of the first reschedule rate plan card's price module: the {@link RoomPriceState}
     * it is in, the raw price + subtitle texts as rendered, and the numeric IDR amount
     * ({@code 0} for {@link RoomPriceState#FREE}, the additional payment for
     * {@link RoomPriceState#ADDITIONAL}, the refund value for {@link RoomPriceState#REFUND}).
     */
    public record RoomPriceInfo(RoomPriceState state, String priceText, String subText, double amount) {}

    /**
     * Unified read-side accessor for the first reschedule rate plan card's price module.
     * Handles all three rendered states ({@link RoomPriceState#FREE},
     * {@link RoomPriceState#ADDITIONAL}, {@link RoomPriceState#REFUND}) without timing out
     * when no refund-eligible alternative exists for the selected dates.
     *
     * <p>Long-form replacement for {@link #getTheFirstRoomRefundEstimation()}, which only
     * works when the new room is cheaper than the original booking. Callers that need to
     * branch by state (book the refund room, otherwise pay the additional, otherwise pick
     * the free room, etc.) should use this method instead of the legacy refund-only one.
     *
     * @return classified snapshot of the rendered price module; never {@code null}.
     * @throws java.util.NoSuchElementException when the price module never appears (the
     *         room list itself failed to render — that's a real failure, not a no-refund run).
     */
    public RoomPriceInfo getFirstRoomPriceInfo() {
        WebElement priceElement = driverTools.waitForElementToAppear(FIRST_ROOM_PRICE_MAIN, 30).orElseThrow();
        String mainPrice = driverTools.getText(priceElement).trim();
        // Subtitle child is always present in the DOM (even if its inner span is empty for
        // the FREE case), so a short timeout is enough — we only fall back to "" defensively.
        String subText = driverTools.waitForElementToPresence(FIRST_ROOM_PRICE_SUBTITLE, 5)
                .map(driverTools::getText)
                .map(String::trim)
                .orElse("");

        String mainLower = mainPrice.toLowerCase(Locale.ROOT);
        String subLower = subText.toLowerCase(Locale.ROOT);

        RoomPriceState state;
        double amount;
        if (subLower.contains("refund") || mainLower.contains("refund")) {
            state = RoomPriceState.REFUND;
            amount = CoreFunctions.convertPriceSafely(subLower.contains("refund") ? subText : mainPrice);
        } else if (mainPrice.startsWith("+")) {
            state = RoomPriceState.ADDITIONAL;
            amount = CoreFunctions.convertPriceSafely(mainPrice);
        } else {
            // Default bucket: "IDR 0 (Free)" / "IDR 0 (Gratis)" / anything that's neither
            // additional ('+') nor refund-prefixed. Treat as same-price (no extra, no refund).
            state = RoomPriceState.FREE;
            amount = 0.0;
        }
        logger.log("RoomPriceInfo[state=" + state + ", amount=" + amount
                + ", priceText='" + mainPrice + "', subText='" + subText + "']");
        return new RoomPriceInfo(state, mainPrice, subText, amount);
    }

    /**
     * Reads the first reschedule rate plan card's price module via
     * {@link #getFirstRoomPriceInfo()} and clicks the Book button of that <em>same</em>
     * card, regardless of whether the card is in {@link RoomPriceState#FREE},
     * {@link RoomPriceState#ADDITIONAL}, or {@link RoomPriceState#REFUND} state.
     *
     * <p>Use this when the test doesn't care which of the three states is rendered (the
     * data run can produce any of them) and only needs to advance to the booking form
     * with whichever room is shown first. The returned {@link RoomPriceInfo} carries the
     * state + amount so callers can still drive downstream assertions
     * (e.g. {@code isPaymentFree}, refund amount checks).
     *
     * <p>Implementation note: anchors to the first {@code PriceModule_container} and
     * clicks the next {@code book-button-*} in document order — always the CTA of the
     * same card we just read. This avoids the legacy free/additional/refund locators
     * which assume a DOM structure ({@code PriceInformation_*}, {@code RoomCard_*})
     * that the reschedule-refundable webview no longer renders.
     *
     * @return the {@link RoomPriceInfo} that classifies the booked card.
     */
    public RoomPriceInfo bookFirstAvailableRoom() {
        RoomPriceInfo priceInfo = getFirstRoomPriceInfo();
        WebElement bookButton = driverTools.waitForElementToAppear(FIRST_RATE_PLAN_BOOK_BUTTON, 30).orElseThrow();
        driverTools.scrollIntoWebView(bookButton);
        driverTools.clickWithJavascriptExecutor(bookButton);
        return priceInfo;
    }

    public void openRoomWithAdditionalPrice() {
        driverTools.waitForElementToAppear(SECTION_ADDITIONAL_PRICE);
        driverTools.scrollIntoWebView(SECTION_ADDITIONAL_PRICE);
        driverTools.clickWithJavascriptExecutor(SECTION_ADDITIONAL_PRICE);
    }

    public void openRoomWithCashback() {
        driverTools.waitForElementToAppear(SECTION_CASHBACK_PRICE);
        driverTools.scrollIntoWebView(SECTION_CASHBACK_PRICE);
        driverTools.clickWithJavascriptExecutor(SECTION_CASHBACK_PRICE);
    }

    public String chooseRoomWithAdditionalPrice() {
        driverTools.waitForElementToAppear(SECTION_ADDITIONAL_PRICE);
        String roomName = getRoomNameWithAdditionalPrice();
        driverTools.clickWithJavascriptExecutor(BOOK_BUTTON_ADDITIONAL_PRICE);
        return roomName;
    }

    public abstract String getRoomNameWithAdditionalPrice();

    public void chooseRoomWithAdditionalPriceNoRoomName() {
        driverTools.waitForElementToAppear(SECTION_ADDITIONAL_PRICE);
        driverTools.clickWithJavascriptExecutor(BOOK_BUTTON_ADDITIONAL_PRICE);
    }

    public void bookRoomRefund() {
        driverTools.waitForElementToAppear(BOOK_BUTTON_ROOM_REFUND);
        driverTools.clickWithJavascriptExecutor(BOOK_BUTTON_ROOM_REFUND);
    }

    /**
     * Opens the guest stay detail form on the reschedule booking form, replaces the guest
     * full name, and saves. Call this on the {@code reschedule/checkout} webview before
     * {@link #submitRescheduleBookingForm()}.
     *
     * @param guestName new guest full name to fill
     */
    public void fillGuestName(String guestName) {
        driverTools.waitForElementToDisappear(LOADING);
        driverTools.waitForElementToAppear(CONTACT_DETAIL_NAME);
        driverTools.clickWithJavascriptExecutor(CONTACT_DETAIL_NAME);

        WebElement guestNameInput = driverTools.waitForElementToAppear(GUEST_NAME_INPUT).orElseThrow();
        driverTools.clearInput(guestNameInput);
        driverTools.sendKeys(guestNameInput, guestName);
        logger.log("Filled reschedule guest name: " + guestName);

        driverTools.waitForElementToAppear(SAVE_GUEST_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(SAVE_GUEST_BUTTON);
    }

    public void submitRescheduleBookingForm() {
        driverTools.waitForElementToDisappear(LOADING);
        // Form readiness signal — loading timeout alone is unreliable when the spinner class lingers
        driverTools.waitForElementToPresence(STAY, 30).orElseThrow();

        if (Configs.PLATFORM == Platform.ANDROID) {
            driverTools.switchToNative();
            HotelReschedulePage<T> hotelReschedulePage = HotelReschedulePage.getInstance(session);
            hotelReschedulePage.clickTNCCheckboxNative();
            driverTools.switchToWebViewUrl("reschedule");
        } else {
            // Checkbox sits below the fold; progressive scroll finds it even when not yet in viewport
            WebElement agreeTnc = driverTools.scrollUntilElementFound(AGREE_TNC_CHECK).orElseThrow();
            driverTools.clickWithJavascriptExecutor(agreeTnc);
        }

        driverTools.isElementDisplayed(SUBMIT_RESCHEDULE_BOOK_BUTTON);
        driverTools.clickWithJavascriptExecutor(SUBMIT_RESCHEDULE_BOOK_BUTTON);

        driverTools.isElementDisplayed(CONFIRM_RESCHEDULE);
        driverTools.clickWithJavascriptExecutor(CONFIRM_RESCHEDULE);
    }

    public String getTotalPriceBookingForm(){
        driverTools.waitForElementToDisappear(LOADING);

        driverTools.waitForElementToPresence(CONTACT_DETAIL_NAME);
        WebElement totalPriceElement = driverTools.waitForElementToPresence(TOTAL_PRICE_TEXT).orElseThrow();
        String totalPrice = driverTools.getText(totalPriceElement);
        logger.log("total price booking form : " + totalPrice);

        return totalPrice;
    }

    public String chooseRoomWithFreePaymentNoCashback() {
        driverTools.waitForElementToAppear(FREE_PAYMENT_NO_CASHBACK_ROOM);
        driverTools.scrollIntoWebView(FREE_PAYMENT_NO_CASHBACK_ROOM);
        String roomName = driverTools.getText(FREE_PAYMENT_NO_CASHBACK_ROOM).orElseThrow();
        logger.log("Selected free payment no cashback room: " + roomName);
        driverTools.click(FREE_PAYMENT_NO_CASHBACK_BOOK_BUTTON);
        return roomName;
    }

    public void chooseRoomWithCashback() {
        driverTools.waitForElementToAppear(FREE_PAYMENT_CASHBACK_BOOK_BUTTON);
        driverTools.scrollElementToCenter(FREE_PAYMENT_CASHBACK_BOOK_BUTTON);
        driverTools.click(FREE_PAYMENT_CASHBACK_BOOK_BUTTON);
    }

    public abstract String openRoomDetail();

    public void chooseRoomWithSamePrice(String expectedPrice) {
        driverTools.waitForElementToAppear(PRICE_DETAIL_BUTTON).orElseThrow();
        driverTools.click(PRICE_DETAIL_BUTTON);
        if (driverTools.isElementDisplayed(totalPriceDetail(expectedPrice))) {
            logger.log("Found room with same price: " + expectedPrice);
            driverTools.clickWithJavascriptExecutor(CLOSE_PRICE_DETAIL);
            driverTools.click(BOOK_BUTTON_ROOM_DETAIL);
        } else {
            String actualTotalPrice = driverTools.getText(TOTAL_PRICE_DETAIL).orElseThrow();
            logger.softWarning("No room found with the expected price: " + expectedPrice + ", actual total price is: " + actualTotalPrice);
            throw new AssertionError("No room found with the expected price: " + expectedPrice);
        }
    }

    public String getPriceRoomWithAdditionalPrice() {
        driverTools.waitForElementToAppear(SECTION_ADDITIONAL_PRICE);
        driverTools.scrollIntoWebView(SECTION_ADDITIONAL_PRICE);
        return driverTools.getText(SECTION_ADDITIONAL_PRICE).orElseThrow();
    }

    // ---------------------------------------------------------------------------------------
    // Non-refundable (NO_CANCELLATION) reschedule form
    // ---------------------------------------------------------------------------------------
    // The non-refundable variant of this page is rendered when the booked room cancellation
    // policy is NO_CANCELLATION ("Refund & reschedule not allowed"). DOM is identical on
    // Android/iOS WebView and dWeb; only the new-date picker differs per platform (handled
    // by {@link #changeNonRefundableNightDuration(int)}).

    public static final Locator NON_REFUND_MAIN_CONTENT = new Locator("RESCHEDULE_NON_REFUND_MAIN_CONTENT",
            AppiumBy.xpath("//*[@data-testid='reschedule-non-refund-main-content']"));
    public static final Locator NON_REFUND_FORM_CONTAINER = new Locator("RESCHEDULE_NON_REFUND_FORM_CONTAINER",
            AppiumBy.xpath("//*[@data-testid='reschedule-form-container']"));
    public static final Locator NON_REFUND_INFO_TITLE = new Locator("RESCHEDULE_NON_REFUND_INFO_TITLE",
            AppiumBy.xpath("//*[@data-testid='reschedule-non-refund-info-title-desktop']"));
    public static final Locator NON_REFUND_INFO_SUBTITLE = new Locator("RESCHEDULE_NON_REFUND_INFO_SUBTITLE",
            AppiumBy.xpath("//*[@data-testid='reschedule-non-refund-info-subtitle']"));
    public static final Locator NON_REFUND_SELECT_DATE_BUTTON = new Locator("RESCHEDULE_NON_REFUND_SELECT_DATE_BUTTON",
            AppiumBy.xpath("//*[@data-testid='reschedule-select-date-button']"));
    public static final Locator NON_REFUND_SELECT_DATE_TEXT = new Locator("RESCHEDULE_NON_REFUND_SELECT_DATE_TEXT",
            AppiumBy.xpath("//*[@data-testid='reschedule-select-date-button']//*[contains(@class,'RescheduleForm_select_date_text')]"));
    public static final Locator NON_REFUND_REASON_DROPDOWN_INPUT = new Locator("RESCHEDULE_NON_REFUND_REASON_DROPDOWN",
            AppiumBy.xpath("//input[@id='select-reason' or @id='pilih-alasan']"));
    // dWeb renders the reason picker as a Modal_modal_popup <section>; mWeb / WebView
    // (Android & iOS) renders it as a BottomSheet identified by data-testid="bottom-sheet-body".
    // Match both, scoped by the popup title so we don't grab unrelated dialogs.
    public static final Locator NON_REFUND_REASON_POPUP = new Locator("RESCHEDULE_NON_REFUND_REASON_POPUP",
            AppiumBy.xpath("//section[contains(@class,'Modal_modal_popup')][.//h2[contains(.,'Reschedule Reason') or contains(.,'Alasan Reschedule')]]"
                    + " | //*[@data-testid='bottom-sheet-body'][.//h2[contains(.,'Reschedule Reason') or contains(.,'Alasan Reschedule')]]"));
    public static final Locator NON_REFUND_REASON_RADIO_ITEMS = new Locator("RESCHEDULE_NON_REFUND_REASON_RADIO_ITEMS",
            AppiumBy.xpath("//label[contains(@class,'RescheduleReasonPopup_reason_radio_item')]"));
    public static final Locator NON_REFUND_DESCRIPTION_TEXTAREA = new Locator("RESCHEDULE_NON_REFUND_DESCRIPTION_TEXTAREA",
            AppiumBy.xpath("//textarea[@id='description' or @id='deskripsi']"));
    public static final Locator NON_REFUND_ATTACHMENT_FILE_INPUT = new Locator("RESCHEDULE_NON_REFUND_ATTACHMENT_FILE_INPUT",
            AppiumBy.xpath("//*[contains(@class,'FileUpload_file_uploader')]//input[@type='file']"));
    public static final Locator NON_REFUND_SUBMIT_BUTTON = new Locator("RESCHEDULE_NON_REFUND_SUBMIT_BUTTON",
            AppiumBy.xpath("//form[contains(@class,'RescheduleForm_form_wrapper')]//button[@type='submit']"));
    public static final Locator NON_REFUND_CHECK_IN_DATE_BANNER = new Locator("RESCHEDULE_NON_REFUND_CHECK_IN_DATE_BANNER",
            AppiumBy.xpath("//*[@data-testid='check-in-date-time-container']//p[contains(@class,'Text_weight_bold')]"));
    public static final Locator NON_REFUND_CHECK_OUT_DATE_BANNER = new Locator("RESCHEDULE_NON_REFUND_CHECK_OUT_DATE_BANNER",
            AppiumBy.xpath("//*[@data-testid='check-out-date-time-container']//p[contains(@class,'Text_weight_bold')]"));

    public static final Locator NON_REFUND_SAME_DATE_ERROR = new Locator("RESCHEDULE_NON_REFUND_SAME_DATE_ERROR",
            AppiumBy.xpath("//*[contains(@class,'styles_helper_text_container') and contains(@class,'styles_state_danger')]"
                    + "//*[contains(.,'must be different from the previous dates') or contains(.,'harus berbeda dengan tanggal sebelumnya')]"));
    // The "Continue to request a reschedule?" / "Lanjutkan pengajuan reschedule?" dialog
    // shares the generic Dialog_pop_up class with several other popups (refund, cancel,
    // refund-not-available, ...) and exposes no data-testid / id / aria-label, so we still
    // need the title text to disambiguate the dialog itself. Once the dialog is identified,
    // the buttons inside are uniquely addressable via Button_variant_primary /
    // Button_variant_secondary — no need to also match their text (which makes the locators
    // resilient to copy changes like "Ajukan reschedule" -> "Kirim reschedule").
    private static final String CONFIRM_DIALOG_SCOPE_XPATH =
            "//*[contains(@class,'Dialog_pop_up')][.//h2["
                    + "contains(.,'Continue to request a reschedule')"
                    + " or contains(.,'Lanjutkan pengajuan reschedule')"
                    + " or contains(.,'Lanjut ajukan reschedule')]]";
    public static final Locator NON_REFUND_CONFIRM_DIALOG = new Locator("RESCHEDULE_NON_REFUND_CONFIRM_DIALOG",
            AppiumBy.xpath(CONFIRM_DIALOG_SCOPE_XPATH));
    public static final Locator NON_REFUND_CONFIRM_REQUEST_BUTTON = new Locator("RESCHEDULE_NON_REFUND_CONFIRM_REQUEST_BUTTON",
            AppiumBy.xpath(CONFIRM_DIALOG_SCOPE_XPATH + "//button[contains(@class,'Button_variant_primary')]"));
    public static final Locator NON_REFUND_CONFIRM_CHECK_AGAIN_BUTTON = new Locator("RESCHEDULE_NON_REFUND_CONFIRM_CHECK_AGAIN_BUTTON",
            AppiumBy.xpath(CONFIRM_DIALOG_SCOPE_XPATH + "//button[contains(@class,'Button_variant_secondary')]"));

    public static final Locator NON_REFUND_STATUS_CONTAINER = new Locator("RESCHEDULE_STATUS_CONTAINER",
            AppiumBy.xpath("//*[@data-testid='reschedule-status-container']"));
    public static final Locator NON_REFUND_STATUS_LABEL = new Locator("RESCHEDULE_STATUS_LABEL",
            AppiumBy.xpath("//*[@data-testid='reschedule-status-label-text']"));
    public static final Locator NON_REFUND_STATUS_NEW_DATE = new Locator("RESCHEDULE_STATUS_NEW_DATE",
            AppiumBy.xpath("//*[@data-testid='new-check-in-out-date']//span[contains(@class,'Text_weight_bold')]"));
    public static final Locator NON_REFUND_STATUS_PROGRESS_STEPS = new Locator("RESCHEDULE_STATUS_PROGRESS_STEPS",
            AppiumBy.xpath("//*[@data-testid='reschedule-status-container']//ol[contains(@class,'ProgressStepVertical')]/li"));
    public static final Locator NON_REFUND_STATUS_PROGRESS_COMPLETED_STEPS = new Locator("RESCHEDULE_STATUS_COMPLETED_STEPS",
            AppiumBy.xpath("//*[@data-testid='reschedule-status-container']//ol[contains(@class,'ProgressStepVertical')]"
                    + "/li[.//*[@data-testid='step-circle' and contains(@class,'Step_circle_filled')]]"));
    public static final Locator NON_REFUND_STATUS_PROGRESS_ACTIVE_STEPS = new Locator("RESCHEDULE_STATUS_ACTIVE_STEPS",
            AppiumBy.xpath("//*[@data-testid='reschedule-status-container']//ol[contains(@class,'ProgressStepVertical')]"
                    + "/li[.//*[@data-testid='step-circle' and contains(@class,'Step_circle_active')]]"));
    // ---------------------------------------------------------------------------------------
    // Date helpers — shared between dWeb and mWeb implementations of
    // {@link #changeNonRefundableNightDuration(int)}.
    // ---------------------------------------------------------------------------------------

    /**
     * Format accepted by {@link com.tiket.web.page.common.calendar.CalendarPageWeb#selectDate}:
     * {@code "<day> <englishMonth> <year>"}, e.g. {@code "7 June 2026"}. The calendar's
     * day-button {@code aria-label} can be in either English or Bahasa; the calendar handles
     * both via {@link CoreFunctions#monthEngToId(String)}, but its lookup table only contains
     * <em>full</em> English month names, so the short form ({@code "MMM"}) must NOT be used.
     */
    protected static final DateTimeFormatter CALENDAR_DATE_FORMAT =
            DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

    // Banner shapes we've seen on prod/preprod (English & Bahasa, with or without weekday
    // prefix), e.g. "7 Sep 2026", "Mon, 7 Sep 2026", "Sen, 7 Sep 2026", "7 September 2026",
    // "Fri, 05 June 2026". We don't anchor at start/end since some banners append a time.
    private static final Pattern BANNER_DATE_PATTERN =
            Pattern.compile("(\\d{1,2})\\s+([A-Za-z]+)\\s+(\\d{4})");

    /**
     * Parse a check-in / check-out banner string into a {@link LocalDate}. Accepts either
     * English or Bahasa month names (full or short, e.g. {@code "Jun"} or {@code "June"} or
     * {@code "Juni"}), with an optional weekday prefix.
     *
     * @throws IllegalStateException when {@code bannerText} is empty, doesn't contain a
     *                               recognisable {@code <day> <month> <year>} triple, or the
     *                               month token is unknown.
     */
    protected LocalDate parseBannerDate(String bannerText) {
        if (bannerText == null || bannerText.isBlank()) {
            throw new IllegalStateException(
                    "Cannot derive new reschedule dates: banner text is empty");
        }
        Matcher m = BANNER_DATE_PATTERN.matcher(bannerText);
        if (!m.find()) {
            throw new IllegalStateException(
                    "Unrecognised banner date format: '" + bannerText + "'");
        }
        int day = Integer.parseInt(m.group(1));
        int year = Integer.parseInt(m.group(3));
        int month;
        try {
            // CoreFunctions.getMonthNumber handles Bahasa long/short, English short, and
            // English long (via the monthEngToId fallback). It NPEs on a totally unknown
            // token, hence the wrap.
            month = CoreFunctions.getMonthNumber(m.group(2));
        } catch (NullPointerException npe) {
            throw new IllegalStateException(
                    "Unknown month name in banner: '" + m.group(2)
                            + "' (full text: '" + bannerText + "')",
                    npe);
        }
        return LocalDate.of(year, month, day);
    }

    private Locator nonRefundReasonRadioByLabel(String reasonText) {
        return new Locator("RESCHEDULE_NON_REFUND_REASON_RADIO_" + reasonText,
                AppiumBy.xpath("//label[contains(@class,'RescheduleReasonPopup_reason_radio_item')][.//span[normalize-space()='"
                        + reasonText + "']]//input[@name='reason']"));
    }

    public boolean isOnNonRefundablePage() {
        return driverTools.waitForElementToAppear(NON_REFUND_MAIN_CONTENT, 30).isPresent()
                && driverTools.waitForElementToAppear(NON_REFUND_FORM_CONTAINER, 30).isPresent();
    }

    /**
     * @return text inside {@code RescheduleForm_select_date_text}, trimmed
     *         (e.g. {@code "27 May - 28 May"} / {@code "27 Mei - 28 Mei"}).
     */
    public String getNonRefundableSelectedDateText() {
        WebElement element = driverTools.waitForElementToAppear(NON_REFUND_SELECT_DATE_TEXT, 30).orElseThrow();
        return driverTools.getText(element).trim();
    }

    public String getNonRefundableCheckInDateBannerText() {
        return driverTools.waitForElementToAppear(NON_REFUND_CHECK_IN_DATE_BANNER, 15)
                .map(driverTools::getText)
                .map(String::trim)
                .orElse("");
    }

    public String getNonRefundableCheckOutDateBannerText() {
        return driverTools.waitForElementToAppear(NON_REFUND_CHECK_OUT_DATE_BANNER, 15)
                .map(driverTools::getText)
                .map(String::trim)
                .orElse("");
    }

    public void selectNonRefundableReason(String reasonText) {
        openNonRefundableReasonPopup();
        Locator radio = nonRefundReasonRadioByLabel(reasonText);
        WebElement option = driverTools.waitForElementToPresence(radio, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(option);
        driverTools.waitForElementToDisappear(NON_REFUND_REASON_POPUP, 10);
    }

    /**
     * Opens the "Reschedule Reason" popup and picks one of its radio options uniformly at random.
     *
     * @return the visible label of the option that was clicked (e.g. "Change of travel plans").
     * @throws IllegalStateException if the popup contains no options.
     */
    public String selectRandomNonRefundableReason() {
        openNonRefundableReasonPopup();

        List<WebElement> items = driverTools.elements(NON_REFUND_REASON_RADIO_ITEMS, 15)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new IllegalStateException("No reschedule reason radio items found"));

        WebElement chosen = items.get(ThreadLocalRandom.current().nextInt(items.size()));
        String label = driverTools.getText(chosen).trim();

        driverTools.scrollIntoWebView(chosen);
        driverTools.clickWithJavascriptExecutor(chosen);
        driverTools.waitForElementToDisappear(NON_REFUND_REASON_POPUP, 10);
        return label;
    }

    private void openNonRefundableReasonPopup() {
        WebElement reasonField = driverTools.waitForElementToAppear(NON_REFUND_REASON_DROPDOWN_INPUT, 15).orElseThrow();
        driverTools.scrollIntoWebView(reasonField);
        driverTools.clickWithJavascriptExecutor(reasonField);
        driverTools.waitForElementToAppear(NON_REFUND_REASON_POPUP, 15).orElseThrow();
    }

    public void fillNonRefundableDescription(String description) {
        WebElement textarea = driverTools.waitForElementToAppear(NON_REFUND_DESCRIPTION_TEXTAREA, 15).orElseThrow();
        driverTools.scrollIntoWebView(textarea);
        driverTools.sendKeys(NON_REFUND_DESCRIPTION_TEXTAREA, description);
    }

    /**
     * Attaches a local file to the (optional) evidence input. The visible "Upload files" button
     * delegates to a hidden {@code <input type="file">} — we send the absolute path directly
     * to that input element.
     *
     * @param filePathRelativeToProject path relative to the project root,
     *                                  e.g. {@code "src/test/resources/dashboard/hotel/sampleImage.jpg"}.
     */
    public void attachNonRefundableEvidence(String filePathRelativeToProject) {
        File file = new File(filePathRelativeToProject);
        if (!file.exists()) {
            throw new IllegalArgumentException("Attachment file not found: " + file.getAbsolutePath());
        }
        driverTools.waitForElementToPresence(NON_REFUND_ATTACHMENT_FILE_INPUT, 15).orElseThrow();
        driverTools.sendKeys(NON_REFUND_ATTACHMENT_FILE_INPUT, file.getAbsolutePath());
    }

    public void clickNonRefundableSubmit() {
        WebElement submit = driverTools.waitForElementToAppear(NON_REFUND_SUBMIT_BUTTON, 15).orElseThrow();
        driverTools.scrollIntoWebView(submit);
        driverTools.clickWithJavascriptExecutor(submit);
    }

    /**
     * @return {@code true} when the inline danger helper text "The new dates must be different
     * from the previous dates" / "Tanggal baru harus berbeda dengan tanggal sebelumnya" is
     * displayed (i.e. submission was rejected because no date change was made).
     */
    public boolean isSameDateErrorVisible() {
        return driverTools.waitForElementToAppear(NON_REFUND_SAME_DATE_ERROR, 10).isPresent();
    }

    /**
     * Confirms the "Continue to request a reschedule?" dialog by clicking its primary
     * "Request reschedule" / "Ajukan reschedule" button. Waits for the dialog to close AND
     * for the form container to be replaced by the approval-status container, so callers
     * can immediately query approval-flow state without racing the page transition.
     */
    public void confirmRescheduleRequest() {
        driverTools.waitForElementToAppear(NON_REFUND_CONFIRM_DIALOG, 15).orElseThrow();
        WebElement confirmButton = driverTools.waitForElementToAppear(NON_REFUND_CONFIRM_REQUEST_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(confirmButton);
        driverTools.waitForElementToDisappear(NON_REFUND_CONFIRM_DIALOG, 15);
        driverTools.waitForElementToAppear(NON_REFUND_STATUS_CONTAINER, 30).orElseThrow();
    }

    public boolean isApprovalFlowStatusVisible() {
        return driverTools.waitForElementToAppear(NON_REFUND_STATUS_CONTAINER, 30).isPresent();
    }

    /**
     * @return text of the new check-in / check-out range rendered on the reschedule-status
     * card, e.g. {@code "12 Jul 2026 - 13 Jul 2026"}.
     */
    public String getApprovalNewDateText() {
        return driverTools.waitForElementToAppear(NON_REFUND_STATUS_NEW_DATE, 30)
                .map(driverTools::getText)
                .map(String::trim)
                .orElse("");
    }

    public String getApprovalStatusLabelText() {
        return driverTools.waitForElementToAppear(NON_REFUND_STATUS_LABEL, 30)
                .map(driverTools::getText)
                .map(String::trim)
                .orElse("");
    }

    public int getApprovalTotalStepCount() {
        return driverTools.elements(NON_REFUND_STATUS_PROGRESS_STEPS).map(List::size).orElse(0);
    }

    public int getApprovalCompletedStepCount() {
        return driverTools.elements(NON_REFUND_STATUS_PROGRESS_COMPLETED_STEPS).map(List::size).orElse(0);
    }

    public int getApprovalActiveStepCount() {
        return driverTools.elements(NON_REFUND_STATUS_PROGRESS_ACTIVE_STEPS).map(List::size).orElse(0);
    }

    /**
     * Opens the calendar from the non-refundable {@code reschedule-select-date-button} and
     * shifts the booking dates by {@code addDays}, preserving the original night count:
     * <ul>
     *   <li>new check-in  = current check-in  + {@code addDays} days</li>
     *   <li>new check-out = new check-in     + (current check-out - current check-in)</li>
     * </ul>
     * Both dWeb ({@code CalendarDesktop_*} single-tap overlay) and mWeb ({@code CalendarMobile_*}
     * single-tap bottom-sheet) implementations conform to this contract — the app
     * auto-derives the new check-out from the original night count after one check-in pick.
     *
     * @param addDays days to shift the check-in; must be {@code > 0} to actually change
     *                the dates. {@code <= 0} is a no-op and returns the current date text
     *                without opening the picker.
     * @return the date-range text displayed in {@code RescheduleForm_select_date_text}
     *         after the selection (e.g. {@code "07 Jun - 08 Jun"}).
     */
    public abstract String changeNonRefundableNightDuration(int addDays);
}
