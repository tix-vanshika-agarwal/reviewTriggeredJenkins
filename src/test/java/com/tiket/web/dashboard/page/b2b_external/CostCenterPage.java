package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CostCenterPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator COST_CENTER_PAGE_HEADING = new Locator("COST_CENTER_PAGE_HEADING", By.xpath("//h1[contains(text(),'Cost Center')] | //span[contains(text(),'Cost Center') and contains(@class,'text-')]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("//button[ .//span[ contains(.,'Edit Cost Center') or contains(.,'Ubah Cost Center')]]"));
    private static final Locator ADD_NEW_COST_CENTER_BUTTON = new Locator("ADD_NEW_COST_CENTER_BUTTON", By.xpath("//button[ contains(normalize-space(.),'Add another Cost Center') or contains(normalize-space(.),'Tambah Cost Center')]"));
    private static final Locator COST_CENTER_ID_INPUT = new Locator("COST_CENTER_ID_INPUT", By.xpath("(//tr[contains(@class,'bg-white')]//td[1]/input)[last()]"));
    private static final Locator COST_CENTER_NAME_INPUT = new Locator("COST_CENTER_NAME_INPUT", By.xpath("(//tr[contains(@class,'bg-white')]//td[2]/input)[last()]"));
    private static final Locator DESCRIPTION_INPUT = new Locator("DESCRIPTION_INPUT", By.xpath("(//tr[contains(@class,'bg-white')]//td[3]/input)[last()]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[.//img[@alt='Icon Save'] and ( contains(.,'Save') or contains(.,'Simpan'))]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class,'Snackbar_text_content') and (contains(text(),'cost center') or contains(text(),'Cost Center') or contains(text(),'saved') or contains(text(),'created'))]"));
    // private static final Locator SAVE_BUTTON_FROM_POPUP = new Locator("SAVE_BUTTON_FROM_POPUP", By.xpath("//footer[contains(@class,'Modal_modal_footer')]//button[normalize-space()='Save']"));
    private static final Locator SAVE_BUTTON_FROM_POPUP = new Locator("SAVE_BUTTON_FROM_POPUP", By.xpath("//footer[contains(@class,'Modal_modal_footer')]//button[normalize-space()='Save' or normalize-space()='Simpan']"));
   
    public CostCenterPage(Session<T> session) {
        super(session);
    }

    public boolean isCostCenterPageOpen() {
        driverTools.waitForElementToAppear(COST_CENTER_PAGE_HEADING);
        return driverTools.isElementDisplayed(COST_CENTER_PAGE_HEADING);
    }

    public void clickEditButton() {
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        driverTools.click(EDIT_BUTTON);
    }

    public void clickAddNewCostCenterButton() {
        driverTools.waitForElementToAppear(ADD_NEW_COST_CENTER_BUTTON);
        driverTools.click(ADD_NEW_COST_CENTER_BUTTON);
    }

    public void fillCostCenterDetails(String costCenterId, String costCenterName, String description) {
        driverTools.waitForElementToAppear(COST_CENTER_ID_INPUT);
        driverTools.sendKeys(COST_CENTER_ID_INPUT, costCenterId);

        driverTools.waitForElementToAppear(COST_CENTER_NAME_INPUT);
        driverTools.sendKeys(COST_CENTER_NAME_INPUT, costCenterName);

        driverTools.waitForElementToAppear(DESCRIPTION_INPUT);
        driverTools.sendKeys(DESCRIPTION_INPUT, description);
    }

    public void clickSaveButton() {
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void clickDeleteButtonByCostCenterId(String costCenterId) {
        Locator deleteBtn = new Locator("DELETE_BTN_" + costCenterId,
                By.xpath("//table[contains(@class,'w-full')]//tbody//tr[.//td[1]//input[contains(@value,'" + costCenterId + "')]]//td[last()]//*[name()='svg']"));
        driverTools.waitForElementToAppear(deleteBtn);
        driverTools.click(deleteBtn);
    }

    public boolean isCostCenterCreatedInTable(String costCenterId, String costCenterName, String description, boolean check) {
        Locator row = new Locator("COST_CENTER_ROW_" + costCenterId,
                By.xpath("//tbody//tr[.//td[1]//span[normalize-space()='" + costCenterId + "']" +
                         " and .//td[2]//span[normalize-space()='" + costCenterName + "']" +
                         " and .//td[3]//span[normalize-space()='" + description + "']]"));
                      
        if(check) driverTools.waitForElementToAppear(row, 10);         
        return driverTools.isElementDisplayed(row);
    }

    public boolean isSuccessSnackbarDisplayed() {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10);
        return driverTools.isElementDisplayed(SUCCESS_SNACKBAR);
    }

    public String getSuccessSnackbarText() {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10);
        return driverTools.getText(SUCCESS_SNACKBAR).orElseThrow();
    }

    public void clickSaveButtonFromPopup() {
        driverTools.waitForElementToAppear(SAVE_BUTTON_FROM_POPUP); 
        driverTools.click(SAVE_BUTTON_FROM_POPUP);
    }
}
