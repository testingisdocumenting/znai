package com.twosigma.documentation.extensions.json;

import com.jayway.jsonpath.JsonPath;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.utils.CollectionUtils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class JsonIncludePlugin implements IncludePlugin {
    private String fileName;

    @Override
    public String id() {
        return "json";
    }

    @Override
    public IncludePlugin create() {
        return new JsonIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        fileName = pluginParams.getFreeParam();
        String json = componentsRegistry.resourceResolver().textContent(fileName);

        String jsonPath = pluginParams.getOpts().get("include", "$");
        Object content = JsonPath.read(json, jsonPath);

        Map<String, Object> props = pluginParams.getOpts().toMap();
        props.put("data", content);
        props.put("paths", highlightedPaths(pluginParams.getOpts().get("paths")));

        return PluginResult.docElement("Json", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(
                componentsRegistry.resourceResolver().fullPath(fileName)));
    }

    private List<String> highlightedPaths(String paths) {
        if (paths == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(paths.split(","));
    }
}
