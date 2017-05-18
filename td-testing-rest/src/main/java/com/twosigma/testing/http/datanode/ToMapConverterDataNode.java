package com.twosigma.testing.http.datanode;

import java.util.Map;

import com.twosigma.testing.data.converters.ToMapConverter;

/**
 * @author mykola
 */
public class ToMapConverterDataNode implements ToMapConverter {
    @Override
    public Map<String, ?> convert(final Object v) {
        if (v instanceof DataNode) {
            return ((DataNode) v).asMap();
        }

        return null;
    }
}
