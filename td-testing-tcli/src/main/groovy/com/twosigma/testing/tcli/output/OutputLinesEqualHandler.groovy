package com.twosigma.testing.tcli.output

import com.twosigma.webtau.data.render.DataRenderers
import com.twosigma.webtau.expectation.ActualPath
import com.twosigma.webtau.expectation.equality.CompareToComparator
import com.twosigma.webtau.expectation.equality.CompareToHandler

/**
 * @author mykola
 */
class OutputLinesEqualHandler implements CompareToHandler {
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
            def result = localComparator.compare(actualPath.index(idx), line, expected)
            if (! result.isMismatch()) {
                matchedIdxs.add(idx)
            }
        }

        if (matchedIdxs.isEmpty()) {
            comparator.reportMismatch(this, actualPath,
                    "doesn't match " + DataRenderers.render(expected) +
                            ":\n" + actual)
        }

        matchedIdxs.each { actualLines.registerCheckedLine(it) }
    }
}
