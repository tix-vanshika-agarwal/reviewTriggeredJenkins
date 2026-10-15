package com.tiket.page.common.child_age;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class ChildAgePickerPage <T extends WebDriver> extends BasePage<T> {

    public ChildAgePickerPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> ChildAgePickerPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new ChildAgePickerPageAndroid<>(session);}
            case IOS -> {return new ChildAgePickerPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void increment();
    public abstract void decrement();
    public abstract void setChildAge(int total, int age);
    public abstract void setChildAgeByIndex(int index, String age);
    public abstract boolean checkSelectedChildAge(int total, List<String> childAges);

    /** Reads the age shown for a child on the Rooms & Guests bottom sheet ({@code <1} -> 0). */
    public abstract int getSelectedChildAgeFromLabel(int index);
    /** Opens the child age picker bottom sheet for the given child index. */
    public abstract void openChildAgePicker(int index);
    /** Returns the age currently highlighted/selected inside the open picker ({@code <1} -> 0). */
    public abstract int getPickerSelectedAge();
    /** Scrolls the open picker until the given age is highlighted (does not submit). */
    public abstract void setPickerAgeTo(int targetAge);
    /** Confirms the currently highlighted age and closes the picker. */
    public abstract void submitChildAgePicker();
}
