package com.twosigma.utils;

import java.util.Arrays;

/**
 * @author mykola
 */
public class StringUtils {
    private StringUtils() {
    }

    public static int maxLineLength(String text) {
        return Arrays.stream(text.replace("\r", "").split("\n")).
                map(String::length).
                max(Integer::compareTo).
                orElse(0);
    }
}
