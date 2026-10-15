package com.tiket.web.dashboard.page.ttd.Ignis.ThingsToDoListPage;

import com.tiket.core.CoreFunctions;
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

public class IgnisThingsToDoListPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisThingsToDoListPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisThingsToDoListPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisThingsToDoListPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisThingsToDoListPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator LIVE_PRODUCT_CHIP_TEXT = new Locator("LIVE_PRODUCT_CHIP_TEXT", By.xpath("//div[@class='status-filter']/button[@name='ACTIVE']/span"));
    private static final Locator STATUS_DROPDOWN_SELECTION = new Locator("STATUS_DROPDOWN_SELECTION", By.xpath("//li[@role='menuitem']"));
    private static final Locator TAB_CHIPS = new Locator("TAB_CHIPS", By.xpath("//div[@class='status-filter']/button/span"));
    private static final Locator SEARCH_BY_NAME_INPUT = new Locator("SEARCH_BY_NAME_INPUT", By.xpath("//input[@name='search-by-name']"));
    private static final Locator PRODUCT_NAMES = new Locator("PRODUCT_NAMES", By.xpath("//div[@class='inventory-list-product-box']//h4"));
    private static final Locator POPUP_PRIMARY_BUTTON = new Locator("POPUP_PRIMARY_BUTTON", By.xpath("//div[@class='dialog-package-status-confirmation']//button[contains(@class,'variant-primary')]"));
    private static final Locator SNACKBAR_MESSAGE = new Locator("SNACKBAR_MESSAGE", By.xpath("//div[@class='snackbar-container']"));
    private static final Locator ADVANCE_CONFIG_BUTTON = new Locator("ADVANCE_CONFIG_BUTTON", By.xpath("//button[contains(@class,'btn-advanced-settings')]"));
    private static final Locator ADVANCE_CONFIG_OPTIONS = new Locator("ADVANCE_CONFIG_OPTIONS", By.xpath("//li[@role='menuitem']/span[@class='item__text']"));
    private static final Locator MANAGE_TICKET_DETAILS_BUTTON = new Locator("MANAGE_TICKET_DETAILS_BUTTON", By.xpath("//div[@class='filter-select']//button"));
    private static final Locator BULK_UPDATE_OPTIONS = new Locator("BULK_UPDATE_OPTIONS", By.xpath("//div[@class='bulk-edit-type-selection-popup']//h3"));
    private static final Locator BULK_UPDATE_POPUP_PRIMARY_BUTTON = new Locator("BULK_UPDATE_POPUP_PRIMARY_BUTTON", By.xpath("//div[@class='bulk-edit-type-selection-popup']//button[contains(@class,'variant-primary')]"));
    private static final Locator PACKAGE_OPTIONS = new Locator("PACKAGE_OPTIONS", By.xpath("//li[@role='menuitem']"));
    private static final Locator ADD_THINGS_TO_DO = new Locator("ADD_THINGS_TO_DO", By.xpath("//div[@class='clone-package-dialog__ttd-addition']/button"));
    private static final Locator PRODUCT_SEARCH_BAR = new Locator("PRODUCT_SEARCH_BAR", By.xpath("//div[@class='clone-package-select-product-dialog']//input"));
    private static final Locator ADD_PRODUCT_BUTTON = new Locator("ADD_PRODUCT_BUTTON", By.xpath("//div[@class='clone-package-select-product-dialog']//button[contains(@class,'primary')]"));
    private static final Locator CLONE_PACKAGE_SUBMIT_BUTTON = new Locator("CLONE_PACKAGE_SUBMIT_BUTTON", By.xpath("//div[@class='clone-package-dialog__wrapper']/div[@class='dialog-footer']/button[contains(@class,'primary')]"));
    private static final Locator APPLY_UPDATE_CLONE_PACKAGE_BUTTON = new Locator("APPLY_UPDATE_CLONE_PACKAGE_BUTTON", By.xpath("//div[@class='clone-preview__action']/button[contains(@class,'btn-primary')]"));
    private static final Locator CLONE_PACKAGE_DIALOG_PRIMARY_BUTTON = new Locator("CLONE_PACKAGE_DIALOG_PRIMARY_BUTTON", By.xpath("//div[@class='clone-preview-dialog__content']/button[contains(@class,'btn-primary')]"));
    private static final Locator OK_EDIT_BUTTON = new Locator("OK_EDIT_BUTTON", By.xpath("//div[@class='modal']//button[last()]"));
    private static final Locator TABS_TEXT = new Locator("TABS_TEXT", By.xpath("//div[@class='inventory-menu']/button/span"));
    private static final Locator PRODUCT_DESC = new Locator("PRODUCT_DESC", By.xpath("//div[@class='section-container' and .//h4[contains(text(),'Deskripsi') or contains(text(),'Description')]]//p"));
    private static final Locator PACKAGE_ADDITIONAL_INFO = new Locator("PACKAGE_ADDITIONAL_INFO", By.xpath("//div[@class='modal']//div[@class='package-information']//div[3]//p"));
    private static final Locator PRODUCT_OPTIONS = new Locator("PRODUCT_OPTIONS", By.xpath("//li[@role='menuitem']"));
    private static final Locator SUBMIT_EDIT_BUTTON = new Locator("SUBMIT_EDIT_BUTTON", By.xpath("//div[@class='dialog-footer']/button[contains(@class,'primary')]"));
    private static final Locator PAID_AMOUNT_BY_CUSTOMER_INPUT = new Locator("PAID_AMOUNT_BY_CUSTOMER_INPUT", By.xpath("//input[@name='publishedPriceInCents']"));
    private static final Locator ON_SITE_TICKET_PRICE_INPUT = new Locator("ON_SITE_TICKET_PRICE_INPUT", By.xpath("//input[@name='gimmickPriceInCents']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[@class='snackbar success visible']"));

    public int getLiveProductCount() {
        driverTools.dummyWait(1);
        return Integer.parseInt(driverTools.getText(LIVE_PRODUCT_CHIP_TEXT).orElseThrow().split(" \\(")[1].split("\\)")[0]);
    }

    public String getProductName(int index) {
        Locator productNameByIndexLocator = getProductNameLocatorByIndex(index);
        driverTools.waitForElementToAppear(productNameByIndexLocator);
        return driverTools.getText(productNameByIndexLocator).orElseThrow();
    }

    private Locator getProductNameLocatorByIndex(int index) {
        return new Locator("PRODUCT_NAME_"+index, By.xpath("(//div[@class='inventory-list-product-box']//h4)["+ index +"]"));
    }

    public void setProductStatus(String productName, String targetStatus) {
        Locator dropdownLocator = getStatusDropdownLocatorByProductName(productName);
        driverTools.click(dropdownLocator);
        driverTools.dummyWait(1);
        List<WebElement> dropdownSelections = driverTools.elementsUntilExist(STATUS_DROPDOWN_SELECTION).get();

        String targetStatusEn = targetStatus.split("/")[0];
        String targetStatusId = targetStatus.split("/")[1];

        boolean found = false;
        for (WebElement e : dropdownSelections) {
            String elementText = driverTools.getText(e);
            if (elementText.equalsIgnoreCase(targetStatusEn) || elementText.equalsIgnoreCase(targetStatusId)) {
                driverTools.click(e);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("There is not status for '"+targetStatus+"'");
        }
    }

    private Locator getStatusDropdownLocatorByProductName(String productName) {
        return new Locator("STATUS_DROPDOWN", By.xpath("//div[@class='inventory-list-product-box' and .//h4[text()='"+productName+"']]//div[@class='set-status-container']/button"));
    }

    public void goToTabChip(String chipTitle) {
        List<WebElement> tabChips = driverTools.elementsUntilExist(TAB_CHIPS).get();

        String chipTitleEn = chipTitle.split("/")[0];
        String chipTitleId = chipTitle.split("/")[1];

        for (WebElement e : tabChips) {
            String elementText = driverTools.getText(e).split(" \\(")[0];

            if (elementText.equalsIgnoreCase(chipTitleEn) || elementText.equalsIgnoreCase(chipTitleId)) {
                driverTools.click(e);
                break;
            }
        }
    }

    public void searchProduct(String productName) {
        driverTools.waitForElementToAppear(SEARCH_BY_NAME_INPUT);
        driverTools.clearInput(SEARCH_BY_NAME_INPUT);
        driverTools.sendKeys(SEARCH_BY_NAME_INPUT, productName);
        driverTools.dummyWait(1);
    }

    public boolean isProductExist(String productName) {
        List<String> productNames = driverTools.getTexts(PRODUCT_NAMES);

        for (String e : productNames) {
            if (e.equalsIgnoreCase(productName)) {
                return true;
            }
        }

        return false;
    }

    public void openProductPackageSection(String productName) {
        Locator expandPackageSectionByProductName = getExpandButtonByProductName(productName);
        driverTools.waitForElementToAppear(expandPackageSectionByProductName);
        driverTools.click(expandPackageSectionByProductName);
    }

    private Locator getExpandButtonByProductName(String productName) {
        return new Locator("EXPAND_PACKAGE_SECTION", By.xpath("//div[@class='inventory-list-product-box' and .//h4[text()='"+productName+"']]//button[contains(@class,'btn-expand-package-list')]"));
    }

    public void setPackageSoldout(String packageName, boolean targetStatus) {
        Locator enabledSoldoutButton = getSoldoutToggleButtonLocator(packageName, true);
        Locator disabledSoldoutButton = getSoldoutToggleButtonLocator(packageName, false);
        if (targetStatus && driverTools.waitForElementToAppear(disabledSoldoutButton).isPresent()) {
            driverTools.waitForElementToAbsence(SNACKBAR_MESSAGE);
            driverTools.click(disabledSoldoutButton);
        } else if (!targetStatus && driverTools.waitForElementToAppear(enabledSoldoutButton).isPresent()) {
            driverTools.waitForElementToAbsence(SNACKBAR_MESSAGE);
            driverTools.click(enabledSoldoutButton);
        } else {
            logger.log("Soldout status already " + targetStatus);
        }
    }

    private Locator getSoldoutToggleButtonLocator(String packageName, boolean enableStatus) {
        String enabledOrDisabled = enableStatus ? " is-on" : "";
        return new Locator("SOLDOUT_TOGGLE_BUTTON_"+enableStatus, By.xpath("//div[@class='package-item' and .//h4[text()='"+packageName+"']]//div[@class='bottom-body']//div[@class='package-action'][2]/div[@class='toggle-switch" + enabledOrDisabled + "']/span[1]"));
    }

    public boolean isPackageSoldoutBadgeVisible(String packageName) {
        return driverTools.waitForElementToAppear(getSoldoutBadgeLocatorByPackageName(packageName)).isPresent();
    }

    private Locator getSoldoutBadgeLocatorByPackageName(String packageName) {
        return new Locator("SOLDOUT_BADGE", By.xpath("//div[@class='package-item' and .//h4[text()='"+packageName+"']]//div[@class='top-body']//div[@class='badge badge-orange']"));
    }

    public void setPackageInactive(String packageName, boolean targetStatus) {
        Locator enabledInactiveButton = getInactiveToggleButtonLocator(packageName, true);
        Locator disabledInactiveButton = getInactiveToggleButtonLocator(packageName, false);
        if (targetStatus && driverTools.waitForElementToAppear(disabledInactiveButton).isPresent()) {
            driverTools.waitForElementToAbsence(SNACKBAR_MESSAGE);
            driverTools.click(disabledInactiveButton);
            driverTools.waitForElementToAppear(POPUP_PRIMARY_BUTTON);
            driverTools.click(POPUP_PRIMARY_BUTTON);
        } else if (!targetStatus && driverTools.waitForElementToAppear(enabledInactiveButton).isPresent()) {
            driverTools.waitForElementToAbsence(SNACKBAR_MESSAGE);
            driverTools.click(enabledInactiveButton);
        } else {
            logger.log("Inactive status already " + targetStatus);
        }
    }

    private Locator getInactiveToggleButtonLocator(String packageName, boolean enableStatus) {
        String enabledOrDisabled = enableStatus ? " is-on" : "";
        return new Locator("INACTIVE_TOGGLE_BUTTON_"+enableStatus, By.xpath("//div[@class='package-item' and .//h4[text()='"+packageName+"']]//div[@class='bottom-body']//div[@class='package-action'][1]/div[@class='toggle-switch" + enabledOrDisabled + "']/span[1]"));
    }

    public boolean isPackageInactiveBadgeVisible(String packageName) {
        return driverTools.waitForElementToAppear(getInactiveBadgeLocatorByPackageName(packageName)).isPresent();
    }

    private Locator getInactiveBadgeLocatorByPackageName(String packageName) {
        return new Locator("INACTIVE_BADGE", By.xpath("//div[@class='package-item' and .//h4[text()='"+packageName+"']]//div[@class='top-body']//div[@class='badge badge-red']"));
    }

    public void clickAdvanceConfigButton() {
        driverTools.waitForElementToAppear(ADVANCE_CONFIG_BUTTON);
        driverTools.click(ADVANCE_CONFIG_BUTTON);
        driverTools.dummyWait(1);
    }

    public void clickAdvanceConfigOption(String targetOption) {
        String targetOptionEn = targetOption.split("/")[0];
        String targetOptionId = targetOption.split("/")[1];

        List<WebElement> advanceConfigOptions = driverTools.elementsUntilExist(ADVANCE_CONFIG_OPTIONS).get();

        for (WebElement e : advanceConfigOptions) {
            String elementText = driverTools.getText(e);
            if (elementText.equalsIgnoreCase(targetOptionEn) || elementText.equalsIgnoreCase(targetOptionId)) {
                driverTools.click(e);
                break;
            }
        }
    }

    public void clickManageTicketDetails() {
        driverTools.waitForElementToAppear(MANAGE_TICKET_DETAILS_BUTTON);
        driverTools.click(MANAGE_TICKET_DETAILS_BUTTON);
    }

    public void chooseBulkUpdateType(String targetBulkUpdateType) {
        String targetBulkUpdateTypeId = targetBulkUpdateType.split("/")[0].toLowerCase();
        String targetBulkUpdateTypeEn = targetBulkUpdateType.split("/")[1].toLowerCase();

        List<WebElement> advanceConfigOptions = driverTools.elementsUntilExist(BULK_UPDATE_OPTIONS).get();

        for (WebElement e : advanceConfigOptions) {
            String elementText = driverTools.getText(e).toLowerCase();
            if (elementText.contains(targetBulkUpdateTypeId) || elementText.contains(targetBulkUpdateTypeEn)) {
                driverTools.click(e);
                break;
            }
        }
    }

    public void clickBulkUpdatePopupPrimaryButton() {
        driverTools.waitForElementToAppear(BULK_UPDATE_POPUP_PRIMARY_BUTTON);
        driverTools.click(BULK_UPDATE_POPUP_PRIMARY_BUTTON);
    }

    public void clickPackageOption(String packageName, String targetOption) {
        Locator locator = getThreeDotPackageOptionByPackageName(packageName);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
        driverTools.dummyWait(1);

        List<WebElement> optionElement = driverTools.elements(PACKAGE_OPTIONS).get();
        String targetOptionEn = targetOption.split("/")[0];
        String targetOptionId = targetOption.split("/")[1];
        boolean found = false;
        for (WebElement e : optionElement) {
            String elementText = driverTools.getText(e).toLowerCase();
            if (elementText.equalsIgnoreCase(targetOptionEn) || elementText.equalsIgnoreCase(targetOptionId)) {
                driverTools.click(e);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("There is no action for '"+targetOption+"'");
        } else {
            driverTools.dummyWait(1);
        }
    }

    private Locator getThreeDotPackageOptionByPackageName(String packageName) {
        return new Locator("THREE_DOT_PACKAGE_OPTION", By.xpath("(//div[@class='package-item' and .//h4[text()='"+packageName+"']])[1]//button[@class='inventory-list-product-package-configuration-button']"));
    }

    public void clonePackageAddThingsToDo(String productName) {
        clickAddThingsToDo();
        inputProductSearchBar(productName);
        chooseProduct(productName);
        clickAddProductButton();
        submitClonepackage();
    }

    private void submitClonepackage() {
        driverTools.waitForElementToAppear(CLONE_PACKAGE_SUBMIT_BUTTON);
        driverTools.click(CLONE_PACKAGE_SUBMIT_BUTTON);
    }

    private void clickAddProductButton() {
        driverTools.waitForElementToAppear(ADD_PRODUCT_BUTTON);
        driverTools.click(ADD_PRODUCT_BUTTON);
    }

    private void chooseProduct(String productName) {
        Locator locator = getCheckBoxLocatorByProductName(productName);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
    }

    private Locator getCheckBoxLocatorByProductName(String productName) {
        return new Locator("CHECKBOX_PRODUCT_"+productName, By.xpath("//tbody/tr[./td[text()='"+productName+"']]//div[@class='checkbox label-left']"));
    }

    private void inputProductSearchBar(String productName) {
        driverTools.waitForElementToAppear(PRODUCT_SEARCH_BAR);
        driverTools.clearInput(PRODUCT_SEARCH_BAR);
        driverTools.sendKeys(PRODUCT_SEARCH_BAR, productName);
        driverTools.dummyWait(1);
    }

    private void clickAddThingsToDo() {
        driverTools.waitForElementToAppear(ADD_THINGS_TO_DO);
        driverTools.click(ADD_THINGS_TO_DO);
    }

    public void clonePackagePreviewClickSubmit() {
        driverTools.waitForElementToAppear(APPLY_UPDATE_CLONE_PACKAGE_BUTTON);
        driverTools.click(APPLY_UPDATE_CLONE_PACKAGE_BUTTON);

        // confirmation popup
        driverTools.waitForElementToAppear(CLONE_PACKAGE_DIALOG_PRIMARY_BUTTON);
        driverTools.click(CLONE_PACKAGE_DIALOG_PRIMARY_BUTTON);

        // go to progress log
        driverTools.waitForElementToAppear(CLONE_PACKAGE_DIALOG_PRIMARY_BUTTON);
        driverTools.click(CLONE_PACKAGE_DIALOG_PRIMARY_BUTTON);
    }

    public void clickEditButton(String productName) {
        Locator locator = getEditButtonByProductName(productName);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);

        if (driverTools.waitForElementToAppear(OK_EDIT_BUTTON, 3).isPresent()) {
            driverTools.click(OK_EDIT_BUTTON);
        }
    }

    public void clickEditButton(String productName, String packageName) {
        Locator locator = getEditButtonByProductNameAndPackageName(productName, packageName);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);

        if (driverTools.waitForElementToAppear(OK_EDIT_BUTTON, 3).isPresent()) {
            driverTools.click(OK_EDIT_BUTTON);
        }
    }

    private Locator getEditButtonByProductNameAndPackageName(String productName, String packageName) {
        return new Locator("EDIT_BUTTON", By.xpath("//div[@class='inventory-list-product-box' and .//h4[text()='"+productName+"']]//div[@class='package-item' and .//h4[text()='"+packageName+"']]//button[contains(@class,'edit-btn')]"));
    }

    private Locator getEditButtonByProductName(String productName) {
        return new Locator("EDIT_BUTTON", By.xpath("//div[@class='inventory-list-product-box' and .//h4[text()='"+productName+"']]//button[contains(@class,'edit-btn')]"));
    }

    public void goToTab(String targetTabTitle) {
        driverTools.dummyWait(1);
        List<WebElement> optionElement = driverTools.elementsUntilExist(TABS_TEXT).get();
        String targetTabTitleEn = targetTabTitle.split("/")[0].toLowerCase();
        String targetTabTitleId = targetTabTitle.split("/")[1].toLowerCase();
        boolean found = false;
        for (WebElement e : optionElement) {
            String elementText = driverTools.getText(e).toLowerCase();
            if (elementText.contains(targetTabTitleEn) || elementText.contains(targetTabTitleId)) {
                driverTools.click(e);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("There is no tab for '"+targetTabTitle+"'");
        }
    }

    public boolean isProductStatusInReview(String productName) {
        Locator locator = getInReviewBadgeLocatorByProductName(productName);
        return driverTools.waitForElementToAppear(locator).isPresent();
    }

    private Locator getInReviewBadgeLocatorByProductName(String productName) {
        return new Locator("IN_REVIEW_BADGE", By.xpath("//div[@class='inventory-list-product-box' and .//h4[text()='"+productName+"']]//div[@class='badge badge-teal' and contains(text(),'review')]"));
    }

    public void clickProduct(String productName) {
        Locator locator = getProductTitleLocatorByProductName(productName);
        driverTools.waitForElementsToAppear(locator);
        driverTools.click(locator);
    }

    private Locator getProductTitleLocatorByProductName(String productName) {
        return new Locator("PRODUCT_TITLE", By.xpath("//div[@class='inventory-list-product-box']//h4[text()='"+productName+"']"));
    }

    public String getProductDescription() {
        return driverTools.getText(driverTools.waitForElementToAppear(PRODUCT_DESC).orElseThrow());
    }

    public void clickPackageTitle(String packageName) {
        Locator locator = getPackageTitleLocatorByPackageName(packageName);
        driverTools.waitForElementsToAppear(locator);
        driverTools.click(locator);
    }

    private Locator getPackageTitleLocatorByPackageName(String packageName) {
        return new Locator("PACKAGE_TITLE", By.xpath("//div[@class='package-item']//h4[text()='"+packageName+"']"));
    }

    public String getPackageAdditionalInfo() {
        return driverTools.getText(driverTools.waitForElementToAppear(PACKAGE_ADDITIONAL_INFO).orElseThrow());
    }

    public void clickProductOption(String productName, String targetOption) {
        Locator locator = getThreeDotProductOptionButtonByProductName(productName);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
        driverTools.dummyWait(1);

        List<WebElement> optionElement = driverTools.elements(PRODUCT_OPTIONS).get();
        String targetOptionEn = targetOption.split("/")[0];
        String targetOptionId = targetOption.split("/")[1];
        boolean found = false;
        for (WebElement e : optionElement) {
            String elementText = driverTools.getText(e).toLowerCase();
            if (elementText.equalsIgnoreCase(targetOptionEn) || elementText.equalsIgnoreCase(targetOptionId)) {
                driverTools.click(e);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("There is no action for '"+targetOption+"'");
        }
    }

    private Locator getThreeDotProductOptionButtonByProductName(String productName) {
        return new Locator("THREE_DOT_PRODUCT_OPTION", By.xpath("//div[@class='inventory-list-product-box' and .//h4[text()='"+productName+"']]//button[@class='inventory-list-product-package-configuration-button']"));
    }

    public void submitEditPricingConfiguration() {
        driverTools.waitForElementToAppear(SUBMIT_EDIT_BUTTON);
        driverTools.click(SUBMIT_EDIT_BUTTON);
    }

    public String editOnSiteTicketPrice(String targetOnSiteTicketPrice, String randomizeAddNumber) {
        int paramInt1 = Integer.parseInt(targetOnSiteTicketPrice);
        int paramInt2 = CoreFunctions.generateRandomInt(1,Integer.parseInt(randomizeAddNumber));

        targetOnSiteTicketPrice = String.valueOf(paramInt1 + paramInt2);
        driverTools.waitForElementToAppear(ON_SITE_TICKET_PRICE_INPUT);
        driverTools.clearInput(ON_SITE_TICKET_PRICE_INPUT);
        driverTools.sendKeys(ON_SITE_TICKET_PRICE_INPUT, targetOnSiteTicketPrice);

        return targetOnSiteTicketPrice;
    }

    public String editAmountPaidByCustomer(String targetAmountPaidByCustomer, String randomizeAddNumber) {
        int paramInt1 = Integer.parseInt(targetAmountPaidByCustomer);
        int paramInt2 = CoreFunctions.generateRandomInt(1,Integer.parseInt(randomizeAddNumber));

        targetAmountPaidByCustomer = String.valueOf(paramInt1 + paramInt2);
        driverTools.waitForElementToAppear(PAID_AMOUNT_BY_CUSTOMER_INPUT);
        driverTools.clearInput(PAID_AMOUNT_BY_CUSTOMER_INPUT);
        driverTools.sendKeys(PAID_AMOUNT_BY_CUSTOMER_INPUT, targetAmountPaidByCustomer);

        return targetAmountPaidByCustomer;
    }

    public boolean isSuccessSnackBarVisible() {
        return driverTools.waitForElementToAppear(SUCCESS_SNACKBAR).isPresent();
    }

    public String getOnSiteTicketPrice() {
        String regex = "[^a-zA-Z0-9]";
        String originalText = driverTools.getValue(ON_SITE_TICKET_PRICE_INPUT);
        String[] cleanedTextArray = originalText.replaceAll(regex, "-").split("-");

        return cleanedTextArray[0] + cleanedTextArray[1];
    }

    public String getAmountPaidByCustomer() {
        String regex = "[^a-zA-Z0-9]";
        String originalText = driverTools.getValue(PAID_AMOUNT_BY_CUSTOMER_INPUT);
        String[] cleanedTextArray = originalText.replaceAll(regex, "-").split("-");

        return cleanedTextArray[0] + cleanedTextArray[1];
    }
}
