package com.tiket.web.page.nft.car_rental.productDetail_page;

import com.tiket.common.XPathUtils;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.carrental.CarRentalDetail;
import com.tiket.pojo.carrental.CarRentalSrpData;
import com.tiket.pojo.carrental.CarRentalReviewPageData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public abstract class CarRentalReviewBookingPageWeb<T extends WebDriver> extends WebPage<T> {

    public CarRentalReviewBookingPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CarRentalReviewBookingPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CarRentalReviewBookingPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CarRentalReviewBookingPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void navigateToRentalAreaPage(String carRentalPackage);
    public abstract void verifyRentalArea(String location);

    private static final Locator CAR_AND_VENDOR_DETAILS = new Locator("CAR_AND_VENDOR_DETAILS", new By.ByXPath("//*[contains(@class, 'CarDetailCard_car_details')]"));
    private static final Locator CAR_VENDOR_NAME_TEXT = new Locator("CAR_VENDOR_NAME_TEXT", new By.ByXPath("//*[contains(@class, 'CarDetailCard_car_details')]//*[contains(@class,'CarDetailCard_ico')]//following-sibling::*[contains(@class,'Text_text')]"));
    private static final Locator CAR_NAME_REVIEW_PAGE = new Locator("CAR_NAME_REVIEW_PAGE", new By.ByXPath("//*[contains(@class, 'CarDetailCard_car_details')]//h3"));
    private static final Locator RENTAL_TIME_SECTION = new Locator("RENTAL_TIME_SECTION", new By.ByXPath("//*[contains(@class, 'rent_time')]"));
    private static final Locator IMPORTANT_INFO_SECTION = new Locator("IMPORTANT_INFO_SECTION", new By.ByXPath("//*[contains(@class, 'ImportantInfo_wrapper')]"));
    private static final Locator PRICE_REVIEW_PAGE = new Locator("PRICE_REVIEW_PAGE", new By.ByXPath("//*[contains(@class, 'ReviewPageFooter_price_and_points')]"));
    private static final Locator REGULAR_PACKAGE = new Locator("REGULAR_PACKAGE", new By.ByXPath("//*[contains(@class, 'PackageSection_package_title') and (contains(text(), 'Aja') or contains(text(), 'Only'))]"));
    private static final Locator REGULAR_PACKAGE_SECTION = new Locator("REGULAR_PACKAGE_SECTION", new By.ByXPath("//*[contains(text(), 'harus kamu bayar') or contains(text(), 'must be paid')]"));
    private static final Locator ALL_IN_PACKAGE = new Locator("ALL_IN_PACKAGE", new By.ByXPath("//*[contains(@class, 'PackageSection_package_title') and " + XPathUtils.containsIgnoreCase("normalize-space(.)", "All-In") + "]"));
    private static final Locator ALL_IN_PACKAGE_SECTION = new Locator("ALL_IN_PACKAGE_SECTION", new By.ByXPath("//*[contains(text(), 'Paket All-in') or contains(text(), 'All-in Package')]"));
    protected static final Locator OPEN_PRICE_BOTTOM_SHEET = new Locator("OPEN_PRICE_BOTTOM_SHEET", new By.ByXPath("//*[contains(@class, 'AnimatedChevronIcon_icon_chevron')]"));
    private static final Locator TRAVEL_ADD_ONS = new Locator("TRAVEL_ADD_ONS", new By.ByXPath("//*[contains(@class, 'PriceDetail_content')]//span[contains(text(), 'Travel') or contains(text(), 'Fasilitas')]"));
    private static final Locator TRAVEL_ADD_ONS_PRICE = new Locator("TRAVEL_ADD_ONS_PRICE", new By.ByXPath("//*[contains(@class, 'PriceDetail_content')]//span[contains(text(), 'Travel') or contains(text(), 'Fasilitas')]/ancestor::section[1]//span[contains(text(), 'IDR')]"));
    protected static final Locator CLOSE_PRICE_BOTTOM_SHEET = new Locator("CLOSE_PRICE_BOTTOM_SHEET",new By.ByXPath("//*[contains(@class, 'Modal_modal_close') or contains(@class, 'BottomSheet_close_button')]"));
    private static final Locator PROCEED_TO_BOOKING_FORM = new Locator("PROCEED_TO_BOOKING_FORM", new By.ByXPath("//*[contains(@data-testid, 'footer')]//button"));
    private static final Locator RENTAL_AREA_COVERAGE_TEXT = new Locator("RENTAL_AREA_COVERAGE_TEXT", new By.ByXPath("//*[contains(@class, 'highEmphasis') and contains(text(), 'Area')]"));
    private static final Locator COVERAGE_AREA_MAP = new Locator("COVERAGE_AREA_MAP", new By.ByXPath("//img[@alt='map-img']"));
    private static final Locator COVERAGE_AREA_LIST = new Locator("COVERAGE_AREA_LIST", new By.ByXPath("//*[contains(@class, 'coverage_areas')]"));
    private static final Locator REVIEW_SECTION_HEADING = new Locator("REVIEW_SECTION_HEADING", new By.ByXPath("//*[contains(@class, 'ReviewWidget_review_section') or contains(@class, 'ReviewWidget-module__heading')]//h2"));
    private static final Locator REVIEW_SECTION_CARD_LIST = new Locator("REVIEW_SECTION_CARD_LIST", new By.ByXPath("//*[contains(@class, 'ReviewWidget_review_section') or contains(@class, 'ReviewWidget-module__review_list')]//*[contains(@data-testid,'review-card')]"));
    private static final Locator HOW_TO_READ_SECTION_HEADING = new Locator("HOW_TO_READ_SECTION_HEADING", new By.ByXPath("//*[contains(@class, 'ImportantInfo_title')]"));
    private static final Locator HOW_TO_READ_IMPORTANT_INFO_LIST = new Locator("HOW_TO_READ_IMPORTANT_INFO_LIST", new By.ByXPath("//*[contains(@class, 'ImportantInfo_wrapper')]//*[contains(@class,'InfoCardList_content')]"));
    private static final Locator WHAT_YOU_NEED_TO_PREPARE_SECTION_HEADING = new Locator("WHAT_YOU_NEED_TO_PREPARE_SECTION_HEADING", new By.ByXPath("//*[contains(@class,'InfoCardList_title')]"));
    private static final Locator WHAT_YOU_NEED_TO_PREPARE_SECTION_INFO_LIST = new Locator("WHAT_YOU_NEED_TO_PREPARE_SECTION_INFO_LIST", new By.ByXPath("//*[contains(@class,'InfoCardList_title')]/following-sibling::*/*[contains(@class,'InfoCardList_content')]"));
    private static final Locator SELECT_YOUR_PACKAGE_SECTION_HEADING = new Locator("SELECT_YOUR_PACKAGE_SECTION_HEADING", new By.ByXPath("//*[contains(@class, 'PackageSection_main_title')]"));
    private static final Locator SELECT_YOUR_PACKAGE_LIST = new Locator("SELECT_YOUR_PACKAGE_LIST", new By.ByXPath("//*[contains(@class, 'PackageSection_package_title')]"));
    private static final Locator PRICE_DETAIL_PRICE_ITEM = new Locator("PRICE_DETAIL_PRICE_ITEM", new By.ByXPath("//*[contains(@class, 'PriceDetail_price__VPT_I Text') and not(contains(@class,'Text_weight_bold'))]"));
    private static final Locator PRICE_DETAIL_TOTAL_PRICE = new Locator("PRICE_DETAIL_TOTAL_PRICE", new By.ByXPath("//*[contains(@class, 'PriceDetail_price__VPT_I Text') and contains(@class,'Text_weight_bold')]"));
    private static final Locator REGION_NAME_IN_NAVIGATION_BAR = new Locator("REGION_NAME_IN_NAVIGATION_BAR", new By.ByXPath("//*[contains(@class,'Header_title')]"));
    protected static final Locator BASE_PRICE_UNDER_BOTTOM_SHEET = new Locator("BASE_PRICE_UNDER_BOTTOM_SHEET", new By.ByXPath("(//span[contains(@class, 'PriceDetail_price')])[last()]"));
    private static final Locator DEFAULT_PACKAGE = new Locator("DEFAULT_PACKAGE", new By.ByXPath("(//*[contains(@class, 'PackageSection_slider')]//*[contains(@class,'PackageSection_package_title')])[1]"));

    /**
     * Verifies that the car and vendor details are displayed on the page.
     * Asserts that the element containing car and vendor details is visible.
     */
    public void verifyCarVendorDetails() {
        driverTools.waitForElementToAppear(CAR_AND_VENDOR_DETAILS);
        Assertion.assertThat("Car and vendor details are displayed", driverTools.isElementDisplayed(CAR_AND_VENDOR_DETAILS), is(true));
    }

    /**
     * Verifies that the car name on the review page matches the expected car name.
     * Asserts that the car name is the same as in the selected search results page (SRP) card.
     *
     * @param carName the expected car name to verify
     * @return true if the car name matches, false otherwise
     */
    public boolean verifyCarName(String carName) {
        String carNameReviewPage = driverTools.waitForElementToAppear(CAR_NAME_REVIEW_PAGE).orElseThrow().getText();
        Assertion.assertThat("Car name in review booking page { " + carNameReviewPage + " } is the same as in the selected SRP card { " + carName + " }", carNameReviewPage.trim(), containsString(carName.trim()));
        return carNameReviewPage.trim().contains(carName.trim());
    }

    /**
     * Verifies that the car vendor name on the review page matches the expected car vendor name.
     * Asserts that the car vendor name is the same as in the selected search results page (SRP) card.
     *
     * @param carVendorNameSrp the expected car vendor name to verify
     * @return true if the car vendor name matches, false otherwise
     */
    public boolean verifyCarVendorName(String carVendorNameSrp) {
        String carVendorNameReviewPage = driverTools.waitForElementToAppear(CAR_VENDOR_NAME_TEXT).orElseThrow().getText();
        Assertion.assertThat("Car vendor name in review booking page { " + carVendorNameReviewPage + " } contains the selected SRP card vendor name { " + carVendorNameSrp + " }", carVendorNameReviewPage.trim(), containsString(carVendorNameSrp.trim()));
        return carVendorNameReviewPage.trim().contains(carVendorNameSrp.trim());
    }

    /**
     * Verifies the presence and content of the car review section.
     * Checks that the review section heading contains the word "Review" and that at least one review card is displayed.
     *
     * @return true if the review section is correctly displayed, false otherwise
     */
    public boolean verifyCarReviewSection() {
        driverTools.waitForElementToAppear(REVIEW_SECTION_HEADING);
        WebElement reviewSectionHeadingElement = driverTools.waitForElementToPresence(REVIEW_SECTION_HEADING).orElseThrow();
        String reviewSectionHeadingText = reviewSectionHeadingElement.getText();
        Assertion.assertThat("Review section heading text is correct {" + reviewSectionHeadingText + " }" , reviewSectionHeadingText, containsString("Review"));
        List<WebElement> reviewCardListElements = driverTools.elements(REVIEW_SECTION_CARD_LIST).orElseThrow();
        logger.log("Number of review cards found: " + reviewCardListElements.size());
        Assertion.assertThat("Review section card list is displayed and its size { " + reviewCardListElements.size() + " } should be more then 0 ",reviewCardListElements.size() , greaterThan(0));

        boolean isHeadingCorrect = reviewSectionHeadingText.contains("Review");
        boolean isReviewCardListDisplayed = !reviewCardListElements.isEmpty();
        return isHeadingCorrect && isReviewCardListDisplayed;
    }

    /**
     * Verifies the presence and content of the "How to Read" information section.
     * Checks that the section heading contains "Important to Read" (or its Indonesian equivalent)
     * and that at least one information item is displayed in the list.
     *
     * @return true if the "How to Read" section is correctly displayed, false otherwise
     */
    public boolean verifyCarHowToReadInfoSection() {
        driverTools.waitForElementToAppear(HOW_TO_READ_SECTION_HEADING);
        WebElement howToReadSectionHeadingElement = driverTools.waitForElementToPresence(HOW_TO_READ_SECTION_HEADING).orElseThrow();
        String howToReadSectionHeadingText = howToReadSectionHeadingElement.getText();
        Assertion.assertThat("How to read section heading text is correct { " + howToReadSectionHeadingText + " }", howToReadSectionHeadingText, anyOf(containsString("Important to Read"), containsString("Penting untuk Dibaca")));

        List<WebElement> howToReadInfoListElements = driverTools.elements(HOW_TO_READ_IMPORTANT_INFO_LIST).orElseThrow();
        logger.log("Number of how to read info items found: " + howToReadInfoListElements.size());

        Assertion.assertThat("How to read info list is displayed and its size { " + howToReadInfoListElements.size() + " } should be more then 0 ",
                howToReadInfoListElements.size(), greaterThan(0));

        boolean isHeadingCorrect = howToReadSectionHeadingText.contains("Important to Read") ||
                howToReadSectionHeadingText.contains("Penting untuk Dibaca");
        boolean isInfoListDisplayed = !howToReadInfoListElements.isEmpty();

        return isHeadingCorrect && isInfoListDisplayed;
    }

    /**
     * Verifies the presence and content of the "What you need to prepare" information section.
     * Checks that the section heading contains "What you need to prepare" (or its Indonesian equivalent)
     * and that at least one information item is displayed in the list.
     *
     * @return true if the "What you need to prepare" section is correctly displayed, false otherwise
     */
    public boolean verifyWhatYouNeedToPrepareSection() {
        driverTools.waitForElementToAppear(WHAT_YOU_NEED_TO_PREPARE_SECTION_HEADING);
        WebElement howToReadSectionHeadingElement = driverTools.waitForElementToPresence(WHAT_YOU_NEED_TO_PREPARE_SECTION_HEADING).orElseThrow();
        String howToReadSectionHeadingText = howToReadSectionHeadingElement.getText();
        Assertion.assertThat("What you need to prepare heading text is correct { " + howToReadSectionHeadingText + " }", howToReadSectionHeadingText, anyOf(containsString("What you need to prepare"), containsString("Yang harus kamu siapkan")));

        List<WebElement> howToReadInfoListElements = driverTools.elements(WHAT_YOU_NEED_TO_PREPARE_SECTION_INFO_LIST).orElseThrow();
        logger.log("Number of What you need to prepare info items found: " + howToReadInfoListElements.size());

        Assertion.assertThat("What you need to prepare info list is displayed and size { " + howToReadInfoListElements.size() + " } should be more then 0 ",
                howToReadInfoListElements.size(), greaterThan(0));

        boolean isHeadingCorrect = howToReadSectionHeadingText.contains("What you need to prepare") ||
                howToReadSectionHeadingText.contains("Yang harus kamu siapkan");
        boolean isInfoListDisplayed = !howToReadInfoListElements.isEmpty();

        return isHeadingCorrect && isInfoListDisplayed;
    }

    /**
     * Verifies the package selection section, including the heading and available package options.
     * Checks that the section heading is correct and that the expected package type is available in the options.
     *
     * @param packageType the expected package type to be available in the selection
     * @return true if the package selection section is correct, false otherwise
     */
    public boolean verifyPackageSelectionSection(String packageType) {
        driverTools.waitForElementToAppear(SELECT_YOUR_PACKAGE_SECTION_HEADING);
        WebElement packageSectionHeadingElement = driverTools.waitForElementToPresence(SELECT_YOUR_PACKAGE_SECTION_HEADING).orElseThrow();
        String packageSectionHeadingText = packageSectionHeadingElement.getText();
        Assertion.assertThat("Package selection section heading text is correct { " + packageSectionHeadingText + " }", packageSectionHeadingText, anyOf( containsString("Select Your Rental Package"), containsString("Pilih Paket Sewa Mobilmu")));

        List<WebElement> packageListElements = driverTools.elements(SELECT_YOUR_PACKAGE_LIST).orElseThrow();
        logger.log("Number of package options found: " + packageListElements.size());

        List<String> packageOptions = new ArrayList<>();
        for (WebElement element : packageListElements) {
            String text = element.getText();
            logger.log("Package option found: " + text + " expected: " + packageType);
            packageOptions.add(text);
        }

        boolean isHeadingCorrect = packageSectionHeadingText.contains("Select Your Rental Package") || packageSectionHeadingText.contains("Pilih Paket Sewa Mobilmu");
        boolean isPackageListDisplayed = !packageListElements.isEmpty();
        boolean isPackageTypeAvailable = packageOptions.stream().anyMatch(option -> option.contains(packageType));

        Assertion.assertThat("Package selection list is displayed and size { " + packageListElements.size() + " } should be more then zero", packageListElements.size(), greaterThan(0));
        Assertion.assertThat("Package options { " + packageOptions +" } contain the specified package type { " + packageType + " }" , packageOptions, hasItem(containsString(packageType)));

        return isHeadingCorrect && isPackageListDisplayed && isPackageTypeAvailable;
    }

    /**
     * Opens the price detail bottom sheet by clicking on the price detail button.
     *
     * @throws RuntimeException if the price detail button is not found or clickable
     */
    public void openPriceDetailBottomSheet() {
        driverTools.waitForElementToAppear(OPEN_PRICE_BOTTOM_SHEET).orElseThrow();
        driverTools.clickWithJavascriptExecutor(OPEN_PRICE_BOTTOM_SHEET);
    }

    /**
     * Closes the price detail bottom sheet by clicking on the close button.
     * Waits for the close button to appear before clicking.
     *
     * @throws RuntimeException if the close button is not found or clickable
     */
    public void closePriceDetailBottomSheet() {
        if (driverTools.waitForElementToAppear(CLOSE_PRICE_BOTTOM_SHEET).isPresent()) {
            driverTools.clickWithJavascriptExecutor(CLOSE_PRICE_BOTTOM_SHEET);
            driverTools.waitForElementToDisappear(CLOSE_PRICE_BOTTOM_SHEET);
        } else {
            driverTools.back();
        }
    }

    /**
     * Calculates the sum of all individual price items and verifies it matches the displayed total price.
     * This method extracts prices from all price detail items, sums them up, and compares with the total price shown.
     *
     * @return true if the calculated sum matches the displayed total price, false otherwise
     * @throws RuntimeException if price elements are not found or if price conversion fails
     */
    public boolean calculatePriceSum() {
        driverTools.waitForElementToPresence(PRICE_DETAIL_PRICE_ITEM);
        List<WebElement> priceItems = driverTools.elements(PRICE_DETAIL_PRICE_ITEM).orElseThrow();
        double sum = 0;

        for (WebElement item : priceItems) {
            String priceText = driverTools.getText(item);
            if (!priceText.isEmpty() && priceText.matches(".*\\d.*")) {
                logger.log("Price item found: " + priceText);
                sum += CoreFunctions.convertPriceSafely(priceText);
            }
        }

        WebElement totalPriceText = driverTools.waitForElementToPresence(PRICE_DETAIL_TOTAL_PRICE).orElseThrow();
        String totalPriceString = driverTools.getText(totalPriceText);
        double totalPrice = CoreFunctions.convertPriceSafely(totalPriceString);

        logger.log("Calculated sum of price items: " + sum);
        logger.log("Displayed total price: " + totalPrice);

        Assertion.assertThat(
                "Calculated sum matches displayed total price. Calculated: {" + sum + "} | Displayed: {" + totalPrice + "}",
                sum,
                is(totalPrice)
        );
        return Double.compare(sum, totalPrice) == 0;
    }

    /**
     * Verifies the add-on package section with different handling for package types vs pickup/drop-off locations.
     * For package types: validates duration and multiplies price by duration.
     * For pickup/drop-off: validates area and uses fixed price (no multiplication).
     *
     * @param packageType the expected package/location type (e.g., "Fuel Package", "Pickup", "Drop-off")
     * @param packagePrice the base price per day for packages, or fixed price for pickup/drop-off
     * @param rentalDuration the rental duration (for packages)
     * @param expectedArea  area (for pickup/drop-off)
     * @return true if type, duration/area, and calculated price are all correct, false otherwise
     * @throws RuntimeException if package elements are not found
     */
    public boolean verifyAddOnPackageSectionWithDuration(String packageType, String packagePrice, int rentalDuration,String expectedArea) {
        Locator packageTypeLocator = getPackagePriceDetailTypeLocator(packageType);
        Locator packagePriceLocator = getPackagePriceDetailAmountLocator(packageType);

        WebElement packageTypeElement = driverTools.waitForElementToPresence(packageTypeLocator).orElseThrow();
        String actualPackageType = driverTools.getText(packageTypeElement);

        WebElement packagePriceElement = driverTools.waitForElementToPresence(packagePriceLocator).orElseThrow();
        String actualPackagePrice = driverTools.getText(packagePriceElement);

        // Determine if this is a package type or pickup/drop-off location
        boolean isPackageType = isPackageType(packageType);
        boolean isPickupDropoff = isPickupOrDropoff(packageType);

        if (isPackageType) {
            return verifyPackageType(packageType, packagePrice, rentalDuration,
                    actualPackageType, actualPackagePrice);
        } else if (isPickupDropoff) {
            return verifyPickupDropoff(packageType, packagePrice, expectedArea,
                    actualPackageType, actualPackagePrice);
        } else {
            throw new IllegalArgumentException("Unknown package type: " + packageType);
        }
    }

    /**
     * Creates a locator for finding the package/location type element in the price detail section.
     * Supports both English and Indonesian package names and pickup/drop-off locations.
     * Handles partial text matching for elements with duration (e.g., "Paket Bensin (2 hari)").
     *
     * @param type the package type or location to locate
     * @return a Locator object for finding the package type element
     */
    private Locator getPackagePriceDetailTypeLocator(String type) {
        String alternativeText = getOppositeLanguageText(type);

        // Create multiple XPath conditions to handle different scenarios
        StringBuilder xpathBuilder = new StringBuilder();
        xpathBuilder.append("//*[contains(@class,'PriceDetail_price_item_label')]//span[");

        // Exact match for both languages
        xpathBuilder.append("contains(text(),'").append(type).append("')");
        xpathBuilder.append(" or contains(text(),'").append(alternativeText).append("')");

        // Partial match for package types that might have duration
        if (isPackageType(type)) {
            String basePackageEng = getBasePackageKeywords(type);
            String basePackageIndo = getBasePackageKeywords(alternativeText);

            if (!basePackageEng.isEmpty()) {
                xpathBuilder.append(" or contains(text(),'").append(basePackageEng).append("')");
            }
            if (!basePackageIndo.isEmpty()) {
                xpathBuilder.append(" or contains(text(),'").append(basePackageIndo).append("')");
            }
        }

        // For pickup/drop-off locations, match base terms
        if (isPickupOrDropoff(type)) {
            if (type.toLowerCase().contains("pickup") || type.toLowerCase().contains("jemput")) {
                xpathBuilder.append(" or contains(text(),'Jemput') or contains(text(),'Pickup')");
            }
            if (type.toLowerCase().contains("drop-off")) {
                xpathBuilder.append(" or contains(text(),'Drop-off')");
            }
        }

        xpathBuilder.append("]");

        return new Locator("PACKAGE_TYPE_PRICE_DETAIL", new By.ByXPath(xpathBuilder.toString()));
    }

    /**
     * Creates a locator for finding the package price amount element in the price detail section.
     * Supports both English and Indonesian package names and pickup/drop-off locations.
     *
     * @param type the package type or location to locate the price for
     * @return a Locator object for finding the package price amount element
     */
    private Locator getPackagePriceDetailAmountLocator(String type) {
        String alternativeText = getOppositeLanguageText(type);

        StringBuilder xpathBuilder = new StringBuilder();
        xpathBuilder.append("//*[contains(@class,'PriceDetail_price_item_label')]//span[");

        // Same matching logic as type locator
        xpathBuilder.append("contains(text(),'").append(type).append("')");
        xpathBuilder.append(" or contains(text(),'").append(alternativeText).append("')");

        if (isPackageType(type)) {
            String basePackageEng = getBasePackageKeywords(type);
            String basePackageIndo = getBasePackageKeywords(alternativeText);

            if (!basePackageEng.isEmpty()) {
                xpathBuilder.append(" or contains(text(),'").append(basePackageEng).append("')");
            }
            if (!basePackageIndo.isEmpty()) {
                xpathBuilder.append(" or contains(text(),'").append(basePackageIndo).append("')");
            }
        }

        if (isPickupOrDropoff(type)) {
            if (type.toLowerCase().contains("pickup") || type.toLowerCase().contains("jemput")) {
                xpathBuilder.append(" or contains(text(),'Jemput') or contains(text(),'Pickup')");
            }
            if (type.toLowerCase().contains("drop-off")) {
                xpathBuilder.append(" or contains(text(),'Drop-off')");
            }
        }

        xpathBuilder.append("]//parent::*/following-sibling::*");

        return new Locator("PACKAGE_PRICE_DETAIL_AMOUNT", new By.ByXPath(xpathBuilder.toString()));
    }

    /**
     * Verifies package types (Fuel Package, Car & Driver Only, etc.)
     * Validates duration and multiplies price by duration.
     */
    private boolean verifyPackageType(String expectedPackageType, String packagePrice, int rentalDuration,
                                      String actualPackageType, String actualPackagePrice) {

        // Extract duration from the displayed text and verify
        int actualDuration = extractDurationFromText(actualPackageType);
        boolean isDurationCorrect = actualDuration == rentalDuration;
        Assertion.assertThat("actual duration {" + actualDuration + " } in package text matches expected rental duration { " + rentalDuration + " }", actualDuration, is(rentalDuration));


        // Verify base package type (without duration)
        String basePackageType = extractBasePackageType(actualPackageType);
        boolean isPackageTypeCorrect = basePackageType.equals(expectedPackageType) ||
                basePackageType.equals(getOppositeLanguageText(expectedPackageType));
        logger.log("Base package type correct: " + isPackageTypeCorrect);
        Assertion.assertThat("Package type is displayed correctly", basePackageType,
                anyOf(is(expectedPackageType), is(getOppositeLanguageText(expectedPackageType))));

        // Calculate expected total price (multiply by duration for packages)
        double expectedTotalPrice = CoreFunctions.convertPriceSafely(packagePrice) * rentalDuration;
        double actualTotalPrice = CoreFunctions.convertPriceSafely(actualPackagePrice);
        boolean isPriceCorrect = actualTotalPrice == expectedTotalPrice;
        logger.log("is price correct: " + isPriceCorrect);
        Assertion.assertThat("Package price is calculated correctly for " + rentalDuration + " day(s)",
                actualTotalPrice, is(expectedTotalPrice));

        return isPackageTypeCorrect && isDurationCorrect && isPriceCorrect;
    }

    /**
     * code is for booking page price detail but logic and locators are same so made it in the review booking page
     * Verifies pickup/drop-off locations
     * Validates area number and uses fixed price (no multiplication by duration).
     */
    private boolean verifyPickupDropoff(String expectedLocationType, String locationPrice, String expectedArea,
                                        String actualLocationText, String actualLocationPrice) {

        boolean isAreaCorrect = actualLocationText.contains(expectedArea.trim());
        Assertion.assertThat(
                "Area in location text matches expected area. Actual: {" + actualLocationText + "} | Expected: {" + expectedArea + "}",
                actualLocationText,
                containsString(expectedArea)
        );

        // Verify base location type (without area)
        String baseLocationType = extractBaseLocationType(actualLocationText);
        boolean isLocationTypeCorrect = baseLocationType.contains(expectedLocationType) ||
                baseLocationType.contains(getOppositeLanguageText(expectedLocationType));
        logger.log("Is location type correct: " + isLocationTypeCorrect);
        Assertion.assertThat("Location type is displayed correctly", baseLocationType,
                anyOf(containsString(expectedLocationType), containsString(getOppositeLanguageText(expectedLocationType))));

        // For pickup/drop-off: use fixed price (no multiplication)
        double expectedPrice = CoreFunctions.convertPriceSafely(locationPrice);
        logger.log("Expected fixed price: " + expectedPrice);
        double actualPrice = CoreFunctions.convertPriceSafely(actualLocationPrice);
        logger.log("Actual fixed price: " + actualPrice);
        boolean isPriceCorrect = actualPrice == expectedPrice;
        logger.log("Is location price correct: " + isPriceCorrect);
        Assertion.assertThat("Location price is correct (fixed price, no duration multiplication)",
                actualPrice, is(expectedPrice));

        return isLocationTypeCorrect && isAreaCorrect && isPriceCorrect;
    }

    /**
     * Gets base package keywords for partial matching (without duration or area info)
     */
    private String getBasePackageKeywords(String packageType) {
        if (packageType == null) return "";

        String lowerType = packageType.toLowerCase();

        if (lowerType.contains("fuel") || lowerType.contains("bensin")) {
            return lowerType.contains("fuel") ? "Fuel" : "Bensin";
        } else if (lowerType.contains("car") || lowerType.contains("driver") ||
                lowerType.contains("mobil") || lowerType.contains("sopir")) {
            if (lowerType.contains("car") || lowerType.contains("driver")) {
                return "Car & Driver";
            } else {
                return "Mobil & Sopir";
            }
        } else if (lowerType.contains("all-in")) {
            return "All-in";
        }

        return "";
    }

    /**
     * Determines if the given text represents a package type
     */
    private boolean isPackageType(String text) {
        if (text == null) return false;
        String lowerText = text.toLowerCase();

        return lowerText.contains("fuel") || lowerText.contains("bensin") ||
                lowerText.contains("car") || lowerText.contains("mobil") ||
                lowerText.contains("driver") || lowerText.contains("sopir") ||
                lowerText.contains("all-in") || lowerText.contains("package") ||
                lowerText.contains("paket");
    }

    /**
     * Determines if the given text represents pickup or drop-off
     */
    private boolean isPickupOrDropoff(String text) {
        if (text == null) return false;
        String lowerText = text.toLowerCase();

        return lowerText.contains("pickup") || lowerText.contains("jemput") ||
                lowerText.contains("drop-off") || lowerText.contains("drop off");
    }

    /**
     * Extracts duration from package text (e.g., "Paket Bensin (2 hari)" -> 2).
     * Supports both English and Indonesian formats ("hari", "day", "days").
     *
     * @param text the text containing duration information
     * @return the extracted duration in days, or -1 if not found/invalid
     */
    private int extractDurationFromText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return -1;
        }

        // Look for patterns like "2 hari", "2 days", "(2 hari)", "(2 days)"
        Pattern durationPattern = Pattern.compile("([0-9]+)\\s*(hari|days?)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = durationPattern.matcher(text);

        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    /**
     * Extracts area number from location text (e.g., "Jemput di Area 2" -> 2)
     */
    private int extractAreaFromText(String text) {
        if (text == null) return -1;

        // Look for patterns like "Area 2", "area 2", "Area2", "area2"
        Pattern areaPattern = Pattern.compile(".*area\\s*([0-9]+).*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = areaPattern.matcher(text);

        if (matcher.matches()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    /**
     * Extracts base package type without duration (e.g., "Paket Bensin (2 hari)" -> "Paket Bensin")
     */
    private String extractBasePackageType(String text) {
        if (text == null) return "";

        // Remove duration parts like "(2 hari)", "(2 days)", etc.
        return text.replaceAll("\\s*\\([0-9]+\\s*(hari|days?)\\)", "").trim();
    }

    /**
     * Extracts base location type without area (e.g., "Jemput di Area 2" -> "Jemput")
     */
    private String extractBaseLocationType(String text) {
        if (text == null) return "";

        // Remove area parts like "di Area 2", "at Area 2", etc.
        return text.replaceAll("\\s*(di|at)\\s+area\\s*[0-9]+", "").trim();
    }

    /**
     * Returns the text in the opposite language (English <-> Indonesian).
     * Supports both package names and pickup/drop-off locations for car rental services.
     */
    public static String getOppositeLanguageText(String detectedText) {
        if (detectedText == null || detectedText.trim().isEmpty()) {
            return "Unknown";
        }

        String lowerText = detectedText.toLowerCase().trim();

        return switch (lowerText) {
            // Package Names: Indonesian -> English
            case "mobil & sopir aja" -> "Car & Driver Only";
            case "paket bensin" -> "Fuel Package";

            // Package Names: English -> Indonesian
            case "car & driver only" -> "Mobil & Sopir Aja";
            case "fuel package" -> "Paket Bensin";

            // Package Names: Same in both languages
            case "all-in area 0", "all-in area0" -> "All-in Area 0";
            case "all-in area 2", "all-in area2" -> "All-In Area 2";

            // Pickup/Drop-off: English -> Indonesian
            case "pickup at area 1", "pickup at area 2", "pickup at area 0",
                 "pickup at area1", "pickup at area2", "pickup at area0" -> "Jemput";
            case "drop-off at area 1", "drop-off at area 2", "drop-off at area 0",
                 "drop-off at area1", "drop-off at area2", "drop-off at area0" -> "Drop-off";

            // Pickup/Drop-off: Indonesian -> English
            case "jemput di area 1", "jemput di area 2", "jemput di area 0",
                 "jemput di area1", "jemput di area2", "jemput di area0" -> "Pickup";
            case "drop-off di area 1", "drop-off di area 2", "drop-off di area 0",
                 "drop-off di area1", "drop-off di area2", "drop-off di area0" -> "Drop-off";

            // Simple forms
            case "pickup" -> "Jemput";
            case "jemput" -> "Pickup";
            case "drop-off", "drop off" -> "Drop-off";

            default -> {
                // Fallback: keyword-based detection and mapping
                if (lowerText.contains("mobil") || lowerText.contains("sopir") || lowerText.contains("bensin")) {
                    if ((lowerText.contains("mobil") && lowerText.contains("sopir")) ||
                            lowerText.contains("sopir aja")) {
                        yield "Car & Driver Only";
                    } else if (lowerText.contains("bensin") || lowerText.contains("paket bensin")) {
                        yield "Fuel Package";
                    }
                } else if (lowerText.contains("car") || lowerText.contains("driver") || lowerText.contains("fuel")) {
                    if (lowerText.contains("car") && lowerText.contains("driver")) {
                        yield "Mobil & Sopir Aja";
                    } else if (lowerText.contains("fuel")) {
                        yield "Paket Bensin";
                    }
                } else if (lowerText.contains("jemput")) {
                    yield "Pickup";
                } else if (lowerText.contains("pickup")) {
                    yield "Jemput";
                } else if (lowerText.contains("drop-off")) {
                    yield "Drop-off";
                }

                yield detectedText;
            }
        };
    }


    /**
     * Verifies the base price in the price detail section by validating duration and calculated total price.
     *
     * @param basePriceSrp the base price per day from the search results page
     * @param duration the rental duration in days
     * @return true if both duration and calculated price are correct, false otherwise
     * @throws RuntimeException if base price elements are not found
     */
    public boolean verifyBasePricePriceDetail(String basePriceSrp, int duration) {
        Locator basePriceLabelLocator = getBasePriceLabelPriceDetailLocator();
        Locator basePriceAmountLocator = getBasePriceDetailAmountLocator();

        WebElement basePriceLabelElement = driverTools.waitForElementToPresence(basePriceLabelLocator).orElseThrow();
        String actualDurationText = driverTools.getText(basePriceLabelElement);

        WebElement basePriceAmountElement = driverTools.waitForElementToPresence(basePriceAmountLocator).orElseThrow();
        String actualPriceText = driverTools.getText(basePriceAmountElement);

        // Extract duration from the displayed text and verify with assertion
        int actualDuration = extractDurationFromText(actualDurationText);
        boolean isDurationCorrect = actualDuration == duration;
        Assertion.assertThat("Duration extracted from base price text should be valid", actualDuration, is(not(-1)));
        Assertion.assertThat("Duration in base price text matches expected duration", actualDuration, is(duration));

        // Calculate expected total price based on duration
        double expectedTotalPrice = CoreFunctions.convertPriceSafely(basePriceSrp) * duration;
        double actualTotalPrice = CoreFunctions.convertPriceSafely(actualPriceText);
        boolean isPriceCorrect = actualTotalPrice == expectedTotalPrice;
        Assertion.assertThat("Base price is calculated correctly for " + duration + " day(s)",
                actualTotalPrice, is(expectedTotalPrice));

        return isDurationCorrect && isPriceCorrect;
    }

    /**
     * Creates a locator for finding the base price label element in the price detail section.
     *
     * @return a Locator object for finding the base price label element
     */
    private Locator getBasePriceLabelPriceDetailLocator() {
        return new Locator("BASE_PRICE_LABEL", new By.ByXPath("//*[contains(@class,'Text_text') and (contains(text(),'Price') or contains(text(),'Harga'))]/following-sibling::*//*[contains(@class,'PriceDetail_price_item_label')]//span"));
    }

    /**
     * Creates a locator for finding the base price amount element in the price detail section.
     *
     * @return a Locator object for finding the base price amount element
     */
    private Locator getBasePriceDetailAmountLocator() {
        return new Locator("BASE_PRICE_AMOUNT", new By.ByXPath("//*[contains(@class,'Text_text') and (contains(text(),'Price') or contains(text(),'Harga'))]/following-sibling::*//*[contains(@class,'PriceDetail_price__VPT_I')]"));
    }

    /**
     * Comprehensive verification of the price detail section including base price, add-on packages, and total price calculation.
     * This method opens the price detail bottom sheet, performs all validations, and closes the sheet.
     *
     * @param carRentalDetail the car rental details from the search results page (containing base price)
     * @return true if all price validations pass (base price, add-on package, and total price calculation), false otherwise
     * @throws RuntimeException if any required elements are not found during the verification process
     */
    public boolean verifyPriceDetailSection(CarRentalDetail carRentalDetail) {
        openPriceDetailBottomSheet();

        boolean isBasePriceCorrect = verifyBasePricePriceDetail(carRentalDetail.srpData.carPrice, carRentalDetail.srpData.duration);
        logger.log("Base price verification result: " + isBasePriceCorrect);
        boolean isAddOnPackageCorrect = true;
        if( carRentalDetail.reviewPageData.carRentalPackage != null && carRentalDetail.reviewPageData.packagePrice != null) {
            isAddOnPackageCorrect = verifyAddOnPackageSectionWithDuration(carRentalDetail.reviewPageData.carRentalPackage, carRentalDetail.reviewPageData.packagePrice, carRentalDetail.srpData.duration,"");
            logger.log("Add-on package verification result: " + isAddOnPackageCorrect);
        }
        logger.log("Add-on package verification result: " + isAddOnPackageCorrect);
        boolean isPickUpAddOnCorrect = true;
        if (carRentalDetail.bookingPageData!=null && carRentalDetail.bookingPageData.pickUpPrice != null ){
            isPickUpAddOnCorrect = verifyAddOnPackageSectionWithDuration("Pickup", carRentalDetail.bookingPageData.pickUpPrice,carRentalDetail.srpData.duration ,carRentalDetail.bookingPageData.pickUpArea);
            logger.log("Pick-up add-on verification result: " + isPickUpAddOnCorrect);
        }
        boolean isDropOffAddOnCorrect = true;
        if (carRentalDetail.bookingPageData!=null && carRentalDetail.bookingPageData.dropOffPrice != null ){
            isDropOffAddOnCorrect = verifyAddOnPackageSectionWithDuration("Drop-off", carRentalDetail.bookingPageData.dropOffPrice, carRentalDetail.srpData.duration,carRentalDetail.bookingPageData.dropOffArea);
            logger.log("Drop-off add-on verification result: " + isDropOffAddOnCorrect);
        }
        boolean isTotalPriceCorrect = calculatePriceSum();
        logger.log("Total price calculation verification result: " + isTotalPriceCorrect);

        closePriceDetailBottomSheet();
        return isBasePriceCorrect && isAddOnPackageCorrect && isTotalPriceCorrect && isPickUpAddOnCorrect && isDropOffAddOnCorrect;
    }

    public void verifyRentalTime() {
        Assertion.assertThat("Rental time is displayed", driverTools.isElementDisplayed(RENTAL_TIME_SECTION) || driverTools.isElementDisplayed(IMPORTANT_INFO_SECTION), is(true));
    }

    public void verifyCarPackage(String packageType) {
        WebElement packageSection = driverTools.waitForElementToAppear(PRICE_REVIEW_PAGE).orElseThrow();
        driverTools.scrollIntoWebView(packageSection);

        if (packageType.equals("Regular")) {
            driverTools.click(REGULAR_PACKAGE);
            Assertion.assertThat("Package section is displayed", driverTools.isElementDisplayed(REGULAR_PACKAGE_SECTION), is(true));
        } else if (packageType.equals(("All-in"))) {
            driverTools.click(ALL_IN_PACKAGE);
            Assertion.assertThat("Package section is displayed", driverTools.isElementDisplayed(ALL_IN_PACKAGE_SECTION), is(true));
        }
    }

    public boolean verifyReviewPageIsDisplayed(String expectedCarName) {
        driverTools.waitForElementToAppear(CAR_AND_VENDOR_DETAILS);
        boolean isCarAndVendorDetailsDisplayed = driverTools.isElementDisplayed(CAR_AND_VENDOR_DETAILS);

        String carNameReviewPage = driverTools.waitForElementToAppear(CAR_NAME_REVIEW_PAGE)
                .orElseThrow().getText();

        boolean isCarNameCorrect = expectedCarName.equals(carNameReviewPage);

        return isCarAndVendorDetailsDisplayed && isCarNameCorrect;
    }

    public String selectPackageType(String type) {
        Locator PACKAGE_TYPE_CHIP = getPackageTypeLocator(type);
        WebElement rentalPac = driverTools.waitForElementToPresence(PACKAGE_TYPE_CHIP).orElseThrow();
        String selectedType = driverTools.getText(rentalPac);
        logger.log("selecting package type: " + selectedType);
        driverTools.clickWithJavascriptExecutor(rentalPac);
        return selectedType;
    }

    private Locator getPackageTypeLocator(String type) {
        String typeIndo = type;
        if(type.toLowerCase().contains("car")){
            typeIndo = "Mobil";
        }
        if (type.contains("/")){
            String[] typeParts = type.split("/");
            type = typeParts[0];
            typeIndo = typeParts[1];
        }
        return new Locator("PACKAGE_TYPE_CHIP", new By.ByXPath("//*[contains(@class, 'PackageSection_slider')]//*[contains(text(), '" + type + "') or contains(text(), '" + typeIndo + "')]"));
    }

    public String selectDefaultPackage() {
        WebElement defaultPackage = driverTools.waitForElementToPresence(DEFAULT_PACKAGE).orElseThrow();
        String defaultPackageText = driverTools.getText(defaultPackage);
        logger.log("selecting default package: " + defaultPackageText);
        driverTools.clickWithJavascriptExecutor(defaultPackage);
        return defaultPackageText;
    }

    private Locator getPackagePriceLocator(String type) {
        String typeIndo = getOppositeLanguageText(type);
        return new Locator("PACKAGE_PRICE", new By.ByXPath("//*[contains(@class, 'PackageSection_card') and .//span[contains(text(), '" + type + "') or contains(text(), '" + typeIndo + "')]]//span[contains(@class, 'PackageSection_price_item') and contains(@class,'Text_variant_price')]"
        ));
    }

    public String getSelectedPackagePrice(String type) {
        Locator priceLocator = getPackagePriceLocator(type);
        String expectedPrice = driverTools.getText(
                driverTools.waitForElementToAppear(priceLocator).orElseThrow()
        );
        String s = expectedPrice.replaceAll("/.*", "").trim();
        logger.log(s);
        return s;
    }

    public void VerifyTravelAddOnPrice(String type) {
        String expectedPrice = getSelectedPackagePrice(type);

        driverTools.waitForElementToAppear(OPEN_PRICE_BOTTOM_SHEET);
        driverTools.scrollIntoWebView(OPEN_PRICE_BOTTOM_SHEET);
        driverTools.click(OPEN_PRICE_BOTTOM_SHEET);

        driverTools.waitForElementToAppear(TRAVEL_ADD_ONS, 3);
        Assertion.assertThat("Travel add ons showing under bottom sheet", driverTools.isElementDisplayed(TRAVEL_ADD_ONS), is(true));

        String actualPrice = String.valueOf(driverTools.getText(TRAVEL_ADD_ONS_PRICE));
        Assertion.assertThat("Travel add ons have same price as showing on PDP PackageType", actualPrice, containsString(expectedPrice));

        driverTools.click(CLOSE_PRICE_BOTTOM_SHEET);
    }

    public abstract String getTotalPrice();

    public boolean isRentalCoverageAreaTitleDisplayed() {
        driverTools.waitForElementToAppear(RENTAL_AREA_COVERAGE_TEXT);

        return driverTools.isElementDisplayed(RENTAL_AREA_COVERAGE_TEXT);
    }

    public boolean isCoverageAreaMapDisplayed() {
        driverTools.waitForElementToAppear(COVERAGE_AREA_MAP);

        return driverTools.isElementDisplayed(COVERAGE_AREA_MAP);
    }

    public boolean isListOfCoverageAreaDisplayed() {
        driverTools.waitForElementToAppear(COVERAGE_AREA_LIST);

        return driverTools.isElementDisplayed(COVERAGE_AREA_LIST);
    }

    public void proceedToBookingPage(){
        driverTools.click(PROCEED_TO_BOOKING_FORM);
    }

    public abstract boolean verifyReviewPageIsDisplayedForWithDriver(String location);

    public boolean verifyRegionNameInNavigationBar(String expectedRegionName) {
        WebElement regionElement = driverTools.waitForElementToAppear(REGION_NAME_IN_NAVIGATION_BAR).orElseThrow();
        String actualRegionName = driverTools.getText(regionElement);
        logger.log("Actual region name in navigation bar: " + actualRegionName);
        boolean isRegionNameCorrect = actualRegionName.contains(expectedRegionName);
        Assertion.assertThat("Region name in navigation bar { "+actualRegionName+" } matches expected { "+ expectedRegionName + " }", actualRegionName, containsString(expectedRegionName));
        return isRegionNameCorrect;
    }
}
