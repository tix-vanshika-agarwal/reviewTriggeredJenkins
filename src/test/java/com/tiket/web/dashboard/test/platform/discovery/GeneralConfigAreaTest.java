package com.tiket.web.dashboard.test.platform.discovery;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.discovery.GeneralConfigAreaPage;
import org.testng.annotations.Test;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;

public class GeneralConfigAreaTest extends BaseTest {
    @Author(names = {"Fandy"})
    @Module(name = "GeneralConfig")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testAddEditArea() throws ExecutionException, InterruptedException{
        Map<String, String> map = getParam("testAddEditArea");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        GeneralConfigAreaPage<?> generalConfigAreaPage = new GeneralConfigAreaPage<>(session);

        final String username = map.get("username");
        final String password = map.get("password");
        final String dialingCode = map.get("dialingCode");

        step("Given admin go to Area Page");
        generalConfigAreaPage.goToAreaPage(username, password);
        Assertion.assertThat("Verify Area Page is loaded", generalConfigAreaPage.isAreaPageOpened(username), is(true));

        step("When admin add new Area");
        generalConfigAreaPage.addNewArea(dialingCode);
        Assertion.assertThat("Verify snackbar success appear", generalConfigAreaPage.isSnackbarSuccessAppear(), is(true));

        step("Admin can search recently added Area");
        generalConfigAreaPage.searchLatestCreated();

        step("Admin can edit & successfully edit Area");
        generalConfigAreaPage.editCreatedArea();

        step("And the new added area should be displayed on area list page");
        Map<String, String> dataToValidate = generalConfigAreaPage.getDataToValidateFromAPI();
        boolean resultValue = generalConfigAreaPage.isGeneralConfigAreaApiDataValid();

        // Assertion Value
        String valuesApiVerification = dataToValidate.get("response");
        Assertion.assertThat("Verify response from API is not null", valuesApiVerification, notNullValue());
        Assertion.assertThat("Verify all data values are according to the inputted data", resultValue, is(true));

        // Assertion is present
        String isIdPresentInApi = dataToValidate.get("isIdPresent");
        String isOrderPresentInApi = dataToValidate.get("isOrderPresent");
        String isTypePresentInApi = dataToValidate.get("isTypePresent");
        String isCodePresentInApi = dataToValidate.get("isCodePresent");
        String isAbbreviationPresentInApi = dataToValidate.get("isAbbreviationPresent");
        String isDialingCodePresentInApi = dataToValidate.get("isDialingCodePresent");
        String isTimeSeparatorPresentInApi = dataToValidate.get("isTimeSeparatorPresent");
        String isDecimalSeparatorPresentInApi = dataToValidate.get("isDecimalSeparatorPresent");
        String isThousandsSeparatorPresentInApi = dataToValidate.get("isThousandsSeparatorPresent");
        String isDateFormatPresentInApi = dataToValidate.get("isDateFormatPresent");
        String isActivePresentInApi = dataToValidate.get("isActivePresent");

        Assertion.assertThat("Verify id is present from API", Boolean.valueOf(isIdPresentInApi), is(true));
        Assertion.assertThat("Verify order is present from API", Boolean.valueOf(isOrderPresentInApi), is(true));
        Assertion.assertThat("Verify type is present from API", Boolean.valueOf(isTypePresentInApi), is(true));
        Assertion.assertThat("Verify code is present from API", Boolean.valueOf(isCodePresentInApi), is(true));
        Assertion.assertThat("Verify abbreviations is present from API", Boolean.valueOf(isAbbreviationPresentInApi), is(true));
        Assertion.assertThat("Verify dialing code is present from API", Boolean.valueOf(isDialingCodePresentInApi), is(true));
        Assertion.assertThat("Verify time separator is present from API", Boolean.valueOf(isTimeSeparatorPresentInApi), is(true));
        Assertion.assertThat("Verify decimal separator is present from API", Boolean.valueOf(isDecimalSeparatorPresentInApi), is(true));
        Assertion.assertThat("Verify thousands separator is present from API", Boolean.valueOf(isThousandsSeparatorPresentInApi), is(true));
        Assertion.assertThat("Verify date format is present from API", Boolean.valueOf(isDateFormatPresentInApi), is(true));
        Assertion.assertThat("Verify active is present from API", Boolean.valueOf(isActivePresentInApi), is(true));
    }
}
