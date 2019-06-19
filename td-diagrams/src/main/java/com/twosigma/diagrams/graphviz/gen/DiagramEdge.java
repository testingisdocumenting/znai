package com.twosigma.diagrams.graphviz.gen;

class DiagramEdge {
    private final String fromId;
    private final String toId;
    private final String direction;

    public DiagramEdge(String fromId, String toId, String direction) {
        this.fromId = fromId;
        this.toId = toId;
        this.direction = direction;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public String getDirection() {
        return direction;
    }
}
