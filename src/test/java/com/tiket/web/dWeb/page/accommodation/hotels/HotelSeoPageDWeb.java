package com.tiket.web.dWeb.page.accommodation.hotels;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

/**
 * dWeb implementation of {@link HotelSeoPage}.
 *
 * <p>Currently inherits the full implementation from {@link HotelSeoPage}.
 * Override methods here when dWeb behaviour needs to differ from the shared
 * base.</p>
 */
public class HotelSeoPageDWeb<T extends WebDriver> extends HotelSeoPage<T> {

    public HotelSeoPageDWeb(Session<T> session) {
        super(session);
    }
}
