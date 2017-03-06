package com.twosigma.documentation.extensions.table;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.PluginResult;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class IncludeCsvTable implements IncludePlugin {
    private String fileName;

    @Override
    public String id() {
        return "csv";
    }

    @Override
    @SuppressWarnings("unchecked")
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        fileName = includeParams.getFreeParam();
        String csv = componentsRegistry.includeResourceResolver().textContent(fileName);
        CsvData csvData = CsvParser.parse(csv);

        Map<String, Object> table = csvData.toMap();
        List<Map<String, Object>> columns = (List<Map<String, Object>>) table.get("columns");

        includeParams.getOpts().forEach((cn, meta) -> {
            Optional<Map<String, Object>> column = columns.stream().filter(c -> c.get("title").equals(cn)).findFirst();
            column.ifPresent(c -> c.putAll((Map<? extends String, ?>) meta));
        });

        return PluginResult.reactComponent("SimpleTable", Collections.singletonMap("table", table));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(
                componentsRegistry.includeResourceResolver().fullPath(fileName)));
    }
}
