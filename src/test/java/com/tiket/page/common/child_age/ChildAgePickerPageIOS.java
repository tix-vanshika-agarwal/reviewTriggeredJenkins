package com.tiket.page.common.child_age;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;

import io.appium.java_client.AppiumBy;

public class ChildAgePickerPageIOS  <T extends WebDriver> extends ChildAgePickerPage<T> {

    private static final int PICKER_MAX_STEPS = 20;
    private static final Locator AGE_CONTAINER = new Locator("AGE_CONTAINER", AppiumBy.name("child_age_container"));
    private static final Locator CHILD_AGE_DISPLAYED_1 = new Locator("CHILD_AGE_DISPLAYED", AppiumBy.xpath("(//*[@name='child_age_container']//XCUIElementTypeCell//XCUIElementTypeStaticText[@visible='true'])[1]"));
    private static final Locator CHILD_AGE_DISPLAYED_2 = new Locator("CHILD_AGE_DISPLAYED", AppiumBy.xpath("(//*[@name='child_age_container']//XCUIElementTypeCell//XCUIElementTypeStaticText[@visible='true'])[2]"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", AppiumBy.xpath("//*[@name='child_age_container']//following-sibling::XCUIElementTypeOther//XCUIElementTypeButton"));

    public ChildAgePickerPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void increment() {
        driverTools.moveElementVertical(CHILD_AGE_DISPLAYED_1, CHILD_AGE_DISPLAYED_2, 1.0);
    }

    @Override
    public void decrement() {
        driverTools.moveElementVertical(CHILD_AGE_DISPLAYED_2, CHILD_AGE_DISPLAYED_1, 1.0);
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

    private Locator getChildAgePickerByIndex(int index) {
        return new Locator("CHILD_AGE_PICKER_INDEX_" + index, AppiumBy.xpath("(//*[@name='childAgeLabel'])[" + index + "]"));
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
    public void setChildAgeByIndex(int index, String age) {
        int convertAge = age.equals("<1") ? 0 : Integer.parseInt(age);
        driverTools.click(getChildAgePickerByIndex(index));
        setPickerAgeTo(convertAge);
        driverTools.click(SUBMIT_BUTTON);
    }

    @Override
    public boolean checkSelectedChildAge(int total, List<String> childAges) {
        Integer selectedAgeText;
        for (int i = 1; i <= total; i++) {
            WebElement selectedAgeElement = driverTools.waitForElementToAppear(getChildAgePickerByIndex(i)).orElseThrow();
            String ageText = driverTools.getText(selectedAgeElement);
            String cleanedAgeText = ageText.replaceAll("[^0-9<]", "");
            if (cleanedAgeText.contains("<1")) {
                selectedAgeText = 0;
            } else {
                selectedAgeText = Integer.parseInt(cleanedAgeText);
            }
            Assertion.assertThat("selected child age should be correct on the index: " + i, selectedAgeText, is(Integer.parseInt(childAges.get(i-1).trim())));
        }
        return driverTools.isElementDisplayed(getChildAgePickerByIndex(1));
    }

    @Override
    public int getSelectedChildAgeFromLabel(int index) {
        WebElement selectedAgeElement = driverTools.waitForElementToAppear(getChildAgePickerByIndex(index)).orElseThrow();
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
        int ageDisplayed = getChildAgeDisplayed();
        for (int guard = 0; guard < PICKER_MAX_STEPS && ageDisplayed != targetAge; guard++) {
            if (targetAge > ageDisplayed) {
                increment();
            } else {
                decrement();
            }
            ageDisplayed = getChildAgeDisplayed();
        }
        if (ageDisplayed != targetAge) {
            throw new DriverToolsException("child age picker did not reach target age " + targetAge
                    + " (stuck at " + ageDisplayed + ") after " + PICKER_MAX_STEPS + " steps");
        }
    }

    @Override
    public void submitChildAgePicker() {
        driverTools.click(SUBMIT_BUTTON);
    }
}
