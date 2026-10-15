package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AddNewPromoAndCampaignPage <T extends WebDriver> extends BasePage<T> {
    public AddNewPromoAndCampaignPage(Session<T> session) {
        super(session);
    }

    private static final Locator ADD_NEW_PROMO_AND_CAMPAIGN_PAGE = new Locator("ADD_NEW_PROMO_AND_CAMPAIGN_PAGE", new By.ByXPath("//div[@id='promo-type']"));
    private static final Locator PRICE_SIMULATION_MODAL_CLOSE_BUTTON = new Locator("PRICE_SIMULATION_MODAL_CLOSE_BUTTON", new By.ByXPath("//div[@class=\"price-simulation-info\"]//button"));
    private static final Locator CANCELLATION_POLICY_COACHMARK_CLOSE_BUTTON = new Locator("CANCELLATION_POLICY_COACHMARK_CLOSE_BUTTON", new By.ByXPath("//div[@class=\"coachmark-content\"]//button"));
    private static final Locator BASIC_DEAL_CARD = new Locator("BASIC_DEAL_CARD", new By.ByXPath("//a[@href='/v3/promotions-and-campaigns/add/promotion/create/basic-deal']"));
    private static final Locator BONUS_NIGHT_CARD = new Locator("BONUS_NIGHT_CARD",new By.ByXPath("//a[@href='/v3/promotions-and-campaigns/add/promotion/create/stay-pay']"));
    private static final Locator BASIC_DEAL_PAGE = new Locator("BASIC_DEAL_PAGE", new By.ByXPath("//div[@class='the-create-basic-deals promotion-form']"));
    private static final Locator BONUS_NIGHT_PAGE = new Locator("BONUS_NIGHT_PAGE",new By.ByXPath("//div[@id='form-stay-pay' and @class='the-row promotion-form']"));
    private static final Locator PROMO_NAME_INPUT = new Locator("PROMO_NAME_INPUT", new By.ByXPath("//input[@name='promoName']"));
    private static final Locator SELECT_ALL_LOGIN_PLATFORM = new Locator("SELECT_ALL_LOGIN_PLATFORM", new By.ByXPath("(//button[contains(@class, 'select-all')])[1]"));
    private static final Locator SELECT_ALL_NON_LOGIN_PLATFORM = new Locator("SELECT_ALL_NON_LOGIN_PLATFORM", new By.ByXPath("(//button[contains(@class, 'select-all')])[2]"));
    private static final Locator START_DATE_STAY_PERIOD_INPUT = new Locator("START_DATE_STAY_PERIOD_INPUT", new By.ByXPath("//input[@name='stayPeriod_startDate']"));
    private static final Locator END_DATE_STAY_PERIOD_INPUT = new Locator("END_DATE_STAY_PERIOD_INPUT", new By.ByXPath("//input[@name='stayPeriod_endDate']"));
    private static final Locator ACTIVE_DATEPICKER_NEXT_MONTH_BUTTON = new Locator("ACTIVE_DATEPICKER_NEXT_MONTH_BUTTON", new By.ByXPath("//div[contains(@class,'popover-slidedown-enter-active')]//span[contains(@class,'the-navbar-next')]"));
    private static final Locator BOOK_ANYTIME_RADIO_BUTTON = new Locator("BOOK_ANYTIME_RADIO_BUTTON", new By.ByXPath("(//input[@name='bookWithPromo'])[1]/parent::div/parent::div"));
    private static final Locator NO_PROMO_ACCUMULATE_RADIO_BUTTON = new Locator("NO_PROMO_ACCUMULATE_RADIO_BUTTON", new By.ByXPath("(//input[@name='accumulative'])[1]/parent::div/parent::div"));
    private static final Locator DISCOUNT_PERCENTAGE_INPUT = new Locator("DISCOUNT_PERCENTAGE_INPUT", new By.ByXPath("//input[@name='discountValue_0']"));
    private static final Locator MIN_LENGTH_STAY_INPUT = new Locator("MIN_LENGTH_STAY_INPUT", new By.ByXPath("//input[@name='minStay']"));
    private static final Locator FREE_STAY_INPUT = new Locator("FREE_STAY_INPUT", new By.ByXPath("//input[@name='freeNight']"));
    private static final Locator REVIEW_PROMO_BUTTON = new Locator("REVIEW_PROMO_BUTTON", new By.ByXPath("//button[text()='Review Promo']"));
    private static final Locator CREATE_PROMO_BUTTON = new Locator("CREATE_PROMO_BUTTON", new By.ByXPath("(//div[@class='promotion-simulation-footer']//button)[2]"));
    private static final Locator COPY_PROMO_CONFIG_BUTTON = new Locator("COPY_PROMO_CONFIG_BUTTON", new By.ByXPath("//div[@class='the-dropdown v4']"));
    private static final Locator PROMOTIONS_AND_CAMPAIGNS_PAGE_TITLE = new Locator("PROMOTIONS_AND_CAMPAIGNS_PAGE_TITLE", new By.ByXPath("//span[normalize-space()='Promotions & Campaigns']"));
    private static final Locator EARLY_BIRD_OFFER_CARD = new Locator("EARLY_BIRD_OFFER_CARD", new By.ByXPath("//a[contains(@href,'/promotions-and-campaigns/regular/early-bird/create')]"));

    public Locator getPromoByName(String name){
        return new Locator("PROMO", new By.ByXPath(String.format("//td[text()='%s']", name)));
    }

    public static Locator getActiveDatepickerDateByIndex(int index) {
        return new Locator("ACTIVE_DATEPICKER_DATE_LABEL", new By.ByXPath(String.format("(//div[contains(@class,'popover-slidedown-enter-active')]//div[contains(@class,'DayPicker-Day')][@aria-disabled=\"false\"]//div[@class=\"day-mod-body\"])[%d]", index)));
    }
    
    public boolean isPromoCreated(String name) {
        return driverTools.waitForElementToAppear(getPromoByName(name)).isPresent();
    }

    public boolean isAddNewPromoAndCampaignPageLoaded() {
        driverTools.waitForElementToAppear(ADD_NEW_PROMO_AND_CAMPAIGN_PAGE);
        return driverTools.isElementDisplayed(ADD_NEW_PROMO_AND_CAMPAIGN_PAGE);
    }

    public void dismissPriceSimulationModal() {
        try {
            WebElement button = driverTools.waitForElementToBeClickable(PRICE_SIMULATION_MODAL_CLOSE_BUTTON).orElseThrow();
            driverTools.click(button);
        } catch (Exception e) {
            logger.log("Price simulation modal is not appeared");
        }
    }

    public void dismissCancellationPolicyCoachmark() {
        try {
            WebElement button = driverTools.waitForElementToBeClickable(CANCELLATION_POLICY_COACHMARK_CLOSE_BUTTON).orElseThrow();
            driverTools.click(button);
        } catch (Exception e) {
            logger.log("Cancellation policy coachmark is not appeared");
        }
    }
    
    public void goToCreateBasicDealPage(){
        driverTools.waitForElementToAppear(BASIC_DEAL_CARD);
        driverTools.click(BASIC_DEAL_CARD);
    }

    public void goToCreateBonusNightPage(){
        driverTools.waitForElementToAppear(BONUS_NIGHT_CARD);
        driverTools.click(BONUS_NIGHT_CARD);
    }
    
    public boolean isCreateBasicDealPageLoaded(){
        driverTools.waitForElementToAppear(BASIC_DEAL_PAGE);
        return driverTools.isElementDisplayed(BASIC_DEAL_PAGE);
    }

    public boolean isCreateBonusNightPageLoaded(){
        driverTools.waitForElementToAppear(BONUS_NIGHT_PAGE);
        return driverTools.isElementDisplayed(BONUS_NIGHT_PAGE);
    }

    public void fillOutPromoNameInput(String name){
        WebElement input = driverTools.waitForElementToAppear(PROMO_NAME_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, name);
    }

    public void selectDateByDateTypeAndIndex(String dateType, int index) {
        Locator locator = switch (dateType) {
            case "STAY_PERIOD_START" -> START_DATE_STAY_PERIOD_INPUT;
            case "STAY_PERIOD_END" -> END_DATE_STAY_PERIOD_INPUT;
            default -> null;
        };
        WebElement input = driverTools.waitForElementToAppear(locator).orElseThrow();
        driverTools.click(input);
        try {
            WebElement date = driverTools.waitForElementToBeClickable(getActiveDatepickerDateByIndex(index)).orElseThrow();
            driverTools.click(date);
        } catch (Exception e) {
            WebElement next = driverTools.waitForElementToBeClickable(ACTIVE_DATEPICKER_NEXT_MONTH_BUTTON).orElseThrow();
            driverTools.click(next);
            WebElement date = driverTools.waitForElementToBeClickable(getActiveDatepickerDateByIndex(index-1)).orElseThrow();
            driverTools.click(date);
        }
    }

    public void clickBookAnytimeRadioButton(){
        driverTools.waitForElementToAppear(BOOK_ANYTIME_RADIO_BUTTON);
        driverTools.click(BOOK_ANYTIME_RADIO_BUTTON);
    }

    public void clickSelectAllLoginPlatformButton(){
        driverTools.waitForElementToAppear(SELECT_ALL_LOGIN_PLATFORM);
        driverTools.click(SELECT_ALL_LOGIN_PLATFORM);
    }

    public void clickSelectAllNonLoginPlatformButton(){
        driverTools.waitForElementToAppear(SELECT_ALL_NON_LOGIN_PLATFORM);
        driverTools.click(SELECT_ALL_NON_LOGIN_PLATFORM);
    }
    
    public void fillOutDiscountPercentageInput(String discount){
        WebElement input = driverTools.waitForElementToAppear(DISCOUNT_PERCENTAGE_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, discount);
    }

    public void fillOutMinLengthStay(String nights){
        WebElement input = driverTools.waitForElementToAppear(MIN_LENGTH_STAY_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input,nights);
    }

    public void fillOutFreeStay(String nights){
        WebElement input = driverTools.waitForElementToAppear(FREE_STAY_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input,nights);
    }

    public void clickNoPromoAccumulateRadioButton(){
        driverTools.waitForElementToAppear(NO_PROMO_ACCUMULATE_RADIO_BUTTON);
        driverTools.click(NO_PROMO_ACCUMULATE_RADIO_BUTTON);
    }
    
    public void clickSelectAllRoomAndRatePlan(){
        List<WebElement> elements = driver.findElements(By.xpath("//div[@name='roomAndRatePlan']//button"));
        for (WebElement element : elements){
            driverTools.waitForElementToAppear(element);
            driverTools.click(element);
        }
    }
    
    public void clickReviewPromoButton(){
        driverTools.waitForElementToAppear(REVIEW_PROMO_BUTTON);
        driverTools.click(REVIEW_PROMO_BUTTON);
    }

    public void clickCreatePromoButton(){
        driverTools.waitForElementToAppear(CREATE_PROMO_BUTTON);
        driverTools.click(CREATE_PROMO_BUTTON);
    }

    public boolean isPromotionsAndCampaignsPageLoaded() {
        driverTools.waitForElementToAppear(PROMOTIONS_AND_CAMPAIGNS_PAGE_TITLE);
        return driverTools.isElementDisplayed(PROMOTIONS_AND_CAMPAIGNS_PAGE_TITLE);
    }

    public void clickEarlyBirdOfferCard() {
        WebElement card = driverTools.waitForElementToBeClickable(EARLY_BIRD_OFFER_CARD).orElseThrow();
        driverTools.scrollElementToCenter(card);
        driverTools.click(card);
    }

}
