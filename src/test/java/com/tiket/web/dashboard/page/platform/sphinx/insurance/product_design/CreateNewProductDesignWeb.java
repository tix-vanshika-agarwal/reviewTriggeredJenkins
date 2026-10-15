package com.tiket.web.dashboard.page.platform.sphinx.insurance.product_design;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

public class CreateNewProductDesignWeb<T extends WebDriver> extends WebPage<T> {
    public CreateNewProductDesignWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CreateNewProductDesignWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CreateNewProductDesignDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CreateNewProductDesignMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    // Create Insurance Name Section
    private static final Locator PRODUCT_DESIGN_NAME_INPUT = new Locator("PRODUCT_DESIGN_NAME_INPUT", By.id("product-design-name"));
    private static final Locator VERTICAL_DROPDOWN_INPUT = new Locator("VERTICAL_DROPDOWN_INPUT", By.xpath("//div[./input[@id='vertical']]"));
    private static final Locator VERTICAL_DROPDOWN_SELECTIONS = new Locator("VERTICAL_DROPDOWN_SELECTIONS", By.xpath("//div[@data-testid='input-select-item-undefined']//span"));
    private static final Locator TEMPLATE_NAME_DROPDOWN_INPUT = new Locator("TEMPLATE_NAME_DROPDOWN_INPUT", By.xpath("//div[./input[@id='template-name']]"));
    private static final Locator TEMPLATE_NAME_SELECTION = new Locator("TEMPLATE_NAME_SELECTION", By.xpath("//div[@data-testid='input-select-item-undefined']//span"));
    private static final Locator CREATE_INSURANCE_NAME_NEXT_BUTTON = new Locator("CREATE_INSURANCE_NAME_NEXT_BUTTON", By.xpath("//button[text()='Next']"));

    // Fill Out The Subtemplate Section
    private static final Locator FILL_SUBTEMPLATE_NEXT_BUTTON = new Locator("FILL_SUBTEMPLATE_NEXT_BUTTON", By.xpath("//button[text()='Next']"));

    // Manage the Subtemplate Section
    private static final Locator MANAGE_SUBTEMPLATE_SAVE_BUTTON = new Locator("MANAGE_SUBTEMPLATE_SAVE_BUTTON", By.xpath("//button[text()='Save']"));

    public void insertProductDesignName(String name) {
        driverTools.click(PRODUCT_DESIGN_NAME_INPUT);
        driverTools.sendKeys(PRODUCT_DESIGN_NAME_INPUT, name);
        driverTools.dummyWait(2);
    }

    public void selectDropdown(String type, Locator dropdown, Locator selections, String input) {
        driverTools.clickElementByCoordinateDWeb(dropdown);
        driverTools.dummyWait(2);
        List<WebElement> options = driverTools.waitForElementsToAppear(selections, 3).orElse(null);
        if (options == null || options.isEmpty()) throw new NoSuchElementException("No selection available for " + type);

        for (WebElement option : options) {
            if (driverTools.getText(option).equalsIgnoreCase(input)) {
                driverTools.scrollIntoWebView(option);
                driverTools.clickElementByCoordinateDWeb(option);
                break;
            }
        }
        driverTools.dummyWait(2);
    }

    public void selectVertical(String vertical) {
        selectDropdown("Vertical", VERTICAL_DROPDOWN_INPUT, VERTICAL_DROPDOWN_SELECTIONS, vertical);
    }

    public void selectTemplateName(String template) {
        selectDropdown("Template", TEMPLATE_NAME_DROPDOWN_INPUT, TEMPLATE_NAME_SELECTION, template);
    }

    public void clickCreateInsuranceNextButton() {
        driverTools.click(CREATE_INSURANCE_NAME_NEXT_BUTTON);
        driverTools.dummyWait(1);
    }

    public void clickFillSubtemplateNextButton() {
        driverTools.waitForElementToAppear(FILL_SUBTEMPLATE_NEXT_BUTTON, 2);
        driverTools.scrollElementToCenter(FILL_SUBTEMPLATE_NEXT_BUTTON);
        driverTools.click(FILL_SUBTEMPLATE_NEXT_BUTTON);
        driverTools.dummyWait(1);
    }

    public void clickManageSubtemplateSaveButton() {
        driverTools.waitForElementToAppear(MANAGE_SUBTEMPLATE_SAVE_BUTTON, 2);
        driverTools.scrollElementToCenter(MANAGE_SUBTEMPLATE_SAVE_BUTTON);
        driverTools.click(MANAGE_SUBTEMPLATE_SAVE_BUTTON);
        driverTools.dummyWait(1);
    }


}
