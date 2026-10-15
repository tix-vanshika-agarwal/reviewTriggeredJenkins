package com.tiket.pojo.utilities;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DynamicContentDeserializer implements JsonDeserializer<HomePageModule.DynamicContent> {

    @Override
    public HomePageModule.DynamicContent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        HomePageModule.DynamicContent content = new HomePageModule().new DynamicContent();
        JsonObject obj = json.getAsJsonObject();

        // Simple fields
        content.title = getAsString(obj, "title");
        content.url = getAsString(obj, "url");
        content.description = getAsString(obj, "description");
        content.buttonTitle = getAsString(obj, "buttonTitle");
        content.imageBackground = getAsString(obj, "imageBackground");
        content.buttonText = getAsString(obj, "buttonText");
        content.contentTitle = getAsString(obj, "contentTitle");
        content.contentDescription = getAsString(obj, "contentDescription");
        content.buttonUrl = getAsString(obj, "buttonUrl");
        content.tabText = getAsString(obj, "tabText");
        content.productName = getAsString(obj, "productName");
        content.variantName = getAsString(obj, "variantName");

        // Handle location flexibly
        JsonElement locationElem = obj.get("location");
        if (locationElem != null) {
            if (locationElem.isJsonPrimitive()) {
                // Case: simple string
                content.location = locationElem.getAsString();
            } else if (locationElem.isJsonObject()) {
                // Case: nested object → convert to readable string
                JsonObject locObj = locationElem.getAsJsonObject();
                String name = getAsString(locObj, "name");
                String cityName = getAsString(locObj, "cityName");
                String countryName = getAsString(locObj, "countryName");
                content.location = Stream.of(name, cityName, countryName)
                        .filter(Objects::nonNull)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.joining(", "));
            }
        }

        // Other nested objects/arrays — let Gson handle them normally
        content.rateInfo = context.deserialize(obj.get("rateInfo"), HomePageModule.RateInfo.class);
        content.reviews = context.deserialize(obj.get("reviews"), HomePageModule.Reviews.class);
        content.chips = context.deserialize(obj.get("chips"), new com.google.gson.reflect.TypeToken<java.util.ArrayList<HomePageModule.Chip>>(){}.getType());
        content.cards = context.deserialize(obj.get("cards"), new com.google.gson.reflect.TypeToken<java.util.ArrayList<Object>>(){}.getType());

        return content;
    }

    private String getAsString(JsonObject obj, String memberName) {
        JsonElement el = obj.get(memberName);
        return (el != null && !el.isJsonNull()) ? el.getAsString() : null;
    }
}
