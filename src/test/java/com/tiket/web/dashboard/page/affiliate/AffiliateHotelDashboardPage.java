
package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.io.File;
import java.util.List;

public class AffiliateHotelDashboardPage<T extends WebDriver> extends BasePage<T> {
    public AffiliateHotelDashboardPage(Session<T> session) {
        super(session);
    }

    private static final Locator SETTINGS_DROPDOWN = new Locator("SETTINGS_DROPDOWN", By.xpath("(//input[@id='settings'])[1]"));
    private static final Locator MANAGE_CREDENTIALS = new Locator("MANAGE_CREDENTIALS", By.xpath("(//div[.//span[text()='Manage Credentials' or text()='Kelola Kredensial']])[4]"));
    private static final Locator PENALTY_INFO = new Locator("PENALTY_INFO", By.xpath("(//div[.//span[text()='Penalty Info']])[4]"));
    private static final Locator ADD_NEW_BUTTON = new Locator("ADD_NEW_BUTTON", By.xpath("//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator CHANNEL_ID_DROPDOWN = new Locator("CHANNEL_ID_DROPDOWN", By.xpath("//input[@aria-label='channelId Dropdown']/following-sibling::div[contains(@class,'right_slot_wrapper')]//button"));
    private static final Locator FIRST_CHANNEL_OPTION = new Locator("FIRST_CHANNEL_OPTION", By.xpath("//ul[contains(@class,'grid')]/li[1]"));
    private static final Locator LOGIN_DROPDOWN_BUTTON = new Locator("LOGIN_DROPDOWN_BUTTON", By.xpath("//input[@aria-label='login Dropdown']/following-sibling::div[contains(@class,'right_slot_wrapper')]//button"));
    private static final Locator LOGIN_FIRST_OPTION = new Locator("LOGIN_FIRST_OPTION", By.xpath("//*[text()='Login' or text()='Masuk']"));
    private static final Locator DELETE = new Locator("DELETE", By.xpath("(//*[text()='Delete' or text()='Hapus'])[2]"));
    private static final Locator SAVE_BTN = new Locator("SAVE_BTN", By.xpath("//button[text()='Save' or text()='Simpan']"));
    private static final Locator CONFIRM_SAVE_BTN = new Locator("CONFIRM_SAVE_BTN", By.xpath("(//button[text()='Save' or text()='Simpan'])[2]"));
    private static final Locator CREDENTIALS_ADDED = new Locator("CREDENTIALS_ADDED", By.xpath("//div[contains(@class, 'Snackbar_text_content') and (normalize-space(text())='Successfully added a new credential' or normalize-space(text())='Berhasil menambahkan kredensial baru')]"));
    private static final Locator YES_BUTTON = new Locator("YES_BUTTON", By.xpath("//button[normalize-space(text())='Yes' or normalize-space(text())='Ya']"));
    private static final Locator CREDENTIALS_DELETED = new Locator("CREDENTIALS_DELETED", By.xpath("//div[contains(@class, 'Snackbar_text_content') and (normalize-space(text())='Successfully deleted credential' or normalize-space(text())='Berhasil menghapus kredensial')]"));
    private static final Locator FIRST_DATA_ROW = new Locator("FIRST_DATA_ROW", By.xpath("(//tr[contains(@class,'odd:bg-f4f7fe') or contains(@class,'even:bg-white')])[1]"));
    private static final Locator HOTEL_UPDATED = new Locator("HOTEL_UPDATED", By.xpath("//div[contains(@class, 'Snackbar_text_content') and normalize-space(text())='Successfully edit hotel config']"));
    private static final Locator COMPANY_NAME = new Locator("COMPANY_NAME", By.name("partnerName"));
    private static final Locator SEARCH_KEYWORD_INPUT = new Locator("SEARCH_KEYWORD_INPUT", By.id("keyword"));
    private static final Locator CONTENT_SYNC_OPTION = new Locator("CONTENT_SYNC_OPTION", By.xpath("//span[normalize-space()='Content Sync']"));
    private static final Locator DROPDOWN_TOGGLE = new Locator("DROPDOWN_TOGGLE", By.xpath("//div[contains(@class,'right_slot_wrapper')]//button[contains(@class,'clickable_icon')]"));
    private static final Locator COMPLETED_CHECKBOX = new Locator("COMPLETED_CHECKBOX", By.xpath("//li[.//p[normalize-space()='Completed']]//label[contains(@class,'Check_check')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[text()='Search' and contains(@class, 'Button_variant_secondary')]"));
    private static final Locator UPLOAD_STATUS_INPUT = new Locator("UPLOAD_STATUS_INPUT", By.xpath("//input[@id='upload-status']"));
    private static final Locator DOWNLOAD_SUMMARY_BUTTON = new Locator("DOWNLOAD_SUMMARY_BUTTON", By.xpath("(//button[contains(@class, 'Button_button') and normalize-space(text())='Download Summary'])[1]"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", By.xpath("//button[contains(@class,'Button_button') and normalize-space(text())='Continue']"));
    private static final Locator COMMISSION_ADDED = new Locator("COMMISSION_ADDED", By.xpath("//div[contains(@class, 'Snackbar_text_content') and normalize-space(text())='Hotel commission successfuly created!']"));
    private static final Locator PUSH_DATA_BTN = new Locator("PUSH_DATA_BTN", By.xpath("//button[normalize-space()='Push Data' and contains(@class, 'Button_variant_primary')]"));
    private static final Locator BULK_PUSH_BTN = new Locator("BULK_PUSH_BTN", By.xpath("//button[contains(@class,'Tabs_tab') and .//p[normalize-space()='Bulk Push']]"));
    private static final Locator ACTIVATE_CHIP_BUTTON = new Locator("ACTIVATE_CHIP_BUTTON", By.xpath("//button[span[normalize-space()='Activate & Remove Exclusion']]"));
    private static final Locator DEACTIVATE_ADD_EXCLUSION_CHIP_BUTTON = new Locator("DEACTIVATE_ADD_EXCLUSION_CHIP_BUTTON", By.xpath("//button[span[normalize-space()='Deactivate & Add Exclusion' or normalize-space()='Nonaktifkan & Tambah Pengecualian']]"));
    private static final Locator SUBMIT = new Locator("SUBMIT", By.xpath("(//button[contains(@class, 'Button_button') and contains(@class, 'Button_horizontal_padding') and contains(@class, 'Button_variant_primary')])[2]"));
    private static final Locator BROWSE_FILE_BTN = new Locator("BROWSE_FILE_BTN", By.xpath("//input[@type='file']"));
    private static final Locator ROOM_CHIP_BTN = new Locator("ROOM_CHIP_BTN", By.xpath("(//div[contains(@class, 'Tabs_tab_panel__jFCb6') and not(@hidden)]//button[.//span[text()='Room']])[1]"));
    private static final Locator UPLOAD_BTN = new Locator("UPLOAD_BTN", By.xpath("//button[contains(@class, 'Button_button__20aRx') and text()='Upload']"));
    private static final Locator ROOM_DATA = new Locator("ROOM_DATA", By.xpath("//div[contains(@class, 'Snackbar_text_content') and normalize-space(text())='Push Content Success']"));
    private static final Locator PARTNER_ID_SEARCH_INPUT = new Locator("PARTNER_ID_SEARCH_INPUT", By.xpath("//input[@id='partner-id']"));
    private static final Locator COMMISSION_VALUE = new Locator("COMMISSION_VALUE", By.xpath("//table//tbody/tr[1]/td[5]"));

    public enum ExclusionAction {
        ACTIVATE,
        DEACTIVATE
    }

    public void clickHotel() {
        try {
            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get("https://gatotkaca.tiket.com/admin/affiliate/hotel");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driverTools.get("https://preprod.tiket.com/admin/affiliate/hotel");
            } else if (Configs.ENVIRONMENT == Environment.PROD) {
                driverTools.get("https://www.tiket.com/admin/affiliate/hotel");
            }
            logger.log("Navigated to Hotel Page.");
        } catch (Exception e) {
            logger.fail("Failed to Navigated to Hotel Page.");
            throw e;
        }
    }

    public void goToHotelExclusion(String partnerId) {
        try {
            String url;
            switch (Configs.ENVIRONMENT) {
                case GK:
                    url = "https://gatotkaca.tiket.com/admin/affiliate/hotel/excluded-hotel/list/";
                    break;
                case PREPROD:
                    url = "https://preprod.tiket.com/admin/affiliate/hotel/excluded-hotel/list/";
                    break;
                case PROD:
                    url = "https://www.tiket.com/admin/affiliate/hotel/excluded-hotel/list/";
                    break;
                default:
                    throw new IllegalStateException("Unexpected environment: " + Configs.ENVIRONMENT);
            }
            driverTools.get(url + partnerId);
            logger.log("Navigated to Hotel Exclusion Page.");
        } catch (Exception e) {
            logger.fail("Failed to navigate to Hotel Exclusion Page.");
            throw e;
        }
    }

    public boolean checkSearch(String searchValue) {
        driverTools.waitForElementToAppear(FIRST_DATA_ROW, 4).orElseThrow();
        String searchData = driverTools.getText(FIRST_DATA_ROW).orElseThrow();
        return searchData.contains(searchValue);
    }

    public void clickContentSync() {
        driverTools.scrollToElement(CONTENT_SYNC_OPTION);
        driverTools.waitForElementToBeClickable(CONTENT_SYNC_OPTION, 5);
        driverTools.click(CONTENT_SYNC_OPTION);
    }

    public void selectUploadStaus() {
        driverTools.click(DROPDOWN_TOGGLE);
        driverTools.click(COMPLETED_CHECKBOX);
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean isHotelIDSearchFieldEmpty() {
        String hotelID = driverTools.getText(SEARCH_KEYWORD_INPUT).orElseThrow();
        return hotelID.isEmpty();
    }

    public boolean isFinanceDetailsPage() {
        boolean isFinanceDetailsPage = false;
        driverTools.waitForElementToAppear(COMPANY_NAME, 5);
        if (Configs.ENVIRONMENT == Environment.GK) {
            isFinanceDetailsPage = driverTools.getCurrentURL().contains("https://gatotkaca.tiket.com/admin/affiliate/hotel/edit/finance-detail/");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            isFinanceDetailsPage = driverTools.getCurrentURL().contains("https://preprod.tiket.com/admin/affiliate/hotel/edit/finance-detail/");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            isFinanceDetailsPage = driverTools.getCurrentURL().contains("https://www.tiket.com/admin/affiliate/hotel/edit/finance-detail/");
        }
        return isFinanceDetailsPage;
    }

    public boolean isHotelDetailsUpdated() {
        driverTools.waitForElementToAppear(HOTEL_UPDATED);
        return driverTools.isElementDisplayed(HOTEL_UPDATED);
    }

    public boolean checkAllStatuses(String status) {
        List<WebElement> cards = driver.findElements(By.cssSelector("div.bg-white.p-6.rounded-lg"));

        for (WebElement card : cards) {
            List<WebElement> statusElements = card.findElements(By.cssSelector("p.text-sm.font-semibold"));

            for (WebElement statusEl : statusElements) {
                String statusText = statusEl.getText().trim();
                if (statusText.equals(status)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isUploadStatusClear() {
        String uploadStatus = driverTools.getText(UPLOAD_STATUS_INPUT).orElseThrow();
        return uploadStatus.isEmpty();
    }

    public boolean downloadBulkPushSummary() {
        driverTools.waitForElementToAppear(DOWNLOAD_SUMMARY_BUTTON, 5);
        if (!driverTools.isElementDisplayed(DOWNLOAD_SUMMARY_BUTTON)) {
            return false;
        }
        driverTools.click(DOWNLOAD_SUMMARY_BUTTON);
        driverTools.waitForElementToAppear(CONTINUE_BUTTON, 5);
        if (!driverTools.isElementDisplayed(CONTINUE_BUTTON)) {
            return false;
        }
        driverTools.click(CONTINUE_BUTTON);
        return true;
    }

    public boolean isCommissionAdded(){
        driverTools.waitForElementToAppear(COMMISSION_ADDED);
        return driverTools.isElementDisplayed(COMMISSION_ADDED);
    }

    public void pushData(String filePath, ExclusionAction action){
        File data = new File(filePath);

        driverTools.click(PUSH_DATA_BTN);
        driverTools.waitForElementToAppear(BULK_PUSH_BTN, 5);
        driverTools.click(BULK_PUSH_BTN);
        driverTools.waitForElementToAppear(ROOM_CHIP_BTN, 5);
        driverTools.clickWithJavascriptExecutor(ROOM_CHIP_BTN);

        Locator CHIPBUTTON = (action == ExclusionAction.ACTIVATE) ? ACTIVATE_CHIP_BUTTON : DEACTIVATE_ADD_EXCLUSION_CHIP_BUTTON;
        driverTools.clickWithJavascriptExecutor(CHIPBUTTON);
        driverTools.waitForElementToAppear(BROWSE_FILE_BTN, 5);
        driverTools.sendKeys(BROWSE_FILE_BTN, data.getAbsolutePath());

        driverTools.clickWithJavascriptExecutor(SUBMIT);
        driverTools.waitForElementToAppear(UPLOAD_BTN, 5);
        driverTools.clickWithJavascriptExecutor(UPLOAD_BTN);
    }

    public boolean isDataPushed(){
        driverTools.waitForElementToAppear(ROOM_DATA);
        return driverTools.isElementDisplayed(ROOM_DATA);
    }

    public void goToManageCredentials(){
        driverTools.waitForElementToAppear(SETTINGS_DROPDOWN, 2);
        driverTools.click(SETTINGS_DROPDOWN);
        driverTools.waitForElementToAppear(MANAGE_CREDENTIALS, 2);
        driverTools.scrollToElement(MANAGE_CREDENTIALS);
        driverTools.click(MANAGE_CREDENTIALS);
    }

    public void goToPenaltyInfo() {
        driverTools.waitForElementToAppear(SETTINGS_DROPDOWN, 2);
        driverTools.click(SETTINGS_DROPDOWN);
        driverTools.waitForElementToAppear(PENALTY_INFO, 2);
        driverTools.scrollToElement(PENALTY_INFO);
        driverTools.click(PENALTY_INFO);
    }

    public void addNewCredentials(){
        driverTools.waitForElementToAppear(CHANNEL_ID_DROPDOWN, 1);
        driverTools.click(ADD_NEW_BUTTON);
        driverTools.click(CHANNEL_ID_DROPDOWN);
        driverTools.waitForElementToAppear(FIRST_CHANNEL_OPTION, 2);
        driverTools.click(FIRST_CHANNEL_OPTION);
        driverTools.click(LOGIN_DROPDOWN_BUTTON);
        driverTools.waitForElementToAppear(LOGIN_FIRST_OPTION, 2);
        driverTools.click(LOGIN_FIRST_OPTION);
        driverTools.click(SAVE_BTN);
        driverTools.waitForElementToAppear(CONFIRM_SAVE_BTN, 2);
        driverTools.click(CONFIRM_SAVE_BTN);
    }

    public boolean isCredentialsAdded(){
        driverTools.waitForElementToAppear(CREDENTIALS_ADDED, 2);
        return driverTools.isElementDisplayed(CREDENTIALS_ADDED);
    }

    public void deleteCredentials(){
        driverTools.waitForElementToAppear(DELETE, 2);
        driverTools.click(DELETE);
        driverTools.waitForElementToAppear(YES_BUTTON, 2);
        driverTools.click(YES_BUTTON);
    }

    public boolean isCredentialsDeleted(){
        driverTools.waitForElementToAppear(CREDENTIALS_DELETED);
        return driverTools.isElementDisplayed(CREDENTIALS_DELETED);
    }

    public void searchByPartnerId(String partnerId) {
        driverTools.waitForElementToAppear(PARTNER_ID_SEARCH_INPUT, 5);
        driverTools.sendKeys(PARTNER_ID_SEARCH_INPUT, partnerId);
        driverTools.click(SEARCH_BUTTON);
    }

    public String getCommissionValue() {
        driverTools.waitForElementToAppear(COMMISSION_VALUE, 5);
        return driverTools.getText(COMMISSION_VALUE).orElseThrow();
    }
}