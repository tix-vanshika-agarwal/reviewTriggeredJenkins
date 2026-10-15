package com.tiket.pojo.ttd;

import java.util.ArrayList;
public class TTDPopularDestinationDataPOJO {
    public ArrayList<DestinationData> data;
    public String message;

    public class DestinationData {
        public String destinationType;
        public String code;
        public ArrayList<String> longLat;
        public ArrayList<Translation> translations;
    }
    public class Translation {
        public String name;
    }
}
