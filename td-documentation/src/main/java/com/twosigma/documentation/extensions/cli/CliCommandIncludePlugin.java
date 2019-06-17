package com.twosigma.documentation.extensions.cli;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginParamsOpts;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.search.SearchScore;
import com.twosigma.documentation.search.SearchText;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author mykola
 */
public class CliCommandIncludePlugin implements IncludePlugin {
    private String command;
    private ResourcesResolver resourcesResolver;

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
    public SearchText textForSearch() {
        return SearchScore.HIGH.text(command);
    }

    private String extractCommand(PluginParams pluginParams) {
        String commandAsFreeParam = pluginParams.getFreeParam().trim();
        if (! commandAsFreeParam.isEmpty()) {
            return commandAsFreeParam;
        }

        PluginParamsOpts opts = pluginParams.getOpts();

        String commandFile = opts.get("commandFile", "");
        if (!commandFile.isEmpty()) {
            return resourcesResolver.textContent(commandFile);
        }

        return opts.getRequiredString("command");
    }
}
