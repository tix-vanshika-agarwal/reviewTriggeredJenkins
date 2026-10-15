package com.tiket.web.page.accommodation.homes.homesSearchResultsPage;

import com.tiket.appium.PopupObserver;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.tiket.testbase.Assertion;
import static org.hamcrest.Matchers.is;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import java.util.List;

public abstract class HomesSearchResultPageWeb<T extends WebDriver> extends WebPage<T> {

    public HomesSearchResultPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HomesSearchResultPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HomesSearchResultPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new HomesSearchResultPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    protected static final Locator SRP_LOADING = new Locator("SRP_LOADING", new By.ByXPath("//*[contains(@class, 'main_loading')]"));
    protected static final Locator HOTEL_CARD = new Locator("HOTEL_CARD", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]"));
    private static final Locator DONE_BUTTON = new Locator("DONE_BUTTON", new By.ByXPath("//div[contains(@class, 'Modal_modal_footer_button_group')]//button"));
    protected static final Locator ADVANCED_FILTER_CHIP = new Locator("ADVANCED_FILTER_CHIP", new By.ByXPath("//*[@data-testid='advanced-filter-chip']"));
    protected static final Locator ADVANCED_FILTER_SAVE = new Locator("ADVANCED_FILTER_SAVE", new By.ByXPath("//button[text()='Save' or text()='Simpan']"));
    protected static final Locator ADVANCED_FILTER_POPULAR_ITEM = new Locator("ADVANCED_FILTER_POPULAR_CHIP", new By.ByXPath("//*[contains(@class, 'PopularFilter_chips_wrapper')]//button[1]"));
    protected static final Locator ADVANCED_FILTER_SWITCH_INPUT_ITEM = new Locator("ADVANCED_FILTER_SWITCH_INPUT_ITEM", new By.ByXPath("//*[contains(@class, 'SwitchInput_content')]/following-sibling::*[contains(@class, 'SwitchInput_toggle')][1]"));
    protected static final Locator ADVANCED_FILTER_FACILITY_GROUP_ITEM = new Locator("ADVANCED_FILTER_FACILITY_GROUP_ITEM", new By.ByXPath("//button[.//h4[text()='WiFi']]"));
    protected static final Locator ADVANCED_FILTER_CHECKBOX_ITEM = new Locator("ADVANCED_FILTER_CHECKBOX_ITEM", new By.ByXPath("//*[contains(@class, 'CheckboxGroupInput_group')]//input[@data-testid='checkbox'][1]"));
    protected static final Locator QUICK_FILTER_SORT_CHIP = new Locator("QUICK_FILTER_SORT_CHIP", new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[contains(text(), 'Sort') or contains(text(), 'Urutkan')]"));
    protected static final Locator QUICK_FILTER_SORT_ITEM = new Locator("QUICK_FILTER_SORT_ITEM", new By.ByXPath("//*[@data-testid='sort-container']//label[2]//span[contains(@class, 'Radio_checkbox')]"));
    protected static final Locator QUICK_FILTER_STAR_RATING_CHIP = new Locator("QUICK_FILTER_STAR_RATING_CHIP", new By.ByXPath("//span[text()='Flower']"));
    protected static final Locator QUICK_FILTER_STAR_RATING_SAVE = new Locator("QUICK_FILTER_STAR_RATING_SAVE", new By.ByXPath("//*[contains(@class, 'PopupStarFilter_action_container')]//button[text()='Save' or text()='Simpan']"));
    private static final Locator POPUP_GOT_IT = new Locator("POPUP_GOT_IT", new By.ByXPath("//button[contains(@class, 'Coachmark_coachmark_done_btn')]"));
    private static final Locator MASTER_TAG = new Locator("MASTER_TAG", new By.ByXPath("//div[contains(@class, 'MasterTagFilter_chips_wrapper')]"));
    protected static final Locator FILTER_STAR_RATING_APPLIED_ON_HOTEL_LIST = new Locator("FILTER_STAR_RATING_APPLIED_ON_HOTEL_LIST", new By.ByXPath("//div[@data-testid='nha-star-rating-container']"));
    protected static final Locator FILTER_FACILITY_APPLIED_ON_HOTEL_LIST = new Locator("FILTER_FACILITY_APPLIED_ON_HOTEL_LIST", new By.ByXPath("//span[contains(text(), 'Free Wifi')]"));
    private static final Locator PRODUCT_CARD = new Locator("PRODUCT_CARD", new By.ByXPath("//a[@data-testid='full-product-card']"));
    protected static final Locator GENERAL_FILTER_BUTTON = new Locator("GENERAL_FILTER_BUTTON", new By.ByXPath("//span[@class='mobile_chip_text_wrapper__WvLCf' and (text()='Filter' or text()='Penyaring')]"));
    protected static final Locator ACCOMMODATION_TYPE_BUTTON = new Locator("ACCOMMODATION_TYPE_FILTER", new By.ByXPath("//span[contains(text(), 'Free Wifi')]"));
    protected static final Locator SEE_ALL_BUTTON = new Locator("SEE_ALL_BUTTON", new By.ByXPath("//h3[text()='Accommodation Types']/following::button[normalize-space()='See all' or normalize-space()='Lihat semua'][1]"));
    protected static final Locator ACCOMMODATION_FILTER = new Locator("ACCOMMODATION_FILTER", new By.ByXPath("//span[@data-testid='facility-group-input-label' and (normalize-space(text())='Villa' or normalize-space(text())='Vila')]"));
    protected static final Locator SAVE_FILTER_BUTTON = new Locator("SAVE_FILTER_BUTTON", new By.ByXPath("//button[normalize-space(text())='Save' or normalize-space(text())='Simpan']"));
    protected static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[normalize-space(text())='Save' or normalize-space(text())='Simpan']"));
    protected static final Locator APPLIED_FILTER_GREEN_DOT = new Locator("APPLIED_FILTER_GREEN_DOT", new By.ByXPath("//span[contains(@class,'Notif_notif__')]"));
    protected static final Locator APPLIED_ACCOMMODATION_FILTER = new Locator("APPLIED_ACCOMMODATION_FILTER", new By.ByXPath("//span[@data-testid='nha-star-label-text' and (normalize-space(text())='Villa' or normalize-space(text())='Vila')]"));
    private static final Locator HOTEL_CARD_LIST = new Locator("HOTEL_CARD_LIST",new By.ByXPath("(//a[contains(@class,'FullProductCard_container') and @data-testid='full-product-card'])"));
    private static final Locator TOTAL_PRICE_FILTER = new Locator("TOTAL_PRICE_FILTER", By.xpath("(//div[contains(@class, 'FilterPriceRange_toggle_item')]//div[contains(@class, 'Toggle_toggle_')])[1]"));
    private static final Locator DATE_ON_SRP = new Locator("DATE_ON_SRP", new By.ByXPath("(//span[contains(@class, 'Text_text') and contains(@class, 'Text_size_b3')])[1]"));

    private static Locator getStarRatingPerProductCardByIndex(int index) {
        return new Locator("STAR_RATING", new By.ByXPath(String.format("(//a[@data-testid='full-product-card'])[%d]//img[@data-testid='nha-star-rating-icon-full']",index)));
    }


    public abstract void applyQuickFilters(int star);


    public abstract void applyAdvancedFilters();


    public abstract boolean isAdvancedFilterApplied();
    public abstract String getLocationHeaderLabel();
    public abstract String getDateHeaderLabel();
    public abstract String getGuestHeaderLabel();


    public void clickDone() {
        driverTools.click(DONE_BUTTON);
    }

    protected void clickAdvancedFilterItem(Locator locator, Boolean withJSExecutor) {
        driverTools.waitForElementToPresence(locator).orElseThrow();
        driverTools.scrollElementToCenter(locator);
        if (withJSExecutor) {
            driverTools.clickWithJavascriptExecutor(locator);
        } else {
            driverTools.click(locator);
        }
    }

    public abstract void updateStayLocation(String location);

    public abstract String getLocationNameFromInput();

    public void clickPopUp() {
        PopupObserver.inject(driver, List.of(POPUP_GOT_IT));
    }

    protected Locator getSearchFormResultLocator(String location) {
        return new Locator("SEARCH_FORM_RESULT", new By.ByXPath("//*[@data-testid='destination-name']//*[text()='" + location + "']"));
    }

    public abstract void clickSearchOnSRP();


    public boolean isStarRatingFilterAppliedToHotelList(int star) {
        driverTools.waitForElementsToAppear(FILTER_STAR_RATING_APPLIED_ON_HOTEL_LIST, 3);
        boolean isNumberOfStarCorrect = true;

        for (int i = 1; i<=star; i++) {
            Locator QUICK_FILTER_STAR_RATING_ITEM = new Locator("QUICK_FILTER_STAR_ITEM", new By.ByXPath("((//a[contains(@class, 'FullProductCard_container')])[1]//img[@data-testid='nha-star-rating-icon'])["+i+"]"));
            isNumberOfStarCorrect = driverTools.isElementDisplayed(QUICK_FILTER_STAR_RATING_ITEM);
            logger.log("Got " + i + " stars and it's " + isNumberOfStarCorrect);
        }

        int starPlusOne = star+1;
        Locator QUICK_FILTER_STAR_RATING_ITEM_PLUS_ONE = new Locator("QUICK_FILTER_STAR_ITEM", new By.ByXPath("((//a[contains(@class, 'FullProductCard_container')])[1]//img[@data-testid='nha-star-rating-icon'])["+starPlusOne+"]"));
        if (driverTools.isElementDisplayed(QUICK_FILTER_STAR_RATING_ITEM_PLUS_ONE)) {
            isNumberOfStarCorrect = false;
            logger.log("Too many stars, false");
        }

        return isNumberOfStarCorrect;
    }

    public boolean isFilterByStarRatingApplied(int expectedStar) {
        boolean result = false;
        driverTools.waitForElementToAppear(PRODUCT_CARD);
        List<WebElement> productList = driverTools.elements(PRODUCT_CARD).orElseThrow();
        logger.log("productList size: " + productList.size());
        for (int i=1; i<=productList.size(); i++) {
            List<WebElement> actualStar = driverTools.waitForElementsToAppear(getStarRatingPerProductCardByIndex(i)).orElseThrow();
            if (actualStar.size() >= expectedStar) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        return result;
    }

    public boolean isFacilityFilterAppliedToHotelList() {
        driverTools.waitForElementsToAppear(FILTER_FACILITY_APPLIED_ON_HOTEL_LIST, 3);
        return driverTools.isElementDisplayed(FILTER_FACILITY_APPLIED_ON_HOTEL_LIST);
    }

    protected Locator getHotelCardLocator(int index) {
        String baseLocator = "(//a[contains(@class, 'ProductCard_container')])[%d]";
        return new Locator("HOTEL_CARD", new By.ByXPath(String.format(baseLocator, index)));
    }

    public void clickHotelCard(int index) {
        Locator destinationHotelCard = getHotelCardLocator(index);
        driverTools.click(destinationHotelCard);
        driverTools.switchToNewTab();
    }

    public void clickBack(){
        driverTools.back();
    }

    public void clickAccommodationFilter() {
        driverTools.waitForElementToAppear(GENERAL_FILTER_BUTTON);
        driverTools.clickWithJavascriptExecutor(GENERAL_FILTER_BUTTON);
        driverTools.scrollIntoWebView(SEE_ALL_BUTTON);
        driverTools.waitForElementToAppear(SEE_ALL_BUTTON);
        driverTools.clickWithJavascriptExecutor(SEE_ALL_BUTTON);
    }

    public void selectAccommodationFilter() {
        driverTools.waitForElementToAppear(ACCOMMODATION_FILTER);
        driverTools.clickWithJavascriptExecutor(ACCOMMODATION_FILTER);
    }

    public void clickAccommodationFilterSave() {
        driverTools.waitForElementToAppear(SAVE_FILTER_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_FILTER_BUTTON);
    }


    public boolean checkAccommodationFilter() {
        driverTools.scrollIntoWebView(APPLIED_FILTER_GREEN_DOT);
        driverTools.waitForElementToAppear(APPLIED_FILTER_GREEN_DOT);
        boolean isGreenDotVisible = driverTools.isElementDisplayed(APPLIED_FILTER_GREEN_DOT);

        driverTools.waitForElementToAppear(APPLIED_ACCOMMODATION_FILTER);
        boolean isFilterVisible = driverTools.isElementDisplayed(APPLIED_ACCOMMODATION_FILTER);

        return isGreenDotVisible && isFilterVisible;
    }

    public void verifyAccommodationFilterForFiveHotelCards() {
        driverTools.waitForElementToAppear(HOTEL_CARD_LIST, 40);
        List<WebElement> hotelCards = driver.findElements(HOTEL_CARD_LIST.by());
        int checkCount = Math.min(hotelCards.size(), 5);
        for (int i = 0; i < checkCount; i++) {
            driverTools.scrollIntoWebView(hotelCards.get(i));
            String appliedFilter = driverTools.getText(APPLIED_ACCOMMODATION_FILTER).orElse("").trim();
            boolean filterMatches = appliedFilter.equals("Villa") || appliedFilter.equals("Vila");
            Assertion.assertThat("Applied filter match selected filter for hotel card " + (i + 1), filterMatches, is(true));
        }
    }

    public int getNumberOfDays() {
        String stayDate = driverTools.getText(DATE_ON_SRP).orElseThrow();
        String[] dates = stayDate.split(" - ");

        String startDateStr = dates[0].trim();
        String endDateStr = dates[1].trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH);

        int currentYear = LocalDate.now().getYear();

        MonthDay startMonthDay = MonthDay.parse(startDateStr, formatter);
        LocalDate startDate = startMonthDay.atYear(currentYear);

        MonthDay endMonthDay = MonthDay.parse(endDateStr, formatter);
        LocalDate endDate = endMonthDay.atYear(currentYear);

        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            endDate = endMonthDay.atYear(currentYear + 1);
        }

        int duration = (int) ChronoUnit.DAYS.between(startDate, endDate);
        logger.log("duration: " + duration);
        return duration;
    }

    private Locator getNHAPriceLocator(String currency) {
        return new Locator("HOTEL_CARD", new By.ByXPath("(//div[contains(@class, 'Text_variant_price') and contains(text(), '" + currency + "')])[1]"));
    }


    private Locator getStrikethroughPriceLocator(String currency) {
        return new Locator("STRIKETHROUGH_PRICE", new By.ByXPath("(//div[contains(@class, 'Text_diagonal_strikethrough')]//div[contains(@class, 'Text_text') and contains(text(), '" + currency + "')])[1]"));
    }

    public int getFirstNHAPrice(String currency) {
        driverTools.waitForElementToAppear(POPUP_GOT_IT, 10);
        driverTools.swipeDown(3);
        Locator newPrice = getNHAPriceLocator(currency);
        Locator originalPrice = getStrikethroughPriceLocator(currency);

        String priceText = "";
        if (driverTools.isElementDisplayed(newPrice)) {
            priceText = String.valueOf(driverTools.getText(newPrice));
            logger.log("Using new price: " + priceText);
        }

        else if (driverTools.isElementDisplayed(originalPrice)) {
            priceText = String.valueOf(driverTools.getText(originalPrice));
            logger.log("Using original (strikethrough) price: " + priceText);
        }
        else {
            throw new RuntimeException("No price element found for currency: " + currency);
        }

        String numericValue = priceText.replaceAll("[^0-9]", "");
        int priceValue = Integer.parseInt(numericValue);
        logger.log("price: " + priceValue);
        return priceValue;
    }

    public void selectTotalPriceToggle(){
        driverTools.scrollIntoWebView(TOTAL_PRICE_FILTER);
        driverTools.waitForElementToPresence(TOTAL_PRICE_FILTER, 20);
        if (driverTools.isElementDisplayed(TOTAL_PRICE_FILTER)){
            driverTools.clickWithJavascriptExecutor(TOTAL_PRICE_FILTER);
        }
    }

    public void verifyPriceCombination(int priceBeforeFilter, int priceAfterFilter, int duration) {
        if (duration == 1) {
            Assertion.assertThat("Price is same for 1 day booking", priceBeforeFilter, is(priceAfterFilter));
        } else {
            Assertion.assertThat("Total price equals per-night price multiplied by duration", priceBeforeFilter * duration, is(priceAfterFilter));
        }
    }
}
