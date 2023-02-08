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
import static org.testingisdocumenting.znai.extensions.table.TablePluginParams.EXCLUDE_ROWS_REGEXP_KEY;
import static org.testingisdocumenting.znai.extensions.table.TablePluginParams.INCLUDE_ROWS_REGEXP_KEY;

class TableDocElementFromParams {
    private final PluginParams pluginParams;
    private final MarkupParser parser;
    private final Path fullPath;
    private final AnchorFeature anchorFeature;
    private final MarkupTableDataFromContentAndParams markupTableDataFromContentAndParams;
    private MarkupTableData modifiedTable;

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

    MarkupTableData getModifiedTable() {
        return modifiedTable;
    }

    @SuppressWarnings("unchecked")
    PluginResult create() {
        PluginParamsOpts opts = pluginParams.getOpts();
        modifiedTable = createModifiedTable(opts);

        Map<String, Object> tableAsMap = modifiedTable.toMap();

        List<Map<String, Object>> columns = (List<Map<String, Object>>) tableAsMap.get("columns");

        opts.forEach((columnName, meta) -> {
            Optional<Map<String, Object>> column = columns.stream().filter(c -> {
                Object columnTitle = c.get("title");
                // we use _ for column names parameters that match plugin parameters (e.g. title)
                return columnName.equals(columnTitle) || columnName.equals("_" + columnTitle);
            }).findFirst();
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
        opts.assignToProps(props, "noGap");
        opts.assignToProps(props, "collapsed");

        anchorFeature.updateProps(props);

        return PluginResult.docElement(DocElementType.TABLE, props);
    }

    private MarkupTableData createModifiedTable(PluginParamsOpts opts) {
        MarkupTableData result = markupTableDataFromContentAndParams.getMarkupTableData();

        if (opts.has("columns")) {
            result = result.withColumnsInOrder(opts.getList("columns"));
        }

        if (opts.has(INCLUDE_ROWS_REGEXP_KEY)) {
            result = result.withRowsMatchingRegexp(opts.getList(INCLUDE_ROWS_REGEXP_KEY));
        }

        if (opts.has(EXCLUDE_ROWS_REGEXP_KEY)) {
            result = result.withoutRowsMatchingRegexp(opts.getList(EXCLUDE_ROWS_REGEXP_KEY));
        }

        return result;
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
