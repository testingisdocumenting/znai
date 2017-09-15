package com.twosigma.documentation.extensions.cli;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class CliOutputIncludePlugin implements IncludePlugin {
    private Path filePath;

    @Override
    public String id() {
        return "cli-output";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        filePath = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());

        LinkedHashMap<String, Object> props = new LinkedHashMap<>(pluginParams.getOpts().toMap());
        props.put("lines", readLines(componentsRegistry, filePath));
        props.put("highlight", pluginParams.getOpts().getList("highlight"));

        return PluginResult.docElement("CliOutput", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(filePath));
    }

    private static List<String> readLines(ComponentsRegistry componentsRegistry, Path filePath) {
        return Arrays.asList(componentsRegistry.resourceResolver().textContent(filePath).split("\n"));
    }
}
