package com.twosigma.testing.tcli.output

import com.twosigma.testing.data.render.DataRenderers
import com.twosigma.testing.expectation.ActualPath
import com.twosigma.testing.expectation.equality.EqualComparator
import com.twosigma.testing.expectation.equality.EqualComparatorHandler

/**
 * @author mykola
 */
class OutputLinesEqualHandler implements EqualComparatorHandler {
    @Override
    boolean handle(Object actual, Object expected) {
        return actual instanceof OutputLines
    }

    @Override
    void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        OutputLines actualLines = actual

        def localComparator = equalComparator.freshCopy()

        def matchedIdxs = []
        def lines = actualLines.getLines()
        lines.eachWithIndex { line, idx ->
            def result = localComparator.compare(actualPath.index(idx), line, expected)
            if (! result.isMismatch()) {
                matchedIdxs.add(idx)
            }
        }

        if (matchedIdxs.isEmpty()) {
            equalComparator.reportMismatch(this, actualPath,
                    "doesn't match " + DataRenderers.render(expected) +
                            ":\n" + actual)
        }

        matchedIdxs.each { actualLines.registerCheckedLine(it) }
    }
}
