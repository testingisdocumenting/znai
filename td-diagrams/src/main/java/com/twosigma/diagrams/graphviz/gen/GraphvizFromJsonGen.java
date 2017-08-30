package com.twosigma.diagrams.graphviz.gen;

import com.twosigma.utils.JsonUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class GraphvizFromJsonGen {
    private final Map<String, ?> graph;

    public GraphvizFromJsonGen(Map<String, ?> graph) {
        this.graph = graph;
    }

    public String generate() {
        String nodes = generateNodes(getList("nodes"));
        String edges = generateEdges(getList("edges"));

        return "digraph Generated {\n" +
                "rankdir=LR;\n" +
                "node [shape=record; fontsize=10; margin=0.2; fontname=Helvetica];\n" +
                (nodes.isEmpty() ? "" : "\n" + nodes + "\n") +
                (edges.isEmpty() ? "" : "\n" + edges + "\n") +
                "}";
    }

    private String generateNodes(List<Map<String, ?>> nodes) {
        return nodes.stream().map(this::generateNode)
                .collect(Collectors.joining("\n"));
    }

    private String generateNode(Map<String, ?> n) {
        boolean isHighlighted = Boolean.TRUE.equals(n.get("highlight"));

        Object label = n.get("label") + (isHighlighted ? "[h]" : "");
        return n.get("id") + " [label=\"" + label + "\"];";
    }

    private String generateEdges(List<List<String>> edges) {
        return edges.stream().map(this::generateEdge)
                .collect(Collectors.joining("\n"));
    }

    private String generateEdge(List<String> idsAndType) {
        if (idsAndType.size() < 2 || idsAndType.size() > 3) {
            throw new IllegalArgumentException("edges definition should be in the format [from, to, optionalType]" +
                    " (type is either both or none)");
        }

        return idsAndType.get(0) + " -> " + idsAndType.get(1) +
                (idsAndType.size() == 3 ? "[dir=" + idsAndType.get(2) + "];" : ";");
    }

    @SuppressWarnings("unchecked")
    private <E> List<E> getList(String name) {
        List<E> result = (List<E>) graph.get(name);
        return result == null ? Collections.emptyList() : result;
    }
}
