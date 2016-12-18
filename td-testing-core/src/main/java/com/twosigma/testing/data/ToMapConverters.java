package com.twosigma.testing.data;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.twosigma.utils.ServiceUtils;
import com.twosigma.utils.TraceUtils;

/**
 * @author mykola
 */
public class ToMapConverters {
    private static List<ToMapConverter> converters = ServiceUtils.discover(ToMapConverter.class);

    public static Map<String, ?> convert(Object v) {
        if (v == null) {
            return null;
        }

        return converters.stream().
            map(h -> h.convert(v)).
            filter(Objects::nonNull).
            findFirst().
            orElseThrow(() -> new IllegalArgumentException("can't find a map converter for: " + TraceUtils.renderValueAndType(v)));
    }
}
