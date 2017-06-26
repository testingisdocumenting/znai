package com.twosigma.testing.data.converters;

import com.twosigma.utils.ServiceLoaderUtils;

import java.util.Set;

/**
 * @author mykola
 */
public class ToNumberConverters {
    private static Set<ToNumberConverter> converters = ServiceLoaderUtils.load(ToNumberConverter.class);

    public static Number convert(Object v) {
        if (v instanceof Number) {
            return (Number) v;
        }

        return TypeConvertersUtils.convert(converters.stream(), "number", v);
    }
}
