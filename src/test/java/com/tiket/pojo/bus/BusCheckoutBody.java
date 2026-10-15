package com.tiket.pojo.bus;

import java.util.ArrayList;

public class BusCheckoutBody {

    public Contact contact;
    public ArrayList<Passengers> passengers;
    public int totalPrice;
    public Schedules schedules;

    public static class Contact {
        public String title;
        public String fullName;
        public String phone;
        public String emailAddress;
        public String profileId;
    }

    public static class Passengers {
        public String paxType;
        public String title;
        public String fullName;
        public String dateOfBirth;
        public String profileId;
    }

    public static class Schedules {
        public Depart depart;
    }

    public static class Depart {
        public ArrayList<SelectedSeats> selectedSeats;
    }

    public static class SelectedSeats {
        public String name;
        public String passengerId;
    }
}
