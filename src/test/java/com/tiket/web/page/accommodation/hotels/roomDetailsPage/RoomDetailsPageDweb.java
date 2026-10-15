package com.tiket.web.page.accommodation.hotels.roomDetailsPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RoomDetailsPageDweb<T extends WebDriver> extends RoomDetailsPageWeb<T> {

    public RoomDetailsPageDweb(Session<T> session) {
        super(session);
    }

    private static final Locator CLOSE_BUTTON_PRICE_DETAIL = new Locator("CLOSE_BUTTON_PRICE_DETAIL", new By.ByXPath("//*[@data-testid='modal-close-button']"));
    private static final Locator ROOM_IMAGE_CARD = new Locator("ROOM_IMAGE_CARD", new By.ByXPath("//*[@data-testid='final-result-image']"));
    private static final Locator CLOSE_BUTTON_IMAGE_DETAIL = new Locator("CLOSE_BUTTON_IMAGE_DETAIL", new By.ByXPath("//*[contains(@class,'HeroBanner_close_icon_cnt')]"));
    public static final Locator SEE_DETAILS_BUTTON = new Locator("SEE_DETAILS_BUTTON", By.xpath("(//button[normalize-space()='See details' or normalize-space()='Lihat detail'])[1]"));

    @Override
    public void closePriceDetails() {
        driverTools.click(CLOSE_BUTTON_PRICE_DETAIL);
    }
    @Override
    public void clickRoomCard() {
        driverTools.waitForElementToBeClickable(SEE_DETAILS_BUTTON);
        driverTools.click(SEE_DETAILS_BUTTON);
    }

    @Override
    public void clickRoomImage() {
        driverTools.click(ROOM_IMAGE_CARD);
    }

    @Override
    public boolean isImageDetailViewDisplayed() {
        return driverTools.waitForElementToPresence(CLOSE_BUTTON_IMAGE_DETAIL, 5).isPresent();
    }

    @Override
    public void clickImageDetailCloseButton() {
        driverTools.clickWithJavascriptExecutor(CLOSE_BUTTON_IMAGE_DETAIL);
    }

    @Override
    public boolean isRoomDetailsDisplayed() {
        return driverTools.waitForElementToPresence(SEE_DETAILS_BUTTON, 5).isPresent();
    }
}
