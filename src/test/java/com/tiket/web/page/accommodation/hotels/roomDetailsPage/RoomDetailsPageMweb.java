package com.tiket.web.page.accommodation.hotels.roomDetailsPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RoomDetailsPageMweb<T extends WebDriver> extends RoomDetailsPageWeb<T> {

    public RoomDetailsPageMweb(Session<T> session) {
        super(session);
    }

    private static final Locator CLOSE_BUTTON_PRICE_DETAIL = new Locator("CLOSE_BUTTON_PRICE_DETAIL", new By.ByXPath("//*[@data-testid='bottom-sheet-header']//*[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator ROOM_IMAGE = new Locator("ROOM_IMAGE", new By.ByXPath("//img[@data-testid='final-result-image']"));
    private static final Locator CLOSE_BUTTON_IMAGE_PREVIEW = new Locator("CLOSE_BUTTON_IMAGE_PREVIEW", new By.ByXPath("//button[contains(@class,'ImagePreview_floating_close')]"));
    private static final Locator ROOM_CARD = new Locator("ROOM_CARD", new By.ByXPath("(//*[@data-testid='rate-plan-card-container'])[1]"));

    @Override
    public void closePriceDetails() {
        driverTools.click(CLOSE_BUTTON_PRICE_DETAIL);
    }

    @Override
    public void clickRoomCard() {
        driverTools.waitForElementToBeClickable(ROOM_CARD);
        driverTools.click(ROOM_CARD);
    }
    @Override
    public void clickRoomImage() {
        driverTools.click(ROOM_IMAGE);
    }
    @Override
    public boolean isImageDetailViewDisplayed() {
        return driverTools.waitForElementToPresence(CLOSE_BUTTON_IMAGE_PREVIEW, 5).isPresent();
    }

    @Override
    public void clickImageDetailCloseButton() {
        driverTools.click(CLOSE_BUTTON_IMAGE_PREVIEW);
    }

    @Override
    public boolean isRoomDetailsDisplayed()
    {
        return driverTools.waitForElementToPresence(ROOM_IMAGE, 5).isPresent();
    }

}
