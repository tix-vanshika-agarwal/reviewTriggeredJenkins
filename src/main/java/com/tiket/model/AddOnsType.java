package com.tiket.model;

import org.apache.commons.lang3.NotImplementedException;

public enum AddOnsType {
    ITEM, ROOM, NAME, SINGLE_PRICE, TOTAL_PRICE, TOTAL_ROOM_TYPE, TOTAL_ADDONS_ITEM, CATEGORY;

    public static AddOnsType parse(String addOnsType) {
        switch (addOnsType.toLowerCase()) {
            case "item" -> {
                return ITEM;
            }
            case "room" -> {
                return ROOM;
            }
            case "name" -> {
                return NAME;
            }
            case "single_price" -> {
                return SINGLE_PRICE;
            }
            case "total_price" -> {
                return TOTAL_PRICE;
            }
            case "total_room_type" -> {
                return TOTAL_ROOM_TYPE;
            }
            case "total_addons_item" -> {
                return TOTAL_ADDONS_ITEM;
            }
            case "category" -> {
                return CATEGORY;
            }
            default -> throw new NotImplementedException("Given addOnsType: %s is not supported", addOnsType);
        }
    }
}
