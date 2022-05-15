/*
 * Copyright 2020 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.parser.table;

import org.testingisdocumenting.znai.utils.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class MarkupTableData {
    private final List<Column> header;
    private final List<Row> data;
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public MarkupTableData() {
        header = new ArrayList<>();
        data = new ArrayList<>();
    }

    MarkupTableData(List<Column> header, List<Row> data) {
        this.header = header;
        this.data = data;
    }

    public MarkupTableData mapValues(MarkupTableDataMapping mapping) {
        return new MarkupTableData(header,
                data.stream().map(row -> row.map(mapping)).collect(toList()));
    }

    public Map<?, Object> toKeyValue() {
        Map<String, Object> result = new LinkedHashMap<>();

        if (header.size() != 2) {
            throw new IllegalArgumentException("toKeyValue only works with two columns tables");
        }

        for (Row row : data) {
            Object key = row.get(0);
            Object value = row.get(1);
            Object previous = result.put(key.toString(), value);

            if (previous != null) {
                throw new IllegalArgumentException("non unique key detected: " + key + "\n" +
                        "previous value: " + previous + "\n" +
                        "new value: " + value);
            }
        }

        return result;
    }

    public Stream<String> columnNamesStream() {
        return header.stream().map(Column::getTitle);
    }

    public Stream<Object> allValuesStream() {
        return data.stream().flatMap(row -> row.getData().stream());
    }

    public void forEachRow(Consumer<Row> consumer) {
        data.forEach(consumer);
    }

    public String allText() {
        List<String> textParts = new ArrayList<>();
        columnNamesStream().forEach(textParts::add);
        allValuesStream()
                .filter(Objects::nonNull)
                .forEach(v -> textParts.add(v.toString()));

        return String.join(" ", textParts);
    }

    public MarkupTableData withColumnsInOrder(List<String> columnNames) {
        List<Integer> newIdxOrder = findColumnIdxes(columnNames);

        List<Column> newHeader = newIdxOrder.stream().map(header::get).collect(toList());
        List<Row> newRows = data.stream().map(r -> r.onlyWithIdxs(newIdxOrder)).collect(toList());

        return new MarkupTableData(newHeader, newRows);
    }

    private List<Integer> findColumnIdxes(List<String> columnNames) {
        return columnNames.stream().map(this::findColumnIdx).collect(toList());
    }

    private Integer findColumnIdx(String columnName) {
        for (int idx = 0; idx < header.size(); idx++) {
            if (header.get(idx).getTitle().equalsIgnoreCase(columnName)) {
                return idx;
            }
        }

        throw new RuntimeException("cannot find column: " + columnName);
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

    public List<String> getColumnTitles() {
        return header.stream().map(Column::getTitle).collect(toList());
    }

    public List<List<Object>> getData() {
        return data.stream().map(Row::getData).collect(toList());
    }

    public List<List<Object>> getDataConvertingNumbers() {
        return data.stream()
                .map(row -> {
                    List<Object> rowData = row.getData();

                    return rowData.stream()
                            .map(this::convertToNumberIfPossible)
                            .collect(toList());
                })
                .collect(toList());
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> result = new LinkedHashMap<>();
        result.put("columns", header.stream().map(Column::toMap).collect(toList()));
        result.put("data", getData());

        return result;
    }

    private Object convertToNumberIfPossible(Object value) {
        if (value == null) {
            return null;
        }

        String valueAsText = value.toString();
        if (StringUtils.isNumeric(numberFormat, valueAsText)) {
            try {
                return numberFormat.parse(valueAsText);
            } catch (ParseException e) {
                return "can't parse <" + valueAsText + "> to number";
            }
        }

        return value;
    }
}
