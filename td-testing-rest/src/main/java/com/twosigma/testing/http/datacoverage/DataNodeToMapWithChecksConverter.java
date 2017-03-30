package com.twosigma.testing.http.datacoverage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.twosigma.testing.data.traceable.TraceableValue;
import com.twosigma.testing.http.datanode.DataNode;

/**
 * @author mykola
 */
public class DataNodeToMapWithChecksConverter {
    public static Object convert(final DataNode n) {
        if (n.isList()) {
            return convertToList(n);
        } else if (n.isSingleValue()) {
            return convertSingleValue(n.get());
        } else {
            return convertToMap(n);
        }
    }

    public static Map<String, Object> convertToMap(final DataNode dataNode) {
        final Map<String, Object> converted = new LinkedHashMap<>();
        dataNode.asMap().forEach((k, v) -> converted.put(k, convert(v)));

        return converted;
    }

    public static List<Object> convertToList(final DataNode dataNode) {
        final List<Object> converted = new ArrayList<>();
        dataNode.all().forEach(n -> converted.add(convert(n)));

        return converted;
    }

    private static Map<String, Object> convertSingleValue(final TraceableValue value) {
        final Map<String, Object> withChecks = new LinkedHashMap<>();
        withChecks.put("__value", value.getValue());
        withChecks.put("__checkLevel", value.getCheckLevel().toString());

        return withChecks;
    }
}
