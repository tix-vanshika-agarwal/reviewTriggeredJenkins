package com.tiket.web.dWeb.page.account;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InboxPage<T extends WebDriver> extends BasePage<T> {

    public InboxPage(Session<T> session) {
        super(session);
    }

    private static final Locator INBOX_TITLE_ITEM = new Locator("INBOX_TITLE_ITEM", new By.ByXPath("//*[contains(@data-testid,'inboxItem')]//*[contains(@class,'inbox_title')]//h2"));
    private static final Locator INBOX_SUBTITLE_ITEM = new Locator("INBOX_SUBTITLE_ITEM", new By.ByXPath("//*[contains(@data-testid,'inboxItem')]//*[contains(@class,'inbox_subtitle')]"));
    private static final Locator INBOX_TIME_ITEM = new Locator("INBOX_TIME_ITEM", new By.ByXPath("//*[contains(@data-testid,'inboxItem')]//*[contains(@class,'inbox_title')]/span[last()] | //*[contains(@data-testid,'inboxItem')]//*[contains(@class,'inbox_header')]/p"));

    public String getInboxTitle(int index) {
        driverTools.waitForElementToAppear(INBOX_TITLE_ITEM);
        List<String> allTitleItem = driverTools.getTexts(INBOX_TITLE_ITEM);
        return allTitleItem.get(index);
    }

    public String getInboxSubtitle(int index) {
        driverTools.waitForElementToAppear(INBOX_SUBTITLE_ITEM);
        List<String> allSubtitleItem = driverTools.getTexts(INBOX_SUBTITLE_ITEM);
        return allSubtitleItem.get(index);
    }

    public int getEarnedPointsFromInbox(int index) {
        String subtitle = getInboxSubtitle(index);
        Matcher matcher = Pattern.compile("(\\d+)\\s*point", Pattern.CASE_INSENSITIVE).matcher(subtitle);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        throw new IllegalStateException("No earned points found in inbox message: " + subtitle);
    }

    public String getInboxTime(int index) {
        driverTools.waitForElementToAppear(INBOX_TIME_ITEM);
        List<String> allTimeItem = driverTools.getTexts(INBOX_TIME_ITEM);
        return allTimeItem.get(index).trim();
    }

    public boolean isInboxTimeCloseToNow(int index, int toleranceMinutes) {
        String inboxTime = getInboxTime(index);
        LocalTime notificationTime = LocalTime.parse(inboxTime, DateTimeFormatter.ofPattern("H:mm"));
        LocalTime now = getBrowserTime();
        long diffMinutes = Math.abs(Duration.between(notificationTime, now).toMinutes());
        diffMinutes = Math.min(diffMinutes, 24 * 60 - diffMinutes);
        logger.log("Inbox time: " + inboxTime + ", now(browser): "
                + now.format(DateTimeFormatter.ofPattern("HH:mm")) + ", diffMinutes: " + diffMinutes);
        return diffMinutes <= toleranceMinutes;
    }

    private LocalTime getBrowserTime() {
        Object result = ((JavascriptExecutor) driver)
                .executeScript("var d = new Date(); return d.getHours() + ':' + d.getMinutes();");
        return LocalTime.parse(String.valueOf(result), DateTimeFormatter.ofPattern("H:m"));
    }

    public void clickInbox(int index) {
        driverTools.waitForElementToAppear(INBOX_TITLE_ITEM);
        List<WebElement> allTitleItem = driverTools.elements(INBOX_TITLE_ITEM).orElseThrow();
        driverTools.click(allTitleItem.get(index));
    }

    public boolean isChatRelatedNotification(String notificationTitle, String hotelName) {
        String lowerTitle = notificationTitle.toLowerCase();
        return lowerTitle.contains("message") || 
               lowerTitle.contains("chat") || 
               lowerTitle.contains("pesan") ||
               lowerTitle.contains("hotel") ||
               lowerTitle.contains(hotelName.toLowerCase()) ||
               lowerTitle.contains("partner") ||
               lowerTitle.contains("reply") ||
               lowerTitle.contains("balasan");
    }
}
