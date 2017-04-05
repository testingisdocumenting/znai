package com.twosigma.testing.http.datacoverage;

import com.twosigma.testing.data.traceable.TraceableValue;
import com.twosigma.testing.http.datanode.DataNode;
import com.twosigma.testing.http.datanode.DataNodeId;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mykola
 */
public class DataNodeToMapOfValuesConverter {
    private TraceableValueConverter traceableValueConverter;

    public DataNodeToMapOfValuesConverter(TraceableValueConverter traceableValueConverter) {
        this.traceableValueConverter = traceableValueConverter;
    }

    public Object convert(DataNode n) {
        if (n.isList()) {
            return convertToList(n);
        } else if (n.isSingleValue()) {
            return convertSingleValue(n.id(), n.get());
        } else {
            return convertToMap(n);
        }
    }

    private Map<String, Object> convertToMap(DataNode dataNode) {
        Map<String, Object> converted = new LinkedHashMap<>();
        dataNode.asMap().forEach((k, v) -> converted.put(k, convert(v)));

        return converted;
    }

    private List<Object> convertToList(DataNode dataNode) {
        List<Object> converted = new ArrayList<>();
        dataNode.all().forEach(n -> converted.add(convert(n)));

        return converted;
    }

    private Object convertSingleValue(DataNodeId id, TraceableValue value) {
        return traceableValueConverter.convert(id, value);
    }
}
