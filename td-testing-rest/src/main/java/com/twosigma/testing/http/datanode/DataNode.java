package com.twosigma.testing.http.datanode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.twosigma.testing.data.traceable.TraceableValue;
import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.ActualPathAware;

/**
 * @author mykola
 */
public interface DataNode extends ActualPathAware {
    DataNodeId id();
    DataNode get(final String name);
    DataNode get(final int idx);
    TraceableValue get();
    boolean isList();
    boolean isSingleValue();
    List<DataNode> all();

    int numberOfChildren();
    int numberOfElements();

    Map<String, DataNode> asMap();

    @Override
    default ActualPath actualPath() {
        return ActualPath.createActualPath(id().getPath());
    }
}
