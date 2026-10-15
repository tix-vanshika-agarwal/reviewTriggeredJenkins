package com.tiket.pojo.b2b.corporate;

public class PostTravelPlanCreateResponse {
    public boolean success;
    public PostTravelPlanCreateData data;
    public Object metadata; // or make a class if needed

    public class PostTravelPlanCreateData {
        public int travelPlanID;
    }
}

