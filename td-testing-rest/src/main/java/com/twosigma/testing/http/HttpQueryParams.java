package com.twosigma.testing.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class HttpQueryParams {
    private Map<String, String> params;
    private String asString;

    public HttpQueryParams(final Map<String, String> params) {
        this.params = new LinkedHashMap<>(params);
        this.asString = this.params.entrySet().stream().map(e -> decode(e.getKey()) + "=" + decode(e.getValue())).collect(
            Collectors.joining("&"));
    }

    @Override
    public String toString() {
        return asString;
    }

    private static String decode(String text) {
        try {
            return URLDecoder.decode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
