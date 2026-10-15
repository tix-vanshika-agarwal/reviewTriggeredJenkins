package com.tiket.page.utilities.message.chat;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class ChatBoxPageIOS <T extends WebDriver> extends ChatBoxPage<T>{
    public ChatBoxPageIOS(Session<T> session) {
        super(session);
    }

    private static final Locator INPUT_CONTAINER = new Locator("INPUT_CONTAINER", AppiumBy.xpath("//*[contains(@label,'Type your message here') or contains(@label,'Tulis pesanmu di sini')]"));
    private static final Locator PLUS_BTN = new Locator("PLUS_BTN", AppiumBy.id("tds_ic_oval_plus"));
    private static final Locator CALL_BTN = new Locator("CALL_BTN", AppiumBy.id("tds_ic_telephone"));
    private static final Locator STATUS = new Locator("STATUS", AppiumBy.xpath("//*[contains(@label,'online')]"));
    private static final Locator AVATAR = new Locator("AVATAR", AppiumBy.xpath("(//XCUIElementTypeImage)[1]"));

    private Locator getNameOfChatBoxLocator(String text){
        return new Locator("NAME_CHAT_BOX", AppiumBy.xpath("(//*[contains(@label,'"+text+"')])[1]"));
    }

    private Locator checkOrderIdInChatBoxLocator(String text){
        return new Locator("ORDER_ID_CHAT_BOX", AppiumBy.xpath("//*[contains(@label,'"+text+"')]"));
    }

    @Override
    public boolean verifyChatBox(String query, String orderID){
        driverTools.waitForElementToAppear(INPUT_CONTAINER);

        boolean isInputContainerDisplayed = driverTools.isElementDisplayed(INPUT_CONTAINER);
        logger.log("is input container displayed : " + isInputContainerDisplayed);

        boolean isPlusInputDisplayed = driverTools.isElementDisplayed(PLUS_BTN);
        logger.log("is plus container displayed : " + isPlusInputDisplayed);

        boolean isCallBtnDisplayed = driverTools.isElementDisplayed(CALL_BTN);
        logger.log("is call btn displayed : " + isCallBtnDisplayed);

        boolean isStatusDisplayed = driverTools.isElementDisplayed(STATUS);
        logger.log("is status displayed : " + isStatusDisplayed);

        boolean isAvatarDisplayed = driverTools.isElementDisplayed(AVATAR);
        logger.log("is avatar displayed : " + isAvatarDisplayed);

        boolean isChatBoxNameDisplayed = driverTools.isElementDisplayed(getNameOfChatBoxLocator(query));
        logger.log("is chat box name correctly displayed : " + isChatBoxNameDisplayed);

        boolean isOrderIdDisplayed = driverTools.isElementDisplayed(driverTools.swipeToElement(checkOrderIdInChatBoxLocator(query), Swipe.DOWN,3).orElseThrow());
        logger.log("is orderId correctly displayed : " + isOrderIdDisplayed);

        return isInputContainerDisplayed && isCallBtnDisplayed && isStatusDisplayed && isAvatarDisplayed
                && isOrderIdDisplayed;
    }
}
