package com.tiket.web.page.ttd.TtdBookingFormPage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.stream.Collectors;

import static com.tiket.core.CoreFunctions.*;
import static org.hamcrest.Matchers.is;

public abstract class TtdBookingFormPageWeb<T extends WebDriver> extends WebPage<T> {

    public TtdBookingFormPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TtdBookingFormPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new TtdBookingFormPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new TtdBookingFormPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator FINAL_PRICE = new Locator("FINAL_PRICE", AppiumBy.xpath("(//*[contains(@class,'PriceModule_final_price_wrapper')]//span)[last()]"));
    private static final Locator PRICE_DETAIL_BUTTON = new Locator("PRICE_DETAIL_BUTTON", new By.ByXPath("(//*[contains(@class,'PriceModule_container')]//following-sibling::*)[1]"));
    private static final Locator DISCOUNT_PRICE = new Locator("DISCOUNT_PRICE", new By.ByXPath("//*[contains(@class,'PaymentDetail_discount')]//span[contains(@class,'Text_variant_positive')]"));
    private static final Locator WARNING_MESSAGE = new Locator("WARNING_MESSAGE", new By.ByXPath("//*[contains(@class,'FormWrapper_desktop_card_body')]//*[contains(@class,'Text_variant_alert') or contains(@class,'state_danger')]"));
    private static final Locator VISITOR_TOGGLE_BTN = new Locator("VISITOR_TOGGLE_BTN", new By.ByXPath("(//*[contains(@class, 'toggle')])[last()]"));
    private static final Locator GV_TITLE_SECTION = new Locator("GV_TITLE_SECTION", new By.ByXPath("//*[contains(@class,'GiftVoucherClaimWidget_title')]"));
    private static final Locator GV_TITLE = new Locator("GV_TITLE", new By.ByXPath("//div[contains(@class,'InfinityPromoBannerSelection_banner_wrapper')]"));
    private static final Locator GV_CLAIM = new Locator("GV_CLAIM", new By.ByXPath("//button[contains(@class,'InfinityPromoCard_apply_btn')]"));
    private static final Locator GV_CLAIM_BUTTON = new Locator("GV_CLAIM_BUTTON", new By.ByXPath("//*[contains(@class,'GiftVoucherClaimWidget_desktop')]//button[contains(@class,'GiftVoucherCard_claim_button')]"));
    private static final Locator SUBMIT_SELECTED_SEAT = new Locator("SUBMIT_SELECTED_SEAT", new By.ByXPath("//button[contains(@class,'Button_variant_primary')]//span[contains(text(),'payment') or contains(text(),'pembayaran')]"));
    private static final Locator REFUND_INFO = new Locator("REFUND_INFO", new By.ByXPath("//*[contains(@class,'WhatYouWillGet')]//*[contains(text(),'100% refund') or contains(text(),'100% Refund')]"));
    private static final Locator ADD_BUNDLE_SECTION = new Locator("ADD_BUNDLE_SECTION", AppiumBy.xpath("//*[contains(@class,'BundleBFCard_btn_claim_add')]"));
    private static final Locator ADD_BUNDLE_PACK = new Locator("ADD_BUNDLE_PACK", new By.ByXPath("//*[contains(@class,'BundleTotalPayment_totalpayment_wrapper')]//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator HOTEL_NAME_ADDRESS = new Locator("HOTEL_NAME_ADDRESS", new By.ByXPath("//*[@id='hotel--address']"));
    private static final Locator PICKUP_TIME = new Locator("PICKUP_TIME", new By.ByXPath("//*[@id='pick-up-time']"));
    private static final Locator PICKUP_TIME_RADIO_BUTTON = new Locator("PICKUP_TIME_RADIO_BUTTON", new By.ByXPath("//*[contains(@class,'SingleSelection_wrapper')]//*[contains(@class,'Radio_checkbox')]"));
    private static final Locator FREE_LUNCH_BUTTON = new Locator("FREE_LUNCH_BUTTON", new By.ByXPath("//*[contains(@class,'FormWrapper_desktop_card_body')]//*[contains(@class,'Radio_checkbox')]"));
    private static final Locator PRICE_CHANGE = new Locator("PRICE_CHANGE", new By.ByXPath("//div[contains(@class, 'Figure_footer')]//button"));
    private static final Locator ADD_TO_BUNDLE_PACK_BTN = new Locator("ADD_TO_BUNDLE_PACK_BTN", AppiumBy.xpath("//button[@type='button' and contains(@class,'Button_variant_primary')]//span[contains(text(),'Add to Bundle Pack') or contains(text(),'Tambah ke Bundle Pack')]"));
    private static final Locator TIMESLOT_CHIPS = new Locator("TIMESLOT_CHIPS", AppiumBy.xpath("(//*[contains(@class,'BundleTimeSlot_time_wrapper')]//*[contains(@class,'Chip_text')])[1]"));
    private static final Locator BUNDLE_DELETE_BTN = new Locator("BUNDLE_DELETE_BTN", AppiumBy.xpath("//*[contains(@class,'BundleBFCard_btn_delete')]"));
    private static final Locator ADDED_BUNDLING_ITEM_TITLE = new Locator("ADDED_BUNDLING_ITEM_TITLE", new By.ByXPath("(//*[contains(@class,'BundleBFCard_package_name')])[1]"));
    private static final Locator VOUCHER_APPLIED_MODAL = new Locator("VOUCHER_APPLIED_MODAL", new By.ByXPath("//div[contains(@class, 'Snackbar_text_content_') and (contains(text(), 'Nice! Vouchers will go to your Voucher Box after payment of this transaction.') or contains(text(), 'Mantul! Voucher akan masuk ke Voucher Box-mu setelah pembayaran transaksi ini.'))]"));
    private static final Locator VOUCHER_CLAIMED = new Locator("VOUCHER_CLAIMED", new By.ByXPath("//button[contains(@class, 'GiftVoucherCard_claim_button') and contains(@class, 'Button_state_disabled_')]"));
    private static final Locator VOUCHER_CLAIM_BTN = new Locator("VOUCHER_CLAIM_BTN", new By.ByXPath("(//button[contains(@class,'GiftVoucherCard_claim_button')])[1]"));
    private static final Locator VALIDITY_TEXT = new Locator("VALIDITY_TEXT", new By.ByXPath("//div[contains(@class,'OrderDetail_flexi')]//b"));
    private static final Locator ORDER_DATE = new Locator("ORDER_DATE", new By.ByXPath("//div[contains(@class,'OrderDetail_schedule_date_')]//div[contains(@class,'OrderDetail_schedule_value')]"));
    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", new By.ByXPath("//input[@id='hotel-name']"));
    private static final Locator HOTEL_ADDRESS = new Locator("HOTEL_ADDRESS", new By.ByXPath("//textarea[@id='hotel-address']"));
    private static final Locator ORIGINAL_PRICE = new Locator("ORIGINAL_PRICE", new By.ByXPath("(//span[contains(@class, 'PaymentDetail_description')])[2]"));
    private static final Locator PAY_WITH = new Locator("PAY_WITH", new By.ByXPath("//span[contains(@class,'SubmitPayment_pay_now_text')]"));
    private static final Locator SEE_ORDER_LIST = new Locator("SEE_ORDER_LIST", new By.ByXPath("//button[contains(@class,'SuccessBanner_orderlist_redirect_button')]"));
    private static final Locator QUANTITY_ERROR_DIALOG = new Locator("QUANTITY_ERROR_DIALOG", new By.ByXPath(
            "//div[contains(@class,'Dialog_pop_up')][.//div[contains(@class,'Dialog_body')]//h2[contains(@class,'Figure_title')]]"));
    private static final Locator QUANTITY_ERROR_TITLE = new Locator("QUANTITY_ERROR_TITLE", new By.ByXPath(
            "//div[contains(@class,'Dialog_pop_up')]//div[contains(@class,'Dialog_body')]//h2[contains(@class,'Figure_title')]"));
    private static final Locator QUANTITY_ERROR_DESCRIPTION = new Locator("QUANTITY_ERROR_DESCRIPTION", new By.ByXPath(
            "//div[contains(@class,'Dialog_pop_up')]//div[contains(@class,'Dialog_body')]//div[contains(@class,'Figure_description')]"));
    private static final Locator CHANGE_QUANTITY = new Locator("CHANGE_QUANTITY", new By.ByXPath(
            "//div[contains(@class,'Dialog_pop_up')]//button[contains(@class,'DialogOrderError_btn_full_width')]"
                    + "[.//span[normalize-space()='Ubah jumlah tiket' or normalize-space()='Change ticket quantity'"
                    + " or contains(normalize-space(),'Change') or contains(normalize-space(),'Ubah')]]"));
    private static final Locator PAYMENT_RADIO_BUTTON = new Locator("PAYMENT_RADIO_BUTTON", new By.ByXPath("//div[@class='PaymentTile_selected_payment_checkbox__zw1vv']//input[@type='radio']"));
    private static final Locator LPG_LABEL_BOOKING_PAGE = new Locator("LPG_LABEL_BOOKING_PAGE", new By.ByXPath("//span[contains(@class, 'Text_text') and contains(@class, 'Text_variant_invert')]//span[contains(@class, 'Text_weight_bold')]"));
    protected static final Locator SALUTATIONS = new Locator("SALUTATIONS", By.xpath("//input[@name='contactDetails.salutation']/following-sibling::span"));
    private static final Locator VISITOR_DETAILS = new Locator("VISITOR_DETAILS", new By.ByXPath("//*[contains(@class,'FormWrapper_desktop_card_header_')]"));
    private static final Locator QUESTIONNAIRE_CHECKBOX_YES = new Locator("QUESTIONNAIRE_CHECKBOX_YES",  new By.ByXPath("//span[contains(@class, 'Radio_children_') and (contains(text(), 'Ya') or contains(text(), 'Yes'))]"));

    public String getFinalPrice() {
        WebElement finalPriceElement = driverTools.waitForElementToAppear(FINAL_PRICE).orElseThrow();
        String finalPrice = driverTools.getText(finalPriceElement);
        logger.log("Final price: "+ finalPrice);
        return finalPrice;
    }

    public String getOriginalPrice() {
        driverTools.click(PRICE_DETAIL_BUTTON);
        WebElement originalPrice = driverTools.waitForElementToAppear(ORIGINAL_PRICE).orElseThrow();
        String price = driverTools.getText(originalPrice);
        logger.log("Original price-> "+ price);
        driverTools.click(TtdLandingPageWeb.CLOSE_POPUP);
        return price;
    }

    public int checkPriceBreakdown(){
        int originalPrice = convertPriceToInt(getOriginalPrice());
        int discountPrice = convertPriceToInt(getDiscountPrice());
        return (originalPrice - discountPrice);
    }

    public static int convertPriceToInt(String priceString) {
        String digitsOnly = priceString.replaceAll("[^\\d]", "");
        return Integer.parseInt(digitsOnly);
    }


    public boolean isRefundInfoDisplayed() {
        return driverTools.waitForElementsToAppear(REFUND_INFO).isPresent();
    }

    public void selectCountry(String country) {
        if (driverTools.element(COUNTRY_SELECTOR, 5).isPresent()) {
            driverTools.scrollElementToCenter(COUNTRY_SELECTOR);
            driverTools.click(COUNTRY_SELECTOR);
            driverTools.sendKeys(COUNTRY_SELECTOR_SEARCH_BAR, country);
            driverTools.scrollElementToCenter(COUNTRY_SELECTOR_FIRST_RESULT);
            driverTools.click(COUNTRY_SELECTOR_FIRST_RESULT);
            return;
        }
        if (driverTools.waitForElementToPresence(NATIONALITY_DROPDOWN, 5).isPresent()) {
            driverTools.scrollIntoWebView(NATIONALITY_DROPDOWN);
            driverTools.clickElementByCoordinateDWeb(NATIONALITY_DROPDOWN);
            driverTools.clickWithJavascriptExecutor(INDONESIA);
        }
    }

    public String getGVTitleSection() {
        driverTools.scrollIntoWebView(GV_TITLE_SECTION);
        return driverTools.getText(GV_TITLE_SECTION)
                .orElseThrow()
                .replaceAll("[^a-zA-Z\s]", "")
                .trim();
    }

    public void clickClaimGVButton() {
        driverTools.scrollIntoWebView(GV_CLAIM_BUTTON);
        driverTools.clickWithJavascriptExecutor(GV_CLAIM_BUTTON);
    }

    public boolean isLPGLabelPresentOnBookingPage() {
        driverTools.waitForElementsToAppear(LPG_LABEL_BOOKING_PAGE);
        return driverTools.waitForElementsToAppear(LPG_LABEL_BOOKING_PAGE).isPresent();
    }

    public void clickPayWithButton(){
        driverTools.scrollIntoWebView(PAYMENT_RADIO_BUTTON);
        driverTools.clickWithJavascriptExecutor(PAYMENT_RADIO_BUTTON);
        driverTools.scrollIntoWebView(PAY_WITH);
        driverTools.clickWithJavascriptExecutor(PAY_WITH);
    }

    public boolean isQuantityErrorMessageDisplayed(){
        return driverTools.waitForElementToAppear(QUANTITY_ERROR_DIALOG, 10).isPresent()
                || driverTools.waitForElementToPresence(QUANTITY_ERROR_TITLE).isPresent();
    }

    public String getQuantityErrorMessage() {
        WebElement errorTitle = driverTools.waitForElementToAppear(QUANTITY_ERROR_TITLE).orElseThrow();
        return driverTools.getText(errorTitle);
    }

    public String getQuantityErrorDescription() {
        WebElement errorDescription = driverTools.waitForElementToAppear(QUANTITY_ERROR_DESCRIPTION).orElseThrow();
        return driverTools.getText(errorDescription);
    }

    public void clickChangeQuantityButton(){
        driverTools.waitForElementToAppear(CHANGE_QUANTITY);
        driverTools.clickWithJavascriptExecutor(CHANGE_QUANTITY);
    }

    public void clickSeeOrderList(){
        driverTools.clickWithJavascriptExecutor(SEE_ORDER_LIST);
    }


    public By generateTextLocator(String text) {
        return By.xpath("//p[contains(@class,'TiketCard_text_black') and text()='" + text + "']");
    }

    /**
     * Selects an available seat from the seat-map and submits.
     *
     * <p>Hardened for the virtualized seat-map container (overflow:auto with absolutely-positioned
     * children) and a long tail of edge cases observed in production:
     * <ul>
     *   <li><b>Input validation</b> — null/blank/non-numeric/negative {@code number} fail fast with clear messages.</li>
     *   <li><b>Pre-flight popup dismissal</b> — best-effort dismissal of {@code PRICE_CHANGE} popup so it cannot intercept the click.</li>
     *   <li><b>Stale-element resilience</b> — chosen seat is re-resolved via indexed XPath right before each click rather than caching a {@link WebElement}. The underlying {@code click(Locator)} also retries once on stale.</li>
     *   <li><b>Click-intercepted resilience</b> — native click first, then JS click fallback when Chrome rejects the native click (tooltip wrapper, transient overlay, etc.).</li>
     *   <li><b>State-change confirmation</b> — the click is considered successful only after {@code SELECTED_SEAT} renders with non-blank text.</li>
     *   <li><b>Bounded retries</b> — {@code "random"} retries up to 3 attempts on a different index; a fixed numeric index gets 2 attempts (handles transient stale failures, never silently picks a different seat).</li>
     *   <li><b>Submit hardening</b> — scroll-into-view + native click + JS click fallback for the submit button.</li>
     * </ul>
     *
     * @param number {@code "random"} or a stringified zero-based index into the rendered list of available seats
     * @throws IllegalArgumentException if {@code number} is null/blank or not parseable
     * @throws IndexOutOfBoundsException if a numeric {@code number} is negative or beyond the rendered seat count
     * @throws NoSuchElementException if the seat-map never renders, has 0 available seats, or no attempt succeeded
     */
    public String selectSeat(String number) {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("selectSeat: 'number' must be \"random\" or a numeric string, got: " + number);
        }
        String normalized = number.trim();
        boolean isRandom = "random".equalsIgnoreCase(normalized);
        int requestedIndex = -1;
        if (!isRandom) {
            try {
                requestedIndex = Integer.parseInt(normalized);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("selectSeat: 'number' must be \"random\" or a numeric string, got: " + number, e);
            }
            if (requestedIndex < 0) {
                throw new IndexOutOfBoundsException("Seat index must be >= 0, got: " + requestedIndex);
            }
        }

        // Best-effort: dismiss the price-change popup if it's covering the seat-map.
        // We only suppress the failure for the dismissal itself; the seat click below has its own
        // intercepted-click handling, so a stuck popup will surface there with a clear message.
        if (driverTools.waitForElementToPresence(PRICE_CHANGE, 3).isPresent()) {
            logger.log("Dismissing price-change popup before seat selection");
            try {
                driverTools.clickWithJavascriptExecutor(PRICE_CHANGE);
            } catch (Exception ignore) {
                logger.softWarning("Failed to dismiss price-change popup; continuing");
            }
        }

        final String seatItemXpath = "//*[contains(@class,'SeatItem_available')]";
        final int maxAttempts = isRandom ? 3 : 2;
        Set<Integer> triedIndexes = new HashSet<>();
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            if (driverTools.waitForElementToAppear(SEAT_BUTTON, 15).isEmpty()) {
                throw new NoSuchElementException("Seat-map did not render: no elements matching " + seatItemXpath);
            }

            int availableCount = driverTools.elements(SEAT_BUTTON).map(List::size).orElse(0);
            if (availableCount == 0) {
                throw new NoSuchElementException("No available seats rendered in the seat-map");
            }

            int seatIndex;
            if (isRandom) {
                int pick;
                int guard = 0;
                do {
                    pick = generateRandomInt(0, availableCount - 1);
                } while (triedIndexes.contains(pick) && guard++ < availableCount * 2);
                seatIndex = pick;
            } else {
                if (requestedIndex >= availableCount) {
                    throw new IndexOutOfBoundsException(
                            "Seat index " + requestedIndex + " out of bounds; rendered available seats: " + availableCount);
                }
                seatIndex = requestedIndex;
            }
            triedIndexes.add(seatIndex);
            logger.log(String.format("Selecting seat: index=%d, attempt=%d/%d, rendered=%d",
                    seatIndex, attempt, maxAttempts, availableCount));

            // Indexed XPath is 1-based.
            Locator indexedSeat = new Locator("SEAT_BUTTON[" + seatIndex + "]",
                    new By.ByXPath("(" + seatItemXpath + ")[" + (seatIndex + 1) + "]"));

            try {
                try {
                    driverTools.scrollIntoWebView(indexedSeat);
                } catch (Exception scrollEx) {
                    logger.softWarning("scrollIntoWebView failed for seat index " + seatIndex + ": " + scrollEx.getMessage());
                }

                // Best-effort clickable wait. We do not bail on timeout here — the JS click fallback
                // below can still succeed when a non-modal layer keeps Chrome from declaring the
                // element "clickable" (e.g. seat sits under a tooltip wrapper that has pointer-events).
                driverTools.waitForElementToBeClickable(indexedSeat, 10);

                boolean clicked = false;
                try {
                    driverTools.click(indexedSeat);
                    clicked = true;
                } catch (Exception nativeClickEx) {
                    logger.softWarning("Native click on seat index " + seatIndex + " failed ("
                            + nativeClickEx.getClass().getSimpleName() + ": " + nativeClickEx.getMessage()
                            + "); falling back to JS click");
                    try {
                        driverTools.clickWithJavascriptExecutor(indexedSeat);
                        clicked = true;
                    } catch (Exception jsClickEx) {
                        logger.softWarning("JS click also failed for seat index " + seatIndex + ": " + jsClickEx.getMessage());
                        lastException = jsClickEx;
                    }
                }
                if (!clicked) {
                    continue;
                }

                Optional<WebElement> selectedSeatEl = driverTools.waitForElementToAppear(SELECTED_SEAT, 10);
                if (selectedSeatEl.isEmpty()) {
                    logger.softWarning("SELECTED_SEAT did not render after clicking seat index " + seatIndex
                            + (attempt < maxAttempts ? "; retrying" : "; giving up"));
                    continue;
                }

                String seatNumber = driverTools.getText(selectedSeatEl.get());
                if (seatNumber == null || seatNumber.isBlank()) {
                    logger.softWarning("SELECTED_SEAT panel rendered but text is blank for seat index " + seatIndex
                            + (attempt < maxAttempts ? "; retrying" : "; giving up"));
                    continue;
                }
                logger.log("Selected seat number: " + seatNumber);

                clickSubmitSelectedSeat();
                return seatNumber;
            } catch (Exception attemptEx) {
                logger.softWarning("Seat selection attempt " + attempt + " threw "
                        + attemptEx.getClass().getSimpleName() + ": " + attemptEx.getMessage());
                lastException = attemptEx;
            }
        }

        String tailMsg = lastException != null
                ? "; last error: " + lastException.getClass().getSimpleName() + ": " + lastException.getMessage()
                : "";
        throw new NoSuchElementException("Could not select a seat after " + maxAttempts + " attempt(s)" + tailMsg);
    }

    private void clickSubmitSelectedSeat() {
        try {
            driverTools.scrollIntoWebView(SUBMIT_SELECTED_SEAT);
        } catch (Exception ignore) {
            // best-effort scroll
        }
        driverTools.waitForElementToBeClickable(SUBMIT_SELECTED_SEAT, 10);
        try {
            driverTools.click(SUBMIT_SELECTED_SEAT);
        } catch (Exception submitEx) {
            logger.softWarning("Native click on SUBMIT_SELECTED_SEAT failed ("
                    + submitEx.getClass().getSimpleName() + ": " + submitEx.getMessage()
                    + "); falling back to JS click");
            driverTools.clickWithJavascriptExecutor(SUBMIT_SELECTED_SEAT);
        }
    }

    public String selectSeatIfAvailable(String number) {
        if(driverTools.waitForElementToPresence(SEAT_BUTTON).isPresent()){
            return selectSeat(number);
        }
        return "";
    }

    public boolean isSelectSeatPageDisplayed() {
        return driverTools.waitForElementToAppear(SEAT_BUTTON, 15).isPresent();
    }

    public void addBundleProduct(int bundleQty, int bundleIndex) {
        driverTools.click(ADD_BUNDLE_SECTION);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        ttdProductDetailPageWeb.setTicketQty(bundleQty, bundleIndex);
        driverTools.click(ADD_BUNDLE_PACK);
    }

    public void addBundleProduct(int bundleQty, int bundleIndex, int visitDateIndex) {
        driverTools.click(ADD_BUNDLE_SECTION);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        ttdProductDetailPageWeb.chooseVisitDate(visitDateIndex);
        ttdProductDetailPageWeb.setTicketQty(bundleQty, bundleIndex);
        driverTools.click(ADD_BUNDLE_PACK);
    }

    public void addBundleProduct(int bundleQty, int bundleIndex, String visitDateText) {
        driverTools.click(ADD_BUNDLE_SECTION);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        ttdProductDetailPageWeb.chooseVisitDate(visitDateText);
        ttdProductDetailPageWeb.setTicketQty(bundleQty, bundleIndex);
        driverTools.click(ADD_BUNDLE_PACK);
    }

    public boolean isBundleProductAdded() throws InterruptedException {
        driverTools.waitForElementToAppear(ADD_BUNDLE_SECTION);
        return !driverTools.isElementDisplayed(ADD_BUNDLE_SECTION);
    }

    public void fillGuestDetailSupplier(String supplierName) {
        switch (supplierName.toLowerCase()) {
            case "klook":
                fillGuestDetailKlook();
                break;
            case "bmg":
                fillGuestBmg();
                break;
            case "globaltix":
                fillGuestDetailGlobalTix();
                break;
            case "jtr":
                fillGuestDetailJtr();
                break;
            default:
                logger.log(supplierName + ": as supplier name not found");
        }
    }

    @SneakyThrows
    private void fillGuestDetailKlook() {
        driverTools.waitForElementToAppear(HOTEL_NAME_ADDRESS);
        driverTools.scrollIntoWebView(HOTEL_NAME_ADDRESS);
        List<WebElement> hotelNameAddress = driverTools.elements(HOTEL_NAME_ADDRESS).orElseThrow();
        logger.log("Hotel name address total: " + hotelNameAddress.size());
        for (WebElement element : hotelNameAddress) {
            driverTools.sendKeys(element, generateRandomString(10) + " TTD Automation Testing Data");
        }
        driverTools.scrollIntoWebView(PICKUP_TIME);
        List<WebElement> pickupTime = driverTools.elements(PICKUP_TIME).orElseThrow();
        logger.log("Pickup time total: " + pickupTime.size());
        for (WebElement element : pickupTime) {
            driverTools.scrollIntoWebView(element);
            driverTools.clickWithJavascriptExecutor(element);
            driverTools.waitForElementsToAppear(PICKUP_TIME_RADIO_BUTTON);
            driverTools.scrollIntoWebView(PICKUP_TIME_RADIO_BUTTON);
            driverTools.waitForElementsToAppear(PICKUP_TIME_RADIO_BUTTON);
            driverTools.click(PICKUP_TIME_RADIO_BUTTON);
            if (driverTools.isElementDisplayed(PICKUP_TIME_RADIO_BUTTON)) driverTools.click(PICKUP_TIME_RADIO_BUTTON);

        }
    }


    private void fillGuestBmg() {
        if (isPresent(HOTEL_NAME)) {
            driverTools.scrollIntoWebView(HOTEL_NAME);

            List<WebElement> hotelName = driverTools.elements(HOTEL_NAME).orElseThrow();
            logger.log("Hotel name total: " + hotelName.size());
            for (WebElement element : hotelName) {
                driverTools.sendKeys(element, generateRandomString(10) + " TTD Automation Testing Data");
            }

            driverTools.scrollIntoWebView(HOTEL_ADDRESS);
            List<WebElement> hotelAddress = driverTools.elements(HOTEL_ADDRESS).orElseThrow();
            logger.log("Hotel address total: " + hotelAddress.size());
            for (WebElement element : hotelAddress) {
                driverTools.sendKeys(element, generateRandomString(11) + " TTD Automation");
            }

        } else if (driverTools.elements(VISITOR_DETAILS).isPresent()) {
            List<WebElement> visitorDetails = driverTools.elements(VISITOR_DETAILS).orElseThrow();
            int quantity = visitorDetails.size();
            logger.log("Total visitor details found: " + quantity);

            for (int i = 0; i < quantity; i++) {
                WebElement visitor = visitorDetails.get(i);
                driverTools.scrollIntoWebView(visitor);
                driverTools.waitForElementToAppear(visitor, 5).orElseThrow();
                driverTools.clickWithJavascriptExecutor(visitor);

                Optional<List<WebElement>> checkBoxYes = driverTools.elements(QUESTIONNAIRE_CHECKBOX_YES);
                driverTools.clickWithJavascriptExecutor(checkBoxYes.get().get(i));
            }
        }
    }

    @SneakyThrows
    private void fillGuestDetailGlobalTix() {
        logger.log("Filling guest details for GLOBALTIX supplier");

        // Handle multiple visitor details if present
        if (driverTools.elements(VISITOR_DETAILS).isPresent()) {
            List<WebElement> visitorDetails = driverTools.elements(VISITOR_DETAILS).orElseThrow();
            int quantity = visitorDetails.size();
            logger.log("Total visitor details found for GLOBALTIX: " + quantity);

            for (int i = 0; i < quantity; i++) {
                WebElement visitor = visitorDetails.get(i);
                driverTools.scrollIntoWebView(visitor);
                driverTools.waitForElementToAppear(visitor, 5).orElseThrow();
                driverTools.clickWithJavascriptExecutor(visitor);

                // Fill additional details if required by GLOBALTIX
                if (driverTools.elements(QUESTIONNAIRE_CHECKBOX_YES).isPresent()) {
                    Optional<List<WebElement>> checkBoxYes = driverTools.elements(QUESTIONNAIRE_CHECKBOX_YES);
                    driverTools.clickWithJavascriptExecutor(checkBoxYes.get().get(i));
                }
            }
        }

        // Handle hotel name and address if required
        if (isPresent(HOTEL_NAME)) {
            driverTools.scrollIntoWebView(HOTEL_NAME);
            List<WebElement> hotelName = driverTools.elements(HOTEL_NAME).orElseThrow();
            logger.log("Hotel name fields for GLOBALTIX: " + hotelName.size());
            for (WebElement element : hotelName) {
                driverTools.sendKeys(element, generateRandomString(10) + " GLOBALTIX Automation Test");
            }

            if (isPresent(HOTEL_ADDRESS)) {
                driverTools.scrollIntoWebView(HOTEL_ADDRESS);
                List<WebElement> hotelAddress = driverTools.elements(HOTEL_ADDRESS).orElseThrow();
                logger.log("Hotel address fields for GLOBALTIX: " + hotelAddress.size());
                for (WebElement element : hotelAddress) {
                    driverTools.sendKeys(element, generateRandomString(11) + " GLOBALTIX Test");
                }
            }
        }

        logger.log("GLOBALTIX guest details filled successfully");
    }

    @SneakyThrows
    private void fillGuestDetailJtr() {
        logger.log("Filling guest details for JTR supplier");

        // Handle visitor details for JTR
        if (driverTools.elements(VISITOR_DETAILS).isPresent()) {
            List<WebElement> visitorDetails = driverTools.elements(VISITOR_DETAILS).orElseThrow();
            int quantity = visitorDetails.size();
            logger.log("Total visitor details found for JTR: " + quantity);

            for (int i = 0; i < quantity; i++) {
                WebElement visitor = visitorDetails.get(i);
                driverTools.scrollIntoWebView(visitor);
                driverTools.waitForElementToAppear(visitor, 5).orElseThrow();
                driverTools.clickWithJavascriptExecutor(visitor);

                // Fill questionnaire if required
                if (driverTools.elements(QUESTIONNAIRE_CHECKBOX_YES).isPresent()) {
                    Optional<List<WebElement>> checkBoxYes = driverTools.elements(QUESTIONNAIRE_CHECKBOX_YES);
                    driverTools.clickWithJavascriptExecutor(checkBoxYes.get().get(i));
                }
            }
        }

        // Handle pickup time if required for JTR
        if (isPresent(PICKUP_TIME)) {
            driverTools.scrollIntoWebView(PICKUP_TIME);
            List<WebElement> pickupTime = driverTools.elements(PICKUP_TIME).orElseThrow();
            logger.log("Pickup time fields for JTR: " + pickupTime.size());
            for (WebElement element : pickupTime) {
                driverTools.scrollIntoWebView(element);
                driverTools.clickWithJavascriptExecutor(element);
                if (driverTools.waitForElementsToAppear(PICKUP_TIME_RADIO_BUTTON).isPresent()) {
                    driverTools.scrollIntoWebView(PICKUP_TIME_RADIO_BUTTON);
                    driverTools.click(PICKUP_TIME_RADIO_BUTTON);
                }
            }
        }

        logger.log("JTR guest details filled successfully");
    }

    private boolean isPresent(Locator locator) {
        return driverTools.elements(locator).map(list -> !list.isEmpty()).orElse(false);
    }

    public void checkPriceChange() {
        if (driverTools.waitForElementToAppear(PRICE_CHANGE).isPresent())
            driverTools.clickWithJavascriptExecutor(PRICE_CHANGE);
    }

    public String addBundlingItems(int qty, int ticketIndex) {
        if(driverTools.isElementDisplayed(ADD_TO_BUNDLING_PACK));
        driverTools.click(ADD_TO_BUNDLING_PACK);

        driverTools.waitForElementToPresence(TIMESLOT_CHIPS,5).ifPresent(driverTools::click);
        //set ticket quantity in bundling item
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        ttdProductDetailPageWeb.setTicketQty( qty,ticketIndex);

        driverTools.clickWithJavascriptExecutor(ADD_TO_BUNDLE_PACK_BTN);
        Assertion.assertThat("verify bundling product is added",driverTools.isElementDisplayed(BUNDLE_DELETE_BTN),is(true));
        WebElement bundleItemElement = driverTools.waitForElementToAppear(ADDED_BUNDLING_ITEM_TITLE,10).orElseThrow();
        return driverTools.getText(bundleItemElement);
    }

    public boolean verifyValidityText(String text) {
        driverTools.waitForElementToPresence(VALIDITY_TEXT);
        String bookingFormDate = driverTools.getText(VALIDITY_TEXT).orElseThrow();
        String[] validityDate = text.split(" - ");
        log("validity text before : [" + validityDate[1]);
        log("validity text on booking form: [" + bookingFormDate );
        return validityDate[1].equalsIgnoreCase(bookingFormDate);
    }

    public void clickClaimVoucher(){
        driverTools.waitForElementToPresence(VOUCHER_CLAIM_BTN).ifPresent(driverTools::click);
    }

    public boolean verifyClaimVoucherPopUp() {
        boolean isClaimedButtonPresent = driverTools.waitForElementToAppear(VOUCHER_CLAIMED).isPresent();
        Assertion.assertThat("The voucher button text will change to 'Claimed'", isClaimedButtonPresent, is(true));

        boolean isSnackbarVisible = driverTools.waitForElementToPresence(VOUCHER_APPLIED_MODAL).isPresent();
        Assertion.assertThat("Voucher applied snackbar will be shown", isSnackbarVisible, is(true));

        return isClaimedButtonPresent && isSnackbarVisible;
    }

    public String getOrderDate(){
        return driverTools.getText(ORDER_DATE).orElseThrow();
    }

    public void fillContactDetailsNonLogin(String salutation, String fullName, String phoneNumber, String email) {
        selectSalutation(salutation);
        driverTools.sendKeys(FULL_NAME_INPUT, fullName);
        driverTools.sendKeys(PHONE_NUMBER_INPUT, phoneNumber);
        driverTools.sendKeys(EMAIL_INPUT, email);
        selectCountry("indonesia");
        clickSameAsContactToggleBtn();
    }

    public void clickSameAsContactToggleBtn(){
        if (driverTools.waitForElementToPresence(SAME_AS_CONTACT_TOGGLE_BTN, 10).isEmpty()) {
            logger.log("Toggle not found.");
            return;
        }
        driverTools.scrollIntoWebView(SAME_AS_CONTACT_TOGGLE_BTN);
        for (int attempt = 1; attempt <= 3; attempt++) {
            if (driverTools.isRadioButtonChecked(SAME_AS_CONTACT_TOGGLE_BTN)) {
                return;
            }
            driverTools.clickWithJavascriptExecutor(SAME_AS_CONTACT_TOGGLE_BTN);
            for (int poll = 0; poll < 3; poll++) {
                if (driverTools.isRadioButtonChecked(SAME_AS_CONTACT_TOGGLE_BTN)) {
                    return;
                }
                driverTools.dummyWait(1);
            }
            logger.log(" Toggle not checked after attempt " + attempt + "; retrying");
        }
    }

    public void selectSalutation(String salutation) {
        driverTools.waitForElementToAppear(CONTACT_DETAIL);
        driverTools.scrollIntoWebView(CONTACT_DETAIL);
        salutation = salutation.toLowerCase();
        int index = switch (salutation) {
            case "mr" -> 0;
            case "mrs" -> 1;
            case "ms" -> 2;
            default -> throw new IllegalArgumentException("Unsupported salutation: " + salutation);
        };
        Locator inputToVerify = contactSalutationInput(index);
        Locator clickTarget;
        if (driverTools.elements(SALUTATIONS, 5).map(List::size).orElse(0) > index) {
            clickTarget = contactSalutationOption(index);
        } else {
            clickTarget = switch (salutation) {
                case "mr" -> MR_SALUTATION_CHECKBOX;
                case "mrs" -> MRS_SALUTATION_CHECKBOX;
                case "ms" -> MS_SALUTATION_CHECKBOX;
                default -> throw new IllegalArgumentException("Unsupported salutation: " + salutation);
            };
        }
        if (!clickRadioUntilChecked(clickTarget, inputToVerify)) {
            throw new NoSuchElementException("Failed to select salutation (title not checked after retries): " + salutation);
        }
    }

    /**
     * Clicks a custom React radio control and confirms the selection actually registered.
     * <p>
     * {@link com.tiket.appium.DriverTools#clickWithJavascriptExecutor(Locator)} dispatches the click
     * asynchronously (it schedules {@code el.click()} via {@code setTimeout(..., 0)}), so reading the
     * state immediately can observe the previous (unchecked) value. We therefore poll the underlying
     * radio input's DOM {@code checked} property — which React updates rather than the HTML attribute —
     * and re-click if it has not flipped, which removes the intermittent "title not selected" failures.
     *
     * @return {@code true} once the radio reports checked, {@code false} if it never did within the retries.
     */
    private boolean clickRadioUntilChecked(Locator clickTarget, Locator inputToVerify) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            driverTools.waitForElementToBeClickable(clickTarget, 10).orElseThrow();
            driverTools.clickWithJavascriptExecutor(clickTarget);
            for (int poll = 0; poll < 3; poll++) {
                if (driverTools.isRadioButtonChecked(inputToVerify)) {
                    return true;
                }
                driverTools.dummyWait(1);
            }
            logger.log("[clickRadioUntilChecked] radio not checked after attempt " + attempt + "; retrying: " + clickTarget);
        }
        return false;
    }

    private Locator contactSalutationOption(int index) {
        return new Locator(
                "CONTACT_SALUTATION_OPTION", By.xpath("(//label[.//input[@name='contactDetails.salutation']])[" + (index + 1) + "]"));
    }

    private Locator contactSalutationInput(int index) {
        return new Locator("CONTACT_SALUTATION_INPUT",
                By.xpath("(//input[@name='contactDetails.salutation'])[" + (index + 1) + "]"));
    }

    public void selectToggleButton(){
        if (driverTools.element(VISITOR_TOGGLE_BTN).isPresent()) {
            driverTools.waitForElementToAppear(VISITOR_TOGGLE_BTN,5).orElseThrow();
            driverTools.clickWithJavascriptExecutor(VISITOR_TOGGLE_BTN);
        }
    }

    public abstract void selectSalutationContactDetailsLogin(String salutation);
    public abstract void selectCountryContactDetailsLogin(String countryName);

    private static final Locator LOGIN_NOW_BUTTON = new Locator("LOGIN_NOW_BUTTON", AppiumBy.xpath("//div[contains(@class,'InfoBanner_word_break')]"));
    private static final Locator FULL_NAME_INPUT = new Locator("FULL_NAME_INPUT", AppiumBy.xpath("//input[@name='contactDetails.fullname' and @id='nama-lengkap' or @id='full-name']"));
    private static final Locator FULL_NAME_INPUT_VISITOR = new Locator("FULL_NAME_INPUT_VISITOR", AppiumBy.xpath("//*[contains(@class,'SmartProfile')]//input[@id='nama-lengkap' or @id='full-name']"));
    private static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", AppiumBy.xpath("//input[@id='nomor-ponsel' or @id='mobile-number']"));
    private static final Locator PHONE_NUMBER_GUEST_INPUT = new Locator("PHONE_NUMBER_GUEST_INPUT", By.xpath("//input[@id='phone-number']"));
    private static final Locator NATIONALITY_DROPDOWN = new Locator("NATIONALITY_DROPDOWN", AppiumBy.xpath("//*[@id='nationality']"));
    private static final Locator INDONESIA = new Locator("INDONESIA", AppiumBy.xpath("//*[contains(text(),'Indonesia')]"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", AppiumBy.xpath("//input[@name='contactDetails.emailAddress' and @id='alamat-email' or @id='email-address']"));
    private static final Locator RADIO_BUTTON_INSURANCE = new Locator("RADIO_BUTTON_INSURANCE", AppiumBy.xpath("//*[contains(@name,'selectedInsurance')]"));
    private static final Locator MR_SALUTATION_CHECKBOX = new Locator("MR_SALUTATION_CHECKBOX", AppiumBy.xpath("//*[contains(@class,'ContactDetails_radio_group')]/*[1]/*[contains(@class,'Radio_checkbox')]"));
    private static final Locator MRS_SALUTATION_CHECKBOX = new Locator("MRS_SALUTATION_CHECKBOX", AppiumBy.xpath("//*[contains(@class,'ContactDetails_radio_group')]/*[2]/*[contains(@class,'Radio_checkbox')]"));
    private static final Locator MS_SALUTATION_CHECKBOX = new Locator("MS_SALUTATION_CHECKBOX", AppiumBy.xpath("//*[contains(@class,'ContactDetails_radio_group')]/*[3]/*[contains(@class,'Radio_checkbox')]"));
    private static final Locator SELECTED_SALUTATION_CHECKBOX = new Locator("SELECTED_SALUTATION_CHECKBOX", AppiumBy.xpath("//*[@checked]"));
    private static final Locator PRODUCT_TITLE = new Locator("PRODUCT_TITLE", AppiumBy.xpath("//*[contains(@class, 'OrderDetail_product_title_')]"));
    private static final Locator GUEST_CONTAINER = new Locator("GUEST_CONTAINER", AppiumBy.xpath("//*[contains(@class,'VisitorDetails_form_list')]//*[contains(@class,'FormWrapper_title')]"));
    private static final Locator SAME_AS_CONTACT_TOGGLE_BTN = new Locator("SAME_AS_CONTACT_TOGGLE_BTN", AppiumBy.xpath("//input[contains(@class,'Toggle_hidden_checkbox')]"));
    private static final Locator VALID_DATE_TEXT = new Locator("VALID_DATE_TEXT", AppiumBy.xpath("//*[contains(@class,'OrderDetail_feature_with_icon')]//*[contains(text(),'Valid') or contains(text(),'Berlaku')]"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", AppiumBy.xpath("//*[contains(@class, 'PaymentDetail_price')]"));
    private static final Locator VALIDITY_PERIOD = new Locator("VALIDITY_PERIOD", AppiumBy.xpath("//*[contains(@class,'OrderDetail_schedule_value')]"));
    private static final Locator TICKET_QTY = new Locator("TICKET_QTY", AppiumBy.xpath("//*[contains(@class,'OrderDetail_ticket_detail')]//*[contains(text(), 'Tickets') or contains(text(), 'Ticket') or contains(text(),'Tiket')]"));
    private static final Locator ADDITIONAL_DETAIL_TEXT = new Locator("ADDITIONAL_DETAIL_TEXT", AppiumBy.xpath("//*[contains(@class,'AdditionalDetails_wrapper')]//div[contains(@class,'FormWrapper_title') or contains(@class,'OrderQuestionnaireForm_form_item_wrapper')]"));
    private static final Locator ID_INPUT_BOX = new Locator("ID_INPUT_BOX", AppiumBy.xpath("//input[@id='apa-nomor-ktp-anda' and (@placeholder='What is your KTP number?' or @placeholder='Apa nomor KTP anda?')]"));
    private static final Locator ADDRESS_INPUT_BOX = new Locator("ADDRESS_INPUT_BOX", AppiumBy.xpath("//input[@id='alamat-tempat-tinggal' or @id='pickup-location']"));
    private static final Locator FLIGHT_DATE = new Locator("FLIGHT_DATE", AppiumBy.xpath("//input[@id='keberangkatan-penerbangan-']"));
    private static final Locator ARE_YOU_READY_QUESTION = new Locator("ARE_YOU_READY_QUESTION", AppiumBy.xpath("//input[@id='are-you-ready' or @id='apakah-kamu-siap']"));
    private static final Locator ADDITIONAL_DETAIL_FREE_TEXT_INPUT = new Locator("ADDITIONAL_DETAIL_FREE_TEXT_INPUT", AppiumBy.xpath("//*[contains(@class,'OrderQuestionnaireForm') or contains(@class,'AdditionalDetails_wrapper') or contains(@class,'BottomSheet_body')]//input[not(@type='radio') and not(@type='checkbox') and not(@readonly) and not(@disabled)]"));
    private static final Locator QUESTIONNAIRE_ANSWER_YES = new Locator("QUESTIONNAIRE_ANSWER_YES", AppiumBy.xpath("//*[contains(@class,'SingleSelection_wrapper')]//span[contains(@class,'Radio_children') and (contains(text(),'Yes') or contains(text(),'Ya'))]"));
    private static final Locator SAVE_DATE_BUTTON = new Locator("SAVE_DATE_BUTTON", AppiumBy.xpath("//*[contains(@class, 'WheelPicker_date_wrapper')]//following-sibling::button"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.xpath("//button[contains(@class, 'variant_primary')]//span[(text()='Save' or text()='Simpan')]"));
    private static final Locator OK_PRICE_CHANGE = new Locator("OK_PRICE_CHANGE", AppiumBy.xpath("//*[contains(@class,'_pop_up')]//button"));
    private static final Locator LPG_BANNER_FOOTER = new Locator("LPG_BANNER_FOOTER", AppiumBy.xpath("//*[contains(@class,'GradientBanner_content')]"));
    private static final Locator PRICE_DETAIL_PRICE_TEXT = new Locator("PRICE_DETAIL_PRICE_TEXT", AppiumBy.xpath("//*[contains(@class, 'PaymentDetail_price_row')]/span[last()]"));
    private static final Locator PRICE_BREAKDOWN_EN = new Locator("PRICE_BREAKDOWN_EN", AppiumBy.xpath("//*[contains(@class,'PaymentDetail_description__')]//*[ (contains(text(),'IDR'))]"));
    private static final Locator PRICE_BREAKDOWN_IN = new Locator("PRICE_BREAKDOWN_IN", AppiumBy.xpath("//*[contains(@class,'PaymentDetail_description__') and(contains(text(),'IDR'))]"));
    private static final Locator SEAT_BUTTON = new Locator("SEAT_BUTTON", AppiumBy.xpath("//*[contains(@class,'SeatItem_available')]"));
    private static final Locator SELECTED_SEAT = new Locator("SELECTED_SEAT", AppiumBy.xpath("//*[contains(@class,'SeatSelected_sectio')]//span[contains(@class,'Text_weight_bold')]"));
    private static final Locator CONTINUE_PAYMENT_FOOTER_BUTTON = new Locator("CONTINUE_PAYMENT_FOOTER_BUTTON", AppiumBy.xpath("//*[contains(@class,'FooterMobile_cta')]//button"));
    private static final Locator PRICE_DISCOUNT = new Locator("PRICE_DISCOUNT", AppiumBy.xpath("//*[contains(@class,'TicketBreakdown_price_row')]//*[contains(@class,'positive')]"));
    private static final Locator PRICE_DETAIL_BTN = new Locator("PRICE_DETAIL_BTN", AppiumBy.xpath("//*[contains(@class,'PaymentDetail_price')]"));
    private static final Locator CLOSE_BTN_PRICE_DETAIL = new Locator("CLOSE_BTN_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'BottomSheet_close_button__')]"));
    private static final Locator TOTAL_PRICE_FROM_PRICE_DETAIL = new Locator("TOTAL_PRICE_FROM_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'Text_text__') and contains(text(),'Total')]/following-sibling::*"));
    private static final Locator PACKAGE_NAME = new Locator("PACKAGE_NAME", AppiumBy.xpath("//*[contains(@class,'OrderDetail_package_detail')]/*[text()]"));
    private static final Locator TIKET_POINT_TOTAL_PAYMENT = new Locator("TIKET_POINT_TOTAL_PAYMENT", AppiumBy.xpath("//*[contains(@class,'PaymentDetail_loyalty_point') and not(contains(@class,'modal'))]"));
    private static final Locator TIKET_POINT_TOTAL_PAYMENT_PRICE_DETAIL = new Locator("TIKET_POINT_TOTAL_PAYMENT_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'PaymentDetail_loyalty_modal')]/*[contains(@class,'PaymentDetail_point_wrapper')]"));
    private static final Locator TIKET_POINT = new Locator("TIKET_POINT", AppiumBy.xpath("//*[contains(@class,'PaymentDetail_point_wrapper')]"));
    private static final Locator GUEST_PRICE_DETAIL_MAP = new Locator("GUEST_PRICE_DETAIL_MAP", AppiumBy.xpath("//div[contains(@class, 'PaymentDetail_price_row')]/span[contains(@class, 'PaymentDetail_description')][1]"));
    private static final Locator PRICE_DETAIL_MAP = new Locator("PRICE_DETAIL_MAP", AppiumBy.xpath("//div[contains(@class, 'PaymentDetail_price_row')]/span[contains(@class, 'PaymentDetail_description')][2]"));
    private static final Locator COUNTRY_SELECTOR = new Locator("COUNTRY_SELECTOR", AppiumBy.xpath("//*[contains(@class,'CountrySelection_dropdown_wrapper')]//*[contains(@class,'styles_input_inner_wrapper')]"));
    private static final Locator COUNTRY_SELECTOR_SEARCH_BAR = new Locator("COUNTRY_SELECTOR_SEARCH_BAR", AppiumBy.xpath("//*[contains(@class,'SearchBox_placeholder')]//input"));
    private static final Locator COUNTRY_SELECTOR_FIRST_RESULT = new Locator("COUNTRY_SELECTOR_FIRST_RESULT", AppiumBy.xpath("(//*[contains(@class,'CountryListSelection_list_item')])[1]"));
    private static final Locator BUNDLING_ITEM = new Locator("BUNDLING_ITEM", AppiumBy.xpath("//*[contains(@class,'BundleBFCard_btn_claim_add')]"));
    private static final Locator ADD_TO_BUNDLING_PACK = new Locator("ADD_TO_BUNDLING_PACK", AppiumBy.xpath("(//*[contains(@class,'BundleBFCard_btn_claim_add')])[1]"));
    private static final Locator BOOKING_INFO = new Locator("BOOKING_INFO", AppiumBy.xpath("//span[contains(@class, 'MobileLeftColumn_page_title')]"));
    private static final Locator CONTACT_DETAIL = new Locator("CONTACT_DETAIL", AppiumBy.xpath("//section[contains(@class, 'ContactDetails_wrapper')]"));
    private static final Locator ACTIVITY_DETAILS = new Locator("ACTIVITY_DETAILS", AppiumBy.xpath("//div[contains(@class, 'OrderDetail_main_content')]"));
    private static final Locator PRODUCT_NAME = new Locator("PRODUCT_NAME", AppiumBy.xpath("//div[@data-testid='order-product-detail']//h3[contains(@class, 'OrderDetail_product_title')]"));
    private static final Locator TICKET_QUANTITY = new Locator("TICKET_QUANTITY", AppiumBy.xpath("//*[contains(@class,'Button_button') and @type=\"button\"]"));
    private static final Locator VISITOR_1_DETAILS = new Locator("VISITOR_1_DETAILS", AppiumBy.xpath("(//*[contains(@class, 'toggle')])[1]"));
    private static final Locator VISITOR_2_DETAILS = new Locator("VISITOR_2_DETAILS", AppiumBy.xpath("(//*[contains(@class, 'toggle')])[2]"));
    private static final Locator FREE_SEATING_BOOKING_PAGE = new Locator("FREE_SEATING_BOOKING_PAGE", By.xpath("//div[contains(@class, 'OrderDetail_seat_type')]//span[contains(text(), 'Free seating')]"));

    @SneakyThrows
    public void clickLoginNow() {
        driverTools.click(LOGIN_NOW_BUTTON);
    }

    @SneakyThrows
    public boolean isLoginNowButtonDisplayed() {
        boolean result = driverTools.waitForElementToAppear(LOGIN_NOW_BUTTON).isPresent();
        return result;
    }

    @SneakyThrows
    public boolean isLpgBannerFooterDisplayed() {
        boolean result = driverTools.waitForElementToAppear(LPG_BANNER_FOOTER).isPresent();
        return result;
    }

    @SneakyThrows
    public void clickContinuePaymentFooterButton() {
        driverTools.click(CONTINUE_PAYMENT_FOOTER_BUTTON);
    }

    public void checkPriceNotifChange() {
        try {
            if(driverTools.waitForElementToAppear(OK_PRICE_CHANGE).isPresent()) {
                driverTools.click(OK_PRICE_CHANGE);
            }
        } finally {
        }
    }

    public void fillContactOrderDetail(boolean isLogin){

        if (!isLogin) {
            driverTools.swipeToElement(EMAIL_INPUT, Swipe.UP,5).orElseThrow();
            driverTools.click(selectedSalutation());
            driverTools.scrollIntoWebView(EMAIL_INPUT);
            driverTools.click(EMAIL_INPUT);
            driverTools.sendKeys(EMAIL_INPUT, randomName() + "-aut@tiket.com");
            driverTools.click(PHONE_NUMBER_INPUT);
            driverTools.sendKeys(PHONE_NUMBER_INPUT, "8123" + randomInt(7));
            driverTools.scrollIntoWebView(FULL_NAME_INPUT);
            driverTools.click(FULL_NAME_INPUT);
            driverTools.sendKeys(FULL_NAME_INPUT, randomName()+" "+randomName());
            driverTools.swipeUp();
            selectCountry("indonesia");
            driverTools.hideKeyboard(1);
            return;
        }

        driverTools.waitForElementToAppear(CONTACT_DETAIL, 15).orElseThrow();
        driverTools.scrollIntoWebView(CONTACT_DETAIL);
        selectSalutationContactDetailsLogin("mr");
        selectCountryContactDetailsLogin("indonesia");
    }

    public void fillContactOrderDetail(boolean isLogin,String email, String phoneNumber){

        if (!isLogin) {
            driverTools.swipeToElement(EMAIL_INPUT, Swipe.UP,5).orElseThrow();
            driverTools.click(selectedSalutation());
            driverTools.scrollIntoWebView(EMAIL_INPUT);
            driverTools.click(EMAIL_INPUT);
            driverTools.sendKeys(EMAIL_INPUT, email);
            driverTools.click(PHONE_NUMBER_INPUT);
            driverTools.sendKeys(PHONE_NUMBER_INPUT, phoneNumber);
            driverTools.scrollIntoWebView(FULL_NAME_INPUT);
            driverTools.click(FULL_NAME_INPUT);
            driverTools.sendKeys(FULL_NAME_INPUT, randomName()+" "+randomName());
            driverTools.swipeUp();
            selectCountry("indonesia");
            driverTools.hideKeyboard(1);
            return;
        }

        driverTools.waitForElementToAppear(CONTACT_DETAIL, 15).orElseThrow();
        driverTools.scrollIntoWebView(CONTACT_DETAIL);
        selectSalutationContactDetailsLogin("mr");
        selectCountryContactDetailsLogin("indonesia");
    }

    /**
     * @param name - input for name column, put "random" for random input
     * @param phoneNumber - input for phone number column, put "random" for random input
     * @param email - input for email column, put "random" for random input
     * This method only works for guest (non-login) case
     */
    public  void fillCustomContactOrderDetail(String name, String phoneNumber, String email){
        name = name.equalsIgnoreCase("random") ? randomName() : name;
        phoneNumber = phoneNumber.equalsIgnoreCase("random") ? "8123" + randomInt(7) : phoneNumber;
        email = email.equalsIgnoreCase("random") ? randomName() + "-aut@tiket.com" : email;

        driverTools.swipeToElement(EMAIL_INPUT, Swipe.UP,5).orElseThrow();
        driverTools.click(selectedSalutation());
        driverTools.sendKeys(EMAIL_INPUT, email);
        driverTools.sendKeys(PHONE_NUMBER_INPUT, phoneNumber);
        driverTools.sendKeys(FULL_NAME_INPUT, name);
        if(driverTools.waitForElementToPresence(NATIONALITY_DROPDOWN,5).isPresent()) {
            driverTools.clickElementByCoordinateDWeb(NATIONALITY_DROPDOWN);
            driverTools.clickWithJavascriptExecutor(INDONESIA);
        }
        driverTools.hideKeyboard(1);
    }

    private Locator selectedSalutation() {
        Locator[] salutationList = { MR_SALUTATION_CHECKBOX, MRS_SALUTATION_CHECKBOX, MS_SALUTATION_CHECKBOX };
        return getRandomObject(salutationList);
    }

    /**
     * Selects a random visitor salutation (Mr/Mrs/Ms) for the visitor card at {@code guestGroupIndex}
     * (1-based, matching the guest order in the form).
     * <p>
     * The visitor radio group is a custom React control, so a plain Selenium click is flaky (it can
     * fire before the radio is interactable or not trigger the synthetic onChange handler, leaving the
     * title unset). The locators are also global, so we scope them to the specific guest's radio group
     * to avoid always hitting guest #1. We then click-and-verify via the input's DOM {@code checked}
     * property with retry — the same reliable approach used by the contact-details
     * {@link #selectSalutation(String)}.
     */
    private void selectVisitorSalutation(int guestGroupIndex) {
        int optionIndex = generateRandomInt(0, 2);
        Locator clickTarget = visitorSalutationOption(guestGroupIndex, optionIndex);
        Locator inputToVerify = visitorSalutationInput(guestGroupIndex, optionIndex);
        if (driverTools.element(inputToVerify).isPresent()) {
            if (!clickRadioUntilChecked(clickTarget, inputToVerify)) {
                logger.softWarning("[selectVisitorSalutation] visitor title not confirmed checked for guest group " + guestGroupIndex);
            }
        } else {
            // Layout without a verifiable radio input — best-effort click with a clickability wait.
            driverTools.waitForElementToBeClickable(clickTarget, 10).orElseThrow();
            driverTools.clickWithJavascriptExecutor(clickTarget);
        }
    }

    private Locator visitorSalutationOption(int guestGroupIndex, int optionIndex) {
        return new Locator("VISITOR_SALUTATION_OPTION",
                By.xpath("(//*[contains(@class,'FormInput_radio_group')])[1]/*[" + (optionIndex + 1) + "]//*[contains(@class,'Radio_checkbox')]"));
    }

    private Locator visitorSalutationInput(int guestGroupIndex, int optionIndex) {
        return new Locator("VISITOR_SALUTATION_INPUT",
                By.xpath("(//*[contains(@class,'FormInput_radio_group')])[1]/*[" + (optionIndex + 1) + "]//input"));
    }

    /**
     * Scrolls the page down. Can be overridden by subclasses to use platform-specific scroll.
     * @param times number of times to scroll
     */
    public void scrollPageDown(int times) {
        driverTools.swipeUp(times);
    }

    public void fillGuestDetail() {
        if(Configs.PLATFORM == Platform.DWEB){
            try {
                driverTools.scroll(2,300);
            }catch (Exception e) {
                logger.log("Scroll down failed: " + e.getMessage());
            }
        }else {
            driverTools.swipeUp(2);
        }
        driverTools.waitForElementToAppear(GUEST_CONTAINER, 5);
        if (!driverTools.isElementDisplayed(GUEST_CONTAINER)) {
            // New booking-form layout (EventInfoPage events): the visitor section only
            // exposes a "Same as contact details" toggle and no per-visitor card, so
            // fillGuestDetail used to no-op here and leave the form invalid.
            if (driverTools.element(SAME_AS_CONTACT_TOGGLE_BTN).isPresent()) {
                logger.log("[fillGuestDetail] GUEST_CONTAINER not present; enabling 'Same as contact details' toggle");
                enableSameAsContactDetailsToggle();
            }
            if (driverTools.isElementDisplayed(ADDITIONAL_DETAIL_TEXT)) {
                fillAdditionalDetail();
            }
            return;
        }
        int guestCount = driverTools.elements(GUEST_CONTAINER).get().size();
        logger.log("Guest count : " + guestCount);

        boolean singlePaxWithSameAsContactToggle = guestCount == 1
                && driverTools.element(SAME_AS_CONTACT_TOGGLE_BTN).isPresent();

        if (singlePaxWithSameAsContactToggle) {
            logger.log("[fillGuestDetail] Single passenger: enabling Same as contact details (skip manual visitor fields)");
            enableSameAsContactDetailsToggle();
        } else if (guestCount >= 1) {
            for (int i = 0; i < guestCount; i++) {
                List<WebElement> guestContainers = driverTools.elements(GUEST_CONTAINER).orElseThrow();
                logger.log("Clicking guest container " + (i + 1));
                driverTools.clickWithJavascriptExecutor(guestContainers.get(i));
                selectVisitorSalutation(i + 1);
                driverTools.sendKeys(FULL_NAME_INPUT_VISITOR, randomName()+" "+randomName());
                clickSaveButton();
            }
        }
        if (!singlePaxWithSameAsContactToggle
                && driverTools.element(SAME_AS_CONTACT_TOGGLE_BTN).isEmpty()
                && driverTools.isElementDisplayed(GUEST_CONTAINER)) {
            driverTools.click(GUEST_CONTAINER);
            if(driverTools.waitForElementToPresence(NATIONALITY_DROPDOWN).isPresent()) {
                driverTools.click(NATIONALITY_DROPDOWN);
                driverTools.clickWithJavascriptExecutor(INDONESIA);
            }
            clickSaveButton();
        }
        if (!singlePaxWithSameAsContactToggle) {
            Optional<WebElement> nameElementOpt = driverTools.waitForElementToAppear(FULL_NAME_INPUT_VISITOR, 20);
            if (nameElementOpt.isEmpty() && driverTools.isElementDisplayed(GUEST_CONTAINER)) {
                logger.log("[fillGuestDetail] FULL_NAME_INPUT_VISITOR not visible; expanding first guest container");
                driverTools.elements(GUEST_CONTAINER).filter(list -> !list.isEmpty()).ifPresent(list ->
                        driverTools.clickWithJavascriptExecutor(list.get(0)));
                nameElementOpt = driverTools.waitForElementToAppear(FULL_NAME_INPUT_VISITOR, 20);
            }
            WebElement nameElement = nameElementOpt.orElseThrow(() -> new NoSuchElementException(
                    "FULL_NAME_INPUT_VISITOR not found (guest row may need a different locator or scroll)"));
            String nameFromGetText = driverTools.getText(nameElement);
            String nameValue = driverTools.getValue(nameElement);
            logger.log("[fillGuestDetail] FULL_NAME_INPUT_VISITOR getText='" + nameFromGetText + "' value(domProperty/attr)='" + nameValue + "'");
            boolean nameIsEmpty = (nameFromGetText == null || nameFromGetText.trim().isEmpty()) && (nameValue == null || nameValue.trim().isEmpty());
            if (driverTools.element(SAME_AS_CONTACT_TOGGLE_BTN).isPresent() && nameIsEmpty) {
                driverTools.clickWithJavascriptExecutor(SAME_AS_CONTACT_TOGGLE_BTN);
            }
        }
        if (driverTools.isElementDisplayed(ADDITIONAL_DETAIL_TEXT)) {
            fillAdditionalDetail();
        }
    }

    /** Turns on "Same as contact details" for visitor (single-ticket flow). */
    private void enableSameAsContactDetailsToggle() {
        driverTools.waitForElementToPresence(SAME_AS_CONTACT_TOGGLE_BTN, 15).orElseThrow();
        driverTools.scrollIntoWebView(SAME_AS_CONTACT_TOGGLE_BTN);
        driverTools.clickWithJavascriptExecutor(SAME_AS_CONTACT_TOGGLE_BTN);
    }

    /**
     * Toggles the "Same as contact details" input on the TTD booking form.
     * <p>
     * Use this when the booking form only exposes the toggle (no per-visitor
     * card / {@code GUEST_CONTAINER}) and {@link #fillGuestDetail()} early-returns
     * without doing anything. Scrolls the toggle into view, then clicks it via
     * the JS executor. Logs and no-ops if the toggle is not present, so callers
     * don't need to guard with {@code isElementDisplayed} themselves.
     *
     * @return {@code true} if the toggle was clicked, {@code false} if it was
     *         not present on the page.
     */
    public boolean toggleSameAsContactDetails() {
        if (Configs.PLATFORM == Platform.DWEB) {
            try {
                driverTools.scroll(2, 300);
            } catch (Exception e) {
                logger.log("[toggleSameAsContactDetails] Scroll down failed: " + e.getMessage());
            }
        } else {
            driverTools.swipeUp(2);
        }
        if (driverTools.waitForElementToPresence(SAME_AS_CONTACT_TOGGLE_BTN, 10).isEmpty()) {
            logger.log("[toggleSameAsContactDetails] SAME_AS_CONTACT_TOGGLE_BTN not found; nothing to toggle");
            return false;
        }
        driverTools.scrollIntoWebView(SAME_AS_CONTACT_TOGGLE_BTN);
        driverTools.clickWithJavascriptExecutor(SAME_AS_CONTACT_TOGGLE_BTN);
        logger.log("[toggleSameAsContactDetails] Toggled 'Same as contact details'");
        return true;
    }

    public void fillAdditionalDetailGuest() {
        if (driverTools.isElementDisplayed(ADDITIONAL_DETAIL_TEXT)) {
            fillAdditionalDetail();
        }
    }

    public void clickSameAsContactToggle(int index) {
        Locator sameAsContactToggleBtn = new Locator("sameAsContactToggleBtn", AppiumBy.xpath("(//*[contains(@class, 'toggle')])["+index+"]"));
        driverTools.waitForElementToPresence(sameAsContactToggleBtn).orElseThrow();
        driverTools.scrollIntoWebView(sameAsContactToggleBtn);
        driverTools.click(sameAsContactToggleBtn);
    }

    public void clickSameAsContactToggle() {
        driverTools.dummyWait(2);
        driverTools.scrollIntoWebView(SAME_AS_CONTACT_TOGGLE_BTN);
        driverTools.clickWithJavascriptExecutor(SAME_AS_CONTACT_TOGGLE_BTN);
    }

    public void fillGuestDetailWithPhoneNumber() {
        scrollPageDown(2);
        driverTools.waitForElementToAppear(GUEST_CONTAINER, 5);
        if (!driverTools.isElementDisplayed(GUEST_CONTAINER)) {
            // New booking-form layout (EventInfoPage events): only a "Same as contact details"
            // toggle is exposed with no per-visitor card, so there are no visitor fields to fill.
            if (driverTools.element(SAME_AS_CONTACT_TOGGLE_BTN).isPresent()) {
                logger.log("[fillGuestDetailWithPhoneNumber] GUEST_CONTAINER not present; enabling 'Same as contact details' toggle");
                enableSameAsContactDetailsToggle();
            }
            if (driverTools.isElementDisplayed(ADDITIONAL_DETAIL_TEXT)) {
                fillAdditionalDetail();
            }
            return;
        }
        int guestCount = driverTools.elements(GUEST_CONTAINER).get().size();
        logger.log("Guest count : " + guestCount);

        boolean singlePaxWithSameAsContactToggle = guestCount == 1
                && driverTools.element(SAME_AS_CONTACT_TOGGLE_BTN).isPresent();

        if (singlePaxWithSameAsContactToggle) {
            logger.log("[fillGuestDetailWithPhoneNumber] Single passenger: enabling Same as contact details (skip manual visitor fields)");
            enableSameAsContactDetailsToggle();
        } else if (guestCount >= 1) {
            for (int i = 0; i < guestCount; i++) {
                List<WebElement> guestContainers = driverTools.elements(GUEST_CONTAINER).orElseThrow();
                logger.log("Clicking guest container " + (i + 1));
                driverTools.clickWithJavascriptExecutor(guestContainers.get(i));
                selectVisitorSalutation(i + 1);
                driverTools.sendKeys(FULL_NAME_INPUT_VISITOR, randomName() + " " + randomName());
                driverTools.sendKeys(PHONE_NUMBER_GUEST_INPUT, randomPhone());
                clickSaveButton();
            }
        }
        if (!singlePaxWithSameAsContactToggle
                && driverTools.element(SAME_AS_CONTACT_TOGGLE_BTN).isEmpty()
                && driverTools.isElementDisplayed(GUEST_CONTAINER)) {
            driverTools.click(GUEST_CONTAINER);
            if(driverTools.waitForElementToPresence(NATIONALITY_DROPDOWN).isPresent()) {
                driverTools.click(NATIONALITY_DROPDOWN);
                driverTools.clickWithJavascriptExecutor(INDONESIA);
            }
            clickSaveButton();
        }
        if (driverTools.isElementDisplayed(ADDITIONAL_DETAIL_TEXT)) {
            fillAdditionalDetail();
        }
    }

    public void clickInfinityPromoApplyButton() {
        driverTools.clickWithJavascriptExecutor(GV_TITLE);
        driverTools.scrollIntoWebView(GV_CLAIM);
        driverTools.clickWithJavascriptExecutor(GV_CLAIM);
    }

    private Locator getGuestContainer(int index) {
        return new Locator("GUEST_CONTAINER", AppiumBy.xpath("//*[contains(@class,'ContactDetails_card__')]["+index+"]"));
    }

    private void fillAdditionalDetail() {
        driverTools.waitForWebToLoad();
        driverTools.click(ADDITIONAL_DETAIL_TEXT);
        boolean touched = false;
        if (driverTools.isElementDisplayed(ID_INPUT_BOX)) {
            driverTools.sendKeys(ID_INPUT_BOX, "8123" + randomInt(3));
            touched = true;
        }
        if (driverTools.isElementDisplayed(ADDRESS_INPUT_BOX)) {
            driverTools.sendKeys(ADDRESS_INPUT_BOX, "Jl. " + randomName());
            touched = true;
        }
        if (driverTools.isElementDisplayed(FLIGHT_DATE)) {
            driverTools.click(FLIGHT_DATE);
            driverTools.click(SAVE_DATE_BUTTON);
            touched = true;
        }
        if(driverTools.isElementDisplayed(ARE_YOU_READY_QUESTION)){
            driverTools.click(ARE_YOU_READY_QUESTION);
            driverTools.click(QUESTIONNAIRE_ANSWER_YES);
            logger.log("Answered 'Are you ready?' question with 'Yes'");
        }
        if (!touched) {
            // The locators above only cover four known questions (KTP number, address, flight date,
            // "are you ready"). A product whose questionnaire asks anything else - "Pick-up point" on
            // the Kecak Uluwatu monitoring product - matched none of them, so nothing was typed, Save
            // was never clicked and the sheet was left open. The form then rejected "Continue payment"
            // and the run died two pages later in PaymentPage.checkOrderID with "No value present".
            touched = fillRemainingAdditionalDetailInputs();
        }
        if (touched) {
            logger.log("Additional details filled");
            clickSaveButton();
        } else {
            throw new IllegalStateException("Additional Booking Details is required but no answer could be filled."
                    + " Question(s) on screen: '" + driverTools.getText(ADDITIONAL_DETAIL_TEXT).orElse("unknown")
                    + "'. Add a locator for it in TtdBookingFormPageWeb.fillAdditionalDetail.");
        }
    }

    /**
     * Types a generic answer into every free-text question still showing in the Additional Booking
     * Details sheet. Used only when none of the known question locators matched, so products that
     * already work keep their existing, more specific handling.
     *
     * @return true when at least one input was filled
     */
    private boolean fillRemainingAdditionalDetailInputs() {
        List<WebElement> inputs = driverTools.elements(ADDITIONAL_DETAIL_FREE_TEXT_INPUT).orElse(List.of());
        boolean filledValue = false;
        for (WebElement input : inputs) {
            if (!driverTools.isElementDisplayed(input)) {
                continue;
            }
            driverTools.sendKeys(input, "TTD Automation " + randomName());
            filledValue = true;
        }
        logger.log("Filled " + (filledValue ? "" : "no ") + "free-text additional detail inputs out of " + inputs.size() + " found");
        return filledValue;
    }

    public void clickSaveButton() {
        logger.log("Clicking save button");
        WebElement webElement = driverTools.waitForElementToBeClickable(SAVE_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(webElement);
    }

    public abstract void continueToPaymentPage();

    public  String getPrice(){
        driverTools.waitForElementsToAppear(TOTAL_PRICE);
        String totalPrice = driverTools.getText(TOTAL_PRICE).orElseThrow();
        return totalPrice;
    }

    public  String getValidDateText(){
        driverTools.waitForElementToPresence(VALID_DATE_TEXT,5);
        String ValidDate = driverTools.getText(VALID_DATE_TEXT).orElseThrow();

        return ValidDate;
    }

    public  String getValidityPeriod(){
        String validityPeriod = driverTools.getText(VALIDITY_PERIOD).orElseThrow();
        return validityPeriod;
    }

    public  String getTicketQuantity(){

        String ticketQty= driverTools.getText(TICKET_QTY).orElseThrow();
        logger.log("Total ticket quantity "+ ticketQty);
        return ticketQty.split(" ")[0];
    }

    public  String getProductTitle(){
        String ProductTitle= driverTools.getText(PRODUCT_TITLE).orElseThrow();
        logger.log("Product Name on Booking form "+ ProductTitle);
        return ProductTitle;
    }

    public Boolean compareValidityText(String actualValidityText, String expectedValidityText) {
        //actualValidityText- "Valid for 31 Des 2023"
        //actualValidityText- "Berlaku hingga 31 Des 2023"
        //actualValidityText- "Sen, 29 Mei 2023 - Min, 31 Des 2023"
        //expectedValidityText- "29 Mei 2023 - 31 Des 2023"
        boolean compareStatus;
        if (actualValidityText.contains(" - ")) {
            if(expectedValidityText.toLowerCase().contains(": ")) {
                expectedValidityText = expectedValidityText.split(": ")[1];
            }

            // Compare normalized endpoints with equals rather than raw substring contains: a substring
            // check can false-positive (e.g. "31 Des 2023".contains("1 Des 2023") is true).
            String[] actualPeriodSplit = actualValidityText.split(" - ");
            String actualStart = normalizeValidityDate(actualPeriodSplit[0]);
            String actualEnd = normalizeValidityDate(actualPeriodSplit.length > 1 ? actualPeriodSplit[1] : actualPeriodSplit[0]);
            String[] expectedValidityPeriodSplit = expectedValidityText.split(" - ");
            String expectedStart = normalizeValidityDate(expectedValidityPeriodSplit[0]);
            String expectedEnd = normalizeValidityDate(expectedValidityPeriodSplit.length > 1 ? expectedValidityPeriodSplit[1] : expectedValidityPeriodSplit[0]);
            logger.log("actualValidity start/end -> " + actualStart + " / " + actualEnd);
            logger.log("expectedValidity start/end -> " + expectedStart + " / " + expectedEnd);
            compareStatus = actualStart.equals(expectedStart) && actualEnd.equals(expectedEnd);
        } else {
            // expectedValidityText may be a single date or a "start - end" range, and the booking-form
            // label can show either endpoint (e.g. "Berlaku pada <start>" or "Valid until <end>"). Strip
            // any leading label (drop everything before the first digit) and a leading zero on the day,
            // then match the normalized actual date against any normalized endpoint of the expected value.
            String actualValidity = normalizeValidityDate(actualValidityText);
            compareStatus = false;
            for (String expectedPart : expectedValidityText.split(" - ")) {
                String expectedValidity = normalizeValidityDate(expectedPart);
                logger.log("expectedValidity -> " + expectedValidity);
                logger.log("actualValidity -> " + actualValidity);
                if (actualValidity.equals(expectedValidity)) {
                    compareStatus = true;
                    break;
                }
            }
        }
        return compareStatus;
    }

    private String normalizeValidityDate(String text) {
        if (text == null) {
            return "";
        }

        return text.replaceFirst("^\\D+", "")
                .replaceFirst("^0", "")
                .replace("Agt", "Aug")
                .replace("Ags", "Aug")
                .replace("Mei", "May")
                .replace("Okt", "Oct")
                .replace("Des", "Dec")
                .trim();
    }
    public Boolean verifyDisplayedCurrency(String currency, String sectionType) {
        switch(sectionType.toLowerCase()) {
            case "booking page":
                return verifyDisplayedCurrencyBookingPage(currency);
            case "price detail":
                return verifyDisplayedCurrencyPriceDetail(currency);
            default:
                logger.fail("There is no option for sectionType '"+ sectionType +"'");
        }

        return false;
    }

    private Boolean verifyDisplayedCurrencyPriceDetail(String currency) {

        driverTools.scrollIntoWebView(TOTAL_PRICE);
        driverTools.click(TOTAL_PRICE);
        List<String> priceTexts = driverTools.getTexts(PRICE_DETAIL_PRICE_TEXT);
        logger.log("PRICE_DETAIL_PRICE_TEXT -> " + Arrays.toString(priceTexts.toArray(new String[0])));


        return CoreFunctions.checkCurrencyOnStringList(currency, priceTexts);
    }

    private Boolean verifyDisplayedCurrencyBookingPage(String currency) {

        driverTools.scrollIntoWebView(TOTAL_PRICE);
        boolean result = driverTools.getText(TOTAL_PRICE).orElseThrow().toLowerCase().contains(currency.toLowerCase());


        return result;
    }

    public void openPriceDetail() {
        driverTools.scrollIntoWebView(TOTAL_PRICE);
        driverTools.click(TOTAL_PRICE);
    }

    public List<String> getPriceDetailDiscount(){
        List<String> priceDiscount = driverTools.getTexts(PRICE_DISCOUNT);
        return priceDiscount;
    }

    public String getDiscountPrice() {
        driverTools.clickWithJavascriptExecutor(TOTAL_PRICE);
        WebElement discountPrice = driverTools.waitForElementToAppear(PRICE_DISCOUNT).orElseThrow();
        String price = driverTools.getText(discountPrice);
        driverTools.click(CLOSE_BTN_PRICE_DETAIL);
        return price;
    }

    Locator priceBreakLocator(String currency) {
        return new Locator("PRICE_BREAKDOWN", AppiumBy.xpath("//*[contains(@class,'TicketBreakdown_price_row')]//*[not(contains(@class,'bold')) and contains(text(),'"+currency+"')]"));
    }

    public boolean priceBreakDown(String currency) {
        openPriceDetail();
        List<WebElement> prices = new ArrayList<>();
        if (driverTools.waitForElementToAppear(priceBreakLocator(currency)).isPresent()) {
            List<WebElement> priceBreakdownElements = driverTools.elements(priceBreakLocator(currency)).orElseThrow();
            List<WebElement> priceDiscountElements = driverTools.elements(PRICE_DISCOUNT).orElseThrow();
            prices.addAll(priceBreakdownElements);
            prices.addAll(priceDiscountElements);
        }
        ArrayList<Double> arrayPrice = new ArrayList<>();
        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            boolean isContainNumber = text.matches(".*[0-9]+.*");
            if (isContainNumber) {
                String filterText = text.replace(",", ".");
                logger.log("price got: " + filterText);
                if (text.isEmpty()) filterText = "0";
                double price = CoreFunctions.convertPrice(filterText);
                logger.log("price got: " + price);
                arrayPrice.add(price);
            }
        }

        double sum = 0;
        for (double i : arrayPrice) {
            sum += i;
        }

        String totalPriceText = null;
        if (driverTools.isElementDisplayed(TOTAL_PRICE_FROM_PRICE_DETAIL)) {
            totalPriceText = driverTools.getText(TOTAL_PRICE_FROM_PRICE_DETAIL).orElseThrow();
        }
        double totalPriceDouble = CoreFunctions.convertPrice(totalPriceText);
        sum = doubleRound(sum);
        logger.log("actual total: " + sum);
        logger.log("expected total: " + totalPriceDouble);

        boolean isTotalPriceCorrect = (totalPriceDouble == sum) && (totalPriceDouble > 0);
        driverTools.click(CLOSE_BTN_PRICE_DETAIL);
        logger.log("isTotalPriceCorrect -> " + isTotalPriceCorrect);
        return isTotalPriceCorrect;
    }

    public String getGuestName(){
        String guestName = driverTools.getValueAttribute(FULL_NAME_INPUT,"value");
        logger.log("Guest Name : "+guestName);
        return guestName;
    }

    public String getGuestTitle(){
        driverTools.click(GUEST_CONTAINER);
        String guestTitle = driverTools.getValueAttribute(SELECTED_SALUTATION_CHECKBOX,"value").replace("\"","");
        driverTools.click(CLOSE_BTN_PRICE_DETAIL);
        logger.log("Guest Title : "+guestTitle);
        return guestTitle;
    }

    public String getPackageName() {
        String packageName = driverTools.getText(PACKAGE_NAME).orElseThrow();
        logger.log("Package Name : " + packageName);
        return packageName;
    }

    /**
     * Checks if the total payment tiket point is displayed.
     *
     * @return True if the total payment tiket point is displayed, false otherwise.
     */
    public boolean isTotalPaymentTiketPointDisplayed() {
        boolean result = driverTools.waitForElementToPresence(TIKET_POINT_TOTAL_PAYMENT).isPresent();
        return result;
    }

    /**
     * Checks if the price detail tiket point is displayed.
     *
     * @return True if the price detail tiket point is displayed, false otherwise.
     */
    public boolean isPriceDetailTiketPointDisplayed() {
        boolean result = driverTools.waitForElementToPresence(TIKET_POINT_TOTAL_PAYMENT_PRICE_DETAIL).isPresent();
        return result;
    }

    /**
     * Retrieves the earned points from the TTD booking form page.
     *
     * @return The earned points as a double value.
     */
    public double getEarnedPoints(){
        driverTools.scrollIntoWebView(TIKET_POINT);
        String points = driverTools.getText(TIKET_POINT).orElseThrow();
        return CoreFunctions.convertPriceSafely(points);
    }


    /**
     * This method retrieves the price details for each guest type from the web view and stores them in a map.
     * The keys of the map are the guest types and the values are lists of prices associated with each guest type.
     *
     * @return Returns a map where the keys are guest types and the values are lists of prices associated with each guest type.
     */
    public Map<String, List<String>> guestTypePriceTierMap() {
        Map<String, List<String>> guestTypePriceMap = new LinkedHashMap<>();
        Optional<List<WebElement>> priceElements = driverTools.elements(PRICE_DETAIL_MAP);
        Optional<List<WebElement>> guestElements = driverTools.elements(GUEST_PRICE_DETAIL_MAP);

        if (priceElements.isPresent() && guestElements.isPresent()) {
            List<WebElement> priceElementList = priceElements.get();
            List<WebElement> guestElementList = guestElements.get();
            for (int i = 0; i < priceElementList.size(); i++) {
                String guestType = driverTools.getText(guestElementList.get(i));
                String price = driverTools.getText(priceElementList.get(i));

                // Check if the guest type already exists in the map
                if (guestTypePriceMap.containsKey(guestType)) {
                    // If the guest type exists, add the price to its existing list
                    guestTypePriceMap.get(guestType).add(price);
                } else {
                    // If the guest type doesn't exist, create a new list and add the price to it
                    List<String> priceList = new ArrayList<>();
                    priceList.add(price);
                    guestTypePriceMap.put(guestType, priceList);
                }
            }
        }
        return normalizeKeys(guestTypePriceMap);
    }

    /**
     * This method normalizes the keys in a given map. The keys of the map are expected to be in the format "Ticket \\d+ \\((.+)\\)".
     * The method replaces each key with the string inside the parentheses.
     *
     * @param map The input map. It's a map where the keys are strings and the values are lists of strings.
     * @return Returns a new map where the keys have been normalized and the values are the same as in the input map.
     */

    private static Map<String, List<String>> normalizeKeys(Map<String, List<String>> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey()
                                .replaceAll("Ticket \\d+ \\((.+)\\)", "$1")
                                .replaceAll("Tiket \\d+ \\((.+)\\)", "$1"),
                        Map.Entry::getValue,
                        (list1, list2) -> {list1.addAll(list2);return list1;}, LinkedHashMap::new ));  //To preserve insertion order
    }

    /**
     * Compares two maps to check if they are equal. The maps are considered equal if they have the same size,
     * and for each key in the expected map, the actual map contains the same key and the associated list of values are equal.
     *
     * @param expected The expected map. It's a map where the keys are strings and the values are lists of strings.
     * @param actual The actual map to be compared with the expected map. It's a map where the keys are strings and the values are lists of strings.
     * @return Returns true if the actual map matches the expected map, false otherwise.
     */
    public  boolean compareMaps(Map<String, List<String>> expected, Map<String, List<String>> actual) {
        logger.log("Expected map: " + expected);
        logger.log("Actual map: " + actual);
        if (expected.size() != actual.size()) {
            logger.log("Maps are not the same size.");
            return false;
        }
        for (String key : expected.keySet()) {
            if (!actual.containsKey(key)) {
                logger.log("Key '" + key + "' not found in actual map.");
                return false;
            }
            List<String> expectedList = expected.get(key);
            List<String> actualList = actual.get(key);

            if (!expectedList.equals(actualList)) {
                logger.log("Values for key '" + key + "' are not equal. Expected: " + expectedList + ", Actual: " + actualList);
                return false;
            }
        }
        logger.log("Maps are equal.");
        return true;
    }

    public void addBundlingItems() {
        List<WebElement> bundlingItems = driverTools.elements(BUNDLING_ITEM).orElseThrow();
        for (WebElement bundlingItem : bundlingItems) {
            driverTools.click(bundlingItem);
            driverTools.waitForElementToAppear(ADD_TO_BUNDLING_PACK).ifPresent(webElement -> driverTools.click(webElement));
        }

    }

    public void fillBundlingGuestDetail() {
        scrollPageDown(2);
        if (driverTools.isElementDisplayed(GUEST_CONTAINER)) {
            int guestCount = driverTools.elements(GUEST_CONTAINER).get().size();
            logger.log("Guest count : " + guestCount);
        }

        List<WebElement> contactDetails = driverTools.elements(SAME_AS_CONTACT_TOGGLE_BTN).orElseThrow();
        for (WebElement contactDetail : contactDetails) {
            driverTools.click(contactDetail);
        }
        if (driverTools.isElementDisplayed(ADDITIONAL_DETAIL_TEXT)) {
            fillAdditionalDetail();
        }
    }

    public boolean isOrderPageOpen(){
        driverTools.waitForElementToAppear(TICKET_QTY, 20);
        Boolean result = driverTools.isElementDisplayed(TICKET_QTY);
        return result;
    }

    public boolean isBookingInfoDislayed(){
        driverTools.waitForElementToAppear(BOOKING_INFO);
        Boolean bookingInfo = driverTools.isElementDisplayed(BOOKING_INFO);
        Boolean contactDetail = driverTools.isElementDisplayed(CONTACT_DETAIL);
        Boolean priceDetail = driverTools.isElementDisplayed(PRICE_DETAIL_BTN);
        Assertion.assertThat("Booking information is displayed", bookingInfo, is(true));
        Assertion.assertThat("Contact detail is displayed", contactDetail, is(true));
        Assertion.assertThat("Price detail is displayed", priceDetail, is(true));
        return bookingInfo && contactDetail && priceDetail ;
    }

    public boolean isActivitiesInfoPresent(){
        driverTools.waitForElementToAppear(ACTIVITY_DETAILS);
        Boolean result = driverTools.isElementDisplayed(ACTIVITY_DETAILS);
        return result;
    }

    public String checkProductName(){
        driverTools.waitForElementToAppear(PRODUCT_NAME);
        String result = driverTools.getText(PRODUCT_NAME).orElseThrow();
        return result;
    }

    public void fillMultipleVisitorDetails(){
        driverTools.waitForElementToAppear(VISITOR_1_DETAILS);
        driverTools.clickWithJavascriptExecutor(VISITOR_1_DETAILS);

        if(driverTools.isElementPresent(VISITOR_2_DETAILS)){
            driverTools.waitForElementToAppear(VISITOR_2_DETAILS);
            driverTools.clickWithJavascriptExecutor(VISITOR_2_DETAILS);
        }
    }

    public void selectCountry (){
        driverTools.click(COUNTRY_SELECTOR);
        driverTools.sendKeys(COUNTRY_SELECTOR_SEARCH_BAR, "indonesia");
        driverTools.click(COUNTRY_SELECTOR_FIRST_RESULT);
    }

    public boolean isFreeSeatingDisplayedOnBookingPage() {
        driverTools.waitForElementToAppear(FREE_SEATING_BOOKING_PAGE, 5);
        return driverTools.isElementDisplayed(FREE_SEATING_BOOKING_PAGE);
    }
}