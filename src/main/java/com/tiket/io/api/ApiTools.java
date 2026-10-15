package com.tiket.io.api;

import com.tiket.logging.ILogger;
import com.tiket.model.Exception.ApiChangeException;
import com.tiket.model.Exception.ApiDataException;
import com.tiket.model.Exception.ApiEnvironmentException;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public record ApiTools(ILogger mainLogger) {

    private static final Logger secondaryLogger = LogManager.getLogger(ApiTools.class);

    private void logCurl(String method, ApiRequest request) {
        StringBuilder curl = new StringBuilder("curl -X ");
        curl.append(method.toUpperCase()).append(" \\\n");

        // Check if this is a Grafana request
        boolean shouldMaskGrafanaToken = isGrafanaRequest(request);

        // Headers (one per line)
        if (request.getHeaderParams() != null) {
            request.getHeaderParams().forEach((k, v) -> {
                // Mask Authorization header ONLY for Grafana requests
                if (shouldMaskGrafanaToken && k.equalsIgnoreCase("Authorization")) {
                    String maskedValue = maskAuthorizationHeader(String.valueOf(v));
                    curl.append("  -H '").append(k).append(": ").append(maskedValue).append("' \\\n");
                } else {
                    curl.append("  -H '").append(k).append(": ").append(v).append("' \\\n");
                }
            });
        }

        // Body (only if method supports it)
        if (request.getBody() != null && !request.getBody().isBlank() &&
                !(method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("DELETE"))) {
            curl.append("  --data '").append(request.getBody().replace("'", "\\'")).append("' \\\n");
        }

        // Query parameters
        StringBuilder uriBuilder = new StringBuilder(request.getUri());
        if (request.getQueryParams() != null && !request.getQueryParams().isEmpty()) {
            uriBuilder.append("?");
            request.getQueryParams().forEach((k, v) ->
                    uriBuilder.append(k).append("=").append(v).append("&")
            );
            uriBuilder.setLength(uriBuilder.length() - 1); // trim trailing &
        }

        curl.append("  '").append(uriBuilder).append("'");

        mainLogger.log("[curl]\n" + curl);
    }

    /**
     * Checks if the request is to Grafana based on configured host and port
     */
    private boolean isGrafanaRequest(ApiRequest request) {
        if (request.getUri() == null) {
            return false;
        }
        
        try {
            String grafanaHost = com.tiket.testbase.Configs.CONFIG.getProperty("grafanaProd");
            String grafanaPort = com.tiket.testbase.Configs.CONFIG.getProperty("grafanaPortProd");
            
            if (grafanaHost == null || grafanaPort == null) {
                return false;
            }
            
            // Check if URI contains both Grafana host and port
            String uri = request.getUri();
            return uri.contains(grafanaHost) && uri.contains(":" + grafanaPort);
            
        } catch (Exception e) {
            // If config reading fails, don't mask
            return false;
        }
    }

    /**
     * Masks Authorization header value (shows first 4 and last 4 chars only)
     */
    private String maskAuthorizationHeader(String authValue) {
        if (authValue == null || authValue.isEmpty()) {
            return "***MASKED***";
        }
        
        if (authValue.startsWith("Bearer ")) {
            String token = authValue.substring(7);
            if (token.length() <= 8) {
                return "Bearer ***MASKED***";
            }
            return "Bearer " + token.substring(0, 4) + "..." + token.substring(token.length() - 4);
        }
        
        // For non-Bearer tokens
        if (authValue.length() <= 8) {
            return "***MASKED***";
        }
        return authValue.substring(0, 4) + "..." + authValue.substring(authValue.length() - 4);
    }


    @SneakyThrows
    public ApiResponse get(ApiRequest request) {

        mainLogger.log("Hitting GET request with, uri: " + request.getUri());
        secondaryLogger.debug("Hitting GET request with: " + request);

        if (request.getUri() == null) {
            mainLogger.fail("Malformed uri");
            throw new ApiDataException("Malformed uri");
        }

        logCurl("GET", request);

        RequestSpecification requestSpecification = init(request);
        if (request.getUri().isBlank()) {
            return peek(from(requestSpecification.get()));
        } else {
            return peek(from(requestSpecification.get(request.getUri())));
        }
    }

    @SneakyThrows
    public byte[] getBytes(ApiRequest request) {

        mainLogger.log("Hitting GET request for binary data with, uri: " + request.getUri());
        secondaryLogger.debug("Hitting GET request for binary data with: " + request);

        if (request.getUri() == null) {
            mainLogger.fail("Malformed uri");
            throw new ApiDataException("Malformed uri");
        }

        logCurl("GET", request);

        RequestSpecification requestSpecification = init(request);
        Response response;
        if (request.getUri().isBlank()) {
            response = requestSpecification.get();
        } else {
            response = requestSpecification.get(request.getUri());
        }

        int statusCode = response.statusCode();
        if (statusCode == 404) {
            throw new ApiDataException("Data not found: 404");
        } else if (statusCode >= 400 && statusCode < 500) {
            throw new ApiChangeException("Got status code: " + statusCode);
        } else if (statusCode < 200 || statusCode > 300) {
            throw new ApiEnvironmentException("Got status code: " + statusCode);
        }

        return response.asByteArray();
    }

    public ApiResponse post(ApiRequest request) {

        mainLogger.log("Hitting POST request with, uri: " + request.getUri());
        secondaryLogger.debug("Hitting POST request with: " + request);

        if (request.getUri() == null) {
            mainLogger.fail("Malformed uri");
            throw new RuntimeException("Malformed uri");
        }

        logCurl("POST", request);

        RequestSpecification requestSpecification = init(request);
        if (request.getBody() != null && !request.getBody().isBlank()) {
            requestSpecification.body(request.getBody());
        }

        if (request.getMultiParts() != null && !request.getMultiParts().isEmpty()) {
            for (Map.Entry<String, Object> entry : request.getMultiParts().entrySet()) {
                requestSpecification.multiPart(entry.getKey(), entry.getValue());
            }
        }

        if (request.getUri() != null && request.getUri().isBlank()) {
            return peek(from(requestSpecification.post()));
        } else {
            return peek(from(requestSpecification.post(request.getUri())));
        }
    }

    public ApiResponse put(ApiRequest request) {

        mainLogger.log("Hitting PUT request with, uri: " + request.getUri());
        secondaryLogger.debug("Hitting PUT request with: " + request);

        if (request.getUri() == null) {
            mainLogger.fail("Malformed uri");
            throw new RuntimeException("Malformed uri");
        }

        logCurl("PUT", request);

        RequestSpecification requestSpecification = init(request);
        if (request.getBody() != null && !request.getBody().isBlank()) {
            requestSpecification.body(request.getBody());
        }

        if (request.getMultiParts() != null && !request.getMultiParts().isEmpty()) {
            for (Map.Entry<String, Object> entry : request.getMultiParts().entrySet()) {
                requestSpecification.multiPart(entry.getKey(), entry.getValue());
            }
        }

        if (request.getUri() != null && request.getUri().isBlank()) {
            return peek(from(requestSpecification.put()));
        } else {
            return peek(from(requestSpecification.put(request.getUri())));
        }
    }

    public ApiResponse delete(ApiRequest request) {

        mainLogger.log("Hitting DELETE request with, uri: " + request.getUri());
        secondaryLogger.debug("Hitting DELETE request with: " + request);

        if (request.getUri() == null) {
            mainLogger.fail("Malformed uri");
            throw new RuntimeException("Malformed uri");
        }

        logCurl("DELETE", request);

        RequestSpecification requestSpecification = init(request);
        if (request.getBody() != null && !request.getBody().isBlank()) {
            requestSpecification.body(request.getBody());
        }

        if (request.getMultiParts() != null && !request.getMultiParts().isEmpty()) {
            for (Map.Entry<String, Object> entry : request.getMultiParts().entrySet()) {
                requestSpecification.multiPart(entry.getKey(), entry.getValue());
            }
        }

        if (request.getUri() != null && request.getUri().isBlank()) {
            return peek(from(requestSpecification.delete()));
        } else {
            return peek(from(requestSpecification.delete(request.getUri())));
        }
    }

    public ApiResponse patch(ApiRequest request) {

        mainLogger.log("Hitting PATCH request with, uri: " + request.getUri());
        secondaryLogger.debug("Hitting PATCH request with: " + request);

        if (request.getUri() == null) {
            mainLogger.fail("Malformed uri");
            throw new RuntimeException("Malformed uri");
        }

        logCurl("PATCH", request);

        RequestSpecification requestSpecification = init(request);
        if (request.getBody() != null && !request.getBody().isBlank()) {
            requestSpecification.body(request.getBody());
        }

        if (request.getMultiParts() != null && !request.getMultiParts().isEmpty()) {
            for (Map.Entry<String, Object> entry : request.getMultiParts().entrySet()) {
                requestSpecification.multiPart(entry.getKey(), entry.getValue());
            }
        }

        if (request.getUri() != null && request.getUri().isBlank()) {
            return peek(from(requestSpecification.patch()));
        } else {
            return peek(from(requestSpecification.patch(request.getUri())));
        }
    }

    private ApiResponse from(Response response) {
        ApiResponse.Builder builder = new ApiResponse.Builder();
        builder.setStatusCode(response.statusCode());
        builder.setContentType(response.contentType());

        Map<String, ArrayList<Object>> headerParams = new HashMap<>();
        for (Header header : response.getHeaders()) {
            ArrayList<Object> list;
            if(headerParams.get(header.getName())==null){
                list = new ArrayList<>();
            }else {
                list = headerParams.get(header.getName());
            }
            list.add(header.getValue());
            headerParams.put(header.getName(),list);
        }
        builder.setHeaderParams(headerParams);

        builder.setBody(response.getBody().asString());
        builder.setTimeTakenInSeconds(response.getTimeIn(TimeUnit.SECONDS));

        return builder.build();
    }

    private ApiResponse peek(ApiResponse apiResponse) {

        secondaryLogger.debug("Got response: " + apiResponse);

        int statusCode = apiResponse.getStatusCode();
        if(statusCode >= 500) {
            throw new ApiEnvironmentException("Got status code: " + statusCode);
        } else if (statusCode >= 400 && statusCode != 404) {
            throw new ApiChangeException("Got status code: " + statusCode);
        } else if (statusCode == 404) {
            throw new ApiDataException("Data not found: 404");
        } else if (statusCode < 200  || statusCode > 300) {
            throw new ApiEnvironmentException("Got status code: " + statusCode);
        }

        return apiResponse;
    }

    private RequestSpecification auth(IAuth auth) {
        if (auth == null) {
            return RestAssured.given();
        }
        return auth.auth();
    }

    private RequestSpecification init(ApiRequest request) {

        assert request != null;

        RequestSpecification requestSpecification = auth(request.getAuth());

        if (request.getHeaderParams() != null && !request.getHeaderParams().isEmpty()) {
            requestSpecification.headers(request.getHeaderParams());
        }

        if (request.getPathParams() != null && !request.getPathParams().isEmpty()) {
            requestSpecification.pathParams(request.getPathParams());
        }

        if (request.getFormParams() != null && !request.getFormParams().isEmpty()) {
            requestSpecification.formParams(request.getFormParams());
        }

        if (request.getQueryParams() != null && !request.getQueryParams().isEmpty()) {
            requestSpecification.queryParams(request.getQueryParams());
        }

        if (request.getContentType() != null) {
            requestSpecification.contentType(request.getContentType().getContentType());
        }

        return requestSpecification;
    }
}
