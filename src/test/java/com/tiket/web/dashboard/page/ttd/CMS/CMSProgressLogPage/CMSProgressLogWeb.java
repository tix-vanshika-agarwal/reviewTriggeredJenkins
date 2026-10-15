package com.tiket.web.dashboard.page.ttd.CMS.CMSProgressLogPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.TimeZone;

public class CMSProgressLogWeb<T extends WebDriver> extends WebPage<T> {
    public CMSProgressLogWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSProgressLogWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSProgressLogDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSProgressLogMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator FIRST_COMPLETED_EXPORT_SEATMAP = new Locator("FIRST_COMPLETED_EXPORT_SEATMAP", By.xpath("(//tr[td/div[contains(text(), 'Export Seatmap')] and td//span[contains(@class, 'status-chip') and text()='Completed']])[1]"));
    private static final Locator FIRST_DOWNLOAD = new Locator("FIRST_DOWNLOAD", By.xpath("//small[text()='Download']"));
    private static final Locator TYPE_CHECK = new Locator("TYPE_CHECK", By.xpath("//tr[@class='table-v2_body-list normal']//td[1]"));
    private static final Locator STATUS_CHECK = new Locator("STATUS_CHECK", By.xpath("//tr[@class='table-v2_body-list normal']//td[4]//span[@class='status-chip complete']"));
    private static final Locator DOWNLOAD_CHECK = new Locator("STATUS_CHECK", By.xpath("//tr[@class='table-v2_body-list normal']//td[6]//small[contains(@class, 'story-icon-title')]"));
    private static final Locator REQUEST_TIME_STAMP = new Locator("REQUEST_TIME_STAMP", By.xpath("//tr[@class='table-v2_body-list normal']//td[3]//div[@style='cursor: default;']"));

    public void goToProgressLogPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {driver.get("https://gatotkaca.tiket.com/admin/event-ms/to-do/activity-log/progress-log");}
            case PREPROD -> {driver.get("https://preprod.tiket.com/admin/event-ms/to-do/activity-log/progress-log");}
            case PROD -> {driver.get("https://www.tiket.com/admin/event-ms/to-do/activity-log/progress-log");}
        }
        driverTools.waitForWebToLoad();
    }

    public boolean isSeatMapExportCompleted(){
        driverTools.waitForElementToAppear(FIRST_COMPLETED_EXPORT_SEATMAP);
        return driverTools.isElementDisplayed(FIRST_COMPLETED_EXPORT_SEATMAP);
    }

    public void clickOnDownload() {
        int maxRetries = 3;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            if (driverTools.waitForElementToAppear(FIRST_DOWNLOAD, 10).isPresent()) {
                driverTools.clickWithJavascriptExecutor(FIRST_DOWNLOAD);
                return;
            }
            driver.navigate().refresh();
            driverTools.waitForWebToLoad();
        }
        driverTools.clickWithJavascriptExecutor(FIRST_DOWNLOAD);
    }

    private boolean verifyType(Locator Locator, String type) {
        Optional<String> resultText = driverTools.getText(Locator);
        if(driverTools.isElementDisplayed(Locator)) {
            return resultText.isPresent() && resultText.get().equals(type);
        }
        return false;
    }

    public boolean verifyTheContent(String verifyType) {
        return verifyType(TYPE_CHECK, verifyType);
    }
    public boolean verifyStatus(String verifyType) {
        return verifyType(STATUS_CHECK, verifyType);
    }

    public boolean verifyDownload() {
        return driverTools.isElementDisplayed(DOWNLOAD_CHECK);
    }

    public boolean verifyTimestampWithinLast2Minutes() {
        ZonedDateTime jakartaTime = ZonedDateTime.now(ZoneId.of("Asia/Jakarta"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy - HH:mm:ss");
        String verifyDateAndTime = jakartaTime.format(formatter);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy - HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));

        Date verifyTime;
        try {
            verifyTime = sdf.parse(verifyDateAndTime);
        } catch (ParseException e) {
            throw new AssertionError("Error parsing verifyDateAndTime: " + e.getMessage());
        }

        Optional<String> timestamp1 = driverTools.getText(REQUEST_TIME_STAMP);
        if(timestamp1.isPresent()) {
            String timestampText = timestamp1.get().trim();

            Date webElementTimestamp;
            try {
                webElementTimestamp = sdf.parse(timestampText);
            } catch (ParseException e) {
                throw new AssertionError("Error parsing WebElement timestamp: " + e.getMessage());
            }

            long diffInMillis = verifyTime.getTime() - webElementTimestamp.getTime();
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);

            return Math.abs(diffInMinutes) <= 2;
        }
        return false;
    }
}
