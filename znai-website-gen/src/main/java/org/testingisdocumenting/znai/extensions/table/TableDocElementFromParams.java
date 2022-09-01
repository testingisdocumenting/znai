/*
 * Copyright 2020 znai maintainers
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

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.file.AnchorFeature;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.docelement.DocElementType;
import org.testingisdocumenting.znai.parser.table.*;

import java.nio.file.Path;
import java.util.*;

import static java.util.stream.Collectors.toList;

class TableDocElementFromParams {
    private final PluginParams pluginParams;
    private final MarkupParser parser;
    private final Path fullPath;
    private final AnchorFeature anchorFeature;
    private final MarkupTableDataFromContentAndParams markupTableDataFromContentAndParams;
    private MarkupTableData rearrangedTable;

    TableDocElementFromParams(ComponentsRegistry componentsRegistry,
                              MarkupTableDataFromContentAndParams markupTableDataFromContentAndParams,
                              Path markupParentPath,
                              PluginParams pluginParams,
                              MarkupParser parser,
                              Path fullPath) {
        this.markupTableDataFromContentAndParams = markupTableDataFromContentAndParams;
        anchorFeature = new AnchorFeature(componentsRegistry.docStructure(), markupParentPath, pluginParams);

        this.pluginParams = pluginParams;
        this.parser = parser;
        this.fullPath = fullPath;
    }

    MarkupTableData getRearrangedTable() {
        return rearrangedTable;
    }

    @SuppressWarnings("unchecked")
    PluginResult create() {
        PluginParamsOpts opts = pluginParams.getOpts();
        rearrangedTable = opts.has("columns") ?
                markupTableDataFromContentAndParams.getMarkupTableData().withColumnsInOrder(opts.getList("columns")) :
                markupTableDataFromContentAndParams.getMarkupTableData();

        Map<String, Object> tableAsMap = rearrangedTable.toMap();

        List<Map<String, Object>> columns = (List<Map<String, Object>>) tableAsMap.get("columns");

        opts.forEach((columnName, meta) -> {
            Optional<Map<String, Object>> column = columns.stream().filter(c -> c.get("title").equals(columnName)).findFirst();
            column.ifPresent(c -> c.putAll((Map<? extends String, ?>) meta));
        });

        tableAsMap.put("data", parseMarkupInEachRow((List<List<Object>>) tableAsMap.get("data")));

        opts.assignToProps(tableAsMap, "minColumnWidth");
        opts.assignToProps(tableAsMap, "wide");

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("table", tableAsMap);
        handleHighlight(props);

        opts.assignToProps(props, "title");
        opts.assignToProps(props, "anchorId");

        anchorFeature.updateProps(props);

        return PluginResult.docElement(DocElementType.TABLE, props);
    }

    private void handleHighlight(Map<String, Object> props) {
        List<Object> rows = pluginParams.getOpts().getList("highlightRow");
        if (rows.isEmpty()) {
            return;
        }

        props.put("highlightRowIndexes", rows);
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
}
