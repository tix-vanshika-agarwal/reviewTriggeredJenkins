package com.tiket.web.dashboard.page.b2bCorp_internal;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CorporateDetailPageWeb<T extends WebDriver> extends WebPage<T> {
    public CorporateDetailPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CorporateDetailPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CorporateDetailPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CorporateDetailPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator CORPORATE_DATA_TAB = new Locator("CORPORATE_DATA_TAB", By.xpath("//button[.//span[text()='Corporate Data'] and contains(@class, 'rounded-lg')]"));
    private static final Locator CORPORATE_DEAL_CODE_TAB = new Locator("CORPORATE_DEAL_CODE_TAB", By.xpath("//button[.//span[text()='Corporate Deal Code'] and contains(@class, 'rounded-lg')]"));
    private static final Locator EMPLOYEE_LIST_TAB = new Locator("EMPLOYEE_LIST_TAB", By.xpath("//button[.//span[text()='Employee List'] and contains(@class, 'rounded-lg')]"));
    private static final Locator CORPORATE_DOCUMENTS_TAB = new Locator("CORPORATE_DOCUMENTS_TAB", By.xpath("//button[.//span[text()='Corporate Documents'] and contains(@class, 'rounded-lg')]"));
    private static final Locator CORPORATE_DOCUMENTS_BUTTON = new Locator("CORPORATE_DOCUMENTS_BUTTON", By.xpath("//button[.//span[text()='Corporate Documents']]"));
    private static final Locator E_AGREEMENT_SPAN = new Locator("E_AGREEMENT_SPAN", By.xpath("//span[text()='E-agreement']"));
    private static final Locator E_AGREEMENT_SUPPORTING_DOCUMENTS_SPAN = new Locator("E_AGREEMENT_SUPPORTING_DOCUMENTS_SPAN", By.xpath("//span[text()='E-agreement Supporting Documents']"));
    private static final Locator LEGAL_DOCUMENTS_SPAN = new Locator("LEGAL_DOCUMENTS_SPAN", By.xpath("//span[text()='Legal Documents']"));


    public void clickCorporateDocuments() {
        driverTools.click(CORPORATE_DOCUMENTS_TAB);
    }

    public boolean validateThereIsEAgreement() {
        return driverTools.isElementPresent(E_AGREEMENT_SPAN);
    }

    public boolean validateThereIsEAgreementSupportingDocument() {
        driverTools.waitForElementToAppear(E_AGREEMENT_SUPPORTING_DOCUMENTS_SPAN);
        return driverTools.isElementPresent(E_AGREEMENT_SUPPORTING_DOCUMENTS_SPAN);
    }

    public boolean validateThereIsLegalDocument() {
        driverTools.waitForElementToAppear(LEGAL_DOCUMENTS_SPAN);
        return driverTools.isElementPresent(LEGAL_DOCUMENTS_SPAN);
    }

    public boolean validateThereAre4Tabs() {
        driverTools.waitForElementToAppear(CORPORATE_DATA_TAB);
        return driverTools.isElementPresent(CORPORATE_DATA_TAB) &&
                driverTools.isElementPresent(CORPORATE_DEAL_CODE_TAB) &&
                driverTools.isElementPresent(EMPLOYEE_LIST_TAB) &&
                driverTools.isElementPresent(CORPORATE_DOCUMENTS_TAB);
    }
}
