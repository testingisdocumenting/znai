package com.twosigma.testing.data.table;

import java.util.ArrayList;
import java.util.List;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;

/**
 * Composite key to be used in structures like {@link TableData}. Keys comparison rules are dictated by {@link EqualComparatorHandler}.
 * @author mykola
 */
public class CompositeKey {
    private List<Object> values;

    public CompositeKey(List<Object> values) {
        this.values = new ArrayList<>(values);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;

        if (other.getClass() != CompositeKey.class)
            return false;

        final List<Object> otherValues = ((CompositeKey) other).values;
        final EqualComparator ec = EqualComparator.comparator();
        ec.compare(ActualPath.createActualPath(""), values, otherValues);

        return ec.areEqual();
    }

    public int hashCode() {
        return values.stream().map(Object::hashCode).reduce(0, (l, r) -> l * 31 + r);
    }
}
