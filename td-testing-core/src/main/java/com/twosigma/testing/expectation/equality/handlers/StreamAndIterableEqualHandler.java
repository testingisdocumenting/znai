package com.twosigma.testing.expectation.equality.handlers;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class StreamAndIterableEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(final Object actual, final Object expected) {
        return actual instanceof Stream && expected instanceof Iterable;
    }

    @Override
    public void compare(final EqualComparator equalComparator, final ActualPath actualPath, final Object actual, final Object expected) {
        final Stream<?> actualStream = (Stream) actual;
        List<?> actualList = actualStream.collect(toList());

        equalComparator.compare(actualPath, actualList, expected);
    }
}
