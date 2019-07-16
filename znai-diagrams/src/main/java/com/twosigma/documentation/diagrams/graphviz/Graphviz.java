package com.twosigma.documentation.diagrams.graphviz;

import com.twosigma.documentation.diagrams.graphviz.meta.GraphvizShapeConfig;
import com.twosigma.utils.ResourceUtils;

public class Graphviz {
    public static final GraphvizShapeConfig shapeConfig =
            new GraphvizShapeConfig(ResourceUtils.textContent("graphviz-shapes.json"));

    public static final GraphvizEngine graphvizEngine = new GraphvizEngine(shapeConfig)
            .registerRuntime(new InteractiveCmdGraphviz("dot"))
            .registerRuntime(new InteractiveCmdGraphviz("neato"));
}
