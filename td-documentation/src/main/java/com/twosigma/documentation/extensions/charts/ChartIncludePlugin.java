package com.twosigma.documentation.extensions.charts;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.table.CsvParser;
import com.twosigma.documentation.parser.ParserHandler;

import java.nio.file.Path;
import java.util.LinkedHashMap;
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
    public IncludePlugin create() {
        return new ChartIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        fullPath = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());
        String textContent = componentsRegistry.resourceResolver().textContent(fullPath);

        Map<String, Object> table = CsvParser.parse(textContent).toMap();
        Map<String, Object> props = new LinkedHashMap<>(pluginParams.getOpts().toMap());
        props.put("chartType", props.get("type"));
        props.put("data", table.get("data"));

        return PluginResult.docElement("Chart", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath));
    }
}
