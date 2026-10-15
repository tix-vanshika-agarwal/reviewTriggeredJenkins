package com.tiket.pojo.order;

import java.util.ArrayList;

public class CompleteOrderListResponse {  public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;



    public static class Action{
        public String url;
        public String type;
        public String id;
    }

    public static class ActiveCards{
        public ArrayList<Card> cards;
    }

    public static class ActiveNotification{
        public String title;
        public String description;
        public String style;
        public Action action;
    }

    public static class Card{
        public String icon;
        public String title;
        public Content content;
        public OrderData orderData;
        public DetailAction detailAction;
        public Status status;
        public ArrayList<Tracker> trackers;
    }

    public static class Content{
        public String style;
        public ArrayList<Contents> contents;
    }

    public static class Contents{
        public ArrayList<Subcontent> subcontents;
    }

    public static class CrossSelling{
        public ArrayList<Data> data;
    }

    public class Data{
        public String title;
        public ArrayList<View> views;
        public String key;
        public String value;
    }

    public static class DetailAction{
        public String url;
        public String type;
        public String id;
    }

    public static class OrderData{
        public String orderId;
        public String orderHash;
        public String orderDetailId;
        public String orderType;
        public String subOrderType;
    }

    public class Root{
        public String code;
        public String message;
        public Data data;
        public String serverTime;
    }

    public static class Status{
        public String title;
        public String style;
    }

    public static class Subcontent{
        public String title;
    }

    public static class Tracker{
        public String key;
        public String value;
    }

    public static class View{
        public String id;
        public int version;
        public ActiveNotification activeNotification;
        public ActiveCards activeCards;
        public CrossSelling crossSelling;
    }


}