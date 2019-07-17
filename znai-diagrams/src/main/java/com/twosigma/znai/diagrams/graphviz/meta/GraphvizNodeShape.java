package com.twosigma.znai.diagrams.graphviz.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GraphvizNodeShape {
    private String shape;
    private String labelLoc;
    private Number width;
    private Number height;

    public GraphvizNodeShape(String shape, String labelLoc, Number width, Number height) {
        this.shape = shape;
        this.labelLoc = labelLoc;
        this.width = width;
        this.height = height;
    }

    public String getShape() {
        return shape;
    }

    public Number getWidth() {
        return width;
    }

    public Number getHeight() {
        return height;
    }

    public boolean isShapeSet() {
        return shape != null && !shape.isEmpty();
    }

    public boolean isLabelLocSet() {
        return labelLoc != null && !labelLoc.isEmpty();
    }

    public boolean isWidthSet() {
        return width != null;
    }

    public boolean isHeightSet() {
        return height != null;
    }

    public String asAttrs() {
        List<String> attrs = new ArrayList<>();
        if (isShapeSet()) {
            attrs.add("shape=" + shape);
        }

        if (isLabelLocSet()) {
            attrs.add("labelloc=" + labelLoc);
        }

        if (isWidthSet()) {
            attrs.add("width=" + width);
        }

        if (isHeightSet()) {
            attrs.add("height=" + height);
        }

        if (isWidthSet() || isHeightSet()) {
            attrs.add("fixedsize=true");
        }

        return String.join("; ", attrs);
    }

    @Override
    public String toString() {
        return asAttrs();
    }
}
