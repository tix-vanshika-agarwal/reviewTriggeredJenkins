package com.tiket.web.dashboard.page.payment;

import com.mongodb.client.FindIterable;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.bson.Document;
import org.json.JSONObject;
import org.openqa.selenium.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class DefaultCardRouting<T extends WebDriver> extends BasePage<T> {

    private static final String paymentPath = "/admin/payment_v2";
    private static final Locator TITLE_DEFAULT_CARD_ROUTING = new Locator("TITLE_DEFAULT_CARD_ROUTING", By.xpath("//h1[contains(text(),'Default Card Routing')]"));
    private static final Locator LOADING = new Locator("LOADING", By.xpath("//*[@data-testid='loading']"));
    private static final Locator CARD_CONFIG_SIDEMENU = new Locator("CARD_CONFIG_SIDEMENU", By.xpath("//*[@data-testid='sidemenu']//*[contains(text(),'Card Payment Configuration')]"));
    private static final Locator DEFAULT_CARD_ROUTING_SUBMENU = new Locator("DEFAULT_CARD_ROUTING_SUBMENU", By.xpath("//*[contains(@data-testid,'submenu')]//*[contains(text(),'Default Card Routing')]"));
    private static final Locator SNACKBAR_ACTION = new Locator("SNACKBAR_ACTION", By.xpath("//*[contains(@class,'snackbar')]//*[contains(@class,'text_content')]"));
    private static final Locator MODAL_BODY_TITLE = new Locator("MODAL_BODY_TITLE", By.xpath("//*[contains(@class,'modal_body')]//h2"));
    private static final Locator YES_ACTIVATE_BTN = new Locator("YES_ACTIVATE_BTN", By.xpath("//button[normalize-space(text())='Yes, activate']"));
    private static final Locator YES_CHANGE_BTN = new Locator("YES_CHANGE_BTN", By.xpath("//button[normalize-space(text())='Yes, change']"));
    private static final Locator CANT_DEACTIVATE_HEADER = new Locator("CANT_DEACTIVATE_HEADER", By.xpath("//h2[contains(text(), \"Can't deactivate\")]"));
    private static final Locator OK_BTN = new Locator("OK_BTN", By.xpath("//button[normalize-space(text())='OK']"));
    private static final Locator YES_DEACTIVATE_BTN = new Locator("YES_DEACTIVATE_BTN", By.xpath("//button[normalize-space(text())='Yes, deactivate']"));
    private Locator getActionColumnByPgName(String pgName, String columnName) {
        return new Locator("COLUMN_"+columnName.toUpperCase()+"_ONPG_"+pgName.toUpperCase(), By.xpath("//*[contains(text(),'"+pgName+"')]/ancestor::*[contains(@data-testid,'pgName')]/following-sibling::*[contains(@data-testid,'"+columnNameToTestId.get(columnName)+"')]//label"));
    }
    private Locator getColumnByPgName(String pgName, String columnName) {
        return new Locator("COLUMN_"+columnName.toUpperCase()+"_ONPG_"+pgName.toUpperCase(), By.xpath("//*[contains(text(),'"+pgName+"')]/ancestor::*[contains(@data-testid,'pgName')]/following-sibling::*[contains(@data-testid,'"+columnNameToTestId.get(columnName)+"')]"));
    }
    private Locator getRoutedPg(String columnName) {
        return new Locator("ROUTED_PG_"+columnName.toUpperCase(), By.xpath("//input[@checked]/ancestor::*[contains(@data-testid,'"+columnNameToTestId.get(columnName)+"')]/preceding-sibling::*[contains(@data-testid,'pgName')]"));
    }
    private Locator modalFooterButton(String button) {
        return new Locator("MODAL_BUTTON_"+button.toUpperCase(), By.xpath("//*[contains(@class,'modal_footer')]//button[contains(text(),'"+button+"')]"));
    }
    private String getRowXPath(String gatewayName) {
        return String.format("//tr[.//td[contains(@data-testid, '-pgName')]//p[normalize-space(text())='%s']]", gatewayName);
    }

    private final Map<String, String> columnNameToTestId = Map.of(
            "Payment Gateway", "pgName",
            "Default IDR", "defaultIdr",
            "Default Non IDR", "defaultNonIdr",
            "Latest Update", "latestUpdate",
            "Status", "status",
            "Gateway Activation", "action"
    );


    public DefaultCardRouting(Session<T> session) { super(session); }

    public boolean isDefaultCardRoutingPageOpened() {
        if (!driverTools.waitForElementToDisappear(LOADING)) {
            logger.log("Element not disappeared or even not appeared");
        }
        return driverTools.waitForElementToPresence(TITLE_DEFAULT_CARD_ROUTING, 3).isPresent();
    }

    public void openDefaultCardRoutingPage(int waitSec) {
        if (!isDefaultCardRoutingPageOpened()) {
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
            WebElement cardRouting = driverTools.waitForElementToPresence(DEFAULT_CARD_ROUTING_SUBMENU).orElseThrow();
            js.executeScript("arguments[0].click();", cardRouting);
            driverTools.waitForElementToPresence(TITLE_DEFAULT_CARD_ROUTING, 3);
        }
    }

    public String getRoutedPgByColumn(String column) {
        driverTools.refreshWebpage();
        WebElement defaultPg = driverTools.waitForElementToPresence(getRoutedPg(column), 2).orElseThrow();
        return driverTools.getText(defaultPg);
    }

    public void clickOnInputColumnByPg(String pgName, String column) {
        driverTools.click(getActionColumnByPgName(pgName, column));
    }

    public void clickOnModalFooterButton(String button) {
        driverTools.scrollIntoWebView(modalFooterButton(button));
        driverTools.click(modalFooterButton(button));
    }

    public String routePg(String pgName, String column) {
        logger.log("Routing currency "+column+" to PG "+pgName);
        clickOnInputColumnByPg(pgName, column);
        clickOnModalFooterButton("Yes, change");
        WebElement snackbar = driverTools.waitForElementToPresence(SNACKBAR_ACTION, 2).orElseThrow();
        return driverTools.getText(snackbar);
    }

    public String togglePgActivation(String pgName, String action) {
        driverTools.waitForElementToDisappear(SNACKBAR_ACTION, 5);
        logger.log("Toggling "+action+" PG -> "+pgName);
        clickOnInputColumnByPg(pgName, "Gateway Activation");
        Assertion.assertThat("Verify modal title content", getModalBodyContent(), equalToIgnoringCase(action+" "+pgName+"?"));
        clickOnModalFooterButton("Yes, "+action.toLowerCase());
        WebElement snackbar = driverTools.waitForElementToPresence(SNACKBAR_ACTION, 2).orElseThrow();
        return driverTools.getText(snackbar);
    }

    public String getColumnValueByPg(String pgName, String column) {
        WebElement status = driverTools.waitForElementToPresence(getColumnByPgName(pgName, column)).orElseThrow();
        logger.log("Getting value from column "+column+" by PG "+pgName);
        return driverTools.getText(status);
    }

    public String getModalBodyContent() {
        driverTools.click(MODAL_BODY_TITLE);
        return driverTools.getText(MODAL_BODY_TITLE).orElse("");
    }

    public List<Document> getDocumentOnCollectionPgConfig(String queryString) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB("mongo", "tix_creditcard", "pg_config", queryString);
        List<Document> listResult = new ArrayList<>();
        result.into(listResult);
        return listResult;
    }

    public String getFieldPromoDocument(Document data, String fieldName){
        Object value = data.get(fieldName);
        String valueString = "";
        if (value instanceof Date) {
            Date date = (Date) value;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            valueString = dateFormat.format(date);
        }else {
            valueString = String.valueOf(value);
        }
        logger.log("Got value: "+valueString+", from field name: "+fieldName);
        return valueString;
    }

    public String getGatewayStatus(String gatewayName) {
        Locator statusLoc = new Locator(gatewayName + "_STATUS", By.xpath(getRowXPath(gatewayName) + "//td[contains(@data-testid, '-status')]//div"));
        driverTools.waitForElementToAppear(statusLoc, 10).orElseThrow();
        return driverTools.getText(statusLoc).orElse("").trim();
    }

    public void toggleGatewayAction(String gatewayName) {
        Locator actionToggleLoc = new Locator(gatewayName + "_ACTION_TOGGLE", By.xpath(getRowXPath(gatewayName) + "//td[contains(@data-testid, '-action')]//input[@type='checkbox']"));
        driverTools.clickWithJavascriptExecutor(actionToggleLoc);
    }

    public void clickModalYesActivate() {
        driverTools.waitForElementToBeClickable(YES_ACTIVATE_BTN, 5).orElseThrow();
        driverTools.click(YES_ACTIVATE_BTN);
        driverTools.waitForElementToDisappear(YES_ACTIVATE_BTN, 5);
        driverTools.dummyWait(2); // Wait for API calls/React rerenders to settle
    }

    public boolean isGatewayDefaultIdr(String gatewayName) {
        Locator defaultIdrLoc = new Locator(gatewayName + "_DEFAULT_IDR", By.xpath(getRowXPath(gatewayName) + "//td[contains(@data-testid, '-defaultIdr')]//input[@type='checkbox']"));
        WebElement checkbox = driverTools.element(defaultIdrLoc).orElseThrow();
        return Boolean.TRUE.equals(
                ((JavascriptExecutor) driverTools.getDriver()).executeScript("return arguments[0].checked === true;", checkbox)
        );
    }

    public void setGatewayDefaultIdr(String gatewayName) {
        Locator defaultIdrLoc = new Locator(gatewayName + "_DEFAULT_IDR", By.xpath(getRowXPath(gatewayName) + "//td[contains(@data-testid, '-defaultIdr')]//input[@type='checkbox']"));
        driverTools.clickWithJavascriptExecutor(defaultIdrLoc);
    }

    public void clickModalYesChange() {
        driverTools.waitForElementToBeClickable(YES_CHANGE_BTN, 5).orElseThrow();
        driverTools.click(YES_CHANGE_BTN);
        driverTools.waitForElementToDisappear(YES_CHANGE_BTN, 5);
    }

    public boolean isCantDeactivateModalDisplayed() {
        WebElement errorHeader = driverTools.waitForElementToAppear(CANT_DEACTIVATE_HEADER, 5).orElse(null);
        return errorHeader != null && errorHeader.isDisplayed();
    }

    public void clickModalOk() {
        driverTools.click(OK_BTN);
        driverTools.waitForElementToDisappear(OK_BTN, 5);
    }

    public void clickModalYesDeactivate() {
        driverTools.waitForElementToBeClickable(YES_DEACTIVATE_BTN, 5).orElseThrow();
        driverTools.click(YES_DEACTIVATE_BTN);
        driverTools.waitForElementToDisappear(YES_DEACTIVATE_BTN, 5);
    }

    public String extractAcsUrlSafely(JSONObject responseJson) {
        if (responseJson == null) {
            return "";
        }
        JSONObject dataObj = responseJson.optJSONObject("data");
        if (dataObj == null) {
            return "";
        }
        JSONObject thirdPartyResponse = dataObj.optJSONObject("thirdPartyResponse");
        if (thirdPartyResponse == null) {
            return "";
        }
        JSONObject innerData = thirdPartyResponse.optJSONObject("data");
        if (innerData == null) {
            return "";
        }
        return innerData.optString("acsUrl", "");
    }

    public void verifySnackbarMessage(String action, String gatewayName) {
        String expectedText = "Successfully " + action + " " + gatewayName + ".";
        Locator snackbarLoc = new Locator("SNACKBAR_MSG", By.xpath("//div[contains(@class, 'dcXu0G_text_content')]"));
        WebElement snackbar = driverTools.waitForElementToAppear(snackbarLoc, 5).orElseThrow(() -> new NoSuchElementException("Snackbar did not appear within 5 seconds."));
        String actualText = snackbar.getText().trim();
        Assertion.assertThat("Verify snackbar message", actualText, is(equalTo(expectedText)));
        driverTools.waitForElementToDisappear(snackbarLoc, 5);
    }

    public String getLatestUpdateTimestamp(String gatewayName) {
        String xpath = String.format("//tr[.//td[contains(@data-testid, '-pgName')]//p[normalize-space(text())='%s']]//td[contains(@data-testid, '-latestUpdate')]", gatewayName);
        Locator latestUpdateLoc = new Locator("LATEST_UPDATE_" + gatewayName, By.xpath(xpath));
        WebElement element = driverTools.waitForElementToAppear(latestUpdateLoc, 5).orElseThrow(
                () -> new NoSuchElementException("Could not find the Latest Update column for gateway: " + gatewayName)
        );
        return element.getText().replace("\n", " ").trim();
    }

    public void activateGatewayIfInactive(String gateway) {
        String currentStatus = getGatewayStatus(gateway);
        if (currentStatus.equalsIgnoreCase("Inactive")) {
            String timeBeforeUpdate = getLatestUpdateTimestamp(gateway);
            toggleGatewayAction(gateway);
            clickModalYesActivate();
            verifySnackbarMessage("activate", gateway);
            String timeAfterUpdate = getLatestUpdateTimestamp(gateway);
            String updatedStatus = getGatewayStatus(gateway);

            Assertion.assertThat("Verify " + gateway + " is now Active", updatedStatus, is(equalToIgnoringCase("Active")));
            Assertion.assertThat("Verify the Latest Update timing changed for " + gateway, timeAfterUpdate, is(not(equalTo(timeBeforeUpdate))));
        }
        else {
            Assertion.assertThat(gateway + " is already in active state", currentStatus, is(equalToIgnoringCase("Active")));
        }
    }
}
