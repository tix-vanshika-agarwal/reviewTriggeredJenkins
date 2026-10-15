package com.tiket.web.page.common.home.Setting_Page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;

public abstract class SettingsPageWeb<T extends WebDriver> extends WebPage<T> {
    public SettingsPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> SettingsPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new SettingsPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new SettingsPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator HELP_CENTER = new Locator("HELP_CENTER", By.xpath("//*[text()='Pusat Bantuan halo tiket' or text()='halo tiket Help Center']"));
    private static final Locator CHATBOT = new Locator("CHATBOT", By.xpath("//div[@id='helpCenterRevampContactStepOne']//*[text()='Chat']"));
    private static final Locator CHAT_IFEAME = new Locator("CHAT_IFAME", By.xpath("//div[@data-testid='chatWindow']"));
    private static final Locator CHAT_LAST_MESSAGE = new Locator("CHAT_LAST_MESSAGE", By.xpath("//div[@data-testid='chat-bubble']"));
    private static final Locator ORDER_ID = new Locator("ORDER_ID", By.xpath("//*[@data-testid=\"orderDesktopId\"]"));
    private static final Locator REFUND_BTN = new Locator("REFUND_BTN", By.xpath("//*[contains(@class,'ActionButton_action_title')]//*[(contains(text(),'Refund'))]"));
    private static final Locator CONFIRM_REDUND_PAGE = new Locator("CONFIRM_REDUND_PAGE", By.xpath("(//button[contains(@class,'styles_btn_continue_refund')])[last()-1]"));
    private static final Locator POPULAR_ARTICLE = new Locator("POPULAR_ARTICLE", By.xpath("//section[contains(@class,'styles_popular_question_section')]/h1"));
    private static final Locator POPULAR_ARTICLE_LIST = new Locator("POPULAR_ARTICLE_LIST", By.xpath("//a[contains(@class,'styles_popular_question_item')]"));
    private static final Locator SKIP_COACHMARK_BUTTON = new Locator("SKIP_COACHMARK_BUTTON", new By.ByXPath("//button[contains(@class, 'Coachmark_coachmark_skip_btn')]"));
    private static final Locator COACHMARK_SKIP_OR_LEWATI = new Locator(
            "COACHMARK_SKIP_OR_LEWATI",
            new By.ByXPath(
                    "//button[normalize-space()='Skip' or normalize-space()='Lewati' "
                            + "or .//span[normalize-space()='Skip' or normalize-space()='Lewati']]"));
    private static final Locator OK_COACHMARK_BUTTON = new Locator("OK_COACHMARK_BUTTON", new By.ByXPath("//button[contains(@class, 'Coachmark_coachmark_done_btn')]"));
    private static final Locator ACCOUNT_ACTIVITY_EMAIL_TOGGLE = new Locator("ACCOUNT_ACTIVITY_EMAIL_TOGGLE", new By.ByXPath("//*[@data-testid='activity-tix-message-batch']//*[contains(@class,'Toggle_container')]"));
    private static final Locator ACCOUNT_ACTIVITY_PUSH_TOGGLE = new Locator("ACCOUNT_ACTIVITY_PUSH_TOGGLE", new By.ByXPath("//*[@data-testid='activity-tix-push-notif']//*[contains(@class,'Toggle_container')]"));
    private static final Locator EXCLUSIVE_INFO_PUSH_TOGGLE = new Locator("EXCLUSIVE_INFO_PUSH_TOGGLE", new By.ByXPath("//*[@data-testid='promotion-tix-push-notif']//*[contains(@class,'Toggle_container')]"));
    private static final Locator EXCLUSIVE_INFO_EMAIL_TOGGLE = new Locator("EXCLUSIVE_INFO_EMAIL_TOGGLE", new By.ByXPath("//*[@data-testid='promotion-tix-message-batch']//*[contains(@class,'Toggle_container')]"));
    private static final Locator REMINDERS_INFO_EMAIL_TOGGLE = new Locator("REMINDERS_INFO_EMAIL_TOGGLE", new By.ByXPath("//*[@data-testid='reminder-tix-message-batch']//*[contains(@class,'Toggle_container')]"));
    private static final Locator REMINDERS_INFO_PUSH_TOGGLE = new Locator("REMINDERS_INFO_PUSH_TOGGLE", new By.ByXPath("//*[@data-testid='reminder-tix-push-notif']//*[contains(@class,'Toggle_container')]"));
    private static final Locator BLIBLI_INFO_PUSH_TOGGLE = new Locator("BLIBLI_INFO_PUSH_TOGGLE", new By.ByXPath("//*[@data-testid='membership-tix-push-notif']//*[contains(@class,'Toggle_container')]"));
    private static final Locator BLIBLI_EMAIL_PUSH_TOGGLE = new Locator("BLIBLI_EMAIL_PUSH_TOGGLE", new By.ByXPath("//*[@data-testid='membership-tix-message-batch']//*[contains(@class,'Toggle_container')]"));
    private static final Locator NOTIFICATION_SETTING_TITLES = new Locator("NOTIFICATION_SETTING_TITLES", new By.ByXPath("//*[contains(@id,'highlight')]/h3"));
    private static final Locator CHECKED_TOGGLE = new Locator("CHECKED_TOGGLE", new By.ByXPath("//*[@checked]"));
    private static final Locator UNCHECKED_TOGGLE = new Locator("UNCHECKED_TOGGLE", new By.ByXPath("//*[@type='checkbox' and not(@checked)]"));
    private static final Locator SEND_MESSAGE = new Locator("SEND_MESSAGE", By.xpath("(//button[contains(@class, 'styles_cs_desktop_item')])[1]"));
    private static final Locator COMPLAINT_DROPDOWN = new Locator("COMPLAINT_DROPDOWN", By.xpath("//select[contains(@class, 'form-control')]"));
    private static final Locator COMPLAINT_TYPE = new Locator("COMPLAINT_TYPE", By.className("form-control"));
    private static final Locator COMPLAINT_REASON = new Locator("COMPLAINT_REASON", By.xpath("//select[@class='form-control' and @id='j_id0:myForm:l2']"));
    private static final Locator COMPLAINT_DESCRIPTION = new Locator("COMPLAINT_DESCRIPTION", By.xpath("//textarea[@id='j_id0:myForm:desc']"));
    private static final Locator SUBMIT_COMPLAINT = new Locator("SUBMIT_COMPLAINT", By.xpath("//input[@value='Continue']"));
    private static final Locator POPULAR_ELEMENT = new Locator("POPULAR_ELEMENT", By.xpath("(//h1[contains(@class, 'Text_text') and contains(@class, 'Text_size_h3')])[4]"));
    private static final Locator FIRST_QUESTION = new Locator("FIRST_QUESTION", By.xpath("(//h2[contains(@class, 'Text_text') and contains(@class, 'Text_size') and contains(@class, 'Text_weight_regular')])[9]"));
    private static final Locator ARTICLE_TITLE = new Locator("ARTICLE_TITLE", By.xpath("(//h1[contains(@class, 'Text_text') and contains(@class, 'Text_size')])[1]"));
    private static final Locator CONTACT_US = new Locator("CONTACT_US", By.xpath("//button[@data-testid='btnContactUsEntrypoint']"));
    private static final Locator CUSTOMER_CARE = new Locator("CUSTOMER_CARE", By.xpath("//h1[contains(text(),'Hubungi') or contains(text(),'Contact')]"));
    private static final Locator EMAIL = new Locator("EMAIL", By.xpath("//h3[contains(text(), 'Email')]"));
    private static final Locator WHATSAPP_NUMBER = new Locator("WHATSAPP_NUMBER", By.xpath("//button[@data-testid='cta-WHATSAPP']"));
    private static final Locator ORDERS_LIST = new Locator("ORDERS_LIST", By.xpath("//div[starts-with(@data-testid, 'orderDesktop-')]"));
    private static final Locator RELATED_ARTICLES = new Locator("RELATED_ARTICLES", By.xpath("//h2[contains(text(), 'Related Articles') or contains(text(), 'Artikel Terkait')]"));
    private static final Locator POP_UP = new Locator("POP_UP", By.xpath("//h2[contains(@class, 'Text_text') and contains(@class, 'Text_size_h3')]"));
    private static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", By.xpath("//button[@data-testid='ym-close-bot-button']"));
    private static final Locator OPEN_ACCOUNT_CENTER = new Locator("OPEN_ACCOUNT_CENTER", new By.ByXPath("(//a[@role='button' and normalize-space(text())='To Account Center']) | (//*[self::span or self::a][normalize-space(text())='To Account Center' or normalize-space(text())='Ke Pusat Akun'])"));
    private static final Locator SETTINGS_COUNTRY = new Locator("SETTINGS_COUNTRY", new By.ByXPath("//div[@data-testid='settings-country']"));
    private static final Locator SEARCH_COUNTRY_INPUT = new Locator("SEARCH_COUNTRY_INPUT", new By.ByXPath("//div[contains(@class,'SearchBox_input_box')]/div[contains(@class,'SearchBox_placeholder')]/label/input"));
    private static final Locator FIRST_COUNTRY_LIST_ITEM = new Locator("FIRST_COUNTRY_LIST_ITEM", new By.ByXPath("(//div[starts-with(@class, 'CountryListSelection_list_item')])[1]"));
    private static final Locator COUNTRY_NAME_SPAN = new Locator("COUNTRY_NAME_SPAN", new By.ByXPath("//div[@data-testid='settings-country']//div[contains(@class, 'SettingsItem_item_right')]//span[contains(@class, 'Text_variant')]"));
    private static final Locator EDIT_ICON = new Locator("EDIT_ICON", new By.ByXPath("//*[self::span or self::a or self::button][normalize-space(.)='Edit' or normalize-space(.)='Ubah']"));
    private static final Locator CHANGE_LANGUAGE = new Locator("CHANGE_LANGUAGE", new By.ByXPath("//span[text()='Language' or text()='Bahasa']"));
    private static final Locator HC_LANGUAGE = new Locator("HC_LANGUAGE", new By.ByXPath("//button[contains(@class, 'language_save_button')]//span"));
    private static final Locator CURRENT_LANGUAGE = new Locator("CURRENT_LANGUAGE", new By.ByXPath("(//span[contains(@class,'Text_variant_lowEmphasis')])[6]"));
    private static final Locator RADIO_BUTTON_ONE = new Locator("RADIO_BUTTON_ONE", new By.ByXPath("(//span[contains(@class,'Radio_icon')])[1]"));
    private static final Locator RADIO_BUTTON_TWO = new Locator("RADIO_BUTTON_TWO", new By.ByXPath("(//span[contains(@class,'Radio_icon')])[2]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[text()='Save' or text()='Simpan']"));
    private static final String Indonesia = "Indonesia";
    private static final String English = "English";

    private Locator getInitialChat(String name) {
        return new Locator("INITIAL_CHAT", By.xpath("//*[contains(@class,'bot-message')]//*[contains(text(),'" + name + "')]"));
    }

    public void openHelpCenter() {
        try {
            driverTools.waitForElementToBeClickable(HELP_CENTER);
            driverTools.click(HELP_CENTER);
        }catch (ElementClickInterceptedException e){
            logger.log("Retry click help center");
            driverTools.clickWithJavascriptExecutor(HELP_CENTER);
        }
        skipCoachmark();
    }

    public void openHelpCenterGuest() {
        try {
            driverTools.waitForElementToBeClickable(HELP_CENTER);
            driverTools.click(HELP_CENTER);
        }catch (ElementClickInterceptedException e){
            logger.log("Retry click help center");
            driverTools.clickWithJavascriptExecutor(HELP_CENTER);
        }
    }

    @SneakyThrows
    public void openChatBot() {
        driverTools.waitForElementToAppear(CHATBOT,3).ifPresent(driverTools::clickWithJavascriptExecutor);
        if (driverTools.waitForElementToAppear(HC_LANGUAGE, 3).isPresent()) {
            driverTools.clickWithJavascriptExecutor(HC_LANGUAGE);
        } else {
            logger.log("HC_LANGUAGE element not found; skipping language save click");
        }
    }

    public boolean isInsideChatBot() {
        try {
            Optional<WebElement> chatIframe = driverTools.waitForElementToAppear(CHAT_IFEAME, 3);
            if (chatIframe.isPresent()) {
                boolean isChatVisible = driverTools.waitForElementToPresence(CHAT_LAST_MESSAGE, 3).isPresent();
                return isChatVisible;
            }
            return false;
        } catch (Exception e) {
            logger.log("Not inside chatbot: " + e.getMessage());
            return false;
        }
    }

    public boolean initialChatVisible(String name) {
        Optional<WebElement> frame = driverTools.element(CHAT_IFEAME);
        driverTools.switchToIframe(frame.orElseThrow());
        return driverTools.waitForElementToPresence(CHAT_LAST_MESSAGE, 20).isPresent();
    }

    public boolean checkOrderIDHelpCenter(String orderID) {
        String orderIDUi = String.valueOf(driverTools.getText(ORDER_ID));
        logger.log("Fetched Order ID: " + orderIDUi);
        Matcher matcher = Pattern.compile("\\d+").matcher(orderIDUi);
        if (matcher.find()) {
            orderIDUi = matcher.group();
        }
        return orderIDUi.equals(orderID);
    }

    public boolean ClickAndProceedRefund() {
        driverTools.clickWithJavascriptExecutor(REFUND_BTN);
        return driverTools.waitForElementToPresence(CONFIRM_REDUND_PAGE).isPresent();
    }

    public boolean checkPopularArticle() {
        return driverTools.waitForElementToPresence(POPULAR_ARTICLE).isPresent();
    }

    public String selectPopularArticle() {
        Optional<List<WebElement>> questionElementsList = driverTools.elements(POPULAR_ARTICLE_LIST);
        if (questionElementsList.isPresent() && !questionElementsList.get().isEmpty()) {
            List<WebElement> elements = questionElementsList.get();
            int randomIndex = CoreFunctions.generateRandomInt(0, elements.size() - 1);
            WebElement randomQuestionElement = elements.get(randomIndex);
            driverTools.scrollIntoWebView(randomQuestionElement);
            String selectedQuestionText = randomQuestionElement.getText();
            driverTools.clickWithJavascriptExecutor(randomQuestionElement);
            logger.log("Selected question: " + selectedQuestionText);
            return selectedQuestionText;
        } else {
            logger.log("No popular articles found.");
            return null;
        }
    }

    public boolean checkSelectedPopularArticle(String selectedQuestionText) {
        return driverTools.waitForElementToPresence(getQuestion(selectedQuestionText)).isPresent();
    }

    private Locator getQuestion(String question) {
        return new Locator("QUESTION", By.xpath("//h1[contains(text(),'" + question + "')]"));
    }

    public void skipCoachmarkBySkipOrLewatiText() {
        for (int attempt = 0; attempt < 4; attempt++) {
            if (driverTools.waitForElementToAppear(COACHMARK_SKIP_OR_LEWATI, 2).isEmpty()) {
                return;
            }
            driverTools.clickWithJavascriptExecutor(COACHMARK_SKIP_OR_LEWATI);
            driverTools.waitForElementToDisappear(COACHMARK_SKIP_OR_LEWATI, 4);
        }
    }

    public void skipCoachmark() {
        Optional<WebElement> skipButton = driverTools.waitForElementToAppear(SKIP_COACHMARK_BUTTON,3);
        Optional<WebElement> okButton = driverTools.waitForElementToAppear(OK_COACHMARK_BUTTON,3);
        skipButton.ifPresent(element -> {
            if (driverTools.element(SKIP_COACHMARK_BUTTON).isPresent()) {
                driverTools.clickWithJavascriptExecutor(SKIP_COACHMARK_BUTTON);
                driverTools.waitForElementToDisappear(SKIP_COACHMARK_BUTTON);
            }
        });
        okButton.ifPresent(element -> {
            if (driverTools.element(OK_COACHMARK_BUTTON).isPresent()) {
                driverTools.clickWithJavascriptExecutor(OK_COACHMARK_BUTTON);
                driverTools.waitForElementToDisappear(OK_COACHMARK_BUTTON);
            } else {
                logger.log("No coachmark button spotted");
            }
        });
    }


    public abstract void openNotificationSetting();

    public boolean verifyAllToggleExist() {
        Assertion.assertThat("Account Activity Email Toggle exist", driverTools.waitForElementToPresence(ACCOUNT_ACTIVITY_EMAIL_TOGGLE).isPresent(), is(true));
        Assertion.assertThat("Account Activity Push Toggle exist", driverTools.waitForElementToPresence(ACCOUNT_ACTIVITY_PUSH_TOGGLE).isPresent(), is(true));
        Assertion.assertThat("Exclusive Info Push Toggle exist", driverTools.waitForElementToPresence(EXCLUSIVE_INFO_PUSH_TOGGLE).isPresent(), is(true));
        Assertion.assertThat("Exclusive Info Email Toggle exist", driverTools.waitForElementToPresence(EXCLUSIVE_INFO_EMAIL_TOGGLE).isPresent(), is(true));
        Assertion.assertThat("Reminders Info Email Toggle exist", driverTools.waitForElementToPresence(REMINDERS_INFO_EMAIL_TOGGLE).isPresent(), is(true));
        Assertion.assertThat("Reminders Info Push Toggle exist", driverTools.waitForElementToPresence(REMINDERS_INFO_PUSH_TOGGLE).isPresent(), is(true));
        Assertion.assertThat("Blibli Info Push Toggle exist", driverTools.waitForElementToPresence(BLIBLI_INFO_PUSH_TOGGLE).isPresent(), is(true));
        Assertion.assertThat("Blibli Info Email Toggle exist", driverTools.waitForElementToPresence(BLIBLI_EMAIL_PUSH_TOGGLE).isPresent(), is(true));
        return true;
    }

    public List<String> getNotificationSettingWordings() {
        return driverTools.getTexts(NOTIFICATION_SETTING_TITLES);
    }

    public abstract void clickBackButtonNotificationSettings();

    public abstract boolean isInSettingsPage();

    public int getCheckedToggleCount() {
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(CHECKED_TOGGLE, 5);
        List<WebElement> checkedToggle = driverTools.elements(CHECKED_TOGGLE).orElseThrow();
        logger.log("Total checked toggle: " + checkedToggle.size());
        return checkedToggle.size();
    }

    @SneakyThrows
    public void turnOffAllToggle() {
        driverTools.waitForElementToPresence(CHECKED_TOGGLE);
        List<WebElement> checkedToggle = driverTools.elements(CHECKED_TOGGLE).orElseThrow();
        int noOfElement = checkedToggle.size();
        for (int i = 0; i < noOfElement; i++) {
            checkedToggle = driverTools.elements(CHECKED_TOGGLE).orElseThrow();
            driverTools.clickWithJavascriptExecutor(checkedToggle.get(i));
            Thread.sleep(500);
        }
    }

    @SneakyThrows
    public void turnOnAllToggle() {
        driverTools.waitForElementToPresence(UNCHECKED_TOGGLE);
        List<WebElement> uncheckedToggle = driverTools.elements(UNCHECKED_TOGGLE).orElseThrow();
        int noOfElement = uncheckedToggle.size();
        for (int i = 0; i < noOfElement; i++) {
            uncheckedToggle = driverTools.elements(UNCHECKED_TOGGLE).orElseThrow();
            driverTools.clickWithJavascriptExecutor(uncheckedToggle.get(i));
            Thread.sleep(500);
        }
    }

    public void clickSendMessage() {
        driverTools.scrollToElement(SEND_MESSAGE);
        Assertion.assertThat("Send message tab is present", driverTools.isElementDisplayed(SEND_MESSAGE), is(true));
        driverTools.clickWithJavascriptExecutor(SEND_MESSAGE);
    }

    public void clickActiveOrder() {
        driverTools.isElementDisplayed(ORDER_ID);
        Assertion.assertThat("Active orders are present", true, is(true));
        driverTools.clickWithJavascriptExecutor(ORDER_ID);
    }

    public boolean submitRequest() {
        driverTools.click(COMPLAINT_DROPDOWN);
        logger.log("Selecting option in complaint dropdown");
        Assertion.assertThat("Complaint dropdown is present", driverTools.isElementDisplayed(COMPLAINT_DROPDOWN), is(true));
        Assertion.assertThat("Complaint type dropdown is present", driverTools.isElementDisplayed(COMPLAINT_TYPE), is(true));
        Assertion.assertThat("Complaint reason dropdown is present", driverTools.isElementDisplayed(COMPLAINT_REASON), is(true));
        logger.log("Entering description in box");
        Assertion.assertThat("Complaint description box is present", driverTools.isElementDisplayed(COMPLAINT_DESCRIPTION), is(true));
        WebElement dropdownElement = driver.findElement(COMPLAINT_DROPDOWN.by());
        Select dropdown = new Select(dropdownElement);
        dropdown.selectByIndex(1);
        driverTools.click(COMPLAINT_TYPE);
        WebElement dropdownElement2 = driver.findElement(COMPLAINT_TYPE.by());
        Select dropdown2 = new Select(dropdownElement2);
        dropdown2.selectByIndex(1);
        driverTools.click(COMPLAINT_REASON);
        WebElement dropdownElement3 = driver.findElement(COMPLAINT_REASON.by());
        Select dropdown3 = new Select(dropdownElement3);
        dropdown3.selectByIndex(1);
        WebElement textarea = driver.findElement(COMPLAINT_DESCRIPTION.by());
        textarea.sendKeys("Complaint description");
        logger.log("Submitting the request");
        boolean res = driverTools.isElementDisplayed(SUBMIT_COMPLAINT);
        driverTools.clickWithJavascriptExecutor(SUBMIT_COMPLAINT);
        return res;
    }

    public void openPopularArticleQuestion() throws InterruptedException {
        driverTools.scrollToElement(POPULAR_ELEMENT);
        Assertion.assertThat("Popular Article section is present", driverTools.isElementDisplayed(POPULAR_ELEMENT), is(true));
        driverTools.scroll(2, 15);
        driverTools.scrollToElement(FIRST_QUESTION);
        driverTools.clickWithJavascriptExecutor(FIRST_QUESTION);
    }

    @SneakyThrows
    public boolean navigateToContactUs() {
        Assertion.assertThat("Is Popular Article page open", driverTools.isElementDisplayed(ARTICLE_TITLE), is(true));
        driverTools.waitForElementToAppear(CONTACT_US);
        driverTools.click(CONTACT_US);
        skipCoachmark();
        return driverTools.isElementDisplayed(CUSTOMER_CARE);
    }

    public boolean checkDetails() {
        Assertion.assertThat("Email address is present and clickable", driverTools.isElementDisplayed(EMAIL), is(true));
        driverTools.click(EMAIL);
        logger.log("Again opened the contact us page");
        if (driverTools.isElementDisplayed(WHATSAPP_NUMBER)) {
            Assertion.assertThat("Whatsapp number is present and clickable", driverTools.isElementDisplayed(WHATSAPP_NUMBER), is(true));
            driverTools.click(WHATSAPP_NUMBER);
            return true;
        }
        return false;
    }

    public boolean verifyNewOrderSectionPresent() {
        driverTools.waitForElementToPresence(CLOSE_BUTTON);
        driverTools.click(CLOSE_BUTTON);
        logger.log("Checking if recent orders are present");
        boolean res = driverTools.isElementDisplayed(ORDERS_LIST);
        driverTools.back();
        return res;
    }

    public void verifyRelatedArticleSection() {
        driverTools.isElementDisplayed(RELATED_ARTICLES);
        Assertion.assertThat("Related articles are present", driverTools.isElementDisplayed(RELATED_ARTICLES), is(true));
    }

    public void sendMessagePopUpAppears() {
        driverTools.waitForElementToAppear(POP_UP);
        Assertion.assertThat("Pop up appeared", driverTools.isElementDisplayed(POP_UP), is(true));
    }

    public void openAccountCenter() {
        driverTools.waitForElementToAppear(OPEN_ACCOUNT_CENTER);
        driverTools.clickWithJavascriptExecutor(OPEN_ACCOUNT_CENTER);
    }

    public void openCountryRegion() {
        driverTools.waitForElementToPresence(SETTINGS_COUNTRY);
        driverTools.clickWithJavascriptExecutor(SETTINGS_COUNTRY);
    }

    public void changeCountryRegion(String country) {
        driverTools.waitForElementToPresence(SEARCH_COUNTRY_INPUT);
        driverTools.clickWithJavascriptExecutor(SEARCH_COUNTRY_INPUT);
        driverTools.sendKeys(SEARCH_COUNTRY_INPUT, country);
        driverTools.waitForElementToAppear(FIRST_COUNTRY_LIST_ITEM);
        driverTools.clickWithJavascriptExecutor(FIRST_COUNTRY_LIST_ITEM);
    }

    public String checkCountryName(String country) throws InterruptedException {
        driverTools.waitForElementToPresence(COUNTRY_NAME_SPAN);
        Thread.sleep(5000);
        Optional<String> optionalText = driverTools.getText(COUNTRY_NAME_SPAN);
        if (optionalText.isEmpty()) {
            logger.log("Country name element is not present.");
            return "Empty";
        }

        String extractedCountryName = optionalText.get().trim();
        logger.log("Country : " + country + ", Extracted : " + extractedCountryName);

        return extractedCountryName.toLowerCase();
    }

    public void clickEditIcon() {
        driverTools.waitForElementToAppear(EDIT_ICON);
        driverTools.clickWithJavascriptExecutor(EDIT_ICON);
    }

    public String changeLanguage() {
        String currentLanguage = driverTools.getText(CURRENT_LANGUAGE).orElseThrow();
        logger.log("Current Language ->" + currentLanguage);
        clickChangeLanguage();
        if (currentLanguage.equalsIgnoreCase(Indonesia)) {
            driverTools.waitForElementToPresence(RADIO_BUTTON_TWO);
            driverTools.click(RADIO_BUTTON_TWO);
        } else {
            driverTools.waitForElementToPresence(RADIO_BUTTON_ONE);
            driverTools.click(RADIO_BUTTON_ONE);
        }
        driverTools.click(SAVE_BUTTON);
        driverTools.refreshWebpage();
        return currentLanguage;
    }

    public void clickChangeLanguage() {
        driverTools.waitForElementToAppear(CHANGE_LANGUAGE);
        driverTools.clickWithJavascriptExecutor(CHANGE_LANGUAGE);
    }

    public Boolean verifyLanguage(String oldLanguage) {
        String currentLanguage = driverTools.getText(CURRENT_LANGUAGE).orElseThrow();
        logger.log("Old Language ->" + oldLanguage);
        logger.log("Current Language ->" + currentLanguage);
        if (oldLanguage.equalsIgnoreCase(Indonesia)) {
            return currentLanguage.equalsIgnoreCase(English);
        } else {
            return currentLanguage.equalsIgnoreCase(Indonesia);
        }
    }

}
