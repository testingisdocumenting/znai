package com.twosigma.testing.http.render;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.console.ansi.FontStyle;
import com.twosigma.testing.data.traceable.TraceableValue;
import com.twosigma.testing.http.datanode.DataNode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author mykola
 */
public class DataNodeAnsiPrinter {
    private static final Color SCOPE_CHAR_COLOR = Color.CYAN;
    private static final Color DELIMITER_COLOR = Color.CYAN;
    private static final Color STRING_COLOR = Color.GREEN;
    private static final Color KEY_COLOR = Color.PURPLE;

    private static final Object[] PASS_STYLE = new Object[]{FontStyle.BOLD, Color.GREEN};
    private static final Object[] FAIL_STYLE = new Object[]{FontStyle.BOLD, Color.RED};
    private static final Object[] NO_STYLE = new Object[]{};

    private List<Line> lines;
    private Line currentLine;
    private int currentIndentLevel;

    public DataNodeAnsiPrinter() {
    }

    public void print(DataNode dataNode) {
        lines = new ArrayList<>();
        currentLine = new Line();
        lines.add(currentLine);

        printNode(dataNode);

        lines.forEach(l -> {
            ConsoleOutputs.out(l.getStyleAndValues().toArray());
        });
    }

    private void printNode(DataNode dataNode) {
        if (dataNode.isList()) {
            printList(dataNode);
        } else if (dataNode.isSingleValue()) {
            printSingle(dataNode);
        } else {
            printObject(dataNode);
        }
    }

    private void printObject(DataNode dataNode) {
        Map<String, DataNode> children = dataNode.asMap();

        printlnScopeOpen("{");

        int i = 0;
        for (Map.Entry<String, DataNode> entry : children.entrySet()) {
            String k = entry.getKey();
            DataNode v = entry.getValue();

            boolean isLast = i == children.size() - 1;

            if (v.isSingleValue()) {
                printSingleKeyValue(k, v);
                if (isLast) {
                    println();
                }
            } else if (v.isList()) {
                print(indentation());
                printSingleKey(k);
                println();
                printNode(v);
            } else {
                print(indentation());
                printSingleKey(k);
                println();
                printNode(v);
            }

            if (! isLast) {
                if (! v.isSingleValue()) {
                    print(indentation());
                }
                printDelimiter(",");
                println();
            }

            i++;
        }

        printlnScopeClose("}");
    }

    private void printList(DataNode dataNode) {
        printlnScopeOpen("[");

        int i = 0;
        int size = dataNode.all().size();
        for (DataNode n : dataNode.all()) {
            if (n.isSingleValue()) {
                print(indentation());
            }
            printNode(n);
            if (!n.isSingleValue()) {
                print(indentation());
            }

            boolean isLast = i == size - 1;
            if (! isLast) {
                printDelimiter(",");
            }
            println();

            i++;
        }

        printlnScopeClose("]");
    }

    private void printSingle(DataNode dataNode) {
        TraceableValue traceableValue = dataNode.get();

        Object value = traceableValue.getValue();
        if (value instanceof String) {
            print(STRING_COLOR);
        }

        print(valueStyle(traceableValue));
        print(convertToString(traceableValue));
    }

    private String convertToString(TraceableValue traceableValue) {
        switch (traceableValue.getCheckLevel()) {
            case FuzzyFailed:
            case ExplicitFailed:
                return "**" + convertToString(traceableValue.getValue()) + "**";
            case ExplicitPassed:
                return "__" + convertToString(traceableValue.getValue()) + "__";
            case FuzzyPassed:
                return "~~" + convertToString(traceableValue.getValue()) + "~~";
            default:
                return convertToString(traceableValue.getValue());
        }
    }

    private String convertToString(Object value) {
        return value instanceof String ?
                "\"" + value + "\"":
                value.toString();
    }

    private Object[] valueStyle(TraceableValue traceableValue) {
        switch (traceableValue.getCheckLevel()) {
            case FuzzyFailed:
            case ExplicitFailed:
                return FAIL_STYLE;
            case FuzzyPassed:
            case ExplicitPassed:
                return PASS_STYLE;
            default:
                return NO_STYLE;
        }
    }

    private void printSingleKeyValue(String k, DataNode v) {
        print(indentation());
        printSingleKey(k);
        printSingle(v);
    }

    private void printSingleKey(String k) {
        print(KEY_COLOR, "\"" + k + "\"", ": ");
    }

    private void printDelimiter(String d) {
        print(DELIMITER_COLOR, d);
    }

    private void printScopeOpen(String scopeChar) {
        print(indentation(), SCOPE_CHAR_COLOR, scopeChar);
        currentIndentLevel++;
    }

    private void printlnScopeOpen(String scopeChar) {
        printScopeOpen(scopeChar);
        println();
    }

    private void printScopeClose(String scopeChar) {
        currentIndentLevel--;
        print(indentation(), Color.CYAN, scopeChar);
    }

    private void printlnScopeClose(String scopeChar) {
        printScopeClose(scopeChar);
        println();
    }

    private void print(Object... styleAndValues) {
        currentLine.append(styleAndValues);
    }

    private void println(Object... styleAndValues) {
        print(styleAndValues);
        currentLine = new Line();
        lines.add(currentLine);
    }

    private String indentation() {
        return indent(currentIndentLevel);
    }

    private static String indent(final int nestLevel) {
        return StringUtils.leftPad(" ", nestLevel * 2);
    }

    private static class Line {
        private List<Object> styleAndValues = new ArrayList<>();

        public void append(Object... styleAndValues) {
            this.styleAndValues.addAll(Arrays.asList(styleAndValues));
        }

        public List<Object> getStyleAndValues() {
            return styleAndValues;
        }
    }
}
