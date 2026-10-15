package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.testbase.Configs;
import com.tiket.model.Environment;
import com.tiket.api.accommodation.hotel.supply.GETMMDashboardMenuApi;
import com.tiket.api.accommodation.hotel.supply.GetAllRoomGroupingsByHotelIdApi;
import com.tiket.api.accommodation.hotel.supply.GetHotelByVendorApi;
import com.tiket.api.accommodation.hotel.supply.GetHotelRecommendationImageApi;
import com.tiket.api.accommodation.hotel.supply.GetRoomGroupingRoomsApi;
import com.tiket.api.accommodation.hotel.supply.ResetImageQualityApi;
import com.tiket.api.accommodation.hotel.supply.HotelApprovalApprovedApi;
import com.tiket.api.accommodation.hotel.supply.MoveRoomGroupExistingApi;
import com.tiket.api.accommodation.hotel.supply.UnmapRoomApi;
import com.tiket.api.auth.AdminDashboardLoginResponseParser;
import com.tiket.api.auth.POSTAdminLoginApi;
import com.tiket.api.auth.POSTAdminLoginMfaApplyApi;
import com.tiket.api.auth.POSTAdminLoginMfaGenerateApi;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.accommodation.hotel.HotelApprovalApprovedResponse;
import com.tiket.pojo.accommodation.hotel.supply.AllRoomGroupingsByHotelIdResponse;
import com.tiket.pojo.accommodation.hotel.supply.HotelByVendorResponse;
import com.tiket.pojo.accommodation.hotel.supply.HotelRecommendationImageResponse;
import com.tiket.pojo.accommodation.hotel.supply.RoomGroupingRoomsResponse;
import com.tiket.pojo.auth.AdminLoginMfaGenerateResponse;
import com.tiket.pojo.auth.AdminLoginResponse;
import com.tiket.pojo.extranet.login.ExtranetAdminLoginMfaApplyResponse;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MmDashboardApi<T extends WebDriver> extends BasePage<T> {

    /** Static grouping key that represents the "Others" (ungrouped) section. */
    public static final String OTHERS_GROUPING_KEY = "1604904d6463f09702be4638fe024c55d5a54627";

    public MmDashboardApi(Session<T> session) {
        super(session);
    }

    /**
     * Logs in to the MM dashboard member and returns reusable session data (Bearer token + cookies)
     * for subsequent dashboard API calls.
     */
    @SneakyThrows
    public DashboardMemberSession loginDashboardMember(String email, String password) {
        POSTAdminLoginApi.Inputs loginInputs = new POSTAdminLoginApi.Inputs(email, password, "INTERNAL", null);
        ApiResponse loginResponse = new POSTAdminLoginApi(loginInputs).hitApi(apiTools);
        logger.log("Dashboard member login API response: " + loginResponse.getBody());

        Gson gson = new Gson();
        AdminLoginResponse adminLoginResponse = gson.fromJson(loginResponse.getBody(), AdminLoginResponse.class);
        if (adminLoginResponse != null && adminLoginResponse.isMfaRequired()) {
            return completeDashboardMemberMfaLogin(adminLoginResponse);
        }

        if (adminLoginResponse != null && adminLoginResponse.isSuccess()
                && adminLoginResponse.data != null
                && adminLoginResponse.data.accessToken != null
                && !adminLoginResponse.data.accessToken.isBlank()) {
            logger.log("Dashboard member access token retrieved from login API");
            return buildDashboardMemberSession(loginResponse, adminLoginResponse);
        }

        throw new RuntimeException("Failed to login to MM dashboard member: " + loginResponse.getBody());
    }

    /** {@code image_raw} ids of every hotel photo (include + exclude lists) that is still "Not scored yet" (quality == null). */
    @SneakyThrows
    public Set<String> getUnscoredImageIds(String hotelId) {
        ApiResponse response = new GetHotelRecommendationImageApi(new GetHotelRecommendationImageApi.Inputs(hotelId)).hitApi(apiTools);
        HotelRecommendationImageResponse parsed = new Gson().fromJson(response.getBody(), HotelRecommendationImageResponse.class);
        if (parsed == null || !"SUCCESS".equalsIgnoreCase(parsed.code) || parsed.data == null) {
            throw new RuntimeException("getUnscoredImageIds: unexpected response for hotelId=" + hotelId + " -> " + response.getBody());
        }
        Set<String> ids = new LinkedHashSet<>();
        for (List<HotelRecommendationImageResponse.Image> list : List.of(
                parsed.data.imageIncludeList == null ? List.<HotelRecommendationImageResponse.Image>of() : parsed.data.imageIncludeList,
                parsed.data.imageExcludeList == null ? List.<HotelRecommendationImageResponse.Image>of() : parsed.data.imageExcludeList)) {
            for (HotelRecommendationImageResponse.Image image : list) {
                if (image.id != null && (image.quality == null || image.quality.isJsonNull())) {
                    ids.add(image.id);
                }
            }
        }
        logger.log("getUnscoredImageIds: hotelId=" + hotelId + " unscored=" + ids.size());
        return ids;
    }

    /**
     * Puts one image back to "Not scored yet" by setting quality = null on its {@code image_raw} document.
     * Returns true only when the call really succeeded (HTTP 2xx and, if the body carries a {@code code}, it is SUCCESS);
     * otherwise logs a warning with the status and body and returns false. Never throws, so cleanup can't mask a test failure.
     */
    public boolean resetImageQuality(String imageRawId) {
        try {
            ApiResponse response = new ResetImageQualityApi(new ResetImageQualityApi.Inputs(imageRawId)).hitApi(apiTools);
            int status = response.getStatusCode();
            String body = response.getBody();
            boolean httpOk = status >= 200 && status < 300;
            String code = null;
            try {
                com.google.gson.JsonObject json = new Gson().fromJson(body, com.google.gson.JsonObject.class);
                if (json != null && json.has("code") && !json.get("code").isJsonNull()) {
                    code = json.get("code").getAsString();
                }
            } catch (Exception ignored) {
                // body is not a JSON object: judge by HTTP status only
            }
            boolean codeOk = code == null || "SUCCESS".equalsIgnoreCase(code);
            if (httpOk && codeOk) {
                logger.log("resetImageQuality: imageRawId=" + imageRawId + " reset OK (status " + status + ")");
                return true;
            }
            logger.log("WARNING resetImageQuality FAILED: imageRawId=" + imageRawId + " status=" + status + " code=" + code + " body=" + body);
            return false;
        } catch (Exception e) {
            logger.log("WARNING resetImageQuality FAILED: imageRawId=" + imageRawId + " -> " + e);
            return false;
        }
    }

    /**
     * Cleanup helper: puts back to "Not scored yet" every photo that was unscored in {@code unscoredBefore}
     * but is not any more. Best effort and never throws, so it cannot hide the real test failure when used in a
     * {@code finally} block; every problem is logged as a WARNING. Returns true only if nothing failed.
     */
    public boolean restoreUnscoredImages(String hotelId, Set<String> unscoredBefore) {
        if (Configs.ENVIRONMENT != Environment.PREPROD) {
            logger.log("WARNING restoreUnscoredImages skipped: the reset endpoint only targets preprod, environment=" + Configs.ENVIRONMENT);
            return false;
        }
        try {
            Set<String> unscoredAfter = getUnscoredImageIds(hotelId);
            List<String> toReset = new ArrayList<>();
            for (String imageId : unscoredBefore) {
                if (!unscoredAfter.contains(imageId)) {
                    toReset.add(imageId);
                }
            }
            logger.log(String.format("restoreUnscoredImages: hotelId=%s unscored before=%d, after=%d, photos to reset=%s", hotelId, unscoredBefore.size(), unscoredAfter.size(), toReset));
            int failed = 0;
            for (String imageId : toReset) {
                if (!resetImageQuality(imageId)) {
                    failed++;
                }
            }
            if (failed > 0) {
                logger.log("WARNING restoreUnscoredImages: " + failed + " of " + toReset.size() + " reset(s) failed for hotelId=" + hotelId);
            } else {
                logger.log(toReset.isEmpty() ? "restoreUnscoredImages: nothing to reset" : "restoreUnscoredImages: reset done");
            }
            return failed == 0;
        } catch (Exception e) {
            logger.log("WARNING restoreUnscoredImages skipped for hotelId=" + hotelId + ": " + e);
            return false;
        }
    }

    /** Resolves the {@code hotelId} UUID from a {@code (vendorName, vendorExternalId)} pair. Throws if not found. */
    @SneakyThrows
    public String getHotelIdByVendor(String vendorName, String vendorExternalId) {
        if (vendorName == null || vendorName.isBlank() || vendorExternalId == null || vendorExternalId.isBlank()) {
            throw new RuntimeException("getHotelIdByVendor: vendorName and vendorExternalId must both be non-blank");
        }
        GetHotelByVendorApi.Inputs inputs = new GetHotelByVendorApi.Inputs(vendorName, vendorExternalId);
        ApiResponse response = new GetHotelByVendorApi(inputs).hitApi(apiTools);
        Gson gson = new Gson();
        HotelByVendorResponse parsed = gson.fromJson(response.getBody(), HotelByVendorResponse.class);
        if (parsed == null || !"SUCCESS".equalsIgnoreCase(parsed.code)) {
            throw new RuntimeException("getHotelIdByVendor: unexpected response for vendorName=" + vendorName
                    + ", vendorExternalId=" + vendorExternalId + " -> " + response.getBody());
        }
        if (parsed.data == null || parsed.data.content == null || parsed.data.content.isEmpty()) {
            throw new RuntimeException("getHotelIdByVendor: no hotel found for vendorName=" + vendorName
                    + ", vendorExternalId=" + vendorExternalId);
        }
        HotelByVendorResponse.Content first = parsed.data.content.get(0);
        if (first.hotelId == null || first.hotelId.isBlank()) {
            throw new RuntimeException("getHotelIdByVendor: matched hotel has blank hotelId for vendorName=" + vendorName
                    + ", vendorExternalId=" + vendorExternalId);
        }
        logger.log("getHotelIdByVendor: resolved hotelId=" + first.hotelId
                + " (name='" + first.name + "') for vendorName=" + vendorName
                + ", vendorExternalId=" + vendorExternalId);
        return first.hotelId;
    }

    /**
     * Fetches the approved native-dashboard hotel id whose name matches {@code hotelName}.
     * Requires an active {@link DashboardMemberSession} from {@link #loginDashboardMember(String, String)}.
     */
    @SneakyThrows
    public int getApprovedHotelId(DashboardMemberSession session, String hotelName) {
        HotelApprovalApprovedResponse response = fetchApprovedHotels(session, hotelName);
        return findHotelIdFromApprovedHotels(response, hotelName);
    }

    /**
     * Best-effort cleanup that unmaps the given rooms back to the "Others" section so the Manage Room
     * Group cases stay repeatable. Resolves each room's current {@code groupingKey} from the funnel
     * rooms API, batches the roomIds per grouping key, and calls the unmap-room API for each batch.
     * Never throws — cleanup failures are logged so they don't mask the actual test result.
     *
     * @param hotelId        hotel UUID taken from the Manage Room Group page URL
     * @param vendorName     vendor filter (e.g. HOTELBEDS, WEBBEDS) — same vendor used on the page
     * @param vendorHotelId  vendor hotel id filter (the vendorExternalId used on the page)
     * @param roomIds        room ids (the {@code id} shown on the page / used as checkbox values)
     */
    public void unmapRoomsToOthers(String hotelId, String vendorName, String vendorHotelId, Collection<String> roomIds) {
        if (roomIds == null || roomIds.isEmpty()) {
            logger.log("No rooms to unmap back to Others; skipping cleanup");
            return;
        }
        try {
            Map<String, String> roomIdToGroupingKey = fetchMappedRoomGroupingKeys(vendorName, vendorHotelId);

            Map<String, List<String>> roomIdsByGroupingKey = new LinkedHashMap<>();
            for (String roomId : roomIds) {
                String groupingKey = roomIdToGroupingKey.get(roomId);
                if (groupingKey == null) {
                    logger.log("Skip unmap; no grouping key found (already unmapped?) for roomId=" + roomId);
                    continue;
                }
                roomIdsByGroupingKey.computeIfAbsent(groupingKey, k -> new ArrayList<>()).add(roomId);
            }

            for (Map.Entry<String, List<String>> entry : roomIdsByGroupingKey.entrySet()) {
                try {
                    UnmapRoomApi.Inputs inputs = new UnmapRoomApi.Inputs(hotelId, entry.getKey(), entry.getValue());
                    ApiResponse response = new UnmapRoomApi(inputs).hitApi(apiTools);
                    logger.log("Unmapped rooms " + entry.getValue() + " from groupingKey " + entry.getKey()
                            + " -> status " + response.getStatusCode());
                } catch (Exception e) {
                    logger.log("Failed to unmap rooms " + entry.getValue() + " from groupingKey "
                            + entry.getKey() + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.log("Unmap-to-Others cleanup failed: " + e.getMessage());
        }
    }


    public void moveRoomsBackToOriginalGroups(String cookie, String hotelId, String vendorName,
                                              String vendorHotelId, Map<String, String> roomToOriginalGroupingKey) {
        if (roomToOriginalGroupingKey == null || roomToOriginalGroupingKey.isEmpty()) {
            logger.log("No rooms to move back to their original group; skipping cleanup");
            return;
        }
        try {
            Map<String, String> currentGroupingKeys = fetchMappedRoomGroupingKeys(vendorName, vendorHotelId);

            // Batch by target (original) grouping key; each room carries its current group as oldGroupingKey.
            Map<String, List<MoveRoomGroupExistingApi.Room>> roomsByTargetGroupingKey = new LinkedHashMap<>();
            for (Map.Entry<String, String> entry : roomToOriginalGroupingKey.entrySet()) {
                String roomId = entry.getKey();
                String originalGroupingKey = entry.getValue();
                // A room not present in the mapped list is currently unmapped (i.e. in "Others").
                String currentGroupingKey = currentGroupingKeys.getOrDefault(roomId, OTHERS_GROUPING_KEY);
                if (originalGroupingKey == null || originalGroupingKey.equals(currentGroupingKey)) {
                    logger.log("Skip revert; room " + roomId + " already in its original group (" + currentGroupingKey + ")");
                    continue;
                }
                roomsByTargetGroupingKey
                        .computeIfAbsent(originalGroupingKey, k -> new ArrayList<>())
                        .add(new MoveRoomGroupExistingApi.Room(roomId, currentGroupingKey));
            }

            for (Map.Entry<String, List<MoveRoomGroupExistingApi.Room>> entry : roomsByTargetGroupingKey.entrySet()) {
                try {
                    MoveRoomGroupExistingApi.Inputs inputs = new MoveRoomGroupExistingApi.Inputs(
                            cookie, hotelId, entry.getKey(), entry.getValue());
                    ApiResponse response = new MoveRoomGroupExistingApi(inputs).hitApi(apiTools);
                    logger.log("Moved rooms " + entry.getValue() + " back to groupingKey " + entry.getKey()
                            + " -> status " + response.getStatusCode());
                } catch (Exception e) {
                    logger.log("Failed to move rooms " + entry.getValue() + " back to groupingKey "
                            + entry.getKey() + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.log("Move-back-to-original cleanup failed: " + e.getMessage());
        }
    }


    public Map<String, String> fetchMappedRoomGroupingKeys(String vendorName, String vendorHotelId) {
        Map<String, String> roomIdToGroupingKey = new LinkedHashMap<>();
        Gson gson = new Gson();
        int page = 0;
        int totalPages = 1;
        do {
            GetRoomGroupingRoomsApi.Inputs inputs = new GetRoomGroupingRoomsApi.Inputs(
                    vendorName, vendorHotelId, null, true, page, 100);
            ApiResponse response = new GetRoomGroupingRoomsApi(inputs).hitApi(apiTools);
            RoomGroupingRoomsResponse parsed = gson.fromJson(response.getBody(), RoomGroupingRoomsResponse.class);
            if (parsed == null || parsed.data == null || parsed.data.content == null) {
                break;
            }
            for (RoomGroupingRoomsResponse.Content content : parsed.data.content) {
                if (content.id != null && content.masterRoomGroup != null && content.masterRoomGroup.groupingKey != null) {
                    roomIdToGroupingKey.put(content.id, content.masterRoomGroup.groupingKey);
                }
            }
            totalPages = parsed.data.totalPages;
            page++;
        } while (page < totalPages);
        logger.log("Fetched grouping keys for " + roomIdToGroupingKey.size() + " mapped rooms of "
                + vendorName + " / " + vendorHotelId);
        return roomIdToGroupingKey;
    }

    /**
     * Returns the display name of the first Room Group on {@code hotelId} with the given
     * source {@code tag} (case-insensitive), {@code totalRoom &ge; 1}, and no verified rooms.
     * Empty string when nothing matches or the API call fails.
     */
    public String findFirstRoomGroupNameWithTag(String hotelId, String targetTag) {
        if (hotelId == null || hotelId.isBlank() || targetTag == null || targetTag.isBlank()) {
            logger.log("findFirstRoomGroupNameWithTag: blank hotelId or targetTag; returning empty");
            return "";
        }
        try {
            GetAllRoomGroupingsByHotelIdApi.Inputs inputs = new GetAllRoomGroupingsByHotelIdApi.Inputs(hotelId);
            ApiResponse response = new GetAllRoomGroupingsByHotelIdApi(inputs).hitApi(apiTools);
            Gson gson = new Gson();
            AllRoomGroupingsByHotelIdResponse parsed = gson.fromJson(
                    response.getBody(), AllRoomGroupingsByHotelIdResponse.class);
            if (parsed == null || parsed.data == null || parsed.data.roomGroupings == null) {
                logger.log("findFirstRoomGroupNameWithTag: empty response for hotelId=" + hotelId);
                return "";
            }
            for (AllRoomGroupingsByHotelIdResponse.RoomGrouping rg : parsed.data.roomGroupings) {
                if (rg.tag == null || !rg.tag.equalsIgnoreCase(targetTag)) continue;
                if (rg.totalRoom < 1) continue;
                if (rg.totalRoomVerified > 0) continue;
                String name = resolveRoomGroupDisplayName(rg);
                if (name.isBlank()) continue;
                logger.log("findFirstRoomGroupNameWithTag: discovered name='" + name
                        + "', id=" + rg.id + ", tag=" + rg.tag
                        + ", totalRoom=" + rg.totalRoom
                        + ", totalRoomVerified=" + rg.totalRoomVerified);
                return name;
            }
            logger.log("findFirstRoomGroupNameWithTag: no clean RG on hotelId=" + hotelId
                    + " matches tag=" + targetTag
                    + " (need totalRoom>=1 AND totalRoomVerified==0)");
            return "";
        } catch (Exception e) {
            logger.log("findFirstRoomGroupNameWithTag: API call failed: " + e.getMessage());
            return "";
        }
    }

    /**
     * Returns the id of the Room Group on {@code hotelId} whose display name matches {@code roomGroupName}
     * (case-insensitive), or empty string. Capture this BEFORE an action that may rename the group, then read the
     * tag with {@link #getRoomGroupTagById} so the check does not depend on the display name staying the same.
     */
    public String getRoomGroupIdByName(String hotelId, String roomGroupName) {
        AllRoomGroupingsByHotelIdResponse parsed = fetchRoomGroupings(hotelId);
        if (parsed == null || roomGroupName == null) return "";
        for (AllRoomGroupingsByHotelIdResponse.RoomGrouping rg : parsed.data.roomGroupings) {
            if (resolveRoomGroupDisplayName(rg).equalsIgnoreCase(roomGroupName)) {
                return rg.id != null ? rg.id : "";
            }
        }
        return "";
    }

    /**
     * Returns the raw source {@code tag} of a Room Group, looked up by id when {@code roomGroupId} is known (so the
     * result does not depend on the display name), otherwise by display name. Empty string when nothing matches.
     */
    public String getRoomGroupTag(String hotelId, String roomGroupId, String roomGroupName) {
        return roomGroupId == null || roomGroupId.isBlank()
                ? getRoomGroupTagByName(hotelId, roomGroupName)
                : getRoomGroupTagById(hotelId, roomGroupId);
    }

    /** Returns the raw source {@code tag} of the Room Group with the given id, or empty string. */
    public String getRoomGroupTagById(String hotelId, String roomGroupId) {
        AllRoomGroupingsByHotelIdResponse parsed = fetchRoomGroupings(hotelId);
        if (parsed == null || roomGroupId == null || roomGroupId.isBlank()) return "";
        for (AllRoomGroupingsByHotelIdResponse.RoomGrouping rg : parsed.data.roomGroupings) {
            if (roomGroupId.equals(rg.id)) {
                String tag = rg.tag != null ? rg.tag : "";
                logger.log("getRoomGroupTagById: id=" + rg.id + ", name='" + resolveRoomGroupDisplayName(rg)
                        + "', tag='" + tag + "'");
                return tag;
            }
        }
        logger.log("getRoomGroupTagById: no RG with id=" + roomGroupId + " on hotelId=" + hotelId);
        return "";
    }

    private AllRoomGroupingsByHotelIdResponse fetchRoomGroupings(String hotelId) {
        if (hotelId == null || hotelId.isBlank()) return null;
        try {
            GetAllRoomGroupingsByHotelIdApi.Inputs inputs = new GetAllRoomGroupingsByHotelIdApi.Inputs(hotelId);
            ApiResponse response = new GetAllRoomGroupingsByHotelIdApi(inputs).hitApi(apiTools);
            AllRoomGroupingsByHotelIdResponse parsed = new Gson().fromJson(
                    response.getBody(), AllRoomGroupingsByHotelIdResponse.class);
            if (parsed == null || parsed.data == null || parsed.data.roomGroupings == null) {
                logger.log("fetchRoomGroupings: empty response for hotelId=" + hotelId);
                return null;
            }
            return parsed;
        } catch (Exception e) {
            logger.log("fetchRoomGroupings: API call failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns the raw BE-canonical lowercase source {@code tag} of the Room Group on
     * {@code hotelId} whose display name matches {@code roomGroupName} (case-insensitive).
     * Empty string when nothing matches or the API call fails.
     */
    public String getRoomGroupTagByName(String hotelId, String roomGroupName) {
        if (hotelId == null || hotelId.isBlank() || roomGroupName == null || roomGroupName.isBlank()) {
            logger.log("getRoomGroupTagByName: blank hotelId or roomGroupName; returning empty");
            return "";
        }
        AllRoomGroupingsByHotelIdResponse parsed = fetchRoomGroupings(hotelId);
        if (parsed == null) return "";
        for (AllRoomGroupingsByHotelIdResponse.RoomGrouping rg : parsed.data.roomGroupings) {
            String name = resolveRoomGroupDisplayName(rg);
            if (!name.equalsIgnoreCase(roomGroupName)) continue;
            String tag = rg.tag != null ? rg.tag : "";
            logger.log("getRoomGroupTagByName: name='" + name + "', tag='" + tag + "', id=" + rg.id);
            return tag;
        }
        StringBuilder known = new StringBuilder();
        for (AllRoomGroupingsByHotelIdResponse.RoomGrouping rg : parsed.data.roomGroupings) {
            known.append("[id=").append(rg.id).append(", name='").append(resolveRoomGroupDisplayName(rg))
                    .append("', tag=").append(rg.tag).append("] ");
        }
        logger.log("getRoomGroupTagByName: no RG named '" + roomGroupName
                + "' on hotelId=" + hotelId + ". Known RGs: " + known);
        return "";
    }

    /**
     * Returns the display name of the first Room Group on {@code hotelId} with
     * {@code totalRoom == 0}. Empty string when nothing matches or the API call fails.
     */
    public String findFirstEmptyRoomGroupName(String hotelId) {
        if (hotelId == null || hotelId.isBlank()) {
            logger.log("findFirstEmptyRoomGroupName: blank hotelId; returning empty");
            return "";
        }
        try {
            GetAllRoomGroupingsByHotelIdApi.Inputs inputs = new GetAllRoomGroupingsByHotelIdApi.Inputs(hotelId);
            ApiResponse response = new GetAllRoomGroupingsByHotelIdApi(inputs).hitApi(apiTools);
            Gson gson = new Gson();
            AllRoomGroupingsByHotelIdResponse parsed = gson.fromJson(
                    response.getBody(), AllRoomGroupingsByHotelIdResponse.class);
            if (parsed == null || parsed.data == null || parsed.data.roomGroupings == null) {
                logger.log("findFirstEmptyRoomGroupName: empty response for hotelId=" + hotelId);
                return "";
            }
            for (AllRoomGroupingsByHotelIdResponse.RoomGrouping rg : parsed.data.roomGroupings) {
                if (rg.totalRoom != 0) continue;
                String name = resolveRoomGroupDisplayName(rg);
                if (name.isBlank()) continue;
                logger.log("findFirstEmptyRoomGroupName: discovered name='" + name
                        + "', id=" + rg.id + ", tag=" + rg.tag
                        + ", totalRoom=" + rg.totalRoom);
                return name;
            }
            logger.log("findFirstEmptyRoomGroupName: no RG on hotelId=" + hotelId + " has totalRoom==0");
            return "";
        } catch (Exception e) {
            logger.log("findFirstEmptyRoomGroupName: API call failed: " + e.getMessage());
            return "";
        }
    }

    /** customGroupingName is a plain string on some groups and a localised object {en,id} on others. */
    private static String customGroupingNameText(com.google.gson.JsonElement el) {
        if (el == null || el.isJsonNull()) return "";
        if (el.isJsonPrimitive()) return el.getAsString();
        if (el.isJsonObject()) {
            com.google.gson.JsonObject o = el.getAsJsonObject();
            for (String key : new String[]{"en", "id"}) {
                if (o.has(key) && o.get(key).isJsonPrimitive() && !o.get(key).getAsString().isBlank()) {
                    return o.get(key).getAsString();
                }
            }
        }
        return "";
    }

    private static String resolveRoomGroupDisplayName(AllRoomGroupingsByHotelIdResponse.RoomGrouping rg) {
        String custom = customGroupingNameText(rg.customGroupingName);
        if (!custom.isBlank()) {
            return custom;
        }
        return rg.groupingName != null && rg.groupingName.en != null ? rg.groupingName.en : "";
    }

    public record DashboardMemberSession(
            String bearerToken,
            String cookie,
            long accountId,
            String email,
            String firstName,
            String lastName) {

        public String tixGatewayAuthorization() {
            String sessionAccessToken = parseCookieValue(cookie, "session_access_token");
            if (sessionAccessToken != null && !sessionAccessToken.isBlank()) {
                return toBearerToken(sessionAccessToken);
            }
            String aat = parseCookieValue(cookie, "aat");
            if (aat != null && !aat.isBlank()) {
                return toBearerToken(aat);
            }
            return bearerToken;
        }

        private static String parseCookieValue(String cookieHeader, String name) {
            if (cookieHeader == null || cookieHeader.isBlank()) {
                return null;
            }
            String prefix = name + "=";
            for (String part : cookieHeader.split(";")) {
                String trimmed = part.trim();
                if (trimmed.startsWith(prefix)) {
                    return trimmed.substring(prefix.length());
                }
            }
            return null;
        }

        private static String toBearerToken(String accessToken) {
            return accessToken.startsWith("Bearer ") ? accessToken : "Bearer " + accessToken;
        }
    }

    private DashboardMemberSession completeDashboardMemberMfaLogin(AdminLoginResponse loginResponse) {
        long accountId = loginResponse.data.accountId != null ? loginResponse.data.accountId : 0L;
        String mfaMethod = loginResponse.data.methodOptions != null && loginResponse.data.methodOptions.length > 0
                ? loginResponse.data.methodOptions[0]
                : "EMAIL";

        POSTAdminLoginMfaGenerateApi.Inputs generateInputs = new POSTAdminLoginMfaGenerateApi.Inputs(
                loginResponse.data.mfaTrxId, mfaMethod, null, null, accountId);
        ApiResponse generateResponse = new POSTAdminLoginMfaGenerateApi(generateInputs).hitApi(apiTools);

        Gson gson = new Gson();
        AdminLoginMfaGenerateResponse mfaGenerate = gson.fromJson(generateResponse.getBody(), AdminLoginMfaGenerateResponse.class);
        if (mfaGenerate == null || mfaGenerate.status == null
                || !"SUCCESS".equalsIgnoreCase(mfaGenerate.status.message)) {
            throw new RuntimeException("Failed to generate dashboard member MFA OTP: " + generateResponse.getBody());
        }

        POSTAdminLoginMfaApplyApi.Inputs applyInputs = new POSTAdminLoginMfaApplyApi.Inputs(
                loginResponse.data.mfaTrxId,
                mfaMethod,
                mfaGenerate.data.otpTrxId,
                "1234",
                null,
                null,
                accountId);
        ApiResponse applyResponse = new POSTAdminLoginMfaApplyApi(applyInputs).hitApi(apiTools);

        ExtranetAdminLoginMfaApplyResponse mfaApply = gson.fromJson(applyResponse.getBody(), ExtranetAdminLoginMfaApplyResponse.class);
        if (mfaApply == null || !mfaApply.isSuccess() || mfaApply.data == null
                || mfaApply.data.accessToken == null || mfaApply.data.accessToken.isBlank()) {
            throw new RuntimeException("Failed to apply dashboard member MFA OTP: " + applyResponse.getBody());
        }
        logger.log("Dashboard member access token retrieved from MFA apply API");
        return buildDashboardMemberSession(applyResponse, mfaApply);
    }

    private DashboardMemberSession buildDashboardMemberSession(ApiResponse loginResponse, AdminLoginResponse adminLogin) {
        return buildDashboardMemberSession(
                loginResponse,
                adminLogin.data.accessToken,
                adminLogin.data.refreshToken,
                adminLogin.data.accountId != null ? adminLogin.data.accountId : 0L,
                defaultIfBlank(adminLogin.data.accountUsername, adminLogin.data.email),
                adminLogin.data.accountFirstName,
                adminLogin.data.accountLastName);
    }

    private DashboardMemberSession buildDashboardMemberSession(ApiResponse loginResponse, ExtranetAdminLoginMfaApplyResponse mfaApply) {
        return buildDashboardMemberSession(
                loginResponse,
                mfaApply.data.accessToken,
                mfaApply.data.refreshToken,
                mfaApply.data.accountId != null ? mfaApply.data.accountId : 0L,
                mfaApply.data.accountUsername,
                mfaApply.data.accountFirstName,
                mfaApply.data.accountLastName);
    }

    private DashboardMemberSession buildDashboardMemberSession(
            ApiResponse loginResponse,
            String accessToken,
            String refreshToken,
            long accountId,
            String email,
            String firstName,
            String lastName) {
        String bearerToken = toBearerToken(accessToken);
        String aat = defaultIfBlank(AdminDashboardLoginResponseParser.resolveCookieValueOrNull(loginResponse, "aat"), accessToken);
        String art = defaultIfBlank(AdminDashboardLoginResponseParser.resolveCookieValueOrNull(loginResponse, "art"), refreshToken);
        String sessionAccessToken = AdminDashboardLoginResponseParser.resolveCookieValueOrNull(loginResponse, "session_access_token");
        String sessionRefreshToken = AdminDashboardLoginResponseParser.resolveCookieValueOrNull(loginResponse, "session_refresh_token");

        if (sessionAccessToken == null || sessionAccessToken.isBlank()) {
            ApiResponse menuResponse = fetchDashboardMenuResponse(aat, art);
            sessionAccessToken = AdminDashboardLoginResponseParser.resolveSessionTokenOrNull(menuResponse);
            if (sessionRefreshToken == null || sessionRefreshToken.isBlank()) {
                sessionRefreshToken = AdminDashboardLoginResponseParser.resolveCookieValueOrNull(
                        menuResponse, "session_refresh_token");
            }
        }

        return new DashboardMemberSession(
                bearerToken,
                buildDashboardCookie(aat, art, sessionAccessToken, sessionRefreshToken),
                accountId,
                email,
                firstName,
                lastName);
    }

    private ApiResponse fetchDashboardMenuResponse(String aat, String art) {
        GETMMDashboardMenuApi.Inputs menuInputs = new GETMMDashboardMenuApi.Inputs(
                toBearerToken(aat), aat, defaultIfBlank(art, ""));
        ApiResponse menuResponse = new GETMMDashboardMenuApi(menuInputs).hitApi(apiTools);
        logger.log("MM dashboard menu API response status: " + menuResponse.getStatusCode());
        return menuResponse;
    }

    private static String buildDashboardCookie(String aat, String art, String sessionAccessToken, String sessionRefreshToken) {
        StringBuilder cookie = new StringBuilder();
        appendCookie(cookie, "aat", aat);
        appendCookie(cookie, "art", art);
        appendCookie(cookie, "session_access_token", sessionAccessToken);
        appendCookie(cookie, "session_refresh_token", sessionRefreshToken);
        return cookie.toString();
    }

    private static void appendCookie(StringBuilder cookie, String name, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        if (cookie.length() > 0) {
            cookie.append("; ");
        }
        cookie.append(name).append('=').append(value);
    }

    private static String defaultIfBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private static String toBearerToken(String accessToken) {
        return accessToken.startsWith("Bearer ") ? accessToken : "Bearer " + accessToken;
    }

    private HotelApprovalApprovedResponse fetchApprovedHotels(DashboardMemberSession session, String searchHotelName) {
        HotelApprovalApprovedApi.Inputs inputs = new HotelApprovalApprovedApi.Inputs(
                session.bearerToken(), searchHotelName, session.cookie());
        HotelApprovalApprovedApi api = new HotelApprovalApprovedApi(inputs);
        ApiResponse apiResponse = api.hitApi(apiTools);
        logger.log("Hotel Approval Approved API response: " + apiResponse.getBody());

        Gson gson = new Gson();
        HotelApprovalApprovedResponse response = gson.fromJson(apiResponse.getBody(), HotelApprovalApprovedResponse.class);
        if (response == null || !"SUCCESS".equalsIgnoreCase(response.code)) {
            throw new RuntimeException("Failed to fetch approved hotels: " + apiResponse.getBody());
        }
        return response;
    }

    private int findHotelIdFromApprovedHotels(HotelApprovalApprovedResponse response, String hotelName) {
        if (response.data == null || response.data.content == null || response.data.content.isEmpty()) {
            throw new RuntimeException("No approved hotel found for name: " + hotelName);
        }
        for (HotelApprovalApprovedResponse.Content content : response.data.content) {
            if (content.hotel == null || content.hotel.hotelGeneralInformation == null) {
                continue;
            }
            String approvedHotelName = content.hotel.hotelGeneralInformation.name;
            if (approvedHotelName != null && approvedHotelName.equalsIgnoreCase(hotelName)) {
                logger.log("Found approved hotel id " + content.hotel.id + " for name: " + approvedHotelName);
                return content.hotel.id;
            }
        }
        throw new RuntimeException("Hotel id not found for name: " + hotelName);
    }
}
