package com.tiket.web.dashboard.page.payment;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.bson.Document;
import org.openqa.selenium.*;

import java.io.File;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiCurrencyDashboard<T extends WebDriver> extends BasePage<T> {
    private static final Locator REFRESH_BUTTON = new Locator("REFRESH_BUTTON", By.xpath("//button[contains(text(), 'Refresh')]"));
    private static final Locator WAITING_BUTTON = new Locator("WAITING_BUTTON",By.xpath("//button[text()='Waiting...']"));
    private static final Locator REFRESH_MODAL_OK = new Locator("REFRESH_MODAL_OK",By.xpath("//button[contains(text(), 'Yes')]"));
    private static final Locator CURRENCY_CONFIGURATION = new Locator("CURRENCY_CONFIGURATION", By.xpath("//span[contains(text(),'Currency Configuration')]"));
    private static final Locator FX_RATE_MENU = new Locator("FX_RATE_MENU",By.xpath("//a[contains(text(),'FX Rate')]"));
    private static final Locator FROM_DROPDOWN = new Locator("FROM_DROPDOWN", By.xpath("(//input[@id='filter-single-dropdown-undefined-undefined'])[1]"));
    private static final Locator USER_CURRENCY = new Locator("USER_CURRENCY",By.xpath("//*[contains(text(),'s Currency')]"));
    private static final Locator CURRENCY_LIST = new Locator("CURRENCY_LIST",By.xpath("(//div[@data-tag='allowRowEvents'])[1]"));
    private static final Locator ALL_CURRENCY = new Locator("ALL_CURRENCY",By.xpath("//*[text()='All Currency']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON",By.xpath("//button[normalize-space()='Search']"));
    private static final Locator DROPDOWNTEXT = new Locator("DROPDOWNTEXT",By.xpath("//input[@placeholder='From:']"));
    private static final Locator CURRENCYINPUT = new Locator("CURRENCYINPUT",By.id("search"));
    private static final Locator TO_DROPDOWN = new Locator("TO_DROPDOWN",By.xpath("(//input[@id='filter-single-dropdown-undefined-undefined'])[2]"));
    private static final Locator TO_AUD_CURRENCY = new Locator("TO_AUD_CURRENCY",By.xpath("//p[text()='AUD']"));
    private static final Locator TO_IDR_CURRENCY = new Locator("TO_IDR_CURRENCY",By.xpath("//p[text()='IDR']"));
    private static final Locator RATE_INFO_BUY = new Locator("RATE_INFO_BUY",By.xpath("//span[contains(text(),'Buy')]"));
    private static final Locator RATE_INFO_SELL = new Locator("RATE_INFO_SELL",By.xpath("//span[contains(text(),'Sell')]"));
    private static final Locator SPAN_ELEMENT = new Locator("SPAN_ELEMENT",By.xpath("//*[contains(@class,'hGYzg')]"));
    private static final Locator COMPARISON_THRESHOLD = new Locator("COMPARISON_THRESHOLD",By.xpath("(//strong[text()='⋮'])[3]"));
    private static final Locator DEVIATION_THRESHOLD = new Locator("DEVIATION_THRESHOLD",By.xpath("(//strong[text()='⋮'])[2]"));
    private static final Locator USD = new Locator("USD",By.xpath("//div[@data-tag='allowRowEvents' and text()='USD']"));
    private static final Locator TEXT_BOX = new Locator("TEXT_BOX",By.xpath("//input[@data-testid='foreign-exchange-value-input']"));
    private static final Locator CHANGE_TEXT_BOX = new Locator("CHANGE_TEXT_BOX",By.xpath("//h2[contains(text(),'Change')]"));
    private static final Locator SAVE_ICON = new Locator("SAVE_ICON",By.xpath("//*[contains(text(),'Save')]"));
    private static final Locator OVERRIDE_OUT_OF_RANGE_DESC = new Locator("OVERRIDE_OUT_OF_RANGE_DESC",By.xpath("//div[contains(text(), 'Deviation check not passed')]"));
    private static final Locator DEVIATION_OUT_OF_RANGE_DESC = new Locator("DEVIATION_OUT_OF_RANGE_DESC",By.xpath("//span[contains(text(), 'Deviation threshold value that you fill')]"));
    private static final Locator COMPARISON_OUT_OF_RANGE_DESC = new Locator("COMPARISON_OUT_OF_RANGE_DESC",By.xpath("//span[contains(text(), 'Comparison threshold value that you fill')]"));
    private static final Locator YES_OVERRIDE_ICON = new Locator("YES_OVERRIDE_ICON", By.xpath("//button[contains(text(), 'Yes')]"));
    private static final Locator USD_CURRENCY = new Locator("USD_CURRENCY",By.xpath("//p[text()='USD']"));
    private static final Locator OVERRIDE_RATE = new Locator("OVERRIDE_RATE", By.xpath("(//strong[text()='⋮'])[1]"));
    private static final Locator USD_TO_IDR_RATE = new Locator("USD_TO_IDR_RATE",By.xpath("//*[@id='cell-3-undefined']"));
    private static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON",By.xpath("//button[contains(text(),'Close')]"));
    private static final Locator LOGIN_USERNAME = new Locator("LOGIN_USERNAME",By.xpath("//span[@data-testid='username']"));
    private static final Locator REFRESH_MODAL_INFO = new Locator("REFRESH_MODAL_INFO",By.xpath("//div[contains(@id,\"html\")]"));
    private static final Locator PAGINATION_BUTTON = new Locator("PAGINATION_BUTTON",By.xpath("//button[@id='pagination-next-page']"));
    private static final Locator COLUMN_ONE_ELEMENT_NAME = new Locator("COLUMN_ONE_ELEMENT_NAME",By.xpath("//div[contains(@class,'ableCell') and contains(@id,'1')]//div[text()='AUD']"));
    private static final Locator RESET_ICON = new Locator("RESET_ICON",By.xpath("//span[text()='↻']"));
    private static final Locator PLUS_ICON = new Locator("PLUS_ICON",By.xpath("//span[text()='+']"));
    private static final Locator DOWNLOAD_BUTTON = new Locator("DOWNLOAD_BUTTON",By.xpath("//button[contains(text(),'Download')]"));

    public MultiCurrencyDashboard(Session<T> session) {
        super(session);
    }
    public void maximize() {
        driver.manage().window().maximize();
    }

    public void multiCurrencyDashboard(){
        driverTools.waitForElementToAppear(CURRENCY_CONFIGURATION).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(CURRENCY_CONFIGURATION + " element not found");
        });
        driverTools.waitForElementToAppear(FX_RATE_MENU).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(FX_RATE_MENU + " element not found");
        });
    }

    public void refresh_MC_V1(){
        driverTools.waitForElementToAppear(REFRESH_BUTTON);
        driverTools.click(REFRESH_BUTTON);
        driverTools.waitForElementToAppear(REFRESH_MODAL_OK);
        driverTools.click(REFRESH_MODAL_OK);
    }

    public boolean isElementRefreshed(){
        driverTools.waitForElementToAppear(WAITING_BUTTON,3);
        return (driverTools.isElementDisplayed(WAITING_BUTTON));
    }

    public String getInfo(){
        WebElement buyInfoElement = driverTools.element(RATE_INFO_BUY).orElseThrow();
        String divText = buyInfoElement.getText();
        Matcher matcher = Pattern.compile("\\d[\\d,.]*").matcher(divText);
        if (matcher.find()) {
            String numericPart = matcher.group().replaceAll("[^\\d]", "");
            return numericPart;
        }
        return "-1";
    }

    public String getSellInfo(){
        WebElement buyInfoElement = driverTools.element(RATE_INFO_SELL).orElseThrow();
        String divText = buyInfoElement.getText();
        Matcher matcher = Pattern.compile("\\d[\\d,.]*").matcher(divText);
        if (matcher.find()) {
            String numericPart = matcher.group().replaceAll("[^\\d]", "");
            return numericPart;
        }
        return "-1";
    }

    public void clickUserCurrency(){
        driverTools.waitForElementToAppear(FROM_DROPDOWN);
        driverTools.click(FROM_DROPDOWN);
        driverTools.waitForElementToAppear(USER_CURRENCY);
        driverTools.click(USER_CURRENCY);
    }

    public String dropdownText(){
        return driverTools.element(DROPDOWNTEXT).get().getAttribute("value");
    }

    public void clickAllCurrencyFROM(){
        driverTools.waitForElementToAppear(FROM_DROPDOWN);
        driverTools.click(FROM_DROPDOWN);
        driverTools.waitForElementToAppear(ALL_CURRENCY);
        driverTools.click(ALL_CURRENCY);
    }

    public void confirmSearch() {
        driverTools.waitForElementToAppear(SEARCH_BUTTON).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(SEARCH_BUTTON + " element not found");
        });
    }

    public void searchCurrencyInput(String entry){
        WebElement inputField = driverTools.element(CURRENCYINPUT).orElseThrow();
        inputField.sendKeys(entry);
        inputField.sendKeys(Keys.ENTER);
    }

    public boolean isCurrencyElementDisplayed(){
        driverTools.waitForElementToAppear(CURRENCY_LIST);
        return driverTools.isElementDisplayed(CURRENCY_LIST);
    }

    public void selectInTODropdown(){
        driverTools.click(TO_DROPDOWN);
        driverTools.click(TO_AUD_CURRENCY);
    }

    public String IsselectInTODropdown(){
        return driverTools.element(TO_DROPDOWN).get().getAttribute("value");
    }

    public void selectToField(){
        driverTools.click(TO_DROPDOWN);
        driverTools.click(TO_IDR_CURRENCY);
    }

    public String getLength(){
        WebElement spanElement = driverTools.element(SPAN_ELEMENT).orElseThrow();
        String textContent = spanElement.getText();
        String[] parts = textContent.split(" ");
        String number = parts[parts.length - 1];
        return number;
    }

    public boolean selectUSDCurrency(){
        driverTools.click(FROM_DROPDOWN);
        driverTools.click(USD_CURRENCY);
        confirmSearch();
        return driverTools.waitForElementToAppear(USD).get().isDisplayed();
    }

    public String getComparisonThresholdValue(){
        driverTools.waitForElementToAppear(COMPARISON_THRESHOLD).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(COMPARISON_THRESHOLD + " element not found");
        });

        String panelValue = driverTools.element(TEXT_BOX).get().getAttribute("value");
        logger.log("Comparison Threshold Value: [" + panelValue + "]");

        return panelValue;
    }

    public boolean comparingComparisonThresholdValue(String value){
        driverTools.waitForElementToAppear(COMPARISON_THRESHOLD).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(COMPARISON_THRESHOLD + " element not found");
        });

        String panelValue = driverTools.element(TEXT_BOX).get().getAttribute("value");
        logger.log("Comparison Threshold Value: [" + panelValue + "]");
        return panelValue.contains(value);
    }

    public void refreshTheWebpage(){
        driverTools.refreshWebpage();
    }

    public void changeComparisonThresholdValue(String value){
        driverTools.waitForElementToAppear(COMPARISON_THRESHOLD).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(COMPARISON_THRESHOLD + " element not found");
        });
        driverTools.waitForElementToAppear(TEXT_BOX).orElseThrow();
        driverTools.clearInput(TEXT_BOX);
        driverTools.sendKeys(TEXT_BOX,value);
        driverTools.waitForElementToAppear(SAVE_ICON, 5).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(SAVE_ICON + " element not found");
        });
    }

    public String getVerificationMessageText(String caseName) {
        Locator targetLocator = switch (caseName) {
            case "Override"   -> OVERRIDE_OUT_OF_RANGE_DESC;
            case "Deviation"  -> DEVIATION_OUT_OF_RANGE_DESC;
            case "Comparison" -> COMPARISON_OUT_OF_RANGE_DESC;
            default -> throw new IllegalArgumentException("Invalid case name: " + caseName);
        };

        String actualMessage = driverTools.element(targetLocator).get().getAttribute("textContent");
        logger.log(caseName + " message: [" + actualMessage + "]");

        return actualMessage != null ? actualMessage : "";
    }

    public void clickOnYesOverride_icon(){
        driverTools.waitForElementToAppear(YES_OVERRIDE_ICON).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(YES_OVERRIDE_ICON + " element not found");
        });
    }

    public boolean getDeviationThresholdValue(String value){
        driverTools.waitForElementToAppear(DEVIATION_THRESHOLD).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(DEVIATION_THRESHOLD + " element not found");
        });

        String panelValue = driverTools.element(TEXT_BOX).get().getAttribute("value");
        logger.log("Deviation Threshold Value: [" + panelValue + "]");
        return panelValue.contains(value);
    }

    public String getOverrideRate(){
        return driverTools.getText(OVERRIDE_RATE).get();
    }

    public boolean compareOverrideRate(String value){
        return driverTools.getText(OVERRIDE_RATE).get().contains(value);
    }

    public String getUSDToIDRRate(){
        return driverTools.getText(USD_TO_IDR_RATE).get();
    }

    public void changeDeviationThresholdValue(String value){
        driverTools.click(DEVIATION_THRESHOLD);
        driverTools.waitForElementToAppear(TEXT_BOX);
        driverTools.clear(TEXT_BOX);
        driverTools.sendKeys(TEXT_BOX,value);
        driverTools.click(SAVE_ICON);
    }

    public void changeOverrideRate(String value){
        driverTools.click(OVERRIDE_RATE);
        driverTools.waitForElementToAppear(TEXT_BOX);
        driverTools.clearInput(TEXT_BOX);
        driverTools.sendKeys(TEXT_BOX,value);
        driverTools.waitForElementToAppear(SAVE_ICON, 5).ifPresent(driverTools::click);
        clickOnYesOverride_icon();
    }

    public boolean changeOverrideRateToDefaultValue(String defaultValue) {
        driverTools.click(OVERRIDE_RATE);
        String currentValue = getTextFromTextBox();
        if (currentValue.equals(defaultValue)) {
            clickClose();
            return false;
        }
        driverTools.clearInput(TEXT_BOX);
        driverTools.sendKeys(TEXT_BOX, defaultValue);
        driverTools.waitForElementToAppear(SAVE_ICON, 5).ifPresent(driverTools::click);
        return true;
    }

    public boolean changeDeviationToDefaultValue(String defaultValue) {
        driverTools.click(DEVIATION_THRESHOLD);
        String currentValue = getTextFromTextBox();
        if (currentValue.equals(defaultValue)) {
            clickClose();
            return false;
        }
        driverTools.clearInput(TEXT_BOX);
        driverTools.sendKeys(TEXT_BOX, defaultValue);
        driverTools.waitForElementToAppear(SAVE_ICON, 5).ifPresent(driverTools::click);
        return true;
    }

    public boolean changeComparisonToDefaultValue(String defaultValue) {
        driverTools.click(COMPARISON_THRESHOLD);
        String currentValue = getTextFromTextBox();
        if (currentValue.equals(defaultValue)) {
            clickClose();
            return false;
        }
        driverTools.clearInput(TEXT_BOX);
        driverTools.sendKeys(TEXT_BOX, defaultValue);
        driverTools.waitForElementToAppear(SAVE_ICON, 5).ifPresent(driverTools::click);
        return true;
    }

    public void resetAllFxRateToDefault(String overrideRate, String deviation, String comparison) {
        if (changeOverrideRateToDefaultValue(overrideRate)) clickOnYesOverride_icon();
        if (changeDeviationToDefaultValue(deviation)) clickOnYesOverride_icon();
        if (changeComparisonToDefaultValue(comparison)) clickOnYesOverride_icon();
    }

    public void resetOverrideToDefaultState(String defaultValue) {
        driverTools.waitForElementToAppear(PLUS_ICON).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(PLUS_ICON + " element not found");
        });
        driverTools.waitForElementToAppear(TEXT_BOX);
        driverTools.clearInput(TEXT_BOX);
        int randomValue = CoreFunctions.generateRandomInt(12000, 12252);
        driverTools.sendKeys(TEXT_BOX, String.valueOf(randomValue));
        if (driverTools.waitForElementToAppear(SAVE_ICON, 5).isPresent()) {
            driverTools.click(SAVE_ICON);
            clickOnYesOverride_icon();
        }
        if (changeOverrideRateToDefaultValue(defaultValue)) {
            clickOnYesOverride_icon();
        }
    }

    public void changeOverrideRateAlphabetic(String value){
        WebElement overrideRate = driverTools.element(OVERRIDE_RATE).orElseThrow();
        driverTools.click(overrideRate);
        WebElement textBox = driverTools.waitForElementToAppear(TEXT_BOX).orElseThrow();
        driverTools.clearInput(textBox);
        driverTools.sendKeys(textBox,value);
        driverTools.waitForElementToAppear(CHANGE_TEXT_BOX).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(CHANGE_TEXT_BOX + " element not found");
        });
    }

    public void changeDeviationRateAlphabetic(String value){
        WebElement deviationRate = driverTools.element(DEVIATION_THRESHOLD).orElseThrow();
        driverTools.click(deviationRate);
        WebElement textBox = driverTools.waitForElementToAppear(TEXT_BOX).orElseThrow();
        driverTools.clearInput(textBox);
        driverTools.sendKeys(textBox,value);
        driverTools.waitForElementToAppear(CHANGE_TEXT_BOX).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(CHANGE_TEXT_BOX + " element not found");
        });
    }

    public void changeComparisonRateAlphabetic(String value){
        WebElement comparisonRate = driverTools.element(COMPARISON_THRESHOLD).orElseThrow();
        driverTools.click(comparisonRate);
        WebElement textBox = driverTools.waitForElementToAppear(TEXT_BOX).orElseThrow();
        driverTools.clearInput(textBox);
        driverTools.sendKeys(textBox,value);
        driverTools.waitForElementToAppear(CHANGE_TEXT_BOX).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(CHANGE_TEXT_BOX + " element not found");
        });
    }

    public void clickClose(){
        driverTools.waitForElementToBeClickable(CLOSE_BUTTON).ifPresentOrElse(driverTools::click, () -> {
            throw new ElementNotInteractableException(CLOSE_BUTTON + " element not interactable");
        });
    }

    public void resetIcon(){
        driverTools.waitForElementToAppear(RESET_ICON, 10).ifPresent(driverTools::click);
    }

    public String getTextFromTextBox(){
        driverTools.waitForElementToAppear(TEXT_BOX);
        String value = driverTools.element(TEXT_BOX).get().getAttribute("value");
        logger.log("TEXT_BOX value: [" + value + "]");
        return value;
    }

    public void downloadIcon(){
        driverTools.waitForElementToAppear(DOWNLOAD_BUTTON,10).ifPresent(driverTools::click);
    }

    public String extractNumericValue(String inputText) {
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(inputText);
        StringBuilder text = new StringBuilder();
        while (matcher.find()) {
            text.append(matcher.group());
        }
        return text.toString();
    }

    public Map<String, Double> fetchLatestBuySellValuesFromMongoDB(String configType, String dataBase, String collectionName) {
        Map<String, Double> buySellValues = new HashMap<>();
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase, collectionName, "{}").sort(new BasicDBObject("doneAt", -1)).limit(1);
        for (Document document : result) {
            Document rateInfo = (Document) document.get("rateInfo");
            buySellValues.put("Buy", rateInfo.getDouble("Buy"));
            buySellValues.put("Sell", rateInfo.getDouble("Sell"));
            break;
        }
        return buySellValues;
    }

    public String fetchDoneByNameFromMongoDB(String configType, String dataBase, String collectionName) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase, collectionName, "{}")
                .sort(new BasicDBObject("doneAt", -1)).limit(1);
        for (Document document : result) {
            String doneBy = document.getString("doneBy");
            return doneBy;
        }
        return null;
    }

    public String getUsername(){
        return driverTools.element(LOGIN_USERNAME).get().getText();
    }

    public String getRefreshModalInfo() throws InterruptedException{
        driverTools.click(WAITING_BUTTON);
        driverTools.waitForElementToAppear(REFRESH_MODAL_INFO);
        return driverTools.element(REFRESH_MODAL_INFO).get().getText();
    }

    public int getNumberOfCurrencyForSearchKey(String configType, String dataBase, String collectionName, String queryString, String searchKey) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase, collectionName, queryString);
        int count = 0;
        for (Document document : result) {
            if(!searchKey.equals("IDR")){
                if (document.containsKey("fromCurrency")) {
                    Object value = document.get("fromCurrency");
                    if (value.equals(searchKey)) {
                        count++;
                    }
                }

            }
            else{
                if (document.containsKey("toCurrency")) {
                    Object value = document.get("toCurrency");
                    if (value.equals(searchKey)) {
                        count++;
                    }
                }

            }
        }
        return count;
    }

    public int getNumberOfCurrencyinToField(String configType, String dataBase, String collectionName, String queryString){
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase, collectionName, queryString);
        int count = 0;
        for (Document document : result) {
            if (document.containsKey("fromCurrency")) {
                Object value = document.get("fromCurrency");
                if (value.equals("IDR")) {
                    count++;
                }
            }
        }

        return count;
    }

    public int getNumberOfFromFieldMathcingCurrency() throws InterruptedException{
        WebElement paginationButton = driverTools.element(PAGINATION_BUTTON).orElseThrow();
        WebElement columnElement = driverTools.element(COLUMN_ONE_ELEMENT_NAME).orElseThrow();
        int count = 0;
        driverTools.scrollIntoWebView(paginationButton);
        while(paginationButton.getAttribute("aria-disabled").equals("false")){
            String elementName = driverTools.getText(columnElement);
            if(elementName.equals("IDR")){
                count++;
            }
            driverTools.waitForElementToBeClickable(paginationButton);
            driverTools.click(paginationButton);
        }
        return count;
    }

    public String changeDecimalValue(String number){
        DecimalFormat df = new DecimalFormat("#,###");
        int value = Integer.parseInt(number);
        number = df.format(value);
        return number;
    }

    public String checkOverrideValue(String override_rate, String usd_to_idr_rate){
        int newValue = 0;
        if(getOverrideRate().contains("+")){
            newValue = Integer.parseInt(override_rate) + Integer.parseInt(usd_to_idr_rate);
            override_rate = String.valueOf(newValue);
            changeOverrideRate(override_rate);
            clickOnYesOverride_icon();
        }
        else {
            override_rate =  getOverrideRate().replaceAll("[[\\D]]", "");
        }
        return override_rate;
    }
//FxRate_2024-06-28_14-17-19.csv

    public String exportFilenameBuilder(){
        String prefix = "FxRate";
        String separator = "_";
        String todayDate = LocalDate.now().toString();
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
        return prefix+separator+todayDate+separator+time+".csv";
    }

    public boolean verifyDownloadedExportCSV(String timeOutMiliSec){
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        logger.log("Download folder: "+downloadDir);
        String expectedFilename = exportFilenameBuilder();
        logger.log("expected filename: "+expectedFilename);
        boolean fileDownloaded = false;

        long startTime = Instant.now().toEpochMilli();
        logger.log("Start time: "+startTime);
        long waitTime = startTime + Integer.parseInt(timeOutMiliSec);
        logger.log("Wait time: "+waitTime);

        while (Instant.now().toEpochMilli() < waitTime){
            File[] listFiles = new File(downloadDir).listFiles();
            for (File file : listFiles){
                if (file.getName().equals(expectedFilename)){
                    String[] listFilesName = new File(downloadDir).list(); //logging purpose
                    for (int i = 0; i < listFilesName.length; i++) {
                        logger.log("File name on dir: "+ listFilesName[i]);
                    }
                    fileDownloaded = true;
                    file.delete();
                    break;
                }
            }
            if (fileDownloaded) break;
        }

        return fileDownloaded;
    }
}

