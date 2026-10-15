package com.tiket.web.dWeb.page.accommodation.hotels;

import com.tiket.apiPage.HotelApi;
import com.tiket.appium.PopupObserver;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.accommodation.hotel.payathotel.PayAtHotelRoom;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;

public class HotelDetailPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator HEADING_PDP = new Locator("HEADING_PDP", new By.ByXPath("//h1[@data-testid='name']"));
    private static final Locator PRICE_PDP = new Locator("PRICE_PDP", new By.ByXPath("//*[contains(@class,'HotelPriceInfo_price_detail')]//*[contains(@class, 'PriceModule_final_price_wrapper')]//*[contains(@class,'Text_variant_price')]"));
    private static final Locator REGION=new Locator("REGION", new By.ByXPath("(//a[@data-testid='breadcrumb-link-item'])[2]"));
    private static final Locator CITY=new Locator("CITY", new By.ByXPath("(//a[@data-testid='breadcrumb-link-item'])[3]"));
    private static final Locator AREA=new Locator("AREA", new By.ByXPath("(//a[@data-testid='breadcrumb-link-item'])[4]"));
    private static final Locator BOOKING_FULL_TEXT=new Locator("BOOKING_FULL_TEXT", new By.ByXPath("//p[contains(@class, 'ActionBanner_title') and contains(@class, 'Text_text') and contains(@class, 'Text_variant_highEmphasis')]"));
    private static final Locator HOTEL_IMAGE_CARD = new Locator("HOTEL_IMAGE_CARD", new By.ByXPath("(//div[contains(@class, 'RenderThumbnailImages')])[2]"));
    private static final Locator SEE_ROOM = new Locator("SEE_ROOM", new By.ByXPath("(//button[@data-testid='button-see-room'])[last()]"));
    private static final Locator HIGHLIGHTS = new Locator("SEE_ROOM", new By.ByXPath("(//div[contains(@class, 'Container_container')])[6]"));
    private static final Locator REVIEWS = new Locator("SEE_ROOM", new By.ByXPath("(//div[contains(@class, 'Container_container')])[7]"));
    private static final Locator POPULAR_FACILITIES = new Locator("SEE_ROOM", new By.ByXPath("(//div[contains(@class, 'Container_container')])[8]"));
    private static final Locator PDP_MAP = new Locator("SEE_ROOM", new By.ByXPath("(//div[contains(@class, 'Container_container')])[9]"));
    private static final Locator ACCOMMODATION_POLICIES = new Locator("SEE_ROOM", new By.ByXPath("(//div[contains(@class, 'Container_container')])[10]"));
    private static final Locator FAQ = new Locator("SEE_ROOM", new By.ByXPath("(//div[contains(@class, 'Container_container')])[12]"));
    private static final Locator BOOK_BTN = new Locator("BOOK_BTN", By.xpath("//button[@data-testid='room-card-button-book']"));
    private static final Locator POPUP_GOT_IT = new Locator("POPUP_GOT_IT", By.xpath("//button[@data-role='secondaryCtaClose']"));
    private static final Locator CHANGE_POPUP = new Locator("CHANGE_POPUP", By.xpath("//div[contains(@class,'Dialog_dialog') or contains(@class,'Dialog_pop_up')]"));
    private static final Locator CHANGE_POPUP_PRIMARY_BTN = new Locator("CHANGE_POPUP_PRIMARY_BTN", By.xpath("//div[contains(@class,'Dialog_dialog') or contains(@class,'Dialog_pop_up')]//button[contains(@class,'Button_variant_primary')]"));
    // Locators for recommendation card (non-refundable room selection)
    private static final Locator RECOMMENDATION_CARD = new Locator("RECOMMENDATION_CARD", By.xpath("//div[contains(@class, 'RecommendationCard_recommendation_card_container')]"));
    private static final Locator NON_REFUNDABLE_TEXT = new Locator("NON_REFUNDABLE_TEXT", By.xpath("(.//span[@data-testid='cancellation-policies-title' and contains(text(), 'Refund') and contains(text(), 'not allowed')])[1]"));
    private static final Locator RECOMMENDATION_BOOK_BTN = new Locator("RECOMMENDATION_BOOK_BTN", By.xpath(".//button[contains(@class, 'Button_variant_primary')]"));
    private static final Locator ROOM_INNER_CARD_NAME = new Locator("ROOM_INNER_CARD_NAME", By.xpath(".//h4[contains(@class, 'RoomInnerCard_room_name_title')]"));
    // Locators for rate plan cards
    private static final Locator RATE_PLAN_CARD = new Locator("RATE_PLAN_CARD", By.xpath("//div[@data-testid='rate-plan-card-container']"));
    private static final Locator RATE_PLAN_BOOK_BTN = new Locator("RATE_PLAN_BOOK_BTN", By.xpath(".//button[contains(@data-testid, 'book-button')]"));
    private static final Locator ROOM_GROUP_NAME = new Locator("ROOM_GROUP_NAME", By.xpath("./ancestor::div[contains(@class, 'RoomGroup')]//span[contains(@class, 'Text_variant_highEmphasis') and contains(@class, 'Text_size_b1') and contains(@class, 'Text_weight_bold')]"));
    private static final Locator ROOM_GROUP_NAME_ALT = new Locator("ROOM_GROUP_NAME_ALT", By.xpath("./ancestor::div[contains(@class, 'CardWithNudge')]//preceding-sibling::div//span[contains(@class, 'Text_weight_bold') and contains(@class, 'Text_size_b1')]"));
    private static final Locator ROOM_ALT = new Locator("ROOM_ALT", By.xpath("./ancestor::div[contains(@class, 'RoomGroup') or contains(@class, 'room_group')]//span[contains(@class, 'Text_weight_bold') and contains(@class, 'Text_size_b1')]"));
    private static final Locator PROMO_LIST=new Locator("PROMO_LIST",By.xpath("//div[contains(@class, 'InfinityPromoList_wrapper__')]"));
    private static final Locator PROMO_COPY_BUTTON=new Locator("PROMO_COPY_BUTTON",By.xpath("(//p[contains(@class, 'InfinityPromoCard_code_text__')])[1]"));
    private static final Locator PROMO_SECTION_COPY_BTN=new Locator("PROMO_SECTION_COPY_BTN",By.xpath("(//button[@data-testid='copy-promo-code-button'])[1]"));
    private static final Locator COPIED_PROMO_CODE=new Locator("COPIED_PROMO_CODE",By.xpath("(//div[contains(@class, 'InfinityPromoCard_success_view')]//p)[1]"));
    private static final Locator SEE_ALL_BUTTON=new Locator("SEE_ALL_BUTTON",By.xpath("//div[contains(@class, 'InfinityPromoList_see_all_icon')]"));
    private static final Locator PROMO_SECTION=new Locator("PROMO_SECTION",By.xpath("//div[contains(@class, 'InfinityPromoList_list_wrapper__')]"));
    private static final Locator PROMO_CODE_COPIED=new Locator("PROMO_CODE_COPIED",By.xpath("//span[contains(@class, 'Ripple_ripple__') and contains(@class, 'Ripple_variant_secondary__')]"));
    private static final Locator CASHBACK_PRICE = new Locator("CASHBACK_PRICE",By.xpath("//div[@data-testid='tix-points-container']"));
    private static final Locator WISHLIST_HEART_COLOR = new Locator("WISHLIST_HEART_COLOR",By.xpath("//button[contains(@class,'wishlist_icon_btn')]//*[local-name()='circle']"));

    public HotelDetailPage(Session<T> session) {
        super(session);
    }

    /**
     * When {@link #openFirstAvailablePayAtHotelPdp(String, int, int, int)} returns {@code null}, explains why
     * (empty PAH summary vs. every candidate PDP failed to render See Hotel Room / showed no
     * bookable room). Cleared when a candidate succeeds.
     */
    private String lastPayAtHotelPdpOpenFailureDetail;

    /**
     * Failure hint for assertion messages when {@link #openFirstAvailablePayAtHotelPdp(String, int, int, int)} returns {@code null}.
     */
    public String getLastPayAtHotelPdpOpenFailureDetail() {
        return lastPayAtHotelPdpOpenFailureDetail;
    }

    public void switchToNewTab() {
        driverTools.switchToNewTab();
    }

    public String getHotelNameDetail() {
        WebElement headingText = driverTools.waitForElementToAppear(HEADING_PDP).orElseThrow();
        return driverTools.getText(headingText);
    }

    public String getHotelCityDetail() {
        WebElement cityText = driverTools.waitForElementToAppear(CITY).orElseThrow();
        return driverTools.getText(cityText);
    }

    public String getHotelAreaDetail() {
        WebElement areaText = driverTools.waitForElementToAppear(AREA).orElseThrow();
        return driverTools.getText(areaText);
    }

    public String getHotelRegionDetail() {
        WebElement regionText = driverTools.waitForElementToAppear(REGION).orElseThrow();
        return driverTools.getText(regionText);
    }

    public boolean isRoomsAvailable(){
        driverTools.waitForElementToAppear(BOOKING_FULL_TEXT,5);
        return !driverTools.isElementDisplayed(BOOKING_FULL_TEXT);
    }

    public String getHotelPriceDetail() {
        WebElement priceFooter = driverTools.waitForElementToAppear(PRICE_PDP).orElseThrow();
        return driverTools.getText(priceFooter);
    }

    public String getBookingFullDetail() {
        WebElement headingText = driverTools.waitForElementToAppear(BOOKING_FULL_TEXT).orElseThrow();
        return driverTools.getText(headingText);
    }

    public boolean isHotelImageVisible(){
        driverTools.waitForElementToAppear(HOTEL_IMAGE_CARD,5);
        return driverTools.isElementDisplayed(HOTEL_IMAGE_CARD);
    }

    public boolean isSeeRoomVisible(){
        driverTools.waitForElementToAppear(SEE_ROOM,5);
        return driverTools.isElementDisplayed(SEE_ROOM);
    }

    public boolean isHighLightsVisible(){
        driverTools.waitForElementToAppear(HIGHLIGHTS,5);
        return driverTools.isElementDisplayed(HIGHLIGHTS);
    }

    public boolean isReviewsVisible(){
        driverTools.waitForElementToAppear(REVIEWS,5);
        return driverTools.isElementDisplayed(REVIEWS);
    }

    public boolean isPopularFacilitiesVisible(){
        driverTools.waitForElementToAppear(POPULAR_FACILITIES,5);
        return driverTools.isElementDisplayed(POPULAR_FACILITIES);
    }

    public boolean isPdpMapVisible(){
        driverTools.waitForElementToAppear(PDP_MAP,5);
        return driverTools.isElementDisplayed(PDP_MAP);
    }

    public boolean isAccommodationPoliciesVisible(){
        driverTools.waitForElementToAppear(ACCOMMODATION_POLICIES,5);
        return driverTools.isElementDisplayed(ACCOMMODATION_POLICIES);
    }

    public boolean isFAQVisible(){
        driverTools.waitForElementToAppear(FAQ,5);
        return driverTools.isElementDisplayed(FAQ);
    }

    public void seeHotelRoom(){
        driverTools.waitForElementToPresence(SEE_ROOM,20);
        driverTools.clickWithJavascriptExecutor(SEE_ROOM);
    }

    public boolean isPdpLoaded(){
        driverTools.waitForElementToAppear(SEE_ROOM,5);
        driverTools.waitForElementToAppear(HOTEL_IMAGE_CARD,5);
        return  driverTools.waitForElementToPresence(HEADING_PDP,5).isPresent();
    }

    public void goToPreviousPage(){
        driverTools.back();
    }

    /**
     * Navigates the browser directly to the given PDP URL (e.g. a URL produced by
     * {@code HotelApi#findPayAtHotelRoom(...)}) and waits for the PDP to render.
     */
    public void openByUrl(String url) {
        driverTools.get(url);
        driverTools.waitForElementToPresence(HEADING_PDP, 20);
    }

    /**
     * PAH summary URLs from {@code HotelApi#buildPahHotelUrl} only include {@code checkin}.
     * dWeb PDP expects {@code checkin}, {@code checkout}, and {@code night} for consistent room loading.
     */
    private static String normalizePahPdpUrl(String url, int totalNight, String fallbackCheckinYyyyMmDd) {
        String checkinRaw = firstQueryParam(url, "checkin");
        if (checkinRaw == null || checkinRaw.isEmpty()) {
            checkinRaw = fallbackCheckinYyyyMmDd;
        }
        LocalDate checkin = LocalDate.parse(checkinRaw);
        LocalDate checkout = checkin.plusDays(totalNight);
        URI uri = URI.create(url);
        String base = uri.getScheme() + "://" + uri.getAuthority() + uri.getPath();
        return base + "?checkin=" + checkin + "&checkout=" + checkout + "&night=" + totalNight;
    }

    private static String firstQueryParam(String url, String name) {
        String query = URI.create(url).getRawQuery();
        if (query == null || query.isEmpty()) {
            return null;
        }
        for (String pair : query.split("&")) {
            int eq = pair.indexOf('=');
            if (eq <= 0) {
                continue;
            }
            String key = pair.substring(0, eq);
            if (!name.equals(key)) {
                continue;
            }
            String value = pair.substring(eq + 1);
            return URLDecoder.decode(value, StandardCharsets.UTF_8);
        }
        return null;
    }

    /**
     * dWeb analogue of {@code HotelPage#openFirstAvailablePayAtHotelPdpDeeplink} used by the
     * Appium PAH tests. Does the full PAH resolve-and-navigate flow in one call:
     * <ol>
     *   <li>Generates the {@code === PAH Hotel Summary ===} via
     *       {@code HotelApi#findPayAtHotelRoom(...)} using the supplied search params.</li>
     *   <li>Iterates the returned candidates and, for each one, navigates to
     *       {@link PayAtHotelRoom#getUrl()} and verifies the PDP rendered correctly — PDP
     *       heading present <b>and</b> the <i>See Hotel Room</i> button is visible (this is the
     *       gate the user called out — a PDP that doesn't expose See Hotel Room is treated as
     *       not-loaded and skipped).</li>
     *   <li>Also verifies the hotel is not fully booked via {@link #isRoomsAvailable()}.</li>
     * </ol>
     * Stops on the first candidate that satisfies all checks and returns it; returns
     * {@code null} when the summary was empty or every candidate failed. In the null case
     * {@link #getLastPayAtHotelPdpOpenFailureDetail()} is populated with a human-readable reason
     * that test assertions can surface.
     *
     * @param dateRange   day-offset range passed to the PAH finder, e.g. {@code "15-18"}.
     * @param totalNight  nights value fed to the PAH finder (matches Appium {@code totalNight}).
     * @param totalRoom   rooms value fed to the PAH finder.
     * @param totalAdult  adults value fed to the PAH finder.
     */
    @SneakyThrows
    public PayAtHotelRoom openFirstAvailablePayAtHotelPdp(
            String dateRange, int totalNight, int totalRoom, int totalAdult) {
        lastPayAtHotelPdpOpenFailureDetail = null;
        HotelApi<T> hotelApi = new HotelApi<>(session);
        List<PayAtHotelRoom> candidates = hotelApi.findPayAtHotelRoom(
                dateRange, String.valueOf(totalNight),
                String.valueOf(totalRoom), String.valueOf(totalAdult));

        if (candidates.isEmpty()) {
            lastPayAtHotelPdpOpenFailureDetail =
                    "Pay at Hotel: PAH Hotel Summary returned 0 rows — the backend scan found no PAH "
                            + "availability for the current env / dateRange / vendor list (nothing to open on PDP).";
            logger.log("[PAH][dWeb] findPayAtHotelRoom returned 0 candidates — nothing to iterate.");
            return null;
        }

        int attempted = 0;
        int pdpNotLoaded = 0;
        int roomsUnavailable = 0;

        for (PayAtHotelRoom candidate : candidates) {
            attempted++;
            logger.log("[PAH][dWeb] Candidate " + attempted + "/" + candidates.size() + " -> "
                    + candidate.vendorExternalIdLabel() + " (" + candidate.getHotelName() + ") "
                    + candidate.getEarliestPahDate() + " | " + candidate.getUrl());

            try {
                openByUrl(normalizePahPdpUrl(candidate.getUrl(), totalNight, candidate.getEarliestPahDate()));
            } catch (Exception e) {
                pdpNotLoaded++;
                logger.log("[PAH][dWeb] Navigation failed for " + candidate.vendorExternalIdLabel()
                        + ": " + e.getMessage() + " — trying next candidate.");
                continue;
            }

            boolean pdpHeadingLoaded = isPdpLoaded();
            boolean seeRoomButtonVisible = isSeeRoomVisible();
            if (!pdpHeadingLoaded || !seeRoomButtonVisible) {
                pdpNotLoaded++;
                logger.log("[PAH][dWeb] PDP did not load properly for " + candidate.vendorExternalIdLabel()
                        + " (pdpHeadingLoaded=" + pdpHeadingLoaded
                        + ", seeRoomButtonVisible=" + seeRoomButtonVisible
                        + ") — trying next candidate.");
                continue;
            }

            if (!isRoomsAvailable()) {
                roomsUnavailable++;
                logger.log("[PAH][dWeb] PDP loaded but hotel is fully booked on " + candidate.getEarliestPahDate()
                        + " for " + candidate.vendorExternalIdLabel() + " — trying next candidate.");
                continue;
            }

            lastPayAtHotelPdpOpenFailureDetail = null;
            logger.log("[PAH][dWeb] Found available PAH PDP -> " + candidate.vendorExternalIdLabel()
                    + " / " + candidate.getHotelName());
            return candidate;
        }

        lastPayAtHotelPdpOpenFailureDetail = String.format(
                "Pay at Hotel: PAH Hotel Summary listed %d candidate hotel(s), but none of them rendered a "
                        + "usable PDP. PDP failed to load (no See Hotel Room / PDP heading): %d; "
                        + "PDP loaded but hotel was fully booked on the PAH date: %d. "
                        + "Causes to check: logged-in as Diamond (LV4), currency/language session, stale "
                        + "inventory, or the URL format produced by findPayAtHotelRoom no longer matches dWeb.",
                candidates.size(), pdpNotLoaded, roomsUnavailable);
        logger.log("[PAH][dWeb] Iterated " + candidates.size() + " candidate(s); none produced a bookable PAH PDP.");
        return null;
    }

    public void selectHotelRoom(int roomIndex) {
        roomIndex = roomIndex - 1;
        driverTools.waitForElementToAppear(BOOK_BTN);
        List<WebElement> roomButtons = driverTools.elements(BOOK_BTN).orElseThrow();
        if (roomIndex < 0 || roomIndex >= roomButtons.size()) {
            throw new IndexOutOfBoundsException("Room index out of bounds: " + roomIndex + ". Available rooms: " + roomButtons.size());
        }
        driverTools.click(roomButtons.get(roomIndex));
    }

    public void clickPopUp() {
        PopupObserver.inject(driver, List.of(POPUP_GOT_IT));
    }

    /**
     * Captures the text of a rate / cancellation-policy change popup that may appear on the PDP
     * (e.g. after the room rate or cancellation policy was updated via the supply allotments API).
     * Returns an empty string when no popup is shown.
     */
    public String getChangePopupText() {
        return driverTools.waitForElementToPresence(CHANGE_POPUP, 15)
                .map(element -> {
                    String text = driverTools.getText(element);
                    logger.log("PDP change popup text -> " + text);
                    return text;
                })
                .orElseGet(() -> {
                    logger.log("No change popup appeared on PDP");
                    return "";
                });
    }

    /** Dismisses the PDP change popup via its primary button when present. */
    public void dismissChangePopupIfPresent() {
        driverTools.waitForElementToAppear(CHANGE_POPUP_PRIMARY_BTN, 5)
                .ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public String selectNonRefundableRoom() {
        int maxSwipes = 15;
        int swipeCount = 0;
        
        while (swipeCount < maxSwipes) {
            logger.log("Searching for non-refundable room - swipe attempt: " + (swipeCount + 1));

            if (driverTools.waitForElementToAppear(RATE_PLAN_CARD, 3).isPresent()) {
                List<WebElement> ratePlanCards = driverTools.elements(RATE_PLAN_CARD).orElse(List.of());
                logger.log("Found " + ratePlanCards.size() + " rate plan cards");
                
                for (WebElement card : ratePlanCards) {
                    try {
                        List<WebElement> nonRefundableElements = card.findElements(NON_REFUNDABLE_TEXT.by());
                        if (!nonRefundableElements.isEmpty()) {
                            String roomName = getRoomNameFromCard(card);
                            logger.log("Found non-refundable room in rate plan card: " + roomName);
                            
                            WebElement bookButton = card.findElement(RATE_PLAN_BOOK_BTN.by());
                            driverTools.scrollIntoWebView(bookButton);
                            driverTools.click(bookButton);
                            logger.log("Clicked Book button for non-refundable room: " + roomName);
                            return roomName;
                        }
                    } catch (Exception e) {
                        logger.log("Error checking rate plan card: " + e.getMessage());
                    }
                }
            }

            logger.log("Non-refundable room not found in current view, swiping up...");
            try {
                driverTools.scroll(3, 300);
                driverTools.dummyWait(1);
            } catch (InterruptedException e) {
                logger.log("Interrupted while scrolling: " + e.getMessage());
            }
            swipeCount++;
        }
        
        throw new RuntimeException("No non-refundable room found on the page after " + maxSwipes + " swipes");
    }


    private String getRoomNameFromCard(WebElement card) {
        try {
            WebElement roomNameElement = driverTools.element(ROOM_INNER_CARD_NAME).orElseThrow();
            String roomName = roomNameElement.getText().trim();
            if (!roomName.isEmpty()) {
                logger.log("Found room name in RoomInnerCard: " + roomName);
                return roomName;
            }
        } catch (Exception e) {
            logger.log("Room name not found in RoomInnerCard structure, trying RoomGroup");
        }

        try {
            WebElement roomNameElement = driverTools.element(ROOM_GROUP_NAME).orElseThrow();
            String roomName = roomNameElement.getText().trim();
            if (!roomName.isEmpty()) {
                logger.log("Found room name in RoomGroup ancestor: " + roomName);
                return roomName;
            }
        } catch (Exception e) {
            logger.log("Could not find room name in RoomGroup ancestor");
        }

        try {
            WebElement roomNameElement = driverTools.element(ROOM_GROUP_NAME_ALT).orElseThrow();
            String roomName = roomNameElement.getText().trim();
            if (!roomName.isEmpty()) {
                logger.log("Found room name in CardWithNudge sibling: " + roomName);
                return roomName;
            }
        } catch (Exception e) {
            logger.log("Could not find room name in CardWithNudge sibling");
        }

        try {
            WebElement parentContainer = driverTools.element(ROOM_ALT).orElseThrow();
            String roomName = parentContainer.getText().trim();
            logger.log("Found room name with fallback locator: " + roomName);
            return roomName;
        } catch (Exception ex) {
            logger.log("Could not find room name with any locator: " + ex.getMessage());
            return "Unknown Room";
        }
    }
    public boolean isPromoListLoaded(){
        return  driverTools.waitForElementToPresence(PROMO_LIST,5).isPresent();
    }

    public void clickPromoCopyButton(){
        driverTools.waitForElementsToAppear(PROMO_COPY_BUTTON, 5);
        if(driverTools.isElementDisplayed(PROMO_COPY_BUTTON)){
            driverTools.clickWithJavascriptExecutor(PROMO_COPY_BUTTON);
        }
    }

    public boolean isPromoCopied(){
        String promo=driverTools.waitForElementToAppear(COPIED_PROMO_CODE,5).orElseThrow().getText();
        return  promo.contains("Promo code copied.") || promo.contains("Kode promo berhasil disalin.");
    }

    public void scrollToRightOfPromo() {
        driverTools.scrollIntoWebView(PROMO_LIST);
        driverTools.waitForElementToPresence(PROMO_LIST,5).orElseThrow();
    }

    public void clickSeeAllPromo() {
        driverTools.clickWithJavascriptExecutor(SEE_ALL_BUTTON);
    }

    public boolean isPromoSectionLoaded() {
        return  driverTools.waitForElementToPresence(PROMO_SECTION,5).isPresent();
    }

    public boolean isPromoCopiedOnPromoSection() {
        return  driverTools.waitForElementToPresence(PROMO_CODE_COPIED,20).isPresent();
    }

    public void clickPromoCopyButtonOnSection() {
        driverTools.waitForElementsToAppear(PROMO_SECTION_COPY_BTN,5);
        if(driverTools.isElementDisplayed(PROMO_SECTION_COPY_BTN)){
            driverTools.click(PROMO_SECTION_COPY_BTN);
        }
    }

    public String getTotalCashback(){
        driverTools.waitForElementsToAppear(CASHBACK_PRICE);
        String text = driverTools.getText(CASHBACK_PRICE).orElseThrow();
        String value = text.replaceAll("[^0-9]", "");
        logger.log("CashBack Value : "+ value);
        return value;
    }

    public void backToSrp(){
        driverTools.closeCurrentTab();
        driverTools.tabSwitch("/hotel/search");
        driverTools.refreshWebpage();
    }

    public boolean isHeartIconColouredRed() {
        WebElement element = driverTools.waitForElementToAppear(WISHLIST_HEART_COLOR).orElseThrow();
        String opacity = element.getAttribute("fill-opacity");
        Assertion.assertThat("Heart icon to be colored red with fill-opacity 1", opacity, is("1"));
        return "1".equals(opacity);
    }

    public boolean isHeartIconColouredGrey() {
        WebElement element = driverTools.waitForElementToAppear(WISHLIST_HEART_COLOR).orElseThrow();
        String opacity = element.getAttribute("fill-opacity");
        Assertion.assertThat("Heart icon to be colored grey with fill-opacity 0", opacity, is("0"));
        return "0".equals(opacity);
    }
}

