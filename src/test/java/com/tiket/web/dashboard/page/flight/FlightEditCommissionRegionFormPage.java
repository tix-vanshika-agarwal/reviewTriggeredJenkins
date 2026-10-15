package com.tiket.web.dashboard.page.flight;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * The edit form renders the same fields as the add form, so only the clearing of
 * the pre-filled values is new.
 */
public class FlightEditCommissionRegionFormPage<T extends WebDriver> extends FlightAddCommissionRegionFormPage<T> {
    public FlightEditCommissionRegionFormPage(Session<T> session) {
        super(session);
    }

    public Map<String, String> updateDetails(String updatedName, String updatedCountries) {
        Map<String, String> updatedCommissionRegion = new HashMap<>();

        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 10);

        // The form renders empty and is populated once the region has been fetched.
        // Anything typed before that lands is silently overwritten, so wait for the
        // pre-filled country - every region has at least one - before editing.
        driverTools.waitForElementToBeClickable(NAME_FIELDS, 5).orElseThrow();
        driverTools.waitForElementToAppear(COUNTRY_SELECTED_VALUE, 10).orElseThrow();

        clearName();
        updatedName = fillName(updatedName);
        updatedCommissionRegion.put("name", updatedName);

        clearCountries();
        updatedCountries = chooseCountries(updatedCountries);
        updatedCommissionRegion.put("countries", updatedCountries);

        return updatedCommissionRegion;
    }

    private void clearName() {
        driverTools.waitForElementToAppear(NAME_FIELDS, 5).orElseThrow();
        driverTools.clear(NAME_FIELDS);
    }

    /** 'Clear all' is only rendered while at least one country is selected. */
    private void clearCountries() {
        if (driverTools.waitForElementToAppear(COUNTRY_CLEAR_ALL, 5).isEmpty()) {
            return;
        }
        driverTools.click(COUNTRY_CLEAR_ALL);
        
        if (!driverTools.waitForElementToDisappear(COUNTRY_SELECTED_VALUE, 5)) {
            throw new IllegalStateException("Failed to clear selected countries");
        }
    }
}
