package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.Matchers.*;

public class PropertyPage<T extends WebDriver> extends BasePage<T> {

    public PropertyPage(Session<T> session) {
        super(session);
    }

    private static final Locator ROOM_DATA_PAGE = new Locator("ROOM_DATA_PAGE", new By.ByXPath("//span[(text()='Data Kamar' or text()='Room Data') and contains(@class, 'text-xl')]"));
    private static final Locator SEARCH_ROOM_INPUT = new Locator("SEARCH_ROOM_INPUT", new By.ById("what-are-you-looking-for"));
    private static final Locator SEARCH_ROOM_BUTTON = new Locator("SEARCH_ROOM_BUTTON", new By.ByXPath("//form//button[@type='submit' and text()='Go']"));
    private static final Locator ADD_NEW_ROOM_BUTTON = new Locator("ADD_NEW_ROOM_BUTTON", new By.ByXPath("//a[contains(@href, '/room-data/create')]/button"));
    private static final Locator ADD_NEW_ROOM_PAGE_HEADER = new Locator("ADD_NEW_ROOM_PAGE_HEADER", new By.ByXPath("//span[(text()='Add New Room' or text()='Tambah Kamar Baru') and contains(@class, 'text-xl')]"));
    private static final Locator EDIT_ROOM_PAGE_HEADER = new Locator("EDIT_ROOM_PAGE_HEADER", new By.ByXPath("//span[(text()='Edit Room' or text()='Ubah Kamar') and contains(@class, 'text-xl')]"));
    private static final Locator CREATE_ROOM_BUTTON = new Locator("CREATE_ROOM_BUTTON", new By.ByXPath("//button[text()='Create Room' or text()='Tambah Kamar']"));
    private static final String EDIT_ROOM_BTN_TEMPLATE = "//table/tbody/tr[td[4][contains(normalize-space(.), '%s')]]//button[contains(normalize-space(.), 'Edit') or contains(normalize-space(.), 'Ubah')]";
    private static final String DELETE_ROOM_BTN_TEMPLATE = "//table/tbody/tr[td[4][contains(normalize-space(.), '%s')]]//button[contains(normalize-space(.), 'Delete') or contains(normalize-space(.), 'Hapus')]";
    private static final Locator ROOM_NAME_INPUT = new Locator("ROOM_NAME_INPUT", new By.ByXPath("//input[@name='name']"));
    private static final Locator ROOM_TYPE_DROPDOWN_INPUT = new Locator("ROOM_TYPE_DROPDOWN_INPUT", new By.ByXPath("//p[text()='Room Type' or contains(text(), 'Tipe Kamar')]/parent::div//input"));
    private static final Locator EDIT_ROOM_SUBMIT_BTN = new Locator("EDIT_ROOM_SUBMIT_BTN", By.xpath("//button[contains(normalize-space(.), 'Edit Room') or contains(normalize-space(.), 'Ubah Kamar')]"));
    private static final Locator ROOM_FLOOR_NUMBER_INPUT = new Locator("ROOM_FLOOR_NUMBER_INPUT", new By.ByXPath("//input[@name='roomFloorNumber']"));
    private static final Locator ROOM_NUMBER_INPUT = new Locator("ROOM_NUMBER_INPUT", new By.ByXPath("//input[@name='roomNumber']"));
    private static final Locator ROOM_DESC_EN_INPUT = new Locator("ROOM_DESC_EN_INPUT", new By.ByXPath("//p[text()='English (English)']/ancestor::div[contains(@class, 'rsw-editor')]//div[@contenteditable='true']"));
    private static final Locator ROOM_DESC_ID_INPUT = new Locator("ROOM_DESC_ID_INPUT", new By.ByXPath("//p[contains(text(), 'Bahasa Indonesia')]/ancestor::div[contains(@class, 'rsw-editor')]//div[@contenteditable='true']"));
    private static final Locator NUMBER_OF_ROOMS_INPUT = new Locator("NUMBER_OF_ROOMS_INPUT", new By.ByXPath("//div[@id='numberOfRooms']//input[@type='number']"));
    private static final Locator NUMBER_OF_ROOMS_PLUS_BTN = new Locator("NUMBER_OF_ROOMS_PLUS_BTN", new By.ByXPath("//div[@id='numberOfRooms']//div[contains(@class, 'QuantityEditor_container')]/button[2]"));
    private static final Locator NUMBER_OF_ROOMS_MINUS_BTN = new Locator("NUMBER_OF_ROOMS_MINUS_BTN", new By.ByXPath("//div[@id='numberOfRooms']//div[contains(@class, 'QuantityEditor_container')]/button[1]"));
    private static final Locator DEFAULT_ALLOTMENT_CHECKBOX = new Locator("DEFAULT_ALLOTMENT_CHECKBOX", new By.ByXPath("//input[contains(@aria-label, 'Default Allotment.')]"));
    private static final Locator DEFAULT_ALLOTMENT_TOGGLE_DIV = new Locator("DEFAULT_ALLOTMENT_TOGGLE_DIV", new By.ByXPath("//label[input[contains(@aria-label, 'Default Allotment.')]]/div[contains(@class, 'Toggle_toggle')]"));
    private static final Locator DEFAULT_ALLOTMENT_INPUT = new Locator("DEFAULT_ALLOTMENT_INPUT", new By.ByXPath("//div[@id='defaultAllotment']//input[@type='number']"));
    private static final Locator DEFAULT_ALLOTMENT_PLUS_BTN = new Locator("DEFAULT_ALLOTMENT_PLUS_BTN", new By.ByXPath("//div[@id='defaultAllotment']//div[contains(@class, 'QuantityEditor_container')]/button[2]"));
    private static final Locator DEFAULT_ALLOTMENT_MINUS_BTN = new Locator("DEFAULT_ALLOTMENT_MINUS_BTN", new By.ByXPath("//div[@id='defaultAllotment']//div[contains(@class, 'QuantityEditor_container')]/button[1]"));
    private static final Locator ACTUAL_ALLOTMENT_EXCEED_ERR = new Locator("ACTUAL_ALLOTMENT_EXCEED_ERR", new By.ByXPath("//div[@id='defaultAllotment']//div[contains(@class, 'Text_variant_alert')]"));
    private static final Locator ACTUAL_ROOM_NAME_ERR = new Locator("ACTUAL_ROOM_NAME_ERR", new By.ByXPath("//input[@name='name']/ancestor::div[contains(@class,'TextInput_custom_text_input')]//div[contains(@class,'styles_helper_text_container')]"));
    private static final Locator ACTUAL_ROOM_TYPE_ERR = new Locator("ACTUAL_ROOM_TYPE_ERR", new By.ByXPath("//div[contains(@class,'DropdownV2_searchable_input')]//div[contains(@class,'styles_helper_text_container')]"));
    private static final Locator ACTUAL_ROOM_DESC_ERR = new Locator("ACTUAL_ROOM_DESC_ERR", new By.ByXPath("//p[contains(text(), 'description') or contains(@class, 'Text_variant_alert')]"));
    private static final Locator ACTUAL_NUM_ROOMS_ERR = new Locator("ACTUAL_NUM_ROOMS_ERR", new By.ByXPath("//div[@id='numberOfRooms']//div[contains(@class, 'Text_variant_alert')]"));
    private static final Locator ACTUAL_MIN_PRICE_ERR = new Locator("ACTUAL_MIN_PRICE_ERR", new By.ByXPath("//input[@name='amount']/ancestor::div[contains(@class,'styles_container')]//div[contains(@class,'styles_helper_text_container')]"));
    private static final Locator ACTUAL_SMOKING_POLICY_ERR = new Locator("ACTUAL_SMOKING_POLICY_ERR", new By.ByXPath("//input[@name='smokingPolicy']/ancestor::div[contains(@class,'items-start')]//p[contains(@class,'RadioGroup_error_msg')]"));
    private static final Locator ACTUAL_AVG_ROOM_SIZE_ERR = new Locator("ACTUAL_AVG_ROOM_SIZE_ERR", new By.ByXPath("//p[contains(text(), 'Average room size') or contains(text(), 'Ukuran kamar')]/parent::div//div[contains(@class, 'Text_variant_alert')]"));
    private static final Locator ACTUAL_MULTIPLE_BED_ERR = new Locator("ACTUAL_MULTIPLE_BED_ERR", new By.ByXPath("//p[contains(text(), 'multiple bedrooms') or contains(text(), 'beberapa kamar tidur')]/parent::div/p[contains(@class, 'RadioGroup_error_msg')]"));
    private static final Locator ACTUAL_BED_TYPE_ERR = new Locator("ACTUAL_BED_TYPE_ERR", new By.ByXPath("//input[@id='bed-type' or @id='pilih-tipe-kasur']/ancestor::div[contains(@class, 'styles_container')]//div[contains(@class, 'styles_helper_text_container')]"));
    private static final Locator ACTUAL_BED_QTY_ERR = new Locator("ACTUAL_BED_QTY_ERR", new By.ByXPath("//input[@id='number-of-beds' or @id='jumlah-kasur']/ancestor::div[contains(@class, 'styles_container')]//div[contains(@class, 'styles_helper_text_container')]"));
    private static final Locator MINIMUM_PRICE_INPUT = new Locator("MINIMUM_PRICE_INPUT", new By.ByXPath("//input[@name='amount']"));
    private static final Locator ROOM_VIEW_DROPDOWN_INPUT = new Locator("ROOM_VIEW_DROPDOWN_INPUT", new By.ByXPath("//p[contains(text(), 'View') or contains(text(), 'Pemandangan')]/parent::div//input"));
    private static final Locator SMOKING_BOTH_RADIO_LABEL = new Locator("SMOKING_BOTH_RADIO_LABEL", new By.ByXPath("//input[@name='smokingPolicy' and contains(@value, 'E')]/ancestor::label"));
    private static final Locator SMOKING_BOTH_INPUT = new Locator("SMOKING_BOTH_INPUT", new By.ByXPath("//input[@name='smokingPolicy' and contains(@value, 'E')]"));
    private static final Locator AVG_ROOM_SIZE_INPUT = new Locator("AVG_ROOM_SIZE_INPUT", new By.ByXPath("//p[contains(text(), 'Average room size') or contains(text(), 'Ukuran kamar')]/parent::div//input[@type='number']"));
    private static final Locator AVG_ROOM_SIZE_PLUS_BTN = new Locator("AVG_ROOM_SIZE_PLUS_BTN", new By.ByXPath("//p[contains(text(), 'Average room size') or contains(text(), 'Ukuran kamar')]/parent::div//div[contains(@class, 'QuantityEditor_container')]/button[2]"));
    private static final Locator AVG_ROOM_SIZE_MINUS_BTN = new Locator("AVG_ROOM_SIZE_MINUS_BTN", new By.ByXPath("//p[contains(text(), 'Average room size') or contains(text(), 'Ukuran kamar')]/parent::div//div[contains(@class, 'QuantityEditor_container')]/button[1]"));
    private static final Locator BATHROOM_COUNT_INPUT = new Locator("BATHROOM_COUNT_INPUT", new By.ByXPath("//p[contains(text(), 'Number of Bathroom') or contains(text(), 'Jumlah Kamar Mandi')]/parent::div//input[@type='number']"));
    private static final Locator BATHROOM_COUNT_PLUS_BTN = new Locator("BATHROOM_COUNT_PLUS_BTN", new By.ByXPath("//p[contains(text(), 'Number of Bathroom') or contains(text(), 'Jumlah Kamar Mandi')]/parent::div//div[contains(@class, 'QuantityEditor_container')]/button[2]"));
    private static final Locator BATHROOM_TYPE_PRIVATE_LABEL = new Locator("BATHROOM_TYPE_PRIVATE_LABEL", new By.ByXPath("//input[@name='bathroomType' and contains(@value, 'PRIVATE')]/ancestor::label"));
    private static final Locator BATHROOM_TYPE_PRIVATE_INPUT = new Locator("BATHROOM_TYPE_PRIVATE_INPUT", new By.ByXPath("//input[@name='bathroomType' and contains(@value, 'PRIVATE')]"));
    private static final Locator MAX_OCCUPANCY_INPUT = new Locator("MAX_OCCUPANCY",new By.ByXPath("//input[@id='maximum-occupancy' or @id='maksimum-okupansi']"));
    private static final Locator HAS_MAX_ADULTS_YES = new Locator("HAS_ADULTS_YES",new By.ByXPath("//input[@name='hasMaxAdults' and contains(@value, 'Y')]/ancestor::label"));
    private static final Locator HAS_MIN_GUEST_YES = new Locator("HAS_MIN_GUEST_YES", new By.ByXPath("//input[@name='hasMinGuest' and contains(@value, 'Y')]/ancestor::label"));
    private static final Locator HAS_MULTIPLE_BEDROOMS_YES = new Locator("HAS_MULTIPLE_BEDROOMS_YES", new By.ByXPath("//input[@name='hasMultipleBedrooms' and contains(@value, 'Y')]/ancestor::label"));
    private static final Locator MAX_ADULTS_INPUT = new Locator("MAX_ADULTS", new By.ByXPath("//input[contains(@placeholder, 'Adults') or contains(@placeholder, 'Tamu Dewasa')]"));
    private static final Locator MAX_CHILDREN_INPUT = new Locator("MAX_CHILDREN", new By.ByXPath("//input[contains(@placeholder, 'Children') or contains(@placeholder, 'Anak')]"));
    private static final Locator MIN_OCCUPANCY_INPUT = new Locator("MIN_OCCUPANCY", new By.ByXPath("//input[contains(@placeholder, 'Minimum Occupancy') or contains(@placeholder, 'Minimum Okupansi')]"));
    private static final Locator BEDROOM_MINUS_BUTTON = new Locator("BEDROOM_MINUS", new By.ByXPath("//div[p[normalize-space()='Number of Bedroom'] or p[normalize-space()='Jumlah Kamar Tidur']]//button[1]"));
    private static final Locator BEDROOM_PLUS_BUTTON = new Locator("BEDROOM_PLUS", new By.ByXPath("//div[p[normalize-space()='Number of Bedroom'] or p[normalize-space()='Jumlah Kamar Tidur']]//button[2]"));
    private static final Locator BEDROOM_QTY_INPUT = new Locator("BEDROOM_QTY_INPUT", new By.ByXPath("//div[p[normalize-space()='Number of Bedroom'] or p[normalize-space()='Jumlah Kamar Tidur']]//input[@type='number']"));
    private static final Locator EXTRA_BED_CHECKBOX = new Locator("EXTRA_BED_CHECKBOX", new By.ByXPath("//span[contains(text(), 'Tersedia kasur tambahan') or contains(text(), 'Extra bed available')]/ancestor::label"));
    private static final Locator EXTRA_BED_INPUT = new Locator("EXTRA_BED_INPUT", new By.ByXPath("//span[contains(text(), 'Tersedia kasur tambahan') or contains(text(), 'Extra bed available')]/ancestor::label/following::div[contains(@class, 'DropdownV2_dropdown_container')][1]//input"));
    private static final Locator ALL_BED_TYPE_INPUTS = new Locator("ALL_BED_TYPE_INPUTS", new By.ByXPath("//input[@id='bed-type' or @id='pilih-tipe-kasur']"));
    private static final String BED_TYPE_INPUT_TEMPLATE = "(//input[@id='bed-type' or @id='pilih-tipe-kasur'])[%d]";
    private static final String BED_OPTION_TEMPLATE = "//li[@data-label='%1$s' or text()='%1$s']";
    private static final String BED_COUNT_INPUT_TEMPLATE = "(//input[@id='number-of-beds' or @id='jumlah-kasur'])[%d]";
    private static final Locator UPLOAD_PHOTO_AREA = new Locator("UPLOAD_PHOTO_AREA", new By.ByXPath("//div[contains(@class, 'border-dashed') and .//span[text()='Add photo' or text()='Tambah foto']]"));
    private static final Locator GALLERY_PHOTO_LABELS = new Locator("GALLERY_PHOTO_LABELS", new By.ByXPath("//div[contains(@class, 'overflow-scroll')]//label[contains(@class, 'Check_check')]"));
    private static final Locator CONFIRM_ADD_PHOTOS_BTN = new Locator("CONFIRM_ADD_PHOTOS_BTN", new By.ByXPath("//button[text()='Add' or text()='Tambah']"));
    private static final Locator SET_MAIN_PHOTO_BTN = new Locator("SET_MAIN_PHOTO_BTN", new By.ByXPath("(//button[span[text()='Set as main photo' or text()='Jadikan foto utama']])[1]"));
    private static final Locator MAIN_PHOTO_LABEL = new Locator("MAIN_PHOTO_LABEL", new By.ByXPath("//div[text()='Main photo' or text()='Foto utama']"));
    private static final Locator FACILITY_NAV_BUTTONS = new Locator("FACILITY_NAV_BUTTONS", new By.ByXPath("//nav[contains(@class, 'overflow-y-auto')]//button"));
    private static final Locator SNACKBAR_TEXT_CONTAINER = new Locator("SNACKBAR_TEXT_CONTAINER", By.xpath("//div[contains(@class, 'snackbars_positioner')]//div[contains(@class, 'text_content')]"));
    private static final String SEARCH_ROW_TEMPLATE = "//table/tbody/tr[td[normalize-space(text())='%s']]";
    private static final String TOGGLE_INPUT_TEMPLATE = "//table/tbody/tr[td[4][contains(normalize-space(.), '%s')]]//div[contains(@class, 'Toggle_container')]//input";
    private static final String TOGGLE_CLICKABLE_TRACK_TEMPLATE = "//table/tbody/tr[td[4][contains(normalize-space(.), '%s')]]//div[contains(@class, 'Toggle_toggle')]";
    private static final Locator MODAL_ACTIVATE_ROOM_BTN = new Locator("MODAL_ACTIVATE_ROOM_BTN", new By.ByXPath("//div[contains(@class, 'Dialog_body')]//button[contains(normalize-space(.), 'Activate room') or contains(normalize-space(.), 'Aktifkan kamar')]"));
    private static final Locator MODAL_COMPLETE_DATA_BTN = new Locator("MODAL_COMPLETE_DATA_BTN", new By.ByXPath("//div[contains(@class, 'Dialog_body')]//button[contains(normalize-space(.), 'Complete data') or contains(normalize-space(.), 'Lengkapi data')]"));
    private static final Locator MODAL_DELETE_YES_BTN = new Locator("MODAL_DELETE_YES_BTN", new By.ByXPath("//div[contains(@class, 'Dialog_body')]//button[text()='Yes' or text()='Ya']"));
    private static final Locator MODAL_DELETE_NO_BTN = new Locator("MODAL_DELETE_NO_BTN", new By.ByXPath("//div[contains(@class, 'Dialog_body')]//button[text()='No' or text()='Tidak']"));
    private static final Locator NO_DATA_FOUND_MESSAGE = new Locator("NO_DATA_FOUND_MESSAGE", By.xpath("//h2[contains(normalize-space(.), 'No data') or contains(normalize-space(.), 'Data tidak ditemukan')]"));

    private static Locator getRoomNameLabel(String text) {
        return new Locator("ROOM_NAME_LABEL", new By.ByXPath(String.format("//td[text()='%s']", text)));
    }

    private static Locator getRoomTypeOption(String typeName) {
        return new Locator("ROOM_TYPE_OPTION", new By.ByXPath(String.format("//li[text()='%s']", typeName)));
    }

    private static Locator getExtraBedOptions(String quantity) {
        return new Locator("EXTRA_BED_OPTIONS", new By.ByXPath(String.format("(//li[@data-label='%1$s' or text()='%1$s'])[last()]", quantity)));
    }

    public boolean isRoomDataPageLoaded() {
        driverTools.waitForElementToAppear(ROOM_DATA_PAGE);
        return driverTools.isElementDisplayed(ROOM_DATA_PAGE);
    }

    public boolean isEditRoomPageLoaded() {
        driverTools.waitForElementToAppear(EDIT_ROOM_PAGE_HEADER);
        return driverTools.isElementDisplayed(EDIT_ROOM_PAGE_HEADER);
    }

    public void searchRoomName(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(SEARCH_ROOM_INPUT).orElseThrow();
        input.click();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, text);
    }

    public void clickSearch() {
        WebElement button = driverTools.waitForElementToBeClickable(SEARCH_ROOM_BUTTON).orElseThrow();
        driverTools.click(button);
        driverTools.dummyWait(5);
    }

    public boolean isRoomAppeared(String roomName) {
        return driverTools.waitForElementToAppear(getRoomNameLabel(roomName)).isPresent();
    }

    public void refreshPage() {
        driverTools.refreshWebpage();
    }

    public void clickAddNewRoomButton() {
        WebElement button = driverTools.waitForElementToBeClickable(ADD_NEW_ROOM_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isAddNewRoomPageLoaded() {
        driverTools.waitForElementToAppear(ADD_NEW_ROOM_PAGE_HEADER);
        return driverTools.isElementDisplayed(ADD_NEW_ROOM_PAGE_HEADER);
    }

    public void clickCreateRoomButton() {
        driverTools.scrollElementToCenter(CREATE_ROOM_BUTTON);
        Assertion.assertThat("Create Room Button Displayed",driverTools.scrollAndCheckElement(CREATE_ROOM_BUTTON), is(true));
        driverTools.waitForElementToBeClickable(CREATE_ROOM_BUTTON).orElseThrow();
        driverTools.click(CREATE_ROOM_BUTTON);
    }

    public String getEnteredRoomName() {
        WebElement input = driverTools.waitForElementToAppear(ROOM_NAME_INPUT).orElseThrow();
        return driverTools.getValue(input); // Utilizes your driverTools value-reader safely
    }

    public void enterRoomName(String roomName) {
        driverTools.waitForElementToAppear(ROOM_NAME_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(ROOM_NAME_INPUT);
        WebElement input = driverTools.waitForElementToBeClickable(ROOM_NAME_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, roomName);
    }

    public void selectRoomType(String typeName) {
        WebElement dropdownInput = driverTools.waitForElementToBeClickable(ROOM_TYPE_DROPDOWN_INPUT).orElseThrow();
        driverTools.click(dropdownInput);

        Locator targetOption = getRoomTypeOption(typeName);
        WebElement optionElement = driverTools.waitForElementToBeClickable(targetOption).orElseThrow();
        driverTools.click(optionElement);
    }

    public String getSelectedRoomType() {
        WebElement input = driverTools.waitForElementToAppear(ROOM_TYPE_DROPDOWN_INPUT).orElseThrow();
        return driverTools.getValue(input);
    }

    public String getEnteredRoomFloorNumber() {
        WebElement input = driverTools.waitForElementToAppear(ROOM_FLOOR_NUMBER_INPUT).orElseThrow();
        return driverTools.getValue(input);
    }

    public String getEnteredRoomNumber() {
        WebElement input = driverTools.waitForElementToAppear(ROOM_NUMBER_INPUT).orElseThrow();
        return driverTools.getValue(input);
    }

    public void enterRoomFloorNumber(String floor) {
        driverTools.scrollElementToCenter(ROOM_FLOOR_NUMBER_INPUT);
        WebElement input = driverTools.waitForElementToBeClickable(ROOM_FLOOR_NUMBER_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, floor);
    }

    public void enterRoomNumber(String number) {
        driverTools.scrollElementToCenter(ROOM_NUMBER_INPUT);
        WebElement input = driverTools.waitForElementToBeClickable(ROOM_NUMBER_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, number);
    }

    public void enterRoomDescriptionEn(String desc) {
        driverTools.scrollElementToCenter(ROOM_DESC_EN_INPUT);
        WebElement input = driverTools.waitForElementToBeClickable(ROOM_DESC_EN_INPUT).orElseThrow();
        input.click();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, desc);
    }

    public String getEnteredRoomDescriptionEn() {
        WebElement input = driverTools.waitForElementToAppear(ROOM_DESC_EN_INPUT).orElseThrow();
        return input.getText().trim(); // Uses direct node text evaluation since value attribute is absent on divs
    }

    public void enterRoomDescriptionId(String desc) {
        driverTools.scrollElementToCenter(ROOM_DESC_ID_INPUT);
        WebElement input = driverTools.waitForElementToBeClickable(ROOM_DESC_ID_INPUT).orElseThrow();
        input.click();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, desc);
    }

    public String getEnteredRoomDescriptionId() {
        WebElement input = driverTools.waitForElementToAppear(ROOM_DESC_ID_INPUT).orElseThrow();
        return input.getText().trim();
    }

    public String getActualFieldErrorText(Locator targetErrorLocator) {
        driverTools.scrollUntilElementFound(targetErrorLocator, 400, 2, 5);
        return driverTools.getText(targetErrorLocator).orElse("").trim();
    }

    public void verifyErrorWithoutFillingTheFields(Map<String, String> param) {
        Map<Locator, String[]> errorValidationMap = new LinkedHashMap<>();
        errorValidationMap.put(ACTUAL_ROOM_NAME_ERR, new String[]{param.get("errRoomNameEn"), param.get("errRoomNameId")});
        errorValidationMap.put(ACTUAL_ROOM_TYPE_ERR, new String[]{param.get("errRoomTypeEn"), param.get("errRoomTypeId")});
        errorValidationMap.put(ACTUAL_ROOM_DESC_ERR, new String[]{param.get("errRoomDescEn"), param.get("errRoomDescId")});
        errorValidationMap.put(ACTUAL_NUM_ROOMS_ERR, new String[]{param.get("errNumRoomsEn"), param.get("errNumRoomsId")});
        errorValidationMap.put(ACTUAL_MIN_PRICE_ERR, new String[]{param.get("errMinPriceEn"), param.get("errMinPriceId")});
        errorValidationMap.put(ACTUAL_SMOKING_POLICY_ERR, new String[]{param.get("errSmokingEn"), param.get("errSmokingId")});
        errorValidationMap.put(ACTUAL_AVG_ROOM_SIZE_ERR, new String[]{param.get("errAvgRoomSizeEn"), param.get("errAvgRoomSizeId")});
        errorValidationMap.put(ACTUAL_MULTIPLE_BED_ERR, new String[]{param.get("errMultipleBedEn"), param.get("errMultipleBedId")});
        errorValidationMap.put(ACTUAL_BED_TYPE_ERR, new String[]{param.get("errBedTypeEn"), param.get("errBedTypeId")});
        errorValidationMap.put(ACTUAL_BED_QTY_ERR, new String[]{param.get("errBedQtyEn"), param.get("errBedQtyId")});

        errorValidationMap.forEach((errorLocator, expectedTexts) -> {
            String expectedEn = expectedTexts[0];
            String expectedId = expectedTexts[1];

            if ((expectedEn != null && !expectedEn.isEmpty()) || (expectedId != null && !expectedId.isEmpty())) {
                driverTools.scrollElementToCenter(errorLocator);
                String actualUIError = getActualFieldErrorText(errorLocator);
                Assertion.assertThat(
                        String.format("Validation mismatch! \nExpected EN: [%s] \nOR ID: [%s] \nActual UI Error found: [%s]\n", expectedEn, expectedId, actualUIError),
                        actualUIError,
                        anyOf(containsString(expectedEn), containsString(expectedId))
                );
            }
        });
    }

    public int getNumberOfRoomsValue() {
        WebElement input = driverTools.waitForElementToAppear(NUMBER_OF_ROOMS_INPUT).orElseThrow();
        String valText = driverTools.getValue(input);
        return valText.isEmpty() ? 0 : Integer.parseInt(valText.trim());
    }

    public void clickNumberOfRoomsPlus() {
        WebElement btn = driverTools.waitForElementToBeClickable(NUMBER_OF_ROOMS_PLUS_BTN).orElseThrow();
        driverTools.click(btn);
    }

    public void clickNumberOfRoomsMinus() {
        WebElement btn = driverTools.waitForElementToBeClickable(NUMBER_OF_ROOMS_MINUS_BTN).orElseThrow();
        driverTools.click(btn);
    }

    public void setNumberOfRooms(int targetCount) {
        driverTools.scrollElementToCenter(NUMBER_OF_ROOMS_INPUT);
        int currentCount = getNumberOfRoomsValue();

        while (currentCount < targetCount) {
            clickNumberOfRoomsPlus();
            currentCount++;
        }
        while (currentCount > targetCount) {
            clickNumberOfRoomsMinus();
            currentCount--;
        }
    }

    public boolean isDefaultAllotmentToggleActive() {
        WebElement checkbox = driverTools.waitForElementToPresence(DEFAULT_ALLOTMENT_CHECKBOX).orElseThrow();
        return checkbox.isSelected();
    }

    public void turnOnDefaultAllotmentToggle() {
        driverTools.scrollElementToCenter(DEFAULT_ALLOTMENT_TOGGLE_DIV);
        if (!isDefaultAllotmentToggleActive()) {
            WebElement toggle = driverTools.waitForElementToBeClickable(DEFAULT_ALLOTMENT_TOGGLE_DIV).orElseThrow();
            driverTools.click(toggle);
        }
    }

    public int getDefaultAllotmentValue() {
        WebElement input = driverTools.waitForElementToAppear(DEFAULT_ALLOTMENT_INPUT).orElseThrow();
        String valText = driverTools.getValue(input);
        return valText.isEmpty() ? 0 : Integer.parseInt(valText.trim());
    }

    public void setDefaultAllotmentValue(int targetValue) {
        driverTools.scrollElementToCenter(DEFAULT_ALLOTMENT_INPUT);
        int currentCount = getDefaultAllotmentValue();

        while (currentCount < targetValue) {
            WebElement plusBtn = driverTools.waitForElementToBeClickable(DEFAULT_ALLOTMENT_PLUS_BTN).orElseThrow();
            driverTools.click(plusBtn);
            currentCount++;
        }
        while (currentCount > targetValue) {
            WebElement minusBtn = driverTools.waitForElementToBeClickable(DEFAULT_ALLOTMENT_MINUS_BTN).orElseThrow();
            driverTools.click(minusBtn);
            currentCount--;
        }
    }

    public String getActualAllotmentExceedErrorText() {
        driverTools.scrollElementToCenter(ACTUAL_ALLOTMENT_EXCEED_ERR);
        return driverTools.getText(ACTUAL_ALLOTMENT_EXCEED_ERR).orElse("").trim();
    }

    public void enterMinimumPrice(String price) {
        driverTools.scrollElementToCenter(MINIMUM_PRICE_INPUT);
        WebElement input = driverTools.waitForElementToBeClickable(MINIMUM_PRICE_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, price);
    }

    public String getEnteredMinimumPrice() {
        WebElement input = driverTools.waitForElementToAppear(MINIMUM_PRICE_INPUT).orElseThrow();
        String rawValue = driverTools.getValue(input);
        return rawValue == null ? "" : rawValue.replaceAll("[.,]", "");
    }

    public String getSelectedRoomView() {
        WebElement input = driverTools.waitForElementToAppear(ROOM_VIEW_DROPDOWN_INPUT).orElseThrow();
        return driverTools.getValue(input);
    }

    public void selectRoomViewByPosition(int index) {
        driverTools.scrollElementToCenter(ROOM_VIEW_DROPDOWN_INPUT);
        WebElement inputElement = driverTools.waitForElementToBeClickable(ROOM_VIEW_DROPDOWN_INPUT).orElseThrow();
        driverTools.click(inputElement);
        Locator dropdownContainer = new Locator("VIEW_DROPDOWN_CONTAINER", By.xpath("//ul[li[@data-name='view']]"));
        driverTools.waitForElementToAppear(dropdownContainer);
        Locator targetOption = new Locator("ROOM_VIEW_INDEX_OPTION", By.xpath(String.format("(//li[@data-name='view'])[%d]", index)));
        driverTools.scrollElementToCenter(targetOption);
        WebElement optionElement = driverTools.waitForElementToBeClickable(targetOption).orElseThrow();
        driverTools.click(optionElement);
    }

    public void selectSmokingAndNonSmokingPolicy() {
        driverTools.scrollElementToCenter(SMOKING_BOTH_RADIO_LABEL);
        WebElement label = driverTools.waitForElementToBeClickable(SMOKING_BOTH_RADIO_LABEL).orElseThrow();
        driverTools.click(label);
    }

    public boolean isSmokingAndNonSmokingSelected() {
        WebElement input = driverTools.waitForElementToPresence(SMOKING_BOTH_INPUT).orElseThrow();
        return input.isSelected();
    }

    public void enterAverageRoomSize(String size) {
        driverTools.scrollElementToCenter(AVG_ROOM_SIZE_INPUT);
        WebElement input = driverTools.waitForElementToBeClickable(AVG_ROOM_SIZE_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, size);
    }

    public int getAverageRoomSizeValue() {
        WebElement input = driverTools.waitForElementToAppear(AVG_ROOM_SIZE_INPUT).orElseThrow();
        String valText = driverTools.getValue(input);
        return valText.isEmpty() ? 0 : Integer.parseInt(valText.trim());
    }

    public void clickAverageRoomSizePlus() {
        WebElement btn = driverTools.waitForElementToBeClickable(AVG_ROOM_SIZE_PLUS_BTN).orElseThrow();
        driverTools.click(btn);
    }

    public void clickAverageRoomSizeMinus() {
        WebElement btn = driverTools.waitForElementToBeClickable(AVG_ROOM_SIZE_MINUS_BTN).orElseThrow();
        driverTools.click(btn);
    }

    public int getNumberOfBathroomsValue() {
        WebElement input = driverTools.waitForElementToAppear(BATHROOM_COUNT_INPUT).orElseThrow();
        String valText = driverTools.getValue(input);
        return valText.isEmpty() ? 0 : Integer.parseInt(valText.trim());
    }

    public void clickNumberOfBathroomsPlus() {
        driverTools.scrollElementToCenter(BATHROOM_COUNT_PLUS_BTN);
        WebElement btn = driverTools.waitForElementToBeClickable(BATHROOM_COUNT_PLUS_BTN).orElseThrow();
        driverTools.click(btn);
    }

    public void selectBathroomTypePrivate() {
        driverTools.scrollElementToCenter(BATHROOM_TYPE_PRIVATE_LABEL);
        WebElement label = driverTools.waitForElementToBeClickable(BATHROOM_TYPE_PRIVATE_LABEL).orElseThrow();
        driverTools.click(label);
    }

    public boolean isBathroomTypePrivateSelected() {
        WebElement input = driverTools.waitForElementToPresence(BATHROOM_TYPE_PRIVATE_INPUT).orElseThrow();
        return input.isSelected();
    }

    public void selectFromDropdown(Locator inputLocator, String value) {
        driverTools.scrollElementToCenter(inputLocator);
        driverTools.click(driverTools.waitForElementToBeClickable(inputLocator).orElseThrow());

        Locator option = new Locator("DROPDOWN_OPTION", By.xpath(String.format("//li[text()='%s']", value)));
        driverTools.click(driverTools.waitForElementToBeClickable(option).orElseThrow());
    }

    public void selectMaxOccupancy(String maxOcc) {
        selectFromDropdown(MAX_OCCUPANCY_INPUT, maxOcc);
    }

    public void setOccupancyDetails(String maxOcc, String maxAdults, String maxChildren) {
        selectFromDropdown(MAX_OCCUPANCY_INPUT, maxOcc);
        WebElement radioLabel = driverTools.waitForElementToBeClickable(HAS_MAX_ADULTS_YES).orElseThrow();
        driverTools.click(radioLabel);
        selectFromDropdown(MAX_ADULTS_INPUT, maxAdults);
        selectFromDropdown(MAX_CHILDREN_INPUT, maxChildren);
    }

    public void setMinOccupancy(String minOcc) {
        driverTools.click(driverTools.waitForElementToBeClickable(HAS_MIN_GUEST_YES).orElseThrow());
        selectFromDropdown(MIN_OCCUPANCY_INPUT, minOcc);
    }


    public void verifyAndSetBedroomQuantity(int targetCount) {
        WebElement yesRadio = driverTools.waitForElementToBeClickable(HAS_MULTIPLE_BEDROOMS_YES).orElseThrow();
        driverTools.click(yesRadio);
        driverTools.scrollElementToCenter(BEDROOM_MINUS_BUTTON);
        int currentVal = Integer.parseInt(getBedroomQuantityValue());
        while (currentVal < targetCount) {
            driverTools.click(BEDROOM_PLUS_BUTTON);
            currentVal++;
        }
        while(currentVal > targetCount){
            driverTools.click(BEDROOM_MINUS_BUTTON);
            currentVal--;
        }
    }

    public String getBedroomQuantityValue() {
        return driverTools.waitForElementToPresence(BEDROOM_QTY_INPUT).orElseThrow().getAttribute("value");
    }

    public String getOccupancyValue() {
        return driverTools.waitForElementToAppear(MAX_OCCUPANCY_INPUT).orElseThrow().getAttribute("value");
    }

    public String getMaxAdultsValue() {
        return driverTools.waitForElementToAppear(MAX_ADULTS_INPUT).orElseThrow().getAttribute("value");
    }

    public String getMaxChildrenValue() {
        return driverTools.waitForElementToAppear(MAX_CHILDREN_INPUT).orElseThrow().getAttribute("value");
    }

    public String getMinOccupancyValue() {
        return driverTools.waitForElementToAppear(MIN_OCCUPANCY_INPUT).orElseThrow().getAttribute("value");
    }

    public void verifyRenderedBedroomSectionsCount(int expectedCount) {
        driverTools.waitForElementToAppear(ALL_BED_TYPE_INPUTS);
        List<WebElement> renderedBedrooms = driver.findElements(ALL_BED_TYPE_INPUTS.by());
        Assertion.assertThat("Number of bedRooms Sections are as Expected", renderedBedrooms.size(), is(expectedCount));
    }

    public void selectBedTypeForAllBedrooms(int totalBedrooms, String bedTypeLabel, String bedCount) {
        for (int i = 1; i <= totalBedrooms; i++) {
            Locator bedTypeInput = new Locator("BEDROOM_" + i + "_INPUT", By.xpath(String.format(BED_TYPE_INPUT_TEMPLATE, i)));
            Locator optionLocator = new Locator("BED_OPTION_" + bedTypeLabel, By.xpath(String.format(BED_OPTION_TEMPLATE, bedTypeLabel)));
            driverTools.scrollElementToCenter(bedTypeInput);
            WebElement inputField = driverTools.waitForElementToBeClickable(bedTypeInput).orElseThrow();
            driverTools.click(inputField);
            WebElement optionToClick = driverTools.waitForElementToBeClickable(optionLocator).orElseThrow();
            driverTools.click(optionToClick);

            WebElement verifyInputField = driverTools.waitForElementToBeClickable(bedTypeInput).orElseThrow();
            String actualSelectedValue = verifyInputField.getAttribute("value");
            if (actualSelectedValue == null || actualSelectedValue.isEmpty()) {
                actualSelectedValue = verifyInputField.getText();
            }
            Assertion.assertThat("Bed Type for Bedroom " + i + " is successfully set to " + bedTypeLabel, actualSelectedValue, is(bedTypeLabel));

            Locator bedCountInput = new Locator("BEDROOM_" + i + "_COUNT_INPUT", By.xpath(String.format(BED_COUNT_INPUT_TEMPLATE, i)));
            Locator countOptionLocator = new Locator("BED_COUNT_OPTION_" + bedCount, By.xpath(String.format(BED_OPTION_TEMPLATE, bedCount)));

            driverTools.scrollElementToCenter(bedCountInput);
            WebElement countInputField = driverTools.waitForElementToBeClickable(bedCountInput).orElseThrow();
            driverTools.click(countInputField);

            WebElement countOptionToClick = driverTools.waitForElementToBeClickable(countOptionLocator).orElseThrow();
            driverTools.click(countOptionToClick);

            WebElement verifyCountField = driverTools.waitForElementToBeClickable(bedCountInput).orElseThrow();
            String actualCountValue = verifyCountField.getAttribute("value");
            if (actualCountValue == null || actualCountValue.isEmpty()) {
                actualCountValue = verifyCountField.getText();
            }
            Assertion.assertThat("Number of beds for Bedroom " + i + " is successfully set to " + bedCount, actualCountValue, is(bedCount));
        }
    }

    public void setExtraBedQuantity(String quantity) {
        driverTools.scrollElementToCenter(EXTRA_BED_CHECKBOX);
        WebElement checkboxLabel = driverTools.waitForElementToBeClickable(EXTRA_BED_CHECKBOX).orElseThrow();
        WebElement checkboxInput = checkboxLabel.findElement(By.xpath(".//input[@type='checkbox']"));
        if (!checkboxInput.isSelected()) {
            driverTools.click(checkboxLabel);
        }
        if(!Objects.equals(getExtraBedQuantityValue(), quantity)){
            driverTools.click(EXTRA_BED_INPUT);
            driverTools.dummyWait(1);
            Locator EXTRA_BED_OPTION = getExtraBedOptions(quantity);
            WebElement optionElement = driverTools.waitForElementToAppear(EXTRA_BED_OPTION).orElseThrow();
            driverTools.click(optionElement);
        }
    }

    public String getExtraBedQuantityValue() {
        WebElement input = driverTools.waitForElementToAppear(EXTRA_BED_INPUT).orElseThrow();
        String actualValue = input.getAttribute("value");
        if (actualValue == null || actualValue.isEmpty()) {
            actualValue = input.getText();
        }
        return actualValue;
    }

    public void verifyExtraBedQuantity(String expectedQuantity) {
        String actualQuantity = getExtraBedQuantityValue();
        Assertion.assertThat("Extra bed quantity is successfully set to " + expectedQuantity, actualQuantity, is(expectedQuantity));
    }

    public void uploadPhotosAndSetMain(int numberOfPhotosToSelect) {
        driverTools.scrollElementToCenter(UPLOAD_PHOTO_AREA);
        WebElement uploadArea = driverTools.waitForElementToBeClickable(UPLOAD_PHOTO_AREA).orElseThrow();
        driverTools.click(uploadArea);
        driverTools.waitForElementToAppear(GALLERY_PHOTO_LABELS,1);
        List<WebElement> photoLabels = driver.findElements(GALLERY_PHOTO_LABELS.by());

        for (int i = 0; i < numberOfPhotosToSelect; i++) {
            WebElement label = photoLabels.get(i);
            WebElement checkboxInput = label.findElement(By.xpath(".//input[@type='checkbox']"));
            if (!checkboxInput.isSelected()) {
                driverTools.click(label);
            }
        }

        WebElement confirmAddBtn = driverTools.waitForElementToBeClickable(CONFIRM_ADD_PHOTOS_BTN).orElseThrow();
        driverTools.click(confirmAddBtn);

        driverTools.scrollElementToCenter(SET_MAIN_PHOTO_BTN);
        WebElement mainPhotoBtn = driverTools.waitForElementToBeClickable(SET_MAIN_PHOTO_BTN).orElseThrow();
        driverTools.click(mainPhotoBtn);

        WebElement mainPhotoRibbon = driverTools.waitForElementToAppear(MAIN_PHOTO_LABEL).orElseThrow();
        Assertion.assertThat("Main photo label successfully appeared on the image", mainPhotoRibbon.isDisplayed(), is(true));
    }

    public void clickAllFacilitiesWithLimit(int limitPerCategory) {
        driverTools.scrollElementToCenter(FACILITY_NAV_BUTTONS);
        driverTools.waitForElementToAppear(FACILITY_NAV_BUTTONS);
        int totalCategories = driver.findElements(FACILITY_NAV_BUTTONS.by()).size();
        for (int i = 0; i < totalCategories; i++) {
            List<WebElement> navButtons = driver.findElements(FACILITY_NAV_BUTTONS.by());
            if (i >= navButtons.size()) break;
            WebElement currentNavButton = navButtons.get(i);
            String categoryName = currentNavButton.getText().split("\\n|\\(")[0].trim();
            driverTools.click(currentNavButton);
            driverTools.dummyWait(1);
            selectFacilitiesInCategory(categoryName, limitPerCategory);
        }
    }

    private void selectFacilitiesInCategory(String categoryName, int maxLimit) {
        String dynamicXpath = String.format("//div[span[contains(@class, 'Text_weight_bold') and normalize-space(text())='%s']]//div[contains(@class, 'pb-4 border-b')]", categoryName);
        Locator categoryRows = new Locator("ROWS_IN_" + categoryName, By.xpath(dynamicXpath));
        Locator actualHeaderLocator = new Locator("HEADER_" + categoryName, By.xpath("//span[contains(@class, 'Text_weight_bold') and normalize-space(text())='" + categoryName + "']"));
        String actualHeaderName = driverTools.getText(actualHeaderLocator).orElse("").trim();
        Assertion.assertThat("Select the same correct facility option that is clicked through the nav bar, Expected = " + categoryName+" ,Actual = "+actualHeaderName, actualHeaderName, equalTo(categoryName));
        driverTools.waitForElementToAppear(categoryRows);
        int totalAvailable = driver.findElements(categoryRows.by()).size();
        int elementsToSelect = Math.min(maxLimit, totalAvailable);
        int selectedFacilities = 0;
        for (int i = 0; i < elementsToSelect; i++) {
            List<WebElement> currentRows = driver.findElements(categoryRows.by());
            if (i >= currentRows.size()) break;
            WebElement row = currentRows.get(i);
            driverTools.scrollElementToCenter(row);
            List<WebElement> standardCheckbox = row.findElements(By.xpath(".//label[contains(@class, 'Check_check')]"));
            List<WebElement> popularSvgBtn = row.findElements(By.xpath(".//div[contains(@class, 'flex gap-1')]/div[1]"));
            if (!standardCheckbox.isEmpty()) {
                WebElement label = standardCheckbox.get(0);
                WebElement input = label.findElement(By.xpath(".//input"));
                selectedFacilities+=1;
                if (!input.isSelected()) {
                    driverTools.click(label);
                }
            } else if (!popularSvgBtn.isEmpty()) {
                selectedFacilities+=1;
                driverTools.click(popularSvgBtn.get(0));
            }
        }
        Assertion.assertThat("Number of selected facilities of " +categoryName+" Expected = "+elementsToSelect+" ,Actual = "+selectedFacilities, elementsToSelect, equalTo(selectedFacilities));
    }

    public void waitForSaveSnackbarToAppear() {
        driverTools.waitForElementToAppear(SNACKBAR_TEXT_CONTAINER).orElseThrow();
    }

    public void verifySnackbarMessage(String roomCreateEn, String roomCreateId) {
        WebElement snackbarTextElem = driverTools.waitForElementToAppear(SNACKBAR_TEXT_CONTAINER).orElseThrow();
        String actualMessage = snackbarTextElem.getText().trim();
        Assertion.assertThat("Snackbar message successfully appeared and matches expected text. Expected (EN): " + roomCreateEn + " or (ID): " + roomCreateId + ", Actual: " + actualMessage, actualMessage, anyOf(containsString(roomCreateEn), containsString(roomCreateId)));
    }

    public void verifySearchResults(String roomName) {
        By rowXpath = By.xpath(String.format(SEARCH_ROW_TEMPLATE, roomName));
        int actualRowCount = driver.findElements(rowXpath).size();
        Assertion.assertThat("Verify that exactly 1 search result is displayed for room: '" + roomName + "'", actualRowCount, is(1));
    }

    public boolean isStatusToggleOffByDefault(String roomName) {
        By toggleTrackXpath = By.xpath(String.format(TOGGLE_CLICKABLE_TRACK_TEMPLATE, roomName));
        Locator toggleTrackLocator = new Locator("TOGGLE_TRACK_" + roomName, toggleTrackXpath);
        driverTools.waitForElementToAppear(toggleTrackLocator).orElseThrow();
        By toggleInputXpath = By.xpath(String.format(TOGGLE_INPUT_TEMPLATE, roomName));
        WebElement toggleInput = driver.findElement(toggleInputXpath);
        return !toggleInput.isSelected();
    }

    public void turnOnStatusToggle(String roomName) {
        By toggleTrackXpath = By.xpath(String.format(TOGGLE_CLICKABLE_TRACK_TEMPLATE, roomName));
        Locator toggleTrackLocator = new Locator("TOGGLE_CLICKABLE_TRACK_" + roomName, toggleTrackXpath);
        WebElement clickableToggle = driverTools.waitForElementToBeClickable(toggleTrackLocator,5).orElseThrow();
        driverTools.click(clickableToggle);
    }

    public void verifyStatusModalAppear() {
        WebElement activateBtn = driverTools.waitForElementToAppear(MODAL_ACTIVATE_ROOM_BTN).orElseThrow();
        Assertion.assertThat("Verify 'Activate room' option is displayed in the modal", activateBtn.isDisplayed(), is(true));
        WebElement completeDataBtn = driverTools.waitForElementToAppear(MODAL_COMPLETE_DATA_BTN).orElseThrow();
        Assertion.assertThat("Verify 'Complete data' option is displayed in the modal", completeDataBtn.isDisplayed(), is(true));
    }

    public void clickCompleteDataButtonInModal() {
        WebElement completeDataBtn = driverTools.waitForElementToBeClickable(MODAL_COMPLETE_DATA_BTN).orElseThrow();
        driverTools.click(completeDataBtn);
    }

    public void clickActivateRoomButtonInModal() {
        WebElement activateBtn = driverTools.waitForElementToBeClickable(MODAL_ACTIVATE_ROOM_BTN).orElseThrow();
        driverTools.click(activateBtn);
    }
    public void navigateToPreviousPage() {
        driver.navigate().back();
    }

    public void clickFirstEditButton() {
        Locator editBtnLocator = new Locator("EDIT_BTN_FIRST_ROW", By.xpath("//table/tbody/tr[1]//button[contains(normalize-space(.), 'Edit') or contains(normalize-space(.), 'Ubah')]"));
        WebElement editBtn = driverTools.waitForElementToBeClickable(editBtnLocator).orElseThrow();
        driverTools.click(editBtn);
    }

    public void clickEditButton(String roomName) {
        String dynamicXPath = String.format(EDIT_ROOM_BTN_TEMPLATE, roomName);
        Locator editBtnLocator = new Locator("EDIT_BTN_" + roomName, By.xpath(dynamicXPath));
        WebElement editBtn = driverTools.waitForElementToBeClickable(editBtnLocator).orElseThrow();
        driverTools.click(editBtn);
    }

    public void clickDeleteButton(String roomName) {
        String dynamicXPath = String.format(DELETE_ROOM_BTN_TEMPLATE, roomName);
        Locator deleteBtnLocator = new Locator("DELETE_BTN_" + roomName, By.xpath(dynamicXPath));
        WebElement deleteBtn = driverTools.waitForElementToBeClickable(deleteBtnLocator).orElseThrow();
        driverTools.click(deleteBtn);
    }

    public void clickEditRoomSubmitButton() {
        Assertion.assertThat("Edit Room Button Displayed",driverTools.scrollAndCheckElement(EDIT_ROOM_SUBMIT_BTN), is(true));
        WebElement submitBtn = driverTools.waitForElementToAppear(EDIT_ROOM_SUBMIT_BTN).orElseThrow();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", submitBtn);
    }

    public void verifyDeleteModalAppears() {
        WebElement yesBtn = driverTools.waitForElementToAppear(MODAL_DELETE_YES_BTN).orElseThrow();
        Assertion.assertThat("Verify 'Yes' option is displayed in the delete modal", yesBtn.isDisplayed(), is(true));
        WebElement noBtn = driverTools.waitForElementToAppear(MODAL_DELETE_NO_BTN).orElseThrow();
        Assertion.assertThat("Verify 'No' option is displayed in the delete modal", noBtn.isDisplayed(), is(true));
    }

    public void clickDeleteModalNoButton() {
        WebElement noBtn = driverTools.waitForElementToBeClickable(MODAL_DELETE_NO_BTN).orElseThrow();
        driverTools.click(noBtn);
    }

    public void clickDeleteModalYesButton() {
        WebElement yesBtn = driverTools.waitForElementToBeClickable(MODAL_DELETE_YES_BTN).orElseThrow();
        driverTools.click(yesBtn);
    }

    public void verifyRoomIsDeletedAndNoDataFound() {
        WebElement noDataHeading = driverTools.waitForElementToAppear(NO_DATA_FOUND_MESSAGE).orElseThrow();
        Assertion.assertThat("Verify 'No data found' is displayed, confirming the room is deleted", noDataHeading.isDisplayed(), is(true));
    }
}