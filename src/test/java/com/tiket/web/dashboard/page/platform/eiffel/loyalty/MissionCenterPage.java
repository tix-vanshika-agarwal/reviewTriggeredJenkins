package com.tiket.web.dashboard.page.platform.eiffel.loyalty;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.web.WebPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.core.Is.is;

/**
 * Mission Center page of the Loyalty admin panel ({@code /admin/care/loyalty/mission-config/center}),
 * where the order missions are presented in on the consumer Mission List Page is configured.
 *
 * <p>The sequence table is read-only until Edit is clicked; edit mode reveals an info banner and a drag
 * handle on every row, and rows are reordered by dragging those handles.
 *
 * <p><b>Why the reorder is driven from the keyboard.</b> The table is a dnd-kit sortable list, and dnd-kit
 * ignores synthetic pointer events — a WebDriver mouse drag never even starts a drag, so the sequence is
 * left untouched. Its keyboard sensor is fully supported though (each handle is {@code role="button"},
 * {@code tabindex="0"}, {@code aria-roledescription="sortable"}): space lifts a row, the arrow keys move it,
 * and space drops it. That runs the same reorder logic and produces the same save payload as a mouse drag,
 * so {@link #moveMissionToOrder(int, int)} uses it.
 */
public class MissionCenterPage<T extends WebDriver> extends WebPage<T> {

    /** Heading of the sequence card in both view and edit mode. */
    public static final String SEQUENCE_TITLE = "Sequence List";

    /** Note under the sequence heading, shown in both view and edit mode. */
    public static final String SEQUENCE_NOTE = "Manage the sequence of the mission center.";

    /** Info banner the card reveals only in edit mode. */
    public static final String EDIT_MODE_BANNER = "Drag and move the rows to readjust the sequence.";

    /** Columns of the sequence table, left to right. */
    public static final List<String> SEQUENCE_COLUMNS = List.of("Order", "Header", "Name", "Slug");

    /** Saved mobile and desktop banner filenames shown on the Header Image card. */
    public record HeaderImages(String mobileFilename, String desktopFilename) {
    }

    private static final String MISSION_CENTER_URL = "/admin/care/loyalty/mission-config/center";

    private static final int MOBILE_HEADER_WIDTH = 838;
    private static final int MOBILE_HEADER_HEIGHT = 240;
    private static final int DESKTOP_HEADER_WIDTH = 1440;
    private static final int DESKTOP_HEADER_HEIGHT = 484;

    private static final String STAGED_MOBILE_PREFIX = "mission-center-mobile-";
    private static final String STAGED_DESKTOP_PREFIX = "mission-center-desktop-";

    private static final String SEQUENCE_CARD = "//*[contains(@class,'MissionCenterSequence_mission_center_sequence_container')]";
    private static final String SEQUENCE_ROW = "//*[contains(@class,'MissionCenterSequence_sortable_item')]";

    private static final int DEFAULT_WAIT_TIMEOUT = 20;

    /**
     * Pause after each arrow key so dnd-kit's keyboard sensor finishes the step before the next one.
     * The slug column is not rewritten until the row is dropped, so this cannot be an order wait.
     */
    private static final int REORDER_STEP_WAIT = 1;

    /** How long to wait for a dropped row to land on its new Order. */
    private static final int REORDER_SETTLE_SECONDS = 10;

    private static final Locator SEQUENCE_TITLE_LABEL = new Locator("SEQUENCE_TITLE_LABEL", By.xpath(SEQUENCE_CARD + "/div[1]/div[contains(@class,'Text_text')]"));
    private static final Locator SEQUENCE_NOTE_LABEL = new Locator("SEQUENCE_NOTE_LABEL", By.xpath(SEQUENCE_CARD + "/div[1]/span[contains(@class,'Text_text')]"));
    private static final Locator EDIT_MODE_BANNER_CONTAINER = new Locator("EDIT_MODE_BANNER_CONTAINER", By.xpath(SEQUENCE_CARD + "//*[@data-testid='collapse-container']"));
    private static final Locator EDIT_MODE_BANNER_LABEL = new Locator("EDIT_MODE_BANNER_LABEL", By.xpath(SEQUENCE_CARD + "//*[@data-testid='collapse-container']//p"));
    private static final Locator COLUMN_HEADER = new Locator("COLUMN_HEADER", By.xpath("//*[contains(@class,'MissionCenterSequence_header')]//div[contains(@class,'Text_text')]"));

    private static final Locator ROW_ORDER = new Locator("ROW_ORDER", By.xpath(SEQUENCE_ROW + "//div[contains(@class,'MissionCenterSequence_item')][1]"));
    private static final Locator ROW_SLUG = new Locator("ROW_SLUG", By.xpath(SEQUENCE_ROW + "//div[contains(@class,'MissionCenterSequence_slug__')]"));
    private static final Locator DRAG_HANDLE = new Locator("DRAG_HANDLE", By.xpath(SEQUENCE_ROW + "//*[@aria-roledescription='sortable']"));

    /** Toggles between view mode ("Edit") and edit mode ("Cancel"). The Save button exists only in edit mode. */
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.cssSelector("[data-testid='edit-btn']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.cssSelector("[data-testid='save-btn']"));

    private static final String HEADER_IMAGE_CARD_XPATH =
            "//*[contains(@class,'MIssionCenterHeaderImage_header_image_card')]";
    private static final Locator HEADER_IMAGE_CARD = new Locator("HEADER_IMAGE_CARD", By.xpath(HEADER_IMAGE_CARD_XPATH));
    private static final Locator SAVED_HEADER_FILENAME = new Locator("SAVED_HEADER_FILENAME",
            By.xpath(HEADER_IMAGE_CARD_XPATH + "//*[contains(@class,'SingleFileUploadCustom_name')]//p"));
    private static final Locator STAGED_HEADER_FILENAME = new Locator("STAGED_HEADER_FILENAME",
            By.xpath(HEADER_IMAGE_CARD_XPATH + "//span[contains(@class,'SingleFileUpload_filename')]"));
    private static final Locator MOBILE_FILE_INPUT = headerFileInput("MOBILE_FILE_INPUT",
            MOBILE_HEADER_WIDTH, MOBILE_HEADER_HEIGHT);
    private static final Locator DESKTOP_FILE_INPUT = headerFileInput("DESKTOP_FILE_INPUT",
            DESKTOP_HEADER_WIDTH, DESKTOP_HEADER_HEIGHT);

    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class,'Snackbar_snackbar') and contains(@class,'Snackbar_type_success')]"));

    private HeaderImages lastStagedHeaderImages;

    public MissionCenterPage(Session<T> session) {
        super(session);
    }

    public void goToMissionCenterPage(String userName, String password) {
        MemberLoginPage<T> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(userName, password);
        openMissionCenterPage();
    }

    /** Re-navigates to the Mission Center page, so a saved sequence is re-read from the backend. */
    public void reloadMissionCenterPage() {
        openMissionCenterPage();
    }

    /**
     * Enters edit mode. The drag banner expands with an animation, so it measures as zero-height
     * for a beat after Save appears — the wait is for the expanded container, not merely its presence.
     */
    public void clickEdit() {
        driverTools.click(EDIT_BUTTON);
        driverTools.waitForElementToAppear(SAVE_BUTTON, DEFAULT_WAIT_TIMEOUT).orElseThrow();
        driverTools.waitForElementToAppear(EDIT_MODE_BANNER_CONTAINER, DEFAULT_WAIT_TIMEOUT)
                .orElseThrow(() -> new IllegalStateException(
                        "Edit mode did not expand the drag banner"));
    }

    public void clickSave() {
        driverTools.scrollElementToCenter(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, DEFAULT_WAIT_TIMEOUT).orElseThrow();
    }

    /**
     * Leaves edit mode without saving. The same {@code edit-btn} that entered edit mode is labelled
     * Cancel once Save is on screen. Waits until Save is gone and any staged header-image filenames
     * have left the card.
     */
    public void clickCancel() {
        driverTools.scrollElementToCenter(EDIT_BUTTON);
        driverTools.click(EDIT_BUTTON);
        boolean leftEditMode = driverTools.waitForElementToDisappear(SAVE_BUTTON, DEFAULT_WAIT_TIMEOUT);
        if (!leftEditMode) {
            throw new IllegalStateException("Cancel did not return the Mission Center page to read-only");
        }
        waitForStagedFilenamesToDisappear();
    }

    /**
     * @return the filenames currently shown on the saved Mobile and Desktop header-image placeholders
     */
    public HeaderImages getSavedHeaderImages() {
        List<String> filenames = waitForSavedHeaderFilenames();
        return new HeaderImages(filenames.get(0), filenames.get(1));
    }

    public void verifySavedHeaderImages(HeaderImages expected) {
        Assertion.assertThat("The saved mobile and desktop banner filenames are unchanged",
                getSavedHeaderImages(), is(expected));
    }

    /**
     * Stages a replacement in each Header Image upload slot. The saved placeholders stay as they are
     * until Save; Cancel discards these staged files.
     */
    public void replaceHeaderImagesWithStagedAssets() {
        driverTools.scrollElementToCenter(HEADER_IMAGE_CARD);
        File mobileImage = null;
        File desktopImage = null;
        try {
            mobileImage = driverTools.writeTempPng(STAGED_MOBILE_PREFIX, MOBILE_HEADER_WIDTH, MOBILE_HEADER_HEIGHT,
                    new Color(0x00, 0x7A, 0xFF));
            desktopImage = driverTools.writeTempPng(STAGED_DESKTOP_PREFIX, DESKTOP_HEADER_WIDTH, DESKTOP_HEADER_HEIGHT,
                    new Color(0xFF, 0x5A, 0x1F));
            lastStagedHeaderImages = new HeaderImages(mobileImage.getName(), desktopImage.getName());
            stageHeaderImage(MOBILE_FILE_INPUT, mobileImage);
            stageHeaderImage(DESKTOP_FILE_INPUT, desktopImage);
        } finally {
            deleteStagedHeaderImage(mobileImage);
            deleteStagedHeaderImage(desktopImage);
        }
    }

    public void verifyStagedHeaderImagesDisplayed() {
        if (lastStagedHeaderImages == null) {
            throw new IllegalStateException("No header images have been staged on this page");
        }
        Assertion.assertThat("Edit mode stages a replacement for both header-image slots",
                getStagedHeaderFilenames(),
                is(List.of(lastStagedHeaderImages.mobileFilename(), lastStagedHeaderImages.desktopFilename())));
    }

    public void verifyStagedHeaderImagesDiscarded() {
        Assertion.assertThat("Cancel discards the staged mobile and desktop header images",
                getStagedHeaderFilenames(), is(List.of()));
    }

    public String getSequenceTitle() {
        return driverTools.getText(SEQUENCE_TITLE_LABEL).orElse("");
    }

    public String getSequenceNote() {
        return driverTools.getText(SEQUENCE_NOTE_LABEL).orElse("");
    }

    public List<String> getColumnHeaders() {
        return driverTools.getTexts(COLUMN_HEADER);
    }

    /**
     * @return the mission slugs in the sequence table, top to bottom. Waits until every row has a slug
     *         so the list length matches the table and drag-handle indexes stay aligned.
     */
    public List<String> getSequenceSlugs() {
        return waitForPopulatedSequenceSlugs();
    }

    /**
     * The banner sits in a collapse container that is left in the DOM and merely collapsed in view mode, so
     * it has to be judged on whether it is actually on screen rather than on whether it can be found.
     *
     * <p>Deliberately does not wait. View mode expects the banner hidden, and a wait for something that is
     * supposed to be absent only burns the timeout before every negative check; {@link #clickEdit()} does the
     * waiting on the way into edit mode instead.
     *
     * @return {@code true} when the edit-mode drag banner is expanded
     */
    public boolean isEditModeBannerDisplayed() {
        return countVisible(EDIT_MODE_BANNER_CONTAINER) > 0;
    }

    /**
     * The banner lives in a collapse that animates open, and Selenium reads no text out of an element that
     * is still clipped to zero height — right after Edit is clicked the copy comes back empty even though
     * the element is already found and already reports as visible.
     *
     * @return the banner copy, or an empty string if it never rendered
     */
    public String getEditModeBannerText() {
        return driverTools.waitUntil(d -> {
            String text = driverTools.getText(EDIT_MODE_BANNER_LABEL).orElse("");
            return text.isEmpty() ? null : text;
        }, DEFAULT_WAIT_TIMEOUT).orElse("");
    }

    /**
     * Drag handles are rendered on every row in both modes and only hidden with CSS in view mode, so
     * they have to be counted by visibility rather than by presence.
     *
     * @return how many rows currently show a usable drag handle
     */
    public int getVisibleDragHandleCount() {
        return countVisible(DRAG_HANDLE);
    }

    /**
     * Moves the mission at {@code fromOrder} to {@code toOrder} using the sortable list's keyboard
     * controls, and waits until it has landed there.
     *
     * @param fromOrder the mission's current position in the Order column (1-based)
     * @param toOrder   the position it should end up at (1-based)
     */
    public void moveMissionToOrder(int fromOrder, int toOrder) {
        List<String> slugsBeforeMove = getSequenceSlugs();
        List<WebElement> handles = driverTools.elements(DRAG_HANDLE).orElse(List.of());
        requireSlugAndHandleAlignment(slugsBeforeMove, handles);
        requireValidOrders(slugsBeforeMove, fromOrder, toOrder);

        String slug = slugsBeforeMove.get(fromOrder - 1);
        logger.log("Moving mission '" + slug + "' from Order " + fromOrder + " to Order " + toOrder);
        liftAndMoveSlug(handles.get(fromOrder - 1), slug, fromOrder, toOrder);
    }

    /**
     * Checks the table against the sequence that moving one mission should have produced: the moved
     * mission sits at its new Order, every other mission keeps its relative position, and the Order
     * column still runs 1..N.
     *
     * @param sequenceBeforeMove the slugs read before the move
     * @param fromOrder          the Order the moved mission started at (1-based)
     * @param toOrder            the Order it was moved to (1-based)
     */
    public void verifySequenceAfterMove(List<String> sequenceBeforeMove, int fromOrder, int toOrder) {
        if (sequenceBeforeMove == null || sequenceBeforeMove.isEmpty()) {
            throw new AssertionError("Cannot verify the sequence after the move — no slugs were recorded beforehand");
        }
        requireValidOrders(sequenceBeforeMove, fromOrder, toOrder);

        List<String> expected = new ArrayList<>(sequenceBeforeMove);
        expected.add(toOrder - 1, expected.remove(fromOrder - 1));

        Assertion.assertThat("The mission moved from Order " + fromOrder + " sits at Order " + toOrder
                        + " and the rest of the sequence shifts around it",
                getSequenceSlugs(), is(expected));
        verifyOrderColumnIsContiguous();
    }

    /** Checks the Order column runs 1..N with no gaps and no duplicates. */
    public void verifyOrderColumnIsContiguous() {
        List<String> actualOrders = driverTools.getTexts(ROW_ORDER);
        List<String> expectedOrders = new ArrayList<>();
        for (int order = 1; order <= actualOrders.size(); order++) {
            expectedOrders.add(String.valueOf(order));
        }
        Assertion.assertThat("Order runs 1.." + actualOrders.size() + " with no gaps and no duplicates",
                actualOrders, is(expectedOrders));
    }

    /**
     * Checks the missions the consumer Mission List Page shows appear in the sequence configured here.
     *
     * <p>The Mission List Page shows a subset of the panel's rows — only missions that are live and that
     * the caller is segmented into — so this compares against the configured sequence narrowed to that
     * subset rather than to the full table.
     *
     * @param liveMissionSlugs the slugs the Mission List Page presents, in the order it presents them
     */
    public void verifyLiveMissionsFollowConfiguredOrder(List<String> liveMissionSlugs) {
        if (liveMissionSlugs == null || liveMissionSlugs.isEmpty()) {
            throw new AssertionError("Mission List Page returned no live missions — cannot verify configured order");
        }
        List<String> configuredOrder = getSequenceSlugs();
        Assertion.assertThat("Every mission the Mission List Page shows is configured on the Mission Center page",
                liveMissionSlugs, everyItem(is(in(configuredOrder))));

        List<String> configuredOrderOfLiveMissions = new ArrayList<>(configuredOrder);
        configuredOrderOfLiveMissions.retainAll(liveMissionSlugs);
        Assertion.assertThat("The Mission List Page lists the missions in the sequence configured on the "
                        + "Mission Center page, without needing a redeployment",
                liveMissionSlugs, is(configuredOrderOfLiveMissions));
    }

    public void verifySuccessSnackbarDisplayed(String expectedMessage) {
        boolean isDisplayed = driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, DEFAULT_WAIT_TIMEOUT).isPresent();
        String actualMessage = driverTools.getText(SUCCESS_SNACKBAR).orElse("");
        boolean disappeared = driverTools.waitForElementToDisappear(SUCCESS_SNACKBAR, DEFAULT_WAIT_TIMEOUT);
        boolean leftEditMode = driverTools.waitForElementToDisappear(SAVE_BUTTON, DEFAULT_WAIT_TIMEOUT);

        Assertion.assertThat("Saving the sequence shows the success snackbar", isDisplayed, is(true));
        Assertion.assertThat("Saving the sequence confirms with the success copy", actualMessage, is(expectedMessage));
        Assertion.assertThat("The success snackbar dismisses after confirming the save", disappeared, is(true));
        Assertion.assertThat("Saving returns the sequence to read-only", leftEditMode, is(true));
    }

    private void openMissionCenterPage() {
        new MemberLoginPage<>(session).goToPage(MISSION_CENTER_URL);
        driverTools.waitForWebToLoad();
        driverTools.waitForElementsToAppear(ROW_SLUG, DEFAULT_WAIT_TIMEOUT).orElseThrow();
        waitForPopulatedSequenceSlugs();
    }

    /**
     * Counts matches that are actually on screen. This page hides both the drag handles and the edit-mode
     * banner with CSS instead of removing them, so presence says nothing about which mode the card is in.
     * Returns 0 rather than blocking when nothing matches, which is a legitimate answer in view mode.
     */
    private int countVisible(Locator locator) {
        return (int) driverTools.elements(locator).orElse(List.of()).stream()
                .filter(WebElement::isDisplayed)
                .count();
    }

    /** Sends a key to the focused drag handle; the sortable list reads them off the document. */
    private void pressKey(WebElement handle, Keys key) {
        driverTools.sendKeysKeyboardNoClick(handle, key.toString());
    }

    /**
     * Lifts the focused handle with Space, walks it with the arrow keys, then drops it with Space.
     * Order is asserted only after the drop — dnd-kit keeps the original slug column while a row is
     * still in the air.
     */
    private void liftAndMoveSlug(WebElement handle, String slug, int fromOrder, int toOrder) {
        driverTools.scrollElementToCenter(handle);
        driverTools.focusWithJavascriptExecutor(handle);

        pressKey(handle, Keys.SPACE);
        driverTools.dummyWait(REORDER_STEP_WAIT);
        Keys direction = toOrder < fromOrder ? Keys.ARROW_UP : Keys.ARROW_DOWN;
        for (int step = 0; step < Math.abs(toOrder - fromOrder); step++) {
            pressKey(handle, direction);
            driverTools.dummyWait(REORDER_STEP_WAIT);
        }
        pressKey(handle, Keys.SPACE);
        waitForSlugToReachOrder(slug, toOrder);
    }

    private List<String> waitForPopulatedSequenceSlugs() {
        return driverTools.waitUntil(d -> {
            List<String> slugs = readSequenceSlugTexts();
            int rowCount = driverTools.elements(ROW_SLUG).orElse(List.of()).size();
            if (rowCount == 0 || slugs.size() != rowCount || slugs.stream().anyMatch(String::isEmpty)) {
                return null;
            }
            return slugs;
        }, DEFAULT_WAIT_TIMEOUT).orElseThrow(() -> {
            List<String> slugs = readSequenceSlugTexts();
            int rowCount = driverTools.elements(ROW_SLUG).orElse(List.of()).size();
            return new IllegalStateException("Sequence slugs did not finish rendering (rows=" + rowCount
                    + ", slugs=" + slugs + ")");
        });
    }

    /**
     * Reads slug cells without dropping blanks, so a still-empty cell cannot shrink the list
     * and shift later rows onto the wrong index.
     */
    private List<String> readSequenceSlugTexts() {
        List<String> slugs = new ArrayList<>();
        for (WebElement cell : driverTools.elements(ROW_SLUG).orElse(List.of())) {
            String text = driverTools.getText(cell);
            slugs.add(text == null ? "" : text.trim());
        }
        return slugs;
    }

    private static void requireSlugAndHandleAlignment(List<String> slugs, List<WebElement> handles) {
        if (handles.size() != slugs.size()) {
            throw new AssertionError("Drag handles (" + handles.size() + ") and slugs (" + slugs.size()
                    + ") are out of sync: " + slugs);
        }
    }

    private static void requireValidOrders(List<String> slugs, int fromOrder, int toOrder) {
        if (fromOrder < 1 || toOrder < 1 || fromOrder > slugs.size() || toOrder > slugs.size()) {
            throw new AssertionError("Cannot move Order " + fromOrder + " to Order " + toOrder
                    + " — sequence has " + slugs.size() + " mission(s): " + slugs);
        }
    }

    private void waitForSlugToReachOrder(String slug, int expectedOrder) {
        driverTools.waitUntil(d -> {
            List<String> slugs = readSequenceSlugTexts();
            boolean reached = slugs.size() >= expectedOrder && slug.equals(slugs.get(expectedOrder - 1));
            if (!reached) {
                logger.log("Mission '" + slug + "' not yet at Order " + expectedOrder + ", current=" + slugs);
            }
            return reached;
        }, REORDER_SETTLE_SECONDS).orElseThrow(() -> new IllegalStateException(
                "Mission '" + slug + "' did not reach Order " + expectedOrder
                        + " after the keyboard drag - the sortable list never picked the row up. Current sequence: "
                        + readSequenceSlugTexts()));
    }

    private static Locator headerFileInput(String name, int width, int height) {
        return new Locator(name, By.xpath(String.format(
                HEADER_IMAGE_CARD_XPATH
                        + "//*[contains(@class,'SingleFileUploadCustom_upload_file') and .//span[contains(text(),'%dx%d')]]"
                        + "//input[@type='file']",
                width, height)));
    }

    private List<String> waitForSavedHeaderFilenames() {
        return driverTools.waitUntil(d -> {
            List<String> filenames = readFilenameTexts(SAVED_HEADER_FILENAME);
            return filenames.size() == 2 ? filenames : null;
        }, DEFAULT_WAIT_TIMEOUT).orElseThrow(() -> new IllegalStateException(
                "Saved header-image filenames did not finish rendering: "
                        + readFilenameTexts(SAVED_HEADER_FILENAME)));
    }

    private List<String> getStagedHeaderFilenames() {
        return readFilenameTexts(STAGED_HEADER_FILENAME);
    }

    private List<String> readFilenameTexts(Locator locator) {
        List<String> filenames = new ArrayList<>();
        for (WebElement cell : driverTools.elements(locator).orElse(List.of())) {
            if (!cell.isDisplayed()) {
                continue;
            }
            String text = driverTools.getText(cell);
            if (text != null && !text.isBlank()) {
                filenames.add(text.trim());
            }
        }
        return filenames;
    }

    private void stageHeaderImage(Locator fileInput, File image) {
        driverTools.waitForElementToPresence(fileInput, DEFAULT_WAIT_TIMEOUT).orElseThrow();
        driverTools.sendKeys(fileInput, image.getAbsolutePath());
        waitForStagedFilename(image.getName());
    }

    private void waitForStagedFilename(String filename) {
        driverTools.waitUntil(d -> getStagedHeaderFilenames().contains(filename), DEFAULT_WAIT_TIMEOUT)
                .orElseThrow(() -> new IllegalStateException(
                        "Staged header image '" + filename + "' did not appear. Staged="
                                + getStagedHeaderFilenames()));
    }

    private void waitForStagedFilenamesToDisappear() {
        driverTools.waitUntil(d -> getStagedHeaderFilenames().isEmpty(), DEFAULT_WAIT_TIMEOUT)
                .orElseThrow(() -> new IllegalStateException(
                        "Cancel did not discard the staged header images. Staged="
                                + getStagedHeaderFilenames()));
    }

    private void deleteStagedHeaderImage(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.delete()) {
            logger.log("Deleted staged header image " + file.getAbsolutePath());
        } else {
            logger.softWarning("Could not delete staged header image " + file.getAbsolutePath());
        }
    }
}
