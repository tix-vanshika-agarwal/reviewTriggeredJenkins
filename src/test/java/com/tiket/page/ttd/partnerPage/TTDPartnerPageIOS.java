package com.tiket.page.ttd.partnerPage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.page.ttd.landingpage.TTDLandingPageIOS;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.pojo.ttd.TTDPartnerPageModules;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class TTDPartnerPageIOS <T extends WebDriver> extends TTDPartnerPage<T> {
    public TTDPartnerPageIOS(Session<T> session) { super(session); }
    private static final Locator PARTNER_PAGE_NAME = new Locator("PARTNER_PAGE_NAME", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='ttd_partner_name']"));
    private static final Locator CLAIM_PROMO_CODE = new Locator("CLAIM_PROMO_CODE", AppiumBy.xpath("//XCUIElementTypeButton[@label='Claim' or @label='Klaim' or @name='tds ic copy']"));
    private static final Locator PARTNER_PAGE_RATING_TITLE = new Locator("PARTNER_PAGE_RATING_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Rating']"));
    private static final Locator PARTNER_PAGE_BOOKING_TITLE = new Locator("PARTNER_PAGE_BOOKING_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Dipesan' or @name='Booked']"));
    private static final Locator PARTNER_PAGE_TOTAL_PRODUCT_TITLE = new Locator("PARTNER_PAGE_TOTAL_PRODUCT_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Total']"));
    private static final Locator RIBBON = new Locator("RIBBON", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'Ribbon')][1]"));
    private static final Locator FINAL_PRICE = new Locator("FINAL_PRICE", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='bn_chips_price'])[2]"));
    private static final Locator STRIKE_PRICE = new Locator("STRIKE_PRICE", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='bn_chips_slash_price'])[1]"));
    private static final Locator PARTNER_PAGE_SEE_ALL_BACK_BTN = new Locator("PARTNER_PAGE_SEE_ALL_BACK_BTN", AppiumBy.xpath("//XCUIElementTypeImage[@name='tds_ic_back']"));
    private static final Locator RELOAD = new Locator("RELOAD", AppiumBy.xpath("//XCUIElementTypeButton[@name='Reload']"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("(//*[@name='tds ic back' or @name='tds_ic_back' and @visible='true'])[last()]"));

    private void removeReloadBtn(){
        for(int tries = 3; tries>0 && driverTools.waitForElementToAppear(RELOAD,5).isPresent(); tries--){
            driverTools.click(RELOAD);
        }
    }

    @Override
    public boolean verifyPartnerPage(String keyword){

        WebElement partnerPageNameElement = driverTools.waitForElementToAppear(PARTNER_PAGE_NAME,30).orElseThrow();
        String partnerPageName = driverTools.getValueAttribute(partnerPageNameElement,"value");
        boolean isPartnerPageNameCorrect = partnerPageName.contains(keyword);
        logger.log("is partner page name correct : " + isPartnerPageNameCorrect);

        boolean isPartnerPageRatingDisplayed = driverTools.isElementDisplayed(PARTNER_PAGE_RATING_TITLE);
        logger.log("is partner page rating title displayed : " + isPartnerPageRatingDisplayed);
        boolean isPartnerPageBookingDisplayed = driverTools.isElementDisplayed(PARTNER_PAGE_BOOKING_TITLE);
        logger.log("is partner page booking title displayed : " + isPartnerPageBookingDisplayed);
        boolean isPartnerPageTotalProductDisplayed = driverTools.isElementDisplayed(PARTNER_PAGE_TOTAL_PRODUCT_TITLE);
        logger.log("is partner page total product title displayed : " + isPartnerPageTotalProductDisplayed);

        return isPartnerPageNameCorrect && isPartnerPageRatingDisplayed && isPartnerPageBookingDisplayed
                && isPartnerPageTotalProductDisplayed;
    }

    private Locator getTitleOfBannerGrid(String type,String title){
        return new Locator(type.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeStaticText[@name='"+title+"']"));
    }

    private Locator getCtaBtnOfBannerGrid(String type,String title){
        return new Locator(type.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeButton[@name='"+title+"']"));
    }

    private Locator getTitleOrSubTitleOfBannerGrid(String title,String gridTitle){
        return new Locator("TITLE_OR_SUBTITLE_BANNER_GRID", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='"+gridTitle+"']//following-sibling::XCUIElementTypeStaticText[@name='"+title+"']"));
    }

    private Locator getBannerGrid(String title,String flag){
        if(flag.equals("preceding")){
            return new Locator("BANNER_UNDER_GRID_PRE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='"+title+"']//preceding-sibling::XCUIElementTypeImage[@name='bn_grid']"));
        }
        return new Locator("BANNER_UNDER_GRID_FOLLOWING", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='"+title+"']//following-sibling::XCUIElementTypeImage[@name='bn_grid']"));
    }

    private Locator getFirstBanner(String title){
        return new Locator("FIRST_BANNER_TITLE", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='"+title+"'])[1]"));
    }

    private int getSizeOfBanner(String gridTitle){
        int index = gridTitle.indexOf(":");
        int firstNumber = Integer.parseInt(String.valueOf(gridTitle.charAt(index-1)));
        int secondNumber = Integer.parseInt(String.valueOf(gridTitle.charAt(index+1)));
        return firstNumber * secondNumber;
    }


    private boolean checkBannerSize(String gridTitle){
        int numberOfGridBannerCalculated = getSizeOfBanner(gridTitle);
        logger.log("calculated number of banner : " + numberOfGridBannerCalculated);

        List<WebElement> bannerGridPre = driverTools.elements(getBannerGrid(gridTitle,"preceding")).orElseThrow();
        List<WebElement> bannerGridAfter = driverTools.elements(getBannerGrid(gridTitle,"following")).orElseThrow();
        int numberOfGridBannerActual = bannerGridPre.size() + bannerGridAfter.size();
        logger.log("actual number of banner : " + numberOfGridBannerActual);

        return numberOfGridBannerCalculated == numberOfGridBannerActual;
    }

    private boolean checkTitleAndSubTitleBanner(String gridTitle, TTDPartnerPageModules.Datum data){
        int numberOfGridBannerCalculated = getSizeOfBanner(gridTitle);
        logger.log("calculated number of banner : " + numberOfGridBannerCalculated);

        String bannerTitle = data.dynamicContents.get(0).chips.get(0).bannerContents.get(0).title;
        String bannerSubTitle = data.dynamicContents.get(0).chips.get(0).bannerContents.get(0).subtitle;
        List<WebElement> bannerTitleList = driverTools.elements(getTitleOrSubTitleOfBannerGrid(bannerTitle,gridTitle)).orElseThrow();
        List<WebElement> bannerSubTitleList = driverTools.elements(getTitleOrSubTitleOfBannerGrid(bannerSubTitle,gridTitle)).orElseThrow();

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

        removeReloadBtn();

        WebElement bannerGridElement = driverTools.swipeToElement(getTitleOfBannerGrid("MAIN_TITLE_BANNER_GRID",bannerGridMainTitleApi), Swipe.UP,4).orElseThrow();
        String bannerGridMainTitleApp = driverTools.getValueAttribute(bannerGridElement,"value");
        logger.log("is grid banner title displayed  : " + !bannerGridMainTitleApp.isEmpty());

        boolean isBannerGridSubTitleDisplayed = driverTools.isElementDisplayed(getTitleOfBannerGrid("MAIN_SUB_TITLE_BANNER_GRID",bannerGridMainSubTitleApi));
        logger.log("is grid banner subtitle displayed : " + isBannerGridSubTitleDisplayed);

        boolean isBannerSizeCorrect = checkBannerSize(bannerGridMainTitleApp);
        logger.log("is number of banner correct : " + isBannerSizeCorrect);

        boolean isBannerTitleAndSubTitleCorrect = checkTitleAndSubTitleBanner(bannerGridMainTitleApp,data);
        logger.log("is title and subtitle of banner correct : " + isBannerTitleAndSubTitleCorrect);

        boolean isBannerGridCtaDisplayed = true;
        if(bannerGridCtaBtnApi!=null && bannerGridCtaBtnApi.toLowerCase().contains("cta")){
            WebElement ctaBtn = driverTools.swipeToElement(getCtaBtnOfBannerGrid("CTA_BTN_TEXT",bannerGridCtaBtnApi),Swipe.UP,3).orElseThrow();
            isBannerGridCtaDisplayed = driverTools.isElementDisplayed(ctaBtn);
            logger.log("is grid banner Cta Btn displayed : " + isBannerGridCtaDisplayed);
        }

        return !bannerGridMainTitleApp.isEmpty() && isBannerGridSubTitleDisplayed && isBannerGridCtaDisplayed
                && isBannerSizeCorrect && isBannerTitleAndSubTitleCorrect;
    }

    @Override
    public boolean clickAndVerifyBannerGridRedirection(TTDPartnerPageModules.Datum data){
        String bannerTitle = data.dynamicContents.get(0).chips.get(0).bannerContents.get(0).title;
        driverTools.click(getFirstBanner(bannerTitle));

        SearchResultPage<T> searchResultPage = SearchResultPage.getInstance(session);
        searchResultPage.closeFilterPopup();
        boolean isOnSearchResultPage = searchResultPage.isSearchResultShown();
        logger.log("is redirected to SRP : " + isOnSearchResultPage);
        return isOnSearchResultPage;
    }

    private Locator getTextLocator(String type, String text){
        return new Locator("TITLE_SUBTITLE_"+type.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'"+text+"')]"));
    }

    private Locator getProductNameLocator(String type, String product){
        return new Locator("PRODUCT_NAME_"+type.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeStaticText[@name='page_module_destination_card_title' and contains(@value,'"+product+"')]"));
    }

    private Locator getImageBannerLocator(String title){
        return new Locator("IMAGE_BANNER", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='"+title+"']//preceding-sibling::XCUIElementTypeImage[@name='bn_chips'][last()]"));
    }

    private Locator getStrikePriceLocator(String title){
        return new Locator("STRIKE_PRICE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='"+title+"']//following-sibling::*[contains(@name,'bn_chips_slash_price')][1]"));
    }

    private Locator getDiscountedPriceLocator(String title){
        return new Locator("DISCOUNTED_PRICE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='"+title+"']//following-sibling::*[contains(@name,'bn_chips_price')][1]"));
    }

    @Override
    public boolean verifyImageWithChips(TTDPartnerPageModules.Datum data){
        String imageWithChipsMainTitleApi = data.moduleDetail.heading.title;
        String imageWithChipsMainSubTitleApi = data.moduleDetail.heading.subtitle;
        String btnSeeAllText = data.dynamicContents.get(0).chips.get(0).buttonText;
        String chip1 = data.dynamicContents.get(0).chips.get(0).text;
        String product1 = data.dynamicContents.get(0).cards.get(0).productName;

        removeReloadBtn();

        WebElement bannerGridElement = driverTools.swipeToElement(getTextLocator("IMAGE_WITH_CHIPS",imageWithChipsMainTitleApi), Swipe.UP,30).orElseThrow();
        String bannerImageWithChipsApp = driverTools.getText(bannerGridElement);
        logger.log("is image with chips title displayed : " + !bannerImageWithChipsApp.isEmpty());

        driverTools.swipeToElement(getTextLocator("BTN_SEE_ALL",btnSeeAllText),Swipe.UP,3).orElseThrow();

        boolean isImageWithChipsSubTitleDisplayed = driverTools.isElementDisplayed(getTextLocator("IMAGE_WITH_CHIPS",imageWithChipsMainSubTitleApi));
        logger.log("is image with chips subtitle displayed : " + isImageWithChipsSubTitleDisplayed);

        boolean isChipsGroupDisplayed = driverTools.isElementDisplayed(getTextLocator("CHIP_1",chip1));
        logger.log("is chips 1 displayed : " + isChipsGroupDisplayed);

        boolean isProductNameDisplayed = driverTools.isElementDisplayed(getProductNameLocator("1",product1));
        logger.log("is product 1 displayed : " + isProductNameDisplayed);

        boolean isImageBannerDisplayed = driverTools.isElementDisplayed(getImageBannerLocator(imageWithChipsMainTitleApi));
        logger.log("is chips group displayed : " + isImageBannerDisplayed);

        boolean isStrikePrice = driverTools.isElementDisplayed(getStrikePriceLocator(imageWithChipsMainTitleApi));
        logger.log("is strike price displayed : " + isStrikePrice);

        boolean isFinalPrice = driverTools.isElementDisplayed(getDiscountedPriceLocator(imageWithChipsMainSubTitleApi));
        logger.log("is final price displayed : " + isFinalPrice);

        boolean isRibbonDisplayed = driverTools.isElementDisplayed(RIBBON);
        logger.log("is ribbon displayed : " + isRibbonDisplayed);

        return !bannerImageWithChipsApp.isEmpty() && isImageWithChipsSubTitleDisplayed && isChipsGroupDisplayed &&
                isProductNameDisplayed && isImageBannerDisplayed && isStrikePrice && isFinalPrice && isRibbonDisplayed;
    }

    @Override
    public boolean clickAndVerifyScrollImageWithChipsRedirection(TTDPartnerPageModules.Datum data){
        String cardTitleApi = data.dynamicContents.get(0).cards.get(data.dynamicContents.get(0).cards.size()-1).productName;
        WebElement cardTitleElement = driverTools.swipeToElement(getProductNameLocator("LAST",cardTitleApi),Swipe.LEFT,5).orElseThrow();
        driverTools.click(cardTitleElement);

        DetailPage<T> detailPageHotel = DetailPage.getInstance(this.session);
        return detailPageHotel.checkHotelName(cardTitleApi);
    }

    @Override
    public boolean clickAndVerifyScrollBannerCarouselRedirection(TTDPartnerPageModules.Datum data){
        String cardTitleApi = data.dynamicContents.get(0).chips.get(0).bannerContents.get(0).title;
        WebElement cardTitleElement = driverTools.swipeToElement(getProductNameLocator("LAST",cardTitleApi),Swipe.LEFT,5).orElseThrow();
        driverTools.click(cardTitleElement);

        SearchResultPage<T> searchResultPage = SearchResultPage.getInstance(this.session);
        return searchResultPage.isSearchResultShown();
    }

    @Override
    public void backToTTDPartnerPage(){
        DetailPage<T> detailPageHotel = DetailPage.getInstance(this.session);
        detailPageHotel.clickBackButton();
    }

    private boolean checkChips(ArrayList<TTDPartnerPageModules.Chip> data){
        //there is no parent to scroll chips so cant check if scroll through code
        String lastChip = data.get(1).text;

        WebElement chipsLast = driverTools.swipeToElement(getTextLocator("LAST_CHIP",lastChip),Swipe.LEFT,5).orElseThrow();
        boolean isChipDisplayed = driverTools.isElementDisplayed(chipsLast);
        logger.log("chip scrollable : " + isChipDisplayed);
        driverTools.click(chipsLast);

        return isChipDisplayed;
    }

    @Override
    public boolean isChipsScrollable(TTDPartnerPageModules.Datum data){
        String chip1 = data.dynamicContents.get(0).chips.get(0).text;
        boolean isChipsGroupDisplayed = driverTools.isElementDisplayed(getChipsTextLocator("CHIP_1",chip1));
        logger.log("is chips group displayed : " + isChipsGroupDisplayed);
        boolean isChipsGroupClickableAndScrollable = checkChips(data.dynamicContents.get(0).chips);
        logger.log("is chips scrollable : " + isChipsGroupClickableAndScrollable);

        return isChipsGroupDisplayed && isChipsGroupClickableAndScrollable;
    }

    private Locator getChipsTextLocator(String type, String text){
        return new Locator("TITLE_CHIPS_"+type.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'page_module_chip_text') and contains(@value,'"+text+"')]"));
    }

    private Locator getCTABtnLocator( String type,String btnText){
        return new Locator("SEE ALL BTN"+type.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeStaticText[@name='page_module_description' and contains(@label,'"+btnText+"')]/preceding-sibling::*"));
    }
    private Locator getBannerSubtitlLocator(String type, String product){
        return new Locator("PRODUCT_NAME_"+type.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeStaticText[@name='page_module_destination_card_subtitle' and contains(@value,'"+product+"')]"));
    }

    @Override
    public boolean clickSeeAllBtn(TTDPartnerPageModules.Datum data){
        String btnSeeAllText = data.dynamicContents.get(0).chips.get(0).buttonText;
        WebElement seeAllBtn = driverTools.swipeToElement(getTextLocator("BTN_SEE_ALL",btnSeeAllText),Swipe.UP,2).orElseThrow();
        driverTools.click(seeAllBtn);

        boolean isSeeAllPageDisplayed = driverTools.waitForElementToAppear(PARTNER_PAGE_NAME).isPresent();
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


        WebElement bannerCarouselElement = driverTools.swipeToElement(getTextLocator("BANNER_CAROUSEL",bannerCarouselMainTitleApi), Swipe.UP,30).orElseThrow();
        String bannerCarouselApp = driverTools.getText(bannerCarouselElement);
        logger.log("is Banner Carousel title displayed : " + !bannerCarouselApp.isEmpty());

        driverTools.swipeToElement(getCTABtnLocator(btnSeeAllText,bannerCarouselMainSubTitleApi),Swipe.UP,3).orElseThrow();

        boolean isBannerCarouselSubTitleDisplayed = driverTools.isElementDisplayed(getTextLocator("BANNER_CAROUSEL",bannerCarouselMainSubTitleApi));
        logger.log("is Banner Carousel subtitle displayed : " + isBannerCarouselSubTitleDisplayed);

        boolean isChipsGroupDisplayed = driverTools.isElementDisplayed(getTextLocator("CHIP_1",chip1));
        logger.log("is chips 1 displayed : " + isChipsGroupDisplayed);

        boolean isBanner1Title = driverTools.isElementDisplayed(getProductNameLocator("BANNER_1_TITLE",banner1Title));
        logger.log("is Banner 1 title displayed : " + isBanner1Title);

        boolean isBanner1SubTitle = driverTools.isElementDisplayed(getBannerSubtitlLocator("BANNER_1_SUBTITLE",banner1SubTitle));
        logger.log("is Banner 1 SubTitle displayed : " + isBanner1SubTitle);


        return !bannerCarouselApp.isEmpty() && isBannerCarouselSubTitleDisplayed && isChipsGroupDisplayed && isBanner1Title && isBanner1SubTitle;
    }

    @Override
    public void clickBack() {
        driverTools.waitForElementToAppear(BACK_BUTTON);
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public boolean verifyDisplayedCurrency(String currency, String section) {
        switch (section) {
            case "partner product":
                return verifyDisplayerCurrencyPartnerProduct(currency);
            case "page module":
                return verifyDisplayedCurrencyPageModule(currency);
        }
        return false;
    }

    @Override
    public boolean isPageModuleDisplayed() {
        return driverTools.waitForElementToPresence(TTDLandingPageIOS.PRODUCT_CARD_PRICE_TEXT, 10).isPresent();
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
    public boolean isPromoSectionDisplayed(){
        driverTools.waitForElementToAppear(PARTNER_PAGE_NAME);
        boolean isPromoSectionShown = driverTools.isElementDisplayed(CLAIM_PROMO_CODE);
        logger.log("is promo section displayed : " + isPromoSectionShown);
        return isPromoSectionShown;
    }
}
