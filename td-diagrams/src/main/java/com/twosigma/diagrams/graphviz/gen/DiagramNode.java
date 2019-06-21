package com.twosigma.diagrams.graphviz.gen;

public class DiagramNode {
    private final String id;
    private final String label;
    private final String url;
    private final String colorGroup;
    private final Boolean highlight;

    public DiagramNode(String id, String label, String url, String colorGroup, Boolean highlight) {
        this.id = id;
        this.label = label;
        this.url = url;
        this.colorGroup = colorGroup;
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

    public Boolean hasUrl() {
        return !url.isEmpty();
    }

    public String getUrl() {
        return url;
    }
}
