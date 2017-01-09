package com.twosigma.documentation.extensions.diagrams;

import com.twosigma.diagrams.graphviz.GraphvizDiagram;
import com.twosigma.diagrams.graphviz.GraphvizEngine;
import com.twosigma.diagrams.graphviz.InteractiveCmdGraphviz;
import com.twosigma.diagrams.graphviz.meta.GraphvizShapeConfig;
import com.twosigma.documentation.extensions.IncludeContext;
import com.twosigma.documentation.extensions.IncludeParams;
import com.twosigma.documentation.extensions.IncludePlugin;
import com.twosigma.documentation.extensions.ReactComponent;
import com.twosigma.utils.FileUtils;
import com.twosigma.utils.JsonUtils;
import com.twosigma.utils.ResourceUtils;

import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mykola
 */
public class IncludeGvDiagram implements IncludePlugin {
    private final GraphvizEngine graphvizEngine;
    private final Map<String, ?> colors;

    public IncludeGvDiagram() {
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
    public ReactComponent process(IncludeParams includeParams) {
        // TODO resource resolver to be able to specify files like ./in-the-same-dir-as-md
        String path = includeParams.getFreeParam();
        String gvContent = FileUtils.fileTextContent(Paths.get(path));

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
