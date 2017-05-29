package com.twosigma.documentation.parser.table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class MarkupTableData {
    private List<Column> header;
    private List<Row> data;

    public MarkupTableData() {
        header = new ArrayList<>();
        data = new ArrayList<>();
    }

    public void addColumn(String name) {
        header.add(new Column(name));
    }

    public void addColumn(String name, String align) {
        header.add(new Column(name, align));
    }

    public void addRow(Map<String, ?> rowData) {
        Row row = new Row();
        header.forEach(c -> row.add(rowData.get(c.getTitle())));

        addRow(row);
    }

    public void addRow(Row row) {
        data.add(row);
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> result = new LinkedHashMap<>();
        result.put("columns", header.stream().map(Column::toMap).collect(toList()));
        result.put("data", data.stream().map(Row::getData).collect(toList()));

        return result;
    }
}
