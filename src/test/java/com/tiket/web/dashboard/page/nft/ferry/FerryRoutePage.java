package com.tiket.web.dashboard.page.nft.ferry;

import com.mongodb.client.FindIterable;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class FerryRoutePage<T extends WebDriver> extends BasePage<T> {
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator SEARCH_FIELD = new Locator("SEARCH_FIELD", new By.ByXPath("//input[@id='sb-text-input']"));
    private static final Locator MOVE_TO_ADD_ROUTE_PAGE_BUTTON = new Locator("MOVE_TO_ADD_ROUTE_PAGE_BUTTON", new By.ByXPath("//button[contains(@href,'/admin/ferry/route-management/add')]"));
    private static final Locator PAGE = new Locator("PAGE", new By.ByXPath("//body"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[contains(text(), 'Save')]"));
    private static final Locator DURATION_FIELD = new Locator("DURATION_FIELD", new By.ByXPath("//input[@name='durations.0.number']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", new By.ByXPath("//div[@class='_2m8F2q_notification_banner _2m8F2q_variant_success']"));
    private static final Locator FERRY_ROUTE_ORIGIN_CELL = new Locator("FERRY_ROUTE_ORIGIN_CELL", new By.ByXPath("//td[@class='rc-table-cell'][3]"));
    private static final Locator FERRY_ROUTE_DESTINATION_CELL = new Locator("FERRY_ROUTE_DESTINATION_CELL", new By.ByXPath("//td[@class='rc-table-cell'][5]"));
    private static final Locator SELECTED_ORIGIN = new Locator("SELECTED_ORIGIN", new By.ByXPath("//div[@class='tix-dashboard-select'][.//p[text()='Origin']]//span"));
    private static final Locator SELECTED_DESTINATION = new Locator("SELECTED_DESTINATION", new By.ByXPath("//div[@class='tix-dashboard-select'][.//p[text()='Destination']]//span"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", new By.ByCssSelector("a[href*='route-management/']"));
    private static final Locator ORIGIN_DROPDOWN = new Locator("ORIGIN_DROPDOWN", new By.ByXPath("(//div[@class='tix-dashboard-select'])[1]"));
    private static final Locator DESTINATION_DROPDOWN = new Locator("DESTINATION_DROPDOWN", new By.ByXPath("(//div[@class='tix-dashboard-select'])[2]"));
    private static final Locator SHIP_DROPDOWN = new Locator("SHIP_DROPDOWN", new By.ByXPath("(//div[@class='tix-dashboard-select'])[3]"));
    private static final Locator MENU_CUSTOM_OPTIONS = new Locator("MENU_CUSTOM_OPTIONS", new By.ByXPath("//div[@class='menu-custom-options']"));
    private static final Locator ACCEPT_BUTTON = new Locator("ACCEPT_BUTTON", new By.ByXPath("//button[@class='KV-nSG_button KV-nSG_full_width KV-nSG_horizontal_padding KV-nSG_variant_primary']"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("(//span[@class='HcPVsG_text HcPVsG_variant_activity'][normalize-space()='Delete'])[1]"));
    private static final Locator ACTIVE_SWITCH = new Locator("ACTIVE_SWITCH", new By.ByXPath("(//div[@class='n8SXEW_toggle n8SXEW_toggle_small'])[1]"));

    public FerryRoutePage(Session<T> session) {
        super(session);
    }

    public void goToFerryPage(String username, String password) {
        driverTools.maximizeWindow();

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/member");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/member");
        }

        if (!isFerryLoggedIn()) {
            FerryLoginPage<?> ferryLoginPage = new FerryLoginPage<>(session);
            ferryLoginPage.loginToFerryPage(username, password);
            driverTools.waitForElementToAppear(USERNAME_GREETING, 2);

            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get("https://gatotkaca.tiket.com/admin/ferry");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driverTools.get("https://preprod.tiket.com/admin/ferry");
            }
        }
    }

    public boolean isFerryLoggedIn() {
        return driverTools.waitForElementToAppear(USERNAME_GREETING,2).isPresent();
    }

    public void goToRouteTab() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/ferry/route-management");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/ferry/route-management");
        }
    }

    public void goToAddRoutePage() {
        driverTools.waitForElementToAppear(MOVE_TO_ADD_ROUTE_PAGE_BUTTON);
        driverTools.clickWithJavascriptExecutor(MOVE_TO_ADD_ROUTE_PAGE_BUTTON);
    }

    public void goToEditRoutePage() {
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_BUTTON);
    }

    public void addNewRoute(int index, int altIndex, String duration) {
        selectOptionByIndex(ORIGIN_DROPDOWN, index);
        selectOptionByIndex(DESTINATION_DROPDOWN, altIndex);
        selectOptionByIndex(SHIP_DROPDOWN, index);
        driverTools.sendKeys(DURATION_FIELD, duration);
    }

    public void saveForm() {
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    private void selectOptionByIndex(Locator dropdown, int index) {
        driverTools.waitForElementToAppear(dropdown);
        driverTools.click(dropdown);

        if (driverTools.waitForElementToAppear(MENU_CUSTOM_OPTIONS, 2).isEmpty()) {
            try {
                driverTools.clickWithJavascriptExecutor(dropdown);
            } catch (Exception ignored) {
            }
        }

        Locator optionLocator = new Locator("DROPDOWN_OPTION", new By.ByXPath(String.format("//div[@class='wrapper-checkbox selected'][%d] | //div[@class='wrapper-checkbox'][%d] | (//div[@class='radio'])[%d]", index, index, index)));
        java.util.Optional<WebElement> option = driverTools.waitForElementToAppear(optionLocator, 3);

        if (option.isEmpty()) {
            Locator fallbackOptions = new Locator("DROPDOWN_OPTIONS_LIST", new By.ByXPath("//div[contains(@class,'wrapper-checkbox')] | //div[contains(@class,'radio')]") );
            var options = driverTools.waitForElementsToAppear(fallbackOptions, 3);
            if (options.isPresent()) {
                List<WebElement> elems = options.get();
                try {
                    driverTools.click(elems.get(index - 1));
                } catch (Exception e) {
                    driverTools.clickWithJavascriptExecutor(elems.get(index - 1));
                }
            } else {
                driverTools.sendKeys(PAGE, Keys.ESCAPE.toString());
                driverTools.waitForElementToDisappear(MENU_CUSTOM_OPTIONS, 2);
                return;
            }
        } else {
            try {
                driverTools.click(optionLocator);
            } catch (Exception e) {
                driverTools.waitForElementToAppear(optionLocator, 3);
                driverTools.click(optionLocator);
            }
        }

        driverTools.sendKeys(PAGE, Keys.ESCAPE.toString());
        driverTools.waitForElementToDisappear(MENU_CUSTOM_OPTIONS, 3);
    }

    public boolean isSuccessSnackbarDisplayedWithCorrectMessage(String successMessage) {
        WebElement snackbar = driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10).orElse(null);
        return snackbar != null && driverTools.getText(snackbar).contains(successMessage);
    }

    public boolean verifyRouteIsAtTheTop(String origin) {
        return driverTools.getText(getFirstSRPItemOrigin()).contains(origin);
    }

    public WebElement getFirstSRPItemOrigin() {
        driverTools.waitForElementToAppear(FERRY_ROUTE_ORIGIN_CELL, 3);
        return driverTools.firstElement(FERRY_ROUTE_ORIGIN_CELL).get();
    }

    public String getSelectedOrigin(){
        WebElement selectedOrigin = driverTools.waitForElementToAppear(SELECTED_ORIGIN).get();
        return driverTools.getText(selectedOrigin);
    }

    public String getSelectedDestination(){
        WebElement selectedDestination = driverTools.waitForElementToAppear(SELECTED_DESTINATION).get();
        return driverTools.getText(selectedDestination);
    }

    public boolean verifySRPContainsRouteOrigin(String text) {
        return verifySRPContainsText(FERRY_ROUTE_ORIGIN_CELL, text);
    }

    public boolean verifySRPContainsRouteDestination(String text) {
        return verifySRPContainsText(FERRY_ROUTE_DESTINATION_CELL, text);
    }

    private boolean verifySRPContainsText(Locator cellLocator, String text) {
        String target = text == null ? "" : text.toLowerCase();
        List<WebElement> elements = driverTools.elements(cellLocator).orElse(new ArrayList<>());
        if (!elements.isEmpty()) {
            for (WebElement element : elements) {
                String elementText = driverTools.getText(element);
                if (elementText == null) {
                    continue;
                }
                if (elementText.toLowerCase().contains(target)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void clearSearchField() {
        WebElement searchField = driverTools.element(SEARCH_FIELD).get();
        driverTools.clearWithJsExecutor(searchField);
    }

    public void searchFerryRoute(String origin) {
        int maxRetries = 5;
        searchFerryRouteWithRetries(origin, maxRetries);
    }

    private void searchFerryRouteWithRetries(String origin, int retriesLeft) {
        if (retriesLeft == 0) {
            throw new RuntimeException("Exceeded maximum retries for searchFerryRoute.");
        }

        clearSearchField();
        WebElement searchInput = driverTools.waitForElementToAppear(SEARCH_FIELD).get();
        try {
            driverTools.sendKeys(searchInput, origin);
            driverTools.sendKeys(searchInput, Keys.RETURN.toString());
        } catch (org.openqa.selenium.StaleElementReferenceException ex) {
            searchFerryRouteWithRetries(origin, retriesLeft - 1);
        }
    }

    public String getRouteId(){
        WebElement element = driverTools.element(EDIT_BUTTON).get();
        String href = driverTools.getValueAttribute(element, "href");
        return href.substring(href.lastIndexOf('/') + 1);
    }

    public String getRouteObjectId(){
        String id = getRouteId();
        return String.format("ObjectId('%s')",id);
    }

    public List<String> getFerryMongoData(String configType, String dataBase, String collectionName, String searchKey, String id) {
        String queryString = String.format("{'%s':%s, 'deletedAt' : null}", searchKey, id);
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase, collectionName, queryString);
        List<String> mongoData = new ArrayList<>();
        for (Document document : result) {
            mongoData.add(document.getObjectId(searchKey).toString());
        }
        return mongoData;
    }

    public boolean verifyTheMongoDataHasCorrectID(List<String> mongoData, String expectedId) {
        return mongoData.stream().allMatch(name -> name.contains(expectedId));
    }

    public boolean verifyTheSRPIsNotEmpty() {
        List<WebElement> classLabels = driverTools.waitForElementsToAppear(FERRY_ROUTE_ORIGIN_CELL, 3).orElse(null);
        return classLabels != null && !classLabels.isEmpty();
    }

    public void editRoute(int index, int altIndex, String duration) {
        selectOptionByIndex(ORIGIN_DROPDOWN, index);
        selectOptionByIndex(DESTINATION_DROPDOWN, altIndex);
        driverTools.sendKeys(DURATION_FIELD, Keys.COMMAND + "a");
        driverTools.sendKeys(DURATION_FIELD, duration);
    }

    public void toggleActiveRoute() {
        waitForSnackbarToDisappear();
        WebElement activateSwitch = driverTools.waitForElementToAppear(ACTIVE_SWITCH).get();
        driverTools.clickWithJavascriptExecutor(activateSwitch);
        WebElement acceptButton = driverTools.waitForElementToAppear(ACCEPT_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(acceptButton);
    }

    public void deleteRoute() {
        driverTools.waitForElementToAppear(DELETE_BUTTON);
        driverTools.click(DELETE_BUTTON);
        driverTools.waitForElementToAppear(ACCEPT_BUTTON);
        driverTools.click(ACCEPT_BUTTON);
    }

    public void waitForSnackbarToDisappear() {
        driverTools.waitForElementToDisappear(SUCCESS_SNACKBAR, 10);
    }

    public boolean verifyTheMongoDataIsEmpty(String configType, String dataBase, String collectionName, String searchKey, String id){
        return getFerryMongoData(configType, dataBase, collectionName, searchKey, id).isEmpty();
    }
}