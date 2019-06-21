package com.twosigma.diagrams.graphviz.gen;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class GraphvizFromJsonGen {
    private final Map<String, ?> graph;
    private final Map<String, DiagramNode> nodesFromGraph;
    private final Map<String, DiagramNode> nodesFromLibs;
    private final List<DiagramEdge> edgesFromGraph;
    private final GraphvizGenConfig config;

    public GraphvizFromJsonGen(Map<String, ?> graph, List<List<?>> nodesLibraries, GraphvizGenConfig config) {
        this.graph = graph;
        this.nodesFromGraph = extractNodesFromGraph(graph);
        this.edgesFromGraph = extractEdgesFromGraph(graph);

        this.nodesFromLibs = organizeNodesFromLibs(nodesLibraries);

        this.config = config;
    }

    public GraphvizGenResult generate() {
        Collection<DiagramNode> referencedNodes = findReferencedNodes();
        String nodes = generateNodes(referencedNodes);
        String edges = generateEdges(edgesFromGraph);

        return new GraphvizGenResult("digraph Generated {\n" +
                (!config.isVertical() ? "rankdir=LR;\n" : "") +
                "bgcolor=\"#ffffff00\";\n" +
                "node [shape=record; fontsize=10; margin=0.2; fontname=Helvetica];\n" +
                (nodes.isEmpty() ? "" : "\n" + nodes + "\n") +
                (edges.isEmpty() ? "" : "\n" + edges + "\n") +
                "}", referencedNodes);
    }

    private Collection<DiagramNode> findReferencedNodes() {
        Map<String, DiagramNode> result = new LinkedHashMap<>();

        Consumer<String> handleNodeId = (id) -> {
            if (result.containsKey(id)) {
                return;
            }

            if (nodesFromGraph.containsKey(id)) {
                result.put(id, nodesFromGraph.get(id));
            }

            if (nodesFromLibs.containsKey(id)) {
                result.put(id, nodesFromLibs.get(id));
            }
        };

        edgesFromGraph.forEach(edge -> {
            handleNodeId.accept(edge.getFromId());
            handleNodeId.accept(edge.getToId());
        });

        return result.values();
    }

    @SuppressWarnings("unchecked")
    private List<DiagramEdge> extractEdgesFromGraph(Map<String, ?> graph) {
        List<List<String>> edges = (List<List<String>>) graph.get("edges");
        if (edges == null) {
            throw new RuntimeException("edges are not specified");
        }

        return edges.stream().map(this::createEdgeFromMap).collect(toList());
    }

    private DiagramEdge createEdgeFromMap(List<String> edge) {
        if (edge.size() < 2 || edge.size() > 3) {
            throw new IllegalArgumentException("edges definition should be in the format [from, to, optionalType]" +
                    " (type is either both or none)");
        }

        return new DiagramEdge(edge.get(0), edge.get(1), edge.size() == 3 ? edge.get(2) : "");
    }

    @SuppressWarnings("unchecked")
    private Map<String, DiagramNode> extractNodesFromGraph(Map<String, ?> graph) {
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) graph.get("nodes");
        if (nodes == null) {
            return Collections.emptyMap();
        }

        return groupNodesById(nodes);
    }

    @SuppressWarnings("unchecked")
    private Map<String, DiagramNode> organizeNodesFromLibs(List<List<?>> nodesLibraries) {
        Map<String, DiagramNode> result = new LinkedHashMap<>();
        nodesLibraries.forEach(nodes -> result.putAll(groupNodesById((List<Map<String, Object>>) nodes)));

        return result;
    }

    private Map<String, DiagramNode> groupNodesById(List<Map<String, Object>> nodes) {
        return nodes.stream().collect(Collectors.toMap(n -> n.get("id").toString(), GraphvizFromJsonGen::createNode));
    }

    private static DiagramNode createNode(Map<String, Object> node) {
        Object id = node.get("id");
        if (id == null) {
            throw new RuntimeException("node id must be specified: " + node);
        }

        return new DiagramNode(id.toString(),
                node.getOrDefault("label", id).toString(),
                node.getOrDefault("url", "").toString(),
                node.getOrDefault("colorGroup", "").toString(),
                Boolean.TRUE.equals(node.getOrDefault("highlight", "")));
    }

    private String generateNodes(Collection<DiagramNode> nodes) {
        return nodes.stream().map(this::generateNode)
                .collect(Collectors.joining("\n"));
    }

    private String generateNode(DiagramNode node) {
        return node.getId() + " [label=\"" + generateNodeLabel(node) + "\"];";
    }

    private String generateNodeLabel(DiagramNode node) {
        String labelSuffix = node.getHighlight() ? "[h]" :
            !node.getColorGroup().isEmpty() ? "[" + node.getColorGroup() + "]" : "";

        return preProcessLabel(node.getLabel()) + labelSuffix;
    }

    private String generateEdges(List<DiagramEdge> edges) {
        return edges.stream().map(this::generateEdge)
                .collect(Collectors.joining("\n"));
    }

    private String generateEdge(DiagramEdge edge) {
        return edge.getFromId() + " -> " + edge.getToId() +
                (!edge.getDirection().isEmpty() ? "[dir=" + edge.getDirection() + "];" : ";");
    }

    private String preProcessLabel(String label) {
        return label.replace("\n", "\\n");
    }
}
