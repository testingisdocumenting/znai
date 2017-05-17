package com.twosigma.testing.data.table.comparison;

import java.util.Set;

import com.twosigma.testing.data.table.Record;
import com.twosigma.testing.data.table.TableData;
import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.EqualComparator;

import static java.util.stream.Collectors.toSet;

/**
 * @author mykola
 */
public class TableDataComparison {
    private TableData actual;
    private TableData expected;
    private TableDataComparisonResult comparisonResult;
    private Set<String> columnsToCompare;

    public static TableDataComparisonResult compare(TableData actual, TableData expected) {
        TableDataComparison comparison = new TableDataComparison(actual, expected);
        comparison.compare();

        return comparison.comparisonResult;
    }

    private TableDataComparison(TableData actual, TableData expected) {
        this.actual = actual;
        this.expected = expected;
        this.comparisonResult = new TableDataComparisonResult(actual, expected);
    }

    private void compare() {
        compareColumns();
        compareRows();
    }

    private void compareColumns() {
        final Set<String> actualColumns = actual.getHeader().names().collect(toSet());
        final Set<String> expectedColumns = expected.getHeader().names().collect(toSet());

        columnsToCompare = expectedColumns.stream().filter(actualColumns::contains).collect(toSet());
        expectedColumns.stream().filter(c -> ! actualColumns.contains(c)).forEach(comparisonResult::addMissingColumn);
    }

    // TODO handle key columns

    private void compareRows() {
        reportExtraRows();
        reportMissingRows();
        compareCommonRows();
    }

    private void reportExtraRows() {
        for (int rowIdx = expected.numberOfRows(); rowIdx < actual.numberOfRows(); rowIdx++) {
            comparisonResult.addExtraRow(actual.row(rowIdx));
        }
    }

    private void reportMissingRows() {
        for (int rowIdx = actual.numberOfRows(); rowIdx < expected.numberOfRows(); rowIdx++) {
            comparisonResult.addMissingRow(expected.row(rowIdx));
        }
    }

    private void compareCommonRows() {
        int commonRowsSize = Math.min(actual.numberOfRows(), expected.numberOfRows());

        for (int rowIdx = 0; rowIdx < commonRowsSize; rowIdx++) {
            compare(rowIdx, actual.row(rowIdx), expected.row(rowIdx));
        }
    }

    private void compare(int rowIdx, Record actual, Record expected) {
        columnsToCompare.forEach(columnName -> compare(rowIdx, columnName, actual.get(columnName), expected.get(columnName)));
    }

    private void compare(int rowIdx, String columnName, Object actual, Object expected) {
        final EqualComparator ec = EqualComparator.comparator();
        ec.compare(ActualPath.createActualPath(columnName), actual, expected);

        if (ec.areEqual())
            return;

        comparisonResult.addMismatch(rowIdx, rowIdx, columnName, ec.generateMismatchReport());
    }
}
