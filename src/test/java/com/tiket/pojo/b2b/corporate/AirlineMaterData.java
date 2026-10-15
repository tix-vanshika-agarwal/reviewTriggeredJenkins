package com.tiket.pojo.b2b.corporate;

import java.util.List;

public class AirlineMaterData {
    public boolean success;
    public List<Data> data;

    public static class Data {
        public int id;
        public String code;
        public String name;
        public String displayName;
        public boolean corporateCodeEnabled;
        public boolean chaEnabled;
    }
}
