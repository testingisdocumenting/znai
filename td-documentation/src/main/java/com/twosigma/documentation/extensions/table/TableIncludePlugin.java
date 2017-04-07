package com.twosigma.documentation.extensions.table;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.parser.docelement.DocElementType;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class TableIncludePlugin implements IncludePlugin {
    private String fileName;

    @Override
    public String id() {
        return "table";
    }

    @Override
    @SuppressWarnings("unchecked")
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        fileName = includeParams.getFreeParam();

        Map<String, Object> table = tableFromCsv(componentsRegistry);
        List<Map<String, Object>> columns = (List<Map<String, Object>>) table.get("columns");

        includeParams.getOpts().forEach((columnName, meta) -> {
            Optional<Map<String, Object>> column = columns.stream().filter(c -> c.get("title").equals(columnName)).findFirst();
            column.ifPresent(c -> c.putAll((Map<? extends String, ?>) meta));
        });

        return PluginResult.docElement(DocElementType.TABLE, Collections.singletonMap("table", table));
    }

    private Map<String, Object> tableFromCsv(ComponentsRegistry componentsRegistry) {
        String csv = componentsRegistry.includeResourceResolver().textContent(fileName);
        CsvData csvData = CsvParser.parse(csv);

        return csvData.toMap();
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(
                componentsRegistry.includeResourceResolver().fullPath(fileName)));
    }
}
