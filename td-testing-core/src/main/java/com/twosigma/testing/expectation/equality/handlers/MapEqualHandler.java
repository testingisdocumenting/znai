package com.twosigma.testing.expectation.equality.handlers;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;

import java.util.*;

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
        comparator.compare();
    }

    private class Comparator {
        private EqualComparator equalComparator;
        private ActualPath actualPath;
        private Map<?, ?> actualMap;
        private Map<?, ?> expectedMap;
        private Set<Object> allKeys;

        Comparator(EqualComparator equalComparator, ActualPath actualPath, Map<?, ?> actualMap, Map<?, ?> expectedMap) {
            this.equalComparator = equalComparator;
            this.actualPath = actualPath;
            this.actualMap = actualMap;
            this.expectedMap = expectedMap;

            allKeys = new HashSet<>(actualMap.keySet());
        }

        void compare() {
            allKeys.forEach(this::handleKey);
        }

        private void handleKey(Object key) {
            final ActualPath propertyPath = actualPath.property(key.toString());

            if (! actualMap.containsKey(key)) {
                equalComparator.reportMissing(MapEqualHandler.this, propertyPath, expectedMap.get(key));
            } else if (! expectedMap.containsKey(key)) {
                equalComparator.reportExtra(MapEqualHandler.this, propertyPath, actualMap.get(key));
            } else {
                equalComparator.compare(propertyPath, actualMap.get(key), expectedMap.get(key));
            }
        }
    }
}
