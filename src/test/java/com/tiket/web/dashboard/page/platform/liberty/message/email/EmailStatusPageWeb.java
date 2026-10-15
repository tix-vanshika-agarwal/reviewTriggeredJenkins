package com.tiket.web.dashboard.page.platform.liberty.message.email;

import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;

public class EmailStatusPageWeb<T extends WebDriver> extends WebPage<T> {
    public EmailStatusPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> EmailStatusPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new EmailStatusPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new EmailStatusPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator TEMPLATE_NAME_INPUT = new Locator("TEMPLATE_NAME_INPUT", By.xpath("//input[@data-testid='template-name-bulk']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[contains(@class,'submit_filter')][.//span[normalize-space()='Search']]"));
    private static final Locator STATUS_FILTER_INPUT = new Locator("STATUS_FILTER_INPUT", By.xpath("//div[@data-testid='select-options-type']"));
    private static final Locator STATUS_FILTER_LABELS = new Locator("STATUS_FILTER_LABELS", By.xpath("//div[./span and ./label]/span"));
    private static final Locator STATUS_FILTER_SELECTIONS = new Locator("STATUS_FILTER_SELECTIONS", By.xpath("//div[./span and ./label]//input"));

    private static final Locator CONFIRMATION_POPUP = new Locator("CONFIRMATION_POPUP", By.xpath("//section[.//h2[text()='Confirmation']]"));
    private static final Locator CONFIRMATION_POPUP_APPROVE_BUTTON = new Locator("CONFIRMATION_POPUP_APPROVE_BUTTON", By.xpath("//section[.//h2[normalize-space()='Confirmation']]//button[.//span[normalize-space()='approve']]"));

    public void goToPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {driver.get("https://gatotkaca.tiket.com/admin/message/email/status");}
            case PREPROD -> {driver.get("https://preprod.tiket.com/admin/message/email/status");}
            case PROD -> {driver.get("https://www.tiket.com/admin/message/email/status");}
        }
        driverTools.waitForWebToLoad();
    }

    public void searchWithTemplateName(String template) {
        driverTools.waitForElementToAppear(TEMPLATE_NAME_INPUT, 2);
        driverTools.click(TEMPLATE_NAME_INPUT);
        driverTools.sendKeys(TEMPLATE_NAME_INPUT, template);
        driverTools.click(SEARCH_BUTTON);
        driverTools.dummyWait(2);
    }

    public boolean validateFirstRowStatusWithTemplateNameAndRequestor(String template, String requestor, String status) {
        Locator rowStatus = new Locator("ROW", By.xpath("//table/tbody/tr[./td[1][text()='" + template + "'] and ./td[2][text()='" + requestor + "']]/td[6]"));
        return driverTools.getText(rowStatus).orElseThrow().equalsIgnoreCase(status);
    }

    public void setStatusFilter(String status) {
        if (!status.equalsIgnoreCase("all")) {
            driverTools.click(STATUS_FILTER_INPUT);
            List<WebElement> labels = driverTools.waitForElementsToAppear(STATUS_FILTER_LABELS).orElse(null);
            if (labels == null || labels.isEmpty()) throw new NoSuchElementException("No selections for status");

            List<WebElement> selections = driverTools.waitForElementsToAppear(STATUS_FILTER_SELECTIONS).orElse(null);
            if (selections == null || selections.isEmpty()) throw new NoSuchElementException("No selections for status");

            for (int i = 0; i < labels.size(); i++) {
                if (driverTools.getText(labels.get(i)).equalsIgnoreCase("all")) driverTools.clickElementByCoordinateDWeb(selections.get(i));
                if (driverTools.getText(labels.get(i)).equalsIgnoreCase(status)) driverTools.clickElementByCoordinateDWeb(selections.get(i));
            }
        }
        driverTools.clickElementByCoordinateDWeb(STATUS_FILTER_INPUT);
        driverTools.click(SEARCH_BUTTON);
        if (!status.equalsIgnoreCase("all")) {
            driverTools.waitForElementToAppear(new Locator("FILTERED_STATUS_CELL", By.xpath("//tbody/tr/td[normalize-space()='" + status + "']")), 20);
        }
    }

    public void clickApproveButtonWithBulkId(String bulkId) {
        Locator approveButtonLoc = new Locator("APPROVE_BUTTON_" + bulkId, By.xpath("//td[text()='" + bulkId + "']/following-sibling::td//div[@data-testid='approve-modal']"));
        driverTools.clickElementByCoordinateDWeb(approveButtonLoc);
    }

    public String getRandomBulkId() {
        int bulkIdIndex = getIndexOfTableColumn("Bulk ID");
        List<WebElement> ids = driverTools.waitForElementsToAppear(
                new Locator("IDS", By.xpath("//tbody/tr/td[" + bulkIdIndex + "]"))
        ).orElse(null);

        if (ids == null || ids.isEmpty()) throw new NoSuchElementException("No bulk emails available");

        int randomIndex = CoreFunctions.generateRandomInt(0, ids.size()-1);

        return driverTools.getText(ids.get(randomIndex));
    }

    public void approveConfirmation() {
        driverTools.waitForElementToAppear(CONFIRMATION_POPUP, 2);
        driverTools.click(CONFIRMATION_POPUP_APPROVE_BUTTON);
    }

    public boolean verifyStatusWithBulkId(String status, String id) {
        driverTools.refreshWebpage();
        setStatusFilter(status);
        int statusIndex = getIndexOfTableColumn("Status");

        int rowIndex = driverTools.getDriver().findElements(
                By.xpath("//tr[./td[text()='" + id + "']]/preceding-sibling::tr")
        ).size() + 1;

        Locator statusLoc = new Locator("BULK_STATUS", By.xpath("//table/tbody/tr[" + rowIndex + "]/td[" + statusIndex + "]"));
        return driverTools.getText(statusLoc).orElseThrow().equalsIgnoreCase(status);
    }

    private int getIndexOfTableColumn(String columnTitle) {
        List<WebElement> cols = driverTools.getDriver().findElements(By.xpath("//th[./div[text()='" + columnTitle + "']]/preceding-sibling::th"));
        return cols.size() + 1;
    }
}
