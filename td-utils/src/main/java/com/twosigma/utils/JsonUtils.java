package com.twosigma.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

/**
 * @author mykola
 */
public class JsonUtils {
    private static final Gson gson = new Gson();
    private static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

    private JsonUtils() {
    }

    public static String serialize(Object data) {
        return gson.toJson(data);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, ?> deserializeAsMap(String data) {
        return gson.fromJson(data, Map.class);
    }

    @SuppressWarnings("unchecked")
    public static List<?> deserializeAsList(String data) {
        return gson.fromJson(data, List.class);
    }

    public static String serializePrettyPrint(Object data) {
        return gsonPretty.toJson(data);
    }
}
