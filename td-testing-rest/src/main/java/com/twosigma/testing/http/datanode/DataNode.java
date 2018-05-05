package com.twosigma.testing.http.datanode;

import com.twosigma.testing.data.traceable.TraceableValue;
import com.twosigma.testing.expectation.*;
import com.twosigma.testing.expectation.timer.ExpectationTimer;
import com.twosigma.testing.reporter.IntegrationTestsMessageBuilder;
import com.twosigma.testing.reporter.ValueMatcherExpectationSteps;

import java.util.List;
import java.util.Map;

import static com.twosigma.testing.Ddjt.createActualPath;
import static com.twosigma.testing.reporter.TokenizedMessage.tokenizedMessage;

/**
 * @author mykola
 */
public interface DataNode extends ActualValueExpectations, DataNodeExpectations {
    DataNodeId id();

    DataNode get(String name);

    DataNode get(int idx);

    TraceableValue get();

    boolean isList();

    boolean isSingleValue();

    List<DataNode> all();

    int numberOfChildren();

    int numberOfElements();

    Map<String, DataNode> asMap();

    @Override
    default ActualPath actualPath() {
        return createActualPath(id().getPath());
    }
}
