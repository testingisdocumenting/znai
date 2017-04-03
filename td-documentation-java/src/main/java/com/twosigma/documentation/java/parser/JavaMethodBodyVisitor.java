package com.twosigma.documentation.java.parser;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.twosigma.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class JavaMethodBodyVisitor extends VoidVisitorAdapter<String> {
    private final List<String> lines;
    private Map<String, String> bodyOnlyByName;
    private Map<String, String> bodyByName;

    public JavaMethodBodyVisitor(String code) {
        lines = Arrays.asList(code.split("\n"));
        bodyOnlyByName = new HashMap<>();
        bodyByName = new HashMap<>();
    }

    public String getDeclaration(String methodName) {
        return bodyByName.get(methodName);
    }

    public String getDeclarationBodyOnly(String methodName) {
        return bodyOnlyByName.get(methodName);
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration, String arg) {
        Range range = methodDeclaration.getRange().orElseGet(() -> new Range(new Position(0, 0), new Position(0, 0)));
        int startLine = range.begin.line - 1;
        int endLine = range.end.line - 1;

        String name = methodDeclaration.getName().getIdentifier();
        String code = lines.subList(startLine, endLine + 1).stream().collect(Collectors.joining("\n"));

        Optional<BlockStmt> body = methodDeclaration.getBody();
        body.ifPresent(blockStmt -> bodyOnlyByName.put(name, removeBracesAndIndent(code)));
        body.ifPresent(blockStmt -> bodyByName.put(name, StringUtils.stripIndentation(code)));
    }

    private String removeBracesAndIndent(String code) {
        int startIdx = code.indexOf('{');
        int endIdx = code.lastIndexOf('}');

        String insideBraces = code.substring(startIdx + 1, endIdx - 1);
        return StringUtils.stripIndentation(insideBraces);
    }
}
