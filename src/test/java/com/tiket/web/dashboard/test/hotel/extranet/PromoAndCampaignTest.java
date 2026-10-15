package com.tiket.web.dashboard.test.hotel.extranet;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.extranet.AddNewPromoAndCampaignPage;
import com.tiket.web.dashboard.page.hotel.extranet.Navbar;
import com.tiket.web.dashboard.page.hotel.extranet.SideMenu;
import com.tiket.web.dashboard.page.hotel.extranet.YourPromotionsPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class PromoAndCampaignTest extends BaseTest {
    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "PromoAndCampaign")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToCreateBasicDeals() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        AddNewPromoAndCampaignPage<?> addNewPromoAndCampaignPage = new AddNewPromoAndCampaignPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        YourPromotionsPage<?> yourPromotionsPage = new YourPromotionsPage<>(session);

        AuthTest authTest = new AuthTest();
        
        Map<String, String> param = getParam("testVerifyUserAbleToCreateBasicDeals");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String name = param.get("name");
        int startDateIndex = Integer.parseInt(param.get("startDateIndex"));
        int endDateIndex = Integer.parseInt(param.get("endDateIndex"));
        String discount = param.get("discount");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to add new promo and campaign page");
        sideMenu.clickPromoAndCampaignMenu();
        sideMenu.clickAddNewPromoAndCampaignMenu();
        Assertion.assertThat("Promo and campaign data page is loaded", addNewPromoAndCampaignPage.isAddNewPromoAndCampaignPageLoaded(), is(true));
        addNewPromoAndCampaignPage.dismissPriceSimulationModal();

        step("Go to create basic deal page");
        addNewPromoAndCampaignPage.goToCreateBasicDealPage();
        Assertion.assertThat("Create basic deal page is loaded", addNewPromoAndCampaignPage.isCreateBasicDealPageLoaded(), is(true));
        addNewPromoAndCampaignPage.dismissCancellationPolicyCoachmark();
        
        step("Fill out the form");
        addNewPromoAndCampaignPage.fillOutPromoNameInput(name);
        addNewPromoAndCampaignPage.clickSelectAllLoginPlatformButton();
        addNewPromoAndCampaignPage.clickSelectAllNonLoginPlatformButton();
        addNewPromoAndCampaignPage.selectDateByDateTypeAndIndex("STAY_PERIOD_START", startDateIndex);
        addNewPromoAndCampaignPage.selectDateByDateTypeAndIndex("STAY_PERIOD_END", endDateIndex);
        addNewPromoAndCampaignPage.clickBookAnytimeRadioButton();
        addNewPromoAndCampaignPage.fillOutDiscountPercentageInput(discount);
        addNewPromoAndCampaignPage.clickSelectAllRoomAndRatePlan();
        addNewPromoAndCampaignPage.clickReviewPromoButton();
        addNewPromoAndCampaignPage.clickCreatePromoButton();
        Assertion.assertThat("The promo is created", addNewPromoAndCampaignPage.isPromoCreated(name), is(true));

        step("Revert test data");
        yourPromotionsPage.clickActionByPromoName(name);
        yourPromotionsPage.deletePromo();
        Assertion.assertThat("Success snackbar is appeared", yourPromotionsPage.isSuccessSnackbarAppeared(), is(true));
        yourPromotionsPage.refreshPage();
        Assertion.assertThat("The promo is deleted", addNewPromoAndCampaignPage.isPromoCreated(name), is(false));
    }

    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "PromoAndCampaign")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToUpdateBasicDeals() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        AddNewPromoAndCampaignPage<?> addNewPromoAndCampaignPage = new AddNewPromoAndCampaignPage<>(session);
        YourPromotionsPage<?> yourPromotionsPage = new YourPromotionsPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();
        
        Map<String, String> param = getParam("testVerifyUserAbleToUpdateBasicDeals");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String name = param.get("name");
        String oldDiscount = param.get("oldDiscount");
        String newDiscount = param.get("newDiscount");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to your promotions page");
        sideMenu.clickPromoAndCampaignMenu();
        sideMenu.clickYourPromotionsMenu();
        Assertion.assertThat("Your promotions page is loaded", yourPromotionsPage.isYourPromotionsPageLoaded(), is(true));

        step("Click an edit button");
        yourPromotionsPage.clickActionByPromoName(name);
        yourPromotionsPage.clickEditButton();
        Assertion.assertThat("Edit basic deal page is loaded", addNewPromoAndCampaignPage.isCreateBasicDealPageLoaded(), is(true));
        
        step("Fill out the form with new data");
        addNewPromoAndCampaignPage.fillOutDiscountPercentageInput(newDiscount);
        addNewPromoAndCampaignPage.clickReviewPromoButton();
        addNewPromoAndCampaignPage.clickCreatePromoButton();
        Assertion.assertThat("The promo is edited", addNewPromoAndCampaignPage.isPromoCreated(name), is(true));

        step("Revert test data");
        yourPromotionsPage.clickActionByPromoName(name);
        yourPromotionsPage.clickEditButton();
        Assertion.assertThat("Edit basic deal page is loaded", addNewPromoAndCampaignPage.isCreateBasicDealPageLoaded(), is(true));
        addNewPromoAndCampaignPage.fillOutDiscountPercentageInput(oldDiscount);
        addNewPromoAndCampaignPage.clickReviewPromoButton();
        addNewPromoAndCampaignPage.clickCreatePromoButton();
        Assertion.assertThat("The promo is edited", addNewPromoAndCampaignPage.isPromoCreated(name), is(true));
    }

    @DataProvider(name = "testVerifyUserAbleToTogglePromotionStatus")
    public Object[][] testVerifyUserAbleToTogglePromotionStatusDataProvider() {
        return getObjectParams("testVerifyUserAbleToTogglePromotionStatus");
    }

    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "PromoAndCampaign")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "testVerifyUserAbleToTogglePromotionStatus")
    public void testVerifyUserAbleToTogglePromotionStatus(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);
        Navbar<?> navbar = new Navbar<>(session);
        YourPromotionsPage<?> yourPromotionsPage = new YourPromotionsPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();
        
        String email = map.get("email");
        String password = map.get("password");
        String hotelName = map.get("hotelName");
        String name = map.get("name").replace('$', ' ');
        boolean isToggleOn = Boolean.parseBoolean(map.get("isToggleOn"));

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to your promotions page");
        sideMenu.clickPromoAndCampaignMenu();
        sideMenu.clickYourPromotionsMenu();
        Assertion.assertThat("Your promotions page is loaded", yourPromotionsPage.isYourPromotionsPageLoaded(), is(true));
        
        step("Click toggle button");
        yourPromotionsPage.clickPromoToggle(name, isToggleOn);
        yourPromotionsPage.refreshPage();
        Assertion.assertThat(String.format("The promotion status is changed to be %b", yourPromotionsPage.getPromoToggleState(name)), yourPromotionsPage.getPromoToggleState(name), is(isToggleOn));
    }

    @Author(names = {"Ketan", "Vipul Kumar"})
    @Module(name = "PromoAndCampaign")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToCreateBonusNight() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        AddNewPromoAndCampaignPage<?> addNewPromoAndCampaignPage = new AddNewPromoAndCampaignPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        YourPromotionsPage<?> yourPromotionsPage = new YourPromotionsPage<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToCreateBonusNight");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String name = param.get("name");
        int startDateIndex = Integer.parseInt(param.get("startDateIndex"));
        int endDateIndex = Integer.parseInt(param.get("endDateIndex"));
        String minLenStay= param.get("minLenStay");
        String freeStay = param.get("freeStay");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to add new promo and campaign page");
        sideMenu.clickPromoAndCampaignMenu();
        sideMenu.clickAddNewPromoAndCampaignMenu();
        Assertion.assertThat("Promo and campaign data page is loaded", addNewPromoAndCampaignPage.isAddNewPromoAndCampaignPageLoaded(), is(true));
        addNewPromoAndCampaignPage.dismissPriceSimulationModal();

        step("Go to create Bonus Night");
        addNewPromoAndCampaignPage.goToCreateBonusNightPage();
        Assertion.assertThat("Create Bonus Night page is loaded", addNewPromoAndCampaignPage.isCreateBonusNightPageLoaded(), is(true));
        addNewPromoAndCampaignPage.dismissCancellationPolicyCoachmark();

        step("Fill out the form");
        addNewPromoAndCampaignPage.fillOutPromoNameInput(name);
        addNewPromoAndCampaignPage.clickSelectAllLoginPlatformButton();
        addNewPromoAndCampaignPage.clickSelectAllNonLoginPlatformButton();
        addNewPromoAndCampaignPage.selectDateByDateTypeAndIndex("STAY_PERIOD_START", startDateIndex);
        addNewPromoAndCampaignPage.selectDateByDateTypeAndIndex("STAY_PERIOD_END", endDateIndex);
        addNewPromoAndCampaignPage.fillOutMinLengthStay(minLenStay);
        addNewPromoAndCampaignPage.fillOutFreeStay(freeStay);
        addNewPromoAndCampaignPage.clickNoPromoAccumulateRadioButton();
        addNewPromoAndCampaignPage.clickSelectAllRoomAndRatePlan();
        addNewPromoAndCampaignPage.clickReviewPromoButton();
        addNewPromoAndCampaignPage.clickCreatePromoButton();
        Assertion.assertThat("The promo is created", addNewPromoAndCampaignPage.isPromoCreated(name), is(true));

        step("Revert test data");
        yourPromotionsPage.clickActionByPromoName(name);
        yourPromotionsPage.deletePromo();
        Assertion.assertThat("Success snackbar is appeared", yourPromotionsPage.isSuccessSnackbarAppeared(), is(true));
        yourPromotionsPage.refreshPage();
        Assertion.assertThat("The promo is deleted", addNewPromoAndCampaignPage.isPromoCreated(name), is(false));
    }

    @Author(names = {"Ketan", "Vipul Kumar"})
    @Module(name = "PromoAndCampaign")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToUpdateBonusNight() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        AddNewPromoAndCampaignPage<?> addNewPromoAndCampaignPage = new AddNewPromoAndCampaignPage<>(session);
        YourPromotionsPage<?> yourPromotionsPage = new YourPromotionsPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToUpdateBonusNight");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String name = param.get("name");
        String newMinLenStay = param.get("newMinLenStay");
        String newFreeStay = param.get("newFreeStay");
        String oldMinLenStay = param.get("oldMinLenStay");
        String oldFreeStay = param.get("oldFreeStay");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to your promotions page");
        sideMenu.clickPromoAndCampaignMenu();
        sideMenu.clickYourPromotionsMenu();
        Assertion.assertThat("Your promotions page is loaded", yourPromotionsPage.isYourPromotionsPageLoaded(), is(true));

        step("Click an edit button");
        yourPromotionsPage.clickActionByPromoName(name);
        yourPromotionsPage.clickEditButton();
        Assertion.assertThat("Edit bonus night page is loaded", addNewPromoAndCampaignPage.isCreateBonusNightPageLoaded(), is(true));

        step("Fill out the form with new data");
        addNewPromoAndCampaignPage.fillOutMinLengthStay(newMinLenStay);
        addNewPromoAndCampaignPage.fillOutFreeStay(newFreeStay);
        addNewPromoAndCampaignPage.clickReviewPromoButton();
        addNewPromoAndCampaignPage.clickCreatePromoButton();
        Assertion.assertThat("The promo is edited", addNewPromoAndCampaignPage.isPromoCreated(name), is(true));

        step("Revert test data");
        yourPromotionsPage.clickActionByPromoName(name);
        yourPromotionsPage.clickEditButton();
        Assertion.assertThat("Edit bonus night page is loaded", addNewPromoAndCampaignPage.isCreateBonusNightPageLoaded(), is(true));
        addNewPromoAndCampaignPage.fillOutMinLengthStay(oldMinLenStay);
        addNewPromoAndCampaignPage.fillOutFreeStay(oldFreeStay);
        addNewPromoAndCampaignPage.clickReviewPromoButton();
        addNewPromoAndCampaignPage.clickCreatePromoButton();
        Assertion.assertThat("The promo is edited", addNewPromoAndCampaignPage.isPromoCreated(name), is(true));
    }

    @Author(names = {"Ketan", "Vipul Kumar"})
    @Module(name = "PromoAndCampaign")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToDeleteBonusNight() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        AddNewPromoAndCampaignPage<?> addNewPromoAndCampaignPage = new AddNewPromoAndCampaignPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        YourPromotionsPage<?> yourPromotionsPage = new YourPromotionsPage<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToDeleteBonusNight");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String name = param.get("name");
        int startDateIndex = Integer.parseInt(param.get("startDateIndex"));
        int endDateIndex = Integer.parseInt(param.get("endDateIndex"));
        String minLenStay= param.get("minLenStay");
        String freeStay = param.get("freeStay");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to add new promo and campaign page");
        sideMenu.clickPromoAndCampaignMenu();
        sideMenu.clickAddNewPromoAndCampaignMenu();
        Assertion.assertThat("Promo and campaign data page is loaded", addNewPromoAndCampaignPage.isAddNewPromoAndCampaignPageLoaded(), is(true));
        addNewPromoAndCampaignPage.dismissPriceSimulationModal();

        step("Create Bonus Night First");
        addNewPromoAndCampaignPage.goToCreateBonusNightPage();
        Assertion.assertThat("Create Bonus Night page is loaded", addNewPromoAndCampaignPage.isCreateBonusNightPageLoaded(), is(true));
        addNewPromoAndCampaignPage.dismissCancellationPolicyCoachmark();
        addNewPromoAndCampaignPage.fillOutPromoNameInput(name);
        addNewPromoAndCampaignPage.clickSelectAllLoginPlatformButton();
        addNewPromoAndCampaignPage.clickSelectAllNonLoginPlatformButton();
        addNewPromoAndCampaignPage.selectDateByDateTypeAndIndex("STAY_PERIOD_START", startDateIndex);
        addNewPromoAndCampaignPage.selectDateByDateTypeAndIndex("STAY_PERIOD_END", endDateIndex);
        addNewPromoAndCampaignPage.fillOutMinLengthStay(minLenStay);
        addNewPromoAndCampaignPage.fillOutFreeStay(freeStay);
        addNewPromoAndCampaignPage.clickNoPromoAccumulateRadioButton();
        addNewPromoAndCampaignPage.clickSelectAllRoomAndRatePlan();
        addNewPromoAndCampaignPage.clickReviewPromoButton();
        addNewPromoAndCampaignPage.clickCreatePromoButton();
        Assertion.assertThat("The promo is created", addNewPromoAndCampaignPage.isPromoCreated(name), is(true));

        step("Delete Bonus Night");
        yourPromotionsPage.clickActionByPromoName(name);
        yourPromotionsPage.deletePromo();
        Assertion.assertThat("Success snackbar is appeared", yourPromotionsPage.isSuccessSnackbarAppeared(), is(true));
        yourPromotionsPage.refreshPage();
        Assertion.assertThat("The Bonus Night promo is deleted", addNewPromoAndCampaignPage.isPromoCreated(name), is(false));
    }

    @Author(names = {"Ketan", "Vipul Kumar"})
    @Module(name = "PromoAndCampaign")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToActivateBonusNight() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        YourPromotionsPage<?> yourPromotionsPage = new YourPromotionsPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToActivateBonusNight");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String PromoName = param.get("PromoName");
        boolean isToggleOn = Boolean.parseBoolean(param.get("isToggleOn"));

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to your promotions page");
        sideMenu.clickPromoAndCampaignMenu();
        sideMenu.clickYourPromotionsMenu();
        Assertion.assertThat("Your promotions page is loaded", yourPromotionsPage.isYourPromotionsPageLoaded(), is(true));

        step("Click toggle button");
        yourPromotionsPage.clickPromoToggle(PromoName, isToggleOn);
        yourPromotionsPage.refreshPage();
        Assertion.assertThat(String.format("The promotion status is changed to Activated", yourPromotionsPage.getPromoToggleState(PromoName)), yourPromotionsPage.getPromoToggleState(PromoName), is(isToggleOn));
    }

    @Author(names = {"Ketan", "Vipul Kumar"})
    @Module(name = "PromoAndCampaign")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToDeactivateBonusNight() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        YourPromotionsPage<?> yourPromotionsPage = new YourPromotionsPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToDeactivateBonusNight");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String PromoName = param.get("PromoName");
        boolean isToggleOn = Boolean.parseBoolean(param.get("isToggleOn"));

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to your promotions page");
        sideMenu.clickPromoAndCampaignMenu();
        sideMenu.clickYourPromotionsMenu();
        Assertion.assertThat("Your promotions page is loaded", yourPromotionsPage.isYourPromotionsPageLoaded(), is(true));

        step("Click toggle button");
        yourPromotionsPage.clickPromoToggle(PromoName, isToggleOn);
        yourPromotionsPage.refreshPage();
        Assertion.assertThat(String.format("The promotion status is changed to Deactivated", yourPromotionsPage.getPromoToggleState(PromoName)), yourPromotionsPage.getPromoToggleState(PromoName), is(isToggleOn));
    }

}