package com.tiket.web.smart.TTD;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.tiket.core.CoreFunctions.randomName;

public abstract class TTDSmartPageWeb<T extends WebDriver> extends WebPage<T> {
    private static final Locator CLOSE_BOTTOMSHEET = new Locator("CLOSE_BOTTOMSHEET", new By.ByXPath("//button[contains(@class,'BottomSheet_close')]"));
    private static final Locator SHARE_BUTTON = new Locator("SHARE_BUTTON", new By.ByXPath("//button[@aria-label='share-button']"));
    private static final Locator VISITOR_DETAILS_BOTTOMSHEET = new Locator("VISITOR_DETAILS_BOTTOMSHEET", new By.ByXPath("//div[contains(@class,'FormWrapper_title')]"));
    private static final Locator SALUTATION_RADIO_BUTTON = new Locator("SALUTATION_RADIO_BUTTON", new By.ByXPath("//*[contains(@class,'FormInput_radio_group')]/*[1]/*[contains(@class,'Radio_checkbox')]"));
    private static final Locator FULL_NAME_VISITOR_DETAILS_GUEST = new Locator("FULL_NAME_VISITOR_DETAILS_GUEST", new By.ByXPath("(//input[contains(@id,'full-name')])[2]"));
    private static final Locator FULL_NAME_VISITOR_DETAILS_LOGIN = new Locator("FULL_NAME_VISITOR_DETAILS_LOGIN", new By.ByXPath("//input[contains(@id,'full-name')]"));
    private static final Locator COUNTRY_BOTTOMSHEET_TITLE = new Locator("COUNTRY_BOTTOMSHEET_TITLE", new By.ByXPath("//h2[contains(@class,'BottomSheet_body_title')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[contains(.,'Save') or contains(.,'Simpan')]"));
    private static final Locator SELECT_TICKET_BUTTON = new Locator("SELECT_TICKET_BUTTON", AppiumBy.xpath("(//*[contains(@class,'PackageCollapse_card_list_wrapper')]//*[contains(@class,'Button_variant_primary') and not(contains(@class,'disabled'))])[1]"));


    public TTDSmartPageWeb(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> TTDSmartPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new TTDSmartPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new TTDSmartPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public void clickShareButton(){
        driverTools.click(SHARE_BUTTON);
        driverTools.waitForElementToAppear(CLOSE_BOTTOMSHEET);
    }

    public void closeBottomsheet(){
        driverTools.click(CLOSE_BOTTOMSHEET);
    }

    public void fillVisitorDetailsData(Boolean isLogin){
        driverTools.click(VISITOR_DETAILS_BOTTOMSHEET);
        driverTools.click(SALUTATION_RADIO_BUTTON);
        if(isLogin){
            driverTools.click(FULL_NAME_VISITOR_DETAILS_LOGIN);
            driverTools.sendKeys(FULL_NAME_VISITOR_DETAILS_LOGIN,randomName()+" "+randomName());
        }else{
            driverTools.click(FULL_NAME_VISITOR_DETAILS_GUEST);
            driverTools.sendKeys(FULL_NAME_VISITOR_DETAILS_GUEST,randomName()+" "+randomName());
        }

    }

    public void closeVisitorDetailsBottomsheet(){
        driverTools.click(SAVE_BUTTON);
    }

    public void chooseTicket(){
        driverTools.click(SELECT_TICKET_BUTTON);
    }
    public void waitForTicketCard(){
        driverTools.waitForElementToBeClickable(SELECT_TICKET_BUTTON);
    }
    public void waitForBottomsheetToBeDismissed(){
        driverTools.waitForElementToDisappear(COUNTRY_BOTTOMSHEET_TITLE);
    }
}
