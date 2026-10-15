package com.tiket.web.page.ttd.TtdSelectedPackageDetailPage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.stream.IntStream;

import static com.tiket.core.CoreFunctions.convertPrice;
import static com.tiket.core.CoreFunctions.generateRandomInt;

public class TtdSelectedPackageDetailPageWeb<T extends WebDriver> extends WebPage<T> {

    public TtdSelectedPackageDetailPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TtdSelectedPackageDetailPageWeb<T> getInstance(Session<T> session) {
        return new TtdSelectedPackageDetailPageWeb<>(session);
    }

    private static final Locator AVAILABLE_DATE_TEXT = new Locator("AVAILABLE_DATE_TEXT", AppiumBy.xpath("//button[contains(@class,'Day_day') and not(contains(@class,'selected')) and not(@disabled)]//span[contains(@class,'Day_day_number') and contains(@class,'Text_text')]"));
    private static final Locator BOOK_BUTTON = new Locator("BOOK_BUTTON", AppiumBy.xpath("//button[@type='submit' and contains(@class, 'KV-nSG_button')] | //*[contains(@style,'book-btn')] | //*[contains(@class,'totalpayment_wrapper')]//*[contains(@class,'Button_variant_primary')] | (//*[contains(@class, 'Button') and contains(@class, 'primary')])[last()]"));
    private static final Locator TOTAL_TICKET = new Locator("TOTAL_TICKET", AppiumBy.xpath("//input[contains(@class,'quantity_input')] | //input[contains(@class,'QuantityEditor_quantity_input')]"));
    private static final Locator INCREMENT_BUTTON = new Locator("INCREMENT_BUTTON", AppiumBy.xpath("//button[contains(@class,'operation') and ./*[contains(@class,'plus')]]"));
    private static final Locator TIME_SLOTS = new Locator("TIME_SLOTS", AppiumBy.xpath("//*[contains(@class,'TimeSlot_time_wrapper')]/button/child::*"));
    private static final Locator SELECTED_PACKAGE_TITLE_PAGE = new Locator("SELECTED_PACKAGE_TITLE_PAGE", AppiumBy.xpath("//*[contains(@class,'BottomSheet_body_title_wrapper')]//h2 | //*[contains(@class,'PriceTierCard_card_pricetier_body')]"));
    private static final Locator SELECTED_PACKAGE_CARD_VIEW = new Locator("SELECTED_PACKAGE_CARD_VIEW", AppiumBy.xpath("//*[contains(@class,'BottomSheetNonEventOrder_wrapper_body')] | //*[contains(@class,'Collapse_collapse_with_transition')]/*[contains(@class,'PriceTierCard_card_pricetier')]"));
    private static final Locator VALID_DAY_TEXT = new Locator("VALID_DAY_TEXT", AppiumBy.xpath("//*[contains(@class,'PackageBenefit_feature_with_icon') or contains(@class,'PackageBenefit_feature_with_dots')]/span[contains(text(),'Berlaku') or contains(text(),'Valid')]"));
    private static final Locator CURRENT_CARD_INDEX = new Locator("CURRENT_CARD_INDEX", AppiumBy.xpath("//*[contains(@class,'PackageSelection_heading')]/span"));
    private static final Locator VALIDITY_PERIOD = new Locator("VALIDITY_PERIOD", AppiumBy.xpath("//div[contains(@class,'BottomSheetNonEventOrder_wrapper_card')]//span[contains(text(),'period') or contains(text(),'berlaku')]|(//*[contains(@class,'PriceTierCard_card_pricetier')])[1]//*[contains(text(),'Validity') or contains(text(),'Berlaku')]//following-sibling::*"));
    private static final Locator OPEN_PRICE_DETAIL = new Locator("OPEN_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'TotalPayment_wrapper')]//button[contains(@style,'price-value')]"));
    private static final Locator TOTAL_PRICE_ON_PRICE_DETAIL = new Locator("TOTAL_PRICE_ON_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'TotalPayment_detail_total')]/span[last()]"));
    private static final Locator TOTAL_PRICE_NEW = new Locator("TOTAL_PRICE_NEW", AppiumBy.xpath("(//*[contains(@class,'PriceModule_final_price_wrapper')]//*)[last()]"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", AppiumBy.xpath("//*[contains(@class,'totalpayment_wrapper')]//*[contains(@class,'final_price')]"));
    private static final Locator CLOSE_PRICE_DETAIL = new Locator("CLOSE_PRICE_DETAIL", AppiumBy.xpath("//button[contains(@class,'close_button')]"));
    private static final Locator CLOSE_CALENDAR = new Locator("CLOSE_CALENDAR", AppiumBy.xpath("(//button[contains(@class,'close_button')])[2]"));
    private static final Locator SEE_CALENDAR = new Locator("LAST_WEEK_CALENDAR", AppiumBy.xpath("//*[contains(@class,'calendar_heading')]/*[contains(@class,'btn_see_all')]"));
    private static final Locator SELECTED_DATE = new Locator("SELECTED_DATE", AppiumBy.xpath("//*[contains(@class,'DateFilter_date_card_active')]/*[2]"));
    private static final Locator TOMORROW_DATE = new Locator("TOMORROW_DATE", AppiumBy.xpath("//*[contains(@class,'DateFilter_date_card')]//*[text()='Besok' or text()='Tomorrow']//following-sibling::*"));
    private static final Locator PACKAGE_PRICE_TIER_PRICE_TEXT = new Locator("PACKAGE_PRICE_TIER_PRICE_TEXT", AppiumBy.xpath("//*[contains(@class,'TicketQuantity_ticket_price')]//span[contains(@style,'line-through')] | //*[contains(@class,'TicketQuantity_ticket_price')]//span[contains(@class,'Text_variant_alert')]"));
    private static final Locator TODAY_PRICE_CALENDAR_TEXT = new Locator("TODAY_PRICE_CALENDAR_TEXT", AppiumBy.xpath("//*[contains(@class,'DayContainer_day_container') and .//*[contains(@class,'Day_day_selected')]]//small[last()]"));
    private static final Locator LAST_DATE = new Locator("LAST_DATE", AppiumBy.xpath("(//*[contains(@class,'DateFilter_date_card') and not(contains(@class,'list'))])[last()]"));
    private static final Locator TIKET_POINT_TOTAL_PAYMENT = new Locator("TIKET_POINT_TOTAL_PAYMENT", AppiumBy.xpath("//*[contains(@class,'TotalPayment_loyalty_point')]"));
    private static final Locator CROSS_PRICE_ELEMENTS = new Locator("CROSS_PRICE_ELEMENTS", AppiumBy.xpath("(//*[contains(@class,'TicketQuantity_ticket_price')])/span[contains(@style,'line-through')]"));
    private static final Locator PRICE_ELEMENTS = new Locator("PRICE_ELEMENTS", AppiumBy.xpath("(//*[contains(@class,'TicketQuantity_ticket_price')])/span[contains(@class,'Text_weight_bold__')]"));
    private static final Locator TICKET_TYPE_ELEMENTS = new Locator("TICKET_TYPE_ELEMENTS", AppiumBy.xpath("//*[contains(@class,'TicketQuantity_ticket_name')]/span[1]"));
    private static final Locator PAX_COUNT = new Locator("PAX_COUNT", AppiumBy.xpath("//input[contains(@class,'QuantityEditor_quantity_input')]"));

    public void chooseDate() {
        List<WebElement> dateElements = driverTools.elements(AVAILABLE_DATE_TEXT).orElseThrow();
        if(!(dateElements.isEmpty())) {
            driverTools.clickWithJavascriptExecutor(dateElements.get(generateRandomInt(0, dateElements.size() - 1)));
        } else {
            driverTools.click(CLOSE_CALENDAR);
        }
    }

    public Locator selectedDate(int date) {
        return new Locator("DATE_CONTAINER", AppiumBy.xpath("//*[contains(@class,'day_container')]/button[.//*[contains(text(),'"+ date +"')] and not(@disabled)]"));
    }

    private int getRandomDate() {

        List<WebElement> availableDateElements = driverTools.elements(AVAILABLE_DATE_TEXT).orElseThrow();
        logger.log("--SIZE OF DATES AVAIALABLE---- "+availableDateElements.size() );
        List<String> availableDateText = new ArrayList<>();

        for(WebElement availableDateElement : availableDateElements) {
            availableDateText.add(driverTools.getText(availableDateElement));
        }


        return availableDateText.isEmpty() ? 0 : Integer.parseInt(availableDateText.get(generateRandomInt(0,availableDateText.size()-1)));
    }

    public void setTicketQty(int qty){

        final Locator FIRST_TIME_SLOT = new Locator("FIRST_TIME_SLOT", AppiumBy.xpath("(//*[contains(@class, 'TimeSlot_time_wrapper')]/button)[1]"));

        driverTools.swipeToElement(BOOK_BUTTON, Swipe.UP, 3);
        driverTools.tryClickWhenPresent(FIRST_TIME_SLOT, 1);
        List<WebElement> ticketQtyElements = driverTools.elements(TOTAL_TICKET).orElseThrow();
        int totalTicketCurrently = 0;

        for (WebElement e: ticketQtyElements) {
            totalTicketCurrently += Integer.parseInt(driverTools.getValue(e));
        }

        int needAdjust = qty - totalTicketCurrently;
        increaseTicketQty(needAdjust);

    }

    private void increaseTicketQty(int countToIncrease) {

        driverTools.swipeToElement(INCREMENT_BUTTON, Swipe.UP, 3).orElseThrow();
        List<WebElement> webElementList = driverTools.elements(INCREMENT_BUTTON).orElseThrow();

        // Always use the base (first) price tier. The lower tiers - "Group of 2-3", "Group of 4+" -
        // carry their own "Min. N tickets" rule, so picking one at random produced a quantity the
        // package will not accept, Book silently did nothing and the booking form never opened.
        // Callers that need a specific tier have setTicketQty(qty, ticketIndex).
        logger.log("Increasing ticket qty by " + countToIncrease + " on the first price tier of " + webElementList.size());

        for(int i = 0; i < countToIncrease; i++) {
            driverTools.clickWithJavascriptExecutor(webElementList.get(0));
        }

    }

    public void setTicketQty(int qty,int ticketIndex){
        if (driverTools.isElementDisplayed(TIME_SLOTS)) chooseTimeSlot();

        driverTools.waitForElementsToAppear(TOTAL_TICKET);
        driverTools.scrollIntoWebView(TOTAL_TICKET);
        WebElement totalTicketElement = driverTools.swipeToElement(TOTAL_TICKET, Swipe.UP, 5).orElseThrow();

        int totalTicket = Integer.parseInt(driverTools.getValue(totalTicketElement));
        IntStream intStream = IntStream.iterate(totalTicket, index -> qty > totalTicket, index -> index + 1);

        driverTools.waitForElementToBeClickable(INCREMENT_BUTTON).orElseThrow();
        List<WebElement> webElementList = driverTools.elements(INCREMENT_BUTTON).orElseThrow();

        WebElement webElement = driverTools.waitForElementToBeClickable(webElementList.get(ticketIndex)).orElseThrow();
        intStream.limit(qty).forEach(index -> driverTools.clickWithJavascriptExecutor(webElement));

    }

    public void clickBookButton(){
        driverTools.waitForElementToBeClickable(BOOK_BUTTON).orElseThrow(
                () -> new IllegalStateException("Book button never became clickable on the price tier sheet"));
        driverTools.clickWithJavascriptExecutor(BOOK_BUTTON);

        // A JS click on a Book button the package has rejected (quantity below the tier minimum, no
        // time slot picked) returns silently and the price tier sheet stays open. Without this the
        // run carried on and died 20 seconds later in TtdBookingFormPageWeb with a bare
        // "No value present", pointing at the wrong page.
        driverTools.waitUntil(d -> !driverTools.getCurrentURL().contains("pricetierDetail"), 10)
                .orElseThrow(() -> new IllegalStateException(
                        "Book did not open the booking form - the price tier sheet is still showing. "
                                + "Check the selected ticket quantity against the tier's minimum."));
    }

    public String chooseTomorrowDate() {
        String tomorrowDate;
        if (driverTools.waitForElementToPresence(TOMORROW_DATE).isPresent()) {
            driverTools.click(TOMORROW_DATE);
            tomorrowDate = driverTools.getText(TOMORROW_DATE).orElseThrow();
        } else {
            logger.log("Tomorrow date is not available, choose the default selected date.");
            tomorrowDate = getSelectedDate();
        }
        return tomorrowDate;
    }

    public String chooseTimeSlot(){

        WebElement element = driverTools.waitForElementToAppear(timeSlotLocator(1)).orElseThrow();
        driverTools.scrollIntoWebView(element);

        List<WebElement> timeSlots = driverTools.elements(TIME_SLOTS).orElseThrow();
        int randomTimeSlotIndex = generateRandomInt(1, timeSlots.size());

        WebElement timeSlot = driverTools.waitForElementToAppear(timeSlotLocator(randomTimeSlotIndex)).orElseThrow();
        if(!(timeSlot.getAttribute("class").contains("_variant_activity"))) {
            driverTools.clickWithJavascriptExecutor(timeSlot);
        }

        String timeSlotText = driverTools.getText(timeSlot);
        logger.log("time slot selected : " + timeSlotText);


        return timeSlotText;
    }

    private Locator timeSlotLocator(int index) {
        return new Locator("TIME_SLOT", AppiumBy.xpath("(//*[contains(@class,'TimeSlot_time_wrapper')]/button/child::*)["+ index +"]"));
    }

    public boolean selectedPackagePageShown(){

        WebElement titlePage = driverTools.waitForElementToPresence(SELECTED_PACKAGE_TITLE_PAGE).orElseThrow();
        WebElement cardView = driverTools.waitForElementToPresence(SELECTED_PACKAGE_CARD_VIEW).orElseThrow();
        boolean titlePageDisplayed = titlePage.isDisplayed();
        boolean cardViewDisplayed = cardView.isDisplayed();


        return titlePageDisplayed && cardViewDisplayed;
    }

    public String getPackageName(int index){

        WebElement webElement = driverTools.waitForElementToPresence(packageName(index)).orElseThrow();
        String result = driverTools.getText(webElement);


        return result;
    }

    private Locator packageName(int index) {
        return new Locator("PACKAGE_NAME", AppiumBy.xpath("//*[contains(@class,'BottomSheetNonEventOrder_wrapper_card')]/span[2] | (//*[contains(@class,'PackageSelection_package_title')])["+index+"]"));
    }

    public String getCrossPrice(int index){

        String result = "";

        Optional<WebElement> crossPriceElement = driverTools.waitForElementToPresence(crossPrice(index));
        if (crossPriceElement.isPresent()) {
            result += driverTools.getText(crossPriceElement.orElseThrow());
        } else {
            result += "";
        }


        return result;
    }

    /**
     * Retrieves guest type and price details from the web view, storing them in a map. The keys are guest types and the values are lists of prices.
     * Throws an IllegalStateException if there is a mismatch in the number of price, ticket type, and guest count elements.
     * @return Map where keys are guest types and values are lists of prices associated with each guest type.
     */

    public Map<String, List<String>> getGuestTypePriceMap() {

        Map<String, List<String>> guestTypePriceMap = new HashMap<>();
        Optional<List<WebElement>> priceElements = driverTools.elements(PRICE_ELEMENTS);
        Optional<List<WebElement>> ticketTypeElements = driverTools.elements(TICKET_TYPE_ELEMENTS);
        Optional<List<WebElement>> crossPriceElements = driverTools.elements(CROSS_PRICE_ELEMENTS);
        Optional<List<WebElement>> guestCountElements = driverTools.elements(PAX_COUNT);

        if (priceElements.get().size() != ticketTypeElements.get().size() ||
                priceElements.get().size() != guestCountElements.get().size()) {
            throw new IllegalStateException("Mismatch in the number of price, ticket type, and guest count elements");
        }
        logger.log("Price elements size: " + priceElements.get().size());

        for (int i = 0; i < priceElements.get().size(); i++) {
            String guestCountText = driverTools.getValueAttribute(guestCountElements.get().get(i), "value");
            if (!guestCountText.isEmpty()) {
                int guestCount = Integer.parseInt(guestCountText);
                logger.log("Guest count: " + guestCount);

                if (guestCount > 0) {
                    WebElement priceElement = priceElements.get().get(i);
                    WebElement ticketTypeElement = ticketTypeElements.get().get(i);

                    String priceText;
                    if (driverTools.isElementDisplayed(crossPriceElements.get().get(i))) {
                        priceText = driverTools.getText(crossPriceElements.get().get(i));
                        logger.log("Cross price text: " + priceText);
                    } else {
                        if (driverTools.isElementDisplayed(PRICE_ELEMENTS) && !driverTools.isElementDisplayed(crossPrice(i))) {
                            priceText = String.valueOf(driverTools.getText(priceElement));
                            logger.log("Price text: " + priceText);
                        } else {
                            throw new IllegalStateException("Price not found for element index " + (i + 1));
                        }
                    }
                    logger.log("Price text: " + priceText);
                    String ticketTypeText = driverTools.getText(ticketTypeElement);
                    // Add the guest type and price to the map
                    guestTypePriceMap.computeIfAbsent(ticketTypeText, k -> new ArrayList<>());
                    for (int j = 0; j < guestCount; j++) {
                        guestTypePriceMap.get(ticketTypeText).add(priceText);
                    }
                }
            }
            if (!guestTypePriceMap.isEmpty()) {
                break;
            }
        }

        logger.log("Guest type price map: " + guestTypePriceMap);
        return guestTypePriceMap;
    }

    private Locator crossPrice(int index) {
        return new Locator("CROSS_PRICE", AppiumBy.xpath("(//*[contains(@class,'TicketQuantity_ticket_price')])["+index+"]//span[contains(@style,'line-through')]"));
    }

    public String getFinalPrice(int index){

        WebElement finalPriceElement = driverTools.waitForElementToPresence(finalPrice(index)).orElseThrow();
        String result = driverTools.getText(finalPriceElement);


        return result;
    }

    private Locator finalPrice(int index) {
        return new Locator("FINAL_PRICE", AppiumBy.xpath("(//*[contains(@class,'PackageSelection_package_item')])["+ index +"]//*[contains(@class,'PackageCard_main_price')]/span"));
    }

    public String getCampaignLabel(int index){

        String result = "";

        Optional<WebElement> campaignLabelElement = driverTools.waitForElementToPresence(campaignLabel(index));
        if (campaignLabelElement.isPresent()) {
            result += driverTools.getText(campaignLabelElement.orElseThrow());
        } else {
            result += "";
        }


        return result;
    }

    private Locator campaignLabel(int index) {
        return new Locator("FINAL_PRICE", AppiumBy.xpath("(//*[contains(@class,'PackageSelection_package_item')])["+ index +"]//*[contains(@class,'PackageCard_main_price')]/div[contains(@class,'label')]"));
    }

    public String getValidDayText(String packageName){

        String result = "";

        if(driverTools.waitForElementToAppear(CURRENT_CARD_INDEX,5).isPresent()) {
            String selectedCardIndex = driverTools.getText(CURRENT_CARD_INDEX).orElseThrow().split("/")[0];
            WebElement validDay = driverTools.waitForElementToPresence(validDay(selectedCardIndex)).orElseThrow();
            result += driverTools.getText(validDay);
        } else {
            result += driverTools.getText(validDay("1"));
        }


        return result;
    }

    private Locator validDay(String index) {
        return new Locator("VALID_DAY", AppiumBy.xpath("(//*[contains(@class,'PackageSelection_package_item')])["+ index +"]//*[contains(@class,'PackageBenefit_feature_with_icon') or contains(@class,'PackageBenefit_feature_with_dots')]/span[contains(text(),'Berlaku') or contains(text(),'Valid')]"));
    }

    public String getValidityPeriod(){

        String result = "";

        WebElement validityPeriod = driverTools.element(VALIDITY_PERIOD).orElseThrow();
        result += driverTools.getText(validityPeriod);
        result = result.replace("Masa berlaku: ","").replace("Validity Period: ","").trim();

        return result;
    }

    public Boolean isTermsAndConditionThere(String packageName){

        String selectedCardIndex = driverTools.getText(CURRENT_CARD_INDEX).orElseThrow().split("/")[0];
        Boolean result = driverTools.isElementDisplayed(termsAndCondition(selectedCardIndex));


        return result;
    }

    private Locator termsAndCondition(String index) {
        return new Locator("TERMS_AND_CONDITION", AppiumBy.xpath("(//*[contains(@class,'PackageSelection_package_item')])["+ index +"]//p[contains(text(),'Syarat Ketentuan') or contains(text(),'Terms and Conditions')]"));
    }

    public void fillAvailableSection(){

        if(driverTools.waitForElementToPresence(SEE_CALENDAR, 5).isPresent()) {
            driverTools.click(SEE_CALENDAR);
            chooseDate();
        } else if (driverTools.element(LAST_DATE).isPresent()) {
            driverTools.clickWithJavascriptExecutor(LAST_DATE);
        } else {
            logger.softWarning("Date section is not available");
        }

        if(driverTools.waitForElementToPresence(timeSlotLocator(1), 5).isPresent()) {
            chooseTimeSlot();
        } else {
            logger.softWarning("Time slot is not available");
        }
    }

    public String getTotalPrice() {
        String result = driverTools.getText(TOTAL_PRICE).orElseThrow();

        return result;
    }

    public String getSelectedDate() {
        driverTools.waitForElementToAppear(SELECTED_DATE);
        String result = driverTools.getText(SELECTED_DATE).orElseThrow();
        logger.log("Selected date -> " + result);

        return result;
    }

    public Boolean verifyDisplayedCurrency(String currency, String sectionType) {
        switch(sectionType.toLowerCase()) {
            case "price tier":
                return verifyDisplayedCurrencyPriceTier(currency);
            case "price calendar":
                return verifyDisplayedCurrencyPriceCalendar(currency);
            default:
                logger.fail("There is no option for sectionType '"+ sectionType +"'");
        }

        return false;
    }

    private Boolean verifyDisplayedCurrencyPriceCalendar(String currency) {

        List<String> priceTierPriceTexts = driverTools.getTexts(PACKAGE_PRICE_TIER_PRICE_TEXT);
        logger.log("PACKAGE_PRICE_TIER_PRICE_TEXT -> " + Arrays.toString(priceTierPriceTexts.toArray(new String[0])));


        openCalendar();

        driverTools.waitForElementToAppear(TODAY_PRICE_CALENDAR_TEXT);
        String todayPriceCalendarText = driverTools.getText(TODAY_PRICE_CALENDAR_TEXT).orElseThrow();

        closeCalendar();

        boolean result = priceTierPriceTexts.stream().anyMatch(priceText -> priceText.contains(todayPriceCalendarText));

        for(int i = 0; !result && i < priceTierPriceTexts.size(); i++) {
            double priceTierPrice = convertPrice(priceTierPriceTexts.get(i));
            double priceCalendar = convertPrice(todayPriceCalendarText);

            double diff = priceCalendar - priceTierPrice;

            if(diff < 1 && diff > -1) {
                return true;
            }
        }

        return result;
    }

    private boolean verifyDisplayedCurrencyPriceTier(String currency) {

        driverTools.waitForElementToAppear(PACKAGE_PRICE_TIER_PRICE_TEXT);
        List<String> priceTexts = driverTools.getTexts(PACKAGE_PRICE_TIER_PRICE_TEXT);
        logger.log("PACKAGE_PRICE_TIER_PRICE_TEXT -> " + Arrays.toString(priceTexts.toArray(new String[0])));


        if (priceTexts.isEmpty()) {
            logger.softWarning("Price tier price text is empty");
            return false;
        }

        return CoreFunctions.checkCurrencyOnStringList(currency, priceTexts);
    }

    public void openCalendar() {

        driverTools.click(SEE_CALENDAR);

    }

    public void closeCalendar() {

        driverTools.click(CLOSE_CALENDAR);

    }

    public boolean isTiketPointDisplayed() {
        boolean result = driverTools.isElementDisplayed(TIKET_POINT_TOTAL_PAYMENT);
        return result;
    }
}
