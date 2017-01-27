package com.twosigma.documentation.extensions.diagrams;

import com.twosigma.diagrams.graphviz.GraphvizDiagram;
import com.twosigma.diagrams.graphviz.GraphvizEngine;
import com.twosigma.diagrams.graphviz.InteractiveCmdGraphviz;
import com.twosigma.diagrams.graphviz.meta.GraphvizShapeConfig;
import com.twosigma.documentation.ComponentsRegistry;
import com.twosigma.documentation.extensions.include.IncludeContext;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.ReactComponent;
import com.twosigma.documentation.extensions.include.IncludeResourcesResolver;
import com.twosigma.utils.FileUtils;
import com.twosigma.utils.JsonUtils;
import com.twosigma.utils.ResourceUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
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
    public ReactComponent process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        String diagramId = includeParams.getFreeParam();
        String diagramPath = includeParams.getOpts().getRequiredString("diagramPath");
        String gvContent = componentsRegistry.includeResourceResolver().textContent(diagramPath);

        GraphvizDiagram diagram = Graphviz.graphvizEngine.diagramFromGv(diagramId, gvContent);
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("diagram", diagram.toMap());
        props.put("colors", Graphviz.colors);

        return new ReactComponent("GraphVizSvg", props);
    }

    @Override
    public String textForSearch() {
        return null;
    }
}
