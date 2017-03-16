package com.twosigma.documentation.extensions.tabs;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.Plugins;
import com.twosigma.documentation.extensions.fence.FencePlugin;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.include.IncludePluginParser;
import com.twosigma.documentation.parser.docelement.DocElement;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class TabsFencePlugin implements FencePlugin {
    private ComponentsRegistry componentsRegistry;
    private Path markupPath;

    @Override
    public String id() {
        return "tabs";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, String content) {
        this.componentsRegistry = componentsRegistry;
        this.markupPath = markupPath;

        String[] tabsDefs = content.split("\n");
        Map<String, Object> tabsProps = new LinkedHashMap<>();
        tabsProps.put("tabsContent", Arrays.stream(tabsDefs).map(this::tabProps).collect(toList()));

        return PluginResult.docElement("Tabs", tabsProps);
    }

    private Map<String, Object> tabProps(String tabDef) {
        int colonIdx = tabDef.indexOf(':');

        if (colonIdx == -1) {
            throw new RuntimeException("expect tab content to be\ntabName:include-plugin: params");
        }

        String tabName = tabDef.substring(0, colonIdx).trim();
        String pluginDef = tabDef.substring(colonIdx).trim();

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("name", tabName);
        props.put("content", contentFromPluginId(pluginDef));

        return props;
    }

    private Object contentFromPluginId(String pluginDef) {
        IncludeParams includeParams = IncludePluginParser.parse(pluginDef);

        IncludePlugin includePlugin = Plugins.includePluginById(includeParams.getId());
        PluginResult result = includePlugin.process(componentsRegistry, markupPath, includeParams);

        return result.getDocElements().stream().map(DocElement::toMap).collect(toList());
    }
}
