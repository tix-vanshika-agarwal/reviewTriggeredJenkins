package com.tiket.web.page.accommodation.hotels.ReviseBooking;

import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.home.HomePageWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HotelReviseBookingPage<T extends WebDriver> extends BasePage<T> {
    private final static Locator GUEST_CONTAINER = new Locator("GUEST_CONTAINER", new By.ByXPath("//div[contains(@data-testid,'guest-details-form-card')]"));
    private final static Locator GUEST_INPUT = new Locator("GUEST_INPUT", new By.ByXPath("//div[contains(@data-testid,'guest-details-form-card')]//input[@type='text']"));
    private final static Locator GUEST_SAVE_BUTTON = new Locator("GUEST_SAVE_BUTTON", new By.ByXPath("//div[contains(@class,'Guest')]//button"));
    private final static Locator AGREE_BUTTON = new Locator("AGREE_BUTTON", new By.ByXPath("//section//button[contains(@class,'primary')]"));
    private final static Locator SUCCESS_TEXT = new Locator("GUEST_SAVE_BUTTON", new By.ByXPath("(//div[contains(@class,'Feedback_description')]//span)[1]"));
    private final static Locator SEE_DETAILS = new Locator("GUEST_SAVE_BUTTON", new By.ByXPath("//div[contains(@class,'Feedback')]//button"));
    private final static Locator SPL_REQ_TEXTAREA = new Locator("GUEST_SAVE_BUTTON", new By.ByXPath("//textarea"));
    private final static Locator SAVE_TEXTAREA = new Locator("GUEST_SAVE_BUTTON", new By.ByXPath("//div[contains(@class,'save')]//button"));

    public HotelReviseBookingPage(Session<T> session) {
        super(session);
    }

    public Locator getLocator(String text) {
        return new Locator("SPECIAL_REQUEST_CHIP", new By.ByXPath("//button[contains(@class, 'chip')]//span[text()='" + text + "']"));
    }

    public String getSuccessText() {
        driverTools.waitForElementToAppear(SUCCESS_TEXT).orElseThrow();
        return driverTools.getText(SUCCESS_TEXT).orElseThrow();
    }

    private static Locator getOtpTextField(int index) {
        return new Locator("OTP_TEXT_FIELD" + index, new By.ByXPath("(//div[contains(@class,'OtpInput')]//input)[" + index + "]"));
    }

    public void inputOTPCode(String otp) {
        driverTools.waitForElementToAppear(getOtpTextField(1), 5);
        for (int i = 1; i <= otp.length(); i++) {
            Locator otpField = getOtpTextField(i);
            driverTools.waitForElementToBeClickable(otpField, 3).orElseThrow();
            driverTools.sendKeys(otpField, String.valueOf(otp.charAt(i - 1)));
        }
    }

    public void addOtherRequest(String request){
        driverTools.waitForElementToAppear(SPL_REQ_TEXTAREA).orElseThrow();
        driverTools.sendKeys(SPL_REQ_TEXTAREA,request);
        driverTools.click(SAVE_TEXTAREA);
    }

    public void updateSplRequest(String text){
        Locator splReqChips=getLocator(text);
        driverTools.waitForElementToAppear(splReqChips,5);
        driverTools.click(splReqChips);
    }

    public void editGuestName(String name){
        driverTools.waitForElementToAppear(GUEST_CONTAINER, 5);

        WebElement fullNameInput = driverTools.waitForElementToAppear(GUEST_INPUT).orElseThrow();
        if (!driverTools.getValue(fullNameInput).equals(name)) {
            driverTools.clearInput(fullNameInput);
            driverTools.sendKeys(fullNameInput, name);

            driverTools.waitForElementToAppear(GUEST_SAVE_BUTTON, 5);
            driverTools.clickWithJavascriptExecutor(GUEST_SAVE_BUTTON);
        }
    }

    public void editSpecialRequest(String request){
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        homePageWeb.continueWithoutApp(5);
        driverTools.waitForElementToAppear(SPL_REQ_TEXTAREA).orElseThrow();
        WebElement splRequestInput = driverTools.waitForElementToAppear(SPL_REQ_TEXTAREA).orElseThrow();
        String existingRequest;
        if (Configs.PLATFORM == Platform.IMWEB) {
            existingRequest = driverTools.getValue(splRequestInput);
        } else {
            existingRequest = driverTools.getText(splRequestInput);
        }
        if(!existingRequest.equals(request)){
            driverTools.clearInput(SPL_REQ_TEXTAREA);
            driverTools.sendKeys(SPL_REQ_TEXTAREA,request);
            driverTools.click(SAVE_TEXTAREA);
        }
    }

    public void clickAgreeButton(){
        driverTools.waitForElementToAppear(AGREE_BUTTON);
        driverTools.clickWithJavascriptExecutor(AGREE_BUTTON);
    }

    public void clickSeeDetails(){
        driverTools.waitForElementToAppear(SEE_DETAILS);
        driverTools.clickWithJavascriptExecutor(SEE_DETAILS);
        driverTools.waitForElementToDisappear(SEE_DETAILS);
    }

    public void clickEditBtnMO(String roomName){
        Locator editBtn=getRoomTypeCardIconByRoomName(roomName);
        driverTools.waitForElementToAppear(editBtn);
        try {
            WebElement element = driverTools.waitForElementToPresence(editBtn).orElseThrow();
            driverTools.clickSvgOrNonHtmlElements(element);
        }catch (Exception e){
            logger.log("Clicking edit button by coordinate due to "+e.getMessage());
            driverTools.clickElementByCoordinateDWeb(editBtn);
        }
    }

    private static Locator getRoomTypeCardIconByRoomName(String roomName) {
        return new Locator("ROOM_TYPE_CARD_ICON", new By.ByXPath(
                "//h4[contains(text(), '"+roomName+"')]/ancestor::div[contains(@class, 'RoomTypeCard_container')]//*[contains(@class,'RoomTypeCard_icon')]"
        ));
    }

}
