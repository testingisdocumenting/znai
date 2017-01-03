package com.twosigma.testing.expectation.equality.handlers;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;
import com.twosigma.utils.TraceUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class MapEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof Map && expected instanceof Map;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        Map<?, ?> actualMap = (Map) actual;
        Map<?, ?> expectedMap = (Map) expected;

        Comparator comparator = new Comparator(equalComparator, actualPath, actualMap, expectedMap);
        String report = comparator.compareAndReport();

        if (! report.isEmpty()) {
            equalComparator.reportMismatch(this, report);
        }
    }

    private static class Comparator {
        private EqualComparator equalComparator;
        private ActualPath actualPath;
        private Map<?, ?> actualMap;
        private Map<?, ?> expectedMap;
        private Set<Object> allKeys;

        private List<String> missing = new ArrayList<>();
        private List<String> extras = new ArrayList<>();
        private List<String> mismatches = new ArrayList<>();

        Comparator(EqualComparator equalComparator, ActualPath actualPath, Map<?, ?> actualMap, Map<?, ?> expectedMap) {
            this.equalComparator = equalComparator;
            this.actualPath = actualPath;
            this.actualMap = actualMap;
            this.expectedMap = expectedMap;

            allKeys = new HashSet<>(actualMap.keySet());
        }

        String compareAndReport() {
            for (Object key : allKeys) {
                handleKey(key);
            }

            return buildReport();
        }

        private void handleKey(Object key) {
            final ActualPath propertyPath = actualPath.property(key.toString());

            if (! actualMap.containsKey(key)) {
                missing.add(propertyPath.getPath());
            } else if (! expectedMap.containsKey(key)) {
                extras.add(propertyPath.getPath());
            } else {
                compare(propertyPath, actualMap.get(key), expectedMap.get(key));
            }
        }

        private void compare(ActualPath path, Object actual, Object expected) {
            EqualComparator freshEqualComparator = this.equalComparator.freshCopy();
            freshEqualComparator.compare(path, actual, expected);

            if (! freshEqualComparator.areEqual()) {
                mismatches.add(freshEqualComparator.generateMismatchReport());
            }
        }

        private String buildReport() {
            if (missing.isEmpty() && extras.isEmpty() && mismatches.isEmpty()) {
                return "";
            }

            Function<List<String>, String> toString = (list) -> list.stream().collect(Collectors.joining("\n")) + "\n";

            // TODO mega reporter with auto indent and colors but semantic colors instead of hardcoded so it can be used in web reports
            return "report for " + actualPath + "\n" +
                    (missing.isEmpty() ? "" : "missing keys:\n" + toString.apply(missing)) +
                    (extras.isEmpty() ? "" : "extra keys:\n" + toString.apply(extras)) +
                    (mismatches.isEmpty() ? "" : "mismatches:\n" + toString.apply(mismatches));
        }
    }
}
