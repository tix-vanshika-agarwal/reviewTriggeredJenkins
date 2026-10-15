package com.tiket.web.page.ttd.TtdProductDetailPage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.SortItem;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static org.hamcrest.core.Is.is;

public class TtdProductDetailPageMweb<T extends WebDriver> extends TtdProductDetailPageWeb<T> {

    public TtdProductDetailPageMweb(Session<T> session) {
        super(session);
    }

    private static final Locator PACKAGE_GROUPING_NAVBAR = new Locator("PACKAGE_GROUPING_NAME_NAVBAR", new By.ByXPath("//*[contains(@class,'FloatingNavMobile_package_group_nav')]//*[contains(@class,'FloatingNavMobile_pkg_group_name_wrapper')]"));
    private static final Locator PACKAGE_GROUPING_NAME = new Locator("PACKAGE_GROUPING_NAME", new By.ByXPath("//*[contains(@data-testid, 'package-group-button') and (contains(text(), 'Seating products') or contains(text(), 'Produk seating'))]"));
    private static final Locator GROUP_NAME = new Locator("GROUP_NAME", new By.ByXPath("//*[contains(@class, 'PackageGroup_heading_section')]//h2[contains(text(), 'Seating products') or contains(text(), 'Produk seating')]"));
    private static final Locator SHARE_BUTTON = new Locator("SHARE_BUTTON", new By.ByXPath("//button[@aria-label='share-button']"));
    private static final Locator SHARE_COPY_LINK = new Locator("SHARE_COPY_LINK", new By.ByXPath("//button[contains(@class, 'copy-to-text-container')]"));
    private static final Locator SNACKBAR_LINK_COPIED = new Locator("SNACKBAR_LINK_COPIED", new By.ByXPath("//*[contains(@class, 'Snackbar_text_content') and (contains(text(), 'Link has been copied.') or contains(text(), 'Tautan berhasil disalin.'))]"));
    private static final Locator LPG_LABEL_PDP = new Locator("LPG_LABEL_PDP", new By.ByXPath("//*[@data-testid='banner-lpg']"));
    private static final Locator SEE_PACKAGES = new Locator("SEE_PACKAGES", new By.ByXPath("//div[contains(@class, 'Text_text__') and (text()='Select Ticket' or text()='Pilih Tiket')n"));
    private static final Locator SELECT_TICKET_BUTTON = new Locator("SELECT_TICKET_BUTTON", AppiumBy.xpath("//*[contains(@class,'PackageCollapse_card_list_wrapper')]//ancestor::*[contains(@class, 'Card_card_header')]//following-sibling::*[contains(@class, 'PackageCard_package_footer')]//*[contains(@class,'Button_variant_primary') and not(contains(@class,'disabled'))]"));
    private static final Locator CLOSE_DATE = new Locator("CLOSE_DATE", By.xpath("//button[contains(@class, 'Modal_modal_close')]"));
    private static final Locator TIME_SLOT = new Locator("TIME_SLOT", new By.ByXPath("(//button[contains(@class,'Chip_chip')])[1]"));
    private static final Locator VALIDITY_PERIOD = new Locator("VALIDITY_PERIOD", AppiumBy.xpath("//*[contains(text(),'Validity period:') or contains(text(),'Masa berlaku')]"));
    private static final Locator BUY_TICKET_BUTTON = new Locator("BUY_TICKET_BUTTON", By.xpath("//button[contains(@class, 'Button_size_large')]"));
    private static final Locator PROMO_BENEFITS_FOOTER = new Locator("PROMO_BENEFITS_FOOTER", AppiumBy.xpath("//*[contains(@class,'BottomSheet_body_title_w')]"));

    @Override
    protected boolean isPromoBenefitsPopupDisplayed() {
        return driverTools.isElementDisplayed(PROMO_BENEFITS_FOOTER);
    }

    @Override
    public void validateGroupNavigation () throws Exception {
        driverTools.swipeUntilElementVisible(PACKAGE_GROUPING_NAVBAR, Swipe.UP, 5);
        driverTools.click(PACKAGE_GROUPING_NAVBAR);
        String clickedPackageGroupName = driverTools.getText(PACKAGE_GROUPING_NAME).orElseThrow();
        driverTools.click(PACKAGE_GROUPING_NAME);
        driverTools.waitForElementToAppear(GROUP_NAME);
        String groupPackageName = driverTools.getText(GROUP_NAME).orElseThrow();

        Assertion.assertThat("Displayed package group is same with on selected group from navbar",
                clickedPackageGroupName, is(groupPackageName));
    }

    public void clickShareButton(){
        driverTools.click(SHARE_BUTTON);
        driverTools.waitForElementToAppear(SHARE_COPY_LINK);
        driverTools.click(SHARE_COPY_LINK);

        WebElement element = driverTools.waitForElementToAppear(SNACKBAR_LINK_COPIED, 10).orElseThrow();

        Assertion.assertThat("Copy link snackbar is present", driverTools.isElementDisplayed(element), is(true));
    }

    @Override
    public boolean isLPGLabelPresentOnPDP () {
        driverTools.waitForElementsToAppear(LPG_LABEL_PDP);
        return driverTools.waitForElementsToAppear(LPG_LABEL_PDP).isPresent();
    }

    @Override
    public void clickSeePackagesButton () {
        if (driverTools.waitForElementToAppear(SEE_PACKAGES).isPresent()) {
            driverTools.click(SEE_PACKAGES);
        } else {
            logger.log("Button did not appear");
        }
    }

    @Override
    @SneakyThrows
    public void chooseTicket(String packageName) {
        driverTools.waitForElementToAppear(SELECT_TICKET_BUTTON);
        WebElement element;
        if (packageName.equals(String.valueOf(SortItem.RANDOM))) {
            List<WebElement> webElementList = driverTools.elements(SELECT_TICKET_BUTTON).orElseThrow();
            logger.log("Total package: " + webElementList.size());
            int randomInt = generateRandomInt(0, webElementList.size() - 1);
            element = webElementList.get(randomInt);
        } else {
            driverTools.scroll(2, 5);
            driverTools.scrollIntoWebView(getSelectTicket(packageName));
            Locator selectTicketLocator = getSelectTicket(packageName);
            element = driverTools.element(selectTicketLocator).orElseThrow();
        }
        driverTools.waitForElementToAppear(element);
        driverTools.waitForElementToBeClickable(element);
        driverTools.clickWithJavascriptExecutor(element);
        if(driverTools.waitForElementToAppear(CLOSE_DATE, 5).isPresent()) {
            driverTools.click(CLOSE_DATE);
        }
    }

    @Override
    public String getValidityPeriodText () {
        driverTools.waitForElementToPresence(VALIDITY_PERIOD);
        return driverTools.getText(VALIDITY_PERIOD).orElseThrow();
    }

    private Locator getSelectTicket(String packageName) {
        String titleCondition;
        if (packageName != null && packageName.contains("/")) {
            // Format "English/Bahasa Indonesia" -> match either side, e.g. "Adult/Dewasa"
            String[] parts = packageName.split("/", 2);
            String en = parts[0].trim();
            String id = parts[1].trim();
            titleCondition = "(contains(text(),'" + en + "') or contains(text(),'" + id + "'))";
        } else {
            titleCondition = "contains(text(),'" + packageName + "')";
        }
        return new Locator("SELECT_TICKET_BUTTON_"+packageName, AppiumBy.xpath("//*[contains(@class,'package_title') and " + titleCondition + "]//ancestor::*[contains(@class, 'Card_card_header')]//following-sibling::*[contains(@class, 'PackageCard_package_footer')]//*[not(@disabled) and contains(@class,'Button_variant_primary')]"));
    }

    public void setTimeslot () {
        driverTools.waitForElementToPresence(TIME_SLOT);
        driverTools.waitForElementToBeClickable(TIME_SLOT, 20);
        driverTools.clickWithJavascriptExecutor(TIME_SLOT);
        Assertion.assertThat("Time slot is present on the selected date", driverTools.isElementDisplayed(TIME_SLOT), is(true));
    }

    @Override
    public void clickBuyTicketButton() {
        driverTools.waitForElementToPresence(BUY_TICKET_BUTTON);
        driverTools.clickWithJavascriptExecutor(BUY_TICKET_BUTTON);
    }

    private Locator getLoyaltyEligibility(String packageName) {
        return new Locator("CHECK_LOYALTY_TIER_"+packageName, AppiumBy.xpath("//*[contains(@class,'package_title') and contains(text(),'"+packageName+"')]//ancestor::*[contains(@class,'Card_card_container')]//following-sibling::*[contains(@class, 'PackageCard_package_body')]//*[contains(@class, 'Text_variant_invert')]"));
    }

    private Locator labelPackageNotEligible(String packageName) {
        return new Locator("LABEL_NOT_ELIGIBLE_"+packageName, AppiumBy.xpath("//*[contains(@class,'package_title') and contains(text(),'"+packageName+"')]//ancestor::*[contains(@class,'Card_card_container')]//following-sibling::*[contains(@class, 'PackageCard_package_footer')]//*[contains(@class, 'PackageCard_no_available_package')]"));
    }

    public void checkEligiblePackage(String packageName, int maxTry) {
        driverTools.swipeToElement(getLoyaltyEligibility(packageName), Swipe.UP, maxTry);

        Assertion.assertThat("When user loyalty tier is lower than package loyalty requirement, user unable to access package",
                driverTools.isElementDisplayed(labelPackageNotEligible(packageName)), is(true));
    }

    private Locator getBxGyLabel(String packageName) {
        return new Locator("CHECK_BXGY_LABEL_"+packageName, AppiumBy.xpath("//*[contains(@class,'package_title') and contains(text(),'"+packageName+"')]//ancestor::*[contains(@class,'Card_card_container')]//*[contains(@class, 'PackageCard_package_header')]//*[contains(text(), 'Beli') or contains(text(), 'Buy')]"));
    }

    private Locator getBxGyNoQuota(String packageName) {
        return new Locator("CHECK_BXGY_QUOTA_"+packageName, AppiumBy.xpath("//*[contains(@class,'package_title') and contains(text(),'"+packageName+"')]//ancestor::*[contains(@class,'Card_card_container')]//*[contains(@class, 'PackageCard_package_header')]//*[text()='Kuota habis' or text()='Quota runs out']"));
    }

    public void verifyBxGyNoQuota(String packageName) {
        Assertion.assertThat("Check package has Buy X Get Y Promo",
                driverTools.isElementDisplayed(getBxGyLabel(packageName)), is(true));

        Assertion.assertThat("Check whether the Buy X Get Y quota is runs out",
                driverTools.isElementDisplayed(getBxGyNoQuota(packageName)), is(true));
    }
}
