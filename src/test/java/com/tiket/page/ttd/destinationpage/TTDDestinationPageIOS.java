package com.tiket.page.ttd.destinationpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.testbase.Assertion;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class TTDDestinationPageIOS <T extends WebDriver> extends TTDDestinationPage<T> {

    public TTDDestinationPageIOS(Session<T> session) {
        super(session);
    }

    private static final Locator DESTINATION_PAGE_NAME_BUTTON = new Locator("DESTINATION_PAGE_NAME_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='ttd_destination_name']"));
    private static final Locator DESTINATIONPAGE_NAME_LABEL = new Locator("DESTINATIONPAGE_NAME_LABEL", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='ttd_destination_name']"));
    private static final Locator DESTINATION_TAB = new Locator("DESTINATION_TAB", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Destinasi' or @name='Destinations']"));
    private static final Locator TO_DO_CATEGORY = new Locator("TO_DO_CATEGORY", AppiumBy.xpath("//XCUIElementTypeImage[@name='product_category']"));
    private static final Locator SEE_ALL_BUTTON = new Locator("SEE_ALL_BUTTON", AppiumBy.xpath("//XCUIElementTypeCell[./*[contains(@name,'product_card')]]//XCUIElementTypeOther[contains(@name,'_see_all') and @visible='true']"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'ttd_back_btn') and @visible='true']"));


    // Campaign Section
    private static final Locator CAMPAIGN_ENDS_IN = new Locator("CAMPAIGN_ENDS_IN", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Berakhir dalam' or @label='Promo ends in']"));
    private static final Locator CAMPAIGN_TIMER = new Locator("CAMPAIGN_TIMER", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Berakhir dalam' or @label='Promo ends in']/following-sibling::XCUIElementTypeStaticText[@label=':']"));
    private static final Locator CAMPAIGN_PRODUCT_CARD_TITLE = new Locator("CAMPAIGN_PRODUCT_CARD_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Berakhir dalam' or @label='Promo ends in']/parent::*/XCUIElementTypeStaticText[@name='product_name']"));

    // Banner Section
    private static final Locator FIRST_BANNER_IMAGE_TITLE = new Locator("FIRST_BANNER_IMAGE_TITLE", AppiumBy.xpath("((//XCUIElementTypeImage[@name='bn_carrousel'])[1]/parent::*/XCUIElementTypeStaticText[@name='Label'])[last()]/following-sibling::XCUIElementTypeStaticText[1]"));
    private static final Locator FIRST_BANNER_SECTION_CHIPS = new Locator("FIRST_BANNER_SECTION_CHIPS", AppiumBy.xpath("(//XCUIElementTypeImage[@name='bn_carrousel'])[1]/parent::*/child::XCUIElementTypeOther[./XCUIElementTypeOther[contains(@name,'selected')]]/XCUIElementTypeOther"));
    private static final Locator SELECTED_BANNER_SECTION_CHIP = new Locator("SELECTED_BANNER_SECTION_CHIP", AppiumBy.xpath("(//XCUIElementTypeImage[@name='bn_carrousel'])[1]/parent::*/child::XCUIElementTypeOther/XCUIElementTypeOther[contains(@name,'selected')]//XCUIElementTypeStaticText"));


    @Override
    public boolean isDestinationPageValid(String keyword, String type) {
        final Locator TYPE_DESTINATIONPAGE = new Locator(
                "TYPE_DESTINATIONPAGE",
                AppiumBy.xpath("(//XCUIElementTypeOther//XCUIElementTypeStaticText[@value='"
                        + type.replace("Optional[", "").replace("]", "") + "'])[2]")
        );
        driverTools.waitForElementToAppear(DESTINATION_PAGE_NAME_BUTTON,10);
        boolean nameDestinationElementDisplayed = driverTools.isElementDisplayed(DESTINATION_PAGE_NAME_BUTTON);
        String text = String.valueOf(driverTools.getValueAttribute(TYPE_DESTINATIONPAGE, "value"));

        logger.log("Is DESTINATIONPAGE_NAME_BUTTON displayed? " + nameDestinationElementDisplayed);
        logger.log("values are: " + text + " and " + type.replace("Optional[", "").replace("]", ""));

        if (type.equals("-")) {
            return nameDestinationElementDisplayed;
        }
        return nameDestinationElementDisplayed && text.equals(type.replace("Optional[", "").replace("]", ""));
    }

    @Override
    public String getDestinationName() {
        WebElement destinationName = driverTools.waitForElementToPresence(DESTINATIONPAGE_NAME_LABEL).orElseThrow();
        return driverTools.getValueAttribute(destinationName, "label");
    }

    @Override
    public String getDestinationType(String type) {
        return driverTools.element(getDestinationTypeLocator(type)).orElseThrow().getAttribute("name");
    }

    private Locator getDestinationTypeLocator(String type) {
        String typeDestinationPageXpath = "";
        if (type.equalsIgnoreCase("country / negara")) {
            typeDestinationPageXpath += "@name='NEGARA' or @name='COUNTRY'";
        } else if (type.equalsIgnoreCase("region / wilayah")) {
            typeDestinationPageXpath += "@name='WILAYAH' or @name='REGION'";
        } else if (type.equalsIgnoreCase("city / kota")) {
            typeDestinationPageXpath += "@name='KOTA' or @name='CITY'";
        }

        return new Locator("TYPE_DESTINATIONPAGE", AppiumBy.xpath("//XCUIElementTypeOther//XCUIElementTypeStaticText["+ typeDestinationPageXpath +"]"));
    }

    @Override
    public void chooseCategory(String type, String param) {
        WebElement categoryElement;

        driverTools.waitForElementToAppear(TO_DO_CATEGORY,10);

        switch (type) {
            case "random":
            case "index":
                List<WebElement> listOfCategory = driverTools.elements(TO_DO_CATEGORY).orElseThrow();

                categoryElement = param.equals("-") ? listOfCategory.get(CoreFunctions.generateRandomInt(0,listOfCategory.size()-1)) : listOfCategory.get(Integer.parseInt(param));
                driverTools.click(categoryElement);

                break;
            case "title":
                categoryElement = driverTools.element(getToDoCategoryByTitle(param)).orElseThrow();
                driverTools.click(categoryElement);
                break;
            default:
                logger.fail("Can't handle choose category by '"+type+"'");
        }

    }

    @Override
    public boolean checkSection(String section) {
        boolean checkResult = false;

        switch (section) {
            case "banner" : return checkBannerSection();
            case "campaign" : return checkCampaignSection();
            case "all cta" : return checkAllCTASection();
            default: logger.fail("There are no section named '"+section+"'");
        }

        return checkResult;
    }

    private boolean checkAllCTASection() {
        TTDSearchResultPage<T> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        driverTools.swipeUp(3);
        List<WebElement> seeAllButtonElementList = driverTools.elements(SEE_ALL_BUTTON).orElseThrow();
        if(seeAllButtonElementList.isEmpty()) {
            driverTools.swipeToElement(SEE_ALL_BUTTON,Swipe.UP,20);
            seeAllButtonElementList = driverTools.elements(SEE_ALL_BUTTON).orElseThrow();
        }

        driverTools.click(seeAllButtonElementList.get(CoreFunctions.generateRandomInt(0, seeAllButtonElementList.size()-1)));
        return ttdSearchResultPage.isSearchResultPageShown();
    }

    private boolean checkCampaignSection() {
        WebViewPage<T> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<T> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        boolean isCountDownPresent = driverTools.waitForElementToPresence(CAMPAIGN_ENDS_IN).isPresent() && driverTools.waitForElementToPresence(CAMPAIGN_TIMER).isPresent();
        boolean isProductCardValid = checkCampaignProductCard();

        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.clickBack();
        webViewPage.switchToNative();

        boolean isSeeAllButtonValid = checkCampaignSeeAllBtn();

        return isCountDownPresent && isProductCardValid && isSeeAllButtonValid;
    }

    private boolean checkCampaignSeeAllBtn() {
        TTDSearchResultPage<T> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        driverTools.swipeToElement(SEE_ALL_BUTTON, Swipe.UP, 5);
        driverTools.click(SEE_ALL_BUTTON);

        boolean checkRedirectedToSRP = ttdSearchResultPage.isSearchResultPageShown();
        boolean checkGeneralFilterAppliedOnSRP = ttdSearchResultPage.isFilterActivated("general");
        boolean checkCampaignLabelPresentOnSRP = ttdSearchResultPage.isCampaignLabelPresent();

        if(!checkRedirectedToSRP) {
            logger.fail("SRP not displayed");
        } else if (!checkGeneralFilterAppliedOnSRP) {
            logger.fail("General Filter not showing a dot");
        } else if (!checkCampaignLabelPresentOnSRP) {
            logger.fail("There is no Campaign label");
        }

        return checkRedirectedToSRP && checkGeneralFilterAppliedOnSRP && checkCampaignLabelPresentOnSRP;
    }

    private boolean checkCampaignProductCard() {
        WebViewPage<T> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<T> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        List<WebElement> productCardTitleElementList = driverTools.elements(CAMPAIGN_PRODUCT_CARD_TITLE).orElseThrow();
        WebElement targetProductCardTitle = productCardTitleElementList.get(CoreFunctions.generateRandomInt(0,productCardTitleElementList.size()-1));

        String productCardTitle = driverTools.getValueAttribute(targetProductCardTitle, "label").toLowerCase();
        driverTools.click(targetProductCardTitle);
        webViewPage.switchToWebViewUrl("to-do");
        String productTitlePDP = ttdProductDetailPageWeb.getProductName().toLowerCase();
        webViewPage.switchToNative();

        Assertion.assertThat("Assert if the campaign section product card is valid", productTitlePDP, is(productCardTitle));
        return true;
    }

    private boolean checkBannerSection() {
        driverTools.swipeToElement(FIRST_BANNER_IMAGE_TITLE, Swipe.UP, 20);
        boolean firstCheckResult = checkBannerCard();
        return firstCheckResult && checkBannerCTA();
    }

    private boolean checkBannerCTA() {
        String productTitleBanner1 = driverTools.getValueAttribute(FIRST_BANNER_IMAGE_TITLE, "label").toLowerCase();
        String selectedChipsBanner1 = driverTools.getValueAttribute(SELECTED_BANNER_SECTION_CHIP, "label").toLowerCase();

        List<WebElement> chipTextElementList = driverTools.elements(FIRST_BANNER_SECTION_CHIPS).orElseThrow();
        driverTools.click(chipTextElementList.get(CoreFunctions.generateRandomInt(1,chipTextElementList.size()-1)));
        driverTools.waitForElementToAppear(FIRST_BANNER_IMAGE_TITLE);

        String productTitleBanner2 = driverTools.getValueAttribute(FIRST_BANNER_IMAGE_TITLE, "label").toLowerCase();
        String selectedChipsBanner2 = driverTools.getValueAttribute(SELECTED_BANNER_SECTION_CHIP, "label").toLowerCase();

        Assertion.assertThat("Assert if product title on banner card is changed after clicking cta", productTitleBanner1, not(productTitleBanner2));
        Assertion.assertThat("Assert if the chip selected on banner section is changed after clicking cta", selectedChipsBanner1, not(selectedChipsBanner2));
        return true;
    }

    private boolean checkBannerCard() {
        WebViewPage<T> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<T> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String productTitleBanner = driverTools.getValueAttribute(FIRST_BANNER_IMAGE_TITLE, "label").toLowerCase();
        driverTools.click(FIRST_BANNER_IMAGE_TITLE);

        TTDSearchResultPage<T> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        boolean isRedirectedToSRP = ttdSearchResultPage.isSearchResultPageShown();
        if(isRedirectedToSRP) {
            Assertion.assertThat("Assert if the banner redirected to SRP", isRedirectedToSRP, is(true));
            ttdSearchResultPage.clickBack();
        } else {
            webViewPage.switchToWebViewUrl("to-do");
            String productTitlePDP = ttdProductDetailPageWeb.getProductName().toLowerCase();
            Assertion.assertThat("Assert if product title on PDP is the same as Banner Card", productTitlePDP, is(productTitleBanner));
            ttdProductDetailPageWeb.clickBack();
            webViewPage.switchToNative();
        }

        return true;
    }

    private Locator getToDoCategoryByTitle(String param) {
        return new Locator("TO_DO_CATEGORY_"+param.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeImage[@name='product_category_name' and @label='"+param+"']"));
    }

    @Override
    public void clickBack() {
        driverTools.click(BACK_BUTTON);
    }
}
