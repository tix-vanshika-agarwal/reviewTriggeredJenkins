package com.tiket.page.ttd.landingpage;

import com.tiket.model.PriceType;
import com.tiket.model.Session;
import com.tiket.page.ttd.TTDPage;
import com.tiket.page.ttd.destinationpage.TTDDestinationPage;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.pojo.ttd.GETLandingPageGroupPOJO;
import com.tiket.pojo.ttd.TTDLandingPagePOJO;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public abstract class TTDLandingPage <T extends WebDriver> extends BasePage<T> {

    public TTDLandingPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TTDLandingPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new TTDLandingPageAndroid<>(session);
            }
            case IOS -> {
                return new TTDLandingPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void checkTabbing();
    public abstract void goToSection(String sectionName);
    public abstract String getCampaignProduct(String productName);
    public abstract String findPrice(PriceType priceType, String currency); //String currency for iOS only
    public abstract void clickSeeAllRecentlyViewed();
    public abstract int getTotalRecentlyViewedItems();
    public abstract String selectRecentlyViewedItem(String list);
    public abstract String removeFirstRecentlyViewedItem();
    public abstract String getFirstRecentlyViewedItem();
    public abstract boolean isRecentlyViewedDisplayed();
    public abstract boolean isLandingPageValid();
    public abstract void clickSearchBar();
    public abstract void clickSearchBarSRP();
    public abstract void clickExploreArrow();
    public abstract void backToHomePage();
    public abstract String openProductByCategory(String productCategory);
    public abstract String getExploreText();
    public abstract void choosePopularDestination();
    public abstract void clickEnableLocation();
    public abstract void clickSeeAllButton();
    public abstract boolean isLocationOff();
    public abstract String getLocationText();
    public abstract void clickLocationText();
    public abstract String getRandomLocationTag();
    public abstract void clickSeeAllButtonLocationTags();
    public abstract Boolean isProductCardVisible();
    public abstract void findSpecificSection(String sectionName);
    public abstract void clickChips(String chipsName);
    public abstract boolean isReloadSectionVisible(String sectionName);
    public abstract void openProductWithDiscount();
    public abstract boolean verifyPromoSection();
    public abstract String getSelectedLocation();
    public abstract String getSelectedTab();
    public abstract List<String> getAllTabs();
    String[] productCategoryList = {"attractions", "playground", "spa and beauty", "events", "jr pass", "jr-pass"};

    public String getProductCategory(String productCategory) {
        switch (productCategory.toLowerCase()) {
            case "attractions" -> {productCategory = "Attraction/Atraksi";}
            case "playgrounds" -> {productCategory = "Playgrounds/Tempat Bermain";}
            case "spa and beauty" -> {productCategory = "Spa & Beauty/Spa & Kecantikan";}
            case "tours" -> {productCategory = "Tours/Tur";}
            case "entertainments" -> {productCategory = "Entertainments/Entertainment";}
            case "sports and outdoors" -> {productCategory = "Sports & Outdoors/Olahraga & Outdoor";}
            case "events" -> {productCategory = "Event/Events";}
            case "jr pass", "jr-pass" -> {productCategory = "JR-Pass/JR Pass";}
            case "travel essential" -> {productCategory = "Travel Essential/Pelengkap Perjalanan";}
            case "wifi & e-sim" -> {productCategory = "Wifi & e-SIM/Wifi & e-SIM";}
            case "umroh" -> {productCategory = "Umroh/Umroh";}
            case "transportation" -> {productCategory = "Transportasi/Transports";}
            default -> {
                logger.softWarning("productCategory will use '" + productCategory + "' as the value");
            }
        }
        return productCategory;
    }

    public boolean getInventoryStatus(TTDLandingPagePOJO.Data details, String location) {
        TTDLandingPagePOJO.DynamicContent detail = details.dynamicContents.get(0);
        int numberOfLocations = detail.chips.size();
        String locationChipId = "";
        for (int i = 0; i < numberOfLocations; i++) {
            if (detail.chips.get(i).text.equals(location)) {
                locationChipId = detail.chips.get(i).id;
                break;
            }
        }

        int count = 0;
        int cardListSize = detail.cards.size();
        for (int i = 0; i < cardListSize; i++) {
            if (detail.cards.get(i).chipsId.equals(locationChipId)) {
                count++;
            }
        }
        logger.log("number of cards for " + location + " are ->" + count);
        return count > 0;
    }

    public void refresh() {
        driverTools.swipeUp();
    }

    public String[] getReloadSectionAndChips(TTDLandingPagePOJO response) {
        int size = response.data.length;
        String sectionName = null;
        String chipsName = null;
        for (int i = 0; i < size; i++) {
            if (response.data[i].templateCode.equals("IMAGE_WITH_CHIPS")) {
                TTDLandingPagePOJO.DynamicContent detail = response.data[i].dynamicContents.get(0);
                sectionName = detail.title;
                logger.log("sectionName -> " + sectionName);
                Map<String, String> chipsID = new HashMap<String, String>();
                Map<String, Integer> cardsCount = new HashMap<String, Integer>();

                int chipsSize = detail.chips.size();
                for (int j = 0; j < chipsSize; j++) {
                    String id = detail.chips.get(j).id;
                    String text = detail.chips.get(j).text;
                    chipsID.put(id, text);
                    cardsCount.put(id, 0);
                }

                int cardSize = detail.cards.size();
                for (int j = 0; j < cardSize; j++) {
                    String cardsChipsID = detail.cards.get(j).chipsId;
                    int value = cardsCount.get(cardsChipsID) + 1;
                    cardsCount.put(cardsChipsID, value);
                }
                for (String key : cardsCount.keySet()) {
                    logger.log("chipsID -> " + chipsID.get(key));
                    logger.log("key -> " + key);
                    logger.log("cardsCount.get(key) -> " + cardsCount.get(key));
                    if (cardsCount.get(key) == 0) {
                        chipsName = chipsID.get(key);
                        return new String[]{sectionName, chipsName};
                    }
                }
            }
        }
        return new String[]{sectionName, chipsName};
    }

    public abstract boolean getSectionName(String sectionName, String productName);
    public abstract boolean verifyRedirectionCTA(String productName);
    public abstract boolean verifyDisplayedCurrency(String currency);
    public abstract void swipeToTop();
    public abstract void goToProductCard(TTDLandingPagePOJO.Card productCard);
    public abstract boolean isProductCardPriceVisible(TTDLandingPagePOJO.Card productCard, String price);
    public abstract String getProductCardPriceText(TTDLandingPagePOJO.Card productCard);
    public abstract String getFirstProductNameRecentlyViewed();
    public abstract void removeFirstRecentlyViewedProduct();
    public abstract void scrollUntilSRP();
    public abstract Boolean scrollUpUntilFloatingButtonShown();
    public abstract void clickFloatingButton();
    public abstract String getFirstGroupMenuChipText();
    public abstract void openGroupMenus(String groupMenuTitle);
    public abstract void clickMenuByIndex(int index);
    public abstract void clickBottomsheetCrossButton();

    /**
     * Verifies if the clicked menu icon matches the specified group menu type and language.
     *
     * @param groupMenuType the type of the group menu.
     * @param menuPOJO      the menu POJO object to be verified.
     * @param language      the language in which the menu is displayed.
     * @return true if the menu icon is verified successfully, false otherwise.
     */
    public Boolean verifyClickedMenuIcon(String groupMenuType, GETLandingPageGroupPOJO.Menu menuPOJO, String language) {
        String menuUrl = menuPOJO.url;
        List<String> needCheck = new ArrayList<>();

        logger.log("groupMenuType -> " + groupMenuType);
        logger.log("menuUrl -> " + menuUrl);
        switch (groupMenuType.toLowerCase()) {
            case "activity":
                if (menuUrl.contains("productCategoryCodes=") || menuUrl.contains("&category=")) {
                    needCheck.add("category");
                } else if (menuUrl.contains("campaignIds=") || menuUrl.contains("campaignId=")
                        || menuUrl.contains("isTiketClean=") || menuUrl.contains("isTiketFlexi=")) {
                    needCheck.add("general_filter");
                } else if (menuUrl.contains("startingPriceInCentsFrom=") || menuUrl.contains("startingPriceInCentsTo=")) {
                    needCheck.add("price_filter");
                } else if (false) {
                    needCheck.add("date_filter");
                } else if (menuUrl.contains("sortAttributes=") || menuUrl.contains("sortDirection=")
                        || menuUrl.contains("isNearby=")) {
                    needCheck.add("sort");
                }

                return verifyActivityGroupMenu(menuUrl, needCheck);
            case "destination":
                if (menuUrl.contains("/country/") || menuUrl.contains("/region/")
                        || menuUrl.contains("/city/")) {
                    needCheck.add("destination_page");
                } else if (menuUrl.contains("/area/")) {
                    needCheck.add("srp");
                }

                return verifyDestinationGroupMenu(menuUrl, needCheck, language);
            default:
                logger.fail("Group menu '" + groupMenuType + "' is not yet supported");
        }
        return false;
    }

    /**
     * Verifies the activity group menu based on the provided menu URL and the checks required.
     *
     * @param menuUrl   the URL of the menu to be verified.
     * @param needCheck the list of checks that need to be performed.
     * @return true if the activity group menu is verified successfully, false otherwise.
     */
    public Boolean verifyActivityGroupMenu(String menuUrl, List<String> needCheck) {
        TTDSearchResultPage<T> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        for (int i = 0; i < needCheck.size(); i++) {
            switch (needCheck.get(i)) {
                case "category" -> {
                    Assertion.assertThat("Selected category is not 'all' or 'semua", ttdSearchResultPage.getSelectedCategory(), not(isOneOf("All", "Semua")));
                }
                case "general_filter" -> {
                    Assertion.assertThat("General filter is active", ttdSearchResultPage.isFilterActivated("general"), is(true));
                }
                case "price_filter" -> {
                    Assertion.assertThat("Price filter is active", ttdSearchResultPage.isFilterActivated("price"), is(true));
                }
                case "date_filter" -> {
                    Assertion.assertThat("Date filter is active", ttdSearchResultPage.isFilterActivated("date"), is(true));
                }
                case "sort" -> {
                    Assertion.assertThat("Sort filter is active", ttdSearchResultPage.isFilterActivated("sort"), is(true));
                }
            }
        }
        return true;
    }

    /**
     * Verifies the destination group menu based on the provided menu URL, the checks required, and the language.
     *
     * @param menuUrl   the URL of the menu to be verified.
     * @param needCheck the list of checks that need to be performed.
     * @param language  the language in which the menu is displayed.
     * @return true if the destination group menu is verified successfully, false otherwise.
     */
    public Boolean verifyDestinationGroupMenu(String menuUrl, List<String> needCheck, String language) {
        TTDSearchResultPage<T> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDDestinationPage<T> ttdDestinationPage = TTDDestinationPage.getInstance(session);
        TTDPage<T> ttdPage = TTDPage.getInstance(session);

        String[] urlSplit = menuUrl.split("/");
        String publicId = urlSplit[urlSplit.length - 1];
        logger.log("publicId -> " + publicId);
        String targetTextValue = "";

        if (needCheck.contains("srp")) {
            targetTextValue = ttdSearchResultPage.getLocation();
            ttdSearchResultPage.clickBack();
        } else if (needCheck.contains("destination_page")) {
            targetTextValue = ttdDestinationPage.getDestinationName();
            logger.log("targetTextValue -> " + targetTextValue);
        }

        Assertion.assertThat("Displayed correct destination/location", ttdPage.getDestinationPublicId(targetTextValue, language), is(publicId));

        return true;
    }

    /**
     * Verifies if all items in the group menu of the specified type are displayed correctly.
     *
     * @param groupMenuType the type of the group menu to be verified.
     * @return true if all items in the group menu are displayed correctly, false otherwise.
     */
    public abstract Boolean verifySeeAllGroupMenu(String groupMenuType);
    public abstract void clickMenuByName(String name);
    public abstract void clickDestinationMenuByName(String name);
    public abstract boolean checkDestinationUpdated();
    public abstract boolean isCurrentLocationOptionAppears();
    public abstract boolean verifyLocationChip();
    public abstract String checkLocationChip();
    public abstract void clickLocationChip();
    public abstract void clickBack();
    public abstract boolean checkCTA();
    public abstract void openSpecialOffers();
    public abstract void checkReloadButton(String chipText, String reloadSection);
    public abstract String getUIPrice(String productName, String currency);
}