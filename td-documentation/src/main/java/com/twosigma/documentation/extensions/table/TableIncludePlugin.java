package com.twosigma.documentation.extensions.table;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.docelement.DocElementType;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class TableIncludePlugin implements IncludePlugin {
    private String fileName;
    private String textContent;
    private MarkupParser parser;
    private Path fullPath;

    @Override
    public String id() {
        return "table";
    }

    @Override
    @SuppressWarnings("unchecked")
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        parser = componentsRegistry.parser();
        String fileName = pluginParams.getFreeParam();
        fullPath = componentsRegistry.includeResourceResolver().fullPath(fileName);
        textContent = componentsRegistry.includeResourceResolver().textContent(fileName);

        Map<String, Object> table = (isJson() ? tableFromJson() : CsvParser.parse(textContent)).toMap();
        List<Map<String, Object>> columns = (List<Map<String, Object>>) table.get("columns");

        pluginParams.getOpts().forEach((columnName, meta) -> {
            Optional<Map<String, Object>> column = columns.stream().filter(c -> c.get("title").equals(columnName)).findFirst();
            column.ifPresent(c -> c.putAll((Map<? extends String, ?>) meta));
        });

        table.put("data", parseMarkupInEachRow((List<List<Object>>) table.get("data")));

        return PluginResult.docElement(DocElementType.TABLE, Collections.singletonMap("table", table));
    }

    private List<Object> parseMarkupInEachRow(List<List<Object>> rows) {
        return rows.stream().map(this::parseMarkupInCell).collect(toList());
    }

    private List<Object> parseMarkupInCell(List<Object> row) {
        return row.stream().map(this::parseMarkupInCell).collect(toList());
    }

    @SuppressWarnings("unchecked")
    private List<Object> parseMarkupInCell(Object cell) {
        MarkupParserResult parserResult = parser.parse(fullPath, cell.toString());
        return (List<Object>) parserResult.getDocElement().toMap().get("content");
    }

    @SuppressWarnings("unchecked")
    private PluginTableData tableFromJson() {
        PluginTableData tableData = new PluginTableData();

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
}
