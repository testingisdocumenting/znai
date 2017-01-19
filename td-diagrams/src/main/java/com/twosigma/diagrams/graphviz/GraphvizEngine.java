package com.twosigma.diagrams.graphviz;

import com.twosigma.diagrams.graphviz.meta.GraphvizDiagramWithMeta;
import com.twosigma.diagrams.graphviz.meta.GraphvizShapeConfig;

import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * @author mykola
 */
public class GraphvizEngine {
    private GraphvizRuntime runtime;
    private GraphvizShapeConfig shapeConfig;

    public GraphvizEngine(GraphvizRuntime runtime, GraphvizShapeConfig shapeConfig) {
        this.runtime = runtime;
        this.shapeConfig = shapeConfig;
    }

    public GraphvizDiagram diagramFromGv(String id, String gv) {
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
}
