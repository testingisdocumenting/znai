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

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.docelement.DocElementType;
import org.testingisdocumenting.znai.parser.table.*;
import org.testingisdocumenting.znai.resources.ResourcesResolver;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class TableDocElementFromParams {
    private final PluginParams pluginParams;
    private final MarkupParser parser;
    private final Path fullPath;
    private final Path mappingPath;
    private final MarkupTableDataMapping tableDataMapping;
    private final String mappingFileName;
    private MarkupTableData rearrangedTable;

    private final ResourcesResolver resourcesResolver;
    private final String content;

    TableDocElementFromParams(PluginParams pluginParams, MarkupParser parser, ResourcesResolver resourcesResolver,
                              Path fullPath, String content) {
        this.pluginParams = pluginParams;
        this.parser = parser;
        this.resourcesResolver = resourcesResolver;
        this.content = content;
        this.fullPath = fullPath;

        this.mappingFileName = pluginParams.getOpts().get("mappingPath", "");
        this.mappingPath = mappingFileName.isEmpty() ? null : resourcesResolver.fullPath(mappingFileName);

        this.tableDataMapping = createMapping();
    }

    MarkupTableData getRearrangedTable() {
        return rearrangedTable;
    }

    Stream<AuxiliaryFile> mappingAuxiliaryFile() {
        return mappingPath == null ? Stream.empty() : Stream.of(AuxiliaryFile.builtTime(mappingPath));
    }

    private MarkupTableDataMapping createMapping() {
        return new MapBasedMarkupTableMapping(mappingPath == null ?
                Collections.emptyMap():
                createMappingFromFileContent());
    }

    private Map<Object, Object> createMappingFromFileContent() {
        MarkupTableData tableData = CsvTableParser.parseWithHeader(resourcesResolver.textContent(mappingFileName),
                "from", "to");
        return Collections.unmodifiableMap(tableData.toKeyValue());
    }

    @SuppressWarnings("unchecked")
    PluginResult create() {
        MarkupTableData tableFromContent = (isJson() ?
                JsonTableParser.parse(content) :
                CsvTableParser.parse(content)).mapValues(tableDataMapping);

        PluginParamsOpts opts = pluginParams.getOpts();
        rearrangedTable = opts.has("columns") ?
                tableFromContent.withColumnsInOrder(opts.getList("columns")) :
                tableFromContent;

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

        opts.assignToProps(props, "title");

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

    private boolean isJson() {
        return content.trim().startsWith("[");
    }
}
