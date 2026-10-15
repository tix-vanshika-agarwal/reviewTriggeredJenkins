package com.tiket.pojo.b2b.corporate;

import java.util.List;

public class GetCustomFieldsPurchaseOrderData {
    public boolean success;
    public List<CustomFieldData> data;
    public Object metadata;

    public static class CustomFieldData {
        public int id;
        public String fieldType;
        public String fieldName;
        public List<String> value; // for most fieldTypes
        public FileData file;      // for fieldType "file"
    }

    public static class FileData {
        public String id;
        public String name;
        public String urlPath;
        public Integer size;
        public String signedURL;
    }
}
