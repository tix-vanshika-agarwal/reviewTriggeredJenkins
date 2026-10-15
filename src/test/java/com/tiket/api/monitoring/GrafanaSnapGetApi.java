package com.tiket.api.monitoring;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record GrafanaSnapGetApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String dashboardId,
            String verticalName,
            int orgId,
            int panelId,
            String from,
            String to,
            String timezone,
            int width,
            int height,
            String theme
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        // Get Grafana configuration from properties
        String grafanaProd = Configs.CONFIG.getProperty("grafanaProd");
        String grafanaPortProd = Configs.CONFIG.getProperty("grafanaPortProd");
        String grafanaToken = Configs.CREDENTIALS.getProperty("grafana.token");

        // Build the base URL
        String baseUrl = grafanaProd + ":" + grafanaPortProd;

        // Build headers with Bearer token. Accept: image/png signals to Grafana that we want
        // the rendered PNG (and not the SPA HTML) so an unauthenticated/unrendered response
        // is more likely to surface as a 4xx instead of a 200 with HTML body.
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + grafanaToken);
        headers.put("Accept", "image/png");

        // Build query parameters
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("orgId", inputs.orgId);
        queryParams.put("panelId", inputs.panelId);
        queryParams.put("from", inputs.from);
        queryParams.put("to", inputs.to);
        queryParams.put("tz", inputs.timezone);
        queryParams.put("width", inputs.width);
        queryParams.put("height", inputs.height);
        if (inputs.theme != null && !inputs.theme.isBlank()) {
            queryParams.put("theme", inputs.theme);
        }

        // Build the endpoint path
        String endpoint = String.format("/render/d-solo/%s/%s", inputs.dashboardId, inputs.verticalName);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(baseUrl + endpoint)
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }

    /**
     * Get binary response (image bytes) from Grafana API
     * 
     * @param apiTools ApiTools instance for making the request
     * @return byte array containing the PNG image data
     */
    @SneakyThrows
    public byte[] getBinaryResponse(ApiTools apiTools) {

        // Get Grafana configuration from properties
        String grafanaProd = Configs.CONFIG.getProperty("grafanaProd");
        String grafanaPortProd = Configs.CONFIG.getProperty("grafanaPortProd");
        String grafanaToken = Configs.CREDENTIALS.getProperty("grafana.token");

        // Build the base URL
        String baseUrl = grafanaProd + ":" + grafanaPortProd;

        // Build headers with Bearer token + Accept: image/png to encourage Grafana to return
        // an actual PNG (and 4xx on auth/rendering failure) instead of falling back to the SPA HTML.
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + grafanaToken);
        headers.put("Accept", "image/png");

        // Build query parameters
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("orgId", inputs.orgId);
        queryParams.put("panelId", inputs.panelId);
        queryParams.put("from", inputs.from);
        queryParams.put("to", inputs.to);
        queryParams.put("tz", inputs.timezone);
        queryParams.put("width", inputs.width);
        queryParams.put("height", inputs.height);
        if (inputs.theme != null && !inputs.theme.isBlank()) {
            queryParams.put("theme", inputs.theme);
        }

        // Build the endpoint path
        String endpoint = String.format("/render/d-solo/%s/%s", inputs.dashboardId, inputs.verticalName);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(baseUrl + endpoint)
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.getBytes(request);
    }
}

