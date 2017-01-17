package com.twosigma.documentation.extensions.diagrams;

import com.twosigma.diagrams.graphviz.GraphvizDiagram;
import com.twosigma.diagrams.graphviz.GraphvizEngine;
import com.twosigma.diagrams.graphviz.InteractiveCmdGraphviz;
import com.twosigma.diagrams.graphviz.meta.GraphvizShapeConfig;
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
    private final GraphvizEngine graphvizEngine;
    private final Map<String, ?> colors;

    public GvDiagramIncludePlugin() {
        GraphvizShapeConfig shapeConfig = new GraphvizShapeConfig(ResourceUtils.textContent("graphviz-shapes.json"));
        InteractiveCmdGraphviz runtime = new InteractiveCmdGraphviz();

        colors = JsonUtils.deserializeAsMap(ResourceUtils.textContent("graphviz-colors.json"));
        // TODO global config? this constructor is called by Service Loader

        graphvizEngine = new GraphvizEngine(runtime, shapeConfig);
    }

    @Override
    public String id() {
        return "gv-diagram";
    }

    @Override
    public void reset(IncludeContext context) {

    }

    @Override
    public ReactComponent process(IncludeResourcesResolver resourcesResolver, IncludeParams includeParams) {
        String gvContent = resourcesResolver.textContent(includeParams.getFreeParam());

        GraphvizDiagram diagram = graphvizEngine.diagramFromGv(gvContent);
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("diagram", diagram.toMap());
        props.put("colors", colors);

        return new ReactComponent("GraphVizSvg", props);
    }

    @Override
    public String textForSearch() {
        return null;
    }
}
