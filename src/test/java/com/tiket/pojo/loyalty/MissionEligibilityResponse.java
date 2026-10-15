package com.tiket.pojo.loyalty;

/**
 * Response of the mission eligibility endpoint {@code GET /tix-mission/api/mission-eligibility/{missionId}}.
 *
 * <p>Only the fields needed by the automation are mapped; Gson ignores the rest. The accessors are
 * null-safe so callers can poll without manual null checks.
 */
public class MissionEligibilityResponse {

    /** {@code data.user.userStatus} of a user inside the mission's segment who has not joined it yet. */
    public static final String USER_STATUS_INACTIVE = "INACTIVE";

    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data {
        public Mission mission;
        public User user;
    }

    public static class Mission {
        public String missionId;
        public String missionSlug;
        public String missionName;
        /** e.g. {@code "ACTIVE"}. */
        public String missionStatus;
        /** e.g. {@code "ORDER_ID"}. */
        public String missionType;
        /** Number of stamps required to complete the mission, e.g. {@code 15}. */
        public int targetValue;
    }

    public static class User {
        /** e.g. {@code "NOT_ELIGIBLE"} (outside the segment), {@code "INACTIVE"} (eligible, not joined). */
        public String userStatus;
        /** Stamps the user has collected so far. */
        public int currentProgress;
    }

    /** @return the user's status for the mission, or {@code null} if not present. */
    public String getUserStatus() {
        return (data != null && data.user != null) ? data.user.userStatus : null;
    }

    /** @return the user's collected stamp count, or {@code 0} if not present. */
    public int getCurrentProgress() {
        return (data != null && data.user != null) ? data.user.currentProgress : 0;
    }
}
