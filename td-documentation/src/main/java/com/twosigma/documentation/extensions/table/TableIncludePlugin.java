package com.twosigma.documentation.extensions.table;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.parser.docelement.DocElementType;
import com.twosigma.documentation.parser.table.MarkupTableData;
import com.twosigma.documentation.search.SearchScore;
import com.twosigma.documentation.search.SearchText;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
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

        return PluginResult.docElement(DocElementType.TABLE, Collections.singletonMap("table", tableAsMap));
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
