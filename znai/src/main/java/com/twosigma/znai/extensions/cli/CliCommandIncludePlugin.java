package com.twosigma.znai.extensions.cli;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.core.ResourcesResolver;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginParamsOpts;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;
import com.twosigma.znai.search.SearchScore;
import com.twosigma.znai.search.SearchText;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

public class CliCommandIncludePlugin implements IncludePlugin {
    private String command;
    private ResourcesResolver resourcesResolver;
    private String commandFile;

    @Override
    public String id() {
        return "cli-command";
    }

    @Override
    public IncludePlugin create() {
        return new CliCommandIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        Set<String> combinedParams = new LinkedHashSet<>(pluginParams.getOpts().getList("paramToHighlight"));

        if (pluginParams.getOpts().has("paramToHighlight")) {
            ConsoleOutputs.out(Color.RED, "cli-command paramToHighlight will be deprecated"); // TODO deprecation warning API
        }

        combinedParams.addAll(pluginParams.getOpts().getList("paramsToHighlight"));

        resourcesResolver = componentsRegistry.resourceResolver();
        LinkedHashMap<String, Object> props = new LinkedHashMap<>();
        command = extractCommand(pluginParams);

        props.put("command", command);
        props.put("paramsToHighlight", combinedParams);

        return PluginResult.docElement("CliCommand", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return commandFile != null && !commandFile.isEmpty() ?
                Stream.of(AuxiliaryFile.builtTime(resourcesResolver.fullPath(commandFile))):
                Stream.empty();
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.HIGH.text(command);
    }

    private String extractCommand(PluginParams pluginParams) {
        String commandAsFreeParam = pluginParams.getFreeParam().trim();
        if (! commandAsFreeParam.isEmpty()) {
            return commandAsFreeParam;
        }

        PluginParamsOpts opts = pluginParams.getOpts();

        commandFile = opts.get("commandFile", "");
        if (!commandFile.isEmpty()) {
            return resourcesResolver.textContent(commandFile);
        }

        return opts.getRequiredString("command");
    }
}
