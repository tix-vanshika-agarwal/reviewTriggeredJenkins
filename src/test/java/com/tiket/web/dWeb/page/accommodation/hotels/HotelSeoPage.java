package com.tiket.web.dWeb.page.accommodation.hotels;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.home.HomePageWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * Base page object for hotel SEO landing pages.
 *
 * <p>This is an abstract base that holds the shared (currently dWeb-oriented)
 * implementation. Platform-specific behaviour lives in {@link HotelSeoPageDWeb}
 * and {@link HotelSeoPageMweb}. Tests must obtain an instance through
 * {@link #getInstance(Session)} so the correct platform subclass is returned
 * based on {@code Configs.PLATFORM}.</p>
 *
 * <p>For now every method is implemented here and inherited as-is by both
 * subclasses. When a method needs to differ per platform, override it in the
 * relevant subclass.</p>
 */
public abstract class HotelSeoPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator SEO_SIDE_PANEL = new Locator("SEO_SIDE_PANEL", By.xpath("//div[@data-testid='side-panel']"));
    private static final Locator SEO_PAGE_HEADING = new Locator("SEO_PAGE_HEADING", By.xpath("//*[@data-testid='location']"));
    private static final Locator SEO_FAQ_SECTION = new Locator("SEO_FAQ_SECTION", By.xpath("//*[contains(@class,'Faq') or contains(@class,'FAQ') or contains(@class,'faq_container')][.//*[normalize-space()='Frequently Asked Questions' or contains(normalize-space(),'Pertanyaan') or contains(normalize-space(),'FAQ')]]"));
    private static final Locator SEO_FAQ_ITEM = new Locator("SEO_FAQ_ITEM", By.xpath("//*[contains(@class,'Faq') or contains(@class,'FAQ')]//*[contains(@class,'FAQEntity_faq_item')]//p[contains(@class,'Accordion_text_truncate_accessibility')]"));
    private static final Locator FIRST_SEO_CARD_FULL_STARS = new Locator("FIRST_SEO_CARD_FULL_STARS", By.xpath("(//div[@data-testid='property-star-rating-container'])[1]//*[local-name()='svg' and @data-testid='hotel-star-rating-full']"));

    public HotelSeoPage(Session<T> session) {
        super(session);
    }

    /**
     * Returns the platform-specific {@link HotelSeoPage} implementation based on
     * {@code Configs.PLATFORM}.
     */
    public static <T extends WebDriver> HotelSeoPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HotelSeoPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new HotelSeoPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }
    public String buildSeoUrl(String slug) {
        String baseUrl;
        if (Configs.ENVIRONMENT == Environment.GK) {
            baseUrl = HomePageWeb.STAGING_WEB_URL;
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            baseUrl = HomePageWeb.PROD_WEB_URL;
        } else {
            baseUrl = HomePageWeb.PREPROD_WEB_URL;
        }
        String normalized = slug.startsWith("/") ? slug.substring(1) : slug;
        return baseUrl + "hotel/" + normalized;
    }

    public void openSeoPage(String slug) {
        String url = buildSeoUrl(slug);
        logger.log("Opening hotel SEO page: " + url);
        driverTools.getWithDynamicTimeout(url, 60);
        driverTools.waitForWebToLoad();
    }

    public String getUrl() {
        return driverTools.getCurrentURL();
    }

    public String getPageHeading() {
        WebElement heading = driverTools.waitForElementToAppear(SEO_PAGE_HEADING, 20).orElseThrow();
        return driverTools.getText(heading);
    }

    public boolean isSidePanelVisible() {
        return driverTools.waitForElementToAppear(SEO_SIDE_PANEL, 15).isPresent()
                && driverTools.isElementDisplayed(SEO_SIDE_PANEL);
    }

    public boolean isHotelsInSectionVisible(String locationName) {
        Locator loc = getHotelsInSectionLocator(locationName);
        return driverTools.waitForElementToAppear(loc, 10).isPresent()
                && driverTools.isElementDisplayed(loc);
    }

    public boolean isLandmarksSectionVisible() {
        Locator loc = getSidePanelSectionByTextLocator(
                "Popular Tourist Attractions","Tempat Wisata Populer"
        );

        driverTools.scrollIntoWebView(loc);

        return driverTools.waitForElementToAppear(loc, 10).isPresent()
                && driverTools.isElementDisplayed(loc);
    }

    public boolean isStarRatingSectionVisible() {
        Locator loc = getStarRatingSectionLocator();
        driverTools.scrollIntoWebView(loc);

        return driverTools.waitForElementToAppear(loc, 10).isPresent()
                && driverTools.isElementDisplayed(loc);
    }

    protected Locator getStarRatingSectionLocator() {
        return new Locator("STAR_RATING_SECTION", By.xpath(sidePanelRoot() + "//h2[contains(text(),'Star') or contains(text(),'Berbintang')]/ancestor::*[@data-testid='side-panel-item'][1]"));
    }

    public boolean isFaqVisible() {
        for (int retry = 0; retry < 3; retry++) {
            try {
                if (driverTools.waitForElementToAppear(SEO_FAQ_SECTION, 10).isPresent()) {
                    driverTools.scrollIntoWebView(SEO_FAQ_SECTION);

                    if (driverTools.isElementDisplayed(SEO_FAQ_SECTION)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                logger.softWarning("FAQ visibility check failed. Retry: " + (retry + 1) + " — " + e);
            }
        }
        return false;
    }
    public int getFaqItemCount() {
        return getFaqItemTexts().size();
    }

    public List<String> getFaqItemTexts() {
        driverTools.scrollIntoWebView(SEO_FAQ_SECTION);
        driverTools.waitForElementToAppear(SEO_FAQ_SECTION, 10).orElseThrow();
        List<String> faqItemTexts = driverTools.getTexts(SEO_FAQ_ITEM);
        logger.log("Found " + faqItemTexts.size() + " FAQ items: " + faqItemTexts);
        return faqItemTexts;
    }

 // Verifies that at least one FAQ question mentions the given location name, confirming
    public boolean isLocationNameShownInFaq(String locationName) {
        String safeName = locationName == null ? "" : locationName.trim();
        List<String> faqItemTexts = getFaqItemTexts();
        return faqItemTexts.stream().anyMatch(text -> text.contains(safeName));
    }

    public int getFirstSeoCardStarRating() {
        driverTools.waitForElementToAppear(FIRST_SEO_CARD_FULL_STARS, 15).orElseThrow();
        driverTools.scrollIntoWebView(FIRST_SEO_CARD_FULL_STARS);
        int starCount = driverTools.elements(FIRST_SEO_CARD_FULL_STARS).map(List::size).orElse(0);
        logger.log("First SEO card star rating (full stars): " + starCount);
        return starCount;
    }

    public String clickFirstLinkInHotelsInSection(String locationName) {
        String sectionXpath = hotelsInSectionXpath(locationName);
        return clickFirstLinkInSection(sectionXpath, "Hotels in " + locationName);
    }

    public String clickFirstLandmarkLink() {
        String sectionXpath = sidePanelSectionXpath(
                "Popular", "Populer", "Landmarks", "Landmark");
        return clickFirstLinkInSection(sectionXpath, "Landmark");
    }

    public String clickFirstStarRatingLink() {
        String sectionXpath = sidePanelSectionXpath(
                "Hotels by Star Rating", "Hotel Berdasarkan Bintang", "Star Rating", "Bintang","Star-Rated","Hotel Berbintang");
        return clickFirstLinkInSection(sectionXpath, "Star Rating");
    }

    public int getHotelsInSectionLinkCount(String locationName) {
        Locator section = getHotelsInSectionLocator(locationName);
        if (driverTools.waitForElementToAppear(section, 10).isEmpty()) {
            return 0;
        }
        WebElement sectionEl = driverTools.element(section).orElse(null);
        if (sectionEl == null) {
            return 0;
        }
        return sectionEl.findElements(By.xpath(".//a")).size();
    }

    protected Locator getHotelsInSectionLocator(String locationName) {
        String safeName = locationName == null ? "" : locationName.trim();
        return new Locator("SEO_HOTELS_IN_" + safeName.toUpperCase(),
                By.xpath(hotelsInSectionXpath(safeName)));
    }

    protected Locator getSidePanelSectionByTextLocator(String... possibleHeadings) {
        return new Locator(
                "SEO_SECTION_" + possibleHeadings[0].toUpperCase().replace(' ', '_'),
                By.xpath(sidePanelSectionXpath(possibleHeadings)));
    }

    protected String hotelsInSectionXpath(String locationName) {
        String safeName = locationName == null ? "" : locationName.trim();
        String enLabel = ("Hotels in " + safeName);
        String idLabel = ("Hotel di Daerah " + safeName);
        return "//*[@data-testid='side-panel-item'][.//h2[contains(text(),'"+enLabel+"') or contains(text(),'"+idLabel+"')]]";
    }

    protected String sidePanelSectionXpath(String... possibleHeadings) {
        StringBuilder condition = new StringBuilder();

        for (int i = 0; i < possibleHeadings.length; i++) {
            if (i > 0) condition.append(" or ");
            condition.append("contains(text(),'")
                    .append(possibleHeadings[i])
                    .append("')");
        }

        return sidePanelRoot()
                + "//h2[" + condition + "]"
                + "/ancestor::*[@data-testid='side-panel-item'][1]";
    }

    protected String sidePanelRoot() {
        return "//div[contains(@class,'SidePanel') or contains(@class,'SeoSidePanel') or contains(@class,'side_panel')]";
    }

    protected String clickFirstLinkInSection(String sectionXpath, String label) {
        Locator firstLink = new Locator("SEO_FIRST_LINK_" + label.toUpperCase().replace(' ', '_'),
                By.xpath("(" + sectionXpath + "//a)[1]"));
        WebElement link = driverTools.waitForElementToAppear(firstLink, 15).orElseThrow();
        driverTools.scrollIntoWebView(link);
        String linkText = driverTools.getText(link);
        logger.log("Clicking SEO side-panel link under '" + label + "': '" + linkText + "'");
        driverTools.clickWithJavascriptExecutor(link);
        driverTools.waitForWebToLoad();
        return linkText;
    }
}
