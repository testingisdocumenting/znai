package com.twosigma.documentation.extensions.cli;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginParamsOpts;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.search.SearchScore;
import com.twosigma.documentation.search.SearchText;
import com.twosigma.utils.NumberUtils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class CliOutputIncludePlugin implements IncludePlugin {
    private Path filePath;
    private List<String> lines;
    private ResourcesResolver resourcesResolver;

    @Override
    public String id() {
        return "cli-output";
    }

    @Override
    public IncludePlugin create() {
        return new CliOutputIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        resourcesResolver = componentsRegistry.resourceResolver();
        filePath = resourcesResolver.fullPath(pluginParams.getFreeParam());

        LinkedHashMap<String, Object> props = new LinkedHashMap<>(pluginParams.getOpts().toMap());
        lines = readLines(componentsRegistry, filePath);
        props.put("lines", lines);
        props.put("highlight", findHighlightIndexes(pluginParams.getOpts()));

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

    private List<Object> findHighlightIndexes(PluginParamsOpts opts) {
        List<Object> list = opts.has("highlightFile") ?
                readListFromFile(opts.getString("highlightFile")):
                opts.getList("highlight");

        return list.stream().flatMap(this::findIndexes).collect(Collectors.toList());
    }

    private List<Object> readListFromFile(String file) {
        return Arrays.stream(resourcesResolver.textContent(file).split("\n")).collect(toList());
    }

    private Stream<Object> findIndexes(Object numberOrText) {
        if (NumberUtils.isInteger(numberOrText.toString())) {
            return Stream.of(numberOrText);
        }

        List<Object> foundIndexes = IntStream.range(0, lines.size())
                .filter(idx -> lines.get(idx).contains(numberOrText.toString()))
                .boxed()
                .collect(toList());

        if (foundIndexes.isEmpty()) {
            throw new RuntimeException("can't find line: " + numberOrText);
        }

        return foundIndexes.stream();
    }
}
