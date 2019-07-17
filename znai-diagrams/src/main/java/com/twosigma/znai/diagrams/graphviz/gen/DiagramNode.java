package com.twosigma.znai.diagrams.graphviz.gen;

public class DiagramNode {
    private final String id;
    private final String label;
    private final String url;
    private final String colorGroup;
    private final String shape;
    private final Boolean highlight;

    public DiagramNode(String id, String label, String url, String colorGroup, String shape, Boolean highlight) {
        this.id = id;
        this.label = label;
        this.url = url;
        this.colorGroup = colorGroup;
        this.shape = shape;
        this.highlight = highlight;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Boolean getHighlight() {
        return highlight;
    }

    public String getColorGroup() {
        return colorGroup;
    }

    public String getShape() {
        return shape;
    }

    public Boolean hasUrl() {
        return !url.isEmpty();
    }

    public String getUrl() {
        return url;
    }
}
