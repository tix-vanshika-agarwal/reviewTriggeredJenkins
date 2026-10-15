package com.tiket.web.dashboard.page.platform.pricing.criterion_group;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class AddCriterionGroupPageWeb<T extends WebDriver> extends WebPage<T> {
    public AddCriterionGroupPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AddCriterionGroupPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new AddCriterionGroupPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new AddCriterionGroupPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator VERTICAL_INPUT = new Locator("VERTICAL_INPUT", By.xpath("//input[@id='vertical--distribution-channel']"));
    private static final Locator VERTICAL_INPUT_SELECTION = new Locator("VERTICAL_INPUT_SELECTION", By.xpath("//div[contains(@id, 'dropdown-options-verticalId')]"));
    private static final Locator VERTICAL_INPUT_SELECTION_SCROLLBAR = new Locator("VERTICAL_INPUT_SELECTION_SCROLLBAR", By.xpath("(//div[@class='display-scrollbar'])[1]/div"));
    private static final Locator CRITERION_INPUT = new Locator("CRITERION_INPUT", By.xpath("//input[@id='criterionkey']"));
    private static final Locator CRITERION_INPUT_SELECTION = new Locator("CRITERION_INPUT_SELECTION", By.xpath("//div[contains(@id, 'dropdown-options-criterion')]"));
    private static final Locator CRITERION_INPUT_SELECTION_SCROLLBAR = new Locator("CRITERION_INPUT_SELECTION_SCROLLBAR", By.xpath("(//div[@class='display-scrollbar'])[2]/div"));
    private static final Locator CRITERION_GROUP_NAME_INPUT = new Locator("CRITERION_GROUP_NAME_INPUT", By.xpath("//input[@id='criterion-group-name']"));
    private static final Locator MANUAL_INPUT_OPTION = new Locator("MANUAL_INPUT_OPTION", By.xpath("(//span[contains(@class, 'Radio_checkbox')])[1]"));
    private static final Locator CRITERION_VALUE_INPUT = new Locator("CRITERION_VALUE_INPUT", By.xpath("//input[@id='criterion-value']"));
    private static Locator criterionNewValueSelection(String criterionValue) {
        String xpath;
        if (!criterionValue.contains("'") && !criterionValue.contains("\"")) {
            xpath = "//span[contains(@class,'Text_text__') and contains(@class,'Text_variant_activity__')"
                    + " and contains(., 'Add \"" + criterionValue + "\" as new value')]";
        } else {
            xpath = "//span[contains(@class,'Text_text__') and contains(@class,'Text_variant_activity__')"
                    + " and contains(., 'as new value')]";
        }
        return new Locator("CRITERION_VALUE_NEW_VALUE_SELECTION", By.xpath(xpath));
    }
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[text()='Save']"));
    private static final Locator ADD_VALUE_BUTTON = new Locator("ADD_VALUE_BUTTON", By.xpath("//button[contains(text(),'Add Value')]"));
    private static final Locator CSV_UPLOAD_RADIO_OPTION = new Locator("CSV_UPLOAD_RADIO_OPTION", By.xpath("(//span[contains(@class,'Radio_checkbox')])[2]"));
    private static final Locator CSV_FILE_INPUT = new Locator("CSV_FILE_INPUT", By.xpath("//input[@type='file']"));
    private static final Locator DUPLICATE_NAME_ERROR = new Locator("DUPLICATE_NAME_ERROR", By.xpath("//*[contains(text(), 'Failed to save criterion group.')]"));
    private static final Locator YES_CONFIRMATION_BUTTON = new Locator("YES_CONFIRMATION_BUTTON", By.xpath("//button[contains(text(),'Yes, Delete')]"));
    private static final Locator MODAL_OVERLAY = new Locator("MODAL_OVERLAY", By.xpath("//div[contains(@class,'BaseModal_modal_overlay')]"));

    private String uploadedFileName;

    public void selectVertical(String vertical) {
        selectDropdown(VERTICAL_INPUT, VERTICAL_INPUT_SELECTION, VERTICAL_INPUT_SELECTION_SCROLLBAR, "Vertical", vertical);
    }

    public void selectCriterion(String criterion) {
        selectDropdown(CRITERION_INPUT, CRITERION_INPUT_SELECTION, CRITERION_INPUT_SELECTION_SCROLLBAR, "Criterion", criterion);
    }

    public void selectDropdown(Locator inputLoc, Locator selectionLoc, Locator scrollbarLoc, String type, String value) {
        driverTools.click(inputLoc);
        int howManyScroll = calculateHowManyScroll(scrollbarLoc, selectionLoc);
        double scrollPixelAmount = calculateHowManyPixelToScroll(scrollbarLoc, howManyScroll);
        WebElement scrollbar = driverTools.waitForElementToAppear(scrollbarLoc).orElseThrow();

        for (int i = 0; i < howManyScroll; i++) {
            driverTools.dummyWait(1);
            List<WebElement> selections = driverTools.waitForElementsToAppear(selectionLoc).orElse(null);
            if (selections == null || selections.isEmpty()) throw new NoSuchElementException("There are no selections for " + type);

            boolean shouldCont = true;
            for (WebElement selection : selections) {
                String text = driverTools.getText(selection);
                if (text == null || text.isEmpty()) {
                    // fallback for headless: getText() may return empty when element is off-viewport
                    String tc = selection.getAttribute("textContent");
                    text = (tc != null) ? tc.trim() : "";
                }
                if (text.equalsIgnoreCase(value)) {
                    jsClick(selection);
                    shouldCont = false;
                    break;
                }
            }
            if (!shouldCont) break;
            driverTools.scrollInsideElement(scrollbar, (int) scrollPixelAmount);
        }
    }

    public void inputCriterionGroupName(String criterionGroupName) {
        driverTools.click(CRITERION_GROUP_NAME_INPUT);
        driverTools.sendKeys(CRITERION_GROUP_NAME_INPUT, criterionGroupName);
    }

    public void selectInputManual() {
        driverTools.click(MANUAL_INPUT_OPTION);
    }

    public void inputCriterionValue(String criterionValue) {
        driverTools.click(CRITERION_VALUE_INPUT);
        driverTools.sendKeys(CRITERION_VALUE_INPUT, criterionValue);
        Locator selection = criterionNewValueSelection(criterionValue);
        driverTools.waitForElementToAppear(selection);
        driverTools.click(selection);
    }

    public void inputCriterionValueOnEdit(String criterionValue) {
        driverTools.scrollToElement(ADD_VALUE_BUTTON);
        driverTools.click(ADD_VALUE_BUTTON);
        driverTools.click(CRITERION_VALUE_INPUT);
        driverTools.sendKeys(CRITERION_VALUE_INPUT, criterionValue);
        Locator selection = criterionNewValueSelection(criterionValue);
        driverTools.waitForElementToAppear(selection);
        driverTools.click(selection);
    }

    public void clickSave() {
        driverTools.click(SAVE_BUTTON);
        driverTools.dummyWait(3);
    }

    public void selectCSVUploadOption() {
        driverTools.click(CSV_UPLOAD_RADIO_OPTION);
    }

    public void uploadCSVFile(String filePath) {
        WebElement fileInput = driverTools.waitForElementToPresence(CSV_FILE_INPUT).orElseThrow();
        driverTools.sendKeys(fileInput, new File(filePath).getAbsolutePath());
    }

    public boolean verifyDuplicateNameError() {
        return driverTools.waitForElementToAppear(DUPLICATE_NAME_ERROR, 5).isPresent();
    }

    private static Locator criterionValueDeleteButton(String value) {
        return new Locator("DELETE_VALUE_" + value, By.xpath(
            "//td[contains(@data-testid,'label') and normalize-space()='" + value + "']/following-sibling::td[contains(@data-testid,'action')]//button[text()='Delete']"
        ));
    }

    public void deleteExistingCriterionValue(String value) {
        Locator deleteBtn = criterionValueDeleteButton(value);
        driverTools.waitForElementToAppear(deleteBtn, 5);
        driverTools.click(deleteBtn);
        driverTools.click(YES_CONFIRMATION_BUTTON);
        driverTools.waitForElementToAbsence(MODAL_OVERLAY);
    }

    public boolean verifyValueExistsInEditPage(String value) {
        Locator valueCell = new Locator("VALUE_CELL_" + value, By.xpath(
            "//td[contains(@data-testid,'label') and normalize-space()='" + value + "']"
        ));
        return driverTools.waitForElementToAppear(valueCell, 5).isPresent();
    }

    private void jsClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        driverTools.clickWithJavascriptExecutor(element);
    }

    private int calculateHowManyScroll(Locator scrollbarLoc, Locator selectionLoc) {
        int scrollbarHeight = driverTools.getElementHeight(scrollbarLoc);
        int selectionHeight = driverTools.getElementHeight(selectionLoc);
        if (selectionHeight <= 0) return 10; // fallback: iterate 10 times when height is unavailable (e.g. headless)
        return Math.max(1, scrollbarHeight / selectionHeight);
    }

    private double calculateHowManyPixelToScroll(Locator scrollbarLoc, int howManyScroll) {
        int scrollbarHeight = driverTools.getElementHeight(scrollbarLoc);
        if (scrollbarHeight <= 0 || howManyScroll <= 0) return 50; // fallback scroll step
        double step = (double) scrollbarHeight / howManyScroll;
        return Math.max(1d, step);
    }


}
