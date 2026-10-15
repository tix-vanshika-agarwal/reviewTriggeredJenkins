package com.tiket.page.common.child_age;

import static org.hamcrest.Matchers.is;

import java.util.List;

import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ChildAgePickerPageAndroid  <T extends WebDriver> extends ChildAgePickerPage<T> {

    private static final int PICKER_MAX_CORRECTION_ROUNDS = 10;
    private static final int PICKER_STABILIZE_READS = 4;
    private static final Locator SCROLL_VIEW = new Locator("SCROLL_VIEW", AppiumBy.id("tds_rv_picker_group"));
    private static final Locator CHILD_AGE_DISPLAYED_1 = new Locator("CHILD_AGE_DISPLAYED", AppiumBy.xpath("(//*[contains(@resource-id,'tds_body_picker')])[1]"));
    private static final Locator CHILD_AGE_DISPLAYED_2 = new Locator("CHILD_AGE_DISPLAYED", AppiumBy.xpath("(//*[contains(@resource-id,'tds_body_picker')])[2]"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", AppiumBy.id("tds_btn"));
    private static final Locator SELECTED_CHILD_AGE = new Locator("SELECTED_CHILD_AGE", AppiumBy.id("tv_child_age"));

    public ChildAgePickerPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void setChildAgeByIndex(int index, String age) {
        int convertAge = age.equals("<1") ? 0 : Integer.parseInt(age);
        driverTools.click(getChildAgePickerByIndex(index));
        setPickerAgeTo(convertAge);
        driverTools.click(SUBMIT_BUTTON);
    }

    @Override
    public void setChildAge(int total, int age) {
        if (age != 12) {
            driverTools.swipeUp();
            for (int i = 1; i < total+1; i++) {
                driverTools.click(getChildAgePickerByIndex(i));

                for (int j = 0; j < 10; j++) {
                    int ageDisplayed = getChildAgeDisplayed();
                    if (age > ageDisplayed) {
                        increment();
                    } else if (age < ageDisplayed) {
                        decrement();
                    } else {
                        break;
                    }
                }
                driverTools.click(SUBMIT_BUTTON);
            }
        }
    }

    @Override
    public void increment() {
        driverTools.moveElementVertical(CHILD_AGE_DISPLAYED_1, CHILD_AGE_DISPLAYED_2, 1.0);
    }

    @Override
    public void decrement() {
        driverTools.moveElementVertical(CHILD_AGE_DISPLAYED_2, CHILD_AGE_DISPLAYED_1, 1.0);
    }

    private Locator getChildAgePickerByIndex(int index) {
        return new Locator("CHILD_PICKER_AGE_"+index, AppiumBy.xpath("//*[contains(@resource-id,'rv_children_age')]/child::*["+ index +"]"));
    }

    private int getChildAgeDisplayed() {
        WebElement childAge = driverTools.waitForElementToAppear(CHILD_AGE_DISPLAYED_2).orElseThrow();
        String childAgeText = driverTools.getText(childAge);
        if (childAgeText.equals("<1")) {
            childAgeText = "0";
        }
        return Integer.parseInt(childAgeText);
    }

    @Override
    public boolean checkSelectedChildAge(int total, List<String> childAges) {
        Integer selectedAgeText;
        for (int i = 1; i <= total; i++) {
            WebElement selectedAgeElement = driverTools.waitForElementToAppear(getSelectedChildAgeLocatorByIndex(i)).orElseThrow();
            String ageText = driverTools.getText(selectedAgeElement);
            String cleanedAgeText = ageText.replaceAll("[^0-9<]", "");
            if (cleanedAgeText.contains("<1")) {
                selectedAgeText = 0;
            } else {
                selectedAgeText = Integer.parseInt(cleanedAgeText);
            }
            Assertion.assertThat("selected child age should be correct on the index: " + i, selectedAgeText, is(Integer.parseInt(childAges.get(i-1).trim())));
        }
        return driverTools.isElementDisplayed(getSelectedChildAgeLocatorByIndex(1));
    }

    private static Locator getSelectedChildAgeLocatorByIndex(int index) {
        return new Locator("SELECTED_CHILD_AGE_"+index, AppiumBy.xpath("(//*[contains(@resource-id,'tv_child_age')])["+index+"]"));
    }

    @Override
    public int getSelectedChildAgeFromLabel(int index) {
        WebElement selectedAgeElement = driverTools.waitForElementToAppear(getSelectedChildAgeLocatorByIndex(index)).orElseThrow();
        String cleanedAgeText = driverTools.getText(selectedAgeElement).replaceAll("[^0-9<]", "");
        return cleanedAgeText.contains("<1") ? 0 : Integer.parseInt(cleanedAgeText);
    }

    @Override
    public void openChildAgePicker(int index) {
        driverTools.click(getChildAgePickerByIndex(index));
    }

    @Override
    public int getPickerSelectedAge() {
        return getChildAgeDisplayed();
    }

    @Override
    public void setPickerAgeTo(int targetAge) {
        int current = getStableChildAgeDisplayed();
        if (current == targetAge) {
            return;
        }

        // Capture the two visible picker slots ONCE and reuse their coordinates for every step.
        // Re-locating the elements and re-reading getLocation()/getSize() on each step is extremely
        // slow because UiAutomator2 blocks on "wait for idle" while the age wheel is still animating.
        WebElement topSlot = driverTools.waitForElementToPresence(CHILD_AGE_DISPLAYED_1).orElseThrow();
        WebElement selectedSlot = driverTools.waitForElementToPresence(CHILD_AGE_DISPLAYED_2).orElseThrow();
        int topY = topSlot.getLocation().getY() + (topSlot.getSize().getHeight() / 2);
        int selectedY = selectedSlot.getLocation().getY() + (selectedSlot.getSize().getHeight() / 2);
        int trackX = selectedSlot.getLocation().getX() + (selectedSlot.getSize().getWidth() / 2);

        // Send the whole delta in one fast burst, then re-read and re-send only the remaining delta.
        // A fast burst can register fewer steps than sent, so this converges over a few rounds. We do
        // NOT settle/read between individual swipes (that is what made this slow), and we fail fast
        // rather than silently submitting a wrong age if the picker never reaches the target.
        int now = current;
        for (int guard = 0; guard < PICKER_MAX_CORRECTION_ROUNDS && now != targetAge; guard++) {
            swipePickerBySteps(trackX, topY, selectedY, targetAge - now);
            now = getStableChildAgeDisplayed();
        }
        if (now != targetAge) {
            throw new DriverToolsException("child age picker did not reach target age " + targetAge
                    + " (stuck at " + now + ") after " + PICKER_MAX_CORRECTION_ROUNDS + " correction rounds");
        }
    }

    /**
     * Reads the displayed age but tolerates the wheel still animating after a fast swipe burst:
     * with the UiAutomator2 idle wait disabled a single read can catch a mid-animation value, so this
     * reads until two consecutive reads agree (the wheel has settled). Bounded by
     * {@link #PICKER_STABILIZE_READS} and uses no fixed sleep — the natural read round-trip provides
     * the spacing, so the settled/happy path stays fast.
     */
    private int getStableChildAgeDisplayed() {
        int last = getChildAgeDisplayed();
        for (int i = 0; i < PICKER_STABILIZE_READS; i++) {
            int value = getChildAgeDisplayed();
            if (value == last) {
                return value;
            }
            last = value;
        }
        return last;
    }

    /**
     * Scrolls the age wheel by {@code steps} positions using the pre-computed slot coordinates.
     * Positive {@code steps} increments the age (drag top slot down onto the selected slot),
     * negative decrements it (drag the selected slot up onto the top slot).
     */
    private void swipePickerBySteps(int trackX, int topY, int selectedY, int steps) {
        if (steps == 0) {
            return;
        }
        int fromY = steps > 0 ? topY : selectedY;
        int toY = steps > 0 ? selectedY : topY;
        for (int i = 0; i < Math.abs(steps); i++) {
            driverTools.swipeVertical(trackX, fromY, toY);
        }
    }

    @Override
    public void submitChildAgePicker() {
        driverTools.click(SUBMIT_BUTTON);
    }
}
