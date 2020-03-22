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

package org.testingisdocumenting.znai.extensions.table;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.docelement.DocElementType;
import org.testingisdocumenting.znai.parser.table.CsvParser;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class TableIncludePlugin implements IncludePlugin {
    private String textContent;
    private MarkupParser parser;
    private Path fullPath;
    private MarkupTableData rearrangedTable;

    @Override
    public String id() {
        return "table";
    }

    @Override
    public IncludePlugin create() {
        return new TableIncludePlugin();
    }

    @Override
    @SuppressWarnings("unchecked")
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        parser = componentsRegistry.defaultParser();
        String fileName = pluginParams.getFreeParam();
        fullPath = componentsRegistry.resourceResolver().fullPath(fileName);
        textContent = componentsRegistry.resourceResolver().textContent(fileName);

        MarkupTableData tableFromFile = isJson() ? tableFromJson() : CsvParser.parse(textContent);
        rearrangedTable = pluginParams.getOpts().has("columns") ?
                tableFromFile.withColumnsInOrder(pluginParams.getOpts().getList("columns")) :
                tableFromFile;

        Map<String, Object> tableAsMap = rearrangedTable.toMap();

        List<Map<String, Object>> columns = (List<Map<String, Object>>) tableAsMap.get("columns");

        pluginParams.getOpts().forEach((columnName, meta) -> {
            Optional<Map<String, Object>> column = columns.stream().filter(c -> c.get("title").equals(columnName)).findFirst();
            column.ifPresent(c -> c.putAll((Map<? extends String, ?>) meta));
        });

        tableAsMap.put("data", parseMarkupInEachRow((List<List<Object>>) tableAsMap.get("data")));

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("table", tableAsMap);

        if (pluginParams.getOpts().has("title")) {
            props.put("title", pluginParams.getOpts().get("title"));
        }

        return PluginResult.docElement(DocElementType.TABLE, props);
    }

    private List<Object> parseMarkupInEachRow(List<List<Object>> rows) {
        return rows.stream().map(this::parseMarkupInCell).collect(toList());
    }

    private List<Object> parseMarkupInCell(List<Object> row) {
        return row.stream().map(this::parseMarkupInCell).collect(toList());
    }

    @SuppressWarnings("unchecked")
    private List<Object> parseMarkupInCell(Object cell) {
        if (cell == null) {
            return Collections.emptyList();
        }

        MarkupParserResult parserResult = parser.parse(fullPath, cell.toString());
        return (List<Object>) parserResult.getDocElement().toMap().get("content");
    }

    @SuppressWarnings("unchecked")
    private MarkupTableData tableFromJson() {
        MarkupTableData tableData = new MarkupTableData();

        List<Map<String, ?>> rows = (List<Map<String, ?>>) JsonUtils.deserializeAsList(textContent);
        if (rows.isEmpty()) {
            return tableData;
        }

        Map<String, ?> firstRowData = rows.get(0);

        firstRowData.keySet().forEach(tableData::addColumn);
        rows.forEach(tableData::addRow);

        return tableData;
    }

    private boolean isJson() {
        return textContent.trim().startsWith("[");
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath));
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(rearrangedTable.allText());
    }
}
