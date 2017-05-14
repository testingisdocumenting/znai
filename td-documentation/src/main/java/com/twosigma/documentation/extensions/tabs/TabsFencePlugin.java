package com.twosigma.documentation.extensions.tabs;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.ColonDelimitedKeyValues;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.fence.FencePlugin;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class TabsFencePlugin implements FencePlugin {
    private Path markupPath;
    private MarkupParser parser;

    @Override
    public String id() {
        return "tabs";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        this.markupPath = markupPath;
        this.parser = componentsRegistry.parser();

        ColonDelimitedKeyValues tabsDefinitions = new ColonDelimitedKeyValues(content);
        Map<String, Object> tabsProps = new LinkedHashMap<>();
        tabsProps.put("tabsContent", tabsDefinitions.map(this::tabProps).collect(toList()));

        return PluginResult.docElement("Tabs", tabsProps);
    }

    private Map<String, Object> tabProps(String tabName, String markup) {
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("name", tabName);
        props.put("content", markupDocElements(markup));

        return props;
    }

    private List<Map<String, Object>> markupDocElements(String markup) {
        MarkupParserResult parserResult = parser.parse(markupPath, markup);
        return parserResult.contentToListOfMaps();
    }
}
