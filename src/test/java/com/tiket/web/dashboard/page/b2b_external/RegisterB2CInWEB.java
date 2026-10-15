package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.security.auth.login.FailedLoginException;
import java.io.File;
import java.util.*;


public class RegisterB2CInWEB<T extends WebDriver> extends WebPage<T> {

    private static final Locator CHOOSE_LANGUAGE_BTN = new Locator("CHOOSE_LANGUAGE_BTN", new By.ByXPath("//button[contains(@aria-label,'Choose language')]"));
    private static final Locator ENGLISH_LANGUAGE_OPTION = new Locator("ENGLISH_LANGUAGE_OPTION", new By.ByXPath("//a[contains(@href,'en')]//span[text()='English']"));
    private static final Locator REGISTER_NOW_BTN = new Locator("REGISTER_NOW_BTN", new By.ByXPath("//a[contains(text(),'Register Now')]"));
    private static final Locator EMAIL_TEXTFIELD = new Locator("EMAIL_TEXTFIELD", new By.ByXPath("//input[contains(@id, 'email')]"));
    private static final Locator CHECK_EMAIL_BTN = new Locator("CHECK_EMAIL_BTN", new By.ByXPath("//button[normalize-space()='Check Email']"));
    private static final Locator FULL_NAME_INPUT = new Locator("FULL_NAME_INPUT", new By.ByXPath("//input[@id='full-name' and @placeholder='Full Name']"));
    private static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", new By.ByXPath("//input[@id='phone-number' and @placeholder='Phone Number']"));
    private static final Locator REGISTER_BUTTON_LOGIN_PAGE = new Locator("REGISTER_BUTTON_LOGIN_PAGE", new By.ByXPath("//button[@type='submit' and @data-testid='btnSubmitForm' and contains(text(),'Register')]"));
    private static final Locator OTP_TEXTFIELD1 = new Locator("OTP_TEXTFIELD1", new By.ByXPath("(//input[contains(@class, 'otp_input')])[1]"));
    private static final Locator OTP_TEXTFIELD2 = new Locator("OTP_TEXTFIELD2", new By.ByXPath("(//input[contains(@class, 'otp_input')])[2]"));
    private static final Locator OTP_TEXTFIELD3 = new Locator("OTP_TEXTFIELD3", new By.ByXPath("(//input[contains(@class, 'otp_input')])[3]"));
    private static final Locator OTP_TEXTFIELD4 = new Locator("OTP_TEXTFIELD4", new By.ByXPath("(//input[contains(@class, 'otp_input')])[4]"));
    private static final Locator GET_REGISTRATION_FORM = new Locator("GET_REGISTRATION_FORM", new By.ByXPath("//button[normalize-space()='Get Registration Form']"));
    private static final Locator CONTINUE_TO_REGISTRATION_FORM = new Locator("CONTINUE_TO_REGISTRATION_FORM", new By.ByXPath("(//button[normalize-space()='Continue to Registration Form'])[1]"));
    private static final Locator ENTITY_TYPE = new Locator("ENTITY_TYPE", new By.ByXPath("//input[@id='entity-type' and @placeholder='Entity Type']"));
    private static final Locator ENTITY_TYPE_FIRST_OPTION = new Locator("ENTITY_TYPE_FIRST_OPTION", new By.ByXPath("(//ul[contains(@class,'overflow-y-scroll')]//li)[1]"));
    private static final Locator CORPORATE_TYPE = new Locator("CORPORATE_TYPE", new By.ByXPath("//input[@id='corporate-type' and @placeholder='Corporate Type']"));
    private static final Locator CORPORATE_TYPE_FIRST_OPTION = new Locator("CORPORATE_TYPE_FIRST_OPTION", new By.ByXPath("(//input[@name='Corporate Type' and @type='radio'])[1]"));
    private static final Locator COMPANY_NAME = new Locator("COMPANY_NAME", new By.ByXPath("//input[@id='company-name' and @placeholder='Company Name']"));
    private static final Locator INDUSTRY = new Locator("INDUSTRY", new By.ByXPath("//input[@id='industry' and @placeholder='Industry']"));
    private static final Locator INDUSTRY_FIRST_OPTION = new Locator("INDUSTRY_FIRST_OPTION", new By.ByXPath("//input[@id='industry']/following::ul[1]/li[1]"));
    private static final Locator MONTHLY_TRAVEL_BUDGET = new Locator("MONTHLY_TRAVEL_BUDGET", new By.ByXPath("//input[@id='monthly-travel-budget-range' and @placeholder='Monthly Travel Budget Range']"));
    private static final Locator MONTHLY_TRAVEL_BUDGET_FIRST_OPTION = new Locator("MONTHLY_TRAVEL_BUDGET_FIRST_OPTION", new By.ByXPath("//input[@id='monthly-travel-budget-range']/following::ul[1]/li[1]"));
    private static final Locator OFFICE_ADDRESS = new Locator("OFFICE_ADDRESS", new By.ByXPath("(//input[@placeholder='Enter the building or street name'])[1]"));
    private static final Locator OFFICE_ADDRESS_FIRST_OPTION = new Locator("OFFICE_ADDRESS_FIRST_OPTION", new By.ByXPath("(//div[contains(@class, 'text-base') and contains(@class, 'items-start')])[1]"));
    private static final Locator SAVE_ADDRESS_BTN = new Locator("SAVE_ADDRESS_BTN", new By.ByXPath("//button[normalize-space()='Save address']"));
    private static final Locator BILLING_ADDRESS_TOGGLE = new Locator("BILLING_ADDRESS_TOGGLE", new By.ByXPath("//span[normalize-space()='Same as the office address']/following::div[contains(@class,'Toggle_toggle')][1]"));
    private static final Locator POSITION = new Locator("POSITION", new By.ByXPath("//input[@id='position' and @placeholder='Position']"));
    private static final Locator FIRST_POSITION_OPTION = new Locator("FIRST_POSITION_OPTION", new By.ByXPath("//input[@id='position']/following::ul[1]/li[1]"));
    private static final Locator UPLOAD_FILE_BUTTON = new Locator("UPLOAD_FILE_BUTTON", new By.ByXPath("//span[normalize-space()='Upload File']/following-sibling::button"));
    private static final Locator CORPORATE_TIN = new Locator("CORPORATE_TIN", new By.ByXPath("//input[@id='corporate-tin' and @placeholder='Corporate TIN']"));
    private static final Locator CORPORATE_PIC_TOGGLE = new Locator("CORPORATE_PIC_TOGGLE", new By.ByXPath("(//span[normalize-space()='Same as registration data']/following::div[contains(@class,'Toggle_toggle')][1])[1]"));
    private static final Locator CORPORATE_PIC_CHECKBOX = new Locator("CORPORATE_PIC_CHECKBOX", new By.ByXPath("(//label[contains(@class,'Check_check')]/input[@type='checkbox'])[1]"));
    private static final Locator FINANCE_PIC_CHECKBOX = new Locator("FINANCE_PIC_CHECKBOX", new By.ByXPath("(//label[contains(@class,'Check_check')]/input[@type='checkbox'])[2]"));
    private static final Locator REGISTER_FULL_NAME_INPUT = new Locator("REGISTER_FULL_NAME_INPUT", new By.ByXPath("(//input[@id='full-name' and @placeholder='Full Name'])[1]"));
    private static final Locator FINANCE_PIC_TOGGLE = new Locator("FINANCE_PIC_TOGGLE", new By.ByXPath("(//span[normalize-space()='Same as registration data']/following::div[contains(@class,'Toggle_toggle')][1])[2]"));
    private static final Locator CONTINUE_BTN = new Locator("CONTINUE_BTN", new By.ByXPath("//button[normalize-space()='Continue']"));
    private static final Locator CONTINUE = new Locator("CONTINUE", new By.ByXPath("//button[contains(@class,'Button_variant_primary') and normalize-space()='Continue']"));
    private static final Locator SEND_BTN = new Locator("SEND_BTN", new By.ByXPath("//button[normalize-space()='Send']"));
    private static final Locator BROWSE_FILE_BTN = new Locator("BROWSE_FILE_BTN", By.xpath("//input[@type='file']"));
    private static final Locator BROWSE_DOC1_BTN = new Locator("BROWSE_DOC1_BTN", By.xpath("(//input[@type='file'])[1]"));
    private static final Locator UPLOAD_DOC1_BTN = new Locator("UPLOAD_DOC1_BTN", By.xpath("(//div[.//text()[normalize-space()='Harshit Test 1']]//button[contains(@class,'SingleFileUpload_suffix_icon')])[1]"));
    private static final Locator WAITING_FOR_DOC_APPROVAL = new Locator("WAITING_FOR_DOC_APPROVAL", By.xpath("//div[contains(@class,'corporate_step_container')]//span[normalize-space()='Waiting for Document Approval']"));
    private static final Locator CORPORATE_PIC_FULL_NAME_VALUE = new Locator("CORPORATE_PIC_FULL_NAME_VALUE", By.xpath("(//span[normalize-space()='Corporate PIC']/following::span[normalize-space()='Full Name']/following-sibling::span)[1]"));
    private static final Locator FINANCE_PIC_FULL_NAME_VALUE = new Locator("FINANCE_PIC_FULL_NAME_VALUE", By.xpath("(//span[normalize-space()='Finance PIC']/following::span[normalize-space()='Full Name']/following-sibling::span)[1]"));
    private static final Locator CORPORATE_PIC_PHONE_VALUE = new Locator("CORPORATE_PIC_PHONE_VALUE", By.xpath("(//span[normalize-space()='Corporate PIC']/following::span[normalize-space()='Phone Number']/following-sibling::span)[1]"));
    private static final Locator CORPORATE_PIC_EMAIL_VALUE = new Locator("CORPORATE_PIC_EMAIL_VALUE", By.xpath("(//span[normalize-space()='Corporate PIC']/following::span[normalize-space()='Email Address']/following-sibling::span)[1]"));
    private static final Locator PENDING_DOC_NAMES = new Locator("PENDING_DOC_NAMES", By.xpath("//span[normalize-space()='Not Uploaded']/preceding-sibling::span[1]"));
    private static final Locator CORPORATE_DETAILS_SECTION = new Locator("CORPORATE_DETAILS_SECTION", By.xpath("//span[normalize-space()='Corporate Details']"));
    private static final Locator PAYMENT_DETAILS_SECTION = new Locator("PAYMENT_DETAILS_SECTION", By.xpath("//span[normalize-space()='Payment Details']"));
    private static final Locator UPLOADED_DOCUMENTS_SECTION = new Locator("UPLOADED_DOCUMENTS_SECTION", By.xpath("//span[normalize-space()='Uploaded documents']"));
    private static final Locator TERMS_AND_CONDITIONS_CHECKBOX = new Locator("TERMS_AND_CONDITIONS_CHECKBOX", By.xpath("//span[contains(normalize-space(),'I accept and agree')]/preceding::label[1]"));

    private static final Locator BENEFIT_PLANS_SECTION = new Locator("BENEFIT_PLANS_SECTION", By.xpath("//span[normalize-space()='Benefit Plans']"));
    private static final Locator REGISTRATION_APPROVED = new Locator("REGISTRATION_APPROVED", By.xpath("//span[normalize-space()='Your Registration has been Approved!']"));

    private static Locator benefitTierRadio(String benefitTier) {
        return new Locator("BENEFIT_TIER_" + benefitTier.toUpperCase().replace(" ", "_"),
                By.xpath("//span[normalize-space()='" + benefitTier + "']/ancestor::div[contains(@class,'RadioCardOption_header_row')]//span//input[@type='radio']"));
    }

    private static Locator paymentMethodRadio(String paymentMethod) {
        return new Locator("PAYMENT_METHOD_" + paymentMethod.toUpperCase().replace(" ", "_"),
                By.xpath("//span[normalize-space()='" + paymentMethod + "']/ancestor::div[contains(@class, 'RadioCardOption_header_row')]//span//input[@type='radio']"));
    }


    public RegisterB2CInWEB(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> RegisterB2CInWEB<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new RegisterB2CInDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new RegisterB2CInMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    public void navigateToCorporateLandingPage() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/corporate");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/corporate");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://www.tiket.com/corporate");
        }
    }

    public void clearCorporateSessionCookies() {
        driverTools.deleteAllCookies();
        driverTools.refreshWebpage();
        driverTools.waitForWebToLoad();
    }

    public void selectEnglishLanguage() {
        driverTools.waitForElementToAppear(CHOOSE_LANGUAGE_BTN);
        driverTools.clickWithJavascriptExecutor(CHOOSE_LANGUAGE_BTN);
        driverTools.waitForElementToAppear(ENGLISH_LANGUAGE_OPTION);
        driverTools.clickWithJavascriptExecutor(ENGLISH_LANGUAGE_OPTION);
        driverTools.waitForWebToLoad();
    }

    public void clickRegisterNowButton() {
        driverTools.waitForElementToAppear(REGISTER_NOW_BTN);
        driverTools.clickWithJavascriptExecutor(REGISTER_NOW_BTN);
    }

    public void inputEmail(String email) {
        driverTools.waitForElementToAppear(EMAIL_TEXTFIELD);
        driverTools.click(EMAIL_TEXTFIELD);
        driverTools.sendKeys(EMAIL_TEXTFIELD, email);
    }

    public void inputOTPCode(String otp) throws InterruptedException, FailedLoginException {
        driverTools.waitForElementToAppear(OTP_TEXTFIELD1, 5);
        driverTools.waitForElementToBeClickable(OTP_TEXTFIELD1, 3).orElseThrow();
        driverTools.sendKeys(OTP_TEXTFIELD1, otp.substring(0, 1));
        driverTools.sendKeys(OTP_TEXTFIELD2, otp.substring(1, 2));
        driverTools.sendKeys(OTP_TEXTFIELD3, otp.substring(2, 3));
        driverTools.sendKeys(OTP_TEXTFIELD4, otp.substring(3, 4));

    }

    public void clickCheckEmailButton() {
        driverTools.waitForElementToAppear(CHECK_EMAIL_BTN);
        driverTools.scrollToElement(CHECK_EMAIL_BTN);
        driverTools.click(CHECK_EMAIL_BTN);
    }

    public void fillUserDetails(String fullName, String phoneNumber) {
        driverTools.waitForElementToAppear(PHONE_NUMBER_INPUT);
        driverTools.click(PHONE_NUMBER_INPUT);
        driverTools.sendKeys(PHONE_NUMBER_INPUT, phoneNumber);

        driverTools.waitForElementToAppear(FULL_NAME_INPUT, 15);
        driverTools.click(FULL_NAME_INPUT);
        driverTools.sendKeys(FULL_NAME_INPUT, fullName);
    }

    public void clickGetRegistrationFormButton() {
        driverTools.waitForElementToAppear(GET_REGISTRATION_FORM);
        driverTools.scrollToElement(GET_REGISTRATION_FORM);
        driverTools.click(GET_REGISTRATION_FORM);
    }

    public void clickContinueToRegistrationFormButton() {
        driverTools.waitForElementToAppear(CONTINUE_TO_REGISTRATION_FORM);
        driverTools.click(CONTINUE_TO_REGISTRATION_FORM);
    }

    public void fillSignUpDetailsToRegister(String registerFullName, String corporateName, String officeAddress, String filePath, String paymentMethod) {
        driverTools.waitForElementToAppear(ENTITY_TYPE);
        driverTools.click(ENTITY_TYPE);
        driverTools.clickWithJavascriptExecutor(ENTITY_TYPE_FIRST_OPTION);

        driverTools.waitForElementToAppear(CORPORATE_TYPE);
        driverTools.click(CORPORATE_TYPE);
        driverTools.clickWithJavascriptExecutor(CORPORATE_TYPE_FIRST_OPTION);

        driverTools.waitForElementToAppear(COMPANY_NAME);
        driverTools.clickWithJavascriptExecutor(COMPANY_NAME);
        driverTools.sendKeys(COMPANY_NAME, corporateName);

        driverTools.waitForElementToAppear(INDUSTRY);
        driverTools.clickWithJavascriptExecutor(INDUSTRY);
        driverTools.clickWithJavascriptExecutor(INDUSTRY_FIRST_OPTION);

        driverTools.waitForElementToAppear(MONTHLY_TRAVEL_BUDGET);
        driverTools.scrollToElement(MONTHLY_TRAVEL_BUDGET);
        driverTools.clickWithJavascriptExecutor(MONTHLY_TRAVEL_BUDGET);
        driverTools.waitForElementToAppear(MONTHLY_TRAVEL_BUDGET_FIRST_OPTION);
        driverTools.clickWithJavascriptExecutor(MONTHLY_TRAVEL_BUDGET_FIRST_OPTION);

        driverTools.waitForElementToAppear(OFFICE_ADDRESS);
        driverTools.scrollToElement(OFFICE_ADDRESS);
        driverTools.clickWithJavascriptExecutor(OFFICE_ADDRESS);
        driverTools.sendKeys(OFFICE_ADDRESS, officeAddress);
        driverTools.dummyWait(2);
        driverTools.waitForElementToAppear(OFFICE_ADDRESS_FIRST_OPTION);
        driverTools.dummyWait(2);

        driverTools.clickWithJavascriptExecutor(OFFICE_ADDRESS_FIRST_OPTION);
        driverTools.dummyWait(2);
        driverTools.waitForElementToAppear(SAVE_ADDRESS_BTN);
        driverTools.scrollToElement(SAVE_ADDRESS_BTN);
        driverTools.clickWithJavascriptExecutor(SAVE_ADDRESS_BTN);

        driverTools.dummyWait(2);
        driverTools.waitForElementToAppear(BILLING_ADDRESS_TOGGLE);
        driverTools.scrollToElement(BILLING_ADDRESS_TOGGLE);
        driverTools.clickWithJavascriptExecutor(BILLING_ADDRESS_TOGGLE);

        driverTools.waitForElementToAppear(POSITION);
        driverTools.clickWithJavascriptExecutor(POSITION);
        driverTools.waitForElementToAppear(FIRST_POSITION_OPTION);
        driverTools.clickWithJavascriptExecutor(FIRST_POSITION_OPTION);

        driverTools.waitForElementToAppear(REGISTER_FULL_NAME_INPUT);
        driverTools.clickWithJavascriptExecutor(REGISTER_FULL_NAME_INPUT);
        driverTools.sendKeys(REGISTER_FULL_NAME_INPUT, registerFullName);

        File data = new File(filePath);
        driverTools.waitForElementToAppear(UPLOAD_FILE_BUTTON, 5);
        driverTools.sendKeys(BROWSE_FILE_BTN, data.getAbsolutePath());

        driverTools.waitForElementToAppear(CORPORATE_TIN);
        driverTools.scrollToElement(CORPORATE_TIN);
        driverTools.clickWithJavascriptExecutor(CORPORATE_TIN);
        driverTools.sendKeys(CORPORATE_TIN, String.valueOf(CoreFunctions.randomInt(16)));

        Locator paymentMethodRadio = paymentMethodRadio(paymentMethod);
        driverTools.waitForElementToAppear(paymentMethodRadio);
        driverTools.scrollToElement(paymentMethodRadio);
        driverTools.clickWithJavascriptExecutor(paymentMethodRadio);

        driverTools.waitForElementToAppear(TERMS_AND_CONDITIONS_CHECKBOX);
        driverTools.scrollToElement(TERMS_AND_CONDITIONS_CHECKBOX);
        driverTools.clickWithJavascriptExecutor(TERMS_AND_CONDITIONS_CHECKBOX);
    }

    public void clickContinueButton() {
        driverTools.waitForElementToAppear(CONTINUE_BTN);
        driverTools.scrollToElement(CONTINUE_BTN);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BTN);
    }

    public void clickOnContinue() {
        driverTools.waitForElementToAppear(CONTINUE);
        driverTools.scrollToElement(CONTINUE);
        driverTools.clickWithJavascriptExecutor(CONTINUE);
    }


    public void clickSendButton() {
        driverTools.waitForElementToAppear(SEND_BTN);
        driverTools.click(SEND_BTN);
    }

    public void uploadDocument(String filePath) {
        File data = new File(filePath);
        // multiple file upload with same locator

        driverTools.waitForElementToAppear(UPLOAD_DOC1_BTN, 5);
        driverTools.sendKeys(BROWSE_DOC1_BTN, data.getAbsolutePath());
        driverTools.waitForElementToAppear(FINANCE_PIC_CHECKBOX);
        driverTools.sendKeys(BROWSE_DOC1_BTN, data.getAbsolutePath());
        driverTools.waitForElementToAppear(FINANCE_PIC_CHECKBOX);
    }

    public boolean checkRegistrationDetails(String fullName, String phoneNumber, String email) {
        boolean waitingForApproval = driverTools.waitForElementToAppear(WAITING_FOR_DOC_APPROVAL, 10).isPresent();
        driverTools.waitForElementToAppear(CORPORATE_PIC_FULL_NAME_VALUE);
        driverTools.scrollToElement(CORPORATE_PIC_FULL_NAME_VALUE);
        String corporatePICFullName = driverTools.getText(CORPORATE_PIC_FULL_NAME_VALUE).orElseThrow();
        String financePICFullName = driverTools.getText(FINANCE_PIC_FULL_NAME_VALUE).orElseThrow();
        String corporatePICPhone = driverTools.getText(CORPORATE_PIC_PHONE_VALUE).orElseThrow();
        String corporatePICEmail = driverTools.getText(CORPORATE_PIC_EMAIL_VALUE).orElseThrow();

        logger.log("Corporate PIC Full Name: " + corporatePICFullName);
        logger.log("Finance PIC Full Name: " + financePICFullName);
        logger.log("Corporate PIC Phone: " + corporatePICPhone);
        logger.log("Corporate PIC Email: " + corporatePICEmail);

        return waitingForApproval
                && corporatePICFullName.trim().equalsIgnoreCase(fullName.trim())
                && financePICFullName.trim().equalsIgnoreCase(fullName.trim())
                && corporatePICPhone.replaceAll("\\s","").contains(phoneNumber.replaceAll("\\s",""))
                && corporatePICEmail.trim().equalsIgnoreCase(email.trim());
    }

    public List<String> getAllDocuments() {
        driverTools.waitForElementToPresence(PENDING_DOC_NAMES);
        Optional<List<WebElement>> pendingElementsOptional = driverTools.elements(PENDING_DOC_NAMES);
        List<WebElement> pendingElements = pendingElementsOptional.get();
        List<String> docsToUpload = new ArrayList<>();
        for (WebElement element : pendingElements) {
            String docName = element.getText().trim();
            if (!docName.isEmpty()) {
                docsToUpload.add(docName);
            }
        }
        logger.log("All documents: " + docsToUpload);
        return docsToUpload;
    }

    public void uploadAllPendingRequiredDocuments(String filePath) {
        File file = new File(filePath);
        String absolutePath = file.getAbsolutePath();
        List<String> docsToUpload = getAllDocuments();
        logger.log("Documents to upload: " + docsToUpload);
        for (String docName : docsToUpload) {
            logger.log("Uploading dynamic document: " + docName);
            String cleanDocName = docName.replace(" ", "");
            String dynamicInputXpath = String.format("//span[contains(translate(normalize-space(), ' ', ''), '%s')]/following::input[@type='file'][1]", cleanDocName);
            Locator dynamicInputLocator = new Locator("DYNAMIC_INPUT_" + cleanDocName.toUpperCase(), By.xpath(dynamicInputXpath));
            driverTools.waitForElementToPresence(dynamicInputLocator, 10);
            driverTools.sendKeys(dynamicInputLocator, absolutePath);
        }
        driverTools.waitForElementToDisappear(PENDING_DOC_NAMES);
    }

    public boolean isWaitingForApprovalPageOpened() {
        return driverTools.waitForElementToAppear(WAITING_FOR_DOC_APPROVAL, 10).isPresent();
    }

    public boolean isUploadedDocumentSectionDisplayed() {
        return driverTools.waitForElementToAppear(UPLOADED_DOCUMENTS_SECTION, 10).isPresent();
    }

    public boolean isPaymentDetailsSectionDisplayed() {
        return driverTools.waitForElementToAppear(PAYMENT_DETAILS_SECTION, 10).isPresent();
    }

    public boolean isCorporateDetailsSectionDisplayed() {
        return driverTools.waitForElementToAppear(CORPORATE_DETAILS_SECTION, 10).isPresent();
    }

    public String getValueByFieldName(String fieldName) {
        String xpathExpression = String.format("//span[normalize-space()='%s']/following::span[1]", fieldName);
        Locator dynamicValueLocator = new Locator("DYNAMIC_INFO_VALUE_" + fieldName.toUpperCase().replace(" ", "_"), By.xpath(xpathExpression));
        driverTools.waitForElementToPresence(dynamicValueLocator, 5);
        return driverTools.getText(dynamicValueLocator).orElse("").trim();
    }

    public boolean verifyUploadedDocumentsExist(List<String> uploadedDocs) {
        for (String docName : uploadedDocs) {
            String xpath = String.format("//span[normalize-space()='Uploaded documents']/following::span[normalize-space()='%s'][1]", docName);
            Locator docLocator = new Locator("UPLOADED_" + docName.toUpperCase().replace(" ", "_"), By.xpath(xpath));
            if (!driverTools.waitForElementToAppear(docLocator, 10).isPresent()) {
                return false;
            }
        }
        return true;
    }

    public boolean isPaymentMethodSelected(String paymentMethod) {
        Locator paymentMethodRadio = paymentMethodRadio(paymentMethod);
        return driverTools.waitForElementToPresence(paymentMethodRadio, 5)
                .map(WebElement::isSelected)
                .orElse(false);
    }

    public boolean isBenefitPlansSectionDisplayed() {
        return driverTools.waitForElementToAppear(BENEFIT_PLANS_SECTION, 10).isPresent();
    }

    public void selectBenefitTier(String benefitTier) {
        Locator benefitTierRadio = benefitTierRadio(benefitTier);
        driverTools.waitForElementToPresence(benefitTierRadio);
        driverTools.scrollToElement(benefitTierRadio);
        driverTools.clickWithJavascriptExecutor(benefitTierRadio);
    }

    public boolean isBenefitTierSelected(String benefitTier) {
        Locator benefitTierRadio = benefitTierRadio(benefitTier);
        return driverTools.waitForElementToPresence(benefitTierRadio, 5)
                .map(WebElement::isSelected)
                .orElse(false);
    }

    public boolean isDocumentUploadPageDisplayed() {
        return driverTools.waitForElementToAppear(PENDING_DOC_NAMES, 5).isPresent();
    }

    public boolean isRegistrationApproved() {
        return driverTools.waitForElementToAppear(REGISTRATION_APPROVED, 10).isPresent();
    }
}
