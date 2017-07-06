package com.twosigma.documentation.extensions.charts;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.table.CsvParser;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class ChartIncludePlugin implements IncludePlugin {
    private Path fullPath;

    @Override
    public String id() {
        return "chart";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        fullPath = componentsRegistry.includeResourceResolver().fullPath(pluginParams.getFreeParam());
        String textContent = componentsRegistry.includeResourceResolver().textContent(fullPath);

        Map<String, Object> data = CsvParser.parse(textContent).toMap();
        return PluginResult.docElement("Chart", data);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath));
    }
}
