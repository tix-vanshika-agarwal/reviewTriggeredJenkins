package com.tiket.web.dashboard.page.nft.ferry;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ElementClickInterceptedException;

import java.io.File;
import java.util.List;

public class FerryClassPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator MOVE_TO_ADD_CLASS_PAGE_BUTTON = new Locator("MOVE_TO_ADD_CLASS_PAGE_BUTTON", new By.ByXPath("//button[contains(@href,'/admin/ferry/class/add')]"));
    private static final Locator LABEL_INPUT = new Locator("LABEL_INPUT", new By.ByXPath("//input[@id='label']"));
    private static final Locator NAME_ID_INPUT = new Locator("NAME_ID_INPUT", new By.ByXPath("//input[@id='name-id']"));
    private static final Locator NAME_EN_INPUT = new Locator("NAME_EN_INPUT", new By.ByXPath("//input[@id='name-en']"));
    private static final Locator AVAILABLE_FACILITIES_DROPDOWN = new Locator("AVAILABLE_FACILITIES_DROPDOWN", new By.ByXPath("//span[contains(text(),'Facilities (Available)')]"));
    private static final Locator PAGE = new Locator("PAGE", new By.ByXPath("//div[@class='menu-custom-options']/following-sibling::div"));
    private static final Locator UNAVAILABLE_FACILITIES_DROPDOWN = new Locator("UNAVAILABLE_FACILITIES_DROPDOWN", new By.ByXPath("//span[contains(text(),'Facilities Not Available')]"));
    private static final Locator VENDOR_DROPDOWN = new Locator("VENDOR_DROPDOWN", new By.ByXPath("//span[contains(text(),'Vendor Name')]"));
    private static final Locator VENDOR_ID_INPUT = new Locator("VENDOR_ID_INPUT", new By.ByXPath("//input[@id='vendor-id']"));
    private static final Locator IMAGE_UPLOAD = new Locator("IMAGE_UPLOAD", new By.ByXPath("//input[@name='file']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[contains(text(), 'Save')]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", new By.ByXPath("//div[@class='_2m8F2q_notification_banner _2m8F2q_variant_success']"));
    private static final Locator SEARCH_FIELD = new Locator("SEARCH_FIELD", new By.ByXPath("//input[@id='sb-text-input']"));
    private static final Locator FERRY_CLASS_LABEL_CELL = new Locator("FERRY_CLASS_LABEL_CELL", new By.ByXPath("//td[@class='rc-table-cell'][2]"));
    private static final Locator FERRY_CLASS_ID_NAME_CELL = new Locator("FERRY_CLASS_ID_NAME_CELL", new By.ByXPath("//td[@class='rc-table-cell'][3]"));
    private static final Locator FERRY_CLASS_EN_NAME_CELL = new Locator("FERRY_CLASS_EN_NAME_CELL", new By.ByXPath("//td[@class='rc-table-cell'][4]"));
    private static final Locator ACTIVE_SWITCH = new Locator("ACTIVE_SWITCH", new By.ByXPath("(//div[@class='n8SXEW_toggle n8SXEW_toggle_small'])[1]"));
    private static final Locator ACCEPT_BUTTON = new Locator("ACCEPT_BUTTON", new By.ByXPath("//button[@class='KV-nSG_button KV-nSG_full_width KV-nSG_horizontal_padding KV-nSG_variant_primary']"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", new By.ByXPath("(//span[@class='HcPVsG_text HcPVsG_variant_activity'][normalize-space()='Edit'])[1]"));
    private static final Locator DUPLICATE_BUTTON = new Locator("DUPLICATE_BUTTON", new By.ByXPath("(//span[@class='HcPVsG_text HcPVsG_variant_activity'][normalize-space()='Duplicate'])[1]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("(//span[@class='HcPVsG_text HcPVsG_variant_activity'][normalize-space()='Delete'])[1]"));

    public FerryClassPage(Session<T> session) {
        super(session);
    }

    public void goToFerryPage(String username, String password) {
        driverTools.maximizeWindow();

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/member");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/member");
        }

        if (!isFerryLoggedIn()) {
            FerryLoginPage<?> ferryLoginPage = new FerryLoginPage<>(session);
            ferryLoginPage.loginToFerryPage(username, password);
            driverTools.waitForElementToAppear(USERNAME_GREETING, 2);

            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get("https://gatotkaca.tiket.com/admin/ferry");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driverTools.get("https://preprod.tiket.com/admin/ferry");
            }
        }
    }

    public boolean isFerryLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 2);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public void goToClassTab() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/ferry/class");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/ferry/class");
        }
    }

    public void goToAddClassPage() {
        driverTools.clickWithJavascriptExecutor(MOVE_TO_ADD_CLASS_PAGE_BUTTON);
    }

    public void addNewClass(String label, String nameId, String nameEn, String vendorId, int index) {
        driverTools.waitForElementToAppear(LABEL_INPUT);
        driverTools.sendKeys(LABEL_INPUT, label);
        driverTools.sendKeys(NAME_EN_INPUT, nameEn);
        driverTools.sendKeys(NAME_ID_INPUT, nameId);
        driverTools.sendKeys(VENDOR_ID_INPUT, vendorId);
        selectOptionByIndex(VENDOR_DROPDOWN, index);
        selectOptionByIndex(AVAILABLE_FACILITIES_DROPDOWN, index);
        safeClickPage();
        selectOptionByIndex(UNAVAILABLE_FACILITIES_DROPDOWN, index);
        safeClickPage();
    }

    public void addImage(String filepath) {
        driverTools.waitForElementToAppear(IMAGE_UPLOAD);
        driverTools.sendKeys(IMAGE_UPLOAD, getLocalImagePath(filepath));
    }

    public void saveForm() {
        waitForSnackbarToDisappear();
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public boolean isSuccessSnackbarDisplayedWithCorrectMessage(String successMessage) {
        WebElement snackbar = driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10).orElse(null);
        return snackbar != null && snackbar.getText().contains(successMessage);
    }

    public void waitForSnackbarToDisappear() {
        if (driverTools.isElementDisplayed(SUCCESS_SNACKBAR)) {
            driverTools.waitForElementToDisappear(SUCCESS_SNACKBAR, 10);
        }
    }

    private void selectOptionByIndex(Locator dropdown, int index) throws StaleElementReferenceException {
        Locator optionLocator = new Locator("DROPDOWN_OPTION", new By.ByXPath(String.format("//div[@class='wrapper-checkbox'][%d] | //div[@class='radio'][%d]", index, index)));
        int attempts = 0;
        int maxAttempts = 4;
        while (attempts < maxAttempts) {
            attempts++;
            try {
                driverTools.click(dropdown);
                driverTools.waitForElementToBeClickable(optionLocator, 3)
                        .ifPresentOrElse(el -> driverTools.click(optionLocator), () -> {
                            throw new RuntimeException("Option not clickable: " + optionLocator);
                        });
                break;
            } catch (RuntimeException e) {
                if (attempts >= maxAttempts) {
                    throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
                }
            }
        }
    }

    private void safeClickPage() {
        try {
            driverTools.click(PAGE);
            return;
        } catch (ElementClickInterceptedException e) {
        } catch (RuntimeException e) {
            if (!(e.getCause() instanceof ElementClickInterceptedException)) {
                throw e;
            }
        }
    }

    public void searchFerryClass(String label) {
        clearSearchField();
        WebElement searchInput = driverTools.waitForElementToAppear(SEARCH_FIELD).get();
        searchInput.sendKeys(label);
        searchInput.sendKeys(Keys.RETURN);
    }

    public boolean verifySRPContainsClassLabel(String label) {
        return verifySRPContainsText(FERRY_CLASS_LABEL_CELL, label);
    }

    public boolean verifySRPContainsClassIdName(String idName) {
        return verifySRPContainsText(FERRY_CLASS_ID_NAME_CELL, idName);
    }

    public boolean verifySRPContainsClassEnName(String enName) {
        return verifySRPContainsText(FERRY_CLASS_EN_NAME_CELL, enName);
    }

    private boolean verifySRPContainsText(Locator cellLocator, String text) {
        List<WebElement> elements = driverTools.elements(cellLocator).get();
        return elements.stream().allMatch(element -> element.getText().contains(text));
    }

    public WebElement getFirstSRPItemLabel() {
        driverTools.waitForElementToAppear(FERRY_CLASS_LABEL_CELL, 3);
        return driverTools.firstElement(FERRY_CLASS_LABEL_CELL).get();
    }

    public WebElement getFirstSRPItemIdName() {
        driverTools.waitForElementToAppear(FERRY_CLASS_ID_NAME_CELL, 3);
        return driverTools.firstElement(FERRY_CLASS_ID_NAME_CELL).get();
    }

    public boolean verifyClassIsAtTheTop(String label) {
        String text = getFirstSRPItemLabel().getText();
        return text.contains(label);
    }

    public boolean verifyTheSRPIsNotEmpty() {
        List<WebElement> classLabels = driverTools.waitForElementsToAppear(FERRY_CLASS_LABEL_CELL, 3).orElse(null);
        return classLabels != null && !classLabels.isEmpty();
    }

    public void clearSearchField() {
        WebElement searchField = driverTools.element(SEARCH_FIELD).get();
        driverTools.clearWithJsExecutor(searchField);
    }

    public void toggleActiveFerry() {
        waitForSnackbarToDisappear();
        WebElement activateSwitch = driverTools.waitForElementToAppear(ACTIVE_SWITCH).get();
        driverTools.click(activateSwitch);
        WebElement acceptButton = driverTools.waitForElementToAppear(ACCEPT_BUTTON).get();
        driverTools.click(acceptButton);
    }

    public void goToEditClassPage() {
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        driverTools.click(EDIT_BUTTON);
    }

    public void editFerryClass(String idName, String enName) {
        driverTools.waitForElementToAppear(NAME_ID_INPUT);
        driverTools.clearWithJsExecutor(NAME_ID_INPUT);
        driverTools.sendKeys(NAME_ID_INPUT, idName);
        driverTools.clearWithJsExecutor(NAME_EN_INPUT);
        driverTools.sendKeys(NAME_EN_INPUT, enName);
        driverTools.click(SAVE_BUTTON);
    }

    public void duplicateClass() {
        driverTools.waitForElementToAppear(DUPLICATE_BUTTON);
        driverTools.click(DUPLICATE_BUTTON);
    }

    public void deleteClass() {
        driverTools.waitForElementToAppear(DELETE_BUTTON);
        driverTools.click(DELETE_BUTTON);
        driverTools.waitForElementToAppear(ACCEPT_BUTTON);
        driverTools.click(ACCEPT_BUTTON);
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public String getLocalImagePath(String filepath){
        File image = new File(filepath);
        return image.getAbsolutePath();
    }
}