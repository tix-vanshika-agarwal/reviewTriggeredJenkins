package com.tiket.web.dashboard.test.platform.eiffel;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.web.WebTest;
import com.tiket.web.dashboard.page.platform.eiffel.loyalty.MissionConfigFormPage;
import com.tiket.web.dashboard.page.platform.eiffel.loyalty.MissionConfigFormPage.RequiredMissionDetails;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static com.tiket.web.dashboard.page.platform.eiffel.loyalty.MissionConfigFormPage.BIN_MIN_LENGTH_HINT;
import static com.tiket.web.dashboard.page.platform.eiffel.loyalty.MissionConfigFormPage.BIN_NO_RESULTS_MESSAGE;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class MissionConfigBinFieldTest extends WebTest {

    @Author(names = { "Yanuar" })
    @Module(name = "MissionConfig")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testMissionConfigBinFieldLookupMultiSelectAndPersistence() {
        Session<?> session = WebTest.sessionThreadLocal.get();
        Map<String, String> testData = getParam("testMissionConfigBinFieldLookupMultiSelectAndPersistence");
        WebTest.paramsThreadLocal.set(testData);

        String email = testData.get("email");
        String password = testData.get("password");
        String binWithSpace = testData.get("binWithSpace");
        String tooShortBin = testData.get("tooShortBin");
        String bin7Digits = testData.get("bin7Digits");
        String bin9Digits = testData.get("bin9Digits");
        String nonExistentBin = testData.get("nonExistentBin");
        String validBin6Digits = testData.get("validBin6Digits");
        String validBin8Digits = testData.get("validBin8Digits");
        String paymentMethod = testData.get("paymentMethod");
        int startDateOffsetDays = Integer.parseInt(testData.get("startDateOffsetDays"));
        int endDateOffsetDays = Integer.parseInt(testData.get("endDateOffsetDays"));

        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        RequiredMissionDetails requiredDetails = new RequiredMissionDetails(
                testData.get("missionName") + uniqueSuffix, testData.get("slug") + uniqueSuffix,
                testData.get("labelTextEn"), testData.get("labelTextId"),
                testData.get("totalOrdersOrStampsRequired"), testData.get("numberOfFreeStamps"),
                testData.get("rewardTitleEn"), testData.get("rewardTitleId"), testData.get("pointType"),
                testData.get("amount"), testData.get("rewardQuota"));

        MissionConfigFormPage<?> missionConfigFormPage = new MissionConfigFormPage<>(session);

        step("Given user opens Create new mission on the Mission Config page");
        missionConfigFormPage.goToCreateNewMissionForm(email, password);

        step("Then the BIN field is shown below Payment Method, labelled optional");
        Assertion.assertThat("BIN is labelled as an optional field", missionConfigFormPage.isBinFieldLabelledOptional(), is(true));

        step("When user enters a BIN with an internal space");
        missionConfigFormPage.typeBin(binWithSpace);

        step("Then the space is never accepted into the field");
        Assertion.assertThat("The BIN field drops the space keystroke instead of accepting it",
                missionConfigFormPage.getBinInputValue(), not(containsString(" ")));
        missionConfigFormPage.closeBinMenu();

        step("When user enters BINs shorter than 6 digits, with 7 digits, and with more than 8 digits");
        missionConfigFormPage.typeBin(tooShortBin);
        String tooShortBinMessage = missionConfigFormPage.getBinMenuMessage();
        missionConfigFormPage.closeBinMenu();

        missionConfigFormPage.typeBin(bin7Digits);
        String bin7DigitsMessage = missionConfigFormPage.getBinMenuMessage();
        missionConfigFormPage.closeBinMenu();

        missionConfigFormPage.typeBin(bin9Digits);
        String bin9DigitsMessage = missionConfigFormPage.getBinMenuMessage();
        missionConfigFormPage.closeBinMenu();

        step("Then none of them offer a selectable BIN option");
        Assertion.assertThat("A BIN shorter than the lookup's minimum length is told to keep typing",
                tooShortBinMessage, containsString(BIN_MIN_LENGTH_HINT));
        Assertion.assertThat("A 7-digit BIN (neither 6 nor 8) matches no bank BIN",
                bin7DigitsMessage, containsString(BIN_NO_RESULTS_MESSAGE));
        Assertion.assertThat("A BIN longer than 8 digits matches no bank BIN",
                bin9DigitsMessage, containsString(BIN_NO_RESULTS_MESSAGE));

        step("When user enters a BIN that does not exist in GET bankBin");
        missionConfigFormPage.typeBin(nonExistentBin);
        String nonExistentBinMessage = missionConfigFormPage.getBinMenuMessage();
        missionConfigFormPage.closeBinMenu();

        step("Then it is reported as not matched");
        Assertion.assertThat("A non-existing BIN matches no bank BIN", nonExistentBinMessage, containsString(BIN_NO_RESULTS_MESSAGE));

        step("When user enters a valid 6-digit BIN and a valid 8-digit BIN and selects both");
        missionConfigFormPage.typeBin(validBin6Digits);
        missionConfigFormPage.selectBinOption(validBin6Digits);
        missionConfigFormPage.typeBin(validBin8Digits);
        missionConfigFormPage.selectBinOption(validBin8Digits);

        step("Then both BINs are selected on the mission");
        List<String> selectedBinsBeforeSave = missionConfigFormPage.getSelectedBins();
        Assertion.assertThat("Both valid BINs are selected", missionConfigFormPage.getSelectedBinCount(), is(2));
        Assertion.assertThat("The 6-digit BIN option is selected", selectedBinsBeforeSave, hasItem(containsString(validBin6Digits)));
        Assertion.assertThat("The 8-digit BIN option is selected", selectedBinsBeforeSave, hasItem(containsString(validBin8Digits)));

        step("When user selects a Payment Method, fills the rest of the required mission data, and saves");
        missionConfigFormPage.selectPaymentMethod(paymentMethod);
        missionConfigFormPage.fillRequiredMissionDetails(requiredDetails);
        missionConfigFormPage.selectStartDate(startDateOffsetDays);
        missionConfigFormPage.selectEndDate(endDateOffsetDays);
        missionConfigFormPage.clickContinue();

        step("Then step 1 is saved, and going into step 2 and back keeps the selected BINs");
        missionConfigFormPage.clickBack();
        missionConfigFormPage.waitForSelectedBinCount(selectedBinsBeforeSave.size());
        Assertion.assertThat("The saved BIN selections persist after returning from step 2",
                missionConfigFormPage.getSelectedBins(), containsInAnyOrder(selectedBinsBeforeSave.toArray(new String[0])));
    }
}
