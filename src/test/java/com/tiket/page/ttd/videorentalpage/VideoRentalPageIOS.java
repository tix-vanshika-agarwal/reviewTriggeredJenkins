package com.tiket.page.ttd.videorentalpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class VideoRentalPageIOS<T extends WebDriver> extends VideoRentalPage<T> {

    public VideoRentalPageIOS(Session<T> session) {
        super(session);
    }

    private static final Locator VIDEO_PAGE_HEADER = new Locator("VIDEO_PAGE_HEADER", AppiumBy.xpath("//*[contains(@name,'Video Rental') or contains(@name,'Sewa Video')]"));
    private static final Locator VIDEO_PLAY_BUTTON = new Locator("VIDEO_PLAY_BUTTON", AppiumBy.xpath("//*[contains(@class,'styles__PlayButton')]"));
    private static final Locator LOADING_STATE = new Locator("LOADING_STATE", AppiumBy.id("loading"));
    private static final Locator VIDEO_PLAY_CONFIRMATION = new Locator("VIDEO_PLAY_CONFIRMATION", AppiumBy.xpath("//*[contains(@class,'styles__ButtonDefaultStyle') and (contains(text(),'Tonton') or contains(text(),'Watch'))]"));
    private static final Locator VIDEO_CONTROLLER = new Locator("VIDEO_CONTROLLER", AppiumBy.xpath("//div[@class='react-player']/video"));
    private static final Locator VIDEO_PLAYER = new Locator("VIDEO_PLAYER", AppiumBy.xpath("//div[@class='react-player']"));


    @Override
    public void clickPlayVideo() {
        driverTools.waitForElementToAppear(VIDEO_PAGE_HEADER);

        driverTools.switchToWebView();

        WebElement webElement = driverTools.waitForElementToAppear(VIDEO_PLAY_BUTTON).orElseThrow();
        driverTools.click(webElement);

        if(driverTools.elements(VIDEO_PLAY_CONFIRMATION).orElseThrow().size() > 1){
            driverTools.waitForElementToBeClickable(VIDEO_PLAY_CONFIRMATION);
            List<WebElement> webElementList = driverTools.elements(VIDEO_PLAY_CONFIRMATION).orElseThrow();
            driverTools.click(webElementList.get(1));
            driverTools.waitForElementToDisappear(webElementList.get(1),10);
        } else {
            driverTools.waitForElementToBeClickable(VIDEO_PLAY_CONFIRMATION);
            List<WebElement> webElementList = driverTools.elements(VIDEO_PLAY_CONFIRMATION).orElseThrow();
            driverTools.click(webElementList.get(0));
            driverTools.waitForElementToDisappear(webElementList.get(0),10);
        }

        driverTools.switchToNative();
    }

    @Override
    public boolean isPlayButtonDisplayed() {
        driverTools.switchToWebView();
        return driverTools.isElementDisplayed(VIDEO_PLAY_BUTTON);
    }

    @Override
    public boolean isVideoControlExisted() {
        driverTools.switchToWebView();
        driverTools.click(VIDEO_PLAYER);
        return driverTools.waitForElementToPresence(VIDEO_CONTROLLER).isPresent();
    }
}
