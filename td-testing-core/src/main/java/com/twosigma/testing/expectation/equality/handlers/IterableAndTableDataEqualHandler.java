package com.twosigma.testing.expectation.equality.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.twosigma.testing.data.converters.ToMapConverters;
import com.twosigma.testing.data.table.Header;
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
public class IterableAndTableDataEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof Iterable && expected instanceof TableData;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        final TableData expectedTable = (TableData) expected;
        final TableData actualTable = createTableFromList(expectedTable.getHeader(), (List) actual);

        final TableDataComparisonResult result = TableDataComparison.compare(actualTable, expectedTable);
        if (! result.areEqual()) {
            equalComparator.reportMismatch(this, actualPath, new TableDataComparisonReport(result).generate());
        }
    }

    private static TableData createTableFromList(Header expectedHeader, List actualList) {
        final TableData actualTable = TableData.header(expectedHeader.getNames());
        for (Object actualRecord : actualList) {
            final Map<String, ?> actualMap = ToMapConverters.convert(actualRecord);
            actualTable.addRow(mapToList(expectedHeader, actualMap));
        }

        return actualTable;
    }

    private static List<Object> mapToList(Header header, Map<String, ?> map) {
        List<Object> result = new ArrayList<>();
        header.getNames().forEach(n -> result.add(map.get(n)));

        return result;
    }
}
