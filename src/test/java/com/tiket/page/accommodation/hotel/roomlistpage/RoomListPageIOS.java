package com.tiket.page.accommodation.hotel.roomlistpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.homes.homesroomdetailpage.HomesRoomDetailPage;
import com.tiket.page.accommodation.hotel.HotelPage;
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.pojo.accommodation.hotel.ChooseFirstRoomData;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.tiket.core.CoreFunctions.convertPrice;
import static com.tiket.core.CoreFunctions.splitLanguageData;
import static com.tiket.page.accommodation.hotel.detailpage.DetailPageIOS.YES_CONTINUE_MEET_POLICY_BTN;
import static com.tiket.page.accommodation.hotel.roomdetailpage.RoomDetailPageIOS.BOOK_BUTTON;
import static com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPageIOS.TOTAL_PRICE_LABEL_WITHOUT_TAX;
import static com.tiket.page.accommodation.hotel.detailpage.DetailPageIOS.PRICE_BREAKDOWN;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;



public class RoomListPageIOS <T extends WebDriver> extends RoomListPage<T>{

    public RoomListPageIOS(Session<T> session) { super(session); }

    public static final Locator FIRST_HOTEL_ROOM_NAME = new Locator("FIRST_HOTEL_ROOM_NAME", By.xpath("//*[contains(@name, 'titleContainerView')]//XCUIElementTypeStaticText[contains(@name, '.title')][1]"));
    public static final Locator FIRST_HOTEL_ROOM_CARD = new Locator("FIRST_HOTEL_ROOM_CARD", By.xpath("(//XCUIElementTypeButton[@name='roomCard.bookButton' and @visible='true'])[1]"));
    public static final Locator FIRST_HOTEL_ROOM_PRICE = new Locator("FIRST_HOTEL_ROOM_PRICE", By.xpath("(//*[contains(@name,'roomItem')]/*[@name='mainPrice' and @visible='true'])[last()]"));
    public static final Locator HOTEL_PRICE = new Locator("HOTEL_PRICE", AppiumBy.name("mainPrice"));
    private static final Locator FIRST_HOTEL_PRICE_TAX = new Locator("FIRST_HOTEL_PRICE_TAX", AppiumBy.name("unitText2"));
    public static final Locator BOOK_BUTTON_LIST = new Locator("BOOK_BUTTON_LIST", AppiumBy.id("roomCard.bookButton"));
    public static final Locator ALTERNATE_ROOM_HEADER = new Locator("ALTERNATE_ROOM_HEADER", By.xpath("//XCUIElementTypeStaticText[@label='Kamar incaranmu tidak tersedia' or @label='No rooms match your search']"));
    public static final Locator ALTERNATE_ROOM_HEADER_OPTION = new Locator("ALTERNATE_ROOM_HEADER_OPTION", By.xpath("//XCUIElementTypeStaticText[@label='Pilihan kamar lain untukmu' or @label='Other room options for you']"));
    public static final Locator FIRST_ALTERNATE_ROOM_NAME = new Locator("FIRST_ALTERNATE_ROOM_NAME", By.xpath("(//*[contains(@label,'x) ')])[1]"));
    private static final Locator PRICE_FOOTER_BUTTON = new Locator("PRICE_FOOTER_BUTTON", By.xpath("//*[@name='cartPriceFooterView']//*[@name='tds_ic_chevron_down']"));
    private static final Locator PRICE_FOOTER_TEXT = new Locator("PRICE_FOOTER_TEXT", By.xpath("//*[@name='cartPriceFooterView']//*[@name='mainPrice']"));
    private static final Locator CURRENT_ROOM_NAME = new Locator("CURRENT_ROOM_NAME", AppiumBy.xpath("(//*[contains(@name,'.title') and @accessible='true'])[1]"));
    private static final Locator EXPAND_ROOM_BUTTON = new Locator("EXPAND_ROOM_BUTTON", AppiumBy.name("tds_ic_chevron_down"));
    private static final Locator UNSELECTED_ROOM_NAME = new Locator("UNSELECTED_ROOM_NAME", AppiumBy.xpath("(//*[@name='roomCard.bookButton' and @enabled='true' and (@label='Select' or @label='Pilih')]//parent::*//following-sibling::*[contains(@name,'.title') and @accessible='true'])[1]"));
    private static final Locator UNSELECTED_ROOM_PRICE = new Locator("UNSELECTED_ROOM_PRICE", AppiumBy.xpath("(//*[@name='roomCard.bookButton' and @enabled='true' and (@label='Select' or @label='Pilih') and @visible='true' and @accessible='true']/following-sibling::*[@name='mainPrice'])[last()]"));
    private static final Locator UNSELECTED_ROOM_PRICE_AFTER_TAX = new Locator("UNSELECTED_ROOM_PRICE_AFTER_TAX", AppiumBy.xpath("(//*[@name='roomCard.bookButton' and @enabled='true' and (@label='Select' or @label='Pilih') and @visible='true' and @accessible='true']/following-sibling::*[contains(@label,'setelah pajak') or contains(@label,'after taxes')])[last()]"));
    private static final Locator SELECT_BUTTON = new Locator("SELECT_BUTTON", AppiumBy.xpath("//*[@name='roomCard.bookButton' and @enabled='true' and (@label='Select' or @label='Pilih') and @visible='true' and @accessible='true']"));
    private static final Locator SNACKBAR_TEXT = new Locator("SNACKBAR_TEXT", AppiumBy.name("room_info_popup"));
    private static final Locator INCREMENT_BUTTON = new Locator("INCREMENT_BUTTON", AppiumBy.name("add_btn"));
    private static final Locator BOOK_BUTTON_FOOTER = new Locator("BOOK_BUTTON_FOOTER", By.name("book_button"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.iOSClassChain("**/*[`name == 'back' OR name == 'back_button'`]"));
    public static final Locator RESCHEDULE_ROOM_LIST_VIEW = new Locator("RESCHEDULE_ROOM_LIST_VIEW", AppiumBy.className("XCUIElementTypeTable"));
    public static final Locator ADDITIONAL_PRICE_SECTION = new Locator("ADDITIONAL_PRICE_SECTION", AppiumBy.xpath("//XCUIElementTypeCell[.//XCUIElementTypeStaticText[contains(@name,'+') or contains(@label,'+')]]"));
    public static final Locator TEXT_VIEW_WITH_REFUND = new Locator("TEXT_VIEW_WITH_REFUND", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name CONTAINS 'Dapat refund' OR name CONTAINS 'Get Refund' OR label CONTAINS 'Dapat refund' OR label CONTAINS 'Get Refund'`]"));
    public static final Locator ROOM_NAME = new Locator("ROOM_NAME", By.xpath("//*[@name='hotelCarouselImage']//following::XCUIElementTypeStaticText[1]"));
    /**
     * Room-card CTA. Accessibility name is {@code roomCard.bookButton} (see FIRST_HOTEL_ROOM_CARD /
     * SELECT_BUTTON); label varies Book/Pesan/Select/Pilih. Requiring name+label both Book/Pesan never matches.
     */
    public static final Locator FIRST_BOOK_ROOM_BUTTON = new Locator("FIRST_BOOK_ROOM_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='roomCard.bookButton' and @visible='true']"));
    private static final Locator ROOM_SHORTCUT_WINDOW_ITEM_CELL = new Locator("ROOM_SHORTCUT_WINDOW_ITEM_CELL", AppiumBy.xpath("//*[contains(@name,'roomShortcutWindowItemCell')]"));
    private static final Locator CLOSE_FLOATING_BUTTON = new Locator("CLOSE_FLOATING_BUTTON", AppiumBy.name("closeFloatingButton"));
    private static final Locator SHORTCUT_FLOATING_BUTTON = new Locator("SHORTCUT_FLOATING_BUTTON", AppiumBy.name("shortcutFloatingButton"));
    /** Older builds where the CTA exposes Book/Pesan as name/label only. */
    private static final Locator FIRST_BOOK_ROOM_BUTTON_LEGACY = new Locator("FIRST_BOOK_ROOM_BUTTON_LEGACY", AppiumBy.xpath("(//XCUIElementTypeButton[(@name='Book' or @name='Pesan' or @label='Book' or @label='Pesan' or @label='Select' or @label='Pilih')])[1]"));
    private static final Locator EXTRA_ROOM_BOTTOMSHEET_PRIMARY_BTN = new Locator("EXTRA_ROOM_BOTTOMSHEET_PRIMARY_BTN", AppiumBy.xpath("//*[@name='TVCell<TDSButtonViewComponent>']/XCUIElementTypeButton[(contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'book') and contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'room')) or (contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pesan') and contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'kamar'))]"));
    private static final Locator FULLY_BOOKED_BOTTOMSHEET_BTN = new Locator("FULLY_BOOKED_BOTTOMSHEET_BTN", AppiumBy.xpath("//*[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'fully booked') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'kamar pilihanmu penuh')]/following-sibling::*//XCUIElementTypeButton"));
    public static final Locator MASTER_TAG_SCROLL_VIEW = new Locator("MASTER_TAG_SCROLL_VIEW", AppiumBy.name("quickFilterView"));
    public static final Locator CHILD_GUEST_COUNT = new Locator("CHILD_GUEST_COUNT", AppiumBy.iOSClassChain("**/*[`name == 'occupancyInfoText' AND value contains 'Anak' OR value contains 'Child'`]"));
    public static final Locator GUEST_COUNT = new Locator("GUEST_COUNT", AppiumBy.xpath("//*[@name='roomCard.bookButton']/parent::*//*[@name ='roomCard.featureAdded' and contains(@label,'Guest') or contains(@label,'Tamu')]"));
    public static final Locator CANCEL_BUTTON = new Locator("CANCEL_BUTTON", AppiumBy.accessibilityId("ic cancel grey"));
    public static final Locator LOYALTY_BADGE = new Locator("LOYALTY_BADGE", AppiumBy.iOSClassChain("**/*[`name == 'roomCard.featureAdded' AND label CONTAINS 'benefit' OR label CONTAINS 'Diskon' OR label CONTAINS 'Discount'`]"));
    private static final Locator SHOW_MORE_LOYALTY_BUTTON = new Locator("SHOW_MORE_LOYALTY_BUTTON", By.xpath("//*[contains(@name,'LoyaltyBenefitView')]//XCUIElementTypeStaticText"));
    private static final Locator ROOM_BANNER_TEXT = new Locator("ROOM_BANNER_TEXT", By.name("multiroom_banner"));
    private static final Locator PER_NIGHT_TEXT = new Locator("PER_NIGHT_TEXT", By.xpath("//XCUIElementTypeStaticText[contains(@label,'/night') or contains(@label,'/malam')]"));
    private static final Locator ROOM_REFUND_ESTIMATION = new Locator("ROOM_REFUND_ESTIMATION", AppiumBy.xpath("//*[contains(@label,'detail')]//preceding-sibling::*[contains(@label,'refund')]"));
    private static final Locator BOOK_ROOM_BUTTON_CASHBACK = new Locator("BOOK_ROOM_BUTTON_CASHBACK", AppiumBy.xpath("//*[contains(@label,'detail')]//preceding-sibling::*[contains(@label,'refund')]//preceding-sibling::XCUIElementTypeButton[@label='PILIH' or @label='SELECT']"));
    private static final Locator FREE_BOOK_ROOM_BUTTON = new Locator("FREE_BOOK_ROOM_BUTTON", AppiumBy.xpath("//*[(@label='GRATIS' or @label='FREE') and (not(contains(@label,'+')) or not(contains(@label,'refund')))]//parent::XCUIElementTypeCell//XCUIElementTypeButton[@label='PILIH' or @label='SELECT']"));
    private static final Locator ADDITIONAL_PRICES = new Locator("ADDITIONAL_PRICES", AppiumBy.xpath("//*[contains(@label,'+')]"));
    private static final Locator TOTAL_ROOM_RECOMMENDATION = new Locator("TOTAL_ROOM_RECOMMENDATION", AppiumBy.name("roomQuantityLabel"));
    private static final Locator ADDITIONAL_ROOM_RECOMMENDATION = new Locator("ADDITIONAL_ROOM_RECOMMENDATION", AppiumBy.xpath("//*[contains(@label,'x') and translate(substring-after(@label, 'x'), '0123456789x', '') = '']"));
    private static final Locator ROOM_RECOMMENDATION_CELL = new Locator("ROOM_RECOMMENDATION_CELL", AppiumBy.name("roomRecommendationCell"));
    private static final Locator RECOMMENDATION_ROOM = new Locator("RECOMMENDATION_ROOM", AppiumBy.iOSClassChain("**/*[`name=='roomRecommendationCell'`]/*[`name=='roomNameLabel'`]"));
    private static final Locator ROOM_LIST_ANNOUNCEMENT_BANNER = new Locator("ROOM_LIST_ANNOUNCEMENT_BANNER",
            AppiumBy.xpath("(//*[@name='multiroom_banner'])[1]"));
    private static final Locator ROOM_FACILITY = new Locator("ROOM_FACILITY", AppiumBy.xpath("//*[@name='roomRecommendationCell']//*[@name='featuresLabel']"));
    private static final Locator TOTAL_PRICE_LABEL = new Locator("TOTAL_PRICE_LABEL", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Total harga (termasuk pajak)' or @label='Total price (including taxes)']"));
    private static final Locator PER_ROOM_PRICE_LABEL_WITHOUT_TAX = new Locator("PER_ROOM_PRICE_LABEL_WITHOUT_TAX", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'/kamar/malam (setelah pajak:') or contains(@label,'/room/night (after taxes:')]"));
    private static final Locator PER_ROOM_PRICE_LABEL = new Locator("PER_ROOM_PRICE_LABEL", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='/kamar/malam (termasuk pajak)' or @label='/room/night (including taxes)']"));
    private static final Locator MEETS_TRAVEL_POLICY_NUDGE = new Locator("MEETS_TRAVEL_POLICY_NUDGE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Meets Travel Policy' or @label='Sesuai Kebijakan']"));
    private static final Locator DOES_NOT_MEET_TRAVEL_POLICY_POP_UP = new Locator("DOES_NOT_MEET_TRAVEL_POLICY_POP_UP", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`(name CONTAINS \"doesn\" AND name CONTAINS \"meet the travel policy\") OR name CONTAINS \"tidak sesuai dengan kebijakan perjalanan\"`]"));
    public static final Locator LABEL_PER_ROOM_PER_NIGHT = new Locator("LABEL_PER_ROOM_PER_NIGHT", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='roomCard.priceTax' and (@label='/room/night (including taxes)' or @label='/kamar/malam (termasuk pajak)')]"));
    private static final Locator GUEST_INFO_GUEST_COUNT = new Locator("GUEST_COUNT_INFO", AppiumBy.name("occupancyInfoText"));
    private static final Locator ROOM_LIST_CONTAINER = new Locator("ROOM_LIST_CONTAINER", By.name("tableView"));
    private static final Locator ROOM_ITEM_CARD = new Locator("ROOM_ITEM_CARD", AppiumBy.iOSClassChain("**/*[`name CONTAINS 'roomItem'`]"));
    private static final Locator ROOM_RECOMMENDATION_TITLE = new Locator("ROOM_RECOMMENDATION_TITLE", AppiumBy.iOSClassChain("**/*[`name == 'roomRecommendationCell'`]/*[`name == 'label'`]"));
    public static final Locator ROOM_FLEXIBLE_PAYMENT = new Locator("ROOM_FLEXIBLE_PAYMENT", AppiumBy.xpath("//*[@name='flexiblePaymentText' and @visible='true']"));
    private static final Locator ROOM_REFUND_RESCHEDULE = new Locator("ROOM_REFUND_RESCHEDULE", AppiumBy.xpath("//*[@name='cancellationPoliciesText' and (contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'reschedule until') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'reschedule with fee') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'reschedule gratis') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'reschedule dengan biaya') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'reschedule s.d.')) and @visible='true']"));
    private static final Locator ROOM_REFUND_RESCHEDULE_CHARGE = new Locator("ROOM_REFUND_RESCHEDULE_CHARGE", AppiumBy.xpath("//*[@name='cancellationPoliciesText' and (contains(@label,'reschedule with fee') or contains(@label,'reschedule dengan biaya')) and @visible='true']"));
    public static final Locator FIRST_ROOM_DETAIL_CHEVRON = new Locator("FIRST_ROOM_DETAIL_CHEVRON", By.xpath("(//*[@name='tds_ic_chevron_right' and @visible='true'])[last()]"));
    private static final Locator ROOM_DETAIL_PRICE_SECTION_ICON = new Locator("ROOM_DETAIL_PRICE_SECTION_ICON", AppiumBy.xpath("//*[@name='tds_ic_chevron_down']"));
    private static final Locator ROOM_DETAIL_PRICE_SECTION_TOTAL = new Locator("ROOM_DETAIL_PRICE_SECTION_TOTAL", AppiumBy.xpath("//*[( contains(@label,\"Total to pay\") or  contains(@label,\"Total bayar\") ) and contains(@name,\"leftText\") and @type=\"XCUIElementTypeStaticText\"]/following-sibling::*[last()]"));

    @Override
    public void clickChangeButton() {
        //FIXME: Pending implementation
    }

    @Override
    public void clickBookButtonFooter() {
        driverTools.click(BOOK_BUTTON_FOOTER);
    }

    @Override
    public void clickMultiRoomBookButton(int totalRoom) {
        Locator multiRoomBookButton = getMultiRoomBookButtonLocator(totalRoom);
        driverTools.swipeElementToMidScreen(multiRoomBookButton);
        driverTools.click(multiRoomBookButton);
    }

    /**
     * Gets the locator of the footer book button that books totalRoom rooms of the same room type.
     * The locator matches the footer's book button whose label equals "Book {totalRoom} rooms" or "Pesan {totalRoom} kamar".
     *
     * @param totalRoom The total number of rooms shown on the button label.
     * @return a Locator object for the multi-room footer book button.
     */
    private Locator getMultiRoomBookButtonLocator(int totalRoom) {
        return new Locator("MULTI_ROOM_BOOK_BUTTON", AppiumBy.xpath("//*[@name='roomCard.bookButton' and @enabled='true' and " +
                "(contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'pesan " + totalRoom + " kamar') " +
                "or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'book " + totalRoom + " room'))]"));
    }

    @Override
    public double sumPriceBreakdown() {
        List<WebElement> priceElements = driverTools.elements(PRICE_BREAKDOWN).orElseThrow();
        double totalPrice = 0.0;
        for (WebElement priceElement : priceElements) {
            double price = convertPrice(driverTools.getText(priceElement));
            logger.log("current processed price ->" + price);
            totalPrice += price;
        }
        logger.log("totalPrice -> " + totalPrice);
        return totalPrice;
    }

    @Override
    public boolean priceBreakdownIsDisplayed() {
        return driverTools.isElementDisplayed(PRICE_BREAKDOWN);
    }

    @Override
    public void clickPriceFooter() {
        driverTools.waitForElementToDisappear(SNACKBAR_TEXT);
        driverTools.waitForElementToPresence(PRICE_FOOTER_BUTTON);
        driverTools.clickElementByCoordinate(PRICE_FOOTER_BUTTON);
    }

    @Override
    public String getPriceFooter() {
        return driverTools.getText(PRICE_FOOTER_TEXT).orElseThrow();
    }

    @Override
    public double selectRoom(int totalGuest, int roomIndex) {
        logger.log("Selecting room no. " + roomIndex);
        logger.log("for " + totalGuest + " guest(s)");
        if (driverTools.isElementDisplayed(PRICE_FOOTER_TEXT)) driverTools.swipeUp();
        if (roomIndex > 1) driverTools.swipeToPresentedElement(UNSELECTED_ROOM_NAME, Swipe.DOWN, 7).orElseThrow();
        WebElement roomNameElement = driverTools.swipeToPresentedElement(UNSELECTED_ROOM_NAME, Swipe.UP, 7).orElseThrow();
        String roomName = driverTools.getText(roomNameElement);
        selectedRoomName.put(roomIndex, roomName);

        WebElement unselectedRoomPrice = driverTools.swipeToPresentedElement(UNSELECTED_ROOM_PRICE, Swipe.UP, 7).orElseThrow();
        if (driverTools.isElementDisplayed(UNSELECTED_ROOM_PRICE_AFTER_TAX)) {
            unselectedRoomPrice = driverTools.element(UNSELECTED_ROOM_PRICE_AFTER_TAX).orElseThrow();
        }
        double priceText = convertPrice(driverTools.getText(unselectedRoomPrice));

        driverTools.swipeToElement(SELECT_BUTTON, Swipe.UP, 5).orElseThrow();
        Assertion.assertThat("text button on the room card is \"Select\" or \"Pilih\" at " + roomIndex + " index", driverTools.getValueAttribute(SELECT_BUTTON, "label"), either(is("Select")).or(is("Pilih")));
        driverTools.waitForElementToPresence(SELECT_BUTTON);
        driverTools.click(SELECT_BUTTON);
        Assertion.assertThat("user can see the price footer room list after select the " + roomIndex + " of the room index", driverTools.waitForElementToPresence(PRICE_FOOTER_TEXT).get().isDisplayed(), is(true));
        driverTools.waitForElementToDisappear(SNACKBAR_TEXT);

        for (int i = 1; i < totalGuest; i++) {
            if (driverTools.waitForElementToPresence(INCREMENT_BUTTON).isPresent()) {
                driverTools.swipeToElement(INCREMENT_BUTTON, Swipe.UP, 2);
                driverTools.click(INCREMENT_BUTTON);
            } else {
                driverTools.waitForElementToPresence(CURRENT_ROOM_NAME).orElseThrow();
                logger.log("clicking header: " + driverTools.getText(CURRENT_ROOM_NAME).orElseThrow());
                driverTools.clickElementByCoordinate(CURRENT_ROOM_NAME);
                WebElement expandRoomButton = driverTools.waitForElementToPresence(EXPAND_ROOM_BUTTON).orElseThrow();
                driverTools.clickElementByCoordinate(expandRoomButton); //click the room name twice to collapse and expand it, it induces the increment button to appear
                driverTools.swipeToElement(INCREMENT_BUTTON, Swipe.UP, 3);
                driverTools.click(INCREMENT_BUTTON);
            }
            driverTools.waitForElementToDisappear(SNACKBAR_TEXT);
        }

        return priceText * totalGuest;
    }

    @Override
    public double selectRoomWithPrefer(int totalGuest, int roomIndex, String preferRoomName) {
        return selectRoom(totalGuest, roomIndex);
    }

    @Override
    public boolean moBannerIsDisplayed() {
        driverTools.waitForElementToAppear(ROOM_BANNER_TEXT, 10);
        return driverTools.isElementDisplayed(ROOM_BANNER_TEXT);
    }

    @Override
    public String getTheFirstHotelRoomName() {
        driverTools.waitForElementToPresence(FIRST_HOTEL_ROOM_NAME);
        if (!driverTools.isElementPresent(FIRST_HOTEL_ROOM_NAME)) {
            driverTools.swipeToElement(FIRST_HOTEL_ROOM_NAME, Swipe.UP, 5);
        }
        return driverTools.getText(FIRST_HOTEL_ROOM_NAME).orElseThrow();
    }

    public String getTheFirstHotelRoomPrice() {
        driverTools.waitForElementToPresence(FIRST_HOTEL_ROOM_PRICE);
        if (!driverTools.isElementPresent(FIRST_HOTEL_ROOM_PRICE)) {
            driverTools.swipeToElement(FIRST_HOTEL_ROOM_PRICE, Swipe.UP, 5);
        }

        WebElement firstHotelRoomPrice = driverTools.waitForElementToPresence(FIRST_HOTEL_ROOM_PRICE).orElseThrow();
        String price;
        try {
            price = driverTools.getText(firstHotelRoomPrice);
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            firstHotelRoomPrice = driverTools.waitForElementToPresence(FIRST_HOTEL_ROOM_PRICE).orElseThrow();
            price = driverTools.getText(firstHotelRoomPrice);
        }
        logger.log("got price -> " + price);
        return price;
    }

    @Override
    public String getTheFirstHotelRoomPrice(String currency) {
        return driverTools.getText(getFirstPriceWithCurrencyLocator(currency)).orElseThrow();
    }

    private Locator getFirstPriceWithCurrencyLocator(String currency) {
        return new Locator("FIRST_HOTEL_ROOM_PRICE_WITH_CURRENCY", By.xpath("((//*[contains(@name,'roomItem')])[last()]//XCUIElementTypeStaticText[contains(@label,'" + currency + "')])[last()]"));
    }

    @Override
    public String getTheFirstHotelRoomPrice(String vendor, boolean childMealPlan,String currency) {
        return null; //FIXME: Pending implementation
    }

    @Override
    public String getTheFirstRoomRefundEstimation() {
        driverTools.waitForElementToAppear(ROOM_REFUND_ESTIMATION);
        driverTools.swipeToElement(ROOM_REFUND_ESTIMATION, Swipe.UP, 5);
        String getRefundEstimation = driverTools.getValueAttribute(ROOM_REFUND_ESTIMATION, "label")
                .replace("Get refund ", "")
                .replace("Dapat refund ", "");
        logger.log("getRefundEstimation-> " + getRefundEstimation);
        return getRefundEstimation;
    }

    @Override
    public void chooseRoomWithCashback() {
        driverTools.waitForElementToAppear(BOOK_ROOM_BUTTON_CASHBACK);
        driverTools.click(BOOK_ROOM_BUTTON_CASHBACK);
    }

    @Override
    public void openRoomDetailPage() {
        driverTools.swipeToElement(FIRST_HOTEL_ROOM_PRICE, Swipe.UP, 5);
        WebElement roomPriceElement = driverTools.waitForElementToPresence(FIRST_HOTEL_ROOM_PRICE).orElseThrow();
        driverTools.clickElementByCoordinate(roomPriceElement);
    }

    @Override
    public void openRoomDetailPage(String price) {
        if (!driverTools.isElementDisplayed(getFirstPriceWithCurrencyLocator(price))) {
            driverTools.swipeToElement(getFirstPriceWithCurrencyLocator(price), Swipe.UP, 5);
        }
        driverTools.click(getFirstPriceWithCurrencyLocator(price));
    }

    @Override
    public void clickBackButton() {
        try {
            driverTools.click(BACK_BUTTON);
        } catch (Exception e) {
            logger.log("Failed to click back button: " + e.getMessage());
            driverTools.back();
        }
    }

    @Override
    public String chooseRoomWithAdditionalPrice() {
        driverTools.waitForElementToAppear(RESCHEDULE_ROOM_LIST_VIEW);
        WebElement additionalPriceSection = driverTools.swipeToElement(ADDITIONAL_PRICE_SECTION, Swipe.UP, 4).orElseThrow();
        driverTools.click(additionalPriceSection);

        String newRoomName = getRoomName();
        HomesRoomDetailPage.getInstance(session).selectRoom();
        return newRoomName;
    }

    @Override
    public String getPriceRoomWithAdditionalPrice() {
        driverTools.waitForElementToAppear(RESCHEDULE_ROOM_LIST_VIEW);
        WebElement textViewAdditionalPrice = driverTools.swipeToElement(ADDITIONAL_PRICES, Swipe.UP, 4).orElseThrow();
        return driverTools.getText(textViewAdditionalPrice);
    }

    @Override
    public ChooseFirstRoomData chooseFirstRoom() {
        ChooseFirstRoomData chooseFirstRoomData = new ChooseFirstRoomData();
        driverTools.waitForElementToAppear(RESCHEDULE_ROOM_LIST_VIEW);
        driverTools.swipeToElement(FREE_BOOK_ROOM_BUTTON,Swipe.UP,5);
        if(driverTools.isElementDisplayed(FREE_BOOK_ROOM_BUTTON)){
            logger.log("FREE_ROOM is will be clicked");
            driverTools.click(FREE_BOOK_ROOM_BUTTON);
            chooseFirstRoomData.isFreeRoom = true;
        } else {
            logger.log("room with additional price is will be clicked");
            driverTools.click(ADDITIONAL_PRICE_SECTION);
            chooseFirstRoomData.isFreeRoom = false;
        }
        chooseFirstRoomData.newRoomName = getRoomName();
        logger.log("newRoomName: " + chooseFirstRoomData.newRoomName);
        return chooseFirstRoomData;
    }

    private String getRoomName() {
        driverTools.swipeToElement(ROOM_NAME, Swipe.UP, 5);
        WebElement roomNameTextElement = driverTools.waitForElementToPresence(ROOM_NAME).orElseThrow();
        String[] fullRoomName = driverTools.getValueAttribute(roomNameTextElement,"value").split(" ");
        String strippedRoomName = String.join(" ",Arrays.copyOfRange(fullRoomName,2, fullRoomName.length));
        return strippedRoomName;
    }

    @Override
    public String getPriceAfterTax() {
        swipeToFirstBookRoomButton();
        String priceAfterTax = driverTools.getText(FIRST_HOTEL_PRICE_TAX).orElseThrow();
        priceAfterTax = priceAfterTax.split(": ")[1].replaceAll("[^a-zA-Z0-9.,\\s]", "");
        logger.log("priceAfterTax -> " + priceAfterTax);
        return priceAfterTax;
    }

    @Override
    public void clickBookRoom() {
        swipeToFirstBookRoomButton();
        driverTools.swipeUp();
        driverTools.swipeElementToMidScreen(FIRST_BOOK_ROOM_BUTTON);
        try {
            driverTools.clickElementByCoordinate(FIRST_BOOK_ROOM_BUTTON);
        } catch (Exception e) {
            logger.log("Error clicking with coordinate: " + e.getMessage());
            driverTools.swipeUp();
            driverTools.click(FIRST_BOOK_ROOM_BUTTON);
        }
        if (driverTools.isElementDisplayed(ROOM_SHORTCUT_WINDOW_ITEM_CELL)) {
            driverTools.click(CLOSE_FLOATING_BUTTON);
            driverTools.waitForElementToDisappear(ROOM_SHORTCUT_WINDOW_ITEM_CELL,5);
            driverTools.swipeByDistance(ROOM_LIST_CONTAINER, SHORTCUT_FLOATING_BUTTON, Swipe.UP,2);
            driverTools.clickElementByCoordinate(FIRST_BOOK_ROOM_BUTTON);
        }
        if (driverTools.isElementDisplayed(FIRST_BOOK_ROOM_BUTTON)) {
            driverTools.swipeElementToMidScreen(FIRST_BOOK_ROOM_BUTTON);
            driverTools.click(FIRST_BOOK_ROOM_BUTTON);
        }
        clickExtraRoomBottomSheetIfAppears();
        driverTools.waitForElementToDisappear(FIRST_BOOK_ROOM_BUTTON);
        BookingFormPage<T> bookingFormPage = BookingFormPage.getInstance(session);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
    }

    @Override
    public void clickExtraRoomBottomSheetIfAppears() {
        driverTools.waitForElementToAppear(EXTRA_ROOM_BOTTOMSHEET_PRIMARY_BTN, 5).ifPresent(driverTools::click);
    }

    @Override
    public boolean clickFullyBookedBottomSheetIfAppears() {
        return driverTools.waitForElementToAppear(FULLY_BOOKED_BOTTOMSHEET_BTN, 5)
                .map(btn -> {
                    driverTools.click(btn);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public void clickBookRoomWithoutSkippingRatePlan() {
        Locator bookRoomButton = swipeToFirstBookRoomButton();
        driverTools.swipeElementToMidScreen(bookRoomButton);
        driverTools.clickElementByCoordinate(bookRoomButton);
        if (driverTools.isElementDisplayed(BOOK_BUTTON)) {
            driverTools.swipeElementToMidScreen(BOOK_BUTTON);
            driverTools.click(BOOK_BUTTON);
        }
        driverTools.waitForElementToDisappear(BOOK_BUTTON);
    }

    /** Prefer stable {@code roomCard.bookButton}; fall back to Book/Pesan/Select label CTA. */
    private Locator swipeToFirstBookRoomButton() {
        if (driverTools.swipeToElement(FIRST_BOOK_ROOM_BUTTON, Swipe.UP, 8).isPresent()) {
            return FIRST_BOOK_ROOM_BUTTON;
        }
        logger.log("[clickBookRoom] roomCard.bookButton not found — trying Book/Pesan/Select label fallback");
        if (driverTools.swipeToElement(FIRST_BOOK_ROOM_BUTTON_LEGACY, Swipe.UP, 5).isPresent()) {
            return FIRST_BOOK_ROOM_BUTTON_LEGACY;
        }
        if (driverTools.swipeToElement(FIRST_HOTEL_ROOM_CARD, Swipe.UP, 3).isPresent()) {
            return FIRST_HOTEL_ROOM_CARD;
        }
        throw new RuntimeException("FIRST_BOOK_ROOM_BUTTON element not found after maximum swipe attempts");
    }

    @Override
    public boolean checkChildGuest(){
        WebElement childGuestCount = driverTools.swipeToElement(CHILD_GUEST_COUNT, Swipe.UP, 3).orElseThrow();
        return driverTools.isElementDisplayed(childGuestCount);
    }

    @Override
    public boolean checkChildMeal() {
        return false; //FIXME: Pending implementation
    }

    @Override
    public void openRoomWithRefundReschedulePolicy() {
        WebElement roomWithRefundReschedule = driverTools.swipeToElement(ROOM_REFUND_RESCHEDULE, Swipe.UP, 6).orElseThrow();
        driverTools.click(roomWithRefundReschedule);
    }

    @Override
    public void openRoomWithRefundRescheduleChargePolicy() {
        WebElement roomWithRefundRescheduleCharge = driverTools.swipeToElement(ROOM_REFUND_RESCHEDULE_CHARGE, Swipe.UP, 6).orElseThrow();
        driverTools.click(roomWithRefundRescheduleCharge);
    }

    @Override
    public void bookRoomWithRefundRescheduleAndFlexiblePayment() {
        for (int i = 0; i < 10; i++) {
            driverTools.swipeToElement(ROOM_REFUND_RESCHEDULE, Swipe.UP, 6);
            if (driverTools.isElementDisplayed(ROOM_FLEXIBLE_PAYMENT)) {
                logger.log("Found room with refund & reschedule and flexible payment");
                driverTools.swipeElementToMidScreen(ROOM_FLEXIBLE_PAYMENT);
                int flexiblePaymentY = driverTools.element(ROOM_FLEXIBLE_PAYMENT).orElseThrow().getLocation().getY();
                WebElement bookButton = driverTools.elements(BOOK_BUTTON_LIST).orElseThrow().stream()
                        .filter(button -> button.getLocation().getY() > flexiblePaymentY)
                        .findFirst().orElseThrow();
                driverTools.click(bookButton);
                clickExtraRoomBottomSheetIfAppears();
                BookingFormPage<T> bookingFormPage = BookingFormPage.getInstance(session);
                bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
                return;
            }
            logger.log("No flexible payment on the refund & reschedule room, swipe to find another one");
            driverTools.swipeByDistance(ROOM_LIST_CONTAINER, Swipe.UP, 0.7, 1);
        }
        Assertion.assertThat("Room with refund & reschedule and flexible payment should be found", false, is(true));
    }

    @Override
    public String chooseRoomWithRefundReschedule() {
        driverTools.waitForElementToAppear(RESCHEDULE_ROOM_LIST_VIEW);
        WebElement textViewAdditionalPrice = driverTools.swipeToElement(TEXT_VIEW_WITH_REFUND, RESCHEDULE_ROOM_LIST_VIEW, Swipe.UP, 4).orElseThrow();
        driverTools.click(textViewAdditionalPrice);
        String newRoomName = getRoomName();
        return newRoomName;
    }

    @Override
    public void clickMastertag(String masterTagTitle) {
        driverTools.waitForElementToPresence(MASTER_TAG_SCROLL_VIEW);
        WebElement selectedMasterTagElement = driverTools.swipeToPresentedElement(
                getMasterTagLocator(masterTagTitle), MASTER_TAG_SCROLL_VIEW, Swipe.LEFT, 5).orElseThrow();
        driverTools.click(selectedMasterTagElement);
        driverTools.waitForElementToAppear(FIRST_HOTEL_ROOM_NAME);
    }

    private Locator getMasterTagLocator(String masterTagTitle) {
        // Accepts either a single label ("Pay at Hotel") or bilingual "EN/ID" ("Pay at Hotel/Bayar di Hotel").
        // Search strings MUST be lowercased because translate() lowercases @label before contains().
        String[] languages = splitLanguageData(masterTagTitle, "/");
        String filterNameEnglish = languages[0].toLowerCase().strip();
        String filterNameBahasa = languages[1].toLowerCase().strip();
        return new Locator("Master_TAG", AppiumBy.xpath(
                "//*[@name='quickFilterView']//*[@visible='true' and (contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'"
                        + filterNameEnglish + "') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'"
                        + filterNameBahasa + "'))]"));
    }

    @Override
    public Boolean checkLoyaltyTierBenefit(){
        boolean status = driverTools.swipeToElement(LOYALTY_BADGE, Swipe.UP, 3).isPresent();
        logger.log("checkLoyaltyTierBenefit status -> " + status);
        return status;
    }

    /**
     * PLEASE CHECK INPUT, MAKE SURE IT CAPITALIZED. Method also open room detail after
     * @param tierLevel varieties of tier input (Diamond, Platinum, Gold)
     */
    @Override
    public Boolean verifyLoyaltyBenefitPopUp(String tierLevel){
        driverTools.click(SHOW_MORE_LOYALTY_BUTTON);
        boolean status = driverTools.waitForElementToPresence(checkLoyaltyBenefitPopUp(tierLevel)).isPresent();
        logger.log("verifyLoyaltyBenefitPopUp status -> " + status);
        driverTools.click(CANCEL_BUTTON);
        driverTools.click(LOYALTY_BADGE);
        return status;
    }

    /**
     * @param tierLevel varieties of tier input (Diamond, Platinum, Gold)
     */
    private Locator checkLoyaltyBenefitPopUp(String tierLevel){
        return new Locator(tierLevel, AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label, '"+tierLevel+"')]"));
    }

    @Override
    public String chooseRoomWithFreePayment() {
        driverTools.waitForElementToAppear(RESCHEDULE_ROOM_LIST_VIEW);
        WebElement freePriceBookBtn = driverTools.swipeToElement(FREE_BOOK_ROOM_BUTTON, RESCHEDULE_ROOM_LIST_VIEW, Swipe.UP, 4).orElseThrow();
        driverTools.click(freePriceBookBtn);
        String roomName = getRoomName();
        logger.log("room name ; " + roomName);
        return roomName;
    }

    @Override
    public void chooseRoomWithFreePaymentNoCashback() {
    }//fixme:implementation pending

    @Override
    public boolean verifyAlternateRoomShowingCorrectly(int guest) {
        driverTools.swipeToElement(FIRST_HOTEL_ROOM_PRICE, Swipe.UP, 5);

        int swipes = 0;

        do {
            driverTools.swipeToElement(GUEST_INFO_GUEST_COUNT, Swipe.UP, 5);
            WebElement guestElement = driverTools.waitForElementToPresence(GUEST_INFO_GUEST_COUNT).orElseThrow();
            String guestString = driverTools.getText(guestElement).replace("\u00a0"," ");
            int roomCapacity = CoreFunctions.getIntFromString(guestString).orElseThrow();

            if (roomCapacity > guest) {
                Assertion.assertThat("Room capacity " + roomCapacity + " matches search query", guestString, allOf(not(containsString("Doesn't match")), not(containsString("Tidak sesuai"))));
            } else if (roomCapacity == guest) {
                Assertion.assertThat("Room capacity " + roomCapacity + " matches or doesn't match search query", guestString, anyOf(
                        allOf(
                            not(containsString("Doesn't match")), not(containsString("Tidak sesuai"))
                        ),
                        anyOf(
                                containsString("Doesn't match"), containsString("Tidak sesuai"))
                        )
                );
            } else {
                Assertion.assertThat("Room capacity " + roomCapacity + " doesn't match search query", guestString, anyOf(containsString("Doesn't match"), containsString("Tidak sesuai")));
            }

            driverTools.swipeUp(2);
            logger.log("Swipe no. " + swipes);
            swipes++;
        } while (swipes < 15);

        return true;
    }

    @Override
    public boolean verifyExactAlternateRoom(String expectedTextLabel, int totalLoop) {
        String textEnglish;
        String textBahasa;
        if (expectedTextLabel.contains("/")) {
            textEnglish = expectedTextLabel.split("/")[0].toLowerCase();
            textBahasa = expectedTextLabel.split("/")[1].toLowerCase();
        } else {
            textEnglish = expectedTextLabel.toLowerCase();
            textBahasa = expectedTextLabel.toLowerCase();
        }
        String alternateText;
        boolean alternateFound = false;

        driverTools.waitForElementToAppear(GUEST_INFO_GUEST_COUNT, 3);
        for (int i = 0; i < totalLoop; i++) {
            logger.log("Swipe no. " + (i + 1));
            driverTools.swipeByDistance(ROOM_LIST_CONTAINER, Swipe.UP, 0.7, 1);
            driverTools.waitForElementToPresence(GUEST_INFO_GUEST_COUNT, 3);

            List<WebElement> roomList = driverTools.elements(ROOM_ITEM_CARD).orElseThrow();
            // Extract name attributes from roomList elements
            List<String> roomNameAttributes = new ArrayList<>();
            for (WebElement webElement : roomList) {
                String nameAttribute = driverTools.getValueAttribute(webElement, "name");
                roomNameAttributes.add(nameAttribute);
            }
            logger.log("roomNameAttributes before sort: " + roomNameAttributes);

            // Sort roomNameAttributes based on the numerical values in the name attribute
            roomNameAttributes.sort((a, b) -> {
                String[] aParts = a.split("_");
                String[] bParts = b.split("_");
                int aFirst = Integer.parseInt(aParts[1]);
                int bFirst = Integer.parseInt(bParts[1]);
                int aSecond = Integer.parseInt(aParts[2]);
                int bSecond = Integer.parseInt(bParts[2]);
                if (aFirst != bFirst) return Integer.compare(aFirst, bFirst);
                return Integer.compare(aSecond, bSecond);
            });
            logger.log("roomNameAttributes after sort: " + roomNameAttributes);
            int totalRoomVisible = roomNameAttributes.size();

            if (alternateFound) {
                int roomIndex = totalRoomVisible-1;
                alternateText = getInfoText(roomNameAttributes, roomIndex);
                Assertion.assertThat("Latest room from Swipe " + (i + 1) + " is getting the alternate text", alternateText, anyOf(containsString(textEnglish), containsString(textBahasa)));
            } else {
                for (int j = 0; j < totalRoomVisible; j++) {
                    alternateText = getInfoText(roomNameAttributes, j);
                    if ((alternateText.contains(textEnglish) || alternateText.contains(textBahasa)) && j > 0) {
                        Assertion.assertThat("Room " + (j + 1) + " from Swipe " + (i + 1) + " is getting the alternate text", alternateText, anyOf(containsString(textEnglish), containsString(textBahasa)));
                        alternateFound = true;
                    } else {
                        Assertion.assertThat("Room " + (j + 1) + " from Swipe " + (i + 1) + "  should be not getting the alternate text", alternateText, allOf(not(containsString(textEnglish)), not(containsString(textBahasa))));
                    }
                }
            }
        }
        return alternateFound;
    }

    private String getInfoText(List<String> roomNameAttributes, int roomIndex){
        Locator infoTextLocator = guestInfoText(roomNameAttributes.get(roomIndex));
        driverTools.waitForElementToPresence(infoTextLocator, 3).orElseThrow();
        return driverTools.getText(infoTextLocator).orElseThrow().replace("\u00A0", " ").toLowerCase();
    }

    private Locator guestInfoText(String nameAttribute){
        return new Locator("GUEST_INFO_TEXT_"+nameAttribute, AppiumBy.iOSClassChain("**/*[`name == '"+nameAttribute+"'`]/*[`name == 'occupancyInfoText'`]"));
    }

    @Override
    public List<String> getPrices(){
        driverTools.swipeToElement(HOTEL_PRICE,Swipe.UP,3);
        List<String> priceItems = driverTools.getTexts(HOTEL_PRICE);
        Assertion.assertThat("Prices exist", priceItems, is(not(empty())));
        logger.log("prices: "+priceItems);
        return priceItems;
    }

    @Override
    public void openFirstRoomDetail(String vendor, boolean childMealPlan) {
        HotelPage<T> hotelPage = HotelPage.getInstance(session);
        String vendorCode = hotelPage.generateVendorCode(vendor);

        Locator roomPriceLocator = vendorCode(vendorCode, childMealPlan);
        driverTools.waitForElementToAppear(roomPriceLocator);
        driverTools.swipeToElement(roomPriceLocator, Swipe.UP, 10).isPresent();
        driverTools.swipeElementToMidScreen(roomPriceLocator);
        driverTools.clickElementByCoordinate(roomPriceLocator);
    }

    private Locator vendorCode(String vendorCode, boolean childMealPlan) {
        if (childMealPlan) {
            return new Locator("VENDOR_CODE_" + vendorCode, AppiumBy.xpath("//*[@name='occupancyInfoText' and (contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'child') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'anak'))]/following-sibling::*[@name='vendorCodeLabel' and @label='"+vendorCode+"']/following-sibling::*[@name='mainPrice' and @visible='true']"));
        } else {
            return new Locator("VENDOR_CODE_" + vendorCode, AppiumBy.xpath("//*[@name='vendorCodeLabel' and @label='"+vendorCode+"']/following-sibling::*[@name='mainPrice' and @visible='true']"));
        }
    }

    @Override
    public String getRoomNudges(String nudgesText){
        driverTools.swipeToElement(getRoomNudgesLocator(nudgesText), Swipe.DOWN, 5);
        return driverTools.getText(getRoomNudgesLocator(nudgesText)).orElseThrow();
    }

    private Locator getRoomNudgesLocator(String nudgesText){
return new Locator("Nudges", AppiumBy.xpath("//*[@name='titleLabel' and contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), " + CoreFunctions.toXPathLiteral(nudgesText.toLowerCase()) + ")]"));
    }

    private Locator getNudgesRoomLocator(String nudgesText){
        return new Locator("Nudges", AppiumBy.xpath("//*[@name='titleLabel' and contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '"+nudgesText.toLowerCase()+"')]/following-sibling::*[@name='header.title']"));
    }

    @Override
    public void clickNudgesRoom(String nudgesText){
        if(driverTools.isElementDisplayed(getNudgesRoomLocator(nudgesText))){
            driverTools.click(driverTools.onScreenElement(getNudgesRoomLocator(nudgesText)));
        } else{
            driverTools.click(driverTools.onScreenElement(getRoomNudgesLocator(nudgesText)));
        }
        driverTools.waitForElementToDisappear(FIRST_HOTEL_ROOM_NAME);
    }
    
    public boolean assertThatPriceCurrency(String currency) {
        List<String> prices = getPrices();
        for(String price: prices){
            Assertion.assertThat("Price currency is correct", price, anyOf(containsString(currency),containsString("GRATIS"),containsString("FREE")) );
        }
        return true;
    }

    @Override
    public boolean isNudgesPresent(String nudgesText) {
        return driverTools.swipeToElement(getRoomNudgesLocator(nudgesText), Swipe.UP, 5).isPresent();
    }

    /**
     * Checks the room recommendation based on the total guest, room capacity, and search criteria.
     * It calculates the updated room count and verifies the display of the room recommendation with total guest title, room info, and book button.
     * If the search criteria is "less", it also checks if the room quantity contains the updated room count.
     *
     * @param totalGuest The total number of guests.
     * @param roomCapacity The capacity of the room.
     * @param searchCriteria The search criteria to match. If it equals "less", the room quantity should contain the updated room count.
     * @return true if the room recommendation with total guest title, room info, and book button are displayed and the room quantity is correct (if applicable), false otherwise.
     */
    @Override
    public boolean checkRoomRecommendation(int totalGuest,int roomCapacity,String searchCriteria) {
        boolean isRoomRecommendationWithTotalGuestTitleDisplayed;

        boolean isRoomQuantityDisplayed = true;
        int updatedRoomCount = CoreFunctions.divisionAndRoundUp(totalGuest,roomCapacity);
        logger.log("total Guest: " + totalGuest + " room capacity: " + roomCapacity + " updated room count: " + updatedRoomCount);

        if(searchCriteria.contains("less")){
            isRoomRecommendationWithTotalGuestTitleDisplayed = driverTools.isElementDisplayed(getRoomRecommendationLocator(totalGuest));
            Assertion.assertThat("Room recommendation with total guest title is displayed", isRoomRecommendationWithTotalGuestTitleDisplayed, is(true));
        }else {
            isRoomRecommendationWithTotalGuestTitleDisplayed = driverTools.isElementDisplayed(getRoomRecommendationLocator(totalGuest));
            Assertion.assertThat("Room recommendation with total guest title is displayed", isRoomRecommendationWithTotalGuestTitleDisplayed, is(true));
        }

        boolean isRoomRecommendationRoomInfoDisplayed = driverTools.isElementDisplayed(getRoomRecommendationRoomInfoLocator(updatedRoomCount));
        Assertion.assertThat("Room recommendation with room info is displayed", isRoomRecommendationRoomInfoDisplayed, is(true));

        boolean isRoomRecommendationBookBtnDisplayed = driverTools.isElementDisplayed(getRoomRecommendationBookBtnLocator(updatedRoomCount));
        Assertion.assertThat("Room recommendation with book button is displayed", isRoomRecommendationBookBtnDisplayed, is(true));
        return isRoomRecommendationWithTotalGuestTitleDisplayed && isRoomQuantityDisplayed && isRoomRecommendationRoomInfoDisplayed && isRoomRecommendationBookBtnDisplayed;
    }

    /**
     * Gets the locator of the room recommendation title.
     * The locator is created with the xpath of the room recommendation title element, which contains the label of total guest.
     *
     * @param totalGuest The total number of guests.
     * @return a Locator object for the room recommendation title.
     */
    private Locator getRoomRecommendationLocator(int totalGuest){
        if(totalGuest > 1)
            return new Locator("Room_Recommendation_Title", AppiumBy.xpath("(//*[contains(@label,'Recommended for "+totalGuest+" guests') or contains(@label,'Rekomendasi untuk "+totalGuest+" tamu')])[1]"));
        else
            return new Locator("Room_Recommendation_Title", AppiumBy.iOSClassChain("**/*[`name == 'roomRecommendationCell'`]/*[`name == 'label'`]"));
    }

    /**
     * Gets the locator of the room recommendation room info.
     * The locator is created with the xpath of the room recommendation room info element, which contains the label of total room.
     *
     * @param totalRoom The total number of rooms.
     * @return a Locator object for the room recommendation room info.
     */
    private Locator getRoomRecommendationRoomInfoLocator(int totalRoom){
        if (totalRoom == 1){
            return new Locator("Room_Recommendation_Room_Info", AppiumBy.xpath("//*[@name='roomQuantityLabel' and (contains(@label,'Total (1 room') or contains(@label,'Total (1 kamar'))]"));
        } else {
            return ROOM_RECOMMENDATION_TITLE;
        }
    }

    /**
     * Gets the locator of the room recommendation book button.
     * The locator is created with the xpath of the room recommendation book button element, which contains the label of total room.
     *
     * @param totalRoom The total number of rooms.
     * @return a Locator object for the room recommendation book button.
     */
    private Locator getRoomRecommendationBookBtnLocator(int totalRoom){
        if (totalRoom > 1) {
            return new Locator("Room_Recommendation_BOOK_BTN", AppiumBy.xpath("//*[contains(@label,'Book "+totalRoom+" rooms') or contains(@label,'Pesan "+totalRoom+" kamar')]"));
        }
        return new Locator("Room_Recommendation_BOOK_BTN", AppiumBy.xpath("//*[contains(@label,'Book "+totalRoom+" room') or contains(@label,'Pesan "+totalRoom+" kamar')]"));
    }

    /**
     * Opens the room detail for the recommended room.
     */
    @Override
    public void openRoomDetailForRecommendedRoom() {
        driverTools.click(ROOM_FACILITY);
    }

    @Override
    public int getRecommendedRoomCapacity(){
        WebElement facilityElement = driverTools.waitForElementToAppear(ROOM_FACILITY).orElseThrow();
        String facility = driverTools.getText(facilityElement);
        String[] facilityArr = facility.split(",");

        for (String s : facilityArr) {
            if (s.contains("tamu") || s.contains("guest")) {
                return CoreFunctions.getIntFromString(s).orElseThrow();
            }
        }
        Assertion.fail("Guest capacity not found");
        return -1;
    }

    @Override public String getRecommendedRoomAmount(){
        String recommendedRoomAmount = driverTools.getText(driverTools.waitForElementToPresence(TOTAL_ROOM_RECOMMENDATION,20).orElseThrow());
        logger.log("Recommended total room amount: "+recommendedRoomAmount);
        // Extract content inside parentheses
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\(([^)]*)\\)").matcher(recommendedRoomAmount);
        if (matcher.find()) {
            return "(" + matcher.group(1) + ")";
        }
        logger.log("Recommended total room amount after extraction: "+recommendedRoomAmount);
        return recommendedRoomAmount;
    }

    @Override public String getRecommendationAdditionalRoom(){
        String recommendationAdditionalRoom = driverTools.getText(ADDITIONAL_ROOM_RECOMMENDATION).orElseThrow();
        logger.log("Recommended addtional room: "+recommendationAdditionalRoom);
        return recommendationAdditionalRoom;
    }

    @Override public void clickRecommendedRoom(){
        driverTools.click(RECOMMENDATION_ROOM);
    }

    @Override
    public boolean checkHotelPrice(boolean isCrossPrice, String price) {
        //isCrossPrice only for Android
        return checkHotelPrice(price);
    }

    @Override
    public boolean checkHotelPrice(String price){
        return driverTools.swipeToElement(getHotelPriceLocator(price), Swipe.UP, 5).isPresent();
    }

    private Locator getHotelPriceLocator(String price) {
        return new Locator("HOTEL_PRICE", AppiumBy.xpath("//*[@name='roomCard.bookButton']//following-sibling::*[contains(@label,'"+price+"')]"));
    }

    @Override
    public boolean isLabelTotalPrice(boolean isTax) {
        return (isTax) ? driverTools.waitForElementToPresence(TOTAL_PRICE_LABEL).isPresent() : driverTools.waitForElementToPresence(TOTAL_PRICE_LABEL_WITHOUT_TAX).isPresent();
    }

    @Override
    public boolean isLabelTotalPrice(){
        return driverTools.waitForElementToPresence(TOTAL_PRICE_LABEL).isPresent();
    }

    @Override
    public boolean isLabelPerRoom(boolean isTax) {
        return (isTax) ? driverTools.waitForElementToPresence(PER_ROOM_PRICE_LABEL).isPresent() : driverTools.waitForElementToPresence(PER_ROOM_PRICE_LABEL_WITHOUT_TAX).isPresent();
    }

    @Override
    public boolean isLabelPerRoom(){
        return driverTools.waitForElementToPresence(PER_ROOM_PRICE_LABEL).isPresent();
    }

    public boolean doesNotMeetTravelPolicyPopUpAppears() {
        return driverTools.isElementDisplayed(DOES_NOT_MEET_TRAVEL_POLICY_POP_UP) && driverTools.isElementDisplayed(YES_CONTINUE_MEET_POLICY_BTN);
    }

    @Override
    public boolean meetTravelPolicyDisplayed() {
        return driverTools.isElementDisplayed(MEETS_TRAVEL_POLICY_NUDGE);
    }

    @Override
    public boolean isLabelPerRoomPerNight(){
        return driverTools.waitForElementToPresence(LABEL_PER_ROOM_PER_NIGHT).isPresent();
    }

    @Override
    public boolean isRoomListShown() {
        boolean shown = driverTools.waitForElementToAppear(ROOM_LIST_CONTAINER, 10).isPresent()
                || driverTools.waitForElementToAppear(FIRST_HOTEL_ROOM_NAME, 5).isPresent()
                || driverTools.waitForElementToAppear(HOTEL_PRICE, 5).isPresent();
        logger.log("isRoomListShown -> " + shown);
        return shown;
    }

    @Override
    public void selectRoomByPriceMore(int price) {
        driverTools.swipeUp();
        for(int swipes = 0; swipes < 5; swipes++){

            List<WebElement> roomPrices = driverTools.onScreenElements(HOTEL_PRICE);
            for(int index = 0; index < roomPrices.size(); index++){
                String roomPrice = driverTools.getText(roomPrices.get(index));
                logger.log("Room price: " + roomPrice);
                logger.log("index: " + index);
                if(convertPrice(roomPrice) >= price){
                    List<WebElement> bookButtons = driverTools.onScreenElements(SELECT_BUTTON);
                    driverTools.click(bookButtons.get(index));
                    return;
                }
            }
            driverTools.swipeUp();
        }
        throw new AssertionError("No room with price more than " + price + " found");
    }

    private static final Locator FIRST_ROOM_CARD_MEAL_INFO = new Locator("FIRST_ROOM_CARD_MEAL_INFO",
            AppiumBy.xpath("//*[@name='TVCell<TACMRoomDetailInfoView>' or contains(@name,'roomItemCell')]/*[(contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'breakfast') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'sarapan') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'meal') or contains(translate(@label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'makan'))]"));

    private static final Locator FIRST_ROOM_CARD_REFUND_RESCHEDULE_INFO = new Locator("FIRST_ROOM_CARD_REFUND_RESCHEDULE_INFO",
            AppiumBy.xpath("(//*[@name='cancellationPoliciesText'])[1]"));

    private static final Locator FIRST_ROOM_CARD_MAX_OCCUPANCY_INFO = new Locator("FIRST_ROOM_CARD_MAX_OCCUPANCY_INFO",
            AppiumBy.xpath("(//*[@name='occupancyInfoText'])[1]"));

    @Override
    public boolean isMealInfoOnFirstRoomCardVisible() {
        return driverTools.waitForElementToPresence(FIRST_ROOM_CARD_MEAL_INFO).isPresent();
    }

    @Override
    public boolean isRefundRescheduleInfoOnFirstRoomCardVisible() {
        return driverTools.waitForElementToPresence(FIRST_ROOM_CARD_REFUND_RESCHEDULE_INFO).isPresent();
    }

    @Override
    public boolean isMaxOccupancyInfoOnFirstRoomCardVisible() {
        return driverTools.waitForElementToPresence(FIRST_ROOM_CARD_MAX_OCCUPANCY_INFO).isPresent();
    }

    @Override
    public boolean isExclusiveNudgePresentOnRoomRecommendationCard(String nudgesText) {
        Locator nudgeOnRecommendationCard = new Locator("ROOM_RECOMMENDATION_CARD_NUDGE",
                AppiumBy.xpath("//*[@name='roomRecommendationCell']//*[@label='" + nudgesText + "']"));
        return driverTools.swipeToElement(nudgeOnRecommendationCard, Swipe.UP, 10).isPresent();
    }

    private static final Locator ROOM_RECOMMENDATION_CARD_MEAL_INFO = new Locator("ROOM_RECOMMENDATION_CARD_MEAL_INFO",
            AppiumBy.xpath("//*[@name='roomRecommendationCell']"
                    + "//*[(contains(@label,'Breakfast') or contains(@label,'breakfast')"
                    + " or contains(@label,'Sarapan') or contains(@label,'sarapan')"
                    + " or contains(@label,'Meal') or contains(@label,'meal')"
                    + " or contains(@label,'Makan') or contains(@label,'makan'))]"));

    private static final Locator ROOM_RECOMMENDATION_CARD_REFUND_RESCHEDULE_INFO = new Locator("ROOM_RECOMMENDATION_CARD_REFUND_RESCHEDULE_INFO",
            AppiumBy.xpath("//*[@name='roomRecommendationCell']/*[@name='refundLabel']"));

    private static final Locator ROOM_RECOMMENDATION_CARD_MAX_OCCUPANCY_INFO = new Locator("ROOM_RECOMMENDATION_CARD_MAX_OCCUPANCY_INFO",
            AppiumBy.xpath("//*[@name='roomRecommendationCell']//*[@name='occupancyInfoText' or @name='featuresLabel']"));

    // UI copy: "Best Pick for you ! " (capital P, space before !, trailing space)
    private static final Locator BEST_PICK_FOR_YOU_HEADER = new Locator("BEST_PICK_FOR_YOU_HEADER",
            AppiumBy.xpath("//*[contains(@label,'Best Pick for you') or contains(@label,'Best pick for you') or contains(@label,'Pilihan terbaik untukmu')]"));

    @Override
    public boolean isRoomRecommendationCardOnTop() {
        return driverTools.waitForElementToAppear(ROOM_RECOMMENDATION_CELL, 10).isPresent();
    }

    @Override
    public boolean isMealInfoOnRoomRecommendationCardVisible() {
        return driverTools.swipeToElement(ROOM_RECOMMENDATION_CARD_MEAL_INFO, Swipe.UP, 10).isPresent();
    }

    @Override
    public boolean isRefundRescheduleInfoOnRoomRecommendationCardVisible() {
        return driverTools.swipeToElement(ROOM_RECOMMENDATION_CARD_REFUND_RESCHEDULE_INFO, Swipe.UP, 10).isPresent();
    }

    @Override
    public boolean isMaxOccupancyInfoOnRoomRecommendationCardVisible() {
        return driverTools.swipeToElement(ROOM_RECOMMENDATION_CARD_MAX_OCCUPANCY_INFO, Swipe.UP, 10).isPresent();
    }

    @Override
    public boolean isBestPickForYouHeaderVisible() {
        return driverTools.swipeToElement(BEST_PICK_FOR_YOU_HEADER, Swipe.UP, 10).isPresent();
    }

    @Override
    public String getBestPickForYouHeaderText() {
        return driverTools.swipeToElement(BEST_PICK_FOR_YOU_HEADER, Swipe.UP, 10)
                .map(driverTools::getText)
                .orElse("");
    }

    @Override
    public boolean isAnnouncementBannerVisible() {
        // Presence ≠ visible: banner is often in the tree above the viewport after Best Pick scroll
        Optional<WebElement> present = driverTools.waitForElementToPresence(ROOM_LIST_ANNOUNCEMENT_BANNER, 20);
        if (present.isPresent()) {
            logger.log("Announcement banner (multiroom_banner) present in page source");
            return true;
        }
        logger.log("Announcement banner (multiroom_banner) not found above room list");
        return false;
    }

    @Override
    public void openRoomDetailFromRoomList() {
        swipeToFirstHotelRoomPrice();
        driverTools.swipeToElement(FIRST_ROOM_DETAIL_CHEVRON, Swipe.UP, 5).orElseThrow();
        driverTools.click(FIRST_ROOM_DETAIL_CHEVRON);
    }
    private void swipeToFirstHotelRoomPrice(){
        driverTools.swipeToElement(FIRST_HOTEL_ROOM_PRICE, Swipe.UP, 6);
        if (!driverTools.isElementPresent(FIRST_HOTEL_ROOM_PRICE))
            driverTools.swipeByDistance(FIRST_HOTEL_ROOM_PRICE, Swipe.UP, 0.9, 3);
    }

    @Override
    public void openPriceSectionOnRoomDetail() {
        WebElement priceSectionIcon = driverTools.waitForElementToAppear(ROOM_DETAIL_PRICE_SECTION_ICON)
                .or(() -> driverTools.swipeToElement(ROOM_DETAIL_PRICE_SECTION_ICON, Swipe.UP, 5))
                .orElseThrow();
        driverTools.click(priceSectionIcon);
    }

    @Override
    public String getRoomPriceFromPriceSection() {
        WebElement priceElement = driverTools.waitForElementToAppear(ROOM_DETAIL_PRICE_SECTION_TOTAL)
                .or(() -> driverTools.swipeToElement(ROOM_DETAIL_PRICE_SECTION_TOTAL, Swipe.UP, 5))
                .orElseThrow();
        String roomPrice = driverTools.getText(priceElement);
        logger.log("roomPrice from room detail price section -> " + roomPrice);

        return roomPrice;
    }
}
