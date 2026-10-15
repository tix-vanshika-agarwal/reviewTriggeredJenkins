package com.tiket.pojo.b2b.corporate;

public class PostCorporateCodeResponse {
    public boolean success;
    public Data data;
    public Object metadata;

    public static class Data {
        public int id;
        public int corporateID;
        public String corporateCode;
        public String productType;
        public String airlineCode;
        public boolean isActive;
        public String type;
        public String createdAt;
        public int createdBy;
        public String updatedAt;
        public int updatedBy;
        public String deletedAt;
        public Object deletedBy;
    }
}
