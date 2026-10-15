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

public class ProductDesignDashboardPageWeb<T extends WebDriver> extends WebPage<T> {
    public ProductDesignDashboardPageWeb(Session<T> session) {
        super(session);
    }

    private static final Locator SET_ORDER_BUTTON = new Locator("SET_ORDER_BUTTON", By.xpath("//button[text()='Set Order']"));
    private static final Locator CREATE_NEW_PRODUCT_DESIGN = new Locator("CREATE_NEW_PRODUCT_DESIGN", By.xpath("(//button[@data-testid='ut-btn-create'])[2]"));

    private static final Locator TEMPLATE_NAME_SEARCH_INPUT = new Locator("TEMPLATE_NAME_SEARCH_INPUT", By.xpath("//div[./div/label[text()='Template Name']]//input"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[text()='Search']"));
    private static final Locator TEMPLATE_NAME_ROWS = new Locator("TEMPLATE_NAME_ROWS", By.xpath("//table/tbody/tr/td[2]/span"));
    private static final Locator CASE_GROUP_NAME_ROWS = new Locator("CASE_GROUP_NAME_ROWS", By.xpath("//table/tbody/tr/td[3]/span"));
    private static final Locator VERTICAL_ROWS = new Locator("VERTICAL_ROWS", By.xpath("//table/tbody/tr/td[4]/span"));

    private static final Locator DELETE_CONFIRMATION_YES_BUTTON = new Locator("DELETE_CONFIRMATION_YES_BUTTON", By.xpath("//button[text()='Yes, delete']"));
    private static final Locator FIRST_ROW_DELETE_BUTTON = new Locator("FIRST_ROW_DELETE_BUTTON", By.xpath("(//div[./div/a[contains(@href, 'action=delete')]])[1]"));
    private static final Locator CREDENTIAL_NAME_INPUT = new Locator("CREDENTIAL_NAME_INPUT", By.xpath("//input[@placeholder='Credential Name']"));
    private static final Locator EXPAND_BUTTONS = new Locator("EXPAND_BUTTONS", By.xpath("//a[@data-testid='ut-button-expand-collapse']"));
    private static final Locator ALL_PRODUCT_DESIGN_NAMES = new Locator("ALL_PRODUCT_DESIGN_NAMES",By.xpath("//div[@data-testid='collapse-container' and @aria-hidden='false']//table[contains(@class,'Table_table__xw3hf')]//tbody//tr[td]//td[2]"));


    public static <T extends WebDriver> ProductDesignDashboardPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new ProductDesignDashboardPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new ProductDesignDashboardPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public void clickSetOrderButton() {
        driverTools.click(SET_ORDER_BUTTON);
    }

    public void clickCreateNewProductDesignButton() {
        driverTools.click(CREATE_NEW_PRODUCT_DESIGN);
        driverTools.waitForWebToLoad();
    }

    public void searchProductDesignByName(String name) {
        driverTools.click(TEMPLATE_NAME_SEARCH_INPUT);
        driverTools.sendKeys(TEMPLATE_NAME_SEARCH_INPUT, name);
        driverTools.dummyWait(1);
        driverTools.click(SEARCH_BUTTON);
        driverTools.dummyWait(3);
    }

    public int getRowIndexWithTemplateName(String target) {
        List<WebElement> names = driverTools.waitForElementsToAppear(TEMPLATE_NAME_ROWS).orElse(null);
        if (names == null || names.isEmpty()) throw new NoSuchElementException("There is no Product Design name");

        int index = 0;
        boolean isFound = false;
        for(WebElement name : names) {
            if (driverTools.getText(name).equalsIgnoreCase(target)) {
                isFound = true;
                break;
            }
            index++;
        }
        if (!isFound) throw new NoSuchElementException("The product design cannot be found");
        return index;
    }

    public String getDataStringFromRowWithIndex(String type, Locator rows, int index) {
        List<WebElement> datas = driverTools.waitForElementsToAppear(rows).orElse(null);
        if (datas == null || datas.isEmpty()) throw new NoSuchElementException("There is no data for " + type);

        return driverTools.getText(datas.get(index));
    }

    public String getCaseGroupNameWithIndex(int index) {
        return getDataStringFromRowWithIndex("Case Group", CASE_GROUP_NAME_ROWS, index);
    }

    public String getVerticalWithIndex(int index) {
        return getDataStringFromRowWithIndex("Vertical", VERTICAL_ROWS, index);
    }

    public boolean validateProductDesignExistWithCorrectNecessaryData(String productDesignName, String caseGroupName, String verticalName) {
        searchProductDesignByName(productDesignName);
        int index = getRowIndexWithTemplateName(productDesignName);
        return caseGroupName.equalsIgnoreCase(getCaseGroupNameWithIndex(index)) &&
                verticalName.equalsIgnoreCase(getVerticalWithIndex(index));
    }

    public void deleteRecentlyCreatedProductDesign() {
        driverTools.clickElementByCoordinateDWeb(FIRST_ROW_DELETE_BUTTON);
        driverTools.waitForElementToAppear(DELETE_CONFIRMATION_YES_BUTTON, 2);
        driverTools.click(DELETE_CONFIRMATION_YES_BUTTON);
    }

    public void inputCredentialName(String credentialName) {
        driverTools.waitForElementToAppear(CREDENTIAL_NAME_INPUT, 3);
        driverTools.click(CREDENTIAL_NAME_INPUT);
        driverTools.clearInput(CREDENTIAL_NAME_INPUT);
        driverTools.sendKeys(CREDENTIAL_NAME_INPUT, credentialName);
        driverTools.click(SEARCH_BUTTON);
        driverTools.dummyWait(1);
    }

    public void expandAllTemplateRows() {
        driverTools.dummyWait(1);
        List<WebElement> expandButtons = driverTools.waitForElementsToAppear(EXPAND_BUTTONS).orElse(null);
        if (expandButtons != null && !expandButtons.isEmpty()) {
            for (WebElement expandButton : expandButtons) {
                try {
                    driverTools.click(expandButton);
                    driverTools.dummyWait(1);
                } catch (Exception e) {
                    // Continue if element is not clickable
                }
            }
        }
        driverTools.dummyWait(2);
    }

    public boolean verifyAllExpandedRowsContainCredentialName(String credentialName) {
        driverTools.dummyWait(1);
        expandAllTemplateRows();
        
        List<WebElement> productDesignElements = driverTools.waitForElementsToAppear(ALL_PRODUCT_DESIGN_NAMES).orElse(null);
        if (productDesignElements == null || productDesignElements.isEmpty()) {
            return false;
        }

        // Check that ALL expanded rows contain the credentialName
        for (WebElement element : productDesignElements) {
            String text = driverTools.getText(element).toLowerCase();
            if (!text.contains(credentialName.toLowerCase())) {
                return false; // If any row doesn't contain credentialName, return false
            }
        }
        return true; // All rows contain the credentialName
    }
}
