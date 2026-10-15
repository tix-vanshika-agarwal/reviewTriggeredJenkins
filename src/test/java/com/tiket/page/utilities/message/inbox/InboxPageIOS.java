package com.tiket.page.utilities.message.inbox;

import com.tiket.appium.DriverTools.SideOfElement;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.home.HomePage;
import com.tiket.page.utilities.message.help_center.HelpCenterPage;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class InboxPageIOS <T extends WebDriver> extends InboxPage<T>{

    private static final Locator SKIP_LOCATOR = new Locator("SKIP_BUTTON", AppiumBy.xpath("//XCUIElementTypeStaticText[@name=\"skip_button\"]"));
    private static final Locator BACK_LOCATOR_NOTIFICATION = new Locator("BACK_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name=\"back\"]"));
    private static final Locator BACK_BTN = new Locator("BACK_BTN", AppiumBy.name("tds ic back"));
    private static final Locator INBOX_LABEL = new Locator("INBOX_LABEL", AppiumBy.name("inbox_header"));
    private static final Locator NOTIFICATION_TAB = new Locator("NOTIFICATION_TAB", AppiumBy.xpath("//*[contains(@name,'notification_tab')]"));
    private static final Locator CHAT_TAB = new Locator("CHAT_TAB", AppiumBy.xpath("//*[contains(@name,'chat_tab')]"));
    private static final Locator INBOX_NOTIFICATION_AREA = new Locator("INBOX_NOTIFICATION_AREA", AppiumBy.name("InboxView"));
    private static final Locator INBOX_NOTIFICATION_LIST = new Locator("INBOX_NOTIFICATION_LIST", AppiumBy.xpath("//*[contains(@name,'inbox_notification_title')]"));
    private static final Locator LEGAL_BANNER = new Locator("LEGAL_BANNER", AppiumBy.accessibilityId("banner_container"));
    private static final Locator ABOUT_PERSONAL_DATA_POPUP_TITLE = new Locator("ABOUT_PERSONAL_DATA_POPUP_TITLE", AppiumBy.xpath("(//XCUIElementTypeStaticText[@label='About Personal Data Security' or @label='Tentang Keamanan Data Pribadi'])[2]"));
    private static final Locator ABOUT_PERSONAL_DATA_CONTENT = new Locator("ABOUT_PERSONAL_DATA_CONTENT", AppiumBy.xpath("//XCUIElementTypeLink[@label='Kebijakan Privasi' or @label='Privacy Policy']"));
    private static final Locator UNREAD_COUNT_NOTIFICATION = new Locator("UNREAD_COUNT_NOTIFICATION", AppiumBy.xpath("//XCUIElementTypeOther[XCUIElementTypeStaticText[@label='Notification']]//XCUIElementTypeOther//XCUIElementTypeStaticText"));
    private static final Locator UNREAD_COUNT_CHAT = new Locator("UNREAD_COUNT_CHAT", AppiumBy.xpath("//XCUIElementTypeOther[XCUIElementTypeStaticText[@label='Chat']]//XCUIElementTypeOther//XCUIElementTypeStaticText"));
    private static final Locator CHAT_MESSAGE_BAR = new Locator("CHAT_MESSAGE_BAR", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Type your message') or contains(@label,'Ketik pesan') or contains(@label,'Tulis pesan')]"));
    private static final Locator SEND_CHAT_BTN = new Locator("SEND_CHAT_BTN", AppiumBy.accessibilityId("tds_ic_oval_chat_send"));
    private static final Locator LAST_MESSAGE_IN_CHAT_ROOM = new Locator("LAST_MESSAGE_IN_CHAT_ROOM", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label, \"Automatically sent by system\") or contains(@label, \"Dikirim otomatis oleh sistem\")])[last()]"));
    private static final Locator LAST_REPLIED_CHAT_MESSAGE_IN_CHAT_ROOM = new Locator("LAST_REPLIED_CHAT_MESSAGE_IN_CHAT_ROOM", AppiumBy.xpath("(//XCUIElementTypeCell[last()]//XCUIElementTypeStaticText)[2]"));
    private static final Locator REPLY_CHAT_BTN = new Locator("REPLY_CHAT_BTN", AppiumBy.xpath("//*[contains(@label,'Reply chat') or contains(@label,'Balas chat')]"));
    private static final Locator REPLY_CONTAINER = new Locator("REPLY_CONTAINER", AppiumBy.xpath("//*[XCUIElementTypeOther[XCUIElementTypeOther[XCUIElementTypeImage[contains(@name,'tds_ic_reply')]]]]"));
    private static final Locator CHAT_ROOM_THREE_DOT_ELIPSIS = new Locator("CHAT_ROOM_THREE_DOT_ELIPSIS", AppiumBy.accessibilityId("tds_ic_more_vertical"));
    private static final Locator CHAT_ROOM_SEE_HELP_CENTER_BTN = new Locator("CHAT_ROOM_SEE_HELP_CENTER_BTN", AppiumBy.xpath("//*[contains(@label,'See help articles') or contains(@label,'Lihat artikel bantuan')]"));
    private static final Locator CLOSE_BTM_SHEET_THREE_ELIPSIS = new Locator("CLOSE_BTM_SHEET_THREE_ELIPSIS", AppiumBy.accessibilityId("ic cancel grey"));
    private static final Locator REFUND_NOTIFICATION = new Locator("REFUND_NOTIFICATION", AppiumBy.xpath("//*[contains(@name,'inbox_notification_title') and (contains(@label,'Refund') or contains(@label,'refund'))]"));

    public InboxPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void openRefundNotification() {
        driverTools.click(REFUND_NOTIFICATION);
    }

    @Override
    public void verifyOnInboxListPage(){} // FIXME: Implementation pending
    @Override
    public void verifyOnInboxDetailPage(){} // FIXME: Implementation pending
    @Override
    public void clickInbox(String deeplinkCondition){} // FIXME: Implementation pending
    @Override
    public void verifyInboxSubtitle(){} // FIXME: Implementation pending
    @Override
    public void verifyDeeplinkRedirect(){} // FIXME: Implementation pending

    @Override
    public boolean verifyWeAreOnInboxPage(){
        driverTools.waitForElementToAppear(NOTIFICATION_TAB).orElseThrow();

        boolean isNotificationTabDisplayed = driverTools.isElementDisplayed(NOTIFICATION_TAB);
        logger.log("is notification tab is displayed : " + isNotificationTabDisplayed);

        boolean isChatTabDisplayed = driverTools.isElementDisplayed(CHAT_TAB);
        logger.log("is chat tab is displayed : " + isChatTabDisplayed);

        boolean isInboxAreaDisplayed = driverTools.isElementDisplayed(INBOX_NOTIFICATION_AREA);
        logger.log("is inbox area is displayed : " + isInboxAreaDisplayed);

        return isNotificationTabDisplayed && isChatTabDisplayed && isInboxAreaDisplayed;
    }

    @Override
    public boolean verifyInboxList(){
        boolean inboxList = true;
        for (int i=0;i<3;i++ ){
            List<WebElement> inbox = driverTools.elements(INBOX_NOTIFICATION_LIST).orElseThrow();
            logger.log("List contains (" + i + ") : " + inbox.size());
            inboxList = inboxList & inbox.size()>0;
            driverTools.swipeUp(i+1);
        }
        return inboxList;
    }

    @Override
    public boolean checkInboxListHeaderAndNavigationBar(){
        boolean isNavigationBarDisplayed = driverTools.isElementDisplayed(BACK_BTN);
        logger.log("is navigation bar is displayed : " + isNavigationBarDisplayed);

        boolean isInboxLabelDisplayed = driverTools.isElementDisplayed(INBOX_LABEL);
        logger.log("is inbox label is displayed : " + isInboxLabelDisplayed);

        return isNavigationBarDisplayed && isInboxLabelDisplayed;
    }
    
    @Override
    public void openInboxChatSection() {
        driverTools.waitForElementToAppear(CHAT_TAB,5);
        driverTools.click(CHAT_TAB);
    }

    @Override
    public void clickLegalBanner() {
        //staging is taking very long to open
        driverTools.waitForElementToAppear(LEGAL_BANNER,20).orElseThrow();
        driverTools.clickSidePartOfElement(LEGAL_BANNER,SideOfElement.btmmiddle);
    }

    @Override
    public boolean verifyLegalBannerAppear() {
        boolean isLegalBannerTitleVisible = driverTools.waitForElementToAppear(LEGAL_BANNER,2).isPresent();
        return isLegalBannerTitleVisible;
    }
    @Override
    public boolean verifyLegalBannerBottomSheetAppear() {
        Assertion.assertThat("Verify personal data title visible", driverTools.isElementDisplayed(ABOUT_PERSONAL_DATA_POPUP_TITLE), is(true));
        Assertion.assertThat("Verify personal data detail is visible", driverTools.isElementDisplayed(ABOUT_PERSONAL_DATA_CONTENT), is(true));
        return true;
    }

    private Locator getSummaryOrderLocatorByTitle(String title){
        return new Locator("SUMMARY_ORDER", AppiumBy.xpath("//XCUIElementTypeCell[XCUIElementTypeStaticText[contains(@label,'"+title+"')]]"));
    }

    @Override
    public void openSummaryOrderByTitle(String title) {
        Locator summaryOrderLocator = getSummaryOrderLocatorByTitle(title);
        driverTools.waitForElementToAppear(LEGAL_BANNER,5); 
        driverTools.swipeToElement(summaryOrderLocator,Swipe.UP,5);
        driverTools.click(summaryOrderLocator);
    }


    private Locator getOrderIDLocator(String orderID){
        return new Locator("ORDER_ID_LOCATOR", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Order ID "+orderID+"')]"));
    }

    private Locator getIternaryLocator(String iternaryNumber){
        return new Locator("ITERNARY_LOCATOR", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Itinerary ID "+iternaryNumber+"')]"));
    }

    private Locator getSpecialRequestLocator(String specialRequest){
        return new Locator("SPECIAL_REQUEST_LOCATOR", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'"+specialRequest+"')]"));
    }

    private WebElement getCheckInCheckOutElement(String checkInDate,String checkOutDate,String timeZone,String lang){
        logger.log("original checkInDate " + checkInDate);
        logger.log("original checkOutDate " + checkOutDate);
        String startTime = "00:00:01";
        String parsedCheckInDate = CoreFunctions.dateTimeToLocaleTimeZone(checkInDate+"T"+startTime, lang, timeZone,"EEE, dd MMM yyyy");
        String parsedCheckOutDate = CoreFunctions.dateTimeToLocaleTimeZone(checkOutDate+"T"+startTime, lang, timeZone,"EEE, dd MMM yyyy");
        logger.log("expected checkInDate -> " + parsedCheckInDate);
        logger.log("expected checkOutDate -> " + parsedCheckOutDate);
        Locator checkInCheckOutLocator = new Locator("CHECKIN_CHECKOUT_DATE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'"+parsedCheckInDate +" - "+ parsedCheckOutDate+"')]"));
        return driverTools.lastElement(checkInCheckOutLocator).orElseThrow();
    }

    @Override
    public boolean verifyHotelOrderSummaryData(HotelDataSummary data) {
        Boolean isOrderIdVisible = driverTools.isElementDisplayed(getOrderIDLocator(data.orderId()));
        logger.log("is order ID visible -> " + isOrderIdVisible);
        Boolean isIternaryNumberVsible = driverTools.isElementDisplayed(getIternaryLocator(data.iternaryNumber()));
        logger.log("is itenary Number visible -> " + isIternaryNumberVsible);
        Boolean isSpecialRequestVisible = driverTools.isElementDisplayed(getSpecialRequestLocator(data.specialRequest()));
        logger.log("is itenary Number visible -> " + isIternaryNumberVsible);
        Boolean isCheckInCheckOutDateVisible = driverTools.isElementDisplayed(getCheckInCheckOutElement(data.checkInDate(),data.checkOutDate(),data.timeZone(),data.appLang()));
        return isOrderIdVisible&&isIternaryNumberVsible&&isSpecialRequestVisible&&isCheckInCheckOutDateVisible;
    }
    @Override
    public boolean verifyUnreadCount(Map<String, Integer> unreadData) {
        int chatUnreadCount = unreadData.get("chatTotalUnreadCount");
        int inboxTotalUnreadCount = unreadData.get("inboxTotalUnreadCount");
        String expectedChatUnreadCount = chatUnreadCount > 99 ? "99+" : String.valueOf(chatUnreadCount);
        String expectedNotificationUnreadCount = inboxTotalUnreadCount > 99 ? "99+" : String.valueOf(inboxTotalUnreadCount);
        
        String actualNotificationUnreadCount = driverTools.getText(UNREAD_COUNT_NOTIFICATION).orElseThrow();
        String actualChatUnreadCount = driverTools.getText(UNREAD_COUNT_CHAT).orElseThrow();

        Assertion.assertThat("Verify notification unread count", actualNotificationUnreadCount, equalTo(expectedNotificationUnreadCount));
        Assertion.assertThat("Verify chat unread count", actualChatUnreadCount, equalTo(expectedChatUnreadCount));
        return true;
    }
    
    @Override
    public boolean verifyUnreadCountDecreased(Map<String, Integer> unreadData1, Map<String,Integer> unreadData) {
        Integer inboxTotalUnreadCount = unreadData1.get("inboxTotalUnreadCount");
        String expectedNotificationUnreadCount = inboxTotalUnreadCount > 99 ? "99+" : String.valueOf(inboxTotalUnreadCount);
        
        String actualNotificationUnreadCount = driverTools.getText(UNREAD_COUNT_NOTIFICATION).orElseThrow();
        Assertion.assertThat("Verify notification unread count decreased", actualNotificationUnreadCount, equalTo(expectedNotificationUnreadCount));
        return inboxTotalUnreadCount < unreadData.get("inboxTotalUnreadCount");
    }

    @Override
    public boolean checkOrderSummaryByTitle(String title) {
        Locator summaryOrderLocator = getSummaryOrderLocatorByTitle(title);
        driverTools.waitForElementToAppear(LEGAL_BANNER,5); 
        driverTools.swipeToElement(summaryOrderLocator,Swipe.UP,5);
        return driverTools.isElementDisplayed(summaryOrderLocator);
    }

    @Override
    public boolean cehckReplyMessageSent(String message){
        String lastMessageInChat = driverTools.getText(LAST_REPLIED_CHAT_MESSAGE_IN_CHAT_ROOM).orElseThrow();
        Assertion.assertThat("Verify last message is the same as in message", lastMessageInChat, equalTo(message));
        return true;
    }

    @Override
    public void sendChat(String message) {
        driverTools.sendKeys(CHAT_MESSAGE_BAR, message);
        driverTools.click(SEND_CHAT_BTN);
    }
    
    @Override
    public boolean checkMessageSent(String message) {
        String lastMessageInChat = driverTools.getText(LAST_MESSAGE_IN_CHAT_ROOM).orElseThrow();
        Assertion.assertThat("Verify last message is the same as in message", lastMessageInChat, equalTo(message));
        return true;
    }

    @Override
    public void refresh() {
        driverTools.swipeDown();
        driverTools.swipeDown();
    }

    @Override
    public boolean isNewInboxAdded(String inboxIdentifier) {
        Locator inbox = new Locator(inboxIdentifier,AppiumBy.xpath("//*[@label='"+inboxIdentifier+"']"));
        return driverTools.isElementDisplayed(inbox);
    }

    @Override
    public void clickNewInboxAndBack(String inboxIdentifier) {
        Locator inbox = new Locator(inboxIdentifier,AppiumBy.xpath("//*[@label='"+inboxIdentifier+"']"));
        driverTools.click(inbox);
        driverTools.click(SKIP_LOCATOR);
        driverTools.click(BACK_LOCATOR_NOTIFICATION);
    }

    @SneakyThrows
    @Override
    public boolean verifyScrollAndTriggerPagination() {
        driverTools.waitForElementToPresence(INBOX_NOTIFICATION_LIST).orElseThrow();
        List<WebElement> inboxesBefore = driverTools.elements(INBOX_NOTIFICATION_LIST).orElseThrow();
        int noOfInboxesBefore = inboxesBefore.size();
        logger.log("Number of inboxes loaded: "+ noOfInboxesBefore);

        for(int tries = 0; tries<=3; tries++){
            driverTools.swipeByDistance(INBOX_NOTIFICATION_AREA,Swipe.UP,0.7);
            driverTools.waitForElementToPresence(INBOX_NOTIFICATION_LIST).orElseThrow();
            List<WebElement> inboxesAfter = driverTools.elements(INBOX_NOTIFICATION_LIST).orElseThrow();
            int noOfInboxesAfter = inboxesAfter.size();
            if(noOfInboxesAfter > noOfInboxesBefore){
                logger.log("Number of inboxes loaded: "+ noOfInboxesAfter);
                return noOfInboxesAfter > 10;
            }
            Thread.sleep(300);
        }
        return false;
    }

    @Override
    public boolean replyChatInChatRoom(int pressDuration,String message) {
        driverTools.longPress(LAST_MESSAGE_IN_CHAT_ROOM, pressDuration);
        driverTools.waitForElementToAppear(REPLY_CHAT_BTN);
        driverTools.click(REPLY_CHAT_BTN);
        boolean isReplyContainerAppear = driverTools.isElementDisplayed(REPLY_CONTAINER);
        sendChat(message);
        return isReplyContainerAppear;
    }

    @Override
    public boolean isReplyChatOptionAvailable(int duration) {
        final Locator TRACKER_LOG = new Locator("TRACKER_LOG", AppiumBy.name("Tracker Log"));
        final Locator CLOSE_TRACKER_LOG = new Locator("CLOSE_TRACKER_LOG", AppiumBy.name("tds ic back"));
        driverTools.longPress(LAST_MESSAGE_IN_CHAT_ROOM, duration);
        if(driverTools.waitForElementToPresence(TRACKER_LOG).isPresent()) {
            driverTools.click(driverTools.element(CLOSE_TRACKER_LOG).orElseThrow());
        }
        return driverTools.waitForElementToAppear(REPLY_CHAT_BTN).isPresent();
    }

    @Override
    public void clickThreeDotInChatRoom() {
        driverTools.click(CHAT_ROOM_THREE_DOT_ELIPSIS);
    }

    @Override
    public boolean openHelpCenterFromChatRoom() {
        HelpCenterPage<T> helpCenterPage = HelpCenterPage.getInstance(session);
        this.clickThreeDotInChatRoom();
        driverTools.click(CHAT_ROOM_SEE_HELP_CENTER_BTN);
        boolean isHelpCenterPageOpen = helpCenterPage.isHelpCenterOpen();
        return isHelpCenterPageOpen;
    }

    @Override
    public boolean closeThreeElipsisBtmSheet() {
        clickThreeDotInChatRoom();
        boolean isBtmSheetAppear = driverTools.isElementDisplayed(CHAT_ROOM_SEE_HELP_CENTER_BTN);
        Assertion.assertThat("Verify btm sheet is open", isBtmSheetAppear, is(true));
        driverTools.click(CLOSE_BTM_SHEET_THREE_ELIPSIS);
        boolean isBtmSheetDissaper = driverTools.waitForElementToDisappear(CHAT_ROOM_SEE_HELP_CENTER_BTN);
        return isBtmSheetDissaper;
    }

    @Override
    public boolean isChatRoomOpen() {
        boolean isChatBarPresent = driverTools.isElementDisplayed(CHAT_MESSAGE_BAR);
        Assertion.assertThat("Verify chat message bar present", isChatBarPresent, is(true));

        boolean isLastMessagePresent = driverTools.isElementDisplayed(LAST_MESSAGE_IN_CHAT_ROOM);
        Assertion.assertThat("Verify message present in chat room", isLastMessagePresent, is(true));
        return true;
    }

    @Override
    public boolean checkBackButton() {
        driverTools.click(BACK_BTN);
        HomePage<T> homePage = HomePage.getInstance(this.session);
        Assertion.assertThat("Is back to homepage", homePage.isOnHomePage(), is(true));
        return true;
    }
}
