package com.tiket.model.api;

import com.tiket.io.api.IAuth;

import java.util.Map;

import static com.tiket.core.CoreFunctions.shorten;

public class ApiRequest {

    private final HttpRestMethod httpRestMethod;
    private final IAuth auth;
    private final String baseUrl;
    private final RestContentType contentType;
    private final String uri;
    private final String body;
    private final Map<String, Object> queryParams;
    private final Map<String, Object> pathParams;
    private final Map<String, Object> headerParams;
    private final Map<String, Object> formParams;
    private final Map<String, Object> multiParts;

    private ApiRequest(Builder builder) {
        this.httpRestMethod = builder.httpRestMethod;
        this.auth = builder.auth;
        this.baseUrl = builder.baseUrl;
        this.contentType = builder.contentType;
        this.uri = builder.uri;
        this.body = builder.body;
        this.queryParams = builder.queryParams;
        this.pathParams = builder.pathParams;
        this.headerParams = builder.headerParams;
        this.formParams = builder.formParams;
        this.multiParts = builder.multiParts;
    }

    public HttpRestMethod getHttpRestMethod() {
        return httpRestMethod;
    }

    public IAuth getAuth() {
        return auth;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public RestContentType getContentType() {
        return contentType;
    }

    public String getUri() {
        return uri;
    }

    public String getBody() {
        return body;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public Map<String, Object> getPathParams() {
        return pathParams;
    }

    public Map<String, Object> getHeaderParams() {
        return headerParams;
    }

    public Map<String, Object> getFormParams() {
        return formParams;
    }

    public Map<String, Object> getMultiParts() {
        return multiParts;
    }

    @Override
    public String toString() {
        return "ApiRequest{" +
                "httpRestMethod=" + httpRestMethod +
                ", auth=" + auth +
                ", baseUrl='" + shorten(baseUrl) + '\'' +
                ", contentType=" + contentType +
                ", uri='" + uri + '\'' +
                ", body='" + body + '\'' +
                ", queryParams=" + queryParams +
                ", pathParams=" + pathParams +
                ", headerParams=" + headerParams +
                ", formParams=" + formParams +
                ", multiParts=" + multiParts +
                '}';
    }

    public static class Builder {

        private HttpRestMethod httpRestMethod;
        private IAuth auth;
        private String baseUrl;
        private RestContentType contentType;
        private String uri;
        private String body;
        private Map<String, Object> queryParams;
        private Map<String, Object> pathParams;
        private Map<String, Object> headerParams;
        private Map<String, Object> formParams;
        private Map<String, Object> multiParts;

        public ApiRequest build() {
            return new ApiRequest(this);
        }

        public Builder setHttpRestMethod(HttpRestMethod httpRestMethod) {
            this.httpRestMethod = httpRestMethod;
            return this;
        }

        public Builder setAuth(IAuth auth) {
            this.auth = auth;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setContentType(RestContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setUri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setQueryParams(Map<String, Object> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public Builder setPathParams(Map<String, Object> pathParams) {
            this.pathParams = pathParams;
            return this;
        }

        public Builder setHeaderParams(Map<String, Object> headerParams) {
            this.headerParams = headerParams;
            return this;
        }

        public Builder setFormParams(Map<String, Object> formParams) {
            this.formParams = formParams;
            return this;
        }

        public Builder setMultiParts(Map<String, Object> multiParts) {
            this.multiParts = multiParts;
            return this;
        }
    }
}
