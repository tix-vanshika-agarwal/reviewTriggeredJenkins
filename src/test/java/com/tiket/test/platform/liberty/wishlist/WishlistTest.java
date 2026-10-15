package com.tiket.test.platform.liberty.wishlist;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.page.accommodation.homes.homeslandingpage.HomesLandingPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.page.wishlist.collection.WishlistCollectionPage;
import com.tiket.page.wishlist.landing.WishlistPage;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class WishlistTest extends BaseTest {

    @Author(names = {"Zia"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistCreateCollection() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);

        Map<String, String> param = getParam("testWishlistCreateCollection");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("Open wishlist");
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();

        step("Create collection");
        String collectionName = "Collection "+CoreFunctions.getCurrentDateTime();
        wishlistPage.createCollection(collectionName);

        step("Verify collection created");
        List<String> collectionNames = wishlistPage.getCollectionNames();
        Assertion.assertThat("Collection name is exist", collectionNames, hasItem(collectionName));

        step("Clean up");
        wishlistPage.cleanUp(collectionName);
    }

    @Author(names = {"Zia"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistDeleteCollection() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        Map<String, String> param = getParam("testWishlistDeleteCollection");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user");
        loginPage.loginWith(loginType, username, password);

        step("Open wishlist");
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();

        step("Create collection");
        String collectionName = "Collection "+CoreFunctions.getCurrentDateTime();
        wishlistPage.createCollection(collectionName);

        step("Verify collection created");
        List<String> collectionNames = wishlistPage.getCollectionNames();
        Assertion.assertThat("Collection name is exist", collectionNames, hasItem(collectionName));

        step("Delete collection");
        wishlistPage.deleteCollection(collectionName);

        step("Verify collection deleted");
        collectionNames = wishlistPage.getCollectionNames();
        Assertion.assertThat("Collection name is not exist", collectionNames, not(hasItem(collectionName)));
    }

    @Author(names = {"Zia"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistEditCollection() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        Map<String, String> param = getParam("testWishlistEditCollection");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user");
        loginPage.loginWith(loginType, username, password);

        step("Open wishlist");
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();

        step("Create collection");
        String collectionName = "Collection "+CoreFunctions.getCurrentDateTime();
        wishlistPage.createCollection(collectionName);

        step("Edit collection");
        String newCollectionName = "Collection Edited"+CoreFunctions.getCurrentDateTime();
        wishlistPage.editCollection(collectionName,newCollectionName);

        step("Verify collection Edited");
        String collectionNameDetail = wishlistPage.getCollectionNameDetail();
        Assertion.assertThat("Edited Collection name is now showing", collectionNameDetail, is(newCollectionName));
        wishlistPage.clickBackButton();

        step("Verify collection Edited in list");
        List<String> collectionNames = wishlistPage.getCollectionNames();
        Assertion.assertThat("Edited Collection name is now showing", collectionNames, hasItem(newCollectionName));

        step("Clean up");
        wishlistPage.cleanUp(newCollectionName);
    }

    @Author(names = {"Zia"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistAddProduct() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        Map<String, String> param = getParam("testWishlistAddProduct");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");
        landingPage.clickFirstHotelCard();
        String productName = detailPage.getHotelNameDetail();

        step("Click heart icon");
        detailPage.clickHeartIcon();
        detailPage.clickManageWishlist();

        step("See all wishlist");
        wishlistPage.clickSeeAllWishlistAfterAdding();

        step("Verify newly added product");
        List<String> wishlistProducts = wishlistPage.getWishlistNamesAll();
        Assertion.assertThat("Product is added to wishlist", wishlistProducts, hasItem(productName));

        step("Clean up");
        wishlistPage.cleanUpAllPage(productName);
    }

    @Author(names = {"Zia"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistTabbingsCollection() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);

        Map<String, String> param = getParam("testWishlistTabbingsCollection");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user");
        loginPage.loginWith(loginType, username, password);

        step("Open wishlist");
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();

        step("Create collection");
        String collectionName = "Collection "+CoreFunctions.getCurrentDateTime();
        wishlistPage.createCollection(collectionName);

        step("Verify collection created");
        List<String> collectionNames = wishlistPage.getCollectionNames();
        Assertion.assertThat("Collection name is exist", collectionNames, hasItem(collectionName));

        step("Verify tabbing");
        wishlistPage.openCollection(collectionName);
        Assertion.assertThat("Tabbing is working", wishlistPage.verifyTabbing(), is(true));

        step("Clean up");
        wishlistPage.clickBackButton();
        wishlistPage.cleanUp(collectionName);
    }

    @Author(names = {"Zia", "Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistClickProductCollection() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        Map<String, String> param = getParam("testWishlistClickProductCollection");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String collectionName = param.get("collectionName");

        step("User goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Open wishlist");
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();

        step("Open collection "+collectionName);
        wishlistPage.openCollection(collectionName);

        step("Click product");
        String productName = wishlistPage.clickProductInCollection();

        step("Verify redirected to PDP");
        String pdpName = detailPage.getHotelNameDetail();
        Assertion.assertThat("Product name on the collection page should be equal", pdpName, is(productName));
    }

    @Author(names = {"Zia"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistEditDateProductCollection() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);

        Map<String, String> param = getParam("testWishlistEditDateProductCollection");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String collectionName = param.get("collectionName");
        String productType = param.get("productType");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user " +username);
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open wishlist");
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();

        step("Open collection "+collectionName);
        wishlistPage.openCollection(collectionName);

        step("Click hotel tab");
        wishlistPage.clickTab(productType);

        step("Edit date");
        String productName = wishlistPage.getFirstProductName();
        String productDate = wishlistPage.getProductDate(productName);
        int dateCheckInOld = Integer.parseInt(productDate.split(" ")[0]);

        int currentDate = Integer.parseInt(CoreFunctions.getCurrentLongDate().split(" ")[0]);

        int newDateCheckin;
        int newDateCheckout;
        if(dateCheckInOld != currentDate){
            newDateCheckin = currentDate;
            newDateCheckout =CoreFunctions.addToDateInt(newDateCheckin,1);
        }else{
            newDateCheckin =CoreFunctions.addToDateInt(dateCheckInOld,1);
            newDateCheckout =CoreFunctions.addToDateInt(newDateCheckin,1);
        }

        wishlistPage.clickManageButtonCollectionViaVerticalSection(productName);
        wishlistPage.changeDate(newDateCheckin,newDateCheckout);

        step("Verify date changed");
        String productDateNew = wishlistPage.getProductDate(productName);
        Assertion.assertThat("Date is changed", productDateNew, is(not(productDate)));
    }

    @Author(names = {"Zia"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistEditGuestProductCollection() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);

        Map<String, String> param = getParam("testWishlistEditGuestProductCollection");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String collectionName = param.get("collectionName");
        String productType = param.get("productType");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user " +username);
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open wishlist");
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();

        step("Open collection "+collectionName);
        wishlistPage.openCollection(collectionName);

        step("Click hotel tab");
        wishlistPage.clickTab(productType);

        step("Edit guest");
        String productName = wishlistPage.getFirstProductName();
        List<String> roomGuest = wishlistPage.getGuestAndRoomInfo(productName);
        int oldRoom = CoreFunctions.getIntFromString(roomGuest.get(0)).orElseThrow();

        int newRoom = (oldRoom >= 5) ? (oldRoom - 1) : (oldRoom + 1);
        int newGuest = newRoom + 1;

        wishlistPage.clickManageButtonCollectionViaVerticalSection(productName);
        wishlistPage.changeGuestAndRoom(newGuest,newRoom);

        step("Verify guest changed");
        List<String> roomGuestNew = wishlistPage.getGuestAndRoomInfo(productName);
        Assertion.assertThat("Guest and room is changed", roomGuestNew, is(not(roomGuest)));
    }

    @Author(names = {"Zia"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistMoveProductBetweenCollection() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);

        Map<String, String> param = getParam("testWishlistMoveProductBetweenCollection");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String collectionName = param.get("collectionName");
        String productType = param.get("productType");
        String productName = param.get("productName");
        String collectionName2 = param.get("collectionName2");

        step("User goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Open wishlist");
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();

        step("Open collection "+collectionName);
        wishlistPage.openCollection(collectionName);

        step("Click hotel tab");
        wishlistPage.clickTab(productType);

        step("Move collection");
        wishlistPage.clickManageButtonCollectionViaVerticalSection(productName);
        wishlistPage.moveToAnotherCollection(collectionName2,true);

        step("Verify collection changed");
        String collectionOpen = wishlistPage.getCollectionNameDetail();
        Assertion.assertThat("Collection is changed", collectionOpen, is(collectionName2));

        step("Click hotel tab");
        wishlistPage.clickTab(productType);

        List<String> productsInCollection = wishlistPage.getProductsInCollection();
        Assertion.assertThat("Product is moved to another collection", productsInCollection, hasItem(productName));

        step("move back to original collection");
        wishlistPage.clickManageButtonCollectionViaVerticalSection(productName);
        wishlistPage.moveToAnotherCollection(collectionName,false);
    }

    @Author(names = {"Zia"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistRemoveProduct() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        Map<String, String> param = getParam("testWishlistRemoveProduct");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user " +username);
        loginPage.loginWith(loginType, username, password);

        step("open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");
        landingPage.clickFirstHotelCard();
        String productName = detailPage.getHotelNameDetail();

        step("Click heart icon");
        detailPage.clickHeartIcon();
        detailPage.clickManageWishlist();

        step("Go to wishlist page");
        homePage.goToHomeDeeplink();
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();

        step("Click manage wishlist");;
        wishlistPage.clickManageWishlist(productName);

        step("Remove product");
        wishlistPage.removeFromAllWishlist();

        step("Verify product removed");
        List<String> wishlistProducts = wishlistPage.getWishlistNamesLanding();
        Assertion.assertThat("Product is removed from wishlist", wishlistProducts, not(hasItem(productName)));
    }

    @Author(names = {"Zia"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistRemoveProductFromCollection() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        Map<String, String> param = getParam("testWishlistRemoveProductFromCollection");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String collectionName = param.get("collectionName");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user " +username);
        loginPage.loginWith(loginType, username, password);

        step("open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");
        landingPage.clickFirstHotelCard();
        String productName = detailPage.getHotelNameDetail();

        step("Click heart icon");
        detailPage.clickHeartIcon();
        detailPage.clickManageWishlist();
        wishlistPage.addToCollectionManage(collectionName);

        step("Go to wishlist page");
        homePage.goToHomeDeeplink();
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();

        step("Open collection");
        wishlistPage.openCollection(collectionName);

        step("Remove product from collection");
        wishlistPage.clickManageButtonCollection(productName);
        wishlistPage.removeFromCollection();

        step("Verify product removed");
        List<String> wishlistProducts = wishlistPage.getProductsInCollection();
        Assertion.assertThat("Product is removed from wishlist", wishlistProducts, not(hasItem(productName)));
    }

    @Author(names = {"Mridul"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistAddProductHomes(){
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);


        Map<String, String> param = getParam("testWishlistAddProductHomes");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user " +username);
        loginPage.loginWith(loginType, username, password);

        step("open homes PDP page");
        homePage.openVerticalPageDeeplink("homes","","");
        hotelLandingPage.clickFirstHotelCard();
        String productName = detailPage.getHotelNameDetail();

        step("Click heart icon");
        detailPage.clickHeartIcon();
        detailPage.clickManageWishlist();

        step("See all wishlist");
        wishlistPage.clickSeeAllWishlistAfterAdding();

        step("Verify newly added product");
        List<String> wishlistProducts = wishlistPage.getWishlistNamesAll();
        Assertion.assertThat("Product is added to wishlist", wishlistProducts, hasItem(productName));

        step("Clean up");
        wishlistPage.cleanUpAllPage(productName);
    }

    @Author(names = {"Mridul"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistAddProductTTD(){
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        Map<String, String> param = getParam("testWishlistAddProductTTD");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String slugProductName = param.get("productName");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user " +username);
        loginPage.loginWith(loginType, username, password);

        step("open TTD PDP page");
        ttdSearchResultPage.openPdpDeeplink(slugProductName);

        step("Click heart icon");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();
        ttdProductDetailPageWeb.clickHeartIconAndManageWishList();

        step("See all wishlist");
        ttdProductDetailPageWeb.clickSeeAllWishlistAfterAdding();
        webViewPage.switchToNative();

        step("Verify newly added product");
        List<String> wishlistProducts = wishlistPage.getWishlistNamesAll();
        Assertion.assertThat("Product is added to wishlist", wishlistProducts, hasItem(productName));

        step("Clean up");
        wishlistPage.cleanUpAllPage(productName);
    }

    @Author(names = {"Mridul"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistRemoveProductHomes(){
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);


        Map<String, String> param = getParam("testWishlistRemoveProductHomes");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user " +username);
        loginPage.loginWith(loginType, username, password);

        step("open homes PDP page");
        homePage.openVerticalPageDeeplink("homes","","");
        homesLandingPage.searchStayLocation("Jakarta");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();
        searchResultPage.selectFirstHotelCard();
        String productName = detailPage.getHotelNameDetail();

        step("Click heart icon");
        detailPage.clickHeartIcon();
        detailPage.clickManageWishlist();

        step("See all wishlist");
        wishlistPage.clickSeeAllWishlistAfterAdding();

        step("Verify newly added product");
        List<String> wishlistProducts = wishlistPage.getWishlistNamesAll();
        Assertion.assertThat("Product is added to wishlist", wishlistProducts, hasItem(productName));

        step("open homes PDP page AGAIN");
        homePage.openVerticalPageDeeplink("homes","","");
        homesLandingPage.searchStayLocation(productName);
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Turn OFF the Heart icon");
        detailPage.TurnOffHeartIcon();

        step("Check the product is removed from wishlist");
        homePage.goToHomeDeeplink();
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();
        Assertion.assertThat("Product is removed from wishlist", wishlistPage.getWishlistNamesAll(), not(hasItem(productName)));
    }

    @Author(names = {"Mridul", "Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistRemoveProductHotel(){
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);


        Map<String, String> param = getParam("testWishlistRemoveProductHotel");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("open Hotel PDP page");
        homePage.openVerticalPageDeeplink("hotel","","");
        hotelLandingPage.clickFirstHotelCard();
        String productName = detailPage.getHotelNameDetail();

        step("Click heart icon");
        detailPage.clickHeartIcon();
        detailPage.clickManageWishlist();

        step("See all wishlist");
        wishlistPage.clickSeeAllWishlistAfterAdding();

        step("Verify newly added product");
        List<String> wishlistProducts = wishlistPage.getWishlistNamesAll();
        Assertion.assertThat("Product is added to wishlist", wishlistProducts, hasItem(productName));

        step("open Hotel PDP page AGAIN");
        homePage.openVerticalPageDeeplink("hotel","","");
        hotelLandingPage.searchStayLocation(productName);
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Turn OFF the Heart icon");
        detailPage.TurnOffHeartIcon();

        step("Check the product is removed from wishlist");
        homePage.goToHomeDeeplink();
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();
        Assertion.assertThat("Product is removed from wishlist", wishlistPage.getWishlistNamesAll(), not(hasItem(productName)));
    }

    @Author(names = {"Mridul"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistRemoveProductTTD(){
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);


        Map<String, String> param = getParam("testWishlistRemoveProductTTD");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String slugProductName = param.get("productName");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user " +username);
        loginPage.loginWith(loginType, username,password);

        step("open TTD PDP page");
        ttdSearchResultPage.openPdpDeeplink(slugProductName);

        step("Click heart icon");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();
        ttdProductDetailPageWeb.clickHeartIconAndManageWishList();

        step("See all wishlist");
        ttdProductDetailPageWeb.clickSeeAllWishlistAfterAdding();
        webViewPage.switchToNative();

        step("Verify newly added product");
        List<String> wishlistProducts = wishlistPage.getWishlistNamesAll();
        Assertion.assertThat("Product is added to wishlist", wishlistProducts, hasItem(productName));

        step("open TTD PDP page AGAIN");
        ttdSearchResultPage.openPdpDeeplink(slugProductName);

        step("Turn OFF the Heart icon");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.TurnOffHeartIcon();
        webViewPage.switchToNative();

        step("Check the product is removed from wishlist");
        homePage.goToHomeDeeplink();
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();
        Assertion.assertThat("Product is removed from wishlist", wishlistPage.getWishlistNamesAll(), not(hasItem(productName)));
    }

    @Author(names = {"Mridul"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistAddProductToCollection(){
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        Map<String, String> param = getParam("testWishlistAddProductToCollection");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String slugProductName = param.get("productName");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user " +username);
        loginPage.loginWith(loginType, username, password);

        step("Open wishlist");
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();

        step("Create collection");
        String collectionName = "Collection "+CoreFunctions.getCurrentDateTime();
        wishlistPage.createCollection(collectionName);

        step("open TTD PDP page");
        ttdSearchResultPage.openPdpDeeplink(slugProductName);

        step("Click heart icon");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();
        ttdProductDetailPageWeb.clickHeartIconAndManageWishList();

        step("See all wishlist");
        ttdProductDetailPageWeb.clickSeeAllWishlistAfterAdding();
        webViewPage.switchToNative();

        step("Verify newly added product");
        List<String> wishlistProducts = wishlistPage.getWishlistNamesAll();
        Assertion.assertThat("Product is added to wishlist", wishlistProducts, hasItem(productName));

        step("Add product to collection");
        homePage.gotoHome();
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();
        wishlistPage.clickManageWishlist(productName);
        wishlistPage.addToCollectionManageFromWishlistPage(collectionName);

        step("Verify product added to collection");
        homePage.gotoHome();
        homePage.openWishlist();
        wishlistPage.openCollection(collectionName);
        Assertion.assertThat("Product is added to collection", wishlistPage.getProductsInCollection(), hasItem(productName));

        step("Clean up");
        homePage.gotoHome();
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();
        wishlistPage.cleanUp(collectionName);
        ttdSearchResultPage.openPdpDeeplink(slugProductName);
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.TurnOffHeartIcon();
    }

    @Author(names = {"Alden"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistOpenFilledCollection() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        WishlistCollectionPage<?> wishlistCollectionPage = new WishlistCollectionPage<>(session);

        Map<String, String> param = getParam("testWishlistOpenFilledCollection");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String collectionName = param.get("collectionName");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open wishlist");
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();

        step("Open collection");
        wishlistPage.openCollection(collectionName);

        step("Check for collection name");
        String actualCollectionName = wishlistCollectionPage.getCollectionName();
        Assertion.assertThat("Collection name is correct", actualCollectionName, is(collectionName));

        step("Check for collection content");
        Assertion.assertThat("Collection is not empty", wishlistCollectionPage.getAllCollectionContentAmount(), greaterThan(0));

        step("Check for tabbing content reflecting all contents");
        Set<String> allContents = wishlistCollectionPage.getAllCollectionContent();
        Set<String> tabbingContents = wishlistCollectionPage.getTabbingContent();
        Assertion.assertThat("Verify tabbing", tabbingContents, is(allContents));
    }

    @Author(names = {"Alden"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testWishlistOpenEmptyCollection() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        WishlistCollectionPage<?> wishlistCollectionPage = new WishlistCollectionPage<>(session);

        Map<String, String> param = getParam("testWishlistOpenEmptyCollection");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String collectionName = param.get("collectionName");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open wishlist");
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();

        step("Open collection");
        wishlistPage.openCollection(collectionName);

        step("Check for collection name");
        String actualCollectionName = wishlistCollectionPage.getCollectionName();
        Assertion.assertThat("Collection name is correct", actualCollectionName, is(collectionName));

        step("Check if collection is empty");
        Assertion.assertThat("Collection is empty", wishlistCollectionPage.getAllCollectionContentAmount(), is(0));

        step("Check for tabbing content reflecting all contents which is empty");
        Set<String> allContents = wishlistCollectionPage.getAllCollectionContent();
        Set<String> tabbingContents = wishlistCollectionPage.getTabbingContent();
        Assertion.assertThat("Verify tabbing", tabbingContents, is(allContents));
    }


    @Author(names = {"Areeb"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void testWishlistCollectionCrudActionsWithAddAndRemoveProductFromManageList() {

        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testWishlistCollectionCrudActionsWithAddAndRemoveProductFromManageList");
        BaseTest.paramsThreadLocal.set(param);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        WishlistCollectionPage<?> wishlistCollectionPage = new WishlistCollectionPage<>(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String slugProductName = param.get("productName");

        boolean isProd = Configs.ENVIRONMENT == Environment.PROD;
        RegisterNewAccountData accountData = null;
        boolean loggedIn = false;
        try {
            String username = param.get("username");
            String password = param.get("password");
            if (!isProd) {
                step("Register new account via API");
                accountData = unm.registerNewAccountViaApiUNM();
                username = accountData.email;
                password = accountData.password;
            }

            step("User goto home");
            homePage.gotoHome();

            
            if (isProd) {
                step("Login as " + username + " via UI");
                loginPage.loginAsGuest();
                loginPage.loginWith("email", username, password);
            } else {
                step("Login as " + username + " via deeplink");
                homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);
            }
            loggedIn = true;

            step("Open wishlist");
            homePage.openWishlist();
            wishlistPage.skipPriceDropsPopup();

            step("Create collection");
            String collectionName = "Collection "+CoreFunctions.getCurrentDateTime();
            wishlistPage.createCollection(collectionName);

            step("Verify collection created");
            List<String> collectionNames = wishlistPage.getCollectionNames();
            Assertion.assertThat("Collection name is exist", collectionNames, hasItem(collectionName));

            step("Edit collection");
            String newCollectionName = "Collection Edited"+CoreFunctions.getCurrentDateTime();
            wishlistPage.editCollection(collectionName,newCollectionName);

            step("Verify collection Edited");
            String collectionNameDetail = wishlistPage.getCollectionNameDetail();
            Assertion.assertThat("Edited Collection name is now showing", collectionNameDetail, is(newCollectionName));
            wishlistPage.clickBackButton();

            step("Open wishlist");
            homePage.goToHomeDeeplink();
            homePage.openWishlist();
            wishlistPage.skipPriceDropsPopup();

            step("Open collection");
            wishlistPage.openCollection(newCollectionName);

            step("Check for collection name");
            String actualCollectionName = wishlistCollectionPage.getCollectionName();
            Assertion.assertThat("Collection name is correct", actualCollectionName, is(newCollectionName));

            step("Check if collection is empty");
            Assertion.assertThat("Collection is empty", wishlistCollectionPage.getAllCollectionContentAmount(), is(0));

            step("Check for tabbing content reflecting all contents which is empty");
            Set<String> allContents = wishlistCollectionPage.getAllCollectionContent();
            Set<String> tabbingContents = wishlistCollectionPage.getTabbingContent();
            Assertion.assertThat("Verify tabbing", tabbingContents, is(allContents));

            step("Add product to collection");
            step("open TTD PDP page");
            ttdSearchResultPage.openPdpDeeplink(slugProductName);

            step("Click heart icon");
            webViewPage.switchToWebViewUrl("to-do");
            String productName = ttdProductDetailPageWeb.getProductName();
            ttdProductDetailPageWeb.clickHeartIconAndManageWishList();

            step("See all wishlist");
            ttdProductDetailPageWeb.clickSeeAllWishlistAfterAdding();
            webViewPage.switchToNative();

            step("Verify newly added product");
            List<String> wishlistProducts = wishlistPage.getWishlistNamesAll();
            Assertion.assertThat("Product is added to wishlist", wishlistProducts, hasItem(productName));

            step("Add product to collection");
            homePage.gotoHome();
            homePage.openWishlist();
            wishlistPage.skipPriceDropsPopup();
            wishlistPage.clickManageWishlist(productName);
            wishlistPage.addToCollectionManageFromWishlistPage(newCollectionName);

            step("Verify product added to collection");
            homePage.gotoHome();
            homePage.openWishlist();
            wishlistPage.openCollection(newCollectionName);
            Assertion.assertThat("Product is added to collection", wishlistPage.getProductsInCollection(), hasItem(productName));

            step("Clean up");
            homePage.gotoHome();
            homePage.openWishlist();
            wishlistPage.skipPriceDropsPopup();
            wishlistPage.cleanUp(newCollectionName);
            ttdSearchResultPage.openPdpDeeplink(slugProductName);
            webViewPage.switchToWebViewUrl("to-do");
            ttdProductDetailPageWeb.TurnOffHeartIcon();

            step("Verify collection deleted");
            homePage.gotoHome();
            homePage.openWishlist();
            collectionNames = wishlistPage.getCollectionNames();
            Assertion.assertThat("Collection name is not exist", collectionNames, not(hasItem(newCollectionName)));

        } finally {
            if (!isProd) {
                step("Cleanup: logout and delete the registered account via API");
                homePage.logoutAndDeleteTemporaryAccount(accountData, loggedIn);
            }
        }
    }

    @Author(names = {"Areeb"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "Liberty")
    @Test
    public void testWishlistEditDateAndGuestProductCollection() {

        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testWishlistEditDateAndGuestProductCollection");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String collectionName = param.get("collectionName");
        String productType = param.get("productType");

        step("User goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Open wishlist");
        homePage.goToHomeDeeplink();
        homePage.openWishlist();
        wishlistPage.skipPriceDropsPopup();

        step("Open collection " + collectionName);
        wishlistPage.openCollection(collectionName);

        step("Click hotel tab");
        wishlistPage.clickTab(productType);

        step("Edit date");
        String productName = wishlistPage.getFirstProductName();
        String productDate = wishlistPage.getProductDate(productName);
        int dateCheckInOld = Integer.parseInt(productDate.split(" ")[0]);

        int currentDate = Integer.parseInt(CoreFunctions.getCurrentLongDate().split(" ")[0]);

        int newDateCheckin;
        int newDateCheckout;
        if (dateCheckInOld != currentDate) {
            newDateCheckin = currentDate;
            newDateCheckout = CoreFunctions.addToDateInt(newDateCheckin, 1);
        } else {
            newDateCheckin = CoreFunctions.addToDateInt(dateCheckInOld, 1);
            newDateCheckout = CoreFunctions.addToDateInt(newDateCheckin, 1);
        }

        wishlistPage.clickManageButtonCollectionViaVerticalSection(productName);
        wishlistPage.changeDate(newDateCheckin, newDateCheckout);

        step("Verify date changed");
        String productDateNew = wishlistPage.getProductDate(productName);
        Assertion.assertThat("Date is changed", productDateNew, is(not(productDate)));


        step("Edit guest");
        productName = wishlistPage.getFirstProductName();
        List<String> roomGuest = wishlistPage.getGuestAndRoomInfo(productName);
        int oldRoom = CoreFunctions.getIntFromString(roomGuest.get(0)).orElseThrow();

        int newRoom = (oldRoom >= 5) ? (oldRoom - 1) : (oldRoom + 1);
        int newGuest = newRoom + 1;

        wishlistPage.clickManageButtonCollectionViaVerticalSection(productName);
        wishlistPage.changeGuestAndRoom(newGuest,newRoom);

        step("Verify guest changed");
        List<String> roomGuestNew = wishlistPage.getGuestAndRoomInfo(productName);
        Assertion.assertThat("Guest and room is changed", roomGuestNew, is(not(roomGuest)));
    }
}
