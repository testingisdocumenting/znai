/*
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

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class MarkupTableData {
    private List<Column> header;
    private List<Row> data;

    public MarkupTableData() {
        header = new ArrayList<>();
        data = new ArrayList<>();
    }

    MarkupTableData(List<Column> header, List<Row> data) {
        this.header = header;
        this.data = data;
    }

    public Stream<String> columnNamesStream() {
        return header.stream().map(Column::getTitle);
    }

    public Stream<Object> allValuesStream() {
        return data.stream().flatMap(row -> row.getData().stream());
    }

    public <E> Stream<E> mapRows(Function<Row, E> converter) {
        return data.stream().map(converter);
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
            if (header.get(idx).getTitle().toLowerCase().equals(columnName.toLowerCase())) {
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

    public Map<String, Object> toMap() {
        final Map<String, Object> result = new LinkedHashMap<>();
        result.put("columns", header.stream().map(Column::toMap).collect(toList()));
        result.put("data", data.stream().map(Row::getData).collect(toList()));

        return result;
    }
}
