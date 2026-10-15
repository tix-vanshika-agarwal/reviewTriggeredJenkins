package com.tiket.model;

import org.apache.commons.lang3.NotImplementedException;

public enum SortItem {
    LATEST_REVIEW, MOST_HELPFUL, RATING_HIGH_TO_LOW, RATING_LOW_TO_HIGH, OLDEST_REVIEW, RANDOM;

    public static SortItem parse(String sortItem) {
        switch (sortItem.toLowerCase()) {
            case "latest_review" -> {
                return LATEST_REVIEW;
            }
            case "most_helpful" -> {
                return MOST_HELPFUL;
            }
            case "rating_high_to_low" -> {
                return RATING_HIGH_TO_LOW;
            }
            case "rating_low_to_high" -> {
                return RATING_LOW_TO_HIGH;
            }
            case "oldest_review" -> {
                return OLDEST_REVIEW;
            }
            case "random" -> {
                return RANDOM;
            }
            default -> throw new NotImplementedException("Given sortItem: %s is not supported", sortItem);
        }
    }
}
