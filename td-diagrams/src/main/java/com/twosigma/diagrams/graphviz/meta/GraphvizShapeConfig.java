package com.twosigma.diagrams.graphviz.meta;

import com.twosigma.utils.JsonUtils;

import java.util.Map;
import java.util.Optional;

/**
 * We can assign a node style by adding an extra information to label
 * <pre>
 *     node [label="text [a database]"];
 * </pre>
 *
 * In the example above two styles are set. A style can change a color scheme.
 * Also style can change a shape. Graphviz has predefined shapes that can be specified explicitly.
 * Instead of doing it explicitly though, we will specify it during preprocessing and at the
 * final rendering will replace that shape with a custom svg.
 *
 * So 'database' style may be auto rendered as a SVG showing a data storage. And in order to make all
 * the lines connect properly a shape must be of a proper height, width and if possible, shape.
 *
 * @author mykola
 */
public class GraphvizShapeConfig {
    private Map<String, ?> config;

    public GraphvizShapeConfig(String json) {
        this(JsonUtils.deserializeAsMap(json));
    }
    public GraphvizShapeConfig(Map<String, ?> config) {
        this.config = config;
    }

    public Optional<String> shapeSvg(String style) {
        Map<String, ?> c = getSubMap(style);
        if (c == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(getValue(c, "svg"));
    }

    public Optional<GraphvizNodeShape> nodeShape(String style) {
        Map<String, ?> c = getSubMap(style);
        if (c == null) {
            return Optional.empty();
        }

        String shape = getValue(c, "shape");
        Number width = getValue(c, "width");
        Number height = getValue(c, "height");

        return Optional.of(new GraphvizNodeShape(shape, width, height));
    }

    @SuppressWarnings("unchecked")
    private static <E> E getValue(Map<String, ?> map, String name) {
        return (E) map.get(name);
    }

    @SuppressWarnings("unchecked")
    private Map<String, ?> getSubMap(String style) {
        return (Map<String, ?>) config.get(style);
    }
}
