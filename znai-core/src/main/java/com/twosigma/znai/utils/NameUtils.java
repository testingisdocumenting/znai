package com.twosigma.znai.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class NameUtils {
    public static String idFromTitle(final String title) {
        if (title == null)
            return null;

        String onlyTextAndNumbers = title.replaceAll("[^a-zA-Z0-9-_ ]", "");
        return onlyTextAndNumbers.toLowerCase().replaceAll("\\s+", "-");
    }

    public static String dashToCamelCaseWithSpaces(final String dashBasedName) {
        if (dashBasedName == null)
            return null;

        final String[] parts = dashBasedName.split("-");
        return Arrays.stream(parts).
            filter(p -> ! p.isEmpty()).
            map(p -> Character.toUpperCase(p.charAt(0)) + p.substring(1)).
            collect(Collectors.joining(" "));
    }
}
