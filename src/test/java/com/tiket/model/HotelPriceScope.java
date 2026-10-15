package com.tiket.model;

/**
 * Whether the screen shows per-room/night prices or total price.
 * PER_ROOM_NIGHT uses rateWithTax, baseRateWithTax, etc. TOTAL uses totalRateWithTax, totalBaseRateWithTax, etc.
 * <p>
 * Use PER_ROOM_NIGHT for: SRP, PDP, SO/MO Room List, Room Detail when wording is "/room/night".
 * Use TOTAL for: LP/SRP/PDP/SO Room List when wording is "Total price", and Booking Form footer "Total (after taxes)".
 * Exception: MO Room List always uses PER_ROOM_NIGHT even when user applied Total Price filter.
 */
public enum HotelPriceScope {
    /** Per room/night – use rateWithTax, rateWithoutTax, baseRateWithTax, baseRateWithoutTax */
    PER_ROOM_NIGHT,
    /** Total price – use totalRateWithTax, totalRateWithoutTax, totalBaseRateWithTax, totalBaseRateWithoutTax */
    TOTAL
}
