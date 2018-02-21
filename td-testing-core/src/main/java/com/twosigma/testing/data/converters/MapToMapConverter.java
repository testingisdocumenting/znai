package com.twosigma.testing.data.converters;

import com.twosigma.utils.JavaBeanUtils;

import java.util.Map;

/**
 * @author mykola
 */
public class MapToMapConverter implements ToMapConverter {
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, ?> convert(Object v) {
        if (v instanceof Map) {
            return (Map<String, ?>) v;
        }

        return null;
    }
}
