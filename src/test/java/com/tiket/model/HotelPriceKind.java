package com.tiket.model;

/**
 * Which price line is displayed on hotel UI (crossed/strikethrough, final/main, or after-tax).
 * Used with {@link HotelPriceScope} and tax flag to select the correct rateInfo.price field.
 */
public enum HotelPriceKind {
    /** Crossed (strikethrough) price – baseRate* or totalBaseRate* */
    CROSSED,
    /** Final (main) price – rate* or totalRate* */
    FINAL,
    /** After-tax line – rateWithTax, or totalRateWithTax/totalRateWithoutTax when scope is TOTAL */
    AFTER_TAX
}
