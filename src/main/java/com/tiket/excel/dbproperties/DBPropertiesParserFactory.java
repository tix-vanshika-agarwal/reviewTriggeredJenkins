package com.tiket.excel.dbproperties;

import com.tiket.excel.Parser;
import com.tiket.model.DBProperties;
import com.tiket.model.Location;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

public class DBPropertiesParserFactory {

    public static Parser<List<DBProperties>> get() {
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
