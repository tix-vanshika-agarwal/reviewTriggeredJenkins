package com.tiket.web.dashboard.page.platform.eiffel.loyalty;

import com.tiket.apiPage.LoyaltyApi;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoyaltyPointsPage<T extends WebDriver> extends BasePage<T> {

    private final String ADMINCARE = "/admin/care/loyalty";
    private static final Locator LOYALTY = new Locator("LOYALTY", new By.ByXPath("//span[text()='Loyalty']"));
    private static final Locator BLIBLI_TIKET_REWARDS = new Locator("BLIBLI_TIKET_REWARDS", new By.ByXPath("//span[text()='Blibli Tiket Rewards']"));
    private static final Locator EMAIL_DROPDOWN_SELECTOR = new Locator("EMAIL_DROPDOWN_SELECTOR", new By.ByXPath("//input[@id='email']"));
    private static final Locator EMAIL_SELECTOR = new Locator("EMAIL_SELECTOR", new By.ByXPath("//p[text()='Email']"));
    private static final Locator PHONE_NUMBER_SELECTOR = new Locator("PHONE_NUMBER_SELECTOR", new By.ByXPath("//p[text()='Phone Number']"));
    private static final Locator EMAIL_TEXT_BOX = new Locator("EMAIL_TEXT_BOX", new By.ByXPath("//input[@placeHolder='Search by email']"));
    private static final Locator PHONE_NUMBER_TEXT_BOX = new Locator("PHONE_NUMBER_TEXT_BOX", new By.ByXPath("//input[@placeHolder='Search by phone number']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[contains(@class,'BlibliTiketPointFilter_search_button')]"));
    private static final Locator EMAIL_ID = new Locator("EMAIL_ID", new By.ByXPath("(//span[contains(@class,'AccountInformation_account_info_text')])[1]"));
    private static final Locator PHONE_NUMBER = new Locator("PHONE_NUMBER", new By.ByXPath("(//span[contains(@class,'AccountInformation_account_info_text')])[2]"));
    private static final Locator TIKET_POINTS =  new Locator("TIKET_POINTS", new By.ByXPath("(//p[contains(@class, 'RewardsDetail_point_text')])[1]"));
    private static final Locator WORTH =  new Locator("WORTH", new By.ByXPath("(//p[contains(@class, 'RewardsDetail_point_text')])[2]"));
    private static final Locator STATUS_POINTS =  new Locator("STATUS_POINTS", new By.ByXPath("(//p[contains(@class, 'RewardsDetail_point_text')])[3]"));

    public void goToUserManagementPage(String userName, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(userName, password);
        memberLoginPage.goToPage(ADMINCARE);
    }

    public LoyaltyPointsPage(Session<T> session) {
        super(session);
    }

    public void clickLoyaltyIcon(){
        driverTools.waitForElementToAppear(LOYALTY, 3);
        if(!driverTools.isElementDisplayed(LOYALTY)) {
            MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
            memberLoginPage.goToPage(ADMINCARE);
        }
        driverTools.waitForElementToAppear(LOYALTY, 3);
        driverTools.click(LOYALTY);
    }

    public void bliblitiketrewardsDashboard(){
        WebElement bliblitiketrewards = driverTools.waitForElementToAppear(BLIBLI_TIKET_REWARDS).orElseThrow();
        driverTools.click(bliblitiketrewards);
    }

    public void selectDropDown(){
        WebElement emailDropDownSelector = driverTools.waitForElementToAppear(EMAIL_DROPDOWN_SELECTOR).orElseThrow();
        driverTools.click(emailDropDownSelector);
    }

    public void selectEmail(){
        driverTools.click(EMAIL_SELECTOR);
    }

    public void selectPhoneNumber(){
        driverTools.click(PHONE_NUMBER_SELECTOR);
    }

    public void inputEmailId(String input) {
        driverTools.sendKeys(EMAIL_TEXT_BOX, input);
    }

    public void inputPhoneNumber(String input) {
        driverTools.sendKeys(PHONE_NUMBER_TEXT_BOX, input);
    }

    public void clickOnSearch(){
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean verifyEmailId(String input) {
        WebElement email_id = driverTools.waitForElementToAppear(EMAIL_ID,3).orElseThrow();
        return driverTools.getText(email_id).contains(input);
    }

    public boolean verifyPhoneNumber(String input) {
        WebElement email_id = driverTools.waitForElementToAppear(PHONE_NUMBER,3).orElseThrow();
        return driverTools.getText(email_id).contains(input);
    }

    public boolean checkTiketPoints() {
        driverTools.waitForElementToAppear(TIKET_POINTS);
        return driverTools.isElementDisplayed(TIKET_POINTS);
    }

    public boolean checkTiketPointsWorth() {
        WebElement worth = driverTools.waitForElementToAppear(WORTH,3).orElseThrow();
        WebElement tiket_points =  driverTools.waitForElementToAppear(TIKET_POINTS,3).orElseThrow();
        String tiketPoints =  driverTools.getText(tiket_points).replace(" Points","");
        String pointsWorth = driverTools.getText(worth).replace("IDR ","");
        return pointsWorth.equals(tiketPoints);
    }

    public boolean checkStatusPoints() {
        driverTools.waitForElementToAppear(STATUS_POINTS);
        return driverTools.isElementDisplayed(STATUS_POINTS);
    }

    public void injectPointApi(String targetAccountId, String points, String processType, String apiUsername) {
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        int maxRetries = 3;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                loyaltyApi.injectTiketPoint(
                        Integer.parseInt(targetAccountId),
                        Integer.parseInt(points),
                        processType,
                        1,
                        apiUsername);
                break;
            } catch (Exception | AssertionError e) {
                if (attempt == maxRetries)
                    throw new RuntimeException(e);
                logger.log("Inject attempt " + attempt + " failed: " + e.getMessage() + ". Retrying...");
            }
        }
    }
}

