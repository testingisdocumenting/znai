package com.twosigma.documentation.java.parser;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.observer.AstObserver;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.twosigma.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class JavaFuncBodyVisitor extends VoidVisitorAdapter<String> {
    private final List<String> lines;
    private Map<String, String> bodyByName;

    public JavaFuncBodyVisitor(String code) {
        lines = Arrays.asList(code.split("\n"));
        bodyByName = new HashMap<>();
    }

    public String getDeclaration(String methodName) {
        return bodyByName.get(methodName);
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration, String arg) {
        Range range = methodDeclaration.getRange().orElseGet(() -> new Range(new Position(0, 0), new Position(0, 0)));
        int startLine = range.begin.line;
        int endLine = range.end.line;

        String name = methodDeclaration.getName().getIdentifier();
        String code = lines.subList(startLine, endLine + 1).stream().collect(Collectors.joining("\n"));

        Optional<BlockStmt> body = methodDeclaration.getBody();
        body.ifPresent(blockStmt -> bodyByName.put(name, reIndent(code)));
    }

    private String reIndent(String code) {
        int startIdx = code.indexOf('{');
        int endIdx = code.lastIndexOf('}');

        String insideBraces = code.substring(startIdx + 1, endIdx - 2);
        return StringUtils.stripIndentation(insideBraces);
    }
}
