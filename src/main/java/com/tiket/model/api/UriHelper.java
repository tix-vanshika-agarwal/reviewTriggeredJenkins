package com.tiket.model.api;

import com.tiket.model.Environment;
import com.tiket.testbase.Configs;

import java.util.Objects;

public class UriHelper {

    private final String baseGkUri, baseProdUri, basePreProdUri;
    private final StringBuilder queryParams;

    public UriHelper(String baseGkUri, String baseProdUri, String basePreProdUri) {
        Objects.requireNonNull(baseGkUri);
        Objects.requireNonNull(baseProdUri);
        Objects.requireNonNull(basePreProdUri);
        this.baseGkUri = baseGkUri;
        this.baseProdUri = baseProdUri;
        this.basePreProdUri = basePreProdUri;
        this.queryParams = new StringBuilder();
    }

    public UriHelper(String baseGkUri, String basePreProdUri) {
        Objects.requireNonNull(baseGkUri);
        Objects.requireNonNull(basePreProdUri);
        this.baseGkUri = baseGkUri;
        this.basePreProdUri = basePreProdUri;
        this.baseProdUri = basePreProdUri;
        this.queryParams = new StringBuilder();
    }

    public UriHelper(String baseUri) {
        Objects.requireNonNull(baseUri);
        this.baseGkUri = baseUri;
        this.baseProdUri = baseUri;
        this.basePreProdUri = baseUri;
        this.queryParams = new StringBuilder();
    }

    public String getUri() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            return baseGkUri;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            return basePreProdUri;
        } else {
            return baseProdUri;
        }
    }

    public String withEndpoint(String endpoint) {
        if (endpoint == null) {
            return getUri();
        }

        if (!endpoint.startsWith("/")) {
            endpoint = "/" + endpoint;
        }

        return getUri() + endpoint + (queryParams.length() > 0 ? "?" + queryParams.toString() : "");
    }

    public UriHelper addQueryParam(String key, String value) {
        if (queryParams.length() > 0) {
            queryParams.append("&");
        }
        queryParams.append(key).append("=").append(value);
        return this;
    }
}
