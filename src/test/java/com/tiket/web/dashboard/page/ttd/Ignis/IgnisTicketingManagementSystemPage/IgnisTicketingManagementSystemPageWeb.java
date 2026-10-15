package com.tiket.web.dashboard.page.ttd.Ignis.IgnisTicketingManagementSystemPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import static org.hamcrest.core.Is.is;

public class IgnisTicketingManagementSystemPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisTicketingManagementSystemPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisTicketingManagementSystemPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisTicketingManagementSystemPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisTicketingManagementSystemPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator GENERATE_BARCODE_BUTTON_MAIN_PAGE = new Locator("GENERATE_BARCODE_BUTTON_MAIN_PAGE", By.xpath("//button[contains(@class,'generate-barcode-button')]"));
    private static final Locator CATEGORY_INPUT = new Locator("CATEGORY_INPUT", By.xpath("//input[@name='categoryName'][last()]"));
    private static final Locator QUANTITY_INPUT = new Locator("QUANTITY_INPUT", By.xpath("//input[@name='totalTicket'][last()]"));
    private static final Locator GENERATE_BARCODE_BUTTON_POPUP = new Locator("GENERATE_BARCODE_BUTTON_POPUP", By.xpath("//div[@class='generate-barcode']//button[contains(@class,'primary')]"));

    public void goToPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {driver.get("https://gatotkaca.tiket.com/ignis/ticketing-management-system");}
            case PREPROD -> {driver.get("https://preprod.tiket.com/ignis/ticketing-management-system");}
            case PROD -> {driver.get("https://www.tiket.com/ignis/ticketing-management-system");}
        }
        driverTools.waitForWebToLoad();
    }

    public void chooseEvent(String eventName) {
        Locator locator = getEventLocatorByEventName(eventName);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
        driverTools.waitForWebToLoad();
    }

    private Locator getEventLocatorByEventName(String eventName) {
        return new Locator("EVENT_CONTAINER", By.xpath("//div[@class='ticketing-management-card-event' and .//h2[text()='"+eventName+"']]"));
    }

    public void clickGenerateBarcodeButton() {
        driverTools.waitForElementsToAppear(GENERATE_BARCODE_BUTTON_MAIN_PAGE);
        driverTools.click(GENERATE_BARCODE_BUTTON_MAIN_PAGE);
    }

    public void addCategory(String categoryName, String quantity) {
        driverTools.waitForElementsToAppear(CATEGORY_INPUT);
        driverTools.clearInput(CATEGORY_INPUT);
        driverTools.sendKeys(CATEGORY_INPUT, categoryName);

        driverTools.waitForElementsToAppear(QUANTITY_INPUT);
        driverTools.clearInput(QUANTITY_INPUT);
        driverTools.sendKeys(QUANTITY_INPUT, quantity);
    }

    public void clickGenerateButtonPopup() {
        driverTools.waitForElementsToAppear(GENERATE_BARCODE_BUTTON_POPUP);
        driverTools.click(GENERATE_BARCODE_BUTTON_POPUP);
    }

    public boolean checkNewlyDownloadedBarcodeGenerated(String categoryName, String quantity) throws IOException {
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        FileInputStream file = new FileInputStream(CoreFunctions.getLastModified(downloadDir));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        Row row = rowIterator.next(); // skip header
        boolean result = true;
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            String dataPointCategoryName = String.valueOf(row.getCell(0));
            String dataPointBarcode = String.valueOf(row.getCell(1));

            if (!(categoryName.equalsIgnoreCase(dataPointCategoryName) && dataPointBarcode != null)) {
                result = false;
                break;
            }
        }

        Assertion.assertThat("Row count and quantity from testdata is matched", sheet.getLastRowNum(), is(Integer.parseInt(quantity)));

        file.close();

        return result;
    }
}
