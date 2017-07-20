package com.twosigma.documentation.extensions.cli;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;

import java.nio.file.Path;
import java.util.*;

/**
 * @author mykola
 */
public class CliCommandIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "cli-command";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        String paramToHighlight = pluginParams.getOpts().get("paramToHighlight", "");
        List<String> paramsToHighlight = pluginParams.getOpts().get("paramsToHighlight", Collections.emptyList());

        Set<String> combinedParams = new LinkedHashSet<>();
        if (! paramToHighlight.isEmpty()) {
            combinedParams.add(paramToHighlight);
        }

        combinedParams.addAll(paramsToHighlight);

        LinkedHashMap<String, Object> props = new LinkedHashMap<>();
        props.put("command", pluginParams.getFreeParam());
        props.put("paramsToHighlight", combinedParams);

        return PluginResult.docElement("CliCommand", props);
    }
}
