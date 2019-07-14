package com.twosigma.testing.tcli.output

import com.twosigma.webtau.data.render.DataRenderers
import com.twosigma.webtau.expectation.ActualPath
import com.twosigma.webtau.expectation.equality.CompareToComparator
import com.twosigma.webtau.expectation.equality.CompareToHandler

class OutputLinesCompareToHandler implements CompareToHandler {
    @Override
    boolean handleNulls() {
        return true
    }

    @Override
    boolean handleEquality(Object actual, Object expected) {
        return actual instanceof OutputLines
    }

    @Override
    void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        OutputLines actualLines = actual

        def localComparator = CompareToComparator.comparator(comparator.assertionMode)

        def matchedIdxs = []
        def lines = actualLines.getLines()
        lines.eachWithIndex { line, idx ->
            def result = localComparator.compareIsEqual(actualPath.index(idx), line, expected)
            if (result) {
                matchedIdxs.add(idx)
            }
        }

        comparator.reportEqualOrNotEqual(this, !matchedIdxs.isEmpty(), actualPath, renderActualExpected(actual, expected))

        matchedIdxs.each { actualLines.registerCheckedLine(it) }
    }

    private static String renderActualExpected(Object actual, Object expected) {
        return "  actual: " + actual + "\n" +
            "expected: " + DataRenderers.render(expected)
    }
}
