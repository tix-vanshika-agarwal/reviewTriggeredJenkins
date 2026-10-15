package com.tiket.pojo.unm;

import java.util.ArrayList;

public class GETDeleteCheckUserUNMApiResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data {
        public ArrayList<ChecklistItem> checkListItems;
        public String token;
    }

    public static class ChecklistItem {
        public String code;
        public String title;
        public String message;
        public String iconUrl;
        public boolean restrictDeletion;
    }
}
