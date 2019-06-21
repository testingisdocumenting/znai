package com.twosigma.documentation.extensions.diagrams;

import com.twosigma.diagrams.graphviz.GraphvizDiagram;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.twosigma.diagrams.graphviz.GraphvizEngine.DOT_LAYOUT;

/**
 * @author mykola
 */
public class GvDiagramIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "gv-diagram";
    }

    @Override
    public IncludePlugin create() {
        return new GvDiagramIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        String diagramId = pluginParams.getFreeParam();
        String diagramPath = pluginParams.getOpts().getRequiredString("diagramPath");
        String gvContent = componentsRegistry.resourceResolver().textContent(diagramPath);

        GraphvizDiagram diagram = Graphviz.graphvizEngine.diagramFromGv(
                pluginParams.getOpts().get("type", DOT_LAYOUT),
                diagramId, gvContent);
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("diagram", diagram.toMap());

        return PluginResult.docElement("GraphVizDiagram", props);
    }
}
