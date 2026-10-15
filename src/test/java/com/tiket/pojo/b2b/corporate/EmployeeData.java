package com.tiket.pojo.b2b.corporate;

public class EmployeeData {
    public boolean success;
    public Data data;

    public static class Data {
        public int corporateID;
        public int employeeID;
        public int accessRoleId;
    }
}
