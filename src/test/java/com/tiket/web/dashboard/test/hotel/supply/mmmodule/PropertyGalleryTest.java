package com.tiket.web.dashboard.test.hotel.supply.mmmodule;

import com.tiket.apiPage.MmDashboardApi;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.EditRoomPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.HotelLibraryPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.LoginPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.PropertyGalleryPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.SidebarPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.HotelMergePage;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class PropertyGalleryTest extends BaseTest {
    @Author(names = {"Raihan"})
    @Module(name = "HotelImage")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testPropertyGallerySendHotelImageToAurora() {
        Map<String, String> param = getParam("testPropertyGallerySendHotelImageToAurora");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        PropertyGalleryPage<?> propertyGalleryPage = new PropertyGalleryPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Search property by vendor ID");
        hotelLibraryPage.clickSearchByVendorId();
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and gallery button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickPropertyGallery();
        Assertion.assertThat("Property gallery page is loaded", propertyGalleryPage.isHotelPhotosTabActive(), is(true));
        Assertion.assertThat("Image is loaded", propertyGalleryPage.isImageLoaded(), is(true));

        step("Check and send image to Aurora");
        propertyGalleryPage.checkUnratedHotelImage();
        propertyGalleryPage.clickSendHotelImageToAurora();
        Assertion.assertThat("Send hotel image to Aurora modal is displayed", propertyGalleryPage.isSendHotelImageToAuroraModalDisplayed(), is(true));
        propertyGalleryPage.checkHotelImageSceneDetectionModule(true);
        propertyGalleryPage.checkHotelImageQualityCheckingModule(true);
        propertyGalleryPage.checkHotelImageSimilarityCheckingModule(false);
        propertyGalleryPage.clickSubmitSendHotelImageToAurora();
        Assertion.assertThat("Hotel image is successfully being sent to Aurora", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelImage")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testPropertyGalleryIncludeHotelImage() {
        Map<String, String> param = getParam("testPropertyGalleryIncludeHotelImage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        PropertyGalleryPage<?> propertyGalleryPage = new PropertyGalleryPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String imageUrl = param.get("imageUrl");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Search property by vendor ID");
        hotelLibraryPage.clickSearchByVendorId();
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and gallery button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickPropertyGallery();
        Assertion.assertThat("Property gallery page is loaded", propertyGalleryPage.isHotelPhotosTabActive(), is(true));
        Assertion.assertThat("Image is loaded", propertyGalleryPage.isImageLoaded(), is(true));

        step("Check and move image to be included");
        Assertion.assertThat("Hotel image is currently excluded", propertyGalleryPage.isHotelImageExcluded(imageUrl), is(true));
        propertyGalleryPage.checkExcludedHotelImage(imageUrl);
        propertyGalleryPage.clickMoveHotelImage();
        Assertion.assertThat("Move hotel image modal is displayed", propertyGalleryPage.isMoveHotelImageModalDisplayed(), is(true));
        propertyGalleryPage.selectIncludeExcludeConfiguration("Include");
        propertyGalleryPage.clickSubmitMoveHotelImage();
        propertyGalleryPage.clickSaveHotelImage();
        Assertion.assertThat("Hotel image is successfully moved to be included", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed() && propertyGalleryPage.isHotelImageIncluded(imageUrl), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelImage")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test (dependsOnMethods = {"testPropertyGalleryIncludeHotelImage"})
    public void testPropertyGalleryExcludeHotelImage() {
        Map<String, String> param = getParam("testPropertyGalleryExcludeHotelImage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        PropertyGalleryPage<?> propertyGalleryPage = new PropertyGalleryPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String imageUrl = param.get("imageUrl");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Search property by vendor ID");
        hotelLibraryPage.clickSearchByVendorId();
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and gallery button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickPropertyGallery();
        Assertion.assertThat("Property gallery page is loaded", propertyGalleryPage.isHotelPhotosTabActive(), is(true));
        Assertion.assertThat("Image is loaded", propertyGalleryPage.isImageLoaded(), is(true));

        step("Check and move image to be excluded");
        Assertion.assertThat("Hotel image is currently included", propertyGalleryPage.isHotelImageIncluded(imageUrl), is(true));
        propertyGalleryPage.checkIncludedHotelImage(imageUrl);
        propertyGalleryPage.clickMoveHotelImage();
        Assertion.assertThat("Move hotel image modal is displayed", propertyGalleryPage.isMoveHotelImageModalDisplayed(), is(true));
        propertyGalleryPage.selectIncludeExcludeConfiguration("Exclude");
        propertyGalleryPage.clickSubmitMoveHotelImage();
        propertyGalleryPage.clickSaveHotelImage();
        Assertion.assertThat("Hotel image is successfully moved to be excluded", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed() && propertyGalleryPage.isHotelImageExcluded(imageUrl), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelImage")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testPropertyGalleryPinHotelImage() {
        Map<String, String> param = getParam("testPropertyGalleryPinHotelImage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        PropertyGalleryPage<?> propertyGalleryPage = new PropertyGalleryPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String imageUrl = param.get("imageUrl");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Search property by vendor ID");
        hotelLibraryPage.clickSearchByVendorId();
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and gallery button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickPropertyGallery();
        Assertion.assertThat("Property gallery page is displayed", propertyGalleryPage.isHotelPhotosTabActive(), is(true));

        step("Pin hotel image and save");
        propertyGalleryPage.clickManageHotelPhotosTab();
        Assertion.assertThat("Manage hotel photos page is displayed", propertyGalleryPage.isSaveManageHotelPhotoDisabled(), is(true));
        Assertion.assertThat("Hotel image is unpinned yet", propertyGalleryPage.isHotelImagePinnedByUrl(imageUrl), is(false));
        propertyGalleryPage.clickPinHotelImageByImageUrl(imageUrl);
        Assertion.assertThat("Save manage hotel photo button enabled", propertyGalleryPage.isSaveManageHotelPhotoDisabled(), is(false));
        propertyGalleryPage.clickSaveManageHotelPhoto();
        Assertion.assertThat("Hotel image is successfully pinned", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed() && propertyGalleryPage.isHotelImagePinnedByUrl(imageUrl), is(true));

        step("Revert hotel image to initial state");
        Assertion.assertThat("Hotel image is currently pinned", propertyGalleryPage.isHotelImagePinnedByUrl(imageUrl), is(true));
        propertyGalleryPage.clickPinHotelImageByImageUrl(imageUrl);
        Assertion.assertThat("Save manage hotel photo button enabled", propertyGalleryPage.isSaveManageHotelPhotoDisabled(), is(false));
        propertyGalleryPage.clickSaveManageHotelPhoto();
        Assertion.assertThat("Hotel image is successfully unpinned", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed() && propertyGalleryPage.isHotelImagePinnedByUrl(imageUrl), is(false));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelImage")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testPropertyGalleryEditHotelImageOrder() {
        Map<String, String> param = getParam("testPropertyGalleryEditHotelImageOrder");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        PropertyGalleryPage<?> propertyGalleryPage = new PropertyGalleryPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        int sourceImageIndex = Integer.parseInt(param.get("sourceImageIndex"));
        int targetImageIndex = Integer.parseInt(param.get("targetImageIndex"));

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Search property by vendor ID");
        hotelLibraryPage.clickSearchByVendorId();
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and gallery button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickPropertyGallery();
        Assertion.assertThat("Property gallery page is displayed", propertyGalleryPage.isHotelPhotosTabActive(), is(true));

        step("Drag and drop hotel image and save");
        propertyGalleryPage.clickManageHotelPhotosTab();
        Assertion.assertThat("Manage hotel photos page is displayed", propertyGalleryPage.isSaveManageHotelPhotoDisabled(), is(true));
        Assertion.assertThat("Hotel image is pinned", propertyGalleryPage.isHotelImagePinnedByIndex(targetImageIndex), is(true));
        String hotelImageStyle = propertyGalleryPage.getSortedHotelImageIdentifierByIndex(sourceImageIndex);
        propertyGalleryPage.clickPinHotelImageByIndex(targetImageIndex);
        Assertion.assertThat("Source hotel images are currently unpinned", propertyGalleryPage.isHotelImagePinnedByIndex(sourceImageIndex), is(false));
        Assertion.assertThat("Target hotel images are currently unpinned", propertyGalleryPage.isHotelImagePinnedByIndex(targetImageIndex), is(false));
        propertyGalleryPage.dragAndDropSortedHotelImage(sourceImageIndex, targetImageIndex);
        propertyGalleryPage.clickPinHotelImageByIndex(targetImageIndex);
        Assertion.assertThat("Save manage hotel photo button enabled", propertyGalleryPage.isSaveManageHotelPhotoDisabled(), is(false));
        propertyGalleryPage.clickSaveManageHotelPhoto();
        Assertion.assertThat("Hotel image order is successfully updated", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed() && propertyGalleryPage.isSortedHotelImageHasIdentifierByIndex(targetImageIndex, hotelImageStyle), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "RoomImage")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testPropertyGalleryExcludeRoomImageFromRoomGroup() {
        Map<String, String> param = getParam("testPropertyGalleryExcludeRoomImageFromRoomGroup");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        PropertyGalleryPage<?> propertyGalleryPage = new PropertyGalleryPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String roomImageUrl = param.get("roomImageUrl");
        String roomName = param.get("roomName");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Search property by vendor ID");
        hotelLibraryPage.clickSearchByVendorId();
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and gallery button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickPropertyGallery();
        Assertion.assertThat("Property gallery page is loaded", propertyGalleryPage.isHotelPhotosTabActive(), is(true));
        Assertion.assertThat("Image is loaded", propertyGalleryPage.isImageLoaded(), is(true));

        step("Check and exclude room image from room group");
        propertyGalleryPage.clickManageRoomPhotosTab();
        Assertion.assertThat("Room image is included to room group", propertyGalleryPage.isRoomImageInRoomGroupSectionByImageUrl(roomName, roomImageUrl), is(true));
        propertyGalleryPage.checkRoomGroupImageByUrl(roomImageUrl);
        propertyGalleryPage.clickRemoveRoomImage();
        Assertion.assertThat("Remove room image modal is displayed", propertyGalleryPage.isIncludeExcludeRoomGroupImageModalDisplayed(), is(true));
        propertyGalleryPage.clickConfirmIncludeExcludeRoomGroupImage();
        Assertion.assertThat("Room image is successfully removed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
        Assertion.assertThat("Room image is excluded to room group", propertyGalleryPage.isRoomImageInRoomGroupSectionByImageUrl(roomName, roomImageUrl), is(false));
    }

    @Author(names = {"Raihan", "Riya Bajpai"})
    @Module(name = "RoomImage")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test(dependsOnMethods = {"testPropertyGalleryExcludeRoomImageFromRoomGroup"})
    public void testPropertyGalleryIncludeRoomImageToRoomGroup() {
        Map<String, String> param = getParam("testPropertyGalleryIncludeRoomImageToRoomGroup");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        PropertyGalleryPage<?> propertyGalleryPage = new PropertyGalleryPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String roomImageUrl = param.get("roomImageUrl");
        String roomName = param.get("roomName");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Search property by vendor ID");
        hotelLibraryPage.clickSearchByVendorId();
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and gallery button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickPropertyGallery();
        Assertion.assertThat("Property gallery page is loaded", propertyGalleryPage.isHotelPhotosTabActive(), is(true));
        Assertion.assertThat("Image is loaded", propertyGalleryPage.isImageLoaded(), is(true));

        step("Check and include room image from room group");
        propertyGalleryPage.clickManageRoomPhotosTab();
        propertyGalleryPage.removeRoomGroupImageByUrl(roomName, roomImageUrl);
        Assertion.assertThat("Room image is excluded to room group", propertyGalleryPage.isRoomImageInRoomGroupSectionByImageUrl(roomName, roomImageUrl), is(false));
        propertyGalleryPage.showAllRoomImageByRoomName(roomName);
        propertyGalleryPage.checkRoomVendorImageByUrl(roomName, roomImageUrl);
        propertyGalleryPage.clickAddRoomImage();
        Assertion.assertThat("Add room image modal is displayed", propertyGalleryPage.isIncludeExcludeRoomGroupImageModalDisplayed(), is(true));
        propertyGalleryPage.clickConfirmIncludeExcludeRoomGroupImage();
        Assertion.assertThat("Room image is successfully added", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
        Assertion.assertThat("Room image is included to room group", propertyGalleryPage.waitForRoomImageInRoomGroupSectionByImageUrl(roomName, roomImageUrl), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "RoomImage")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test(dependsOnMethods = {"testPropertyGalleryPinRoomVendorImage"})
    public void testPropertyGalleryArrangeRoomVendorImage() {
        Map<String, String> param = getParam("testPropertyGalleryArrangeRoomVendorImage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        PropertyGalleryPage<?> propertyGalleryPage = new PropertyGalleryPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String roomName = param.get("roomName");
        String imageVendorName = param.get("imageVendorName");
        int sourceImageIndex = Integer.parseInt(param.get("sourceImageIndex"));
        int targetImageIndex = Integer.parseInt(param.get("targetImageIndex"));

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Search property by vendor ID");
        hotelLibraryPage.clickSearchByVendorId();
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and gallery button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickPropertyGallery();
        Assertion.assertThat("Property gallery page is loaded", propertyGalleryPage.isHotelPhotosTabActive(), is(true));
        Assertion.assertThat("Image is loaded", propertyGalleryPage.isImageLoaded(), is(true));

        step("Check and arrange room vendor image");
        propertyGalleryPage.clickManageRoomPhotosTab();
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.searchAndSelectRoomGroupTypeFilter(roomName);
        propertyGalleryPage.clickManageRoomPhotosSearchFilter();
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.showAllRoomImageByRoomName(roomName);
        propertyGalleryPage.clickRoomVendorArrangePhoto(roomName, imageVendorName);
        Assertion.assertThat("Vendor image arrange photo modal displayed", propertyGalleryPage.isRoomVendorArrangePhotoModalDisplayed(), is(true));
        String roomImageStyle = propertyGalleryPage.getRoomVendorImageIdentifierByIndex(sourceImageIndex);
        propertyGalleryPage.dragAndDropRoomVendorImage(sourceImageIndex, targetImageIndex);
        propertyGalleryPage.clickSaveRoomImageArrangePhoto();
        Assertion.assertThat("Successful snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
        propertyGalleryPage.refreshPage();
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.showAllRoomImageByRoomName(roomName);
        propertyGalleryPage.clickRoomVendorArrangePhoto(roomName, imageVendorName);
        Assertion.assertThat("Vendor image arrange photo modal displayed", propertyGalleryPage.isRoomVendorArrangePhotoModalDisplayed(), is(true));
        Assertion.assertThat("Room vendor image order is successfully updated", propertyGalleryPage.isRoomVendorImageHasIdentifierByIndex(targetImageIndex, roomImageStyle), is(true));

        step("Revert room vendor image arrangement");
        roomImageStyle = propertyGalleryPage.getRoomVendorImageIdentifierByIndex(sourceImageIndex);
        propertyGalleryPage.dragAndDropRoomVendorImage(sourceImageIndex, targetImageIndex);
        propertyGalleryPage.clickSaveRoomImageArrangePhoto();
        Assertion.assertThat("Successful snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
        propertyGalleryPage.refreshPage();
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.showAllRoomImageByRoomName(roomName);
        propertyGalleryPage.clickRoomVendorArrangePhoto(roomName, imageVendorName);
        Assertion.assertThat("Vendor image arrange photo modal displayed", propertyGalleryPage.isRoomVendorArrangePhotoModalDisplayed(), is(true));
        Assertion.assertThat("Room vendor image order is successfully updated", propertyGalleryPage.isRoomVendorImageHasIdentifierByIndex(targetImageIndex, roomImageStyle), is(true));
    }

    @Author(names = {"Raihan","Riya Bajpai"})
    @Module(name = "RoomImage")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testPropertyGalleryPinRoomVendorImage() {
        Map<String, String> param = getParam("testPropertyGalleryPinRoomVendorImage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        PropertyGalleryPage<?> propertyGalleryPage = new PropertyGalleryPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String roomName = param.get("roomName");
        String imageVendorName = param.get("imageVendorName");
        int imageIndex = Integer.parseInt(param.get("imageIndex"));

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Search property by vendor ID");
        hotelLibraryPage.clickSearchByVendorId();
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and gallery button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickPropertyGallery();
        Assertion.assertThat("Property gallery page is loaded", propertyGalleryPage.isHotelPhotosTabActive(), is(true));
        Assertion.assertThat("Image is loaded", propertyGalleryPage.isImageLoaded(), is(true));

        step("Check and pin room vendor image");
        propertyGalleryPage.clickManageRoomPhotosTab();
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.searchAndSelectRoomGroupTypeFilter(roomName);
        propertyGalleryPage.clickManageRoomPhotosSearchFilter();
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.showAllRoomImageByRoomName(roomName);
        propertyGalleryPage.clickRoomVendorArrangePhoto(roomName, imageVendorName);
        Assertion.assertThat("Vendor image arrange photo modal displayed", propertyGalleryPage.isRoomVendorArrangePhotoModalDisplayed(), is(true));
        if (propertyGalleryPage.isRoomVendorImagePinnedNow(imageIndex)) {
            step("Image was left pinned by an earlier run: unpin and save it first");
            propertyGalleryPage.clickRoomVendorImagePinIcon(imageIndex, false);
            propertyGalleryPage.clickSaveRoomImageArrangePhoto();
            Assertion.assertThat("Successful snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
            propertyGalleryPage.showAllRoomImageByRoomName(roomName);
            propertyGalleryPage.clickRoomVendorArrangePhoto(roomName, imageVendorName);
            Assertion.assertThat("Vendor image arrange photo modal displayed again", propertyGalleryPage.isRoomVendorArrangePhotoModalDisplayed(), is(true));
        }
        Assertion.assertThat("Room vendor image is unpinned before pinning", propertyGalleryPage.isRoomVendorImageUnpinned(imageIndex), is(true));
        propertyGalleryPage.clickRoomVendorImagePinIcon(imageIndex, true);
        propertyGalleryPage.clickSaveRoomImageArrangePhoto();
        Assertion.assertThat("Successful snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.refreshPage();
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.showAllRoomImageByRoomName(roomName);
        propertyGalleryPage.clickRoomVendorArrangePhoto(roomName, imageVendorName);
        Assertion.assertThat("Vendor image arrange photo modal displayed", propertyGalleryPage.isRoomVendorArrangePhotoModalDisplayed() && propertyGalleryPage.isRoomVendorImagePinned(imageIndex), is(true));

        step("Revert room vendor image to be unpinned");
        propertyGalleryPage.clickRoomVendorImagePinIcon(imageIndex, false);
        propertyGalleryPage.clickSaveRoomImageArrangePhoto();
        Assertion.assertThat("Successful snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.showAllRoomImageByRoomName(roomName);
        propertyGalleryPage.clickRoomVendorArrangePhoto(roomName, imageVendorName);
        Assertion.assertThat("Vendor image arrange photo modal displayed", propertyGalleryPage.isRoomVendorArrangePhotoModalDisplayed() && propertyGalleryPage.isRoomVendorImageUnpinned(imageIndex), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "RoomImage")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test(dependsOnMethods = {"testPropertyGalleryPinRoomGroupImage"})
    public void testPropertyGalleryArrangeRoomGroupImage() {
        Map<String, String> param = getParam("testPropertyGalleryArrangeRoomGroupImage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        PropertyGalleryPage<?> propertyGalleryPage = new PropertyGalleryPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String roomName = param.get("roomName");
        int sourceImageIndex = Integer.parseInt(param.get("sourceImageIndex"));
        int targetImageIndex = Integer.parseInt(param.get("targetImageIndex"));

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Search property by vendor ID");
        hotelLibraryPage.clickSearchByVendorId();
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and gallery button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickPropertyGallery();
        Assertion.assertThat("Property gallery page is loaded", propertyGalleryPage.isHotelPhotosTabActive(), is(true));
        Assertion.assertThat("Image is loaded", propertyGalleryPage.isImageLoaded(), is(true));

        step("Check and arrange room vendor image");
        propertyGalleryPage.clickManageRoomPhotosTab();
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.searchAndSelectRoomGroupTypeFilter(roomName);
        propertyGalleryPage.clickManageRoomPhotosSearchFilter();
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.clickRoomGroupArrangePhoto(roomName);
        Assertion.assertThat("Room group image arrange photo modal displayed", propertyGalleryPage.isRoomGroupArrangePhotoModalDisplayed() && propertyGalleryPage.isRoomGroupImageUnpinned(sourceImageIndex), is(true));
        String roomImageStyle = propertyGalleryPage.getRoomGroupImageIdentifierByIndex(sourceImageIndex);
        propertyGalleryPage.dragAndDropRoomGroupImage(sourceImageIndex, targetImageIndex);
        propertyGalleryPage.clickRoomGroupImagePinIcon(sourceImageIndex, true);
        propertyGalleryPage.clickSaveRoomImageArrangePhoto();
        Assertion.assertThat("Successful snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.refreshPage();
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.clickRoomGroupArrangePhoto(roomName);
        Assertion.assertThat("Room group image arrange photo modal displayed", propertyGalleryPage.isRoomGroupArrangePhotoModalDisplayed() && propertyGalleryPage.isRoomGroupImagePinned(sourceImageIndex), is(true));
        Assertion.assertThat("Room group image order is successfully updated", propertyGalleryPage.isRoomGroupImageHasIdentifierByIndex(targetImageIndex, roomImageStyle), is(true));

        step("Revert room group image arrangement");
        propertyGalleryPage.clickRoomGroupImagePinIcon(sourceImageIndex, false);
        roomImageStyle = propertyGalleryPage.getRoomGroupImageIdentifierByIndex(sourceImageIndex);
        propertyGalleryPage.dragAndDropRoomGroupImage(sourceImageIndex, targetImageIndex);
        propertyGalleryPage.clickSaveRoomImageArrangePhoto();
        Assertion.assertThat("Successful snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
        propertyGalleryPage.clickRoomGroupArrangePhoto(roomName);
        Assertion.assertThat("Room group image arrange photo modal displayed", propertyGalleryPage.isRoomGroupArrangePhotoModalDisplayed() && propertyGalleryPage.isRoomGroupImageUnpinned(sourceImageIndex), is(true));
        Assertion.assertThat("Room group image order is successfully updated", propertyGalleryPage.isRoomGroupImageHasIdentifierByIndex(targetImageIndex, roomImageStyle), is(true));
    }


    @Author(names = {"Raihan"})
    @Module(name = "RoomImage")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testPropertyGalleryPinRoomGroupImage() {
        Map<String, String> param = getParam("testPropertyGalleryPinRoomGroupImage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        PropertyGalleryPage<?> propertyGalleryPage = new PropertyGalleryPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String roomName = param.get("roomName");
        int imageIndex = Integer.parseInt(param.get("imageIndex"));

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Search property by vendor ID");
        hotelLibraryPage.clickSearchByVendorId();
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and gallery button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickPropertyGallery();
        Assertion.assertThat("Property gallery page is loaded", propertyGalleryPage.isHotelPhotosTabActive(), is(true));
        Assertion.assertThat("Image is loaded", propertyGalleryPage.isImageLoaded(), is(true));

        step("Check and pin room group image");
        propertyGalleryPage.clickManageRoomPhotosTab();
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.searchAndSelectRoomGroupTypeFilter(roomName);
        propertyGalleryPage.clickManageRoomPhotosSearchFilter();
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.clickRoomGroupArrangePhoto(roomName);
        Assertion.assertThat("Room group image arrange photo modal displayed", propertyGalleryPage.isRoomGroupArrangePhotoModalDisplayed() && propertyGalleryPage.isRoomGroupImageUnpinned(imageIndex), is(true));
        propertyGalleryPage.clickRoomGroupImagePinIcon(imageIndex, true);
        propertyGalleryPage.clickSaveRoomImageArrangePhoto();
        Assertion.assertThat("Successful snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.refreshPage();
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.clickRoomGroupArrangePhoto(roomName);
        Assertion.assertThat("Room group image arrange photo modal displayed", propertyGalleryPage.isRoomGroupArrangePhotoModalDisplayed() && propertyGalleryPage.isRoomGroupImagePinned(imageIndex), is(true));

        step("Revert room group image to be unpinned");
        propertyGalleryPage.clickRoomGroupImagePinIcon(imageIndex, false);
        propertyGalleryPage.clickSaveRoomImageArrangePhoto();
        Assertion.assertThat("Successful snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
        Assertion.assertThat(String.format("%s room group card is available", roomName), propertyGalleryPage.isRoomGroupCardDisplayed(roomName), is(true));
        propertyGalleryPage.clickRoomGroupArrangePhoto(roomName);
        Assertion.assertThat("Room group image arrange photo modal displayed", propertyGalleryPage.isRoomGroupArrangePhotoModalDisplayed() && propertyGalleryPage.isRoomGroupImageUnpinned(imageIndex), is(true));
    }

    @Author(names = {"Manan Kumar","Riya Bajpai"})
    @Module(name = "HotelImage")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserCanEditPhoto() {
        Map<String, String> param = getParam("testVerifyUserCanEditPhoto");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        PropertyGalleryPage<?> propertyGalleryPage = new PropertyGalleryPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String propertyName = param.get("propertyName");
        String category = param.get("category");
        String label = param.get("label");
        String quality = param.get("quality");
        String roomGroup = param.get("roomGroup");
        String username = param.get("username");
        String password = param.get("password");

        MmDashboardApi<?> mmDashboardApi = new MmDashboardApi<>(session);
        String hotelId = mmDashboardApi.getHotelIdByVendor(vendorName.toUpperCase(), vendorExternalId);
        Set<String> unscoredBefore = mmDashboardApi.getUnscoredImageIds(hotelId);
        String movedPhotoId = null;
        boolean photoLeftInExclude = false;

        try {
            step("Open MM Module");
            loginPage.openMMModule(username, password);
            Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
            Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

            step("User clicks on vendor ID");
            hotelLibraryPage.clickSearchByVendorId();

            step("Search property by vendor ID");
            hotelLibraryPage.selectVendorName(vendorName);

            step("Input vendor external ID");
            hotelLibraryPage.inputVendorExternalId(vendorExternalId);

            step("Click search button");
            hotelLibraryPage.clickSearch();
            Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

            step("Click action.");
            hotelLibraryPage.clickAction();

            step("Click property gallery button");
            hotelLibraryPage.clickPropertyGallery();
            Assertion.assertThat("Property gallery page is loaded", propertyGalleryPage.isHotelPhotosTabActive(), is(true));

            step("Select first photo from Not scored yet section");
            propertyGalleryPage.checkFirstUnratedHotelImage();

            step("Click on Edit button");
            propertyGalleryPage.clickMoveHotelImage();

            step("Verify move hotel image modal is displayed");
            Assertion.assertThat("Move hotel image modal is displayed", propertyGalleryPage.isMoveHotelImageModalDisplayed(), is(true));

            step("Click include Button");
            propertyGalleryPage.selectIncludeExcludeConfiguration("Include");

            step(String.format("Select photo category as %s", category));
            propertyGalleryPage.selectEditPhotoCategory(category);

            step(String.format("Select photo label as %s", label));
            propertyGalleryPage.selectEditPhotoLabel(label);

            step(String.format("Select good/bad configuration as %s", quality));
            propertyGalleryPage.selectEditPhotoQuality(quality);

            step("Click on Move button");
            propertyGalleryPage.clickSubmitMoveHotelImage();

            step("Click on save button");
            propertyGalleryPage.clickSaveHotelImage();

            step("Verify successful toast snackbar is displayed");
            Assertion.assertThat("Successful toast snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));

            step("Click on Room Photos tab");
            propertyGalleryPage.clickRoomPhotosTab();

            step(String.format("Select room group as %s", roomGroup));
            propertyGalleryPage.selectRoomPhotoRoomGroup(roomGroup);

            step("Click search button");
            propertyGalleryPage.clickRoomPhotosSearchFilter();

            step("Move from Include to Exclude: select first Not Scored Yet photo in the include column and remember it");
            movedPhotoId = propertyGalleryPage.checkFirstNotScoredRoomPhotoFrom("include");

            step("Click on Edit button");
            propertyGalleryPage.clickEditPhoto();

            step("Move photo to Exclude");
            propertyGalleryPage.selectIncludeExcludeConfiguration("Exclude");

            step("Click on Move button");
            propertyGalleryPage.clickSubmitMoveHotelImage();

            step("Click on save button");
            propertyGalleryPage.clickSaveRoomPhotos();

            step("Verify successful toast snackbar is displayed");
            Assertion.assertThat("Successful toast snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));

            photoLeftInExclude = true;

            step("Move the same photo back from Exclude to Include");
            propertyGalleryPage.checkNotScoredRoomPhotoFrom("exclude", movedPhotoId);

            step("Click on Edit button");
            propertyGalleryPage.clickEditPhoto();

            step("Move photo to Include");
            propertyGalleryPage.selectIncludeExcludeConfiguration("Include");

            step("Click on Move button");
            propertyGalleryPage.clickSubmitMoveHotelImage();

            step("Click on save button");
            propertyGalleryPage.clickSaveRoomPhotos();

            step("Verify successful toast snackbar is displayed");
            Assertion.assertThat("Successful toast snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));

            photoLeftInExclude = false;

            step("Verify Room Group Image section has image after the photo is included again");
            Assertion.assertThat("Room Group Image is displayed", propertyGalleryPage.isRoomGroupImagesDisplayed(), is(true));
        } finally {
            if (photoLeftInExclude) {
                step("Cleanup: the test stopped while the room photo was in Exclude, move it back to Include");
                propertyGalleryPage.restoreRoomPhotoToInclude(movedPhotoId);
            }
            // The move scores the photos and the UI cannot undo it: restore them so the test is repeatable.
            step("Cleanup: restore photos moved by this test back to Not scored yet");
            mmDashboardApi.restoreUnscoredImages(hotelId, unscoredBefore);
        }
    }

    @Author(names = {"Manan Kumar","Riya Bajpai"})
    @Module(name = "HotelImage")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserCanUploadEditAndManageHotelImagesAndSendToAurora() {
        Map<String, String> param = getParam("verifyUserCanUploadEditAndManageHotelImagesAndSendToAurora");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        PropertyGalleryPage<?> propertyGalleryPage = new PropertyGalleryPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String imagePath = param.get("imagePath");
        String category = param.get("category");
        String label = param.get("label");
        String quality = param.get("quality");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));

        step("Filter Hotel Library by official district");
        hotelLibraryPage.selectSearchType("official");
        Assertion.assertThat("Official district search type is selected", hotelLibraryPage.isSearchTypeSelected("official"), is(true));

        step("Click search button");
        hotelLibraryPage.clickSearch();

        step("Click Action > Gallery > Hotel Photos");
        hotelLibraryPage.clickAction();

        step("Click property gallery button");
        hotelLibraryPage.clickPropertyGallery();
        Assertion.assertThat("Property gallery page is loaded", propertyGalleryPage.isHotelPhotosTabActive(), is(true));

        step("Upload hotel photo, set category and label, then validate image is showing in gallery");
        int userImageCountBefore = propertyGalleryPage.getUserUploadedImageCount();
        propertyGalleryPage.clickUploadHotelImage();

        step("Verify upload hotel image modal is displayed");
        Assertion.assertThat("Upload hotel image modal is displayed", propertyGalleryPage.isUploadHotelImageModalDisplayed(), is(true));

        step("Upload hotel image");
        propertyGalleryPage.uploadHotelImage(new File(imagePath));

        step("Verify uploaded image preview is displayed");
        Assertion.assertThat("Uploaded image preview is displayed", propertyGalleryPage.isUploadHotelImagePreviewDisplayed(), is(true));

        step("Click save button");
        propertyGalleryPage.clickSaveUploadHotelImageButton();

        step("Verify select category and label modal is displayed");
        Assertion.assertThat("Select category and label modal is displayed", propertyGalleryPage.isUploadHotelImageSelectCategoryAndLabelModalDisplayed(), is(true));

        step("Select category");
        propertyGalleryPage.selectHotelImageCategory(category);

        step("Select label");
        propertyGalleryPage.selectHotelImageLabel(label);

        step("Click save button");
        propertyGalleryPage.clickSaveUploadHotelImageCategory();

        step("Verify successful toast snackbar is displayed");
        Assertion.assertThat("Successful toast snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
        Assertion.assertThat("Toast message contains 'successfully uploaded'", propertyGalleryPage.getSuccessfulToastSnackbarText(), containsString("successfully uploaded"));

        step("Verify uploaded image is displayed in gallery");
        Assertion.assertThat("Uploaded image is displayed in gallery", propertyGalleryPage.isImageLoaded(), is(true));
        int userImageCountAfter = propertyGalleryPage.getUserUploadedImageCount();
        Assertion.assertThat(String.format("USER image count increased by 1 after upload [before: %d, after: %d]", userImageCountBefore, userImageCountAfter), userImageCountAfter, is(userImageCountBefore + 1));

        step("Remember the uploaded image so it can be pinned later");
        String uploadedImageKey = propertyGalleryPage.getLastUserUploadedImageKey();

        step("Select the uploaded image");
        propertyGalleryPage.selectLastUserUploadedImage();

        step("Click Edit Photo");
        propertyGalleryPage.clickMoveHotelImage();

        step("Verify Move Section modal is displayed");
        Assertion.assertThat("Move Section modal is displayed", propertyGalleryPage.isMoveHotelImageModalDisplayed(), is(true));

        step("Fill in Edit Photo details - Include, Category, Label, Quality");
        propertyGalleryPage.selectIncludeExcludeConfiguration("Include");

        step("Select category");
        propertyGalleryPage.selectEditPhotoCategory(category);

        step("Select label");
        propertyGalleryPage.selectEditPhotoLabel(label);

        step("Select quality");
        propertyGalleryPage.selectEditPhotoQuality(quality);

        step("Click Move button");
        propertyGalleryPage.clickSubmitMoveHotelImage();

        step("Select the uploaded image");
        propertyGalleryPage.selectLastUserUploadedImage();

        step("Click Send to Aurora");
        propertyGalleryPage.clickSendHotelImageToAurora();

        step("Verify Send to Aurora modal is displayed");
        Assertion.assertThat("Send to Aurora modal is displayed", propertyGalleryPage.isSendHotelImageToAuroraModalDisplayed(), is(true));

        step("Tick all Aurora modules");
        propertyGalleryPage.checkHotelImageSceneDetectionModule(true);
        propertyGalleryPage.checkHotelImageQualityCheckingModule(true);
        propertyGalleryPage.checkHotelImageSimilarityCheckingModule(true);

        step("Click Send button");
        propertyGalleryPage.clickSubmitSendHotelImageToAurora();

        step("Verify successful toast snackbar is displayed after sending to Aurora");
        Assertion.assertThat("Successful toast snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
        Assertion.assertThat("Toast message contains 'successfully'", propertyGalleryPage.getSuccessfulToastSnackbarText(), containsString("successfully"));

        step("Wait for the Aurora toast to disappear so the next toast check is not a stale one");
        propertyGalleryPage.waitForSuccessfulToastSnackbarToDisappear();

        step("Click Manage Hotel Photos tab");
        propertyGalleryPage.clickManageHotelPhotosTab();

        step("Pin the user uploaded image");
        Assertion.assertThat("User uploaded image is pinned", propertyGalleryPage.pinHotelImageByKeyWithRetry(uploadedImageKey), is(true));

        step("Save manage hotel photos");
        propertyGalleryPage.clickSaveManageHotelPhoto();

        step("Verify image is pinned successfully");
        Assertion.assertThat("Successful toast snackbar is displayed after pinning", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
        Assertion.assertThat("Toast message contains 'success'", propertyGalleryPage.getSuccessfulToastSnackbarText(), containsStringIgnoringCase("success"));

    }

    @Author(names = "Abhishek Sharma")
    @Module(name = "HotelImage")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelListOfficialDistrictManageRoomPropertyImages() {
        Map<String, String> param = getParam("testHotelListOfficialDistrictManageRoomPropertyImages");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        PropertyGalleryPage<?> propertyGalleryPage = new PropertyGalleryPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchType = param.get("searchType");
        int sourceImageIndex = Integer.parseInt(param.get("sourceImageIndex"));
        int targetImageIndex = Integer.parseInt(param.get("targetImageIndex"));
        String roomImageStyleSource, roomImageStyleTarget, hotelImageStyleSource, hotelImageStyleTarget;

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Search by Search Type: " + searchType);
        hotelLibraryPage.selectSearchType(searchType);
        Assertion.assertThat(String.format("Search type selected: %s", searchType), hotelLibraryPage.isSearchTypeSelected(searchType), is(true));

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Click Action > Gallery Button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickPropertyGallery();
        Assertion.assertThat("Property gallery page is displayed", propertyGalleryPage.isHotelPhotosTabActive(), is(true));

        step("Click Manage Hotel Photos tab");
        propertyGalleryPage.clickManageHotelPhotosTab();
        Assertion.assertThat("Manage hotel photos page is active", propertyGalleryPage.isManageHotelPhotosTabActive(), is(true));

        step("Drag and drop hotel image from index %d to index %d and save".formatted(sourceImageIndex, targetImageIndex));
        hotelImageStyleSource = propertyGalleryPage.getSortedHotelImageIdentifierByIndex(sourceImageIndex);
        hotelImageStyleTarget = propertyGalleryPage.getSortedHotelImageIdentifierByIndex(targetImageIndex);

        if (propertyGalleryPage.isHotelImagePinnedByIndex(sourceImageIndex)) {
            propertyGalleryPage.clickPinHotelImageByIndex(sourceImageIndex);
        }
        if (propertyGalleryPage.isHotelImagePinnedByIndex(targetImageIndex)) {
            propertyGalleryPage.clickPinHotelImageByIndex(targetImageIndex);
        }

        Assertion.assertThat("Source hotel image is currently unpinned", propertyGalleryPage.isHotelImagePinnedByIndex(sourceImageIndex), is(false));
        Assertion.assertThat("Target hotel image is currently unpinned", propertyGalleryPage.isHotelImagePinnedByIndex(targetImageIndex), is(false));
        propertyGalleryPage.dragAndDropSortedHotelImage(sourceImageIndex, targetImageIndex);
        Assertion.assertThat("Hotel image order is updated after drag", propertyGalleryPage.isSortedHotelImageHasIdentifierByIndex(sourceImageIndex, hotelImageStyleTarget), is(true));
        propertyGalleryPage.pinHotelImageByIdentifierWithRetry(hotelImageStyleTarget);
        Assertion.assertThat("Swapped hotel image is pinned before save", propertyGalleryPage.isHotelImagePinnedByIdentifier(hotelImageStyleTarget), is(true));
        Assertion.assertThat("Save manage hotel photo button enabled", propertyGalleryPage.isSaveManageHotelPhotoDisabled(), is(false));
        propertyGalleryPage.clickSaveManageHotelPhoto();
        Assertion.assertThat("successfully updated snackbar appears", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
        Assertion.assertThat("Source hotel image is currently pinned", propertyGalleryPage.isHotelImagePinnedByIdentifier(hotelImageStyleTarget), is(true));
        Assertion.assertThat("Hotel source image order is successfully updated", propertyGalleryPage.isSortedHotelImageHasIdentifierByIndex(sourceImageIndex, hotelImageStyleTarget), is(true));
        Assertion.assertThat("Hotel target image order is successfully updated", propertyGalleryPage.isSortedHotelImageHasIdentifierByIndex(targetImageIndex, hotelImageStyleSource), is(true));

        step("Click Manage Room Photos tab");
        propertyGalleryPage.clickManageRoomPhotosTab();
        Assertion.assertThat("Manage room photos page is active", propertyGalleryPage.isManageRoomPhotosTabActive(), is(true));

        step("Filter by room group name (Room group with photos)");
        List<String> roomGroupNames = propertyGalleryPage.getAllRoomGroupTypeFilters();
        Collections.reverse(roomGroupNames);
        String selectedRoomGroupName = null, selectedVendorRoomName = null, url = null;
        List<String> groupUrls = new ArrayList<>(), vendorRoomNames = new ArrayList<>(), vendorUrls = new ArrayList<>();
        // Used only when every vendor photo is already in its room group: setup then frees one photo by removing it from this room group.
        String fallbackRoomGroupName = null, fallbackVendorRoomName = null;
        List<String> fallbackGroupUrls = new ArrayList<>(), fallbackVendorRoomNames = new ArrayList<>(), fallbackVendorUrls = new ArrayList<>();

        for (String roomGroupName : roomGroupNames) {
            propertyGalleryPage.showAllRoomImageByRoomName(roomGroupName);
            groupUrls = propertyGalleryPage.getAllRoomGroupImageUrlsByRoomName(roomGroupName);

            if (!groupUrls.isEmpty() && groupUrls.size() > 1){
                vendorRoomNames = propertyGalleryPage.getAllVendorRoomNamesByRoomGroup(roomGroupName);
                propertyGalleryPage.showAllRoomImageByRoomName(roomGroupName);

                for (String vendorRoomName : vendorRoomNames) {
                    vendorUrls = propertyGalleryPage.getAllRoomVendorImageUrlsByRoomNameAndVendorRoomName(roomGroupName, vendorRoomName);

                    if (fallbackRoomGroupName == null && groupUrls.size() > 2 && vendorUrls.size() > 2) {
                        fallbackRoomGroupName = roomGroupName;
                        fallbackVendorRoomName = vendorRoomName;
                        fallbackGroupUrls = new ArrayList<>(groupUrls);
                        fallbackVendorRoomNames = new ArrayList<>(vendorRoomNames);
                        fallbackVendorUrls = new ArrayList<>(vendorUrls);
                    }

                    if (!vendorUrls.isEmpty() && vendorUrls.size() > 1){
                        for (String vendorUrl: vendorUrls){
                            if (!propertyGalleryPage.isRoomImageInRoomGroupSectionByImageUrl(roomGroupName, vendorUrl)){
                                url = vendorUrl;
                                break;
                            }
                        }

                        if (url != null) {
                            selectedVendorRoomName = vendorRoomName;
                            break;
                        }
                    }
                }

                if (selectedVendorRoomName != null) {
                    selectedRoomGroupName = roomGroupName;
                    break;
                }
            }
        }

        // Precondition: the Add/Remove steps need a vendor photo that is not yet in the room group.
        // If earlier runs left every vendor photo assigned, free one by removing an assigned photo from the room group.
        boolean needsFreedPhoto = url == null;
        if (needsFreedPhoto) {
            selectedRoomGroupName = fallbackRoomGroupName;
            selectedVendorRoomName = fallbackVendorRoomName;
            groupUrls = fallbackGroupUrls;
            vendorRoomNames = fallbackVendorRoomNames;
            vendorUrls = fallbackVendorUrls;
            Assertion.assertThat("Verify a room group with vendor photos exists for setup", selectedRoomGroupName, notNullValue());
            for (String vendorUrl : vendorUrls) {
                if (propertyGalleryPage.isRoomImageInRoomGroupSectionByImageUrl(selectedRoomGroupName, vendorUrl)) {
                    url = vendorUrl;
                    break;
                }
            }
            Assertion.assertThat("Verify a vendor photo assigned to the room group exists to free up", url, notNullValue());
        }
        Assertion.assertThat("Verify Selected Room Group Name is present", selectedRoomGroupName, notNullValue());
        propertyGalleryPage.searchAndSelectRoomGroupTypeFilter(selectedRoomGroupName);
        propertyGalleryPage.clickManageRoomPhotosSearchFilter();
        Assertion.assertThat("Verify Selected Room Group Images are more than 1", groupUrls.size(), greaterThan(2));
        Assertion.assertThat("Verify Selected Room Vendors are not empty", vendorRoomNames.size(), greaterThan(0));
        Assertion.assertThat("Verify Room Vendor is present", selectedVendorRoomName, notNullValue());
        Assertion.assertThat("Verify Selected Room Vendor Images are more than 1", vendorUrls.size(), greaterThan(2));
        if (needsFreedPhoto) {
            step("Setup: remove vendor photo from room group so the Add step has a photo to add");
            Assertion.assertThat("Setup: vendor photo is removed from room group", propertyGalleryPage.removeRoomGroupImageByUrl(selectedRoomGroupName, url), is(true));
            propertyGalleryPage.searchAndSelectRoomGroupTypeFilter(selectedRoomGroupName);
            propertyGalleryPage.clickManageRoomPhotosSearchFilter();
        }
        propertyGalleryPage.showAllRoomImageByRoomName(selectedRoomGroupName);
        Assertion.assertThat("Verify url not present in room group", propertyGalleryPage.isRoomImageInRoomGroupSectionByImageUrl(selectedRoomGroupName, url), is(false));

        boolean addedImageStillInRoomGroup = false;
        try {
            step("Select Vendor Image and Add it");
            propertyGalleryPage.showAllRoomImageByRoomName(selectedRoomGroupName);
            propertyGalleryPage.checkRoomVendorImageByRoomNameAndVendorRoomAndUrl(selectedRoomGroupName, selectedVendorRoomName, url);
            propertyGalleryPage.clickAddRoomImage();
            Assertion.assertThat("Add room image modal is displayed", propertyGalleryPage.isIncludeExcludeRoomGroupImageModalDisplayed(), is(true));
            propertyGalleryPage.clickConfirmIncludeExcludeRoomGroupImage();
            addedImageStillInRoomGroup = true;
            Assertion.assertThat("Room image is successfully added", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
            Assertion.assertThat("Room image is included to room group", propertyGalleryPage.waitForRoomImageInRoomGroupSectionByImageUrl(selectedRoomGroupName, url), is(true));

            step("Click Arrange Photo on room group image section > Change the image order by drag and drop > Pin the first image > Save");
            propertyGalleryPage.clickRoomGroupArrangePhoto(selectedRoomGroupName);
            Assertion.assertThat("Room group image arrange photo modal displayed", propertyGalleryPage.isRoomGroupArrangePhotoModalDisplayed(), is(true));
            roomImageStyleSource = propertyGalleryPage.getRoomGroupImageIdentifierByIndex(sourceImageIndex);
            roomImageStyleTarget = propertyGalleryPage.getRoomGroupImageIdentifierByIndex(targetImageIndex);
            propertyGalleryPage.clickRoomGroupImagePinIcon(sourceImageIndex, false);
            propertyGalleryPage.clickRoomGroupImagePinIcon(targetImageIndex, false);
            Assertion.assertThat("Room group source image is unpinned", propertyGalleryPage.isRoomGroupImageUnpinned(sourceImageIndex), is(true));
            Assertion.assertThat("Room group target image is unpinned", propertyGalleryPage.isRoomGroupImageUnpinned(targetImageIndex), is(true));
            propertyGalleryPage.dragAndDropRoomGroupImage(sourceImageIndex, targetImageIndex);
            Assertion.assertThat("Room group image order changed after drag", propertyGalleryPage.isRoomGroupImageHasIdentifierByIndex(sourceImageIndex, roomImageStyleTarget), is(true));
            propertyGalleryPage.clickRoomGroupImagePinIcon(sourceImageIndex, true);
            propertyGalleryPage.clickSaveRoomImageArrangePhoto();
            Assertion.assertThat("Successful snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
            propertyGalleryPage.refreshPage();
            Assertion.assertThat(String.format("%s room group card is available", selectedRoomGroupName), propertyGalleryPage.isRoomGroupCardDisplayed(selectedRoomGroupName), is(true));
            propertyGalleryPage.clickRoomGroupArrangePhoto(selectedRoomGroupName);
            Assertion.assertThat("Room group image arrange photo modal displayed", propertyGalleryPage.isRoomGroupArrangePhotoModalDisplayed(), is(true));
            Assertion.assertThat("Room group source image is pinned", propertyGalleryPage.isRoomGroupImagePinned(sourceImageIndex), is(true));
            Assertion.assertThat("Room group source image order is successfully updated", propertyGalleryPage.isRoomGroupImageHasIdentifierByIndex(sourceImageIndex, roomImageStyleTarget), is(true));
            Assertion.assertThat("Room group target image order is successfully updated", propertyGalleryPage.isRoomGroupImageHasIdentifierByIndex(targetImageIndex, roomImageStyleSource), is(true));

            step("Revert room group image arrangement");
            if (!propertyGalleryPage.isRoomGroupArrangePhotoModalDisplayed()){
                propertyGalleryPage.clickRoomGroupArrangePhoto(selectedRoomGroupName);
            }
            Assertion.assertThat("Room group image arrange photo modal displayed", propertyGalleryPage.isRoomGroupArrangePhotoModalDisplayed(), is(true));
            propertyGalleryPage.clickRoomGroupImagePinIcon(sourceImageIndex, false);
            propertyGalleryPage.clickRoomGroupImagePinIcon(targetImageIndex, false);
            roomImageStyleSource = propertyGalleryPage.getRoomGroupImageIdentifierByIndex(sourceImageIndex);
            roomImageStyleTarget = propertyGalleryPage.getRoomGroupImageIdentifierByIndex(targetImageIndex);
            propertyGalleryPage.clickRoomGroupImagePinIcon(sourceImageIndex, false);
            propertyGalleryPage.clickRoomGroupImagePinIcon(targetImageIndex, false);
            Assertion.assertThat("Room group source image is unpinned", propertyGalleryPage.isRoomGroupImageUnpinned(sourceImageIndex), is(true));
            Assertion.assertThat("Room group target image is unpinned", propertyGalleryPage.isRoomGroupImageUnpinned(targetImageIndex), is(true));
            propertyGalleryPage.dragAndDropRoomGroupImage(sourceImageIndex, targetImageIndex);
            Assertion.assertThat("Room group image order changed after drag", propertyGalleryPage.isRoomGroupImageHasIdentifierByIndex(sourceImageIndex, roomImageStyleTarget), is(true));
            propertyGalleryPage.clickRoomGroupImagePinIcon(sourceImageIndex, true);
            propertyGalleryPage.clickSaveRoomImageArrangePhoto();
            Assertion.assertThat("Successful snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
            propertyGalleryPage.refreshPage();
            Assertion.assertThat(String.format("%s room group card is available", selectedRoomGroupName), propertyGalleryPage.isRoomGroupCardDisplayed(selectedRoomGroupName), is(true));
            propertyGalleryPage.clickRoomGroupArrangePhoto(selectedRoomGroupName);
            Assertion.assertThat("Room group image arrange photo modal displayed", propertyGalleryPage.isRoomGroupArrangePhotoModalDisplayed(), is(true));
            Assertion.assertThat("Room group source image is pinned", propertyGalleryPage.isRoomGroupImagePinned(sourceImageIndex), is(true));
            Assertion.assertThat("Room group source image order is successfully updated", propertyGalleryPage.isRoomGroupImageHasIdentifierByIndex(sourceImageIndex, roomImageStyleTarget), is(true));
            Assertion.assertThat("Room group target image order is successfully updated", propertyGalleryPage.isRoomGroupImageHasIdentifierByIndex(targetImageIndex, roomImageStyleSource), is(true));

            step("Click Arrange Photo on room image section > Change the image order by drag and drop > Pin the first image > Save");
            propertyGalleryPage.refreshPage();
            propertyGalleryPage.showAllRoomImageByRoomName(selectedRoomGroupName);
            propertyGalleryPage.clickRoomVendorArrangePhoto(selectedRoomGroupName, selectedVendorRoomName);
            Assertion.assertThat("Vendor image arrange photo modal displayed", propertyGalleryPage.isRoomVendorArrangePhotoModalDisplayed(), is(true));
            roomImageStyleSource = propertyGalleryPage.getRoomVendorImageIdentifierByIndex(sourceImageIndex);
            roomImageStyleTarget = propertyGalleryPage.getRoomVendorImageIdentifierByIndex(targetImageIndex);
            propertyGalleryPage.clickRoomVendorImagePinIcon(sourceImageIndex, false);
            propertyGalleryPage.clickRoomVendorImagePinIcon(targetImageIndex, false);
            Assertion.assertThat("Vendor source image is unpinned", propertyGalleryPage.isRoomVendorImageUnpinned(sourceImageIndex), is(true));
            Assertion.assertThat("Vendor target image is unpinned", propertyGalleryPage.isRoomVendorImageUnpinned(targetImageIndex), is(true));
            propertyGalleryPage.dragAndDropRoomVendorImage(sourceImageIndex, targetImageIndex);
            Assertion.assertThat("Room vendor image order changed after drag", propertyGalleryPage.isRoomVendorImageHasIdentifierByIndex(sourceImageIndex, roomImageStyleTarget), is(true));
            propertyGalleryPage.clickRoomVendorImagePinIcon(sourceImageIndex, true);
            propertyGalleryPage.clickSaveRoomImageArrangePhoto();
            Assertion.assertThat("Successful snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
            propertyGalleryPage.refreshPage();
            Assertion.assertThat(String.format("%s room group card is available", selectedRoomGroupName), propertyGalleryPage.isRoomGroupCardDisplayed(selectedRoomGroupName), is(true));
            propertyGalleryPage.showAllRoomImageByRoomName(selectedRoomGroupName);
            propertyGalleryPage.clickRoomVendorArrangePhoto(selectedRoomGroupName, selectedVendorRoomName);
            Assertion.assertThat("Vendor image arrange photo modal displayed", propertyGalleryPage.isRoomVendorArrangePhotoModalDisplayed(), is(true));
            Assertion.assertThat("Room vendor source image is pinned", propertyGalleryPage.isRoomVendorImagePinned(sourceImageIndex), is(true));
            Assertion.assertThat("Room vendor source image order is successfully updated", propertyGalleryPage.isRoomVendorImageHasIdentifierByIndex(sourceImageIndex, roomImageStyleTarget), is(true));
            Assertion.assertThat("Room vendor target image order is successfully updated", propertyGalleryPage.isRoomVendorImageHasIdentifierByIndex(targetImageIndex, roomImageStyleSource), is(true));

            step("Revert room vendor image arrangement");
            if (!propertyGalleryPage.isRoomVendorArrangePhotoModalDisplayed()){
                propertyGalleryPage.showAllRoomImageByRoomName(selectedRoomGroupName);
                propertyGalleryPage.clickRoomVendorArrangePhoto(selectedRoomGroupName, selectedVendorRoomName);
            }
            Assertion.assertThat("Vendor image arrange photo modal displayed", propertyGalleryPage.isRoomVendorArrangePhotoModalDisplayed(), is(true));
            roomImageStyleSource = propertyGalleryPage.getRoomVendorImageIdentifierByIndex(sourceImageIndex);
            roomImageStyleTarget = propertyGalleryPage.getRoomVendorImageIdentifierByIndex(targetImageIndex);
            propertyGalleryPage.clickRoomVendorImagePinIcon(sourceImageIndex, false);
            propertyGalleryPage.clickRoomVendorImagePinIcon(targetImageIndex, false);
            Assertion.assertThat("Vendor source image is unpinned", propertyGalleryPage.isRoomVendorImageUnpinned(sourceImageIndex), is(true));
            Assertion.assertThat("Vendor target image is unpinned", propertyGalleryPage.isRoomVendorImageUnpinned(targetImageIndex), is(true));
            propertyGalleryPage.dragAndDropRoomVendorImage(sourceImageIndex, targetImageIndex);
            Assertion.assertThat("Room vendor image order changed after drag", propertyGalleryPage.isRoomVendorImageHasIdentifierByIndex(sourceImageIndex, roomImageStyleTarget), is(true));
            propertyGalleryPage.clickRoomVendorImagePinIcon(sourceImageIndex, true);
            propertyGalleryPage.clickSaveRoomImageArrangePhoto();
            Assertion.assertThat("Successful snackbar is displayed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
            propertyGalleryPage.refreshPage();
            Assertion.assertThat(String.format("%s room group card is available", selectedRoomGroupName), propertyGalleryPage.isRoomGroupCardDisplayed(selectedRoomGroupName), is(true));
            propertyGalleryPage.showAllRoomImageByRoomName(selectedRoomGroupName);
            propertyGalleryPage.clickRoomVendorArrangePhoto(selectedRoomGroupName, selectedVendorRoomName);
            Assertion.assertThat("Vendor image arrange photo modal displayed", propertyGalleryPage.isRoomVendorArrangePhotoModalDisplayed(), is(true));
            Assertion.assertThat("Room vendor source image is pinned", propertyGalleryPage.isRoomVendorImagePinned(sourceImageIndex), is(true));
            Assertion.assertThat("Room vendor source image order is successfully updated", propertyGalleryPage.isRoomVendorImageHasIdentifierByIndex(sourceImageIndex, roomImageStyleTarget), is(true));
            Assertion.assertThat("Room vendor target image order is successfully updated", propertyGalleryPage.isRoomVendorImageHasIdentifierByIndex(targetImageIndex, roomImageStyleSource), is(true));

            step("Select Added Image from Room group: %s and remove it".formatted(selectedRoomGroupName));
            propertyGalleryPage.refreshPage();
            propertyGalleryPage.showAllRoomImageByRoomName(selectedRoomGroupName);
            Assertion.assertThat("Room image is included to room group", propertyGalleryPage.isRoomImageInRoomGroupSectionByImageUrl(selectedRoomGroupName, url), is(true));
            propertyGalleryPage.checkRoomGroupImageByUrl(url);
            propertyGalleryPage.clickRemoveRoomImage();
            Assertion.assertThat("Remove room image modal is displayed", propertyGalleryPage.isIncludeExcludeRoomGroupImageModalDisplayed(), is(true));
            propertyGalleryPage.clickConfirmIncludeExcludeRoomGroupImage();
            Assertion.assertThat("Room image is successfully removed", propertyGalleryPage.isSuccessfulToastSnackbarDisplayed(), is(true));
            Assertion.assertThat("Room image is excluded to room group", propertyGalleryPage.isRoomImageInRoomGroupSectionByImageUrl(selectedRoomGroupName, url), is(false));
            addedImageStillInRoomGroup = false;
        } finally {
            // Cleanup even when the test failed between Add and Remove, so the data is left as it was found.
            if (addedImageStillInRoomGroup) {
                try {
                    propertyGalleryPage.removeRoomGroupImageByUrl(selectedRoomGroupName, url);
                } catch (Exception e) {
                    step("Cleanup could not remove the added room image: " + e.getMessage());
                }
            }
        }
    }

    @Author(names = {"Manan Kumar", "Riya Bajpai"})
    @Module(name = "HotelLibrary")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelLibraryUnmergePropertyWithMultipleVendorAndEditRoomStatus() {
        Map<String, String> param = getParam("testHotelLibraryUnmergePropertyWithMultipleVendorAndEditRoomStatus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        EditRoomPage<?> editRoomPage = new EditRoomPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        HotelMergePage<?> hotelMergePage = new HotelMergePage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String vendorName2 = param.get("vendorName2");
        String vendorExternalId2 = param.get("vendorExternalId2");
        String propertyName = param.get("propertyName");
        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");
        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        hotelLibraryPage.ensurePropertiesNotMerged(vendorName, vendorExternalId, vendorExternalId2);

        boolean unmerged = false;
        try {
            // merge hotel before unmerge
            step("Go to hotel merge page");
            sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.HOTEL_MERGE);
            sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.MANUAL_MERGE);
            Assertion.assertThat("Hotel merge page is displayed", hotelMergePage.isHotelMergePageDisplayed(), is(true));

            step("Search source hotel");
            hotelMergePage.selectCountry("SOURCE", country);
            hotelMergePage.selectRegion("SOURCE", region);
            hotelMergePage.selectCity("SOURCE", city);
            hotelMergePage.selectArea("SOURCE", area);
            hotelMergePage.inputKeyword("SOURCE", propertyName);
            hotelMergePage.clickSearch("SOURCE");
            Assertion.assertThat("Search result for source hotel is available", hotelMergePage.getSearchResultAvailable("SOURCE", propertyName), containsString(propertyName));
            hotelMergePage.clickSourceActionRadioButtonByVendorName(vendorName, vendorExternalId);

            step("Search target hotel");
            hotelMergePage.inputKeyword("TARGET", propertyName);
            hotelMergePage.selectCountry("TARGET", country);
            hotelMergePage.selectRegion("TARGET", region);
            hotelMergePage.selectCity("TARGET", city);
            hotelMergePage.selectArea("TARGET", area);
            hotelMergePage.clickSearch("TARGET");
            Assertion.assertThat("Search result for source hotel is available", hotelMergePage.getSearchResultAvailable("TARGET", propertyName), containsString(propertyName));
            hotelMergePage.clickTargetActionRadioButtonByVendorName(vendorName2, vendorExternalId2);

            step("Click Merge button");
            hotelMergePage.clickMergeButton();

            step("Verify and click confirmation modal");
            Assertion.assertThat("Merge confirmation modal is displayed", hotelMergePage.isMergeConfirmationModalAppeared(), is(true));
            hotelMergePage.confirmHotelMerge();
            Assertion.assertThat("Merge properties is successful", hotelMergePage.isSuccessfulSnackbarDisplayed(), is(true));


            // now unmerge same hotel with different vendor

            step("Go to Hotel Library Page");
            sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.HOTEL_CORE);
            sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.HOTEL_LIBRARY);
            Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
            Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

            step(String.format("Input property name: %s", propertyName));
            hotelLibraryPage.inputPropertyName(propertyName);

            step("Click search");
            hotelLibraryPage.clickSearch();

            step("Click Action and Unmerge");
            hotelLibraryPage.clickAction();
            hotelLibraryPage.clickUnmerge();
            Assertion.assertThat("Unmerge property modal is displayed", hotelLibraryPage.isUnmergePropertyModalAppeared(), is(true));

            step("Select Unmerge All");
            hotelLibraryPage.selectUnmergeAllPropertyRadioButton();
            hotelLibraryPage.clickContinueUnmerge();
            Assertion.assertThat("Unmerge reason modal is displayed", hotelLibraryPage.isUnmergePropertyModalAppeared(), is(true));

            step("Select unmerge reason");
            hotelLibraryPage.inputOtherUnmergeReason(CoreFunctions.generateRandomString(10));

            step("Submit unmerge");
            hotelLibraryPage.clickUnmergeSubmit();
            // set right after submit,before the snackbar check: cleanup re-checks the data before unmerging anyway
            unmerged = true;
            Assertion.assertThat("Unmerge property is successful", hotelLibraryPage.isSnackbarAppeared(), is(true));
        } finally {
            if (!unmerged) {
                hotelLibraryPage.cleanupMergedProperties(sidebarPage, vendorName, vendorExternalId, vendorExternalId2);
            }
        }

        step("Click Action and Edit Room");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickEditRoom();

        step("Turn Room Status OFF");
        editRoomPage.clickRoomStatusToggle();
        Assertion.assertThat("Successful snackbar is displayed after turning room status OFF", editRoomPage.isSuccessfulSnackbarDisplayed(), is(true));

        step("Turn Room Status ON");
        editRoomPage.clickRoomStatusToggle();
        Assertion.assertThat("Successful snackbar is displayed after turning room status ON", editRoomPage.isSuccessfulSnackbarDisplayed(), is(true));

    }
}
