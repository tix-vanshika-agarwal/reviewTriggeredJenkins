package com.tiket.web.test.platform.liberty;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.FailedLoginException;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import static com.tiket.testdata.TestData.getParam;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.wishlist.WishlistLandingWeb;

public class WishlistTest extends BaseTest {

    @Author(names = {"Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void verifyWishlistLandingPageWeb() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyWishlistLandingPageWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Wishlist page");
        homePage.goToWishlistPage();

        step("Click cross and ok on coachmark");
        wishlistLandingPage.clickCross();

        step("verify all headings");
        Assertion.assertThat("Wishlist header displayed", wishlistLandingPage.checkWishlistHeading(), is(true));
        Assertion.assertThat("Collection header displayed", wishlistLandingPage.checkCollectionHeading(), is(true));
        Assertion.assertThat("ALL Wishlist header displayed", wishlistLandingPage.checkAllWishlistHeading(), is(true));
        Assertion.assertThat("Recently header displayed", wishlistLandingPage.checkRecentlySearchedHeading(), is(true));
        Assertion.assertThat("Wse Wishlist header displayed", wishlistLandingPage.checkUseWishlistHeading(), is(true));

        step("verify all Buttons");
        Assertion.assertThat("See All collection Button", wishlistLandingPage.checkCollectionSeeAllButton(), is(true));
        Assertion.assertThat("See Clear Button", wishlistLandingPage.checkClearButton(), is(true));
        Assertion.assertThat("See Hotel Button", wishlistLandingPage.checkHotelButton(), is(true));
        Assertion.assertThat("See Villa Button", wishlistLandingPage.checkVillaButton(), is(true));

    }

    @Author(names = {"Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void verifyWishlistEditCollectionWeb() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyWishlistEditCollectionWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Wishlist page");
        homePage.goToWishlistPage();

        step("Click cross and ok on coachmark");
        wishlistLandingPage.clickCross();

        step("Click see all");
        wishlistLandingPage.clickCollectionSeeAll();

        step("Open Collection to Edit");
        wishlistLandingPage.openCollectionToEdit();

        step("Click Three dots on Collection");
        wishlistLandingPage.clickThreeDotsOnCollection();

        step("Click Edit Cover and Collection Name");
        wishlistLandingPage.clickEditCollection();

        String name = wishlistLandingPage.getRandomCollectionName();

        step("Fill the New Name and Save");
        wishlistLandingPage.fillNewCollectionNameAndSave(name);

        step("Check The snackbar appeared");
        Assertion.assertThat("Verify Snackbar after editing the collection", wishlistLandingPage.checkEditCollectionSnackbar(), is(true));

        step("Check The New collection name while being in collection");
        Assertion.assertThat("Verify Snackbar after editing the collection", wishlistLandingPage.checkCollectionName(name), is(true));

        step("Come out of the collection");
        wishlistLandingPage.clickBackButton();

        step("Check the new collection name coming out of collection");
        Assertion.assertThat("Verify Snackbar after editing the collection", wishlistLandingPage.checkCollectionNameOutside(name), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyAddProductFromHotelPDP() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyAddProductFromHotelPDP");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String hotel = param.get("hotel");
        String hotelTitle = param.get("hotelTitle");
        String allWishlist = param.get("allWishlist");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Hotel PDP page");
        wishlistLandingPage.goToHotelPDP(hotel);

        step("Click Wishlist Icon");
        wishlistLandingPage.clickWishlistHeartIcon();

        step("Go to all wishlist");
        wishlistLandingPage.goToAllWishlist(allWishlist);

        Boolean isCardPresent = wishlistLandingPage.checkCardTitle(hotelTitle);

        step("Remove the added product from wishlist for Clean up");
        wishlistLandingPage.goToHotelPDP(hotel);
        wishlistLandingPage.clickWishlistHeartIcon();

        step("Check if Hotel card is present in All wishlist");
        Assertion.assertThat("Verify hotel card is present in all wishlist", isCardPresent, is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyRemoveProductFromHotelPDP() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRemoveProductFromHotelPDP");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String hotel = param.get("hotel");
        String hotelTitle = param.get("hotelTitle");
        String allWishlist = param.get("allWishlist");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Hotel PDP page");
        wishlistLandingPage.goToHotelPDP(hotel);

        step("Click Wishlist Icon to add The Hotel product as a Prerequisite");
        wishlistLandingPage.clickWishlistHeartIcon();

        step("Click Manage on Snackbar");
        wishlistLandingPage.clickManageOnSnackbar();

        step("Click View all wishlist To verify that product is added so we can remove it");
        wishlistLandingPage.clickViewAllWishlist();

        Boolean isCardPresent = wishlistLandingPage.checkCardTitle(hotelTitle);

        step("Again Go to Hotel PDP page");
        wishlistLandingPage.goToHotelPDP(hotel);

        step("Click The Heart icon on Already added product to remove it");
        wishlistLandingPage.clickWishlistHeartIcon();

        Boolean isRemoveSnackbarAppeared = wishlistLandingPage.checkRemoveSnackbar();

        step("Go to all wishlist");
        wishlistLandingPage.goToAllWishlist(allWishlist);

        Boolean isProductPresent = wishlistLandingPage.checkCard(hotelTitle);

        step("Check if Hotel card is present in All wishlist");
        Assertion.assertThat("Verify hotel card is present in all wishlist", isCardPresent, is(true));

        step("Verify that snackbar appears on removing the product");
        Assertion.assertThat("Verify that snackbar appears on removing the product", isRemoveSnackbarAppeared, is(true));

        step("Go to wishlist and verify that same product should not be present now");
        Assertion.assertThat("Verify that product is not present in wishlist", isProductPresent, is(false));

    }

    @Author(names = {"Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyAddProductFromHomesPDP() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyAddProductFromHomesPDP");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String hotel = param.get("hotel");
        String hotelTitle = param.get("hotelTitle");
        String allWishlist = param.get("allWishlist");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Hotel PDP page");
        wishlistLandingPage.goToHotelPDP(hotel);

        step("Click Wishlist Icon");
        wishlistLandingPage.clickWishlistHeartIcon();

        step("Go to all wishlist and open Villa & Apt. tab");
        wishlistLandingPage.goToAllWishlistForHomes(allWishlist);

        Boolean isCardPresent = wishlistLandingPage.checkCardTitle(hotelTitle);

        step("Remove the added product from wishlist for Clean up");
        wishlistLandingPage.goToHotelPDP(hotel);
        wishlistLandingPage.clickWishlistHeartIcon();

        step("Check if Homes card is present in Villa & Apt. wishlist");
        Assertion.assertThat("Verify homes card is present in Villa & Apt. wishlist", isCardPresent, is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyRemoveProductFromHomesPDP() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRemoveProductFromHomesPDP");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String hotel = param.get("hotel");
        String hotelTitle = param.get("hotelTitle");
        String allWishlist = param.get("allWishlist");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Hotel PDP page");
        wishlistLandingPage.goToHotelPDP(hotel);

        step("Click Wishlist Icon to add The Hotel product as a Prerequisite");
        wishlistLandingPage.clickWishlistHeartIcon();

        step("Click Manage on Snackbar");
        wishlistLandingPage.clickManageOnSnackbar();

        step("Click View all wishlist To verify that product is added so we can remove it");
        wishlistLandingPage.clickViewAllWishlist();

        Boolean isCardPresent = wishlistLandingPage.checkCardTitle(hotelTitle);

        step("Again Go to Hotel PDP page");
        wishlistLandingPage.goToHotelPDP(hotel);

        step("Click The Heart icon on Already added product to remove it");
        wishlistLandingPage.clickWishlistHeartIcon();

        Boolean isRemoveSnackbarAppeared = wishlistLandingPage.checkRemoveSnackbar();

        step("Go to all wishlist");
        wishlistLandingPage.goToAllWishlist(allWishlist);

        Boolean isProductPresent = wishlistLandingPage.checkCard(hotelTitle);

        step("Check if Hotel card is present in All wishlist");
        Assertion.assertThat("Verify hotel card is present in all wishlist", isCardPresent, is(true));

        step("Verify that snackbar appears on removing the product");
        Assertion.assertThat("Verify that snackbar appears on removing the product", isRemoveSnackbarAppeared, is(true));

        step("Go to wishlist and verify that same product should not be present now");
        Assertion.assertThat("Verify that product is not present in wishlist", isProductPresent, is(false));

    }

    @Author(names = {"Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void verifyOpeningEmptyCollection() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyOpeningEmptyCollection");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Wishlist page");
        homePage.goToWishlistPage();

        step("Click cross and ok on coachmark");
        wishlistLandingPage.clickCross();

        step("Click see all");
        wishlistLandingPage.clickCollectionSeeAll();

        step("Open Empty Collection");
        wishlistLandingPage.openEmptyCollection();

        step("Check Heading text");
        Assertion.assertThat("Check Heading text", wishlistLandingPage.checkHeadingText(), is(true));

        step("Verify Hotels entry point is present");
        Assertion.assertThat("Verify Hotels entry point is present", wishlistLandingPage.checkHotelsEntryPoint(), is(true));

        step("Verify Homes entry point is present");
        Assertion.assertThat("Verify Homes entry point is present", wishlistLandingPage.checkHomesEntryPoint(), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void verifyOpeningFilledCollection() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyOpeningFilledCollection");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Wishlist page");
        homePage.goToWishlistPage();

        step("Click cross and ok on coachmark");
        wishlistLandingPage.clickCross();

        step("Click see all");
        wishlistLandingPage.clickCollectionSeeAll();

        step("Open filled Collection");
        wishlistLandingPage.openFilledCollection();

        step("Verify Products are present");
        Assertion.assertThat("Verify Products are present", wishlistLandingPage.checkProductVisible(), is(true));

        step("Verify product count is present");
        Assertion.assertThat("Verify product count is present", wishlistLandingPage.checkProductCountVisible(), is(true));
    }

    @Author(names = {"Hiten", "Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyAddingProductToCollection() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyAddingProductToCollection");
        BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String hotel = param.get("hotel");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Remove promo voucher");
        wishlistLandingPage.clickCrossOnPromoVoucher();

        step("Go to Hotel PDP page");
        wishlistLandingPage.goToHotelPDP(hotel);

        step("Click The Heart icon");
        wishlistLandingPage.clickWishlistHeartIcon();

        step("Remove promo voucher");
        wishlistLandingPage.clickCrossOnPromoVoucherMweb();

        step("Click Manage CTA");
        wishlistLandingPage.clickManageOnSnackbar();

        step("Add product to the collection");
        wishlistLandingPage.selectCollectionAndAdd();

        step("Verify the snackbar after adding the product");
        Assertion.assertThat("Verify the snackbar is visible after adding or not", wishlistLandingPage.isSnackbarVisibleAfterAdding(), is(true));

        step("remove product from collection");
        wishlistLandingPage.clickWishlistHeartIcon();
    }

    @Author(names = {"Hiten", "Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyClickingTheProduct() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyClickingTheProduct");
        BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Wishlist page");
        homePage.goToWishlistPage();

        step("Click cross and ok on coachmark");
        wishlistLandingPage.clickCross();

        step("Click on see all wishlist button");
        wishlistLandingPage.clickOnWishlistSeeAllButton();

        step("Get the wishlist product");
        String wishlistProduct = wishlistLandingPage.getWishlistProduct();

        step("Click on wishlist product");
        wishlistLandingPage.clickOnWishlistProduct();

        step(("Get the wishlist product after opening it"));
        String productAfterOpening = wishlistLandingPage.getWishlistProductPageHeading();
        Assertion.assertThat("Verify the product opened is correct or not", wishlistProduct, is(productAfterOpening));
    }

    @Author(names = {"Hiten", "Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyTabbingsInWishlist() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyTabbingsInWishlist");
        BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);
        String email = param.get("email");
        String password = param.get("password");
        String urlparam1 = param.get("urlparam1");
        String urlparam2 = param.get("urlparam2");
        String urlparam3 = param.get("urlparam3");
        String urlparam4 = param.get("urlparam4");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Wishlist page");
        homePage.goToWishlistPage();

        step("Click cross and ok on coachmark");
        wishlistLandingPage.clickCross();

        step("Click on see all wishlist button");
        wishlistLandingPage.clickOnWishlistSeeAllButton();
        String allUrl = wishlistLandingPage.getURl();
        Assertion.assertThat("Verify the url is correct or not", allUrl.contains(urlparam1), is(true));

        step("Click on the hotel tab");
        wishlistLandingPage.clickOnHotelTab();
        String hotelUrl = wishlistLandingPage.getURl();
        Assertion.assertThat("Verify the url is correct or not", hotelUrl.contains(urlparam2), is(true));

        step("Click on the villa tab");
        wishlistLandingPage.clickOnVillaTab();
        String homesUrl = wishlistLandingPage.getURl();
        Assertion.assertThat("Verify the url is correct or not", homesUrl.contains(urlparam3), is(true));

        step("Click on the TTD tab");
        wishlistLandingPage.clickOnTodoTab();
        String todoUrl = wishlistLandingPage.getURl();
        Assertion.assertThat("Verify the url is correct or not", todoUrl.contains(urlparam4), is(true));
    }

    @Author(names = {"Hiten", "Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyByRemovingProductFromTheCollection() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyByRemovingProductFromTheCollection");
        BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);
        String email = param.get("email");
        String password = param.get("password");
        String hotel = param.get("hotel");
        String allWishlist = param.get("allWishlist");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Wishlist page");
        homePage.goToWishlistPage();

        step("Click cross and ok on coachmark");
        wishlistLandingPage.clickCross();

        step("Open Hotel PDP page");
        wishlistLandingPage.goToHotelPDP(hotel);

        step("Click Wishlist Icon");
        wishlistLandingPage.clickWishlistHeartIcon();

        step("Go to all wishlist");
        wishlistLandingPage.goToAllWishlist(allWishlist);

        step("Click manage button");
        wishlistLandingPage.clickManageButton();

        step("Click Check box");
        wishlistLandingPage.clickCheckBox();

        step("Click manage button");
        wishlistLandingPage.clickManageButton();

        step("Delete from the collection");
        wishlistLandingPage.removeFromCollectionMenuButton();

        step("Verify the snackbar is visible after deleting the collection");
        Assertion.assertThat("Verify the snackbar is visible after deleting or not", wishlistLandingPage.isSnackbarVisibleAfterDeleting(), is(true));
    }

    @Author(names = {"Daffa", "Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void verifyWishlistDeleteCollection() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyWishlistDeleteCollection");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String collectionName = "Collection " + CoreFunctions.getCurrentDateTime();

        step("Go to home page");
        homePage.goToHomePage();

        step("Check login state and log in");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Open Wishlist page");
        homePage.goToWishlistPage();

        step("Click cross and ok on coachmark");
        wishlistLandingPage.clickCross();

        step("Create Collection");
        wishlistLandingPage.addCollection(collectionName);
        Assertion.assertThat("Verify collection Created", wishlistLandingPage.checkCollectionNameOutside(collectionName), is(true));

        step("Delete Collection");
        wishlistLandingPage.openAndDeleteNewCollection();
        Assertion.assertThat("Verify collection was deleted", wishlistLandingPage.checkDeleteCollectionSnackbar(), is(true));
    }

    @Author(names = {"Daffa", "Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyWishlistMoveCollection() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyWishlistMoveCollection");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String collectionFrom = param.get("collectionFrom");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Wishlist page");
        homePage.goToWishlistPage();

        step("Click cross and ok on coachmark");
        wishlistLandingPage.clickCross();

        step("Go to Collection with saved hotels");
        wishlistLandingPage.clickCollectionSeeAll();
        boolean checkCollectionFrom = wishlistLandingPage.checkCollectionName(collectionFrom);
        if (!checkCollectionFrom) {
            Assertion.skip("Collection not found");
        } else {
            wishlistLandingPage.checkAndOpenCollection(collectionFrom);
        }

        step("Move saved hotels to another Collection");
        wishlistLandingPage.moveToAnotherCollection();
        Assertion.assertThat("Verify collection was moved", wishlistLandingPage.checkMoveCollectionSnackbar(), is(true));

        step("Reset the collection");
        wishlistLandingPage.resetCollection();
    }

    @Author(names = {"Daffa", "Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyChangeDateWishlist() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyChangeDateWishlist");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String collectionFrom = param.get("collectionFrom");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Wishlist page");
        homePage.goToWishlistPage();

        step("Click cross and ok on coachmark");
        wishlistLandingPage.clickCross();

        step("Go to Collection with saved hotels");
        wishlistLandingPage.clickCollectionSeeAll();
        boolean checkCollectionFrom = wishlistLandingPage.checkCollectionNameOutside(collectionFrom);
        if (!checkCollectionFrom) {
            Assertion.skip("Collection not found");
        } else {
            wishlistLandingPage.checkAndOpenCollection(collectionFrom);
        }

        step("Get hotel date before change");
        String dateBefore = wishlistLandingPage.getHotelDate();

        step("Change date of a product and verify");
        wishlistLandingPage.changeDateOfProduct();
        String dateAfter = wishlistLandingPage.getHotelDate();
        Assertion.assertThat("Hotel date was changed", dateAfter, not(dateBefore));
    }

    @Author(names = {"Daffa", "Rizky"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void verifyChangeGuestWishlist() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyChangeGuestWishlist");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String collectionFrom = param.get("collectionFrom");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Wishlist page");
        homePage.goToWishlistPage();

        step("Click cross and ok on coachmark");
        wishlistLandingPage.clickCross();

        step("Go to Collection with saved hotels");
        wishlistLandingPage.clickCollectionSeeAll();
        boolean checkCollectionFrom = wishlistLandingPage.checkCollectionName(collectionFrom);
        if (!checkCollectionFrom) {
            Assertion.skip("Collection not found");
        } else {
            wishlistLandingPage.checkAndOpenCollection(collectionFrom);
        }

        step("Change guest of a product and verify");
        wishlistLandingPage.manageWishlist();
        String guestAfter = wishlistLandingPage.getHotelGuest();
        Assertion.assertThat("Hotel guest was changed", guestAfter, not(guestAfter.isEmpty()));
    }

    @Author(names = {"Ishika", "Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Wishlist")
    @Vertical(name = "Liberty")
    @Test
    public void testVerifyWishlistCollectionCreation() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("testVerifyWishlistCollectionCreation");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);
        String email = param.get("email");
        String password = param.get("password");

        step("User goto home");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open wishlist");
        homePage.goToWishlistPage();

        step("Click cross and ok on coachmark");
        wishlistLandingPage.clickCross();

        step("Create Collection With Cover");
        String collectionName = "Collection " + CoreFunctions.getCurrentDateTime();
        wishlistLandingPage.createCollectionWithCover(collectionName);
        Assertion.assertThat("Verify collection Created ", wishlistLandingPage.checkCollectionNameOutside(collectionName), is(true));

        step("Delete Collection");
        wishlistLandingPage.openAndDeleteNewCollection();

    }

    @Author(names = {"Ashish"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void verifyChangeDateAndGuestWishlist() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyChangeDateAndGuestWishlist");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String collectionFrom = param.get("collectionFrom");
        String collectionVertical = param.get("collectionVertical");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        // Wrap the wishlist flow so the account is always logged out afterwards (in the finally),
        // whether the test passes, fails an assertion, or is skipped mid-flow. This safeguards the
        // shared collection account so a leftover session can't affect the next run.
        try {
            step("Open Wishlist page");
            homePage.goToWishlistPage();

            step("Click cross and ok on coachmark");
            wishlistLandingPage.clickCross();

            step("Go to Collection with saved hotels");
            boolean checkCollectionFrom = wishlistLandingPage.checkCollectionNameOutside(collectionFrom);
            if (!checkCollectionFrom) {
                Assertion.skip("Collection not found");
            } else {
                wishlistLandingPage.checkAndOpenCollection(collectionFrom);
            }

            step("Open " + collectionVertical + " tab");
            wishlistLandingPage.openCollectionTab(collectionVertical);

            step("Verify collection has at least 1 product");
            int productCount = wishlistLandingPage.getProductCount();
            if (productCount < 1) {
                Assertion.skip("Collection has no product to change");
            }
            Assertion.assertThat("Collection has at least 1 product", productCount >= 1, is(true));

            step("Capture the target product title so all changes/verifications stay on the same card");
            String productTitle = wishlistLandingPage.getFirstProductTitle();

            step("Get hotel date before change for the target product");
            String dateBefore = wishlistLandingPage.getHotelDate(productTitle);

            step("Change date of the product via 3 dots menu (avoid re-selecting same date)");
            wishlistLandingPage.openProductManageMenu(productTitle, "Change date", "Ubah tanggal");
            List<Integer> selectedDays = wishlistLandingPage.chooseAvailableDate(dateBefore);

            step("Reload and re-open the collection tab so the change is reflected on the visible panel");
            wishlistLandingPage.reloadAndOpenCollectionTab(collectionVertical);

            step("Get hotel date after change and verify it changed on the same card");
            String dateAfter = wishlistLandingPage.getHotelDate(productTitle);
            Assertion.assertThat("Hotel date was changed { dateBefore: "+ dateBefore + " } { dateAfter: " + dateAfter + " }" , dateAfter, not(dateBefore));

            step("Verify the exact dates selected on the calendar appear on the card");
            List<Integer> cardDays = wishlistLandingPage.extractDayNumbers(dateAfter);
            Assertion.assertThat("Selected calendar days appear on card { selected: " + selectedDays + " } { card: " + cardDays + " }", cardDays, is(selectedDays));

            step("Get hotel guest and room before change for the target product");
            String guestBefore = wishlistLandingPage.getHotelGuest(productTitle);
            String roomBefore = wishlistLandingPage.getHotelRoom(productTitle);

            step("Change guest & room of the product via 3 dots menu (target counts different from current)");
            wishlistLandingPage.openProductManageMenu(productTitle, "Change guest & room", "Ubah tamu & kamar");
            List<Integer> selectedRoomGuest = wishlistLandingPage.changeRoomAndGuestToDifferent(roomBefore, guestBefore);
            int selectedRoom = selectedRoomGuest.get(0);
            int selectedGuest = selectedRoomGuest.get(1);

            step("Reload and re-open the collection tab so the change is reflected on the visible panel");
            wishlistLandingPage.reloadAndOpenCollectionTab(collectionVertical);

            step("Get hotel guest after change and verify it changed on the same card");
            String guestAfter = wishlistLandingPage.getHotelGuest(productTitle);
            Assertion.assertThat("Hotel guest was changed { guestBefore: " + guestBefore + " } { guestAfter: " + guestAfter + " }", guestAfter, not(guestBefore));

            step("Verify the exact guest count selected appears on the card");
            Assertion.assertThat("Selected guest count appears on card { selected: " + selectedGuest + " } { card: " + guestAfter + " }", wishlistLandingPage.extractCount(guestAfter), is(selectedGuest));

            step("Get hotel room after change and verify it changed on the same card");
            String roomAfter = wishlistLandingPage.getHotelRoom(productTitle);
            Assertion.assertThat("Hotel room was changed { roomBefore: " + roomBefore + " } { roomAfter: " + roomAfter + " }", roomAfter, not(roomBefore));

            step("Verify the exact room count selected appears on the card");
            Assertion.assertThat("Selected room count appears on card { selected: " + selectedRoom + " } { card: " + roomAfter + " }", wishlistLandingPage.extractCount(roomAfter), is(selectedRoom));
        } finally {
            // Guard the logout so a cleanup failure can never replace the real test result: if logout
            // throws (e.g. the session/browser is already in a bad state), we log it and swallow it so
            // the original failure/skip still propagates and is reported at the correct step.
            try {
                step("Logout to safeguard the collection account (runs whether the test passed, failed, or was skipped)");
                loginPage.logoutViaUrl();
            } catch (Exception e) {
                log("Logout during cleanup failed (ignored to preserve the original result): " + e.getMessage());
            }
        }
    }

    @Author(names = {"Jashan"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyRemoveProductFromWishlist() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRemoveProductFromWishlist");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String hotel = param.get("hotel");
        String hotelTitle = param.get("hotelTitle");
        String allWishlist = param.get("allWishlist");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Hotel PDP page");
        wishlistLandingPage.goToHotelPDP(hotel);

        step("Click Wishlist Icon to add The Hotel product as a Prerequisite");
        wishlistLandingPage.clickWishlistHeartIcon();

        step("Click Manage on Snackbar");
        wishlistLandingPage.clickManageOnSnackbar();

        step("Click View all wishlist To verify that product is added so we can remove it");
        wishlistLandingPage.clickViewAllWishlist();

        Boolean isCardPresent = wishlistLandingPage.checkCardTitle(hotelTitle);

        step("Remove the added product from wishlist for Clean up");
        wishlistLandingPage.goToHotelPDP(hotel);
        wishlistLandingPage.clickWishlistHeartIcon();

        step("Check if Hotel card is present in All wishlist");
        Assertion.assertThat("Verify hotel card is present in all wishlist", isCardPresent, is(true));
    }

    /**
     * End-to-end Wishlist collection CRUD plus add/remove product from Manage Wishlist on dWeb.
     * Combines flows from {@link #testVerifyWishlistCollectionCreation},
     * {@link #verifyOpeningEmptyCollection}, {@link #verifyWishlistEditCollectionWeb},
     * {@link #verifyAddingProductToCollection}, {@link #verifyByRemovingProductFromTheCollection},
     * and {@link #verifyWishlistDeleteCollection}.
     *
     * <p>Sheet key: {@code verifyManageWishlistCollectionWeb}.
     * Params: {@code email}, {@code password}.
     *
     * <p>Prerequisite: the logged-in account must already have at least one product in the
     * All Wishlist section before the test starts.
     *
     * <p>Flow:
     * <ol>
     *   <li>Create collection (name + cover) and verify name on landing</li>
     *   <li>Open empty collection — verify name, empty state, Hotels/Homes entry points, tabs</li>
     *   <li>Edit collection name via top-right three-dots menu</li>
     *   <li>Add an All Wishlist product to the collection via product three-dots → Add to collection</li>
     *   <li>Verify filled collection shows product card and tabs</li>
     *   <li>Remove product from collection via product three-dots menu</li>
     *   <li>Delete collection via top-right three-dots menu</li>
     * </ol>
     */
    @Author(names = {"Yazid"})
    @Module(name = "Wishlist")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void verifyManageWishlistCollectionWeb()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyManageWishlistCollectionWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistLandingPage = WishlistLandingWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String collectionName = "Collection " + CoreFunctions.getCurrentDateTime();

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Wishlist page");
        homePage.goToWishlistPage();

        step("Click cross and ok on coachmark");
        wishlistLandingPage.clickCross();

        step("Verify at least 1 product is present in All Wishlist section");
        wishlistLandingPage.clickOnWishlistSeeAllButton();
        String productTitle = wishlistLandingPage.getWishlistProduct();
        Assertion.assertThat("At least 1 product should be present in All Wishlist", productTitle, not(blankOrNullString()));
        wishlistLandingPage.clickBackButton();

        step("Create collection with name and cover");
        wishlistLandingPage.createCollectionWithCover(collectionName);
        Assertion.assertThat("Collection should be created", wishlistLandingPage.checkCollectionNameOutside(collectionName), is(true));

        step("Open newly created empty collection");
        wishlistLandingPage.clickCollectionSeeAll();
        wishlistLandingPage.checkAndOpenCollection(collectionName);
        Assertion.assertThat("Collection name should be displayed inside collection", wishlistLandingPage.checkCollectionName(collectionName), is(true));
        Assertion.assertThat("Empty collection should show start adding wishlist message", wishlistLandingPage.checkHeadingText(), is(true));
        Assertion.assertThat("Empty collection should contain Hotels entry section", wishlistLandingPage.checkHotelsEntryPoint(), is(true));
        Assertion.assertThat("Empty collection should contain Villas & Apt. entry section", wishlistLandingPage.checkHomesEntryPoint(), is(true));
        Assertion.assertThat("Empty collection should contain Hotel tab", wishlistLandingPage.checkHotelButton(), is(true));
        Assertion.assertThat("Empty collection should contain Villa & Apt. tab", wishlistLandingPage.checkVillaButton(), is(true));

        step("Edit collection name via three dots menu");
        wishlistLandingPage.clickThreeDotsOnCollection();
        wishlistLandingPage.clickEditCollection();
        String editedCollectionName = wishlistLandingPage.getRandomCollectionName();
        wishlistLandingPage.fillNewCollectionNameAndSave(editedCollectionName);
        Assertion.assertThat("Edit collection snackbar should be displayed", wishlistLandingPage.checkEditCollectionSnackbar(), is(true));
        Assertion.assertThat("Edited collection name should be displayed inside collection", wishlistLandingPage.checkCollectionName(editedCollectionName), is(true));
        wishlistLandingPage.clickBackButton();
        Assertion.assertThat("Edited collection name should be displayed outside collection", wishlistLandingPage.checkCollectionNameOutside(editedCollectionName), is(true));

        step("Add product to newly created collection from All Wishlist manage menu");
        wishlistLandingPage.clickOnWishlistSideMenu();
        wishlistLandingPage.openProductThreeDotsMenu(productTitle);
        wishlistLandingPage.addToCollectionMenuButton();
        wishlistLandingPage.selectCollectionByNameAndAdd(editedCollectionName);
        Assertion.assertThat("Snackbar should appear after adding product to collection", wishlistLandingPage.isSnackbarVisibleAfterAdding(), is(true));

        step("Verify product is added to the collection");
        wishlistLandingPage.clickCollectionSeeAll();
        wishlistLandingPage.checkAndOpenCollection(editedCollectionName);
        Assertion.assertThat("Collection name should be displayed inside filled collection", wishlistLandingPage.checkCollectionName(editedCollectionName), is(true));
        Assertion.assertThat("Filled collection should contain hotel/homes product card", wishlistLandingPage.checkProductVisible(), is(true));
        Assertion.assertThat("Filled collection should contain Hotel tab", wishlistLandingPage.checkHotelButton(), is(true));
        Assertion.assertThat("Filled collection should contain Villa & Apt. tab", wishlistLandingPage.checkVillaButton(), is(true));

        step("Remove product from the collection via product three dots menu");
        wishlistLandingPage.openProductThreeDotsMenu(productTitle);
        wishlistLandingPage.removeFromCollectionMenuButton();
        Assertion.assertThat("Snackbar should appear after removing product from collection", wishlistLandingPage.isSnackbarVisibleAfterDeleting(), is(true));
        Assertion.assertThat("Product should be removed from the collection", wishlistLandingPage.checkHeadingText(), is(true));

        step("Delete the collection via collection three dots menu");
        wishlistLandingPage.deleteOpenedCollection();
        Assertion.assertThat("Delete collection snackbar should be displayed", wishlistLandingPage.checkDeleteCollectionSnackbar(), is(true));
    }
}
