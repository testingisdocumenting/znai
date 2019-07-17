package com.twosigma.znai.diagrams;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginParamsOpts;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;
import com.twosigma.znai.search.SearchScore;
import com.twosigma.znai.search.SearchText;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class DiagramLegendIncludePlugin implements IncludePlugin {
    private static final String CLICKABLE_NODES_NAME = "clickableNodes";
    private Map<String, String> legend;

    @Override
    public String id() {
        return "diagram-legend";
    }

    @Override
    public IncludePlugin create() {
        return new DiagramLegendIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        Boolean clickableNodes = pluginParams.getOpts().get(CLICKABLE_NODES_NAME, false);

        legend = extractLegend(pluginParams.getOpts());
        legend.remove(CLICKABLE_NODES_NAME);

        Map<String, Object> props = new HashMap<>();
        props.put("legend", legend);
        props.put("clickableNodes", clickableNodes);

        return PluginResult.docElement("DiagramLegend", props);
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(String.join(" ", legend.values()));
    }

    private Map<String, String> extractLegend(PluginParamsOpts opts) {
        Map<String, String> result = new HashMap<>();
        opts.forEach((k, v) -> result.put(k, v.toString()));
        result.remove(CLICKABLE_NODES_NAME);

        return result;
    }
}
