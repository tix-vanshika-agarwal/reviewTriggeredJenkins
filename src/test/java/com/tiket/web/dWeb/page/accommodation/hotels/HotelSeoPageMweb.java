package com.tiket.web.dWeb.page.accommodation.hotels;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

/**
 * mWeb implementation of {@link HotelSeoPage}.
 *
 * <p>Currently inherits the full implementation from {@link HotelSeoPage}.
 * Override methods here when mWeb behaviour needs to differ from the shared
 * base.</p>
 */
public class HotelSeoPageMweb<T extends WebDriver> extends HotelSeoPage<T> {

    public HotelSeoPageMweb(Session<T> session) {
        super(session);
    }
}
