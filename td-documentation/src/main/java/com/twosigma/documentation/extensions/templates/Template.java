package com.twosigma.documentation.extensions.templates;

import java.util.regex.Pattern;

/**
 * @author mykola
 */
public class Template {
    public static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$(\\w+)");
}
