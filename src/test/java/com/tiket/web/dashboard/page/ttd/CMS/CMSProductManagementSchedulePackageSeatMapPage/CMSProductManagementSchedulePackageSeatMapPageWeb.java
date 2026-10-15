package com.tiket.web.dashboard.page.ttd.CMS.CMSProductManagementSchedulePackageSeatMapPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.core.Is.is;

public class CMSProductManagementSchedulePackageSeatMapPageWeb<T extends WebDriver> extends WebPage<T> {
    public CMSProductManagementSchedulePackageSeatMapPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSProductManagementSchedulePackageSeatMapPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSProductManagementSchedulePackageSeatMapPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSProductManagementSchedulePackageSeatMapPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator SEATMAP_CONFIG = new Locator("SEATMAP_CONFIG", By.xpath("//div[@class='wrapper-seatmap-section']"));

    public boolean isSeatMapConfigLoaded() {
        return driverTools.waitForElementToAppear(SEATMAP_CONFIG).isPresent();
    }

    public boolean isSeatMapBooked(Map<String, String> seatMap) {
        Set<String> seatCode = seatMap.keySet();
        String chosenCategoryName = seatMap.get("seat0").split(":")[0];
        clickCategoryTab(chosenCategoryName);

        for (String e : seatCode) {
            String chosenSeatCode = seatMap.get(e).split(":")[1];
            Locator locator = getBookedSeatLocatorBySeatCode(chosenSeatCode);
            boolean isSeatMapBooked = driverTools.waitForElementsToAppear(locator).isPresent();
            Assertion.assertThat("Seat code on cat '"+chosenCategoryName+"' with seat code '"+chosenSeatCode+"' is booked", isSeatMapBooked, is(true));
        }

        return true;
    }

    private Locator getBookedSeatLocatorBySeatCode(String chosenSeatCode) {
        return new Locator("BOOKED_SEAT", By.xpath("//div[contains(@class,'seat-partner-booked')]/div[text()='"+chosenSeatCode+"']"));
    }

    private Locator getCategoryTabLocatorByCategoryName(String categoryName) {
        return new Locator("CATEGORY_TAB", By.xpath("//div[contains(@class,'tix-tab-btn v4')]//span[contains(@class,'tab-title') and text()='"+categoryName+"']"));
    }

    private void clickCategoryTab(String categoryName) {
        Locator locator = getCategoryTabLocatorByCategoryName(categoryName);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
    }
}
