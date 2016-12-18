package com.twosigma.testing.data.table.comparison;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.twosigma.testing.data.table.Record;
import com.twosigma.testing.data.table.TableData;

/**
 * @author mykola
 */
public class TableDataComparisonResult {
    private Map<Integer, Map<String, String>> messageByActualRowIdxAndColumn;
    private Map<Integer, Map<String, String>> messageByExpectedRowIdxAndColumn;

    private Set<String> missingColumns;
    private List<Record> missingRows;
    private List<Record> extraRows;

    private TableData actual;
    private TableData expected;

    public TableDataComparisonResult(TableData actual, TableData expected) {
        this.actual = actual;
        this.expected = expected;

        messageByActualRowIdxAndColumn = new HashMap<>();
        messageByExpectedRowIdxAndColumn = new HashMap<>();

        missingColumns = new TreeSet<>();
        missingRows = new ArrayList<>();
        extraRows = new ArrayList<>();
    }

    public boolean areEqual() {
        return messageByActualRowIdxAndColumn.isEmpty() &&
            missingColumns.isEmpty() &&
            missingRows.isEmpty() &&
            extraRows.isEmpty();
    }

    public TableData getActual() {
        return actual;
    }

    public TableData getExpected() {
        return expected;
    }

    public void addMissingColumn(String name) {
        missingColumns.add(name);
    }

    public void addExtraRow(Record row) {
        extraRows.add(row);
    }

    public void addMissingRow(Record row) {
        missingRows.add(row);
    }

    public Set<String> getMissingColumns() {
        return missingColumns;
    }

    public void setMissingColumns(final Set<String> missingColumns) {
        this.missingColumns = missingColumns;
    }

    public List<Record> getMissingRows() {
        return missingRows;
    }

    public void setMissingRows(final List<Record> missingRows) {
        this.missingRows = missingRows;
    }

    public List<Record> getExtraRows() {
        return extraRows;
    }

    public void setExtraRows(final List<Record> extraRows) {
        this.extraRows = extraRows;
    }

    // TODO keys support
    public void addMismatch(int actualRowIdx, int expectedRowIdx, String columnName, String message) {
        addMismatch(messageByActualRowIdxAndColumn, actualRowIdx, columnName, message);
        addMismatch(messageByExpectedRowIdxAndColumn, expectedRowIdx, columnName, message);
    }

    public String getActualMismatch(int rowIdx, String columnName) {
        return getMismatch(messageByActualRowIdxAndColumn, rowIdx, columnName);
    }

    public String getExpectedMismatch(int rowIdx, String columnName) {
        return getMismatch(messageByExpectedRowIdxAndColumn, rowIdx, columnName);
    }

    private String getMismatch(Map<Integer, Map<String, String>> messagesByRowIdx, int rowIdx, String columnName) {
        Map<String, String> byRow = messagesByRowIdx.get(rowIdx);
        if (byRow == null) {
            return null;
        }

        return byRow.get(columnName);
    }

    private void addMismatch(Map<Integer, Map<String, String>> messagesByRowIdx, int rowIdx, String columnName, String message) {
        Map<String, String> byRow = messagesByRowIdx.get(rowIdx);
        if (byRow == null) {
            byRow = new HashMap<>();
            messagesByRowIdx.put(rowIdx, byRow);
        }

        byRow.put(columnName, message);
    }

}
