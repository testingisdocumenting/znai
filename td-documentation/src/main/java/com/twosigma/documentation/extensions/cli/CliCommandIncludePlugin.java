package com.twosigma.documentation.extensions.cli;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.search.SearchScore;
import com.twosigma.documentation.search.SearchText;

import java.nio.file.Path;
import java.util.*;

/**
 * @author mykola
 */
public class CliCommandIncludePlugin implements IncludePlugin {
    private String command;

    @Override
    public String id() {
        return "cli-command";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        Set<String> combinedParams = new LinkedHashSet<>();
        combinedParams.addAll(pluginParams.getOpts().getList("paramToHighlight"));
        ConsoleOutputs.out(Color.RED, "cli-command paramToHighlight will be deprecated"); // TODO deprecation warning API

        combinedParams.addAll(pluginParams.getOpts().getList("paramsToHighlight"));

        LinkedHashMap<String, Object> props = new LinkedHashMap<>();
        command = pluginParams.getFreeParam();
        props.put("command", command);
        props.put("paramsToHighlight", combinedParams);

        return PluginResult.docElement("CliCommand", props);
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.HIGH.text(command);
    }
}
