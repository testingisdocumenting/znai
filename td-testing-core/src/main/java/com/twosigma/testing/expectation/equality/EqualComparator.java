package com.twosigma.testing.expectation.equality;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.twosigma.testing.data.render.DataRenderers;
import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.handlers.AnyEqualHandler;
import com.twosigma.testing.expectation.equality.handlers.NullEqualHandler;
import com.twosigma.utils.ServiceUtils;
import com.twosigma.utils.TraceUtils;

/**
 * @author mykola
 */
public class EqualComparator {
    private static List<EqualComparatorHandler> handlers = discoverHandlers();
    private List<String> mismatches = new ArrayList<>();

    private final boolean isNegative;

    public static EqualComparator comparator() {
        return new EqualComparator(false);
    }

    public static EqualComparator negativeComparator() {
        return new EqualComparator(true);
    }

    private EqualComparator(final boolean isNegative) {
        this.isNegative = isNegative;
    }

    public void compare(ActualPath actualPath, Object actual, Object expected) {
        EqualComparatorHandler handler = handlers.stream().
            filter(h -> h.handle(actual, expected)).findFirst().
            orElseThrow(() -> noHandlerFound(actual, expected));

        handler.compare(this, actualPath, actual, expected);
    }

    /**
     * comparator operates in two mode: should and shouldNot
     * @return true if the mode is shouldNot
     */
    public boolean isNegative() {
        return isNegative;
    }

    public boolean areEqual() {
        if (isNegative()) {
            return !mismatches.isEmpty();
        } else {
            return mismatches.isEmpty();
        }
    }

    public String generateMismatchReport() {
        return mismatches.stream().collect(Collectors.joining("\n"));
    }

    public void reportMismatch(EqualComparatorHandler reporter, String mismatch) {
        mismatches.add(mismatch + " [reported by " + reporter.getClass().getSimpleName() + "]");
    }

    public int numberOfMismatches() {
        return mismatches.size();
    }

    private static List<EqualComparatorHandler> discoverHandlers() {
        List<EqualComparatorHandler> result = new ArrayList<>();

        List<EqualComparatorHandler> discovered = ServiceUtils.discover(EqualComparatorHandler.class);

        discovered.stream().filter(EqualComparatorHandler::handleNulls).forEach(result::add);
        result.add(new NullEqualHandler());
        discovered.stream().filter(h -> ! h.handleNulls()).forEach(result::add);

        result.add(new AnyEqualHandler());

        return result;
    }

    private RuntimeException noHandlerFound(Object actual, Object expected) {
        return new RuntimeException(
            "no equal comparator handler found for\nactual: " + DataRenderers.render(actual) + " " + TraceUtils.renderType(actual) +
            "\nexpected: " + DataRenderers.render(expected) + " " + TraceUtils.renderType(expected));
    }
}
