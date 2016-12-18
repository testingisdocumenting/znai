package com.twosigma.testing.data.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Represents a set of rows with named columns to be used as part of test input preparation and/or test output validation
 *
 * @author mykola
 */
public class TableData implements Iterable<Record> {
    private List<Record> rows;
    private Header header;

    public static TableData withHeader(List<String> columnNames) {
        return new TableData(columnNames.stream());
    }

    public static TableData withHeader(Stream<String> columnNames) {
        return new TableData(columnNames);
    }

    public static TableData fromRows(List<Record> rows) {
        return new TableData(rows);
    }

    private TableData(Stream<String> columnNames) {
        this(new Header(columnNames));
    }

    private TableData(Header header) {
        this.header = header;
        this.rows = new ArrayList<>();
    }

    private TableData(List<Record> rows) {
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("rows can't be empty");
        }

        header = rows.get(0).header();
        validateHeaderIsTheSame(rows, header);

        this.rows = new ArrayList<>(rows);
    }

    public Header getHeader() {
        return header;
    }

    public Record row(int rowIdx) {
        validateRowIdx(rowIdx);
        return rows.get(rowIdx);
    }

    public void addRow(List<Object> values) {
        addRow(values.stream());
    }

    public void addRow(Stream<Object> values) {
        Record record = new Record(header, values);
        rows.add(record);
    }

    public TableData map(TableDataCellFunction mapper) {
        TableData mapped = new TableData(header);

        int rowIdx = 0;
        for (Record originalRow : rows) {
            mapped.addRow(mapRow(rowIdx, originalRow, mapper));
            rowIdx++;
        }

        return mapped;
    }

    @SuppressWarnings("unchecked")
    public <T, R> Stream<R> mapColumn(String columnName, Function<T, R> mapper) {
        final int idx = header.columnIdxByName(columnName);
        return rows.stream().map(r -> mapper.apply((T) r.valueByIdx(idx)));
    }

    private void validateRowIdx(final int rowIdx) {
        if (rowIdx < 0 || rowIdx >= numberOfRows())
            throw new IllegalArgumentException("rowIdx is out of range: [0, " + (numberOfRows() - 1) + "]");
    }

    @SuppressWarnings("unchecked")
    private <T, R> Stream<Object> mapRow(final int rowIdx, final Record originalRow, final TableDataCellFunction mapper) {
        return header.columnIdxStream().mapToObj(idx -> mapper.apply(rowIdx, idx, header.columnNameByIdx(idx), originalRow.valueByIdx(idx)));
    }

    private void validateHeader() {
        if (header == null || header.size() == 0)
            throw new IllegalStateException("header is not set");
    }

    public Stream<Record> rowsStream() {
        return rows.stream();
    }

    @Override
    public Iterator<Record> iterator() {
        return rows.iterator();
    }

    public int numberOfRows() {
        return rows.size();
    }

    private void validateHeaderIsTheSame(List<Record> records, Header expectedHeader) {
        if (records.stream().anyMatch(r -> r.header() != expectedHeader)) {
            throw new IllegalStateException("header is not consistent for the records: " + records);
        }
    }
}
