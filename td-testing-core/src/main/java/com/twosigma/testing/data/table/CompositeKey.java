package com.twosigma.testing.data.table;

import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.twosigma.testing.Ddjt.createActualPath;

/**
 * Composite key to be used in structures like {@link TableData}. Keys comparison rules are dictated by {@link EqualComparatorHandler}.
 * @author mykola
 */
public class CompositeKey {
    private List<Object> values;

    public CompositeKey(Stream<Object> values) {
        this.values = values.collect(Collectors.toList());
    }

    public List<?> getValues() {
        return Collections.unmodifiableList(values);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;

        if (other.getClass() != CompositeKey.class)
            return false;

        final List<Object> otherValues = ((CompositeKey) other).values;
        final EqualComparator ec = EqualComparator.comparator();
        ec.compare(createActualPath(""), values, otherValues);

        return ec.areEqual();
    }

    public int hashCode() {
        return values.stream().map(Object::hashCode).reduce(0, (l, r) -> l * 31 + r);
    }

    @Override
    public String toString() {
        return "CompositeKey: " + values;
    }
}
