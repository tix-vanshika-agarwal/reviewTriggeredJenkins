package com.tiket.pojo.b2b.corporate;

import java.util.List;

public class GetConfigSettingResponse {
    public boolean success;
    public List<Data> data;
    public Object metadata;

    public static class Data {
        public String configName;
        public boolean configValue;
    }
}
