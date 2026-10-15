package com.tiket.pojo.accommodation.hotel.payathotel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * One row of the {@code === PAH Hotel Summary ===} table.
 * Returned by {@code HotelApi#findPayAtHotelRoom(...)} so downstream steps can, for example,
 * open the PDP URL, book the first available hotel, or assert vendor-by-vendor coverage.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class PayAtHotelRoom {

    /** Vendor identifier from testData.json (e.g. {@code AGODA_V2}). */
    private String vendor;

    /** Vendor-specific external id (e.g. Agoda hotel id). */
    private String externalId;

    /** Public hotel id from Core BE ({@code content.publicId}) — matches the URL slug suffix. */
    private String publicId;

    /** Hotel display name from Core BE ({@code content.name}). */
    private String hotelName;

    /** Country id from Core BE ({@code content.location.country.id}, e.g. {@code japan}). */
    private String countryId;

    /**
     * Accommodation segment used when building the PDP path: {@code hotel} by default,
     * {@code homes} when {@code content.hotelChain.accommodationType} is non-hotel.
     */
    private String accommodationType;

    /** Earliest {@code yyyy-MM-dd} within the requested range where PAH was available. */
    private String earliestPahDate;

    /** Fully-built tiket PDP URL (en-sg) for the {@code earliestPahDate}. */
    private String url;

    /** Convenience — the composite label used in the report table, e.g. {@code AGODA_V2 (5198645)}. */
    public String vendorExternalIdLabel() {
        return vendor + " (" + externalId + ")";
    }
}
