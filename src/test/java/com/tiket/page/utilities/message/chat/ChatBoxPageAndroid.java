package com.tiket.page.utilities.message.chat;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class ChatBoxPageAndroid <T extends WebDriver> extends ChatBoxPage<T> {
    public ChatBoxPageAndroid(Session<T> session) {
        super(session);
    }

    private static final Locator INPUT_CONTAINER = new Locator("INPUT_CONTAINER", AppiumBy.id("input_container"));
    private static final Locator CALL_BTN = new Locator("CALL_BTN", AppiumBy.id("iv_call"));
    private static final Locator STATUS = new Locator("STATUS", AppiumBy.id("tv_status"));
    private static final Locator AVATAR = new Locator("AVATAR", AppiumBy.id("iv_avatar"));
    private static final Locator CHAT_HEADER_COMPONENT = new Locator("CHAT_HEADER_COMPONENT", AppiumBy.id("chat_header_component"));

    private Locator getNameOfChatBoxLocator(String text){
        return new Locator("NAME_CHAT_BOX", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'"+text+"')]"));
    }

    private Locator checkOrderIdInChatBoxLocator(String text){
        return new Locator("ORDER_ID_CHAT_BOX", AppiumBy.xpath("//*[contains(@resource-id,'tv_message') and contains(@text,'"+text+"')]"));
    }

    @Override
    public boolean verifyChatBox(String query, String orderID){
        driverTools.waitForElementToAppear(INPUT_CONTAINER);

        boolean isInputContainerDisplayed = driverTools.isElementDisplayed(INPUT_CONTAINER);
        logger.log("is input container displayed : " + isInputContainerDisplayed);

        boolean isCallBtnDisplayed = driverTools.isElementDisplayed(CALL_BTN);
        logger.log("is call btn displayed : " + isCallBtnDisplayed);

        boolean isStatusDisplayed = driverTools.isElementDisplayed(STATUS);
        logger.log("is status displayed : " + isStatusDisplayed);

        boolean isAvatarDisplayed = driverTools.isElementDisplayed(AVATAR);
        logger.log("is avatar displayed : " + isAvatarDisplayed);

        boolean isChatHeaderDisplayed = driverTools.isElementDisplayed(CHAT_HEADER_COMPONENT);
        logger.log("is chat header displayed : " + isChatHeaderDisplayed);

        boolean isChatBoxNameDisplayed = driverTools.isElementDisplayed(getNameOfChatBoxLocator(query));
        logger.log("is chat box name correctly displayed : " + isChatBoxNameDisplayed);

        boolean isOrderIdDisplayed = driverTools.isElementDisplayed(driverTools.swipeToElement(checkOrderIdInChatBoxLocator(query), Swipe.DOWN,3).orElseThrow());
        logger.log("is orderId correctly displayed : " + isOrderIdDisplayed);

        return isInputContainerDisplayed && isCallBtnDisplayed && isStatusDisplayed && isAvatarDisplayed
                && isChatHeaderDisplayed && isOrderIdDisplayed;
    }
}
