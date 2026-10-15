package com.tiket.pojo.loyalty;

import java.util.ArrayList;
import java.util.List;

/**
 * Response of the mission list endpoint {@code GET /tix-mission/api/mission-list}.
 *
 * <p>This is the payload the consumer Mission Center page ({@code /id-sg/mission}) renders, split into
 * the tabs it shows: {@code joined} ("Your Progress"), {@code available} ("Other Missions") and
 * {@code past} ("History"). Missions appear within a tab in the sequence configured on the Mission
 * Center admin panel, filtered to those that are live and that the caller is segmented into. Only the
 * fields the automation needs are mapped; Gson ignores the rest.
 */
public class MissionListResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data {
        /** Page-level Mission List banner; independent of the per-mission sequence. */
        public Banner banner;
        public Tabs tabs;
    }

    public static class Banner {
        public ImageUrl imageUrl;
    }

    public static class ImageUrl {
        public String mobile;
        public String desktop;
    }

    public static class Tabs {
        /** Missions the caller has already joined, i.e. the "Your Progress" tab. */
        public List<Mission> joined;
        /** Live missions the caller has not joined yet, i.e. the "Other Missions" tab. */
        public List<Mission> available;
        /** Finished missions, i.e. the "History" tab. */
        public List<Mission> past;
    }

    public static class Mission {
        public String missionId;
        public String slug;
        public String missionName;
        /** e.g. {@code "ACTIVE"}. */
        public String missionStatus;
        /** e.g. {@code "INACTIVE"} when the caller has not joined. */
        public String userStatus;
    }

    /**
     * @return the slugs of the missions in the {@code available} tab, in the order the Mission List Page
     *         presents them. Empty when the tab is absent or has no missions.
     */
    public List<String> getAvailableSlugs() {
        List<String> slugs = new ArrayList<>();
        if (data == null || data.tabs == null || data.tabs.available == null) {
            return slugs;
        }
        for (Mission mission : data.tabs.available) {
            slugs.add(mission.slug);
        }
        return slugs;
    }
}
