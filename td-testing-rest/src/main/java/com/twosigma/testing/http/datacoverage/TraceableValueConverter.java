package com.twosigma.testing.http.datacoverage;

import com.twosigma.testing.data.traceable.TraceableValue;
import com.twosigma.testing.http.datanode.DataNodeId;

/**
 * @author mykola
 */
public interface TraceableValueConverter {
    Object convert(DataNodeId id, TraceableValue traceableValue);
}
