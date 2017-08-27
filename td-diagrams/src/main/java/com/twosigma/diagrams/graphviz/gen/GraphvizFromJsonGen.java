package com.twosigma.diagrams.graphviz.gen;

import com.twosigma.utils.JsonUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class GraphvizFromJsonGen {
    @SuppressWarnings("unchecked")
    public String generate(String json) {
        Map<String, ?> graph = JsonUtils.deserializeAsMap(json);

        return "digraph Generated {\n" +
                "node [shape=record];\n" +
                "graph [nodesep=1];\n" +
                "\n" + generateNodes((List<Map<String, ?>>) graph.get("nodes")) +
                "\n\n" + generateEdges((List<List<String>>) graph.get("edges")) +
                "\n}";
    }

    private String generateNodes(List<Map<String, ?>> nodes) {
        return nodes.stream().map(n -> n.get("id") + " [label=\"" + n.get("label") + "\"];")
                .collect(Collectors.joining("\n"));
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
}
