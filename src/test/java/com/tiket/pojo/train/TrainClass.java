package com.tiket.pojo.train;

import org.apache.commons.lang3.NotImplementedException;

public class TrainClass {
    public String eng;
    public String id;

    public TrainClass(String trainClass) {
        switch (trainClass.toLowerCase()) {
            case "executive", "eksekutif" -> {
                this.id = "Eksekutif";
                this.eng = "Executive";
            }
            case "economy", "ekonomi" -> {
                this.id = "Ekonomi";
                this.eng = "Economy";
            }
            case "business", "bisnis" -> {
                this.id = "Bisnis";
                this.eng = "Business";
            }
            default -> throw new NotImplementedException("Not implemented: " + trainClass);
        }
    }
}
