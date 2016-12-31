package com.twosigma.documentation.html;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mykola
 */
public class HtmlRenderContext {
    private final int nestLevel;

    private HtmlRenderContext(final int nestLevel) {
        this.nestLevel = nestLevel;
    }

    public static HtmlRenderContext nested(final int nestLevel) {
        return new HtmlRenderContext(nestLevel);
    }

    public int getNestLevel() {
        return nestLevel;
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> result = new LinkedHashMap<>();
        result.put("nestLevel", nestLevel);

        return result;
    }
}
