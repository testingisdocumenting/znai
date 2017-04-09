package com.twosigma.documentation.extensions.table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
class PluginTableData {
    private List<Column> header;
    private List<Row> data;

    PluginTableData() {
        header = new ArrayList<>();
        data = new ArrayList<>();
    }

    void addColumn(String name) {
        header.add(new Column(name));
    }

    void addRow(Map<String, ?> rowData) {
        Row row = new Row();
        header.forEach(c -> row.add(rowData.get(c.getTitle())));

        addRow(row);
    }

    void addRow(Row row) {
        data.add(row);
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> result = new LinkedHashMap<>();
        result.put("columns", header.stream().map(Column::toMap).collect(toList()));
        result.put("data", data.stream().map(Row::getData).collect(toList()));

        return result;
    }
}
