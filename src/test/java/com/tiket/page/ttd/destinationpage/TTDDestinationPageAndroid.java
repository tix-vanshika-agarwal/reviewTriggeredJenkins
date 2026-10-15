package com.tiket.page.ttd.destinationpage;

import com.tiket.appium.Swipe;
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
import java.util.NoSuchElementException;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class TTDDestinationPageAndroid <T extends WebDriver> extends TTDDestinationPage<T> {

    public static final Locator NAME_DESTINATIONPAGE = new Locator("NAME_DESTINATIONPAGE", AppiumBy.id("tv_description"));
    public static final Locator DESTINATION_PAGE_NAME_BUTTON = new Locator("DESTINATION_PAGE_NAME_BUTTON", AppiumBy.id("iv_arrow_down"));
    public static final Locator TYPE_DESTINATIONPAGE = new Locator("TYPE_DESTINATIONPAGE", AppiumBy.xpath("//*[contains(@resource-id,'cl_homeheader_container')]//*[contains(@resource-id,'tv_country')]"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("(//*[contains(@resource-id,'appbar_back_icon') or contains(@resource-id,'tds_icon_navigation_toolbar') and @displayed='true'])[1]"));
    private static final Locator ACCESS_LOCATION_LATER = new Locator("ACCESS_LOCATION_LATER", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (@text='Nanti aja' or @text='Maybe later')]"));
    private static final Locator TO_DO_CATEGORY = new Locator("TO_DO_CATEGORY", AppiumBy.xpath("//*[contains(@resource-id,'cl_') and contains(@resource-id,'_category')]"));
    public static final Locator FIRST_BANNER_IMAGE_TITLE = new Locator("FIRST_BANNER_IMAGE_TITLE", AppiumBy.xpath("(//android.view.ViewGroup[./*[contains(@resource-id,'view_gradient')]])[1]/*[contains(@resource-id,'tv_title')]"));
    public static final Locator FIRST_BANNER_SECTION_CHIPS = new Locator("FIRST_BANNER_SECTION_CHIPS", AppiumBy.xpath("(//android.view.ViewGroup[./*[contains(@resource-id,'chip_group')] and .//*[contains(@resource-id,'view_gradient')]])[1]//*[contains(@resource-id,'tds_chips_text')]"));
    public static final Locator CAMPAIGN_ENDS_IN = new Locator("CAMPAIGN_ENDS_IN", AppiumBy.id("tv_countdown_label"));
    public static final Locator CAMPAIGN_TIMER = new Locator("CAMPAIGN_TIMER", AppiumBy.id("countdown"));
    public static final Locator CAMPAIGN_PRODUCT_CARD_TITLE = new Locator("CAMPAIGN_PRODUCT_CARD_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'content') and ./*[contains(@resource-id,'tv_countdown_label')]]//*[contains(@resource-id,'recycler_view')]//*[contains(@resource-id,'tv_title')]"));
    public static final Locator SEE_ALL_BUTTON_CAMPAIGN = new Locator("SEE_ALL_BUTTON_CAMPAIGN", AppiumBy.xpath("//*[contains(@resource-id,'content') and ./*[contains(@resource-id,'tv_countdown_label')]]//*[contains(@resource-id,'btn_see_all')]"));
    public static final Locator SEE_ALL_BUTTON_NON_CAMPAIGN = new Locator("SEE_ALL_BUTTON_NON_CAMPAIGN", AppiumBy.xpath("//*[contains(@resource-id,'constraint_root') and not(./*[contains(@resource-id,'countdown')])]/*[contains(@resource-id,'btn_see_all')]"));

    public TTDDestinationPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public boolean isDestinationPageValid(String keyword, String type) {
        driverTools.waitForElementToAppear(DESTINATION_PAGE_NAME_BUTTON,10);
        boolean nameDestinationElementDisplayed = driverTools.isElementDisplayed(DESTINATION_PAGE_NAME_BUTTON);
        String nameLocationText = driverTools.getText(NAME_DESTINATIONPAGE).orElseThrow();

        logger.log("Is DESTINATION_PAGE_NAME_BUTTON displayed? " + nameDestinationElementDisplayed);
        logger.log("values are: " + nameLocationText + " and " + type.replace("Optional[", "").replace("]", ""));

        if (type.equals("-")) {
            return nameDestinationElementDisplayed;
        }
        return nameDestinationElementDisplayed && nameLocationText.equals(type.replace("Optional[", "").replace("]", ""));
    }

    @Override
    public String getDestinationName() {
        if(driverTools.isElementDisplayed(ACCESS_LOCATION_LATER)) {
            driverTools.click(ACCESS_LOCATION_LATER);
        }

        return driverTools.waitForElementToAppear(NAME_DESTINATIONPAGE).orElseThrow().getText();
    }

    @Override
    public String getDestinationType(String type) {
        return driverTools.waitForElementToAppear(TYPE_DESTINATIONPAGE).orElseThrow().getText();
    }

    @Override
    public void chooseCategory(String type, String param) {
        WebElement categoryElement;

        driverTools.waitForElementToAppear(TO_DO_CATEGORY);

        switch (type) {
            case "random":
            case "index":
                List<WebElement> listOfCategory = driverTools.elements(TO_DO_CATEGORY).orElseThrow();

                categoryElement = param.equals("-") ? listOfCategory.get(generateRandomInt(0,listOfCategory.size()-1)) : listOfCategory.get(Integer.parseInt(param));
                driverTools.click(categoryElement);

                break;
            case "title":
                //TODO: Implement Method
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

    @Override
    public void clickBack() {
        driverTools.click(BACK_BUTTON);
    }

    private boolean checkAllCTASection() {
        TTDSearchResultPage<T> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        WebElement element = driverTools.swipeToElement(SEE_ALL_BUTTON_NON_CAMPAIGN,Swipe.UP,10).orElseThrow();
        driverTools.click(element);

        return ttdSearchResultPage.isSearchResultPageShown();
    }

    private boolean checkCampaignSection() {
        WebViewPage<T> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<T> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        driverTools.swipeToElement(CAMPAIGN_TIMER,Swipe.UP,3);
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

        driverTools.swipeToElement(SEE_ALL_BUTTON_CAMPAIGN, Swipe.UP, 5);
        driverTools.click(SEE_ALL_BUTTON_CAMPAIGN);

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

        driverTools.swipeToElement(CAMPAIGN_PRODUCT_CARD_TITLE, Swipe.UP, 5);
        List<WebElement> productCardTitleElementList = driverTools.elements(CAMPAIGN_PRODUCT_CARD_TITLE).orElseThrow();
        WebElement targetProductCardTitle = productCardTitleElementList.get(generateRandomInt(0,productCardTitleElementList.size()-1));

        String productCardTitle = driverTools.getText(targetProductCardTitle).toLowerCase();
        driverTools.click(targetProductCardTitle);
        webViewPage.switchToWebViewUrl("to-do");
        String productTitlePDP = ttdProductDetailPageWeb.getProductName().toLowerCase();
        webViewPage.switchToNative();

        Assertion.assertThat("Assert if the campaign section product card is valid", productTitlePDP, is(productCardTitle));
        return true;
    }

    private boolean checkBannerSection() {
        driverTools.swipeToElement(FIRST_BANNER_IMAGE_TITLE, Swipe.UP, 20);
        boolean bannerCTACheckResult = checkBannerCTA();
        boolean bannerCardCheckResult = checkBannerCard();

        return bannerCTACheckResult && bannerCardCheckResult;
    }

    private boolean checkBannerCTA() {
        String productTitleBanner1 = driverTools.getText(FIRST_BANNER_IMAGE_TITLE).orElseThrow().toLowerCase();
        String selectedChipsBanner1 = driverTools.getText(getSelectedBannerChip(1)).orElseThrow().toLowerCase();

        List<WebElement> chipTextElementList = driverTools.elements(FIRST_BANNER_SECTION_CHIPS).orElseThrow();
        int randomInt = generateRandomInt(1,chipTextElementList.size()-1);
        driverTools.click(chipTextElementList.get(randomInt));
        driverTools.waitForElementToAppear(FIRST_BANNER_IMAGE_TITLE);

        String productTitleBanner2 = driverTools.getText(FIRST_BANNER_IMAGE_TITLE).orElseThrow().toLowerCase();
        String selectedChipsBanner2 = driverTools.getText(getSelectedBannerChip(randomInt+1)).orElseThrow().toLowerCase();

        Assertion.assertThat("Assert if product title on banner card is changed after clicking cta", productTitleBanner1, not(productTitleBanner2));
        Assertion.assertThat("Assert if the chip selected on banner section is changed after clicking cta", selectedChipsBanner1, not(selectedChipsBanner2));
        return true;
    }

    private Locator getSelectedBannerChip(int index) {
        return new Locator("SELECTED_BANNER_SECTION_CHIP", AppiumBy.xpath("((//android.view.ViewGroup[./*[contains(@resource-id,'chip_group')] and .//*[contains(@resource-id,'view_gradient')]])[1]//*[contains(@resource-id,'tds_chips_text')])["+index+"]"));
    }

    private boolean checkBannerCard() {
        WebViewPage<T> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<T> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String productTitleBanner = driverTools.getText(FIRST_BANNER_IMAGE_TITLE).orElseThrow().toLowerCase();
        driverTools.click(FIRST_BANNER_IMAGE_TITLE);

        try {
            webViewPage.switchToWebViewUrl("to-do");
            String productTitlePDP = ttdProductDetailPageWeb.getProductName().toLowerCase();
            Assertion.assertThat("Assert if product title on PDP is the same as Banner Card", productTitlePDP, is(productTitleBanner));
            webViewPage.switchToNative();
        } catch (NoSuchElementException e) {
            TTDSearchResultPage<T> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
            Assertion.assertThat("Assert if the banner redirected to SRP", ttdSearchResultPage.isSearchResultPageShown(), is(true));
        }

        return true;
    }
}
