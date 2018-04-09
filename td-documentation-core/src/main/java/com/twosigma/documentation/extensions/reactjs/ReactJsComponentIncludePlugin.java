package com.twosigma.documentation.extensions.reactjs;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.utils.CollectionUtils;

import java.nio.file.Path;

public class ReactJsComponentIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "reactjs-component";
    }

    @Override
    public IncludePlugin create() {
        return new ReactJsComponentIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        ComponentPath componentPath = extractComponentPath(pluginParams.getFreeParam());

        return PluginResult.docElement("CustomReactJSComponent", CollectionUtils.createMap(
                "namespace", componentPath.namespace,
                "name", componentPath.name,
                "props", pluginParams.getOpts().toMap()));
    }

    private static ComponentPath extractComponentPath(String fullName) {
        String[] parts = fullName.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("component path must be specified as <namespace>.<name>," +
                    " given: " + fullName);
        }

        return new ComponentPath(parts[0], parts[1]);
    }

    private static class ComponentPath {
        private String namespace;
        private String name;

        ComponentPath(String namespace, String name) {
            this.namespace = namespace;
            this.name = name;
        }
    }
}
