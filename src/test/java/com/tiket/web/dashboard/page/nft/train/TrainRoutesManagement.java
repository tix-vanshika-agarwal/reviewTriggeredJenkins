package com.tiket.web.dashboard.page.nft.train;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class TrainRoutesManagement <T extends WebDriver> extends BasePage<T> {
    private static final Locator TRAIN_ROUTES_MANAGEMENT_MENU = new Locator("TRAIN_ROUTES_MANAGEMENT_MENU", new By.ByXPath("//div[@class='menu-list' and contains(text(),'Train Routes Management')]"));
    private static final Locator ADD_NEW_ROUTE_BUTTON = new Locator("ADD_NEW_ROUTE_BUTTON", new By.ByXPath("//div[contains(text(),'+ ADD NEW ROUTES')]"));
    private static final Locator SELECT_PRODUCT_INPUT = new Locator("SELECT_PRODUCT_INPUT", new By.ByXPath("//div[contains(@class, 'product__control')]"));
    private static final Locator KAI_PRODUCT_AS_INPUT = new Locator("KAI_PRODUCT_AS_INPUT", new By.ByXPath("//div[contains(@class, 'product__option') and contains(text(), 'KAI')]"));
    private static final Locator CHOOSE_ORIGIN_INPUT = new Locator("CHOOSE_ORIGIN_INPUT", new By.ByXPath("//div[contains(@class, 'with-popup') and contains(@class, 'show')]//p[contains(text(), 'Origin')]/following-sibling::div[contains(@class, '-container')]//div[contains(text(),'Choose Station')]"));
    private static final Locator CHOOSE_DESTINATION_INPUT = new Locator("CHOOSE_DESTINATION_INPUT", new By.ByXPath("//div[contains(@class, 'with-popup') and contains(@class, 'show')]//p[contains(text(), 'Destination')]/following-sibling::div[contains(@class, '-container')]//div[contains(text(),'Choose Station')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//div[contains(@class, 'with-popup') and contains(@class, 'show')]//button[@class='button-primary' and @type='submit' and contains(text(), 'SAVE')]"));
    private static final Locator SUCCESS_ADD_BANNER = new Locator("SUCCESS_ADD_BANNER", new By.ByXPath("//p[contains(text(), 'Page succesfully added')]"));
    private static final Locator SEARCH_INPUT = new Locator("SEARCH_INPUT", new By.ByXPath("//input[@class='form-control' and @type='text' and @placeholder='Search something here…']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[contains(text(), 'SEARCH')]"));
    private static final Locator EDIT_ORIGIN_INPUT = new Locator("EDIT_ORIGIN_INPUT", new By.ByXPath("//div[contains(@class, 'with-popup') and contains(@class, 'show')]//p[contains(text(), 'Origin')]/following-sibling::div[contains(@class, '-container')]"));
    private static final Locator EDIT_DESTINATION_INPUT = new Locator("EDIT_DESTINATION_INPUT", new By.ByXPath("//div[contains(@class, 'with-popup') and contains(@class, 'show')]//p[contains(text(), 'Destination')]/following-sibling::div[contains(@class, '-container')]"));
    private static final Locator SUCCESS_DELETE_BANNER = new Locator("SUCCESS_DELETE_BANNER", new By.ByXPath("//p[contains(text(), 'Page succesfully deleted')]"));
    private static final Locator SUCCESS_EDIT_BANNER = new Locator("SUCCESS_EDIT_BANNER", new By.ByXPath("//p[contains(text(), 'Page succesfully edited')]"));
    private static final Locator POPUP_ACTIVATE_BANNER = new Locator("POPUP_ACTIVATE_BANNER", new By.ByXPath("//button[contains(text(),'Yes, activate')]"));
    private static final Locator SUCCESS_ACTIVATE_BANNER = new Locator("SUCCESS_ACTIVATE_BANNER", new By.ByXPath("//p[contains(text(), 'Page Successfully Activated')]"));
    private static final Locator POPUP_DEACTIVATE_BANNER = new Locator("POPUP_DEACTIVATE_BANNER", new By.ByXPath("//button[contains(text(),'Yes, deactivate')]"));
    private static final Locator SUCCESS_DEACTIVATED_BANNER = new Locator("SUCCESS_DEACTIVATED_BANNER", new By.ByXPath("//p[contains(text(), 'Page Successfully Deactivated')]"));
    private static final Locator POPUP_DELETE_BANNER = new Locator("POPUP_DELETE_BANNER", new By.ByXPath("//button[contains(text(),'YES, DELETE')]"));
    private static final Locator NEXT_PAGE_PAGINATION = new Locator("NEXT_PAGE_PAGINATION", new By.ByXPath("//li[contains(@title,'next page')]"));
    private static final Locator TRAIN_ROUTES_MANAGEMENT_TITLE = new Locator("TRAIN_ROUTES_MANAGEMENT_TITLE", new By.ByXPath("//div[contains(text(),' Train Routes Management')]"));



    public TrainRoutesManagement(Session<T> session) {
        super(session);
    }

    public void goToTrainRoutesManagementMenu(){
        driverTools.click(TRAIN_ROUTES_MANAGEMENT_MENU);
    }

    public void addTrainRoute(String origin, String destination){
        Locator ORIGIN_AS_INPUT = new Locator("ORIGIN_AS_INPUT", new By.ByXPath("//div[contains(@class, '-option') and contains(text(), '" + origin + "')]"));
        Locator DESTINATION_AS_INPUT = new Locator("DESTINATION_AS_INPUT", new By.ByXPath("//div[contains(@class, '-option') and contains(text(), '" + destination + "')]"));

        WebElement addNewRouteButton = driverTools.waitForElementToAppear(ADD_NEW_ROUTE_BUTTON).orElseThrow();
        driverTools.click(addNewRouteButton);
        driverTools.click(SELECT_PRODUCT_INPUT);
        driverTools.click(KAI_PRODUCT_AS_INPUT);

        driverTools.click(CHOOSE_ORIGIN_INPUT);
        WebElement addOriginElement = driver.switchTo().activeElement();
        driverTools.sendKeys(addOriginElement, origin);
        driverTools.click(ORIGIN_AS_INPUT);

        driverTools.click(CHOOSE_DESTINATION_INPUT);
        WebElement addDestinationElement = driver.switchTo().activeElement();
        driverTools.sendKeys(addDestinationElement, destination);
        driverTools.click(DESTINATION_AS_INPUT);

        driverTools.click(SAVE_BUTTON);
    }

    public boolean verifyAddTrainRoute(){
        return driverTools.waitForElementToAppear(SUCCESS_ADD_BANNER).isPresent();
    }

    public void searchTrainRoute(String origin, String destination) {
        Locator SEARCHED_ROUTE = new Locator("SEARCHED_ROUTE", new By.ByXPath("//td[contains(text(),'" + origin + "')]/following-sibling::td[contains(text(),'" + destination + "')]"));
        Actions action = new Actions(driver);
        final int MAX_ATTEMPTS = 10;

        initiateSearch(origin);

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            logger.log("Attempt: " + attempt + " ");
            action.sendKeys(Keys.ARROW_DOWN).perform();

            if (searchForRoute(SEARCHED_ROUTE)) {
                logger.log("Element found, stopping the scroll.");
                return;
            }
            if (navigateToNextPage()) {
                driverTools.waitForElementToAppear(TRAIN_ROUTES_MANAGEMENT_TITLE).orElseThrow();
            } else {
                logger.log("Element not found after " + MAX_ATTEMPTS + " attempts.");
                break;
            }
        }
    }

    private void initiateSearch(String origin) {
        driverTools.click(SEARCH_INPUT);
        driverTools.sendKeys(SEARCH_INPUT, origin);
        driverTools.click(SEARCH_BUTTON);
        driverTools.click(TRAIN_ROUTES_MANAGEMENT_TITLE);
    }

    private boolean searchForRoute(Locator route) {
        return driverTools.waitForElementToAppear(route,3).isPresent();
    }

    private boolean navigateToNextPage() {
        if (driverTools.isElementEnabled(NEXT_PAGE_PAGINATION)) {
            driverTools.waitForElementToBeClickable(NEXT_PAGE_PAGINATION);
            driverTools.click(NEXT_PAGE_PAGINATION);
            return true;
        }
        return false;
    }

    public void editTrainRoute(String existingOrigin, String existingDestination, String toBeEditedOrigin, String toBeEditedDestination){
        Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", new By.ByXPath("//td[contains(text(),'" + existingOrigin + "')]/following-sibling::td[contains(text(),'" + existingDestination + "')]/following-sibling::td//i[@class='tix-clean-edit']"));
        Locator TO_BE_EDITED_ORIGIN_AS_INPUT = new Locator("TO_BE_EDITED_ORIGIN_AS_INPUT", new By.ByXPath("//div[contains(@class, '-option') and contains(text(), '" + toBeEditedOrigin + "')]"));
        Locator TO_BE_EDITED_DESTINATION_AS_INPUT = new Locator("TO_BE_EDITED_DESTINATION_AS_INPUT", new By.ByXPath("//div[contains(@class, '-option') and contains(text(), '" + toBeEditedDestination + "')]"));

        driverTools.click(EDIT_BUTTON);

        driverTools.click(EDIT_ORIGIN_INPUT);
        WebElement editOriginElement = driver.switchTo().activeElement();
        driverTools.sendKeys(editOriginElement, toBeEditedOrigin);
        driverTools.click(TO_BE_EDITED_ORIGIN_AS_INPUT);

        driverTools.click(EDIT_DESTINATION_INPUT);
        WebElement editDestinationElement = driver.switchTo().activeElement();
        driverTools.sendKeys(editDestinationElement, toBeEditedDestination);
        driverTools.click(TO_BE_EDITED_DESTINATION_AS_INPUT);
        driverTools.click(SAVE_BUTTON);
    }

    public boolean verifyEditTrainRoute(){
        return driverTools.waitForElementToAppear(SUCCESS_EDIT_BANNER).isPresent();
    }

    public void activateTrainRoute(String origin, String destination){
        Locator ACTIVATE_BUTTON = new Locator("ACTIVATE_BUTTON", new By.ByXPath("//td[contains(text(),'" + origin + "')]/following-sibling::td[contains(text(),'" + destination + "')]/following-sibling::td//div[@class='button-status' and contains(text(),'Activate')]"));

        driverTools.click(ACTIVATE_BUTTON);
        driverTools.click(POPUP_ACTIVATE_BANNER);
    }

    public boolean verifyActivateTrainRoute(){
        return driverTools.waitForElementToAppear(SUCCESS_ACTIVATE_BANNER).isPresent();
    }

    public void deactivateTrainRoute(String origin, String destination){
        Locator DEACTIVATE_BUTTON = new Locator("DEACTIVATE_BUTTON", new By.ByXPath("//td[contains(text(),'" + origin + "')]/following-sibling::td[contains(text(),'" + destination + "')]/following-sibling::td//div[@class='button-status' and contains(text(),'Deactivate')]"));

        driverTools.click(DEACTIVATE_BUTTON);
        driverTools.click(POPUP_DEACTIVATE_BANNER);
    }

    public boolean verifyDeactivateTrainRoute(){
        return driverTools.waitForElementToAppear(SUCCESS_DEACTIVATED_BANNER).isPresent();
    }

    public void deleteTrainRoute(String origin, String destination){
        Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("//td[contains(text(),'" + origin + "')]/following-sibling::td[contains(text(),'" + destination + "')]/following-sibling::td//i[@class='tix-trash']"));

        driverTools.click(DELETE_BUTTON);
        driverTools.click(POPUP_DELETE_BANNER);
    }

    public boolean verifyDeleteTrainRoute(){
        return driverTools.waitForElementToAppear(SUCCESS_DELETE_BANNER).isPresent();
    }
}
