package com.twosigma.documentation.extensions.table;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.parser.docelement.DocElementType;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class TableIncludePlugin implements IncludePlugin {
    private String fileName;
    private String textContent;

    @Override
    public String id() {
        return "table";
    }

    @Override
    @SuppressWarnings("unchecked")
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        fileName = includeParams.getFreeParam();
        textContent = componentsRegistry.includeResourceResolver().textContent(fileName);

        Map<String, Object> table = (isJson() ? tableFromJson() : CsvParser.parse(textContent)).toMap();
        List<Map<String, Object>> columns = (List<Map<String, Object>>) table.get("columns");

        includeParams.getOpts().forEach((columnName, meta) -> {
            Optional<Map<String, Object>> column = columns.stream().filter(c -> c.get("title").equals(columnName)).findFirst();
            column.ifPresent(c -> c.putAll((Map<? extends String, ?>) meta));
        });

        return PluginResult.docElement(DocElementType.TABLE, Collections.singletonMap("table", table));
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
        return Stream.of(AuxiliaryFile.builtTime(
                componentsRegistry.includeResourceResolver().fullPath(fileName)));
    }
}
