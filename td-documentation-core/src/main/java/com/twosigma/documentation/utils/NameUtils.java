package com.twosigma.documentation.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class NameUtils {
    public static String camelCaseWithSpacesToDashes(final String headingText) {
        if (headingText == null)
            return null;

        return headingText.toLowerCase().replace(" ", "-");
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
