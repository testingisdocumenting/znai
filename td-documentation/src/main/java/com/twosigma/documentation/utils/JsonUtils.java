package com.twosigma.documentation.utils;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author mykola
 */
public class JsonUtils {
    public static String serialize(Map<String, Object> data) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(data);
    }
}
