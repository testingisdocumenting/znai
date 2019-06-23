package com.twosigma.documentation.diagrams.graphviz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mykola
 */
public class GraphvizDiagram {
    private String id;
    private String svg;
    private Map<String, List<String>> stylesByNodeId;
    private Map<String, Boolean> isInvertedTextColorByStyleId;

    public GraphvizDiagram(String id, String svg, Map<String, List<String>> stylesByNodeId,
                           Map<String, Boolean> isInvertedTextColorByStyleId) {
        this.id = id;
        this.svg = svg;
        this.stylesByNodeId = stylesByNodeId;
        this.isInvertedTextColorByStyleId = isInvertedTextColorByStyleId;
    }

    public String getSvg() {
        return svg;
    }

    public Map<String, List<String>> getStylesByNodeId() {
        return stylesByNodeId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("svg", svg);
        result.put("stylesByNodeId", stylesByNodeId);
        result.put("isInvertedTextColorByStyleId", isInvertedTextColorByStyleId);

        return result;
    }
}
