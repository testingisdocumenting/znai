package com.twosigma.diagrams.graphviz.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class GraphvizNodeShape {
    private String shape;
    private Number width;
    private Number height;

    public GraphvizNodeShape(String shape, Number width, Number height) {
        this.shape = shape;
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

        if (isWidthSet()) {
            attrs.add("width=" + width);
        }

        if (isHeightSet()) {
            attrs.add("height=" + height);
        }

        if (isWidthSet() || isHeightSet()) {
            attrs.add("fixedSize=" + true);
        }

        return attrs.stream().collect(Collectors.joining(","));
    }
}
