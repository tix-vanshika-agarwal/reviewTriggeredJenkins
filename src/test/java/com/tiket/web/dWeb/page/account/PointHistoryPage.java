package com.tiket.web.dWeb.page.account;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PointHistoryPage<T extends WebDriver> extends BasePage<T> {

    public PointHistoryPage(Session<T> session) {
        super(session);
    }

    private static final Locator FIRST_TRANSACTION_DATE = new Locator("FIRST_TRANSACTION_DATE", new By.ByXPath("(//*[contains(@class,'child__description')]//*[contains(@class,'group__date')])[1]"));
    private static final Locator FIRST_TRANSACTION_INFO = new Locator("FIRST_TRANSACTION_INFO", new By.ByXPath("(//*[contains(@class,'child__description')]//*[contains(@class,'group__info')])[1]"));
    private static final Locator FIRST_TRANSACTION_POINT = new Locator("FIRST_TRANSACTION_POINT", new By.ByXPath("(//*[contains(@class,'child__description')]/parent::*//*[contains(@class,'point')])[1]"));

    public boolean verifyFirstTransaction(int expectedPoints, String vertical) {
        String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US));
        String actualDate = driverTools.getText(driverTools.waitForElementToAppear(FIRST_TRANSACTION_DATE, 15).orElseThrow()).trim();
        String actualInfo = driverTools.getText(driverTools.waitForElementToAppear(FIRST_TRANSACTION_INFO, 15).orElseThrow()).trim();
        int actualPoints = Integer.parseInt(driverTools.getText(driverTools.waitForElementToAppear(FIRST_TRANSACTION_POINT, 15).orElseThrow()).replaceAll("[^\\d]", ""));

        boolean dateMatch = expectedDate.equals(actualDate);
        boolean pointsMatch = actualPoints == expectedPoints;
        boolean verticalMatch = actualInfo.toLowerCase().contains(vertical.toLowerCase());
        
        logger.log("Point-history date -> actual: " + actualDate + ", expected: " + expectedDate + ", match=" + dateMatch);
        logger.log("Point-history points -> actual: " + actualPoints + ", expected: " + expectedPoints + ", match=" + pointsMatch);
        logger.log("Point-history vertical -> info: '" + actualInfo + "', expected vertical: '" + vertical + "', match=" + verticalMatch);

        return dateMatch && pointsMatch && verticalMatch;
    }
}
