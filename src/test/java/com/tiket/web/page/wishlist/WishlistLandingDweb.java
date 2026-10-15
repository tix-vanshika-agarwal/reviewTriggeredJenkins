package com.tiket.web.page.wishlist;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Locator;
import org.openqa.selenium.By;

public class WishlistLandingDweb <T extends WebDriver> extends WishlistLandingWeb<T>{

    public WishlistLandingDweb(Session<T> session){
        super(session);
    }

    private static final Locator CROSS_BUTTON = new Locator("CROSS_BUTTON", By.xpath("//button[contains(@data-testid,'modal-close-button')]"));
    private static final Locator OK_BUTTON = new Locator("OK_BUTTON", By.xpath("//span[text()='OK' or text()='Oke']"));
    private static final Locator COLLECTION_THREE_DOTS = new Locator("COLLECTION_THREE_DOTS", By.xpath("//div[contains(@class,'RoundedButton_rounded_button_wrapper')]"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", By.xpath("//span[text()='Back' or text()='Kembali']"));
    private static final Locator COLLECTION_T0_EDIT = new Locator("COLLECTION_T0_EDIT", By.xpath("//p[contains(text(),'For Automation (Do not Delete/Edit)')]"));
    private static final Locator REMOVE_FROM_COLLECTION_MENU_BUTTON = new Locator("REMOVE_FROM_COLLECTION_MENU_BUTTON", By.xpath("(//div[contains(@class,'ManageWishlist_menu_item')])[2]"));

    @Override
    public void clickCross() {
    driverTools.waitForElementsToAppear(CROSS_BUTTON, 5);
    if (driverTools.isElementDisplayed(CROSS_BUTTON)) {
        driverTools.clickWithJavascriptExecutor(CROSS_BUTTON);
        driverTools.waitForElementsToAppear(OK_BUTTON);
        driverTools.clickWithJavascriptExecutor(OK_BUTTON);
    }
}

    @Override
    public void clickThreeDotsOnCollection() {
        driverTools.waitForElementsToAppear(COLLECTION_THREE_DOTS);
        driverTools.click(COLLECTION_THREE_DOTS);
    }

    @Override
    public Locator getCollectionName(String name) {
        return new Locator("COLLECTION_NAME_AFTER_EDIT", By.xpath("//*[text()='" + name + "']"));
    }

    @Override
    public void clickBackButton() {
        driverTools.waitForElementsToAppear(BACK_BUTTON);
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public void openEmptyCollection() {
        driverTools.waitForElementsToAppear(COLLECTION_T0_EDIT);
        driverTools.click(COLLECTION_T0_EDIT);
    }

    @Override
    public void removeFromCollectionMenuButton() {
        driverTools.waitForElementsToAppear(REMOVE_FROM_COLLECTION_MENU_BUTTON);
        driverTools.clickWithJavascriptExecutor(REMOVE_FROM_COLLECTION_MENU_BUTTON);
    }
}