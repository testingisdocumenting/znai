package com.twosigma.documentation.extensions.diagrams;

import com.twosigma.diagrams.graphviz.GraphvizDiagram;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.include.IncludeContext;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.PluginResult;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mykola
 */
public class GvDiagramIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "gv-diagram";
    }

    @Override
    public void reset(IncludeContext context) {

    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        String diagramId = pluginParams.getFreeParam();
        String diagramPath = pluginParams.getOpts().getRequiredString("diagramPath");
        String gvContent = componentsRegistry.includeResourceResolver().textContent(diagramPath);

        GraphvizDiagram diagram = Graphviz.graphvizEngine.diagramFromGv(diagramId, gvContent);
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("diagram", diagram.toMap());
        props.put("colors", Graphviz.colors);

        return PluginResult.docElement("GraphVizDiagram", props);
    }

    @Override
    public String textForSearch() {
        return null;
    }
}
