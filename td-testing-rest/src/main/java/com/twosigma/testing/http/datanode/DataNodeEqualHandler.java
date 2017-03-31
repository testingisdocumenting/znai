package com.twosigma.testing.http.datanode;

import java.util.List;
import java.util.Map;

import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;
import javafx.scene.control.Tab;

import com.twosigma.testing.data.table.TableData;
import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.http.datanode.DataNode;

/**
 * @author mykola
 */
public class DataNodeEqualHandler
    implements EqualComparatorHandler {
    @Override
    public boolean handleNulls() {
        return true;
    }

    @Override
    public boolean handle(final Object actual, final Object expected) {
        return actual instanceof DataNode;
    }

    @Override
    public void compare(final EqualComparator equalComparator, final ActualPath actualPath, final Object actual,
                        final Object expected) {

        final Object convertedActual = convertBasedOnExpected((DataNode) actual, expected);
        equalComparator.compare(actualPath, convertedActual, expected);
    }

    private Object convertBasedOnExpected(DataNode actual, final Object expected) {
        if (expected instanceof Map) {
            return actual.asMap();
        }

        if (expected instanceof List || expected instanceof TableData) {
            return actual.all();
        }

        return actual.get();
    }
}
