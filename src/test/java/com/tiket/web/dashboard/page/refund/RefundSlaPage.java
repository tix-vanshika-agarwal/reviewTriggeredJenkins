package com.tiket.web.dashboard.page.refund;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class RefundSlaPage<T extends WebDriver> extends BasePage<T> {
    public RefundSlaPage(Session<T> session) { super(session); }

    private static final Locator MENU_REFUND_SLA = new Locator("MENU_REFUND_SLA", new By.ByXPath("//span[contains(text(),'SLA')]"));
    private static final Locator TITLE_REFUND_SLA = new Locator("TITLE_REFUND_SLA", new By.ByXPath("//h1[contains(text(),'SLA')]"));
    private static final Locator BUTTON_PARTNER_NAME = new Locator("BUTTON_PARTNER_NAME", new By.ByXPath("//div//span[contains(text(),'Partner Name')]"));
    private static final Locator BUTTON_SEARCH = new Locator("BUTTON_SEARCH", new By.ByXPath("//button[@type='submit'][normalize-space()='Search']"));
    private static final Locator FIRST_ACTION_BUTTON = new Locator("FIRST_ACTION_BUTTON", new By.ByXPath("(//div[@aria-roledescription='button for toggle menu'])[1]"));
    private static final Locator BUTTON_SEE_LOG = new Locator("BUTTON_SEE_LOG", new By.ByXPath("//div[@role='menuitem' and contains(., 'See Log')]"));
    private static final Locator TITLE_SEE_LOG = new Locator("TITLE_SEE_LOG", new By.ByXPath("//h2[contains(text(),'See Log')]"));
    private static final Locator TABLE_ROWS = new Locator("TABLE_ROWS", new By.ByXPath("//div[contains(@data-testid,'table')]//table//tbody//tr"));
    private static final Locator MODAL_DATA_TABLE = new Locator("MODAL_DATA_TABLE", new By.ByXPath("(//div[@data-testid='data-table'])[last()]"));

    public void goToRefundSlaTab() {
        driverTools.waitForElementToAppear(MENU_REFUND_SLA).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(MENU_REFUND_SLA + "element not found");
        });
    }

    public boolean isOnRefundSlaTab() {
        driverTools.waitForElementToAppear(TITLE_REFUND_SLA);
        return driverTools.isElementDisplayed(TITLE_REFUND_SLA);
    }

    public void clickButtonPartnerNameFilter() {
        driverTools.waitForElementToAppear(BUTTON_PARTNER_NAME).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(BUTTON_PARTNER_NAME + "element not found");
        });
    }

    private Locator getPartnerNameLocator(String partnerName) {
        String partnerNameLocator = String.format("//label//span[contains(text(),'%s')]", partnerName);

        return new Locator("PARTNER_NAME_LOCATOR_" + partnerName.toUpperCase(), new By.ByXPath(partnerNameLocator));
    }

    public void selectPartnerNameInFilter(String partnerName) {
        Locator partnerNameLocator = getPartnerNameLocator(partnerName);
        driverTools.waitForElementToAppear(partnerNameLocator).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(partnerNameLocator + "element not found");
        });
        driverTools.waitForElementToAppear(BUTTON_SEARCH).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(BUTTON_SEARCH + "element not found");
        });
    }

    private Locator getPartnerNameResultLocator(String partnerName) {
        String partnerNameResultLocator = String.format("//p[normalize-space(.)='%s']", partnerName);
        return new Locator("PARTNER_NAME_RESULT_" + partnerName.toUpperCase(), new By.ByXPath(partnerNameResultLocator));
    }

    public boolean isResultForPartnerNameAvailable(String partnerName) {
        Locator partnerNameResultLocator = getPartnerNameResultLocator(partnerName);

        return driverTools.waitForElementToAppear(partnerNameResultLocator).isPresent();
    }

    private Locator getSelectedPartnerNameChipLocator(String partnerName) {
        String selectedPartnerNameChipLocator = String.format("//form//div[contains(@class,'rounded-full') and normalize-space(text())='%s']", partnerName);

        return new Locator("SELECTED_PARTNER_NAME_CHIP_" + partnerName.toUpperCase(), new By.ByXPath(selectedPartnerNameChipLocator));
    }

    public boolean isPartnerNameSelectedInFilter(String partnerName) {
        Locator selectedPartnerNameChipLocator = getSelectedPartnerNameChipLocator(partnerName);

        return driverTools.waitForElementToAppear(selectedPartnerNameChipLocator).isPresent();
    }

    private Locator getResultRowsForPartnerNameLocator(String partnerName) {
        String resultRowsLocator = String.format("//div[contains(@data-testid,'table')]//table//tbody//tr[.//p[normalize-space(.)='%s']]", partnerName);

        return new Locator("RESULT_ROWS_" + partnerName.toUpperCase(), new By.ByXPath(resultRowsLocator));
    }

    public boolean isAllResultsForPartnerName(String partnerName) {
        int totalRows = driverTools.waitForElementsToAppear(TABLE_ROWS).map(List::size).orElse(0);
        int matchingRows = driverTools.waitForElementsToAppear(getResultRowsForPartnerNameLocator(partnerName)).map(List::size).orElse(0);

        return totalRows > 0 && totalRows == matchingRows;
    }

    public void clickButtonAction() {
        driverTools.waitForElementToAppear(FIRST_ACTION_BUTTON).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(FIRST_ACTION_BUTTON + "element not found");
        });
    }

    public void clickButtonSeeLog() {
        driverTools.waitForElementToAppear(BUTTON_SEE_LOG).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(BUTTON_SEE_LOG + "element not found");
        });
    }

    public boolean isModalSeeLogOpened() {
        driverTools.waitForElementToAppear(TITLE_SEE_LOG);
        boolean isTitleDisplayed = driverTools.isElementDisplayed(TITLE_SEE_LOG);

        driverTools.waitForElementToAppear(MODAL_DATA_TABLE);
        boolean isTableDisplayed = driverTools.isElementDisplayed(MODAL_DATA_TABLE);

        return isTitleDisplayed && isTableDisplayed;
    }
}
