package com.tiket.web.dashboard.page.b2bCorp_internal;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.util.Optional;

public class RegisteredCorporatePage<T extends WebDriver> extends BasePage<T>{

    private static final Locator REGISTERED_CORPORATE_HEADER = new Locator("REGISTERED_CORPORATE_HEADER", new By.ByXPath("//h1[text()='Registered Corporates']"));
    private static final Locator NEGOTIATION_PRE_ONBOARDING_TAB = new Locator("NEGOTIATION_PRE_ONBOARDING_TAB", By.xpath("//div[./div/span[text()='Negotiation & Pre-Onboarding']]"));
    private static final Locator DOCUMENT_SUBMISSION_BUTTON = new Locator("DOCUMENT_SUBMISSION_BUTTON", By.xpath("//button[.//span[text()='Document Submission']]"));
    private static final Locator PROCEED_TO_ONBOARDING_BUTTON = new Locator("PROCEED_TO_ONBOARDING_BUTTON", By.xpath("//button[.//span[text()='Proceed to Onboarding']]"));
    private static final Locator REQUIRED_DOCUMENTS_SPAN = new Locator("REQUIRED_DOCUMENTS_SPAN", By.xpath("//span[text()='Required and Optional Documents']"));
    private static final Locator SUPPORTING_DOCUMENTS = new Locator("SUPPORTING_DOCUMENTS", By.xpath("//span[text()='Supporting Documents']"));
    private static final Locator READY_ONBOARD_TEXT = new Locator("READY_ONBOARD_TEXT", By.xpath("//p[text()='Ready to be onboarded']"));
    private static final Locator STATUS_DROPDOWN = new Locator("STATUS_DROPDOWN", new By.ByXPath("//button[.//span[normalize-space(text())='Status']]"));
    private static final Locator APPROVE_OPTION = new Locator("APPROVE_OPTION", new By.ByXPath("(//div[contains(@class, 'flex') and .//span[text()='Approved']])[4]"));
    private static final Locator SAVE_BTN = new Locator("SAVE_BUTTON", new By.ByXPath("(//div[contains(@class,'sticky')])[4]//button//span[text()='Save']"));
    private static final Locator LEAD_DATA = new Locator("LEAD_DATA", new By.ByXPath("//table[@class='border-separate w-full']/tbody/tr[1]"));
    private static final Locator LEAD_NAME = new Locator("LEAD_NAME", new By.ByXPath("(//span[contains(@class, 'cursor-pointer')])[3]"));
    private static final Locator NEGOTIATION_BTN = new Locator("NEGOTIATION_BUTTON", new By.ByXPath("//div[@class='flex justify-between items-center gap-2 w-full']/span[text()='Negotiation & Pre-Onboarding']"));
    private static final Locator AM_EMAIL = new Locator("AM_EMAIL", new By.ByXPath("//input[@id='email-address']"));
    private static final Locator AM_NAME = new Locator("AM_NAME", new By.ByXPath("//input[@id='full-name']"));
    private static final Locator H1 = new Locator("H1", new By.ByXPath("//h1"));
    private static final Locator LEAD_NAME_HEADING   = new Locator("LEAD_NAME_HEADING", new By.ByXPath("//th[div//div[text()='Lead Name']]"));
    private static final Locator CORPORATE_NAME_HEADING   = new Locator("CORPORATE_NAME_HEADING", new By.ByXPath("//th[div//div[text()='Corporate Name']]"));
    private static final Locator TYPE_HEADING = new Locator("TYPE_HEADING", new By.ByXPath("//th[div//div[text()='Type']]"));
    private static final Locator REGISTRATION_PIC_HEADING   = new Locator("REGISTRATION_PIC_HEADING", new By.ByXPath("//th[div[text()='Registration PIC']]"));
    private static final Locator MONTHLY_BUDGET_HEADING   = new Locator("MONTHLY_BUDGET_HEADING", new By.ByXPath("//th[div//div[text()='Monthly Budget']]"));
    private static final Locator ACCOUNT_MANAGER_HEADING   = new Locator("ACCOUNT_MANAGER_HEADING", new By.ByXPath("//th[div[text()='Account Manager']]"));
    private static final Locator PAYMENT_METHOD_HEADING   = new Locator("PAYMENT_METHOD_HEADING", new By.ByXPath("//th[div//div[text()='Payment Method']]"));
    private static final Locator STATUS_HEADING   = new Locator("STATUS_HEADING", new By.ByXPath("//th[div//div[text()='Status']]"));
    private static final Locator FIRST_CORPORATE_NAME = new Locator("FIRST_CORPORATE_NAME", new By.ByXPath("(//tbody//tr)[1]//td[2]"));
    private static final Locator MANAGE_REGISTERED_PAGE_CORPORATE_NAME = new Locator("MANAGE_REGISTERED_PAGE_CORPORATE_NAME", new By.ByXPath("//span[contains(@class,'font-bold') and contains(@class,'text-[24px]')]"));
    private static final Locator TOGGLE_BUTTON = new Locator("TOGGLE_BUTTON", new By.ByXPath("//input[@type='checkbox' and contains(@class, 'Toggle_hidden_checkbox__xwVQA')]"));
    private static final Locator REGISTERED_BUTTON = new Locator("CORPORATE_DETAILS", new By.ByXPath("//div[normalize-space(text())='Registered']"));
    private static final Locator CORPORATE_DETAILS = new Locator("", new By.ByXPath("//span[normalize-space(text())='Corporate Details']"));
    private static final Locator PAYMENT_DETAILS = new Locator("PAYMENT_DETAILS", new By.ByXPath("//span[normalize-space(text())='Payment Details']"));
    private static final Locator ASSIGN_AM_BTN = new Locator("ASSIGN_AM_BUTTON", new By.ByXPath("//span[text()='Assign AM']"));
    private static final Locator UNDER_NEGOTIATION_BTN = new Locator("UNDER_NEGOTIATION_BUTTON", new By.ByXPath("//span[text()='Under Negotiation']"));
    private static final Locator DOCUMENT_SUBMISSION_BTN = new Locator("DOCUMENT_SUBMISSION_BUTTON", new By.ByXPath("//span[text()='Document Submission']"));
    private static final Locator PROCEED_TO_ONBOARDING_BTN = new Locator("PROCEED_TO_ONBOARDING_BUTTON", new By.ByXPath("//span[text()='Proceed to Onboarding']"));
    
    public RegisteredCorporatePage(Session<T> session) {
        super(session);
    }

    public boolean isPageOpened() {
        driverTools.waitForElementToAppear(REGISTERED_CORPORATE_HEADER);
        return  driverTools.isElementPresent(REGISTERED_CORPORATE_HEADER);
    }

    public void clickStatusDropdown(){
        driverTools.waitForElementToAppear(STATUS_DROPDOWN,10);
        driverTools.click(STATUS_DROPDOWN);
    }

    public void selectApprovedOption() {
        driverTools.waitForElementToAppear(APPROVE_OPTION, 10);
        driverTools.scrollToElement(APPROVE_OPTION);
        if(!driverTools.isElementPresent(APPROVE_OPTION)) {
            logger.log("Approved option is not present in the dropdown");
        }
        else {
            driverTools.click(APPROVE_OPTION);
        }

    }

    public void clickSaveButton() {
        driverTools.waitForElementToAppear(SAVE_BTN, 10);
        driverTools.click(SAVE_BTN);
    }

    public  boolean isLeadDataPresent(){
        driverTools.waitForElementToAppear(LEAD_DATA, 10);
        if(!driverTools.isElementPresent(LEAD_DATA)) {
            logger.log("Lead data is not present");
            return false;
        }
        else return true;
    }

    public void clickLeadName() {
        driverTools.waitForElementToAppear(LEAD_NAME, 10);
        driverTools.click(LEAD_NAME);
        driverTools.dummyWait(1);
        driverTools.switchToNewTab();

    }

    public void clickNegotiationButton() {
        driverTools.waitForElementToAppear(NEGOTIATION_BTN, 10);
        driverTools.click(NEGOTIATION_BTN);
    }

    public boolean isAMInfoPresent() {
        driverTools.waitForElementToAppear(AM_EMAIL, 10);

        boolean isAMEmailPresent = driverTools.isElementPresent(AM_EMAIL) &&
                driverTools.getAttribute(AM_EMAIL.by(), "value") != null &&
                !driverTools.getAttribute(AM_EMAIL.by(), "value").trim().isEmpty();

        boolean isAMNamePresent = driverTools.isElementPresent(AM_NAME) &&
                driverTools.getAttribute(AM_NAME.by(), "value") != null &&
                !driverTools.getAttribute(AM_NAME.by(), "value").isEmpty();

        return isAMEmailPresent && isAMNamePresent;
    }

    public boolean verifyHeading(String heading) {
        Optional<String> resultText = driverTools.getText(H1);
        if(driverTools.isElementDisplayed(H1)) {
            return resultText.isPresent() && resultText.get().equals(heading);
        }
        return false;
    }

    public boolean verifyLeadName() {
        return driverTools.isElementDisplayed(LEAD_NAME_HEADING);
    }

    public boolean verifyCorporateName() {
        return driverTools.isElementDisplayed(CORPORATE_NAME_HEADING);
    }

    public boolean verifyType() {
        return driverTools.isElementDisplayed(TYPE_HEADING);
    }

    public boolean verifyRegistrationPIC() {
        return driverTools.isElementDisplayed(REGISTRATION_PIC_HEADING);
    }

    public void clickNegotiationPreOnboardingTab() {
        driverTools.waitForElementToAppear(NEGOTIATION_PRE_ONBOARDING_TAB, 4);
        driverTools.click(NEGOTIATION_PRE_ONBOARDING_TAB);
    }

    public void clickDocumentSubmissionButton() {
        driverTools.click(DOCUMENT_SUBMISSION_BUTTON);
    }

    public void clickProceedToOnboardingButton() {
        driverTools.click(PROCEED_TO_ONBOARDING_BUTTON);
    }

    public boolean verifyReadyToOnboardTextDisplayed() {
        return driverTools.isElementPresent(READY_ONBOARD_TEXT);
    }

    public boolean verifyRequiredAndSupportingDocumentsDisplayed() {
        return driverTools.isElementPresent(REQUIRED_DOCUMENTS_SPAN) &&
                driverTools.isElementPresent(SUPPORTING_DOCUMENTS);
    }    
    
    public boolean verifyMonthlyBudget() {
        return driverTools.isElementDisplayed(MONTHLY_BUDGET_HEADING);
    }

    public boolean verifyAccountManager() {
        return driverTools.isElementDisplayed(ACCOUNT_MANAGER_HEADING);
    }

    public boolean verifyPaymentMethod() {
        return driverTools.isElementDisplayed(PAYMENT_METHOD_HEADING);
    }

    public boolean verifyStatus() {
        return driverTools.isElementDisplayed(STATUS_HEADING);
    }

    private String normalizeText(String text) {
        return text.replace("\u00A0", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    public String extractCorporateName() {
        driverTools.dummyWait(1);
        driverTools.waitForElementToAppear(FIRST_CORPORATE_NAME);
        return driverTools.getText(FIRST_CORPORATE_NAME).orElseThrow();
    }

    public boolean checkIfCorporateNameMatches(String valueToCheck) {
        driverTools.dummyWait(5);
        driverTools.waitForElementToAppear(MANAGE_REGISTERED_PAGE_CORPORATE_NAME,2);
       String text=driverTools.getText(MANAGE_REGISTERED_PAGE_CORPORATE_NAME).orElseThrow();
       return text.equals(valueToCheck);
    }

    public boolean isCheckboxDisabled() {
        return driverTools.element(TOGGLE_BUTTON)
                .map(el -> el.getAttribute("disabled") != null)
                .orElse(false);
    }

    public void clickOnRegistered() {
        driverTools.waitForElementToBeVisible(REGISTERED_BUTTON);
        driverTools.click(REGISTERED_BUTTON);
    }

    public boolean isCorporateDetailsPresent() {
        return driverTools.isElementPresent(CORPORATE_DETAILS);
    }

    public boolean isPaymentDetailsPresent() {
        return driverTools.isElementPresent(PAYMENT_DETAILS);
    }
    
    public boolean isAssignAMPresent() {
        return driverTools.isElementPresent(ASSIGN_AM_BTN);
    }

    public boolean isUnderNegotiationPresent() {
        return driverTools.isElementPresent(UNDER_NEGOTIATION_BTN);
    }

    public boolean isDocumentSubmissionPresent() {
        return driverTools.isElementPresent(DOCUMENT_SUBMISSION_BTN);
    }

    public boolean isProceedToOnBoardingPresent() {
        return driverTools.isElementPresent(PROCEED_TO_ONBOARDING_BTN);
    }

}
