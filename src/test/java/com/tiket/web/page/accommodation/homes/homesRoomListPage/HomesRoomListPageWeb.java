package com.tiket.web.page.accommodation.homes.homesRoomListPage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.*;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.core.Is.is;

public abstract class HomesRoomListPageWeb<T extends WebDriver> extends WebPage<T> {

    public HomesRoomListPageWeb(Session<T> session) { super(session); }

    public static <T extends WebDriver> HomesRoomListPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HomesRoomListPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new HomesRoomListPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator ROOM_BOOKING_BUTTON = new Locator("ROOM_BOOKING_BUTTON", new By.ByXPath("//button[@data-testid='room-card-button-book']"));
    private static final Locator CURRENT_ROOM_AMOUNT = new Locator("CURRENT_ROOM_AMOUNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[1]"));
    private static final Locator BEST_PICK_TEXT = new Locator("BEST_PICK_TEXT", new By.ByXPath("(//*[@data-testid='room-recommendation-container']//h3)[2]"));
    private static final Locator ROOM_PLUS = new Locator("ROOM_PLUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[2]"));
    private static final Locator ROOM_MINUS = new Locator("ROOM_MINUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[1]"));
    private static final Locator BOOK_ROOM = new Locator("BOOK_ROOM", new By.ByXPath("//*[@data-testid='room-card-button-book']"));
    private static final Locator FIRST_ROOM_NAME= new Locator("BOOK_ROOM_DETAIL", new By.ByXPath("(//div[@data-testid='room-card-container'])[1]//h3"));
    private static final Locator FIRST_ROOM_CARD = new Locator("FIRST_ROOM_CARD", new By.ByXPath("(//div[@data-testid='room-card-container'])[1]"));
    private static final Locator FIRST_ROOM_IMG = new Locator("FIRST_ROOM_IMG", new By.ByXPath("(//div[contains(@class, 'ListGroup_list_group')]//img[@data-testid='final-result-image'])[1]"));
    private static final Locator FIRST_SELECT_CARD = new Locator("FIRST_SELECT_CARD", new By.ByXPath("//button[text()='Select']"));
    private static final Locator PRICE_FOOTER = new Locator("PRICE_FOOTER", new By.ByXPath("//div[@data-testid='multi-order-price-footer-button-view-price-details']"));
    private static final Locator EXACT_MATCH_ROOM_CONTAINER = new Locator("EXACT_MATCH_ROOM_CONTAINER", new By.ByXPath("//div[@data-testid='exact-list-container']"));
    private static final Locator MULTI_ORDER_BANNER = new Locator("MULTI_ORDER_BANNER", new By.ByXPath("//button[contains(@data-testid, 'multi-order-price-footer-button-book')]"));
    private static final Locator SELECT_ROOM_BUTTON = new Locator("SELECT_ROOM_BUTTON", new By.ByXPath("//button[text()='Select']"));
    private static final Locator ROOM_NAME_DETAIL=new Locator("ROOM_NAME_DETAIL",new By.ByXPath("//span[contains(@class, 'Text_size_h2') and contains(@class, 'Text_weight_bold')]"));
    private static final Locator RECOMMENDED_ROOM_GUEST = new Locator("RECOMMENDED_ROOM_GUEST", new By.ByXPath("(//*[@data-testid='room-recommendation-container']//h3)[1]"));
    private static final Locator RECOMMENDED_ROOM = new Locator("RECOMMENDED_ROOM", new By.ByXPath("//*[@data-testid='room-recommendation-container']"));
    private static final Locator SELECT_BOOK_BUTTON = new Locator("SELECT_ROOM_BUTTON", new By.ByXPath("//button[@data-testid='room-card-button-book']"));
    private static final Locator FIRST_BOOK_CARD = new Locator("FIRST_BOOK_CARD", new By.ByXPath("//button[contains(@data-testid,'book-button')]"));
    private static final Locator CURRENT_CHILD_AMOUNT = new Locator("CURRENT_CHILD_AMOUNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[3]"));
    private static final Locator CHILD_PLUS = new Locator("CHILD_PLUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[6]"));
    private static final Locator CHILD_MINUS = new Locator("CHILD_MINUS", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[5]"));
    private static final Locator SEE_ROOMS_BUTTON = new Locator("SEE_ROOMS_BUTTON", new By.ByXPath("//button[@data-testid='button-see-room']"));
    private static final Locator ROOM_FILTER_APPLIED = new Locator("ROOM_FILTER_APPLIED", new By.ByXPath("//span[@data-testid='room-filter-label']"));
    private static final Locator PROPERTY_NAME_LABEL = new Locator("PROPERTY_NAME_LABEL", new By.ByXPath("//h1[@data-testid=\"name\"]"));
    private static final Locator SELECTED_FILTER = new Locator("SELECTED_FILTER", new By.ByXPath("//*[contains(@class,'desktop_active_chip_icon')]//parent::*[contains(@class,'Chip_text')]"));

    private static Locator getMasterTagFilterChipByName(String filterEN, String filterID) {
        return new Locator("MASTER_TAG_FILTER_CHIP", By.xpath(String.format("//*[@data-testid=\"room-list-filter-chip-item\"]//*[text()='%s' or text()='%s']", filterEN, filterID)));
    }

    private static Locator getMealPlanLabelOnRoomCardByRoomName(String mealPlanEN, String mealPlanID, String roomName) {
        return new Locator("MEAL_PLAN_LABEL", By.xpath(String.format("//*[contains(@class,'RoomCard_title_text')][text()='%s']//ancestor::*[@data-testid='room-card-container']//*[@data-testid='bedroom-content-container']//following-sibling::*[contains(@class,'RoomCard_list_item')][1]/p[contains(text(),'%s') or contains(text(),'%s')]", roomName, mealPlanEN, mealPlanID)));
    }

    private static Locator getMealPlanLabelOnRoomCard(String mealPlanEN, String mealPlanID) {
        return new Locator("MEAL_PLAN_LABEL", By.xpath(String.format("//*[@data-testid='room-card-container']//*[@data-testid='bedroom-content-container']//following-sibling::*[contains(@class,'RoomCard_list_item')][1]/p[contains(text(),'%s') or contains(text(),'%s')]", mealPlanEN, mealPlanID)));
    }

    public void setTotal(int current, int target, Locator plusButton, Locator minusButton, String type) {
        logger.log(type + " current value is '" + current + "'");
        if (target > current) {
            for (int i = 0; i < target - current; i++) {
                driverTools.clickWithJavascriptExecutor(plusButton);
            }
        } else if (target < current) {
            for (int i = 0; i < current - target; i++) {
                driverTools.clickWithJavascriptExecutor(minusButton);
            }
        }
    }

    public String getBookingButtonText() {
        driverTools.waitForElementToAppear(ROOM_BOOKING_BUTTON, 5).orElseThrow();
        return driverTools.getText(ROOM_BOOKING_BUTTON).orElseThrow();
    }

    public void setTotalRoom(int room) {
        int currentRoom = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ROOM_AMOUNT, "value"));
        setTotal(currentRoom, room, ROOM_PLUS, ROOM_MINUS, "Room");
    }

    public abstract void clickGuestDoneButton();

    public abstract void clickRoomListChangeButton();

    public abstract void clickRoomNGuestButton();

    public boolean isRoomListSectionVisible(){
        driverTools.waitForElementToAppear(FIRST_ROOM_CARD,5);
        return driverTools.isElementDisplayed(FIRST_ROOM_CARD);
    }

    public boolean isRoomImageVisible(){
        driverTools.waitForElementToAppear(FIRST_ROOM_IMG,5);
        return driverTools.isElementDisplayed(FIRST_ROOM_IMG);
    }

    @SneakyThrows
    public void clickBookButton() throws InterruptedException {
        driverTools.waitForElementToPresence(BOOK_ROOM, 15).orElseThrow();
        driverTools.scroll(2,20);
        driverTools.scrollIntoWebView(BOOK_ROOM);
        driverTools.clickWithJavascriptExecutor(BOOK_ROOM);
    }

    public String getRoomName(){
        WebElement webElement = driverTools.waitForElementToAppear(FIRST_ROOM_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

    public void roomListDetails(int index) {
        for (int i = 1; i <= index; i++) {
            Assertion.assertThat("Room no " + i + " is visible on room List", isRoomVisible(i, ""), is(true));
            Assertion.assertThat("Room no " + i + "'s name is visible on room List", isRoomVisible(i, "//h3"), is(true));
            Assertion.assertThat("Room no " + i + "'s guest number is visible", isRoomVisible(i, "(//p)[1]"), is(true));
            Assertion.assertThat("Room no " + i + "'s bed type is visible", isRoomVisible(i, "(//p)[2]"), is(true));
            Assertion.assertThat("Room no " + i + "'s Price is visible on room list card", isRoomVisible(i, "//div[contains(@class, 'final')]"), is(true));
        }
    }

    public boolean isRoomVisible(int index, String subElement) {
        Locator roomElement = getLocatorForRoom(index, subElement);
        driverTools.waitForElementToAppear(roomElement, 5);
        return driverTools.isElementDisplayed(roomElement);
    }

    private Locator getLocatorForRoom(int index, String subElement) {
        // Base XPath for the room container
        String xpath = "(//div[@data-testid='room-card-container'])[" + index + "]";
        // Adding the parentheses dynamically if the subElement contains "p"
        if (subElement.contains("p")) {
            xpath = "(" + xpath + subElement + ")";
        } else if (!subElement.isEmpty()) {
            // For other subElements, append directly
            xpath += subElement;
        }
        return new Locator("IndividualRoom", new By.ByXPath(xpath));
    }

    public void setTotalChild(int child) {
        int currentChild = Integer.parseInt(driverTools.getValueAttribute(CURRENT_CHILD_AMOUNT, "value"));
        setTotal(currentChild, child, CHILD_PLUS, CHILD_MINUS, "Child");
    }

    public abstract void setTotalGuest(int guests);

    public abstract void clickGuestDoneAndSubmitButton();

    public void scrollToRoomListSelectSection(){
        driverTools.waitForElementToAppear(FIRST_SELECT_CARD,5);
        driverTools.swipeToElement(FIRST_SELECT_CARD, Swipe.UP, 5);
    }

    public void clickSelectRoomButton() {
        driverTools.waitForElementsToAppear(SELECT_ROOM_BUTTON);
        driverTools.click(SELECT_ROOM_BUTTON);
    }

    public void clickSelectRoomButtonMultiOrder(int noOfRoom) {
        Locator SELECT_ROOM_BUTTON = new Locator("BOOK_ROOM", new By.ByXPath("(//button[text()='Select'])[1]"));
        int totalScrollAmount = 0;

        for (int i = 1; i <= noOfRoom; i++) {
            long startTime = System.currentTimeMillis();

            while (!driverTools.isElementDisplayed(SELECT_ROOM_BUTTON)) {
                if (System.currentTimeMillis() - startTime > 10000) {
                    throw new TimeoutException("Failed to display 'Select Room' button within 10 seconds");
                }
                driverTools.scrollDown(100);
                totalScrollAmount += 100;
            }

            driverTools.scrollDown(200);
            totalScrollAmount += 200;

            driverTools.waitForElementToBeClickable(SELECT_ROOM_BUTTON);

            try {
                driverTools.click(SELECT_ROOM_BUTTON);
            } catch (ElementClickInterceptedException e) {
                driverTools.scrollDown(200);
                totalScrollAmount += 200;
                driverTools.click(SELECT_ROOM_BUTTON);
            }
        }

        driverTools.scrollDown(-totalScrollAmount);
    }
    public void scrollToIncrement(){
        driverTools.waitForElementToAppear(ROOM_PLUS,5);
        driverTools.swipeToElement(ROOM_PLUS, Swipe.UP, 5);
    }

    public void clickIncrement(){
        driverTools.waitForElementToAppear(ROOM_PLUS,5);
        for(int i = 0; i < 8; i++){
            driverTools.swipeToElement(ROOM_PLUS, Swipe.UP, 5);
            driverTools.clickWithJavascriptExecutor(ROOM_PLUS);
        }
    }

    public boolean isMoBannerVisible() {
        driverTools.scrollUpUntilElementFound(MULTI_ORDER_BANNER, 300, 5);
        driverTools.waitForElementToAppear(MULTI_ORDER_BANNER, 5);
        return driverTools.isElementDisplayed(MULTI_ORDER_BANNER);
    }

    public boolean isExactMatchRoomVisible() {
        driverTools.waitForElementToAppear(EXACT_MATCH_ROOM_CONTAINER, 5);
        return driverTools.isElementDisplayed(EXACT_MATCH_ROOM_CONTAINER);
    }

    public boolean isPriceFooterVisible() {
        driverTools.scrollUpUntilElementFound(PRICE_FOOTER, 300, 5);
        driverTools.waitForElementToAppear(PRICE_FOOTER, 5);
        return driverTools.isElementDisplayed(PRICE_FOOTER);
    }

    public void clickFooterBookButton() {
        By footerBookButton = By.xpath("//button[@data-testid='multi-order-price-footer-button-book' and text()='Book']");
        driverTools.click(driver.findElement(footerBookButton));
    }
    public void clickBookRoomButton() {
        driverTools.waitForElementsToAppear(SELECT_BOOK_BUTTON);
        driverTools.click(SELECT_BOOK_BUTTON);
    }

    public abstract void clickChevronButton();

    public void scrollToRoomListBookSection(){
        driverTools.waitForElementToAppear(FIRST_BOOK_CARD,5);
        driverTools.swipeToElement(FIRST_BOOK_CARD, Swipe.UP, 5);
    }

    public void recommendedRoomForGuest(int guest){
        String GuestText=String.valueOf(guest);
        WebElement headingText = driverTools.waitForElementToAppear(RECOMMENDED_ROOM_GUEST).orElseThrow();
        String text= driverTools.getText(headingText);
        if(guest==1){
            Assertion.assertThat("Guest number on recommended Text should match the searched Guest",text, containsString("solo"));
        }else{
            Assertion.assertThat("Guest number on recommended Text should match the searched Guest",text, containsString(GuestText));
        }

        WebElement BestPick=driverTools.waitForElementToAppear(BEST_PICK_TEXT).orElseThrow();
        String BestPickMessage=driverTools.getText(BestPick);

        Assertion.assertThat("Best pick for you HEADING should be visible based on Selected Language",BestPickMessage, either(containsString("Best pick for you")).or(containsString("Pilihan terbaik untukmu")));
    }

    public boolean isRecommendedRoomVisible(){
        driverTools.waitForElementToAppear(RECOMMENDED_ROOM,5);
        return driverTools.isElementDisplayed(RECOMMENDED_ROOM);
    }

    public void selectMasterTagFilter(String en, String id) {
        driverTools.click(getMasterTagFilterChipByName(en, id));
    }

    public boolean isMealPlanOnRoomCardDisplayed(String mealPlanEN, String mealPlanID, String roomName) {
        return driverTools.waitForElementToAppear(getMealPlanLabelOnRoomCardByRoomName(mealPlanEN, mealPlanID, roomName)).isPresent();
    }

    public boolean isMealPlanOnRoomCardDisplayed(String mealPlanEN, String mealPlanID) {
        boolean result = false;
        List<WebElement> mealPlanLabel = driverTools.waitForElementsToAppear(getMealPlanLabelOnRoomCard(mealPlanEN, mealPlanID)).orElseThrow();
        for (int i = 0; i < mealPlanLabel.size(); i++) {
            if (mealPlanLabel.get(i).isDisplayed()) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        return result;
    }

    public String getRoomNameFromDetail(){
        WebElement webElement=driverTools.waitForElementToAppear(ROOM_NAME_DETAIL).orElseThrow();
        return driverTools.getText(webElement);
    }

    public void openRoomDetail(){
        driverTools.waitForElementToAppear(FIRST_ROOM_CARD,5);
        driverTools.clickWithJavascriptExecutor(FIRST_ROOM_CARD);
    }

    public Boolean checkRoomCardGuestCount(int count) {
        // Covering 2 language and 2 different use case of wording
        String locatorTemplate = "(//*[@data-testid='room-card-container'])[1]" +
                "//*[contains(text(), '%d Tamu') or " +
                "contains(text(), '%d Guest') or " +
                "contains(text(), '%d Dewasa') or " +
                "contains(text(), '%d Adult')]";
        Locator roomCardGuest = new Locator("ROOM_CARD_GUEST_TEXT", new By.ByXPath(String.format(locatorTemplate, count, count, count, count)));

        driverTools.waitForElementToAppear(roomCardGuest);
        return driverTools.isElementDisplayed(roomCardGuest);
    }

    public Boolean checkRoomCardChildCount(int count) {
        String locatorTemplate = "(//*[@data-testid='room-card-container'])[1]//*[contains(text(), '%d Anak') or contains(text(), '%d Child')]";
        Locator roomCardChild = new Locator("ROOM_CARD_CHILD_TEXT", new By.ByXPath(String.format(locatorTemplate, count, count)));

        driverTools.waitForElementToAppear(roomCardChild);
        return driverTools.isElementDisplayed(roomCardChild);
    }

    public void clickSeeRooms() {
    }

    public void clickFilter() {
    }

    public String getSelectedFilter() {
        driverTools.waitForElementToAppear(HomesRoomListPageDWeb.SELECT_FILTER_ROOM_LIST, 5);
        return driverTools.getText(HomesRoomListPageDWeb.SELECT_FILTER_ROOM_LIST).orElseThrow();
    }

    private Locator getSelectedFilterLocator(String filter) {
        return new Locator("SELECTED_FILTER", new By.ByXPath("//*[contains(@class,'RatePlanCard_rate_plan_card_item_container')]//*[contains(text(),'"+filter+"')]"));
    }

    public boolean isFilterSelected(String filter) {
        driverTools.waitForElementToAppear(getSelectedFilterLocator(filter), 5);
        return driverTools.isElementDisplayed(getSelectedFilterLocator(filter));
    }

    public boolean isFilterRoomListApplied() {
        driverTools.waitForElementsToAppear(ROOM_FILTER_APPLIED, 5);
        return driverTools.isElementDisplayed(ROOM_FILTER_APPLIED);
    }

    public String getPropertyNameLabel() {
        driverTools.waitForElementsToAppear(PROPERTY_NAME_LABEL);
        return driverTools.getText(PROPERTY_NAME_LABEL).orElseThrow();
    }
}
