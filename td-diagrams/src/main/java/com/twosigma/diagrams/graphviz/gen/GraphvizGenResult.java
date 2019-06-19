package com.twosigma.diagrams.graphviz.gen;

import java.util.Collection;

public class GraphvizGenResult {
    private final String graphViz;
    private final Collection<DiagramNode> usedNodes;

    public GraphvizGenResult(String graphViz, Collection<DiagramNode> usedNodes) {
        this.graphViz = graphViz;
        this.usedNodes = usedNodes;
    }

    public String getGraphViz() {
        return graphViz;
    }

    public Collection<DiagramNode> getUsedNodes() {
        return usedNodes;
    }
}
