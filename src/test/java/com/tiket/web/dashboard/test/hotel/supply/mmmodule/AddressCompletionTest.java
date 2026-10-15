package com.tiket.web.dashboard.test.hotel.supply.mmmodule;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.AddressCompletionPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.HotelLibraryPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.LoginPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.SidebarPage;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class AddressCompletionTest extends BaseTest {

    @Author(names = {"Raihan", "Bikash kumar jha"})
    @Module(name = "AddressCompletion")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testUploadAddressCompletion() throws Exception {
        Map<String, String> param = getParam("testUploadAddressCompletion");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        AddressCompletionPage<?> addressCompletionPage = new AddressCompletionPage<>(session);

        String fileName = param.get("fileName");
        String[] hotelIds = param.get("hotelIds").split("\\|");
        String expectedStatus = param.get("expectedStatus");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module via UI login");
        loginPage.openMMModuleViaUI(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to address completion page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.ADDRESS_COMPLETION);
        Assertion.assertThat("Address completion page is displayed", addressCompletionPage.isAddressCompletionPageDisplayed(), is(true));

        step("Generate csv file");
        File file = addressCompletionPage.generateAddressCompletionCSVFile(fileName, hotelIds);

        step("Upload address completion file");
        addressCompletionPage.clickUpload();
        Assertion.assertThat("Upload file modal is appeared", addressCompletionPage.isUploadFileModalAppeared(), is(true));
        addressCompletionPage.uploadFile(file);
        Assertion.assertThat("File has been selected", addressCompletionPage.isFileSelected(fileName), is(true));

        step("Submit address completion File");
        addressCompletionPage.submitFile();
        Assertion.assertThat("Successful snackbar is appeared", addressCompletionPage.isSuccessfulSnackbarAppeared(), is(true));
        Assertion.assertThat("Upload file modal is disappeared", addressCompletionPage.isUploadFileModalAppeared(), is(false));

        step("Verify latest activity");
        Assertion.assertThat("Last uploaded date is updated", addressCompletionPage.isLastUploadedDateUpdated(), is(true));
        Assertion.assertThat("Address completion upload status is correct", addressCompletionPage.isStatusDisplayedCorrectly(expectedStatus), is(true));

        step("Delete file");
        addressCompletionPage.deleteFile(file);
        Assertion.assertThat("File is already removed", addressCompletionPage.isFileExist(file), is(false));
    }
}
