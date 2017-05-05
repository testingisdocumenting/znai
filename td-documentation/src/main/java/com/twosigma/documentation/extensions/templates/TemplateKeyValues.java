package com.twosigma.documentation.extensions.templates;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class TemplateKeyValues {
    private static final Pattern VAR_DEF = Pattern.compile("^\\w+:");
    private final String[] lines;
    private Map<String, String> vars;
    private String currentVarName;
    private List<String> currentValueLines;

    public TemplateKeyValues(String content) {
        lines = content.split("\n");
        vars = new HashMap<>();
        currentVarName = "";
        currentValueLines = new ArrayList<>();
        extractVars();
    }

    public void forEach(BiConsumer<String, String> consumer) {
        vars.forEach(consumer);
    }

    public String get(String name) {
        if (! vars.containsKey(name)) {
            throw new RuntimeException("no value defined for key: " + name);
        }

        return vars.get(name);
    }

    public Map<String, String> toMap() {
        return Collections.unmodifiableMap(vars);
    }

    @Override
    public String toString() {
        return vars.toString();
    }

    private void extractVars() {
        Arrays.stream(lines).forEach(this::processLine);
        flushVar();
    }

    private void processLine(String line) {
        if (VAR_DEF.matcher(line).find()) {
            flushVar();
            int colonIdx = line.indexOf(':');

            currentVarName = line.substring(0, colonIdx);
            currentValueLines.add(line.substring(colonIdx + 1));
        } else {
            currentValueLines.add(line);
        }
    }

    private void flushVar() {
        if (currentVarName.isEmpty()) {
            return;
        }

        vars.put(currentVarName, currentValueLines.stream().collect(Collectors.joining("\n")));

        currentValueLines.clear();
        currentVarName = "";
    }
}
