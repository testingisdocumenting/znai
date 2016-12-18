package com.twosigma.utils;

import com.twosigma.testing.data.render.DataRenderers;

/**
 * @author mykola
 */
public class TraceUtils {
    private TraceUtils() {
    }

    public static String renderValueAndType(final Object v) {
        return DataRenderers.render(v) + " " + renderType(v);
    }

    public static String renderType(final Object v) {
        return "<" + (v == null ? "null" : v.getClass().getCanonicalName()) + ">";
    }

}
