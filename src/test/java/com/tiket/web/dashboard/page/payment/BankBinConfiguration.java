package com.tiket.web.dashboard.page.payment;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.tiket.api.payment.BankBinApi;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.payment.BankBinResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;

import lombok.SneakyThrows;

import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BankBinConfiguration<T extends WebDriver> extends BasePage<T> {
    private static final Gson gson = new Gson();
    private static final String paymentPath = "/admin/payment_v2";
    private static final Locator BIN_CONFIG_TITLE = new Locator("BIN_CONFIG_TITLE", By.xpath("//h1[contains(text(),'BIN Configuration')]"));
    private static final Locator LOADING = new Locator("LOADING", By.xpath("//*[@data-testid='loading']"));
    private static final Locator BIN_LIST_LOADING = new Locator("BIN_LIST_LOADING", By.xpath("//*[@data-testid='list-loading-indicator']"));
    private static final Locator IMAGE_EMPTY_TABLE = new Locator("IMAGE_EMPTY_TABLE", By.xpath("//div[contains(@class, 'tab_panel_active')]//*[@data-testid='table-control']//*[contains(@src,'empty-table')]"));
    private static final Locator TITLE_EMPTY_TABLE = new Locator("TITLE_EMPTY_TABLE", By.xpath("//div[contains(@class, 'tab_panel_active')]//*[@data-testid='table-control']//*[contains(text(),'no data')]"));
    private static final Locator SIDEMENU = new Locator("SIDEMENU", By.xpath("//*[@data-testid='sidemenu']"));
    private static final Locator CARD_CONFIG_SIDEMENU = new Locator("CARD_CONFIG_SIDEMENU", By.xpath("//*[@data-testid='sidemenu']//*[contains(text(),'Card Payment Configuration')]"));
    private static final Locator BANK_CONFIG_SUBMENU = new Locator("BANK_CONFIG_SUBMENU", By.xpath("//*[contains(@data-testid,'submenu')]//*[contains(text(),'BIN Configuration')]"));
    private static final Locator INPUT_SEARCHBY = new Locator("INPUT_SEARCHBY", By.xpath("//*[@data-testid='response-code-field']"));
    private static final Locator SNACKBAR_ACTION = new Locator("SNACKBAR_ACTION", By.xpath("//*[contains(@class,'snackbar')]//*[contains(@class,'text_content')]"));
    private static final Locator POPUP_CHANGELOG_OF = new Locator("POPUP_CHANGELOG_OF", By.xpath("//*[contains(@class,'modal_body')]//*[contains(text(),'Changes log of')]"));
    private static final Locator SEARCH_BY_BIN = new Locator("SEARCH_BY_BIN", By.xpath("//*[@id='bin']"));
    
    private Locator getButtonByText(String button) {
        return new Locator("BUTTON_"+button.toUpperCase(), By.xpath("//button[contains(text(),'"+button+"')]"));
    }
    private Locator getTabByText(String tabName) {
        return new Locator("TAB_"+tabName.toUpperCase(), By.xpath("//*[contains(text(),'"+tabName+"')]/parent::button"));
    }
    private Locator getDropdownByLabel(String label) {
        return new Locator("DROPDOWN_"+label.toUpperCase(), By.xpath("//*[@data-testid='dropdown-btn-default']//label[contains(text(),'"+label+"')]/preceding-sibling::input"));
    }
    private Locator getDropdownItem(String item) {
        return new Locator("DROPDOWN_ITEM_"+item.toUpperCase(), By.xpath("//*[@data-testid='dropdown-item-"+item+"']"));
    }
    private Locator getInputFieldByLabel(String label) {
        return new Locator("INPUT_FIELD_"+label.toUpperCase(), By.xpath("//following-sibling::label[contains(text(),'"+label+"')]/preceding-sibling::*[@data-testid='response-code-field']"));
    }
    private Locator getCellByRowAndColumn(String rowIndex, String columnName) {
        return new Locator("COLUMN_"+columnName.toUpperCase()+"_CELL_"+rowIndex, By.xpath("//*[@data-testid='row-cell-"+rowIndex+"-"+columnNameToTestId.get(columnName)+"']//p"));
    }
    private Locator getCellByRowAndColumnChangeLog(String rowIndex, String columnName) {
        return new Locator("CHANGELOG_COLUMN_"+columnName.toUpperCase()+"_CELL_"+rowIndex, By.xpath("//*[@data-testid='row-cell-"+rowIndex+"-"+columnChangelogToTestId.get(columnName)+"']"));
    }
    private Locator changelogPopupBinData(String columnId) {
        return new Locator("CHANGELOG_POPUP_COLUMN_"+columnId, By.xpath("//*[contains(@class,'modal_body')]//*[contains(@id,'cell-"+columnId+"')]"));
    }
    private final Map<String, String> columnNameToTestId = Map.of(
            "Bank Name", "issuer",
            "Bank Alias", "issuerAlias",
            "Country", "countryCode",
            "BIN", "bin",
            "Card Type", "cardType",
            "Card Name", "cardName",
            "Whitelabel", "whiteLabel",
            "Action", "action",
            "BIN Range Min", "binMin",
            "BIN Range Max", "binMax"
    );

    private final Map<String, String> filterSearchByToTestId = Map.of(
            "Bank Name", "Bank_Name",
            "Bank Alias", "Bank_Alias",
            "BIN", "BIN",
            "Card Type", "Card_Type"
    );

    private final Map<String, String> columnChangelog = Map.of(
            "Date", "1",
            "Type", "2",
            "Old Value", "3",
            "New Value", "4",
            "Admin", "5"
    );

    private final Map<String, String> columnChangelogToTestId = Map.of(
            "Date", "updatedDate",
            "Type", "type",
            "Old Value", "oldValue",
            "New Value", "newValue",
            "Admin", "updatedBy"
    );

    public BankBinConfiguration(Session<T> session) {
        super(session);
    }

    public boolean isBankBinConfigPageOpened() {
        if (!driverTools.waitForElementToDisappear(LOADING)) {
            logger.log("Element not disappeared or even not appeared");
        }
        driverTools.waitForElementToDisappear(BIN_LIST_LOADING, 5);
        return driverTools.waitForElementToPresence(BIN_CONFIG_TITLE, 3).isPresent();
    }

    public void openBankBinConfigPage() {
        if (!isBankBinConfigPageOpened()) {
            Deeplink deeplink = new Deeplink("ADMIN_CARE", "https://gatotkaca.tiket.com", "https://preprod.tiket.com", "https://tiket.com");
            String url = switch (Configs.ENVIRONMENT) {
                    case PROD -> deeplink.prodDeeplink()+paymentPath;
                    case PREPROD -> deeplink.preprodDeeplink()+paymentPath;
                    case GK -> deeplink.gkDeeplink()+paymentPath;
                    default -> deeplink.gkDeeplink()+paymentPath;
            };
            driverTools.get(url);
            if (!driverTools.getCurrentURL().contains(url)) Assertion.skip("Failed to open payment dashboard "+url);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement cardConfig = driverTools.waitForElementToPresence(CARD_CONFIG_SIDEMENU).orElseThrow();
            js.executeScript("arguments[0].click();", cardConfig);
            WebElement bankConfig = driverTools.waitForElementToPresence(BANK_CONFIG_SUBMENU).orElseThrow();
            js.executeScript("arguments[0].click();", bankConfig);
            driverTools.waitForElementToDisappear(BIN_LIST_LOADING, 5);
            driverTools.waitForElementToPresence(BIN_CONFIG_TITLE, 3);
        }
    }

    public void refreshPage() {
        driverTools.refreshWebpage();
        if (!isBankBinConfigPageOpened()) {
            Assertion.skip("Bank BIN Configuration page not opened");
        }
    }

    public boolean isEmptyBinList() {
        driverTools.waitForElementToDisappear(BIN_LIST_LOADING, 5);
        return driverTools.waitForElementToPresence(IMAGE_EMPTY_TABLE, 4).isPresent() || driverTools.waitForElementToPresence(TITLE_EMPTY_TABLE, 4).isPresent();
    }

    public void clickButton(String button) {
        driverTools.click(getButtonByText(button));
    }

    public void clickTab(String tabName) {
        driverTools.click(getTabByText(tabName));
    }

    public void clickButtonUsingJsExecutor(String button) {
        driverTools.waitForElementToBeClickable(getButtonByText(button), 4);
        driverTools.clickWithJavascriptExecutor(getButtonByText(button));
    }

    public boolean isDisabledButton(String button) {
        return Boolean.parseBoolean(driverTools.getValueAttribute(getButtonByText(button), "disabled"));
    }

    public boolean isDisabledInputField(String label) {
        return Boolean.parseBoolean(driverTools.getValueAttribute(getInputFieldByLabel(label), "disabled"));
    }

    public void clickOnDropdownByLabel(String label) {
        driverTools.click(getDropdownByLabel(label));
    }

    public void clickOnDropdownItem(String item) {
        driverTools.scrollIntoWebView(getDropdownItem(item));
        driverTools.click(getDropdownItem(item));
    }

    public void clickOnDropdownItemUsingActions(String item) {
        driverTools.scrollIntoWebView(getDropdownItem(item));
        driverTools.clickWithActions(getDropdownItem(item));
    }

    public String getDropdownValueOnFieldByLabel(String label) {
        WebElement field = driverTools.waitForElementToPresence(getDropdownByLabel(label)).orElseThrow();
        return driverTools.getValue(field);
    }

    public void inputOnFieldByLabel(String label, String message) {
        driverTools.click(getInputFieldByLabel(label));
        driverTools.clearInput(driverTools.element(getInputFieldByLabel(label)).orElseThrow());
        driverTools.sendKeys(getInputFieldByLabel(label), message);
    }

    public String getInputValueOnFieldByLabel(String label) {
        WebElement field = driverTools.waitForElementToPresence(getInputFieldByLabel(label)).orElseThrow();
        return driverTools.getValue(field);
    }

    public void inputFilterSearchBy(String searchBy, String message) {
        logger.log("Filter search by "+searchBy+" for input "+message);
        clickOnDropdownByLabel("Search By");
        clickOnDropdownItem(filterSearchByToTestId.get(searchBy));
        driverTools.click(INPUT_SEARCHBY);
        driverTools.sendKeys(INPUT_SEARCHBY, message);
        clickButton("Search");
        driverTools.waitForElementToDisappear(BIN_LIST_LOADING);
    }

    public String getRowValueFromColumn(String rowIndex, String columnName) {
        driverTools.waitForElementToDisappear(BIN_LIST_LOADING, 5);
        WebElement cell = driverTools.waitForElementToPresence(getCellByRowAndColumn(rowIndex, columnName)).orElseThrow();
        return driverTools.getText(cell);
    }

    public void clickActionRow(String rowIndex, String action) {
        driverTools.waitForElementToDisappear(BIN_LIST_LOADING, 5);
        driverTools.click(getCellByRowAndColumn(rowIndex, "Action"));
        driverTools.click(getDropdownItem(action));
    }

    public void clickActionRowThenScroll(String rowIndex, String action) {
        driverTools.waitForElementToDisappear(BIN_LIST_LOADING, 5);
        driverTools.clickWithActions(getCellByRowAndColumn(rowIndex, "Action"));
        driverTools.waitForElementToPresence(getDropdownItem(action), 10);
        driverTools.scrollIntoWebView(getDropdownItem(action));
        driverTools.click(getDropdownItem(action));
    }

    public String getSnackbarText() {
        WebElement snackbar = driverTools.waitForElementToPresence(SNACKBAR_ACTION, 2).orElseThrow();
        return driverTools.getText(snackbar);
    }

    public List<Document> getDocumentOnCollectionPgConfig(String queryString) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB("mongo", "tix_payment_core", "bank_bin_list", queryString);
        List<Document> listResult = new ArrayList<>();
        result.into(listResult);
        return listResult;
    }

    public void openChangelogOnBinList(String rowIndex) {
        String bin = driverTools.getText(getCellByRowAndColumn(rowIndex, "BIN")).orElse("");
        driverTools.click(getCellByRowAndColumn(rowIndex, "BIN"));
        String popupChangelog = driverTools.getText(POPUP_CHANGELOG_OF).orElse("");
        if (!popupChangelog.contains(bin)) {
            Assertion.fail("incorrect popup changes log of BIN, expected BIN: "+bin+", actual BIN: "+popupChangelog);
        }
    }

    public String getChangelogPopupBinData(String columnName) {
        return driverTools.waitForElementToPresence(changelogPopupBinData(columnChangelog.get(columnName))).map(driverTools::getText).orElseThrow();
    }

    public String getRowValueFromColumnBinChangeLog(String rowIndex, String columnName) {
        return driverTools.waitForElementToPresence(getCellByRowAndColumnChangeLog(rowIndex, columnName)).map(driverTools::getText).orElseThrow();
    }

    @SneakyThrows
    public BankBinResponse getBankBinFromApi(String bin, String currency, String deviceId, String lang, String countryCode) {
        BankBinApi.Inputs inputs = new BankBinApi.Inputs(bin, currency, deviceId, lang, countryCode);
        BankBinApi bankBinApi = new BankBinApi(inputs);
        driverTools.dummyWait(10);
        ApiResponse apiResponse = bankBinApi.hitApi(apiTools);
        logger.log("debug : " + apiResponse.getBody());      
        return gson.fromJson(apiResponse.getBody(), BankBinResponse.class);
    }

    public void inputSearchByBIN(String bin) {
        driverTools.waitForElementToAppear(SEARCH_BY_BIN,10);
        driverTools.click(SEARCH_BY_BIN);
        driverTools.sendKeys(SEARCH_BY_BIN, bin + Keys.ENTER);
        driverTools.waitForElementToDisappear(BIN_LIST_LOADING);
    }
}
