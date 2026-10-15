package com.tiket.page.ttd.partnerPage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.page.ttd.landingpage.TTDLandingPageAndroid;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPageAndroid;
import com.tiket.pojo.ttd.TTDPartnerPageModules;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class TTDPartnerPageAndroid <T extends WebDriver> extends TTDPartnerPage<T> {
    public TTDPartnerPageAndroid(Session<T> session) {
        super(session);
    }

    private static final Locator PARTNER_PAGE_RATING = new Locator("PARTNER_PAGE_RATING", AppiumBy.id("view_rating"));
    private static final Locator PARTNER_PAGE_BOOKING = new Locator("PARTNER_PAGE_BOOKING", AppiumBy.id("view_booking"));
    private static final Locator PARTNER_PAGE_TOTAL_PRODUCT = new Locator("PARTNER_PAGE_TOTAL_PRODUCT", AppiumBy.id("view_product"));
    private static final Locator PARENT_GRID_ELEMENT = new Locator("PARENT_GRID_ELEMENT", AppiumBy.id("constraint_root"));
    private static final Locator RELOAD = new Locator("RELOAD", AppiumBy.id("button_reload"));
    private static final Locator CHIPS_GROUP = new Locator("CHIPS_GROUP", AppiumBy.id("chip_group"));
    private static final Locator RIBBON = new Locator("RIBBON", AppiumBy.id("ribbon"));
    private static final Locator BANNER = new Locator("RIBBON", AppiumBy.id("iv_banner"));
    private static final Locator FINAL_PRICE = new Locator("FINAL_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_final_price')][1]"));
    private static final Locator STRIKE_PRICE = new Locator("STRIKE_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_base_price')][1]"));
    private static final Locator SEE_ALL_BTN = new Locator("SEE_ALL_BTN", AppiumBy.id("btn_see_all"));
    private static final Locator PARTNER_PAGE_IMG_SEE_ALL_BTN = new Locator("PARTNER_PAGE_IMG_SEE_ALL_BTN", AppiumBy.id("iv_partner_image_gradiant"));
    private static final Locator CARD_VIEW = new Locator("CARD_VIEW", AppiumBy.id("tds_chips"));
    private static final Locator PARTNER_PAGE_SEE_ALL_BACK_BTN = new Locator("PARTNER_PAGE_SEE_ALL_BACK_BTN", AppiumBy.id("appbar_back_icon"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("(//*[contains(@resource-id,'tds_icon_navigation_toolbar') or contains(@resource-id,'appbar_back_icon')])[last()]"));
    private static final Locator PARTNER_PAGE_NAME = new Locator("PARTNER_PAGE_NAME", AppiumBy.id("tv_title"));
    private static final Locator PROMO_SECTION = new Locator("PROMO_SECTION", AppiumBy.id("user_promo_list_view"));

    private Locator getTitle(String type, String text){
        return new Locator("TITLE_"+type.toUpperCase(), AppiumBy.xpath("(//*[contains(@resource-id,'tv_title') and contains(@text,'"+text+"')])"));
    }

    private Locator getSubTitle(String type, String text){
        return new Locator("SUB_TITLE_"+type.toUpperCase(), AppiumBy.xpath("(//*[contains(@resource-id,'tv_subtitle') and contains(@text,'"+text+"')])"));
    }

    private Locator getCtaBtnOfBannerGrid(String type,String text){
        return new Locator(type.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'tds_btn') and contains(@text,'"+text+"')]"));
    }

    @Override
    public boolean verifyPartnerPage(String keyword){
        boolean isPartnerPageNameCorrect = driverTools.isElementDisplayed(getTitle("PARTNER_PAGE_NAME",keyword));
        logger.log("is partner page name correct : " + isPartnerPageNameCorrect);

        boolean isPartnerPageRatingDisplayed = driverTools.isElementDisplayed(PARTNER_PAGE_RATING);
        logger.log("is partner page rating displayed : " + isPartnerPageRatingDisplayed);
        boolean isPartnerPageBookingDisplayed = driverTools.isElementDisplayed(PARTNER_PAGE_BOOKING);
        logger.log("is partner page booking displayed : " + isPartnerPageBookingDisplayed);
        boolean isPartnerPageTotalProductDisplayed = driverTools.isElementDisplayed(PARTNER_PAGE_TOTAL_PRODUCT);
        logger.log("is partner page total product displayed : " + isPartnerPageTotalProductDisplayed);

        return isPartnerPageNameCorrect && isPartnerPageRatingDisplayed && isPartnerPageBookingDisplayed
                && isPartnerPageTotalProductDisplayed;
    }

    private Locator getBannerGridLocator(String gridTitle){
        return new Locator("BANNER_UNDER_GRID", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'"+gridTitle+"')]//following-sibling::*//*[contains(@resource-id,'iv_banner')]"));
    }

    private int getSizeOfBanner(String gridTitle){
        int index = gridTitle.indexOf(":");
        logger.log("Initial index: "+ index);
        int firstNumber = Integer.parseInt(String.valueOf(gridTitle.charAt(index-1)));
        int secondNumber = Integer.parseInt(String.valueOf(gridTitle.charAt(index+1)));
        return firstNumber * secondNumber;
    }

    private boolean checkBannerSize(String gridTitle){
        int numberOfGridBannerCalculated = getSizeOfBanner(gridTitle);
        logger.log("calculated number of banner : " + numberOfGridBannerCalculated);

        List<WebElement> bannersElement = driverTools.elements(getBannerGridLocator(gridTitle)).orElseThrow();
        int numberOfGridBannerActual = bannersElement.size();
        logger.log("actual number of banner : " + numberOfGridBannerActual);

        return numberOfGridBannerCalculated == numberOfGridBannerActual;
    }

    private boolean checkTitleAndSubTitleBanner(String gridTitle, TTDPartnerPageModules.Datum data){
        int numberOfGridBannerCalculated = getSizeOfBanner(gridTitle);
        logger.log("calculated number of banner : " + numberOfGridBannerCalculated);

        String bannerTitle = data.dynamicContents.get(0).chips.get(0).bannerContents.get(0).title;
        String bannerSubTitle = data.dynamicContents.get(0).chips.get(0).bannerContents.get(0).subtitle;
        driverTools.swipeUp();
        List<WebElement> bannerTitleList = driverTools.elements(getTitle("BANNER_UNDER_GRID",bannerTitle)).orElseThrow();
        List<WebElement> bannerSubTitleList = driverTools.elements(getSubTitle("BANNER_UNDER_GRID",bannerSubTitle)).orElseThrow();

        int numberOfTitleOfBanner = bannerTitleList.size();
        logger.log("number of title in banner grid : " + numberOfTitleOfBanner);
        int numberOfSubTitleOfBanner = bannerSubTitleList.size();
        logger.log("number of subtitle title in banner grid : " + numberOfSubTitleOfBanner);

        return numberOfGridBannerCalculated == numberOfTitleOfBanner && numberOfTitleOfBanner ==  numberOfSubTitleOfBanner;
    }

    @Override
    public boolean verifyBannerGrid(TTDPartnerPageModules.Datum data){
        String bannerGridMainTitleApi = data.moduleDetail.heading.title;
        String bannerGridMainSubTitleApi = data.moduleDetail.heading.subtitle;
        String bannerGridCtaBtnApi = data.dynamicContents.get(0).chips.get(0).buttonText;

        logger.log("Banner Grid Main Title : " + bannerGridMainTitleApi);

        removeReloadBtn();
        driverTools.swipeToElement(PARENT_GRID_ELEMENT, Swipe.DOWN, 3).orElseThrow();

        WebElement bannerGridElement = driverTools.swipeToElement(getTitle("BANNER_GRID",bannerGridMainTitleApi), Swipe.UP,4).orElseThrow();
        String bannerGridMainTitleApp = driverTools.getText(bannerGridElement);
        logger.log("is grid banner displayed : " + !bannerGridMainTitleApp.isEmpty());

        boolean isBannerGridSubTitleDisplayed = driverTools.isElementDisplayed(getSubTitle("BANNER_GRID",bannerGridMainSubTitleApi));
        logger.log("is grid banner subtitle displayed : " + isBannerGridSubTitleDisplayed);

        boolean isBannerSizeCorrect = checkBannerSize(bannerGridMainTitleApp);
        logger.log("is number of banner correct : " + isBannerSizeCorrect);

        boolean isBannerTitleAndSubTitleCorrect = checkTitleAndSubTitleBanner(bannerGridMainTitleApp,data);
        logger.log("is title and subtitle of banner correct : " + isBannerTitleAndSubTitleCorrect);

        boolean isBannerGridCtaDisplayed = true;
        if(bannerGridCtaBtnApi!=null && bannerGridCtaBtnApi.toLowerCase().contains("cta")){
            WebElement ctaBtn = driverTools.swipeToElement(getCtaBtnOfBannerGrid("CTA_BTN_TEXT",bannerGridCtaBtnApi),Swipe.UP,3).orElseThrow();
            isBannerGridCtaDisplayed = driverTools.isElementDisplayed(ctaBtn);
            logger.log("is grid banner CTA Btn displayed : " + isBannerGridCtaDisplayed);
        }

        return !bannerGridMainTitleApp.isEmpty() && isBannerGridSubTitleDisplayed && isBannerGridCtaDisplayed
                && isBannerSizeCorrect && isBannerTitleAndSubTitleCorrect;

    }

    @Override
    public boolean clickAndVerifyBannerGridRedirection(TTDPartnerPageModules.Datum data){
        String bannerTitle = data.dynamicContents.get(0).chips.get(0).bannerContents.get(0).title;
        driverTools.click(getTitle("BANNER_UNDER_GRID",bannerTitle));

        SearchResultPage<T> searchResultPage = SearchResultPage.getInstance(session);
        searchResultPage.closeFilterPopup();
        boolean isOnSearchResultPage = searchResultPage.isSearchResultShown();
        logger.log("is redirected to SRP : " + isOnSearchResultPage);
        return isOnSearchResultPage;
    }

    private void removeReloadBtn(){
        for(int tries = 3; tries>0 && driverTools.waitForElementToAppear(RELOAD,5).isPresent(); tries--){
            driverTools.click(RELOAD);
        }
    }

    private Locator getChipsLocator(String text){
        return new Locator("CHIPS_LOCATOR", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and contains(@text,'"+text+"')]"));
    }

    private boolean checkChips(ArrayList<TTDPartnerPageModules.Chip> data){
        String lastChip = data.get(data.size()-1).text;

        WebElement chipsLast = driverTools.swipeToElement(getChipsLocator(lastChip),CHIPS_GROUP,Swipe.LEFT,5).orElseThrow();
        boolean isChipDisplayed = driverTools.isElementDisplayed(chipsLast);
        logger.log("chip scrollable : " + isChipDisplayed);
        driverTools.click(chipsLast);

        return isChipDisplayed;
    }

    @Override
    public boolean verifyImageWithChips(TTDPartnerPageModules.Datum data){
        String imageWithChipsMainTitleApi = data.moduleDetail.heading.title;
        String imageWithChipsMainSubTitleApi = data.moduleDetail.heading.subtitle;

        removeReloadBtn();
        driverTools.waitForElementToAppear(PARENT_GRID_ELEMENT).orElseThrow();

        WebElement bannerGridElement = driverTools.swipeToElement(getTitle("IMAGE_WITH_CHIPS",imageWithChipsMainTitleApi), Swipe.UP,30).orElseThrow();
        String bannerImageWithChipsApp = driverTools.getText(bannerGridElement);
        logger.log("is image with chips title displayed : " + !bannerImageWithChipsApp.isEmpty());

        driverTools.swipeUp();;

        boolean isImageWithChipsSubTitleDisplayed = driverTools.isElementDisplayed(getSubTitle("IMAGE_WITH_CHIPS",imageWithChipsMainSubTitleApi));
        logger.log("is image with chips subtitle displayed : " + isImageWithChipsSubTitleDisplayed);

        boolean isChipsGroupDisplayed = driverTools.isElementDisplayed(CHIPS_GROUP);
        logger.log("is chips group displayed : " + isChipsGroupDisplayed);

        boolean isCardDisplayed = driverTools.isElementDisplayed(CARD_VIEW);
        logger.log("is card displayed : " + isCardDisplayed);

        boolean isImageBannerDisplayed = driverTools.isElementDisplayed(BANNER);
        logger.log("is chips group displayed : " + isImageBannerDisplayed);

        boolean isStrikePrice = driverTools.isElementDisplayed(STRIKE_PRICE);
        logger.log("is strike price displayed : " + isStrikePrice);

        boolean isFinalPrice = driverTools.isElementDisplayed(FINAL_PRICE);
        logger.log("is final price displayed : " + isFinalPrice);

        boolean isRibbonDisplayed = driverTools.isElementDisplayed(RIBBON);
            logger.log("is ribbon displayed : " + isRibbonDisplayed);

        return !bannerImageWithChipsApp.isEmpty() && isImageWithChipsSubTitleDisplayed && isChipsGroupDisplayed &&
                isCardDisplayed && isImageBannerDisplayed && isStrikePrice && isFinalPrice && isRibbonDisplayed;
    }

    private Locator getCardViewLocator(String text){
        return new Locator("CARD_LOCATOR", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'"+text+"')]"));
    }

    @Override
    public boolean clickAndVerifyScrollImageWithChipsRedirection(TTDPartnerPageModules.Datum data){
        String cardTitleApi = data.dynamicContents.get(0).cards.get(data.dynamicContents.get(0).cards.size()-1).productName;
        WebElement cardTitleElement = driverTools.swipeToElement(getCardViewLocator(cardTitleApi),Swipe.LEFT,5).orElseThrow();
        driverTools.click(cardTitleElement);

        DetailPage<T> detailPageHotel = DetailPage.getInstance(this.session);
        return detailPageHotel.checkHotelName(cardTitleApi);
    }

    @Override
    public boolean clickAndVerifyScrollBannerCarouselRedirection(TTDPartnerPageModules.Datum data){
        String cardTitleApi = data.dynamicContents.get(0).chips.get(0).bannerContents.get(data.dynamicContents.get(0).chips.get(0).bannerContents.size()-1).title;
        WebElement cardTitleElement = driverTools.swipeToElement(getCardViewLocator(cardTitleApi),Swipe.LEFT,5).orElseThrow();
        driverTools.click(cardTitleElement);

        SearchResultPage<T> searchResultPage = SearchResultPage.getInstance(this.session);
        return searchResultPage.isSearchResultShown();
    }

    @Override
    public void backToTTDPartnerPage(){
        DetailPage<T> detailPageHotel = DetailPage.getInstance(this.session);
        detailPageHotel.clickBackButton();

        LandingPage<T> landingPageHotel = LandingPage.getInstance(this.session);
        landingPageHotel.backToHomePage();
    }

    @Override
    public boolean isChipsScrollable(TTDPartnerPageModules.Datum data){
        boolean isChipsGroupDisplayed = driverTools.isElementDisplayed(CHIPS_GROUP);
        logger.log("is chips group displayed : " + isChipsGroupDisplayed);
        boolean isChipsGroupClickableAndScrollable = checkChips(data.dynamicContents.get(0).chips);
        logger.log("is chips scrollable : " + isChipsGroupClickableAndScrollable);

        return isChipsGroupDisplayed && isChipsGroupClickableAndScrollable;
    }

    @Override
    public boolean clickSeeAllBtn(TTDPartnerPageModules.Datum data){
        WebElement seeAllBtn = driverTools.swipeToElement(SEE_ALL_BTN,Swipe.UP,2).orElseThrow();
        driverTools.click(seeAllBtn);

        boolean isSeeAllPageDisplayed = driverTools.waitForElementToAppear(PARTNER_PAGE_IMG_SEE_ALL_BTN).isPresent();
        driverTools.click(PARTNER_PAGE_SEE_ALL_BACK_BTN);
        return isSeeAllPageDisplayed;
    }

    @Override
    public boolean verifyBannerCarousel(TTDPartnerPageModules.Datum data) {
        String bannerCarouselMainTitleApi = data.moduleDetail.heading.title;
        String bannerCarouselMainSubTitleApi = data.moduleDetail.heading.subtitle;
        String btnSeeAllText = data.dynamicContents.get(0).chips.get(0).buttonText;
        String chip1 = data.dynamicContents.get(0).chips.get(0).text;
        String banner1Title = data.dynamicContents.get(0).chips.get(0).bannerContents.get(0).title;
        String banner1SubTitle = data.dynamicContents.get(0).chips.get(0).bannerContents.get(0).subtitle;

        removeReloadBtn();
        driverTools.waitForElementToAppear(PARENT_GRID_ELEMENT).orElseThrow();

        WebElement bannerGridElement = driverTools.swipeToElement(getTitle("BANNER_CAROUSEL",bannerCarouselMainTitleApi), Swipe.UP,30).orElseThrow();
        String bannerCarouselApp = driverTools.getText(bannerGridElement);
        logger.log("is Banner Carousel title displayed : " + !bannerCarouselApp.isEmpty());

        driverTools.swipeUp();

        boolean isBannerCarouselSubTitleDisplayed = driverTools.isElementDisplayed(getSubTitle("BANNER_CAROUSEL",bannerCarouselMainSubTitleApi));
        logger.log("is Banner Carousel subtitle displayed : " + isBannerCarouselSubTitleDisplayed);

        boolean isChipsGroupDisplayed = driverTools.isElementDisplayed(CHIPS_GROUP);
        logger.log("is chips group displayed : " + isChipsGroupDisplayed);

        boolean isSeeAllBtnTextCorrect = driverTools.isElementDisplayed(getCtaBtnOfBannerGrid("BANNER_CAROUSEL_SEE_ALL_BTM",btnSeeAllText));
        logger.log("is chips see all btn displayed: " + isSeeAllBtnTextCorrect);

        boolean isBannerCarouselBannerTitleDisplayed = driverTools.isElementDisplayed(getTitle("BANNER_CAROUSEL",banner1Title));
        logger.log("is Banner Carousel subtitle displayed : " + isBannerCarouselBannerTitleDisplayed);

        boolean isBannerCarouselBannerSubTitleDisplayed = driverTools.isElementDisplayed(getSubTitle("BANNER_CAROUSEL",banner1SubTitle));
        logger.log("is Banner Carousel subtitle displayed : " + isBannerCarouselBannerSubTitleDisplayed);

        return !bannerCarouselApp.isEmpty() && isBannerCarouselSubTitleDisplayed && isChipsGroupDisplayed && isSeeAllBtnTextCorrect && isBannerCarouselBannerTitleDisplayed && isBannerCarouselBannerSubTitleDisplayed;
    }

    @Override
    public boolean verifyDisplayedCurrency(String currency, String section) {
        switch (section) {
            case "partner product":
                return verifyDisplayerCurrencyPartnerProduct(currency);
            case "page module":
                goToPartnerProductSection();
                return verifyDisplayedCurrencyPageModule(currency);
        }
        return false;
    }

    private void goToPartnerProductSection() {
        driverTools.swipeToElement(TTDSearchResultPageAndroid.PRODUCT_CARD_PRICE_TEXTS, Swipe.UP, 20);
    }

    @Override
    public boolean isPageModuleDisplayed() {
        driverTools.swipeUp();
        return driverTools.waitForElementToAppear(TTDLandingPageAndroid.PRICE_TEXTS, 10).isPresent();
    }

    private boolean verifyDisplayedCurrencyPageModule(String currency) {
        TTDLandingPage<T> ttdLandingPage = TTDLandingPage.getInstance(session);
        return ttdLandingPage.verifyDisplayedCurrency(currency);
    }

    private boolean verifyDisplayerCurrencyPartnerProduct(String currency) {
        TTDSearchResultPage<T> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        return ttdSearchResultPage.verifyDisplayedCurrency(currency,"product card");
    }

    @Override
    public void clickBack() {
        driverTools.waitForElementToAppear(BACK_BUTTON);
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public boolean isPromoSectionDisplayed(){
        driverTools.waitForElementToAppear(PARTNER_PAGE_NAME);
        boolean isPromoSectionShown = driverTools.isElementDisplayed(PROMO_SECTION);
        logger.log("is promo section displayed : " + isPromoSectionShown);
        return isPromoSectionShown;
    }
}
