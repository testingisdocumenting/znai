package com.twosigma.diagrams.graphviz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mykola
 */
public class GraphvizDiagram {
    private String svg;
    private Map<String, List<String>> stylesByNodeId;
    private Map<String, String> shapeSvgByStyleId;
    private Map<String, Boolean> isInvertedTextColorByStyleId;

    public GraphvizDiagram(String svg, Map<String, List<String>> stylesByNodeId,
                           Map<String, String> shapeSvgByStyleId,
                           Map<String, Boolean> isInvertedTextColorByStyleId) {
        this.svg = svg;
        this.stylesByNodeId = stylesByNodeId;
        this.shapeSvgByStyleId = shapeSvgByStyleId;
        this.isInvertedTextColorByStyleId = isInvertedTextColorByStyleId;
    }

    public String getSvg() {
        return svg;
    }

    public Map<String, List<String>> getStylesByNodeId() {
        return stylesByNodeId;
    }

    public Map<String, String> getShapeSvgByStyleId() {
        return shapeSvgByStyleId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("svg", svg);
        result.put("stylesByNodeId", stylesByNodeId);
        result.put("shapeSvgByStyleId", shapeSvgByStyleId);
        result.put("isInvertedTextColorByStyleId", isInvertedTextColorByStyleId);

        return result;
    }
}
