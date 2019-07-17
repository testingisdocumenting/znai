package com.twosigma.znai.diagrams.graphviz;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.diagrams.DiagramsGlobalAssetsRegistration;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

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

        DiagramsGlobalAssetsRegistration.register(componentsRegistry.globalAssetsRegistry());

        GraphvizDiagram diagram = Graphviz.graphvizEngine.diagramFromGv(
                pluginParams.getOpts().get("type", GraphvizEngine.DOT_LAYOUT),
                diagramId, gvContent);
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("diagram", diagram.toMap());

        return PluginResult.docElement("GraphVizDiagram", props);
    }
}
