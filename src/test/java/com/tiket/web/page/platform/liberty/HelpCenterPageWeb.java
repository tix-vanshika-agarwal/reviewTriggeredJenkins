package com.tiket.web.page.platform.liberty;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.api.utilities.MemberInboxApi;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import com.tiket.api.utilities.InboxApi;
import java.util.*;
import org.openqa.selenium.WebElement;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.notNullValue;

public abstract class HelpCenterPageWeb<T extends WebDriver> extends BasePage<T> {

    public HelpCenterPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HelpCenterPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HelpCenterPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new HelpCenterPageMWeb<>(session);
            }
            default ->
                throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator OTHER_INFO_LINK = new Locator("OTHER_INFO_LINK", By.xpath("//*[contains(@class,'styles_infopages_entry_item')]//a"));
    private static final Locator OTHER_INFO_CONTENT = new Locator("OTHER_INFO_CONTENT", By.xpath("//div[@data-testid='articleContentContainer']"));
    private static final Locator LIKE = new Locator("LIKE", By.xpath("//*[@data-testid='btnDesktopPositiveReview']"));
    private static final Locator DISLIKE = new Locator("DISLIKE", By.xpath("//*[@data-testid='btnDesktopNegativeReview']"));
    private static final Locator INBOX_POPUP = new Locator("INBOX_POPUP", By.xpath("//button[contains(@class, 'close')]"));
    private static final Locator SKIP_COACHMARK = new Locator("SKIP_COACHMARK", By.xpath("//*[contains(@class,'Coachmark_coachmark_skip')]"));
    private static final Locator CHAT_ELEMENT = new Locator("CHAT_ELEMENT", By.xpath("//*[contains(text(), 'Chat')]"));
    public static final Locator CHAT_PLATFORM_ENTRY_POINT = new Locator("CHAT_PLATFORM_ENTRY_POINT", By.xpath("//p[contains(@class,'styles_side_panel')][contains(text(), 'Chat')]"));
    private static final Locator CHAT_PLATFORM_ELEMENTS = new Locator("CHAT_PLATFORM_ELEMENTS", By.xpath("//div[contains(@class, 'chat_list_item')]"));
    private static final Locator READ_MORE_BUTTON = new Locator("READ_MORE_BUTTON", By.xpath("//button[contains(@class, 'action_button')]//*[contains(text(), 'Read') or contains(text(), 'Baca')]"));
    private static final Locator POP_UP_PERSONAL_POLICY = new Locator("POP_UP_PERSONAL_POLICY", By.xpath("//section[contains(@class, 'modal_popup')]"));
    private static final Locator REPORT_LIST = new Locator("REPORT_LIST", By.xpath("//span[contains(@class, 'Button_truncation') and (normalize-space()='Lihat semua' or normalize-space()='See all reports')]"));
    private static final Locator REPORT_LIST_PAGE_ONGOING = new Locator("REPORT_LIST_PAGE_ONGOING", By.xpath("//section[contains(@class,'tab_container')]//button//span[contains(@class,'Text_variant_activity')]"));
    private static final Locator REPORT_LIST_DONE = new Locator("REPORT_LIST_DONE", By.xpath("//section[contains(@class,'tab_container')]//button//span[contains(@class,'Text_variant') and (text()='Done' or text()='Selesai')]"));
    private static final Locator REPORT_LIST_PAGE_DONE = new Locator("REPORT_LIST_PAGE_DONE", By.xpath("//h3[text()='No report has been resolved yet' or text()='Saat ini, belum ada pengaduan yang sudah diselesaikan']"));
    protected static final Locator REPORT_LIST_BACK_BUTTON = new Locator("REPORT_LIST_BACK_BUTTON", By.xpath("//p[contains(@class, 'Text_text') and contains(@class, 'Text_variant_activity') and contains(@class, 'Text_weight_bold')]"));
    private static final Locator HELP_CENTER_IS_OPENED = new Locator("HELP_CENTER_IS_OPENED", By.xpath("//div[contains(@class,'HelpCenterBanner')]"));
    private static final Locator CHAT_PLATFORM_WINDOW_NAME = new Locator("CHAT_PLATFORM_WINDOW_NAME", By.xpath("//div[contains(@class, 'chat_name')]"));
    private static final Locator CHAT_PLATFORM_WINDOW_ELLIPSIS = new Locator("CHAT_PLATFORM_WINDOW_ELLIPSIS", By.xpath("//div[contains(@class, 'action_more_item')]"));
    private static final Locator CHAT_PLATFORM_WINDOW_INPUT_TEXT = new Locator("CHAT_PLATFORM_WINDOW_INPUT_TEXT", By.xpath("//div[contains(@class,'sendbird-message-input-text-field') and @role='textbox']"));
    private static final Locator CHAT_FREEZE_CONTAINER = new Locator("CHAT_FREEZE_CONTAINER", By.xpath("//div[contains(@class, 'chat_frezee_container')]"));
    private static final Locator CHAT_FROM_PARTNER = new Locator("CHAT_FROM_PARTNER", By.xpath("//div[contains(@class, 'message_incoming')]"));
    private static final Locator INBOX_PAGE_IS_OPENED = new Locator("INBOX_PAGE_IS_OPENED", By.xpath("//h1[text()='Inbox']"));
    private static final Locator INBOX_UNREAD_BADGE = new Locator("INBOX_UNREAD_BADGE", By.xpath("//span[contains(@class,'Notif-module__notif')]"));
    private static final Locator INBOX_REFUND_TYPE = new Locator("INBOX_REFUND_TYPE", By.xpath("//div[contains(@class, 'styles_inbox_title')]//*[contains(text(),'refund')]"));
    private static final Locator CONTEXTUAL_BUTTON = new Locator("CONTEXTUAL_BUTTON", By.xpath("//button[contains(@class, 'OrderCard_action_button')]//span"));
    private static final Locator CONTINUE_REFUND_BUTTON = new Locator("CONTINUE_REFUND_BUTTON", By.xpath("//button[@data-testid='nudge-button-0']"));
    private static final Locator REFUND_PAGE_TITLE = new Locator("REFUND_PAGE_TITLE", By.xpath("//div[contains(@class, 'Card_card_container') and contains(@class, 'styles_card_banner')]"));
    private static final Locator CHANNEL_MANAGEMENT_CONTAINER = new Locator("CHANNEL_MANAGEMENT_CONTAINER", By.xpath("//div[@id='helpCenterRevampContactSection']"));
    private static final Locator POPULAR_ARTICLE_LIST = new Locator("POPULAR_ARTICLE_LIST", By.xpath("//span[contains(@class, 'FaqSection_article_title')]"));
    private static final Locator ORDERS_LIST = new Locator("ORDERS_LIST", By.xpath("//div[contains(@class, 'OrderCard_order_card')]"));

    private static Locator getPartnerMessageId(String chatId){
        return new Locator("PARTNER_MESSAGE_ID", By.xpath(String.format("//div[@data-sb-message-id='%s']", chatId)));
    }

    public List<String> getOtherInfoTitleList() {
        driverTools.waitForElementToPresence(POPULAR_ARTICLE_LIST);
        List<String> otherInfoTitles = driverTools.getTexts(POPULAR_ARTICLE_LIST);
        return otherInfoTitles;
    }

    public boolean verifyRedirectionOfInfoPage() {
        driverTools.click(POPULAR_ARTICLE_LIST);
        Assertion.assertThat("Content exist", driverTools.waitForElementToAppear(OTHER_INFO_CONTENT, 3).isPresent(), is(true));
        String currentUrl = driverTools.getCurrentURL();
        Assertion.assertThat("Verify redirection of info page", currentUrl, notNullValue());
        return true;
    }

    public void clickLikeArticle() {
        driverTools.scrollIntoWebView(LIKE);
        driverTools.click(LIKE);
    }

    public void clickDislikeArticle() {
        driverTools.waitForElementToAppear(DISLIKE);
        driverTools.click(DISLIKE);
        driverTools.dummyWait(3);
    }

    public abstract void clickLogin();

    public boolean isLoggedInCondition() {
        return driverTools.waitForElementToPresence(CHAT_ELEMENT).isPresent();
    }

    public abstract void clickInboxFirstMessage();

    public abstract void clickReportListBackButton();

    public void postNewInbox(String accountId) {
        MemberInboxApi.Inputs inboxApiInput = new MemberInboxApi.Inputs(accountId);
        MemberInboxApi inboxApi = new MemberInboxApi(inboxApiInput);
        inboxApi.hitApi(apiTools);
    }

    public boolean verifyPopup() {
        return driverTools.waitForElementToPresence(INBOX_POPUP).isPresent();
    }

    public boolean redirectionToHelpCenterPage(String url) {
        String currentUrl = driverTools.getCurrentURL();
        return currentUrl.toLowerCase().contains(url.toLowerCase());
    }

    public void postNewVerticalInbox(long accountId, String subtitle) {
        InboxApi.Inputs inboxApiInput = new InboxApi.Inputs(accountId, subtitle);
        InboxApi inboxApi = new InboxApi(inboxApiInput);
        inboxApi.hitApi(apiTools);
    }

    public void clickSkipCoachmark() {
        try {
            var button = driverTools.waitForElementToAppear(SKIP_COACHMARK, 5).orElseThrow();
            driverTools.click(button);
        } catch (Exception e) {
            logger.log("Skip coachmark not found or not clickable");
        }
    }

    public abstract boolean isChatPlatformExist();

    public void clickChatPlatform() {
        driverTools.waitForElementToAppear(CHAT_PLATFORM_ENTRY_POINT);
        driverTools.click(CHAT_PLATFORM_ENTRY_POINT);
        driverTools.dummyWait(5);
    }

    public boolean verifyActiveChatPlatform(){
        List<WebElement> elements = driverTools.elements(CHAT_PLATFORM_ELEMENTS).get();
        if (!elements.isEmpty()){
            logger.log("Total chat platform elements: " + elements.size());
        }
        return !elements.isEmpty();
    }

    public boolean verifyLegalBannerChatPlatform(){
        driverTools.waitForElementToAppear(READ_MORE_BUTTON);
        driverTools.click(READ_MORE_BUTTON);
        return driverTools.waitForElementToAppear(POP_UP_PERSONAL_POLICY).isPresent();
    }

    public void clickReportList() {
        driverTools.waitForElementToAppear(REPORT_LIST);
        driverTools.click(REPORT_LIST);
    }

    public boolean isReportListOpened() {
        driverTools.waitForElementToAppear(REPORT_LIST_PAGE_ONGOING);
        return driverTools.isElementDisplayed(REPORT_LIST_PAGE_ONGOING);
    }

    public void clickReportListDone() {
        driverTools.waitForElementToAppear(REPORT_LIST_DONE);
        driverTools.click(REPORT_LIST_DONE);
    }

    public boolean isReportListDoneOpened() {
        driverTools.waitForElementToAppear(REPORT_LIST_PAGE_DONE);
        return driverTools.isElementDisplayed(REPORT_LIST_PAGE_DONE);
    }

    public boolean isHelpCenterOpened() {
        driverTools.waitForElementToAppear(HELP_CENTER_IS_OPENED);
        return driverTools.isElementDisplayed(HELP_CENTER_IS_OPENED);
    }

    public void clickChatPlatformActiveChat() {
        driverTools.waitForElementsToAppear(CHAT_PLATFORM_ELEMENTS);
        driverTools.click(CHAT_PLATFORM_ELEMENTS);
    }

    public boolean verifyChatPlatformWindowName() {
        return driverTools.waitForElementsToAppear(CHAT_PLATFORM_WINDOW_NAME).isPresent();
    }

    public boolean verifyPlatformWindowEllipsis() {
        return driverTools.waitForElementsToAppear(CHAT_PLATFORM_WINDOW_ELLIPSIS).isPresent();
    }

    public boolean verifyPlatformWindowTextField() {
        driverTools.waitForElementToPresence(CHAT_PLATFORM_WINDOW_INPUT_TEXT);
        return driverTools.waitForElementsToAppear(CHAT_PLATFORM_WINDOW_INPUT_TEXT).isPresent();
    }

    public void checkInsertTextField(String input){
        driverTools.waitForElementToBeClickable(CHAT_PLATFORM_WINDOW_INPUT_TEXT, 15);
        driverTools.sendKeys(CHAT_PLATFORM_WINDOW_INPUT_TEXT, input);
    }

    public boolean verifyChatFreezeContainer() {
        driverTools.waitForElementToAppear(CHAT_FREEZE_CONTAINER);
        return driverTools.isElementDisplayed(CHAT_FREEZE_CONTAINER);
    }

    public boolean verifyInboxPageIsOpened() {
        driverTools.waitForElementToAppear(INBOX_PAGE_IS_OPENED);
        return driverTools.isElementDisplayed(INBOX_PAGE_IS_OPENED);
    }
  
    public abstract boolean verifyInboxPageCanBeScrolled();
  
    public boolean verifyUnreadInboxAndChat(){
       return driverTools.waitForElementToAppear(INBOX_UNREAD_BADGE).isPresent();
    }

    public boolean verifyChatFromPartner(String messageId) {
        boolean chatPartner = driverTools.waitForElementToAppear(getPartnerMessageId(messageId)).isPresent();
        if(chatPartner){
            logger.log("Partner chat exist corresponding message id: " + messageId);
        }else{
            logger.log("Partner chat not exist with corresponding message id");
        }
        return chatPartner;
    }

    public int verifyRefundInbox() {
        driverTools.waitForElementToAppear(INBOX_REFUND_TYPE);
        List<WebElement> inboxRefundList = driverTools.elements(INBOX_REFUND_TYPE).get();
        if(!inboxRefundList.isEmpty()){
            logger.log("Got refund inbox: " + inboxRefundList.size());
        }else{
            logger.log("Empty refund inbox");
            Assertion.skip("Skipping test case");
        }
        return inboxRefundList.size();
    }

    public String checkRefundInboxRedirection(){
        try {
            driverTools.waitForElementToAppear(INBOX_REFUND_TYPE);
            driverTools.click(INBOX_REFUND_TYPE);
        }catch (NoSuchElementException e){
            logger.log("Inbox refund not found");
            Assertion.skip("Skipping test case");
            return "";
        }
        return driverTools.getCurrentURL();
    }

    public int checkContextualButton() {
        driverTools.waitForElementToAppear(CONTEXTUAL_BUTTON);
        List<WebElement> elements = driverTools.elements(CONTEXTUAL_BUTTON).get();
        if (!elements.isEmpty()) {
            logger.log("Number of contextual button: " + elements.size());
        } else {
            logger.log("Contextual button not showing");
            Assertion.skip("Skipping test case");
        }
        return elements.size();
    }

    public boolean isInActiveChatConversation() {
        return verifyChatPlatformWindowName() && verifyPlatformWindowTextField();
    }

    public boolean verifyChannelManagement(){
        driverTools.waitForElementToAppear(CHANNEL_MANAGEMENT_CONTAINER);
        driverTools.scrollIntoWebView(CHANNEL_MANAGEMENT_CONTAINER);
        return driverTools.isElementDisplayed(CHANNEL_MANAGEMENT_CONTAINER);
    }

    public boolean verifyNewOrderSectionPresent() {
        logger.log("Checking if recent orders are present");
        boolean res = driverTools.isElementDisplayed(ORDERS_LIST);
        return res;
    }

    public String selectRandomArticle(){
        driverTools.dummyWait(3);
        Optional<List<WebElement>> questionElementsList = driverTools.elements(POPULAR_ARTICLE_LIST);
        if (questionElementsList.isPresent() && !questionElementsList.get().isEmpty()) {
            List<WebElement> elements = questionElementsList.get();
            int randomIndex = CoreFunctions.generateRandomInt(0, elements.size() - 1);
            WebElement randomQuestionElement = elements.get(randomIndex);
            String selectedQuestionText = randomQuestionElement.getText();
            driverTools.clickWithJavascriptExecutor(randomQuestionElement);
            logger.log("Selected question: " + selectedQuestionText);
            return selectedQuestionText;
        } else {
            logger.log("No popular articles found.");
            return null;
        }
    }
}
