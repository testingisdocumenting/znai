package com.twosigma.documentation.extensions.json;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.utils.CollectionUtils;
import com.twosigma.utils.JsonUtils;

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
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        fileName = pluginParams.getFreeParam();
        String json = componentsRegistry.includeResourceResolver().textContent(fileName);

        Map<String, Object> props = CollectionUtils.createMap("data", JsonUtils.deserialize(json),
                "paths", highlightedPaths(pluginParams.getOpts().get("paths")));

        return PluginResult.docElement("Json", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(
                componentsRegistry.includeResourceResolver().fullPath(fileName)));
    }

    private List<String> highlightedPaths(String paths) {
        if (paths == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(paths.split(","));
    }
}
