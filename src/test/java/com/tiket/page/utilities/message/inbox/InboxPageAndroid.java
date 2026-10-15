package com.tiket.page.utilities.message.inbox;

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

import java.util.*;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class InboxPageAndroid <T extends WebDriver> extends InboxPage<T> {

    private static final Locator SKIP_LOCATOR = new Locator("SKIP_BUTTON", AppiumBy.xpath("//*[contains(@text,'Skip') or contains(@text,'Lewati')]"));
    private static final Locator BACK_LOCATOR_NOTIFICATION = new Locator("BACK_BUTTON", AppiumBy.xpath("//*[contains(@text,'back')]"));
    private static final Locator BACK_BTN = new Locator("BACK_BTN", AppiumBy.id("tds_icon_navigation_toolbar"));
    private static final Locator NAVIGATION_BAR = new Locator("NAVIGATION_BAR", AppiumBy.id("tds_main_item_group"));
    private static final Locator INBOX_LABEL = new Locator("INBOX_LABEL", AppiumBy.xpath("//android.widget.TextView[contains(@resource-id,'tds_header_navigation_appbar') and (contains(@text,'Inbox') or contains(@text,'Kotak Masuk'))]"));
    private static final Locator NOTIFICATION_TAB = new Locator("NOTIFICATION_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and (contains(@text,'Notifikasi') or contains(@text,'Notification') )]"));
    private static final Locator CHAT_TAB = new Locator("CHAT_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and contains(@text,'Chat')]"));
    private static final Locator INBOX_NOTIFICATION_AREA = new Locator("INBOX_NOTIFICATION_AREA", AppiumBy.id("view_pager"));
    private static final Locator INBOX_NOTIFICATION_LIST = new Locator("INBOX_NOTIFICATION_LIST", AppiumBy.xpath("//*[contains(@resource-id,'view_pager')]//*[contains(@resource-id,'tv_inbox_title')]"));
    private static final Locator LEGAL_BANNER = new Locator("LEGAL_BANNER", AppiumBy.id("tds_content_notification_banner"));
    private static final Locator LEGAL_BANNER_TITLE = new Locator("LEGAL_BANNER", AppiumBy.id("tds_content_notification_banner"));
    private static final Locator LEGAL_BANNER_SUBTITLE = new Locator("LEGAL_BANNER_SUBTITLE", AppiumBy.id("tds_subtitle_notification_banner"));
    private static final Locator ABOUT_PERSONAL_DATA_POPUP = new Locator("ABOUT_PERSONAL_DATA_POPUP", AppiumBy.id("dw_frame_layout"));
    private static final Locator ABOUT_PERSONAL_DATA_POPUP_TITLE = new Locator("ABOUT_PERSONAL_DATA_POPUP_TITLE", AppiumBy.id("tv_legal_detail_title"));
    private static final Locator ABOUT_PERSONAL_DATA_DETAIL = new Locator("ABOUT_PERSONAL_DATA_TITLE", AppiumBy.id("sv_legal_detail"));
    private static final Locator ORDER_SUMMARY_ORDER_ID = new Locator("ORDER_SUMMARY_ORDER_ID", AppiumBy.id("tv_order_id"));
    private static final Locator UNREAD_COUNT_NOTIFICATION = new Locator("UNREAD_COUNT_NOTIFICATION", AppiumBy.xpath("//android.widget.LinearLayout[@content-desc='Notifikasi' or @content-desc='Notification']//*[contains(@resource-id,'tds_badge_tab_layout')]//android.widget.TextView"));
    private static final Locator UNREAD_COUNT_CHAT = new Locator("UNREAD_COUNT_CHAT", AppiumBy.xpath("//android.widget.LinearLayout[@content-desc='Chat']//*[contains(@resource-id,'tds_badge_tab_layout')]//android.widget.TextView"));
    private static final Locator INBOX_TITLE = new Locator("INBOX_TITLE", AppiumBy.id("tv_inbox_title"));
    private static final Locator INBOX_SUBTITLE = new Locator("INBOX_SUBTITLE", AppiumBy.id("tv_inbox_subtitle"));
    private static final Locator INBOX_TIME= new Locator("INBOX_TIME", AppiumBy.id("tv_inbox_time"));
    private static final Locator CHAT_MESSAGE_BAR = new Locator("CHAT_MESSAGE_BAR", AppiumBy.id("et_message"));
    private static final Locator SEND_CHAT_BTN = new Locator("SEND_CHAT_BTN", AppiumBy.id("iv_send"));
    private static final Locator CHAT_PHONE_ICON = new Locator("CHAT_PHONE_ICON", AppiumBy.id("iv_call"));
    private static final Locator LAST_MESSAGE_IN_CHAT_ROOM = new Locator("LAST_MESSAGE_IN_CHAT_ROOM", AppiumBy.xpath("(//*[contains(@resource-id,'tv_message')])[last()]"));
    private static final Locator CHAT_ROOM_BOTTOM_SHEET = new Locator("CHAT_ROOM_BOTTOM_SHEET", AppiumBy.id("crf_bottom_sheet"));
    private static final Locator REPLY_CHAT_BTN = new Locator("REPLY_CHAT_BTN", AppiumBy.id("item_reply_chat"));
    private static final Locator REPLY_CONTAINER = new Locator("REPLY_CONTAINER", AppiumBy.id("reply_container"));
    private static final Locator CHAT_ROOM_THREE_DOT_ELIPSIS = new Locator("CHAT_ROOM_THREE_DOT_ELIPSIS", AppiumBy.id("iv_more"));
    private static final Locator CHAT_ROOM_SEE_HELP_CENTER_BTN = new Locator("CHAT_ROOM_SEE_HELP_CENTER_BTN", AppiumBy.id("tds_list_parent"));
    private static final Locator CLOSE_BTM_SHEET_THREE_ELIPSIS = new Locator("CLOSE_BTM_SHEET_THREE_ELIPSIS", AppiumBy.id("iv_close"));
    private static final Locator REFUND_NOTIFICATION = new Locator("REFUND_NOTIFICATION", AppiumBy.xpath("//*[contains(@resource-id,'tv_inbox_title') and (contains(@text,'Refund') or contains(@text,'refund'))]"));

    public InboxPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void openRefundNotification() {
        driverTools.swipeToElement(REFUND_NOTIFICATION, Swipe.UP, 5);
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
        driverTools.waitForElementToAppear(INBOX_LABEL).orElseThrow();

        boolean isInboxLabelDisplayed = driverTools.isElementDisplayed(INBOX_LABEL);
        logger.log("is inbox label is displayed : " + isInboxLabelDisplayed);

        boolean isNotificationTabDisplayed = driverTools.isElementDisplayed(NOTIFICATION_TAB);
        logger.log("is notification tab is displayed : " + isNotificationTabDisplayed);

        boolean isChatTabDisplayed = driverTools.isElementDisplayed(CHAT_TAB);
        logger.log("is chat tab is displayed : " + isChatTabDisplayed);

        boolean isInboxAreaDisplayed = driverTools.isElementDisplayed(INBOX_NOTIFICATION_AREA);
        logger.log("is inbox area is displayed : " + isInboxAreaDisplayed);

        return isInboxLabelDisplayed && isNotificationTabDisplayed && isChatTabDisplayed && isInboxAreaDisplayed;
    }

    @Override
    public boolean verifyInboxList(){
        boolean inboxList = true;
        for (int i=0;i<3;i++ ){
            List<WebElement> inbox = driverTools.elements(INBOX_NOTIFICATION_LIST).orElseThrow();
            logger.log("List contains (" + i + ") : " + inbox.size());
            inboxList = inboxList & inbox.size()>0;
            driverTools.swipeUp();
        }
        return inboxList;
    }

    @Override
    public boolean checkInboxListHeaderAndNavigationBar(){
        boolean isNavigationBarDisplayed = driverTools.isElementDisplayed(NAVIGATION_BAR);
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
        driverTools.click(LEGAL_BANNER);
    }

    @Override
    public boolean verifyLegalBannerAppear() {
        boolean isLegalBannerTitleVisible = driverTools.waitForElementToAppear(LEGAL_BANNER_TITLE,2).isPresent();
        return isLegalBannerTitleVisible;
    }

    private Locator getSummaryOrderLocatorByTitle(String title){
        return new Locator("SUMMARY_ORDER", AppiumBy.xpath("//android.widget.TextView[contains(@text,'"+title+"')]"));
    }

    @Override
    public void openSummaryOrderByTitle(String title) {
       Locator summaryOrderLocator = getSummaryOrderLocatorByTitle(title);
        driverTools.waitForElementToAppear(LEGAL_BANNER,5); 
        driverTools.swipeToElement(summaryOrderLocator,Swipe.UP,5);
        driverTools.click(summaryOrderLocator);
    }

    private Locator getOrderIDLocator(String orderID){
        return new Locator("ORDER_ID_LOCATOR", AppiumBy.xpath("//android.widget.TextView[contains(@resource-id,'tv_message') and contains(@text,'Order ID "+orderID+"')]"));
    }

    private Locator getIternaryLocator(String iternaryNumber){
        return new Locator("ITERNARY_LOCATOR", AppiumBy.xpath("//android.widget.TextView[contains(@text,'Itinerary ID "+iternaryNumber+"')]"));
    }

    private Locator getSpecialRequestLocator(String specialRequest){
        return new Locator("SPECIAL_REQUEST_LOCATOR", AppiumBy.xpath("//android.widget.TextView[contains(@text,'"+specialRequest+"')]"));
    }


    private WebElement getCheckInCheckOutElement(String checkInDate,String checkOutDate,String timeZone,String lang){
        logger.log("original checkInDate " + checkInDate);
        logger.log("original checkOutDate " + checkOutDate);
        String startTime = "00:00:01";
        String parsedCheckInDate = CoreFunctions.dateTimeToLocaleTimeZone(checkInDate+"T"+startTime, lang, timeZone,"EEE, dd MMM yyyy");
        String parsedCheckOutDate = CoreFunctions.dateTimeToLocaleTimeZone(checkOutDate+"T"+startTime, lang, timeZone,"EEE, dd MMM yyyy");
        logger.log("expected checkInDate -> " + parsedCheckInDate);
        logger.log("expected checkOutDate -> " + parsedCheckOutDate);
        Locator checkInCheckOutLocator = new Locator("CHECKIN_CHECKOUT_DATE", AppiumBy.xpath("//android.widget.TextView[contains(@text,'"+parsedCheckInDate +" - "+ parsedCheckOutDate+"')]"));
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

    public boolean verifyLegalBannerBottomSheetAppear() {
        Assertion.assertThat("Verify personal data container visible", driverTools.isElementDisplayed(ABOUT_PERSONAL_DATA_POPUP), is(true));
        Assertion.assertThat("Verify personal data title visible", driverTools.isElementDisplayed(ABOUT_PERSONAL_DATA_POPUP_TITLE), is(true));
        Assertion.assertThat("Verify personal data detail is visible", driverTools.isElementDisplayed(ABOUT_PERSONAL_DATA_DETAIL), is(true));
        return true;
    }
    @Override
    public boolean verifyUnreadCount(Map<String, Integer> unreadData) {
        String expectedChatUnreadCount = String.valueOf(unreadData.get("chatTotalUnreadCount"));
        String expectedNotificationUnreadCount = String.valueOf(unreadData.get("inboxTotalUnreadCount"));
        
        String actualNotificationUnreadCount = driverTools.getText(UNREAD_COUNT_NOTIFICATION).orElseThrow();
        Assertion.assertThat("Verify notification unread count", actualNotificationUnreadCount, equalTo(expectedNotificationUnreadCount));

        if(unreadData.get("chatTotalUnreadCount") != 0){
            String actualChatUnreadCount = driverTools.getText(UNREAD_COUNT_CHAT).orElseThrow();
            Assertion.assertThat("Verify chat unread count", actualChatUnreadCount, equalTo(expectedChatUnreadCount));
        }
        return true;
    }

    @Override
    public boolean verifyUnreadCountDecreased(Map<String, Integer> unreadData1, Map<String,Integer> unreadData) {
        Integer expectedNotificationUnreadCount = unreadData1.get("inboxTotalUnreadCount");
        Integer actualNotificationUnreadCount = Integer.valueOf(driverTools.getText(UNREAD_COUNT_NOTIFICATION).orElseThrow());
        Assertion.assertThat("Verify notification unread count decreased", actualNotificationUnreadCount, is(expectedNotificationUnreadCount));
        return expectedNotificationUnreadCount < unreadData.get("inboxTotalUnreadCount");
    }

    @Override
    public void refresh() {
        driverTools.swipeDown();
        driverTools.swipeDown();
    }

    @Override
    public boolean isNewInboxAdded(String inboxIdentifier) {
        Locator inbox = new Locator(inboxIdentifier,AppiumBy.xpath("//*[@text='"+inboxIdentifier+"']"));
        return driverTools.isElementDisplayed(inbox);
    }

    @Override
    public void clickNewInboxAndBack(String inboxIdentifier) {
        Locator inbox = new Locator(inboxIdentifier,AppiumBy.xpath("//*[@text='"+inboxIdentifier+"']"));
        driverTools.click(inbox);
        driverTools.waitForElementToAppear(SKIP_LOCATOR,5).ifPresent(driverTools::click);;
        driverTools.click(BACK_LOCATOR_NOTIFICATION);
    }

    @SneakyThrows
    @Override
    public boolean verifyScrollAndTriggerPagination() {
        driverTools.waitForElementToAppear(INBOX_NOTIFICATION_LIST).orElseThrow();

        List<String> uniqueInboxesBeforeSwipe = getNoOfUniqueInbox();

        int totalInboxBeforeSwipe = uniqueInboxesBeforeSwipe.size();
        logger.log("unique inboxes before swipe: " + totalInboxBeforeSwipe);

        for(int tries = 0; tries<=3; tries++){
            driverTools.swipeByDistance(INBOX_NOTIFICATION_AREA,Swipe.UP,0.7);
            driverTools.waitForElementToPresence(INBOX_NOTIFICATION_LIST).orElseThrow();
            List<String> uniqueInboxesAfterSwipe = getNoOfUniqueInbox();
            List<String> tempInboxBeforeSwipe = uniqueInboxesBeforeSwipe;
            tempInboxBeforeSwipe.addAll(uniqueInboxesAfterSwipe);

            Set<String> totalUniqueInboxesAfterSwipe = new HashSet<>(uniqueInboxesBeforeSwipe);

            int totalInboxAfterSwipe = totalUniqueInboxesAfterSwipe.size();
            logger.log("unique inboxes after swipe: "+totalInboxAfterSwipe);

            if(totalInboxAfterSwipe > 10){
                logger.log("Number of inboxes loaded: "+ totalInboxAfterSwipe);
                return true;
            }
            Thread.sleep(300);
        }
        return false;
    }

    private List<String> getNoOfUniqueInbox() {
        List<String> inboxSubtitleTextList = driverTools.getTexts(INBOX_SUBTITLE);
        List<String> inboxTimeTextList = driverTools.getTexts(INBOX_TIME);

        int noOfInboxesBefore = inboxSubtitleTextList.size();
        logger.log("Number of inboxes loaded on screen: " + noOfInboxesBefore);

        List<String> combinedList = IntStream.range(0, Math.min(inboxSubtitleTextList.size(), inboxTimeTextList.size()))
                .mapToObj(i -> inboxSubtitleTextList.get(i) + " " + inboxTimeTextList.get(i))
                .toList();

        Set<String> uniqueInboxes = new HashSet<>(combinedList);

        return new ArrayList<>(uniqueInboxes);
    }

    @Override
    public boolean checkOrderSummaryByTitle(String title) {
        Locator summaryOrderLocator = getSummaryOrderLocatorByTitle(title);
        driverTools.waitForElementToAppear(LEGAL_BANNER,5); 
        driverTools.swipeToElement(summaryOrderLocator,Swipe.UP,5);
        return driverTools.isElementDisplayed(summaryOrderLocator);
    }
    @Override
    public void sendChat(String message) {
        driverTools.click(CHAT_MESSAGE_BAR);
        driverTools.sendKeys(CHAT_MESSAGE_BAR, message);
        driverTools.click(SEND_CHAT_BTN);
    }
    
    @Override
    public boolean checkMessageSent(String message) {
        driverTools.waitForElementToAppear(LAST_MESSAGE_IN_CHAT_ROOM);
        String lastMessageInChat = driverTools.getText(LAST_MESSAGE_IN_CHAT_ROOM).orElseThrow();
        Assertion.assertThat("Verify last message is the same as in message", lastMessageInChat, equalTo(message));
        return true;
    }
    @Override
    public boolean replyChatInChatRoom(int pressDuration,String message) {
        driverTools.longPress(LAST_MESSAGE_IN_CHAT_ROOM, pressDuration);
        driverTools.waitForElementToAppear(CHAT_ROOM_BOTTOM_SHEET);
        driverTools.click(REPLY_CHAT_BTN);
        boolean isReplyContainerAppear = driverTools.isElementDisplayed(REPLY_CONTAINER);
        sendChat(message);
        return isReplyContainerAppear;
    }

    @Override
    public boolean isReplyChatOptionAvailable(int duration) {
        driverTools.longPress(LAST_MESSAGE_IN_CHAT_ROOM, duration);
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
        boolean isBtmSheetAppear = driverTools.isElementDisplayed(CHAT_ROOM_BOTTOM_SHEET);
        Assertion.assertThat("Verify btm sheet is open", isBtmSheetAppear, is(true));
        driverTools.click(CLOSE_BTM_SHEET_THREE_ELIPSIS);
        boolean isBtmSheetDissaper = driverTools.waitForElementToDisappear(CHAT_ROOM_BOTTOM_SHEET);
        return isBtmSheetDissaper;
    }

    @Override
    public boolean cehckReplyMessageSent(String message) {
        //element in ios is different so need to create different method to check reply message.
        return checkMessageSent(message);
    }
    
    @Override
    public boolean isChatRoomOpen() {
        driverTools.waitForElementToPresence(CHAT_MESSAGE_BAR);
        boolean isChatBarPresent = driverTools.element(CHAT_MESSAGE_BAR).isPresent();
        Assertion.assertThat("Verify chat message bar present", isChatBarPresent, is(true));

        boolean isLastMessagePresent = driverTools.element(LAST_MESSAGE_IN_CHAT_ROOM).isPresent();
        Assertion.assertThat("Verify message present in chat room", isLastMessagePresent, is(true));
        return true;
    }

    @Override
    public boolean checkBackButton() {
        driverTools.back();
        HomePage<T> homePage = HomePage.getInstance(this.session);
        Assertion.assertThat("Is back to homepage", homePage.isOnHomePage(), is(true));
        return true;
    }
}
