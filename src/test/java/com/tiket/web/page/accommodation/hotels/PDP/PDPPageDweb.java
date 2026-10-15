package com.tiket.web.page.accommodation.hotels.PDP;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.page.accommodation.hotels.PDP.PDPPageWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PDPPageDweb<T extends WebDriver> extends PDPPageWeb<T> {
    private static final Locator SEE_ROOM_BUTTON = new Locator("SEE_ROOM_BUTTON",new By.ByXPath("(//div[contains(@class, 'see_room_button')])[1]"));
    private static final Locator PRICE_PDP = new Locator("PRICE_PDP", new By.ByXPath("//div[contains(@class, 'Text_variant_price') and contains(@class, 'Text_align_right')]"));


    public PDPPageDweb(Session<T> session) {
        super(session);
    }

    public void clickNextAvailableDate(){
        driverTools.clickWithJavascriptExecutor(NEXT_AVAILABLE_DATE_LIST_ITEM);
    }

    public void clickSeeRoomButton(){
        driverTools.waitForElementsToAppear(SEE_ROOM_BUTTON,3);
        driverTools.clickWithJavascriptExecutor(SEE_ROOM_BUTTON);
    }

    @Override
    public String getHotelPriceDetail() {
            WebElement priceText = driverTools.waitForElementToAppear(PRICE_PDP).orElseThrow();
            return driverTools.getText(priceText);
        }

    @Override
    public void setStayDatePDP(String type, String date) {
        return;
    }
}
