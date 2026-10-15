package com.tiket.pojo.b2b.corporate;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Accepts both a JSON array of {@link OrderDetailResponse.OrderDetail} and a single
 * OrderDetail object and normalizes them to a List, so callers can always do
 * {@code data.get(0).orderDetail.get(0)} regardless of product type.
 *
 * <p>Flight responses return {@code orderDetail} as an array (each entry with
 * {@code depart}/{@code return}), while hotel/single-leg responses return it as a
 * single object.
 *
 * <p>Anything other than null, JSON object, or JSON array is treated as an API contract
 * violation and surfaces as a {@link JsonParseException} so the failure is loud rather
 * than silently producing an empty list that breaks downstream {@code .get(0)} access
 * with an opaque {@link IndexOutOfBoundsException}.
 */
public class OrderDetailListAdapter implements JsonDeserializer<List<OrderDetailResponse.OrderDetail>> {
    @Override
    public List<OrderDetailResponse.OrderDetail> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
        List<OrderDetailResponse.OrderDetail> list = new ArrayList<>();
        if (json == null || json.isJsonNull()) {
            return list;
        }
        if (json.isJsonArray()) {
            for (JsonElement el : json.getAsJsonArray()) {
                if (el == null || el.isJsonNull()) continue;
                list.add(ctx.deserialize(el, OrderDetailResponse.OrderDetail.class));
            }
            return list;
        }
        if (json.isJsonObject()) {
            list.add(ctx.deserialize(json, OrderDetailResponse.OrderDetail.class));
            return list;
        }
        throw new JsonParseException("Unexpected JSON type for orderDetail: expected array, object, or null but got "
                + json.getClass().getSimpleName() + " -> " + json);
    }
}
