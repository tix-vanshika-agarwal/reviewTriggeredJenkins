package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.room.SelectRoomResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;

public class PromoAndCampaignPage <T extends WebDriver> extends BasePage<T> {
    public PromoAndCampaignPage(Session<T> session) {
        super(session);
    }
    private static final Locator REWARDS_PAGE_TITLE = new Locator("REWARDS_PAGE_TITLE", By.xpath("//h2[text()='Blibli Tiket Rewards']"));
    private static final Locator JOIN_NOW_BUTTON = new Locator("JOIN_NOW_BUTTON", By.xpath("//button[.//span[translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='join now' or translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='gabung sekarang']]"));
    private static final Locator REMOVE_PROGRAM_LINK = new Locator("REMOVE_PROGRAM_LINK", By.xpath("//p[text()='Remove from program' or text()='Hapus dari program']"));
    private static final Locator YES_REMOVE_BTN = new Locator("YES_REMOVE_BTN", By.xpath("//button[.//span[normalize-space(.)='Yes, remove it' or normalize-space(.)='Ya, hapus']]"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));
    private static final Locator SET_BENEFITS_HEADER = new Locator("SET_BENEFITS_HEADER", By.xpath("//span[text()='Set Benefits' or text()='Atur Benefit']"));
    private static final Locator DISCOUNT_INPUT = new Locator("DISCOUNT_INPUT", By.xpath("//input[@name='discount']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[@type='submit' and (normalize-space(.)='Submit' or normalize-space(.)='Kirim')]"));
    private static final Locator EDIT_SUBMIT_BUTTON = new Locator("EDIT_SUBMIT_BUTTON", By.xpath("//button[@type='submit' and (normalize-space(.)='Request approval' or normalize-space(.)='Ajukan perubahan')]"));
    private static final Locator ADD_BLACKOUT_DATES_LINK = new Locator("ADD_BLACKOUT_DATES_LINK", By.xpath("//span[contains(., '+ Add other dates') or contains(., '+ Tambah tanggal lain')]"));
    private static final Locator DISCOUNT_ERROR_CONTAINER = new Locator("DISCOUNT_ERROR_CONTAINER", By.xpath("//input[@name='discount']/ancestor::div[contains(@class, 'styles_container')]//div[contains(@class, 'styles_state_danger')]"));
    private static final Locator EXTRA_BENEFIT_YES = new Locator("EXTRA_BENEFIT_YES", By.xpath("//label[.//input[@name='extraBenefit' and contains(@value, 'yes')]]"));
    private static final Locator EXTRA_BENEFIT_NO = new Locator("EXTRA_BENEFIT_NO", By.xpath("//label[.//input[@name='extraBenefit' and contains(@value, 'no')]]"));
    private static final Locator ROOM_RATE_PLAN_ALL_LABEL = new Locator("ROOM_RATE_PLAN_ALL_LABEL", By.xpath("//label[.//input[@name='roomRatePlan' and contains(@value, 'all')]]"));
    private static final Locator ROOM_RATE_PLAN_ALL_INPUT = new Locator("ROOM_RATE_PLAN_ALL_INPUT", By.xpath("//input[@name='roomRatePlan' and contains(@value, 'all')]"));
    private static final Locator START_DATE_INPUT = new Locator("START_DATE_INPUT", By.xpath("//input[@id='start-date' or @id='tanggal-mulai']"));
    private static final Locator END_DATE_INPUT = new Locator("END_DATE_INPUT", By.xpath("//input[@id='end-date' or @id='tanggal-selesai']"));
    private static final Locator CALENDAR_DAY_BUTTONS = new Locator("CALENDAR_DAY_BUTTONS", By.xpath("//button[contains(@class, 'Day_day')]"));
    private static final By CALENDAR_DAY_BUTTONS_BY = By.xpath("//button[contains(@class, 'Day_day')]");
    private static final By TODAY_LABEL_XPATH = By.xpath(".//small[contains(@class, 'Day_today_label') or contains(text(), 'Today')]");
    private static final Locator BENEFIT_DETAILS_HEADER = new Locator("BENEFIT_DETAILS_HEADER", By.xpath("//h3[text()='Your benefit details' or text()='Detail benefit Anda']"));
    private static final Locator SUMMARY_DISCOUNT = new Locator("SUMMARY_DISCOUNT", By.xpath("//td[.//span[text()='Discount' or text()='Diskon']]/following-sibling::td//span"));
    private static final Locator SUMMARY_EXTRA_BENEFIT = new Locator("SUMMARY_EXTRA_BENEFIT", By.xpath("//td[.//span[text()='Extra benefits' or text()='Benefit Ekstra']]/following-sibling::td//span"));
    private static final Locator SUMMARY_ROOMS = new Locator("SUMMARY_ROOMS", By.xpath("//td[.//span[contains(text(), 'Rooms & Rate Plans') or contains(text(), 'Kamar & Rate Plan')]]/following-sibling::td//span"));
    private static final Locator SUMMARY_BLACKOUT_DATES = new Locator("SUMMARY_BLACKOUT_DATES", By.xpath("//td[.//span[text()='Blackout Dates' or text()='Tanggal Blackout']]/following-sibling::td//li"));
    private static final Locator MANAGE_BENEFIT_BUTTON = new Locator("MANAGE_BENEFIT_BUTTON", By.xpath("//button[.//span[normalize-space(.)='Manage benefit' or normalize-space(.)='Atur benefit']]"));
    private static final Locator EXTRA_BENEFITS_YES_RADIO = new Locator("EXTRA_BENEFITS_YES_RADIO", By.xpath("//label[.//input[@name='extraBenefit' and contains(@value, 'yes')]]"));
    private static final Locator EXTRA_BENEFITS_CHECKBOX_LABELS = new Locator("EXTRA_BENEFITS_CHECKBOX_LABELS", By.xpath("//label[contains(@class, 'Check_check') and .//input[@type='checkbox' and (@value='DISCOUNT' or @value='FREE_BREAKFAST' or @value='FREE_CLUB_LOUNGE_ACCESS' or @value='FREE_EARLY_CHECK_IN' or @value='FREE_LATE_CHECK_OUT')]]"));
    private static final Locator SPECIFIC_ROOM_RATE_RADIO = new Locator("SPECIFIC_ROOM_RATE_RADIO", By.xpath("//label[.//input[@name='roomRatePlan' and contains(@value, 'custom')]]"));
    private static final Locator SELECT_ROOMS_INPUT = new Locator("SELECT_ROOMS_INPUT", By.xpath("//input[contains(@class, 'DropdownFormDisplay_input') and (@value='Select rooms' or @value='Pilih kamar')]"));
    private static final Locator SELECT_ALL_ROOMS_BUTTON = new Locator("SELECT_ALL_ROOMS_BUTTON", By.xpath("//button[contains(normalize-space(.), 'Select all') or contains(normalize-space(.), 'Pilih semua')]"));
    private static final Locator POPOVER_DONE_BUTTON = new Locator("POPOVER_DONE_BUTTON", By.xpath("//button[contains(normalize-space(.),'Select all') or contains(normalize-space(.),'Pilih semua')]/following-sibling::button[contains(normalize-space(.),'Done') or contains(normalize-space(.),'Selesai')]"));
    private static final Locator MAIN_MODAL_DONE_BUTTON = new Locator("MAIN_MODAL_DONE_BUTTON", By.xpath("//div[contains(@class,'Dialog_body')]//button[@type='submit' and (contains(normalize-space(.),'Done') or contains(normalize-space(.),'Selesai') or contains(normalize-space(.),'Simpan'))]"));
    private static final Locator POPOVER_ROOM_CHECKBOXES = new Locator("POPOVER_ROOM_CHECKBOXES", By.xpath("//div[contains(@class, 'max-h-[300px]')]//label[contains(@class, 'Check_check')]//input[@type='checkbox']"));
    private static final Locator RATE_PLAN_CHECKBOXES = new Locator("RATE_PLAN_CHECKBOXES", By.xpath("//section[contains(@class, 'flex flex-col gap-3')]//label[contains(@class, 'Check_check')]//input[@type='checkbox']"));
    private static final Locator YES_CONTINUE_BUTTON = new Locator("YES_CONTINUE_BUTTON", By.xpath("//div[contains(@class, 'Figure_footer')]//button[contains(normalize-space(.), 'Yes, continue') or contains(normalize-space(.), 'Ya, Lanjutkan')]"));
    private static final Locator REVIEW_BANNER_TEXT = new Locator("REVIEW_BANNER_TEXT", By.xpath("//span[contains(normalize-space(.), 'requested for changes') or contains(normalize-space(.), 'mengajukan perubahan')]"));
    private static final Locator SEE_REQUEST_BUTTON = new Locator("SEE_REQUEST_BUTTON", By.xpath("//button[.//span[contains(normalize-space(.), 'See request') or contains(normalize-space(.), 'Lihat pengajuan')]]"));
    private static final Locator SELECTED_ROOM_SUMMARY = new Locator("SELECTED_ROOM_SUMMARY", By.xpath("//div[contains(@class, 'bg-[var(--color-N100)]')]//ul/li/span"));
    private static final Locator EXTRA_BENEFIT_CHECKBOXES = new Locator("EXTRA_BENEFIT_CHECKBOXES", By.xpath("//label[contains(@class, 'Check_check')]//input[@type='checkbox']"));
    private static final Locator NEW_ROOMS_RATE_PLANS_VALUE = new Locator("NEW_ROOMS_RATE_PLANS_VALUE", By.xpath("//tr[td[1]//span[contains(text(), 'Rooms & Rate Plans') or contains(text(), 'Kamar & Rate Plan')]]/td[3]"));
    private static final Locator NEW_EXTRA_BENEFITS_VALUE = new Locator("NEW_EXTRA_BENEFITS_VALUE", By.xpath("//tr[td[1]//span[contains(text(), 'Extra benefits') or contains(text(), 'Benefit Ekstra')]]/td[3]"));
    private static final Locator APPROVE_BUTTON = new Locator("APPROVE_BUTTON", By.xpath("//button[.//span[normalize-space(.)='Approve' or normalize-space(.)='Setujui']]"));
    private static final Locator APPROVED_DISCOUNT_VALUE = new Locator("APPROVED_DISCOUNT_VALUE", By.xpath("//tr[td[1]//span[contains(normalize-space(.), 'Discount') or contains(normalize-space(.), 'Diskon')]]/td[2]"));
    private static final Locator APPROVED_EXTRA_BENEFITS_VALUE = new Locator("APPROVED_EXTRA_BENEFITS_VALUE", By.xpath("//tr[td[1]//span[contains(normalize-space(.), 'Extra benefits') or contains(normalize-space(.), 'Benefit Ekstra')]]/td[2]"));
    private static final Locator APPROVED_ROOMS_VALUE = new Locator("APPROVED_ROOMS_VALUE", By.xpath("//tr[td[1]//span[contains(normalize-space(.), 'Rooms & Rate Plans') or contains(normalize-space(.), 'Kamar & Rate Plan')]]/td[2]"));
    public void verifyRewardsPageLoaded() {
        WebElement pageTitle = driverTools.waitForElementToAppear(REWARDS_PAGE_TITLE, 10).orElseThrow(() -> new NoSuchElementException("Blibli Tiket Rewards page title did not load"));
        Assertion.assertThat("Verify Blibli Tiket Rewards page title is Loaded", pageTitle.isDisplayed(), is(true));
    }

    public boolean isJoinNowButtonPresent() {
        return driverTools.isElementDisplayed(JOIN_NOW_BUTTON);
    }

    public boolean isRemoveProgramLinkVisible(){
        return driverTools.isElementDisplayed(REMOVE_PROGRAM_LINK);
    }

    public void removePropertyFromRewards() {
        WebElement removeLink = driverTools.waitForElementToAppear(REMOVE_PROGRAM_LINK, 5).orElseThrow(() -> new NoSuchElementException("Remove link not found"));
        removeLink.click();
        WebElement yesRemoveBtn = driverTools.waitForElementToAppear(YES_REMOVE_BTN, 5).orElseThrow(() -> new NoSuchElementException("'Yes, remove' button not found"));
        yesRemoveBtn.click();
    }

    public void verifyJoinNowButtonAppears() {
        WebElement joinNowBtn = driverTools.waitForElementToAppear(JOIN_NOW_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("Join now button did not appear"));
        Assertion.assertThat("Verify 'Join now' button is present", joinNowBtn.isDisplayed(), is(true));
    }

    public void verifySnackbar(String expectedEn, String expectedId) {
        WebElement snackbar = driverTools.waitForElementToAppear(SNACKBAR, 5).orElseThrow(() -> new NoSuchElementException("Remove success snackbar did not appear"));
        Assertion.assertThat("Verify snackbar is displayed", snackbar.isDisplayed(), is(true));
        String actualMessage = snackbar.getText().trim();
        Assertion.assertThat("Verify snackbar message text is correct. Expected En: " +expectedEn+" Expected Id: "+expectedId+ " Actual: " + actualMessage, actualMessage, anyOf(containsStringIgnoringCase(expectedEn), containsStringIgnoringCase(expectedId)));
        boolean isDismissed = driverTools.waitForElementToDisappear(SNACKBAR, 5);
        Assertion.assertThat("Verify success snackbar dismissed", isDismissed, is(true));
    }

    public void clickJoinNowButton(){
        driverTools.waitForElementToAppear(JOIN_NOW_BUTTON);
        driverTools.click(JOIN_NOW_BUTTON);
    }

    public void verifyRewardsSetupPageLoaded() {
        WebElement setBenefitsHeader = driverTools.waitForElementToAppear(SET_BENEFITS_HEADER, 10).orElseThrow(() -> new NoSuchElementException("'Set Benefits' / 'Atur Benefit' header did not load"));
        Assertion.assertThat("Verify 'Set Benefits' header is displayed", setBenefitsHeader.isDisplayed(), is(true));
        WebElement discountInput = driverTools.waitForElementToAppear(DISCOUNT_INPUT, 5).orElseThrow(() -> new NoSuchElementException("Discount percentage input field not found"));
        Assertion.assertThat("Verify discount input field is displayed", discountInput.isDisplayed(), is(true));
        WebElement submitBtn = driverTools.waitForElementToAppear(SUBMIT_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("Submit/Kirim button not found"));
        Assertion.assertThat("Verify Submit button is displayed", submitBtn.isDisplayed(), is(true));
    }

    public void clickAddBlackoutDates() {
        WebElement addDatesLink = driverTools.waitForElementToAppear(ADD_BLACKOUT_DATES_LINK, 5).orElseThrow(() -> new NoSuchElementException("Add blackout dates link not found"));
        driverTools.scrollElementToCenter(addDatesLink);
        addDatesLink.click();
    }

    public void clickSubmitButton() {
        WebElement submitBtn = driverTools.waitForElementToAppear(SUBMIT_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("Submit button not found"));
        driverTools.scrollElementToCenter(submitBtn);
        submitBtn.click();
    }

    public void verifyUIValidationErrorMessage(String expectedEn, String expectedId) {
        String dynamicXpath = String.format("//*[translate(normalize-space(.), ',', '')=\"%s\" or translate(normalize-space(.), ',', '')=\"%s\"]", expectedEn, expectedId);
        Locator errorLocator = new Locator("ERROR_MSG_" + expectedEn, By.xpath(dynamicXpath));
        WebElement errorElement = driverTools.waitForElementToAppear(errorLocator, 5).orElseThrow(() -> new NoSuchElementException("Error message not found: " + expectedEn + " / " + expectedId));
        driverTools.scrollElementToCenter(errorElement);
        String actualText = errorElement.getText().replace(",", "").trim();
        Assertion.assertThat("Verify validation error message is correctly displayed. Expected = " + expectedEn + " or " + expectedId + " | Actual = " + actualText, actualText, anyOf(containsString(expectedEn), containsString(expectedId)));
    }

    public void fillDiscount(String discountValue) {
        WebElement discountField = driverTools.waitForElementToAppear(DISCOUNT_INPUT, 5).orElseThrow(() -> new NoSuchElementException("Discount input field not found"));
        driverTools.scrollElementToCenter(discountField);
        driverTools.click(DISCOUNT_INPUT);
        driverTools.clearInput(DISCOUNT_INPUT);
        driverTools.sendKeys(discountField, discountValue);
    }

    public void verifyDiscountAccepted(String expectedValue) {
        WebElement discountField = driverTools.waitForElementToAppear(DISCOUNT_INPUT, 5).orElseThrow(() -> new NoSuchElementException("Discount input field not found"));
        String actualValue = discountField.getAttribute("value");
        Assertion.assertThat("Verify discount input retains valid value", actualValue, is(expectedValue));
        boolean isErrorPresent = driverTools.isElementDisplayed(DISCOUNT_ERROR_CONTAINER);
        Assertion.assertThat("Verify no error message is displayed for valid discount", isErrorPresent, is(false));
    }

    public void verifyExtraBenefitOptionsAvailable() {
        WebElement yesOption = driverTools.waitForElementToAppear(EXTRA_BENEFIT_YES, 5).orElseThrow(() -> new NoSuchElementException("Extra Benefit 'Yes' option not found"));
        Assertion.assertThat("Verify Extra Benefit 'Yes' option is displayed", yesOption.isDisplayed(), is(true));
        WebElement noOption = driverTools.waitForElementToAppear(EXTRA_BENEFIT_NO, 5).orElseThrow(() -> new NoSuchElementException("Extra Benefit 'No' option not found"));
        Assertion.assertThat("Verify Extra Benefit 'No' option is displayed", noOption.isDisplayed(), is(true));
    }

    public void selectExtraBenefitNo() {
        WebElement noOption = driverTools.waitForElementToAppear(EXTRA_BENEFIT_NO, 5).orElseThrow(() -> new NoSuchElementException("Extra Benefit 'No' option not found"));
        driverTools.scrollElementToCenter(noOption);
        noOption.click();
    }

    public void selectAllRoomsAndRatePlans() {
        WebElement allOptionLabel = driverTools.waitForElementToAppear(ROOM_RATE_PLAN_ALL_LABEL, 5).orElseThrow(() -> new NoSuchElementException("'All rooms and rate plans' option not found"));
        driverTools.scrollElementToCenter(allOptionLabel);
        allOptionLabel.click();
    }

    public void verifyAllRoomsAndRatePlansSelected() {
        WebElement allOptionInput = driverTools.waitForElementToPresence(ROOM_RATE_PLAN_ALL_INPUT, 5).orElseThrow(() -> new NoSuchElementException("'All rooms and rate plans' input not found"));
        Assertion.assertThat("Verify 'All rooms and rate plans' radio button is selected", allOptionInput.isSelected(), is(true));
    }

    public void selectBlackoutDateRangeNext7Days() {
        WebElement startDateInput = driverTools.waitForElementToAppear(START_DATE_INPUT, 5).orElseThrow();
        driverTools.scrollElementToCenter(startDateInput);
        startDateInput.click();

        driverTools.waitForElementToAppear(CALENDAR_DAY_BUTTONS, 5).orElseThrow();
        List<WebElement> startDays = driverTools.getDriver().findElements(CALENDAR_DAY_BUTTONS_BY);

        int todayIndex = -1;
        for (int i = 0; i < startDays.size(); i++) {
            if (!startDays.get(i).findElements(TODAY_LABEL_XPATH).isEmpty()) {
                todayIndex = i;
                break;
            }
        }
        if (todayIndex == -1) {
            throw new NoSuchElementException("Could not find the 'Today' element in the Start Date calendar.");
        }
        startDays.get(todayIndex).click();

        WebElement endDateInput = driverTools.waitForElementToAppear(END_DATE_INPUT, 5).orElseThrow();
        driverTools.scrollElementToCenter(endDateInput);
        endDateInput.click();
        driverTools.waitForElementToAppear(CALENDAR_DAY_BUTTONS, 5).orElseThrow();
        List<WebElement> endDays = driverTools.getDriver().findElements(CALENDAR_DAY_BUTTONS_BY);
        int endDateIndex = todayIndex + 7;
        if (endDateIndex >= endDays.size()) {
            throw new IndexOutOfBoundsException("End date is out of the currently rendered calendar view. (Need index " + endDateIndex + " but only have " + endDays.size() + ")");
        }
        endDays.get(endDateIndex).click();
    }

    public List<String> getSelectedBlackoutDates() {
        WebElement startDateInput = driverTools.waitForElementToAppear(START_DATE_INPUT, 5).orElseThrow(() -> new NoSuchElementException("Start date input not found"));
        String startDate = startDateInput.getAttribute("value");
        WebElement endDateInput = driverTools.waitForElementToAppear(END_DATE_INPUT, 5).orElseThrow(() -> new NoSuchElementException("End date input not found"));
        String endDate = endDateInput.getAttribute("value");
        return Arrays.asList(startDate, endDate);
    }

    public void verifySubmittedBenefitDetails(String expectedDiscount, String expectedStartDate, String expectedEndDate) {
        WebElement header = driverTools.waitForElementToAppear(BENEFIT_DETAILS_HEADER, 5).orElseThrow(() -> new NoSuchElementException("Benefit details summary page did not load."));
        driverTools.scrollElementToCenter(header);

        String actualDiscount = driverTools.waitForElementToAppear(SUMMARY_DISCOUNT, 5).orElseThrow().getText().trim();
        Assertion.assertThat("Verify discount matches", actualDiscount, is(expectedDiscount + "%"));

        String actualExtraBenefit = driverTools.waitForElementToAppear(SUMMARY_EXTRA_BENEFIT, 5).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Extra Benefit is No/Tidak", actualExtraBenefit, anyOf(equalToIgnoringCase("No"), equalToIgnoringCase("Tidak")));

        String actualRooms = driverTools.waitForElementToAppear(SUMMARY_ROOMS, 5).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Rooms & Rate Plans selection", actualRooms, anyOf(equalToIgnoringCase("All rooms and rate plans"), equalToIgnoringCase("Semua kamar dan rate plan")));

        String summaryStartPattern = CoreFunctions.convertDateFormat(expectedStartDate, "EEE, dd MMM yyyy", "d MMMM yyyy");
        String summaryEndPattern = CoreFunctions.convertDateFormat(expectedEndDate, "EEE, dd MMM yyyy", "d MMMM yyyy");
        String actualBlackoutDates = driverTools.waitForElementToAppear(SUMMARY_BLACKOUT_DATES, 5).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Blackout Dates contain expected start and end dates. Actual UI text: " + actualBlackoutDates, actualBlackoutDates, allOf(containsString(summaryStartPattern), containsString(summaryEndPattern)));
    }

    public void verifyCampaignDiscountApplied(SelectRoomResponse response, String expectedDiscount) {
        String discountStr = expectedDiscount.contains("%") ? expectedDiscount : expectedDiscount + "%";
        SelectRoomResponse.RateInfo rateInfo = response.data.rooms.get(0).ratePlans.get(0).rateInfo;
        Assertion.assertThat("Verify rateInfo loyalty block is present", rateInfo.loyalty, notNullValue());

        Map<String, Object> rateLoyaltyMap = (Map<String, Object>) rateInfo.loyalty;
        Map<String, Object> detailsMap = (Map<String, Object>) rateLoyaltyMap.get("details");
        Map<String, Object> tierMap = (Map<String, Object>) rateLoyaltyMap.get("tier");
        Assertion.assertThat("Verify tier details object exists in API response", tierMap, notNullValue());
        String actualApiTierValue = ((String) tierMap.get("value")).trim();
        Assertion.assertThat("Verify user tier status is classified as premium (Gold, Platinum, or Diamond)", actualApiTierValue, anyOf(is("Gold"), is("Platinum"), is("Diamond")));

        String actualSummary = (String) detailsMap.get("summary");
        Assertion.assertThat("Verify active rate plan contains exact campaign rewards summary", actualSummary, anyOf(is("Discount " + discountStr), is("Diskon " + discountStr)));
        Assertion.assertThat("Verify loyaltyInfo is present", response.data.loyaltyInfo, notNullValue());

        Map<String, Object> loyaltyInfoMap = (Map<String, Object>) response.data.loyaltyInfo;
        Map<String, Object> loyaltyDetailMap = (Map<String, Object>) loyaltyInfoMap.get("loyaltyDetail");
        List<Map<String, Object>> bodyList = (List<Map<String, Object>>) loyaltyDetailMap.get("body");
        String actualDescription = (String) bodyList.get(0).get("description");
        Assertion.assertThat("Verify loyalty structural discount description is exact", actualDescription, anyOf(is("Discount " + discountStr), is("Diskon " + discountStr)));
    }

    public void verifyCampaignDiscountBlockedByBlackout(SelectRoomResponse response) {
        SelectRoomResponse.RateInfo rateInfo = response.data.rooms.get(0).ratePlans.get(0).rateInfo;
        Map<String, Object> loyaltyMap = (Map<String, Object>) rateInfo.loyalty;
        Map<String, Object> detailsMap = (Map<String, Object>) loyaltyMap.get("details");
        Map<String, Object> tierMap = (Map<String, Object>) loyaltyMap.get("tier");
        Assertion.assertThat("Verify tier details object exists in API response", tierMap, notNullValue());
        String actualApiTierValue = ((String) tierMap.get("value")).trim();
        Assertion.assertThat("Verify user tier status is classified as premium (Gold, Platinum, or Diamond)", actualApiTierValue, anyOf(is("Gold"), is("Platinum"), is("Diamond")));
        Assertion.assertThat("Verify active rate plan benefits are null.", detailsMap.get("benefits"), nullValue());
        Assertion.assertThat("Verify Rate Info summary is null.", detailsMap.get("summary"), nullValue());
        Assertion.assertThat("Verify loyaltyInfo is null,", response.data.loyaltyInfo, nullValue());
    }

    public void verifyEarlyBirdPromoApplied(SelectRoomResponse response) {
        List<SelectRoomResponse.Promo> promos = response.data.rooms.get(0).promos;
        Assertion.assertThat("Verify room promos list is present", promos, notNullValue());
        String allPromos = describePromos(promos);
        SelectRoomResponse.Promo earlyBirdPromo = promos.stream().filter(promo -> "EARLY_BIRD".equals(promo.type)).findFirst().orElse(null);
        Assertion.assertThat("Verify a promo of type EARLY_BIRD is present in the room promos. Actual promos: " + allPromos, earlyBirdPromo, notNullValue());
        Assertion.assertThat("Verify the Early Bird promo text. Actual promos: " + allPromos, earlyBirdPromo.text, is("Early Bird Offer"));
        Assertion.assertThat("Verify the Early Bird promo type. Actual promos: " + allPromos, earlyBirdPromo.type, is("EARLY_BIRD"));
    }

    // Used whenever the Early Bird promo is expected to be absent - a blackout date, the promo deactivated, or the promo deleted.
    public void verifyEarlyBirdPromoNotApplied(SelectRoomResponse response) {
        List<SelectRoomResponse.Promo> promos = response.data.rooms.get(0).promos;
        String allPromos = describePromos(promos);
        String actualTypes = promos == null ? "" : promos.stream().map(promo -> promo.type).collect(Collectors.joining(", "));
        String actualTexts = promos == null ? "" : promos.stream().map(promo -> promo.text).collect(Collectors.joining(", "));
        Assertion.assertThat("Verify no promo with type EARLY_BIRD is present. Actual promos: " + allPromos, actualTypes, not(containsString("EARLY_BIRD")));
        Assertion.assertThat("Verify no promo with text 'Early Bird Offer' is present. Actual promos: " + allPromos, actualTexts, not(containsString("Early Bird Offer")));
    }

    private String describePromos(List<SelectRoomResponse.Promo> promos) {
        if (promos == null || promos.isEmpty()) {
            return "[]";
        }
        return promos.stream().map(promo -> "{text=" + promo.text + ", type=" + promo.type + "}").collect(Collectors.joining(", "));
    }

    public void clickManageBenefitAndVerifyFormOpens() {
        WebElement manageButton = driverTools.waitForElementToAppear(MANAGE_BENEFIT_BUTTON, 10).orElseThrow(() -> new NoSuchElementException("Manage benefit button not found on the summary page"));
        manageButton.click();
    }

    public void verifyBenefitEditPageLoaded(String expectedValue) {
        WebElement setBenefitsHeader = driverTools.waitForElementToAppear(SET_BENEFITS_HEADER, 10).orElseThrow(() -> new NoSuchElementException("'Set Benefits' / 'Atur Benefit' header did not load"));
        Assertion.assertThat("Verify 'Set Benefits' header is displayed", setBenefitsHeader.isDisplayed(), is(true));
        WebElement discountField = driverTools.waitForElementToAppear(DISCOUNT_INPUT, 5).orElseThrow(() -> new NoSuchElementException("Discount input field not found on the edit page"));
        String actualValue = discountField.getAttribute("value");
        Assertion.assertThat("Verify locked discount field shows correct value", actualValue, is(expectedValue));
    }

    public void enableAndSelectAllExtraBenefits() {
        WebElement yesRadio = driverTools.waitForElementToAppear(EXTRA_BENEFITS_YES_RADIO, 5).orElseThrow(
                () -> new NoSuchElementException("Extra Benefits 'Yes' radio button not found")
        );
        driverTools.scrollElementToCenter(yesRadio);
        yesRadio.click();
        driverTools.waitForElementToAppear(EXTRA_BENEFITS_CHECKBOX_LABELS, 5).orElseThrow(
                () -> new NoSuchElementException("Extra benefit checkboxes did not appear after clicking 'Yes'")
        );
        List<WebElement> benefitLabels = driverTools.elements(EXTRA_BENEFITS_CHECKBOX_LABELS).orElseThrow(() -> new NoSuchElementException("Extra benefit labels not found"));
        Assertion.assertThat("Verify additional benefit options are displayed", benefitLabels.isEmpty(), is(false));
        System.out.println("Selecting all extra benefit options...");
        for (WebElement label : benefitLabels) {
            WebElement checkboxInput = label.findElement(By.xpath(".//input[@type='checkbox']"));
            if (!checkboxInput.isSelected()) {
                driverTools.scrollElementToCenter(label);
                label.click();
            }
        }
    }

    public void selectSpecificRoomsAndVerifyRatePlans() {
        WebElement specificRadio = driverTools.waitForElementToAppear(SPECIFIC_ROOM_RATE_RADIO, 5).orElseThrow(() -> new NoSuchElementException("Specific room/rate plan radio button not found"));
        driverTools.scrollElementToCenter(specificRadio);
        specificRadio.click();
        WebElement selectRoomsInput = driverTools.waitForElementToAppear(SELECT_ROOMS_INPUT, 5).orElseThrow(() -> new NoSuchElementException("'Select rooms' input field not found"));
        selectRoomsInput.click();
        driverTools.waitForElementToAppear(POPOVER_ROOM_CHECKBOXES, 5);
        List<WebElement> roomCheckboxes = driverTools.elements(POPOVER_ROOM_CHECKBOXES).orElseThrow();
        Assertion.assertThat("Verify room options exist. Room list size: "+roomCheckboxes.size(), !roomCheckboxes.isEmpty(), is(true));
        for (WebElement checkbox : roomCheckboxes) {
            Assertion.assertThat("Verify room checkbox is enabled and selectable", checkbox.isEnabled(), is(true));
        }
        WebElement selectAllBtn = driverTools.waitForElementToAppear(SELECT_ALL_ROOMS_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("'Select all' button not found inside room dropdown"));
        selectAllBtn.click();

        WebElement popoverDoneBtn = driverTools.waitForElementToAppear(POPOVER_DONE_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("'Done' button not found inside room dropdown"));
        popoverDoneBtn.click();
        driverTools.waitForElementToAppear(RATE_PLAN_CHECKBOXES, 5);
        List<WebElement> ratePlanCheckboxes = driverTools.elements(RATE_PLAN_CHECKBOXES).orElseThrow();
        Assertion.assertThat("Verify rate plans are displayed for the selected rooms", !ratePlanCheckboxes.isEmpty(), is(true));
        for (WebElement ratePlanCheckbox : ratePlanCheckboxes) {
            boolean isChecked = ratePlanCheckbox.isSelected() || "true".equals(ratePlanCheckbox.getAttribute("checked"));
            Assertion.assertThat("Verify rate plan is checked by default", isChecked, is(true));
        }
        WebElement mainModalDoneBtn = driverTools.waitForElementToAppear(MAIN_MODAL_DONE_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("Main modal 'Done' submit button not found"));
        mainModalDoneBtn.click();
    }

    public void verifyBlackoutDatesOnUI(String expectedStartDate, String expectedEndDate) {
        if(!driverTools.isElementDisplayed(START_DATE_INPUT)){
            clickAddBlackoutDates();
        }
        WebElement startInput = driverTools.waitForElementToAppear(START_DATE_INPUT, 5).orElseThrow(() -> new java.util.NoSuchElementException("Blackout start date input field not found"));
        driverTools.scrollElementToCenter(startInput);
        String actualStartDate = startInput.getAttribute("value");
        Assertion.assertThat("Verify start date matches the previously selected date", actualStartDate, is(expectedStartDate));
        WebElement endInput = driverTools.waitForElementToAppear(END_DATE_INPUT, 5).orElseThrow(() -> new java.util.NoSuchElementException("Blackout end date input field not found"));
        String actualEndDate = endInput.getAttribute("value");
        Assertion.assertThat("Verify end date matches the previously selected date", actualEndDate, is(expectedEndDate));
    }

    public void submitAndConfirmChanges() {
        WebElement submitBtn = driverTools.waitForElementToAppear(EDIT_SUBMIT_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("Main form 'Submit' button not found"));
        driverTools.scrollElementToCenter(submitBtn);
        submitBtn.click();
        WebElement yesContinueBtn = driverTools.waitForElementToAppear(YES_CONTINUE_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("'Yes, continue' button not found in the modal"));
        yesContinueBtn.click();
    }

    public String getSelectedRoomSummaryText() {
        WebElement roomSummary = driverTools.waitForElementToAppear(SELECTED_ROOM_SUMMARY, 5).orElseThrow(
                () -> new NoSuchElementException("Selected room summary text not found")
        );
        return roomSummary.getText().trim();
    }

    public List<String> getSelectedExtraBenefitsTexts() {
        List<String> selectedBenefits = new ArrayList<>();
        List<WebElement> labels = driverTools.elements(EXTRA_BENEFIT_CHECKBOXES).orElseThrow();
        for(WebElement checkbox : labels) {
            boolean isChecked = checkbox.isSelected() || "true".equals(checkbox.getAttribute("checked")) || checkbox.getAttribute("checked") != null;

            if (isChecked) {
                WebElement textSpan = checkbox.findElement(By.xpath("./ancestor::label//span[contains(@class, 'Check_children')]/span"));
                String rawText = textSpan.getText();
                String cleanText = rawText.replaceAll("\\s*\\(.*\\)", "").trim();
                selectedBenefits.add(cleanText);
            }
        }
        return selectedBenefits;
    }

    public void verifySubmittedRequestDetailsDynamic(String expectedRoomSummary, List<String> expectedBenefits) {
        WebElement roomsCell = driverTools.waitForElementToAppear(NEW_ROOMS_RATE_PLANS_VALUE, 5).orElseThrow(() -> new NoSuchElementException("Rooms & Rate Plans 'New Value' cell not found in modal"));
        String actualRoomsText = roomsCell.getText();
        Assertion.assertThat("Verify the specific room selected is listed", actualRoomsText, containsString(expectedRoomSummary));

        WebElement benefitsCell = driverTools.waitForElementToAppear(NEW_EXTRA_BENEFITS_VALUE, 5).orElseThrow(
                () -> new NoSuchElementException("Extra benefits 'New Value' cell not found in modal")
        );
        String actualBenefitsText = benefitsCell.getText();
        Assertion.assertThat("Verify extra benefits is set to Yes/Ya", actualBenefitsText, anyOf(containsString("Yes"), containsString("Ya")));
        for (String expectedBenefit : expectedBenefits) {
            Assertion.assertThat("Verify benefit '" + expectedBenefit + "' is listed in the modal", actualBenefitsText, containsString(expectedBenefit));
        }
    }

    public void verifyAndClickSeeRequest() {
        WebElement reviewBanner = driverTools.waitForElementToAppear(REVIEW_BANNER_TEXT, 10).orElseThrow(() -> new NoSuchElementException("Review banner indicating submitted changes was not found on the page"));
        Assertion.assertThat("Verify the review banner is displayed", reviewBanner.isDisplayed(), is(true));
        WebElement seeRequestBtn = driverTools.waitForElementToAppear(SEE_REQUEST_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("'See request' button not found"));
        driverTools.scrollElementToCenter(seeRequestBtn);
        seeRequestBtn.click();
    }

    public void clickApproveButton() {
        WebElement approveBtn = driverTools.waitForElementToAppear(APPROVE_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("'Approve' button not found on the page"));
        driverTools.scrollElementToCenter(approveBtn);
        approveBtn.click();
    }

    public void verifyApprovedSummaryDetails(String expectedDiscount, String expectedRoomSummary, List<String> expectedBenefits, String expectedStartDate, String expectedEndDate) {
        WebElement discountCell = driverTools.waitForElementToAppear(APPROVED_DISCOUNT_VALUE, 10).orElseThrow(() -> new NoSuchElementException("Approved Discount cell not found"));
        String discountStr = expectedDiscount.contains("%") ? expectedDiscount : expectedDiscount + "%";
        Assertion.assertThat("Verify final discount value", discountCell.getText(), containsString(discountStr));
        WebElement roomsCell = driverTools.waitForElementToAppear(APPROVED_ROOMS_VALUE, 5).orElseThrow(() -> new NoSuchElementException("Approved Rooms cell not found"));
        String actualRoomsText = roomsCell.getText();
        Assertion.assertThat("Verify approved room configuration reflects specific combinations", actualRoomsText, anyOf(containsString("Specific room and rate plan"), containsString("Kombinasi kamar dan rate plan")));
        Assertion.assertThat("Verify the specific room selected is listed in approved summary", actualRoomsText, containsString(expectedRoomSummary));

        WebElement benefitsCell = driverTools.waitForElementToAppear(APPROVED_EXTRA_BENEFITS_VALUE, 5).orElseThrow(() -> new NoSuchElementException("Approved Extra Benefits cell not found"));
        String actualBenefitsText = benefitsCell.getText();
        Assertion.assertThat("Verify approved extra benefits is set to Yes/Ya", actualBenefitsText, anyOf(containsString("Yes"), containsString("Ya")));

        for (String expectedBenefit : expectedBenefits) {
            Assertion.assertThat("Verify benefit '" + expectedBenefit + "' is listed in approved summary", actualBenefitsText, containsString(expectedBenefit));
        }

        String summaryStartPattern = CoreFunctions.convertDateFormat(expectedStartDate, "EEE, dd MMM yyyy", "d MMMM yyyy");
        String summaryEndPattern = CoreFunctions.convertDateFormat(expectedEndDate, "EEE, dd MMM yyyy", "d MMMM yyyy");
        String actualBlackoutDates = driverTools.waitForElementToAppear(SUMMARY_BLACKOUT_DATES, 5).orElseThrow().getText().trim();
        Assertion.assertThat("Verify Blackout Dates contain expected start and end dates. Actual UI text: " + actualBlackoutDates, actualBlackoutDates, allOf(containsString(summaryStartPattern), containsString(summaryEndPattern)));
    }

    public void verifyCampaignAndExtraBenefitsForNonBlackoutDates(SelectRoomResponse response, String expectedDiscount) {
        String discountStr = expectedDiscount.contains("%") ? expectedDiscount : expectedDiscount + "%";
        Assertion.assertThat("Verify loyaltyInfo block is available", response.data.loyaltyInfo, notNullValue());

        SelectRoomResponse.RateInfo rateInfo = response.data.rooms.get(0).ratePlans.get(0).rateInfo;
        Assertion.assertThat("Verify rateInfo loyalty block is available", rateInfo.loyalty, notNullValue());
        Map<String, Object> loyaltyInfoMap = (Map<String, Object>) response.data.loyaltyInfo;
        Map<String, Object> loyaltyDetailMap = (Map<String, Object>) loyaltyInfoMap.get("loyaltyDetail");
        List<Map<String, Object>> bodyList = (List<Map<String, Object>>) loyaltyDetailMap.get("body");
        String loyaltyInfoDescription = (String) bodyList.get(0).get("description");
        Assertion.assertThat("Verify loyaltyInfo shows the discount as " + discountStr, loyaltyInfoDescription, anyOf(is("Discount " + discountStr), is("Diskon " + discountStr)));

        Map<String, Object> rateLoyaltyMap = (Map<String, Object>) rateInfo.loyalty;
        Map<String, Object> detailsMap = (Map<String, Object>) rateLoyaltyMap.get("details");
        String rateInfoSummary = (String) detailsMap.get("summary");
        Assertion.assertThat("Verify rateInfo summary shows the discount as " + discountStr, rateInfoSummary, anyOf(containsString("Discount " + discountStr), containsString("Diskon " + discountStr)));
        Map<String, Object> tierMap = (Map<String, Object>) rateLoyaltyMap.get("tier");
        Assertion.assertThat("Verify tier details object exists in API response", tierMap, notNullValue());
        String actualApiTierValue = ((String) tierMap.get("value")).trim();
        Assertion.assertThat("Verify user tier status is classified as premium (Gold, Platinum, or Diamond)", actualApiTierValue, anyOf(is("Gold"), is("Platinum"), is("Diamond")));
        List<Map<String, Object>> benefitsList = (List<Map<String, Object>>) detailsMap.get("benefits");
        List<String> actualBenefitCodes = new ArrayList<>();

        for (Map<String, Object> benefit : benefitsList) {
            actualBenefitCodes.add((String) benefit.get("code"));
        }
        Assertion.assertThat("Verify FREE_BREAKFAST extra benefit is present", actualBenefitCodes, hasItem("FREE_BREAKFAST"));
        Assertion.assertThat("Verify FREE_CLUB_LOUNGE_ACCESS extra benefit is present", actualBenefitCodes, hasItem("FREE_CLUB_LOUNGE_ACCESS"));
        Assertion.assertThat("Verify FREE_EARLY_CHECK_IN extra benefit is present", actualBenefitCodes, hasItem("FREE_EARLY_CHECK_IN"));
        Assertion.assertThat("Verify FREE_LATE_CHECK_OUT extra benefit is present", actualBenefitCodes, hasItem("FREE_LATE_CHECK_OUT"));
    }
}
