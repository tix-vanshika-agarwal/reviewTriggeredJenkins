package com.tiket.page.accommodation.hotel.searchresultpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Session;
import com.tiket.pojo.accommodation.homes.HomesData;
import com.tiket.pojo.accommodation.hotel.FilterInfo;
import com.tiket.pojo.accommodation.hotel.LandingPageModulePOJO;
import com.tiket.pojo.accommodation.master_tag.AccommodationMasterTagResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPageIOS.LOADING_STATE;
import static org.hamcrest.Matchers.*;

public abstract class SearchResultPage <T extends WebDriver> extends BasePage<T> {

    protected static final int UNAVAILABLE_PRICE = -1;

    public SearchResultPage(Session<T> session) { super(session); }

    public static <T extends WebDriver> SearchResultPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new SearchResultPageAndroid<>(session); }
            case IOS -> { return new SearchResultPageIOS<>(session); }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    protected static final String DEFAULT_CURRENCY = "IDR";

    /** Price ranges (min, max) per currency for &lt;200, &lt;500, 500-1000, &gt;1000. Values in smallest unit (e.g. IDR rupiah, USD dollars). */
    protected static final Map<String, Map<String, int[]>> PRICE_RANGES_BY_CURRENCY = buildPriceRangesByCurrency();

    private static Map<String, Map<String, int[]>> buildPriceRangesByCurrency() {
        Map<String, Map<String, int[]>> m = new java.util.HashMap<>();
        m.put("IDR", Map.of("<200", new int[]{0, 200_000}, "<500", new int[]{0, 500_000}, "500-1000", new int[]{500_000, 1_000_000}, ">1000", new int[]{1_000_000, 99_999_999}));
        m.put("USD", Map.of("<200", new int[]{0, 200}, "<500", new int[]{0, 500}, "500-1000", new int[]{500, 1_000}, ">1000", new int[]{1_000, 999_999}));
        m.put("SGD", Map.of("<200", new int[]{0, 200}, "<500", new int[]{0, 500}, "500-1000", new int[]{500, 1_000}, ">1000", new int[]{1_000, 999_999}));
        m.put("AUD", Map.of("<200", new int[]{0, 200}, "<500", new int[]{0, 500}, "500-1000", new int[]{500, 1_000}, ">1000", new int[]{1_000, 999_999}));
        m.put("CAD", Map.of("<200", new int[]{0, 200}, "<500", new int[]{0, 500}, "500-1000", new int[]{500, 1_000}, ">1000", new int[]{1_000, 999_999}));
        m.put("CHF", Map.of("<200", new int[]{0, 200}, "<500", new int[]{0, 500}, "500-1000", new int[]{500, 1_000}, ">1000", new int[]{1_000, 999_999}));
        m.put("CNY", Map.of("<200", new int[]{0, 1_400}, "<500", new int[]{0, 3_500}, "500-1000", new int[]{3_500, 7_000}, ">1000", new int[]{7_000, 999_999}));
        m.put("EUR", Map.of("<200", new int[]{0, 200}, "<500", new int[]{0, 500}, "500-1000", new int[]{500, 1_000}, ">1000", new int[]{1_000, 999_999}));
        m.put("GBP", Map.of("<200", new int[]{0, 200}, "<500", new int[]{0, 500}, "500-1000", new int[]{500, 1_000}, ">1000", new int[]{1_000, 999_999}));
        m.put("HKD", Map.of("<200", new int[]{0, 1_500}, "<500", new int[]{0, 4_000}, "500-1000", new int[]{4_000, 8_000}, ">1000", new int[]{8_000, 999_999}));
        m.put("JPY", Map.of("<200", new int[]{0, 30_000}, "<500", new int[]{0, 75_000}, "500-1000", new int[]{75_000, 150_000}, ">1000", new int[]{150_000, 99_999_999}));
        m.put("MYR", Map.of("<200", new int[]{0, 900}, "<500", new int[]{0, 2_250}, "500-1000", new int[]{2_250, 4_500}, ">1000", new int[]{4_500, 999_999}));
        m.put("NZD", Map.of("<200", new int[]{0, 200}, "<500", new int[]{0, 500}, "500-1000", new int[]{500, 1_000}, ">1000", new int[]{1_000, 999_999}));
        m.put("PHP", Map.of("<200", new int[]{0, 11_000}, "<500", new int[]{0, 28_000}, "500-1000", new int[]{28_000, 56_000}, ">1000", new int[]{56_000, 99_999_999}));
        m.put("THB", Map.of("<200", new int[]{0, 7_000}, "<500", new int[]{0, 18_000}, "500-1000", new int[]{18_000, 36_000}, ">1000", new int[]{36_000, 99_999_999}));
        m.put("VND", Map.of("<200", new int[]{0, 5_000_000}, "<500", new int[]{0, 12_500_000}, "500-1000", new int[]{12_500_000, 25_000_000}, ">1000", new int[]{25_000_000, 999_999_999}));
        return Map.copyOf(m);
    }

    /** For "random" filter: { minBase, step } per currency (min as low as possible, 100-based). */
    protected static final Map<String, int[]> RANDOM_PRICE_BASE_BY_CURRENCY = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("IDR", new int[]{0, 100}),
            new AbstractMap.SimpleEntry<>("USD", new int[]{0, 100}),
            new AbstractMap.SimpleEntry<>("SGD", new int[]{0, 100}),
            new AbstractMap.SimpleEntry<>("AUD", new int[]{0, 100}),
            new AbstractMap.SimpleEntry<>("CAD", new int[]{0, 100}),
            new AbstractMap.SimpleEntry<>("CHF", new int[]{0, 100}),
            new AbstractMap.SimpleEntry<>("CNY", new int[]{0, 100}),
            new AbstractMap.SimpleEntry<>("EUR", new int[]{0, 100}),
            new AbstractMap.SimpleEntry<>("GBP", new int[]{0, 100}),
            new AbstractMap.SimpleEntry<>("HKD", new int[]{0, 100}),
            new AbstractMap.SimpleEntry<>("JPY", new int[]{0, 1000}),
            new AbstractMap.SimpleEntry<>("MYR", new int[]{0, 100}),
            new AbstractMap.SimpleEntry<>("NZD", new int[]{0, 100}),
            new AbstractMap.SimpleEntry<>("PHP", new int[]{0, 100}),
            new AbstractMap.SimpleEntry<>("THB", new int[]{0, 100}),
            new AbstractMap.SimpleEntry<>("VND", new int[]{0, 10000})
    );

    /** Max cap for random price filter per currency. */
    protected static final Map<String, Integer> RANDOM_MAX_CAP_BY_CURRENCY = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("IDR", 99_999_999),
            new AbstractMap.SimpleEntry<>("USD", 999_999),
            new AbstractMap.SimpleEntry<>("SGD", 999_999),
            new AbstractMap.SimpleEntry<>("AUD", 999_999),
            new AbstractMap.SimpleEntry<>("CAD", 999_999),
            new AbstractMap.SimpleEntry<>("CHF", 999_999),
            new AbstractMap.SimpleEntry<>("CNY", 999_999),
            new AbstractMap.SimpleEntry<>("EUR", 999_999),
            new AbstractMap.SimpleEntry<>("GBP", 999_999),
            new AbstractMap.SimpleEntry<>("HKD", 999_999),
            new AbstractMap.SimpleEntry<>("JPY", 99_999_999),
            new AbstractMap.SimpleEntry<>("MYR", 999_999),
            new AbstractMap.SimpleEntry<>("NZD", 999_999),
            new AbstractMap.SimpleEntry<>("PHP", 99_999_999),
            new AbstractMap.SimpleEntry<>("THB", 99_999_999),
            new AbstractMap.SimpleEntry<>("VND", 999_999_999)
    );

    /** Normalize currency code (e.g. "R" -> "IDR", blank -> DEFAULT_CURRENCY). */
    protected static String normalizeCurrency(String currency) {
        if (currency == null || currency.isBlank()) return DEFAULT_CURRENCY;
        return "R".equalsIgnoreCase(currency.trim()) ? "IDR" : currency.trim().toUpperCase();
    }

    /** Returns [min, max] for the given price range and currency. Use for &lt;200, &lt;500, 500-1000, &gt;1000 only. */
    protected int[] getPriceRange(String priceRange, String currency) {
        String c = normalizeCurrency(currency);
        Map<String, int[]> ranges = PRICE_RANGES_BY_CURRENCY.get(c);
        if (ranges == null) {
            logger.fail("Unsupported currency for price filter: " + currency + ". Supported: " + PRICE_RANGES_BY_CURRENCY.keySet());
            return new int[]{0, 0};
        }
        int[] range = ranges.get(priceRange);
        if (range == null) {
            logger.fail("wrong argument priceRange '" + priceRange + "', please check you testdata.xlsx");
            return new int[]{0, 0};
        }
        return range;
    }

    /** Returns [minBase, step, maxCap] for "random" filter. Min is as low as possible (minBase); caller should set max = minBase + step * random(1..(maxCap-minBase)/step). */
    protected int[] getRandomPriceRangeBounds(String currency) {
        String c = normalizeCurrency(currency);
        int[] base = RANDOM_PRICE_BASE_BY_CURRENCY.getOrDefault(c, new int[]{0, 100});
        int minBase = base[0];
        int step = base[1];
        int maxCap = RANDOM_MAX_CAP_BY_CURRENCY.getOrDefault(c, 99_999_999);
        return new int[]{minBase, step, maxCap};
    }

    protected String[] minMaxPrice(String priceRange, String currency) {
        String minPrice, maxPrice;
        if ("random".equals(priceRange)) {
            int[] bounds = getRandomPriceRangeBounds(currency);
            int minBase = bounds[0];
            int step = bounds[1];
            int maxCap = bounds[2];
            minPrice = String.valueOf(minBase);
            int stepsToMax = Math.max(1, (maxCap - minBase) / step);
            int maxVal = minBase + step * generateRandomInt(1, stepsToMax);
            maxPrice = String.valueOf(Math.min(maxVal, maxCap));
        } else {
            int[] range = getPriceRange(priceRange, currency);
            minPrice = String.valueOf(range[0]);
            maxPrice = String.valueOf(range[1]);
        }
        return new String[]{minPrice, maxPrice};
    }

    public abstract boolean isHotelNameAdsVisible(String hotelName);
    public abstract String getSrpBanner();
    public abstract boolean checkBannerChanges(String initBanner);
    public abstract boolean checkHighlightedResult();
    public abstract String swipeBanner(Swipe swipe);
    public abstract void clickBanner();
    public abstract String getRedirectionBanner();
    public abstract void scrollToBannerSection();
    public abstract void clickBannerByTitle(String bannerTitle);
    public abstract void navigateBackToSrp();
    public abstract void selectHotelVendor(String vendor);
    public abstract void changeLocation(String newLocation);
    public abstract void changeLocation(String newLocation, String locationType);
    public abstract String changeNearMeLocation();
    public abstract void clickSubmit();
    public abstract String getCurrentLocation();
    public abstract boolean verifyLoopingHeaderQuickFilter(int index);
    public abstract String getPriceDescription();
    public abstract void clickFilterButton();
    public abstract void swipeToFilter(String filterName);
    public abstract void selectHotelStarFilter(String star);
    public abstract void selectHotelPaymentMethod(String paymentMethod);
    public abstract void clickFilterSubmit();
    public abstract String[] getHotelCardList(String cardAttribute,int totalHotel, String[] param);
    public abstract boolean checkHotelCardList(String cardAttribute,int totalHotel, String[] param);
    public abstract void clickSeeAllBtnFilter(String filterName);
    public abstract String[] selectAreaFilter(String area);
    public abstract String[] selectLocationFilter(String locationType, String locationName);
    public abstract String selectPropertyRatingFilter(String rating);
    public abstract void applyAdvanceFilter(String priceRange,String benefit,String facility,String currency);
    public abstract void checkBenefitAndFacility(String benefit,String facility,String totalHotel);
    public abstract void checkPerNightBtnFilter(String filterName);
    public abstract boolean isPriceAfterTaxFilterApplied();
    public abstract void clickWishlistFirstHotelCardForRemoval();

    /**
     docs:
     method for specific number of card or random
     add "list" param on test data to select the card number
     */

    public abstract String getHotelNameList(String list);
    public abstract String getHotelLocationList(String list);
    public abstract void selectHotelList(String list);
    public abstract boolean isSearchResultShown();
    public abstract boolean isHeaderSearchResultShown();
    public abstract void openPriceFilter();
    public abstract String selectPriceFilterByRange( String priceRange,String currency);
    public abstract boolean isPriceFilterByRangeCorrect(String priceRange, String totalHotel);
    public abstract String verifyCorrectSRP(String chip);

    /**
     docs:
     method for 1st hotel card based only
     does not require to add "list" param on test data
     */

    public abstract void selectFirstHotelCard();
    public abstract void selectFirstHotelCardFromPrice();
    public abstract Optional<WebElement> goToFirstHotelCard();
    public abstract String getFirstHotelName();
    public abstract String getFirstHotelLocation();
    public abstract String getFirstHotelPrice();
    public abstract boolean getFirstHotelStar();
    public abstract String getFirstHotelReview();
    public abstract Boolean istFirstHotelReviewDisplayed();
    public abstract void swipeToFirstCard();

    /**
     * Guest review line (rating text) on the Nth listing card when counting from the top of the SRP results.
     * {@code cardIndex} is 1-based.
     */
    public abstract Boolean isNthHotelReviewDisplayed(int cardIndex);

    public abstract String getNthHotelReview(int cardIndex);

    /**
     * Scans cards {@code 1} through {@code maxCardsToScan} (inclusive, top of SRP order) and returns the first
     * visible, non-blank guest review line text, or {@code null} if none is found.
     */
    public String findReviewLineAmongFirstCards(int maxCardsToScan) {
        for (int cardIndex = 1; cardIndex <= maxCardsToScan; cardIndex++) {
            if (Boolean.TRUE.equals(isNthHotelReviewDisplayed(cardIndex))) {
                String candidate = normalizeNonBreakingSpaceText(getNthHotelReview(cardIndex));
                if (candidate != null && !candidate.isBlank()) {
                    return candidate;
                }
            }
        }
        return null;
    }

    public abstract void filterSortBy(String filterType, String hotelCount);
    public abstract Boolean isFirstHotelNameDisplayed();
    public abstract Boolean isFirstHotelPriceDisplayed();
    public abstract void selectFilterStar(String star);
    public abstract void selectFilterStarRange(String starRange);
    public abstract void selectFilterSortBy(String filterType);
    public abstract Boolean isSortByLowestPriceValid(String totalHotel);
    public abstract String getDateRoomGuestDetail();
    public abstract String getCheckInOutDate();
    public abstract int getTotalGuestHeader();
    public abstract void clickBackButton();
    public abstract void checkPopupAndClickBackBtn();
    public abstract boolean istFirstHotelLocationDisplayed();
    public abstract boolean  isAddedBenefitVisible();
    public abstract boolean isBenefitVisible(String benefitData, int maxHotelsToCheck);
    public abstract FilterInfo findAndChooseInterceptFilter();
    public abstract boolean isFilterActivated(String filterType);
    public abstract void zoomMaps(boolean zoomIn);
    public void zoomMaps(int numberOfZoomOut, int numberOfZoomIn) {
        for (int i = 0; i < numberOfZoomOut; i++) {
            logger.log("Zooming out " + (i + 1) + " times");
            zoomMaps(false);
        }
        for (int i = 0; i < numberOfZoomIn; i++) {
            logger.log("Zooming in " + (i + 1) + " times");
            zoomMaps(true);
        }
    }
    public abstract void swipeMaps(Swipe swipe, int numberOfSwipe);
    public abstract void searchThisArea();
    public abstract String getVisibleHotelPrice();
    public abstract String getVisibleHotelPriceMaps();
    public abstract void searchPrevious();
    public abstract void swipeHotelCardItem();
    public abstract boolean isHotelNameCardVisibleOnMaps();
    public abstract String selectVisibleHotelItem();
    public abstract void clickSeeMapButton();
    public abstract String[] getHotelPriceMaps();
    public abstract String getHotelPriceMapsText();
    public abstract boolean isHotelStarCardVisibleOnMaps();
    public abstract String getHotelPriceCard(String priceText, boolean isMaps);
    public abstract void selectHotelPriceDisplay(String filterOption);
    public abstract void selectBenefit(String benefitType);
    public abstract void selectFacility(String facilityType);
    public abstract void changePriceFilter(String newFilterPrice);
    public abstract boolean isPriceFilterByManualCorrect(String priceRange, String totalHotel);
    public abstract void handleMultipleFilter(String[] filterTypes, String[] filterOptions);
    public abstract boolean checkMasterTags(AccommodationMasterTagResponse arrays, int totalMasterTag, String groupFilterName);
    public abstract void selectMasterTag(String name);
    public abstract String selectMasterTagByIndex(int index);
    public abstract boolean isSelectedMasterTagDuplicate(String masterTag);
    public abstract boolean checkSubtitleMasterTag(String groupFilterSubtitle);
    public abstract boolean checkEmptyHotelList();
    public abstract void goToCardWithLoyaltyBenefitBadge();

    /**
     * Opens the SRP filter sheet, applies the {@code Preferred Partner} and
     * {@code Preferred Partner Plus} benefit toggles, and submits the filter.
     */
    public abstract void applyPreferredPartnerFilters();

    /**
     * Whether the Preferred Partner / Loyalty badge icon is visible on the first hotel card on SRP.
     */
    public abstract boolean isPreferredPartnerIconOnFirstHotelCardVisible();

    /**
     * Whether the SRP filter chip shows a numeric badge counter (i.e. one or more filters applied).
     */
    public abstract boolean isFilterAppliedBadgeVisible();
    public abstract boolean verifyRedirectSRP(LandingPageModulePOJO.Redirect redirectData, String language, String vertical);
    public abstract boolean verifyRedirectSRPWithPojo(String location, String language, String vertical);
    public abstract boolean verifySearchFormWithPojo(HomesData.ContentData list);
    public abstract void closeFilterPopup();
    public abstract List<String> getPrices();
    public abstract List<String> getPricesInFilter();
    public abstract void closeMap();
    public abstract void resetPriceFilter();
    public abstract void resetFilter();
    public abstract void clickResetFilterButton();
    public abstract void selectFilterToggle(String filterOption);
    public abstract void selectFilterCheckbox(String filterOption);
    public abstract boolean isFilterSectionVisible(String filterName);
    public abstract List<String> getHotelDistances();
    public abstract  String selectPopularPlaceFilter();
    public abstract void waitForSrpToLoad();
    public abstract void selectPriceAfterTaxFilter(boolean isTax, String priceDescription);
    public abstract boolean checkHotelPrice(boolean isCrossPrice, String hotelName,String price);
    public abstract void checkAndSelectPriceAfterTaxFilter(boolean isTaxApplied);
    public abstract boolean checkHotelPrice(String hotelName,String price);
    public abstract boolean isLabelTotalPrice(boolean isTax);
    public abstract boolean isLabelTotalPrice();
    public abstract void selectHotelWithMeetTravelPolicyNudge();
    public abstract boolean isHotelRangeMeetPolicyPriceRange(int totalCard,String priceRange);
    public abstract boolean isMeetsTravelPolicyNudgeVisible();
    public abstract String getPriceRemarkText();
    public abstract String clickHeartIcon();
    public abstract String isWishlistDialogShown();
    public abstract boolean isWishlistActive(String hotelName);
    public abstract boolean isWishlistActiveOnMap(String hotelName);
    public abstract void chooseHotelCardOnMap(String hotelName);
    public abstract boolean clickWishlistFirstHotelCard();
    public abstract boolean clickRedWishlistIcon();
    public abstract void openHotelCardInMaps();
    public abstract void closeMaps();
    public abstract boolean isWishlistIconSelected();
    public abstract void verifyCurrencyOnFilter(String currency);
    public abstract void applyAndVerifyPreferredPartnerFilter(String filterName);
    /**
     * Verifies that at least one hotel card on the SRP shows a "from your location" / "dari lokasimu"
     * location info, confirming the search result is correctly bound to the user's current location (Near Me).
     *
     * @return true if at least one hotel card's location info contains the Near Me text in EN or ID
     */
    public abstract boolean verifyNearMeOnHotelCards();

    /**
     * Whether the non-login join / log-in banner is visible on Hotel SRP.
     */
    public abstract boolean isLoginBannerDisplayed();

    /**
     * Whether the login / join action on the SRP banner is displayed and enabled.
     */
    public abstract boolean isLoginButtonClickable();

    /**
     * Clicks the login / join action on the SRP login banner.
     */
    public abstract void clickLoginBanner();

    /**
     * Scrolls the SRP until the "Hotels near your selected area" / "Hotel di dekat lokasi pilihanmu"
     * section becomes visible. The section is rendered below the standard hotel card list and contains
     * additional property cards recommended around the user's chosen region/city/area.
     *
     * @return {@code true} if the section heading was found within the scroll budget; {@code false} otherwise
     */
    public abstract boolean isHotelsNearSelectedAreaSectionVisible();

    /**
     * Returns the number of property cards (with prices) rendered inside the "Hotels near your selected area"
     * section. Useful as an assertion that the section actually has bookable inventory before tapping it.
     *
     * @return the number of hotel cards visible in the section (0 if the section is not present)
     */
    public abstract int getHotelsNearSelectedAreaCardCount();

    /**
     * Taps the first property card inside the "Hotels near your selected area" section and returns the
     * hotel name displayed on that card. Caller must ensure the section is visible (e.g. by calling
     * {@link #isHotelsNearSelectedAreaSectionVisible()} first).
     *
     * @return hotel name shown on the tapped card; used to correlate with the PDP that opens
     */
    public abstract String clickFirstHotelInNearSelectedArea();

    public abstract void scrollToFlexibleStay();
    public abstract boolean isFlexibleStayVisible();
    public abstract String getFlexibleStayHeading();
    public abstract int getFlexibleStayHotelCardCount();
    public abstract boolean isFlexibleStayPriceVisible();

    public boolean isHotelCardLocationMatch(String location, int maxCards) {
        for (int i = 1; i <= maxCards; i++) {
            String locText = getHotelLocationList(String.valueOf(i));
            logger.log("Card #" + i + " location text: " + locText);
            if (locText == null || !locText.toLowerCase().contains(location.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public boolean checkChangeLocation(List<String> newLocation) {
        String locationName;
        String locationType;

        for (String location : newLocation) {
            if (location.equalsIgnoreCase("near_me")) {
                locationName = changeNearMeLocation();
                locationType = "near_me";

            } else {
                locationName = location.split("/")[0];
                locationType = location.split("/")[1];
                changeLocation(locationName, locationType);
                clickSubmit();
            }
            logger.log("Changing location to " + locationType.toUpperCase() + " with name: " + locationName);

            Assertion.assertThat("SRP will reloaded", isSearchResultShown(), is(true));

            if (locationType.equalsIgnoreCase("landmark")) {
                Assertion.assertThat("search location is changed to " + locationType.toUpperCase() + " location", getCurrentLocation().toLowerCase(), either(is("around " + locationName.toLowerCase())).or(is("dekat " + locationName.toLowerCase())));
                List<String> hotelDistances = getHotelDistances();
                Assertion.assertThat("Hotel distances contain the expected text", hotelDistances, everyItem(anyOf(containsStringIgnoringCase("m dari " + locationName.toLowerCase()), containsStringIgnoringCase("m from " + locationName.toLowerCase()))));
            } else if (locationType.equals("near_me")) {
                List<String> nearYou = List.of("hotels near you", "hotel dekat anda", "dekat saya", "near me", "di dekat kamu");
                Assertion.assertThat("search location is changed to " + locationType.toUpperCase() + " location", getCurrentLocation().toLowerCase(), either(is(nearYou.get(0))).or(is(nearYou.get(1))).or(is(nearYou.get(2))).or(is(nearYou.get(3))).or(is(nearYou.get(4))));
                List<String> hotelDistances = getHotelDistances();
                Assertion.assertThat("Hotel distances contain the expected text", hotelDistances, everyItem(anyOf(containsStringIgnoringCase("m dari lokasimu"), containsStringIgnoringCase("m from your location"))));
            } else {
                Assertion.assertThat("search location is changed to " + locationType.toUpperCase() + " location", getCurrentLocation().toLowerCase(), is(locationName.toLowerCase()));
            }
            logger.log("Location changed to " + locationType.toUpperCase() + " with name: " + locationName);
            logger.log("--------------------------------------------------");
        }
        return true;
    }

    /**
     * The purpose of this method is to sync testdata param with hotelCardPriceCheck method
     * @param priceParam the input is limited to "<200, <500, 500-1000, >1000"
     * @return is "0-200000, 0-500000, 500000-1000000, 1000000-Inf"
     */
    public String convertPriceParam(String priceParam) {
        String result = "";

        if (priceParam.contains("<")) {
            result += "0-";
            if (priceParam.contains("200")) {
                result += "200000";
            } else if (priceParam.contains("500")) {
                result += "500000";
            } else {
                logger.fail("1 There is no handle for '" + priceParam + "'");
            }
        } else if (priceParam.contains("-")) {
            result += "500000-1000000";

        } else if (priceParam.contains(">")) {
            result += "1000000-Inf";

        } else {
            logger.fail("3 There is no handle for '" + priceParam + "'");
            throw new NoSuchMethodError();
        }

        return result;
    }

    public boolean verifyURLWithPojo(HomesData.ContentData list) {
        String URLFromAPI = list.redirect.button;
        logger.log("URL from API"+ URLFromAPI);

        driverTools.waitForElementToDisappear(LOADING_STATE, 20);
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        String currentURL = driver.getCurrentUrl();
        logger.log("URL from APP" + currentURL);
        driverTools.switchToNative();

        return currentURL.contains(URLFromAPI);
    }

    public boolean verifyRedirectUrl(LandingPageModulePOJO.Redirect redirectData) {
        String URLFromAPI = redirectData.button;
        logger.log("URL from API"+ URLFromAPI);

        driverTools.waitForElementToDisappear(LOADING_STATE, 20);
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        String currentURL = driver.getCurrentUrl();
        logger.log("URL from APP" + currentURL);
        driverTools.switchToNative();

        return currentURL.contains(URLFromAPI);
    }

    public String getOpenedWebViewUrl() {
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        String url = driverTools.getCurrentURL();
        driverTools.switchToNative();
        return url;
    }

    protected boolean benefitTextContains(String benefitText, String expectedPart) {
        return normalizeNonBreakingSpaceText(benefitText)
                .toLowerCase()
                .contains(normalizeNonBreakingSpaceText(expectedPart).toLowerCase());
    }


}
