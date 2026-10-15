package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;

public class ExclusivePrivateDealsPage<T extends WebDriver> extends BasePage<T> {
    public ExclusivePrivateDealsPage(Session<T> session) {
        super(session);
    }

    private static final Locator EPD_PAGE = new Locator("EPD_PAGE", new By.ByXPath("//div[@class=\"epd-card\"]"));
    private static final Locator STATUS_ON_TOGGLE = new Locator("STATUS_ON_TOGGLE", new By.ByXPath("//span[@class=\"the-toggle-btn the-toggle-btn-on\"]"));
    private static final Locator STATUS_OFF_TOGGLE = new Locator("STATUS_OFF_TOGGLE", new By.ByXPath("//span[@class=\"the-toggle-btn the-toggle-btn-off\"]"));
    private static final Locator DISCOUNT_VALUE_SLIDER_INPUT = new Locator("DISCOUNT_VALUE_SLIDER_INPUT", new By.ByXPath("//input[@name='valueDiscount']"));
    private static final Locator COMBINABLE_YES_RADIO_BUTTON = new Locator("COMBINABLE_YES_RADIO_BUTTON", new By.ByXPath("//input[@name=\"combinable\"][@value=\"true\"]/following-sibling::span"));
    private static final Locator COMBINABLE_NO_RADIO_BUTTON = new Locator("COMBINABLE_NO_RADIO_BUTTON", new By.ByXPath("//input[@name=\"combinable\"][@value=\"false\"]/following-sibling::span"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[@type=\"submit\"][contains(@class,'the-button yellow')]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", new By.ByXPath("//div[@class='the-toast success opened']"));
    private static final Locator MIN_DISCOUNT_VALUE_SLIDER_INPUT = new Locator("MIN_DISCOUNT_VALUE_SLIDER_INPUT",new By.ByXPath("//input[@name='minDiscount']") );


    public boolean isEPDPageDisplayed() {
        return driverTools.waitForElementToAppear(EPD_PAGE).isPresent();
    }

    public void switchStatusToggle(boolean isActive) {
        WebElement toggle = null;
        if (isActive && driverTools.isElementDisplayed(STATUS_ON_TOGGLE)) {
            logger.log("Toggle is already active");
            return;
        }
        if (!isActive && driverTools.isElementDisplayed(STATUS_OFF_TOGGLE)) {
            logger.log("Toggle is already inactive");
            return;
        }
        if (isActive) {
            try {
                toggle = driverTools.waitForElementToBeClickable(STATUS_OFF_TOGGLE).orElseThrow();
            } catch (NoSuchElementException e) {
                logger.log("Toggle is already active");

            }
        } else {
            try {
                toggle = driverTools.waitForElementToBeClickable(STATUS_ON_TOGGLE).orElseThrow();
            } catch (NoSuchElementException e) {
                logger.log("Toggle is already inactive");

            }
        }
        assert toggle != null;
        driverTools.click(toggle);
    }

    public void selectCombinableDiscount(boolean isCombinable) {
        WebElement radio = null;
        if (isCombinable) {
            radio = driverTools.waitForElementToBeClickable(COMBINABLE_YES_RADIO_BUTTON).orElseThrow();
        } else {
            radio = driverTools.waitForElementToBeClickable(COMBINABLE_NO_RADIO_BUTTON).orElseThrow();
        }
        driverTools.click(radio);
    }

    public void enterDiscountValue(String value) {
        WebElement slider = driverTools.waitForElementToAppear(DISCOUNT_VALUE_SLIDER_INPUT).orElseThrow();
        driverTools.slideToValue(value,slider);
    }

    public void enterMinDiscountValue(String value) {
        WebElement slider = driverTools.waitForElementToAppear(MIN_DISCOUNT_VALUE_SLIDER_INPUT).orElseThrow();
        driverTools.slideToValue(value,slider);
    }

    public void clickSave() {
        WebElement button = driverTools.waitForElementToBeClickable(SAVE_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isSuccessfulSnackbarDisplayed() {
        return driverTools.waitForElementToAppear(SUCCESS_SNACKBAR).isPresent();
    }

    public void refreshPage() {
        driverTools.refreshWebpage();
    }

}
