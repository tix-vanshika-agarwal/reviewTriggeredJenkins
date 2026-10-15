package com.tiket.web.dashboard.page.platform.sphinx.insurance.product_design;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;

public class SetOrderPageWeb<T extends WebDriver> extends WebPage<T> {
    public SetOrderPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> SetOrderPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new SetOrderPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new SetOrderPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator PRIMARY_INACTIVE_TOGGLES = new Locator("PRIMARY_INACTIVE_TOGGLES", By.xpath("//button[text()='Primary Inactive']"));
    private static final Locator PRIMARY_INACTIVE_NAMES = new Locator("PRIMARY_INACTIVE_NAMES", By.xpath("//tr[.//button[contains(text(), 'Primary Inactive')]]/td[2]/span"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[text()='Save']"));
    private static final Locator PRODUCT_DESIGN_NAME_ROWS = new Locator("PRODUCT_DESIGN_NAME_ROWS", By.xpath("//table/tbody/tr/td[2]/span"));
    private static final Locator PRIMARY_TOGGLES = new Locator("PRIMARY_TOGGLES", By.xpath("//table/tbody/tr/td[5]/div/button[1]"));

    public String setRandomProductDesignFromInactive() {
        List<WebElement> toggles = driverTools.waitForElementsToAppear(PRIMARY_INACTIVE_TOGGLES).orElse(null);
        List<WebElement> productNames = driverTools.waitForElementsToAppear(PRIMARY_INACTIVE_NAMES).orElse(null);

        if (toggles == null || toggles.isEmpty()) throw new NoSuchElementException("There are no Inactive Prduct Design");

        int selectedIndex = CoreFunctions.generateRandomInt(0, toggles.size()-1);
        driverTools.scrollIntoWebView(toggles.get(selectedIndex));
        driverTools.click(toggles.get(selectedIndex));

        String selectedProductName = driverTools.getText(productNames.get(selectedIndex));
        logger.log(String.format("Change status for Product %s", selectedProductName));

        return selectedProductName;
    }

    public void clickSaveButton() {
        driverTools.scrollElementToCenter(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
        driverTools.dummyWait(3);
    }

    public boolean verifyHasConvertedIntoPrimaryActive(String productDesign) {
        driverTools.dummyWait(3);
        List<WebElement> names = driverTools.waitForElementsToAppear(PRODUCT_DESIGN_NAME_ROWS).orElse(null);
        if (names == null || names.isEmpty()) throw new NoSuchElementException();

        int index = 0;
        for (WebElement name : names) {
            if (driverTools.getText(name).equalsIgnoreCase(productDesign)) break;
            index++;
        }

        List<WebElement> toggles = driverTools.waitForElementsToAppear(PRIMARY_TOGGLES).get();
        return driverTools.getText(toggles.get(index)).equalsIgnoreCase("Primary Active");
    }

    public void resetStateToPrimaryActive(String product) {
        int productNameIndex = getTableColumnIndex("Product Design Name");
        int actionsIndex = getTableColumnIndex("Actions");

        Locator buttonLoc = new Locator("PRIMARY_ACTIVE_BUTTON_" + product, By.xpath("//tr[./td[" + productNameIndex + "]/span[text()='" + product + "']]/td[" + actionsIndex + "]/div/button[1]"));

        Assertion.assertThat("Button text is Primary Active", driverTools.getText(buttonLoc).orElseThrow(), is("Primary Active"));
        driverTools.click(buttonLoc);
        clickSaveButton();
    }

    private int getTableColumnIndex(String colTitle) {
        List<WebElement> cols = driverTools.getDriver().findElements(By.xpath("//div[.//div/span[text()='Flight']]//th[text()='" + colTitle + "']/preceding-sibling::th"));
        return cols.size() + 1;
    }
}
