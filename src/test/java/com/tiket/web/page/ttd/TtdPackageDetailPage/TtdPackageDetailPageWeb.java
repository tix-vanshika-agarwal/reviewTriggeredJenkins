package com.tiket.web.page.ttd.TtdPackageDetailPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class TtdPackageDetailPageWeb<T extends WebDriver> extends WebPage<T> {

    public TtdPackageDetailPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TtdPackageDetailPageWeb<T> getInstance(Session<T> session) {
        return new TtdPackageDetailPageWeb<>(session);
    }

    public static final Locator PACKAGE_NAME = new Locator("PRODUCT_NAME", AppiumBy.xpath("//*[contains(@class,'NewPackageDetailModal_info_container')]//h3"));
    private static final Locator CROSS_PRICE = new Locator("CROSS_PRICE", AppiumBy.xpath("//*[contains(@class,'PackageDetailPriceFooter_container')]//*[contains(@style,'line-through')]"));
    private static final Locator FINAL_PRICE = new Locator("FINAL_PRICE", AppiumBy.xpath("//*[contains(@class,'PackageDetailPrice_price')]/div"));
    private static final Locator CAMPAIGN_LABEL = new Locator("CAMPAIGN_LABEL", AppiumBy.xpath("//*[contains(@class,'PackageDetailPriceFooter_container')]//*[contains(@class,'Label_label')]"));
    public static final Locator VALID_DAY_TEXT= new Locator("VALID_DAY_TEXT", AppiumBy.xpath("//div[contains(@class,'PackageDetailBenefit_item_label')]//*[contains(text(),'Berlaku') or contains(text(),'Valid')]"));
    private static final Locator SELECT_TICKET_BUTTON = new Locator("SELECT_TICKET_BUTTON", AppiumBy.xpath("//*[@data-testid='package-card-footer']//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator SELECT_TICKET_FROM_DETAIL = new Locator("SELECT_TICKET_FROM_DETAIL", AppiumBy.xpath("//*[contains(@class,'PackageDetailPriceFooter_container')]//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator LOYALTY_BADGE = new Locator("LOYALTY_BADGE", AppiumBy.xpath("//*[contains(@class,'SectionLoyalty_section_container')]//*[contains(@data-testid,'loyalty-icon')]"));
    private static final Locator LOYALTY_BADGE_DETAILS_BTN = new Locator("LOYALTY_BADGE_DETAILS_BTN", AppiumBy.xpath("//*[contains(@data-testid, 'loyalty-icon')]//parent::*[contains(@class,'BenefitSection_card_title_icon')]"));
    private static final Locator LOYALTY_BADGE_DETAILS = new Locator("LOYALTY_BADGE_DETAILS", AppiumBy.xpath("//*[contains(@class,'PopupBenefit_label')]//*[contains(text(),'Benefit')]"));
    private static final Locator CLOSE_BTN = new Locator("CLOSE_BTN", AppiumBy.xpath("//button[contains(@class, 'BottomSheet_close_button')]"));

    public boolean packageDetailShown() {
        boolean result = driverTools.waitForElementToAppear(SELECT_TICKET_BUTTON).isPresent();
        return result;
    }

    public String getPackageName() {
        WebElement webElement = driverTools.waitForElementToPresence(PACKAGE_NAME).orElseThrow();
        String result = driverTools.getText(webElement);
        return result;
    }

    public String getCrossPrice() {
        Optional<WebElement> crossPrice = driverTools.waitForElementToPresence(CROSS_PRICE);
        String result = "";

        if (crossPrice.isPresent()) {
            result = driverTools.getText(crossPrice.orElseThrow());
        } else {
            result = "";
        }
        return result;
    }

    public String getFinalPrice() {
        WebElement finalPrice = driverTools.waitForElementToPresence(FINAL_PRICE).orElseThrow();
        String result = driverTools.getText(finalPrice);
        return result;
    }

    public String getCampaignLabel() {
        Optional<WebElement> campaignLabel = driverTools.waitForElementToPresence(CAMPAIGN_LABEL);
        String result = "";

        if (campaignLabel.isPresent()) {
            result = driverTools.getText(campaignLabel.orElseThrow());
        } else {
            result = "";
        }
        return result;
    }

    public void clickSelectTicketFromDetail() {
        driverTools.waitForElementToAppear(SELECT_TICKET_FROM_DETAIL);
        driverTools.click(SELECT_TICKET_FROM_DETAIL);
    }

    public String getValidDayText() {
        String result = driverTools.getText(VALID_DAY_TEXT).orElseThrow();
        return result;
    }

    public boolean verifyAndClickLoyaltyBadge(){
        driverTools.scrollIntoWebView(LOYALTY_BADGE);
        boolean isLoyaltyBadgeDisplayed = driverTools.isElementDisplayed(LOYALTY_BADGE);
        return isLoyaltyBadgeDisplayed;
    }

    public boolean verifyAndClickLoyaltyDetails(){
        driverTools.scrollIntoWebView(LOYALTY_BADGE_DETAILS_BTN);
        driverTools.clickWithJavascriptExecutor(LOYALTY_BADGE_DETAILS_BTN);
        boolean isLoyaltyBadgeDisplayed = driverTools.isElementDisplayed(LOYALTY_BADGE_DETAILS);
        driverTools.click(CLOSE_BTN);
        return isLoyaltyBadgeDisplayed;
    }
}
