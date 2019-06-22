package com.twosigma.documentation.diagrams.graphviz;

import com.twosigma.documentation.diagrams.graphviz.meta.GraphvizDiagramWithMeta;
import com.twosigma.documentation.diagrams.graphviz.meta.GraphvizShapeConfig;

import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * @author mykola
 */
public class GraphvizEngine {
    public static final String DOT_LAYOUT = "dot";

    private final GraphvizShapeConfig shapeConfig;
    private final Map<String, GraphvizRuntime> runtimeByType;

    public GraphvizEngine(GraphvizShapeConfig shapeConfig) {
        this.shapeConfig = shapeConfig;
        this.runtimeByType = new HashMap<>();
    }

    public GraphvizDiagram diagramFromGv(String layoutType, String id, String gv) {
        GraphvizRuntime runtime = findGraphvizRuntime(layoutType);

        GraphvizDiagramWithMeta withMeta = GraphvizDiagramWithMeta.create(shapeConfig, gv);
        String graphSvg = runtime.svgFromGv(withMeta.getPreprocessed());

        Set<String> styles = withMeta.getStylesById().values().stream().flatMap(Collection::stream).collect(toSet());
        Map<String, String> svgByStyle = new LinkedHashMap<>();
        Map<String, Boolean> isInvertedTextColorByStyleId = new LinkedHashMap<>();
        styles.forEach((s) -> {
            Optional<String> shapeSvg = shapeConfig.shapeSvg(s);
            shapeSvg.ifPresent((svg) -> svgByStyle.put(s, svg));

            boolean isInverted = shapeConfig.isInvertedTextColorByStyleId(s);
            if (isInverted) {
                isInvertedTextColorByStyleId.put(s, isInverted);
            }

        });

        return new GraphvizDiagram(id, graphSvg, withMeta.getStylesById(), svgByStyle, isInvertedTextColorByStyleId);
    }

    private GraphvizRuntime findGraphvizRuntime(String layoutType) {
        GraphvizRuntime runtime = runtimeByType.get(layoutType);
        if (runtime == null) {
            throw new RuntimeException("no runtime found for layout type <" + layoutType + ">");
        }
        return runtime;
    }

    public GraphvizEngine registerRuntime(GraphvizRuntime runtime) {
        runtimeByType.put(runtime.getLayoutType(), runtime);
        return this;
    }
}
