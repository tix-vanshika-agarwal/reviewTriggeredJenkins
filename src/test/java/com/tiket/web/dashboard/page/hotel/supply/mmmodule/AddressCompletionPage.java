package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.mongodb.client.FindIterable;
import com.opencsv.CSVWriter;
import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddressCompletionPage <T extends WebDriver> extends BasePage<T> {
    public AddressCompletionPage(Session<T> session) {
        super(session);
    }

    private static final Locator UPLOAD_BUTTON = new Locator("UPLOAD_BUTTON", new By.ByXPath("//button[@data-testid=\"button-upload-csv\"]"));
    private static final Locator UPLOAD_FILE_MODAL_TITLE = new Locator("UPLOAD_FILE_MODAL_TITLE", new By.ByXPath("//section//h2[text()='Upload file']"));
    private static final Locator UPLOAD_FILE_INPUT = new Locator("UPLOAD_FILE_INPUT", new By.ByXPath("//input[@data-testid=\"dropzone-input\"]"));
    private static final Locator UPLOAD_FILE_NAME_LABEL = new Locator("UPLOAD_FILE_NAME_LABEL", new By.ByXPath("//div[contains(@class,'upload_dropzone')]//span[contains(@class,'Text_weight_bold')]"));
    private static final Locator UPLOAD_FILE_SUBMIT_BUTTON = new Locator("UPLOAD_FILE_SUBMIT_BUTTON", new By.ByXPath("//section//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator UPLOAD_FILE_SUCCESSFUL_SNACKBAR = new Locator("UPLOAD_FILE_SUCCESSFUL_SNACKBAR", new By.ByXPath("//div[contains(@class,'Snackbar_type_success')]"));
    private static final Locator LATEST_ACTIVITY_TABLE = new Locator("LATEST_ACTIVITY_TABLE", new By.ByXPath("//span[@data-id='activeMenu' and normalize-space()='Address Completion']"));
    private static final Locator LATEST_ACTIVITY_LAST_UPDATED_DATE_LABEL = new Locator("LATEST_ACTIVITY_LAST_UPDATED_DATE_LABEL", new By.ByXPath("//div[@data-testid=\"address-completion-test-status-date\"]"));
    private static final Locator LATEST_ACTIVITY_STATUS_LABEL = new Locator("LATEST_ACTIVITY_STATUS_LABEL", new By.ByXPath("//div[@data-testid=\"address-completion-test-status\"]/span"));
    
    public Document addressCompletionData = new Document();

    public boolean isAddressCompletionPageDisplayed() {
        return driverTools.waitForElementToAppear(LATEST_ACTIVITY_TABLE).isPresent();
    }

    public void clickUpload() {
        WebElement button = driverTools.waitForElementToBeClickable(UPLOAD_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isUploadFileModalAppeared() {
        return driverTools.waitForElementToAppear(UPLOAD_FILE_MODAL_TITLE).isPresent();
    }

    public void uploadFile(File file) {
        WebElement input = driverTools.waitForElementToPresence(UPLOAD_FILE_INPUT).orElseThrow();
        driverTools.sendKeys(input, file.getAbsolutePath());
    }

    public boolean isFileSelected(String fileName) {
        WebElement label = driverTools.waitForElementToAppear(UPLOAD_FILE_NAME_LABEL).orElseThrow();
        return driverTools.getText(label).contains(fileName);
    }

    public void submitFile() {
        WebElement button = driverTools.waitForElementToBeClickable(UPLOAD_FILE_SUBMIT_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isSuccessfulSnackbarAppeared() {
        return driverTools.waitForElementToAppear(UPLOAD_FILE_SUCCESSFUL_SNACKBAR).isPresent();
    }

    public boolean isLastUploadedDateUpdated() {
        WebElement label = driverTools.waitForElementToAppear(LATEST_ACTIVITY_LAST_UPDATED_DATE_LABEL).orElseThrow();
        String actualDate = driverTools.getText(label);
        return actualDate.contains(CoreFunctions.getCurrentDateYYYYMMDDWithSlash());
    }

    public boolean isLastUploadedDateUpdatedOnDB(String configType, String dbName, String collectionName, String queryString, String sortBy) {
        WebElement label = driverTools.waitForElementToAppear(LATEST_ACTIVITY_LAST_UPDATED_DATE_LABEL).orElseThrow();
        String actualDate = driverTools.getText(label);
        return actualDate.contains(getAddressCompletionDataFromDB(configType, dbName, collectionName, queryString, sortBy));
    }

    public boolean isStatusDisplayedCorrectly(String expectedStatus) {
        WebElement label = driverTools.waitForElementToAppear(LATEST_ACTIVITY_STATUS_LABEL).orElseThrow();
        boolean result = driverTools.getText(label).equals(expectedStatus);
        if (!result) {
            driver.navigate().refresh();
            label = driverTools.waitForElementToAppear(LATEST_ACTIVITY_STATUS_LABEL).orElseThrow();
            result = driverTools.getText(label).equals(expectedStatus);
        }
        return result;
    }

    public File generateAddressCompletionCSVFile(String fileName, String[] hotelHashId) throws Exception {
        File file = new File("src/main/resources/" + fileName);
        FileWriter fw = new FileWriter(file);
        CSVWriter csvWriter = new CSVWriter(fw, ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
        String[] header = {"hotel hash id"};
        List list = new ArrayList<>();
        list.add(header);

        for (int i=0; i< hotelHashId.length; i++) {
            String[] hotelId = {hotelHashId[i]};
            list.add(hotelId);
        }

        csvWriter.writeAll(list);
        csvWriter.flush();
        return file;
    }

    private String getAddressCompletionDataFromDB(String configType, String dbName, String collectionName, String queryString, String sortBy){
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDBWithSortLimit(configType, dbName, collectionName, queryString, sortBy, 1);
        addressCompletionData = result.first();
        Date updatedDate = addressCompletionData.getDate("updatedDate");
        String pattern = "yyyy/MM/dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String formattedUpdatedDate = simpleDateFormat.format(updatedDate);
        return formattedUpdatedDate;
    }

    public void deleteFile(File file) {
        file.delete();
    }

    public boolean isFileExist(File file) {
        return file.exists();
    }
}
