package com.twosigma.testing.http.json;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 * @author mykola
 */
public class JsonSerialization {
    private static Gson gson = new Gson(); // TODO remove all the references from the code and switch to util call

    private JsonSerialization() {
    }

    public static String toJson(final Object o) {
        return gson.toJson(o);
    }

    public static String toJson(final List<?> list) {
        return gson.toJson(list);
    }

    public static String toJson(final Map<String, ?> map) {
        return gson.toJson(map);
    }
}
