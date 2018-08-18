package com.twosigma.testing.http;

import com.twosigma.utils.UrlUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author mykola
 */
public class HttpUrl {
    private static List<String> fullUrlPrefixes = Arrays.asList("http:", "https:", "file:", "mailto:");

    private HttpUrl() {
    }

    public static boolean isFull(String url) {
        return fullUrlPrefixes.stream().anyMatch(p -> url.toLowerCase().startsWith(p));
    }

    public static String concat(String left, String right) {
        return UrlUtils.concat(left, right);
    }
}
