package com.tiket.pojo.ttd;

import lombok.Getter;

import java.util.List;

@Getter
public class SupplierId {
    private String code;
    private String message;
    private Object errors;
    private List<Product> data;

    public void setCode(String code) { this.code = code; }

    public void setMessage(String message) { this.message = message; }

    public void setErrors(Object errors) { this.errors = errors; }

    public void setData(List<Product> data) { this.data = data; }

    // Nested Product class
    public static class Product {
        private String id;
        private List<Image> images;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public List<Image> getImages() { return images; }
        public void setImages(List<Image> images) { this.images = images; }
    }

    // Nested Image class
    public static class Image {
        private String urlSmall;
        private String urlMedium;
        private List<Translation> translations;
        private String supplierCode;
        private Boolean isHidden;
        private String videoUrl;

        public String getUrlSmall() { return urlSmall; }
        public void setUrlSmall(String urlSmall) { this.urlSmall = urlSmall; }

        public String getUrlMedium() { return urlMedium; }
        public void setUrlMedium(String urlMedium) { this.urlMedium = urlMedium; }

        public List<Translation> getTranslations() { return translations; }
        public void setTranslations(List<Translation> translations) { this.translations = translations; }

        public String getSupplierCode() { return supplierCode; }
        public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }

        public Boolean getIsHidden() { return isHidden; }
        public void setIsHidden(Boolean isHidden) { this.isHidden = isHidden; }

        public String getVideoUrl() { return videoUrl; }
        public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    }

    // Nested Translation class
    public static class Translation {
        private String language;
        private String alt;
        private String description;

        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }

        public String getAlt() { return alt; }
        public void setAlt(String alt) { this.alt = alt; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
