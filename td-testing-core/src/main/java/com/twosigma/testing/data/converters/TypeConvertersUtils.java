package com.twosigma.testing.data.converters;

import com.twosigma.utils.TraceUtils;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author mykola
 */
class TypeConvertersUtils {
    public static <E> E convert(Stream<? extends ToTypeConverter<E>> converters, String typeName, Object v) {
        if (v == null) {
            return null;
        }

        return converters.
                map(h -> h.convert(v)).
                filter(Objects::nonNull).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException("can't find a " + typeName +
                        " converter for: " + TraceUtils.renderValueAndType(v)));
    }
}
