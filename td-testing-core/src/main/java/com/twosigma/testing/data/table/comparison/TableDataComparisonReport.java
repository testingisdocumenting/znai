package com.twosigma.testing.data.table.comparison;

import com.twosigma.testing.data.table.TableData;
import com.twosigma.testing.data.table.render.TableRenderer;

import static java.util.stream.Collectors.joining;

/**
 * @author mykola
 */
public class TableDataComparisonReport {
    private TableDataComparisonResult result;

    public TableDataComparisonReport(TableDataComparisonResult result) {
        this.result = result;
    }

    public String generate() {
        return missingColumnsReport() +
            "\nactual:\n\n" +
            TableRenderer.render(result.getActual().map(this::mapActualBreaks)) +
            "\nexpected:\n\n" +
            TableRenderer.render(result.getExpected().map(this::mapExpectedBreaks)) +
            missingRowsReport() +
            extraRowsReport();
    }

    private String extraRowsReport() {
        if (result.getExtraRows().isEmpty())
            return "";

        final TableData extra = TableData.fromRows(result.getExtraRows());
        return "\nextra rows:\n" + TableRenderer.render(extra);
    }

    private String missingRowsReport() {
        if (result.getMissingRows().isEmpty())
            return "";

        final TableData extra = TableData.fromRows(result.getMissingRows());
        return "\nmissing rows:\n" + TableRenderer.render(extra);
    }

    private String missingColumnsReport() {
        return result.getMissingColumns().isEmpty() ? "" : "missing columns: " + result.getMissingColumns().stream().collect(joining(", "));
    }

    private Object mapActualBreaks(final int rowIdx, final int colIdx, final String columnName, final Object value) {
        return annotateCellBreak(value, result.getActualMismatch(rowIdx, columnName));
    }

    private Object mapExpectedBreaks(final int rowIdx, final int colIdx, final String columnName, final Object value) {
        return annotateCellBreak(value, result.getExpectedMismatch(rowIdx, columnName));
    }

    private Object annotateCellBreak(final Object value, final String mismatch) {
        return mismatch == null ? value :
            "***\n" + mismatch + "\n***\n\n" + value;
    }
}
