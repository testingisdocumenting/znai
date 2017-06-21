package com.twosigma.testing.data.converters;

import com.twosigma.utils.ServiceLoaderUtils;

import java.util.Set;

/**
 * @author mykola
 */
public class ToNumberConverters {
    private static Set<ToNumberConverter> converters = ServiceLoaderUtils.load(ToNumberConverter.class);

    public static Number convert(Object v) {
        return TypeConvertersUtils.convert(converters.stream(), "number", v);
    }
}
