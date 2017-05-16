package com.twosigma.testing.expectation.equality.handlers;

import java.math.BigDecimal;
import java.util.Scanner;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;

import static com.twosigma.utils.TraceUtils.renderValueAndType;

/**
 * @author mykola
 */
public class NumbersEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(final Object actual, final Object expected) {
        return actual instanceof Number && expected instanceof Number && (actual.getClass() != expected.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void compare(final EqualComparator equalComparator, final ActualPath actualPath, final Object actual,
        final Object expected) {

        final Class largest = largest(actual, expected);

        Comparable convertedActual = convertTo(largest, actual);
        Comparable convertedExpected = convertTo(largest, expected);

        if (convertedActual.compareTo(convertedExpected) != 0) {
            equalComparator.reportMismatch(this, actualPath, mismatchMessage(actualPath, actual, convertedActual, expected, convertedExpected));
        }
    }

    private String mismatchMessage(ActualPath actualPath, Object actual, Object convertedActual, Object expected, Object convertedExpected) {
        return actualPath + "   actual: " + renderValueAndType(convertedActual) + "(before conversion: " + renderValueAndType(actual) + ")\n" +
            actualPath + " expected: " + renderValueAndType(convertedExpected) + "(before conversion: " + renderValueAndType(expected) + ")";
    }

    private static Class largest(Object actual, Object expected) {
        if (isOfType(BigDecimal.class, actual, expected)) {
            return BigDecimal.class;
        }

        if (isOfType(Double.class, actual, expected)) {
            return Double.class;
        }

        if (isOfType(Long.class, actual, expected)) {
            return Long.class;
        }

        return actual.getClass();
    }

    private static boolean isOfType(Class type, Object actual, Object expected) {
        return actual.getClass().isAssignableFrom(type) || expected.getClass().isAssignableFrom(expected.getClass());
    }

    private static Comparable convertTo(Class to, Object original) {
        final Scanner scanner = new Scanner(original.toString());

        if (to == BigDecimal.class) {
            return scanner.nextBigDecimal();
        }

        if (to == Double.class) {
            return scanner.nextDouble();
        }

        if (to == Long.class) {
            return scanner.nextLong();
        }

        if (to == Integer.class) {
            return scanner.nextInt();
        }

        return (Comparable) original;
    }
}
