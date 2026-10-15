package com.tiket.pojo.b2b.corporate;

import java.util.ArrayList;

public class GetAllTravelPolicyApiResponse {
    public boolean success;
    public ArrayList<Datum> data;

    public static class Datum{
        public int id;
        public String name;
        public int corporateID;
        public Assignees assignees;
    }

    public static class Assignees{
        public ArrayList<Object> groups;
        public ArrayList<Object> departments;
        public int employeeCount;
    }
}
