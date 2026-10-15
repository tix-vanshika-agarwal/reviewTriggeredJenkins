package com.tiket.web.dWeb.page.accommodation.homes;

import com.tiket.core.CoreFunctions;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Base page object for Homes (NHA) SEO landing pages, e.g.
 * {@code https://preprod.tiket.com/en-id/homes/villa}.
 *
 * <p>This is an abstract base that holds the shared (currently dWeb-oriented)
 * implementation. Platform-specific behaviour lives in {@link HomesSeoPageDWeb}
 * and {@link HomesSeoPageMweb}. Tests must obtain an instance through
 * {@link #getInstance(Session)} so the correct platform subclass is returned
 * based on {@code Configs.PLATFORM}.</p>
 */
public abstract class HomesSeoPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator BREADCRUMB_WRAPPER = new Locator("BREADCRUMB_WRAPPER", By.cssSelector("[data-testid='breadcrumbs-content-wrapper']"));
    private static final Locator BREADCRUMB_HOMES_LINK = new Locator("BREADCRUMB_HOMES_LINK", By.cssSelector("[data-testid='breadcrumb-link-item']"));
    private static final Locator BREADCRUMB_ACTIVE_LABEL = new Locator("BREADCRUMB_ACTIVE_LABEL", By.cssSelector("[data-testid='breadcrumb-label-text-item']"));
    private static final Locator PAGE_TITLE = new Locator("PAGE_TITLE", By.cssSelector("[data-testid='location']"));
    private static final Locator HOTEL_COUNT = new Locator("HOTEL_COUNT", By.cssSelector("[data-testid='hotel-count']"));
    private static final Locator SEO_CARD_PROPERTY_LABELS = new Locator("SEO_CARD_PROPERTY_LABELS", By.xpath("//h3[contains(@class,'SeoCard_nha_property_label')]"));
    private static final Locator SEO_CARD_NAMES = new Locator("SEO_CARD_NAMES", By.xpath("//h3[contains(@class,'SeoCard_hotel_name')]"));

    public HomesSeoPage(Session<T> session) {
        super(session);
    }

    /**
     * Returns the platform-specific {@link HomesSeoPage} implementation based on
     * {@code Configs.PLATFORM}.
     */
    public static <T extends WebDriver> HomesSeoPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HomesSeoPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new HomesSeoPageMweb<>(session);
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
        return baseUrl + "en-id/homes/" + normalized;
    }

    public void openSeoPage(String slug) {
        String url = buildSeoUrl(slug);
        logger.log("Opening homes SEO page: " + url);
        driverTools.getWithDynamicTimeout(url, 60);
        driverTools.waitForWebToLoad();
    }

    /**
     * Converts a human-readable property name into its SEO URL slug, e.g.
     * {@code "Vacation Home" -> "vacation-home"}.
     */
    public String toSlug(String propertyName) {
        return propertyName == null ? "" : propertyName.trim().toLowerCase().replaceAll("\\s+", "-");
    }

    /**
     * Opens the SEO page for a property type using its display name (the slug is
     * derived automatically via {@link #toSlug(String)}).
     */
    public void openSeoPageByProperty(String propertyName) {
        openSeoPage(toSlug(propertyName));
    }

    public String getUrl() {
        return driverTools.getCurrentURL();
    }

    // --- Breadcrumb ---

    public String getBreadcrumbText() {
        WebElement wrapper = driverTools.waitForElementToAppear(BREADCRUMB_WRAPPER, 20).orElseThrow();
        return driverTools.getText(wrapper);
    }

    public String getBreadcrumbHomesLinkText() {
        WebElement link = driverTools.waitForElementToAppear(BREADCRUMB_HOMES_LINK, 15).orElseThrow();
        return driverTools.getText(link);
    }

    public String getBreadcrumbActiveLabel() {
        WebElement label = driverTools.waitForElementToAppear(BREADCRUMB_ACTIVE_LABEL, 15).orElseThrow();
        return driverTools.getText(label);
    }

    // --- Title ---

    public String getPageTitle() {
        WebElement title = driverTools.waitForElementToAppear(PAGE_TITLE, 20).orElseThrow();
        return driverTools.getText(title);
    }

    // --- Hotel count ---

    public String getHotelCountText() {
        WebElement count = driverTools.waitForElementToAppear(HOTEL_COUNT, 20).orElseThrow();
        return driverTools.getText(count);
    }

    public int getHotelCount() {
        String text = getHotelCountText();
        int count = CoreFunctions.getIntFromString(text).orElse(0);
        logger.log("Parsed hotel count '" + count + "' from text: '" + text + "'");
        return count;
    }

    // --- Popular Locations side panel ---

    public boolean isPopularLocationsPanelVisible(String propertyName) {
        Locator loc = getPopularLocationsPanelLocator(propertyName);
        Optional<WebElement> panel = driverTools.waitForElementToAppear(loc, 10);
        if (panel.isEmpty()) {
            return false;
        }
        driverTools.scrollIntoWebView(panel.get());
        return driverTools.isElementDisplayed(loc);
    }
    public String getPopularLocationsHeading(String propertyName) {
        Locator heading = new Locator("POPULAR_LOCATIONS_HEADING_" + safeName(propertyName),
                By.xpath(popularLocationsPanelXpath(propertyName) + "//h2"));
        WebElement el = driverTools.waitForElementToAppear(heading, 10).orElseThrow();
        return driverTools.getText(el);
    }

    public int getPopularLocationsLinkCount(String propertyName) {
        Locator panel = getPopularLocationsPanelLocator(propertyName);
        if (driverTools.waitForElementToAppear(panel, 10).isEmpty()) {
            return 0;
        }
        WebElement panelEl = driverTools.element(panel).orElse(null);
        if (panelEl == null) {
            return 0;
        }
        return panelEl.findElements(By.xpath(".//a")).size();
    }

    protected Locator getPopularLocationsPanelLocator(String propertyName) {
        return new Locator("POPULAR_LOCATIONS_PANEL_" + safeName(propertyName),
                By.xpath(popularLocationsPanelXpath(propertyName)));
    }

    protected String popularLocationsPanelXpath(String propertyName) {
        String enLabel = propertyName + " in Popular Locations";
        String idLabel = propertyName + " di Lokasi Populer";

        String condition = "contains(normalize-space(),'" + enLabel + "') or contains(normalize-space(),'" + idLabel + "')";

        if ("Palace".equalsIgnoreCase(propertyName)) {
            condition += " or contains(normalize-space(),'Istana di Lokasi Populer')";
        }

        return "//*[@data-testid='side-panel-item'][.//h2[" + condition + "]]";
    }
    // --- SEO cards ---

    public List<String> getSeoCardPropertyLabels(int limit) {
        driverTools.scrollIntoWebView(SEO_CARD_PROPERTY_LABELS);
        driverTools.waitForElementToAppear(SEO_CARD_PROPERTY_LABELS, 15).orElseThrow();
        List<String> labels = driverTools.getTexts(SEO_CARD_PROPERTY_LABELS);
        logger.log("Found " + labels.size() + " SEO card property labels: " + labels);
        return labels.stream().limit(limit).collect(Collectors.toList());
    }

    public List<String> getSeoCardNames(int limit) {
        driverTools.waitForElementToAppear(SEO_CARD_NAMES, 15).orElseThrow();
        List<String> names = driverTools.getTexts(SEO_CARD_NAMES);
        logger.log("Found " + names.size() + " SEO card names: " + names);
        return names.stream().limit(limit).collect(Collectors.toList());
    }

    protected String safeName(String name) {
        return (name == null ? "" : name.trim()).toUpperCase().replace(' ', '_');
    }
}
