package com.tiket.web.dWeb.page.accommodation.homes;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomesSearchResultPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//h3"));
    private static final Locator PRICE_TAG = new Locator("PRICE_TAG", new By.ByXPath("(//a[contains(@class, 'ProductCard_container')])[1]//div[contains(@class,'PriceModule_final')]"));
    private static final Locator HOTEL_LOCATION = new Locator("HOTEL_LOCATION", new By.ByXPath("//*[contains(@class,'FullProductCard_hotel_address')]"));
    private static final Locator ADULT_INC_BUTTON = new Locator("ADULT_INC_BUTTON", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[2]"));
    private static final Locator ADULT_DEC_BUTTON = new Locator("ADULT_DEC_BUTTON", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[1]"));
    private static final Locator DONE_BUTTON = new Locator("DONE_BUTTON", new By.ByXPath("//div[contains(@class, 'Modal_modal_footer_button_group')]//button"));
    private static final Locator CURRENT_ADULT_AMOUNT = new Locator("MASTER_TAG", new By.ByXPath("(//*[contains(@class,'QuantityEditor_quantity_input')])[1]"));


    public HomesSearchResultPage(Session<T> session) {
        super(session);
    }

    public String getHotelName() {
        WebElement webElement = driverTools.waitForElementToAppear(HOTEL_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String getHotelPrice() {
        WebElement webElement = driverTools.waitForElementToAppear(PRICE_TAG).orElseThrow();
        return driverTools.getText(webElement);
    }

    public String[] getHotelLocation() {
        WebElement webElement = driverTools.waitForElementToAppear(HOTEL_LOCATION).orElseThrow();
        String text =driverTools.getText(webElement);
        String[] locations = text.split(",");
        return locations;
    }

    public void setTotalGuest(int adult) throws InterruptedException {
        int currentAdult = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ADULT_AMOUNT,"value"));
        logger.log("currentAdult is '" + currentAdult + "'");
        if(adult > currentAdult){
            for(int i = 0; i < adult - currentAdult; i++){
                driverTools.clickWithJavascriptExecutor(ADULT_INC_BUTTON);
            }
        } else if(adult < currentAdult){
            for(int i = 0; i < currentAdult - adult; i++){
                driverTools.clickWithJavascriptExecutor(ADULT_DEC_BUTTON);
            }
        }
    }

    public void clickDone(){
        driverTools.clickWithJavascriptExecutor(DONE_BUTTON);
    }
}
