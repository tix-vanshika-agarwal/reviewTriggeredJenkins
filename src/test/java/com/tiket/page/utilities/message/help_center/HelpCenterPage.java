package com.tiket.page.utilities.message.help_center;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public abstract class HelpCenterPage <T extends WebDriver> extends BasePage<T> {

    private static final Locator OTHER_INFO_PAGE_LIST_SECTION = new Locator("OTHER_INFO_PAGE_LIST_SECTION", AppiumBy.xpath("//*[contains(@class,'FaqSection_article_link')]"));
    private static final Locator HELP_CENTER_HEADER_TITLE = new Locator("HELP_CENTER_HEADER_TITLE", AppiumBy.xpath("//*[contains(@id,'helpCenterRevampSearchSection')]"));
    private static final Locator HELP_CENTER_SEARCH_BAR = new Locator("HELP_CENTER_SEARCH_BAR", AppiumBy.xpath("//*[contains(@class,'search_box')]"));
    private static final Locator ARROW_UP_BUTTON = new Locator("ARROW_UP_BUTTON", AppiumBy.accessibilityId("arrow up"));
    private static final Locator LOGIN_BANNER = new Locator("LOGIN_BANNER", AppiumBy.xpath("//*[contains(@class,'Login_main_container')]//*[contains(@class,'Login_copy')]"));
    private static final Locator TRAVIS_ICON = new Locator("TRAVIS_ICON", AppiumBy.xpath("//*[@id='helpCenterRevampContactStepOne']//button"));
    private static final Locator DISLIKE_ARTICLE = new Locator("DISLIKE_POPULAR_ARTICLE", AppiumBy.xpath("//*[contains(@data-testid,'Negative')]"));
    private static final Locator LIKE_ARTICLE = new Locator("LIKE_POPULAR_ARTICLE", AppiumBy.xpath("//*[contains(@data-testid,'Positive')]"));
    private static final Locator ARTICLE_TITLE = new Locator("ARTICLE_TITLE", AppiumBy.xpath("(//*[contains(@class,'main_content')]//h1)[1]"));
    private static final Locator POPULAR_ARTICLE_PAGE_TITLE = new Locator("POPULAR_ARTICLE_TITLE", AppiumBy.xpath("(//div[contains(@class,'main_content')]//main//*[contains(@class,'text')])[1]"));
    private static final Locator CONTACT_CUSTOMER_CARE = new Locator("CONTACT_CUSTOMER_CARE", AppiumBy.xpath("//*[contains(text(),'Hubungi Customer Care') or contains(text(),'Contact Customer Care')]"));
    private static final Locator CHATBOT_IFRAME = new Locator("CHATBOT_IFRAME", AppiumBy.xpath("//iframe[@name='ymIframe']"));
    private static final Locator CHATBOT_START_NEW = new Locator("CHATBOT_START_NEW", AppiumBy.xpath("(//*[@id='chatOptions']//*[contains(@class,'option')])[2]"));
    private static final Locator CHATBOT_CHAT_BAR = new Locator("CHATBOT_CHAT_BAR", AppiumBy.xpath("//textarea[contains(@class,'StaticPlaceholderInput_text_area')]"));
    private static final Locator CHATBOT_LAST_MESSAGE = new Locator("CHATBOT_LAST_MESSAGE", AppiumBy.xpath("(//div[contains(@data-testid,'chat-bubble')])[last()]"));
    //Dynamic form locator
    private static final Locator SEND_QUESTION_INFO_SECTION = new Locator("SEND_QUESTION_INFO_SECTION", AppiumBy.xpath("//*[contains(@data-testid, 'btnCustomerCareMobile')]"));
    private static final Locator CLOSE_TRAVIS_INFO_BANNER = new Locator("CLOSE_TRAVIS_INFO_BANNER", AppiumBy.xpath("//div[contains(@class,'close')]"));
    private static final Locator USER_FIRST_NAME = new Locator("USER_FIRST_NAME", AppiumBy.xpath("//*[contains(@class,'hero_title')]"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//button[@aria-label='back']"));
    public static final Locator SKIP_BTN = new Locator("SKIP_BTN", AppiumBy.xpath("//*[contains(text(),'Skip') or contains(text(),'Lewati') or contains(text(),'OK')]"));
    public static final Locator ORDER_STATUS_LIST = new Locator("ORDER_STATUS", AppiumBy.xpath("//div[contains(@class,'OrderCard_order_id_status_row')]"));
    public static final Locator ORDER_SLIDER_CARD_LIST = new Locator("ORDER_SLIDER_CARD_LIST", AppiumBy.xpath("//div[contains(@class,'OrderCardsListAndFaq_order_cards_container')]"));
    public static final Locator ORDER_SLIDER_CARD_TITLE = new Locator("ORDER_SLIDER_CARD_TITLE", AppiumBy.xpath("//*[contains(@class,'OrderCard_product_title')]"));
    public static final Locator SKIP_COACHMARK_BTN = new Locator("SKIP_COACHMARK_BTN", AppiumBy.xpath("//*[contains(@class,'coachmark_skip_btn')]"));
    private static final Locator SEE_ORDER_DETAIL_BUTTON = new Locator("SEE_ORDER_DETAIL_BUTTON", AppiumBy.xpath("//*[contains(@class,'dialog')]//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator BACK_POPUP_BUTTON = new Locator("BACK_POPUP_BUTTON", AppiumBy.xpath("//*[contains(@class,'dialog')]//*[contains(@class,'Button_variant_secondary')]"));
    private static final Locator CONTEXTUAL_SEE_ALL = new Locator("CONTEXTUAL_SEE_ALL", AppiumBy.xpath("//*[contains(@class,'styles_see_all')]"));
    private static final Locator ORDER_ID_CONTEXTUAL = new Locator("ORDER_ID_CONTEXTUAL", AppiumBy.xpath("//*[contains(@data-testid,'orderIdText')]"));
    private static final Locator ARTICLE_CONTEXTUAL = new Locator("ARTICLE_CONTEXTUAL", AppiumBy.xpath("//*[contains(@class,'styles_faq')]//*[contains(@data-testid,'faq')]/p[text()]"));

    public HelpCenterPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HelpCenterPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID : return new HelpCenterPageAndroid<>(session);
            case IOS : return new HelpCenterPageIOS<>(session);
            default : throw new NotImplementedException("Not implemented");
        }
    }

    public void verifyOnHelpCenterPage(){throw new UnsupportedOperationException("Unimplemented method 'verifyOnHelpCenterPage'");}; // FIXME: Implementation pending
    public boolean verifyHelpCenterOtherInformation() {
        driverTools.waitForWebview();
        driverTools.switchToWebView();

        boolean isOtherInfoPageListDisplayed = driverTools.swipeToElement(OTHER_INFO_PAGE_LIST_SECTION, Swipe.LEFT, 10).isPresent();

        Locator firstInfoPageEntryListLocator = getInfoPageEntryListLocator(1);
        WebElement firstInfoPageEntryListElement = driverTools.swipeToElement(firstInfoPageEntryListLocator, Swipe.UP, 3).get();
        String href = driverTools.getValueAttribute(firstInfoPageEntryListElement, "href");
        driverTools.click(firstInfoPageEntryListElement);

        driverTools.switchToNative();

        driverTools.waitForElementToAppear(ARROW_UP_BUTTON);

        driverTools.waitForWebview();
        driverTools.switchToWebView();
        boolean isRedirectedToCorrectPage = driverTools.getCurrentURL().contains(href);
        driverTools.switchToNative();

        logger.log("isOtherInfoPageListDisplayed : " + isOtherInfoPageListDisplayed);
        logger.log("isRedirectedToCorrectPage : " + isRedirectedToCorrectPage);
        return isOtherInfoPageListDisplayed && isRedirectedToCorrectPage;
    }

    private Locator getInfoPageEntryListLocator(int index) {
        return new Locator("INFO_PAGE_ENTRY_LIST_"+index, AppiumBy.xpath("(//*[contains(@class,'FaqSection_article_link')])["+index+"]"));
    }


    public boolean verifyChatBot(String name){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.clickWithJavascriptExecutor(TRAVIS_ICON);
        Optional<WebElement> chatBotStartNewElement = driverTools.waitForElementToAppear(CHATBOT_START_NEW,5);
        if(chatBotStartNewElement.isPresent()){
            driverTools.click(chatBotStartNewElement.orElseThrow());
        }else{
            driverTools.clickWithJavascriptExecutor(CHATBOT_CHAT_BAR);
            String lastChatText = driverTools.getText(CHATBOT_LAST_MESSAGE).orElseThrow();
            String newConversationCommand = lastChatText.toLowerCase().contains("sobat tiket")? "Mulai percakapan baru" : "Start new conversation";
            driverTools.sendKeys(CHATBOT_CHAT_BAR, newConversationCommand);
        }
        name = name.split(" ")[0]; //get first name only
        boolean isChatFromBotPresent = driverTools.waitForElementToAppear(new Locator(name,AppiumBy.xpath("//*[contains(@data-testid,'chat-bubble')]//*[contains(text(),'"+name+"')]")),10).isPresent();

        driverTools.switchToNative();
        return isChatFromBotPresent;
    }
    public abstract boolean isAllContactCustomerCareOptionPresent();

    public void clickSendQuestionFromArticle(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.swipeToElement(SEND_QUESTION_INFO_SECTION, Swipe.UP, 5);
        driverTools.waitForElementToAppear(CLOSE_TRAVIS_INFO_BANNER,2).ifPresent(driverTools::click);;
        driverTools.click(SEND_QUESTION_INFO_SECTION);
        driverTools.switchToNative();
    }

    public abstract boolean verifyReportComplaintField();

    public abstract void fillReportComplaint(String orderID);

    public abstract boolean isRequestSubmitted();

    public boolean isHelpCenterOpen() {
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(SKIP_BTN,10).ifPresent(driverTools::click);;
        driverTools.waitForElementToAppear(HELP_CENTER_SEARCH_BAR);
        boolean isOnHelpCenterPage = driverTools.waitForElementToAppear(HELP_CENTER_HEADER_TITLE).isPresent();
        driverTools.switchToNative();
        return isOnHelpCenterPage;
    }

    public void clickLoginBanner() {
        driverTools.waitForWebview();
        driverTools.switchToWebView();

        driverTools.click(LOGIN_BANNER);

        driverTools.switchToNative();
    }

    public Boolean isLoginBannerDisappeared() {
        driverTools.waitForWebview();
        driverTools.switchToWebView();

        boolean isLoginBannerDisappeared = driverTools.waitForElementToDisappear(LOGIN_BANNER);

        driverTools.switchToNative();

        return isLoginBannerDisappeared;
    }

    public Boolean isTravisIconDisplayed() {
        driverTools.waitForWebview();
        driverTools.switchToWebView();

        boolean isTravisIconDisplayed = driverTools.waitForElementToPresence(TRAVIS_ICON,20).isPresent();

        driverTools.switchToNative();

        return isTravisIconDisplayed;
    }

    private Locator getPopularArticleLocator(int index){
        return new Locator("POPULAR_ARTICLE", AppiumBy.xpath("(//*[contains(@class,'FaqSection_article')]//*[name()='svg'])["+index+"]"));
    }

    private Locator getPopularArticleLocator(String titleId,String titleEn){
        return new Locator("POPULAR_ARTICLE", AppiumBy.xpath("//*[contains(@class,'popular_question_list')]//*[contains(text(),'"+titleId+"') or contains(text(),'"+titleEn+"')]"));
    }

    public void clickPopularArticle(){
        driverTools.swipeLeft(10);
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.click(getPopularArticleLocator(1));
        driverTools.switchToNative();
    }

    public void clickPopularArticle(String titleId,String titleEn){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(CLOSE_TRAVIS_INFO_BANNER,2).ifPresent(driverTools::clickElementByCoordinate);;
        WebElement popularArticle = driverTools.swipeToElement(getPopularArticleLocator(titleId,titleEn),Swipe.UP,3).orElseThrow();
        driverTools.click(popularArticle);
        driverTools.switchToNative();
    }

    public String clickPopularArticleAndGetTitle(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        WebElement popularArticle = driverTools.swipeToElement(getPopularArticleLocator(2),Swipe.UP,3).orElseThrow();
        String popularArticleTitle = driverTools.getText(popularArticle);
        logger.log("title :" + popularArticleTitle);
        driverTools.click(popularArticle);
        driverTools.switchToNative();
        return popularArticleTitle;
    }

    public void clickDislikeBtnPopularArticle(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        WebElement dislikePopularArticle = driverTools.swipeToElement(DISLIKE_ARTICLE,Swipe.UP,3).orElseThrow();
        driverTools.click(dislikePopularArticle);
        driverTools.switchToNative();
    }

    public void clickLikeBtnPopularArticle(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        WebElement likePopularArticle = driverTools.swipeToElement(LIKE_ARTICLE,Swipe.UP,3).orElseThrow();
        driverTools.click(likePopularArticle);
        driverTools.switchToNative();
    }

    public boolean isOnClickedPopularArticle(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        boolean isDislikeIconPopularArticle = driverTools.isElementDisplayed(DISLIKE_ARTICLE);
        logger.log("is on correct popular article i.e dislike icon displayed :" + isDislikeIconPopularArticle);
        boolean isLikeIconPopularArticle = driverTools.isElementDisplayed(LIKE_ARTICLE);
        logger.log("is on correct popular article i.e like icon displayed :" + isLikeIconPopularArticle);
        driverTools.switchToNative();
        return isDislikeIconPopularArticle && isLikeIconPopularArticle;
    }

    public boolean verifyRelatedArticle(String orderId){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        if(driverTools.waitForElementToAppear(CLOSE_TRAVIS_INFO_BANNER).isPresent()){
            driverTools.clickElementByCoordinateDWeb(CLOSE_TRAVIS_INFO_BANNER);
        }
        WebElement article = driverTools.swipeToElement(getRelatedArticleLocator(orderId),Swipe.LEFT,10).orElseThrow();
        String articleTitle = driverTools.getText(article);
        logger.log("Article title in help center: "+articleTitle);
        driverTools.click(article);
        WebElement articleTitleInArticleElement = driverTools.waitForElementToAppear(ARTICLE_TITLE).orElseThrow();
        String articleTitleInArticle = driverTools.getText(articleTitleInArticleElement);
        logger.log("Article title in article: "+articleTitleInArticle);
        driverTools.switchToNative();
        return articleTitleInArticle.equals(articleTitle);
    }

    private Locator getRelatedArticleLocator(String orderId){
        return new Locator("Article locator", AppiumBy.xpath("//*[text()='"+orderId+"']//parent::*//following-sibling::*[contains(@class,'styles_faq_list')]//*[contains(@data-testid,'faqItem')]/p[text()]"));
    }

    public String getPopularArticlePagTitle(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        WebElement popularArticlePageTitile = driverTools.waitForElementToAppear(POPULAR_ARTICLE_PAGE_TITLE,10).orElseThrow();
        String pageTitle = driverTools.getText(popularArticlePageTitile);
        driverTools.switchToNative();
        return pageTitle;
    }

    public void clickContactCustomerCare(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        logger.log("Clicking contact customer care");
        if(driverTools.waitForElementToAppear(CLOSE_TRAVIS_INFO_BANNER,2).isPresent()){
            driverTools.clickElementByCoordinateDWeb(CLOSE_TRAVIS_INFO_BANNER);
        }
        driverTools.swipeToElement(CONTACT_CUSTOMER_CARE, Swipe.UP, 10);
        driverTools.click(CONTACT_CUSTOMER_CARE);
        driverTools.switchToNative();
    }

    public boolean isUserAccountNameDisplayedCorrectly(String firstName) {
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(USER_FIRST_NAME);
        String textDisplayedOnHelpCenter = driverTools.getText(USER_FIRST_NAME).orElseThrow();
        logger.log("USER_FIRST_NAME : " + textDisplayedOnHelpCenter);
        driverTools.switchToNative();

        return textDisplayedOnHelpCenter.contains(firstName);
    }

    public void clickBack() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("help");
        driverTools.waitForElementToAppear(BACK_BUTTON).ifPresent(driverTools::click);
        driverTools.switchToNative();
    }

    public String getOpenedPopularArticleUrl(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        String url = driverTools.getCurrentURL();
        driverTools.switchToNative();
        return url;
    }

    public boolean checkListOfOrders(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        boolean isOrderSliderDisplayed = driverTools.isElementDisplayed(ORDER_SLIDER_CARD_LIST);
        logger.log("is order slider list displayed : " + isOrderSliderDisplayed);
        boolean isOderStatusAlongWithOrderIdDisplayed = driverTools.isElementDisplayed(ORDER_STATUS_LIST);
        logger.log("is orderID ad order status displayed : " + isOderStatusAlongWithOrderIdDisplayed);
        boolean isOderTitleDisplayed = driverTools.isElementDisplayed(ORDER_SLIDER_CARD_TITLE);
        logger.log("is order title displayed : " + isOderTitleDisplayed);
        driverTools.switchToNative();
        return isOrderSliderDisplayed && isOderStatusAlongWithOrderIdDisplayed && isOderTitleDisplayed;
    }

    public void skipCoachmark() {
        driverTools.waitForWebview();
        driverTools.switchToWebView();

        driverTools.waitForElementToPresence(SKIP_COACHMARK_BTN,5).ifPresent(driverTools::click);

        driverTools.switchToNative();
    }

    public void clickRefundButton(String orderId){
        driverTools.switchToWebView();
        driverTools.click(getButtonLocator(orderId,"Refund"));
        driverTools.switchToNative();
    }

    public void clickRescheduleButton(String orderId){
        driverTools.switchToWebView();
        driverTools.click(getButtonLocator(orderId,"Reschedule"));
        driverTools.switchToNative();
    }

    public void clickSeeCompleteHelpButton(String orderId){
        driverTools.switchToWebView();
        driverTools.click(getButtonLocator(orderId,"See all"));
        driverTools.switchToNative();
    }

    public String checkChangeOrderFromContextual(String orderId){
        driverTools.switchToWebViewUrl("help");
        driverTools.click(CONTEXTUAL_SEE_ALL);
        driverTools.click(getDifferentOrderLocator(orderId));
        driverTools.waitForElementToDisappear(getOrderLocator(orderId));
        String newOrderId = driverTools.getText(ORDER_ID_CONTEXTUAL).orElseThrow();
        driverTools.click(CONTEXTUAL_SEE_ALL);
        driverTools.click(getOrderLocator(orderId));
        driverTools.switchToNative();
        return newOrderId;
    }

    private Locator getDifferentOrderLocator(String orderId){
        return new Locator("not "+orderId,AppiumBy.xpath("//*[contains(@data-testid,'orderIdText') and not(contains(normalize-space(.),'"+orderId+"'))]"));
    }

    private Locator getOrderLocator(String orderId){
        return new Locator("not "+orderId,AppiumBy.xpath("//*[contains(@data-testid,'orderIdText') and contains(normalize-space(.),'"+orderId+"')]"));
    }

    public String checkArticleContextual(){
        driverTools.switchToWebViewUrl("help");
        driverTools.click(ARTICLE_CONTEXTUAL);
        driverTools.waitForElementToDisappear(ARTICLE_CONTEXTUAL);
        driverTools.waitForElementToAppear(ARTICLE_TITLE);
        String articleTitle = driverTools.getText(ARTICLE_TITLE).orElseThrow();
        driverTools.click(BACK_BUTTON);
        driverTools.switchToNative();
        return articleTitle;
    }

    private Locator getButtonLocator(String orderId,String locatorType){
        if(locatorType.equals("See all")){
            return new Locator(locatorType,AppiumBy.xpath("(//*[normalize-space(.)='Order ID: "+orderId+"']//following::*[contains(text(),'Lihat') or contains(text(),'See')])[1]"));
        }
        return new Locator(locatorType,AppiumBy.xpath("(//*[normalize-space(.)='Order ID: "+orderId+"']//following::*[contains(text(),'"+locatorType+"')])[1]"));
    }

    public void clickSeeDetailOrderButton(){
        driverTools.switchToWebViewUrl("help");
        driverTools.click(SEE_ORDER_DETAIL_BUTTON);
        driverTools.switchToNative();
    }

    public void clickBackPopup(){
        driverTools.switchToWebViewUrl("help");
        driverTools.click(BACK_POPUP_BUTTON);
        driverTools.switchToNative();
    }
}
