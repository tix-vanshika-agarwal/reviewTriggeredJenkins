package com.tiket.web.dashboard.page.ttd.Ignis.IgnisThingsToDoEditPage;

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

public class IgnisThingsToDoEditPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisThingsToDoEditPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisThingsToDoEditPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisThingsToDoEditPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisThingsToDoEditPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator STEPS_NAME_TEXT = new Locator("STEPS_NAME_TEXT", By.xpath("//div[contains(@class,'management-step-item')]//p[@class='step-name']"));
    private static final Locator STEPS_AFTER_ACTIVE = new Locator("STEPS_AFTER_ACTIVE", By.xpath("//div[contains(@class,'management-step-item') and contains(@class,'active')]/following-sibling::*"));
    private static final Locator FOOTER_PRIMARY_BTN = new Locator("FOOTER_PRIMARY_BTN", By.xpath("//div[@class='mgmt-footer-menu']//button[contains(@class,'variant-primary')]"));
    private static final Locator POPUP_PRIMARY_BUTTON = new Locator("POPUP_PRIMARY_BUTTON", By.xpath("//div[@class='modal']//button[contains(@class,'primary')]"));
    private static final Locator BACK_TO_THINGS_TO_DO_PAGE_BTN = new Locator("BACK_TO_THINGS_TO_DO_PAGE_BTN", By.xpath("//div[@class='modal']//button[@class='submit-button']"));
    private static final Locator PRODUCT_DESC_INPUT = new Locator("PRODUCT_DESC_INPUT", By.xpath("//div[@role='textbox']"));
    private static final Locator PACKAGE_ADDITIONAL_INFO_INPUT = new Locator("PACKAGE_ADDITIONAL_INFO_INPUT", By.xpath("//div[@role='textbox']"));

    public void goToTab(String targetedText) {
        driverTools.dummyWait(1);
        List<WebElement> stepElement = driverTools.elementsUntilExist(STEPS_NAME_TEXT).get();
        boolean found = false;
        String targetTextId = targetedText.split("/")[0].toLowerCase();
        String targetTextEn = targetedText.split("/")[1].toLowerCase();
        for (WebElement e : stepElement) {
            String elementText = driverTools.getText(e).toLowerCase();
            if (elementText.contains(targetTextId) || elementText.equalsIgnoreCase(targetTextEn)) {
                found = true;
                driverTools.click(e);
            }
        }

        if (!found) {
            throw new NoSuchElementException("There is no such steps '"+targetedText+"'");
        }
    }

    public void clickContinueUntilSubmit() {
        int stepsAfterActiveStep = driverTools.elementsUntilExist(STEPS_AFTER_ACTIVE).get().size();
        for (int i = 0 ; i < stepsAfterActiveStep ; i++) {
            driverTools.waitForElementToAppear(FOOTER_PRIMARY_BTN);
            driverTools.click(FOOTER_PRIMARY_BTN);
            driverTools.dummyWait(1);
        }
    }

    public void clickSubmitButton() {
        driverTools.waitForElementToAppear(FOOTER_PRIMARY_BTN);
        driverTools.click(FOOTER_PRIMARY_BTN);

        driverTools.waitForElementToAppear(POPUP_PRIMARY_BUTTON);
        driverTools.click(POPUP_PRIMARY_BUTTON);
    }

    public void backToThingsToDoPage() {
        driverTools.waitForElementToAppear(BACK_TO_THINGS_TO_DO_PAGE_BTN);
        driverTools.click(BACK_TO_THINGS_TO_DO_PAGE_BTN);
    }

    public String editProductDescription(String updateDescription) {
        updateDescription += "-" + System.currentTimeMillis();
        driverTools.waitForElementToAppear(PRODUCT_DESC_INPUT);
        driverTools.clearInput(PRODUCT_DESC_INPUT);
        driverTools.sendKeys(PRODUCT_DESC_INPUT, updateDescription);

        return updateDescription;
    }

    public String editPackageAdditionalInfo(String updateAdditionalInfo) {
        updateAdditionalInfo += "-" + System.currentTimeMillis();
        driverTools.waitForElementToAppear(PACKAGE_ADDITIONAL_INFO_INPUT);
        driverTools.clearInput(PACKAGE_ADDITIONAL_INFO_INPUT);
        driverTools.sendKeys(PACKAGE_ADDITIONAL_INFO_INPUT, updateAdditionalInfo);

        return updateAdditionalInfo;
    }
}
