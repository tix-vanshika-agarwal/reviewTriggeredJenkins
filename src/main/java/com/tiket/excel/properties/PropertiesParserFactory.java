package com.tiket.excel.properties;

import com.tiket.excel.Parser;
import com.tiket.model.Location;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Map;

public class PropertiesParserFactory {

    public static Parser<Map<String, String>> get() {
        Location location = Configs.LOCATION;
        switch (location) {
            case LOCAL -> {
                return new LocalParser();
            }
            case GOOGLE -> {
                return new GoogleParser();
            }
            default -> throw new NotImplementedException("Not implemented: " + location);
        }
    }
}
