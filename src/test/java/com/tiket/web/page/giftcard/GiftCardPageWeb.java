package com.tiket.web.page.giftcard;

import com.google.gson.Gson;
import com.tiket.api.giftCard.GiftCardCreateCartApi;
import com.tiket.api.giftCard.GiftCardCreateGCOrderApi;
import com.tiket.api.giftCard.GiftCardGetInventoryApi;
import com.tiket.api.giftCard.GiftCardGetOrderDetailsApi;
import com.tiket.api.giftCard.GiftCardIssuanceApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.appium.Swipe;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.giftCard.GiftCardCartResponse;
import com.tiket.pojo.giftCard.GiftCardInventoryResponse;
import com.tiket.pojo.giftCard.GiftCardIssuanceResponse;
import com.tiket.pojo.giftCard.GiftCardOrderDetailsResponse;
import com.tiket.pojo.giftCard.GiftCardOrderResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import java.time.Instant;
import java.util.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.CoreMatchers.is;

public abstract class GiftCardPageWeb<T extends WebDriver> extends WebPage<T> {

    public GiftCardPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> GiftCardPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new GiftCardPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new GiftCardPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator GIFT_CARD_HERO_IMAGE = new Locator("GIFT_CARD_HERO_IMAGE", new By.ByXPath("//div[contains(@class,'GiftHeroImage_sombrero_container')]"));
    private static final Locator SEND_GIFT_CARD_BUTTON = new Locator("SEND_GIFT_CARD_BUTTON", new By.ByXPath("//span[text()='Send gift card' or text()='Kirim gift card']//parent::button"));
    private static final Locator REDEEM_GIFT_CARD_BUTTON = new Locator("REDEEM_GIFT_CARD_BUTTON", new By.ByXPath("//span[text()='Redeem gift card' or text()='Tukar gift card']//parent::button"));
    private static final Locator BULK_ORDER_BUTTON = new Locator("BULK_ORDER_BUTTON", new By.ByXPath("//button[text()='Order in bulk' or text()='Pesan dalam jumlah banyak']"));
    private static final Locator LANDING_OCCASION_SECTION = new Locator("LANDING_OCCASION_SECTION", new By.ByXPath("//div[contains(@class,'OccasionSection_occasion_container')]"));
    private static final Locator STATIC_USP_LIST = new Locator("STATIC_USP_LIST", new By.ByXPath("//div[contains(@class,'StaticUspList_usp_list_container')]"));
    private static final Locator SEND_GIFT_CARD_HEADER = new Locator("SEND_GIFT_CARD_HEADER", new By.ByXPath("//div[contains(@class,'GiftCardHeader')]//h3[text()='Send Gift Card' or text()='Kirim Gift Card']"));
    private static final Locator CARD_ITEMS = new Locator("CARD_ITEMS", new By.ByXPath("//div[contains(@class,'GiftCardCarousel')]//div[contains(@class,'ChipsCarousel_slider')]"));
    private static final Locator FIGURE_CAPTION = new Locator("FIGURE_CAPTION", new By.ByXPath("//figcaption[contains(@class,'GiftCardFigure_caption')]"));
    private static final Locator BIRTHDAY_OCCASION_BUTTON = new Locator("BIRTHDAY_OCCASION_BUTTON", new By.ByXPath("//a[contains(@href,'occasion=birthday')]"));
    private static final Locator REDEEM_GIFT_CARD = new Locator("REDEEM_GIFT_CARD", new By.ByXPath("//button//span[text()='Redeem gift card']"));
    private static final Locator GIFT_CARD_INPUT_FIELD = new Locator("GIFT_CARD_INPUT_FIELD", new By.ByXPath("//input[@id='enter-the-redeem-code']"));
    private static final Locator REDEEM_CODE = new Locator("REDEEM_CODE", new By.ByXPath("//button//span[normalize-space(text())='Redeem']"));
    private static final Locator ERROR_POP_UP_MSG_TITLE_GIFT_CARD = new Locator("ERROR_POP_UP_MSG_TITLE_GIFT_CARD", new By.ByXPath("//h2[contains(@class, 'Figure_title')]"));
    private static final Locator ERROR_POP_UP_MSG_GIFT_CARD = new Locator("ERROR_POP_UP_MSG_GIFT_CARD", new By.ByXPath("//div[contains(@class, 'Figure_description__')]/div/p[1]"));
    private static final Locator SELECT_CARD_DESIGN_TEXT = new Locator("SELECT_CARD_DESIGN_TEXT", new By.ByXPath("//h3[normalize-space()='Select Card Design']"));
    private static final Locator OCCASION_BIRTHDAY_CHIP = new Locator("OCCASION_BIRTHDAY_CHIP", new By.ByXPath("//section[contains(@class,'ChipsCarousel')]//span[contains(@class,'Text_variant_activity')]"));
    private static final Locator ADD_RECIPIENT_BUTTON_IMG = new Locator("ADD_RECIPIENT_BUTTON_IMG", new By.ByXPath("//*[contains(@class,'Recipients_actions')]//*"));
    private static final Locator ADD_RECIPIENT_BUTTON = new Locator("ADD_RECIPIENT_BUTTON", new By.ByXPath("//div[contains(@class,'Recipients_actions')]"));
    private static final Locator RECIPIENT_DETAIL_TEXT = new Locator("RECIPIENT_DETAIL_TEXT", new By.ByXPath("//h2[normalize-space()='Recipient Details']"));
    private static final Locator RECIPIENT_NAME_FIELD = new Locator("RECIPIENT_NAME_FIELD", new By.ByXPath("//input[@id='recipient-name' or @placeholder='Recipient Name']"));
    private static final Locator PHONE_FIELD = new Locator("PHONE_FIELD", new By.ByXPath("//input[@id='mobile-number' or @name='phone']"));
    private static final Locator EMAIL_FIELD = new Locator("EMAIL_FIELD", new By.ByXPath("//input[@id='email-address-optional' or @placeholder='Email Address (Optional)']"));
    private static final Locator AMOUNT_FIELD = new Locator("AMOUNT_FIELD", new By.ByXPath("//input[@id='enter-the-amount' or @placeholder='Enter the amount']"));
    private static final Locator MESSAGE_FIELD = new Locator("MESSAGE_FIELD", new By.ByXPath("//textarea[@id='add-a-message-to-your-gift-optional' or @placeholder='Add a message to your gift (optional)']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[normalize-space()='Save']"));
    private static final Locator CONTINUE_SENDER_BUTTON = new Locator("CONTINUE_SENDER_BUTTON", new By.ByXPath("//button[normalize-space()='Continue']"));
    private static final Locator COMPLETE_ORDER_TEXT = new Locator("COMPLETE_ORDER_TEXT", new By.ByXPath("//h3[normalize-space()='Complete Your Order']"));
    private static final Locator TNC_BUTTON_SENDER_PAGE = new Locator("TNC_BUTTON_SENDER_PAGE", new By.ByXPath("//button[normalize-space()='See more']"));
    private static final Locator VALIDITY_PERIOD = new Locator("VALIDITY_PERIOD", new By.ByXPath("//h3[normalize-space()='Validity Period']"));
    private static final Locator ELIGIBLE_PRODUCT = new Locator("ELIGIBLE_PRODUCT", new By.ByXPath("//h3[normalize-space()='Eligible Products']/following::ul[1]/li"));
    private static final Locator INELIGIBLE_PRODUCT = new Locator("INELIGIBLE_PRODUCT", new By.ByXPath("//h3[normalize-space()='Ineligible Products']/following::ul[1]/li"));
    private static final Locator TNC_NOTES_SENDER_PAGE = new Locator("TNC_NOTES_SENDER_PAGE", new By.ByXPath("//h3[normalize-space()='Others']/following::ol[1]/li"));
    private static final Locator TNC_MODAL_CONTAINER =
            new Locator("TNC_MODAL_CONTAINER",
                    By.xpath("//section[contains(@class,'SenderTnc_container')]"));
    private static final Locator CLOSE_TNC_BUTTON = new Locator("CLOSE_TNC_BUTTON", new By.ByXPath("//button[normalize-space()='Close']"));
    private static final Locator RECIPIENT_MODAL_TITLE =
            new Locator("RECIPIENT_MODAL_TITLE",
                    By.xpath("//h3[contains(text(),'Recipient Details')]"));
    private static final Locator CONTINUE_TO_PAYMENT_BUTTON = new Locator("CONTINUE_TO_PAYMENT_BUTTON", new By.ByXPath("//button[normalize-space()='Continue to payment']"));
    private static final Locator PRICE_FOOTER = new Locator("PRICE_FOOTER", new By.ByXPath("//button[contains(@class, 'SenderFormContainer_price_button')]"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", new By.ByXPath("//button[contains(@class, 'SenderFormContainer_price_button')]/span"));
    private static final Locator NON_TOTAL_PRICE_DETAIL = new Locator("NON_TOTAL_PRICE_DETAIL", new By.ByXPath("//*[contains(@class, 'ModalResponsivePrice_list_item')]/span[1][not(text()='Total')]/following-sibling::span"));
    private static final Locator TOTAL_PRICE_DETAIL = new Locator("TOTAL_PRICE_DETAIL", new By.ByXPath("//*[contains(@class, 'ModalResponsivePrice_list_item')]/span[1][text()='Total']/following-sibling::span"));

    private static final Locator REDEEM_CODE_INPUT = new Locator("REDEEM_CODE_INPUT", new By.ByXPath("//input[@id='enter-the-redeem-code']"));
    private static final Locator REDEEM_BUTTON = new Locator("REDEEM_BUTTON", new By.ByXPath("//button[contains(@class,'RedeemDialog_redeem_button')]"));
    private static final Locator CONFIRM_YOUR_VOUCHER_BUTTON = new Locator("CONFIRM_YOUR_VOUCHER_BUTTON", new By.ByXPath("//div[contains(@class,'SplitVoucherForm')]//button"));
    private static final Locator TNC_REDEEM_DETAILS = new Locator("TNC_REDEEM_DETAILS", new By.ByXPath("//div[contains(@class,'DetailTermsAndConditions')]"));
    private static final Locator ERROR_POP_UP_MSG_TITLE_GIFT_CARD_INVALID = new Locator("ERROR_POP_UP_MSG_GIFT_CARD", new By.ByXPath("//h2[contains(@class, 'Figure_title')]"));
    private static final Locator CLICK_SEND_LATER = new Locator("CLICK_SEND_LATER", new By.ByXPath("//label[.//span[normalize-space()='Send later']]"));
    private static final Locator SENDER_NAME_INPUT_FIELD = new Locator("SENDER_NAME_INPUT_FIELD", new By.ByXPath("//input[@id='sender-name']"));
    private static final Locator TERMS_CONDITION = new Locator("TERMS_CONDITION", new By.ByXPath("//h3[normalize-space()='Terms and Conditions']"));
    private static final Locator SEND_DATE = new Locator("SEND_DATE", new By.ByXPath("//*[@id=\"send-date\"]"));
    private static final Locator SEND_TIME = new Locator("SEND_TIME", new By.ByXPath("//*[@id=\"send-time\"]"));
    private static final Locator RECIPIENT_NAME = new Locator("RECIPIENT_NAME", new By.ByXPath("//input[@id='full-name' and @placeholder='Full Name']"));

    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", new By.ByXPath("//button[normalize-space(.)='Continue to payment']"));
    private static final Locator RECIPIENT_NAME_INPUT_FIELD = new Locator("RECIPIENT_NAME_INPUT_FIELD", new By.ByXPath("//input[@id='recipient-name']"));
    private static final Locator RECIPIENT_NUMBER_INPUT_FIELD = new Locator("RECIPIENT_NUMBER_INPUT_FIELD", new By.ByXPath("//input[@id='mobile-number']"));
    private static final Locator RECIPIENT_EMAIL_INPUT_FIELD = new Locator("RECIPIENT_EMAIL_INPUT_FIELD", new By.ByXPath("//input[@id='email-address-optional']"));
    private static final Locator AMOUNT_INPUT_FIELD = new Locator("AMOUNT_INPUT_FIELD", new By.ByXPath("//input[@id='enter-the-amount']"));
    private static final Locator RECIPIENT_BUTTON = new Locator("RECIPIENT_BUTTON", new By.ByXPath("//div[contains(@class,'Recipients_actions')]"));
    private static final Locator CONTINUE_SENDERS_BUTTON = new Locator("CONTINUE_SENDERS_BUTTON", new By.ByXPath("//button[normalize-space()='Continue']"));
    private static final Locator RECIPIENT_EMAIL = new Locator("RECIPIENT_EMAIL", new By.ByXPath("//span[contains(text(), '@')]"));
    private static final Locator RECIPIENT_CONTACT = new Locator("RECIPIENT_CONTACT", new By.ByXPath("//span[starts-with(normalize-space(.), '+')]"));
    private static final Locator AMOUNT_CARD = new Locator("AMOUNT_CARD", new By.ByXPath("//div[contains(@class,'Recipients_card_body')]//span[starts-with(normalize-space(.), 'IDR')]"));
    private static final Locator SALUTATION_BUTTON = new Locator("SALUTATION_BUTTON", new By.ByXPath("//span[normalize-space()='Mr']"));
    private static final Locator GIFT_CARD_ORDER = new Locator("GIFT_CARD_ORDER", new By.ByXPath("//p[contains(normalize-space(.), 'Gift Card')]"));
    private static final Locator AMOUNT_CARD_NONIDR = new Locator("AMOUNT_CARD_NONIDR", new By.ByXPath("//div[contains(@class,'Recipients_card_body')]//span[starts-with(normalize-space(.), 'SGD')]"));

    private static final Locator RECIPIENT_NAME_ONLY = new Locator("RECIPIENT_NAME_ONLY", new By.ByXPath("//span[starts-with(normalize-space(.), '1.')]"));
    private static final Locator CARD_DESIGN_IMAGE = new Locator("CARD_DESIGN_IMAGE", new By.ByXPath("//div[contains(@class,'GiftCardSombrero_mask')]"));

    private static final Locator OCCASION_BUTTON(String occasion) {
        return new Locator(
                "OCCASION_BUTTON",
                By.xpath(String.format("//div[contains(@class,'OccasionSection_card_container')]//span[contains(text(),'%s')]", occasion))
        );
    }

    private static final Locator USER_CATEGORY_SELECTED(String occasion) {
        return new Locator(
                "USER_CATEGORY_SELECTED",
                By.xpath(String.format("//button[contains(@class,'Chip_chip')]/span[contains(text(),'%s')]", occasion))
        );
    }

    private static final Locator CARD_DESIGN(String occasion) {
        return new Locator(
                "CARD_DESIGN",
                By.xpath(String.format("//div[contains(@class,'GiftCardSombrero_mask')]//figcaption[contains(text(),'%s')]", occasion))
        );
    }

    private static final Locator NUMBER_OF_VOUCHERS = new Locator("NUMBER_OF_VOUCHERS", new By.ByXPath("//div[contains(@class,'GiftCardCode_count_indicator')]//span[text()='×']"));
    private static final Locator VALIDITY_PERIOD_SHOWN = new Locator("VALIDITY_PERIOD_SHOWN", new By.ByXPath("//h3[contains(text(),'Validity Period')]"));
    private static final Locator GIFT_CARD_VALIDITY_PERIOD_SHOWN = new Locator("GIFT_CARD_VALIDITY_PERIOD_SHOWN", new By.ByXPath("//div[contains(@class,'SenderTnc_validity_container')]"));
    private static final Locator ELIGIBLE_PRODUCTS = new Locator("ELIGIBLE_PRODUCTS", new By.ByXPath("//div[contains(@class,'SenderTnc_section_container')]//h3[text()='Eligible Products']"));
    private static final Locator INELIGIBLE_PRODUCTS = new Locator("INELIGIBLE_PRODUCTS", new By.ByXPath("//div[contains(@class,'SenderTnc_section_container')]//h3[text()='Ineligible Products']"));
    private static final Locator OTHER_DETAILS = new Locator("OTHER_DETAILS", new By.ByXPath("//div[contains(@class,'SenderTnc_section_container')]//h3[text()='Others']"));
    private static final Locator AMOUNT_VERIFY = new Locator("AMOUNT_VERIFY", new By.ByXPath("//figcaption[contains(@class,'GiftCardFigure_caption')]//span[contains(text(),'SGD')]"));
    private static final Locator GIFT_VOUCHER_DATE_LIMIT = new Locator("GIFT_VOUCHER_DATE_LIMIT", new By.ByXPath("//p[starts-with(text(),'Valid until')]"));
    private static final Locator GIFT_VOUCHER_CODE = new Locator("GIFT_VOUCHER_CODE", new By.ByXPath("//div[contains(@class,'GiftCardCode_voucher_content')]//p[contains(@class,'Text_weight_bold')]"));

    public abstract void maximize();

    public abstract void clickRedeemClaimGiftCard();

    public abstract void selectVoucherOptions();

    protected abstract void clickSpecificCardItem(int index);
    public abstract void clickAddRecipient();

    public void goToGiftCardPage() {
        Deeplink deepLink = new Deeplink("GIFT_CARD_LANDING",
                "https://gatotkaca.tiket.com/gift-card",
                "https://preprod.tiket.com/gift-card",
                "https://tiket.com/gift-card");

        switch (Configs.ENVIRONMENT) {
            case PROD:
                driverTools.get(deepLink.prodDeeplink());
                break;
            case PREPROD:
                driverTools.get(deepLink.preprodDeeplink());
                break;
            case GK:
                driverTools.get(deepLink.gkDeeplink());
                break;
        }
    }

    public void verifyLandingPageLoads() {
        driverTools.waitForElementToAppear(GIFT_CARD_HERO_IMAGE).orElseThrow();
    }

    public boolean isHeroImageVisible() {
        return driverTools.isElementDisplayed(GIFT_CARD_HERO_IMAGE);
    }

    public boolean isSendAndRedeemButtonVisible() {
        return driverTools.isElementDisplayed(SEND_GIFT_CARD_BUTTON) && driverTools.isElementDisplayed(REDEEM_GIFT_CARD_BUTTON);
    }

    public boolean isBulkOrderButtonVisible() {
        return driverTools.isElementDisplayed(BULK_ORDER_BUTTON);
    }

    public boolean isOccasionSectionVisible() {
        return driverTools.isElementDisplayed(LANDING_OCCASION_SECTION);
    }

    public void verifyClickOccasionButton() {
        driverTools.waitForElementToAppear(BIRTHDAY_OCCASION_BUTTON);
        driverTools.click(BIRTHDAY_OCCASION_BUTTON);
    }

    public boolean verifyRedirectToSenderPage() {
        driverTools.waitForElementToAppear(SELECT_CARD_DESIGN_TEXT);
        return driverTools.isElementDisplayed(SELECT_CARD_DESIGN_TEXT);
    }

    public boolean verifyRedirectToSelectCardDesign(String expectedValue) {
        driverTools.waitForElementToAppear(SELECT_CARD_DESIGN_TEXT);
        driverTools.waitForElementToAppear(OCCASION_BIRTHDAY_CHIP);
        String selectedOccasion = driverTools.getText(OCCASION_BIRTHDAY_CHIP).get();
        return driverTools.isElementDisplayed(SELECT_CARD_DESIGN_TEXT)
                && selectedOccasion.equals(expectedValue);
    }

    public boolean verifyRedirectionProcessSuccess() {
        return driverTools.isElementPresent(SELECT_CARD_DESIGN_TEXT);
    }

    public boolean isStaticUspListVisible() {
        return driverTools.isElementDisplayed(STATIC_USP_LIST);
    }

    public void clickSendGiftCardButton() {
        driverTools.waitForElementToAppear(SEND_GIFT_CARD_BUTTON).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(SEND_GIFT_CARD_HEADER).orElseThrow();
    }

    public boolean isCardItemsVisible() {
        driverTools.scrollIntoWebView(SEND_GIFT_CARD_HEADER);
        driverTools.waitForElementToAppear(CARD_ITEMS);
        return driverTools.isElementDisplayed(CARD_ITEMS);
    }

    public boolean verifyTextChangesAfterClick(int cardIndexToClick) {
        String initialText = driverTools.getText(FIGURE_CAPTION).orElseThrow(() ->
                new NoSuchElementException("Figure caption initial text not found"));
        clickSpecificCardItem(cardIndexToClick);
        String updatedText = driverTools.getText(FIGURE_CAPTION).orElseThrow(() ->
                new NoSuchElementException("Figure caption updated text not found"));
        return initialText.equals(updatedText);
    }

    public void clickRedeemGiftCard() {
        driverTools.waitForElementToBeClickable(REDEEM_GIFT_CARD);
        driverTools.clickWithJavascriptExecutor(REDEEM_GIFT_CARD);
    }

    public void clickRedeemCode() {
        driverTools.waitForElementToBeClickable(REDEEM_CODE);
        driverTools.clickWithJavascriptExecutor(REDEEM_CODE);
    }

    public void clickEditRecipientButton() {
        driverTools.waitForElementToPresence(ADD_RECIPIENT_BUTTON);
        driverTools.scrollIntoWebView(ADD_RECIPIENT_BUTTON);
        if (Configs.PLATFORM == Platform.IMWEB) {
            driverTools.click(ADD_RECIPIENT_BUTTON_IMG);
        } else {
            driverTools.click(ADD_RECIPIENT_BUTTON);
        }
    }

    public void fillRecipientDetails(String recipientName, String phoneNumber, String email, String amount, String message) {
        driverTools.waitForElementToAppear(RECIPIENT_NAME_FIELD, 10);
        driverTools.click(RECIPIENT_NAME_FIELD);
        driverTools.sendKeys(RECIPIENT_NAME_FIELD, recipientName);
        driverTools.click(PHONE_FIELD);
        driverTools.sendKeys(PHONE_FIELD, phoneNumber);
        if (email != null && !email.isEmpty()) {
            driverTools.click(EMAIL_FIELD);
            driverTools.sendKeys(EMAIL_FIELD, email);
        }
        driverTools.click(AMOUNT_FIELD);
        driverTools.sendKeys(AMOUNT_FIELD, amount);
        driverTools.click(MESSAGE_FIELD);
        driverTools.sendKeys(MESSAGE_FIELD, message);
        driverTools.click(SAVE_BUTTON);
    }

    public void clickContinueSenderButton() {
        if (Configs.PLATFORM == Platform.DWEB) {
            driverTools.scrollToElement(CONTINUE_SENDER_BUTTON);
        }
        driverTools.waitForElementToBeClickable(CONTINUE_SENDER_BUTTON);
        if (Configs.PLATFORM == Platform.IMWEB || Configs.PLATFORM == Platform.AMWEB) {
            driverTools.clickWithJavascriptExecutor(CONTINUE_SENDER_BUTTON);
        } else {
            driverTools.click(CONTINUE_SENDER_BUTTON);
        }
    }

    public boolean verifyCompleteOrderPageOpened() {
        return driverTools.isElementDisplayed(COMPLETE_ORDER_TEXT);
    }

    public void verifyTncSenderPage() {
        driverTools.waitForElementToAppear(TNC_BUTTON_SENDER_PAGE);
        driverTools.scrollIntoWebView(TNC_BUTTON_SENDER_PAGE);
        driverTools.waitForElementToBeClickable(TNC_BUTTON_SENDER_PAGE);
        driverTools.click(TNC_BUTTON_SENDER_PAGE);
    }

    public boolean isTncPeriodVisible() {
        return driverTools.isElementDisplayed(VALIDITY_PERIOD);
    }

    public boolean isEligibleProductsVisible() {
        return driverTools.isElementDisplayed(ELIGIBLE_PRODUCT);
    }

    public boolean isIneligibleProductsVisible() {
        return driverTools.isElementDisplayed(INELIGIBLE_PRODUCT);
    }

    public boolean isTncNotesSendePageVisible() {
        return driverTools.isElementDisplayed(TNC_NOTES_SENDER_PAGE);
    }

    public boolean isTncModalVisible() {
        driverTools.waitForElementToAppear(TNC_MODAL_CONTAINER, 5);
        return driverTools.isElementDisplayed(TNC_MODAL_CONTAINER);
    }

    public boolean closeTncAndVerifyClosed() {
        driverTools.click(CLOSE_TNC_BUTTON);
        return driverTools.waitForElementToDisappear(TNC_MODAL_CONTAINER, 10);
    }


    public void inputCodeInputField(String value) {
        driverTools.waitForElementToAppear(GIFT_CARD_INPUT_FIELD, 10);
        driverTools.clickWithJavascriptExecutor(GIFT_CARD_INPUT_FIELD);
        driverTools.sendKeys(GIFT_CARD_INPUT_FIELD, value);
    }

    public boolean verifyReedemedGiftCardErrorMsgFunct(String errorMsgEN, String errorMsgID) {
        boolean isErrorMessageDisplayed = driverTools.waitForElementToAppear(ERROR_POP_UP_MSG_TITLE_GIFT_CARD).isPresent();
        if (isErrorMessageDisplayed) {
            String errorMsgTitle = String.valueOf(driverTools.getText(ERROR_POP_UP_MSG_TITLE_GIFT_CARD));
            String errorMsg = String.valueOf(driverTools.getText(ERROR_POP_UP_MSG_GIFT_CARD));
            return (errorMsgTitle.contains(errorMsgEN) || errorMsg.contains(errorMsgID));
        } else {
            logger.log("No error message is displayed");
            return false;
        }
    }

    public void clickRedeemGiftCardButton() {
        driverTools.waitForElementToAppear(REDEEM_GIFT_CARD_BUTTON);
        driverTools.clickWithJavascriptExecutor(REDEEM_GIFT_CARD_BUTTON);
    }

    public void inputRedeemCode(String giftCode) {
        driverTools.waitForElementToAppear(REDEEM_BUTTON);
        driverTools.waitForElementToAppear(REDEEM_CODE_INPUT);
        driverTools.sendKeys(REDEEM_CODE_INPUT, giftCode);
    }

    public void clickRedeem() {
        driverTools.waitForElementToBeClickable(REDEEM_BUTTON);
        driverTools.clickWithJavascriptExecutor(REDEEM_BUTTON);
    }

    public GiftCardOrderResponse createGiftCardOrderViaAPI(
            String currency, int voucherAmount,
            String profileId, String emailAddress, String fullName,
            String phone, String title,
            String loginEmail, String loginPassword
    ) {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return createGiftCardOrderViaAPIOnce(currency, voucherAmount, profileId, emailAddress, fullName, phone, title, loginEmail, loginPassword);
            } catch (Exception e) {
                logger.log("Attempt " + attempt + "/" + maxAttempts + " to create gift card order failed: " + e.getMessage());
                if (attempt == maxAttempts) {
                    throw e;
                }
                logger.log("Retrying gift card order creation via API (attempt " + (attempt + 1) + "/" + maxAttempts + ")");
            }
        }
        throw new IllegalStateException("Unreachable");
    }

    private GiftCardOrderResponse createGiftCardOrderViaAPIOnce(
            String currency, int voucherAmount,
            String profileId, String emailAddress, String fullName,
            String phone, String title,
            String loginEmail, String loginPassword
    ) {
        Gson gson = new Gson();

        GiftCardGetInventoryApi.Inputs giftCardGetInventoryApiInput = new GiftCardGetInventoryApi.Inputs(currency);
        GiftCardGetInventoryApi giftCardGetInventoryApi = new GiftCardGetInventoryApi(giftCardGetInventoryApiInput);
        ApiResponse inventoryApiResponse = giftCardGetInventoryApi.hitApi(apiTools);
        GiftCardInventoryResponse giftCardInventoryResponse = gson.fromJson(inventoryApiResponse.getBody(), GiftCardInventoryResponse.class);

        int randomIndex = CoreFunctions.generateRandomInt(0, giftCardInventoryResponse.data.inventories.size() - 1);
        String randomInventoryId = giftCardInventoryResponse.data.inventories.get(randomIndex).id;
        String searchKey = giftCardInventoryResponse.data.searchKey;

        GiftCardCreateCartApi.Inputs giftCardCreateCartApiInput = new GiftCardCreateCartApi.Inputs(searchKey, randomInventoryId, voucherAmount, currency, profileId, emailAddress, fullName, phone, title);
        GiftCardCreateCartApi giftCardCreateCartApi = new GiftCardCreateCartApi(giftCardCreateCartApiInput);
        ApiResponse cartApiResponse = giftCardCreateCartApi.hitApi(apiTools);
        GiftCardCartResponse giftCardCartResponse = gson.fromJson(cartApiResponse.getBody(), GiftCardCartResponse.class);

        String cartId = giftCardCartResponse.data.cartId;
        String cartSecret = giftCardCartResponse.data.cartSecret;

        LoginDataUnmResponse.Data loginData = new Unm<>(session).unmLoginApi(loginEmail, loginPassword);
        String accountId = String.valueOf(loginData.accountId);
        String username = loginData.accountUsername;
        String cookieHeader = buildGiftCardCookieHeader(loginData, currency);

        GiftCardCreateGCOrderApi.Inputs giftCardCreateGCOrderApiInput = new GiftCardCreateGCOrderApi.Inputs(
                cartId,
                cartSecret,
                Instant.now().toString(),
                profileId,
                emailAddress,
                fullName,
                phone,
                title,
                currency,
                fullName,
                "Asia/Calcutta",
                cookieHeader,
                accountId,
                username,
                ""
        );
        GiftCardCreateGCOrderApi giftCardCreateGCOrderApi = new GiftCardCreateGCOrderApi(giftCardCreateGCOrderApiInput);
        ApiResponse orderApiResponse = giftCardCreateGCOrderApi.hitApi(apiTools);
        JSONObject orderJson = new JSONObject(orderApiResponse.getBody());
        // A failed order carries no booking data, so the body is safe to log and is the only
        // way to see the backend's error code.
        if (orderJson.isNull("data")) {
            throw new IllegalStateException("Create gift card order returned no data. HTTP "
                    + orderApiResponse.getStatusCode() + ", body: " + orderApiResponse.getBody());
        }
        GiftCardOrderResponse orderResponse = gson.fromJson(orderJson.getJSONObject("data").toString(), GiftCardOrderResponse.class);
        logger.log("Created gift card order, HTTP " + orderApiResponse.getStatusCode()
                + ", orderId " + (orderResponse.order == null ? null : orderResponse.order.orderId));
        return orderResponse;
    }

    private String buildGiftCardCookieHeader(LoginDataUnmResponse.Data loginData, String currency) {
        StringBuilder cookie = new StringBuilder();
        appendCookie(cookie, "device_id", loginData.uniqueId);
        appendCookie(cookie, "uniqueId", loginData.uniqueId);
        appendCookie(cookie, "userlang", loginData.lang == null || loginData.lang.isBlank() ? "en" : loginData.lang);
        appendCookie(cookie, "tiket_currency", currency == null || currency.isBlank() ? "IDR" : currency.toUpperCase());
        appendCookie(cookie, "country_code", "id");
        appendCookie(cookie, "session_access_token", loginData.accessToken);
        appendCookie(cookie, "session_refresh_token", loginData.refreshToken);
        return cookie.toString();
    }

    private void appendCookie(StringBuilder cookie, String name, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        if (cookie.length() > 0) {
            cookie.append("; ");
        }
        cookie.append(name).append("=").append(value);
    }

    public String verifyGiftCardOrderPayment(String orderId, String orderHash) {
        GiftCardIssuanceApi.Inputs giftCardIssuanceApiInputs = new GiftCardIssuanceApi.Inputs(orderId, orderHash);
        GiftCardIssuanceApi giftCardIssuanceApi = new GiftCardIssuanceApi(giftCardIssuanceApiInputs);
        ApiResponse apiResponse = giftCardIssuanceApi.hitApi(apiTools);
        logger.log("Got response ->" + apiResponse);
        GiftCardIssuanceResponse giftCardIssuanceResponse = new Gson().fromJson(apiResponse.getBody(), GiftCardIssuanceResponse.class);
        return giftCardIssuanceResponse.data.bookingStatus;
    }

    public String verifyissuance(String orderId, String orderHash) {
        GiftCardGetOrderDetailsApi.Inputs giftCardGetOrderDetailsApiInputs = new GiftCardGetOrderDetailsApi.Inputs(orderId, orderHash);
        GiftCardGetOrderDetailsApi giftCardGetOrderDetailsApi = new GiftCardGetOrderDetailsApi(giftCardGetOrderDetailsApiInputs);
        ApiResponse apiResponse = giftCardGetOrderDetailsApi.hitApi(apiTools);
        logger.log("Got response ->" + apiResponse);
        JSONObject jsonData = new JSONObject(apiResponse.getBody());
        GiftCardOrderDetailsResponse giftCardOrderDetailsResponse = new Gson().fromJson(jsonData.getJSONObject("data").toString(), GiftCardOrderDetailsResponse.class);
        return giftCardOrderDetailsResponse.bookingStatus;
    }

    public String getRedeemCode(String orderId, String orderHash) {
        GiftCardGetOrderDetailsApi.Inputs giftCardGetOrderDetailsApiInputs = new GiftCardGetOrderDetailsApi.Inputs(orderId, orderHash);
        GiftCardGetOrderDetailsApi giftCardGetOrderDetailsApi = new GiftCardGetOrderDetailsApi(giftCardGetOrderDetailsApiInputs);
        ApiResponse apiResponse = giftCardGetOrderDetailsApi.hitApi(apiTools);
        logger.log("Got response ->" + apiResponse);
        JSONObject jsonData = new JSONObject(apiResponse.getBody());
        GiftCardOrderDetailsResponse giftCardOrderDetailsResponse = new Gson().fromJson(jsonData.getJSONObject("data").toString(), GiftCardOrderDetailsResponse.class);
        return giftCardOrderDetailsResponse.inventories.get(0).bookingItems.get(0).redeemCode;
    }

    public void clickSubmitConfirmVoucherButton() {
        driverTools.clickWithJavascriptExecutor(CONFIRM_YOUR_VOUCHER_BUTTON);
    }

    public boolean isTncRedeemDetailsShown() {
        driverTools.waitForElementToAppear(TNC_REDEEM_DETAILS).orElseThrow();
        return driverTools.isElementDisplayed(TNC_REDEEM_DETAILS);
    }

    public boolean verifyInvalidGiftCardCodeErrorMsgFunct(String errorMsgEN, String errorMsgID) {
        boolean isErrorMessageDisplayed = driverTools.waitForElementToAppear(ERROR_POP_UP_MSG_TITLE_GIFT_CARD_INVALID).isPresent();
        if (isErrorMessageDisplayed) {
            String errorMsgTitle = String.valueOf(driverTools.getText(ERROR_POP_UP_MSG_TITLE_GIFT_CARD_INVALID));
            String errorMsg = String.valueOf(driverTools.getText(ERROR_POP_UP_MSG_GIFT_CARD));
            return (errorMsgTitle.contains(errorMsgEN) || errorMsg.contains(errorMsgID));
        } else {
            logger.log("No error message is displayed");
            return false;
        }
    }

    public void inputRecipientNameInput(String value) {
        driverTools.waitForElementToAppear(RECIPIENT_NAME_FIELD, 10);
        driverTools.sendKeys(RECIPIENT_NAME_FIELD, value);
    }

    public void inputRECIPIENT_MOBILE(String value) {
        driverTools.waitForElementToAppear(PHONE_FIELD, 10);
        driverTools.clickWithJavascriptExecutor(PHONE_FIELD);
        driverTools.sendKeys(PHONE_FIELD, value);
    }

    public void inputRECIPIENT_EMAIL(String value) {
        driverTools.waitForElementToAppear(EMAIL_FIELD, 10);
        driverTools.clickWithJavascriptExecutor(EMAIL_FIELD);
        driverTools.sendKeys(EMAIL_FIELD, value);
    }

    public void recipient_Click_Save() {
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void click_Send_Later() {
        driverTools.waitForElementToAppear(CLICK_SEND_LATER);
        driverTools.waitForElementToBeClickable(CLICK_SEND_LATER);
        driverTools.clickWithJavascriptExecutor(CLICK_SEND_LATER);
    }

    public void fillSenderName(String value) {
        driverTools.waitForElementToAppear(SENDER_NAME_INPUT_FIELD, 10);
        driverTools.sendKeys(SENDER_NAME_INPUT_FIELD, value);
    }

    public Boolean isTermsAndConditionVisible() {
        return driverTools.waitForElementToPresence(TERMS_CONDITION, 5).isPresent();
    }

    public Boolean isSenderNameDisplayed() {
        return driverTools.waitForElementToPresence(SENDER_NAME_INPUT_FIELD, 5).isPresent();
    }

    public Boolean sendDate() {
        return driverTools.waitForElementToPresence(SEND_DATE, 5).isPresent();
    }

    public Boolean sendTime() {
        return driverTools.waitForElementToPresence(SEND_TIME, 5).isPresent();
    }

    public Boolean getRecipientNameValue() {
        return driverTools.waitForElementToPresence(RECIPIENT_NAME, 5).isPresent();
    }

    public void clickContinueButton(){
        driverTools.swipeToElement(CONTINUE_BUTTON, Swipe.UP, 10);
        driverTools.waitForElementToBeClickable(CONTINUE_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
        driverTools.waitForElementToAppear(SALUTATION_BUTTON, 15);
    }

    public void clickSalutation(){
        driverTools.waitForElementToAppear(SALUTATION_BUTTON,10);
        driverTools.waitForElementToBeClickable(SALUTATION_BUTTON);
        driverTools.clickWithJavascriptExecutor(SALUTATION_BUTTON);
    }

    public void fillRecipientName(String value){
        driverTools.waitForElementToAppear(RECIPIENT_NAME_INPUT_FIELD, 10);
        driverTools.clickWithJavascriptExecutor(RECIPIENT_NAME_INPUT_FIELD);
        driverTools.sendKeys(RECIPIENT_NAME_INPUT_FIELD, value);
    }

    public void fillContactNumber(String value){
        driverTools.waitForElementToAppear(RECIPIENT_NUMBER_INPUT_FIELD, 10);
        driverTools.clickWithJavascriptExecutor(RECIPIENT_NUMBER_INPUT_FIELD);
        driverTools.sendKeys(RECIPIENT_NUMBER_INPUT_FIELD, value);
    }

    public void fillEmailAddress(String value){
        driverTools.waitForElementToAppear(RECIPIENT_EMAIL_INPUT_FIELD, 10);
        driverTools.clickWithJavascriptExecutor(RECIPIENT_EMAIL_INPUT_FIELD);
        driverTools.sendKeys(RECIPIENT_EMAIL_INPUT_FIELD, value);
    }

    public void fillGiftCardAmount(String value){
        driverTools.waitForElementToAppear(AMOUNT_INPUT_FIELD, 10);
        driverTools.clickWithJavascriptExecutor(AMOUNT_INPUT_FIELD);
        driverTools.sendKeys(AMOUNT_INPUT_FIELD, value);
    }

    public void continueSaving(){
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.waitForElementToBeClickable(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void continueToSendersForm(){
        driverTools.waitForElementToAppear(CONTINUE_SENDERS_BUTTON);
        driverTools.waitForElementToBeClickable(CONTINUE_SENDERS_BUTTON);
        driverTools.clickWithJavascriptExecutor(CONTINUE_SENDERS_BUTTON);
    }

    public String isRecepientNamePresent() {

        String name = driverTools.getText(RECIPIENT_NAME_ONLY).orElseThrow();
        name = name.replaceAll("^\\d+\\.\\s*", "");
        return name;
    }

    public String isRecepientNumberPresent() {
        String number = String.valueOf(driverTools.getText(RECIPIENT_CONTACT));
        number = number.replaceFirst("^\\+\\d{2}", "");
        return number;
    }

    public String isRecepientEmailPresent() {
        return String.valueOf(driverTools.getText(RECIPIENT_EMAIL));
    }

    public String isGiftCardAmountPresent() {
        String amt = String.valueOf(driverTools.getText(AMOUNT_CARD));
        amt = amt.replace("IDR", "").trim();
        amt = amt.replace(",", "");
        return amt;
    }

    public String isGiftCardAmountPresentNonIDR() {
        String amt = String.valueOf(driverTools.getText(AMOUNT_CARD_NONIDR));
        amt = amt.replace("IDR", "").trim();
        amt = amt.replace(",", "");
        return amt;
    }

    public void verifyClickSpecificOccasionButton(String occasion){
        WebElement element = driverTools.waitForElementToAppear(OCCASION_BUTTON(occasion)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(element);
    }

    public boolean verifyRedirectToSelectSpecificCardDesign(String occasion){
        driverTools.waitForElementToAppear(SELECT_CARD_DESIGN_TEXT);
        String selectedOccasion = driverTools.getText(OCCASION_BIRTHDAY_CHIP).orElse("");
        boolean textConfirmation= selectedOccasion.equals(occasion);
        boolean imageConfirmation = driverTools.isElementPresent(CARD_DESIGN_IMAGE) && driverTools.isElementPresent(CARD_DESIGN(occasion));
        return textConfirmation && imageConfirmation;
    }

    public Boolean verifyValidityPeriod() {
        return driverTools.isElementPresent(GIFT_CARD_VALIDITY_PERIOD_SHOWN) && driverTools.isElementPresent(VALIDITY_PERIOD_SHOWN);
    }

    public Boolean verifyEligibleProducts() {
        return driverTools.isElementPresent(ELIGIBLE_PRODUCTS);
    }

    public Boolean verifyInEligibleProducts() {
        return driverTools.isElementPresent(INELIGIBLE_PRODUCTS);
    }

    public Boolean verifyOtherDetails() {
        return driverTools.isElementPresent(OTHER_DETAILS);
    }

    public Boolean verifyGiftCardAmount() {
        return driverTools.isElementPresent(AMOUNT_VERIFY);
    }

    public Boolean verifyGiftCardExpiryDate() {
        return driverTools.isElementPresent(GIFT_VOUCHER_DATE_LIMIT);
    }

    public Boolean verifyNumbersOfGiftCardVouchers() {
        return driverTools.isElementPresent(NUMBER_OF_VOUCHERS);
    }

    public Boolean verifyRedeemGiftCardCode() {
        return driverTools.isElementPresent(GIFT_VOUCHER_CODE);
    } 
  
    public long verifyPriceDetails() {
        driverTools.scrollIntoWebView(PRICE_FOOTER);
        driverTools.clickWithJavascriptExecutor(PRICE_FOOTER);

        driverTools.waitForElementToAppear(TOTAL_PRICE_DETAIL, 10);
        long totalPrice = CoreFunctions.removeText(driverTools.getText(TOTAL_PRICE_DETAIL).orElseThrow());

        driverTools.waitForElementsToAppear(NON_TOTAL_PRICE_DETAIL, 10);
        long nonTotalPriceDetailsSum = driverTools.getTexts(NON_TOTAL_PRICE_DETAIL)
                .stream()
                .map(CoreFunctions::getLongFromString)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToLong(Long::valueOf)
                .sum();

        Assertion.assertThat("Price detail and total price is the same", nonTotalPriceDetailsSum, is(totalPrice));
        return  nonTotalPriceDetailsSum;

    }

    public long getPrice() {
        return CoreFunctions.removeText(driverTools.getText(TOTAL_PRICE).orElseThrow());
    }
}