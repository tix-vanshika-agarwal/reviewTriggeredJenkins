package com.tiket.pojo.loyalty;

import java.util.List;

/**
 * Response of the mission detail endpoint {@code GET /tix-mission/api/mission/{slug}/detail}.
 *
 * <p>This is the same payload the mission microsite reads to render a user's progress, so it is the
 * source of truth for a mission's live status. Only the fields needed by the automation are mapped;
 * Gson ignores the rest. The convenience accessors ({@link #getRewardStatus()}, {@link #getCtaType()},
 * {@link #isRewardClaimable()}, …) are null-safe so callers can poll without manual null checks.
 */
public class MissionDetailResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data {
        public Mission mission;
        public Cta cta;
        public Object popup;
    }

    public static class Mission {
        public String slug;
        public String name;
        public int totalActiveProgress;
        public String progressState;
        public boolean multiEnroll;
        public Reward reward;
        public List<UserProgress> userProgress;
        public GbvProgress gbvProgress;
        public long claimPeriodsEndAt;
        public long endAt;
        public long startAt;
    }

    /**
     * Spend-based (Gross Booking Value) progress block, present only for GBV missions. Tracks the
     * eligible amount spent so far against the target ({@code totalAmount}).
     */
    public static class GbvProgress {
        /** Target spend required to complete the mission, e.g. {@code 1000000}. */
        public long totalAmount;
        /** Eligible amount spent so far, e.g. {@code 563546}. */
        public long totalSpend;
        /** Remaining spend to complete the mission, e.g. {@code 436454}. */
        public long remaining;
    }

    public static class Reward {
        /** e.g. {@code "POINT"}, {@code "GV"} (gift voucher). */
        public String type;
        /** e.g. {@code "ACTIVE"}, {@code "COMPLETED"}, {@code "CLAIMED"}. */
        public String status;
        public long amount;
        public String productLabel;
        public String rewardTitle;
    }

    public static class UserProgress {
        /** e.g. {@code "COMPLETE"}, {@code "INCOMPLETE"}. */
        public String status;
    }

    public static class Cta {
        /** e.g. {@code "CLAIM_REWARD"}, {@code "JOIN"}, {@code "SEARCH"}. */
        public String type;
        public String title;
        public String link;
    }

    /** @return the reward status (e.g. {@code "COMPLETED"}), or {@code null} if not present. */
    public String getRewardStatus() {
        return (data != null && data.mission != null && data.mission.reward != null)
                ? data.mission.reward.status : null;
    }

    /** @return the reward type (e.g. {@code "POINT"}), or {@code null} if not present. */
    public String getRewardType() {
        return (data != null && data.mission != null && data.mission.reward != null)
                ? data.mission.reward.type : null;
    }

    /** @return the reward amount (e.g. {@code 250000}), or {@code 0} if not present. */
    public long getRewardAmount() {
        return (data != null && data.mission != null && data.mission.reward != null)
                ? data.mission.reward.amount : 0;
    }

    /** @return the primary CTA type (e.g. {@code "CLAIM_REWARD"}), or {@code null} if not present. */
    public String getCtaType() {
        return (data != null && data.cta != null) ? data.cta.type : null;
    }

    /**
     * @return the accumulated GBV (Gross Booking Value) spend the mission has credited to the user
     *         (i.e. {@code gbvProgress.totalSpend}, e.g. {@code 563546}). {@code 0} if the user has no
     *         GBV progress yet or this is not a GBV mission.
     */
    public long getGbvSpend() {
        return (data != null && data.mission != null && data.mission.gbvProgress != null)
                ? data.mission.gbvProgress.totalSpend : 0;
    }

    /**
     * @return the GBV target spend required to complete the mission (i.e. {@code gbvProgress.totalAmount},
     *         e.g. {@code 1000000}). {@code 0} if this is not a GBV mission.
     */
    public long getGbvTotalAmount() {
        return (data != null && data.mission != null && data.mission.gbvProgress != null)
                ? data.mission.gbvProgress.totalAmount : 0;
    }

    /**
     * @return the total number of stamps the mission requires, i.e. the number of stamp slots in
     *         {@code userProgress} (each slot is one stamp, COMPLETE or not). {@code 0} if unavailable.
     */
    public int getTotalStampCount() {
        return (data != null && data.mission != null && data.mission.userProgress != null)
                ? data.mission.userProgress.size() : 0;
    }

    /** @return the number of stamps the user has collected (userProgress entries with COMPLETE status). */
    public int getCompletedStampCount() {
        if (data == null || data.mission == null || data.mission.userProgress == null) {
            return 0;
        }
        int completed = 0;
        for (UserProgress p : data.mission.userProgress) {
            if ("COMPLETE".equalsIgnoreCase(p.status)) {
                completed++;
            }
        }
        return completed;
    }

    /**
     * @return {@code true} if every stamp slot is COMPLETE (collected == required and at least one stamp
     *         exists), i.e. the mission's progress is fully done.
     */
    public boolean areAllStampsComplete() {
        int total = getTotalStampCount();
        return total > 0 && getCompletedStampCount() == total;
    }

    /**
     * @return {@code true} if the mission is complete and its reward is ready to be claimed, i.e. the
     *         primary CTA is {@code CLAIM_REWARD} or the reward status is {@code COMPLETED}.
     */
    public boolean isRewardClaimable() {
        return "CLAIM_REWARD".equalsIgnoreCase(getCtaType()) || "COMPLETED".equalsIgnoreCase(getRewardStatus());
    }

    /** @return {@code true} if the reward status matches {@code expectedStatus} (case-insensitive). */
    public boolean hasRewardStatus(String expectedStatus) {
        return expectedStatus != null && expectedStatus.equalsIgnoreCase(getRewardStatus());
    }
}
