package com.twosigma.documentation.extensions.cli;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.search.SearchScore;
import com.twosigma.documentation.search.SearchText;

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
    private List<String> lines;

    @Override
    public String id() {
        return "cli-output";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        filePath = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());

        LinkedHashMap<String, Object> props = new LinkedHashMap<>(pluginParams.getOpts().toMap());
        lines = readLines(componentsRegistry, filePath);
        props.put("lines", lines);
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

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(String.join(" ", lines));
    }
}
