package com.twosigma.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public static String serializePrettyPrint(Object data) {
        return gsonPretty.toJson(data);
    }
}
