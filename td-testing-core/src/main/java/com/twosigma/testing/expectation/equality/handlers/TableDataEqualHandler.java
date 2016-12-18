package com.twosigma.testing.expectation.equality.handlers;

import com.twosigma.testing.data.table.TableData;
import com.twosigma.testing.data.table.comparison.TableDataComparison;
import com.twosigma.testing.data.table.comparison.TableDataComparisonReport;
import com.twosigma.testing.data.table.comparison.TableDataComparisonResult;
import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;

/**
 * @author mykola
 */
public class TableDataEqualHandler
    implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof TableData && expected instanceof TableData;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        final TableDataComparisonResult result = TableDataComparison.compare((TableData) actual, (TableData) expected);
        if (! result.areEqual()) {
            equalComparator.reportMismatch(this, new TableDataComparisonReport(result).generate());
        }
    }
}
