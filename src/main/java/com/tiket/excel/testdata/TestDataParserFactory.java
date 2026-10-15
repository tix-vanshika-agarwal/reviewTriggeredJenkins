package com.tiket.excel.testdata;

import com.tiket.excel.Parser;
import com.tiket.model.Location;
import com.tiket.model.TestDataElement;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

public class TestDataParserFactory {

    public static Parser<List<TestDataElement>> get() {
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
