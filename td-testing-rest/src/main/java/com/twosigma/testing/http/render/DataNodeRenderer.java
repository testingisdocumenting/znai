package com.twosigma.testing.http.render;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.twosigma.testing.data.render.DataRenderers;
import com.twosigma.testing.data.traceable.CheckLevel;
import com.twosigma.testing.data.traceable.TraceableValue;
import com.twosigma.testing.http.datanode.DataNode;

import static java.util.stream.Collectors.joining;

/**
 * @author mykola
 */
public class DataNodeRenderer {
    private DataNodeRenderStyle style;

    public DataNodeRenderer(DataNodeRenderStyle style) {
        this.style = style;
    }

    public static String render(DataNode dataNode) {
        final DataNodeRenderer dataNodeRenderer = new DataNodeRenderer(new DataNodeRenderStyle() {
            @Override
            public String wrapChecked(final CheckLevel checkLevel, final Object value) {
                String surrounding = checkLevel == CheckLevel.ExplicitFailed ? "***" : "";
                return surrounding + DataRenderers.render(value) + surrounding;
            }
        });

        return dataNodeRenderer.render(dataNode, 0);
    }

    public String render(DataNode dataNode, int nestLevel) {
        if (dataNode.isList()) {
            return renderList(dataNode.all(), nestLevel);
        } else if (dataNode.isSingleValue()) {
            return renderSingleValue(dataNode.get());
        } else {
            return renderMap(dataNode.asMap(), nestLevel);
        }
    }

    private String renderMap(final Map<String, DataNode> map, final int nestLevel) {
        return "{" + map.entrySet().stream().map(e -> e.getKey() + ": " + render(e.getValue(), nestLevel + 1)).
            collect(joining(",\n" + indent(nestLevel)))  + "}";
    }

    private String renderList(final List<DataNode> list, final int nestLevel) {
        return "[" + list.stream().map(n -> render(n, nestLevel)).collect(Collectors.joining(",")) + "]";
    }

    private String renderSingleValue(final TraceableValue traceableValue) {
        return style.wrapChecked(traceableValue.getCheckLevel(), traceableValue.getValue());
    }

    private static String indent(final int nestLevel) {
        return StringUtils.leftPad(" ", nestLevel * 4);
    }

    private static class Printer {
        private StringBuilder output;
        private Deque<Integer> blockIndentations;
        private int indentation;

        Printer() {
            output = new StringBuilder();
            blockIndentations = new ArrayDeque<>();
        }

        public void openArray() {
            blockIndentations.addLast(indentation);
            startLine();
            append("[");
            indentation += 2;
        }

        public void closeArray() {
            indentation = blockIndentations.removeLast();
            startLine();
            append("]");
        }

        private void startLine() {
            output.append("\n");
            output.append(indent(indentation));
        }

        private void append(String text) {
            output.append(text);
        }
    }
}
