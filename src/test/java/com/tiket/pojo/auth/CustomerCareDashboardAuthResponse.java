package com.tiket.pojo.auth;

public class CustomerCareDashboardAuthResponse {
    public Data data;

    public static class Data {
        public LoginUser loginUser;
    }

    public static class LoginUser {
        public String message;
    }
}
