package com.tiket.web.page.accommodation.homes.LandingPage;

import com.tiket.appium.PopupObserver;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public abstract class HomesLandingPageWeb<T extends WebDriver> extends WebPage<T> {

    private static final Locator SEARCH_FORM = new Locator("SEARCH_FORM", new By.ByXPath("//div[@data-testid='destination-input']"));
    private static final Locator SEARCH_FORM_INPUT = new Locator("SEARCH_FORM_INPUT", new By.ByXPath("//*[@data-testid='destination-search-box']"));
    private static final Locator SEARCH_SUBMIT_BUTTON = new Locator("SEARCH_SUBMIT_BUTTON", AppiumBy.xpath("//*[@data-testid='search-button-lp']"));
    private static final Locator GOT_IT_POPUP = new Locator("GOT_IT_POPUP", AppiumBy.xpath("//button[text()='Got it']"));
    private static final Locator GUEST_FORM = new Locator("GUEST_FORM", new By.ByXPath("//*[@data-testid='rooms-and-guests']"));
    private static final Locator DATE_FORM = new Locator("DATE_FORM", new By.ByXPath("//*[@data-testid='date-picker']"));
    private static final Locator NEAR_ME_BUTTON = new Locator("NEAR_ME_BUTTON", new By.ByXPath("//div[@data-testid='nearby-destination']"));
    private static final Locator DESTINATION_FORM_DRAWER = new Locator("DESTINATION_FORM_DRAWER", new By.ByXPath("//header[@data-testid='bottom-sheet-header']"));
    private static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", new By.ByXPath("//button[contains(@class, 'BottomSheet_close_button')]"));
    private static final Locator NEAR_ME=new Locator("NEAR_ME",new By.ByXPath("//span[@data-testid='destination-input-text']"));
    // --- Locators ---
    private static final Locator HERO_BANNER = new Locator("HERO_BANNER", AppiumBy.xpath("//*[@data-testid='nha-hero-banner-container']"));
    private static final Locator HERO_BANNER_TITLE = new Locator("HERO_BANNER_TITLE", AppiumBy.xpath("//h1[@data-testid='title']"));
    private static final Locator HERO_BANNER_TEXT = new Locator("HERO_BANNER_TEXT", AppiumBy.xpath("//span[contains(@class,'HeroBannerNHA_typing_list_item')]"));
    private static final Locator CLEAR_ALL_RECENT_LIST_BUTTON = new Locator("CLEAR_ALL", new By.ByXPath("//*[@data-testid=\"recent_list_item_clear\"]"));
    private static final Locator SEE_MORE =new Locator("SEE_MORE",new By.ByXPath("//span[contains(text(), 'Lihat Lebih Banyak') or contains(text(), 'See More')]")) ;
    private static final Locator FIRST_MODULE = new Locator("FIRST_MODULE",new By.ByXPath("//div[@data-testid='pagemodule-view-0']"));
    private static final Locator BACK_TO_TOP_BUTTON=new Locator("BACK_TO_TOP_BUTTON",new By.ByXPath("//span[contains(text(), 'Kembali ke Atas') or contains(text(), 'Back to Top')]"));
    private static final Locator FOOTER_PART = new Locator("FOOTER_PART",new By.ByXPath("//div[contains(@class,'Footer-module__copyright_logo')]"));

    private static Locator getPageModulePrimaryButtonById(String id) {
        return new Locator("PAGE_MODULE_PRIMARY_BUTTON", new By.ByXPath(String.format("//section[@id='%s']//a[contains(@class,'Button')]", id)));
    }
    private static Locator getPageModuleProductCardByIndex(int index) {
        return new Locator("PAGE_MODULE_PRODUCT_CARD", new By.ByXPath(String.format("(//*[@data-testid=\"accom-product-card\"])[%d]", index)));
    }

    private static Locator getPageModuleProductCardByName(String id, String name) {
        return new Locator("PAGE_MODULE_PRODUCT_CARD", new By.ByXPath(String.format("//section[@id='%s']//*[@data-testid=\"accom-product-card\"]//h2[text()='%s']", id, name)));
    }

    public abstract boolean isSearchHistoryDestinationLabelDisplayed(String expected, int index);
    public abstract boolean isSearchHistoryDateLabelDisplayed(String expected, int index);
    public abstract boolean isSearchHistoryGuestLabelDisplayed(String expected, int index);


    public HomesLandingPageWeb(Session<T> session) { super(session); }

    public static <T extends WebDriver> HomesLandingPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HomesLandingPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new HomesLandingPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public void clickSubmit (){
        driverTools.clickWithJavascriptExecutor(SEARCH_SUBMIT_BUTTON);
        handlePopup();
    }

    public void searchStayLocation (String location){
        driverTools.waitForElementToPresence(SEARCH_FORM, 5).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEARCH_FORM);
        driverTools.waitForElementToAppear(SEARCH_FORM_INPUT).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEARCH_FORM_INPUT);
        driverTools.sendKeys(SEARCH_FORM_INPUT, location);
        WebElement searchFormResult = driverTools.waitForElementToAppear(getSearchFormResultLocator(location)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(searchFormResult);
    }

    private Locator getSearchFormResultLocator(String location) {
        return new Locator("SEARCH_FORM_RESULT", new By.ByXPath("//*[@data-testid='item-name']//*[text()='"+location+"']"));
    }

    private void handlePopup() {
        PopupObserver.inject(driver, List.of(GOT_IT_POPUP));
    }

    // --- Hero Banner Methods ---
    public boolean isHeroBannerVisible() {
        return driverTools.waitForElementToPresence(HERO_BANNER).isPresent();
    }

    private static final Locator DONE_SET_ROOM_GUEST = new Locator("DONE_SET_ROOM_GUEST", AppiumBy.xpath("//*[contains(@class,'footer_button')]//*[contains(@class,'Button_variant_primary')]"));
    public boolean isSearchFormPresent(){
        return driverTools.isElementPresent(SEARCH_FORM);
    }

    public boolean isDateFormPresent(){
        return driverTools.isElementPresent(DATE_FORM);
    }

    public boolean isGuestFormPresent(){
        return driverTools.isElementPresent(GUEST_FORM);
    }

    public boolean isSubmitButtonPresent(){
        return driverTools.isElementPresent(SEARCH_SUBMIT_BUTTON);
    }

    public boolean isNearMeButtonPresent(){
        return driverTools.isElementPresent(NEAR_ME_BUTTON);
    }

    public boolean isDestinationDrawerPresent(){
        driverTools.clickWithJavascriptExecutor(SEARCH_FORM);
        return driverTools.isElementPresent(CLOSE_BUTTON);
    }

    public boolean isDatePickerDrawerPresent(){
        driverTools.waitForElementToAppear(DATE_FORM);
        driverTools.clickWithJavascriptExecutor(DATE_FORM);
        return driverTools.isElementPresent(CLOSE_BUTTON);
    }

    public boolean isGuestFormDrawerPresent(){
        driverTools.scrollElementToCenter(GUEST_FORM);
        driverTools.clickWithJavascriptExecutor(GUEST_FORM);
        return driverTools.isElementPresent(CLOSE_BUTTON);
    }

    public void isCloseBottomDrawer(){
        driverTools.waitForElementToPresence(CLOSE_BUTTON,10);
        driverTools.clickWithJavascriptExecutor(CLOSE_BUTTON);
    }

    public boolean isNearMePresent(){
        WebElement webElement=driverTools.waitForElementToAppear(NEAR_ME).orElseThrow();
        String nearMe=driverTools.getText(webElement);
        return nearMe.contains("Near") || nearMe.contains("Di dekat kamu");
    }

    public boolean isHeroBannerTitleVisible() {
        return driverTools.waitForElementToPresence(HERO_BANNER_TITLE).isPresent();
    }

    public boolean isHeroBannerTextVisible() {
        return driverTools.waitForElementToPresence(HERO_BANNER_TEXT).isPresent();
    }
    public void fillSearchDetails(String location, String checkIn, String checkOut, int adults, int children) {

        driverTools.waitForElementToAppear(SEARCH_FORM, 5);
        driverTools.clickWithJavascriptExecutor(SEARCH_FORM);
        driverTools.waitForElementToAppear(SEARCH_FORM_INPUT, 5);
        driverTools.sendKeys(SEARCH_FORM_INPUT, location);

        driverTools.waitForElementToPresence(getSearchFormResultLocator(location), 5)
                .ifPresent(driverTools::clickWithJavascriptExecutor);

        driverTools.waitForElementToAppear(DATE_FORM, 5);
        driverTools.clickWithJavascriptExecutor(DATE_FORM);

        driverTools.clickWithJavascriptExecutor(DATE_FORM);
        driverTools.clickWithJavascriptExecutor(DATE_FORM);

        driverTools.waitForElementToAppear(DONE_SET_ROOM_GUEST, 5);
        driverTools.clickWithJavascriptExecutor(DONE_SET_ROOM_GUEST);

        driverTools.waitForElementToAppear(GUEST_FORM, 5);
        driverTools.clickWithJavascriptExecutor(GUEST_FORM);

        driverTools.waitForElementToAppear(DONE_SET_ROOM_GUEST, 5);
        driverTools.clickWithJavascriptExecutor(DONE_SET_ROOM_GUEST);

        driverTools.waitForElementToAppear(SEARCH_SUBMIT_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(SEARCH_SUBMIT_BUTTON);
    }


    public void clickPageModulePrimaryButton(String id) {
        driverTools.waitForElementToAppear(getPageModulePrimaryButtonById(id));
        driverTools.click(getPageModulePrimaryButtonById(id));
    }

    public void clearAllRecentList() {
        driverTools.waitForElementToAppear(CLEAR_ALL_RECENT_LIST_BUTTON);
        driverTools.scrollIntoWebView(SEARCH_SUBMIT_BUTTON);
        driverTools.click(CLEAR_ALL_RECENT_LIST_BUTTON);
    }

    public void clickPageModuleProductCardByIndex(int index) {
        driverTools.waitForElementToAppear(getPageModuleProductCardByIndex(index));
        driverTools.click(getPageModuleProductCardByIndex(index));
    }

    public void clickPageModuleProductCardByName(String id, String name) {
        driverTools.waitForElementToAppear(getPageModuleProductCardByName(id, name));
        driverTools.click(getPageModuleProductCardByName(id, name));
    }

    public Boolean isLandingPageLoaded() {
        return driverTools.waitForElementToAppear(SEARCH_FORM).isPresent();
    }

    public Boolean checkSeeMoreButton() {
        return driverTools.waitForElementToAppear(SEE_MORE).isPresent();
    }

    public void clickSeeMoreButton() {
        driverTools.waitForElementToAppear(SEE_MORE).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEE_MORE);
    }

    public Boolean checkFirstModule() {
        return driverTools.waitForElementToAppear(FIRST_MODULE).isPresent();
    }

    public void scrollToBottom() {
        driverTools.scrollToElement(FOOTER_PART);
    }

    public Boolean checkBackToTopButton() {
        return driverTools.waitForElementToAppear(BACK_TO_TOP_BUTTON).isPresent();
    }

    public void clickBackToTopButton() {
        driverTools.clickWithJavascriptExecutor(BACK_TO_TOP_BUTTON);
    }

    public Boolean checkIfScrolledUp() {
        return driverTools.waitForElementToAppear(SEARCH_FORM).isPresent();
    }
}
