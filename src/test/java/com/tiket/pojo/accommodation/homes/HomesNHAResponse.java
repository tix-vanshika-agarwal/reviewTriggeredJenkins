package com.tiket.pojo.accommodation.homes;

import java.math.BigInteger;
import java.util.ArrayList;

public class HomesNHAResponse {
    public String code;
    public String message;
    public Data data;
    public Object errors;
    public BigInteger serverTime;

    public static class Data{
        public String type;
        public ArrayList<List> list;
    }

    public static class List{
        public String id;
        public String name;
        public String templateCode;
        public Content content;
        public String location;
        public String buttonText;
        public String redirectTarget;
        public Redirect redirect;
        public String title;
        public Image image;
        public String imageUrl;
        public String url;
        public Icon icon;
        public String subTitle;
    }

    public static class Content{
        public String title;
        public ArrayList<Module> modules;
        public String topColor;
        public String bottomColor;
        public String supergraphicImage;
        public String desktopSuperGraphic;
        public String subsidyLabel;
        public String icon;
        public boolean loginOnly;
        public long remainingTime;
        public ArrayList<List> list;
        public String subTitle;
        public String name;
        public ImageBackground imageBackground;
        public boolean showButton;
        public String buttonText;
        public String redirectTarget;
        public Redirect redirect;
    }

    public static class Country{
        public String id;
        public String name;
    }

    public static class Destination{
        public String type;
        public String publicId;
        public String name;
        public String location;
        public Country country;
        public String labelText;
    }

    public static class Icon{
        public String caption;
        public String url;
        public String mobileUrl;
    }

    public static class Image{
        public String caption;
        public String url;
        public String mobileUrl;
    }

    public static class ImageBackground{
        public String caption;
        public String url;
        public String mobileUrl;
    }

    public static class Module{
        public String name;
        public String title;
        public Image image;
    }

    public static class PopularDestination{
        public ArrayList<Destination> destinations;
    }

    public static class Redirect{
        public String location;
        public ArrayList<String> facilityGroups;
        public String priorityRanking;
        public ArrayList<String> accommodationType;
        public ArrayList<String> subsidyLabels;
        public Object hotelBrands;
        public ArrayList<Object> hotelChains;
        public Object maxPrice;
        public Object minPrice;
        public Object roomFacilityTags;
        public Object starRatings;
        public Object paymentOptions;
        public boolean tiketFlexi;
        public String locationType;
        public String groupFilterKeyword;
        public boolean loyaltyToggle;
        public Object reviewOptions;
        public Object benefits;
        public boolean preferredPartner;
        public PopularDestination popularDestination;
        public Object autocompleteLocation;
        public Object poi;
        public Object srpPropertyTypes;
        public Object recentPoiTypes;
        public Object recentAutocompleteLocation;
        public boolean showSearchBox;
        public String button;
    }
}
