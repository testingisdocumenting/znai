package com.twosigma.documentation.extensions.diagrams;

import com.twosigma.diagrams.graphviz.GraphvizEngine;
import com.twosigma.diagrams.graphviz.InteractiveCmdGraphviz;
import com.twosigma.diagrams.graphviz.meta.GraphvizShapeConfig;
import com.twosigma.utils.JsonUtils;
import com.twosigma.utils.ResourceUtils;

import java.util.Map;

/**
 * @author mykola
 */
public class Graphviz {
    private static final InteractiveCmdGraphviz runtime = new InteractiveCmdGraphviz();

    public static final GraphvizShapeConfig shapeConfig = new GraphvizShapeConfig(ResourceUtils.textContent("graphviz-shapes.json"));
    public static final Map<String, ?> colors = JsonUtils.deserializeAsMap(ResourceUtils.textContent("graphviz-colors.json"));
    public static final GraphvizEngine graphvizEngine = new GraphvizEngine(runtime, shapeConfig);
}
