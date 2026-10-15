package com.tiket.web.dashboard.page.payment;

import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CustomPaymentRoutingPage<T extends WebDriver> extends BasePage<T> {

    private static final String paymentPath = "/admin/payment_v2";

    private static final java.util.Map<String, String> PG_RULE_PATTERN = java.util.Map.of(
            "CCPP", "ccpp",
            "Xendit", "xendit",
            "Nicepay", "nicepay"
    );

    private String getPgRowXPath(String pgName) {
        String pattern = PG_RULE_PATTERN.get(pgName).toLowerCase();
        return "//tr[starts-with(@data-testid,'row-key-')]"
                + "[.//td[contains(@data-testid,'ruleName')]"
                + "//p[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), '" + pattern + "')]]";
    }

    private Locator getActionDropdown(String pgName) {
        return new Locator("ACTION_DROPDOWN_" + pgName.toUpperCase(),
                By.xpath(getPgRowXPath(pgName) + "//*[@data-testid='dropdown-btn-default']"));
    }

    private Locator getStatusCellLocator(String pgName) {
        return new Locator("STATUS_CELL_" + pgName.toUpperCase(),
                By.xpath(getPgRowXPath(pgName) + "//td[contains(@data-testid,'-status')]//div[contains(@class,'label')]"));
    }

    private static final Locator TITLE_CUSTOM_PAYMENT_ROUTING = new Locator("TITLE_CUSTOM_PAYMENT_ROUTING",By.xpath("//h1[contains(text(),'Custom Payment Routing')]"));
    private static final Locator LOADING = new Locator("LOADING", By.xpath("//*[@data-testid='loading']"));
    private static final Locator CARD_CONFIG_SIDEMENU = new Locator("CARD_CONFIG_SIDEMENU",By.xpath("//*[@data-testid='sidemenu']//*[contains(text(),'Card Payment Configuration')]"));
    private static final Locator CUSTOM_PAYMENT_ROUTING_SUBMENU = new Locator("CUSTOM_PAYMENT_ROUTING_SUBMENU",By.xpath("//*[contains(@data-testid,'submenu')]//*[contains(text(),'Custom Payment Routing')]"));
    private static final Locator SNACKBAR_ACTION = new Locator("SNACKBAR_ACTION",By.xpath("//*[contains(@class,'snackbar')]//*[contains(@class,'text_content')]"));
    private static final Locator DROPDOWN_EDIT_OPTION = new Locator("DROPDOWN_EDIT_OPTION",By.xpath("//*[@data-testid='dropdown-item-Edit']"));
    private static final Locator STATUS_TOGGLE = new Locator("STATUS_TOGGLE", By.xpath("//input[@type='checkbox']/following-sibling::div"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON",By.xpath("//button[@type='submit' and normalize-space()='Save']"));
    private static final Locator SAVE_CHANGES_BUTTON = new Locator("SAVE_CHANGES_BUTTON", By.xpath("//button[normalize-space()='Save Changes']"));

    public CustomPaymentRoutingPage(Session<T> session) {
        super(session);
    }

    public boolean isCustomPaymentRoutingPageOpened() {
        if (!driverTools.waitForElementToDisappear(LOADING)) {
            logger.log("Loading element not disappeared or not appeared");
        }
        return driverTools.waitForElementToPresence(TITLE_CUSTOM_PAYMENT_ROUTING, 3).isPresent();
    }

    public void openCustomPaymentRoutingPage() {
        if (!isCustomPaymentRoutingPageOpened()) {
            Deeplink deeplink = new Deeplink("ADMIN_CARE", "https://gatotkaca.tiket.com", "https://preprod.tiket.com", "https://tiket.com");
            String url = switch (Configs.ENVIRONMENT) {
                case PROD -> deeplink.prodDeeplink() + paymentPath;
                case PREPROD -> deeplink.preprodDeeplink() + paymentPath;
                case GK -> deeplink.gkDeeplink() + paymentPath;
                default -> deeplink.gkDeeplink() + paymentPath;
            };
            driverTools.get(url);
            if (!driverTools.getCurrentURL().contains(url)) Assertion.skip("Failed to open payment dashboard " + url);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement cardConfig = driverTools.waitForElementToPresence(CARD_CONFIG_SIDEMENU).orElseThrow();
            js.executeScript("arguments[0].click();", cardConfig);
            WebElement cprSubmenu = driverTools.waitForElementToPresence(CUSTOM_PAYMENT_ROUTING_SUBMENU).orElseThrow();
            js.executeScript("arguments[0].click();", cprSubmenu);
            driverTools.waitForElementToPresence(TITLE_CUSTOM_PAYMENT_ROUTING, 3);
        }
    }

    
    public void clickActionDropdown(String pgName) {
        driverTools.waitForElementToDisappear(SNACKBAR_ACTION, 5);
        Locator dropdown = getActionDropdown(pgName);
        driverTools.waitForElementToPresence(dropdown).orElseThrow(
                () -> new RuntimeException("Action dropdown not found for PG: " + pgName));
        driverTools.scrollIntoWebView(dropdown);
        driverTools.clickWithJavascriptExecutor(dropdown);
        driverTools.waitForElementToPresence(DROPDOWN_EDIT_OPTION, 5).orElseThrow(
                () -> new RuntimeException("Edit option not visible in dropdown for PG: " + pgName));
    }

    public void clickEditOption() {
        driverTools.waitForElementToPresence(DROPDOWN_EDIT_OPTION, 5);
        driverTools.click(DROPDOWN_EDIT_OPTION);
    }

    public void clickStatusToggle() {
        driverTools.waitForElementToPresence(STATUS_TOGGLE, 5).orElseThrow();
        driverTools.click(STATUS_TOGGLE);
    }

    public void saveStatusChange() {
        driverTools.scrollIntoWebView(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);

        driverTools.waitForElementToPresence(SAVE_CHANGES_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(SAVE_CHANGES_BUTTON);
        driverTools.waitForElementToPresence(SNACKBAR_ACTION, 5);
        driverTools.waitForElementToDisappear(SAVE_CHANGES_BUTTON, 5);
        driverTools.waitForElementToPresence(TITLE_CUSTOM_PAYMENT_ROUTING, 5);
    }

    public String getStatusText(String pgName) {
        Locator statusCell = getStatusCellLocator(pgName);
        long deadline = System.currentTimeMillis() + 10_000;
        while (System.currentTimeMillis() < deadline) {
            WebElement cell = driverTools.waitForElementToPresence(statusCell, 2).orElse(null);
            if (cell != null) {
                String text = driverTools.getText(cell).trim();
                if (!text.isEmpty()) {
                    return text;
                }
            }
            try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        throw new RuntimeException("Status cell text for PG " + pgName + " remained empty after timeout");
    }
}
