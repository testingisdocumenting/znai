package com.twosigma.documentation.cpp;

import com.twosigma.documentation.cpp.parser.EntryDef;
import com.twosigma.utils.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class ClassDefBuilder {
    private String name;
    private int startLine;
    private int endLine;
    private StringBuilder bodyOnly;
    private int nonClosedScopes;
    private List<String> codeLines;

    public ClassDefBuilder(List<String> codeLines) {
        this.codeLines = codeLines;
        bodyOnly = new StringBuilder();
    }

    public void addToBody(String text) {
        bodyOnly.append(text);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public EntryDef build() {
        return new EntryDef(name,
                codeLines.subList(startLine - 1, endLine).stream().collect(Collectors.joining("\n")),
                stripIndentation());
    }

    private String stripIndentation() {
        return StringUtils.stripIndentation(bodyOnly.substring(1, bodyOnly.length() - 1));
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", startLine=" + startLine +
                ", endLine=" + endLine +
                ", bodyOnly=" + bodyOnly;
    }

    public void scopeOpen() {
        nonClosedScopes++;
    }

    public void scopeClose() {
        nonClosedScopes--;
    }

    public boolean isMainScopeClosed() {
        return nonClosedScopes == 0;
    }
}
