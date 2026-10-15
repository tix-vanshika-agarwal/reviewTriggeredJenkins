package com.tiket.web.page.platform.sphinx.insurance;

import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Locator;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public abstract class ClaimCenterPageWeb<T extends WebDriver> extends WebPage<T> {
    public ClaimCenterPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> ClaimCenterPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new ClaimCenterPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new ClaimCenterPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator INSURANCE_CLAIM_LOGIN_BUTTON = new Locator("INSURANCE_CLAIM_LOGIN_BUTTON", new By.ByXPath("//button[.//span[normalize-space()='Log in now' or normalize-space()='Log in sekarang' or normalize-space()='Masuk sekarang']]"));
    private static final Locator INSURANCE_ORDER_DETAILS_ICON = new Locator("INSURANCE_ORDER_DETAILS_ICON", new By.ByXPath("//a[text()=\"Detail\" or text()=\"Details\"]"));
    private static final Locator INSURANCE_ORDER_DETAILS_FAQ_LINK = new Locator("INSURANCE_ORDER_DETAILS_FAQ_LINK", new By.ByXPath("//span[text()='FAQ']"));
    private static final Locator INSURANCE_POLICY_COLLAPSE_ICON = new Locator("INSURANCE_POLICY_COLLAPSE_ICON", new By.ByXPath("//div[contains(@class,'FAQContentCollapse_card_header') and @data-testid='collapse-faq']"));
    private static final Locator INSURANCE_CLAIM_CENTER_COLLAPSE_ICON = new Locator("INSURANCE_CLAIM_CENTER_COLLAPSE_ICON", new By.ByXPath("//div[@data-testid='collapse-faq']//span[text()='How to Claim' or text()='Cara Klaim']"));
    private static final Locator INSURANCE_CONTACT_CENTER_COLLAPSE_ICON = new Locator("INSURANCE_CONTACT_CENTER_COLLAPSE_ICON", new By.ByXPath("//div[@data-testid='collapse-faq']//span[text()='Contact Cermati Protect' or text()='Kontak Cermati Protect']"));
    private static final Locator CLAIM_LIST_DETAIL_BUTTON = new Locator("CLAIM_LIST_DETAIL_BUTTON", new By.ByXPath("//a[@data-testid='card-details' and (text()='Details' or text()='Detail')]"));
    private static final Locator CLAIM_HISTORY_DETAILS = new Locator("CLAIM_HISTORY_DETAILS", new By.ByXPath("//span[contains(@class,'InsuranceCard_detail_button') and normalize-space()='Detail']"));
    private static final Locator CLAIM_DETAIL_HEADER = new Locator("CLAIM_DETAIL_HEADER", new By.ByXPath("//*[@data-testid='card-collapsible'][.//span[normalize-space()='Coverage and Policy' or normalize-space()='Cakupan & Polis']]"));

    private static final Locator EXPIRED_LIST_DETAIL_BUTTON=new Locator("EXPIRED_LIST_DETAIL_BUTTON",new By.ByXPath("//a[(text()='Details' or text()='Detail') and @data-testid='card-details']"));
    private static final Locator FAQ_BUTTON=new Locator("FAQ_BUTTON",new By.ByXPath("//span[@data-testid='insurance-card-faq' and text()='FAQ']"));
    private static final Locator INSURANCE_POLICY_OPTION=new Locator("INSURANCE_POLICY_OPTION",new By.ByXPath("//span[text()='Insurance Policy']"));
    private static final Locator HOW_TO_CLAIM_OPTION = new Locator("HOW_TO_CLAIM_OPTION", new By.ByXPath("//h2[normalize-space(text())='how to claim' or normalize-space(text())='cara klaim']"));
    private static final Locator CONTACT_CERMATI_PROTECT_OPTION=new Locator("CONTACT_CERMATI_PROTECT_OPTION",new By.ByXPath("//span[text()='Contact Cermati Protect' or text()='Kontak Cermati Protect']"));

    private static final Locator EXPIRED_INSURANCE_DETAILS=new Locator("EXPIRED_INSURANCE_DETAILS",new By.ByXPath("//div[contains(text(), 'Order ID')]"));

    private static final Locator CLAIM_LIST_DES = new Locator("CLAIM_LIST_DES", new By.ByXPath("//button[.//span[normalize-space(text())='Claim List' or normalize-space(text())='Daftar Klaim']]"));
    private static final Locator EXPIRED_INSURANCE_DES=new Locator("EXPIRED_INSURANCE_DES",new By.ByXPath("//span[text()='Expired Insurance' or text()='Asuransi Kedaluwarsa']"));
    private static final Locator CLAIM_HISTORY_ORDER=new Locator("CLAIM_HISTORY_ORDER",new By.ByXPath("//div[contains(@class,'OrderCard_wrapper')]//span[contains(normalize-space(),'Order ID')]"));
    private static final Locator CLAIM_PAGE_ORDER_TAB=new Locator("CLAIM_PAGE_ORDER_TAB", new By.ByXPath("//div[contains(@class,'OrderSection_tab_list')]"));
    private static final Locator CLAIM_LIST_POPUP=new Locator("CLAIM_LIST_POPUP",new By.ByXPath("//button[text()='Skip']"));
    private static final Locator INSURANCE_CARD_CLAIM = new Locator("INSURANCE_CARD_CLAIM", new By.ByXPath("//*[@data-testid='claim-card']"));
    private static final String CLAIM_BUTTON_LABELS =
            "contains(normalize-space(.),'Submit claim') or contains(normalize-space(.),'Kirim klaim')"
                    + " or contains(normalize-space(.),'Add claim') or contains(normalize-space(.),'Tambah klaim')"
                    + " or contains(normalize-space(.),'New claim') or contains(normalize-space(.),'Klaim baru')";
    private static final Locator ADD_OR_NEW_CLAIM_BTN = new Locator("ADD_OR_NEW_CLAIM_BTN",
            new By.ByXPath("//button[" + CLAIM_BUTTON_LABELS + "]"));
    private static final Locator WELCOME_POPUP = new Locator("WELCOME_POPUP", new By.ByXPath("//section[contains(@class, 'Modal_modal_popup')]"));
    private static final Locator WELCOME_POPUP_CLOSE_BUTTON = new Locator("WELCOME_POPUP_CLOSE_BUTTON", new By.ByXPath("//button[@data-testid='modal-close-button']"));
    private static final Locator CLAIM_ID_IN_DETAILS = new Locator("CLAIM_ID_IN_DETAILS", new By.ByXPath("//span[@class='Text_text__xsku9 Text_size_b2__KfDvI Text_weight_bold__PWgjm']"));
    private static final Locator FLIGHT_ROUTE_IN_DETAILS = new Locator("FLIGHT_ROUTE_IN_DETAILS", new By.ByXPath("//span[@data-testid='separator-description-1']"));
    private static final Locator ACTIVE_CLAIM_IN_DETAILS = new Locator("ACTIVE_CLAIM_IN_DETAILS", new By.ByXPath("//span[text()='Klaim diproses' or text()='Processing your claim']"));
    private static final String ADD_PROTECTION_BUTTON_XPATH = "//button[@data-testid='add-protection-button' or .//span[normalize-space(.)='Add protection' or normalize-space(.)='Tambah perlindungan']]";
    private static final Locator ADD_PROTECTION_BUTTON = new Locator("ADD_PROTECTION_BUTTON", new By.ByXPath(ADD_PROTECTION_BUTTON_XPATH));
    private static final Locator PROTECTION_CARD = new Locator("PROTECTION_CARD", new By.ByXPath("//div[@data-testid='protection-card']"));
    private static final Locator PROTECTION_CARD_BUTTON = new Locator("PROTECTION_CARD_BUTTON", new By.ByXPath("//button[@data-testid='protection-card:add-button']"));
    private static final Locator SELECT_BUTTON = new Locator("SELECT_BUTTON", new By.ByXPath("//button[span[normalize-space(text())='Select' or normalize-space(text())='Pilih']]"));
    private static final Locator CONTINUE_TO_PAY_BTN = new Locator("CONTINUE_TO_PAY_BTN", new By.ByXPath("//button[contains(@class, 'Button_button')][.//span[normalize-space(.)='Continue to payment' or normalize-space(.)='Lanjut ke pembayaran']]"));
    private static final Locator ORDER_CHANGED_MODAL_CONTINUE_BTN = new Locator("ORDER_CHANGED_MODAL_CONTINUE_BTN", new By.ByXPath("//button[contains(normalize-space(.),'Manage orders') or contains(normalize-space(.),'Kelola pesanan')]/following::button[contains(normalize-space(.),'Continue to payment') or contains(normalize-space(.),'Lanjut ke pembayaran')][1]"));
    private static final Locator COVERAGE_AND_POLICY = new Locator("COVERAGE_AND_POLICY", By.xpath("//h2[normalize-space(text())='Coverage and Policy' or normalize-space(text())='Perlindungan dan Kebijakan']"));
    private static final Locator CONTINUE_BTN = new Locator("CONTINUE_BTN", new By.ByXPath("//button[@data-testid='claim-form-submit']"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", new By.ByXPath("//button[normalize-space(.)='Continue' or normalize-space(.)='Lanjutkan']"));
    private static final Locator RADIO_BTN = new Locator("RADIO_BTN", new By.ByXPath("//span[contains(@class, 'Radio_icon')]"));
    private static final Locator TNC_AGREEMENT = new Locator("TNC_AGREEMENT", new By.ByXPath("//input[@data-testid='tnc-agreement']"));
    private static final Locator CONTINUE_AFTER_TNC_AGREEMENT = new Locator("CONTINUE_AFTER_TNC_AGREEMENT", new By.ByXPath("//button[@data-testid='tnc-submit']"));
    private static final Locator INPUT_ID_NUMBER = new Locator("INPUT_ID_NUMBER", new By.ByXPath("//input[@data-testid='field-insured-identity-number']"));
    private static final Locator INPUT_ADDRESS = new Locator("INPUT_ADDRESS", new By.ByXPath("//input[@data-testid='field-insured-address']"));
    private static final Locator INPUT_ACCOUNT_NUMBER = new Locator("INPUT_ACCOUNT_NUMBER", new By.ByXPath("//input[@data-testid='account-number']"));
    private static final Locator ADD_BANK_NAME = new Locator("ADD_BANK_NAME", By.xpath("//*[@id='bank-name']"));
    private static final Locator INPUT_BANK_NAME = new Locator("INPUT_BANK_NAME", By.xpath("//div[contains(@class,'SearchBox_placeholder')]//input"));
    private static final Locator FIRST_BANK = new Locator("FIRST_BANK", By.xpath("//div[@data-testid='dropdown-item-0']"));
    private static final Locator CHECK_BTN = new Locator("CHECK_BTN", By.xpath("//span[@data-testid='account-number-check']"));
    private static final Locator INCIDENT_DATE = new Locator("INCIDENT_DATE", By.xpath("(//button[contains(@class, 'styles_clickable_icon')])[1]"));
    private static final Locator DATE_PICKER_ICON = new Locator("DATE_PICKER_ICON", By.xpath("//input[@data-testid='date-input']/following::button[contains(@class,'styles_clickable_icon')][1]"));
    private static final Locator SAVE_BTN = new Locator("SAVE_BTN", By.xpath("//button[normalize-space(.)='Save' or normalize-space(.)='Simpan']"));
    private static final Locator INCIDENT_LOCATION = new Locator("INCIDENT_LOCATION", By.xpath("//input[@data-testid='field-incident-location']"));
    private static final Locator INCIDENT_CHRONOLOGY = new Locator("INCIDENT_CHRONOLOGY", By.xpath("//textarea[@data-testid='field-incident-chronology']"));
    private static final Locator CLAIM_FORM_PAGE_TITLE = new Locator("CLAIM_FORM_PAGE_TITLE", By.xpath("//h1[text()='Complete the Claim Form']"));
    private static final Locator SELECT_ROUTE_SECTION = new Locator("SELECT_ROUTE_SECTION", By.xpath("//*[normalize-space(.)='Select a route' or normalize-space(.)='Pilih rute']"));
    private static final Locator SELECT_PASSENGER_SECTION = new Locator("SELECT_PASSENGER_SECTION", By.xpath("//*[normalize-space(.)='Select passenger' or normalize-space(.)='Pilih penumpang']"));
    private static final Locator TRIP_DETAILS = new Locator("TRIP_DETAILS", new By.ByXPath("(//*[@data-testid='trip-details'])[1]"));
    private static final Locator ROUTE_OPTION_INPUT = new Locator("ROUTE_OPTION_INPUT", new By.ByXPath("//input[@name='route']"));
    private static final Locator DOCUMENT_UPLOAD_POPUP = new Locator("DOCUMENT_UPLOAD_POPUP", By.xpath("//h2[contains(text(),'upload these documents') or contains(text(),'upload dokumen')]"));
    private static final Locator INSURED_DETAILS_PAGE_TITLE = new Locator("INSURED_DETAILS_PAGE_TITLE", By.xpath("//h2[text()='Enter Insured Details']"));
    private static final Locator INPUT_FULL_NAME = new Locator("INPUT_FULL_NAME", By.xpath("//input[@id='full-name' or @data-testid='field-full-name']"));
    private static final Locator INPUT_MOBILE_PHONE = new Locator("INPUT_MOBILE_PHONE", By.xpath("//input[@id='mobile-phone' or @data-testid='field-mobile-phone']"));
    private static final Locator INPUT_EMAIL = new Locator("INPUT_EMAIL", By.xpath("//input[@id='email' or @data-testid='field-email']"));
    private static final Locator ADD_BANK_ACCOUNT_BTN = new Locator("ADD_BANK_ACCOUNT_BTN", By.xpath("//button[contains(normalize-space(.),'Select bank account') or contains(normalize-space(.),'Pilih rekening bank') or contains(normalize-space(.),'Add bank account') or contains(normalize-space(.),'Tambah rekening bank')]"));
    private static final Locator BANK_ACCOUNT_POPUP_TITLE = new Locator("BANK_ACCOUNT_POPUP_TITLE", By.xpath("//*[normalize-space(.)='Select a bank account' or normalize-space(.)='Pilih rekening bank' or normalize-space(.)='Add a bank account' or normalize-space(.)='Tambah rekening bank']"));
    private static final Locator BANK_ACCOUNT_SELECT_BTN = new Locator("BANK_ACCOUNT_SELECT_BTN", By.xpath("//button[normalize-space(.)='Select' or normalize-space(.)='Pilih']"));
    private static final Locator CHANGE_DISBURSEMENT_METHOD = new Locator("CHANGE_DISBURSEMENT_METHOD", By.xpath("//*[normalize-space(.)='Change method' or normalize-space(.)='Ganti metode']"));
    private static final Locator INPUT_BANK_ACCOUNT_OWNER = new Locator("INPUT_BANK_ACCOUNT_OWNER", By.xpath("//input[@id='account-owner-name' or @data-testid='account-owner-name']"));
    private static final Locator CLAIM_SUCCESS_MESSAGE = new Locator("CLAIM_SUCCESS_MESSAGE", By.xpath("//h2[contains(text(),'successfully') or contains(text(),'berhasil')]"));
    private static final Locator UPLOAD_E_TICKET_INPUT = new Locator("UPLOAD_E_TICKET_INPUT", By.xpath("(//input[@type='file'])[1]"));
    private static final Locator UPLOAD_BOARDING_PASS_INPUT = new Locator("UPLOAD_BOARDING_PASS_INPUT", By.xpath("(//input[@type='file'])[2]"));
    private static final Locator UPLOAD_ID_PASSPORT_INPUT = new Locator("UPLOAD_ID_PASSPORT_INPUT", By.xpath("(//input[@type='file'])[3]"));
    private static final Locator UPLOAD_TICKET_RECEIPT_INPUT = new Locator("UPLOAD_TICKET_RECEIPT_INPUT", By.xpath("(//input[@type='file'])[4]"));
    private static final Locator UPLOAD_DELAY_PROOF_INPUT = new Locator("UPLOAD_DELAY_PROOF_INPUT", By.xpath("(//input[@type='file'])[5]"));
    private static final Locator UPLOAD_LEGAL_DOC_INPUT = new Locator("UPLOAD_LEGAL_DOC_INPUT", By.xpath("(//input[@type='file'])[6]"));
    private static final Locator CONTINUE_AFTER_UPLOAD_BTN = new Locator("CONTINUE_AFTER_UPLOAD_BTN", By.xpath("//button[normalize-space(.)='Continue' or normalize-space(.)='Lanjutkan']"));
    private static final Locator CHEVRON_BUTTON = new Locator("CHEVRON_BUTTON", By.xpath("//*[@data-testid='protection-card:details']"));
    private static final Locator BENEFITS_DETAIL = new Locator("BENEFITS_DETAIL", By.xpath("(//button[contains(@class,'Tabs_tab')])[1]"));
    private static final Locator HOW_TO_CLAIM = new Locator("HOW_TO_CLAIM", By.xpath("(//button[contains(@class,'Tabs_tab')])[2]"));
    private static final Locator ABOUT_INSURERS = new Locator("ABOUT_INSURERS", By.xpath("(//button[contains(@class,'Tabs_tab')])[3]"));
    private static final Locator BENEFITS_DETAIL_CONTAINER = new Locator("BENEFITS_DETAIL_CONTAINER", By.xpath("//div[contains(@class,'BenefitDetails_wrapper')]"));
    private static final Locator HOW_TO_CLAIM_CONTAINER = new Locator("HOW_TO_CLAIM_CONTAINER", By.xpath("//div[contains(@class,'HowToClaim_wrapper')]"));
    private static final Locator ABOUT_INSURERS_CONTAINER = new Locator("ABOUT_INSURERS_CONTAINER", By.xpath("//div[contains(@class,'AboutTheInsurers_wrapper')]"));
    private static final Locator CLAIM_HISTORY_TAB=new Locator("CLAIM_HISTORY_ICON_DES",new By.ByXPath("//button//span[contains(@class,'Text_text__') and (contains(normalize-space(),'History') or contains(normalize-space(),'Riwayat'))]"));
    public abstract void removeWelcomePopup();

    protected abstract Locator CheckClaimCenterPageTitle();

    public boolean isOnInsuranceClaimLandingPage(boolean isLogin){
        String url = driverTools.getCurrentURL();
        boolean isOnCorrectURL = url.toLowerCase().contains("insurance-claim");
        Assertion.assertThat("Url should contain insurance-claim", url.toLowerCase(), is(containsString("insurance-claim")));
        boolean isClaimCenterTabShown = driverTools.waitForElementToPresence(CLAIM_PAGE_ORDER_TAB).isPresent();
        Assertion.assertThat("Landing page contain claim list tab", isClaimCenterTabShown, is(true));
        boolean isLoginButtonShown = driverTools.waitForElementToPresence(INSURANCE_CLAIM_LOGIN_BUTTON).isPresent();
        if (!isLogin) {
            Assertion.assertThat("Claim Page should contain Login Button",
                    isLoginButtonShown, is(true));
            return isOnCorrectURL && isClaimCenterTabShown && isLoginButtonShown;
        } else {
            Assertion.assertThat("Claim Page should NOT contain Login Button",
                    isLoginButtonShown, is(false));
            return isOnCorrectURL && isClaimCenterTabShown && !isLoginButtonShown;
        }
    }

    public void clickOrderDetailsInsurance() {
        driverTools.waitForElementToAppear(INSURANCE_ORDER_DETAILS_ICON);
        driverTools.waitForElementToBeClickable(INSURANCE_ORDER_DETAILS_ICON);
        driverTools.clickWithJavascriptExecutor(INSURANCE_ORDER_DETAILS_ICON);
    }

    public boolean isFAQDropdownAccessible() {
        boolean faqLinkPresent = driverTools.waitForElementToPresence(INSURANCE_ORDER_DETAILS_FAQ_LINK).isPresent();
        if (!faqLinkPresent) return false;
        driverTools.clickWithJavascriptExecutor(INSURANCE_ORDER_DETAILS_FAQ_LINK);

        boolean policyIconPresent = driverTools.waitForElementToPresence(INSURANCE_POLICY_COLLAPSE_ICON).isPresent();
        if (!policyIconPresent) return false;
        driverTools.clickWithJavascriptExecutor(INSURANCE_POLICY_COLLAPSE_ICON);

        boolean claimCenterIconPresent = driverTools.waitForElementToPresence(INSURANCE_CLAIM_CENTER_COLLAPSE_ICON).isPresent();
        if (!claimCenterIconPresent) return false;
        driverTools.clickWithJavascriptExecutor(INSURANCE_CLAIM_CENTER_COLLAPSE_ICON);

        boolean contactCenterIconPresent = driverTools.waitForElementToPresence(INSURANCE_CONTACT_CENTER_COLLAPSE_ICON).isPresent();
        if (!contactCenterIconPresent) return false;
        driverTools.clickWithJavascriptExecutor(INSURANCE_CONTACT_CENTER_COLLAPSE_ICON);

        return true;
    }

    public void removeClaimListPopup(){
        driverTools.waitForElementToAppear(CLAIM_LIST_POPUP).ifPresent(driverTools::click);
    }

    public void clicksClaimHistory(){
        driverTools.waitForElementToPresence(CLAIM_HISTORY_TAB);
        driverTools.clickWithJavascriptExecutor(CLAIM_HISTORY_TAB);
    }

    public void clicksDetails(){
        driverTools.waitForElementToPresence(EXPIRED_LIST_DETAIL_BUTTON);
        driverTools.clickWithJavascriptExecutor(EXPIRED_LIST_DETAIL_BUTTON);
    }

    public void faqDetails(){
        driverTools.waitForElementToPresence(FAQ_BUTTON);
        driverTools.clickWithJavascriptExecutor(FAQ_BUTTON);
    }

    public boolean claimAndExpiredInsuranceAppears(){
        return  driverTools.isElementDisplayed(CLAIM_LIST_DES) && driverTools.isElementDisplayed(EXPIRED_INSURANCE_DES);
    }

    public boolean claimAndExpiredInsuranceDetailsAppears(){
        driverTools.waitForElementToPresence(EXPIRED_INSURANCE_DETAILS);
        return driverTools.isElementDisplayed(EXPIRED_INSURANCE_DETAILS);
    }

    public boolean claimAndExpiredInsuranceOrdersAppears(){
        return driverTools.isElementDisplayed(CLAIM_HISTORY_ORDER);
    }

    public boolean historyInsuranceDetailsAppears(){
        driverTools.waitForElementToPresence(CLAIM_HISTORY_DETAILS);
        driverTools.clickWithJavascriptExecutor(CLAIM_HISTORY_DETAILS);
        driverTools.waitForElementToPresence(CLAIM_DETAIL_HEADER);
        String url = driverTools.getCurrentURL();
        Assertion.assertThat("Url should contain /insurance-claim/detail/", url.toLowerCase(), is(containsString("/insurance-claim/detail/")));
        boolean isOnCorrectURL = url.toLowerCase().contains("insurance-claim");
        return isOnCorrectURL;
    }

    public boolean insurancePolicyClaimAndContactCermatiAppears(){

        driverTools.waitForElementToPresence(INSURANCE_POLICY_OPTION);
        driverTools.waitForElementToPresence(HOW_TO_CLAIM_OPTION);
        driverTools.waitForElementToPresence(CONTACT_CERMATI_PROTECT_OPTION);

        boolean insurancePolicy = driverTools.waitForElementToPresence(INSURANCE_POLICY_OPTION).isPresent();
        Assertion.assertThat("Insurance Policy Option appears.", insurancePolicy, is(true));

        boolean claimOption = driverTools.waitForElementToPresence(HOW_TO_CLAIM_OPTION).isPresent();
        Assertion.assertThat("How to claim option appears.", claimOption, is(true));

        boolean cermatiOption = driverTools.waitForElementToPresence(CONTACT_CERMATI_PROTECT_OPTION).isPresent();
        Assertion.assertThat("Contact cermati protect option appears.", cermatiOption, is(true));

        return insurancePolicy && claimOption && cermatiOption;
    }

    public boolean insuranceClaimLandingPageAppears(){
        driverTools.waitForElementToPresence(CLAIM_PAGE_ORDER_TAB);
        return driverTools.isElementDisplayed(CLAIM_PAGE_ORDER_TAB);
    }

    public void clickInsuranceCardClaim(){
        driverTools.waitForElementToPresence(INSURANCE_CARD_CLAIM);
        driverTools.clickWithJavascriptExecutor(INSURANCE_CARD_CLAIM);
    }

    public boolean isInsuranceCardClaimDisplayed(){
        return driverTools.waitForElementToAppear(INSURANCE_CARD_CLAIM).isPresent();
    }

    public void closeWelcomePopUp(){
        if (driverTools.waitForElementToAppear(WELCOME_POPUP, 10).isPresent()) {
            driverTools.clickWithJavascriptExecutor(WELCOME_POPUP_CLOSE_BUTTON);
        }
    }

    public boolean isClaimIdInDetailsPresent() {
        driverTools.waitForElementToPresence(CLAIM_ID_IN_DETAILS);
        return driverTools.isElementPresent(CLAIM_ID_IN_DETAILS);
    }

    public boolean isFlightRouteDisplayed(){
        driverTools.waitForElementToPresence(FLIGHT_ROUTE_IN_DETAILS);
        return driverTools.isElementPresent(FLIGHT_ROUTE_IN_DETAILS);
    }

    public boolean isClaimStatusSectionDisplayed(){
        driverTools.waitForElementToPresence(ACTIVE_CLAIM_IN_DETAILS);
        return driverTools.isElementDisplayed(ACTIVE_CLAIM_IN_DETAILS);
    }

    public void clickOnAddProtectionButton(){
        driverTools.waitForElementToAppear(ADD_PROTECTION_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADD_PROTECTION_BUTTON);
    }

    private static Locator addProtectionButtonAt(int position) {
        return new Locator("ADD_PROTECTION_BUTTON[" + position + "]",
                new By.ByXPath("(" + ADD_PROTECTION_BUTTON_XPATH + ")[" + position + "]"));
    }

    private static final Locator SEE_ALL_BOOKINGS = new Locator("SEE_ALL_BOOKINGS", new By.ByXPath(
            "//*[text()[contains(.,'See All')] or text()[contains(.,'Lihat Semua')]]"));

    public void showAllBookings() {
        if (driverTools.waitForElementToAppear(SEE_ALL_BOOKINGS, 5).isEmpty()) {
            return;
        }
        logger.log("The insurance page previews only the first few bookings, so the full list is opened "
                + "before the orders are counted.");
        driverTools.clickWithJavascriptExecutor(SEE_ALL_BOOKINGS);
        driverTools.waitForWebToLoad();
    }

    public int countOrdersOfferingProtection() {
        if (driverTools.waitForElementToAppear(ADD_PROTECTION_BUTTON, 10).isEmpty()) {
            return 0;
        }
        return driverTools.elements(ADD_PROTECTION_BUTTON).map(List::size).orElse(0);
    }

    public boolean clickOnAddProtectionButton(int position) {
        Locator button = addProtectionButtonAt(position);
        if (driverTools.waitForElementToAppear(button).isEmpty()) {
            logger.log("Flight order " + position + " no longer offers protection.");
            return false;
        }
        driverTools.clickWithJavascriptExecutor(button);
        return true;
    }

    public boolean isOrderChangeConfirmationRaised() {
        return driverTools.waitForElementToAppear(ORDER_CHANGED_MODAL_CONTINUE_BTN, 5).isPresent();
    }

    public boolean verifyInsuranceAvailableToBuy(){
        return driverTools.waitForElementToAppear(PROTECTION_CARD).isPresent();
    }

    public void clickOnAddButton(){
        driverTools.waitForElementToAppear(PROTECTION_CARD_BUTTON);
        driverTools.clickWithJavascriptExecutor(PROTECTION_CARD_BUTTON);
    }

    public void clickOnSelectButton(){
        driverTools.waitForElementToAppear(SELECT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_BUTTON);
    }

    public void clickContinueToPayButton(){
        driverTools.waitForElementToAppear(CONTINUE_TO_PAY_BTN);
        driverTools.waitForElementToBeClickable(CONTINUE_TO_PAY_BTN);
        driverTools.click(CONTINUE_TO_PAY_BTN);
    }

    public boolean checkHeading() {
        driverTools.waitForElementToAppear(SELECT_PASSENGER_SECTION);
        return driverTools.isElementDisplayed(SELECT_PASSENGER_SECTION);
    }


    public void verifyOneWayRoute(String orderId) {
        driverTools.waitForElementToAppear(TRIP_DETAILS);

        int routeOptions = driverTools.elements(ROUTE_OPTION_INPUT).map(List::size).orElse(0);
        Assertion.assertThat("Claim form offers exactly one route, as a one-way order has. A round trip "
                + "offers one per leg", routeOptions, is(1));

        String routeText = driverTools.getText(TRIP_DETAILS).orElse("");
        List<String> stations = Arrays.stream(routeText.split("[\\s-]+"))
                .filter(station -> !station.isBlank())
                .toList();
        Assertion.assertThat("Claim form names an origin and a destination, read as '" + routeText + "'",
                stations.size() >= 2, is(true));

        if (orderId != null && !orderId.isBlank()) {
            Assertion.assertThat("Claim form was opened for flight order " + orderId,
                    driverTools.waitForElementToPresence(orderIdOnClaimForm(orderId), 5).isPresent(), is(true));
        }
    }

    private static Locator orderIdOnClaimForm(String orderId) {
        return new Locator("ORDER_ID_ON_CLAIM_FORM[" + orderId + "]", new By.ByXPath(
                "//*[normalize-space(.)='Order Info' or normalize-space(.)='Info Pesanan']"
                        + "/following::*[text()[contains(.,'" + orderId + "')]][1]"));
    }

    public void clickClaimDetailButton() {
        driverTools.waitForElementToAppear(CLAIM_LIST_DETAIL_BUTTON);
        driverTools.clickWithJavascriptExecutor(CLAIM_LIST_DETAIL_BUTTON);
    }

    public void checkDetails(){
        driverTools.waitForElementToAppear(COVERAGE_AND_POLICY);
        Assertion.assertThat("Insurance Policy Option appears.", driverTools.isElementDisplayed(COVERAGE_AND_POLICY), is(true));

        driverTools.waitForElementToAppear(CONTACT_CERMATI_PROTECT_OPTION);
        Assertion.assertThat("Contact cermati protect option appears.", driverTools.isElementDisplayed(CONTACT_CERMATI_PROTECT_OPTION), is(true));
    }

    public boolean clickOnAddOrNewClaimButton() {
        return clickClaimButtonWhenEnabled(ADD_OR_NEW_CLAIM_BTN);
    }

    private static Locator claimButtonOfOrder(String orderId) {
        return new Locator("ADD_OR_NEW_CLAIM_BTN[order " + orderId + "]", new By.ByXPath(
                "//*[text()[contains(.,'" + orderId + "')]]/following::button[" + CLAIM_BUTTON_LABELS + "][1]"));
    }


    public boolean clickOnAddOrNewClaimButton(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            return clickOnAddOrNewClaimButton();
        }
        logger.log("Claiming against flight order " + orderId + " rather than whichever is listed first.");
        return clickClaimButtonWhenEnabled(claimButtonOfOrder(orderId));
    }

    protected boolean clickClaimButtonWhenEnabled(Locator claimButton) {
        if (driverTools.waitForElementToAppear(claimButton).isEmpty()) {
            logger.log("No claim button for this policy, so its claim form cannot be opened.");
            return false;
        }
        if (!driverTools.isElementEnabled(claimButton) || !driverTools.isElementClickable(claimButton, 10)) {
            logger.log("This policy's claim button is present but still disabled, which the claim center "
                    + "shows while the e-ticket is being issued.");
            return false;
        }
        driverTools.clickWithJavascriptExecutor(claimButton);
        return true;
    }

    private static String headingXPath(String heading, String headingIndonesian) {
        return "//*[normalize-space(.)='" + heading + "' or normalize-space(.)='" + headingIndonesian + "']";
    }

    private static Locator firstRadioUnder(String heading, String headingIndonesian) {
        return new Locator("RADIO under '" + heading + "'", new By.ByXPath(
                headingXPath(heading, headingIndonesian)
                        + "/following::span[contains(@class,'Radio_icon')][1]"));
    }

    private static final Locator ROUTE_RADIO = firstRadioUnder("Select a route", "Pilih rute");
    private static final Locator PASSENGER_RADIO = firstRadioUnder("Select passenger", "Pilih penumpang");
    private static final Locator REASON_RADIO = firstRadioUnder("Select a reason for claim", "Pilih alasan klaim");

    private static final Locator FINISH_CLAIM_DRAFT_BTN = new Locator("FINISH_CLAIM_DRAFT_BTN", new By.ByXPath(
            "//button[contains(normalize-space(.),'Finish claim draft') or contains(normalize-space(.),'Selesaikan draf klaim')"
                    + " or contains(normalize-space(.),'Lanjutkan draf klaim')]"));

    public void finishClaimDraftIfAsked() {
        if (driverTools.waitForElementToAppear(FINISH_CLAIM_DRAFT_BTN, 5).isPresent()) {
            logger.log("A claim draft from an earlier run is still open, so it is carried on with rather "
                    + "than starting the new claim the claim center would refuse.");
            driverTools.clickWithJavascriptExecutor(FINISH_CLAIM_DRAFT_BTN);
        }
    }

    public void selectReasonForClaim(){
        driverTools.waitForElementToAppear(REASON_RADIO);
        driverTools.clickWithJavascriptExecutor(REASON_RADIO);
        logger.log("Selected the reason to claim");
    }

    private static final String REASON_HEADING = headingXPath("Select a reason for claim", "Pilih alasan klaim");
    private static final Locator REASON_RADIOS = new Locator("REASON_RADIOS",
            new By.ByXPath(REASON_HEADING + "/following::span[contains(@class,'Radio_icon')]"));
    private static final Locator SIMILAR_CLAIM_ACK_BTN = new Locator("SIMILAR_CLAIM_ACK_BTN", new By.ByXPath(
            "//button[normalize-space(.)='I understand' or normalize-space(.)='Saya mengerti']"));

    private static Locator reasonRadioAt(int position) {
        return new Locator("REASON_RADIO[" + position + "]",
                new By.ByXPath(REASON_HEADING + "/following::span[contains(@class,'Radio_icon')][" + position + "]"));
    }

    private boolean acknowledgeSimilarClaimIfRaised() {
        if (driverTools.waitForElementToAppear(SIMILAR_CLAIM_ACK_BTN, 5).isEmpty()) {
            return false;
        }
        driverTools.clickWithJavascriptExecutor(SIMILAR_CLAIM_ACK_BTN);
        return true;
    }

    public boolean selectClaimReasonAndContinue() {
        if (driverTools.waitForElementToAppear(reasonRadioAt(1), 10).isEmpty()) {
            logger.log("The claim form offered no reason to claim for.");
            return false;
        }
        int reasons = driverTools.elements(REASON_RADIOS).map(List::size).orElse(0);

        for (int reason = 1; reason <= reasons; reason++) {
            Locator radio = reasonRadioAt(reason);
            if (driverTools.waitForElementToAppear(radio).isEmpty()) {
                logger.log("Reason " + reason + " of " + reasons + " is no longer on screen, so the claim "
                        + "form has moved on from the reason list and no further reason can be tried.");
                return false;
            }
            driverTools.clickWithJavascriptExecutor(radio);
            clickContinueButton();

            if (!acknowledgeSimilarClaimIfRaised()) {
                logger.log("Claiming on reason " + reason + " of " + reasons + ".");
                return true;
            }
            logger.log("Reason " + reason + " of " + reasons + " already has a claim being processed, so "
                    + "the next reason is tried.");
        }
        logger.log("Every one of this policy's " + reasons + " reasons already has a claim being "
                + "processed, so this policy has none left to claim.");
        return false;
    }

    public int countClaimableInsurances() {
        if (driverTools.waitForElementToAppear(ADD_OR_NEW_CLAIM_BTN, 10).isEmpty()) {
            return 0;
        }
        return driverTools.elements(ADD_OR_NEW_CLAIM_BTN).map(List::size).orElse(0);
    }

    public boolean clickClaimButtonAt(int position) {
        return clickClaimButtonWhenEnabled(new Locator("ADD_OR_NEW_CLAIM_BTN[" + position + "]",
                new By.ByXPath("(//button[" + CLAIM_BUTTON_LABELS + "])[" + position + "]")));
    }

    public void agreeToTermsAndProceed(){
        if (driverTools.waitForElementToPresence(TNC_AGREEMENT, 5).isEmpty()) {
            logger.log("No terms are being asked for, which is what a resumed draft looks like: they were "
                    + "agreed when the draft was started.");
            return;
        }
        driverTools.clickWithJavascriptExecutor(TNC_AGREEMENT);
        driverTools.waitForElementToPresence(CONTINUE_AFTER_TNC_AGREEMENT, 10);
        driverTools.clickWithJavascriptExecutor(CONTINUE_AFTER_TNC_AGREEMENT);
    }

    public void fillClaimFormAndProceed(String address, String accountNumber, String bankName){
        driverTools.waitForElementToAppear(INPUT_ADDRESS);
        driverTools.waitForElementToBeClickable(INPUT_ADDRESS);
        driverTools.clickWithJavascriptExecutor(INPUT_ADDRESS);
        driverTools.sendKeys(INPUT_ADDRESS,address);
        driverTools.waitForElementToAppear(INPUT_ACCOUNT_NUMBER);
        driverTools.waitForElementToBeClickable(INPUT_ACCOUNT_NUMBER);
        driverTools.clickWithJavascriptExecutor(INPUT_ACCOUNT_NUMBER);
        driverTools.sendKeys(INPUT_ACCOUNT_NUMBER,accountNumber);
        driverTools.waitForElementToAppear(ADD_BANK_NAME);
        driverTools.waitForElementToBeClickable(ADD_BANK_NAME);
        driverTools.clickWithJavascriptExecutor(ADD_BANK_NAME);
        driverTools.waitForElementToAppear(INPUT_BANK_NAME);
        driverTools.clickWithJavascriptExecutor(INPUT_BANK_NAME);
        driverTools.sendKeys(INPUT_BANK_NAME,bankName);
        driverTools.waitForElementToAppear(FIRST_BANK);
        driverTools.clickWithJavascriptExecutor(FIRST_BANK);
    }

    @SneakyThrows
    public void clickCheckAndContinue(){
        driverTools.waitForElementToPresence(CHECK_BTN);
        driverTools.waitForElementToBeClickable(CHECK_BTN, 20);
        driverTools.waitForWebToLoad();
        driverTools.clickWithJavascriptExecutor(CHECK_BTN);
        driverTools.waitForElementToAppear(TNC_AGREEMENT,20);
        driverTools.waitForElementToPresence(CONTINUE_BTN);
        driverTools.waitForElementToBeClickable(CONTINUE_BTN, 20);
        driverTools.waitForWebToLoad();
        driverTools.clickWithJavascriptExecutor(CONTINUE_BTN);
    }

    public void fillIncidentDetails(String incidentLocation, String incidentChronology){
        confirmDatePicker();

        driverTools.waitForElementToAppear(INCIDENT_LOCATION);
        driverTools.clickWithJavascriptExecutor(INCIDENT_LOCATION);
        driverTools.sendKeys(INCIDENT_LOCATION, incidentLocation);

        driverTools.waitForElementToAppear(INCIDENT_CHRONOLOGY);
        driverTools.clickWithJavascriptExecutor(INCIDENT_CHRONOLOGY);
        driverTools.sendKeys(INCIDENT_CHRONOLOGY, incidentChronology);
    }

    public boolean canProceedToNextStepAfterFillingIncidentDetails() {
        driverTools.waitForElementToAppear(CONTINUE_BTN);
        return driverTools.isElementEnabled(CONTINUE_BTN);
    }

    public boolean isClaimFormPageLoaded() {
        driverTools.waitForElementToAppear(CLAIM_FORM_PAGE_TITLE, 10);
        return driverTools.isElementDisplayed(CLAIM_FORM_PAGE_TITLE);
    }

    public void selectRouteToClaim() {
        driverTools.waitForElementToAppear(SELECT_ROUTE_SECTION);
        if (driverTools.waitForElementToPresence(ROUTE_OPTION_INPUT, 5).isEmpty()) {
            logger.log("The claim form offers no route to choose, so the route step is left as it stands "
                    + "rather than reaching past it into the next section.");
            return;
        }
        driverTools.waitForElementToAppear(ROUTE_RADIO);
        driverTools.clickWithJavascriptExecutor(ROUTE_RADIO);
        logger.log("Selected route to claim");
    }

    public void selectPassengerToClaimForRoute() {
        driverTools.waitForElementToAppear(SELECT_PASSENGER_SECTION);
        driverTools.waitForElementToAppear(PASSENGER_RADIO);
        driverTools.clickWithJavascriptExecutor(PASSENGER_RADIO);
        logger.log("Selected passenger to claim");
    }

    public void clickContinueButton() {
        driverTools.waitForElementToAppear(CONTINUE_BUTTON);
        driverTools.waitForElementToBeClickable(CONTINUE_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
    }

    public boolean isDocumentUploadPopupDisplayed() {
        driverTools.waitForElementToAppear(DOCUMENT_UPLOAD_POPUP, 10);
        return driverTools.isElementDisplayed(DOCUMENT_UPLOAD_POPUP);
    }

    public void agreeToTermsInUploadPopup() {
        driverTools.waitForElementToAppear(TNC_AGREEMENT);
        driverTools.clickWithJavascriptExecutor(TNC_AGREEMENT);
    }

    public void clickContinueToClaimForm() {
        driverTools.waitForElementToAppear(CONTINUE_AFTER_TNC_AGREEMENT);
        driverTools.waitForElementToBeClickable(CONTINUE_AFTER_TNC_AGREEMENT);
        driverTools.clickWithJavascriptExecutor(CONTINUE_AFTER_TNC_AGREEMENT);
    }

    public boolean isInsuredDetailsPageLoaded() {
        driverTools.waitForElementToAppear(INSURED_DETAILS_PAGE_TITLE, 10);
        return driverTools.isElementDisplayed(INSURED_DETAILS_PAGE_TITLE);
    }

    public void inputFullName(String fullName) {
        driverTools.waitForElementToAppear(INPUT_FULL_NAME);
        driverTools.waitForElementToBeClickable(INPUT_FULL_NAME);
        driverTools.clickWithJavascriptExecutor(INPUT_FULL_NAME);
        driverTools.sendKeys(INPUT_FULL_NAME, fullName);
    }

    public void inputIdNumber(String idNumber) {
        driverTools.waitForElementToAppear(INPUT_ID_NUMBER);
        driverTools.waitForElementToBeClickable(INPUT_ID_NUMBER);
        driverTools.clickWithJavascriptExecutor(INPUT_ID_NUMBER);
        driverTools.sendKeys(INPUT_ID_NUMBER, idNumber);
    }

    public void inputAddress(String address) {
        driverTools.waitForElementToAppear(INPUT_ADDRESS);
        driverTools.waitForElementToBeClickable(INPUT_ADDRESS);
        driverTools.clickWithJavascriptExecutor(INPUT_ADDRESS);
        driverTools.sendKeys(INPUT_ADDRESS, address);
    }

    public void inputAddressIfAsked(String address) {
        if (driverTools.waitForElementToAppear(INPUT_ADDRESS, 5).isEmpty()) {
            logger.log("This claim reason's insured details asks for no address, so none is given.");
            return;
        }
        driverTools.clickWithJavascriptExecutor(INPUT_ADDRESS);
        driverTools.sendKeys(INPUT_ADDRESS, address);
    }

    public void selectBirthDate(String birthDate) {
        confirmDatePicker();
    }

    public void confirmDatePicker() {
        driverTools.waitForElementToAppear(INCIDENT_DATE);
        driverTools.clickWithJavascriptExecutor(INCIDENT_DATE);
        driverTools.waitForElementToAppear(SAVE_BTN);
        driverTools.clickWithJavascriptExecutor(SAVE_BTN);
    }

    public void confirmDatePickerIfAsked() {
        if (driverTools.waitForElementToAppear(DATE_PICKER_ICON, 5).isEmpty()) {
            logger.log("This step carries no date field, so none is set.");
            return;
        }
        driverTools.clickWithJavascriptExecutor(DATE_PICKER_ICON);
        driverTools.waitForElementToAppear(SAVE_BTN);
        driverTools.clickWithJavascriptExecutor(SAVE_BTN);
    }

    public void inputMobilePhone(String mobilePhone) {
        driverTools.waitForElementToAppear(INPUT_MOBILE_PHONE);
        driverTools.waitForElementToBeClickable(INPUT_MOBILE_PHONE);
        driverTools.clickWithJavascriptExecutor(INPUT_MOBILE_PHONE);
        driverTools.sendKeys(INPUT_MOBILE_PHONE, mobilePhone);
    }

    public void inputEmail(String email) {
        driverTools.waitForElementToAppear(INPUT_EMAIL);
        driverTools.waitForElementToBeClickable(INPUT_EMAIL);
        driverTools.clickWithJavascriptExecutor(INPUT_EMAIL);
        driverTools.sendKeys(INPUT_EMAIL, email);
    }

    public void clickAddBankAccount() {
        driverTools.waitForElementToAppear(ADD_BANK_ACCOUNT_BTN);
        driverTools.clickWithJavascriptExecutor(ADD_BANK_ACCOUNT_BTN);
    }

    public boolean isBankAccountPopupDisplayed() {
        driverTools.waitForElementToAppear(BANK_ACCOUNT_POPUP_TITLE, 10);
        return driverTools.isElementDisplayed(BANK_ACCOUNT_POPUP_TITLE);
    }

    public void selectBankName(String bankName) {
        driverTools.waitForElementToAppear(ADD_BANK_NAME);
        driverTools.waitForElementToBeClickable(ADD_BANK_NAME);
        driverTools.clickWithJavascriptExecutor(ADD_BANK_NAME);
        driverTools.waitForElementToAppear(INPUT_BANK_NAME);
        driverTools.clickWithJavascriptExecutor(INPUT_BANK_NAME);
        driverTools.sendKeys(INPUT_BANK_NAME, bankName);
        driverTools.waitForElementToAppear(FIRST_BANK);
        driverTools.clickWithJavascriptExecutor(FIRST_BANK);
    }

    public void inputBankAccountNumber(String accountNumber) {
        driverTools.waitForElementToAppear(INPUT_ACCOUNT_NUMBER);
        driverTools.waitForElementToBeClickable(INPUT_ACCOUNT_NUMBER);
        driverTools.clickWithJavascriptExecutor(INPUT_ACCOUNT_NUMBER);
        driverTools.sendKeys(INPUT_ACCOUNT_NUMBER, accountNumber);
    }

    public void inputBankAccountOwnerName(String ownerName) {
        driverTools.waitForElementToAppear(INPUT_BANK_ACCOUNT_OWNER);
        driverTools.waitForElementToBeClickable(INPUT_BANK_ACCOUNT_OWNER);
        driverTools.clickWithJavascriptExecutor(INPUT_BANK_ACCOUNT_OWNER);
        driverTools.sendKeys(INPUT_BANK_ACCOUNT_OWNER, ownerName);
    }

    public void clickSaveBankAccount() {
        driverTools.waitForElementToAppear(SAVE_BTN);
        driverTools.waitForElementToBeClickable(SAVE_BTN);
        driverTools.clickWithJavascriptExecutor(SAVE_BTN);
    }

    public boolean isBankAccountSaved() {
        driverTools.waitForElementToDisappear(BANK_ACCOUNT_POPUP_TITLE, 5);
        return !driverTools.isElementDisplayed(BANK_ACCOUNT_POPUP_TITLE);
    }

    public void clickContinueOnInsuredDetails() {
        driverTools.waitForElementToAppear(CONTINUE_BUTTON);
        driverTools.waitForElementToBeClickable(CONTINUE_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
    }

    public void selectIncidentDate() {
        driverTools.waitForElementToAppear(INCIDENT_DATE);
        driverTools.clickWithJavascriptExecutor(INCIDENT_DATE);
        driverTools.waitForElementToAppear(SAVE_BTN);
        driverTools.clickWithJavascriptExecutor(SAVE_BTN);
    }

    public void inputIncidentLocation(String location) {
        driverTools.waitForElementToAppear(INCIDENT_LOCATION);
        driverTools.clickWithJavascriptExecutor(INCIDENT_LOCATION);
        driverTools.sendKeys(INCIDENT_LOCATION, location);
    }

    public void inputIncidentChronology(String chronology) {
        driverTools.waitForElementToAppear(INCIDENT_CHRONOLOGY);
        driverTools.clickWithJavascriptExecutor(INCIDENT_CHRONOLOGY);
        driverTools.sendKeys(INCIDENT_CHRONOLOGY, chronology);
    }

    public boolean isSubmitClaimEnabled() {
        driverTools.waitForElementToAppear(CONTINUE_BTN);
        return driverTools.isElementEnabled(CONTINUE_BTN);
    }

    public void clickContinueAfterIncidentDetails() {
        driverTools.waitForElementToAppear(CONTINUE_BTN);
        driverTools.waitForElementToBeClickable(CONTINUE_BTN);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BTN);
    }

    public boolean isClaimSubmittedSuccessfully() {
        driverTools.waitForElementToAppear(CLAIM_SUCCESS_MESSAGE, 15);
        return driverTools.isElementDisplayed(CLAIM_SUCCESS_MESSAGE);
    }

    public void fillInsuredDetails(String fullName, String idNumber, String address, String birthDate, String mobilePhone, String email) {
        inputFullName(fullName);
        inputIdNumber(idNumber);
        inputAddress(address);
        selectBirthDate(birthDate);
        inputMobilePhone(mobilePhone);
        inputEmail(email);
    }

    private static final Locator BENEFICIARY_MISMATCH_OK_BTN = new Locator("BENEFICIARY_MISMATCH_OK_BTN",
            new By.ByXPath("//button[normalize-space(.)='OK' or normalize-space(.)='Oke']"));

    public void confirmBeneficiaryNameMismatchIfAsked() {
        if (driverTools.waitForElementToAppear(BENEFICIARY_MISMATCH_OK_BTN, 5).isPresent()) {
            logger.log("The bank account is not in the insured's name, which the claim form warns about and "
                    + "covers by asking for supporting documents later. Acknowledged so the form carries on.");
            driverTools.clickWithJavascriptExecutor(BENEFICIARY_MISMATCH_OK_BTN);
        }
    }

    private static Locator bankAccountRadioFor(String accountNumber) {
        return new Locator("BANK_ACCOUNT_RADIO[" + accountNumber + "]", new By.ByXPath(
                "//*[text()[contains(.,'" + accountNumber + "')]]/following::span[contains(@class,'Radio_icon')][1]"));
    }

    public void chooseBankAccountIfNotSet(String accountNumber) {
        if (driverTools.waitForElementToAppear(ADD_BANK_ACCOUNT_BTN, 5).isEmpty()) {
            logger.log("The claim already names a bank account, which is what a resumed draft carries, "
                    + "so the chooser is left alone.");
            return;
        }
        driverTools.clickWithJavascriptExecutor(ADD_BANK_ACCOUNT_BTN);
        selectExistingBankAccount(accountNumber);
    }

    public boolean isBankAccountChosen() {
        return driverTools.waitForElementToAppear(CHANGE_DISBURSEMENT_METHOD, 10).isPresent();
    }

    public void selectExistingBankAccount(String accountNumber) {
        driverTools.waitForElementToAppear(BANK_ACCOUNT_POPUP_TITLE, 10);

        Locator wanted = firstRadioUnder("Select a bank account", "Pilih rekening bank");
        if (accountNumber != null && !accountNumber.isBlank()
                && driverTools.waitForElementToPresence(bankAccountRadioFor(accountNumber), 5).isPresent()) {
            wanted = bankAccountRadioFor(accountNumber);
            logger.log("Disbursing the claim to the saved account ending " + accountNumber + ".");
        } else {
            logger.log("Disbursing the claim to the first saved account, since the data named none that "
                    + "the profile still holds.");
        }

        driverTools.clickWithJavascriptExecutor(wanted);
        driverTools.waitForElementToAppear(BANK_ACCOUNT_SELECT_BTN);
        driverTools.clickWithJavascriptExecutor(BANK_ACCOUNT_SELECT_BTN);
    }

    public void addBankAccountDetails(String bankName, String accountNumber, String accountOwnerName) {
        clickAddBankAccount();
        driverTools.waitForElementToAppear(BANK_ACCOUNT_POPUP_TITLE, 10);
        selectBankName(bankName);
        inputBankAccountNumber(accountNumber);
        inputBankAccountOwnerName(accountOwnerName);
        clickSaveBankAccount();
    }
    
    public void uploadETicket(String filePath) {
        driverTools.waitForElementToPresence(UPLOAD_E_TICKET_INPUT);
        driverTools.sendKeys(UPLOAD_E_TICKET_INPUT, filePath);
    }
    
    public void uploadBoardingPass(String filePath) {
        driverTools.waitForElementToPresence(UPLOAD_BOARDING_PASS_INPUT);
        driverTools.sendKeys(UPLOAD_BOARDING_PASS_INPUT, filePath);
    }
    
    public void uploadIdPassport(String filePath) {
        driverTools.waitForElementToPresence(UPLOAD_ID_PASSPORT_INPUT);
        driverTools.sendKeys(UPLOAD_ID_PASSPORT_INPUT, filePath);
    }
    
    public void uploadTicketReceipt(String filePath) {
        driverTools.waitForElementToPresence(UPLOAD_TICKET_RECEIPT_INPUT);
        driverTools.sendKeys(UPLOAD_TICKET_RECEIPT_INPUT, filePath);
    }
    
    public void uploadDelayProof(String filePath) {
        driverTools.waitForElementToPresence(UPLOAD_DELAY_PROOF_INPUT);
        driverTools.sendKeys(UPLOAD_DELAY_PROOF_INPUT, filePath);
    }
    
    public void uploadLegalDocument(String filePath) {
        driverTools.waitForElementToPresence(UPLOAD_LEGAL_DOC_INPUT);
        driverTools.sendKeys(UPLOAD_LEGAL_DOC_INPUT, filePath);
    }
    
    public void clickContinueAfterDocumentUpload() {
        driverTools.waitForElementToAppear(CONTINUE_AFTER_UPLOAD_BTN);
        driverTools.waitForElementToBeClickable(CONTINUE_AFTER_UPLOAD_BTN);
        driverTools.clickWithJavascriptExecutor(CONTINUE_AFTER_UPLOAD_BTN);
    }
    
    public void uploadMandatoryDocuments(String eTicketPath, String boardingPassPath, String idPassportPath) {
        uploadETicket(eTicketPath);
        uploadBoardingPass(boardingPassPath);
        uploadIdPassport(idPassportPath);
    }
    
    public void uploadOptionalDocuments(String ticketReceiptPath, String delayProofPath, String legalDocPath) {
        if (ticketReceiptPath != null && !ticketReceiptPath.isEmpty()) {
            uploadTicketReceipt(ticketReceiptPath);
        }
        if (delayProofPath != null && !delayProofPath.isEmpty()) {
            uploadDelayProof(delayProofPath);
        }
        if (legalDocPath != null && !legalDocPath.isEmpty()) {
            uploadLegalDocument(legalDocPath);
        }
    }
    public boolean isAddProtectionButtonDisplayed(){
        return driverTools.waitForElementToAppear(ADD_PROTECTION_BUTTON).isPresent()
                && driverTools.isElementClickable(ADD_PROTECTION_BUTTON, 10);
    }

    public void clickChevronButton() {
        driverTools.waitForElementToAppear(CHEVRON_BUTTON);
        driverTools.click(CHEVRON_BUTTON);
    }

    public void isPDPContentForInsuranceDisplayed() {
        driverTools.waitForElementToAppear(BENEFITS_DETAIL);
        driverTools.click(BENEFITS_DETAIL);

        Assertion.assertThat("Benefits Detail content is displayed", driverTools.isElementDisplayed(BENEFITS_DETAIL_CONTAINER), is(true));

        driverTools.waitForElementToAppear(HOW_TO_CLAIM);
        driverTools.click(HOW_TO_CLAIM);

        Assertion.assertThat("Benefits Detail content is displayed", driverTools.isElementDisplayed(HOW_TO_CLAIM_CONTAINER), is(true));

        driverTools.waitForElementToAppear(ABOUT_INSURERS);
        driverTools.click(ABOUT_INSURERS);

        Assertion.assertThat("Benefits Detail content is displayed", driverTools.isElementDisplayed(ABOUT_INSURERS_CONTAINER), is(true));
    }
}