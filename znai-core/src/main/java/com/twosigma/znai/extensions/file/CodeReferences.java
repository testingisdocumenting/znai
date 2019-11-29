package com.twosigma.znai.extensions.file;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.parser.table.CsvParser;
import com.twosigma.znai.parser.table.MarkupTableData;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * common code for the include-file plugin, include-java plugin, etc
 * to handle parsing of code references, converting them to props, and handling auxiliary files.
 *
 * sort of like a plugin trait. Maybe a good idea to formalize traits on plugins interface level.
 */
public class CodeReferences {
    private final ComponentsRegistry componentsRegistry;

    private final Path referencesPath;
    private final String referencesPathValue;

    public CodeReferences(ComponentsRegistry componentsRegistry, PluginParams pluginParams) {
        this.componentsRegistry = componentsRegistry;

        this.referencesPathValue = pluginParams.getOpts().get("referencesPath", null);
        this.referencesPath = referencesPathValue != null ?
                componentsRegistry.resourceResolver().fullPath(referencesPathValue):
                null;
    }

    public void updateProps(Map<String, Object> props) {
        if (!referencesProvided()) {
            return;
        }

        props.put("references", buildReferences());
    }

    private Map<String, Object> buildReferences() {
        MarkupTableData tableData = CsvParser.parseWithHeader(
                componentsRegistry.resourceResolver().textContent(referencesPathValue),
                "reference", "url");

        Map<String, Object> result = new LinkedHashMap<>();
        tableData.forEachRow(row -> {
            result.put(row.get(0), Collections.singletonMap("pageUrl", row.get(1)));
        });

        return result;
    }

    public Stream<AuxiliaryFile> auxiliaryFiles() {
        return referencesProvided() ?
                Stream.of(AuxiliaryFile.builtTime(referencesPath)) : Stream.empty();
    }

    private boolean referencesProvided() {
        return referencesPath != null;
    }
}
