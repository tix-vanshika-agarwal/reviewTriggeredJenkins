package com.tiket.web.dashboard.page.platform.sphinx.insurance.insurance_report;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InsuranceReportDashboardPageWeb<T extends WebDriver> extends WebPage<T> {

    public InsuranceReportDashboardPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> InsuranceReportDashboardPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new InsuranceReportDashboardPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new InsuranceReportDashboardPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Platform not supported");
        }
    }

    private static final Locator INSURANCE_REPORT_PAGE=  new Locator("INSURANCE_REPORT_PAGE", By.xpath("//div[contains(@class, 'PageHeader_page_header_container_content__fXkax')]//p[text()='Insurance Report']"));
    private static final Locator DOWNLOAD = new Locator("DOWNLOAD", By.xpath("//button[@data-testid='button-download']"));
    private static final Locator CLOSE_DOWNLOAD_BUTTON = new Locator("CLOSE_DOWNLOAD_BUTTON", By.xpath("//div[@class='DownloadProgress_icon_close__msp96']"));
    private static final Locator REPORT_DOWNLOAD_PAGE = new Locator("REPORT_DOWNLOAD_PAGE", By.xpath("//header[contains(@class,'Modal_modal_header')]//h2[normalize-space()='Select Date Range To Download Report']"));
    private static final Locator DATE_INPUT = new Locator("DATE_INPUT", By.xpath("//div[contains(@class,'Modal')]//input[@data-testid='input-daterange-start-default']"));
    private static final Locator VERTICAL_PRODUCT = new Locator("VERTICAL_PRODUCT", By.xpath("//input[@data-testid='input-select-dialog-download-vertical-product']"));
    private static final Locator PARTNER_NAME_DROPDOWN = new Locator("PARTNER_NAME_DROPDOWN", By.xpath("(//input[@id='partner-name'])[2]"));
    private static final Locator DOWNLOAD_REPORT_BUTTON = new Locator("DOWNLOAD_REPORT_BUTTON", By.xpath("//button[contains(@class, 'Button_variant_primary')]"));
    private static final Locator VERTICAL_SEARCH = new Locator("VERTICAL_SEARCH", By.xpath("//input[@data-testid='search-select-client-name']"));
    private static final Locator EXPORT_COMPLETED_STATUS = new Locator("EXPORT_COMPLETED_STATUS",By.xpath("//div[contains(@class,'DownloadProgress_download_progress_content_status_type')]//span[normalize-space()='Export has completed']"));

    private String startDate;
    private String endDate;

    public boolean verifyInsurancePageIsOpened(){
        driverTools.waitForElementToAppear(INSURANCE_REPORT_PAGE,10);
        return driverTools.isElementPresent(INSURANCE_REPORT_PAGE);
    }

    public void clickDownloadButton() {
        driverTools.waitForElementToAppear(DOWNLOAD, 10);
        driverTools.clickWithJavascriptExecutor(DOWNLOAD);
    }

    public boolean isReportDownloadPageOpened() {
        driverTools.waitForElementToAppear(REPORT_DOWNLOAD_PAGE, 10);
        if(driverTools.isElementPresent(REPORT_DOWNLOAD_PAGE)) return true;
        else{
            logger.log("Report Download Page is not Present.");
            return false;
        }
    }

    public void inputDate() {
        driverTools.waitForElementToAppear(DATE_INPUT, 10);
        driverTools.clickWithJavascriptExecutor(DATE_INPUT);

        Locale indo = new Locale("id", "ID");
        Calendar calendar = Calendar.getInstance();

        endDate = new SimpleDateFormat("d MMMM yyyy EEEE", indo).format(calendar.getTime());
        Locator END_DATE = new Locator("END_DATE", By.xpath("//span[@aria-label='" + endDate + "']"));

        calendar.add(Calendar.DAY_OF_MONTH, -5);
        startDate = new SimpleDateFormat("d MMMM yyyy EEEE", indo).format(calendar.getTime());
        Locator START_DATE = new Locator("START_DATE", By.xpath("//span[@aria-label='" + startDate + "']"));

        driverTools.waitForElementToAppear(START_DATE, 10);
        driverTools.clickWithJavascriptExecutor(START_DATE);

        driverTools.waitForElementToAppear(END_DATE, 10);
        driverTools.clickWithJavascriptExecutor(END_DATE);
    }

    public boolean isStartDateBeforeEndDate(){
        try {
            Locale indo = new Locale("id", "ID");
            SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy EEEE", indo);

            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            if (!start.before(end)) {
                logger.log("Start date can't be the same or after the end date.");
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Locator getDropdownOption(String optionName) {
        return new Locator("DROPDOWN_OPTION", By.xpath("//div[contains(@class,'SelectItem_select_item_content')]//span[normalize-space()='" + optionName + "']"));
    }

    public void selectVerticalProduct(String vertical) {
        driverTools.waitForElementToAppear(VERTICAL_PRODUCT,10);
        driverTools.click(VERTICAL_PRODUCT);

        Locator VERTICAL_OPTION = getDropdownOption(vertical);

        driverTools.waitForElementToAppear(VERTICAL_OPTION, 10);
        driverTools.scrollToElement(VERTICAL_OPTION);
        driverTools.click(VERTICAL_OPTION);
    }

    public void selectPartnerName(String partnerName) {
        driverTools.waitForElementToBeClickable(PARTNER_NAME_DROPDOWN, 10);
        driverTools.clickWithJavascriptExecutor(PARTNER_NAME_DROPDOWN);
        driverTools.sendKeys(VERTICAL_SEARCH, partnerName);

        Locator PARTNER_OPTION = getDropdownOption(partnerName);

        driverTools.waitForElementToAppear(PARTNER_OPTION, 10);
        driverTools.scrollToElement(PARTNER_OPTION);
        driverTools.click(PARTNER_OPTION);
    }

    public void clickDownloadReportButton() {
        driverTools.waitForElementToAppear(DOWNLOAD_REPORT_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(DOWNLOAD_REPORT_BUTTON);
    }

    public boolean isExportCompleted() {
        driverTools.waitForElementToAppear(CLOSE_DOWNLOAD_BUTTON);
        driverTools.clickWithJavascriptExecutor(CLOSE_DOWNLOAD_BUTTON);
        driverTools.waitForElementToAppear(EXPORT_COMPLETED_STATUS, 10);
        return driverTools.isElementPresent(EXPORT_COMPLETED_STATUS);
    }
}