package com.tiket.testbase;

import com.tiket.io.api.ApiTools;
import com.tiket.io.local.PropertiesReader;
import com.tiket.logging.ILogger;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public interface BaseApi {

    UriHelper uriHelper = new UriHelper("https://gatotkaca.tiket.com", "https://api.tiket.com", "https://preprod.tiket.com");
    ILogger logger = BaseTest.mainLogger.get();

    ApiResponse hitApi(ApiTools apiTools);

    @SneakyThrows
    default JSONObject getStaticJson(String path) {
        File file = new File(path);
        return new JSONObject(FileUtils.readFileToString(file, Charset.defaultCharset()));
    }

    @SneakyThrows
    default JSONArray getStaticJsonArray(String path) {
        File file = new File(path);
        return new JSONArray(FileUtils.readFileToString(file, Charset.defaultCharset()));
    }

    default Map<String, Object> getStaticMap(String path) {
        Properties properties = PropertiesReader.read(path);
        Map<String, Object> map = new HashMap<>();
        properties.forEach((k, v) -> map.put((String) k, v));
        return map;
    }
}
