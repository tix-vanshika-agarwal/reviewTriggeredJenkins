package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TransactionListPage <T extends WebDriver> extends BasePage<T> {
    private static final Locator SEE_FULL_REPORT=new Locator("SEE_FULL_REPORT", new By.ByXPath("//button//span[contains(text(),'See full')]"));
    private static final Locator DOWNLOAD_FULL_REPORT=new Locator("DOWNLOAD_FULL_REPORT", new By.ByXPath("//button//span[contains(text(),'Download')]"));
    private static final Locator DOWNLOAD_CSV=new Locator("DOWNLOAD_CSV", new By.ByXPath("//button//span[contains(text(),'CSV')]"));
    private static final Locator PROCESSING_FILE=new Locator("PROCESSING_FILE", new By.ByXPath("//div[contains(text(),'Processing the file')]"));
    private static final Locator SNACKBAR_TEXT=new Locator("SNACKBAR_TEXT", new By.ByXPath("//div[contains(@class,'Snackbar_text')]"));


    public TransactionListPage(Session<T> session) {super(session);}

    public boolean isTransactionListPageOpened(){
        driverTools.waitForElementToAppear(SEE_FULL_REPORT,2);
        return driverTools.isElementDisplayed(SEE_FULL_REPORT);
    }

    public void downloadTransactionList(){
        driverTools.click(SEE_FULL_REPORT);

        driverTools.waitForElementToAppear(DOWNLOAD_FULL_REPORT,2);
        driverTools.click(DOWNLOAD_FULL_REPORT);

        driverTools.waitForElementToAppear(DOWNLOAD_CSV,2);
        driverTools.click(DOWNLOAD_CSV);

    }

    public boolean isProcessingFileVisible(){
        driverTools.waitForElementToAppear(PROCESSING_FILE,5);
        return driverTools.isElementDisplayed(PROCESSING_FILE);
    }

    public String getTextFromSnackbar(){
        driverTools.waitForElementToAppear(SNACKBAR_TEXT);
        return driverTools.getText(SNACKBAR_TEXT).orElseThrow();
    }
}
