package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AffiliatePWAPartnerEditPage<T extends WebDriver> extends BasePage<T> {

    public AffiliatePWAPartnerEditPage(Session<T> session) {
        super(session);
    }

    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[contains(@class,'Button_variant_primary') and text()='Save']"));
    private static final Locator ADD_PRODUCT_BUTTON = new Locator("ADD_PRODUCT_BUTTON", By.xpath("//button[contains(@class,'Button_variant_primary') and text()='Add Product']"));
    private static final Locator NEW_PRODUCT_NAME_FIELD = new Locator("NEW_PRODUCT_NAME_FIELD", By.xpath("(//input[@id='product-name'])[last()]"));
    private static final Locator NEW_PRODUCT_ICON_URL_FIELD = new Locator("NEW_PRODUCT_ICON_URL_FIELD", By.xpath("(//input[@id='product-icon-url'])[last()]"));
    private static final Locator NEW_PRODUCT_URL_FIELD = new Locator("NEW_PRODUCT_URL_FIELD", By.xpath("(//input[@id='product-url'])[last()]"));

    public void addNewProductOffered(String productName, String productIconURL, String productURL) {
        driverTools.waitForElementToAppear(NEW_PRODUCT_NAME_FIELD);

        driverTools.click(ADD_PRODUCT_BUTTON);

        driverTools.sendKeys(NEW_PRODUCT_NAME_FIELD, productName);
        driverTools.sendKeys(NEW_PRODUCT_ICON_URL_FIELD, "https://" + productIconURL);
        driverTools.sendKeys(NEW_PRODUCT_URL_FIELD, "https://" + productURL);
    }

    public void clickSaveButton() {
        driverTools.clickElementByCoordinateDWeb(SAVE_BUTTON);
    }

    public void deleteProductOffered(String productName) {
        driverTools.click(getDeleteButtonLocatorByProductName(productName));
    }

    private Locator getDeleteButtonLocatorByProductName(String productName) {
        return new Locator("DELETE_BUTTON_" + productName, By.xpath("//div[contains(@class,'Card_card_container') and contains(@class,'Card_padding_medium') and .//input[@id='product-name' and @value='"+productName+"']]/*/*[last()]"));
    }
}
