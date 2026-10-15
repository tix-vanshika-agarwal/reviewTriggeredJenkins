package com.tiket.web.dashboard.page.platform.liberty.message.inbox;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

public class InboxPNStatusPageWeb<T extends WebDriver> extends WebPage<T> {
    public InboxPNStatusPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> InboxPNStatusPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new InboxPNStatusPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new InboxPNStatusPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator TEMPLATE_NAME_INPUT = new Locator("TEMPLATE_NAME_INPUT", By.xpath("//input[@data-testid='input-search-filter']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[contains(@class,'submit_filter')][.//span[normalize-space()='Search']]"));
    private static final Locator ROWS = new Locator("ROWS", By.cssSelector("tr[data-testid='row-table']"));
    private static final Locator DOWNLOAD_TSV_BUTTON = new Locator("DOWNLOAD_TSV_BUTTON", By.xpath("//div[normalize-space()='Download TSV']"));

    public void goToPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {driver.get("https://gatotkaca.tiket.com/admin/message/inboxpn/status");}
            case PREPROD -> {driver.get("https://preprod.tiket.com/admin/message/inboxpn/status");}
            case PROD -> {driver.get("https://www.tiket.com/admin/message/inboxpn/status");}
        }
        driverTools.waitForWebToLoad();
    }

    public void searchTemplate(String template) {
        driverTools.click(TEMPLATE_NAME_INPUT);
        driverTools.sendKeys(TEMPLATE_NAME_INPUT, template);
        driverTools.click(SEARCH_BUTTON);
        driverTools.dummyWait(2);
    }

    public boolean verifyStatus(String template, LocalDateTime requestDate, String tsvContent) {
        tsvContent = tsvContent.replace("\\n", "\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy - HH:mm");
        String formattedRequestDate = requestDate.format(formatter);
        logger.log("To be verified Request Date : " + formattedRequestDate);

        int templateCodeTableIndex = getTheIndexOfTableColumn("Template Code");
        int requestDateTableIndex = getTheIndexOfTableColumn("Date of Request");
        int actionTableIndex = getTheIndexOfTableColumn("Action");

        List<WebElement> rows = driverTools.waitForElementsToAppear(ROWS).orElseThrow();
        List<WebElement> templates = driverTools.waitForElementsToAppear(new Locator("TEMPLATE_VALUE", By.xpath("//tr[@data-testid='row-table']/td[" + templateCodeTableIndex+ "]"))).orElseThrow();
        List<WebElement> requestDates = driverTools.waitForElementsToAppear(new Locator("REQUEST_DATE_VALUE", By.xpath("//tr[@data-testid='row-table']/td[" + requestDateTableIndex+ "]"))).orElseThrow();
        List<WebElement> moreButtons = driverTools.waitForElementsToAppear(new Locator("MORE_BUTTON", By.xpath("//tr[@data-testid='row-table']/td[" + actionTableIndex + "]//div[@data-testid='btn-more']"))).orElseThrow();

        boolean fail = true;
        for (int i = 0; i < rows.size(); i++) {
            if (driverTools.getText(templates.get(i)).equals(template) &&
                    driverTools.getText(requestDates.get(i)).equals(formattedRequestDate)
            ) {
                fail=false;
                driverTools.click(moreButtons.get(i));
                break;
            }
        }
        if (fail) throw new NoSuchElementException();

        driverTools.click(DOWNLOAD_TSV_BUTTON);
        driverTools.dummyWait(2);

        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        String filePath = CoreFunctions.getLastModified(downloadDir).getAbsolutePath();

        try {
            String fileContent = Files.readString(Paths.get(filePath));
            logger.log("Expected TSV Content: \n" + tsvContent);
            logger.log("Actual TSV Content: \n" + fileContent);
            if (!fileContent.equals(tsvContent)) {
                throw new IllegalArgumentException("TSV Content is different from the expected content");
            }
        } catch (IOException io) {
            throw new RuntimeException();
        }
        return true;
    }

    private int getTheIndexOfTableColumn(String colTitle) {
        List<WebElement> elements = driverTools.getDriver().findElements(By.xpath("//th[contains(normalize-space(),'" + colTitle + "')]/preceding-sibling::th"));
        return elements.size() + 1;
    }

}
