package com.twosigma.testing.http;

import java.util.Arrays;
import java.util.List;

/**
 * @author mykola
 */
public class HttpUrl {
    private static List<String> fullUrlPrefixes = Arrays.asList("http:", "https:", "file:");

    private HttpUrl() {
    }

    public static boolean isFull(final String url) {
        return fullUrlPrefixes.stream().anyMatch(url::startsWith);
    }

    public static String concat(final String left, final String right) {
        if (left == null) {
            throw new IllegalArgumentException("passed url on the left is NULL");
        }

        if (right == null) {
            throw new IllegalArgumentException("passed url on the right is NULL");
        }

        if (left.endsWith("/") && !right.startsWith("/")) {
            return left + right;
        }

        if (! left.endsWith("/") && right.startsWith("/")) {
            return left + right;
        }

        if (left.endsWith("/") && right.startsWith("/")) {
            return left + right.substring(1);
        }

        return left + "/" + right;
    }
}
