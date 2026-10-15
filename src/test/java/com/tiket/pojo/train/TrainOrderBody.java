package com.tiket.pojo.train;

import java.util.ArrayList;

public class TrainOrderBody {
    public Contact contact;
    public ArrayList<Passenger> passengers;
    public String cartId;
    public String secret;

    public static class Contact{
        public String areaCode;
        public String emailAddress;
        public String fullName;
        public String phone;
        public String profileId;
        public String title;
    }

    public static class Passenger{
        public String fullName;
        public String identityNumber;
        public String passportNumber;
        public String paxType;
        public String profileId;
        public String title;
        public String identityType;
    }
}
