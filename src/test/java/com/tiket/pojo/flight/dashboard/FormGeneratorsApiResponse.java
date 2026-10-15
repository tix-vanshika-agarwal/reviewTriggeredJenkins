package com.tiket.pojo.flight.dashboard;

import java.util.List;

public class FormGeneratorsApiResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data {
        public List<Content> content;
        public Pageable pageable;
        public String lastUpdatedBy;
        public int size;
        public boolean prev;
        public boolean next;
        public int totalPages;
        public boolean last;
        public int totalElements;
        public boolean first;
        public Sort sort;
        public int number;
        public int numberOfElements;
        public boolean empty;
    }

    public static class Content {
        public String id;
        public String airlineCode;
        public String supplier;
        public FormSectionType formSectionType;
        public String originCountry;
        public String destinationCountry;
        public boolean international;
    }

    public static class FormSectionType {
        public List<String> adults;
        public List<String> childs;
        public List<String> infants;
        public List<String> contact;
    }

    public static class Pageable {
        public Sort sort;
        public int pageNumber;
        public int pageSize;
        public int offset;
        public boolean paged;
        public boolean unpaged;
    }

    public static class Sort {
        public boolean sorted;
        public boolean unsorted;
        public boolean empty;
    }
}
