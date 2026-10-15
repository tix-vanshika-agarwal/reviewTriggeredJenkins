package com.tiket.pojo.b2b.corporate;

public class PostUploadFileResponse {
    public boolean success;
    public String code;
    public Data data;
    public Object metadata; // Use appropriate type if metadata is not always null

    public static class Data {
        public String id;
        public String fileHash;
        public int size;
        public String fileName;
        public String type;
        public String fileType;
        public String fileURL;
        public String notes;
    }
}

