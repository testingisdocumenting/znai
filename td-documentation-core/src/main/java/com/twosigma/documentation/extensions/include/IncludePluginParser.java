package com.twosigma.documentation.extensions.include;

import com.twosigma.documentation.extensions.PluginParams;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mykola
 */
public class IncludePluginParser {
    private static final Pattern PATTERN = Pattern.compile(":include-(\\S+)+:(.*)$");

    private IncludePluginParser() {
    }

    public static PluginParams parse(String line) {
        final Matcher matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("To define include plugin use\n:" +
                    "include-plugin-id: free form value {optional: keyValues}\n" +
                    "Got: " + line);
        }

        return new PluginParams(matcher.group(1).trim(), matcher.group(2).trim());
    }
}
