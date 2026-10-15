package com.tiket.web.page.accommodation.hotels.hotelLandingPage;

import com.tiket.appium.PopupObserver;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;
import java.util.Map;


public abstract class HotelLandingPageWeb<T extends WebDriver> extends WebPage<T> {

    public HotelLandingPageWeb(Session<T> session) { super(session); }

    public static <T extends WebDriver> HotelLandingPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HotelLandingPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new HotelLandingPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator SEARCH_FORM = new Locator("SEARCH_FORM", new By.ByXPath("//*[@data-testid='destination-input-text']"));
    private static final Locator SEARCH_FORM_CHECK = new Locator("SEARCH_FORM_CHECK",  AppiumBy.xpath("//*[@text=\"Near you\" and @class=\"android.widget.TextView\"]"));
    private static final Locator SEARCH_FORM_INPUT = new Locator("SEARCH_FORM_INPUT", new By.ByXPath("//*[@data-testid='destination-search-box']"));
    private static final Locator SEARCH_SUBMIT_BUTTON = new Locator("SEARCH_SUBMIT_BUTTON", AppiumBy.xpath("//*[@data-testid='search-button-lp']"));
    private static final Locator GOT_IT_POPUP = new Locator("GOT_IT_POPUP", AppiumBy.xpath("//button[text()='Got it']"));
    private static final Locator ROOM_GUEST_BUTTON = new Locator("ROOM_GUEST_BUTTON", AppiumBy.xpath("//*[@data-testid='rooms-and-guests']"));
    private static final Locator CURRENT_ROOM_AMOUNT = new Locator("CURRENT_ROOM_AMOUNT", AppiumBy.xpath("//*[@data-testid='rooms_container']//*[contains(@class,'quantity_input')]"));
    private static final Locator ROOM_PLUS = new Locator("ROOM_PLUS", AppiumBy.xpath("//*[@data-testid='rooms_container']//*[contains(@class,'QuantityEditor_plus_icon')]"));
    private static final Locator ROOM_MINUS= new Locator("ROOM_MINUS", AppiumBy.xpath("//*[@data-testid='rooms_container']//*[contains(@class,'QuantityEditor_minus_icon')]"));
    private static final Locator DONE_SET_ROOM_GUEST = new Locator("DONE_SET_ROOM_GUEST", AppiumBy.xpath("//button//*[text()='Done' or text()='Selesai']"));
    public static final Locator DATE_FIELD_BUTTON = new Locator("DATE_FIELD_BUTTON", AppiumBy.xpath("//*[@data-testid='date-picker']"));
    private static final Locator ADULT_PLUS = new Locator("ADULT_PLUS", AppiumBy.xpath("(//*[@data-testid='guest_container']//*[contains(@class,'button')])[2]"));
    private static final Locator ADULT_MINUS = new Locator("ADULT_MINUS", AppiumBy.xpath("(//*[@data-testid='guest_container']//*[contains(@class,'button')])[1]"));
    private static final Locator CURRENT_ADULT_AMOUNT = new Locator("CURRENT_ADULT_AMOUNT", AppiumBy.xpath("//*[@data-testid='guest_container']//*[contains(@class, 'quantity_input')]"));
    private static final Locator FIRST_HOTEL_CARD = new Locator("FIRST_HOTEL_CARD", AppiumBy.xpath("(//h3[@data-testid='product-card-property-name'])[1]"));
    private static final Locator FIRST_HOTEL_CARD_PDP = new Locator("FIRST_HOTEL_CARD_PDP", AppiumBy.xpath("//h1[@data-testid='name']\n"));
    private static final Locator NEAR_ME = new Locator("NEAR_ME", AppiumBy.xpath("//*[@class=\"android.widget.Image\" and ./parent::*[@class=\"android.view.View\" and ./preceding-sibling::*[@text=\"Going anywhere?\"]]]"));
    private static final Locator FIRST_POPULAR_DESTINATION = new Locator("FIRST_POPULAR_DESTINATION", new By.ByXPath("//*[contains(@class, 'PopularDestination_chips_wrapper')]//*[contains(@class, 'Text_variant_highEmphasis')][1]"));
    private static final Locator ANDROID_ACCEPT_ALERT = new Locator("ANDROID_ACCEPT_ALERT", AppiumBy.xpath("//*[@resource-id=\"com.android.chrome:id/positive_button\"]"));
    private static final Locator CLEAR_ALL_BUTTON = new Locator("CLEAR_ALL_BUTTON", AppiumBy.xpath("//span[@data-testid='recent_list_item_clear']"));
    private static final Locator RECENTLY_SEARCHED_HOTELS = new Locator("RECENTLY_SEARCHED_HOTELS", AppiumBy.xpath("//div[contains(@class,'RecentListItem_recent_search_item_wrapper')]"));
    private static final Locator CLEAR_ALL_RECENT_LIST_BUTTON = new Locator("CLEAR_ALL", new By.ByXPath("//*[@data-testid=\"recent_list_item_clear\"]"));
    private static Locator getPageModulePrimaryButtonById(String id) {
        return new Locator("PAGE_MODULE_PRIMARY_BUTTON", new By.ByXPath(String.format("//section[@id='%s']//a[contains(@class,'Button')]", id)));
    }
    private static Locator getPageModuleProductCardByIndex(int index) {
        return new Locator("PAGE_MODULE_PRODUCT_CARD", new By.ByXPath(String.format("(//*[@data-testid=\"accom-product-card\"])[%d]", index)));
    }

    private static Locator getPageModuleProductCardByName(String id, String name) {
        return new Locator("PAGE_MODULE_PRODUCT_CARD", new By.ByXPath(String.format("//section[@id='%s']//*[@data-testid=\"accom-product-card\"]//h2[text()='%s']", id, name)));
    }

    public abstract boolean isSearchHistoryDestinationLabelDisplayed(String expected, int index);
    public abstract boolean isSearchHistoryDateLabelDisplayed(String expected, int index);
    public abstract boolean isSearchHistoryGuestLabelDisplayed(String expected, int index);

    public abstract void closeContinueOnWebPopup();

    public abstract void setStayDate(String type, String date);
    public abstract void clickOnCalendarWidgetToOpen();

    public abstract void clickDoneSetRoomGuest();

    public abstract void clickDoneSetStayDate();

    public String getFirstPopularDestinationText() {
        WebElement firstPopularDestinationElement = driverTools.waitForElementToAppear(FIRST_POPULAR_DESTINATION).orElseThrow();
        return driverTools.getText(firstPopularDestinationElement);
    }

    public void clickSearchForm() {
        driverTools.waitForElementToPresence(SEARCH_FORM, 5).orElseThrow();
        driverTools.click(SEARCH_FORM);
    }

    public void clickFirstPopularDestination() {
        driverTools.clickWithJavascriptExecutor(FIRST_POPULAR_DESTINATION);
    }

    public static final Locator ALLOW_ONCE = new Locator("ALLOW_ONCE", AppiumBy.xpath("//*[@resource-id=\"com.android.chrome:id/positive_button\"]"));
    public static final Locator ALLOW = new Locator("ALLOW", AppiumBy.xpath("//*[@resource-id=\"com.android.permissioncontroller:id/permission_allow_one_time_button\"]"));

    public abstract void clickNearMeLocation();

    public boolean isNearMeSearchAlreadySet() {
        String near = driverTools.getText(SEARCH_FORM).get();

        Assertion.assertThat("Near Me search is already set", near, is(either(containsString("Near you")).or(containsString("Di dekat kamu"))));

        return true;
    }

    public void searchStayLocation(String location) {
        driverTools.waitForElementToPresence(SEARCH_FORM, 5).orElseThrow();
        driverTools.click(SEARCH_FORM);
        driverTools.waitForElementToAppear(SEARCH_FORM_INPUT).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEARCH_FORM_INPUT);
        driverTools.sendKeys(SEARCH_FORM_INPUT, location);
        WebElement searchFormResult = driverTools.waitForElementToAppear(getSearchFormResultLocator(location)).orElseThrow();
        driverTools.click(searchFormResult);
    }

    private Locator getSearchFormResultLocator(String location) {
        String translatedLocation = translateLocationToIndonesian(location);
        return new Locator("SEARCH_FORM_RESULT", new By.ByXPath(
                        "//*[@data-testid='item-name'][" +
                                "contains(normalize-space(.), " + CoreFunctions.toXPathLiteral(location) +
                                ") or contains(normalize-space(.), " + CoreFunctions.toXPathLiteral(CoreFunctions.titleize(location)) +
                                ") or contains(normalize-space(.), " + CoreFunctions.toXPathLiteral(translatedLocation) +
                                ") or contains(normalize-space(.), " + CoreFunctions.toXPathLiteral(CoreFunctions.titleize(translatedLocation)) + ")]"));
    }

    private static final Map<String, String> LOCATION_TRANSLATIONS = Map.of(
            "Central Jakarta", "Jakarta Pusat",
            "West Jakarta", "Jakarta Barat",
            "East Jakarta", "Jakarta Timur",
            "South Jakarta", "Jakarta Selatan",
            "North Jakarta", "Jakarta Utara"
    );

    private String translateLocationToIndonesian(String location) {
        return LOCATION_TRANSLATIONS.getOrDefault(location, location);
    }
    public void typeInSearchForm(String location) {
        driverTools.waitForElementToPresence(SEARCH_FORM, 5).orElseThrow();
        driverTools.click(SEARCH_FORM);
        driverTools.waitForElementToAppear(SEARCH_FORM_INPUT).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEARCH_FORM_INPUT);
        driverTools.sendKeys(SEARCH_FORM_INPUT, location);
    }

    public boolean isAutocompleteSuggestionVisible(String location) {
        return driverTools.waitForElementToAppear(getSearchFormResultLocator(location)).isPresent();
    }

    /**
     * Verifies that the autocomplete suggestion list contains at least one item whose name
     * contains the typed text (case-insensitive). Useful when typing partial keywords
     * (e.g. "SDET") where the suggestion is rendered as part of a larger item name
     * (e.g. "SDET Domestic Hotel Jakarta") rather than an exact match.
     *
     * @param text the partial text typed into the search form
     * @return true if at least one autocomplete item-name contains the text in any casing
     */
    public boolean isAutocompleteListContainsText(String text) {
        String textLower = text == null ? "" : text.strip().toLowerCase();
        Locator listItem = new Locator("AUTOCOMPLETE_LIST_ITEM_" + textLower.toUpperCase(),
                By.xpath("//*[@data-testid='item-name' and contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'" + textLower + "')]"));
        return driverTools.waitForElementToPresence(listItem, 10).isPresent();
    }

    /**
     * Clicks the first autocomplete suggestion whose item-name contains the given text
     * (case-insensitive) and returns the full text of the clicked item. This is useful
     * for partial-keyword searches where the suggestion is rendered as part of a larger
     * item name (e.g. typing "SDET" matches "SDET Domestic Hotel Jakarta").
     *
     * @param text the partial text typed into the search form
     * @return the full text of the clicked autocomplete item
     */
    public String clickFirstAutocompleteSuggestionContaining(String text) {
        String textLower = text == null ? "" : text.strip().toLowerCase();
        Locator listItem = new Locator("AUTOCOMPLETE_LIST_ITEM_" + textLower.toUpperCase(),
                By.xpath("//*[@data-testid='item-name' and contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'" + textLower + "')]"));
        WebElement element = driverTools.waitForElementToPresence(listItem, 10).orElseThrow();
        String fullText = driverTools.getText(element);
        driverTools.clickWithJavascriptExecutor(element);
        return fullText;
    }

    public String getSearchFieldText() {
        return driverTools.getText(SEARCH_FORM).orElse("");
    }

    public void clickSubmit() {
        driverTools.clickWithJavascriptExecutor(SEARCH_SUBMIT_BUTTON);
        handlePopup();
    }

    private void handlePopup() {
        PopupObserver.inject(driver, List.of(GOT_IT_POPUP));
    }

    public boolean isLandingPageVisible() {
        driverTools.waitForElementToAppear(SEARCH_FORM,5);
        return driverTools.isElementDisplayed(SEARCH_FORM);
    }

    /**
     * Verifies that at least one hotel card on the SRP shows a "from {poi}" / "dari {poi}" address text,
     * confirming the search result is correctly bound to the selected POI.
     *
     * @param poi the POI name selected on the landing page (e.g. "National Monument")
     * @return true if at least one hotel card's address contains the POI text in EN or ID
     */
    public boolean verifyPoiOnHotelCards(String poi) {
        return driverTools.waitForElementToPresence(getPoiHotelAddress(poi), 10).isPresent();
    }

    private Locator getPoiHotelAddress(String poi) {
        String poiLower = poi == null ? "" : poi.strip().toLowerCase();
        return new Locator("POI_HOTEL_ADDRESS", new By.ByXPath("//span[contains(@class,'FullProductCard_hotel_address') and (contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'from') or contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'dari')) and contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'"+poiLower+"')]"));
    }

    protected List<Locator> getStartAndEndDateLocators(String type, String date) {
        String[] stayDate = date.split("-");

        if(stayDate[0].equals("0")){
            return null;
        }

        int startDate = Integer.parseInt(stayDate[0]);
        int endDate = Integer.parseInt(stayDate[1]);

        if(!type.equals("add date")){
            if (startDate <= 0
                    || startDate > 31
                    || startDate > endDate
                    || endDate > 31
                    || endDate <= 0) {
                throw new DriverToolsException("Test data 'date' with value '" + date + "' is not valid, please check again");
            }
        }

        if (type.equals("range date")) {
            int randomStartDate = generateRandomInt(startDate, endDate - 1);
            int randomEndDate = generateRandomInt(randomStartDate + 1, endDate);

            startDate = randomStartDate;
            endDate = randomEndDate;
        }

        if(type.equals("add date")){
            String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
            String expectedDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate,startDate);
            startDate = Integer.parseInt(expectedDate.split("-")[2]);
            String expectedEndDate = CoreFunctions.addToDateYYYY_MM_DD(expectedDate,endDate);
            endDate = Integer.parseInt(expectedEndDate.split("-")[2]);
        }

        logger.log("stringStartDate is '" + startDate + "', stringEndDate is '" + endDate + "'");

        final Locator SELECTED_START_DATE = getDateElement("SELECTED_START_DATE", startDate);
        final Locator SELECTED_END_DATE = getDateElement("SELECTED_END_DATE", startDate, endDate);

        return List.of(SELECTED_START_DATE, SELECTED_END_DATE);
    }

    public void clickFirstHotelCard(){
        handlePopup();
        if (driverTools.waitForElementToPresence(FIRST_HOTEL_CARD).isPresent())
            driverTools.clickWithJavascriptExecutor(FIRST_HOTEL_CARD);
        Assertion.assertThat("Hotel is opened", driverTools.isElementDisplayed(FIRST_HOTEL_CARD_PDP), is(true));
    }

    public void clickRoomAndGuestButton(){
        driverTools.click(ROOM_GUEST_BUTTON);
    }

    public void setTotalRoom(int room){
        driverTools.waitForElementToAppear(CURRENT_ROOM_AMOUNT);
        int currentRoom = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ROOM_AMOUNT,"value"));
        logger.log("currentRoom is '" + currentRoom + "'");
        if(room > currentRoom){
            for(int i = 0; i < room - currentRoom; i++){
                driverTools.click(ROOM_PLUS);
            }
        } else if(room < currentRoom){
            for(int i = 0; i < currentRoom - room; i++){
                driverTools.click(ROOM_MINUS);
            }
        }
    }

    public abstract void saveDate();

    public void clickDone(){
        driverTools.clickWithJavascriptExecutor(DONE_SET_ROOM_GUEST);
    }

    protected Locator getDateElement(String elementName, int date) {
        return new Locator(elementName, AppiumBy.xpath("(//*[not(contains(@class,'disabled')) and contains(@class,'Day_day_number') and text()='" + date + "'])[1]"));
    }

    protected Locator getDateElement(String elementName, int startDate, int endDate) {
        return new Locator(elementName, AppiumBy.xpath("//*[not(contains(@class,'disabled')) and contains(@class,'Day_day_number') and text()='" + startDate + "']//following::*//*[contains(@class,'Day_day_number') and text()='" + endDate + "']"));
    }

    public void setTotalGuest(int adult) {
        driverTools.waitForElementToAppear(CURRENT_ADULT_AMOUNT);
        int currentAdult = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ADULT_AMOUNT, "value"));
        logger.log("currentAdult is '" + currentAdult + "'");
        if (adult > currentAdult) {
            for (int i = 0; i < adult - currentAdult; i++) {
                driverTools.clickWithJavascriptExecutor(ADULT_PLUS);
            }
        } else if (adult < currentAdult) {
            for (int i = 0; i < currentAdult - adult; i++) {
                driverTools.clickWithJavascriptExecutor(ADULT_MINUS);
            }
        }
    }

    protected Locator getLastViewedCloseButton(int index) {
        String baseLocator = "//*[@data-testid='delete_item_icon'][%d]";
        return new Locator("CLOSE_LAST_VIEWED", new By.ByXPath(String.format(baseLocator, index)));
    }

    public void closeRecentlyViewedItem(int count) {
        for (int i = 1; i < count; i++) {
            driverTools.click(getLastViewedCloseButton(1));
        }
    }

    public void clickClearAll(){
        driverTools.click(CLEAR_ALL_BUTTON);
    }

    public Boolean isRecentlySearchedVisible() {
        return driverTools.isElementDisplayed(RECENTLY_SEARCHED_HOTELS);
    }

    public void clickPageModulePrimaryButton(String id) {
        driverTools.waitForElementToAppear(getPageModulePrimaryButtonById(id));
        driverTools.click(getPageModulePrimaryButtonById(id));
    }

    public void clearAllRecentList() {
        driverTools.waitForElementToAppear(CLEAR_ALL_RECENT_LIST_BUTTON);
        driverTools.scrollIntoWebView(SEARCH_SUBMIT_BUTTON);
        driverTools.click(CLEAR_ALL_RECENT_LIST_BUTTON);
    }

    public void clickPageModuleProductCardByIndex(int index) {
        driverTools.waitForElementToAppear(getPageModuleProductCardByIndex(index));
        driverTools.click(getPageModuleProductCardByIndex(index));
    }

    public void clickPageModuleProductCardByName(String id, String name) {
        driverTools.waitForElementToAppear(getPageModuleProductCardByName(id, name));
        driverTools.click(getPageModuleProductCardByName(id, name));
    }
}
