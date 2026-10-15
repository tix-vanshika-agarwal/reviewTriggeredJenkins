package com.tiket.pojo.b2b.corporate;

import java.util.List;

public class GetCustomFieldsData {
    public boolean success;
    public List<CustomFieldData> data;
    public Object metadata; // Use appropriate type if metadata is not always null

    public static class CustomFieldData {
        public int id;
        public int corporateID;
        public boolean enable;
        public String fieldName;
        public String fieldType;
        public String description;
        public List<String> options;
        public List<Validation> validations;
        public List<Config> configs;
    }

    public static class Validation {
        public String id;
        public String customFieldConfigID;
        public String name;
        public String type;
        public String value;
        public int enable;
        public ErrorMessage errorMessage;
    }

    public static class ErrorMessage {
        public String id;
        public String en;
    }

    public static class Config {
        public String id;
        public int customFieldID;
        public int required;
        public int enable;
        public String page;
        public int order;
    }
}
